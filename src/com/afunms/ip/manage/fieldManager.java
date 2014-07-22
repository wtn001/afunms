package com.afunms.ip.manage;

import java.awt.Color;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.afunms.common.base.BaseManager;
import com.afunms.common.base.DaoInterface;
import com.afunms.common.base.ManagerInterface;
import com.afunms.initialize.ResourceCenter;
import com.afunms.ip.stationtype.dao.*;
import com.afunms.ip.stationtype.model.*;
import com.lowagie.text.Cell;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.rtf.RtfWriter2;

public class fieldManager extends BaseManager implements ManagerInterface {


	
	
	public String execute(String action) {
		if(action.equals("ready_query")){
			return ready_query();
		}if(action.equals("ready_query1")){
			return ready_query1();
		}
		
		if(action.equals("query")){
			return query();
		}if(action.equals("ready_add")){
			return ready_add();
		}if(action.equals("add")){
			return add();
		}if(action.equals("setReceiver")){
			return setReceiver();
		}if(action.equals("setReceiver1")){
			return setReceiver1();
		}if(action.equals("setReceiver2")){
			return setReceiver2();
		}if(action.equals("update")){
			return update();
		}if(action.equals("facility")){
			return facility();
		}if(action.equals("add1")){
			return add1();
		}
		if(action.equals("find")){
			return find();
		}
		if(action.equals("queryAll")){
			return queryAll();
		}if(action.equals("updateStation")){
			return updateStation();
		}if(action.equals("deleteStation")){
			return deleteStation();
		}if(action.equals("ready_edit")){
			return ready_edit();
		}if(action.equals("delete")){
			return delete();
		}if(action.equals("createdoc")){
			return createdoc();
		}if(action.equals("createdoc1")){
			return createdoc1();
		}if(action.equals("conduct")){
			return conduct();
		}if(action.equals("save")){
			return save();
		}if(action.equals("listpe")){
			return listpe();
		}if(action.equals("listpe_ce")){
			return listpe_ce();
		}if(action.equals("listbussiness")){
			return listbussiness();
		}if(action.equals("bussinessOne")){
			return bussinessOne();
		}if(action.equals("busBackbone")){
			return busBackbone();
		}if(action.equals("createdocLoopback")){
			return createdocLoopback();
		}if(action.equals("createdocPe"))
		{
			return createdocPe();
		}if(action.equals("createdocPe_ce")){
			return createdocPe_ce();
		}if(action.equals("createdocBussiness")){
			return createdocBussiness();
		}if(action.equals("findOne")){
			return findOne();
		}if(action.equals("findLoopback")){
			return findLoopback();
		}if(action.equals("findBussiness")){
			return findBussiness();
		}
		return null;
	}
	
	public String findBussiness(){
		String bus = request.getParameter("bus");
		bussinessDao l = new bussinessDao();
		List lo = l.loadAll(" where vlan_ip = '"+bus+"'");
		ip_bussinesstype loop = null;
		if(lo.size() == 1 && lo != null)
		{
			 loop = (ip_bussinesstype) lo.get(0);
		}else{
			return ready_query();
		}
		DaoInterface f = new fieldDao();
		field f_vo = (field) f.findByID(loop.getField_id()+"");
		alltypeDao all = new alltypeDao();
		alltype all_vo = (alltype) all.findByID(f_vo.getBackbone_id()+"");
		String backbone_name = all_vo.getBackbone_name();
		stationtypeDao sta = new stationtypeDao();
		stationtype st = sta.findByID(f_vo.getRunning());
		String running = st.getName();
		
		
		request.setAttribute("running", running);
		request.setAttribute("backbone_name", backbone_name);
		request.setAttribute("field", f_vo);
		
		return "/ipconfig/ipselect/queryOne.jsp";
		
	}
	
	public String findLoopback(){
		String loopback = request.getParameter("loopback");
		loopbackstorageDao l = new loopbackstorageDao();
		List lo = l.loadAll(" where loopback = '"+loopback+"'");
		loopbackstorage loop = null;
		if(lo.size() == 1 && lo != null)
		{
			 loop = (loopbackstorage) lo.get(0);
		}else{
			return ready_query();
		}
		DaoInterface f = new fieldDao();
		field f_vo = (field) f.findByID(loop.getField_id()+"");
		alltypeDao all = new alltypeDao();
		alltype all_vo = (alltype) all.findByID(f_vo.getBackbone_id()+"");
		String backbone_name = all_vo.getBackbone_name();
		stationtypeDao sta = new stationtypeDao();
		stationtype st = sta.findByID(f_vo.getRunning());
		String running = st.getName();
		
		
		request.setAttribute("running", running);
		request.setAttribute("backbone_name", backbone_name);
		request.setAttribute("field", f_vo);
		
		return "/ipconfig/ipselect/queryOne.jsp";
	}
	
	
	public String findOne(){
		String[] field_id = request.getParameterValues("select1");
		DaoInterface f = new fieldDao();
		field f_vo = (field) f.findByID(field_id[0]);
		alltypeDao all = new alltypeDao();
		alltype all_vo = (alltype) all.findByID(f_vo.getBackbone_id()+"");
		String backbone_name = all_vo.getBackbone_name();
		stationtypeDao sta = new stationtypeDao();
		stationtype st = sta.findByID(f_vo.getRunning());
		String running = st.getName();
		ip_selectDao is = new ip_selectDao();
		List ips = is.loadAll(" where name='"+f_vo.getName()+"'");
		ip_select is_vo = (ip_select) ips.get(0);
		
		request.setAttribute("is_vo", is_vo);
		request.setAttribute("running", running);
		request.setAttribute("backbone_name", backbone_name);
		request.setAttribute("field", f_vo);
		
		return "/ipconfig/ipselect/report.jsp";
	}
	
	public String find (){
		
		String[] field_id = request.getParameterValues("select1");
		DaoInterface f = new fieldDao();
		field f_vo = (field) f.findByID(field_id[0]);
		alltypeDao all = new alltypeDao();
		alltype all_vo = (alltype) all.findByID(f_vo.getBackbone_id()+"");
		String backbone_name = all_vo.getBackbone_name();
		stationtypeDao sta = new stationtypeDao();
		stationtype st = sta.findByID(f_vo.getRunning());
		String running = st.getName();
		
		
		request.setAttribute("running", running);
		request.setAttribute("backbone_name", backbone_name);
		request.setAttribute("field", f_vo);
		
		return "/ipconfig/ipselect/queryOne.jsp";
	}
	
	public String busBackbone(){
		String backbone_id = request.getParameter("backbone_id");
		fieldDao f = new fieldDao();
	    List field =  f.loadAll(" where backbone_id="+backbone_id);
	    request.setAttribute("field", field);
	    DaoInterface all = new alltypeDao();
	    alltype vo = (alltype) all.findByID(backbone_id);
	    request.setAttribute("all", vo);
	    bussinessDao bus = new bussinessDao();
	    List bussiness = bus.loadAll(" where backbone_id="+backbone_id+" and field_id != 0");
	    request.setAttribute("bussiness", bussiness);
		return "/ipconfig/ipselect/busBackbone.jsp";
	}
	
	public String bussinessOne(){
		String f_id = request.getParameter("id");
		DaoInterface f = new fieldDao();
		bussinessDao bus = new bussinessDao();
		field field = (field) f.findByID(f_id);
		List bussiness = bus.loadAll(" where field_id="+f_id );
		request.setAttribute("field", field);
		request.setAttribute("bussiness", bussiness);
		return "/ipconfig/ipselect/bussinessOne.jsp";
	}
	
	
	public String listpe(){
		DaoInterface p = new pestorageDao();
		List pe = p.loadAll(" where field_id != 0");
		DaoInterface all = new alltypeDao();
	    List list = all.loadAll();
	    request.setAttribute("pe", pe);
		request.setAttribute("list", list);
		return "/ipconfig/ipselect/listpe.jsp";
	}
	
    public String listpe_ce(){
    	DaoInterface pe = new pe_cestorageDao();
		List pe_ce = pe.loadAll(" where field_id != 0");
		DaoInterface all = new alltypeDao();
	    List list = all.loadAll();
	    request.setAttribute("pe_ce", pe_ce);
		request.setAttribute("list", list);
		return "/ipconfig/ipselect/listpe_ce.jsp";
	}
	
    public String listbussiness(){
    	DaoInterface pe = new bussinessDao();
		List bussiness = pe.loadAll(" where field_id != 0");
		DaoInterface all = new alltypeDao();
	    List list = all.loadAll();
	    request.setAttribute("bussiness", bussiness);
		request.setAttribute("list", list);
		return "/ipconfig/ipselect/listbussiness.jsp";
	}
	
	
	public String save(){
		String name = request.getParameter("name");
		String[]  station = request.getParameterValues("station");
		String[] loopback_id = request.getParameterValues("loopback");
		String[] pe_id = request.getParameterValues("pe");
		String[] pe_ce_id = request.getParameterValues("pe_ce");
		String[] bus_flag = request.getParameterValues("bussiness");
		String b_id = request.getParameter("backbone_id");
		String id = request.getParameter("id");
		int backbone_id = Integer.parseInt(b_id);
		
		System.out.println(name+"----"+station[0]+"----"+loopback_id[0]+"----"+pe_id[0]+"----"+pe_ce_id[0]+"----"+bus_flag[0]+"----"+backbone_id);
		
		field baseVo = new field();
		baseVo.setName(name);
		baseVo.setBackbone_id(backbone_id);
		baseVo.setRunning(station[0]);
		baseVo.setFlag(1);
		
		fieldDao fd = new fieldDao();
		fd.save(baseVo);
		
		fieldDao fd1 = new fieldDao();
		int field_id = fd1.getNextID("ip_field")-1;
		System.out.println(id+"------------------>"+field_id);
		
		loopbackstorageDao lp = new loopbackstorageDao();
		pestorageDao p = new pestorageDao();
		pe_cestorageDao pc = new pe_cestorageDao();
		bussinessDao bus = new bussinessDao();
		ip_selectDao is = new ip_selectDao();
		
		System.out.println("sql----------->"+"update ip_loopback set field_id ="+field_id+" where id ="+loopback_id[0]);
		lp.update("update ip_loopback set field_id ="+field_id+" where id ="+loopback_id[0]);
		p.update("update ip_pe set field_id="+field_id+" where id ="+pe_id[0]);
		pc.update("update ip_pe_ce set field_id="+field_id+" where id ="+pe_ce_id[0]);
		bus.update("update ip_bussiness set field_id="+field_id+" where flag = '"+bus_flag[0]+"'");
		is.updateFlag(id+"");
		return facility();
	}
	
