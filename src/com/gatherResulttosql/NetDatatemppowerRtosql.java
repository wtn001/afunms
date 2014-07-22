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
import com.afunms.polling.om.Devicecollectdata;
import com.afunms.polling.om.Interfacecollectdata;
import com.gatherdb.GathersqlListManager;

public class NetDatatemppowerRtosql {

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

			if (dataresult != null && dataresult.size() > 0) {
				NodeDTO nodeDTO = null;
				String ip = null;
				Interfacecollectdata vo = null;
				Calendar tempCal = null;
				Date cc = null;
				String time = null;
				Vector powerVector = null;

				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				NodeUtil nodeUtil = new NodeUtil();
				nodeDTO = nodeUtil.creatNodeDTOByNode(node);
				powerVector = (Vector) dataresult.get("power");
				Host host = (Host) PollingEngine.getInstance().getNodeByIP(ip);

				String hendsql = "insert into nms_envir_data_temp (nodeid,ip,type,subtype,entity,subentity,sindex,thevalue,chname,restype,collecttime,unit,bak) values(";
				String endsql = "')";
				String deleteSql = "delete from nms_envir_data_temp where nodeid='"
						+ node.getId() + "' and entity='power'";
				//envdeletedHash.put(((Host)PollingEngine.getInstance().getNodeByIP(ip)).getId()+"", ((Host)PollingEngine.getInstance().getNodeByIP(ip)).getId()+"");

				if (powerVector != null && powerVector.size() > 0) {
					tempCal = Calendar.getInstance();
					cc = tempCal.getTime();
					time = sdf.format(cc);
					Vector list = new Vector();
					for (int i = 0; i < powerVector.size(); i++) {
						vo = (Interfacecollectdata) powerVector.elementAt(i);

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
						if("mysql".equalsIgnoreCase(SystemConstant.DBType)){
							sbuffer.append("'").append(time).append("',");
						}else if("oracle".equalsIgnoreCase(SystemConstant.DBType)){
							sbuffer.append("to_date('"+time+"','YYYY-MM-DD HH24:MI:SS'),");
						}
						sbuffer.append("'").append(vo.getUnit()).append("',");
						sbuffer.append("'").append(vo.getBak());
						sbuffer.append(endsql);
						list.add(sbuffer.toString());

						sbuffer = null;
						vo=null;

					}
					
					GathersqlListManager.AdddateTempsql(deleteSql, list);
					list=null;
				}
				nodeDTO = null;
				powerVector = null;

			}

		}

	}

}
