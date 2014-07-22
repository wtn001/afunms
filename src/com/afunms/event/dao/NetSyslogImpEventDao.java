package com.afunms.event.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import com.afunms.common.base.BaseDao;
import com.afunms.common.base.BaseVo;
import com.afunms.common.base.DaoInterface;
import com.afunms.common.util.SyslogDefs;
import com.afunms.common.util.SyslogFinals;
import com.afunms.common.util.SystemConstant;
import com.afunms.event.manage.NetSyslogManager;
import com.afunms.event.model.NetSyslogEvent;
import com.afunms.event.model.NetSyslogImpEvent;

public class NetSyslogImpEventDao extends BaseDao implements DaoInterface {
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public NetSyslogImpEventDao() {
		super("nms_netsyslog");
	}
	
/*	public int getCountByEventid(String tablename, String startTime, String toTime, String eventids){
		String where = " where recordtime >= '" + startTime + "' and recordtime <= '" + toTime + "'"; 
		if (tablename.startsWith("log")) {
			where = " where eventid in (" + eventids + ") and recordtime >= '" + startTime + "' and recordtime <= '" + toTime + "'"; 
			
		}
		String sql = "select count(*) from " + tablename + where;
		rs = conn.executeQuery(sql);
		System.out.println("getCountByEventid sql = " + sql);
		try {
			while(rs.next()){
				return rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
*/
	/**
	 * ���ӵ�¼���ͷ���
	 * @param tablename
	 * @param startTime
	 * @param toTime
	 * @param eventids
	 * @param loginType
	 * @return
	 */
	public int getCountByEventid(String tablename, String startTime, String toTime, String eventids, String loginType){
		String where = "";
		 
		if("mysql".equalsIgnoreCase(SystemConstant.DBType)){
			where = " where eventid in (" + eventids + ") and recordtime >= '" + startTime + "' and recordtime <= '" + toTime + "'";
		}else if("oracle".equalsIgnoreCase(SystemConstant.DBType)){
			where = " where eventid in (" + eventids + ") and recordtime >= to_date('"+startTime+"','YYYY-MM-DD HH24:MI:SS') and recordtime <= to_date('"+toTime+"','YYYY-MM-DD HH24:MI:SS')";
		}
		if (null != loginType && !"".equals(loginType)) {
			where += " and message like '%��¼����: " + loginType + " %'"; 
		}
		String sql = "select count(*) from " + tablename + where;
		//System.out.println("getCountByLoginType sql = " + sql);
		rs = conn.executeQuery(sql);
		//System.out.println("getCountByPriority sql = " + sql);
		try {
			while(rs.next()){
				return rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}	
	
	public int getCountByPriority(String ipaddress, String startTime, String toTime, String priority){
		String where = "";
		 
		if("mysql".equalsIgnoreCase(SystemConstant.DBType)){
			where = " where ipaddress = '" + ipaddress.trim() + "' and priorityname like '%" + priority.trim() + "%' and recordtime >= '" + startTime + "' and recordtime <= '" + toTime + "'";
		}else if("oracle".equalsIgnoreCase(SystemConstant.DBType)){
			where = " where ipaddress = '" + ipaddress.trim() + "' and priorityname like '%" + priority.trim() + "%' and recordtime >= to_date('"+startTime+"','YYYY-MM-DD HH24:MI:SS') and recordtime <= to_date('"+toTime+"','YYYY-MM-DD HH24:MI:SS')";
		}
		String sql = "select count(*) from nms_netsyslog " + where;
//		String sql = "select count(*) from " + SysUtil.doip("log" + ipaddress) + where;
		rs = conn.executeQuery(sql);
		//System.out.println("getCountByPriority sql = " + sql);
		try {
			while(rs.next()){
				return rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public Map getCountByUserAccout(String tablename, String startTime, String toTime, String eventids){
		String where = "";
		 
		if("mysql".equalsIgnoreCase(SystemConstant.DBType)){
			where = " where eventid in (" + eventids + ") and recordtime >= '" + startTime + "' and recordtime <= '" + toTime + "'";
		}else if("oracle".equalsIgnoreCase(SystemConstant.DBType)){
			where = " where eventid in (" + eventids + ") and recordtime >= to_date('"+startTime+"','YYYY-MM-DD HH24:MI:SS') and recordtime <= to_date('"+toTime+"','YYYY-MM-DD HH24:MI:SS')";
		}
		String sql = "select * from " + tablename + where;
		rs = conn.executeQuery(sql);
		//System.out.println("getCountByUserAccout sql = " + sql);
		NetSyslogEventDao dao = new NetSyslogEventDao();
		NetSyslogEvent event = new NetSyslogEvent();
		Map<String, Integer> map = new HashMap<String, Integer>();
		try {
			while(rs.next()){
				event = (NetSyslogEvent)dao.loadFromRS(rs);
				String message = event.getMessage();
				int begin = message.indexOf("Ŀ���ʻ���:") + "Ŀ���ʻ���:".length();
				int end = message.indexOf("Ŀ����:");
				String username = message.substring(begin, end);
				if(null == map.get(username.trim())){
					map.put(username.trim(), 1);
				}else{
					map.put(username.trim(), map.get(username.trim()) + 1);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return map;
	}
	
	public NetSyslogImpEvent getNetSyslogImpEvent(String ipaddress, String startTime, String toTime){
		NetSyslogImpEvent event = new NetSyslogImpEvent();
		if (NetSyslogManager.isNetworkDev(ipaddress)) {
			int loginSuccess = getCountByEventNetwork(ipaddress,startTime, toTime, SyslogDefs.NWLOG_LOGIN_SUCCESS);//��¼�ɹ����û�
			int logoutSuccess = getCountByEventNetwork(ipaddress,startTime, toTime, SyslogDefs.NWLOG_LOGOUT_SUCCESS);//�ǳ��ɹ����û�
			int loginFailure = getCountByEventNetwork(ipaddress,startTime, toTime, SyslogDefs.NWLOG_LOGIN_FAILURE);//��¼ʧ�ܵ��û�
			event.setLoginSuccess(loginSuccess);
			event.setLogoutSuccess(logoutSuccess);
			event.setLoginFailure(loginFailure);

		}else{
			int loginSuccess = getCountByEvent(startTime, toTime, SyslogFinals.getMsgClause("userLoginSuccess"));//��¼�ɹ����û�
			int loginFailure = getCountByEvent(startTime, toTime, SyslogFinals.getMsgClause("userLoginFailure"));//��¼ʧ�ܵ��û�
			int logoutSuccess = getCountByEvent(startTime, toTime, SyslogFinals.getMsgClause("userLogoutSuccess"));//�ǳ��ɹ����û�
			int clearLog = getCountByEvent(startTime, toTime, SyslogFinals.getMsgClause("userLoginSuccess"));//��������־
			int strategyModified = getCountByEvent(startTime, toTime, SyslogFinals.getMsgClause("userLoginSuccess"));//��Ʋ��Ա��
			int accoutModified = getCountByEvent(startTime, toTime, SyslogFinals.getMsgClause("userLoginSuccess"));//�û��ʺű��
			int accoutLocked = getCountByEvent(startTime, toTime, SyslogFinals.getMsgClause("userLoginSuccess"));//�������û��ʺ�
			int sceCli = getCountByEvent(startTime, toTime, SyslogFinals.getMsgClause("userLoginSuccess"));//SceCli�����
			event.setLoginSuccess(loginSuccess);
			event.setLogoutSuccess(logoutSuccess);
			event.setLoginFailure(loginFailure);
			event.setClearLog(clearLog);
			event.setStrategyModified(strategyModified);
			event.setAccoutModified(accoutModified);
			event.setAccoutLocked(accoutLocked);
			event.setSceCli(sceCli);
		}
		return event;
	}
	
	private int getCountByEvent(String startTime, String toTime, String msgClause) {
		String where = "";
		
		if("mysql".equalsIgnoreCase(SystemConstant.DBType)){
			where = " where recordtime >= '" + startTime + "' and recordtime <= '" + toTime + "' and " + msgClause; 
		}else if("oracle".equalsIgnoreCase(SystemConstant.DBType)){
			where = " where recordtime >= to_date('"+startTime+"','YYYY-MM-DD HH24:MI:SS')  and recordtime <= to_date('"+toTime+"','YYYY-MM-DD HH24:MI:SS')  and " + msgClause; 
		}
		String sql = "select count(*) from nms_netsyslog " + where;
		rs = conn.executeQuery(sql);
		//System.out.println("getCountByEvent sql = " + sql);
		try {
			while(rs.next()){
				return rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	private int getCountByEventNetwork(String ipaddress, String startTime, String toTime, String regexp) {
		String where = "";
		 
		if("mysql".equalsIgnoreCase(SystemConstant.DBType)){
			where = " where ipaddress = '" + ipaddress + "' and message like '%" + regexp + "%' and recordtime >= '" + startTime + "' and recordtime <= '" + toTime + "'";
		}else if("oracle".equalsIgnoreCase(SystemConstant.DBType)){
			where = " where ipaddress = '" + ipaddress + "' and message like '%" + regexp + "%' and recordtime >= to_date('"+startTime+"','YYYY-MM-DD HH24:MI:SS')  and recordtime <= to_date('"+toTime+"','YYYY-MM-DD HH24:MI:SS') ";
		}
		String sql = "select count(*) from nms_netsyslog " + where;
		rs = conn.executeQuery(sql);
		//System.out.println("getCountByEventNetwork sql = " + sql);
		try {
			while(rs.next()){
				return rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public BaseVo loadFromRS(ResultSet rs) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean save(BaseVo vo) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean update(BaseVo vo) {
		// TODO Auto-generated method stub
		return false;
	}

/*	public static String getTableName(String ipaddress){
		String table = "nms_netsyslog";
		boolean isNetworkDev = isNetworkDev(ipaddress);
		if (!isNetworkDev) {
			table = "log" + SysUtil.doip(ipaddress);
		}
		return table;
	}
	 
	public static boolean isNetworkDev(String ipaddress){
		boolean isNetworkDev = false;
		HostNodeDao dao = new HostNodeDao();
		List nodeList = dao.loadNetwork(1);
		if (null != nodeList && nodeList.size() > 0) {
			for (int i = 0; i < nodeList.size(); i++) {
				HostNode node = (HostNode)nodeList.get(i);
				if (node.getIpAddress().equals(ipaddress)) {
					isNetworkDev = true;
					break;
				}
			}
		}
		return isNetworkDev;
	}*/	
}
