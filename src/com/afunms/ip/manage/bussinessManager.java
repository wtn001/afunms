package com.afunms.ip.manage;

import java.util.ArrayList;
import java.util.List;

import com.afunms.common.base.BaseManager;
import com.afunms.common.base.BaseVo;
import com.afunms.common.base.DaoInterface;
import com.afunms.common.base.ManagerInterface;
import com.afunms.common.util.SysLogger;
import com.afunms.ip.stationtype.dao.*;
import com.afunms.ip.stationtype.model.*;
import com.afunms.system.dao.DepartmentDao;

public class bussinessManager extends BaseManager implements ManagerInterface {

	// private stationtypeDao id = new stationtypeDao();

	public String execute(String action) {
	
		if (action.equals("list")) {
			return list();
		}
		if (action.equals("edit")) {
			return edit();
		}
		if (action.equals("delete")) {
			return delete();
		}
		if (action.equals("update")) {
			return update();
		}if(action.equals("ready_add")){
			return ready_add();
		}if(action.equals("add")){
			return add();
		}if(action.equals("ready_edit")){
			return ready_edit();
		}
		return null;
	}
	
	public String ready_add(){
		
		//查询骨干点名称
		DaoInterface alltype = new alltypeDao();
		List backbone = alltype.loadAll();
		//查询业务地址类型
		DaoInterface bussinesstype = new bussinesstypeDao();
		List bussiness = bussinesstype.loadAll();
//		for(int i = 0;i<backbone.size();i++){
//			alltype vo = (alltype)backbone.get(i);
//			System.out.println("----------->>>>>"+vo.getBackbone_name());
//		}
		request.setAttribute("backbone", backbone);
		request.setAttribute("bussiness", bussiness);
		return "/ipconfig/bussinessstorage/add.jsp";
	}

	
	
	
	
	public String list(){
		DaoInterface bussiness = new bussinessDao();
//		List list = bussiness.loadAll();
//		request.setAttribute("list", list);
//		return "/ipconfig/bussinessstorage/list.jsp";
		setTarget("/ipconfig/bussinessstorage/list.jsp");
		return list(bussiness);
	}
	

	public String delete() {
		DaoInterface bussiness = new bussinessDao();
		String[] id = request.getParameterValues("checkbox");
		ip_bussinesstype ibus = null;
		bussinessstorage bus_vo = null;
		for(int i=0;i<id.length;i++){
			ibus = (ip_bussinesstype) bussiness.findByID(id[i]);
			String s = ibus.getSegment();
			String gateway = ibus.getGateway();
			String[] st = s.split("/");
			String segment = st[0];
			DaoInterface bu1 = new bussinessstorageDao();
			DaoInterface bu2 = new bussinessstorageDao();
			List l1 = bu1.loadAll(" where bussiness ='"+segment+"'");
			List l2 = bu2.loadAll(" where bussiness ='"+gateway+"'");
			bussinessstorage buss1 = (bussinessstorage) l1.get(0);
			bussinessstorage buss2 = (bussinessstorage) l2.get(0);
			int ip_bussiness_id1 = buss1.getId();
			int ip_bussiness_id2 =  buss2.getId();
			for(int j=ip_bussiness_id1;j<=ip_bussiness_id2;j++){
				bussinessstorageDao bus = new bussinessstorageDao();
				bus.updateIP("update ip_bussiness set type=0,field_id='0' where id = "+j);
			}
		}
		bussiness.delete(id);
		return list();
	}

