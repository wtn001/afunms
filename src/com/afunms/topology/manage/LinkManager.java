/**
 * <p>Description:DiscoverManager</p>
 * <p>Company: dhcc.com</p>
 * @author afunms
 * @project afunms
 * @date 2006-08-12
 */

package com.afunms.topology.manage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import com.afunms.common.base.BaseManager;
import com.afunms.common.base.ErrorMessage;
import com.afunms.common.base.ManagerInterface;
import com.afunms.common.util.ChartXml;
import com.afunms.common.util.ExcelImportUtil;
import com.afunms.common.util.ShareData;
import com.afunms.common.util.SysLogger;
import com.afunms.discovery.RepairLink;
import com.afunms.indicators.model.NodeDTO;
import com.afunms.indicators.util.Constant;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.base.LinkRoad;
import com.afunms.polling.base.Node;
import com.afunms.polling.impl.IpResourceReport;
import com.afunms.polling.node.Host;
import com.afunms.polling.node.IfEntity;
import com.afunms.report.abstraction.ExcelReport1;
import com.afunms.report.base.AbstractionReport1;
import com.afunms.temp.model.Objbean;
import com.afunms.topology.dao.HintNodeDao;
import com.afunms.topology.dao.HostNodeDao;
import com.afunms.topology.dao.LineDao;
import com.afunms.topology.dao.LinkDao;
import com.afunms.topology.dao.ManageXmlDao;
import com.afunms.topology.dao.NodeDependDao;
import com.afunms.topology.dao.RepairLinkDao;
import com.afunms.topology.dao.TreeNodeDao;
import com.afunms.topology.model.HintLine;
import com.afunms.topology.model.HintNode;
import com.afunms.topology.model.HostNode;
import com.afunms.topology.model.Link;
import com.afunms.topology.model.ManageXml;
import com.afunms.topology.model.TreeNode;
import com.afunms.topology.service.TopoNodeInfoService;
import com.afunms.topology.util.ManageXmlOperator;
import com.afunms.topology.util.XmlOperator;

