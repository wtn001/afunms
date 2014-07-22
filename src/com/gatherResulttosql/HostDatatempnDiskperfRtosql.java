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

public class HostDatatempnDiskperfRtosql {
	
	

	/**
	 * �ѽ������sql
	 * @param dataresult �ɼ����
	 * @param node ��Ԫ�ڵ�
	 */
	public void CreateResultTosql(Hashtable dataresult, Host node) {

		if ("1".equals(PollingEngine.getCollectwebflag())) {//�Ƿ���������ģʽ
			
					String ip = (String)node.getIpAddress();
    				
					
					
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					NodeUtil nodeUtil = new NodeUtil();
			 		NodeDTO nodeDTO = nodeUtil.creatNodeDTOByNode(node);
					
					
		 			List diskperfList = (ArrayList)dataresult.get("alldiskperf");
					String deleteSql = "delete from nms_diskperf_data_temp where nodeid='" +node.getId() + "'";
					
					Calendar tempCal=Calendar.getInstance();
					Date cc = tempCal.getTime();
					String time = sdf.format(cc);
					Vector list=new Vector();
					
					
					for(int i=0;i<diskperfList.size();i++){
						Hashtable diskprefHash = (Hashtable)diskperfList.get(i);
						try {				
							Enumeration en = diskprefHash.keys();
							while(en.hasMoreElements()){
								String key = (String) en.nextElement();
								String value = String.valueOf(diskprefHash.get(key));
							    StringBuffer sql = new StringBuffer(200);
							    sql.append("insert into nms_diskperf_data_temp(nodeid,ip,type,subtype,entity,subentity,sindex,thevalue,chname,restype,collecttime,unit,bak)values('");
							    sql.append(nodeDTO.getId());
							    sql.append("','");
							    sql.append(ip);
							    sql.append("','");
							    sql.append(nodeDTO.getType());
							    sql.append("','");
							    sql.append(nodeDTO.getSubtype());
							    sql.append("','alldiskperf");//entity
							    sql.append("','");
							    sql.append(key);//subentity
							    sql.append("','");
							    sql.append(i+1);//sindex
							    sql.append("','");
							    sql.append(value);//thevalue
							    sql.append("','");
							    sql.append(key);//chname
							    sql.append("','static");//restype
							    if("mysql".equalsIgnoreCase(SystemConstant.DBType)){
							    	sql.append("','");
								    sql.append(time);//collecttime
								    sql.append("',' ',' ')");//unit
							    }else if("oracle".equalsIgnoreCase(SystemConstant.DBType)){
							    	sql.append("',");
								    sql.append("to_date('"+time+"','YYYY-MM-DD HH24:MI:SS')");//collecttime
								    sql.append(",' ',' ')");//unit
							    }
							    

							    list.add(sql.toString());
							    sql=null;
							    key=null;
							    value=null;
							    
							    
							}
							en=null;
						} catch (Exception e) {
							e.printStackTrace();
						}
						
						diskprefHash=null;
					}
					
					 GathersqlListManager.AdddateTempsql(deleteSql, list);
					 list=null;
					 diskperfList=null;
					
					
				}
			}
	

}
