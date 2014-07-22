package com.afunms.application.manage;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import com.afunms.application.util.ReportExport;
import com.afunms.capreport.dao.SubscribeResourcesDao;
import com.afunms.capreport.dao.UtilReportDao;
import com.afunms.capreport.manage.NetCapReportManager;
import com.afunms.capreport.model.SubscribeResources;
import com.afunms.capreport.model.UtilReport;
import com.afunms.common.base.BaseManager;
import com.afunms.common.base.ErrorMessage;
import com.afunms.common.base.ManagerInterface;
import com.afunms.common.util.Arith;
import com.afunms.common.util.SessionConstant;
import com.afunms.config.dao.PortconfigDao;
import com.afunms.initialize.ResourceCenter;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.api.I_HostLastCollectData;
import com.afunms.polling.base.LinkRoad;
import com.afunms.polling.impl.HostLastCollectDataManager;
import com.afunms.polling.loader.HostLoader;
import com.afunms.polling.task.CheckLinkTask;
import com.afunms.system.model.User;
import com.afunms.topology.dao.HostNodeDao;
import com.afunms.topology.dao.LinkDao;
import com.afunms.topology.manage.LinkManager;
import com.afunms.topology.manage.LinkPerformanceManager;
import com.afunms.topology.model.Link;
import com.afunms.topology.model.LinkPerformanceDTO;

/**
 * @author HONGLI E-mail: ty564457881@163.com
 * @version ����ʱ�䣺Aug 8, 2011 9:33:43 AM
 * ��˵��:�˵���ת������
 */
public class TabMenuManager  extends BaseManager implements ManagerInterface{

	public String execute(String action) {
		if("xnzy".equals(action)){//������Դҳ��
			return xnzy();
		}
		if("llxn".equals(action)){//��·����
			return llxn();
		}
		if("toppx".equals(action)){//top����
			return toppx();
		}
		if("topnpx".equals(action)){//top����
			return topnpx();
		}
		if("xnmb".equals(action)){//�������
//			return xnmb();
		}
		if("linkList".equals(action)){
			return linkList();
		}
		if("portcomparelist".equals(action)){
			return portcomparelist();
		}
		if("portcompareExport".equals(action)){
			return portcompareExport();
		}
		if(action.equals("loaddkdbtempletdetail")){
			return loadDkdbTeplateDetail();
		}
		setErrorCode(ErrorMessage.ACTION_NO_FOUND);
		return null;
	}
	