public class LinkManager extends BaseManager implements ManagerInterface
{
	private String list()
	{
		LinkDao dao = new LinkDao();
		setTarget("/topology/network/link_list.jsp");
		return list(dao);
	}
	//׼������ʾ����·
	private String readyAddLine(){
		String start_id = getParaValue("start_id");
		String s_alias = "";
    	String end_id = getParaValue("end_id");
    	String e_alias = "";
    	String xml = getParaValue("xml");
    	String start_x_y = getParaValue("start_x_y");
    	String end_x_y = getParaValue("end_x_y");
    	Node snode =null;
    	Node enode =null;
    	if("hin".equals(start_id.substring(0, 3))){
    		HintNodeDao hintNodeDao = new HintNodeDao();
    		HintNode hintNode = (HintNode) hintNodeDao.findById(start_id, xml);
    		if(hintNode!=null){
    			s_alias = hintNode.getName();
    		}
    	} else {
    		try {
    			TreeNodeDao treeNodeDao = new TreeNodeDao();
    			TreeNode svo = (TreeNode) treeNodeDao.findByNodeTag(start_id.substring(0, 3));
    			if(svo!=null){
    				snode = PollingEngine.getInstance().getNodeByCategory(svo.getName(), Integer.parseInt(start_id.substring(3)));
    				s_alias = snode.getAlias();
    			}
    		} catch (RuntimeException e) {
    			e.printStackTrace();
    		}
    	}
    	if("hin".equals(end_id.substring(0, 3))){
    		HintNodeDao hintNodeDao = new HintNodeDao();
    		HintNode hintNode = (HintNode) hintNodeDao.findById(end_id, xml);
    		if(hintNode!=null){
    			e_alias = hintNode.getName();
    		}
    	} else {
    		try {
    			TreeNodeDao treeNodeDao = new TreeNodeDao();
    			TreeNode evo = (TreeNode) treeNodeDao.findByNodeTag(end_id.substring(0, 3));
    			if(evo!=null){
    				enode = PollingEngine.getInstance().getNodeByCategory(evo.getName(), Integer.parseInt(end_id.substring(3)));
    				e_alias = enode.getAlias();
    			}
    		} catch (RuntimeException e) {
    			e.printStackTrace();
    		}
    	}
		
    	request.setAttribute("start_x_y",start_x_y);
    	request.setAttribute("end_x_y",end_x_y);
    	request.setAttribute("s_alias",s_alias);
    	request.setAttribute("e_alias",e_alias);
    	request.setAttribute("start_id",start_id);
    	request.setAttribute("end_id",end_id);
    	request.setAttribute("xml",xml);
		return "/topology/depend/addLine.jsp";
	}
	//����ʾ����·yangjun add 
	public String addDemoLink(String direction1,String xml,String line_name,String link_width,String start_id,
			String start_x_y,String s_alias,String end_id,String end_x_y,String e_alias){
		String returs = "error";
		String father_xy = "";
    	String child_xy = "";
    	String fatherid = "";
    	String childid = "";
    	String f_alias = "";
    	String c_alias = "";
    	if(direction1!=null&&direction1.equals("1")){
    		fatherid = start_id;
    		father_xy = start_x_y;
    		f_alias = s_alias;
    		childid = end_id;
    		child_xy = end_x_y;
    		c_alias = e_alias;
    	} else {
    		fatherid = end_id;
    		father_xy = end_x_y;
    		f_alias = e_alias;
    		childid = start_id;
    		child_xy = start_x_y;
    		c_alias = s_alias;
    	}
    	
    	LineDao lineDao = new LineDao();
    	try {
			HintLine hintLine = new HintLine();
			hintLine.setChildId(childid);
			hintLine.setChildXy(child_xy);
			hintLine.setFatherId(fatherid);
			hintLine.setFatherXy(father_xy);
			hintLine.setXmlfile(xml);
			hintLine.setLineName(line_name);
			hintLine.setWidth(Integer.parseInt(link_width));
			ManageXmlOperator mxmlOpr = new ManageXmlOperator();		
			mxmlOpr.setFile(xml);		
			mxmlOpr.init4updateXml();		
			int lineId = mxmlOpr.findMaxDemoLineId();
			hintLine.setLineId("hl"+lineId);
			if(lineDao.save(hintLine)){
				lineDao = new LineDao();
				HintLine vo = lineDao.findById("hl"+lineId, xml);
				mxmlOpr.addLine(vo.getId(),"hl"+lineId,fatherid, childid,link_width);
				if(xml.indexOf("businessmap")!=-1){//�����ҵ����ͼ
//				NodeDependDao nodeDependDao = new NodeDependDao();
//				if(!nodeDependDao.isNodeExist(fatherid, xml)){//�жϸ��ڵ��Ƿ������nms_node_depend���У������������
//					NodeDepend nodeDepend = new NodeDepend();
//					nodeDepend.setAlias(f_alias);
//					nodeDepend.setLocation(father_xy);
//					nodeDepend.setNodeId(fatherid);
//					nodeDepend.setXmlfile(xml);
//					NodeDependDao nodeDependDaos = new NodeDependDao();
//					nodeDependDaos.save(nodeDepend);
//				}
//	            if(!nodeDependDao.isNodeExist(childid, xml)){//�ж��ӽڵ��Ƿ������nms_node_depend���У������������
//					NodeDepend nodeDepend = new NodeDepend();
//					nodeDepend.setAlias(c_alias);
//					nodeDepend.setLocation(child_xy);
//					nodeDepend.setNodeId(childid);
//					nodeDepend.setXmlfile(xml);
//					NodeDependDao nodeDependDaos = new NodeDependDao();
//					nodeDependDaos.save(nodeDepend);
//				}
//	            nodeDependDao.close();
					ManageXmlDao subMapDao = new ManageXmlDao();
					
					ManageXml manageXml = (ManageXml) subMapDao.findByXml(xml);
					if(manageXml!=null){
						NodeDependDao nodeDependDao = new NodeDependDao();
						try{
						    List list = nodeDependDao.findByXml(xml);
						    ChartXml chartxml;
							chartxml = new ChartXml("NetworkMonitor","/"+xml.replace("jsp", "xml"));
							chartxml.addBussinessXML(manageXml.getTopoName(),list);
							ChartXml chartxmlList;
							chartxmlList = new ChartXml("NetworkMonitor","/"+xml.replace("jsp", "xml").replace("businessmap", "list"));
							chartxmlList.addListXML(manageXml.getTopoName(),list);
						}catch(Exception e){
						    e.printStackTrace();  
						    return "error";
						}finally{
							nodeDependDao.close();
			            }
					}
				}
				returs = "hl"+lineId;
			}
			mxmlOpr.writeXml();
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
		
		return returs;
		
	}
    //׼���༭ʾ����·
	private String readyEditLine(){
		String id = getParaValue("id");
		String f_alias = "";
		String c_alias = "";
		LineDao lineDao = new LineDao();
		HintLine vo = (HintLine) lineDao.findByID(id);
		lineDao.close();
		if("hin".equals(vo.getFatherId().substring(0, 3))){
    		HintNodeDao hintNodeDao = new HintNodeDao();
    		HintNode hintNode = (HintNode) hintNodeDao.findById(vo.getFatherId(), vo.getXmlfile());
    		if(hintNode!=null){
    			f_alias = hintNode.getName();
    		}
    	} else {
    		try {
    			TreeNodeDao treeNodeDao = new TreeNodeDao();
    			TreeNode svo = (TreeNode) treeNodeDao.findByNodeTag(vo.getFatherId().substring(0, 3));
    			if(svo!=null){
    				Node snode = PollingEngine.getInstance().getNodeByCategory(svo.getName(), Integer.parseInt(vo.getFatherId().substring(3)));
    				f_alias = snode.getAlias();
    			}
    		} catch (RuntimeException e) {
    			e.printStackTrace();
    		}
    	}
    	if("hin".equals(vo.getChildId().substring(0, 3))){
    		HintNodeDao hintNodeDao = new HintNodeDao();
    		HintNode hintNode = (HintNode) hintNodeDao.findById(vo.getChildId(), vo.getXmlfile());
    		if(hintNode!=null){
    			c_alias = hintNode.getName();
    		}
    	} else {
    		try {
    			TreeNodeDao treeNodeDao = new TreeNodeDao();
    			TreeNode evo = (TreeNode) treeNodeDao.findByNodeTag(vo.getChildId().substring(0, 3));
    			if(evo!=null){
    				Node enode = PollingEngine.getInstance().getNodeByCategory(evo.getName(), Integer.parseInt(vo.getChildId().substring(3)));
    				c_alias = enode.getAlias();
    			}
    		} catch (RuntimeException e) {
    			e.printStackTrace();
    		}
    	}
		request.setAttribute("father_id", vo.getFatherId());
		request.setAttribute("child_id", vo.getChildId());
		request.setAttribute("xml", vo.getXmlfile());
		request.setAttribute("line_name", vo.getLineName());
		request.setAttribute("line_width", vo.getWidth());
		request.setAttribute("line_id", vo.getLineId());
		request.setAttribute("id", vo.getId());
		request.setAttribute("father_x_y", vo.getFatherXy());
		request.setAttribute("child_x_y", vo.getChildXy());
		request.setAttribute("f_alias", f_alias);
		request.setAttribute("c_alias", c_alias);
		return "/topology/depend/editLine.jsp";
	}
    //����༭ʾ����·yangjun add 
	private String editDemoLink(){
		String id = getParaValue("id");
		String direction1 = getParaValue("direction1");
		String xml = getParaValue("xml");
		String line_name = getParaValue("line_name");
		String link_width = getParaValue("line_width");
		String line_id = getParaValue("line_id");
		String father_xy = "";
    	String child_xy = "";
    	String fatherid = "";
    	String childid = "";
    	if(direction1!=null&&direction1.equals("1")){
    		fatherid = getParaValue("child_id");
    		father_xy = getParaValue("child_x_y");
    		childid = getParaValue("father_id");
    		child_xy = getParaValue("father_x_y");
    	} else {
    		fatherid = getParaValue("father_id");
    		father_xy = getParaValue("father_x_y");
    		childid = getParaValue("child_id");
    		child_xy = getParaValue("child_x_y");
    	}
    	LineDao lineDao = new LineDao();
    	HintLine hintLine = new HintLine();
    	hintLine.setId(Integer.parseInt(id));
    	hintLine.setChildId(childid);
    	hintLine.setChildXy(child_xy);
    	hintLine.setFatherId(fatherid);
    	hintLine.setFatherXy(father_xy);
    	hintLine.setXmlfile(xml);
    	hintLine.setLineName(line_name);
    	hintLine.setWidth(Integer.parseInt(link_width));
    	hintLine.setLineId(line_id);
    	ManageXmlOperator mxmlOpr = new ManageXmlOperator();		
		mxmlOpr.setFile(xml);		
		mxmlOpr.init4updateXml();
		if(lineDao.update(hintLine)){
			if(mxmlOpr.isDemoLinkExist(line_id)){
				mxmlOpr.updateDemoLine(line_id, "lineWidth", link_width);
				if(!"".equals(line_name)){
					mxmlOpr.updateDemoLine(line_id, "lineInfo", line_name);	
				}
			} else {
				SysLogger.error("LinkManager.editDemoLink:"+"����ͼû�и���·");
			}
		}
		mxmlOpr.writeXml();
		
		return "/topology/network/change.jsp?customview=" + xml;
		
	
		
	}
	//ɾ��ʾ����·yangjun add 
	private String deleteDemoLink(){
		String id = getParaValue("id");
        String xml = getParaValue("xml");
        //ɾ��ʾ����·������
        LineDao lineDao = new LineDao();
        //����xml
        ManageXmlOperator mxmlOpr = new ManageXmlOperator();
        mxmlOpr.setFile(xml);
        mxmlOpr.init4updateXml();
        if(lineDao.delete(id, xml)){
        	NodeDependDao nodeDependDao = new NodeDependDao();
        	System.out.println(nodeDependDao.isNodeExist(id, xml)+"==============");
        	if(nodeDependDao.isNodeExist(id, xml)){
        		nodeDependDao.deleteByIdXml(id, xml);
        	} else {
        		nodeDependDao.close();
        	}
        	mxmlOpr.deleteDemoLinesByID(id);
        }
        mxmlOpr.writeXml();
        
        return "/topology/network/change.jsp?customview=" + xml;
	}
	
	//׼������ʵ����·yangjun add
	private String readyAddLink(){
		HostNodeDao dao = new HostNodeDao();
    	List list = dao.loadAll();
    	
    	String start_id = getParaValue("start_id");
    	String end_id = getParaValue("end_id");
    	int id1 = Integer.parseInt(start_id.substring(3));
    	int id2 = Integer.parseInt(end_id.substring(3));
    	String fileName = getParaValue("xml");
    	String alias1 = "";
    	String ipAddress1 = "";
    	String alias2 = "";
    	String ipAddress2 = "";
    	String link_name = "";
    	for(int i=0;i<list.size();i++){
    	     HostNode node = (HostNode)list.get(i);
    	     if(node.getId()==id1){
    	    	 alias1 = node.getAlias();
    	    	 ipAddress1 = node.getIpAddress();
    	     } 
    	     if(node.getId()==id2){
    	    	 alias2 = node.getAlias();
    	    	 ipAddress2 = node.getIpAddress();
    	     }
    	}
    	link_name = ipAddress1 +"/" + ipAddress2;
    	Host host1 = (Host)PollingEngine.getInstance().getNodeByID(id1);
    	Host host2 = (Host)PollingEngine.getInstance().getNodeByID(id2);
        
    	List<IfEntity> startHostIfentityList = getSortListByHash(host1.getInterfaceHash());
    	List<IfEntity> endHostIfentityList = getSortListByHash(host2.getInterfaceHash());
    	
    	request.setAttribute("startHostIfentityList", startHostIfentityList);
    	request.setAttribute("endHostIfentityList", endHostIfentityList);
    	request.setAttribute("start_if",host1.getInterfaceHash().values().iterator());
    	request.setAttribute("end_if",host2.getInterfaceHash().values().iterator());
    	request.setAttribute("alias_start",alias1);
    	request.setAttribute("ipAddress_start",ipAddress1);
    	request.setAttribute("alias_end",alias2);
    	request.setAttribute("ipAddress_end",ipAddress2);
    	request.setAttribute("start_id",start_id);
    	request.setAttribute("end_id",end_id);
    	request.setAttribute("link_name",link_name);
    	request.setAttribute("xml", fileName);
	    	    
        return "/topology/network/addLink.jsp";
	}
	
    //׼���༭ʵ����·yangjun add
	private String readyEditLink(){
		
		HostNodeDao dao = new HostNodeDao();
		
    	List list = dao.loadAll();
    	
    	String lineId = getParaValue("lineId");
    	
    	LinkDao linkdao = new LinkDao();
        Link link = (Link)linkdao.findByID(lineId);      
        linkdao.close();
        
        int startId = link.getStartId();
        int endId = link.getEndId();
        String startIndex = link.getStartIndex();
        String endIndex = link.getEndIndex();
    	
    	String alias_start = "";
    	String ipAddress_start = "";
    	String alias_end = "";
    	String ipAddress_end = "";
    	String link_name = link.getLinkName();
    	String linkAliasName = link.getLinkAliasName(); 
    	String max_speed = link.getMaxSpeed();
    	String linetext = link.getType()+"";
    	String showinterf = link.getShowinterf()+"";
    	String max_per = link.getMaxPer();
    	for(int i=0;i<list.size();i++){
    	     HostNode node = (HostNode)list.get(i);
    	     if(node.getId()==startId){
    	    	 alias_start = node.getAlias();
    	    	 ipAddress_start = node.getIpAddress();
    	     } 
    	     if(node.getId()==endId){
    	    	 alias_end = node.getAlias();
    	    	 ipAddress_end = node.getIpAddress();
    	     }
    	}
    	Host host1 = (Host)PollingEngine.getInstance().getNodeByID(startId);
    	Host host2 = (Host)PollingEngine.getInstance().getNodeByID(endId);
    	
    	if(host1==null){
			host1 = (Host) PollingEngine.getInstance().getDbByID(startId);
		}
        if(host2==null){
        	host2 = (Host) PollingEngine.getInstance().getDbByID(endId);
		}
        
    	request.setAttribute("start_if",host1.getInterfaceHash().values().iterator());
    	request.setAttribute("end_if",host2.getInterfaceHash().values().iterator());
    	request.setAttribute("alias_start",alias_start);
    	request.setAttribute("ipAddress_start",ipAddress_start);
    	request.setAttribute("alias_end",alias_end);
    	request.setAttribute("ipAddress_end",ipAddress_end);
    	request.setAttribute("start_id",new Integer(startId));
    	request.setAttribute("end_id",new Integer(endId));
    	request.setAttribute("start_index",startIndex);
    	request.setAttribute("end_index",endIndex);  
    	request.setAttribute("link_name",link_name);
    	request.setAttribute("max_speed",max_speed);
    	request.setAttribute("max_per",max_per);
    	request.setAttribute("linetext",linetext);
    	request.setAttribute("showinterf",showinterf);
    	request.setAttribute("id",lineId);
    	request.setAttribute("linkAliasName",linkAliasName);
	    	    
        return "/topology/network/editLink.jsp";
	}
	
	public synchronized static List<IfEntity> getSortListByHash(Hashtable<String, IfEntity> orignalHash){
		if(orignalHash == null){
			return null;
		}
		List<IfEntity> retList = new ArrayList<IfEntity>();
		Iterator<String> iterator = orignalHash.keySet().iterator();
		while(iterator.hasNext()){
			String key = iterator.next();
			retList.add(orignalHash.get(key));
		}
		Collections.sort(retList); 
		return retList;
	}
	
    private String readyAdd()
    {
    	HostNodeDao dao = new HostNodeDao();
    	List list = dao.loadNetwork(0);
    	
    	int startId = getParaIntValue("start_id");
    	int endId = getParaIntValue("end_id");
    	String startIndex = getParaValue("start_index");
    	String endIndex = getParaValue("end_index");
    	try{
    	if (startId == -1) {
				// System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~"+list);
				// if(list.size() == 0){
				// return "/topology/network/link_add.jsp";
				// }else{
				HostNode node = (HostNode) list.get(0);
				startId = node.getId();
				endId = node.getId();
				startIndex = "";
				endIndex = "";
				// }

			}
			Host startHost = (Host) PollingEngine.getInstance().getNodeByID(
					startId);
			Host endHost = (Host) PollingEngine.getInstance()
					.getNodeByID(endId);

			List<IfEntity> startHostIfentityList = getSortListByHash(startHost
					.getInterfaceHash());
			List<IfEntity> endHostIfentityList = getSortListByHash(endHost
					.getInterfaceHash());

			request
					.setAttribute("startHostIfentityList",
							startHostIfentityList);
			request.setAttribute("endHostIfentityList", endHostIfentityList);
			request.setAttribute("start_if", startHost.getInterfaceHash()
					.values().iterator());
			request.setAttribute("end_if", endHost.getInterfaceHash().values()
					.iterator());
			request.setAttribute("start_id", new Integer(startId));
			request.setAttribute("end_id", new Integer(endId));
			request.setAttribute("start_index", startIndex);
			request.setAttribute("end_index", endIndex);
			request.setAttribute("end_index", endIndex);
			request.setAttribute("startIp", startHost.getIpAddress());
			request.setAttribute("endIp", endHost.getIpAddress());
			request.setAttribute("list", list);	
    	}catch(Exception e)
        {
       //    return "/topology/network/link_add.jsp";
         }
    	
	    	    
        return "/topology/network/link_add.jsp";
    }
    
    private String readyEdit()
    {
    	HostNodeDao dao = new HostNodeDao();
    	List list = dao.loadNetwork(0);
    	
    	String id = getParaValue("checkbox");
    	
	    String startIndex = getParaValue("start_index");
	    String endIndex = getParaValue("end_index");
	    int startId = getParaIntValue("start_id");
	    int endId = getParaIntValue("end_id");
    	
        LinkDao linkdao = new LinkDao();
        Link link = (Link)linkdao.findByID(id);      
        
        if(startId == -1) startId = link.getStartId();
        if(endId == -1) endId = link.getEndId();
    	if(startIndex == null) startIndex = link.getStartIndex();
    	if(endIndex == null) endIndex = link.getEndIndex();
    	
    	if(startId==-1)
    	{
    		HostNode node = (HostNode)list.get(0);
    		startId = node.getId();
    		endId = node.getId();
    		startIndex = "";
    		endIndex = "";
    	}
    	Host startHost = (Host)PollingEngine.getInstance().getNodeByID(startId);
    	Host endHost = (Host)PollingEngine.getInstance().getNodeByID(endId);
    	
    	List<IfEntity> startHostIfentityList = getSortListByHash(startHost.getInterfaceHash());
    	List<IfEntity> endHostIfentityList = getSortListByHash(endHost.getInterfaceHash());
    	
    	request.setAttribute("interf", link.getShowinterf()+"");
    	request.setAttribute("linetext", link.getType()+"");
    	request.setAttribute("maxPer", link.getMaxPer());
    	request.setAttribute("maxSpeed", link.getMaxSpeed());
    	request.setAttribute("linkName", link.getLinkName());
    	request.setAttribute("startHostIfentityList", startHostIfentityList);
    	request.setAttribute("endHostIfentityList", endHostIfentityList);
    	request.setAttribute("start_if",startHost.getInterfaceHash().values().iterator());
    	request.setAttribute("end_if",endHost.getInterfaceHash().values().iterator());
    	request.setAttribute("start_id",new Integer(startId));
    	request.setAttribute("end_id",new Integer(endId));
    	request.setAttribute("start_index",startIndex);
    	request.setAttribute("end_index",endIndex);   
    	request.setAttribute("startIp",startHost.getIpAddress());    	
    	request.setAttribute("endIp",endHost.getIpAddress()); 
    	request.setAttribute("id",id);
	    request.setAttribute("list",list);	
	    request.setAttribute("linkAliasName", link.getLinkAliasName());  // hipo
	    request.setAttribute("startAliasName", startHost.getAlias());    // hipo
	    request.setAttribute("endAliasName", endHost.getAlias());        // hipo
        return "/topology/network/link_edit.jsp";
    }
    
    private String add() 
    {  	
	    String startIndex = getParaValue("start_index");
	    String endIndex = getParaValue("end_index");
	    int startId = getParaIntValue("start_id");
	    int endId = getParaIntValue("end_id");
	    String linkName = getParaValue("link_name");
	    String maxSpeed = getParaValue("max_speed");
	    String maxPer = getParaValue("max_per");
	    String interf = getParaValue("interf");
	    String linkAliasName = getParaValue("link_alias_name");
	    
	    if(startId==endId)
	    {
	    	setErrorCode(ErrorMessage.DEVICES_SAME);
	    	return null;
	    }
	    
	    LinkDao dao = new LinkDao();
	    int exist = dao.linkExist(startId, startIndex,endId,endIndex);	    
	    if(exist==1)
	    {
	    	setErrorCode(ErrorMessage.LINK_EXIST);
	    	dao.close();
	    	return null;
	    }	
	    if(exist==2)
	    {
	    	setErrorCode(ErrorMessage.DOUBLE_LINKS);
	    	dao.close();
	    	return null;
	    }	
		Host startHost = (Host)PollingEngine.getInstance().getNodeByID(startId);
		IfEntity if1 = startHost.getIfEntityByIndex(startIndex);
		Host endHost = (Host)PollingEngine.getInstance().getNodeByID(endId);
		IfEntity if2 = endHost.getIfEntityByIndex(endIndex);
//		System.err.println("============================="+endHostName+"===========================");
//		System.err.println("============================="+startHostName+"===========================");
		Link link = new Link();
	    link.setStartId(startId);
	    link.setEndId(endId);
        link.setStartIndex(startIndex);
        link.setEndIndex(endIndex);
        link.setStartIp(startHost.getIpAddress());
        link.setEndIp(endHost.getIpAddress());
        link.setStartDescr(if1.getDescr());
        link.setEndDescr(if2.getDescr());
        link.setLinkName(linkName);//yangjun add
	    link.setMaxSpeed(maxSpeed);//yangjun add
	    link.setMaxPer(maxPer);//yangjun add
        link.setShowinterf(Integer.parseInt(interf));
        link.setType(1);
        link.setLinkAliasName(linkAliasName);
	    Link newLink = dao.save(link);
	    
 	    //����xml
	    XmlOperator opr = new XmlOperator();
	    opr.setFile("network.jsp");
	    opr.init4updateXml();
	    if(newLink.getAssistant()==0)
	       opr.addLine(String.valueOf(newLink.getId()),"net"+String.valueOf(startId),"net"+String.valueOf(endId));
	    else
	       opr.addAssistantLine(String.valueOf(newLink.getId()),"net"+String.valueOf(startId),"net"+String.valueOf(endId));	
	    opr.writeXml();
	    
	    //��·��Ϣʵʱ����
		LinkRoad lr = new LinkRoad();
		lr.setId(newLink.getId());
		lr.setShowinterf(newLink.getShowinterf());
		lr.setMaxPer(newLink.getMaxPer());
		lr.setMaxSpeed(newLink.getMaxSpeed());
		lr.setLinkName(newLink.getLinkName());
		lr.setStartId(newLink.getStartId());
		lr.setStartIp(newLink.getStartIp());			
		lr.setStartIndex(newLink.getStartIndex());
		lr.setStartDescr(newLink.getStartDescr());
		lr.setEndIp(newLink.getEndIp());				
		lr.setEndId(newLink.getEndId());
		lr.setEndIndex(newLink.getEndIndex());
		lr.setEndDescr(newLink.getEndDescr());
		lr.setAssistant(newLink.getAssistant());
		PollingEngine.getInstance().getLinkList().add(lr);

        return "/link.do?action=list";
    }  
    //������· yangjun add
    public String addLink(String direction1,String linkName,String maxSpeed,String maxPer,String xml,String start_id,String start_index,String end_id,String end_index,String linetext,String interf) 
    {  	
    	String returns = "error";
    	String startIndex = "";
    	String endIndex = "";
    	String start_Id = "";
    	String end_Id = "";
    	if(direction1!=null&&direction1.equals("1")){//�����豸
    		startIndex = start_index;
    		endIndex = end_index;
    		start_Id = start_id;
    		end_Id = end_id;
    	} else {//�����豸
    		startIndex = end_index;
    		endIndex = start_index;
    		start_Id = end_id;
    		end_Id = start_id;
    	}
    	if(!"".equals(start_Id)&&!"".equals(end_Id)){
    		int startId = Integer.parseInt(start_Id.substring(3));
        	int endId = Integer.parseInt(end_Id.substring(3));

    	    LinkDao dao = new LinkDao();
    	    try {
    			String exist = dao.linkExists(startId,startIndex,endId,endIndex);	
    			XmlOperator xopr = new XmlOperator();
				xopr.setFile(xml);
				xopr.init4updateXml();
    			if(exist.split(":").length>1&&exist.split(":")[1].equals("1"))
    			{
    				if(xopr.isLinkExist(exist.split(":")[0])){
    					setErrorCode(ErrorMessage.LINK_EXIST);
        				dao.close();
        				return "error1";
    				} else if(xopr.isAssLinkExist(exist.split(":")[0])){
    					setErrorCode(ErrorMessage.LINK_EXIST);
        				dao.close();
        				return "error1";
    				} else {
    					if(exist.split(":")[2].equals("0")){
    						if(xopr.isNodeExist(start_Id)&&xopr.isNodeExist(end_Id)){
        						xopr.addLine(linkName,String.valueOf(exist.split(":")[0]),start_Id,end_Id);
        					}
        					xopr.writeXml();
    					}else if(exist.split(":")[2].equals("1")){
    						if(xopr.isNodeExist(start_Id)&&xopr.isNodeExist(end_Id)){
            					xopr.addAssistantLine(linkName,String.valueOf(exist.split(":")[0]),start_Id,end_Id);
        					}
        					xopr.writeXml();
    					}
    					return exist.split(":")[0]+":"+exist.split(":")[2];
    				}
    			}	
//    			System.out.println(exist.split(":")[1]);
//    			System.out.println("3:"+exist.split(":")[1].equals("2"));
//    			SysLogger.info("===>"+exist.split(":")[2]+" "+exist.split(":")[4]);
    			System.out.println("exist===>"+exist);
    			if(exist.split(":").length>3&&exist.split(":")[1].equals("2"))
    			{
    				if(exist.split(":")[0].equals("2")){
    					if((xopr.isLinkExist(exist.split(":")[2])&&xopr.isAssLinkExist(exist.split(":")[4]))||(xopr.isLinkExist(exist.split(":")[4])&&xopr.isAssLinkExist(exist.split(":")[2]))){
    						setErrorCode(ErrorMessage.DOUBLE_LINKS);
	        				dao.close();
	        				return "error2";
    					} else {
    						if(exist.split(":")[3].equals("0")&&!xopr.isLinkExist(exist.split(":")[2])){
    							if(xopr.isNodeExist(start_Id)&&xopr.isNodeExist(end_Id)){
            						xopr.addLine(linkName,String.valueOf(exist.split(":")[2]),start_Id,end_Id);
            					}
            					xopr.writeXml();
            					return exist.split(":")[2]+":"+exist.split(":")[3];
    						}else if(exist.split(":")[3].equals("1")&&!xopr.isAssLinkExist(exist.split(":")[2])){
    							if(xopr.isNodeExist(start_Id)&&xopr.isNodeExist(end_Id)){
            						xopr.addAssistantLine(linkName,String.valueOf(exist.split(":")[2]),start_Id,end_Id);
            					}
            					xopr.writeXml();
            					return exist.split(":")[2]+":"+exist.split(":")[3];
    						}
    						if(exist.split(":")[5].equals("0")&&!xopr.isLinkExist(exist.split(":")[4])){
    							if(xopr.isNodeExist(start_Id)&&xopr.isNodeExist(end_Id)){
            						xopr.addLine(linkName,String.valueOf(exist.split(":")[4]),start_Id,end_Id);
            					}
            					xopr.writeXml();
            					return exist.split(":")[4]+":"+exist.split(":")[5];
    						}else if(exist.split(":")[5].equals("1")&&!xopr.isAssLinkExist(exist.split(":")[4])){
    							if(xopr.isNodeExist(start_Id)&&xopr.isNodeExist(end_Id)){
            						xopr.addAssistantLine(linkName,String.valueOf(exist.split(":")[4]),start_Id,end_Id);
            					}
            					xopr.writeXml();
            					return exist.split(":")[4]+":"+exist.split(":")[5];
    						}
    					}
    				}
//    				if(xopr.isAssLinkExist(exist.split(":")[0])){
//    					setErrorCode(ErrorMessage.DOUBLE_LINKS);
//        				dao.close();
//        				return "error2";
//    				}else {
//    					if(xopr.isNodeExist(start_Id)&&xopr.isNodeExist(end_Id)){
//        					xopr.addAssistantLine(linkName,String.valueOf(exist.split(":")[0]),start_Id,end_Id);
//    					}
//    					xopr.writeXml();
//    					return exist.split(":")[0]+":"+exist.split(":")[2];
//    				}
    			}
    		} catch (Exception e) {
    			e.printStackTrace();
    			return "error";
    		}
    		Host startHost = (Host)PollingEngine.getInstance().getNodeByID(startId);
    		IfEntity if1 = startHost.getIfEntityByIndex(startIndex);
    		Host endHost = (Host)PollingEngine.getInstance().getNodeByID(endId);
    		IfEntity if2 = endHost.getIfEntityByIndex(endIndex);
    	    
    	    Link link = new Link();
    	    link.setLinkName(linkName);//yangjun add
    	    link.setMaxSpeed(maxSpeed);//yangjun add
    	    link.setMaxPer(maxPer);//yangjun add
    	    link.setStartId(startId);
    	    link.setEndId(endId);
            link.setStartIndex(startIndex);
            link.setEndIndex(endIndex);
            link.setStartIp(startHost.getIpAddress());
            link.setEndIp(endHost.getIpAddress());
            link.setStartDescr(if1.getDescr());
            link.setEndDescr(if2.getDescr());
            link.setType(Integer.parseInt(linetext));
            link.setShowinterf(Integer.parseInt(interf));
    	    Link newLink = null;
    	    try{
    	    	newLink = dao.save(link);
    	    }catch(Exception e){
    	    	e.printStackTrace();
    	    	return "error";
    	    }finally{
    	    	dao.close();
    	    }
    	    if(newLink!=null){
//    	    	������������ͼ
    	        try {
//    				ManageXmlDao mdao = new ManageXmlDao();
//    				List<ManageXml> list = mdao.loadAll();
//    				if(list!=null&&list.size()>0){
//    					for(int i = 0;i < list.size(); i++){
//    						ManageXml manageXml = list.get(i);
//    						XmlOperator xopr = new XmlOperator();
//    						xopr.setFile(manageXml.getXmlName());
//    						xopr.init4updateXml();
//    						if(xopr.isNodeExist(start_Id)&&xopr.isNodeExist(end_Id)){
//    							if(newLink.getAssistant()==0)
//    								xopr.addLine(linkName,String.valueOf(newLink.getId()),start_Id,end_Id);
//    						    else
//    						    	xopr.addAssistantLine(linkName,String.valueOf(newLink.getId()),start_Id,end_Id);	
//    						}
//    						xopr.writeXml();
//    					}
//    				}
    	        	XmlOperator xopr = new XmlOperator();
					xopr.setFile(xml);
					xopr.init4updateXml();
					if(xopr.isNodeExist(start_Id)&&xopr.isNodeExist(end_Id)){
//						System.out.println("4:"+newLink.getAssistant());
						if(newLink.getAssistant()==0)
							xopr.addLine(linkName,String.valueOf(newLink.getId()),start_Id,end_Id);
					    else
					    	xopr.addAssistantLine(linkName,String.valueOf(newLink.getId()),start_Id,end_Id);	
					}
					xopr.writeXml();
    			} catch (Exception e) {
    				e.printStackTrace();
    				return "error";
    			} 
    		    //��·��Ϣʵʱ����
    			LinkRoad lr = new LinkRoad();
    			lr.setId(newLink.getId());
    			lr.setLinkName(newLink.getLinkName());
    			lr.setLinkName(newLink.getLinkName());//yangjun add
    			lr.setMaxSpeed(newLink.getMaxSpeed());//yangjun add
    			lr.setMaxPer(newLink.getMaxPer());//yangjun add
    			lr.setStarOper(if1.getOperStatus()==1?"up":"down");//wangdaju add
    			lr.setEndOper(if2.getOperStatus()==1?"up":"down");//wangdaju add
    			lr.setStartId(newLink.getStartId());
    			lr.setStartIp(newLink.getStartIp());		
    			lr.setStartIndex(newLink.getStartIndex());
    			lr.setStartDescr(newLink.getStartDescr());
    			lr.setEndIp(newLink.getEndIp());				
    			lr.setEndId(newLink.getEndId());
    			lr.setEndIndex(newLink.getEndIndex());
    			lr.setEndDescr(newLink.getEndDescr());
    			lr.setAssistant(newLink.getAssistant());
    			lr.setType(newLink.getType());
    			lr.setShowinterf(newLink.getShowinterf());
    			PollingEngine.getInstance().getLinkList().add(lr);
    			returns = newLink.getId()+":"+newLink.getAssistant();
    	    }
    	}
        //return "/topology/network/change.jsp?customview=" + xml;
		return returns;
    }
    //����༭��· yangjun add
    private String editLink() 
    {  	   	
    	String direction1 = getParaValue("direction1");
    	String startIndex = "";
    	String endIndex = "";
    	int startId = 0;
    	int endId = 0;
    	if(direction1!=null&&direction1.equals("1")){//�����豸
    		startIndex = getParaValue("end_index");
    		endIndex = getParaValue("start_index");
    		startId = getParaIntValue("end_id");
    		endId = getParaIntValue("start_id");
    	} else {//�����豸
    		startIndex = getParaValue("start_index");
    		endIndex = getParaValue("end_index");
    		startId = getParaIntValue("start_id");
    		endId = getParaIntValue("end_id");
    	}
    	String linkName = getParaValue("link_name");
    	String linkAliasName = getParaValue("link_alias_name");
    	String maxSpeed = getParaValue("max_speed");
    	String maxPer = getParaValue("max_per");
    	String linetext = getParaValue("linetext");
    	String interf = getParaValue("interf");
    	String id = getParaValue("id");
	    if(startId==endId)
	    {
	    	setErrorCode(ErrorMessage.DEVICES_SAME);
	    	return null;
	    }
	    
	    LinkDao dao = new LinkDao();
	    RepairLinkDao repairdao = new RepairLinkDao();
	    Link formerLink = (Link)dao.findByID(id);
	    dao.close();
	    String formerStartIndex = formerLink.getStartIndex();
	    String formerEndIndex = formerLink.getEndIndex();

		Host startHost = (Host)PollingEngine.getInstance().getNodeByID(startId);
		IfEntity if1 = startHost.getIfEntityByIndex(startIndex);
		Host endHost = (Host)PollingEngine.getInstance().getNodeByID(endId);
		IfEntity if2 = endHost.getIfEntityByIndex(endIndex);
		
		String startHostName = startHost.getSysName();
		String endHostName = endHost.getSysName();
	   
		RepairLink repairLink = null;
	    repairLink = repairdao.loadLink(startHost.getIpAddress(), formerStartIndex, endHost.getIpAddress(), formerEndIndex);
		
		//formerLink.setStartId(startId);
		
	    //Link link = new Link();
	    formerLink.setLinkAliasName(linkAliasName);
	    formerLink.setLinkName(linkName);//yangjun add
	    formerLink.setMaxSpeed(maxSpeed);//yangjun add
	    formerLink.setMaxPer(maxPer);//yangjun add
	    formerLink.setStartIndex(startIndex);
	    formerLink.setEndIndex(endIndex);
	    formerLink.setStartDescr(if1.getDescr());
	    formerLink.setEndDescr(if2.getDescr());
	    formerLink.setType(Integer.parseInt(linetext));
	    formerLink.setShowinterf(Integer.parseInt(interf));
	    dao = new LinkDao();
	    dao.update(formerLink);
	    
	    //�����޸ĵ����ӹ�ϵ����ԭʼ����
	    if(repairLink == null){
	    	//��Ҫ���жϸ����ӹ�ϵ�Ƿ��Ѿ����޸Ĺ�
	    	repairLink = repairdao.loadRepairLink(startHost.getIpAddress(), formerStartIndex, endHost.getIpAddress(), formerEndIndex);
	    	if(repairLink == null){
	    		//˵���ǵ�һ���޸�
		    	repairLink = new RepairLink();
		    	repairLink.setStartIp(startHost.getIpAddress());
		    	repairLink.setStartIndex(formerStartIndex);
		    	repairLink.setNewStartIndex(formerLink.getStartIndex());
		    	repairLink.setEndIp(endHost.getIpAddress());
		    	repairLink.setEndIndex(formerEndIndex);
		    	repairLink.setNewEndIndex(formerLink.getEndIndex());
		    	repairdao.save(repairLink);
	    	}else{
	    		//�������޸Ĺ�
		    	repairLink.setNewStartIndex(formerLink.getStartIndex());
		    	repairLink.setNewEndIndex(formerLink.getEndIndex());
		    	System.out.println("�޸����ӹ�ϵ!");
		    	repairdao.update(repairLink); 		
	    	}
	    }else{
	    	repairLink.setNewStartIndex(formerLink.getStartIndex());
	    	repairLink.setNewEndIndex(formerLink.getEndIndex());
	    	repairdao.update(repairLink);
	    }
	    
		LinkRoad lr = new LinkRoad();
		lr.setId(formerLink.getId());
		lr.setLinkName(formerLink.getLinkName());//yangjun add
        lr.setMaxSpeed(formerLink.getMaxSpeed());//yangjun add
        lr.setMaxPer(formerLink.getMaxPer());//yangjun add
        lr.setStarOper(if1.getOperStatus()==1?"up":"down");//wangdaju add
        lr.setEndOper(if2.getOperStatus()==1?"up":"down");//wangdaju add
		lr.setStartId(formerLink.getStartId());
		lr.setStartIp(formerLink.getStartIp());		
		lr.setStartIndex(formerLink.getStartIndex());
		lr.setStartDescr(formerLink.getStartDescr());
		lr.setEndIp(formerLink.getEndIp());	
		lr.setEndId(formerLink.getEndId());
		lr.setEndIndex(formerLink.getEndIndex());
		lr.setEndDescr(formerLink.getEndDescr());
		lr.setAssistant(formerLink.getAssistant());
		lr.setType(formerLink.getType());
		lr.setShowinterf(formerLink.getShowinterf());
		PollingEngine.getInstance().deleteLinkByID(lr.getId());
		PollingEngine.getInstance().getLinkList().add(lr);
		
        return null;
    }
    	
    private String edit() 
    {  	   	
    	String id = getParaValue("id");
	    String startIndex = getParaValue("start_index");
	    String endIndex = getParaValue("end_index");
	    int startId = getParaIntValue("startId");
	    int endId = getParaIntValue("endId");

	    String linkName = getParaValue("link_name");
	    String linkAliasName = getParaValue("link_alias_name");
	    String maxSpeed = getParaValue("max_speed");
	    String maxPer = getParaValue("max_per");
	    String interf = getParaValue("interf");
	    
	    if(startId==endId)
	    {
	    	setErrorCode(ErrorMessage.DEVICES_SAME);
	    	return null;
	    }
	    
	    LinkDao dao = new LinkDao();
	    RepairLinkDao repairdao = new RepairLinkDao();
	    Link formerLink = (Link)dao.findByID(id);
	    String formerStartIndex = formerLink.getStartIndex();
	    String formerEndIndex = formerLink.getEndIndex();

	    //��Ҫ�ж�ԭ���Ƿ��Ѿ��Ǳ��޸Ĺ�������
	    
	    
	    //���Ѿ����ڵ����ӽ����޸�,���Բ���Ҫ�ж��Ƿ����
	    /*
	    int exist = dao.linkExist(startId, startIndex,endId,endIndex);	    
	    if(exist==1)
	    {
	    	setErrorCode(ErrorMessage.LINK_EXIST);
	    	dao.close();
	    	return null;
	    }	
	    if(exist==2)
	    {
	    	setErrorCode(ErrorMessage.DOUBLE_LINKS);
	    	dao.close();
	    	return null;
	    }
	    */
		Host startHost = (Host)PollingEngine.getInstance().getNodeByID(startId);
		IfEntity if1 = startHost.getIfEntityByIndex(startIndex);
		Host endHost = (Host)PollingEngine.getInstance().getNodeByID(endId);
		IfEntity if2 = endHost.getIfEntityByIndex(endIndex);
		
	    RepairLink repairLink = null;
	    repairLink = repairdao.loadLink(startHost.getIpAddress(), formerStartIndex, endHost.getIpAddress(), formerEndIndex);
		
		//formerLink.setStartId(startId);
		
	    //Link link = new Link();
	    formerLink.setMaxSpeed(maxSpeed);
	    formerLink.setMaxPer(maxPer);
	    formerLink.setShowinterf(Integer.parseInt(interf));
	    formerLink.setLinkName(linkName);
	    formerLink.setStartId(startId);
	    formerLink.setEndId(endId);
	    formerLink.setStartIndex(startIndex);
	    formerLink.setEndIndex(endIndex);
	    formerLink.setStartIp(startHost.getIpAddress());
	    formerLink.setEndIp(endHost.getIpAddress());
	    formerLink.setStartDescr(if1.getDescr());
	    formerLink.setEndDescr(if2.getDescr());
	    formerLink.setType(1);
	    formerLink.setLinkAliasName(linkAliasName);
	    dao = new LinkDao();
	    dao.update(formerLink);
	    
	    //�����޸ĵ����ӹ�ϵ����ԭʼ����
	    if(repairLink == null){
	    	//��Ҫ���жϸ����ӹ�ϵ�Ƿ��Ѿ����޸Ĺ�
	    	repairLink = repairdao.loadRepairLink(startHost.getIpAddress(), formerStartIndex, endHost.getIpAddress(), formerEndIndex);
	    	if(repairLink == null){
	    		//˵���ǵ�һ���޸�
		    	repairLink = new RepairLink();
		    	repairLink.setStartIp(startHost.getIpAddress());
		    	repairLink.setStartIndex(formerStartIndex);
		    	repairLink.setNewStartIndex(formerLink.getStartIndex());
		    	repairLink.setEndIp(endHost.getIpAddress());
		    	repairLink.setEndIndex(formerEndIndex);
		    	repairLink.setNewEndIndex(formerLink.getEndIndex());
		    	repairdao.save(repairLink);
	    	}else{
	    		//�������޸Ĺ�
		    	repairLink.setNewStartIndex(formerLink.getStartIndex());
		    	repairLink.setNewEndIndex(formerLink.getEndIndex());
		    	System.out.println("�޸����ӹ�ϵ!");
		    	repairdao.update(repairLink); 		
	    	}
	    }else{
	    	repairLink.setNewStartIndex(formerLink.getStartIndex());
	    	repairLink.setNewEndIndex(formerLink.getEndIndex());
	    	repairdao.update(repairLink);
	    }
	    
 	    //����xml
	    XmlOperator opr = new XmlOperator();
	    opr.setFile("network.jsp");
	    opr.init4updateXml();
	    /*
	    if(formerLink.getAssistant()==0)
	       opr.addLine(String.valueOf(formerLink.getId()),String.valueOf(startId),String.valueOf(endId));
	    else
	       opr.addAssistantLine(String.valueOf(formerLink.getId()),String.valueOf(startId),String.valueOf(endId));	
	    */
	    opr.writeXml();
	    
		LinkRoad lr = new LinkRoad();
		lr.setLinkName(formerLink.getLinkName());
		lr.setShowinterf(formerLink.getShowinterf());
		lr.setMaxPer(formerLink.getMaxPer());
		lr.setMaxSpeed(formerLink.getMaxSpeed());
		lr.setId(formerLink.getId());
		lr.setStartId(startId);
		if("".equals(if1.getIpAddress()))
		   lr.setStartIp(startHost.getIpAddress());
		else
		   lr.setStartIp(if1.getIpAddress());			
		lr.setStartIndex(startIndex);
		lr.setStartDescr(if1.getDescr());
		
	    if("".equals(if2.getIpAddress()))
		   lr.setEndIp(endHost.getIpAddress());
		else
		   lr.setEndIp(if2.getIpAddress());					
		lr.setEndId(endId);
		//lr.setEndIp(if2.getIpAddress());
		lr.setEndIndex(endIndex);
		lr.setEndDescr(if2.getDescr());
		lr.setAssistant(formerLink.getAssistant());
		PollingEngine.getInstance().deleteLinkByID(lr.getId());
		PollingEngine.getInstance().getLinkList().add(lr);
		
        return "/link.do?action=list";
    }
    
    private String delete(){
    	String[] ids = getParaArrayValue("checkbox");
		if (ids != null && ids.length > 0) {
			for (int i = 0; i < ids.length; i++) {
				String id = ids[i];
				// �������ݿ�
				LinkDao dao = new LinkDao();
				dao.delete(id);
				// ����xml
				XmlOperator opr = new XmlOperator();
				opr.setFile("network.jsp");
				opr.init4updateXml();
				opr.deleteLineByID(id);
				opr.writeXml();

				// �����ڴ�
				ShareData.getAllLinkData().remove(Integer.parseInt(id));
				PollingEngine.getInstance().deleteLinkByID(Integer.parseInt(id));
			}
		}
        return "/link.do?action=list";
    }
    
    // ɾ��ʵ����· yangjun add
    private String deleteLink()
    {
        String id = getParaValue("lineId"); 
        String xml = getParaValue("xml");
        //������������ͼ
        ManageXmlDao mdao = new ManageXmlDao();
        List<ManageXml> list = mdao.loadAll();
        if(list.size()>0){
        	for(int i = 0;i < list.size(); i++){
        		ManageXml manageXml = list.get(i);
        		XmlOperator xopr = new XmlOperator();
        		xopr.setFile(manageXml.getXmlName());
        		xopr.init4updateXml();
        		if(xopr.isLinkExist(id)){
        			xopr.deleteLineByID(id);
        		}
        		if(xopr.isAssLinkExist(id)){
        			xopr.deleteAssLineByID(id);
        		}
        		xopr.writeXml();
        	}
        }
        //�������ݿ�
        LinkDao dao = new LinkDao();
        try{
        	dao.delete(id);
        }catch(Exception e){
        	e.printStackTrace();
        }finally{
        	dao.close();
        }
        //�����ڴ�
        ShareData.getAllLinkData().remove(Integer.parseInt(id));
        PollingEngine.getInstance().deleteLinkByID(Integer.parseInt(id));
        return "/topology/network/change.jsp?customview=" + xml;
    }
    
    private String downloadlinklist()
    {

 		//Hashtable allcpuhash = new Hashtable();
 		
 		//int startrow = (Integer)session.getAttribute("linkstartrow");
 		Hashtable reporthash = new Hashtable();
 		//reporthash.put("startrow", startrow);
 		AbstractionReport1 report = new ExcelReport1(new IpResourceReport(),
 				reporthash);
 		
 		report.createReport_linklist("/temp/linklist_report.xls");
 		request.setAttribute("filename", report.getFileName());
 		return "/topology/network/downloadreport.jsp";
    }
    
    private String backuplinklist()
    {
    	
    	
    	Hashtable reporthash = new Hashtable();
    	AbstractionReport1 report = new ExcelReport1(new IpResourceReport(),
    			reporthash);
    	
    	report.backup_linklist("/temp/backup_linklist.xls");
    	request.setAttribute("filename", report.getFileName());
    	return "/topology/network/downloadreport.jsp";
    }
    
    /**
     * �����޸���·��ֵ
     * @return
     */
    public String batchEditLink(String linkids,String maxPer,String  maxSpeed){
    	String retStr = "true";
		LinkDao linkdao = new LinkDao();
    	try {
    		linkdao.batchUpdateThresholdLevel(linkids, maxPer, maxSpeed);
		} catch (Exception e) {
			e.printStackTrace();
			retStr = "false";
		} finally{
			linkdao.close();
		}
		return retStr;
    }
   /**
    * �����޸���·��ֵ
    * @return
    */
   private String editall(){
	   String linkids = getParaValue("linkids");
	   request.setAttribute("linkids", linkids);
	   return "/topology/network/link_edit_all.jsp";
   }
   /**
    * 
    * @return
    */
   private String importExcel(){
	   String path= getParaValue("files");
	   ExcelImportUtil util=new ExcelImportUtil();
	   util.loadAllData(path);
	   return "";
   }
// ��ȡ��·��Ϣ
   public String getShowMessage(String Id) {
       if (Id.indexOf(Constant.TYPE_LINK_SUBTYPE_HIN) >= 0) {
           return "ʾ����·";
       }
       com.afunms.polling.base.LinkRoad lr = (com.afunms.polling.base.LinkRoad) PollingEngine.getInstance().getLinkByID(Integer.parseInt(Id));
       if (lr == null) {
           SysLogger.info("����һ����ɾ������·��ID=" + Id);
           return "����·�ѱ�ɾ��";
       }
       
       return lr.getShowMessage();
   }
   public Objbean[] refreshLink(Objbean obj[]) {
       if (obj != null && obj.length > 0) {
           for (Objbean objbean : obj) {
               try {
				if (objbean.getLineid().indexOf(Constant.TYPE_LINK_SUBTYPE_HIN) >= 0) {
				       // ʾ����·�ݲ�����
				       continue;
				   }
//				System.out.println("objbean.getLineid()======"+objbean.getLineid());
				   com.afunms.polling.base.LinkRoad lr = (com.afunms.polling.base.LinkRoad) PollingEngine.getInstance().getLinkByID(Integer.parseInt(objbean.getLineid()));
//				   System.out.println("lr.isAlarm()=========="+lr.isAlarm());
				   if (lr == null) {
			           SysLogger.info("����һ����ɾ������·��ID=" + objbean.getLineid());
			           objbean.setColor("gray");
			       } else {
			    	   if(lr.isAlarm()){
			    		   if(lr.getLevels()>0){
			    			   objbean.setColor("#FFCC33");
			    		   }
			    		   if(lr.getLevels()>1){
			    			   objbean.setColor("red");
			    		   }
					   } else {
						   if (lr.getAssistant() == 1) {
					           objbean.setColor("blue");
					       } else {
					           objbean.setColor("green");
					       }
					   }
			       }
			} catch (Exception e) {
				e.printStackTrace();
			}
           }
       }
       
       return obj;
   }
   public Objbean[] refreshLinkTxt(Objbean obj[]) {
       if (obj != null && obj.length > 0) {
           for (Objbean objbean : obj) {
               try {
				if (objbean.getLineid().indexOf(Constant.TYPE_LINK_SUBTYPE_HIN) >= 0) {
				       // ʾ����·�ݲ�����
				       continue;
				   }
//				System.out.println("objbean.getLineid()======"+objbean.getLineid());
				   com.afunms.polling.base.LinkRoad lr = (com.afunms.polling.base.LinkRoad) PollingEngine.getInstance().getLinkByID(Integer.parseInt(objbean.getLineid()));
//				   System.out.println("lr.isAlarm()=========="+lr.isAlarm());
				   if (lr == null) {
			           SysLogger.info("����һ����ɾ������·��ID=" + objbean.getLineid());
			           objbean.setLinetext("0.0");
			       } else {
			    	   objbean.setLinetext(lr.getMessage(lr.getType()));
			       }
			} catch (Exception e) {
				e.printStackTrace();
			}
           }
       }
       
       return obj;
   }
   
   public Objbean[] refreshLinkWidth(String xmlName,Objbean obj[]) {
       if (obj != null && obj.length > 0) {
    	   ManageXml mvo = null;
	   		try{
	   			if(ShareData.getManagexmlhash() != null){
	   				if(((Hashtable)ShareData.getManagexmlhash()).containsKey(xmlName)){
	   					mvo = (ManageXml)(((Hashtable)ShareData.getManagexmlhash()).get(xmlName));
	   				}
	   				
	   			}
	   			//mvo = (ManageXml) manageXmlDao.findByXml(xmlName);
	   		}catch(Exception e){
	   			e.printStackTrace();
	   		}finally{
	   			//manageXmlDao.close();
	   		}
           for (Objbean objbean : obj) {
               try {
				if (objbean.getLineid().indexOf(Constant.TYPE_LINK_SUBTYPE_HIN) >= 0) {
				       // ʾ����·�ݲ�����
				       continue;
				   }
//				System.out.println("objbean.getLineid()======"+objbean.getLineid());
				   com.afunms.polling.base.LinkRoad lr = (com.afunms.polling.base.LinkRoad) PollingEngine.getInstance().getLinkByID(Integer.parseInt(objbean.getLineid()));
//				   System.out.println("lr.isAlarm()=========="+lr.isAlarm());
				   if (lr == null) {
			           SysLogger.info("����һ����ɾ������·��ID=" + objbean.getLineid());
			           objbean.setLineWidth("1");
			       } else {
			    	   objbean.setLineWidth(lr.getLinkWidth(Integer.parseInt(mvo.getUtilhdx()),Integer.parseInt(mvo.getUtilhdxperc())));
			       }
			} catch (Exception e) {
				e.printStackTrace();
			}
           }
       }
       
       return obj;
   }
   
   //��·�澯ȷ��
   public String confirmAlarm(String xmlName,String lineid){
		String returns = "error";
		int id = Integer.valueOf(lineid).intValue();
		com.afunms.polling.base.LinkRoad lr = (com.afunms.polling.base.LinkRoad) PollingEngine.getInstance().getLinkByID(id);
		try {
			if(lr!=null){
				lr.setAlarm(false);
				lr.setLevels(0);
				ManageXmlOperator mxmlOpr = new ManageXmlOperator();
				mxmlOpr.setFile(xmlName);
				mxmlOpr.init4editNodes();
				if(mxmlOpr.isLinkExist(lineid)){
					mxmlOpr.updateLine(lineid, "color", "green");
				}else if(mxmlOpr.isAssLinkExist(lineid)){
					mxmlOpr.updateAssLine(lineid, "color", "green");
				}
				mxmlOpr.writeXml();
			}
			returns = "line_net"+lr.getStartId()+"_net"+lr.getEndId();
		} catch (Exception e) {
			returns = "error";
			e.printStackTrace();
		}
		return returns;
	}

   
   public String execute(String action)
   {	   
	  if(action.equals("list"))
	     return list();     
	  if(action.equals("delete"))
	     return delete();  
	  if(action.equals("deleteLink"))
		 return deleteLink(); 
	  if(action.equals("ready_add"))
	     return readyAdd();
	  if(action.equals("ready_edit"))
          return readyEdit();
      if(action.equals("add"))
          return add();
      if(action.equals("edit"))
          return edit();
//      if(action.equals("saveAdd"))
//          return addLink();
      if(action.equals("saveEdit"))
          return editLink();
      if(action.equals("addLink"))
	      return readyAddLink();
      if(action.equals("editLink"))
          return readyEditLink();
//      if(action.equals("addDemoLink"))
//	      return addDemoLink();
      if(action.equals("readyAddLine"))
    	  return readyAddLine();
      if(action.equals("deleteDemoLink"))
	      return deleteDemoLink();
      if(action.equals("readyEditLine"))
    	  return readyEditLine();
      if(action.equals("editDemoLink"))
    	  return editDemoLink();
      if(action.equals("downloadlinklist"))
		     return downloadlinklist(); 
      if(action.equals("backuplinklist"))
    	  return backuplinklist(); 
      if(action.equals("editall")){
    	  return editall();
      }
      if(action.equals("importExcel")){
    	  return importExcel();
      }
      setErrorCode(ErrorMessage.ACTION_NO_FOUND);
      return null;
   }
}
