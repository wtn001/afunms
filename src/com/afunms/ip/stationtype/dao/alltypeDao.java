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

public class alltypeDao extends BaseDao implements DaoInterface {

	public alltypeDao(){
		super("ip_alltype");
	}

	public BaseVo loadFromRS(ResultSet rs)  {
		alltype vo = new alltype();
		 try
	      {
			  vo.setId(rs.getInt("id"));
	          vo.setBackbone_name(rs.getString("backbone_name"));
	          vo.setLoopback_begin(rs.getString("loopback_begin"));
	          vo.setLoopback_end(rs.getString("loopback_end"));
	          vo.setPe_begin(rs.getString("pe_begin"));
	          vo.setPe_end(rs.getString("pe_end"));
	          vo.setPe_ce_begin(rs.getString("pe_ce_begin"));
	          vo.setPe_ce_end(rs.getString("pe_ce_end"));
	          vo.setBus_begin(rs.getString("bus_begin"));
	          vo.setBus_end(rs.getString("bus_end"));
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
		alltype vo=(alltype)baseVo;
		
//		System.out.println("alltypeDao.java_----------------->"+vo.getBackbone_begin()+"==========="+vo.getBackbone_end());
		
		StringBuffer sql = new StringBuffer(100);
		sql.append("insert into ip_alltype (backbone_name,loopback_begin,loopback_end,pe_begin,pe_end,pe_ce_begin,pe_ce_end,bus_begin,bus_end) values(");
		sql.append("'");
		sql.append(vo.getBackbone_name());
		sql.append("',");
		sql.append("'");
		sql.append(vo.getLoopback_begin());
		sql.append("',");
		sql.append("'");
		sql.append(vo.getLoopback_end());
		sql.append("',");
		sql.append("'");
		sql.append(vo.getPe_begin());
		sql.append("',");
		sql.append("'");
		sql.append(vo.getPe_end());
		sql.append("',");
		sql.append("'");
		sql.append(vo.getPe_ce_begin());
		sql.append("',");
		sql.append("'");
		sql.append(vo.getPe_ce_end());
		sql.append("',");
		sql.append("'");
		sql.append(vo.getBus_begin());
		sql.append("',");
		sql.append("'");
		sql.append(vo.getBus_end());
		sql.append("')");
//		System.out.println("------------------->"+sql.toString());
		return saveOrUpdate(sql.toString());
	}

//
	public boolean save(BaseVo vo) {
		// TODO Auto-generated method stub
		return false;
	}
	public boolean update(BaseVo baseVo) {
		boolean result = false;
		alltype vo = (alltype)baseVo;
			StringBuffer sql = new StringBuffer();
			sql.append("update ip_alltype set name='");
			sql.append("");
			sql.append("',descr='");
			sql.append("");
			sql.append("',bak='");
			sql.append("");
			sql.append("' where id=");
			sql.append(vo.getId());
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

	
	public int count(String table,String where)
	   {
		   int i = 0;	   
		   try 
		   {	
			   rs = conn.executeQuery("select count(*) from " + table + " " + where);
			   if(rs == null)return 0;
			   while(rs.next()){
				   i=rs.getInt(1);	
			   }
			 
		   } 
		   catch (Exception e) 
		   {
			   i=0;
			   SysLogger.error("BaseDao.listByPage()",e);
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
		   return i;
	   }

	
}
