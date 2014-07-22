<%@page language="java" contentType="text/html;charset=gb2312"%>
<%@ include file="/include/globe.inc"%>
<%@ include file="/include/globeChinese.inc"%>


<%@page import="com.afunms.topology.model.HostNode"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="com.afunms.common.util.SysUtil"%>
<%@page import="com.afunms.common.util.*"%>
<%@page import="com.afunms.monitor.item.*"%>
<%@page import="com.afunms.polling.node.*"%>
<%@page import="com.afunms.polling.*"%>
<%@page import="com.afunms.polling.impl.*"%>
<%@page import="com.afunms.polling.api.*"%>
<%@page import="com.afunms.topology.util.NodeHelper"%>
<%@page import="com.afunms.topology.dao.*"%>
<%@page import="com.afunms.monitor.item.base.MoidConstants"%>
<%@page import="org.jfree.data.general.DefaultPieDataset"%>
<%@ page import="com.afunms.polling.api.I_Portconfig"%>
<%@ page import="com.afunms.polling.om.Portconfig"%>
<%@ page import="com.afunms.polling.om.*"%>
<%@ page import="com.afunms.polling.impl.PortconfigManager"%>
<%@page import="com.afunms.report.jfree.ChartCreator"%>

<%@page import="java.util.*"%>
<%@page import="java.text.*"%>
<%@page import="java.lang.*"%>
<%@page import="com.afunms.monitor.item.base.*"%>
<%@page import="com.afunms.monitor.executor.base.*"%>
<%@page import="com.afunms.config.model.*"%>
<%@page import="com.afunms.config.dao.*"%>

<%@page import="com.afunms.indicators.util.NodeUtil"%>
<%@page import="com.afunms.indicators.model.NodeDTO"%>
<%@page import="com.afunms.detail.net.service.NetService"%>
<%@page import="com.afunms.temp.model.NodeTemp"%>
<%@page import="com.afunms.temp.model.*"%>
<%@ page import="com.afunms.polling.loader.HostLoader"%>