	public String conduct(){
		String id = request.getParameter("id");
		String backbone_id = request.getParameter("backbone_name");
		DaoInterface apply = new ip_selectDao();
		ip_select vo = (ip_select) apply.findByID(id);
		request.setAttribute("ip_select_vo", vo);
		
		System.out.println(id+"..................................."+backbone_id);
		
		
		//��ѯ�ùǸɵ��Ӧ��loopback,pe,pe_ce,bussiness
		pestorageDao p = new pestorageDao();
		List pe = p.loadAll(" where backbone_id ="+backbone_id+" and field_id = 0");
		request.setAttribute("pe", pe);
		
		pe_cestorageDao pc = new pe_cestorageDao();
		List pe_ce = pc.loadAll(" where backbone_id="+backbone_id+" and field_id = 0");
		request.setAttribute("pe_ce", pe_ce);
		
		loopbackstorageDao lp = new loopbackstorageDao();
		List loopback  = lp.loadAll(" where backbone_id="+backbone_id+" and field_id=0");
//		for(int i = 0;i<loopback.size();i++){
//			loopbackstorage lpp = new loopbackstorage();
//			lpp=(loopbackstorage) loopback.get(i);
//			System.out.println(loopback.size()+"-----------------"+lpp.getLoopback());
//		}
		request.setAttribute("loopback", loopback);
		
		bussinessDao bus = new bussinessDao();
		List bussiness = bus.select("select t.* from ip_bussiness t where backbone_id="+backbone_id+" and field_id=0 group by flag  order by id");
		request.setAttribute("bussiness", bussiness);
		
		DaoInterface sta = new stationtypeDao();
		List station = sta.loadAll();
		request.setAttribute("station", station);
		request.setAttribute("backbone_id", backbone_id);
		request.setAttribute("id", id);
		return "/ipconfig/ipselect/conduct.jsp";
	}
	
	public String update(){
		String n = request.getParameter("num");
		int num = Integer.parseInt(n);
//		System.out.println("--------����------------��"+num);
		//��վ��ID
		fieldDao fielddao = new fieldDao();
		int field_id = fielddao.getNextID("ip_field")-1;
		
		
		//loopback
		String[] loopback = request.getParameterValues("sendemail");
		String[] loopback_h = request.getParameterValues("loopback_h");
		String[] loopback_id = request.getParameterValues("l");
		
//		System.out.println(loopback[0]+"-----------------------"+loopback_h[0]+",,,,,,,,,field_id-----------"+field_id);
		//pe
		String[] pe = request.getParameterValues("sendemail1");
		String[] pe_h = request.getParameterValues("pe_h");
		String[] p = request.getParameterValues("p");
		//pe_ce
		String[] pe_ce = request.getParameterValues("sendemail2");
		String[] pe_ce_h = request.getParameterValues("pe_ce_h");
		String[] pc = request.getParameterValues("pc");
		
		for(int i=0;i<num;i++){
			if(!loopback[i].equals(loopback_h[i])){
				loopbackstorageDao l = new loopbackstorageDao();
				//���Զ������Loopback��ַ�ĳɿ���״̬��
				l.updateFirst(loopback_h[i]);
				loopbackstorageDao l1 = new loopbackstorageDao();
				//���޸ĺ��loopback��ַ�ĳɸó�վ���ѱ�ʹ�õ�״̬
				l1.updateSecond(loopback[i], field_id);
			}else if(!pe[i].equals(pe_h[i])){
				pestorageDao pedao = new pestorageDao();
				pedao.updateFirst(pe_h[i]);
				pestorageDao pedao1 = new pestorageDao();
				pedao1.updateSecond(pe[i], field_id);
			}else if(!pe_ce[i].equals(pe_ce_h[i])){
				pe_cestorageDao pcdao= new pe_cestorageDao();
				pcdao.updateFirst(pe_ce_h[i]);
				pe_cestorageDao pcdao1= new pe_cestorageDao();
				pcdao1.updateSecond(pe_ce[i], field_id);
			}
		}
		
		return loadOne(field_id);
	}
	
	public  String loadOne(int field_id){
		DaoInterface field = new fieldDao();
		field f = (field) field.findByID(field_id+"");
//		System.out.println("-------------------------------------?//////////////////>>>>>>>>>"+field_id);
		String name = f.getName();
		DaoInterface ip_selectdao = new ip_selectDao();
		List iss  = (List) ip_selectdao.loadAll("where name = '"+name+"'");
		if(iss.size()!=0 && iss != null){
		ip_select is = (ip_select) iss.get(0);
		if(is.getIds().contains(",")){
			String[] alltype_id = is.getIds().split(",");
			DaoInterface all = new alltypeDao();
			alltype a = (alltype)all.findByID(alltype_id[0]);
			String backbone1 = a.getBackbone_name();
			DaoInterface all2 = new alltypeDao();
			alltype a2 = (alltype)all2.findByID(alltype_id[1]);
			String backbone2 = a.getBackbone_name();
			request.setAttribute("backbone1", backbone1);
			request.setAttribute("backbone2", backbone2);
			
		}else{
			DaoInterface all = new alltypeDao();
			alltype a = (alltype)all.findByID(is.getIds());
			String backbone1 = a.getBackbone_name();
			request.setAttribute("backbone1", backbone1);
			request.setAttribute("backbone2", "");
		}
		request.setAttribute("list", is);
		}
		//�õ�������ӽ�ȥ��վ��  ID 
		fieldDao fielddao = new fieldDao();
		int id1 = fielddao.getNextID("ip_field")-1;
//		System.out.println("------------------------>"+id1);
		// �õ��ó�վ�¶�Ӧ��   ҵ��
		DaoInterface bussinessstorage = new bussinessDao();
		List bussiness = bussinessstorage.loadAll("where field_id = "+id1);
		// �õ��ó�վ�¶�Ӧ��   loopback
		DaoInterface lp = new loopbackstorageDao();
		List loopback = lp.loadAll("where field_id = "+id1);
		// �õ��ó�վ�¶�Ӧ��   pe
		DaoInterface p = new pestorageDao();
		List pe = p.loadAll("where field_id = "+id1);
		// �õ��ó�վ�¶�Ӧ��   pe_ce
		DaoInterface pc = new pe_cestorageDao();
		List pe_ce = pc.loadAll("where field_id = "+id1);
		request.setAttribute("loopback", loopback);
		request.setAttribute("pe",pe );
		request.setAttribute("pe_ce",pe_ce );
		request.setAttribute("bussiness",bussiness );
		
		return "/ipconfig/ipselect/listOne.jsp";
	}
	
	
	
	
	
	
	
	
	public String add(){
		field f = new field();
		String[] backbone = request.getParameterValues("backbone");
		String[] station = request.getParameterValues("station");
		String name = request.getParameter("name");
		String[] num = request.getParameterValues("num");
		String section = request.getParameter("section");
		System.out.println("section��û������-------------------------------->"+section);
		int number = Integer.parseInt(num[0]);
		
		int backbone_id = Integer.parseInt(backbone[0]);
		
		fieldDao field = new fieldDao();
		loopbackstorageDao loopback =  new loopbackstorageDao();
		pestorageDao pe = new pestorageDao();
		pe_cestorageDao pe_ce = new pe_cestorageDao();
		fieldDao fb = new fieldDao();
		int max_id = fb.getNextID("ip_field");
		
		//�õ���ip_loopback ��loopback_id = backbone_id �Ĳ���������С��  type=0�ģ�����IP�ģ�  һ������ ��
		loopbackstorage loopback_vo = null;
		List lb = loopback.QueryTwo(number,backbone_id);
		for(int i=0;i<lb.size();i++){
			loopback_vo = (loopbackstorage) lb.get(i);
			loopbackstorageDao loopback1 =  new loopbackstorageDao();
			loopback1.update(loopback_vo.getId(),max_id);
		}
		
		
		//�õ���ip_pe ��pe_id = backbone_id �Ĳ���������С��  type=0�ģ�����IP�ģ�  duo������ ��
		pestorage pe_vo = null;
		List pe_f_id = pe.QueryTwo(number,backbone_id);
		for(int i=0;i<pe_f_id.size();i++){
			pe_vo = (pestorage) pe_f_id.get(i);
			pestorageDao pe1 = new pestorageDao();
			pe1.update(pe_vo.getId(),max_id);
		}
		
		//conn.executeUpdate("update ip_pe set type = 1,field_id='"+field_id+"'  where id = ");
		
		
		//�õ���ip_pe_ce ��pe_ce_id = backbone_id �Ĳ���������С��  type=0�ģ�����IP�ģ�  һ������ ��
		pe_cestorage pe_ce_vo = null;
		List pe_ce_f_id = pe_ce.QueryTwo(number,backbone_id);
		for(int i=0;i<pe_ce_f_id.size();i++){
			pe_ce_vo = (pe_cestorage) pe_ce_f_id.get(i);
			pe_cestorageDao pe_ce1 = new pe_cestorageDao();
			pe_ce1.update(pe_ce_vo.getId(),max_id);
		}
		
		
		fieldDao fielddao = new fieldDao();
		int id = fielddao.getNextID("ip_field");
		
		//��վ�����ʵʱҵ��
		bussinessDao bu = new bussinessDao();
		DaoInterface alltype = new alltypeDao();
		alltype all = (alltype) alltype.findByID(backbone[0]);
		
		List list_bussiness = bu.queryBussiness(all.getBackbone_name(), "ʵʱҵ��");
		ip_bussinesstype bu_vo = null;
		for(int i = 0;i<list_bussiness.size();i++){
			bu_vo = (ip_bussinesstype) list_bussiness.get(i);
			bussinessDao b1 = new bussinessDao();
			b1.updateField_id(bu_vo.getId(),id);
		}
		//��վ����ӷ�ʵʱҵ��
		bussinessDao bu2 = new bussinessDao();
		List list_bussiness1 = bu2.queryBussiness(all.getBackbone_name(), "��ʵʱҵ��");
		ip_bussinesstype bu_vo2 = null;
		for(int i = 0;i<list_bussiness1.size();i++){
			bu_vo2 = (ip_bussinesstype) list_bussiness1.get(i);
			bussinessDao b2 = new bussinessDao();
			b2.updateField_id(bu_vo2.getId(),id);
		}
		
		f.setName(name);
		f.setRunning(station[0]);
//		f.setBackbone_name(backbone[0]);
		field.save(f);
		
		bussinessDao bus = new bussinessDao();
 		List list = bus.QueryList(id);
		
		request.setAttribute("loopback", lb);
		request.setAttribute("pe", pe_f_id);
		request.setAttribute("pe_ce", pe_ce_f_id);
		request.setAttribute("list", list);
		request.setAttribute("name", name);
		request.setAttribute("backbone_name", all.getBackbone_name());
		request.setAttribute("backbone_id", all.getId()+"");
		request.setAttribute("running", station[0]);
		request.setAttribute("field_id", id);
		return "/ipconfig/ipselect/editOne.jsp";
	}
	
	
	public String add1(){
		field f = new field();
		String backbone = request.getParameter("backbone_name");
//		String[] station = request.getParameterValues("station");
		String num = request.getParameter("num");
		String apply_id = request.getParameter("id");
		String section = request.getParameter("section");
		System.out.println("-----section------->"+section);
		ip_selectDao is1 = new ip_selectDao();
		ip_select ivo = (ip_select)is1.findByID(apply_id);
		String name = ivo.getName();
		
//		DaoInterface al = new alltypeDao();
//		alltype avo = (alltype)al.loadAll("where backbone_name = '"+backbone+"'");
//		int backbone_id = avo.getId();
		
		
		int number = Integer.parseInt(num);
		
		int backbone_id = Integer.parseInt(backbone);
		
		fieldDao field = new fieldDao();
		loopbackstorageDao loopback =  new loopbackstorageDao();
		pestorageDao pe = new pestorageDao();
		pe_cestorageDao pe_ce = new pe_cestorageDao();
		fieldDao fielddao = new fieldDao();
		f.setName(name);
		f.setRunning("��Ͷ��");
		f.setBackbone_id(backbone_id);
		field.save(f);
		fieldDao fb = new fieldDao();
		int max_id = fb.getNextID("ip_field")-1;
		
		//�õ���ip_loopback ��loopback_id = backbone_id �Ĳ���������С��  type=0�ģ�����IP�ģ�  һ������ ��
		loopbackstorage loopback_vo = null;
		List lb = loopback.QueryTwo(number,backbone_id);
		for(int i=0;i<lb.size();i++){
			loopback_vo = (loopbackstorage) lb.get(i);
			loopbackstorageDao loopback1 =  new loopbackstorageDao();
			
//			System.out.println(max_id+"========................==============="+loopback_vo.getId());
			
			loopback1.update(loopback_vo.getId(),max_id);
		}
		
		
		//�õ���ip_pe ��pe_id = backbone_id �Ĳ���������С��  type=0�ģ�����IP�ģ�  duo������ ��
		pestorage pe_vo = null;
		List pe_f_id = pe.QueryTwo(number,backbone_id);
		for(int i=0;i<pe_f_id.size();i++){
			pe_vo = (pestorage) pe_f_id.get(i);
			pestorageDao pe1 = new pestorageDao();
			pe1.update(pe_vo.getId(),max_id);
		}
		
		//conn.executeUpdate("update ip_pe set type = 1,field_id='"+field_id+"'  where id = ");
		
		
		//�õ���ip_pe_ce ��pe_ce_id = backbone_id �Ĳ���������С��  type=0�ģ�����IP�ģ�  һ������ ��
		pe_cestorage pe_ce_vo = null;
		List pe_ce_f_id = pe_ce.QueryTwo(number,backbone_id);
		for(int i=0;i<pe_ce_f_id.size();i++){
			pe_ce_vo = (pe_cestorage) pe_ce_f_id.get(i);
			pe_cestorageDao pe_ce1 = new pe_cestorageDao();
			pe_ce1.update(pe_ce_vo.getId(),max_id);
		}
		
		
		
		
		int id = fielddao.getNextID("ip_field")-1;
		
//		System.out.println("----------������-----------------------������"+id);
		
		//��վ�����ʵʱҵ��
		bussinessDao bu = new bussinessDao();
		DaoInterface alltype = new alltypeDao();
		alltype all = (alltype) alltype.findByID(backbone);
		
		List list_bussiness = bu.queryBussiness(all.getBackbone_name(), "ʵʱҵ��");
		ip_bussinesstype bu_vo = null;
		for(int i = 0;i<list_bussiness.size();i++){
			bu_vo = (ip_bussinesstype) list_bussiness.get(i);
			bussinessDao b1 = new bussinessDao();
			b1.updateField_id(bu_vo.getId(),id);
		}
		//��վ����ӷ�ʵʱҵ��
		bussinessDao bu2 = new bussinessDao();
		List list_bussiness1 = bu2.queryBussiness(all.getBackbone_name(), "��ʵʱҵ��");
		ip_bussinesstype bu_vo2 = null;
		for(int i = 0;i<list_bussiness1.size();i++){
			bu_vo2 = (ip_bussinesstype) list_bussiness1.get(i);
			bussinessDao b2 = new bussinessDao();
			b2.updateField_id(bu_vo2.getId(),id);
		}
		
//		f.setName(name);
//		f.setRunning("��Ͷ��");
//		f.setBackbone_name(backbone);
//		field.save(f);
		
		bussinessDao bus = new bussinessDao();
 		List list = bus.QueryList(id);
		
 		ip_selectDao is = new ip_selectDao();
 		is.updateFlag(apply_id);
 		
		request.setAttribute("loopback", lb);
		request.setAttribute("pe", pe_f_id);
		request.setAttribute("pe_ce", pe_ce_f_id);
		request.setAttribute("list", list);
		request.setAttribute("name", name);
		request.setAttribute("backbone_name", all.getBackbone_name());
		request.setAttribute("backbone_id", all.getId()+"");
		request.setAttribute("running", "��Ͷ��");
		request.setAttribute("apply_id", apply_id);
		
		return "/ipconfig/ipselect/editOne.jsp";
		
	}
	
	
	

