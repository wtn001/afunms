package com.afunms.event.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.afunms.common.util.CreateAlarmMetersPic;
import com.afunms.common.util.DBManager;
import com.afunms.common.util.SysLogger;
import com.afunms.common.util.SystemConstant;
import com.afunms.event.dao.CheckEventDao;
import com.afunms.event.dao.EventListDao;
import com.afunms.event.model.CheckEvent;
import com.afunms.event.model.EventList;
import com.afunms.indicators.model.NodeDTO;
import com.afunms.indicators.util.NodeUtil;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.base.Node;
import com.afunms.polling.node.Host;
import com.afunms.topology.util.XmlOperator;

/********************************************************
 *Title:AlarmSummarize
 *Description:�澯��Ϣ����service��
 *Company  dhccs
 *@author zhangcw
 * Mar 25, 2011 11:44:59 AM
 ********************************************************
 */
public class AlarmSummarize {
	private DBManager conn;
	private ResultSet rs;
	private double closedRatio=0;//�ر���
	public HashMap<String, ?> getData(String filename){
		HashMap<String, Object> dataMap=new HashMap<String, Object>();
		conn=new DBManager();
		try{
			String[][] tabledata=gettableData();
			dataMap.put("tabledata", tabledata);
			String piedata=getpieData();
			dataMap.put("piedata", piedata);
			String columndata=getcolumnData();
			dataMap.put("columndata", columndata);
			String dayalarmData=getDayAlarmData();
			dataMap.put("dayalarmData", dayalarmData);
			String weekalarmData=getWeekAlarmData();
			dataMap.put("weekalarmData", weekalarmData);
			String closedAlarmPicFile=this.closedAlarmPic(filename);
			dataMap.put("closedAlarmPicFile", closedAlarmPicFile);
		}catch(Exception e)
		{
			e.printStackTrace();
		}finally{
			conn.close();
		}
		return dataMap;
	}
	/**
	 * ���ɱ����������
	 * @return
	 */
	public String[][] gettableData(){
		String [][] dataStr=new String[][] {{"���","��ʾ","��ͨ","����","����"},
											{"����澯","0","0","0","0"},
											{"�豸�澯","0","0","0","0"},
											{"�������澯","0","0","0","0"},
											{"���ݿ�澯","0","0","0","0"},
											{"�м���澯","0","0","0","0"},
											{"Ӧ�ø澯","0","0","0","0"},
											{"�洢�澯","0","0","0","0"},
											{"ҵ��澯","0","0","0","0"},
											{"��ȫ�澯","0","0","0","0"}};
		try
	     {
			String subtype="";
			int level=0;
			//rs = conn.executeQuery("select subtype,level1,count(1) as cnt from system_eventlist group by subtype,level1;");
			//--�޸�Ϊֻ��ѯ��������
			StringBuilder sb=new StringBuilder();
			sb.append(" select subtype,level1,count(1) as cnt from system_eventlist ");
			if(SystemConstant.DBType.equals("mysql")){
				sb.append(" where to_days(recordtime) = to_days(now()) ");
			}else if(SystemConstant.DBType.equals("oracle")){
				sb.append(" where to_char(recordtime,'yyyy-mm-dd') = to_char(sysdate,'yyyy-mm-dd')");
			}
			sb.append(" group by subtype,level1 ");
			System.out.println("gettableData"+"------------"+sb.toString());
			rs = conn.executeQuery(sb.toString()); 
	         while(rs.next()){
	        	 subtype=rs.getString("subtype"); 
				//�˿ڣ�"plot"
				//����:"net""dns" 
				//���ݿ� "db"
				//��������"host" 
				//�м�� "domino""tomcat""cics""mq""wasserver""weblogic""iis""jboss""apache" 
				//����"mail" "ftp" "web""socket"
				//ҵ��澯 "bus"   
				//"grapes" "radar"
		        // "network"
				//   	 	
	        	 if(subtype.equalsIgnoreCase("net")||subtype.equalsIgnoreCase("dns")){//����澯
	        		 level=rs.getInt("level1");
	        		 dataStr[1][level+1]= String.valueOf((Integer.parseInt(dataStr[1][level+1])+Integer.parseInt(rs.getString("cnt"))));
//	 	         }else if(subtype.equalsIgnoreCase("network")){ //�豸�澯 
//	        		 level=rs.getInt("level1"); 
//	        		 dataStr[2][level+1]= String.valueOf((Integer.parseInt(dataStr[2][level+1])+Integer.parseInt(rs.getString("cnt"))));
	        	 }else if(subtype.equalsIgnoreCase("host")){//�������澯 
	        		 level=rs.getInt("level1");
	        		 dataStr[3][level+1]= String.valueOf((Integer.parseInt(dataStr[3][level+1])+Integer.parseInt(rs.getString("cnt"))));
	 	         }else if(subtype.equalsIgnoreCase("db")){//���ݿ�澯 
	        		 level=rs.getInt("level1");
	        		 dataStr[4][level+1]=rs.getString("cnt");
	        	 }else if(subtype.equalsIgnoreCase("domino")
	        			 ||subtype.equalsIgnoreCase("tomcat")
	        			 ||subtype.equalsIgnoreCase("cics")
	        			 ||subtype.equalsIgnoreCase("mq")
	        			 ||subtype.equalsIgnoreCase("wasserver")
	        			 ||subtype.equalsIgnoreCase("weblogic")
	        			 ||subtype.equalsIgnoreCase("iis")
	        			 ||subtype.equalsIgnoreCase("jboss")
	        			 ||subtype.equalsIgnoreCase("apache")){ //�м���澯 
	        		 level=rs.getInt("level1");
	        		 dataStr[5][level+1]= String.valueOf((Integer.parseInt(dataStr[5][level+1])+Integer.parseInt(rs.getString("cnt"))));
	        	 } 
	        	 //�д��������
	        	 //6Ӧ�ø澯 
	        	 //7�洢�澯 
	        	 //8ҵ��澯 
	        	 else if(subtype.equalsIgnoreCase("bus")){//ҵ��澯  
        		 level=rs.getInt("level1");
        		 dataStr[8][level+1]=rs.getString("cnt");
	        	 }
	        	 //9��ȫ�澯 
	        	 
	         }
	     }
	     catch(Exception e)
	     {
	         SysLogger.error("AlarmSummarize:",e);
	     }
		return dataStr;
	}
	/**
	 * ���ɸ澯״̬��ͼ����
	 * @return
	 */
	public String getpieData(){
		StringBuffer dataStr=new StringBuffer();
		 Map<String, String> map=new TreeMap<String, String>();
		 //��ʼֵΪ0
		 map.put("0", "0");
		 map.put("1", "0");
		 map.put("2", "0");
	     try
	     {
	    	 //rs = conn.executeQuery("select managesign as sign,count(1) as cnt from system_eventlist group by (managesign)");
	    	//--�޸�Ϊֻ��ѯ��������
	    	 StringBuilder sb=new StringBuilder();
				sb.append(" select managesign as sign,count(1) as cnt from system_eventlist ");
				if(SystemConstant.DBType.equals("mysql")){
					sb.append(" where to_days(recordtime) = to_days(now()) ");
					sb.append(" group by (managesign) ; ");
				}else if(SystemConstant.DBType.equals("oracle")){
					sb.append(" where to_char(recordtime,'yyyy-mm-dd') = to_char(sysdate,'yyyy-mm-dd')");
					sb.append(" group by managesign ");			
				}
				rs = conn.executeQuery(sb.toString());
	         while(rs.next())
	        	 map.put(rs.getString("sign"), rs.getString("cnt"));
	     }
	     catch(Exception e)
	     {
	         SysLogger.error("AlarmSummarize:",e);
	     }
	    int unprocess=Integer.valueOf(map.get("0"));
	   //  int unprocess=Integer.valueOf("0");
	    int process=Integer.valueOf(map.get("1"));
	    int closed=Integer.valueOf(map.get("2"));
	    if((unprocess+process+closed) != 0){
	    	this.closedRatio=closed/(unprocess+process+closed);
	    }
	    if(unprocess==0&&process==0&&closed==0){
	    	dataStr.append("0");
	    }else{
	    dataStr.append("δ����;").append(unprocess).append(";false;FFCC00\\n");
	    dataStr.append("������;").append(process).append(";false;6666FF\\n");
		dataStr.append("�ر�;").append(closed).append(";false;CC33FF\\n");
	    }
		return dataStr.toString();
	}
	/**
	 * ���ɵ��յ�Сʱ�澯��
	 * @return
	 */
	public String getDayAlarmData(){
		Map<Integer, Integer> map=new TreeMap<Integer, Integer>();
		for(int i=0;i<24;i++){
			map.put(i, 0);
		}
		try
	     {
			String sql = "";
			if(SystemConstant.DBType.equals("mysql")){
				sql="select HOUR(recordtime)  as h,count(1) as cnt from system_eventlist where  DATE(recordtime)=CURDATE() group by h;";
			}else if(SystemConstant.DBType.equals("oracle")){
				sql="select to_char(s.recordtime,'HH24') as h,count(*) as cnt from system_eventlist s where  to_char(s.recordtime,'YYYY-MM-DD')= to_char(sysdate) group by to_char(s.recordtime,'HH24')";
			}
	    	 rs = conn.executeQuery(sql);
	         while(rs.next())
	        	 map.put(rs.getInt("h"), rs.getInt("cnt"));
	     }
	     catch(Exception e)
	     {
	         SysLogger.error("AlarmSummarize:",e);
	     }
		StringBuffer dataStr=new StringBuffer();
		for(int i=0;i<24;i++){
			dataStr.append(i).append(";").append(map.get(i)).append("\\n");
		}
		return dataStr.toString();
	}
	/**
	 * ���ɽ�һ�ܸ澯���� ��ͼ
	 * @return
	 */
	public String getWeekAlarmData(){
		Map<Integer, Integer> map=new TreeMap<Integer, Integer>();
		Calendar cal = Calendar.getInstance();
		for(int i=0;i<7;i++){
			 int day = cal.get(Calendar.DATE);
			 map.put(day, 0);
			 cal.add(Calendar.DATE, -1);
		}
		try
	     {
			String sql ="";
			if(SystemConstant.DBType.equals("mysql")){
				sql="select DAY(recordtime)  as d,count(1) as cnt from system_eventlist where  DATEDIFF(CURDATE(),recordtime)<7 group by d;";
			}else if(SystemConstant.DBType.equals("oracle")){
				sql="select to_char(s.recordtime,'dd') as d ,count(*) as cnt from system_eventlist s  where to_char(s.recordtime,'d')<'7' group by s.recordtime";
			}
	    	 rs = conn.executeQuery(sql);
	         while(rs.next())
	        	 map.put(rs.getInt("d"), rs.getInt("cnt"));
	     }
	     catch(Exception e)
	     {
	         SysLogger.error("AlarmSummarize:",e);
	     }
		StringBuffer dataStr=new StringBuffer();
		for(Integer date : map.keySet()) {     
			dataStr.append(date).append(";").append(map.get(date)).append("\\n");
		  }  
		return dataStr.toString();
	}
	/**
	 * ��һ��Ƶ�ʽϸߵı������� ��ͼ
	 * @return
	 */
	public String getcolumnData(){
		String[] titleStr=new String[] {"����澯","�豸�澯","����������","���ݿ⾯��","�м������","Ӧ�ø澯","����澯","�洢�澯","ҵ��澯","��ȫ�澯"};
		String[] colorStr=new String[] {"#33CCFF","#003366","#33FF33","#FF0033","#9900FF","#FFFF00","#333399","#0000FF","#A52A2A","#23f266"};
		String[] dataStr=new String[]  {"0","0","0","0","0","0","0","0","0","0"};
		try
	     {
			String subtype="";
			String sql ="";
			if(SystemConstant.DBType.equals("mysql")){
				sql="select subtype,count(1) as cnt from system_eventlist where DATEDIFF(CURDATE(),recordtime)<7 group by subtype;";
			}else if(SystemConstant.DBType.equals("oracle")){
				sql="select subtype ,count(*) as cnt from system_eventlist s  where to_char(s.recordtime,'d')<'7' group by subtype";
			}
			rs = conn.executeQuery(sql);
	         while(rs.next()){  
	        	 subtype=rs.getString("subtype"); 
	        	 
	        	 if(subtype.equalsIgnoreCase("net")||subtype.equalsIgnoreCase("dns")){//1����澯
	        		 dataStr[0]= String.valueOf((Integer.parseInt(dataStr[0])+Integer.parseInt(rs.getString("cnt")))); 
//	        	 }else if(subtype.equalsIgnoreCase("network")){ //2�豸�澯 
//	        		 dataStr[1]=rs.getString("cnt"); 
	        	 }else if(subtype.equalsIgnoreCase("host")){//3�������澯 
	        		 dataStr[2]=rs.getString("cnt"); 
	        	 } else if(subtype.equalsIgnoreCase("db")){//4���ݿ�澯 
	        		 dataStr[3]=rs.getString("cnt"); 
	        	 } else if(subtype.equalsIgnoreCase("domino")
	        			 ||subtype.equalsIgnoreCase("tomcat")
	        			 ||subtype.equalsIgnoreCase("cics")
	        			 ||subtype.equalsIgnoreCase("mq")
	        			 ||subtype.equalsIgnoreCase("wasserver")
	        			 ||subtype.equalsIgnoreCase("weblogic")
	        			 ||subtype.equalsIgnoreCase("iis")
	        			 ||subtype.equalsIgnoreCase("jboss")
	        			 ||subtype.equalsIgnoreCase("apache")){ //5�м���澯  
	        		 dataStr[4]= String.valueOf((Integer.parseInt(dataStr[4])+Integer.parseInt(rs.getString("cnt"))));
	        	 } else if(subtype.equalsIgnoreCase("bus")){//8ҵ��澯  
	        		 dataStr[7]=rs.getString("cnt"); 
	        	 }
	         }
	     }
	     catch(Exception e)
	     {
	         SysLogger.error("AlarmSummarize:",e);
	     }
		StringBuffer xmlStr=new StringBuffer();
		xmlStr.append("<?xml version='1.0' encoding='gb2312'?>");
		xmlStr.append("<chart><series>");
		for(int i=0;i<10;i++){
			xmlStr.append("<value xid='").append(i).append("'>").append(titleStr[i]).append("</value>");
			
		}
		xmlStr.append("</series><graphs>");
		xmlStr.append("<graph gid='0'>");
		for(int i=0;i<10;i++){
			xmlStr.append("<value xid='").append(i).append("' color='").append(colorStr[i]).append("'>").append(dataStr[i]).append("</value>");
		}
		xmlStr.append("</graph>");
		xmlStr.append("</graphs></chart>");
		return xmlStr.toString();
	}
	/**
	 * ���ɸ澯�ر��ʱ�������
	 * @param filename ���ɱ���ͼƬ���ļ���
	 * @return
	 */
	public String closedAlarmPic(String filename){
		CreateAlarmMetersPic createAlarmMetersPic=new CreateAlarmMetersPic();
		String file=createAlarmMetersPic.createClosedAlarmPic(filename, this.closedRatio);
		return file;
		
	}
	public List<EventList> getLastestEventList1(String bid,String xml) {
//		System.out.println("------------------------"+bid);
		List nodelist=new ArrayList();
		try {
			XmlOperator xmlOpr = new XmlOperator();
			xmlOpr.setFile(xml);
			xmlOpr.init4updateXml();
			nodelist = xmlOpr.getAllNodes();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		List sqllist = new ArrayList();
		List<EventList> list = null;
		// ��������CheckEvent �澯�ȼ���3�ĸ澯
		StringBuffer s = new StringBuffer();
		int _flag = 0;
		if (bid != null){
			if(bid !="-1"){
				String[] bids = bid.split(",");
				if(bids.length>0){
					for(int i=0;i<bids.length;i++){
						if(bids[i].trim().length()>0){
							if(_flag==0){
								s.append(" and ( businessid like '%,"+bids[i].trim()+",%' ");
								_flag = 1;
							}else{
								s.append(" or businessid like '%,"+bids[i].trim()+",%' ");
							}
						}
					}
					s.append(") ") ;
				}
			}	
		}
		String sql = " ";
		for(int j = 0; j < nodelist.size(); j++){
			String nodeid = ((String) nodelist.get(j)).split(":")[0];
			String category = ((String) nodelist.get(j)).split(":")[1];
			com.afunms.polling.base.Node nodes = null;
			if (nodeid.indexOf("hin") < 0){
				StringBuffer ss = new StringBuffer();
				if(nodeid.indexOf("dbs") != -1){
					Node mnode = (Node) PollingEngine.getInstance().getNodeByCategory("dbs", Integer.valueOf(nodeid.substring(3)));
					NodeUtil nodeUtil = new NodeUtil();
					NodeDTO nodeDTO = nodeUtil.conversionToNodeDTO(mnode);
					if(nodeDTO!=null){
						ss.append(" and nodeid="+ mnode.getId() +" and subtype='"+nodeDTO.getType()+"' ");
					}
				} else if(nodeid.indexOf("net") != -1){
					nodes = (Host) PollingEngine.getInstance().getNodeByCategory(category, Integer.valueOf(nodeid.substring(3)));
					NodeUtil nodeUtil = new NodeUtil();
					NodeDTO nodeDTO = nodeUtil.conversionToNodeDTO(nodes);
					if(nodeDTO!=null){
						ss.append(" and nodeid="+ nodes.getId() +" and subtype='"+nodeDTO.getType()+"' ");
					}
				} else {
					Node mnode = (Node) PollingEngine.getInstance().getNodeByCategory(category, Integer.valueOf(nodeid.substring(3)));
					NodeUtil nodeUtil = new NodeUtil();
					NodeDTO nodeDTO = nodeUtil.conversionToNodeDTO(mnode);
					if(nodeDTO!=null){
						ss.append(" and nodeid="+ mnode.getId() +" and subtype='"+nodeDTO.getType()+"' ");
					}
				}
				sql = "select  *,count(distinct content) from system_eventlist where managesign=0  and date(now())= date(recordtime) "+s+ss+" group by content order by id desc";
				sqllist.add(sql);
			}
		}
//		System.out.println("********************************"+sql);
		if(sqllist!=null&&sqllist.size()>0){
			EventListDao dao = new EventListDao();
			try {
				list = dao.findByCriteria(sqllist);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				dao.close();
			}
		}
//		EventListDao dao = new EventListDao();
//		try {
//			list = dao.findByCriteria(sql);
//		} catch (Exception e) {
//		} finally {
//			dao.close();
//		}
//		for (int i = 0; i < list.size(); i++) {
////			System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<"+list.get(i).getAlarmlevel());
//			boolean bln = compareBids(list.get(i).getBid(), bid);
//			if (!bln) {
//				list.remove(i);
//			}
//		}
		if(list==null){
			List<EventList> listevent=new ArrayList<EventList>();
			return listevent;
		}
		return list;
	}

	/**
	 * ���Է���
	 * @param args
	 */
	public static void main(String args[])
	{

	}
}
