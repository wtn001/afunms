/**
 * <p>Description:create excel report,bridge pattern</p>
 * <p>Company: dhcc.com</p>
 * @author afunms
 * @project afunms
 * @date 2006-11-18
 */

package com.afunms.report.abstraction;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import jxl.CellView;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Colour;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableImage;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.jfree.chart.ChartUtilities;

import com.afunms.application.dao.DBDao;
import com.afunms.application.dao.DBTypeDao;
import com.afunms.application.dao.Db2spaceconfigDao;
import com.afunms.application.dao.InformixspaceconfigDao;
import com.afunms.application.dao.OraspaceconfigDao;
import com.afunms.application.dao.SqldbconfigDao;
import com.afunms.application.dao.SybspaceconfigDao;
import com.afunms.application.dao.WeblogicConfigDao;
import com.afunms.application.manage.IISManager;
import com.afunms.application.manage.WeblogicManager;
import com.afunms.application.model.DBTypeVo;
import com.afunms.application.model.DBVo;
import com.afunms.application.model.Db2spaceconfig;
import com.afunms.application.model.IISVo;
import com.afunms.application.model.Informixspaceconfig;
import com.afunms.application.model.MonitorDBDTO;
import com.afunms.application.model.MonitorMiddlewareDTO;
import com.afunms.application.model.Oraspaceconfig;
import com.afunms.application.model.Sqldbconfig;
import com.afunms.application.model.SybaseVO;
import com.afunms.application.model.Sybspaceconfig;
import com.afunms.application.model.TablesVO;
import com.afunms.application.model.WebConfig;
import com.afunms.application.model.WeblogicConfig;
import com.afunms.application.util.AssetHelper;
import com.afunms.application.weblogicmonitor.WeblogicHeap;
import com.afunms.application.weblogicmonitor.WeblogicJdbc;
import com.afunms.application.weblogicmonitor.WeblogicNormal;
import com.afunms.application.weblogicmonitor.WeblogicQueue;
import com.afunms.application.weblogicmonitor.WeblogicServer;
import com.afunms.application.weblogicmonitor.WeblogicSnmp;
import com.afunms.cabinet.dao.EqpRoomDao;
import com.afunms.cabinet.model.Cabinet;
import com.afunms.cabinet.model.EqpRoom;
import com.afunms.cabinet.model.EquipmentReport;
import com.afunms.cabinet.model.OperCabinet;
import com.afunms.capreport.model.StatisNumer;
import com.afunms.common.util.Arith;
import com.afunms.common.util.CEIString;
import com.afunms.common.util.ShareData;
import com.afunms.common.util.SysLogger;
import com.afunms.config.dao.DistrictDao;
import com.afunms.config.model.DistrictConfig;
import com.afunms.config.model.Macconfig;
import com.afunms.event.model.EventList;
import com.afunms.event.model.NetSyslog;
import com.afunms.event.model.NetSyslogEvent;
import com.afunms.event.model.NetSyslogViewer;
import com.afunms.initialize.ResourceCenter;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.base.Node;
import com.afunms.polling.node.IIS;
import com.afunms.polling.node.Tomcat;
import com.afunms.polling.node.Weblogic;
import com.afunms.polling.om.Devicecollectdata;
import com.afunms.polling.om.IpMac;
import com.afunms.polling.om.IpRouter;
import com.afunms.polling.om.Portconfig;
import com.afunms.polling.om.Softwarecollectdata;
import com.afunms.polling.om.Storagecollectdata;
import com.afunms.portscan.model.PortConfig;
import com.afunms.report.base.AbstractionReport1;
import com.afunms.report.base.AbstractionReport2;
import com.afunms.report.base.ImplementorReport1;
import com.afunms.report.base.ImplementorReport2;
import com.afunms.system.model.User;
import com.afunms.temp.model.FdbNodeTemp;
import com.afunms.temp.model.RouterNodeTemp;
import com.afunms.topology.dao.HostNodeDao;
import com.afunms.topology.dao.LinkDao;
import com.afunms.topology.dao.NodeMonitorDao;
import com.afunms.topology.model.HostNode;
import com.afunms.topology.model.Link;
import com.afunms.topology.model.MonitorNodeDTO;
import com.afunms.topology.model.NetDistrictIpDetail;
import com.afunms.topology.model.NodeMonitor;
import com.lowagie.text.BadElementException;
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
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.rtf.RtfWriter2;

public class ExcelReport2 extends AbstractionReport2 {
	private HttpServletRequest request;
	protected HttpSession session; 
	private Hashtable dataHash;

	public Hashtable getDataHash() {
		return dataHash;
	}

