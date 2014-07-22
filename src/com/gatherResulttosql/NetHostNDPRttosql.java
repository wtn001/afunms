package com.gatherResulttosql;

import java.text.SimpleDateFormat;
import java.util.Hashtable;
import java.util.Vector;

import com.afunms.common.util.CommonUtil;
import com.afunms.common.util.SystemConstant;
import com.afunms.indicators.model.NodeDTO;
import com.afunms.indicators.util.NodeUtil;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.node.Host;
import com.afunms.polling.om.NDP;
import com.gatherdb.GathersqlListManager;

public class NetHostNDPRttosql {
	
	/**
	 * �ѽ������sql
	 * @param dataresult �ɼ����
	 * @param node ��Ԫ�ڵ�
	 */
	public void CreateResultTosql(Vector ndpVector, Host node) {

		if ("1".equals(PollingEngine.getCollectwebflag())) {//�Ƿ���������ģʽ			
			// ����NDP��Ϣ���	
			Vector addSqlV = new Vector();
		    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			if(ndpVector != null && ndpVector.size()>0){
				NDP ndp = null;
				for(int i=0;i<ndpVector.size();i++){
					try{
					ndp = (NDP) ndpVector.elementAt(i);	
				    String sqll = "";
					String time = sdf.format(ndp.getCollecttime().getTime());
					sqll = "insert into nms_ndp(nodeid,deviceid,portname,collecttime)values(";
					sqll = sqll + ndp.getNodeid() + ",'" + ndp.getDeviceId() + "','" + ndp.getPortName() + "','";
					if("mysql".equalsIgnoreCase(SystemConstant.DBType)){
						sqll = sqll + "','" + time + "')";
					}else if("oracle".equalsIgnoreCase(SystemConstant.DBType)){
						sqll = sqll + "',to_date('"+time+"','YYYY-MM-DD HH24:MI:SS'),')";
					}
					  								
					  
					addSqlV.add(sqll);
					ndp=null;
					
				}catch(Exception e)
				{}
				}
				
			}
					
			//GathersqlListManager.AdddateTempsql(deleteSql, list);
			GathersqlListManager.AdddateTempsql("skip", addSqlV);
			addSqlV=null;
			//}

			}

		}
	
	

}
