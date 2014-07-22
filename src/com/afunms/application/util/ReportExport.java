/**
 * @author sunqichang/������
 * Created on May 30, 2011 11:16:30 AM
 */
package com.afunms.application.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.jfree.chart.ChartUtilities;
import org.jfree.data.category.DefaultCategoryDataset;

import com.afunms.capreport.model.ReportValue;
import com.afunms.capreport.model.StatisNumer;
import com.afunms.common.util.CreateAmLinePic;
import com.afunms.common.util.SysLogger;
import com.afunms.config.dao.AclBaseDao;
import com.afunms.config.dao.AclDetailDao;
import com.afunms.config.model.AclBase;
import com.afunms.config.model.AclDetail;
import com.afunms.config.model.CfgBaseInfo;
import com.afunms.config.model.PolicyInterface;
import com.afunms.config.model.QueueInfo;
import com.afunms.initialize.ResourceCenter;
import com.afunms.report.export.Excel;
import com.afunms.report.export.ExportInterface;
import com.afunms.report.export.ExportPdf;
import com.afunms.report.export.Pdf;
import com.afunms.report.export.Word;
import com.afunms.report.jfree.ChartCreator;
import com.afunms.report.jfree.JFreeChartBrother;
import com.lowagie.text.DocumentException;

/**
 * @author sunqichang/������
 * 
 */
public class ReportExport {
	private final int xlabel = 12;

	private ReportHelper reportHelper = null;

	private int chartWith = 768;

	private int chartHigh = 238;

	/**
	 * ��������
	 * 
	 * @param ids
	 *            ָ��id
	 * @param type
	 *            ����
	 * @param filePath
	 *            ����·��
	 * @param startTime
	 *            ��ʼʱ��
	 * @param toTime
	 *            ����ʱ��
	 * @param exportType
	 *            �����ļ�����
	 */
	public void exportReport(String ids, String type, String filePath,
			String startTime, String toTime, String exportType) {
		// startTime = "2011-05-31 00:00:00";
		// toTime = "2011-05-31 23:59:59";
		HashMap<?, ArrayList<?>> hm = null;
		// ��ű����Ϣ
		ArrayList<ArrayList<String[]>> tableList = null;
		// ���ͼƬ·��
		ArrayList<String> chartList = new ArrayList<String>();
		reportHelper = new ReportHelper();
		String title = "����";
		if ("net".equalsIgnoreCase(type) || "host".equalsIgnoreCase(type)) {
			// �����豸��������
			if ("net".equalsIgnoreCase(type)) {
				title = "�����豸����";
			} else {
				title = "����������";
			}
			hm = exportNetHost(ids, type, filePath, startTime, toTime);
			tableList = (ArrayList<ArrayList<String[]>>) hm.get("table");
			chartList = (ArrayList<String>) hm.get("chart");
		} else if ("db".equalsIgnoreCase(type)) {
			// ���ݿ�
			title = "���ݿⱨ��";
			hm = exportDb(ids, type, filePath, startTime, toTime);
			tableList = (ArrayList<ArrayList<String[]>>) hm.get("table");
			chartList = (ArrayList<String>) hm.get("chart");
		} else if ("midware".equalsIgnoreCase(type)) {
			// �м��
			title = "�м������";
			hm = exportMidware(ids, type, filePath, startTime, toTime);
			tableList = (ArrayList<ArrayList<String[]>>) hm.get("table");
			chartList = (ArrayList<String>) hm.get("chart");
		}
		String time = startTime + "~" + toTime;
		ExportInterface export = null;
		if ("xls".equals(exportType)) {
			chartWith = 768;
			chartHigh = 238;
			export = new Excel(filePath);
		} else if ("pdf".equals(exportType)) {
			chartWith = 540;
			chartHigh = 240;
			export = new Pdf(filePath);
		} else if ("doc".equals(exportType)) {
			chartWith = 540;
			chartHigh = 240;
			export = new Word(filePath);
		}
		export(export, tableList, chartList, title, time);
		if (chartList != null) {
			for (int i = 0; i < chartList.size(); i++) {
				String chartal = chartList.get(i);
				try {
					File f = new File(chartal);
					f.delete();
				} catch (Exception e) {
					SysLogger.error("ɾ��ͼƬ��" + chartal + "ʧ�ܣ�", e);
				}
			}
		}
	}
	/**
	 * ��������
	 */
	public void exportRoomReport(String type, String filePath,int cabinetid,
			String startTime, String toTime, String exportType) {
		// ��ű����Ϣ
		ArrayList<String[]> tableList = null;
		// ���ͼƬ·��
		String title = "����";
		if ("video".equalsIgnoreCase(type)) {
			
				title = "������ȫ��Ƶ������";
			RoomReportHelper helper=new RoomReportHelper();
			tableList = helper.exportRoomData(type, filePath,cabinetid, startTime, toTime);
		}else if ("law".equalsIgnoreCase(type)) {
			title = "���������ƶȱ���";
			RoomReportHelper helper=new RoomReportHelper();
			tableList = helper.exportRoomLawData(type, filePath,cabinetid, startTime, toTime);
		}else if ("guest".equalsIgnoreCase(type)) {
			title = "���������ǼǱ���";
			RoomReportHelper helper=new RoomReportHelper();
			tableList = helper.exportRoomGuestData(type, filePath,cabinetid, startTime, toTime);
		} 
		String time = startTime + "~" + toTime;
		ExportInterface export = null;
		if ("xls".equals(exportType)) {
			chartWith = 768;
			chartHigh = 238;
			export = new Excel(filePath);
		} else if ("pdf".equals(exportType)) {
			chartWith = 540;
			chartHigh = 240;
			export = new Pdf(filePath);
		} else if ("doc".equals(exportType)) {
			chartWith = 540;
			chartHigh = 240;
			export = new Word(filePath);
		}
		export(export, tableList, title, time);
		
	}
	/**
	 * �������� �˿ڶԱ� zhangys
	 * 
	 * @param ids
	 *            ָ��id
	 * @param type
	 *            ����
	 * @param filePath
	 *            ����·��
	 * @param startTime
	 *            ��ʼʱ��
	 * @param toTime
	 *            ����ʱ��
	 * @param exportType
	 *            �����ļ�����
	 */
	public void portcompareExport(String ids, String type, String filePath,
			String startTime, String toTime, String exportType) {
		HashMap<?, ArrayList<?>> hm = null;
		// ��ű����Ϣ
		ArrayList<ArrayList<String[]>> tableList = null;
		// ���ͼƬ·��
		ArrayList<String> chartList = new ArrayList<String>();
		reportHelper = new ReportHelper();
		String title = "����";
		if ("net".equalsIgnoreCase(type) || "host".equalsIgnoreCase(type)) {
			// �����豸��������
			if ("net".equalsIgnoreCase(type)) {
				title = "�����豸�ӿڱ���";
			} else {
				title = "����������";
			}
			hm = exportDkdb(ids, type, filePath, startTime, toTime);
			tableList = (ArrayList<ArrayList<String[]>>) hm.get("table");
			chartList = (ArrayList<String>) hm.get("chart");
		}
		String time = startTime + "~" + toTime;
		ExportInterface export = null;
		if ("xls".equals(exportType)) {
			chartWith = 768;
			chartHigh = 238;
			export = new Excel(filePath);
		} else if ("pdf".equals(exportType)) {
			chartWith = 540;
			chartHigh = 240;
			export = new Pdf(filePath);
		} else if ("doc".equals(exportType)) {
			chartWith = 540;
			chartHigh = 240;
			export = new Word(filePath);
		}
		export(export, tableList, chartList, title, time);
		if (chartList != null) {
			for (int i = 0; i < chartList.size(); i++) {
				String chartal = chartList.get(i);
				try {
					File f = new File(chartal);
					f.delete();
				} catch (Exception e) {
					SysLogger.error("ɾ��ͼƬ��" + chartal + "ʧ�ܣ�", e);
				}
			}
		}
	}
	
