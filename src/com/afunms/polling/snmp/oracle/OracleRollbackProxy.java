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
 * Oracle �ع�����Ϣ �ɼ� ʹ��JDBC�ɼ�
 * 
 * @author wupinlong 2012/09/17
 * 
 */
public class OracleRollbackProxy extends SnmpMonitor {

	public OracleRollbackProxy() {
	}

	@SuppressWarnings("unchecked")
	public Hashtable collect_Data(NodeGatherIndicators nodeGatherIndicators) {
		Hashtable returndata = new Hashtable();
		String htKey = "rollback";
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
			String[] args = new String[]{"rollback_segment","wraps","shrinks","average_shrink","extends","mon_time"};
			returndata = LogParser.parse(this, dbmonitorlist, htKey, args);

			// �����ڴ�
			if (!ShareData.getSharedata().containsKey(
					dbmonitorlist.getIpAddress() + ":" + dbmonitorlist.getId())) {
				ShareData.getSharedata().put(
						dbmonitorlist.getIpAddress() + ":"
								+ dbmonitorlist.getId(), returndata);
			} else {
				Hashtable oracleHash = (Hashtable) ShareData.getSharedata()
						.get(
								dbmonitorlist.getIpAddress() + ":"
										+ dbmonitorlist.getId());
				oracleHash.put("rollbackinfo_v", returndata.get(htKey));
			}

			// ----------------------------------���浽���ݿ⼰�澯 start
			Vector rollbackinfo_v = (Vector) returndata.get(htKey);
			if (rollbackinfo_v != null && !rollbackinfo_v.isEmpty()) {
				String hex = IpTranslation.formIpToHex(dbmonitorlist
						.getIpAddress());
				serverip = hex + ":" + dbmonitorlist.getId();

				try {
					Calendar tempCal = Calendar.getInstance();
					Date cc = tempCal.getTime();
					SimpleDateFormat sdf = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");
					String montime = sdf.format(cc);

					String deletesql = "delete from nms_orarollback where serverip='"
							+ serverip + "'";
					GathersqlListManager.Addsql(deletesql);

					String insertsql = null;
					for (int k = 0; k < rollbackinfo_v.size(); k++) {
						Hashtable ht = (Hashtable) rollbackinfo_v.get(k);
						String rollback = ht.get("rollback_segment").toString()
								.trim();
						String wraps = ht.get("wraps").toString();
						String shrink = ht.get("shrinks").toString();
						String ashrink = ht.get("average_shrink").toString();
						String extend = ht.get("extends").toString();
						insertsql = "";
						insertsql = "insert into nms_orarollback(serverip,rollback,wraps,shrink,ashrink,extend,mon_time) "
								+ "values('"
								+ serverip
								+ "','"
								+ rollback
								+ "','"
								+ wraps
								+ "','"
								+ shrink
								+ "','"
								+ ashrink + "','" + extend;
						if ("mysql".equalsIgnoreCase(SystemConstant.DBType)) {
							insertsql = insertsql + "','" + montime + "')";
						} else if ("oracle"
								.equalsIgnoreCase(SystemConstant.DBType)) {
							insertsql = insertsql + "',to_date('" + montime
									+ "','YYYY-MM-DD HH24:MI:SS'))";
						}
						GathersqlListManager.Addsql(insertsql);
					}
					// ---------------------------------rollback����澯

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			// ----------------------------------���浽���ݿ⼰�澯 end

		}
		return returndata;
	}

}
