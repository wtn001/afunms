package com.afunms.ip.stationtype.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.afunms.common.base.BaseDao;
import com.afunms.common.base.BaseVo;
import com.afunms.common.base.DaoInterface;
import com.afunms.common.util.SysLogger;
import com.afunms.ip.stationtype.model.bussinesstype;
import com.afunms.ip.stationtype.model.stationtype;
import com.afunms.topology.model.NetSyslogNodeAlarmKey;

public class bussinesstypeDao extends BaseDao implements DaoInterface {

	public bussinesstypeDao(){
		super("ip_bussinesstype");
	}
	@Override
	public BaseVo loadFromRS(ResultSet rs)  {
		bussinesstype vo = new bussinesstype();
		 try
	      {
			  vo.setId(rs.getInt("id"));
	          vo.setName(rs.getString("name"));
	          vo.setDescr(rs.getString("descr"));
	          vo.setBak(rs.getString("bak"));
	          
	      }
	      catch(Exception e)
	      {
	          //SysLogger.error("PortconfigDao.loadFromRS()",e);
	    	  e.printStackTrace();
	          vo = null;
	      }
	      return vo;
	}

	public boolean saveCZ(BaseVo baseVo) {
		bussinesstype vo=(bussinesstype)baseVo;
		StringBuffer sql = new StringBuffer(100);
		sql.append("insert into ip_bussinesstype (name,descr,bak) values(");
		sql.append("'");
		sql.append(vo.getName());
		sql.append("',");
		sql.append("'");
		sql.append(vo.getDescr());
		sql.append("',");
		sql.append("'");
		sql.append(vo.getBak());
		sql.append("')");
//		System.out.println("==============="+sql.toString());
		return saveOrUpdate(sql.toString());
	}

//	public boolean update(BaseVo vo) {
//		boolean result = false;
//		  stationtype vo = (stationtype)baseVo;
//			StringBuffer sql = new StringBuffer();
//			sql.append("update nms_netsyslogalarmkey_node set keywords='");
//			sql.append(vo.getKeywords());	 
//			sql.append("' where id=");
//			sql.append(vo.getId());
//	     
//	     try
//	     {
//	    	 //SysLogger.info(sql.toString());
//	         conn.executeUpdate(sql.toString());
//	         result = true;
//	     }
//	     catch(Exception e)
//	     {
//	    	 result = false;
//	         SysLogger.error("EventListDao:update()",e);
//	     }
//	     finally
//	     {
//	    	 conn.close();
//	     }     
//	     return result;
//	}
	
//	public List loadID(int id){
//		stationtype vo = new stationtype();
//		List list = new ArrayList();
//		try{
//			String sql="select * from ip_stationtype where id =  "+id;
//			rs = conn.executeQuery(sql);
//	        while(rs.next())
//	        	list.add(loadFromRS(rs));
//		}
//		catch(Exception e){
//			e.printStackTrace();
//		}
//		finally
//  		{
//			try {
//	  			if (rs!=null)
//					rs.close();
//				
//	  			if (conn!=null)
//	  				conn.close();
//				
//	  			} catch (SQLException e) {
//					e.printStackTrace();
//				}
//  		}
//		return list;
//	}
	
	public List loadCZ(){
		List list = new ArrayList();
		try{
			String sql="select * from ip_dy ";
			rs = conn.executeQuery(sql);
	        while(rs.next())
	        	list.add(loadFromRS(rs));
	        
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally
  		{
			try {
	  			if (rs!=null)
					rs.close();
				
	  			if (conn!=null)
	  				conn.close();
				
	  			} catch (SQLException e) {
					e.printStackTrace();
				}
  		}
		// TODO Auto-generated method stub
		return list;
	}
	public boolean save(BaseVo vo) {
		// TODO Auto-generated method stub
		return false;
	}
	public boolean update(BaseVo baseVo) {
		boolean result = false;
		bussinesstype vo = (bussinesstype)baseVo;
			StringBuffer sql = new StringBuffer();
			sql.append("update ip_bussinesstype set name='");
			sql.append(vo.getName());
			sql.append("',descr='");
			sql.append(vo.getDescr());
			sql.append("',bak='");
			sql.append(vo.getBak());
			sql.append("' where id=");
			sql.append(vo.getId());
//	        System.out.println(sql.toString()+"---------------------");
	     try
	     {
	    	 //SysLogger.info(sql.toString());
	         conn.executeUpdate(sql.toString());
	         result = true;
	     }
	     catch(Exception e)
	     {
	    	 result = false;
	         SysLogger.error("EventListDao:update()",e);
	     }
	     finally
	     {
	    	 conn.close();
	     }     
	     return result;
	}

}
