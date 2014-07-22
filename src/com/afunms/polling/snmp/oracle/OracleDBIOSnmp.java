package com.afunms.polling.snmp.oracle;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import com.afunms.application.model.DBVo;
import com.afunms.application.util.IpTranslation;
import com.afunms.common.util.EncryptUtil;
import com.afunms.common.util.OracleJdbcUtil;
import com.afunms.common.util.ShareData;
import com.afunms.common.util.SystemConstant;
import com.afunms.indicators.model.NodeGatherIndicators;
import com.afunms.monitor.executor.base.SnmpMonitor;
import com.gatherdb.GathersqlListManager;

/**
 * Oracle IO �ɼ� ʹ��JDBC�ɼ�
 * 
 * @author wupinlong 2012/09/17
 * 
 */
public class OracleDBIOSnmp extends SnmpMonitor {

	public OracleDBIOSnmp() {
	}

	@SuppressWarnings("unchecked")
	public Hashtable collect_Data(NodeGatherIndicators nodeGatherIndicators) {
		Hashtable returndata = new Hashtable();
		List dbmonitorlists = new ArrayList();
		dbmonitorlists = ShareData.getDBList();
		DBVo dbmonitorlist = new DBVo();
		if (dbmonitorlists != null && dbmonitorlists.size() > 0) {
			for (int i = 0; i < dbmonitorlists.size(); i++) {
				DBVo vo = (DBVo) dbmonitorlists.get(i);
				if (vo.getId() == Integer.parseInt(nodeGatherIndicators
						.getNodeid())) {
					dbmonitorlist = vo;
					break;
				}
			}
		}
		if (dbmonitorlist != null) {
			if (dbmonitorlist.getManaged() == 0) {
				// ���δ���������ɼ���user��ϢΪ��
				return returndata;
			}
			String serverip = dbmonitorlist.getIpAddress();
			int port = Integer.parseInt(dbmonitorlist.getPort());

			OracleJdbcUtil util = null;
			try {
				String dburl = "jdbc:oracle:thin:@" + serverip + ":" + port
						+ ":" + dbmonitorlist.getDbName();
				util = new OracleJdbcUtil(dburl, dbmonitorlist.getUser(),
						EncryptUtil.decode(dbmonitorlist.getPassword()));
				util.jdbc();

				// *********************************ȡ���� start
				// ���ݿ�I/O״��
				Hashtable dbio = new Hashtable();
				String sqldbio = "SELECT DF.TABLESPACE_NAME NAME,DF.FILE_NAME FILENAME,F.PHYRDS PYR,"
						+ "F.PHYBLKRD PBR,F.PHYWRTS PYW, F.PHYBLKWRT PBW FROM V$FILESTAT F, DBA_DATA_FILES DF WHERE F.FILE# = DF.FILE_ID "
						+ " ORDER BY DF.TABLESPACE_NAME";
				ResultSet rs = null;
				try {
					rs = util.stmt.executeQuery(sqldbio);
					while (rs.next()) {
						Hashtable return_value = new Hashtable();

						return_value.put("name", rs.getString("NAME")
								.toString());
						return_value.put("file", rs.getString("FILENAME")
								.toString());
						return_value.put("pyr", rs.getString("PYR").toString());
						return_value.put("pbr", rs.getString("PBR").toString());
						return_value.put("pyw", rs.getString("PYW").toString());
						return_value.put("pbw", rs.getString("PBW").toString());

						dbio.put(rs.getString("FILENAME").toString(),
								return_value);
						return_value = null;
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (rs != null)
						rs.close();
				}
				returndata.put("dbio", dbio);
				// *********************************ȡ���� end
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				util.closeStmt();
				util.closeConn();
			}

			// �����ڴ�
			if (!(ShareData.getSharedata().containsKey(dbmonitorlist
					.getIpAddress()
					+ ":" + dbmonitorlist.getId()))) {
				ShareData.getSharedata().put(
						dbmonitorlist.getIpAddress() + ":"
								+ dbmonitorlist.getId(), returndata);
			} else {
				Hashtable oracleHash = (Hashtable) ShareData.getSharedata()
						.get(
								dbmonitorlist.getIpAddress() + ":"
										+ dbmonitorlist.getId());
				oracleHash.put("dbio", returndata.get("dbio"));
			}

			// ----------------------------------���浽���ݿ⼰�澯 start
			Hashtable dbiohash = (Hashtable) returndata.get("dbio");
			if (dbiohash != null && !dbiohash.isEmpty()) {
				String hex = IpTranslation.formIpToHex(dbmonitorlist
						.getIpAddress());
				serverip = hex + ":" + dbmonitorlist.getId();

				try {
					Calendar tempCal = Calendar.getInstance();
					Date cc = tempCal.getTime();
					SimpleDateFormat sdf = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");
					String montime = sdf.format(cc);
					String deletesql = "delete from nms_oradbio where serverip='"
							+ serverip + "'";
					GathersqlListManager.Addsql(deletesql);

					String name = null;
					String pyr = null;
					String pbr = null;
					String pyw = null;
					String pbw = null;

					String insertsql = null;
					Iterator ipidIterator = dbiohash.keySet().iterator();
					while (ipidIterator.hasNext()) {
						String filename = (String) ipidIterator.next();
						name = "";
						pyr = "";
						pbr = "";
						pyw = "";
						pbw = "";
						if (dbiohash.containsKey(filename)) {
							Hashtable iodetail = (Hashtable) dbiohash.get(filename);
							if (iodetail != null && iodetail.size() > 0) {
								name = (String) iodetail.get("name");
								pyr = (String) iodetail.get("pyr");
								pbr = (String) iodetail.get("pbr");
								pyw = (String) iodetail.get("pyw");
								pbw = (String) iodetail.get("pbw");
							}
							filename = filename.replaceAll("\\\\", "/");
						}
						insertsql = "insert into nms_oradbio(serverip, name, filename, pyr, pbr, pyw, pbw,mon_time) "
								+ "values('"
								+ serverip
								+ "','"
								+ name
								+ "','"
								+ filename
								+ "','"
								+ pyr
								+ "','"
								+ pbr
								+ "','"
								+ pyw + "','" + pbw;
						if ("mysql".equalsIgnoreCase(SystemConstant.DBType)) {
							insertsql = insertsql + "','" + montime + "')";
						} else if ("oracle"
								.equalsIgnoreCase(SystemConstant.DBType)) {
							insertsql = insertsql + "',to_date('" + montime
									+ "','YYYY-MM-DD HH24:MI:SS'))";
						}
						GathersqlListManager.Addsql(insertsql);
					}
					// ---------------------------------dbio����澯

				} catch (Exception e) {
					e.printStackTrace();
				}

			}
			// ----------------------------------���浽���ݿ⼰�澯 end

		}
		return returndata;
	}

}
