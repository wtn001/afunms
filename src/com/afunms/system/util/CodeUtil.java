package com.afunms.system.util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.ResultSetHandler;

import com.afunms.common.util.DBManager;
import com.afunms.common.util.SysLogger;
import com.afunms.system.dao.CodedetailDao;
import com.afunms.system.dao.DictionaryDao;
import com.afunms.system.model.CodeType;
import com.afunms.system.model.Codedetail;
import com.afunms.system.model.User;
import com.bpm.design.model.DesignTempModel;
import com.bpm.system.utils.StringUtil;

/**
 * ��ȡ�����ֵ������Ϣ
 * @author HXL
 * nms_codetype���nms_codedetail��
 */
public class CodeUtil {

	public static String flag = "-->";
	/**
	 * 
	 * @param where �����Ӿ�
	 * @param defaultflag �Ƿ���Ĭ��ֵ
	 * @param defaultValue Ĭ��ֵ
	 * @return
	 */
	public static String getLCLX(String where,boolean defaultflag,String defaultValue) {
		DictionaryDao dao = new DictionaryDao();
		List<CodeType> list = dao.loadAll(where);
		StringBuilder sb = new StringBuilder();
		for(CodeType vo : list) {
			if(defaultflag) {
				if(defaultValue.equals(vo.getCode().split("\\|")[0])) {
					sb.append("<option value='"+vo.getCode()+"' selected>")
					  .append(vo.getName()) 
					  .append("</option>");
				}else {
					sb.append("<option value='"+vo.getCode()+"' >")
					   .append(vo.getName())
					   .append("</option>");
				}
			}else {
				sb.append("<option value='"+vo.getCode()+"' >")
				   .append(vo.getName())
				   .append("</option>");
			}
		}
		return sb.toString();
	}
	
	/**
	 * 
	 * @param where �����Ӿ�
	 * @param defaultflag �Ƿ���Ĭ��ֵ
	 * @param defaultValue Ĭ��ֵ
	 * @return
	 */
	public static String getLCLX_TWO(String where,boolean defaultflag,String defaultValue) {
		CodedetailDao dao = new CodedetailDao();
		List<Codedetail> list = dao.loadAll(where);
		StringBuilder sb = new StringBuilder();
		for(Codedetail vo : list) {
			if(defaultflag) {
				if(defaultValue.equals(vo.getCode())) {
					sb.append("<option value='"+vo.getCode()+"' selected>")
					  .append(vo.getName()) 
					  .append("</option>");
				}else {
					sb.append("<option value='"+vo.getCode()+"' >")
					   .append(vo.getName())
					   .append("</option>");
				}
			}else {
				sb.append("<option value='"+vo.getCode()+"' >")
				   .append(vo.getName())
				   .append("</option>");
			}
		}
		return sb.toString();
	}
	/**
	 * ��ȡ����ģ�͵����� keyΪresourceName
	 * @return
	 */
	public static Map<String, String> getCodeModelType() {
		DictionaryDao dao = new DictionaryDao();
		List<DesignTempModel> list = dao.getCodeModelType();
		Map<String, String> map = new HashMap<String, String>();
		for(DesignTempModel vo : list) {
			map.put(vo.getName()+".bpmn20.xml",vo.getTypename());
		}
		return map;
	} 
	/**
	 * ��ȡ����ģ�͵����� keyΪprocessdefinitionid
	 * @return
	 */
	public static Map<String,String> getCodeModeType_Definition() {
		return new DictionaryDao().getCodelModelType();
	}
	
	/**
	 * ��ȡ�û���Ϣ
	 * @param userId
	 * @return
	 */
	public static  User getUserByUserID(String userId) {
		String sql = "select su.user_id as userid  ,su.name,su.sex, " +
				" sd.dept,sp.name as position,sr.role,aig.name_ as `group`  " +
				"from system_user su " +
				"left join system_department sd on su.dept_id = sd.id left join  system_position sp on su.position_id = sp.id left join system_role sr on su.role_id = sr.id " +
				"left join act_id_membership aim on aim.USER_ID_=su.user_id left join act_id_group aig on aig.ID_=aim.GROUP_ID_ " +
				"where su.user_id='"+userId+"' order by su.user_id,aig.name_";
		DBManager dbm = new DBManager();
		User user = new User();
		user.setGroup("");
		ResultSet rs = null;
		boolean flag = true;
		try {
			rs = dbm.executeQuery(sql);
			while(rs.next()) {
				if(flag) {
					user.setUserid(rs.getString("userid"));
					user.setName(rs.getString("name"));
					user.setSex(rs.getInt("sex"));
					user.setDeptname(rs.getString("dept"));
					user.setPositionname(rs.getString("position"));
					user.setRolename(rs.getString("role"));
//					user.setDept(rs.getInt("dept"));
//					user.setPosition(rs.getInt("position"));
//					user.setRole(rs.getInt("role"));
				}
				flag = false;
				if(StringUtil.isBlank(rs.getString("group"))) continue;
				user.setGroup(user.getGroup()+","+rs.getString("group"));
			}
			user.setGroup(user.getGroup().length()>1?user.getGroup().substring(1):"");
		} catch (SQLException e) {
			SysLogger.error("CodeUtil.getUserByUserID-ʧ��", e);
		} finally {
			if(null!=rs) {
				try {
					rs.close();
					dbm.close();
				} catch (SQLException e) {
					
				}
			}
		}
		return user;
	}
}
