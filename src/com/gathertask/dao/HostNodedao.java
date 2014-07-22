package com.gathertask.dao;


import java.util.Hashtable;
import org.apache.log4j.Logger;

//import com.database.DBManager;
import com.afunms.common.util.DBManager;



/**
 * 
 * 
 * ������ѯ��Ԫ������Ϣ��
 * 
 * @author Administrator
 *
 */
public class HostNodedao {
	
	
	/**
	 * 
	 * ��ѯ��Ҫ�������Ԫ�ڵ��б�
	 * ��Ԫ��id��Ϊkey��topo_host_node����Ϊvalue
	 * @return
	 */
	
	Logger logger=Logger.getLogger(HostNodedao.class);
	
	public Hashtable queryHostmanagedList(){
		
		String sql="select * from topo_host_node where managed='1'";
		DBManager manager=null;
		Hashtable list=new Hashtable();
		try {
			manager=new DBManager();
			list=manager.executeQuerykeyoneListHashMap(sql, "id");
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		}finally{
			
			
			if(manager!=null)
				manager.close();
		}
		
		logger.info(list);
		return list;
	}
	
	
	/**
	 * 
	 * ��ѯ������������б�
	 * 
	 * @return
	 */
	public Hashtable queryHostnotmanagedList(){
		
		String sql="select * from topo_host_node where managed='0' ";
		DBManager manager=null;
		Hashtable list=new Hashtable();
		try {
			manager=new DBManager();
			list=manager.executeQuerykeyoneListHashMap(sql, "id");
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		}finally{
			
			
			if(manager!=null)
				manager.close();
		}
		
		logger.info(list);
		return list;
	}
	
	
	public static void main(String[] arg)
	{
		
		HostNodedao dao=new HostNodedao();
		Hashtable table=new Hashtable();
		table=dao.queryHostmanagedList();
		
	}

}