	public void exportCfgReport(String allipStr, String filePath,
			String startTime, String toTime, String exportType) {
		HashMap<?, ArrayList<?>> hm = null;
		// ��ű����Ϣ
		ArrayList<ArrayList<String[]>> tableList = null;
		// ���ͼƬ·��
		ArrayList<String> chartList = new ArrayList<String>();
		reportHelper = new ReportHelper();
		String title = "CBWFQ��ر���";

		String time = startTime + "~" + toTime;
		ExportInterface export = null;
		tableList = (ArrayList<ArrayList<String[]>>) getTableList(allipStr);
		chartList = getChartList(allipStr, startTime, toTime);
		if ("xls".equals(exportType)) {
			chartWith = 768;
			chartHigh = 238;
			export = new Excel(filePath);

		} else if ("pdf".equals(exportType)) {
			chartWith = 540;
			chartHigh = 240;
			export = new ExportPdf(filePath);
		} else if ("doc".equals(exportType)) {
			chartWith = 540;
			chartHigh = 240;
			export = new Word(filePath);
		}
		exportCfg(export, tableList, chartList, title, time);
		if (chartList != null) {
			for (int i = 0; i < chartList.size(); i++) {
				String chartal = chartList.get(i);
				try {
					File f = new File(chartal);
					 f.delete();
				} catch (Exception e) {
					SysLogger.error("ɾ��ͼƬ��" + chartal + "ʧ�ܣ�", e);
				}
			}
		}
	}
	
	/*
	 * Aclƥ����Ϣ
	 */
	public void exportAclReport(String allipStr, String filePath,
			String startTime, String toTime, String exportType) {
		HashMap<?, ArrayList<?>> hm = null;
		// ��ű����Ϣ
		ArrayList<ArrayList<String[]>> tableList = null;
		// ���ͼƬ·��
		ArrayList<String> chartList = new ArrayList<String>();
		reportHelper = new ReportHelper();
		String title = "ACL��Ϣ����";

		String time = startTime + "~" + toTime;
		ExportInterface export = null;
		tableList = (ArrayList<ArrayList<String[]>>) getAclTableList(allipStr);
		chartList = getAclChartList(allipStr, startTime, toTime);
		if ("xls".equals(exportType)) {
			chartWith = 768;
			chartHigh = 238;
			export = new Excel(filePath);

		} else if ("pdf".equals(exportType)) {
			chartWith = 540;
			chartHigh = 240;
			export = new Pdf(filePath);
		} else if ("doc".equals(exportType)) {
			chartWith = 540;
			chartHigh = 240;
			export = new Word(filePath);
		}
		exportAcl(export, tableList, chartList, title, time);
		if (chartList != null&&chartList.size()>0) {
			for (int i = 0; i < chartList.size(); i++) {
				String chartal = chartList.get(i);
				try {
					File f = new File(chartal);
					 f.delete();
				} catch (Exception e) {
					SysLogger.error("ɾ��ͼƬ��" + chartal + "ʧ�ܣ�", e);
				}
			}
		}
	}

	/**
	 * ����
	 * 
	 * @param export
	 * @param tableList
	 * @param chartList
	 * @param title
	 * @param time
	 */
	private void export(ExportInterface export,
			ArrayList<ArrayList<String[]>> tableList,
			ArrayList<String> chartList, String title, String time) {
		if (tableList != null) {
			if (tableList.get(0) != null) {
				try {
					export.insertTitle(title, tableList.get(0).get(0).length,
							time);
				} catch (Exception e) {
					SysLogger.error("", e);
				}
			} else {
				try {
					export.insertTitle(title, 0, time);
				} catch (Exception e) {
					SysLogger.error("", e);
				}
			}
			for (int i = 0; i < tableList.size(); i++) {
				String chartal = chartList.get(i);
				if (chartal != null && !"".equals(chartal.trim())) {
					try {
						export.insertChart(chartal);
					} catch (MalformedURLException e) {
						SysLogger.error("", e);
					} catch (IOException e) {
						SysLogger.error("", e);
					} catch (DocumentException e) {
						SysLogger.error("", e);
					} catch (Exception e) {
						SysLogger.error("", e);
					}
				}
				ArrayList<String[]> tableal = tableList.get(i);
				if (tableal != null && tableal.size() > 0) {
					try {
						export.insertTable(tableal);
					} catch (IOException e) {
						SysLogger.error("", e);
					} catch (Exception e) {
						SysLogger.error("", e);
					}
				}
			}
			try {
				export.save();
			} catch (Exception e) {
				SysLogger.error("------�����ļ�����ʧ�ܣ�------", e);
			}
		}
	}
	private void export(ExportInterface export,
			ArrayList<String[]> tableList, String title, String time) {
		if (tableList != null) {
			if (tableList.get(0) != null) {
				try {
					export.insertTitle(title, tableList.get(0).length,
							time);
				} catch (Exception e) {
					SysLogger.error("", e);
				}
			} else {
				try {
					export.insertTitle(title, 0, time);
				} catch (Exception e) {
					SysLogger.error("", e);
				}
			}
			try {
				export.insertTable(tableList);
			} catch (IOException e) {
				SysLogger.error("", e);
			} catch (Exception e) {
				SysLogger.error("", e);
			}
	try {
		export.save();
	} catch (Exception e) {
		SysLogger.error("------�����ļ�����ʧ�ܣ�------", e);
	}
		}		
	}
	private void exportCfg(ExportInterface export,
			ArrayList<ArrayList<String[]>> tableList,
			ArrayList<String> chartList, String title, String time) {
		if (tableList != null) {
			if (tableList.get(0) != null) {
				try {
					export.insertTitle(title, tableList.get(0).get(0).length,
							time);
				} catch (Exception e) {
					SysLogger.error("", e);
				}
			} else {
				try {
					export.insertTitle(title, 0, time);
				} catch (Exception e) {
					SysLogger.error("", e);
				}
			}
			for (int i = 0; i < tableList.size(); i++) {
				ArrayList<String[]> tableal = tableList.get(i);

				if (tableal != null && tableal.size() > 0) {
					try {
						export.insertTable(tableal);
					} catch (IOException e) {
						SysLogger.error("", e);
					} catch (Exception e) {
						SysLogger.error("", e);
					}
				}
			}
			for (int i = 0; i < chartList.size(); i++) {
				String chartal = chartList.get(i);
				if (chartal != null && !"".equals(chartal.trim())) {
					try {
						export.insertChart(chartal);
					} catch (MalformedURLException e) {
						SysLogger.error("", e);
					} catch (IOException e) {
						SysLogger.error("", e);
					} catch (DocumentException e) {
						SysLogger.error("", e);
					} catch (Exception e) {
						SysLogger.error("", e);
					}
				}
			}
			try {
				export.save();
			} catch (Exception e) {
				SysLogger.error("------�����ļ�����ʧ�ܣ�------", e);
			}
		}
	}
	/**
	 * ����
	 * 
	 * @param export
	 * @param tableList
	 * @param chartList
	 * @param title
	 * @param time
	 */
	private void exportAcl(ExportInterface export,
			ArrayList<ArrayList<String[]>> tableList,
			ArrayList<String> chartList, String title, String time) {
		if (tableList != null) {
			if (tableList.get(0) != null) {
				try {
					export.insertTitle(title, tableList.get(0).get(0).length,
							time);
				} catch (Exception e) {
					SysLogger.error("", e);
				}
			} else {
				try {
					export.insertTitle(title, 0, time);
				} catch (Exception e) {
					SysLogger.error("", e);
				}
			}
			for (int i = 0; i < tableList.size(); i++) {
				ArrayList<String[]> tableal = tableList.get(i);
				if (tableal != null && tableal.size() > 0) {
					try {
						export.insertTable(tableal);
					} catch (IOException e) {
						SysLogger.error("", e);
					} catch (Exception e) {
						SysLogger.error("", e);
					}
				}
				String chartal = chartList.get(i);
				if (chartal != null && !"".equals(chartal.trim())) {
					try {
						export.insertChart(chartal);
					} catch (MalformedURLException e) {
						SysLogger.error("", e);
					} catch (IOException e) {
						SysLogger.error("", e);
					} catch (DocumentException e) {
						SysLogger.error("", e);
					} catch (Exception e) {
						SysLogger.error("", e);
					}
				}
				
			}
			try {
				export.save();
			} catch (Exception e) {
				SysLogger.error("------�����ļ�����ʧ�ܣ�------", e);
			}
		}
	}

