/**
 * <p>Description:DiscoverManager</p>
 * <p>Company: dhcc.com</p>
 * @author afunms
 * @project afunms
 * @date 2006-08-12
 */

package com.afunms.cabinet.manage;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import com.afunms.cabinet.dao.CabinetEquipmentDao;
import com.afunms.cabinet.dao.EqpRoomDao;
import com.afunms.cabinet.dao.MachineCabinetDao;
import com.afunms.cabinet.model.Cabinet;
import com.afunms.cabinet.model.CabinetEquipment;
import com.afunms.cabinet.model.EqpRoom;
import com.afunms.cabinet.model.EquipmentReport;
import com.afunms.cabinet.model.MachineCabinet;
import com.afunms.common.base.BaseManager;
import com.afunms.common.base.DaoInterface;
import com.afunms.common.base.ErrorMessage;
import com.afunms.common.base.ManagerInterface;
import com.afunms.common.util.DBManager;
import com.afunms.common.util.SessionConstant;
import com.afunms.common.util.SysLogger;
import com.afunms.config.dao.BusinessDao;
import com.afunms.config.dao.BusinessSystemDao;
import com.afunms.config.dao.PortconfigDao;
import com.afunms.config.model.Business;
import com.afunms.config.model.Portconfig;
import com.afunms.initialize.ResourceCenter;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.impl.IpResourceReport;
import com.afunms.polling.impl.IpResourceReport2;
import com.afunms.polling.snmp.interfaces.SnmpSet;
import com.afunms.report.abstraction.ExcelReport1;
import com.afunms.report.abstraction.ExcelReport2;
import com.afunms.report.base.AbstractionReport1;
import com.afunms.report.base.AbstractionReport2;
import com.afunms.system.model.User;
import com.afunms.temp.dao.InterfaceTempDao;
import com.afunms.temp.model.NodeTemp;
import com.afunms.topology.dao.HostInterfaceDao;
import com.afunms.topology.model.InterfaceNode;
import com.lowagie.text.DocumentException;

/**
 * ��������
 * 
 */
