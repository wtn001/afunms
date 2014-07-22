package com.afunms.ip.stationtype.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.afunms.common.base.BaseDao;
import com.afunms.common.base.BaseVo;
import com.afunms.common.base.DaoInterface;
import com.afunms.common.util.SysLogger;
import com.afunms.ip.stationtype.model.pestorage;
import com.afunms.ip.stationtype.model.stationtype;

public class pestorageDao extends BaseDao implements DaoInterface {

	public pestorageDao(){
		super("ip_pe");
	}
	@Override
	public BaseVo loadFromRS(ResultSet rs)  {
		pestorage vo = new pestorage();
		 try
	      {
			  vo.setId(rs.getInt("id"));
	          vo.setBackbone1(rs.getString("backbone1"));
	          vo.setBackbone2(rs.getString("backbone2"));
	          vo.setField_id(rs.getInt("field_id"));
	          vo.setBackbone_id(rs.getInt("backbone_id"));
	      }
	      catch(Exception e)
	      {
	          //SysLogger.error("PortconfigDao.loadFromRS()",e);
	    	  e.printStackTrace();
	          vo = null;
	      }
	      return vo;
	}
	
	public List findID(String sql){
		BaseDao base = new pestorageDao();
		return base.findByCriteria(sql);
	}

	 public boolean delete(String[] id)
	   {
		   boolean result = false;
		   try
		   {
		       for(int i=0;i<id.length;i++)
		           conn.addBatch("delete from ip_pe where backbone_id=" + id[i]);
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
	
	 public void saveIP(String ip,int begin,String backbone,int end,String ip_end) {
		 String[] ip1 = ip.split("\\.");
			String[] ip2 = ip_end.split("\\.");
			String address = ip.substring(0, ip.lastIndexOf("."));
			int num_end = ip1[0].length()+ip1[1].length();
			
			String address2 = ip.substring(0,num_end+1);
			String addressend = ip.substring(ip.lastIndexOf(".")+1);
			int end_num = Integer.parseInt(addressend);
			try {
				if(!ip1[2].equals(ip2[2])){
				for(int i=begin; i<=end; i++){
					int x = begin++;
					StringBuffer sql = new StringBuffer(100);
					sql.append("insert into ip_pe (pe,pe_id) values(");
					sql.append("'");
					sql.append(address2+"."+x+"."+addressend);
					sql.append("',");
					sql.append("'");
					sql.append(backbone);
					sql.append("')");
					for(int j=end_num;j<=254;j++){
						StringBuffer sql1 = new StringBuffer();
						sql1.append("insert into ip_pe (pe,pe_id) values(");
						sql1.append("'");
						if(!ip1[2].equals(ip2[2])){
							sql1.append(address2+"."+x+"."+j);
						}else{
							sql1.append(address+"."+x);
						}
						sql1.append("',");
						sql1.append("'");
						sql1.append(backbone);
						sql1.append("')");
						try {
							conn.addBatch(sql1.toString());
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					try {
						conn.addBatch(sql.toString());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				}else if(!ip1[1].equals(ip2[1])){
					
				}else{
					for(int i=begin; i<=end; i++){
						int x = begin++;
						StringBuffer sql = new StringBuffer(100);
						sql.append("insert into ip_pe (pe,pe_id) values(");
						sql.append("'");
						sql.append(address+"."+x);
						sql.append("',");
						sql.append("'");
						sql.append(backbone);
						sql.append("')");
						try {
							conn.addBatch(sql.toString());
						} catch (Exception e) {
							e.printStackTrace();
						}
				}
				}
				conn.executeBatch();
			} catch (Exception e) {
				e.printStackTrace();
			} finally{
				conn.close();
			}
			
		}	
	 
	 
	 public boolean update(int id,int field_id) {
			boolean result = false;
				StringBuffer sql = new StringBuffer();
				sql.append("update ip_pe set type=1,field_id = '");
				sql.append(field_id);
				sql.append("' where id=");
				sql.append(id);
		     try
		     {
		    	 //SysLogger.info(sql.toString());
//		    	 System.out.println("-------->"+sql.toString());
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
		    stationtype vo = (stationtype)baseVo;
			StringBuffer sql = new StringBuffer();
			sql.append("update ip_stationtype set name='");
			sql.append(vo.getName());
			sql.append("',descr='");
			sql.append(vo.getDescr());
			sql.append("',bak='");
			sql.append(vo.getBak());
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

	
	public List QueryTwo(int n,int pe_id)
	   {
		   List list = new ArrayList();
	       try
		   {
	    	
	    		   rs = conn.executeQuery("select t.* from(select * from ip_pe where type=0 and pe_id = "+pe_id+") t  limit 0, "+n); 
	    		   while(rs.next())
				       list.add(loadFromRS(rs));
			   
		   }    
		   catch(Exception ex)
		   {
			   //ex.printStackTrace();
			   SysLogger.error("BaseDao.findByID()",ex);
		   }finally{
			   if(rs != null){
				   try{
					   rs.close();
				   }catch(Exception e){
				   }
			   }
		   }
	       return list;
	   }
	public boolean updateFirst(String pe) {
		boolean result = false;
			StringBuffer sql = new StringBuffer();
			sql.append("update ip_pe set type=0,field_id = '");
			sql.append(0);
			sql.append("' where pe='");
			sql.append(pe);
			sql.append("'");
	     try
	     {
//	    	 SysLogger.info(sql.toString());
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
 
 public boolean updateSecond(String pe,int field_id) {
		boolean result = false;
			StringBuffer sql = new StringBuffer();
			sql.append("update ip_pe set type=1,field_id = '");
			sql.append(field_id);
			sql.append("' where pe='");
			sql.append(pe);
			sql.append("'");
	     try
	     {
//	    	 SysLogger.info(sql.toString());
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
	
 
 public void update(String sql) {
		
     try
     {
//    	 SysLogger.info(sql.toString());
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
     
     public void insert_pe(String sql) {
 		
         try
         {
//        	 SysLogger.info(sql.toString());
             conn.addBatch(sql);
             conn.executeBatch();
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
