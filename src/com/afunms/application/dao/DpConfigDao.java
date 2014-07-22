/**
 * <p>Description: app_db_node</p>
 * <p>Company:dhcc.com</p>
 * @author miiwill
 * @project afunms
 * @date 2007-1-7
 */

package com.afunms.application.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import com.afunms.application.model.DpConfig;
import com.afunms.common.base.BaseDao;
import com.afunms.common.base.BaseVo;
import com.afunms.common.base.DaoInterface;
import com.afunms.common.util.CreateTableManager;
import com.afunms.common.util.ShareData;
import com.afunms.common.util.SysLogger;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.node.Dp;

public class DpConfigDao extends BaseDao implements DaoInterface {

	public DpConfigDao() {
		
		super("nms_dpconfig");
		
	}
	
	public boolean delete(String []ids){
		if(ids != null && ids.length>0){
			DpConfigDao dpdao = new DpConfigDao();
			List list = dpdao.loadAll();
			if(list == null)list = new ArrayList();
			ShareData.setDpconfiglist(list);
			clearRubbish(list);

		}
		return super.delete(ids);
	}
	
	public void clearRubbish(List baseVoList) {
		List nodeList = PollingEngine.getInstance().getDpList(); // �õ��ڴ��е�list
		for (int index = 0; index < nodeList.size(); index++) {
			if (nodeList.get(index) instanceof Dp) {
				Dp node = (Dp) nodeList.get(index);
				if (baseVoList == null) {
					nodeList.remove(node);
				} else {
					boolean flag = false;
					for (int j = 0; j < baseVoList.size(); j++) {
						DpConfig hostNode = (DpConfig) baseVoList.get(j);
						if (node.getId() == hostNode.getId()) {
							flag = true;
						}
					}
					if (!flag) {
						nodeList.remove(node);
					}
				}
			}
		}
	}

	public BaseVo loadFromRS(ResultSet rs) {
		DpConfig vo = new DpConfig();
		//WebConfig vo=new WebConfig();
		
		try {
			vo.setId(rs.getInt("id"));
			vo.setFlag(rs.getInt("flag"));
			vo.setMon_flag(rs.getInt("mon_flag"));
			vo.setAlias(rs.getString("alias"));
			vo.setSendmobiles(rs.getString("sendmobiles"));
			vo.setNetid(rs.getString("netid"));
			vo.setSendemail(rs.getString("sendemail"));
			vo.setSendphone(rs.getString("sendphone"));
			vo.setIpAddress(rs.getString("ipAddress"));
			vo.setSupperid(rs.getInt("supperid"));
		} catch (SQLException e) {
			
			SysLogger.error("DpConfigDao.loadFromRS()",e);
		}
		return vo;
	}

	public boolean save(BaseVo vo) {
		DpConfig vo1=(DpConfig)vo;
		StringBuffer sql=new StringBuffer();
		sql.append("insert into nms_dpconfig(id,ipaddress,alias,flag,sendmobiles,sendemail,sendphone,supperid,netid) values('");
		sql.append(vo1.getId());
		sql.append("','");
		sql.append(vo1.getIpAddress());
		sql.append("','");
		sql.append(vo1.getAlias());
		sql.append("','");
		sql.append(vo1.getFlag());
		sql.append("','");
		sql.append(vo1.getSendmobiles());
		sql.append("','");
		sql.append(vo1.getSendemail());
		sql.append("','");
		sql.append(vo1.getSendphone());
		sql.append("','");
		sql.append(vo1.getSupperid());
		sql.append("','");
		sql.append(vo1.getNetid());
		sql.append("')");
		boolean result = saveOrUpdate(sql.toString());
		CreateTableManager ctable = new CreateTableManager();
		ctable.createTable(conn,"ping",vo1.getId() + "","ping");
		conn.executeBatch();
		conn.close();
		return result;
		
	}
	   public List getDpByBID(Vector bids){
		   StringBuffer sql = new StringBuffer();
		   String wstr = "";
		   if(bids != null && bids.size()>0){
			   for(int i=0;i<bids.size();i++){
				   if(wstr.trim().length()==0){
					   wstr = wstr+" where ( netid like '%,"+bids.get(i)+",%' "; 
				   }else{
					   wstr = wstr+" or netid like '%,"+bids.get(i)+",%' ";
				   }
			   }
			   wstr=wstr+")";
		   }
		   
		   sql.append("select * from nms_dpconfig "+wstr);
		   //SysLogger.info(sql.toString());
		   return findByCriteria(sql.toString());
	   }
	   
	   public List getWebByFlag(int flag){
		   List rlist = new ArrayList();
		   StringBuffer sql = new StringBuffer();
		   sql.append("select * from nms_urlconfig where flag = "+flag);
		   return findByCriteria(sql.toString());
	   }
	
	public boolean update(BaseVo vo) {
		DpConfig vo1 = (DpConfig)vo;
		StringBuffer sql=new StringBuffer();
		sql.append("update nms_dpconfig set alias ='");
		sql.append(vo1.getAlias());
		sql.append("',flag='");
		sql.append(vo1.getFlag());
		sql.append("',netid='");
		sql.append(vo1.getNetid());
		sql.append("',sendmobiles='");
		sql.append(vo1.getSendmobiles());
		sql.append("',sendemail='");
		sql.append(vo1.getSendemail());
		sql.append("',sendphone='");
		sql.append(vo1.getSendphone());
		sql.append("',ipaddress='");
		sql.append(vo1.getIpAddress());
		sql.append("',supperid='");
		sql.append(vo1.getSupperid());
		sql.append("' where id="+vo1.getId());
System.out.println(sql);
		return saveOrUpdate(sql.toString());
	}
} 