package com.bpm.process.dao;


import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import jxl.Workbook;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.apache.struts2.ServletActionContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bpm.process.model.ProcessStatisticalsModel;
import com.bpm.process.model.SearchModel;
import com.bpm.process.service.ProcessService;
//import com.bpm.system.utils.ExportExcelEnum;
import com.bpm.system.utils.ProcessEnum;
import com.bpm.system.utils.StringUtil;

@Repository
@Transactional
public class ExportExcelDao {
	@Resource
	private ProcessService processService;
	
	private static final String[] PERSONTASK={"�û���","����������"};
	private static final String[] PERSONPROCESSTANCE={"�û���","ʵ����������"};
	private static final String[] POSTTASK={"����","����������"};
	private static final String[] POSTPROCESSTANCE={"����","ʵ����������"};
	private static final String[] PROINSSTAT={"����","����"};
	private static final int WIDTH=20;
	private static final int HEIGHT=400;
	//public static Hashtable<String, ExportExcelEnum> EX_HX;
	private Label label = null;// �ı�����
	//private Number number=null;//��ֵ
	private int row=0;
	private int column=0;
	private static final String EXCELNAME="���̱���ͳ��";
	//private SearchModel model;
	
	private WritableFont writefont0 = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK); 
	private WritableCellFormat writeFormat0 = new WritableCellFormat(writefont0); 
	private WritableFont writefont = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD, false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK); 
	private WritableCellFormat writeFormat = new WritableCellFormat(writefont); 
	private WritableSheet worksheet =null;
	public ExportExcelDao() 
	{
		
	}
	
	public static void init()
	{
		/*EX_HX=new Hashtable<String, ExportExcelEnum>();
		for(ExportExcelEnum item:ExportExcelEnum.values())
		{
			EX_HX.put(item.toString(), item);
		}*/
	}
	
	public ExportExcelDao(String type,SearchModel model)
	{
		//excelEnum=EX_HX.get(type);
		//this.model=model;
	}
	
	
	private void creatContent(WritableWorkbook workbook,SearchModel model)
	{
		setProcessStatisticalContent(workbook,model);
	}
	
	public String export(String type,SearchModel model) 
	{
		String filename=null;
		try
		{
			//ExportExcelEnum excelEnum=null;
			//excelEnum=EX_HX.get(type);
			String filepath =EXCELNAME;
			filepath =  new String(filepath.getBytes(), "ISO8859-1"); 
			filename = filepath + ".xls";
			String path = ServletActionContext.getServletContext().getRealPath("/downloadfile")+"/"+filename;
			OutputStream os = new FileOutputStream(path);
			WritableWorkbook workbook = Workbook.createWorkbook(os);// ����������
			//writeFormat0.setAlignment(Alignment.CENTRE);
			//writeFormat.setAlignment(Alignment.CENTRE);
			if(StringUtil.isNotBlank(type) && type.equals("pro_ins_stat"))
			{
				setProInsStatContent(workbook);
			}
			else
			{
				creatContent(workbook,model);
			}
			workbook.write();
			workbook.close();
			os.close();
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return filename;
	}
	
	@SuppressWarnings("unchecked")
	private void setProcessStatisticalContent(WritableWorkbook workbook,SearchModel model)
	{
		List<ProcessStatisticalsModel> list=new ArrayList();
		if(model!=null)
		{
			list=processService.processStatisticals(model.getType(), model.getPerson(), model.getStartdate(), model.getTodate());
			setProcessStatisticalContent(workbook,  model.getType(), model.getPerson(), list);
		}
		else {
			list=processService.processStatisticals(null,null,null,null);
			setProcessStatisticalContent(workbook,  null, null, list);
		}
	}
	
	public void setProcessStatisticalContent(WritableWorkbook workbook,String type,String person,List<ProcessStatisticalsModel> list)
	{
		if(StringUtil.isBlank(person)) person="0";
 		if(StringUtil.isBlank(type)) type="0";
 		if(person.equals("0") && type.equals("0"))
 		{
 			setPersonTaskContent(workbook, list);
 		}
 		else if(person.equals("0") && type.equals("1"))
 		{
 			setPersonProcesstanceContent(workbook, list);
 		}
 		else if(person.equals("1") && type.equals("0"))
 		{
 			setPostTaskContent(workbook, list);
 		}
 		else
 		{
 			setPostProcesstanceContent(workbook, list);
 		}
	}
	
	private void setPersonTaskContent(WritableWorkbook workbook,List<ProcessStatisticalsModel> list)
	{
		int k=0;
		if(list.size()==0)  
		{
			worksheet = workbook.createSheet(EXCELNAME+k, k++);
			return ;
		}
		try {
			
			int j = 0;
			int i=1;
			for (int t = 0, size = list.size(); t <size; t++) {
				 if(t%5000 == 0)//5000����¼����һ��sheet
				 {
					 i=1;
					 row=0;
					 column=0;
					 worksheet = workbook.createSheet(EXCELNAME+k, k++);// ������k��������
					 worksheet.setRowView(row++, HEIGHT);
					 for (int p = 0; p< PERSONTASK.length; p++) 
						{
							worksheet.setColumnView(column++,WIDTH); 
							label = new Label(p, 0, PERSONTASK[p],writeFormat0);// �������δ�������������������
							worksheet.addCell(label);// д�뵥Ԫ��
						}
				 }
				 
				if(worksheet==null) return ;
				
				worksheet.setRowView(row++, HEIGHT);
				ProcessStatisticalsModel model2 = list.get(t);
				label = new Label(j++, i, model2.getAssignee(),writeFormat);
				worksheet.addCell(label);
				label = new Label(j++, i, model2.getTotal(),writeFormat);
				worksheet.addCell(label);
				j=0;
				i++;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void setPersonProcesstanceContent(WritableWorkbook workbook,List<ProcessStatisticalsModel> list)
	{
		if(list.size()==0)  
		{
			worksheet = workbook.createSheet(EXCELNAME, 0);
			return ;
		}
		try {
			
			int j = 0;
			int i=1;
			for (int t = 0, size = list.size(); t <size; t++) {
				 if(t == 0)
				 {
					 i=1;
					 row=0;
					 column=0;
					 worksheet = workbook.createSheet(EXCELNAME, 0);// ������k��������
					 worksheet.setRowView(row++, HEIGHT);
					 for (int p = 0; p< PERSONPROCESSTANCE.length; p++) 
						{
							worksheet.setColumnView(column++,WIDTH); 
							label = new Label(p, 0, PERSONPROCESSTANCE[p],writeFormat0);// �������δ�������������������
							worksheet.addCell(label);// д�뵥Ԫ��
						}
				 }
				 
				if(worksheet==null) return ;
				
				worksheet.setRowView(row++, HEIGHT);
				ProcessStatisticalsModel model2 = list.get(t);
				label = new Label(j++, i, model2.getAssignee(),writeFormat);
				worksheet.addCell(label);
				label = new Label(j++, i, model2.getTotal(),writeFormat);
				worksheet.addCell(label);
				j=0;
				i++;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void setPostTaskContent(WritableWorkbook workbook,List<ProcessStatisticalsModel> list)
	{
		if(list.size()==0)  
		{
			worksheet = workbook.createSheet(EXCELNAME, 0);
			return ;
		}
		try {
			
			int j = 0;
			int i=1;
			for (int t = 0, size = list.size(); t <size; t++) {
				 if(t == 0)
				 {
					 i=1;
					 row=0;
					 column=0;
					 worksheet = workbook.createSheet(EXCELNAME, 0);// ������k��������
					 worksheet.setRowView(row++, HEIGHT);
					 for (int p = 0; p< POSTTASK.length; p++) 
						{
							worksheet.setColumnView(column++,WIDTH); 
							label = new Label(p, 0, POSTTASK[p],writeFormat0);// �������δ�������������������
							worksheet.addCell(label);// д�뵥Ԫ��
						}
				 }
				 
				if(worksheet==null) return ;
				
				worksheet.setRowView(row++, HEIGHT);
				ProcessStatisticalsModel model2 = list.get(t);
				label = new Label(j++, i, model2.getGroupname(),writeFormat);
				worksheet.addCell(label);
				label = new Label(j++, i, model2.getTotal(),writeFormat);
				worksheet.addCell(label);
				j=0;
				i++;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void setPostProcesstanceContent(WritableWorkbook workbook,List<ProcessStatisticalsModel> list)
	{
		if(list.size()==0)  
		{
			worksheet = workbook.createSheet(EXCELNAME, 0);
			return ;
		}
		try {
			
			int j = 0;
			int i=1;
			for (int t = 0, size = list.size(); t <size; t++) {
				 if(t == 0)
				 {
					 i=1;
					 row=0;
					 column=0;
					 worksheet = workbook.createSheet(EXCELNAME, 0);// ������k��������
					 worksheet.setRowView(row++, HEIGHT);
					 for (int p = 0; p< POSTPROCESSTANCE.length; p++) 
						{
							worksheet.setColumnView(column++,WIDTH); 
							label = new Label(p, 0, POSTPROCESSTANCE[p],writeFormat0);// �������δ�������������������
							worksheet.addCell(label);// д�뵥Ԫ��
						}
				 }
				 
				if(worksheet==null) return ;
				worksheet.setRowView(row++, HEIGHT);
				ProcessStatisticalsModel model2 = list.get(t);
				label = new Label(j++, i, model2.getGroupname(),writeFormat);
				worksheet.addCell(label);
				label = new Label(j++, i, model2.getTotal(),writeFormat);
				worksheet.addCell(label);
				j=0;
				i++;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void setProInsStatContent(WritableWorkbook workbook)
	{
		HashMap<ProcessEnum, String> map=processService.queryProIns();
		if(map.size()==0)  
		{
			worksheet = workbook.createSheet(EXCELNAME, 0);
			return ;
		}
		try {
			
			int j = 0;
			int i=1;
			int t=0;
			for (ProcessEnum key:map.keySet()) {
				 if(t == 0)
				 {
					 i=1;
					 row=0;
					 column=0;
					 worksheet = workbook.createSheet(EXCELNAME, 0);// ����������
					 worksheet.setRowView(row++, HEIGHT);
					 for (int p = 0; p< PROINSSTAT.length; p++) 
						{
							worksheet.setColumnView(column++,WIDTH); 
							label = new Label(p, 0, PROINSSTAT[p],writeFormat0);// �������δ�������������������
							worksheet.addCell(label);// д�뵥Ԫ��
						}
				 }
				 
				if(worksheet==null) return ;
				worksheet.setRowView(row++, HEIGHT);
				label = new Label(j++, i, key.decp,writeFormat);
				worksheet.addCell(label);
				label = new Label(j++, i, map.get(key),writeFormat);
				worksheet.addCell(label);
				j=0;
				i++;
				t++;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
	