public class EquipmentReportManager extends BaseManager implements ManagerInterface {
	
public String equipmentList(){
	String roomid = getParaValue("roomid");
	String cabinetid = getParaValue("cabinetid");
	EqpRoomDao eqpRoomDao =new EqpRoomDao();
	EqpRoom eqpRoom=(EqpRoom)eqpRoomDao.findByID(roomid);
	String roomname=eqpRoom.getName();
	MachineCabinetDao machineCabinetDao=new MachineCabinetDao();
	MachineCabinet machineCabinet=(MachineCabinet)machineCabinetDao.findByID(cabinetid);
	String cabinetname=machineCabinet.getName();
	int m=this.loadequpment(roomid, cabinetid);
	String uselect = machineCabinet.getUselect();
	//int n = uselect.indexOf("U");
	String str = uselect;
	int temp = Integer.parseInt(str)
			- m;
	DecimalFormat df=new DecimalFormat ("0.00");
	double rateu = (double) m
			/ (double) Integer.parseInt(str) * 100;
	String rate = df.format(rateu) + "%";

	// ���������������
	EqpRoomDao roomdao = new EqpRoomDao();
	List roomlist = new ArrayList();
	try {
		roomlist = roomdao.loadAll();
	} catch (Exception e) {

	} finally {
		roomdao.close();
	}
	
	// ���������������
	MachineCabinetDao cabinetdao = new MachineCabinetDao();
//	Hashtable roomHash = new Hashtable();
	List cabinetlist = new ArrayList();
	try {
		cabinetlist = cabinetdao.loadAll();
	} catch (Exception e) {

	} finally {
		cabinetdao.close();
	}
	BusinessDao businessDao;
	EquipmentReport equipmentReport;
	CabinetEquipmentDao dao1 = new CabinetEquipmentDao();
	List list = new ArrayList();
	try {
		List equipmentlist = dao1.findByEquipment(Integer.parseInt(roomid),Integer.parseInt(cabinetid));	
		
		if (null != equipmentlist && equipmentlist.size() > 0) {
			for (int i = 0; i < equipmentlist.size(); i++) {
				businessDao = new BusinessDao();
				equipmentReport = new EquipmentReport();
				CabinetEquipment cabinetEqp = (CabinetEquipment) equipmentlist.get(i);
				Business business=(Business)businessDao.findByID(Integer.toString(cabinetEqp.getOperid()));
				equipmentReport.setId(cabinetEqp.getId());;
				equipmentReport.setEquipmentname(cabinetEqp.getNodename());
				equipmentReport.setEquipmentdesc(cabinetEqp.getNodedescr());
				equipmentReport.setOperation(business.getName());
				equipmentReport.setContactname(cabinetEqp.getContactname());
				equipmentReport.setContactphone(cabinetEqp.getContactphone());
				list.add(equipmentReport);
			}
		}
	} catch (Exception e) {

	} finally {
		dao1.close();
	}	
	request.setAttribute("list", list);
	session.setAttribute("roomname", roomname);
	session.setAttribute("cabinetname", cabinetname);
	session.setAttribute("uselect", uselect);
	session.setAttribute("unumber", Integer.toString(m)+"U");
	session.setAttribute("roomid", roomid);
	session.setAttribute("cabinetid", cabinetid);
	session.setAttribute("rate", rate);
	request.setAttribute("roomlist", roomlist);
	request.setAttribute("cabinetlist", cabinetlist);
	return "/cabinet/equipmentreport/equipmentlist.jsp";
}
/**
 * ����Excel
 */

private String downExlReport() {
	String roomname = (String)session.getAttribute("roomname");
	String cabinetname =(String)session.getAttribute("cabinetname");
	String uselect = (String)session.getAttribute("uselect");
	String unumber = (String)session.getAttribute("unumber");
	String rate = (String)session.getAttribute("rate");
	String roomid = (String)session.getAttribute("roomid");
	String cabinetid = (String)session.getAttribute("cabinetid");
	List equipmentlist = this.loaddate(roomid,cabinetid);
	Hashtable reporthash = new Hashtable();
//	reporthash.put("room", room);
	if (equipmentlist != null) {
		reporthash.put("equipmentlist", equipmentlist);
	} else {
		equipmentlist = new ArrayList();
	}
	AbstractionReport2 report = new ExcelReport2(new IpResourceReport2(),
			reporthash);

	report.createReport_equipment("temp" + File.separator + "equipment.xls",
			roomname,cabinetname,uselect,unumber,rate);
	request.setAttribute("filename", report.getFileName());
	return "/cabinet/cabinetreport/download.jsp";
}
private List loaddate(String roomid,String cabinetid) {
	BusinessDao businessDao;
	EquipmentReport equipmentReport;
	CabinetEquipmentDao dao1 = new CabinetEquipmentDao();
	List list = new ArrayList();
	try {
		List equipmentlist = dao1.findByEquipment(Integer.parseInt(roomid),Integer.parseInt(cabinetid));	
		
		if (null != equipmentlist && equipmentlist.size() > 0) {
			for (int i = 0; i < equipmentlist.size(); i++) {
				businessDao = new BusinessDao();
				equipmentReport = new EquipmentReport();
				CabinetEquipment cabinetEqp = (CabinetEquipment) equipmentlist.get(i);
				Business business=(Business)businessDao.findByID(Integer.toString(cabinetEqp.getOperid()));
				equipmentReport.setId(cabinetEqp.getId());;
				equipmentReport.setEquipmentname(cabinetEqp.getNodename());
				equipmentReport.setEquipmentdesc(cabinetEqp.getNodedescr());
				equipmentReport.setOperation(business.getName());
				equipmentReport.setContactname(cabinetEqp.getContactname());
				equipmentReport.setContactphone(cabinetEqp.getContactphone());
				list.add(equipmentReport);
			}
		}
	} catch (Exception e) {

	} finally {
		dao1.close();
	}
	return list;
}
/**
 * ����Word
 */

private String downWordReport() {
	String roomname = (String)session.getAttribute("roomname");
	String cabinetname =(String)session.getAttribute("cabinetname");
	String uselect = (String)session.getAttribute("uselect");
	String unumber = (String)session.getAttribute("unumber");
	String rate = (String)session.getAttribute("rate");
	String roomid = (String)session.getAttribute("roomid");
	String cabinetid = (String)session.getAttribute("cabinetid");
	List equipmentlist = this.loaddate(roomid,cabinetid);
	Hashtable reporthash = new Hashtable();
	String file = "temp/equipment.doc";// ���浽��Ŀ�ļ����µ�ָ���ļ���
	String filename = ResourceCenter.getInstance().getSysPath() + file;
//	reporthash.put("room", room);
	if (equipmentlist != null) {
		reporthash.put("equipmentlist", equipmentlist);
	} else {
		equipmentlist = new ArrayList();
	}
	ExcelReport2 report = new ExcelReport2(new IpResourceReport2(),
			reporthash);

	try {
		
		report.createReport_equipmentWord(filename,
				roomname,cabinetname,uselect,unumber,rate);
	} catch (DocumentException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	request.setAttribute("filename", filename);
	return "/cabinet/cabinetreport/download.jsp";
}
/**
 * ����PDF
 */

private String downPdfReport() {
	String roomname = (String)session.getAttribute("roomname");
	String cabinetname =(String)session.getAttribute("cabinetname");
	String uselect = (String)session.getAttribute("uselect");
	String unumber = (String)session.getAttribute("unumber");
	String rate = (String)session.getAttribute("rate");
	String roomid = (String)session.getAttribute("roomid");
	String cabinetid = (String)session.getAttribute("cabinetid");
	String file = "temp/equipment.pdf";// ���浽��Ŀ�ļ����µ�ָ���ļ���
	String filename = ResourceCenter.getInstance().getSysPath() + file;// ��ȡϵͳ�ļ���·��
	List equipmentlist = this.loaddate(roomid,cabinetid);
	Hashtable reporthash = new Hashtable();
//	reporthash.put("room", room);
	if (equipmentlist != null) {
		reporthash.put("equipmentlist", equipmentlist);
	} else {
		equipmentlist = new ArrayList();
	}
	ExcelReport2 report = new ExcelReport2(new IpResourceReport2(),
			reporthash);

	try {
		
		report.createReport_equipmentPdf(filename,
				roomname,cabinetname,uselect,unumber,rate);
	} catch (DocumentException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	request.setAttribute("filename", filename);
	return "/cabinet/cabinetreport/download.jsp";
}
private int loadequpment(String roomid,String cabinetid){
	CabinetEquipmentDao dao = new CabinetEquipmentDao();
	int b=0;
	try {
	List  listcabinet=dao.findByEquipment(Integer.parseInt(roomid),Integer.parseInt(cabinetid));
	if(null != listcabinet && listcabinet.size() > 0){
		
		for (int i = 0; i < listcabinet.size(); i++) {
			CabinetEquipment eqp = (CabinetEquipment) listcabinet.get(i);
			String use=eqp.getUnmubers();
			int s= use.indexOf("U");
			String Str3=use.substring(0, s);
			int a=Integer.parseInt(Str3);
			b=b+a;	
		}
	}
	} catch (Exception e) {

	} finally {
		dao.close();
	}
	return b;
	
}
private String queryList(){
	String roomid = getParaValue("room");
	String cabinetid = getParaValue("cabinet");
	EqpRoomDao eqpRoomDao =new EqpRoomDao();
	EqpRoom eqpRoom=(EqpRoom)eqpRoomDao.findByID(roomid);
	String roomname=eqpRoom.getName();
	MachineCabinetDao machineCabinetDao=new MachineCabinetDao();
	MachineCabinet machineCabinet=(MachineCabinet)machineCabinetDao.findByID(cabinetid);
	System.out.println("..............."+machineCabinet);
	String cabinetname="";
	if(machineCabinet != null){
		cabinetname=machineCabinet.getName();
	}
//	String cabinetname=machineCabinet.getName();
	int m=this.loadequpment(roomid, cabinetid);
//	CabinetEquipmentDao dao = new CabinetEquipmentDao();
//	CabinetEquipment  cabinetEquipment=(CabinetEquipment)dao.findByID(equipmentid);
	String uselect = "";
	String rate = "";
	if(machineCabinet != null){
		 uselect = machineCabinet.getUselect();
//		String unumber = cabinetEquipment.getUnmubers();
//		 System.out.println("=================="+uselect.indexOf("U"));
		int n = uselect.indexOf("U");
		if(n != -1){
			String str = uselect.substring(0, n);
			int temp = Integer.parseInt(str)
					- m;
			DecimalFormat df=new DecimalFormat ("0.00");
			double rateu = (double) m
					/ (double) Integer.parseInt(str) * 100;
			rate = df.format(rateu) + "%";
		}
	}
	
	// ���������������
	EqpRoomDao roomdao = new EqpRoomDao();
//	Hashtable roomHash = new Hashtable();
	List roomlist = new ArrayList();
	try {
		roomlist = roomdao.loadAll();
	} catch (Exception e) {

	} finally {
		roomdao.close();
	}
	
	// ���������������
	MachineCabinetDao cabinetdao = new MachineCabinetDao();
//	Hashtable roomHash = new Hashtable();
	List cabinetlist = new ArrayList();
	try {
		cabinetlist = cabinetdao.loadAll();
	} catch (Exception e) {

	} finally {
		cabinetdao.close();
	}
	BusinessDao businessDao;
	EquipmentReport equipmentReport;
	CabinetEquipmentDao dao1 = new CabinetEquipmentDao();
	List list = new ArrayList();
	try {
		List equipmentlist = dao1.findByEquipment(Integer.parseInt(roomid),Integer.parseInt(cabinetid));	
		
		if (null != equipmentlist && equipmentlist.size() > 0) {
			for (int i = 0; i < equipmentlist.size(); i++) {
				businessDao = new BusinessDao();
				equipmentReport = new EquipmentReport();
				CabinetEquipment cabinetEqp = (CabinetEquipment) equipmentlist.get(i);
				Business business=(Business)businessDao.findByID(Integer.toString(cabinetEqp.getOperid()));
				equipmentReport.setId(cabinetEqp.getId());;
				equipmentReport.setEquipmentname(cabinetEqp.getNodename());
				equipmentReport.setEquipmentdesc(cabinetEqp.getNodedescr());
				equipmentReport.setOperation(business.getName());
				equipmentReport.setContactname(cabinetEqp.getContactname());
				equipmentReport.setContactphone(cabinetEqp.getContactphone());
				list.add(equipmentReport);
			}
		}
	} catch (Exception e) {

	} finally {
		dao1.close();
	}
	request.setAttribute("list", list);
	session.setAttribute("roomname", roomname);
	session.setAttribute("cabinetname", cabinetname);
	session.setAttribute("uselect", uselect);
	session.setAttribute("unumber", Integer.toString(m)+"U");
	session.setAttribute("roomid", roomid);
	session.setAttribute("cabinetid", cabinetid);
	session.setAttribute("rate", rate);
	request.setAttribute("roomlist", roomlist);
	request.setAttribute("cabinetlist", cabinetlist);
	return "/cabinet/equipmentreport/equipmentlist.jsp";
}

	public String execute(String action) {
		if (action.equals("list")){		
			EqpRoomDao roomdao = new EqpRoomDao();
			Hashtable roomHash = new Hashtable();
			   List roomlist = new ArrayList();
			   try{
				   roomlist = roomdao.loadAll();
			   }catch(Exception e){
				   
			   }finally{
				   roomdao.close();
			   }
			   if(roomlist != null && roomlist.size()>0){
				   for(int i=0;i<roomlist.size();i++){
					   EqpRoom room = (EqpRoom)roomlist.get(i);
					   roomHash.put(room.getId()+"", room.getName());
				   }
			   }
			   request.setAttribute("roomhash",roomHash);
			 DaoInterface dao = new MachineCabinetDao();
	   	       setTarget("/cabinet/equipmentreport/list.jsp");
	   	     return list(dao);
		}
		if (action.equals("equipmentlist")) {
			return equipmentList();
		}
		if (action.equals("downexlreport")) {
			return downExlReport();
		}
		if (action.equals("querylist")) {
			return queryList();
		}
		if (action.equals("downwordreport")) {
			return downWordReport();
		}		
		if (action.equals("downpdfreport")) {
			return downPdfReport();
		}
		return null;
	}
}
