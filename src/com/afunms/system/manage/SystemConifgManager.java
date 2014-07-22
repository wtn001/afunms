package com.afunms.system.manage;

import com.afunms.common.base.BaseManager;
import com.afunms.common.base.ManagerInterface;
import com.afunms.common.util.CommonAppUtil;
import com.afunms.common.util.SessionConstant;
import com.afunms.system.dao.UserDao;
import com.afunms.system.model.User;



public class SystemConifgManager extends BaseManager implements ManagerInterface{

	
	public String execute(String action) { 
		if(action.equals("collectwebflag")){
			return collectwebflag();
		}else if(action.equals("changeskins")){
			return changeskins();
		}
		return null;
	}
	
	private String changeskins() {
		String skin = getParaValue("skin");
		CommonAppUtil.setSkin(skin);
		UserDao dao = new UserDao();
		User user = (User)session.getAttribute(SessionConstant.CURRENT_USER);
		user.setSkins(skin);
		dao.update(user);
//		System.out.println("...................."+skin);
		return "/user.do?action=home";
	}
	
	/**
	 * �޸�ϵͳ����ģʽ
	 * 0 :�ɼ�����ʼ���
	 * 1 :�ɼ�����ʷ���
	 * @return
	 */
	private String collectwebflag(){
		return "/config/systemconfig/editcollectwebflag.jsp";
	}
}
