package com.afunms.polling.snmp.informix;

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
import com.afunms.common.util.InformixJdbcUtil;
import com.afunms.common.util.OracleJdbcUtil;
import com.afunms.common.util.ShareData;
import com.afunms.common.util.SystemConstant;
import com.afunms.indicators.model.NodeGatherIndicators;
import com.afunms.monitor.executor.base.SnmpMonitor;
import com.gatherdb.GathersqlListManager;

/**
 * Oracle ��־�ļ���Ϣ �ɼ� ʹ��JDBC�ɼ�
 * 
 * @author wupinlong 2012/09/17
 * 
 */
public class InformixLogSnmp extends SnmpMonitor {
	
	public InformixLogSnmp() {
	}

	@SuppressWarnings("unchecked")
	public Hashtable collect_Data(NodeGatherIndicators nodeGatherIndicators) {
		Hashtable returndata = new Hashtable();
		List dbmonitorlists = new ArrayList();
		Hashtable monitorValue = new Hashtable();
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
			String dbnames = dbmonitorlist.getDbName();
			String username = dbmonitorlist.getUser();
			int port = Integer.parseInt(dbmonitorlist.getPort());
			String dbservername = dbmonitorlist.getAlias();//��ʱ�ķ�������
			ArrayList loglist = new ArrayList();// ��־�ļ���Ϣ
			InformixJdbcUtil util = null;
			try {
				String passwords = EncryptUtil.decode(dbmonitorlist.getPassword());
				String dburl = "jdbc:informix-sqli://" + serverip + ":" + port + "/"
				+ "sysmaster" + ":INFORMIXSERVER=" + dbservername + "; user="
				+ username + ";password=" + passwords; // myDBΪ���ݿ���
				util = new InformixJdbcUtil(dburl, username, passwords);
				util.jdbc();

				// *********************************ȡ���� start
				String sqlLog = "select * from syslogs";
				ResultSet rs = null;
				try {
					// ��־�ļ�
					rs = util.stmt.executeQuery(sqlLog);
					int uniqid = 0;// ��־�ļ���ʾ
					int size = 0;// ��־�ļ���ҳ��
					int used = 0;// ��־�ļ������õ�ҳ��
					int is_used = 0;// �Ƿ�ʹ�ã���ǰ״̬��
					int is_current = 0;// �Ƿ��ǵ�ǰ�ļ�
					int is_backed_up = 0;// �Ƿ��Ѿ����ݹ�
					int is_archived = 0;// �Ƿ������ڱ��ݴ�����
					int is_temp = 0;// �Ƿ�Ϊ��ʱ��־�ļ�
					while (rs.next()) {
						Hashtable informixlog = new Hashtable();
						try {
							uniqid = rs.getInt("uniqid");
							size = rs.getInt("size");
							used = rs.getInt("used");
							is_used = rs.getInt("is_used");
							is_current = rs.getInt("is_current");
							is_backed_up = rs.getInt("is_backed_up");
							is_archived = rs.getInt("is_archived");
							is_temp = rs.getInt("is_temp");
							informixlog.put("uniqid", uniqid);
							informixlog.put("size", size);
							informixlog.put("used", used);
							if (is_used == 1) {
								informixlog.put("is_used", "true");
							} else {
								informixlog.put("is_used", "false");
							}
							if (is_current == 1) {
								informixlog.put("is_current", "true");
							} else {
								informixlog.put("is_current", "false");
							}
							if (is_backed_up == 1) {
								informixlog.put("is_backed_up", "true");
							} else {
								informixlog.put("is_backed_up", "false");
							}
							if (is_archived == 1) {
								informixlog.put("is_archived", "true");
							} else {
								informixlog.put("is_archived", "false");
							}
							if (is_temp == 1) {
								informixlog.put("is_temp", "true");
							} else {
								informixlog.put("is_temp", "false");
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						loglist.add(informixlog);
					}

				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (rs != null)
						rs.close();
				}
				// *********************************ȡ���� end
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				util.closeStmt();
				util.closeConn();
			}
			returndata.put("informixlog", loglist);// ��־��Ϣ
			// �����ڴ�
			Hashtable informixData = new Hashtable();
			informixData.put("informix", returndata);
			monitorValue.put(dbnames, informixData);
//			 �����ڴ�
			if (!(ShareData.getInformixmonitordata().containsKey(serverip))) {
				ShareData.setInfomixmonitordata(serverip, monitorValue);
			} else {
				Hashtable informixHash = (Hashtable) ShareData.getInformixmonitordata().get(serverip);
				((Hashtable)((Hashtable)informixHash.get(dbnames)).get("informix")).put("informixlog", returndata.get("informixlog"));
			}
			// ----------------------------------���浽���ݿ⼰�澯 start
			List informixlog = (ArrayList)returndata.get("informixlog");
			if(informixlog != null && informixlog.size()>0){

				String hex = IpTranslation.formIpToHex(dbmonitorlist.getIpAddress());
				serverip = hex + ":" + dbnames;

				try {
					Calendar tempCal = Calendar.getInstance();
					Date cc = tempCal.getTime();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String montime = sdf.format(cc);

					String deletesql = "delete from nms_informixlog where serverip='"+serverip+"'";
					GathersqlListManager.Addsql(deletesql);
					String insertsql = null;
					for(int i=0;i<informixlog.size();i++){
						Hashtable log = (Hashtable)informixlog.get(i);
						if(log != null && log.size()>0){
							try {
								StringBuffer sBuffer = new StringBuffer();
								sBuffer.append("insert into nms_informixlog(serverip, is_backed_up, is_current,sizes,used ,is_temp,uniqid,is_archived,is_used,mon_time)");
								sBuffer.append(" values('");
								sBuffer.append(serverip);
								sBuffer.append("','");
								sBuffer.append(String.valueOf(log.get("is_backed_up")));
								sBuffer.append("','");
								sBuffer.append(String.valueOf(log.get("is_current")));
								sBuffer.append("','");
								sBuffer.append(String.valueOf(log.get("size")));
								sBuffer.append("','");
								sBuffer.append(String.valueOf(log.get("used")));
								sBuffer.append("','");
								sBuffer.append(String.valueOf(log.get("is_temp")));
								sBuffer.append("','");
								sBuffer.append(String.valueOf(log.get("uniqid")));
								sBuffer.append("','");
								sBuffer.append(String.valueOf(log.get("is_archived")));
								sBuffer.append("','");
								sBuffer.append(String.valueOf(log.get("is_used")));
								// sBuffer.append("','");
								// sBuffer.append(montime);
								// sBuffer.append("')");
								if (SystemConstant.DBType.equals("mysql")) {
									sBuffer.append("','");
									sBuffer.append(montime);
									sBuffer.append("')");
								} else if (SystemConstant.DBType.equals("oracle")) {
									sBuffer.append("',to_date('" + montime
											+ "','yyyy-mm-dd hh24:mi:ss'))");
								}
								insertsql = sBuffer.toString();
								GathersqlListManager.Addsql(insertsql);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
					// ---------------------------------log����澯

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			// ----------------------------------���浽���ݿ⼰�澯 end
			
		}
		return returndata;
	}

}
