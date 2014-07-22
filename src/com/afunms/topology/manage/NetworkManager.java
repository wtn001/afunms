/**
 * <p>Description:DiscoverManager</p>
 * <p>Company: dhcc.com</p>
 * @author afunms
 * @project afunms
 * @date 2006-08-12
 */

package com.afunms.topology.manage;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import com.afunms.alarm.dao.AlarmIndicatorsNodeDao;
import com.afunms.alarm.dao.AlarmPortDao;
import com.afunms.alarm.util.AlarmConstant;
import com.afunms.alarm.util.AlarmIndicatorsUtil;
import com.afunms.application.ajaxManager.PerformancePanelAjaxManager;
import com.afunms.application.util.ProcessGroupConfigurationUtil;
import com.afunms.common.base.BaseManager;
import com.afunms.common.base.BaseVo;
import com.afunms.common.base.DaoInterface;
import com.afunms.common.base.ErrorMessage;
import com.afunms.common.base.ManagerInterface;
import com.afunms.common.util.ChartXml;
import com.afunms.common.util.CreateTableManager;
import com.afunms.common.util.DBManager;
import com.afunms.common.util.EncryptUtil;
import com.afunms.common.util.Fileupload;
import com.afunms.common.util.NetworkUtil;
import com.afunms.common.util.NodeAlarmUtil;
import com.afunms.common.util.PollDataUtil;
import com.afunms.common.util.SessionConstant;
import com.afunms.common.util.ShareData;
import com.afunms.common.util.SnmpPing;
import com.afunms.common.util.SysLogger;
import com.afunms.common.util.SysUtil;
import com.afunms.common.util.SystemConstant;
import com.afunms.common.util.UserAuditUtil;
import com.afunms.config.dao.BusinessDao;
import com.afunms.config.dao.IpAliasDao;
import com.afunms.config.dao.IpaddressPanelDao;
import com.afunms.config.dao.NetNodeCfgFileDao;
import com.afunms.config.dao.PortconfigDao;
import com.afunms.config.dao.SupperDao;
import com.afunms.config.model.Supper;
import com.afunms.event.dao.EventListDao;
import com.afunms.event.dao.NetSyslogDao;
import com.afunms.event.dao.NetSyslogRuleDao;
import com.afunms.event.model.EventList;
import com.afunms.event.model.NetSyslogRule;
import com.afunms.indicators.dao.NodeGatherIndicatorsDao;
import com.afunms.indicators.model.NodeDTO;
import com.afunms.indicators.model.NodeGatherIndicators;
import com.afunms.indicators.util.NodeGatherIndicatorsUtil;
import com.afunms.indicators.util.NodeUtil;
import com.afunms.initialize.PortConfigCenter;
import com.afunms.initialize.ResourceCenter;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.api.I_HostCollectData;
import com.afunms.polling.api.I_HostCollectDataDay;
import com.afunms.polling.api.I_HostLastCollectData;
import com.afunms.polling.impl.HostCollectDataDayManager;
import com.afunms.polling.impl.HostCollectDataManager;
import com.afunms.polling.impl.HostLastCollectDataManager;
import com.afunms.polling.impl.IpResourceReport;
import com.afunms.polling.impl.ProcessNetData;
import com.afunms.polling.loader.HostLoader;
import com.afunms.polling.node.Host;
import com.afunms.polling.om.AllUtilHdx;
import com.afunms.polling.om.CPUcollectdata;
import com.afunms.polling.om.Memorycollectdata;
import com.afunms.polling.om.Pingcollectdata;
import com.afunms.polling.om.VMWareConnectConfig;
import com.afunms.polling.om.VMWareVid;
import com.afunms.polling.snmp.LoadAixFile;
import com.afunms.polling.snmp.LoadHpUnixFile;
import com.afunms.polling.snmp.LoadLinuxFile;
import com.afunms.polling.snmp.LoadScoOpenServerFile;
import com.afunms.polling.snmp.LoadScoUnixWareFile;
import com.afunms.polling.snmp.LoadSunOSFile;
import com.afunms.polling.snmp.LoadWindowsWMIFile;
import com.afunms.polling.snmp.ping.PingSnmp;
import com.afunms.polling.task.UpdateXmlTask;
import com.afunms.portscan.dao.PortScanDao;
import com.afunms.report.abstraction.ExcelReport1;
import com.afunms.report.base.AbstractionReport1;
import com.afunms.system.model.TimeGratherConfig;
import com.afunms.system.model.User;
import com.afunms.system.util.TimeGratherConfigUtil;
import com.afunms.system.util.TimeShareConfigUtil;
import com.afunms.system.util.UserView;
import com.afunms.topology.dao.ConnectDao;
import com.afunms.topology.dao.DiscoverCompleteDao;
import com.afunms.topology.dao.HostInterfaceDao;
import com.afunms.topology.dao.HostNodeDao;
import com.afunms.topology.dao.IpMacBaseDao;
import com.afunms.topology.dao.IpMacChangeDao;
import com.afunms.topology.dao.IpMacDao;
import com.afunms.topology.dao.LineDao;
import com.afunms.topology.dao.LinkDao;
import com.afunms.topology.dao.ManageXmlDao;
import com.afunms.topology.dao.NetSyslogNodeRuleDao;
import com.afunms.topology.dao.NodeDependDao;
import com.afunms.topology.dao.NodeEquipDao;
import com.afunms.topology.dao.NodeMonitorDao;
import com.afunms.topology.dao.RelationDao;
import com.afunms.topology.dao.RemotePingHostDao;
import com.afunms.topology.dao.RemotePingNodeDao;
import com.afunms.topology.dao.RepairLinkDao;
import com.afunms.topology.dao.VMWareConnectConfigDao;
import com.afunms.topology.dao.VMWareVidDao;
import com.afunms.topology.model.Connect;
import com.afunms.topology.model.HostNode;
import com.afunms.topology.model.Link;
import com.afunms.topology.model.ManageXml;
import com.afunms.topology.model.MonitorHostDTO;
import com.afunms.topology.model.MonitorNetDTO;
import com.afunms.topology.model.MonitorNodeDTO;
import com.afunms.topology.model.NetSyslogNodeRule;
import com.afunms.topology.model.NodeDepend;
import com.afunms.topology.model.NodeMonitor;
import com.afunms.topology.model.Relation;
import com.afunms.topology.util.ManageXmlOperator;
import com.afunms.topology.util.TopoHelper;
import com.afunms.topology.util.WMWareUtil;
import com.afunms.topology.util.XmlOperator;
import com.afunms.vmware.vim25.common.VIMMgr;

/**
 * ����һ���ڵ㣬Ҫ��server.jsp����һ���ڵ�;
 * ɾ�������һ���ڵ���Ϣ��������ֱ�Ӳ���server.jsp,����������updateXml������xml.
 */
public class NetworkManager extends BaseManager implements ManagerInterface {
    SimpleDateFormat sdf0 = new SimpleDateFormat("yyyy-MM-dd");

    I_HostCollectData hostmanager = new HostCollectDataManager();

    I_HostLastCollectData hostlastmanager = new HostLastCollectDataManager();

    private static Hashtable connectConfigHashtable = new Hashtable();

    private String list() {
        User current_user = (User) session.getAttribute(SessionConstant.CURRENT_USER);

        StringBuffer s = new StringBuffer();
        int _flag = 0;
        if (current_user.getBusinessids() != null) {
            if (current_user.getBusinessids() != "-1") {
                String[] bids = current_user.getBusinessids().split(",");
                if (bids.length > 0) {
                    for (int i = 0; i < bids.length; i++) {
                        if (bids[i].trim().length() > 0) {
                            if (_flag == 0) {
                                s.append(" and ( bid like '%," + bids[i].trim() + ",%' ");
                                _flag = 1;
                            } else {
                                // flag = 1;
                                s.append(" or bid like '%," + bids[i].trim() + ",%' ");
                            }
                        }
                    }
                    s.append(") ");
                }

            }
        }
        request.setAttribute("actionlist", "list");
        setTarget("/topology/network/list.jsp");
        HostNodeDao dao = new HostNodeDao();
        if (current_user.getRole() == 0) {
            return list(dao, "where 1=1 ");
        } else {
            return list(dao, "where 1=1 " + s);
        }

    }

    /**
     * ������Ӽ���/ȡ������
     * 
     * @author HONGLI 2011-04-21
     * @return
     */
    public String batchModifyMoniter() {
        String eventids = getParaValue("eventids");// 12,13,14,
        String modifyFlag = getParaValue("modifyFlag");// �������±�־λ add / remove
        String[] ids = eventids.split(",");
        // ȡ���ɼ�
        List<String> nodeidList = new ArrayList<String>();
        HostLoader hl = new HostLoader();
        HostNodeDao dao = new HostNodeDao();
        DBManager db = new DBManager();
        try {

            for (int i = 0; i < ids.length; i++) {
                String id = ids[i];
                if (id == "" || id == null) {
                    continue;
                }
                HostNode host = (HostNode) dao.findByID(id);
                nodeidList.add(id);
                if ("add".equals(modifyFlag)) {
                    // �����ѯ�ڵ�
                    host.setManaged(true);
                    db.addBatch(dao.updateSql(host));
                    hl.loadOne(host);
                } else {
                    host.setManaged(false);
                    db.addBatch(dao.updateSql(host));
                    // ɾ����ѯ�ڵ�
                    PollingEngine.getInstance().deleteNodeByID(Integer.parseInt(id));
                    // ɾ���豸��ǰ���¸澯��Ϣ���е�����
                    NodeAlarmUtil.deleteByDeviceIdAndDeviceType(host.getId() + "", "net");
                }
            }
            db.executeBatch();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dao.close();
            db.close();
            hl.close();
        }
        // HostNodeDao _hostNodeDao = new HostNodeDao();
        // Hashtable nodehash = ShareData.getNodehash();
        // try{
        // List hostlist = _hostNodeDao.loadIsMonitored(1);
        // if(hostlist != null && hostlist.size()>0){
        // for(int i=0;i<hostlist.size();i++){
        // HostNode node = (HostNode)hostlist.get(i);
        // //SysLogger.info("node alias:==="+node.getAlias());
        // if(nodehash.containsKey(node.getCategory()+"")){
        // List list = (List)nodehash.get(node.getCategory()+"");
        // for(int k=0;k<list.size();k++){
        // HostNode exitnode = (HostNode)list.get(k);
        // if(exitnode.getId() == node.getId()){
        // list.remove(k);
        // list.add(k, node);
        // break;
        // }
        // }
        // nodehash.put(node.getCategory()+"", list);
        // //((List)nodehash.get(node.getCategory()+"")).add(node);
        // }else{
        // List nodelist = new ArrayList();
        // nodelist.add(node);
        // nodehash.put(node.getCategory()+"", nodelist);
        // }
        // }
        // }
        // }catch(Exception e){
        //			
        // }finally{
        // _hostNodeDao.close();
        // }
        // ShareData.setNodehash(nodehash);
        HostNodeDao hostNodeDao = new HostNodeDao();
        Hashtable nodehash = new Hashtable();
        try {
            List hostlist = hostNodeDao.loadIsMonitored(1);
            if (hostlist != null && hostlist.size() > 0) {
                for (int i = 0; i < hostlist.size(); i++) {
                    HostNode node = (HostNode) hostlist.get(i);
                    if (nodehash.containsKey(node.getCategory() + "")) {
                        ((List) nodehash.get(node.getCategory() + "")).add(node);
                    } else {
                        List nodelist = new ArrayList();
                        nodelist.add(node);
                        nodehash.put(node.getCategory() + "", nodelist);
                    }
                }
            }
        } catch (Exception e) {

        } finally {
            hostNodeDao.close();
        }
        ShareData.setNodehash(nodehash);
        return list();
    }

    // private String monitornodelist()
    // {
    // User current_user =
    // (User)session.getAttribute(SessionConstant.CURRENT_USER);
    //			 
    // StringBuffer s = new StringBuffer();
    // int _flag = 0;
    // if (current_user.getBusinessids() != null){
    // if(current_user.getBusinessids() !="-1"){
    // String[] bids = current_user.getBusinessids().split(",");
    // if(bids.length>0){
    // for(int i=0;i<bids.length;i++){
    // if(bids[i].trim().length()>0){
    // if(_flag==0){
    // s.append(" and ( bid like '%,"+bids[i].trim()+",%' ");
    // _flag = 1;
    // }else{
    // //flag = 1;
    // s.append(" or bid like '%,"+bids[i].trim()+",%' ");
    // }
    // }
    // }
    // s.append(") ") ;
    // }
    //				
    // }
    // }
    // setTarget("/topology/network/monitornodelist.jsp");
    // request.setAttribute("actionlist", "monitornodelist");
    // SysLogger.info("select * from topo_host_node where managed=1 "+s);
    // HostNodeDao dao = new HostNodeDao();
    // if(current_user.getRole() == 0){
    // return list(dao," where managed=1 ");
    // }else{
    // return list(dao," where managed=1 "+s);
    // }
    //        
    // }

    private String endpointnodelist() {
        HostNodeDao dao = new HostNodeDao();
        setTarget("/topology/network/endponitnodelist.jsp");
        return list(dao, " where (category=2 or category=3) and endpoint=1");
    }

    private String panelnodelist() {
        HostNodeDao dao = new HostNodeDao();
        setTarget("/topology/network/panelnodelist.jsp");
        return list(dao, " where managed=1 and (category<4 or category=7 or category=8)");
    }

    // private String monitorswitchlist()
    // {
    // HostNodeDao dao = new HostNodeDao();
    // setTarget("/topology/network/monitorswitchlist.jsp");
    // request.setAttribute("actionlist", "monitorswitchlist");
    // return list(dao," where managed=1 and (category=2 or category=3 or
    // category=7) ");
    // }
    //	
    // private String monitorroutelist()
    // {
    // HostNodeDao dao = new HostNodeDao();
    // setTarget("/topology/network/monitorroutelist.jsp");
    // return list(dao," where managed=1 and category=1");
    // }

    private String monitorfirewalllist() {
        HostNodeDao dao = new HostNodeDao();
        setTarget("/topology/network/monitorfirewalllist.jsp");
        return list(dao, " where managed=1 and category=8");
    }

    // private String monitorhostlist()
    // {
    // HostNodeDao dao = new HostNodeDao();
    // setTarget("/topology/network/monitorhostlist.jsp");
    // request.setAttribute("actionlist", "monitorhostlist");
    // return list(dao," where managed=1 and category=4");
    // }

    /**
     * ��������������б�
     * 
     * @author nielin
     * @date 2010-08-09
     */
    private String monitorhostlist() {
        String jsp = "/topology/network/monitorhostlist.jsp";
        setTarget(jsp);

        List monitorhostlist = getMonitorListByCategory("net_server");

        Hashtable sharedata = ShareData.getSharedata();

        Hashtable allpingdata = ShareData.getPingdata();

        List list = new ArrayList();

        NumberFormat numberFormat = new DecimalFormat();
        numberFormat.setMaximumFractionDigits(0);

        if (monitorhostlist != null) {
            for (int i = 0; i < monitorhostlist.size(); i++) {

                HostNode hostNode = (HostNode) monitorhostlist.get(i);
                MonitorNodeDTO monitorHostDTO = getMonitorNodeDTOByHostNode(hostNode);
                list.add(monitorHostDTO);
            }
        }
        String field = getParaValue("field");
        String sorttype = getParaValue("sorttype");
        if (field != null) {
            if (sorttype == null || sorttype.trim().length() == 0) {
                sorttype = "asc";
            } else if ("asc".equals(sorttype)) {
                sorttype = "desc";
            } else if ("desc".equals(sorttype)) {
                sorttype = "asc";
            }

            monitorListSort(list, "host", field, sorttype);

            request.setAttribute("field", field);
            request.setAttribute("sorttype", sorttype);
        }

        request.setAttribute("list", list);
        return jsp;
    }

    private String read() {
        DaoInterface dao = new HostNodeDao();
        setTarget("/topology/network/read.jsp");
        return readyEdit(dao);
    }

    private String telnet() {
        request.setAttribute("ipaddress", getParaValue("ipaddress"));

        return "/tool/webTelnet/webTelnet.jsp";
    }

    private String readyEdit() {

        setTarget("/topology/network/edit.jsp");
        String nodeId = getParaValue("id");

        NetSyslogNodeRuleDao noderuledao = new NetSyslogNodeRuleDao();
        NetSyslogNodeRule noderule = null;
        try {
            noderule = (NetSyslogNodeRule) noderuledao.findByID(nodeId);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            noderuledao.close();
        }
        if (noderule != null) {
            String nodefacility = noderule.getFacility();
            List nodeflist = new ArrayList();
            if (nodefacility != null && nodefacility.trim().length() > 0) {
                String[] nodefacilitys = nodefacility.split(",");

                if (nodefacilitys != null && nodefacilitys.length > 0) {
                    for (int i = 0; i < nodefacilitys.length; i++) {
                        nodeflist.add(nodefacilitys[i]);
                    }
                }
            }

            request.setAttribute("nodefacilitys", nodeflist);
        }
        // nielin add ---- use to timeShare show 2010-01-03
        TimeShareConfigUtil timeShareConfigUtil = new TimeShareConfigUtil();
        List timeShareConfigList = timeShareConfigUtil.getTimeShareConfigList(nodeId, timeShareConfigUtil.getObjectType("0"));
        request.setAttribute("timeShareConfigList", timeShareConfigList);
        // nielin add ----- end

        /* snow add at 2010-05-18 */
        // �ṩ��ѡ��Ĺ�Ӧ����Ϣ
        SupperDao supperdao = new SupperDao();
        List<Supper> allSupper = supperdao.loadAll();
        request.setAttribute("allSupper", allSupper);
        DiscoverCompleteDao disDao = new DiscoverCompleteDao();
        // �ṩ�����õĲɼ�ʱ����Ϣ
        TimeGratherConfigUtil tg = new TimeGratherConfigUtil();
        List<TimeGratherConfig> timeGratherConfigList = tg.getTimeGratherConfig(nodeId, tg.getObjectType("0"));
        for (TimeGratherConfig timeGratherConfig : timeGratherConfigList) {
            timeGratherConfig.setHourAndMin();
        }
        request.setAttribute("timeGratherConfigList", timeGratherConfigList);
        /* snow end */

        DaoInterface dao = new HostNodeDao();
        return readyEdit(dao);
    }

    private String readyEditAlias() {
        HostNodeDao dao = new HostNodeDao();
        String targetJsp = "/topology/network/editAliasItem.jsp";
        BaseVo vo = null;
        try {
            // String ii=getParaValue("id");
            vo = dao.findByID(getParaValue("id"));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dao.close();
        }
        if (vo != null) {
            request.setAttribute("vo", vo);
        }
        return targetJsp;
    }

    private String readyEditSysGroup() {
        HostNodeDao dao = new HostNodeDao();
        String targetJsp = "/topology/network/editsysgroup.jsp";
        BaseVo vo = null;
        try {
            // String ii=getParaValue("id");
            vo = dao.findByID(getParaValue("id"));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dao.close();
        }
        if (vo != null) {
            request.setAttribute("vo", vo);
        }
        return targetJsp;
    }

    private String readyEditSnmp() {
        HostNodeDao dao = new HostNodeDao();
        String targetJsp = "/topology/network/editsnmp.jsp";
        BaseVo vo = null;
        try {
            vo = dao.findByID(getParaValue("id"));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dao.close();
        }
        if (vo != null) {
            request.setAttribute("vo", vo);
        }
        return targetJsp;

        // DaoInterface dao = new HostNodeDao();
        // setTarget("/topology/network/editsnmp.jsp");
        // return readyEdit(dao);
    }

    private String ready_EditIpAlias() {
        HostNodeDao dao = new HostNodeDao();
        String targetJsp = "/topology/network/editipalias.jsp";
        String ipaddress = getParaValue("ipaddress");
        BaseVo vo = null;
        try {
            // String ii=getParaValue("id");
            vo = dao.findByID(getParaValue("id"));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dao.close();
        }
        if (vo != null) {
            request.setAttribute("vo", vo);
        }
        request.setAttribute("ipaddress", ipaddress);
        return targetJsp;
    }