	public String ready_add(){
		DaoInterface st = new stationtypeDao();
		List station = st.loadAll();
		DaoInterface  all = new alltypeDao();
		List backbone_name = all.loadAll();
		request.setAttribute("station", station);
		request.setAttribute("backbone", backbone_name);
		return "/ipconfig/ipselect/add.jsp";
	}
	
	
	public String ready_query(){
		DaoInterface field = new fieldDao();
		List list = field.loadAll();
		request.setAttribute("list", list);
		return "/ipconfig/ipselect/find.jsp";
	}
	
	public String ready_query1(){
		DaoInterface field = new fieldDao();
		List list = field.loadAll();
		request.setAttribute("list", list);
		return "/ipconfig/ipselect/findOne.jsp";
	}
	
	public String query(){
		String id = request.getParameter("id");//ip_portal_apply���Ӧ��ID
		DaoInterface ip_selectdao = new ip_selectDao();
		ip_select is  = (ip_select) ip_selectdao.findByID(id);
//		System.out.println("-----------ip_portal_apply���Ӧ��ID---------------->>>>>>>>>>"+id);
		if(is != null){
		if(is.getIds().contains(",")){
			String[] alltype_id = is.getIds().split(",");
			DaoInterface all = new alltypeDao();
			alltype a = (alltype)all.findByID(alltype_id[0]);
			String backbone1 = a.getBackbone_name();
			DaoInterface all2 = new alltypeDao();
			alltype a2 = (alltype)all2.findByID(alltype_id[1]);
			String backbone2 = a.getBackbone_name();
			request.setAttribute("backbone1", backbone1);
			request.setAttribute("backbone2", backbone2);
			
		}else{
			DaoInterface all = new alltypeDao();
			alltype a = (alltype)all.findByID(is.getIds());
			String backbone1 = a.getBackbone_name();
			request.setAttribute("backbone1", backbone1);
			request.setAttribute("backbone2", "");
		}
		request.setAttribute("list", is);
		}
		//�õ�������ӽ�ȥ��վ��  ID 
		fieldDao fielddao = new fieldDao();
		int id1 = fielddao.getNextID("ip_field")-1;
//		System.out.println("------------------------>"+id1);
		// �õ��ó�վ�¶�Ӧ��   ҵ��
		DaoInterface bussinessstorage = new bussinessDao();
		List bussiness = bussinessstorage.loadAll("where field_id = "+id1);
		// �õ��ó�վ�¶�Ӧ��   loopback
		DaoInterface lp = new loopbackstorageDao();
		List loopback = lp.loadAll("where field_id = "+id1);
		// �õ��ó�վ�¶�Ӧ��   pe
		DaoInterface p = new pestorageDao();
		List pe = p.loadAll("where field_id = "+id1);
		// �õ��ó�վ�¶�Ӧ��   pe_ce
		DaoInterface pc = new pe_cestorageDao();
		List pe_ce = pc.loadAll("where field_id = "+id1);
		request.setAttribute("loopback", loopback);
		request.setAttribute("pe",pe );
		request.setAttribute("pe_ce",pe_ce );
		request.setAttribute("bussiness",bussiness );
		
		return "/ipconfig/ipselect/listOne.jsp";
	}
	
	public String setReceiver(){
//		System.out.println("---------------------------------------------------");
		String event = request.getParameter("event");
		request.setAttribute("event", event);
		String value = request.getParameter("val");
//		System.out.println("����ɳ��---------------------��"+event+"-----------"+value);
		
		DaoInterface loopback = new loopbackstorageDao();
		List list = loopback.loadAll("where loopback_id = "+value+"  and type = 0");
		request.setAttribute("list", list);
		return "/ipconfig/ipselect/setreceiverLoopback.jsp";
		
	}
	
	public String setReceiver1(){
		String event = request.getParameter("event");
		request.setAttribute("event", event);
		String value = request.getParameter("val");
//		System.out.println("����ɳ��---------------------��"+event+"-----------"+value);
		
		DaoInterface pe = new pestorageDao();
		List list = pe.loadAll("where pe_id = "+value+"  and type = 0");
		request.setAttribute("list", list);
		return "/ipconfig/ipselect/setreceiverPe.jsp";
		
	}
	
	public String setReceiver2(){
		String event = request.getParameter("event");
		request.setAttribute("event", event);
		String value = request.getParameter("val");
//		System.out.println("����ɳ��---------------------��"+event+"-----------"+value);
		
		DaoInterface pe_ce = new pe_cestorageDao();
		List list = pe_ce.loadAll("where pe_ce_id = "+value+"  and type = 0");
		request.setAttribute("list", list);
		return "/ipconfig/ipselect/setreceiverPe_ce.jsp";
		
	}
	                           
	
	public String facility(){
		DaoInterface select = new ip_selectDao();
		List list = select.loadAll();
		request.setAttribute("list", list);
//		setTarget("/ipconfig/ipselect/list.jsp");
		return "/ipconfig/ipselect/list.jsp";
	}
	
	//ͨ����վ����ѯ��Ӧ�ĳ�վ
//	public String find(){
//		String[] f_id = request.getParameterValues("station");
//		int field_id= Integer.parseInt(f_id[0]);
//		DaoInterface field = new fieldDao();
//		field f = (field) field.findByID(field_id+"");
////		System.out.println(f_id[0]+"-------------------------------------?//////////////////>>>>>>>>>"+field_id);
//		String name = f.getName();
////		String section = f.getSection();
////		String all_id = f.getBackbone_name();
//		DaoInterface ip_selectdao = new ip_selectDao();
//		List iss  = (List) ip_selectdao.loadAll("where name = '"+name+"' and ids='"+all_id+","+section+"'");
////		System.out.println(name+"............."+section+"......................"+all_id);
//		if(iss.size()!=0 && iss!=null){
//		    ip_select is = (ip_select) iss.get(0);
//			String[] alltype_id = is.getIds().split(",");
//			DaoInterface all = new alltypeDao();
//			alltype a = (alltype)all.findByID(alltype_id[0]);
//			String backbone1 = "";
//			String backbone2 = "";
//			if(alltype_id[1].equals("1")){
//				 backbone1 = a.getBackbone_name();
//				 backbone2 = "";
//			}else if(alltype_id[1].equals("2")){
//				 backbone1 = "";
//				 backbone2 = a.getBackbone_name();
//			}
//			request.setAttribute("backbone1", backbone1);
//			request.setAttribute("backbone2", backbone2);
//			
////		else{
////			DaoInterface all = new alltypeDao();
////			alltype a = (alltype)all.findByID(is.getIds());
////			String backbone1 = a.getBackbone_name();
////			request.setAttribute("backbone1", backbone1);
////			request.setAttribute("backbone2", "");
////		}
//        
//		request.setAttribute("list", is);
//		}
//		//�õ�������ӽ�ȥ��վ��  ID 
////		fieldDao fielddao = new fieldDao();
////		int id1 = fielddao.getNextID("ip_field");
////		System.out.println("------------------------>"+field_id);
//		// �õ��ó�վ�¶�Ӧ��   ҵ��
//		DaoInterface bussinessstorage = new bussinessDao();
//		List bussiness = bussinessstorage.loadAll("where field_id = "+field_id);
//		// �õ��ó�վ�¶�Ӧ��   loopback
//		DaoInterface lp = new loopbackstorageDao();
//		List loopback = lp.loadAll("where field_id = "+field_id);
//		// �õ��ó�վ�¶�Ӧ��   pe
//		DaoInterface p = new pestorageDao();
//		List pe = p.loadAll("where field_id = "+field_id);
//		// �õ��ó�վ�¶�Ӧ��   pe_ce
//		DaoInterface pc = new pe_cestorageDao();
//		List pe_ce = pc.loadAll("where field_id = "+field_id);
//		request.setAttribute("loopback", loopback);
//		request.setAttribute("pe",pe );
//		request.setAttribute("pe_ce",pe_ce );
//		request.setAttribute("bussiness",bussiness );
//		
//		return "/ipconfig/ipselect/listOne.jsp";
//	}
	
