package com.afunms.polling.snmp.informix;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.afunms.alarm.model.AlarmIndicatorsNode;
import com.afunms.alarm.util.AlarmIndicatorsUtil;
import com.afunms.application.dao.InformixspaceconfigDao;
import com.afunms.application.model.DBVo;
import com.afunms.application.model.Informixspaceconfig;
import com.afunms.application.util.IpTranslation;
import com.afunms.common.util.CheckEventUtil;
import com.afunms.common.util.DBManager;
import com.afunms.common.util.EncryptUtil;
import com.afunms.common.util.InformixJdbcUtil;
import com.afunms.common.util.ShareData;
import com.afunms.common.util.SystemConstant;
import com.afunms.indicators.model.NodeDTO;
import com.afunms.indicators.model.NodeGatherIndicators;
import com.afunms.indicators.util.NodeUtil;
import com.afunms.monitor.executor.base.SnmpMonitor;
import com.gatherdb.GathersqlListManager;

/**
 * Oracle ��ռ���Ϣ �ɼ� ʹ��JDBC�ɼ�
 * 
 * @author wupinlong 2012/09/17
 * 
 */
public class InformixTableSpaceSnmp extends SnmpMonitor {

	public InformixTableSpaceSnmp() {
	}

	@SuppressWarnings("unchecked")
	public Hashtable collect_Data(NodeGatherIndicators nodeGatherIndicators) {
		Hashtable returndata = new Hashtable();
		Hashtable monitorValue = new Hashtable();
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
			String username = dbmonitorlist.getUser();
			int port = Integer.parseInt(dbmonitorlist.getPort());
			String dbnames = dbmonitorlist.getDbName();
			String dbservername = dbmonitorlist.getAlias();//��ʱ�ķ�������
			ArrayList spaceList = new ArrayList();// �ռ���Ϣ
			InformixJdbcUtil util = null;
			try {
				String passwords = EncryptUtil.decode(dbmonitorlist.getPassword());
				String dburl = "jdbc:informix-sqli://" + serverip + ":" + port + "/"
				+ "sysmaster" + ":INFORMIXSERVER=" + dbservername + "; user="
				+ username + ";password=" + passwords; // myDBΪ���ݿ���
				util = new InformixJdbcUtil(dburl, username, passwords);
				util.jdbc();
				// *********************************ȡ���� start
				ResultSet rs = null;
				try {
					// ��ȡ��ռ���Ϣ
					String sqlTablespace = "select d.name[1,8] dbspace,d.owner,c.fname,sum(c.chksize) Pages_size,sum(c.chksize) - sum(c.nfree) Pages_used,sum(c.nfree) Pages_free,round ((sum(c.nfree)) / (sum(c.chksize)) * 100, 2) percent_free from      sysdbspaces d, syschunks c where d.dbsnum = c.dbsnum group by 1,2,3 order by 1";
					Vector returnVal2 = new Vector();
					Map<String, Integer> names = new HashMap<String, Integer>();
					rs = util.stmt.executeQuery(sqlTablespace);
					while (rs.next()) {
						Hashtable informixspaces = new Hashtable();
						String name = rs.getString("dbspace").trim();
						// if (names.get(name) == null) {
						informixspaces.put("dbspace", name);// �ռ���
						informixspaces.put("owner", rs.getString("owner")
								.trim());// �ռ��������
						String fname = rs.getString("fname").trim();
						// informixspaces.put("fname", );//�ÿ��ļ���·��
						String page_size = rs.getString("pages_size").trim();
						float p_size = 0;
						if (page_size != null && !page_size.equals(""))
							p_size = Float.parseFloat(page_size);
						informixspaces.put("pages_size", String
								.valueOf(p_size / 1024));// �ռ��С
						String page_used = rs.getString("pages_used").trim();
						float p_used = 0;
						if (page_used != null && !"".equals(page_used))
							p_used = Float.parseFloat(page_used);
						informixspaces.put("pages_used", String
								.valueOf(p_used / 1024));// ��ʹ�ÿռ�
						String page_free = rs.getString("pages_free").trim();
						float p_free = 0;
						if (page_free != null && !"".equals(page_free))
							p_free = Float.parseFloat(page_free);
						informixspaces.put("pages_free", String
								.valueOf(p_free / 1024));// ���пռ�

						String percent_free = rs.getString("percent_free")
								.trim();// ���аٷֱ�
						informixspaces.put("percent_free", percent_free);// ��ʹ�ÿռ�ٷֱ�
						int len = 0;
						if (fname.lastIndexOf("/") != -1) {
							len = fname.lastIndexOf("/");
						} else {
							len = fname.length();
						}
						informixspaces.put("fname", fname);
						if (len != -1) {
							String tpath = fname.substring(0, len);
							informixspaces.put("file_name", tpath);
						}
						spaceList.add(informixspaces);
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
			returndata.put("informixspaces", spaceList);// �ռ���Ϣ
			Hashtable informixData = new Hashtable();
			informixData.put("informix", returndata);
			monitorValue.put(dbnames, informixData);
//			 �����ڴ�
			if (!(ShareData.getInformixmonitordata().containsKey(serverip))) {
				ShareData.setInfomixmonitordata(serverip, monitorValue);
			} else {
				Hashtable informixHash = (Hashtable) ShareData.getInformixmonitordata().get(serverip);
				((Hashtable)((Hashtable)informixHash.get(dbnames)).get("informix")).put("informixspaces", returndata.get("informixspaces"));
			}
			
			// ----------------------------------���浽���ݿ⼰�澯 start
			spaceList = (ArrayList)returndata.get("informixspaces");
			if (spaceList != null && spaceList.size()>0) {
				String hex = IpTranslation.formIpToHex(dbmonitorlist.getIpAddress());
				serverip = hex + ":" + dbnames;

				try {
					Calendar tempCal = Calendar.getInstance();
					Date cc = tempCal.getTime();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String montime = sdf.format(cc);

					String deletesql = "delete from nms_informixspace where serverip='" + serverip + "'";
					GathersqlListManager.Addsql(deletesql);

					String insertsql = null;
					for(int i=0;i<spaceList.size();i++){
						Hashtable space = (Hashtable)spaceList.get(i);
						if(space != null && space.size()>0){
							DBManager dbmanager = new DBManager();
							try {
								StringBuffer sBuffer = new StringBuffer();
								sBuffer.append("insert into nms_informixspace(serverip, owner,pages_free,dbspace,pages_size,pages_used,file_name,fname,percent_free,mon_time)");
								sBuffer.append(" values('");
								sBuffer.append(serverip);
								sBuffer.append("','");
								sBuffer.append(String.valueOf(space.get("owner")));
								sBuffer.append("','");
								sBuffer.append(String.valueOf(space.get("pages_free")));
								sBuffer.append("','");
								sBuffer.append(String.valueOf(space.get("dbspace")));
								sBuffer.append("','");
								sBuffer.append(String.valueOf(space.get("pages_size")));
								sBuffer.append("','");
								sBuffer.append(String.valueOf(space.get("pages_used")));
								sBuffer.append("','");
								sBuffer.append(String.valueOf(space.get("file_name")).replaceAll(
										"\\\\", "/"));
								sBuffer.append("','");
								sBuffer.append(String.valueOf(space.get("fname")).replaceAll(
										"\\\\", "/"));
								sBuffer.append("','");
								sBuffer.append(String.valueOf(space.get("percent_free")));
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
								// System.out.println(sBuffer.toString());
								insertsql = sBuffer.toString();
								GathersqlListManager.Addsql(insertsql);
							} catch (Exception e) {
								e.printStackTrace();
							} finally {
								dbmanager.close();
							}
						}
					}
					// *****************tablespace�澯 start
					try {
						NodeUtil nodeUtil = new NodeUtil();
						NodeDTO nodeDTO = nodeUtil
								.conversionToNodeDTO(dbmonitorlist);
						// �ж��Ƿ���ڴ˸澯ָ��
						AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
						List list = alarmIndicatorsUtil
								.getAlarmInicatorsThresholdForNode(nodeDTO
										.getId()
										+ "", nodeDTO.getType(), nodeDTO
										.getSubtype());
						CheckEventUtil checkEventUtil = new CheckEventUtil();
//						System.out.println("informix�澯��ʼ-----list.size()===="+list.size());
						for (int i = 0; i < list.size(); i++) {
							AlarmIndicatorsNode alarmIndicatorsNode = (AlarmIndicatorsNode) list.get(i);
							if ("tablespace".equalsIgnoreCase(alarmIndicatorsNode.getName())) {
//								System.out.println("informix�澯��ʼ-----spaceList.size()===="+spaceList.size());
								if (spaceList != null && spaceList.size() > 0) {
									//�滻ԭ���ı�ռ�����		
									//SysLogger.info("add infromix space size====="+spaceList.size());
									ShareData.setInformixspacedata(serverip, spaceList);
									InformixspaceconfigDao informixspaceconfigdao = new InformixspaceconfigDao();
									Hashtable informixspaces = new Hashtable();
									try {
										informixspaces = informixspaceconfigdao.getByAlarmflag(1);
									} catch (Exception e) {
										e.printStackTrace();
									} finally {
										informixspaceconfigdao.close();
									}
									for (int k = 0; k < spaceList.size(); k++) {
										Hashtable ht = (Hashtable) spaceList.get(k);
										String tablespace = ht.get("dbspace").toString();
										String percent = ht.get("percent_free").toString();
//										System.out.println("informix�澯��ʼ-----"+dbmonitorlist.getIpAddress() + ":" + tablespace);
//										System.out.println("informix�澯��ʼ-----tablespace==="+informixspaces.containsKey(dbmonitorlist.getIpAddress() + ":" + tablespace));
										if (informixspaces.containsKey(dbmonitorlist.getIpAddress() + ":" + tablespace)) {
											//������Ҫ�澯�ı�ռ�								
											Integer free = 0;
											try {
												free = new Float(percent).intValue();
											} catch (Exception e) {
												e.printStackTrace();
											}
											//���ݱ�ռ�澯�����ж��Ƿ�澯
											Informixspaceconfig informixspaceconfig = (Informixspaceconfig) informixspaces.get(dbmonitorlist.getIpAddress() + ":" + tablespace);
//											 ���ݱ�ռ�澯�����ж��Ƿ�澯
											alarmIndicatorsNode.setLimenvalue0(informixspaceconfig.getAlarmvalue() + "");
											alarmIndicatorsNode.setLimenvalue1(informixspaceconfig.getAlarmvalue() + "");
											alarmIndicatorsNode.setLimenvalue2(informixspaceconfig.getAlarmvalue() + "");
											checkEventUtil.checkEvent(nodeDTO, alarmIndicatorsNode, (100 - free)+"",tablespace);
										}
									}
								}
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					// *****************tablespace�澯 end
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			// ----------------------------------���浽���ݿ⼰�澯 end

		}
		return returndata;
	}

}
