package com.afunms.event.manage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.afunms.common.util.SysLogger;
import com.afunms.common.util.SysUtil;
import com.afunms.common.util.SyslogDefs;
import com.afunms.common.util.SyslogFinals;
import com.afunms.event.dao.NetSyslogDao;
import com.afunms.event.dao.NetSyslogEventDao;
import com.afunms.event.dao.NetSyslogImpEventDao;
import com.afunms.event.dao.NetSyslogViewerDao;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.base.Node;
import com.afunms.topology.dao.HostNodeDao;
import com.afunms.topology.model.HostNode;

@SuppressWarnings("unchecked")
public class QuestionManager {
	HttpServletRequest request;
	HttpServletResponse response;
	public QuestionManager(HttpServletRequest request, HttpServletResponse response){
		this.request = request;
		this.response = response;
		
	}
	private String getParaDate(String date){
		String rtnDate = null;
		if (null != date) {
			rtnDate = request.getParameter(date);
		}
		if (rtnDate == null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			rtnDate = sdf.format(new Date());
		}
		return rtnDate;
	}

	public Map sortByValue(Map map, final boolean reverse) {
	    List list = new LinkedList (map.entrySet());
	    Collections .sort(list, new Comparator () {
	
	        public int compare(Object o1, Object o2) {
	            if (reverse) {
	                return -((Comparable ) ((Map .Entry) (o1)).getValue())
	                        .compareTo(((Map .Entry) (o2)).getValue());
	            }
	            return ((Comparable ) ((Map .Entry) (o1)).getValue())
	                    .compareTo(((Map .Entry) (o2)).getValue());
	        }
	    });
	
	    Map result = new LinkedHashMap ();
	    for (Iterator it = list.iterator(); it.hasNext();) {
	        Map .Entry entry = (Map .Entry) it.next();
	        result.put(entry.getKey(), entry.getValue());
	    }
	    return result;
	}   	
	
	public String doAction(){
		String questionID = request.getParameter("questionid");
		//System.out.println(questionID);
		String b_time = getParaDate("startdate");
		String t_time = getParaDate("todate");
		String starttime = b_time + " 00:00:00";
		String totime = t_time + " 23:59:59";
		request.setAttribute("startdate", b_time);
		request.setAttribute("todate", t_time);
		request.setAttribute("questionid", questionID);
		if (null != questionID) {
			if (questionID.startsWith("0A")) {
				return loginQuestion(questionID, starttime, totime);
			}else if(questionID.startsWith("0B")) {
				return userManagerQuestion(questionID, starttime, totime);
			}else if(questionID.startsWith("0C")) {
				return eventQuestion(questionID, starttime, totime);
			}else if(questionID.startsWith("0D")) {
				return warningByTimeQuestion(questionID);
			}else if(questionID.startsWith("0E")) {
				return warningQuestion(questionID, starttime, totime);
			}else if(questionID.startsWith("0F")) {
				return systemQuestion(questionID, starttime, totime);
			}
		}
		return null;
	}
	