	public String queryAll()
	{
        DaoInterface all = new alltypeDao();
        List list = all.loadAll();
//        System.out.println(list.size()+"------------------------------------------------------------------------------");
        request.setAttribute("list", list);
		return "/ipconfig/ipselect/listloopback.jsp";
	}
	
	public String updateStation(){
		
		String field_id = request.getParameter("id");
		DaoInterface i = new fieldDao();
		field f_vo = (field) i.findByID(field_id);
		String stationName = f_vo.getName();
		String name = request.getParameter("name");
		String[] station = request.getParameterValues("station");
		fieldDao f = new fieldDao();
		f.update(field_id, name, station[0]);
		ip_selectDao ip_select  = new ip_selectDao();
		List l = (List)ip_select.loadAll(" where name = '"+stationName+"'");
		ip_select is = (ip_select) l.get(0);
		ip_selectDao ip_select1  = new ip_selectDao();
		ip_select1.updateName(is.getId()+"", name);
		return queryAll();
	}
	
	public String deleteStation(){
		String field_id = request.getParameter("id");
		
//		System.out.println("-------------delete------------------>"+field_id);
		
		loopbackstorageDao loopback = new loopbackstorageDao();
		pestorageDao pe = new pestorageDao();
		pe_cestorageDao pe_ce = new pe_cestorageDao();
		fieldDao field = new fieldDao();
		ip_selectDao is = new ip_selectDao();
		bussinessDao bus = new bussinessDao();
		
		field f = (field) field.findByID(field_id);
		
		
		loopback.update("update ip_loopback set  type = 0,field_id=0 where field_id = "+field_id);
		pe.update("update ip_pe  set  field_id=0 where field_id = "+field_id);
		pe_ce.update("update ip_pe_ce set field_id=0 where field_id = "+field_id);
		bus.update("update ip_bussiness  set field_id=0 where field_id = "+field_id);
		
		field.delete("delete from ip_field where id = "+field_id);
		is.delete("delete from ip_portal_apply where name = '"+f.getName()+"'");
		return queryAll();
	}
	
