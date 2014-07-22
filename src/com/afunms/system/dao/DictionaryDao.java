/**
 * <p>Description:operate table NMS_USER</p>
 * <p>Company: dhcc.com</p>
 * @author afunms
 * @project afunms
 * @date 2006-08-07
 */

package com.afunms.system.dao;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import com.afunms.common.base.BaseDao;
import com.afunms.common.base.BaseVo;
import com.afunms.common.base.DaoInterface;
import com.afunms.common.util.DBManager;
import com.afunms.common.util.SysLogger;
import com.afunms.system.model.CodeType;
import com.afunms.system.util.CodeUtil;
import com.bpm.design.model.DesignTempModel;
import com.bpm.system.utils.StringUtil;

public class DictionaryDao extends BaseDao implements DaoInterface
{
   public DictionaryDao()
   {
	   super("nms_codetype");
   }

   public List listByPage(int curpage,int perpage)
   {
	   return listByPage(curpage,"",perpage);
   }

   public BaseVo loadFromRS(ResultSet rs) {
	 CodeType vo = new CodeType();
	         try {
				vo.setId(rs.getString("ID"));
				 vo.setName(rs.getString("NAME"));
				 vo.setCode(rs.getString("CODE"));
				 vo.setDesp(rs.getString("DESP"));
				 vo.setSeq(rs.getInt("SEQ"));
				 vo.setType(rs.getString("TYPE"));
			} catch (SQLException e) {
				SysLogger.error("Error in DictionaryDao.loadFromRS()",e);
		          vo = null;
			}
	         
	return vo;
   }

   public boolean save(BaseVo baseVo) {
	boolean flag = true;
	CodeType vo = (CodeType)baseVo;
	StringBuilder sb = new StringBuilder();
	sb.append("insert into nms_codetype (ID,NAME,CODE,DESP,SEQ,TYPE) values ('").append(vo.getId()).append("','")
	.append(vo.getName()).append("','").append(vo.getCode()).append("','").append(vo.getDesp()).append("',").append(vo.getSeq()).append(",'").append(vo.getType()).append("')");
	try {
		conn.executeUpdate(sb.toString());
	} catch (Exception e) {
		SysLogger.error("Error in DictionaryDao.save()",e);
		flag = false;
	}finally {
		conn.close();
	}
	return flag;
   }

   public boolean update(BaseVo baseVo) {
	boolean flag = true;
	CodeType vo = (CodeType)baseVo;
	StringBuilder sb = new StringBuilder();
	sb.append("update nms_codetype set NAME='").append(vo.getName()).append("',DESP='")
	.append(vo.getDesp()).append("',SEQ=").append(vo.getSeq()).append(" where ID='").append(vo.getId()).append("'");
	try {
		conn.executeUpdate(sb.toString());
	} catch (Exception e) {
		SysLogger.error("Error in DictionaryDao.update()",e);
		flag = false;
	}finally {
		conn.close();
	}
	return flag;
  }
   
   public boolean delete(String [] checkbox) {
	   boolean flag = true;
	   StringBuilder sb = new StringBuilder();//ɾ��nms_codetype
	   StringBuilder sb1 = new StringBuilder();
	   StringBuilder sb2 = new StringBuilder();//ɾ��nms_codedetail
	   StringBuilder sb3 = new StringBuilder();//ɾ��bpm_modeltype
	   sb.append("delete from nms_codetype where ID in ('");
	   for(String str : checkbox) {
		   sb1.append(str).append("','");
	   }
	   sb1.delete(sb1.length()-3,sb1.length()-1);
	   sb.append(sb1.toString());
	   sb.append(")");
	   conn.addBatch(sb.toString());
	   sb2.append("delete from nms_codedetail where TYPEID in ('");
	   sb2.append(sb1.toString());
	   sb2.append(")");
	   conn.addBatch(sb2.toString());
	   sb3.append("delete from bpm_modeltype where CTID in ('");
	   sb3.append(sb1.toString());
	   sb3.append(")");
	   conn.addBatch(sb3.toString());
	   try {
		conn.executeBatch();
	} catch (Exception e) {
		SysLogger.error("Error in DictionaryDao.delete()",e);
		flag = false;
	}finally {
		conn.close();
	}
	return flag;
   }
   
