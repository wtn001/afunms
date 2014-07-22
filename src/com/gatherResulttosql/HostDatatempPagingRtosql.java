package com.gatherResulttosql;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import com.afunms.common.util.SystemConstant;
import com.afunms.indicators.model.NodeDTO;
import com.afunms.indicators.util.NodeUtil;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.node.Host;
import com.gatherdb.GathersqlListManager;

public class HostDatatempPagingRtosql {
	
	

	/**
	 * �ѽ������sql
	 * 
	 * @param dataresult
	 *            �ɼ����
	 * @param node
	 *            ��Ԫ�ڵ�
	 */
	public void CreateResultTosql(Hashtable dataresult, Host node) {

		if ("1".equals(PollingEngine.getCollectwebflag())) {// �Ƿ���������ģʽ
			
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			NodeUtil nodeUtil = new NodeUtil();
	 		        NodeDTO nodeDTO = nodeUtil.creatNodeDTOByNode(node);
		 			Hashtable pagingHashtable = (Hashtable)dataresult.get("pagehash");
			
					if(null!=pagingHashtable && pagingHashtable.size()>0)
					{
						
						
						String deleteSql = "delete from nms_other_data_temp where nodeid='" +node.getId() + "' and entity = 'paginghash'";
						Enumeration en = pagingHashtable.keys();
						int sindex = 0;
						Vector list=new Vector();
						
					while(en.hasMoreElements()){
						sindex ++;
						String key = (String) en.nextElement();
						String value = (String) pagingHashtable.get(key);
						Calendar tempCal=Calendar.getInstance();
						Date cc = tempCal.getTime();
						String time = sdf.format(cc);
						try {									
							StringBuffer sql = new StringBuffer(200);
							sql.append("insert into nms_other_data_temp(nodeid,ip,type,subtype,entity,subentity,sindex,thevalue,chname,restype,collecttime,unit,bak)values('");
						    sql.append(nodeDTO.getId());
						    sql.append("','");
						    sql.append(node.getIpAddress());
						    sql.append("','");
						    sql.append(nodeDTO.getType());
						    sql.append("','");
						    sql.append(nodeDTO.getSubtype());
						    sql.append("','paginghash','");// entity
						    sql.append(key);// subentity
						    sql.append("','");
						    sql.append(sindex);// sindex
						    sql.append("','");
						    sql.append(value);// thevalue
						    sql.append("','");
						    sql.append(key);// chname
						    sql.append("','static");// restype
//						    sql.append("','");
//						    sql.append(time);// collecttime
//						    sql.append("',' ',' ')");// unit
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
						    
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					
					 GathersqlListManager.AdddateTempsql(deleteSql, list);
					 list=null;
					
				}
					
		}
			
			}

			
			
	

}