	public ArrayList<String> getChartList(String allipStr, String starttime,
			String totime) {
		ArrayList<String> charList = new ArrayList<String>();
		CreateAmLinePic linePic = new CreateAmLinePic();
		List policyInterfaceList = linePic.getInterfaceList(allipStr,
				starttime, totime);
		List<List<PolicyInterface>> sortData = linePic
				.getSortClassData(policyInterfaceList);
		List queueList = linePic.getQueueList(allipStr, starttime, totime);
		String imgPath = makeJfreeChartData(sortData, "���Ͱ�������������ͼ", "", "");
		String[] imgpaths = makeJfreeChartInterface(sortData,
				"����ƥ�估��������ͼ(�˿���:����:ָ��)", "", "");

		String queuePath = makeJfreeChartQueue(queueList, "�˿�����ڶ�������ͼ", "", "");
		charList.add(imgPath);
		if (imgpaths != null) {
			for (int i = 0; i < imgpaths.length; i++) {
				charList.add(imgpaths[i]);
			}
		}
		charList.add(queuePath);
		return charList;
	}
	/*
	 * Acl��Ϣ����
	 */
	public ArrayList<String> getAclChartList(String ip, String starttime,
			String totime) {
		ArrayList<String> charList = new ArrayList<String>();
		AclDetailDao detailDao=null;
		List detailList=null;
		
		
		try {
			
			detailDao=new AclDetailDao();
			 detailList=detailDao.findByCondition(" where collecttime>='"+starttime+"' and collecttime<='"+totime+"' and status=1 group by baseId,name,collecttime");
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			detailDao.close();
		}
		CreateAmLinePic linePic = new CreateAmLinePic();
		
		HashMap<Integer, List<?>> sortData = linePic.getSortDetailData(detailList);
	//	List queueList = linePic.getQueueList(allipStr, starttime, totime);
		String[] imgpaths = makeAclJfreeChart(sortData,ip,
				"Aclƥ������ͼ", "", "");

		
		if (imgpaths != null) {
			for (int i = 0; i < imgpaths.length; i++) {
				charList.add(imgpaths[i]);
			}
		}
		
		return charList;
	}
	public ArrayList<?> getTableList(String allipStr) {
		CreateAmLinePic helper = new CreateAmLinePic();
		List<CfgBaseInfo> classList = helper.getClassList(allipStr);
		List<CfgBaseInfo> policyList = helper.getPolicyList(allipStr);
		ArrayList<ArrayList<String[]>> tableList = new ArrayList<ArrayList<String[]>>();
		ArrayList<String[]> baseal = new ArrayList<String[]>();
		ArrayList<String[]> policyal = new ArrayList<String[]>();
		String[] classtitle = { "������", "����", "�ɼ�ʱ��" };
		baseal.add(classtitle);
		if (classList != null && classList.size() > 0) {
			for (int i = 0; i < classList.size(); i++) {
				CfgBaseInfo info = classList.get(i);
				String[] arr1 = { info.getName(), info.getValue(),
						info.getCollecttime() };

				baseal.add(arr1);
			}
		}
		tableList.add(baseal);
		String[] policy = { "�������� ", "������", "����", "�ɼ�ʱ��" };
		policyal.add(policy);
		if (policyList != null && policyList.size() > 0) {
			for (int i = 0; i < policyList.size(); i++) {
				CfgBaseInfo info = policyList.get(i);
				String[] arr1 = { info.getPolicyName(), info.getName(),
						info.getValue(), info.getCollecttime() };
				policyal.add(arr1);
			}
		}
		String[] strings = baseal.get(0);
		tableList.add(policyal);
		return tableList;

	}
	public ArrayList<?> getAclTableList(String ip) {
		CreateAmLinePic helper = new CreateAmLinePic();
		AclBaseDao baseDao=null;
		List<AclBase> baseList=null;
		try {
			baseDao=new AclBaseDao();
			baseList=baseDao.findByCondition(" where ipaddress='"+ip+"'");
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			baseDao.close();
		}
		ArrayList<ArrayList<String[]>> tableList = new ArrayList<ArrayList<String[]>>();
		ArrayList<String[]> baseal = null;
	if(baseList!=null){
		for (int i = 0; i < baseList.size(); i++) {
			 baseal = new ArrayList<String[]>();
			AclBase base=baseList.get(i);
			if (base!=null) {
				String[] classtitle = { "���ƣ�"+base.getName()};
				baseal.add(classtitle);
			}
			tableList.add(baseal);
		}
		
	}
		
		
		
		return tableList;

	}
	
