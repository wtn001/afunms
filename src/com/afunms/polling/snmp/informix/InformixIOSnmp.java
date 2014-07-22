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

public class InformixIOSnmp extends SnmpMonitor {
	
	public InformixIOSnmp() {
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
			ArrayList iolist = new ArrayList();// ��־�ļ���Ϣ
			InformixJdbcUtil util = null;
			try {
				String passwords = EncryptUtil.decode(dbmonitorlist.getPassword());
				String dburl = "jdbc:informix-sqli://" + serverip + ":" + port + "/"
				+ "sysmaster" + ":INFORMIXSERVER=" + dbservername + "; user="
				+ username + ";password=" + passwords; // myDBΪ���ݿ���
				util = new InformixJdbcUtil(dburl, username, passwords);
				util.jdbc();

				// *********************************ȡ���� start
				String sqlLog = "select * from syschkio";
				ResultSet rs = null;
				try {
					// ��־�ļ�
					rs = util.stmt.executeQuery(sqlLog);
					while (rs.next()) {
						Hashtable informixio = new Hashtable();
						informixio.put("chunknum", rs.getString("chunknum"));// ����
						informixio.put("reads", rs.getInt("reads"));// �����ȡ��
						informixio.put("pagesread", rs.getInt("pagesread"));// ��ȡ��ҳ��
						informixio.put("writes", rs.getInt("writes"));// ����д����
						informixio.put("pageswritten", rs
								.getInt("pageswritten"));// д���ҳ��
						informixio.put("mreads", rs.getInt("mreads"));// �����ȡ��������
						informixio.put("mpagesread", rs.getInt("mpagesread"));// ��ȡ�����񣩵�ҳ��
						informixio.put("mwrites", rs.getInt("mwrites"));// ����д�루������
						informixio.put("mpageswritten", rs
								.getInt("mpageswritten"));// д�루���񣩵�ҳ��
						// SysLogger.info("pagesread ====
						// "+informixio.get("pagesread"));
						// SysLogger.info("pageswritten ====
						// "+informixio.get("pageswritten"));
						iolist.add(informixio);
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
			returndata.put("iolist", iolist);// ��־��Ϣ
			// �����ڴ�
			Hashtable informixData = new Hashtable();
			informixData.put("informix", returndata);
			monitorValue.put(dbnames, informixData);
//			 �����ڴ�
			if (!(ShareData.getInformixmonitordata().containsKey(serverip))) {
				ShareData.setInfomixmonitordata(serverip, monitorValue);
			} else {
				Hashtable informixHash = (Hashtable) ShareData.getInformixmonitordata().get(serverip);
				((Hashtable)((Hashtable)informixHash.get(dbnames)).get("informix")).put("iolist", returndata.get("iolist"));
			}
			// ----------------------------------���浽���ݿ⼰�澯 start
			List ioList = (ArrayList)returndata.get("iolist");
			if(ioList != null && ioList.size()>0){

				String hex = IpTranslation.formIpToHex(dbmonitorlist.getIpAddress());
				serverip = hex + ":" + dbnames;
				try {
					Calendar tempCal = Calendar.getInstance();
					Date cc = tempCal.getTime();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String montime = sdf.format(cc);

					String deletesql = "delete from nms_informixio where serverip='"+serverip+"'";
					GathersqlListManager.Addsql(deletesql);
					String insertsql = null;
					for(int i=0;i<ioList.size();i++){
						Hashtable io = (Hashtable)ioList.get(i);
						if(io != null && io.size()>0){
							try {
								StringBuffer sBuffer = new StringBuffer();
								sBuffer
										.append("insert into nms_informixio(serverip, pagesread,readsstr,writes,mwrites,chunknum,mreads,pageswritten,mpagesread,mpageswritten,mon_time)");
								sBuffer.append(" values('");
								sBuffer.append(serverip);
								sBuffer.append("','");
								sBuffer.append(String.valueOf(io.get("pagesread")));
								sBuffer.append("','");
								sBuffer.append(String.valueOf(io.get("reads")));
								sBuffer.append("','");
								sBuffer.append(String.valueOf(io.get("writes")));
								sBuffer.append("','");
								sBuffer.append(String.valueOf(io.get("mwrites")));
								sBuffer.append("','");
								sBuffer.append(String.valueOf(io.get("chunknum")));
								sBuffer.append("','");
								sBuffer.append(String.valueOf(io.get("mreads")));
								sBuffer.append("','");
								sBuffer.append(String.valueOf(io.get("pageswritten")));
								sBuffer.append("','");
								sBuffer.append(String.valueOf(io.get("mpagesread")));
								sBuffer.append("','");
								sBuffer.append(String.valueOf(io.get("mpageswritten")));
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
