package com.afunms.polling.snmp.oracle;

import java.sql.ResultSet;
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
 * Oracle wait �ɼ� ʹ��JDBC�ɼ�
 * 
 * @author wupinlong 2012/09/17
 * 
 */
public class OracleWaitSnmp extends SnmpMonitor {

	public OracleWaitSnmp() {
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

				// **********ȡ���� start
				String sqlWait = "SELECT EVENT,SUM(DECODE(WAIT_TIME,0,0,1)) PREV,SUM(DECODE(WAIT_TIME,0,1,0)) CURR,COUNT(*) TOT FROM V$SESSION_WAIT GROUP BY EVENT ORDER BY 4";
				// WAIT״��
				Vector waits = new Vector();
				ResultSet rs = null;
				try {
					rs = util.stmt.executeQuery(sqlWait);
					while (rs.next()) {
						Hashtable return_value = new Hashtable();

						return_value.put("event", rs.getString("event")
								.toString());
						return_value.put("prev", rs.getString("prev")
								.toString());
						return_value.put("curr", rs.getString("curr")
								.toString());
						return_value.put("tot", rs.getString("tot").toString());

						waits.addElement(return_value);
						return_value = null;
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (rs != null)
						rs.close();
				}
				returndata.put("wait", waits);
				// ******************ȡ���� end
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
				oracleHash.put("wait", returndata.get("wait"));
			}

			// ----------------------------------���浽���ݿ⼰�澯 start
			Vector wait = (Vector) returndata.get("wait");
			if (wait != null && !wait.isEmpty()) {
				String hex = IpTranslation.formIpToHex(dbmonitorlist
						.getIpAddress());
				serverip = hex + ":" + dbmonitorlist.getId();

				try {
					Calendar tempCal = Calendar.getInstance();
					Date cc = tempCal.getTime();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String montime = sdf.format(cc);
					String deletesql = "delete from nms_orawait where serverip='"
							+ serverip + "'";
					// �������
					GathersqlListManager.Addsql(deletesql);

					String insertsql=null;
					for (int k = 0; k < wait.size(); k++) {
						Hashtable ht = (Hashtable) wait.get(k);
						String event = ht.get("event").toString();
						String prev = ht.get("prev").toString();
						String curr = ht.get("curr").toString();
						String tot = ht.get("tot").toString();
						insertsql="";
						insertsql = "insert into nms_orawait(serverip,event,prev,curr,tot,mon_time) "
								+ "values('"
								+ serverip
								+ "','"
								+ event
								+ "','"
								+ prev + "','" + curr + "','" + tot;
						if ("mysql".equalsIgnoreCase(SystemConstant.DBType)) {
							insertsql = insertsql + "','" + montime + "')";
						} else if ("oracle"
								.equalsIgnoreCase(SystemConstant.DBType)) {
							insertsql = insertsql + "',to_date('" + montime
									+ "','YYYY-MM-DD HH24:MI:SS'))";
						}
						GathersqlListManager.Addsql(insertsql);
					}

					// ---------------------------------wait����澯

				} catch (Exception e) {
					e.printStackTrace();
				}

			}
			// ----------------------------------���浽���ݿ⼰�澯 end

		}
		return returndata;
	}

}
