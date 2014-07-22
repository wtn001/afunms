package com.gatherResulttosql;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import com.afunms.common.util.SystemConstant;
import com.afunms.indicators.model.NodeDTO;
import com.afunms.indicators.util.NodeUtil;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.node.Host;
import com.gatherdb.GathersqlListManager;

public class HostDatatempCpuperRtosql {
	
	
	

	/**
	 * �ѽ������sql
	 * @param dataresult �ɼ����
	 * @param node ��Ԫ�ڵ�
	 */
	public void CreateResultTosql(Hashtable dataresult, Host node) {

		if ("1".equals(PollingEngine.getCollectwebflag())) {//�Ƿ���������ģʽ
			

		
				
				
				
				NodeUtil nodeUtil = new NodeUtil();		
		 		NodeDTO nodeDTO = nodeUtil.creatNodeDTOByNode(node);
		 		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 		Calendar tempCal=Calendar.getInstance();
				Date cc = tempCal.getTime();
				String time = sdf.format(cc);
		 		
					List cpuperflist = (ArrayList)dataresult.get("cpuperflist");
					String deleteSql = "delete from nms_other_data_temp where nodeid='" +node.getId() + "' and entity = 'cpuperflist'";
					
					
					//�õ�CPUƽ��
					int index = 0;
					if (cpuperflist != null && cpuperflist.size() > 0) {
						
						
						Vector list=new Vector();
						for (int i = 0; i < cpuperflist.size(); i++){
							Hashtable hash = (Hashtable) cpuperflist.get(i);
							Enumeration en = hash.keys();
							while(en.hasMoreElements()){
								index++;
								String key = (String) en.nextElement();
								String value = (String) hash.get(key);
								try {
								    StringBuffer sql = new StringBuffer(200);
								    sql.append("insert into nms_other_data_temp(nodeid,ip,type,subtype,entity,subentity,sindex,thevalue,chname,restype,collecttime,unit)values('");
								    sql.append(nodeDTO.getId());
								    sql.append("','");
								    sql.append(node.getIpAddress());
								    sql.append("','");
								    sql.append(nodeDTO.getType());
								    sql.append("','");
								    sql.append(nodeDTO.getSubtype());//subtype
								    sql.append("','cpuperflist','");//entity
								    sql.append(key);//subentity
								    sql.append("','");
								    sql.append(index);//sindex
								    sql.append("','");
								    sql.append(value);//thevalue
								    sql.append("','");
								    sql.append(key);//chname
								    if("mysql".equalsIgnoreCase(SystemConstant.DBType)){
								    	sql.append("','static','");//restype
									    sql.append(time);//collecttime
									    sql.append("','')");//unit
								    }else if("oracle".equalsIgnoreCase(SystemConstant.DBType)){
								    	sql.append("','static',");//restype
									    sql.append("to_date('"+time+"','YYYY-MM-DD HH24:MI:SS')");//collecttime
									    sql.append(",'')");//unit
								    }
								    
								    list.add(sql.toString());
								    sql=null;
								
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}
						
						 GathersqlListManager.AdddateTempsql(deleteSql, list);
						 list=null;
						
					}
					
					
					
				}
			}
			
			
			
	

}
