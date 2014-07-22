package com.afunms.ip.stationtype.dao;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.afunms.common.base.BaseDao;
import com.afunms.common.base.BaseVo;
import com.afunms.common.base.DaoInterface;
import com.afunms.common.base.JspPage;
import com.afunms.common.util.SysLogger;
import com.afunms.ip.stationtype.model.alltype;
import com.afunms.ip.stationtype.model.ip_bussinesstype;
import com.afunms.ip.stationtype.model.pe_cestorage;

public class bussinessDao extends BaseDao implements DaoInterface {

	public bussinessDao(){
		super("ip_bussiness");
	}
	
	
	public List QueryList(int id){
		List list = new ArrayList();
		try 
		   {
//			   System.out.println("BaseDao.java--251行------>"+"select * from " + table + " order by id");
			   rs = conn.executeQuery("select * from ip_bussiness where field_id = "+ id);
			   if(rs == null)return null;
			   while(rs.next()){
				  list.add(loadFromRS(rs));				
			   }
		   }   
		   catch(Exception e) 
		   {
			   e.printStackTrace();
	           list = null;
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
		
		return list; 
	}
	

	public boolean delete(String[] id)
	   {
		   boolean result = false;
		   try
		   {
		       for(int i=0;i<id.length;i++)
		           conn.addBatch("delete from ip_bussiness where id=" + id[i]);
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
	
	public boolean delect (List id){
		boolean result = false;
		 try
		   {
			   for(int i=0;i<id.size();i++)
		             conn.addBatch("delete from ip_bussiness where backbone_id=" + id.get(i));
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
	
	public BaseVo loadFromRS(ResultSet rs)  {
		ip_bussinesstype vo = new ip_bussinesstype();
		 try
	      {
			  vo.setId(rs.getInt("id"));
	          vo.setBusname(rs.getString("busname"));
	          vo.setBuskind(rs.getString("buskind"));//实时业务，非实时业务
	          vo.setSegment(rs.getString("segment"));
	          vo.setGateway(rs.getString("gateway"));
	          vo.setEncryption(rs.getString("encryption"));
	          vo.setVlan(rs.getString("vlan"));
	          vo.setField_id(rs.getInt("field_id"));
	          vo.setBackbone_id(rs.getInt("backbone_id"));
	          vo.setVlan_ip(rs.getString("vlan_ip"));
	          vo.setFlag(rs.getString("flag"));
	          vo.setIp_use(rs.getString("ip_use"));
	      }
	      catch(Exception e)
	      {
	          //SysLogger.error("PortconfigDao.loadFromRS()",e);
	    	  e.printStackTrace();
	          vo = null;
	      }
	      return vo;
	}

	
	
	
	
	

//

	public boolean update(BaseVo baseVo) {
		boolean result = false;
		ip_bussinesstype vo = (ip_bussinesstype)baseVo;
			StringBuffer sql = new StringBuffer();
//			sql.append("update ip_bussinessstorage set backbone_name='");
//			sql.append(vo.getBackbone_name());
//			sql.append("',name='");
//			sql.append(vo.getName());
//			sql.append("',bussiness='");
//			sql.append(vo.getBussiness());
//			sql.append("',segment='");
//			sql.append(vo.getSegment());
//			sql.append("',gateway='");
//			sql.append(vo.getGateway());
//			sql.append("',encryption='");
//			sql.append(vo.getEncryption());
//			sql.append("',access_point='");
//			sql.append(vo.getAccess_point());
//			sql.append("' where id=");
//			sql.append(vo.getId());
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
	
	
	
	
	public void updateField_id(int id,int field_id) {
			String sql = "update ip_bussiness set field_id="+field_id+ " where id = "+id;
	     try
	     {
	    	 //SysLogger.info(sql.toString());
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

	@Override
	protected synchronized int getNextID() {
		// TODO Auto-generated method stub
		return super.getNextID();
	}

	public List queryID() {
		List list = new ArrayList();
		String sql = "select max(id) from ip_alltype ";
		 try 
		   {
			   rs = conn.executeQuery(sql);
			   if(rs == null)return null;
			   while(rs.next()){
				  list.add(loadFromRS(rs));	
			   }
		   } 
		   catch(Exception e) 
		   {
	           list = null;
	           e.printStackTrace();
			   SysLogger.error("BaseDao.findByCondition()",e);
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
		return list;
	}

	public List queryBussiness(String backbone_name,String bussiness) {
		List list = new ArrayList();
		String sql = "select t.* from  (select * from ip_bussiness where backbone_name='"+backbone_name+"' and bussiness='"+bussiness+"' and field_id = 0) t  limit 0,8";
		 try 
		   {
//			 System.out.println(sql);
			   rs = conn.executeQuery(sql);
			   if(rs == null)return null;
			   while(rs.next()){
				  list.add(loadFromRS(rs));	
			   }
		   } 
		   catch(Exception e) 
		   {
	           list = null;
	           e.printStackTrace();
			   SysLogger.error("BaseDao.findByCondition()",e);
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
		return list;
	}

	 public void update(String sql) {
			
	     try
	     {
//	    	 SysLogger.info(sql.toString());
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


	public boolean save(BaseVo vo) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public List select(String sql) {
		List list = new ArrayList();
		 try 
		   {
//			 System.out.println(sql);
			   rs = conn.executeQuery(sql);
			   if(rs == null)return null;
			   while(rs.next()){
				  list.add(loadFromRS(rs));	
			   }
		   } 
		   catch(Exception e) 
		   {
	           list = null;
	           e.printStackTrace();
			   SysLogger.error("BaseDao.findByCondition()",e);
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
		return list;
	}
	
	
	
}