<%
	String temp = "";
	String runmodel = PollingEngine.getCollectwebflag();
	String menuTable = (String) request.getAttribute("menuTable");
	String[] diskItem = { "AllSize", "UsedSize", "Utilization" };
	String[] diskItemch = { "总容量", "已用容量", "利用率" };

	String[] memoryItem = { "Capability", "Utilization" };
	String[] memoryItemch = { "容量", "当前", "最大", "平均" };

	List cpulist = new ArrayList();
	Vector deviceV = new Vector();

	Hashtable memmaxhash = (Hashtable) request
			.getAttribute("memmaxhash");
	Hashtable memavghash = (Hashtable) request
			.getAttribute("memavghash");
	Hashtable diskhash = new Hashtable();
	Hashtable memhash = new Hashtable();

	String memcollecttime = "";
	String pingcollecttime = "";
	String avgcpu = "";
	String cpumax = "";
	Hashtable max = (Hashtable) request.getAttribute("max");
	if (max != null) {
		avgcpu = (String) max.get("cpuavg");
		cpumax = (String) max.get("cpu");
	}
	if (avgcpu.equals("") || avgcpu == null)
		avgcpu = "0";
	if (cpumax.equals("") || cpumax == null)
		cpumax = "0";
	String begindate = "";
	String enddate = "";

	String tmp = request.getParameter("id");
	String _flag = (String) request.getAttribute("flag");
	SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	double cpuvalue = 0;
	String pingconavg = "0";
	String collecttime = "";
	String sysuptime = "";
	String sysservices = "";
	String sysdescr = "";
	String syslocation = "";
	String vvalue = "0";
	String pvalue = "0";
	float capvalue = 0f;
	float fvvalue = 0f;
	String vused = "0";
	String maxpingvalue = "0";
	String responsevalue = "0";
	String pingvalue = "0";

	int cpuper = 0;

	Host host = (Host) PollingEngine.getInstance().getNodeByID(
			Integer.parseInt(tmp));
	if (host == null) {
		//从数据库里获取
		HostNodeDao hostdao = new HostNodeDao();
		HostNode node = null;
		try {
			node = (HostNode) hostdao.findByID(tmp);
		} catch (Exception e) {
		} finally {
			hostdao.close();
		}
		HostLoader loader = new HostLoader();
		loader.loadOne(node);
		loader.close();
		host = (Host) PollingEngine.getInstance().getNodeByID(
				Integer.parseInt(tmp));
	}
	NodeUtil nodeUtil = new NodeUtil();
	NodeDTO nodedto = nodeUtil.creatNodeDTOByNode(host);

	String ipaddress = host.getIpAddress();
	//CommonUtil commutil = new CommonUtil();
	String picip = CommonUtil.doip(ipaddress);
	String[] time = { "", "" };
	DateE datemanager = new DateE();
	Calendar current = new GregorianCalendar();
	current.set(Calendar.MINUTE, 59);
	current.set(Calendar.SECOND, 59);
	time[1] = datemanager.getDateDetail(current);
	current.add(Calendar.HOUR_OF_DAY, -1);
	current.set(Calendar.MINUTE, 0);
	current.set(Calendar.SECOND, 0);
	time[0] = datemanager.getDateDetail(current);
	String starttime = time[0];
	String endtime = time[1];

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	String time1 = sdf.format(new Date());
	String starttime1 = time1 + " 00:00:00";
	String totime1 = time1 + " 23:59:59";
	Vector memoryVector = new Vector();
	if ("0".equals(runmodel)) {
		//采集与访问是集成模式
		Hashtable ipAllData = (Hashtable) ShareData.getSharedata().get(
				host.getIpAddress());
		if (ipAllData != null) {
			Vector cpuV = (Vector) ipAllData.get("cpu");
			if (cpuV != null && cpuV.size() > 0) {
				CPUcollectdata cpu = (CPUcollectdata) cpuV.get(0);
				if (cpu != null && cpu.getThevalue() != null) {
					cpuvalue = new Double(cpu.getThevalue());
				}
			}
			memhash = (Hashtable) request.getAttribute("memhash");
			diskhash = (Hashtable) request.getAttribute("diskhash");
			//得到内存利用率
			memoryVector = (Vector) ipAllData.get("memory");
			if (memoryVector != null && memoryVector.size() > 0) {
				for (int i = 0; i < memoryVector.size(); i++) {
					Memorycollectdata memorydata = (Memorycollectdata) memoryVector
							.get(i);
					if ("VirtualMemory".equalsIgnoreCase(memorydata
							.getSubentity())
							&& "Utilization"
									.equalsIgnoreCase(memorydata
											.getEntity())) {
						vvalue = Math.round(Float.parseFloat(memorydata
								.getThevalue()))
								+ "";
						fvvalue = Float.parseFloat(memorydata
								.getThevalue());
					} else if ("PhysicalMemory"
							.equalsIgnoreCase(memorydata.getSubentity())
							&& "Utilization"
									.equalsIgnoreCase(memorydata
											.getEntity())) {
						pvalue = Math.round(Float.parseFloat(memorydata
								.getThevalue()))
								+ "";
					}
					if ("VirtualMemory".equalsIgnoreCase(memorydata
							.getSubentity())
							&& "Capability".equalsIgnoreCase(memorydata
									.getEntity())) {
						capvalue = Float.parseFloat(memorydata
								.getThevalue());
					}
					if (i == 0) {
						SimpleDateFormat _sdf1 = new SimpleDateFormat(
								"yyyy-MM-dd HH:mm:ss");
						Calendar tempCal = (Calendar) memorydata
								.getCollecttime();
						Date cc = tempCal.getTime();
						//Date d = _sdf1.parse(nodetemp.getCollecttime());
						//Calendar c = Calendar.getInstance();
						//c.setTime(d);
						_sdf1 = new SimpleDateFormat("MM-dd HH:mm");
						memcollecttime = _sdf1.format(cc);
					}
				}
				DecimalFormat df = new DecimalFormat("#.##");
				vused = df.format(capvalue * fvvalue / 100) + "";
			}

			deviceV = (Vector) ipAllData.get("device");

			//得到系统启动时间
			Vector systemV = (Vector) ipAllData.get("system");
			if (systemV != null && systemV.size() > 0) {
				for (int i = 0; i < systemV.size(); i++) {
					Systemcollectdata systemdata = (Systemcollectdata) systemV
							.get(i);
					if (systemdata.getSubentity().equalsIgnoreCase(
							"sysUpTime")) {
						sysuptime = systemdata.getThevalue();
					}
					if (systemdata.getSubentity().equalsIgnoreCase(
							"sysServices")) {
						sysservices = systemdata.getThevalue();
					}
					if (systemdata.getSubentity().equalsIgnoreCase(
							"sysDescr")) {
						sysdescr = systemdata.getThevalue();
					}
				}
			}
			Vector pingData = (Vector) ShareData.getPingdata().get(
					host.getIpAddress());
			if (pingData != null && pingData.size() > 0) {
				Pingcollectdata pingdata = (Pingcollectdata) pingData
						.get(0);
				Calendar tempCal = (Calendar) pingdata.getCollecttime();
				Date cc = tempCal.getTime();
				collecttime = sdf1.format(cc);
				SimpleDateFormat _sdf1 = new SimpleDateFormat(
						"MM-dd HH:mm");
				pingcollecttime = _sdf1.format(cc);
				pingvalue = pingdata.getThevalue();
				pingdata = (Pingcollectdata) pingData.get(1);
				responsevalue = pingdata.getThevalue();
			}
			if (ipAllData != null) {
				//Vector cpuV = (Vector)ipAllData.get("cpu");
				if (cpuV != null && cpuV.size() > 0) {
					CPUcollectdata cpu = (CPUcollectdata) cpuV.get(0);
					if (cpu != null && cpu.getThevalue() != null) {
						cpuvalue = new Double(cpu.getThevalue());
						cpuper = Double.valueOf(cpuvalue).intValue();
					}
					//cpuvalue = cpuper+"";

				}
				cpulist = (List) ipAllData.get("cpulist");
				if (cpulist == null)
					cpulist = new ArrayList();
			}
		}

	} else {
		//采集与访问是分离模式
		List systemList = new ArrayList();
		List pingList = new ArrayList();
		List memoryList = new ArrayList();
		List flashList = new ArrayList();
		List deviceList = new ArrayList();
		try {
			systemList = new NetService(host.getId() + "", nodedto
					.getType(), nodedto.getSubtype()).getSystemInfo();
			pingList = new NetService(host.getId() + "", nodedto
					.getType(), nodedto.getSubtype()).getCurrPingInfo();
			cpuvalue = new Double(new NetService(host.getId() + "",
					nodedto.getType(), nodedto.getSubtype())
					.getCurrCpuAvgInfo());
			cpuper = Double.valueOf(cpuvalue).intValue();
			;
			memoryList = new NetService(host.getId() + "", nodedto
					.getType(), nodedto.getSubtype())
					.getCurrMemoryInfo();
			deviceList = new NetService(host.getId() + "", nodedto
					.getType(), nodedto.getSubtype()).getDeviceInfo();
			//flashList = new NetService(host.getId()+"", nodedto.getType(), nodedto.getSubtype()).getCurrFlashInfo();
			//pingconavg = new NetService(host.getId()+"", nodedto.getType(), nodedto.getSubtype()).getCurrDayPingAvgInfo();
		} catch (Exception e) {
		}
		if (memoryList != null && memoryList.size() > 0) {
			for (int i = 0; i < memoryList.size(); i++) {
				Memorycollectdata memorycollectdata = new Memorycollectdata();
				NodeTemp nodetemp = (NodeTemp) memoryList.get(i);
				if (i == 0) {
					SimpleDateFormat _sdf1 = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");
					Date d = _sdf1.parse(nodetemp.getCollecttime());
					//Calendar c = Calendar.getInstance();
					//c.setTime(d);
					_sdf1 = new SimpleDateFormat("MM-dd HH:mm");
					memcollecttime = _sdf1.format(d);
				}
				memorycollectdata.setSubentity(nodetemp.getSindex());
				memorycollectdata.setEntity(nodetemp.getSubentity());
				memorycollectdata.setThevalue(nodetemp.getThevalue());
				memoryVector.add(memorycollectdata);
			}
		}

		//得到内存利用率
		if (memoryVector != null && memoryVector.size() > 0) {
			for (int i = 0; i < memoryVector.size(); i++) {
				Memorycollectdata memorydata = (Memorycollectdata) memoryVector
						.get(i);
				if ("VirtualMemory".equalsIgnoreCase(memorydata
						.getSubentity())
						&& "Utilization".equalsIgnoreCase(memorydata
								.getEntity())) {
					vvalue = Math.round(Float.parseFloat(memorydata
							.getThevalue()))
							+ "";
					fvvalue = Float
							.parseFloat(memorydata.getThevalue());
				} else if ("PhysicalMemory".equalsIgnoreCase(memorydata
						.getSubentity())
						&& "Utilization".equalsIgnoreCase(memorydata
								.getEntity())) {
					pvalue = Math.round(Float.parseFloat(memorydata
							.getThevalue()))
							+ "";
				}
				if ("VirtualMemory".equalsIgnoreCase(memorydata
						.getSubentity())
						&& "Capability".equalsIgnoreCase(memorydata
								.getEntity())) {
					capvalue = Float.parseFloat(memorydata
							.getThevalue());
				}
			}
			DecimalFormat df = new DecimalFormat("#.##");
			vused = df.format(capvalue * fvvalue / 100) + "";
		}
		if (deviceList != null && deviceList.size() > 0) {
			Devicecollectdata devicedata = null;
			for (int i = 0; i < deviceList.size(); i++) {
				DeviceNodeTemp nodetemp = (DeviceNodeTemp) deviceList
						.get(i);
				devicedata = new Devicecollectdata();
				devicedata.setIpaddress(host.getIpAddress());
				devicedata.setName(nodetemp.getName());
				devicedata.setStatus(nodetemp.getStatus());
				devicedata.setType(nodetemp.getDtype());
				deviceV.addElement(devicedata);
			}
		}

		I_HostLastCollectData hostlastmanager = new HostLastCollectDataManager();
		try {
			memhash = hostlastmanager.getMemory(host.getIpAddress(),
					"Memory", starttime, endtime);
			diskhash = hostlastmanager.getDisk(host.getIpAddress(),
					"Disk", starttime, endtime);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (systemList != null && systemList.size() > 0) {
			for (int i = 0; i < systemList.size(); i++) {
				NodeTemp nodetemp = (NodeTemp) systemList.get(i);
				if ("sysUpTime".equals(nodetemp.getSindex()))
					sysuptime = nodetemp.getThevalue();
				if ("sysDescr".equals(nodetemp.getSindex()))
					sysdescr = nodetemp.getThevalue();
				if ("sysLocation".equals(nodetemp.getSindex()))
					syslocation = nodetemp.getThevalue();
			}
		}
		if (pingList != null && pingList.size() > 0) {
			for (int i = 0; i < pingList.size(); i++) {
				NodeTemp nodetemp = (NodeTemp) pingList.get(i);
				if ("ConnectUtilization".equals(nodetemp.getSindex())) {
					collecttime = nodetemp.getCollecttime();
					pingvalue = nodetemp.getThevalue();
					SimpleDateFormat _sdf1 = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");
					Date d = _sdf1.parse(nodetemp.getCollecttime());
					//Calendar c = Calendar.getInstance();
					//c.setTime(d);
					_sdf1 = new SimpleDateFormat("MM-dd HH:mm");
					pingcollecttime = _sdf1.format(d);
				}
				if ("ResponseTime".equals(nodetemp.getSindex())) {
					responsevalue = nodetemp.getThevalue();
				}
			}
		}
	}

	Hashtable ConnectUtilizationhash = new Hashtable();
	I_HostCollectData hostmanager = new HostCollectDataManager();
	try {
		ConnectUtilizationhash = hostmanager.getCategory(host
				.getIpAddress(), "Ping", "ConnectUtilization",
				starttime1, totime1);
	} catch (Exception ex) {
		ex.printStackTrace();
	}

	if (ConnectUtilizationhash.get("avgpingcon") != null) {
		pingconavg = (String) ConnectUtilizationhash.get("avgpingcon");
		pingconavg = pingconavg.replace("%", "");
		maxpingvalue = (String) ConnectUtilizationhash.get("pingmax");
		maxpingvalue = maxpingvalue.replaceAll("%", "");
	}

	try {
		ConnectUtilizationhash = hostmanager.getCategory(host
				.getIpAddress(), "Ping", "ResponseTime", starttime1,
				totime1);
	} catch (Exception ex) {
		ex.printStackTrace();
	}

	//----得到响应时间 
	String avgresponse = "0";//-----------
	String maxresponse = "0";//----------- 

	try {
		ConnectUtilizationhash = hostmanager.getCategory(host
				.getIpAddress(), "Ping", "ResponseTime", starttime1,
				totime1);
	} catch (Exception ex) {
		ex.printStackTrace();
	}

	if (ConnectUtilizationhash.get("avgpingcon") != null) {
		avgresponse = (String) ConnectUtilizationhash.get("avgpingcon");
		avgresponse = avgresponse.replace("毫秒", "").replaceAll("%", "");
		maxresponse = (String) ConnectUtilizationhash.get("pingmax");
		maxresponse = maxresponse.replaceAll("%", "");
	}

	double avgpingcon = new Double(pingconavg);
	int percent1 = Double.valueOf(avgpingcon).intValue();
	int percent2 = 100 - percent1;

	String rootPath = request.getContextPath();

	SupperDao supperdao = new SupperDao();
	Supper supper = null;
	String suppername = "";
	try {
		supper = (Supper) supperdao.findByID(host.getSupperid() + "");
		if (supper != null)
			suppername = supper.getSu_name() + "("
					+ supper.getSu_dept() + ")";
	} catch (Exception e) {
		e.printStackTrace();
	} finally {
		supperdao.close();
	}

	//生成连通率饼图
	//CreatePiePicture cpp = new CreatePiePicture();
	//double[] data = {avgpingcon, 100-avgpingcon};
	//String[] _labels = {"连通", "未连通"};
	//int[] colors = {0x80ff80, 0xff0000};
	//TitleModel titleModel = new TitleModel();
	//titleModel.setXpic(150);
	//titleModel.setYpic(140);
	//titleModel.setX1(80);
	//titleModel.setX2(50);
	//titleModel.setX3(50);
	//titleModel.setBgcolor(0xffffff);
	//titleModel.setPictype("png");
	//titleModel.setPicName(picip+"ping");
	//titleModel.setTopTitle("");
	//cpp.createPieChartWithLegend(_labels,colors,data,titleModel);  

	CreateMetersPic cmp = new CreateMetersPic();
	cmp.createCpuPic(picip, cpuper); //生成CPU仪表盘
	cmp.createMaxCpuPic(picip, cpumax); //生成CPU最大值仪表盘
	cmp.createAvgCpuPic(picip, avgcpu); //生成CPU平均值仪表盘

	//生成内存利用率图   

	//生成当天平均连通率图形
	CreatePiePicture _cpp = new CreatePiePicture();
	TitleModel _titleModel = new TitleModel();
	_titleModel.setXpic(150);
	_titleModel.setYpic(150);//160, 200, 150, 100
	_titleModel.setX1(75);//外环向左的位置
	_titleModel.setX2(60);//外环向上的位置
	_titleModel.setX3(65);
	_titleModel.setX4(30);
	_titleModel.setX5(75);
	_titleModel.setX6(70);
	_titleModel.setX7(10);
	_titleModel.setX8(115);
	_titleModel.setBgcolor(0xffffff);
	_titleModel.setPictype("png");
	_titleModel.setPicName(picip + "pingavg");
	_titleModel.setTopTitle("");

	double[] _data1 = { avgpingcon, 100 - avgpingcon };
	//double[] _data2 = {77, 87};
	String[] p_labels = { "连通", "未连通" };
	int[] _colors = { 0x66ff66, 0xff0000 };
	String _title1 = "第一季度";
	String _title2 = "第二季度";
	_cpp.createOneRingChart(_data1, p_labels, _colors, _titleModel);

	//生成当前连通率图形
	CreatePiePicture ping_cpp = new CreatePiePicture();
	TitleModel ping_titleModel = new TitleModel();
	ping_titleModel.setXpic(150);
	ping_titleModel.setYpic(150);//160, 200, 150, 100
	ping_titleModel.setX1(75);//外环向左的位置
	ping_titleModel.setX2(60);//外环向上的位置
	ping_titleModel.setX3(65);
	ping_titleModel.setX4(30);
	ping_titleModel.setX5(75);
	ping_titleModel.setX6(70);
	ping_titleModel.setX7(10);
	ping_titleModel.setX8(115);
	ping_titleModel.setBgcolor(0xffffff);
	ping_titleModel.setPictype("png");
	ping_titleModel.setPicName(picip + "realping");
	ping_titleModel.setTopTitle("");

	double[] ping_data1 = { avgpingcon, 100 - avgpingcon };
	String[] ping_labels = { "连通", "未连通" };
	int[] ping_colors = { 0x66ff66, 0xff0000 };
	_cpp.createOneRingChart(ping_data1, ping_labels, ping_colors,
			ping_titleModel);

	//生成最小连通率图形
	ping_titleModel = new TitleModel();
	ping_titleModel.setXpic(150);
	ping_titleModel.setYpic(150);//160, 200, 150, 100
	ping_titleModel.setX1(75);//外环向左的位置
	ping_titleModel.setX2(60);//外环向上的位置
	ping_titleModel.setX3(65);
	ping_titleModel.setX4(30);
	ping_titleModel.setX5(75);
	ping_titleModel.setX6(70);
	ping_titleModel.setX7(10);
	ping_titleModel.setX8(115);
	ping_titleModel.setBgcolor(0xffffff);
	ping_titleModel.setPictype("png");
	ping_titleModel.setPicName(picip + "minping");
	ping_titleModel.setTopTitle("");
	double d_maxping = new Double(maxpingvalue);
	double[] minping_data1 = { d_maxping, 100 - d_maxping };
	//String[] ping_labels = {"连通", "未连通"};
	//int[] ping_colors = {0x66ff66, 0xff0000}; 
	_cpp.createOneRingChart(minping_data1, ping_labels, ping_colors,
			ping_titleModel);

	CreateBarPic cbp = new CreateBarPic();
	TitleModel tm = new TitleModel();

	cbp = new CreateBarPic();

	double[] r_data1 = { new Double(responsevalue),
			new Double(maxresponse), new Double(avgresponse) };
	String[] r_labels = { "当前响应时间(ms)", "最大响应时间(ms)", "平均响应时间(ms)" };
	tm = new TitleModel();
	tm.setPicName(picip + "response");//
	tm.setBgcolor(0xffffff);
	tm.setXpic(450);//图片长度
	tm.setYpic(180);//图片高度
	tm.setX1(30);//左面距离
	tm.setX2(20);//上面距离
	tm.setX3(400);//内图宽度
	tm.setX4(130);//内图高度
	tm.setX5(10);
	tm.setX6(115);
	cbp.createTimeBarPic(r_data1, r_labels, tm, 40);
	tm.setPicName(picip + "response1");//
	//cbp.createRoundTimeBarPic(r_data1, r_labels, tm, 40);
/**
	CreateBarPic disk_cbp = new CreateBarPic();
	double[] disk_data1 = new double[diskhash.size()];
	double[] disk_data2 = new double[diskhash.size()];
	String[] disk_labels = new String[diskhash.size()];

	for (int k = 0; k < diskhash.size(); k++) {
		Hashtable dhash = (Hashtable) (diskhash.get(new Integer(k)));

		String name = "";
		if (dhash.get("name") != null) {
			name = (String) dhash.get("name");
			// System.out.println("====="+name);
		}
		for (int j = 0; j < diskItem.length; j++) {
			String value = "";
			if (dhash.get(diskItem[j]) != null) {

				value = (String) dhash.get(diskItem[j]);
				disk_data1[k] = new Double(value.replaceAll("G", "")
						.replaceAll("%", "").replaceAll("M", ""));
				disk_data2[k] = 100 - disk_data1[k];
				disk_labels[k] = name;

			}

		}
	}

	TitleModel disk_tm = new TitleModel();
	disk_tm.setPicName(picip + "disk");//
	disk_tm.setBgcolor(0x000000);
	disk_tm.setXpic(450);
	disk_tm.setYpic(170);
	disk_tm.setX1(50);
	disk_tm.setX2(20);
	disk_tm.setX3(360);
	disk_tm.setX4(100);
	disk_tm.setX5(160);
	disk_tm.setX6(140);
	int disk_color1 = 0x80ff80;
	int disk_color2 = 0x8080ff;
	disk_cbp.createCylindricalPic(disk_data1, disk_data2, disk_labels,
			disk_tm, "已使用", "未使用", disk_color1, disk_color2);
	**/	
	  //磁盘的利用率	
	 CreateAmColumnPic amColumnPic=new CreateAmColumnPic();
	 String dataStr=amColumnPic.createWinDiskChart(diskhash);
			
    //amchar 连通率
   Double realValue = Double.valueOf(pingvalue.replaceAll("%", ""));
   StringBuffer dataStr1 = new StringBuffer();
	dataStr1.append("连通;").append(Math.round(realValue)).append(";false;7CFC00\\n");
	dataStr1.append("未连通;").append(100-Math.round(realValue)).append(";false;FF0000\\n");
	String realdata = dataStr1.toString();
			StringBuffer dataStr2 = new StringBuffer();
	dataStr2.append("连通;").append(Math.round(avgpingcon)).append(";false;7CFC00\\n");
	dataStr2.append("未连通;").append(100-Math.round(avgpingcon)).append(";false;FF0000\\n");
	String avgdata = dataStr2.toString();
			StringBuffer dataStr3 = new StringBuffer();
			String maxping=maxpingvalue.replaceAll("%", "");
	dataStr3.append("连通;").append(Math.round(Float.parseFloat(maxping))).append(";false;7CFC00\\n");
	dataStr3.append("未连通;").append(100-Math.round(Float.parseFloat(maxping))).append(";false;FF0000\\n");
	String maxdata = dataStr3.toString();
	%>
<html>
	<head>

		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<script type="text/javascript"
			src="<%=rootPath%>/include/swfobject.js"></script>
		<script language="JavaScript" type="text/javascript"
			src="<%=rootPath%>/include/navbar.js"></script>

		<link href="<%=rootPath%>/resource/<%=com.afunms.common.util.CommonAppUtil.getSkinPath() %>css/global/global.css"
			rel="stylesheet" type="text/css" />
		<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script>

		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

		<link rel="stylesheet" type="text/css"
			href="<%=rootPath%>/js/ext/lib/resources/css/ext-all.css"
			charset="utf-8" />
		<link rel="stylesheet" type="text/css"
			href="<%=rootPath%>/js/ext/css/common.css" charset="utf-8" />
		<script type="text/javascript"
			src="<%=rootPath%>/js/ext/lib/adapter/ext/ext-base.js"
			charset="utf-8"></script>
		<script type="text/javascript"
			src="<%=rootPath%>/js/ext/lib/ext-all.js" charset="utf-8"></script>
		<script type="text/javascript"
			src="<%=rootPath%>/js/ext/lib/locale/ext-lang-zh_CN.js"
			charset="utf-8"></script>
		<script type="text/javascript"
			src="<%=rootPath%>/resource/js/jquery-1.4.2.min.js"></script>
		<script type="text/javascript"
			src="<%=rootPath%>/resource/xml/flush/amcolumn/swfobject.js"></script>
		<script>
function createQueryString(){
		var user_name = encodeURI($("#user_name").val());
		var passwd = encodeURI($("#passwd").val());
		var queryString = {userName:user_name,passwd:passwd};
		return queryString;
	}
function gzmajax(){
$.ajax({
			type:"GET",
			dataType:"json",
			url:"<%=rootPath%>/serverAjaxManager.ajax?action=ajaxUpdate_availability&tmp=<%=tmp%>&nowtime="+(new Date()),
			success:function(data){
				 $("#pingavg").attr({ src: "<%=rootPath%>/resource/image/jfreechart/reportimg/<%=picip%>pingavg.png?nowtime="+(new Date()), alt: "平均连通率" });
				$("#cpu").attr({ src: "<%=rootPath%>/resource/image/jfreechart/reportimg/<%=picip%>cpu.png?nowtime="+(new Date()), alt: "当前CPU利用率" }); 
			
			}
		});
}
$(document).ready(function(){
	//$("#testbtn").bind("click",function(){
	//	gzmajax();
	//});
	$('#liantong-detail').hide();
	$('#xiangying-detail').hide();
	$('#xiangxi-detail').hide();
	$('#cipan-detail').hide();
	$('#neicun-detail').hide();
	$('#liantong').bind('click',function(){
			$('#liantong-detail').slideToggle('slow');
		});
	$('#xiangying').bind('click',function(){
			$('#xiangying-detail').slideToggle('slow');
		});
	$('#xiangxi').bind('click',function(){
			$('#xiangxi-detail').slideToggle('slow');
		});
	$('#cipan').bind('click',function(){
			$('#cipan-detail').slideToggle('slow');
		});
	$('#neicun').bind('click',function(){
			$('#neicun-detail').slideToggle('slow');
		});
setInterval(gzmajax,60000);
});
</script>

		<script>
function createQueryString(){
		var user_name = encodeURI($("#user_name").val());
		var passwd = encodeURI($("#passwd").val());
		var queryString = {userName:user_name,passwd:passwd};
		return queryString;
	}
function gzmajax2(){
$.ajax({
			type:"GET",
			dataType:"json",
			url:"<%=rootPath%>/serverAjaxManager.ajax?action=ajaxUpdate_memory&tmp=<%=tmp%>&nowtime="+(new Date()),
			success:function(data){
			var vvalue=data.vvalue;
			var pvalue=data.pvalue;
			var pppvalue=100-pvalue;
			var vvvvalue=100-vvalue;
			var vused=data.vused;
			$('#PFTABLE').html('<tr><td width="'+vvalue+'%" bgcolor="green" align=center>&nbsp;&nbsp;'+vused+'GB</td><td width="'+pppvalue+'%" bgcolor=#ffffff></td></tr>');
			$('#WLTABLE').html('<tr><td width="'+pvalue+'%" bgcolor="green" align=center>&nbsp;&nbsp;'+pvalue+'%</td><td width="'+pppvalue+'%" bgcolor=#ffffff></td></tr>');
			$('#XNTABLE').html('<tr><td width="'+vvalue+'%" bgcolor="green" align=center>&nbsp;&nbsp;'+vvalue+'%</td><td width="'+vvvvalue+'%" bgcolor=#ffffff></td></tr>');
			}
		});
}
$(document).ready(function(){
	//$("#testbtn").bind("click",function(){
	//gzmajax();
	//});
setInterval(gzmajax2,60000);
});
</script>

		<script type="text/javascript">
	function closetime(){
		Ext.MessageBox.hide();
	}
	function gzmajax_PF(){
	Ext.MessageBox.wait('数据加载中，请稍后.. ');
	$.ajax({
			type:"GET",
			dataType:"json",
			url:"<%=rootPath%>/serverAjaxManager.ajax?action=ajaxMemory_fresh&tmp=<%=tmp%>&nowtime="+(new Date()),
			success:function(data){
			var vvalue=data.vvalue;
			var pvalue=data.pvalue;
			var pppvalue=100-pvalue;
			var vused=data.vused;
			$('#PFTABLE').html('<tr><td width="'+vvalue+'%" bgcolor="green" align=center>&nbsp;&nbsp;'+vused+'GB</td><td width="'+pppvalue+'%" bgcolor=#ffffff></td></tr>');
			}
		});
}
	function gzmajax_WL(){
	Ext.MessageBox.wait('数据加载中，请稍后.. ');
	$.ajax({
			type:"GET",
			dataType:"json",
			url:"<%=rootPath%>/serverAjaxManager.ajax?action=ajaxMemory_fresh&tmp=<%=tmp%>&nowtime="+(new Date()),
			success:function(data){
			var vvalue=data.vvalue;
			var pvalue=data.pvalue;
			var pppvalue=100-pvalue;
			var vused=data.vused;
			$('#WLTABLE').html('<tr><td width="'+pvalue+'%" bgcolor="green" align=center>&nbsp;&nbsp;'+pvalue+'%</td><td width="'+pppvalue+'%" bgcolor=#ffffff></td></tr>');
			}
		});
}
	function gzmajax_XN(){
	Ext.MessageBox.wait('数据加载中，请稍后.. ');
	$.ajax({
			type:"GET",
			dataType:"json",
			url:"<%=rootPath%>/serverAjaxManager.ajax?action=ajaxMemory_fresh&tmp=<%=tmp%>&nowtime="+(new Date()),
			success:function(data){
			var vvalue=data.vvalue;
			var pvalue=data.pvalue;
			var pppvalue=100-pvalue;
			var vvvvalue=100-vvalue;
			var vused=data.vused;
			$('#XNTABLE').html('<tr><td width="'+vvalue+'%" bgcolor="green" align=center>&nbsp;&nbsp;'+vvalue+'%</td><td width="'+vvvvalue+'%" bgcolor=#ffffff></td></tr>');
			}
		});
}
	function fresh_PF(){
		gzmajax_PF();
		setTimeout(closetime,2000);
	}
	function fresh_WL(){
		gzmajax_WL();
		setTimeout(closetime,2000);
	}
	function fresh_XN(){
		gzmajax_XN();
		setTimeout(closetime,2000);
	}
</script>

		<script language="javascript">	

  function doQuery()
  {  
     if(mainForm.key.value=="")
     {
     	alert("请输入查询条件");
     	return false;
     }
     mainForm.action = "<%=rootPath%>/network.do?action=find";
     mainForm.submit();
  }
  
    function toGetConfigFile()
  {
        msg.style.display="block";
        mainForm.action = "<%=rootPath%>/cfgfile.do?action=getcfgfile&ipaddress=<%=host.getIpAddress()%>";
        mainForm.submit();
  }
  
  function doChange()
  {
     if(mainForm.view_type.value==1)
        window.location = "<%=rootPath%>/topology/network/index.jsp";
     else
        window.location = "<%=rootPath%>/topology/network/port.jsp";
  }

  function toAdd()
  {
      mainForm.action = "<%=rootPath%>/network.do?action=ready_add";
      mainForm.submit();
  }
  
function changeOrder(para){
  	location.href="<%=rootPath%>/topology/network/networkview.jsp?id=<%=tmp%>&ipaddress=<%=host.getIpAddress()%>&orderflag="+para;
} 
function openwin3(operate,index,ifname) 
{	
        window.open ("<%=rootPath%>/monitor.do?action="+operate+"&id=<%=tmp%>&ipaddress=<%=host.getIpAddress()%>&ifindex="+index+"&ifname="+ifname, "newwindow", "height=500, width=600, toolbar= no, menubar=no, scrollbars=yes, resizable=yes, location=yes, status=yes") 
} 

function cpuReportByMinute(){
	window.open("<%=rootPath%>/monitor.do?action=hostcpu_report&id=<%=tmp%>&timeType=minute","newWindow","height=300, width=800, top=0, left= 200,screenX=0,screenY=0");
  }
  
  
  
  function memoryReportByMinute(){
	window.open("<%=rootPath%>/monitor.do?action=hostmemory_report&id=<%=tmp%>&timeType=minute","newWindow","height=300, width=800, top=0, left= 200,screenX=0,screenY=0");
   
  }
// 全屏观看
function gotoFullScreen() {
	parent.mainFrame.resetProcDlg();
	var status = "toolbar=no,height="+ window.screen.height + ",";
	status += "width=" + (window.screen.width-8) + ",scrollbars=no";
	status += "screenX=0,screenY=0";
	window.open("topology/network/index.jsp", "fullScreenWindow", status);
	parent.mainFrame.zoomProcDlg("out");
}

</script>
		<script language="JavaScript" type="text/JavaScript">
var show = true;
var hide = false;
//修改菜单的上下箭头符号
function my_on(head,body)
{
	var tag_a;
	for(var i=0;i<head.childNodes.length;i++)
	{
		if (head.childNodes[i].nodeName=="A")
		{
			tag_a=head.childNodes[i];
			break;
		}
	}
	tag_a.className="on";
}
function my_off(head,body)
{
	var tag_a;
	for(var i=0;i<head.childNodes.length;i++)
	{
		if (head.childNodes[i].nodeName=="A")
		{
			tag_a=head.childNodes[i];
			break;
		}
	}
	tag_a.className="off";
}
//添加菜单	
function initmenu()
{
	var idpattern=new RegExp("^menu");
	var menupattern=new RegExp("child$");
	var tds = document.getElementsByTagName("div");
	for(var i=0,j=tds.length;i<j;i++){
		var td = tds[i];
		if(idpattern.test(td.id)&&!menupattern.test(td.id)){					
			menu =new Menu(td.id,td.id+"child",'dtu','100',show,my_on,my_off);
			menu.init();		
		}
	}
	setClass();

}

function setClass(){
	document.getElementById('hostDetailTitle-10').className='detail-data-title';
	document.getElementById('hostDetailTitle-10').onmouseover="this.className='detail-data-title'";
	document.getElementById('hostDetailTitle-10').onmouseout="this.className='detail-data-title'";
}

function refer(action){
		document.getElementById("id").value="<%=tmp%>";
		var mainForm = document.getElementById("mainForm");
		mainForm.action = '<%=rootPath%>' + action;
		mainForm.submit();
}

//网络设备的ip地址
function modifyIpAliasajax(ipaddress){
	var t = document.getElementById('ipalias'+ipaddress);
	var ipalias = t.options[t.selectedIndex].text;//获取下拉框的值
	$.ajax({
			type:"GET",
			dataType:"json",
			url:"<%=rootPath%>/networkDeviceAjaxManager.ajax?action=modifyIpAlias&ipaddress="+ipaddress+"&ipalias="+ipalias,
			success:function(data){
				window.alert("修改成功！");
			}
		});
}
$(document).ready(function(){
	//$("#testbtn").bind("click",function(){
	//	gzmajax();
	//});
//setInterval(modifyIpAliasajax,60000);
});

// zhubinhua  add   
//用于在服务器上生成对应Flex图片,,然后生成word文档  下载
function outputWord() {
	//alert("YES!");
	//先生成图片
	window["Area_flux"].createImage();
	window["Response_time"].createImage();
	window["Area_Disk"].createImage();
	window["Column_Disk"].createImage();
	window["Line_CPU"].createImage();
	window["Ping"].createImage();
	
	//window.open("<%=rootPath%>/monitor.do?action=createWord");
	var a = document.getElementById("outputWord");
	a.href="<%=rootPath%>/monitor.do?action=createWord&whattype=host&id=<%=tmp%>";
}
</script>


	</head>
	<body id="body" class="body" onload="initmenu();">
		<form id="mainForm" method="post" name="mainForm">
		<input type=hidden name="orderflag">
		<input type=hidden name="id">
		
		<table id="body-container" class="body-container">
			<tr>
				<td class="td-container-menu-bar">
					<table id="container-menu-bar" class="container-menu-bar">
						<tr>
							<td>
								<%=menuTable%>
							</td>	
						</tr>
					</table>
				</td>
			
				<td class="td-container-main">
					<table id="container-main" class="container-main" >
						<tr>
							<td class="td-container-main-detail"  width=98%> 
								<table id="container-main-detail" class="container-main-detail" >
										<tr>
										<td> 
											<table id="detail-content" class="detail-content" width="100%" background="<%=rootPath%>/common/images/right_t_02.jpg" cellspacing="0" cellpadding="0">
				<tr>
					<td align="left"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
					<td class="layout_title"><b>设备详细信息</b></td>
					<td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
				</tr>
				<tr>
				 <td width="20%" height="26" align="left" nowrap  class=txtGlobal>&nbsp;设备标签:
										<!--  	<td width="70%"><%=host.getAlias()%> [<a href="<%=rootPath%>/network.do?action=ready_editalias&id=<%=tmp%>&ipaddress=<%=host.getIpAddress()%>"><img src="<%=rootPath%>/resource/image/editicon.gif" border=0>修改</a>]</td>-->
										        <span id="lable"><%=host.getAlias()%></span> </td>
                         <td width="20%" height="26" align="left" nowrap class=txtGlobal >&nbsp;系统名称:
											<span id="sysname"><%=host.getSysName()%></span></td>
                         <td width="20%" height="26" align=left valign=middle nowrap class=txtGlobal>&nbsp;IP地址:
											<%
												 IpAliasDao ipdao = new IpAliasDao();
												 List iplist = ipdao.loadByIpaddress(host.getIpAddress());
												 ipdao.close();
												 ipdao = new IpAliasDao();
												 IpAlias ipalias = ipdao.getByIpAndUsedFlag(host.getIpAddress(),"1");
												 ipdao.close();
												 if(iplist == null)iplist = new ArrayList(); %>
											
												<select name="ipalias<%=host.getIpAddress() %>">
													<option selected><%=host.getIpAddress() %></option>
													<% 
													   for(int j=0 ;j<iplist.size() ; j++){
																IpAlias voTemp = (IpAlias)iplist.get(j); 
															%>
															<option <%if(ipalias != null && ipalias.getAliasip().equals(voTemp.getAliasip())){ %>selected<%} %>><%=voTemp.getAliasip() %></option>
													<%} %>
												</select>
												[
												<a href="#"  style="cursor:hand" onclick="modifyIpAliasajax('<%=host.getIpAddress()%>');">修改</a>
												]
											</td>
				</tr>
			</table>
										</td>
									</tr>
									<tr>
										<td>
											<table id="detail-data" class="detail-data">
												<tr>
											    	<td class="detail-data-header">
											    		<%=hostDetailTitleTable%>
											    	</td>
											  </tr>
											  <tr>
											      
											    	<td>
											    		<table class="detail-data-body">
												      		<tr>
												      			<td>
													      			<table width="100%" border="0" cellpadding="0" cellspacing="0">
   	<tr>
										<td> 
											<jsp:include page="/topology/includejsp/systeminfo_host.jsp">
													<jsp:param name="rootPath" value="<%=rootPath%>" />
													<jsp:param name="tmp" value="<%=tmp%>" />
													<jsp:param name="sysdescr" value="<%=sysdescr%>" />
													<jsp:param name="sysuptime" value="<%=sysuptime%>" />
													<jsp:param name="sysuptime" value="<%=sysuptime%>" />
													<jsp:param name="collecttime" value="<%=collecttime%>" />
													<jsp:param name="pvalue" value="<%=pvalue%>" />
													<jsp:param name="vvalue" value="<%=vvalue%>" />
													<jsp:param name="vused" value="<%=vused%>" />
													<jsp:param name="picip" value="<%=picip%>" />
													<jsp:param name="pingavg" value="<%=Math.round(Float.parseFloat(pingconavg))%>"/>
													<jsp:param name="avgresponse" value="<%=avgresponse%>" />
												</jsp:include>
										</td>
										
									</tr>
                      
                      
                      
                        </table>   
														       	</td>
												      		</tr>
											    		</table>
											    </td>
											  </tr>
											</table>
										</td>
									</tr>
									</table>
									</table>
									
							</td>
							
							<%
									String subtype = "windows";
									if (host.getSysOid().indexOf("1.3.6.1.4.1.2021") >= 0
											|| host.getSysOid().indexOf("1.3.6.1.4.1.8072") >= 0) {
										subtype = "linux";
									}
								%>
							<td class="td-container-main-tool" width='15%' >
								<jsp:include page="/include/toolbar.jsp">
										<jsp:param value="<%=host.getIpAddress()%>" name="ipaddress" />
										<jsp:param value="<%=host.getSysOid()%>" name="sys_oid" />
										<jsp:param value="<%=tmp%>" name="tmp" />
										<jsp:param value="host" name="category" />
										<jsp:param value="<%=subtype%>" name="subtype" />
									</jsp:include>
							</td>
						</tr>
					
					</table>
				</td>
			</tr>
		</table>
	</form>
	</BODY>
</HTML>
