package com.afunms.ip.manage;

import java.util.ArrayList;
import java.util.List;

import com.afunms.common.base.BaseManager;
import com.afunms.common.base.BaseVo;
import com.afunms.common.base.DaoInterface;
import com.afunms.common.base.ManagerInterface;
import com.afunms.ip.stationtype.dao.alltypeDao;
import com.afunms.ip.stationtype.dao.encryptiontypeDao;
import com.afunms.ip.stationtype.dao.stationtypeDao;
import com.afunms.ip.stationtype.model.*;
import com.afunms.system.dao.DepartmentDao;

public class ipEncryptionManager extends BaseManager implements ManagerInterface {

	//private stationtypeDao id = new stationtypeDao();
	
	public String execute(String action) {
		if(action.equals("add")){
			return add();
		}if(action.equals("list")){
			return list();
		}if(action.equals("ready_edit")){
			return ready_edit();
		}if(action.equals("delete")){
			return delete();
		}if(action.equals("update")){
			return update();
		}
		return null;
	}


	
	public String delete(){
		DaoInterface ipconfigdao = new encryptiontypeDao();
		String[] id = request.getParameterValues("checkbox");
		ipconfigdao.delete(id);
		return list();
	}
	

	public String add(){
//		DaoInterface ipconfigdao = new stationtypeDao();
		encryptiontypeDao ipconfigdao = new encryptiontypeDao();
		encryptiontype vo = new encryptiontype();
		String name = request.getParameter("addzt");
		String descr = request.getParameter("descr");
		String bak = request.getParameter("bak");
		vo.setName(name);
		vo.setDescr(descr);
		vo.setBak(bak);
		try{
			ipconfigdao.saveCZ(vo);
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			ipconfigdao.close();
		}
		return list();
	}

	public String list(){
		DaoInterface ipconfigdao = new encryptiontypeDao();
	    setTarget("/ipconfig/encryptiontype/encryptiontypelist.jsp");
        return list(ipconfigdao);
	}
	

	public String ready_edit(){
		DaoInterface ipconfigdao = new encryptiontypeDao();
		String id1 = request.getParameter("id");
//		int id  = Integer.parseInt("id1");
		encryptiontype list =  (encryptiontype)ipconfigdao.findByID(id1);
		request.setAttribute("list", list);
		return "/ipconfig/encryptiontype/edit.jsp";
	}

	protected String update() {
		encryptiontype vo =  new encryptiontype();
		String name = request.getParameter("name");
		String id1 = request.getParameter("id");
		String descr =  request.getParameter("descr");
		String bak = request.getParameter("bak");
		int id = Integer.parseInt(id1);
		vo.setBak(bak);
		vo.setDescr(descr);
		vo.setId(id);
		vo.setName(name);
		DaoInterface dao = new encryptiontypeDao();
		dao.update(vo);
		return list();
	}
	
	

}