	/**
	 * �������
	 * @return
	 */
//	private String xnmb(){
//		//�����豸�澯��Ϣ��ά����
//		//ȡ�����е������豸����
//		HostNodeDao hostNodeDao = new HostNodeDao();
//		User current_user = (User)session.getAttribute(SessionConstant.CURRENT_USER);
//		List<HostNode> hostNodeList = null;
//		try{
//			hostNodeList = hostNodeDao.loadByPerAll(current_user.getBusinessids());
//		}catch(Exception e){
//			e.printStackTrace();
//		}finally{
//			hostNodeDao.close();
//		}
//		//�豸�澯��Ϣ����
////		String[][] nodeAlarmsArry = new String[][]{
////				{"1","10.10.1.1","cpu","memory","interface","AllInBandwidthUtilHdx","AllOutBandwidthUtilHdx"},	
////				{"2","10.10.1.2","cpu","memory","interface","AllInBandwidthUtilHdx","AllOutBandwidthUtilHdx"},	
////				{"3","10.10.1.3","cpu","memory","interface","AllInBandwidthUtilHdx","AllOutBandwidthUtilHdx"},	
////		};
//		//�豸�澯��Ϣ����
//		String[][] nodeAlarmsArry = null;
//		if(hostNodeList != null && hostNodeList.size() > 0){
//			Hashtable<String, NodeAlarmModel> nodeAlarmModelHash = PollingEngine.getNodeAlarmModelHash();
//			nodeAlarmsArry = new String[hostNodeList.size()][8];
//			for(int i=0; i<hostNodeList.size(); i++){//�����豸�б� ���澯��Ϣ���ϣ���װ�豸��ά�澯�б�
//				HostNode hostNode = hostNodeList.get(i);
//				nodeAlarmsArry[i][0] = hostNode.getId()+"";
//				nodeAlarmsArry[i][1] = hostNode.getIpAddress();
//				if(nodeAlarmModelHash.containsKey(hostNode.getId()+"")){
//					NodeAlarmModel nodeAlarmModel = nodeAlarmModelHash.get(hostNode.getId()+"");
//					System.out.println("hostNode.getIpAddress=="+hostNode.getIpAddress());
//					nodeAlarmsArry[i][2] = nodeAlarmModel.getPingAlarmLevel();
//					nodeAlarmsArry[i][3] = nodeAlarmModel.getCpuAlarmLevel();
//					nodeAlarmsArry[i][4] = nodeAlarmModel.getMemoryAlarmLevel();
//					nodeAlarmsArry[i][5] = nodeAlarmModel.getInterfaceAlarmLevel();
//					nodeAlarmsArry[i][6] = nodeAlarmModel.getAllInBandwidthUtilHdxAlarmLevel();
//					nodeAlarmsArry[i][7] = nodeAlarmModel.getAllOutBandwidthUtilHdxAlarmLevel();
//				}else{
//					nodeAlarmsArry[i][2] = "0";
//					nodeAlarmsArry[i][3] = "0";
//					nodeAlarmsArry[i][4] = "0";
//					nodeAlarmsArry[i][5] = "0";
//					nodeAlarmsArry[i][6] = "0";
//					nodeAlarmsArry[i][7] = "0";
//				}
//			}
//		}
//		request.setAttribute("nodeAlarmsArry", nodeAlarmsArry);
//		return "/performance/sbgjylb.jsp";
//	}
	
	/**
	 * �˿ڶԱ� �������� zhangys
	 */
	private String portcompareExport() {
		System.out.println("zhangys:" + request.getQueryString());
		String ids=getParaValue("ids");
		String type=getParaValue("type");
		String exportType=getParaValue("exportType");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if (ids==null||ids.equals("")||ids.equals("null")) {
    		String id = request.getParameter("id");
    		
    		if(id.equals("null"))return null;
    		UtilReport report=new UtilReport();
    		UtilReportDao dao=new UtilReportDao();
    		report=(UtilReport) dao.findByBid(id);
    		ids=report.getIds();
		}
		String filename="";
		if (type.equals("net")) {
			if (exportType.equals("xls")) {
				filename="temp" + File.separator + "network_report.xls";
			}else if (exportType.equals("doc")) {
				filename="temp" + File.separator + "network_report.doc";
			}else if (exportType.equals("pdf")) {
				filename="temp" + File.separator + "network_report.pdf";
			}
		}
		String filePath=ResourceCenter.getInstance().getSysPath()+filename;
		String startTime=getParaValue("startdate");
		String toTime=getParaValue("todate");
		if (startTime == null) {
			startTime = sdf.format(new Date()) + " 00:00:00";
		} else {
			startTime = startTime + " 00:00:00";
		}
		if (toTime == null) {
			toTime = sdf.format(new Date()) + " 23:59:59";
		} else {
			toTime = toTime + " 23:59:59";
		}
		ReportExport export=new ReportExport();
		export.portcompareExport(ids, type, filePath, startTime, toTime, exportType);
		request.setAttribute("filename", filePath);
		return "/capreport/net/download.jsp";
	}

