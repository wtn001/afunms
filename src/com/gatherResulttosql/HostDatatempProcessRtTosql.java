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
import com.afunms.polling.om.Processcollectdata;
import com.gatherdb.GathersqlListManager;

public class HostDatatempProcessRtTosql {
	
	/**
	 * �ѽ������sql
	 * @param dataresult �ɼ����
	 * @param node ��Ԫ�ڵ�
	 */
	public void CreateResultTosql(Hashtable dataresult, Host node) {

		if ("1".equals(PollingEngine.getCollectwebflag())) {//�Ƿ���������ģʽ

			if(dataresult != null && dataresult.size()>0){
				
				String hendsql="insert into nms_process_data_temp(nodeid,ip,`type`,subtype,entity,subentity,sindex,thevalue,chname,restype,collecttime,unit,bak)values(";
				String endsql=")";

				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    				NodeUtil nodeUtil = new NodeUtil();
		 			NodeDTO nodeDTO = nodeUtil.creatNodeDTOByNode(node);
					Vector processVector = (Vector)dataresult.get("process");
					String deleteSql = "delete from nms_process_data_temp where nodeid='" +node.getId() + "'";
					if(processVector != null && processVector.size()>0){
						Vector list=new Vector();
						for(int i=0;i<processVector.size();i++){
							Processcollectdata vo = (Processcollectdata) processVector.elementAt(i);
							Calendar tempCal = (Calendar) vo.getCollecttime();
							Date cc = tempCal.getTime();
							String time = sdf.format(cc);	
							String thevalue = vo.getThevalue();
							if(thevalue != null){
								thevalue = thevalue.replaceAll("\\\\", "/");
								if(thevalue.length() > 50){
									thevalue = thevalue.substring(0, 50)+"...";
								}
							}
									
							
							
							StringBuffer sbuffer = new StringBuffer(200);
							sbuffer.append(hendsql);
							sbuffer.append("'").append(node.getId()).append("',");
							sbuffer.append("'").append(node.getIpAddress()).append("',");
							sbuffer.append("'").append(nodeDTO.getType()).append("',");
							sbuffer.append("'").append(nodeDTO.getSubtype()).append("',");
							sbuffer.append("'").append(vo.getCategory()).append("',");
							sbuffer.append("'").append(vo.getEntity()).append("',");
							sbuffer.append("'").append(vo.getSubentity()).append("',");
							sbuffer.append("'").append(vo.getThevalue()).append("',");
							sbuffer.append("'").append(vo.getChname()).append("',");
							sbuffer.append("'").append(vo.getRestype()).append("',");
							if("mysql".equalsIgnoreCase(SystemConstant.DBType)){
								sbuffer.append("'").append(time).append("',");
							}else if("oracle".equalsIgnoreCase(SystemConstant.DBType)){
								sbuffer.append("to_date('"+time+"','YYYY-MM-DD HH24:MI:SS'),");
							}
							
							sbuffer.append("'").append(vo.getUnit()).append("',");
							sbuffer.append("'").append(vo.getBak()).append("'");
							sbuffer.append(endsql);
							list.add(sbuffer.toString());
						    
							sbuffer=null;
							vo=null;
							
							thevalue=null;
							//cc=null;
							//tempCal=null;
							//time=null;
						}
						GathersqlListManager.AdddateTempsql(deleteSql, list);
						list=null;

					}
					
					processVector=null;
				}
			//}


			
		}
	}

	
	
	
	

}