	/**
	 * @param ids
	 * @param type
	 * @param filePath
	 * @param startTime
	 * @param toTime
	 * @return
	 */
	private HashMap<?, ArrayList<?>> exportNetHost(String ids, String type,
			String filePath, String startTime, String toTime) {
		HashMap hm = new HashMap();
		// ��ű����Ϣ
		ArrayList<ArrayList<String[]>> tableList = new ArrayList<ArrayList<String[]>>();
		// ���ͼƬ·��
		ArrayList<String> chartList = new ArrayList<String>();
		// �����豸��������
		HashMap all = reportHelper.getAllValue(ids, startTime, toTime);
		ReportValue ping = (ReportValue) all.get("ping");
		ReportValue cpu = (ReportValue) all.get("cpu");
		ReportValue mem = (ReportValue) all.get("mem");
		ReportValue port = (ReportValue) all.get("port");
		ReportValue disk = (ReportValue) all.get("disk");
		List<StatisNumer> table = (List<StatisNumer>) all.get("gridVlue");
		Iterator<StatisNumer> tableIt = table.iterator();
		ArrayList<String[]> pingal = new ArrayList<String[]>();
		String[] pingtitle = { "ip", "��ǰ��ͨ��", "��С��ͨ��", "ƽ����ͨ��" };
		if(ping.getIpList().size() > 0)
		pingal.add(pingtitle);
		ArrayList<String[]> cpual = new ArrayList<String[]>();
		String[] cputitle = { "ip", "��ǰ������", "���������", "ƽ��������" };
		if(cpu.getIpList().size()>0)
		cpual.add(cputitle);
		ArrayList<String[]> memal = new ArrayList<String[]>();
		String[] memtitle = { "ip", "��ǰ������", "���������", "ƽ��������" };
		if(mem.getIpList().size()>0)
		memal.add(memtitle);
		ArrayList<String[]> diskal = new ArrayList<String[]>();

		ArrayList<String[]> portal = new ArrayList<String[]>();
		String[] porttitle = { "�˿�����", "��ǰ����", "�������", "ƽ������" };
		if(port.getIpList().size()>0)
		portal.add(porttitle);
		
		ArrayList<StatisNumer> portin = new ArrayList<StatisNumer>();
		ArrayList<StatisNumer> portout = new ArrayList<StatisNumer>();
		while (tableIt.hasNext()) {
			StatisNumer sn = tableIt.next();
			String tabletype = sn.getType();
			if ("gridPing".equals(tabletype)) {
				String[] arry = { sn.getIp(), sn.getCurrent(), sn.getMininum(),
						sn.getAverage() };
				pingal.add(arry);
			} else if ("gridCpu".equals(tabletype)) {
				String[] arry = { sn.getIp(), sn.getCurrent(), sn.getMaximum(),
						sn.getAverage() };
				cpual.add(arry);
			} else if ("gridMem".equals(tabletype)) {
				String[] arry = { sn.getIp(), sn.getCurrent(), sn.getMaximum(),
						sn.getAverage() };
				memal.add(arry);
			} else if ("gridDisk".equals(tabletype)) {
				String[] arry = { sn.getIp() + "(" + sn.getName() + ")",
						sn.getCurrent() };
				diskal.add(arry);
			} else if ("gridPortIn".equals(tabletype)) {
				portin.add(sn);
			} else if ("gridPortOut".equals(tabletype)) {
				portout.add(sn);
			}
		}
		ArrayList<ArrayList<String>> portName = new ArrayList<ArrayList<String>>();
		ArrayList<String> portInName = new ArrayList<String>();
		ArrayList<String> portOutName = new ArrayList<String>();
		for (int i = 0; i < portin.size(); i++) {
			StatisNumer snin = portin.get(i);
			StatisNumer snout = portout.get(i);
			portInName.add("(" + snin.getName() + "���)");
			portOutName.add("(" + snout.getName() + "����)");
			String[] arry1 = { snin.getIp() + "(" + snin.getName() + "���)",
					snin.getCurrent(), snin.getMaximum(), snin.getAverage() };
			String[] arry2 = { snout.getIp() + "(" + snout.getName() + "����)",
					snout.getCurrent(), snout.getMaximum(), snout.getAverage() };
			portal.add(arry1);
			portal.add(arry2);
		}
		
		if(ping.getIpList().size() > 0) tableList.add(pingal);
		if(cpu.getIpList().size() > 0)tableList.add(cpual);
		if(mem.getIpList().size() > 0)tableList.add(memal);
		if (diskal != null && diskal.size() > 0) {
			tableList.add(diskal);
		}
		if(port.getIpList().size() > 0)tableList.add(portal);
		
		// ��ͨ��
		SysLogger.info("ping=========ListValueSize:"+ping.getListValue().size()+"=====iplistsize:"+ping.getIpList().size());
		String pingpath = makeJfreeChartData(ping.getListValue(), ping
				.getIpList(), "��ͨ��", "ʱ��", "");
		if(ping.getIpList().size() > 0) chartList.add(pingpath);
		// cpu������
		String cpupath = makeJfreeChartData(cpu.getListValue(),
				cpu.getIpList(), "cpu������", "ʱ��", "");
		if(cpu.getIpList().size() > 0) chartList.add(cpupath);
		// �ڴ�
		String mempath = makeJfreeChartData(mem.getListValue(),
				mem.getIpList(), "�ڴ�������", "ʱ��", "");
		if(mem.getIpList().size() > 0) chartList.add(mempath);
		if (diskal != null && diskal.size() > 0) {
			// ����
			String diskpath = makeDiskJfreeChartData(diskal, disk.getIpList(),
					"����������", "ʱ��", "");
			chartList.add(diskpath);
			if ("host".equalsIgnoreCase(type)) {
				String[] disktitle = { "IP(��������)", "��ǰ������" };
				diskal.add(0, disktitle);
			}
		}
		// �˿�
		ArrayList<List<?>> alport = new ArrayList<List<?>>();
		alport.add(port.getListValue());
		alport.add(port.getListTemp());
		portName.add(portInName);
		portName.add(portOutName);
		String portpath = makePortJfreeChartData(alport, portName, port
				.getIpList(), "�˿�", "ʱ��", "");
		if(port.getIpList().size()>0) chartList.add(portpath);
		hm.put("table", tableList);
		hm.put("chart", chartList);
		return hm;
	}

	/**
	 * ��Դ|�˿ڶԱ�--���� zhangys
	 * @param ids
	 * @param type
	 * @param filePath
	 * @param startTime
	 * @param toTime
	 * @return
	 */
	private HashMap<?, ArrayList<?>> exportDkdb(String ids, String type,
			String filePath, String startTime, String toTime) {
		HashMap hm = new HashMap();
		// ��ű����Ϣ
		ArrayList<ArrayList<String[]>> tableList = new ArrayList<ArrayList<String[]>>();
		// ���ͼƬ·��
		ArrayList<String> chartList = new ArrayList<String>();
		// �����豸��������
		HashMap all = reportHelper.getAllValue(ids, startTime, toTime);
		ReportValue port = (ReportValue) all.get("port");
		List<StatisNumer> table = (List<StatisNumer>) all.get("gridVlue");
		Iterator<StatisNumer> tableIt = table.iterator();

		ArrayList<String[]> portal = new ArrayList<String[]>();
		String[] porttitle = { "�˿�����", "��ǰ����", "�������", "��С����", "ƽ������" };
		portal.add(porttitle);
		ArrayList<StatisNumer> portin = new ArrayList<StatisNumer>();
		ArrayList<StatisNumer> portout = new ArrayList<StatisNumer>();
		while (tableIt.hasNext()) {
			StatisNumer sn = tableIt.next();
			String tabletype = sn.getType();
			if ("gridPortIn".equals(tabletype)) {
				portin.add(sn);
			} else if ("gridPortOut".equals(tabletype)) {
				portout.add(sn);
			}
		}
		ArrayList<ArrayList<String>> portName = new ArrayList<ArrayList<String>>();
		ArrayList<String> portInName = new ArrayList<String>();
		ArrayList<String> portOutName = new ArrayList<String>();
		for (int i = 0; i < portin.size(); i++) {
			StatisNumer snin = portin.get(i);
			StatisNumer snout = portout.get(i);
			portInName.add("(" + snin.getName() + "���)");
			portOutName.add("(" + snout.getName() + "����)");
			String[] arry1 = { snin.getIp() + "(" + snin.getName() + "���)",
					snin.getCurrent(), snin.getMaximum(), snin.getMininum(), snin.getAverage() };
			String[] arry2 = { snout.getIp() + "(" + snout.getName() + "����)",
					snout.getCurrent(), snout.getMaximum(), snout.getMininum(), snout.getAverage() };
			portal.add(arry1);
			portal.add(arry2);
		}
		tableList.add(portal);
		// �˿�
		ArrayList<List<?>> alport = new ArrayList<List<?>>();
		alport.add(port.getListValue());
		alport.add(port.getListTemp());
		portName.add(portInName);
		portName.add(portOutName);
		String portpath = makePortJfreeChartData(alport, portName, port.getIpList(), "�˿�", "ʱ��", "");
		chartList.add(portpath);
		hm.put("table", tableList);
		hm.put("chart", chartList);
		return hm;
	}
	