	/**
	 * top����
	 * @return
	 */
	private String toppx(){
		return "/performance/toppx.jsp";
	}
	/**
	 * 
	 * @description TODO
	 * @author wangxiangyong
	 * @date Mar 5, 2013 11:47:13 AM
	 * @return String  
	 * @return
	 */
	private String topnpx(){
		return "/performance/topnpx.jsp";
	}
	/**
	 * ��·����
	 * @return
	 */
	private String llxn(){
		String linkids = request.getParameter("linkids");
		//ȡ�����е���·����
		List linkList = null;
		try {
			LinkDao linkDao = new LinkDao();
			linkList = linkDao.loadAll();
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
		request.setAttribute("linkids", linkids);
		request.setAttribute("linkList", linkList);
		return "/performance/llxn.jsp";
	}
	
	private String linkList(){
		String linkids = request.getParameter("linkids");
		//ȡ�����е���·����
		List linkList = null;
		try {
			LinkDao linkDao = new LinkDao();
			linkList = linkDao.loadAll();
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
		
		List linkPerformanceList = new ArrayList();
		DecimalFormat df=new DecimalFormat("#.##");
		String runmodel = PollingEngine.getCollectwebflag(); 
		if("0".equals(runmodel)){
	       	//�ɼ�������Ǽ���ģʽ	
			for(int i = 0 ; i < linkList.size() ; i++){
				Link link = (Link)linkList.get(i);
				if(link.getLinktype()!=-1){
					LinkPerformanceDTO linkPerformanceDTO = getLinkPerformanceDTO(link);
					linkPerformanceList.add(linkPerformanceDTO);
				}
			}
		}else{
		 	//�ɼ�������Ƿ���ģʽ
			//������·��ID�õ���·����ʼ�˿ڡ����ٵ���Ϣ
			//ȡ�˿�����
			I_HostLastCollectData hostlastmanager = new HostLastCollectDataManager();
			Vector end_vector = new Vector();
			Vector start_vector = new Vector();
			String[] netInterfaceItem = { "index", "ifDescr", "ifSpeed",
					"ifOperStatus", "ifOutBroadcastPkts", "ifInBroadcastPkts",
					"ifOutMulticastPkts", "ifInMulticastPkts",
					"OutBandwidthUtilHdx", "InBandwidthUtilHdx",
					"InBandwidthUtilHdxPerc", "OutBandwidthUtilHdxPerc" };
			Hashtable interfaceHash = CheckLinkTask.getLinknodeInterfaceData(linkList);//�Ȳɼ����У�������forѭ���ж�βɼ�
			for(int k = 0 ; k < linkList.size() ; k++){
				Link link = (Link)linkList.get(k);
				if(link.getLinktype()!=-1){
				int startId = link.getStartId();
				int endId = link.getEndId();
				String startIndex = link.getStartIndex();
				String endIndex = link.getEndIndex();
				String start_inutilhdx = "0";
				String start_oututilhdx = "0";
				String start_inutilhdxperc = "0";
				String start_oututilhdxperc = "0";
				String end_inutilhdx = "0";
				String end_oututilhdx = "0";
				String end_inutilhdxperc = "0";
				String end_oututilhdxperc = "0";
				String starOper = "";
				String endOper = "";
				String pingValue = "0";
				String allSpeedRate = "0";
				com.afunms.polling.base.Node startnode = (com.afunms.polling.base.Node) PollingEngine
					.getInstance().getNodeByID(startId);
				com.afunms.polling.base.Node endnode = (com.afunms.polling.base.Node) PollingEngine
						.getInstance().getNodeByID(endId);
				if(startnode == null || endnode == null){
					continue;
				}
				try {
//					start_vector = hostlastmanager.getInterface(startnode.getIpAddress(),netInterfaceItem,"index","",""); 
//					end_vector = hostlastmanager.getInterface(endnode.getIpAddress(),netInterfaceItem,"index","","");
					if(interfaceHash != null && interfaceHash.containsKey(startnode.getIpAddress())){
						start_vector = (Vector)interfaceHash.get(startnode.getIpAddress());
					}
					if(interfaceHash != null && interfaceHash.containsKey(endnode.getIpAddress())){
						end_vector = (Vector)interfaceHash.get(endnode.getIpAddress());
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				} 
				if (startnode != null) {
					try {
						for (int i = 0; i < start_vector.size(); i++) {
							String[] strs = (String[]) start_vector.get(i);
							String index = strs[0];
							if (index.equalsIgnoreCase(startIndex)) {
								starOper = strs[3].trim();
								start_oututilhdx = strs[8].replaceAll("KB/��", "").replaceAll("kb/s", "").replaceAll("kb/��", "").replaceAll("KB/S", "");
								start_inutilhdx = strs[9].replaceAll("KB/��", "").replaceAll("kb/s", "").replaceAll("kb/��", "").replaceAll("KB/S", "");
								start_oututilhdxperc = strs[10].replaceAll("%", "");
								start_inutilhdxperc = strs[11].replaceAll("%", "");
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
				if (endnode != null) {
					try {
						for (int i = 0; i < end_vector.size(); i++) {
							String[] strs = (String[]) end_vector.get(i);
							String index = strs[0];
							if (index.equalsIgnoreCase(endIndex)) {
								endOper = strs[3].trim();;
								end_oututilhdx = strs[8].replaceAll("KB/��", "").replaceAll("kb/s", "").replaceAll("kb/��", "").replaceAll("KB/S", "");
								end_inutilhdx = strs[9].replaceAll("KB/��", "").replaceAll("kb/s", "").replaceAll("kb/��", "").replaceAll("KB/S", "");
								end_oututilhdxperc = strs[10].replaceAll("%", "");
								end_inutilhdxperc = strs[11].replaceAll("%", "");
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				int downspeed = (Integer.parseInt(start_oututilhdx) + Integer
						.parseInt(end_inutilhdx.replaceAll("KB/��", "").replaceAll("kb/s", "").replaceAll("kb/��", "").replaceAll("KB/S", ""))) / 2;
				int upspeed = (Integer.parseInt(start_inutilhdx) + Integer
						.parseInt(end_oututilhdx.replaceAll("KB/��", "").replaceAll("kb/s", "").replaceAll("kb/��", "").replaceAll("KB/S", ""))) / 2;
	
				double upperc = 0;
				try {
					if (start_oututilhdxperc != null
							&& start_oututilhdxperc.trim().length() > 0
							&& end_inutilhdxperc != null
							&& end_inutilhdxperc.trim().length() > 0)
						upperc = Arith.div((Double
								.parseDouble(start_oututilhdxperc) + Double
								.parseDouble(end_inutilhdxperc)), 2);
				} catch (Exception e) {
					e.printStackTrace();
				}
				double downperc = 0;
				try {
					if (start_inutilhdxperc != null
							&& start_inutilhdxperc.trim().length() > 0
							&& end_oututilhdxperc != null
							&& end_oututilhdxperc.trim().length() > 0)
						downperc = Arith.div((Double
								.parseDouble(start_inutilhdxperc) + Double
								.parseDouble(end_oututilhdxperc)), 2);
				} catch (Exception e) {
					e.printStackTrace();
				}
				int linkflag = 100;
				if ("".equals(starOper.trim()) || "".equals(endOper.trim()) || "down".equalsIgnoreCase(starOper)||"down".equalsIgnoreCase(endOper)) {
					linkflag = 0;
				}
				
				pingValue = String.valueOf(linkflag);
				LinkPerformanceDTO linkPerformanceDTO = new LinkPerformanceDTO();
				String name = link.getLinkName();
				int id = link.getId();
				allSpeedRate = String.valueOf(df.format(downperc + upperc));
				
				//��װ��·�˿����ٵ���Ϣ 
				linkPerformanceDTO.setId(id);
				linkPerformanceDTO.setName(name);
				linkPerformanceDTO.setStartNode(startnode.getIpAddress());
				linkPerformanceDTO.setEndNode(endnode.getIpAddress());
				linkPerformanceDTO.setStratIndex(startIndex);
				linkPerformanceDTO.setEndIndex(endIndex);
				linkPerformanceDTO.setUplinkSpeed(upspeed+"");
				linkPerformanceDTO.setDownlinkSpeed(downspeed+"");
				linkPerformanceDTO.setPingValue(pingValue);
				linkPerformanceDTO.setAllSpeedRate(allSpeedRate);
				linkPerformanceList.add(linkPerformanceDTO);
			    }
			}
		}
		
		String field = getParaValue("field");
		
		String sorttype = getParaValue("sorttype");
		if(field != null){
			if(sorttype == null || sorttype.trim().length() == 0){
				sorttype = "asc";
			} else if ("asc".equals(sorttype)){
				sorttype = "desc";
			} else if ("desc".equals(sorttype)){
				sorttype = "asc";
			}
			
			linkPerformanceList = new LinkPerformanceManager().linkPerformanceListSort(linkPerformanceList, field, sorttype);
			
			request.setAttribute("field", field);
			request.setAttribute("sorttype", sorttype);
		} 
		request.setAttribute("linkids", linkids);
		request.setAttribute("linkList", linkList);
		request.setAttribute("linkPerformanceList", linkPerformanceList);
		return "/performance/llxn.jsp";
	}
	
	public LinkPerformanceDTO getLinkPerformanceDTO(Link link){
		
		LinkPerformanceDTO linkPerformanceDTO = new LinkPerformanceDTO();
		
		try {
			String name = link.getLinkName();
			int id = link.getId();
			//������·����Ϣ���ڴ� wxy update
			LinkManager l=new LinkManager();
			l.getShowMessage(id+"");
			
			LinkRoad linkRoad = null;
			linkRoad = PollingEngine.getInstance().getLinkByID(id);
			String stratIndex = linkRoad.getStartDescr();
			String endIndex = linkRoad.getEndDescr();
			String startNode = linkRoad.getStartIp();
			String endNode = linkRoad.getEndIp();

			String uplinkSpeed = linkRoad.getUplinkSpeed();
			String downlinkSpeed = linkRoad.getDownlinkSpeed();
			String pingValue = linkRoad.getPing();
			String allSpeedRate = linkRoad.getAllSpeedRate();
			if(uplinkSpeed==null||uplinkSpeed.equalsIgnoreCase("null"))uplinkSpeed="0";
			if(downlinkSpeed==null||downlinkSpeed.equalsIgnoreCase("null"))downlinkSpeed="0";
			if(pingValue==null||pingValue.equalsIgnoreCase("null"))pingValue="0";
			if(allSpeedRate==null||allSpeedRate.equalsIgnoreCase("null")){
				allSpeedRate="0.00";
			}else {
				DecimalFormat df=new DecimalFormat("#.##");
				allSpeedRate=String.valueOf(df.format(Double.parseDouble(allSpeedRate)));
			}
//			String maxSpeed = linkRoad.getMaxSpeed();
			
//			String uplinkSpeedColor = "green";
//			
//			if(uplinkSpeed != null && uplinkSpeed.trim().length() > 0
//					&& maxSpeed != null && !"".equals(maxSpeed)  
//					&& !"null".equals(maxSpeed)){
//				if(Double.valueOf(uplinkSpeed) > Double.valueOf(maxSpeed) ){
//					uplinkSpeedColor = "red";
//				}
//			}
//			
//			String downlinkSpeedColor = "green";
//			
//			if(downlinkSpeedColor != null && downlinkSpeedColor.trim().length() > 0
//					&& maxSpeed != null && !"".equals(maxSpeed)  
//					&& !"null".equals(maxSpeed)){
//				if(Double.valueOf(downlinkSpeedColor) > Double.valueOf(maxSpeed) ){
//					downlinkSpeedColor = "red";
//				}
//			}
			
			linkPerformanceDTO.setId(id);
			linkPerformanceDTO.setName(name);
			linkPerformanceDTO.setStartNode(startNode);
			linkPerformanceDTO.setEndNode(endNode);
			linkPerformanceDTO.setStratIndex(stratIndex);
			linkPerformanceDTO.setEndIndex(endIndex);
			linkPerformanceDTO.setUplinkSpeed(uplinkSpeed);
			linkPerformanceDTO.setDownlinkSpeed(downlinkSpeed);
			linkPerformanceDTO.setPingValue(pingValue);
			linkPerformanceDTO.setAllSpeedRate(allSpeedRate);
//			linkPerformanceDTO.setUplinkSpeedColor(uplinkSpeedColor);
//			linkPerformanceDTO.setDownlinkSpeedColor(downlinkSpeedColor);
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block 
			e.printStackTrace();
		}
		
		return linkPerformanceDTO;
	}
	
	/**
	 * ������Դ
	 * @return
	 */
	private String xnzy(){
		return "/performance/tabMenuContent.jsp";
	}
	/**
	 * �˿���� zhangys
	 * @return
	 */
	private String portcomparelist() {
		StringBuffer s = new StringBuffer();

		User current_user = (User) session.getAttribute(SessionConstant.CURRENT_USER);
		HostNodeDao dao = new HostNodeDao();
		if (isSuperAdmin(current_user)) {
			request.setAttribute("list", dao.loadNetwork(1));
		}else{
			request.setAttribute("list", dao.loadNetworkByBid2(1, current_user.getBusinessids()));	
		}
		return "/performance/dkdb.jsp";
	}
	
	private boolean isSuperAdmin(User user){
		if (user.getRole() == 0) {
			return true;
		}
		return false;
	}
	
	/**
	 * ���ض˿ڶԱ�ģ����ϸ��Ϣ by zhangys
	 */
	private String loadDkdbTeplateDetail() {	
		String id = this.getParaValue("id");

		UtilReportDao dao = new UtilReportDao();
		UtilReport report = dao.findByBid("nms_dkdbareport", id);
		dao = new UtilReportDao();
		List<String> list = dao.findIdsByBid("nms_dkdbports", id);
		
		SubscribeResources vo = new SubscribeResources();
		SubscribeResourcesDao subDao = new SubscribeResourcesDao();
		vo = subDao.findById("sys_dkdb_ssresources", id);
		
		String[] frequencyName = { "ÿ��", "ÿ��", "ÿ��", "ÿ��", "ÿ��" };
		String[] monthCh = { " 1��", " 2��", " 3��", " 4��", " 5��", " 6��", " 7��", " 8��",
				" 9��", " 10��", " 11��", " 12��" };
		String[] weekCh = { " ������", " ����һ", " ���ڶ�", " ������", " ������", " ������", " ������" };
		String[] dayCh = null;
		String[] hourCh =null;

		StringBuffer sb = new StringBuffer();
		int frequency = vo.getReport_sendfrequency();
		NetCapReportManager netManager = new NetCapReportManager();
		String month = netManager.splitDate(vo.getReport_time_month(), monthCh, "month");
		String week = netManager.splitDate(vo.getReport_time_week(), weekCh, "week");
		String day = netManager.splitDate(vo.getReport_time_day(), dayCh, "day");
		String hour = netManager.splitDate(vo.getReport_time_hou(), hourCh, "hour");
		sb.append(frequencyName[frequency - 1] + " ");
		if (month != null && !month.equals(""))
			sb.append(" �·ݣ�(" + month + ")");
		if (week != null && !week.equals(""))
			sb.append(" ����:(" + week + ")");
		if (day != null && !day.equals(""))
			sb.append(" ���ڣ�(" + day + ")");
		if (hour != null && !hour.equals(""))
			sb.append(" ʱ�䣺(" + hour + ")");
		String sendDate = sb.toString();
		Vector vector = new Vector();
		vector.add(0, report);
		vector.add(1, vo);
		request.setAttribute("vector", vector);
		request.setAttribute("list", list);
		request.setAttribute("sendDate", sendDate);
		return "/performance/dkdbtemplatedetail.jsp";
	}
}