	private String systemQuestion(String questionID,
			String starttime, String totime) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		if ("0F01".equalsIgnoreCase(questionID)) {//0F01����������վ�϶����û��޸���ϵͳʱ�䣿
			request.setAttribute("title", "�����û��޸���ϵͳʱ��");
			//TODO 
			/*List allNodeList = PollingEngine.getInstance().getNodeList();
			for (int i = 0; i < allNodeList.size(); i++) {
				String ipaddress = ((Node)allNodeList.get(i)).getIpAddress();
				NetSyslogImpEventDao dao = new NetSyslogImpEventDao();
				int count = dao.getCountByEventid("log" + SysUtil.doip(ipaddress), starttime, totime, SyslogDefs.LOG_LOGIN_FAILURE);
				if (count > 0) {
					map.put(ipaddress, count);
				}
			}
			map = sortByValue(map, true);
			request.setAttribute("map", map);*/
			
		}else if ("0F02".equalsIgnoreCase(questionID)) {//0F02������������ж��ٸ�Σ���¼���
			request.setAttribute("title", "��¼ʧ�ܵ���������");
			//TODO
		}else if ("0F03".equalsIgnoreCase(questionID)) {//0F03������������ж��ٴζ�����ļ��б����ʣ�
			request.setAttribute("title", "��¼ʧ�ܵ���������");
			//TODO
		}
		return "/alarm/syslog/answerlist.jsp";
	}
	
	
	private String warningQuestion(String questionID, String starttime, String totime) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		if ("0E01".equalsIgnoreCase(questionID)) {//0E01��Щ�������ɵĸ澯��ࣿ
			request.setAttribute("title", "�����澯����");
			map = getEventCountByPriority(starttime, totime, "warning");
			map = sortByValue(map, true);
			request.setAttribute("map", map);
		}	
		return "/alarm/syslog/answerlist.jsp";
	}
	
	
	private String warningByTimeQuestion(String questionID) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		request.setAttribute("warningByTimeQuestion", "warningByTimeQuestion");
		if ("0D01".equalsIgnoreCase(questionID)) {//���һ�ܵĸ澯�¼�����Щ��
			request.setAttribute("title", "���һ�ܵĸ澯�¼�");
			String totime = (String)request.getAttribute("todate");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date toDate = null;
			try {
				toDate = sdf.parse(totime);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			Calendar c = Calendar.getInstance();
			c.setTime(toDate);
			c.add(Calendar.DATE, -7);
			Date startDate = c.getTime();
			String starttime = sdf.format(startDate) + " 00:00:00";
			request.setAttribute("startdate", sdf.format(startDate));
			map = getEventCountByPriority(starttime, totime + " 23:59:59", "warning");
			map = sortByValue(map, true);
			request.setAttribute("map", map);
		}else if ("0D02".equalsIgnoreCase(questionID)) {//���һСʱ���ֵĴ����¼� 
			request.setAttribute("title", "���һСʱ�Ĵ����¼�");
			long curTime = System.currentTimeMillis();
			long time = curTime - 60*60*1000;
			Date d = new Date(time);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String totime = sdf.format(new Date(curTime));
			String starttime = sdf.format(d);
			request.setAttribute("startdate", starttime);
			request.setAttribute("todate", totime);
			request.setAttribute("dateType", "hour");
			map = getEventCountByPriority(starttime, totime, "error");
			map = sortByValue(map, true);
			request.setAttribute("map", map);
		}
		return "/alarm/syslog/answerlist.jsp";
	}
	
	private Map getEventCountByPriority(String starttime, String totime, String priority){
		Map<String, Integer> map = new HashMap<String, Integer>();
		List allNodeList = PollingEngine.getInstance().getNodeList();
		request.setAttribute("priority", priority);
		for (int i = 0; i < allNodeList.size(); i++) {
			String ipaddress = ((Node)allNodeList.get(i)).getIpAddress();
			int count = 0;
			if (isNetworkDev(ipaddress)) {
				NetSyslogImpEventDao dao = new NetSyslogImpEventDao();
				count = dao.getCountByPriority(ipaddress, starttime, totime, priority);
				dao.close();
			}else{
				NetSyslogEventDao dao = new NetSyslogEventDao();
				count = dao.getCountByPriority(ipaddress, starttime, totime, priority);
				dao.close();
			}
			if (count > 0) {
				map.put(ipaddress, count);
			}
		}	
		return map;
	}
	
	private String eventQuestion(String questionID,	String starttime, String totime){
		Map<String, Integer> map = new HashMap<String, Integer>();
		if ("0C01".equalsIgnoreCase(questionID)) {//0C01��Щ���������˴������¼���
			request.setAttribute("title", "�����豸�����˴����¼�");
			List list = new ArrayList();
			String strclass = request.getParameter("strclass");
			String ipaddress = request.getParameter("ipaddress");
			NetSyslogViewerDao dao = new NetSyslogViewerDao();
			list = dao.loadNetSyslogViewers(Integer.MAX_VALUE, 1, starttime, totime, strclass, ipaddress);
			request.setAttribute("ipaddress", ipaddress);
			request.setAttribute("page", dao.getPage());
			request.setAttribute("viewersList", list);
			return "/alarm/syslog/statistic.jsp";
		}else if ("0C02".equalsIgnoreCase(questionID)) {//0C02��Щ���������˾����¼���
			request.setAttribute("title", "�����¼�");
			map = getEventCountByPriority(starttime, totime, "warning");
		}else if ("0C03".equalsIgnoreCase(questionID)) {//0C03��Щ���������˴�����Σ���¼���  
			request.setAttribute("title", "�豸Σ���¼�");
			map = getEventCountByPriority(starttime, totime, "error");
		}
		map = sortByValue(map, true);
		request.setAttribute("map", map);
		return "/alarm/syslog/answerlist.jsp";
	}
		
	
	private Map getUserCount(String starttime, String totime, String msgRegexp){
		/*Map<String, Map> map = new HashMap<String, Map>();
		List allNodeList = PollingEngine.getInstance().getNodeList();
		for (int i = 0; i < allNodeList.size(); i++) {
			String ipaddress = ((Node)allNodeList.get(i)).getIpAddress();
			if(!isNetworkDev(ipaddress)){
				NetSyslogImpEventDao dao = new NetSyslogImpEventDao();
				Map mapTemp = dao.getCountByUserAccout("log" + SysUtil.doip(ipaddress), starttime, totime, msgRegexp);
				request.setAttribute("eventid", msgRegexp);
				if (mapTemp.size() > 0) {
					map.put(ipaddress, mapTemp);
				}
			}
		}
		return map;*/
		Map<String, Map> map = new HashMap<String, Map>();
		List allNodeList = PollingEngine.getInstance().getNodeList();
		NetSyslogDao dao = new NetSyslogDao();
		for (int i = 0; i < allNodeList.size(); i++) {
			String ipaddress = ((Node)allNodeList.get(i)).getIpAddress();
			Map mapTemp = dao.getCountByUserAccout(ipaddress, starttime, totime, msgRegexp);
			if (mapTemp.size() > 0) {
				map.put(ipaddress, mapTemp);
			}
		}
		dao.close();
		return map;
	}
	
	private String userManagerQuestion(String questionID, String starttime, String totime) {
		Map<String, Map> map = new HashMap<String, Map>();
		if ("0B01".equalsIgnoreCase(questionID)) {//��Щ�û��ɹ��޸������ǵ����룿
			request.setAttribute("title", "�����޸ĳɹ����û�");
			map = getUserCount(starttime, totime, SyslogFinals.getMsgClause(SyslogFinals.QSUSERCHANGEPASSSUCCESS));//map = getUserCount(starttime, totime, "628");
			request.setAttribute("streventname", SyslogFinals.QSUSERCHANGEPASSSUCCESS);
		}else if ("0B02".equalsIgnoreCase(questionID)) {//0B02��Щ�û��޸�����ʧ�ܣ� 
			request.setAttribute("title", "�����޸�ʧ�ܵ��û�");
			map = getUserCount(starttime, totime, SyslogFinals.getMsgClause(SyslogFinals.QSUSERCHANGEPASSFAILURE));
			request.setAttribute("streventname", SyslogFinals.QSUSERCHANGEPASSFAILURE);
		}else if ("0B03".equalsIgnoreCase(questionID)) {//0B03��Щ�˺ű�ɾ��/���ã�
			request.setAttribute("title", "�ʺű�ɾ���ͽ��õ���ϸ��Ϣ");
			map = getUserCount(starttime, totime, SyslogFinals.getMsgClause(SyslogFinals.QSUSERACCOUNTDISABLED));//map = getUserCount(starttime, totime, "629");
			request.setAttribute("streventname", SyslogFinals.QSUSERACCOUNTDISABLED);
		}else if ("0B04".equalsIgnoreCase(questionID)) {//0B04��Щ�û��޸Ļ�������˰�ȫ�����־�� 
			//TODO û���û�����
			request.setAttribute("title", "�޸Ļ�����������־���û��б�");
			map = getUserCount(starttime, totime, SyslogFinals.getMsgClause(SyslogFinals.QSUSERMODIFYLOG));//map = getUserCount(starttime, totime, SyslogDefs.LOG_LOG_CLEARED);
			request.setAttribute("streventname", SyslogFinals.QSUSERMODIFYLOG);
			map = sortByValue(map, true);
		}
		request.setAttribute("map", map);
		return "/alarm/syslog/usermanageanswerlist.jsp";
	}
	
	
	private Map getLoginCount(String starttime, String totime, String eventMsg){
		Map<String, Integer> map = new HashMap<String, Integer>();
		List allNodeList = PollingEngine.getInstance().getNodeList();
		for (int i = 0; i < allNodeList.size(); i++) {
			String ipaddress = ((Node)allNodeList.get(i)).getIpAddress();
			int count = 0;
			if (isNetworkDev(ipaddress)) {
				NetSyslogDao dao = new NetSyslogDao();
				count = dao.getCountByEvent(ipaddress, starttime, totime, eventMsg);
			}else{
				NetSyslogEventDao dao = new NetSyslogEventDao();
				count = dao.getCountByEvent(ipaddress, starttime, totime, eventMsg);
			}
			
			if (count > 0) {
				map.put(ipaddress, count);
			}
		}
		return map;
	}
	
	private String loginQuestion(String questionID, String starttime, String totime){
		Map<String, Integer> map = new HashMap<String, Integer>();
		if ("0A01".equalsIgnoreCase(questionID)) {//��Щ�����ĵ�¼ʧ�ܴ�����ࣿ
			request.setAttribute("title", "��¼ʧ�ܵ���������");
//			map = getLoginCount(starttime, totime, SyslogDefs.LOG_LOGIN_FAILURE, null);
			map = getLoginCount(starttime, totime, SyslogFinals.getMsgClause(SyslogFinals.QSLOGINFAILURE));
			request.setAttribute("streventname", SyslogFinals.QSLOGINFAILURE);
			map = sortByValue(map, true);
			request.setAttribute("map", map);
		}else if ("0A02".equalsIgnoreCase(questionID)) {//�����з����˶��ٵ�¼ʧ�ܵ��¼���
			request.setAttribute("title", "�����е�¼ʧ���¼�����");
//			map = getLoginCount(starttime, totime, SyslogDefs.LOG_LOGIN_FAILURE, "3");
			map = getLoginCount(starttime, totime, SyslogFinals.getMsgClause(SyslogFinals.QSNWLOGINFAILURE));
			request.setAttribute("streventname", SyslogFinals.QSNWLOGINFAILURE);
			map = sortByValue(map, true);
			request.setAttribute("map", map);
		}else if ("0A03".equalsIgnoreCase(questionID)) {//��Щ�û��˺ű������¼��Ƚ϶ࣿ
			request.setAttribute("title", "�û��˺ű������¼�");
			//TODO //��Щ�û��˺ű������¼��Ƚ϶ࣿ
		}
		return "/alarm/syslog/answerlist.jsp";
	}

	public static boolean isNetworkDev(String ipaddress){
		boolean isNetworkDev = false;
		HostNodeDao dao = new HostNodeDao();
		List nodeList = dao.loadNetwork(-1);
		if (null != nodeList && nodeList.size() > 0) {
			for (int i = 0; i < nodeList.size(); i++) {
				HostNode node = (HostNode)nodeList.get(i);
				if (node.getIpAddress().equalsIgnoreCase(ipaddress)) {
					isNetworkDev = true;
					break;
				}
			}
		}
		return isNetworkDev;
	}   	
}
/**
 * 		String[] c1 = {"0A01��Щ�����ĵ�¼ʧ�ܴ�����ࣿ","0A02��Щ�û��˺ű������¼��Ƚ϶ࣿ","0A03�����з����˶��ٵ�¼ʧ�ܵ��¼���"};
		String[] c2 = {"0B01��Щ�û��ɹ��޸������ǵ����룿","0B02��Щ�û��޸�����ʧ�ܣ�","0B03��Щ�˺ű�ɾ��/���ã�","0B04��Щ�û��޸Ļ�������˰�ȫ�����־��"};
		String[] c3 = {"0C01��Щ���������˴������¼���","0C02��Щ���������˾����¼���","0C03��Щ���������˴�����Σ���¼���","0C04��Щ���������˶��ٸ�Σ���¼���"};
		String[] c4 = {"0D01���һ�ܵĸ澯�¼�����Щ��", "0D02���һСʱ�����˶��ٴ����¼���"};
		String[] c5 = {"0E01��Щ�������ɵĸ澯��ࣿ"};
		String[] c6 = {"0F01����������վ�϶����û��޸���ϵͳʱ�䣿","0F02������������ж��ٸ�Σ���¼���","0F03������������ж��ٴζ�����ļ��б����ʣ�"};
 */
