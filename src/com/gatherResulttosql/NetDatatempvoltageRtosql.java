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

public class NetDatatempvoltageRtosql {
	
	
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

			// ���������Ϣ���
			if (dataresult != null && dataresult.size() > 0) {
				NodeDTO nodeDTO = null;
				String ip = null;
				Interfacecollectdata vo = null;
				Calendar tempCal = null;
				Date cc = null;
				String time = null;
				Vector fanVector = null;

				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				NodeUtil nodeUtil = new NodeUtil();
				nodeDTO = nodeUtil.creatNodeDTOByNode(node);
				fanVector = (Vector) dataresult.get("voltage");

			
				String hendsql = "insert into nms_envir_data_temp (nodeid,ip,type,subtype,entity,subentity,sindex,thevalue,chname,restype,collecttime,unit,bak) values(";
				String endsql = "')";
				String deleteSql = "delete from nms_envir_data_temp where nodeid='"
						+ node.getId() + "' and entity='voltage'";
				
				// �ж��Ƿ��Ѿ���ɾ��������û��ɾ����������ӵ��ѱ�ɾ����������ִ��ɾ�����
			    String flg="";
				if (fanVector != null && fanVector.size() > 0) {
					tempCal = Calendar.getInstance();
					cc = tempCal.getTime();
					time = sdf.format(cc);
					Vector list =new Vector();
					
					for (int i = 0; i < fanVector.size(); i++) {
						vo = (Interfacecollectdata) fanVector.elementAt(i);
						
							StringBuffer sbuffer = new StringBuffer(200);
							sbuffer.append(hendsql);
							sbuffer.append("'").append(node.getId()).append("',");
							sbuffer.append("'").append(node.getIpAddress()).append(
									"',");
							sbuffer.append("'").append(nodeDTO.getType()).append(
									"',");
							sbuffer.append("'").append(nodeDTO.getSubtype())
									.append("',");
							sbuffer.append("'").append(vo.getCategory()).append(
									"',");
							sbuffer.append("'").append(vo.getEntity()).append("',");
							sbuffer.append("'").append(vo.getSubentity()).append(
									"',");
							sbuffer.append("'").append(vo.getThevalue()).append(
									"',");
							sbuffer.append("'").append(vo.getChname()).append("',");
							sbuffer.append("'").append(vo.getRestype())
									.append("',");
//							sbuffer.append("'").append(time).append("',");
							if("mysql".equalsIgnoreCase(SystemConstant.DBType)){
								sbuffer.append("'").append(time).append("',");
							}else if("oracle".equalsIgnoreCase(SystemConstant.DBType)){
								sbuffer.append("to_date('"+time+"','YYYY-MM-DD HH24:MI:SS'),");
							}
							sbuffer.append("'").append(vo.getUnit()).append("',");
							sbuffer.append("'").append(vo.getBak());
							sbuffer.append(endsql);
							list.add(sbuffer.toString());
                            flg=vo.getEntity();
							sbuffer = null;

					}
					
					
					
					GathersqlListManager.AdddateTempsql(deleteSql, list);
					list=null;
					vo = null;
				}

				nodeDTO = null;
				fanVector = null;
			}
		}
	}
	

}