	/**
	 * @param ids
	 * @param type
	 * @param filePath
	 * @param startTime
	 * @param toTime
	 * @return
	 */
	private HashMap<?, ArrayList<?>> exportMidware(String ids, String type,
			String filePath, String startTime, String toTime) {
		HashMap hm = new HashMap();
		// ��ű����Ϣ
		ArrayList<ArrayList<String[]>> tableList = new ArrayList<ArrayList<String[]>>();
		// ���ͼƬ·��
		ArrayList<String> chartList = new ArrayList<String>();
		HashMap<?, ?> mwall = reportHelper.getMidwareValue(ids, startTime,
				toTime);
		ReportValue ping = (ReportValue) mwall.get("ping");
		ReportValue jvm = (ReportValue) mwall.get("jvm");
		List<StatisNumer> table = (List<StatisNumer>) mwall.get("gridVlue");
		ArrayList<String[]> pingal = new ArrayList<String[]>();
		String[] pingtitle = { "ip", "��ǰ��ͨ��", "��С��ͨ��", "ƽ����ͨ��" };
		pingal.add(pingtitle);
		ArrayList<String[]> jvmal = new ArrayList<String[]>();
		String[] jvmtitle = { "ip", "��ǰ������", "���������", "ƽ��������" };
		jvmal.add(jvmtitle);
		Iterator<StatisNumer> tableIt = table.iterator();
		while (tableIt.hasNext()) {
			StatisNumer sn = tableIt.next();
			String tabletype = sn.getType();
			if ("gridPing".equals(tabletype)) {
				String name = sn.getName();
				if ("IIS".equalsIgnoreCase(name)) {
					String[] arry = { sn.getIp(), sn.getCurrent(),
							sn.getMininum(), sn.getAverage() };
					pingal.add(arry);
				} else if ("Tomcat".equalsIgnoreCase(name)) {
					String[] arry = { sn.getIp(), sn.getCurrent(),
							sn.getMininum(), sn.getAverage() };
					pingal.add(arry);
				}
			} else if ("tomcat_jvm".equals(tabletype)) {
				String[] arry = { sn.getIp(), sn.getCurrent(), sn.getMaximum(),
						sn.getAverage() };
				jvmal.add(arry);
			}
		}
		tableList.add(pingal);
		tableList.add(jvmal);
		// ��ͨ��
		String pingpath = makeJfreeChartData(ping.getListValue(), ping
				.getIpList(), "��ͨ��", "ʱ��", "");
		chartList.add(pingpath);
		String jvmpath = makeJfreeChartData(jvm.getListValue(),
				jvm.getIpList(), "������", "ʱ��", "");
		chartList.add(jvmpath);
		hm.put("table", tableList);
		hm.put("chart", chartList);
		return hm;
	}

	/**
	 * @param ids
	 * @param type
	 * @param filePath
	 * @param startTime
	 * @param toTime
	 * @return
	 */
	private HashMap<?, ArrayList<?>> exportDb(String ids, String type,
			String filePath, String startTime, String toTime) {
		HashMap hm = new HashMap();
		// ��ű����Ϣ
		ArrayList<ArrayList<String[]>> tableList = new ArrayList<ArrayList<String[]>>();
		// ���ͼƬ·��
		ArrayList<String> chartList = new ArrayList<String>();
		// ���ݿⱨ��
		HashMap<?, ?> dball = reportHelper.getDbValue(ids, startTime, toTime);
		ReportValue ping = (ReportValue) dball.get("ping");
		List<StatisNumer> val = (List<StatisNumer>) dball.get("val");// mysql������Ϣ
		ReportValue tablespace = (ReportValue) dball.get("tablespace");// oracle��ռ�
		List<StatisNumer> table = (List<StatisNumer>) dball.get("gridVlue");
		Iterator<StatisNumer> tableIt = table.iterator();
		ArrayList<String[]> pingal = new ArrayList<String[]>();
		String[] pingtitle = { "ip", "��ǰ��ͨ��", "��С��ͨ��", "ƽ����ͨ��" };
		pingal.add(pingtitle);
		ArrayList<String[]> valal = new ArrayList<String[]>();
		String[] valtite = { "ip", "����", "����ָ��" };
		valal.add(valtite);
		ArrayList<String[]> tablespaceal = new ArrayList<String[]>();
		// String[] tablespacetitle = { "ip", "��ǰ������", "���������", "ƽ��������" };
		// tablespaceal.add(tablespacetitle);
		while (tableIt.hasNext()) {
			StatisNumer sn = tableIt.next();
			String tabletype = sn.getType();
			if ("gridPing".equals(tabletype)) {
				String[] arry = { sn.getIp(), sn.getCurrent(), sn.getMininum(),
						sn.getAverage() };
				pingal.add(arry);
			} else if ("gridTableSpace".equals(tabletype)) {
				// String[] arry = { sn.getIp(), sn.getCurrent(),
				// sn.getMaximum(), sn.getAverage() };
				// tablespaceal.add(arry);
			}
		}
		Iterator<StatisNumer> valit = val.iterator();
		while (valit.hasNext()) {
			StatisNumer sn = valit.next();
			String[] arry = { sn.getIp(), sn.getName(), sn.getCurrent() };
			valal.add(arry);
		}
		tableList.add(pingal);
		tableList.add(valal);
		tableList.add(tablespaceal);
		// ��ͨ��
		String pingpath = makeJfreeChartData(ping.getListValue(), ping
				.getIpList(), "��ͨ��", "ʱ��", "");
		chartList.add(pingpath);
		// mysql ������Ϣ
		// String valpath = makeJfreeChartData(val.getListValue(),
		// val.getIpList(), "mysql������Ϣ", "ʱ��", "");
		chartList.add("");
		// oracle��ռ�
		String tablespacealspath = "";
		if (tablespace.getListValue() != null
				&& tablespace.getListValue().size() > 0) {
			tablespacealspath = makeJfreeChartData((List) tablespace
					.getListValue().get(0), tablespace.getIpList(),
					"oracle��ռ�", "ʱ��", "");
		} else {
			SysLogger.info("------oracle��ռ�����Ϊ�գ�δ����ͼƬ��------");
		}
		chartList.add(tablespacealspath);
		hm.put("table", tableList);
		hm.put("chart", chartList);
		return hm;

	}

	/**
	 * ���ɶ˿�jfreechartͼƬ
	 * 
	 * @param alport
	 * @param portName
	 * @param ipList
	 * @param title
	 * @param xdesc
	 * @param ydesc
	 * @return
	 */
	private String makePortJfreeChartData(ArrayList<List<?>> alport,
			ArrayList<ArrayList<String>> portName, List<String> ipList,
			String title, String xdesc, String ydesc) {
		String chartPath = ResourceCenter.getInstance().getSysPath() + "temp"
				+ File.separator + System.currentTimeMillis() + ".png";
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		for (int i = 0; i < alport.size(); i++) {
			List<List> dataList = (List<List>) alport.get(i);
			ArrayList<String> pnlist = portName.get(i);
			if (dataList != null && dataList.size() > 0) {
				int xcount = 0;
				String x = "";
				for (int j = 0; j < dataList.size(); j++) {
					x = "";
					Vector v0 = null;
					List xList = (List) dataList.get(0);
					xcount = xList.size();
					List dataList1 = (List) dataList.get(j);
					String pn = pnlist.get(j);
					for (int m = 0; m < dataList1.size(); m++) {
						// TODO:x��ʱ�����겻һ���������в���������ʾ����ʱ����Ϊ����1��x��ʱ������
						if (xcount <= m) {
							continue;
						}
						v0 = (Vector) xList.get(m);
						Vector v = (Vector) dataList1.get(m);
						if (xcount > xlabel) {
							x = x + "\r";
							BigDecimal bd = new BigDecimal(xcount);
							int mod = Integer.parseInt(bd.divide(
									new BigDecimal(xlabel),
									BigDecimal.ROUND_CEILING).toString());
							if (m % mod == 0) {
								dataset.addValue(Double.parseDouble(String
										.valueOf(v.get(0))), String
										.valueOf(ipList.get(j) + pn), String
										.valueOf(v0.get(1)));
							} else {
								// ���x����̫�༷��һ�𿴲���
								dataset.addValue(Double.parseDouble(String
										.valueOf(v.get(0))), String
										.valueOf(ipList.get(j) + pn), x);
							}
						} else {
							dataset.addValue(Double.parseDouble(String
									.valueOf(v.get(0))), String.valueOf(ipList
									.get(j)
									+ pn), String.valueOf(v0.get(1)));
						}
					}
				}
			}
			String chartkey = ChartCreator.createLineChart(title, xdesc, ydesc,
					dataset, chartWith, chartHigh);
			JFreeChartBrother chart = (JFreeChartBrother) ResourceCenter
					.getInstance().getChartStorage().get(chartkey);
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
		}
		return chartPath;
	}