	public String ready_edit(){
		String field_id = request.getParameter("id");
		DaoInterface st = new stationtypeDao();
		List station = st.loadAll();
		DaoInterface  all = new alltypeDao();
		List backbone_name = all.loadAll();
		DaoInterface field = new fieldDao();
		field f = (field)field.findByID(field_id);
		request.setAttribute("station", station);
		request.setAttribute("backbone", backbone_name);
		request.setAttribute("field", f);
		return "/ipconfig/ipselect/editStation.jsp";
	}
	
	
	public String delete(){
		String[] s = request.getParameterValues("checkbox");
		DaoInterface ip_select = new ip_selectDao();
		ip_select.delete(s);
		return facility();
	}
	
	
	public String createdocLoopback(){
		String file = "/temp/loopback.doc";// ���浽��Ŀ�ļ����µ�ָ���ļ���
		String fileName = ResourceCenter.getInstance().getSysPath() + file;// ��ȡϵͳ�ļ���·��
		try {
			createDocContextEventLoopback(fileName);
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		request.setAttribute("filename", fileName);
		return "/ipconfig/ipselect/download.jsp";
	}
	
	public String createdocPe(){
		String file = "/temp/pe.doc";// ���浽��Ŀ�ļ����µ�ָ���ļ���
		String fileName = ResourceCenter.getInstance().getSysPath() + file;// ��ȡϵͳ�ļ���·��
		try {
			createDocContextEventPe(fileName);
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		request.setAttribute("filename", fileName);
		return "/ipconfig/ipselect/download.jsp";
	}
	
	public String createdocPe_ce(){
		String file = "/temp/pe_ce.doc";// ���浽��Ŀ�ļ����µ�ָ���ļ���
		String fileName = ResourceCenter.getInstance().getSysPath() + file;// ��ȡϵͳ�ļ���·��
		try {
			createDocContextEventPe_ce(fileName);
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		request.setAttribute("filename", fileName);
		return "/ipconfig/ipselect/download.jsp";
	}
	
	public String createdocBussiness(){
		String file = "/temp/bussiness.doc";// ���浽��Ŀ�ļ����µ�ָ���ļ���
		String fileName = ResourceCenter.getInstance().getSysPath() + file;// ��ȡϵͳ�ļ���·��
		String backbone_id = request.getParameter("backbone_id");
		try {
			createDocContextEventBussiness(fileName,backbone_id);
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		request.setAttribute("filename", fileName);
		return "/ipconfig/ipselect/download.jsp";
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	public String createdoc(){
		String file = "/temp/shenqingbiao.doc";// ���浽��Ŀ�ļ����µ�ָ���ļ���
		String fileName = ResourceCenter.getInstance().getSysPath() + file;// ��ȡϵͳ�ļ���·��
		//��վ��id
		String field_id = request.getParameter("field_id");
		String is_id = request.getParameter("id");
		try {
			createDocContextEvent(fileName,field_id,is_id);
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		request.setAttribute("filename", fileName);
		return "/ipconfig/ipselect/download.jsp";
	}
	
	public String createdoc1(){
		String file = "/temp/IPliebiao.doc";// ���浽��Ŀ�ļ����µ�ָ���ļ���
		String fileName = ResourceCenter.getInstance().getSysPath() + file;// ��ȡϵͳ�ļ���·��
		//��վ��id
		String field_id = request.getParameter("id");
		try {
			createDocContextEvent1(fileName,field_id);
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		request.setAttribute("filename", fileName);
		return "/ipconfig/alltype/download.jsp";
	}
	
	
	
	
	public void createDocContextEvent(String file,String field_id,String is_id)throws DocumentException, IOException{
		//ȡ����Ӧ������
		DaoInterface field = new fieldDao();
		DaoInterface loopback = new loopbackstorageDao();
		DaoInterface pe = new pestorageDao();
		DaoInterface pe_ce = new pe_cestorageDao();
		DaoInterface bu = new bussinessDao();
		DaoInterface apply = new ip_selectDao();
		DaoInterface all = new alltypeDao();
		DaoInterface st = new stationtypeDao();
		
//		DaoInterface busst = new bussinessstorageDao();
		//��ѯ���ó�վ��  ����Ӧ��Loopback��pe,pe_ce ,bussiness
		
		List loop = loopback.loadAll(" where field_id = "+field_id);//��վ��Ӧ��Loopback��ַ
		List p = pe.loadAll(" where field_id = "+field_id);//��վ��Ӧ��pe��ַ
		List pc = pe_ce.loadAll(" where field_id =" +field_id);//��վ��Ӧ��pe_ce��ַ
		List bussiness = bu.loadAll(" where field_id = "+field_id);//��վ��Ӧ��ҵ��
		
		loopbackstorage loop_vo = null;
		pestorage p_vo = null;
		pe_cestorage pc_vo = null;
		ip_bussinesstype bus_vo = null;
		ip_select is_vo = (ip_select) apply.findByID(is_id);
		alltype a = (alltype) all.findByID(is_vo.getIds());
		field f = (field) field.findByID(field_id);
		
		stationtype sta = (stationtype) st.findByID(f.getRunning());
		
		
//		System.out.println("--------------------------id---------------------"+bussiness.size());
		
		int route_num = 1;
		int switch_num = 1;
		
		
		
         /*
			 * at = (alltype) all.findByID(alltype_id[1]); backbone_name2 =
			 * at.getBackbone_name(); }else{ String alltype_id = is.getIds(); at =
			 * (alltype) all.findByID(alltype_id); backbone_name1 =
			 * at.getBackbone_name(); }
			 */
		
		
		// ����ֽ�Ŵ�С
		Document document = new Document(PageSize.A4);
		// ����һ����д��(Writer)��document���������ͨ����д��(Writer)���Խ��ĵ�д�뵽������
		RtfWriter2.getInstance(document, new FileOutputStream(file));
		document.open();
		// ������������
		BaseFont bfChinese = BaseFont.createFont("Times-Roman", "", BaseFont.NOT_EMBEDDED);
		// ����������
		Font titleFont = new Font(bfChinese, 12, Font.BOLD);
		// ����������
		Font contextFont = new Font(bfChinese, 10, Font.NORMAL);
		Paragraph title = new Paragraph("��������������������", titleFont);
		// ���ñ����ʽ���뷽ʽ
		title.setAlignment(Element.ALIGN_CENTER);
		// title.setFont(titleFont);
		document.add(title);
		// ���� Table ���
		Font fontChinese = new Font(bfChinese, 6, Font.NORMAL, Color.black);
		Table aTable = new Table(6);
		
		int width[] = { 30, 50, 55, 40, 55, 40 };
		aTable.setWidths(width);
		aTable.setWidth(100); // ռҳ���� 90%
		aTable.setAlignment(Element.ALIGN_CENTER);// ������ʾ
		aTable.setAutoFillEmptyCells(true); // �Զ�����
		aTable.setBorderWidth(1); // �߿���
		aTable.setBorderColor(new Color(0, 125, 255)); // �߿���ɫ
		aTable.setPadding(2);// �ľ࣬��Ч����֪��ʲô��˼��
		aTable.setSpacing(0);// ����Ԫ��֮��ļ��
		aTable.setBorder(2);// �߿�
		aTable.endHeaders();

//		cell0.setBackgroundColor(Color.LIGHT_GRAY);
		Cell cell1 = new Cell("��վ��");
		Cell cell2 = new Cell(f.getName());
		Cell cell3 = new Cell("��Ӫ״̬");
		Cell cell4 = new Cell(sta.getName());
		Cell cell5 = new Cell("����Ǹɵ�");
		Cell cell6 = new Cell(a.getBackbone_name());
		cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell4.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell5.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell6.setVerticalAlignment(Element.ALIGN_MIDDLE);

		cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell6.setHorizontalAlignment(Element.ALIGN_CENTER);

		aTable.addCell(cell1);
		aTable.addCell(cell2);
		aTable.addCell(cell3);
		aTable.addCell(cell4);
		aTable.addCell(cell5);
		aTable.addCell(cell6);
		
		
		Font fontChinese1 = new Font(bfChinese, 4, Font.NORMAL, Color.black);
		Table aTable1 = new Table(4);
		
		int width1[] = { 30, 50, 30, 50};
		aTable1.setWidths(width1);
		aTable1.setWidth(100); // ռҳ���� 90%
		aTable1.setAlignment(Element.ALIGN_CENTER);// ������ʾ
		aTable1.setAutoFillEmptyCells(true); // �Զ�����
		aTable1.setBorderWidth(1); // �߿���
		aTable1.setBorderColor(new Color(0, 125, 255)); // �߿���ɫ
		aTable1.setPadding(2);// �ľ࣬��Ч����֪��ʲô��˼��
		aTable1.setSpacing(0);// ����Ԫ��֮��ļ��
		aTable1.setBorder(2);// �߿�
		aTable1.endHeaders();

//		cell0.setBackgroundColor(Color.LIGHT_GRAY);
		Cell cell10 = new Cell();
		Cell cell7 = new Cell("·����");
		Cell cell8 = new Cell("������(һ)");
		Cell cell9 = new Cell("������(��)");
		Cell cell11 = new Cell("����");
		Cell cell12 = new Cell(is_vo.getRFactory());
		Cell cell13 = new Cell(is_vo.getS1Factory());
		Cell cell14 = new Cell(is_vo.getS2Factory());
		Cell cell15 = new Cell("�ͺ�");
		Cell cell16 = new Cell(is_vo.getRouteType());
		Cell cell17 = new Cell(is_vo.getS1Type());
		Cell cell18 = new Cell(is_vo.getS2Type());
		Cell cell19 = new Cell("�豸����");
		Cell cell110 = new Cell(is_vo.getRouteName());
		Cell cell111 = new Cell(is_vo.getS1Name());
		Cell cell112 = new Cell(is_vo.getS2Name());
		Cell cell113 = new Cell("�������������豸");
		cell113.setColspan(2);
		Cell cell114 = new Cell(is_vo.getDeviceType()+is_vo.getDeviceCount());
		cell114.setColspan(2);
		Cell cell115 = new Cell("�����������");
	    cell115.setColspan(2);
	    Cell cell116=null;
	    if(is_vo.getHasCabinet().endsWith("0"))
	    {
	    	 cell116 = new Cell("��");
	    	cell116.setColspan(2);
	    }else{
	    	 cell116 = new Cell("��");
	    	cell116.setColspan(2);
	    }
		Cell cell117 = new Cell("���簲װ�ص�");
		cell117.setColspan(2);
		Cell cell118 = new Cell(is_vo.getLocation());
		cell118.setColspan(2);
		Cell cell119 = new Cell("ʩ�����Ե�λ");
		cell119.setColspan(2);
		Cell cell120 = new Cell(is_vo.getDebugUnit());
		cell120.setColspan(2);
		cell11.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell12.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell13.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell14.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell15.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell16.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell17.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell18.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell19.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell110.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell111.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell112.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell113.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell114.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell115.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell116.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell117.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell118.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell119.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell120.setVerticalAlignment(Element.ALIGN_MIDDLE);
		

		cell11.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell12.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell13.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell14.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell15.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell16.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell17.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell18.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell19.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell110.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell111.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell112.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell113.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell114.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell115.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell116.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell117.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell118.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell119.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell120.setHorizontalAlignment(Element.ALIGN_CENTER);

		
        cell17.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell17.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell18.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell18.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell19.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell19.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell110.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell110.setHorizontalAlignment(Element.ALIGN_CENTER);
		
		aTable1.addCell(cell10);
		aTable1.addCell(cell7);
		aTable1.addCell(cell8);
		aTable1.addCell(cell9);
		
		aTable1.addCell(cell11);
		aTable1.addCell(cell12);
		aTable1.addCell(cell13);
		aTable1.addCell(cell14);
		aTable1.addCell(cell15);
		aTable1.addCell(cell16);
		aTable1.addCell(cell17);
		aTable1.addCell(cell18);
		aTable1.addCell(cell19);
		aTable1.addCell(cell110);
		aTable1.addCell(cell111);
		aTable1.addCell(cell112);
		aTable1.addCell(cell113);
		aTable1.addCell(cell114);
		aTable1.addCell(cell115);
		aTable1.addCell(cell116);
		aTable1.addCell(cell117);
		aTable1.addCell(cell118);
		aTable1.addCell(cell119);
		aTable1.addCell(cell120);

		
//		Font fontChinese2 = new Font(bfChinese, 4, Font.NORMAL, Color.black);
//		Table aTable2 = new Table(4);
//		
//		int width2[] = { 100, 100, 100, 100};
//		aTable1.setWidths(width2);
//		aTable1.setWidth(100); // ռҳ���� 90%
//		aTable1.setAlignment(Element.ALIGN_CENTER);// ������ʾ
//		aTable1.setAutoFillEmptyCells(true); // �Զ�����
//		aTable1.setBorderWidth(1); // �߿���
//		aTable1.setBorderColor(new Color(0, 125, 255)); // �߿���ɫ
//		aTable1.setPadding(2);// �ľ࣬��Ч����֪��ʲô��˼��
//		aTable1.setSpacing(0);// ����Ԫ��֮��ļ��
//		aTable1.setBorder(2);// �߿�
//		aTable1.endHeaders();

//		cell0.setBackgroundColor(Color.LIGHT_GRAY);
		loop_vo = (loopbackstorage) loop.get(0); 
		p_vo = (pestorage) p.get(0);
		pc_vo = (pe_cestorage) pc.get(0);
		
		
		Cell cell21 = new Cell("��վLoopback��ַ");
		cell21.setColspan(2);
		Cell cell22 = new Cell(loop_vo.getLoopback());
		cell22.setColspan(2);
		Cell cell23 = new Cell("PE_PE������ַ");
		cell23.setColspan(2);
		cell23.setRowspan(2);
		Cell cell24 = new Cell(a.getBackbone_name()+"1 :" +p_vo.getBackbone1().split("\\.")[0]+"."+p_vo.getBackbone1().split("\\.")[1]+"."+p_vo.getBackbone1().split("\\.")[2]+"."+(Integer.parseInt(p_vo.getBackbone1().split("\\.")[3])+1) +" ---->"+  f.getName()+" :" + p_vo.getBackbone1().split("\\.")[0]+"."+p_vo.getBackbone1().split("\\.")[1]+"."+p_vo.getBackbone1().split("\\.")[2]+"."+(Integer.parseInt(p_vo.getBackbone1().split("\\.")[3])+2));
		cell24.setColspan(2);
		Cell cell25 = new Cell(a.getBackbone_name()+"2 :" +p_vo.getBackbone2().split("\\.")[0]+"."+p_vo.getBackbone1().split("\\.")[1]+"."+p_vo.getBackbone2().split("\\.")[2]+"."+(Integer.parseInt(p_vo.getBackbone2().split("\\.")[3])+1) +" ---->"+  f.getName()+" :" + p_vo.getBackbone2().split("\\.")[0]+"."+p_vo.getBackbone2().split("\\.")[1]+"."+p_vo.getBackbone2().split("\\.")[2]+"."+(Integer.parseInt(p_vo.getBackbone2().split("\\.")[3])+2));
		cell25.setColspan(2);
		Cell cell26 = new Cell("PE_CE������ַ");
		cell26.setColspan(2);
		cell26.setRowspan(2);
		Cell cell27 = new Cell(f.getName()+" :" +pc_vo.getS1().split("\\.")[0]+"."+pc_vo.getS1().split("\\.")[1]+"."+pc_vo.getS1().split("\\.")[2]+"."+(Integer.parseInt(pc_vo.getS1().split("\\.")[3])+1) +" ----> " +" S1 :"+ pc_vo.getS1().split("\\.")[0]+"."+pc_vo.getS1().split("\\.")[1]+"."+pc_vo.getS1().split("\\.")[2]+"."+(Integer.parseInt(pc_vo.getS1().split("\\.")[3])+2));
		cell27.setColspan(2);
		Cell cell28 = new Cell(f.getName()+" :" +pc_vo.getS2().split("\\.")[0]+"."+pc_vo.getS2().split("\\.")[1]+"."+pc_vo.getS2().split("\\.")[2]+"."+(Integer.parseInt(pc_vo.getS2().split("\\.")[3])+1) +" ----> " +" S2 :"+ pc_vo.getS1().split("\\.")[0]+"."+pc_vo.getS1().split("\\.")[1]+"."+pc_vo.getS2().split("\\.")[2]+"."+(Integer.parseInt(pc_vo.getS2().split("\\.")[3])+2));
		cell28.setColspan(2);
		
		
		cell21.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell22.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell23.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell24.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell25.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell26.setVerticalAlignment(Element.ALIGN_MIDDLE);
		
		cell21.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell22.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell23.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell24.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell25.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell26.setHorizontalAlignment(Element.ALIGN_CENTER);
		
		
		aTable1.addCell(cell21);
		aTable1.addCell(cell22);
		aTable1.addCell(cell23);
		aTable1.addCell(cell24);
		aTable1.addCell(cell25);
		aTable1.addCell(cell26);
		cell27.setVerticalAlignment(Element.ALIGN_MIDDLE);
	    cell28.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell27.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell28.setHorizontalAlignment(Element.ALIGN_CENTER);
			
		aTable1.addCell(cell27);
		aTable1.addCell(cell28);

		
		
		Cell cell223 = new Cell("�Ǹɵ�˿ں�1");
		cell223.setColspan(2);
		Cell cell224 = new Cell();
		cell224.setColspan(2);
//		Cell cell225 = new Cell();
//		Cell cell226 = new Cell();
		Cell cell227 = new Cell("�Ǹɵ�˿ں�2");
		cell227.setColspan(2);
		Cell cell228 = new Cell();
		cell228.setColspan(2);
//		Cell cell229 = new Cell();
//		Cell cell230 = new Cell();
		
		cell223.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell224.setVerticalAlignment(Element.ALIGN_MIDDLE);
//		cell225.setVerticalAlignment(Element.ALIGN_MIDDLE);
//		cell226.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell227.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell228.setVerticalAlignment(Element.ALIGN_MIDDLE);
//		cell229.setVerticalAlignment(Element.ALIGN_MIDDLE);
//		cell230.setVerticalAlignment(Element.ALIGN_MIDDLE);
		

		cell223.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell224.setHorizontalAlignment(Element.ALIGN_CENTER);
//		cell225.setHorizontalAlignment(Element.ALIGN_CENTER);
//		cell226.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell227.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell228.setHorizontalAlignment(Element.ALIGN_CENTER);
//		cell229.setHorizontalAlignment(Element.ALIGN_CENTER);
//		cell230.setHorizontalAlignment(Element.ALIGN_CENTER);
		
		
		
		aTable1.addCell(cell223);
		aTable1.addCell(cell224);
//		aTable1.addCell(cell225);
//		aTable1.addCell(cell226);
		aTable1.addCell(cell227);
		aTable1.addCell(cell228);
//		aTable1.addCell(cell229);
//		aTable1.addCell(cell230);
		
		
		Cell cell231 = new Cell("MPLS VPN RD/RT");
		cell231.setColspan(2);
		Cell cell232 = new Cell("300:1");
		cell232.setColspan(2);
		
		Cell cell233 = new Cell("VPNӦ�õ�ַ");
		cell233.setColspan(4);
		
		cell231.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell232.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell233.setVerticalAlignment(Element.ALIGN_MIDDLE);
		

		cell231.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell232.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell233.setHorizontalAlignment(Element.ALIGN_CENTER);
		
		aTable1.addCell(cell231);
		aTable1.addCell(cell232);
		aTable1.addCell(cell233);
		
		Cell cell234 = null;
		Cell cell235 = null;
		Cell cell40 = null;
		Cell cell41 = null;
		if(bussiness.size()!=0 && bussiness != null){
			for(int i = 0;i<bussiness.size();i++)
		{
				bus_vo = (ip_bussinesstype) bussiness.get(i);
			    cell234 = new Cell(bus_vo.getBusname());
				cell235 = new Cell(bus_vo.getBuskind());
				cell40 = new Cell(bus_vo.getVlan());
				cell41 = new Cell(bus_vo.getVlan_ip());
				
				cell234.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell235.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell234.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell235.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell40.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell40.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell41.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell41.setHorizontalAlignment(Element.ALIGN_CENTER);
				
				aTable1.addCell(cell234);
				aTable1.addCell(cell235);
				aTable1.addCell(cell40);
				aTable1.addCell(cell41);
				
			}
		}
		
		
		Cell cell236 = new Cell("���뵥λ������ǩ��");
		cell236.setColspan(1);
		cell236.setRowspan(2);
		Cell cell237 = new Cell("��      ��       ��");
		cell237.setColspan(3);
		cell237.setRowspan(2);
		
		Cell cell238 = new Cell("ʡ��������ǩ��");
		cell238.setColspan(1);
		cell238.setRowspan(2);
		Cell cell239 = new Cell("��      ��       ��");
		cell239.setColspan(3);
		cell239.setRowspan(2);
		
		cell236.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell237.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell238.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell239.setVerticalAlignment(Element.ALIGN_MIDDLE);

		cell236.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell237.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell238.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell239.setHorizontalAlignment(Element.ALIGN_RIGHT);
		
		aTable1.addCell(cell236);
		aTable1.addCell(cell237);
		aTable1.addCell(cell238);
		aTable1.addCell(cell239);
		
		
		
		
		
		document.add(aTable);
		document.add(aTable1);
//		document.add(aTable2);
		document.add(new Paragraph("\n"));
		document.close();
	}
	
	
	
	public void createDocContextEvent1(String file,String id)throws DocumentException, IOException{
		//ȡ����Ӧ������
		DaoInterface loopback = new loopbackstorageDao();
		DaoInterface pe = new pestorageDao();
		DaoInterface pe_ce = new pe_cestorageDao();
		DaoInterface bu = new bussinessstorageDao();
		DaoInterface backbone = new backbonestorageDao();
		//��ѯ���ó�վ��  ����Ӧ��Loopback��pe,pe_ce ,bussiness
		
		
		
		List loop = loopback.loadAll();//��վ��Ӧ��Loopback��ַ
		List p = pe.loadAll();//��վ��Ӧ��pe��ַ
		List pc = pe_ce.loadAll();//��վ��Ӧ��pe_ce��ַ
		List bussiness = bu.loadAll();//��վ��Ӧ��ҵ��
		List bb = backbone.loadAll();
		
		loopbackstorage loop_vo = null;
		pestorage p_vo = null;
		pe_cestorage pc_vo = null;
		bussinessstorage bus_vo = null;
		backbonestorage backbone_vo = null;
		
		// ����ֽ�Ŵ�С
		Document document = new Document(PageSize.A4);
		// ����һ����д��(Writer)��document���������ͨ����д��(Writer)���Խ��ĵ�д�뵽������
		RtfWriter2.getInstance(document, new FileOutputStream(file));
		document.open();
		// ������������
		BaseFont bfChinese = BaseFont.createFont("Times-Roman", "", BaseFont.NOT_EMBEDDED);
		// ����������
		Font titleFont = new Font(bfChinese, 12, Font.BOLD);
		// ����������
		Font contextFont = new Font(bfChinese, 10, Font.NORMAL);
		Paragraph title = new Paragraph("��������������������", titleFont);
		// ���ñ����ʽ���뷽ʽ
		title.setAlignment(Element.ALIGN_CENTER);
		// title.setFont(titleFont);
		document.add(title);
		// ���� Table ���
		Font fontChinese = new Font(bfChinese, 3, Font.NORMAL, Color.black);
		Table aTable = new Table(3);
		
		int width[] = { 30, 50, 55,  };
		aTable.setWidths(width);
		aTable.setWidth(100); // ռҳ���� 90%
		aTable.setAlignment(Element.ALIGN_CENTER);// ������ʾ
		aTable.setAutoFillEmptyCells(true); // �Զ�����
		aTable.setBorderWidth(1); // �߿���
		aTable.setBorderColor(new Color(0, 125, 255)); // �߿���ɫ
		aTable.setPadding(2);// �ľ࣬��Ч����֪��ʲô��˼��
		aTable.setSpacing(0);// ����Ԫ��֮��ļ��
		aTable.setBorder(2);// �߿�
		aTable.endHeaders();

		
		
//		cell0.setBackgroundColor(Color.LIGHT_GRAY);
		Cell cell1 = new Cell("���");
		Cell cell2 = null;
		if(id.equals("0")){
			 cell2 = new Cell("�Ǹɵ�IP");
		}else if(id.equals("1")){
			 cell2 = new Cell("Loopback");
		}else if(id.equals("2")){
			 cell2 = new Cell("pe������ַ");
		}else if(id.equals("3")){
			 cell2 = new Cell("pe_ce������ַ");
		}else if(id.equals("4")){
			 cell2 = new Cell("ҵ���ַ");
		}
		Cell cell3 = new Cell("�Ƿ����");
		
		Cell cell4 = null;
		Cell cell5 = null;
		Cell cell6 = null;
		if(id.equals("0")){
			for(int i = 0;i<bb.size();i++ ){
				backbone_vo = (backbonestorage) bb.get(i);
				cell4 = new Cell(i+1+"");
				if(backbone_vo.getType()== 1){
					cell5 = new Cell(backbone_vo.getBackbone());
					cell5.setBackgroundColor(Color.LIGHT_GRAY);
					cell5.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
					
					cell6 = new Cell("������");
					cell6.setBackgroundColor(Color.LIGHT_GRAY);
					cell6.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
				}else{
					cell5 = new Cell(backbone_vo.getBackbone());
					cell5.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
					
					cell6 = new Cell("����");
					cell6.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
				}
				aTable.addCell(cell4);
				aTable.addCell(cell5);
				aTable.addCell(cell6);
				
			}
		}else if(id.equals("1")){
			for(int i = 0;i<loop.size();i++ ){
				loop_vo = (loopbackstorage) loop.get(i);
				cell4 = new Cell(i+1+"");
				if(loop_vo.getType()== 1){
					cell5 = new Cell(loop_vo.getLoopback());
					cell5.setBackgroundColor(Color.LIGHT_GRAY);
					cell5.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
					
					cell6 = new Cell("������");
					cell6.setBackgroundColor(Color.LIGHT_GRAY);
					cell6.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
				}else{
					cell5 = new Cell(loop_vo.getLoopback());
					cell5.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
					
					cell6 = new Cell("����");
					cell6.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
				}
				
				aTable.addCell(cell4);
				aTable.addCell(cell5);
				aTable.addCell(cell6);
			}
				
		}else if(id.equals("2")){
			for(int i = 0;i<p.size();i++ ){
				p_vo = (pestorage) p.get(i);
				cell4 = new Cell(i+1+"");
				if(1==1){
					cell5 = new Cell();
					cell5.setBackgroundColor(Color.LIGHT_GRAY);
					cell5.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
					
					cell6 = new Cell("������");
					cell6.setBackgroundColor(Color.LIGHT_GRAY);
					cell6.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
				}else{
					cell5 = new Cell();
					cell5.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
					
					cell6 = new Cell("����");
					cell6.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
				}
				aTable.addCell(cell4);
				aTable.addCell(cell5);
				aTable.addCell(cell6);
			}
		}else if(id.equals("3")){
			for(int i = 0;i<pc.size();i++ ){
				pc_vo = (pe_cestorage) pc.get(i);
				cell4 = new Cell(i+1+"");
				if(1==1){
					cell5 = new Cell();
					cell5.setBackgroundColor(Color.LIGHT_GRAY);
					cell5.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
					
					cell6 = new Cell("������");
					cell6.setBackgroundColor(Color.LIGHT_GRAY);
					cell6.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
				}else{
					cell5 = new Cell();
					cell5.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
					
					cell6 = new Cell("����");
					cell6.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
				}
				aTable.addCell(cell4);
				aTable.addCell(cell5);
				aTable.addCell(cell6);
			}
		}else if(id.equals("4")){
			for(int i = 0;i<bussiness.size();i++ ){
				bus_vo = (bussinessstorage) bussiness.get(i);
				cell4 = new Cell(i+1+"");
				if(bus_vo.getType()== 1){
					cell5 = new Cell(bus_vo.getBussiness());
					cell5.setBackgroundColor(Color.LIGHT_GRAY);
					cell5.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
					
					cell6 = new Cell("������");
					cell6.setBackgroundColor(Color.LIGHT_GRAY);
					cell6.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
				}else{
					cell5 = new Cell(bus_vo.getBussiness());
					cell5.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
					
					cell6 = new Cell("����");
					cell6.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
				}
				aTable.addCell(cell4);
				aTable.addCell(cell5);
				aTable.addCell(cell6);
		}
		}
		
		document.add(aTable);
		document.add(new Paragraph("\n"));
		document.close();
	}



	public void createDocContextEventLoopback(String file)throws DocumentException, IOException{
		//ȡ����Ӧ������
		DaoInterface loopback = new loopbackstorageDao();
		DaoInterface pe = new pestorageDao();
		DaoInterface pe_ce = new pe_cestorageDao();
		DaoInterface bus = new bussinessDao();
		DaoInterface all = new alltypeDao();
		//��ѯ���ó�վ��  ����Ӧ��Loopback��pe,pe_ce ,bussiness
		
		stationtypeDao st = new stationtypeDao();
        List station = st.loadAll();
        stationtype sta = null;
        Map m = new HashMap(); 
        for(int x=0;x<station.size();x++){
             sta = (stationtype)station.get(x);
             m.put(sta.getId()+"",sta.getName());
        }
		
		List backbone = all.loadAll();
		
		loopbackstorage loop_vo = null;
		pestorage p_vo = null;
		pe_cestorage pc_vo = null;
		
		// ����ֽ�Ŵ�С
		Document document = new Document(PageSize.A4);
		// ����һ����д��(Writer)��document���������ͨ����д��(Writer)���Խ��ĵ�д�뵽������
		RtfWriter2.getInstance(document, new FileOutputStream(file));
		document.open();
		// ������������
		BaseFont bfChinese = BaseFont.createFont("Times-Roman", "", BaseFont.NOT_EMBEDDED);
		// ����������
		Font titleFont = new Font(bfChinese, 12, Font.BOLD);
		// ����������
		Font contextFont = new Font(bfChinese, 10, Font.NORMAL);
		Paragraph title = new Paragraph("LoopBack��ַ�����", titleFont);
		// ���ñ����ʽ���뷽ʽ
		title.setAlignment(Element.ALIGN_CENTER);
		// title.setFont(titleFont);
		document.add(title);
		// ���� Table ���
		Font fontChinese = new Font(bfChinese, 6, Font.NORMAL, Color.black);
		Table aTable = new Table(6);
		
		int width[] = { 30, 40, 55,50,40,55  };
		aTable.setWidths(width);
		aTable.setWidth(100); // ռҳ���� 90%
		aTable.setAlignment(Element.ALIGN_CENTER);// ������ʾ
		aTable.setAutoFillEmptyCells(true); // �Զ�����
		aTable.setBorderWidth(1); // �߿���
		aTable.setBorderColor(new Color(0, 125, 255)); // �߿���ɫ
		aTable.setPadding(2);// �ľ࣬��Ч����֪��ʲô��˼��
		aTable.setSpacing(0);// ����Ԫ��֮��ļ��
		aTable.setBorder(2);// �߿�
		aTable.endHeaders();

		
		
//		cell0.setBackgroundColor(Color.LIGHT_GRAY);
		Cell cell1 = new Cell("���");
		Cell cell2 = new Cell("�ڵ�����");
		Cell cell3 = new Cell("Loopback��ַ");
		
		Cell cell4 = new Cell("������վ");
		Cell cell5 = new Cell("����״̬");
		Cell cell6 = new Cell("��վLoopBack��ַ");
		
		cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
		
		aTable.addCell(cell1);
		aTable.addCell(cell2);
		aTable.addCell(cell3);
		aTable.addCell(cell4);
		aTable.addCell(cell5);
		aTable.addCell(cell6);
		
		alltype al_vo = null;
		if(backbone.size()!=0 && backbone!=null){
			Cell cell20 = null;
			Cell cell8 = null;
			Cell cell9 = null;
			
			Cell cell10 = null;
			Cell cell11 = null;
			Cell cell12 = null;
		for(int i=0;i<backbone.size();i++){
			al_vo = (alltype)backbone.get(i);
			int backbone_id = al_vo.getId();
			fieldDao f = new fieldDao();
			List field = f.loadAll(" where backbone_id="+backbone_id);
			String x = (i+1)+"";
			
			
			
			cell20 = new Cell(x);
			cell20.setRowspan(field.size());
			cell20.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell20.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell8 = new Cell(al_vo.getBackbone_name());
			cell8.setRowspan(field.size());
			cell8.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell8.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell9 = new Cell(al_vo.getLoopback_begin().substring(0, al_vo.getLoopback_begin().lastIndexOf("."))+".1--"+al_vo.getLoopback_begin().substring(0, al_vo.getLoopback_begin().lastIndexOf("."))+".2");
			cell9.setRowspan(field.size());
			cell9.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell9.setHorizontalAlignment(Element.ALIGN_CENTER); 
			
			
			
			field f_vo = null;
			if(field.size()!=0 && field!=null){
				aTable.addCell(cell20);
				aTable.addCell(cell8);
				aTable.addCell(cell9);
				for(int j=0;j<field.size();j++)
				{
					f_vo = (field)field.get(j);
					String running_id = f_vo.getRunning();
					
					loopbackstorageDao loop =  new loopbackstorageDao();
					List l_loop = loop.loadAll(" where field_id="+f_vo.getId());
					loopbackstorage l_vo = (loopbackstorage) l_loop.get(0);
					String lk = l_vo.getLoopback();
					
					cell10 = new Cell(f_vo.getName());
					cell10.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell10.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell11 = new Cell(m.get(f_vo.getRunning())+"");
					cell11.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell11.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell12 = new Cell(lk);
					cell12.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell12.setHorizontalAlignment(Element.ALIGN_CENTER); 
					aTable.addCell(cell10);
					aTable.addCell(cell11);
					aTable.addCell(cell12);
				}
			}else{
				cell10 = new Cell();
				cell11 = new Cell();
				cell12 = new Cell();
				
				aTable.addCell(x);
				aTable.addCell(cell8);
				aTable.addCell(cell9);
				aTable.addCell(cell10);
				aTable.addCell(cell11);
				aTable.addCell(cell12);
			}
		  }
		}
		
		document.add(aTable);
		document.add(new Paragraph("\n"));
		document.close();
	}

	
	public void createDocContextEventPe(String file)throws DocumentException, IOException{
		//ȡ����Ӧ������
		DaoInterface loopback = new loopbackstorageDao();
		DaoInterface pe = new pestorageDao();
		DaoInterface pe_ce = new pe_cestorageDao();
		DaoInterface bus = new bussinessDao();
		DaoInterface all = new alltypeDao();
		//��ѯ���ó�վ��  ����Ӧ��Loopback��pe,pe_ce ,bussiness
		
		
		
		List backbone = all.loadAll();
		
		loopbackstorage loop_vo = null;
		pestorage p_vo = null;
		pe_cestorage pc_vo = null;
		
		// ����ֽ�Ŵ�С
		Document document = new Document(PageSize.A4);
		// ����һ����д��(Writer)��document���������ͨ����д��(Writer)���Խ��ĵ�д�뵽������
		RtfWriter2.getInstance(document, new FileOutputStream(file));
		document.open();
		// ������������
		BaseFont bfChinese = BaseFont.createFont("Times-Roman", "", BaseFont.NOT_EMBEDDED);
		// ����������
		Font titleFont = new Font(bfChinese, 12, Font.BOLD);
		// ����������
		Font contextFont = new Font(bfChinese, 10, Font.NORMAL);
		Paragraph title = new Paragraph("PE������ַ�����", titleFont);
		// ���ñ����ʽ���뷽ʽ
		title.setAlignment(Element.ALIGN_CENTER);
		// title.setFont(titleFont);
		document.add(title);
		// ���� Table ���
		Font fontChinese = new Font(bfChinese, 6, Font.NORMAL, Color.black);
		Table aTable = new Table(6);
		
		int width[] = { 30, 40, 55,50,40,55  };
		aTable.setWidths(width);
		aTable.setWidth(100); // ռҳ���� 90%
		aTable.setAlignment(Element.ALIGN_CENTER);// ������ʾ
		aTable.setAutoFillEmptyCells(true); // �Զ�����
		aTable.setBorderWidth(1); // �߿���
		aTable.setBorderColor(new Color(0, 125, 255)); // �߿���ɫ
		aTable.setPadding(2);// �ľ࣬��Ч����֪��ʲô��˼��
		aTable.setSpacing(0);// ����Ԫ��֮��ļ��
		aTable.setBorder(2);// �߿�
		aTable.endHeaders();

		
		
//		cell0.setBackgroundColor(Color.LIGHT_GRAY);
		Cell cell1 = new Cell("���");
		Cell cell2 = new Cell("�ڵ�����");
		Cell cell3 = new Cell("PE������ַ��");
		
		Cell cell4 = new Cell("���˽ڵ�");
		Cell cell5 = new Cell("�ڵ�һ");
		Cell cell6 = new Cell("�ڵ��");
		
		cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
		
		aTable.addCell(cell1);
		aTable.addCell(cell2);
		aTable.addCell(cell3);
		aTable.addCell(cell4);
		aTable.addCell(cell5);
		aTable.addCell(cell6);
		
		alltype al_vo = null;
		if(backbone.size()!=0 && backbone!=null){
			Cell cell20 = null;
			Cell cell8 = null;
			Cell cell9 = null;
			
			Cell cell10 = null;
			Cell cell11 = null;
			Cell cell12 = null;
		for(int i=0;i<backbone.size();i++){
			al_vo = (alltype)backbone.get(i);
			int backbone_id = al_vo.getId();
			fieldDao f = new fieldDao();
			List field = f.loadAll(" where backbone_id="+backbone_id);
			String x = (i+1)+"";
			
			
			
			cell20 = new Cell(x);
			cell20.setRowspan(field.size());
			cell20.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell20.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell8 = new Cell(al_vo.getBackbone_name());
			cell8.setRowspan(field.size());
			cell8.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell8.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell9 = new Cell(al_vo.getPe_begin()+"---"+al_vo.getPe_end());
			cell9.setRowspan(field.size());
			cell9.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell9.setHorizontalAlignment(Element.ALIGN_CENTER); 
			
			field f_vo = null;
			if(field.size()!=0 && field!=null){
				aTable.addCell(cell20);
				aTable.addCell(cell8);
				aTable.addCell(cell9);
				for(int j=0;j<field.size();j++)
				{
					f_vo = (field)field.get(j);
					String running_id = f_vo.getRunning();
					DaoInterface sta = new stationtypeDao();
					stationtype st = (stationtype) sta.findByID(running_id);
					String running = st.getName();
					
					pestorageDao p =  new pestorageDao();
					List pe1 = p.loadAll(" where field_id="+f_vo.getId());
					pestorage l_vo = (pestorage) pe1.get(0);
					
					cell10 = new Cell(f_vo.getName());
					cell10.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell10.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell11 = new Cell(l_vo.getBackbone1()+"/30");
					cell11.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell11.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell12 = new Cell(l_vo.getBackbone2()+"/30");
					cell12.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell12.setHorizontalAlignment(Element.ALIGN_CENTER); 
					aTable.addCell(cell10);
					aTable.addCell(cell11);
					aTable.addCell(cell12);
				}
			}else{
				cell10 = new Cell();
				cell11 = new Cell();
				cell12 = new Cell();
				
				aTable.addCell(x);
				aTable.addCell(cell8);
				aTable.addCell(cell9);
				aTable.addCell(cell10);
				aTable.addCell(cell11);
				aTable.addCell(cell12);
			}
		  }
		}
		
		document.add(aTable);
		document.add(new Paragraph("\n"));
		document.close();
	}
	
	
	public void createDocContextEventBussiness(String file,String backbone_id)throws DocumentException, IOException{
		//ȡ����Ӧ������
		DaoInterface loopback = new loopbackstorageDao();
		DaoInterface pe = new pestorageDao();
		DaoInterface pe_ce = new pe_cestorageDao();
		DaoInterface bus = new bussinessDao();
		DaoInterface all = new alltypeDao();
		//��ѯ���ó�վ��  ����Ӧ��Loopback��pe,pe_ce ,bussiness
		
		alltype bk = (alltype) all.findByID(backbone_id);
		
		
		loopbackstorage loop_vo = null;
		pestorage p_vo = null;
		pe_cestorage pc_vo = null;
		
		// ����ֽ�Ŵ�С
		Document document = new Document(PageSize.A4);
		// ����һ����д��(Writer)��document���������ͨ����д��(Writer)���Խ��ĵ�д�뵽������
		RtfWriter2.getInstance(document, new FileOutputStream(file));
		document.open();
		// ������������
		BaseFont bfChinese = BaseFont.createFont("Times-Roman", "", BaseFont.NOT_EMBEDDED);
		// ����������
		Font titleFont = new Font(bfChinese, 12, Font.BOLD);
		// ����������
		Font contextFont = new Font(bfChinese, 10, Font.NORMAL);
		Paragraph title = new Paragraph(bk.getBackbone_name()+"ҵ���ַ��ַ�����", titleFont);
		// ���ñ����ʽ���뷽ʽ
		title.setAlignment(Element.ALIGN_CENTER);
		// title.setFont(titleFont);
		document.add(title);
		// ���� Table ���
		Font fontChinese = new Font(bfChinese, 9, Font.NORMAL, Color.black);
		Table aTable = new Table(9);
		
		int width[] = { 30, 40, 55,40,30,55,50,50,60  };
		aTable.setWidths(width);
		aTable.setWidth(100); // ռҳ���� 90%
		aTable.setAlignment(Element.ALIGN_CENTER);// ������ʾ
		aTable.setAutoFillEmptyCells(true); // �Զ�����
		aTable.setBorderWidth(1); // �߿���
		aTable.setBorderColor(new Color(0, 125, 255)); // �߿���ɫ
		aTable.setPadding(2);// �ľ࣬��Ч����֪��ʲô��˼��
		aTable.setSpacing(0);// ����Ԫ��֮��ļ��
		aTable.setBorder(2);// �߿�
		aTable.endHeaders();

		
		
//		cell0.setBackgroundColor(Color.LIGHT_GRAY);
		Cell cell1 = new Cell("���");
		Cell cell2 = new Cell("�ڵ�����");
		Cell cell3 = new Cell("�ڵ�����");
		Cell cell4 = new Cell("ҵ������");
		Cell cell5 = new Cell("Vlan");
		Cell cell6 = new Cell("����");
		Cell cell7 = new Cell("����");
		Cell cell8 = new Cell("�������");
		Cell cell9 = new Cell("�豸�����ַ");
		
		cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell7.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell8.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell9.setHorizontalAlignment(Element.ALIGN_CENTER);
		
		aTable.addCell(cell1);
		aTable.addCell(cell2);
		aTable.addCell(cell3);
		aTable.addCell(cell4);
		aTable.addCell(cell5);
		aTable.addCell(cell6);
		aTable.addCell(cell7);
		aTable.addCell(cell8);
		aTable.addCell(cell9);
		
		field f_vo = null;
		if( bk != null){

			fieldDao f = new fieldDao();
			List list = f.loadAll(" where backbone_id = "+backbone_id);
			if(list.size()!=0 && list!=null){
				Cell cell10 = null;
				Cell cell11 = null;
				Cell cell12 = null;
				Cell cell13 = null;
				Cell cell14 = null;
				Cell cell15 = null;
				Cell cell16 = null;
				Cell cell17 = null;
				Cell cell18 = null;
				for(int i = 0;i<list.size();i++){
					f_vo = (field) list.get(i);
					String index = i+1+"";
					cell10 = new Cell(index);
					cell10.setRowspan(16);
					cell10.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell10.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell11 = new Cell(f_vo.getName());
					cell11.setRowspan(16);
					cell11.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell11.setHorizontalAlignment(Element.ALIGN_CENTER);
					
					aTable.addCell(cell10);
					aTable.addCell(cell11);
					
					bussinessDao bus1 = new bussinessDao();
					List bussiness = bus1.loadAll(" where field_id="+f_vo.getId());
					ip_bussinesstype bus_vo = null;
					
					
					for(int j = 0;j<16;j++){
						bus_vo = (ip_bussinesstype) bussiness.get(j);
						
						cell12 = new Cell(bus_vo.getBuskind());
						cell12.setVerticalAlignment(Element.ALIGN_MIDDLE);
						cell12.setHorizontalAlignment(Element.ALIGN_CENTER);
						
						cell13 = new Cell(bus_vo.getBusname());
						cell13.setVerticalAlignment(Element.ALIGN_MIDDLE);
						cell13.setHorizontalAlignment(Element.ALIGN_CENTER);
						
						cell14 = new Cell(bus_vo.getVlan());
						cell14.setVerticalAlignment(Element.ALIGN_MIDDLE);
						cell14.setHorizontalAlignment(Element.ALIGN_CENTER);
						
						cell15 = new Cell(bus_vo.getSegment());
						cell15.setVerticalAlignment(Element.ALIGN_MIDDLE);
						cell15.setHorizontalAlignment(Element.ALIGN_CENTER);
					
						cell16 = new Cell(bus_vo.getGateway());
						cell16.setVerticalAlignment(Element.ALIGN_MIDDLE);
						cell16.setHorizontalAlignment(Element.ALIGN_CENTER);
						
						cell17 = new Cell(bus_vo.getEncryption());
						cell17.setVerticalAlignment(Element.ALIGN_MIDDLE);
						cell17.setHorizontalAlignment(Element.ALIGN_CENTER);
						
						cell18 = new Cell(bus_vo.getIp_use());
						cell18.setVerticalAlignment(Element.ALIGN_MIDDLE);
						cell18.setHorizontalAlignment(Element.ALIGN_CENTER);
						
						aTable.addCell(cell12);
						aTable.addCell(cell13);
						aTable.addCell(cell14);
						aTable.addCell(cell15);
						aTable.addCell(cell16);
						aTable.addCell(cell17);
						aTable.addCell(cell18);
					}
				}
			}
//			
//			cell20 = new Cell(x);
//			cell20.setRowspan(field.size());
//			cell20.setVerticalAlignment(Element.ALIGN_MIDDLE);
//			cell20.setHorizontalAlignment(Element.ALIGN_CENTER);
//			cell8 = new Cell(al_vo.getBackbone_name());
//			cell8.setRowspan(field.size());
//			cell8.setVerticalAlignment(Element.ALIGN_MIDDLE);
//			cell8.setHorizontalAlignment(Element.ALIGN_CENTER);
//			cell9 = new Cell(al_vo.getPe_ce_begin()+"---"+al_vo.getPe_ce_end());
//			cell9.setRowspan(field.size());
//			cell9.setVerticalAlignment(Element.ALIGN_MIDDLE);
//			cell9.setHorizontalAlignment(Element.ALIGN_CENTER); 
//			
//			field f_vo = null;
//			if(field.size()!=0 && field!=null){
//				aTable.addCell(cell20);
//				aTable.addCell(cell8);
//				aTable.addCell(cell9);
//				for(int j=0;j<field.size();j++)
//				{
//					f_vo = (field)field.get(j);
//					String running_id = f_vo.getRunning();
//					DaoInterface sta = new stationtypeDao();
//					stationtype st = (stationtype) sta.findByID(running_id);
//					String running = st.getName();
//					
//					pe_cestorageDao p =  new pe_cestorageDao();
//					List pe1 = p.loadAll(" where field_id="+f_vo.getId());
//					pe_cestorage l_vo = (pe_cestorage) pe1.get(0);
//					
//					cell10 = new Cell(f_vo.getName());
//					cell10.setVerticalAlignment(Element.ALIGN_MIDDLE);
//					cell10.setHorizontalAlignment(Element.ALIGN_CENTER);
//					cell11 = new Cell(l_vo.getS1()+"/30");
//					cell11.setVerticalAlignment(Element.ALIGN_MIDDLE);
//					cell11.setHorizontalAlignment(Element.ALIGN_CENTER);
//					cell12 = new Cell(l_vo.getS2()+"/30");
//					cell12.setVerticalAlignment(Element.ALIGN_MIDDLE);
//					cell12.setHorizontalAlignment(Element.ALIGN_CENTER); 
//					aTable.addCell(cell10);
//					aTable.addCell(cell11);
//					aTable.addCell(cell12);
//				}
//			}else{
//				cell10 = new Cell();
//				cell11 = new Cell();
//				cell12 = new Cell();
//				
//				aTable.addCell(x);
//				aTable.addCell(cell8);
//				aTable.addCell(cell9);
//				aTable.addCell(cell10);
//				aTable.addCell(cell11);
//				aTable.addCell(cell12);
//			}
//		  }
//		}
		
		document.add(aTable);
		document.add(new Paragraph("\n"));
		document.close();
	}
}
	
	public void createDocContextEventPe_ce(String file)throws DocumentException, IOException{
		//ȡ����Ӧ������
		DaoInterface loopback = new loopbackstorageDao();
		DaoInterface pe = new pestorageDao();
		DaoInterface pe_ce = new pe_cestorageDao();
		DaoInterface bus = new bussinessDao();
		DaoInterface all = new alltypeDao();
		//��ѯ���ó�վ��  ����Ӧ��Loopback��pe,pe_ce ,bussiness
		
		
		
		List backbone = all.loadAll();
		
		loopbackstorage loop_vo = null;
		pestorage p_vo = null;
		pe_cestorage pc_vo = null;
		
		// ����ֽ�Ŵ�С
		Document document = new Document(PageSize.A4);
		// ����һ����д��(Writer)��document���������ͨ����д��(Writer)���Խ��ĵ�д�뵽������
		RtfWriter2.getInstance(document, new FileOutputStream(file));
		document.open();
		// ������������
		BaseFont bfChinese = BaseFont.createFont("Times-Roman", "", BaseFont.NOT_EMBEDDED);
		// ����������
		Font titleFont = new Font(bfChinese, 12, Font.BOLD);
		// ����������
		Font contextFont = new Font(bfChinese, 10, Font.NORMAL);
		Paragraph title = new Paragraph("PE_CE������ַ�����", titleFont);
		// ���ñ����ʽ���뷽ʽ
		title.setAlignment(Element.ALIGN_CENTER);
		// title.setFont(titleFont);
		document.add(title);
		// ���� Table ���
		Font fontChinese = new Font(bfChinese, 6, Font.NORMAL, Color.black);
		Table aTable = new Table(6);
		
		int width[] = { 30, 40, 55,50,40,55  };
		aTable.setWidths(width);
		aTable.setWidth(100); // ռҳ���� 90%
		aTable.setAlignment(Element.ALIGN_CENTER);// ������ʾ
		aTable.setAutoFillEmptyCells(true); // �Զ�����
		aTable.setBorderWidth(1); // �߿���
		aTable.setBorderColor(new Color(0, 125, 255)); // �߿���ɫ
		aTable.setPadding(2);// �ľ࣬��Ч����֪��ʲô��˼��
		aTable.setSpacing(0);// ����Ԫ��֮��ļ��
		aTable.setBorder(2);// �߿�
		aTable.endHeaders();

		
		
//		cell0.setBackgroundColor(Color.LIGHT_GRAY);
		Cell cell1 = new Cell("���");
		Cell cell2 = new Cell("�ڵ�����");
		Cell cell3 = new Cell("PE_CE������ַ��");
		
		Cell cell4 = new Cell("���˽ڵ�");
		Cell cell5 = new Cell("������һ(S1)");
		Cell cell6 = new Cell("��������(S2)");
		
		cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
		
		aTable.addCell(cell1);
		aTable.addCell(cell2);
		aTable.addCell(cell3);
		aTable.addCell(cell4);
		aTable.addCell(cell5);
		aTable.addCell(cell6);
		
		alltype al_vo = null;
		if(backbone.size()!=0 && backbone!=null){
			Cell cell20 = null;
			Cell cell8 = null;
			Cell cell9 = null;
			
			Cell cell10 = null;
			Cell cell11 = null;
			Cell cell12 = null;
		for(int i=0;i<backbone.size();i++){
			al_vo = (alltype)backbone.get(i);
			int backbone_id = al_vo.getId();
			fieldDao f = new fieldDao();
			List field = f.loadAll(" where backbone_id="+backbone_id);
			String x = (i+1)+"";
			
			
			
			cell20 = new Cell(x);
			cell20.setRowspan(field.size());
			cell20.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell20.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell8 = new Cell(al_vo.getBackbone_name());
			cell8.setRowspan(field.size());
			cell8.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell8.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell9 = new Cell(al_vo.getPe_ce_begin()+"---"+al_vo.getPe_ce_end());
			cell9.setRowspan(field.size());
			cell9.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell9.setHorizontalAlignment(Element.ALIGN_CENTER); 
			
			field f_vo = null;
			if(field.size()!=0 && field!=null){
				aTable.addCell(cell20);
				aTable.addCell(cell8);
				aTable.addCell(cell9);
				for(int j=0;j<field.size();j++)
				{
					f_vo = (field)field.get(j);
					String running_id = f_vo.getRunning();
					DaoInterface sta = new stationtypeDao();
					stationtype st = (stationtype) sta.findByID(running_id);
					String running = st.getName();
					
					pe_cestorageDao p =  new pe_cestorageDao();
					List pe1 = p.loadAll(" where field_id="+f_vo.getId());
					pe_cestorage l_vo = (pe_cestorage) pe1.get(0);
					
					cell10 = new Cell(f_vo.getName());
					cell10.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell10.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell11 = new Cell(l_vo.getS1()+"/30");
					cell11.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell11.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell12 = new Cell(l_vo.getS2()+"/30");
					cell12.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell12.setHorizontalAlignment(Element.ALIGN_CENTER); 
					aTable.addCell(cell10);
					aTable.addCell(cell11);
					aTable.addCell(cell12);
				}
			}else{
				cell10 = new Cell();
				cell11 = new Cell();
				cell12 = new Cell();
				
				aTable.addCell(x);
				aTable.addCell(cell8);
				aTable.addCell(cell9);
				aTable.addCell(cell10);
				aTable.addCell(cell11);
				aTable.addCell(cell12);
			}
		  }
		}
		
		document.add(aTable);
		document.add(new Paragraph("\n"));
		document.close();
	}
}
