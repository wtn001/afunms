package com.afunms.ip.manage;

import java.awt.Color;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.afunms.common.base.BaseManager;
import com.afunms.common.base.BaseVo;
import com.afunms.common.base.DaoInterface;
import com.afunms.common.base.ManagerInterface;
import com.afunms.common.util.SysLogger;
import com.afunms.initialize.ResourceCenter;
import com.afunms.ip.stationtype.dao.*;
import com.afunms.ip.stationtype.model.*;
import com.afunms.system.dao.DepartmentDao;
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

public class ipAllManager extends BaseManager implements ManagerInterface {

	// private stationtypeDao id = new stationtypeDao();

	public String execute(String action) {
		if (action.equals("add")) {
			return add();
		}
		if (action.equals("list")) {
			return list();
		}
		if (action.equals("ready_edit")) {
			return ready_edit();
		}
		if (action.equals("delete")) {
			return delete();
		}
		if (action.equals("update")) {
			return update();
		}if(action.equals("listbackbone")){
			return listBackbone();
		}if(action.equals("listloopback")){
			return listLoopback();
		}if(action.equals("listpe")){
			return listPe();
		}if(action.equals("listpe_ce")){
			return listPe_ce();
		}if(action.equals("listbussiness")){
			return listBussiness();
		}if(action.equals("reportlist")){
			return reportList();
		}if(action.equals("createdoc")){
			return createdoc();
		}
		return null;
	}

	public String delete() {
		DaoInterface ipconfigdao = new alltypeDao();
		pestorageDao peDao = new pestorageDao();
		pe_cestorageDao pe_ceDao = new pe_cestorageDao();
		bussinessDao bussinessdao = new bussinessDao();
		loopbackstorageDao loopback = new loopbackstorageDao();
		fieldDao field = new fieldDao();
		ip_selectDao is = new ip_selectDao();
		
		List l = new ArrayList();
		alltype all = null;
		String[] id = request.getParameterValues("checkbox");
		for(int i = 0;i<id.length;i++){
			all = (alltype)ipconfigdao.findByID(id[i]);
			l.add(all.getId());
			is.delete("delete from ip_portal_apply where ids="+id[i]);
			loopback.update(" delete from ip_loopback where backbone_id="+id[i]);
		}
		
		ipconfigdao.delete(id);
		peDao.delete(id);
		pe_ceDao.delete(id);
		bussinessdao.delect(l);
		field.delete(id);
		return list();
	}

	public String add() {
		// DaoInterface ipconfigdao = new stationtypeDao();
		alltypeDao ipconfigdao = new alltypeDao();
		alltype vo = new alltype();
		String backbone_name = request.getParameter("backbone_name");
		String loopback_begin = request.getParameter("loopback_begin");
		String loopback_end = request.getParameter("loopback_end");
		String pe_begin = request.getParameter("pe_begin");
		String pe_end = request.getParameter("pe_end");
		String pe_ce_begin = request.getParameter("pe_ce_begin");
		String pe_ce_end = request.getParameter("pe_ce_end");
		String bussiness_begin = request.getParameter("bussiness_begin");
		String bussiness_end = request.getParameter("bussiness_end");
		
		vo.setBackbone_name(backbone_name);
		vo.setLoopback_begin(loopback_begin);
		vo.setLoopback_end(loopback_end);
		vo.setPe_begin(pe_begin);
		vo.setPe_end(pe_end);
		vo.setPe_ce_begin(pe_ce_begin);
		vo.setPe_ce_end(pe_ce_end);
		vo.setBus_begin(bussiness_begin);
		vo.setBus_end(bussiness_end);
		try {
			ipconfigdao.saveCZ(vo);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ipconfigdao.close();
		}
		
		List list =new ArrayList();
		ipAllManager manager = new ipAllManager();
//		// ��ip_backbone�����������IP
		backbonestorageDao backbone = new backbonestorageDao();
        alltypeDao all = new alltypeDao();
        int idd = all.getNextID("ip_alltype")-1;
        String id = idd+"";
//        
//        backbonestorageDao backbone = new backbonestorageDao();
//        int backbone_b = manager.Integer(backbone_begin,backbone_end);
//        int backbone_e = manager.Integer(backbone_end,backbone_begin);
//        backbone.saveIP(backbone_begin, backbone_b, id, backbone_e,backbone_end);
		// ��ip_loopback�����������IP
		loopbackstorageDao loopback = new loopbackstorageDao();
		int loopback_b = manager.Integer(loopback_begin,loopback_end);
		int loopback_e = manager.Integer(loopback_end,loopback_begin);
		loopback.saveIP(loopback_begin,  id, loopback_end);
		
		// ��ip_pe�����������IP
//		pestorageDao pe = new pestorageDao();
//		int pe_b = manager.Integer(pe_begin,pe_end);
//		int pe_e = manager.Integer(pe_end,pe_begin);
//		pe.saveIP(pe_begin, pe_b, id, pe_e,pe_end);
		
		ipAllManager iam = new ipAllManager();
		iam.insert_pe(pe_begin, pe_end,idd);
		
		
		// ��ip_pe_ce�����������IP
//		pe_cestorageDao pe_ce = new pe_cestorageDao();
//		int pe_ce_b = manager.Integer(pe_ce_begin,pe_ce_end);
//		int pe_ce_e = manager.Integer(pe_ce_end,pe_ce_begin);
//		pe_ce.saveIP(pe_ce_begin, pe_ce_b, id, pe_ce_e,pe_ce_end);
		iam.insert_pe_ce(pe_ce_begin, pe_ce_end, idd);
		
		
		// ��ip_bussiness�����������IP
//		bussinessstorageDao bussiness = new bussinessstorageDao();
//		int bussiness_b = manager.Integer(bussiness_begin,bussiness_end);
//		int bussiness_e = manager.Integer(bussiness_end,bussiness_begin);
//		bussiness.saveIP(bussiness_begin, bussiness_b, id, bussiness_e,bussiness_end);
		iam.insert_bus(bussiness_begin, bussiness_end, idd);
		return list();
	}