	/**
	 * ����jfreechartͼƬ
	 * 
	 * @param dataList
	 * @param ipList
	 * @param title
	 * @param xdesc
	 * @param ydesc
	 * @return ͼƬ·��
	 */
	public String makeJfreeChartData(List dataList, List ipList, String title,
			String xdesc, String ydesc) {   
		String chartPath = ResourceCenter.getInstance().getSysPath() + "temp"
				+ File.separator + System.currentTimeMillis() + ".png";
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		if (dataList != null && dataList.size() > 0) {
			int xcount = 0;
			String x = "";
			for (int j = 0; j < dataList.size(); j++) {
				x = "";
				Vector v0 = null;
				List xList = (List) dataList.get(0);
				xcount = xList.size();
				List dataList1 = (List) dataList.get(j);
				for (int m = 0; m < dataList1.size(); m++) {
					// TODO:x��ʱ�����겻һ���������в���������ʾ����ʱ����Ϊ����1��x��ʱ������
					if (xcount <= m) {
						continue;
					}
					v0 = (Vector) xList.get(m);
					Vector v = (Vector) dataList1.get(m);
					if (xcount > xlabel) {
						x = x + "\r";
						BigDecimal bd = new BigDecimal(xcount);
						int mod = Integer.parseInt(bd.divide(
								new BigDecimal(xlabel),
								BigDecimal.ROUND_CEILING).toString());
						if (m % mod == 0) {
							dataset.addValue(Double.parseDouble(String
									.valueOf(v.get(0))), String.valueOf(ipList
									.get(j)), String.valueOf(v0.get(1)));
						} else {
							// ���x����̫�༷��һ�𿴲���
							dataset.addValue(Double.parseDouble(String
									.valueOf(v.get(0))), String.valueOf(ipList
									.get(j)), x);
						}
					} else {
						dataset.addValue(Double.parseDouble(String.valueOf(v
								.get(0))), String.valueOf(ipList.get(j)),
								String.valueOf(v0.get(1)));
					}
				}
			}
			String chartkey = ChartCreator.createLineChart(title, xdesc, ydesc,
					dataset, chartWith, chartHigh);
			JFreeChartBrother chart = (JFreeChartBrother) ResourceCenter
					.getInstance().getChartStorage().get(chartkey);
			FileOutputStream fos = null;
			try {
				File f = new File(chartPath);
				if (!f.exists()) {
					f.createNewFile();
				}
				fos = new FileOutputStream(f);
				ChartUtilities.writeChartAsPNG(fos, chart.getChart(), chart
						.getWidth(), chart.getHeight());
				fos.flush();
			} catch (IOException ioe) {
				chartPath = "";
				SysLogger.error("", ioe);
			} finally{
				if(fos != null){
					try {
						fos.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return chartPath;
	}

	public String makeJfreeChartData(List dataList, String title, String xdesc,
			String ydesc) {
		String chartPath = ResourceCenter.getInstance().getSysPath() + "temp"
				+ File.separator + System.currentTimeMillis() + ".png";
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		;
		if (dataList != null && dataList.size() > 0) {
			Vector<String> timeVector = new Vector<String>();

			int xcount = 0;
			String x = "";

			List interList = (List) dataList.get(0);
			if (interList != null) {

				for (int j = 0; j < interList.size(); j++) {
					PolicyInterface vo = (PolicyInterface) interList.get(j);
					timeVector.add(vo.getCollecttime());
				}
			}
			for (int j = 0; j < dataList.size(); j++) {

				x = "";
				// List<PolicyInterface> xList = (List) dataList.get(0);

				// TODO:x��ʱ�����겻һ���������в���������ʾ����ʱ����Ϊ����1��x��ʱ������
				List interfaceList = (List) dataList.get(j);
				xcount = interfaceList.size();
				if (interfaceList != null && interfaceList.size() > 0) {
					for (int i = 0; i < interfaceList.size(); i++) {
						if (i > timeVector.size())
							continue;
						PolicyInterface vo = (PolicyInterface) interfaceList
								.get(i);
						// System.out.println(vo.getInterfaceName()+":"+vo.getClassName());
						if (xcount > xlabel) {
							x = x + "\r";
							BigDecimal bd = new BigDecimal(xcount);
							int mod = Integer.parseInt(bd.divide(
									new BigDecimal(xlabel),
									BigDecimal.ROUND_CEILING).toString());
							if (i % mod == 0) {
								dataset.addValue(Double.parseDouble(String
										.valueOf(vo.getOfferedRate())), vo
										.getInterfaceName()
										+ ":"
										+ vo.getClassName()
										+ ":offered rate", timeVector.get(i));
								dataset.addValue(Double.parseDouble(String
										.valueOf(vo.getDropRate())), vo
										.getInterfaceName()
										+ ":"
										+ vo.getClassName()
										+ ":drop rate", timeVector.get(i));
							} else {
								dataset.addValue(Double.parseDouble(String
										.valueOf(vo.getOfferedRate())), vo
										.getInterfaceName()
										+ ":"
										+ vo.getClassName()
										+ ":offered rate", x);
								dataset.addValue(Double.parseDouble(String
										.valueOf(vo.getDropRate())), vo
										.getInterfaceName()
										+ ":"
										+ vo.getClassName()
										+ ":drop rate", x);
							}
						} else {
							dataset.addValue(Double.parseDouble(String
									.valueOf(vo.getOfferedRate())),
									vo.getInterfaceName() + ":"
											+ vo.getClassName()
											+ ":offered rate", timeVector
											.get(i));
							dataset.addValue(Double.parseDouble(String
									.valueOf(vo.getDropRate())), vo
									.getInterfaceName()
									+ ":" + vo.getClassName() + ":drop rate",
									timeVector.get(i));
						}
					}
				}
			}
		}
		String chartkey = ChartCreator.createLineChart(title, xdesc, ydesc,
				dataset, chartWith, chartHigh);
		JFreeChartBrother chart = (JFreeChartBrother) ResourceCenter
				.getInstance().getChartStorage().get(chartkey);

		try {
			File f = new File(chartPath);
			if (!f.exists()) {
				f.createNewFile();
			}
			FileOutputStream fos = new FileOutputStream(f);
			ChartUtilities.writeChartAsPNG(fos, chart.getChart(), chart
					.getWidth(), chart.getHeight());
		} catch (IOException ioe) {
			// chartPath = "";
			SysLogger.error("", ioe);
		}

		return chartPath;
	}

	public String makeJfreeChartQueue(List dataList, String title,
			String xdesc, String ydesc) {
		String chartPath = ResourceCenter.getInstance().getSysPath() + "temp"
				+ File.separator + System.currentTimeMillis() + ".png";
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		String[] items = { "input size", "input max", "input drops",
				"input flushes", "output size", "output max total",
				"output threshold", "output drops", "Available Bandwidth(kb/s)" };
		Integer[] values = new Integer[items.length];
		if (dataList != null && dataList.size() > 0) {
			int xcount = 0;
			String x = "";
			QueueInfo vo = null;
			Vector<String> xVector = new Vector<String>();
			for (int i = 0; i < dataList.size(); i++) {
				vo = (QueueInfo) dataList.get(i);
				if (vo == null)
					continue;
				xVector.add(vo.getCollecttime());
			}
			String time = "";
			for (int j = 0; j < dataList.size(); j++) {
				x = "";

				xcount = dataList.size();

				vo = (QueueInfo) dataList.get(j);
               if(vo==null)continue;
				time = vo.getCollecttime();
				values[0] = vo.getInputSize();
				values[1] = vo.getInputMax();
				values[2] = vo.getInputDrops();
				values[3] = vo.getInputFlushes();
				values[4] = vo.getOutputSize();
				values[5] = vo.getOutputMax();
				values[6] = vo.getOutputThreshold();
				values[7] = vo.getOutputDrops();
				values[8] = vo.getAvailBandwidth();
				if (xcount > xlabel) {

					x = x + "\r";
					BigDecimal bd = new BigDecimal(xcount);
					int mod = Integer.parseInt(bd.divide(
							new BigDecimal(xlabel), BigDecimal.ROUND_CEILING)
							.toString());
					if (j % mod == 0) {
						for (int k = 0; k < items.length; k++) {
							dataset.addValue(Double.parseDouble(String
									.valueOf(values[k])), items[k], time);
						}

					} else {
						// ���x����̫�༷��һ�𿴲���
						for (int k = 0; k < items.length; k++) {
							dataset.addValue(Double.parseDouble(String
									.valueOf(values[k])), items[k], x);
						}

					}
				} else {
					for (int k = 0; k < items.length; k++) {
						dataset.addValue(Double.parseDouble(String
								.valueOf(values[k])), items[k], time);
					}

				}

			}
		}

		String chartkey = ChartCreator.createLineChart(title, xdesc, ydesc,
				dataset, chartWith, chartHigh);
		JFreeChartBrother chart = (JFreeChartBrother) ResourceCenter
				.getInstance().getChartStorage().get(chartkey);
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

	// �ӿڶ������
	public String[] makeJfreeChartInterface(List dataList, String title,
			String xdesc, String ydesc) {
		String[] chartPaths = null;
		// String chartPath = ResourceCenter.getInstance().getSysPath() + "temp"
		// + File.separator
		// + System.currentTimeMillis() + ".png";
		DefaultCategoryDataset dataset = null;
		if (dataList != null && dataList.size() > 0) {
			Vector<String> timeVector = new Vector<String>();
			String[] items = { "bytes matched", "pkts matched", "total drops",
					"bytes drops", "depth", "total queued", "no-buffer drops" };
			chartPaths = new String[dataList.size()];
			int xcount = 0;
			String x = "";

			List interList = (List) dataList.get(0);
			if (interList != null) {

				for (int j = 0; j < interList.size(); j++) {
					PolicyInterface vo = (PolicyInterface) interList.get(j);
					timeVector.add(vo.getCollecttime());
				}
			}
			for (int j = 0; j < dataList.size(); j++) {
				chartPaths[j] = ResourceCenter.getInstance().getSysPath()
						+ "temp" + File.separator + System.currentTimeMillis()
						+ j + ".png";
				dataset = new DefaultCategoryDataset();
				x = "";
				// List<PolicyInterface> xList = (List) dataList.get(0);

				// TODO:x��ʱ�����겻һ���������в���������ʾ����ʱ����Ϊ����1��x��ʱ������
				List interfaceList = (List) dataList.get(j);
				xcount = interfaceList.size();
				if (interfaceList != null && interfaceList.size() > 0) {
					for (int i = 0; i < interfaceList.size(); i++) {
						if (i > timeVector.size())
							continue;
						PolicyInterface vo = (PolicyInterface) interfaceList
								.get(i);
						if (xcount > xlabel) {
							x = x + "\r";
							BigDecimal bd = new BigDecimal(xcount);
							int mod = Integer.parseInt(bd.divide(
									new BigDecimal(xlabel),
									BigDecimal.ROUND_CEILING).toString());
							if (i % mod == 0) {
								addDataSet(dataset, vo, timeVector.get(i));
							} else {
								addDataSet(dataset, vo, "");
							}
						} else {
							addDataSet(dataset, vo, timeVector.get(i));
						}
					}
				}
				String chartkey = ChartCreator.createLineChart(title, xdesc,
						ydesc, dataset, chartWith, chartHigh);
				JFreeChartBrother chart = (JFreeChartBrother) ResourceCenter
						.getInstance().getChartStorage().get(chartkey);

				try {
					File f = new File(chartPaths[j]);
					if (!f.exists()) {
						f.createNewFile();
					}
					FileOutputStream fos = new FileOutputStream(f);
					ChartUtilities.writeChartAsPNG(fos, chart.getChart(), chart
							.getWidth(), chart.getHeight());
				} catch (IOException ioe) {
					// chartPath = "";
					SysLogger.error("", ioe);
				}
			}
		}
		return chartPaths;
	}
	// Acl����ͼ���
	public String[] makeAclJfreeChart(HashMap<Integer, List<?>> alldata,String ip, String title,
			String xdesc, String ydesc) {
		String[] chartPaths = null;
		// String chartPath = ResourceCenter.getInstance().getSysPath() + "temp"
		// + File.separator
		// + System.currentTimeMillis() + ".png";
		
		if (alldata!=null) {
			AclBaseDao baseDao=null;
			List<AclBase> baseList=null;
			HashMap<String, String> amMap=new HashMap<String, String>();
			try {
				baseDao=new AclBaseDao();
				baseList=baseDao.findByCondition(" where ipaddress='"+ip+"'");
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				baseDao.close();
			}
			
			List<List<AclDetail>> dataList=null;
			if (alldata != null) {
				if (baseList!=null&&baseList.size()>0) {
					chartPaths=new String[baseList.size()];
					for (int i = 0; i < baseList.size(); i++) {
						AclBase base=baseList.get(i);
						if(base!=null){
							if(alldata.containsKey(base.getId())){
							dataList=(List<List<AclDetail>>) alldata.get(base.getId());
							if (dataList!=null&&dataList.size()>0) {
								chartPaths[i]=makeAclChart(dataList,title, xdesc, ydesc);
							}
							}
						}
					}
				}
			}
		}
	
	
		return chartPaths;
	}
	public String makeAclChart(List<List<AclDetail>> dataList,String title,String xdesc, String ydesc) {
		DefaultCategoryDataset dataset =new DefaultCategoryDataset();;
		
		String chartPath = ResourceCenter.getInstance().getSysPath()
		+ "temp" + File.separator + System.currentTimeMillis()+ ".png";
		if (dataList != null && dataList.size() > 0) {
			
			int xcount = 0;
			String x = "";
			
			for (int j = 0; j < dataList.size(); j++) {
				
				x = "";

				// TODO:x��ʱ�����겻һ���������в���������ʾ����ʱ����Ϊ����1��x��ʱ������
				List interfaceList = (List) dataList.get(j);
				xcount = interfaceList.size();
				if (interfaceList != null && interfaceList.size() > 0) {
					for (int i = 0; i < interfaceList.size(); i++) {
						
						AclDetail vo = (AclDetail) interfaceList
								.get(i);
						if (xcount > xlabel) {
							x = x + "\r";
							BigDecimal bd = new BigDecimal(xcount);
							int mod = Integer.parseInt(bd.divide(
									new BigDecimal(xlabel),
									BigDecimal.ROUND_CEILING).toString());
							if (i % mod == 0) {
								dataset.addValue(Double.parseDouble(String
										.valueOf(vo.getMatches())), String.valueOf(vo.getName()),vo.getCollecttime());
							} else {
								dataset.addValue(Double.parseDouble(String
										.valueOf(vo.getMatches())), String.valueOf(vo.getName()),x);
							}
						} else {
							dataset.addValue(Double.parseDouble(String.valueOf(vo.getMatches())), String.valueOf(vo.getName()),vo.getCollecttime());
						}
					}
				}
				String chartkey = ChartCreator.createLineChart(title, xdesc,
						ydesc, dataset, chartWith, chartHigh);
				JFreeChartBrother chart = (JFreeChartBrother) ResourceCenter
						.getInstance().getChartStorage().get(chartkey);

				try {
					File f = new File(chartPath);
					if (!f.exists()) {
						f.createNewFile();
					}
					FileOutputStream fos = new FileOutputStream(f);
					ChartUtilities.writeChartAsPNG(fos, chart.getChart(), chart
							.getWidth(), chart.getHeight());
				} catch (IOException ioe) {
					// chartPath = "";
					SysLogger.error("", ioe);
				}
			}
		}
	return chartPath;
	}
	public void addDataSet(DefaultCategoryDataset dataset, PolicyInterface vo,
			String time) {
		if (vo.getMatchedBytes() != -1) {
			dataset.addValue(Double.parseDouble(String.valueOf(vo
					.getOfferedRate())), vo.getInterfaceName() + ":"
					+ vo.getClassName() + ":bytes matched", time);
		}
		if (vo.getMatchedPkts() != -1) {
			dataset.addValue(Double.parseDouble(String.valueOf(vo
					.getMatchedPkts())), vo.getInterfaceName() + ":"
					+ vo.getClassName() + ":pkts matched", time);

		}
		if (vo.getDropsTotal() != -1) {
			dataset.addValue(Double.parseDouble(String.valueOf(vo
					.getDropsTotal())), vo.getInterfaceName() + ":"
					+ vo.getClassName() + ":total drops", time);

		}
		if (vo.getDropsBytes() != -1) {
			dataset.addValue(Double.parseDouble(String.valueOf(vo
					.getDropsBytes())), vo.getInterfaceName() + ":"
					+ vo.getClassName() + ":bytes drops", time);
		}
		if (vo.getDepth() != -1) {
			dataset.addValue(Double.parseDouble(String.valueOf(vo.getDepth())),
					vo.getInterfaceName() + ":" + vo.getClassName() + ":depth",
					time);
		}
		if (vo.getTotalQueued() != -1) {
			dataset.addValue(Double.parseDouble(String.valueOf(vo
					.getTotalQueued())), vo.getInterfaceName() + ":"
					+ vo.getClassName() + ":total queued", time);
		}
		if (vo.getNoBufferDrop() != -1) {
			dataset.addValue(Double.parseDouble(String.valueOf(vo
					.getNoBufferDrop())), vo.getInterfaceName() + ":"
					+ vo.getClassName() + ":no-buffer drops", time);
		}
	}

	/**
	 * @param dataList
	 * @param ipList
	 * @param title
	 * @param xdesc
	 * @param ydesc
	 * @return
	 */
	private String makeDiskJfreeChartData(ArrayList<String[]> dataList,
			List ipList, String title, String xdesc, String ydesc) {
		String chartPath = ResourceCenter.getInstance().getSysPath() + "temp"
				+ File.separator + System.currentTimeMillis() + ".png";
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		if (dataList != null) {
			for (int j = 0; j < dataList.size(); j++) {
				String[] dataList1 = dataList.get(j);
				if (dataList1 != null) {
					String s = dataList1[1];
					s = s.replace("%", "");
					dataset.addValue(Double.parseDouble(s), "", dataList1[0]);
				}
			}
		}

		String chartkey = ChartCreator.createBarChart(title, xdesc, ydesc,
				dataset, chartWith, chartHigh);
		JFreeChartBrother chart = (JFreeChartBrother) ResourceCenter
				.getInstance().getChartStorage().get(chartkey);
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
	
	 public void exportScheduleReport (String type, String filePath, 

	           String startTime, String toTime, String exportType) { 

	       // ��ű����Ϣ 

	       List resultList = null ; 

	       ArrayList<String[]> tableList = null ; 

	       // ���ͼƬ·�� 

	       String title = " �ճ̱� " ; 

	       if ( "schedule" .equalsIgnoreCase(type)) { 

	           title = " ֵ���ճ̱� " ; 

	           ScheduleReportHelper helper= new ScheduleReportHelper(); 

	           resultList = helper.exportScheduleData(type, filePath, startTime, toTime); 

	       } 

	       String time = startTime + "~" + toTime; 

	       ExportInterface export = null ; 

	       if ( "xls" .equals(exportType)) { 

	           chartWith = 768; 

	           chartHigh = 238; 

	           export = new Excel(filePath); 

	       } else if ( "pdf" .equals(exportType)) { 

	           chartWith = 540; 

	           chartHigh = 240; 

	           export = new Pdf(filePath); 

	       } else if ( "doc" .equals(exportType)) { 

	           chartWith = 540; 

	           chartHigh = 240; 

	           export = new Word(filePath); 

	       } 

	       exportSchedule(export, resultList, title, time); 

	       

	    } 

	  

	  

	    private void exportSchedule (ExportInterface export,  List resultList, String title, String time) { 

	       com.afunms.schedule.dao.PositionDao positionDao = new com.afunms.schedule.dao.PositionDao(); 

	       List<com.afunms.schedule.model.Position> positionList = positionDao.loadAll(); 

	       Map<String,com.afunms.schedule.model.Position> positionMap = new HashMap<String,com.afunms.schedule.model.Position>(); 

	       for (com.afunms.schedule.model.Position position : positionList) { 

	           positionMap.put(position.getId(), position); 

	       } 

	  

	       com.afunms.schedule.dao.ScheduleDao dao = new com.afunms.schedule.dao.ScheduleDao(); 

	       List<String> polist = dao.getPosition( "select distinct position from nms_schedule" ); 

	       

	       if (polist.size() != resultList.size()) { 

	           SysLogger.error ( " �����ļ�ʧ�� " ); 

	           return ; 

	       } 

	       

	       if ( null != resultList && resultList.size() > 0) { 

	           for ( int i = 0; i < resultList.size(); i++) { 

	              String position = positionMap.get(polist.get(i)).getName(); 

	              List regionTableList = (ArrayList)resultList.get(i); 

	              

	              if (regionTableList.get(0) != null ) { 

	                  try { 

	                     export.insertTitle(position + title, 4, time); 

	                  } catch (Exception e) { 

	                     SysLogger.error ( "" , e); 

	                  } 

	              } else { 

	                  try { 

	                     export.insertTitle(title, 0, time); 

	                  } catch (Exception e) { 

	                     SysLogger.error ( "" , e); 

	                  } 

	              } 

	              

	              for ( int j = 0; j < regionTableList.size(); j++) { 

	                  try { 

	                     export.insertTable((ArrayList<String[]>)regionTableList.get(j)); 

	                  } catch (IOException e) { 

	                     SysLogger.error ( "" , e); 

	                  } catch (Exception e) { 

	                     SysLogger.error ( "" , e); 

	                  } 

	              } 

	              

	           } 

	           try { 

	              export.save(); 

	           } catch (Exception e) { 

	              SysLogger.error ( "------ �����ļ�����ʧ�ܣ� ------" , e); 

	           } 

	       } 

	    } 

}
