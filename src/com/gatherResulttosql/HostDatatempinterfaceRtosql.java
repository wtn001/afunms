package com.gatherResulttosql;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

import com.afunms.common.util.SystemConstant;
import com.afunms.indicators.model.NodeDTO;
import com.afunms.indicators.util.NodeUtil;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.node.Host;
import com.afunms.polling.om.Interfacecollectdata;
import com.gatherdb.GathersqlListManager;

public class HostDatatempinterfaceRtosql {
	
	

	/**
	 * �ѽ������sql
	 * @param dataresult �ɼ����
	 * @param node ��Ԫ�ڵ�
	 */
	public void CreateResultTosql(Hashtable dataresult, Host node) {

		if ("1".equals(PollingEngine.getCollectwebflag())) {
			
	
					String ip = node.getIpAddress();
					NodeUtil nodeUtil = new NodeUtil();
					NodeDTO nodeDTO = null;
					nodeDTO = nodeUtil.creatNodeDTOByNode(node);
					
					
					Vector interfaceVector = (Vector)dataresult.get("interface");
					
					String deleteSql = "delete from nms_interface_data_temp where nodeid='" +node.getId() + "' and subentity not in('InBandwidthUtilHdx','OutBandwidthUtilHdx')";
					if(interfaceVector != null && interfaceVector.size()>0){
						
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						Calendar tempCal=Calendar.getInstance();
						Date cc = tempCal.getTime();
						String time = sdf.format(cc);
						Vector list=new Vector();
						
						for(int i=0;i<interfaceVector.size();i++){
							Interfacecollectdata vo = (Interfacecollectdata) interfaceVector.elementAt(i);	
							try {									
//								if(vo.getName() == null)vo.setName("");
								StringBuffer sql = new StringBuffer(200);
							    sql.append("insert into nms_interface_data_temp(nodeid,ip,type,subtype,entity,subentity,sindex,thevalue,chname,restype,collecttime,unit,bak)values('");
							    sql.append(nodeDTO.getId());
							    sql.append("','");
							    sql.append(ip);
							    sql.append("','");
							    sql.append(nodeDTO.getType());//type
							    sql.append("','");
							    sql.append(nodeDTO.getSubtype());//subtype
							    sql.append("','");
							    sql.append(vo.getCategory());//entity
							    sql.append("','");
							    sql.append(vo.getEntity());//subentity
							    sql.append("','");
							    sql.append(vo.getSubentity());//sindex
							    sql.append("','");
							    sql.append(vo.getThevalue());//thevalue
							    sql.append("','");
							    sql.append(vo.getChname());//chname
							    sql.append("','");
							    sql.append(vo.getRestype());//restype
							    if("mysql".equalsIgnoreCase(SystemConstant.DBType)){
							    	sql.append("','");
								    sql.append(time);//collecttime
								    sql.append("','','");//unit
							    }else if("oracle".equalsIgnoreCase(SystemConstant.DBType)){
							    	sql.append("',");
								    sql.append("to_date('"+time+"','YYYY-MM-DD HH24:MI:SS')");//collecttime
								    sql.append(",'','");//unit
							    }
							    
							    sql.append(vo.getBak());//bak
							    sql.append("')");
							    list.add(sql.toString());
							    sql=null;
							    vo=null;
							
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						
						GathersqlListManager.AdddateTempsql(deleteSql, list);
						 list=null;
						 interfaceVector=null;
						
					}
				}
		
	}
	
	

}