	public void insert_bus(String pe_begin,String pe_end,int backbone_id){
		String[] ip_begin = pe_begin.split("\\.");
		String[] ip_end = pe_end.split("\\.");
		String ip_begin_3 = ip_begin[2];
		String ip_end_3 = ip_end[2];
		int pe_b = Integer.parseInt(ip_begin_3);
		int pe_e = Integer.parseInt(ip_end_3);
		int bus = 1;
		for (int j = pe_b; j <= pe_e; j++) {
			for (int i = 0; i < 8; i++) {
				int y = i*16;
				int x = y+14;
				String encryption="";  
				if(i==0){
					  encryption=ip_begin[0]+"."+ip_begin[1]+"."+j+"."+(x-1);
				  }
					StringBuffer sql = new StringBuffer(100);
					sql.append("insert into ip_bussiness (buskind,busname,vlan,segment,gateway,encryption,vlan_ip,flag,ip_use,backbone_id) values(");
					sql.append("'");
					sql.append("ʵʱҵ��");
					sql.append("',");
					sql.append("'");
					sql.append("ҵ��"+(i+1));
					sql.append("','");
					sql.append((i+10));
					sql.append("',");
					sql.append("'");
					sql.append(ip_begin[0]+"."+ip_begin[1]+"."+j+"."+y);
					sql.append("',");
					sql.append("'");
					sql.append(ip_begin[0]+"."+ip_begin[1]+"."+j+"."+x);
					sql.append("',");
					sql.append("'");
					sql.append(encryption);
					sql.append("',");
					sql.append("'");
					sql.append(ip_begin[0]+"."+ip_begin[1]+"."+j+"."+(y+1));
					sql.append("',");
					sql.append("'");
					sql.append(ip_begin[0]+"."+ip_begin[1]+"."+j);
					sql.append("',");
					sql.append("'");
					sql.append(ip_begin[0]+"."+ip_begin[1]+"."+j+"."+(y+1)+"~~"+(x-2));
					sql.append("',");
					sql.append(backbone_id);
					sql.append(")");
					pestorageDao pe = new pestorageDao();
					pe.insert_pe(sql.toString());
		  }
			for (int i = 8; i < 16; i++) {
				int y = i*16;
				int x = y+14;
				
				String encryption="";  
				if(i==8){
					  encryption=ip_begin[0]+"."+ip_begin[1]+"."+j+"."+(x-1);
				  }
					StringBuffer sql = new StringBuffer(100);
					sql.append("insert into ip_bussiness (buskind,busname,vlan,segment,gateway,encryption,vlan_ip,flag,ip_use,backbone_id) values(");
					sql.append("'");
					sql.append("��ʵʱҵ��");
					sql.append("',");
					sql.append("'");
					sql.append("ҵ��"+(i-7));
					sql.append("','");
					sql.append((i+12));
					sql.append("',");
					sql.append("'");
					sql.append(ip_begin[0]+"."+ip_begin[1]+"."+j+"."+y);
					sql.append("',");
					sql.append("'");
					sql.append(ip_begin[0]+"."+ip_begin[1]+"."+j+"."+x);
					sql.append("',");
					sql.append("'");
					sql.append(encryption);
					sql.append("',");
					sql.append("'");
					sql.append(ip_begin[0]+"."+ip_begin[1]+"."+j+"."+(y+1));
					sql.append("',");
					sql.append("'");
					sql.append(ip_begin[0]+"."+ip_begin[1]+"."+j);
					sql.append("',");
					sql.append("'");
					sql.append(ip_begin[0]+"."+ip_begin[1]+"."+j+"."+(y+1)+"~~"+(x-2));
					sql.append("',");
					sql.append(backbone_id);
					sql.append(")");
					pestorageDao pe = new pestorageDao();
					pe.insert_pe(sql.toString());
		}			
			
	}
}
	
