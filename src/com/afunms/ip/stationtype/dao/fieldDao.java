package com.afunms.ip.stationtype.dao;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.afunms.common.base.BaseDao;
import com.afunms.common.base.BaseVo;
import com.afunms.common.base.DaoInterface;
import com.afunms.common.util.SysLogger;
import com.afunms.ip.stationtype.model.alltype;
import com.afunms.ip.stationtype.model.field;
import com.afunms.ip.stationtype.model.ip_bussinesstype;

public class fieldDao extends BaseDao implements DaoInterface {

	public fieldDao(){
		super("ip_field");
	}
	@Override
	public BaseVo loadFromRS(ResultSet rs) {
		field vo = new field();
		 try
	      {
			  vo.setId(rs.getInt("id"));
	          vo.setRunning(rs.getString("running"));
	          vo.setName(rs.getString("name"));
	          vo.setBackbone_id(rs.getInt("backbone_id"));
	          vo.setFlag(rs.getInt("flag"));
	      }
	      catch(Exception e)
	      {
	          //SysLogger.error("PortconfigDao.loadFromRS()",e);
	    	  e.printStackTrace();
	          vo = null;
	      }
	      return vo;
	}

	public boolean delete(String[] id)
	   {
		   boolean result = false;
		   try
		   {
		       for(int i=0;i<id.length;i++)
		           conn.addBatch("delete from ip_field where backbone_id=" + id[i]);
		       conn.executeBatch();
		       result = true;
		   }
		   catch(Exception ex)
		   {
		       SysLogger.error("BaseDao.delete()",ex);
		       result = false;
		   }
		   return result;
	   }


	
	 public BaseVo loadOne(String tablename,String backbone_name)
	   {
		 BaseVo vo = null;
		   try 
		   {
//			   System.out.println("BaseDao.java--251ÐÐ------>"+"select * from " + table + " order by id");
			   rs = conn.executeQuery("select * from " + tablename + " where backbone_name = '"+backbone_name+"'");
			   if(rs == null)return null;
			   while(rs.next()){
				  vo=loadFromRS(rs);				
			   }
		   }   
		   catch(Exception e) 
		   {
			   e.printStackTrace();
			   SysLogger.error("BaseDao.loadAll()",e);
		   }
		   finally
		   {
			   if(rs != null){
				   try{
					   rs.close();
				   }catch(Exception e){
				   }
			   }
			   conn.close();
		   }
		   return vo;	
	   }
	
	
//	public BaseVo QueryOne(String tablename,String name,int id)
//	   {
//		   BaseVo vo = null;
//	       try
//		   {
//	    	   System.out.println("select * from " + tablename + " where id= (select min(id) from "+tablename+" where type = 0 and "+name+"_id = "+id+")");
//			   rs = conn.executeQuery("select * from " + tablename + " where id= (select min(id) from "+tablename+" where type = 0 and "+name+"_id = "+id+")"); 
//			   if(rs.next())
//			       vo = loadFromRS(rs);
//		   }    
//		   catch(Exception ex)
//		   {
//			   //ex.printStackTrace();
//			   SysLogger.error("BaseDao.findByID()",ex);
//		   }finally{
//			   if(rs != null){
//				   try{
//					   rs.close();
//				   }catch(Exception e){
//				   }
//			   }
//		   }
//	       return vo;
//	   }
	
//	public List QueryTwo(String tablename,String name,int id)
//	   {
//		   List list = new ArrayList();
//	       try
//		   {
//			   rs = conn.executeQuery("select * from " + tablename + " where id= (select min(id) from "+tablename+" where type = 0 and "+name+"_id = "+id+")"); 
//			   if(rs.next())
//			       list.add(loadFromRS(rs));
//		   }    
//		   catch(Exception ex)
//		   {
//			   //ex.printStackTrace();
//			   SysLogger.error("BaseDao.findByID()",ex);
//		   }finally{
//			   if(rs != null){
//				   try{
//					   rs.close();
//				   }catch(Exception e){
//				   }
//			   }
//		   }
//	       return list;
//	   }
	 public synchronized int getNextID(String otherTable) {
		 int id = 0;
		   try
		   {
		       rs = conn.executeQuery("select max(id) from " + otherTable);
		       if (rs.next())
		          id = rs.getInt(1) + 1;
		   }
		   catch(Exception ex)
		   {
		       SysLogger.error("BaseDao.getNextID()",ex);
		       id = 0;
		   }finally{
			   if(rs != null){
				   try{
					   rs.close();
				   }catch(Exception e){
				   }
			   }
		   }
		   return id;
	}
	
	 
	 public boolean save(BaseVo baseVo) {
			boolean result = false;
			field vo = (field)baseVo;
				StringBuffer sql = new StringBuffer();
				sql.append("insert into ip_field (name,running,backbone_id,flag)  values('");
				sql.append(vo.getName());
				sql.append("','");
				sql.append(vo.getRunning());
				sql.append("',");
				sql.append(vo.getBackbone_id());
				sql.append(",");
				sql.append(vo.getFlag());
				sql.append(")");
		     try
		     {
//		    	 SysLogger.info(sql.toString());
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
	 
	 
	 
	 public void update(String field_id,String name,String running) {
				StringBuffer sql = new StringBuffer();
				sql.append("update ip_field set name='");
				sql.append(name);
				sql.append("',running='");
				sql.append(running);
				sql.append("'");
				sql.append(" where id=");
				sql.append(field_id);
		     try
		     {
//		    	 SysLogger.info(sql.toString());
		         conn.executeUpdate(sql.toString());
		     }
		     catch(Exception e)
		     {
		         SysLogger.error("EventListDao:update()",e);
		     }
		     finally
		     {
		    	 conn.close();
		     }     
		}
	public boolean update(BaseVo vo) {
		// TODO Auto-generated method stub
		return false;
	}
	
	
	 public void delete(String sql) {
			
	     try
	     {
//	    	 SysLogger.info(sql.toString());
	         conn.executeUpdate(sql);
	     }
	     catch(Exception e)
	     {
	         SysLogger.error("EventListDao:update()",e);
	     }
	     finally
	     {
	    	 conn.close();
	     }     
	}

	
	
	
}
