package com.bpm.design.dao;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.activiti.editor.data.model.ModelData;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.log4j.Logger;

import com.afunms.common.base.JspPage;
import com.afunms.common.util.DBManager;
import com.afunms.system.util.CodeUtil;
import com.bpm.design.model.DesignTempModel;
import com.bpm.system.dao.SystemDao;
import com.bpm.system.utils.StringUtil;
import com.bpm.system.utils.UUIDKey;

/**
 * 
 * Description:处理流程设计相关数据库操作
 * DesignDao.java Create on 2012-11-9 下午2:01:42 
 * @author hexinlin
 * Copyright (c) 2012 DHCC Company,Inc. All Rights Reserved.
 */
public class DesignDao {

	private  static ResultSet rs = null;
	private static Logger logger = Logger.getLogger(SystemDao.class);
	
	/**
	 * 
	 * Description:获取流程设计列表
	 * Date:2012-11-9
	 * @author hexinlin
	 * @return List<DesignTempModel>
	 */
	public JspPage queryDesign(int curpage, int perpage) {
		 List<DesignTempModel> list = new ArrayList();
		 CallableStatement cs = null;
		 String typename = "";
		 JspPage jspPage=null;
	     String sql="select agbt.*,bn.id as keytext from act_ge_bytearray_temp agbt " +
	     		"left join bpm_modeltype bm on agbt.modelid = bm.modelid " +
	     		"left join bpm_node bn on bm.typeid = bn.id";
	     String countSql="select count(*) from act_ge_bytearray_temp";
	     DBManager db=new DBManager();
	     try {
	    	int rowcount=db.executeQueryCount(countSql);
		    jspPage = new JspPage(perpage, curpage, rowcount);
			rs = db.executeQueryFromAll(sql, jspPage.getMinNum(), perpage);
			ResultSetHandler<List<DesignTempModel>> rsh = new BeanListHandler<DesignTempModel>(DesignTempModel.class);
			list = rsh.handle(rs);
			cs = db.getConn().prepareCall("{call Pro_GetParNode(?)}");
			for(DesignTempModel model : list) {
				typename="";
				cs.setString(1,model.getKeytext());
				rs = cs.executeQuery();
				while(rs.next()) {
					if(!"0".equals(rs.getString("PID"))) {
						if("".equals(typename)) {
							typename = rs.getString("NAME");
						}else {
							typename = rs.getString("NAME")+CodeUtil.flag+typename;
						}
						
					}
				}
				model.setTypename(typename);
			}
			jspPage.setList(list);
		} catch (Exception e) {
			logger.error("DesignDao.queryDesign-------执行出错", e);
		} finally {
			if(null!=rs) {
				try {
					rs.close();
				} catch (SQLException e) {
					
				}
			}
			db.close();
		}
	     return jspPage;
	}
	/**
	 * 
	 * Description:存储流程设计基本模型
	 * Date:2012-11-9
	 * @author hexinlin
	 * @return long
	 */
	public long saveBaseModel(ModelData model,String desc,String keytext){
		long id = StringUtil.getTimeId();
		String sql = String.format("insert into act_ge_bytearray_temp(id,modelid,name,revision,modeljson,editorjson,modeldesc)values('%s','%s','%s','%s','%s','%s','%s')",UUIDKey.getKey(),String.valueOf(id),model.getName(),model.getRevision(),model.getModelJson(),model.getModelEditorJson(),desc);
		DBManager db=new DBManager();
	    db.addBatch(sql);
	    String sql2 = String.format("insert into bpm_modeltype(ID,MODELID,TYPEID) values ('%s','%s','%s')", UUIDKey.getKey(),String.valueOf(id),keytext);
	    db.addBatch(sql2);
	    db.executeBatch();
	    db.close();
	    return id;
	}
	
	/**
	 * 
	 * Description:删除流程模版
	 * Date:2012-11-14
	 * @author hexinlin
	 * @return String
	 */
	public String delProcessModels(String [] checkbox){
		String strs = "(";
		String result = "success";
		for(String s : checkbox) {
			strs = strs + "'"+ s +"'"+",";
		}
		if(strs.length()==1) {
			strs = "('')";
		}else {
			strs = strs.substring(0,strs.length()-1)+")";
		}
		String sql = "delete from act_ge_bytearray_temp where modelid in " + strs;
		try {
			DBManager db=new DBManager();
			db.executeUpdate(sql);
			db.close();
		} catch (Exception e) {
			logger.error("DesignDao--delProcessModels--失败", e);
			result = "error";
		} 
		return result;
	}
}