	public void insert_pe(String pe_begin,String pe_end,int backbone_id){
		String[] ip_begin = pe_begin.split("\\.");
		String[] ip_end = pe_end.split("\\.");
		String ip_begin_3 = ip_begin[2];
		String ip_end_3 = ip_end[2];
		
		if(ip_begin_3.equalsIgnoreCase(ip_end_3)){
				for(int i=0; i<32; i++){
					int y = i*8;
					int x = y+4;
					StringBuffer sql = new StringBuffer(100);
					sql.append("insert into ip_pe (backbone1,backbone2,backbone_id) values(");
					sql.append("'");
					sql.append(ip_begin[0]+"."+ip_begin[1]+"."+ip_begin[2]+"."+y);
					sql.append("',");
					sql.append("'");
					sql.append(ip_begin[0]+"."+ip_begin[1]+"."+ip_begin[2]+"."+x);
					sql.append("',");
					sql.append(backbone_id);
					sql.append(")");
					pestorageDao pe = new pestorageDao();
					pe.insert_pe(sql.toString());
				}
		}else{
			int pe_b = Integer.parseInt(ip_begin_3);
			int pe_e = Integer.parseInt(ip_end_3);
			for(int j=pe_b;j<=pe_e;j++){
				for(int i=0; i<32; i++){
					int y = i*8;
					int x = y+4;
					StringBuffer sql = new StringBuffer(100);
					sql.append("insert into ip_pe (backbone1,backbone2,backbone_id) values(");
					sql.append("'");
					sql.append(ip_begin[0]+"."+ip_begin[1]+"."+j+"."+y);
					sql.append("',");
					sql.append("'");
					sql.append(ip_begin[0]+"."+ip_begin[1]+"."+j+"."+x);
					sql.append("',");
					sql.append(backbone_id);
					sql.append(")");
					pestorageDao pe = new pestorageDao();
					pe.insert_pe(sql.toString());
				}
			}
		}
	}
	
	
	public void insert_pe_ce(String pe_ce_begin,String pe_ce_end,int backbone_id){
		String[] ip_begin = pe_ce_begin.split("\\.");
		String[] ip_end = pe_ce_end.split("\\.");
		String ip_begin_3 = ip_begin[2];
		String ip_end_3 = ip_end[2];
		
		if(ip_begin_3.equalsIgnoreCase(ip_end_3)){
				for(int i=0; i<32; i++){
					int y = i*8;
					int x = y+4;
					StringBuffer sql = new StringBuffer(100);
					sql.append("insert into ip_pe_ce (s1,s2,backbone_id) values(");
					sql.append("'");
					sql.append(ip_begin[0]+"."+ip_begin[1]+"."+ip_begin[2]+"."+y);
					sql.append("',");
					sql.append("'");
					sql.append(ip_begin[0]+"."+ip_begin[1]+"."+ip_begin[2]+"."+x);
					sql.append("',");
					sql.append(backbone_id);
					sql.append(")");
					pestorageDao pe = new pestorageDao();
					pe.insert_pe(sql.toString());
				}
		}else{
			int pe_b = Integer.parseInt(ip_begin_3);
			int pe_e = Integer.parseInt(ip_end_3);
			for(int j=pe_b;j<=pe_e;j++){
				for(int i=0; i<32; i++){
					int y = i*8;
					int x = y+4;
					StringBuffer sql = new StringBuffer(100);
					sql.append("insert into ip_pe_ce (s1,s2,backbone_id) values(");
					sql.append("'");
					sql.append(ip_begin[0]+"."+ip_begin[1]+"."+j+"."+y);
					sql.append("',");
					sql.append("'");
					sql.append(ip_begin[0]+"."+ip_begin[1]+"."+j+"."+x);
					sql.append("',");
					sql.append(backbone_id);
					sql.append(")");
					pestorageDao pe = new pestorageDao();
					pe.insert_pe(sql.toString());
				}
			}
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public String list() {
		DaoInterface ipconfigdao = new alltypeDao();
		setTarget("/ipconfig/alltype/alltypelist.jsp");
		return list(ipconfigdao);
	}
	
	public String listBackbone(){
		String id = request.getParameter("id");
		DaoInterface backbone = new backbonestorageDao();
		List list = backbone.loadAll("where backbone_id = "+id);
		request.setAttribute("list", list);
//		setTarget("/ipconfig/alltype/listbackbone.jsp");
		return "/ipconfig/alltype/listbackbone.jsp";
	}
	
	
	public String listLoopback(){
		String id = request.getParameter("id");
		DaoInterface loopback = new loopbackstorageDao();
		List list = loopback.loadAll("where loopback_id = "+id);
		request.setAttribute("list", list);
		return "/ipconfig/alltype/listloopback.jsp";
	}
	
	
	public String listPe(){
		String id = request.getParameter("id");
		DaoInterface pe = new pestorageDao();
		List list = pe.loadAll("where pe_id = "+id);
		request.setAttribute("list", list);
//		setTarget("/ipconfig/alltype/listpe.jsp");
		return "/ipconfig/alltype/listpe.jsp";
	}
	
	public String listPe_ce(){
		String id = request.getParameter("id");
		DaoInterface pe_ce = new pe_cestorageDao();
		List list = pe_ce.loadAll("where pe_ce_id = "+id);
		request.setAttribute("list", list);
//		setTarget("/ipconfig/alltype/listpe_ce.jsp");
		return "/ipconfig/alltype/listpe_ce.jsp";
	}
	
	public String listBussiness(){
		String id = request.getParameter("id");
		DaoInterface bussiness = new bussinessstorageDao();
		List list = bussiness.loadAll("where bussiness_id = "+id);
		request.setAttribute("list", list);
//		setTarget("/ipconfig/alltype/listbussiness.jsp");
		return "/ipconfig/alltype/listbussiness.jsp";
	}
	

	public String ready_edit() {
		DaoInterface ipconfigdao = new alltypeDao();
		String id1 = request.getParameter("id");
		// int id = Integer.parseInt("id1");
		encryptiontype list = (encryptiontype) ipconfigdao.findByID(id1);
		request.setAttribute("list", list);
		return "/ipconfig/alltype/alltypeedit.jsp";
	}

	
	protected String update() {
		alltype vo = new alltype();
		String name = request.getParameter("name");
		String id1 = request.getParameter("id");
		String descr = request.getParameter("descr");
		String bak = request.getParameter("bak");
		int id = Integer.parseInt(id1);

		DaoInterface dao = new alltypeDao();
		dao.update(vo);
		return list();
	}

	public int Integer(String s,String ss) {
		if(s.split("\\.")[2].equals(ss.split("\\.")[2])){
			String[] list = s.split("\\.");
			return Integer.parseInt(list[3]);
		}else{
			String[] list = s.split("\\.");
			return Integer.parseInt(list[2]);
		}
	}

	public int ipNum(String ip_begin, String ip_end) {
		int i = 0;
		String[] ip1 = ip_begin.split("\\.");
		String[] ip2 = ip_end.split("\\.");
		
		if(ip1[2].equals(ip2[2])){
			String[] ip_e = ip_end.split("\\.");
			int end = Integer.parseInt(ip_e[3]);
			String[] ip_b = ip_begin.split("\\.");
			int begin = Integer.parseInt(ip_b[3]);
			i = end - begin;
		}else if(!ip1[1].equals(ip2[1])){
			String[] ip_e = ip_end.split("\\.");
			int end = Integer.parseInt(ip_e[1]);
			String[] ip_b = ip_begin.split("\\.");
			int begin = Integer.parseInt(ip_b[1]);
			i = end - begin;
		}else{
			String[] ip_e = ip_end.split("\\.");
			int end = Integer.parseInt(ip_e[2]);
			String[] ip_b = ip_begin.split("\\.");
			int begin = Integer.parseInt(ip_b[2]);
			i = end - begin;
		}
		return i;
	}

	public String reportList(){
		DaoInterface alltype = new alltypeDao();
		setTarget("/ipconfig/ipreport/list.jsp");
		return list(alltype);
	}
	
	
	public String createdoc(){
		String file = "/temp/ipReport.doc";// ���浽��Ŀ�ļ����µ�ָ���ļ���
		String fileName = ResourceCenter.getInstance().getSysPath() + file;// ��ȡϵͳ�ļ���·��
		//��վ��id
		try {
			createDocContextEvent(fileName);
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		request.setAttribute("filename", fileName);
		return "/ipconfig/ipreport/download.jsp";
	}
	
	public void createDocContextEvent(String file)throws DocumentException, IOException{
		//ȡ����Ӧ������
		DaoInterface alltype = new alltypeDao();
		
		//��ѯ���ó�վ��  ����Ӧ��Loopback��pe,pe_ce ,bussiness
		
		
		
		
		
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
		Font fontChinese = new Font(bfChinese,7, Font.NORMAL, Color.black);
		Table aTable = new Table(7);
		
		int width[] = {40,40,60,110,40,40,40};
		aTable.setWidths(width);
		aTable.setWidth(90); // ռҳ���� 90%
		aTable.setAlignment(Element.ALIGN_CENTER);// ������ʾ
		aTable.setAutoFillEmptyCells(true); // �Զ�����
		aTable.setBorderWidth(1); // �߿���
		aTable.setBorderColor(new Color(0, 125, 255)); // �߿���ɫ
		aTable.setPadding(2);// �ľ࣬��Ч����֪��ʲô��˼��
		aTable.setSpacing(0);// ����Ԫ��֮��ļ��
		aTable.setBorder(2);// �߿�
		aTable.endHeaders();

		Cell cell1 = new Cell("����ƽ��");
		Cell cell2 = new Cell("�Ǹɵ�");
		Cell cell3 = new Cell("IP��ַ����");
		Cell cell4 = new Cell("IP�ض�");
		Cell cell5 = new Cell("IP����");
		Cell cell6 = new Cell("��ʹ��");
		Cell cell7 = new Cell("ʹ����");
		
		cell1.setBackgroundColor(Color.LIGHT_GRAY);
		cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell2.setBackgroundColor(Color.LIGHT_GRAY);
		cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell3.setBackgroundColor(Color.LIGHT_GRAY);
		cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell4.setBackgroundColor(Color.LIGHT_GRAY);
		cell4.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell5.setBackgroundColor(Color.LIGHT_GRAY);
		cell5.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell6.setBackgroundColor(Color.LIGHT_GRAY);
		cell6.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell7.setBackgroundColor(Color.LIGHT_GRAY);
		cell7.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell7.setHorizontalAlignment(Element.ALIGN_CENTER);
		
		aTable.addCell(cell1);
		aTable.addCell(cell2);
		aTable.addCell(cell3);
		aTable.addCell(cell4);
		aTable.addCell(cell5);
		aTable.addCell(cell6);
		aTable.addCell(cell7);
		
		
		List list = alltype.loadAll();
		alltype vo = null;
		int z_num_loop = 0;
		int u_num_loop = 0;
		int z_num_pe = 0;
		int u_num_pe = 0;
		int z_num_pe_ce = 0;
		int u_num_pe_ce = 0;
		int z_num_bussiness = 0;
		int u_num_bussiness = 0;
		for (int i = 0; i < list.size(); i++) {
			vo = (alltype) list.get(i);
			//loopback
			alltypeDao ad = new alltypeDao();
			int loop_l = ad.count("ip_loopback","where loopback_id ="+vo.getId());
			z_num_loop = loop_l;
			alltypeDao ad1 = new alltypeDao();
			int loop_l1 = ad1.count("ip_loopback","where type=1 and loopback_id ="+vo.getId());
			u_num_loop = loop_l1;
			float loopback1 = (float)loop_l1/(float)loop_l;
		    DecimalFormat format = new DecimalFormat("#.00");
            String loopback = format.format(loopback1*100)+"%";
			//pe
			alltypeDao ad_p = new alltypeDao();
			int pe_l = ad_p.count("ip_pe","where pe_id ="+vo.getId());
			z_num_pe = pe_l;
			alltypeDao ad_p1 = new alltypeDao();
			int pe_l1 = ad_p1.count("ip_pe"," where type=1 and pe_id ="+vo.getId());
			u_num_pe = pe_l1;
			float pe1 = (float)pe_l1/(float)pe_l;
		    DecimalFormat format1 = new DecimalFormat("#.00");
            String pe = format1.format(pe1*100)+"%";
			//pe_ce
			alltypeDao ad_pc = new alltypeDao();
			int pc_l = ad_pc.count("ip_pe_ce"," where pe_ce_id ="+vo.getId());
			z_num_pe_ce = pc_l;
			alltypeDao ad_pc1 = new alltypeDao();
			int pc_l1 = ad_pc1.count("ip_pe_ce"," where type=1 and pe_ce_id ="+vo.getId());
			u_num_pe_ce = pc_l1;
			float pe_ce1 = (float)pc_l1/(float)pc_l;
		    DecimalFormat format2 = new DecimalFormat("#.00");
            String pe_ce = format2.format(pe_ce1*100)+"%";
			//bussiness
			alltypeDao bu = new alltypeDao();
			int bu_l = bu.count("(select distinct bussiness ,t.id,t.bussiness_id,t.type  from ip_bussiness t ) x"," where bussiness_id ="+vo.getId());
			z_num_bussiness = bu_l;
			alltypeDao bu1 = new alltypeDao();
			int bu_l1 = bu1.count("(select distinct bussiness ,t.id,t.bussiness_id,t.type  from ip_bussiness t) x ","where type=1 and bussiness_id ="+vo.getId());
			u_num_bussiness = bu_l1;
			float bussiness1 = (float)bu_l1/(float)bu_l;
		    DecimalFormat format3 = new DecimalFormat("#.00");
            String bussiness = format3.format(bussiness1*100)+"%";
            
            Cell cell8 = new Cell();
            cell8.setRowspan(4);
            Cell cell9 = new Cell(vo.getBackbone_name());
            cell9.setRowspan(4);
            
            Cell cell10 = new Cell("loopback��ַ");
            Cell cell11 = new Cell(vo.getLoopback_begin()+"--"+vo.getLoopback_end());
            Cell cell12 = new Cell(z_num_loop+"");
            Cell cell13 = new Cell(u_num_loop+"");
            Cell cell14 = new Cell(loopback);
            
//            Cell cell30 = new Cell();
//            Cell cell31 = new Cell();
            Cell cell15 = new Cell("PE������ַ");
            Cell cell16 = new Cell(vo.getPe_begin()+"--"+vo.getPe_end());
            Cell cell17 = new Cell(z_num_pe+"");
            Cell cell18 = new Cell(u_num_pe+"");
            Cell cell19 = new Cell(pe);
            
//            Cell cell32 = new Cell();
//            Cell cell33 = new Cell();
            Cell cell20 = new Cell("PE_CE������ַ");
            Cell cell21 = new Cell(vo.getPe_ce_begin()+"--"+vo.getPe_ce_end());
            Cell cell22 = new Cell(z_num_pe_ce+"");
            Cell cell23 = new Cell(u_num_pe_ce+"");
            Cell cell24 = new Cell(pe_ce);
            
//            Cell cell34 = new Cell();
//            Cell cell35 = new Cell();
            Cell cell25 = new Cell("ҵ���ַ");
            Cell cell26 = new Cell(vo.getBus_begin()+"--"+vo.getBus_end());
            Cell cell27 = new Cell(z_num_bussiness+"");
            Cell cell28 = new Cell(u_num_bussiness+"");
            Cell cell29 = new Cell(bussiness);
            
            cell8.setVerticalAlignment(Element.ALIGN_MIDDLE);
    		cell8.setHorizontalAlignment(Element.ALIGN_CENTER);
    		cell9.setVerticalAlignment(Element.ALIGN_MIDDLE);
    		cell9.setHorizontalAlignment(Element.ALIGN_CENTER);
    		cell10.setVerticalAlignment(Element.ALIGN_MIDDLE);
    		cell10.setHorizontalAlignment(Element.ALIGN_CENTER);
    		cell11.setVerticalAlignment(Element.ALIGN_MIDDLE);
    		cell11.setHorizontalAlignment(Element.ALIGN_CENTER);
    		cell12.setVerticalAlignment(Element.ALIGN_MIDDLE);
    		cell12.setHorizontalAlignment(Element.ALIGN_CENTER);
    		cell13.setVerticalAlignment(Element.ALIGN_MIDDLE);
    		cell13.setHorizontalAlignment(Element.ALIGN_CENTER);
    		cell14.setVerticalAlignment(Element.ALIGN_MIDDLE);
    		cell14.setHorizontalAlignment(Element.ALIGN_CENTER);
    		cell15.setVerticalAlignment(Element.ALIGN_MIDDLE);
    		cell15.setHorizontalAlignment(Element.ALIGN_CENTER);
    		cell16.setVerticalAlignment(Element.ALIGN_MIDDLE);
    		cell16.setHorizontalAlignment(Element.ALIGN_CENTER);
    		cell17.setVerticalAlignment(Element.ALIGN_MIDDLE);
    		cell17.setHorizontalAlignment(Element.ALIGN_CENTER);
    		cell18.setVerticalAlignment(Element.ALIGN_MIDDLE);
    		cell18.setHorizontalAlignment(Element.ALIGN_CENTER);
    		cell19.setVerticalAlignment(Element.ALIGN_MIDDLE);
    		cell19.setHorizontalAlignment(Element.ALIGN_CENTER);
    		cell20.setVerticalAlignment(Element.ALIGN_MIDDLE);
    		cell20.setHorizontalAlignment(Element.ALIGN_CENTER);
    		cell21.setVerticalAlignment(Element.ALIGN_MIDDLE);
    		cell21.setHorizontalAlignment(Element.ALIGN_CENTER);
    		cell22.setVerticalAlignment(Element.ALIGN_MIDDLE);
    		cell22.setHorizontalAlignment(Element.ALIGN_CENTER);
    		cell23.setVerticalAlignment(Element.ALIGN_MIDDLE);
    		cell23.setHorizontalAlignment(Element.ALIGN_CENTER);
    		cell24.setVerticalAlignment(Element.ALIGN_MIDDLE);
    		cell24.setHorizontalAlignment(Element.ALIGN_CENTER);
    		cell25.setVerticalAlignment(Element.ALIGN_MIDDLE);
    		cell25.setHorizontalAlignment(Element.ALIGN_CENTER);
    		cell26.setVerticalAlignment(Element.ALIGN_MIDDLE);
    		cell26.setHorizontalAlignment(Element.ALIGN_CENTER);
    		cell27.setVerticalAlignment(Element.ALIGN_MIDDLE);
    		cell27.setHorizontalAlignment(Element.ALIGN_CENTER);
    		cell28.setVerticalAlignment(Element.ALIGN_MIDDLE);
    		cell28.setHorizontalAlignment(Element.ALIGN_CENTER);
    		cell29.setVerticalAlignment(Element.ALIGN_MIDDLE);
    		cell29.setHorizontalAlignment(Element.ALIGN_CENTER);
            
            
            
            aTable.addCell(cell8);
    		aTable.addCell(cell9);
    		aTable.addCell(cell10);
    		aTable.addCell(cell11);
    		aTable.addCell(cell12);
    		aTable.addCell(cell13);
    		aTable.addCell(cell14);
    		
//    		aTable.addCell(cell30);
//    		aTable.addCell(cell31);
    		
    		aTable.addCell(cell15);
    		aTable.addCell(cell16);
    		aTable.addCell(cell17);
    		aTable.addCell(cell18);
    		aTable.addCell(cell19);
    		
//    		aTable.addCell(cell32);
//    		aTable.addCell(cell33);
    		
    		aTable.addCell(cell20);
    		aTable.addCell(cell21);
    		aTable.addCell(cell22);
    		aTable.addCell(cell23);
    		aTable.addCell(cell24);
    		
//    		aTable.addCell(cell34);
//    		aTable.addCell(cell35);
    		
    		aTable.addCell(cell25);
    		aTable.addCell(cell26);
    		aTable.addCell(cell27);
    		aTable.addCell(cell28);
    		aTable.addCell(cell29);
		}     
		

		
		document.add(aTable);
		document.add(new Paragraph("\n"));
		document.close();
	}
}
