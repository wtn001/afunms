<%@page language="java" contentType="text/html;charset=gb2312"%>
<%@ include file="/include/globe.inc"%>
<%@ include file="/include/globeChinese.inc"%>
<%@page import="com.afunms.topology.util.NodeHelper"%>
<%@page import="com.afunms.polling.node.Tomcat"%>
<%@page import="com.afunms.common.util.*"%>
<%@page import="java.util.*"%>
<%@page import="java.text.*"%>
<%@page import="com.afunms.application.manage.TomcatManager"%>
<%@ page import="com.afunms.detail.service.tomcatInfo.TomcatInfoService"%>
<%@page import="com.afunms.initialize.*"%>
<%
    String rootPath = request.getContextPath();
    String runmodel = PollingEngine.getCollectwebflag();
    String tmp = request.getParameter("tmp");
    String avgpingStr = request.getParameter("avgpingcon") == null ? "0" : request.getParameter("avgpingcon");
    double avgpingcon = Double.parseDouble(avgpingStr);
    String avgjvm = request.getParameter("avgjvmcon") == null ? "0" : request.getParameter("avgjvmcon");
    double avgjvmcon = Double.parseDouble(avgjvm);
    double jvm_memoryuiltillize = 0;
    double tomcatping = 0;
    String lasttime;
    String nexttime;
    String jvm = "";
    String jvm_utilization = "";
    Hashtable data_ht = new Hashtable();
    Hashtable pollingtime_ht = new Hashtable();
    TomcatManager tm = new TomcatManager();
    Tomcat tomcat = (Tomcat) PollingEngine.getInstance().getTomcatByID(Integer.parseInt(tmp));
    Hashtable hash_data = null;
    if ("0".equals(runmodel)) {
        //�ɼ�������Ǽ���ģʽ
        Hashtable tomcatvalues = ShareData.getTomcatdata();
        if (tomcatvalues != null && tomcatvalues.containsKey(tomcat.getIpAddress()+":"+tomcat.getId())) {
            data_ht = (Hashtable) tomcatvalues.get(tomcat.getIpAddress()+":"+tomcat.getId());
        }
    } else {
        //�ɼ�����ʷ���ģʽ
        TomcatInfoService tomcatInfoService = new TomcatInfoService();
        data_ht = tomcatInfoService.getTomcatDataHashtable(tmp);
    }

    tomcatping = (double) tm.tomcatping(tomcat.getId());
    pollingtime_ht = tm.getCollecttime(tomcat.getIpAddress());

    if (pollingtime_ht != null) {
        lasttime = (String) pollingtime_ht.get("lasttime");
        nexttime = (String) pollingtime_ht.get("nexttime");
    } else {
        lasttime = null;
        nexttime = null;
    }
    if (data_ht != null) {
        if (data_ht.get("jvm") != null)
            jvm = (String) data_ht.get("jvm");
        if (data_ht.get("jvm_utilization") != null) {
            jvm_utilization = (String) data_ht.get("jvm_utilization");
        }

    } else {
        jvm = "";
    }
    int percent1 = Double.valueOf(tomcatping).intValue();
    int percent2 = 100 - percent1;
    String cpuper = "0";
    cpuper = jvm_utilization;

    CreateMetersPic cmp = new CreateMetersPic();
    String path = ResourceCenter.getInstance().getSysPath() + "resource\\image\\dashboard1.png";
    cmp.createPic(tmp, avgjvmcon, path, "JVM������", "tomcat_jvm");

    StringBuffer dataStr = new StringBuffer();
    dataStr.append("��ͨ;").append(Math.round(avgpingcon)).append(";false;7CFC00\\n");
    dataStr.append("δ��ͨ;").append(100 - Math.round(avgpingcon)).append(";false;FF0000\\n");
    String avgdata = dataStr.toString();
    if (data_ht.get("server") != null) {
        String info = (String) data_ht.get("server");
        String[] seperator = info.split(",");
        tomcat.setVersion(seperator[0]);
        tomcat.setJvmversion(seperator[1]);
        tomcat.setJvmvender(seperator[2]);
        tomcat.setOs(seperator[3]);
        tomcat.setOsversion(seperator[4]);
    }
    if (tomcat.getLastAlarm() == null || tomcat.getLastAlarm() == "") {
        tomcat.setLastAlarm("��");
    }
