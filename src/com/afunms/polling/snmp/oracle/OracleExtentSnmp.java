package com.afunms.polling.snmp.oracle;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

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
 * Oracle Extent �ɼ� ʹ��JDBC�ɼ�
 * 
 * @author wupinlong 2012/09/17
 * 
 */
public class OracleExtentSnmp extends SnmpMonitor {

	public OracleExtentSnmp() {
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

				String sqlExtent = "select a.tablespace_name, sum(a.extents) from dba_segments a, dba_tablespaces b "
						+ "where a.tablespace_name = b.tablespace_name and b.extent_management = 'DICTIONARY' "
						+ "group by a.tablespace_name order by sum(a.extents)";
				ResultSet rs = null;
				// �ֵ�����ռ��е�Extent����
				Vector returnVal10 = new Vector();
				try {
					rs = util.stmt.executeQuery(sqlExtent);
					ResultSetMetaData rsmd10 = rs.getMetaData();
					while (rs.next()) {
						Hashtable return_value = new Hashtable();
						for (int i = 1; i <= rsmd10.getColumnCount(); i++) {
							String col = rsmd10.getColumnName(i);
							if (rs.getString(i) != null) {
								String tmp = rs.getString(i).toString();
								return_value.put(col.toLowerCase(), tmp);
							} else
								return_value.put(col.toLowerCase(), "--");
						}
						returnVal10.addElement(return_value);
						return_value = null;
					}
					returndata.put("extent", returnVal10);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					rs.close();
				}
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
				oracleHash.put("extent_v", returndata.get("extent"));
			}

			// ----------------------------------���浽���ݿ⼰�澯 start
			Vector extent_v = (Vector) returndata.get("extent");
			if (extent_v != null && !extent_v.isEmpty()) {
				String hex = IpTranslation.formIpToHex(dbmonitorlist
						.getIpAddress());
				serverip = hex + ":" + dbmonitorlist.getId();

				try {
					Calendar tempCal = Calendar.getInstance();
					Date cc = tempCal.getTime();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String montime = sdf.format(cc);

					String deletesql = "delete from nms_oraextent where serverip='"
							+ serverip + "'";
					GathersqlListManager.Addsql(deletesql);

					String tablespace_name = null;
					String extents = null;
					String insertsql = null;
					for (int k = 0; k < extent_v.size(); k++) {
						Hashtable ht = (Hashtable) extent_v.get(k);
						tablespace_name = ht.get("tablespace_name").toString();
						extents = ht.get("sum(a.extents)").toString();
						tablespace_name = tablespace_name.replaceAll("\\\\",
								"/");
						insertsql = "insert into nms_oraextent(serverip,tablespace_name,extents,mon_time) "
								+ "values('"
								+ serverip
								+ "','"
								+ tablespace_name + "','" + extents;
						if ("mysql".equalsIgnoreCase(SystemConstant.DBType)) {
							insertsql = insertsql + "','" + montime + "')";
						} else if ("oracle"
								.equalsIgnoreCase(SystemConstant.DBType)) {
							insertsql = insertsql + "',to_date('" + montime
									+ "','YYYY-MM-DD HH24:MI:SS'))";
						}
						GathersqlListManager.Addsql(insertsql);
					}
					// ---------------------------------extent����澯

				} catch (Exception e) {
					e.printStackTrace();
				}

			}
			// ----------------------------------���浽���ݿ⼰�澯 end

		}
		return returndata;
	}

}