    private String update() {
        HostNode vo = new HostNode();
        vo.setId(getParaIntValue("id"));
        vo.setAssetid(getParaValue("assetid"));
        vo.setLocation(getParaValue("location"));
        vo.setAlias(getParaValue("alias"));
        vo.setSnmpversion(getParaIntValue("snmpversion"));
        vo.setTransfer(getParaIntValue("transfer"));
        vo.setManaged(getParaIntValue("managed") == 1 ? true : false);
        vo.setSendemail(getParaValue("sendemail"));
        vo.setSendmobiles(getParaValue("sendmobiles"));
        vo.setSendphone(getParaValue("sendphone"));
        vo.setSupperid(getParaIntValue("supper"));// snow add at 2010-5-18
        vo.setBid(getParaValue("bid"));

        String ipaddress = getParaValue("ipaddress");
        vo.setIpAddress(ipaddress);
        // �����ڴ�
        String formerip = "";
        Host host = (Host) PollingEngine.getInstance().getNodeByID(vo.getId());
        if (host != null) {

            host.setAssetid(vo.getAssetid());
            host.setLocation(vo.getLocation());
            host.setAlias(vo.getAlias());
            host.setSnmpversion(vo.getSnmpversion());
            host.setTransfer(vo.getTransfer());
            host.setManaged(vo.isManaged());
            host.setSendemail(vo.getSendemail());
            host.setSendmobiles(vo.getSendmobiles());
            host.setSupperid(vo.getSupperid());// snow add at 2010-5-18
        } else {
            if (getParaIntValue("managed") == 1) {
                HostNodeDao dao = new HostNodeDao();
                HostNode hostnode = null;
                try {
                    hostnode = dao.loadHost(vo.getId());
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    dao.close();
                }
                hostnode.setAssetid(getParaValue("assetid"));
                hostnode.setLocation(getParaValue("location"));
                hostnode.setAlias(getParaValue("alias"));
                host.setSnmpversion(getParaIntValue("snmpversion"));
                host.setTransfer(getParaIntValue("transfer"));
                hostnode.setManaged(getParaIntValue("managed") == 1 ? true : false);
                hostnode.setSendemail(getParaValue("sendemail"));
                hostnode.setSendmobiles(getParaValue("sendmobiles"));
                hostnode.setSendphone(getParaValue("sendphone"));
                hostnode.setSupperid(getParaIntValue("supper"));// snow add at
                // 2010-5-18
                HostLoader loader = new HostLoader();
                try {
                    loader.loadOne(hostnode);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    loader.close();
                }
                // PollingEngine.getInstance().addNode(node)
            }
        }

        // ���»�ȡһ���ڴ��еĶ���
        host = (Host) PollingEngine.getInstance().getNodeByID(vo.getId());

        // SysLogger.info(ipaddress+"============"+host.getIpAddress());
        if (!host.getIpAddress().equalsIgnoreCase(ipaddress)) {
            // IP��ַ�Ѿ����޸�,��Ҫ������ص�IP��������Ϣ
            formerip = host.getIpAddress();

            // String ip = host.getIpAddress();
            String ip = formerip;
            // String ip1 ="",ip2="",ip3="",ip4="";
            // String[] ipdot = ip.split(".");
            // String tempStr = "";
            // String allipstr = "";
            // if (ip.indexOf(".")>0){
            // ip1=ip.substring(0,ip.indexOf("."));
            // ip4=ip.substring(ip.lastIndexOf(".")+1,ip.length());
            // tempStr = ip.substring(ip.indexOf(".")+1,ip.lastIndexOf("."));
            // }
            // ip2=tempStr.substring(0,tempStr.indexOf("."));
            // ip3=tempStr.substring(tempStr.indexOf(".")+1,tempStr.length());
            // allipstr=ip1+ip2+ip3+ip4;
            String allipstr = SysUtil.doip(ip);

            CreateTableManager ctable = new CreateTableManager();
            DBManager conn = new DBManager();
            try {

                if (host.getCategory() < 4 || host.getCategory() == 7 || host.getCategory() == 8) {
                    // ��ɾ�������豸��
                    // ��ͨ��
                    try {
                        ctable.deleteTable(conn, "ping", allipstr, "ping");// Ping
                        ctable.deleteTable(conn, "pinghour", allipstr, "pinghour");// PingHour
                        ctable.deleteTable(conn, "pingday", allipstr, "pingday");// PingDay
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    // �ڴ�
                    try {
                        ctable.deleteTable(conn, "memory", allipstr, "mem");// �ڴ�
                        ctable.deleteTable(conn, "memoryhour", allipstr, "memhour");// �ڴ�
                        ctable.deleteTable(conn, "memoryday", allipstr, "memday");// �ڴ�
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        ctable.deleteTable(conn, "flash", allipstr, "flash");// ����
                        ctable.deleteTable(conn, "flashhour", allipstr, "flashhour");// ����
                        ctable.deleteTable(conn, "flashday", allipstr, "flashday");// ����
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        ctable.deleteTable(conn, "buffer", allipstr, "buffer");// ����
                        ctable.deleteTable(conn, "bufferhour", allipstr, "bufferhour");// ����
                        ctable.deleteTable(conn, "bufferday", allipstr, "bufferday");// ����
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        ctable.deleteTable(conn, "fan", allipstr, "fan");// ����
                        ctable.deleteTable(conn, "fanhour", allipstr, "fanhour");// ����
                        ctable.deleteTable(conn, "fanday", allipstr, "fanday");// ����
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        ctable.deleteTable(conn, "power", allipstr, "power");// ��Դ
                        ctable.deleteTable(conn, "powerhour", allipstr, "powerhour");// ��Դ
                        ctable.deleteTable(conn, "powerday", allipstr, "powerday");// ��Դ
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        ctable.deleteTable(conn, "vol", allipstr, "vol");// ��ѹ
                        ctable.deleteTable(conn, "volhour", allipstr, "volhour");// ��ѹ
                        ctable.deleteTable(conn, "volday", allipstr, "volday");// ��ѹ
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    // CPU
                    try {
                        ctable.deleteTable(conn, "cpu", allipstr, "cpu");// CPU
                        ctable.deleteTable(conn, "cpuhour", allipstr, "cpuhour");// CPU
                        ctable.deleteTable(conn, "cpuday", allipstr, "cpuday");// CPU
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    // ����������
                    try {
                        ctable.deleteTable(conn, "utilhdxperc", allipstr, "hdperc");
                        ctable.deleteTable(conn, "hdxperchour", allipstr, "hdperchour");
                        ctable.deleteTable(conn, "hdxpercday", allipstr, "hdpercday");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    // ����
                    try {
                        ctable.deleteTable(conn, "utilhdx", allipstr, "hdx");
                        ctable.deleteTable(conn, "utilhdxhour", allipstr, "hdxhour");
                        ctable.deleteTable(conn, "utilhdxday", allipstr, "hdxday");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    // �ۺ�����
                    try {
                        ctable.deleteTable(conn, "allutilhdx", allipstr, "allhdx");
                        ctable.deleteTable(conn, "autilhdxh", allipstr, "ahdxh");
                        ctable.deleteTable(conn, "autilhdxd", allipstr, "ahdxd");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    // ������
                    try {
                        ctable.deleteTable(conn, "discardsperc", allipstr, "dcardperc");
                        ctable.deleteTable(conn, "dcarperh", allipstr, "dcarperh");
                        ctable.deleteTable(conn, "dcarperd", allipstr, "dcarperd");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    // ������
                    try {
                        ctable.deleteTable(conn, "errorsperc", allipstr, "errperc");
                        ctable.deleteTable(conn, "errperch", allipstr, "errperch");
                        ctable.deleteTable(conn, "errpercd", allipstr, "errpercd");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    // ���ݰ�
                    try {
                        ctable.deleteTable(conn, "packs", allipstr, "packs");
                        ctable.deleteTable(conn, "packshour", allipstr, "packshour");
                        ctable.deleteTable(conn, "packsday", allipstr, "packsday");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    // ������ݰ�
                    try {
                        ctable.deleteTable(conn, "inpacks", allipstr, "inpacks");
                        ctable.deleteTable(conn, "ipacksh", allipstr, "ipacksh");
                        ctable.deleteTable(conn, "ipackd", allipstr, "ipackd");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    // �������ݰ�
                    try {
                        ctable.deleteTable(conn, "outpacks", allipstr, "outpacks");
                        ctable.deleteTable(conn, "opackh", allipstr, "opackh");
                        ctable.deleteTable(conn, "opacksd", allipstr, "opacksd");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    // �¶�
                    try {
                        ctable.deleteTable(conn, "temper", allipstr, "temper");
                        ctable.deleteTable(conn, "temperh", allipstr, "temperh");
                        ctable.deleteTable(conn, "temperd", allipstr, "temperd");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        conn.commit();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    DiscoverCompleteDao dcDao = new DiscoverCompleteDao();
                    try {
                        dcDao.deleteMonitor(host.getId(), host.getIpAddress());
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        dcDao.close();
                        // conn.close();
                    }

                    EventListDao eventdao = new EventListDao();
                    try {
                        // ͬʱɾ���¼�������������
                        eventdao.delete(host.getId(), "network");
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        eventdao.close();
                    }

                    PortconfigDao portconfigdao = new PortconfigDao();
                    try {
                        // ͬʱɾ���˿����ñ�����������
                        portconfigdao.deleteByIpaddress(host.getIpAddress());
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        portconfigdao.close();
                    }

                    // ɾ��IP-MAC-BASE����Ķ�Ӧ������
                    IpMacChangeDao macchangebasedao = new IpMacChangeDao();
                    try {
                        // delte��,conn�Ѿ��ر�
                        macchangebasedao.deleteByHostIp(host.getIpAddress());
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        macchangebasedao.close();
                    }

                    // ɾ�������豸�����ļ�����Ķ�Ӧ������
                    NetNodeCfgFileDao dao = new NetNodeCfgFileDao();
                    try {
                        // delte��,conn�Ѿ��ر�
                        dao.deleteByHostIp(host.getIpAddress());
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        dao.close();
                    }

                    // ɾ�������豸SYSLOG���ձ���Ķ�Ӧ������
                    NetSyslogDao syslogdao = new NetSyslogDao();
                    try {
                        // delte��,conn�Ѿ��ر�
                        syslogdao.deleteByHostIp(host.getIpAddress());
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        syslogdao.close();
                    }

                    // ɾ�������豸�˿�ɨ�����Ķ�Ӧ������
                    PortScanDao portscandao = new PortScanDao();
                    try {
                        // delte��,conn�Ѿ��ر�
                        portscandao.deleteByHostIp(host.getIpAddress());
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        portscandao.close();
                    }

                    // ɾ�������豸���ͼ����Ķ�Ӧ������
                    IpaddressPanelDao addresspaneldao = new IpaddressPanelDao();
                    try {
                        // delte��,conn�Ѿ��ر�
                        addresspaneldao.deleteByHostIp(host.getIpAddress());
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        addresspaneldao.close();
                    }

                    // ɾ�������豸�ӿڱ���Ķ�Ӧ������
                    HostInterfaceDao interfacedao = new HostInterfaceDao();
                    try {
                        // delte��,conn�Ѿ��ر�
                        interfacedao.deleteByHostId(host.getId() + "");
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        interfacedao.close();
                    }

                    // ɾ�������豸IP��������Ķ�Ӧ������
                    IpAliasDao ipaliasdao = new IpAliasDao();
                    try {
                        // delte��,conn�Ѿ��ر�
                        ipaliasdao.deleteByHostIp(host.getIpAddress());
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        ipaliasdao.close();
                    }

                    // ɾ�������豸�ֹ����õ���·����Ķ�Ӧ������
                    RepairLinkDao repairdao = new RepairLinkDao();
                    try {
                        // delte��,conn�Ѿ��ر�
                        repairdao.updatestartlinkip(host.getIpAddress(), ipaddress);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        repairdao.close();
                    }
                    // ɾ�������豸�ֹ����õ���·����Ķ�Ӧ������
                    repairdao = new RepairLinkDao();
                    try {
                        // delte��,conn�Ѿ��ر�
                        repairdao.updateendlinkip(host.getIpAddress(), ipaddress);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        repairdao.close();
                    }

                    // ɾ�������豸IPMAC����Ķ�Ӧ������
                    IpMacDao ipmacdao = new IpMacDao();
                    try {
                        // delte��,conn�Ѿ��ر�
                        ipmacdao.deleteByHostIp(host.getIpAddress());
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        ipmacdao.close();
                    }
                    // ɾ��nms_ipmacchange����Ķ�Ӧ������
                    IpMacBaseDao macbasedao = new IpMacBaseDao();
                    try {
                        // delte��,conn�Ѿ��ر�
                        macbasedao.deleteByHostIp(host.getIpAddress());
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        macbasedao.close();
                    }
                    host.setIpAddress(ipaddress);
                    vo.setIpAddress(ipaddress);
                    // ������ʱ��
                    // ���������豸��
                    ip = ipaddress;
                    // if (ip.indexOf(".")>0){
                    // ip1=ip.substring(0,ip.indexOf("."));
                    // ip4=ip.substring(ip.lastIndexOf(".")+1,ip.length());
                    // tempStr =
                    // ip.substring(ip.indexOf(".")+1,ip.lastIndexOf("."));
                    // }
                    // ip2=tempStr.substring(0,tempStr.indexOf("."));
                    // ip3=tempStr.substring(tempStr.indexOf(".")+1,tempStr.length());
                    // allipstr=ip1+ip2+ip3+ip4;
                    allipstr = SysUtil.doip(ip);
                    try {
                        ctable.createTable(conn, "ping", allipstr, "ping");// Ping
                        ctable.createTable(conn, "pinghour", allipstr, "pinghour");// Ping
                        ctable.createTable(conn, "pingday", allipstr, "pingday");// Ping

                        ctable.createTable(conn, "memory", allipstr, "mem");// �ڴ�
                        ctable.createTable(conn, "memoryhour", allipstr, "memhour");// �ڴ�
                        ctable.createTable(conn, "memoryday", allipstr, "memday");// �ڴ�

                        ctable.createTable(conn, "flash", allipstr, "flash");// ����
                        ctable.createTable(conn, "flashhour", allipstr, "flashhour");// ����
                        ctable.createTable(conn, "flashday", allipstr, "flashday");// ����

                        ctable.createTable(conn, "buffer", allipstr, "buffer");// ����
                        ctable.createTable(conn, "bufferhour", allipstr, "bufferhour");// ����
                        ctable.createTable(conn, "bufferday", allipstr, "bufferday");// ����

                        ctable.createTable(conn, "fan", allipstr, "fan");// ����
                        ctable.createTable(conn, "fanhour", allipstr, "fanhour");// ����
                        ctable.createTable(conn, "fanday", allipstr, "fanday");// ����

                        ctable.createTable(conn, "power", allipstr, "power");// ��Դ
                        ctable.createTable(conn, "powerhour", allipstr, "powerhour");// ��Դ
                        ctable.createTable(conn, "powerday", allipstr, "powerday");// ��Դ

                        ctable.createTable(conn, "vol", allipstr, "vol");// ��ѹ
                        ctable.createTable(conn, "volhour", allipstr, "volhour");// ��ѹ
                        ctable.createTable(conn, "volday", allipstr, "volday");// ��ѹ

                        ctable.createTable(conn, "cpu", allipstr, "cpu");// CPU
                        ctable.createTable(conn, "cpuhour", allipstr, "cpuhour");// CPU
                        ctable.createTable(conn, "cpuday", allipstr, "cpuday");// CPU

                        ctable.createTable(conn, "utilhdxperc", allipstr, "hdperc");
                        ctable.createTable(conn, "hdxperchour", allipstr, "hdperchour");
                        ctable.createTable(conn, "hdxpercday", allipstr, "hdpercday");

                        ctable.createTable(conn, "utilhdx", allipstr, "hdx");
                        ctable.createTable(conn, "utilhdxhour", allipstr, "hdxhour");
                        ctable.createTable(conn, "utilhdxday", allipstr, "hdxday");

                        ctable.createTable(conn, "allutilhdx", allipstr, "allhdx");
                        ctable.createTable(conn, "autilhdxh", allipstr, "ahdxh");
                        ctable.createTable(conn, "autilhdxd", allipstr, "ahdxd");

                        ctable.createTable(conn, "discardsperc", allipstr, "dcardperc");
                        ctable.createTable(conn, "dcarperh", allipstr, "dcarperh");
                        ctable.createTable(conn, "dcarperd", allipstr, "dcarperd");

                        ctable.createTable(conn, "errorsperc", allipstr, "errperc");
                        ctable.createTable(conn, "errperch", allipstr, "errperch");
                        ctable.createTable(conn, "errpercd", allipstr, "errpercd");

                        ctable.createTable(conn, "packs", allipstr, "packs");
                        ctable.createTable(conn, "packshour", allipstr, "packshour");
                        ctable.createTable(conn, "packsday", allipstr, "packsday");

                        ctable.createTable(conn, "inpacks", allipstr, "inpacks");
                        ctable.createTable(conn, "ipacksh", allipstr, "ipacksh");
                        ctable.createTable(conn, "ipackd", allipstr, "ipackd");

                        ctable.createTable(conn, "outpacks", allipstr, "outpacks");
                        ctable.createTable(conn, "opackh", allipstr, "opackh");
                        ctable.createTable(conn, "opacksd", allipstr, "opacksd");

                        ctable.createTable(conn, "temper", allipstr, "temper");
                        ctable.createTable(conn, "temperh", allipstr, "temperh");
                        ctable.createTable(conn, "temperd", allipstr, "temperd");
                        conn.commit();
                    } catch (Exception e) {
                        // e.printStackTrace();
                    } finally {
                        try {
                            conn.executeBatch();
                        } catch (Exception e) {

                        }
                        // conn.close();
                    }

                } else if (host.getCategory() == 4) {
                    // ɾ������������
                    try {
                        ctable.deleteTable(conn, "pro", allipstr, "pro");// ����
                    } catch (Exception e) {

                    }
                    try {
                        ctable.deleteTable(conn, "prohour", allipstr, "prohour");// ����Сʱ
                    } catch (Exception e) {

                    }
                    try {
                        ctable.deleteTable(conn, "proday", allipstr, "proday");// ������
                    } catch (Exception e) {

                    }

                    try {
                        ctable.deleteTable(conn, "log", allipstr, "log");// ������
                    } catch (Exception e) {

                    }

                    try {
                        ctable.deleteTable(conn, "memory", allipstr, "mem");// �ڴ�
                    } catch (Exception e) {

                    }
                    try {
                        ctable.deleteTable(conn, "memoryhour", allipstr, "memhour");// �ڴ�
                    } catch (Exception e) {

                    }
                    try {
                        ctable.deleteTable(conn, "memoryday", allipstr, "memday");// �ڴ�
                    } catch (Exception e) {

                    }

                    try {
                        ctable.deleteTable(conn, "cpu", allipstr, "cpu");// CPU
                    } catch (Exception e) {

                    }
                    try {
                        ctable.deleteTable(conn, "cpuhour", allipstr, "cpuhour");// CPU
                    } catch (Exception e) {

                    }
                    try {
                        ctable.deleteTable(conn, "cpuday", allipstr, "cpuday");// CPU
                    } catch (Exception e) {

                    }
                    try {
                        ctable.deleteTable(conn, "cpudtl", allipstr, "cpudtl");// CPU
                    } catch (Exception e) {

                    }
                    try {
                        ctable.deleteTable(conn, "cpudtlhour", allipstr, "cpudtlhour");// CPU
                    } catch (Exception e) {

                    }
                    try {
                        ctable.deleteTable(conn, "cpudtlday", allipstr, "cpudtlday");// CPU
                    } catch (Exception e) {

                    }

                    try {
                        ctable.deleteTable(conn, "disk", allipstr, "disk");// disk
                    } catch (Exception e) {

                    }
                    try {
                        ctable.deleteTable(conn, "diskhour", allipstr, "diskhour");// disk
                    } catch (Exception e) {

                    }
                    try {
                        ctable.deleteTable(conn, "diskday", allipstr, "diskday");// disk
                    } catch (Exception e) {

                    }

                    try {
                        ctable.deleteTable(conn, "diskincre", allipstr, "diskincre");// ��������
                    } catch (Exception e) {

                    }
                    try {
                        ctable.deleteTable(conn, "diskinch", allipstr, "diskinch");// ��������
                    } catch (Exception e) {

                    }
                    try {
                        ctable.deleteTable(conn, "diskincd", allipstr, "diskincd");// ��������
                    } catch (Exception e) {

                    }
                    try {
                        ctable.deleteTable(conn, "ping", allipstr, "ping");
                    } catch (Exception e) {

                    }
                    try {
                        ctable.deleteTable(conn, "pinghour", allipstr, "pinghour");
                    } catch (Exception e) {

                    }
                    try {
                        ctable.deleteTable(conn, "pingday", allipstr, "pingday");
                    } catch (Exception e) {

                    }

                    try {
                        ctable.deleteTable(conn, "utilhdxperc", allipstr, "hdperc");
                    } catch (Exception e) {

                    }
                    try {
                        ctable.deleteTable(conn, "hdxperchour", allipstr, "hdperchour");
                    } catch (Exception e) {

                    }
                    try {
                        ctable.deleteTable(conn, "hdxpercday", allipstr, "hdpercday");
                    } catch (Exception e) {

                    }

                    try {
                        ctable.deleteTable(conn, "utilhdx", allipstr, "hdx");
                    } catch (Exception e) {

                    }
                    try {
                        ctable.deleteTable(conn, "utilhdxhour", allipstr, "hdxhour");
                    } catch (Exception e) {

                    }
                    try {
                        ctable.deleteTable(conn, "utilhdxday", allipstr, "hdxday");
                    } catch (Exception e) {

                    }
                    try {
                        ctable.deleteTable(conn, "software", allipstr, "software");
                    } catch (Exception e) {

                    }
                    ctable.deleteTable(conn, "allutilhdx", allipstr, "allhdx");
                    ctable.deleteTable(conn, "autilhdxh", allipstr, "ahdxh");
                    ctable.deleteTable(conn, "autilhdxd", allipstr, "ahdxd");

                    ctable.deleteTable(conn, "discardsperc", allipstr, "dcardperc");
                    ctable.deleteTable(conn, "dcarperh", allipstr, "dcarperh");
                    ctable.deleteTable(conn, "dcarperd", allipstr, "dcarperd");

                    ctable.deleteTable(conn, "errorsperc", allipstr, "errperc");
                    ctable.deleteTable(conn, "errperch", allipstr, "errperch");
                    ctable.deleteTable(conn, "errpercd", allipstr, "errpercd");

                    ctable.deleteTable(conn, "packs", allipstr, "packs");
                    ctable.deleteTable(conn, "packshour", allipstr, "packshour");
                    ctable.deleteTable(conn, "packsday", allipstr, "packsday");

                    ctable.deleteTable(conn, "inpacks", allipstr, "inpacks");
                    ctable.deleteTable(conn, "ipacksh", allipstr, "ipacksh");
                    ctable.deleteTable(conn, "ipackd", allipstr, "ipackd");

                    ctable.deleteTable(conn, "outpacks", allipstr, "outpacks");
                    ctable.deleteTable(conn, "opackh", allipstr, "opackh");
                    ctable.deleteTable(conn, "opacksd", allipstr, "opacksd");
                    if (host.getSysOid().equalsIgnoreCase("1.3.6.1.4.1.2.3.1.2.1.1")) {
                        // ɾ����ҳ��
                        try {
                            ctable.deleteTable(conn, "pgused", allipstr, "pgused");
                            ctable.deleteTable(conn, "pgusedhour", allipstr, "pgusedhour");
                            ctable.deleteTable(conn, "pgusedday", allipstr, "pgusedday");
                        } catch (Exception e) {

                        }
                    }

                    try {
                        conn.commit();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    DiscoverCompleteDao dcDao = new DiscoverCompleteDao();
                    try {
                        dcDao.deleteMonitor(host.getId(), host.getIpAddress());
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        dcDao.close();
                        // conn.close();
                    }

                    EventListDao eventdao = new EventListDao();
                    try {
                        // ͬʱɾ���¼�������������
                        eventdao.delete(host.getId(), "network");
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        eventdao.close();
                    }

                    PortconfigDao portconfigdao = new PortconfigDao();
                    try {
                        // ͬʱɾ���˿����ñ�����������
                        portconfigdao.deleteByIpaddress(host.getIpAddress());
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        portconfigdao.close();
                    }

                    // ɾ��IP-MAC-BASE����Ķ�Ӧ������
                    IpMacChangeDao macchangebasedao = new IpMacChangeDao();
                    try {
                        // delte��,conn�Ѿ��ر�
                        macchangebasedao.deleteByHostIp(host.getIpAddress());
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        macchangebasedao.close();
                    }

                    // ɾ�������豸�����ļ�����Ķ�Ӧ������
                    NetNodeCfgFileDao dao = new NetNodeCfgFileDao();
                    try {
                        // delte��,conn�Ѿ��ر�
                        dao.deleteByHostIp(host.getIpAddress());
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        dao.close();
                    }

                    // ɾ�������豸SYSLOG���ձ���Ķ�Ӧ������
                    NetSyslogDao syslogdao = new NetSyslogDao();
                    try {
                        // delte��,conn�Ѿ��ر�
                        syslogdao.deleteByHostIp(host.getIpAddress());
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        syslogdao.close();
                    }

                    // ɾ�������豸�˿�ɨ�����Ķ�Ӧ������
                    PortScanDao portscandao = new PortScanDao();
                    try {
                        // delte��,conn�Ѿ��ر�
                        portscandao.deleteByHostIp(host.getIpAddress());
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        portscandao.close();
                    }

                    // ɾ�������豸���ͼ����Ķ�Ӧ������
                    IpaddressPanelDao addresspaneldao = new IpaddressPanelDao();
                    try {
                        // delte��,conn�Ѿ��ر�
                        addresspaneldao.deleteByHostIp(host.getIpAddress());
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        addresspaneldao.close();
                    }

                    // ɾ�������豸�ӿڱ���Ķ�Ӧ������
                    HostInterfaceDao interfacedao = new HostInterfaceDao();
                    try {
                        // delte��,conn�Ѿ��ر�
                        interfacedao.deleteByHostId(host.getId() + "");
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        interfacedao.close();
                    }

                    // ɾ�������豸IP��������Ķ�Ӧ������
                    IpAliasDao ipaliasdao = new IpAliasDao();
                    try {
                        // delte��,conn�Ѿ��ر�
                        ipaliasdao.deleteByHostIp(host.getIpAddress());
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        ipaliasdao.close();
                    }

                    // ɾ�������豸�ֹ����õ���·����Ķ�Ӧ������
                    RepairLinkDao repairdao = new RepairLinkDao();
                    try {
                        // delte��,conn�Ѿ��ر�
                        repairdao.updatestartlinkip(host.getIpAddress(), ipaddress);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        repairdao.close();
                    }
                    // ɾ�������豸�ֹ����õ���·����Ķ�Ӧ������
                    repairdao = new RepairLinkDao();
                    try {
                        // delte��,conn�Ѿ��ر�
                        repairdao.updateendlinkip(host.getIpAddress(), ipaddress);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        repairdao.close();
                    }

                    // ɾ�������豸IPMAC����Ķ�Ӧ������
                    IpMacDao ipmacdao = new IpMacDao();
                    try {
                        // delte��,conn�Ѿ��ر�
                        ipmacdao.deleteByHostIp(host.getIpAddress());
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        ipmacdao.close();
                    }
                    // ɾ��nms_ipmacchange����Ķ�Ӧ������
                    IpMacBaseDao macbasedao = new IpMacBaseDao();
                    try {
                        // delte��,conn�Ѿ��ر�
                        macbasedao.deleteByHostIp(host.getIpAddress());
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        macbasedao.close();
                    }
                    host.setIpAddress(ipaddress);
                    vo.setIpAddress(ipaddress);
                    // ������ʱ��
                    // ���������豸��
                    ip = ipaddress;//	  			
                    allipstr = SysUtil.doip(ip);
                    try {
                        ctable.createTable(conn, "ping", allipstr, "ping");// Ping
                        ctable.createTable(conn, "pinghour", allipstr, "pinghour");// Ping
                        ctable.createTable(conn, "pingday", allipstr, "pingday");// Ping

                        ctable.createTable(conn, "pro", allipstr, "pro");// ����
                        ctable.createTable(conn, "prohour", allipstr, "prohour");// ����Сʱ
                        ctable.createTable(conn, "proday", allipstr, "proday");// ������

                        ctable.createTable(conn, "memory", allipstr, "mem");// �ڴ�
                        ctable.createTable(conn, "memoryhour", allipstr, "memhour");// �ڴ�
                        ctable.createTable(conn, "memoryday", allipstr, "memday");// �ڴ�

                        ctable.createTable(conn, "cpu", allipstr, "cpu");// CPU
                        ctable.createTable(conn, "cpuhour", allipstr, "cpuhour");// CPU
                        ctable.createTable(conn, "cpuday", allipstr, "cpuday");// CPU

                        ctable.createTable(conn, "utilhdxperc", allipstr, "hdperc");
                        ctable.createTable(conn, "hdxperchour", allipstr, "hdperchour");
                        ctable.createTable(conn, "hdxpercday", allipstr, "hdpercday");

                        ctable.createTable(conn, "utilhdx", allipstr, "hdx");
                        ctable.createTable(conn, "utilhdxhour", allipstr, "hdxhour");
                        ctable.createTable(conn, "utilhdxday", allipstr, "hdxday");

                        ctable.createTable(conn, "allutilhdx", allipstr, "allhdx");
                        ctable.createTable(conn, "autilhdxh", allipstr, "ahdxh");
                        ctable.createTable(conn, "autilhdxd", allipstr, "ahdxd");

                        ctable.createTable(conn, "discardsperc", allipstr, "dcardperc");
                        ctable.createTable(conn, "dcarperh", allipstr, "dcarperh");
                        ctable.createTable(conn, "dcarperd", allipstr, "dcarperd");

                        ctable.createTable(conn, "errorsperc", allipstr, "errperc");
                        ctable.createTable(conn, "errperch", allipstr, "errperch");
                        ctable.createTable(conn, "errpercd", allipstr, "errpercd");

                        ctable.createTable(conn, "packs", allipstr, "packs");
                        ctable.createTable(conn, "packshour", allipstr, "packshour");
                        ctable.createTable(conn, "packsday", allipstr, "packsday");

                        ctable.createTable(conn, "inpacks", allipstr, "inpacks");
                        ctable.createTable(conn, "ipacksh", allipstr, "ipacksh");
                        ctable.createTable(conn, "ipackd", allipstr, "ipackd");

                        ctable.createTable(conn, "outpacks", allipstr, "outpacks");
                        ctable.createTable(conn, "opackh", allipstr, "opackh");
                        ctable.createTable(conn, "opacksd", allipstr, "opacksd");

                        ctable.createTable(conn, "cpudtl", allipstr, "cpudtl");
                        ctable.createTable(conn, "cpudtlhour", allipstr, "cpudtlhour");
                        ctable.createTable(conn, "cpudtlday", allipstr, "cpudtlday");

                        ctable.createTable(conn, "disk", allipstr, "disk");// yangjun
                        ctable.createTable(conn, "diskhour", allipstr, "diskhour");
                        ctable.createTable(conn, "diskday", allipstr, "diskday");

                        ctable.createTable(conn, "diskincre", allipstr, "diskincre");// ����������yangjun
                        ctable.createTable(conn, "diskinch", allipstr, "diskinch");// ����������Сʱ
                        ctable.createTable(conn, "diskincd", allipstr, "diskincd");// ������������

                        ctable.createSyslogTable(conn, "log", allipstr, "log");// ������
                        ctable.createTable(conn, "software", allipstr, "software");

                        conn.commit();
                    } catch (Exception e) {
                        // e.printStackTrace();
                    } finally {
                        try {
                            conn.executeBatch();
                        } catch (Exception e) {

                        }
                        // conn.close();
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                conn.close();
            }
        }
        String[] fs = getParaArrayValue("fcheckbox");
        String faci_str = "";
        if (fs != null && fs.length > 0) {
            for (int i = 0; i < fs.length; i++) {

                String fa = fs[i];
                faci_str = faci_str + fa + ",";
            }
        }

        NetSyslogNodeRuleDao nodeRuleDao = new NetSyslogNodeRuleDao();
        NetSyslogNodeRule noderule = null;
        try {
            noderule = (NetSyslogNodeRule) nodeRuleDao.findByID(vo.getId() + "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        String strSql = "";
        if (noderule == null) {
            strSql = "insert into nms_netsyslogrule_node(id,nodeid,facility)values(0,'" + vo.getId() + "','" + faci_str + "')";
        } else {
            strSql = "update nms_netsyslogrule_node set facility='" + faci_str + "' where nodeid='" + vo.getId() + "'";
        }
        try {
            nodeRuleDao.saveOrUpdate(strSql);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            nodeRuleDao.close();
        }

        // nielin add for time-sharing at 2010-01-04 start
        TimeShareConfigUtil timeShareConfigUtil = new TimeShareConfigUtil();
        try {
            boolean result = timeShareConfigUtil.saveTimeShareConfigList(request, String.valueOf(vo.getId()), timeShareConfigUtil.getObjectType("0"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        // nielin add for time-sharing at 2010-01-04 end

        /* snow add for time-garther at 2010-05-17 */
        TimeGratherConfigUtil timeGratherConfigUtil = new TimeGratherConfigUtil();
        try {
            boolean result2 = timeGratherConfigUtil.saveTimeGratherConfigList(request, String.valueOf(vo.getId()), timeGratherConfigUtil.getObjectType("0"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        /* snow close */

        vo.setEndpoint(host.getEndpoint());
        // �������ݿ�
        // DaoInterface dao = new HostNodeDao();
        setTarget("/network.do?action=list");

        // HostNodeDao hostNodeDao = new HostNodeDao();
        // Hashtable nodehash = new Hashtable();
        // try{
        // List hostlist = hostNodeDao.loadIsMonitored(1);
        // if(hostlist != null && hostlist.size()>0){
        // for(int i=0;i<hostlist.size();i++){
        // HostNode node = (HostNode)hostlist.get(i);
        // if(nodehash.containsKey(node.getCategory()+"")){
        // ((List)nodehash.get(node.getCategory()+"")).add(node);
        // }else{
        // List nodelist = new ArrayList();
        // nodelist.add(node);
        // nodehash.put(node.getCategory()+"", nodelist);
        // }
        // }
        // }
        // }catch(Exception e){
        //			
        // }finally{
        // hostNodeDao.close();
        // }
        // ShareData.setNodehash(nodehash);
        HostNodeDao _hostNodeDao = new HostNodeDao();
        try {
        	//ͬ���޸�����ͼ   --by hipo
        	HostNode node = new HostNode();
            node = _hostNodeDao.loadHost(vo.getId());
            if (!node.getAlias().trim().equals(vo.getAlias().trim())) {
            	ManageXmlOperator mXmlOpr = new ManageXmlOperator();
        		mXmlOpr.setFile("network.jsp");
        		mXmlOpr.init4updateXml();
        		if (mXmlOpr.isNodeExist("net" + vo.getId())) {
        			mXmlOpr.updateNode("net" + vo.getId(), "alias", vo.getAlias());
        			String info = "�豸��ǩ:" + vo.getAlias() + "<br>IP��ַ:" + vo.getIpAddress();
        			mXmlOpr.updateNode("net" + vo.getId(), "info", info);
        		} else {
        			SysLogger.error("NetworkManager.update:"+"����ͼû�иýڵ�");
        		}
        		mXmlOpr.writeXml();
			}
            if (!node.getIpAddress().trim().equals(vo.getIpAddress().trim())) {
            	ManageXmlOperator mXmlOpr = new ManageXmlOperator();
        		mXmlOpr.setFile("network.jsp");
        		mXmlOpr.init4updateXml();
        		if (mXmlOpr.isNodeExist("net" + vo.getId())) {
        			mXmlOpr.updateNode("net" + vo.getId(), "ip", vo.getIpAddress());
        		} else {
        			SysLogger.error("NetworkManager.update:"+"����ͼû�иýڵ�");
        		}
        		mXmlOpr.writeXml();
			}
            //---end
            _hostNodeDao.update(vo);
        } catch (Exception e) {

        } finally {
            _hostNodeDao.close();
        }
        // update(dao,vo);

        _hostNodeDao = new HostNodeDao();
        Hashtable nodehash = ShareData.getNodehash();
        try {
            List hostlist = _hostNodeDao.loadIsMonitored(1);
            if (hostlist != null && hostlist.size() > 0) {
                for (int i = 0; i < hostlist.size(); i++) {
                    HostNode node = (HostNode) hostlist.get(i);
                    // SysLogger.info("node alias:==="+node.getAlias());
                    if (nodehash.containsKey(node.getCategory() + "")) {
                        List list = (List) nodehash.get(node.getCategory() + "");
                        for (int k = 0; k < list.size(); k++) {
                            HostNode exitnode = (HostNode) list.get(k);
                            if (exitnode.getId() == node.getId()) {
                                list.remove(k);
                                list.add(k, node);
                                break;
                            }
                        }
                        nodehash.put(node.getCategory() + "", list);
                        // ((List)nodehash.get(node.getCategory()+"")).add(node);
                    } else {
                        List nodelist = new ArrayList();
                        nodelist.add(node);
                        nodehash.put(node.getCategory() + "", nodelist);
                    }
                }
            }
        } catch (Exception e) {

        } finally {
            _hostNodeDao.close();
        }
        ShareData.setNodehash(nodehash);

        return list();
    }

    private String updatealias() {
        HostNode vo = new HostNode();
        vo.setId(getParaIntValue("id"));
        vo.setAlias(getParaValue("alias"));
        vo.setSendemail(getParaValue("sendemail"));
        vo.setSendmobiles(getParaValue("sendmobiles"));
        vo.setBid(getParaValue("bid"));
        // vo.setManaged(getParaIntValue("managed")==1?true:false);

        // �����ڴ�
        Host host = (Host) PollingEngine.getInstance().getNodeByID(vo.getId());
        host.setAlias(vo.getAlias());
        host.setManaged(vo.isManaged());
        host.setSendemail(vo.getSendemail());
        host.setSendmobiles(vo.getSendmobiles());
        host.setBid(vo.getBid());
        vo.setManaged(true);

        // �������ݿ�
        HostNodeDao dao = new HostNodeDao();
        try {
            dao.update(vo);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dao.close();
        }
        setTarget("/network.do?action=list");

        HostNodeDao hostNodeDao = new HostNodeDao();
        Hashtable nodehash = ShareData.getNodehash();
        try {
            List hostlist = hostNodeDao.loadIsMonitored(1);
            if (hostlist != null && hostlist.size() > 0) {
                for (int i = 0; i < hostlist.size(); i++) {
                    HostNode node = (HostNode) hostlist.get(i);
                    SysLogger.info("node alias:===" + node.getAlias());
                    if (nodehash.containsKey(node.getCategory() + "")) {
                        List list = (List) nodehash.get(node.getCategory() + "");
                        for (int k = 0; k < list.size(); k++) {
                            HostNode exitnode = (HostNode) list.get(k);
                            if (exitnode.getId() == node.getId()) {
                                list.remove(k);
                                list.add(k, node);
                                break;
                            }
                        }
                        nodehash.put(node.getCategory() + "", list);
                        // ((List)nodehash.get(node.getCategory()+"")).add(node);
                    } else {
                        List nodelist = new ArrayList();
                        nodelist.add(node);
                        nodehash.put(node.getCategory() + "", nodelist);
                    }
                }
            }
        } catch (Exception e) {

        } finally {
            hostNodeDao.close();
        }
        ShareData.setNodehash(nodehash);

        if (ShareData.getNodehash() != null) {
            String category = "1,2,3,7";
            String[] categorys = category.split(",");
            if (categorys != null && categorys.length > 0) {
                // SysLogger.info("categorys
                // length=========="+categorys.length);
                for (int k = 0; k < categorys.length; k++) {
                    String category_per = categorys[k];

                    if (ShareData.getNodehash().containsKey(category_per)) {
                        List templist = (List) ShareData.getNodehash().get(category_per);
                        if (templist != null && templist.size() > 0) {
                            for (int j = 0; j < templist.size(); j++) {
                                HostNode hostnode = (HostNode) templist.get(j);
                                SysLogger.info("-----" + category_per + "----" + hostnode.getIpAddress() + "---" + hostnode.getAlias());
                                // list.add((HostNode)templist.get(j));
                            }
                        }
                    }
                }
            }

        }

        dao = new HostNodeDao();
        return update(dao, vo);
    }

    // wxy add
    private String updateAlias() {
        HostNode vo = new HostNode();
        vo.setId(getParaIntValue("id"));
        vo.setIpAddress(getParaValue("ip"));
        vo.setAlias(getParaValue("alias"));
        vo.setSendemail(getParaValue("sendemail"));
        vo.setSendmobiles(getParaValue("sendmobiles"));
        vo.setBid(getParaValue("bid"));

        // �����ڴ�
        Host host = (Host) PollingEngine.getInstance().getNodeByID(vo.getId());

        host.setAlias(vo.getAlias());
        host.setIpAddress(vo.getIpAddress());
        host.setManaged(vo.isManaged());
        host.setSendemail(vo.getSendemail());
        host.setSendmobiles(vo.getSendmobiles());
        host.setBid(vo.getBid());
        vo.setManaged(true);

        // �������ݿ�
        HostNodeDao dao = new HostNodeDao();
        dao.editAlias(vo);

        String path = "/network.do?action=list";
        HostNodeDao hostNodeDao = new HostNodeDao();
        Hashtable nodehash = new Hashtable();
        try {
            List hostlist = hostNodeDao.loadIsMonitored(1);
            if (hostlist != null && hostlist.size() > 0) {
                for (int i = 0; i < hostlist.size(); i++) {
                    HostNode node = (HostNode) hostlist.get(i);
                    if (nodehash.containsKey(node.getCategory() + "")) {
                        ((List) nodehash.get(node.getCategory() + "")).add(node);
                    } else {
                        List nodelist = new ArrayList();
                        nodelist.add(node);
                        nodehash.put(node.getCategory() + "", nodelist);
                    }
                }
            }
        } catch (Exception e) {

        } finally {
            hostNodeDao.close();
        }
        ShareData.setNodehash(nodehash);

        return path;
    }

    private String updatesysgroup() {
        HostNode vo = new HostNode();
        HostNodeDao dao = new HostNodeDao();
        try {
            vo = dao.loadHost(getParaIntValue("id"));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dao.close();
        }
        vo.setId(getParaIntValue("id"));
        vo.setSysName(getParaValue("sysname"));
        vo.setSysContact(getParaValue("syscontact"));
        vo.setSysLocation(getParaValue("syslocation"));

        Hashtable mibvalues = new Hashtable();
        mibvalues.put("sysContact", getParaValue("syscontact"));
        mibvalues.put("sysName", getParaValue("sysname"));
        mibvalues.put("sysLocation", getParaValue("syslocation"));

        // �������ݿ�
        // dao.close();
        dao = new HostNodeDao();
        boolean flag = false;
        try {
            flag = dao.updatesysgroup(vo, mibvalues);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dao.close();
        }
        if (flag) {
            // �����ڴ�
            Host host = (Host) PollingEngine.getInstance().getNodeByID(vo.getId());
            host.setSysName(getParaValue("sysname"));
            host.setSysContact(getParaValue("syscontact"));
            host.setSysLocation(getParaValue("syslocation"));
        }
        return "/topology/network/networkview.jsp?id=" + vo.getId() + "&ipaddress=" + vo.getIpAddress();
    }

    private String updatesnmp() {
        HostNode vo = new HostNode();
        HostNodeDao dao = new HostNodeDao();
        try {
            vo = dao.loadHost(getParaIntValue("id"));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dao.close();
        }
        vo.setId(getParaIntValue("id"));
        vo.setCommunity(getParaValue("readcommunity"));
        vo.setWriteCommunity(getParaValue("writecommunity"));
        vo.setSnmpversion(getParaIntValue("snmpversion"));

        // �������ݿ�
        // dao.close();
        dao = new HostNodeDao();
        // �����ڴ�
        Host host = (Host) PollingEngine.getInstance().getNodeByID(vo.getId());
        host.setCommunity(getParaValue("readcommunity"));
        host.setWritecommunity(getParaValue("writecommunity"));
        host.setSnmpversion(getParaIntValue("snmpversion"));
        try {
            dao.updatesnmp(vo);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dao.close();
        }
        return "/topology/network/networkview.jsp?id=" + vo.getId() + "&ipaddress=" + vo.getIpAddress();
    }

    private String refreshsysname() {
        HostNodeDao dao = new HostNodeDao();
        String sysName = "";
        try {
            sysName = dao.refreshSysName(getParaIntValue("id"));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dao.close();
        }

        // �����ڴ�
        Host host = (Host) PollingEngine.getInstance().getNodeByID(getParaIntValue("id"));
        if (host != null) {
            host.setSysName(sysName);
            host.setAlias(sysName);
        }

        return "/network.do?action=list";
    }

    private String delete() {
        SysLogger.info("��ʼɾ���豸................");
        String[] ids = getParaArrayValue("checkbox");
        if (ids != null && ids.length > 0) {
            // �����޸�
            // TaskUtil taskutil = new TaskUtil();
            for (int i = 0; i < ids.length; i++) {
                String id = ids[i];
                // ȡ���ɼ�����
                // taskutil.removeTask(id+"");
                PollingEngine.getInstance().deleteNodeByID(Integer.parseInt(id));
                HostNodeDao dao = new HostNodeDao();
                HostNode host = null;
                try {
                    host = (HostNode) dao.findByID(id);
                    dao.delete(id);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    dao.close();
                }
                LinkDao linkdao = new LinkDao();
                List linklist = new ArrayList();
                Link link = null;
                try {
                    linklist = linkdao.findByNodeId(host.getId() + "");
                    if (linklist != null && linklist.size() > 0) {
                        link = (Link) linklist.get(0);
                    }
                } catch (Exception e) {

                } finally {
                    linkdao.close();
                }

                // ˢ���ڴ��вɼ�ָ��
                NodeGatherIndicatorsUtil gatherutil = new NodeGatherIndicatorsUtil();
                gatherutil.refreshShareDataGather();

                String ip = host.getIpAddress();
                // String ip1 ="",ip2="",ip3="",ip4="";
                // String[] ipdot = ip.split(".");
                // String tempStr = "";
                // String allipstr = "";
                // if (ip.indexOf(".")>0){
                // ip1=ip.substring(0,ip.indexOf("."));
                // ip4=ip.substring(ip.lastIndexOf(".")+1,ip.length());
                // tempStr =
                // ip.substring(ip.indexOf(".")+1,ip.lastIndexOf("."));
                // }
                // ip2=tempStr.substring(0,tempStr.indexOf("."));
                // ip3=tempStr.substring(tempStr.indexOf(".")+1,tempStr.length());
                // allipstr=ip1+ip2+ip3+ip4;
                String allipstr = SysUtil.doip(ip);
                CreateTableManager ctable = new CreateTableManager();
                DBManager conn = new DBManager();
                try {
                    if (host.getCategory() < 4 || host.getCategory() == 7 || host.getCategory() == 8 || host.getCategory() == 9 || host.getCategory() == 10 || host.getCategory() == 11 || host.getCategory() == 12 || host.getCategory() == 13 || host.getCategory() == 14 || host.getCategory() == 15 || host.getCategory() == 16 || host.getCategory() == 17) {
                        // ��ɾ�������豸��
                        // ��ͨ��
                        try {
                            ctable.deleteTable(conn, "ping", allipstr, "ping");// Ping
                        } catch (Exception e) {

                        }
                        try {
                            ctable.deleteTable(conn, "pinghour", allipstr, "pinghour");// Ping
                        } catch (Exception e) {

                        }
                        try {
                            ctable.deleteTable(conn, "pingday", allipstr, "pingday");// Ping
                        } catch (Exception e) {

                        }

                        // �ڴ�
                        try {
                            ctable.deleteTable(conn, "memory", allipstr, "mem");// �ڴ�
                        } catch (Exception e) {

                        }
                        try {
                            ctable.deleteTable(conn, "memoryhour", allipstr, "memhour");// �ڴ�
                        } catch (Exception e) {

                        }
                        try {
                            ctable.deleteTable(conn, "memoryday", allipstr, "memday");// �ڴ�
                        } catch (Exception e) {

                        }

                        try {
                            ctable.deleteTable(conn, "portstatus", allipstr, "port");// �˿�״̬
                        } catch (Exception e) {

                        }

                        if (host.getCategory() != 12) {
                            // VPN����ʱ��û�����±���Ϣ
                            ctable.deleteTable(conn, "flash", allipstr, "flash");// ����
                            ctable.deleteTable(conn, "flashhour", allipstr, "flashhour");// ����
                            ctable.deleteTable(conn, "flashday", allipstr, "flashday");// ����

                            ctable.deleteTable(conn, "buffer", allipstr, "buffer");// ����
                            ctable.deleteTable(conn, "bufferhour", allipstr, "bufferhour");// ����
                            ctable.deleteTable(conn, "bufferday", allipstr, "bufferday");// ����

                            ctable.deleteTable(conn, "fan", allipstr, "fan");// ����
                            ctable.deleteTable(conn, "fanhour", allipstr, "fanhour");// ����
                            ctable.deleteTable(conn, "fanday", allipstr, "fanday");// ����

                            ctable.deleteTable(conn, "power", allipstr, "power");// ��Դ
                            ctable.deleteTable(conn, "powerhour", allipstr, "powerhour");// ��Դ
                            ctable.deleteTable(conn, "powerday", allipstr, "powerday");// ��Դ

                            ctable.deleteTable(conn, "vol", allipstr, "vol");// ��ѹ
                            ctable.deleteTable(conn, "volhour", allipstr, "volhour");// ��ѹ
                            ctable.deleteTable(conn, "volday", allipstr, "volday");// ��ѹ
                        }
                        if (host.getCategory() == 13) {
                            // CMTS�豸
                            ctable.deleteTable(conn, "status", allipstr, "status");// ͨ��״̬
                            ctable.deleteTable(conn, "statushour", allipstr, "statushour");// ͨ��״̬
                            ctable.deleteTable(conn, "statusday", allipstr, "statusday");// ͨ��״̬

                            ctable.deleteTable(conn, "noise", allipstr, "noise");// ͨ�������
                            ctable.deleteTable(conn, "noisehour", allipstr, "noisehour");// ͨ�������
                            ctable.deleteTable(conn, "noiseday", allipstr, "noiseday");// ͨ�������

                            ctable.deleteTable(conn, "ipmac", allipstr, "ipmac");// IPMAC��Ϣ�������û���Ϣ��
                        } else if (host.getCategory() == 14) {
                            // �洢�豸��
                            ctable.deleteTable(conn, "pings", allipstr, "pings");// ��ͨ��
                            ctable.deleteTable(conn, "pinghours", allipstr, "pinghours");// ��ͨ��
                            ctable.deleteTable(conn, "pingdays", allipstr, "pingdays");// ��ͨ��

                            ctable.deleteTable(conn, "env", allipstr, "env");//
                            ctable.deleteTable(conn, "efan", allipstr, "efan");//
                            ctable.deleteTable(conn, "epower", allipstr, "epower");//
                            ctable.deleteTable(conn, "eenv", allipstr, "eenv");//
                            ctable.deleteTable(conn, "edrive", allipstr, "edrive");//

                            ctable.deleteTable(conn, "rcpu", allipstr, "rcpu");
                            ctable.deleteTable(conn, "rcable", allipstr, "rcable");// ����״�壺�ڲ�����״̬
                            ctable.deleteTable(conn, "rcache", allipstr, "rcache");// ����״�壺����״̬
                            ctable.deleteTable(conn, "rmemory", allipstr, "rmemory");// ����״�壺�����ڴ�״̬
                            ctable.deleteTable(conn, "rpower", allipstr, "rpower");// ����״�壺��Դ״̬
                            ctable.deleteTable(conn, "rbutter", allipstr, "rbutter");// ����״�壺���״̬
                            ctable.deleteTable(conn, "rfan", allipstr, "rfan");// ����״�壺����״̬
                            ctable.deleteTable(conn, "renv", allipstr, "renv");// �洢�豸����-����״̬

                            ctable.deleteTable(conn, "rluncon", allipstr, "rluncon");
                            ctable.deleteTable(conn, "rsluncon", allipstr, "rsluncon");
                            ctable.deleteTable(conn, "rwwncon", allipstr, "rwwncon");
                            ctable.deleteTable(conn, "rsafety", allipstr, "rsafety");
                            ctable.deleteTable(conn, "rnumber", allipstr, "rnumber");
                            ctable.deleteTable(conn, "rswitch", allipstr, "rswitch");

                            ctable.deleteTable(conn, "events", allipstr, "events");// �¼�

                            ctable.deleteTable(conn, "emcdiskper", allipstr, "emcdiskper");
                            ctable.deleteTable(conn, "emclunper", allipstr, "emclunper");
                            ctable.deleteTable(conn, "emcenvpower", allipstr, "emcenvpower");
                            ctable.deleteTable(conn, "emcenvstore", allipstr, "emcenvstore");
                            ctable.deleteTable(conn, "emcbakpower", allipstr, "emcbakpower");
                            if (host.getOstype() == 44) {// hp�洢ɾ��ping�� ��
                                                            // ��������
                                ctable.deleteTable(conn, "ping", allipstr, "ping");// ��ͨ��
                                ctable.deleteTable(conn, "pinghour", allipstr, "pinghour");// ��ͨ��
                                ctable.deleteTable(conn, "pingday", allipstr, "pingday");// ��ͨ��
                            }

                        } else if (host.getCategory() == 15) {
                            // VMWare�豸��
                            ctable.deleteTable(conn, "pings", allipstr, "pings");// Ping
                            ctable.deleteTable(conn, "pinghours", allipstr, "pinghours");// Ping
                            ctable.deleteTable(conn, "pingdays", allipstr, "pingdays");// Ping

                            ctable.deleteTable(conn, "memory", allipstr, "memory");// �ڴ�������
                            ctable.deleteTable(conn, "memoryhour", allipstr, "memhour");// �ڴ�
                            ctable.deleteTable(conn, "memoryday", allipstr, "memday");// �ڴ�

                            ctable.deleteTable(conn, "cpu", allipstr, "cpu");// CPU
                            ctable.deleteTable(conn, "cpuhour", allipstr, "cpuhour");// CPU
                            ctable.deleteTable(conn, "cpuday", allipstr, "cpuday");// CPU

                            ctable.deleteTable(conn, "state", allipstr, "state");// �������Դ״�����򿪻�رգ���
                            ctable.deleteTable(conn, "gstate", allipstr, "gstate");// �ͻ�������ϵͳ��״��������أ���

                            ctable.deleteTable(conn, "pings", allipstr, "pings");// ��ͨ��
                            ctable.deleteTable(conn, "pinghours", allipstr, "pinghours");// ��ͨ��
                            ctable.deleteTable(conn, "pingdays", allipstr, "pingdays");// ��ͨ��

                            ctable.deleteTable(conn, "vm_host", allipstr, "vm_host");// ����VMWare
                            // �������������Ϣ��
                            ctable.deleteTable(conn, "vm_guesthost", allipstr, "vm_guesthost");// ����VMWare
                            // �������������Ϣ��
                            ctable.deleteTable(conn, "vm_cluster", allipstr, "vm_cluster");// ����VMWare
                            // ��Ⱥ��������Ϣ��
                            ctable.deleteTable(conn, "vm_datastore", allipstr, "vm_datastore");// ����VMWare
                            // �洢��������Ϣ��
                            ctable.deleteTable(conn, "vm_resourcepool", allipstr, "vm_resourcepool");// ����VMWare
                            // ��Դ�ص�������Ϣ��
                            // vm_basephysical
                            ctable.deleteTable(conn, "vm_basephysical", allipstr, "vm_basephysical");// ����VMWare
                            // ������Ļ�����Ϣ��
                            ctable.deleteTable(conn, "vm_basevmware", allipstr, "vm_basevmware");// ����VMWare
                            // ������Ļ�����Ϣ��
                            ctable.deleteTable(conn, "vm_baseyun", allipstr, "vm_baseyun");// ����VMWare
                            // ����Դ�Ļ�����Ϣ��
                            ctable.deleteTable(conn, "vm_basedatastore", allipstr, "vm_basedatastore");// ����VMWare
                            // �洢�Ļ�����Ϣ��
                            ctable.deleteTable(conn, "vm_basedatacenter", allipstr, "vm_basedatacenter");// ����VMWare
                            // �������ĵĻ�����Ϣ��
                            ctable.deleteTable(conn, "vm_baseresource", allipstr, "vm_baseresource");// ����VMWare
                            // ��Դ�صĻ�����Ϣ��
                        } else if (host.getCategory() == 16) {// aircondition
                            // ɾ���յ���ʱ���е�����
                            String[] nmsTempDataTables = { "nms_emeairconhum", "nms_emeairconparinfo", "nms_emeaircontem" };
                            CreateTableManager createTableManager = new CreateTableManager();
                            String[] uniqueKeyValues = { host.getIpAddress() };
                            createTableManager.clearTablesData(nmsTempDataTables, "ipaddress", uniqueKeyValues);
                        } else if (host.getCategory() == 17) {// UPS
                            ctable.deleteTable(conn, "input", allipstr, "input");
                            ctable.deleteTable(conn, "inputhour", allipstr, "inputhour");
                            ctable.deleteTable(conn, "inputday", allipstr, "inputday");
                            ctable.deleteTable(conn, "output", allipstr, "output");
                            ctable.deleteTable(conn, "outputhour", allipstr, "outputhour");
                            ctable.deleteTable(conn, "outputday", allipstr, "outputday");
                        }

                        // CPU
                        try {
                            ctable.deleteTable(conn, "cpu", allipstr, "cpu");// CPU
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            ctable.deleteTable(conn, "cpuhour", allipstr, "cpuhour");// CPU
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            ctable.deleteTable(conn, "cpuday", allipstr, "cpuday");// CPU
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        // ����������
                        try {
                            ctable.deleteTable(conn, "utilhdxperc", allipstr, "hdperc");
                        } catch (Exception e) {

                        }
                        try {
                            ctable.deleteTable(conn, "hdxperchour", allipstr, "hdperchour");
                        } catch (Exception e) {

                        }
                        try {
                            // SysLogger.info(allipstr+"==============hdxpercday");
                            ctable.deleteTable(conn, "hdxpercday", allipstr, "hdpercday");
                        } catch (Exception e) {
                        }

                        // //�˿�״̬
                        // try{
                        // ctable.deleteTable(conn,"portstatus",allipstr,"port");
                        // }catch(Exception e){
                        // }
                        // try{
                        // ctable.deleteTable(conn,"portstatushour",allipstr,"porthour");
                        // }catch(Exception e){
                        // }
                        // try{
                        // ctable.deleteTable(conn,"portstatusday",allipstr,"portday");
                        // }catch(Exception e){
                        // }

                        // ����
                        try {
                            ctable.deleteTable(conn, "utilhdx", allipstr, "hdx");
                        } catch (Exception e) {

                        }
                        try {
                            ctable.deleteTable(conn, "utilhdxhour", allipstr, "hdxhour");
                        } catch (Exception e) {

                        }
                        try {
                            ctable.deleteTable(conn, "utilhdxday", allipstr, "hdxday");
                        } catch (Exception e) {

                        }

                        // �ۺ�����
                        try {
                            ctable.deleteTable(conn, "allutilhdx", allipstr, "allhdx");
                        } catch (Exception e) {

                        }
                        try {
                            ctable.deleteTable(conn, "autilhdxh", allipstr, "ahdxh");
                        } catch (Exception e) {

                        }
                        try {
                            ctable.deleteTable(conn, "autilhdxd", allipstr, "ahdxd");
                        } catch (Exception e) {

                        }

                        // ������
                        try {
                            ctable.deleteTable(conn, "discardsperc", allipstr, "dcardperc");
                        } catch (Exception e) {

                        }
                        try {
                            ctable.deleteTable(conn, "dcarperh", allipstr, "dcarperh");
                        } catch (Exception e) {

                        }
                        try {
                            ctable.deleteTable(conn, "dcarperd", allipstr, "dcarperd");
                        } catch (Exception e) {

                        }

                        // ������
                        try {
                            ctable.deleteTable(conn, "errorsperc", allipstr, "errperc");
                        } catch (Exception e) {

                        }
                        try {
                            ctable.deleteTable(conn, "errperch", allipstr, "errperch");
                        } catch (Exception e) {

                        }
                        try {
                            ctable.deleteTable(conn, "errpercd", allipstr, "errpercd");
                        } catch (Exception e) {

                        }

                        // ���ݰ�
                        try {
                            ctable.deleteTable(conn, "packs", allipstr, "packs");
                        } catch (Exception e) {

                        }
                        try {
                            ctable.deleteTable(conn, "packshour", allipstr, "packshour");
                        } catch (Exception e) {

                        }
                        try {
                            ctable.deleteTable(conn, "packsday", allipstr, "packsday");
                        } catch (Exception e) {

                        }

                        // ������ݰ�
                        try {
                            ctable.deleteTable(conn, "inpacks", allipstr, "inpacks");
                        } catch (Exception e) {

                        }
                        try {
                            ctable.deleteTable(conn, "ipacksh", allipstr, "ipacksh");
                        } catch (Exception e) {

                        }
                        try {
                            ctable.deleteTable(conn, "ipackd", allipstr, "ipackd");
                        } catch (Exception e) {

                        }

                        // �������ݰ�
                        try {
                            ctable.deleteTable(conn, "outpacks", allipstr, "outpacks");
                        } catch (Exception e) {

                        }
                        try {
                            ctable.deleteTable(conn, "opackh", allipstr, "opackh");
                        } catch (Exception e) {

                        }
                        try {
                            ctable.deleteTable(conn, "opacksd", allipstr, "opacksd");
                        } catch (Exception e) {

                        }

                        // �¶�
                        try {
                            ctable.deleteTable(conn, "temper", allipstr, "temper");
                        } catch (Exception e) {

                        }
                        try {
                            ctable.deleteTable(conn, "temperh", allipstr, "temperh");
                        } catch (Exception e) {

                        }
                        try {
                            ctable.deleteTable(conn, "temperd", allipstr, "temperd");
                        } catch (Exception e) {

                        }

                        DiscoverCompleteDao dcDao = new DiscoverCompleteDao();
                        try {
                            dcDao.deleteMonitor(host.getId(), host.getIpAddress());
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            dcDao.close();
                            conn.close();
                        }

                        EventListDao eventdao = new EventListDao();
                        try {
                            // ͬʱɾ���¼�������������
                            if (host.getCategory() == 12) {
                                eventdao.delete(host.getId(), "vpn");
                            } else if (host.getCategory() == 13) {
                                eventdao.delete(host.getId(), "cmts");
                            } else if (host.getCategory() == 14) {
                                eventdao.delete(host.getId(), "storage");
                            } else if (host.getCategory() == 16) {
                                eventdao.delete(host.getId(), "air");
                            } else if (host.getCategory() == 17) {
                                eventdao.delete(host.getId(), "ups");
                            } else
                                eventdao.delete(host.getId(), "net");
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            eventdao.close();
                        }

                        PortconfigDao portconfigdao = new PortconfigDao();
                        try {
                            // ͬʱɾ���˿����ñ�����������
                            portconfigdao.deleteByIpaddress(host.getIpAddress());
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            portconfigdao.close();
                        }

                        AlarmPortDao portdao = new AlarmPortDao();
                        try {
                            // ͬʱɾ���˿ڼ������ñ�����������
                            portdao.deleteByIpaddress(host.getIpAddress());
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            portdao.close();
                        }

                        // ɾ��nms_ipmacchange����Ķ�Ӧ������
                        IpMacChangeDao macchangebasedao = new IpMacChangeDao();
                        try {
                            // delte��,conn�Ѿ��ر�
                            macchangebasedao.deleteByHostIp(host.getIpAddress());
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            macchangebasedao.close();
                        }

                        // ɾ�������豸�����ļ�����Ķ�Ӧ������
                        NetNodeCfgFileDao configdao = new NetNodeCfgFileDao();
                        try {
                            // delte��,conn�Ѿ��ر�
                            configdao.deleteByHostIp(host.getIpAddress());
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            configdao.close();
                        }

                        // ɾ�������豸SYSLOG���ձ���Ķ�Ӧ������
                        NetSyslogDao syslogdao = new NetSyslogDao();
                        try {
                            // delte��,conn�Ѿ��ر�
                            syslogdao.deleteByHostIp(host.getIpAddress());
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            syslogdao.close();
                        }

                        // ɾ�������豸�˿�ɨ�����Ķ�Ӧ������
                        PortScanDao portscandao = new PortScanDao();
                        try {
                            // delte��,conn�Ѿ��ر�
                            portscandao.deleteByHostIp(host.getIpAddress());
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            portscandao.close();
                        }

                        // ɾ�������豸���ͼ����Ķ�Ӧ������
                        IpaddressPanelDao addresspaneldao = new IpaddressPanelDao();
                        try {
                            // delte��,conn�Ѿ��ر�
                            addresspaneldao.deleteByHostIp(host.getIpAddress());
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            addresspaneldao.close();
                        }

                        // ɾ�������豸�ӿڱ���Ķ�Ӧ������
                        HostInterfaceDao interfacedao = new HostInterfaceDao();
                        try {
                            // delte��,conn�Ѿ��ر�
                            interfacedao.deleteByHostId(host.getId() + "");
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            interfacedao.close();
                        }

                        // ɾ�������豸IP��������Ķ�Ӧ������
                        IpAliasDao ipaliasdao = new IpAliasDao();
                        try {
                            // delte��,conn�Ѿ��ر�
                            ipaliasdao.deleteByHostIp(host.getIpAddress());
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            ipaliasdao.close();
                        }

                        // ɾ�������豸�ֹ����õ���·����Ķ�Ӧ������
                        RepairLinkDao repairdao = new RepairLinkDao();
                        try {
                            // delte��,conn�Ѿ��ر�
                            repairdao.deleteByHostIp(host.getIpAddress());
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            repairdao.close();
                        }

                        // ɾ�������豸IPMAC����Ķ�Ӧ������
                        IpMacDao ipmacdao = new IpMacDao();
                        try {
                            // delte��,conn�Ѿ��ر�
                            ipmacdao.deleteByHostIp(host.getIpAddress());
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            ipmacdao.close();
                        }

                        // delete vmwareconnectconfig db table
                        if (host.getCategory() == 15 && host.getOstype() == 40 && host.getCollecttype() == 10) {
                            VMWareConnectConfigDao vmwaredao = new VMWareConnectConfigDao();
                            try {
                                vmwaredao.delete(Long.parseLong(host.getId() + ""));
                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                vmwaredao.close();
                            }
                        }

                        // ɾ�����豸�Ĳɼ�ָ��
                        NodeGatherIndicatorsDao gatherdao = new NodeGatherIndicatorsDao();
                        try {
                            if (host.getCategory() == 12) {
                                gatherdao.deleteByNodeIdAndTypeAndSubtype(host.getId() + "", "vpn", "");
                            } else if (host.getCategory() == 13) {
                                gatherdao.deleteByNodeIdAndTypeAndSubtype(host.getId() + "", "cmts", "");
                            } else if (host.getCategory() == 14) {
                                gatherdao.deleteByNodeIdAndTypeAndSubtype(host.getId() + "", "storage", "");
                            } else if (host.getCategory() == 15) {
                                gatherdao.deleteByNodeIdAndTypeAndSubtype(host.getId() + "", "virtual", "");
                            } else if (host.getCategory() == 16) {
                                gatherdao.deleteByNodeIdAndTypeAndSubtype(host.getId() + "", "air", "");
                            } else if (host.getCategory() == 17) {
                                gatherdao.deleteByNodeIdAndTypeAndSubtype(host.getId() + "", "ups", "");
                            } else
                                gatherdao.deleteByNodeIdAndTypeAndSubtype(host.getId() + "", "net", "");
                        } catch (RuntimeException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } finally {
                            gatherdao.close();
                        }

                        // ɾ�������豸ָ��ɼ�����Ķ�Ӧ������
                        AlarmIndicatorsNodeDao indicatdao = new AlarmIndicatorsNodeDao();
                        try {
                            // delte��,conn�Ѿ��ر�
                            if (host.getCategory() == 12) {
                                indicatdao.deleteByNodeId(host.getId() + "", "vpn");
                            } else if (host.getCategory() == 13) {
                                indicatdao.deleteByNodeId(host.getId() + "", "cmts");
                            } else if (host.getCategory() == 14) {
                                indicatdao.deleteByNodeId(host.getId() + "", "storage");
                            } else if (host.getCategory() == 15) {
                                indicatdao.deleteByNodeId(host.getId() + "", "virtual");
                            } else if (host.getCategory() == 16) {
                                indicatdao.deleteByNodeId(host.getId() + "", "air");
                            } else if (host.getCategory() == 17) {
                                indicatdao.deleteByNodeId(host.getId() + "", "ups");
                            } else
                                indicatdao.deleteByNodeId(host.getId() + "", "net");
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            indicatdao.close();
                        }

                        // ɾ��IP-MAC-BASE����Ķ�Ӧ������
                        IpMacBaseDao macbasedao = new IpMacBaseDao();
                        try {
                            // delte��,conn�Ѿ��ر�
                            macbasedao.deleteByHostIp(host.getIpAddress());
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            macbasedao.close();
                        }
                        // ɾ���豸��ǰ���¸澯��Ϣ���е�����
                        NodeAlarmUtil.deleteByDeviceIdAndDeviceType(host);

                        // ɾ��SYSLOG�����
                        NetSyslogNodeRuleDao ruledao = new NetSyslogNodeRuleDao();
                        try {
                            ruledao.deleteByNodeID(host.getId() + "");
                        } catch (Exception e) {

                        } finally {
                            ruledao.close();
                        }
                    } else if (host.getCategory() == 4) {
                        // ɾ������������
                        try {
                            ctable.deleteTable(conn, "pro", allipstr, "pro");// ����
                        } catch (Exception e) {

                        }
                        try {
                            ctable.deleteTable(conn, "prohour", allipstr, "prohour");// ����Сʱ
                        } catch (Exception e) {

                        }
                        try {
                            ctable.deleteTable(conn, "proday", allipstr, "proday");// ������
                        } catch (Exception e) {

                        }

                        try {
                            ctable.deleteTable(conn, "log", allipstr, "log");// ������
                        } catch (Exception e) {

                        }

                        try {
                            ctable.deleteTable(conn, "memory", allipstr, "mem");// �ڴ�
                        } catch (Exception e) {

                        }
                        try {
                            ctable.deleteTable(conn, "memoryhour", allipstr, "memhour");// �ڴ�
                        } catch (Exception e) {

                        }
                        try {
                            ctable.deleteTable(conn, "memoryday", allipstr, "memday");// �ڴ�
                        } catch (Exception e) {

                        }

                        try {
                            ctable.deleteTable(conn, "cpu", allipstr, "cpu");// CPU
                        } catch (Exception e) {

                        }
                        try {
                            ctable.deleteTable(conn, "cpuhour", allipstr, "cpuhour");// CPU
                        } catch (Exception e) {

                        }
                        try {
                            ctable.deleteTable(conn, "cpuday", allipstr, "cpuday");// CPU
                        } catch (Exception e) {

                        }
                        try {
                            ctable.deleteTable(conn, "cpudtl", allipstr, "cpudtl");// CPU
                        } catch (Exception e) {

                        }
                        try {
                            ctable.deleteTable(conn, "cpudtlhour", allipstr, "cpudtlhour");// CPU
                        } catch (Exception e) {

                        }
                        try {
                            ctable.deleteTable(conn, "cpudtlday", allipstr, "cpudtlday");// CPU
                        } catch (Exception e) {

                        }

                        try {
                            ctable.deleteTable(conn, "disk", allipstr, "disk");// disk
                        } catch (Exception e) {

                        }
                        try {
                            ctable.deleteTable(conn, "diskhour", allipstr, "diskhour");// disk
                        } catch (Exception e) {

                        }
                        try {
                            ctable.deleteTable(conn, "diskday", allipstr, "diskday");// disk
                        } catch (Exception e) {

                        }

                        try {
                            ctable.deleteTable(conn, "diskincre", allipstr, "diskincre");// ��������
                        } catch (Exception e) {

                        }
                        try {
                            ctable.deleteTable(conn, "diskinch", allipstr, "diskinch");// ��������
                        } catch (Exception e) {

                        }
                        try {
                            ctable.deleteTable(conn, "diskincd", allipstr, "diskincd");// ��������
                        } catch (Exception e) {

                        }

                        /*
                         * ctable.createTable("disk",allipstr,"disk");
                         * ctable.createTable("diskhour",allipstr,"diskhour");
                         * ctable.createTable("diskday",allipstr,"diskday");
                         */
                        try {
                            ctable.deleteTable(conn, "ping", allipstr, "ping");
                        } catch (Exception e) {

                        }
                        try {
                            ctable.deleteTable(conn, "pinghour", allipstr, "pinghour");
                        } catch (Exception e) {

                        }
                        try {
                            ctable.deleteTable(conn, "pingday", allipstr, "pingday");
                        } catch (Exception e) {

                        }

                        try {
                            ctable.deleteTable(conn, "utilhdxperc", allipstr, "hdperc");
                        } catch (Exception e) {

                        }
                        try {
                            ctable.deleteTable(conn, "hdxperchour", allipstr, "hdperchour");
                        } catch (Exception e) {

                        }
                        try {
                            ctable.deleteTable(conn, "hdxpercday", allipstr, "hdpercday");
                        } catch (Exception e) {

                        }

                        try {
                            ctable.deleteTable(conn, "utilhdx", allipstr, "hdx");
                        } catch (Exception e) {

                        }
                        try {
                            ctable.deleteTable(conn, "utilhdxhour", allipstr, "hdxhour");
                        } catch (Exception e) {

                        }
                        try {
                            ctable.deleteTable(conn, "utilhdxday", allipstr, "hdxday");
                        } catch (Exception e) {

                        }
                        try {
                            ctable.deleteTable(conn, "software", allipstr, "software");
                        } catch (Exception e) {

                        }

                        // //�¶�
                        // try{
                        // ctable.deleteTable(conn,"temper",allipstr,"temper");
                        // }catch(Exception e){
                        //        					
                        // }
                        // try{
                        // ctable.deleteTable(conn,"temperh",allipstr,"temperh");
                        // }catch(Exception e){
                        //        					
                        // }
                        // try{
                        // ctable.deleteTable(conn,"temperd",allipstr,"temperd");
                        // }catch(Exception e){
                        //        					
                        // }

                        ctable.deleteTable(conn, "allutilhdx", allipstr, "allhdx");
                        ctable.deleteTable(conn, "autilhdxh", allipstr, "ahdxh");
                        ctable.deleteTable(conn, "autilhdxd", allipstr, "ahdxd");

                        ctable.deleteTable(conn, "discardsperc", allipstr, "dcardperc");
                        ctable.deleteTable(conn, "dcarperh", allipstr, "dcarperh");
                        ctable.deleteTable(conn, "dcarperd", allipstr, "dcarperd");

                        ctable.deleteTable(conn, "errorsperc", allipstr, "errperc");
                        ctable.deleteTable(conn, "errperch", allipstr, "errperch");
                        ctable.deleteTable(conn, "errpercd", allipstr, "errpercd");

                        ctable.deleteTable(conn, "packs", allipstr, "packs");
                        ctable.deleteTable(conn, "packshour", allipstr, "packshour");
                        ctable.deleteTable(conn, "packsday", allipstr, "packsday");

                        ctable.deleteTable(conn, "inpacks", allipstr, "inpacks");
                        ctable.deleteTable(conn, "ipacksh", allipstr, "ipacksh");
                        ctable.deleteTable(conn, "ipackd", allipstr, "ipackd");

                        ctable.deleteTable(conn, "outpacks", allipstr, "outpacks");
                        ctable.deleteTable(conn, "opackh", allipstr, "opackh");
                        ctable.deleteTable(conn, "opacksd", allipstr, "opacksd");
                        if (host.getSysOid().equalsIgnoreCase("1.3.6.1.4.1.2.3.1.2.1.1")) {
                            // ɾ����ҳ��
                            try {
                                ctable.deleteTable(conn, "pgused", allipstr, "pgused");
                                ctable.deleteTable(conn, "pgusedhour", allipstr, "pgusedhour");
                                ctable.deleteTable(conn, "pgusedday", allipstr, "pgusedday");
                            } catch (Exception e) {

                            }

                        }

                        // ɾ�����豸�Ĳɼ�ָ��
                        NodeGatherIndicatorsDao gatherdao = new NodeGatherIndicatorsDao();
                        try {
                            gatherdao.deleteByNodeIdAndTypeAndSubtype(host.getId() + "", "host", "");
                        } catch (RuntimeException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } finally {
                            gatherdao.close();
                        }

                        // ɾ��������ָ��ɼ�����Ķ�Ӧ������
                        AlarmIndicatorsNodeDao indicatdao = new AlarmIndicatorsNodeDao();
                        try {
                            // delte��,conn�Ѿ��ر�
                            indicatdao.deleteByNodeId(host.getId() + "", "host");
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            indicatdao.close();
                        }

                        EventListDao eventdao = new EventListDao();
                        try {
                            // ͬʱɾ���¼�������������
                            eventdao.delete(host.getId(), "host");
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            eventdao.close();
                        }
                        // ɾ��diskconfig
                        String[] otherTempData = new String[] { "nms_diskconfig" };
                        String[] ipStrs = new String[] { host.getIpAddress() };
                        ctable.clearTablesData(otherTempData, "ipaddress", ipStrs);
                        // ɾ�������������
                        ProcessGroupConfigurationUtil processGroupConfigurationUtil = new ProcessGroupConfigurationUtil();
                        processGroupConfigurationUtil.deleteProcessGroupAndConfigurationByNodeid(host.getId() + "");

                        // ɾ��SYSLOG�����
                        NetSyslogNodeRuleDao ruledao = new NetSyslogNodeRuleDao();
                        try {
                            ruledao.deleteByNodeID(host.getId() + "");
                        } catch (Exception e) {

                        } finally {
                            ruledao.close();
                        }

                        // ɾ���豸��ǰ���¸澯��Ϣ���е�����
                        NodeAlarmUtil.deleteByDeviceIdAndDeviceType(host);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                    conn.close();
                }

                // 2.����xml
                if (host.getCategory() < 4) {
                    // �����豸
                    ManageXmlDao mdao = new ManageXmlDao();
                    List<ManageXml> list = mdao.loadAll();
                    if (list.size() > 0) {
                        for (int k = 0; k < list.size(); k++) {

                            ManageXml manageXml = list.get(k);
                            XmlOperator xopr = new XmlOperator();
                            String xmlName = manageXml.getXmlName();
                            String nodeid = "net" + host.getId();
                            xopr.setFile(xmlName);
                            xopr.init4updateXml();
                            if (xopr.isNodeExist(nodeid)) {
                                xopr.deleteNodeByID(nodeid);
                            }
                            xopr.writeXml();
                            RelationDao rdao = new RelationDao();
                            Relation vo = (Relation) rdao.findByNodeId(id, xmlName);
                            if (vo != null) {
                                rdao.deleteByNode(id, xmlName);
                            } else {
                                rdao.close();
                            }
                            // ɾ������ͼԪ�������
                            NodeEquipDao nodeEquipDao = new NodeEquipDao();
                            if (nodeEquipDao.findByNode(nodeid) != null) {
                                nodeEquipDao.deleteByNode(nodeid);
                            } else {
                                nodeEquipDao.close();
                            }
                        }
                    }
                    // try {
                    // XmlOperator opr = new XmlOperator();
                    // opr.setFile("network.jsp");
                    // opr.init4updateXml();
                    // opr.deleteNodeByID("net"+host.getId() + "");
                    // // opr.addNode(helper.getHost());
                    // opr.writeXml();
                    // } catch (Exception e) {
                    // e.printStackTrace();
                    // }
                } else if (host.getCategory() == 4) {
                    // ����������
//                    try {
//                        XmlOperator opr = new XmlOperator();
//                        opr.setFile("server.jsp");
//                        opr.init4updateXml();
//                        opr.deleteNodeByID(host.getId() + "");
//                        // opr.addNode(helper.getHost());
//                        opr.writeXml();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
                }

                // ɾ��ָ��ȫ����ֵ���Ӧ������
                NodeMonitorDao nodeMonitorDao = new NodeMonitorDao();
                try {
                    nodeMonitorDao.deleteByID(id);
                } catch (RuntimeException e1) {
                    e1.printStackTrace();
                } finally {
                    nodeMonitorDao.close();
                }

                // nielin add 2010-07-21 for as400 start
                if (host.getOstype() == 15) {
                    // as400
                    CreateTableManager ctable2 = new CreateTableManager();
                    DBManager conn2 = new DBManager();
                    try {
                        ctable2.deleteTable(conn2, "systemasp", allipstr, "systemasp");
                        ctable2.deleteTable(conn2, "dbcapability", allipstr, "dbcapability");
                    } catch (RuntimeException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } finally {
                        conn2.close();
                    }
                }

                // nielin add 2010-07-21 for as400 end

                TimeShareConfigUtil timeShareConfigUtil = new TimeShareConfigUtil();
                try {
                    boolean result = timeShareConfigUtil.deleteTimeShareConfig(id, timeShareConfigUtil.getObjectType("0"));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // ������ɾ��(guangfei edite)
                if (linklist != null && linklist.size() > 0) {
                    for (int l = 0; l < linklist.size(); l++) {
                        link = (Link) linklist.get(l);
                        if (link != null) {
                            LinkDao ldao = new LinkDao();
                            try {
                                ldao.delete(link.getId() + "");
                            } catch (Exception e) {

                            } finally {
                                ldao.close();
                            }
                        }
                    }
                }

                /* snow add for time-garther at 2010-05-17 */
                TimeGratherConfigUtil timeGratherConfigUtil = new TimeGratherConfigUtil();
                try {
                    boolean result2 = timeGratherConfigUtil.deleteTimeGratherConfig(id, timeGratherConfigUtil.getObjectType("0"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                /* snow close */

                // ����ҵ����ͼ
                NodeDependDao nodedependao = new NodeDependDao();
                List list = nodedependao.findByNode("net" + id);
                if (list != null && list.size() > 0) {
                    for (int j = 0; j < list.size(); j++) {
                        NodeDepend vo = (NodeDepend) list.get(j);
                        if (vo != null) {
                            LineDao lineDao = new LineDao();
                            lineDao.deleteByidXml("net" + id, vo.getXmlfile());
                            NodeDependDao nodeDependDao = new NodeDependDao();
                            if (nodeDependDao.isNodeExist("net" + id, vo.getXmlfile())) {
                                nodeDependDao.deleteByIdXml("net" + id, vo.getXmlfile());
                            } else {
                                nodeDependDao.close();
                            }

                            // yangjun
                            User user = (User) session.getAttribute(SessionConstant.CURRENT_USER);
                            ManageXmlDao mXmlDao = new ManageXmlDao();
                            List xmlList = new ArrayList();
                            try {
                                xmlList = mXmlDao.loadByPerAll(user.getBusinessids());
                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                mXmlDao.close();
                            }
                            try {
                                ChartXml chartxml;
                                chartxml = new ChartXml("tree");
                                chartxml.addViewTree(xmlList);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            ManageXmlDao subMapDao = new ManageXmlDao();
                            ManageXml manageXml = (ManageXml) subMapDao.findByXml(vo.getXmlfile());
                            if (manageXml != null) {
                                NodeDependDao nodeDepenDao = new NodeDependDao();
                                try {
                                    List lists = nodeDepenDao.findByXml(vo.getXmlfile());
                                    ChartXml chartxml;
                                    chartxml = new ChartXml("NetworkMonitor", "/" + vo.getXmlfile().replace("jsp", "xml"));
                                    chartxml.addBussinessXML(manageXml.getTopoName(), lists);
                                    ChartXml chartxmlList;
                                    chartxmlList = new ChartXml("NetworkMonitor", "/" + vo.getXmlfile().replace("jsp", "xml").replace("businessmap", "list"));
                                    chartxmlList.addListXML(manageXml.getTopoName(), lists);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                } finally {
                                    nodeDepenDao.close();
                                }
                            }
                        }
                    }
                }

                // �û��������
                User current_user = (User) session.getAttribute(SessionConstant.CURRENT_USER);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Calendar tempCal = Calendar.getInstance();
                Date cc = tempCal.getTime();
                String time = sdf.format(cc);
                UserAuditUtil useraudit = new UserAuditUtil();
                String useraction = "";
                useraction = useraction + "ɾ���豸 IP:" + host.getIpAddress() + " ����:" + host.getAlias() + " ����:" + host.getType();
                try {
                    useraudit.saveUserAudit(current_user, time, useraction);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // �����Զ��ping ɾ������Ϣ

                RemotePingHostDao remotePingHostDao = new RemotePingHostDao();
                try {
                    remotePingHostDao.deleteByNodeId(id);
                } catch (RuntimeException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } finally {
                    remotePingHostDao.close();
                }

                RemotePingNodeDao remotePingNodeDao = new RemotePingNodeDao();
                try {
                    remotePingNodeDao.deleteByNodeId(id);
                } catch (RuntimeException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } finally {
                    remotePingNodeDao.close();
                }

                remotePingNodeDao = new RemotePingNodeDao();
                try {
                    remotePingNodeDao.deleteByChildNodeId(id);
                } catch (RuntimeException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } finally {
                    remotePingNodeDao.close();
                }

            }
            // taskutil = null;

            // ɾ���豸����ʱ�����д洢������
            String[] nmsTempDataTables = { "nms_cpu_data_temp", "nms_device_data_temp", "nms_disk_data_temp", "nms_diskperf_data_temp", "nms_envir_data_temp", "nms_fdb_data_temp", "nms_fibrecapability_data_temp", "nms_fibreconfig_data_temp", "nms_flash_data_temp", "nms_interface_data_temp", "nms_lights_data_temp", "nms_memory_data_temp", "nms_other_data_temp", "nms_ping_data_temp", "nms_process_data_temp", "nms_route_data_temp", "nms_sercice_data_temp", "nms_software_data_temp", "nms_storage_data_temp", "nms_system_data_temp", "nms_user_data_temp", "nms_nodeconfig", "nms_nodecpuconfig", "nms_nodediskconfig", "nms_nodememconfig", "nms_vmwarevid", "nms_emcdiskcon", "nms_emcluncon", "nms_emchard", "nms_emcraid", "nms_emcsystem", "nms_connect" };
            CreateTableManager createTableManager = new CreateTableManager();
            createTableManager.clearNmsTempDatas(nmsTempDataTables, ids);

        }
        HostNodeDao hostNodeDao = new HostNodeDao();
        Hashtable nodehash = new Hashtable();
        try {
            List hostlist = hostNodeDao.loadIsMonitored(1);
            if (hostlist != null && hostlist.size() > 0) {
                for (int i = 0; i < hostlist.size(); i++) {
                    HostNode node = (HostNode) hostlist.get(i);
                    if (nodehash.containsKey(node.getCategory() + "")) {
                        ((List) nodehash.get(node.getCategory() + "")).add(node);
                    } else {
                        List nodelist = new ArrayList();
                        nodelist.add(node);
                        nodehash.put(node.getCategory() + "", nodelist);
                    }
                }
            }
        } catch (Exception e) {

        } finally {
            hostNodeDao.close();
        }
        ShareData.setNodehash(nodehash);

        PortConfigCenter.getInstance().setPortHastable();

        return "/network.do?action=list";
    }

    private String cancelmanage() {
        String[] ids = getParaArrayValue("checkbox");
        if (ids != null && ids.length > 0) {
            // �����޸�
            for (int i = 0; i < ids.length; i++) {
                try {
                    HostNodeDao dao = new HostNodeDao();
                    HostNode host = null;
                    try {
                        host = (HostNode) dao.findByID(ids[i]);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    } finally {
                        dao.close();
                    }
                    host.setManaged(false);
                    dao = new HostNodeDao();
                    try {
                        dao.update(host);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    } finally {
                        dao.close();
                    }
                    PollingEngine.getInstance().deleteNodeByID(Integer.parseInt(ids[i]));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }

        HostNodeDao hostNodeDao = new HostNodeDao();
        Hashtable nodehash = new Hashtable();
        try {
            List hostlist = hostNodeDao.loadIsMonitored(1);
            if (hostlist != null && hostlist.size() > 0) {
                for (int i = 0; i < hostlist.size(); i++) {
                    HostNode node = (HostNode) hostlist.get(i);
                    if (nodehash.containsKey(node.getCategory() + "")) {
                        ((List) nodehash.get(node.getCategory() + "")).add(node);
                    } else {
                        List nodelist = new ArrayList();
                        nodelist.add(node);
                        nodehash.put(node.getCategory() + "", nodelist);
                    }
                }
            }
        } catch (Exception e) {

        } finally {
            hostNodeDao.close();
        }
        ShareData.setNodehash(nodehash);

        return "/network.do?action=monitornodelist";
    }

    private String menucancelmanage() {
        String id = getParaValue("id");
        try {
            HostNodeDao dao = new HostNodeDao();
            HostNode host = null;
            try {
                host = (HostNode) dao.findByID(id);
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                dao.close();
            }
            host.setManaged(false);
            dao = new HostNodeDao();
            try {
                dao.update(host);
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                dao.close();
            }
            PollingEngine.getInstance().deleteNodeByID(Integer.parseInt(id));
            // ɾ���豸��ǰ���¸澯��Ϣ���е�����
            NodeAlarmUtil.deleteByDeviceIdAndDeviceType(host);
            // //ȡ���ɼ�
            // List<String> nodeidList = new ArrayList<String>();
            // nodeidList.add(id);
            // TaskUtil taskutil = new TaskUtil();
            // taskutil.removeTask(nodeidList);
            // taskutil = null;
        } catch (Exception e) {
            e.printStackTrace();
        }

        HostNodeDao hostNodeDao = new HostNodeDao();
        Hashtable nodehash = new Hashtable();
        try {
            List hostlist = hostNodeDao.loadIsMonitored(1);
            if (hostlist != null && hostlist.size() > 0) {
                for (int i = 0; i < hostlist.size(); i++) {
                    HostNode node = (HostNode) hostlist.get(i);
                    if (nodehash.containsKey(node.getCategory() + "")) {
                        ((List) nodehash.get(node.getCategory() + "")).add(node);
                    } else {
                        List nodelist = new ArrayList();
                        nodelist.add(node);
                        nodehash.put(node.getCategory() + "", nodelist);
                    }
                }
            }
        } catch (Exception e) {

        } finally {
            hostNodeDao.close();
        }
        ShareData.setNodehash(nodehash);

        return "/network.do?action=list";
    }

    private String menucancelendpoint() {
        String id = getParaValue("id");
        try {
            HostNodeDao dao = new HostNodeDao();
            HostNode host = null;
            try {
                host = (HostNode) dao.findByID(id);
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                dao.close();
            }
            host.setEndpoint(0);
            dao = new HostNodeDao();
            try {
                dao.update(host);
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                dao.close();
            }
            // PollingEngine.getInstance().deleteNodeByID(Integer.parseInt(id));
            // �����ڴ�
            Host _host = (Host) PollingEngine.getInstance().getNodeByID(host.getId());
            if (_host != null) {
                _host.setEndpoint(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "/network.do?action=list";
    }

    private String detailcancelmanage() {
        // BASE64Encoder base = new BASE64Encoder();
        String id = getParaValue("id");
        try {
            HostNodeDao dao = new HostNodeDao();
            HostNode host = null;
            try {
                host = (HostNode) dao.findByID(id);
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                dao.close();
            }
            host.setManaged(false);
            dao = new HostNodeDao();
            try {
                dao.update(host);
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                dao.close();
            }
            // PollingEngine.getInstance().deleteNodeByID(Integer.parseInt(id));
            // //ȡ���ɼ�����
            // TaskUtil taskutil = new TaskUtil();
            // taskutil.removeTask(id);
            // taskutil = null;
        } catch (Exception e) {
            e.printStackTrace();
        }

        HostNodeDao hostNodeDao = new HostNodeDao();
        Hashtable nodehash = new Hashtable();
        try {
            List hostlist = hostNodeDao.loadIsMonitored(1);
            if (hostlist != null && hostlist.size() > 0) {
                for (int i = 0; i < hostlist.size(); i++) {
                    HostNode node = (HostNode) hostlist.get(i);
                    if (nodehash.containsKey(node.getCategory() + "")) {
                        ((List) nodehash.get(node.getCategory() + "")).add(node);
                    } else {
                        List nodelist = new ArrayList();
                        nodelist.add(node);
                        nodehash.put(node.getCategory() + "", nodelist);
                    }
                }
            }
        } catch (Exception e) {

        } finally {
            hostNodeDao.close();
        }
        ShareData.setNodehash(nodehash);

        return "/network.do?action=list";
    }

    private String menuaddmanage() {
        String id = getParaValue("id");
        try {
            HostNodeDao dao = new HostNodeDao();
            HostNode host = null;
            try {
                host = (HostNode) dao.findByID(id);
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                dao.close();
            }
            host.setManaged(true);
            dao = new HostNodeDao();
            try {
                dao.update(host);
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                dao.close();
            }
            HostLoader hl = new HostLoader();
            try {
                hl.loadOne(host);
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                hl.close();
            }
            // PollingEngine.getInstance().deleteNodeByID(Integer.parseInt(id));
            // PollingEngine.getInstance().addNode(node);
            // ���Ӳɼ�
            // List<String> nodeidList = new ArrayList<String>();
            // nodeidList.add(id);
            // TaskUtil taskutil = new TaskUtil();
            // taskutil.addTask(nodeidList);
            // taskutil = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        HostNodeDao hostNodeDao = new HostNodeDao();
        Hashtable nodehash = new Hashtable();
        try {
            List hostlist = hostNodeDao.loadIsMonitored(1);
            if (hostlist != null && hostlist.size() > 0) {
                for (int i = 0; i < hostlist.size(); i++) {
                    HostNode node = (HostNode) hostlist.get(i);
                    if (nodehash.containsKey(node.getCategory() + "")) {
                        ((List) nodehash.get(node.getCategory() + "")).add(node);
                    } else {
                        List nodelist = new ArrayList();
                        nodelist.add(node);
                        nodehash.put(node.getCategory() + "", nodelist);
                    }
                }
            }
        } catch (Exception e) {

        } finally {
            hostNodeDao.close();
        }
        ShareData.setNodehash(nodehash);

        return "/network.do?action=list";
    }

    private String menuaddendpoint() {
        String id = getParaValue("id");
        try {
            HostNodeDao dao = new HostNodeDao();
            HostNode host = null;
            try {
                host = (HostNode) dao.findByID(id);
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                dao.close();
            }
            host.setEndpoint(1);
            dao = new HostNodeDao();
            try {
                dao.update(host);
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                dao.close();
            }
            // �����ڴ�
            Host _host = (Host) PollingEngine.getInstance().getNodeByID(host.getId());
            if (_host != null) {
                _host.setEndpoint(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        HostNodeDao hostNodeDao = new HostNodeDao();
        Hashtable nodehash = new Hashtable();
        try {
            List hostlist = hostNodeDao.loadIsMonitored(1);
            if (hostlist != null && hostlist.size() > 0) {
                for (int i = 0; i < hostlist.size(); i++) {
                    HostNode node = (HostNode) hostlist.get(i);
                    if (nodehash.containsKey(node.getCategory() + "")) {
                        ((List) nodehash.get(node.getCategory() + "")).add(node);
                    } else {
                        List nodelist = new ArrayList();
                        nodelist.add(node);
                        nodehash.put(node.getCategory() + "", nodelist);
                    }
                }
            }
        } catch (Exception e) {

        } finally {
            hostNodeDao.close();
        }
        ShareData.setNodehash(nodehash);

        return "/network.do?action=list";
    }

    /**
     * @author nielin
     * @since 2009-12-28
     * @return add.jsp
     */
    private String ready_add() {
        List allbuss = null;
        BusinessDao bussdao = new BusinessDao();
        try {
            allbuss = bussdao.loadAll();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bussdao.close();
        }
        /* snow add at 2010-05-18 */
        SupperDao supperdao = new SupperDao();
        List<Supper> allSupper = supperdao.loadAll();
        request.setAttribute("allSupper", allSupper);
        /* snow end */
        // ��ʼ��������ӵ�IP�б�
        PollingEngine.setAddiplist(new ArrayList());
        request.setAttribute("allbuss", allbuss);
        return "/topology/network/add.jsp";
    }

    /**
     * @author nielin modify
     * @since 2009-12-29
     * @return
     */
    private String add() {
        SysLogger.info("&&&&&&& ��ʼ����豸 &&&&&&&&&&&&&&&&&&&&");
        String assetid = getParaValue("assetid");// �豸�ʲ����
        String location = getParaValue("location");// ����λ��
        String ipAddress = getParaValue("ip_address");
        String alias = getParaValue("alias");
        int snmpversion = getParaIntValue("snmpversion");

        String community = getParaValue("community");
        String writecommunity = getParaValue("writecommunity");
        String vmwareusername = getParaValue("uname");
        String vmwarepassword = getParaValue("pw");
        int type = getParaIntValue("type");
        int transfer = getParaIntValue("transfer");
        String subtype = getParaValue("subtype");
        if (type == 14 && subtype != null) {
            location = location + "," + subtype;
            // System.out.println("-------------location---------------"+location);
        }
        int ostype = 0;
            ostype = getParaIntValue("ostype");
        int collecttype = 0;
        try {
            collecttype = getParaIntValue("collecttype");
        } catch (Exception e) {

        }
        String bid = getParaValue("bid");

        String sendmobiles = getParaValue("sendmobiles");
        String sendemail = getParaValue("sendemail");
        String sendphone = getParaValue("sendphone");
        int supperid = getParaIntValue("supper");// snow add 2010-5-18

        if (sendmobiles == null) {
            sendmobiles = "";
        }
        if (sendemail == null) {
            sendemail = "";
        }
        if (sendphone == null) {
            sendphone = "";
        }

        // SNMP V3
        int securityLevel = getParaIntValue("securityLevel");
        String securityName = getParaValue("securityName");
        int v3_ap = getParaIntValue("v3_ap");
        String authPassPhrase = getParaValue("authPassPhrase");
        int v3_privacy = getParaIntValue("v3_privacy");
        String privacyPassPhrase = getParaValue("privacyPassPhrase");
        int manageInt = getParaIntValue("manage");
        boolean managed = false;
        if (manageInt == 1)
            managed = true;
        if (securityName == null)
            securityName = "";
        if (authPassPhrase == null || authPassPhrase.trim().length() == 0)
            authPassPhrase = securityName;
        if (privacyPassPhrase == null || privacyPassPhrase.trim().length() == 0)
            privacyPassPhrase = securityName;

        /**
         * @author nielin modify int addResult =
         *         helper.addHost(ipAddress,alias,community,writecommunity,type,ostype,
         *         collecttype); //����һ̨������
         */

        TopoHelper helper = new TopoHelper(); // �����������ݿ�͸����ڴ�
        int addResult = 0;
        if (supperid >= 0) {
            if (collecttype == 3) {
                if (PollingEngine.getAddiplist() != null && PollingEngine.getAddiplist().size() > 0) {
                    List list = PollingEngine.getAddiplist();
                    for (int i = 0; i < list.size(); i++) {
                        Hashtable hst = (Hashtable) list.get(i);
                        NetworkUtil test = new NetworkUtil();
                        List listip = test.parseAllIp((String) hst.get("startip"), (String) hst.get("endip"));
                        for (int j = 0; j < listip.size(); j++) {
                            String iplist = (String) listip.get(j);
                            addResult = helper.addHost(assetid, location, iplist, iplist, snmpversion, community, writecommunity, transfer, type, ostype, collecttype, bid, sendmobiles, sendemail, sendphone, supperid, managed, securityLevel, securityName, v3_ap, authPassPhrase, v3_privacy, privacyPassPhrase);
                            Host node = (Host) PollingEngine.getInstance().getNodeByIP(iplist);
                            try {
                                if (node.getEndpoint() == 2) {
                                    // REMOTEPING���ӽڵ㣬����
                                    // return;
                                } else {
                                    if (node.getCategory() == 4) {
                                        // ��ʼ���������ɼ�ָ��ͷ�ֵ
                                        try {
                                            AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
                                            alarmIndicatorsUtil.saveAlarmInicatorsThresholdForNode(node.getId() + "", AlarmConstant.TYPE_HOST, getSutType(node.getSysOid()));

                                            // �жϲɼ���ʽ
                                            if (node.getCollecttype() == 1) {// snmp��ʽ�ɼ�
                                                NodeGatherIndicatorsUtil nodeGatherIndicatorsUtil = new NodeGatherIndicatorsUtil();
                                                nodeGatherIndicatorsUtil.addGatherIndicatorsForNode(node.getId() + "", AlarmConstant.TYPE_HOST, getSutType(node.getSysOid()), "1");
                                            } else if (node.getCollecttype() > 1) {// ������ʽ�ɼ�

                                                NodeGatherIndicatorsUtil nodeGatherIndicatorsUtil = new NodeGatherIndicatorsUtil();
                                                nodeGatherIndicatorsUtil.addGatherIndicatorsOtherForNode(node.getId() + "", AlarmConstant.TYPE_HOST, getSutType(node.getSysOid()), "0", node.getCollecttype());

                                            }
                                        } catch (RuntimeException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    PollingEngine.setAddiplist(new ArrayList());
                    addResult = helper.addHost(assetid, location, ipAddress, alias, snmpversion, community, writecommunity, transfer, type, ostype, collecttype, bid, sendmobiles, sendemail, sendphone, supperid, managed, securityLevel, securityName, v3_ap, authPassPhrase, v3_privacy, privacyPassPhrase);
                } else {
                    addResult = helper.addHost(assetid, location, ipAddress, alias, snmpversion, community, writecommunity, transfer, type, ostype, collecttype, bid, sendmobiles, sendemail, sendphone, supperid, managed, securityLevel, securityName, v3_ap, authPassPhrase, v3_privacy, privacyPassPhrase);
                }
            } else {

                addResult = helper.addHost(assetid, location, ipAddress, alias, snmpversion, community, writecommunity, transfer, type, ostype, collecttype, bid, sendmobiles, sendemail, sendphone, supperid, managed, securityLevel, securityName, v3_ap, authPassPhrase, v3_privacy, privacyPassPhrase);
            }
        }
        if (addResult > 0) {
            String[] fs = getParaArrayValue("fcheckbox");
            String faci_str = "";
            if (fs != null && fs.length > 0) {
                for (int i = 0; i < fs.length; i++) {
                    String fa = fs[i];
                    faci_str = faci_str + fa + ",";
                }
            }

            NetSyslogNodeRuleDao netruledao = new NetSyslogNodeRuleDao();
            NetSyslogRuleDao ruledao = new NetSyslogRuleDao();
            try {
                String strNodeId = "";
                try {
                    strNodeId = netruledao.findIdByIpaddress(ipAddress);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String strFacility = "";
                List rulelist = new ArrayList();
                try {
                    rulelist = ruledao.loadAll();
                } catch (Exception e) {

                } finally {
                    ruledao.close();
                }
                // if(rulelist != null && rulelist.size()>0){
                // NetSyslogRule logrule = (NetSyslogRule)rulelist.get(0);
                // strFacility = logrule.getFacility();
                // }
                if (rulelist != null && rulelist.size() > 0 && "".equals(faci_str)) {
                    NetSyslogRule logrule = (NetSyslogRule) rulelist.get(0);
                    strFacility = logrule.getFacility();
                } else {
                    strFacility = faci_str;
                }
                String strSql = "";
                strSql = "insert into nms_netsyslogrule_node(nodeid,facility)values('" + strNodeId + "','" + strFacility + "')";
                try {
                    netruledao.saveOrUpdate(strSql);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // nielin add for time-sharing at 2009-01-04
                TimeShareConfigUtil timeShareConfigUtil = new TimeShareConfigUtil();
                try {
                    boolean resutl = timeShareConfigUtil.saveTimeShareConfigList(request, String.valueOf(addResult), timeShareConfigUtil.getObjectType("0"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // netlog.close();
                /* snow add for time-garther at 2010-05-17 */
                TimeGratherConfigUtil timeGratherConfigUtil = new TimeGratherConfigUtil();
                try {
                    boolean result2 = timeGratherConfigUtil.saveTimeGratherConfigList(request, String.valueOf(addResult), timeGratherConfigUtil.getObjectType("0"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                /* snow close */
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                netruledao.close();
                ruledao.close();
            }
        }
        if (addResult == 0) {
            setErrorCode(ErrorMessage.ADD_HOST_FAILURE);
            return null;
        }
        if (addResult == -1) {
            setErrorCode(ErrorMessage.IP_ADDRESS_EXIST);
            return null;
        }
        if (addResult == -2) {
            setErrorCode(ErrorMessage.PING_FAILURE);
            return null;
        }
        if (addResult == -3) {
            setErrorCode(ErrorMessage.SNMP_FAILURE);
            return null;
        }

        // SysLogger.info("### ��ʼ����XML "+ipAddress+" �豸############");
        // 2.����xml
        if (type == 4) {
            // ����������
//            XmlOperator opr = new XmlOperator();
//            opr.setFile("server.jsp");
//            opr.init4updateXml();
//            opr.addNode(helper.getHost());
//            opr.writeXml();
        } else if (type < 4) {
            // �����豸
            XmlOperator opr = new XmlOperator();
            opr.setFile("network.jsp");
            opr.init4updateXml();
            opr.addNode(helper.getHost());
            opr.writeXml();
        } else {

        }
     
        Host node = (Host) PollingEngine.getInstance().getNodeByID(addResult);
        // �ɼ��豸��Ϣ
        try {

            if (node.getEndpoint() == 2) {
                // REMOTEPING���ӽڵ㣬����
                // return;
            } else {
                // SysLogger.info("### ��ʼ��Ӳɼ�ָ�� "+ipAddress+" �豸############");
                if (node.getCategory() == 4) {
                    // ��ʼ���������ɼ�ָ��ͷ�ֵ
                    try {
                        AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
                        if (node.getCollecttype() == SystemConstant.COLLECTTYPE_PING) {
                            alarmIndicatorsUtil.saveAlarmInicatorsThresholdForNode(node.getId() + "", AlarmConstant.TYPE_HOST, getSutType(node.getSysOid()), "ping");
                        } else {
                            alarmIndicatorsUtil.saveAlarmInicatorsThresholdForNode(node.getId() + "", AlarmConstant.TYPE_HOST, getSutType(node.getSysOid()));
                        }
                        NodeGatherIndicatorsUtil nodeGatherIndicatorsUtil = new NodeGatherIndicatorsUtil();
                        nodeGatherIndicatorsUtil.addGatherIndicatorsOtherForNode(node.getId() + "", AlarmConstant.TYPE_HOST, getSutType(node.getSysOid()), "1", node.getCollecttype());

                    } catch (RuntimeException e) {
                        e.printStackTrace();
                    }

                } else if (node.getCategory() < 4 || node.getCategory() == 7) {
                    // ��ʼ���������ɼ�ָ��ͷ�ֵ
                    // SysLogger.info(node.getSysOid());
                    try {
                        AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
                        if (node.getCollecttype() == SystemConstant.COLLECTTYPE_PING) {
                            alarmIndicatorsUtil.saveAlarmInicatorsThresholdForNode(node.getId() + "", AlarmConstant.TYPE_NET, getSutType(node.getSysOid()), "ping");
                        } else {
                            alarmIndicatorsUtil.saveAlarmInicatorsThresholdForNode(node.getId() + "", AlarmConstant.TYPE_NET, getSutType(node.getSysOid()));
                        }

                        NodeGatherIndicatorsUtil nodeGatherIndicatorsUtil = new NodeGatherIndicatorsUtil();
                        nodeGatherIndicatorsUtil.addGatherIndicatorsOtherForNode(node.getId() + "", AlarmConstant.TYPE_NET, getSutType(node.getSysOid()), "1", node.getCollecttype());

                        PortConfigCenter.getInstance().setPortHastable();
                    } catch (RuntimeException e) {
                        e.printStackTrace();
                    }
                } else if (node.getCategory() == 8) {
                    // ��ʼ������ǽ�ɼ�ָ��ͷ�ֵ
                    // SysLogger.info(node.getSysOid());
                    try {
                        AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
                        if (node.getCollecttype() == SystemConstant.COLLECTTYPE_PING) {
                            alarmIndicatorsUtil.saveAlarmInicatorsThresholdForNode(node.getId() + "", AlarmConstant.TYPE_FIREWALL, getSutType(node.getSysOid()), "ping");
                        } else {
                            alarmIndicatorsUtil.saveAlarmInicatorsThresholdForNode(node.getId() + "", AlarmConstant.TYPE_FIREWALL, getSutType(node.getSysOid()));
                        }

                        NodeGatherIndicatorsUtil nodeGatherIndicatorsUtil = new NodeGatherIndicatorsUtil();
                        nodeGatherIndicatorsUtil.addGatherIndicatorsForNode(node.getId() + "", AlarmConstant.TYPE_FIREWALL, getSutType(node.getSysOid()), "1");
                        PortConfigCenter.getInstance().setPortHastable();
                    } catch (RuntimeException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                } else if (node.getCategory() == 9) {
                    // ��ʼ��ATM�豸�ɼ�ָ��
                    // if(node.getSysOid().startsWith("1.3.6.1.4.1.9.")){
                    // ATM�豸
                    if (node.getCollecttype() == SystemConstant.COLLECTTYPE_PING) {
                        // ֻ��PING�����ͨ��
                        try {
                            AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
                            alarmIndicatorsUtil.saveAlarmInicatorsThresholdForNode(node.getId() + "", AlarmConstant.TYPE_NET, "atm", "ping");
                        } catch (RuntimeException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        try {
                            NodeGatherIndicatorsUtil nodeGatherIndicatorsUtil = new NodeGatherIndicatorsUtil();
                            nodeGatherIndicatorsUtil.addGatherIndicatorsForNode(node.getId() + "", AlarmConstant.TYPE_NET, "atm", "1", "ping");
                        } catch (RuntimeException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                    // }
                }else if (node.getCategory() == 12) {
                   
                    if (node.getCollecttype() == SystemConstant.COLLECTTYPE_SNMP) {
                        // ֻ��PING�����ͨ��
                        try {
                            AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
                            alarmIndicatorsUtil.saveAlarmInicatorsThresholdForNode(node.getId() + "", AlarmConstant.TYPE_NET, "");
                        } catch (RuntimeException e) {
                            e.printStackTrace();
                        }
                        try {
                            NodeGatherIndicatorsUtil nodeGatherIndicatorsUtil = new NodeGatherIndicatorsUtil();
                            nodeGatherIndicatorsUtil.addGatherIndicatorsForNode(node.getId() + "", AlarmConstant.TYPE_NET, "");
                        } catch (RuntimeException e) {
                            e.printStackTrace();
                        }
                    }else {
                    	if (node.getCollecttype() == SystemConstant.COLLECTTYPE_PING) {
                            // ֻ��PING�����ͨ��
                            try {
                                AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
                                alarmIndicatorsUtil.saveAlarmInicatorsThresholdForNode(node.getId() + "", AlarmConstant.TYPE_NET, "","ping");
                            } catch (RuntimeException e) {
                                e.printStackTrace();
                            }
                            try {
                                NodeGatherIndicatorsUtil nodeGatherIndicatorsUtil = new NodeGatherIndicatorsUtil();
                                nodeGatherIndicatorsUtil.addGatherIndicatorsForNode(node.getId() + "", AlarmConstant.TYPE_NET, "", "ping");
                            } catch (RuntimeException e) {
                                e.printStackTrace();
                            }
                        }
					}
                    // }
                } else if (node.getCategory() == 13) {
                    // ��ʼ��CMTS�豸�ɼ�ָ��
                    // if(node.getSysOid().startsWith("1.3.6.1.4.1.116.")){
                    // HDS�豸
                    if (node.getCollecttype() == SystemConstant.COLLECTTYPE_PING) {
                        // ֻ��PING�����ͨ��
                        try {
                            AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
                            alarmIndicatorsUtil.saveAlarmInicatorsThresholdForNode(node.getId() + "", AlarmConstant.TYPE_CMTS, getSutType(node.getSysOid()), "ping");
                        } catch (RuntimeException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        try {
                            NodeGatherIndicatorsUtil nodeGatherIndicatorsUtil = new NodeGatherIndicatorsUtil();
                            nodeGatherIndicatorsUtil.addGatherIndicatorsForNode(node.getId() + "", AlarmConstant.TYPE_CMTS, getSutType(node.getSysOid()), "1", "ping");
                        } catch (RuntimeException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    } else {
                        // �����ɼ�״̬
                        try {
                            AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
                            alarmIndicatorsUtil.saveAlarmInicatorsThresholdForNode(node.getId() + "", AlarmConstant.TYPE_CMTS, getSutType(node.getSysOid()));
                            NodeGatherIndicatorsUtil nodeGatherIndicatorsUtil = new NodeGatherIndicatorsUtil();
                            nodeGatherIndicatorsUtil.addGatherIndicatorsForNode(node.getId() + "", AlarmConstant.TYPE_CMTS, getSutType(node.getSysOid()), "1");
                            PortConfigCenter.getInstance().setPortHastable();
                        } catch (RuntimeException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                    // }
                } else if (node.getCategory() == 14) {
                    // ��ʼ���洢�豸�ɼ�ָ��
                    // if(node.getSysOid().startsWith("1.3.6.1.4.1.116.")){
                    // HDS�豸
                    // if(node.getCollecttype() ==
                    // SystemConstant.COLLECTTYPE_PING){
                    // System.out.println(node.getCollecttype()+"----ֻ��PING�����ͨ��-----"+SystemConstant.COLLECTTYPE_PING);
                    // //ֻ��PING�����ͨ��
                    // try {
                    // AlarmIndicatorsUtil alarmIndicatorsUtil = new
                    // AlarmIndicatorsUtil();
                    // alarmIndicatorsUtil.saveAlarmInicatorsThresholdForNode(node.getId()+"",
                    // AlarmConstant.TYPE_STORAGE,
                    // getSutType(node.getSysOid()),"ping");
                    // } catch (RuntimeException e) {
                    // // TODO Auto-generated catch block
                    // e.printStackTrace();
                    // }
                    // try {
                    // NodeGatherIndicatorsUtil nodeGatherIndicatorsUtil = new
                    // NodeGatherIndicatorsUtil();
                    // nodeGatherIndicatorsUtil.addGatherIndicatorsForNode(node.getId()+"",
                    // AlarmConstant.TYPE_STORAGE,
                    // getSutType(node.getSysOid()),"1","ping");
                    // } catch (RuntimeException e) {
                    // // TODO Auto-generated catch block
                    // e.printStackTrace();
                    // }
                    // }else{
                    // �����ɼ�״̬
                    // System.out.println(node.getCollecttype()+"----�����ɼ�״̬-----"+SystemConstant.COLLECTTYPE_PING);
                    try {
                        AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
                        if (node.getCollecttype() == SystemConstant.COLLECTTYPE_PING) {
                            alarmIndicatorsUtil.saveAlarmInicatorsThresholdForNode(node.getId() + "", AlarmConstant.TYPE_STORAGE, getSutType(node.getSysOid()), "ping");
                        } else {
                            alarmIndicatorsUtil.saveAlarmInicatorsThresholdForNode(node.getId() + "", AlarmConstant.TYPE_STORAGE, getSutType(node.getSysOid()));
                        }

                        NodeGatherIndicatorsUtil nodeGatherIndicatorsUtil = new NodeGatherIndicatorsUtil();
                        nodeGatherIndicatorsUtil.addGatherIndicatorsForNode(node.getId() + "", AlarmConstant.TYPE_STORAGE, getSutType(node.getSysOid()), "1");
                        PortConfigCenter.getInstance().setPortHastable();

                        Connect vo_c = new Connect();
                        ConnectDao cdao = new ConnectDao();
                        vo_c.setNodeid(Long.parseLong(node.getId() + ""));
                        vo_c.setUsername(vmwareusername);
                        vo_c.setPwd(EncryptUtil.encode(vmwarepassword));
                        vo_c.setType("storage");
                        vo_c.setSubtype(subtype);
                        vo_c.setIpaddress(ipAddress);
                        cdao.save(vo_c);

                    } catch (RuntimeException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    // }
                    // }
                } else if (node.getCategory() == 15) {
                    // ��ʼ��VMWare�豸�ɼ�ָ��
                    // if(node.getSysOid().startsWith("1.3.6.1.4.1.116.")){
                    // vmware�豸
                    if (node.getCollecttype() == SystemConstant.COLLECTTYPE_PING) {
                        // ֻ��PING�����ͨ��
                        try {
                            AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
                            alarmIndicatorsUtil.saveAlarmInicatorsThresholdForNode(node.getId() + "", AlarmConstant.TYPE_VIRTUAL, getSutType(node.getSysOid()), "ping");
                        } catch (RuntimeException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        try {
                            NodeGatherIndicatorsUtil nodeGatherIndicatorsUtil = new NodeGatherIndicatorsUtil();
                            nodeGatherIndicatorsUtil.addGatherIndicatorsForNode(node.getId() + "", AlarmConstant.TYPE_VIRTUAL, getSutType(node.getSysOid()), "1", "ping");
                        } catch (RuntimeException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                    } else {
                        // �����ɼ�״̬
                        try {
                            NodeGatherIndicatorsUtil nodeGatherIndicatorsUtil = new NodeGatherIndicatorsUtil();
                            nodeGatherIndicatorsUtil.addGatherIndicatorsForNode(node.getId() + "", AlarmConstant.TYPE_VIRTUAL, getSutType(node.getSysOid()), "1");
                            PortConfigCenter.getInstance().setPortHastable();
                            // add vmware connect username/password to db
                            VMWareConnectConfigDao vmwaredao = new VMWareConnectConfigDao();
                            try {
                                VMWareConnectConfig vmware = new VMWareConnectConfig();
                                vmware.setNodeid(Long.parseLong(node.getId() + ""));
                                vmware.setUsername(vmwareusername);
                                vmware.setPwd(EncryptUtil.encode(vmwarepassword));
                                vmware.setHosturl("");
                                vmware.setBak("");
                                vmwaredao.save(vmware);

                                WMWareUtil vmwareutil = new WMWareUtil();
                                HashMap vmids = new HashMap();
                                try {
                                    SysLogger.info(node.getIpAddress() + "   username:" + vmwareusername + "    password:" + vmwarepassword);
                                    // vmids =
                                    // (HashMap)vmwareutil.SyncVM(node.getIpAddress(),
                                    // vmwareusername, vmware.getPwd());
                                    if (vmids != null && vmids.size() > 0) {
                                        Iterator iterator = vmids.keySet().iterator();
                                        List vmwarevids = new ArrayList();
                                        while (iterator.hasNext()) {
                                            String vmid = (String) iterator.next();
                                            String guestname = (String) vmids.get(vmid);
                                            VMWareVid vmwarevid = new VMWareVid();
                                            vmwarevid.setNodeid(Long.parseLong(node.getId() + ""));
                                            vmwarevid.setVid(vmid);
                                            vmwarevid.setGuestname(guestname);
                                            vmwarevid.setBak("");
                                            vmwarevids.add(vmwarevid);
                                            SysLogger.info(vmid + " ##############=======" + guestname);
                                        }
                                        VMWareVidDao vmwareviddao = new VMWareVidDao();
                                        try {
                                            vmwareviddao.save(vmwarevids);
                                        } catch (Exception e) {
                                        } finally {
                                            vmwareviddao.close();
                                        }
                                    }

                                    try {
                                        String url = "https://" + node.getIpAddress() + "/sdk";
                                        HashMap<String, Object> summaryresultMap = (HashMap<String, Object>) VIMMgr.syncVIMObjs(url, vmwareusername, vmwarepassword);
                                        ArrayList<HashMap<String, Object>> dslist = (ArrayList<HashMap<String, Object>>) summaryresultMap.get("Datastore");
                                        ArrayList<HashMap<String, Object>> wulist = (ArrayList<HashMap<String, Object>>) summaryresultMap.get("HostSystem");
                                        ArrayList<HashMap<String, Object>> rplist = (ArrayList<HashMap<String, Object>>) summaryresultMap.get("ResourcePool");
                                        ArrayList<HashMap<String, Object>> vmlist = (ArrayList<HashMap<String, Object>>) summaryresultMap.get("VirtualMachine");
                                        ArrayList<HashMap<String, Object>> dclist = (ArrayList<HashMap<String, Object>>) summaryresultMap.get("Datacenter");
                                        ArrayList<HashMap<String, Object>> crlist = (ArrayList<HashMap<String, Object>>) summaryresultMap.get("ComputeResource");// Ⱥ��
                                        PerformancePanelAjaxManager per = new PerformancePanelAjaxManager();
                                        per.savePhysical(wulist, node.getId() + "", node.getIpAddress());
                                        per.saveVmware(vmlist, node.getId() + "", node.getIpAddress());
                                        per.saveDatastore(dslist, node.getId() + "", node.getIpAddress());
                                        per.saveResourcepool(rplist, node.getId() + "", node.getIpAddress());
                                        per.saveDatacenter(dclist, node.getId() + "", node.getIpAddress());
                                        per.saveYun(crlist, node.getId() + "", node.getIpAddress());
                                        // ��ʼ���澯
                                        if (wulist != null && wulist.size() > 0) {
                                            String vid = "";
                                            for (int i = 0; i < wulist.size(); i++) {
                                                vid = wulist.get(i).get("vid").toString();
                                                AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
                                                alarmIndicatorsUtil.VMsaveAlarmInicatorsThresholdForNode(node.getId() + "", AlarmConstant.TYPE_VIRTUAL, getSutType(node.getSysOid()), "physical", vid);
                                            }
                                        }
                                        if (vmlist != null && vmlist.size() > 0) {
                                            String vid = "";
                                            for (int i = 0; i < vmlist.size(); i++) {
                                                vid = vmlist.get(i).get("vid").toString();
                                                AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
                                                alarmIndicatorsUtil.VMsaveAlarmInicatorsThresholdForNode(node.getId() + "", AlarmConstant.TYPE_VIRTUAL, getSutType(node.getSysOid()), "vmware", vid);
                                            }
                                        }
                                        if (dslist != null && dslist.size() > 0) {
                                            String vid = "";
                                            for (int i = 0; i < dslist.size(); i++) {
                                                vid = dslist.get(i).get("vid").toString();
                                                AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
                                                alarmIndicatorsUtil.VMsaveAlarmInicatorsThresholdForNode(node.getId() + "", AlarmConstant.TYPE_VIRTUAL, getSutType(node.getSysOid()), "datastore", vid);
                                            }
                                        }
                                        if (rplist != null && rplist.size() > 0) {
                                            String vid = "";
                                            for (int i = 0; i < rplist.size(); i++) {
                                                vid = rplist.get(i).get("vid").toString();
                                                AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
                                                alarmIndicatorsUtil.VMsaveAlarmInicatorsThresholdForNode(node.getId() + "", AlarmConstant.TYPE_VIRTUAL, getSutType(node.getSysOid()), "resourcepool", vid);
                                            }
                                        }
                                        if (crlist != null && crlist.size() > 0) {
                                            String vid = "";
                                            for (int i = 0; i < crlist.size(); i++) {
                                                vid = crlist.get(i).get("vid").toString();
                                                AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
                                                alarmIndicatorsUtil.VMsaveAlarmInicatorsThresholdForNode(node.getId() + "", AlarmConstant.TYPE_VIRTUAL, getSutType(node.getSysOid()), "yun", vid);
                                            }
                                        }
                                    } catch (Exception e) {
                                        SysLogger.info("================�ɼ�����ʧ��" + node.getIpAddress());
                                        e.printStackTrace();
                                    }
                                    SysLogger.info("================�����ɼ�VMWARE" + node.getIpAddress());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                vmwaredao.close();
                            }
                            // HashMap<String, Object> VIMmap = new
                            // HashMap<String,Object>();
                            // VIMmap = (HashMap<String,
                            // Object>)VIMMgr.syncVIMObjs("https://"+node.getIpAddress()+"/sdk",
                            // vmwareusername, vmwarepassword);//��ȡvmware���ص���������
                            //		   						
                            // ArrayList<HashMap<String,Object>> VMlist = new
                            // ArrayList<HashMap<String,Object>>();
                            // VMlist =
                            // (ArrayList<HashMap<String,Object>>)VIMmap.get(VIMConstants.SYNC_VM);//����������
                            //		   						
                            //		   					
                            // HashMap<String, Object> VMmap = new
                            // HashMap<String, Object>();
                            // if(VMlist.size()==0){
                            // System.out.println("û�з��������");
                            // }else{
                            // for(int i=0;i<VMlist.size();i++){
                            // String vid =
                            // (String)VMlist.get(i).get(VIMConstants.SYNC_COMMON_VID);
                            // String name =
                            // (String)VMlist.get(i).get(VIMConstants.SYNC_COMMON_NAME);
                            // VMmap.put(vid,name);
                            // }
                            // }
                        } catch (RuntimeException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                    // }
                } else if (node.getCategory() == 16) {
                    // �����ɼ�״̬
                    try {
                        AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
                        alarmIndicatorsUtil.saveAlarmInicatorsThresholdForNode(node.getId() + "", AlarmConstant.TYPE_AIRCONDITION, getSutType(node.getSysOid()));
                        NodeGatherIndicatorsUtil nodeGatherIndicatorsUtil = new NodeGatherIndicatorsUtil();
                        nodeGatherIndicatorsUtil.addGatherIndicatorsForNode(node.getId() + "", AlarmConstant.TYPE_AIRCONDITION, getSutType(node.getSysOid()), "1");
                        PortConfigCenter.getInstance().setPortHastable();
                    } catch (RuntimeException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                } else if (node.getCategory() == 17) {
                    // �����ɼ�״̬
                    try {
                        AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
                        alarmIndicatorsUtil.saveAlarmInicatorsThresholdForNode(node.getId() + "", AlarmConstant.TYPE_UPS, getSutType(node.getSysOid()));
                        NodeGatherIndicatorsUtil nodeGatherIndicatorsUtil = new NodeGatherIndicatorsUtil();
                        nodeGatherIndicatorsUtil.addGatherIndicatorsForNode(node.getId() + "", AlarmConstant.TYPE_UPS, getSutType(node.getSysOid()), "1");
                        PortConfigCenter.getInstance().setPortHastable();
                    } catch (RuntimeException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                // SysLogger.info("### �������ָ�� "+ipAddress+" �豸############");

                // ��ֻ��PING TELNET SSH��ʽ��������,���������ݲ��ɼ�,����
                if (node.getCollecttype() == SystemConstant.COLLECTTYPE_PING || node.getCollecttype() == SystemConstant.COLLECTTYPE_TELNETCONNECT || node.getCollecttype() == SystemConstant.COLLECTTYPE_SSHCONNECT) {
                    // SysLogger.info("ֻPING TELNET SSH��ʽ��������,�������ݲ��ɼ�,����");
                    if (node.getCategory() < 4 || node.getCategory() == 7) {
                        PollDataUtil polldata = new PollDataUtil();
                        polldata.collectNetData(node.getId() + "");
                    } else if (node.getCategory() == 4) {
                        collectHostData(node);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        HostNodeDao hostNodeDao = new HostNodeDao();
        Hashtable nodehash = new Hashtable();
        try {
            List hostlist = hostNodeDao.loadIsMonitored(1);
            if (hostlist != null && hostlist.size() > 0) {
                for (int i = 0; i < hostlist.size(); i++) {
                    HostNode _node = (HostNode) hostlist.get(i);
                    if (nodehash.containsKey(_node.getCategory() + "")) {
                        ((List) nodehash.get(_node.getCategory() + "")).add(_node);
                    } else {
                        List nodelist = new ArrayList();
                        nodelist.add(_node);
                        nodehash.put(_node.getCategory() + "", nodelist);
                    }
                }
            }
        } catch (Exception e) {

        } finally {
            hostNodeDao.close();
        }
        ShareData.setNodehash(nodehash);

        return "/network.do?action=list";
    }

    /**
     * @author hukelei modify
     * @���������̨�豸
     * @since 2012-1-4
     * @return
     */
    private String addBatchHosts() {
        SysLogger.info("&&&&&&& ��ʼ��������豸 &&&&&&&&&&&&&&&&&&&&");
        List nodelist = new ArrayList();
        // ��Ҫ���EXCEL�ļ������Ĳ��ֻ�ȡ�����豸���б�
        String fileName = getParaValue("fileName");
        String saveDirPath = ResourceCenter.getInstance().getSysPath() + "WEB-INF/macConfig/";
        Fileupload fileupload = new Fileupload(saveDirPath);
        fileupload.doupload(request, 10000000);
        List formFieldList = fileupload.getFormFieldList();

        if (null == formFieldList || formFieldList.size() == 0) {
            request.setAttribute("success", false);
        } else {
            for (int i = 0; i < formFieldList.size(); i++) {
                List formField = (List) formFieldList.get(i);
                String formFieldType = (String) formField.get(0);
                String formFieldName = (String) formField.get(1);
                String formFieldValue = (String) formField.get(2);
                // SysLogger.info(formFieldType+"==="+formFieldName+"==="+formFieldValue);
                if ("file".equals(formFieldType)) {
                    if ("fileName".equals(formFieldName)) {
                        fileName = formFieldValue;
                    }
                }
            }
        }
        // SysLogger.info("fileName========="+fileName);
        try {
            nodelist = readXls(fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (nodelist != null && nodelist.size() > 0) {
            for (int k = 0; k < nodelist.size(); k++) {
                try {
                    HostNode hostnode = (HostNode) nodelist.get(k);
                    String assetid = "";// �豸�ʲ����
                    String location = "";// ����λ��
                    String ipAddress = hostnode.getIpAddress();
                    String alias = hostnode.getAlias();
                    int snmpversion = hostnode.getSnmpversion();
                    String community = hostnode.getCommunity();
                    String writecommunity = hostnode.getWriteCommunity();
                    int type = hostnode.getCategory();
                    int transfer = 0;

                    int ostype = 0;

                    String bid = "";

                    String sendmobiles = "";
                    String sendemail = "";
                    String sendphone = "";
                    int supperid = -1;
                    int collecttype = 0;
                    try {
                        ostype = hostnode.getOstype();
                        collecttype = hostnode.getCollecttype();
                        bid = hostnode.getBid();
                    } catch (Exception e) {

                    }
                    TopoHelper helper = new TopoHelper(); // �����������ݿ�͸����ڴ�
                    int addResult = 0;
                    addResult = helper.addHost(assetid, location, ipAddress, alias, snmpversion, community, writecommunity, transfer, type, ostype, collecttype, bid, sendmobiles, sendemail, sendphone, hostnode.isManaged()); // ����һ̨������

                    if (addResult > 0) {
                        NetSyslogNodeRuleDao netruledao = new NetSyslogNodeRuleDao();
                        NetSyslogRuleDao ruledao = new NetSyslogRuleDao();
                        try {
                            String strFacility = "";
                            List rulelist = new ArrayList();
                            try {
                                rulelist = ruledao.loadAll();
                            } catch (Exception e) {

                            } finally {
                                ruledao.close();
                            }
                            if (rulelist != null && rulelist.size() > 0 && "".equals(hostnode.getSysLocation())) {
                                NetSyslogRule logrule = (NetSyslogRule) rulelist.get(0);
                                strFacility = logrule.getFacility();
                            } else {
                                strFacility = hostnode.getSysLocation();
                            }
                            String strSql = "";
                            strSql = "insert into nms_netsyslogrule_node(id,nodeid,facility)values(0,'" + hostnode.getId() + "','" + strFacility + "')";
                            try {
                                netruledao.saveOrUpdate(strSql);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            // nielin add for time-sharing at 2009-01-04
                            TimeShareConfigUtil timeShareConfigUtil = new TimeShareConfigUtil();
                            try {
                                boolean resutl = timeShareConfigUtil.saveTimeShareConfigList(request, String.valueOf(addResult), timeShareConfigUtil.getObjectType("0"));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            // netlog.close();
                            /* snow add for time-garther at 2010-05-17 */
                            TimeGratherConfigUtil timeGratherConfigUtil = new TimeGratherConfigUtil();
                            try {
                                boolean result2 = timeGratherConfigUtil.saveTimeGratherConfigList(request, String.valueOf(addResult), timeGratherConfigUtil.getObjectType("0"));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            /* snow close */
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            netruledao.close();
                            ruledao.close();
                        }
                    }
                    SysLogger.info("---------------------------------------------");
                    SysLogger.info("-----" + ipAddress + " ��ӽ��:  " + addResult + "--------------");
                    SysLogger.info("---------------------------------------------");

                    if (addResult == 0) {
                        setErrorCode(ErrorMessage.ADD_HOST_FAILURE);
                        continue;
                        // return null;
                    }
                    if (addResult == -1) {
                        setErrorCode(ErrorMessage.IP_ADDRESS_EXIST);
                        SysLogger.info("---------------------------------------------");
                        SysLogger.info("-----" + ipAddress + " �Ѿ�����,����--------------");
                        SysLogger.info("---------------------------------------------");
                        continue;
                        // return null;
                    }
                    if (addResult == -2) {
                        setErrorCode(ErrorMessage.PING_FAILURE);
                        continue;
                        // return null;
                    }
                    if (addResult == -3) {
                        setErrorCode(ErrorMessage.SNMP_FAILURE);
                        continue;
                        // return null;
                    }
                    // SysLogger.info("### ��ʼ����XML "+ipAddress+"
                    // �豸############");
                    // //2.����xml
                    // if (type == 4){
                    // //����������
                    // XmlOperator opr = new XmlOperator();
                    // opr.setFile("server.jsp");
                    // opr.init4updateXml();
                    // opr.addNode(helper.getHost());
                    // opr.writeXml();
                    // } else if(type<4){
                    // //�����豸
                    // XmlOperator opr = new XmlOperator();
                    // opr.setFile("network.jsp");
                    // opr.init4updateXml();
                    // opr.addNode(helper.getHost());
                    // opr.writeXml();
                    // } else {
                    //	    		    	
                    // }
                    // SysLogger.info("### �������� "+ipAddress+" �豸############");

                    Host node = (Host) PollingEngine.getInstance().getNodeByIP(ipAddress);
                    // �ɼ��豸��Ϣ
                    try {
                        // SysLogger.info("endpoint:
                        // "+node.getEndpoint()+"====collecttype:
                        // "+node.getCollecttype()+"====category:
                        // "+node.getCategory());
                        try {
                            // pingData(node);
                            // PingSnmp pingsnmp = new PingSnmp();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        if (node.getEndpoint() == 2) {
                            // REMOTEPING���ӽڵ㣬����
                            // return;
                        } else {
                            // SysLogger.info("### ��ʼ��Ӳɼ�ָ�� "+ipAddress+"
                            // �豸############");
                            if (node.getCategory() == 4) {
                                // ��ʼ���������ɼ�ָ��ͷ�ֵ
                                // SysLogger.info(node.getSysOid());
                                SysLogger.info("---------------------------------------------");
                                SysLogger.info("-----" + ipAddress + " ��Ӳɼ�ָ��   --------------");
                                SysLogger.info("---------------------------------------------");
                                // ------
                                try {
                                    AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
                                    alarmIndicatorsUtil.saveAlarmInicatorsThresholdForNode(node.getId() + "", AlarmConstant.TYPE_HOST, getSutType(node.getSysOid()));

                                    // �жϲɼ���ʽ
                                    if (node.getCollecttype() == 1) {
                                        // snmp��ʽ�ɼ�
                                        NodeGatherIndicatorsUtil nodeGatherIndicatorsUtil = new NodeGatherIndicatorsUtil();
                                        nodeGatherIndicatorsUtil.addGatherIndicatorsForNode(node.getId() + "", AlarmConstant.TYPE_HOST, getSutType(node.getSysOid()), "1");
                                    } else if (node.getCollecttype() == 3 || node.getCollecttype() == 8 || node.getCollecttype() == 9) {
                                        // ֻ������ͨ�ʼ��
                                        SysLogger.info("---------------------------------------------");
                                        SysLogger.info("-----" + ipAddress + " ��Ӳɼ�ָ��  �������״̬ -----oid:" + node.getSysOid());
                                        SysLogger.info("---------------------------------------------");
                                        NodeGatherIndicatorsUtil nodeGatherIndicatorsUtil = new NodeGatherIndicatorsUtil();
                                        nodeGatherIndicatorsUtil.addGatherIndicatorsOtherForNodePing(node.getId() + "", AlarmConstant.TYPE_HOST, getSutType(node.getSysOid()), "1", "ping");
                                    } else {
                                        // ������ʽ�ɼ�
                                        NodeGatherIndicatorsUtil nodeGatherIndicatorsUtil = new NodeGatherIndicatorsUtil();
                                        nodeGatherIndicatorsUtil.addGatherIndicatorsOtherForNode(node.getId() + "", AlarmConstant.TYPE_HOST, getSutType(node.getSysOid()), "0", node.getCollecttype());

                                    }
                                } catch (RuntimeException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            } else if (node.getCategory() < 4 || node.getCategory() == 7 || node.getCategory() == 8) {
                                // ��ʼ���������ɼ�ָ��ͷ�ֵ
                                // SysLogger.info(node.getSysOid());
                                SysLogger.info("---------------------------------------------");
                                SysLogger.info("-----" + ipAddress + " ��Ӳɼ�ָ��   --------------");
                                SysLogger.info("---------------------------------------------");
                                try {
                                    AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
                                    alarmIndicatorsUtil.saveAlarmInicatorsThresholdForNode(node.getId() + "", AlarmConstant.TYPE_NET, getSutType(node.getSysOid()));

                                    // �жϲɼ���ʽ
                                    if (node.getCollecttype() == 1) {
                                        // snmp��ʽ�ɼ�
                                        NodeGatherIndicatorsUtil nodeGatherIndicatorsUtil = new NodeGatherIndicatorsUtil();
                                        nodeGatherIndicatorsUtil.addGatherIndicatorsForNode(node.getId() + "", AlarmConstant.TYPE_NET, getSutType(node.getSysOid()), "1");
                                    } else if (node.getCollecttype() == 3 || node.getCollecttype() == 8 || node.getCollecttype() == 9) {
                                        // ֻ������ͨ�ʼ��
                                        SysLogger.info("---------------------------------------------");
                                        SysLogger.info("-----" + ipAddress + " ��Ӳɼ�ָ��  �������״̬ -----oid:" + node.getSysOid());
                                        SysLogger.info("---------------------------------------------");
                                        NodeGatherIndicatorsUtil nodeGatherIndicatorsUtil = new NodeGatherIndicatorsUtil();
                                        nodeGatherIndicatorsUtil.addGatherIndicatorsOtherForNodePing(node.getId() + "", AlarmConstant.TYPE_NET, getSutType(node.getSysOid()), "1", "ping");
                                    } else {
                                        // ������ʽ�ɼ�

                                        NodeGatherIndicatorsUtil nodeGatherIndicatorsUtil = new NodeGatherIndicatorsUtil();
                                        nodeGatherIndicatorsUtil.addGatherIndicatorsOtherForNode(node.getId() + "", AlarmConstant.TYPE_NET, getSutType(node.getSysOid()), "0", node.getCollecttype());
                                    }
                                } catch (RuntimeException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            } else if (node.getCategory() == 9) {
                                // ��ʼ��ATM�豸�ɼ�ָ��
                                // if(node.getSysOid().startsWith("1.3.6.1.4.1.9.")){
                                // ATM�豸
                                if (node.getCollecttype() == SystemConstant.COLLECTTYPE_PING) {
                                    // ֻ��PING�����ͨ��
                                    try {
                                        AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
                                        alarmIndicatorsUtil.saveAlarmInicatorsThresholdForNode(node.getId() + "", AlarmConstant.TYPE_NET, "atm", "ping");
                                    } catch (RuntimeException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }
                                    try {
                                        NodeGatherIndicatorsUtil nodeGatherIndicatorsUtil = new NodeGatherIndicatorsUtil();
                                        nodeGatherIndicatorsUtil.addGatherIndicatorsForNode(node.getId() + "", AlarmConstant.TYPE_NET, "atm", "1", "ping");
                                    } catch (RuntimeException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }
                                }
                                // }
                            }
                            // SysLogger.info("### �������ָ�� "+ipAddress+"
                            // �豸############");

                            // ��ֻ��PING TELNET SSH��ʽ��������,���������ݲ��ɼ�,����
                            if (node.getCollecttype() == SystemConstant.COLLECTTYPE_PING || node.getCollecttype() == SystemConstant.COLLECTTYPE_TELNETCONNECT || node.getCollecttype() == SystemConstant.COLLECTTYPE_SSHCONNECT) {
                                // SysLogger.info("ֻPING TELNET
                                // SSH��ʽ��������,�������ݲ��ɼ�,����");
                                // if(node.getCategory() < 4 ||
                                // node.getCategory() == 7){
                                // PollDataUtil polldata = new PollDataUtil();
                                // polldata.collectNetData(node.getId()+"");
                                // }else if(node.getCategory() == 4){
                                // collectHostData(node);
                                // }
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }

        // 2.����xml
        try {
            UpdateXmlTask xmltask = new UpdateXmlTask();
            xmltask.run();
            // TopoHelper helper = new TopoHelper(); //�����������ݿ�͸����ڴ�
            // //����������
            // XmlOperator opr = new XmlOperator();
            // opr.setFile("server.jsp");
            // opr.init4updateXml();
            // opr.addNode(helper.getHost());
            // opr.writeXml();
            //	        
            // //�����豸
            // opr = new XmlOperator();
            // opr.setFile("network.jsp");
            // opr.init4updateXml();
            // opr.addNode(helper.getHost());
            // opr.writeXml();

        } catch (Exception e) {
            e.printStackTrace();
        }

        HostNodeDao hostNodeDao = new HostNodeDao();
        Hashtable nodehash = new Hashtable();
        try {
            List hostlist = hostNodeDao.loadIsMonitored(1);
            if (hostlist != null && hostlist.size() > 0) {
                for (int i = 0; i < hostlist.size(); i++) {
                    HostNode node = (HostNode) hostlist.get(i);
                    if (nodehash.containsKey(node.getCategory() + "")) {
                        ((List) nodehash.get(node.getCategory() + "")).add(node);
                    } else {
                        List _nodelist = new ArrayList();
                        _nodelist.add(node);
                        nodehash.put(node.getCategory() + "", _nodelist);
                    }
                }
            }
        } catch (Exception e) {

        } finally {
            hostNodeDao.close();
        }
        ShareData.setNodehash(nodehash);

        return "/network.do?action=list";
    }

    private String find() {
        String key = getParaValue("key");
        String value = getParaValue("value");
        HostNodeDao dao = new HostNodeDao();
        request.setAttribute("list", dao.findByCondition(key, value));

        return "/topology/network/find.jsp";
    }

    private String queryByCondition() {
        String key = getParaValue("key");
        String value = getParaValue("value");
        User current_user = (User) session.getAttribute(SessionConstant.CURRENT_USER);

        StringBuffer s = new StringBuffer();
        int _flag = 0;
        if (current_user.getBusinessids() != null) {
            if (current_user.getBusinessids() != "-1") {
                String[] bids = current_user.getBusinessids().split(",");
                if (bids.length > 0) {
                    for (int i = 0; i < bids.length; i++) {
                        if (bids[i].trim().length() > 0) {
                            if (_flag == 0) {
                                s.append(" and ( bid like '%," + bids[i].trim() + ",%' ");
                                _flag = 1;
                            } else {
                                // flag = 1;
                                s.append(" or bid like '%," + bids[i].trim() + ",%' ");
                            }
                        }
                    }
                    s.append(") and " + key + " like '%" + value + "%'");

                }

            }
        }
        request.setAttribute("actionlist", "list");
        setTarget("/topology/network/list.jsp");
        HostNodeDao dao = new HostNodeDao();
        if (current_user.getRole() == 0) {

            return list(dao, "where 1=1 and " + key + " like '%" + value + "%'");
        } else {
            return list(dao, "where 1=1 " + s);
        }
    }

    private String save() {
        String xmlString = request.getParameter("hidXml");
        String vlanString = request.getParameter("vlan");
        xmlString = xmlString.replace("<?xml version=\"1.0\"?>", "<?xml version=\"1.0\" encoding=\"GB2312\"?>");
        XmlOperator xmlOpr = new XmlOperator();
        if (vlanString != null && vlanString.equals("1")) {
            xmlOpr.setFile("networkvlan.jsp");
        } else
            xmlOpr.setFile("network.jsp");
        xmlOpr.saveImage(xmlString);
        request.setAttribute("fresh", "fresh");
        
        return "/topology/network/save.jsp";
    }

    private String savevlan() {
        String xmlString = request.getParameter("hidXml");
        xmlString = xmlString.replace("<?xml version=\"1.0\"?>", "<?xml version=\"1.0\" encoding=\"GB2312\"?>");
        XmlOperator xmlOpr = new XmlOperator();
        xmlOpr.setFile("networkvlan.jsp");
        xmlOpr.saveImage(xmlString);

        return "/topology/network/save.jsp";
    }

    public String execute(String action) {
        if (action.equals("list"))
            return list();
        if (action.equals("monitornodelist"))
            return monitornodelist();
        if (action.equals("monitornetlist"))
            return monitornetlist();
        if (action.equals("endpointnodelist"))
            return endpointnodelist();
        if (action.equals("monitorhostlist"))
            return monitorhostlist();
        if (action.equals("panelnodelist"))
            return panelnodelist();
        if (action.equals("monitorswitchlist"))
            return monitorswitchlist();
        if (action.equals("monitorroutelist"))
            return monitorroutelist();
        if (action.equals("monitorfirewalllist"))
            return monitorfirewalllist();
        if (action.equals("read"))
            return read();
        if (action.equals("ready_edit"))
            return readyEdit();
        if (action.equals("ready_editalias"))
            return readyEditAlias();
        if (action.equals("ready_editsysgroup"))
            return readyEditSysGroup();
        if (action.equals("ready_editsnmp"))
            return readyEditSnmp();
        if (action.equals("ready_editipalias"))
            return ready_EditIpAlias();
        if (action.equals("update"))
            return update();
        if (action.equals("cancelmanage"))
            return cancelmanage();
        if (action.equals("menucancelmanage"))
            return menucancelmanage();
        if (action.equals("menuaddmanage"))
            return menuaddmanage();
        if (action.equals("menucancelendpoint"))
            return menucancelendpoint();
        if (action.equals("menuaddendpoint"))
            return menuaddendpoint();
        if (action.equals("detailcancelmanage"))
            return detailcancelmanage();
        if (action.equals("updatealias"))
            return updatealias();
        if (action.equals("updateAlias"))
            return updateAlias();
        if (action.equals("updatesysgroup"))
            return updatesysgroup();
        if (action.equals("updatesnmp"))
            return updatesnmp();
        if (action.equals("refreshsysname"))
            return refreshsysname();
        if (action.equals("delete"))
            return delete();
        if (action.equals("find"))
            return find();
        if (action.equals("queryByCondition"))
            return queryByCondition();
        if (action.equals("ready_add"))
            return ready_add();
        if (action.equals("monitorfind"))
            return monitorfind();
        if (action.equals("add"))
            return add();
        if (action.equals("telnet"))
            return telnet();
        if (action.equals("save"))
            return save();
        if (action.equals("listbynameasc"))
            return listByNameAsc();
        if (action.equals("listbynamedesc"))
            return listByNameDesc();
        if (action.equals("listbyipasc"))
            return listByIpAsc();
        if (action.equals("listbyipdesc"))
            return listByIpDesc();
        if (action.equals("listbybripasc"))
            return listByBrIpAsc();
        if (action.equals("listbybripdesc"))
            return listByBrIpDesc();
        if (action.equals("listbynodeasc"))
            return listByNodeAsc();
        if (action.equals("listbynodedesc"))
            return listByNodeDesc();
        if (action.equals("listbynodenameasc"))
            return listByNodeNameAsc();
        if (action.equals("listbynodenamedesc"))
            return listByNodeNameDesc();
        if (action.equals("monitorswitchbynameasc"))
            return monitorswitchlistByNameAsc();
        if (action.equals("monitorswitchbynamedesc"))
            return monitorswitchlistByNameDesc();
        if (action.equals("monitorswitchbyipasc"))
            return monitorswitchlistByIpAsc();
        if (action.equals("monitorswitchbyipdesc"))
            return monitorswitchlistByIpDesc();

        if (action.equals("monitorhostbynameasc"))
            return monitorhostlistByNameAsc();
        if (action.equals("monitorhostbynamedesc"))
            return monitorhostlistByNameDesc();
        if (action.equals("monitorhostbyipasc"))
            return monitorhostlistByIpAsc();
        if (action.equals("monitorhostbyipdesc"))
            return monitorhostlistByIpDesc();
        if (action.equals("editall"))
            return editall();
        if (action.equals("updateBid"))
            return updateBid();
        // quzhi add
        if (action.equals("netChoce"))
            return netChoce();
        if (action.equals("hostChoce"))
            return hostChoce();
        if (action.equals("netchocereport"))
            return netchocereport();
        if (action.equals("hostchocereport"))
            return hostchocereport();
        if (action.equals("downloadnetworklistback"))
            return downloadnetworklistback();
        if (action.equals("remotePing")) {
            return remotePing();
        }
        if (action.equals("alladd")) {
            return alladd();
        }
        if (action.equals("ipalladd")) {
            return ipalladd();
        }
        if (action.equals("batchModifyMoniter")) {
            return batchModifyMoniter();
        }
        if (action.equals("showaddbatchhosts")) {
            return showaddbatchhosts();
        }
        if (action.equals("addbatchhosts")) {
            return addBatchHosts();
        }
        setErrorCode(ErrorMessage.ACTION_NO_FOUND);
        return null;
    }

    private String alladd() {
        return "/topology/network/alladdlist.jsp";
    }

    private String showaddbatchhosts() {
        return "/topology/network/importbatchhosts.jsp";
    }

    public String ipalladd() {

        String ipaddress = "";
        String startip = "";
        String endip = "";
        int sign = Integer.parseInt(getParaValue("sign"));
        if (sign == 1) {
            ipaddress = getParaValue("ipaddress");
        } else if (sign == 2) {
            startip = getParaValue("startip");
            endip = getParaValue("endip");
        }
        Hashtable hst = new Hashtable();
        hst.put("ipaddress", ipaddress);
        hst.put("startip", startip);
        hst.put("endip", endip);
        PollingEngine.getAddiplist().add(hst);

        return "/network.do?action=alladd";
    }

    /**
     * remotePing ����
     * 
     * @return
     * @author snow
     */
    private String remotePing() {
        String value = "";
        SnmpPing snmpPing = null;
        String ip = getParaValue("ip");
        try {
            snmpPing = new SnmpPing(getParaValue("ipaddress"), "161");
            String community = getParaValue("community");
            if (community == null || "".equals(community)) {
                request.setAttribute("pingResult", "������Ϊ�գ���������������");
                return "/tool/remotePing2.jsp";
            }

            String version = getParaValue("version");
            if (version != null && (version.equals("v2") || version.equals("V2"))) {
                System.out.println("you have set your version to v2");
                snmpPing.setVersion(1);
            } else {
                snmpPing.setVersion(0);
            }

            snmpPing.setCommunity(community); // д����
            snmpPing.setTimeout("5000"); // ��λ ms
            // 0 �� 1 �� 3
            value = snmpPing.ping(ip);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (snmpPing != null) {
                snmpPing.close(); // �ر�snmp������
            }
        }

        StringBuffer pingResult = new StringBuffer("<br>Snmp RemotePing Ip��" + ip + "<br>");
        if (value == null) {
            pingResult.append("����ipΪ��");
        } else if ("Null".equals(value)) {
            pingResult.append("��IP�޷�pingͨ");
        } else if ("Uncertain".equals(value)) {
            pingResult.append("�����ȷ����������ĳ�������쳣");
        } else if (value.matches("\\d+")) {
            pingResult.append("ƽ����Ӧʱ��Ϊ�� " + value + " ����");
        } else {
            pingResult.append("�����쳣");
        }
        request.setAttribute("pingResult", pingResult.toString());
        return "/tool/remotePing2.jsp";
    }

    // quzhi
    private String editall() {
        String[] ids = getParaArrayValue("checkbox");
        String hostid = "";
        if (ids != null && ids.length > 0) {
            // �����޸�
            for (int i = 0; i < ids.length; i++) {
                String id = ids[i];
                HostNodeDao dao = new HostNodeDao();
                HostNode host = null;
                try {
                    host = (HostNode) dao.findByID(id);
                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                    dao.close();
                }
                hostid = hostid + host.getId() + ",";

            }
            request.setAttribute("hostid", hostid);
        }
        return "/topology/network/editall.jsp";
    }

    // quzhi
    private String updateBid() {
        String ids = request.getParameter("ids");
        String[] businessids = getParaArrayValue("checkboxbid");
        String[] bids = ids.split(",");
        if (bids != null && bids.length > 0) {
            // �����޸�
            for (int i = 0; i < bids.length; i++) {
                String hostid = bids[i];
                String allbid = ",";
                if (businessids != null && businessids.length > 0) {
                    for (int j = 0; j < businessids.length; j++) {
                        String bid = businessids[j];
                        allbid = allbid + bid + ",";
                    }
                    HostNodeDao dao = new HostNodeDao();
                    try {
                        dao.updatebid(allbid, hostid);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    } finally {
                        dao.close();
                    }
                    // �����ڴ�
                    Host host = (Host) PollingEngine.getInstance().getNodeByID(Integer.parseInt(hostid));
                    if (host != null)
                        host.setBid(allbid);
                }
            }
        }
        return "/network.do?action=list";
    }

    /*
     * ��ѯ�豸
     */
    private String monitorfind() {
        String key = getParaValue("key");
        String value = getParaValue("value");
        HostNodeDao dao = new HostNodeDao();
        List searchList = new ArrayList();
        try {
            searchList = dao.findByCondition(key, value);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dao.close();
        }
        request.setAttribute("list", searchList);

        return "/topology/network/monitorfind.jsp";
    }

    /**
     * �����豸���� ��������
     * 
     * @return
     */
    private String listByNameAsc() {

        request.setAttribute("actionlist", "listbynameasc");
        setTarget("/topology/network/list.jsp");

        User current_user = (User) session.getAttribute(SessionConstant.CURRENT_USER);
        StringBuffer s = new StringBuffer();
        int _flag = 0;
        if (current_user.getBusinessids() != null) {
            if (current_user.getBusinessids() != "-1") {
                String[] bids = current_user.getBusinessids().split(",");
                if (bids.length > 0) {
                    for (int i = 0; i < bids.length; i++) {
                        if (bids[i].trim().length() > 0) {
                            if (_flag == 0) {
                                s.append(" and ( bid like '%," + bids[i].trim() + ",%' ");
                                _flag = 1;
                            } else {
                                // flag = 1;
                                s.append(" or bid like '%," + bids[i].trim() + ",%' ");
                            }
                        }
                    }
                    s.append(") ");
                }

            }
        }
        HostNodeDao dao = new HostNodeDao();
        if (current_user.getRole() == 0) {
            return list(dao, "where 1=1 order by sys_name asc");
        } else {
            return list(dao, "where 1=1 " + s + " order by sys_name asc");
        }

        // return list(dao,"order by sys_name asc");
    }

    /**
     * �����豸���� ��������
     * 
     * @return
     */
    private String listByNameDesc() {

        request.setAttribute("actionlist", "listbynamedesc");
        setTarget("/topology/network/list.jsp");
        User current_user = (User) session.getAttribute(SessionConstant.CURRENT_USER);
        StringBuffer s = new StringBuffer();
        int _flag = 0;
        if (current_user.getBusinessids() != null) {
            if (current_user.getBusinessids() != "-1") {
                String[] bids = current_user.getBusinessids().split(",");
                if (bids.length > 0) {
                    for (int i = 0; i < bids.length; i++) {
                        if (bids[i].trim().length() > 0) {
                            if (_flag == 0) {
                                s.append(" and ( bid like '%," + bids[i].trim() + ",%' ");
                                _flag = 1;
                            } else {
                                // flag = 1;
                                s.append(" or bid like '%," + bids[i].trim() + ",%' ");
                            }
                        }
                    }
                    s.append(") ");
                }

            }
        }
        HostNodeDao dao = new HostNodeDao();
        if (current_user.getRole() == 0) {
            return list(dao, "where 1=1 order by sys_name desc");
        } else {
            return list(dao, "where 1=1 " + s + " order by sys_name desc");
        }

        // return list(dao,"order by sys_name desc");
    }

    /**
     * ����IP��ַ ��������
     * 
     * @return
     */
    private String listByIpAsc() {

        request.setAttribute("actionlist", "listbyipasc");
        setTarget("/topology/network/list.jsp");
        User current_user = (User) session.getAttribute(SessionConstant.CURRENT_USER);
        StringBuffer s = new StringBuffer();
        int _flag = 0;
        if (current_user.getBusinessids() != null) {
            if (current_user.getBusinessids() != "-1") {
                String[] bids = current_user.getBusinessids().split(",");
                if (bids.length > 0) {
                    for (int i = 0; i < bids.length; i++) {
                        if (bids[i].trim().length() > 0) {
                            if (_flag == 0) {
                                s.append(" and ( bid like '%," + bids[i].trim() + ",%' ");
                                _flag = 1;
                            } else {
                                // flag = 1;
                                s.append(" or bid like '%," + bids[i].trim() + ",%' ");
                            }
                        }
                    }
                    s.append(") ");
                }

            }
        }
        HostNodeDao dao = new HostNodeDao();
        if (current_user.getRole() == 0) {
            return list(dao, "where 1=1 order by ip_address asc");
        } else {
            return list(dao, "where 1=1 " + s + " order by ip_address asc");
        }

        // return list(dao,"order by ip_address asc");
    }

    /**
     * ����IP��ַ ��������
     * 
     * @return
     */
    private String listByIpDesc() {

        request.setAttribute("actionlist", "listbyipdesc");
        setTarget("/topology/network/list.jsp");
        User current_user = (User) session.getAttribute(SessionConstant.CURRENT_USER);
        StringBuffer s = new StringBuffer();
        int _flag = 0;
        if (current_user.getBusinessids() != null) {
            if (current_user.getBusinessids() != "-1") {
                String[] bids = current_user.getBusinessids().split(",");
                if (bids.length > 0) {
                    for (int i = 0; i < bids.length; i++) {
                        if (bids[i].trim().length() > 0) {
                            if (_flag == 0) {
                                s.append(" and ( bid like '%," + bids[i].trim() + ",%' ");
                                _flag = 1;
                            } else {
                                // flag = 1;
                                s.append(" or bid like '%," + bids[i].trim() + ",%' ");
                            }
                        }
                    }
                    s.append(") ");
                }

            }
        }
        HostNodeDao dao = new HostNodeDao();
        if (current_user.getRole() == 0) {
            return list(dao, "where 1=1 order by ip_address desc");
        } else {
            return list(dao, "where 1=1 " + s + " order by ip_address desc");
        }

        // return list(dao,"order by ip_address desc");
    }

    /**
     * ����MAC��ַ ��������
     * 
     * @return
     */
    private String listByBrIpAsc() {

        request.setAttribute("actionlist", "listbybripasc");
        setTarget("/topology/network/list.jsp");
        User current_user = (User) session.getAttribute(SessionConstant.CURRENT_USER);
        StringBuffer s = new StringBuffer();
        int _flag = 0;
        if (current_user.getBusinessids() != null) {
            if (current_user.getBusinessids() != "-1") {
                String[] bids = current_user.getBusinessids().split(",");
                if (bids.length > 0) {
                    for (int i = 0; i < bids.length; i++) {
                        if (bids[i].trim().length() > 0) {
                            if (_flag == 0) {
                                s.append(" and ( bid like '%," + bids[i].trim() + ",%' ");
                                _flag = 1;
                            } else {
                                // flag = 1;
                                s.append(" or bid like '%," + bids[i].trim() + ",%' ");
                            }
                        }
                    }
                    s.append(") ");
                }

            }
        }
        HostNodeDao dao = new HostNodeDao();
        if (current_user.getRole() == 0) {
            return list(dao, "where 1=1 order by bridge_address asc");
        } else {
            return list(dao, "where 1=1 " + s + " order by bridge_address asc");
        }

        // return list(dao,"order by bridge_address asc");
    }

    /**
     * ����MAC��ַ ��������
     * 
     * @return
     */
    private String listByBrIpDesc() {

        request.setAttribute("actionlist", "listbybripdesc");
        setTarget("/topology/network/list.jsp");
        User current_user = (User) session.getAttribute(SessionConstant.CURRENT_USER);
        StringBuffer s = new StringBuffer();
        int _flag = 0;
        if (current_user.getBusinessids() != null) {
            if (current_user.getBusinessids() != "-1") {
                String[] bids = current_user.getBusinessids().split(",");
                if (bids.length > 0) {
                    for (int i = 0; i < bids.length; i++) {
                        if (bids[i].trim().length() > 0) {
                            if (_flag == 0) {
                                s.append(" and ( bid like '%," + bids[i].trim() + ",%' ");
                                _flag = 1;
                            } else {
                                // flag = 1;
                                s.append(" or bid like '%," + bids[i].trim() + ",%' ");
                            }
                        }
                    }
                    s.append(") ");
                }

            }
        }
        HostNodeDao dao = new HostNodeDao();
        if (current_user.getRole() == 0) {
            return list(dao, "where 1=1 order by bridge_address desc");
        } else {
            return list(dao, "where 1=1 " + s + " order by bridge_address desc");
        }
        // return list(dao,"order by bridge_address desc");
    }

    /**
     * ���Ӷ��� ����IP��ַ ��������
     * 
     * @return
     */
    private String listByNodeAsc() {

        request.setAttribute("actionlist", "listbynodeasc");
        setTarget("/topology/network/monitornodelist.jsp");
        User current_user = (User) session.getAttribute(SessionConstant.CURRENT_USER);
        StringBuffer s = new StringBuffer();
        int _flag = 0;
        if (current_user.getBusinessids() != null) {
            if (current_user.getBusinessids() != "-1") {
                String[] bids = current_user.getBusinessids().split(",");
                if (bids.length > 0) {
                    for (int i = 0; i < bids.length; i++) {
                        if (bids[i].trim().length() > 0) {
                            if (_flag == 0) {
                                s.append(" and ( bid like '%," + bids[i].trim() + ",%' ");
                                _flag = 1;
                            } else {
                                // flag = 1;
                                s.append(" or bid like '%," + bids[i].trim() + ",%' ");
                            }
                        }
                    }
                    s.append(") ");
                }

            }
        }
        HostNodeDao dao = new HostNodeDao();
        if (current_user.getRole() == 0) {
            return list(dao, "where managed=1 order by ip_address asc");
        } else {
            return list(dao, "where managed=1 " + s + " order by ip_address asc");
        }

        // return list(dao,"where managed=1 order by ip_address asc");
    }

    /**
     * ���Ӷ��� ����IP��ַ ��������
     * 
     * @return
     */
    private String listByNodeDesc() {

        request.setAttribute("actionlist", "listbynodedesc");
        setTarget("/topology/network/monitornodelist.jsp");
        User current_user = (User) session.getAttribute(SessionConstant.CURRENT_USER);
        StringBuffer s = new StringBuffer();
        int _flag = 0;
        if (current_user.getBusinessids() != null) {
            if (current_user.getBusinessids() != "-1") {
                String[] bids = current_user.getBusinessids().split(",");
                if (bids.length > 0) {
                    for (int i = 0; i < bids.length; i++) {
                        if (bids[i].trim().length() > 0) {
                            if (_flag == 0) {
                                s.append(" and ( bid like '%," + bids[i].trim() + ",%' ");
                                _flag = 1;
                            } else {
                                // flag = 1;
                                s.append(" or bid like '%," + bids[i].trim() + ",%' ");
                            }
                        }
                    }
                    s.append(") ");
                }

            }
        }
        HostNodeDao dao = new HostNodeDao();
        if (current_user.getRole() == 0) {
            return list(dao, "where managed=1 order by ip_address desc");
        } else {
            return list(dao, "where managed=1 " + s + " order by ip_address desc");
        }

        // return list(dao,"where managed=1 order by ip_address desc");
    }

    /**
     * ���Ӷ��� �����豸���� ��������
     * 
     * @return
     */
    private String listByNodeNameAsc() {

        request.setAttribute("actionlist", "listbynodenameasc");
        setTarget("/topology/network/monitornodelist.jsp");
        User current_user = (User) session.getAttribute(SessionConstant.CURRENT_USER);
        StringBuffer s = new StringBuffer();
        int _flag = 0;
        if (current_user.getBusinessids() != null) {
            if (current_user.getBusinessids() != "-1") {
                String[] bids = current_user.getBusinessids().split(",");
                if (bids.length > 0) {
                    for (int i = 0; i < bids.length; i++) {
                        if (bids[i].trim().length() > 0) {
                            if (_flag == 0) {
                                s.append(" and ( bid like '%," + bids[i].trim() + ",%' ");
                                _flag = 1;
                            } else {
                                // flag = 1;
                                s.append(" or bid like '%," + bids[i].trim() + ",%' ");
                            }
                        }
                    }
                    s.append(") ");
                }

            }
        }
        HostNodeDao dao = new HostNodeDao();
        if (current_user.getRole() == 0) {
            return list(dao, "where managed=1 order by sys_name asc");
        } else {
            return list(dao, "where managed=1 " + s + " order by sys_name asc");
        }

        // return list(dao,"where managed=1 order by sys_name asc");
    }

    /**
     * ���Ӷ��� �����豸���� ��������
     * 
     * @return
     */
    private String listByNodeNameDesc() {

        request.setAttribute("actionlist", "listbynodenamedesc");
        setTarget("/topology/network/monitornodelist.jsp");
        User current_user = (User) session.getAttribute(SessionConstant.CURRENT_USER);
        StringBuffer s = new StringBuffer();
        int _flag = 0;
        if (current_user.getBusinessids() != null) {
            if (current_user.getBusinessids() != "-1") {
                String[] bids = current_user.getBusinessids().split(",");
                if (bids.length > 0) {
                    for (int i = 0; i < bids.length; i++) {
                        if (bids[i].trim().length() > 0) {
                            if (_flag == 0) {
                                s.append(" and ( bid like '%," + bids[i].trim() + ",%' ");
                                _flag = 1;
                            } else {
                                // flag = 1;
                                s.append(" or bid like '%," + bids[i].trim() + ",%' ");
                            }
                        }
                    }
                    s.append(") ");
                }

            }
        }
        HostNodeDao dao = new HostNodeDao();
        if (current_user.getRole() == 0) {
            return list(dao, "where managed=1 order by sys_name desc");
        } else {
            return list(dao, "where managed=1 " + s + " order by sys_name desc");
        }
        // return list(dao,"where managed=1 order by sys_name desc");
    }

    /**
     * �������������� ����
     * 
     * @return
     */
    private String monitorswitchlistByNameAsc() {

        request.setAttribute("actionlist", "monitorswitchbynameasc");
        setTarget("/topology/network/monitorswitchlist.jsp");
        User current_user = (User) session.getAttribute(SessionConstant.CURRENT_USER);
        StringBuffer s = new StringBuffer();
        int _flag = 0;
        if (current_user.getBusinessids() != null) {
            if (current_user.getBusinessids() != "-1") {
                String[] bids = current_user.getBusinessids().split(",");
                if (bids.length > 0) {
                    for (int i = 0; i < bids.length; i++) {
                        if (bids[i].trim().length() > 0) {
                            if (_flag == 0) {
                                s.append(" and ( bid like '%," + bids[i].trim() + ",%' ");
                                _flag = 1;
                            } else {
                                // flag = 1;
                                s.append(" or bid like '%," + bids[i].trim() + ",%' ");
                            }
                        }
                    }
                    s.append(") ");
                }

            }
        }
        HostNodeDao dao = new HostNodeDao();
        if (current_user.getRole() == 0) {
            return list(dao, " where managed=1 and (category=2 or category=3 or category=7) order by alias asc ");
        } else {
            return list(dao, " where managed=1 " + s + " and (category=2 or category=3 or category=7) order by alias asc ");
        }
    }

    /**
     * �������������� ����
     * 
     * @return
     */
    private String monitorswitchlistByNameDesc() {

        request.setAttribute("actionlist", "monitorswitchbynamedesc");
        setTarget("/topology/network/monitorswitchlist.jsp");
        User current_user = (User) session.getAttribute(SessionConstant.CURRENT_USER);
        StringBuffer s = new StringBuffer();
        int _flag = 0;
        if (current_user.getBusinessids() != null) {
            if (current_user.getBusinessids() != "-1") {
                String[] bids = current_user.getBusinessids().split(",");
                if (bids.length > 0) {
                    for (int i = 0; i < bids.length; i++) {
                        if (bids[i].trim().length() > 0) {
                            if (_flag == 0) {
                                s.append(" and ( bid like '%," + bids[i].trim() + ",%' ");
                                _flag = 1;
                            } else {
                                // flag = 1;
                                s.append(" or bid like '%," + bids[i].trim() + ",%' ");
                            }
                        }
                    }
                    s.append(") ");
                }

            }
        }
        HostNodeDao dao = new HostNodeDao();
        if (current_user.getRole() == 0) {
            return list(dao, " where managed=1 and (category=2 or category=3 or category=7) order by alias desc ");
        } else {
            return list(dao, " where managed=1 " + s + " and (category=2 or category=3 or category=7) order by alias desc ");
        }

    }

    /**
     * ����������IP ����
     * 
     * @return
     */
    private String monitorswitchlistByIpAsc() {

        request.setAttribute("actionlist", "monitorswitchbyipasc");
        setTarget("/topology/network/monitorswitchlist.jsp");
        User current_user = (User) session.getAttribute(SessionConstant.CURRENT_USER);
        StringBuffer s = new StringBuffer();
        int _flag = 0;
        if (current_user.getBusinessids() != null) {
            if (current_user.getBusinessids() != "-1") {
                String[] bids = current_user.getBusinessids().split(",");
                if (bids.length > 0) {
                    for (int i = 0; i < bids.length; i++) {
                        if (bids[i].trim().length() > 0) {
                            if (_flag == 0) {
                                s.append(" and ( bid like '%," + bids[i].trim() + ",%' ");
                                _flag = 1;
                            } else {
                                // flag = 1;
                                s.append(" or bid like '%," + bids[i].trim() + ",%' ");
                            }
                        }
                    }
                    s.append(") ");
                }

            }
        }
        HostNodeDao dao = new HostNodeDao();
        if (current_user.getRole() == 0) {
            return list(dao, " where managed=1 and (category=2 or category=3 or category=7) order by ip_address asc ");
        } else {
            return list(dao, " where managed=1 " + s + " and (category=2 or category=3 or category=7) order by ip_address asc ");
        }

    }

    /**
     * ����������IP ����
     * 
     * @return
     */
    private String monitorswitchlistByIpDesc() {

        request.setAttribute("actionlist", "monitorswitchbyipdesc");
        setTarget("/topology/network/monitorswitchlist.jsp");
        User current_user = (User) session.getAttribute(SessionConstant.CURRENT_USER);
        StringBuffer s = new StringBuffer();
        int _flag = 0;
        if (current_user.getBusinessids() != null) {
            if (current_user.getBusinessids() != "-1") {
                String[] bids = current_user.getBusinessids().split(",");
                if (bids.length > 0) {
                    for (int i = 0; i < bids.length; i++) {
                        if (bids[i].trim().length() > 0) {
                            if (_flag == 0) {
                                s.append(" and ( bid like '%," + bids[i].trim() + ",%' ");
                                _flag = 1;
                            } else {
                                // flag = 1;
                                s.append(" or bid like '%," + bids[i].trim() + ",%' ");
                            }
                        }
                    }
                    s.append(") ");
                }

            }
        }
        HostNodeDao dao = new HostNodeDao();
        if (current_user.getRole() == 0) {
            return list(dao, " where managed=1 and (category=2 or category=3 or category=7) order by ip_address desc ");
        } else {
            return list(dao, " where managed=1 " + s + " and (category=2 or category=3 or category=7) order by ip_address desc ");
        }

    }

    /**
     * �������������� ����
     * 
     * @return
     */
    private String monitorhostlistByNameAsc() {

        request.setAttribute("actionlist", "monitorhostbynameasc");
        setTarget("/topology/network/monitorhostlist.jsp");
        User current_user = (User) session.getAttribute(SessionConstant.CURRENT_USER);
        StringBuffer s = new StringBuffer();
        int _flag = 0;
        if (current_user.getBusinessids() != null) {
            if (current_user.getBusinessids() != "-1") {
                String[] bids = current_user.getBusinessids().split(",");
                if (bids.length > 0) {
                    for (int i = 0; i < bids.length; i++) {
                        if (bids[i].trim().length() > 0) {
                            if (_flag == 0) {
                                s.append(" and ( bid like '%," + bids[i].trim() + ",%' ");
                                _flag = 1;
                            } else {
                                // flag = 1;
                                s.append(" or bid like '%," + bids[i].trim() + ",%' ");
                            }
                        }
                    }
                    s.append(") ");
                }

            }
        }
        HostNodeDao dao = new HostNodeDao();
        if (current_user.getRole() == 0) {
            return list(dao, " where managed=1 and category=4 order by alias asc ");
        } else {
            return list(dao, " where managed=1 " + s + " and category=4 order by alias asc ");
        }

    }

    /**
     * �������������� ����
     * 
     * @return
     */
    private String monitorhostlistByNameDesc() {

        request.setAttribute("actionlist", "monitorhostbynamedesc");
        setTarget("/topology/network/monitorhostlist.jsp");
        User current_user = (User) session.getAttribute(SessionConstant.CURRENT_USER);
        StringBuffer s = new StringBuffer();
        int _flag = 0;
        if (current_user.getBusinessids() != null) {
            if (current_user.getBusinessids() != "-1") {
                String[] bids = current_user.getBusinessids().split(",");
                if (bids.length > 0) {
                    for (int i = 0; i < bids.length; i++) {
                        if (bids[i].trim().length() > 0) {
                            if (_flag == 0) {
                                s.append(" and ( bid like '%," + bids[i].trim() + ",%' ");
                                _flag = 1;
                            } else {
                                // flag = 1;
                                s.append(" or bid like '%," + bids[i].trim() + ",%' ");
                            }
                        }
                    }
                    s.append(") ");
                }

            }
        }
        HostNodeDao dao = new HostNodeDao();
        if (current_user.getRole() == 0) {
            return list(dao, " where managed=1 and category=4 order by alias desc ");
        } else {
            return list(dao, " where managed=1 " + s + " and category=4 order by alias desc ");
        }

    }

    /**
     * ����������IP ����
     * 
     * @return
     */
    private String monitorhostlistByIpAsc() {

        request.setAttribute("actionlist", "monitorhostbyipasc");
        setTarget("/topology/network/monitorhostlist.jsp");
        User current_user = (User) session.getAttribute(SessionConstant.CURRENT_USER);
        StringBuffer s = new StringBuffer();
        int _flag = 0;
        if (current_user.getBusinessids() != null) {
            if (current_user.getBusinessids() != "-1") {
                String[] bids = current_user.getBusinessids().split(",");
                if (bids.length > 0) {
                    for (int i = 0; i < bids.length; i++) {
                        if (bids[i].trim().length() > 0) {
                            if (_flag == 0) {
                                s.append(" and ( bid like '%," + bids[i].trim() + ",%' ");
                                _flag = 1;
                            } else {
                                // flag = 1;
                                s.append(" or bid like '%," + bids[i].trim() + ",%' ");
                            }
                        }
                    }
                    s.append(") ");
                }

            }
        }
        HostNodeDao dao = new HostNodeDao();
        if (current_user.getRole() == 0) {
            return list(dao, " where managed=1 and category=4 order by ip_address asc ");
        } else {
            return list(dao, " where managed=1  " + s + " and category=4 order by ip_address asc ");
        }

    }

    /**
     * ����������IP ����
     * 
     * @return
     */
    private String monitorhostlistByIpDesc() {

        request.setAttribute("actionlist", "monitorhostbyipdesc");
        setTarget("/topology/network/monitorhostlist.jsp");
        User current_user = (User) session.getAttribute(SessionConstant.CURRENT_USER);
        StringBuffer s = new StringBuffer();
        int _flag = 0;
        if (current_user.getBusinessids() != null) {
            if (current_user.getBusinessids() != "-1") {
                String[] bids = current_user.getBusinessids().split(",");
                if (bids.length > 0) {
                    for (int i = 0; i < bids.length; i++) {
                        if (bids[i].trim().length() > 0) {
                            if (_flag == 0) {
                                s.append(" and ( bid like '%," + bids[i].trim() + ",%' ");
                                _flag = 1;
                            } else {
                                // flag = 1;
                                s.append(" or bid like '%," + bids[i].trim() + ",%' ");
                            }
                        }
                    }
                    s.append(") ");
                }

            }
        }
        HostNodeDao dao = new HostNodeDao();
        if (current_user.getRole() == 0) {
            return list(dao, " where managed=1 and category=4 order by ip_address desc ");
        } else {
            return list(dao, " where managed=1  " + s + " and category=4 order by ip_address desc ");
        }

    }

    // quzhi
    private String hostchocereport() {
        String oids = getParaValue("ids");
        if (oids == null)
            oids = "";
        // SysLogger.info("ids========="+oids);
        Integer[] ids = null;
        if (oids.split(",").length > 0) {
            String[] _ids = oids.split(",");
            if (_ids != null && _ids.length > 0)
                ids = new Integer[_ids.length];
            for (int i = 0; i < _ids.length; i++) {
                if (_ids[i] == null || _ids[i].trim().length() == 0)
                    continue;
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
        try {
            Hashtable allreporthash = new Hashtable();
            if (ids != null && ids.length > 0) {
                for (int i = 0; i < ids.length; i++) {
                    HostNodeDao dao = new HostNodeDao();
                    Hashtable reporthash = new Hashtable();
                    HostNode node = (HostNode) dao.loadHost(ids[i]);
                    dao.close();
                    // ----------------------------shijian
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
                            if (eventlist.getLevel1() == null)
                                continue;
                            if (eventlist.getLevel1() == 1) {
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
                    // ----------------------------------shijian
                    ip = node.getIpAddress();
                    equipname = node.getAlias();
                    String remoteip = request.getRemoteAddr();
                    String newip = doip(ip);
                    String[] time = { "", "" };
                    // ��lastcollectdata��ȡ���µ�cpu�����ʣ��ڴ������ʣ���������������
                    String[] item = { "CPU" };
                    // hash =
                    // hostlastmanager.getbyCategories_share(ip,item,starttime,endtime);
                    /*
                     * //hash = hostlastmanager.getbyCategories(ip, item,
                     * startdate, todate);
                     */
                    memhash = hostlastmanager.getMemory_share(ip, "Memory", startdate, todate);
                    // memhash =
                    // hostlastmanager.getMemory(ip,"Memory",starttime,endtime);
                    diskhash = hostlastmanager.getDisk_share(ip, "Disk", startdate, todate);
                    // diskhash =
                    // hostlastmanager.getDisk(ip,"Disk",starttime,endtime);
                    // ��collectdata��ȡһ��ʱ���cpu�����ʣ��ڴ������ʵ���ʷ�����Ի�����ͼ��ͬʱȡ�����ֵ
                    Hashtable cpuhash = hostmanager.getCategory(ip, "CPU", "Utilization", starttime, totime);
                    Hashtable[] memoryhash = hostmanager.getMemory(ip, "Memory", starttime, totime);
                    // ��memory���ֵ
                    memmaxhash = memoryhash[1];
                    memavghash = memoryhash[2];
                    // cpu���ֵ
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

                    Hashtable ConnectUtilizationhash = hostmanager.getCategory(ip, "Ping", "ConnectUtilization", starttime, totime);
                    String pingconavg = "";
                    if (ConnectUtilizationhash.get("avgpingcon") != null)
                        pingconavg = (String) ConnectUtilizationhash.get("avgpingcon");
                    String ConnectUtilizationmax = "";
                    maxping.put("avgpingcon", pingconavg);
                    if (ConnectUtilizationhash.get("max") != null) {
                        ConnectUtilizationmax = (String) ConnectUtilizationhash.get("max");
                    }
                    maxping.put("pingmax", ConnectUtilizationmax);

                    // Hashtable reporthash = new Hashtable();

                    Vector pdata = (Vector) pingdata.get(ip);
                    // ��ping�õ������ݼӽ�ȥ
                    if (pdata != null && pdata.size() > 0) {
                        for (int m = 0; m < pdata.size(); m++) {
                            Pingcollectdata hostdata = (Pingcollectdata) pdata.get(m);
                            if (hostdata != null) {
                                if (hostdata.getSubentity() != null) {
                                    if (hostdata.getSubentity().equals("ConnectUtilization")) {
                                        reporthash.put("time", hostdata.getCollecttime());
                                        reporthash.put("Ping", hostdata.getThevalue());
                                        reporthash.put("ping", maxping);
                                    }
                                } else {
                                    reporthash.put("time", hostdata.getCollecttime());
                                    reporthash.put("Ping", hostdata.getThevalue());
                                    reporthash.put("ping", maxping);

                                }
                            } else {
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
                    Vector cpuVector = (Vector) hdata.get("cpu");
                    if (cpuVector != null && cpuVector.size() > 0) {
                        for (int si = 0; si < cpuVector.size(); si++) {
                            CPUcollectdata cpudata = (CPUcollectdata) cpuVector.elementAt(si);
                            maxhash.put("cpu", cpudata.getThevalue());
                            reporthash.put("CPU", maxhash);
                        }
                    } else {
                        reporthash.put("CPU", maxhash);
                    }
                    reporthash.put("Memory", memhash);
                    reporthash.put("Disk", diskhash);
                    reporthash.put("equipname", equipname);
                    reporthash.put("memmaxhash", memmaxhash);
                    reporthash.put("memavghash", memavghash);
                    allreporthash.put(ip, reporthash);
                }

            }
            request.setAttribute("startdate", startdate);
            request.setAttribute("allreporthash", allreporthash);
            request.getSession().setAttribute("allreporthash", allreporthash);
            request.setAttribute("todate", todate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "/topology/network/hostchocereport.jsp";
    }

    private String doip(String ip) {
        // // String newip="";
        // for(int i=0;i<3;i++){
        // int p=ip.indexOf(".");
        // newip+=ip.substring(0,p);
        // ip=ip.substring(p+1);
        // }
        // newip+=ip;
        // //System.out.println("newip="+newip);
        // return newip;
        String allipstr = SysUtil.doip(ip);
        return allipstr;
    }

    private String netchocereport() {
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
        // Hashtable allcpuhash = new Hashtable();
        String ip = "";
        String equipname = "";

        Hashtable memmaxhash = new Hashtable();// mem--max
        Hashtable memavghash = new Hashtable();// mem--avg
        Hashtable maxhash = new Hashtable();// "Cpu"--max
        Hashtable maxping = new Hashtable();// Ping--max
        Hashtable pingdata = ShareData.getPingdata();
        Hashtable sharedata = ShareData.getSharedata();
        User vo = (User) session.getAttribute(SessionConstant.CURRENT_USER);
        UserView view = new UserView();

        // String positionname = view.getPosition(vo.getPosition());
        // String username = vo.getName();
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

            request.setAttribute("startdate", startdate);
            request.setAttribute("allreporthash", allreporthash);
            request.getSession().setAttribute("allreporthash", allreporthash);
            request.setAttribute("todate", todate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "/topology/network/netchocereport.jsp";
    }

    /**
     * �Լ���б��������
     * 
     * @author nielin
     * @date 2010-08-09
     * @param montinorList
     *            <code>����б�</code>
     * @param category
     *            <code>�豸����</code>
     * @param field
     *            <code>�����ֶ�</code>
     * @param type
     *            <code>��������</code>
     * @return
     */
    public List monitorListSort(List montinorList, String category, String field, String type) {
        // System.out.println(category+ "===String==" + field +"====String===="+
        // type);
        // if("host".equals(category)){
        // for(int i = 0 ; i < montinorList.size() -1 ; i ++){
        // for(int j = i + 1 ; j < montinorList.size() ; j ++){
        // MonitorHostDTO monitorHostDTO = (MonitorHostDTO)montinorList.get(i);
        // MonitorHostDTO monitorHostDTO2 = (MonitorHostDTO)montinorList.get(j);
        //						
        // String fieldValue = "";
        //						
        // String fieldValue2 = "";
        // if("name".equals(field)){
        // fieldValue = monitorHostDTO.getAlias();
        //							
        // fieldValue2 = monitorHostDTO2.getAlias();
        // if("desc".equals(type)){
        // // ����ǽ��� �� ǰһ�� С�� ��һ�� �򽻻�
        // if(fieldValue.compareTo(fieldValue2) < 0){
        // montinorList.set(i, monitorHostDTO2);
        // montinorList.set(j, monitorHostDTO);
        // }
        // }else if ("asc".equals(type)){
        // // ��������� �� ǰһ�� ���� ��һ�� �򽻻�
        // if(fieldValue.compareTo(fieldValue2) > 0){
        // montinorList.set(i, monitorHostDTO2);
        // montinorList.set(j, monitorHostDTO);
        // }
        // }
        //								
        // }else if ("ipaddress".equals(field)){
        // fieldValue = monitorHostDTO.getIpAddress();
        //							
        // fieldValue2 = monitorHostDTO2.getIpAddress();
        //							
        // if("desc".equals(type)){
        // // ����ǽ��� �� ǰһ�� С�� ��һ�� �򽻻�
        // if(fieldValue.compareTo(fieldValue2) < 0){
        // montinorList.set(i, monitorHostDTO2);
        // montinorList.set(j, monitorHostDTO);
        // }
        // }else if ("asc".equals(type)){
        // // ��������� �� ǰһ�� ���� ��һ�� �򽻻�
        // if(fieldValue.compareTo(fieldValue2) > 0){
        // montinorList.set(i, monitorHostDTO2);
        // montinorList.set(j, monitorHostDTO);
        // }
        // }
        // }else if ("cpu".equals(field)){
        // fieldValue = monitorHostDTO.getCpuValue();
        //							
        // fieldValue2 = monitorHostDTO2.getCpuValue();
        //							
        // if("desc".equals(type)){
        // // ����ǽ��� �� ǰһ�� С�� ��һ�� �򽻻�
        // if(Double.valueOf(fieldValue) < Double.valueOf(fieldValue2)){
        // montinorList.set(i, monitorHostDTO2);
        // montinorList.set(j, monitorHostDTO);
        // }
        // }else if ("asc".equals(type)){
        // // ��������� �� ǰһ�� ���� ��һ�� �򽻻�
        // if(Double.valueOf(fieldValue) > Double.valueOf(fieldValue2)){
        // montinorList.set(i, monitorHostDTO2);
        // montinorList.set(j, monitorHostDTO);
        // }
        // }
        // }else if ("ping".equals(field)){
        // fieldValue = monitorHostDTO.getPingValue();
        //							
        // fieldValue2 = monitorHostDTO2.getPingValue();
        //							
        // if("desc".equals(type)){
        // // ����ǽ��� �� ǰһ�� С�� ��һ�� �򽻻�
        // if(Double.valueOf(fieldValue) < Double.valueOf(fieldValue2)){
        // montinorList.set(i, monitorHostDTO2);
        // montinorList.set(j, monitorHostDTO);
        // }
        // }else if ("asc".equals(type)){
        // // ��������� �� ǰһ�� ���� ��һ�� �򽻻�
        // if(Double.valueOf(fieldValue) > Double.valueOf(fieldValue2)){
        // montinorList.set(i, monitorHostDTO2);
        // montinorList.set(j, monitorHostDTO);
        // }
        // }
        // }else if ("memory".equals(field)){
        // fieldValue = monitorHostDTO.getMemoryValue();
        //							
        // fieldValue2 = monitorHostDTO2.getMemoryValue();
        //							
        // if("desc".equals(type)){
        // // ����ǽ��� �� ǰһ�� С�� ��һ�� �򽻻�
        // if(Double.valueOf(fieldValue) < Double.valueOf(fieldValue2)){
        // montinorList.set(i, monitorHostDTO2);
        // montinorList.set(j, monitorHostDTO);
        // }
        // }else if ("asc".equals(type)){
        // // ��������� �� ǰһ�� ���� ��һ�� �򽻻�
        // if(Double.valueOf(fieldValue) > Double.valueOf(fieldValue2)){
        // montinorList.set(i, monitorHostDTO2);
        // montinorList.set(j, monitorHostDTO);
        // }
        // }
        // }
        //						
        //						
        // }
        // }
        // } else if("net".equals(category)){
        for (int i = 0; i < montinorList.size() - 1; i++) {
            for (int j = i + 1; j < montinorList.size(); j++) {
                MonitorNodeDTO monitorNodeDTO = (MonitorNodeDTO) montinorList.get(i);
                MonitorNodeDTO monitorNodeDTO2 = (MonitorNodeDTO) montinorList.get(j);

                String fieldValue = "";

                String fieldValue2 = "";
                if ("name".equals(field)) {
                    fieldValue = monitorNodeDTO.getAlias();

                    fieldValue2 = monitorNodeDTO2.getAlias();
                    if ("desc".equals(type)) {
                        // ����ǽ��� �� ǰһ�� С�� ��һ�� �򽻻�
                        if (fieldValue.compareTo(fieldValue2) < 0) {
                            montinorList.set(i, monitorNodeDTO2);
                            montinorList.set(j, monitorNodeDTO);
                        }
                    } else if ("asc".equals(type)) {
                        // ��������� �� ǰһ�� ���� ��һ�� �򽻻�
                        if (fieldValue.compareTo(fieldValue2) > 0) {
                            montinorList.set(i, monitorNodeDTO2);
                            montinorList.set(j, monitorNodeDTO);
                        }
                    }

                } else if ("ipaddress".equals(field)) {
                    fieldValue = monitorNodeDTO.getIpAddress();

                    fieldValue2 = monitorNodeDTO2.getIpAddress();

                    if ("desc".equals(type)) {
                        // ����ǽ��� �� ǰһ�� С�� ��һ�� �򽻻�
                        if (fieldValue.compareTo(fieldValue2) < 0) {
                            montinorList.set(i, monitorNodeDTO2);
                            montinorList.set(j, monitorNodeDTO);
                        }
                    } else if ("asc".equals(type)) {
                        // ��������� �� ǰһ�� ���� ��һ�� �򽻻�
                        if (fieldValue.compareTo(fieldValue2) > 0) {
                            montinorList.set(i, monitorNodeDTO2);
                            montinorList.set(j, monitorNodeDTO);
                        }
                    }
                } else if ("cpu".equals(field)) {
                    fieldValue = monitorNodeDTO.getCpuValue();

                    fieldValue2 = monitorNodeDTO2.getCpuValue();

                    if ("desc".equals(type)) {
                        // ����ǽ��� �� ǰһ�� С�� ��һ�� �򽻻�
                        if (Double.valueOf(fieldValue) < Double.valueOf(fieldValue2)) {
                            montinorList.set(i, monitorNodeDTO2);
                            montinorList.set(j, monitorNodeDTO);
                        }
                    } else if ("asc".equals(type)) {
                        // ��������� �� ǰһ�� ���� ��һ�� �򽻻�
                        if (Double.valueOf(fieldValue) > Double.valueOf(fieldValue2)) {
                            montinorList.set(i, monitorNodeDTO2);
                            montinorList.set(j, monitorNodeDTO);
                        }
                    }
                } else if ("ping".equals(field)) {
                    fieldValue = monitorNodeDTO.getPingValue();

                    fieldValue2 = monitorNodeDTO2.getPingValue();

                    if ("desc".equals(type)) {
                        // ����ǽ��� �� ǰһ�� С�� ��һ�� �򽻻�
                        if (Double.valueOf(fieldValue) < Double.valueOf(fieldValue2)) {
                            montinorList.set(i, monitorNodeDTO2);
                            montinorList.set(j, monitorNodeDTO);
                        }
                    } else if ("asc".equals(type)) {
                        // ��������� �� ǰһ�� ���� ��һ�� �򽻻�
                        if (Double.valueOf(fieldValue) > Double.valueOf(fieldValue2)) {
                            montinorList.set(i, monitorNodeDTO2);
                            montinorList.set(j, monitorNodeDTO);
                        }
                    }
                } else if ("memory".equals(field)) {
                    fieldValue = monitorNodeDTO.getMemoryValue();

                    fieldValue2 = monitorNodeDTO2.getMemoryValue();

                    if ("desc".equals(type)) {
                        // ����ǽ��� �� ǰһ�� С�� ��һ�� �򽻻�
                        if (Double.valueOf(fieldValue) < Double.valueOf(fieldValue2)) {
                            montinorList.set(i, monitorNodeDTO2);
                            montinorList.set(j, monitorNodeDTO);
                        }
                    } else if ("asc".equals(type)) {
                        // ��������� �� ǰһ�� ���� ��һ�� �򽻻�
                        if (Double.valueOf(fieldValue) > Double.valueOf(fieldValue2)) {
                            montinorList.set(i, monitorNodeDTO2);
                            montinorList.set(j, monitorNodeDTO);
                        }
                    }
                } else if ("inutilhdx".equals(field)) {
                    fieldValue = monitorNodeDTO.getInutilhdxValue();

                    fieldValue2 = monitorNodeDTO2.getInutilhdxValue();

                    if ("desc".equals(type)) {
                        // ����ǽ��� �� ǰһ�� С�� ��һ�� �򽻻�
                        if (Double.valueOf(fieldValue) < Double.valueOf(fieldValue2)) {
                            montinorList.set(i, monitorNodeDTO2);
                            montinorList.set(j, monitorNodeDTO);
                        }
                    } else if ("asc".equals(type)) {
                        // ��������� �� ǰһ�� ���� ��һ�� �򽻻�
                        if (Double.valueOf(fieldValue) > Double.valueOf(fieldValue2)) {
                            montinorList.set(i, monitorNodeDTO2);
                            montinorList.set(j, monitorNodeDTO);
                        }
                    }
                } else if ("oututilhdx".equals(field)) {
                    fieldValue = monitorNodeDTO.getOututilhdxValue();

                    fieldValue2 = monitorNodeDTO2.getOututilhdxValue();

                    if ("desc".equals(type)) {
                        // ����ǽ��� �� ǰһ�� С�� ��һ�� �򽻻�
                        if (Double.valueOf(fieldValue) < Double.valueOf(fieldValue2)) {
                            montinorList.set(i, monitorNodeDTO2);
                            montinorList.set(j, monitorNodeDTO);
                        }
                    } else if ("asc".equals(type)) {
                        // ��������� �� ǰһ�� ���� ��һ�� �򽻻�
                        if (Double.valueOf(fieldValue) > Double.valueOf(fieldValue2)) {
                            montinorList.set(i, monitorNodeDTO2);
                            montinorList.set(j, monitorNodeDTO);
                        }
                    }
                } else if ("category".equals(field)) {
                    fieldValue = monitorNodeDTO.getCategory();

                    fieldValue2 = monitorNodeDTO2.getCategory();

                    if ("desc".equals(type)) {
                        // ����ǽ��� �� ǰһ�� С�� ��һ�� �򽻻�
                        if (fieldValue.compareTo(fieldValue2) < 0) {
                            montinorList.set(i, monitorNodeDTO2);
                            montinorList.set(j, monitorNodeDTO);
                        }
                    } else if ("asc".equals(type)) {
                        // ��������� �� ǰһ�� ���� ��һ�� �򽻻�
                        if (fieldValue.compareTo(fieldValue2) > 0) {
                            montinorList.set(i, monitorNodeDTO2);
                            montinorList.set(j, monitorNodeDTO);
                        }
                    }
                }

            }
        }
        // }

        return montinorList;
    }

    /**
     * �����豸����б�
     * 
     * @author nielin
     * @date 2010-08-09
     * @return
     */
    private String monitornetlist() {
        // HostNodeDao dao = new HostNodeDao();
        // setTarget("/topology/network/monitorroutelist.jsp");
        // return list(dao," where managed=1 and category=1");

        String jsp = "/topology/network/monitornetlist.jsp";
        setTarget(jsp);

        List monitornetlist = getMonitorListByCategory("net");

        Hashtable sharedata = ShareData.getSharedata();

        Hashtable allpingdata = ShareData.getPingdata();

        List list = new ArrayList();

        NumberFormat numberFormat = new DecimalFormat();
        numberFormat.setMaximumFractionDigits(0);

        if (monitornetlist != null) {
            for (int i = 0; i < monitornetlist.size(); i++) {

                HostNode hostNode = (HostNode) monitornetlist.get(i);

                MonitorNodeDTO monitorNetDTO = getMonitorNodeDTOByHostNode(hostNode);

                list.add(monitorNetDTO);
            }
        }
        String field = getParaValue("field");
        String sorttype = getParaValue("sorttype");
        if (field != null) {
            if (sorttype == null || sorttype.trim().length() == 0) {
                sorttype = "asc";
            } else if ("asc".equals(sorttype)) {
                sorttype = "desc";
            } else if ("desc".equals(sorttype)) {
                sorttype = "asc";
            }

            monitorListSort(list, "net", field, sorttype);

            request.setAttribute("field", field);
            request.setAttribute("sorttype", sorttype);
        }
        request.setAttribute("list", list);
        return jsp;
    }

    /**
     * ���ҵ��Ȩ�޵� SQL ���
     * 
     * @author nielin
     * @date 2010-08-09
     * @return
     */
    public String getBidSql() {
        User current_user = (User) session.getAttribute(SessionConstant.CURRENT_USER);

        StringBuffer s = new StringBuffer();
        int _flag = 0;
        if (current_user.getBusinessids() != null) {
            if (current_user.getBusinessids() != "-1") {
                String[] bids = current_user.getBusinessids().split(",");
                if (bids.length > 0) {
                    for (int i = 0; i < bids.length; i++) {
                        if (bids[i].trim().length() > 0) {
                            if (_flag == 0) {
                                s.append(" and ( bid like '%," + bids[i].trim() + ",%' ");
                                _flag = 1;
                            } else {
                                // flag = 1;
                                s.append(" or bid like '%," + bids[i].trim() + ",%' ");
                            }
                        }
                    }
                    s.append(") ");
                }

            }
        }

        SysLogger.info("select * from topo_host_node where managed=1 " + s);

        String sql = "";
        if (current_user.getRole() == 0) {
            sql = "";
        } else {
            sql = s.toString();
        }
        return sql;
    }

    /**
     * ͨ�� hostNode ����װ MonitorNodeDTO
     * 
     * @param hostNode
     * @return
     */
    public MonitorNodeDTO getMonitorNodeDTOByHostNode(HostNode hostNode) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = simpleDateFormat.format(new Date());

        String starttime = date + " 00:00:00";
        String totime = date + " 23:59:59";

        NumberFormat numberFormat = new DecimalFormat();
        numberFormat.setMaximumFractionDigits(0);

        MonitorNodeDTO monitorNodeDTO = null;

        String ipAddress = hostNode.getIpAddress();
        int nodeId = hostNode.getId();
        String alias = hostNode.getAlias();

        int category = hostNode.getCategory();
        if (category == 1) {
            monitorNodeDTO = new MonitorNetDTO();
            monitorNodeDTO.setCategory("·����");
        } else if (category == 4) {
            monitorNodeDTO = new MonitorHostDTO();
            monitorNodeDTO.setCategory("������");
        } else if (category == 8) {
            monitorNodeDTO = new MonitorHostDTO();
            monitorNodeDTO.setCategory("����ǽ");
        } else {
            monitorNodeDTO = new MonitorNetDTO();
            monitorNodeDTO.setCategory("������");
        }
        NodeUtil nodeUtil = new NodeUtil();
        NodeDTO nodeDTO = nodeUtil.creatNodeDTOByNode(hostNode);
        monitorNodeDTO.setType(nodeDTO.getType());
        monitorNodeDTO.setSubtype(nodeDTO.getSubtype());
        // ����id
        monitorNodeDTO.setId(nodeId);
        // ����ip
        monitorNodeDTO.setIpAddress(ipAddress);
        // ��������
        monitorNodeDTO.setAlias(alias);

        Host node = (Host) PollingEngine.getInstance().getNodeByID(nodeId);
        // �澯״̬
        if (node != null)
            monitorNodeDTO.setStatus(node.getStatus() + "");
        else
            monitorNodeDTO.setStatus("0");
        String cpuValue = "0"; // cpu Ĭ��Ϊ 0
        String memoryValue = "0"; // memory Ĭ��Ϊ 0
        String inutilhdxValue = "0"; // inutilhdx Ĭ��Ϊ 0
        String oututilhdxValue = "0"; // oututilhdx Ĭ��Ϊ 0
        String pingValue = "0"; // ping Ĭ��Ϊ 0
        String eventListCount = ""; // eventListCount Ĭ��Ϊ 0
        String collectType = ""; // �ɼ�����

        String cpuValueColor = "green"; // cpu ��ɫ
        String memoryValueColor = "green"; // memory ��ɫ

        String generalAlarm = "0"; // ��ͨ�澯�� Ĭ��Ϊ 0
        String urgentAlarm = "0"; // ���ظ澯�� Ĭ��Ϊ 0
        String seriousAlarm = "0"; // �����澯�� Ĭ��Ϊ 0

        double cpuValueDouble = 0;
        double memeryValueDouble = 0;

        Hashtable eventListSummary = new Hashtable();

        Hashtable sharedata = ShareData.getSharedata();

        Hashtable ipAllData = (Hashtable) sharedata.get(ipAddress);

        Hashtable allpingdata = ShareData.getPingdata();

        if (ipAllData != null) {
            Vector cpuV = (Vector) ipAllData.get("cpu");
            if (cpuV != null && cpuV.size() > 0) {
                CPUcollectdata cpu = (CPUcollectdata) cpuV.get(0);
                cpuValueDouble = Double.valueOf(cpu.getThevalue());
                cpuValue = numberFormat.format(cpuValueDouble);
            }

            Vector memoryVector = (Vector) ipAllData.get("memory");

            if (memoryVector != null && memoryVector.size() > 0) {

                for (int si = 0; si < memoryVector.size(); si++) {
                    Memorycollectdata memorydata = (Memorycollectdata) memoryVector.elementAt(si);
                    if (memorydata.getEntity().equalsIgnoreCase("Utilization")) {
                        // ������
                        if (memorydata.getSubentity().equalsIgnoreCase("PhysicalMemory")) {
                            memeryValueDouble = memeryValueDouble + Double.valueOf(memorydata.getThevalue());
                        }
                    }
                }
                memoryValue = numberFormat.format(memeryValueDouble);
            }

            Vector allutil = (Vector) ipAllData.get("allutilhdx");
            if (allutil != null && allutil.size() == 3) {
                AllUtilHdx inutilhdx = (AllUtilHdx) allutil.get(0);
                inutilhdxValue = inutilhdx.getThevalue();

                AllUtilHdx oututilhdx = (AllUtilHdx) allutil.get(1);
                oututilhdxValue = oututilhdx.getThevalue();
            }
        }

        if (allpingdata != null) {
            Vector pingData = (Vector) allpingdata.get(ipAddress);
            if (pingData != null && pingData.size() > 0) {
                Pingcollectdata pingcollectdata = (Pingcollectdata) pingData.get(0);
                pingValue = pingcollectdata.getThevalue();
            }
        }
        String count = "";
        // EventListDao eventListDao = new EventListDao();
        // try {
        // generalAlarm = eventListDao.getCountByWhere(" where nodeid='" +
        // hostNode.getId() + "'" + " and level1='1' and recordtime>='" +
        // starttime + "' and recordtime<='" + totime + "'");
        // urgentAlarm = eventListDao.getCountByWhere(" where nodeid='" +
        // hostNode.getId() + "'" + " and level1='2' and recordtime>='" +
        // starttime + "' and recordtime<='" + totime + "'");
        // seriousAlarm = eventListDao.getCountByWhere(" where nodeid='" +
        // hostNode.getId() + "'" + " and level1='3' and recordtime>='" +
        // starttime + "' and recordtime<='" + totime + "'");
        // } catch (RuntimeException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // } finally{
        // eventListDao.close();
        // }
        eventListCount = count;
        eventListSummary.put("generalAlarm", generalAlarm);
        eventListSummary.put("urgentAlarm", urgentAlarm);
        eventListSummary.put("seriousAlarm", seriousAlarm);

        monitorNodeDTO.setEventListSummary(eventListSummary);
        // �ӿ�����
        int entityNumber = 0;
        HostInterfaceDao hostInterfaceDao = null;
        try {
            hostInterfaceDao = new HostInterfaceDao();
            entityNumber = hostInterfaceDao.getEntityNumByNodeid(hostNode.getId());
        } catch (RuntimeException e1) {
            e1.printStackTrace();
        } finally {
            hostInterfaceDao.close();
        }
        monitorNodeDTO.setEntityNumber(entityNumber);

        if (SystemConstant.COLLECTTYPE_SNMP == hostNode.getCollecttype()) {
            collectType = "SNMP";
        } else if (SystemConstant.COLLECTTYPE_PING == hostNode.getCollecttype()) {
            collectType = "PING";
        } else if (SystemConstant.COLLECTTYPE_REMOTEPING == hostNode.getCollecttype()) {
            collectType = "REMOTEPING";
        } else if (SystemConstant.COLLECTTYPE_SHELL == hostNode.getCollecttype()) {
            // collectType = "SHELL";
            collectType = "����";
        } else if (SystemConstant.COLLECTTYPE_SSH == hostNode.getCollecttype()) {
            collectType = "SSH";
        } else if (SystemConstant.COLLECTTYPE_TELNET == hostNode.getCollecttype()) {
            collectType = "TELNET";
        } else if (SystemConstant.COLLECTTYPE_WMI == hostNode.getCollecttype()) {
            collectType = "WMI";
        }

        NodeMonitorDao nodeMonitorDao = new NodeMonitorDao();

        List nodeMonitorList = null;
        try {
            nodeMonitorList = nodeMonitorDao.loadByNodeID(nodeId);
        } catch (RuntimeException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            nodeMonitorDao.close();
        }
        if (nodeMonitorList != null) {
            for (int j = 0; j < nodeMonitorList.size(); j++) {
                NodeMonitor nodeMonitor = (NodeMonitor) nodeMonitorList.get(j);
                if ("cpu".equals(nodeMonitor.getCategory())) {
                    if (cpuValueDouble > nodeMonitor.getLimenvalue2()) {
                        cpuValueColor = "red";
                    } else if (cpuValueDouble > nodeMonitor.getLimenvalue1()) {
                        cpuValueColor = "orange";
                    } else if (cpuValueDouble > nodeMonitor.getLimenvalue0()) {
                        cpuValueColor = "yellow";
                    } else {
                        cpuValueColor = "green";
                    }
                }

                if ("memory".equals(nodeMonitor.getCategory())) {
                    if (memeryValueDouble > nodeMonitor.getLimenvalue2()) {
                        memoryValueColor = "red";
                    } else if (memeryValueDouble > nodeMonitor.getLimenvalue1()) {
                        memoryValueColor = "orange";
                    } else if (memeryValueDouble > nodeMonitor.getLimenvalue0()) {
                        memoryValueColor = "yellow";
                    } else {
                        memoryValueColor = "green";
                    }
                }
            }
        }

        monitorNodeDTO.setCpuValue(cpuValue);
        monitorNodeDTO.setMemoryValue(memoryValue);
        monitorNodeDTO.setInutilhdxValue(inutilhdxValue);
        monitorNodeDTO.setOututilhdxValue(oututilhdxValue);
        monitorNodeDTO.setPingValue(pingValue);
        monitorNodeDTO.setEventListCount(eventListCount);
        monitorNodeDTO.setCollectType(collectType);
        monitorNodeDTO.setCpuValueColor(cpuValueColor);
        monitorNodeDTO.setMemoryValueColor(memoryValueColor);
        return monitorNodeDTO;
    }

    public List getMonitorListByCategory(String category) {

        String where = "";

        // if("node".equals(category)){
        // where = " where managed=1";
        // }else if("host".equals(category)){
        // where = " where managed=1 and category=4";
        // }else if("net".equals(category)){
        // where = " where managed=1 and (category=1 or category=2 or category=3
        // or category=7) ";
        // }else if("router".equals(category)){
        // where = " where managed=1 and category=1";
        // }else if("switch".equals(category)){
        // where = " where managed=1 and (category=2 or category=3 or
        // category=7) ";
        // }

        if ("node".equals(category)) {
            where = " where managed=1";
        } else if ("net_server".equals(category)) {
            where = " where managed=1 and category=4";
        } else if ("net".equals(category)) {
            where = " where managed=1 and (category=1 or category=2 or category=3 or category=7) ";
        } else if ("net_router".equals(category)) {
            where = " where managed=1 and category=1";
        } else if ("net_switch".equals(category)) {
            where = " where managed=1 and (category=2 or category=3 or category=7) ";
        } else {
            where = " where managed=1";
        }
        where = where + getBidSql();

        HostNodeDao dao = new HostNodeDao();

        String key = getParaValue("key");

        String value = getParaValue("value");
        if (key != null && key.trim().length() > 0 && value != null && value.trim().length() > 0) {
            where = where + " and " + key + "='" + value + "'";

            System.out.println(where);

        }
        list(dao, where);

        List list = (List) request.getAttribute("list");
        return list;
    }

    /**
     * �豸����б�
     * 
     * @author nielin
     * @date 2010-08-09
     */
    private String monitornodelist() {

        String category = request.getParameter("category");
        request.setAttribute("category", category);
        // SysLogger.info("category==="+category);
        List monitornodelist = getMonitorListByCategory(category);
        String jsp = "/topology/network/monitornodelist.jsp";

        Hashtable sharedata = ShareData.getSharedata();

        Hashtable allpingdata = ShareData.getPingdata();

        List list = new ArrayList();

        NumberFormat numberFormat = new DecimalFormat();
        numberFormat.setMaximumFractionDigits(0);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = simpleDateFormat.format(new Date());

        String starttime = date + " 00:00:00";
        String totime = date + " 23:59:59";

        if (monitornodelist != null) {
            for (int i = 0; i < monitornodelist.size(); i++) {
                HostNode hostNode = (HostNode) monitornodelist.get(i);
                MonitorNodeDTO monitorNodeDTO = getMonitorNodeDTOByHostNode(hostNode);

                list.add(monitorNodeDTO);
            }
        }

        String field = getParaValue("field");
        String sorttype = getParaValue("sorttype");
        if (field != null) {
            if (sorttype == null || sorttype.trim().length() == 0) {
                sorttype = "asc";
            } else if ("asc".equals(sorttype)) {
                sorttype = "desc";
            } else if ("desc".equals(sorttype)) {
                sorttype = "asc";
            }

            monitorListSort(list, "net", field, sorttype);

            request.setAttribute("field", field);
            request.setAttribute("sorttype", sorttype);
        }
        request.setAttribute("list", list);

        return jsp;

    }

    /**
     * ����������б�
     * 
     * @author nielin
     * @date 2010-08-09
     */
    private String monitorswitchlist() {
        // HostNodeDao dao = new HostNodeDao();
        // setTarget("/topology/network/monitorswitchlist.jsp");
        // request.setAttribute("actionlist", "monitorswitchlist");
        // return list(dao," where managed=1 and (category=2 or category=3 or
        // category=7) ");

        String jsp = "/topology/network/monitorswitchlist.jsp";
        setTarget(jsp);
        List monitornetlist = getMonitorListByCategory("net_switch");

        Hashtable sharedata = ShareData.getSharedata();

        Hashtable allpingdata = ShareData.getPingdata();

        List list = new ArrayList();

        NumberFormat numberFormat = new DecimalFormat();
        numberFormat.setMaximumFractionDigits(0);

        if (monitornetlist != null) {
            for (int i = 0; i < monitornetlist.size(); i++) {

                HostNode hostNode = (HostNode) monitornetlist.get(i);

                MonitorNodeDTO monitorNetDTO = getMonitorNodeDTOByHostNode(hostNode);

                list.add(monitorNetDTO);
            }
        }

        String field = getParaValue("field");
        String sorttype = getParaValue("sorttype");
        if (field != null) {
            if (sorttype == null || sorttype.trim().length() == 0) {
                sorttype = "asc";
            } else if ("asc".equals(sorttype)) {
                sorttype = "desc";
            } else if ("desc".equals(sorttype)) {
                sorttype = "asc";
            }

            monitorListSort(list, "net", field, sorttype);

            request.setAttribute("field", field);
            request.setAttribute("sorttype", sorttype);
        }
        request.setAttribute("list", list);
        return jsp;
    }

    // private String monitorroutelist()
    // {
    // HostNodeDao dao = new HostNodeDao();
    // setTarget("/topology/network/monitorroutelist.jsp");
    // return list(dao," where managed=1 and category=1");
    // }

    /**
     * ·��������б�
     * 
     * @author nielin
     * @date 2010-08-09
     */
    private String monitorroutelist() {
        // HostNodeDao dao = new HostNodeDao();
        // setTarget("/topology/network/monitorroutelist.jsp");
        // return list(dao," where managed=1 and category=1");

        String jsp = "/topology/network/monitorroutelist.jsp";
        setTarget(jsp);

        List monitornetlist = getMonitorListByCategory("net_router");

        Hashtable sharedata = ShareData.getSharedata();

        Hashtable allpingdata = ShareData.getPingdata();

        List list = new ArrayList();

        NumberFormat numberFormat = new DecimalFormat();
        numberFormat.setMaximumFractionDigits(0);

        if (monitornetlist != null) {
            for (int i = 0; i < monitornetlist.size(); i++) {

                HostNode hostNode = (HostNode) monitornetlist.get(i);

                MonitorNodeDTO monitorNetDTO = getMonitorNodeDTOByHostNode(hostNode);

                list.add(monitorNetDTO);
            }
        }
        String field = getParaValue("field");
        String sorttype = getParaValue("sorttype");
        if (field != null) {
            if (sorttype == null || sorttype.trim().length() == 0) {
                sorttype = "asc";
            } else if ("asc".equals(sorttype)) {
                sorttype = "desc";
            } else if ("desc".equals(sorttype)) {
                sorttype = "asc";
            }

            monitorListSort(list, "net", field, sorttype);

            request.setAttribute("field", field);
            request.setAttribute("sorttype", sorttype);
        }
        request.setAttribute("list", list);
        return jsp;
    }

    public String netChoce() {
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
        return "/topology/network/netChoce.jsp";
    }

    public String hostChoce() {
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
        request.setAttribute("list", dao.loadHostByFlag(1));
        return "/topology/network/hostChoce.jsp";
    }

    private String downloadnetworklistback() {
        Hashtable reporthash = new Hashtable();
        AbstractionReport1 report = new ExcelReport1(new IpResourceReport(), reporthash);

        report.createReport_networklist("/temp/networklist_report.xls");
        request.setAttribute("filename", report.getFileName());
        return "/topology/network/downloadreport.jsp";
    }

    private void collectHostData(Host node) {
        try {
            Vector vector = null;
            Hashtable hashv = null;
            Hashtable pinghash = null;// ��ͨ��
            LoadAixFile aix = null;
            LoadLinuxFile linux = null;
            LoadScoUnixWareFile scounix = null;
            LoadScoOpenServerFile scoopenserver = null;
            LoadHpUnixFile hpunix = null;
            LoadSunOSFile sununix = null;
            LoadWindowsWMIFile windowswmi = null;
            I_HostCollectData hostdataManager = new HostCollectDataManager();
            ProcessNetData porcessData = new ProcessNetData();

            // ��ȡ��ͨ������
            Date startDate = new Date();
            SysLogger.info("#######################ping --��ʼ:");
            pinghash = getPingHash(node);
            SysLogger.info("#######################ping ���ѵ�ʱ�䣨ms��---" + (new Date().getTime() - startDate.getTime()));

            I_HostLastCollectData hostlastdataManager = new HostLastCollectDataManager();
            if (node.getCollecttype() == SystemConstant.COLLECTTYPE_SHELL) {
                // SHELL��ȡ��ʽ
                try {
                    if (node.getOstype() == 6) {
                        // SysLogger.info("�ɼ�:
                        // ��ʼ�ɼ�IP��ַΪ"+node.getIpAddress()+"����ΪAIX����������������");
                        // //AIX������
                        // try{
                        // aix = new LoadAixFile(node.getIpAddress());
                        // hashv=aix.getTelnetMonitorDetail();
                        // //
                        // hostdataManager.createHostData(node.getIpAddress(),hashv);
                        // //�õ�ping������
                        // hashv.put("ping", pinghash);
                        // //
                        // hostdataManager.createHostData(node.getIpAddress(),hashv);
                        // Hashtable datahash = new Hashtable();
                        // datahash.put(node.getIpAddress(), hashv);
                        // porcessData.processHostData(datahash);
                        // }catch(Exception e){
                        // e.printStackTrace();
                        // }
                    } else if (node.getOstype() == 9) {
                        // SysLogger.info("�ɼ�:
                        // ��ʼ�ɼ�IP��ַΪ"+node.getIpAddress()+"����ΪLINUX����������������");
                        // //LINUX������
                        // try{
                        // linux = new LoadLinuxFile(node.getIpAddress());
                        // hashv=linux.getTelnetMonitorDetail();
                        // //
                        // hostdataManager.createHostData(node.getIpAddress(),hashv);
                        // //�õ�ping������
                        // hashv.put("ping", pinghash);
                        // //
                        // hostdataManager.createHostData(node.getIpAddress(),hashv);
                        // Hashtable datahash = new Hashtable();
                        // datahash.put(node.getIpAddress(), hashv);
                        // porcessData.processHostData(datahash);
                        // }catch(Exception e){
                        // e.printStackTrace();
                        // }
                    } else if (node.getOstype() == 20) {
                        SysLogger.info("�ɼ�: ��ʼ�ɼ�IP��ַΪ" + node.getIpAddress() + "����ΪSCOUNIX����������������");
                        // LINUX������
                        try {
                            // scounix = new
                            // LoadScoUnixWareFile(node.getIpAddress());
                            // hashv=scounix.getTelnetMonitorDetail();
                            // hostdataManager.createHostData(node.getIpAddress(),hashv);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if (node.getOstype() == 21) {
                        SysLogger.info("�ɼ�: ��ʼ�ɼ�IP��ַΪ" + node.getIpAddress() + "����ΪSCOOPENSERVER����������������");
                        // LINUX������
                        try {
                            // scoopenserver = new
                            // LoadScoOpenServerFile(node.getIpAddress());
                            // hashv=scoopenserver.getTelnetMonitorDetail();
                            // hostdataManager.createHostData(node.getIpAddress(),hashv);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if (node.getOstype() == 7) {
                        // SysLogger.info("�ɼ�:
                        // ��ʼ�ɼ�IP��ַΪ"+node.getIpAddress()+"����ΪHPUNIX����������������");
                        // //HPUNIX������
                        // try{
                        // hpunix = new LoadHpUnixFile(node.getIpAddress());
                        // hashv=hpunix.getTelnetMonitorDetail();
                        // //
                        // hostdataManager.createHostData(node.getIpAddress(),hashv);
                        // //�õ�ping������
                        // hashv.put("ping", pinghash);
                        // //
                        // hostdataManager.createHostData(node.getIpAddress(),hashv);
                        // Hashtable datahash = new Hashtable();
                        // datahash.put(node.getIpAddress(), hashv);
                        // porcessData.processHostData(datahash);
                        // }catch(Exception e){
                        // e.printStackTrace();
                        // }
                    } else if (node.getOstype() == 8) {
                        // SysLogger.info("�ɼ�:
                        // ��ʼ�ɼ�IP��ַΪ"+node.getIpAddress()+"����ΪSOLARIS����������������");
                        // //SOLARIS������
                        // try{
                        // sununix = new LoadSunOSFile(node.getIpAddress());
                        // hashv=sununix.getTelnetMonitorDetail();
                        // //
                        // hostdataManager.createHostData(node.getIpAddress(),hashv);
                        // //�õ�ping������
                        // hashv.put("ping", pinghash);
                        // //
                        // hostdataManager.createHostData(node.getIpAddress(),hashv);
                        // Hashtable datahash = new Hashtable();
                        // datahash.put(node.getIpAddress(), hashv);
                        // porcessData.processHostData(datahash);
                        // }catch(Exception e){
                        // e.printStackTrace();
                        // }
                    } else if (node.getOstype() == 5) {
                        SysLogger.info("�ɼ�: ��ʼ��WMI��ʽ�ɼ�IP��ַΪ" + node.getIpAddress() + "����ΪWINDOWS����������������");
                        try {
                            windowswmi = new LoadWindowsWMIFile(node.getIpAddress());
                            hashv = windowswmi.getTelnetMonitorDetail();
                            hostdataManager.createHostData(node.getIpAddress(), hashv);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                aix = null;
                hashv = null;
            }

            else if (node.getCollecttype() == SystemConstant.COLLECTTYPE_WMI) {
                // WINDOWS�µ�WMI�ɼ���ʽ
                SysLogger.info("�ɼ�: ��ʼ��WMI��ʽ�ɼ�IP��ַΪ" + node.getIpAddress() + "����ΪWINDOWS����������������");
                try {
                    windowswmi = new LoadWindowsWMIFile(node.getIpAddress());
                    hashv = windowswmi.getTelnetMonitorDetail();
                    hostdataManager.createHostData(node.getIpAddress(), hashv);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                aix = null;
                hashv = null;
            }

            else {
                // SNMP�ɼ���ʽ
                HostNode hostnode = new HostNode();
                // Host host = new Host();
                hostnode.setId(node.getId());
                hostnode.setSysName(node.getSysName());
                hostnode.setCategory(node.getCategory());
                hostnode.setCommunity(node.getCommunity());
                // hostnode.setWritecommunity(node.getWritecommunity());
                hostnode.setSnmpversion(node.getSnmpversion());
                hostnode.setIpAddress(node.getIpAddress());
                hostnode.setLocalNet(node.getLocalNet());
                hostnode.setNetMask(node.getNetMask());
                hostnode.setAlias(node.getAlias());
                hostnode.setSysDescr(node.getSysDescr());
                hostnode.setSysOid(node.getSysOid());
                hostnode.setType(node.getType());
                hostnode.setManaged(node.isManaged());
                hostnode.setOstype(node.getOstype());
                hostnode.setCollecttype(node.getCollecttype());
                hostnode.setSysLocation(node.getSysLocation());
                hostnode.setSendemail(node.getSendemail());
                hostnode.setSendmobiles(node.getSendmobiles());
                hostnode.setSendphone(node.getSendphone());
                hostnode.setBid(node.getBid());
                hostnode.setEndpoint(node.getEndpoint());
                hostnode.setStatus(0);
                hostnode.setSupperid(node.getSupperid());

                try {
                    NodeGatherIndicatorsDao indicatorsdao = new NodeGatherIndicatorsDao();
                    List<NodeGatherIndicators> monitorItemList = new ArrayList<NodeGatherIndicators>();
                    try {
                        // ��ȡ�����õ����б�����ָ��
                        monitorItemList = indicatorsdao.findByNodeIdAndTypeAndSubtype(hostnode.getId() + "", "host", "");
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        indicatorsdao.close();
                    }
                    if (monitorItemList != null && monitorItemList.size() > 0) {
                        for (int i = 0; i < monitorItemList.size(); i++) {
                            NodeGatherIndicators nodeGatherIndicators = (NodeGatherIndicators) monitorItemList.get(i);
                            PollDataUtil polldatautil = new PollDataUtil();
                            polldatautil.collectHostData(nodeGatherIndicators);
                        }
                    }
                } catch (Exception e) {

                }

                // if(node.getSysOid().equalsIgnoreCase("1.3.6.1.4.1.311.1.1.3.1.1")
                // ||
                // node.getSysOid().equalsIgnoreCase("1.3.6.1.4.1.311.1.1.3.1.2")||
                // node.getSysOid().equalsIgnoreCase("1.3.6.1.4.1.311.1.1.3.1.3")||
                // node.getSysOid().equalsIgnoreCase("1.3.6.1.4.1.311.1.1.3.1")){
                // SysLogger.info("�ɼ�:
                // ��ʼ�ɼ�IP��ַΪ"+node.getIpAddress()+"����ΪWINDOWS����������������");
                // //windows
                // WindowsSnmp windows=new WindowsSnmp();
                // try{
                // hashv=windows.collect_Data(hostnode);
                // hostdataManager.createHostData(node.getIpAddress(),hashv);
                // }catch(Exception ex){
                // ex.printStackTrace();
                // }
                // windows=null;
                // vector=null;
                //                   		
                // }else if(node.getOstype() == 9){
                // if(node.getCollecttype() == 1){
                // //System.out.println("==================linux================");
                // LinuxSnmp linuxSnmp = new LinuxSnmp();
                // hashv = linuxSnmp.collect_Data(hostnode);
                // //System.out.println("==================linux SNMP
                // end================");
                // hostdataManager.createHostData(node.getIpAddress(),hashv);
                // }else{
                //       						
                // }
                // }
            }
        } catch (Exception exc) {

        }
    }

    /**
     * ��ȡ��ͨ��
     * 
     * @param node
     * @return
     */
    private Hashtable getPingHash(Host node) {
        Hashtable returnHash = null;
        try {
            NodeGatherIndicatorsDao indicatorsdao = new NodeGatherIndicatorsDao();
            List<NodeGatherIndicators> monitorItemList = new ArrayList<NodeGatherIndicators>();
            try {
                // ��ȡ�����õ����б�����ָ��
                monitorItemList = indicatorsdao.findByNodeIdAndTypeAndSubtype(node.getId() + "", "host", "");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                indicatorsdao.close();
            }
            if (monitorItemList != null && monitorItemList.size() > 0) {
                for (int i = 0; i < monitorItemList.size(); i++) {
                    NodeGatherIndicators nodeGatherIndicators = (NodeGatherIndicators) monitorItemList.get(i);
                    // Host node =
                    // (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(nodeGatherIndicatorsNode.getNodeid()));
                    if (node != null && nodeGatherIndicators.getName().equalsIgnoreCase("ping")) {
                        PingSnmp pingsnmp = null;
                        try {
                            pingsnmp = (PingSnmp) Class.forName("com.afunms.polling.snmp.ping.PingSnmp").newInstance();
                            returnHash = pingsnmp.collect_Data(nodeGatherIndicators);
                            // �ڲɼ��������Ѿ��������ݿ�
                            // hostdataManager.createHostItemData(node.getIpAddress(),returnHash,alarmIndicatorsNode.getType(),alarmIndicatorsNode.getSubtype(),"utilhdx");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnHash;
    }

    /**
     * 
     * ���� oids ���ж�������
     * 
     * @param oids
     *            �豸oids
     * @return ����������
     */
    public String getSutType(String oids) {
        String subtype = "";
        if (oids.startsWith("1.3.6.1.4.1.311.")) {
            subtype = "windows";
        } else if (oids.startsWith("1.3.6.1.4.1.2021") || oids.startsWith("1.3.6.1.4.1.8072")) {
            subtype = "linux";
        } else if (oids.startsWith("as400")) {
            subtype = "as400";

        } else if (oids.startsWith("1.3.6.1.4.1.42.2.1.1")) {
            subtype = "solaris";
        } else if (oids.startsWith("1.3.6.1.4.1.2.3.1.2.1.1")) {
            subtype = "aix";
        } else if (oids.startsWith("1.3.6.1.4.1.11.2.3.10.1")) {
            subtype = "hpunix";
        } else if (oids.startsWith("1.3.6.1.4.1.11.2.3.7.11")) {
            subtype = "hp";
        } else if (oids.startsWith("1.3.6.1.4.1.9.")) {
            subtype = "cisco";
        } else if (oids.startsWith("1.3.6.1.4.1.25506.") || oids.startsWith("1.3.6.1.4.1.2011.")) {
            subtype = "h3c";
        } else if (oids.startsWith("1.3.6.1.4.1.4881.")) {
            subtype = "redgiant";
        } else if (oids.startsWith("1.3.6.1.4.1.5651.")) {
            subtype = "maipu";
        } else if (oids.startsWith("1.3.6.1.4.1.171.")) {
            subtype = "dlink";
        } else if (oids.startsWith("1.3.6.1.4.1.2272.")) {
            subtype = "northtel";
        } else if (oids.startsWith("1.3.6.1.4.1.89.")) {
            subtype = "radware";
        } else if (oids.startsWith("1.3.6.1.4.1.3320.")) {
            subtype = "bdcom";
        } else if (oids.startsWith("1.3.6.1.4.1.1588.2.1.")) {
            subtype = "brocade";
        } else if (oids.startsWith("1.3.6.1.4.1.3902.")) {
            subtype = "zte";
        } else if (oids.startsWith("1.3.6.1.4.1.116.")) {
            subtype = "hds";
        } else if (oids.startsWith("1.3.6.1.4.1.14331.")) {
            // �����ŷ���ǽ
            subtype = "topsec";
        } else if (oids.startsWith("1.3.6.1.4.1.800.")) {
            // Alcatel
            subtype = "alcatel";
        } else if (oids.startsWith("1.3.6.1.4.1.45.")) {
            // Avaya
            subtype = "avaya";
        } else if (oids.startsWith("1.3.6.1.4.1.6876.")) {
            // VMWare
            subtype = "vmware";
        } else if (oids.startsWith("1.3.6.1.4.1.1981.1")) {
            subtype = "emc_vnx";
        } else if (oids.startsWith("1.3.6.1.4.1.1981.2")) {
            subtype = "emc_dmx";
        } else if (oids.startsWith("1.3.6.1.4.1.1981.3")) {
            subtype = "emc_vmax";
        } else if (oids.startsWith("1.3.6.1.4.1.2636.")) {
            subtype = "juniper";
        } else if (oids.startsWith("1.3.6.1.4.1.3224.")) {
            subtype = "checkpoint";
        } else if (oids.startsWith("1.3.6.1.4.1.789.")) {
            subtype = "netapp";
        } else if (oids.startsWith("1.3.6.1.4.1.476.1.42") || oids.startsWith("1.3.6.1.4.1.13400.2.1")) {
            subtype = "emerson";
        }

        return subtype;
    }

    public List readXls(String fileName) {
        List list = new ArrayList();
        HostNode hostnode = new HostNode();
        String str = null;
        try {
            File file = new File(fileName);
            // SysLogger.info("file========"+fileName);
            if (file.exists()) {
                Workbook book = Workbook.getWorkbook(new File(fileName));
                Sheet rs = book.getSheet(0);
                int rows = rs.getRows();// ����
                int cols = rs.getColumns();// ����
                String[] value = new String[rows * cols];
                for (int row = 2; row < rows; row++) {// ��������һ��
                    hostnode = new HostNode();
                    for (int c = 0; c < cols; c++) {// ÿһ��
                        if (c == 1) {// 1��2��9��10��11��
                            str = rs.getCell(c, row).getContents().trim();
                            SysLogger.info("����========" + str);
                            hostnode.setAlias(str);

                        } else if (c == 2) {
                            str = rs.getCell(c, row).getContents().trim();
                            SysLogger.info("IP========" + str);
                            hostnode.setIpAddress(str);
                        } else if (c == 7) {
                            str = rs.getCell(c, row).getContents().trim();
                            SysLogger.info("�Ƿ����========" + str);
                            if ("��".equalsIgnoreCase(str)) {
                                hostnode.setManaged(true);
                            } else {
                                hostnode.setManaged(false);
                            }
                        } else if (c == 8) {
                            str = rs.getCell(c, row).getContents().trim();
                            if ("��".equalsIgnoreCase(str)) {
                                hostnode.setEndpoint(1);
                            } else {
                                hostnode.setEndpoint(0);
                            }
                        } else if (c == 9) {
                            str = rs.getCell(c, row).getContents().trim();
                            int temp = Integer.parseInt(str);
                            SysLogger.info("�ɼ���ʽ========" + str);
                            hostnode.setCollecttype(temp);

                        } else if (c == 10) {
                            str = rs.getCell(c, row).getContents().trim();
                            int temp = Integer.parseInt(str);
                            SysLogger.info("SNMP�汾========" + str);
                            hostnode.setSnmpversion(temp);
                        } else if (c == 11) {
                            str = rs.getCell(c, row).getContents().trim();
                            // String comm = Integer.parseInt(str);
                            SysLogger.info("SNMP����������========" + str);
                            hostnode.setCommunity(str);
                        } else if (c == 13) {
                            str = rs.getCell(c, row).getContents().trim();
                            // String comm = Integer.parseInt(str);
                            int temp = Integer.parseInt(str);
                            SysLogger.info("����========" + str);
                            hostnode.setCategory(temp);
                        } else if (c == 14) {
                            str = rs.getCell(c, row).getContents().trim();
                            // String comm = Integer.parseInt(str);
                            int temp = Integer.parseInt(str);
                            SysLogger.info("��������========" + str);
                            hostnode.setOstype(temp);
                        } else if (c == 15) {
                            str = rs.getCell(c, row).getContents().trim();
                            // String comm = Integer.parseInt(str);
                            SysLogger.info("ҵ��ϵͳID========" + str);
                            hostnode.setBid(str);
                        }
                    }
                    hostnode.setDiscovertatus(-1);
                    list.add(hostnode);

                }
                book.close();
            }
        } catch (BiffException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static void main(String[] args) {
        // autilhdxh
    }

}