	public void setDataHash(Hashtable dataHash) {
		this.dataHash = dataHash;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public ExcelReport2(ImplementorReport2 impReport) {
		super(impReport);
	}

	public ExcelReport2(ImplementorReport2 impReport, Hashtable hash) {
		super(impReport);
		reportHash = hash;
	}
	public void createReport_cabinetPdf(String filename,String room)throws DocumentException, IOException {
		if (impReport.getTable() == null) {
			fileName = null;
			return;
		}
		try {
			// ����ֽ�Ŵ�С
			Document document = new Document(PageSize.A4);
			// ����һ����д��(Writer)��document���������ͨ����д��(Writer)���Խ��ĵ�д�뵽������
			PdfWriter.getInstance(document, new FileOutputStream(filename));
			document.open();
			// ������������new File(filename)
			BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
			// ����������
			Font titleFont = new Font(bfChinese, 12, Font.BOLD);
			// ����������
			Font contextFont = new Font(bfChinese, 12, Font.NORMAL);
//			Paragraph title = new Paragraph(room + "����ʹ�û��ܱ�", titleFont);
			// ���ñ����ʽ���뷽ʽ
//			title.setAlignment(Element.ALIGN_CENTER);
//			// title.setFont(titleFont);
//			document.add(title);
			List cabinetlist = (List) reportHash.get("cabinetlist");

//			Paragraph context = new Paragraph();
//			// ���ĸ�ʽ�����
//			context.setAlignment(Element.ALIGN_LEFT);
////			context.setFont(contextFont);
//			// ����һ���䣨���⣩�յ�����
//			context.setSpacingBefore(5);
//			// ���õ�һ�пյ�����
//			context.setFirstLineIndent(6);
//			document.add(context);
//			document.add(new Paragraph("\n"));
			PdfPTable aTable = new PdfPTable(6);
			PdfPCell cell = null;
			cell = new PdfPCell(new Phrase(room + "����ʹ�û��ܱ�", titleFont));
			cell.setColspan(6);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER); // ˮƽ����
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE); // ��ֱ����
			aTable.addCell(cell);
			cell = new PdfPCell(new Phrase("���", titleFont));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER); // ˮƽ����
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE); // ��ֱ����
			aTable.addCell(cell);
			cell = new PdfPCell(new Phrase("��������", titleFont));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER); // ˮƽ����
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE); // ��ֱ����
			aTable.addCell(cell);
			cell = new PdfPCell(new Phrase("��U��", titleFont));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER); // ˮƽ����
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE); // ��ֱ����
			aTable.addCell(cell);
			cell = new PdfPCell(new Phrase("����U��", titleFont));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER); // ˮƽ����
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE); // ��ֱ����
			aTable.addCell(cell);
			cell = new PdfPCell(new Phrase("����U��", titleFont));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER); // ˮƽ����
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE); // ��ֱ����
			aTable.addCell(cell);
			cell = new PdfPCell(new Phrase("Uʹ����(%)", titleFont));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER); // ˮƽ����
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE); // ��ֱ����
			aTable.addCell(cell);
			Cabinet cabinet=null;
			if (cabinetlist != null && cabinetlist.size() > 0) {
				for (int i = 0; i < cabinetlist.size(); i++) {

					cabinet = (Cabinet) cabinetlist.get(i);
					cell = new PdfPCell(new Phrase(1 + i + "",contextFont));
					cell.setHorizontalAlignment(Element.ALIGN_CENTER); // ˮƽ����
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE); // ��ֱ����
					aTable.addCell(cell);
					String cc=cabinet.getName();
					cell = new PdfPCell(new Phrase(cabinet.getName(),contextFont));
					cell.setHorizontalAlignment(Element.ALIGN_CENTER); // ˮƽ����
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE); // ��ֱ����
					aTable.addCell(cell);
					cell = new PdfPCell(new Phrase(cabinet.getAllu(),contextFont));
					cell.setHorizontalAlignment(Element.ALIGN_CENTER); // ˮƽ����
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE); // ��ֱ����
					aTable.addCell(cell);
					cell = new PdfPCell(new Phrase(cabinet.getUseu(),contextFont));
					cell.setHorizontalAlignment(Element.ALIGN_CENTER); // ˮƽ����
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE); // ��ֱ����
					aTable.addCell(cell);
					cell = new PdfPCell(new Phrase(cabinet.getTempu(),contextFont));
					cell.setHorizontalAlignment(Element.ALIGN_CENTER); // ˮƽ����
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE); // ��ֱ����
					aTable.addCell(cell);
					cell = new PdfPCell(new Phrase(cabinet.getRateu(),contextFont));
					cell.setHorizontalAlignment(Element.ALIGN_CENTER); // ˮƽ����
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE); // ��ֱ����
					aTable.addCell(cell);

				}
			}
			document.add(aTable);
			document.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	public void createReport_equipmentPdf(String filename,String roomname,String cabinetname,String uselect,String unumber,String rate)throws DocumentException, IOException {
		if (impReport.getTable() == null) {
			fileName = null;
			return;
		}
		try {
			// ����ֽ�Ŵ�С
			Document document = new Document(PageSize.A4);
			// ����һ����д��(Writer)��document���������ͨ����д��(Writer)���Խ��ĵ�д�뵽������
			PdfWriter.getInstance(document, new FileOutputStream(filename));
			document.open();
			// ������������new File(filename)
			BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
			// ����������
			Font titleFont = new Font(bfChinese, 12, Font.BOLD);
			// ����������
			Font contextFont = new Font(bfChinese, 12, Font.NORMAL);
//			Paragraph title = new Paragraph(room + "����ʹ�û��ܱ�", titleFont);
			// ���ñ����ʽ���뷽ʽ
//			title.setAlignment(Element.ALIGN_CENTER);
//			// title.setFont(titleFont);
//			document.add(title);
			List equipmentlist = (List) reportHash.get("equipmentlist");

//			Paragraph context = new Paragraph();
//			// ���ĸ�ʽ�����
//			context.setAlignment(Element.ALIGN_LEFT);
////			context.setFont(contextFont);
//			// ����һ���䣨���⣩�յ�����
//			context.setSpacingBefore(5);
//			// ���õ�һ�пյ�����
//			context.setFirstLineIndent(6);
//			document.add(context);
//			document.add(new Paragraph("\n"));
			PdfPTable aTable = new PdfPTable(6);
			PdfPCell cell = null;
			cell = new PdfPCell(new Phrase(roomname +cabinetname+ "ʹ����ϸ��", titleFont));
			cell.setColspan(6);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER); // ˮƽ����
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE); // ��ֱ����
			aTable.addCell(cell);
			
			cell = new PdfPCell(new Phrase("��������:"+roomname, titleFont));
			cell.setColspan(3);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT); // ˮƽ����
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE); // ��ֱ����
			aTable.addCell(cell);
			
			cell = new PdfPCell(new Phrase("������:"+cabinetname, titleFont));
			cell.setColspan(3);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT); // ˮƽ����
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE); // ��ֱ����
			aTable.addCell(cell);
			
			cell = new PdfPCell(new Phrase("U����:"+uselect, titleFont));
			cell.setColspan(2);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT); // ˮƽ����
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE); // ��ֱ����
			aTable.addCell(cell);

			
			cell = new PdfPCell(new Phrase("��ʹ��:"+unumber, titleFont));
			cell.setColspan(2);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT); // ˮƽ����
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE); // ��ֱ����
			aTable.addCell(cell);

			
			cell = new PdfPCell(new Phrase("ʹ����:"+rate, titleFont));
			cell.setColspan(2);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT); // ˮƽ����
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE); // ��ֱ����
			aTable.addCell(cell);
	

			

			
			
			
			cell = new PdfPCell(new Phrase("���", titleFont));

			cell.setHorizontalAlignment(Element.ALIGN_CENTER); // ˮƽ����
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE); // ��ֱ����
			aTable.addCell(cell);

			cell = new PdfPCell(new Phrase("�豸����", titleFont));

			cell.setHorizontalAlignment(Element.ALIGN_CENTER); // ˮƽ����
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE); // ��ֱ����
			aTable.addCell(cell);
	
			cell = new PdfPCell(new Phrase("�豸����", titleFont));


			cell.setHorizontalAlignment(Element.ALIGN_CENTER); // ˮƽ����
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE); // ��ֱ����
			aTable.addCell(cell);
		
			cell = new PdfPCell(new Phrase("����ҵ��", titleFont));

	
			cell.setHorizontalAlignment(Element.ALIGN_CENTER); // ˮƽ����
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE); // ��ֱ����
			aTable.addCell(cell);
			
			cell = new PdfPCell(new Phrase("��ϵ��", titleFont));

	
			cell.setHorizontalAlignment(Element.ALIGN_CENTER); // ˮƽ����
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE); // ��ֱ����
			aTable.addCell(cell);
			
			cell = new PdfPCell(new Phrase("��ϵ�绰", titleFont));

	
			cell.setHorizontalAlignment(Element.ALIGN_CENTER); // ˮƽ����
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE); // ��ֱ����
			aTable.addCell(cell);
			EquipmentReport equipmentReport=null;
			if (equipmentlist != null && equipmentlist.size() > 0) {
				for (int i = 0; i < equipmentlist.size(); i++) {

					equipmentReport = (EquipmentReport) equipmentlist.get(i);
					cell = new PdfPCell(new Phrase(1 + i + "",contextFont));
		
					cell.setHorizontalAlignment(Element.ALIGN_CENTER); // ˮƽ����
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE); // ��ֱ����
					aTable.addCell(cell);
					cell = new PdfPCell(new Phrase(equipmentReport.getEquipmentname(),contextFont));
			
					cell.setHorizontalAlignment(Element.ALIGN_CENTER); // ˮƽ����
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE); // ��ֱ����
					aTable.addCell(cell);
					
					cell = new PdfPCell(new Phrase(equipmentReport.getEquipmentdesc(),contextFont));
				
					cell.setHorizontalAlignment(Element.ALIGN_CENTER); // ˮƽ����
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE); // ��ֱ����
					aTable.addCell(cell);
					cell = new PdfPCell(new Phrase(equipmentReport.getOperation(),contextFont));
				
					cell.setHorizontalAlignment(Element.ALIGN_CENTER); // ˮƽ����
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE); // ��ֱ����
					aTable.addCell(cell);
					cell = new PdfPCell(new Phrase(equipmentReport.getContactname(),contextFont));
				
					cell.setHorizontalAlignment(Element.ALIGN_CENTER); // ˮƽ����
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE); // ��ֱ����
					aTable.addCell(cell);
					cell = new PdfPCell(new Phrase(equipmentReport.getContactphone(),contextFont));
				
					cell.setHorizontalAlignment(Element.ALIGN_CENTER); // ˮƽ����
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE); // ��ֱ����
					aTable.addCell(cell);


				}
			}
			document.add(aTable);
			document.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public void createReport_OperEquipmentPdf(String filename,String opername,String contactname,String contactphone)throws DocumentException, IOException {
		if (impReport.getTable() == null) {
			fileName = null;
			return;
		}
		try {
			// ����ֽ�Ŵ�С
			Document document = new Document(PageSize.A4);
			// ����һ����д��(Writer)��document���������ͨ����д��(Writer)���Խ��ĵ�д�뵽������
			PdfWriter.getInstance(document, new FileOutputStream(filename));
			document.open();
			// ������������new File(filename)
			BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
			// ����������
			Font titleFont = new Font(bfChinese, 12, Font.BOLD);
			// ����������
			Font contextFont = new Font(bfChinese, 12, Font.NORMAL);
//			Paragraph title = new Paragraph(room + "����ʹ�û��ܱ�", titleFont);
			// ���ñ����ʽ���뷽ʽ
//			title.setAlignment(Element.ALIGN_CENTER);
//			// title.setFont(titleFont);
//			document.add(title);
			List equipmentlist = (List) reportHash.get("equipmentlist");

//			Paragraph context = new Paragraph();
//			// ���ĸ�ʽ�����
//			context.setAlignment(Element.ALIGN_LEFT);
////			context.setFont(contextFont);
//			// ����һ���䣨���⣩�յ�����
//			context.setSpacingBefore(5);
//			// ���õ�һ�пյ�����
//			context.setFirstLineIndent(6);
//			document.add(context);
//			document.add(new Paragraph("\n"));
			PdfPTable aTable = new PdfPTable(7);
			PdfPCell cell = null;
			cell = new PdfPCell(new Phrase(opername+ "ҵ�����ʹ�÷ֲ����ܱ�", titleFont));
			cell.setColspan(7);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER); // ˮƽ����
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE); // ��ֱ����
			aTable.addCell(cell);
			
			
			cell = new PdfPCell(new Phrase("ҵ������:"+opername, titleFont));
			cell.setColspan(2);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT); // ˮƽ����
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE); // ��ֱ����
			aTable.addCell(cell);

			
			cell = new PdfPCell(new Phrase("��ϵ��:"+contactname, titleFont));
			cell.setColspan(2);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT); // ˮƽ����
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE); // ��ֱ����
			aTable.addCell(cell);

			
			cell = new PdfPCell(new Phrase("��ϵ�绰:"+contactphone, titleFont));
			cell.setColspan(3);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT); // ˮƽ����
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE); // ��ֱ����
			aTable.addCell(cell);
	

			

			
			
			
			cell = new PdfPCell(new Phrase("��������", titleFont));

			cell.setHorizontalAlignment(Element.ALIGN_CENTER); // ˮƽ����
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE); // ��ֱ����
			aTable.addCell(cell);

			cell = new PdfPCell(new Phrase("��������", titleFont));

			cell.setHorizontalAlignment(Element.ALIGN_CENTER); // ˮƽ����
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE); // ��ֱ����
			aTable.addCell(cell);
	
			cell = new PdfPCell(new Phrase("U��Ԫ", titleFont));


			cell.setHorizontalAlignment(Element.ALIGN_CENTER); // ˮƽ����
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE); // ��ֱ����
			aTable.addCell(cell);
		
			cell = new PdfPCell(new Phrase("�豸����", titleFont));

	
			cell.setHorizontalAlignment(Element.ALIGN_CENTER); // ˮƽ����
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE); // ��ֱ����
			aTable.addCell(cell);
			
			cell = new PdfPCell(new Phrase("IP��ַ", titleFont));

	
			cell.setHorizontalAlignment(Element.ALIGN_CENTER); // ˮƽ����
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE); // ��ֱ����
			aTable.addCell(cell);
			
			cell = new PdfPCell(new Phrase("��ϵ��", titleFont));

	
			cell.setHorizontalAlignment(Element.ALIGN_CENTER); // ˮƽ����
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE); // ��ֱ����
			aTable.addCell(cell);
			cell = new PdfPCell(new Phrase("��ϵ�绰", titleFont));

			
			cell.setHorizontalAlignment(Element.ALIGN_CENTER); // ˮƽ����
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE); // ��ֱ����
			aTable.addCell(cell);
			OperCabinet operCabinet=null;
			if (equipmentlist != null && equipmentlist.size() > 0) {
				for (int i = 0; i < equipmentlist.size(); i++) {

					operCabinet = (OperCabinet) equipmentlist.get(i);
					cell = new PdfPCell(new Phrase(operCabinet.getRoomname(),contextFont));
		
					cell.setHorizontalAlignment(Element.ALIGN_CENTER); // ˮƽ����
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE); // ��ֱ����
					aTable.addCell(cell);
					cell = new PdfPCell(new Phrase(operCabinet.getCabinetname(),contextFont));
			
					cell.setHorizontalAlignment(Element.ALIGN_CENTER); // ˮƽ����
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE); // ��ֱ����
					aTable.addCell(cell);
					
					cell = new PdfPCell(new Phrase(operCabinet.getUseu(),contextFont));
				
					cell.setHorizontalAlignment(Element.ALIGN_CENTER); // ˮƽ����
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE); // ��ֱ����
					aTable.addCell(cell);
					cell = new PdfPCell(new Phrase(operCabinet.getEquipmentname(),contextFont));
				
					cell.setHorizontalAlignment(Element.ALIGN_CENTER); // ˮƽ����
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE); // ��ֱ����
					aTable.addCell(cell);
					cell = new PdfPCell(new Phrase(operCabinet.getIpaddress(),contextFont));
				
					cell.setHorizontalAlignment(Element.ALIGN_CENTER); // ˮƽ����
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE); // ��ֱ����
					aTable.addCell(cell);
					cell = new PdfPCell(new Phrase(operCabinet.getContactname(),contextFont));
				
					cell.setHorizontalAlignment(Element.ALIGN_CENTER); // ˮƽ����
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE); // ��ֱ����
					aTable.addCell(cell);
					cell = new PdfPCell(new Phrase(operCabinet.getContactphone(),contextFont));
					
					cell.setHorizontalAlignment(Element.ALIGN_CENTER); // ˮƽ����
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE); // ��ֱ����
					aTable.addCell(cell);


				}
			}
			document.add(aTable);
			document.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	public void createReport_cabinetWord(String filename,String room)throws DocumentException, IOException {
		if (impReport.getTable() == null) {
			fileName = null;
			return;
		}
		try {
			// ����ֽ�Ŵ�С
			Document document = new Document(PageSize.A4);
			// ����һ����д��(Writer)��document���������ͨ����д��(Writer)���Խ��ĵ�д�뵽������
			RtfWriter2.getInstance(document, new FileOutputStream(filename));
			document.open();
			// ������������new File(filename)
			BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
			// ����������
			Font titleFont = new Font(bfChinese, 12, Font.BOLD);
			// ����������
			Font contextFont = new Font(bfChinese, 12, Font.NORMAL);
			// title.setFont(titleFont);
			List cabinetlist = (List) reportHash.get("cabinetlist");

			Paragraph context = new Paragraph();
			// ���ĸ�ʽ�����
			context.setAlignment(Element.ALIGN_CENTER);
//			context.setFont(contextFont);
			// ����һ���䣨���⣩�յ�����
//			context.setSpacingBefore(0);
			// ���õ�һ�пյ�����
//			context.setFirstLineIndent(6);
//			document.add(context);
//			document.add(new Paragraph("\n"));
			Table aTable = new Table(6);
//			float[] widths = { 220f, 220f, 220f, 110f, 110f, 110f, 110f, 220f };
//			aTable.setWidths(widths);
			aTable.setWidth(100); // ռҳ���� 90%
			aTable.setAlignment(Element.ALIGN_CENTER);// ������ʾ
			aTable.setAutoFillEmptyCells(true); // �Զ�����
//			aTable.setBorderWidth(1); // �߿���
//			aTable.setBorderColor(new Color(0, 125, 255)); // �߿���ɫ
//			aTable.setPadding(2);// �ľ࣬��Ч����֪��ʲô��˼��
//			aTable.setSpacing(4);// ����Ԫ��֮��ļ��
//			aTable.setBorder(2);// �߿�
			Cell cell = null;
			cell = new Cell(new Phrase(room + "����ʹ�û��ܱ�", titleFont));
			cell.setRowspan(2);
			cell.setColspan(6);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER); // ˮƽ����
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE); // ��ֱ����
			aTable.addCell(cell);
			cell = new Cell(new Phrase("���", titleFont));
			cell.setRowspan(2);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER); // ˮƽ����
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE); // ��ֱ����
			aTable.addCell(cell);

			cell = new Cell(new Phrase("��������", titleFont));
			cell.setRowspan(2);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER); // ˮƽ����
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE); // ��ֱ����
			aTable.addCell(cell);
	
			cell = new Cell(new Phrase("��U��", titleFont));
			cell.setRowspan(2);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER); // ˮƽ����
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE); // ��ֱ����
			aTable.addCell(cell);
		
			cell = new Cell(new Phrase("����U��", titleFont));
			cell.setRowspan(2);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER); // ˮƽ����
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE); // ��ֱ����
			aTable.addCell(cell);
			
			cell = new Cell(new Phrase("����U��", titleFont));
			cell.setRowspan(2);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER); // ˮƽ����
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE); // ��ֱ����
			aTable.addCell(cell);
			
			cell = new Cell(new Phrase("Uʹ����(%)", titleFont));
			cell.setRowspan(2);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER); // ˮƽ����
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE); // ��ֱ����
			aTable.addCell(cell);
			Cabinet cabinet=null;
			if (cabinetlist != null && cabinetlist.size() > 0) {
				for (int i = 0; i < cabinetlist.size(); i++) {

					cabinet = (Cabinet) cabinetlist.get(i);
					cell = new Cell(new Phrase(1 + i + "",contextFont));
					cell.setRowspan(2);
					cell.setHorizontalAlignment(Element.ALIGN_CENTER); // ˮƽ����
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE); // ��ֱ����
					aTable.addCell(cell);
					cell = new Cell(new Phrase(cabinet.getName(),contextFont));
					cell.setRowspan(2);
					cell.setHorizontalAlignment(Element.ALIGN_CENTER); // ˮƽ����
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE); // ��ֱ����
					aTable.addCell(cell);
					
					cell = new Cell(new Phrase(cabinet.getAllu(),contextFont));
					cell.setRowspan(2);
					cell.setHorizontalAlignment(Element.ALIGN_CENTER); // ˮƽ����
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE); // ��ֱ����
					aTable.addCell(cell);
					cell = new Cell(new Phrase(cabinet.getUseu(),contextFont));
					cell.setRowspan(2);
					cell.setHorizontalAlignment(Element.ALIGN_CENTER); // ˮƽ����
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE); // ��ֱ����
					aTable.addCell(cell);
					cell = new Cell(new Phrase(cabinet.getTempu(),contextFont));
					cell.setRowspan(2);
					cell.setHorizontalAlignment(Element.ALIGN_CENTER); // ˮƽ����
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE); // ��ֱ����
					aTable.addCell(cell);
					cell = new Cell(new Phrase(cabinet.getRateu(),contextFont));
					cell.setRowspan(2);
					cell.setHorizontalAlignment(Element.ALIGN_CENTER); // ˮƽ����
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE); // ��ֱ����
					aTable.addCell(cell);

				}
			}
			document.add(aTable);
			document.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	public void createReport_equipmentWord(String filename,String roomname,String cabinetname,String uselect,String unumber,String rate)throws DocumentException, IOException {
		if (impReport.getTable() == null) {
			fileName = null;
			return;
		}
		try {
			// ����ֽ�Ŵ�С
			Document document = new Document(PageSize.A4);
			// ����һ����д��(Writer)��document���������ͨ����д��(Writer)���Խ��ĵ�д�뵽������
			RtfWriter2.getInstance(document, new FileOutputStream(filename));
			document.open();
			// ������������new File(filename)
			BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
			// ����������
			Font titleFont = new Font(bfChinese, 12, Font.BOLD);
			// ����������
			Font contextFont = new Font(bfChinese, 12, Font.NORMAL);
			// title.setFont(titleFont);
			List equipmentlist = (List) reportHash.get("equipmentlist");

			Paragraph context = new Paragraph();
			// ���ĸ�ʽ�����
			context.setAlignment(Element.ALIGN_CENTER);
//			context.setFont(contextFont);
			// ����һ���䣨���⣩�յ�����
//			context.setSpacingBefore(0);
			// ���õ�һ�пյ�����
//			context.setFirstLineIndent(6);
//			document.add(context);
//			document.add(new Paragraph("\n"));
			Table aTable = new Table(6);
//			float[] widths = { 220f, 220f, 220f, 110f, 110f, 110f, 110f, 220f };
//			aTable.setWidths(widths);
			aTable.setWidth(100); // ռҳ���� 90%
			aTable.setAlignment(Element.ALIGN_CENTER);// ������ʾ
			aTable.setAutoFillEmptyCells(true); // �Զ�����
//			aTable.setBorderWidth(1); // �߿���
//			aTable.setBorderColor(new Color(0, 125, 255)); // �߿���ɫ
//			aTable.setPadding(2);// �ľ࣬��Ч����֪��ʲô��˼��
//			aTable.setSpacing(4);// ����Ԫ��֮��ļ��
//			aTable.setBorder(2);// �߿�
			Cell cell = null;
			cell = new Cell(new Phrase(roomname +cabinetname+ "ʹ����ϸ��", titleFont));
			cell.setColspan(6);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER); // ˮƽ����
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE); // ��ֱ����
			aTable.addCell(cell);
			
			
			cell = new Cell(new Phrase("��������:"+roomname, titleFont));
			cell.setColspan(3);
			this.setCellFormat(cell, false);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT); // ˮƽ����
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE); // ��ֱ����
			aTable.addCell(cell);
			
			cell = new Cell(new Phrase("������:"+cabinetname, titleFont));
			cell.setColspan(3);
			this.setCellFormat(cell, false);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT); // ˮƽ����
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE); // ��ֱ����
			aTable.addCell(cell);
		
			
			cell = new Cell(new Phrase("U����:"+uselect, titleFont));
			cell.setColspan(2);
			this.setCellFormat(cell, false);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT); // ˮƽ����
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE); // ��ֱ����
			aTable.addCell(cell);
	
			
			cell = new Cell(new Phrase("��ʹ��:"+unumber, titleFont));
			cell.setColspan(2);
			this.setCellFormat(cell, false);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT); // ˮƽ����
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE); // ��ֱ����
			aTable.addCell(cell);
			
			cell = new Cell(new Phrase("ʹ����:"+rate, titleFont));
			cell.setColspan(2);
			this.setCellFormat(cell, false);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT); // ˮƽ����
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE); // ��ֱ����
			aTable.addCell(cell);
	

			

			
			
			
			cell = new Cell(new Phrase("���", titleFont));
	
			cell.setHorizontalAlignment(Element.ALIGN_CENTER); // ˮƽ����
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE); // ��ֱ����
			aTable.addCell(cell);

			cell = new Cell(new Phrase("�豸����", titleFont));
	
			cell.setHorizontalAlignment(Element.ALIGN_CENTER); // ˮƽ����
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE); // ��ֱ����
			aTable.addCell(cell);
	
			cell = new Cell(new Phrase("�豸����", titleFont));
	

			cell.setHorizontalAlignment(Element.ALIGN_CENTER); // ˮƽ����
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE); // ��ֱ����
			aTable.addCell(cell);
		
			cell = new Cell(new Phrase("����ҵ��", titleFont));
	
	
			cell.setHorizontalAlignment(Element.ALIGN_CENTER); // ˮƽ����
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE); // ��ֱ����
			aTable.addCell(cell);
			
			cell = new Cell(new Phrase("��ϵ��", titleFont));

	
			cell.setHorizontalAlignment(Element.ALIGN_CENTER); // ˮƽ����
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE); // ��ֱ����
			aTable.addCell(cell);
			
			cell = new Cell(new Phrase("��ϵ�绰", titleFont));

	
			cell.setHorizontalAlignment(Element.ALIGN_CENTER); // ˮƽ����
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE); // ��ֱ����
			aTable.addCell(cell);
			EquipmentReport equipmentReport=null;
			if (equipmentlist != null && equipmentlist.size() > 0) {
				for (int i = 0; i < equipmentlist.size(); i++) {

					equipmentReport = (EquipmentReport) equipmentlist.get(i);
					cell = new Cell(new Phrase(1 + i + "",contextFont));

					cell.setHorizontalAlignment(Element.ALIGN_CENTER); // ˮƽ����
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE); // ��ֱ����
					aTable.addCell(cell);
					cell = new Cell(new Phrase(equipmentReport.getEquipmentname(),contextFont));
		
					cell.setHorizontalAlignment(Element.ALIGN_CENTER); // ˮƽ����
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE); // ��ֱ����
					aTable.addCell(cell);
					
					cell = new Cell(new Phrase(equipmentReport.getEquipmentdesc(),contextFont));
		
					cell.setHorizontalAlignment(Element.ALIGN_CENTER); // ˮƽ����
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE); // ��ֱ����
					aTable.addCell(cell);
					cell = new Cell(new Phrase(equipmentReport.getOperation(),contextFont));
			
					cell.setHorizontalAlignment(Element.ALIGN_CENTER); // ˮƽ����
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE); // ��ֱ����
					aTable.addCell(cell);
					cell = new Cell(new Phrase(equipmentReport.getContactname(),contextFont));
			
					cell.setHorizontalAlignment(Element.ALIGN_CENTER); // ˮƽ����
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE); // ��ֱ����
					aTable.addCell(cell);
					cell = new Cell(new Phrase(equipmentReport.getContactphone(),contextFont));
		
					cell.setHorizontalAlignment(Element.ALIGN_CENTER); // ˮƽ����
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE); // ��ֱ����
					aTable.addCell(cell);

				}
			}
			document.add(aTable);
			document.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	public void createReport_OperEquipmentWord(String filename,String opername,String contactname,String contactphone)throws DocumentException, IOException {
		if (impReport.getTable() == null) {
			fileName = null;
			return;
		}
		try {
			// ����ֽ�Ŵ�С
			Document document = new Document(PageSize.A4);
			// ����һ����д��(Writer)��document���������ͨ����д��(Writer)���Խ��ĵ�д�뵽������
			RtfWriter2.getInstance(document, new FileOutputStream(filename));
			document.open();
			// ������������new File(filename)
			BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
			// ����������
			Font titleFont = new Font(bfChinese, 12, Font.BOLD);
			// ����������
			Font contextFont = new Font(bfChinese, 12, Font.NORMAL);
			// title.setFont(titleFont);
			List equipmentlist = (List) reportHash.get("equipmentlist");

			Paragraph context = new Paragraph();
			// ���ĸ�ʽ�����
			context.setAlignment(Element.ALIGN_CENTER);
//			context.setFont(contextFont);
			// ����һ���䣨���⣩�յ�����
//			context.setSpacingBefore(0);
			// ���õ�һ�пյ�����
//			context.setFirstLineIndent(6);
//			document.add(context);
//			document.add(new Paragraph("\n"));
			Table aTable = new Table(7);
//			float[] widths = { 220f, 220f, 220f, 110f, 110f, 110f, 110f, 220f };
//			aTable.setWidths(widths);
			aTable.setWidth(100); // ռҳ���� 90%
			aTable.setAlignment(Element.ALIGN_CENTER);// ������ʾ
			aTable.setAutoFillEmptyCells(true); // �Զ�����
//			aTable.setBorderWidth(1); // �߿���
//			aTable.setBorderColor(new Color(0, 125, 255)); // �߿���ɫ
//			aTable.setPadding(2);// �ľ࣬��Ч����֪��ʲô��˼��
//			aTable.setSpacing(4);// ����Ԫ��֮��ļ��
//			aTable.setBorder(2);// �߿�
			Cell cell = null;
			cell = new Cell(new Phrase(opername+ "ҵ�����ʹ�÷ֲ����ܱ�", titleFont));
			cell.setColspan(7);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER); // ˮƽ����
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE); // ��ֱ����
			aTable.addCell(cell);
			
		
			
			cell = new Cell(new Phrase("ҵ������:"+opername, titleFont));
			cell.setColspan(2);
			this.setCellFormat(cell, false);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT); // ˮƽ����
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE); // ��ֱ����
			aTable.addCell(cell);
	
			
			cell = new Cell(new Phrase("��ϵ��:"+contactname, titleFont));
			cell.setColspan(2);
			this.setCellFormat(cell, false);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT); // ˮƽ����
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE); // ��ֱ����
			aTable.addCell(cell);
			
			cell = new Cell(new Phrase("��ϵ�绰:"+contactphone, titleFont));
			cell.setColspan(3);
			this.setCellFormat(cell, false);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT); // ˮƽ����
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE); // ��ֱ����
			aTable.addCell(cell);
	

			

			
			
			
			cell = new Cell(new Phrase("��������", titleFont));
	
			cell.setHorizontalAlignment(Element.ALIGN_CENTER); // ˮƽ����
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE); // ��ֱ����
			aTable.addCell(cell);

			cell = new Cell(new Phrase("��������", titleFont));
	
			cell.setHorizontalAlignment(Element.ALIGN_CENTER); // ˮƽ����
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE); // ��ֱ����
			aTable.addCell(cell);
	
			cell = new Cell(new Phrase("U��Ԫ", titleFont));
	

			cell.setHorizontalAlignment(Element.ALIGN_CENTER); // ˮƽ����
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE); // ��ֱ����
			aTable.addCell(cell);
		
			cell = new Cell(new Phrase("�豸����", titleFont));
	
	
			cell.setHorizontalAlignment(Element.ALIGN_CENTER); // ˮƽ����
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE); // ��ֱ����
			aTable.addCell(cell);
			
			cell = new Cell(new Phrase("IP��ַ", titleFont));

	
			cell.setHorizontalAlignment(Element.ALIGN_CENTER); // ˮƽ����
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE); // ��ֱ����
			aTable.addCell(cell);
			
			cell = new Cell(new Phrase("��ϵ��", titleFont));

	
			cell.setHorizontalAlignment(Element.ALIGN_CENTER); // ˮƽ����
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE); // ��ֱ����
			aTable.addCell(cell);
			
			cell = new Cell(new Phrase("��ϵ�绰", titleFont));

			
			cell.setHorizontalAlignment(Element.ALIGN_CENTER); // ˮƽ����
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE); // ��ֱ����
			aTable.addCell(cell);
			OperCabinet operCabinet=null;
			if (equipmentlist != null && equipmentlist.size() > 0) {
				for (int i = 0; i < equipmentlist.size(); i++) {

					operCabinet = (OperCabinet) equipmentlist.get(i);
					cell = new Cell(new Phrase(operCabinet.getRoomname(),contextFont));

					cell.setHorizontalAlignment(Element.ALIGN_CENTER); // ˮƽ����
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE); // ��ֱ����
					aTable.addCell(cell);
					cell = new Cell(new Phrase(operCabinet.getCabinetname(),contextFont));
		
					cell.setHorizontalAlignment(Element.ALIGN_CENTER); // ˮƽ����
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE); // ��ֱ����
					aTable.addCell(cell);
					
					cell = new Cell(new Phrase(operCabinet.getUseu(),contextFont));
		
					cell.setHorizontalAlignment(Element.ALIGN_CENTER); // ˮƽ����
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE); // ��ֱ����
					aTable.addCell(cell);
					cell = new Cell(new Phrase(operCabinet.getEquipmentname(),contextFont));
			
					cell.setHorizontalAlignment(Element.ALIGN_CENTER); // ˮƽ����
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE); // ��ֱ����
					aTable.addCell(cell);
					cell = new Cell(new Phrase(operCabinet.getIpaddress(),contextFont));
			
					cell.setHorizontalAlignment(Element.ALIGN_CENTER); // ˮƽ����
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE); // ��ֱ����
					aTable.addCell(cell);
					cell = new Cell(new Phrase(operCabinet.getCabinetname(),contextFont));
		
					cell.setHorizontalAlignment(Element.ALIGN_CENTER); // ˮƽ����
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE); // ��ֱ����
					aTable.addCell(cell);
					cell = new Cell(new Phrase(operCabinet.getContactphone(),contextFont));
					
					cell.setHorizontalAlignment(Element.ALIGN_CENTER); // ˮƽ����
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE); // ��ֱ����
					aTable.addCell(cell);

				}
			}
			document.add(aTable);
			document.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public void createReport_Allcabinet(String filename) {
		if (impReport.getTable() == null) {
			fileName = null;
			return;
		}
		
		//TODO
		WritableWorkbook wb = null;
		try {
			// fileName = ResourceCenter.getInstance().getSysPath() +
			// "temp\\dhcnms_report.xls";
			// fileName = CommonAppUtil.getAppName() +
			// "/temp/hostnms_report.xls";
			fileName = ResourceCenter.getInstance().getSysPath() + filename;
			wb = Workbook.createWorkbook(new File(fileName));
			List list=new ArrayList();
			EqpRoomDao dao=new EqpRoomDao();
			try {	
			list=dao.loadAll();
			} catch (Exception e) {
				
			}finally {
				dao.close();
			}
			List cabinetlist = (List) reportHash.get("cabinetlist");
			for(int j=0;j<cabinetlist.size();j++){
				String room = ((EqpRoom) list.get(j)).getName();
			WritableSheet sheet = wb.createSheet(room+"����ʹ�û��ܱ�", 0);
			WritableFont labelFont = new WritableFont(WritableFont.createFont("����"), 12, WritableFont.BOLD, false);
			WritableCellFormat labelFormat = new WritableCellFormat(labelFont);

			WritableCellFormat _labelFormat = new WritableCellFormat();
			_labelFormat.setBackground(jxl.format.Colour.GRAY_25);

			WritableCellFormat p_labelFormat = new WritableCellFormat();
			p_labelFormat.setBackground(jxl.format.Colour.WHITE);

			WritableCellFormat b_labelFormat = new WritableCellFormat();
			b_labelFormat.setBackground(jxl.format.Colour.WHITE);
			
			Integer index = (Integer) reportHash.get("startRow");
			int startRow = 1;
			if (null != index) {
				startRow = index.intValue();
			}
			
			Label tmpLabel = null;

			// ��ռ���Ϣ
			tmpLabel = new Label(2, 0, room+"����ʹ�û��ܱ�", b_labelFormat);
			sheet.addCell(tmpLabel);
			tmpLabel = new Label(0, 1, "���", _labelFormat);
			sheet.addCell(tmpLabel);
			tmpLabel = new Label(1, 1, "��������", _labelFormat);
			sheet.addCell(tmpLabel);
			tmpLabel = new Label(2, 1, "��U��", _labelFormat);
			sheet.addCell(tmpLabel);
			tmpLabel = new Label(3, 1, "����U��", _labelFormat);
			sheet.addCell(tmpLabel);
			tmpLabel = new Label(4, 1, "����U��", _labelFormat);
			sheet.addCell(tmpLabel);
			tmpLabel = new Label(5, 1, "Uʹ���ʣ�%��", _labelFormat);
			sheet.addCell(tmpLabel);
			Cabinet cabinet = null;
			List  cabinetlist1=(List)cabinetlist.get(j);
			for (int i = 0; i < cabinetlist1.size(); i++) {	
				cabinet = (Cabinet) cabinetlist1.get(i);
				// row = row + (i);
				tmpLabel = new Label(0, 2 + i, 1 + i + "", p_labelFormat);
				sheet.addCell(tmpLabel);
				tmpLabel = new Label(1, 2 + i, cabinet.getName(), p_labelFormat);
				sheet.addCell(tmpLabel);
				tmpLabel = new Label(2, 2 + i, cabinet.getAllu(), p_labelFormat);
				sheet.addCell(tmpLabel);
				tmpLabel = new Label(3, 2 + i, cabinet.getUseu(), p_labelFormat);
				sheet.addCell(tmpLabel);
				tmpLabel = new Label(4, 2 + i, cabinet.getTempu(), p_labelFormat);
				sheet.addCell(tmpLabel);
				tmpLabel = new Label(5, 2 + i, cabinet.getRateu() + "", p_labelFormat);
				sheet.addCell(tmpLabel);
			}
			}
			wb.write();
		} catch (Exception e) {
			SysLogger.error("", e);
		} finally {
			try {
				if (wb != null)
					wb.close();
			} catch (Exception e) {
			}
		}
	}

	public void createReport_OperEquipment(String filename,String opername,String contactname,String contactphone) {
		if (impReport.getTable() == null) {
			fileName = null;
			return;
		}
		
		//TODO
		WritableWorkbook wb = null;
		try {
			// fileName = ResourceCenter.getInstance().getSysPath() +
			// "temp\\dhcnms_report.xls";
			// fileName = CommonAppUtil.getAppName() +
			// "/temp/hostnms_report.xls";
			fileName = ResourceCenter.getInstance().getSysPath() + filename;
			wb = Workbook.createWorkbook(new File(fileName));
			WritableSheet sheet = wb.createSheet(opername+"ҵ�����ʹ�÷ֲ����ܱ�", 0);
			WritableFont labelFont = new WritableFont(WritableFont.createFont("����"), 12, WritableFont.BOLD, false);
			WritableCellFormat labelFormat = new WritableCellFormat(labelFont);

			WritableCellFormat _labelFormat = new WritableCellFormat();
			_labelFormat.setBackground(jxl.format.Colour.GRAY_25);

			WritableCellFormat p_labelFormat = new WritableCellFormat();
			p_labelFormat.setBackground(jxl.format.Colour.WHITE);

			WritableCellFormat b_labelFormat = new WritableCellFormat();
			b_labelFormat.setBackground(jxl.format.Colour.WHITE);
			List equipmentlist = (List) reportHash.get("equipmentlist");
			Integer index = (Integer) reportHash.get("startRow");
			int startRow = 1;
			if (null != index) {
				startRow = index.intValue();
			}
			
			Label tmpLabel = null;

			// ��ռ���Ϣ
			tmpLabel = new Label(2, 0, opername+"ҵ�����ʹ�÷ֲ����ܱ�", b_labelFormat);
			sheet.addCell(tmpLabel);

			tmpLabel = new Label(0, 1, "ҵ������", p_labelFormat);
			sheet.addCell(tmpLabel);
			tmpLabel = new Label(1, 1, opername, p_labelFormat);
			sheet.addCell(tmpLabel);
			tmpLabel = new Label(2, 1, "��ϵ��", p_labelFormat);
			sheet.addCell(tmpLabel);
			tmpLabel = new Label(3, 1, contactname, p_labelFormat);
			sheet.addCell(tmpLabel);
			tmpLabel = new Label(4, 1, "��ϵ�绰", p_labelFormat);
			sheet.addCell(tmpLabel);
			tmpLabel = new Label(5, 1, contactphone, p_labelFormat);
			sheet.addCell(tmpLabel);
			
			
			
			
			tmpLabel = new Label(0, 2, "��������", _labelFormat);
			sheet.addCell(tmpLabel);
			tmpLabel = new Label(1, 2, "��������", _labelFormat);
			sheet.addCell(tmpLabel);
			tmpLabel = new Label(2, 2, "U��Ԫ", _labelFormat);
			sheet.addCell(tmpLabel);
			tmpLabel = new Label(3, 2, "�豸����", _labelFormat);
			sheet.addCell(tmpLabel);
			tmpLabel = new Label(4, 2, "IP��ַ", _labelFormat);
			sheet.addCell(tmpLabel);
			tmpLabel = new Label(5, 2, "��ϵ��", _labelFormat);
			sheet.addCell(tmpLabel);
			tmpLabel = new Label(6, 2, "��ϵ�绰", _labelFormat);
			sheet.addCell(tmpLabel);
			OperCabinet operCabinet = null;
			for (int i = 0; i < equipmentlist.size(); i++) {	
				operCabinet = (OperCabinet) equipmentlist.get(i);
				tmpLabel = new Label(0, 3 + i, operCabinet.getRoomname(), p_labelFormat);
				sheet.addCell(tmpLabel);
				tmpLabel = new Label(1, 3 + i, operCabinet.getCabinetname(), p_labelFormat);
				sheet.addCell(tmpLabel);
				tmpLabel = new Label(2, 3 + i, operCabinet.getUseu(), p_labelFormat);
				sheet.addCell(tmpLabel);
				tmpLabel = new Label(3, 3 + i, operCabinet.getEquipmentname(), p_labelFormat);
				sheet.addCell(tmpLabel);
				tmpLabel = new Label(4, 3 + i, operCabinet.getIpaddress(), p_labelFormat);
				sheet.addCell(tmpLabel);
				tmpLabel = new Label(5, 3 + i, operCabinet.getContactname(), p_labelFormat);
				sheet.addCell(tmpLabel);
				tmpLabel = new Label(6, 3 + i, operCabinet.getContactphone(), p_labelFormat);
				sheet.addCell(tmpLabel);
			}
			wb.write();
		} catch (Exception e) {
			SysLogger.error("", e);
		} finally {
			try {
				if (wb != null)
					wb.close();
			} catch (Exception e) {
			}
		}
	}

	public void createReport_cabinet(String filename,String room) {
		if (impReport.getTable() == null) {
			fileName = null;
			return;
		}
		
		//TODO
		WritableWorkbook wb = null;
		try {
			// fileName = ResourceCenter.getInstance().getSysPath() +
			// "temp\\dhcnms_report.xls";
			// fileName = CommonAppUtil.getAppName() +
			// "/temp/hostnms_report.xls";
			fileName = ResourceCenter.getInstance().getSysPath() + filename;
			wb = Workbook.createWorkbook(new File(fileName));
			WritableSheet sheet = wb.createSheet(room+"����ʹ�û��ܱ�", 0);
			WritableFont labelFont = new WritableFont(WritableFont.createFont("����"), 12, WritableFont.BOLD, false);
			WritableCellFormat labelFormat = new WritableCellFormat(labelFont);

			WritableCellFormat _labelFormat = new WritableCellFormat();
			_labelFormat.setBackground(jxl.format.Colour.GRAY_25);

			WritableCellFormat p_labelFormat = new WritableCellFormat();
			p_labelFormat.setBackground(jxl.format.Colour.WHITE);

			WritableCellFormat b_labelFormat = new WritableCellFormat();
			b_labelFormat.setBackground(jxl.format.Colour.WHITE);
			List cabinetlist = (List) reportHash.get("cabinetlist");
			Integer index = (Integer) reportHash.get("startRow");
			int startRow = 1;
			if (null != index) {
				startRow = index.intValue();
			}
			
			Label tmpLabel = null;

			// ��ռ���Ϣ
			tmpLabel = new Label(2, 0, room+"����ʹ�û��ܱ�", b_labelFormat);
			sheet.addCell(tmpLabel);
			tmpLabel = new Label(0, 1, "���", _labelFormat);
			sheet.addCell(tmpLabel);
			tmpLabel = new Label(1, 1, "��������", _labelFormat);
			sheet.addCell(tmpLabel);
			tmpLabel = new Label(2, 1, "��U��", _labelFormat);
			sheet.addCell(tmpLabel);
			tmpLabel = new Label(3, 1, "����U��", _labelFormat);
			sheet.addCell(tmpLabel);
			tmpLabel = new Label(4, 1, "����U��", _labelFormat);
			sheet.addCell(tmpLabel);
			tmpLabel = new Label(5, 1, "Uʹ���ʣ�%��", _labelFormat);
			sheet.addCell(tmpLabel);
			Cabinet cabinet = null;
			for (int i = 0; i < cabinetlist.size(); i++) {	
				cabinet = (Cabinet) cabinetlist.get(i);
				// row = row + (i);
				tmpLabel = new Label(0, 2 + i, 1 + i + "", p_labelFormat);
				sheet.addCell(tmpLabel);
				tmpLabel = new Label(1, 2 + i, cabinet.getName(), p_labelFormat);
				sheet.addCell(tmpLabel);
				tmpLabel = new Label(2, 2 + i, cabinet.getAllu(), p_labelFormat);
				sheet.addCell(tmpLabel);
				tmpLabel = new Label(3, 2 + i, cabinet.getUseu(), p_labelFormat);
				sheet.addCell(tmpLabel);
				tmpLabel = new Label(4, 2 + i, cabinet.getTempu() + "U", p_labelFormat);
				sheet.addCell(tmpLabel);
				tmpLabel = new Label(5, 2 + i, cabinet.getRateu() + "", p_labelFormat);
				sheet.addCell(tmpLabel);
			}
			wb.write();
		} catch (Exception e) {
			SysLogger.error("", e);
		} finally {
			try {
				if (wb != null)
					wb.close();
			} catch (Exception e) {
			}
		}
	}

	public void createReport_equipment(String filename,String roomname,String cabinetname,String uselect,String unumber,String rate) {
		if (impReport.getTable() == null) {
			fileName = null;
			return;
		}
		
		//TODO
		WritableWorkbook wb = null;
		try {
			// fileName = ResourceCenter.getInstance().getSysPath() +
			// "temp\\dhcnms_report.xls";
			// fileName = CommonAppUtil.getAppName() +
			// "/temp/hostnms_report.xls";
			fileName = ResourceCenter.getInstance().getSysPath() + filename;
			wb = Workbook.createWorkbook(new File(fileName));
			WritableSheet sheet = wb.createSheet(roomname+cabinetname+"ʹ����ϸ��", 0);
			WritableFont labelFont = new WritableFont(WritableFont.createFont("����"), 12, WritableFont.BOLD, false);
			WritableCellFormat labelFormat = new WritableCellFormat(labelFont);

			WritableCellFormat _labelFormat = new WritableCellFormat();
			_labelFormat.setBackground(jxl.format.Colour.GRAY_25);

			WritableCellFormat p_labelFormat = new WritableCellFormat();
			p_labelFormat.setBackground(jxl.format.Colour.WHITE);

			WritableCellFormat b_labelFormat = new WritableCellFormat();
			b_labelFormat.setBackground(jxl.format.Colour.WHITE);
			List equipmentlist = (List) reportHash.get("equipmentlist");
			Integer index = (Integer) reportHash.get("startRow");
			int startRow = 1;
			if (null != index) {
				startRow = index.intValue();
			}
			
			Label tmpLabel = null;

			// ��ռ���Ϣ
			tmpLabel = new Label(2, 0, roomname+cabinetname+"ʹ����ϸ��", b_labelFormat);
			sheet.addCell(tmpLabel);
			tmpLabel = new Label(0, 1, "��������", _labelFormat);
			sheet.addCell(tmpLabel);
			tmpLabel = new Label(1, 1, roomname, p_labelFormat);
			sheet.addCell(tmpLabel);
			tmpLabel = new Label(2, 1, "������", p_labelFormat);
			sheet.addCell(tmpLabel);
			tmpLabel = new Label(3, 1, cabinetname, p_labelFormat);
			sheet.addCell(tmpLabel);
			tmpLabel = new Label(0, 2, "U����", p_labelFormat);
			sheet.addCell(tmpLabel);
			tmpLabel = new Label(1, 2, uselect, p_labelFormat);
			sheet.addCell(tmpLabel);
			tmpLabel = new Label(2, 2, "��ʹ��", p_labelFormat);
			sheet.addCell(tmpLabel);
			tmpLabel = new Label(3, 2, unumber, p_labelFormat);
			sheet.addCell(tmpLabel);
			tmpLabel = new Label(4, 2, "ʹ����", p_labelFormat);
			sheet.addCell(tmpLabel);
			tmpLabel = new Label(5, 2, rate, p_labelFormat);
			sheet.addCell(tmpLabel);
			
			
			
			
			tmpLabel = new Label(0, 3, "U���", _labelFormat);
			sheet.addCell(tmpLabel);
			tmpLabel = new Label(1, 3, "�豸����", _labelFormat);
			sheet.addCell(tmpLabel);
			tmpLabel = new Label(2, 3, "�豸����", _labelFormat);
			sheet.addCell(tmpLabel);
			tmpLabel = new Label(3, 3, "����ҵ��", _labelFormat);
			sheet.addCell(tmpLabel);
			tmpLabel = new Label(4, 3, "��ϵ��", _labelFormat);
			sheet.addCell(tmpLabel);
			tmpLabel = new Label(5, 3, "��ϵ�绰", _labelFormat);
			sheet.addCell(tmpLabel);
			EquipmentReport equipmentReport = null;
			for (int i = 0; i < equipmentlist.size(); i++) {	
				equipmentReport = (EquipmentReport) equipmentlist.get(i);
				// row = row + (i);
				tmpLabel = new Label(0, 4 + i, 1 + i + "", p_labelFormat);
				sheet.addCell(tmpLabel);
				tmpLabel = new Label(1, 4 + i, equipmentReport.getEquipmentname(), p_labelFormat);
				sheet.addCell(tmpLabel);
				tmpLabel = new Label(2, 4 + i, equipmentReport.getEquipmentdesc(), p_labelFormat);
				sheet.addCell(tmpLabel);
				tmpLabel = new Label(3, 4 + i, equipmentReport.getOperation(), p_labelFormat);
				sheet.addCell(tmpLabel);
				tmpLabel = new Label(4, 4 + i, equipmentReport.getContactname(), p_labelFormat);
				sheet.addCell(tmpLabel);
				tmpLabel = new Label(5, 4 + i, equipmentReport.getContactphone(), p_labelFormat);
				sheet.addCell(tmpLabel);
			}
			wb.write();
		} catch (Exception e) {
			SysLogger.error("", e);
		} finally {
			try {
				if (wb != null)
					wb.close();
			} catch (Exception e) {
			}
		}
	}
}