	public String add(){
		DaoInterface bus = new bussinessDao();
		ip_bussinesstype vo = new ip_bussinesstype();
		String[] backbone_name = request.getParameterValues("backbone");
		String[] bussiness = request.getParameterValues("bussiness");
		String name = request.getParameter("name");
		String segment = request.getParameter("segment");
		String gateway = request.getParameter("gateway");
		String encryption = request.getParameter("encryption");
		
		String segment_ip = segment.substring(0,segment.indexOf("/"));
		String result = segment_ip+"---"+gateway;
		
		
//		System.out.println(backbone_name[0]+"----------"+bussiness[0]+"-------------"+name+"==============="+segment+"--------------"+gateway+"------------"+encryption);
		
//		vo.setBackbone_name(backbone_name[0]);
//		vo.setBussiness(bussiness[0]);
//		vo.setName(name);
//		vo.setSegment(segment);
//		vo.setGateway(gateway);
//		vo.setEncryption(encryption);
//		vo.setAccess_point(result);
		bus.save(vo);
		
		DaoInterface bussinessDao = new bussinessDao();
		List l = bussinessDao.loadAll("where gateway ='"+gateway+"'");
		bussinessDao bussinessDao1 = new bussinessDao();
		
		int bus_id = bussinessDao1.getNextID("ip_bussinessstorage")-1;
		
		ip_bussinesstype ibus = null;
		if(l.size()!= 0 && l!=null)
		{
			 ibus = (ip_bussinesstype) l.get(0);
		}
		int bussiness_id = ibus.getId();
		
		String[] sg = segment.split("/");
		String st = sg[0];
		bussinessstorageDao busDao = new bussinessstorageDao();
		busDao.updateIP("update ip_bussiness set type = 1,field_id='"+bussiness_id+"'  where bussiness = '"+st+"'");
		bussinessstorageDao busDao1 = new bussinessstorageDao();
		busDao1.updateIP("update ip_bussiness set type = 1,field_id='"+bussiness_id+"'  where bussiness = '"+gateway+"'");
		DaoInterface bu1 = new bussinessstorageDao();
		DaoInterface bu2 = new bussinessstorageDao();
		List l1 = bu1.loadAll(" where bussiness ='"+st+"'");
		List l2 = bu2.loadAll(" where bussiness ='"+gateway+"'");
		bussinessstorage buss1 = (bussinessstorage) l1.get(0);
		bussinessstorage buss2 = (bussinessstorage) l2.get(0);
		
		//给业务添加一个vpn  ip地址
		bussinessstorageDao bd = new bussinessstorageDao();
		bd.updateIP("update ip_bussinessstorage set vpn='"+(buss1.getId()+1)+"' where id="+bussiness_id);
		
		bussinessstorageDao busDao3 = new bussinessstorageDao();
		busDao3.updateIP("update ip_bussiness set type = 1,field_id='"+bussiness_id+"'  where id = "+(buss2.getId()-1));
		bussinessstorageDao busDao4 = new bussinessstorageDao();
		busDao4.updateIP("update ip_bussiness set type = 1,field_id='"+bussiness_id+"'  where id = "+(buss1.getId()+1));
		
		for(int i = buss1.getId()+2;i<buss2.getId();i++){
			bussinessstorageDao busDao2 = new bussinessstorageDao();
			busDao2.updateIP("update ip_bussiness set  field_id='"+bussiness_id+"' where id="+i);
		}
		return list();
	}
	
	public String ready_edit(){
		String id = request.getParameter("id");
		
		
		//查询骨干点名称
		DaoInterface alltype = new alltypeDao();
		List backbone = alltype.loadAll();
		//查询业务地址类型
		DaoInterface bussinesstype = new bussinesstypeDao();
		List bussiness = bussinesstype.loadAll();
		DaoInterface ip_bussiness = new bussinessDao();
		ip_bussinesstype list = (ip_bussinesstype) ip_bussiness.findByID(id);
		
		request.setAttribute("list", list);
		request.setAttribute("backbone", backbone);
		request.setAttribute("bussiness", bussiness);

		return "/ipconfig/bussinessstorage/edit.jsp";
	}
	

