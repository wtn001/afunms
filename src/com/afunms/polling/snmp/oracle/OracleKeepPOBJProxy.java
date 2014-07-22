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
import com.afunms.common.util.CommonUtil;
import com.afunms.common.util.EncryptUtil;
import com.afunms.common.util.OracleJdbcUtil;
import com.afunms.common.util.ShareData;
import com.afunms.common.util.SystemConstant;
import com.afunms.indicators.model.NodeGatherIndicators;
import com.afunms.monitor.executor.base.SnmpMonitor;
import com.gatherdb.GathersqlListManager;

/**
 * Oracle �̶�������� �ɼ� ʹ��JDBC�ɼ�
 * 
 * @author wupinlong 2012/09/17
 * 
 */
public class OracleKeepPOBJProxy extends SnmpMonitor {

	public OracleKeepPOBJProxy() {
	}

	@SuppressWarnings("unchecked")
	public Hashtable collect_Data(NodeGatherIndicators nodeGatherIndicators) {
		Hashtable returndata = new Hashtable();
		String htKey = "keepobj";
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
			String[] args = new String[]{"owner", "name", "db_link", "namespace", "type", "sharable_mem", "loads", "executions", "locks", "pins", "kept", "child_latch", "invalidations", "mon_time"};
			returndata = LogParser.parse(this, dbmonitorlist, htKey, args);

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
				oracleHash.put("keepObj_v", returndata.get(htKey));
			}

			// ----------------------------------���浽���ݿ⼰�澯 start
			Vector keepobj = (Vector) returndata.get(htKey);
			if (keepobj != null && keepobj.size() > 0) {
				String hex = IpTranslation.formIpToHex(dbmonitorlist
						.getIpAddress());
				serverip = hex + ":" + dbmonitorlist.getId();

				try {
					Calendar tempCal = Calendar.getInstance();
					Date cc = tempCal.getTime();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String montime = sdf.format(cc);

					String deletesql = "delete from nms_orakeepobj where serverip='"
							+ serverip + "'";
					GathersqlListManager.Addsql(deletesql);

					StringBuffer sBuffer = null;
					for (int k = 0; k < keepobj.size(); k++) {
						Hashtable ht = (Hashtable) keepobj.get(k);
						String owner = ht.get("owner").toString();
						String name = ht.get("name").toString();
						String db_link = ht.get("db_link").toString();
						String namespace = ht.get("namespace").toString();
						String type = ht.get("type").toString();
						String sharable_mem = ht.get("sharable_mem").toString();
						String loads = ht.get("loads").toString();
						String executions = ht.get("executions").toString();
						String locks = ht.get("locks").toString();
						String pins = ht.get("pins").toString();
						String kept = ht.get("kept").toString();
						String child_latch = ht.get("child_latch").toString();
						String invalidations = CommonUtil.getValue(ht,
								"invalidations", "--");
						sBuffer = new StringBuffer();
						sBuffer
								.append("insert into nms_orakeepobj(serverip, owner, name, db_link, namespace, type, sharable_mem, loads,"
										+ " executions, locks, pins, kept, child_latch, invalidations, mon_time) ");
						sBuffer.append(" values('");
						sBuffer.append(serverip);
						sBuffer.append("','");
						sBuffer.append(owner);
						sBuffer.append("','");
						sBuffer.append(name);
						sBuffer.append("','");
						sBuffer.append(db_link);
						sBuffer.append("','");
						sBuffer.append(namespace);
						sBuffer.append("','");
						sBuffer.append(type);
						sBuffer.append("','");
						sBuffer.append(sharable_mem);
						sBuffer.append("','");
						sBuffer.append(loads);
						sBuffer.append("','");
						sBuffer.append(executions);
						sBuffer.append("','");
						sBuffer.append(locks);
						sBuffer.append("','");
						sBuffer.append(pins);
						sBuffer.append("','");
						sBuffer.append(kept);
						sBuffer.append("','");
						sBuffer.append(child_latch);
						sBuffer.append("','");
						sBuffer.append(invalidations);
						if ("mysql".equalsIgnoreCase(SystemConstant.DBType)) {
							sBuffer.append("','");
							sBuffer.append(montime);
							sBuffer.append("')");
						} else if ("oracle"
								.equalsIgnoreCase(SystemConstant.DBType)) {
							sBuffer.append("',");
							sBuffer.append("to_date('" + montime
									+ "','YYYY-MM-DD HH24:MI:SS')");
							sBuffer.append(")");
						}
						GathersqlListManager.Addsql(sBuffer.toString());
					}
					// ---------------------------------keepobj����澯

				} catch (Exception e) {
					e.printStackTrace();
				}

			}
			// ----------------------------------���浽���ݿ⼰�澯 end

		}
		return returndata;
	}

}