%>
<table id="service-detail-content" class="service-detail-content">
	<tr>
		<td>
			<jsp:include page="/topology/includejsp/detail_content_top.jsp">
				<jsp:param name="contentTitle" value="Tomcat��Ϣ" />
			</jsp:include>
		</td>
	</tr>
	<tr>
		<td>
			<table width="100%"
				background="<%=rootPath%>/common/images/right_t_02.jpg"
				cellspacing="0" cellpadding="0">
				<tr>
					<td height="29" style="padding-left: 20px;">
						����:<%=tomcat.getAlias()%>
					</td>
					<td height="29">
						IP��ַ:<%=tomcat.getIpAddress()%>
					</td>
					<td height="29">
						�˿�:<%=tomcat.getPort()%>
					</td>
					<td height="29">
						״̬:
						<img
							src="<%=rootPath%>/resource/<%=NodeHelper.getCurrentStatusImage(tomcat.getStatus())%>">
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td>
			<table id="service-detail-content-body"
				class="service-detail-content-body">
				<tr>
					<td>
						<table style="BORDER-COLLAPSE: collapse" bordercolor=#cedefa
							rules=none align=center border=1 cellpadding=0 cellspacing="0"
							width=100%>
							<tr>
								<td width="80%" align="left" valign="top">
									<table>
										<tr height="29">
											<td align="left">
												Tomcat�汾:
											</td>
											<td align="left"><%=data_ht.get("serverInfo")%></td>
											<td align="left">
												JDK�汾:
											</td>
											<td align="left"><%=data_ht.get("implementationVersion")%></td>
											<td align="left">
												JDK��Ӧ��:
											</td>
											<td align="left"><%=data_ht.get("vmVendor")%></td>
										</tr>
										<tr bgcolor="#F1F1F1" height="29">
											<td align="left">
												�����:
											</td>
											<td align="left">
												<%=data_ht.get("vmNameVer")%>
											</td>
											<td align="left">
												����ʱ��:
											</td>
											<td align="left">
												<%=data_ht.get("startTime")%>
											</td>
											<td align="left">
												����ʱ��:
											</td>
											<td align="left">
												<%=data_ht.get("upTime")%>
											</td>
										</tr>
										<tr>
											<td colspan=6>
												<hr size="2px" color="gray">
											</td>
										</tr>
										<tr height="29">
											<td align="left">
												����ϵͳ:
											</td>
											<td><%=data_ht.get("operatingSystemName")%></td>
											<td align="left">
												�����ڴ�����(Mb):
											</td>
											<%
											    String total = (String) data_ht.get("totalPhysicalMemorySize");
											    long totalPhysicalMemorySize = -1;
											    if (null != total) {
											        totalPhysicalMemorySize = Long.parseLong(total) / 1024 / 1024;
											    }
											%>
											<td><%=totalPhysicalMemorySize%></td>
											<td align="left">
												���������ڴ�(Mb):
											</td>
											<%
											    String free = (String) data_ht.get("freePhysicalMemorySize");
											    long freePhysicalMemorySize = -1;
											    if (null != total) {
											        freePhysicalMemorySize = Long.parseLong(free) / 1024 / 1024;
											    }
											%>
											<td><%=freePhysicalMemorySize%></td>
										</tr>
										<tr bgcolor="#F1F1F1" height="29">
											<td align="left">
												�����ռ�����(Mb):
											</td>
											<%
											    total = (String) data_ht.get("totalSwapSpaceSize");
											    long totalSwapSpaceSize = -1;
											    if (null != total) {
											        totalSwapSpaceSize = Long.parseLong(total) / 1024 / 1024;
											    }
											%>
											<td><%=totalSwapSpaceSize%></td>
											<td align="left">
												���ý����ռ�(Mb):
											</td>
											<%
											    free = (String) data_ht.get("freeSwapSpaceSize");
											    long freeSwapSpaceSize = -1;
											    if (null != total) {
											        freeSwapSpaceSize = Long.parseLong(free) / 1024 / 1024;
											    }
											%>
											<td><%=freeSwapSpaceSize%></td>
											<td align="left">
												���������ڴ�(Mb):
											</td>
											<%
											    String committed = (String) data_ht.get("committedVirtualMemorySize");
											    long committedVirtualMemorySize = -1;
											    if (null != committed) {
											        committedVirtualMemorySize = Long.parseLong(committed) / 1024 / 1024;
											    }
											%>
											<td><%=committedVirtualMemorySize%></td>
										</tr>
										<tr>
											<td height="29" align=left nowrap class=txtGlobal>
												��ϵ�ܹ�:
											</td>
											<td><%=data_ht.get("arch")%></td>
											<td height="29" align=left nowrap class=txtGlobal>
												����CPU��:
											</td>
											<td><%=data_ht.get("availableProcessors")%></td>
											<td height="29" align=left nowrap class=txtGlobal>
												����Cpuʱ��:
											</td>
											<td><%=data_ht.get("processCpuTime")%></td>
										</tr>
										<tr>
											<td colspan=6>
												<hr size="2px" color="gray">
											</td>
										</tr>
										<tr bgcolor="#F1F1F1">
											<td height="29" align=left nowrap class=txtGlobal>
												��߳�:
											</td>
											<td><%=data_ht.get("ThreadCount")%></td>
											<td height="29" align=left nowrap class=txtGlobal>
												��:
											</td>
											<td><%=data_ht.get("PeakThreadCount")%></td>
											<td height="29" align=left nowrap class=txtGlobal>
												�ػ��߳�:
											</td>
											<td><%=data_ht.get("DaemonThreadCount")%></td>
										</tr>
										<tr>
											<td height="29" align=left nowrap class=txtGlobal>
												�������߳�:
											</td>
											<td><%=data_ht.get("TotalStartedThreadCount")%></td>
											<td height="29" align=left nowrap class=txtGlobal>
												��ǰװ����:
											</td>
											<td><%=data_ht.get("loadedClassCount")%></td>
											<td height="29" align=left nowrap class=txtGlobal>
												��װ��������:
											</td>
											<td><%=data_ht.get("totalLoadedClassCount")%></td>
										</tr>
										<tr bgcolor="#F1F1F1">
											<td height="29" align=left nowrap class=txtGlobal>
												��ж����:
											</td>
											<td colspan="5"><%=data_ht.get("unloadedClassCount")%></td>
										</tr>
										<tr>
											<td colspan=6>
												<hr size="2px" color="gray">
											</td>
										</tr>
										<tr>
											<td height="29" nowrap class=txtGlobal>
												��ǰ�Ѵ�С(MB):
											</td>
											<%
											    long heapUsedMemory = -1;
											    if (null != data_ht.get("heapUsedMemory")) {
											        heapUsedMemory = (Long) data_ht.get("heapUsedMemory");
											        heapUsedMemory = heapUsedMemory / 1024 / 1024;
											    }
											%>
											<td><%=heapUsedMemory%></td>
											<td height="29" nowrap class=txtGlobal>
												������ڴ�(MB):
											</td>
											<%
											    long heapCommitMemory = -1;
											    if (null != data_ht.get("heapCommitMemory")) {
											        heapCommitMemory = (Long) data_ht.get("heapCommitMemory");
											        heapCommitMemory = heapCommitMemory / 1024 / 1024;
											    }
											%>
											<td><%=heapCommitMemory%></td>
											<td height="29" nowrap class=txtGlobal>
												�����ֵ(MB):
											</td>
											<%
											    long heapMaxMemory = -1;
											    if (null != data_ht.get("heapMaxMemory")) {
											        heapMaxMemory = (Long) data_ht.get("heapMaxMemory");
											        heapMaxMemory = heapMaxMemory / 1024 / 1024;
											    }
											%>
											<td><%=heapMaxMemory%></td>
										</tr>
										<tr bgcolor="#F1F1F1">
											<td height="29" class=txtGlobal nowrap>
												�����ռ���:
											</td>
											<%
											    String collector = "";
											    Vector collectorVector = (Vector) data_ht.get("collector");
											    if (null != collectorVector && collectorVector.size() > 0) {
											        for (int i = 0; i < collectorVector.size(); i++) {
											            collector = collector + collectorVector.get(i) + " ; ";
											        }
											    }
											%>
											<td colspan=5>
												<%=collector%>
											</td>
										</tr>
										<tr>
											<td height="29" align=left nowrap class=txtGlobal>
												&nbsp;��һ����ѯ:
											</td>
											<td colspan=5><%=lasttime%></td>
										</tr>
										<tr bgcolor="#F1F1F1">
											<td height="29" class=txtGlobal nowrap>
												&nbsp;��һ����ѯ:
											</td>
											<td colspan=5><%=nexttime%></td>
										</tr>
										<tr>
											<td height="29" class=txtGlobal align=right nowrap colspan=3>
												&nbsp;
												<a
													href="<%=rootPath%>/tomcat.do?action=syncconfig&id=<%=tomcat.getId()%>&flag=<%=flag%>">ͬ������</a>&nbsp;&nbsp;
											</td>
										</tr>
									</table>
								</td>
								<td width=20% align="center">
									<table class="container-main-service-detail-tool">
										<tr>
											<td>
												<table style="BORDER-COLLAPSE: collapse" bordercolor="#9FB0C4"
													rules=none align=center border=1 cellpadding=0
													cellspacing="0" width=100%>
													<tr bgcolor=#F1F1F1 height="26">
														<td align="center">
															������ͨ��
														</td>
													</tr>
													<tr>
														<td align=center>
															<!-- 
															<div id="flashcontent00">
																<strong>You need to upgrade your Flash Player</strong>
															</div>
															<script type="text/javascript">
																var so = new SWFObject("<%=rootPath%>/flex/Pie_Component.swf?percent1=<%=percent1%>&percentStr1=����&percent2=<%=percent2%>&percentStr2=������", "Pie_Component", "160", "160", "8", "#ffffff");
																so.write("flashcontent00");
															</script>
														-->
															<div id="avgping">
																<strong>You need to upgrade your Flash Player</strong>
															</div>
															<script type="text/javascript"
																src="<%=rootPath%>/include/swfobject.js"></script>
															<script type="text/javascript">
						                                       var so = new SWFObject("<%=rootPath%>/amchart/ampie.swf", "ampie","160", "155", "8", "#FFFFFF");
						                                           so.addVariable("path", "<%=rootPath%>/amchart/");
						                                           so.addVariable("settings_file", escape("<%=rootPath%>/amcharts_settings/pingStatepie.xml"));
						                                           so.addVariable("chart_data","<%=avgdata%>");
						                                           so.write("avgping");
					                                  </script>
														</td>
													</tr>
													<tr>
														<td height="7" align=center>
															<img src="<%=rootPath%>/resource/image/Loading_2.gif">
														</td>
													</tr>
												</table>
											</td>
										</tr>
										<tr>

											<td>
												<table style="BORDER-COLLAPSE: collapse" bordercolor="#9FB0C4"
													rules=none align=center border=1 cellpadding=0
													cellspacing="0" width=100%>
													<!-- 
													<tr>
														<td align="center" valign="middle" height='30'>
															<div id="flashcontent01">
																<strong>You need to upgrade your Flash Player</strong>
															</div>
															<script type="text/javascript">
																var so = new SWFObject("<%=rootPath%>/flex/DHCCGauge.swf?percent=<%=cpuper%>&title=JVM������", "Pie_Component1", "160", "160", "8", "#ffffff");
																so.write("flashcontent01");
															</script>
														</td>
													</tr>
													<tr>
														<td align=center>
															<img src="<%=rootPath%>/resource/image/Loading.gif">
														</td>
													</tr>
											-->
													<tr bgcolor=#F1F1F1 height="26">
														<td align="center">
															JVMƽ��������
														</td>
													</tr>
													<tr height=160>
														<td align="center">
															<img
																src="<%=rootPath%>/resource/image/jfreechart/reportimg/<%=tmp%>tomcat_jvm.png">
														</td>
													</tr>
													<tr height="7">
														<td align=center>
															&nbsp;
															<img src="<%=rootPath%>/resource/image/Loading.gif">
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
		</td>
	</tr>
	<tr>
		<td>
			<jsp:include page="/topology/includejsp/detail_content_footer.jsp" />
		</td>
	</tr>
</table>