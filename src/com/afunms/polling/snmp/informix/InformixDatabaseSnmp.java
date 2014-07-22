package com.afunms.polling.snmp.informix;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import com.afunms.application.model.DBVo;
import com.afunms.application.util.IpTranslation;
import com.afunms.common.util.EncryptUtil;
import com.afunms.common.util.InformixJdbcUtil;
import com.afunms.common.util.ShareData;
import com.afunms.common.util.SystemConstant;
import com.afunms.indicators.model.NodeGatherIndicators;
import com.afunms.monitor.executor.base.SnmpMonitor;
import com.gatherdb.GathersqlListManager;

public class InformixDatabaseSnmp extends SnmpMonitor {
	
	public InformixDatabaseSnmp() {
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
			String createuser = "";// ������
			String createtime = "";// ����ʱ��
			String log = "";// ��־��¼�Ƿ�
			String bufflog = "";// ��־��¼�Ƿ��ѻ���
			String ansi = "";// �Ƿ����ansi
			String gls = "";// �Ƿ�����gls
			String serverip = dbmonitorlist.getIpAddress();
			String dbnames = dbmonitorlist.getDbName();
			String username = dbmonitorlist.getUser();
			int port = Integer.parseInt(dbmonitorlist.getPort());
			String dbservername = dbmonitorlist.getAlias();//��ʱ�ķ�������
			ArrayList databaselist = new ArrayList();// ��־�ļ���Ϣ
			String dbname = dbnames; // ���ݿ�����
			String dbserver = dbservername;// ��Ӧ�ķ�������
			InformixJdbcUtil util = null;
			try {
				String passwords = EncryptUtil.decode(dbmonitorlist.getPassword());
				String dburl = "jdbc:informix-sqli://" + serverip + ":" + port + "/"
				+ "sysmaster" + ":INFORMIXSERVER=" + dbservername + "; user="
				+ username + ";password=" + passwords; // myDBΪ���ݿ���
				util = new InformixJdbcUtil(dburl, username, passwords);
				util.jdbc();

				// *********************************ȡ���� start
				String sqlLog = "select * from sysdatabases where name='" + dbnames + "'";
				ResultSet rs = null;
				try {
					// ��־�ļ�
					rs = util.stmt.executeQuery(sqlLog);
					while (rs.next()) {
						Hashtable databaseVal = new Hashtable();
						createuser = rs.getString("owner");// ������
						createtime = rs.getString("created");// ����ʱ��
						if (rs.getInt("is_logging") == 1) {
							log = "��־��¼�ǻ��";// ��־��¼�Ƿ�
						} else {
							log = "��־��¼�ǲ����";
						}
						if (rs.getInt("is_buff_log") == 1) {
							bufflog = "��־��¼���ѻ���";
						} else {
							bufflog = "��־��¼δ����";
						}
						if (rs.getInt("is_ansi") == 1) {
							ansi = "����ansi";
						} else {
							ansi = "������ansi";
						}
						if (rs.getInt("is_nls") == 1) {
							gls = "������gls";
						} else {
							gls = "û������gls";
						}
						databaseVal.put("dbname", dbname);
						databaseVal.put("dbserver", dbserver);
						databaseVal.put("createuser", createuser);
						databaseVal.put("createtime", createtime);
						databaseVal.put("log", log);
						databaseVal.put("bufflog", bufflog);
						databaseVal.put("ansi", ansi);
						databaseVal.put("gls", gls);
						databaselist.add(databaseVal);
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
			returndata.put("databaselist", databaselist);// ��־��Ϣ
			// �����ڴ�
			Hashtable informixData = new Hashtable();
			informixData.put("informix", returndata);
			monitorValue.put(dbnames, informixData);
//			 �����ڴ�
			if (!(ShareData.getInformixmonitordata().containsKey(serverip))) {
				ShareData.setInfomixmonitordata(serverip, monitorValue);
			} else {
				Hashtable informixHash = (Hashtable) ShareData.getInformixmonitordata().get(serverip);
				((Hashtable)((Hashtable)informixHash.get(dbnames)).get("informix")).put("databaselist", returndata.get("databaselist"));
			}
			// ----------------------------------���浽���ݿ⼰�澯 start
			ArrayList databaseList = (ArrayList)returndata.get("databaselist");
			if(databaseList != null && databaseList.size()>0){

				String hex = IpTranslation.formIpToHex(dbmonitorlist.getIpAddress());
				serverip = hex + ":" + dbnames;
				try {
					Calendar tempCal = Calendar.getInstance();
					Date cc = tempCal.getTime();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String montime = sdf.format(cc);

					String deletesql = "delete from nms_informixdatabase where serverip='"+serverip+"'";
					GathersqlListManager.Addsql(deletesql);
					String insertsql = null;
					for(int i=0;i<databaseList.size();i++){
						Hashtable database = (Hashtable)databaseList.get(i);
						if(database != null && database.size()>0){
							try {
								StringBuffer sBuffer = new StringBuffer();
								sBuffer
										.append("insert into nms_informixdatabase(serverip, bufflog, createtime,log,dbserver ,gls,createuser,ansi,dbname,mon_time)");
								sBuffer.append(" values('");
								sBuffer.append(serverip);
								sBuffer.append("','");
								sBuffer.append(String.valueOf(database.get("bufflog")));
								sBuffer.append("','");
								sBuffer.append(String.valueOf(database.get("createtime")));
								sBuffer.append("','");
								sBuffer.append(String.valueOf(database.get("log")));
								sBuffer.append("','");
								sBuffer.append(String.valueOf(database.get("dbserver")));
								sBuffer.append("','");
								sBuffer.append(String.valueOf(database.get("gls")));
								sBuffer.append("','");
								sBuffer.append(String.valueOf(database.get("createuser")));
								sBuffer.append("','");
								sBuffer.append(String.valueOf(database.get("ansi")));
								sBuffer.append("','");
								sBuffer.append(String.valueOf(database.get("dbname")));
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
