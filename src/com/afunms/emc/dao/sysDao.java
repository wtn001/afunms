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
import com.afunms.ip.stationtype.model.alltype;

public class sysDao extends BaseDao implements DaoInterface {

	public sysDao(){
		super("nms_emcsystem");
	}

	public BaseVo loadFromRS(ResultSet rs) {
		Agent sys = new Agent();
		try {
			sys.setNodeid(rs.getString("nodeid"));
			sys.setName(rs.getString("name"));
			sys.setAgentRev(rs.getString("agentrev"));
			sys.setCabinet(rs.getString("cabinet"));
			sys.setDescr(rs.getString("descr"));
			sys.setModel(rs.getString("model"));
			sys.setModelType(rs.getString("modeltype"));
			sys.setNode(rs.getString("node"));
			sys.setPeerSignature(rs.getString("peersignature"));
			sys.setPhysicalNode(rs.getString("physicalnode"));
			sys.setPromRev(rs.getString("promrev"));
			sys.setRevision(rs.getString("revision"));
			sys.setSCSIId(rs.getString("scsiid"));
			sys.setSerialNo(rs.getString("serialno"));
			sys.setSignature(rs.getString("signature"));
			sys.setSPIdentifier(rs.getString("spidentifier"));
			sys.setSPMemory(rs.getString("spmemory"));
		} catch (Exception e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		    sys=null;
		}
		return sys;
	}
	
	
	public boolean save(BaseVo vo,String nodeid) {
		Agent agent = (Agent) vo;
		StringBuffer addsql = new StringBuffer(200);
		addsql.append("insert into nms_emcsystem(nodeid,name,node,agentrev,cabinet,descr,model,modeltype," +
				"peersignature,physicalnode,promrev,revision,scsiid,serialno,signature,spidentifier,spmemory)values('");
		addsql.append(nodeid);
		addsql.append("','");
		addsql.append(agent.getName());
		addsql.append("','");
		addsql.append(agent.getNode());
		addsql.append("','");
		addsql.append(agent.getAgentRev());
		addsql.append("','");
		addsql.append(agent.getCabinet());
		addsql.append("','");
		addsql.append(agent.getDescr());
		addsql.append("','");
		addsql.append(agent.getModel());
		addsql.append("','");
		addsql.append(agent.getModelType());
		addsql.append("','");
		addsql.append(agent.getPeerSignature());
		addsql.append("','");
		addsql.append(agent.getPhysicalNode());
		addsql.append("','");
		addsql.append(agent.getPromRev());
		addsql.append("','");
		addsql.append(agent.getRevision());
		addsql.append("','");
		addsql.append(agent.getSCSIId());
		addsql.append("','");
		addsql.append(agent.getSerialNo());
		addsql.append("','");
		addsql.append(agent.getSignature());
		addsql.append("','");
		addsql.append(agent.getSPIdentifier());
		addsql.append("','");
		addsql.append(agent.getSPMemory());
		addsql.append("')");
//		System.out.println(addsql.toString());
		return saveOrUpdate(addsql.toString());
	}
	
	public Agent query(String nodeid)
	{
		Agent vo = new Agent();
		List list = new ArrayList();
		String queryonesql = "select * from nms_emcsystem where nodeid='"+nodeid+"'";
		try {
			rs = conn.executeQuery(queryonesql); 
			if(rs == null)return null;
			while(rs.next()){
			vo = (Agent) loadFromRS(rs);	
		   }
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("query cdp vmwareconnectconfig error");
			e.printStackTrace();
		}
		return vo;
	}
	
	 public void delete(String nodeid)
	  {
		  try
		  {
	          conn.executeUpdate("delete from nms_emcsystem where nodeid ='" + nodeid+"'");		  
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
