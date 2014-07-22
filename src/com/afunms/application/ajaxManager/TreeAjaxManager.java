package com.afunms.application.ajaxManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import com.afunms.application.dao.DBDao;
import com.afunms.application.dao.MQConfigDao;
//import com.afunms.application.dao.NmsSpacesDao;
import com.afunms.application.dao.TuxedoConfigDao;
import com.afunms.application.dao.WeblogicConfigDao;
import com.afunms.application.model.DBVo;
import com.afunms.application.model.MQConfig;
//import com.afunms.application.model.Nms_oraspaces;
import com.afunms.application.model.TuxedoConfig;
import com.afunms.application.model.WeblogicConfig;
import com.afunms.common.base.AjaxBaseManager;
import com.afunms.common.base.AjaxManagerInterface;
import com.afunms.common.base.BaseUtil;
import com.afunms.common.util.SessionConstant;
import com.afunms.common.util.ShareData;
import com.afunms.config.dao.BusinessDao;
//import com.afunms.config.dao.DiskbusyconfigDao;
import com.afunms.config.dao.DiskconfigDao;
import com.afunms.config.dao.MQchannelDao;
import com.afunms.config.dao.PortconfigDao;
import com.afunms.config.model.Business;
//import com.afunms.config.model.Diskbusyconfig;
import com.afunms.config.model.Diskconfig;
import com.afunms.config.model.MQchannel;
import com.afunms.config.model.Portconfig;
import com.afunms.indicators.model.NodeDTO;
import com.afunms.indicators.util.Constant;
import com.afunms.indicators.util.NodeUtil;
import com.afunms.system.model.User;
import com.afunms.topology.dao.HostNodeDao;
import com.afunms.topology.dao.TreeNodeDao;
import com.afunms.topology.model.HostNode;
import com.afunms.topology.model.TreeNode;
import com.afunms.topology.util.NodeHelper;

public class TreeAjaxManager extends AjaxBaseManager implements AjaxManagerInterface {

	public void execute(String action) {
		if (action.equals("getChildrenNodes")) {
			getChildrenNodes();
		}else if (action.equals("getChildrenNodes4Report")) {
			getChildrenNodes4Report();
		}
	}

