/**
 * <p>Description:DiscoverManager</p>
 * <p>Company: dhcc.com</p>
 * @author afunms
 * @project afunms
 * @date 2006-08-12
 */

package com.afunms.capreport.manage;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableImage;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYAreaRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.time.Day;
import org.jfree.data.time.Hour;
import org.jfree.data.time.Minute;
import org.jfree.data.time.Month;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.xy.XYDataset;

import com.afunms.application.util.DbConversionUtil;
import com.afunms.application.util.ReportExport;
import com.afunms.application.util.ReportHelper;
import com.afunms.capreport.dao.SubscribeResourcesDao;
import com.afunms.capreport.dao.UtilReportDao;
import com.afunms.capreport.model.ReportValue;
import com.afunms.capreport.model.SubscribeResources;
import com.afunms.capreport.model.UtilReport;
import com.afunms.common.base.BaseManager;
import com.afunms.common.base.DaoInterface;
import com.afunms.common.base.ErrorMessage;
import com.afunms.common.base.ManagerInterface;
import com.afunms.common.util.CEIString;
import com.afunms.common.util.ChartGraph;
import com.afunms.common.util.CommonUtil;
import com.afunms.common.util.DBManager;
import com.afunms.common.util.DateE;
import com.afunms.common.util.PingUtil;
import com.afunms.common.util.SessionConstant;
import com.afunms.common.util.ShareData;
import com.afunms.common.util.SysLogger;
import com.afunms.common.util.SysUtil;
import com.afunms.common.util.SystemConstant;
import com.afunms.config.dao.BusinessDao;
import com.afunms.config.dao.PortconfigDao;
import com.afunms.detail.net.service.NetService;
import com.afunms.detail.service.cpuInfo.CpuInfoService;
import com.afunms.detail.service.memoryInfo.MemoryInfoService;
import com.afunms.detail.service.pingInfo.PingInfoService;
import com.afunms.event.dao.EventListDao;
import com.afunms.event.model.EventList;
import com.afunms.indicators.model.NodeDTO;
import com.afunms.indicators.util.NodeUtil;
import com.afunms.initialize.ResourceCenter;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.api.I_HostCollectData;
import com.afunms.polling.api.I_HostCollectDataDay;
import com.afunms.polling.api.I_HostLastCollectData;
import com.afunms.polling.impl.HostCollectDataDayManager;
import com.afunms.polling.impl.HostCollectDataManager;
import com.afunms.polling.impl.HostLastCollectDataManager;
import com.afunms.polling.impl.Hostcollectdata;
import com.afunms.polling.impl.IpResourceReport;
import com.afunms.polling.node.ArrayInfo;
import com.afunms.polling.node.Battery;
import com.afunms.polling.node.Controller;
import com.afunms.polling.node.CtrlPort;
import com.afunms.polling.node.DIMM;
import com.afunms.polling.node.Disk;
import com.afunms.polling.node.Enclosure;
import com.afunms.polling.node.EnclosureFru;
import com.afunms.polling.node.Host;
import com.afunms.polling.node.Lun;
import com.afunms.polling.node.Port;
import com.afunms.polling.node.Processor;
import com.afunms.polling.node.SubSystemInfo;
import com.afunms.polling.node.VFP;
import com.afunms.polling.om.AllUtilHdx;
import com.afunms.polling.om.Buffercollectdata;
import com.afunms.polling.om.CPUcollectdata;
import com.afunms.polling.om.DiscardsPerc;
import com.afunms.polling.om.Diskcollectdata;
import com.afunms.polling.om.ErrorsPerc;
import com.afunms.polling.om.Flashcollectdata;
import com.afunms.polling.om.InPkts;
import com.afunms.polling.om.Interfacecollectdata;
import com.afunms.polling.om.IpMac;
import com.afunms.polling.om.IpMacBase;
import com.afunms.polling.om.IpMacChange;
import com.afunms.polling.om.IpRouter;
import com.afunms.polling.om.Memorycollectdata;
import com.afunms.polling.om.OutPkts;
import com.afunms.polling.om.Packs;
import com.afunms.polling.om.Pingcollectdata;
import com.afunms.polling.om.Processcollectdata;
import com.afunms.polling.om.UtilHdx;
import com.afunms.polling.om.UtilHdxPerc;
import com.afunms.report.abstraction.ExcelReport1;
import com.afunms.report.base.AbstractionReport1;
import com.afunms.report.jfree.ChartCreator;
import com.afunms.report.jfree.JFreeChartBrother;
import com.afunms.system.model.User;
import com.afunms.system.util.UserView;
import com.afunms.topology.dao.HostNodeDao;
import com.afunms.topology.dao.IpMacChangeDao;
import com.afunms.topology.model.HostNode;
import com.ibm.db2.jcc.b.p;
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

import cz.dhl.ui.CoTabbed;

/**
 * ����һ���ڵ㣬Ҫ��server.jsp����һ���ڵ�;
 * ɾ�������һ���ڵ���Ϣ��������ֱ�Ӳ���server.jsp,����������updateXml������xml.
 */
public class NetCapReportManager extends BaseManager implements ManagerInterface {
	DateE datemanager = new DateE();
	SimpleDateFormat sdf0 = new SimpleDateFormat("yyyy-MM-dd");
	I_HostCollectData hostmanager = new HostCollectDataManager();
	I_HostLastCollectData hostlastmanager = new HostLastCollectDataManager();
	SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private String list() {
		Date d = new Date();
		String startdate = getParaValue("startdate");
		if (startdate == null) {
			startdate = sdf0.format(d);
		}
		String todate = getParaValue("todate");
		if (todate == null) {
			todate = sdf0.format(d);
		}
		request.setAttribute("startdate", startdate);
		request.setAttribute("todate", todate);
		HostNodeDao dao = new HostNodeDao();
		request.setAttribute("list", dao.loadNetwork(1));
		return "/capreport/net/list.jsp";
	}

	private String find() {
		int netflag = 0;
		Date d = new Date();
		String startdate = getParaValue("startdate");
		if (startdate == null) {
			startdate = sdf0.format(d);
		}
		String todate = getParaValue("todate");
		String bid = getParaValue("bidtext");
		netflag = getParaIntValue("netflag");
		if (todate == null) {
			todate = sdf0.format(d);
		}
		request.setAttribute("startdate", startdate);
		request.setAttribute("todate", todate);
		HostNodeDao dao = new HostNodeDao();
		request.setAttribute("list", dao.loadNetworkByBid(1, bid));
		if (netflag == 1) {
			return "/capreport/net/list.jsp";
		} else if (netflag == 2) {
			return "/capreport/net/neteventlist.jsp";
		} else if (netflag == 3) {
			return "/capreport/net/netmultilist.jsp";
		} else
			return "/capreport/net/list.jsp";
	}

	private String eventlist() {
		Date d = new Date();
		String startdate = getParaValue("startdate");
		if (startdate == null) {
			startdate = sdf0.format(d);
		}
		String todate = getParaValue("todate");
		if (todate == null) {
			todate = sdf0.format(d);
		}
		request.setAttribute("startdate", startdate);
		request.setAttribute("todate", todate);
		HostNodeDao dao = new HostNodeDao();
		request.setAttribute("list", dao.loadNetwork(1));
		return "/capreport/net/neteventlist.jsp";
	}

	private String netmultilist() {
		Date d = new Date();
		String startdate = getParaValue("startdate");
		if (startdate == null) {
			startdate = sdf0.format(d);
		}
		String todate = getParaValue("todate");
		if (todate == null) {
			todate = sdf0.format(d);
		}
		request.setAttribute("startdate", startdate);
		request.setAttribute("todate", todate);
		HostNodeDao dao = new HostNodeDao();
		request.setAttribute("list", dao.loadNetwork(1));
		return "/capreport/net/netmultilist.jsp";
	}

	private String netping() {
		Date d = new Date();
		String startdate = getParaValue("startdate");
		if (startdate == null) {
			startdate = sdf0.format(d);
		}
		String todate = getParaValue("todate");
		if (todate == null) {
			todate = sdf0.format(d);
		}
		String starttime = startdate + " 00:00:00";
		String totime = todate + " 23:59:59";

		String[] ids = getParaArrayValue("checkbox");

		// �������־ȡ���˿����¼�¼���б�
		String orderflag = "ipaddress";
		if (getParaValue("orderflag") != null && !getParaValue("orderflag").equals("")) {
			orderflag = getParaValue("orderflag");
		}

		List orderList = new ArrayList();
		if (ids != null && ids.length > 0) {
			for (int i = 0; i < ids.length; i++) {
				HostNodeDao dao = new HostNodeDao();
				HostNode node = dao.loadHost(Integer.parseInt(ids[i]));

				Hashtable pinghash = new Hashtable();
				try {
					pinghash = hostmanager.getCategory(node.getIpAddress(), "Ping", "ConnectUtilization", starttime, totime);
				} catch (Exception e) {
					e.printStackTrace();
				}
				Hashtable responsehash = new Hashtable();
				try {
					responsehash = hostmanager.getCategory(node.getIpAddress(), "Ping", "ResponseTime", starttime, totime);
				} catch (Exception e) {
					e.printStackTrace();
				}
				Hashtable ipmemhash = new Hashtable();
				ipmemhash.put("hostnode", node);
				ipmemhash.put("pinghash", pinghash);
				ipmemhash.put("responsehash", responsehash);
				ipmemhash.put("ipaddress", node.getIpAddress() + "(" + node.getAlias() + ")");
				orderList.add(ipmemhash);
			}

		}
		List returnList = new ArrayList();
		if (orderflag.equalsIgnoreCase("avgping") || orderflag.equalsIgnoreCase("downnum") || orderflag.equalsIgnoreCase("responseavg") || orderflag.equalsIgnoreCase("responsemax")) {
			returnList = (List) session.getAttribute("pinglist");
			orderList = (ArrayList) session.getAttribute("orderList");
		} else {
			// ��orderList����theValue��������

			// **********************************************************
			List pinglist = orderList;
			if (pinglist != null && pinglist.size() > 0) {
				for (int i = 0; i < pinglist.size(); i++) {
					Hashtable _pinghash = (Hashtable) pinglist.get(i);
					HostNode node = (HostNode) _pinghash.get("hostnode");
					Hashtable pinghash = (Hashtable) _pinghash.get("pinghash");
					Hashtable responsehash = (Hashtable) _pinghash.get("responsehash");
					if (pinghash == null)
						continue;
					String equname = node.getAlias();
					String ip = node.getIpAddress();

					String pingconavg = "";
					String downnum = "";
					String responseavg = "";
					String responsemax = "";
					if (pinghash.get("avgpingcon") != null)
						pingconavg = (String) pinghash.get("avgpingcon");
					if (pinghash.get("downnum") != null)
						downnum = (String) pinghash.get("downnum");
					// ��ȡ��Ӧʱ��
					if (responsehash.get("avgpingcon") != null)
						responseavg = (String) responsehash.get("avgpingcon");
					if (responsehash.get("pingmax") != null)
						responsemax = (String) responsehash.get("pingmax");

					List ipdiskList = new ArrayList();
					ipdiskList.add(ip);
					ipdiskList.add(equname);
					ipdiskList.add(node.getType());
					ipdiskList.add(pingconavg);
					ipdiskList.add(downnum);
					ipdiskList.add(responseavg);
					ipdiskList.add(responsemax);
					returnList.add(ipdiskList);
				}
			}
		}
		// **********************************************************

		List list = new ArrayList();
		if (returnList != null && returnList.size() > 0) {
			for (int m = 0; m < returnList.size(); m++) {
				List ipdiskList = (List) returnList.get(m);
				for (int n = m + 1; n < returnList.size(); n++) {
					List _ipdiskList = (List) returnList.get(n);
					if (orderflag.equalsIgnoreCase("ipaddress")) {
					} else if (orderflag.equalsIgnoreCase("avgping")) {
						String avgping = "";
						if (ipdiskList.get(3) != null) {
							avgping = (String) ipdiskList.get(3);
						}
						String _avgping = "";
						if (ipdiskList.get(3) != null) {
							_avgping = (String) _ipdiskList.get(3);
						}
						if (new Double(avgping.substring(0, avgping.length() - 2)).doubleValue() < new Double(_avgping.substring(0, _avgping.length() - 2)).doubleValue()) {
							returnList.remove(m);
							returnList.add(m, _ipdiskList);
							returnList.remove(n);
							returnList.add(n, ipdiskList);
							ipdiskList = _ipdiskList;
							_ipdiskList = null;
						}
					} else if (orderflag.equalsIgnoreCase("downnum")) {
						String downnum = "";
						if (ipdiskList.get(4) != null) {
							downnum = (String) ipdiskList.get(4);
						}
						String _downnum = "";
						if (ipdiskList.get(4) != null) {
							_downnum = (String) _ipdiskList.get(4);
						}
						if (new Double(downnum).doubleValue() < new Double(_downnum).doubleValue()) {
							returnList.remove(m);
							returnList.add(m, _ipdiskList);
							returnList.remove(n);
							returnList.add(n, ipdiskList);
							ipdiskList = _ipdiskList;
							_ipdiskList = null;
						}
					} else if (orderflag.equalsIgnoreCase("responseavg")) {
						String avgping = "";
						if (ipdiskList.get(5) != null) {
							avgping = (String) ipdiskList.get(5);
						}
						String _avgping = "";
						if (ipdiskList.get(5) != null) {
							_avgping = (String) _ipdiskList.get(5);
						}
						if (new Double(avgping.substring(0, avgping.length() - 2)).doubleValue() < new Double(_avgping.substring(0, _avgping.length() - 2)).doubleValue()) {
							returnList.remove(m);
							returnList.add(m, _ipdiskList);
							returnList.remove(n);
							returnList.add(n, ipdiskList);
							ipdiskList = _ipdiskList;
							_ipdiskList = null;
						}
					} else if (orderflag.equalsIgnoreCase("responsemax")) {
						String downnum = "";
						if (ipdiskList.get(6) != null) {
							downnum = (String) ipdiskList.get(6);
						}
						String _downnum = "";
						if (ipdiskList.get(6) != null) {
							_downnum = (String) _ipdiskList.get(6);
						}
						if (new Double(downnum).doubleValue() < new Double(_downnum).doubleValue()) {
							returnList.remove(m);
							returnList.add(m, _ipdiskList);
							returnList.remove(n);
							returnList.add(n, ipdiskList);
							ipdiskList = _ipdiskList;
							_ipdiskList = null;
						}
					}
				}
				// �õ�������Subentity���б�
				list.add(ipdiskList);
				ipdiskList = null;
			}
		}
		String pingChartDivStr = ReportHelper.getChartDivStr(orderList, "ping");
		String responsetimeDivStr = ReportHelper.getChartDivStr(orderList, "responsetime");
		// ��ͨ��
		ReportValue pingReportValue = ReportHelper.getReportValue(orderList, "ping");
		ReportValue responseReportValue = ReportHelper.getReportValue(orderList, "responsetime");
		String pingpath = new ReportExport().makeJfreeChartData(pingReportValue.getListValue(), pingReportValue.getIpList(), "��ͨ��", "ʱ��", "");
		String responsetimepath = new ReportExport().makeJfreeChartData(responseReportValue.getListValue(), responseReportValue.getIpList(), "��Ӧʱ��", "ʱ��", "");

		request.setAttribute("responsetimeDivStr", responsetimeDivStr);
		request.setAttribute("pingChartDivStr", pingChartDivStr);
		request.setAttribute("starttime", starttime);
		request.setAttribute("totime", totime);
		session.setAttribute("pingpath", pingpath);
		session.setAttribute("responsetimepath", responsetimepath);
		session.setAttribute("orderList", orderList);
		session.setAttribute("pinglist", list);
		request.setAttribute("startdate", startdate);
		request.setAttribute("todate", todate);
		request.setAttribute("pinglist", list);
		return "/capreport/net/netping.jsp";
	}

	// /**
	// * ��ȡ��Ӧʱ��ͼ��chart���ַ���
	// * @param orderList
	// * @return
	// */
	// private String getResonsetimeChartDivStr(List orderList){
	// List pingList = new ArrayList();
	// List pingipList = new ArrayList();
	// for(int i=0; i < orderList.size(); i++){
	// Hashtable tempHash = (Hashtable)orderList.get(i);
	// Hashtable responsetimeHash = null;//��ͨ��hash
	// if(tempHash.containsKey("responsehash")){
	// responsetimeHash = (Hashtable)tempHash.get("responsehash");
	// }
	// //ȡ����ͨ�ʵ�����ֵ
	// List<Vector> dataList = null;
	// if(responsetimeHash.containsKey("list")){
	// dataList = (List<Vector>)responsetimeHash.get("list");
	// }
	// pingList.add(dataList);
	// pingipList.add(tempHash.get("ipaddress"));
	// }
	// AmChartTool amChartTool = new AmChartTool();
	// String pingdata = amChartTool.makeAmChartData(pingList, pingipList);
	// return pingdata;
	// }

	/**
	 * ���������豸�ɼ�����ͨ�ʺ���Ӧʱ��ȵļ��ϣ���ȡjfreechart�ӿ�����Ҫ�����ݼ���
	 * 
	 * @param orderList
	 * @return
	 * @throws ParseException
	 */
	private Hashtable<String, Object> getChartDataHash(List orderList) throws ParseException {
		Hashtable<String, Object> retHash = new Hashtable<String, Object>();
		if (orderList == null) {
			return null;
		}
		// SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

		double[][] datas = null;// ���ݶ�ά����
		String[] rowKeys = null;// ���ߵ����Ƶ�����
		String columnKeys[] = null;// �е�Ԫ������Ƶ�����
		datas = new double[orderList.size()][];
		for (int i = 0; i < orderList.size(); i++) {
			Hashtable tempHash = (Hashtable) orderList.get(i);
			Hashtable pinghash = null;// ��ͨ��hash
			// Hashtable responsehash = null;//��Ӧʱ��hash
			if (tempHash.containsKey("pinghash")) {
				pinghash = (Hashtable) tempHash.get("pinghash");
			}
			// if(tempHash.containsKey("responsehash")){
			// responsehash = (Hashtable)tempHash.get("responsehash");
			// }
			// ȡ����ͨ�ʵ�����ֵ
			List<Vector> dataList = null;
			if (pinghash.containsKey("list")) {
				dataList = (List<Vector>) pinghash.get("list");
			}
			if (rowKeys == null) {
				rowKeys = new String[orderList.size()];
			}
			double[] data = new double[dataList.size()];
			if (columnKeys == null) {
				columnKeys = new String[dataList.size()];
			}
			if (dataList != null) {
				for (int j = 0; j < dataList.size(); j++) {
					Vector tempPingVector = (Vector) dataList.get(j);
					String value = (String) tempPingVector.get(0);// value
					data[j] = Double.valueOf(value);
					String collecttime = (String) tempPingVector.get(1);// collecttime
					// tempPingVector.get(2);//unit
					columnKeys[j] = collecttime;
					// Date date = sdf.parse(collecttime);
					// columnKeys[j] = String.valueOf("");
					if (tempHash.containsKey("ipaddress")) {
						rowKeys[i] = (String) tempHash.get("ipaddress");
					}
				}
			}
			datas[i] = data;
		}
		retHash.put("datas", datas);
		retHash.put("rowKeys", rowKeys);
		retHash.put("columnKeys", columnKeys);
		return retHash;
	}

	private String netevent() {
		Date d = new Date();
		String startdate = getParaValue("startdate");
		if (startdate == null) {
			startdate = sdf0.format(d);
		}
		String todate = getParaValue("todate");
		if (todate == null) {
			todate = sdf0.format(d);
		}
		String starttime = startdate + " 00:00:00";
		String totime = todate + " 23:59:59";

		String[] ids = getParaArrayValue("checkbox");
		Hashtable allcpuhash = new Hashtable();

		// �������־ȡ���˿����¼�¼���б�
		String orderflag = "ipaddress";
		if (getParaValue("orderflag") != null && !getParaValue("orderflag").equals("")) {
			orderflag = getParaValue("orderflag");
		}

		List orderList = new ArrayList();
		if (ids != null && ids.length > 0) {
			for (int i = 0; i < ids.length; i++) {
				HostNodeDao dao = new HostNodeDao();
				HostNode node = dao.loadHost(Integer.parseInt(ids[i]));
				if (node == null)
					continue;
				EventListDao eventdao = new EventListDao();
				// �õ��¼��б�
				StringBuffer s = new StringBuffer();
				s.append("select * from system_eventlist where recordtime>= '" + starttime + "' " + "and recordtime<='" + totime + "' ");
				s.append(" and nodeid=" + node.getId());

				List infolist = eventdao.findByCriteria(s.toString());

				// List infolist =
				// eventqueryManager.getQuery(starttime,totime,"99","99",99,node.getId());
				if (infolist != null && infolist.size() > 0) {
					// mainreport = mainreport+ " \r\n";
					int levelone = 0;
					int levletwo = 0;
					int levelthree = 0;
					int pingvalue = 0;
					int cpuvalue = 0;
					int updownvalue = 0;
					int utilvalue = 0;

					for (int j = 0; j < infolist.size(); j++) {
						EventList eventlist = (EventList) infolist.get(j);
						if (eventlist.getContent() == null)
							eventlist.setContent("");
						String content = eventlist.getContent();
						String subentity = eventlist.getSubentity();
						if (eventlist.getLevel1() == 1) {
							levelone = levelone + 1;
						} else if (eventlist.getLevel1() == 2) {
							levletwo = levletwo + 1;
						} else if (eventlist.getLevel1() == 3) {
							levelthree = levelthree + 1;
						}
						// if(content.indexOf("��ͨ��")>=0){
						// pingvalue = pingvalue + 1;
						// }else if(content.indexOf("CPU������")>=0){
						// cpuvalue = cpuvalue + 1;
						// }else if(content.indexOf("up")>=0 ||
						// content.indexOf("down")>=0){
						// updownvalue = updownvalue + 1;
						// }else if(content.indexOf("����")>=0){
						// utilvalue = utilvalue + 1;
						// }

						if (("ping").equals(subentity)) {// ��ͨ��
							pingvalue = pingvalue + 1;
						} else if (("cpu").equals(subentity)) {// cpu
							cpuvalue = cpuvalue + 1;
						} else if (("interface").equals(subentity) && content.indexOf("�˿�") >= 0) {// �˿�
							updownvalue = updownvalue + 1;
						} else if (("interface").equals(subentity) && content.indexOf("�˿�") < 0) {// ����
							utilvalue = utilvalue + 1;
						}
					}
					String equname = node.getAlias();
					String ip = node.getIpAddress();
					List ipeventList = new ArrayList();
					ipeventList.add(ip);
					ipeventList.add(equname);
					ipeventList.add(node.getType());
					ipeventList.add((levelone + levletwo + levelthree) + "");
					ipeventList.add(levelone + "");
					ipeventList.add(levletwo + "");
					ipeventList.add(levelthree + "");
					ipeventList.add(pingvalue + "");
					ipeventList.add(cpuvalue + "");
					ipeventList.add(updownvalue + "");
					ipeventList.add(utilvalue + "");
					orderList.add(ipeventList);

				}
			}

		}
		List returnList = new ArrayList();
		if (orderflag.equalsIgnoreCase("one") || orderflag.equalsIgnoreCase("two") || orderflag.equalsIgnoreCase("three") || orderflag.equalsIgnoreCase("ping") || orderflag.equalsIgnoreCase("cpu")
				|| orderflag.equalsIgnoreCase("updown") || orderflag.equalsIgnoreCase("util") || orderflag.equalsIgnoreCase("sum")) {
			returnList = (List) session.getAttribute("eventlist");
		} else {
			returnList = orderList;
		}

		List list = new ArrayList();
		if (returnList != null && returnList.size() > 0) {
			for (int m = 0; m < returnList.size(); m++) {
				List ipdiskList = (List) returnList.get(m);
				for (int n = m + 1; n < returnList.size(); n++) {
					List _ipdiskList = (List) returnList.get(n);
					if (orderflag.equalsIgnoreCase("ipaddress")) {
					} else if (orderflag.equalsIgnoreCase("sum")) {
						String sum = "";
						if (ipdiskList.get(3) != null) {
							sum = (String) ipdiskList.get(3);
						}
						String _sum = "";
						if (ipdiskList.get(3) != null) {
							_sum = (String) _ipdiskList.get(3);
						}
						if (new Double(sum).doubleValue() < new Double(_sum).doubleValue()) {
							returnList.remove(m);
							returnList.add(m, _ipdiskList);
							returnList.remove(n);
							returnList.add(n, ipdiskList);
							ipdiskList = _ipdiskList;
							_ipdiskList = null;
						}
					} else if (orderflag.equalsIgnoreCase("one")) {
						String downnum = "";
						if (ipdiskList.get(4) != null) {
							downnum = (String) ipdiskList.get(4);
						}
						String _downnum = "";
						if (ipdiskList.get(4) != null) {
							_downnum = (String) _ipdiskList.get(4);
						}
						if (new Double(downnum).doubleValue() < new Double(_downnum).doubleValue()) {
							returnList.remove(m);
							returnList.add(m, _ipdiskList);
							returnList.remove(n);
							returnList.add(n, ipdiskList);
							ipdiskList = _ipdiskList;
							_ipdiskList = null;
						}
					} else if (orderflag.equalsIgnoreCase("two")) {
						String downnum = "";
						if (ipdiskList.get(5) != null) {
							downnum = (String) ipdiskList.get(5);
						}
						String _downnum = "";
						if (ipdiskList.get(5) != null) {
							_downnum = (String) _ipdiskList.get(5);
						}
						if (new Double(downnum).doubleValue() < new Double(_downnum).doubleValue()) {
							returnList.remove(m);
							returnList.add(m, _ipdiskList);
							returnList.remove(n);
							returnList.add(n, ipdiskList);
							ipdiskList = _ipdiskList;
							_ipdiskList = null;
						}
					} else if (orderflag.equalsIgnoreCase("three")) {
						String downnum = "";
						if (ipdiskList.get(6) != null) {
							downnum = (String) ipdiskList.get(6);
						}
						String _downnum = "";
						if (ipdiskList.get(6) != null) {
							_downnum = (String) _ipdiskList.get(6);
						}
						if (new Double(downnum).doubleValue() < new Double(_downnum).doubleValue()) {
							returnList.remove(m);
							returnList.add(m, _ipdiskList);
							returnList.remove(n);
							returnList.add(n, ipdiskList);
							ipdiskList = _ipdiskList;
							_ipdiskList = null;
						}
					} else if (orderflag.equalsIgnoreCase("ping")) {
						String downnum = "";
						if (ipdiskList.get(7) != null) {
							downnum = (String) ipdiskList.get(7);
						}
						String _downnum = "";
						if (ipdiskList.get(7) != null) {
							_downnum = (String) _ipdiskList.get(7);
						}
						if (new Double(downnum).doubleValue() < new Double(_downnum).doubleValue()) {
							returnList.remove(m);
							returnList.add(m, _ipdiskList);
							returnList.remove(n);
							returnList.add(n, ipdiskList);
							ipdiskList = _ipdiskList;
							_ipdiskList = null;
						}
					} else if (orderflag.equalsIgnoreCase("cpu")) {
						String downnum = "";
						if (ipdiskList.get(8) != null) {
							downnum = (String) ipdiskList.get(8);
						}
						String _downnum = "";
						if (ipdiskList.get(8) != null) {
							_downnum = (String) _ipdiskList.get(8);
						}
						if (new Double(downnum).doubleValue() < new Double(_downnum).doubleValue()) {
							returnList.remove(m);
							returnList.add(m, _ipdiskList);
							returnList.remove(n);
							returnList.add(n, ipdiskList);
							ipdiskList = _ipdiskList;
							_ipdiskList = null;
						}
					} else if (orderflag.equalsIgnoreCase("updown")) {
						String downnum = "";
						if (ipdiskList.get(9) != null) {
							downnum = (String) ipdiskList.get(9);
						}
						String _downnum = "";
						if (ipdiskList.get(9) != null) {
							_downnum = (String) _ipdiskList.get(9);
						}
						if (new Double(downnum).doubleValue() < new Double(_downnum).doubleValue()) {
							returnList.remove(m);
							returnList.add(m, _ipdiskList);
							returnList.remove(n);
							returnList.add(n, ipdiskList);
							ipdiskList = _ipdiskList;
							_ipdiskList = null;
						}
					} else if (orderflag.equalsIgnoreCase("util")) {
						String downnum = "";
						if (ipdiskList.get(10) != null) {
							downnum = (String) ipdiskList.get(10);
						}
						String _downnum = "";
						if (ipdiskList.get(10) != null) {
							_downnum = (String) _ipdiskList.get(10);
						}
						if (new Double(downnum).doubleValue() < new Double(_downnum).doubleValue()) {
							returnList.remove(m);
							returnList.add(m, _ipdiskList);
							returnList.remove(n);
							returnList.add(n, ipdiskList);
							ipdiskList = _ipdiskList;
							_ipdiskList = null;
						}
					}
				}
				// �õ�������Subentity���б�
				list.add(ipdiskList);
				ipdiskList = null;
			}
		}

		// setListProperty(capReportForm, request, list);
		request.setAttribute("starttime", starttime);
		request.setAttribute("totime", totime);
		request.setAttribute("startdate", startdate);
		request.setAttribute("todate", todate);
		request.setAttribute("eventlist", list);
		session.setAttribute("eventlist", list);
		return "/capreport/net/netevent.jsp";
	}

	private String downloadpingreport() {
		Date d = new Date();
		String startdate = getParaValue("startdate");
		if (startdate == null) {
			startdate = sdf0.format(d);
		}
		String todate = getParaValue("todate");
		if (todate == null) {
			todate = sdf0.format(d);
		}
		String starttime = startdate + " 00:00:00";
		String totime = todate + " 23:59:59";
		Hashtable allcpuhash = new Hashtable();

		List returnList = new ArrayList();
		// I_MonitorIpList monitorManager=new MonitoriplistManager();
		List memlist = (List) session.getAttribute("pinglist");
		// ������ͨ��
		String pingpath = (String) session.getAttribute("pingpath");
		String responsetimepath = (String) session.getAttribute("responsetimepath");
		Hashtable reporthash = new Hashtable();
		reporthash.put("pinglist", memlist);
		reporthash.put("starttime", starttime);
		reporthash.put("totime", totime);
		reporthash.put("pingpath", pingpath);
		reporthash.put("responsetimepath", responsetimepath);
		ExcelReport1 report = new ExcelReport1(new IpResourceReport(), reporthash);
		report.createReport_hostping("/temp/hostping_report.xls");
		request.setAttribute("filename", report.getFileName());
		return "/capreport/net/download.jsp";
		// return mapping.findForward("report_info");
	}

	private String downloadnetpingreport() {
		Date d = new Date();
		String startdate = getParaValue("startdate");
		if (startdate == null) {
			startdate = sdf0.format(d);
		}
		String todate = getParaValue("todate");
		if (todate == null) {
			todate = sdf0.format(d);
		}
		String starttime = startdate + " 00:00:00";
		String totime = todate + " 23:59:59";
		Hashtable allcpuhash = new Hashtable();

		List returnList = new ArrayList();
		// I_MonitorIpList monitorManager=new MonitoriplistManager();
		List memlist = (List) session.getAttribute("pinglist");
		// ������ͨ��
		String pingpath = (String) session.getAttribute("pingpath");
		// ������Ӧʱ��ͼƬ
		String responsetimepath = (String) session.getAttribute("responsetimepath");
		Hashtable reporthash = new Hashtable();
		reporthash.put("pinglist", memlist);
		reporthash.put("starttime", starttime);
		reporthash.put("totime", totime);
		reporthash.put("pingpath", pingpath);
		reporthash.put("responsetimepath", responsetimepath);

		ExcelReport1 report = new ExcelReport1(new IpResourceReport(), reporthash);
		report.createReport_netping("/temp/netping_report.xls");
		request.setAttribute("filename", report.getFileName());
		return "/capreport/net/download.jsp";
		// return mapping.findForward("report_info");
	}

	private String downloadneteventreport() {
		Date d = new Date();
		String startdate = getParaValue("startdate");
		if (startdate == null) {
			startdate = sdf0.format(d);
		}
		String todate = getParaValue("todate");
		if (todate == null) {
			todate = sdf0.format(d);
		}
		String starttime = startdate + " 00:00:00";
		String totime = todate + " 23:59:59";
		Hashtable allcpuhash = new Hashtable();

		List returnList = new ArrayList();
		// I_MonitorIpList monitorManager=new MonitoriplistManager();
		List memlist = (List) session.getAttribute("eventlist");
		Hashtable reporthash = new Hashtable();
		reporthash.put("eventlist", memlist);
		reporthash.put("starttime", starttime);
		reporthash.put("totime", totime);

		AbstractionReport1 report = new ExcelReport1(new IpResourceReport(), reporthash);
		report.createReport_netevent("/temp/hostping_report.xls");
		request.setAttribute("filename", report.getFileName());
		return "/capreport/net/download.jsp";
		// return mapping.findForward("report_info");
	}
	
	/**
	 * ���������豸��������Ϣ
	 * @return
	 */
	private String downloadnetnetflowreport() {
		Date d = new Date();
		String startdate = getParaValue("startdate");
		if (startdate == null) {
			startdate = sdf0.format(d);
		}
		String todate = getParaValue("todate");
		if (todate == null) {
			todate = sdf0.format(d);
		}
		if(!startdate.contains("00:00:00")){
			startdate = startdate + " 00:00:00";
		}
		if(!todate.contains("23:59:59")){
			todate = todate + " 23:59:59";
		}
		Hashtable allcpuhash = new Hashtable();

		// I_MonitorIpList monitorManager=new MonitoriplistManager();
		Hashtable netflowHash = (Hashtable) session.getAttribute("netflowHash");//������״ͼ��������ݼ���
		List flowLineList = (ArrayList) session.getAttribute("flowLineList");//��������ͼ��������ݼ���
		WritableWorkbook wb = null;
		String fileName = null;
		try{
			fileName = ResourceCenter.getInstance().getSysPath() + "/netflow.xls";
			wb = Workbook.createWorkbook(new File(fileName));
			WritableSheet sheet = wb.createSheet("��������", 0);
			WritableCellFormat labelFormat = new WritableCellFormat();
			labelFormat.setBackground(jxl.format.Colour.GRAY_25);
			WritableCellFormat contentlabelFormat = new WritableCellFormat();
			contentlabelFormat.setBackground(jxl.format.Colour.WHITE);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); 
			int row = 0;//����
			Label tmpLabel = null;
			tmpLabel = new Label(0, row, "�����豸��������", labelFormat);
			sheet.addCell(tmpLabel);
			sheet.mergeCells(0, row, 6, 0);//�趨��Ԫ��ռ��column��
			row++;
			tmpLabel = new Label(0, 1, "��������ʱ��:" + sdf.format(new Date()), contentlabelFormat);
			sheet.addCell(tmpLabel);
			sheet.mergeCells(0, row, 6, 1);
			row++;
			tmpLabel = new Label(0, 2, "����ͳ��ʱ���: " + startdate + " �� " + todate, contentlabelFormat);
			sheet.addCell(tmpLabel);
			sheet.mergeCells(0, row, 6, 2);
			sheet.setColumnView(0,15);//�趨�п� 
			sheet.setColumnView(1,15); 
			sheet.setColumnView(2,15); 
			row++;
			//���ӱ�ͷ
			tmpLabel = new Label(0, row, "���", labelFormat);
			sheet.addCell(tmpLabel);
			tmpLabel = new Label(1, row, "ԴIP��ַ", labelFormat);
			sheet.addCell(tmpLabel);
			tmpLabel = new Label(2, row, "������(KBytes)", labelFormat);
			sheet.addCell(tmpLabel);
			Iterator tempIterator = netflowHash.keySet().iterator();
			row++;
			int i=0;
			while(tempIterator.hasNext()){
				i++;
				String srcip = (String)tempIterator.next();
				double thevalue = (Double) netflowHash.get(srcip);
				thevalue = Double.parseDouble(CommonUtil.format(thevalue, 3));
				tmpLabel = new Label(0, row, i+"", contentlabelFormat);
				sheet.addCell(tmpLabel);
				tmpLabel = new Label(1, row, srcip, contentlabelFormat);
				sheet.addCell(tmpLabel);
				tmpLabel = new Label(2, row, thevalue+"", contentlabelFormat);
				sheet.addCell(tmpLabel);
				row++;
			}
			row++;
			//ʹ��jfreeChart������״ͼ
			String path = makeNetFlowJfreeChartData(netflowHash, "����ͳ����״ͼ", "Դ��ַ", "������(KBytes)", 800, 300);
			// ����ͼƬ
			File file = new File(path);
			// ��sheet��������ͼƬ,0,0,5,1�ֱ������,��,ͼƬ���ռλ���ٸ���,�߶�ռλ���ٸ���
			// allRow = allRow+2;
			sheet.addImage(new WritableImage(0, row, 10, 12, file));
			row = row+12;
			//ʹ��jfreeChart�������ٵ�����ͼ
			String netflowLinePicPath = makeNetFlowLinesJfreeChartData(flowLineList, "����ͳ������ͼ", "����", "������(KBytes)", 800, 300);
			// ����ͼƬ
			File linefile = new File(netflowLinePicPath);
			sheet.addImage(new WritableImage(0, row, 10, 12, linefile));
			// allRow = allRow+7;
			wb.write();
		}catch(Exception e){
			e.printStackTrace();
		} finally {
			try {
				if (wb != null)
					wb.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		Hashtable reporthash = new Hashtable();
//		reporthash.put("eventlist", memlist);
		request.setAttribute("filename", fileName);
//		request.setAttribute("filename", report.getFileName());
		return "/capreport/net/download.jsp";
	}
	
	/**
	 * ���������豸��������Ϣ��ϸ
	 * @return
	 */
	private String downloadnetnetflowdetailreport() {
		Date d = new Date();
		String startdate = getParaValue("startdate");
		if (startdate == null) {
			startdate = sdf0.format(d);
		}
		String todate = getParaValue("todate");
		if (todate == null) {
			todate = sdf0.format(d);
		}
		if(!startdate.contains("00:00:00")){
			startdate = startdate + " 00:00:00";
		}
		if(!todate.contains("23:59:59")){
			todate = todate + " 23:59:59";
		}
		Hashtable allcpuhash = new Hashtable();

		// I_MonitorIpList monitorManager=new MonitoriplistManager();
		List flowDetailList = (ArrayList) session.getAttribute("flowDetailList");
		WritableWorkbook wb = null;
		String fileName = null;
		try{
			fileName = ResourceCenter.getInstance().getSysPath() + "/netflowdetail.xls";
			wb = Workbook.createWorkbook(new File(fileName));
			WritableSheet sheet = wb.createSheet("������ϸ����", 0);
			WritableCellFormat labelFormat = new WritableCellFormat();
			labelFormat.setBackground(jxl.format.Colour.GRAY_25);
			WritableCellFormat contentlabelFormat = new WritableCellFormat();
			contentlabelFormat.setBackground(jxl.format.Colour.WHITE);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); 
			int row = 0;//����
			Label tmpLabel = null;
			tmpLabel = new Label(0, row, "�����豸��������", labelFormat);
			sheet.addCell(tmpLabel);
			sheet.mergeCells(0, row, 6, 0);//�趨��Ԫ��ռ��column��
			row++;
			tmpLabel = new Label(0, 1, "��������ʱ��:" + sdf.format(new Date()), contentlabelFormat);
			sheet.addCell(tmpLabel);
			sheet.mergeCells(0, row, 6, 1);
			row++;
			tmpLabel = new Label(0, 2, "����ͳ��ʱ���: " + startdate + " �� " + todate, contentlabelFormat);
			sheet.addCell(tmpLabel);
			sheet.mergeCells(0, row, 6, 2);
			sheet.setColumnView(0,15);//�趨�п� 
			sheet.setColumnView(1,15); 
			sheet.setColumnView(2,15); 
			sheet.setColumnView(3,15); 
			row++;
			//���ӱ�ͷ
			tmpLabel = new Label(0, row, "���", labelFormat);
			sheet.addCell(tmpLabel);
			tmpLabel = new Label(1, row, "ԴIP��ַ", labelFormat);
			sheet.addCell(tmpLabel);
			tmpLabel = new Label(2, row, "Ŀ��IP��ַ", labelFormat);
			sheet.addCell(tmpLabel);
			tmpLabel = new Label(3, row, "������(KBytes)", labelFormat);
			sheet.addCell(tmpLabel);
			row++;
			if(flowDetailList != null){
				for(int i=0; i<flowDetailList.size(); i++){
					Vector vec = (Vector)flowDetailList.get(i);
					tmpLabel = new Label(0, row, String.valueOf(i+1), contentlabelFormat);
					sheet.addCell(tmpLabel);
					tmpLabel = new Label(1, row, String.valueOf(vec.get(0)), contentlabelFormat);
					sheet.addCell(tmpLabel);
					tmpLabel = new Label(2, row, String.valueOf(vec.get(1)), contentlabelFormat);
					sheet.addCell(tmpLabel);
					tmpLabel = new Label(3, row, String.valueOf(vec.get(2)), contentlabelFormat);
					sheet.addCell(tmpLabel);
					row++;
				}
			}
			row++;
			//ʹ��jfreeChart������״ͼ
			String path = makeNetFlowJfreeChartData(flowDetailList, "����ͳ����״ͼ", "Ŀ�ĵ�ַ", "������(KBytes)", 800, 300);
			// ����ͼƬ
			File file = new File(path);
			// ��sheet��������ͼƬ,0,0,5,1�ֱ������,��,ͼƬ���ռλ���ٸ���,�߶�ռλ���ٸ���
			// allRow = allRow+2;
			sheet.addImage(new WritableImage(0, row, 10, 12, file));
			// allRow = allRow+7;
			wb.write();
		}catch(Exception e){
			e.printStackTrace();
		} finally {
			try {
				if (wb != null)
					wb.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		Hashtable reporthash = new Hashtable();
//		reporthash.put("eventlist", memlist);
		request.setAttribute("filename", fileName);
//		request.setAttribute("filename", report.getFileName());
		return "/capreport/net/download.jsp";
	}
	
	/**
	 * ����������ϸjfreeChartͼ
	 * @param flowDetailList
	 * @param ipList
	 * @param title
	 * @param xdesc
	 * @param ydesc
	 * @param chartWith
	 * @param chartHigh
	 * @return
	 */
	private String makeNetFlowJfreeChartData(List flowDetailList, String title, String xdesc, String ydesc, int chartWith, int chartHigh) {
		String chartPath = ResourceCenter.getInstance().getSysPath() + "temp"
				+ File.separator + System.currentTimeMillis() + ".png";
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		if(flowDetailList != null){
			for(int i=0; i<flowDetailList.size(); i++){
				Vector vec = (Vector)flowDetailList.get(i);
				String srcip = String.valueOf(vec.get(1));
				Double thevalue = Double.valueOf(vec.get(2)+""); 
				dataset.addValue(thevalue, "", srcip);
			}
		}
		String chartkey = ChartCreator.createBarChart(title, xdesc, ydesc,dataset, chartWith, chartHigh);
		JFreeChartBrother chart = (JFreeChartBrother) ResourceCenter.getInstance().getChartStorage().get(chartkey);
		try {
			File f = new File(chartPath);
			if (!f.exists()) {
				f.createNewFile();
			}
			FileOutputStream fos = new FileOutputStream(f);
			ChartUtilities.writeChartAsPNG(fos, chart.getChart(), chart
					.getWidth(), chart.getHeight());
		} catch (IOException ioe) {
			chartPath = "";
			SysLogger.error("", ioe);
		}
		return chartPath;
	}
	
	/**
	 * ��������jfreeChartͼ
	 * @param dataList
	 * @param ipList
	 * @param title
	 * @param xdesc
	 * @param ydesc
	 * @param chartWith
	 * @param chartHigh
	 * @return
	 */
	private String makeNetFlowJfreeChartData(Hashtable netflowHash, String title, String xdesc, String ydesc, int chartWith, int chartHigh) {
		String chartPath = ResourceCenter.getInstance().getSysPath() + "temp"
				+ File.separator + System.currentTimeMillis() + ".png";
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		Iterator tempIterator = netflowHash.keySet().iterator();
		int i=0;
		while(tempIterator.hasNext()){
			i++;
			String srcip = (String)tempIterator.next();
			double thevalue = (Double) netflowHash.get(srcip);
			thevalue = Double.parseDouble(CommonUtil.format(thevalue, 3));
			dataset.addValue(thevalue, "", srcip);
		}
		String chartkey = ChartCreator.createBarChart(title, xdesc, ydesc,dataset, chartWith, chartHigh);
		JFreeChartBrother chart = (JFreeChartBrother) ResourceCenter.getInstance().getChartStorage().get(chartkey);
		try {
			File f = new File(chartPath);
			if (!f.exists()) {
				f.createNewFile();
			}
			FileOutputStream fos = new FileOutputStream(f);
			ChartUtilities.writeChartAsPNG(fos, chart.getChart(), chart
					.getWidth(), chart.getHeight());
		} catch (IOException ioe) {
			chartPath = "";
			SysLogger.error("", ioe);
		}
		return chartPath;
	}
	
	/**
	 * ��������jfreeChart����ͼ
	 * @param dataList
	 * @param ipList
	 * @param title
	 * @param xdesc
	 * @param ydesc
	 * @param chartWith
	 * @param chartHigh
	 * @return
	 */
	private String makeNetFlowLinesJfreeChartData(List netflowdata, String title, String xdesc, String ydesc, int chartWith, int chartHigh) {
		//�������ڵ�List����
		List dateList = new ArrayList();
		//ԴIP��ַ�ļ���
		List ipList = new ArrayList();
		for(int i=0; i<netflowdata.size(); i++){
			Vector vec = (Vector) netflowdata.get(i);
			if(!ipList.contains(vec.get(0))){
				ipList.add(vec.get(0));
			}
			if(dateList.contains(vec.get(2))){
				continue;
			}else{
				dateList.add(vec.get(2));
			}
		}
		List dataList = new ArrayList();
		for(int i=0; i<ipList.size(); i++){
			String srcip = (String)ipList.get(i); 
			List tempList = new ArrayList();
			for(int j=0; j<netflowdata.size(); j++){
				Vector tempVector = (Vector)netflowdata.get(j);
				Vector v = new Vector();
				v.add(0, (String)tempVector.get(1));//ֵ
				v.add(1, (String)tempVector.get(2));//����
				v.add(2,"KBytes");//��λ
				String srciptemp =(String) tempVector.get(0);
				if(srcip.equals(srciptemp)){
					tempList.add(v);
				}
			}
			dataList.add(tempList);
		}
		//dataList iplist
		ReportExport reportExport = new ReportExport();
		String chartPath = reportExport.makeJfreeChartData(dataList, ipList, title, xdesc, ydesc);
		return chartPath;
	}
	/*
	 * guzhiming �����豸�ۺϱ��� ������ģ��ʹ��
	 */
	public void createselfnetreport(String startdate, String todate, String ip, String type, String str, WritableWorkbook wb, String filena) {
		Date d = new Date();
		if (startdate == null) {
			startdate = sdf0.format(d);
		}
		if (todate == null) {
			todate = sdf0.format(d);
		}
		String starttime = startdate + " 00:00:00";
		String totime = todate + " 23:59:59";
		Hashtable allcpuhash = new Hashtable();
		String equipname = "";
		// zhushouzhi--------------------------start
		String typename = "";
		String equipnameNetDoc = "";
		// zhushouzhi-----------------------------end
		Hashtable hash = new Hashtable();// "Cpu",--current
		Hashtable memhash = new Hashtable();// mem--current
		Hashtable diskhash = new Hashtable();
		Hashtable memmaxhash = new Hashtable();// mem--max
		Hashtable memavghash = new Hashtable();// mem--avg
		Hashtable maxhash = new Hashtable();// "Cpu"--max
		Hashtable maxping = new Hashtable();// Ping--max
		Hashtable pingdata = ShareData.getPingdata();
		Hashtable sharedata = ShareData.getSharedata();
		Vector vector = new Vector();
		Hashtable reporthash = new Hashtable();
		int pingvalue = 0;
		int cpuvalue = 0;
		int updownvalue = 0;
		int utilvalue = 0;

		try {
			HostNodeDao dao = new HostNodeDao();
			HostNode node = (HostNode) dao.findByCondition("ip_address", ip).get(0);
			dao.close();
			// ip=node.getIpAddress();
			equipname = node.getAlias() + "(" + ip + ")";
			// zhushouzhi---------------------start
			equipnameNetDoc = node.getAlias();
			// zhushouzhi-----------------------end
			// String remoteip = request.getRemoteAddr();
			String newip = doip(ip);

			// �������־ȡ���˿����¼�¼���б�
			String orderflag = "index";

			String[] netInterfaceItem = { "index", "ifname", "ifSpeed", "ifOperStatus", "OutBandwidthUtilHdx", "InBandwidthUtilHdx" };

			vector = hostlastmanager.getInterface_share(ip, netInterfaceItem, orderflag, startdate, todate);
			PortconfigDao portdao = new PortconfigDao();
			Hashtable portconfigHash = portdao.getIpsHash(ip);
			List reportports = portdao.getByIpAndReportflag(ip, new Integer(1));
			if (reportports != null && reportports.size() > 0) {
				// SysLogger.info("reportports size
				// ##############"+reportports.size());
				// ��ʾ�˿ڵ�����ͼ��
				I_HostCollectDataDay daymanager = new HostCollectDataDayManager();
				String unit = "kb/s";
				String title = starttime + "��" + totime + "�˿�����";
				String[] banden3 = { "InBandwidthUtilHdx", "OutBandwidthUtilHdx" };
				String[] bandch3 = { "�������", "��������" };

				for (int i = 0; i < reportports.size(); i++) {
					SysLogger.info(reportports.get(i).getClass() + "===============");
					com.afunms.config.model.Portconfig portconfig = null;
					try {
						portconfig = (com.afunms.config.model.Portconfig) reportports.get(i);
						// ��������ʾ����
						Hashtable value = new Hashtable();
						value = daymanager.getmultiHisHdx(ip, "ifspeed", portconfig.getPortindex() + "", banden3, bandch3, startdate, todate, "UtilHdx");
						String reportname = "��" + portconfig.getPortindex() + "(" + portconfig.getName() + ")�˿�����" + startdate + "��" + todate + "����(��������ʾ)";
						p_drawchartMultiLineMonth(value, reportname, newip + portconfig.getPortindex() + "ifspeed_day", 800, 200, "UtilHdx");
						String url1 = "../images/jfreechart/" + newip + portconfig.getPortindex() + "ifspeed_day.png";
					} catch (Exception ex) {
						ex.printStackTrace();
					}

				}
			}

			reporthash.put("portconfigHash", portconfigHash);
			reporthash.put("reportports", reportports);
			reporthash.put("netifVector", vector);
			// zhushouzhi--------------------start
			reporthash.put("startdate", startdate);
			reporthash.put("todate", todate);
			reporthash.put("totime", totime);
			reporthash.put("starttime", starttime);
			// zhushouzhi-------------------------end
			Hashtable cpuhash = hostmanager.getCategory(ip, "CPU", "Utilization", starttime, totime);
			Hashtable streaminHash = hostmanager.getAllutilhdx(ip, "AllInBandwidthUtilHdx", starttime, totime, "avg");
			Hashtable streamoutHash = hostmanager.getAllutilhdx(ip, "AllOutBandwidthUtilHdx", starttime, totime, "avg");
			String pingconavg = "";
			String avgput = "";
			// String avgoutput = "";
			maxhash = new Hashtable();
			String cpumax = "";
			String avgcpu = "";
			String maxput = "";
			if (cpuhash.get("max") != null) {
				cpumax = (String) cpuhash.get("max");
			}
			if (cpuhash.get("avgcpucon") != null) {
				avgcpu = (String) cpuhash.get("avgcpucon");
			}
			// zhushouzhi-----------------------start
			if (streaminHash.get("avgput") != null) {
				avgput = (String) streaminHash.get("avgput");
				reporthash.put("avginput", avgput);
			}
			if (streamoutHash.get("avgput") != null) {
				avgput = (String) streamoutHash.get("avgput");
				reporthash.put("avgoutput", avgput);
			}
			Hashtable streammaxinHash = hostmanager.getAllutilhdx(ip, "AllInBandwidthUtilHdx", starttime, totime, "max");
			Hashtable streammaxoutHash = hostmanager.getAllutilhdx(ip, "AllOutBandwidthUtilHdx", starttime, totime, "max");
			if (streammaxinHash.get("max") != null) {
				maxput = (String) streammaxinHash.get("max");
				reporthash.put("maxinput", maxput);
			}
			if (streammaxoutHash.get("max") != null) {
				maxput = (String) streammaxoutHash.get("max");
				reporthash.put("maxoutput", maxput);
			}
			// zhushouzhi--------------------------------------end
			maxhash.put("cpumax", cpumax);
			maxhash.put("avgcpu", avgcpu);

			// ���ڴ��л�õ�ǰ�ĸ���IP��ص�IP-MAC��FDB����Ϣ
			Hashtable _IpRouterHash = ShareData.getIprouterdata();
			vector = (Vector) _IpRouterHash.get(ip);
			if (vector != null)
				reporthash.put("iprouterVector", vector);
			// zhushouzhi--------------------start
			EventListDao eventdao = new EventListDao();
			// �õ��¼��б�
			StringBuffer s = new StringBuffer();
			s.append("select * from system_eventlist where recordtime>= '" + starttime + "' " + "and recordtime<='" + totime + "' ");
			s.append(" and nodeid=" + node.getId());

			List infolist = eventdao.findByCriteria(s.toString());

			if (infolist != null && infolist.size() > 0) {
				// mainreport = mainreport+ " \r\n";

				for (int j = 0; j < infolist.size(); j++) {
					EventList eventlist = (EventList) infolist.get(j);
					if (eventlist.getContent() == null)
						eventlist.setContent("");
					String content = eventlist.getContent();
					String subentity = eventlist.getSubentity();
					if (("ping").equals(subentity)) {// ��ͨ��
						pingvalue = pingvalue + 1;
					} else if (("cpu").equals(subentity)) {// cpu
						cpuvalue = cpuvalue + 1;
					} else if (("interface").equals(subentity) && content.indexOf("�˿�") >= 0) {// �˿�
						updownvalue = updownvalue + 1;
					} else if (("interface").equals(subentity) && content.indexOf("�˿�") < 0) {// ����
						utilvalue = utilvalue + 1;
					}
				}
			}
			reporthash.put("pingvalue", pingvalue);
			reporthash.put("cpuvalue", cpuvalue);
			reporthash.put("updownvalue", updownvalue);
			reporthash.put("utilvalue", utilvalue);
			// zhushouzhi-------------------------end

			Vector pdata = (Vector) pingdata.get(ip);
			// ��ping�õ������ݼӽ�ȥ
			if (pdata != null && pdata.size() > 0) {
				for (int m = 0; m < pdata.size(); m++) {
					Pingcollectdata hostdata = (Pingcollectdata) pdata.get(m);
					if (hostdata.getSubentity().equals("ConnectUtilization")) {
						reporthash.put("time", hostdata.getCollecttime());
						reporthash.put("Ping", hostdata.getThevalue());
						reporthash.put("ping", maxping);
					}
				}
			}

			// CPU
			Hashtable hdata = (Hashtable) sharedata.get(ip);
			if (hdata == null)
				hdata = new Hashtable();
			Vector cpuVector = new Vector();
			if (hdata.get("cpu") != null)
				cpuVector = (Vector) hdata.get("cpu");
			if (cpuVector != null && cpuVector.size() > 0) {
				// for(int si=0;si<cpuVector.size();si++){
				CPUcollectdata cpudata = (CPUcollectdata) cpuVector.elementAt(0);
				maxhash.put("cpu", cpudata.getThevalue());
				reporthash.put("CPU", maxhash);
				// }
			} else {
				reporthash.put("CPU", maxhash);
			}
			// ����

			reporthash.put("Memory", memhash);
			reporthash.put("Disk", diskhash);
			reporthash.put("equipname", equipname);
			reporthash.put("equipnameNetDoc", equipnameNetDoc);
			reporthash.put("ip", ip);
			if ("network".equals(type)) {
				typename = "�����豸";

			}
			reporthash.put("typename", typename);

			ExcelReport1 report = new ExcelReport1(new IpResourceReport(), reporthash);
			// ��ͼ

			p_draw_line(cpuhash, "", newip + "cpu", 740, 120);

			Hashtable ConnectUtilizationhash = hostmanager.getCategory(ip, "Ping", "ConnectUtilization", starttime, totime);
			if (ConnectUtilizationhash.get("avgpingcon") != null)
				pingconavg = (String) ConnectUtilizationhash.get("avgpingcon");
			String ConnectUtilizationmax = "";
			maxping.put("avgpingcon", pingconavg);
			if (ConnectUtilizationhash.get("max") != null) {
				ConnectUtilizationmax = (String) ConnectUtilizationhash.get("max");
			}
			maxping.put("pingmax", ConnectUtilizationmax);

			p_draw_line(ConnectUtilizationhash, "", newip + "ConnectUtilization", 740, 120);

			// String str = request.getParameter("str");//
			// ��ҳ�淵���趨��strֵ�����жϣ�����excel�������word����
			// zhushouzhi--------------------------------
			if ("0".equals(str)) {
				report.createReport_networkWithoutClose(filena, wb);
				SysLogger.info("filename---" + report.getFileName());// excel�ۺϱ���
				// request.setAttribute("filename", report.getFileName());
			} else if ("1".equals(str)) {
				ExcelReport1 report1 = new ExcelReport1(new IpResourceReport(), reporthash);
				try {
					String file = "temp/networknms_report.doc";// ���浽��Ŀ�ļ����µ�ָ���ļ���
					String fileName = ResourceCenter.getInstance().getSysPath() + file;// ��ȡϵͳ�ļ���·��
					report1.createReport_networkDoc(fileName);// word�ۺϱ���

					// request.setAttribute("filename", fileName);
				} catch (DocumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if ("3".equals(str)) {
				ExcelReport1 report1 = new ExcelReport1(new IpResourceReport(), reporthash);
				try {
					String file = "temp/networkNewknms_report.doc";// ���浽��Ŀ�ļ����µ�ָ���ļ���
					String fileName = ResourceCenter.getInstance().getSysPath() + file;// ��ȡϵͳ�ļ���·��
					report1.createReport_networkNewDoc(fileName);// word���з�������

					// request.setAttribute("filename", fileName);
				} catch (DocumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else if ("4".equals(str)) {
				ExcelReport1 report1 = new ExcelReport1(new IpResourceReport(), reporthash);
				try {
					String file = "temp/networkNewknms_report.pdf";// ���浽��Ŀ�ļ����µ�ָ���ļ���
					String fileName = ResourceCenter.getInstance().getSysPath() + file;// ��ȡϵͳ�ļ���·��
					report1.createReport_networkNewPdf(fileName);// pdf�����豸ҵ�������

					// request.setAttribute("filename", fileName);
				} catch (DocumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else if ("5".equals(str)) {
				ExcelReport1 report1 = new ExcelReport1(new IpResourceReport(), reporthash);
				try {
					String file = "temp/networkknms_report.pdf";// ���浽��Ŀ�ļ����µ�ָ���ļ���
					String fileName = ResourceCenter.getInstance().getSysPath() + file;// ��ȡϵͳ�ļ���·��
					report1.createReport_networkPDF(fileName);

					// request.setAttribute("filename", fileName);
				} catch (DocumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// zhushouzhi---------------------------end
		// return mapping.findForward("report_info");

	}

	private String downloadselfnetreport() {
		Date d = new Date();
		String startdate = getParaValue("startdate");
		if (startdate == null) {
			startdate = sdf0.format(d);
		}
		String todate = getParaValue("todate");
		if (todate == null) {
			todate = sdf0.format(d);
		}
		String starttime = startdate + " 00:00:00";
		String totime = todate + " 23:59:59";
		Hashtable allcpuhash = new Hashtable();
		String ip = "";
		String equipname = "";
		// zhushouzhi--------------------------start
		String type = "";
		String typename = "";
		String equipnameNetDoc = "";
		// zhushouzhi-----------------------------end
		Hashtable hash = new Hashtable();// "Cpu",--current
		Hashtable memhash = new Hashtable();// mem--current
		Hashtable diskhash = new Hashtable();
		Hashtable memmaxhash = new Hashtable();// mem--max
		Hashtable memavghash = new Hashtable();// mem--avg
		Hashtable maxhash = new Hashtable();// "Cpu"--max
		Hashtable maxping = new Hashtable();// Ping--max
		Hashtable pingdata = ShareData.getPingdata();
		Hashtable sharedata = ShareData.getSharedata();
		Vector vector = new Vector();
		Hashtable reporthash = new Hashtable();
		int pingvalue = 0;
		int cpuvalue = 0;
		int updownvalue = 0;
		int utilvalue = 0;

		try {
			ip = getParaValue("ipaddress");
			type = getParaValue("type");
			HostNodeDao dao = new HostNodeDao();
			HostNode node = (HostNode) dao.findByCondition("ip_address", ip).get(0);
			dao.close();
			// ip=node.getIpAddress();
			equipname = node.getAlias() + "(" + ip + ")";
			// zhushouzhi---------------------start
			equipnameNetDoc = node.getAlias();
			// zhushouzhi-----------------------end
			String remoteip = request.getRemoteAddr();
			String newip = doip(ip);

			// �������־ȡ���˿����¼�¼���б�
			String orderflag = "index";

			String[] netInterfaceItem = { "index", "ifname", "ifSpeed", "ifOperStatus", "OutBandwidthUtilHdx", "InBandwidthUtilHdx" };

			vector = hostlastmanager.getInterface_share(ip, netInterfaceItem, orderflag, startdate, todate);
			PortconfigDao portdao = new PortconfigDao();
			Hashtable portconfigHash = new Hashtable();

			List reportports = new ArrayList();
			try {
				portconfigHash = portdao.getIpsHash(ip);
				reportports = portdao.getByIpAndReportflag(ip, new Integer(1));
			} catch (Exception e) {

			} finally {
				portdao.close();
			}

			if (reportports != null && reportports.size() > 0) {
				// SysLogger.info("reportports size
				// ##############"+reportports.size());
				// ��ʾ�˿ڵ�����ͼ��
				I_HostCollectDataDay daymanager = new HostCollectDataDayManager();
				String unit = "kb/s";
				String title = starttime + "��" + totime + "�˿�����";
				String[] banden3 = { "InBandwidthUtilHdx", "OutBandwidthUtilHdx" };
				String[] bandch3 = { "�������", "��������" };

				for (int i = 0; i < reportports.size(); i++) {
					// SysLogger.info(reportports.get(i).getClass()+
					// "===============");
					com.afunms.config.model.Portconfig portconfig = null;
					try {
						portconfig = (com.afunms.config.model.Portconfig) reportports.get(i);
						// ��������ʾ����
						Hashtable value = new Hashtable();
						value = daymanager.getmultiHisHdx(ip, "ifspeed", portconfig.getPortindex() + "", banden3, bandch3, startdate, todate, "UtilHdx");
						String reportname = "��" + portconfig.getPortindex() + "(" + portconfig.getName() + ")�˿�����" + startdate + "��" + todate + "����(��������ʾ)";
						p_drawchartMultiLineMonth(value, reportname, newip + portconfig.getPortindex() + "ifspeed_day", 800, 200, "UtilHdx");
						String url1 = "../images/jfreechart/" + newip + portconfig.getPortindex() + "ifspeed_day.png";
					} catch (Exception ex) {
						ex.printStackTrace();
					}

				}
			}

			reporthash.put("portconfigHash", portconfigHash);
			reporthash.put("reportports", reportports);
			reporthash.put("netifVector", vector);
			// zhushouzhi--------------------start
			reporthash.put("startdate", startdate);
			reporthash.put("todate", todate);
			reporthash.put("totime", totime);
			reporthash.put("starttime", starttime);
			// zhushouzhi-------------------------end
			Hashtable cpuhash = hostmanager.getCategory(ip, "CPU", "Utilization", starttime, totime);
			Hashtable streaminHash = hostmanager.getAllutilhdx(ip, "AllInBandwidthUtilHdx", starttime, totime, "avg");
			Hashtable streamoutHash = hostmanager.getAllutilhdx(ip, "AllOutBandwidthUtilHdx", starttime, totime, "avg");
			String pingconavg = "";
			String avgput = "";
			// String avgoutput = "";
			maxhash = new Hashtable();
			String cpumax = "";
			String avgcpu = "";
			String maxput = "";
			if (cpuhash.get("max") != null) {
				cpumax = (String) cpuhash.get("max");
			}
			if (cpuhash.get("avgcpucon") != null) {
				avgcpu = (String) cpuhash.get("avgcpucon");
			}
			// zhushouzhi-----------------------start
			if (streaminHash.get("avgput") != null) {
				avgput = (String) streaminHash.get("avgput");
				reporthash.put("avginput", avgput);
			}
			if (streamoutHash.get("avgput") != null) {
				avgput = (String) streamoutHash.get("avgput");
				reporthash.put("avgoutput", avgput);
			}
			Hashtable streammaxinHash = hostmanager.getAllutilhdx(ip, "AllInBandwidthUtilHdx", starttime, totime, "max");
			Hashtable streammaxoutHash = hostmanager.getAllutilhdx(ip, "AllOutBandwidthUtilHdx", starttime, totime, "max");
			if (streammaxinHash.get("max") != null) {
				maxput = (String) streammaxinHash.get("max");
				reporthash.put("maxinput", maxput);
			}
			if (streammaxoutHash.get("max") != null) {
				maxput = (String) streammaxoutHash.get("max");
				reporthash.put("maxoutput", maxput);
			}
			// zhushouzhi--------------------------------------end
			maxhash.put("cpumax", cpumax);
			maxhash.put("avgcpu", avgcpu);

			// ���ڴ��л�õ�ǰ�ĸ���IP��ص�IP-MAC��FDB����Ϣ
			Hashtable _IpRouterHash = ShareData.getIprouterdata();
			vector = (Vector) _IpRouterHash.get(ip);
			if (vector != null)
				reporthash.put("iprouterVector", vector);
			// zhushouzhi--------------------start
			EventListDao eventdao = new EventListDao();
			// �õ��¼��б�
			StringBuffer s = new StringBuffer();
			s.append("select * from system_eventlist where recordtime>= '" + starttime + "' " + "and recordtime<='" + totime + "' ");
			s.append(" and nodeid=" + node.getId());

			List infolist = new ArrayList();
			try {
				infolist = eventdao.findByCriteria(s.toString());
			} catch (Exception e) {

			} finally {
				eventdao.close();
			}

			if (infolist != null && infolist.size() > 0) {
				// mainreport = mainreport+ " \r\n";

				for (int j = 0; j < infolist.size(); j++) {
					EventList eventlist = (EventList) infolist.get(j);
					if (eventlist.getContent() == null)
						eventlist.setContent("");
					String content = eventlist.getContent();
					String subentity = eventlist.getSubentity();
					if (("ping").equals(subentity)) {// ��ͨ��
						pingvalue = pingvalue + 1;
					} else if (("cpu").equals(subentity)) {// cpu
						cpuvalue = cpuvalue + 1;
					} else if (("interface").equals(subentity) && content.indexOf("�˿�") >= 0) {// �˿�
						updownvalue = updownvalue + 1;
					} else if (("interface").equals(subentity) && content.indexOf("�˿�") < 0) {// ����
						utilvalue = utilvalue + 1;
					}
				}
			}
			reporthash.put("pingvalue", pingvalue);
			reporthash.put("cpuvalue", cpuvalue);
			reporthash.put("updownvalue", updownvalue);
			reporthash.put("utilvalue", utilvalue);
			// zhushouzhi-------------------------end

			Vector pdata = (Vector) pingdata.get(ip);
			// ��ping�õ������ݼӽ�ȥ
			if (pdata != null && pdata.size() > 0) {
				for (int m = 0; m < pdata.size(); m++) {
					Pingcollectdata hostdata = (Pingcollectdata) pdata.get(m);
					if (hostdata.getSubentity().equals("ConnectUtilization")) {
						reporthash.put("time", hostdata.getCollecttime());
						reporthash.put("Ping", hostdata.getThevalue());
						reporthash.put("ping", maxping);
					}
				}
			}

			// CPU
			Hashtable hdata = (Hashtable) sharedata.get(ip);
			if (hdata == null)
				hdata = new Hashtable();
			Vector cpuVector = new Vector();
			if (hdata.get("cpu") != null)
				cpuVector = (Vector) hdata.get("cpu");
			if (cpuVector != null && cpuVector.size() > 0) {
				// for(int si=0;si<cpuVector.size();si++){
				CPUcollectdata cpudata = (CPUcollectdata) cpuVector.elementAt(0);
				maxhash.put("cpu", cpudata.getThevalue());
				reporthash.put("CPU", maxhash);
				// }
			} else {
				reporthash.put("CPU", maxhash);
			}
			// ��������
			String grade = "��";
			if (pingvalue + cpuvalue + updownvalue + utilvalue >= 3) {
				grade = "��";
			} else if (pingvalue + cpuvalue + updownvalue + utilvalue < 3 && pingvalue + cpuvalue + updownvalue + utilvalue > 0) {
				grade = "��";
			} else {
				grade = "��";
			}
			reporthash.put("grade", grade);
			// ����
			reporthash.put("Memory", memhash);
			reporthash.put("Disk", diskhash);
			reporthash.put("equipname", equipname);
			reporthash.put("equipnameNetDoc", equipnameNetDoc);
			reporthash.put("ip", ip);
			if ("network".equals(type)) {
				typename = "�����豸";

			}
			reporthash.put("typename", typename);

			AbstractionReport1 report = new ExcelReport1(new IpResourceReport(), reporthash);
			// ��ͼ

			p_draw_line(cpuhash, "", newip + "cpu", 740, 120);

			Hashtable ConnectUtilizationhash = hostmanager.getCategory(ip, "Ping", "ConnectUtilization", starttime, totime);
			if (ConnectUtilizationhash.get("avgpingcon") != null)
				pingconavg = (String) ConnectUtilizationhash.get("avgpingcon");
			String ConnectUtilizationmax = "";
			maxping.put("avgpingcon", pingconavg);
			if (ConnectUtilizationhash.get("max") != null) {
				ConnectUtilizationmax = (String) ConnectUtilizationhash.get("max");
			}
			maxping.put("pingmax", ConnectUtilizationmax);

			p_draw_line(ConnectUtilizationhash, "", newip + "ConnectUtilization", 740, 120);

			String str = request.getParameter("str");// ��ҳ�淵���趨��strֵ�����жϣ�����excel�������word����
			// zhushouzhi--------------------------------
			if ("0".equals(str)) {
				report.createReport_network("temp/networknms_report.xls");
				// SysLogger.info("filename---" +
				// report.getFileName());//excel�ۺϱ���
				request.setAttribute("filename", report.getFileName());
			} else if ("1".equals(str)) {
				ExcelReport1 report1 = new ExcelReport1(new IpResourceReport(), reporthash);
				try {
					String file = "temp/networknms_report.doc";// ���浽��Ŀ�ļ����µ�ָ���ļ���
					String fileName = ResourceCenter.getInstance().getSysPath() + file;// ��ȡϵͳ�ļ���·��
					report1.createReport_networkDoc(fileName);// word�ۺϱ���

					request.setAttribute("filename", fileName);
				} catch (DocumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if ("3".equals(str)) {
				ExcelReport1 report1 = new ExcelReport1(new IpResourceReport(), reporthash);
				try {
					String file = "temp/networkNewknms_report.doc";// ���浽��Ŀ�ļ����µ�ָ���ļ���
					String fileName = ResourceCenter.getInstance().getSysPath() + file;// ��ȡϵͳ�ļ���·��
					report1.createReport_networkNewDoc(fileName);// word���з�������

					request.setAttribute("filename", fileName);
				} catch (DocumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else if ("4".equals(str)) {
				ExcelReport1 report1 = new ExcelReport1(new IpResourceReport(), reporthash);
				try {
					String file = "temp/networkNewknms_report.pdf";// ���浽��Ŀ�ļ����µ�ָ���ļ���
					String fileName = ResourceCenter.getInstance().getSysPath() + file;// ��ȡϵͳ�ļ���·��
					report1.createReport_networkNewPdf(fileName);// pdf�����豸ҵ�������

					request.setAttribute("filename", fileName);
				} catch (DocumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else if ("5".equals(str)) {
				ExcelReport1 report1 = new ExcelReport1(new IpResourceReport(), reporthash);
				try {
					String file = "temp/networkknms_report.pdf";// ���浽��Ŀ�ļ����µ�ָ���ļ���
					String fileName = ResourceCenter.getInstance().getSysPath() + file;// ��ȡϵͳ�ļ���·��
					report1.createReport_networkPDF(fileName);

					request.setAttribute("filename", fileName);
				} catch (DocumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "/capreport/net/download.jsp";
		// zhushouzhi---------------------------end
		// return mapping.findForward("report_info");
	}

	private String downloadmultinetreport() {
		String oids = getParaValue("ids");
		if (oids == null)
			oids = "";
		Integer[] ids = null;
		if (oids.split(",").length > 0) {
			String[] _ids = oids.split(",");
			if (_ids != null && _ids.length > 0)
				ids = new Integer[_ids.length];
			for (int i = 0; i < _ids.length; i++) {
				ids[i] = new Integer(_ids[i]);
			}
		}
		Date d = new Date();
		String startdate = getParaValue("startdate");
		if (startdate == null) {
			startdate = sdf0.format(d);
		}
		String todate = getParaValue("todate");
		if (todate == null) {
			todate = sdf0.format(d);
		}
		String starttime = startdate + " 00:00:00";
		String totime = todate + " 23:59:59";
		Hashtable allcpuhash = new Hashtable();
		String ip = "";
		String equipname = "";

		Hashtable hash = new Hashtable();// "Cpu",--current
		Hashtable memhash = new Hashtable();// mem--current
		Hashtable diskhash = new Hashtable();
		Hashtable memmaxhash = new Hashtable();// mem--max
		Hashtable memavghash = new Hashtable();// mem--avg
		Hashtable maxhash = new Hashtable();// "Cpu"--max
		Hashtable maxping = new Hashtable();// Ping--max
		Hashtable pingdata = ShareData.getPingdata();
		Hashtable sharedata = ShareData.getSharedata();
		Vector vector = new Vector();
		// Hashtable reporthash = new Hashtable();
		try {
			Hashtable allreporthash = new Hashtable();
			if (ids != null && ids.length > 0) {
				for (int i = 0; i < ids.length; i++) {
					Hashtable reporthash = new Hashtable();
					HostNodeDao dao = new HostNodeDao();
					HostNode node = (HostNode) dao.loadHost(ids[i]);
					dao.close();
					// Monitoriplist monitor = monitorManager.getById(ids[i]);
					// Equipment equipment = (Equipment)hostMonitor.get(i);
					ip = node.getIpAddress();
					equipname = node.getAlias();
					String remoteip = request.getRemoteAddr();
					String newip = doip(ip);
					// �������־ȡ���˿����¼�¼���б�
					String orderflag = "index";

					String[] netInterfaceItem = { "index", "ifname", "ifSpeed", "ifOperStatus", "OutBandwidthUtilHdx", "InBandwidthUtilHdx" };

					vector = hostlastmanager.getInterface_share(ip, netInterfaceItem, orderflag, startdate, todate);
					PortconfigDao portdao = new PortconfigDao();
					Hashtable portconfigHash = new Hashtable();
					List reportports = new ArrayList();
					try {
						portconfigHash = portdao.getIpsHash(ip);
						reportports = portdao.getByIpAndReportflag(ip, new Integer(1));
					} catch (Exception e) {

					} finally {
						portdao.close();
					}
					reporthash.put("portconfigHash", portconfigHash);
					reporthash.put("reportports", reportports);
					if (reportports != null && reportports.size() > 0) {
						// ��ʾ�˿ڵ�����ͼ��
						I_HostCollectDataDay daymanager = new HostCollectDataDayManager();
						String unit = "kb/s";
						String title = startdate + "��" + todate + "�˿�����";
						String[] banden3 = { "InBandwidthUtilHdx", "OutBandwidthUtilHdx" };
						String[] bandch3 = { "�������", "��������" };

						for (int k = 0; k < reportports.size(); k++) {
							com.afunms.config.model.Portconfig portconfig = (com.afunms.config.model.Portconfig) reportports.get(k);
							// ��������ʾ����
							Hashtable value = new Hashtable();
							value = daymanager.getmultiHisHdx(ip, "ifspeed", portconfig.getPortindex() + "", banden3, bandch3, startdate, todate, "UtilHdx");
							String reportname = "��" + portconfig.getPortindex() + "(" + portconfig.getName() + ")�˿�����" + startdate + "��" + todate + "����(��������ʾ)";
							p_drawchartMultiLineMonth(value, reportname, newip + portconfig.getPortindex() + "ifspeed_day", 800, 200, "UtilHdx");
							// String url1 =
							// "/resource/image/jfreechart/"+newip+portconfig.getPortindex()+"ifspeed_day.png";
						}
					}

					reporthash.put("netifVector", vector);

					Hashtable cpuhash = hostmanager.getCategory(ip, "CPU", "Utilization", starttime, totime);
					String pingconavg = "";

					maxhash = new Hashtable();
					String cpumax = "";
					String avgcpu = "";
					if (cpuhash.get("max") != null) {
						cpumax = (String) cpuhash.get("max");
					}
					if (cpuhash.get("avgcpucon") != null) {
						avgcpu = (String) cpuhash.get("avgcpucon");
					}

					maxhash.put("cpumax", cpumax);
					maxhash.put("avgcpu", avgcpu);
					// ��ͼ
					p_draw_line(cpuhash, "", newip + "cpu", 740, 120);
					Hashtable ConnectUtilizationhash = hostmanager.getCategory(ip, "Ping", "ConnectUtilization", starttime, totime);
					if (ConnectUtilizationhash.get("avgpingcon") != null)
						pingconavg = (String) ConnectUtilizationhash.get("avgpingcon");
					String ConnectUtilizationmax = "";
					maxping.put("avgpingcon", pingconavg);
					if (ConnectUtilizationhash.get("max") != null) {
						ConnectUtilizationmax = (String) ConnectUtilizationhash.get("max");
					}
					maxping.put("pingmax", ConnectUtilizationmax);

					p_draw_line(ConnectUtilizationhash, "", newip + "ConnectUtilization", 740, 120);

					// ���ڴ��л�õ�ǰ�ĸ���IP��ص�IP-MAC��FDB����Ϣ
					Hashtable _IpRouterHash = ShareData.getIprouterdata();
					vector = (Vector) _IpRouterHash.get(ip);
					if (vector != null)
						reporthash.put("iprouterVector", vector);

					Vector pdata = (Vector) pingdata.get(ip);
					// ��ping�õ������ݼӽ�ȥ
					if (pdata != null && pdata.size() > 0) {
						for (int m = 0; m < pdata.size(); m++) {
							Pingcollectdata hostdata = (Pingcollectdata) pdata.get(m);
							if (hostdata.getSubentity().equals("ConnectUtilization")) {
								reporthash.put("time", hostdata.getCollecttime());
								reporthash.put("Ping", hostdata.getThevalue());
								reporthash.put("ping", maxping);
							}
						}
					}

					// CPU
					Hashtable hdata = (Hashtable) sharedata.get(ip);
					if (hdata == null)
						hdata = new Hashtable();
					Vector cpuVector = new Vector();
					if (hdata.get("cpu") != null)
						cpuVector = (Vector) hdata.get("cpu");
					if (cpuVector != null && cpuVector.size() > 0) {
						// for(int si=0;si<cpuVector.size();si++){
						CPUcollectdata cpudata = (CPUcollectdata) cpuVector.elementAt(0);
						maxhash.put("cpu", cpudata.getThevalue());
						reporthash.put("CPU", maxhash);
						// }
					} else {
						reporthash.put("CPU", maxhash);
					}
					reporthash.put("equipname", equipname);
					reporthash.put("memmaxhash", memmaxhash);
					reporthash.put("memavghash", memavghash);
					allreporthash.put(ip, reporthash);
				}
			}
			AbstractionReport1 report = new ExcelReport1(new IpResourceReport(), allreporthash);
			report.createReport_networkall("/temp/networknms_report.xls");
			request.setAttribute("filename", report.getFileName());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/capreport/net/download.jsp";
		// return mapping.findForward("report_info");
	}

	private void p_draw_line(Hashtable hash, String title1, String title2, int w, int h) {
		List list = (List) hash.get("list");
		try {
			if (list == null || list.size() == 0) {
				draw_blank(title1, title2, w, h);
			} else {
				String unit = (String) hash.get("unit");
				if (unit == null)
					unit = "%";
				ChartGraph cg = new ChartGraph();

				TimeSeries ss = new TimeSeries(title1, Minute.class);
				TimeSeries[] s = { ss };
				for (int j = 0; j < list.size(); j++) {
					Vector v = (Vector) list.get(j);
					Double d = new Double((String) v.get(0));
					String dt = (String) v.get(1);
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Date time1 = sdf.parse(dt);
					Calendar temp = Calendar.getInstance();
					temp.setTime(time1);
					Minute minute = new Minute(temp.get(Calendar.MINUTE), temp.get(Calendar.HOUR_OF_DAY), temp.get(Calendar.DAY_OF_MONTH), temp.get(Calendar.MONTH) + 1, temp.get(Calendar.YEAR));
					ss.addOrUpdate(minute, d);
				}
				cg.timewave(s, "x(ʱ��)", "y(" + unit + ")", title1, title2, w, h);

			}
			hash = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void draw_blank(String title1, String title2, int w, int h) {
		ChartGraph cg = new ChartGraph();
		TimeSeries ss = new TimeSeries(title1, Minute.class);
		TimeSeries[] s = { ss };
		try {
			Calendar temp = Calendar.getInstance();
			Minute minute = new Minute(temp.get(Calendar.MINUTE), temp.get(Calendar.HOUR_OF_DAY), temp.get(Calendar.DAY_OF_MONTH), temp.get(Calendar.MONTH) + 1, temp.get(Calendar.YEAR));
			ss.addOrUpdate(minute, null);
			cg.timewave(s, "x(ʱ��)", "y", title1, title2, w, h);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String readyEdit() {
		DaoInterface dao = new HostNodeDao();
		setTarget("/topology/network/edit.jsp");
		return readyEdit(dao);
	}

	private String update() {
		HostNode vo = new HostNode();
		vo.setId(getParaIntValue("id"));
		vo.setAlias(getParaValue("alias"));
		vo.setManaged(getParaIntValue("managed") == 1 ? true : false);

		// �����ڴ�
		Host host = (Host) PollingEngine.getInstance().getNodeByID(vo.getId());
		host.setAlias(vo.getAlias());
		host.setManaged(vo.isManaged());

		// �������ݿ�
		DaoInterface dao = new HostNodeDao();
		setTarget("/network.do?action=list");
		return update(dao, vo);
	}

	private String refreshsysname() {
		HostNodeDao dao = new HostNodeDao();
		String sysName = "";
		sysName = dao.refreshSysName(getParaIntValue("id"));

		// �����ڴ�
		Host host = (Host) PollingEngine.getInstance().getNodeByID(getParaIntValue("id"));
		if (host != null) {
			host.setSysName(sysName);
			host.setAlias(sysName);
		}

		return "/network.do?action=list";
	}

	private String delete() {
		String id = getParaValue("radio");

		PollingEngine.getInstance().deleteNodeByID(Integer.parseInt(id));
		HostNodeDao dao = new HostNodeDao();
		dao.delete(id);
		return "/network.do?action=list";
	}

	// zhushouzhi12-18��������ͨ�ʱ���

	public void createDocContext(String file) throws DocumentException, IOException {
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
		//Paragraph title = new Paragraph("������������ͨ�ʱ���", titleFont);
		Paragraph title = new Paragraph("��ͨ�ʱ���", titleFont);
		// ���ñ����ʽ���뷽ʽ
		title.setAlignment(Element.ALIGN_CENTER);
		// title.setFont(titleFont);
		document.add(title);
		// ���� Table ���
		Font fontChinese = new Font(bfChinese, 12, Font.NORMAL, Color.black);
		List pinglist = (List) session.getAttribute("pinglist");
		Table aTable = new Table(8);
		int width[] = { 30, 50, 50, 70, 50, 50, 60, 60 };
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

		Cell cell0 = new Cell("");
		cell0.setBackgroundColor(Color.LIGHT_GRAY);
		aTable.addCell(cell0);
		Cell cell1 = new Cell("IP��ַ");
		Cell cell11 = new Cell("�豸����");
		Cell cell2 = new Cell("����ϵͳ");
		Cell cell3 = new Cell("ƽ����ͨ��");
		Cell cell4 = new Cell("崻�����");
		Cell cell13 = null;
		cell13 = new Cell("ƽ����Ӧʱ��(ms)");
		Cell cell14 = new Cell("�����Ӧʱ��(ms)");
		cell13.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell11.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell14.setHorizontalAlignment(Element.ALIGN_CENTER);

		cell13.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell11.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell4.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell14.setVerticalAlignment(Element.ALIGN_MIDDLE);

		cell1.setBackgroundColor(Color.LIGHT_GRAY);
		cell11.setBackgroundColor(Color.LIGHT_GRAY);
		cell13.setBackgroundColor(Color.LIGHT_GRAY);
		cell14.setBackgroundColor(Color.LIGHT_GRAY);
		cell2.setBackgroundColor(Color.LIGHT_GRAY);
		cell3.setBackgroundColor(Color.LIGHT_GRAY);
		cell4.setBackgroundColor(Color.LIGHT_GRAY);
		aTable.addCell(cell1);
		aTable.addCell(cell11);
		aTable.addCell(cell2);
		aTable.addCell(cell3);
		aTable.addCell(cell4);
		aTable.addCell(cell13);
		aTable.addCell(cell14);
		if (pinglist != null && pinglist.size() > 0) {
			for (int i = 0; i < pinglist.size(); i++) {
				List _pinglist = (List) pinglist.get(i);
				String ip = (String) _pinglist.get(0);
				String equname = (String) _pinglist.get(1);
				String osname = (String) _pinglist.get(2);
				String avgping = (String) _pinglist.get(3);
				String downnum = (String) _pinglist.get(4);
				String responseavg = (String) _pinglist.get(5);
				String responsemax = (String) _pinglist.get(6);
				Cell cell5 = new Cell(i + 1 + "");
				Cell cell6 = new Cell(ip);
				Cell cell7 = new Cell(equname);
				Cell cell8 = new Cell(osname);
				Cell cell9 = new Cell(avgping);
				Cell cell10 = new Cell(downnum);
				Cell cell15 = new Cell(responseavg.replace("����", ""));
				Cell cell16 = new Cell(responsemax.replace("����", ""));
				cell5.setHorizontalAlignment(Element.ALIGN_CENTER); // ����
				cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell7.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell8.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell9.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell10.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell15.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell16.setHorizontalAlignment(Element.ALIGN_CENTER);

				aTable.addCell(cell5);
				aTable.addCell(cell6);
				aTable.addCell(cell7);
				aTable.addCell(cell8);
				aTable.addCell(cell9);
				aTable.addCell(cell10);
				aTable.addCell(cell15);
				aTable.addCell(cell16);

			}
		}
		// ������ͨ��
		String pingpath = (String) session.getAttribute("pingpath");
		Image img = Image.getInstance(pingpath);
		img.setAbsolutePosition(0, 0);
		img.setAlignment(Image.LEFT);// ����ͼƬ��ʾλ��
		document.add(img);
		// ������Ӧʱ��ͼƬ
		String responsetimepath = (String) session.getAttribute("responsetimepath");
		img = Image.getInstance(responsetimepath);
		img.setAbsolutePosition(0, 0);
		img.setAlignment(Image.LEFT);// ����ͼƬ��ʾλ��
		document.add(img);
		document.add(aTable);
		document.close();
	}

	// zhushouzhi���÷�������ͨ�ʱ�����
	public String createdoc() {
		String file = "/temp/liantonglvbaobiao.doc";// ���浽��Ŀ�ļ����µ�ָ���ļ���
		String fileName = ResourceCenter.getInstance().getSysPath() + file;// ��ȡϵͳ�ļ���·��
		try {
			createDocContext(fileName);
//			request.getSession().removeAttribute("pingpath");
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		request.setAttribute("filename", fileName);
		return "/capreport/net/download.jsp";
	}

	// zhushouzhi�������¼�����
	public void createDocContextEvent(String file) throws DocumentException, IOException {
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
		Paragraph title = new Paragraph("�����������¼�����", titleFont);
		// ���ñ����ʽ���뷽ʽ
		title.setAlignment(Element.ALIGN_CENTER);
		// title.setFont(titleFont);
		document.add(title);
		// ���� Table ���
		Font fontChinese = new Font(bfChinese, 12, Font.NORMAL, Color.black);
		List eventlist = (List) session.getAttribute("eventlist");
		Table aTable = new Table(12);
		// int width[] = { 30, 50, 55, 55, 55, 40, 40, 40, 55, 55, 55, 55 };
		// aTable.setWidths(width);
		aTable.setWidth(100); // ռҳ���� 90%
		aTable.setAlignment(Element.ALIGN_CENTER);// ������ʾ
		aTable.setAutoFillEmptyCells(true); // �Զ�����
		aTable.setBorderWidth(1); // �߿���
		aTable.setBorderColor(new Color(0, 125, 255)); // �߿���ɫ
		aTable.setPadding(2);// �ľ࣬��Ч����֪��ʲô��˼��
		aTable.setSpacing(0);// ����Ԫ��֮��ļ��
		aTable.setBorder(2);// �߿�
		aTable.endHeaders();

		Cell cell0 = new Cell("");
		cell0.setBackgroundColor(Color.LIGHT_GRAY);
		aTable.addCell(cell0);
		Cell cell1 = new Cell("IP��ַ");
		Cell cell2 = new Cell("�豸����");
		Cell cell3 = new Cell("����ϵͳ");
		Cell cell4 = new Cell("�¼�����(��)");
		Cell cell5 = new Cell("��ͨ(��)");
		Cell cell6 = new Cell("����(��)");
		Cell cell7 = new Cell("����(��)");
		Cell cell8 = new Cell("��ͨ���¼�(��)");
		Cell cell9 = new Cell("cpu�¼�(��)");
		Cell cell10 = new Cell("�˿��¼�(��)");
		Cell cell11 = new Cell("�����¼�(��)");

		cell1.setBackgroundColor(Color.LIGHT_GRAY);
		cell2.setBackgroundColor(Color.LIGHT_GRAY);
		cell3.setBackgroundColor(Color.LIGHT_GRAY);
		cell4.setBackgroundColor(Color.LIGHT_GRAY);
		cell5.setBackgroundColor(Color.LIGHT_GRAY);
		cell6.setBackgroundColor(Color.LIGHT_GRAY);
		cell7.setBackgroundColor(Color.LIGHT_GRAY);
		cell8.setBackgroundColor(Color.LIGHT_GRAY);
		cell9.setBackgroundColor(Color.LIGHT_GRAY);
		cell10.setBackgroundColor(Color.LIGHT_GRAY);
		cell11.setBackgroundColor(Color.LIGHT_GRAY);

		cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell4.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell5.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell6.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell7.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell8.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell9.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell10.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell11.setVerticalAlignment(Element.ALIGN_MIDDLE);

		cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell7.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell8.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell9.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell10.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell11.setHorizontalAlignment(Element.ALIGN_CENTER);

		aTable.addCell(cell1);
		aTable.addCell(cell2);
		aTable.addCell(cell3);
		aTable.addCell(cell4);
		aTable.addCell(cell5);
		aTable.addCell(cell6);
		aTable.addCell(cell7);
		aTable.addCell(cell8);
		aTable.addCell(cell9);
		aTable.addCell(cell10);
		aTable.addCell(cell11);

		if (eventlist != null && eventlist.size() > 0) {
			for (int i = 0; i < eventlist.size(); i++) {
				List _eventlist = (List) eventlist.get(i);
				String ip = (String) _eventlist.get(0);
				String equname = (String) _eventlist.get(1);
				String osname = (String) _eventlist.get(2);
				String sum = (String) _eventlist.get(3);
				String levelone = (String) _eventlist.get(4);
				String leveltwo = (String) _eventlist.get(5);
				String levelthree = (String) _eventlist.get(6);
				String pingvalue = (String) _eventlist.get(7);
				String memvalue = (String) _eventlist.get(8);
				String diskvalue = (String) _eventlist.get(9);
				String cpuvalue = (String) _eventlist.get(10);
				Cell cell13 = new Cell(i + 1 + "");
				Cell cell14 = new Cell(ip);
				Cell cell15 = new Cell(equname);
				Cell cell16 = new Cell(osname);
				Cell cell17 = new Cell(sum);
				Cell cell18 = new Cell(levelone);
				Cell cell19 = new Cell(leveltwo);
				Cell cell20 = new Cell(levelthree);
				Cell cell21 = new Cell(pingvalue);
				Cell cell22 = new Cell(memvalue);
				Cell cell23 = new Cell(diskvalue);
				Cell cell24 = new Cell(cpuvalue);

				/*
				 * cell5.setHorizontalAlignment(Element.ALIGN_CENTER); //����
				 * cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
				 * cell7.setHorizontalAlignment(Element.ALIGN_CENTER);
				 * cell8.setHorizontalAlignment(Element.ALIGN_CENTER);
				 * cell9.setHorizontalAlignment(Element.ALIGN_CENTER);
				 * cell10.setHorizontalAlignment(Element.ALIGN_CENTER);
				 */
				/*
				 * if(i == 1){ Cell cell12 = new Cell(i + 1 + ""+ip);
				 * cell12.setHorizontalAlignment(Element.ALIGN_CENTER);
				 * cell12.setColspan(2);//�ϲ����еĵ�Ԫ�� aTable.addCell(cell12);
				 * aTable.addCell(cell7); aTable.addCell(cell8);
				 * aTable.addCell(cell9); aTable.addCell(cell10); }
				 */

				aTable.addCell(cell13);
				aTable.addCell(cell14);
				aTable.addCell(cell15);
				aTable.addCell(cell16);
				aTable.addCell(cell17);
				aTable.addCell(cell18);
				aTable.addCell(cell19);
				aTable.addCell(cell20);
				aTable.addCell(cell21);
				aTable.addCell(cell22);
				aTable.addCell(cell23);
				aTable.addCell(cell24);
			}
		}
		document.add(aTable);
		document.add(new Paragraph("\n"));
		document.close();
	}

	// zhushouzhi���÷������¼�������
	public String createdocEvent() {
		String file = "/temp/shijianbaobiao.doc";// ���浽��Ŀ�ļ����µ�ָ���ļ���
		String fileName = ResourceCenter.getInstance().getSysPath() + file;// ��ȡϵͳ�ļ���·��
		try {
			createDocContextEvent(fileName);
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		request.setAttribute("filename", fileName);
		return "/capreport/net/download.jsp";
	}

	// ֮��Ϊzhushouzhi
	// wxy add
	public String networkReport() {
		StringBuffer s = new StringBuffer();

		User current_user = (User) session.getAttribute(SessionConstant.CURRENT_USER);
		if (current_user.getBusinessids() != null) {
			if (current_user.getBusinessids() != "-1") {
				String[] bids = current_user.getBusinessids().split(",");
				if (bids.length > 0) {
					for (int ii = 0; ii < bids.length; ii++) {
						if (bids[ii].trim().length() > 0) {
							s.append(" bid like '%").append(bids[ii]).append("%' ");
							if (ii != bids.length - 1)
								s.append(" or ");
						}
					}

				}

			}
		}
		// InterfaceTempDao interfaceDao = new InterfaceTempDao();
		PortconfigDao portconfigDao = new PortconfigDao();
		List interfaceList = new ArrayList();
		try {
			interfaceList = portconfigDao.getAllBySms();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			portconfigDao.close();
		}
		List networkList = new ArrayList();
		HostNodeDao dao = new HostNodeDao();
		try {
			networkList = dao.loadNetworkByBid(10, current_user.getBusinessids());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dao.close();
		}
		request.setAttribute("list", networkList);
		request.setAttribute("interfaceList", interfaceList);
		return "/capreport/net/netWorkReport.jsp";
	}

	// wxy add ͳ�Ʊ���
	private String statisticsReport() {
		BusinessDao businessDao = new BusinessDao();
		List allbusiness = null;
		try {
			allbusiness = businessDao.loadAll();
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		request.setAttribute("allbusiness", allbusiness);
		return "/capreport/statistics/statisticsReport.jsp";
	}

	private String networkReportConfig() {
		String id = this.getParaValue("id");

		UtilReportDao dao = new UtilReportDao();
		UtilReport report = dao.findByBid(id);
		String ids = report.getIds();
		String[] idValue = null;
		if (ids != null && !ids.equals("null") && !ids.equals("")) {
			idValue = new String[ids.split(",").length];
			idValue = ids.split(",");
		}
		List<String> list = new ArrayList<String>();
		if (idValue != null) {
			for (int i = 0; i < idValue.length; i++) {
				list.add(idValue[i]);
			}
		}
		SubscribeResources vo = new SubscribeResources();
		SubscribeResourcesDao subDao = new SubscribeResourcesDao();

		vo = subDao.findById(id);
		// ////////////
		String[] frequencyName = { "ÿ��", "ÿ��", "ÿ��", "ÿ��", "ÿ��" };
		String[] monthCh = { " 1��", " 2��", " 3��", " 4��", " 5��", " 6��", " 7��", " 8��", " 9��", " 10��", " 11��", " 12��" };
		String[] weekCh = { " ������", " ����һ", " ���ڶ�", " ������", " ������", " ������", " ������" };
		String[] dayCh = null;
		String[] hourCh = null;

		StringBuffer sb = new StringBuffer();
		int frequency = vo.getReport_sendfrequency();
		String month = splitDate(vo.getReport_time_month(), monthCh, "month");
		String week = splitDate(vo.getReport_time_week(), weekCh, "week");

		String day = splitDate(vo.getReport_time_day(), dayCh, "day");
		String hour = splitDate(vo.getReport_time_hou(), hourCh, "hour");
		sb.append(frequencyName[frequency - 1] + " ");
		if (month != null && !month.equals(""))
			sb.append(" �·ݣ�(" + month + ")");
		if (week != null && !week.equals(""))
			sb.append(" ����:(" + week + ")");
		if (day != null && !day.equals(""))
			sb.append(" ���ڣ�(" + day + ")");
		if (hour != null && !hour.equals(""))
			sb.append(" ʱ�䣺(" + hour + ")");
		String sendDate = sb.toString();
		// ///////////
		Vector vector = new Vector();
		vector.add(0, report);
		vector.add(1, vo);
		request.setAttribute("vector", vector);
		request.setAttribute("list", list);
		request.setAttribute("sendDate", sendDate);
		return "/capreport/net/netWorkReportDetail.jsp";
	}

	public String splitDate(String item, String[] itemCh, String type) {
		String[] idValue = null;
		String value = "";
		idValue = new String[item.split("/").length];
		idValue = item.split("/");

		for (int i = 0; i < idValue.length; i++) {
			if (!idValue[i].equals("")) {
				if (type.equals("week")) {
					value += itemCh[Integer.parseInt(idValue[i])];
				} else if (type.equals("day")) {
					value += (idValue[i] + "�� ");
				} else if (type.equals("hour")) {
					value += (idValue[i] + "ʱ ");
				} else {
					value += itemCh[Integer.parseInt(idValue[i]) - 1];
				}
			}
		}

		return value;
	}

	public String executeReport() {
		Hashtable cpuhash = new Hashtable();

		String id = request.getParameter("id");
		String startTime = request.getParameter("startTime");
		if (startTime == null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			startTime = sdf.format(new Date()) + " 00:00:00";
		}
		String toTime = request.getParameter("toTime");
		if (toTime == null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			toTime = sdf.format(new Date()) + " 23:59:59";
		}
		HostNodeDao hostdao = new HostNodeDao();
		id = "1";
		Host host = (Host) PollingEngine.getInstance().getNodeByID(Integer.parseInt(id));
		try {
			cpuhash = hostmanager.getCategory(host.getIpAddress(), "CPU", "Utilization", startTime, toTime);
		} catch (Exception e) {
			e.printStackTrace();
		}
		request.setAttribute("cpuhash", cpuhash);
		return "/capreport/net/netWorkReport.jsp";
	}

	private String downloadReport() {
		String ids = getParaValue("ids");
		String type = getParaValue("type");
		String exportType = getParaValue("exportType");

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if (ids == null || ids.equals("") || ids.equals("null")) {
			String id = request.getParameter("id");

			if (id.equals("null"))
				return null;
			UtilReport report = new UtilReport();
			UtilReportDao dao = new UtilReportDao();
			report = (UtilReport) dao.findByBid(id);
			ids = report.getIds();
		}
		String filename = "";
		if (type.equals("net")) {
			if (exportType.equals("xls")) {
				filename = "/temp/network_report.xls";
			} else if (exportType.equals("doc")) {
				filename = "/temp/network_report.doc";
			} else if (exportType.equals("pdf")) {
				filename = "/temp/network_report.pdf";
			}
		} else if (type.equals("host")) {
			if (exportType.equals("xls")) {
				filename = "/temp/host_report.xls";
			} else if (exportType.equals("doc")) {
				filename = "/temp/host_report.doc";
			} else if (exportType.equals("pdf")) {
				filename = "/temp/host_report.pdf";
			}
		} else if (type.equals("db")) {
			if (exportType.equals("xls")) {
				filename = "/temp/db_report.xls";
			} else if (exportType.equals("doc")) {
				filename = "/temp/db_report.doc";
			} else if (exportType.equals("pdf")) {
				filename = "/temp/db_report.pdf";
			}
		} else if (type.equals("midware")) {
			if (exportType.equals("xls")) {
				filename = "/temp/db_report.xls";
			} else if (exportType.equals("doc")) {
				filename = "/temp/db_report.doc";
			} else if (exportType.equals("pdf")) {
				filename = "/temp/db_report.pdf";
			}
		}
		String filePath = ResourceCenter.getInstance().getSysPath() + filename;
		String startTime = getParaValue("startdate");
		String toTime = getParaValue("todate");
		int beginHour = getParaIntValue("beginHour");
		int endHour = getParaIntValue("endHour");
		String begin = " " + beginHour + ":00:00";
		String end = " " + endHour + ":59:59";
		if (beginHour < 10) {
			begin = " 0" + beginHour + ":00:00";
		}
		if (endHour < 10) {
			end = " 0" + endHour + ":59:59";
		}
		if (startTime == null) {
			startTime = sdf.format(new Date()) + " 00:00:00";
		} else {
			startTime = startTime + begin;
		}
		if (toTime == null) {
			toTime = sdf.format(new Date()) + " 23:59:59";
		} else {
			toTime = toTime + end;
		}
		ReportExport export = new ReportExport();
		export.exportReport(ids, type, filePath, startTime, toTime, exportType);
		request.setAttribute("filename", filePath);
		return "/capreport/net/download.jsp";
	}

	public String execute(String action) {

		if(action.equalsIgnoreCase("creatXlsEMC")){
			return creatXlsEMC();
		}
		if(action.equalsIgnoreCase("createpdfEMC")){
			return createpdfEMC();
		}
		if(action.equalsIgnoreCase("createdocEMC")){
			return createdocEMC();
		}
		if(action.equalsIgnoreCase("emcenvbakwt")){
			return emcenvbakwt();
		}
		if(action.equalsIgnoreCase("emcenvbakavgwt")){
			return emcenvbakavgwt();
		}
		if(action.equalsIgnoreCase("emcenvmemtmp")){
			return emcenvmemtmp();
		}
		if(action.equalsIgnoreCase("emcenvmemavgtmp")){
			return emcenvmemavgtmp();
		}
		if(action.equalsIgnoreCase("emcenvmemavgwt")){
			return emcenvmemavgwt();
		}
		if(action.equalsIgnoreCase("emcenvmemwt")){
			return emcenvmemwt();
		}
		if(action.equalsIgnoreCase("emcenvarrayavgwt")){
			return emcenvarrayavgwt();
		}
		if(action.equalsIgnoreCase("emcenvarraywt")){
			return emcenvarraywt();
		}
		if(action.equalsIgnoreCase("emcdiskread")){
			return emcdiskread();
		}
		if(action.equalsIgnoreCase("emcdiskwrite")){
			return emcdiskwrite();
		}
		if(action.equalsIgnoreCase("emcdisksoftread")){
			return emcdisksoftread();
		}
		if(action.equalsIgnoreCase("emcdisksoftwrite")){
			return emcdisksoftwrite();
		}
		if(action.equalsIgnoreCase("emcdiskreadkb")){
			return emcdiskreadkb();
		}
		if(action.equalsIgnoreCase("emcdiskwritekb")){
			return emcdiskwritekb();
		}
		if(action.equalsIgnoreCase("emcdiskfree")){
			return emcdiskfree();
		}
		if(action.equalsIgnoreCase("emcdiskbus")){
			return emcdiskbus();
		}
		if(action.equalsIgnoreCase("emcdiskhardread")){
			return emcdiskhardread();
		}
		if(action.equalsIgnoreCase("emcdiskhardwrite")){
			return emcdiskhardwrite();
		}
		
		if(action.equalsIgnoreCase("emcTotalLength")){
			return emcTotalLength();
		}
		if(action.equalsIgnoreCase("emcSofterrors")){
			return emcLunSofterrors();
		}
		if(action.equalsIgnoreCase("emcHarderrors")){
			return emcLunHarderrors();
		}
		if(action.equals("vmwarecpuCR")){
			return vmwarecpuCR();
		}
		if(action.equals("vmwarecputotalCR")){
			return vmwarecpuuseCR();
		}
		if(action.equals("vmwarememCR")){
			return vmwarememCR();
		}
		if(action.equals("vmwarememtotalCR")){
			return vmwarememuseCR();
		}
		if(action.equals("vmwarecpuRP")){
			return vmwarecpuRP();
		}
		if(action.equals("creatXlsVM")){
			return creatXlsVM();
		}
		if(action.equals("createdocVM")){
			return createdocVM();
		}
		if(action.equals("createpdfVM")){
			return createpdfVM();
		}
		if(action.equals("vmwaremem")){
			return vmwaremem();
		}
		if(action.equals("vmwarememin")){
			return vmwarememin();
		}
		if(action.equals("vmwarememout")){
			return vmwarememout();
		}
		if(action.equals("vmwaredisk")){
			return vmwaredisk();
		}
		if(action.equals("vmwarenet")){
			return vmwarenet();
		}
		if(action.equals("vmwarecpuuse")){
			return vmwarecpuuse();
		}
		if(action.equals("vmwarecpu"))
		{
			return vmwarecpu();
		}
		if(action.equals("storageSwitch"))
		{
			return storageLunSwitch();
		}
		if(action.equals("storageLunCon"))
		{
			return storageLunCon();
		}
		if(action.equals("storageNumber"))
		{
			return storageWwnNumber();
		}
		if(action.equals("storageWwnCon"))
		{
			return storageWwnCon();
		}
		if(action.equals("storageSafety"))
		{
			return storageSlunSafety();
		}
		if(action.equals("storageSlunCon"))
		{
			return storageSlunCon();
		}
		if(action.equals("storageHDS"))
		{
			return storageHDS();
		}
		if(action.equals("storageFan"))
		{
			return storageFan();
		}
		if(action.equals("storageEnv")){
			return storageEnv();
		}
		if(action.equals("storageDrive")){
			return storageDrive();
		}
		if(action.equals("storageCpu")){
			return storageCpu();
		}
		if(action.equals("storageCable")){
			return storageCable();
		}
		if(action.equals("storageCache")){
			return storageCache();
		}
		if(action.equals("storageButter")){
			return storageButter();
		}
		if(action.equals("storageMemory")){
			return storageMemory();
		}
		if(action.equals("createdocStorage")){
			return createdocStorage();
		}
		if(action.equals("createpdfStorage")){
			return createpdfStorage();
		}
		if(action.equals("creatXlsStorage")){
			return creatXlsStorage();
		}
		if (action.equals("list"))
			return list();
		if (action.equals("eventlist"))
			return eventlist();
		if (action.equals("netmultilist"))
			return netmultilist();
		if (action.equals("netping"))
			return netping();
		if (action.equals("netevent"))
			return netevent();
		if (action.equals("downloadpingreport"))
			return downloadpingreport();
		if (action.equals("downloadnetpingreport"))
			return downloadnetpingreport();
		if(action.equals("downloadnetnetflowreport"))
			return downloadnetnetflowreport();
		if(action.equals("downloadnetnetflowdetailreport"))
			return downloadnetnetflowdetailreport();
		if (action.equals("downloadneteventreport"))
			return downloadneteventreport();
		if (action.equals("downloadselfnetreport"))
			return downloadselfnetreport();
		if (action.equals("downloadmultinetreport"))
			return downloadmultinetreport();
		if (action.equals("createdoc"))
			return createdoc();
		if (action.equals("createdocEvent"))
			return createdocEvent();
		if (action.equals("createpdf"))
			return createpdf();
		if (action.equals("createdocEventPDF"))
			return createdocEventPDF();
		if (action.equals("choceDoc"))
			return choceDoc();
		if (action.equals("downloadselfnetchocereport"))
			return downloadselfnetchocereport();

		// ɽ���ƶ�oa����ӿ�ƽ������ͳ�Ʊ���konglq��
		if (action.equals("netIfxls")) {
			return downloadsxoaIfreport();
		}
		if (action.equals("showPingReport")) {
			return showNetworkPingReport();
		}
		if (action.equals("showEventReport")) {
			return showNetworkEventReport();
		}
		if (action.equals("showCompositeReport")) {
			return showNetworkCompositeReport();
		}
		if (action.equals("showNetworkConfigReport")) {
			return showNetworkConfigReport();
		}
		if (action.equals("downloadNetworkPingReport")) {
			return downloadNetworkPingReport();
		}
		if (action.equals("downloadNetworkEventReport")) {
			return downloadNetworkEventReport();
		}
		if (action.equals("downloadNetworkCompositeReport")) {
			return downloadNetworkCompositeReport();
		}
		if (action.equals("downloadNetworkConfigReport")) {
			return downloadNetworkConfigReport();
		}
		if (action.equals("find")) {
			return find();
		}
		if (action.equals("networkReport"))
			return networkReport();
		if (action.equals("executeReport"))
			return executeReport();
		if (action.equals("networkReportConfig"))
			return networkReportConfig();
		if (action.equals("downloadReport"))
			return downloadReport();
		if (action.equals("showChoce"))
			return showChoce();
		if (action.equals("statisticsReport"))
			return statisticsReport();
		if(action.equals("showHPstorPingReport"))
			//HP�洢��ͨ�ʱ���
			return showHPstorPingReport();
		if(action.equals("showHPstorDiskReport"))
			//HP�洢���̱���
			return showHPstorDiskReport();
		if(action.equals("showHPstorCompositeReport"))
			//HP�洢�ۺϱ���
			return showHPstorCompositeReport();		
		if(action.equals("downloadHPStorageDiskReport"))
			//����HP�洢���̱���
			return downloadHPStorageDiskReport();
		if(action.equals("downloadHPStorageCompReport"))
			//����HP�洢�ۺϱ���
			return downloadHPStorageCompReport();
		
		setErrorCode(ErrorMessage.ACTION_NO_FOUND);
		return null;
	}

	private void getTime(HttpServletRequest request, String[] time) {
		Calendar current = new GregorianCalendar();
		String key = getParaValue("beginhour");
		if (getParaValue("beginhour") == null) {
			Integer hour = new Integer(current.get(Calendar.HOUR_OF_DAY));
			request.setAttribute("beginhour", new Integer(hour.intValue() - 1));
			request.setAttribute("endhour", hour);
			// mForm.setBeginhour(new Integer(hour.intValue()-1));
			// mForm.setEndhour(hour);
		}
		if (getParaValue("begindate") == null) {
			current.set(Calendar.MINUTE, 59);
			current.set(Calendar.SECOND, 59);
			time[1] = datemanager.getDateDetail(current);
			current.add(Calendar.HOUR_OF_DAY, -1);
			current.set(Calendar.MINUTE, 0);
			current.set(Calendar.SECOND, 0);
			time[0] = datemanager.getDateDetail(current);

			java.text.SimpleDateFormat timeFormatter = new java.text.SimpleDateFormat("yyyy-M-d");
			String begindate = "";
			begindate = timeFormatter.format(new java.util.Date());
			request.setAttribute("begindate", begindate);
			request.setAttribute("enddate", begindate);
			// mForm.setBegindate(begindate);
			// mForm.setEnddate(begindate);
		} else {
			String temp = getParaValue("begindate");
			time[0] = temp + " " + getParaValue("beginhour") + ":00:00";
			temp = getParaValue("enddate");
			time[1] = temp + " " + getParaValue("endhour") + ":59:59";
		}
		if (getParaValue("startdate") == null) {
			current.set(Calendar.MINUTE, 59);
			current.set(Calendar.SECOND, 59);
			time[1] = datemanager.getDateDetail(current);
			current.add(Calendar.HOUR_OF_DAY, -1);
			current.set(Calendar.MINUTE, 0);
			current.set(Calendar.SECOND, 0);
			time[0] = datemanager.getDateDetail(current);

			java.text.SimpleDateFormat timeFormatter = new java.text.SimpleDateFormat("yyyy-M-d");
			String startdate = "";
			startdate = timeFormatter.format(new java.util.Date());
			request.setAttribute("startdate", startdate);
			request.setAttribute("todate", startdate);
			// mForm.setStartdate(startdate);
			// mForm.setTodate(startdate);
		} else {
			String temp = getParaValue("startdate");
			time[0] = temp + " " + getParaValue("beginhour") + ":00:00";
			temp = getParaValue("todate");
			time[1] = temp + " " + getParaValue("endhour") + ":59:59";
		}

	}

	private String doip(String ip) {
		// String newip="";
		// for(int i=0;i<3;i++){
		// int p=ip.indexOf(".");
		// newip+=ip.substring(0,p);
		// ip=ip.substring(p+1);
		// }
		// newip+=ip;
		// //System.out.println("newip="+newip);
		// return newip;
		ip = ip.replaceAll("\\.", "_");
		return ip;
	}

	private void p_drawchartMultiLineMonth(Hashtable hash, String title1, String title2, int w, int h, String flag) {
		if (hash.size() != 0) {
			// String unit = (String)hash.get("unit");
			// hash.remove("unit");
			String unit = "";
			String[] keys = (String[]) hash.get("key");
			ChartGraph cg = new ChartGraph();
			TimeSeries[] s = new TimeSeries[keys.length];
			try {
				for (int i = 0; i < keys.length; i++) {
					String key = keys[i];
					// TimeSeries ss = new TimeSeries(key,Hour.class);
					TimeSeries ss = new TimeSeries(key, Minute.class);
					String[] value = (String[]) hash.get(key);
					if (flag.equals("UtilHdx")) {
						unit = "y(kb/s)";
					} else {
						unit = "y(%)";
					}
					// ����
					for (int j = 0; j < value.length; j++) {
						String val = value[j];
						if (val != null && val.indexOf("&") >= 0) {
							String[] splitstr = val.split("&");
							String splittime = splitstr[0];
							Double v = new Double(splitstr[1]);
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							Date da = sdf.parse(splittime);
							Calendar tempCal = Calendar.getInstance();
							tempCal.setTime(da);
							// UtilHdx obj = (UtilHdx)vector.get(j);
							// Double v=new Double(obj.getThevalue());
							// Calendar temp = obj.getCollecttime();
							// new org.jfree.data.time.Hour(newTime)

							// Hour hour=new
							// Hour(tempCal.get(Calendar.HOUR_OF_DAY),tempCal.get(Calendar.DAY_OF_MONTH),tempCal.get(Calendar.MONTH)+1,tempCal.get(Calendar.YEAR));
							// Day day=new
							// Day(tempCal.get(Calendar.DAY_OF_MONTH),tempCal.get(Calendar.MONTH)+1,tempCal.get(Calendar.YEAR));
							// ss.addOrUpdate(new
							// org.jfree.data.time.Day(da),v);
							// ss.addOrUpdate(hour,v);
							Minute minute = new Minute(tempCal.get(Calendar.MINUTE), tempCal.get(Calendar.HOUR_OF_DAY), tempCal.get(Calendar.DAY_OF_MONTH), tempCal.get(Calendar.MONTH) + 1, tempCal
									.get(Calendar.YEAR));
							ss.addOrUpdate(minute, v);
						}
					}
					// }
					s[i] = ss;
				}
				cg.timewave(s, "x(ʱ��)", unit, title1, title2, w, h);
				hash = null;
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			draw_blank(title1, title2, w, h);
		}
	}

	private void p_drawchartMultiLineYear(Hashtable hash, String title1, String title2, int w, int h, String flag) {
		if (hash.size() != 0) {
			// String unit = (String)hash.get("unit");
			// hash.remove("unit");
			String unit = "";
			String[] keys = (String[]) hash.get("key");
			ChartGraph cg = new ChartGraph();
			TimeSeries[] s = new TimeSeries[keys.length];
			try {
				for (int i = 0; i < keys.length; i++) {
					String key = keys[i];
					TimeSeries ss = new TimeSeries(key, Hour.class);
					// TimeSeries ss = new TimeSeries(key,Minute.class);
					String[] value = (String[]) hash.get(key);
					if (flag.equals("UtilHdx")) {
						unit = "y(kb/s)";
					} else {
						unit = "y(%)";
					}
					// ����
					for (int j = 0; j < value.length; j++) {
						String val = value[j];
						if (val != null && val.indexOf("&") >= 0) {
							String[] splitstr = val.split("&");
							String splittime = splitstr[0];
							Double v = new Double(splitstr[1]);
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							Date da = sdf.parse(splittime);
							Calendar tempCal = Calendar.getInstance();
							tempCal.setTime(da);
							// UtilHdx obj = (UtilHdx)vector.get(j);
							// Double v=new Double(obj.getThevalue());
							// Calendar temp = obj.getCollecttime();
							// new org.jfree.data.time.Hour(newTime)

							// Hour hour=new
							// Hour(tempCal.get(Calendar.HOUR_OF_DAY),tempCal.get(Calendar.DAY_OF_MONTH),tempCal.get(Calendar.MONTH)+1,tempCal.get(Calendar.YEAR));
							// Day day=new
							// Day(tempCal.get(Calendar.DAY_OF_MONTH),tempCal.get(Calendar.MONTH)+1,tempCal.get(Calendar.YEAR));
							ss.addOrUpdate(new org.jfree.data.time.Hour(da), v);
							// Minute minute=new
							// Minute(tempCal.get(Calendar.MINUTE),tempCal.get(Calendar.HOUR_OF_DAY),tempCal.get(Calendar.DAY_OF_MONTH),tempCal.get(Calendar.MONTH)+1,tempCal.get(Calendar.YEAR));
							// ss.addOrUpdate(day,v);
						}
					}
					// }
					s[i] = ss;
				}
				cg.timewave(s, "x(ʱ��)", unit, title1, title2, w, h);
				hash = null;
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			draw_blank(title1, title2, w, h);
		}
	}

	private void drawchartMultiLineMonth(Hashtable hash, String title1, String title2, int w, int h, String flag) {
		if (hash.size() != 0) {
			// String unit = (String)hash.get("unit");
			// hash.remove("unit");
			String[] keys = (String[]) hash.get("key");
			ChartGraph cg = new ChartGraph();
			TimeSeries[] s = new TimeSeries[keys.length];
			try {
				for (int i = 0; i < keys.length; i++) {
					String key = keys[i];
					// TimeSeries ss = new TimeSeries(key,Hour.class);
					TimeSeries ss = new TimeSeries(key, Minute.class);
					String[] value = (String[]) hash.get(key);
					if (flag.equals("UtilHdx")) {
						// ����
						for (int j = 0; j < value.length; j++) {
							String val = value[j];
							if (val != null && val.indexOf("&") >= 0) {
								String[] splitstr = val.split("&");
								String splittime = splitstr[0];
								Double v = new Double(splitstr[1]);
								SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
								Date da = sdf.parse(splittime);
								Calendar tempCal = Calendar.getInstance();
								tempCal.setTime(da);
								// UtilHdx obj = (UtilHdx)vector.get(j);
								// Double v=new Double(obj.getThevalue());
								// Calendar temp = obj.getCollecttime();
								// new org.jfree.data.time.Hour(newTime)

								// Hour hour=new
								// Hour(tempCal.get(Calendar.HOUR_OF_DAY),tempCal.get(Calendar.DAY_OF_MONTH),tempCal.get(Calendar.MONTH)+1,tempCal.get(Calendar.YEAR));
								// Day day=new
								// Day(tempCal.get(Calendar.DAY_OF_MONTH),tempCal.get(Calendar.MONTH)+1,tempCal.get(Calendar.YEAR));
								// ss.addOrUpdate(new
								// org.jfree.data.time.Day(da),v);
								// ss.addOrUpdate(hour,v);
								Minute minute = new Minute(tempCal.get(Calendar.MINUTE), tempCal.get(Calendar.HOUR_OF_DAY), tempCal.get(Calendar.DAY_OF_MONTH), tempCal.get(Calendar.MONTH) + 1,
										tempCal.get(Calendar.YEAR));
								ss.addOrUpdate(minute, v);
							}
						}
					}
					s[i] = ss;
				}
				cg.timewave(s, "x(ʱ��)", "y(kb/s)", title1, title2, w, h);
				hash = null;
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			draw_blank(title1, title2, w, h);
		}
	}

	private void p_drawchartMultiLine(Hashtable hash, String title1, String title2, int w, int h, String flag) {
		if (hash.size() != 0) {
			String unit = (String) hash.get("unit");
			hash.remove("unit");
			String[] keys = (String[]) hash.get("key");
			if (keys == null) {
				draw_blank(title1, title2, w, h);
				return;
			}
			ChartGraph cg = new ChartGraph();
			TimeSeries[] s = new TimeSeries[keys.length];
			try {
				for (int i = 0; i < keys.length; i++) {
					String key = keys[i];
					TimeSeries ss = new TimeSeries(key, Minute.class);
					Vector vector = (Vector) (hash.get(key));
					if (flag.equals("AllUtilHdxPerc")) {
						// �ۺϴ���������
						for (int j = 0; j < vector.size(); j++) {
							/*
							 * //if
							 * (title1.equals("����������")||title1.equals("�˿�����")){
							 * AllUtilHdxPerc obj =
							 * (AllUtilHdxPerc)vector.get(j); Double v=new
							 * Double(obj.getThevalue()); Calendar temp =
							 * obj.getCollecttime(); Minute minute=new
							 * Minute(temp.get(Calendar.MINUTE),temp.get(Calendar.HOUR_OF_DAY),temp.get(Calendar.DAY_OF_MONTH),temp.get(Calendar.MONTH)+1,temp.get(Calendar.YEAR));
							 * ss.addOrUpdate(minute,v); //}
							 */
						}
					} else if (flag.equals("AllUtilHdx")) {
						// �ۺ�����
						for (int j = 0; j < vector.size(); j++) {
							// if
							// (title1.equals("����������")||title1.equals("�˿�����")){
							AllUtilHdx obj = (AllUtilHdx) vector.get(j);
							Double v = new Double(obj.getThevalue());
							Calendar temp = obj.getCollecttime();
							Minute minute = new Minute(temp.get(Calendar.MINUTE), temp.get(Calendar.HOUR_OF_DAY), temp.get(Calendar.DAY_OF_MONTH), temp.get(Calendar.MONTH) + 1, temp
									.get(Calendar.YEAR));
							ss.addOrUpdate(minute, v);
							// }
						}
					} else if (flag.equals("UtilHdxPerc")) {
						// ����������
						for (int j = 0; j < vector.size(); j++) {
							Vector obj = (Vector) vector.get(j);
							Double v = new Double((String) obj.get(0));
							String dt = (String) obj.get(1);
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							Date time1 = sdf.parse(dt);
							Calendar temp = Calendar.getInstance();
							temp.setTime(time1);
							Minute minute = new Minute(temp.get(Calendar.MINUTE), temp.get(Calendar.HOUR_OF_DAY), temp.get(Calendar.DAY_OF_MONTH), temp.get(Calendar.MONTH) + 1, temp
									.get(Calendar.YEAR));
							ss.addOrUpdate(minute, v);
						}

					} else if (flag.equals("UtilHdx")) {
						// ����
						for (int j = 0; j < vector.size(); j++) {
							Vector obj = (Vector) vector.get(j);
							Double v = new Double((String) obj.get(0));
							String dt = (String) obj.get(1);
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							Date time1 = sdf.parse(dt);
							Calendar temp = Calendar.getInstance();
							temp.setTime(time1);
							Minute minute = new Minute(temp.get(Calendar.MINUTE), temp.get(Calendar.HOUR_OF_DAY), temp.get(Calendar.DAY_OF_MONTH), temp.get(Calendar.MONTH) + 1, temp
									.get(Calendar.YEAR));
							ss.addOrUpdate(minute, v);
						}
					} else if (flag.equals("ErrorsPerc")) {
						// ����
						for (int j = 0; j < vector.size(); j++) {
							Vector obj = (Vector) vector.get(j);
							Double v = new Double((String) obj.get(0));
							String dt = (String) obj.get(1);
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							Date time1 = sdf.parse(dt);
							Calendar temp = Calendar.getInstance();
							temp.setTime(time1);
							Minute minute = new Minute(temp.get(Calendar.MINUTE), temp.get(Calendar.HOUR_OF_DAY), temp.get(Calendar.DAY_OF_MONTH), temp.get(Calendar.MONTH) + 1, temp
									.get(Calendar.YEAR));
							ss.addOrUpdate(minute, v);
						}
					} else if (flag.equals("DiscardsPerc")) {
						// ����
						for (int j = 0; j < vector.size(); j++) {
							Vector obj = (Vector) vector.get(j);
							Double v = new Double((String) obj.get(0));
							String dt = (String) obj.get(1);
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							Date time1 = sdf.parse(dt);
							Calendar temp = Calendar.getInstance();
							temp.setTime(time1);
							Minute minute = new Minute(temp.get(Calendar.MINUTE), temp.get(Calendar.HOUR_OF_DAY), temp.get(Calendar.DAY_OF_MONTH), temp.get(Calendar.MONTH) + 1, temp
									.get(Calendar.YEAR));
							ss.addOrUpdate(minute, v);
						}
					} else if (flag.equals("Packs")) {
						// ���ݰ�
						for (int j = 0; j < vector.size(); j++) {
							Vector obj = (Vector) vector.get(j);
							Double v = new Double((String) obj.get(0));
							String dt = (String) obj.get(1);
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							Date time1 = sdf.parse(dt);
							Calendar temp = Calendar.getInstance();
							temp.setTime(time1);
							Minute minute = new Minute(temp.get(Calendar.MINUTE), temp.get(Calendar.HOUR_OF_DAY), temp.get(Calendar.DAY_OF_MONTH), temp.get(Calendar.MONTH) + 1, temp
									.get(Calendar.YEAR));
							ss.addOrUpdate(minute, v);
						}
					}
					s[i] = ss;
				}
				cg.timewave(s, "x(ʱ��)", "y(" + unit + ")", title1, title2, w, h);
				hash = null;
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			draw_blank(title1, title2, w, h);
		}
	}

	private static CategoryDataset getDataSet() {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		dataset.addValue(10, "", "values1");
		dataset.addValue(20, "", "values2");
		dataset.addValue(30, "", "values3");
		dataset.addValue(40, "", "values4");
		dataset.addValue(50, "", "values5");
		return dataset;
	}

	public void draw_column(Hashtable bighash, String title1, String title2, int w, int h) {
		if (bighash.size() != 0) {
			ChartGraph cg = new ChartGraph();
			int size = bighash.size();
			double[][] d = new double[1][size];
			String c[] = new String[size];
			Hashtable hash;
			for (int j = 0; j < size; j++) {
				hash = (Hashtable) bighash.get(new Integer(j));
				c[j] = (String) hash.get("name");
				d[0][j] = Double.parseDouble((String) hash.get("Utilization" + "value"));
			}
			String rowKeys[] = { "" };
			CategoryDataset dataset = DatasetUtilities.createCategoryDataset(rowKeys, c, d);// .createCategoryDataset(rowKeys,
			// columnKeys, data);
			cg.zhu(title1, title2, dataset, w, h);
		} else {
			draw_blank(title1, title2, w, h);
		}
		bighash = null;
	}

	private void p_drawchartMultiLine(Hashtable hash, String title1, String title2, int w, int h) {
		if (hash.size() != 0) {
			String unit = (String) hash.get("unit");
			hash.remove("unit");
			String[] keys = (String[]) hash.get("key");
			if (keys == null) {
				draw_blank(title1, title2, w, h);
				return;
			}
			ChartGraph cg = new ChartGraph();
			TimeSeries[] s = new TimeSeries[keys.length];
			try {
				for (int i = 0; i < keys.length; i++) {
					String key = keys[i];
					TimeSeries ss = new TimeSeries(key, Minute.class);
					Vector vector = (Vector) (hash.get(key));
					for (int j = 0; j < vector.size(); j++) {
						// if (title1.equals("�ڴ�������")){
						Vector obj = (Vector) vector.get(j);
						// Memorycollectdata obj =
						// (Memorycollectdata)vector.get(j);
						Double v = new Double((String) obj.get(0));
						String dt = (String) obj.get(1);
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						Date time1 = sdf.parse(dt);
						Calendar temp = Calendar.getInstance();
						temp.setTime(time1);
						// Calendar temp = obj.getCollecttime();
						Minute minute = new Minute(temp.get(Calendar.MINUTE), temp.get(Calendar.HOUR_OF_DAY), temp.get(Calendar.DAY_OF_MONTH), temp.get(Calendar.MONTH) + 1, temp.get(Calendar.YEAR));
						ss.addOrUpdate(minute, v);
						// }
					}
					s[i] = ss;
				}
				cg.timewave(s, "x(ʱ��)", "y(" + unit + ")", title1, title2, w, h);
				hash = null;
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			draw_blank(title1, title2, w, h);
		}
	}

	//zhushouzhi--------------------------start pdf
	public void createPdfContextPing(String file) throws DocumentException, IOException {

		// com.lowagie.text.Font FontChinese = new
		// com.lowagie.text.Font(bfChinese, 12, com.lowagie.text.Font.NORMAL);

		// ����ֽ�Ŵ�С

		Document document = new Document(PageSize.A4);
		// ����һ����д��(Writer)��document���������ͨ����д��(Writer)���Խ��ĵ�д�뵽������
		PdfWriter.getInstance(document, new FileOutputStream(file));
		document.open();
		// ������������
		BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
		// ����������
		Font titleFont = new Font(bfChinese, 12, Font.BOLD);
		// com.lowagie.text.Font titleFont = new
		// com.lowagie.text.Font(bfChinese, 12, com.lowagie.text.Font.NORMAL);
		// ����������
		Font contextFont = new com.lowagie.text.Font(bfChinese, 11, Font.NORMAL);
		Paragraph title = new Paragraph("��ͨ�ʱ���", titleFont);
		// ���ñ����ʽ���뷽ʽ
		title.setAlignment(Element.ALIGN_CENTER);
		// title.setFont(titleFont);
		document.add(title);
		document.add(new Paragraph("\n"));
		// ���� Table ���
		Font fontChinese = new Font(bfChinese, 11, Font.NORMAL, Color.black);
		List pinglist = (List) session.getAttribute("pinglist");
		PdfPTable aTable = new PdfPTable(8);

		int width[] = { 30, 50, 50, 70, 50, 50, 60, 60 };
		aTable.setWidthPercentage(100);
		aTable.setWidths(width);

		PdfPCell c = new PdfPCell(new Phrase("", fontChinese));
		c.setBackgroundColor(Color.LIGHT_GRAY);
		aTable.addCell(c);
		PdfPCell cell1 = new PdfPCell(new Phrase("IP��ַ", fontChinese));
		PdfPCell cell11 = new PdfPCell(new Phrase("�豸����", contextFont));
		PdfPCell cell2 = new PdfPCell(new Phrase("����ϵͳ", contextFont));
		PdfPCell cell3 = new PdfPCell(new Phrase("ƽ����ͨ��", contextFont));
		PdfPCell cell4 = new PdfPCell(new Phrase("崻�����", contextFont));
		PdfPCell cell15 = new PdfPCell(new Phrase("ƽ����Ӧʱ��(ms)", contextFont));
		PdfPCell cell16 = new PdfPCell(new Phrase("�����Ӧʱ��(ms)", contextFont));
		cell1.setBackgroundColor(Color.LIGHT_GRAY);
		cell1.setBackgroundColor(Color.LIGHT_GRAY);
		cell11.setBackgroundColor(Color.LIGHT_GRAY);
		cell2.setBackgroundColor(Color.LIGHT_GRAY);
		cell3.setBackgroundColor(Color.LIGHT_GRAY);
		cell4.setBackgroundColor(Color.LIGHT_GRAY);
		cell15.setBackgroundColor(Color.LIGHT_GRAY);
		cell16.setBackgroundColor(Color.LIGHT_GRAY);
		cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell11.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell15.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell16.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell11.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell4.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell15.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell16.setVerticalAlignment(Element.ALIGN_MIDDLE);
		aTable.addCell(cell1);
		aTable.addCell(cell11);
		aTable.addCell(cell2);
		aTable.addCell(cell3);
		aTable.addCell(cell4);
		aTable.addCell(cell15);
		aTable.addCell(cell16);
		if (pinglist != null && pinglist.size() > 0) {
			for (int i = 0; i < pinglist.size(); i++) {
				List _pinglist = (List) pinglist.get(i);
				String ip = (String) _pinglist.get(0);
				String equname = (String) _pinglist.get(1);
				String osname = (String) _pinglist.get(2);
				String avgping = (String) _pinglist.get(3);
				String downnum = (String) _pinglist.get(4);
				String responseavg = (String) _pinglist.get(5);
				String responsemax = (String) _pinglist.get(6);
				PdfPCell cell5 = new PdfPCell(new Phrase(i + 1 + ""));
				PdfPCell cell6 = new PdfPCell(new Phrase(ip));
				PdfPCell cell7 = new PdfPCell(new Paragraph(equname, fontChinese));
				PdfPCell cell8 = new PdfPCell(new Phrase(osname, contextFont));
				PdfPCell cell9 = new PdfPCell(new Phrase(avgping));
				PdfPCell cell10 = new PdfPCell(new Phrase(downnum));

				PdfPCell cell17 = new PdfPCell(new Phrase(responseavg.replace("����", "")));
				PdfPCell cell18 = new PdfPCell(new Phrase(responsemax.replace("����", "")));

				cell5.setHorizontalAlignment(Element.ALIGN_CENTER); // ����
				cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell7.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell8.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell9.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell10.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell17.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell18.setHorizontalAlignment(Element.ALIGN_CENTER);

				aTable.addCell(cell5);
				aTable.addCell(cell6);
				aTable.addCell(cell7);
				aTable.addCell(cell8);
				aTable.addCell(cell9);
				aTable.addCell(cell10);
				aTable.addCell(cell17);
				aTable.addCell(cell18);

			}
		}
		// ������ͨ��
		String pingpath = (String) session.getAttribute("pingpath");
		// ������Ӧʱ��ͼƬ
		String responsetimepath = (String) session.getAttribute("responsetimepath");
		// document.add(new Paragraph("\n"));
		// String ipaddress = (String)request.getParameter("ipaddress");
		// String newip = doip(ipaddress);
		Image img = Image.getInstance(pingpath);
		img.setAlignment(Image.LEFT);// ����ͼƬ��ʾλ��
		img.scalePercent(75);
		document.add(img);
		img = Image.getInstance(responsetimepath);
		img.setAlignment(Image.LEFT);// ����ͼƬ��ʾλ��
		img.scalePercent(75);
		document.add(img);
		document.add(aTable);
		document.close();
	}

	public void createDocContexxtEventPDF() {

	}

	// zhushouzhi���÷�������ͨ�ʱ�����
	public String createpdf() {
		String file = "/temp/liantonglvbaobiao.PDF";// ���浽��Ŀ�ļ����µ�ָ���ļ���
		String fileName = ResourceCenter.getInstance().getSysPath() + file;// ��ȡϵͳ�ļ���·��
		try {
			createPdfContextPing(fileName);
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		request.setAttribute("filename", fileName);
		return "/capreport/net/download.jsp";
	}

	// zhushouzhi���÷�������ͨ�ʱ�����---pdf

	// zhushouzhi-----------------------pdf--eventlist
	public void createDocContextEventPDF(String file) throws DocumentException, IOException {
		// ����ֽ�Ŵ�С
		Document document = new Document(PageSize.A4);
		// ����һ����д��(Writer)��document���������ͨ����д��(Writer)���Խ��ĵ�д�뵽������
		PdfWriter.getInstance(document, new FileOutputStream(file));
		document.open();
		// ������������
		BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);

		// ����������
		Font titleFont = new Font(bfChinese, 12, Font.BOLD);
		// ����������
		Font contextFont = new Font(bfChinese, 10, Font.NORMAL);
		Paragraph title = new Paragraph("�����������¼�����", titleFont);
		// ���ñ����ʽ���뷽ʽ
		title.setAlignment(Element.ALIGN_CENTER);
		title.setSpacingAfter(10);
		// title.setFont(titleFont);
		document.add(title);
		// ���� Table ���
		Font fontChinese = new Font(bfChinese, 12, Font.NORMAL, Color.black);
		List eventlist = (List) session.getAttribute("eventlist");
		PdfPTable aTable = new PdfPTable(12);
		// int width[] = { 30, 70, 55, 55, 55, 40, 40, 40, 55, 55, 55, 55 };
		// aTable.setWidths(width);
		aTable.setWidthPercentage(100);

		PdfPCell cell0 = new PdfPCell(new Phrase(""));
		cell0.setBackgroundColor(Color.LIGHT_GRAY);
		aTable.addCell(cell0);
		PdfPCell cell1 = new PdfPCell(new Phrase("IP��ַ", contextFont));
		PdfPCell cell2 = new PdfPCell(new Phrase("�豸����", contextFont));
		PdfPCell cell3 = new PdfPCell(new Phrase("����ϵͳ", contextFont));
		PdfPCell cell5 = new PdfPCell(new Phrase("�¼�����(��)", contextFont));
		PdfPCell cell61 = new PdfPCell(new Phrase("��ͨ������", contextFont));
		PdfPCell cell6 = new PdfPCell(new Phrase("����(��)", contextFont));
		PdfPCell cell7 = new PdfPCell(new Phrase("����(��)", contextFont));
		PdfPCell cell8 = new PdfPCell(new Phrase("��ͨ���¼�(��)", contextFont));
		PdfPCell cell9 = new PdfPCell(new Phrase("cpu�¼�(��)", contextFont));
		PdfPCell cell10 = new PdfPCell(new Phrase("�˿��¼�(��)", contextFont));
		PdfPCell cell11 = new PdfPCell(new Phrase("�����¼�(��)", contextFont));

		cell1.setBackgroundColor(Color.LIGHT_GRAY);
		cell2.setBackgroundColor(Color.LIGHT_GRAY);
		cell3.setBackgroundColor(Color.LIGHT_GRAY);
		cell5.setBackgroundColor(Color.LIGHT_GRAY);
		cell61.setBackgroundColor(Color.LIGHT_GRAY);
		cell6.setBackgroundColor(Color.LIGHT_GRAY);
		cell7.setBackgroundColor(Color.LIGHT_GRAY);
		cell8.setBackgroundColor(Color.LIGHT_GRAY);
		cell9.setBackgroundColor(Color.LIGHT_GRAY);
		cell10.setBackgroundColor(Color.LIGHT_GRAY);
		cell11.setBackgroundColor(Color.LIGHT_GRAY);

		cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell5.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell61.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell6.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell7.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell8.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell9.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell10.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell11.setVerticalAlignment(Element.ALIGN_MIDDLE);

		cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell61.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell7.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell8.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell9.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell10.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell11.setHorizontalAlignment(Element.ALIGN_CENTER);

		aTable.addCell(cell1);
		aTable.addCell(cell2);
		aTable.addCell(cell3);
		aTable.addCell(cell5);
		aTable.addCell(cell61);
		aTable.addCell(cell6);
		aTable.addCell(cell7);
		aTable.addCell(cell8);
		aTable.addCell(cell9);
		aTable.addCell(cell10);
		aTable.addCell(cell11);
		document.add(aTable);
		aTable = new PdfPTable(12);
		// aTable.setWidths(width);
		aTable.setWidthPercentage(100);
		if (eventlist != null && eventlist.size() > 0) {
			for (int i = 0; i < eventlist.size(); i++) {
				List _eventlist = (List) eventlist.get(i);
				String ip = (String) _eventlist.get(0);
				String equname = (String) _eventlist.get(1);
				String osname = (String) _eventlist.get(2);
				String sum = (String) _eventlist.get(3);
				String levelone = (String) _eventlist.get(4);
				String leveltwo = (String) _eventlist.get(5);
				String levelthree = (String) _eventlist.get(6);
				String pingvalue = (String) _eventlist.get(7);
				String memvalue = (String) _eventlist.get(8);
				String diskvalue = (String) _eventlist.get(9);
				String cpuvalue = (String) _eventlist.get(10);
				PdfPCell cell13 = new PdfPCell(new Phrase(i + 1 + ""));
				PdfPCell cell14 = new PdfPCell(new Phrase(ip));
				PdfPCell cell15 = new PdfPCell(new Phrase(equname, contextFont));
				PdfPCell cell16 = new PdfPCell(new Phrase(osname, contextFont));
				PdfPCell cell17 = new PdfPCell(new Phrase(sum));
				PdfPCell cell18 = new PdfPCell(new Phrase(levelone));
				PdfPCell cell19 = new PdfPCell(new Phrase(levelone));
				PdfPCell cell20 = new PdfPCell(new Phrase(levelthree));
				PdfPCell cell21 = new PdfPCell(new Phrase(levelthree));
				PdfPCell cell22 = new PdfPCell(new Phrase(levelthree));
				PdfPCell cell23 = new PdfPCell(new Phrase(levelthree));
				PdfPCell cell24 = new PdfPCell(new Phrase(levelthree));

				aTable.addCell(cell13);
				aTable.addCell(cell14);
				aTable.addCell(cell15);
				aTable.addCell(cell16);
				aTable.addCell(cell17);
				aTable.addCell(cell18);
				aTable.addCell(cell19);
				aTable.addCell(cell20);
				aTable.addCell(cell21);
				aTable.addCell(cell22);
				aTable.addCell(cell23);
				aTable.addCell(cell24);
			}
		}
		document.add(aTable);
		document.add(new Paragraph("\n"));
		document.close();
	}

	public String createdocEventPDF() {
		String file = "/temp/shijianbaobiao.pdf";// ���浽��Ŀ�ļ����µ�ָ���ļ���
		String fileName = ResourceCenter.getInstance().getSysPath() + file;// ��ȡϵͳ�ļ���·��
		try {
			createDocContextEventPDF(fileName);
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		request.setAttribute("filename", fileName);
		return "/capreport/net/download.jsp";
	}

	public String choceDoc() {
		Date d = new Date();
		String startdate = getParaValue("startdate");
		if (startdate == null) {
			startdate = sdf0.format(d);
		}
		String todate = getParaValue("todate");
		if (todate == null) {
			todate = sdf0.format(d);
		}
		request.setAttribute("startdate", startdate);
		request.setAttribute("todate", todate);
		HostNodeDao dao = new HostNodeDao();
		request.setAttribute("list", dao.loadNetwork(1));
		return "/capreport/net/choce.jsp";
	}

	/**
	 * Ԥ������֧�ֱ���ҳ��
	 * 
	 * @return
	 */
	private String showChoce() {
		String oids = getParaValue("ids");
		if (oids == null)
			oids = "";
		Integer[] ids = null;
		if (oids.split(",").length > 0) {
			String[] _ids = oids.split(",");
			if (_ids != null && _ids.length > 0)
				ids = new Integer[_ids.length];
			for (int i = 0; i < _ids.length; i++) {
				ids[i] = new Integer(_ids[i]);
			}
		}

		Date d = new Date();
		String startdate = getParaValue("startdate");
		if (startdate == null) {
			startdate = sdf0.format(d);
		}
		String todate = getParaValue("todate");
		if (todate == null) {
			todate = sdf0.format(d);
		}
		String starttime = startdate + " 00:00:00";
		String totime = todate + " 23:59:59";
		Hashtable allcpuhash = new Hashtable();
		String ip = "";
		String equipname = "";

		Hashtable hash = new Hashtable();// "Cpu",--current
		Hashtable memhash = new Hashtable();// mem--current
		Hashtable diskhash = new Hashtable();
		Hashtable memmaxhash = new Hashtable();// mem--max
		Hashtable memavghash = new Hashtable();// mem--avg
		Hashtable maxhash = new Hashtable();// "Cpu"--max
		Hashtable maxping = new Hashtable();// Ping--max
		Hashtable pingdata = ShareData.getPingdata();
		Hashtable sharedata = ShareData.getSharedata();
		User vo = (User) session.getAttribute(SessionConstant.CURRENT_USER);
		UserView view = new UserView();

		String positionname = view.getPosition(vo.getPosition());
		String username = vo.getName();
		// String position = vo.getPosition();
		Vector vector = new Vector();
		// Hashtable reporthash = new Hashtable();
		String runAppraise = "��";// ��������
		int levelOneAlarmNum = 0;// �澯������
		int levelTwoAlarmNum = 0;// �澯������
		int levelThreeAlarmNum = 0;// �澯������
		try {
			Hashtable allreporthash = new Hashtable();

			if (ids != null && ids.length > 0) {
				for (int i = 0; i < ids.length; i++) {
					Hashtable reporthash = new Hashtable();
					HostNodeDao dao = new HostNodeDao();
					HostNode node = (HostNode) dao.loadHost(ids[i]);
					dao.close();
					if (node == null)
						continue;
					EventListDao eventdao = new EventListDao();
					// �õ��¼��б�
					StringBuffer s = new StringBuffer();
					s.append("select * from system_eventlist where recordtime>= '" + starttime + "' " + "and recordtime<='" + totime + "' ");
					s.append(" and nodeid=" + node.getId());

					List infolist = eventdao.findByCriteria(s.toString());
					int levelone = 0;
					int levletwo = 0;
					int levelthree = 0;
					if (infolist != null && infolist.size() > 0) {
						for (int j = 0; j < infolist.size(); j++) {
							EventList eventlist = (EventList) infolist.get(j);
							if (eventlist.getContent() == null)
								eventlist.setContent("");
							String content = eventlist.getContent();
							String subentity = eventlist.getSubentity();

							if (eventlist.getContent() == null)
								eventlist.setContent("");

							if (eventlist.getLevel1() != 1) {
								levelone = levelone + 1;
							} else if (eventlist.getLevel1() == 2) {
								levletwo = levletwo + 1;
							} else if (eventlist.getLevel1() == 3) {
								levelthree = levelthree + 1;
							}
						}
					}
					levelOneAlarmNum = levelOneAlarmNum + levelone;
					levelTwoAlarmNum = levelTwoAlarmNum + levletwo;
					levelThreeAlarmNum = levelThreeAlarmNum + levelthree;
					reporthash.put("levelone", levelone + "");
					reporthash.put("levletwo", levletwo + "");
					reporthash.put("levelthree", levelthree + "");
					// ------------------------�¼�end
					ip = node.getIpAddress();
					equipname = node.getAlias();
					String remoteip = request.getRemoteAddr();
					String newip = doip(ip);
					// �������־ȡ���˿����¼�¼���б�
					String orderflag = "index";

					String[] netInterfaceItem = { "index", "ifname", "ifSpeed", "ifOperStatus", "OutBandwidthUtilHdx", "InBandwidthUtilHdx" };

					vector = hostlastmanager.getInterface_share(ip, netInterfaceItem, orderflag, startdate, todate);
					PortconfigDao portdao = new PortconfigDao();
					Hashtable portconfigHash = portdao.getIpsHash(ip);
					// Hashtable portconfigHash =
					// portconfigManager.getIpsHash(ip);
					reporthash.put("portconfigHash", portconfigHash);

					List reportports = portdao.getByIpAndReportflag(ip, new Integer(1));
					reporthash.put("reportports", reportports);
					if (reportports != null && reportports.size() > 0) {
						// ��ʾ�˿ڵ�����ͼ��
						I_HostCollectDataDay daymanager = new HostCollectDataDayManager();
						String unit = "kb/s";
						String title = startdate + "��" + todate + "�˿�����";
						String[] banden3 = { "InBandwidthUtilHdx", "OutBandwidthUtilHdx" };
						String[] bandch3 = { "�������", "��������" };

						for (int k = 0; k < reportports.size(); k++) {
							com.afunms.config.model.Portconfig portconfig = (com.afunms.config.model.Portconfig) reportports.get(k);
							// ��������ʾ����
							Hashtable value = new Hashtable();
							value = daymanager.getmultiHisHdx(ip, "ifspeed", portconfig.getPortindex() + "", banden3, bandch3, startdate, todate, "UtilHdx");
							String reportname = "��" + portconfig.getPortindex() + "(" + portconfig.getName() + ")�˿�����" + startdate + "��" + todate + "����(��������ʾ)";
							p_drawchartMultiLineMonth(value, reportname, newip + portconfig.getPortindex() + "ifspeed_day", 800, 200, "UtilHdx");
							// String url1 =
							// "/resource/image/jfreechart/"+newip+portconfig.getPortindex()+"ifspeed_day.png";
						}
					}

					reporthash.put("netifVector", vector);

					Hashtable cpuhash = hostmanager.getCategory(ip, "CPU", "Utilization", starttime, totime);
					String pingconavg = "";

					maxhash = new Hashtable();
					String cpumax = "";
					String avgcpu = "";
					if (cpuhash.get("max") != null) {
						cpumax = (String) cpuhash.get("max");
					}
					if (cpuhash.get("avgcpucon") != null) {
						avgcpu = (String) cpuhash.get("avgcpucon");
					}

					maxhash.put("cpumax", cpumax);
					maxhash.put("avgcpu", avgcpu);
					// ��ͼ
					p_draw_line(cpuhash, "", newip + "cpu", 740, 120);
					Hashtable ConnectUtilizationhash = hostmanager.getCategory(ip, "Ping", "ConnectUtilization", starttime, totime);
					if (ConnectUtilizationhash.get("avgpingcon") != null)
						pingconavg = (String) ConnectUtilizationhash.get("avgpingcon");
					String ConnectUtilizationmax = "";
					maxping.put("avgpingcon", pingconavg);
					if (ConnectUtilizationhash.get("max") != null) {
						ConnectUtilizationmax = (String) ConnectUtilizationhash.get("max");
					}
					maxping.put("pingmax", ConnectUtilizationmax);

					p_draw_line(ConnectUtilizationhash, "", newip + "ConnectUtilization", 740, 120);

					// ���ڴ��л�õ�ǰ�ĸ���IP��ص�IP-MAC��FDB����Ϣ
					Hashtable _IpRouterHash = ShareData.getIprouterdata();
					vector = (Vector) _IpRouterHash.get(ip);
					if (vector != null)
						reporthash.put("iprouterVector", vector);

					Vector pdata = (Vector) pingdata.get(ip);
					// ��ping�õ������ݼӽ�ȥ
					if (pdata != null && pdata.size() > 0) {
						for (int m = 0; m < pdata.size(); m++) {
							Pingcollectdata hostdata = (Pingcollectdata) pdata.get(m);
							if (hostdata.getSubentity().equals("ConnectUtilization")) {
								reporthash.put("time", hostdata.getCollecttime());
								reporthash.put("Ping", hostdata.getThevalue());
								reporthash.put("ping", maxping);
							}
						}
					}

					// CPU
					Hashtable hdata = (Hashtable) sharedata.get(ip);
					if (hdata == null)
						hdata = new Hashtable();
					Vector cpuVector = new Vector();
					if (hdata.get("cpu") != null)
						cpuVector = (Vector) hdata.get("cpu");
					if (cpuVector != null && cpuVector.size() > 0) {
						// for(int si=0;si<cpuVector.size();si++){
						CPUcollectdata cpudata = (CPUcollectdata) cpuVector.elementAt(0);
						maxhash.put("cpu", cpudata.getThevalue());
						reporthash.put("CPU", maxhash);
						// }
					} else {
						reporthash.put("CPU", maxhash);
					}// -----����
					Hashtable streaminHash = hostmanager.getAllutilhdx(ip, "AllInBandwidthUtilHdx", starttime, totime, "avg");
					Hashtable streamoutHash = hostmanager.getAllutilhdx(ip, "AllOutBandwidthUtilHdx", starttime, totime, "avg");
					String avgput = "";
					if (streaminHash.get("avgput") != null) {
						avgput = (String) streaminHash.get("avgput");
						reporthash.put("avginput", avgput);
					}
					if (streamoutHash.get("avgput") != null) {
						avgput = (String) streamoutHash.get("avgput");
						reporthash.put("avgoutput", avgput);
					}
					Hashtable streammaxinHash = hostmanager.getAllutilhdx(ip, "AllInBandwidthUtilHdx", starttime, totime, "max");
					Hashtable streammaxoutHash = hostmanager.getAllutilhdx(ip, "AllOutBandwidthUtilHdx", starttime, totime, "max");
					String maxput = "";
					if (streammaxinHash.get("max") != null) {
						maxput = (String) streammaxinHash.get("max");
						reporthash.put("maxinput", maxput);
					}
					if (streammaxoutHash.get("max") != null) {
						maxput = (String) streammaxoutHash.get("max");
						reporthash.put("maxoutput", maxput);
					}
					// ------����end

					reporthash.put("starttime", starttime);
					reporthash.put("totime", totime);

					reporthash.put("equipname", equipname);
					reporthash.put("memmaxhash", memmaxhash);
					reporthash.put("memavghash", memavghash);

					allreporthash.put(ip, reporthash);
				}
				runAppraise = SysUtil.getRunAppraise(levelOneAlarmNum, levelTwoAlarmNum, levelThreeAlarmNum);
			}
			// String file = "temp/networknms_report.doc";// ���浽��Ŀ�ļ����µ�ָ���ļ���
			// String fileName = ResourceCenter.getInstance().getSysPath() +
			// file;// ��ȡϵͳ�ļ���·��
			// ExcelReport1 report = new ExcelReport1(new IpResourceReport(),
			// allreporthash);
			//
			// report.createReport_networkchoce(starttime, totime, fileName,
			// username, positionname);
			// request.setAttribute("filename", fileName);
			request.setAttribute("allreporthash", allreporthash);
			request.setAttribute("startdate", starttime);
			request.setAttribute("todate", totime);
			request.setAttribute("username", username);
			request.setAttribute("positionname", positionname);
			request.setAttribute("oids", oids);
			request.setAttribute("runAppraise", runAppraise);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/capreport/net/showChoce.jsp";
	}

	// zhushouzhi------------------------------------end pdf

	// zhushouzhi-----------------------------start
	private String downloadselfnetchocereport() {
		String oids = getParaValue("ids");
		String businessAnalytics = getParaValue("businessAnalytics");// ҵ��ϵͳ���з���
		String runAppraise = getParaValue("runAppraise");// ҵ��ϵͳ����״��
		if (businessAnalytics == null) {
			businessAnalytics = "";
		}
		if (oids == null)
			oids = "";
		Integer[] ids = null;
		if (oids.split(",").length > 0) {
			String[] _ids = oids.split(",");
			if (_ids != null && _ids.length > 0)
				ids = new Integer[_ids.length];
			for (int i = 0; i < _ids.length; i++) {
				ids[i] = new Integer(_ids[i]);
			}
		}

		Date d = new Date();
		String startdate = getParaValue("startdate");
		if (startdate == null) {
			startdate = sdf0.format(d);
		}
		String todate = getParaValue("todate");
		if (todate == null) {
			todate = sdf0.format(d);
		}
		String starttime = startdate;
		String totime = todate;
		Hashtable allcpuhash = new Hashtable();
		String ip = "";
		String equipname = "";

		Hashtable hash = new Hashtable();// "Cpu",--current
		Hashtable memhash = new Hashtable();// mem--current
		Hashtable diskhash = new Hashtable();
		Hashtable memmaxhash = new Hashtable();// mem--max
		Hashtable memavghash = new Hashtable();// mem--avg
		Hashtable maxhash = new Hashtable();// "Cpu"--max
		Hashtable maxping = new Hashtable();// Ping--max
		Hashtable pingdata = ShareData.getPingdata();
		Hashtable sharedata = ShareData.getSharedata();
		User vo = (User) session.getAttribute(SessionConstant.CURRENT_USER);
		UserView view = new UserView();

		String positionname = view.getPosition(vo.getPosition());
		String username = vo.getName();
		// String position = vo.getPosition();
		Vector vector = new Vector();
		// Hashtable reporthash = new Hashtable();
		try {
			Hashtable allreporthash = new Hashtable();
			if (ids != null && ids.length > 0) {
				for (int i = 0; i < ids.length; i++) {
					Hashtable reporthash = new Hashtable();
					HostNodeDao dao = new HostNodeDao();
					HostNode node = (HostNode) dao.loadHost(ids[i]);
					dao.close();
					if (node == null)
						continue;
					EventListDao eventdao = new EventListDao();
					// �õ��¼��б�
					StringBuffer s = new StringBuffer();
					s.append("select * from system_eventlist where recordtime>= '" + starttime + "' " + "and recordtime<='" + totime + "' ");
					s.append(" and nodeid=" + node.getId());

					List infolist = eventdao.findByCriteria(s.toString());
					int levelone = 0;
					int levletwo = 0;
					int levelthree = 0;
					if (infolist != null && infolist.size() > 0) {
						for (int j = 0; j < infolist.size(); j++) {
							EventList eventlist = (EventList) infolist.get(j);
							if (eventlist.getContent() == null)
								eventlist.setContent("");
							String content = eventlist.getContent();
							String subentity = eventlist.getSubentity();

							if (eventlist.getContent() == null)
								eventlist.setContent("");

							if (eventlist.getLevel1() != 1) {
								levelone = levelone + 1;
							} else if (eventlist.getLevel1() == 2) {
								levletwo = levletwo + 1;
							} else if (eventlist.getLevel1() == 3) {
								levelthree = levelthree + 1;
							}
						}
					}
					reporthash.put("levelone", levelone + "");
					reporthash.put("levletwo", levletwo + "");
					reporthash.put("levelthree", levelthree + "");
					// ------------------------�¼�end
					ip = node.getIpAddress();
					equipname = node.getAlias();
					String remoteip = request.getRemoteAddr();
					String newip = doip(ip);
					// �������־ȡ���˿����¼�¼���б�
					String orderflag = "index";

					String[] netInterfaceItem = { "index", "ifname", "ifSpeed", "ifOperStatus", "OutBandwidthUtilHdx", "InBandwidthUtilHdx" };

					vector = hostlastmanager.getInterface_share(ip, netInterfaceItem, orderflag, startdate, todate);
					PortconfigDao portdao = new PortconfigDao();
					Hashtable portconfigHash = portdao.getIpsHash(ip);
					// Hashtable portconfigHash =
					// portconfigManager.getIpsHash(ip);
					reporthash.put("portconfigHash", portconfigHash);

					List reportports = portdao.getByIpAndReportflag(ip, new Integer(1));
					reporthash.put("reportports", reportports);
					if (reportports != null && reportports.size() > 0) {
						// ��ʾ�˿ڵ�����ͼ��
						I_HostCollectDataDay daymanager = new HostCollectDataDayManager();
						String unit = "kb/s";
						String title = startdate + "��" + todate + "�˿�����";
						String[] banden3 = { "InBandwidthUtilHdx", "OutBandwidthUtilHdx" };
						String[] bandch3 = { "�������", "��������" };

						for (int k = 0; k < reportports.size(); k++) {
							com.afunms.config.model.Portconfig portconfig = (com.afunms.config.model.Portconfig) reportports.get(k);
							// ��������ʾ����
							Hashtable value = new Hashtable();
							value = daymanager.getmultiHisHdx(ip, "ifspeed", portconfig.getPortindex() + "", banden3, bandch3, startdate, todate, "UtilHdx");
							String reportname = "��" + portconfig.getPortindex() + "(" + portconfig.getName() + ")�˿�����" + startdate + "��" + todate + "����(��������ʾ)";
							p_drawchartMultiLineMonth(value, reportname, newip + portconfig.getPortindex() + "ifspeed_day", 800, 200, "UtilHdx");
							// String url1 =
							// "/resource/image/jfreechart/"+newip+portconfig.getPortindex()+"ifspeed_day.png";
						}
					}

					reporthash.put("netifVector", vector);

					Hashtable cpuhash = hostmanager.getCategory(ip, "CPU", "Utilization", starttime, totime);
					String pingconavg = "";

					maxhash = new Hashtable();
					String cpumax = "";
					String avgcpu = "";
					if (cpuhash.get("max") != null) {
						cpumax = (String) cpuhash.get("max");
					}
					if (cpuhash.get("avgcpucon") != null) {
						avgcpu = (String) cpuhash.get("avgcpucon");
					}

					maxhash.put("cpumax", cpumax);
					maxhash.put("avgcpu", avgcpu);
					// ��ͼ
					p_draw_line(cpuhash, "", newip + "cpu", 740, 120);
					Hashtable ConnectUtilizationhash = hostmanager.getCategory(ip, "Ping", "ConnectUtilization", starttime, totime);
					if (ConnectUtilizationhash.get("avgpingcon") != null)
						pingconavg = (String) ConnectUtilizationhash.get("avgpingcon");
					String ConnectUtilizationmax = "";
					maxping.put("avgpingcon", pingconavg);
					if (ConnectUtilizationhash.get("max") != null) {
						ConnectUtilizationmax = (String) ConnectUtilizationhash.get("max");
					}
					maxping.put("pingmax", ConnectUtilizationmax);

					p_draw_line(ConnectUtilizationhash, "", newip + "ConnectUtilization", 740, 120);

					// ���ڴ��л�õ�ǰ�ĸ���IP��ص�IP-MAC��FDB����Ϣ
					Hashtable _IpRouterHash = ShareData.getIprouterdata();
					vector = (Vector) _IpRouterHash.get(ip);
					if (vector != null)
						reporthash.put("iprouterVector", vector);

					Vector pdata = (Vector) pingdata.get(ip);
					// ��ping�õ������ݼӽ�ȥ
					if (pdata != null && pdata.size() > 0) {
						for (int m = 0; m < pdata.size(); m++) {
							Pingcollectdata hostdata = (Pingcollectdata) pdata.get(m);
							if (hostdata.getSubentity().equals("ConnectUtilization")) {
								reporthash.put("time", hostdata.getCollecttime());
								reporthash.put("Ping", hostdata.getThevalue());
								reporthash.put("ping", maxping);
							}
						}
					}

					// CPU
					Hashtable hdata = (Hashtable) sharedata.get(ip);
					if (hdata == null)
						hdata = new Hashtable();
					Vector cpuVector = new Vector();
					if (hdata.get("cpu") != null)
						cpuVector = (Vector) hdata.get("cpu");
					if (cpuVector != null && cpuVector.size() > 0) {
						// for(int si=0;si<cpuVector.size();si++){
						CPUcollectdata cpudata = (CPUcollectdata) cpuVector.elementAt(0);
						maxhash.put("cpu", cpudata.getThevalue());
						reporthash.put("CPU", maxhash);
						// }
					} else {
						reporthash.put("CPU", maxhash);
					}// -----����
					Hashtable streaminHash = hostmanager.getAllutilhdx(ip, "AllInBandwidthUtilHdx", starttime, totime, "avg");
					Hashtable streamoutHash = hostmanager.getAllutilhdx(ip, "AllOutBandwidthUtilHdx", starttime, totime, "avg");
					String avgput = "";
					if (streaminHash.get("avgput") != null) {
						avgput = (String) streaminHash.get("avgput");
						reporthash.put("avginput", avgput);
					}
					if (streamoutHash.get("avgput") != null) {
						avgput = (String) streamoutHash.get("avgput");
						reporthash.put("avgoutput", avgput);
					}
					Hashtable streammaxinHash = hostmanager.getAllutilhdx(ip, "AllInBandwidthUtilHdx", starttime, totime, "max");
					Hashtable streammaxoutHash = hostmanager.getAllutilhdx(ip, "AllOutBandwidthUtilHdx", starttime, totime, "max");
					String maxput = "";
					if (streammaxinHash.get("max") != null) {
						maxput = (String) streammaxinHash.get("max");
						reporthash.put("maxinput", maxput);
					}
					if (streammaxoutHash.get("max") != null) {
						maxput = (String) streammaxoutHash.get("max");
						reporthash.put("maxoutput", maxput);
					}
					// ------����end

					reporthash.put("starttime", starttime);
					reporthash.put("totime", totime);

					reporthash.put("equipname", equipname);
					reporthash.put("memmaxhash", memmaxhash);
					reporthash.put("memavghash", memavghash);

					allreporthash.put(ip, reporthash);
				}
			}
			String file = "temp/networknms_report.doc";// ���浽��Ŀ�ļ����µ�ָ���ļ���
			String fileName = ResourceCenter.getInstance().getSysPath() + file;// ��ȡϵͳ�ļ���·��
			ExcelReport1 report = new ExcelReport1(new IpResourceReport(), allreporthash);
			Hashtable dataHash = new Hashtable();
			dataHash.put("businessAnalytics", businessAnalytics);// ���з���
			dataHash.put("runAppraise", runAppraise);// ��������
			report.setDataHash(dataHash);
			report.createReport_networkchoce(starttime, totime, fileName, username, positionname);
			request.setAttribute("filename", fileName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/capreport/net/download.jsp";
		// return mapping.findForward("report_info");
	}

	// zhushouzhi-------------------------------end

	// ===============================����oa��Ҫ��������

	/**
	 * 
	 * ���ô˷��������ɽ���ƶ�oa ͳ�������豸�ӿ����ٱ��� �����Ѿ�д�þͲ����ɱ���ı�񷽷�
	 * 
	 */
	public String downloadsxoaIfreport() {
		String oids = getParaValue("ids");// ��ȡҪ����id
		if (oids == null)
			oids = "";
		Integer[] ids = null;
		if (oids.split(",").length > 0) {
			String[] _ids = oids.split(",");
			if (_ids != null && _ids.length > 0)
				ids = new Integer[_ids.length];
			for (int i = 0; i < _ids.length; i++) {
				ids[i] = new Integer(_ids[i]);
			}
		}
		Date d = new Date();
		String startdate = getParaValue("startdate");
		if (startdate == null) {
			startdate = sdf0.format(d);
		}
		String todate = getParaValue("todate");
		if (todate == null) {
			todate = sdf0.format(d);
		}
		String starttime = startdate + " 00:00:00";
		String totime = todate + " 23:59:59";
		Hashtable allcpuhash = new Hashtable();
		String ip = "";
		String equipname = "";

		Hashtable allreporthash = new Hashtable();
		Vector vector = new Vector();

		try {

			if (ids != null && ids.length > 0) {
				for (int i = 0; i < ids.length; i++) {
					Hashtable reporthash = new Hashtable();
					HostNodeDao dao = new HostNodeDao();
					HostNode node = (HostNode) dao.loadHost(ids[i]);
					dao.close();
					// Monitoriplist monitor = monitorManager.getById(ids[i]);
					// Equipment equipment = (Equipment)hostMonitor.get(i);
					ip = node.getIpAddress();
					equipname = node.getAlias();
					String remoteip = request.getRemoteAddr();
					String newip = doip(ip);
					// �������־ȡ���˿����¼�¼���б�
					String orderflag = "index";

					// ��ȡ�˿��б�
					PortconfigDao portdao = new PortconfigDao();
					// Hashtable portconfigHash = portdao.getIpsHash(ip);

					// ��Ҫ��������б�
					List reportports = portdao.getByIpAndReportflag(ip, new Integer(1));

					if (reportports != null && reportports.size() > 0) {
						// ��ʾ�˿ڵ�����ͼ��
						I_HostCollectDataDay daymanager = new HostCollectDataDayManager();

						for (int k = 0; k < reportports.size(); k++) {
							com.afunms.config.model.Portconfig portconfig = (com.afunms.config.model.Portconfig) reportports.get(k);

							Hashtable value = new Hashtable();
							value = ((HostCollectDataDayManager) daymanager).getmultiHisHdx_OA(ip, portconfig.getPortindex() + "", startdate, todate, "UtilHdx", portconfig.getName(), portconfig
									.getLinkuse());
							vector.add(value);
						}
					}

				}
			}

			// reporthash.put("netifVector",vector);

			allreporthash.put("sxoanetifreport", vector);
			// ����ӿ���Ҫ��һ��

			// -----------------��ҪЭ���޸�---------------------
			AbstractionReport1 report = new ExcelReport1(new IpResourceReport(), allreporthash);
			// -----------------------------------------------

			report.createReport_oawork("/temp/networknms_report.xls", startdate, todate);
			// report.createReport_networkall("/temp/networknms_report.xls");
			request.setAttribute("filename", report.getFileName());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/capreport/net/download.jsp";
		// return mapping.findForward("report_info");
	}

	private String showNetworkPingReport() {
		Date d = new Date();
		String startdate = getParaValue("startdate");
		if (startdate == null) {
			startdate = sdf0.format(d);
		}
		String todate = getParaValue("todate");
		if (todate == null) {
			todate = sdf0.format(d);
		}
		String ipaddress = getParaValue("ipaddress");
		String newip = doip(ipaddress);
		request.setAttribute("ipaddress", ipaddress);
		request.setAttribute("newip", newip);
		request.setAttribute("startdate", startdate);
		request.setAttribute("todate", todate);		
		
		String starttime = startdate + " 00:00:00";
		String totime = todate + " 23:59:59";

		String[] ids = getParaArrayValue("checkbox");
		Hashtable allcpuhash = new Hashtable();

		// �������־ȡ���˿����¼�¼���б�
		String orderflag = "ipaddress";
		if (getParaValue("orderflag") != null && !getParaValue("orderflag").equals("")) {
			orderflag = getParaValue("orderflag");
		}

		List orderList = new ArrayList();
		HostNodeDao dao = new HostNodeDao();
		List selectedIPNodeList = dao.findBynode("ip_address", ipaddress);
		if (selectedIPNodeList != null && selectedIPNodeList.size() > 0) {
			HostNode node = (HostNode) selectedIPNodeList.get(0);			
			
			request.setAttribute("nodeid", node.getId());
			
			Hashtable pinghash = new Hashtable();
			try {
				pinghash = hostmanager.getCategory(node.getIpAddress(), "Ping", "ConnectUtilization", starttime, totime);
			} catch (Exception e) {
				e.printStackTrace();
			}
			Hashtable responsehash = new Hashtable();
			try {
				responsehash = hostmanager.getCategory(node.getIpAddress(), "Ping", "ResponseTime", starttime, totime);
			} catch (Exception e) {
				e.printStackTrace();
			}
			Hashtable ipmemhash = new Hashtable();
			ipmemhash.put("hostnode", node);
			ipmemhash.put("pinghash", pinghash);
			ipmemhash.put("responsehash", responsehash);
			orderList.add(ipmemhash);

		}
		List returnList = new ArrayList();
		if (orderflag.equalsIgnoreCase("avgping") || orderflag.equalsIgnoreCase("downnum") || orderflag.equalsIgnoreCase("responseavg") || orderflag.equalsIgnoreCase("responsemax")) {
			returnList = (List) session.getAttribute("pinglist");
		} else {
			// ��orderList����theValue��������

			// **********************************************************
			List pinglist = orderList;
			if (pinglist != null && pinglist.size() > 0) {
				for (int i = 0; i < pinglist.size(); i++) {
					Hashtable _pinghash = (Hashtable) pinglist.get(i);
					HostNode node = (HostNode) _pinghash.get("hostnode");
					Hashtable pinghash = (Hashtable) _pinghash.get("pinghash");
					Hashtable responsehash = (Hashtable) _pinghash.get("responsehash");
					if (pinghash == null)
						continue;
					String equname = node.getAlias();
					String ip = node.getIpAddress();

					String pingconavg = "";
					String downnum = "";
					String responseavg = "";
					String responsemax = "";
					if (pinghash.get("avgpingcon") != null)
						pingconavg = (String) pinghash.get("avgpingcon");
					if (pinghash.get("downnum") != null)
						downnum = (String) pinghash.get("downnum");
					// ��ȡ��Ӧʱ��
					if (responsehash.get("avgpingcon") != null)
						responseavg = (String) responsehash.get("avgpingcon");
					if (responsehash.get("pingmax") != null)
						responsemax = (String) responsehash.get("pingmax");

					List ipdiskList = new ArrayList();
					ipdiskList.add(ip);
					ipdiskList.add(equname);
					ipdiskList.add(node.getType());
					ipdiskList.add(pingconavg);
					ipdiskList.add(downnum);
					ipdiskList.add(responseavg);
					ipdiskList.add(responsemax);
					returnList.add(ipdiskList);
				}
			}
		}
		// **********************************************************

		List list = new ArrayList();
		if (returnList != null && returnList.size() > 0) {
			for (int m = 0; m < returnList.size(); m++) {
				List ipdiskList = (List) returnList.get(m);
				for (int n = m + 1; n < returnList.size(); n++) {
					List _ipdiskList = (List) returnList.get(n);
					if (orderflag.equalsIgnoreCase("ipaddress")) {
					} else if (orderflag.equalsIgnoreCase("avgping")) {
						String avgping = "";
						if (ipdiskList.get(3) != null) {
							avgping = (String) ipdiskList.get(3);
						}
						String _avgping = "";
						if (ipdiskList.get(3) != null) {
							_avgping = (String) _ipdiskList.get(3);
						}
						if (new Double(avgping.substring(0, avgping.length() - 2)).doubleValue() < new Double(_avgping.substring(0, _avgping.length() - 2)).doubleValue()) {
							returnList.remove(m);
							returnList.add(m, _ipdiskList);
							returnList.remove(n);
							returnList.add(n, ipdiskList);
							ipdiskList = _ipdiskList;
							_ipdiskList = null;
						}
					} else if (orderflag.equalsIgnoreCase("downnum")) {
						String downnum = "";
						if (ipdiskList.get(4) != null) {
							downnum = (String) ipdiskList.get(4);
						}
						String _downnum = "";
						if (ipdiskList.get(4) != null) {
							_downnum = (String) _ipdiskList.get(4);
						}
						if (new Double(downnum).doubleValue() < new Double(_downnum).doubleValue()) {
							returnList.remove(m);
							returnList.add(m, _ipdiskList);
							returnList.remove(n);
							returnList.add(n, ipdiskList);
							ipdiskList = _ipdiskList;
							_ipdiskList = null;
						}
					} else if (orderflag.equalsIgnoreCase("responseavg")) {
						String avgping = "";
						if (ipdiskList.get(5) != null) {
							avgping = (String) ipdiskList.get(5);
						}
						String _avgping = "";
						if (ipdiskList.get(5) != null) {
							_avgping = (String) _ipdiskList.get(5);
						}
						if (new Double(avgping.substring(0, avgping.length() - 2)).doubleValue() < new Double(_avgping.substring(0, _avgping.length() - 2)).doubleValue()) {
							returnList.remove(m);
							returnList.add(m, _ipdiskList);
							returnList.remove(n);
							returnList.add(n, ipdiskList);
							ipdiskList = _ipdiskList;
							_ipdiskList = null;
						}
					} else if (orderflag.equalsIgnoreCase("responsemax")) {
						String downnum = "";
						if (ipdiskList.get(6) != null) {
							downnum = (String) ipdiskList.get(6);
						}
						String _downnum = "";
						if (ipdiskList.get(6) != null) {
							_downnum = (String) _ipdiskList.get(6);
						}
						if (new Double(downnum).doubleValue() < new Double(_downnum).doubleValue()) {
							returnList.remove(m);
							returnList.add(m, _ipdiskList);
							returnList.remove(n);
							returnList.add(n, ipdiskList);
							ipdiskList = _ipdiskList;
							_ipdiskList = null;
						}
					}
				}
				// �õ�������Subentity���б�
				list.add(ipdiskList);
				ipdiskList = null;
			}
		}
		String pingconavg = "";
		Hashtable ConnectUtilizationhash = new Hashtable();
		try {
			ConnectUtilizationhash = hostmanager.getCategory(ipaddress, "Ping", "ConnectUtilization", starttime, totime);
		} catch (Exception e) {

		}
		if (ConnectUtilizationhash.get("avgpingcon") != null)
			pingconavg = (String) ConnectUtilizationhash.get("avgpingcon");
		String ConnectUtilizationmax = "";
		// maxping.put("avgpingcon", pingconavg);
		if (ConnectUtilizationhash.get("max") != null) {
			ConnectUtilizationmax = (String) ConnectUtilizationhash.get("max");
		}
		// maxping.put("pingmax", ConnectUtilizationmax);

		// Hashtable ConnectUtilizationhash = new Hashtable();
		// try{
		// ConnectUtilizationhash =
		// hostmanager.getCategory(host.getIpAddress(),"Ping","ConnectUtilization",getStartTime(),getToTime());
		// }catch(Exception ex){
		// ex.printStackTrace();
		// }

		Hashtable ResponseTimehash = new Hashtable();
		try {
			ResponseTimehash = hostmanager.getCategory(ipaddress, "Ping", "ResponseTime", starttime, totime);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		String[] bandch = { "��ͨ��", "��Ӧʱ��" };
		String[] bandch1 = { "��ͨ��(%)", "��Ӧʱ��(ms)" };		
		
		
		String returnStr = area_chooseDrawLineType("minute", ConnectUtilizationhash, ResponseTimehash, bandch, bandch1, 350, 200, 110, 20);
		SysLogger.info("returnStr######## " + returnStr + " ############");		
		JFreeChartBrother jfb = (JFreeChartBrother) ResourceCenter.getInstance().getChartStorage().get(returnStr);				
		
		String imgpath = ResourceCenter.getInstance().getSysPath() + "/resource/image/jfreechart/";
		FileOutputStream fos = null;
		try {			
			fos = new FileOutputStream(imgpath + returnStr + ".png");// ��ͳ��ͼ�������JPG�ļ�
			ChartUtilities.writeChartAsJPEG(fos, // ������ĸ������
					1,// JPEGͼƬ��������0~1֮��
					jfb.getChart(), // ͳ��ͼ�����
					740, // ��
					200,// ��
					null// ChartRenderingInfo ��Ϣ
					);
			fos.close();			
		} catch (Exception e) {
			//e.printStackTrace();
		}

		p_draw_line(ConnectUtilizationhash, "", newip + "ConnectUtilization", 740, 120);
		
		session.setAttribute("pingpath", imgpath + returnStr + ".png");
		
		request.setAttribute("starttime", starttime);
		request.setAttribute("totime", totime);
		session.setAttribute("pinglist", list);
		request.setAttribute("startdate", startdate);
		request.setAttribute("todate", todate);
		request.setAttribute("pinglist", list);
		request.setAttribute("imgpath", returnStr + ".png");
		return "/capreport/net/showNetworkPingReport.jsp";
	}
	/*
	 * HP�洢��ͨ�ʱ���
	 */
	private String showHPstorPingReport() {
		Date d = new Date();
		String startdate = getParaValue("startdate");
		if (startdate == null) {
			startdate = sdf0.format(d);
		}
		String todate = getParaValue("todate");
		if (todate == null) {
			todate = sdf0.format(d);
		}
		String ipaddress = getParaValue("ipaddress");
		String newip = doip(ipaddress);
		request.setAttribute("ipaddress", ipaddress);
		request.setAttribute("newip", newip);
		request.setAttribute("startdate", startdate);
		request.setAttribute("todate", todate);		
		
		String starttime = startdate + " 00:00:00";
		String totime = todate + " 23:59:59";

		String[] ids = getParaArrayValue("checkbox");
		Hashtable allcpuhash = new Hashtable();

		// �������־ȡ���˿����¼�¼���б�
		String orderflag = "ipaddress";
		if (getParaValue("orderflag") != null && !getParaValue("orderflag").equals("")) {
			orderflag = getParaValue("orderflag");
		}

		List orderList = new ArrayList();
		HostNodeDao dao = new HostNodeDao();
		List selectedIPNodeList = dao.findBynode("ip_address", ipaddress);
		if (selectedIPNodeList != null && selectedIPNodeList.size() > 0) {
			HostNode node = (HostNode) selectedIPNodeList.get(0);			
			
			request.setAttribute("nodeid", node.getId());
			
			Hashtable pinghash = new Hashtable();
			try {
				pinghash = hostmanager.getCategory(node.getIpAddress(), "Ping", "ConnectUtilization", starttime, totime);
			} catch (Exception e) {
				e.printStackTrace();
			}
			Hashtable responsehash = new Hashtable();
			try {
				responsehash = hostmanager.getCategory(node.getIpAddress(), "Ping", "ResponseTime", starttime, totime);
			} catch (Exception e) {
				e.printStackTrace();
			}
			Hashtable ipmemhash = new Hashtable();
			ipmemhash.put("hostnode", node);
			ipmemhash.put("pinghash", pinghash);
			ipmemhash.put("responsehash", responsehash);
			orderList.add(ipmemhash);

		}
		List returnList = new ArrayList();
		if (orderflag.equalsIgnoreCase("avgping") || orderflag.equalsIgnoreCase("downnum") || orderflag.equalsIgnoreCase("responseavg") || orderflag.equalsIgnoreCase("responsemax")) {
			returnList = (List) session.getAttribute("pinglist");
		} else {
			// ��orderList����theValue��������

			// **********************************************************
			List pinglist = orderList;
			if (pinglist != null && pinglist.size() > 0) {
				for (int i = 0; i < pinglist.size(); i++) {
					Hashtable _pinghash = (Hashtable) pinglist.get(i);
					HostNode node = (HostNode) _pinghash.get("hostnode");
					Hashtable pinghash = (Hashtable) _pinghash.get("pinghash");
					Hashtable responsehash = (Hashtable) _pinghash.get("responsehash");
					if (pinghash == null)
						continue;
					String equname = node.getAlias();
					String ip = node.getIpAddress();

					String pingconavg = "";
					String downnum = "";
					String responseavg = "";
					String responsemax = "";
					if (pinghash.get("avgpingcon") != null)
						pingconavg = (String) pinghash.get("avgpingcon");
					if (pinghash.get("downnum") != null)
						downnum = (String) pinghash.get("downnum");
					// ��ȡ��Ӧʱ��
					if (responsehash.get("avgpingcon") != null)
						responseavg = (String) responsehash.get("avgpingcon");
					if (responsehash.get("pingmax") != null)
						responsemax = (String) responsehash.get("pingmax");

					List ipdiskList = new ArrayList();
					ipdiskList.add(ip);
					ipdiskList.add(equname);
					ipdiskList.add(node.getType());
					ipdiskList.add(pingconavg);
					ipdiskList.add(downnum);
					ipdiskList.add(responseavg);
					ipdiskList.add(responsemax);
					returnList.add(ipdiskList);
				}
			}
		}
		// **********************************************************

		List list = new ArrayList();
		if (returnList != null && returnList.size() > 0) {
			for (int m = 0; m < returnList.size(); m++) {
				List ipdiskList = (List) returnList.get(m);
				for (int n = m + 1; n < returnList.size(); n++) {
					List _ipdiskList = (List) returnList.get(n);
					if (orderflag.equalsIgnoreCase("ipaddress")) {
					} else if (orderflag.equalsIgnoreCase("avgping")) {
						String avgping = "";
						if (ipdiskList.get(3) != null) {
							avgping = (String) ipdiskList.get(3);
						}
						String _avgping = "";
						if (ipdiskList.get(3) != null) {
							_avgping = (String) _ipdiskList.get(3);
						}
						if (new Double(avgping.substring(0, avgping.length() - 2)).doubleValue() < new Double(_avgping.substring(0, _avgping.length() - 2)).doubleValue()) {
							returnList.remove(m);
							returnList.add(m, _ipdiskList);
							returnList.remove(n);
							returnList.add(n, ipdiskList);
							ipdiskList = _ipdiskList;
							_ipdiskList = null;
						}
					} else if (orderflag.equalsIgnoreCase("downnum")) {
						String downnum = "";
						if (ipdiskList.get(4) != null) {
							downnum = (String) ipdiskList.get(4);
						}
						String _downnum = "";
						if (ipdiskList.get(4) != null) {
							_downnum = (String) _ipdiskList.get(4);
						}
						if (new Double(downnum).doubleValue() < new Double(_downnum).doubleValue()) {
							returnList.remove(m);
							returnList.add(m, _ipdiskList);
							returnList.remove(n);
							returnList.add(n, ipdiskList);
							ipdiskList = _ipdiskList;
							_ipdiskList = null;
						}
					} else if (orderflag.equalsIgnoreCase("responseavg")) {
						String avgping = "";
						if (ipdiskList.get(5) != null) {
							avgping = (String) ipdiskList.get(5);
						}
						String _avgping = "";
						if (ipdiskList.get(5) != null) {
							_avgping = (String) _ipdiskList.get(5);
						}
						if (new Double(avgping.substring(0, avgping.length() - 2)).doubleValue() < new Double(_avgping.substring(0, _avgping.length() - 2)).doubleValue()) {
							returnList.remove(m);
							returnList.add(m, _ipdiskList);
							returnList.remove(n);
							returnList.add(n, ipdiskList);
							ipdiskList = _ipdiskList;
							_ipdiskList = null;
						}
					} else if (orderflag.equalsIgnoreCase("responsemax")) {
						String downnum = "";
						if (ipdiskList.get(6) != null) {
							downnum = (String) ipdiskList.get(6);
						}
						String _downnum = "";
						if (ipdiskList.get(6) != null) {
							_downnum = (String) _ipdiskList.get(6);
						}
						if (new Double(downnum).doubleValue() < new Double(_downnum).doubleValue()) {
							returnList.remove(m);
							returnList.add(m, _ipdiskList);
							returnList.remove(n);
							returnList.add(n, ipdiskList);
							ipdiskList = _ipdiskList;
							_ipdiskList = null;
						}
					}
				}
				// �õ�������Subentity���б�
				list.add(ipdiskList);
				ipdiskList = null;
			}
		}
		String pingconavg = "";
		Hashtable ConnectUtilizationhash = new Hashtable();
		try {
			ConnectUtilizationhash = hostmanager.getCategory(ipaddress, "Ping", "ConnectUtilization", starttime, totime);
		} catch (Exception e) {

		}
		if (ConnectUtilizationhash.get("avgpingcon") != null)
			pingconavg = (String) ConnectUtilizationhash.get("avgpingcon");
		String ConnectUtilizationmax = "";
		// maxping.put("avgpingcon", pingconavg);
		if (ConnectUtilizationhash.get("max") != null) {
			ConnectUtilizationmax = (String) ConnectUtilizationhash.get("max");
		}
		// maxping.put("pingmax", ConnectUtilizationmax);

		// Hashtable ConnectUtilizationhash = new Hashtable();
		// try{
		// ConnectUtilizationhash =
		// hostmanager.getCategory(host.getIpAddress(),"Ping","ConnectUtilization",getStartTime(),getToTime());
		// }catch(Exception ex){
		// ex.printStackTrace();
		// }

		Hashtable ResponseTimehash = new Hashtable();
		try {
			ResponseTimehash = hostmanager.getCategory(ipaddress, "Ping", "ResponseTime", starttime, totime);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		String[] bandch = { "��ͨ��", "��Ӧʱ��" };
		String[] bandch1 = { "��ͨ��(%)", "��Ӧʱ��(ms)" };		
		
		
		String returnStr = area_chooseDrawLineType("minute", ConnectUtilizationhash, ResponseTimehash, bandch, bandch1, 350, 200, 110, 20);
		SysLogger.info("returnStr######## " + returnStr + " ############");		
		JFreeChartBrother jfb = (JFreeChartBrother) ResourceCenter.getInstance().getChartStorage().get(returnStr);				
		
		String imgpath = ResourceCenter.getInstance().getSysPath() + "/resource/image/jfreechart/";
		FileOutputStream fos = null;
		try {			
			fos = new FileOutputStream(imgpath + returnStr + ".png");// ��ͳ��ͼ�������JPG�ļ�
			ChartUtilities.writeChartAsJPEG(fos, // ������ĸ������
					1,// JPEGͼƬ��������0~1֮��
					jfb.getChart(), // ͳ��ͼ�����
					740, // ��
					200,// ��
					null// ChartRenderingInfo ��Ϣ
					);
			fos.close();			
		} catch (Exception e) {
			//e.printStackTrace();
		}

		p_draw_line(ConnectUtilizationhash, "", newip + "ConnectUtilization", 740, 120);
		
		session.setAttribute("pingpath", imgpath + returnStr + ".png");
		
		request.setAttribute("starttime", starttime);
		request.setAttribute("totime", totime);
		session.setAttribute("pinglist", list);
		request.setAttribute("startdate", startdate);
		request.setAttribute("todate", todate);
		request.setAttribute("pinglist", list);
		request.setAttribute("imgpath", returnStr + ".png");
		return "/application/hpstorage/showPingReport.jsp";
	}
	/*
	 * HP�洢���̱���
	 * �����������󻹲��Ǻ���ȷ�����ԣ�����ı���ֻչʾ���²ɼ�����HP�洢������Ϣ
	 */
	private String showHPstorDiskReport()
	{
		Date d = new Date();
		String startdate = getParaValue("startdate");
		if (startdate == null) {
			startdate = sdf0.format(d);
		}
		String todate = getParaValue("todate");
		if (todate == null) {
			todate = sdf0.format(d);
		}
		String ipaddress = getParaValue("ipaddress");
		//String newip = doip(ipaddress);
		request.setAttribute("ipaddress", ipaddress);
		//request.setAttribute("newip", newip);
		request.setAttribute("startdate", startdate);
		request.setAttribute("todate", todate);
		String nodeid = getParaValue("id");
		request.setAttribute("nodeid", Integer.parseInt(nodeid));
		return "/application/hpstorage/showDiskReport.jsp";
	}
	/*
	 * HP�洢�ۺϱ���
	 * �����������󻹲��Ǻ���ȷ�����ԣ�����ı���ֻչʾ���²ɼ�����HP�洢�ۺ���Ϣ
	 * 
	 */
	private String showHPstorCompositeReport()
	{
		Date d = new Date();
		String startdate = getParaValue("startdate");
		if (startdate == null) {
			startdate = sdf0.format(d);
		}
		String todate = getParaValue("todate");
		if (todate == null) {
			todate = sdf0.format(d);
		}
		String ipaddress = getParaValue("ipaddress");
		//String newip = doip(ipaddress);
		request.setAttribute("ipaddress", ipaddress);
		//request.setAttribute("newip", newip);
		request.setAttribute("startdate", startdate);
		request.setAttribute("todate", todate);
		String nodeid = getParaValue("id");
		request.setAttribute("nodeid", Integer.parseInt(nodeid));
		return "/application/hpstorage/showCompositeReport.jsp";
	}
	/*
	 * ����HP�洢���̱���
	 * 
	 */
	private String downloadHPStorageDiskReport() {
		String str = request.getParameter("str");
		String ip = request.getParameter("ipaddress");
		if (str.equals("1")) {
			String file = "/temp/" + ip + "hpstoragediskreport.doc";// ���浽��Ŀ�ļ����µ�ָ���ļ���
			String fileName = ResourceCenter.getInstance().getSysPath() + file;// ��ȡϵͳ�ļ���·��
			try {
				//createDocContext(fileName);
				creatHPstorageDiskDoc(fileName,ip);
			} catch (DocumentException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			request.setAttribute("filename", fileName);
		}
		if (str.equals("0")) {
			Date d = new Date();
			String startdate = getParaValue("startdate");
			if (startdate == null) {
				startdate = sdf0.format(d);
			}
			String todate = getParaValue("todate");
			if (todate == null) {
				todate = sdf0.format(d);
			}
			String starttime = startdate + " 00:00:00";
			String totime = todate + " 23:59:59";			
			
			List<Disk> disks = null;
			Hashtable ipAllData = new Hashtable();
			//ֻȡ��ǰ�ɼ��Ĵ�����Ϣ
			ipAllData = (Hashtable) ShareData.getSharedata().get("hpstorage:" + ip);
			if(ipAllData != null){
			    disks = (List<Disk>) ipAllData.get("disks");
			}
			
			Hashtable reporthash = new Hashtable();			
			
			reporthash.put("disks", disks);
			reporthash.put("starttime", starttime);
			reporthash.put("totime", totime);		

			ExcelReport1 report = new ExcelReport1(new IpResourceReport(), reporthash);
			report.setRequest(request);
			report.createReport_HPstorageDisk("/temp/" + ip + "hpstoragediskreport.xls",ip);
			request.setAttribute("filename", report.getFileName());
		}
		if (str.equals("4")) {
			String file = "/temp/" + ip + "hpstoragediskreport.PDF";// ���浽��Ŀ�ļ����µ�ָ���ļ���
			String fileName = ResourceCenter.getInstance().getSysPath() + file;// ��ȡϵͳ�ļ���·��
			try {
				//createPdfContextPing(fileName);
				createHPstorageDiskPdf(fileName,ip);
			} catch (DocumentException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			request.setAttribute("filename", fileName);
		}

		return "/capreport/net/download.jsp";
	}
	/*
	 * ����HP�洢�ۺϱ���
	 * 
	 */
	private String downloadHPStorageCompReport() {
		String str = request.getParameter("str");
		String ip = request.getParameter("ipaddress");
		if (str.equals("1")) {
			String file = "/temp/" + ip + "hpstoragecompreport.doc";// ���浽��Ŀ�ļ����µ�ָ���ļ���
			String fileName = ResourceCenter.getInstance().getSysPath() + file;// ��ȡϵͳ�ļ���·��
			try {
				//createDocContext(fileName);
				creatHPstorageCompDoc(fileName,ip);
			} catch (DocumentException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			request.setAttribute("filename", fileName);
		}
		if (str.equals("0")) {
			Date d = new Date();
			String startdate = getParaValue("startdate");
			if (startdate == null) {
				startdate = sdf0.format(d);
			}
			String todate = getParaValue("todate");
			if (todate == null) {
				todate = sdf0.format(d);
			}
			String starttime = startdate + " 00:00:00";
			String totime = todate + " 23:59:59";			
			
			List<Disk> disks = null;
			ArrayInfo arrayinfo = null;
			List<Enclosure> enclosures = null;
			List<Controller> controllers = null;
			List<Port> ports = null;
			List<Lun> luns = null;
			List<VFP> vfps = null;
			SubSystemInfo subSystemInfo = null;
			Hashtable ipAllData = new Hashtable();
			//ֻȡ��ǰ�ɼ����ۺ���Ϣ
			ipAllData = (Hashtable) ShareData.getSharedata().get("hpstorage:" + ip);
			if(ipAllData != null){
			    disks = (List<Disk>) ipAllData.get("disks");
			    arrayinfo = (ArrayInfo)ipAllData.get("arrayinfo");
			    enclosures = (List<Enclosure>)ipAllData.get("enclosures");
			    controllers = (List<Controller>)ipAllData.get("controllers");
			    ports = (List<Port>)ipAllData.get("ports");
			    luns = (List<Lun>)ipAllData.get("luns");
			    vfps = (List<VFP>)ipAllData.get("vfps");
			    subSystemInfo = (SubSystemInfo)ipAllData.get("subSystemInfo");
			}
			
			Hashtable reporthash = new Hashtable();			
			
			reporthash.put("disks", disks);
			reporthash.put("arrayinfo", arrayinfo);
			reporthash.put("enclosures", enclosures);
			reporthash.put("controllers", controllers);
			reporthash.put("ports", ports);
			reporthash.put("luns", luns);
			reporthash.put("vfps", vfps);
			reporthash.put("subSystemInfo", subSystemInfo);			
			reporthash.put("starttime", starttime);
			reporthash.put("totime", totime);		

			ExcelReport1 report = new ExcelReport1(new IpResourceReport(), reporthash);
			report.setRequest(request);
			report.createReport_HPstorageComp("/temp/" + ip + "hpstoragecompreport.xls",ip);
			request.setAttribute("filename", report.getFileName());
		}
		if (str.equals("4")) {
			String file = "/temp/" + ip + "hpstoragecompreport.PDF";// ���浽��Ŀ�ļ����µ�ָ���ļ���
			String fileName = ResourceCenter.getInstance().getSysPath() + file;// ��ȡϵͳ�ļ���·��
			try {
				//createPdfContextPing(fileName);
				createHPstorageCompPdf(fileName,ip);
			} catch (DocumentException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			request.setAttribute("filename", fileName);
		}

		return "/capreport/net/download.jsp";
	}
	
	/*
	 * HP�洢�ۺ�����DOC�ĵ�
	 */
	public void creatHPstorageCompDoc(String file,String ipaddress) throws DocumentException, IOException {
		
		  List<Disk> disks = null;
		  ArrayInfo arrayinfo = null;
		  List<Enclosure> enclosures = null;
		  List<Controller> controllers = null;
		  List<Port> ports = null;
		  List<Lun> luns = null;
		  List<VFP> vfps = null;
		  SubSystemInfo subSystemInfo = null;
		  
		  Hashtable ipAllData = new Hashtable();
		  //ֻȡ��ǰ�ɼ����ۺ���Ϣ
		  ipAllData = (Hashtable) ShareData.getSharedata().get("hpstorage:" + ipaddress);
		  if(ipAllData != null){
		      disks = (List<Disk>)ipAllData.get("disks");
		      arrayinfo = (ArrayInfo)ipAllData.get("arrayinfo");
		      enclosures = (List<Enclosure>)ipAllData.get("enclosures");
		      controllers = (List<Controller>)ipAllData.get("controllers");
		      ports = (List<Port>)ipAllData.get("ports");
		      luns = (List<Lun>)ipAllData.get("luns");
		      vfps = (List<VFP>)ipAllData.get("vfps");
		      subSystemInfo = (SubSystemInfo)ipAllData.get("subSystemInfo");
		  }	
		  
		// ����ֽ�Ŵ�С
		Document document = new Document(PageSize.A4);
		// ����һ����д��(Writer)��document���������ͨ����д��(Writer)���Խ��ĵ�д�뵽������
		RtfWriter2.getInstance(document, new FileOutputStream(file));
		document.open();
		// ������������
		BaseFont bfChinese = BaseFont.createFont("Times-Roman", "", BaseFont.NOT_EMBEDDED);
		// ����������
		Font titleFont = new Font(bfChinese, 12, Font.BOLD);
		Font subtitle = new Font(bfChinese, 8, Font.BOLD);
		// ����������
		Font contextFont = new Font(bfChinese, 10, Font.NORMAL);
		//Paragraph title = new Paragraph("������������ͨ�ʱ���", titleFont);
		Paragraph title = new Paragraph("HP�洢" + ipaddress + "�ۺϱ���", titleFont);
		// ���ñ����ʽ���뷽ʽ
		title.setAlignment(Element.ALIGN_CENTER);
		// title.setFont(titleFont);
		document.add(title);
		// ���� Table ���
		///Font fontChinese = new Font(bfChinese, 12, Font.NORMAL, Color.black);
		
		if(arrayinfo != null)
		{//������Ϣ	
		Table arrtable = new Table(4);		
		int arrwidth[] = {100,100,100,100};
		arrtable.setWidths(arrwidth);
		arrtable.setWidth(100); // ռҳ���� 90%
		arrtable.setAlignment(Element.ALIGN_CENTER);// ������ʾ
		arrtable.setAutoFillEmptyCells(true); // �Զ�����
		arrtable.setBorderWidth(1); // �߿���
		arrtable.setBorderColor(new Color(0, 125, 255)); // �߿���ɫ
		arrtable.setPadding(2);// �ľ࣬��Ч����֪��ʲô��˼��
		arrtable.setSpacing(0);// ����Ԫ��֮��ļ��
		arrtable.setBorder(2);// �߿�
		arrtable.endHeaders();
		
		Cell arrtitle = new Cell("������Ϣ");
		arrtitle.setBackgroundColor(Color.LIGHT_GRAY);
		arrtitle.setHorizontalAlignment(Element.ALIGN_LEFT);
		arrtitle.setColspan(4);
		
		Cell arrcell1 = new Cell("����״̬:");
		Cell arrcell2 = new Cell("�̼��汾:");
		Cell arrcell3 = new Cell("��Ʒ�汾:");
		Cell arrcell4 = new Cell("���ؿ�������Ʒ�汾:");
		Cell arrcell5 = new Cell("Զ�̿�������Ʒ�汾:");
		Cell arrcell6 = new Cell("");
		Cell arrcell66 = new Cell("");
		
		Cell arrcell11 = new Cell(SysUtil.ifNull(arrayinfo.getArrayStatus()));
		Cell arrcell22 = new Cell(SysUtil.ifNull(arrayinfo.getFirmwareRevision()));
		Cell arrcell33 = new Cell(SysUtil.ifNull(arrayinfo.getProductRevision()));
		Cell arrcell44 = new Cell(SysUtil.ifNull(arrayinfo.getLocalControllerProductRevision()));
		Cell arrcell55 = new Cell(SysUtil.ifNull(arrayinfo.getRemoteControllerProductRevision()));
		
		arrcell1.setHorizontalAlignment(Element.ALIGN_CENTER);
		arrcell2.setHorizontalAlignment(Element.ALIGN_CENTER);
		arrcell3.setHorizontalAlignment(Element.ALIGN_CENTER);
		arrcell4.setHorizontalAlignment(Element.ALIGN_CENTER);
		arrcell5.setHorizontalAlignment(Element.ALIGN_CENTER);
		
		arrcell11.setHorizontalAlignment(Element.ALIGN_CENTER);
		arrcell22.setHorizontalAlignment(Element.ALIGN_CENTER);
		arrcell33.setHorizontalAlignment(Element.ALIGN_CENTER);
		arrcell44.setHorizontalAlignment(Element.ALIGN_CENTER);
		arrcell55.setHorizontalAlignment(Element.ALIGN_CENTER);
		
		arrtable.addCell(arrtitle);
		arrtable.addCell(arrcell1);
		arrtable.addCell(arrcell11);
		arrtable.addCell(arrcell2);
		arrtable.addCell(arrcell22);
		arrtable.addCell(arrcell3);
		arrtable.addCell(arrcell33);
		arrtable.addCell(arrcell4);
		arrtable.addCell(arrcell44);
		arrtable.addCell(arrcell5);
		arrtable.addCell(arrcell55);
		arrtable.addCell(arrcell6);
		arrtable.addCell(arrcell66);
		document.add(arrtable);		
		}
		
		if(enclosures != null && enclosures.size()>0)
		{//������Ϣ
			for(int i = 0;i<enclosures.size();i++)
			{
				Enclosure encl = enclosures.get(i);
				
				Table encltable = new Table(4);		
				int enclwidth[] = {100,100,100,100};
				encltable.setWidths(enclwidth);
				encltable.setWidth(100); // ռҳ���� 90%
				encltable.setAlignment(Element.ALIGN_CENTER);// ������ʾ
				encltable.setAutoFillEmptyCells(true); // �Զ�����
				encltable.setBorderWidth(1); // �߿���
				encltable.setBorderColor(new Color(0, 125, 255)); // �߿���ɫ
				encltable.setPadding(2);// �ľ࣬��Ч����֪��ʲô��˼��
				encltable.setSpacing(0);// ����Ԫ��֮��ļ��
				encltable.setBorder(2);// �߿�
				encltable.endHeaders();
				
				Cell encltitle = new Cell("������Ϣ");
				encltitle.setBackgroundColor(Color.LIGHT_GRAY);
				encltitle.setHorizontalAlignment(Element.ALIGN_LEFT);
				encltitle.setColspan(4);
				
				Cell enclcell1 = new Cell("��������:");
				Cell enclcell2 = new Cell("�������:");
				Cell enclcell3 = new Cell("����״̬:");
				Cell enclcell4 = new Cell("��������:");
				Cell enclcell5 = new Cell("����ȫ������:");
				Cell enclcell6 = new Cell("");
				Cell enclcell66 = new Cell("");
				
				Cell enclcell11 = new Cell(SysUtil.ifNull(encl.getName()));
				Cell enclcell22 = new Cell(SysUtil.ifNull(encl.getEnclosureID()));
				Cell enclcell33 = new Cell(SysUtil.ifNull(encl.getEnclosureStatus()));
				Cell enclcell44 = new Cell(SysUtil.ifNull(encl.getEnclosureType()));
				Cell enclcell55 = new Cell(SysUtil.ifNull(encl.getNodeWWN()));
				
				enclcell1.setHorizontalAlignment(Element.ALIGN_CENTER);
				enclcell2.setHorizontalAlignment(Element.ALIGN_CENTER);
				enclcell3.setHorizontalAlignment(Element.ALIGN_CENTER);
				enclcell4.setHorizontalAlignment(Element.ALIGN_CENTER);
				enclcell5.setHorizontalAlignment(Element.ALIGN_CENTER);
				
				enclcell11.setHorizontalAlignment(Element.ALIGN_CENTER);
				enclcell22.setHorizontalAlignment(Element.ALIGN_CENTER);
				enclcell33.setHorizontalAlignment(Element.ALIGN_CENTER);
				enclcell44.setHorizontalAlignment(Element.ALIGN_CENTER);
				enclcell55.setHorizontalAlignment(Element.ALIGN_CENTER);
				
				encltable.addCell(encltitle);
				encltable.addCell(enclcell1);
				encltable.addCell(enclcell11);
				encltable.addCell(enclcell2);
				encltable.addCell(enclcell22);
				encltable.addCell(enclcell3);
				encltable.addCell(enclcell33);
				encltable.addCell(enclcell4);
				encltable.addCell(enclcell44);
				encltable.addCell(enclcell5);
				encltable.addCell(enclcell55);
				encltable.addCell(enclcell6);
				encltable.addCell(enclcell66);
				document.add(encltable);
				if(encl.getFrus() != null && encl.getFrus().size()>0)
				{
					Table enclfrutable = new Table(4);		
					int enclfruwidth[] = {100,100,100,100};
					enclfrutable.setWidths(enclfruwidth);
					enclfrutable.setWidth(100); // ռҳ���� 90%
					enclfrutable.setAlignment(Element.ALIGN_CENTER);// ������ʾ
					enclfrutable.setAutoFillEmptyCells(true); // �Զ�����
					encltable.setBorderWidth(1); // �߿���
					enclfrutable.setBorderColor(new Color(0, 125, 255)); // �߿���ɫ
					enclfrutable.setPadding(2);// �ľ࣬��Ч����֪��ʲô��˼��
					enclfrutable.setSpacing(0);// ����Ԫ��֮��ļ��
					enclfrutable.setBorder(2);// �߿�
					enclfrutable.endHeaders();
					
					Cell enclfrutitle = new Cell("FRU��Ϣ");
					enclfrutitle.setBackgroundColor(Color.LIGHT_GRAY);
					enclfrutitle.setHorizontalAlignment(Element.ALIGN_LEFT);
					enclfrutitle.setColspan(4);
					enclfrutable.addCell(enclfrutitle);
					
					Cell enclfrucell1 = new Cell("����:");
					Cell enclfrucell2 = new Cell("���:");
					Cell enclfrucell3 = new Cell("��ʶ:");
					Cell enclfrucell4 = new Cell("״̬:");
					
					enclfrucell1.setHorizontalAlignment(Element.ALIGN_CENTER);
					enclfrucell2.setHorizontalAlignment(Element.ALIGN_CENTER);
					enclfrucell3.setHorizontalAlignment(Element.ALIGN_CENTER);
					enclfrucell4.setHorizontalAlignment(Element.ALIGN_CENTER);
					
					enclfrutable.addCell(enclfrucell1);
					enclfrutable.addCell(enclfrucell2);
					enclfrutable.addCell(enclfrucell3);
					enclfrutable.addCell(enclfrucell4);
					
					 for(int j=0;j<encl.getFrus().size();j++)
			         {
			               EnclosureFru fru = encl.getFrus().get(j);
			               
			               Cell enclfrucell11 = new Cell(SysUtil.ifNull(fru.getFru()));
						   Cell enclfrucell22 = new Cell(SysUtil.ifNull(fru.getHwComponent()));
						   Cell enclfrucell33 = new Cell(SysUtil.ifNull(fru.getIdentification()));
						   Cell enclfrucell44 = new Cell(SysUtil.ifNull(fru.getIdStatus()));
						   
						   enclfrucell11.setHorizontalAlignment(Element.ALIGN_CENTER);
						   enclfrucell22.setHorizontalAlignment(Element.ALIGN_CENTER);
						   enclfrucell33.setHorizontalAlignment(Element.ALIGN_CENTER);
						   enclfrucell44.setHorizontalAlignment(Element.ALIGN_CENTER);
						   
						   enclfrutable.addCell(enclfrucell11);
						   enclfrutable.addCell(enclfrucell22);
						   enclfrutable.addCell(enclfrucell33);
						   enclfrutable.addCell(enclfrucell44);        
			        }
					document.add(enclfrutable);
				}
				
			}
			
		}
		
		if(controllers != null)
		{//��������Ϣ
			for(int i = 0;i<controllers.size();i++)
			{
				Controller cont = controllers.get(i);
				
				Table conttable = new Table(4);		
				int enclwidth[] = {100,100,100,100};
				conttable.setWidths(enclwidth);
				conttable.setWidth(100); // ռҳ���� 90%
				conttable.setAlignment(Element.ALIGN_CENTER);// ������ʾ
				conttable.setAutoFillEmptyCells(true); // �Զ�����
				conttable.setBorderWidth(1); // �߿���
				conttable.setBorderColor(new Color(0, 125, 255)); // �߿���ɫ
				conttable.setPadding(2);// �ľ࣬��Ч����֪��ʲô��˼��
				conttable.setSpacing(0);// ����Ԫ��֮��ļ��
				conttable.setBorder(2);// �߿�
				conttable.endHeaders();
				
				Cell conttitle = new Cell("��������Ϣ");
				conttitle.setBackgroundColor(Color.LIGHT_GRAY);
				conttitle.setHorizontalAlignment(Element.ALIGN_LEFT);
				conttitle.setColspan(4);
				
				Cell contcell1 = new Cell("����:");
				Cell contcell2 = new Cell("״̬:");
				Cell contcell3 = new Cell("���к�:");
				Cell contcell4 = new Cell("�����̱��:");
				Cell contcell5 = new Cell("��Ʒ���:");
				Cell contcell6 = new Cell("��Ʒ�汾:");
				Cell contcell7 = new Cell("�̼��汾:");
				Cell contcell8 = new Cell("�����̲�Ʒ����:");
				Cell contcell9 = new Cell("��������:");
				Cell contcell10 = new Cell("����ع̼��汾:");
				Cell contcell11 = new Cell("������������:");
				Cell contcell12 = new Cell("�������:");
				Cell contcell13 = new Cell("������:");
				Cell contcell14 = new Cell("�������:");
				Cell contcell15 = new Cell("����������ַ:");
				Cell contcell16 = new Cell("�ι̵�ַ:");
				
				contcell1.setHorizontalAlignment(Element.ALIGN_CENTER);
				contcell2.setHorizontalAlignment(Element.ALIGN_CENTER);
				contcell3.setHorizontalAlignment(Element.ALIGN_CENTER);
				contcell4.setHorizontalAlignment(Element.ALIGN_CENTER);				
				contcell5.setHorizontalAlignment(Element.ALIGN_CENTER);
				contcell6.setHorizontalAlignment(Element.ALIGN_CENTER);
				contcell7.setHorizontalAlignment(Element.ALIGN_CENTER);
				contcell8.setHorizontalAlignment(Element.ALIGN_CENTER);
				contcell9.setHorizontalAlignment(Element.ALIGN_CENTER);
				contcell10.setHorizontalAlignment(Element.ALIGN_CENTER);
				contcell11.setHorizontalAlignment(Element.ALIGN_CENTER);
				contcell12.setHorizontalAlignment(Element.ALIGN_CENTER);
				contcell13.setHorizontalAlignment(Element.ALIGN_CENTER);
				contcell14.setHorizontalAlignment(Element.ALIGN_CENTER);
				contcell15.setHorizontalAlignment(Element.ALIGN_CENTER);
				contcell16.setHorizontalAlignment(Element.ALIGN_CENTER);
				
				Cell vcontcell1 = new Cell(SysUtil.ifNull(cont.getName()));
				Cell contcell22 = new Cell(SysUtil.ifNull(cont.getStatus()));
				Cell contcell33 = new Cell(SysUtil.ifNull(cont.getSerialNumber()));
				Cell contcell44 = new Cell(SysUtil.ifNull(cont.getVendorID()));
				Cell contcell55 = new Cell(SysUtil.ifNull(cont.getProductID()));
				Cell contcell66 = new Cell(SysUtil.ifNull(cont.getProductRevision()));
				Cell contcell77 = new Cell(SysUtil.ifNull(cont.getFirmwareRevision()));
				Cell contcell88 = new Cell(SysUtil.ifNull(cont.getManufacturingProductCode()));
				Cell contcell99 = new Cell(SysUtil.ifNull(cont.getControllerType()));
				Cell contcell100 = new Cell(SysUtil.ifNull(cont.getBatteryChargerFirmwareRevision()));
				Cell contcell111 = new Cell(SysUtil.ifNull(cont.getEnclosureSwitchSetting()));
				Cell contcell122 = new Cell(SysUtil.ifNull(cont.getEnclosureID()));
				Cell contcell133 = new Cell(SysUtil.ifNull(cont.getLoopPair()));
				Cell contcell144 = new Cell(SysUtil.ifNull(cont.getLoopID()));
				Cell contcell155 = new Cell(SysUtil.ifNull(cont.getDriveAddressBasis()));
				Cell contcell166 = new Cell(SysUtil.ifNull(cont.getHardAddress()));
				
				vcontcell1.setHorizontalAlignment(Element.ALIGN_CENTER);
				contcell22.setHorizontalAlignment(Element.ALIGN_CENTER);				
				contcell33.setHorizontalAlignment(Element.ALIGN_CENTER);
				contcell44.setHorizontalAlignment(Element.ALIGN_CENTER);
				contcell55.setHorizontalAlignment(Element.ALIGN_CENTER);
				contcell66.setHorizontalAlignment(Element.ALIGN_CENTER);
				contcell77.setHorizontalAlignment(Element.ALIGN_CENTER);				
				contcell88.setHorizontalAlignment(Element.ALIGN_CENTER);
				contcell99.setHorizontalAlignment(Element.ALIGN_CENTER);
				contcell100.setHorizontalAlignment(Element.ALIGN_CENTER);
				contcell111.setHorizontalAlignment(Element.ALIGN_CENTER);
				contcell122.setHorizontalAlignment(Element.ALIGN_CENTER);
				contcell133.setHorizontalAlignment(Element.ALIGN_CENTER);
				contcell144.setHorizontalAlignment(Element.ALIGN_CENTER);
				contcell155.setHorizontalAlignment(Element.ALIGN_CENTER);
				contcell166.setHorizontalAlignment(Element.ALIGN_CENTER);
				
				conttable.addCell(conttitle);
				conttable.addCell(contcell1);
				conttable.addCell(vcontcell1);
				conttable.addCell(contcell2);
				conttable.addCell(contcell22);
				conttable.addCell(contcell3);
				conttable.addCell(contcell33);
				conttable.addCell(contcell4);
				conttable.addCell(contcell44);
				conttable.addCell(contcell5);
				conttable.addCell(contcell55);
				conttable.addCell(contcell6);
				conttable.addCell(contcell66);
				conttable.addCell(contcell7);
				conttable.addCell(contcell77);
				conttable.addCell(contcell8);
				conttable.addCell(contcell88);
				conttable.addCell(contcell9);
				conttable.addCell(contcell99);
				conttable.addCell(contcell10);
				conttable.addCell(contcell100);
				conttable.addCell(contcell11);
				conttable.addCell(contcell111);
				conttable.addCell(contcell12);
				conttable.addCell(contcell122);
				conttable.addCell(contcell13);
				conttable.addCell(contcell133);
				conttable.addCell(contcell14);
				conttable.addCell(contcell144);
				conttable.addCell(contcell15);
				conttable.addCell(contcell155);
				conttable.addCell(contcell16);
				conttable.addCell(contcell166);
				document.add(conttable);
				
				if(cont.getFrontPortList() != null && cont.getFrontPortList().size() > 0)
				{
					Table fptable = new Table(4);		
					int fpwidth[] = {100,100,100,100};
					fptable.setWidths(fpwidth);
					fptable.setWidth(100); // ռҳ���� 90%
					fptable.setAlignment(Element.ALIGN_CENTER);// ������ʾ
					fptable.setAutoFillEmptyCells(true); // �Զ�����
					fptable.setBorderWidth(1); // �߿���
					fptable.setBorderColor(new Color(0, 125, 255)); // �߿���ɫ
					fptable.setPadding(2);// �ľ࣬��Ч����֪��ʲô��˼��
					fptable.setSpacing(0);// ����Ԫ��֮��ļ��
					fptable.setBorder(2);// �߿�
					fptable.endHeaders();
					
					Cell fptitle = new Cell("ǰ�˿��б�");
					fptitle.setBackgroundColor(Color.LIGHT_GRAY);
					fptitle.setHorizontalAlignment(Element.ALIGN_LEFT);
					fptitle.setColspan(4);
					fptable.addCell(fptitle);
					
					for(int j = 0;j<cont.getFrontPortList().size();j++)
					{
						CtrlPort fctp = cont.getFrontPortList().get(j);				
						Cell fpcell1 = new Cell("����:");
						Cell fpcell2 = new Cell("״̬:");
						Cell fpcell3 = new Cell("�˿�ʵ��:");
						Cell fpcell4 = new Cell("�����ַ:");
						Cell fpcell5 = new Cell("��������:");
						Cell fpcell6 = new Cell("�ڵ�ȫ������:");
						Cell fpcell7 = new Cell("�˿�ȫ������:");
						Cell fpcell8 = new Cell("����:");
						Cell fpcell9 = new Cell("��������:");
						Cell fpcell10 = new Cell("�˿ڱ��:");
						Cell fpcell11 = new Cell("�豸����:");
						Cell fpcell12 = new Cell("�豸·��:");
						
						fpcell1.setHorizontalAlignment(Element.ALIGN_CENTER);
						fpcell2.setHorizontalAlignment(Element.ALIGN_CENTER);
						fpcell3.setHorizontalAlignment(Element.ALIGN_CENTER);
						fpcell4.setHorizontalAlignment(Element.ALIGN_CENTER);
						fpcell5.setHorizontalAlignment(Element.ALIGN_CENTER);
						fpcell6.setHorizontalAlignment(Element.ALIGN_CENTER);
						fpcell7.setHorizontalAlignment(Element.ALIGN_CENTER);
						fpcell8.setHorizontalAlignment(Element.ALIGN_CENTER);
						fpcell9.setHorizontalAlignment(Element.ALIGN_CENTER);
						fpcell10.setHorizontalAlignment(Element.ALIGN_CENTER);
						fpcell11.setHorizontalAlignment(Element.ALIGN_CENTER);
						fpcell12.setHorizontalAlignment(Element.ALIGN_CENTER);
						
						Cell vfpcell1 = new Cell(SysUtil.ifNull(fctp.getName()));
						Cell vfpcell2 = new Cell(SysUtil.ifNull(fctp.getStatus()));
						Cell vfpcell3 = new Cell(SysUtil.ifNull(fctp.getPortInstance()));
						Cell vfpcell4 = new Cell(SysUtil.ifNull(fctp.getHardAddress()));
						Cell vfpcell5 = new Cell(SysUtil.ifNull(fctp.getLinkState()));
						Cell vfpcell6 = new Cell(SysUtil.ifNull(fctp.getNodeWWN()));
						Cell vfpcell7 = new Cell(SysUtil.ifNull(fctp.getPortWWN()));
						Cell vfpcell8 = new Cell(SysUtil.ifNull(fctp.getTopology()));
						Cell vfpcell9 = new Cell(SysUtil.ifNull(fctp.getDataRate()));
						Cell vfpcell10 = new Cell(SysUtil.ifNull(fctp.getPortID()));
						Cell vfpcell11 = new Cell(SysUtil.ifNull(fctp.getDeviceHostName()));
						Cell vfpcell12 = new Cell(SysUtil.ifNull(fctp.getDevicePath()));
						
						vfpcell1.setHorizontalAlignment(Element.ALIGN_CENTER);
						vfpcell2.setHorizontalAlignment(Element.ALIGN_CENTER);
						vfpcell3.setHorizontalAlignment(Element.ALIGN_CENTER);
						vfpcell4.setHorizontalAlignment(Element.ALIGN_CENTER);
						vfpcell5.setHorizontalAlignment(Element.ALIGN_CENTER);
						vfpcell6.setHorizontalAlignment(Element.ALIGN_CENTER);
						vfpcell7.setHorizontalAlignment(Element.ALIGN_CENTER);
						vfpcell8.setHorizontalAlignment(Element.ALIGN_CENTER);
						vfpcell9.setHorizontalAlignment(Element.ALIGN_CENTER);
						vfpcell10.setHorizontalAlignment(Element.ALIGN_CENTER);
						vfpcell11.setHorizontalAlignment(Element.ALIGN_CENTER);
						vfpcell12.setHorizontalAlignment(Element.ALIGN_CENTER);
						
						fptable.addCell(fpcell1);
						fptable.addCell(vfpcell1);
						fptable.addCell(fpcell2);
						fptable.addCell(vfpcell2);
						fptable.addCell(fpcell3);
						fptable.addCell(vfpcell3);
						fptable.addCell(fpcell4);
						fptable.addCell(vfpcell4);
						fptable.addCell(fpcell5);
						fptable.addCell(vfpcell5);
						fptable.addCell(fpcell6);
						fptable.addCell(vfpcell6);
						fptable.addCell(fpcell7);
						fptable.addCell(vfpcell7);
						fptable.addCell(fpcell8);
						fptable.addCell(vfpcell8);
						fptable.addCell(fpcell9);
						fptable.addCell(vfpcell9);
						fptable.addCell(fpcell10);
						fptable.addCell(vfpcell10);
						fptable.addCell(fpcell11);
						fptable.addCell(vfpcell11);
						fptable.addCell(fpcell12);
						fptable.addCell(vfpcell12);						
					}
					document.add(fptable);
				}
				if(cont.getBackPortList() != null && cont.getBackPortList().size() > 0)
				{
					Table bptable = new Table(4);		
					int bpwidth[] = {100,100,100,100};
					bptable.setWidths(bpwidth);
					bptable.setWidth(100); // ռҳ���� 90%
					bptable.setAlignment(Element.ALIGN_CENTER);// ������ʾ
					bptable.setAutoFillEmptyCells(true); // �Զ�����
					bptable.setBorderWidth(1); // �߿���
					bptable.setBorderColor(new Color(0, 125, 255)); // �߿���ɫ
					bptable.setPadding(2);// �ľ࣬��Ч����֪��ʲô��˼��
					bptable.setSpacing(0);// ����Ԫ��֮��ļ��
					bptable.setBorder(2);// �߿�
					bptable.endHeaders();
					
					Cell bptitle = new Cell("��˿��б�");
					bptitle.setBackgroundColor(Color.LIGHT_GRAY);
					bptitle.setHorizontalAlignment(Element.ALIGN_LEFT);
					bptitle.setColspan(4);
					bptable.addCell(bptitle);
					
					for(int j = 0;j<cont.getBackPortList().size();j++)
					{
						CtrlPort bctp = cont.getBackPortList().get(j);				
						Cell bpcell1 = new Cell("����:");
						Cell bpcell2 = new Cell("״̬:");
						Cell bpcell3 = new Cell("�˿�ʵ��:");
						Cell bpcell4 = new Cell("�����ַ:");
						Cell bpcell5 = new Cell("��������:");
						Cell bpcell6 = new Cell("�ڵ�ȫ������:");
						Cell bpcell7 = new Cell("�˿�ȫ������:");
						Cell bpcell8 = new Cell("����:");
						Cell bpcell9 = new Cell("��������:");
						Cell bpcell10 = new Cell("�˿ڱ��:");
						Cell bpcell11 = new Cell("�豸����:");
						Cell bpcell12 = new Cell("�豸·��:");
						
						bpcell1.setHorizontalAlignment(Element.ALIGN_CENTER);
						bpcell2.setHorizontalAlignment(Element.ALIGN_CENTER);
						bpcell3.setHorizontalAlignment(Element.ALIGN_CENTER);
						bpcell4.setHorizontalAlignment(Element.ALIGN_CENTER);
						bpcell5.setHorizontalAlignment(Element.ALIGN_CENTER);
						bpcell6.setHorizontalAlignment(Element.ALIGN_CENTER);
						bpcell7.setHorizontalAlignment(Element.ALIGN_CENTER);
						bpcell8.setHorizontalAlignment(Element.ALIGN_CENTER);
						bpcell9.setHorizontalAlignment(Element.ALIGN_CENTER);
						bpcell10.setHorizontalAlignment(Element.ALIGN_CENTER);
						bpcell11.setHorizontalAlignment(Element.ALIGN_CENTER);
						bpcell12.setHorizontalAlignment(Element.ALIGN_CENTER);
						
						Cell vbpcell1 = new Cell(SysUtil.ifNull(bctp.getName()));
						Cell vbpcell2 = new Cell(SysUtil.ifNull(bctp.getStatus()));
						Cell vbpcell3 = new Cell(SysUtil.ifNull(bctp.getPortInstance()));
						Cell vbpcell4 = new Cell(SysUtil.ifNull(bctp.getHardAddress()));
						Cell vbpcell5 = new Cell(SysUtil.ifNull(bctp.getLinkState()));
						Cell vbpcell6 = new Cell(SysUtil.ifNull(bctp.getNodeWWN()));
						Cell vbpcell7 = new Cell(SysUtil.ifNull(bctp.getPortWWN()));
						Cell vbpcell8 = new Cell(SysUtil.ifNull(bctp.getTopology()));
						Cell vbpcell9 = new Cell(SysUtil.ifNull(bctp.getDataRate()));
						Cell vbpcell10 = new Cell(SysUtil.ifNull(bctp.getPortID()));
						Cell vbpcell11 = new Cell(SysUtil.ifNull(bctp.getDeviceHostName()));
						Cell vbpcell12 = new Cell(SysUtil.ifNull(bctp.getDevicePath()));
						
						vbpcell1.setHorizontalAlignment(Element.ALIGN_CENTER);
						vbpcell2.setHorizontalAlignment(Element.ALIGN_CENTER);
						vbpcell3.setHorizontalAlignment(Element.ALIGN_CENTER);
						vbpcell4.setHorizontalAlignment(Element.ALIGN_CENTER);
						vbpcell5.setHorizontalAlignment(Element.ALIGN_CENTER);
						vbpcell6.setHorizontalAlignment(Element.ALIGN_CENTER);
						vbpcell7.setHorizontalAlignment(Element.ALIGN_CENTER);
						vbpcell8.setHorizontalAlignment(Element.ALIGN_CENTER);
						vbpcell9.setHorizontalAlignment(Element.ALIGN_CENTER);
						vbpcell10.setHorizontalAlignment(Element.ALIGN_CENTER);
						vbpcell11.setHorizontalAlignment(Element.ALIGN_CENTER);
						vbpcell12.setHorizontalAlignment(Element.ALIGN_CENTER);
						
						bptable.addCell(bpcell1);
						bptable.addCell(vbpcell1);
						bptable.addCell(bpcell2);
						bptable.addCell(vbpcell2);
						bptable.addCell(bpcell3);
						bptable.addCell(vbpcell3);
						bptable.addCell(bpcell4);
						bptable.addCell(vbpcell4);
						bptable.addCell(bpcell5);
						bptable.addCell(vbpcell5);
						bptable.addCell(bpcell6);
						bptable.addCell(vbpcell6);
						bptable.addCell(bpcell7);
						bptable.addCell(vbpcell7);
						bptable.addCell(bpcell8);
						bptable.addCell(vbpcell8);
						bptable.addCell(bpcell9);
						bptable.addCell(vbpcell9);
						bptable.addCell(bpcell10);
						bptable.addCell(vbpcell10);
						bptable.addCell(bpcell11);
						bptable.addCell(vbpcell11);
						bptable.addCell(bpcell12);
						bptable.addCell(vbpcell12);	
					}
					document.add(bptable);
				}
				if(cont.getDimmList() != null && cont.getDimmList().size()>0)
				{
					Table dimtable = new Table(4);		
					int dimwidth[] = {100,100,100,100};
					dimtable.setWidths(dimwidth);
					dimtable.setWidth(100); // ռҳ���� 90%
					dimtable.setAlignment(Element.ALIGN_CENTER);// ������ʾ
					dimtable.setAutoFillEmptyCells(true); // �Զ�����
					dimtable.setBorderWidth(1); // �߿���
					dimtable.setBorderColor(new Color(0, 125, 255)); // �߿���ɫ
					dimtable.setPadding(2);// �ľ࣬��Ч����֪��ʲô��˼��
					dimtable.setSpacing(0);// ����Ԫ��֮��ļ��
					dimtable.setBorder(2);// �߿�
					dimtable.endHeaders();
					
					Cell dimtitle = new Cell("˫��ֱ��ʽ�洢ģ���б�");
					dimtitle.setBackgroundColor(Color.LIGHT_GRAY);
					dimtitle.setHorizontalAlignment(Element.ALIGN_LEFT);
					dimtitle.setColspan(4);
					dimtable.addCell(dimtitle);
					
					for(int j=0;j<cont.getDimmList().size();j++)
		            {
		                DIMM dimm = cont.getDimmList().get(j);
		                Cell dimcell1 = new Cell("����:");
		                Cell dimcell2 = new Cell("״̬:");
		                Cell dimcell3 = new Cell("����:");
		                Cell dimcell4 = new Cell("");
		                
		                dimcell1.setHorizontalAlignment(Element.ALIGN_CENTER);
		                dimcell2.setHorizontalAlignment(Element.ALIGN_CENTER);
		                dimcell3.setHorizontalAlignment(Element.ALIGN_CENTER);
		                dimcell4.setHorizontalAlignment(Element.ALIGN_CENTER);
		                
		                Cell vdimcell1 = new Cell(SysUtil.ifNull(dimm.getName()));
		                Cell vdimcell2 = new Cell(SysUtil.ifNull(dimm.getStatus()));
		                Cell vdimcell3 = new Cell(SysUtil.ifNull(dimm.getCapacity()));
		                Cell vdimcell4 = new Cell("");
		                
		                vdimcell1.setHorizontalAlignment(Element.ALIGN_CENTER);
		                vdimcell2.setHorizontalAlignment(Element.ALIGN_CENTER);
		                vdimcell3.setHorizontalAlignment(Element.ALIGN_CENTER);
		                vdimcell4.setHorizontalAlignment(Element.ALIGN_CENTER);
		                
		                dimtable.addCell(dimcell1);
		                dimtable.addCell(vdimcell1);
		                dimtable.addCell(dimcell2);
		                dimtable.addCell(vdimcell2);
		                dimtable.addCell(dimcell3);
		                dimtable.addCell(vdimcell3);
		                dimtable.addCell(dimcell4);
		                dimtable.addCell(vdimcell4);    
		            }
					document.add(dimtable);
				}
				if(cont.getBattery() != null)
				{
					Battery battery = cont.getBattery();
					Table battable = new Table(4);		
					int batwidth[] = {100,100,100,100};
					battable.setWidths(batwidth);
					battable.setWidth(100); // ռҳ���� 90%
					battable.setAlignment(Element.ALIGN_CENTER);// ������ʾ
					battable.setAutoFillEmptyCells(true); // �Զ�����
					battable.setBorderWidth(1); // �߿���
					battable.setBorderColor(new Color(0, 125, 255)); // �߿���ɫ
					battable.setPadding(2);// �ľ࣬��Ч����֪��ʲô��˼��
					battable.setSpacing(0);// ����Ԫ��֮��ļ��
					battable.setBorder(2);// �߿�
					battable.endHeaders();
					
					Cell battitle = new Cell("�����Ϣ");
					battitle.setBackgroundColor(Color.LIGHT_GRAY);
					battitle.setHorizontalAlignment(Element.ALIGN_LEFT);
					battitle.setColspan(4);
					battable.addCell(battitle);
					
					Cell batcell1 = new Cell("����:");
	                Cell batcell2 = new Cell("״̬:");
	                Cell batcell3 = new Cell("��ʶ:");
	                Cell batcell4 = new Cell("����:");
	                Cell batcell5 = new Cell("�豸����:");
	                Cell batcell6 = new Cell("��������:");
	                Cell batcell7 = new Cell("ʣ�����:");
	                Cell batcell8 = new Cell("ʣ������ٷֱ�:");
	                Cell batcell9 = new Cell("��ѹ:");
	                Cell batcell10 = new Cell("�ŵ�����:");
	                
	                batcell1.setHorizontalAlignment(Element.ALIGN_CENTER);
	                batcell2.setHorizontalAlignment(Element.ALIGN_CENTER);
	                batcell3.setHorizontalAlignment(Element.ALIGN_CENTER);
	                batcell4.setHorizontalAlignment(Element.ALIGN_CENTER);
	                batcell5.setHorizontalAlignment(Element.ALIGN_CENTER);
	                batcell6.setHorizontalAlignment(Element.ALIGN_CENTER);
	                batcell7.setHorizontalAlignment(Element.ALIGN_CENTER);
	                batcell8.setHorizontalAlignment(Element.ALIGN_CENTER);
	                batcell9.setHorizontalAlignment(Element.ALIGN_CENTER);
	                batcell10.setHorizontalAlignment(Element.ALIGN_CENTER);
	                
	                Cell vbatcell1 = new Cell(SysUtil.ifNull(battery.getName()));
	                Cell vbatcell2 = new Cell(SysUtil.ifNull(battery.getStatus()));
	                Cell vbatcell3 = new Cell(SysUtil.ifNull(battery.getIdentification()));
	                Cell vbatcell4 = new Cell(SysUtil.ifNull(battery.getManufacturerName()));
	                Cell vbatcell5 = new Cell(SysUtil.ifNull(battery.getDeviceName()));
	                Cell vbatcell6 = new Cell(SysUtil.ifNull(battery.getManufacturerDate()));
	                Cell vbatcell7 = new Cell(SysUtil.ifNull(battery.getRemainingCapacity()));
	                Cell vbatcell8 = new Cell(SysUtil.ifNull(battery.getPctRemainingCapacity()));
	                Cell vbatcell9 = new Cell(SysUtil.ifNull(battery.getVoltage()));
	                Cell vbatcell10 = new Cell(SysUtil.ifNull(battery.getDischargeCycles()));
	                
	                vbatcell1.setHorizontalAlignment(Element.ALIGN_CENTER);
	                vbatcell2.setHorizontalAlignment(Element.ALIGN_CENTER);
	                vbatcell3.setHorizontalAlignment(Element.ALIGN_CENTER);
	                vbatcell4.setHorizontalAlignment(Element.ALIGN_CENTER);
	                vbatcell5.setHorizontalAlignment(Element.ALIGN_CENTER);
	                vbatcell6.setHorizontalAlignment(Element.ALIGN_CENTER);
	                vbatcell7.setHorizontalAlignment(Element.ALIGN_CENTER);
	                vbatcell8.setHorizontalAlignment(Element.ALIGN_CENTER);
	                vbatcell9.setHorizontalAlignment(Element.ALIGN_CENTER);
	                vbatcell10.setHorizontalAlignment(Element.ALIGN_CENTER);
	                
	                battable.addCell(batcell1);
	                battable.addCell(vbatcell1);
	                battable.addCell(batcell2);
	                battable.addCell(vbatcell2);
	                battable.addCell(batcell3);
	                battable.addCell(vbatcell3);
	                battable.addCell(batcell4);
	                battable.addCell(vbatcell4);
	                battable.addCell(batcell5);
	                battable.addCell(vbatcell5);
	                battable.addCell(batcell6);
	                battable.addCell(vbatcell6);
	                battable.addCell(batcell7);
	                battable.addCell(vbatcell7);
	                battable.addCell(batcell8);
	                battable.addCell(vbatcell8);
	                battable.addCell(batcell9);
	                battable.addCell(vbatcell9);
	                battable.addCell(batcell10);
	                battable.addCell(vbatcell10);
	                
	                document.add(battable);	                
				}
				if(cont.getProcessor() != null)
	            {
					Processor processor = cont.getProcessor();
					Table protable = new Table(4);		
					int prowidth[] = {100,100,100,100};
					protable.setWidths(prowidth);
					protable.setWidth(100); // ռҳ���� 90%
					protable.setAlignment(Element.ALIGN_CENTER);// ������ʾ
					protable.setAutoFillEmptyCells(true); // �Զ�����
					protable.setBorderWidth(1); // �߿���
					protable.setBorderColor(new Color(0, 125, 255)); // �߿���ɫ
					protable.setPadding(2);// �ľ࣬��Ч����֪��ʲô��˼��
					protable.setSpacing(0);// ����Ԫ��֮��ļ��
					protable.setBorder(2);// �߿�
					protable.endHeaders();					
					
					Cell protitle = new Cell("��������Ϣ");
					protitle.setBackgroundColor(Color.LIGHT_GRAY);
					protitle.setHorizontalAlignment(Element.ALIGN_LEFT);
					protitle.setColspan(4);
					protable.addCell(protitle);
					
					Cell procell1 = new Cell("����:");
	                Cell procell2 = new Cell("״̬:");
	                Cell procell3 = new Cell("��ʶ:");
	                Cell procell4 = new Cell("");
	                
	                procell1.setHorizontalAlignment(Element.ALIGN_CENTER);
	                procell2.setHorizontalAlignment(Element.ALIGN_CENTER);
	                procell3.setHorizontalAlignment(Element.ALIGN_CENTER);
	                procell4.setHorizontalAlignment(Element.ALIGN_CENTER);
	                
	                Cell vprocell1 = new Cell(SysUtil.ifNull(processor.getName()));
	                Cell vprocell2 = new Cell(SysUtil.ifNull(processor.getStatus()));
	                Cell vprocell3 = new Cell(SysUtil.ifNull(processor.getIdentification()));
	                Cell vprocell4 = new Cell("");
	                
	                vprocell1.setHorizontalAlignment(Element.ALIGN_CENTER);
	                vprocell2.setHorizontalAlignment(Element.ALIGN_CENTER);
	                vprocell3.setHorizontalAlignment(Element.ALIGN_CENTER);
	                vprocell4.setHorizontalAlignment(Element.ALIGN_CENTER);
	                
	                protable.addCell(procell1);
	                protable.addCell(vprocell1);
	                protable.addCell(procell2);	                
	                protable.addCell(vprocell2);
	                protable.addCell(procell3);	                
	                protable.addCell(vprocell3);
	                protable.addCell(procell4);
	                protable.addCell(vprocell4);    
	                document.add(protable);
	            }
				
			}
		}
		
		if(ports != null && ports.size()>0)
		{
			Table portTable = new Table(6);
			int portwidth[] = {50, 50, 50, 100, 50, 150};
			portTable.setWidths(portwidth);
			portTable.setWidth(100); // ռҳ���� 90%
			portTable.setAlignment(Element.ALIGN_CENTER);// ������ʾ
			portTable.setAutoFillEmptyCells(true); // �Զ�����
			portTable.setBorderWidth(1); // �߿���
			portTable.setBorderColor(new Color(0, 125, 255)); // �߿���ɫ
			portTable.setPadding(2);// �ľ࣬��Ч����֪��ʲô��˼��
			portTable.setSpacing(0);// ����Ԫ��֮��ļ��
			portTable.setBorder(2);// �߿�
			portTable.endHeaders();	
			
			Cell porttitle = new Cell("�˿���Ϣ");
			porttitle.setBackgroundColor(Color.LIGHT_GRAY);
			porttitle.setHorizontalAlignment(Element.ALIGN_LEFT);
			porttitle.setColspan(6);
			portTable.addCell(porttitle);
			
			Cell portcell1 = new Cell("����");
			Cell portcell2 = new Cell("���");
			Cell portcell3 = new Cell("��Ϊ");
			Cell portcell4 = new Cell("�˿�����");
			Cell portcell5 = new Cell("�������ֵ");		
			Cell portcell6 = new Cell("���ݴ�������");			
			
			portcell1.setHorizontalAlignment(Element.ALIGN_CENTER);
			portcell2.setHorizontalAlignment(Element.ALIGN_CENTER);
			portcell3.setHorizontalAlignment(Element.ALIGN_CENTER);
			portcell4.setHorizontalAlignment(Element.ALIGN_CENTER);
			portcell5.setHorizontalAlignment(Element.ALIGN_CENTER);
			portcell6.setHorizontalAlignment(Element.ALIGN_CENTER);			

			portcell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
			portcell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
			portcell3.setVerticalAlignment(Element.ALIGN_MIDDLE);
			portcell4.setVerticalAlignment(Element.ALIGN_MIDDLE);
			portcell5.setVerticalAlignment(Element.ALIGN_MIDDLE);
			portcell6.setVerticalAlignment(Element.ALIGN_MIDDLE);			

			portcell1.setBackgroundColor(Color.LIGHT_GRAY);
			portcell2.setBackgroundColor(Color.LIGHT_GRAY);
			portcell3.setBackgroundColor(Color.LIGHT_GRAY);
			portcell4.setBackgroundColor(Color.LIGHT_GRAY);
			portcell5.setBackgroundColor(Color.LIGHT_GRAY);
			portcell6.setBackgroundColor(Color.LIGHT_GRAY);			
			
			portTable.addCell(portcell1);
			portTable.addCell(portcell2);
			portTable.addCell(portcell3);
			portTable.addCell(portcell4);
			portTable.addCell(portcell5);
			portTable.addCell(portcell6);			
			
				for (int i = 0; i < ports.size(); i++) {
					Port port = (Port) ports.get(i);
					
					Cell cell6 = new Cell(SysUtil.ifNull(port.getName()));
					Cell cell7 = new Cell(SysUtil.ifNull(port.getPortID()));
					Cell cell8 = new Cell(SysUtil.ifNull(port.getBehavior()));
					Cell cell9 = new Cell(SysUtil.ifNull(port.getTopology()));
					Cell cell10 = new Cell(SysUtil.ifNull(port.getQueueFullThreshold()));
					Cell cell15 = new Cell(SysUtil.ifNull(port.getDataRate()));					
					
					cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell7.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell8.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell9.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell10.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell15.setHorizontalAlignment(Element.ALIGN_CENTER);				
					
					portTable.addCell(cell6);
					portTable.addCell(cell7);
					portTable.addCell(cell8);
					portTable.addCell(cell9);
					portTable.addCell(cell10);
					portTable.addCell(cell15);
				}			
			document.add(portTable);
		}
		
		if(disks != null && disks.size()>0)
		{	
		Table aTable = new Table(7);
		int width[] = {50, 50, 50, 130, 30, 50,50};
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
		
		Cell atitle = new Cell("������Ϣ");
		atitle.setBackgroundColor(Color.LIGHT_GRAY);
		atitle.setHorizontalAlignment(Element.ALIGN_LEFT);
		atitle.setColspan(7);
		aTable.addCell(atitle);
		
		Cell cell1 = new Cell("����");
		Cell cell11 = new Cell("״̬");
		Cell cell2 = new Cell("����");
		Cell cell3 = new Cell("����");
		Cell cell4 = new Cell("������");		
		Cell cell13 = new Cell("��ʼ������");
		Cell cell14 = new Cell("�̼��汾");
		
		cell13.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell11.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell14.setHorizontalAlignment(Element.ALIGN_CENTER);

		cell13.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell11.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell4.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell14.setVerticalAlignment(Element.ALIGN_MIDDLE);

		cell1.setBackgroundColor(Color.LIGHT_GRAY);
		cell11.setBackgroundColor(Color.LIGHT_GRAY);
		cell13.setBackgroundColor(Color.LIGHT_GRAY);
		cell14.setBackgroundColor(Color.LIGHT_GRAY);
		cell2.setBackgroundColor(Color.LIGHT_GRAY);
		cell3.setBackgroundColor(Color.LIGHT_GRAY);
		cell4.setBackgroundColor(Color.LIGHT_GRAY);
		aTable.addCell(cell1);
		aTable.addCell(cell11);
		aTable.addCell(cell2);
		aTable.addCell(cell3);
		aTable.addCell(cell4);
		aTable.addCell(cell13);
		aTable.addCell(cell14);		
			for (int i = 0; i < disks.size(); i++) {
				Disk disk = (Disk) disks.get(i);				
				Cell cell6 = new Cell(SysUtil.ifNull(disk.getName()));
				Cell cell7 = new Cell(SysUtil.ifNull(disk.getStatus()));
				Cell cell8 = new Cell(SysUtil.ifNull(disk.getVendorID()));
				Cell cell9 = new Cell(SysUtil.ifNull(disk.getDataCapacity()));
				Cell cell10 = new Cell(SysUtil.ifNull(disk.getRedundancyGroup()));
				Cell cell15 = new Cell(SysUtil.ifNull(disk.getInitializeState()));
				Cell cell16 = new Cell(SysUtil.ifNull(disk.getFirmwareRevision()));
				
				cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell7.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell8.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell9.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell10.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell15.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell16.setHorizontalAlignment(Element.ALIGN_CENTER);
				
				aTable.addCell(cell6);
				aTable.addCell(cell7);
				aTable.addCell(cell8);
				aTable.addCell(cell9);
				aTable.addCell(cell10);
				aTable.addCell(cell15);
				aTable.addCell(cell16);
			}		
		    document.add(aTable);
		}
		if(luns != null && luns.size()>0)
		{
			Table lunTable = new Table(5);
			int lunwidth[] = {50, 50, 50, 100, 200};
			lunTable.setWidths(lunwidth);
			lunTable.setWidth(100); // ռҳ���� 90%
			lunTable.setAlignment(Element.ALIGN_CENTER);// ������ʾ
			lunTable.setAutoFillEmptyCells(true); // �Զ�����
			lunTable.setBorderWidth(1); // �߿���
			lunTable.setBorderColor(new Color(0, 125, 255)); // �߿���ɫ
			lunTable.setPadding(2);// �ľ࣬��Ч����֪��ʲô��˼��
			lunTable.setSpacing(0);// ����Ԫ��֮��ļ��
			lunTable.setBorder(2);// �߿�
			lunTable.endHeaders();	
			
			Cell luntitle = new Cell("�߼���Ԫ��Ϣ");
			luntitle.setBackgroundColor(Color.LIGHT_GRAY);
			luntitle.setHorizontalAlignment(Element.ALIGN_LEFT);
			luntitle.setColspan(5);
			lunTable.addCell(luntitle);
			
			Cell luncell1 = new Cell("����");
			Cell luncell2 = new Cell("���");
			Cell luncell3 = new Cell("��Ϊ");
			Cell luncell4 = new Cell("�˿�����");
			Cell luncell5 = new Cell("�������ֵ");						
			
			luncell1.setHorizontalAlignment(Element.ALIGN_CENTER);
			luncell2.setHorizontalAlignment(Element.ALIGN_CENTER);
			luncell3.setHorizontalAlignment(Element.ALIGN_CENTER);
			luncell4.setHorizontalAlignment(Element.ALIGN_CENTER);
			luncell5.setHorizontalAlignment(Element.ALIGN_CENTER);						

			luncell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
			luncell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
			luncell3.setVerticalAlignment(Element.ALIGN_MIDDLE);
			luncell4.setVerticalAlignment(Element.ALIGN_MIDDLE);
			luncell5.setVerticalAlignment(Element.ALIGN_MIDDLE);						

			luncell1.setBackgroundColor(Color.LIGHT_GRAY);
			luncell2.setBackgroundColor(Color.LIGHT_GRAY);
			luncell3.setBackgroundColor(Color.LIGHT_GRAY);
			luncell4.setBackgroundColor(Color.LIGHT_GRAY);
			luncell5.setBackgroundColor(Color.LIGHT_GRAY);					
			
			lunTable.addCell(luncell1);
			lunTable.addCell(luncell2);
			lunTable.addCell(luncell3);
			lunTable.addCell(luncell4);
			lunTable.addCell(luncell5);		
				for (int i = 0; i < luns.size(); i++) {
					Lun lun = (Lun) luns.get(i);
					
					Cell cell6 = new Cell(SysUtil.ifNull(lun.getName()));
					Cell cell7 = new Cell(SysUtil.ifNull(lun.getRedundancyGroup()));
					Cell cell8 = new Cell(SysUtil.ifNull(lun.getActive()));
					Cell cell9 = new Cell(SysUtil.ifNull(lun.getDataCapacity()));
					Cell cell10 = new Cell(SysUtil.ifNull(lun.getWwn()));										
					
					cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell7.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell8.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell9.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell10.setHorizontalAlignment(Element.ALIGN_CENTER);									
					
					lunTable.addCell(cell6);
					lunTable.addCell(cell7);
					lunTable.addCell(cell8);
					lunTable.addCell(cell9);
					lunTable.addCell(cell10);
					
				}			
			document.add(lunTable);
		}
		if(vfps != null && vfps.size()>0)
		{
			Table vfpTable = new Table(3);
			int vfpwidth[] = {50, 50, 50};
			vfpTable.setWidths(vfpwidth);
			vfpTable.setWidth(100); // ռҳ���� 90%
			vfpTable.setAlignment(Element.ALIGN_CENTER);// ������ʾ
			vfpTable.setAutoFillEmptyCells(true); // �Զ�����
			vfpTable.setBorderWidth(1); // �߿���
			vfpTable.setBorderColor(new Color(0, 125, 255)); // �߿���ɫ
			vfpTable.setPadding(2);// �ľ࣬��Ч����֪��ʲô��˼��
			vfpTable.setSpacing(0);// ����Ԫ��֮��ļ��
			vfpTable.setBorder(2);// �߿�
			vfpTable.endHeaders();	
			
			Cell vfptitle = new Cell("VFP��Ϣ");
			vfptitle.setBackgroundColor(Color.LIGHT_GRAY);
			vfptitle.setHorizontalAlignment(Element.ALIGN_LEFT);
			vfptitle.setColspan(3);
			vfpTable.addCell(vfptitle);
			
			Cell vfpcell1 = new Cell("����");
			Cell vfpcell2 = new Cell("������");
			Cell vfpcell3 = new Cell("��ҳֵ");									
			
			vfpcell1.setHorizontalAlignment(Element.ALIGN_CENTER);
			vfpcell2.setHorizontalAlignment(Element.ALIGN_CENTER);
			vfpcell3.setHorizontalAlignment(Element.ALIGN_CENTER);								

			vfpcell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
			vfpcell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
			vfpcell3.setVerticalAlignment(Element.ALIGN_MIDDLE);								

			vfpcell1.setBackgroundColor(Color.LIGHT_GRAY);
			vfpcell2.setBackgroundColor(Color.LIGHT_GRAY);
			vfpcell3.setBackgroundColor(Color.LIGHT_GRAY);								
			
			vfpTable.addCell(vfpcell1);
			vfpTable.addCell(vfpcell2);
			vfpTable.addCell(vfpcell3);				
				for (int i = 0; i < vfps.size(); i++) {
					VFP vfp = (VFP) vfps.get(i);
					
					Cell cell6 = new Cell(SysUtil.ifNull(vfp.getName()));
					Cell cell7 = new Cell(SysUtil.ifNull(vfp.getVFPBaudRate()));
					Cell cell8 = new Cell(SysUtil.ifNull(vfp.getVFPPagingValue()));															
					
					cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell7.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell8.setHorizontalAlignment(Element.ALIGN_CENTER);														
					
					vfpTable.addCell(cell6);
					vfpTable.addCell(cell7);
					vfpTable.addCell(cell8);				
				}			
			document.add(vfpTable);
		}
		if(subSystemInfo != null)
		{			
			Table substable = new Table(4);		
			int subswidth[] = {100,100,100,100};
			substable.setWidths(subswidth);
			substable.setWidth(100); // ռҳ���� 90%
			substable.setAlignment(Element.ALIGN_CENTER);// ������ʾ
			substable.setAutoFillEmptyCells(true); // �Զ�����
			substable.setBorderWidth(1); // �߿���
			substable.setBorderColor(new Color(0, 125, 255)); // �߿���ɫ
			substable.setPadding(2);// �ľ࣬��Ч����֪��ʲô��˼��
			substable.setSpacing(0);// ����Ԫ��֮��ļ��
			substable.setBorder(2);// �߿�
			substable.endHeaders();
			
			Cell substitle = new Cell("��ϵͳ��Ϣ");
			substitle.setBackgroundColor(Color.LIGHT_GRAY);
			substitle.setHorizontalAlignment(Element.ALIGN_LEFT);
			substitle.setColspan(4);
			substable.addCell(substitle);
			
			Cell subscell1 = new Cell("RAIDģʽ:");
			Cell subscell2 = new Cell("�Զ���ʽ������:");
			Cell subscell3 = new Cell("��ֱ���:");
			Cell subscell4 = new Cell("��������ٽ�ֵ:");
			Cell subscell5 = new Cell("��ֱ���:");
			Cell subscell6 = new Cell("��������ٽ�ֵ:");
			Cell subscell7 = new Cell("�������ٽ�ֵ:");
			Cell subscell8 = new Cell("�Ż�����:");
			Cell subscell9 = new Cell("�ֶ�����:");
			Cell subscell10 = new Cell("�ֶ�����Ŀ��:");
			Cell subscell11 = new Cell("���治�ܶ�:");
			Cell subscell12 = new Cell("�ؽ����ȼ�:");
			Cell subscell13 = new Cell("�Ƿ񼤻ȫ����:");
			Cell subscell14 = new Cell("�ػ������:");
			Cell subscell15 = new Cell("��ϵͳ���ͱ��:");
			Cell subscell16 = new Cell("�Ƿ���������ע:");
			Cell subscell17 = new Cell("�Ƿ����л���:");
			Cell subscell18 = new Cell("���治��д:");			
			Cell subscell19 = new Cell("д���:");
			Cell subscell20 = new Cell("�Ƿ���Ԥȡ:");
			Cell subscell21 = new Cell("�رյڶ�·������:");
			Cell subscell22 = new Cell("");		
			
			subscell1.setHorizontalAlignment(Element.ALIGN_CENTER);
			subscell2.setHorizontalAlignment(Element.ALIGN_CENTER);
			subscell3.setHorizontalAlignment(Element.ALIGN_CENTER);
			subscell4.setHorizontalAlignment(Element.ALIGN_CENTER);
			subscell5.setHorizontalAlignment(Element.ALIGN_CENTER);
			subscell6.setHorizontalAlignment(Element.ALIGN_CENTER);			
			subscell7.setHorizontalAlignment(Element.ALIGN_CENTER);
			subscell8.setHorizontalAlignment(Element.ALIGN_CENTER);
			subscell9.setHorizontalAlignment(Element.ALIGN_CENTER);
			subscell10.setHorizontalAlignment(Element.ALIGN_CENTER);
			subscell11.setHorizontalAlignment(Element.ALIGN_CENTER);
			subscell12.setHorizontalAlignment(Element.ALIGN_CENTER);
			subscell13.setHorizontalAlignment(Element.ALIGN_CENTER);
			subscell14.setHorizontalAlignment(Element.ALIGN_CENTER);
			subscell15.setHorizontalAlignment(Element.ALIGN_CENTER);			
			subscell16.setHorizontalAlignment(Element.ALIGN_CENTER);
			subscell17.setHorizontalAlignment(Element.ALIGN_CENTER);
			subscell18.setHorizontalAlignment(Element.ALIGN_CENTER);
			subscell9.setHorizontalAlignment(Element.ALIGN_CENTER);
			subscell20.setHorizontalAlignment(Element.ALIGN_CENTER);
			subscell21.setHorizontalAlignment(Element.ALIGN_CENTER);
			subscell22.setHorizontalAlignment(Element.ALIGN_CENTER);
			
			Cell vsubscell1 = new Cell(SysUtil.ifNull(subSystemInfo.getRaidLevel()));
			Cell vsubscell2 = new Cell(SysUtil.ifNull(subSystemInfo.getAutoFormatDrive()));
			Cell vsubscell3 = new Cell(SysUtil.ifNull(subSystemInfo.getHangDetection()));
			Cell vsubscell4 = new Cell(SysUtil.ifNull(subSystemInfo.getCapacityDepletionThreshold()));
			Cell vsubscell5 = new Cell(SysUtil.ifNull(subSystemInfo.getHangDetection()));
			Cell vsubscell6 = new Cell(SysUtil.ifNull(subSystemInfo.getCapacityDepletionThreshold()));
			Cell vsubscell7 = new Cell(SysUtil.ifNull(subSystemInfo.getQueueFullThresholdMaximum()));
			Cell vsubscell8 = new Cell(SysUtil.ifNull(subSystemInfo.getEnableOptimizePolicy()));
			Cell vsubscell9 = new Cell(SysUtil.ifNull(subSystemInfo.getEnableManualOverride()));
			Cell vsubscell10 = new Cell(SysUtil.ifNull(subSystemInfo.getManualOverrideDestination()));
			Cell vsubscell11 = new Cell(SysUtil.ifNull(subSystemInfo.getReadCacheDisable()));
			Cell vsubscell12 = new Cell(SysUtil.ifNull(subSystemInfo.getRebuildPriority()));
			Cell vsubscell13 = new Cell(SysUtil.ifNull(subSystemInfo.getSecurityEnabled()));
			Cell vsubscell14 = new Cell(SysUtil.ifNull(subSystemInfo.getShutdownCompletion()));
			Cell vsubscell15 = new Cell(SysUtil.ifNull(subSystemInfo.getSubsystemTypeID()));
			Cell vsubscell16 = new Cell(SysUtil.ifNull(subSystemInfo.getUnitAttention()));
			Cell vsubscell17 = new Cell(SysUtil.ifNull(subSystemInfo.getVolumeSetPartition()));
			Cell vsubscell18 = new Cell(SysUtil.ifNull(subSystemInfo.getWriteCacheEnable()));			
			Cell vsubscell19 = new Cell(SysUtil.ifNull(subSystemInfo.getWriteWorkingSetInterval()));
			Cell vsubscell20 = new Cell(SysUtil.ifNull(subSystemInfo.getEnablePrefetch()));
			Cell vsubscell21 = new Cell(SysUtil.ifNull(subSystemInfo.getDisableSecondaryPathPresentation()));
			Cell vsubscell22 = new Cell("");
			
			vsubscell1.setHorizontalAlignment(Element.ALIGN_CENTER);
			vsubscell2.setHorizontalAlignment(Element.ALIGN_CENTER);
			vsubscell3.setHorizontalAlignment(Element.ALIGN_CENTER);
			vsubscell4.setHorizontalAlignment(Element.ALIGN_CENTER);
			vsubscell5.setHorizontalAlignment(Element.ALIGN_CENTER);
			vsubscell6.setHorizontalAlignment(Element.ALIGN_CENTER);			
			vsubscell7.setHorizontalAlignment(Element.ALIGN_CENTER);
			vsubscell8.setHorizontalAlignment(Element.ALIGN_CENTER);
			vsubscell9.setHorizontalAlignment(Element.ALIGN_CENTER);
			vsubscell10.setHorizontalAlignment(Element.ALIGN_CENTER);
			vsubscell11.setHorizontalAlignment(Element.ALIGN_CENTER);
			vsubscell12.setHorizontalAlignment(Element.ALIGN_CENTER);
			vsubscell13.setHorizontalAlignment(Element.ALIGN_CENTER);
			vsubscell14.setHorizontalAlignment(Element.ALIGN_CENTER);
			vsubscell15.setHorizontalAlignment(Element.ALIGN_CENTER);			
			vsubscell16.setHorizontalAlignment(Element.ALIGN_CENTER);
			vsubscell17.setHorizontalAlignment(Element.ALIGN_CENTER);
			vsubscell18.setHorizontalAlignment(Element.ALIGN_CENTER);
			vsubscell19.setHorizontalAlignment(Element.ALIGN_CENTER);
			vsubscell20.setHorizontalAlignment(Element.ALIGN_CENTER);
			vsubscell21.setHorizontalAlignment(Element.ALIGN_CENTER);
			vsubscell22.setHorizontalAlignment(Element.ALIGN_CENTER);
			
			substable.addCell(subscell1);
			substable.addCell(vsubscell1);
			substable.addCell(subscell2);
			substable.addCell(vsubscell2);
			substable.addCell(subscell3);
			substable.addCell(vsubscell3);
			substable.addCell(subscell4);
			substable.addCell(vsubscell4);
			substable.addCell(subscell5);
			substable.addCell(vsubscell5);
			substable.addCell(subscell6);
			substable.addCell(vsubscell6);
			substable.addCell(subscell7);
			substable.addCell(vsubscell7);
			substable.addCell(subscell8);
			substable.addCell(vsubscell8);
			substable.addCell(subscell9);
			substable.addCell(vsubscell9);
			substable.addCell(subscell10);
			substable.addCell(vsubscell10);
			substable.addCell(subscell11);
			substable.addCell(vsubscell11);
			substable.addCell(subscell12);
			substable.addCell(vsubscell12);
			substable.addCell(subscell13);
			substable.addCell(vsubscell13);
			substable.addCell(subscell14);
			substable.addCell(vsubscell14);
			substable.addCell(subscell15);
			substable.addCell(vsubscell15);
			substable.addCell(subscell16);
			substable.addCell(vsubscell16);
			substable.addCell(subscell17);
			substable.addCell(vsubscell17);
			substable.addCell(subscell18);
			substable.addCell(vsubscell18);
			substable.addCell(subscell19);
			substable.addCell(vsubscell19);
			substable.addCell(subscell20);
			substable.addCell(vsubscell20);
			substable.addCell(subscell21);
			substable.addCell(vsubscell21);
			substable.addCell(subscell22);
			substable.addCell(vsubscell22);
			document.add(substable);			
		}
		document.close();		
	}
	/*
	 * HP�洢�ۺϱ�������PDF�ĵ�
	 */
	public void createHPstorageCompPdf(String file,String ipaddress) throws DocumentException, IOException {		

		  List<Disk> disks = null;
		  ArrayInfo arrayinfo = null;
		  List<Enclosure> enclosures = null;
		  List<Controller> controllers = null;
		  List<Port> ports = null;
		  List<Lun> luns = null;
		  List<VFP> vfps = null;
		  SubSystemInfo subSystemInfo = null;
		  
		  Hashtable ipAllData = new Hashtable();
		  //ֻȡ��ǰ�ɼ����ۺ���Ϣ
		  ipAllData = (Hashtable) ShareData.getSharedata().get("hpstorage:" + ipaddress);
		  if(ipAllData != null){
		      disks = (List<Disk>)ipAllData.get("disks");
		      arrayinfo = (ArrayInfo)ipAllData.get("arrayinfo");
		      enclosures = (List<Enclosure>)ipAllData.get("enclosures");
		      controllers = (List<Controller>)ipAllData.get("controllers");
		      ports = (List<Port>)ipAllData.get("ports");
		      luns = (List<Lun>)ipAllData.get("luns");
		      vfps = (List<VFP>)ipAllData.get("vfps");
		      subSystemInfo = (SubSystemInfo)ipAllData.get("subSystemInfo");
		  }
		
		Document document = new Document(PageSize.A4);
		// ����һ����д��(Writer)��document���������ͨ����д��(Writer)���Խ��ĵ�д�뵽������
		PdfWriter.getInstance(document, new FileOutputStream(file));
		document.open();
		// ������������
		BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
		// ����������
		Font titleFont = new Font(bfChinese, 12, Font.BOLD);
		// com.lowagie.text.Font titleFont = new
		// com.lowagie.text.Font(bfChinese, 12, com.lowagie.text.Font.NORMAL);
		// ����������
		Font contextFont = new com.lowagie.text.Font(bfChinese, 11, Font.NORMAL);
		Paragraph title = new Paragraph("HP�洢" + ipaddress + "�ۺϱ���", titleFont);
		// ���ñ����ʽ���뷽ʽ
		title.setAlignment(Element.ALIGN_CENTER);
		// title.setFont(titleFont);
		document.add(title);
		document.add(new Paragraph("\n"));
		// ���� Table ���
		Font fontChinese = new Font(bfChinese, 11, Font.NORMAL, Color.black);
		
		if(arrayinfo != null)
		{//������Ϣ	
		PdfPTable arrtable = new PdfPTable(4);		
		int arrwidth[] = {100,100,100,100};
		arrtable.setWidthPercentage(100);
		arrtable.setWidths(arrwidth);	
		
		
		PdfPCell arrtitle = new PdfPCell(new Phrase("������Ϣ", contextFont));
		arrtitle.setBackgroundColor(Color.LIGHT_GRAY);
		arrtitle.setHorizontalAlignment(Element.ALIGN_LEFT);
		arrtitle.setColspan(4);
		
		PdfPCell arrcell1 = new PdfPCell(new Phrase("����״̬:", contextFont));
		PdfPCell arrcell2 = new PdfPCell(new Phrase("�̼��汾:", contextFont));
		PdfPCell arrcell3 = new PdfPCell(new Phrase("��Ʒ�汾:", contextFont));
		PdfPCell arrcell4 = new PdfPCell(new Phrase("���ؿ�������Ʒ�汾:", contextFont));
		PdfPCell arrcell5 = new PdfPCell(new Phrase("Զ�̿�������Ʒ�汾:", contextFont));
		PdfPCell arrcell6 = new PdfPCell(new Phrase("", contextFont));
		PdfPCell arrcell66 = new PdfPCell(new Phrase("", contextFont));
		
		PdfPCell arrcell11 = new PdfPCell(new Phrase(SysUtil.ifNull(arrayinfo.getArrayStatus()),contextFont));
		PdfPCell arrcell22 = new PdfPCell(new Phrase(SysUtil.ifNull(arrayinfo.getFirmwareRevision()),contextFont));
		PdfPCell arrcell33 = new PdfPCell(new Phrase(SysUtil.ifNull(SysUtil.ifNull(arrayinfo.getProductRevision())),contextFont));
		PdfPCell arrcell44 = new PdfPCell(new Phrase(SysUtil.ifNull(SysUtil.ifNull(arrayinfo.getLocalControllerProductRevision())),contextFont));
		PdfPCell arrcell55 = new PdfPCell(new Phrase(SysUtil.ifNull(arrayinfo.getRemoteControllerProductRevision()),contextFont));
		
		arrcell1.setHorizontalAlignment(Element.ALIGN_CENTER);
		arrcell2.setHorizontalAlignment(Element.ALIGN_CENTER);
		arrcell3.setHorizontalAlignment(Element.ALIGN_CENTER);
		arrcell4.setHorizontalAlignment(Element.ALIGN_CENTER);
		arrcell5.setHorizontalAlignment(Element.ALIGN_CENTER);
		
		arrcell11.setHorizontalAlignment(Element.ALIGN_CENTER);
		arrcell22.setHorizontalAlignment(Element.ALIGN_CENTER);
		arrcell33.setHorizontalAlignment(Element.ALIGN_CENTER);
		arrcell44.setHorizontalAlignment(Element.ALIGN_CENTER);
		arrcell55.setHorizontalAlignment(Element.ALIGN_CENTER);
		
		arrtable.addCell(arrtitle);
		arrtable.addCell(arrcell1);
		arrtable.addCell(arrcell11);
		arrtable.addCell(arrcell2);
		arrtable.addCell(arrcell22);
		arrtable.addCell(arrcell3);
		arrtable.addCell(arrcell33);
		arrtable.addCell(arrcell4);
		arrtable.addCell(arrcell44);
		arrtable.addCell(arrcell5);
		arrtable.addCell(arrcell55);
		arrtable.addCell(arrcell6);
		arrtable.addCell(arrcell66);
		document.add(arrtable);		
		}
		
		if(enclosures != null && enclosures.size()>0)
		{//������Ϣ
			for(int i = 0;i<enclosures.size();i++)
			{
				Enclosure encl = enclosures.get(i);				
				
				PdfPTable encltable = new PdfPTable(4);		
				int enclwidth[] = {100,100,100,100};
				encltable.setWidthPercentage(100);
				encltable.setWidths(enclwidth);				
				
				PdfPCell encltitle = new PdfPCell(new Phrase("������Ϣ", contextFont));
				encltitle.setBackgroundColor(Color.LIGHT_GRAY);
				encltitle.setHorizontalAlignment(Element.ALIGN_LEFT);
				encltitle.setColspan(4);
				
				PdfPCell enclcell1 = new PdfPCell(new Phrase("��������:",contextFont));
				PdfPCell enclcell2 = new PdfPCell(new Phrase("�������:",contextFont));
				PdfPCell enclcell3 = new PdfPCell(new Phrase("����״̬:",contextFont));
				PdfPCell enclcell4 = new PdfPCell(new Phrase("��������:",contextFont));
				PdfPCell enclcell5 = new PdfPCell(new Phrase("����ȫ������:",contextFont));
				PdfPCell enclcell6 = new PdfPCell(new Phrase("",contextFont));
				PdfPCell enclcell66 = new PdfPCell(new Phrase("",contextFont));
				
				PdfPCell enclcell11 = new PdfPCell(new Phrase(SysUtil.ifNull(encl.getName()),contextFont));
				PdfPCell enclcell22 = new PdfPCell(new Phrase(SysUtil.ifNull(encl.getEnclosureID()),contextFont));
				PdfPCell enclcell33 = new PdfPCell(new Phrase(SysUtil.ifNull(encl.getEnclosureStatus()),contextFont));
				PdfPCell enclcell44 = new PdfPCell(new Phrase(SysUtil.ifNull(encl.getEnclosureType()),contextFont));
				PdfPCell enclcell55 = new PdfPCell(new Phrase(SysUtil.ifNull(encl.getNodeWWN()),contextFont));
				
				enclcell1.setHorizontalAlignment(Element.ALIGN_CENTER);
				enclcell2.setHorizontalAlignment(Element.ALIGN_CENTER);
				enclcell3.setHorizontalAlignment(Element.ALIGN_CENTER);
				enclcell4.setHorizontalAlignment(Element.ALIGN_CENTER);
				enclcell5.setHorizontalAlignment(Element.ALIGN_CENTER);
				
				enclcell11.setHorizontalAlignment(Element.ALIGN_CENTER);
				enclcell22.setHorizontalAlignment(Element.ALIGN_CENTER);
				enclcell33.setHorizontalAlignment(Element.ALIGN_CENTER);
				enclcell44.setHorizontalAlignment(Element.ALIGN_CENTER);
				enclcell55.setHorizontalAlignment(Element.ALIGN_CENTER);
				
				encltable.addCell(encltitle);
				encltable.addCell(enclcell1);
				encltable.addCell(enclcell11);
				encltable.addCell(enclcell2);
				encltable.addCell(enclcell22);
				encltable.addCell(enclcell3);
				encltable.addCell(enclcell33);
				encltable.addCell(enclcell4);
				encltable.addCell(enclcell44);
				encltable.addCell(enclcell5);
				encltable.addCell(enclcell55);
				encltable.addCell(enclcell6);
				encltable.addCell(enclcell66);
				document.add(encltable);
				if(encl.getFrus() != null && encl.getFrus().size()>0)
				{
					PdfPTable enclfrutable = new PdfPTable(4);		
					int enclfruwidth[] = {100,100,100,100};
					enclfrutable.setWidthPercentage(100);
					enclfrutable.setWidths(enclfruwidth);				
					
					PdfPCell enclfrutitle = new PdfPCell(new Phrase("FRU��Ϣ",contextFont));
					enclfrutitle.setBackgroundColor(Color.LIGHT_GRAY);
					enclfrutitle.setHorizontalAlignment(Element.ALIGN_LEFT);
					enclfrutitle.setColspan(4);
					enclfrutable.addCell(enclfrutitle);
					
					PdfPCell enclfrucell1 = new PdfPCell(new Phrase("����:",contextFont));
					PdfPCell enclfrucell2 = new PdfPCell(new Phrase("���:",contextFont));
					PdfPCell enclfrucell3 = new PdfPCell(new Phrase("��ʶ:",contextFont));
					PdfPCell enclfrucell4 = new PdfPCell(new Phrase("״̬:",contextFont));
					
					enclfrucell1.setHorizontalAlignment(Element.ALIGN_CENTER);
					enclfrucell2.setHorizontalAlignment(Element.ALIGN_CENTER);
					enclfrucell3.setHorizontalAlignment(Element.ALIGN_CENTER);
					enclfrucell4.setHorizontalAlignment(Element.ALIGN_CENTER);
					
					enclfrutable.addCell(enclfrucell1);
					enclfrutable.addCell(enclfrucell2);
					enclfrutable.addCell(enclfrucell3);
					enclfrutable.addCell(enclfrucell4);
					
					 for(int j=0;j<encl.getFrus().size();j++)
			         {
			               EnclosureFru fru = encl.getFrus().get(j);
			               
			               PdfPCell enclfrucell11 = new PdfPCell(new Phrase(SysUtil.ifNull(fru.getFru()),contextFont));
			               PdfPCell enclfrucell22 = new PdfPCell(new Phrase(SysUtil.ifNull(fru.getHwComponent()),contextFont));
			               PdfPCell enclfrucell33 = new PdfPCell(new Phrase(SysUtil.ifNull(fru.getIdentification()),contextFont));
			               PdfPCell enclfrucell44 = new PdfPCell(new Phrase(SysUtil.ifNull(fru.getIdStatus()),contextFont));
						   
						   enclfrucell11.setHorizontalAlignment(Element.ALIGN_CENTER);
						   enclfrucell22.setHorizontalAlignment(Element.ALIGN_CENTER);
						   enclfrucell33.setHorizontalAlignment(Element.ALIGN_CENTER);
						   enclfrucell44.setHorizontalAlignment(Element.ALIGN_CENTER);
						   
						   enclfrutable.addCell(enclfrucell11);
						   enclfrutable.addCell(enclfrucell22);
						   enclfrutable.addCell(enclfrucell33);
						   enclfrutable.addCell(enclfrucell44);        
			        }
					document.add(enclfrutable);
				}
				
			}
			
		}
		
		if(controllers != null)
		{//��������Ϣ
			for(int i = 0;i<controllers.size();i++)
			{
				Controller cont = controllers.get(i);
				
				PdfPTable conttable = new PdfPTable(4);		
				int enclwidth[] = {100,100,100,100};
				conttable.setWidthPercentage(100);
				conttable.setWidths(enclwidth);				
				
				PdfPCell conttitle = new PdfPCell(new Phrase("��������Ϣ",contextFont));
				conttitle.setBackgroundColor(Color.LIGHT_GRAY);
				conttitle.setHorizontalAlignment(Element.ALIGN_LEFT);
				conttitle.setColspan(4);
				
				PdfPCell contcell1 = new PdfPCell(new Phrase("����:",contextFont));
				PdfPCell contcell2 = new PdfPCell(new Phrase("״̬:",contextFont));
				PdfPCell contcell3 = new PdfPCell(new Phrase("���к�:",contextFont));
				PdfPCell contcell4 = new PdfPCell(new Phrase("�����̱��:",contextFont));
				PdfPCell contcell5 = new PdfPCell(new Phrase("��Ʒ���:",contextFont));
				PdfPCell contcell6 = new PdfPCell(new Phrase("��Ʒ�汾:",contextFont));
				PdfPCell contcell7 = new PdfPCell(new Phrase("�̼��汾:",contextFont));
				PdfPCell contcell8 = new PdfPCell(new Phrase("�����̲�Ʒ����:",contextFont));
				PdfPCell contcell9 = new PdfPCell(new Phrase("��������;",contextFont));
				PdfPCell contcell10 = new PdfPCell(new Phrase("����ع̼��汾:",contextFont));
				PdfPCell contcell11 = new PdfPCell(new Phrase("������������:",contextFont));
				PdfPCell contcell12 = new PdfPCell(new Phrase("�������:",contextFont));
				PdfPCell contcell13 = new PdfPCell(new Phrase("������:",contextFont));
				PdfPCell contcell14 = new PdfPCell(new Phrase("�������:",contextFont));
				PdfPCell contcell15 = new PdfPCell(new Phrase("����������ַ:",contextFont));
				PdfPCell contcell16 = new PdfPCell(new Phrase("�ι̵�ַ:",contextFont));
				
				contcell1.setHorizontalAlignment(Element.ALIGN_CENTER);
				contcell2.setHorizontalAlignment(Element.ALIGN_CENTER);
				contcell3.setHorizontalAlignment(Element.ALIGN_CENTER);
				contcell4.setHorizontalAlignment(Element.ALIGN_CENTER);				
				contcell5.setHorizontalAlignment(Element.ALIGN_CENTER);
				contcell6.setHorizontalAlignment(Element.ALIGN_CENTER);
				contcell7.setHorizontalAlignment(Element.ALIGN_CENTER);
				contcell8.setHorizontalAlignment(Element.ALIGN_CENTER);
				contcell9.setHorizontalAlignment(Element.ALIGN_CENTER);
				contcell10.setHorizontalAlignment(Element.ALIGN_CENTER);
				contcell11.setHorizontalAlignment(Element.ALIGN_CENTER);
				contcell12.setHorizontalAlignment(Element.ALIGN_CENTER);
				contcell13.setHorizontalAlignment(Element.ALIGN_CENTER);
				contcell14.setHorizontalAlignment(Element.ALIGN_CENTER);
				contcell15.setHorizontalAlignment(Element.ALIGN_CENTER);
				contcell16.setHorizontalAlignment(Element.ALIGN_CENTER);
				
				PdfPCell vcontcell1 = new PdfPCell(new Phrase(SysUtil.ifNull(cont.getName()),contextFont));
				PdfPCell contcell22 = new PdfPCell(new Phrase(SysUtil.ifNull(cont.getStatus()),contextFont));
				PdfPCell contcell33 = new PdfPCell(new Phrase(SysUtil.ifNull(cont.getSerialNumber()),contextFont));
				PdfPCell contcell44 = new PdfPCell(new Phrase(SysUtil.ifNull(cont.getVendorID()),contextFont));
				PdfPCell contcell55 = new PdfPCell(new Phrase(SysUtil.ifNull(cont.getProductID()),contextFont));
				PdfPCell contcell66 = new PdfPCell(new Phrase(SysUtil.ifNull(cont.getProductRevision()),contextFont));
				PdfPCell contcell77 = new PdfPCell(new Phrase(SysUtil.ifNull(cont.getFirmwareRevision()),contextFont));
				PdfPCell contcell88 = new PdfPCell(new Phrase(SysUtil.ifNull(cont.getManufacturingProductCode()),contextFont));
				PdfPCell contcell99 = new PdfPCell(new Phrase(SysUtil.ifNull(cont.getControllerType()),contextFont));
				PdfPCell contcell100 = new PdfPCell(new Phrase(SysUtil.ifNull(cont.getBatteryChargerFirmwareRevision()),contextFont));
				PdfPCell contcell111 = new PdfPCell(new Phrase(SysUtil.ifNull(cont.getEnclosureSwitchSetting()),contextFont));
				PdfPCell contcell122 = new PdfPCell(new Phrase(SysUtil.ifNull(cont.getEnclosureID()),contextFont));
				PdfPCell contcell133 = new PdfPCell(new Phrase(SysUtil.ifNull(cont.getLoopPair()),contextFont));
				PdfPCell contcell144 = new PdfPCell(new Phrase(SysUtil.ifNull(cont.getLoopID()),contextFont));
				PdfPCell contcell155 = new PdfPCell(new Phrase(SysUtil.ifNull(cont.getDriveAddressBasis()),contextFont));
				PdfPCell contcell166 = new PdfPCell(new Phrase(SysUtil.ifNull(cont.getHardAddress()),contextFont));
				
				vcontcell1.setHorizontalAlignment(Element.ALIGN_CENTER);
				contcell22.setHorizontalAlignment(Element.ALIGN_CENTER);				
				contcell33.setHorizontalAlignment(Element.ALIGN_CENTER);
				contcell44.setHorizontalAlignment(Element.ALIGN_CENTER);
				contcell55.setHorizontalAlignment(Element.ALIGN_CENTER);
				contcell66.setHorizontalAlignment(Element.ALIGN_CENTER);
				contcell77.setHorizontalAlignment(Element.ALIGN_CENTER);				
				contcell88.setHorizontalAlignment(Element.ALIGN_CENTER);
				contcell99.setHorizontalAlignment(Element.ALIGN_CENTER);
				contcell100.setHorizontalAlignment(Element.ALIGN_CENTER);
				contcell111.setHorizontalAlignment(Element.ALIGN_CENTER);
				contcell122.setHorizontalAlignment(Element.ALIGN_CENTER);
				contcell133.setHorizontalAlignment(Element.ALIGN_CENTER);
				contcell144.setHorizontalAlignment(Element.ALIGN_CENTER);
				contcell155.setHorizontalAlignment(Element.ALIGN_CENTER);
				contcell166.setHorizontalAlignment(Element.ALIGN_CENTER);
				
				conttable.addCell(conttitle);
				conttable.addCell(contcell1);
				conttable.addCell(vcontcell1);
				conttable.addCell(contcell2);
				conttable.addCell(contcell22);
				conttable.addCell(contcell3);
				conttable.addCell(contcell33);
				conttable.addCell(contcell4);
				conttable.addCell(contcell44);
				conttable.addCell(contcell5);
				conttable.addCell(contcell55);
				conttable.addCell(contcell6);
				conttable.addCell(contcell66);
				conttable.addCell(contcell7);
				conttable.addCell(contcell77);
				conttable.addCell(contcell8);
				conttable.addCell(contcell88);
				conttable.addCell(contcell9);
				conttable.addCell(contcell99);
				conttable.addCell(contcell10);
				conttable.addCell(contcell100);
				conttable.addCell(contcell11);
				conttable.addCell(contcell111);
				conttable.addCell(contcell12);
				conttable.addCell(contcell122);
				conttable.addCell(contcell13);
				conttable.addCell(contcell133);
				conttable.addCell(contcell14);
				conttable.addCell(contcell144);
				conttable.addCell(contcell15);
				conttable.addCell(contcell155);
				conttable.addCell(contcell16);
				conttable.addCell(contcell166);
				document.add(conttable);
				
				if(cont.getFrontPortList() != null && cont.getFrontPortList().size() > 0)
				{
					PdfPTable fptable = new PdfPTable(4);		
					int fpwidth[] = {100,100,100,100};
					fptable.setWidthPercentage(100);
					fptable.setWidths(fpwidth);
					
					
					PdfPCell fptitle = new PdfPCell(new Phrase("ǰ�˿��б�",contextFont));
					fptitle.setBackgroundColor(Color.LIGHT_GRAY);
					fptitle.setHorizontalAlignment(Element.ALIGN_LEFT);
					fptitle.setColspan(4);
					fptable.addCell(fptitle);
					
					for(int j = 0;j<cont.getFrontPortList().size();j++)
					{
						CtrlPort fctp = cont.getFrontPortList().get(j);				
						PdfPCell fpcell1 = new PdfPCell(new Phrase("����:",contextFont));
						PdfPCell fpcell2 = new PdfPCell(new Phrase("״̬:",contextFont));
						PdfPCell fpcell3 = new PdfPCell(new Phrase("�˿�ʵ��:",contextFont));
						PdfPCell fpcell4 = new PdfPCell(new Phrase("�����ַ:",contextFont));
						PdfPCell fpcell5 = new PdfPCell(new Phrase("��������:",contextFont));
						PdfPCell fpcell6 = new PdfPCell(new Phrase("�ڵ�ȫ������:",contextFont));
						PdfPCell fpcell7 = new PdfPCell(new Phrase("�˿�ȫ������:",contextFont));
						PdfPCell fpcell8 = new PdfPCell(new Phrase("����:",contextFont));
						PdfPCell fpcell9 = new PdfPCell(new Phrase("��������:",contextFont));
						PdfPCell fpcell10 = new PdfPCell(new Phrase("�˿ڱ��:",contextFont));
						PdfPCell fpcell11 = new PdfPCell(new Phrase("�豸����:",contextFont));
						PdfPCell fpcell12 = new PdfPCell(new Phrase("�豸·��:",contextFont));
						
						fpcell1.setHorizontalAlignment(Element.ALIGN_CENTER);
						fpcell2.setHorizontalAlignment(Element.ALIGN_CENTER);
						fpcell3.setHorizontalAlignment(Element.ALIGN_CENTER);
						fpcell4.setHorizontalAlignment(Element.ALIGN_CENTER);
						fpcell5.setHorizontalAlignment(Element.ALIGN_CENTER);
						fpcell6.setHorizontalAlignment(Element.ALIGN_CENTER);
						fpcell7.setHorizontalAlignment(Element.ALIGN_CENTER);
						fpcell8.setHorizontalAlignment(Element.ALIGN_CENTER);
						fpcell9.setHorizontalAlignment(Element.ALIGN_CENTER);
						fpcell10.setHorizontalAlignment(Element.ALIGN_CENTER);
						fpcell11.setHorizontalAlignment(Element.ALIGN_CENTER);
						fpcell12.setHorizontalAlignment(Element.ALIGN_CENTER);
						
						PdfPCell vfpcell1 = new PdfPCell(new Phrase(SysUtil.ifNull(fctp.getName()),contextFont));
						PdfPCell vfpcell2 = new PdfPCell(new Phrase(SysUtil.ifNull(fctp.getStatus()),contextFont));
						PdfPCell vfpcell3 = new PdfPCell(new Phrase(SysUtil.ifNull(fctp.getPortInstance()),contextFont));
						PdfPCell vfpcell4 = new PdfPCell(new Phrase(SysUtil.ifNull(fctp.getHardAddress()),contextFont));
						PdfPCell vfpcell5 = new PdfPCell(new Phrase(SysUtil.ifNull(fctp.getLinkState()),contextFont));
						PdfPCell vfpcell6 = new PdfPCell(new Phrase(SysUtil.ifNull(fctp.getNodeWWN()),contextFont));
						PdfPCell vfpcell7 = new PdfPCell(new Phrase(SysUtil.ifNull(fctp.getPortWWN()),contextFont));
						PdfPCell vfpcell8 = new PdfPCell(new Phrase(SysUtil.ifNull(fctp.getTopology()),contextFont));
						PdfPCell vfpcell9 = new PdfPCell(new Phrase(SysUtil.ifNull(fctp.getDataRate()),contextFont));
						PdfPCell vfpcell10 = new PdfPCell(new Phrase(SysUtil.ifNull(fctp.getPortID()),contextFont));
						PdfPCell vfpcell11 = new PdfPCell(new Phrase(SysUtil.ifNull(fctp.getDeviceHostName()),contextFont));
						PdfPCell vfpcell12 = new PdfPCell(new Phrase(SysUtil.ifNull(fctp.getDevicePath()),contextFont));
						
						vfpcell1.setHorizontalAlignment(Element.ALIGN_CENTER);
						vfpcell2.setHorizontalAlignment(Element.ALIGN_CENTER);
						vfpcell3.setHorizontalAlignment(Element.ALIGN_CENTER);
						vfpcell4.setHorizontalAlignment(Element.ALIGN_CENTER);
						vfpcell5.setHorizontalAlignment(Element.ALIGN_CENTER);
						vfpcell6.setHorizontalAlignment(Element.ALIGN_CENTER);
						vfpcell7.setHorizontalAlignment(Element.ALIGN_CENTER);
						vfpcell8.setHorizontalAlignment(Element.ALIGN_CENTER);
						vfpcell9.setHorizontalAlignment(Element.ALIGN_CENTER);
						vfpcell10.setHorizontalAlignment(Element.ALIGN_CENTER);
						vfpcell11.setHorizontalAlignment(Element.ALIGN_CENTER);
						vfpcell12.setHorizontalAlignment(Element.ALIGN_CENTER);
						
						fptable.addCell(fpcell1);
						fptable.addCell(vfpcell1);
						fptable.addCell(fpcell2);
						fptable.addCell(vfpcell2);
						fptable.addCell(fpcell3);
						fptable.addCell(vfpcell3);
						fptable.addCell(fpcell4);
						fptable.addCell(vfpcell4);
						fptable.addCell(fpcell5);
						fptable.addCell(vfpcell5);
						fptable.addCell(fpcell6);
						fptable.addCell(vfpcell6);
						fptable.addCell(fpcell7);
						fptable.addCell(vfpcell7);
						fptable.addCell(fpcell8);
						fptable.addCell(vfpcell8);
						fptable.addCell(fpcell9);
						fptable.addCell(vfpcell9);
						fptable.addCell(fpcell10);
						fptable.addCell(vfpcell10);
						fptable.addCell(fpcell11);
						fptable.addCell(vfpcell11);
						fptable.addCell(fpcell12);
						fptable.addCell(vfpcell12);						
					}
					document.add(fptable);
				}
				if(cont.getBackPortList() != null && cont.getBackPortList().size() > 0)
				{
					PdfPTable bptable = new PdfPTable(4);		
					int bpwidth[] = {100,100,100,100};
					bptable.setWidthPercentage(100);
					bptable.setWidths(bpwidth);					
					
					PdfPCell bptitle = new PdfPCell(new Phrase("��˿��б�",contextFont));
					bptitle.setBackgroundColor(Color.LIGHT_GRAY);
					bptitle.setHorizontalAlignment(Element.ALIGN_LEFT);
					bptitle.setColspan(4);
					bptable.addCell(bptitle);
					
					for(int j = 0;j<cont.getBackPortList().size();j++)
					{
						CtrlPort bctp = cont.getBackPortList().get(j);				
						PdfPCell fpcell1 = new PdfPCell(new Phrase("����:",contextFont));
						PdfPCell fpcell2 = new PdfPCell(new Phrase("״̬:",contextFont));
						PdfPCell fpcell3 = new PdfPCell(new Phrase("�˿�ʵ��:",contextFont));
						PdfPCell fpcell4 = new PdfPCell(new Phrase("�����ַ:",contextFont));
						PdfPCell fpcell5 = new PdfPCell(new Phrase("��������:",contextFont));
						PdfPCell fpcell6 = new PdfPCell(new Phrase("�ڵ�ȫ������:",contextFont));
						PdfPCell fpcell7 = new PdfPCell(new Phrase("�˿�ȫ������:",contextFont));
						PdfPCell fpcell8 = new PdfPCell(new Phrase("����:",contextFont));
						PdfPCell fpcell9 = new PdfPCell(new Phrase("��������:",contextFont));
						PdfPCell fpcell10 = new PdfPCell(new Phrase("�˿ڱ��:",contextFont));
						PdfPCell fpcell11 = new PdfPCell(new Phrase("�豸����:",contextFont));
						PdfPCell fpcell12 = new PdfPCell(new Phrase("�豸·��:",contextFont));
						
						fpcell1.setHorizontalAlignment(Element.ALIGN_CENTER);
						fpcell2.setHorizontalAlignment(Element.ALIGN_CENTER);
						fpcell3.setHorizontalAlignment(Element.ALIGN_CENTER);
						fpcell4.setHorizontalAlignment(Element.ALIGN_CENTER);
						fpcell5.setHorizontalAlignment(Element.ALIGN_CENTER);
						fpcell6.setHorizontalAlignment(Element.ALIGN_CENTER);
						fpcell7.setHorizontalAlignment(Element.ALIGN_CENTER);
						fpcell8.setHorizontalAlignment(Element.ALIGN_CENTER);
						fpcell9.setHorizontalAlignment(Element.ALIGN_CENTER);
						fpcell10.setHorizontalAlignment(Element.ALIGN_CENTER);
						fpcell11.setHorizontalAlignment(Element.ALIGN_CENTER);
						fpcell12.setHorizontalAlignment(Element.ALIGN_CENTER);
						
						PdfPCell vfpcell1 = new PdfPCell(new Phrase(SysUtil.ifNull(bctp.getName()),contextFont));
						PdfPCell vfpcell2 = new PdfPCell(new Phrase(SysUtil.ifNull(bctp.getStatus()),contextFont));
						PdfPCell vfpcell3 = new PdfPCell(new Phrase(SysUtil.ifNull(bctp.getPortInstance()),contextFont));
						PdfPCell vfpcell4 = new PdfPCell(new Phrase(SysUtil.ifNull(bctp.getHardAddress()),contextFont));
						PdfPCell vfpcell5 = new PdfPCell(new Phrase(SysUtil.ifNull(bctp.getLinkState()),contextFont));
						PdfPCell vfpcell6 = new PdfPCell(new Phrase(SysUtil.ifNull(bctp.getNodeWWN()),contextFont));
						PdfPCell vfpcell7 = new PdfPCell(new Phrase(SysUtil.ifNull(bctp.getPortWWN()),contextFont));
						PdfPCell vfpcell8 = new PdfPCell(new Phrase(SysUtil.ifNull(bctp.getTopology()),contextFont));
						PdfPCell vfpcell9 = new PdfPCell(new Phrase(SysUtil.ifNull(bctp.getDataRate()),contextFont));
						PdfPCell vfpcell10 = new PdfPCell(new Phrase(SysUtil.ifNull(bctp.getPortID()),contextFont));
						PdfPCell vfpcell11 = new PdfPCell(new Phrase(SysUtil.ifNull(bctp.getDeviceHostName()),contextFont));
						PdfPCell vfpcell12 = new PdfPCell(new Phrase(SysUtil.ifNull(bctp.getDevicePath()),contextFont));
						
						vfpcell1.setHorizontalAlignment(Element.ALIGN_CENTER);
						vfpcell2.setHorizontalAlignment(Element.ALIGN_CENTER);
						vfpcell3.setHorizontalAlignment(Element.ALIGN_CENTER);
						vfpcell4.setHorizontalAlignment(Element.ALIGN_CENTER);
						vfpcell5.setHorizontalAlignment(Element.ALIGN_CENTER);
						vfpcell6.setHorizontalAlignment(Element.ALIGN_CENTER);
						vfpcell7.setHorizontalAlignment(Element.ALIGN_CENTER);
						vfpcell8.setHorizontalAlignment(Element.ALIGN_CENTER);
						vfpcell9.setHorizontalAlignment(Element.ALIGN_CENTER);
						vfpcell10.setHorizontalAlignment(Element.ALIGN_CENTER);
						vfpcell11.setHorizontalAlignment(Element.ALIGN_CENTER);
						vfpcell12.setHorizontalAlignment(Element.ALIGN_CENTER);
						
						bptable.addCell(fpcell1);
						bptable.addCell(vfpcell1);
						bptable.addCell(fpcell2);
						bptable.addCell(vfpcell2);
						bptable.addCell(fpcell3);
						bptable.addCell(vfpcell3);
						bptable.addCell(fpcell4);
						bptable.addCell(vfpcell4);
						bptable.addCell(fpcell5);
						bptable.addCell(vfpcell5);
						bptable.addCell(fpcell6);
						bptable.addCell(vfpcell6);
						bptable.addCell(fpcell7);
						bptable.addCell(vfpcell7);
						bptable.addCell(fpcell8);
						bptable.addCell(vfpcell8);
						bptable.addCell(fpcell9);
						bptable.addCell(vfpcell9);
						bptable.addCell(fpcell10);
						bptable.addCell(vfpcell10);
						bptable.addCell(fpcell11);
						bptable.addCell(vfpcell11);
						bptable.addCell(fpcell12);
						bptable.addCell(vfpcell12);						
					}
					document.add(bptable);
				}				
				
				if(cont.getDimmList() != null && cont.getDimmList().size()>0)
				{
					PdfPTable dimtable = new PdfPTable(4);		
					int dimwidth[] = {100,100,100,100};
					dimtable.setWidthPercentage(100);
					dimtable.setWidths(dimwidth);					
					
					PdfPCell dimtitle = new PdfPCell(new Phrase("˫��ֱ��ʽ�洢ģ���б�",contextFont));
					dimtitle.setBackgroundColor(Color.LIGHT_GRAY);
					dimtitle.setHorizontalAlignment(Element.ALIGN_LEFT);
					dimtitle.setColspan(4);
					dimtable.addCell(dimtitle);
					
					for(int j=0;j<cont.getDimmList().size();j++)
		            {
		                DIMM dimm = cont.getDimmList().get(j);
		                PdfPCell dimcell1 = new PdfPCell(new Phrase("����:",contextFont));
		                PdfPCell dimcell2 = new PdfPCell(new Phrase("״̬:",contextFont));
		                PdfPCell dimcell3 = new PdfPCell(new Phrase("����:",contextFont));
		                PdfPCell dimcell4 = new PdfPCell(new Phrase("",contextFont));
		                
		                dimcell1.setHorizontalAlignment(Element.ALIGN_CENTER);
		                dimcell2.setHorizontalAlignment(Element.ALIGN_CENTER);
		                dimcell3.setHorizontalAlignment(Element.ALIGN_CENTER);
		                dimcell4.setHorizontalAlignment(Element.ALIGN_CENTER);
		                
		                PdfPCell vdimcell1 = new PdfPCell(new Phrase(SysUtil.ifNull(dimm.getName()),contextFont));
		                PdfPCell vdimcell2 = new PdfPCell(new Phrase(SysUtil.ifNull(dimm.getStatus()),contextFont));
		                PdfPCell vdimcell3 = new PdfPCell(new Phrase(SysUtil.ifNull(dimm.getCapacity()),contextFont));
		                PdfPCell vdimcell4 = new PdfPCell(new Phrase("",contextFont));
		                
		                vdimcell1.setHorizontalAlignment(Element.ALIGN_CENTER);
		                vdimcell2.setHorizontalAlignment(Element.ALIGN_CENTER);
		                vdimcell3.setHorizontalAlignment(Element.ALIGN_CENTER);
		                vdimcell4.setHorizontalAlignment(Element.ALIGN_CENTER);
		                
		                dimtable.addCell(dimcell1);
		                dimtable.addCell(vdimcell1);
		                dimtable.addCell(dimcell2);
		                dimtable.addCell(vdimcell2);
		                dimtable.addCell(dimcell3);
		                dimtable.addCell(vdimcell3);
		                dimtable.addCell(dimcell4);
		                dimtable.addCell(vdimcell4);    
		            }
					document.add(dimtable);
				}
				if(cont.getBattery() != null)
				{
					Battery battery = cont.getBattery();
					PdfPTable battable = new PdfPTable(4);		
					int batwidth[] = {100,100,100,100};
					battable.setWidthPercentage(100);
					battable.setWidths(batwidth);				
					
					PdfPCell battitle = new PdfPCell(new Phrase("�����Ϣ",contextFont));
					battitle.setBackgroundColor(Color.LIGHT_GRAY);
					battitle.setHorizontalAlignment(Element.ALIGN_LEFT);
					battitle.setColspan(4);
					battable.addCell(battitle);
					
					PdfPCell batcell1 = new PdfPCell(new Phrase("����:",contextFont));
					PdfPCell batcell2 = new PdfPCell(new Phrase("״̬:",contextFont));
					PdfPCell batcell3 = new PdfPCell(new Phrase("��ʶ:",contextFont));
					PdfPCell batcell4 = new PdfPCell(new Phrase("����:",contextFont));
					PdfPCell batcell5 = new PdfPCell(new Phrase("�豸����:",contextFont));
					PdfPCell batcell6 = new PdfPCell(new Phrase("��������:",contextFont));
					PdfPCell batcell7 = new PdfPCell(new Phrase("ʣ�����:",contextFont));
					PdfPCell batcell8 = new PdfPCell(new Phrase("ʣ������ٷֱ�:",contextFont));
					PdfPCell batcell9 = new PdfPCell(new Phrase("��ѹ:",contextFont));
					PdfPCell batcell10 = new PdfPCell(new Phrase("�ŵ�����:",contextFont));
	                
	                batcell1.setHorizontalAlignment(Element.ALIGN_CENTER);
	                batcell2.setHorizontalAlignment(Element.ALIGN_CENTER);
	                batcell3.setHorizontalAlignment(Element.ALIGN_CENTER);
	                batcell4.setHorizontalAlignment(Element.ALIGN_CENTER);
	                batcell5.setHorizontalAlignment(Element.ALIGN_CENTER);
	                batcell6.setHorizontalAlignment(Element.ALIGN_CENTER);
	                batcell7.setHorizontalAlignment(Element.ALIGN_CENTER);
	                batcell8.setHorizontalAlignment(Element.ALIGN_CENTER);
	                batcell9.setHorizontalAlignment(Element.ALIGN_CENTER);
	                batcell10.setHorizontalAlignment(Element.ALIGN_CENTER);
	                
	                PdfPCell vbatcell1 = new PdfPCell(new Phrase(SysUtil.ifNull(battery.getName()),contextFont));
	                PdfPCell vbatcell2 = new PdfPCell(new Phrase(SysUtil.ifNull(battery.getStatus()),contextFont));
	                PdfPCell vbatcell3 = new PdfPCell(new Phrase(SysUtil.ifNull(battery.getIdentification()),contextFont));
	                PdfPCell vbatcell4 = new PdfPCell(new Phrase(SysUtil.ifNull(battery.getManufacturerName()),contextFont));
	                PdfPCell vbatcell5 = new PdfPCell(new Phrase(SysUtil.ifNull(battery.getDeviceName()),contextFont));
	                PdfPCell vbatcell6 = new PdfPCell(new Phrase(SysUtil.ifNull(battery.getManufacturerDate()),contextFont));
	                PdfPCell vbatcell7 = new PdfPCell(new Phrase(SysUtil.ifNull(battery.getRemainingCapacity()),contextFont));
	                PdfPCell vbatcell8 = new PdfPCell(new Phrase(SysUtil.ifNull(battery.getPctRemainingCapacity()),contextFont));
	                PdfPCell vbatcell9 = new PdfPCell(new Phrase(SysUtil.ifNull(battery.getVoltage()),contextFont));
	                PdfPCell vbatcell10 = new PdfPCell(new Phrase(SysUtil.ifNull(battery.getDischargeCycles()),contextFont));
	                
	                vbatcell1.setHorizontalAlignment(Element.ALIGN_CENTER);
	                vbatcell2.setHorizontalAlignment(Element.ALIGN_CENTER);
	                vbatcell3.setHorizontalAlignment(Element.ALIGN_CENTER);
	                vbatcell4.setHorizontalAlignment(Element.ALIGN_CENTER);
	                vbatcell5.setHorizontalAlignment(Element.ALIGN_CENTER);
	                vbatcell6.setHorizontalAlignment(Element.ALIGN_CENTER);
	                vbatcell7.setHorizontalAlignment(Element.ALIGN_CENTER);
	                vbatcell8.setHorizontalAlignment(Element.ALIGN_CENTER);
	                vbatcell9.setHorizontalAlignment(Element.ALIGN_CENTER);
	                vbatcell10.setHorizontalAlignment(Element.ALIGN_CENTER);
	                
	                battable.addCell(batcell1);
	                battable.addCell(vbatcell1);
	                battable.addCell(batcell2);
	                battable.addCell(vbatcell2);
	                battable.addCell(batcell3);
	                battable.addCell(vbatcell3);
	                battable.addCell(batcell4);
	                battable.addCell(vbatcell4);
	                battable.addCell(batcell5);
	                battable.addCell(vbatcell5);
	                battable.addCell(batcell6);
	                battable.addCell(vbatcell6);
	                battable.addCell(batcell7);
	                battable.addCell(vbatcell7);
	                battable.addCell(batcell8);
	                battable.addCell(vbatcell8);
	                battable.addCell(batcell9);
	                battable.addCell(vbatcell9);
	                battable.addCell(batcell10);
	                battable.addCell(vbatcell10);
	                
	                document.add(battable);	                
				}
				if(cont.getProcessor() != null)
	            {
					Processor processor = cont.getProcessor();
					PdfPTable protable = new PdfPTable(4);		
					int prowidth[] = {100,100,100,100};
					protable.setWidthPercentage(100);
					protable.setWidths(prowidth);										
					
					PdfPCell protitle = new PdfPCell(new Phrase("��������Ϣ",contextFont));
					protitle.setBackgroundColor(Color.LIGHT_GRAY);
					protitle.setHorizontalAlignment(Element.ALIGN_LEFT);
					protitle.setColspan(4);
					protable.addCell(protitle);
					
					PdfPCell procell1 = new PdfPCell(new Phrase("����:",contextFont));
					PdfPCell procell2 = new PdfPCell(new Phrase("״̬:",contextFont));
					PdfPCell procell3 = new PdfPCell(new Phrase("��ʶ:",contextFont));
					PdfPCell procell4 = new PdfPCell(new Phrase("",contextFont));
	                
	                procell1.setHorizontalAlignment(Element.ALIGN_CENTER);
	                procell2.setHorizontalAlignment(Element.ALIGN_CENTER);
	                procell3.setHorizontalAlignment(Element.ALIGN_CENTER);
	                procell4.setHorizontalAlignment(Element.ALIGN_CENTER);
	                
	                PdfPCell vprocell1 = new PdfPCell(new Phrase(SysUtil.ifNull(processor.getName()),contextFont));
	                PdfPCell vprocell2 = new PdfPCell(new Phrase(SysUtil.ifNull(processor.getStatus()),contextFont));
	                PdfPCell vprocell3 = new PdfPCell(new Phrase(SysUtil.ifNull(processor.getIdentification()),contextFont));
	                PdfPCell vprocell4 = new PdfPCell(new Phrase("",contextFont));
	                
	                vprocell1.setHorizontalAlignment(Element.ALIGN_CENTER);
	                vprocell2.setHorizontalAlignment(Element.ALIGN_CENTER);
	                vprocell3.setHorizontalAlignment(Element.ALIGN_CENTER);
	                vprocell4.setHorizontalAlignment(Element.ALIGN_CENTER);
	                
	                protable.addCell(procell1);
	                protable.addCell(vprocell1);
	                protable.addCell(procell2);	                
	                protable.addCell(vprocell2);
	                protable.addCell(procell3);	                
	                protable.addCell(vprocell3);
	                protable.addCell(procell4);
	                protable.addCell(vprocell4);    
	                document.add(protable);
	            }
				
			}
		}
		
		if(ports != null && ports.size()>0)
		{
			PdfPTable portTable = new PdfPTable(6);
			int portwidth[] = {50, 50, 50, 100, 50, 150};
			portTable.setWidthPercentage(100);
			portTable.setWidths(portwidth);			
			
			PdfPCell porttitle = new PdfPCell(new Phrase("�˿���Ϣ",contextFont));
			porttitle.setBackgroundColor(Color.LIGHT_GRAY);
			porttitle.setHorizontalAlignment(Element.ALIGN_LEFT);
			porttitle.setColspan(6);
			portTable.addCell(porttitle);
			
			PdfPCell portcell1 = new PdfPCell(new Phrase("����",contextFont));
			PdfPCell portcell2 = new PdfPCell(new Phrase("���",contextFont));
			PdfPCell portcell3 = new PdfPCell(new Phrase("��Ϊ",contextFont));
			PdfPCell portcell4 = new PdfPCell(new Phrase("�˿�����",contextFont));
			PdfPCell portcell5 = new PdfPCell(new Phrase("�������ֵ",contextFont));		
			PdfPCell portcell6 = new PdfPCell(new Phrase("���ݴ�������",contextFont));			
			
			portcell1.setHorizontalAlignment(Element.ALIGN_CENTER);
			portcell2.setHorizontalAlignment(Element.ALIGN_CENTER);
			portcell3.setHorizontalAlignment(Element.ALIGN_CENTER);
			portcell4.setHorizontalAlignment(Element.ALIGN_CENTER);
			portcell5.setHorizontalAlignment(Element.ALIGN_CENTER);
			portcell6.setHorizontalAlignment(Element.ALIGN_CENTER);			

			portcell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
			portcell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
			portcell3.setVerticalAlignment(Element.ALIGN_MIDDLE);
			portcell4.setVerticalAlignment(Element.ALIGN_MIDDLE);
			portcell5.setVerticalAlignment(Element.ALIGN_MIDDLE);
			portcell6.setVerticalAlignment(Element.ALIGN_MIDDLE);			

			portcell1.setBackgroundColor(Color.LIGHT_GRAY);
			portcell2.setBackgroundColor(Color.LIGHT_GRAY);
			portcell3.setBackgroundColor(Color.LIGHT_GRAY);
			portcell4.setBackgroundColor(Color.LIGHT_GRAY);
			portcell5.setBackgroundColor(Color.LIGHT_GRAY);
			portcell6.setBackgroundColor(Color.LIGHT_GRAY);			
			
			portTable.addCell(portcell1);
			portTable.addCell(portcell2);
			portTable.addCell(portcell3);
			portTable.addCell(portcell4);
			portTable.addCell(portcell5);
			portTable.addCell(portcell6);			
			
				for (int i = 0; i < ports.size(); i++) {
					Port port = (Port) ports.get(i);
					
					PdfPCell cell6 = new PdfPCell(new Phrase(SysUtil.ifNull(port.getName()),contextFont));
					PdfPCell cell7 = new PdfPCell(new Phrase(SysUtil.ifNull(port.getPortID()),contextFont));
					PdfPCell cell8 = new PdfPCell(new Phrase(SysUtil.ifNull(port.getBehavior()),contextFont));
					PdfPCell cell9 = new PdfPCell(new Phrase(SysUtil.ifNull(port.getTopology()),contextFont));
					PdfPCell cell10 = new PdfPCell(new Phrase(SysUtil.ifNull(port.getQueueFullThreshold()),contextFont));
					PdfPCell cell15 = new PdfPCell(new Phrase(SysUtil.ifNull(port.getDataRate()),contextFont));					
					
					cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell7.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell8.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell9.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell10.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell15.setHorizontalAlignment(Element.ALIGN_CENTER);				
					
					portTable.addCell(cell6);
					portTable.addCell(cell7);
					portTable.addCell(cell8);
					portTable.addCell(cell9);
					portTable.addCell(cell10);
					portTable.addCell(cell15);
				}			
			document.add(portTable);
		}
		
		if(disks != null && disks.size() > 0)
		{
		PdfPTable aTable = new PdfPTable(7);
		int width[] = {50, 50, 50, 130, 30, 50,50};
		aTable.setWidthPercentage(100);
		aTable.setWidths(width);		
		
		PdfPCell atitle = new PdfPCell(new Phrase("������Ϣ", contextFont));
		atitle.setBackgroundColor(Color.LIGHT_GRAY);
		atitle.setHorizontalAlignment(Element.ALIGN_LEFT);
		atitle.setColspan(7);
		aTable.addCell(atitle);
		
		PdfPCell cell1 = new PdfPCell(new Phrase("����", contextFont));
		PdfPCell cell11 = new PdfPCell(new Phrase("״̬", contextFont));
		PdfPCell cell2 = new PdfPCell(new Phrase("����", contextFont));
		PdfPCell cell3 = new PdfPCell(new Phrase("����", contextFont));
		PdfPCell cell4 = new PdfPCell(new Phrase("������", contextFont));
		PdfPCell cell15 = new PdfPCell(new Phrase("��ʼ������", contextFont));
		PdfPCell cell16 = new PdfPCell(new Phrase("�̼��汾", contextFont));
		
		cell1.setBackgroundColor(Color.LIGHT_GRAY);
		cell11.setBackgroundColor(Color.LIGHT_GRAY);
		cell2.setBackgroundColor(Color.LIGHT_GRAY);
		cell3.setBackgroundColor(Color.LIGHT_GRAY);
		cell4.setBackgroundColor(Color.LIGHT_GRAY);
		cell15.setBackgroundColor(Color.LIGHT_GRAY);
		cell16.setBackgroundColor(Color.LIGHT_GRAY);
		
		cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell11.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell15.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell16.setHorizontalAlignment(Element.ALIGN_CENTER);
		
		cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell11.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell4.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell15.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell16.setVerticalAlignment(Element.ALIGN_MIDDLE);
		
		aTable.addCell(cell1);
		aTable.addCell(cell11);
		aTable.addCell(cell2);
		aTable.addCell(cell3);
		aTable.addCell(cell4);
		aTable.addCell(cell15);
		aTable.addCell(cell16);		
		
			for (int i = 0; i < disks.size(); i++) {
				Disk disk = (Disk) disks.get(i);			
				
				PdfPCell cell6 = new PdfPCell(new Phrase(SysUtil.ifNull(disk.getName()),contextFont));
				PdfPCell cell7 = new PdfPCell(new Paragraph(SysUtil.ifNull(disk.getStatus()), contextFont));
				PdfPCell cell8 = new PdfPCell(new Phrase(SysUtil.ifNull(disk.getVendorID()), contextFont));
				PdfPCell cell9 = new PdfPCell(new Phrase(SysUtil.ifNull(disk.getDataCapacity()),contextFont));
				PdfPCell cell10 = new PdfPCell(new Phrase(SysUtil.ifNull(disk.getRedundancyGroup()),contextFont));
				PdfPCell cell17 = new PdfPCell(new Phrase(SysUtil.ifNull(disk.getInitializeState()),contextFont));
				PdfPCell cell18 = new PdfPCell(new Phrase(SysUtil.ifNull(disk.getFirmwareRevision()),contextFont));
				
				cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell7.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell8.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell9.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell10.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell17.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell18.setHorizontalAlignment(Element.ALIGN_CENTER);
				
				aTable.addCell(cell6);
				aTable.addCell(cell7);
				aTable.addCell(cell8);
				aTable.addCell(cell9);
				aTable.addCell(cell10);
				aTable.addCell(cell17);
				aTable.addCell(cell18);
			}				
		document.add(aTable);
		}
		if(luns != null && luns.size()>0)
		{
			PdfPTable lunTable = new PdfPTable(5);
			int lunwidth[] = {50, 50, 50, 100, 200};
			lunTable.setWidthPercentage(100);
			lunTable.setWidths(lunwidth);				
			
			PdfPCell luntitle = new PdfPCell(new Phrase("�߼���Ԫ��Ϣ",contextFont));
			luntitle.setBackgroundColor(Color.LIGHT_GRAY);
			luntitle.setHorizontalAlignment(Element.ALIGN_LEFT);
			luntitle.setColspan(5);
			lunTable.addCell(luntitle);
			
			PdfPCell luncell1 = new PdfPCell(new Phrase("����",contextFont));
			PdfPCell luncell2 = new PdfPCell(new Phrase("���",contextFont));
			PdfPCell luncell3 = new PdfPCell(new Phrase("��Ϊ",contextFont));
			PdfPCell luncell4 = new PdfPCell(new Phrase("�˿�����",contextFont));
			PdfPCell luncell5 = new PdfPCell(new Phrase("�������ֵ",contextFont));						
			
			luncell1.setHorizontalAlignment(Element.ALIGN_CENTER);
			luncell2.setHorizontalAlignment(Element.ALIGN_CENTER);
			luncell3.setHorizontalAlignment(Element.ALIGN_CENTER);
			luncell4.setHorizontalAlignment(Element.ALIGN_CENTER);
			luncell5.setHorizontalAlignment(Element.ALIGN_CENTER);						

			luncell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
			luncell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
			luncell3.setVerticalAlignment(Element.ALIGN_MIDDLE);
			luncell4.setVerticalAlignment(Element.ALIGN_MIDDLE);
			luncell5.setVerticalAlignment(Element.ALIGN_MIDDLE);						

			luncell1.setBackgroundColor(Color.LIGHT_GRAY);
			luncell2.setBackgroundColor(Color.LIGHT_GRAY);
			luncell3.setBackgroundColor(Color.LIGHT_GRAY);
			luncell4.setBackgroundColor(Color.LIGHT_GRAY);
			luncell5.setBackgroundColor(Color.LIGHT_GRAY);					
			
			lunTable.addCell(luncell1);
			lunTable.addCell(luncell2);
			lunTable.addCell(luncell3);
			lunTable.addCell(luncell4);
			lunTable.addCell(luncell5);		
				for (int i = 0; i < luns.size(); i++) {
					Lun lun = (Lun) luns.get(i);
					
					PdfPCell cell6 = new PdfPCell(new Phrase(SysUtil.ifNull(lun.getName()),contextFont));
					PdfPCell cell7 = new PdfPCell(new Phrase(SysUtil.ifNull(lun.getRedundancyGroup()),contextFont));
					PdfPCell cell8 = new PdfPCell(new Phrase(SysUtil.ifNull(lun.getActive()),contextFont));
					PdfPCell cell9 = new PdfPCell(new Phrase(SysUtil.ifNull(lun.getDataCapacity()),contextFont));
					PdfPCell cell10 = new PdfPCell(new Phrase(SysUtil.ifNull(lun.getWwn()),contextFont));										
					
					cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell7.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell8.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell9.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell10.setHorizontalAlignment(Element.ALIGN_CENTER);									
					
					lunTable.addCell(cell6);
					lunTable.addCell(cell7);
					lunTable.addCell(cell8);
					lunTable.addCell(cell9);
					lunTable.addCell(cell10);
					
				}			
			document.add(lunTable);
		}
		if(vfps != null && vfps.size()>0)
		{
			PdfPTable vfpTable = new PdfPTable(3);
			int vfpwidth[] = {50, 50, 50};
			vfpTable.setWidthPercentage(100);
			vfpTable.setWidths(vfpwidth);			
			
			PdfPCell vfptitle = new PdfPCell(new Phrase("VFP��Ϣ",contextFont));
			vfptitle.setBackgroundColor(Color.LIGHT_GRAY);
			vfptitle.setHorizontalAlignment(Element.ALIGN_LEFT);
			vfptitle.setColspan(3);
			vfpTable.addCell(vfptitle);
			
			PdfPCell vfpcell1 = new PdfPCell(new Phrase("����",contextFont));
			PdfPCell vfpcell2 = new PdfPCell(new Phrase("������",contextFont));
			PdfPCell vfpcell3 = new PdfPCell(new Phrase("��ҳֵ",contextFont));									
			
			vfpcell1.setHorizontalAlignment(Element.ALIGN_CENTER);
			vfpcell2.setHorizontalAlignment(Element.ALIGN_CENTER);
			vfpcell3.setHorizontalAlignment(Element.ALIGN_CENTER);								

			vfpcell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
			vfpcell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
			vfpcell3.setVerticalAlignment(Element.ALIGN_MIDDLE);								

			vfpcell1.setBackgroundColor(Color.LIGHT_GRAY);
			vfpcell2.setBackgroundColor(Color.LIGHT_GRAY);
			vfpcell3.setBackgroundColor(Color.LIGHT_GRAY);								
			
			vfpTable.addCell(vfpcell1);
			vfpTable.addCell(vfpcell2);
			vfpTable.addCell(vfpcell3);				
				for (int i = 0; i < vfps.size(); i++) {
					VFP vfp = (VFP) vfps.get(i);
					
					PdfPCell cell6 = new PdfPCell(new Phrase(SysUtil.ifNull(vfp.getName()),contextFont));
					PdfPCell cell7 = new PdfPCell(new Phrase(SysUtil.ifNull(vfp.getVFPBaudRate()),contextFont));
					PdfPCell cell8 = new PdfPCell(new Phrase(SysUtil.ifNull(vfp.getVFPPagingValue()),contextFont));															
					
					cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell7.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell8.setHorizontalAlignment(Element.ALIGN_CENTER);													
					
					vfpTable.addCell(cell6);
					vfpTable.addCell(cell7);
					vfpTable.addCell(cell8);				
				}			
			document.add(vfpTable);
		}
		if(subSystemInfo != null)
		{			
			PdfPTable substable = new PdfPTable(4);		
			int subswidth[] = {100,100,100,100};
			substable.setWidthPercentage(100);
			substable.setWidths(subswidth);			
			
			PdfPCell substitle = new PdfPCell(new Phrase("��ϵͳ��Ϣ",contextFont));
			substitle.setBackgroundColor(Color.LIGHT_GRAY);
			substitle.setHorizontalAlignment(Element.ALIGN_LEFT);
			substitle.setColspan(4);
			substable.addCell(substitle);
			
			PdfPCell subscell1 = new PdfPCell(new Phrase("RAIDģʽ:",contextFont));
			PdfPCell subscell2 = new PdfPCell(new Phrase("�Զ���ʽ������:",contextFont));
			PdfPCell subscell3 = new PdfPCell(new Phrase("��ֱ���:",contextFont));
			PdfPCell subscell4 = new PdfPCell(new Phrase("��������ٽ�ֵ:",contextFont));
			PdfPCell subscell5 = new PdfPCell(new Phrase("��ֱ���:",contextFont));
			PdfPCell subscell6 = new PdfPCell(new Phrase("��������ٽ�ֵ:",contextFont));
			PdfPCell subscell7 = new PdfPCell(new Phrase("�������ٽ�ֵ:",contextFont));
			PdfPCell subscell8 = new PdfPCell(new Phrase("�Ż�����:",contextFont));
			PdfPCell subscell9 = new PdfPCell(new Phrase("�ֶ�����:",contextFont));
			PdfPCell subscell10 = new PdfPCell(new Phrase("�ֶ�����Ŀ��:",contextFont));
			PdfPCell subscell11 = new PdfPCell(new Phrase("���治�ܶ�:",contextFont));
			PdfPCell subscell12 = new PdfPCell(new Phrase("�ؽ����ȼ�:",contextFont));
			PdfPCell subscell13 = new PdfPCell(new Phrase("�Ƿ񼤻ȫ����:",contextFont));
			PdfPCell subscell14 = new PdfPCell(new Phrase("�ػ������:",contextFont));
			PdfPCell subscell15 = new PdfPCell(new Phrase("��ϵͳ���ͱ��:",contextFont));
			PdfPCell subscell16 = new PdfPCell(new Phrase("�Ƿ���������ע:",contextFont));
			PdfPCell subscell17 = new PdfPCell(new Phrase("�Ƿ����л���:",contextFont));
			PdfPCell subscell18 = new PdfPCell(new Phrase("���治��д:",contextFont));			
			PdfPCell subscell19 = new PdfPCell(new Phrase("д���:",contextFont));
			PdfPCell subscell20 = new PdfPCell(new Phrase("�Ƿ���Ԥȡ:",contextFont));
			PdfPCell subscell21 = new PdfPCell(new Phrase("�رյڶ�·������:",contextFont));
			PdfPCell subscell22 = new PdfPCell(new Phrase("",contextFont));		
			
			subscell1.setHorizontalAlignment(Element.ALIGN_CENTER);
			subscell2.setHorizontalAlignment(Element.ALIGN_CENTER);
			subscell3.setHorizontalAlignment(Element.ALIGN_CENTER);
			subscell4.setHorizontalAlignment(Element.ALIGN_CENTER);
			subscell5.setHorizontalAlignment(Element.ALIGN_CENTER);
			subscell6.setHorizontalAlignment(Element.ALIGN_CENTER);			
			subscell7.setHorizontalAlignment(Element.ALIGN_CENTER);
			subscell8.setHorizontalAlignment(Element.ALIGN_CENTER);
			subscell9.setHorizontalAlignment(Element.ALIGN_CENTER);
			subscell10.setHorizontalAlignment(Element.ALIGN_CENTER);
			subscell11.setHorizontalAlignment(Element.ALIGN_CENTER);
			subscell12.setHorizontalAlignment(Element.ALIGN_CENTER);
			subscell13.setHorizontalAlignment(Element.ALIGN_CENTER);
			subscell14.setHorizontalAlignment(Element.ALIGN_CENTER);
			subscell15.setHorizontalAlignment(Element.ALIGN_CENTER);			
			subscell16.setHorizontalAlignment(Element.ALIGN_CENTER);
			subscell17.setHorizontalAlignment(Element.ALIGN_CENTER);
			subscell18.setHorizontalAlignment(Element.ALIGN_CENTER);
			subscell9.setHorizontalAlignment(Element.ALIGN_CENTER);
			subscell20.setHorizontalAlignment(Element.ALIGN_CENTER);
			subscell21.setHorizontalAlignment(Element.ALIGN_CENTER);
			subscell22.setHorizontalAlignment(Element.ALIGN_CENTER);
			
			PdfPCell vsubscell1 = new PdfPCell(new Phrase(SysUtil.ifNull(subSystemInfo.getRaidLevel()),contextFont));
			PdfPCell vsubscell2 = new PdfPCell(new Phrase(SysUtil.ifNull(subSystemInfo.getAutoFormatDrive()),contextFont));
			PdfPCell vsubscell3 = new PdfPCell(new Phrase(SysUtil.ifNull(subSystemInfo.getHangDetection()),contextFont));
			PdfPCell vsubscell4 = new PdfPCell(new Phrase(SysUtil.ifNull(subSystemInfo.getCapacityDepletionThreshold()),contextFont));
			PdfPCell vsubscell5 = new PdfPCell(new Phrase(SysUtil.ifNull(subSystemInfo.getHangDetection()),contextFont));
			PdfPCell vsubscell6 = new PdfPCell(new Phrase(SysUtil.ifNull(subSystemInfo.getCapacityDepletionThreshold()),contextFont));
			PdfPCell vsubscell7 = new PdfPCell(new Phrase(SysUtil.ifNull(subSystemInfo.getQueueFullThresholdMaximum()),contextFont));
			PdfPCell vsubscell8 = new PdfPCell(new Phrase(SysUtil.ifNull(subSystemInfo.getEnableOptimizePolicy()),contextFont));
			PdfPCell vsubscell9 = new PdfPCell(new Phrase(SysUtil.ifNull(subSystemInfo.getEnableManualOverride()),contextFont));
			PdfPCell vsubscell10 = new PdfPCell(new Phrase(SysUtil.ifNull(subSystemInfo.getManualOverrideDestination()),contextFont));
			PdfPCell vsubscell11 = new PdfPCell(new Phrase(SysUtil.ifNull(subSystemInfo.getReadCacheDisable()),contextFont));
			PdfPCell vsubscell12 = new PdfPCell(new Phrase(SysUtil.ifNull(subSystemInfo.getRebuildPriority()),contextFont));
			PdfPCell vsubscell13 = new PdfPCell(new Phrase(SysUtil.ifNull(subSystemInfo.getSecurityEnabled()),contextFont));
			PdfPCell vsubscell14 = new PdfPCell(new Phrase(SysUtil.ifNull(subSystemInfo.getShutdownCompletion()),contextFont));
			PdfPCell vsubscell15 = new PdfPCell(new Phrase(SysUtil.ifNull(subSystemInfo.getSubsystemTypeID()),contextFont));
			PdfPCell vsubscell16 = new PdfPCell(new Phrase(SysUtil.ifNull(subSystemInfo.getUnitAttention()),contextFont));
			PdfPCell vsubscell17 = new PdfPCell(new Phrase(SysUtil.ifNull(subSystemInfo.getVolumeSetPartition()),contextFont));
			PdfPCell vsubscell18 = new PdfPCell(new Phrase(SysUtil.ifNull(subSystemInfo.getWriteCacheEnable()),contextFont));			
			PdfPCell vsubscell19 = new PdfPCell(new Phrase(SysUtil.ifNull(subSystemInfo.getWriteWorkingSetInterval()),contextFont));
			PdfPCell vsubscell20 = new PdfPCell(new Phrase(SysUtil.ifNull(subSystemInfo.getEnablePrefetch()),contextFont));
			PdfPCell vsubscell21 = new PdfPCell(new Phrase(SysUtil.ifNull(subSystemInfo.getDisableSecondaryPathPresentation()),contextFont));
			PdfPCell vsubscell22 = new PdfPCell(new Phrase("",contextFont));
			
			vsubscell1.setHorizontalAlignment(Element.ALIGN_CENTER);
			vsubscell2.setHorizontalAlignment(Element.ALIGN_CENTER);
			vsubscell3.setHorizontalAlignment(Element.ALIGN_CENTER);
			vsubscell4.setHorizontalAlignment(Element.ALIGN_CENTER);
			vsubscell5.setHorizontalAlignment(Element.ALIGN_CENTER);
			vsubscell6.setHorizontalAlignment(Element.ALIGN_CENTER);			
			vsubscell7.setHorizontalAlignment(Element.ALIGN_CENTER);
			vsubscell8.setHorizontalAlignment(Element.ALIGN_CENTER);
			vsubscell9.setHorizontalAlignment(Element.ALIGN_CENTER);
			vsubscell10.setHorizontalAlignment(Element.ALIGN_CENTER);
			vsubscell11.setHorizontalAlignment(Element.ALIGN_CENTER);
			vsubscell12.setHorizontalAlignment(Element.ALIGN_CENTER);
			vsubscell13.setHorizontalAlignment(Element.ALIGN_CENTER);
			vsubscell14.setHorizontalAlignment(Element.ALIGN_CENTER);
			vsubscell15.setHorizontalAlignment(Element.ALIGN_CENTER);			
			vsubscell16.setHorizontalAlignment(Element.ALIGN_CENTER);
			vsubscell17.setHorizontalAlignment(Element.ALIGN_CENTER);
			vsubscell18.setHorizontalAlignment(Element.ALIGN_CENTER);
			vsubscell19.setHorizontalAlignment(Element.ALIGN_CENTER);
			vsubscell20.setHorizontalAlignment(Element.ALIGN_CENTER);
			vsubscell21.setHorizontalAlignment(Element.ALIGN_CENTER);
			vsubscell22.setHorizontalAlignment(Element.ALIGN_CENTER);
			
			substable.addCell(subscell1);
			substable.addCell(vsubscell1);
			substable.addCell(subscell2);
			substable.addCell(vsubscell2);
			substable.addCell(subscell3);
			substable.addCell(vsubscell3);
			substable.addCell(subscell4);
			substable.addCell(vsubscell4);
			substable.addCell(subscell5);
			substable.addCell(vsubscell5);
			substable.addCell(subscell6);
			substable.addCell(vsubscell6);
			substable.addCell(subscell7);
			substable.addCell(vsubscell7);
			substable.addCell(subscell8);
			substable.addCell(vsubscell8);
			substable.addCell(subscell9);
			substable.addCell(vsubscell9);
			substable.addCell(subscell10);
			substable.addCell(vsubscell10);
			substable.addCell(subscell11);
			substable.addCell(vsubscell11);
			substable.addCell(subscell12);
			substable.addCell(vsubscell12);
			substable.addCell(subscell13);
			substable.addCell(vsubscell13);
			substable.addCell(subscell14);
			substable.addCell(vsubscell14);
			substable.addCell(subscell15);
			substable.addCell(vsubscell15);
			substable.addCell(subscell16);
			substable.addCell(vsubscell16);
			substable.addCell(subscell17);
			substable.addCell(vsubscell17);
			substable.addCell(subscell18);
			substable.addCell(vsubscell18);
			substable.addCell(subscell19);
			substable.addCell(vsubscell19);
			substable.addCell(subscell20);
			substable.addCell(vsubscell20);
			substable.addCell(subscell21);
			substable.addCell(vsubscell21);
			substable.addCell(subscell22);
			substable.addCell(vsubscell22);
			document.add(substable);			
		}
		document.close();
	}
	/*
	 * HP�洢��������DOC�ĵ�
	 */
	public void creatHPstorageDiskDoc(String file,String ipaddress) throws DocumentException, IOException {
		
		 List<Disk> disks = null;
		 Hashtable ipAllData = new Hashtable();
		 //ֻȡ��ǰ�ɼ��Ĵ�����Ϣ
		 ipAllData = (Hashtable) ShareData.getSharedata().get("hpstorage:" + ipaddress);
		 if(ipAllData != null){
		     disks = (List<Disk>) ipAllData.get("disks");
		 }		 
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
		//Paragraph title = new Paragraph("������������ͨ�ʱ���", titleFont);
		Paragraph title = new Paragraph("HP�洢" + ipaddress + "���̱���", titleFont);
		// ���ñ����ʽ���뷽ʽ
		title.setAlignment(Element.ALIGN_CENTER);
		// title.setFont(titleFont);
		document.add(title);
		// ���� Table ���
		Font fontChinese = new Font(bfChinese, 12, Font.NORMAL, Color.black);		
		Table aTable = new Table(7);
		int width[] = {50, 50, 50, 130, 30, 50,50};
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
		
		Cell cell1 = new Cell("����");
		Cell cell11 = new Cell("״̬");
		Cell cell2 = new Cell("����");
		Cell cell3 = new Cell("����");
		Cell cell4 = new Cell("������");		
		Cell cell13 = new Cell("��ʼ������");
		Cell cell14 = new Cell("�̼��汾");
		
		cell13.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell11.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell14.setHorizontalAlignment(Element.ALIGN_CENTER);

		cell13.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell11.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell4.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell14.setVerticalAlignment(Element.ALIGN_MIDDLE);

		cell1.setBackgroundColor(Color.LIGHT_GRAY);
		cell11.setBackgroundColor(Color.LIGHT_GRAY);
		cell13.setBackgroundColor(Color.LIGHT_GRAY);
		cell14.setBackgroundColor(Color.LIGHT_GRAY);
		cell2.setBackgroundColor(Color.LIGHT_GRAY);
		cell3.setBackgroundColor(Color.LIGHT_GRAY);
		cell4.setBackgroundColor(Color.LIGHT_GRAY);
		aTable.addCell(cell1);
		aTable.addCell(cell11);
		aTable.addCell(cell2);
		aTable.addCell(cell3);
		aTable.addCell(cell4);
		aTable.addCell(cell13);
		aTable.addCell(cell14);
		
		if (disks != null && disks.size() > 0) {
			for (int i = 0; i < disks.size(); i++) {
				Disk disk = (Disk) disks.get(i);				
				Cell cell6 = new Cell(SysUtil.ifNull(disk.getName()));
				Cell cell7 = new Cell(SysUtil.ifNull(disk.getStatus()));
				Cell cell8 = new Cell(SysUtil.ifNull(disk.getVendorID()));
				Cell cell9 = new Cell(SysUtil.ifNull(disk.getDataCapacity()));
				Cell cell10 = new Cell(SysUtil.ifNull(disk.getRedundancyGroup()));
				Cell cell15 = new Cell(SysUtil.ifNull(disk.getInitializeState()));
				Cell cell16 = new Cell(SysUtil.ifNull(disk.getFirmwareRevision()));
				
				cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell7.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell8.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell9.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell10.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell15.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell16.setHorizontalAlignment(Element.ALIGN_CENTER);
				
				aTable.addCell(cell6);
				aTable.addCell(cell7);
				aTable.addCell(cell8);
				aTable.addCell(cell9);
				aTable.addCell(cell10);
				aTable.addCell(cell15);
				aTable.addCell(cell16);

			}
		}		
		document.add(aTable);
		document.close();		
	}
	/*
	 * HP�洢��������PDF�ĵ�
	 */
	public void createHPstorageDiskPdf(String file,String ipaddress) throws DocumentException, IOException {

		// com.lowagie.text.Font FontChinese = new
		// com.lowagie.text.Font(bfChinese, 12, com.lowagie.text.Font.NORMAL);

		// ����ֽ�Ŵ�С

		List<Disk> disks = null;
		Hashtable ipAllData = new Hashtable();
		//ֻȡ��ǰ�ɼ��Ĵ�����Ϣ
		ipAllData = (Hashtable) ShareData.getSharedata().get("hpstorage:" + ipaddress);
		if(ipAllData != null){
		   disks = (List<Disk>) ipAllData.get("disks");
		}
		
		Document document = new Document(PageSize.A4);
		// ����һ����д��(Writer)��document���������ͨ����д��(Writer)���Խ��ĵ�д�뵽������
		PdfWriter.getInstance(document, new FileOutputStream(file));
		document.open();
		// ������������
		BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
		// ����������
		Font titleFont = new Font(bfChinese, 12, Font.BOLD);
		// com.lowagie.text.Font titleFont = new
		// com.lowagie.text.Font(bfChinese, 12, com.lowagie.text.Font.NORMAL);
		// ����������
		Font contextFont = new com.lowagie.text.Font(bfChinese, 11, Font.NORMAL);
		Paragraph title = new Paragraph("HP�洢" + ipaddress + "���̱���", titleFont);
		// ���ñ����ʽ���뷽ʽ
		title.setAlignment(Element.ALIGN_CENTER);
		// title.setFont(titleFont);
		document.add(title);
		document.add(new Paragraph("\n"));
		// ���� Table ���
		Font fontChinese = new Font(bfChinese, 11, Font.NORMAL, Color.black);		
		
		PdfPTable aTable = new PdfPTable(7);

		int width[] = {50, 50, 50, 130, 30, 50,50};
		aTable.setWidthPercentage(100);
		aTable.setWidths(width);
		
		PdfPCell cell1 = new PdfPCell(new Phrase("����", fontChinese));
		PdfPCell cell11 = new PdfPCell(new Phrase("״̬", contextFont));
		PdfPCell cell2 = new PdfPCell(new Phrase("����", contextFont));
		PdfPCell cell3 = new PdfPCell(new Phrase("����", contextFont));
		PdfPCell cell4 = new PdfPCell(new Phrase("������", contextFont));
		PdfPCell cell15 = new PdfPCell(new Phrase("��ʼ������", contextFont));
		PdfPCell cell16 = new PdfPCell(new Phrase("�̼��汾", contextFont));
		
		cell1.setBackgroundColor(Color.LIGHT_GRAY);
		cell11.setBackgroundColor(Color.LIGHT_GRAY);
		cell2.setBackgroundColor(Color.LIGHT_GRAY);
		cell3.setBackgroundColor(Color.LIGHT_GRAY);
		cell4.setBackgroundColor(Color.LIGHT_GRAY);
		cell15.setBackgroundColor(Color.LIGHT_GRAY);
		cell16.setBackgroundColor(Color.LIGHT_GRAY);
		
		cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell11.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell15.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell16.setHorizontalAlignment(Element.ALIGN_CENTER);
		
		cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell11.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell4.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell15.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell16.setVerticalAlignment(Element.ALIGN_MIDDLE);
		
		aTable.addCell(cell1);
		aTable.addCell(cell11);
		aTable.addCell(cell2);
		aTable.addCell(cell3);
		aTable.addCell(cell4);
		aTable.addCell(cell15);
		aTable.addCell(cell16);
		
		if (disks != null && disks.size() > 0) {
			for (int i = 0; i < disks.size(); i++) {
				Disk disk = (Disk) disks.get(i);			
				
				PdfPCell cell6 = new PdfPCell(new Phrase(SysUtil.ifNull(disk.getName()),contextFont));
				PdfPCell cell7 = new PdfPCell(new Paragraph(SysUtil.ifNull(disk.getStatus()), contextFont));
				PdfPCell cell8 = new PdfPCell(new Phrase(SysUtil.ifNull(disk.getVendorID()), contextFont));
				PdfPCell cell9 = new PdfPCell(new Phrase(SysUtil.ifNull(disk.getDataCapacity()),contextFont));
				PdfPCell cell10 = new PdfPCell(new Phrase(SysUtil.ifNull(disk.getRedundancyGroup()),contextFont));
				PdfPCell cell17 = new PdfPCell(new Phrase(SysUtil.ifNull(disk.getInitializeState()),contextFont));
				PdfPCell cell18 = new PdfPCell(new Phrase(SysUtil.ifNull(disk.getFirmwareRevision()),contextFont));
				
				cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell7.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell8.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell9.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell10.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell17.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell18.setHorizontalAlignment(Element.ALIGN_CENTER);
				
				aTable.addCell(cell6);
				aTable.addCell(cell7);
				aTable.addCell(cell8);
				aTable.addCell(cell9);
				aTable.addCell(cell10);
				aTable.addCell(cell17);
				aTable.addCell(cell18);
			}
		}		
		document.add(aTable);
		document.close();
	}

	private String showNetworkEventReport() {
		String ipaddress = getParaValue("ipaddress");
		request.setAttribute("ipaddress", ipaddress);
		Date d = new Date();
		String startdate = getParaValue("startdate");
		if (startdate == null) {
			startdate = sdf0.format(d);
		}
		String todate = getParaValue("todate");
		if (todate == null) {
			todate = sdf0.format(d);
		}
		String starttime = startdate + " 00:00:00";
		String totime = todate + " 23:59:59";

		String[] ids = getParaArrayValue("checkbox");
		Hashtable allcpuhash = new Hashtable();

		// �������־ȡ���˿����¼�¼���б�
		String orderflag = "ipaddress";
		if (getParaValue("orderflag") != null && !getParaValue("orderflag").equals("")) {
			orderflag = getParaValue("orderflag");
		}

		List orderList = new ArrayList();
		HostNodeDao dao = new HostNodeDao();
		List selectedIPNodeList = dao.findBynode("ip_address", ipaddress);
		if (selectedIPNodeList != null && selectedIPNodeList.size() > 0) {
			HostNode node = (HostNode) selectedIPNodeList.get(0);
			EventListDao eventdao = new EventListDao();
			// �õ��¼��б�
			StringBuffer s = new StringBuffer();
//			s.append("select * from system_eventlist where recordtime>= '" + starttime + "' " + "and recordtime<='" + totime + "' ");
			if(SystemConstant.DBType.equals("mysql")){
				s.append("select * from system_eventlist where recordtime>= '" + starttime + "' " + "and recordtime<='" + totime + "' ");

			}else if(SystemConstant.DBType.equals("oracle")){
				s.append("select * from system_eventlist where recordtime>=to_date( '" + starttime + "','yyyy-mm-dd  hh24:mi:ss') " + "and recordtime<=to_date('" + totime + "','yyyy-mm-dd hh24:mi:ss') ");

			}
			s.append(" and nodeid=" + node.getId());

			request.setAttribute("nodeid", node.getId());
			
			List infolist = eventdao.findByCriteria(s.toString());

			// List infolist =
			// eventqueryManager.getQuery(starttime,totime,"99","99",99,node.getId());
			if (infolist != null && infolist.size() > 0) {
				// mainreport = mainreport+ " \r\n";
				int levelone = 0;
				int levletwo = 0;
				int levelthree = 0;
				int pingvalue = 0;
				int cpuvalue = 0;
				int updownvalue = 0;
				int utilvalue = 0;

				for (int j = 0; j < infolist.size(); j++) {
					EventList eventlist = (EventList) infolist.get(j);
					if (eventlist.getContent() == null)
						eventlist.setContent("");
					String content = eventlist.getContent();
					String subentity = eventlist.getSubentity();
					if (eventlist.getLevel1() == 1) {
						levelone = levelone + 1;
					} else if (eventlist.getLevel1() == 2) {
						levletwo = levletwo + 1;
					} else if (eventlist.getLevel1() == 3) {
						levelthree = levelthree + 1;
					}
					// if(content.indexOf("��ͨ��")>=0){
					// pingvalue = pingvalue + 1;
					// }else if(content.indexOf("CPU������")>=0){
					// cpuvalue = cpuvalue + 1;
					// }else if(content.indexOf("up")>=0 ||
					// content.indexOf("down")>=0){
					// updownvalue = updownvalue + 1;
					// }else if(content.indexOf("����")>=0){
					// utilvalue = utilvalue + 1;
					// }

					if (("ping").equals(subentity)) {// ��ͨ��
						pingvalue = pingvalue + 1;
					} else if (("cpu").equals(subentity)) {// cpu
						cpuvalue = cpuvalue + 1;
					} else if (("interface").equals(subentity) && content.indexOf("�˿�") >= 0) {// �˿�
						updownvalue = updownvalue + 1;
					} else if (("interface").equals(subentity) && content.indexOf("�˿�") < 0) {// ����
						utilvalue = utilvalue + 1;
					}
				}
				String equname = node.getAlias();
				String ip = node.getIpAddress();
				List ipeventList = new ArrayList();
				ipeventList.add(ip);
				ipeventList.add(equname);
				ipeventList.add(node.getType());
				ipeventList.add((levelone + levletwo + levelthree) + "");
				ipeventList.add(levelone + "");
				ipeventList.add(levletwo + "");
				ipeventList.add(levelthree + "");
				ipeventList.add(pingvalue + "");
				ipeventList.add(cpuvalue + "");
				ipeventList.add(updownvalue + "");
				ipeventList.add(utilvalue + "");
				orderList.add(ipeventList);

			}
		}
		List returnList = new ArrayList();
		if (orderflag.equalsIgnoreCase("one") || orderflag.equalsIgnoreCase("two") || orderflag.equalsIgnoreCase("three") || orderflag.equalsIgnoreCase("ping") || orderflag.equalsIgnoreCase("cpu")
				|| orderflag.equalsIgnoreCase("updown") || orderflag.equalsIgnoreCase("util") || orderflag.equalsIgnoreCase("sum")) {
			returnList = (List) session.getAttribute("eventlist");
		} else {
			returnList = orderList;
		}

		List list = new ArrayList();
		if (returnList != null && returnList.size() > 0) {
			for (int m = 0; m < returnList.size(); m++) {
				List ipdiskList = (List) returnList.get(m);
				for (int n = m + 1; n < returnList.size(); n++) {
					List _ipdiskList = (List) returnList.get(n);
					if (orderflag.equalsIgnoreCase("ipaddress")) {
					} else if (orderflag.equalsIgnoreCase("sum")) {
						String sum = "";
						if (ipdiskList.get(3) != null) {
							sum = (String) ipdiskList.get(3);
						}
						String _sum = "";
						if (ipdiskList.get(3) != null) {
							_sum = (String) _ipdiskList.get(3);
						}
						if (new Double(sum).doubleValue() < new Double(_sum).doubleValue()) {
							returnList.remove(m);
							returnList.add(m, _ipdiskList);
							returnList.remove(n);
							returnList.add(n, ipdiskList);
							ipdiskList = _ipdiskList;
							_ipdiskList = null;
						}
					} else if (orderflag.equalsIgnoreCase("one")) {
						String downnum = "";
						if (ipdiskList.get(4) != null) {
							downnum = (String) ipdiskList.get(4);
						}
						String _downnum = "";
						if (ipdiskList.get(4) != null) {
							_downnum = (String) _ipdiskList.get(4);
						}
						if (new Double(downnum).doubleValue() < new Double(_downnum).doubleValue()) {
							returnList.remove(m);
							returnList.add(m, _ipdiskList);
							returnList.remove(n);
							returnList.add(n, ipdiskList);
							ipdiskList = _ipdiskList;
							_ipdiskList = null;
						}
					} else if (orderflag.equalsIgnoreCase("two")) {
						String downnum = "";
						if (ipdiskList.get(5) != null) {
							downnum = (String) ipdiskList.get(5);
						}
						String _downnum = "";
						if (ipdiskList.get(5) != null) {
							_downnum = (String) _ipdiskList.get(5);
						}
						if (new Double(downnum).doubleValue() < new Double(_downnum).doubleValue()) {
							returnList.remove(m);
							returnList.add(m, _ipdiskList);
							returnList.remove(n);
							returnList.add(n, ipdiskList);
							ipdiskList = _ipdiskList;
							_ipdiskList = null;
						}
					} else if (orderflag.equalsIgnoreCase("three")) {
						String downnum = "";
						if (ipdiskList.get(6) != null) {
							downnum = (String) ipdiskList.get(6);
						}
						String _downnum = "";
						if (ipdiskList.get(6) != null) {
							_downnum = (String) _ipdiskList.get(6);
						}
						if (new Double(downnum).doubleValue() < new Double(_downnum).doubleValue()) {
							returnList.remove(m);
							returnList.add(m, _ipdiskList);
							returnList.remove(n);
							returnList.add(n, ipdiskList);
							ipdiskList = _ipdiskList;
							_ipdiskList = null;
						}
					} else if (orderflag.equalsIgnoreCase("ping")) {
						String downnum = "";
						if (ipdiskList.get(7) != null) {
							downnum = (String) ipdiskList.get(7);
						}
						String _downnum = "";
						if (ipdiskList.get(7) != null) {
							_downnum = (String) _ipdiskList.get(7);
						}
						if (new Double(downnum).doubleValue() < new Double(_downnum).doubleValue()) {
							returnList.remove(m);
							returnList.add(m, _ipdiskList);
							returnList.remove(n);
							returnList.add(n, ipdiskList);
							ipdiskList = _ipdiskList;
							_ipdiskList = null;
						}
					} else if (orderflag.equalsIgnoreCase("cpu")) {
						String downnum = "";
						if (ipdiskList.get(8) != null) {
							downnum = (String) ipdiskList.get(8);
						}
						String _downnum = "";
						if (ipdiskList.get(8) != null) {
							_downnum = (String) _ipdiskList.get(8);
						}
						if (new Double(downnum).doubleValue() < new Double(_downnum).doubleValue()) {
							returnList.remove(m);
							returnList.add(m, _ipdiskList);
							returnList.remove(n);
							returnList.add(n, ipdiskList);
							ipdiskList = _ipdiskList;
							_ipdiskList = null;
						}
					} else if (orderflag.equalsIgnoreCase("updown")) {
						String downnum = "";
						if (ipdiskList.get(9) != null) {
							downnum = (String) ipdiskList.get(9);
						}
						String _downnum = "";
						if (ipdiskList.get(9) != null) {
							_downnum = (String) _ipdiskList.get(9);
						}
						if (new Double(downnum).doubleValue() < new Double(_downnum).doubleValue()) {
							returnList.remove(m);
							returnList.add(m, _ipdiskList);
							returnList.remove(n);
							returnList.add(n, ipdiskList);
							ipdiskList = _ipdiskList;
							_ipdiskList = null;
						}
					} else if (orderflag.equalsIgnoreCase("util")) {
						String downnum = "";
						if (ipdiskList.get(10) != null) {
							downnum = (String) ipdiskList.get(10);
						}
						String _downnum = "";
						if (ipdiskList.get(10) != null) {
							_downnum = (String) _ipdiskList.get(10);
						}
						if (new Double(downnum).doubleValue() < new Double(_downnum).doubleValue()) {
							returnList.remove(m);
							returnList.add(m, _ipdiskList);
							returnList.remove(n);
							returnList.add(n, ipdiskList);
							ipdiskList = _ipdiskList;
							_ipdiskList = null;
						}
					}
				}
				// �õ�������Subentity���б�
				list.add(ipdiskList);
				ipdiskList = null;
			}
		}

		// setListProperty(capReportForm, request, list);
		request.setAttribute("starttime", starttime);
		request.setAttribute("totime", totime);
		request.setAttribute("startdate", startdate);
		request.setAttribute("todate", todate);
		request.setAttribute("eventlist", list);
		session.setAttribute("eventlist", list);
		return "/capreport/net/showNetworkEventReport.jsp";
	}

	private String showNetworkCompositeReport() {
		Date d = new Date();
		String startdate = getParaValue("startdate");
		if (startdate == null) {
			startdate = sdf0.format(d);
		}
		String todate = getParaValue("todate");
		if (todate == null) {
			todate = sdf0.format(d);
		}
		String ipaddress = getParaValue("ipaddress");
		request.setAttribute("ipaddress", ipaddress);
		request.setAttribute("startdate", startdate);
		request.setAttribute("todate", todate);
		// dddddddddddddddddd
		String starttime = startdate + " 00:00:00";
		String totime = todate + " 23:59:59";
		Hashtable allcpuhash = new Hashtable();
		String ip = "";
		String equipname = "";
		// zhushouzhi--------------------------start
		String type = "";
		String typename = "";
		String equipnameNetDoc = "";
		// zhushouzhi-----------------------------end
		Hashtable hash = new Hashtable();// "Cpu",--current
		Hashtable memhash = new Hashtable();// mem--current
		Hashtable diskhash = new Hashtable();
		Hashtable memmaxhash = new Hashtable();// mem--max
		Hashtable memavghash = new Hashtable();// mem--avg
		Hashtable maxhash = new Hashtable();// "Cpu"--max
		Hashtable maxping = new Hashtable();// Ping--max

		Vector vector = new Vector();
		Vector iprouterVector = new Vector();
		List routerList = null;
		Hashtable reporthash = new Hashtable();
		int pingvalue = 0;
		int cpuvalue = 0;
		int updownvalue = 0;
		int utilvalue = 0;
		String runmodel = PollingEngine.getCollectwebflag();

		try {
			ip = getParaValue("ipaddress");
			// System.out.println("##########################ipaddress="+ip);
			type = getParaValue("type");
			HostNodeDao dao = new HostNodeDao();
			HostNode node = null;
			try {
				node = (HostNode) dao.findByCondition("ip_address", ip).get(0);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				dao.close();
			}
			// ip=node.getIpAddress();
			equipname = node.getAlias() + "(" + ip + ")";

			// zhushouzhi---------------------start
			equipnameNetDoc = node.getAlias();
			// zhushouzhi-----------------------end
			String remoteip = request.getRemoteAddr();
			String newip = doip(ip);
			// System.out.println("#####################newip"+newip);
			request.setAttribute("newip", newip);
			request.setAttribute("nodeid", node.getId());
			// �������־ȡ���˿����¼�¼���б�
			String orderflag = "index";

			String[] netInterfaceItem = { "index", "ifname", "ifSpeed", "ifOperStatus", "OutBandwidthUtilHdx", "InBandwidthUtilHdx" };
			if ("0".equals(runmodel)) {
				// �ɼ�������Ǽ���ģʽ
				Hashtable pingdata = ShareData.getPingdata();
				Hashtable sharedata = ShareData.getSharedata();
				vector = hostlastmanager.getInterface_share(ip, netInterfaceItem, orderflag, startdate, todate);
				Vector pdata = (Vector) pingdata.get(ip);
				// ��ping�õ������ݼӽ�ȥ
				if (pdata != null && pdata.size() > 0) {
					for (int m = 0; m < pdata.size(); m++) {
						Pingcollectdata hostdata = (Pingcollectdata) pdata.get(m);
						if (hostdata.getSubentity().equals("ConnectUtilization")) {
							reporthash.put("time", hostdata.getCollecttime());
							reporthash.put("Ping", hostdata.getThevalue());
							reporthash.put("ping", maxping);
						}
					}
				}
				// ���ڴ��л�õ�ǰ�ĸ���IP��ص�IP-MAC��FDB����Ϣ
				Hashtable _IpRouterHash = ShareData.getIprouterdata();
				iprouterVector = (Vector) _IpRouterHash.get(ip);

				Hashtable hdata = (Hashtable) sharedata.get(ip);
				if (hdata == null)
					hdata = new Hashtable();
				Vector cpuVector = new Vector();
				if (hdata.get("cpu") != null)
					cpuVector = (Vector) hdata.get("cpu");
				if (cpuVector != null && cpuVector.size() > 0) {
					// for(int si=0;si<cpuVector.size();si++){
					CPUcollectdata cpudata = (CPUcollectdata) cpuVector.elementAt(0);
					maxhash.put("cpu", cpudata.getThevalue());
					reporthash.put("CPU", maxhash);
					// }
				} else {
					reporthash.put("CPU", maxhash);
				}
			} else {
				// �ɼ�������Ƿ���ģʽ
				NodeUtil nodeUtil = new NodeUtil();
				NodeDTO nodeDTO = nodeUtil.creatNodeDTOByNode(node);

				// ��ȡ��ǰ�ڴ�ֵ
				MemoryInfoService memoryInfoService = new MemoryInfoService(String.valueOf(nodeDTO.getId()), nodeDTO.getType(), nodeDTO.getSubtype());
				List memoryInfoList = memoryInfoService.getCurrPerMemoryListInfo();
				// �˿�����
				try {
					vector = hostlastmanager.getInterface(ip, netInterfaceItem, orderflag, starttime, totime);
				} catch (Exception e) {
					e.printStackTrace();
				}
				// CPU
				CpuInfoService cpuInfoService = new CpuInfoService(String.valueOf(nodeDTO.getId()), nodeDTO.getType(), nodeDTO.getSubtype());
				String currCpuAvgInfo = cpuInfoService.getCurrCpuAvgInfo();
				maxhash.put("cpu", currCpuAvgInfo);
				// iprout
				NetService netService = new NetService(String.valueOf(nodeDTO.getId()), nodeDTO.getType(), nodeDTO.getSubtype());
				routerList = netService.getRouterInfo();
				// iprouterVector =
				// netService.getIpRouterVectorByList(routerList);
				// ******��ǰ��ͨ��start
				String pingnow = "0.0";
				String pingconavg = "0";
				Vector pingData = new PingInfoService(String.valueOf(nodeDTO.getId()), nodeDTO.getType(), nodeDTO.getSubtype()).getPingInfo();
				;
				if (pingData != null && pingData.size() > 0) {
					Pingcollectdata pingdata = (Pingcollectdata) pingData.get(0);
					Calendar tempCal = (Calendar) pingdata.getCollecttime();
					Date cc = tempCal.getTime();
					pingnow = pingdata.getThevalue();// ��ǰ��ͨ��
				}
				Hashtable ConnectUtilizationhash = hostmanager.getCategory(ip, "Ping", "ConnectUtilization", starttime, totime);
				if (ConnectUtilizationhash.get("avgpingcon") != null) {
					pingconavg = (String) ConnectUtilizationhash.get("avgpingcon");
				}
				String ConnectUtilizationmax = "";
				maxping.put("avgpingcon", pingconavg);
				if (ConnectUtilizationhash.get("max") != null) {
					ConnectUtilizationmax = (String) ConnectUtilizationhash.get("max");
				}
				maxping.put("pingmax", ConnectUtilizationmax);
				reporthash.put("ping", maxping);
				reporthash.put("Ping", pingnow);
				// ******��ǰ��ͨ��end
			}
			PortconfigDao portdao = new PortconfigDao();
			Hashtable portconfigHash = portdao.getIpsHash(ip);
			List reportports = portdao.getByIpAndReportflag(ip, new Integer(1));
			if (reportports != null && reportports.size() > 0) {
				// SysLogger.info("reportports size
				// ##############"+reportports.size());
				// ��ʾ�˿ڵ�����ͼ��
				I_HostCollectDataDay daymanager = new HostCollectDataDayManager();
				String unit = "kb/s";
				String title = starttime + "��" + totime + "�˿�����";
				String[] banden3 = { "InBandwidthUtilHdx", "OutBandwidthUtilHdx" };
				String[] bandch3 = { "�������", "��������" };

				for (int i = 0; i < reportports.size(); i++) {
					// SysLogger.info(reportports.get(i).getClass()+
					// "===============");
					com.afunms.config.model.Portconfig portconfig = null;
					try {
						portconfig = (com.afunms.config.model.Portconfig) reportports.get(i);
						// ��������ʾ����
						Hashtable value = new Hashtable();
						value = daymanager.getmultiHisHdx(ip, "ifspeed", portconfig.getPortindex() + "", banden3, bandch3, startdate, todate, "UtilHdx");
						String reportname = "��" + portconfig.getPortindex() + "(" + portconfig.getName() + ")�˿�����" + startdate + "��" + todate + "����(��������ʾ)";
						p_drawchartMultiLineMonth(value, reportname, newip + portconfig.getPortindex() + "ifspeed_day", 800, 200, "UtilHdx");
						String url1 = "../images/jfreechart/" + newip + portconfig.getPortindex() + "ifspeed_day.png";
					} catch (Exception ex) {
						ex.printStackTrace();
					}

				}
			}
//            System.out.println("----------------------------"+routerList);
			if(routerList != null)
			{
				reporthash.put("routerList", routerList);
			}
			reporthash.put("portconfigHash", portconfigHash);
			reporthash.put("reportports", reportports);
			reporthash.put("netifVector", vector);
			// zhushouzhi--------------------start
			reporthash.put("startdate", startdate);
			reporthash.put("todate", todate);
			reporthash.put("totime", totime);
			reporthash.put("starttime", starttime);
			reporthash.put("time", Calendar.getInstance());
			// zhushouzhi-------------------------end
			Hashtable cpuhash = hostmanager.getCategory(ip, "CPU", "Utilization", starttime, totime);
			Hashtable streaminHash = hostmanager.getAllutilhdx(ip, "AllInBandwidthUtilHdx", starttime, totime, "avg");
			Hashtable streamoutHash = hostmanager.getAllutilhdx(ip, "AllOutBandwidthUtilHdx", starttime, totime, "avg");
			String pingconavg = "";
			String avgput = "";
			// String avgoutput = "";
			// maxhash = new Hashtable();
			String cpumax = "";
			String avgcpu = "";
			String maxput = "";
			if (cpuhash.get("max") != null) {
				cpumax = (String) cpuhash.get("max");
			}
			if (cpuhash.get("avgcpucon") != null) {
				avgcpu = (String) cpuhash.get("avgcpucon");
			}
			// zhushouzhi-----------------------start
			if (streaminHash.get("avgput") != null) {
				avgput = (String) streaminHash.get("avgput");
				reporthash.put("avginput", avgput);
			}
			if (streamoutHash.get("avgput") != null) {
				avgput = (String) streamoutHash.get("avgput");
				reporthash.put("avgoutput", avgput);
			}
			Hashtable streammaxinHash = hostmanager.getAllutilhdx(ip, "AllInBandwidthUtilHdx", starttime, totime, "max");
			Hashtable streammaxoutHash = hostmanager.getAllutilhdx(ip, "AllOutBandwidthUtilHdx", starttime, totime, "max");
			if (streammaxinHash.get("max") != null) {
				maxput = (String) streammaxinHash.get("max");
				reporthash.put("maxinput", maxput);
			}
			if (streammaxoutHash.get("max") != null) {
				maxput = (String) streammaxoutHash.get("max");
				reporthash.put("maxoutput", maxput);
			}
			// zhushouzhi--------------------------------------end
			maxhash.put("cpumax", cpumax);
			maxhash.put("avgcpu", avgcpu);

			if (iprouterVector != null)
				reporthash.put("iprouterVector", iprouterVector);
			// zhushouzhi--------------------start
			EventListDao eventdao = new EventListDao();
			// �õ��¼��б�
			StringBuffer s = new StringBuffer();
//			s.append("select * from system_eventlist where recordtime>= '" + starttime + "' " + "and recordtime<='" + totime + "' ");
			if(SystemConstant.DBType.equals("mysql")){
				s.append("select * from system_eventlist where recordtime>= '" + starttime + "' " + "and recordtime<='" + totime + "' ");

			}else if(SystemConstant.DBType.equals("oracle")){
				s.append("select * from system_eventlist where recordtime>= to_date('" + starttime + "','yyyy-mm-dd hh24:mi:ss') " + "and recordtime<= to_date('" + totime + "','yyyy-mm-dd hh24:mi:ss') ");

			}
			s.append(" and nodeid=" + node.getId());

			List infolist = eventdao.findByCriteria(s.toString());

			if (infolist != null && infolist.size() > 0) {
				// mainreport = mainreport+ " \r\n";

				for (int j = 0; j < infolist.size(); j++) {
					EventList eventlist = (EventList) infolist.get(j);
					if (eventlist.getContent() == null)
						eventlist.setContent("");
					String content = eventlist.getContent();
					String subentity = eventlist.getSubentity();
					if (("ping").equals(subentity)) {// ��ͨ��
						pingvalue = pingvalue + 1;
					} else if (("cpu").equals(subentity)) {// cpu
						cpuvalue = cpuvalue + 1;
					} else if (("interface").equals(subentity) && content.indexOf("�˿�") >= 0) {// �˿�
						updownvalue = updownvalue + 1;
					} else if (("interface").equals(subentity) && content.indexOf("�˿�") < 0) {// ����
						utilvalue = utilvalue + 1;
					}
				}
			}
			reporthash.put("CPU", maxhash);
			reporthash.put("pingvalue", pingvalue);
			reporthash.put("cpuvalue", cpuvalue);
			reporthash.put("updownvalue", updownvalue);
			reporthash.put("utilvalue", utilvalue);
			// zhushouzhi-------------------------end

			// Vector pdata = (Vector) pingdata.get(ip);
			// // ��ping�õ������ݼӽ�ȥ
			// if (pdata != null && pdata.size() > 0) {
			// for (int m = 0; m < pdata.size(); m++) {
			// Pingcollectdata hostdata = (Pingcollectdata) pdata.get(m);
			// if (hostdata.getSubentity().equals("ConnectUtilization")) {
			// reporthash.put("time", hostdata.getCollecttime());
			// reporthash.put("Ping", hostdata.getThevalue());
			// reporthash.put("ping", maxping);
			// }
			// }
			// }

			// CPU
			// Hashtable hdata = (Hashtable) sharedata.get(ip);
			// if (hdata == null)
			// hdata = new Hashtable();
			// Vector cpuVector = new Vector();
			// if (hdata.get("cpu") != null)
			// cpuVector = (Vector) hdata.get("cpu");
			// if (cpuVector != null && cpuVector.size() > 0) {
			// // for(int si=0;si<cpuVector.size();si++){
			// CPUcollectdata cpudata = (CPUcollectdata) cpuVector.elementAt(0);
			// maxhash.put("cpu", cpudata.getThevalue());
			// reporthash.put("CPU", maxhash);
			// // }
			// } else {
			// reporthash.put("CPU", maxhash);
			// }
			// ����

			reporthash.put("Memory", memhash);
			reporthash.put("Disk", diskhash);
			reporthash.put("equipname", equipname);
			reporthash.put("equipnameNetDoc", equipnameNetDoc);
			reporthash.put("ip", ip);
			if ("network".equals(type)) {
				typename = "�����豸";

			}
			reporthash.put("typename", typename);

			AbstractionReport1 report = new ExcelReport1(new IpResourceReport(), reporthash);
			// ��ͼ

			p_draw_line(cpuhash, "", newip + "cpu", 740, 120);

			Hashtable ConnectUtilizationhash = hostmanager.getCategory(ip, "Ping", "ConnectUtilization", starttime, totime);
			if (ConnectUtilizationhash.get("avgpingcon") != null)
				pingconavg = (String) ConnectUtilizationhash.get("avgpingcon");
			String ConnectUtilizationmax = "";
			maxping.put("avgpingcon", pingconavg);
			if (ConnectUtilizationhash.get("max") != null) {
				ConnectUtilizationmax = (String) ConnectUtilizationhash.get("max");
			}
			maxping.put("pingmax", ConnectUtilizationmax);

			p_draw_line(ConnectUtilizationhash, "", newip + "ConnectUtilization", 740, 120);
		} catch (Exception e) {
			e.printStackTrace();
		}
		session.setAttribute("reporthash", reporthash);
		return "/capreport/net/showNetworkCompositeReport.jsp";
	}

	private String showNetworkConfigReport() {
		String startdate = this.getParaValue("startdate");
		String todate = this.getParaValue("todate");
		String ipaddress = getParaValue("ipaddress");
		request.setAttribute("ipaddress", ipaddress);
		request.setAttribute("startdate", startdate);
		request.setAttribute("todate", todate);
		return "/capreport/net/showNetworkConfigReport.jsp";
	}

	private String downloadNetworkPingReport() {
		String str = request.getParameter("str");
		if (str.equals("1")) {
			String file = "/temp/liantonglvbaobiao.doc";// ���浽��Ŀ�ļ����µ�ָ���ļ���
			String fileName = ResourceCenter.getInstance().getSysPath() + file;// ��ȡϵͳ�ļ���·��
			try {
				createDocContext(fileName);
			} catch (DocumentException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			request.setAttribute("filename", fileName);
		}
		if (str.equals("0")) {
			Date d = new Date();
			String startdate = getParaValue("startdate");
			if (startdate == null) {
				startdate = sdf0.format(d);
			}
			String todate = getParaValue("todate");
			if (todate == null) {
				todate = sdf0.format(d);
			}
			String starttime = startdate + " 00:00:00";
			String totime = todate + " 23:59:59";
			Hashtable allcpuhash = new Hashtable();

			List returnList = new ArrayList();
			// I_MonitorIpList monitorManager=new MonitoriplistManager();
			List memlist = (List) session.getAttribute("pinglist");
			Hashtable reporthash = new Hashtable();
			
			reporthash.put("pinglist", memlist);
			reporthash.put("starttime", starttime);
			reporthash.put("totime", totime);
			
			reporthash.put("pingpath", session.getAttribute("pingpath"));

			ExcelReport1 report = new ExcelReport1(new IpResourceReport(), reporthash);
			report.setRequest(request);
			report.createReport_hostping("/temp/liantonglvbaobiao.xls");
			request.setAttribute("filename", report.getFileName());
		}
		if (str.equals("4")) {
			String file = "/temp/liantonglvbaobiao.PDF";// ���浽��Ŀ�ļ����µ�ָ���ļ���
			String fileName = ResourceCenter.getInstance().getSysPath() + file;// ��ȡϵͳ�ļ���·��
			try {
				createPdfContextPing(fileName);
			} catch (DocumentException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			request.setAttribute("filename", fileName);
		}

		return "/capreport/net/download.jsp";
	}

	private String downloadNetworkEventReport() {
		String str = request.getParameter("str");
		if (str.equals("1")) {
			String file = "/temp/shijianbaobiao.doc";// ���浽��Ŀ�ļ����µ�ָ���ļ���
			String fileName = ResourceCenter.getInstance().getSysPath() + file;// ��ȡϵͳ�ļ���·��
			try {
				createDocContextEvent(fileName);
			} catch (DocumentException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			request.setAttribute("filename", fileName);
		}
		if (str.equals("0")) {
			Date d = new Date();
			String startdate = getParaValue("startdate");
			if (startdate == null) {
				startdate = sdf0.format(d);
			}
			String todate = getParaValue("todate");
			if (todate == null) {
				todate = sdf0.format(d);
			}
			String starttime = startdate + " 00:00:00";
			String totime = todate + " 23:59:59";
			Hashtable allcpuhash = new Hashtable();

			List returnList = new ArrayList();
			// I_MonitorIpList monitorManager=new MonitoriplistManager();
			List memlist = (List) session.getAttribute("eventlist");
			Hashtable reporthash = new Hashtable();
			reporthash.put("eventlist", memlist);
			reporthash.put("starttime", starttime);
			reporthash.put("totime", totime);

			AbstractionReport1 report = new ExcelReport1(new IpResourceReport(), reporthash);
			report.createReport_netevent("/temp/shijianbiao_report.xls");
			request.setAttribute("filename", report.getFileName());
		}
		if (str.equals("4")) {
			String file = "/temp/shijianbaobiao.pdf";// ���浽��Ŀ�ļ����µ�ָ���ļ���
			String fileName = ResourceCenter.getInstance().getSysPath() + file;// ��ȡϵͳ�ļ���·��
			try {
				createDocContextEventPDF(fileName);
			} catch (DocumentException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			request.setAttribute("filename", fileName);
		}
		return "/capreport/net/download.jsp";
	}

	private String downloadNetworkCompositeReport() {
		Hashtable reporthash = (Hashtable) session.getAttribute("reporthash");
		AbstractionReport1 report = new ExcelReport1(new IpResourceReport(), reporthash);
		String str = request.getParameter("str");// ��ҳ�淵���趨��strֵ�����жϣ�����excel�������word����
		// zhushouzhi--------------------------------
		if ("0".equals(str)) {
			report.createReport_network("temp/networknms_report.xls");
			// SysLogger.info("filename---" + report.getFileName());//excel�ۺϱ���
			request.setAttribute("filename", report.getFileName());
		} else if ("1".equals(str)) {
			ExcelReport1 report1 = new ExcelReport1(new IpResourceReport(), reporthash);
			try {
				String file = "temp/networknms_report.doc";// ���浽��Ŀ�ļ����µ�ָ���ļ���
				String fileName = ResourceCenter.getInstance().getSysPath() + file;// ��ȡϵͳ�ļ���·��
				report1.createReport_networkDoc(fileName);// word�ۺϱ���

				request.setAttribute("filename", fileName);
			} catch (DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if ("3".equals(str)) {
			ExcelReport1 report1 = new ExcelReport1(new IpResourceReport(), reporthash);
			try {
				String file = "temp/networkNewknms_report.doc";// ���浽��Ŀ�ļ����µ�ָ���ļ���
				String fileName = ResourceCenter.getInstance().getSysPath() + file;// ��ȡϵͳ�ļ���·��
				report1.createReport_networkNewDoc(fileName);// word���з�������

				request.setAttribute("filename", fileName);
			} catch (DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else if ("4".equals(str)) {
			ExcelReport1 report1 = new ExcelReport1(new IpResourceReport(), reporthash);
			try {
				String file = "temp/networkNewknms_report.pdf";// ���浽��Ŀ�ļ����µ�ָ���ļ���
				String fileName = ResourceCenter.getInstance().getSysPath() + file;// ��ȡϵͳ�ļ���·��
				report1.createReport_networkNewPdf(fileName);// pdf�����豸ҵ�������

				request.setAttribute("filename", fileName);
			} catch (DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else if ("5".equals(str)) {
			ExcelReport1 report1 = new ExcelReport1(new IpResourceReport(), reporthash);
			try {
				String file = "temp/networkknms_report.pdf";// ���浽��Ŀ�ļ����µ�ָ���ļ���
				String fileName = ResourceCenter.getInstance().getSysPath() + file;// ��ȡϵͳ�ļ���·��
				report1.createReport_networkPDF(fileName);

				request.setAttribute("filename", fileName);
			} catch (DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return "/capreport/net/download.jsp";
	}

	private String downloadNetworkConfigReport() {
		return "/capreport/net/download.jsp";
	}

	private String area_chooseDrawLineType(String timeType, Hashtable hash, Hashtable hash1, String[] brand1, String[] brand2, int w, int h, int range1, int range2) {
		String returnStr = "";
		if (timeType == null) {
			timeType = "minute";
		}
		if ("minute".equals(timeType)) {
			returnStr = area_p_draw_line(hash, hash1, brand1, brand2, w, h, range1, range2);
		} else if ("hour".equals(timeType)) {
			// p_draw_lineByHour(hash, title1, title2, w, h);
		} else if ("day".equals(timeType)) {
			// p_draw_lineByDay(hash, title1, title2, w, h);
		} else if ("month".equals(timeType)) {
			// p_draw_lineByMonth(hash,title1,title2,w,h);
		} else if ("year".equals(timeType)) {
			// p_draw_line(hash,title1,title2,w,h);
		} else {
			// p_draw_line(hash,title1,title2,w,h);
		}
		// SysLogger.info("###########"+returnStr);
		return returnStr;
	}

	public String area_p_draw_line(Hashtable hash, Hashtable hash1, String[] brand1, String[] brand2, int w, int h, int range1, int range2) {
		String seriesKey = SysUtil.getLongID();
		List list = (List) hash.get("list");
		// SysLogger.info("list size ============ "+list.size());
		try {
			
			if (list == null || list.size() == 0) {
				// draw_blank(title1,title2,w,h);
				seriesKey = "";
			} else {
				// ��ʼ������
				final JFreeChart chart = ChartFactory.createTimeSeriesChart(brand1[0] + brand1[1] + "����ͼ", "ʱ��", brand2[0], ChartCreator.createForceDataset(hash, brand1[0]), true, true, false);

				// ����ȫͼ����ɫΪ��ɫ
				chart.setBackgroundPaint(Color.WHITE);

				final XYPlot plot = chart.getXYPlot();
				plot.getDomainAxis().setLowerMargin(0.0);
				plot.getDomainAxis().setUpperMargin(0.0);
				plot.setRangeCrosshairVisible(true);
				plot.setDomainCrosshairVisible(true);
				plot.setBackgroundPaint(Color.WHITE);
				plot.setForegroundAlpha(0.8f);
				plot.setRangeGridlinesVisible(true);
				plot.setRangeGridlinePaint(Color.darkGray);
				plot.setDomainGridlinesVisible(true);
				plot.setDomainGridlinePaint(new Color(139, 69, 19));
				XYLineAndShapeRenderer render0 = (XYLineAndShapeRenderer) plot.getRenderer(0);
				render0.setSeriesPaint(0, Color.BLUE);

				// configure the range axis to display directions...
				final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
				rangeAxis.setAutoRangeIncludesZero(true);
				rangeAxis.setRange(0, range1);

				plot.setRangeAxis(rangeAxis);
				final XYItemRenderer renderer2 = new XYAreaRenderer();
				final ValueAxis axis2 = new NumberAxis(brand2[1]);
				axis2.setRange(0.0, range2);

				// �������ͼ��ɫ
				XYAreaRenderer xyarearenderer = new XYAreaRenderer();
				xyarearenderer.setSeriesPaint(1, new Color(0, 204, 0));
				xyarearenderer.setSeriesFillPaint(1, Color.GREEN);
				xyarearenderer.setPaint(Color.GREEN);
				plot.setDataset(1, ChartCreator.createForceDataset(hash1, brand1[1]));
				plot.setRenderer(1, xyarearenderer);
				plot.setRangeAxis(1, axis2);
				plot.mapDatasetToRangeAxis(1, 1);

				LegendTitle legend = chart.getLegend();
				// legend.setItemFont(new Font("Verdena", 0, 9));
				JFreeChartBrother jfb = new JFreeChartBrother();
				jfb.setChart(chart);
				jfb.setWidth(w);
				jfb.setHeight(h);
				ResourceCenter.getInstance().getChartStorage().put(seriesKey, jfb);
				
			}
			hash = null;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return seriesKey;
	}

	public String area_p_draw_multiline(Hashtable hash, String title, String[] bandch, String[] bandch1, int w, int h, int range1, int range2) {
		String seriesKey = SysUtil.getLongID();
		Vector datasetV = ChartCreator.createMultiDataset(hash, bandch, bandch1);
		try {
			if (datasetV == null || datasetV.size() == 0) {
				seriesKey = "";
			} else {
				// ��ʼ������
				final JFreeChart chart = ChartFactory.createTimeSeriesChart(title + "����ͼ", "ʱ��", bandch1[0], (XYDataset) datasetV.get(0), true, true, false);

				// ����ȫͼ����ɫΪ��ɫ
				chart.setBackgroundPaint(Color.WHITE);
				chart.setBorderPaint(new Color(30, 144, 255));
				chart.setBorderVisible(true);

				final XYPlot plot = chart.getXYPlot();
				plot.getDomainAxis().setLowerMargin(0.0);
				plot.getDomainAxis().setUpperMargin(0.0);
				plot.setRangeCrosshairVisible(true);
				plot.setDomainCrosshairVisible(true);
				plot.setBackgroundPaint(Color.WHITE);
				plot.setForegroundAlpha(0.8f);
				plot.setRangeGridlinesVisible(true);
				plot.setRangeGridlinePaint(Color.darkGray);
				plot.setDomainGridlinesVisible(true);
				plot.setDomainGridlinePaint(new Color(139, 69, 19));
				XYLineAndShapeRenderer render0 = (XYLineAndShapeRenderer) plot.getRenderer(0);
				render0.setSeriesPaint(0, Color.BLUE);

				// configure the range axis to display directions...
				final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
				rangeAxis.setAutoRangeIncludesZero(true);
				rangeAxis.setRange(0, range1);

				plot.setRangeAxis(rangeAxis);
				final XYItemRenderer renderer2 = new XYAreaRenderer();
				final ValueAxis axis2 = new NumberAxis(bandch1[1]);
				axis2.setRange(0.0, range2);

				// �������ͼ��ɫ
				XYAreaRenderer xyarearenderer = new XYAreaRenderer();
				xyarearenderer.setSeriesPaint(1, new Color(0, 204, 0));
				xyarearenderer.setSeriesFillPaint(1, Color.GREEN);
				xyarearenderer.setPaint(Color.GREEN);
				plot.setDataset(1, (XYDataset) datasetV.get(1));
				plot.setRenderer(1, xyarearenderer);
				plot.setRangeAxis(1, axis2);
				plot.mapDatasetToRangeAxis(1, 1);

				LegendTitle legend = chart.getLegend();
				// legend.setItemFont(new Font("Verdena", 0, 9));
				// legend.setMargin(0, 0, 0, 0);
				JFreeChartBrother jfb = new JFreeChartBrother();
				jfb.setChart(chart);
				jfb.setWidth(w);
				jfb.setHeight(h);

				ResourceCenter.getInstance().getChartStorage().put(seriesKey, jfb);
			}
			hash = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return seriesKey;
	}

	private void p_draw_lineByHour(Hashtable hash, String title1, String title2, int w, int h) {
		List list = (List) hash.get("list");
		try {
			if (list == null || list.size() == 0) {
				draw_blank(title1, title2, w, h);
			} else {
				String unit = (String) hash.get("unit");
				if (unit == null)
					unit = "%";
				ChartGraph cg = new ChartGraph();
				TimeSeries ss = new TimeSeries(title1, Hour.class);
				TimeSeries[] s = { ss };
				System.out.println(list.size());
				Vector v0 = (Vector) list.get(0);
				String dt0 = (String) v0.get(1);
				SimpleDateFormat sdf0 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date time0 = sdf0.parse(dt0);
				Calendar temp0 = Calendar.getInstance();
				temp0.setTime(time0);
				for (int j = 0; j < list.size(); j++) {
					Vector v = (Vector) list.get(j);
					Double d = new Double((String) v.get(0));
					String dt = (String) v.get(1);
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Date time1 = sdf.parse(dt);
					Calendar temp = Calendar.getInstance();
					temp.setTime(time1);

					Hour hour = new Hour(temp.get(Calendar.HOUR_OF_DAY), temp.get(Calendar.DAY_OF_MONTH), temp.get(Calendar.MONTH) + 1, temp.get(Calendar.YEAR));
					if (temp.get(Calendar.HOUR_OF_DAY) > temp0.get(Calendar.HOUR_OF_DAY)) {
						ss.addOrUpdate(hour, d);
					}

					// ss.addOrUpdate(hour,d);
				}

				cg.timewave(s, "x(ʱ��)", "y(" + unit + ")", title1, title2, w, h);
			}
			hash = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void p_draw_lineByDay(Hashtable hash, String title1, String title2, int w, int h) {
		List list = (List) hash.get("list");
		try {
			if (list == null || list.size() == 0) {
				draw_blank(title1, title2, w, h);
			} else {
				String unit = (String) hash.get("unit");
				if (unit == null)
					unit = "%";
				ChartGraph cg = new ChartGraph();
				TimeSeries ss = new TimeSeries(title1, Day.class);
				TimeSeries[] s = { ss };

				for (int j = 0; j < list.size(); j++) {
					Vector v = (Vector) list.get(j);
					Double d = new Double((String) v.get(0));
					String dt = (String) v.get(1);
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Date time1 = sdf.parse(dt);
					Calendar temp = Calendar.getInstance();
					temp.setTime(time1);

					Day day = new Day(temp.get(Calendar.DAY_OF_MONTH), temp.get(Calendar.MONTH) + 1, temp.get(Calendar.YEAR));

					ss.addOrUpdate(day, d);
				}

				cg.timewave(s, "x(ʱ��)", "y(" + unit + ")", title1, title2, w, h);
			}
			hash = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void p_draw_lineByMonth(Hashtable hash, String title1, String title2, int w, int h) {
		List list = (List) hash.get("list");
		try {
			if (list == null || list.size() == 0) {
				draw_blank(title1, title2, w, h);
			} else {
				String unit = (String) hash.get("unit");
				if (unit == null)
					unit = "%";
				ChartGraph cg = new ChartGraph();
				TimeSeries ss = new TimeSeries(title1, Month.class);
				TimeSeries[] s = { ss };

				for (int j = 0; j < list.size(); j++) {
					Vector v = (Vector) list.get(j);
					Double d = new Double((String) v.get(0));
					String dt = (String) v.get(1);
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Date time1 = sdf.parse(dt);
					Calendar temp = Calendar.getInstance();
					temp.setTime(time1);

					Month month = new Month(temp.get(Calendar.MONTH) + 1, temp.get(Calendar.YEAR));

					ss.addOrUpdate(month, d);
				}

				cg.timewave(s, "x(ʱ��)", "y(" + unit + ")", title1, title2, w, h);
			}
			hash = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	private String storageHDS() {
		Date d = new Date();
		String startdate = getParaValue("startdate");
//		System.out.println(startdate+"*****************************************");
		if (startdate == null) {
			startdate = sdf0.format(d);
		}
		String todate = getParaValue("todate");
//		System.out.println("*****************************************"+todate);
		if (todate == null) {
			todate = sdf0.format(d);
		}
		String starttime = startdate + " 00:00:00";
		String totime = todate + " 23:59:59";
		String ids = getParaValue("id");
		String flag = (String) session.getAttribute("ids");
		if(flag != null && ids == null){
			ids = flag;
			request.getSession().removeAttribute("ids");
		}
		// �������־ȡ���˿����¼�¼���б�
		String orderflag = "ipaddress";
		if (getParaValue("orderflag") != null && !getParaValue("orderflag").equals("")) {
			orderflag = getParaValue("orderflag");
		}
 
		List orderList = new ArrayList();
		if (ids != null) {
			    session.setAttribute("ids", ids);
				HostNodeDao dao = new HostNodeDao();
				HostNode node = dao.loadHost(Integer.parseInt(ids));
                String ip =node.getIpAddress();
                if(node.getIpAddress().contains(".")){
                	ip=node.getIpAddress().replace(".", "_");
                }
				Hashtable pinghash = new Hashtable();
				try {
					pinghash = this.getCategory(ip, "rpower",  starttime, totime);
				} catch (Exception e) {
					e.printStackTrace();
				}
				Hashtable responsehash = new Hashtable();
				Hashtable ipmemhash = new Hashtable();
				ipmemhash.put("hostnode", node);
				ipmemhash.put("pinghash", pinghash);
				ipmemhash.put("ipaddress", node.getIpAddress() + "(" + node.getAlias() + ")");
				orderList.add(ipmemhash);
		}
		String pingChartDivStr = ReportHelper.getChartDivStr(orderList, "ping");
//		System.out.println("--------amchart---------"+pingChartDivStr);
//		String responsetimeDivStr = ReportHelper.getChartDivStr(orderList, "responsetime");
		// ��ͨ��
		ReportValue pingReportValue = ReportHelper.getReportValue(orderList, "ping");
	//	ReportValue responseReportValue = ReportHelper.getReportValue(orderList, "responsetime");
		String pingpath = new ReportExport().makeJfreeChartData(pingReportValue.getListValue(), pingReportValue.getIpList(), "��Դ״̬", "", "");
//		String responsetimepath = new ReportExport().makeJfreeChartData(responseReportValue.getListValue(), responseReportValue.getIpList(), "��Ӧʱ��", "ʱ��", "");
        System.out.println(pingReportValue.getListValue().size());
//		request.setAttribute("responsetimeDivStr", responsetimeDivStr);
		request.setAttribute("pingChartDivStr", pingChartDivStr);
		request.setAttribute("starttime", starttime);
		request.setAttribute("totime", totime);
		session.setAttribute("pingpath", pingpath);
//		session.setAttribute("responsetimepath", responsetimepath);
		session.setAttribute("orderList", orderList);
		request.setAttribute("startdate", startdate);
		request.setAttribute("todate", todate);
//		request.setAttribute("pinglist", list);
//		request.setAttribute("id", ids);
		return "/capreport/net/hdsPower.jsp";
	}
	
	java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	HostCollectDataManager manage = new HostCollectDataManager();
	public synchronized boolean createHostItemData(String ip, Hashtable datahash, String type, String subtype, String category) {
		DBManager dbmanager = null;
		try {
			dbmanager = new DBManager();
			Hashtable hash = ShareData.getOctetsdata(ip);

			String allipstr = SysUtil.doip(ip);
			if (type.equalsIgnoreCase("host")) {
				if ("cpu".equalsIgnoreCase(category)) {
					// CPU
					Vector cpuVector = (Vector) datahash.get("cpu");
					if (cpuVector != null && cpuVector.size() > 0) {
						// �õ�CPUƽ��
						CPUcollectdata cpudata = (CPUcollectdata) cpuVector.elementAt(0);
						if (cpudata.getRestype().equals("dynamic")) {
							Calendar tempCal = (Calendar) cpudata.getCollecttime();
							Date cc = tempCal.getTime();
							String time = DbConversionUtil.coversionTimeSql(sdf.format(cc));
							String tablename = "cpu" + allipstr;
							String sql = "";
							if("mysql".equalsIgnoreCase(SystemConstant.DBType)){
								sql = "insert into " + tablename + "(ipaddress,restype,category,entity,subentity,unit,chname,bak,count,thevalue,collecttime) " + "values('" + ip + "','"
								+ cpudata.getRestype() + "','" + cpudata.getCategory() + "','" + cpudata.getEntity() + "','" + cpudata.getSubentity() + "','" + cpudata.getUnit() + "','"
								+ cpudata.getChname() + "','" + cpudata.getBak() + "'," + cpudata.getCount() + ",'" + cpudata.getThevalue() + "','" + time + "')";
							}else if("oracle".equalsIgnoreCase(SystemConstant.DBType)){
								sql = "insert into " + tablename + "(ipaddress,restype,category,entity,subentity,unit,chname,bak,count,thevalue,collecttime) " + "values('" + ip + "','"
								+ cpudata.getRestype() + "','" + cpudata.getCategory() + "','" + cpudata.getEntity() + "','" + cpudata.getSubentity() + "','" + cpudata.getUnit() + "','"
								+ cpudata.getChname() + "','" + cpudata.getBak() + "'," + cpudata.getCount() + ",'" + cpudata.getThevalue() + "',to_date('"+time+"','YYYY-MM-DD HH24:MI:SS'))";
							}							
							// System.out.println(sql);
							try {
								dbmanager.executeUpdate(sql);
							} catch (Exception ex) {
								ex.printStackTrace();
							} finally {
								// dbmanager.close();
							}
							// conn.executeUpdate(sql);
						}
					}
				} else if ("process".equalsIgnoreCase(category)) {
					// Process
					Vector proVector = (Vector) datahash.get("process");


					if (proVector != null && proVector.size() > 0) {
						for (int i = 0; i < proVector.size(); i++) {
							Processcollectdata processdata = (Processcollectdata) proVector.elementAt(i);
							processdata.setCount(null);
							if (processdata.getRestype().equals("dynamic")) {
								Calendar tempCal = (Calendar) processdata.getCollecttime();
								Date cc = tempCal.getTime();
								String time = DbConversionUtil.coversionTimeSql(sdf.format(cc));
								String tablename = "pro" + allipstr;
								String sql = "";
								if("mysql".equalsIgnoreCase(SystemConstant.DBType)){
									sql = "insert into " + tablename + "(ipaddress,restype,category,entity,subentity,unit,chname,bak,count,thevalue,collecttime) " + "values('" + ip + "','"
									+ processdata.getRestype() + "','" + processdata.getCategory() + "','" + processdata.getEntity() + "','" + processdata.getSubentity() + "','"
									+ processdata.getUnit() + "','" + processdata.getChname() + "','" + processdata.getBak() + "'," + processdata.getCount() + ",'" + processdata.getThevalue()
									+ "','" + time + "')";
								}else if("oracle".equalsIgnoreCase(SystemConstant.DBType)){
									sql = "insert into " + tablename + "(ipaddress,restype,category,entity,subentity,unit,chname,bak,count,thevalue,collecttime) " + "values('" + ip + "','"
									+ processdata.getRestype() + "','" + processdata.getCategory() + "','" + processdata.getEntity() + "','" + processdata.getSubentity() + "','"
									+ processdata.getUnit() + "','" + processdata.getChname() + "','" + processdata.getBak() + "'," + processdata.getCount() + ",'" + processdata.getThevalue()
									+ "',to_date('"+time+"','YYYY-MM-DD HH24:MI:SS'))";
								}								
								// System.out.println(sql);
								try {
									dbmanager.executeUpdate(sql);
								} catch (Exception ex) {
									ex.printStackTrace();
								}

							}
						}
					}
				} else if ("physicalmemory".equalsIgnoreCase(category)) {
					// physicalmemory
					Vector memoryVector = (Vector) datahash.get("memory");
					if (memoryVector != null && memoryVector.size() > 0) {
						for (int si = 0; si < memoryVector.size(); si++) {
							Memorycollectdata memorydata = (Memorycollectdata) memoryVector.elementAt(si);
							if (!memorydata.getSubentity().equalsIgnoreCase("PhysicalMemory"))
								continue;
							if (memorydata.getRestype().equals("dynamic")) {
								Calendar tempCal = (Calendar) memorydata.getCollecttime();
								Date cc = tempCal.getTime();
								String time = DbConversionUtil.coversionTimeSql(sdf.format(cc));
								String tablename = "memory" + allipstr;
								// if
								// (memorydata.getIpaddress().equals("10.217.255.253")||memorydata.getIpaddress().equals("10.217.255.64")||memorydata.getIpaddress().equals("10.216.2.35")){
								String sql ="";
								if("mysql".equalsIgnoreCase(SystemConstant.DBType)){
									sql = "insert into " + tablename + "(ipaddress,restype,category,entity,subentity,unit,chname,bak,count,thevalue,collecttime) " + "values('" + ip + "','"
									+ memorydata.getRestype() + "','" + memorydata.getCategory() + "','" + memorydata.getEntity() + "','" + memorydata.getSubentity() + "','"
									+ memorydata.getUnit() + "','" + memorydata.getChname() + "','" + memorydata.getBak() + "'," + memorydata.getCount() + ",'" + memorydata.getThevalue() + "','"
									+ time + "')";
								}else if("oracle".equalsIgnoreCase(SystemConstant.DBType)){
									sql = "insert into " + tablename + "(ipaddress,restype,category,entity,subentity,unit,chname,bak,count,thevalue,collecttime) " + "values('" + ip + "','"
									+ memorydata.getRestype() + "','" + memorydata.getCategory() + "','" + memorydata.getEntity() + "','" + memorydata.getSubentity() + "','"
									+ memorydata.getUnit() + "','" + memorydata.getChname() + "','" + memorydata.getBak() + "'," + memorydata.getCount() + ",'" + memorydata.getThevalue() + "',"+time+")";
								}							
								System.out.println(sql);
								try {
									dbmanager.executeUpdate(sql);
								} catch (Exception ex) {
									ex.printStackTrace();
								}
							}
						}

					}
				} else if ("virtualmemory".equalsIgnoreCase(category)) {
					// physicalmemory
					Vector memoryVector = (Vector) datahash.get("memory");
					if (memoryVector != null && memoryVector.size() > 0) {
						for (int si = 0; si < memoryVector.size(); si++) {
							Memorycollectdata memorydata = (Memorycollectdata) memoryVector.elementAt(si);
							if (!memorydata.getSubentity().equalsIgnoreCase("VirtualMemory"))
								continue;
							if (memorydata.getRestype().equals("dynamic")) {
								Calendar tempCal = (Calendar) memorydata.getCollecttime();
								Date cc = tempCal.getTime();
								String time = DbConversionUtil.coversionTimeSql(sdf.format(cc));
								String tablename = "memory" + allipstr;
								// if
								// (memorydata.getIpaddress().equals("10.217.255.253")||memorydata.getIpaddress().equals("10.217.255.64")||memorydata.getIpaddress().equals("10.216.2.35")){
								String sql ="";
								if("mysql".equalsIgnoreCase(SystemConstant.DBType)){
									sql = "insert into " + tablename + "(ipaddress,restype,category,entity,subentity,unit,chname,bak,count,thevalue,collecttime) " + "values('" + ip + "','"
									+ memorydata.getRestype() + "','" + memorydata.getCategory() + "','" + memorydata.getEntity() + "','" + memorydata.getSubentity() + "','"
									+ memorydata.getUnit() + "','" + memorydata.getChname() + "','" + memorydata.getBak() + "'," + memorydata.getCount() + ",'" + memorydata.getThevalue() + "','"
									+ time + "')";
								}else if("oracle".equalsIgnoreCase(SystemConstant.DBType)){
									sql = "insert into " + tablename + "(ipaddress,restype,category,entity,subentity,unit,chname,bak,count,thevalue,collecttime) " + "values('" + ip + "','"
									+ memorydata.getRestype() + "','" + memorydata.getCategory() + "','" + memorydata.getEntity() + "','" + memorydata.getSubentity() + "','"
									+ memorydata.getUnit() + "','" + memorydata.getChname() + "','" + memorydata.getBak() + "'," + memorydata.getCount() + ",'" + memorydata.getThevalue() + "',"+time+")";
								}
								
								// System.out.println(sql);
								try {
									dbmanager.executeUpdate(sql);
								} catch (Exception ex) {
									ex.printStackTrace();
								}
							}
						}

					}
				} else if ("disk".equalsIgnoreCase(category)) {
					Vector diskVector = (Vector) datahash.get("disk");
					if (diskVector != null && diskVector.size() > 0) {
						for (int si = 0; si < diskVector.size(); si++) {
							Diskcollectdata diskdata = (Diskcollectdata) diskVector.elementAt(si);
							if (diskdata.getRestype().equals("dynamic")) {
								Calendar tempCal = (Calendar) diskdata.getCollecttime();
								Date cc = tempCal.getTime();
								String time = DbConversionUtil.coversionTimeSql(sdf.format(cc));
								String tablename = "diskincre" + allipstr;
								String sql = "";
								if("mysql".equalsIgnoreCase(SystemConstant.DBType)){
									sql = "insert into " + tablename + "(ipaddress,category,subentity,restype,entity,thevalue,collecttime,unit) " + "values('" + ip + "','" + diskdata.getCategory()
									+ "','" + diskdata.getSubentity() + "','" + diskdata.getRestype() + "','" + diskdata.getEntity() + "','" + diskdata.getThevalue() + "','" + time + "','"
									+ diskdata.getUnit() + "')";
								}else if("oracle".equalsIgnoreCase(SystemConstant.DBType)){
									sql = "insert into " + tablename + "(ipaddress,category,subentity,restype,entity,thevalue,collecttime,unit) " + "values('" + ip + "','" + diskdata.getCategory()
									+ "','" + diskdata.getSubentity() + "','" + diskdata.getRestype() + "','" + diskdata.getEntity() + "','" + diskdata.getThevalue() + "',to_date('"+time+"','YYYY-MM-DD HH24:MI:SS'),'"
									+ diskdata.getUnit() + "')";
								}
								
								try {
									dbmanager.executeUpdate(sql);
								} catch (Exception ex) {
									ex.printStackTrace();
								}
							}
							if (diskdata.getEntity().equals("Utilization")) {
								Calendar tempCal = (Calendar) diskdata.getCollecttime();
								Date cc = tempCal.getTime();
								String time = DbConversionUtil.coversionTimeSql(sdf.format(cc));
								String tablename = "disk" + allipstr;
								String sql = "";
								if("mysql".equalsIgnoreCase(SystemConstant.DBType)){
									sql = "insert into " + tablename + "(ipaddress,category,subentity,restype,entity,thevalue,collecttime,unit) " + "values('" + ip + "','" + diskdata.getCategory()
									+ "','" + diskdata.getSubentity() + "','" + diskdata.getRestype() + "','" + diskdata.getEntity() + "','" + diskdata.getThevalue() + "','" + time + "','"
									+ diskdata.getUnit() + "')";
								}else if("oracle".equalsIgnoreCase(SystemConstant.DBType)){
									sql = "insert into " + tablename + "(ipaddress,category,subentity,restype,entity,thevalue,collecttime,unit) " + "values('" + ip + "','" + diskdata.getCategory()
									+ "','" + diskdata.getSubentity() + "','" + diskdata.getRestype() + "','" + diskdata.getEntity() + "','" + diskdata.getThevalue() + "',to_date('"+time+"','YYYY-MM-DD HH24:MI:SS'),'"
									+ diskdata.getUnit() + "')";
								}								
								try {
									dbmanager.executeUpdate(sql);
								} catch (Exception ex) {
									ex.printStackTrace();
								}
							}
						}
					}
				} else if ("interface".equalsIgnoreCase(category)) {
					// UtilHdx
					Vector utilhdxVector = (Vector) datahash.get("utilhdx");
					if (utilhdxVector != null && utilhdxVector.size() > 0) {
						for (int si = 0; si < utilhdxVector.size(); si++) {
							UtilHdx utilhdx = (UtilHdx) utilhdxVector.elementAt(si);

							if (utilhdx.getRestype().equals("dynamic")) {
								// session.save(utilhdx);
								if (utilhdx.getThevalue().equals("0"))
									continue;
								Calendar tempCal = (Calendar) utilhdx.getCollecttime();
								Date cc = tempCal.getTime();
								String time = DbConversionUtil.coversionTimeSql(sdf.format(cc));
								String tablename = "utilhdx" + allipstr;
								String sql = "";
								if("mysql".equalsIgnoreCase(SystemConstant.DBType)){
									sql = "insert into " + tablename + "(ipaddress,restype,category,entity,subentity,unit,chname,bak,count,thevalue,collecttime) " + "values('" + ip + "','"
									+ utilhdx.getRestype() + "','" + utilhdx.getCategory() + "','" + utilhdx.getEntity() + "','" + utilhdx.getSubentity() + "','" + utilhdx.getUnit() + "','"
									+ utilhdx.getChname() + "','" + utilhdx.getBak() + "'," + utilhdx.getCount() + ",'" + utilhdx.getThevalue() + "','" + time + "')";
								}else if("oracle".equalsIgnoreCase(SystemConstant.DBType)){
									sql = "insert into " + tablename + "(ipaddress,restype,category,entity,subentity,unit,chname,bak,count,thevalue,collecttime) " + "values('" + ip + "','"
									+ utilhdx.getRestype() + "','" + utilhdx.getCategory() + "','" + utilhdx.getEntity() + "','" + utilhdx.getSubentity() + "','" + utilhdx.getUnit() + "','"
									+ utilhdx.getChname() + "','" + utilhdx.getBak() + "'," + utilhdx.getCount() + ",'" + utilhdx.getThevalue() + "',to_date('"+time+"','YYYY-MM-DD HH24:MI:SS'))";
								}								
								// System.out.println(sql);
								try {
									dbmanager.executeUpdate(sql);
								} catch (Exception ex) {
									ex.printStackTrace();
								}
								// conn.executeUpdate(sql);
							}
						}
					}
				}
			} else if (type.equalsIgnoreCase("net")) {
				// ���������豸������
				if ("cpu".equalsIgnoreCase(category)) {
					// CPU
					Vector cpuVector = (Vector) datahash.get("cpu");
					if (cpuVector != null && cpuVector.size() > 0) {
						// �õ�CPUƽ��
						CPUcollectdata cpudata = (CPUcollectdata) cpuVector.elementAt(0);
						if (cpudata.getRestype().equals("dynamic")) {
							Calendar tempCal = (Calendar) cpudata.getCollecttime();
							Date cc = tempCal.getTime();
							String time = DbConversionUtil.coversionTimeSql(sdf.format(cc));
							String tablename = "cpu" + allipstr;
							String sql  ="";
							if("mysql".equalsIgnoreCase(SystemConstant.DBType)){
								sql = "insert into " + tablename + "(ipaddress,restype,category,entity,subentity,unit,chname,bak,count,thevalue,collecttime) " + "values('" + ip + "','"
								+ cpudata.getRestype() + "','" + cpudata.getCategory() + "','" + cpudata.getEntity() + "','" + cpudata.getSubentity() + "','" + cpudata.getUnit() + "','"
								+ cpudata.getChname() + "','" + cpudata.getBak() + "'," + cpudata.getCount() + ",'" + cpudata.getThevalue() + "','" + time + "')";
							}else if("oracle".equalsIgnoreCase(SystemConstant.DBType)){
								sql = "insert into " + tablename + "(ipaddress,restype,category,entity,subentity,unit,chname,bak,count,thevalue,collecttime) " + "values('" + ip + "','"
								+ cpudata.getRestype() + "','" + cpudata.getCategory() + "','" + cpudata.getEntity() + "','" + cpudata.getSubentity() + "','" + cpudata.getUnit() + "','"
								+ cpudata.getChname() + "','" + cpudata.getBak() + "'," + cpudata.getCount() + ",'" + cpudata.getThevalue() + "',to_date('"+time+"','YYYY-MM-DD HH24:MI:SS'))";
							}
							
							try {
								dbmanager.executeUpdate(sql);
							} catch (Exception ex) {
								ex.printStackTrace();
							} finally {
							}
						}
					}
				} else if ("memory".equalsIgnoreCase(category)) {
					Vector memoryVector = (Vector) datahash.get("memory");
					if (memoryVector != null && memoryVector.size() > 0) {
						for (int si = 0; si < memoryVector.size(); si++) {
							Memorycollectdata memorydata = (Memorycollectdata) memoryVector.elementAt(si);
							if (memorydata.getRestype().equals("dynamic")) {
								Calendar tempCal = (Calendar) memorydata.getCollecttime();
								Date cc = tempCal.getTime();
								String time = DbConversionUtil.coversionTimeSql(sdf.format(cc));
								String tablename = "memory" + allipstr;
								String sql = "";
								if("mysql".equalsIgnoreCase(SystemConstant.DBType)){
									sql = "insert into " + tablename + "(ipaddress,restype,category,entity,subentity,unit,chname,bak,count,thevalue,collecttime) " + "values('" + ip + "','"
									+ memorydata.getRestype() + "','" + memorydata.getCategory() + "','" + memorydata.getEntity() + "','" + memorydata.getSubentity() + "','"
									+ memorydata.getUnit() + "','" + memorydata.getChname() + "','" + memorydata.getBak() + "'," + memorydata.getCount() + ",'" + memorydata.getThevalue() + "','"
									+ time + "')";
								}else if("oracle".equalsIgnoreCase(SystemConstant.DBType)){
									sql = "insert into " + tablename + "(ipaddress,restype,category,entity,subentity,unit,chname,bak,count,thevalue,collecttime) " + "values('" + ip + "','"
									+ memorydata.getRestype() + "','" + memorydata.getCategory() + "','" + memorydata.getEntity() + "','" + memorydata.getSubentity() + "','"
									+ memorydata.getUnit() + "','" + memorydata.getChname() + "','" + memorydata.getBak() + "'," + memorydata.getCount() + ",'" + memorydata.getThevalue() + "',to_date('"+time+"','YYYY-MM-DD HH24:MI:SS'))";
								}								
								try {
									dbmanager.executeUpdate(sql);
								} catch (Exception ex) {
									ex.printStackTrace();
								}
							}
						}

					}
				} else if ("flash".equalsIgnoreCase(category)) {
					// ����
					Vector flashVector = (Vector) datahash.get("flash");
					if (flashVector != null && flashVector.size() > 0) {
						Flashcollectdata flashdata = (Flashcollectdata) flashVector.elementAt(0);
						if (flashdata.getRestype().equals("dynamic")) {
							// session.save(cpudata);
							Calendar tempCal = (Calendar) flashdata.getCollecttime();
							Date cc = tempCal.getTime();
							String time = DbConversionUtil.coversionTimeSql(sdf.format(cc));
							String tablename = "flash" + allipstr;
							String sql = "";
							if("mysql".equalsIgnoreCase(SystemConstant.DBType)){
								sql = "insert into " + tablename + "(ipaddress,restype,category,entity,subentity,unit,chname,bak,count,thevalue,collecttime) " + "values('" + ip + "','"
								+ flashdata.getRestype() + "','" + flashdata.getCategory() + "','" + flashdata.getEntity() + "','" + flashdata.getSubentity() + "','" + flashdata.getUnit() + "','"
								+ flashdata.getChname() + "','" + flashdata.getBak() + "'," + flashdata.getCount() + ",'" + flashdata.getThevalue() + "','" + time + "')";
							}else if("oracle".equalsIgnoreCase(SystemConstant.DBType)){
								sql = "insert into " + tablename + "(ipaddress,restype,category,entity,subentity,unit,chname,bak,count,thevalue,collecttime) " + "values('" + ip + "','"
								+ flashdata.getRestype() + "','" + flashdata.getCategory() + "','" + flashdata.getEntity() + "','" + flashdata.getSubentity() + "','" + flashdata.getUnit() + "','"
								+ flashdata.getChname() + "','" + flashdata.getBak() + "'," + flashdata.getCount() + ",'" + flashdata.getThevalue() + "',to_date('"+time+"','YYYY-MM-DD HH24:MI:SS'))";
							}
							
							// System.out.println(sql);
							try {
								dbmanager.executeUpdate(sql);
							} catch (Exception ex) {
								ex.printStackTrace();
							} finally {
								// dbmanager.close();
							}
							// conn.executeUpdate(sql);
						}
					}
				} else if ("temperature".equalsIgnoreCase(category)) {
					// �¶�
					Vector temperatureVector = (Vector) datahash.get("temperature");
					if (temperatureVector != null && temperatureVector.size() > 0) {
						// �õ��¶�
						Interfacecollectdata temperdata = (Interfacecollectdata) temperatureVector.elementAt(0);
						if (temperdata.getRestype().equals("dynamic")) {
							// session.save(cpudata);
							Calendar tempCal = (Calendar) temperdata.getCollecttime();
							Date cc = tempCal.getTime();
							String time = DbConversionUtil.coversionTimeSql(sdf.format(cc));
							String tablename = "temper" + allipstr;
							String sql = "";
							if("mysql".equalsIgnoreCase(SystemConstant.DBType)){
								sql = "insert into " + tablename + "(ipaddress,restype,category,entity,subentity,unit,chname,bak,count,thevalue,collecttime) " + "values('" + ip + "','"
								+ temperdata.getRestype() + "','" + temperdata.getCategory() + "','" + temperdata.getEntity() + "','" + temperdata.getSubentity() + "','" + temperdata.getUnit()
								+ "','" + temperdata.getChname() + "','" + temperdata.getBak() + "'," + temperdata.getCount() + ",'" + temperdata.getThevalue() + "','" + time + "')";
							}else if("oracle".equalsIgnoreCase(SystemConstant.DBType)){
								sql = "insert into " + tablename + "(ipaddress,restype,category,entity,subentity,unit,chname,bak,count,thevalue,collecttime) " + "values('" + ip + "','"
								+ temperdata.getRestype() + "','" + temperdata.getCategory() + "','" + temperdata.getEntity() + "','" + temperdata.getSubentity() + "','" + temperdata.getUnit()
								+ "','" + temperdata.getChname() + "','" + temperdata.getBak() + "'," + temperdata.getCount() + ",'" + temperdata.getThevalue() + "',to_date('"+time+"','YYYY-MM-DD HH24:MI:SS'))";
							}							
							// System.out.println(sql);
							try {
								dbmanager.executeUpdate(sql);
							} catch (Exception ex) {
								ex.printStackTrace();
							} finally {
								// dbmanager.close();
							}
							// conn.executeUpdate(sql);
						}
					}
				} else if ("fan".equalsIgnoreCase(category)) {
					// ����
					Vector fanVector = (Vector) datahash.get("fan");
					if (fanVector != null && fanVector.size() > 0) {
						// �õ�����
						Interfacecollectdata fandata = (Interfacecollectdata) fanVector.elementAt(0);
						if (fandata.getRestype().equals("dynamic")) {
							// session.save(cpudata);
							Calendar tempCal = (Calendar) fandata.getCollecttime();
							Date cc = tempCal.getTime();
							String time = DbConversionUtil.coversionTimeSql(sdf.format(cc));
							String tablename = "fan" + allipstr;
							String sql  = "";
							if("mysql".equalsIgnoreCase(SystemConstant.DBType)){
								sql = "insert into " + tablename + "(ipaddress,restype,category,entity,subentity,unit,chname,bak,count,thevalue,collecttime) " + "values('" + ip + "','"
								+ fandata.getRestype() + "','" + fandata.getCategory() + "','" + fandata.getEntity() + "','" + fandata.getSubentity() + "','" + fandata.getUnit() + "','"
								+ fandata.getChname() + "','" + fandata.getBak() + "'," + fandata.getCount() + ",'" + fandata.getThevalue() + "','" + time + "')";
							}else if("oracle".equalsIgnoreCase(SystemConstant.DBType)){
								sql = "insert into " + tablename + "(ipaddress,restype,category,entity,subentity,unit,chname,bak,count,thevalue,collecttime) " + "values('" + ip + "','"
								+ fandata.getRestype() + "','" + fandata.getCategory() + "','" + fandata.getEntity() + "','" + fandata.getSubentity() + "','" + fandata.getUnit() + "','"
								+ fandata.getChname() + "','" + fandata.getBak() + "'," + fandata.getCount() + ",'" + fandata.getThevalue() + "',to_date('"+time+"','YYYY-MM-DD HH24:MI:SS'))";
							}
							// System.out.println(sql);
							try {
								dbmanager.executeUpdate(sql);
							} catch (Exception ex) {
								ex.printStackTrace();
							} finally {
								// dbmanager.close();
							}
							// conn.executeUpdate(sql);
						}
					}
				} else if ("power".equalsIgnoreCase(category)) {
					// ��Դ
					// SysLogger.info("��ӵ�Դ��Ϣ ################"+allipstr);
					Vector powerVector = (Vector) datahash.get("power");
					if (powerVector != null && powerVector.size() > 0) {
						for (int i = 0; i < powerVector.size(); i++) {
							Interfacecollectdata powerdata = (Interfacecollectdata) powerVector.elementAt(i);
							if (powerdata.getRestype().equals("dynamic")) {
								// session.save(cpudata);
								Calendar tempCal = (Calendar) powerdata.getCollecttime();
								Date cc = tempCal.getTime();
								String time = DbConversionUtil.coversionTimeSql(sdf.format(cc));
								String tablename = "power" + allipstr;
								String sql = "";
								if("mysql".equalsIgnoreCase(SystemConstant.DBType)){
									sql = "insert into " + tablename + "(ipaddress,restype,category,entity,subentity,unit,chname,bak,count,thevalue,collecttime) " + "values('" + ip + "','"
									+ powerdata.getRestype() + "','" + powerdata.getCategory() + "','" + powerdata.getEntity() + "','" + powerdata.getSubentity() + "','" + powerdata.getUnit()
									+ "','" + powerdata.getChname() + "','" + powerdata.getBak() + "'," + powerdata.getCount() + ",'" + powerdata.getThevalue() + "','" + time + "')";
								}else if("oracle".equalsIgnoreCase(SystemConstant.DBType)){
									sql = "insert into " + tablename + "(ipaddress,restype,category,entity,subentity,unit,chname,bak,count,thevalue,collecttime) " + "values('" + ip + "','"
									+ powerdata.getRestype() + "','" + powerdata.getCategory() + "','" + powerdata.getEntity() + "','" + powerdata.getSubentity() + "','" + powerdata.getUnit()
									+ "','" + powerdata.getChname() + "','" + powerdata.getBak() + "'," + powerdata.getCount() + ",'" + powerdata.getThevalue() + "',to_date('"+time+"','YYYY-MM-DD HH24:MI:SS'))";
								}								
								// SysLogger.info(sql);
								try {
									dbmanager.executeUpdate(sql);
								} catch (Exception ex) {
									ex.printStackTrace();
								} finally {
									// dbmanager.close();
								}
								// conn.executeUpdate(sql);
							}
						}
					}
				} else if ("voltage".equalsIgnoreCase(category)) {
					// ��Դ
					Vector voltageVector = (Vector) datahash.get("voltage");
					if (voltageVector != null && voltageVector.size() > 0) {
						for (int i = 0; i < voltageVector.size(); i++) {
							Interfacecollectdata powerdata = (Interfacecollectdata) voltageVector.elementAt(i);
							if (powerdata.getRestype().equals("dynamic")) {
								// session.save(cpudata);
								Calendar tempCal = (Calendar) powerdata.getCollecttime();
								Date cc = tempCal.getTime();
								String time = DbConversionUtil.coversionTimeSql(sdf.format(cc));
								String tablename = "vol" + allipstr;
								String sql  = "";
								if("mysql".equalsIgnoreCase(SystemConstant.DBType)){
									sql = "insert into " + tablename + "(ipaddress,restype,category,entity,subentity,unit,chname,bak,count,thevalue,collecttime) " + "values('" + ip + "','"
									+ powerdata.getRestype() + "','" + powerdata.getCategory() + "','" + powerdata.getEntity() + "','" + powerdata.getSubentity() + "','" + powerdata.getUnit()
									+ "','" + powerdata.getChname() + "','" + powerdata.getBak() + "'," + powerdata.getCount() + ",'" + powerdata.getThevalue() + "','" + time + "')";
								}else if("oracle".equalsIgnoreCase(SystemConstant.DBType)){
									sql = "insert into " + tablename + "(ipaddress,restype,category,entity,subentity,unit,chname,bak,count,thevalue,collecttime) " + "values('" + ip + "','"
									+ powerdata.getRestype() + "','" + powerdata.getCategory() + "','" + powerdata.getEntity() + "','" + powerdata.getSubentity() + "','" + powerdata.getUnit()
									+ "','" + powerdata.getChname() + "','" + powerdata.getBak() + "'," + powerdata.getCount() + ",'" + powerdata.getThevalue() + "',to_date('"+time+"','YYYY-MM-DD HH24:MI:SS'))";
								}							
								// SysLogger.info(sql);
								try {
									dbmanager.executeUpdate(sql);
								} catch (Exception ex) {
									ex.printStackTrace();
								} finally {
									// dbmanager.close();
								}
								// conn.executeUpdate(sql);
							}
						}
					}
				}
			}
			if ("interface".equalsIgnoreCase(category)) {
				// SysLogger.info("###########��ʼ����ӿ�����############");
				// AllUtilHdx
				Vector allutilhdxVector = (Vector) datahash.get("allutilhdx");
				if (allutilhdxVector != null && allutilhdxVector.size() > 0) {
					for (int si = 0; si < allutilhdxVector.size(); si++) {
						AllUtilHdx allutilhdx = (AllUtilHdx) allutilhdxVector.elementAt(si);

						if (allutilhdx.getRestype().equals("dynamic")) {
							if (allutilhdx.getThevalue().equals("0"))
								continue;
							Calendar tempCal = (Calendar) allutilhdx.getCollecttime();
							Date cc = tempCal.getTime();
							String time = DbConversionUtil.coversionTimeSql(sdf.format(cc));
							String tablename = "allutilhdx" + allipstr;
							String sql = "";
							if("mysql".equalsIgnoreCase(SystemConstant.DBType)){
								sql = "insert into " + tablename + "(ipaddress,restype,category,entity,subentity,unit,chname,bak,count,thevalue,collecttime) " + "values('" + ip + "','"
								+ allutilhdx.getRestype() + "','" + allutilhdx.getCategory() + "','" + allutilhdx.getEntity() + "','" + allutilhdx.getSubentity() + "','" + allutilhdx.getUnit()
								+ "','" + allutilhdx.getChname() + "','" + allutilhdx.getBak() + "'," + allutilhdx.getCount() + ",'" + allutilhdx.getThevalue() + "','" + time + "')";
							}else if("oracle".equalsIgnoreCase(SystemConstant.DBType)){
								sql = "insert into " + tablename + "(ipaddress,restype,category,entity,subentity,unit,chname,bak,count,thevalue,collecttime) " + "values('" + ip + "','"
								+ allutilhdx.getRestype() + "','" + allutilhdx.getCategory() + "','" + allutilhdx.getEntity() + "','" + allutilhdx.getSubentity() + "','" + allutilhdx.getUnit()
								+ "','" + allutilhdx.getChname() + "','" + allutilhdx.getBak() + "'," + allutilhdx.getCount() + ",'" + allutilhdx.getThevalue() + "',to_date('"+time+"','YYYY-MM-DD HH24:MI:SS'))";
							}
							
							// System.out.println(sql);
							try {
								dbmanager.executeUpdate(sql);
							} catch (Exception ex) {
								ex.printStackTrace();
							}
						}
					}
				}

				// UtilHdxPerc
				Vector utilhdxpercVector = (Vector) datahash.get("utilhdxperc");
				if (utilhdxpercVector != null && utilhdxpercVector.size() > 0) {
					for (int si = 0; si < utilhdxpercVector.size(); si++) {
						UtilHdxPerc utilhdxperc = (UtilHdxPerc) utilhdxpercVector.elementAt(si);

						if (utilhdxperc.getRestype().equals("dynamic")) {
							// session.save(utilhdx);
							if (utilhdxperc.getThevalue().equals("0"))
								continue;
							Calendar tempCal = (Calendar) utilhdxperc.getCollecttime();
							Date cc = tempCal.getTime();
							String time = DbConversionUtil.coversionTimeSql(sdf.format(cc));
							String tablename = "utilhdxperc" + allipstr;
							String sql = "";
							if("mysql".equalsIgnoreCase(SystemConstant.DBType)){
								sql = "insert into " + tablename + "(ipaddress,restype,category,entity,subentity,unit,chname,bak,count,thevalue,collecttime) " + "values('" + ip + "','"
								+ utilhdxperc.getRestype() + "','" + utilhdxperc.getCategory() + "','" + utilhdxperc.getEntity() + "','" + utilhdxperc.getSubentity() + "','"
								+ utilhdxperc.getUnit() + "','" + utilhdxperc.getChname() + "','" + utilhdxperc.getBak() + "'," + utilhdxperc.getCount() + ",'" + utilhdxperc.getThevalue() + "','"
								+ time + "')";
							}else if("oracle".equalsIgnoreCase(SystemConstant.DBType)){
								sql = "insert into " + tablename + "(ipaddress,restype,category,entity,subentity,unit,chname,bak,count,thevalue,collecttime) " + "values('" + ip + "','"
								+ utilhdxperc.getRestype() + "','" + utilhdxperc.getCategory() + "','" + utilhdxperc.getEntity() + "','" + utilhdxperc.getSubentity() + "','"
								+ utilhdxperc.getUnit() + "','" + utilhdxperc.getChname() + "','" + utilhdxperc.getBak() + "'," + utilhdxperc.getCount() + ",'" + utilhdxperc.getThevalue() + "',to_date('"+time+"','YYYY-MM-DD HH24:MI:SS'))";
							}							
							try {
								dbmanager.executeUpdate(sql);
							} catch (Exception ex) {
								ex.printStackTrace();
							}
							// conn.executeUpdate(sql);
						}
					}
				}

				// UtilHdx
				Vector utilhdxVector = (Vector) datahash.get("utilhdx");
				// SysLogger.info("��ַΪ"+ip+"�����ٹ��� "+utilhdxVector.size()+"��");
				if (utilhdxVector != null && utilhdxVector.size() > 0) {
					for (int si = 0; si < utilhdxVector.size(); si++) {
						UtilHdx utilhdx = (UtilHdx) utilhdxVector.elementAt(si);

						if (utilhdx.getRestype().equals("dynamic")) {
							// session.save(utilhdx);
							if (utilhdx.getThevalue().equals("0"))
								continue;
							Calendar tempCal = (Calendar) utilhdx.getCollecttime();
							Date cc = tempCal.getTime();
							String time = DbConversionUtil.coversionTimeSql(sdf.format(cc));
							String tablename = "utilhdx" + allipstr;
							String sql = "";
							if("mysql".equalsIgnoreCase(SystemConstant.DBType)){
								sql = "insert into " + tablename + "(ipaddress,restype,category,entity,subentity,unit,chname,bak,count,thevalue,collecttime) " + "values('" + ip + "','"
								+ utilhdx.getRestype() + "','" + utilhdx.getCategory() + "','" + utilhdx.getEntity() + "','" + utilhdx.getSubentity() + "','" + utilhdx.getUnit() + "','"
								+ utilhdx.getChname() + "','" + utilhdx.getBak() + "'," + utilhdx.getCount() + ",'" + utilhdx.getThevalue() + "','" + time + "')";
							}else if("oracle".equalsIgnoreCase(SystemConstant.DBType)){
								sql = "insert into " + tablename + "(ipaddress,restype,category,entity,subentity,unit,chname,bak,count,thevalue,collecttime) " + "values('" + ip + "','"
								+ utilhdx.getRestype() + "','" + utilhdx.getCategory() + "','" + utilhdx.getEntity() + "','" + utilhdx.getSubentity() + "','" + utilhdx.getUnit() + "','"
								+ utilhdx.getChname() + "','" + utilhdx.getBak() + "'," + utilhdx.getCount() + ",'" + utilhdx.getThevalue() + "',to_date('"+time+"','YYYY-MM-DD HH24:MI:SS'))";
							}							
							// System.out.println(sql);
							try {
								dbmanager.executeUpdate(sql);
							} catch (Exception ex) {
								ex.printStackTrace();
							}
							// conn.executeUpdate(sql);
						}
					}
				}
				// discardsperc
				Vector discardspercVector = (Vector) datahash.get("discardsperc");
				if (discardspercVector != null && discardspercVector.size() > 0) {
					for (int si = 0; si < discardspercVector.size(); si++) {
						DiscardsPerc discardsperc = (DiscardsPerc) discardspercVector.elementAt(si);

						if (discardsperc.getRestype().equals("dynamic")) {
							if (discardsperc.getThevalue().equals("0.0"))
								continue;
							// session.save(discardsperc);
							Calendar tempCal = (Calendar) discardsperc.getCollecttime();
							Date cc = tempCal.getTime();
							String time = DbConversionUtil.coversionTimeSql(sdf.format(cc));
							String tablename = "discardsperc" + allipstr;
							String sql = "";
							if("mysql".equalsIgnoreCase(SystemConstant.DBType)){
								sql = "insert into " + tablename + "(ipaddress,restype,category,entity,subentity,unit,chname,bak,count,thevalue,collecttime) " + "values('" + ip + "','"
								+ discardsperc.getRestype() + "','" + discardsperc.getCategory() + "','" + discardsperc.getEntity() + "','" + discardsperc.getSubentity() + "','"
								+ discardsperc.getUnit() + "','" + discardsperc.getChname() + "','" + discardsperc.getBak() + "'," + discardsperc.getCount() + ",'" + discardsperc.getThevalue()
								+ "','" + time + "')";
							}else if("oracle".equalsIgnoreCase(SystemConstant.DBType)){
								sql = "insert into " + tablename + "(ipaddress,restype,category,entity,subentity,unit,chname,bak,count,thevalue,collecttime) " + "values('" + ip + "','"
								+ discardsperc.getRestype() + "','" + discardsperc.getCategory() + "','" + discardsperc.getEntity() + "','" + discardsperc.getSubentity() + "','"
								+ discardsperc.getUnit() + "','" + discardsperc.getChname() + "','" + discardsperc.getBak() + "'," + discardsperc.getCount() + ",'" + discardsperc.getThevalue()
								+ "',to_date('"+time+"','YYYY-MM-DD HH24:MI:SS'))";
							}							
							try {
								dbmanager.executeUpdate(sql);
							} catch (Exception ex) {
								ex.printStackTrace();
							}
							// conn.executeUpdate(sql);
						}
					}
				}

				// errorsperc
				Vector errorspercVector = (Vector) datahash.get("errorsperc");
				if (errorspercVector != null && errorspercVector.size() > 0) {
					for (int si = 0; si < errorspercVector.size(); si++) {
						ErrorsPerc errorsperc = (ErrorsPerc) errorspercVector.elementAt(si);
						if (errorsperc.getRestype().equals("dynamic")) {
							if (errorsperc.getThevalue().equals("0.0"))
								continue;
							// session.save(errorsperc);
							Calendar tempCal = (Calendar) errorsperc.getCollecttime();
							Date cc = tempCal.getTime();
							String time = DbConversionUtil.coversionTimeSql(sdf.format(cc));
							String tablename = "errorsperc" + allipstr;
							String sql = "";
							if("mysql".equalsIgnoreCase(SystemConstant.DBType)){
								sql = "insert into " + tablename + "(ipaddress,restype,category,entity,subentity,unit,chname,bak,count,thevalue,collecttime) " + "values('" + ip + "','"
								+ errorsperc.getRestype() + "','" + errorsperc.getCategory() + "','" + errorsperc.getEntity() + "','" + errorsperc.getSubentity() + "','" + errorsperc.getUnit()
								+ "','" + errorsperc.getChname() + "','" + errorsperc.getBak() + "'," + errorsperc.getCount() + ",'" + errorsperc.getThevalue() + "','" + time + "')";
							}else if("oracle".equalsIgnoreCase(SystemConstant.DBType)){
								sql = "insert into " + tablename + "(ipaddress,restype,category,entity,subentity,unit,chname,bak,count,thevalue,collecttime) " + "values('" + ip + "','"
								+ errorsperc.getRestype() + "','" + errorsperc.getCategory() + "','" + errorsperc.getEntity() + "','" + errorsperc.getSubentity() + "','" + errorsperc.getUnit()
								+ "','" + errorsperc.getChname() + "','" + errorsperc.getBak() + "'," + errorsperc.getCount() + ",'" + errorsperc.getThevalue() + "',to_date('"+time+"','YYYY-MM-DD HH24:MI:SS'))";
							}							
							try {
								dbmanager.executeUpdate(sql);
							} catch (Exception ex) {
								ex.printStackTrace();
							}
							// conn.executeUpdate(sql);

						}
					}
				}
				// packs
				Vector packsVector = (Vector) datahash.get("packs");
				if (packsVector != null && packsVector.size() > 0) {
					for (int si = 0; si < packsVector.size(); si++) {
						Packs packs = (Packs) packsVector.elementAt(si);
						if (packs.getRestype().equals("dynamic")) {
							if (packs.getThevalue().equals("0"))
								continue;
							// session.save(packs);
							Calendar tempCal = (Calendar) packs.getCollecttime();
							Date cc = tempCal.getTime();
							String time = DbConversionUtil.coversionTimeSql(sdf.format(cc));
							String tablename = "packs" + allipstr;
							String sql = "insert into " + tablename + "(ipaddress,restype,category,entity,subentity,unit,chname,bak,count,thevalue,collecttime) " + "values('" + ip + "','"
									+ packs.getRestype() + "','" + packs.getCategory() + "','" + packs.getEntity() + "','" + packs.getSubentity() + "','" + packs.getUnit() + "','" + packs.getChname()
									+ "','" + packs.getBak() + "'," + packs.getCount() + ",'" + packs.getThevalue() + "'," + time + ")";
							try {
								dbmanager.executeUpdate(sql);
							} catch (Exception ex) {
								ex.printStackTrace();
							}
							// conn.executeUpdate(sql);
						}
					}
				}
			} else if ("inpacks".equalsIgnoreCase(category)) {
				// inpacks
				Vector inpacksVector = (Vector) datahash.get("inpacks");
				if (inpacksVector != null && inpacksVector.size() > 0) {
					for (int si = 0; si < inpacksVector.size(); si++) {
						InPkts packs = (InPkts) inpacksVector.elementAt(si);
						if (packs.getRestype().equals("dynamic")) {
							if (packs.getThevalue().equals("0"))
								continue;
							// session.save(packs);
							Calendar tempCal = (Calendar) packs.getCollecttime();
							Date cc = tempCal.getTime();
							String time = DbConversionUtil.coversionTimeSql(sdf.format(cc));
							String tablename = "inpacks" + allipstr;
							String sql = "insert into " + tablename + "(ipaddress,restype,category,entity,subentity,unit,chname,bak,count,thevalue,collecttime) " + "values('" + ip + "','"
									+ packs.getRestype() + "','" + packs.getCategory() + "','" + packs.getEntity() + "','" + packs.getSubentity() + "','" + packs.getUnit() + "','" + packs.getChname()
									+ "','" + packs.getBak() + "'," + packs.getCount() + ",'" + packs.getThevalue() + "'," + time + ")";
							try {
								dbmanager.executeUpdate(sql);
							} catch (Exception ex) {
								ex.printStackTrace();
							}
							// conn.executeUpdate(sql);
						}
					}
				}
			} else if ("outpacks".equalsIgnoreCase(category)) {
				// inpacks
				Vector outpacksVector = (Vector) datahash.get("outpacks");
				if (outpacksVector != null && outpacksVector.size() > 0) {
					for (int si = 0; si < outpacksVector.size(); si++) {
						OutPkts packs = (OutPkts) outpacksVector.elementAt(si);
						if (packs.getRestype().equals("dynamic")) {
							if (packs.getThevalue().equals("0"))
								continue;
							// session.save(packs);
							Calendar tempCal = (Calendar) packs.getCollecttime();
							Date cc = tempCal.getTime();
							String time = DbConversionUtil.coversionTimeSql(sdf.format(cc));
							String tablename = "outpacks" + allipstr;
							String sql = "insert into " + tablename + "(ipaddress,restype,category,entity,subentity,unit,chname,bak,count,thevalue,collecttime) " + "values('" + ip + "','"
									+ packs.getRestype() + "','" + packs.getCategory() + "','" + packs.getEntity() + "','" + packs.getSubentity() + "','" + packs.getUnit() + "','" + packs.getChname()
									+ "','" + packs.getBak() + "'," + packs.getCount() + ",'" + packs.getThevalue() + "'," + time + ")";
							try {
								dbmanager.executeUpdate(sql);
							} catch (Exception ex) {
								ex.printStackTrace();
							}
							// conn.executeUpdate(sql);
						}
					}
				}
			}

			// ����
			Vector bufferVector = (Vector) datahash.get("buffer");
			if (bufferVector != null && bufferVector.size() > 0) {
				Buffercollectdata bufferdata = (Buffercollectdata) bufferVector.elementAt(0);
				if (bufferdata.getRestype().equals("dynamic")) {
					// session.save(cpudata);
					Calendar tempCal = (Calendar) bufferdata.getCollecttime();
					Date cc = tempCal.getTime();
					String time = DbConversionUtil.coversionTimeSql(sdf.format(cc));
					String tablename = "buffer" + allipstr;
					String sql = "insert into " + tablename + "(ipaddress,restype,category,entity,subentity,unit,chname,bak,count,thevalue,collecttime) " + "values('" + ip + "','"
							+ bufferdata.getRestype() + "','" + bufferdata.getCategory() + "','" + bufferdata.getEntity() + "','" + bufferdata.getSubentity() + "','" + bufferdata.getUnit() + "','"
							+ bufferdata.getChname() + "','" + bufferdata.getBak() + "'," + bufferdata.getCount() + ",'" + bufferdata.getThevalue() + "'," + time
							+ ")";
					// System.out.println(sql);
					try {
						dbmanager.executeUpdate(sql);
					} catch (Exception ex) {
						ex.printStackTrace();
					} finally {
						// dbmanager.close();
					}
					// conn.executeUpdate(sql);
				}
			}

			Vector ipmacVector = (Vector) datahash.get("ipmac");
			Hashtable ipmacs = ShareData.getRelateipmacdata();
			Hashtable ipmacband = ShareData.getIpmacbanddata();
			IpMacChangeDao macchangedao = new IpMacChangeDao();
			Hashtable macHash = null;
			try {
				macHash = macchangedao.loadMacIpHash();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				macchangedao.close();
			}

			if (ipmacVector != null && ipmacVector.size() > 0) {
				// �����ڴ���
				ShareData.setRelateipmacdata(ip, ipmacVector);
			}
			int firstipmac = ShareData.getFirstipmac();
			if (firstipmac == 0) {
				// ��һ������
				// ���ñ���ֵ����װ��IP-MAC���ձ�
				ShareData.setFirstipmac(1);
				// װ�ظ�IP��IP-MAC��Ӧ�󶨹�ϵ��
				ResultSet rs = dbmanager.executeQuery("select * from nms_ipmacbase");
				List list = manage.loadFromIpMacBandRS(rs);
				if (list != null && list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						IpMacBase _ipmacbase = (IpMacBase) list.get(i);
						if (ipmacband.containsKey(_ipmacbase.getMac())) {
							// �Ѿ�����
							List existMacList = (List) ipmacband.get(_ipmacbase.getMac());
							existMacList.add(_ipmacbase);
							ipmacband.put(_ipmacbase.getMac(), existMacList);
						} else {
							List macList = new ArrayList();
							macList.add(_ipmacbase);
							ipmacband.put(_ipmacbase.getMac(), macList);
						}

					}
				}
				rs.close();

			}
			if (ipmacVector != null && ipmacVector.size() > 0) {
				// ��ʵʱ�ɼ�����ARP����Ϣ��ŵ����������Ȼ���ٸ������ݿ�
				ipmacs.put(ip, ipmacVector);
				String sql = "delete from ipmac where relateipaddr='" + ip + "'";
				try {
					dbmanager.executeUpdate(sql);
				} catch (Exception e) {
					e.printStackTrace();
				}

				String _localMAC = "";
				Hashtable _AllMAC = new Hashtable();
				Hashtable _sameMAC = new Hashtable();
				Vector tempVector = new Vector();
				for (int si = 0; si < ipmacVector.size(); si++) {
					IpMac ipmac = (IpMac) ipmacVector.elementAt(si);
					if (ipmac == null)
						continue;
					if (ipmac.getIpaddress() == null)
						continue;
					if (ipmac.getIpaddress().equals(ip)) {
						// ���Ǳ���IP
						_localMAC = ipmac.getMac();
					}
					if (!_AllMAC.containsKey(ipmac.getMac())) {
						// �������ڴ�IP
						_AllMAC.put(ipmac.getMac(), ipmac);
					} else {
						// ������

						tempVector.add(ipmac);
						// tempVector.add(_AllIp.get(ipmac.getIpaddress()));
						_sameMAC.put(ipmac.getMac(), tempVector);
					}
				}
				for (int si = 0; si < ipmacVector.size(); si++) {
					IpMac ipmac = (IpMac) ipmacVector.elementAt(si);
					try {
						if (ipmac != null && ipmac.getMac() != null && ipmac.getMac().length() == 17) {
							dbmanager.executeUpdate(manage.ipmacInsertSQL(ipmac));
						}
					} catch (Exception ex) {
						ex.printStackTrace();
					}
					// session.save(ipmac);
					if (ipmac == null)
						continue;
					if (ipmac.getMac() == null)
						continue;
					if (ipmac.getMac().equalsIgnoreCase(_localMAC)) {
						// ���Ǳ���MAC,IP���Ǳ����������
						if (!ipmac.getIpaddress().equals(ip))
							continue;
					}
					// ͬʱ����IP-MAC�󶨱���IP-MAC��ַ�����������µ�IP-MAC��Ϣ��ͬʱ���浽��ʷ�������
					if (ipmacband.containsKey(ipmac.getMac())) {
						List existMacList = (List) ipmacband.get(ipmac.getMac());
						if (existMacList != null && existMacList.size() > 0) {
							int changeflag = 0;
							int samenodeflag = 0;
							String ipstr = "";
							IpMacBase changeMacBase = null;
							for (int i = 0; i < existMacList.size(); i++) {
								IpMacBase temp_ipmacband = (IpMacBase) existMacList.get(i);
								ipstr = ipstr + "��" + temp_ipmacband.getRelateipaddr();
								if (temp_ipmacband.getIpaddress().equalsIgnoreCase(ipmac.getIpaddress())) {
									changeflag = 1;
								}
							}
							if (changeflag == 0) {
								// û����ȵ�IP,�жϴ��ĸ��豸��·��
								for (int i = 0; i < existMacList.size(); i++) {
									IpMacBase temp_ipmacband = (IpMacBase) existMacList.get(i);
									if (temp_ipmacband.getRelateipaddr().equalsIgnoreCase(ipmac.getRelateipaddr())) {
										samenodeflag = 1;
										changeMacBase = temp_ipmacband;
										break;
									}
								}
							}
							// �����ڴ�MAC�����ж�IP�Ƿ����
							// IpMacBase temp_ipmacband =
							// (IpMacBase)ipmacband.get(ipmac.getMac());

							// if(!temp_ipmacband.getIpaddress().equalsIgnoreCase(ipmac.getIpaddress())){
							if (changeMacBase != null) {
								// ��IP����ȣ������ҵ��豸��·��,�����µ���Ϣ�޸ĸ�IP-MAC�󶨱�ͬʱ����ʷ����׷��һ���¼������ʾ��Ϣ

								String _ipaddress = changeMacBase.getIpaddress();
								changeMacBase.setIpaddress(ipmac.getIpaddress());
								// ShareData.setIpmacbanddata(ipmac.getMac(),temp_ipmacband);
								existMacList = (List) ipmacband.get(changeMacBase.getMac());
								existMacList.add(changeMacBase);
								ipmacband.put(changeMacBase.getMac(), existMacList);
								try {
									dbmanager.executeUpdate(manage.ipmacbandUpdateSQL(changeMacBase));
								} catch (Exception ex) {
									ex.printStackTrace();
								}
								if (_sameMAC.containsKey(ipmac.getMac())) {
									if (changeMacBase.getIfband() == 0)
										continue;
								}

								// ����ʷ����׷��һ���¼������ʾ��Ϣ
								IpMacChange _ipmacchange = new IpMacChange();
								_ipmacchange.setMac(ipmac.getMac());
								_ipmacchange.setChangetype("2");
								_ipmacchange.setDetail("��MAC��ַ��IP��" + _ipaddress + " �任��IP��" + ipmac.getIpaddress());
								_ipmacchange.setIpaddress(ipmac.getIpaddress());
								_ipmacchange.setCollecttime(ipmac.getCollecttime());
								// �Է���IP�����Ҫ����PING����������PINGͨ���������һ������

								Vector vector = null;
								PingUtil pingU = new PingUtil(ipmac.getIpaddress());
								Integer[] packet = pingU.ping();
								// vector=pingU.addhis(packet);
								if (packet[0] != null) {
									// hostdata.setThevalue(packet[0].toString());
								} else {
									// hostdata.setThevalue("0");
									continue;
								}

								// ��IP-MAC�Ѿ��󶨣�������¼�
								if (changeMacBase.getIfband() == 1) {
									try {
										dbmanager.executeUpdate(manage.ipmacchangeInsertSQL(_ipmacchange));
									} catch (Exception ex) {
										ex.printStackTrace();
									}
								}
							} else if (changeflag == 0) {
								// ��IP����ȣ���û�ҵ���ͬ���豸��·��,˵����MAC�������豸�����˸ı�,��׷���µ�IP-MAC�󶨱�ͬʱ����ʷ����׷��һ���¼������ʾ��Ϣ

								IpMacBase _ipmacband = new IpMacBase();
								_ipmacband.setRelateipaddr(ipmac.getRelateipaddr());
								_ipmacband.setIfindex(ipmac.getIfindex());
								_ipmacband.setIpaddress(ipmac.getIpaddress());
								_ipmacband.setMac(ipmac.getMac());
								_ipmacband.setCollecttime(ipmac.getCollecttime());
								_ipmacband.setIfband(0);
								_ipmacband.setIfsms("0");
								_ipmacband.setEmployee_id(new Integer(0));
								_ipmacband.setRelateipaddr(ip);
								ipmacband.put(ipmac.getMac(), _ipmacband);
								try {
									dbmanager.executeUpdate(manage.ipmacbaseInsertSQL(_ipmacband));
								} catch (Exception ex) {
									ex.printStackTrace();
								}
								if (macHash.containsKey(ipmac.getRelateipaddr() + ":" + ipmac.getIpaddress() + ":" + ipmac.getMac()))
									continue;
								// ����ʷ����׷��һ���¼������ʾ��Ϣ
								IpMacChange _ipmacchange = new IpMacChange();
								_ipmacchange.setMac(ipmac.getMac());
								_ipmacchange.setChangetype("1");
								_ipmacchange.setDetail("��MAC��ַ���豸" + ipstr + " �任���豸" + ipmac.getRelateipaddr() + "��,IP��" + ipmac.getIpaddress());
								_ipmacchange.setIpaddress(ipmac.getIpaddress());
								_ipmacchange.setCollecttime(ipmac.getCollecttime());
								_ipmacchange.setRelateipaddr(ip);
								_ipmacchange.setIfindex(ipmac.getIfindex());

								try {
									dbmanager.executeUpdate(manage.ipmacchangeInsertSQL(_ipmacchange));
									// session.save(_ipmacband);
								} catch (Exception ex) {
									ex.printStackTrace();
								}

							}
						}
					} else {
						// �жϱ�������Ƿ��Ѿ��и�MAC��ַ
						if (macHash.containsKey(ipmac.getRelateipaddr() + ":" + ipmac.getIpaddress() + ":" + ipmac.getMac()))
							continue;
						// ����ʷ����׷��һ���¼������ʾ��Ϣ
						IpMacChange _ipmacchange = new IpMacChange();
						_ipmacchange.setMac(ipmac.getMac());
						_ipmacchange.setChangetype("1");
						_ipmacchange.setDetail("�����ӵ�IP-MAC");
						_ipmacchange.setIpaddress(ipmac.getIpaddress());
						_ipmacchange.setCollecttime(ipmac.getCollecttime());
						_ipmacchange.setRelateipaddr(ip);
						_ipmacchange.setIfindex(ipmac.getIfindex());

						try {
							if (ipmac.getMac().length() == 17) {
								dbmanager.executeUpdate(manage.ipmacchangeInsertSQL(_ipmacchange));
							}
							// session.save(_ipmacband);
						} catch (Exception ex) {
							ex.printStackTrace();
						}

						// session.save(_ipmacchange);
					}
				}
			}

			// iprouter
			Vector iprouterVector = (Vector) datahash.get("iprouter");
			if (iprouterVector != null && iprouterVector.size() > 0) {
				// �����ڴ���
				ShareData.setIprouterdata(ip, iprouterVector);
			}
			if (iprouterVector != null && iprouterVector.size() > 0) {
				String sql = "delete from iprouter where relateipaddr='" + ip + "'";
				// dbmanager.executeUpdate(sql);
				for (int si = 0; si < iprouterVector.size(); si++) {
					IpRouter iprouter = (IpRouter) iprouterVector.elementAt(si);
					try {
						// session.save(iprouter);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

			// policydata
			Hashtable policyHash = (Hashtable) datahash.get("policys");
			if (policyHash != null && policyHash.size() > 0) {
				// �����ڴ���
				ShareData.setPolicydata(ip, policyHash);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			dbmanager.close();
			dbmanager = null;
			datahash = null;
			System.gc();
		}

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dhcc.webnms.host.api.I_HostCollectData#deleteHostData(java.lang.String)
	 */

	public Hashtable getCategory(String ip, String category,  String starttime, String endtime) throws Exception {
		Hashtable hash = new Hashtable();
		Connection con = null;
		PreparedStatement stmt = null;
		DBManager dbmanager = new DBManager();
		ResultSet rs = null;
		String sid = ip;
		try {
			if (!starttime.equals("") && !endtime.equals("")) {
				if (category.equals("Ping") || category.equals("rcpu") || category.equals("rmemory") || category.equals("rcable") || category.equals("rcache") || category.equals("rmemory")
						|| category.equals("rpower") || category.equals("rfan") || category.equals("renv")) {
					sid = SysUtil.doip(ip);
				}
				String sql = "";
				StringBuffer sb = new StringBuffer();
				if (category.equals("Ping")) {
					//
					if ("mysql".equalsIgnoreCase(SystemConstant.DBType)) {
						sb.append(" select h.thevalue,DATE_FORMAT(h.collecttime,'%Y-%m-%d %H:%i:%s') as collecttime ,h.unit from pings" + sid + " h where ");
					} else if ("oracle".equalsIgnoreCase(SystemConstant.DBType)) {
						sb.append(" select h.thevalue,to_char(h.collecttime,'YYYY-MM-DD HH24:MI:SS') as collecttime ,h.unit from pings" + sid + " h where ");
					}
				}  else if (category.equals("rcpu")) {
					if ("mysql".equalsIgnoreCase(SystemConstant.DBType)) {
						sb.append(" select h.thevalue,DATE_FORMAT(h.collecttime,'%Y-%m-%d %H:%i:%s') as collecttime,h.unit from rcpu" + sid + " h where ");
					} else if ("oracle".equalsIgnoreCase(SystemConstant.DBType)) {
						sb.append(" select h.thevalue,to_char(h.collecttime,'YYYY-MM-DD HH24:MI:SS') as collecttime,h.unit from rcpu" + sid + " h where ");
					}

				} else if (category.equals("rmemory")) {
					if ("mysql".equalsIgnoreCase(SystemConstant.DBType)) {
						sb.append(" select h.thevalue,DATE_FORMAT(h.collecttime,'%Y-%m-%d %H:%i:%s') as collecttime,h.unit from rmemory" + sid + " h where ");
					} else if ("oracle".equalsIgnoreCase(SystemConstant.DBType)) {
						sb.append(" select h.thevalue,to_char(h.collecttime,'YYYY-MM-DD HH24:MI:SS') as collecttime,h.unit from rmemory" + sid + " h where ");
					}

				} else if (category.equals("rcable")) {
					if ("mysql".equalsIgnoreCase(SystemConstant.DBType)) {
						sb.append(" select h.thevalue,DATE_FORMAT(h.collecttime,'%Y-%m-%d %H:%i:%s') as collecttime,h.unit from rcable" + sid + " h where ");
					} else if ("oracle".equalsIgnoreCase(SystemConstant.DBType)) {
						sb.append(" select h.thevalue,to_char(h.collecttime,'YYYY-MM-DD HH24:MI:SS') as collecttime,h.unit from rcable" + sid + " h where ");
					}

				} else if (category.equals("rcache")) {
					if ("mysql".equalsIgnoreCase(SystemConstant.DBType)) {
						sb.append(" select h.thevalue,DATE_FORMAT(h.collecttime,'%Y-%m-%d %H:%i:%s') as collecttime,h.unit from rcache" + sid + " h where ");
					} else if ("oracle".equalsIgnoreCase(SystemConstant.DBType)) {
						sb.append(" select h.thevalue,to_char(h.collecttime,'YYYY-MM-DD HH24:MI:SS') as collecttime,h.unit from rcache" + sid + " h where ");
					}

				} else if (category.equals("rmemory")) {
					if ("mysql".equalsIgnoreCase(SystemConstant.DBType)) {
						sb.append(" select h.thevalue,DATE_FORMAT(h.collecttime,'%Y-%m-%d %H:%i:%s') as collecttime,h.unit from rmemory" + sid + " h where ");
					} else if ("oracle".equalsIgnoreCase(SystemConstant.DBType)) {
						sb.append(" select h.thevalue,to_char(h.collecttime,'YYYY-MM-DD HH24:MI:SS') as collecttime,h.unit from rmemory" + sid + " h where ");
					}

				} else if (category.equals("rpower")) {
					if ("mysql".equalsIgnoreCase(SystemConstant.DBType)) {
						sb.append(" select h.thevalue,DATE_FORMAT(h.collecttime,'%Y-%m-%d %H:%i:%s') as collecttime,h.unit from rpower" + sid + " h where ");
					} else if ("oracle".equalsIgnoreCase(SystemConstant.DBType)) {
						sb.append(" select h.thevalue,to_char(h.collecttime,'YYYY-MM-DD HH24:MI:SS') as collecttime,h.unit from rpower" + sid + " h where ");
					}

				} else if (category.equals("rfan")) {
					if ("mysql".equalsIgnoreCase(SystemConstant.DBType)) {
						sb.append(" select h.thevalue,DATE_FORMAT(h.collecttime,'%Y-%m-%d %H:%i:%s') as collecttime,h.unit from rfan" + sid + " h where ");
					} else if ("oracle".equalsIgnoreCase(SystemConstant.DBType)) {
						sb.append(" select h.thevalue,to_char(h.collecttime,'YYYY-MM-DD HH24:MI:SS') as collecttime,h.unit from rfan" + sid + " h where ");
					}

				} else if (category.equals("renv")) {
					if ("mysql".equalsIgnoreCase(SystemConstant.DBType)) {
						sb.append(" select h.thevalue,DATE_FORMAT(h.collecttime,'%Y-%m-%d %H:%i:%s') as collecttime,h.unit from renv" + sid + " h where ");
					} else if ("oracle".equalsIgnoreCase(SystemConstant.DBType)) {
						sb.append(" select h.thevalue,to_char(h.collecttime,'YYYY-MM-DD HH24:MI:SS') as collecttime,h.unit from renv" + sid + " h where ");
					}

				} else if (category.equals("edrive")) {
					if ("mysql".equalsIgnoreCase(SystemConstant.DBType)) {
						sb.append(" select h.thevalue,DATE_FORMAT(h.collecttime,'%Y-%m-%d %H:%i:%s') as collecttime,h.unit from edrive" + sid + " h where ");
					} else if ("oracle".equalsIgnoreCase(SystemConstant.DBType)) {
						sb.append(" select h.thevalue,to_char(h.collecttime,'YYYY-MM-DD HH24:MI:SS') as collecttime,h.unit from edrive" + sid + " h where ");
					}

				} else if (category.equals("rbutter")) {
					if ("mysql".equalsIgnoreCase(SystemConstant.DBType)) {
						sb.append(" select h.thevalue,DATE_FORMAT(h.collecttime,'%Y-%m-%d %H:%i:%s') as collecttime,h.unit from rbutter" + sid + " h where ");
					} else if ("oracle".equalsIgnoreCase(SystemConstant.DBType)) {
						sb.append(" select h.thevalue,to_char(h.collecttime,'YYYY-MM-DD HH24:MI:SS') as collecttime,h.unit from rbutter" + sid + " h where ");
					}

				} else if (category.equals("rnumber")) {
					if ("mysql".equalsIgnoreCase(SystemConstant.DBType)) {
						sb.append(" select h.thevalue,DATE_FORMAT(h.collecttime,'%Y-%m-%d %H:%i:%s') as collecttime,h.unit from rnumber" + sid + " h where ");
					} else if ("oracle".equalsIgnoreCase(SystemConstant.DBType)) {
						sb.append(" select h.thevalue,to_char(h.collecttime,'YYYY-MM-DD HH24:MI:SS') as collecttime,h.unit from rnumber" + sid + " h where ");
					}

				} else if (category.equals("rluncon")) {
					if ("mysql".equalsIgnoreCase(SystemConstant.DBType)) {
						sb.append(" select h.thevalue,DATE_FORMAT(h.collecttime,'%Y-%m-%d %H:%i:%s') as collecttime,h.unit from rluncon" + sid + " h where ");
					} else if ("oracle".equalsIgnoreCase(SystemConstant.DBType)) {
						sb.append(" select h.thevalue,to_char(h.collecttime,'YYYY-MM-DD HH24:MI:SS') as collecttime,h.unit from rluncon" + sid + " h where ");
					}

				} else if (category.equals("rswitch")) {
					if ("mysql".equalsIgnoreCase(SystemConstant.DBType)) {
						sb.append(" select h.thevalue,DATE_FORMAT(h.collecttime,'%Y-%m-%d %H:%i:%s') as collecttime,h.unit from rswitch" + sid + " h where ");
					} else if ("oracle".equalsIgnoreCase(SystemConstant.DBType)) {
						sb.append(" select h.thevalue,to_char(h.collecttime,'YYYY-MM-DD HH24:MI:SS') as collecttime,h.unit from rswitch" + sid + " h where ");
					}

				} else if (category.equals("rwwncon")) {
					if ("mysql".equalsIgnoreCase(SystemConstant.DBType)) {
						sb.append(" select h.thevalue,DATE_FORMAT(h.collecttime,'%Y-%m-%d %H:%i:%s') as collecttime,h.unit from rwwncon" + sid + " h where ");
					} else if ("oracle".equalsIgnoreCase(SystemConstant.DBType)) {
						sb.append(" select h.thevalue,to_char(h.collecttime,'YYYY-MM-DD HH24:MI:SS') as collecttime,h.unit from rwwncon" + sid + " h where ");
					}

				} else if (category.equals("rsafety")) {
					if ("mysql".equalsIgnoreCase(SystemConstant.DBType)) {
						sb.append(" select h.thevalue,DATE_FORMAT(h.collecttime,'%Y-%m-%d %H:%i:%s') as collecttime,h.unit from rsafety" + sid + " h where ");
					} else if ("oracle".equalsIgnoreCase(SystemConstant.DBType)) {
						sb.append(" select h.thevalue,to_char(h.collecttime,'YYYY-MM-DD HH24:MI:SS') as collecttime,h.unit from rsafety" + sid + " h where ");
					}

				} else if (category.equals("rsluncon")) {
					if ("mysql".equalsIgnoreCase(SystemConstant.DBType)) {
						sb.append(" select h.thevalue,DATE_FORMAT(h.collecttime,'%Y-%m-%d %H:%i:%s') as collecttime,h.unit from rsluncon" + sid + " h where ");
					} else if ("oracle".equalsIgnoreCase(SystemConstant.DBType)) {
						sb.append(" select h.thevalue,to_char(h.collecttime,'YYYY-MM-DD HH24:MI:SS') as collecttime,h.unit from rsluncon" + sid + " h where ");
					}

				} 
				sb.append(" h.category='");
				sb.append(category);
				if ("mysql".equalsIgnoreCase(SystemConstant.DBType)) {
					sb.append("' and h.collecttime >= '");
					sb.append(starttime);
					sb.append("' and h.collecttime <= '");
					sb.append(endtime);
					sb.append("' order by h.collecttime asc");
				} else if ("oracle".equalsIgnoreCase(SystemConstant.DBType)) {
					sb.append("' and h.collecttime >= to_date('");
					sb.append(starttime);
					sb.append("','YYYY-MM-DD HH24:MI:SS') and h.collecttime <= to_date('");
					sb.append(endtime);
					sb.append("','YYYY-MM-DD HH24:MI:SS')");
					sb.append(" order by h.collecttime asc");
				}

				sql = sb.toString();
				
//				System.out.println("--------HostCollectDataManager-------->"+sql);
				
				rs = dbmanager.executeQuery(sql);
				List list1 = new ArrayList();
				String unit = "";
				String max = "";
				double tempfloat = 0;
				// HONGLI ADD START1
				double pingMaxFloat = 0;
				// HONGLI ADD END1
				double pingcon = 0;
				double cpucon = 0;
				double memory = 0;
				int downnum = 0;
				int i = 0;
				if (rs != null) {
					while (rs.next()) {
						i = i + 1;
						Vector v = new Vector();
						String thevalue = rs.getString("thevalue");
						String collecttime = rs.getString("collecttime");
						v.add(0, emitStr(thevalue));
						v.add(1, collecttime);
						v.add(2, rs.getString("unit"));
						list1.add(v);
					}
					rs.close();
				}
				// stmt.close();

				Integer size = new Integer(0);
				hash.put("list", list1);
				if (list1.size() != 0) {
					size = new Integer(list1.size());
					if (list1.get(0) != null) {
						Vector tempV = (Vector) list1.get(0);
						unit = (String) tempV.get(2);
					}
				}
			}
		
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null)
				rs.close();
			dbmanager.close();
		}

		return hash;
	}
	
	
	private String emitStr(String num) {
		if (num != null) {
			if (num.indexOf(".") >= 0) {
				if (num.substring(num.indexOf(".") + 1).length() > 7) {
					String tempStr = num.substring(num.indexOf(".") + 1);
					num = num.substring(0, num.indexOf(".") + 1) + tempStr.substring(0, 7);
				}
			}
		}
		return num;
	}
	private double getfloat(String num) {
		double snum = 0.0;
		if (num != null) {
			if (num.indexOf(".") >= 0) {
				if (num.substring(num.indexOf(".") + 1).length() > 7) {
					String tempStr = num.substring(num.indexOf(".") + 1);
					num = num.substring(0, num.indexOf(".") + 1) + tempStr.substring(0, 7);
				}
			}
			int inum = (int) (Float.parseFloat(num) * 100);
			snum = new Double(inum / 100.0).doubleValue();
		}
		return snum;
	}
	
	
	private String dofloat(String num) {
		String snum = "0.0";
		if (num != null) {
			int inum = (int) (Float.parseFloat(num) * 100);
			snum = Double.toString(inum / 100.0);
		}
		return snum;
	}
	
	
	private String storageFan() {
		Date d = new Date();
		String startdate = getParaValue("startdate");
		if (startdate == null) {
			startdate = sdf0.format(d);
		}
		String todate = getParaValue("todate");
		if (todate == null) {
			todate = sdf0.format(d);
		}
		String starttime = startdate + " 00:00:00";
		String totime = todate + " 23:59:59";

		String ids = request.getParameter("id");
		String flag = (String) session.getAttribute("ids");
		if(flag != null && ids == null){
			ids = flag;
			request.getSession().removeAttribute("ids");
		}
		// �������־ȡ���˿����¼�¼���б�
		String orderflag = "ipaddress";
		if (getParaValue("orderflag") != null && !getParaValue("orderflag").equals("")) {
			orderflag = getParaValue("orderflag");
		}
 
		List orderList = new ArrayList();
		if (ids != null) {
			session.setAttribute("ids", ids);
				HostNodeDao dao = new HostNodeDao();
				HostNode node = dao.loadHost(Integer.parseInt(ids));
                String ip =node.getIpAddress();
                if(node.getIpAddress().contains(".")){
                	ip=node.getIpAddress().replace(".", "_");
                }
				Hashtable pinghash = new Hashtable();
				try {
					pinghash = this.getCategory(ip, "rfan",  starttime, totime);
				} catch (Exception e) {
					e.printStackTrace();
				}
				Hashtable responsehash = new Hashtable();
				Hashtable ipmemhash = new Hashtable();
				ipmemhash.put("hostnode", node);
				ipmemhash.put("pinghash", pinghash);
				ipmemhash.put("ipaddress", node.getIpAddress() + "(" + node.getAlias() + ")");
				orderList.add(ipmemhash);

		}
		String pingChartDivStr = ReportHelper.getChartDivStr(orderList, "ping");
//		String responsetimeDivStr = ReportHelper.getChartDivStr(orderList, "responsetime");
		// ��ͨ��
		ReportValue pingReportValue = ReportHelper.getReportValue(orderList, "ping");
//	    ReportValue responseReportValue = ReportHelper.getReportValue(orderList, "responsetime");
		String pingpath = new ReportExport().makeJfreeChartData(pingReportValue.getListValue(), pingReportValue.getIpList(), "����", "", "");
//		String responsetimepath = new ReportExport().makeJfreeChartData(responseReportValue.getListValue(), responseReportValue.getIpList(), "��Ӧʱ��", "ʱ��", "");
        System.out.println(pingReportValue.getListValue().size());
//		request.setAttribute("responsetimeDivStr", responsetimeDivStr);
		request.setAttribute("pingChartDivStr", pingChartDivStr);
		request.setAttribute("starttime", starttime);
		request.setAttribute("totime", totime);
		session.setAttribute("pingpath", pingpath);
//		session.setAttribute("responsetimepath", responsetimepath);
		session.setAttribute("orderList", orderList);
		request.setAttribute("startdate", startdate);
		request.setAttribute("todate", todate);
//		request.setAttribute("pinglist", list);
		return "/capreport/net/hdsFan.jsp";
	}
	
	
	private String storageEnv() {
		Date d = new Date();
		String startdate = getParaValue("startdate");
		if (startdate == null) {
			startdate = sdf0.format(d);
		}
		String todate = getParaValue("todate");
		if (todate == null) {
			todate = sdf0.format(d);
		}
		String starttime = startdate + " 00:00:00";
		String totime = todate + " 23:59:59";

		String ids = request.getParameter("id");
		String flag = (String) session.getAttribute("ids");
		if(flag != null && ids == null){
			ids = flag;
			request.getSession().removeAttribute("ids");
		}
		// �������־ȡ���˿����¼�¼���б�
		String orderflag = "ipaddress";
		if (getParaValue("orderflag") != null && !getParaValue("orderflag").equals("")) {
			orderflag = getParaValue("orderflag");
		}
 
		List orderList = new ArrayList();
		if (ids != null) {
			    session.setAttribute("ids", ids);
				HostNodeDao dao = new HostNodeDao();
				HostNode node = dao.loadHost(Integer.parseInt(ids));
                String ip =node.getIpAddress();
                if(node.getIpAddress().contains(".")){
                	ip=node.getIpAddress().replace(".", "_");
                }
				Hashtable pinghash = new Hashtable();
				try {
					pinghash = this.getCategory(ip, "renv",  starttime, totime);
				} catch (Exception e) {
					e.printStackTrace();
				}
				Hashtable responsehash = new Hashtable();
				Hashtable ipmemhash = new Hashtable();
				ipmemhash.put("hostnode", node);
				ipmemhash.put("pinghash", pinghash);
				ipmemhash.put("ipaddress", node.getIpAddress() + "(" + node.getAlias() + ")");
				orderList.add(ipmemhash);

		}
		String pingChartDivStr = ReportHelper.getChartDivStr(orderList, "ping");
//		String responsetimeDivStr = ReportHelper.getChartDivStr(orderList, "responsetime");
		// ��ͨ��
		ReportValue pingReportValue = ReportHelper.getReportValue(orderList, "ping");
		ReportValue responseReportValue = ReportHelper.getReportValue(orderList, "responsetime");
		String pingpath = new ReportExport().makeJfreeChartData(pingReportValue.getListValue(), pingReportValue.getIpList(), "����״̬", "", "");
//		String responsetimepath = new ReportExport().makeJfreeChartData(responseReportValue.getListValue(), responseReportValue.getIpList(), "��Ӧʱ��", "ʱ��", "");
        System.out.println(pingReportValue.getListValue().size());
//		request.setAttribute("responsetimeDivStr", responsetimeDivStr);
		request.setAttribute("pingChartDivStr", pingChartDivStr);
		request.setAttribute("starttime", starttime);
		request.setAttribute("totime", totime);
		session.setAttribute("pingpath", pingpath);
//		session.setAttribute("responsetimepath", responsetimepath);
		session.setAttribute("orderList", orderList);
		request.setAttribute("startdate", startdate);
		request.setAttribute("todate", todate);
//		request.setAttribute("pinglist", list);
		return "/capreport/net/hdsEnv.jsp";
	}
	
	private String storageDrive() {
		Date d = new Date();
		String startdate = getParaValue("startdate");
		if (startdate == null) {
			startdate = sdf0.format(d);
		}
		String todate = getParaValue("todate");
		if (todate == null) {
			todate = sdf0.format(d);
		}
		String starttime = startdate + " 00:00:00";
		String totime = todate + " 23:59:59";

		String ids = request.getParameter("id");
		String flag = (String) session.getAttribute("ids");
		if(flag != null && ids == null){
			ids = flag;
			request.getSession().removeAttribute("ids");
		}
		// �������־ȡ���˿����¼�¼���б�
		String orderflag = "ipaddress";
		if (getParaValue("orderflag") != null && !getParaValue("orderflag").equals("")) {
			orderflag = getParaValue("orderflag");
		}
 
		List orderList = new ArrayList();
		if (ids != null) {
			session.setAttribute("ids", ids);
				HostNodeDao dao = new HostNodeDao();
				HostNode node = dao.loadHost(Integer.parseInt(ids));
                String ip =node.getIpAddress();
                if(node.getIpAddress().contains(".")){
                	ip=node.getIpAddress().replace(".", "_");
                }
				Hashtable pinghash = new Hashtable();
				try {
					pinghash = this.getCategory(ip, "edrive",  starttime, totime);
				} catch (Exception e) {
					e.printStackTrace();
				}
				Hashtable responsehash = new Hashtable();
				Hashtable ipmemhash = new Hashtable();
				ipmemhash.put("hostnode", node);
				ipmemhash.put("pinghash", pinghash);
				ipmemhash.put("ipaddress", node.getIpAddress() + "(" + node.getAlias() + ")");
				orderList.add(ipmemhash);

		}
		String pingChartDivStr = ReportHelper.getChartDivStr(orderList, "ping");
//		String responsetimeDivStr = ReportHelper.getChartDivStr(orderList, "responsetime");
		// ��ͨ��
		ReportValue pingReportValue = ReportHelper.getReportValue(orderList, "ping");
		ReportValue responseReportValue = ReportHelper.getReportValue(orderList, "responsetime");
		String pingpath = new ReportExport().makeJfreeChartData(pingReportValue.getListValue(), pingReportValue.getIpList(), "����״̬", "", "");
//		String responsetimepath = new ReportExport().makeJfreeChartData(responseReportValue.getListValue(), responseReportValue.getIpList(), "��Ӧʱ��", "ʱ��", "");
        System.out.println(pingReportValue.getListValue().size());
//		request.setAttribute("responsetimeDivStr", responsetimeDivStr);
		request.setAttribute("pingChartDivStr", pingChartDivStr);
		request.setAttribute("starttime", starttime);
		request.setAttribute("totime", totime);
		session.setAttribute("pingpath", pingpath);
//		session.setAttribute("responsetimepath", responsetimepath);
		session.setAttribute("orderList", orderList);
		request.setAttribute("startdate", startdate);
		request.setAttribute("todate", todate);
//		request.setAttribute("pinglist", list);
		return "/capreport/net/hdsDrive.jsp";
	}
	
	private String storageCpu() {
		Date d = new Date();
		String startdate = getParaValue("startdate");
		if (startdate == null) {
			startdate = sdf0.format(d);
		}
		String todate = getParaValue("todate");
		if (todate == null) {
			todate = sdf0.format(d);
		}
		String starttime = startdate + " 00:00:00";
		String totime = todate + " 23:59:59";

		String ids = request.getParameter("id");
		String flag = (String) session.getAttribute("ids");
		if(flag != null && ids == null){
			ids = flag;
			request.getSession().removeAttribute("ids");
		}
		// �������־ȡ���˿����¼�¼���б�
		String orderflag = "ipaddress";
		if (getParaValue("orderflag") != null && !getParaValue("orderflag").equals("")) {
			orderflag = getParaValue("orderflag");
		}
 
		List orderList = new ArrayList();
		if (ids != null) {
			session.setAttribute("ids", ids);
				HostNodeDao dao = new HostNodeDao();
				HostNode node = dao.loadHost(Integer.parseInt(ids));
                String ip =node.getIpAddress();
                if(node.getIpAddress().contains(".")){
                	ip=node.getIpAddress().replace(".", "_");
                }
				Hashtable pinghash = new Hashtable();
				try {
					pinghash = this.getCategory(ip, "rcpu",  starttime, totime);
				} catch (Exception e) {
					e.printStackTrace();
				}
				Hashtable responsehash = new Hashtable();
				Hashtable ipmemhash = new Hashtable();
				ipmemhash.put("hostnode", node);
				ipmemhash.put("pinghash", pinghash);
				ipmemhash.put("ipaddress", node.getIpAddress() + "(" + node.getAlias() + ")");
				orderList.add(ipmemhash);

		}
		String pingChartDivStr = ReportHelper.getChartDivStr(orderList, "ping");
//		String responsetimeDivStr = ReportHelper.getChartDivStr(orderList, "responsetime");
		// ��ͨ��
		ReportValue pingReportValue = ReportHelper.getReportValue(orderList, "ping");
		ReportValue responseReportValue = ReportHelper.getReportValue(orderList, "responsetime");
		String pingpath = new ReportExport().makeJfreeChartData(pingReportValue.getListValue(), pingReportValue.getIpList(), "CPU", "", "");
//		String responsetimepath = new ReportExport().makeJfreeChartData(responseReportValue.getListValue(), responseReportValue.getIpList(), "��Ӧʱ��", "ʱ��", "");
        System.out.println(pingReportValue.getListValue().size());
//		request.setAttribute("responsetimeDivStr", responsetimeDivStr);
		request.setAttribute("pingChartDivStr", pingChartDivStr);
		request.setAttribute("starttime", starttime);
		request.setAttribute("totime", totime);
		session.setAttribute("pingpath", pingpath);
//		session.setAttribute("responsetimepath", responsetimepath);
		session.setAttribute("orderList", orderList);
		request.setAttribute("startdate", startdate);
		request.setAttribute("todate", todate);
//		request.setAttribute("pinglist", list);
		return "/capreport/net/hdsCpu.jsp";
	}
	
	
	
	
	private String storageCache() {
		Date d = new Date();
		String startdate = getParaValue("startdate");
		if (startdate == null) {
			startdate = sdf0.format(d);
		}
		String todate = getParaValue("todate");
		if (todate == null) {
			todate = sdf0.format(d);
		}
		String starttime = startdate + " 00:00:00";
		String totime = todate + " 23:59:59";

		String ids = request.getParameter("id");
		String flag = (String) session.getAttribute("ids");
		if(flag != null && ids == null){
			ids = flag;
			request.getSession().removeAttribute("ids");
		}
		// �������־ȡ���˿����¼�¼���б�
		String orderflag = "ipaddress";
		if (getParaValue("orderflag") != null && !getParaValue("orderflag").equals("")) {
			orderflag = getParaValue("orderflag");
		}
 
		List orderList = new ArrayList();
		if (ids != null) {
			session.setAttribute("ids", ids);
				HostNodeDao dao = new HostNodeDao();
				HostNode node = dao.loadHost(Integer.parseInt(ids));
                String ip =node.getIpAddress();
                if(node.getIpAddress().contains(".")){
                	ip=node.getIpAddress().replace(".", "_");
                }
				Hashtable pinghash = new Hashtable();
				try {
					pinghash = this.getCategory(ip, "rcache",  starttime, totime);
				} catch (Exception e) {
					e.printStackTrace();
				}
				Hashtable responsehash = new Hashtable();
				Hashtable ipmemhash = new Hashtable();
				ipmemhash.put("hostnode", node);
				ipmemhash.put("pinghash", pinghash);
				ipmemhash.put("ipaddress", node.getIpAddress() + "(" + node.getAlias() + ")");
				orderList.add(ipmemhash);

		}
		String pingChartDivStr = ReportHelper.getChartDivStr(orderList, "ping");
//		String responsetimeDivStr = ReportHelper.getChartDivStr(orderList, "responsetime");
		// ��ͨ��
		ReportValue pingReportValue = ReportHelper.getReportValue(orderList, "ping");
		ReportValue responseReportValue = ReportHelper.getReportValue(orderList, "responsetime");
		String pingpath = new ReportExport().makeJfreeChartData(pingReportValue.getListValue(), pingReportValue.getIpList(), "����", "", "");
//		String responsetimepath = new ReportExport().makeJfreeChartData(responseReportValue.getListValue(), responseReportValue.getIpList(), "��Ӧʱ��", "ʱ��", "");
        System.out.println(pingReportValue.getListValue().size());
//		request.setAttribute("responsetimeDivStr", responsetimeDivStr);
		request.setAttribute("pingChartDivStr", pingChartDivStr);
		request.setAttribute("starttime", starttime);
		request.setAttribute("totime", totime);
		session.setAttribute("pingpath", pingpath);
//		session.setAttribute("responsetimepath", responsetimepath);
		session.setAttribute("orderList", orderList);
		request.setAttribute("startdate", startdate);
		request.setAttribute("todate", todate);
//		request.setAttribute("pinglist", list);
		return "/capreport/net/hdsCache.jsp";
	}



	private String storageCable() {
		Date d = new Date();
		String startdate = getParaValue("startdate");
		if (startdate == null) {
			startdate = sdf0.format(d);
		}
		String todate = getParaValue("todate");
		if (todate == null) {
			todate = sdf0.format(d);
		}
		String starttime = startdate + " 00:00:00";
		String totime = todate + " 23:59:59";

		String ids = request.getParameter("id");
		String flag = (String) session.getAttribute("ids");
		if(flag != null && ids == null){
			ids = flag;
			request.getSession().removeAttribute("ids");
		}
		// �������־ȡ���˿����¼�¼���б�
		String orderflag = "ipaddress";
		if (getParaValue("orderflag") != null && !getParaValue("orderflag").equals("")) {
			orderflag = getParaValue("orderflag");
		}
 
		List orderList = new ArrayList();
		if (ids != null) {
			session.setAttribute("ids", ids);
				HostNodeDao dao = new HostNodeDao();
				HostNode node = dao.loadHost(Integer.parseInt(ids));
                String ip =node.getIpAddress();
                if(node.getIpAddress().contains(".")){
                	ip=node.getIpAddress().replace(".", "_");
                }
				Hashtable pinghash = new Hashtable();
				try {
					pinghash = this.getCategory(ip, "rcable",  starttime, totime);
				} catch (Exception e) {
					e.printStackTrace();
				}
				Hashtable responsehash = new Hashtable();
				Hashtable ipmemhash = new Hashtable();
				ipmemhash.put("hostnode", node);
				ipmemhash.put("pinghash", pinghash);
				ipmemhash.put("ipaddress", node.getIpAddress() + "(" + node.getAlias() + ")");
				orderList.add(ipmemhash);

		}
		String pingChartDivStr = ReportHelper.getChartDivStr(orderList, "ping");
//		String responsetimeDivStr = ReportHelper.getChartDivStr(orderList, "responsetime");
		// ��ͨ��
		ReportValue pingReportValue = ReportHelper.getReportValue(orderList, "ping");
		ReportValue responseReportValue = ReportHelper.getReportValue(orderList, "responsetime");
		String pingpath = new ReportExport().makeJfreeChartData(pingReportValue.getListValue(), pingReportValue.getIpList(), "�ڲ�����", "", "");
//		String responsetimepath = new ReportExport().makeJfreeChartData(responseReportValue.getListValue(), responseReportValue.getIpList(), "��Ӧʱ��", "ʱ��", "");
        System.out.println(pingReportValue.getListValue().size());
//		request.setAttribute("responsetimeDivStr", responsetimeDivStr);
		request.setAttribute("pingChartDivStr", pingChartDivStr);
		request.setAttribute("starttime", starttime);
		request.setAttribute("totime", totime);
		session.setAttribute("pingpath", pingpath);
//		session.setAttribute("responsetimepath", responsetimepath);
		session.setAttribute("orderList", orderList);
		request.setAttribute("startdate", startdate);
		request.setAttribute("todate", todate);
//		request.setAttribute("pinglist", list);
		return "/capreport/net/hdsCable.jsp";
	}
	
	private String storageButter() {
		Date d = new Date();
		String startdate = getParaValue("startdate");
		if (startdate == null) {
			startdate = sdf0.format(d);
		}
		String todate = getParaValue("todate");
		if (todate == null) {
			todate = sdf0.format(d);
		}
		String starttime = startdate + " 00:00:00";
		String totime = todate + " 23:59:59";

		String ids = request.getParameter("id");
		String flag = (String) session.getAttribute("ids");
		if(flag != null && ids == null){
			ids = flag;
			request.getSession().removeAttribute("ids");
		}
		// �������־ȡ���˿����¼�¼���б�
		String orderflag = "ipaddress";
		if (getParaValue("orderflag") != null && !getParaValue("orderflag").equals("")) {
			orderflag = getParaValue("orderflag");
		}
 
		List orderList = new ArrayList();
		if (ids != null) {
			    session.setAttribute("ids", ids);
				HostNodeDao dao = new HostNodeDao();
				HostNode node = dao.loadHost(Integer.parseInt(ids));
                String ip =node.getIpAddress();
                if(node.getIpAddress().contains(".")){
                	ip=node.getIpAddress().replace(".", "_");
                }
				Hashtable pinghash = new Hashtable();
				try {
					pinghash = this.getCategory(ip, "rbutter",  starttime, totime);
				} catch (Exception e) {
					e.printStackTrace();
				}
				Hashtable responsehash = new Hashtable();
				Hashtable ipmemhash = new Hashtable();
				ipmemhash.put("hostnode", node);
				ipmemhash.put("pinghash", pinghash);
				ipmemhash.put("ipaddress", node.getIpAddress() + "(" + node.getAlias() + ")");
				orderList.add(ipmemhash);

		}
		String pingChartDivStr = ReportHelper.getChartDivStr(orderList, "ping");
//		String responsetimeDivStr = ReportHelper.getChartDivStr(orderList, "responsetime");
		// ��ͨ��
		ReportValue pingReportValue = ReportHelper.getReportValue(orderList, "ping");
		ReportValue responseReportValue = ReportHelper.getReportValue(orderList, "responsetime");
		String pingpath = new ReportExport().makeJfreeChartData(pingReportValue.getListValue(), pingReportValue.getIpList(), "���", "", "");
//		String responsetimepath = new ReportExport().makeJfreeChartData(responseReportValue.getListValue(), responseReportValue.getIpList(), "��Ӧʱ��", "ʱ��", "");
        System.out.println(pingReportValue.getListValue().size());
//		request.setAttribute("responsetimeDivStr", responsetimeDivStr);
		request.setAttribute("pingChartDivStr", pingChartDivStr);
		request.setAttribute("starttime", starttime);
		request.setAttribute("totime", totime);
		session.setAttribute("pingpath", pingpath);
//		session.setAttribute("responsetimepath", responsetimepath);
		session.setAttribute("orderList", orderList);
		request.setAttribute("startdate", startdate);
		request.setAttribute("todate", todate);
//		request.setAttribute("pinglist", list);
		return "/capreport/net/hdsButter.jsp";
	}
	
	private String storageMemory() {
		Date d = new Date();
		String startdate = getParaValue("startdate");
		if (startdate == null) {
			startdate = sdf0.format(d);
		}
		String todate = getParaValue("todate");
		if (todate == null) {
			todate = sdf0.format(d);
		}
		String starttime = startdate + " 00:00:00";
		String totime = todate + " 23:59:59";

		String ids = request.getParameter("id");
		String flag = (String) session.getAttribute("ids");
		if(flag != null && ids == null){
			ids = flag;
			request.getSession().removeAttribute("ids");
		}
		// �������־ȡ���˿����¼�¼���б�
		String orderflag = "ipaddress";
		if (getParaValue("orderflag") != null && !getParaValue("orderflag").equals("")) {
			orderflag = getParaValue("orderflag");
		}
 
		List orderList = new ArrayList();
		if (ids != null) {
			    session.setAttribute("ids", ids);
				HostNodeDao dao = new HostNodeDao();
				HostNode node = dao.loadHost(Integer.parseInt(ids));
                String ip =node.getIpAddress();
                if(node.getIpAddress().contains(".")){
                	ip=node.getIpAddress().replace(".", "_");
                }
				Hashtable pinghash = new Hashtable();
				try {
					pinghash = this.getCategory(ip, "rmemory",  starttime, totime);
				} catch (Exception e) {
					e.printStackTrace();
				}
				Hashtable responsehash = new Hashtable();
				Hashtable ipmemhash = new Hashtable();
				ipmemhash.put("hostnode", node);
				ipmemhash.put("pinghash", pinghash);
				ipmemhash.put("ipaddress", node.getIpAddress() + "(" + node.getAlias() + ")");
				orderList.add(ipmemhash);

		}
		String pingChartDivStr = ReportHelper.getChartDivStr(orderList, "ping");
//		String responsetimeDivStr = ReportHelper.getChartDivStr(orderList, "responsetime");
		// ��ͨ��
		ReportValue pingReportValue = ReportHelper.getReportValue(orderList, "ping");
		ReportValue responseReportValue = ReportHelper.getReportValue(orderList, "responsetime");
		String pingpath = new ReportExport().makeJfreeChartData(pingReportValue.getListValue(), pingReportValue.getIpList(), "�����ڴ�", "", "");
//		String responsetimepath = new ReportExport().makeJfreeChartData(responseReportValue.getListValue(), responseReportValue.getIpList(), "��Ӧʱ��", "ʱ��", "");
        System.out.println(pingReportValue.getListValue().size());
//		request.setAttribute("responsetimeDivStr", responsetimeDivStr);
		request.setAttribute("pingChartDivStr", pingChartDivStr);
		request.setAttribute("starttime", starttime);
		request.setAttribute("totime", totime);
		session.setAttribute("pingpath", pingpath);
//		session.setAttribute("responsetimepath", responsetimepath);
		session.setAttribute("orderList", orderList);
		request.setAttribute("startdate", startdate);
		request.setAttribute("todate", todate);
//		request.setAttribute("pinglist", list);
		return "/capreport/net/hdsMemory.jsp";
	}
	
	
	public void createDocContextStorage(String file,String flag) throws DocumentException, IOException {
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
		
		Paragraph title = null;
		if(flag.equalsIgnoreCase("rpower")){
			title = new Paragraph("�洢�豸��Դ����", titleFont);
		}else if(flag.equalsIgnoreCase("rfan")){
			title = new Paragraph("�洢�豸���ȱ���", titleFont);
		}else if(flag.equalsIgnoreCase("rcpu")){
			title = new Paragraph("�洢�豸CPU����", titleFont);
		}else if(flag.equalsIgnoreCase("rcable")){
			title = new Paragraph("�洢�豸�ڲ����߱���", titleFont);
		}else if(flag.equalsIgnoreCase("rcache")){
			title = new Paragraph("�洢�豸���汨��", titleFont);
		}else if(flag.equalsIgnoreCase("rbutter")){
			title = new Paragraph("�洢�豸��ر���", titleFont);
		}else if(flag.equalsIgnoreCase("rmemory")){
			title = new Paragraph("�洢�豸�����ڴ汨��", titleFont);
		}else if(flag.equalsIgnoreCase("edrive")){
			title = new Paragraph("�洢�豸��������", titleFont);
		}else if(flag.equalsIgnoreCase("renv")){
			title = new Paragraph("�洢�豸��������", titleFont);
		}else if(flag.equalsIgnoreCase("rswitch")){
			title = new Paragraph("ת�����ر���", titleFont);
		}else if(flag.equalsIgnoreCase("rluncon")){
			title = new Paragraph("����������״̬����", titleFont);
		}else if(flag.equalsIgnoreCase("rnumber")){
			title = new Paragraph("���ʹ��״̬����", titleFont);
		}else if(flag.equalsIgnoreCase("rwwncon")){
			title = new Paragraph("wwn������״̬����", titleFont);
		}else if(flag.equalsIgnoreCase("rsluncon")){
			title = new Paragraph("slun������״̬����", titleFont);
		}else if(flag.equalsIgnoreCase("rsafety")){
			title = new Paragraph("wwn��ȫ����", titleFont);
		}
		// ���ñ����ʽ���뷽ʽ
		title.setAlignment(Element.ALIGN_CENTER);
		// title.setFont(titleFont);
		document.add(title);
		// ���� Table ���
		Font fontChinese = new Font(bfChinese, 12, Font.NORMAL, Color.black);
//		List pinglist = (List) session.getAttribute("pinglist");
		Table aTable = new Table(6);
		int width[] = { 50, 50, 50, 50, 50, 50  };
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

		String pingpath = (String) session.getAttribute("pingpath");
		Image img = Image.getInstance(pingpath);
		img.setAbsolutePosition(0, 0);
		img.setAlignment(Image.LEFT);// ����ͼƬ��ʾλ��
		document.add(img);
		
		Cell cell1 = new Cell("ͼ����");
		Cell cell2 = new Cell("1-����");
		Cell cell3 = new Cell("2-����");
		Cell cell4 = new Cell("3-����");
		Cell cell5 = new Cell("4-�е�");
		Cell cell6 = new Cell("5-ά��");
		cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell5.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell4.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell6.setVerticalAlignment(Element.ALIGN_MIDDLE);

		cell1.setBackgroundColor(Color.LIGHT_GRAY);
		cell5.setBackgroundColor(Color.LIGHT_GRAY);
		cell2.setBackgroundColor(Color.LIGHT_GRAY);
		cell3.setBackgroundColor(Color.LIGHT_GRAY);
		cell4.setBackgroundColor(Color.LIGHT_GRAY);
		cell6.setBackgroundColor(Color.LIGHT_GRAY);
		
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
		if(!flag.equals("rswitch") && !flag.equals("rluncon") && !flag.equals("rsluncon") && !flag.equals("rwwncon") && !flag.equals("rsafety") && !flag.equals("rnumber"))
		{
			document.add(aTable);
		}
		document.close();
	}

	// zhushouzhi���÷�������ͨ�ʱ�����
	public String createdocStorage() {
		String file = "";// ���浽��Ŀ�ļ����µ�ָ���ļ���
		String flag = request.getParameter("flag");
		if(flag.equalsIgnoreCase("rpower")){
			file = "/temp/dianyuanbaobiao.doc";
		}else if(flag.equalsIgnoreCase("rfan")){
			file = "/temp/fengshanbaobiao.doc";
		}else if(flag.equalsIgnoreCase("rcpu")){
			file = "/temp/cpubaobiao.doc";
		}else if(flag.equalsIgnoreCase("rcable")){
			file = "/temp/neibuzongxianbaobiao.doc";
		}else if(flag.equalsIgnoreCase("rcache")){
			file = "/temp/huancunbaobiao.doc";
		}else if(flag.equalsIgnoreCase("rbutter")){
			file = "/temp/dianchibaobiao.doc";
		}else if(flag.equalsIgnoreCase("rmemory")){
			file = "/temp/gongxiangneicunbaobiao.doc";
		}else if(flag.equalsIgnoreCase("edrive")){
			file = "/temp/qudongbaobiao.doc";
		}else if(flag.equalsIgnoreCase("renv")){
			file = "/temp/huanjingbaobiao.doc";
		}else if(flag.equalsIgnoreCase("rnumber")){
			file = "/temp/numberbaobiao.doc";
		}else if(flag.equalsIgnoreCase("rluncon")){
			file = "/temp/lunControllerbaobiao.doc";
		}else if(flag.equalsIgnoreCase("rswitch")){
			file = "/temp/switchbaobiao.doc";
		}else if(flag.equalsIgnoreCase("rwwncon")){
			file = "/temp/wwnControllerbaobiao.doc";
		}else if(flag.equalsIgnoreCase("rsafety")){
			file = "/temp/safetybaobiao.doc";
		}else if(flag.equalsIgnoreCase("rsluncon")){
			file = "/temp/slunControllerbaobiao.doc";
		}
		String fileName = ResourceCenter.getInstance().getSysPath() + file;// ��ȡϵͳ�ļ���·��
		try {
			createDocContextStorage(fileName,flag);
//			request.getSession().removeAttribute("pingpath");
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		request.setAttribute("filename", fileName);
		return "/capreport/net/download.jsp";
	}
	
	public void createPdfStorage(String file,String flag) throws DocumentException, IOException {

		// com.lowagie.text.Font FontChinese = new
		// com.lowagie.text.Font(bfChinese, 12, com.lowagie.text.Font.NORMAL);

		// ����ֽ�Ŵ�С

		Document document = new Document(PageSize.A4);
		// ����һ����д��(Writer)��document���������ͨ����д��(Writer)���Խ��ĵ�д�뵽������
		PdfWriter.getInstance(document, new FileOutputStream(file));
		document.open();
		// ������������
		BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
		// ����������
		Font titleFont = new Font(bfChinese, 12, Font.BOLD);
		// com.lowagie.text.Font titleFont = new
		// com.lowagie.text.Font(bfChinese, 12, com.lowagie.text.Font.NORMAL);
		// ����������
		Font contextFont = new com.lowagie.text.Font(bfChinese, 11, Font.NORMAL);
		Paragraph title = null;
		if(flag.equalsIgnoreCase("rpower")){
			title = new Paragraph("�洢�豸��Դ����", titleFont);
		}else if(flag.equalsIgnoreCase("rfan")){
			title = new Paragraph("�洢�豸���ȱ���", titleFont);
		}else if(flag.equalsIgnoreCase("rcpu")){
			title = new Paragraph("�洢�豸CPU����", titleFont);
		}else if(flag.equalsIgnoreCase("rcable")){
			title = new Paragraph("�洢�豸�ڲ����߱���", titleFont);
		}else if(flag.equalsIgnoreCase("rcache")){
			title = new Paragraph("�洢�豸���汨��", titleFont);
		}else if(flag.equalsIgnoreCase("rbutter")){
			title = new Paragraph("�洢�豸��ر���", titleFont);
		}else if(flag.equalsIgnoreCase("rmemory")){
			title = new Paragraph("�洢�豸�����ڴ汨��", titleFont);
		}else if(flag.equalsIgnoreCase("edrive")){
			title = new Paragraph("�洢�豸��������", titleFont);
		}else if(flag.equalsIgnoreCase("renv")){
			title = new Paragraph("�洢�豸��������", titleFont);
		}else if(flag.equalsIgnoreCase("rluncon")){
			title = new Paragraph("����������״̬����", titleFont);
		}else if(flag.equalsIgnoreCase("rnumber")){
			title = new Paragraph("���ʹ��״̬����", titleFont);
		}else if(flag.equalsIgnoreCase("rwwncon")){
			title = new Paragraph("wwn������״̬����", titleFont);
		}else if(flag.equalsIgnoreCase("rsluncon")){
			title = new Paragraph("slun������״̬����", titleFont);
		}else if(flag.equalsIgnoreCase("rsafety")){
			title = new Paragraph("wwn��ȫ����", titleFont);
		}else if(flag.equalsIgnoreCase("rswitch")){
			title = new Paragraph("ת������", titleFont);
		}
		// ���ñ����ʽ���뷽ʽ
		title.setAlignment(Element.ALIGN_CENTER);
		// title.setFont(titleFont);
		document.add(title);
		document.add(new Paragraph("\n"));
		// ���� Table ���
		Font fontChinese = new Font(bfChinese, 11, Font.NORMAL, Color.black);
		PdfPTable aTable = new PdfPTable(6);

		int width[] = { 50, 50, 50, 50, 50, 50};
		aTable.setWidthPercentage(100);
		aTable.setWidths(width);

//		PdfPCell c = new PdfPCell(new Phrase("", fontChinese));
//		c.setBackgroundColor(Color.LIGHT_GRAY);
//		aTable.addCell(c);
		PdfPCell cell1 = new PdfPCell(new Phrase("ͼ����", fontChinese));
		PdfPCell cell11 = new PdfPCell(new Phrase("1-����", fontChinese));
		PdfPCell cell2 = new PdfPCell(new Phrase("2-����", fontChinese));
		PdfPCell cell3 = new PdfPCell(new Phrase("3-����", fontChinese));
		PdfPCell cell4 = new PdfPCell(new Phrase("4-�е�", fontChinese));
		PdfPCell cell15 = new PdfPCell(new Phrase("5-ά��", fontChinese));
		cell1.setBackgroundColor(Color.LIGHT_GRAY);
		cell11.setBackgroundColor(Color.LIGHT_GRAY);
		cell2.setBackgroundColor(Color.LIGHT_GRAY);
		cell3.setBackgroundColor(Color.LIGHT_GRAY);
		cell4.setBackgroundColor(Color.LIGHT_GRAY);
		cell15.setBackgroundColor(Color.LIGHT_GRAY);
		cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell11.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell15.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell11.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell4.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell15.setVerticalAlignment(Element.ALIGN_MIDDLE);
		aTable.addCell(cell1);
		aTable.addCell(cell11);
		aTable.addCell(cell2);
		aTable.addCell(cell3);
		aTable.addCell(cell4);
		aTable.addCell(cell15);
		// ������ͨ��
		String pingpath = (String) session.getAttribute("pingpath");
		// ������Ӧʱ��ͼƬ
		// document.add(new Paragraph("\n"));
		// String ipaddress = (String)request.getParameter("ipaddress");
		// String newip = doip(ipaddress);
		Image img = Image.getInstance(pingpath);
		img.setAlignment(Image.LEFT);// ����ͼƬ��ʾλ��
		img.scalePercent(75);
		document.add(img);
		if(!flag.equals("rswitch") && !flag.equals("rluncon") && !flag.equals("rsluncon") && !flag.equals("rwwncon") && !flag.equals("rsafety") && !flag.equals("rnumber"))
		{
			document.add(aTable);
		}
		document.close();
	}


	// zhushouzhi���÷�������ͨ�ʱ�����
	public String createpdfStorage() {
		String file = "";// ���浽��Ŀ�ļ����µ�ָ���ļ���
		String flag = request.getParameter("flag");
		if(flag.equalsIgnoreCase("rpower")){
			file = "/temp/dianyuanbaobiao.PDF";
		}else if(flag.equalsIgnoreCase("rfan")){
			file = "/temp/fengshanbaobiao.PDF";
		}else if(flag.equalsIgnoreCase("rcpu")){
			file = "/temp/cpubaobiao.PDF";
		}else if(flag.equalsIgnoreCase("rcable")){
			file = "/temp/neibuzongxianbaobiao.PDF";
		}else if(flag.equalsIgnoreCase("rcache")){
			file = "/temp/huancunbaobiao.PDF";
		}else if(flag.equalsIgnoreCase("rbutter")){
			file = "/temp/dianchibaobiao.PDF";
		}else if(flag.equalsIgnoreCase("rmemory")){
			file = "/temp/gongxiangneicunbaobiao.PDF";
		}else if(flag.equalsIgnoreCase("edrive")){
			file = "/temp/qudongbaobiao.PDF";
		}else if(flag.equalsIgnoreCase("renv")){
			file = "/temp/huanjingbaobiao.PDF";
		}else if(flag.equalsIgnoreCase("rnumber")){
			file = "/temp/numberbaobiao.PDF";
		}else if(flag.equalsIgnoreCase("rluncon")){
			file = "/temp/lunControllerbaobiao.PDF";
		}else if(flag.equalsIgnoreCase("rswitch")){
			file = "/temp/switchbaobiao.PDF";
		}else if(flag.equalsIgnoreCase("rwwncon")){
			file = "/temp/wwnControllerbaobiao.PDF";
		}else if(flag.equalsIgnoreCase("rsafety")){
			file = "/temp/safetybaobiao.PDF";
		}else if(flag.equalsIgnoreCase("rsluncon")){
			file = "/temp/slunControllerbaobiao.PDF";
		}
		String fileName = ResourceCenter.getInstance().getSysPath() + file;// ��ȡϵͳ�ļ���·��
		try {
			createPdfStorage(fileName,flag);
//			request.getSession().removeAttribute("pingpath");
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		request.setAttribute("filename", fileName);
		return "/capreport/net/download.jsp";
	}
	
	private String creatXlsStorage() {
		Date d = new Date();
		String startdate = getParaValue("startdate");
		String flag = request.getParameter("flag");
		if (startdate == null) {
			startdate = sdf0.format(d);
		}
		String todate = getParaValue("todate");
		if (todate == null) {
			todate = sdf0.format(d);
		}
		String starttime = startdate + " 00:00:00";
		String totime = todate + " 23:59:59";
		Hashtable allcpuhash = new Hashtable();

		List returnList = new ArrayList();
		// I_MonitorIpList monitorManager=new MonitoriplistManager();
//		List memlist = (List) session.getAttribute("pinglist");
		// ������ͨ��
		String pingpath = (String) session.getAttribute("pingpath");
		Hashtable reporthash = new Hashtable();
//		reporthash.put("pinglist", memlist);
		reporthash.put("starttime", starttime);
		reporthash.put("totime", totime);
		reporthash.put("pingpath", pingpath);

		ExcelReport1 report = new ExcelReport1(new IpResourceReport(), reporthash);
		String file = "";
		if(flag.equalsIgnoreCase("rpower")){
			file = "/temp/dianyuanbaobiao.xls";
		}else if(flag.equalsIgnoreCase("rfan")){
			file = "/temp/fengshanbaobiao.xls";
		}else if(flag.equalsIgnoreCase("rcpu")){
			file = "/temp/cpubaobiao.xls";
		}else if(flag.equalsIgnoreCase("rcable")){
			file = "/temp/neibuzongxianbaobiao.xls";
		}else if(flag.equalsIgnoreCase("rcache")){
			file = "/temp/huancunbaobiao.xls";
		}else if(flag.equalsIgnoreCase("rbutter")){
			file = "/temp/dianchibaobiao.xls";
		}else if(flag.equalsIgnoreCase("rmemory")){
			file = "/temp/gongxiangneicunbaobiao.xls";
		}else if(flag.equalsIgnoreCase("edrive")){
			file = "/temp/qudongbaobiao.xls";
		}else if(flag.equalsIgnoreCase("renv")){
			file = "/temp/huanjingbaobiao.xls";
		}else if(flag.equalsIgnoreCase("rnumber")){
			file = "/temp/numberbaobiao.xls";
		}else if(flag.equalsIgnoreCase("rluncon")){
			file = "/temp/lunControllerbaobiao.xls";
		}else if(flag.equalsIgnoreCase("rswitch")){
			file = "/temp/switchbaobiao.xls";
		}else if(flag.equalsIgnoreCase("rwwncon")){
			file = "/temp/wwnControllerbaobiao.xls";
		}else if(flag.equalsIgnoreCase("rsafety")){
			file = "/temp/safetybaobiao.xls";
		}else if(flag.equalsIgnoreCase("rsluncon")){
			file = "/temp/slunControllerbaobiao.xls";
		}
		report.createReport_storage(file,flag);
//		request.getSession().removeAttribute("pingpath");
		request.setAttribute("filename", report.getFileName());
		return "/capreport/net/download.jsp";
		// return mapping.findForward("report_info");
	}
	
	
	
	private String storageLunSwitch() {
		Date d = new Date();
		String startdate = getParaValue("startdate");
//		System.out.println(startdate+"*****************************************");
		if (startdate == null) {
			startdate = sdf0.format(d);
		}
		String todate = getParaValue("todate");
//		System.out.println("*****************************************"+todate);
		if (todate == null) {
			todate = sdf0.format(d);
		}
		String starttime = startdate + " 00:00:00";
		String totime = todate + " 23:59:59";
		String ids = getParaValue("id");
		String flag = (String) session.getAttribute("ids");
		if(flag != null && ids == null){
			ids = flag;
			request.getSession().removeAttribute("ids");
		}
		// �������־ȡ���˿����¼�¼���б�
		String orderflag = "ipaddress";
		if (getParaValue("orderflag") != null && !getParaValue("orderflag").equals("")) {
			orderflag = getParaValue("orderflag");
		}
 
		List orderList = new ArrayList();
		if (ids != null) {
			    session.setAttribute("ids", ids);
				HostNodeDao dao = new HostNodeDao();
				HostNode node = dao.loadHost(Integer.parseInt(ids));
                String ip =node.getIpAddress();
                if(node.getIpAddress().contains(".")){
                	ip=node.getIpAddress().replace(".", "_");
                }
				Hashtable pinghash = new Hashtable();
				try {
					pinghash = this.getCategory(ip, "rswitch",  starttime, totime);
				} catch (Exception e) {
					e.printStackTrace();
				}
				Hashtable responsehash = new Hashtable();
				Hashtable ipmemhash = new Hashtable();
				ipmemhash.put("hostnode", node);
				ipmemhash.put("pinghash", pinghash);
				ipmemhash.put("ipaddress", node.getIpAddress() + "(" + node.getAlias() + ")");
				orderList.add(ipmemhash);
		}
		String pingChartDivStr = ReportHelper.getChartDivStr(orderList, "ping");
//		System.out.println("--------amchart---------"+pingChartDivStr);
//		String responsetimeDivStr = ReportHelper.getChartDivStr(orderList, "responsetime");
		// ��ͨ��
		ReportValue pingReportValue = ReportHelper.getReportValue(orderList, "ping");
	//	ReportValue responseReportValue = ReportHelper.getReportValue(orderList, "responsetime");
		String pingpath = new ReportExport().makeJfreeChartData(pingReportValue.getListValue(), pingReportValue.getIpList(), "ת������", "", "");
//		String responsetimepath = new ReportExport().makeJfreeChartData(responseReportValue.getListValue(), responseReportValue.getIpList(), "��Ӧʱ��", "ʱ��", "");
        System.out.println(pingReportValue.getListValue().size());
//		request.setAttribute("responsetimeDivStr", responsetimeDivStr);
		request.setAttribute("pingChartDivStr", pingChartDivStr);
		request.setAttribute("starttime", starttime);
		request.setAttribute("totime", totime);
		session.setAttribute("pingpath", pingpath);
//		session.setAttribute("responsetimepath", responsetimepath);
		session.setAttribute("orderList", orderList);
		request.setAttribute("startdate", startdate);
		request.setAttribute("todate", todate);
//		request.setAttribute("pinglist", list);
//		request.setAttribute("id", ids);
		return "/capreport/net/hdsLunSwitch.jsp";
	}
	
	private String storageLunCon() {
		Date d = new Date();
		String startdate = getParaValue("startdate");
//		System.out.println(startdate+"*****************************************");
		if (startdate == null) {
			startdate = sdf0.format(d);
		}
		String todate = getParaValue("todate");
//		System.out.println("*****************************************"+todate);
		if (todate == null) {
			todate = sdf0.format(d);
		}
		String starttime = startdate + " 00:00:00";
		String totime = todate + " 23:59:59";
		String ids = getParaValue("id");
		String flag = (String) session.getAttribute("ids");
		if(flag != null && ids == null){
			ids = flag;
			request.getSession().removeAttribute("ids");
		}
		// �������־ȡ���˿����¼�¼���б�
		String orderflag = "ipaddress";
		if (getParaValue("orderflag") != null && !getParaValue("orderflag").equals("")) {
			orderflag = getParaValue("orderflag");
		}
 
		List orderList = new ArrayList();
		if (ids != null) {
			    session.setAttribute("ids", ids);
				HostNodeDao dao = new HostNodeDao();
				HostNode node = dao.loadHost(Integer.parseInt(ids));
                String ip =node.getIpAddress();
                if(node.getIpAddress().contains(".")){
                	ip=node.getIpAddress().replace(".", "_");
                }
				Hashtable pinghash = new Hashtable();
				try {
					pinghash = this.getCategory(ip, "rluncon",  starttime, totime);
				} catch (Exception e) {
					e.printStackTrace();
				}
				Hashtable responsehash = new Hashtable();
				Hashtable ipmemhash = new Hashtable();
				ipmemhash.put("hostnode", node);
				ipmemhash.put("pinghash", pinghash);
				ipmemhash.put("ipaddress", node.getIpAddress() + "(" + node.getAlias() + ")");
				orderList.add(ipmemhash);
		}
		String pingChartDivStr = ReportHelper.getChartDivStr(orderList, "ping");
//		System.out.println("--------amchart---------"+pingChartDivStr);
//		String responsetimeDivStr = ReportHelper.getChartDivStr(orderList, "responsetime");
		// ��ͨ��
		ReportValue pingReportValue = ReportHelper.getReportValue(orderList, "ping");
	//	ReportValue responseReportValue = ReportHelper.getReportValue(orderList, "responsetime");
		String pingpath = new ReportExport().makeJfreeChartData(pingReportValue.getListValue(), pingReportValue.getIpList(), "����������״̬", "", "");
//		String responsetimepath = new ReportExport().makeJfreeChartData(responseReportValue.getListValue(), responseReportValue.getIpList(), "��Ӧʱ��", "ʱ��", "");
        System.out.println(pingReportValue.getListValue().size());
//		request.setAttribute("responsetimeDivStr", responsetimeDivStr);
		request.setAttribute("pingChartDivStr", pingChartDivStr);
		request.setAttribute("starttime", starttime);
		request.setAttribute("totime", totime);
		session.setAttribute("pingpath", pingpath);
//		session.setAttribute("responsetimepath", responsetimepath);
		session.setAttribute("orderList", orderList);
		request.setAttribute("startdate", startdate);
		request.setAttribute("todate", todate);
//		request.setAttribute("pinglist", list);
//		request.setAttribute("id", ids);
		return "/capreport/net/hdsLunCon.jsp";
	}
	
	
	private String storageWwnNumber() {
		Date d = new Date();
		String startdate = getParaValue("startdate");
//		System.out.println(startdate+"*****************************************");
		if (startdate == null) {
			startdate = sdf0.format(d);
		}
		String todate = getParaValue("todate");
//		System.out.println("*****************************************"+todate);
		if (todate == null) {
			todate = sdf0.format(d);
		}
		String starttime = startdate + " 00:00:00";
		String totime = todate + " 23:59:59";
		String ids = getParaValue("id");
		String flag = (String) session.getAttribute("ids");
		if(flag != null && ids == null){
			ids = flag;
			request.getSession().removeAttribute("ids");
		}
		// �������־ȡ���˿����¼�¼���б�
		String orderflag = "ipaddress";
		if (getParaValue("orderflag") != null && !getParaValue("orderflag").equals("")) {
			orderflag = getParaValue("orderflag");
		}
 
		List orderList = new ArrayList();
		if (ids != null) {
			    session.setAttribute("ids", ids);
				HostNodeDao dao = new HostNodeDao();
				HostNode node = dao.loadHost(Integer.parseInt(ids));
                String ip =node.getIpAddress();
                if(node.getIpAddress().contains(".")){
                	ip=node.getIpAddress().replace(".", "_");
                }
				Hashtable pinghash = new Hashtable();
				try {
					pinghash = this.getCategory(ip, "rnumber",  starttime, totime);
				} catch (Exception e) {
					e.printStackTrace();
				}
				Hashtable responsehash = new Hashtable();
				Hashtable ipmemhash = new Hashtable();
				ipmemhash.put("hostnode", node);
				ipmemhash.put("pinghash", pinghash);
				ipmemhash.put("ipaddress", node.getIpAddress() + "(" + node.getAlias() + ")");
				orderList.add(ipmemhash);
		}
		String pingChartDivStr = ReportHelper.getChartDivStr(orderList, "ping");
//		System.out.println("--------amchart---------"+pingChartDivStr);
//		String responsetimeDivStr = ReportHelper.getChartDivStr(orderList, "responsetime");
		// ��ͨ��
		ReportValue pingReportValue = ReportHelper.getReportValue(orderList, "ping");
	//	ReportValue responseReportValue = ReportHelper.getReportValue(orderList, "responsetime");
		String pingpath = new ReportExport().makeJfreeChartData(pingReportValue.getListValue(), pingReportValue.getIpList(), "���ʹ��״̬", "", "");
//		String responsetimepath = new ReportExport().makeJfreeChartData(responseReportValue.getListValue(), responseReportValue.getIpList(), "��Ӧʱ��", "ʱ��", "");
        System.out.println(pingReportValue.getListValue().size());
//		request.setAttribute("responsetimeDivStr", responsetimeDivStr);
		request.setAttribute("pingChartDivStr", pingChartDivStr);
		request.setAttribute("starttime", starttime);
		request.setAttribute("totime", totime);
		session.setAttribute("pingpath", pingpath);
//		session.setAttribute("responsetimepath", responsetimepath);
		session.setAttribute("orderList", orderList);
		request.setAttribute("startdate", startdate);
		request.setAttribute("todate", todate);
//		request.setAttribute("pinglist", list);
//		request.setAttribute("id", ids);
		return "/capreport/net/hdsWwnNumber.jsp";
	}
	
	private String storageWwnCon() {
		Date d = new Date();
		String startdate = getParaValue("startdate");
//		System.out.println(startdate+"*****************************************");
		if (startdate == null) {
			startdate = sdf0.format(d);
		}
		String todate = getParaValue("todate");
//		System.out.println("*****************************************"+todate);
		if (todate == null) {
			todate = sdf0.format(d);
		}
		String starttime = startdate + " 00:00:00";
		String totime = todate + " 23:59:59";
		String ids = getParaValue("id");
		String flag = (String) session.getAttribute("ids");
		if(flag != null && ids == null){
			ids = flag;
			request.getSession().removeAttribute("ids");
		}
		// �������־ȡ���˿����¼�¼���б�
		String orderflag = "ipaddress";
		if (getParaValue("orderflag") != null && !getParaValue("orderflag").equals("")) {
			orderflag = getParaValue("orderflag");
		}
 
		List orderList = new ArrayList();
		if (ids != null) {
			    session.setAttribute("ids", ids);
				HostNodeDao dao = new HostNodeDao();
				HostNode node = dao.loadHost(Integer.parseInt(ids));
                String ip =node.getIpAddress();
                if(node.getIpAddress().contains(".")){
                	ip=node.getIpAddress().replace(".", "_");
                }
				Hashtable pinghash = new Hashtable();
				try {
					pinghash = this.getCategory(ip, "rwwncon",  starttime, totime);
				} catch (Exception e) {
					e.printStackTrace();
				}
				Hashtable responsehash = new Hashtable();
				Hashtable ipmemhash = new Hashtable();
				ipmemhash.put("hostnode", node);
				ipmemhash.put("pinghash", pinghash);
				ipmemhash.put("ipaddress", node.getIpAddress() + "(" + node.getAlias() + ")");
				orderList.add(ipmemhash);
		}
		String pingChartDivStr = ReportHelper.getChartDivStr(orderList, "ping");
//		System.out.println("--------amchart---------"+pingChartDivStr);
//		String responsetimeDivStr = ReportHelper.getChartDivStr(orderList, "responsetime");
		// ��ͨ��
		ReportValue pingReportValue = ReportHelper.getReportValue(orderList, "ping");
	//	ReportValue responseReportValue = ReportHelper.getReportValue(orderList, "responsetime");
		String pingpath = new ReportExport().makeJfreeChartData(pingReportValue.getListValue(), pingReportValue.getIpList(), "wwn������״̬", "", "");
//		String responsetimepath = new ReportExport().makeJfreeChartData(responseReportValue.getListValue(), responseReportValue.getIpList(), "��Ӧʱ��", "ʱ��", "");
        System.out.println(pingReportValue.getListValue().size());
//		request.setAttribute("responsetimeDivStr", responsetimeDivStr);
		request.setAttribute("pingChartDivStr", pingChartDivStr);
		request.setAttribute("starttime", starttime);
		request.setAttribute("totime", totime);
		session.setAttribute("pingpath", pingpath);
//		session.setAttribute("responsetimepath", responsetimepath);
		session.setAttribute("orderList", orderList);
		request.setAttribute("startdate", startdate);
		request.setAttribute("todate", todate);
//		request.setAttribute("pinglist", list);
//		request.setAttribute("id", ids);
		return "/capreport/net/hdsWwnCon.jsp";
	}
	
	private String storageSlunSafety() {
		Date d = new Date();
		String startdate = getParaValue("startdate");
//		System.out.println(startdate+"*****************************************");
		if (startdate == null) {
			startdate = sdf0.format(d);
		}
		String todate = getParaValue("todate");
//		System.out.println("*****************************************"+todate);
		if (todate == null) {
			todate = sdf0.format(d);
		}
		String starttime = startdate + " 00:00:00";
		String totime = todate + " 23:59:59";
		String ids = getParaValue("id");
		String flag = (String) session.getAttribute("ids");
		if(flag != null && ids == null){
			ids = flag;
			request.getSession().removeAttribute("ids");
		}
		// �������־ȡ���˿����¼�¼���б�
		String orderflag = "ipaddress";
		if (getParaValue("orderflag") != null && !getParaValue("orderflag").equals("")) {
			orderflag = getParaValue("orderflag");
		}
 
		List orderList = new ArrayList();
		if (ids != null) {
			    session.setAttribute("ids", ids);
				HostNodeDao dao = new HostNodeDao();
				HostNode node = dao.loadHost(Integer.parseInt(ids));
                String ip =node.getIpAddress();
                if(node.getIpAddress().contains(".")){
                	ip=node.getIpAddress().replace(".", "_");
                }
				Hashtable pinghash = new Hashtable();
				try {
					pinghash = this.getCategory(ip, "rsafety",  starttime, totime);
				} catch (Exception e) {
					e.printStackTrace();
				}
				Hashtable responsehash = new Hashtable();
				Hashtable ipmemhash = new Hashtable();
				ipmemhash.put("hostnode", node);
				ipmemhash.put("pinghash", pinghash);
				ipmemhash.put("ipaddress", node.getIpAddress() + "(" + node.getAlias() + ")");
				orderList.add(ipmemhash);
		}
		String pingChartDivStr = ReportHelper.getChartDivStr(orderList, "ping");
//		System.out.println("--------amchart---------"+pingChartDivStr);
//		String responsetimeDivStr = ReportHelper.getChartDivStr(orderList, "responsetime");
		// ��ͨ��
		ReportValue pingReportValue = ReportHelper.getReportValue(orderList, "ping");
	//	ReportValue responseReportValue = ReportHelper.getReportValue(orderList, "responsetime");
		String pingpath = new ReportExport().makeJfreeChartData(pingReportValue.getListValue(), pingReportValue.getIpList(), "wwn��ȫ", "", "");
//		String responsetimepath = new ReportExport().makeJfreeChartData(responseReportValue.getListValue(), responseReportValue.getIpList(), "��Ӧʱ��", "ʱ��", "");
        System.out.println(pingReportValue.getListValue().size());
//		request.setAttribute("responsetimeDivStr", responsetimeDivStr);
		request.setAttribute("pingChartDivStr", pingChartDivStr);
		request.setAttribute("starttime", starttime);
		request.setAttribute("totime", totime);
		session.setAttribute("pingpath", pingpath);
//		session.setAttribute("responsetimepath", responsetimepath);
		session.setAttribute("orderList", orderList);
		request.setAttribute("startdate", startdate);
		request.setAttribute("todate", todate);
//		request.setAttribute("pinglist", list);
//		request.setAttribute("id", ids);
		return "/capreport/net/hdsSlunSafety.jsp";
	}
	
	private String storageSlunCon() {
		Date d = new Date();
		String startdate = getParaValue("startdate");
//		System.out.println(startdate+"*****************************************");
		if (startdate == null) {
			startdate = sdf0.format(d);
		}
		String todate = getParaValue("todate");
//		System.out.println("*****************************************"+todate);
		if (todate == null) {
			todate = sdf0.format(d);
		}
		String starttime = startdate + " 00:00:00";
		String totime = todate + " 23:59:59";
		String ids = getParaValue("id");
		String flag = (String) session.getAttribute("ids");
		if(flag != null && ids == null){
			ids = flag;
			request.getSession().removeAttribute("ids");
		}
		// �������־ȡ���˿����¼�¼���б�
		String orderflag = "ipaddress";
		if (getParaValue("orderflag") != null && !getParaValue("orderflag").equals("")) {
			orderflag = getParaValue("orderflag");
		}
 
		List orderList = new ArrayList();
		if (ids != null) {
			    session.setAttribute("ids", ids);
				HostNodeDao dao = new HostNodeDao();
				HostNode node = dao.loadHost(Integer.parseInt(ids));
                String ip =node.getIpAddress();
                if(node.getIpAddress().contains(".")){
                	ip=node.getIpAddress().replace(".", "_");
                }
				Hashtable pinghash = new Hashtable();
				try {
					pinghash = this.getCategory(ip, "rsluncon",  starttime, totime);
				} catch (Exception e) {
					e.printStackTrace();
				}
				Hashtable responsehash = new Hashtable();
				Hashtable ipmemhash = new Hashtable();
				ipmemhash.put("hostnode", node);
				ipmemhash.put("pinghash", pinghash);
				ipmemhash.put("ipaddress", node.getIpAddress() + "(" + node.getAlias() + ")");
				orderList.add(ipmemhash);
		}
		String pingChartDivStr = ReportHelper.getChartDivStr(orderList, "ping");
//		System.out.println("--------amchart---------"+pingChartDivStr);
//		String responsetimeDivStr = ReportHelper.getChartDivStr(orderList, "responsetime");
		// ��ͨ��
		ReportValue pingReportValue = ReportHelper.getReportValue(orderList, "ping");
	//	ReportValue responseReportValue = ReportHelper.getReportValue(orderList, "responsetime");
		String pingpath = new ReportExport().makeJfreeChartData(pingReportValue.getListValue(), pingReportValue.getIpList(), "slun������״̬", "", "");
//		String responsetimepath = new ReportExport().makeJfreeChartData(responseReportValue.getListValue(), responseReportValue.getIpList(), "��Ӧʱ��", "ʱ��", "");
        System.out.println(pingReportValue.getListValue().size());
//		request.setAttribute("responsetimeDivStr", responsetimeDivStr);
		request.setAttribute("pingChartDivStr", pingChartDivStr);
		request.setAttribute("starttime", starttime);
		request.setAttribute("totime", totime);
		session.setAttribute("pingpath", pingpath);
//		session.setAttribute("responsetimepath", responsetimepath);
		session.setAttribute("orderList", orderList);
		request.setAttribute("startdate", startdate);
		request.setAttribute("todate", todate);
//		request.setAttribute("pinglist", list);
//		request.setAttribute("id", ids);
		return "/capreport/net/hdsSlunCon.jsp";
	}
	
	
	private String vmwarecpu() {
		Date d = new Date();
		String startdate = getParaValue("startdate");
		if (startdate == null) {
			startdate = sdf0.format(d);
		}
		String todate = getParaValue("todate");
		if (todate == null) {
			todate = sdf0.format(d);
		}
		String maxvalue="";
		String avgvalue = "";
		String starttime = startdate + " 00:00:00";
		String totime = todate + " 23:59:59";
		String ipaddress = getParaValue("ip");
		String vid = getParaValue("vid");
		String nowvalue = request.getParameter("cpunow");
		String flag = (String) session.getAttribute("ipaddress");
		String flag_vid = (String) session.getAttribute("vid");
		if(flag != null && ipaddress == null){
			ipaddress = flag;
			request.getSession().removeAttribute("ipaddress");
		}
		if(flag_vid != null && vid == null){
			vid = flag_vid;
			request.getSession().removeAttribute("vid");
		}
		// �������־ȡ���˿����¼�¼���б�
		String orderflag = "ipaddress";
		if (getParaValue("orderflag") != null && !getParaValue("orderflag").equals("")) {
			orderflag = getParaValue("orderflag");
		}
 
		List orderList = new ArrayList();
		if(ipaddress != null && vid != null){
			    session.setAttribute("ipaddress", ipaddress);
			    session.setAttribute("vid", vid);
				Hashtable pinghash = new Hashtable();
				try {
					pinghash = this.getVMCategory(ipaddress, "cpu",vid,  starttime, totime);
					maxvalue = this.findMaxValue(ipaddress, "cpu", vid, starttime, totime);
					avgvalue = this.findAvgValue(ipaddress, "cpu", vid, starttime, totime);
				} catch (Exception e) {
					e.printStackTrace();
				}
				Hashtable ipmemhash = new Hashtable();
				ipmemhash.put("pinghash", pinghash);
				ipmemhash.put("ipaddress",ipaddress);
				orderList.add(ipmemhash);
		}
		String pingChartDivStr = ReportHelper.getChartDivStr(orderList, "ping");
		ReportValue pingReportValue = ReportHelper.getReportValue(orderList, "ping");
		String pingpath = new ReportExport().makeJfreeChartData(pingReportValue.getListValue(), pingReportValue.getIpList(), "VMWare CPU������", "", "");
        System.out.println(pingReportValue.getListValue().size());
		request.setAttribute("pingChartDivStr", pingChartDivStr);
		request.setAttribute("starttime", starttime);
		request.setAttribute("totime", totime);
		session.setAttribute("pingpath", pingpath);
		session.setAttribute("orderList", orderList);
		request.setAttribute("startdate", startdate);
		request.setAttribute("todate", todate);
		request.setAttribute("vid", vid);
		request.setAttribute("ip", ipaddress.replace("_", "."));
		request.setAttribute("maxvalue", maxvalue);
		request.setAttribute("avgvalue", avgvalue);
		request.setAttribute("nowvalue", nowvalue);
		return "/capreport/net/vmwarecpu.jsp";
	}
	
	
	public Hashtable getVMCategory(String ip, String category,String vid,  String starttime, String endtime) throws Exception {
		Hashtable hash = new Hashtable();
		Connection con = null;
		PreparedStatement stmt = null;
		DBManager dbmanager = new DBManager();
		ResultSet rs = null;
		String sid = ip;
		try {
				String sql = "";
				StringBuffer sb = new StringBuffer();
//				if (category.equals("Ping")) {
					//
					if ("mysql".equalsIgnoreCase(SystemConstant.DBType)) {
						sb.append(" select h."+category+",DATE_FORMAT(h.collecttime,'%Y-%m-%d %H:%i:%s') as collecttime  from vm_guesthost" + sid + " h where ");
					} else if ("oracle".equalsIgnoreCase(SystemConstant.DBType)) {
						sb.append(" select h."+category+",to_char(h.collecttime,'YYYY-MM-DD HH24:MI:SS') as collecttime ,h.unit from vm_guesthost" + sid + " h where ");
					}

//				} 
				sb.append(" h.vid='");
				sb.append(vid);
				if ("mysql".equalsIgnoreCase(SystemConstant.DBType)) {
					sb.append("' and h.collecttime >= '");
					sb.append(starttime);
					sb.append("' and h.collecttime <= '");
					sb.append(endtime);
					sb.append("' order by h.collecttime asc");
				} else if ("oracle".equalsIgnoreCase(SystemConstant.DBType)) {
					sb.append("' and h.collecttime >= to_date('");
					sb.append(starttime);
					sb.append("','YYYY-MM-DD HH24:MI:SS') and h.collecttime <= to_date('");
					sb.append(endtime);
					sb.append("','YYYY-MM-DD HH24:MI:SS')");
					sb.append(" order by h.collecttime asc");
				}

				sql = sb.toString();
				
//				System.out.println("--------HostCollectDataManager-------->"+sql);
				
				rs = dbmanager.executeQuery(sql);
				List list1 = new ArrayList();
				String unit = "";
				String max = "";
				double tempfloat = 0;
				// HONGLI ADD START1
				double pingMaxFloat = 0;
				// HONGLI ADD END1
				double pingcon = 0;
				double cpucon = 0;
				double memory = 0;
				int downnum = 0;
				int i = 0;
				if (rs != null) {
					while (rs.next()) {
						i = i + 1;
						Vector v = new Vector();
						String thevalue = rs.getString(category);
						String collecttime = rs.getString("collecttime");
						v.add(0, emitStr(thevalue));
						v.add(1, collecttime);
						list1.add(v);
					}
					rs.close();
				}
				// stmt.close();

				Integer size = new Integer(0);
				hash.put("list", list1);
				if (list1.size() != 0) {
					size = new Integer(list1.size());
					if (list1.get(0) != null) {
						Vector tempV = (Vector) list1.get(0);
//						unit = (String) tempV.get(2);
					}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null)
				rs.close();
			dbmanager.close();
		}

		return hash;
	}
	
	public String findMaxValue(String ip, String category,String vid,  String starttime, String endtime) throws Exception {
		String result = "";
		Hashtable hash = new Hashtable();
		Connection con = null;
		PreparedStatement stmt = null;
		DBManager dbmanager = new DBManager();
		ResultSet rs = null;
		String sid = ip;
		try {
				String sql = "";
				StringBuffer sb = new StringBuffer();
//				if (category.equals("Ping")) {
					//
					if ("mysql".equalsIgnoreCase(SystemConstant.DBType)) {
						sb.append(" select max(h."+category+") as "+category+" from vm_guesthost" + sid + " h where ");
					} else if ("oracle".equalsIgnoreCase(SystemConstant.DBType)) {
						sb.append(" select max(h."+category+") as "+category+" from vm_guesthost" + sid + " h where ");
					}

//				} 
				sb.append(" h.vid='");
				sb.append(vid);
				if ("mysql".equalsIgnoreCase(SystemConstant.DBType)) {
					sb.append("' and h.collecttime >= '");
					sb.append(starttime);
					sb.append("' and h.collecttime <= '");
					sb.append(endtime);
					sb.append("'");
				} else if ("oracle".equalsIgnoreCase(SystemConstant.DBType)) {
					sb.append("' and h.collecttime >= to_date('");
					sb.append(starttime);
					sb.append("','YYYY-MM-DD HH24:MI:SS') and h.collecttime <= to_date('");
					sb.append(endtime);
					sb.append("','YYYY-MM-DD HH24:MI:SS')");
				}

				sql = sb.toString();
				
//				System.out.println("--------HostCollectDataManager----max---->"+sql);
				
				rs = dbmanager.executeQuery(sql);
				List list1 = new ArrayList();
				if (rs != null) {
					while (rs.next()) {
						result = rs.getString(category);
					}
					rs.close();
				}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null)
				rs.close();
			dbmanager.close();
		}

		return result;
	}
	
	
	public String findAvgValue(String ip, String category,String vid,  String starttime, String endtime) throws Exception {
		String result = "";
		Hashtable hash = new Hashtable();
		Connection con = null;
		PreparedStatement stmt = null;
		DBManager dbmanager = new DBManager();
		ResultSet rs = null;
		String sid = ip;
		try {
				String sql = "";
				StringBuffer sb = new StringBuffer();
//				if (category.equals("Ping")) {
					//
					if ("mysql".equalsIgnoreCase(SystemConstant.DBType)) {
						sb.append(" select round(avg(h."+category+"),2) as "+category+" from vm_guesthost" + sid + " h where ");
					} else if ("oracle".equalsIgnoreCase(SystemConstant.DBType)) {
						sb.append(" select avg(h."+category+") as "+category+" from vm_guesthost" + sid + " h where ");
					}

//				} 
				sb.append(" h.vid='");
				sb.append(vid);
				if ("mysql".equalsIgnoreCase(SystemConstant.DBType)) {
					sb.append("' and h.collecttime >= '");
					sb.append(starttime);
					sb.append("' and h.collecttime <= '");
					sb.append(endtime);
					sb.append("'");
				} else if ("oracle".equalsIgnoreCase(SystemConstant.DBType)) {
					sb.append("' and h.collecttime >= to_date('");
					sb.append(starttime);
					sb.append("','YYYY-MM-DD HH24:MI:SS') and h.collecttime <= to_date('");
					sb.append(endtime);
					sb.append("','YYYY-MM-DD HH24:MI:SS')");
				}

				sql = sb.toString();
				
				System.out.println("--------HostCollectDataManager----avg---->"+sql);
				
				rs = dbmanager.executeQuery(sql);
				List list1 = new ArrayList();
				if (rs != null) {
					while (rs.next()) {
						result = rs.getString(category);
					}
					rs.close();
				}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null)
				rs.close();
			dbmanager.close();
		}

		return result;
	}
	
	
	private String vmwarecpuuse() {
		Date d = new Date();
		String startdate = getParaValue("startdate");
		if (startdate == null) {
			startdate = sdf0.format(d);
		}
		String todate = getParaValue("todate");
		if (todate == null) {
			todate = sdf0.format(d);
		}
		String maxvalue="";
		String avgvalue = "";
		String starttime = startdate + " 00:00:00";
		String totime = todate + " 23:59:59";
		String ipaddress = getParaValue("ip");
		String vid = getParaValue("vid");
		String flag = (String) session.getAttribute("ipaddress");
		String flag_vid = (String) session.getAttribute("vid");
		if(flag != null && ipaddress == null){
			ipaddress = flag;
			request.getSession().removeAttribute("ipaddress");
		}
		if(flag_vid != null && vid == null){
			vid = flag_vid;
			request.getSession().removeAttribute("vid");
		}
		// �������־ȡ���˿����¼�¼���б�
		String orderflag = "ipaddress";
		if (getParaValue("orderflag") != null && !getParaValue("orderflag").equals("")) {
			orderflag = getParaValue("orderflag");
		}
 
		List orderList = new ArrayList();
		if(ipaddress != null && vid != null){
			    session.setAttribute("ipaddress", ipaddress);
			    session.setAttribute("vid", vid);
				Hashtable pinghash = new Hashtable();
				try {
					pinghash = this.getVMCategory(ipaddress, "cpuuse",vid,  starttime, totime);
					maxvalue = this.findMaxValue(ipaddress, "cpuuse", vid, starttime, totime);
					avgvalue = this.findAvgValue(ipaddress, "cpuuse", vid, starttime, totime);
				} catch (Exception e) {
					e.printStackTrace();
				}
				Hashtable ipmemhash = new Hashtable();
				ipmemhash.put("pinghash", pinghash);
				ipmemhash.put("ipaddress",ipaddress);
				orderList.add(ipmemhash);
		}
		String pingChartDivStr = ReportHelper.getChartDivStr(orderList, "ping");
		ReportValue pingReportValue = ReportHelper.getReportValue(orderList, "ping");
		String pingpath = new ReportExport().makeJfreeChartData(pingReportValue.getListValue(), pingReportValue.getIpList(), "VMWare CPUʹ�����", "", "");
        System.out.println(pingReportValue.getListValue().size());
		request.setAttribute("pingChartDivStr", pingChartDivStr);
		request.setAttribute("starttime", starttime);
		request.setAttribute("totime", totime);
		session.setAttribute("pingpath", pingpath);
		session.setAttribute("orderList", orderList);
		request.setAttribute("startdate", startdate);
		request.setAttribute("todate", todate);
		request.setAttribute("vid", vid);
		request.setAttribute("ip", ipaddress.replace("_", "."));
		request.setAttribute("maxvalue", maxvalue);
		request.setAttribute("avgvalue", avgvalue);
		return "/capreport/net/vmwarecpuuse.jsp";
	}
	
	
	private String vmwarememout() {
		Date d = new Date();
		String startdate = getParaValue("startdate");
		if (startdate == null) {
			startdate = sdf0.format(d);
		}
		String todate = getParaValue("todate");
		if (todate == null) {
			todate = sdf0.format(d);
		}
		String maxvalue="";
		String avgvalue = "";
		String starttime = startdate + " 00:00:00";
		String totime = todate + " 23:59:59";
		String ipaddress = getParaValue("ip");
		String vid = getParaValue("vid");
		String nowvalue = request.getParameter("cpunow");
		String flag = (String) session.getAttribute("ipaddress");
		String flag_vid = (String) session.getAttribute("vid");
		if(flag != null && ipaddress == null){
			ipaddress = flag;
			request.getSession().removeAttribute("ipaddress");
		}
		if(flag_vid != null && vid == null){
			vid = flag_vid;
			request.getSession().removeAttribute("vid");
		}
		// �������־ȡ���˿����¼�¼���б�
		String orderflag = "ipaddress";
		if (getParaValue("orderflag") != null && !getParaValue("orderflag").equals("")) {
			orderflag = getParaValue("orderflag");
		}
 
		List orderList = new ArrayList();
		if(ipaddress != null && vid != null){
			    session.setAttribute("ipaddress", ipaddress);
			    session.setAttribute("vid", vid);
				Hashtable pinghash = new Hashtable();
				try {
					pinghash = this.getVMCategory(ipaddress, "memout",vid,  starttime, totime);
					maxvalue = this.findMaxValue(ipaddress, "memout", vid, starttime, totime);
					avgvalue = this.findAvgValue(ipaddress, "memout", vid, starttime, totime);
				} catch (Exception e) {
					e.printStackTrace();
				}
				Hashtable ipmemhash = new Hashtable();
				ipmemhash.put("pinghash", pinghash);
				ipmemhash.put("ipaddress",ipaddress);
				orderList.add(ipmemhash);
		}
		String pingChartDivStr = ReportHelper.getChartDivStr(orderList, "ping");
		ReportValue pingReportValue = ReportHelper.getReportValue(orderList, "ping");
		String pingpath = new ReportExport().makeJfreeChartData(pingReportValue.getListValue(), pingReportValue.getIpList(), "VMWare �ڴ滻������", "", "");
        System.out.println(pingReportValue.getListValue().size());
		request.setAttribute("pingChartDivStr", pingChartDivStr);
		request.setAttribute("starttime", starttime);
		request.setAttribute("totime", totime);
		session.setAttribute("pingpath", pingpath);
		session.setAttribute("orderList", orderList);
		request.setAttribute("startdate", startdate);
		request.setAttribute("todate", todate);
		request.setAttribute("vid", vid);
		request.setAttribute("ip", ipaddress.replace("_", "."));
		request.setAttribute("maxvalue", maxvalue);
		request.setAttribute("avgvalue", avgvalue);
		request.setAttribute("nowvalue", nowvalue);
		return "/capreport/net/vmwarememout.jsp";
	}
	
	private String vmwarememin() {
		Date d = new Date();
		String startdate = getParaValue("startdate");
		if (startdate == null) {
			startdate = sdf0.format(d);
		}
		String todate = getParaValue("todate");
		if (todate == null) {
			todate = sdf0.format(d);
		}
		String maxvalue="";
		String avgvalue = "";
		String starttime = startdate + " 00:00:00";
		String totime = todate + " 23:59:59";
		String ipaddress = getParaValue("ip");
		String vid = getParaValue("vid");
		String nowvalue = request.getParameter("cpunow");
		String flag = (String) session.getAttribute("ipaddress");
		String flag_vid = (String) session.getAttribute("vid");
		if(flag != null && ipaddress == null){
			ipaddress = flag;
			request.getSession().removeAttribute("ipaddress");
		}
		if(flag_vid != null && vid == null){
			vid = flag_vid;
			request.getSession().removeAttribute("vid");
		}
		// �������־ȡ���˿����¼�¼���б�
		String orderflag = "ipaddress";
		if (getParaValue("orderflag") != null && !getParaValue("orderflag").equals("")) {
			orderflag = getParaValue("orderflag");
		}
 
		List orderList = new ArrayList();
		if(ipaddress != null && vid != null){
			    session.setAttribute("ipaddress", ipaddress);
			    session.setAttribute("vid", vid);
				Hashtable pinghash = new Hashtable();
				try {
					pinghash = this.getVMCategory(ipaddress, "memin",vid,  starttime, totime);
					maxvalue = this.findMaxValue(ipaddress, "memin", vid, starttime, totime);
					avgvalue = this.findAvgValue(ipaddress, "memin", vid, starttime, totime);
				} catch (Exception e) {
					e.printStackTrace();
				}
				Hashtable ipmemhash = new Hashtable();
				ipmemhash.put("pinghash", pinghash);
				ipmemhash.put("ipaddress",ipaddress);
				orderList.add(ipmemhash);
		}
		String pingChartDivStr = ReportHelper.getChartDivStr(orderList, "ping");
		ReportValue pingReportValue = ReportHelper.getReportValue(orderList, "ping");
		String pingpath = new ReportExport().makeJfreeChartData(pingReportValue.getListValue(), pingReportValue.getIpList(), "VMWare �ڴ滻������", "", "");
        System.out.println(pingReportValue.getListValue().size());
		request.setAttribute("pingChartDivStr", pingChartDivStr);
		request.setAttribute("starttime", starttime);
		request.setAttribute("totime", totime);
		session.setAttribute("pingpath", pingpath);
		session.setAttribute("orderList", orderList);
		request.setAttribute("startdate", startdate);
		request.setAttribute("todate", todate);
		request.setAttribute("vid", vid);
		request.setAttribute("ip", ipaddress.replace("_", "."));
		request.setAttribute("maxvalue", maxvalue);
		request.setAttribute("avgvalue", avgvalue);
		request.setAttribute("nowvalue", nowvalue);
		return "/capreport/net/vmwarememin.jsp";
	}
	
	
	private String vmwaremem() {
		Date d = new Date();
		String startdate = getParaValue("startdate");
		if (startdate == null) {
			startdate = sdf0.format(d);
		}
		String todate = getParaValue("todate");
		if (todate == null) {
			todate = sdf0.format(d);
		}
		String maxvalue="";
		String avgvalue = "";
		String starttime = startdate + " 00:00:00";
		String totime = todate + " 23:59:59";
		String ipaddress = getParaValue("ip");
		String vid = getParaValue("vid");
		String nowvalue = request.getParameter("cpunow");
		String flag = (String) session.getAttribute("ipaddress");
		String flag_vid = (String) session.getAttribute("vid");
		if(flag != null && ipaddress == null){
			ipaddress = flag;
			request.getSession().removeAttribute("ipaddress");
		}
		if(flag_vid != null && vid == null){
			vid = flag_vid;
			request.getSession().removeAttribute("vid");
		}
		// �������־ȡ���˿����¼�¼���б�
		String orderflag = "ipaddress";
		if (getParaValue("orderflag") != null && !getParaValue("orderflag").equals("")) {
			orderflag = getParaValue("orderflag");
		}
 
		List orderList = new ArrayList();
		if(ipaddress != null && vid != null){
			    session.setAttribute("ipaddress", ipaddress);
			    session.setAttribute("vid", vid);
				Hashtable pinghash = new Hashtable();
				try {
					pinghash = this.getVMCategory(ipaddress, "mem",vid,  starttime, totime);
					maxvalue = this.findMaxValue(ipaddress, "mem", vid, starttime, totime);
					avgvalue = this.findAvgValue(ipaddress, "mem", vid, starttime, totime);
				} catch (Exception e) {
					e.printStackTrace();
				}
				Hashtable ipmemhash = new Hashtable();
				ipmemhash.put("pinghash", pinghash);
				ipmemhash.put("ipaddress",ipaddress);
				orderList.add(ipmemhash);
		}
		String pingChartDivStr = ReportHelper.getChartDivStr(orderList, "ping");
		ReportValue pingReportValue = ReportHelper.getReportValue(orderList, "ping");
		String pingpath = new ReportExport().makeJfreeChartData(pingReportValue.getListValue(), pingReportValue.getIpList(), "VMWare �ڴ�ʹ�����", "", "");
        System.out.println(pingReportValue.getListValue().size());
		request.setAttribute("pingChartDivStr", pingChartDivStr);
		request.setAttribute("starttime", starttime);
		request.setAttribute("totime", totime);
		session.setAttribute("pingpath", pingpath);
		session.setAttribute("orderList", orderList);
		request.setAttribute("startdate", startdate);
		request.setAttribute("todate", todate);
		request.setAttribute("vid", vid);
		request.setAttribute("ip", ipaddress.replace("_", "."));
		request.setAttribute("maxvalue", maxvalue);
		request.setAttribute("avgvalue", avgvalue);
		request.setAttribute("nowvalue", nowvalue);
		return "/capreport/net/vmwaremem.jsp";
	}
	
	
	private String vmwaredisk() {
		Date d = new Date();
		String startdate = getParaValue("startdate");
		if (startdate == null) {
			startdate = sdf0.format(d);
		}
		String todate = getParaValue("todate");
		if (todate == null) {
			todate = sdf0.format(d);
		}
		String maxvalue="";
		String avgvalue = "";
		String starttime = startdate + " 00:00:00";
		String totime = todate + " 23:59:59";
		String ipaddress = getParaValue("ip");
		String vid = getParaValue("vid");
		String nowvalue = request.getParameter("cpunow");
		String flag = (String) session.getAttribute("ipaddress");
		String flag_vid = (String) session.getAttribute("vid");
		if(flag != null && ipaddress == null){
			ipaddress = flag;
			request.getSession().removeAttribute("ipaddress");
		}
		if(flag_vid != null && vid == null){
			vid = flag_vid;
			request.getSession().removeAttribute("vid");
		}
		// �������־ȡ���˿����¼�¼���б�
		String orderflag = "ipaddress";
		if (getParaValue("orderflag") != null && !getParaValue("orderflag").equals("")) {
			orderflag = getParaValue("orderflag");
		}
 
		List orderList = new ArrayList();
		if(ipaddress != null && vid != null){
			    session.setAttribute("ipaddress", ipaddress);
			    session.setAttribute("vid", vid);
				Hashtable pinghash = new Hashtable();
				try {
					pinghash = this.getVMCategory(ipaddress, "disk",vid,  starttime, totime);
					maxvalue = this.findMaxValue(ipaddress, "disk", vid, starttime, totime);
					avgvalue = this.findAvgValue(ipaddress, "disk", vid, starttime, totime);
				} catch (Exception e) {
					e.printStackTrace();
				}
				Hashtable ipmemhash = new Hashtable();
				ipmemhash.put("pinghash", pinghash);
				ipmemhash.put("ipaddress",ipaddress);
				orderList.add(ipmemhash);
		}
		String pingChartDivStr = ReportHelper.getChartDivStr(orderList, "ping");
		ReportValue pingReportValue = ReportHelper.getReportValue(orderList, "ping");
		String pingpath = new ReportExport().makeJfreeChartData(pingReportValue.getListValue(), pingReportValue.getIpList(), "VMWare ����ʹ�����", "", "");
        System.out.println(pingReportValue.getListValue().size());
		request.setAttribute("pingChartDivStr", pingChartDivStr);
		request.setAttribute("starttime", starttime);
		request.setAttribute("totime", totime);
		session.setAttribute("pingpath", pingpath);
		session.setAttribute("orderList", orderList);
		request.setAttribute("startdate", startdate);
		request.setAttribute("todate", todate);
		request.setAttribute("vid", vid);
		request.setAttribute("ip", ipaddress.replace("_", "."));
		request.setAttribute("maxvalue", maxvalue);
		request.setAttribute("avgvalue", avgvalue);
		request.setAttribute("nowvalue", nowvalue);
		return "/capreport/net/vmwaredisk.jsp";
	}
	
	
	private String vmwarenet() {
		Date d = new Date();
		String startdate = getParaValue("startdate");
		if (startdate == null) {
			startdate = sdf0.format(d);
		}
		String todate = getParaValue("todate");
		if (todate == null) {
			todate = sdf0.format(d);
		}
		String maxvalue="";
		String avgvalue = "";
		String starttime = startdate + " 00:00:00";
		String totime = todate + " 23:59:59";
		String ipaddress = getParaValue("ip");
		String vid = getParaValue("vid");
		String nowvalue = request.getParameter("cpunow");
		String flag = (String) session.getAttribute("ipaddress");
		String flag_vid = (String) session.getAttribute("vid");
		if(flag != null && ipaddress == null){
			ipaddress = flag;
			request.getSession().removeAttribute("ipaddress");
		}
		if(flag_vid != null && vid == null){
			vid = flag_vid;
			request.getSession().removeAttribute("vid");
		}
		// �������־ȡ���˿����¼�¼���б�
		String orderflag = "ipaddress";
		if (getParaValue("orderflag") != null && !getParaValue("orderflag").equals("")) {
			orderflag = getParaValue("orderflag");
		}
 
		List orderList = new ArrayList();
		if(ipaddress != null && vid != null){
			    session.setAttribute("ipaddress", ipaddress);
			    session.setAttribute("vid", vid);
				Hashtable pinghash = new Hashtable();
				try {
					pinghash = this.getVMCategory(ipaddress, "net",vid,  starttime, totime);
					maxvalue = this.findMaxValue(ipaddress, "net", vid, starttime, totime);
					avgvalue = this.findAvgValue(ipaddress, "net", vid, starttime, totime);
				} catch (Exception e) {
					e.printStackTrace();
				}
				Hashtable ipmemhash = new Hashtable();
				ipmemhash.put("pinghash", pinghash);
				ipmemhash.put("ipaddress",ipaddress);
				orderList.add(ipmemhash);
		}
		String pingChartDivStr = ReportHelper.getChartDivStr(orderList, "ping");
		ReportValue pingReportValue = ReportHelper.getReportValue(orderList, "ping");
		String pingpath = new ReportExport().makeJfreeChartData(pingReportValue.getListValue(), pingReportValue.getIpList(), "VMWare ����ʹ��", "", "");
        System.out.println(pingReportValue.getListValue().size());
		request.setAttribute("pingChartDivStr", pingChartDivStr);
		request.setAttribute("starttime", starttime);
		request.setAttribute("totime", totime);
		session.setAttribute("pingpath", pingpath);
		session.setAttribute("orderList", orderList);
		request.setAttribute("startdate", startdate);
		request.setAttribute("todate", todate);
		request.setAttribute("vid", vid);
		request.setAttribute("ip", ipaddress.replace("_", "."));
		request.setAttribute("maxvalue", maxvalue);
		request.setAttribute("avgvalue", avgvalue);
		request.setAttribute("nowvalue", nowvalue);
		return "/capreport/net/vmwarenet.jsp";
	}
	
	
	public String createdocVM() {
		String file = "";// ���浽��Ŀ�ļ����µ�ָ���ļ���
		String flag = request.getParameter("flag");
		String nowvalue = request.getParameter("nowvalue");
		String avgvalue = request.getParameter("avgvalue");
		String maxvalue = request.getParameter("maxvalue");
		if(flag.equalsIgnoreCase("cpu")){
			file = "/temp/cpu.doc";
		}else if(flag.equalsIgnoreCase("cpuuse")){
			file = "/temp/cpuuse.doc";
		}else if(flag.equalsIgnoreCase("mem")){
			file = "/temp/mem.doc";
		}else if(flag.equalsIgnoreCase("memin")){
			file = "/temp/memin.doc";
		}else if(flag.equalsIgnoreCase("memout")){
			file = "/temp/memout.doc";
		}else if(flag.equalsIgnoreCase("disk")){
			file = "/temp/disk.doc";
		}else if(flag.equalsIgnoreCase("net")){
			file = "/temp/net.doc";
		}else if(flag.equalsIgnoreCase("cpuCR")){
			file = "/temp/cpuCR.doc";
		}else if(flag.equalsIgnoreCase("cputotalCR")){
			file = "/temp/cputotalCR.doc";
		}else if(flag.equalsIgnoreCase("memCR")){
			file = "/temp/memCR.doc";
		}else if(flag.equalsIgnoreCase("memtotalCR")){
			file = "/temp/memtotalCR.doc";
		}else if(flag.equalsIgnoreCase("cpuRP")){
			file = "/temp/resourcepoolCPU.doc";
		}
		String fileName = ResourceCenter.getInstance().getSysPath() + file;// ��ȡϵͳ�ļ���·��
		try {
			createDocContextVM(fileName,flag,nowvalue,avgvalue,maxvalue);
//			request.getSession().removeAttribute("pingpath");
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		request.setAttribute("filename", fileName);
		return "/capreport/net/download.jsp";
	}
	
	public void createDocContextVM(String file,String flag,String nowvalue,String avgvalue,String maxvalue) throws DocumentException, IOException {
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
		
		Paragraph title = null;
		if(flag.equalsIgnoreCase("cpu")){
			title = new Paragraph("cpu�����ʱ���", titleFont);
		}else if(flag.equalsIgnoreCase("cpuuse")){
			title = new Paragraph("cpuʹ���������", titleFont);
		}else if(flag.equalsIgnoreCase("mem")){
			title = new Paragraph("�ڴ�ʹ���������", titleFont);
		}else if(flag.equalsIgnoreCase("memin")){
			title = new Paragraph("�ڴ滻�����ʱ���", titleFont);
		}else if(flag.equalsIgnoreCase("memout")){
			title = new Paragraph("�ڴ滻�����ʱ���", titleFont);
		}else if(flag.equalsIgnoreCase("disk")){
			title = new Paragraph("����ʹ���������", titleFont);
		}else if(flag.equalsIgnoreCase("net")){
			title = new Paragraph("����ʹ���������", titleFont);
		}else if(flag.equalsIgnoreCase("cpuCR")){
			title = new Paragraph("��Ⱥcpuʹ���������", titleFont);
		}else if(flag.equalsIgnoreCase("cputotalCR")){
			title = new Paragraph("��Ⱥcpu�ܼƱ���", titleFont);
		}else if(flag.equalsIgnoreCase("memCR")){
			title = new Paragraph("��Ⱥ�ڴ�ʹ���������", titleFont);
		}else if(flag.equalsIgnoreCase("memtotalCR")){
			title = new Paragraph("��Ⱥ�ڴ��ܼƱ���", titleFont);
		}else if(flag.equalsIgnoreCase("cpuRP")){
			title = new Paragraph("��Դ��cpuʹ���������", titleFont);
		}
		// ���ñ����ʽ���뷽ʽ
		title.setAlignment(Element.ALIGN_CENTER);
		// title.setFont(titleFont);
		document.add(title);
		// ���� Table ���
		Font fontChinese = new Font(bfChinese, 12, Font.NORMAL, Color.black);
//		List pinglist = (List) session.getAttribute("pinglist");
		Table aTable = new Table(4);
		int width[] = { 50, 50, 50, 50,  };
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

		String pingpath = (String) session.getAttribute("pingpath");
		Image img = Image.getInstance(pingpath);
		img.setAbsolutePosition(0, 0);
		img.setAlignment(Image.LEFT);// ����ͼƬ��ʾλ��
		document.add(img);
		
		Cell cell1 = new Cell("ָ������");
		Cell cell2 = new Cell("��ǰ");
		Cell cell3 = new Cell("ƽ��");
		Cell cell4 = new Cell("���");
		Cell cell5 = null;
		if(flag.equalsIgnoreCase("cpu")){
			cell5 = new Cell("cpu������");
		}else if(flag.equalsIgnoreCase("cpuuse")){
			cell5 = new Cell("cpuʹ�����");
		}else if(flag.equalsIgnoreCase("mem")){
			cell5 = new Cell("�ڴ�ʹ�����");
		}else if(flag.equalsIgnoreCase("memin")){
			cell5 = new Cell("�ڴ滻������");
		}else if(flag.equalsIgnoreCase("memout")){
			cell5 = new Cell("�ڴ滻������");
		}else if(flag.equalsIgnoreCase("disk")){
			cell5 = new Cell("����ʹ�����");
		}else if(flag.equalsIgnoreCase("net")){
			cell5 = new Cell("����ʹ�����");
		}else if(flag.equalsIgnoreCase("cpuCR")){
			cell5 = new Cell("cpuʹ�����");
		}else if(flag.equalsIgnoreCase("cputotalCR")){
			cell5 = new Cell("cpu�ܼ�");
		}else if(flag.equalsIgnoreCase("memCR")){
			cell5 = new Cell("�ڴ�ʹ�����");
		}else if(flag.equalsIgnoreCase("memtotalCR")){
			cell5 = new Cell("�ڴ��ܼ�");
		}else if(flag.equalsIgnoreCase("cpuRP")){
			cell5 = new Cell("cpuʹ�����");
		}
		Cell cell6 = new Cell(nowvalue);
		Cell cell7 = new Cell(avgvalue);
		Cell cell8 = new Cell(maxvalue);
		cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell4.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell5.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell6.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell7.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell8.setVerticalAlignment(Element.ALIGN_MIDDLE);

		cell1.setBackgroundColor(Color.LIGHT_GRAY);
		cell2.setBackgroundColor(Color.LIGHT_GRAY);
		cell3.setBackgroundColor(Color.LIGHT_GRAY);
		cell4.setBackgroundColor(Color.LIGHT_GRAY);
		cell5.setBackgroundColor(Color.LIGHT_GRAY);
		cell6.setBackgroundColor(Color.LIGHT_GRAY);
		cell7.setBackgroundColor(Color.LIGHT_GRAY);
		cell8.setBackgroundColor(Color.LIGHT_GRAY);
		
		cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell7.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell8.setHorizontalAlignment(Element.ALIGN_CENTER);
		
		
		aTable.addCell(cell1);
		aTable.addCell(cell2);
		aTable.addCell(cell3);
		aTable.addCell(cell4);
		aTable.addCell(cell5);
		aTable.addCell(cell6);
		aTable.addCell(cell7);
		aTable.addCell(cell8);
		document.add(aTable);
		document.close();
	}
	
	public String createpdfVM() {
		String file = "";// ���浽��Ŀ�ļ����µ�ָ���ļ���
		String flag = request.getParameter("flag");
		String nowvalue = request.getParameter("nowvalue");
		String avgvalue = request.getParameter("avgvalue");
		String maxvalue = request.getParameter("maxvalue");
		if(flag.equalsIgnoreCase("cpu")){
			file = "/temp/cpu.PDF";
		}else if(flag.equalsIgnoreCase("cpuuse")){
			file = "/temp/cpuuse.PDF";
		}else if(flag.equalsIgnoreCase("mem")){
			file = "/temp/mem.PDF";
		}else if(flag.equalsIgnoreCase("memin")){
			file = "/temp/memin.PDF";
		}else if(flag.equalsIgnoreCase("memout")){
			file = "/temp/memout.PDF";
		}else if(flag.equalsIgnoreCase("disk")){
			file = "/temp/disk.PDF";
		}else if(flag.equalsIgnoreCase("net")){
			file = "/temp/net.PDF";
		}else if(flag.equalsIgnoreCase("cpuCR")){
			file = "/temp/cpuCR.PDF";
		}else if(flag.equalsIgnoreCase("cputotalCR")){
			file = "/temp/cputotalCR.PDF";
		}else if(flag.equalsIgnoreCase("memCR")){
			file = "/temp/memCR.PDF";
		}else if(flag.equalsIgnoreCase("memtotalCR")){
			file = "/temp/memtotalCR.PDF";
		}else if(flag.equalsIgnoreCase("cpuRP")){
			file = "/temp/resourcepoolCPU.PDF";
		}
		String fileName = ResourceCenter.getInstance().getSysPath() + file;// ��ȡϵͳ�ļ���·��
		try {
			createPdfVM(fileName,flag,nowvalue,avgvalue,maxvalue);
//			request.getSession().removeAttribute("pingpath");
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		request.setAttribute("filename", fileName);
		return "/capreport/net/download.jsp";
	}
	
	public void createPdfVM(String file,String flag,String nowvalue,String avgvalue,String maxvalue) throws DocumentException, IOException {

		// com.lowagie.text.Font FontChinese = new
		// com.lowagie.text.Font(bfChinese, 12, com.lowagie.text.Font.NORMAL);

		// ����ֽ�Ŵ�С

		Document document = new Document(PageSize.A4);
		// ����һ����д��(Writer)��document���������ͨ����д��(Writer)���Խ��ĵ�д�뵽������
		PdfWriter.getInstance(document, new FileOutputStream(file));
		document.open();
		// ������������
		BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
		// ����������
		Font titleFont = new Font(bfChinese, 12, Font.BOLD);
		// com.lowagie.text.Font titleFont = new
		// com.lowagie.text.Font(bfChinese, 12, com.lowagie.text.Font.NORMAL);
		// ����������
		Font contextFont = new com.lowagie.text.Font(bfChinese, 11, Font.NORMAL);
		Paragraph title = null;
		if(flag.equalsIgnoreCase("cpu")){
			title = new Paragraph("cpu�����ʱ���", titleFont);
		}else if(flag.equalsIgnoreCase("cpuuse")){
			title = new Paragraph("cpuʹ���������", titleFont);
		}else if(flag.equalsIgnoreCase("mem")){
			title = new Paragraph("�ڴ�ʹ���������", titleFont);
		}else if(flag.equalsIgnoreCase("memin")){
			title = new Paragraph("�ڴ滻�����ʱ���", titleFont);
		}else if(flag.equalsIgnoreCase("memout")){
			title = new Paragraph("�ڴ滻�����ʱ���", titleFont);
		}else if(flag.equalsIgnoreCase("disk")){
			title = new Paragraph("����ʹ���������", titleFont);
		}else if(flag.equalsIgnoreCase("net")){
			title = new Paragraph("����ʹ���������", titleFont);
		}else if(flag.equalsIgnoreCase("cpuCR")){
			title = new Paragraph("��Ⱥcpuʹ���������", titleFont);
		}else if(flag.equalsIgnoreCase("cputotalCR")){
			title = new Paragraph("��Ⱥcpu�ܼƱ���", titleFont);
		}else if(flag.equalsIgnoreCase("memCR")){
			title = new Paragraph("��Ⱥ�ڴ�ʹ���������", titleFont);
		}else if(flag.equalsIgnoreCase("memtotalCR")){
			title = new Paragraph("��Ⱥ�ڴ��ܼƱ���", titleFont);
		}else if(flag.equalsIgnoreCase("cpuRP")){
			title = new Paragraph("��Դ��cpuʹ���������", titleFont);
		}
		// ���ñ����ʽ���뷽ʽ
		title.setAlignment(Element.ALIGN_CENTER);
		// title.setFont(titleFont);
		document.add(title);
		document.add(new Paragraph("\n"));
		// ���� Table ���
		Font fontChinese = new Font(bfChinese, 11, Font.NORMAL, Color.black);
		PdfPTable aTable = new PdfPTable(4);

		int width[] = { 50, 50, 50, 50};
		aTable.setWidthPercentage(100);
		aTable.setWidths(width);

//		PdfPCell c = new PdfPCell(new Phrase("", fontChinese));
//		c.setBackgroundColor(Color.LIGHT_GRAY);
//		aTable.addCell(c);
		PdfPCell cell1 = new PdfPCell(new Phrase("ָ������", fontChinese));
		PdfPCell cell2 = new PdfPCell(new Phrase("��ǰ", fontChinese));
		PdfPCell cell3 = new PdfPCell(new Phrase("ƽ��", fontChinese));
		PdfPCell cell4 = new PdfPCell(new Phrase("���", fontChinese));
		
		PdfPCell cell5 = null;
		if(flag.equalsIgnoreCase("cpu")){
			cell5 = new PdfPCell(new Phrase("cpu������", fontChinese));
		}else if(flag.equalsIgnoreCase("cpuuse")){
			cell5 = new PdfPCell(new Phrase("cpuʹ�����", fontChinese));
		}else if(flag.equalsIgnoreCase("mem")){
			cell5 = new PdfPCell(new Phrase("�ڴ�ʹ�����", fontChinese));
		}else if(flag.equalsIgnoreCase("memin")){
			cell5 = new PdfPCell(new Phrase("�ڴ滻������", fontChinese));
		}else if(flag.equalsIgnoreCase("memout")){
			cell5 = new PdfPCell(new Phrase("�ڴ滻������", fontChinese));
		}else if(flag.equalsIgnoreCase("disk")){
			cell5 = new PdfPCell(new Phrase("����ʹ�����", fontChinese));
		}else if(flag.equalsIgnoreCase("net")){
			cell5 = new PdfPCell(new Phrase("����ʹ�����", fontChinese));
		}else if(flag.equalsIgnoreCase("cpuCR")){
			cell5 = new PdfPCell(new Phrase("cpuʹ�����", fontChinese));
		}else if(flag.equalsIgnoreCase("cputotalCR")){
			cell5 = new PdfPCell(new Phrase("cpu�ܼ�", fontChinese));
		}else if(flag.equalsIgnoreCase("memCR")){
			cell5 = new PdfPCell(new Phrase("�ڴ�ʹ�����", fontChinese));
		}else if(flag.equalsIgnoreCase("memtotalCR")){
			cell5 = new PdfPCell(new Phrase("�ڴ��ܼ�", fontChinese));
		}else if(flag.equalsIgnoreCase("cpuRP")){
			cell5 = new PdfPCell(new Phrase("cpuʹ�����", fontChinese));
		}
		PdfPCell cell6 = new PdfPCell(new Phrase(nowvalue, fontChinese));
		PdfPCell cell7 = new PdfPCell(new Phrase(avgvalue, fontChinese));
		PdfPCell cell8 = new PdfPCell(new Phrase(maxvalue, fontChinese));
		
		
		cell1.setBackgroundColor(Color.LIGHT_GRAY);
		cell2.setBackgroundColor(Color.LIGHT_GRAY);
		cell3.setBackgroundColor(Color.LIGHT_GRAY);
		cell4.setBackgroundColor(Color.LIGHT_GRAY);
		cell5.setBackgroundColor(Color.LIGHT_GRAY);
		cell6.setBackgroundColor(Color.LIGHT_GRAY);
		cell7.setBackgroundColor(Color.LIGHT_GRAY);
		cell8.setBackgroundColor(Color.LIGHT_GRAY);
		cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell7.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell8.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell4.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell5.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell6.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell7.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell8.setVerticalAlignment(Element.ALIGN_MIDDLE);
		aTable.addCell(cell1);
		aTable.addCell(cell2);
		aTable.addCell(cell3);
		aTable.addCell(cell4);
		aTable.addCell(cell5);
		aTable.addCell(cell6);
		aTable.addCell(cell7);
		aTable.addCell(cell8);
		// ������ͨ��
		String pingpath = (String) session.getAttribute("pingpath");
		// ������Ӧʱ��ͼƬ
		// document.add(new Paragraph("\n"));
		// String ipaddress = (String)request.getParameter("ipaddress");
		// String newip = doip(ipaddress);
		Image img = Image.getInstance(pingpath);
		img.setAlignment(Image.LEFT);// ����ͼƬ��ʾλ��
		img.scalePercent(75);
		document.add(img);
		document.add(aTable);
		document.close();
	}
	
	
	private String creatXlsVM() {
		Date d = new Date();
		String startdate = getParaValue("startdate");
		String flag = request.getParameter("flag");
		if (startdate == null) {
			startdate = sdf0.format(d);
		}
		String todate = getParaValue("todate");
		if (todate == null) {
			todate = sdf0.format(d);
		}
		String starttime = startdate + " 00:00:00";
		String totime = todate + " 23:59:59";
		Hashtable allcpuhash = new Hashtable();

		List returnList = new ArrayList();
		// I_MonitorIpList monitorManager=new MonitoriplistManager();
//		List memlist = (List) session.getAttribute("pinglist");
		// ������ͨ��
		String pingpath = (String) session.getAttribute("pingpath");
		Hashtable reporthash = new Hashtable();
//		reporthash.put("pinglist", memlist);
		reporthash.put("starttime", starttime);
		reporthash.put("totime", totime);
		reporthash.put("pingpath", pingpath);

		ExcelReport1 report = new ExcelReport1(new IpResourceReport(), reporthash);
		String file = "";
		String nowvalue = request.getParameter("nowvalue");
		String avgvalue = request.getParameter("avgvalue");
		String maxvalue = request.getParameter("maxvalue");
		if(flag.equalsIgnoreCase("cpu")){
			file = "/temp/cpu.xls";
		}else if(flag.equalsIgnoreCase("cpuuse")){
			file = "/temp/cpuuse.xls";
		}else if(flag.equalsIgnoreCase("mem")){
			file = "/temp/mem.xls";
		}else if(flag.equalsIgnoreCase("memin")){
			file = "/temp/memin.xls";
		}else if(flag.equalsIgnoreCase("memout")){
			file = "/temp/memout.xls";
		}else if(flag.equalsIgnoreCase("disk")){
			file = "/temp/disk.xls";
		}else if(flag.equalsIgnoreCase("net")){
			file = "/temp/net.xls";
		}else if(flag.equalsIgnoreCase("cpuCR")){
			file = "/temp/cpuCR.xls";
		}else if(flag.equalsIgnoreCase("cputotalCR")){
			file = "/temp/cputotalCR.xls";
		}else if(flag.equalsIgnoreCase("memCR")){
			file = "/temp/memCR.xls";
		}else if(flag.equalsIgnoreCase("memtotalCR")){
			file = "/temp/memtotalCR.xls";
		}else if(flag.equalsIgnoreCase("cpuRP")){
			file = "/temp/resourcepoolCPU.xls";
		}
		report.createReport_vm(file,flag,nowvalue,avgvalue,maxvalue);
//		request.getSession().removeAttribute("pingpath");
		request.setAttribute("filename", report.getFileName());
		return "/capreport/net/download.jsp";
		// return mapping.findForward("report_info");
	}
	
	
	
	public String findMaxValueOther(String ip, String category,String vid,  String starttime, String endtime , String tablename) throws Exception {
		String result = "";
		Hashtable hash = new Hashtable();
		Connection con = null;
		PreparedStatement stmt = null;
		DBManager dbmanager = new DBManager();
		ResultSet rs = null;
		String sid = ip.replace(".", "_");
		try {
				String sql = "";
				StringBuffer sb = new StringBuffer();
//				if (category.equals("Ping")) {
					//
					if ("mysql".equalsIgnoreCase(SystemConstant.DBType)) {
						sb.append(" select max(h."+category+") as "+category+" from "+tablename + sid + " h where ");
					} else if ("oracle".equalsIgnoreCase(SystemConstant.DBType)) {
						sb.append(" select max(h."+category+") as "+category+" from "+tablename + sid + " h where ");
					}

//				} 
				sb.append(" h.vid='");
				sb.append(vid);
				if ("mysql".equalsIgnoreCase(SystemConstant.DBType)) {
					sb.append("' and h.collecttime >= '");
					sb.append(starttime);
					sb.append("' and h.collecttime <= '");
					sb.append(endtime);
					sb.append("'");
				} else if ("oracle".equalsIgnoreCase(SystemConstant.DBType)) {
					sb.append("' and h.collecttime >= to_date('");
					sb.append(starttime);
					sb.append("','YYYY-MM-DD HH24:MI:SS') and h.collecttime <= to_date('");
					sb.append(endtime);
					sb.append("','YYYY-MM-DD HH24:MI:SS')");
				}

				sql = sb.toString();
				
//				System.out.println("--------HostCollectDataManager----max---->"+sql);
				
				rs = dbmanager.executeQuery(sql);
				List list1 = new ArrayList();
				if (rs != null) {
					while (rs.next()) {
						result = rs.getString(category);
					}
					rs.close();
				}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null)
				rs.close();
			dbmanager.close();
		}

		return result;
	}
	
	
	public String findAvgValueOther(String ip, String category,String vid,  String starttime, String endtime,String tablename) throws Exception {
		String result = "";
		Hashtable hash = new Hashtable();
		Connection con = null;
		PreparedStatement stmt = null;
		DBManager dbmanager = new DBManager();
		ResultSet rs = null;
		String sid = ip.replace(".", "_");
		try {
				String sql = "";
				StringBuffer sb = new StringBuffer();
//				if (category.equals("Ping")) {
					//
					if ("mysql".equalsIgnoreCase(SystemConstant.DBType)) {
						sb.append(" select round(avg(h."+category+"),2) as "+category+" from "+tablename + sid + " h where ");
					} else if ("oracle".equalsIgnoreCase(SystemConstant.DBType)) {
						sb.append(" select avg(h."+category+") as "+category+" from "+tablename + sid + " h where ");
					}

//				} 
				sb.append(" h.vid='");
				sb.append(vid);
				if ("mysql".equalsIgnoreCase(SystemConstant.DBType)) {
					sb.append("' and h.collecttime >= '");
					sb.append(starttime);
					sb.append("' and h.collecttime <= '");
					sb.append(endtime);
					sb.append("'");
				} else if ("oracle".equalsIgnoreCase(SystemConstant.DBType)) {
					sb.append("' and h.collecttime >= to_date('");
					sb.append(starttime);
					sb.append("','YYYY-MM-DD HH24:MI:SS') and h.collecttime <= to_date('");
					sb.append(endtime);
					sb.append("','YYYY-MM-DD HH24:MI:SS')");
				}

				sql = sb.toString();
				
//				System.out.println("--------HostCollectDataManager----avg---->"+sql);
				
				rs = dbmanager.executeQuery(sql);
				List list1 = new ArrayList();
				if (rs != null) {
					while (rs.next()) {
						result = rs.getString(category);
					}
					rs.close();
				}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null)
				rs.close();
			dbmanager.close();
		}

		return result;
	}
	
	
	public String emcFindMaxValueOther(String ip, String category,String flag,  String starttime, String endtime , String tablename,String column) throws Exception {
		String result = "";
		Hashtable hash = new Hashtable();
		Connection con = null;
		PreparedStatement stmt = null;
		DBManager dbmanager = new DBManager();
		ResultSet rs = null;
		String sid = ip.replace(".", "_");
		try {
				String sql = "";
				StringBuffer sb = new StringBuffer();
//				if (category.equals("Ping")) {
					//
					if ("mysql".equalsIgnoreCase(SystemConstant.DBType)) {
						sb.append(" select max(h."+category+") as "+category+" from "+tablename + sid + " h where ");
					} else if ("oracle".equalsIgnoreCase(SystemConstant.DBType)) {
						sb.append(" select max(h."+category+") as "+category+" from "+tablename + sid + " h where ");
					}

//				} 
					if(!column.equalsIgnoreCase("null")){
				sb.append(" h."+column+"='");
				sb.append(flag);
				sb.append("' and ");
					}
				if ("mysql".equalsIgnoreCase(SystemConstant.DBType)) {
					sb.append(" h.collecttime >= '");
					sb.append(starttime);
					sb.append("' and h.collecttime <= '");
					sb.append(endtime);
					sb.append("'");
				} else if ("oracle".equalsIgnoreCase(SystemConstant.DBType)) {
					sb.append("  h.collecttime >= to_date('");
					sb.append(starttime);
					sb.append("','YYYY-MM-DD HH24:MI:SS') and h.collecttime <= to_date('");
					sb.append(endtime);
					sb.append("','YYYY-MM-DD HH24:MI:SS')");
				}

				sql = sb.toString();
				
//				System.out.println("--------HostCollectDataManager----max---->"+sql);
				
				rs = dbmanager.executeQuery(sql);
				List list1 = new ArrayList();
				if (rs != null) {
					while (rs.next()) {
						result = rs.getString(category);
					}
					rs.close();
				}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null)
				rs.close();
			dbmanager.close();
		}

		return result;
	}
	
	
	public String emcFindAvgValueOther(String ip, String category,String flag,  String starttime, String endtime,String tablename,String column) throws Exception {
		String result = "";
		Hashtable hash = new Hashtable();
		Connection con = null;
		PreparedStatement stmt = null;
		DBManager dbmanager = new DBManager();
		ResultSet rs = null;
		String sid = ip.replace(".", "_");
		try {
				String sql = "";
				StringBuffer sb = new StringBuffer();
//				if (category.equals("Ping")) {
					//
					if ("mysql".equalsIgnoreCase(SystemConstant.DBType)) {
						sb.append(" select round(avg(h."+category+"),2) as "+category+" from "+tablename + sid + " h where ");
					} else if ("oracle".equalsIgnoreCase(SystemConstant.DBType)) {
						sb.append(" select avg(h."+category+") as "+category+" from "+tablename + sid + " h where ");
					}

//				} 
			if(!column.equalsIgnoreCase("null")){
				sb.append(" h."+column+"='");
				sb.append(flag);
				sb.append("' and ");
			}
				if ("mysql".equalsIgnoreCase(SystemConstant.DBType)) {
					sb.append(" h.collecttime >= '");
					sb.append(starttime);
					sb.append("' and h.collecttime <= '");
					sb.append(endtime);
					sb.append("'");
				} else if ("oracle".equalsIgnoreCase(SystemConstant.DBType)) {
					sb.append(" h.collecttime >= to_date('");
					sb.append(starttime);
					sb.append("','YYYY-MM-DD HH24:MI:SS') and h.collecttime <= to_date('");
					sb.append(endtime);
					sb.append("','YYYY-MM-DD HH24:MI:SS')");
				}

				sql = sb.toString();
				
//				System.out.println("--------HostCollectDataManager----avg---->"+sql);
				
				rs = dbmanager.executeQuery(sql);
				List list1 = new ArrayList();
				if (rs != null) {
					while (rs.next()) {
						result = rs.getString(category);
					}
					rs.close();
				}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null)
				rs.close();
			dbmanager.close();
		}

		return result;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	private String vmwarecpuCR() {
		Date d = new Date();
		String startdate = getParaValue("startdate");
		if (startdate == null) {
			startdate = sdf0.format(d);
		}
		String todate = getParaValue("todate");
		if (todate == null) {
			todate = sdf0.format(d);
		}
		String maxvalue="";
		String avgvalue = "";
		String starttime = startdate + " 00:00:00";
		String totime = todate + " 23:59:59";
		String ipaddress = getParaValue("ip");
		String vid = getParaValue("vid");
		String nowvalue = request.getParameter("cpunow");
		String flag = (String) session.getAttribute("ipaddress");
		String flag_vid = (String) session.getAttribute("vid");
		if(flag != null && ipaddress == null){
			ipaddress = flag;
			request.getSession().removeAttribute("ipaddress");
		}
		if(flag_vid != null && vid == null){
			vid = flag_vid;
			request.getSession().removeAttribute("vid");
		}
		// �������־ȡ���˿����¼�¼���б�
		String orderflag = "ipaddress";
		if (getParaValue("orderflag") != null && !getParaValue("orderflag").equals("")) {
			orderflag = getParaValue("orderflag");
		}
 
		List orderList = new ArrayList();
		if(ipaddress != null && vid != null){
			    session.setAttribute("ipaddress", ipaddress);
			    session.setAttribute("vid", vid);
				Hashtable pinghash = new Hashtable();
				try {
					pinghash = this.getVMCategoryOther(ipaddress, "cpu",vid,  starttime, totime,"vm_cluster");
					maxvalue = this.findMaxValueOther(ipaddress, "cpu", vid, starttime, totime,"vm_cluster");
					avgvalue = this.findAvgValueOther(ipaddress, "cpu", vid, starttime, totime,"vm_cluster");
				} catch (Exception e) {
					e.printStackTrace();
				}
				Hashtable ipmemhash = new Hashtable();
				ipmemhash.put("pinghash", pinghash);
				ipmemhash.put("ipaddress",ipaddress);
				orderList.add(ipmemhash);
		}
		String pingChartDivStr = ReportHelper.getChartDivStr(orderList, "ping");
		ReportValue pingReportValue = ReportHelper.getReportValue(orderList, "ping");
		String pingpath = new ReportExport().makeJfreeChartData(pingReportValue.getListValue(), pingReportValue.getIpList(), "VMWare ��Ⱥcpuʹ�����", "", "");
        System.out.println(pingReportValue.getListValue().size());
		request.setAttribute("pingChartDivStr", pingChartDivStr);
		request.setAttribute("starttime", starttime);
		request.setAttribute("totime", totime);
		session.setAttribute("pingpath", pingpath);
		session.setAttribute("orderList", orderList);
		request.setAttribute("startdate", startdate);
		request.setAttribute("todate", todate);
		request.setAttribute("vid", vid);
		request.setAttribute("ip", ipaddress.replace("_", "."));
		request.setAttribute("maxvalue", maxvalue);
		request.setAttribute("avgvalue", avgvalue);
		request.setAttribute("nowvalue", nowvalue);
		return "/capreport/net/vmwarecpuCR.jsp";
	}
	
	
	public Hashtable getVMCategoryOther(String ip, String category,String vid,  String starttime, String endtime,String tablename) throws Exception {
		Hashtable hash = new Hashtable();
		Connection con = null;
		PreparedStatement stmt = null;
		DBManager dbmanager = new DBManager();
		ResultSet rs = null;
		String sid = ip.replace(".", "_");
		try {
				String sql = "";
				StringBuffer sb = new StringBuffer();
//				if (category.equals("Ping")) {
					//
					if ("mysql".equalsIgnoreCase(SystemConstant.DBType)) {
						sb.append(" select h."+category+",DATE_FORMAT(h.collecttime,'%Y-%m-%d %H:%i:%s') as collecttime  from "+tablename + sid + " h where ");
					} else if ("oracle".equalsIgnoreCase(SystemConstant.DBType)) {
						sb.append(" select h."+category+",to_char(h.collecttime,'YYYY-MM-DD HH24:MI:SS') as collecttime ,h.unit from "+tablename  + sid + " h where ");
					}

//				} 
				sb.append(" h.vid='");
				sb.append(vid);
				if ("mysql".equalsIgnoreCase(SystemConstant.DBType)) {
					sb.append("' and h.collecttime >= '");
					sb.append(starttime);
					sb.append("' and h.collecttime <= '");
					sb.append(endtime);
					sb.append("' order by h.collecttime asc");
				} else if ("oracle".equalsIgnoreCase(SystemConstant.DBType)) {
					sb.append("' and h.collecttime >= to_date('");
					sb.append(starttime);
					sb.append("','YYYY-MM-DD HH24:MI:SS') and h.collecttime <= to_date('");
					sb.append(endtime);
					sb.append("','YYYY-MM-DD HH24:MI:SS')");
					sb.append(" order by h.collecttime asc");
				}

				sql = sb.toString();
				
//				System.out.println("--------HostCollectDataManager-------->"+sql);
				
				rs = dbmanager.executeQuery(sql);
				List list1 = new ArrayList();
				String unit = "";
				String max = "";
				double tempfloat = 0;
				// HONGLI ADD START1
				double pingMaxFloat = 0;
				// HONGLI ADD END1
				double pingcon = 0;
				double cpucon = 0;
				double memory = 0;
				int downnum = 0;
				int i = 0;
				if (rs != null) {
					while (rs.next()) {
						i = i + 1;
						Vector v = new Vector();
						String thevalue = rs.getString(category);
						String collecttime = rs.getString("collecttime");
						v.add(0, emitStr(thevalue));
						v.add(1, collecttime);
						list1.add(v);
					}
					rs.close();
				}
				// stmt.close();

				Integer size = new Integer(0);
				hash.put("list", list1);
				if (list1.size() != 0) {
					size = new Integer(list1.size());
					if (list1.get(0) != null) {
						Vector tempV = (Vector) list1.get(0);
//						unit = (String) tempV.get(2);
					}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null)
				rs.close();
			dbmanager.close();
		}

		return hash;
	}
	
	
	public Hashtable getEMCCategory(String ip, String category,String flag,  String starttime, String endtime,String tablename,String column) throws Exception {
		Hashtable hash = new Hashtable();
		Connection con = null;
		PreparedStatement stmt = null;
		DBManager dbmanager = new DBManager();
		ResultSet rs = null;
		String sid = ip.replace(".", "_");
		try {
				String sql = "";
				StringBuffer sb = new StringBuffer();
//				if (category.equals("Ping")) {
					//
					if ("mysql".equalsIgnoreCase(SystemConstant.DBType)) {
						sb.append(" select h."+category+",DATE_FORMAT(h.collecttime,'%Y-%m-%d %H:%i:%s') as collecttime  from "+tablename + sid + " h where ");
					} else if ("oracle".equalsIgnoreCase(SystemConstant.DBType)) {
						sb.append(" select h."+category+",to_char(h.collecttime,'YYYY-MM-DD HH24:MI:SS') as collecttime ,h.unit from "+tablename  + sid + " h where ");
					}

//				} 
			    if(!column.equalsIgnoreCase("null")){
				   sb.append(" h."+column+"='");
				   sb.append(flag);
				   sb.append("' and");
			    }
				if ("mysql".equalsIgnoreCase(SystemConstant.DBType)) {
					sb.append(" h.collecttime >= '");
					sb.append(starttime);
					sb.append("' and h.collecttime <= '");
					sb.append(endtime);
					sb.append("' order by h.collecttime asc");
				} else if ("oracle".equalsIgnoreCase(SystemConstant.DBType)) {
					sb.append(" h.collecttime >= to_date('");
					sb.append(starttime);
					sb.append("','YYYY-MM-DD HH24:MI:SS') and h.collecttime <= to_date('");
					sb.append(endtime);
					sb.append("','YYYY-MM-DD HH24:MI:SS')");
					sb.append(" order by h.collecttime asc");
				}

				sql = sb.toString();
				
//				System.out.println("--------HostCollectDataManager-------->"+sql);
				
				rs = dbmanager.executeQuery(sql);
				List list1 = new ArrayList();
				String unit = "";
				String max = "";
				double tempfloat = 0;
				// HONGLI ADD START1
				double pingMaxFloat = 0;
				// HONGLI ADD END1
				double pingcon = 0;
				double cpucon = 0;
				double memory = 0;
				int downnum = 0;
				int i = 0;
				if (rs != null) {
					while (rs.next()) {
						i = i + 1;
						Vector v = new Vector();
						String thevalue = rs.getString(category);
						String collecttime = rs.getString("collecttime");
						v.add(0, emitStr(thevalue));
						v.add(1, collecttime);
						list1.add(v);
					}
					rs.close();
				}
				// stmt.close();

				Integer size = new Integer(0);
				hash.put("list", list1);
				if (list1.size() != 0) {
					size = new Integer(list1.size());
					if (list1.get(0) != null) {
						Vector tempV = (Vector) list1.get(0);
//						unit = (String) tempV.get(2);
					}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null)
				rs.close();
			dbmanager.close();
		}

		return hash;
	}
	
	
	
	
	
	
	
	
	
	
	private String vmwarecpuuseCR() {
		Date d = new Date();
		String startdate = getParaValue("startdate");
		if (startdate == null) {
			startdate = sdf0.format(d);
		}
		String todate = getParaValue("todate");
		if (todate == null) {
			todate = sdf0.format(d);
		}
		String maxvalue="";
		String avgvalue = "";
		String starttime = startdate + " 00:00:00";
		String totime = todate + " 23:59:59";
		String ipaddress = getParaValue("ip");
		String vid = getParaValue("vid");
		String nowvalue = request.getParameter("cpunow");
		String flag = (String) session.getAttribute("ipaddress");
		String flag_vid = (String) session.getAttribute("vid");
		if(flag != null && ipaddress == null){
			ipaddress = flag;
			request.getSession().removeAttribute("ipaddress");
		}
		if(flag_vid != null && vid == null){
			vid = flag_vid;
			request.getSession().removeAttribute("vid");
		}
		// �������־ȡ���˿����¼�¼���б�
		String orderflag = "ipaddress";
		if (getParaValue("orderflag") != null && !getParaValue("orderflag").equals("")) {
			orderflag = getParaValue("orderflag");
		}
 
		List orderList = new ArrayList();
		if(ipaddress != null && vid != null){
			    session.setAttribute("ipaddress", ipaddress);
			    session.setAttribute("vid", vid);
				Hashtable pinghash = new Hashtable();
				try {
					pinghash = this.getVMCategoryOther(ipaddress, "cputotal",vid,  starttime, totime,"vm_cluster");
					maxvalue = this.findMaxValueOther(ipaddress, "cputotal", vid, starttime, totime,"vm_cluster");
					avgvalue = this.findAvgValueOther(ipaddress, "cputotal", vid, starttime, totime,"vm_cluster");
				} catch (Exception e) {
					e.printStackTrace();
				}
				Hashtable ipmemhash = new Hashtable();
				ipmemhash.put("pinghash", pinghash);
				ipmemhash.put("ipaddress",ipaddress);
				orderList.add(ipmemhash);
		}
		String pingChartDivStr = ReportHelper.getChartDivStr(orderList, "ping");
		ReportValue pingReportValue = ReportHelper.getReportValue(orderList, "ping");
		String pingpath = new ReportExport().makeJfreeChartData(pingReportValue.getListValue(), pingReportValue.getIpList(), "VMWare ��Ⱥcpu�ܼ�", "", "");
//        System.out.println(pingReportValue.getListValue().size());
		request.setAttribute("pingChartDivStr", pingChartDivStr);
		request.setAttribute("starttime", starttime);
		request.setAttribute("totime", totime);
		session.setAttribute("pingpath", pingpath);
		session.setAttribute("orderList", orderList);
		request.setAttribute("startdate", startdate);
		request.setAttribute("todate", todate);
		request.setAttribute("vid", vid);
		request.setAttribute("ip", ipaddress.replace("_", "."));
		request.setAttribute("maxvalue", maxvalue);
		request.setAttribute("avgvalue", avgvalue);
		request.setAttribute("nowvalue", nowvalue);
		return "/capreport/net/vmwarecputotalCR.jsp";
	}
	
	
	private String vmwarememCR() {
		Date d = new Date();
		String startdate = getParaValue("startdate");
		if (startdate == null) {
			startdate = sdf0.format(d);
		}
		String todate = getParaValue("todate");
		if (todate == null) {
			todate = sdf0.format(d);
		}
		String maxvalue="";
		String avgvalue = "";
		String starttime = startdate + " 00:00:00";
		String totime = todate + " 23:59:59";
		String ipaddress = getParaValue("ip");
		String vid = getParaValue("vid");
		String nowvalue = request.getParameter("cpunow");
		String flag = (String) session.getAttribute("ipaddress");
		String flag_vid = (String) session.getAttribute("vid");
		if(flag != null && ipaddress == null){
			ipaddress = flag;
			request.getSession().removeAttribute("ipaddress");
		}
		if(flag_vid != null && vid == null){
			vid = flag_vid;
			request.getSession().removeAttribute("vid");
		}
		// �������־ȡ���˿����¼�¼���б�
		String orderflag = "ipaddress";
		if (getParaValue("orderflag") != null && !getParaValue("orderflag").equals("")) {
			orderflag = getParaValue("orderflag");
		}
 
		List orderList = new ArrayList();
		if(ipaddress != null && vid != null){
			    session.setAttribute("ipaddress", ipaddress);
			    session.setAttribute("vid", vid);
				Hashtable pinghash = new Hashtable();
				try {
					pinghash = this.getVMCategoryOther(ipaddress, "mem",vid,  starttime, totime,"vm_cluster");
					maxvalue = this.findMaxValueOther(ipaddress, "mem", vid, starttime, totime,"vm_cluster");
					avgvalue = this.findAvgValueOther(ipaddress, "mem", vid, starttime, totime,"vm_cluster");
				} catch (Exception e) {
					e.printStackTrace();
				}
				Hashtable ipmemhash = new Hashtable();
				ipmemhash.put("pinghash", pinghash);
				ipmemhash.put("ipaddress",ipaddress);
				orderList.add(ipmemhash);
		}
		String pingChartDivStr = ReportHelper.getChartDivStr(orderList, "ping");
		ReportValue pingReportValue = ReportHelper.getReportValue(orderList, "ping");
		String pingpath = new ReportExport().makeJfreeChartData(pingReportValue.getListValue(), pingReportValue.getIpList(), "VMWare ��Ⱥ�ڴ�ʹ�����", "", "");
//        System.out.println(pingReportValue.getListValue().size());
		request.setAttribute("pingChartDivStr", pingChartDivStr);
		request.setAttribute("starttime", starttime);
		request.setAttribute("totime", totime);
		session.setAttribute("pingpath", pingpath);
		session.setAttribute("orderList", orderList);
		request.setAttribute("startdate", startdate);
		request.setAttribute("todate", todate);
		request.setAttribute("vid", vid);
		request.setAttribute("ip", ipaddress.replace("_", "."));
		request.setAttribute("maxvalue", maxvalue);
		request.setAttribute("avgvalue", avgvalue);
		request.setAttribute("nowvalue", nowvalue);
		return "/capreport/net/vmwarememCR.jsp";
	}
	
	private String vmwarememuseCR() {
		Date d = new Date();
		String startdate = getParaValue("startdate");
		if (startdate == null) {
			startdate = sdf0.format(d);
		}
		String todate = getParaValue("todate");
		if (todate == null) {
			todate = sdf0.format(d);
		}
		String maxvalue="";
		String avgvalue = "";
		String starttime = startdate + " 00:00:00";
		String totime = todate + " 23:59:59";
		String ipaddress = getParaValue("ip");
		String vid = getParaValue("vid");
		String nowvalue = request.getParameter("cpunow");
		String flag = (String) session.getAttribute("ipaddress");
		String flag_vid = (String) session.getAttribute("vid");
		if(flag != null && ipaddress == null){
			ipaddress = flag;
			request.getSession().removeAttribute("ipaddress");
		}
		if(flag_vid != null && vid == null){
			vid = flag_vid;
			request.getSession().removeAttribute("vid");
		}
		// �������־ȡ���˿����¼�¼���б�
		String orderflag = "ipaddress";
		if (getParaValue("orderflag") != null && !getParaValue("orderflag").equals("")) {
			orderflag = getParaValue("orderflag");
		}
 
		List orderList = new ArrayList();
		if(ipaddress != null && vid != null){
			    session.setAttribute("ipaddress", ipaddress);
			    session.setAttribute("vid", vid);
				Hashtable pinghash = new Hashtable();
				try {
					pinghash = this.getVMCategoryOther(ipaddress, "memtotal",vid,  starttime, totime,"vm_cluster");
					maxvalue = this.findMaxValueOther(ipaddress, "memtotal", vid, starttime, totime,"vm_cluster");
					avgvalue = this.findAvgValueOther(ipaddress, "memtotal", vid, starttime, totime,"vm_cluster");
				} catch (Exception e) {
					e.printStackTrace();
				}
				Hashtable ipmemhash = new Hashtable();
				ipmemhash.put("pinghash", pinghash);
				ipmemhash.put("ipaddress",ipaddress);
				orderList.add(ipmemhash);
		}
		String pingChartDivStr = ReportHelper.getChartDivStr(orderList, "ping");
		ReportValue pingReportValue = ReportHelper.getReportValue(orderList, "ping");
		String pingpath = new ReportExport().makeJfreeChartData(pingReportValue.getListValue(), pingReportValue.getIpList(), "VMWare ��Ⱥ�ڴ��ܼ�", "", "");
        System.out.println(pingReportValue.getListValue().size());
		request.setAttribute("pingChartDivStr", pingChartDivStr);
		request.setAttribute("starttime", starttime);
		request.setAttribute("totime", totime);
		session.setAttribute("pingpath", pingpath);
		session.setAttribute("orderList", orderList);
		request.setAttribute("startdate", startdate);
		request.setAttribute("todate", todate);
		request.setAttribute("vid", vid);
		request.setAttribute("ip", ipaddress.replace("_", "."));
		request.setAttribute("maxvalue", maxvalue);
		request.setAttribute("avgvalue", avgvalue);
		request.setAttribute("nowvalue", nowvalue);
		return "/capreport/net/vmwarememtotalCR.jsp";
	}
	
	
	
	private String vmwarecpuRP() {
		Date d = new Date();
		String startdate = getParaValue("startdate");
		if (startdate == null) {
			startdate = sdf0.format(d);
		}
		String todate = getParaValue("todate");
		if (todate == null) {
			todate = sdf0.format(d);
		}
		String maxvalue="";
		String avgvalue = "";
		String starttime = startdate + " 00:00:00";
		String totime = todate + " 23:59:59";
		String ipaddress = getParaValue("ip");
		String vid = getParaValue("vid");
		String nowvalue = request.getParameter("cpunow");
		String flag = (String) session.getAttribute("ipaddress");
		String flag_vid = (String) session.getAttribute("vid");
		if(flag != null && ipaddress == null){
			ipaddress = flag;
			request.getSession().removeAttribute("ipaddress");
		}
		if(flag_vid != null && vid == null){
			vid = flag_vid;
			request.getSession().removeAttribute("vid");
		}
		// �������־ȡ���˿����¼�¼���б�
		String orderflag = "ipaddress";
		if (getParaValue("orderflag") != null && !getParaValue("orderflag").equals("")) {
			orderflag = getParaValue("orderflag");
		}
 
		List orderList = new ArrayList();
		if(ipaddress != null && vid != null){
			    session.setAttribute("ipaddress", ipaddress);
			    session.setAttribute("vid", vid);
				Hashtable pinghash = new Hashtable();
				try {
					pinghash = this.getVMCategoryOther(ipaddress, "cpu",vid,  starttime, totime,"vm_resourcepool");
					maxvalue = this.findMaxValueOther(ipaddress, "cpu", vid, starttime, totime,"vm_resourcepool");
					avgvalue = this.findAvgValueOther(ipaddress, "cpu", vid, starttime, totime,"vm_resourcepool");
				} catch (Exception e) {
					e.printStackTrace();
				}
				Hashtable ipmemhash = new Hashtable();
				ipmemhash.put("pinghash", pinghash);
				ipmemhash.put("ipaddress",ipaddress);
				orderList.add(ipmemhash);
		}
		String pingChartDivStr = ReportHelper.getChartDivStr(orderList, "ping");
		ReportValue pingReportValue = ReportHelper.getReportValue(orderList, "ping");
		String pingpath = new ReportExport().makeJfreeChartData(pingReportValue.getListValue(), pingReportValue.getIpList(), "VMWare ��Դ��cpuʹ�����", "", "");
        System.out.println(pingReportValue.getListValue().size());
		request.setAttribute("pingChartDivStr", pingChartDivStr);
		request.setAttribute("starttime", starttime);
		request.setAttribute("totime", totime);
		session.setAttribute("pingpath", pingpath);
		session.setAttribute("orderList", orderList);
		request.setAttribute("startdate", startdate);
		request.setAttribute("todate", todate);
		request.setAttribute("vid", vid);
		request.setAttribute("ip", ipaddress.replace("_", "."));
		request.setAttribute("maxvalue", maxvalue);
		request.setAttribute("avgvalue", avgvalue);
		request.setAttribute("nowvalue", nowvalue);
		return "/capreport/net/vmwarecpuRP.jsp";
	}
	
	
	private String emcTotalLength() {
		Date d = new Date();
		String startdate = getParaValue("startdate");
		if (startdate == null) {
			startdate = sdf0.format(d);
		}
		String todate = getParaValue("todate");
		if (todate == null) {
			todate = sdf0.format(d);
		}
		String maxvalue="";
		String avgvalue = "";
		String starttime = startdate + " 00:00:00";
		String totime = todate + " 23:59:59";
		String ipaddress = getParaValue("ip");
		String name = getParaValue("flag");
		String nowvalue = request.getParameter("totallength");
		String flag = (String) session.getAttribute("ipaddress");
		String flag_name = (String) session.getAttribute("flag");
		if(flag != null && ipaddress == null){
			ipaddress = flag;
			request.getSession().removeAttribute("ipaddress");
		}
		if(flag_name != null && name == null){
			name = flag_name;
			request.getSession().removeAttribute("flag");
		}
		// �������־ȡ���˿����¼�¼���б�
		String orderflag = "ipaddress";
		if (getParaValue("orderflag") != null && !getParaValue("orderflag").equals("")) {
			orderflag = getParaValue("orderflag");
		}
 
		List orderList = new ArrayList();
		if(ipaddress != null && name != null){
			    session.setAttribute("ipaddress", ipaddress);
			    session.setAttribute("flag", name);
				Hashtable pinghash = new Hashtable();
				try {
					pinghash = this.getEMCCategory(ipaddress, "totalqueuelength",name,  starttime, totime,"emclunper","name");
					maxvalue = this.emcFindMaxValueOther(ipaddress, "totalqueuelength", name, starttime, totime,"emclunper","name");
					avgvalue = this.emcFindAvgValueOther(ipaddress, "totalqueuelength", name, starttime, totime,"emclunper","name");
				} catch (Exception e) {
					e.printStackTrace();
				}
				Hashtable ipmemhash = new Hashtable();
				ipmemhash.put("pinghash", pinghash);
				ipmemhash.put("ipaddress",ipaddress);
				orderList.add(ipmemhash);
		}
		String pingChartDivStr = ReportHelper.getChartDivStr(orderList, "ping");
		ReportValue pingReportValue = ReportHelper.getReportValue(orderList, "ping");
		String pingpath = new ReportExport().makeJfreeChartData(pingReportValue.getListValue(), pingReportValue.getIpList(), "emc Lun�����ܳ���", "", "");
        System.out.println(pingReportValue.getListValue().size());
		request.setAttribute("pingChartDivStr", pingChartDivStr);
		request.setAttribute("starttime", starttime);
		request.setAttribute("totime", totime);
		session.setAttribute("pingpath", pingpath);
		session.setAttribute("orderList", orderList);
		request.setAttribute("startdate", startdate);
		request.setAttribute("todate", todate);
		request.setAttribute("name", name);
		request.setAttribute("ip", ipaddress.replace("_", "."));
		request.setAttribute("maxvalue", maxvalue);
		request.setAttribute("avgvalue", avgvalue);
		request.setAttribute("nowvalue", nowvalue);
		return "/capreport/net/emcLunLength.jsp";
	}
	
	private String emcLunSofterrors() {
		Date d = new Date();
		String startdate = getParaValue("startdate");
		if (startdate == null) {
			startdate = sdf0.format(d);
		}
		String todate = getParaValue("todate");
		if (todate == null) {
			todate = sdf0.format(d);
		}
		String maxvalue="";
		String avgvalue = "";
		String starttime = startdate + " 00:00:00";
		String totime = todate + " 23:59:59";
		String ipaddress = getParaValue("ip");
		String name = getParaValue("flag");
		String nowvalue = request.getParameter("totallength");
		String flag = (String) session.getAttribute("ipaddress");
		String flag_name = (String) session.getAttribute("flag");
		if(flag != null && ipaddress == null){
			ipaddress = flag;
			request.getSession().removeAttribute("ipaddress");
		}
		if(flag_name != null && name == null){
			name = flag_name;
			request.getSession().removeAttribute("flag");
		}
		// �������־ȡ���˿����¼�¼���б�
		String orderflag = "ipaddress";
		if (getParaValue("orderflag") != null && !getParaValue("orderflag").equals("")) {
			orderflag = getParaValue("orderflag");
		}
 
		List orderList = new ArrayList();
		if(ipaddress != null && name != null){
			    session.setAttribute("ipaddress", ipaddress);
			    session.setAttribute("flag", name);
				Hashtable pinghash = new Hashtable();
				try {
					pinghash = this.getEMCCategory(ipaddress, "totalsofterrors",name,  starttime, totime,"emclunper","name");
					maxvalue = this.emcFindMaxValueOther(ipaddress, "totalsofterrors", name, starttime, totime,"emclunper","name");
					avgvalue = this.emcFindAvgValueOther(ipaddress, "totalsofterrors", name, starttime, totime,"emclunper","name");
				} catch (Exception e) {
					e.printStackTrace();
				}
				Hashtable ipmemhash = new Hashtable();
				ipmemhash.put("pinghash", pinghash);
				ipmemhash.put("ipaddress",ipaddress);
				orderList.add(ipmemhash);
		}
		String pingChartDivStr = ReportHelper.getChartDivStr(orderList, "ping");
		ReportValue pingReportValue = ReportHelper.getReportValue(orderList, "ping");
		String pingpath = new ReportExport().makeJfreeChartData(pingReportValue.getListValue(), pingReportValue.getIpList(), "emc Lun�����������", "", "");
        System.out.println(pingReportValue.getListValue().size());
		request.setAttribute("pingChartDivStr", pingChartDivStr);
		request.setAttribute("starttime", starttime);
		request.setAttribute("totime", totime);
		session.setAttribute("pingpath", pingpath);
		session.setAttribute("orderList", orderList);
		request.setAttribute("startdate", startdate);
		request.setAttribute("todate", todate);
		request.setAttribute("name", name);
		request.setAttribute("ip", ipaddress.replace("_", "."));
		request.setAttribute("maxvalue", maxvalue);
		request.setAttribute("avgvalue", avgvalue);
		request.setAttribute("nowvalue", nowvalue);
		return "/capreport/net/emcLunsoft.jsp";
	}
	
	
	private String emcLunHarderrors() {
		Date d = new Date();
		String startdate = getParaValue("startdate");
		if (startdate == null) {
			startdate = sdf0.format(d);
		}
		String todate = getParaValue("todate");
		if (todate == null) {
			todate = sdf0.format(d);
		}
		String maxvalue="";
		String avgvalue = "";
		String starttime = startdate + " 00:00:00";
		String totime = todate + " 23:59:59";
		String ipaddress = getParaValue("ip");
		String name = getParaValue("flag");
		String nowvalue = request.getParameter("totallength");
		String flag = (String) session.getAttribute("ipaddress");
		String flag_name = (String) session.getAttribute("flag");
		if(flag != null && ipaddress == null){
			ipaddress = flag;
			request.getSession().removeAttribute("ipaddress");
		}
		if(flag_name != null && name == null){
			name = flag_name;
			request.getSession().removeAttribute("flag");
		}
		// �������־ȡ���˿����¼�¼���б�
		String orderflag = "ipaddress";
		if (getParaValue("orderflag") != null && !getParaValue("orderflag").equals("")) {
			orderflag = getParaValue("orderflag");
		}
 
		List orderList = new ArrayList();
		if(ipaddress != null && name != null){
			    session.setAttribute("ipaddress", ipaddress);
			    session.setAttribute("flag", name);
				Hashtable pinghash = new Hashtable();
				try {
					pinghash = this.getEMCCategory(ipaddress, "totalqueuelength",name,  starttime, totime,"emclunper","name");
					maxvalue = this.emcFindMaxValueOther(ipaddress, "totalqueuelength", name, starttime, totime,"emclunper","name");
					avgvalue = this.emcFindAvgValueOther(ipaddress, "totalqueuelength", name, starttime, totime,"emclunper","name");
				} catch (Exception e) {
					e.printStackTrace();
				}
				Hashtable ipmemhash = new Hashtable();
				ipmemhash.put("pinghash", pinghash);
				ipmemhash.put("ipaddress",ipaddress);
				orderList.add(ipmemhash);
		}
		String pingChartDivStr = ReportHelper.getChartDivStr(orderList, "ping");
		ReportValue pingReportValue = ReportHelper.getReportValue(orderList, "ping");
		String pingpath = new ReportExport().makeJfreeChartData(pingReportValue.getListValue(), pingReportValue.getIpList(), "emc LunӲ����������", "", "");
        System.out.println(pingReportValue.getListValue().size());
		request.setAttribute("pingChartDivStr", pingChartDivStr);
		request.setAttribute("starttime", starttime);
		request.setAttribute("totime", totime);
		session.setAttribute("pingpath", pingpath);
		session.setAttribute("orderList", orderList);
		request.setAttribute("startdate", startdate);
		request.setAttribute("todate", todate);
		request.setAttribute("name", name);
		request.setAttribute("ip", ipaddress.replace("_", "."));
		request.setAttribute("maxvalue", maxvalue);
		request.setAttribute("avgvalue", avgvalue);
		request.setAttribute("nowvalue", nowvalue);
		return "/capreport/net/emcLunHard.jsp";
	}
	
	
	private String emcdiskread() {
		Date d = new Date();
		String startdate = getParaValue("startdate");
		if (startdate == null) {
			startdate = sdf0.format(d);
		}
		String todate = getParaValue("todate");
		if (todate == null) {
			todate = sdf0.format(d);
		}
		String maxvalue="";
		String avgvalue = "";
		String starttime = startdate + " 00:00:00";
		String totime = todate + " 23:59:59";
		String ipaddress = getParaValue("ip");
		String name = getParaValue("flag");
		String nowvalue = request.getParameter("totallength");
		String flag = (String) session.getAttribute("ipaddress");
		String flag_name = (String) session.getAttribute("flag");
		if(flag != null && ipaddress == null){
			ipaddress = flag;
			request.getSession().removeAttribute("ipaddress");
		}
		if(flag_name != null && name == null){
			name = flag_name;
			request.getSession().removeAttribute("flag");
		}
		// �������־ȡ���˿����¼�¼���б�
		String orderflag = "ipaddress";
		if (getParaValue("orderflag") != null && !getParaValue("orderflag").equals("")) {
			orderflag = getParaValue("orderflag");
		}
 
		List orderList = new ArrayList();
		if(ipaddress != null && name != null){
			    session.setAttribute("ipaddress", ipaddress);
			    session.setAttribute("flag", name);
				Hashtable pinghash = new Hashtable();
				try {
					pinghash = this.getEMCCategory(ipaddress, "numberofreads",name,  starttime, totime,"emcdiskper","serialnumber");
					maxvalue = this.emcFindMaxValueOther(ipaddress, "numberofreads", name, starttime, totime,"emcdiskper","serialnumber");
					avgvalue = this.emcFindAvgValueOther(ipaddress, "numberofreads", name, starttime, totime,"emcdiskper","serialnumber");
				} catch (Exception e) {
					e.printStackTrace();
				}
				Hashtable ipmemhash = new Hashtable();
				ipmemhash.put("pinghash", pinghash);
				ipmemhash.put("ipaddress",ipaddress);
				orderList.add(ipmemhash);
		}
		String pingChartDivStr = ReportHelper.getChartDivStr(orderList, "ping");
		ReportValue pingReportValue = ReportHelper.getReportValue(orderList, "ping");
		String pingpath = new ReportExport().makeJfreeChartData(pingReportValue.getListValue(), pingReportValue.getIpList(), "emc ���̶�������", "", "");
        System.out.println(pingReportValue.getListValue().size());
		request.setAttribute("pingChartDivStr", pingChartDivStr);
		request.setAttribute("starttime", starttime);
		request.setAttribute("totime", totime);
		session.setAttribute("pingpath", pingpath);
		session.setAttribute("orderList", orderList);
		request.setAttribute("startdate", startdate);
		request.setAttribute("todate", todate);
		request.setAttribute("name", name);
		request.setAttribute("ip", ipaddress.replace("_", "."));
		request.setAttribute("maxvalue", maxvalue);
		request.setAttribute("avgvalue", avgvalue);
		request.setAttribute("nowvalue", nowvalue);
		return "/capreport/net/emcDiskRead.jsp";
	}
	
	
	private String emcdisksoftread() {
		Date d = new Date();
		String startdate = getParaValue("startdate");
		if (startdate == null) {
			startdate = sdf0.format(d);
		}
		String todate = getParaValue("todate");
		if (todate == null) {
			todate = sdf0.format(d);
		}
		String maxvalue="";
		String avgvalue = "";
		String starttime = startdate + " 00:00:00";
		String totime = todate + " 23:59:59";
		String ipaddress = getParaValue("ip");
		String name = getParaValue("flag");
		String nowvalue = request.getParameter("totallength");
		String flag = (String) session.getAttribute("ipaddress");
		String flag_name = (String) session.getAttribute("flag");
		if(flag != null && ipaddress == null){
			ipaddress = flag;
			request.getSession().removeAttribute("ipaddress");
		}
		if(flag_name != null && name == null){
			name = flag_name;
			request.getSession().removeAttribute("flag");
		}
		// �������־ȡ���˿����¼�¼���б�
		String orderflag = "ipaddress";
		if (getParaValue("orderflag") != null && !getParaValue("orderflag").equals("")) {
			orderflag = getParaValue("orderflag");
		}
 
		List orderList = new ArrayList();
		if(ipaddress != null && name != null){
			    session.setAttribute("ipaddress", ipaddress);
			    session.setAttribute("flag", name);
				Hashtable pinghash = new Hashtable();
				try {
					pinghash = this.getEMCCategory(ipaddress, "softreaderrors",name,  starttime, totime,"emcdiskper","serialnumber");
					maxvalue = this.emcFindMaxValueOther(ipaddress, "softreaderrors", name, starttime, totime,"emcdiskper","serialnumber");
					avgvalue = this.emcFindAvgValueOther(ipaddress, "softreaderrors", name, starttime, totime,"emcdiskper","serialnumber");
				} catch (Exception e) {
					e.printStackTrace();
				}
				Hashtable ipmemhash = new Hashtable();
				ipmemhash.put("pinghash", pinghash);
				ipmemhash.put("ipaddress",ipaddress);
				orderList.add(ipmemhash);
		}
		String pingChartDivStr = ReportHelper.getChartDivStr(orderList, "ping");
		ReportValue pingReportValue = ReportHelper.getReportValue(orderList, "ping");
		String pingpath = new ReportExport().makeJfreeChartData(pingReportValue.getListValue(), pingReportValue.getIpList(), "emc ���������������", "", "");
        System.out.println(pingReportValue.getListValue().size());
		request.setAttribute("pingChartDivStr", pingChartDivStr);
		request.setAttribute("starttime", starttime);
		request.setAttribute("totime", totime);
		session.setAttribute("pingpath", pingpath);
		session.setAttribute("orderList", orderList);
		request.setAttribute("startdate", startdate);
		request.setAttribute("todate", todate);
		request.setAttribute("name", name);
		request.setAttribute("ip", ipaddress.replace("_", "."));
		request.setAttribute("maxvalue", maxvalue);
		request.setAttribute("avgvalue", avgvalue);
		request.setAttribute("nowvalue", nowvalue);
		return "/capreport/net/emcDiskSoftRead.jsp";
	}
	
	private String emcdisksoftwrite() {
		Date d = new Date();
		String startdate = getParaValue("startdate");
		if (startdate == null) {
			startdate = sdf0.format(d);
		}
		String todate = getParaValue("todate");
		if (todate == null) {
			todate = sdf0.format(d);
		}
		String maxvalue="";
		String avgvalue = "";
		String starttime = startdate + " 00:00:00";
		String totime = todate + " 23:59:59";
		String ipaddress = getParaValue("ip");
		String name = getParaValue("flag");
		String nowvalue = request.getParameter("totallength");
		String flag = (String) session.getAttribute("ipaddress");
		String flag_name = (String) session.getAttribute("flag");
		if(flag != null && ipaddress == null){
			ipaddress = flag;
			request.getSession().removeAttribute("ipaddress");
		}
		if(flag_name != null && name == null){
			name = flag_name;
			request.getSession().removeAttribute("flag");
		}
		// �������־ȡ���˿����¼�¼���б�
		String orderflag = "ipaddress";
		if (getParaValue("orderflag") != null && !getParaValue("orderflag").equals("")) {
			orderflag = getParaValue("orderflag");
		}
 
		List orderList = new ArrayList();
		if(ipaddress != null && name != null){
			    session.setAttribute("ipaddress", ipaddress);
			    session.setAttribute("flag", name);
				Hashtable pinghash = new Hashtable();
				try {
					pinghash = this.getEMCCategory(ipaddress, "softwriteerrors",name,  starttime, totime,"emcdiskper","serialnumber");
					maxvalue = this.emcFindMaxValueOther(ipaddress, "softwriteerrors", name, starttime, totime,"emcdiskper","serialnumber");
					avgvalue = this.emcFindAvgValueOther(ipaddress, "softwriteerrors", name, starttime, totime,"emcdiskper","serialnumber");
				} catch (Exception e) {
					e.printStackTrace();
				}
				Hashtable ipmemhash = new Hashtable();
				ipmemhash.put("pinghash", pinghash);
				ipmemhash.put("ipaddress",ipaddress);
				orderList.add(ipmemhash);
		}
		String pingChartDivStr = ReportHelper.getChartDivStr(orderList, "ping");
		ReportValue pingReportValue = ReportHelper.getReportValue(orderList, "ping");
		String pingpath = new ReportExport().makeJfreeChartData(pingReportValue.getListValue(), pingReportValue.getIpList(), "emc �������д������", "", "");
        System.out.println(pingReportValue.getListValue().size());
		request.setAttribute("pingChartDivStr", pingChartDivStr);
		request.setAttribute("starttime", starttime);
		request.setAttribute("totime", totime);
		session.setAttribute("pingpath", pingpath);
		session.setAttribute("orderList", orderList);
		request.setAttribute("startdate", startdate);
		request.setAttribute("todate", todate);
		request.setAttribute("name", name);
		request.setAttribute("ip", ipaddress.replace("_", "."));
		request.setAttribute("maxvalue", maxvalue);
		request.setAttribute("avgvalue", avgvalue);
		request.setAttribute("nowvalue", nowvalue);
		return "/capreport/net/emcDiskSoftWrite.jsp";
	}
	
	private String emcdiskreadkb() {
		Date d = new Date();
		String startdate = getParaValue("startdate");
		if (startdate == null) {
			startdate = sdf0.format(d);
		}
		String todate = getParaValue("todate");
		if (todate == null) {
			todate = sdf0.format(d);
		}
		String maxvalue="";
		String avgvalue = "";
		String starttime = startdate + " 00:00:00";
		String totime = todate + " 23:59:59";
		String ipaddress = getParaValue("ip");
		String name = getParaValue("flag");
		String nowvalue = request.getParameter("totallength");
		String flag = (String) session.getAttribute("ipaddress");
		String flag_name = (String) session.getAttribute("flag");
		if(flag != null && ipaddress == null){
			ipaddress = flag;
			request.getSession().removeAttribute("ipaddress");
		}
		if(flag_name != null && name == null){
			name = flag_name;
			request.getSession().removeAttribute("flag");
		}
		// �������־ȡ���˿����¼�¼���б�
		String orderflag = "ipaddress";
		if (getParaValue("orderflag") != null && !getParaValue("orderflag").equals("")) {
			orderflag = getParaValue("orderflag");
		}
 
		List orderList = new ArrayList();
		if(ipaddress != null && name != null){
			    session.setAttribute("ipaddress", ipaddress);
			    session.setAttribute("flag", name);
				Hashtable pinghash = new Hashtable();
				try {
					pinghash = this.getEMCCategory(ipaddress, "kbytesread",name,  starttime, totime,"emcdiskper","serialnumber");
					maxvalue = this.emcFindMaxValueOther(ipaddress, "kbytesread", name, starttime, totime,"emcdiskper","serialnumber");
					avgvalue = this.emcFindAvgValueOther(ipaddress, "kbytesread", name, starttime, totime,"emcdiskper","serialnumber");
				} catch (Exception e) {
					e.printStackTrace();
				}
				Hashtable ipmemhash = new Hashtable();
				ipmemhash.put("pinghash", pinghash);
				ipmemhash.put("ipaddress",ipaddress);
				orderList.add(ipmemhash);
		}
		String pingChartDivStr = ReportHelper.getChartDivStr(orderList, "ping");
		ReportValue pingReportValue = ReportHelper.getReportValue(orderList, "ping");
		String pingpath = new ReportExport().makeJfreeChartData(pingReportValue.getListValue(), pingReportValue.getIpList(), "emc ���̶�KB", "", "");
        System.out.println(pingReportValue.getListValue().size());
		request.setAttribute("pingChartDivStr", pingChartDivStr);
		request.setAttribute("starttime", starttime);
		request.setAttribute("totime", totime);
		session.setAttribute("pingpath", pingpath);
		session.setAttribute("orderList", orderList);
		request.setAttribute("startdate", startdate);
		request.setAttribute("todate", todate);
		request.setAttribute("name", name);
		request.setAttribute("ip", ipaddress.replace("_", "."));
		request.setAttribute("maxvalue", maxvalue);
		request.setAttribute("avgvalue", avgvalue);
		request.setAttribute("nowvalue", nowvalue);
		return "/capreport/net/emcDiskReadKb.jsp";
	}
	
	private String emcdiskwritekb() {
		Date d = new Date();
		String startdate = getParaValue("startdate");
		if (startdate == null) {
			startdate = sdf0.format(d);
		}
		String todate = getParaValue("todate");
		if (todate == null) {
			todate = sdf0.format(d);
		}
		String maxvalue="";
		String avgvalue = "";
		String starttime = startdate + " 00:00:00";
		String totime = todate + " 23:59:59";
		String ipaddress = getParaValue("ip");
		String name = getParaValue("flag");
		String nowvalue = request.getParameter("totallength");
		String flag = (String) session.getAttribute("ipaddress");
		String flag_name = (String) session.getAttribute("flag");
		if(flag != null && ipaddress == null){
			ipaddress = flag;
			request.getSession().removeAttribute("ipaddress");
		}
		if(flag_name != null && name == null){
			name = flag_name;
			request.getSession().removeAttribute("flag");
		}
		// �������־ȡ���˿����¼�¼���б�
		String orderflag = "ipaddress";
		if (getParaValue("orderflag") != null && !getParaValue("orderflag").equals("")) {
			orderflag = getParaValue("orderflag");
		}
 
		List orderList = new ArrayList();
		if(ipaddress != null && name != null){
			    session.setAttribute("ipaddress", ipaddress);
			    session.setAttribute("flag", name);
				Hashtable pinghash = new Hashtable();
				try {
					pinghash = this.getEMCCategory(ipaddress, "kbyteswritten",name,  starttime, totime,"emcdiskper","serialnumber");
					maxvalue = this.emcFindMaxValueOther(ipaddress, "kbyteswritten", name, starttime, totime,"emcdiskper","serialnumber");
					avgvalue = this.emcFindAvgValueOther(ipaddress, "kbyteswritten", name, starttime, totime,"emcdiskper","serialnumber");
				} catch (Exception e) {
					e.printStackTrace();
				}
				Hashtable ipmemhash = new Hashtable();
				ipmemhash.put("pinghash", pinghash);
				ipmemhash.put("ipaddress",ipaddress);
				orderList.add(ipmemhash);
		}
		String pingChartDivStr = ReportHelper.getChartDivStr(orderList, "ping");
		ReportValue pingReportValue = ReportHelper.getReportValue(orderList, "ping");
		String pingpath = new ReportExport().makeJfreeChartData(pingReportValue.getListValue(), pingReportValue.getIpList(), "emc����дKB", "", "");
        System.out.println(pingReportValue.getListValue().size());
		request.setAttribute("pingChartDivStr", pingChartDivStr);
		request.setAttribute("starttime", starttime);
		request.setAttribute("totime", totime);
		session.setAttribute("pingpath", pingpath);
		session.setAttribute("orderList", orderList);
		request.setAttribute("startdate", startdate);
		request.setAttribute("todate", todate);
		request.setAttribute("name", name);
		request.setAttribute("ip", ipaddress.replace("_", "."));
		request.setAttribute("maxvalue", maxvalue);
		request.setAttribute("avgvalue", avgvalue);
		request.setAttribute("nowvalue", nowvalue);
		return "/capreport/net/emcDiskWriteKb.jsp";
	}
	
	private String emcdiskfree() {
		Date d = new Date();
		String startdate = getParaValue("startdate");
		if (startdate == null) {
			startdate = sdf0.format(d);
		}
		String todate = getParaValue("todate");
		if (todate == null) {
			todate = sdf0.format(d);
		}
		String maxvalue="";
		String avgvalue = "";
		String starttime = startdate + " 00:00:00";
		String totime = todate + " 23:59:59";
		String ipaddress = getParaValue("ip");
		String name = getParaValue("flag");
		String nowvalue = request.getParameter("totallength");
		String flag = (String) session.getAttribute("ipaddress");
		String flag_name = (String) session.getAttribute("flag");
		if(flag != null && ipaddress == null){
			ipaddress = flag;
			request.getSession().removeAttribute("ipaddress");
		}
		if(flag_name != null && name == null){
			name = flag_name;
			request.getSession().removeAttribute("flag");
		}
		// �������־ȡ���˿����¼�¼���б�
		String orderflag = "ipaddress";
		if (getParaValue("orderflag") != null && !getParaValue("orderflag").equals("")) {
			orderflag = getParaValue("orderflag");
		}
 
		List orderList = new ArrayList();
		if(ipaddress != null && name != null){
			    session.setAttribute("ipaddress", ipaddress);
			    session.setAttribute("flag", name);
				Hashtable pinghash = new Hashtable();
				try {
					pinghash = this.getEMCCategory(ipaddress, "idleticks",name,  starttime, totime,"emcdiskper","serialnumber");
					maxvalue = this.emcFindMaxValueOther(ipaddress, "idleticks", name, starttime, totime,"emcdiskper","serialnumber");
					avgvalue = this.emcFindAvgValueOther(ipaddress, "idleticks", name, starttime, totime,"emcdiskper","serialnumber");
				} catch (Exception e) {
					e.printStackTrace();
				}
				Hashtable ipmemhash = new Hashtable();
				ipmemhash.put("pinghash", pinghash);
				ipmemhash.put("ipaddress",ipaddress);
				orderList.add(ipmemhash);
		}
		String pingChartDivStr = ReportHelper.getChartDivStr(orderList, "ping");
		ReportValue pingReportValue = ReportHelper.getReportValue(orderList, "ping");
		String pingpath = new ReportExport().makeJfreeChartData(pingReportValue.getListValue(), pingReportValue.getIpList(), "emc���̿���״̬ʱ����", "", "");
        System.out.println(pingReportValue.getListValue().size());
		request.setAttribute("pingChartDivStr", pingChartDivStr);
		request.setAttribute("starttime", starttime);
		request.setAttribute("totime", totime);
		session.setAttribute("pingpath", pingpath);
		session.setAttribute("orderList", orderList);
		request.setAttribute("startdate", startdate);
		request.setAttribute("todate", todate);
		request.setAttribute("name", name);
		request.setAttribute("ip", ipaddress.replace("_", "."));
		request.setAttribute("maxvalue", maxvalue);
		request.setAttribute("avgvalue", avgvalue);
		request.setAttribute("nowvalue", nowvalue);
		return "/capreport/net/emcDiskFree.jsp";
	}
	
	private String emcdiskbus() {
		Date d = new Date();
		String startdate = getParaValue("startdate");
		if (startdate == null) {
			startdate = sdf0.format(d);
		}
		String todate = getParaValue("todate");
		if (todate == null) {
			todate = sdf0.format(d);
		}
		String maxvalue="";
		String avgvalue = "";
		String starttime = startdate + " 00:00:00";
		String totime = todate + " 23:59:59";
		String ipaddress = getParaValue("ip");
		String name = getParaValue("flag");
		String nowvalue = request.getParameter("totallength");
		String flag = (String) session.getAttribute("ipaddress");
		String flag_name = (String) session.getAttribute("flag");
		if(flag != null && ipaddress == null){
			ipaddress = flag;
			request.getSession().removeAttribute("ipaddress");
		}
		if(flag_name != null && name == null){
			name = flag_name;
			request.getSession().removeAttribute("flag");
		}
		// �������־ȡ���˿����¼�¼���б�
		String orderflag = "ipaddress";
		if (getParaValue("orderflag") != null && !getParaValue("orderflag").equals("")) {
			orderflag = getParaValue("orderflag");
		}
 
		List orderList = new ArrayList();
		if(ipaddress != null && name != null){
			    session.setAttribute("ipaddress", ipaddress);
			    session.setAttribute("flag", name);
				Hashtable pinghash = new Hashtable();
				try {
					pinghash = this.getEMCCategory(ipaddress, "busyticks",name,  starttime, totime,"emcdiskper","serialnumber");
					maxvalue = this.emcFindMaxValueOther(ipaddress, "busyticks", name, starttime, totime,"emcdiskper","serialnumber");
					avgvalue = this.emcFindAvgValueOther(ipaddress, "busyticks", name, starttime, totime,"emcdiskper","serialnumber");
				} catch (Exception e) {
					e.printStackTrace();
				}
				Hashtable ipmemhash = new Hashtable();
				ipmemhash.put("pinghash", pinghash);
				ipmemhash.put("ipaddress",ipaddress);
				orderList.add(ipmemhash);
		}
		String pingChartDivStr = ReportHelper.getChartDivStr(orderList, "ping");
		ReportValue pingReportValue = ReportHelper.getReportValue(orderList, "ping");
		String pingpath = new ReportExport().makeJfreeChartData(pingReportValue.getListValue(), pingReportValue.getIpList(), "emc����æ״̬ʱ����", "", "");
        System.out.println(pingReportValue.getListValue().size());
		request.setAttribute("pingChartDivStr", pingChartDivStr);
		request.setAttribute("starttime", starttime);
		request.setAttribute("totime", totime);
		session.setAttribute("pingpath", pingpath);
		session.setAttribute("orderList", orderList);
		request.setAttribute("startdate", startdate);
		request.setAttribute("todate", todate);
		request.setAttribute("name", name);
		request.setAttribute("ip", ipaddress.replace("_", "."));
		request.setAttribute("maxvalue", maxvalue);
		request.setAttribute("avgvalue", avgvalue);
		request.setAttribute("nowvalue", nowvalue);
		return "/capreport/net/emcDiskBus.jsp";
	}
	
	private String emcdiskhardread() {
		Date d = new Date();
		String startdate = getParaValue("startdate");
		if (startdate == null) {
			startdate = sdf0.format(d);
		}
		String todate = getParaValue("todate");
		if (todate == null) {
			todate = sdf0.format(d);
		}
		String maxvalue="";
		String avgvalue = "";
		String starttime = startdate + " 00:00:00";
		String totime = todate + " 23:59:59";
		String ipaddress = getParaValue("ip");
		String name = getParaValue("flag");
		String nowvalue = request.getParameter("totallength");
		String flag = (String) session.getAttribute("ipaddress");
		String flag_name = (String) session.getAttribute("flag");
		if(flag != null && ipaddress == null){
			ipaddress = flag;
			request.getSession().removeAttribute("ipaddress");
		}
		if(flag_name != null && name == null){
			name = flag_name;
			request.getSession().removeAttribute("flag");
		}
		// �������־ȡ���˿����¼�¼���б�
		String orderflag = "ipaddress";
		if (getParaValue("orderflag") != null && !getParaValue("orderflag").equals("")) {
			orderflag = getParaValue("orderflag");
		}
 
		List orderList = new ArrayList();
		if(ipaddress != null && name != null){
			    session.setAttribute("ipaddress", ipaddress);
			    session.setAttribute("flag", name);
				Hashtable pinghash = new Hashtable();
				try {
					pinghash = this.getEMCCategory(ipaddress, "hardreaderrors",name,  starttime, totime,"emcdiskper","serialnumber");
					maxvalue = this.emcFindMaxValueOther(ipaddress, "hardreaderrors", name, starttime, totime,"emcdiskper","serialnumber");
					avgvalue = this.emcFindAvgValueOther(ipaddress, "hardreaderrors", name, starttime, totime,"emcdiskper","serialnumber");
				} catch (Exception e) {
					e.printStackTrace();
				}
				Hashtable ipmemhash = new Hashtable();
				ipmemhash.put("pinghash", pinghash);
				ipmemhash.put("ipaddress",ipaddress);
				orderList.add(ipmemhash);
		}
		String pingChartDivStr = ReportHelper.getChartDivStr(orderList, "ping");
		ReportValue pingReportValue = ReportHelper.getReportValue(orderList, "ping");
		String pingpath = new ReportExport().makeJfreeChartData(pingReportValue.getListValue(), pingReportValue.getIpList(), "emc ����Ӳ����������", "", "");
        System.out.println(pingReportValue.getListValue().size());
		request.setAttribute("pingChartDivStr", pingChartDivStr);
		request.setAttribute("starttime", starttime);
		request.setAttribute("totime", totime);
		session.setAttribute("pingpath", pingpath);
		session.setAttribute("orderList", orderList);
		request.setAttribute("startdate", startdate);
		request.setAttribute("todate", todate);
		request.setAttribute("name", name);
		request.setAttribute("ip", ipaddress.replace("_", "."));
		request.setAttribute("maxvalue", maxvalue);
		request.setAttribute("avgvalue", avgvalue);
		request.setAttribute("nowvalue", nowvalue);
		return "/capreport/net/emcDiskHardRead.jsp";
	}
	
	
	private String emcdiskhardwrite() {
		Date d = new Date();
		String startdate = getParaValue("startdate");
		if (startdate == null) {
			startdate = sdf0.format(d);
		}
		String todate = getParaValue("todate");
		if (todate == null) {
			todate = sdf0.format(d);
		}
		String maxvalue="";
		String avgvalue = "";
		String starttime = startdate + " 00:00:00";
		String totime = todate + " 23:59:59";
		String ipaddress = getParaValue("ip");
		String name = getParaValue("flag");
		String nowvalue = request.getParameter("totallength");
		String flag = (String) session.getAttribute("ipaddress");
		String flag_name = (String) session.getAttribute("flag");
		if(flag != null && ipaddress == null){
			ipaddress = flag;
			request.getSession().removeAttribute("ipaddress");
		}
		if(flag_name != null && name == null){
			name = flag_name;
			request.getSession().removeAttribute("flag");
		}
		// �������־ȡ���˿����¼�¼���б�
		String orderflag = "ipaddress";
		if (getParaValue("orderflag") != null && !getParaValue("orderflag").equals("")) {
			orderflag = getParaValue("orderflag");
		}
 
		List orderList = new ArrayList();
		if(ipaddress != null && name != null){
			    session.setAttribute("ipaddress", ipaddress);
			    session.setAttribute("flag", name);
				Hashtable pinghash = new Hashtable();
				try {
					pinghash = this.getEMCCategory(ipaddress, "hardwritererrors",name,  starttime, totime,"emcdiskper","serialnumber");
					maxvalue = this.emcFindMaxValueOther(ipaddress, "hardwritererrors", name, starttime, totime,"emcdiskper","serialnumber");
					avgvalue = this.emcFindAvgValueOther(ipaddress, "hardwritererrors", name, starttime, totime,"emcdiskper","serialnumber");
				} catch (Exception e) {
					e.printStackTrace();
				}
				Hashtable ipmemhash = new Hashtable();
				ipmemhash.put("pinghash", pinghash);
				ipmemhash.put("ipaddress",ipaddress);
				orderList.add(ipmemhash);
		}
		String pingChartDivStr = ReportHelper.getChartDivStr(orderList, "ping");
		ReportValue pingReportValue = ReportHelper.getReportValue(orderList, "ping");
		String pingpath = new ReportExport().makeJfreeChartData(pingReportValue.getListValue(), pingReportValue.getIpList(), "emc ����Ӳ��д������", "", "");
        System.out.println(pingReportValue.getListValue().size());
		request.setAttribute("pingChartDivStr", pingChartDivStr);
		request.setAttribute("starttime", starttime);
		request.setAttribute("totime", totime);
		session.setAttribute("pingpath", pingpath);
		session.setAttribute("orderList", orderList);
		request.setAttribute("startdate", startdate);
		request.setAttribute("todate", todate);
		request.setAttribute("name", name);
		request.setAttribute("ip", ipaddress.replace("_", "."));
		request.setAttribute("maxvalue", maxvalue);
		request.setAttribute("avgvalue", avgvalue);
		request.setAttribute("nowvalue", nowvalue);
		return "/capreport/net/emcDiskHardWrite.jsp";
	}
	
	private String emcdiskwrite() {
		Date d = new Date();
		String startdate = getParaValue("startdate");
		if (startdate == null) {
			startdate = sdf0.format(d);
		}
		String todate = getParaValue("todate");
		if (todate == null) {
			todate = sdf0.format(d);
		}
		String maxvalue="";
		String avgvalue = "";
		String starttime = startdate + " 00:00:00";
		String totime = todate + " 23:59:59";
		String ipaddress = getParaValue("ip");
		String name = getParaValue("flag");
		String nowvalue = request.getParameter("totallength");
		String flag = (String) session.getAttribute("ipaddress");
		String flag_name = (String) session.getAttribute("flag");
		if(flag != null && ipaddress == null){
			ipaddress = flag;
			request.getSession().removeAttribute("ipaddress");
		}
		if(flag_name != null && name == null){
			name = flag_name;
			request.getSession().removeAttribute("flag");
		}
		// �������־ȡ���˿����¼�¼���б�
		String orderflag = "ipaddress";
		if (getParaValue("orderflag") != null && !getParaValue("orderflag").equals("")) {
			orderflag = getParaValue("orderflag");
		}
 
		List orderList = new ArrayList();
		if(ipaddress != null && name != null){
			    session.setAttribute("ipaddress", ipaddress);
			    session.setAttribute("flag", name);
				Hashtable pinghash = new Hashtable();
				try {
					pinghash = this.getEMCCategory(ipaddress, "numberofwrites",name,  starttime, totime,"emcdiskper","serialnumber");
					maxvalue = this.emcFindMaxValueOther(ipaddress, "numberofwrites", name, starttime, totime,"emcdiskper","serialnumber");
					avgvalue = this.emcFindAvgValueOther(ipaddress, "numberofwrites", name, starttime, totime,"emcdiskper","serialnumber");
				} catch (Exception e) {
					e.printStackTrace();
				}
				Hashtable ipmemhash = new Hashtable();
				ipmemhash.put("pinghash", pinghash);
				ipmemhash.put("ipaddress",ipaddress);
				orderList.add(ipmemhash);
		}
		String pingChartDivStr = ReportHelper.getChartDivStr(orderList, "ping");
		ReportValue pingReportValue = ReportHelper.getReportValue(orderList, "ping");
		String pingpath = new ReportExport().makeJfreeChartData(pingReportValue.getListValue(), pingReportValue.getIpList(), "emc ����д������", "", "");
        System.out.println(pingReportValue.getListValue().size());
		request.setAttribute("pingChartDivStr", pingChartDivStr);
		request.setAttribute("starttime", starttime);
		request.setAttribute("totime", totime);
		session.setAttribute("pingpath", pingpath);
		session.setAttribute("orderList", orderList);
		request.setAttribute("startdate", startdate);
		request.setAttribute("todate", todate);
		request.setAttribute("name", name);
		request.setAttribute("ip", ipaddress.replace("_", "."));
		request.setAttribute("maxvalue", maxvalue);
		request.setAttribute("avgvalue", avgvalue);
		request.setAttribute("nowvalue", nowvalue);
		return "/capreport/net/emcDiskWrite.jsp";
	}
	
	private String emcenvarraywt() {
		Date d = new Date();
		String startdate = getParaValue("startdate");
		if (startdate == null) {
			startdate = sdf0.format(d);
		}
		String todate = getParaValue("todate");
		if (todate == null) {
			todate = sdf0.format(d);
		}
		String maxvalue="";
		String avgvalue = "";
		String starttime = startdate + " 00:00:00";
		String totime = todate + " 23:59:59";
		String ipaddress = getParaValue("ip");
//		String name = getParaValue("flag");
//		String nowvalue = request.getParameter("totallength");
		String flag = (String) session.getAttribute("ipaddress");
//		String flag_name = (String) session.getAttribute("flag");
		if(flag != null && ipaddress == null){
			ipaddress = flag;
			request.getSession().removeAttribute("ipaddress");
		}
//		if(flag_name != null && name == null){
//			name = flag_name;
//			request.getSession().removeAttribute("flag");
//		}
		// �������־ȡ���˿����¼�¼���б�
		String orderflag = "ipaddress";
		if (getParaValue("orderflag") != null && !getParaValue("orderflag").equals("")) {
			orderflag = getParaValue("orderflag");
		}
 
		List orderList = new ArrayList();
		if(ipaddress != null){
			    session.setAttribute("ipaddress", ipaddress);
//			    session.setAttribute("flag", name);
				Hashtable pinghash = new Hashtable();
				try {
					pinghash = this.getEMCCategory(ipaddress, "presentwatts","",  starttime, totime,"emcenvpower","null");
					maxvalue = this.emcFindMaxValueOther(ipaddress, "presentwatts", "", starttime, totime,"emcenvpower","null");
					avgvalue = this.emcFindAvgValueOther(ipaddress, "presentwatts", "", starttime, totime,"emcenvpower","null");
				} catch (Exception e) {
					e.printStackTrace();
				}
				Hashtable ipmemhash = new Hashtable();
				ipmemhash.put("pinghash", pinghash);
				ipmemhash.put("ipaddress",ipaddress);
				orderList.add(ipmemhash);
		}
		String pingChartDivStr = ReportHelper.getChartDivStr(orderList, "ping");
		ReportValue pingReportValue = ReportHelper.getReportValue(orderList, "ping");
		String pingpath = new ReportExport().makeJfreeChartData(pingReportValue.getListValue(), pingReportValue.getIpList(), "emc ���е�Դ��ǰ����", "", "");
        System.out.println(pingReportValue.getListValue().size());
		request.setAttribute("pingChartDivStr", pingChartDivStr);
		request.setAttribute("starttime", starttime);
		request.setAttribute("totime", totime);
		session.setAttribute("pingpath", pingpath);
		session.setAttribute("orderList", orderList);
		request.setAttribute("startdate", startdate);
		request.setAttribute("todate", todate);
//		request.setAttribute("name", name);
		request.setAttribute("ip", ipaddress.replace("_", "."));
		request.setAttribute("maxvalue", maxvalue);
		request.setAttribute("avgvalue", avgvalue);
//		request.setAttribute("nowvalue", nowvalue);
		return "/capreport/net/emcEnvArraywt.jsp";
	}
	
	
	
	private String emcenvarrayavgwt() {
		Date d = new Date();
		String startdate = getParaValue("startdate");
		if (startdate == null) {
			startdate = sdf0.format(d);
		}
		String todate = getParaValue("todate");
		if (todate == null) {
			todate = sdf0.format(d);
		}
		String maxvalue="";
		String avgvalue = "";
		String starttime = startdate + " 00:00:00";
		String totime = todate + " 23:59:59";
		String ipaddress = getParaValue("ip");
//		String name = getParaValue("flag");
//		String nowvalue = request.getParameter("totallength");
		String flag = (String) session.getAttribute("ipaddress");
//		String flag_name = (String) session.getAttribute("flag");
		if(flag != null && ipaddress == null){
			ipaddress = flag;
			request.getSession().removeAttribute("ipaddress");
		}
//		if(flag_name != null && name == null){
//			name = flag_name;
//			request.getSession().removeAttribute("flag");
//		}
		// �������־ȡ���˿����¼�¼���б�
		String orderflag = "ipaddress";
		if (getParaValue("orderflag") != null && !getParaValue("orderflag").equals("")) {
			orderflag = getParaValue("orderflag");
		}
 
		List orderList = new ArrayList();
		if(ipaddress != null){
			    session.setAttribute("ipaddress", ipaddress);
//			    session.setAttribute("flag", name);
				Hashtable pinghash = new Hashtable();
				try {
					pinghash = this.getEMCCategory(ipaddress, "averagewatts","",  starttime, totime,"emcenvpower","null");
					maxvalue = this.emcFindMaxValueOther(ipaddress, "averagewatts", "", starttime, totime,"emcenvpower","null");
					avgvalue = this.emcFindAvgValueOther(ipaddress, "averagewatts", "", starttime, totime,"emcenvpower","null");
				} catch (Exception e) {
					e.printStackTrace();
				}
				Hashtable ipmemhash = new Hashtable();
				ipmemhash.put("pinghash", pinghash);
				ipmemhash.put("ipaddress",ipaddress);
				orderList.add(ipmemhash);
		}
		String pingChartDivStr = ReportHelper.getChartDivStr(orderList, "ping");
		ReportValue pingReportValue = ReportHelper.getReportValue(orderList, "ping");
		String pingpath = new ReportExport().makeJfreeChartData(pingReportValue.getListValue(), pingReportValue.getIpList(), "emc ���е�Դƽ������", "", "");
        System.out.println(pingReportValue.getListValue().size());
		request.setAttribute("pingChartDivStr", pingChartDivStr);
		request.setAttribute("starttime", starttime);
		request.setAttribute("totime", totime);
		session.setAttribute("pingpath", pingpath);
		session.setAttribute("orderList", orderList);
		request.setAttribute("startdate", startdate);
		request.setAttribute("todate", todate);
//		request.setAttribute("name", name);
		request.setAttribute("ip", ipaddress.replace("_", "."));
		request.setAttribute("maxvalue", maxvalue);
		request.setAttribute("avgvalue", avgvalue);
//		request.setAttribute("nowvalue", nowvalue);
		return "/capreport/net/emcEnvArrayAvgwt.jsp";
	}
	
	
	private String emcenvmemwt() {
		Date d = new Date();
		String startdate = getParaValue("startdate");
		if (startdate == null) {
			startdate = sdf0.format(d);
		}
		String todate = getParaValue("todate");
		if (todate == null) {
			todate = sdf0.format(d);
		}
		String maxvalue="";
		String avgvalue = "";
		String starttime = startdate + " 00:00:00";
		String totime = todate + " 23:59:59";
		String ipaddress = getParaValue("ip");
		String name = getParaValue("flag");
		String nowvalue = request.getParameter("totallength");
		String flag = (String) session.getAttribute("ipaddress");
		String flag_name = (String) session.getAttribute("flag");
		if(flag != null && ipaddress == null){
			ipaddress = flag;
			request.getSession().removeAttribute("ipaddress");
		}
		if(flag_name != null && name == null){
			name = flag_name;
			request.getSession().removeAttribute("flag");
		}
		// �������־ȡ���˿����¼�¼���б�
		String orderflag = "ipaddress";
		if (getParaValue("orderflag") != null && !getParaValue("orderflag").equals("")) {
			orderflag = getParaValue("orderflag");
		}
 
		List orderList = new ArrayList();
		if(ipaddress != null && name != null){
			    session.setAttribute("ipaddress", ipaddress);
			    session.setAttribute("flag", name);
				Hashtable pinghash = new Hashtable();
				try {
					pinghash = this.getEMCCategory(ipaddress, "PresentWatts",name,  starttime, totime,"emcenvstore","name");
					maxvalue = this.emcFindMaxValueOther(ipaddress, "PresentWatts", name, starttime, totime,"emcenvstore","name");
					avgvalue = this.emcFindAvgValueOther(ipaddress, "PresentWatts", name, starttime, totime,"emcenvstore","name");
				} catch (Exception e) {
					e.printStackTrace();
				}
				Hashtable ipmemhash = new Hashtable();
				ipmemhash.put("pinghash", pinghash);
				ipmemhash.put("ipaddress",ipaddress);
				orderList.add(ipmemhash);
		}
		String pingChartDivStr = ReportHelper.getChartDivStr(orderList, "ping");
		ReportValue pingReportValue = ReportHelper.getReportValue(orderList, "ping");
		String pingpath = new ReportExport().makeJfreeChartData(pingReportValue.getListValue(), pingReportValue.getIpList(), "emc �洢ģ���Դ��ǰƽ������", "", "");
        System.out.println(pingReportValue.getListValue().size());
		request.setAttribute("pingChartDivStr", pingChartDivStr);
		request.setAttribute("starttime", starttime);
		request.setAttribute("totime", totime);
		session.setAttribute("pingpath", pingpath);
		session.setAttribute("orderList", orderList);
		request.setAttribute("startdate", startdate);
		request.setAttribute("todate", todate);
		request.setAttribute("name", name);
		request.setAttribute("ip", ipaddress.replace("_", "."));
		request.setAttribute("maxvalue", maxvalue);
		request.setAttribute("avgvalue", avgvalue);
		request.setAttribute("nowvalue", nowvalue);
		return "/capreport/net/emcEnvMemWt.jsp";
	}
	
	
	private String emcenvmemavgwt() {
		Date d = new Date();
		String startdate = getParaValue("startdate");
		if (startdate == null) {
			startdate = sdf0.format(d);
		}
		String todate = getParaValue("todate");
		if (todate == null) {
			todate = sdf0.format(d);
		}
		String maxvalue="";
		String avgvalue = "";
		String starttime = startdate + " 00:00:00";
		String totime = todate + " 23:59:59";
		String ipaddress = getParaValue("ip");
		String name = getParaValue("flag");
		String nowvalue = request.getParameter("totallength");
		String flag = (String) session.getAttribute("ipaddress");
		String flag_name = (String) session.getAttribute("flag");
		if(flag != null && ipaddress == null){
			ipaddress = flag;
			request.getSession().removeAttribute("ipaddress");
		}
		if(flag_name != null && name == null){
			name = flag_name;
			request.getSession().removeAttribute("flag");
		}
		// �������־ȡ���˿����¼�¼���б�
		String orderflag = "ipaddress";
		if (getParaValue("orderflag") != null && !getParaValue("orderflag").equals("")) {
			orderflag = getParaValue("orderflag");
		}
 
		List orderList = new ArrayList();
		if(ipaddress != null && name != null){
			    session.setAttribute("ipaddress", ipaddress);
			    session.setAttribute("flag", name);
				Hashtable pinghash = new Hashtable();
				try {
					pinghash = this.getEMCCategory(ipaddress, "AverageWatts",name,  starttime, totime,"emcenvstore","name");
					maxvalue = this.emcFindMaxValueOther(ipaddress, "AverageWatts", name, starttime, totime,"emcenvstore","name");
					avgvalue = this.emcFindAvgValueOther(ipaddress, "AverageWatts", name, starttime, totime,"emcenvstore","name");
				} catch (Exception e) {
					e.printStackTrace();
				}
				Hashtable ipmemhash = new Hashtable();
				ipmemhash.put("pinghash", pinghash);
				ipmemhash.put("ipaddress",ipaddress);
				orderList.add(ipmemhash);
		}
		String pingChartDivStr = ReportHelper.getChartDivStr(orderList, "ping");
		ReportValue pingReportValue = ReportHelper.getReportValue(orderList, "ping");
		String pingpath = new ReportExport().makeJfreeChartData(pingReportValue.getListValue(), pingReportValue.getIpList(), "emc �洢ģ���Դƽ������", "", "");
        System.out.println(pingReportValue.getListValue().size());
		request.setAttribute("pingChartDivStr", pingChartDivStr);
		request.setAttribute("starttime", starttime);
		request.setAttribute("totime", totime);
		session.setAttribute("pingpath", pingpath);
		session.setAttribute("orderList", orderList);
		request.setAttribute("startdate", startdate);
		request.setAttribute("todate", todate);
		request.setAttribute("name", name);
		request.setAttribute("ip", ipaddress.replace("_", "."));
		request.setAttribute("maxvalue", maxvalue);
		request.setAttribute("avgvalue", avgvalue);
		request.setAttribute("nowvalue", nowvalue);
		return "/capreport/net/emcEnvMemAvgWt.jsp";
	}
	
	
	private String emcenvmemtmp() {
		Date d = new Date();
		String startdate = getParaValue("startdate");
		if (startdate == null) {
			startdate = sdf0.format(d);
		}
		String todate = getParaValue("todate");
		if (todate == null) {
			todate = sdf0.format(d);
		}
		String maxvalue="";
		String avgvalue = "";
		String starttime = startdate + " 00:00:00";
		String totime = todate + " 23:59:59";
		String ipaddress = getParaValue("ip");
		String name = getParaValue("flag");
		String nowvalue = request.getParameter("totallength");
		String flag = (String) session.getAttribute("ipaddress");
		String flag_name = (String) session.getAttribute("flag");
		if(flag != null && ipaddress == null){
			ipaddress = flag;
			request.getSession().removeAttribute("ipaddress");
		}
		if(flag_name != null && name == null){
			name = flag_name;
			request.getSession().removeAttribute("flag");
		}
		// �������־ȡ���˿����¼�¼���б�
		String orderflag = "ipaddress";
		if (getParaValue("orderflag") != null && !getParaValue("orderflag").equals("")) {
			orderflag = getParaValue("orderflag");
		}
 
		List orderList = new ArrayList();
		if(ipaddress != null && name != null){
			    session.setAttribute("ipaddress", ipaddress);
			    session.setAttribute("flag", name);
				Hashtable pinghash = new Hashtable();
				try {
					pinghash = this.getEMCCategory(ipaddress, "PresentDegree",name,  starttime, totime,"emcenvstore","name");
					maxvalue = this.emcFindMaxValueOther(ipaddress, "PresentDegree", name, starttime, totime,"emcenvstore","name");
					avgvalue = this.emcFindAvgValueOther(ipaddress, "PresentDegree", name, starttime, totime,"emcenvstore","name");
				} catch (Exception e) {
					e.printStackTrace();
				}
				Hashtable ipmemhash = new Hashtable();
				ipmemhash.put("pinghash", pinghash);
				ipmemhash.put("ipaddress",ipaddress);
				orderList.add(ipmemhash);
		}
		String pingChartDivStr = ReportHelper.getChartDivStr(orderList, "ping");
		ReportValue pingReportValue = ReportHelper.getReportValue(orderList, "ping");
		String pingpath = new ReportExport().makeJfreeChartData(pingReportValue.getListValue(), pingReportValue.getIpList(), "emc �洢����¶ȵ�ǰ�¶�", "", "");
        System.out.println(pingReportValue.getListValue().size());
		request.setAttribute("pingChartDivStr", pingChartDivStr);
		request.setAttribute("starttime", starttime);
		request.setAttribute("totime", totime);
		session.setAttribute("pingpath", pingpath);
		session.setAttribute("orderList", orderList);
		request.setAttribute("startdate", startdate);
		request.setAttribute("todate", todate);
		request.setAttribute("name", name);
		request.setAttribute("ip", ipaddress.replace("_", "."));
		request.setAttribute("maxvalue", maxvalue);
		request.setAttribute("avgvalue", avgvalue);
		request.setAttribute("nowvalue", nowvalue);
		return "/capreport/net/emcEnvMemTmp.jsp";
	}
	
	
	private String emcenvmemavgtmp() {
		Date d = new Date();
		String startdate = getParaValue("startdate");
		if (startdate == null) {
			startdate = sdf0.format(d);
		}
		String todate = getParaValue("todate");
		if (todate == null) {
			todate = sdf0.format(d);
		}
		String maxvalue="";
		String avgvalue = "";
		String starttime = startdate + " 00:00:00";
		String totime = todate + " 23:59:59";
		String ipaddress = getParaValue("ip");
		String name = getParaValue("flag");
		String nowvalue = request.getParameter("totallength");
		String flag = (String) session.getAttribute("ipaddress");
		String flag_name = (String) session.getAttribute("flag");
		if(flag != null && ipaddress == null){
			ipaddress = flag;
			request.getSession().removeAttribute("ipaddress");
		}
		if(flag_name != null && name == null){
			name = flag_name;
			request.getSession().removeAttribute("flag");
		}
		// �������־ȡ���˿����¼�¼���б�
		String orderflag = "ipaddress";
		if (getParaValue("orderflag") != null && !getParaValue("orderflag").equals("")) {
			orderflag = getParaValue("orderflag");
		}
 
		List orderList = new ArrayList();
		if(ipaddress != null && name != null){
			    session.setAttribute("ipaddress", ipaddress);
			    session.setAttribute("flag", name);
				Hashtable pinghash = new Hashtable();
				try {
					pinghash = this.getEMCCategory(ipaddress, "AverageDegree",name,  starttime, totime,"emcenvstore","name");
					maxvalue = this.emcFindMaxValueOther(ipaddress, "AverageDegree", name, starttime, totime,"emcenvstore","name");
					avgvalue = this.emcFindAvgValueOther(ipaddress, "AverageDegree", name, starttime, totime,"emcenvstore","name");
				} catch (Exception e) {
					e.printStackTrace();
				}
				Hashtable ipmemhash = new Hashtable();
				ipmemhash.put("pinghash", pinghash);
				ipmemhash.put("ipaddress",ipaddress);
				orderList.add(ipmemhash);
		}
		String pingChartDivStr = ReportHelper.getChartDivStr(orderList, "ping");
		ReportValue pingReportValue = ReportHelper.getReportValue(orderList, "ping");
		String pingpath = new ReportExport().makeJfreeChartData(pingReportValue.getListValue(), pingReportValue.getIpList(), "emc �洢����¶�ƽ���¶�", "", "");
        System.out.println(pingReportValue.getListValue().size());
		request.setAttribute("pingChartDivStr", pingChartDivStr);
		request.setAttribute("starttime", starttime);
		request.setAttribute("totime", totime);
		session.setAttribute("pingpath", pingpath);
		session.setAttribute("orderList", orderList);
		request.setAttribute("startdate", startdate);
		request.setAttribute("todate", todate);
		request.setAttribute("name", name);
		request.setAttribute("ip", ipaddress.replace("_", "."));
		request.setAttribute("maxvalue", maxvalue);
		request.setAttribute("avgvalue", avgvalue);
		request.setAttribute("nowvalue", nowvalue);
		return "/capreport/net/emcEnvMemAvgTmp.jsp";
	}
	
	
	private String emcenvbakwt() {
		Date d = new Date();
		String startdate = getParaValue("startdate");
		if (startdate == null) {
			startdate = sdf0.format(d);
		}
		String todate = getParaValue("todate");
		if (todate == null) {
			todate = sdf0.format(d);
		}
		String maxvalue="";
		String avgvalue = "";
		String starttime = startdate + " 00:00:00";
		String totime = todate + " 23:59:59";
		String ipaddress = getParaValue("ip");
		String name = getParaValue("flag");
		String nowvalue = request.getParameter("totallength");
		String flag = (String) session.getAttribute("ipaddress");
		String flag_name = (String) session.getAttribute("flag");
		if(flag != null && ipaddress == null){
			ipaddress = flag;
			request.getSession().removeAttribute("ipaddress");
		}
		if(flag_name != null && name == null){
			name = flag_name;
			request.getSession().removeAttribute("flag");
		}
		// �������־ȡ���˿����¼�¼���б�
		String orderflag = "ipaddress";
		if (getParaValue("orderflag") != null && !getParaValue("orderflag").equals("")) {
			orderflag = getParaValue("orderflag");
		}
 
		List orderList = new ArrayList();
		if(ipaddress != null && name != null){
			    session.setAttribute("ipaddress", ipaddress);
			    session.setAttribute("flag", name);
				Hashtable pinghash = new Hashtable();
				try {
					pinghash = this.getEMCCategory(ipaddress, "PresentWatts",name,  starttime, totime,"emcbakpower","name");
					maxvalue = this.emcFindMaxValueOther(ipaddress, "PresentWatts", name, starttime, totime,"emcbakpower","name");
					avgvalue = this.emcFindAvgValueOther(ipaddress, "PresentWatts", name, starttime, totime,"emcbakpower","name");
				} catch (Exception e) {
					e.printStackTrace();
				}
				Hashtable ipmemhash = new Hashtable();
				ipmemhash.put("pinghash", pinghash);
				ipmemhash.put("ipaddress",ipaddress);
				orderList.add(ipmemhash);
		}
		String pingChartDivStr = ReportHelper.getChartDivStr(orderList, "ping");
		ReportValue pingReportValue = ReportHelper.getReportValue(orderList, "ping");
		String pingpath = new ReportExport().makeJfreeChartData(pingReportValue.getListValue(), pingReportValue.getIpList(), "emc sps���õ�Դ��ǰ����", "", "");
        System.out.println(pingReportValue.getListValue().size());
		request.setAttribute("pingChartDivStr", pingChartDivStr);
		request.setAttribute("starttime", starttime);
		request.setAttribute("totime", totime);
		session.setAttribute("pingpath", pingpath);
		session.setAttribute("orderList", orderList);
		request.setAttribute("startdate", startdate);
		request.setAttribute("todate", todate);
		request.setAttribute("name", name);
		request.setAttribute("ip", ipaddress.replace("_", "."));
		request.setAttribute("maxvalue", maxvalue);
		request.setAttribute("avgvalue", avgvalue);
		request.setAttribute("nowvalue", nowvalue);
		return "/capreport/net/emcEnvBakWt.jsp";
	}
	
	
	private String emcenvbakavgwt() {
		Date d = new Date();
		String startdate = getParaValue("startdate");
		if (startdate == null) {
			startdate = sdf0.format(d);
		}
		String todate = getParaValue("todate");
		if (todate == null) {
			todate = sdf0.format(d);
		}
		String maxvalue="";
		String avgvalue = "";
		String starttime = startdate + " 00:00:00";
		String totime = todate + " 23:59:59";
		String ipaddress = getParaValue("ip");
		String name = getParaValue("flag");
		String nowvalue = request.getParameter("totallength");
		String flag = (String) session.getAttribute("ipaddress");
		String flag_name = (String) session.getAttribute("flag");
		if(flag != null && ipaddress == null){
			ipaddress = flag;
			request.getSession().removeAttribute("ipaddress");
		}
		if(flag_name != null && name == null){
			name = flag_name;
			request.getSession().removeAttribute("flag");
		}
		// �������־ȡ���˿����¼�¼���б�
		String orderflag = "ipaddress";
		if (getParaValue("orderflag") != null && !getParaValue("orderflag").equals("")) {
			orderflag = getParaValue("orderflag");
		}
 
		List orderList = new ArrayList();
		if(ipaddress != null && name != null){
			    session.setAttribute("ipaddress", ipaddress);
			    session.setAttribute("flag", name);
				Hashtable pinghash = new Hashtable();
				try {
					pinghash = this.getEMCCategory(ipaddress, "AverageWatts",name,  starttime, totime,"emcbakpower","name");
					maxvalue = this.emcFindMaxValueOther(ipaddress, "AverageWatts", name, starttime, totime,"emcbakpower","name");
					avgvalue = this.emcFindAvgValueOther(ipaddress, "AverageWatts", name, starttime, totime,"emcbakpower","name");
				} catch (Exception e) {
					e.printStackTrace();
				}
				Hashtable ipmemhash = new Hashtable();
				ipmemhash.put("pinghash", pinghash);
				ipmemhash.put("ipaddress",ipaddress);
				orderList.add(ipmemhash);
		}
		String pingChartDivStr = ReportHelper.getChartDivStr(orderList, "ping");
		ReportValue pingReportValue = ReportHelper.getReportValue(orderList, "ping");
		String pingpath = new ReportExport().makeJfreeChartData(pingReportValue.getListValue(), pingReportValue.getIpList(), "emc sps���õ�Դƽ������", "", "");
        System.out.println(pingReportValue.getListValue().size());
		request.setAttribute("pingChartDivStr", pingChartDivStr);
		request.setAttribute("starttime", starttime);
		request.setAttribute("totime", totime);
		session.setAttribute("pingpath", pingpath);
		session.setAttribute("orderList", orderList);
		request.setAttribute("startdate", startdate);
		request.setAttribute("todate", todate);
		request.setAttribute("name", name);
		request.setAttribute("ip", ipaddress.replace("_", "."));
		request.setAttribute("maxvalue", maxvalue);
		request.setAttribute("avgvalue", avgvalue);
		request.setAttribute("nowvalue", nowvalue);
		return "/capreport/net/emcEnvBakAvgWt.jsp";
	}
	
	
	//EMc�洢  ����
	
	public String createdocEMC() {
		String file = "";// ���浽��Ŀ�ļ����µ�ָ���ļ���
		String flag = request.getParameter("flag");
		String nowvalue = request.getParameter("nowvalue");
		String avgvalue = request.getParameter("avgvalue");
		String maxvalue = request.getParameter("maxvalue");
		if(flag.equalsIgnoreCase("arraywt")){
			file = "/temp/arraywt.doc";
		}else if(flag.equalsIgnoreCase("arrayavgwt")){
			file = "/temp/arrayavgwt.doc";
		}else if(flag.equalsIgnoreCase("memwt")){
			file = "/temp/memwt.doc";
		}else if(flag.equalsIgnoreCase("memavgwt")){
			file = "/temp/memavgwt.doc";
		}else if(flag.equalsIgnoreCase("memtmp")){
			file = "/temp/memtmp.doc";
		}else if(flag.equalsIgnoreCase("memavgtmp")){
			file = "/temp/memavgtmp.doc";
		}else if(flag.equalsIgnoreCase("bakwt")){
			file = "/temp/bakwt.doc";
		}else if(flag.equalsIgnoreCase("bakavgwt")){
			file = "/temp/bakavgwt.doc";
		}else if(flag.equalsIgnoreCase("diskread")){
			file = "/temp/diskread.doc";
		}else if(flag.equalsIgnoreCase("diskwrite")){
			file = "/temp/memCR.doc";
		}else if(flag.equalsIgnoreCase("disksoftreaderror")){
			file = "/temp/disksoftreaderror.doc";
		}else if(flag.equalsIgnoreCase("disksoftwriteerror")){
			file = "/temp/disksoftwriteerror.doc";
		}else if(flag.equalsIgnoreCase("diskwritekb")){
			file = "/temp/diskwritekb.doc";
		}else if(flag.equalsIgnoreCase("diskreadkb")){
			file = "/temp/diskreadkb.doc";
		}else if(flag.equalsIgnoreCase("diskfree")){
			file = "/temp/diskfree.doc";
		}else if(flag.equalsIgnoreCase("diskbus")){
			file = "/temp/diskbus.doc";
		}else if(flag.equalsIgnoreCase("diskhardwriteerror")){
			file = "/temp/diskhardwriteerror.doc";
		}else if(flag.equalsIgnoreCase("diskhardreaderror")){
			file = "/temp/diskhardreaderror.doc";
		}else if(flag.equalsIgnoreCase("lunharderror")){
			file = "/temp/lunharderror.doc";
		}else if(flag.equalsIgnoreCase("lunsofterror")){
			file = "/temp/lunsofterror.doc";
		}else if(flag.equalsIgnoreCase("lunlength")){
			file = "/temp/lunlength.doc";
		}
		String fileName = ResourceCenter.getInstance().getSysPath() + file;// ��ȡϵͳ�ļ���·��
		try {
			createDocContextEMC(fileName,flag,nowvalue,avgvalue,maxvalue);
//			request.getSession().removeAttribute("pingpath");
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		request.setAttribute("filename", fileName);
		return "/capreport/net/download.jsp";
	}
	
	public void createDocContextEMC(String file,String flag,String nowvalue,String avgvalue,String maxvalue) throws DocumentException, IOException {
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
		
		Paragraph title = null;
		if(flag.equalsIgnoreCase("arraywt")){
			title = new Paragraph("���е�Դ��ǰ���ʱ���", titleFont);
		}else if(flag.equalsIgnoreCase("arrayavgwt")){
			title = new Paragraph("���е�Դƽ�����ʱ���", titleFont);
		}else if(flag.equalsIgnoreCase("memwt")){
			title = new Paragraph("�洢ģ���Դ��ǰ���ʱ���", titleFont);
		}else if(flag.equalsIgnoreCase("memavgwt")){
			title = new Paragraph("�洢ģ���Դƽ�����ʱ���", titleFont);
		}else if(flag.equalsIgnoreCase("memtmp")){
			title = new Paragraph("�洢����¶ȵ�ǰ�¶ȱ���", titleFont);
		}else if(flag.equalsIgnoreCase("memavgtmp")){
			title = new Paragraph("�洢����¶�ƽ���¶ȱ���", titleFont);
		}else if(flag.equalsIgnoreCase("bakwt")){
			title = new Paragraph("SPS���õ�Դ��ǰ������ʱ���", titleFont);
		}else if(flag.equalsIgnoreCase("bakavgwt")){
			title = new Paragraph("SPS���õ�Դƽ��������ʱ���", titleFont);
		}else if(flag.equalsIgnoreCase("lunharderror")){
			title = new Paragraph("Ӳ��������������", titleFont);
		}else if(flag.equalsIgnoreCase("lunsofterror")){
			title = new Paragraph("���������������", titleFont);
		}else if(flag.equalsIgnoreCase("lunlength")){
			title = new Paragraph("�����ܳ���", titleFont);
		}else if(flag.equalsIgnoreCase("diskread")){
			title = new Paragraph("������������", titleFont);
		}else if(flag.equalsIgnoreCase("diskwrite")){
			title = new Paragraph("д����������", titleFont);
		}else if(flag.equalsIgnoreCase("disksoftreaderror")){
			title = new Paragraph("���������������", titleFont);
		}else if(flag.equalsIgnoreCase("disksoftwriteerror")){
			title = new Paragraph("���д����������", titleFont);
		}else if(flag.equalsIgnoreCase("diskreadkb")){
			title = new Paragraph("��KB����", titleFont);
		}else if(flag.equalsIgnoreCase("diskwritekb")){
			title = new Paragraph("дKB����", titleFont);
		}else if(flag.equalsIgnoreCase("diskfree")){
			title = new Paragraph("��״̬ʱ��������", titleFont);
		}else if(flag.equalsIgnoreCase("diskbus")){
			title = new Paragraph("æ״̬ʱ��������", titleFont);
		}else if(flag.equalsIgnoreCase("diskhardreaderror")){
			title = new Paragraph("Ӳ��������������", titleFont);
		}else if(flag.equalsIgnoreCase("diskhardwriteerror")){
			title = new Paragraph("Ӳ��д����������", titleFont);
		}
		// ���ñ����ʽ���뷽ʽ
		title.setAlignment(Element.ALIGN_CENTER);
		// title.setFont(titleFont);
		document.add(title);
		// ���� Table ���
		Font fontChinese = new Font(bfChinese, 12, Font.NORMAL, Color.black);
//		List pinglist = (List) session.getAttribute("pinglist");
		Table aTable = new Table(4);
		int width[] = { 50, 50, 50, 50,  };
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

		String pingpath = (String) session.getAttribute("pingpath");
		Image img = Image.getInstance(pingpath);
		img.setAbsolutePosition(0, 0);
		img.setAlignment(Image.LEFT);// ����ͼƬ��ʾλ��
		document.add(img);
		
		Cell cell1 = new Cell("ָ������");
		Cell cell2 = new Cell("��ǰ");
		Cell cell3 = new Cell("ƽ��");
		Cell cell4 = new Cell("���");
		Cell cell5 = null;
		if(flag.equalsIgnoreCase("arraywt")){
			cell5 = new Cell("���е�Դ��ǰ����");
		}else if(flag.equalsIgnoreCase("arrayavgwt")){
			cell5 = new Cell("���е�Դƽ������");
		}else if(flag.equalsIgnoreCase("memwt")){
			cell5 = new Cell("�洢ģ���Դ��ǰ����");
		}else if(flag.equalsIgnoreCase("memavgwt")){
			cell5 = new Cell("�洢ģ���Դƽ������");
		}else if(flag.equalsIgnoreCase("memtmp")){
			cell5 = new Cell("�洢����¶ȵ�ǰ�¶�");
		}else if(flag.equalsIgnoreCase("memavgtmp")){
			cell5 = new Cell("SPS���õ�Դ��ǰ�������");
		}else if(flag.equalsIgnoreCase("bakwt")){
			cell5 = new Cell("SPS���õ�Դ��ǰ�������");
		}else if(flag.equalsIgnoreCase("bakavgwt")){
			cell5 = new Cell("SPS���õ�Դƽ���������");
		}else if(flag.equalsIgnoreCase("lunharderror")){
			cell5 = new Cell("Ӳ����������");
		}else if(flag.equalsIgnoreCase("lunsofterror")){
			cell5 = new Cell("�����������");
		}else if(flag.equalsIgnoreCase("lunlength")){
			cell5 = new Cell("�����ܳ���");
		}else if(flag.equalsIgnoreCase("diskread")){
			cell5 = new Cell("��������");
		}else if(flag.equalsIgnoreCase("diskwrite")){
			cell5 = new Cell("д������");
		}else if(flag.equalsIgnoreCase("disksoftreaderror")){
			cell5 = new Cell("�����������");
		}else if(flag.equalsIgnoreCase("disksoftwriteerror")){
			cell5 = new Cell("���д������");
		}else if(flag.equalsIgnoreCase("diskreadkb")){
			cell5 = new Cell("��KB");
		}else if(flag.equalsIgnoreCase("diskwritekb")){
			cell5 = new Cell("дKB");
		}else if(flag.equalsIgnoreCase("diskfree")){
			cell5 = new Cell("��״̬ʱ����");
		}else if(flag.equalsIgnoreCase("diskbus")){
			cell5 = new Cell("æ״̬ʱ����");
		}else if(flag.equalsIgnoreCase("diskhardreaderror")){
			cell5 = new Cell("Ӳ����������");
		}else if(flag.equalsIgnoreCase("diskhardwriteerror")){
			cell5 = new Cell("Ӳ��д������");
		}
		Cell cell6 = new Cell(nowvalue);
		Cell cell7 = new Cell(avgvalue);
		Cell cell8 = new Cell(maxvalue);
		cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell4.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell5.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell6.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell7.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell8.setVerticalAlignment(Element.ALIGN_MIDDLE);

		cell1.setBackgroundColor(Color.LIGHT_GRAY);
		cell2.setBackgroundColor(Color.LIGHT_GRAY);
		cell3.setBackgroundColor(Color.LIGHT_GRAY);
		cell4.setBackgroundColor(Color.LIGHT_GRAY);
		cell5.setBackgroundColor(Color.LIGHT_GRAY);
		cell6.setBackgroundColor(Color.LIGHT_GRAY);
		cell7.setBackgroundColor(Color.LIGHT_GRAY);
		cell8.setBackgroundColor(Color.LIGHT_GRAY);
		
		cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell7.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell8.setHorizontalAlignment(Element.ALIGN_CENTER);
		
		
		aTable.addCell(cell1);
		aTable.addCell(cell2);
		aTable.addCell(cell3);
		aTable.addCell(cell4);
		aTable.addCell(cell5);
		aTable.addCell(cell6);
		aTable.addCell(cell7);
		aTable.addCell(cell8);
		document.add(aTable);
		document.close();
	}
	
	public String createpdfEMC() {
		String file = "";// ���浽��Ŀ�ļ����µ�ָ���ļ���
		String flag = request.getParameter("flag");
		String nowvalue = request.getParameter("nowvalue");
		String avgvalue = request.getParameter("avgvalue");
		String maxvalue = request.getParameter("maxvalue");
		if(flag.equalsIgnoreCase("arraywt")){
			file = "/temp/arraywt.PDF";
		}else if(flag.equalsIgnoreCase("arrayavgwt")){
			file = "/temp/arrayavgwt.PDF";
		}else if(flag.equalsIgnoreCase("memwt")){
			file = "/temp/memwt.PDF";
		}else if(flag.equalsIgnoreCase("memavgwt")){
			file = "/temp/memavgwt.PDF";
		}else if(flag.equalsIgnoreCase("memtmp")){
			file = "/temp/memtmp.PDF";
		}else if(flag.equalsIgnoreCase("memavgtmp")){
			file = "/temp/memavgtmp.PDF";
		}else if(flag.equalsIgnoreCase("bakwt")){
			file = "/temp/bakwt.PDF";
		}else if(flag.equalsIgnoreCase("bakavgwt")){
			file = "/temp/bakavgwt.PDF";
		}else if(flag.equalsIgnoreCase("diskread")){
			file = "/temp/diskread.PDF";
		}else if(flag.equalsIgnoreCase("diskwrite")){
			file = "/temp/memCR.PDF";
		}else if(flag.equalsIgnoreCase("disksoftreaderror")){
			file = "/temp/disksoftreaderror.PDF";
		}else if(flag.equalsIgnoreCase("disksoftwriteerror")){
			file = "/temp/disksoftwriteerror.PDF";
		}else if(flag.equalsIgnoreCase("diskwritekb")){
			file = "/temp/diskwritekb.PDF";
		}else if(flag.equalsIgnoreCase("diskreadkb")){
			file = "/temp/diskreadkb.PDF";
		}else if(flag.equalsIgnoreCase("diskfree")){
			file = "/temp/diskfree.PDF";
		}else if(flag.equalsIgnoreCase("diskbus")){
			file = "/temp/diskbus.PDF";
		}else if(flag.equalsIgnoreCase("diskhardwriteerror")){
			file = "/temp/diskhardwriteerror.PDF";
		}else if(flag.equalsIgnoreCase("diskhardreaderror")){
			file = "/temp/diskhardreaderror.PDF";
		}else if(flag.equalsIgnoreCase("lunharderror")){
			file = "/temp/lunharderror.PDF";
		}else if(flag.equalsIgnoreCase("lunsofterror")){
			file = "/temp/lunsofterror.PDF";
		}else if(flag.equalsIgnoreCase("lunlength")){
			file = "/temp/lunlength.PDF";
		}
		String fileName = ResourceCenter.getInstance().getSysPath() + file;// ��ȡϵͳ�ļ���·��
		try {
			createPdfEMC(fileName,flag,nowvalue,avgvalue,maxvalue);
//			request.getSession().removeAttribute("pingpath");
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		request.setAttribute("filename", fileName);
		return "/capreport/net/download.jsp";
	}
	
	public void createPdfEMC(String file,String flag,String nowvalue,String avgvalue,String maxvalue) throws DocumentException, IOException {

		// com.lowagie.text.Font FontChinese = new
		// com.lowagie.text.Font(bfChinese, 12, com.lowagie.text.Font.NORMAL);

		// ����ֽ�Ŵ�С

		Document document = new Document(PageSize.A4);
		// ����һ����д��(Writer)��document���������ͨ����д��(Writer)���Խ��ĵ�д�뵽������
		PdfWriter.getInstance(document, new FileOutputStream(file));
		document.open();
		// ������������
		BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
		// ����������
		Font titleFont = new Font(bfChinese, 12, Font.BOLD);
		// com.lowagie.text.Font titleFont = new
		// com.lowagie.text.Font(bfChinese, 12, com.lowagie.text.Font.NORMAL);
		// ����������
		Font contextFont = new com.lowagie.text.Font(bfChinese, 11, Font.NORMAL);
		Paragraph title = null;
		//title = new Paragraph("cpu�����ʱ���", titleFont);
		if(flag.equalsIgnoreCase("arraywt")){
			title = new Paragraph("���е�Դ��ǰ���ʱ���", titleFont);
		}else if(flag.equalsIgnoreCase("arrayavgwt")){
			title = new Paragraph("���е�Դƽ�����ʱ���", titleFont);
		}else if(flag.equalsIgnoreCase("memwt")){
			title = new Paragraph("�洢ģ���Դ��ǰ���ʱ���", titleFont);
		}else if(flag.equalsIgnoreCase("memavgwt")){
			title = new Paragraph("�洢ģ���Դƽ�����ʱ���", titleFont);
		}else if(flag.equalsIgnoreCase("memtmp")){
			title = new Paragraph("�洢����¶ȵ�ǰ�¶ȱ���", titleFont);
		}else if(flag.equalsIgnoreCase("memavgtmp")){
			title = new Paragraph("�洢����¶�ƽ���¶ȱ���", titleFont);
		}else if(flag.equalsIgnoreCase("bakwt")){
			title = new Paragraph("SPS���õ�Դ��ǰ������ʱ���", titleFont);
		}else if(flag.equalsIgnoreCase("bakavgwt")){
			title = new Paragraph("SPS���õ�Դƽ��������ʱ���", titleFont);
		}else if(flag.equalsIgnoreCase("lunharderror")){
			title = new Paragraph("Ӳ��������������", titleFont);
		}else if(flag.equalsIgnoreCase("lunsofterror")){
			title = new Paragraph("���������������", titleFont);
		}else if(flag.equalsIgnoreCase("lunlength")){
			title = new Paragraph("�����ܳ���", titleFont);
		}else if(flag.equalsIgnoreCase("diskread")){
			title = new Paragraph("������������", titleFont);
		}else if(flag.equalsIgnoreCase("diskwrite")){
			title = new Paragraph("д����������", titleFont);
		}else if(flag.equalsIgnoreCase("disksoftreaderror")){
			title = new Paragraph("���������������", titleFont);
		}else if(flag.equalsIgnoreCase("disksoftwriteerror")){
			title = new Paragraph("���д����������", titleFont);
		}else if(flag.equalsIgnoreCase("diskreadkb")){
			title = new Paragraph("��KB����", titleFont);
		}else if(flag.equalsIgnoreCase("diskwritekb")){
			title = new Paragraph("дKB����", titleFont);
		}else if(flag.equalsIgnoreCase("diskfree")){
			title = new Paragraph("��״̬ʱ��������", titleFont);
		}else if(flag.equalsIgnoreCase("diskbus")){
			title = new Paragraph("æ״̬ʱ��������", titleFont);
		}else if(flag.equalsIgnoreCase("diskhardreaderror")){
			title = new Paragraph("Ӳ��������������", titleFont);
		}else if(flag.equalsIgnoreCase("diskhardwriteerror")){
			title = new Paragraph("Ӳ��д����������", titleFont);
		}
		// ���ñ����ʽ���뷽ʽ
		title.setAlignment(Element.ALIGN_CENTER);
		// title.setFont(titleFont);
		document.add(title);
		document.add(new Paragraph("\n"));
		// ���� Table ���
		Font fontChinese = new Font(bfChinese, 11, Font.NORMAL, Color.black);
		PdfPTable aTable = new PdfPTable(4);

		int width[] = { 50, 50, 50, 50};
		aTable.setWidthPercentage(100);
		aTable.setWidths(width);

//		PdfPCell c = new PdfPCell(new Phrase("", fontChinese));
//		c.setBackgroundColor(Color.LIGHT_GRAY);
//		aTable.addCell(c);
		PdfPCell cell1 = new PdfPCell(new Phrase("ָ������", fontChinese));
		PdfPCell cell2 = new PdfPCell(new Phrase("��ǰ", fontChinese));
		PdfPCell cell3 = new PdfPCell(new Phrase("ƽ��", fontChinese));
		PdfPCell cell4 = new PdfPCell(new Phrase("���", fontChinese));
		//cell5 = new PdfPCell(new Phrase("cpu������", fontChinese));
		PdfPCell cell5 = null;
		if(flag.equalsIgnoreCase("arraywt")){
			cell5 = new PdfPCell(new Phrase("���е�Դ��ǰ����", fontChinese));
		}else if(flag.equalsIgnoreCase("arrayavgwt")){
			cell5 = new PdfPCell(new Phrase("���е�Դƽ������", fontChinese));
		}else if(flag.equalsIgnoreCase("memwt")){
			cell5 = new PdfPCell(new Phrase("�洢ģ���Դ��ǰ����", fontChinese));
		}else if(flag.equalsIgnoreCase("memavgwt")){
			cell5 = new PdfPCell(new Phrase("�洢ģ���Դƽ������", fontChinese));
		}else if(flag.equalsIgnoreCase("memtmp")){
			cell5 = new PdfPCell(new Phrase("�洢����¶ȵ�ǰ�¶�", fontChinese));
		}else if(flag.equalsIgnoreCase("memavgtmp")){
			cell5 = new PdfPCell(new Phrase("SPS���õ�Դ��ǰ�������", fontChinese));
		}else if(flag.equalsIgnoreCase("bakwt")){
			cell5 = new PdfPCell(new Phrase("SPS���õ�Դ��ǰ�������", fontChinese));
		}else if(flag.equalsIgnoreCase("bakavgwt")){
			cell5 = new PdfPCell(new Phrase("SPS���õ�Դƽ���������", fontChinese));
		}else if(flag.equalsIgnoreCase("lunharderror")){
			cell5 = new PdfPCell(new Phrase("Ӳ����������", fontChinese));
		}else if(flag.equalsIgnoreCase("lunsofterror")){
			cell5 = new PdfPCell(new Phrase("�����������", fontChinese));
		}else if(flag.equalsIgnoreCase("lunlength")){
			cell5 = new PdfPCell(new Phrase("�����ܳ���", fontChinese));
		}else if(flag.equalsIgnoreCase("diskread")){
			cell5 = new PdfPCell(new Phrase("��������", fontChinese));
		}else if(flag.equalsIgnoreCase("diskwrite")){
			cell5 = new PdfPCell(new Phrase("д������", fontChinese));
		}else if(flag.equalsIgnoreCase("disksoftreaderror")){
			cell5 = new PdfPCell(new Phrase("�����������", fontChinese));
		}else if(flag.equalsIgnoreCase("disksoftwriteerror")){
			cell5 = new PdfPCell(new Phrase("���д������", fontChinese));
		}else if(flag.equalsIgnoreCase("diskreadkb")){
			cell5 = new PdfPCell(new Phrase("��KB", fontChinese));
		}else if(flag.equalsIgnoreCase("diskwritekb")){
			cell5 = new PdfPCell(new Phrase("дKB", fontChinese));
		}else if(flag.equalsIgnoreCase("diskfree")){
			cell5 = new PdfPCell(new Phrase("��״̬ʱ����", fontChinese));
		}else if(flag.equalsIgnoreCase("diskbus")){
			cell5 = new PdfPCell(new Phrase("æ״̬ʱ����", fontChinese));
		}else if(flag.equalsIgnoreCase("diskhardreaderror")){
			cell5 = new PdfPCell(new Phrase("Ӳ����������", fontChinese));
		}else if(flag.equalsIgnoreCase("diskhardwriteerror")){
			cell5 = new PdfPCell(new Phrase("Ӳ��д������", fontChinese));
		}
		PdfPCell cell6 = new PdfPCell(new Phrase(nowvalue, fontChinese));
		PdfPCell cell7 = new PdfPCell(new Phrase(avgvalue, fontChinese));
		PdfPCell cell8 = new PdfPCell(new Phrase(maxvalue, fontChinese));
		
		
		cell1.setBackgroundColor(Color.LIGHT_GRAY);
		cell2.setBackgroundColor(Color.LIGHT_GRAY);
		cell3.setBackgroundColor(Color.LIGHT_GRAY);
		cell4.setBackgroundColor(Color.LIGHT_GRAY);
		cell5.setBackgroundColor(Color.LIGHT_GRAY);
		cell6.setBackgroundColor(Color.LIGHT_GRAY);
		cell7.setBackgroundColor(Color.LIGHT_GRAY);
		cell8.setBackgroundColor(Color.LIGHT_GRAY);
		cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell7.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell8.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell4.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell5.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell6.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell7.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell8.setVerticalAlignment(Element.ALIGN_MIDDLE);
		aTable.addCell(cell1);
		aTable.addCell(cell2);
		aTable.addCell(cell3);
		aTable.addCell(cell4);
		aTable.addCell(cell5);
		aTable.addCell(cell6);
		aTable.addCell(cell7);
		aTable.addCell(cell8);
		// ������ͨ��
		String pingpath = (String) session.getAttribute("pingpath");
		// ������Ӧʱ��ͼƬ
		// document.add(new Paragraph("\n"));
		// String ipaddress = (String)request.getParameter("ipaddress");
		// String newip = doip(ipaddress);
		Image img = Image.getInstance(pingpath);
		img.setAlignment(Image.LEFT);// ����ͼƬ��ʾλ��
		img.scalePercent(75);
		document.add(img);
		document.add(aTable);
		document.close();
	}
	
	
	
	private String creatXlsEMC() {
		Date d = new Date();
		String startdate = getParaValue("startdate");
		String flag = request.getParameter("flag");
		if (startdate == null) {
			startdate = sdf0.format(d);
		}
		String todate = getParaValue("todate");
		if (todate == null) {
			todate = sdf0.format(d);
		}
		String starttime = startdate + " 00:00:00";
		String totime = todate + " 23:59:59";
		Hashtable allcpuhash = new Hashtable();

		List returnList = new ArrayList();
		// I_MonitorIpList monitorManager=new MonitoriplistManager();
//		List memlist = (List) session.getAttribute("pinglist");
		// ������ͨ��
		String pingpath = (String) session.getAttribute("pingpath");
		Hashtable reporthash = new Hashtable();
//		reporthash.put("pinglist", memlist);
		reporthash.put("starttime", starttime);
		reporthash.put("totime", totime);
		reporthash.put("pingpath", pingpath);

		ExcelReport1 report = new ExcelReport1(new IpResourceReport(), reporthash);
		String file = "";
		String nowvalue = request.getParameter("nowvalue");
		String avgvalue = request.getParameter("avgvalue");
		String maxvalue = request.getParameter("maxvalue");
		if(flag.equalsIgnoreCase("arraywt")){
			file = "/temp/arraywt.xls";
		}else if(flag.equalsIgnoreCase("arrayavgwt")){
			file = "/temp/arrayavgwt.xls";
		}else if(flag.equalsIgnoreCase("memwt")){
			file = "/temp/memwt.xls";
		}else if(flag.equalsIgnoreCase("memavgwt")){
			file = "/temp/memavgwt.xls";
		}else if(flag.equalsIgnoreCase("memtmp")){
			file = "/temp/memtmp.xls";
		}else if(flag.equalsIgnoreCase("memavgtmp")){
			file = "/temp/memavgtmp.xls";
		}else if(flag.equalsIgnoreCase("bakwt")){
			file = "/temp/bakwt.xls";
		}else if(flag.equalsIgnoreCase("bakavgwt")){
			file = "/temp/bakavgwt.xls";
		}else if(flag.equalsIgnoreCase("diskread")){
			file = "/temp/diskread.xls";
		}else if(flag.equalsIgnoreCase("diskwrite")){
			file = "/temp/memCR.xls";
		}else if(flag.equalsIgnoreCase("disksoftreaderror")){
			file = "/temp/disksoftreaderror.xls";
		}else if(flag.equalsIgnoreCase("disksoftwriteerror")){
			file = "/temp/disksoftwriteerror.xls";
		}else if(flag.equalsIgnoreCase("diskwritekb")){
			file = "/temp/diskwritekb.xls";
		}else if(flag.equalsIgnoreCase("diskreadkb")){
			file = "/temp/diskreadkb.xls";
		}else if(flag.equalsIgnoreCase("diskfree")){
			file = "/temp/diskfree.xls";
		}else if(flag.equalsIgnoreCase("diskbus")){
			file = "/temp/diskbus.PDF";
		}else if(flag.equalsIgnoreCase("diskhardwriteerror")){
			file = "/temp/diskhardwriteerror.xls";
		}else if(flag.equalsIgnoreCase("diskhardreaderror")){
			file = "/temp/diskhardreaderror.xls";
		}else if(flag.equalsIgnoreCase("lunharderror")){
			file = "/temp/lunharderror.xls";
		}else if(flag.equalsIgnoreCase("lunsofterror")){
			file = "/temp/lunsofterror.xls";
		}else if(flag.equalsIgnoreCase("lunlength")){
			file = "/temp/lunlength.xls";
		}
		report.createReport_emc(file,flag,nowvalue,avgvalue,maxvalue);
//		request.getSession().removeAttribute("pingpath");
		request.setAttribute("filename", report.getFileName());
		return "/capreport/net/download.jsp";
		// return mapping.findForward("report_info");
	}
	
	
	
	
	
}