   /**
    * ��ȡ����LCLX����һ���ֵ�
    */
  public List<CodeType> loadAll(String where) {
	  StringBuilder sb = new StringBuilder();
	  List<CodeType> list = new ArrayList<CodeType>();
	  sb.append("select ID,NAME,CODE from nms_codetype where 1=1")
	  .append(" AND TYPE = 'LCLX' ");
	  if(StringUtil.isNotBlank(where)) {
		  sb.append(where);
	  }
	  sb.append(" order by seq asc ");
	 ResultSet rs =   conn.executeQuery(sb.toString());
	 CodeType vo = null;
	 try {
		while(rs.next()) {
			 vo = new CodeType();
			 vo.setName(rs.getString("NAME"));
			 vo.setCode(rs.getString("CODE")+"|"+rs.getString("ID"));
			 list.add(vo);
		 }
	} catch (Exception e) {
		SysLogger.error("Error in DictionaryDao.loadAll()", e);
	} finally {
		if(null != rs) {
			try {
				rs.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	return list;
  }
  
  /**
   * ��ȡ����ģ�͵�����
   * @return
   */
  public List<DesignTempModel> getCodeModelType() {
	  String sql = "select bn.id as keytext,agbt.name from act_ge_bytearray_temp  agbt , " +
	  		" bpm_node bn,bpm_modeltype bm where bm.modelid=agbt.modelid and bm.typeid = bn.id";
	  List<DesignTempModel> list = new ArrayList<DesignTempModel>();
	  DBManager dbm = new DBManager();
	  CallableStatement cs = null;
	  String typename = "";
	  ResultSet rs = null;
	  try {
		rs = dbm.executeQuery(sql);
		  ResultSetHandler<List<DesignTempModel>> rsh = new BeanListHandler<DesignTempModel>(DesignTempModel.class);
		  list = rsh.handle(rs);
		  cs = dbm.getConn().prepareCall("{call Pro_GetParNode(?)}");
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
	} catch (SQLException e) {
		SysLogger.error("Error in DictionaryDao.getCodeModelType()", e);
	} finally {
		if(null!=rs) {
			try {
				rs.close();
			} catch (SQLException e) {
			}
			dbm.close();
		}
	}
	  return list;
  }
  /**
   * ��ȡģ�͵����� key Ϊprocessdefinitionid
   * @return
   */
  public  Map<String,String> getCodelModelType() {
	  String sql ="select bn.id as keytext,arp.ID_ as definition from bpm_modeltype bm,act_ge_bytearray_temp  agbt ,bpm_node bn, " +
	  		"act_re_procdef arp where bm.modelid=agbt.modelid and bm.typeid=bn.id " +
	  		"and concat(agbt.name,'.bpmn20.xml')=arp.RESOURCE_NAME_";
	  Map<String,String> map = new HashMap<String, String>();
	  DBManager dbm = new DBManager();
	  ResultSet rs = null;
	  CallableStatement cs = null;
	  String typename = "";
	  String keytext = "";
	  String definition = "";
	  try {
		rs = dbm.executeQuery(sql);
		  while(rs.next()) {
			  map.put(rs.getString("definition"), rs.getString("keytext"));
		  }
	 Set<String> set = map.keySet();
	 Iterator<String> itr = set.iterator();
	 cs = dbm.getConn().prepareCall("{call Pro_GetParNode(?)}");
	 while(itr.hasNext()) {
		 typename = "";
		 definition = itr.next();
		 keytext = map.get(definition);
		 cs.setString(1, keytext);
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
		 map.put(definition, typename);
	 }
	    	  
	} catch (SQLException e) {
		SysLogger.error("Error in DictionaryDao. Map<String,String>getCodelModelType()", e);
	} finally {
		if(null!=rs) {
			try {
				rs.close();
				dbm.close();
			} catch (SQLException e) {
			}
		}
		
	}
	 return map; 
  }

}
