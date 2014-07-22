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

public class InformixSessionSnmp extends SnmpMonitor {
	
	public InformixSessionSnmp() {
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
			ArrayList sessionList = new ArrayList();// ��־�ļ���Ϣ
			InformixJdbcUtil util = null;
			try {
				String passwords = EncryptUtil.decode(dbmonitorlist.getPassword());
				String dburl = "jdbc:informix-sqli://" + serverip + ":" + port + "/"
				+ "sysmaster" + ":INFORMIXSERVER=" + dbservername + "; user="
				+ username + ";password=" + passwords; // myDBΪ���ݿ���
				util = new InformixJdbcUtil(dburl, username, passwords);
				util.jdbc();

				// *********************************ȡ���� start
				String sqlLog = "select username, hostname,connected,(isreads+bufreads+bufwrites+pagreads+pagwrites) access,lockreqs,locksheld,lockwts,deadlks,lktouts,logrecs,longtxs,bufreads,bufwrites,seqscans,pagreads,pagwrites,total_sorts,dsksorts,max_sortdiskspace from syssessions s, syssesprof f where s.sid =f.sid";
				ResultSet rs = null;
				try {
					// ��־�ļ�
					rs = util.stmt.executeQuery(sqlLog);
					while (rs.next()) {
						Hashtable informixsession = new Hashtable();
						informixsession.put("username", rs
								.getString("username"));// �û���
						informixsession.put("hostname", rs
								.getString("hostname"));// ����
						informixsession.put("connected", rs
								.getString("connected"));// �û����ӵ����ݿ�������ϵ�ʱ��
						informixsession.put("access", rs.getString("access"));// ���д���
						informixsession.put("lockreqs", rs.getInt("lockreqs"));// ��������������
						informixsession
								.put("locksheld", rs.getInt("locksheld"));// ��ǰ������������
						informixsession.put("lockwts", rs.getInt("lockwts"));// �ȴ����Ĵ���
						informixsession.put("deadlks", rs.getInt("deadlks"));// ��⵽����������
						informixsession.put("lktouts", rs.getInt("lktouts"));// ������ʱ��
						/*
						 * informixsession.put("logrecs",
						 * rs.getInt("logrecs").trim());//��д���߼���־�ļ�¼��
						 * informixsession.put("longtxs",
						 * rs.getInt("longtxs").trim());//��������
						 */
						informixsession.put("bufreads", rs.getInt("bufreads"));// ��������ȡ��
						informixsession
								.put("bufwrites", rs.getInt("bufwrites"));// ������д����
						// informixsession.put("seqscans",
						// rs.getInt("seqscans").trim());//˳��ɨ����
						informixsession.put("pagreads", rs.getInt("pagreads"));// ҳ��ȡ��
						informixsession
								.put("pagwrites", rs.getInt("pagwrites"));// ҳд����
						/*
						 * informixsession.put("total_sorts",
						 * rs.getInt("total_sorts").trim());//��������
						 * informixsession.put("dsksorts",
						 * rs.getInt("dsksorts").trim());//���ʺ��ڴ��������
						 * informixsession.put("max_sortdiskspace",
						 * rs.getInt("max_sortdiskspace").trim());//������ʹ�õ����ռ�
						 */
						sessionList.add(informixsession);
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
			returndata.put("sessionList", sessionList);// ��־��Ϣ
			// �����ڴ�
			Hashtable informixData = new Hashtable();
			informixData.put("informix", returndata);
			monitorValue.put(dbnames, informixData);
//			 �����ڴ�
			if (!(ShareData.getInformixmonitordata().containsKey(serverip))) {
				ShareData.setInfomixmonitordata(serverip, monitorValue);
			} else {
				Hashtable informixHash = (Hashtable) ShareData.getInformixmonitordata().get(serverip);
				((Hashtable)((Hashtable)informixHash.get(dbnames)).get("informix")).put("sessionList", returndata.get("sessionList"));
			}
			// ----------------------------------���浽���ݿ⼰�澯 start
			List sessionlist = (ArrayList)returndata.get("sessionList");
			if(sessionlist != null && sessionlist.size()>0){

				String hex = IpTranslation.formIpToHex(dbmonitorlist.getIpAddress());
				serverip = hex + ":" + dbnames;
				try {
					Calendar tempCal = Calendar.getInstance();
					Date cc = tempCal.getTime();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String montime = sdf.format(cc);

					String deletesql = "delete from nms_informixsession where serverip='"+serverip+"'";
					GathersqlListManager.Addsql(deletesql);
					String insertsql = null;
					for(int i=0;i<sessionlist.size();i++){
						Hashtable session = (Hashtable)sessionList.get(i);
						if(session != null && session.size()>0){
							try {
								StringBuffer sBuffer = new StringBuffer();
								sBuffer
										.append("insert into nms_informixsession(serverip, bufwrites, pagwrites,pagreads,locksheld ,bufreads,accesses,");
								sBuffer
										.append("connected,username,lktouts,lockreqs,hostname,lockwts,deadlks,mon_time)");
								sBuffer.append(" values('");
								sBuffer.append(serverip);
								sBuffer.append("','");
								sBuffer.append(String.valueOf(session.get("bufwrites")));
								sBuffer.append("','");
								sBuffer.append(String.valueOf(session.get("pagwrites")));
								sBuffer.append("','");
								sBuffer.append(String.valueOf(session.get("pagreads")));
								sBuffer.append("','");
								sBuffer.append(String.valueOf(session.get("locksheld")));
								sBuffer.append("','");
								sBuffer.append(String.valueOf(session.get("bufreads")));
								sBuffer.append("','");
								sBuffer.append(String.valueOf(session.get("access")));
								sBuffer.append("','");
								sBuffer.append(String.valueOf(session.get("connected")));
								sBuffer.append("','");
								sBuffer.append(String.valueOf(session.get("username")));
								sBuffer.append("','");
								sBuffer.append(String.valueOf(session.get("lktouts")));
								sBuffer.append("','");
								sBuffer.append(String.valueOf(session.get("lockreqs")));
								sBuffer.append("','");
								sBuffer.append(String.valueOf(session.get("hostname")));
								sBuffer.append("','");
								sBuffer.append(String.valueOf(session.get("lockwts")));
								sBuffer.append("','");
								sBuffer.append(String.valueOf(session.get("deadlks")));
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
