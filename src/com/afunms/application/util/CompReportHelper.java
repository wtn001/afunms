package com.afunms.application.util;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableImage;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

import com.afunms.common.util.SysLogger;
import com.afunms.config.dao.CheckResultDao;
import com.afunms.config.dao.StrategyIpDao;
import com.afunms.config.model.CheckResult;
import com.afunms.config.model.StrategyIp;
import com.afunms.initialize.ResourceCenter;
import com.lowagie.text.Cell;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.rtf.RtfWriter2;


public class CompReportHelper {
public Hashtable getAllDevice(){
	Hashtable addData=new Hashtable();
	CheckResultDao dao=new CheckResultDao();
	List<CheckResult> list=new ArrayList<CheckResult>();
	List<CheckResult> checkList=dao.getAllResult();
	int count=0;
	int wrongCount = 0;
	int exactCount = 0;
	int disabledCount = 0;
	int allCount=0;
	Vector<Integer> vector=new Vector<Integer>();
	if (checkList != null && checkList.size() > 0) {
		for (int i = 0; i < checkList.size(); i++) {
			CheckResult result = checkList.get(i);
			String status = "�� ��";
			if (result.getCount0() > 0 || result.getCount1() > 0
					|| result.getCount2() > 0){
				status = "Υ ��";
				wrongCount++;
				result.setStatus(status);
				}
			CheckResultDao resultDao = new CheckResultDao();

			CheckResult vo = resultDao.getExtraCount1(result.getId() + "",result.getIp());
			if (vo != null){
				count = vo.getExactCount();
				result.setExactCount(count);
			}
			list.add(result);
		}
	}
	CheckResultDao resultDao = new CheckResultDao();
	List resultList = resultDao.getExtraCountList();
	if (resultList!=null&&resultList.size()>0) {
		exactCount = resultList.size();
		for (int i = 0; i <checkList.size(); i++) {
			CheckResult result = (CheckResult)resultList.get(i);
			result.setStatus("�� ��");
			result.setCount0(0);
			result.setCount1(0);
			result.setCount2(0);
			list.add(result);
		}
	}
	StrategyIpDao ipDao=new StrategyIpDao();
	List deviceList=ipDao.findByCondition(" where AVAILABILITY=0");
	if (deviceList != null && deviceList.size() > 0) {
		disabledCount = deviceList.size();
		for (int i = 0; i < deviceList.size(); i++) {
			StrategyIp vo = (StrategyIp) deviceList.get(i);
			CheckResult result=new CheckResult();
			result.setIp(vo.getIp());
			result.setStatus("������");
			result.setName(vo.getStrategyName());
			result.setCheckTime("--NA--");
		   list.add(result);
		}
	}
	allCount=exactCount+wrongCount+disabledCount;
	vector.add(0,allCount);
	vector.add(1, exactCount);
	vector.add(2, wrongCount);
	vector.add(3, disabledCount);
	addData.put("deviceList", list);
	addData.put("deviceVec", vector);
	return addData;
}
public static void createPie(Vector vector) {

	DefaultPieDataset dataset = new DefaultPieDataset();
	int enable=(Integer)vector.get(1);
	
	int wrong=(Integer)vector.get(2);
	int disable=(Integer)vector.get(3);
	// ��һ�����������ݵ����ƣ��ڶ������������ݵ�ֵ��
	dataset.setValue("Υ��", wrong);
	dataset.setValue("������", disable);
	dataset.setValue("˳��",enable);
   
	JFreeChart chart = ChartFactory.createPieChart("���ԺϹ��Ի���", dataset,
			true, false, false);
	// ը���ı�ͼ
	PiePlot pp = (PiePlot) chart.getPlot();
	// pp.setExplodePercent("����1",1); // ը���ı�ͼ��Ҫ���������ݵ�����һ��
	// ������������
	// JfreeChinese.setChineseForPie(chart);
	FileOutputStream fos = null;
	try {
		fos = new FileOutputStream(ResourceCenter.getInstance().getSysPath()
				+ "/resource/image/jfreechart/allDevice.png");
		ChartUtilities.writeChartAsPNG(fos, chart, 400, 300);
	} catch (FileNotFoundException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	} finally {
		try {
			if (null != fos) {
				fos.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
public void createDoc(Vector<Integer> vector,List list,String filePath) throws DocumentException, IOException {
	File file=new File(filePath);
		// ����ֽ�Ŵ�С
		Document document = new Document(PageSize.A4);
		// ����һ����д��(Writer)��document���������ͨ����д��(Writer)���Խ��ĵ�д�뵽������
		RtfWriter2.getInstance(document, new FileOutputStream(file));
		document.open();
		// ������������
		BaseFont bfChinese = BaseFont.createFont("Times-Roman", "",BaseFont.NOT_EMBEDDED);
		// ����������
		Font titleFont = new Font(bfChinese, 12, Font.BOLD);
		// ����������
		Font contextFont = new Font(bfChinese, 10, Font.NORMAL);
		Paragraph title = new Paragraph("���ԺϹ��Ա���",titleFont);
		// ���ñ����ʽ���뷽ʽ
		title.setAlignment(Element.ALIGN_CENTER);
		// title.setFont(titleFont);
		document.add(title);
		// ���� Table ���
		Font fontChinese = new Font(bfChinese, 12, Font.NORMAL, Color.black);
		String rootPath = ResourceCenter.getInstance().getSysPath();// ��ȡϵͳ�ļ���·��
		 Image img =Image.getInstance(rootPath+"/resource/image/jfreechart/allDevice.png");
		 img.setAbsolutePosition(0, 0);
		 img.setAlignment(Image.LEFT);// ����ͼƬ��ʾλ��
		// document.add(img);
		Table imgTable = new Table(2);
		
		imgTable.setWidth(100); 
		Cell img1=new Cell("���в��Ե��豸��"+vector.get(0));
		Cell img2=new Cell("�Ϲ���Ե��豸��"+vector.get(1));
		Cell img3=new Cell("Υ�����Ե��豸��"+vector.get(2));
		Cell img4=new Cell("���ݲ������豸��"+vector.get(3));
		
		Cell img5=new Cell(img);
		img5.setRowspan(4);
		imgTable.addCell(img1);
		imgTable.addCell(img5);
		imgTable.addCell(img2);
		imgTable.addCell(img3);
		imgTable.addCell(img4);
		
		imgTable.setBorderWidth(0);
		document.add(imgTable);
		
		
		Table aTable = new Table(14);
		aTable.setBorderWidth(0);
		int width[] = { 30, 50,12,30 ,12,12,12,12,12,12, 12,30, 60, 60 };
		aTable.setWidths(width);
		aTable.setWidth(100); // ռҳ���� 90%
		aTable.setAlignment(Element.ALIGN_CENTER);// ������ʾ
		aTable.setAutoFillEmptyCells(true); // �Զ�����
		aTable.setPadding(2);// �ľ࣬��Ч����֪��ʲô��˼��
		aTable.setSpacing(0);// ����Ԫ��֮��ļ��
		//aTable.setBorder(2);// �߿�
		aTable.endHeaders();

		Cell c0 = new Cell("");
		c0.setBackgroundColor(Color.LIGHT_GRAY);
		
		Cell c1 = new Cell("IP��ַ");
		
		Cell c3 = new Cell("�Ϲ���״̬");
		c3.setColspan(2);
		
		Cell c4 = new Cell("Υ��������");
		c4.setColspan(6);
		Cell c5 = new Cell("�Ϲ��Թ�����");
		c5.setColspan(2);
		
		Cell c6 = null;
		c6 = new Cell("��������");
		Cell c7= new Cell("�ϴμ��ʱ��");
		
		
		c1.setBackgroundColor(Color.LIGHT_GRAY);
		c3.setBackgroundColor(Color.LIGHT_GRAY);
		c4.setBackgroundColor(Color.LIGHT_GRAY);
		c5.setBackgroundColor(Color.LIGHT_GRAY);
		c6.setBackgroundColor(Color.LIGHT_GRAY);
		c7.setBackgroundColor(Color.LIGHT_GRAY);
		aTable.addCell(c0);
		aTable.addCell(c1);
		
		aTable.addCell(c3);
		aTable.addCell(c4);
		aTable.addCell(c5);
		aTable.addCell(c6);
		aTable.addCell(c7);
		
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				CheckResult result=(CheckResult)list.get(i);
				String ip = result.getIp();
				String status = result.getStatus();
				Cell c00 = new Cell(i + 1 + "");
				Cell ipCell = new Cell(ip);
				String statusPath="";
				if (result.getCount0()>0||result.getCount1()>0||result.getCount2()>0) {
					statusPath=rootPath+"/img/error.PNG";
				}else {
					statusPath=rootPath+"/img/correct.PNG";
				}
				if (result.getStatus().equals("������")) {
					statusPath=rootPath+"/img/blue.PNG";
				}
				Image png0 =Image.getInstance(statusPath);
				Cell statusImg=new Cell(png0);
				Cell statusCell = new Cell(status);
				
				
				Cell vio0 = new Cell(result.getCount0()+"");
				Cell vio1 = new Cell(result.getCount1()+"");
				Cell vio2 = new Cell(result.getCount2()+"");
				Image vioPng0 =Image.getInstance(rootPath+"/img/common.PNG");
				Image vioPng1=Image.getInstance(rootPath+"/img/serious.PNG");
				Image vioPng2 =Image.getInstance(rootPath+"/img/urgency.PNG");
				Cell vioImg0 = new Cell(vioPng0);
				Cell vioImg1 = new Cell(vioPng1);
				Cell vioImg2 = new Cell(vioPng2);
				
				Image compPng =Image.getInstance(rootPath+"/img/correct.PNG");
				Cell compImg= new Cell(compPng);
				Cell compCell=new Cell(result.getExactCount()+"");
				
				Cell strategyCell = new Cell(result.getName());
				Cell timeCell = new Cell(result.getCheckTime());
			
				aTable.addCell(c00);
				aTable.addCell(ipCell);
				
				aTable.addCell(statusImg);
				aTable.addCell(statusCell);
				
				aTable.addCell(vioImg0);
				aTable.addCell(vio0);
				aTable.addCell(vioImg1);
				aTable.addCell(vio1);
				aTable.addCell(vioImg2);
				aTable.addCell(vio2);
				
				
				aTable.addCell(compImg);
				aTable.addCell(compCell);
				
				aTable.addCell(strategyCell);
				aTable.addCell(timeCell);

			}
		}
		document.add(aTable);
		

		document.close();

	}
public void createPdf(Vector<Integer> vector,List list,String filePath) throws DocumentException, IOException {
	Document document = new Document(PageSize.A4);
	// ����һ����д��(Writer)��document���������ͨ����д��(Writer)���Խ��ĵ�д�뵽������
	File file=new File(filePath);
	PdfWriter.getInstance(document, new FileOutputStream(file));
	document.open();
	// ������������
	BaseFont bfChinese = BaseFont.createFont("STSong-Light",
			"UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
	// ����������
	Font titleFont = new Font(bfChinese, 12, Font.BOLD);
	// com.lowagie.text.Font titleFont = new
	// com.lowagie.text.Font(bfChinese, 12, com.lowagie.text.Font.NORMAL);
	// ����������
	Font contextFont = new com.lowagie.text.Font(bfChinese, 11, Font.NORMAL);
	Paragraph title = new Paragraph("������������ͨ�ʱ���", titleFont);
	// ���ñ����ʽ���뷽ʽ
	title.setAlignment(Element.ALIGN_CENTER);
	// title.setFont(titleFont);
	document.add(title);
	document.add(new Paragraph("\n"));
	
	// ���� Table ���
	Font fontChinese = new Font(bfChinese, 12, Font.NORMAL, Color.black);
	String rootPath = ResourceCenter.getInstance().getSysPath();// ��ȡϵͳ�ļ���·��
	Image img =Image.getInstance(rootPath+"/resource/image/jfreechart/allDevice.png");
	img.setAbsolutePosition(0, 0);
	img.setAlignment(Image.LEFT);// ����ͼƬ��ʾλ��
	// document.add(img);
	Table imgTable = new Table(2);
	
	imgTable.setWidth(100); 
	Cell img1=new Cell(new Phrase("���в��Ե��豸��"+vector.get(0), fontChinese));
	Cell img2=new Cell(new Phrase("�Ϲ���Ե��豸��"+vector.get(1), fontChinese));
	Cell img3=new Cell(new Phrase("Υ�����Ե��豸��"+vector.get(2), fontChinese));
	Cell img4=new Cell(new Phrase("���ݲ������豸��"+vector.get(3), fontChinese));
	
	Cell img5=new Cell(img);
	img5.setRowspan(4);
	imgTable.addCell(img1);
	imgTable.addCell(img5);
	imgTable.addCell(img2);
	imgTable.addCell(img3);
	imgTable.addCell(img4);
	
	imgTable.setBorderWidth(0);
	document.add(imgTable);
	
	
	Table aTable = new Table(14);
	aTable.setBorderWidth(0);
	int width[] = { 30, 50,12,30 ,12,12,12,12,12,12, 12,30, 60, 60 };
	aTable.setWidths(width);
	aTable.setWidth(100); // ռҳ���� 90%
	aTable.setAlignment(Element.ALIGN_CENTER);// ������ʾ
	aTable.setAutoFillEmptyCells(true); // �Զ�����
	aTable.setPadding(2);// �ľ࣬��Ч����֪��ʲô��˼��
	aTable.setSpacing(0);// ����Ԫ��֮��ļ��
	//aTable.setBorder(2);// �߿�
	aTable.endHeaders();
	
	Cell c0 = new Cell("");
	c0.setBackgroundColor(Color.LIGHT_GRAY);
	
	Cell c1 = new Cell(new Phrase("IP��ַ", fontChinese));
	
	Cell c3 = new Cell(new Phrase("�Ϲ���״̬", fontChinese));
	c3.setColspan(2);
	
	Cell c4 = new Cell(new Phrase("Υ��������", fontChinese));
	c4.setColspan(6);
	Cell c5 = new Cell(new Phrase("�Ϲ��Թ�����", fontChinese));
	c5.setColspan(2);
	
	Cell c6 = null;
	c6 = new Cell(new Phrase("��������", fontChinese));
	Cell c7= new Cell(new Phrase("�ϴμ��ʱ��", fontChinese));
	
	
	c1.setBackgroundColor(Color.LIGHT_GRAY);
	c3.setBackgroundColor(Color.LIGHT_GRAY);
	c4.setBackgroundColor(Color.LIGHT_GRAY);
	c5.setBackgroundColor(Color.LIGHT_GRAY);
	c6.setBackgroundColor(Color.LIGHT_GRAY);
	c7.setBackgroundColor(Color.LIGHT_GRAY);
	aTable.addCell(c0);
	aTable.addCell(c1);
	
	aTable.addCell(c3);
	aTable.addCell(c4);
	aTable.addCell(c5);
	aTable.addCell(c6);
	aTable.addCell(c7);
	
	if (list != null && list.size() > 0) {
		for (int i = 0; i < list.size(); i++) {
			CheckResult result=(CheckResult)list.get(i);
			String ip = result.getIp();
			String status = result.getStatus();
			
			Cell c00 = new Cell(i + 1 + "");
			Cell ipCell = new Cell(ip);
			String statusPath="";
			if (result.getCount0()>0||result.getCount1()>0||result.getCount2()>0) {
				statusPath=rootPath+"/img/error.PNG";
			}else {
				statusPath=rootPath+"/img/correct.PNG";
			}
			if (result.getStatus().equals("������")) {
				statusPath=rootPath+"/img/blue.PNG";
			}
			Image png0 =Image.getInstance(statusPath);
			Cell statusImg=new Cell(png0);
			statusImg.setBorderColorRight(Color.WHITE);
			statusImg.setBorderWidthRight(0);
			statusImg.setBorderColorBottom(Color.GRAY);
			statusImg.setBorderWidthBottom(1);
			
			
			Cell statusCell = new Cell(new Phrase(status, fontChinese));
			statusCell.setBorderColorLeft(Color.WHITE);
			statusCell.setBorderWidthLeft(0);
			statusCell.setBorderColorRight(Color.BLACK);
			statusCell.setBorderWidthRight(1);
			statusCell.setBorderColorBottom(Color.GRAY);
			statusCell.setBorderWidthBottom(1);
			
			Cell vio0 = new Cell(result.getCount0()+"");
			vio0.setBorderColorRight(Color.WHITE);
			vio0.setBorderWidthRight(0);
			vio0.setBorderColorLeft(Color.WHITE);
			vio0.setBorderWidthLeft(0);
			vio0.setBorderColorBottom(Color.GRAY);
			vio0.setBorderWidthBottom(1);
			
			Cell vio1 = new Cell(result.getCount1()+"");
			vio1.setBorderColorRight(Color.WHITE);
			vio1.setBorderWidthRight(0);
			vio1.setBorderColorLeft(Color.WHITE);
			vio1.setBorderWidthLeft(0);
			vio1.setBorderColorBottom(Color.GRAY);
			vio1.setBorderWidthBottom(1);
			
			Cell vio2 = new Cell(result.getCount2()+"");
			vio2.setBorderColorLeft(Color.WHITE);
			vio2.setBorderWidthLeft(0);
			vio2.setBorderColorBottom(Color.GRAY);
			vio2.setBorderWidthBottom(1);
			
			Image vioPng0 =Image.getInstance(rootPath+"/img/common.PNG");
			Image vioPng1=Image.getInstance(rootPath+"/img/serious.PNG");
			Image vioPng2 =Image.getInstance(rootPath+"/img/urgency.PNG");
			Cell vioImg0 = new Cell(vioPng0);
			vioImg0.setBorderColorRight(Color.WHITE);
			vioImg0.setBorderWidthRight(0);
			vioImg0.setBorderColorBottom(Color.GRAY);
			vioImg0.setBorderWidthBottom(1);
			
			Cell vioImg1 = new Cell(vioPng1);
			vioImg1.setBorderColorRight(Color.WHITE);
			vioImg1.setBorderWidthRight(0);
			vioImg1.setBorderColorLeft(Color.WHITE);
			vioImg1.setBorderWidthLeft(0);
			vioImg1.setBorderColorBottom(Color.GRAY);
			vioImg1.setBorderWidthBottom(1);
			
			Cell vioImg2 = new Cell(vioPng2);
			vioImg2.setBorderColorRight(Color.WHITE);
			vioImg2.setBorderWidthRight(0);
			vioImg2.setBorderColorLeft(Color.WHITE);
			vioImg2.setBorderWidthLeft(0);
			vioImg2.setBorderColorBottom(Color.GRAY);
			vioImg2.setBorderWidthBottom(1);
			
			Image compPng =Image.getInstance(rootPath+"/img/correct.PNG");
			Cell compImg= new Cell(compPng);
			compImg.setBorderColorRight(Color.WHITE);
			compImg.setBorderWidthRight(0);
			compImg.setBorderColorLeft(Color.GRAY);
			compImg.setBorderWidthLeft(1);
			compImg.setBorderColorBottom(Color.GRAY);
			compImg.setBorderWidthBottom(1);
			
			Cell compCell=new Cell(result.getExactCount()+"");
			compCell.setBorderColorLeft(Color.WHITE);
			compCell.setBorderWidthLeft(0);
			compCell.setBorderColorBottom(Color.GRAY);
			compCell.setBorderWidthBottom(1);
			
			Cell strategyCell = new Cell(new Phrase(result.getName(), fontChinese));
			Cell timeCell = new Cell(result.getCheckTime());
			
			aTable.addCell(c00);
			aTable.addCell(ipCell);
			
			aTable.addCell(statusImg);
			aTable.addCell(statusCell);
			
			aTable.addCell(vioImg0);
			aTable.addCell(vio0);
			aTable.addCell(vioImg1);
			aTable.addCell(vio1);
			aTable.addCell(vioImg2);
			aTable.addCell(vio2);
			
			
			aTable.addCell(compImg);
			aTable.addCell(compCell);
			
			aTable.addCell(strategyCell);
			aTable.addCell(timeCell);
			
		}
	}
	document.add(aTable);
	
	
	document.close();
	
}
public void createExcel(Vector<Integer> vector,List list,String filePath) throws WriteException{

	String rootPath = ResourceCenter.getInstance().getSysPath();// ��ȡϵͳ�ļ���·��
	File file=new File(filePath);
		WritableWorkbook wb = null;
		WritableFont labelFont=null;
		WritableCellFormat labelFormat;
		 WritableCellFormat labelFormat1;
		 WritableCellFormat _labelFormat;
		 WritableCellFormat testFormat;
		 WritableCellFormat p_labelFormat;
		labelFont = new WritableFont(WritableFont.createFont("����"), 12, WritableFont.BOLD, false);
		labelFormat = new WritableCellFormat(labelFont);
		labelFormat1 = new WritableCellFormat(labelFont);
		_labelFormat = new WritableCellFormat();
		p_labelFormat = new WritableCellFormat();
		testFormat= new WritableCellFormat();
		try {
			_labelFormat.setShrinkToFit(true);
		} catch (WriteException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		labelFormat.setAlignment(Alignment.CENTRE);
		
		_labelFormat.setBackground(jxl.format.Colour.GRAY_25);
		_labelFormat.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN, jxl.format.Colour.WHITE);
		_labelFormat.setAlignment(Alignment.CENTRE);
		_labelFormat.setVerticalAlignment(VerticalAlignment.CENTRE);

		p_labelFormat.setShrinkToFit(true);
		p_labelFormat.setBackground(jxl.format.Colour.ICE_BLUE);
		p_labelFormat.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN, jxl.format.Colour.BLACK);
		p_labelFormat.setAlignment(Alignment.CENTRE);
		p_labelFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
		
		//testFormat.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN, jxl.format.Colour.WHITE);
		try {
			
			wb = Workbook.createWorkbook(file);

			WritableSheet sheet = wb.createSheet("��ͨ��ʹ��ͳ�Ʊ���", 0);
            sheet.setColumnView(1, 22);
            sheet.setColumnView(3, 25);
            sheet.setColumnView(6, 18);
           sheet.setRowView(1, 800);
           sheet.setRowView(2, 800);
           sheet.setRowView(3, 800);
           sheet.setRowView(4, 800);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			

			Label tmpLabel = null;
			tmpLabel = new Label(0, 0, "���ԺϹ��Ա���", labelFormat);
			sheet.addCell(tmpLabel);
			tmpLabel = new Label(0, 1, "���в��Ե��豸��"+vector.get(0), labelFormat1);
			sheet.addCell(tmpLabel);
			tmpLabel = new Label(0, 2, "�Ϲ���Ե��豸��"+vector.get(1) , labelFormat1);
			sheet.addCell(tmpLabel);
			tmpLabel = new Label(0, 3, "Υ�����Ե��豸��"+vector.get(2), labelFormat1);
			sheet.addCell(tmpLabel);
			tmpLabel = new Label(0, 4, "���ݲ������豸��"+vector.get(3), labelFormat1);
			sheet.addCell(tmpLabel);
			sheet.mergeCells(0, 0, 7, 0);
			sheet.mergeCells(0, 1, 3, 1);
			sheet.mergeCells(0, 2, 3, 2);
			sheet.mergeCells(0, 3, 3, 3);//(�У��У��ϲ��ĵ�Ԫ������)
			sheet.mergeCells(0, 4, 3, 4);//(�У��У��ϲ��ĵ�Ԫ������)
			tmpLabel = new Label(0, 6, "���", _labelFormat);
			sheet.addCell(tmpLabel);
			tmpLabel = new Label(1, 6, "IP��ַ", _labelFormat);
			sheet.addCell(tmpLabel);
			tmpLabel = new Label(2, 6, "�Ϲ���״̬", _labelFormat);
			sheet.addCell(tmpLabel);
			//sheet.mergeCells(2, 6, 3, 6);
			tmpLabel = new Label(3, 6, "Υ��������", _labelFormat);
			sheet.addCell(tmpLabel);
			tmpLabel = new Label(4, 6, "�Ϲ��Թ�����", _labelFormat);
			sheet.addCell(tmpLabel);
			tmpLabel = new Label(5, 6, "��������", _labelFormat);
			sheet.addCell(tmpLabel);
			tmpLabel = new Label(6, 6, "�ϴμ��ʱ��", _labelFormat);
			
			sheet.addCell(tmpLabel);
			int row = 5;
			File statusImg=new File(rootPath+"/img/common.png");
			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					// sheet.setRowView(7+i, 530);
					CheckResult result=(CheckResult)list.get(i);
					String ip = result.getIp();
					String status = result.getStatus();
					
					String statusPath="";
					if (result.getCount0()>0||result.getCount1()>0||result.getCount2()>0) {
						statusPath=rootPath+"/img/error.PNG";
					}else {
						statusPath=rootPath+"/img/correct.PNG";
					}
					if (result.getStatus().equals("������")) {
						statusPath=rootPath+"/img/blue.PNG";
					}
					String diStr="��ͨ:"+result.getCount0()+" ��Ҫ:"+result.getCount1()+" ����:"+result.getCount2();
					row = row + (i);
					tmpLabel = new Label(0, 7 + i, i + 1 + "", p_labelFormat);
					sheet.addCell(tmpLabel);
					tmpLabel = new Label(1, 7 + i, ip, p_labelFormat);
					sheet.addCell(tmpLabel);
					tmpLabel = new Label(2, 7 + i, status, p_labelFormat);
					sheet.addCell(tmpLabel);
					tmpLabel = new Label(3, 7 + i,diStr, p_labelFormat);
					sheet.addCell(tmpLabel);
					tmpLabel = new Label(4, 7 + i, result.getExactCount()+"", p_labelFormat);
					sheet.addCell(tmpLabel);
					tmpLabel = new Label(5, 7 + i, result.getName(), p_labelFormat);
					sheet.addCell(tmpLabel);
					tmpLabel = new Label(6, 7+ i, result.getCheckTime(), p_labelFormat);
					sheet.addCell(tmpLabel);
					
					//sheet.addImage(new WritableImage(2,7+i, 1, 1, statusImg));
				}
			}
			File file2=new File(rootPath+"/resource/image/jfreechart/allDevice.png");
			WritableImage writableImage=new WritableImage(3,1, 4, 5, file2);
			writableImage.setHeight(4);
			
			sheet.addImage(writableImage);
			
			wb.write();
		} catch (Exception e) {
			SysLogger.error("", e);
		} finally {
			try {
				if (wb != null)
					wb.close();
			} catch (Exception e) {
				SysLogger.error("", e);
			}
		}


	
}
}