	/**
	 * ȡ�ø��ݸ��ڵ�id�������ӽڵ� ���ܲ˵���
	 * @author
	 * @date 2013-3-6
	 */
	public void getChildrenNodes() {
		//System.out.println(request.getParameter("id") + " " + request.getParameter("name") + " " + request.getParameter("otherParam"));
		String id = request.getParameter("id");
		String rootPath = request.getContextPath();
		//������ڵ���Ϣ  
		List<Map<String,Object>> items = new ArrayList<Map<String,Object>>();  
		
		//ҵ��ڵ�
		if(BaseUtil.isEmpty(id) || "null".equals(id)){
			Map<String,Object> item = new HashMap<String,Object>(); 
			item.put("id", 0);
			item.put("pId", -1);
			item.put("name", "�豸��Դ��");
			item.put("url_my", "perform.do?action=monitornodelist&flag=1&category=node&firstin=1");
			item.put("icon", rootPath+"/performance/dtree/img/base.gif");
			items.add(item);
			User current_user = (User) session.getAttribute(SessionConstant.CURRENT_USER);
			String businessIds = current_user.getBusinessids();
			List<Business> businessList = new ArrayList<Business>();
			BusinessDao businessDao = new BusinessDao();
			try {
				businessList = businessDao.queryRecursionIDs(businessIds.replace(",,","=").replace(",","").replace("=",","));
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				businessDao.close();
			}
			for(Business business : businessList){
				String bid = business.getId();
				String pid = business.getPid();
				String name = business.getName();
				
				item = new HashMap<String,Object>(); 
				item.put("id", bid);
				item.put("pId", pid);
				item.put("name", name);
				item.put("isParent", true);
				item.put("url_my", "perform.do?action=monitornodelist&flag=1&category=node&firstin=1&bid=,"+bid+",&treeId=,"+bid+",");
				
				items.add(item);
			}
		}else {
			//�豸�ڵ�
			NodeUtil nodeUtil = new NodeUtil();
			nodeUtil.setSetedMonitorFlag(true);
			nodeUtil.setMonitorFlag("1");
			List<TreeNode> treeNodeList = new ArrayList<TreeNode>();			// ���нڵ��б�
			TreeNodeDao treeNodeDao = new TreeNodeDao();
			try{
				treeNodeList = treeNodeDao.loadAll(); // ��ȡ���еĽڵ�
			}catch(Exception e){
				e.printStackTrace();
			} finally {
				treeNodeDao.close();
			}
			boolean isShowTreeNodeFlag = true;	// �Ƿ���ʾ�ýڵ� 	
			boolean rightFrameFlag = true;		// �ұ߿����ʾҳ���ģʽ ��ʱֻ��Ϊ true �Ժ��������ģʽ 
			String currTreeNodeId = "";		// ��ǰ���Ľڵ� Id
			String treeNodeFatherId = "";		// ��ǰ���Ľڵ�ĸ� Id	
			String currTreeNodeFatherId = "";			// ��ǰ�ڵ㸸 Id
			String currBusinessNodeId = "";		// ��ǰҵ��ڵ��Id
			int treeNodeNum = 0;				// ���ڵ��� �ڼ���
			Hashtable checkEventHashtable = ShareData.getCheckEventHash();
			for(Object treeNodeObject : treeNodeList){
				// ѭ��ÿһ���豸���ڵ�
				TreeNode currTreeNode = (TreeNode)treeNodeObject;
				List nodeList = nodeUtil.getByNodeTagAndBid(currTreeNode.getNodeTag(), currTreeNode.getCategory(), "," + id + ",");
				List nodeDTOList = nodeUtil.conversionToNodeDTO(nodeList);
				
				if(nodeDTOList == null){
					nodeDTOList = new ArrayList();
				}
				
				List tempNodeDTOList = new ArrayList();		// ��ʱ�洢node
				for(Object nodeDTOObject : nodeDTOList){
					NodeDTO nodeDTO = (NodeDTO)nodeDTOObject;
					if(nodeDTO.getBusinessId() != null && nodeDTO.getBusinessId().contains("," + id + ",")){
						tempNodeDTOList.add(nodeDTO);
					}
				}
				nodeDTOList = tempNodeDTOList;
				currTreeNode.setDeceiveNum(nodeDTOList.size()+"");
				
				isShowTreeNodeFlag = true;
//		 	if("0".equals(currTreeNode.getDeceiveNum())){
//		 		// ����豸��Ϊ 0 �� �� ��ʾģʽ�ĸ�ֵ��
//		 		isShowTreeNodeFlag = treeshowflag;
//			}
				
				// ����ǰ�ڵ㸳ֵ Ϊ �ø��ڵ� + "_" + �ýڵ�id;
				currTreeNodeId = currBusinessNodeId + "_" + currTreeNode.getId();
				currTreeNodeFatherId = currBusinessNodeId + "_" + currTreeNode.getFatherId();
				//System.out.println("currTreeNodeId="+currTreeNodeId+"===currTreeNodeFatherId="+currTreeNodeFatherId);
				if(0 == currTreeNode.getFatherId()){
					currTreeNodeFatherId = currBusinessNodeId;
				}
				if(isShowTreeNodeFlag){
					currTreeNodeId = currTreeNodeId;
					currTreeNodeFatherId = currTreeNodeFatherId;
					String imagestr = "<%=rootPath%>/performance/<%=NodeHelper.getTypeImage(currTreeNode.getName())%>";
					if(0 == treeNodeNum && rightFrameFlag){
						//��ҳ������ͼ����豸ʱ��ת��ҳ������
						String rightFramePath = rootPath + request.getParameter("rightFramePath");
						rightFramePath = rightFramePath.replaceAll("-equals-","=");
						rightFramePath = rightFramePath.replaceAll("-and-","&");
						if(request.getParameter("rightFramePath") == null || request.getParameter("rightFramePath").equals("null")){
							// ������ע����ǰ�ģ���Ϊ����� ԭ�򣺵�����ܲ˵�ʱ���Բ�ѯ�������豸
							//rightFramePath = rootPath + currTreeNode.getUrl() + "&treeBid=" + currbid;
							rightFramePath = "perform.do?action=monitornodelist&flag=1&category=node&firstin=1";
						}
						//parent.document.getElementById("rightFrame").src="<%=rightFramePath%>";
					}
					treeNodeNum++;
					//currTreeNodeFatherId = currTreeNodeId;
					if("1".equals(currTreeNode.getIsHaveChild())){       
						// �����κ���
					} else {
						L3:for(Object nodeDTOObject : nodeDTOList){
							NodeDTO nodeDTO = (NodeDTO)nodeDTOObject;
							
							int alarmLevel = 0;
							//CheckEventDao checkEventDao = new CheckEventDao();
							//try{
							//	alermlevel = checkEventDao.findMaxAlarmLevelByName(nodeDTO.getId() + ":" + nodeDTO.getType());
							//} catch (Exception e) {
							
							//} finally {
							//	checkEventDao.close();
							//}
							String chexkname = nodeDTO.getId()+":"+nodeDTO.getType()+":"+nodeDTO.getSubtype()+":";
							for(Iterator it = checkEventHashtable.keySet().iterator();it.hasNext();){ 
								String key = (String)it.next(); 
								if(key.startsWith(chexkname)){
									if(alarmLevel < (Integer) checkEventHashtable.get(key)){
										alarmLevel = (Integer) checkEventHashtable.get(key); 
									}
								}
							}
							//checkEventDao.close();
							
							currTreeNodeId = currTreeNode.getNodeTag() + "_" + nodeDTO.getId();
							imagestr = rootPath + "/resource/" + NodeHelper.getCurrentStatusImage(alarmLevel);
							if(Constant.TYPE_GATEWAY.equals(nodeDTO.getType()) || Constant.TYPE_F5.equals(nodeDTO.getType()) || Constant.TYPE_VPN.equals(nodeDTO.getType()) || Constant.TYPE_HOST.equals(nodeDTO.getType()) || Constant.TYPE_NET.equals(nodeDTO.getType()) || Constant.TYPE_FIREWALL.equals(nodeDTO.getType())|| Constant.TYPE_DB.equals(nodeDTO.getType()) || Constant.TYPE_MIDDLEWARE.equals(nodeDTO.getType())){
								imagestr = rootPath + "/performance/" + NodeHelper.getSubTypeImage(nodeDTO.getSubtype());  
							}   
							currTreeNodeId = currTreeNodeId;
							currTreeNodeFatherId = currTreeNodeFatherId;
							
							HashMap<String,Object> item = new HashMap<String,Object>(); 
							item.put("id", currTreeNodeId);
							item.put("pId", currTreeNodeFatherId);
							item.put("name", nodeDTO.getName());
							item.put("icon", imagestr);
							item.put("url_my", rootPath+"/detail/dispatcher.jsp?flag=1&id="+currTreeNode.getNodeTag()+nodeDTO.getId());
							items.add(item); //��ӵ����ĵ�һ��
//					    d.add(currTreeNodeId,currTreeNodeFatherId,nodeDTO.getName(),"<%=rootPath
//								+ "/detail/dispatcher.jsp?flag=1&id="
//								+ currTreeNode.getNodeTag()
//								+ nodeDTO.getId()%>","","","rightFrame",imagestr,imagestr);
						} // ��� ÿһ���豸 ѭ�� (L3)
					}
				}
			}	// ���ÿһ���豸���ڵ�ѭ�� (L2)
		}
		JSONArray json = JSONArray.fromObject(items);//ת��json��ʽ  
		out.print(json);
		out.flush();
		out.close();
	}
	/**
	 * ȡ�ø��ݸ��ڵ�id�������ӽڵ� ����˵���
	 * @author 
	 * @date 2013-3-6
	 */
	public void getChildrenNodes4Report() {
		//System.out.println(request.getParameter("id") + " " + request.getParameter("name") + " " + request.getParameter("otherParam"));
		String id = request.getParameter("id");
		String rootPath = request.getContextPath();
		
		//������ڵ���Ϣ  
		List<Map<String,Object>> items = new ArrayList<Map<String,Object>>();  
		
		//ҵ��ڵ�
		if(BaseUtil.isEmpty(id) || "null".equals(id)){
			User current_user = (User) session.getAttribute(SessionConstant.CURRENT_USER);
			String businessIds = current_user.getBusinessids();
			List<Business> businessList = new ArrayList<Business>();
			BusinessDao businessDao = new BusinessDao();
			try {
				businessList = businessDao.queryRecursionIDs(businessIds.replace(",,","=").replace(",","").replace("=",","));
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				businessDao.close();
			}
			for(Business business : businessList){
				String bid = business.getId();
				String pid = business.getPid();
				String name = business.getName();
				
				HashMap<String,Object> item = new HashMap<String,Object>(); 
				item.put("id", bid+"yw");
				item.put("pId", pid+"yw");
				item.put("name", name);
				item.put("isParent", true);
				
				items.add(item);
			}
		}else if(id.endsWith("yw")) {
			//���ҵ��ڵ� ����ҵ���µ��豸
			String realId = id.replace("yw", "");
			
			//�������������豸
			List hostNetList = new ArrayList();
			HostNodeDao dao = new HostNodeDao();
			try {
				hostNetList = dao.findByCondition(" where bid=',"+realId+",' and managed='1' ");
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				dao.close();
			}
			HostNode vo = new HostNode();
			//int serialNum = 0;//�豸��� Ŀǰû�õ� Ԥ��
			if(hostNetList!=null&&hostNetList.size()>0){
				for(int i=0;i<hostNetList.size();i++){
					vo=(HostNode)hostNetList.get(i);
					HashMap<String,Object> item = new HashMap<String,Object>(); 
					item.put("id", vo.getId());
					item.put("pId", id);
					item.put("name", vo.getIpAddress()+"("+vo.getAlias()+")");
					item.put("isParent", true);
					
					items.add(item);
				}
			}
			//db
			List dbList = new ArrayList();
			DBDao dbdao = new DBDao();
			try {
				dbList = dbdao.findByCondition(" where bid=',"+realId+",' and managed='1' ");
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				dbdao.close();
			}
			if(null != dbList && dbList.size() > 0){
				for(int i=0; i<dbList.size(); i++){
					String dbType = "";
					DBVo DBvo = (DBVo)dbList.get(i);
					if(1==DBvo.getDbtype()){
						dbType = "oracle";
					}else if (6 == DBvo.getDbtype()) {
						dbType = "sybase";
					}else if (7 == DBvo.getDbtype()) {
						dbType = "informix";
					}
					HashMap<String,Object> item = new HashMap<String,Object>(); 
					item.put("id", dbType+DBvo.getId());
					item.put("pId", id);
					item.put("name", DBvo.getIpAddress()+"("+DBvo.getAlias()+")");
					item.put("isParent", true);
					
					items.add(item);
				}
			}
			//weblogic
			WeblogicConfigDao configdao = new WeblogicConfigDao();
			List weblogicList = new ArrayList();
			try{
				weblogicList = configdao.findByCondition(" where netid=',"+realId+",' and mon_flag='1' ");
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				configdao.close();
			}
			if(null != weblogicList && weblogicList.size() > 0){
				for(int i=0; i<weblogicList.size(); i++){
					WeblogicConfig weblogicConfig = (WeblogicConfig)weblogicList.get(i);
					HashMap<String,Object> item = new HashMap<String,Object>(); 
					item.put("id", "weblg"+weblogicConfig.getId());
					item.put("pId", id);
					item.put("name", weblogicConfig.getIpAddress()+"("+weblogicConfig.getAlias()+")");
					item.put("isParent", true);
					
					items.add(item);
				}
			}
			//tuxedo
			TuxedoConfigDao tuxedoConfigDao = new TuxedoConfigDao();
			List tuxedoList = new ArrayList();
			try{
				tuxedoList = tuxedoConfigDao.findByCondition(" where bid=',"+realId+",' and mon_flag='1' ");
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				tuxedoConfigDao.close();
			}
			if(null != tuxedoList && tuxedoList.size() > 0){
				for(int i=0; i<tuxedoList.size(); i++){
					TuxedoConfig tuxedoConfig = (TuxedoConfig)tuxedoList.get(i);
					HashMap<String,Object> item = new HashMap<String,Object>(); 
					item.put("id", "tux"+tuxedoConfig.getId());
					item.put("pId", id);
					item.put("name", tuxedoConfig.getIpAddress()+"("+tuxedoConfig.getName()+")");
					item.put("isParent", true);
					
					items.add(item);
				}
			}
			//mq
			MQConfigDao mQConfigDao = new MQConfigDao();
			List mqList = new ArrayList();
			try {
				mqList = mQConfigDao.findByCondition(" where netid=',"+realId+",' and mon_flag='1' ");
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				mQConfigDao.close();
			}
			if(null != mqList && mqList.size() > 0){
				for(int i=0; i<mqList.size(); i++){
					MQConfig mQConfig = (MQConfig)mqList.get(i);
					HashMap<String,Object> item = new HashMap<String,Object>(); 
					item.put("id", "mq"+mQConfig.getId());
					item.put("pId", id);
					item.put("name", mQConfig.getIpaddress()+"("+mQConfig.getName()+")");
					item.put("isParent", true);
					
					items.add(item);
				}
			}
		}else if(id.startsWith("mq")) {
			//���ҵ��ڵ� ����ҵ���µ��豸
			String realId = id.replace("mq", "");
			
			MQConfigDao mQConfigDao = new MQConfigDao();
			List mqList = new ArrayList();
			try {
				mqList = mQConfigDao.findByCondition(" where id='"+realId+"' ");
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				mQConfigDao.close();
			}
			//mq
			if(null != mqList && mqList.size() > 0){
				for(int i=0; i<mqList.size(); i++){
					MQConfig mQConfig = (MQConfig)mqList.get(i);
					String[] itemsTmp = { "��ͨ��", "���Ŷ������", "ͨ��״̬"};
		  			String[] itemId = { "ping", "deadq", "channelstate"};
					for(int j=0;j<itemsTmp.length;j++){
						HashMap<String,Object> item = new HashMap<String,Object>(); 
						item.put("id", "mq"+itemId[j]+mQConfig.getIpaddress()+"_/"+mQConfig.getName()+"__mq");
						item.put("pId", id);
						item.put("name", itemsTmp[j]);
						
						items.add(item);
					}
				}
			}
			//mqͨ����Ϣ
			List mqChannelList = new ArrayList();
			MQchannelDao mQchannelDao = new MQchannelDao();
			try {
				mqChannelList = mQchannelDao.loadById(realId);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				mQchannelDao.close();
			}
			if(null != mqChannelList && mqChannelList.size() > 0){
				for(int i = 0; i < mqChannelList.size(); i++){
					MQchannel mQchannel = (MQchannel) mqChannelList.get(i);
					HashMap<String,Object> item = new HashMap<String,Object>(); 
					item.put("id", "mqchannelstate*"+mQchannel.getIpaddress()+"*"+mQchannel.getName());
					item.put("pId", "mqchannelstate"+mQchannel.getIpaddress()+"_/"+mQchannel.getMqName()+"__mq");
					item.put("name", mQchannel.getName());
					
					items.add(item);
				}
			}
		}else if(id.startsWith("tux")) {
			//���ҵ��ڵ� ����ҵ���µ��豸
			String realId = id.replace("tux", "");
			
			TuxedoConfigDao tuxedoConfigDao = new TuxedoConfigDao();
			List tuxedoList = new ArrayList();
			try{
				tuxedoList = tuxedoConfigDao.findByCondition(" where id='"+realId+"' ");
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				tuxedoConfigDao.close();
			}
			//tuxedo
			int serialNum = 0;//�豸��� Ŀǰû�õ� Ԥ��
			if(null != tuxedoList && tuxedoList.size() > 0){
				for(int i=0; i<tuxedoList.size(); i++){
					TuxedoConfig tuxedoConfig = (TuxedoConfig)tuxedoList.get(i);
					String[] items_tmp = { "��ͨ��", "״̬������", "ƽ�����г���", "��������", "������"};
		  			String[] itemId = { "ping", "stateRate", "queueLength", "ServerNum", "ServiceNum"};
					for(int j=0;j<items_tmp.length;j++){
						HashMap<String,Object> item = new HashMap<String,Object>(); 
						item.put("id", "tux"+itemId[j]+tuxedoConfig.getIpAddress()+"_/"+tuxedoConfig.getName()+"__tuxedo"+"_^"+serialNum);
						item.put("pId", id);
						item.put("name", items_tmp[j]);
						items.add(item);
					}
				}
			}
		}else if(id.startsWith("weblg")) {
			//���ҵ��ڵ� ����ҵ���µ��豸
			String realId = id.replace("weblg", "");
			
			WeblogicConfigDao configdao = new WeblogicConfigDao();
			List weblogicList = new ArrayList();
			try{
				weblogicList = configdao.findByCondition(" where id='"+realId+"' ");
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				configdao.close();
			}
			//weblogic
			int serialNum = 0;//�豸��� Ŀǰû�õ� Ԥ��
			if(null != weblogicList && weblogicList.size() > 0){
				for(int i=0; i<weblogicList.size(); i++){
					WeblogicConfig weblogicConfig = (WeblogicConfig)weblogicList.get(i);
					String[] items_tmp = { "��ͨ��", "״̬������"};
		  			String[] itemId = { "ping", "stateRate"};
					for(int j=0;j<items_tmp.length;j++){
						HashMap<String,Object> item = new HashMap<String,Object>(); 
						item.put("id", "weblg"+itemId[j]+weblogicConfig.getIpAddress()+"_/"+weblogicConfig.getAlias()+"__weblogic"+"_^"+serialNum);
						item.put("pId", id);
						item.put("name", items_tmp[j]);
						items.add(item);
					}
				}
			}
		}else if(id.startsWith("oracle")) {
			//���ҵ��ڵ� ����ҵ���µ��豸
			String realId = id.replace("oracle", "");
			
			int serialNum = 0;//�豸��� Ŀǰû�õ� Ԥ��
			List oraList = new ArrayList();
			DBDao dbdao = new DBDao();
			try {
				oraList = dbdao.findByCondition(" where id='"+realId+"' ");
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				dbdao.close();
			}
			if(null != oraList && oraList.size() > 0){
				for(int i=0; i<oraList.size(); i++){
					DBVo DBvo = (DBVo)oraList.get(i);
					String bidTmp = DBvo.getBid().replace(",","");
					String[] items_tmp = { "��ͨ��", "CPU", "�ڴ�", "��־���","I/O���ܼ��","��ռ�",};
		  			String[] itemId = { "ping", "pu","pga","archivelog","iocache","tablespace"};
					for(int j=0;j<items_tmp.length;j++){
						String itemIdStr = itemId[j];
						HashMap<String,Object> item = new HashMap<String,Object>(); 
						if("tablespace".equals(itemIdStr)){
							item.put("id", "oracle"+itemId[j]+"*"+DBvo.getId()+"*"+DBvo.getIpAddress());
							item.put("pId", id);
							item.put("name", items_tmp[j]);
						}else{
							item.put("id", "oracle"+itemId[j]+"*"+DBvo.getId()+"*"+DBvo.getIpAddress()+"_/"+DBvo.getAlias()+"__oracle"+"_^"+serialNum);
							item.put("pId", id);
							item.put("name", items_tmp[j]);
						}
						items.add(item);
					}
				}
			}
			//oracle���ݿ�����б�ռ�
			List oraTableSpaceList = new ArrayList();
			/*NmsSpacesDao nmsOraSpacesDao = new NmsSpacesDao();
			try {
				oraTableSpaceList = nmsOraSpacesDao.getOracleTablespaceNameById(realId);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				nmsOraSpacesDao.close();
			}
			if(oraTableSpaceList!=null && oraTableSpaceList.size()>0){
				for(int k=0;k<oraTableSpaceList.size();k++){
					Nms_oraspaces nms_oraspaces = (Nms_oraspaces) oraTableSpaceList.get(k);
					HashMap<String,Object> item = new HashMap<String,Object>();
					item.put("id", "oraclebiaokongjian"+"*"+nms_oraspaces.getServerip()+"*"+nms_oraspaces.getTablespace()+"*"+nms_oraspaces.getIp_address()+"_/"+nms_oraspaces.getAlias()+"__oracle");
					item.put("pId", "oracletablespace*"+nms_oraspaces.getId()+"*"+nms_oraspaces.getIp_address());
					item.put("name", nms_oraspaces.getTablespace());
					items.add(item);
				}
			 }*/
		}else if(id.startsWith("informix")) {
			//���ҵ��ڵ� ����ҵ���µ��豸
			String realId = id.replace("informix", "");
			
			int serialNum = 0;//�豸��� Ŀǰû�õ� Ԥ��
			List informixList = new ArrayList();
			DBDao dbdao = new DBDao();
			try {
				informixList = dbdao.findByCondition(" where id='"+realId+"' ");
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				dbdao.close();
			}
			if(null != informixList && informixList.size() > 0){
				for(int i=0; i<informixList.size(); i++){
					DBVo DBvo = (DBVo)informixList.get(i);
					String bidTmp = DBvo.getBid().replace(",","");
					String[] itemsTmp = { "��ͨ��", "Cache��������", "Cacheд������", "��ռ�", "Chunk"};
		  			String[] itemId = { "ping", "bufreadratio", "bufwriteratio", "tablespace", "chunk"};
					for(int j=0;j<itemsTmp.length;j++){
						String itemIdStr = itemId[j];
						HashMap<String,Object> item = new HashMap<String,Object>(); 
						if("tablespace".equals(itemIdStr) || "chunk".equals(itemIdStr)){
							item.put("id", "informix"+itemId[j]+"*"+DBvo.getId()+"*"+DBvo.getIpAddress());
							item.put("pId", id);
							item.put("name", itemsTmp[j]);
						}else{
							item.put("id", "informix"+itemId[j]+"*"+DBvo.getId()+"*"+DBvo.getIpAddress()+"_/"+DBvo.getAlias()+"__informix"+"_^"+serialNum);
							item.put("pId", id);
							item.put("name", itemsTmp[j]);
						}
						items.add(item);
					}
				}
			}
			//informix���ݿ�����б�ռ�
			/*List informixTableSpaceList = new ArrayList();
			NmsSpacesDao nmsOraSpacesDao = new NmsSpacesDao();
			try {
				informixTableSpaceList = nmsOraSpacesDao.getInformixTablespaceNameById(realId);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				nmsOraSpacesDao.close();
			}
			if(informixTableSpaceList!=null && informixTableSpaceList.size()>0){
				for(int k=0;k<informixTableSpaceList.size();k++){
					Nms_oraspaces spaces = (Nms_oraspaces) informixTableSpaceList.get(k);
					HashMap<String,Object> item = new HashMap<String,Object>();
					item.put("id", "informixbiaokongjian"+"*"+spaces.getServerip()+"*"+spaces.getTablespace()+"*"+spaces.getIp_address()+"_/"+spaces.getAlias()+"__informix");
					item.put("pId", "informixtablespace*"+spaces.getId()+"*"+spaces.getIp_address());
					item.put("name", spaces.getTablespace());
					items.add(item);
				}
			 }
			//informix���ݿ������chunk
			List informixChunkList = new ArrayList();
			nmsOraSpacesDao = new NmsSpacesDao();
			try {
				informixChunkList = nmsOraSpacesDao.getInformixChunkNameById(realId);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				nmsOraSpacesDao.close();
			}
			if(informixChunkList!=null && informixChunkList.size()>0){
				for(int k=0; k < informixChunkList.size(); k++){
					Nms_oraspaces spaces = (Nms_oraspaces) informixChunkList.get(k);
					HashMap<String,Object> item = new HashMap<String,Object>();
					item.put("id", "informixchunk"+"*"+spaces.getServerip()+"*"+spaces.getTablespace()+"*"+spaces.getIp_address()+"_/"+spaces.getAlias()+"__informix");
					item.put("pId", "informixchunk*"+spaces.getId()+"*"+spaces.getIp_address());
					item.put("name", spaces.getTablespace());
					items.add(item);
				}
			 }*/
		}else if(id.startsWith("sybase")) {
			//���ҵ��ڵ� ����ҵ���µ��豸
			String realId = id.replace("sybase", "");
			
			int serialNum = 0;//�豸��� Ŀǰû�õ� Ԥ��
			List sybaseList = new ArrayList();
			DBDao dbdao = new DBDao();
			try {
				sybaseList = dbdao.findByCondition(" where id='"+realId+"' ");
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				dbdao.close();
			}
			//sybase
			if(null != sybaseList && sybaseList.size() > 0){
				for(int i=0; i<sybaseList.size(); i++){
					DBVo DBvo = (DBVo)sybaseList.get(i);
					String bidTmp = DBvo.getBid().replace(",","");
					String[] items_tmp = { "��ͨ��", "��ռ�",};
		  			String[] itemId = { "ping", "tablespace"};
					//ddtree.insertNewItem("<%=bidTmp%>"+"yw","sybase"+"<%=DBvo.getId()%>","<%=DBvo.getIpAddress()%>"+"("+"<%=DBvo.getAlias()%>"+")", 0, "", "","", "");
					for(int j=0;j<items_tmp.length;j++){
						String itemIdStr = itemId[j];
						HashMap<String,Object> item = new HashMap<String,Object>(); 
						if("ping".equals(itemIdStr)){
							item.put("id", "sybase"+itemId[j]+"*"+DBvo.getId()+"*"+DBvo.getIpAddress()+"_/"+DBvo.getAlias()+"__sybase"+"_^"+serialNum);
							item.put("pId", id);
							item.put("name", items_tmp[j]);
						}else{
							item.put("id", "sybase"+itemId[j]+"*"+DBvo.getId()+"*"+DBvo.getIpAddress());
							item.put("pId", id);
							item.put("name", items_tmp[j]);
						}
						items.add(item);
					}
				}
			}
			//sybase���ݿ�����б�ռ�
			List sybaseTableSpaceList = new ArrayList();
			/*NmsSpacesDao nmsOraSpacesDao = new NmsSpacesDao();
			try {
				sybaseTableSpaceList = nmsOraSpacesDao.getSybaseTablespaceNameById(realId);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				nmsOraSpacesDao.close();
			}
			if(sybaseTableSpaceList!=null && sybaseTableSpaceList.size()>0){
				for(int k=0; k < sybaseTableSpaceList.size(); k++){
					Nms_oraspaces spaces = (Nms_oraspaces) sybaseTableSpaceList.get(k);
					HashMap<String,Object> item = new HashMap<String,Object>(); 
					item.put("id", "sybasebiaokongjian"+"*"+spaces.getServerip()+"*"+spaces.getTablespace()+"*"+spaces.getIp_address()+"_/"+spaces.getAlias()+"__sybase");
					item.put("pId", "sybasetablespace*"+spaces.getId()+"*"+spaces.getIp_address());
					item.put("name", spaces.getTablespace());
					items.add(item);
				}
			 }*/
		}else{
			//�������������豸
			List hostNetList = new ArrayList();
			HostNodeDao dao = new HostNodeDao();
			try {
				hostNetList = dao.findByCondition(" where id='"+id+"' ");
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				dao.close();
			}
			//�豸�ڵ�
			HostNode vo = new HostNode();
			int serialNum = 0;//�豸���
			if(hostNetList!=null&&hostNetList.size()>0){
				for(int i=0;i<hostNetList.size();i++){
					vo=(HostNode)hostNetList.get(i);
					String[] itemsArray={"��ͨ��","CPU","�ڴ�","������","ҳ������","ҳ������","�ļ�ϵͳ","���̷�æ��"};
		  			String[] itemId={"ping","cpu","mem","SwapMemory","pagein","pageout","file","diskbusy"};
					//������
					if(vo.getCategory() == 4){
						if(vo.getOstype() == 21){
							//scoOpenServer
							String[] items_tmp = {"��ͨ��","CPU","�ڴ�","������","�ļ�ϵͳ"};
				  			String[] itemId_tmp = {"ping","cpu","mem","SwapMemory","file"};
				  			itemsArray = items_tmp;
				  			itemId = itemId_tmp;
						}else if(vo.getOstype() == 5){
							//windows
							String[] items_tmp = {"��ͨ��","CPU","�ڴ�"};
				  			String[] itemId_tmp = {"ping","cpu","mem"};
				  			itemsArray = items_tmp;
				  			itemId = itemId_tmp;
						}
						for(int j=0;j<itemsArray.length;j++){
							String itemIdStr = itemId[j];
							HashMap<String,Object> item = new HashMap<String,Object>(); 
							if("file".equals(itemIdStr) || "diskbusy".equals(itemIdStr)){
								item.put("id", itemId[j]+vo.getIpAddress());
								item.put("pId", id);
								item.put("name", itemsArray[j]);
							}else{
								item.put("id", itemId[j]+vo.getIpAddress()+"_/"+vo.getAlias()+"__"+vo.getOstype()+"_^"+serialNum);
								item.put("pId", id);
								item.put("name", itemsArray[j]);
							}
							items.add(item);
						}
						if(vo.getOstype() == 21){/*
							//scoOpenServer
							//�ļ�������
							List fileList = new ArrayList();
							DiskconfigDao diskconfigDao = new DiskconfigDao();
							try {
								fileList = diskconfigDao.loadAllById(id);
							} catch (Exception e) {
								e.printStackTrace();
							} finally {
								diskconfigDao.close();
							}
							if(null != fileList && fileList.size() > 0){
								for(int q = 0; q < fileList.size(); q++){
									Diskconfig diskconfig = (Diskconfig) fileList.get(q);
									HashMap<String,Object> item = new HashMap<String,Object>(); 
									item.put("id", "file*"+diskconfig.getIpaddress()+"*"+diskconfig.getName());
									item.put("pId", "file"+diskconfig.getIpaddress());
									item.put("name", diskconfig.getName());
									items.add(item);
								}
							}
						*/}else if(vo.getOstype() == 5){
							//windows ��ʱû���� �ļ����
						}else {/*
							//�ļ�������
							List fileList = new ArrayList();
							DiskconfigDao diskconfigDao = new DiskconfigDao();
							try {
								fileList = diskconfigDao.loadAllById(id);
							} catch (Exception e) {
								e.printStackTrace();
							} finally {
								diskconfigDao.close();
							}
							if(null != fileList && fileList.size() > 0){
								for(int q = 0; q < fileList.size(); q++){
									Diskconfig diskconfig = (Diskconfig) fileList.get(q);
									HashMap<String,Object> item = new HashMap<String,Object>(); 
									item.put("id", "file*"+diskconfig.getIpaddress()+"*"+diskconfig.getName());
									item.put("pId", "file"+diskconfig.getIpaddress());
									item.put("name", diskconfig.getName());
									items.add(item);
								}
							}
							//���̷�æ��
							List diskbusyList = new ArrayList();
							DiskbusyconfigDao diskbusyconfigDao = new DiskbusyconfigDao();
							try {
									diskbusyList = diskbusyconfigDao.loadAllById(id);
							} catch (Exception e) {
								e.printStackTrace();
							} finally {
								diskbusyconfigDao.close();
							}
							if(null != diskbusyList && diskbusyList.size() > 0){
								for(int w = 0; w < diskbusyList.size(); w++){
									Diskbusyconfig diskbusyconfig = (Diskbusyconfig) diskbusyList.get(w);
									HashMap<String,Object> item = new HashMap<String,Object>(); 
									item.put("id", "diskbusy*"+diskbusyconfig.getIpaddress()+"*"+diskbusyconfig.getName());
									item.put("pId", "diskbusy"+diskbusyconfig.getIpaddress());
									item.put("name", diskbusyconfig.getName());
									items.add(item);
								}
							}
						*/}
					}
					//�����豸
					else if(vo.getCategory() == 1 || vo.getCategory() == 2 || vo.getCategory() == 8){
						String bidTmp = vo.getBid().replace(",","");
						
						String[] items_tmp = { "��ͨ��", "CPU", "�ڴ�", "�˿�" };
			  			String[] itemId_tmp = { "ping", "cpu", "mem", "port" };
			  			itemsArray = items_tmp;
			  			itemId = itemId_tmp;
						for(int j=0;j<itemsArray.length;j++){
							String itemIdStr = itemId[j];
							HashMap<String,Object> item = new HashMap<String,Object>(); 
							if("port".equals(itemIdStr)){
								item.put("id", itemId[j]+vo.getIpAddress());
								item.put("pId", id);
								item.put("name", itemsArray[j]);
							}else{
								item.put("id", itemId[j]+vo.getIpAddress()+"_/"+vo.getAlias()+"__"+vo.getCategory()+"_^"+serialNum);
								item.put("pId", id);
								item.put("name", itemsArray[j]);
							}
							items.add(item);
						}
						//�˿���Ϣ 
						List interfaceList = new ArrayList();
						PortconfigDao portconfigDao = new PortconfigDao();
						try {
							interfaceList = portconfigDao.getAllBySmsAndId(id);
						} catch (Exception e) {
							e.printStackTrace();
						} finally {
							portconfigDao.close();
						}
						if(interfaceList!=null&&interfaceList.size()>0){
							for(int k=0;k<interfaceList.size();k++){
								Portconfig node = (Portconfig) interfaceList.get(k);
								HashMap<String,Object> item = new HashMap<String,Object>();
								item.put("id", "port*"+node.getIpaddress()+"*"+node.getPortindex()+"*"+node.getName());
								item.put("pId", "port"+node.getIpaddress());
								item.put("name", node.getName());
								items.add(item);
							}
						 }
					}else if(vo.getCategory() == 14){
						String bidTmp = vo.getBid().replace(",","");
						
						String[] items_tmp = { "״̬������"};
			  			String[] itemId_tmp = { "emcstateRate"};
			  			itemsArray = items_tmp;
			  			itemId = itemId_tmp;
						for(int j=0;j<itemsArray.length;j++){
							HashMap<String,Object> item = new HashMap<String,Object>();
							item.put("id", itemId[j]+vo.getIpAddress()+"*"+vo.getId()+"_/"+vo.getAlias()+"__"+vo.getCategory()+"_^"+serialNum);
							item.put("pId", id);
							item.put("name", itemsArray[j]);
							items.add(item);
						}
					}
				}
			}
		}
		JSONArray json = JSONArray.fromObject(items);//ת��json��ʽ  
		out.print(json);
		out.flush();
		out.close();
	}
}
