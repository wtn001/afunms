package com.afunms.emc.dao;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.afunms.common.base.BaseDao;
import com.afunms.common.base.BaseVo;
import com.afunms.common.base.DaoInterface;
import com.afunms.common.base.JspPage;
import com.afunms.common.util.SysLogger;
import com.afunms.emc.model.Agent;
import com.afunms.emc.model.RaidGroup;
import com.afunms.ip.stationtype.model.alltype;

public class raidDao extends BaseDao implements DaoInterface {

	public raidDao(){
		super("nms_emcraid");
	}

	public BaseVo loadFromRS(ResultSet rs) {
		RaidGroup raid = new RaidGroup();
		try {
			raid.setNodeid(rs.getString("nodeid"));
			raid.setDefragPriority(rs.getString("defragpriority"));
			raid.setRid(rs.getString("rid"));
			raid.setFreeCapacity(rs.getString("freecapacity"));
			raid.setLogicalCapacity(rs.getString("logicalcapacity"));
			raid.setMaxNumDisk(rs.getString("maxnumdisk"));
			raid.setMaxNumLun(rs.getString("maxnumlun"));
			raid.setRawCapacity(rs.getString("rawcapacity"));
			raid.setType(rs.getString("type"));
			raid.setStateStr(rs.getString("state"));
			raid.setDisklistStr(rs.getString("disklist"));
			raid.setLunlistStr(rs.getString("lunlist"));
		} catch (Exception e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		    raid=null;
		}
		return raid;
	}
	
	
	public boolean save(BaseVo vo,String nodeid) {
		RaidGroup raid = (RaidGroup) vo;
		StringBuffer addsql = new StringBuffer(200);
		StringBuffer state = new StringBuffer(100);
		StringBuffer disklist = new StringBuffer(200);
		StringBuffer lunlist = new StringBuffer(200);
		
		if(raid.getState() != null && raid.getState().length>0){
			for(int i=0;i<raid.getState().length;i++)
			{
				state.append(raid.getState()[i]+";");
			}
			}
		if(raid.getDisksList() != null && raid.getDisksList().size()>0){
			 for(int i=0;i<raid.getDisksList().size();i++)
			  {
				  disklist.append(raid.getDisksList().get(i)+";");
			  }
		}
		if(raid.getLunsList() != null && raid.getLunsList().size()>0){
			 for(int i=0;i<raid.getLunsList().size();i++)
			  {
				 lunlist.append(raid.getLunsList().get(i)+";");
			  }
		}
		
		
		addsql.append("insert into nms_emcraid(nodeid,defragpriority,rid,freecapacity,logicalcapacity,maxnumdisk,maxnumlun,rawcapacity," +
				"type,state,disklist,lunlist)values('");
		addsql.append(nodeid);
		addsql.append("','");
		addsql.append(raid.getDefragPriority());
		addsql.append("','");
		addsql.append(raid.getRid());
		addsql.append("','");
		addsql.append(raid.getFreeCapacity());
		addsql.append("','");
		addsql.append(raid.getLogicalCapacity());
		addsql.append("','");
		addsql.append(raid.getMaxNumDisk());
		addsql.append("','");
		addsql.append(raid.getMaxNumLun());
		addsql.append("','");
		addsql.append(raid.getRawCapacity());
		addsql.append("','");
		addsql.append(raid.getType());
		addsql.append("','");
		addsql.append(state.toString());
		addsql.append("','");
		addsql.append(disklist.toString());
		addsql.append("','");
		addsql.append(lunlist.toString());
		addsql.append("')");
//		System.out.println(addsql.toString());
		return saveOrUpdate(addsql.toString());
	}
	
	public List<RaidGroup> query(String nodeid)
	{
		List<RaidGroup> list = new ArrayList<RaidGroup>();
		String queryonesql = "select * from nms_emcraid where nodeid='"+nodeid+"'";
		try {
			rs = conn.executeQuery(queryonesql); 
			if(rs == null)return null;
			while(rs.next()){
			list.add((RaidGroup) loadFromRS(rs));	
		   }
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("query cdp vmwareconnectconfig error");
			e.printStackTrace();
		}
		return list;
	}
	
	 public void delete(String nodeid)
	  {
		  try
		  {
	          conn.executeUpdate("delete from nms_emcraid where nodeid ='" + nodeid+"'");		  
		  }
		  catch(Exception e)
		  {
			  SysLogger.error("Error in SysDao.delete()");
		  }
		  finally
		  {
			  conn.close();
		  }
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