	public String edit() {
		DaoInterface ipconfigdao = new bussinessDao();
		String id1 = request.getParameter("id");
		// int id = Integer.parseInt("id1");
		ip_bussinesstype list = (ip_bussinesstype) ipconfigdao.findByID(id1);
		request.setAttribute("list", list);
		return list();
	}

	
	protected String update() {
		DaoInterface dao = new bussinessDao();
		String[] backbone = request.getParameterValues("backbone");
		String[] bussiness = request.getParameterValues("bussiness");
		String name =request.getParameter("name");
		String segment = request.getParameter("segment");
		String gateway = request.getParameter("gateway");
		String encryption = request.getParameter("encryption");
		String id = request.getParameter("id");
		
		String[] s = segment.split("/");
		bussinessstorageDao bd1_a = new bussinessstorageDao();
		bussinessstorageDao bd2_a = new bussinessstorageDao();
		bussinessstorage buss1_a = new bussinessstorage();
		bussinessstorage buss2_a = new bussinessstorage();
		
		
		List bus_l1_a = bd1_a.loadAll(" where bussiness = '"+s[0]+"'");
		List bus_l2_a = bd2_a.loadAll(" where bussiness = '"+gateway+"'");
		buss1_a = (bussinessstorage) bus_l1_a.get(0);
		buss2_a = (bussinessstorage) bus_l2_a.get(0);
		int ip_bussinessstorage_id1_a = buss1_a.getId();
		int ip_bussinessstorage_id2_a = buss2_a.getId();
		
		
		int idd =Integer.parseInt(id);
		//查询出  没更新之前的数据
		DaoInterface bus = new bussinessDao();
		ip_bussinesstype bus_vo =  (ip_bussinesstype) bus.findByID(id); 
		String segment_y = bus_vo.getSegment();
		String gateway_y = bus_vo.getGateway();
		String[] sg = segment_y.split("/");
		String sg_y = sg[0];
		bussinessstorageDao bd1 = new bussinessstorageDao();
		bussinessstorageDao bd2 = new bussinessstorageDao();
		bussinessstorage buss1 = new bussinessstorage();
		bussinessstorage buss2 = new bussinessstorage();
		List bus_l1 = bd1.loadAll(" where bussiness = '"+sg_y+"'");
		List bus_l2 = bd2.loadAll(" where bussiness = '"+gateway_y+"'");
		buss1 = (bussinessstorage) bus_l1.get(0);
		buss2 = (bussinessstorage) bus_l2.get(0);
		int ip_bussinessstorage_id1 = buss1.getId();
		int ip_bussinessstorage_id2 = buss2.getId();
		
		
		String segment_ip = segment.substring(0,segment.indexOf("/"));
		String result = segment_ip+"---"+gateway;
		
		ip_bussinesstype vo = new ip_bussinesstype();
//		vo.setBackbone_name(backbone[0]);
//		vo.setBussiness(bussiness[0]);
//		vo.setName(name);
//		vo.setSegment(segment);
//		vo.setGateway(gateway);
//		vo.setEncryption(encryption);
//		vo.setId(idd);
//		vo.setAccess_point(result);
//		dao.update(vo);
		
		//把原来的数据更新成可用状态
//		bussinessstorageDao busdao = new bussinessstorageDao();
//	    busdao.updateIP("update ip_bussiness set type=0,field_id='0' where bussiness = '"+sg_y+"'");
//	    bussinessstorageDao busdao1 = new bussinessstorageDao();
//	    busdao1.updateIP("update ip_bussiness set type=0,field_id='0' where bussiness = '"+gateway_y+"'");
	    for(int i =ip_bussinessstorage_id1;i<=ip_bussinessstorage_id2;i++ ){
	    	bussinessstorageDao busdao2 = new bussinessstorageDao();
		    busdao2.updateIP("update ip_bussiness set type=0,field_id='0' where id = '"+i+"'");
	    }
		//把新数据更新成不可用状态
	    
	    bussinessstorageDao busdao2 = new bussinessstorageDao();
	    busdao2.updateIP("update ip_bussiness set type=1,field_id='"+idd+"' where bussiness = '"+s[0]+"'");
	    bussinessstorageDao busdao3 = new bussinessstorageDao();
	    busdao3.updateIP("update ip_bussiness set type=1,field_id='"+idd+"' where bussiness = '"+gateway+"'");
	   
	    bussinessstorageDao busDao4 = new bussinessstorageDao();
		busDao4.updateIP("update ip_bussiness set type = 1,field_id='"+idd+"'  where id = "+(ip_bussinessstorage_id2_a-1));
	    
		//分配一个vpn地址
		bussinessstorageDao busDao5 = new bussinessstorageDao();
		busDao5.updateIP("update ip_bussiness set type = 1,field_id='"+idd+"'  where id = "+(ip_bussinessstorage_id1_a+1));
		
		bussinessstorageDao busDao6 = new bussinessstorageDao();
		busDao6.updateIP("update ip_bussinessstorage set vpn = '"+(ip_bussinessstorage_id1_a+1)+"' where id="+idd);
		
	    for(int i =ip_bussinessstorage_id1_a+2;i<ip_bussinessstorage_id2_a-1;i++ ){
	    	bussinessstorageDao busdao2_a = new bussinessstorageDao();
		    busdao2_a.updateIP("update ip_bussiness set type=0,field_id='"+idd+"' where id = '"+i+"'");
	    }
	    return list();
	}

    

}
