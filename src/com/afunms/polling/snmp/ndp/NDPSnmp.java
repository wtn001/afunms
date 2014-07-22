package com.afunms.polling.snmp.ndp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import com.afunms.common.util.ShareData;
import com.afunms.common.util.SnmpUtil;
import com.afunms.common.util.SnmpUtils;
import com.afunms.common.util.SysLogger;
import com.afunms.config.dao.IpAliasDao;
import com.afunms.config.model.IpAlias;
import com.afunms.discovery.IfEntity;
import com.afunms.indicators.model.NodeGatherIndicators;
import com.afunms.monitor.executor.base.SnmpMonitor;
import com.afunms.monitor.item.base.MonitoredItem;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.base.LinkRoad;
import com.afunms.polling.base.Node;
import com.afunms.polling.node.Host;
import com.afunms.polling.om.Interfacecollectdata;
import com.afunms.polling.om.IpMac;
import com.afunms.polling.om.NDP;
import com.afunms.polling.task.ThreadPool;
import com.afunms.topology.dao.HostInterfaceDao;
import com.afunms.topology.dao.HostNodeDao;
import com.afunms.topology.dao.LinkDao;
import com.afunms.topology.dao.NDPDao;
import com.afunms.topology.model.HostNode;
import com.afunms.topology.model.InterfaceNode;
import com.afunms.topology.model.Link;
import com.afunms.topology.util.TopoHelper;
import com.afunms.topology.util.XmlOperator;
import com.gatherResulttosql.NetHostNDPRttosql;

public class NDPSnmp extends SnmpMonitor {
    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public NDPSnmp() {
    }

    public void collectData(Node node, MonitoredItem item) {

    }

    public void collectData(HostNode node) {

    }

    public Hashtable collect_Data(NodeGatherIndicators alarmIndicatorsNode) {

        Hashtable returnHash = new Hashtable();
        Vector ndpVector = new Vector();
        Host node = (Host) PollingEngine.getInstance().getNodeByID(Integer.parseInt(alarmIndicatorsNode.getNodeid()));
        if (node == null)
            return returnHash;
//        SysLogger.info("######## " + node.getIpAddress() + " ��ʼ�ɼ�NDP��Ϣ##########");
        
        int layer = node.getLayer();

        //KEY:deviceid|portname VALUE:ndp
        Hashtable formerNDP = new Hashtable();
        
        //KEY:mac VALUE:ipaddress
        Hashtable ipmacHash = new Hashtable(); 

        List nodeMacList = new ArrayList();
        if (node.getMac().contains("|")) {
            String[] macs = node.getMac().split("|");
            for (int k = 0; k < macs.length; k++) {
                nodeMacList.add(macs[k]);
            }
        } else {
            nodeMacList.add(node.getMac());
        }

        try {
            Calendar date = Calendar.getInstance();
            if (ShareData.getSharedata().get(node.getIpAddress()) != null) {
                if (((Hashtable) ShareData.getSharedata().get(node.getIpAddress())).containsKey("ndp")) {
                    Vector former_ndpVector = (Vector) ((Hashtable) ShareData.getSharedata().get(node.getIpAddress())).get("ndp");
                    if (former_ndpVector != null && former_ndpVector.size() > 0) {
                        NDP ndp = null;
                        for (int i = 0; i < former_ndpVector.size(); i++) {
                            ndp = (NDP) former_ndpVector.get(i);
                            formerNDP.put(ndp.getDeviceId() + "|" + ndp.getPortName(), ndp);
                        }
                    }
                    Vector ipmacVector = (Vector) ((Hashtable) ShareData.getSharedata().get(node.getIpAddress())).get("ipmac");
                    if (ipmacVector != null && ipmacVector.size() > 0) {
                        IpMac ipmac = null;
                        for (int i = 0; i < ipmacVector.size(); i++) {
                            ipmac = (IpMac) ipmacVector.get(i);
                            ipmacHash.put(ipmac.getMac(), ipmac.getIpaddress());
                        }
                    }
                }
            }
            Hashtable ipAllData = (Hashtable) ShareData.getSharedata().get(node.getIpAddress());
            if (ipAllData == null)
                ipAllData = new Hashtable();

            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                com.afunms.polling.base.Node snmpnode = (com.afunms.polling.base.Node) PollingEngine.getInstance().getNodeByIP(node.getIpAddress());
                Date cc = date.getTime();
                String time = sdf.format(cc);
                snmpnode.setLastTime(time);
            } catch (Exception e) {

            }
            // ---------------------------------------------------�õ�����NDP start
            try {
                String[] oids = new String[] { 
                		"1.3.6.1.4.1.2011.6.7.5.6.1.1", // 1.hwNDPPortNbDeviceId
                        "1.3.6.1.4.1.2011.6.7.5.6.1.2", // 2.hwNDPPortNbPortName
                };

//                String[] oids1 = new String[] { 
//                		"1.3.6.1.4.1.25506.8.7.5.6.1.1", // 1.hwNDPPortNbDeviceId
//                        "1.3.6.1.4.1.25506.8.7.5.6.1.2", // 2.hwNDPPortNbPortName
//                };
                String[][] valueArray = null; 
                try {
                    valueArray = SnmpUtils.walkTable(node.getIpAddress(), node.getCommunity(), oids, node.getSnmpversion(), node.getSecuritylevel(), node.getSecurityName(), node.getV3_ap(), node.getAuthpassphrase(), node.getV3_privacy(), node.getPrivacyPassphrase(), 3, 1000 * 30);
                } catch (Exception e) {
                    valueArray = null;
                }
                if (valueArray.length == 1) {
                    if (valueArray[0][0] == null || valueArray[0][1] == null) {
                        try {
                            valueArray = SnmpUtils.walkTable(node.getIpAddress(), node.getCommunity(), oids, node.getSnmpversion(), node.getSecuritylevel(), node.getSecurityName(), node.getV3_ap(), node.getAuthpassphrase(), node.getV3_privacy(), node.getPrivacyPassphrase(), 3, 1000 * 30);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                if (valueArray == null) {
                    try {
                        valueArray = SnmpUtils.walkTable(node.getIpAddress(), node.getCommunity(), oids, node.getSnmpversion(), node.getSecuritylevel(), node.getSecurityName(), node.getV3_ap(), node.getAuthpassphrase(), node.getV3_privacy(), node.getPrivacyPassphrase(), 3, 1000 * 30);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (valueArray == null)
                    return null;
                NDP ndp = null;
                for (int i = 0; i < valueArray.length; i++) {
                    ndp = new NDP();
                    ndp.setDeviceId(valueArray[i][0]);
                    ndp.setPortName(valueArray[i][1]);
                    ndp.setNodeid(Long.parseLong(node.getId() + ""));
                    ndp.setCollecttime(date);
                    ndpVector.addElement(ndp);
                   //SysLogger.info(node.getIpAddress() + "   deviceid:" + ndp.getDeviceId() + "   portname:" + ndp.getPortName());
                }
                valueArray = null;
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
        }
        returnHash.put("ndp", ndpVector);
        if (!(ShareData.getSharedata().containsKey(node.getIpAddress()))) {
            Hashtable ipAllData = new Hashtable();
            if (ipAllData == null)
                ipAllData = new Hashtable();
            if (ndpVector != null && ndpVector.size() > 0)
                ipAllData.put("ndp", ndpVector);
            ShareData.getSharedata().put(node.getIpAddress(), ipAllData);
        } else {
            if (ndpVector != null && ndpVector.size() > 0)
                ((Hashtable) ShareData.getSharedata().get(node.getIpAddress())).put("ndp", ndpVector);

        }

      //KEY:mac VALUE:ipaddress ��ŵ���MAC��IP��Ӧ�Ĺ�ϵ
        Hashtable nodehash = new Hashtable();
        
        Hashtable nodelistHash = new Hashtable();
        try {
        	List hostlist = PollingEngine.getInstance().getNodeList();
            if (hostlist != null && hostlist.size() > 0) {
                for (int i = 0; i < hostlist.size(); i++) {
                	Host _node = (Host) hostlist.get(i);
                    if (_node.getMac () != null && _node.getMac().trim().length() > 0) {
                        if (_node.getMac().contains("|")) {

                            String[] macs = _node.getMac().split("|");
                            for (int k = 0; k < macs.length; k++) {                         
                                nodehash.put(macs[k], _node.getIpAddress());
                            }
                        } else {
                            nodehash.put(_node.getMac(), _node.getIpAddress());
                        }
                    }
                    //SysLogger.info("====put "+_node.getIpAddress()+" to nodelistHash ====");
                    nodelistHash.put(_node.getIpAddress(), _node.getIpAddress());

                }
            }
        } catch (Exception e) {

        } finally {
//            hostNodeDao.close();
        }

        Vector newNDP = new Vector();
        if (ndpVector != null && ndpVector.size() > 0) {
            NDPDao ndpdao = new NDPDao();
            List ndplistdb = new ArrayList();
            //���MAC:�˿�������NDP�Ķ��ձ�
            //KEY=MAC:�˿����ƣ�VALUE=ndp
            Hashtable ndpFromDbHash = new Hashtable();
            try {
            	//�����ݿ���װ����ʷNDP��Ϣ
                ndplistdb = ndpdao.getbynodeid(Long.parseLong(node.getId() + ""));
                if (ndplistdb != null && ndplistdb.size() > 0) {
                    NDP ndp = null;
                    for (int i = 0; i < ndplistdb.size(); i++) {
                        ndp = (NDP) ndplistdb.get(i);
                        ndpFromDbHash.put(ndp.getDeviceId() + "|" + ndp.getPortName(), ndp);
                    }
                }
            } catch (Exception e) {

            } finally {
                ndpdao.close();
            }

            IpAliasDao ipaliasdao = new IpAliasDao();
            Hashtable ipaliasHash = new Hashtable();
            try {
                List aliasList = ipaliasdao.loadAll();
                if (aliasList != null && aliasList.size() > 0) {
                    for (int k = 0; k < aliasList.size(); k++) {
                        IpAlias vo = (IpAlias) aliasList.get(k);
                        //SysLogger.info("#### put "+vo.getAliasip() +" to ipaliasHash ####");
                        ipaliasHash.put(vo.getAliasip(), vo.getIpaddress());
                    }
                }
            } catch (Exception e) {

            } finally {
                ipaliasdao.close();
            }

            NDP ndp = null;
            for (int i = 0; i < ndpVector.size(); i++) {
                ndp = (NDP) ndpVector.get(i);

//                SysLogger.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@");
//                SysLogger.info("ip:" + node.getIpAddress() + " ### " + ndp.getDeviceId() + "====" + ndp.getPortName());
//                SysLogger.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@");

                if (!formerNDP.containsKey(ndp.getDeviceId() + "|" + ndp.getPortName())) {
                	//ϵͳ�ڵ�һ������ʱ�򣬺ܶ��豸����û�н���NDP�ɼ�����������»���������
//                    SysLogger.info(" ### �����µ�NDP��Ϣ��" + ndp.getDeviceId() + "  " + ndp.getPortName() + " ###");
                    // �ҵ��µ�NDP��Ϣ���豸�п������¼ӵ�
                    //ϵͳ�ڵ�һ������ʱ��,nodehash�п��ܻ������ڸ�MAC��ַ
                    if (nodehash.containsKey(ndp.getDeviceId())) {
                        // �Ѿ���ϵͳ���ڸ��豸,ֻ����NDP��
                        newNDP.add(ndp);
                        
                        //������·��ϵ ��ʼ
                        String ipaddress = (String)nodehash.get(ndp.getDeviceId()); //nodehash===>key:mac value:ip
                        Host _host = (Host) PollingEngine.getInstance().getNodeByIp(ipaddress);
                        if(_host == null)continue;
                        Hashtable portDescHash = new Hashtable();
                        if(ShareData.getSharedata() != null && ShareData.getSharedata().containsKey(ipaddress)){
                        	Vector interfaceVector = null;
                        	if (((Hashtable) ShareData.getSharedata().get(ipaddress)).containsKey("interface")) {
                                interfaceVector = (Vector) ((Hashtable) ShareData.getSharedata().get(ipaddress)).get("interface");
                                if(interfaceVector != null && interfaceVector.size()>0){
                                	Interfacecollectdata interfaceCollectData = null;
                                	for(int m=0;m<interfaceVector.size();m++){
                                		interfaceCollectData = (Interfacecollectdata) interfaceVector.get(m);
                                		if("ifDescr".equalsIgnoreCase(interfaceCollectData.getEntity())){
                                			//�˿�����:�˿�����
                                			portDescHash.put(interfaceCollectData.getThevalue(), interfaceCollectData.getSubentity());
                                		}
                                	}
                                }
                        	}
                        	if (((Hashtable) ShareData.getSharedata().get(ipaddress)).containsKey("ndp")) {
                                Vector _ndpVector = (Vector) ((Hashtable) ShareData.getSharedata().get(ipaddress)).get("ndp");
                                if(_ndpVector != null && _ndpVector.size()>0){
                                	NDP _ndp = null;
                                	//IfEntity endIfEntity = _host.getIfEntityByIndex(ifIndex).getIfEntityByDesc(ndp.getPortName());
                                	com.afunms.polling.node.IfEntity endIfEntity = null;
                                	if(portDescHash.containsKey(ndp.getPortName())){
                                		//���ڸĶ˿�����
                                		endIfEntity = _host.getIfEntityByIndex((String)portDescHash.get(ndp.getPortName()));
                                	}
                                    IfEntity startIfEntity = null;
                                    HostInterfaceDao ifdao = new HostInterfaceDao();
                                    List iflist = null;
                                    Hashtable ifhash = new Hashtable();
                                    try{
                                    	iflist = ifdao.loadAll();
                                    	if(iflist != null && iflist.size()>0){
                                    		InterfaceNode vo = null;
                                    		for(int n=0;n<iflist.size();n++){
                                    			vo = (InterfaceNode)iflist.get(n);
                                    			ifhash.put(vo.getNode_id()+":"+vo.getDescr(), vo);
                                    		}
                                    	}
                                    }catch(Exception e){
                                    	e.printStackTrace();
                                    }finally{
                                    	ifdao.close();
                                    }                                  
                                    if(endIfEntity != null){
                                    	//�����ڶԶ˽ӿ�
                                    	InterfaceNode vo = null;
                                    	for (int k = 0; k < _ndpVector.size(); k++) {
                                            _ndp = (NDP) _ndpVector.get(k);
//                                            SysLogger.info(_host.getIpAddress() + "  ---  " + _ndp.getDeviceId() + "  ---  " + _ndp.getPortName());
                                            if (nodeMacList.contains(_ndp.getDeviceId())) {
                                            	if(ifhash.containsKey(node.getId()+":"+_ndp.getPortName())){
                                            		vo = (InterfaceNode)ifhash.get(node.getId()+":"+_ndp.getPortName());
                                            		if(vo != null){
                                            			Link link = new Link();
                                                        link.setStartId(node.getId());
                                                        link.setStartIndex(vo.getEntity());
                                                        link.setStartIp(vo.getIp_address() == null ? "" : vo.getIp_address());
                                                        link.setStartDescr(vo.getDescr());

                                                        link.setEndId(_host.getId());
                                                        link.setEndIndex(endIfEntity.getIndex());
                                                        link.setEndIp(endIfEntity.getIpAddress() == null ? "" : endIfEntity.getIpAddress());
                                                        link.setEndDescr(endIfEntity.getDescr());

                                                        link.setMaxSpeed("200000");
                                                        link.setMaxPer("50");
                                                        link.setLinktype(1);
                                                        link.setType(1);
                                                        link.setFindtype(1);
                                                        link.setLinkName(node.getIpAddress() + "/" + link.getStartDescr() + "-" + _host.getIpAddress() + "/" + link.getEndDescr());
                                                        
                                                        if(link.getStartId() == link.getEndId())continue;
                                                        LinkDao linkdao = new LinkDao();
                                                        try {
                                                        	List linklist = PollingEngine.getInstance().getLinkList();
                                                            Hashtable existLinkHash = new Hashtable();
                                                            Hashtable existEndLinkHash = new Hashtable();
                                                            if(linklist != null && linklist.size()>0){
                                                            	LinkRoad lr = null;
                                                            	
                                                            	for(int p=0;p<linklist.size();p++){
                                                            		lr = (LinkRoad)linklist.get(p);
                                                            		existLinkHash.put(lr.getStartId()+":"+lr.getStartIndex()+":"+lr.getEndId()+":"+lr.getEndIndex(), lr);
                                                            		existEndLinkHash.put(lr.getEndId()+":"+lr.getEndIndex()+":"+lr.getStartId()+":"+lr.getStartIndex(), lr);
                                                            	}
                                                            }
                                                        	if(!existLinkHash.containsKey(link.getStartId()+":"+link.getStartIndex()+":"+link.getEndId()+":"+link.getEndIndex())
                                                            		&& !existLinkHash.containsKey(link.getEndId()+":"+link.getEndIndex()+":"+link.getStartId()+":"+link.getStartIndex())
                                                            		&& !existEndLinkHash.containsKey(link.getStartId()+":"+link.getStartIndex()+":"+link.getEndId()+":"+link.getEndIndex())
                                                            		&& !existEndLinkHash.containsKey(link.getEndId()+":"+link.getEndIndex()+":"+link.getStartId()+":"+link.getStartIndex())){
                                                            	//
                                                            	linkdao.save(link);

                                                                XmlOperator xopr = new XmlOperator();
                                                                xopr.setFile("network.jsp");
                                                                xopr.init4updateXml();
                                                                xopr.addLine(link.getLinkName(), String.valueOf(link.getId()), "net" + String.valueOf(link.getStartId()), "net" + String.valueOf(link.getEndId()));
                                                                xopr.writeXml();

                                                                // ��·��Ϣʵʱ����
                                                                LinkRoad lr = new LinkRoad();
                                                                lr.setId(link.getId());
                                                                lr.setLinkName(link.getLinkName());
                                                                lr.setLinkName(link.getLinkName());// yangjun
                                                                // add
                                                                lr.setMaxSpeed(link.getMaxSpeed());// yangjun
                                                                // add
                                                                lr.setMaxPer(link.getMaxPer());// yangjun
                                                                // add
                                                                lr.setStartId(link.getStartId());
                                                                lr.setStartIp(link.getStartIp());
                                                                lr.setStartIndex(link.getStartIndex());
                                                                lr.setStartDescr(link.getStartDescr());
                                                                lr.setEndIp(link.getEndIp());
                                                                lr.setEndId(link.getEndId());
                                                                lr.setEndIndex(link.getEndIndex());
                                                                lr.setEndDescr(link.getEndDescr());
                                                                lr.setAssistant(link.getAssistant());
                                                                lr.setType(link.getType());
                                                                lr.setShowinterf(link.getShowinterf());
                                                                PollingEngine.getInstance().getLinkList().add(lr);
                                                            }
                                                        	
                                                        	
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        } finally {
                                                            linkdao.close();
                                                        }

                                                        break;
                                            		}
                                            	}
                                            }
                                        }
                                    }
                                	
                                }
                        	}
                        }
                        
                    } else {
                        // ϵͳ�����ڸ��豸
                    	//ϵͳ�ڵ�һ������ʱ��,nodehash�п��ܻ������ڸ�MAC��ַ,����������
//                        SysLogger.info("ndp.getDeviceId()======" + ndp.getDeviceId());
                        //ϵͳ�ڵ�һ������ʱ��,ipmacHash�п��ܻ������ڸ�MAC��ַ
                        if (ipmacHash.containsKey(ndp.getDeviceId())) {
                            // �ҵ���NDP��MAC��ַ��Ӧ��IP
                            String ip = (String) ipmacHash.get(ndp.getDeviceId());
                            // �����IP��ַ����SNMP���
//                            SysLogger.info(ip + "     �����IP��ַ����SNMP���1");
                            
                            if(ipaliasHash.containsKey(ip)){
                            	//�Ѿ����ڸ��豸
                            	String nodeIp = "";
                                if(nodelistHash.containsKey(ip))nodeIp = (String)nodelistHash.get(ip);
                                if(nodeIp == null || nodeIp.trim().length()==0){
                                	if(ipaliasHash.containsKey(ip))nodeIp = (String)ipaliasHash.get(ip);
                                }
                                Host _host = null;
                                if(nodeIp != null && nodeIp.trim().length()>0){
                                	_host = (Host) PollingEngine.getInstance().getNodeByIp(nodeIp);
                                	if(_host != null){
                                		//ֻ������·��ϵ
                                		continue;
                                	}
                                }
                            }
                            
                            try {
                                // addNode(ip,node);
                            	
                            	List linklist = PollingEngine.getInstance().getLinkList();
                                Hashtable existLinkHash = new Hashtable();
                                Hashtable existEndLinkHash = new Hashtable();
                                if(linklist != null && linklist.size()>0){
                                	LinkRoad lr = null;
                                	
                                	for(int p=0;p<linklist.size();p++){
                                		lr = (LinkRoad)linklist.get(p);
                                		existLinkHash.put(lr.getStartId()+":"+lr.getStartIndex()+":"+lr.getEndId()+":"+lr.getEndIndex(), lr);
                                		existEndLinkHash.put(lr.getEndId()+":"+lr.getEndIndex()+":"+lr.getStartId()+":"+lr.getStartIndex(), lr);
                                		
                                	}
                                }
                                
                                TopoHelper helper = new TopoHelper(); // �����������ݿ�͸����ڴ�
                                int addResult = 0;
                                addResult = helper.addHost(node.getAssetid(), node.getLocation(), ip, ip, node.getSnmpversion(), node.getCommunity(), node.getWritecommunity(), node.getTransfer(), 2, node.getOstype(), 1, node.getBid(), node.getSendmobiles(), node.getSendemail(), node.getSendphone(), node.getSupperid(), true, node.getSecuritylevel(), node.getSecurityName(), node.getV3_ap(), node.getAuthpassphrase(), node.getV3_privacy(), node.getPrivacyPassphrase(),layer+1);

                                // ������·
                                NDPSingleSnmp ndpsnmp = null;
                                com.afunms.discovery.Host host = null;
                                NodeGatherIndicators _alarmIndicatorsNode = new NodeGatherIndicators();
                                try {
                                    host = helper.getHost();

                                    HostNodeDao nodedao = new HostNodeDao();
                                    try {
                                        nodedao.addNodeByNDP(host, addResult);
                                        // �����豸
                                        XmlOperator opr = new XmlOperator();
                                        opr.setFile("network.jsp");
                                        opr.init4updateXml();
                                        opr.addNode(helper.getHost());
                                        opr.writeXml();
                                    } catch (Exception e) {

                                    } finally {
                                        nodedao.close();
                                    }

                                    _alarmIndicatorsNode.setNodeid(host.getId() + "");
                                    ndpsnmp = (NDPSingleSnmp) Class.forName("com.afunms.polling.snmp.ndp.NDPSingleSnmp").newInstance();
                                    returnHash = ndpsnmp.collect_Data(_alarmIndicatorsNode);
                                    IfEntity endIfEntity = host.getIfEntityByDesc(ndp.getPortName());
                                    IfEntity startIfEntity = null;
                                    if (returnHash != null && returnHash.containsKey("ndp")) {
                                        Vector _ndpVector = (Vector) returnHash.get("ndp");
                                        if (_ndpVector != null && _ndpVector.size() > 0) {
                                            NDP _ndp = null;
                                            for (int k = 0; k < _ndpVector.size(); k++) {
                                                _ndp = (NDP) _ndpVector.get(k);
                                                //SysLogger.info(host.getIpAddress() + "   ---    " + _ndp.getDeviceId() + "  ---  " + _ndp.getPortName());
                                                if (nodeMacList.contains(_ndp.getDeviceId())) {
                                                    HostInterfaceDao ifdao = new HostInterfaceDao();
                                                    try {
                                                        startIfEntity = (IfEntity) ifdao.loadInterfacesByNodeIDAndDesc(node.getId(), _ndp.getPortName());
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    } finally {
                                                        ifdao.close();
                                                    }
                                                    if (startIfEntity != null && endIfEntity != null) {
                                                        Link link = new Link();
                                                        link.setStartId(node.getId());
                                                        link.setStartIndex(startIfEntity.getIndex());
                                                        link.setStartIp(startIfEntity.getIpAddress() == null ? "" : startIfEntity.getIpAddress());
                                                        link.setStartDescr(startIfEntity.getDescr());

                                                        link.setEndId(host.getId());
                                                        link.setEndIndex(endIfEntity.getIndex());
                                                        link.setEndIp(endIfEntity.getIpAddress() == null ? "" : endIfEntity.getIpAddress());
                                                        link.setEndDescr(endIfEntity.getDescr());

                                                        link.setMaxSpeed("200000");
                                                        link.setMaxPer("50");
                                                        link.setLinktype(1);
                                                        link.setType(1);
                                                        link.setFindtype(1);
                                                        link.setLinkName(node.getIpAddress() + "/" + link.getStartDescr() + "-" + host.getIpAddress() + "/" + link.getEndDescr());
                                                        
                                                        if(link.getStartId() == link.getEndId())continue;
                                                        LinkDao linkdao = new LinkDao();
                                                        try {
                                                        	
                                                        	if(!existLinkHash.containsKey(link.getStartId()+":"+link.getStartIndex()+":"+link.getEndId()+":"+link.getEndIndex())
                                                            		&& !existLinkHash.containsKey(link.getEndId()+":"+link.getEndIndex()+":"+link.getStartId()+":"+link.getStartIndex())
                                                            		&& !existEndLinkHash.containsKey(link.getStartId()+":"+link.getStartIndex()+":"+link.getEndId()+":"+link.getEndIndex())
                                                            		&& !existEndLinkHash.containsKey(link.getEndId()+":"+link.getEndIndex()+":"+link.getStartId()+":"+link.getStartIndex())){
                                                        		linkdao.save(link);

                                                                XmlOperator xopr = new XmlOperator();
                                                                xopr.setFile("network.jsp");
                                                                xopr.init4updateXml();
                                                                xopr.addLine(link.getLinkName(), String.valueOf(link.getId()), "net" + String.valueOf(link.getStartId()), "net" + String.valueOf(link.getEndId()));
                                                                xopr.writeXml();

                                                                // ��·��Ϣʵʱ����
                                                                LinkRoad lr = new LinkRoad();
                                                                lr.setId(link.getId());
                                                                lr.setLinkName(link.getLinkName());
                                                                lr.setLinkName(link.getLinkName());// yangjun
                                                                // add
                                                                lr.setMaxSpeed(link.getMaxSpeed());// yangjun
                                                                // add
                                                                lr.setMaxPer(link.getMaxPer());// yangjun
                                                                // add
                                                                lr.setStartId(link.getStartId());
                                                                lr.setStartIp(link.getStartIp());
                                                                lr.setStartIndex(link.getStartIndex());
                                                                lr.setStartDescr(link.getStartDescr());
                                                                lr.setEndIp(link.getEndIp());
                                                                lr.setEndId(link.getEndId());
                                                                lr.setEndIndex(link.getEndIndex());
                                                                lr.setEndDescr(link.getEndDescr());
                                                                lr.setAssistant(link.getAssistant());
                                                                lr.setType(link.getType());
                                                                lr.setShowinterf(link.getShowinterf());
                                                                PollingEngine.getInstance().getLinkList().add(lr);
                                                        	}
                                                        	
                                                            
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        } finally {
                                                            linkdao.close();
                                                        }
                                                        break;
                                                    }

                                                }
                                            }
                                        }
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                } finally {

                                }

                                newNDP.add(ndp);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {
                        	////ϵͳ�ڵ�һ������ʱ��,ipmacHash�п��ܻ������ڸ�MAC��ַ,����������
                            // ���Ҳ���NDP��ǰ��IP��ַ,�򲻸���NDP���Ա��´η�����
                            if (ipmacHash != null && ipmacHash.size() > 0) {
//                                Enumeration dbs = ipmacHash.keys();
//                                while (dbs.hasMoreElements()) {
//                                    String mac = (String) dbs.nextElement();
//                                    String ip = (String) ipmacHash.get(mac);
//                                    SysLogger.info(ip + "---------" + mac);
//
//                                }
                                // �����̳߳�
                                int perthread = 50;
                                if (ipmacHash.size() < 50)
                                    perthread = ipmacHash.size();
                                ThreadPool dbthreadPool = new ThreadPool(perthread);
                                // ��MAC-IP����SNMP�ɼ�
                                Hashtable doipmacHash = new Hashtable();
                                for (Enumeration enumeration = ipmacHash.keys(); enumeration.hasMoreElements();) {
                                    String mac = (String) enumeration.nextElement();
                                    String ip = (String) ipmacHash.get(mac);
                                    if (ipaliasHash.containsKey(ip))
                                        continue;
                                    doipmacHash.put(mac, ip);
                                    // dbthreadPool.runTask(createMACTask(ip,node));
                                }
                                for (Enumeration enumeration = doipmacHash.keys(); enumeration.hasMoreElements();) {
                                    String mac = (String) enumeration.nextElement();
                                    String ip = (String) ipmacHash.get(mac);
                                    dbthreadPool.runTask(createMACTask(ip, node));
                                }
                                // �ر��̳߳ز��ȴ������������
                                dbthreadPool.join();
                                dbthreadPool.close();
                                dbthreadPool = null;
                                //SysLogger.info("1======================================" + node.getIpAddress());
                                for (Enumeration enumeration = ipmacHash.keys(); enumeration.hasMoreElements();) {
                                    String mac = (String) enumeration.nextElement();
                                    String ip = (String) ipmacHash.get(mac);

                                    if (ShareData.getMacNDP().containsKey(ip + ":" + node.getIpAddress())) {
                                        List macList = (List) ShareData.getMacNDP().get(ip + ":" + node.getIpAddress());
                                        if (macList.contains(ndp.getDeviceId())) {
                                            // �����IP��ַ����SNMP���
//                                            SysLogger.info(ip + "     �����IP��ַ����SNMP���2");
                                            //�жϸ�IP�Ƿ��Ѿ���ϵͳ�м���
                                            String nodeIp = "";
                                            if(nodelistHash.containsKey(ip))nodeIp = (String)nodelistHash.get(ip);
                                            if(nodeIp == null || nodeIp.trim().length()==0){
                                            	if(ipaliasHash.containsKey(ip))nodeIp = (String)ipaliasHash.get(ip);
                                            }
                                            Host _host = null;
                                            if(nodeIp != null && nodeIp.trim().length()> 0){
                                            	_host = (Host) PollingEngine.getInstance().getNodeByIp(nodeIp);
                                            	
                                            	if(_host != null){
                                            		//ֻ������·��ϵ
                                            		//������·��ϵ ��ʼ
                                            		String ipaddress = nodeIp;
//                                                    String ipaddress = (String)nodehash.get(ndp.getDeviceId()); //nodehash===>key:mac value:ip
//                                                    Host _host = (Host) PollingEngine.getInstance().getNodeByIp(ipaddress);
//                                                    if(_host == null)continue;
                                                    Hashtable portDescHash = new Hashtable();
                                                    if(ShareData.getSharedata() != null && ShareData.getSharedata().containsKey(ipaddress)){
                                                    	Vector interfaceVector = null;
                                                    	if (((Hashtable) ShareData.getSharedata().get(ipaddress)).containsKey("interface")) {
                                                            interfaceVector = (Vector) ((Hashtable) ShareData.getSharedata().get(ipaddress)).get("interface");
                                                            if(interfaceVector != null && interfaceVector.size()>0){
                                                            	Interfacecollectdata interfaceCollectData = null;
                                                            	for(int m=0;m<interfaceVector.size();m++){
                                                            		interfaceCollectData = (Interfacecollectdata) interfaceVector.get(m);
                                                            		if("ifDescr".equalsIgnoreCase(interfaceCollectData.getEntity())){
                                                            			//�˿�����:�˿�����
                                                            			portDescHash.put(interfaceCollectData.getThevalue(), interfaceCollectData.getSubentity());
                                                            		}
                                                            	}
                                                            }
                                                    	}
                                                    	if (((Hashtable) ShareData.getSharedata().get(ipaddress)).containsKey("ndp")) {
                                                            Vector _ndpVector = (Vector) ((Hashtable) ShareData.getSharedata().get(ipaddress)).get("ndp");
                                                            if(_ndpVector != null && _ndpVector.size()>0){
                                                            	NDP _ndp = null;
                                                            	//IfEntity endIfEntity = _host.getIfEntityByIndex(ifIndex).getIfEntityByDesc(ndp.getPortName());
                                                            	com.afunms.polling.node.IfEntity endIfEntity = null;
                                                            	if(portDescHash.containsKey(ndp.getPortName())){
                                                            		//���ڸĶ˿�����
                                                            		endIfEntity = _host.getIfEntityByIndex((String)portDescHash.get(ndp.getPortName()));
                                                            	}
                                                                IfEntity startIfEntity = null;
                                                                HostInterfaceDao ifdao = new HostInterfaceDao();
                                                                List iflist = null;
                                                                Hashtable ifhash = new Hashtable();
                                                                try{
                                                                	iflist = ifdao.loadAll();
                                                                	if(iflist != null && iflist.size()>0){
                                                                		InterfaceNode vo = null;
                                                                		for(int n=0;n<iflist.size();n++){
                                                                			vo = (InterfaceNode)iflist.get(n);
                                                                			ifhash.put(vo.getNode_id()+":"+vo.getDescr(), vo);
                                                                		}
                                                                	}
                                                                }catch(Exception e){
                                                                	e.printStackTrace();
                                                                }finally{
                                                                	ifdao.close();
                                                                }                                  
                                                                if(endIfEntity != null){
                                                                	//�����ڶԶ˽ӿ�
                                                                	InterfaceNode vo = null;
                                                                	for (int k = 0; k < _ndpVector.size(); k++) {
                                                                        _ndp = (NDP) _ndpVector.get(k);
//                                                                        SysLogger.info(_host.getIpAddress() + "  ---  " + _ndp.getDeviceId() + "  ---  " + _ndp.getPortName());
                                                                        if (nodeMacList.contains(_ndp.getDeviceId())) {
                                                                        	if(ifhash.containsKey(node.getId()+":"+_ndp.getPortName())){
                                                                        		vo = (InterfaceNode)ifhash.get(node.getId()+":"+_ndp.getPortName());
                                                                        		if(vo != null){
                                                                        			Link link = new Link();
                                                                                    link.setStartId(node.getId());
                                                                                    link.setStartIndex(vo.getEntity());
                                                                                    link.setStartIp(vo.getIp_address() == null ? "" : vo.getIp_address());
                                                                                    link.setStartDescr(vo.getDescr());

                                                                                    link.setEndId(_host.getId());
                                                                                    link.setEndIndex(endIfEntity.getIndex());
                                                                                    link.setEndIp(endIfEntity.getIpAddress() == null ? "" : endIfEntity.getIpAddress());
                                                                                    link.setEndDescr(endIfEntity.getDescr());

                                                                                    link.setMaxSpeed("200000");
                                                                                    link.setMaxPer("50");
                                                                                    link.setLinktype(1);
                                                                                    link.setType(1);
                                                                                    link.setFindtype(1);
                                                                                    link.setLinkName(node.getIpAddress() + "/" + link.getStartDescr() + "-" + _host.getIpAddress() + "/" + link.getEndDescr());
                                                                                    
                                                                                    if(link.getStartId() == link.getEndId())continue;
                                                                                    LinkDao linkdao = new LinkDao();
                                                                                    try {
                                                                                    	List linklist = PollingEngine.getInstance().getLinkList();
                                                                                        Hashtable existLinkHash = new Hashtable();
                                                                                        Hashtable existEndLinkHash = new Hashtable();
                                                                                        if(linklist != null && linklist.size()>0){
                                                                                        	LinkRoad lr = null;
                                                                                        	
                                                                                        	for(int p=0;p<linklist.size();p++){
                                                                                        		lr = (LinkRoad)linklist.get(p);
                                                                                        		existLinkHash.put(lr.getStartId()+":"+lr.getStartIndex()+":"+lr.getEndId()+":"+lr.getEndIndex(), lr);
                                                                                        		existEndLinkHash.put(lr.getEndId()+":"+lr.getEndIndex()+":"+lr.getStartId()+":"+lr.getStartIndex(), lr);
                                                                                        	}
                                                                                        }
                                                                                    	if(!existLinkHash.containsKey(link.getStartId()+":"+link.getStartIndex()+":"+link.getEndId()+":"+link.getEndIndex())
                                                                                        		&& !existLinkHash.containsKey(link.getEndId()+":"+link.getEndIndex()+":"+link.getStartId()+":"+link.getStartIndex())
                                                                                        		&& !existEndLinkHash.containsKey(link.getStartId()+":"+link.getStartIndex()+":"+link.getEndId()+":"+link.getEndIndex())
                                                                                        		&& !existEndLinkHash.containsKey(link.getEndId()+":"+link.getEndIndex()+":"+link.getStartId()+":"+link.getStartIndex())){
                                                                                        	//
                                                                                        	linkdao.save(link);

                                                                                            XmlOperator xopr = new XmlOperator();
                                                                                            xopr.setFile("network.jsp");
                                                                                            xopr.init4updateXml();
                                                                                            xopr.addLine(link.getLinkName(), String.valueOf(link.getId()), "net" + String.valueOf(link.getStartId()), "net" + String.valueOf(link.getEndId()));
                                                                                            xopr.writeXml();

                                                                                            // ��·��Ϣʵʱ����
                                                                                            LinkRoad lr = new LinkRoad();
                                                                                            lr.setId(link.getId());
                                                                                            lr.setLinkName(link.getLinkName());
                                                                                            lr.setLinkName(link.getLinkName());// yangjun
                                                                                            // add
                                                                                            lr.setMaxSpeed(link.getMaxSpeed());// yangjun
                                                                                            // add
                                                                                            lr.setMaxPer(link.getMaxPer());// yangjun
                                                                                            // add
                                                                                            lr.setStartId(link.getStartId());
                                                                                            lr.setStartIp(link.getStartIp());
                                                                                            lr.setStartIndex(link.getStartIndex());
                                                                                            lr.setStartDescr(link.getStartDescr());
                                                                                            lr.setEndIp(link.getEndIp());
                                                                                            lr.setEndId(link.getEndId());
                                                                                            lr.setEndIndex(link.getEndIndex());
                                                                                            lr.setEndDescr(link.getEndDescr());
                                                                                            lr.setAssistant(link.getAssistant());
                                                                                            lr.setType(link.getType());
                                                                                            lr.setShowinterf(link.getShowinterf());
                                                                                            PollingEngine.getInstance().getLinkList().add(lr);
                                                                                        }
                                                                                    	
                                                                                    	
                                                                                    } catch (Exception e) {
                                                                                        e.printStackTrace();
                                                                                    } finally {
                                                                                        linkdao.close();
                                                                                    }

                                                                                    break;
                                                                        		}
                                                                        	}
                                                                        }
                                                                    }
                                                                }
                                                            	
                                                            }
                                                    	}
                                                    }                                            		
                                            		
                                            	}else{
                                            		//��Ҫ��Ӹ��豸
                                            		try {
                                            			
                                            			if(ipaliasHash.containsKey(ip)){
                                                        	//�Ѿ����ڸ��豸
                                                        	String _nodeIp = "";
                                                            if(nodelistHash.containsKey(ip))_nodeIp = (String)nodelistHash.get(ip);
                                                            if(_nodeIp == null || _nodeIp.trim().length()==0){
                                                            	if(ipaliasHash.containsKey(ip))_nodeIp = (String)ipaliasHash.get(ip);
                                                            }
                                                            Host p_host = null;
                                                            if(_nodeIp != null && _nodeIp.trim().length()>0){
                                                            	p_host = (Host) PollingEngine.getInstance().getNodeByIp(_nodeIp);
                                                            	if(p_host != null){
                                                            		//ֻ������·��ϵ
                                                            		continue;
                                                            	}
                                                            }
                                                        }
                                            			
                                            			
                                                        TopoHelper helper = new TopoHelper(); // �����������ݿ�͸����ڴ�
                                                        int addResult = 0;
                                                        addResult = helper.addHost(node.getAssetid(), node.getLocation(), ip, ip, node.getSnmpversion(), node.getCommunity(), node.getWritecommunity(), node.getTransfer(), 2, node.getOstype(), 1, node.getBid(), node.getSendmobiles(), node.getSendemail(), node.getSendphone(), node.getSupperid(), true, node.getSecuritylevel(), node.getSecurityName(), node.getV3_ap(), node.getAuthpassphrase(), node.getV3_privacy(), node.getPrivacyPassphrase(),layer+1);
                                                        // ������·
                                                        NDPSingleSnmp ndpsnmp = null;
                                                        com.afunms.discovery.Host host = null;
                                                        NodeGatherIndicators _alarmIndicatorsNode = new NodeGatherIndicators();
                                                        try {
                                                            host = helper.getHost();

                                                            HostNodeDao nodedao = new HostNodeDao();
                                                            try {
                                                                nodedao.addNodeByNDP(host, addResult);
                                                                // �����豸
                                                                XmlOperator opr = new XmlOperator();
                                                                opr.setFile("network.jsp");
                                                                opr.init4updateXml();
                                                                opr.addNode(helper.getHost());
                                                                opr.writeXml();
                                                            } catch (Exception e) {

                                                            } finally {
                                                                nodedao.close();
                                                            }

                                                            _alarmIndicatorsNode.setNodeid(host.getId() + "");
                                                            ndpsnmp = (NDPSingleSnmp) Class.forName("com.afunms.polling.snmp.ndp.NDPSingleSnmp").newInstance();
                                                            returnHash = ndpsnmp.collect_Data(_alarmIndicatorsNode);
                                                            IfEntity endIfEntity = host.getIfEntityByDesc(ndp.getPortName());
                                                            IfEntity startIfEntity = null;
                                                            if (returnHash != null && returnHash.containsKey("ndp")) {
                                                                Vector _ndpVector = (Vector) returnHash.get("ndp");
                                                                if (_ndpVector != null && _ndpVector.size() > 0) {
                                                                    NDP _ndp = null;
                                                                    for (int k = 0; k < _ndpVector.size(); k++) {
                                                                        _ndp = (NDP) _ndpVector.get(k);
//                                                                        SysLogger.info(host.getIpAddress() + "  ---  " + _ndp.getDeviceId() + "  ---  " + _ndp.getPortName());
                                                                        if (nodeMacList.contains(_ndp.getDeviceId())) {
                                                                            HostInterfaceDao ifdao = new HostInterfaceDao();
                                                                            try {
                                                                                startIfEntity = (IfEntity) ifdao.loadInterfacesByNodeIDAndDesc(node.getId(), _ndp.getPortName());
                                                                            } catch (Exception e) {
                                                                                e.printStackTrace();
                                                                            } finally {
                                                                                ifdao.close();
                                                                            }
                                                                            if (startIfEntity != null && endIfEntity != null) {
                                                                                Link link = new Link();
                                                                                link.setStartId(node.getId());
                                                                                link.setStartIndex(startIfEntity.getIndex());
                                                                                link.setStartIp(startIfEntity.getIpAddress() == null ? "" : startIfEntity.getIpAddress());
                                                                                link.setStartDescr(startIfEntity.getDescr());

                                                                                link.setEndId(host.getId());
                                                                                link.setEndIndex(endIfEntity.getIndex());
                                                                                link.setEndIp(endIfEntity.getIpAddress() == null ? "" : endIfEntity.getIpAddress());
                                                                                link.setEndDescr(endIfEntity.getDescr());

                                                                                link.setMaxSpeed("200000");
                                                                                link.setMaxPer("50");
                                                                                link.setLinktype(1);
                                                                                link.setType(1);
                                                                                link.setFindtype(1);
                                                                                link.setLinkName(node.getIpAddress() + "/" + link.getStartDescr() + "-" + host.getIpAddress() + "/" + link.getEndDescr());
                                                                                
                                                                                if(link.getStartId() == link.getEndId())continue;
                                                                                LinkDao linkdao = new LinkDao();
                                                                                try {
                                                                                    List linklist = PollingEngine.getInstance().getLinkList();
                                                                                    Hashtable existLinkHash = new Hashtable();
                                                                                    Hashtable existEndLinkHash = new Hashtable();
                                                                                    if(linklist != null && linklist.size()>0){
                                                                                    	LinkRoad lr = null;
                                                                                    	
                                                                                    	for(int p=0;p<linklist.size();p++){
                                                                                    		lr = (LinkRoad)linklist.get(p);
                                                                                    		existLinkHash.put(lr.getStartId()+":"+lr.getStartIndex()+":"+lr.getEndId()+":"+lr.getEndIndex(), lr);
                                                                                    		existEndLinkHash.put(lr.getEndId()+":"+lr.getEndIndex()+":"+lr.getStartId()+":"+lr.getStartIndex(), lr);
                                                                                    	}
                                                                                    }
                                                                                	if(!existLinkHash.containsKey(link.getStartId()+":"+link.getStartIndex()+":"+link.getEndId()+":"+link.getEndIndex())
                                                                                    		&& !existLinkHash.containsKey(link.getEndId()+":"+link.getEndIndex()+":"+link.getStartId()+":"+link.getStartIndex())
                                                                                    		&& !existEndLinkHash.containsKey(link.getStartId()+":"+link.getStartIndex()+":"+link.getEndId()+":"+link.getEndIndex())
                                                                                    		&& !existEndLinkHash.containsKey(link.getEndId()+":"+link.getEndIndex()+":"+link.getStartId()+":"+link.getStartIndex())){
                                                                                		linkdao.save(link);

                                                                                        XmlOperator xopr = new XmlOperator();
                                                                                        xopr.setFile("network.jsp");
                                                                                        xopr.init4updateXml();
                                                                                        xopr.addLine(link.getLinkName(), String.valueOf(link.getId()), "net" + String.valueOf(link.getStartId()), "net" + String.valueOf(link.getEndId()));
                                                                                        xopr.writeXml();

                                                                                        // ��·��Ϣʵʱ����
                                                                                        LinkRoad lr = new LinkRoad();
                                                                                        lr.setId(link.getId());
                                                                                        lr.setLinkName(link.getLinkName());
                                                                                        lr.setLinkName(link.getLinkName());// yangjun
                                                                                        // add
                                                                                        lr.setMaxSpeed(link.getMaxSpeed());// yangjun
                                                                                        // add
                                                                                        lr.setMaxPer(link.getMaxPer());// yangjun
                                                                                        // add
                                                                                        lr.setStartId(link.getStartId());
                                                                                        lr.setStartIp(link.getStartIp());
                                                                                        lr.setStartIndex(link.getStartIndex());
                                                                                        lr.setStartDescr(link.getStartDescr());
                                                                                        lr.setEndIp(link.getEndIp());
                                                                                        lr.setEndId(link.getEndId());
                                                                                        lr.setEndIndex(link.getEndIndex());
                                                                                        lr.setEndDescr(link.getEndDescr());
                                                                                        lr.setAssistant(link.getAssistant());
                                                                                        lr.setType(link.getType());
                                                                                        lr.setShowinterf(link.getShowinterf());
                                                                                        PollingEngine.getInstance().getLinkList().add(lr);
                                                                                	}
                                                                                    
                                                                                } catch (Exception e) {
                                                                                    e.printStackTrace();
                                                                                } finally {
                                                                                    linkdao.close();
                                                                                }
                                                                                break;
                                                                            }

                                                                        }
                                                                    }
                                                                }
                                                            }

                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        } finally {

                                                        }

                                                        newNDP.add(ndp);
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                    break;
                                            	}
                                            }                                          
                                        }
                                    }
                                    // dbthreadPool.runTask(createMACTask(ip,node));
                                }

                            }

                        }
                    }
                } else {
                    // ������һ�βɼ����и�NDP��Ϣ������Ҫ�ж����ݿ����Ƿ��У������ݿ���û�У�
                    // ��˵���ϴ�û�жԸ�NDP��Ϣ���з���
                    if (!ndpFromDbHash.containsKey(ndp.getDeviceId() + "|" + ndp.getPortName())) {
//                        SysLogger.info(" $$$ �����µ�NDP��Ϣ��" + ndp.getDeviceId() + "  " + ndp.getPortName() + " $$$");
                        // �ҵ��µ�NDP��Ϣ���豸�п������¼ӵ�
                        if (nodehash.containsKey(ndp.getDeviceId())) {
//                        	SysLogger.info("====================nodehash���ڸ�MAC ====="+ndp.getDeviceId());
                            // �Ѿ���ϵͳ���ڸ��豸,ֻ����NDP��
                            newNDP.add(ndp);
                            //��Ҫ���¼�����·��ϵ
//                            SysLogger.info("==========================================");
//                            SysLogger.info("=======��ʼ������·��ϵ "+ndp.getDeviceId()+"   "+ndp.getPortName()+" =======");
//                            SysLogger.info("==========================================");
                            String ipaddress = (String)nodehash.get(ndp.getDeviceId());
                            String nodeIp = "";
                            if(nodelistHash.containsKey(ipaddress))nodeIp = (String)nodelistHash.get(ipaddress);
                            if(nodeIp == null || nodeIp.trim().length()==0){
                            	if(ipaliasHash.containsKey(ipaddress))nodeIp = (String)ipaliasHash.get(ipaddress);
                            }
                            //SysLogger.info("=== ϵͳ�д��ڸ��豸IPADDRESS "+ipaddress+" =====");
                            ipaddress = nodeIp;
                            //SysLogger.info("=== 99ϵͳ�д��ڸ��豸IPADDRESS "+ipaddress+" =====");
                            Host _host = (Host) PollingEngine.getInstance().getNodeByIp(ipaddress);
                            if(_host == null)continue;
                            
                            Hashtable portDescHash = new Hashtable();
                            if(ShareData.getSharedata() != null && ShareData.getSharedata().containsKey(ipaddress)){
                            	//SysLogger.info("=== ϵͳ�д��ڸ��豸 "+ipaddress+" =====");
                            	Vector interfaceVector = null;
                            	if (((Hashtable) ShareData.getSharedata().get(ipaddress)).containsKey("interface")) {
                                    interfaceVector = (Vector) ((Hashtable) ShareData.getSharedata().get(ipaddress)).get("interface");
                                    if(interfaceVector != null && interfaceVector.size()>0){
                                    	Interfacecollectdata interfaceCollectData = null;
                                    	for(int m=0;m<interfaceVector.size();m++){
                                    		interfaceCollectData = (Interfacecollectdata) interfaceVector.get(m);
                                    		//SysLogger.info(ipaddress+" interface data "+interfaceCollectData.getThevalue()+" ---  "+interfaceCollectData.getSubentity());
                                    		if("ifDescr".equalsIgnoreCase(interfaceCollectData.getEntity())){
                                    			//�˿�����:�˿�����
                                    			//SysLogger.info("$$$$$$$$  put portDescHash "+ipaddress+"  "+interfaceCollectData.getThevalue()+" ---  "+interfaceCollectData.getSubentity());
                                    			portDescHash.put(interfaceCollectData.getThevalue(), interfaceCollectData.getSubentity());
                                    		}
                                    	}
                                    }
                            	}
                            	//SysLogger.info("=== 1ϵͳ�д��ڸ��豸 "+ipaddress+" =====");
                            	if (((Hashtable) ShareData.getSharedata().get(ipaddress)).containsKey("ndp")) {
                                    Vector _ndpVector = (Vector) ((Hashtable) ShareData.getSharedata().get(ipaddress)).get("ndp");
                                    if(_ndpVector != null && _ndpVector.size()>0){
                                    	NDP _ndp = null;
                                    	//IfEntity endIfEntity = _host.getIfEntityByIndex(ifIndex).getIfEntityByDesc(ndp.getPortName());
                                    	com.afunms.polling.node.IfEntity endIfEntity = null;
                                    	if(portDescHash.containsKey(ndp.getPortName())){
                                    		//���ڸĶ˿�����
                                    		endIfEntity = _host.getIfEntityByIndex((String)portDescHash.get(ndp.getPortName()));
                                    	}
                                        IfEntity startIfEntity = null;
                                        HostInterfaceDao ifdao = new HostInterfaceDao();
                                        List iflist = null;
                                        Hashtable ifhash = new Hashtable();
                                        try{
                                        	iflist = ifdao.loadAll();
                                        	if(iflist != null && iflist.size()>0){
                                        		InterfaceNode vo = null;
                                        		for(int n=0;n<iflist.size();n++){
                                        			vo = (InterfaceNode)iflist.get(n);
                                        			ifhash.put(vo.getNode_id()+":"+vo.getDescr(), vo);
                                        		}
                                        	}
                                        }catch(Exception e){
                                        	e.printStackTrace();
                                        }finally{
                                        	ifdao.close();
                                        }
                                        
                                        //SysLogger.info("=== 2ϵͳ�д��ڸ��豸 "+ipaddress+" =====");
                                        
                                        if(endIfEntity != null){
                                        	//SysLogger.info("=== 3ϵͳ�д��ڸ��豸 "+ipaddress+" =====");
                                        	//�����ڶԶ˽ӿ�
                                        	InterfaceNode vo = null;
                                        	for (int k = 0; k < _ndpVector.size(); k++) {
                                        		//SysLogger.info("=== 4ϵͳ�д��ڸ��豸 "+ipaddress+" =====");
                                                _ndp = (NDP) _ndpVector.get(k);
                                                //SysLogger.info(_host.getIpAddress() + "  ---  " + _ndp.getDeviceId() + "  ---  " + _ndp.getPortName());
                                                if (nodeMacList.contains(_ndp.getDeviceId())) {
                                                	//SysLogger.info("=== 5ϵͳ�д��ڸ��豸 "+ipaddress+" =====");
                                                	if(ifhash.containsKey(node.getId()+":"+_ndp.getPortName())){
                                                		//SysLogger.info("=== 6ϵͳ�д��ڸ��豸 "+ipaddress+" =====");
                                                		vo = (InterfaceNode)ifhash.get(node.getId()+":"+_ndp.getPortName());
                                                		if(vo != null){
                                                			//SysLogger.info("=== 7ϵͳ�д��ڸ��豸 "+ipaddress+" =====");
                                                			Link link = new Link();
                                                            link.setStartId(node.getId());
                                                            link.setStartIndex(vo.getEntity());
                                                            link.setStartIp(vo.getIp_address() == null ? "" : vo.getIp_address());
                                                            link.setStartDescr(vo.getDescr());

                                                            link.setEndId(_host.getId());
                                                            link.setEndIndex(endIfEntity.getIndex());
                                                            link.setEndIp(endIfEntity.getIpAddress() == null ? "" : endIfEntity.getIpAddress());
                                                            link.setEndDescr(endIfEntity.getDescr());

                                                            link.setMaxSpeed("200000");
                                                            link.setMaxPer("50");
                                                            link.setLinktype(1);
                                                            link.setType(1);
                                                            link.setFindtype(1);
                                                            link.setLinkName(node.getIpAddress() + "/" + link.getStartDescr() + "-" + _host.getIpAddress() + "/" + link.getEndDescr());
                                                            //
                                                            
                                                            if(link.getStartId() == link.getEndId())continue;
                                                            LinkDao linkdao = new LinkDao();
                                                            try {
                                                            	List linklist = PollingEngine.getInstance().getLinkList();
                                                                Hashtable existLinkHash = new Hashtable();
                                                                Hashtable existEndLinkHash = new Hashtable();
                                                                if(linklist != null && linklist.size()>0){
                                                                	LinkRoad lr = null;
                                                                	
                                                                	for(int p=0;p<linklist.size();p++){
                                                                		lr = (LinkRoad)linklist.get(p);
                                                                		existLinkHash.put(lr.getStartId()+":"+lr.getStartIndex()+":"+lr.getEndId()+":"+lr.getEndIndex(), lr);
                                                                		existEndLinkHash.put(lr.getEndId()+":"+lr.getEndIndex()+":"+lr.getStartId()+":"+lr.getStartIndex(), lr);
                                                                	}
                                                                }
                                                                if(!existLinkHash.containsKey(link.getStartId()+":"+link.getStartIndex()+":"+link.getEndId()+":"+link.getEndIndex())
                                                                		&& !existLinkHash.containsKey(link.getEndId()+":"+link.getEndIndex()+":"+link.getStartId()+":"+link.getStartIndex())
                                                                		&& !existEndLinkHash.containsKey(link.getStartId()+":"+link.getStartIndex()+":"+link.getEndId()+":"+link.getEndIndex())
                                                                		&& !existEndLinkHash.containsKey(link.getEndId()+":"+link.getEndIndex()+":"+link.getStartId()+":"+link.getStartIndex())){
                                                                	//
                                                                	linkdao.save(link);

                                                                    XmlOperator xopr = new XmlOperator();
                                                                    xopr.setFile("network.jsp");
                                                                    xopr.init4updateXml();
                                                                    xopr.addLine(link.getLinkName(), String.valueOf(link.getId()), "net" + String.valueOf(link.getStartId()), "net" + String.valueOf(link.getEndId()));
                                                                    xopr.writeXml();

                                                                    // ��·��Ϣʵʱ����
                                                                    LinkRoad lr = new LinkRoad();
                                                                    lr.setId(link.getId());
                                                                    lr.setLinkName(link.getLinkName());
                                                                    lr.setLinkName(link.getLinkName());// yangjun
                                                                    // add
                                                                    lr.setMaxSpeed(link.getMaxSpeed());// yangjun
                                                                    // add
                                                                    lr.setMaxPer(link.getMaxPer());// yangjun
                                                                    // add
                                                                    lr.setStartId(link.getStartId());
                                                                    lr.setStartIp(link.getStartIp());
                                                                    lr.setStartIndex(link.getStartIndex());
                                                                    lr.setStartDescr(link.getStartDescr());
                                                                    lr.setEndIp(link.getEndIp());
                                                                    lr.setEndId(link.getEndId());
                                                                    lr.setEndIndex(link.getEndIndex());
                                                                    lr.setEndDescr(link.getEndDescr());
                                                                    lr.setAssistant(link.getAssistant());
                                                                    lr.setType(link.getType());
                                                                    lr.setShowinterf(link.getShowinterf());
                                                                    PollingEngine.getInstance().getLinkList().add(lr);
                                                                }
                                                            	
                                                            	
                                                            } catch (Exception e) {
                                                                e.printStackTrace();
                                                            } finally {
                                                                linkdao.close();
                                                            }

                                                            break;
                                                		}
                                                	}
                                                }
                                            }
                                        }
                                    	
                                    }
                            	}
                            }
                        } else {
                            // ϵͳ�����ڸ��豸
//                        	SysLogger.info("====================nodehash�����ڸ�MAC ====="+ndp.getDeviceId());
//                            SysLogger.info("ndp.getDeviceId() #####  " + ndp.getDeviceId());
                            if (ipmacHash.containsKey(ndp.getDeviceId())) {
                                // �ҵ���NDP��MAC��ַ��Ӧ��IP
                                String ip = (String) ipmacHash.get(ndp.getDeviceId());
                                com.afunms.discovery.Host host = null;
                                // �����IP��ַ����SNMP���
                                try {
//                                    SysLogger.info(ip + "   ###  �����IP��ַ����SNMP���3");
                                    //���ݿ��IP�������д��ڸ�IP
                                    if(ipaliasHash.containsKey(ip)){
                                    	//�Ѿ����ڸ��豸
                                    	String nodeIp = "";
                                        if(nodelistHash.containsKey(ip))nodeIp = (String)nodelistHash.get(ip);
                                        if(nodeIp == null || nodeIp.trim().length()==0){
                                        	if(ipaliasHash.containsKey(ip))nodeIp = (String)ipaliasHash.get(ip);
                                        }
                                        Host _host = null;
                                        if(nodeIp != null && nodeIp.trim().length()>0){
                                        	_host = (Host) PollingEngine.getInstance().getNodeByIp(nodeIp);
                                        	if(_host != null){
                                        		//ֻ������·��ϵ
                                        		//������·��ϵ ��ʼ
                                        		String ipaddress = nodeIp;
//                                                String ipaddress = (String)nodehash.get(ndp.getDeviceId()); //nodehash===>key:mac value:ip
//                                                Host _host = (Host) PollingEngine.getInstance().getNodeByIp(ipaddress);
//                                                if(_host == null)continue;
                                                Hashtable portDescHash = new Hashtable();
                                                if(ShareData.getSharedata() != null && ShareData.getSharedata().containsKey(ipaddress)){
                                                	Vector interfaceVector = null;
                                                	if (((Hashtable) ShareData.getSharedata().get(ipaddress)).containsKey("interface")) {
                                                        interfaceVector = (Vector) ((Hashtable) ShareData.getSharedata().get(ipaddress)).get("interface");
                                                        if(interfaceVector != null && interfaceVector.size()>0){
                                                        	Interfacecollectdata interfaceCollectData = null;
                                                        	for(int m=0;m<interfaceVector.size();m++){
                                                        		interfaceCollectData = (Interfacecollectdata) interfaceVector.get(m);
                                                        		if("ifDescr".equalsIgnoreCase(interfaceCollectData.getEntity())){
                                                        			//�˿�����:�˿�����
                                                        			portDescHash.put(interfaceCollectData.getThevalue(), interfaceCollectData.getSubentity());
                                                        		}
                                                        	}
                                                        }
                                                	}
                                                	if (((Hashtable) ShareData.getSharedata().get(ipaddress)).containsKey("ndp")) {
                                                        Vector _ndpVector = (Vector) ((Hashtable) ShareData.getSharedata().get(ipaddress)).get("ndp");
                                                        if(_ndpVector != null && _ndpVector.size()>0){
                                                        	NDP _ndp = null;
                                                        	//IfEntity endIfEntity = _host.getIfEntityByIndex(ifIndex).getIfEntityByDesc(ndp.getPortName());
                                                        	com.afunms.polling.node.IfEntity endIfEntity = null;
                                                        	if(portDescHash.containsKey(ndp.getPortName())){
                                                        		//���ڸĶ˿�����
                                                        		endIfEntity = _host.getIfEntityByIndex((String)portDescHash.get(ndp.getPortName()));
                                                        	}
                                                            IfEntity startIfEntity = null;
                                                            HostInterfaceDao ifdao = new HostInterfaceDao();
                                                            List iflist = null;
                                                            Hashtable ifhash = new Hashtable();
                                                            try{
                                                            	iflist = ifdao.loadAll();
                                                            	if(iflist != null && iflist.size()>0){
                                                            		InterfaceNode vo = null;
                                                            		for(int n=0;n<iflist.size();n++){
                                                            			vo = (InterfaceNode)iflist.get(n);
                                                            			ifhash.put(vo.getNode_id()+":"+vo.getDescr(), vo);
                                                            		}
                                                            	}
                                                            }catch(Exception e){
                                                            	e.printStackTrace();
                                                            }finally{
                                                            	ifdao.close();
                                                            }                                  
                                                            if(endIfEntity != null){
                                                            	//�����ڶԶ˽ӿ�
                                                            	InterfaceNode vo = null;
                                                            	for (int k = 0; k < _ndpVector.size(); k++) {
                                                                    _ndp = (NDP) _ndpVector.get(k);
//                                                                    SysLogger.info(_host.getIpAddress() + "  ---  " + _ndp.getDeviceId() + "  ---  " + _ndp.getPortName());
                                                                    if (nodeMacList.contains(_ndp.getDeviceId())) {
                                                                    	if(ifhash.containsKey(node.getId()+":"+_ndp.getPortName())){
                                                                    		vo = (InterfaceNode)ifhash.get(node.getId()+":"+_ndp.getPortName());
                                                                    		if(vo != null){
                                                                    			Link link = new Link();
                                                                                link.setStartId(node.getId());
                                                                                link.setStartIndex(vo.getEntity());
                                                                                link.setStartIp(vo.getIp_address() == null ? "" : startIfEntity.getIpAddress());
                                                                                link.setStartDescr(vo.getDescr());

                                                                                link.setEndId(_host.getId());
                                                                                link.setEndIndex(endIfEntity.getIndex());
                                                                                link.setEndIp(endIfEntity.getIpAddress() == null ? "" : endIfEntity.getIpAddress());
                                                                                link.setEndDescr(endIfEntity.getDescr());

                                                                                link.setMaxSpeed("200000");
                                                                                link.setMaxPer("50");
                                                                                link.setLinktype(1);
                                                                                link.setType(1);
                                                                                link.setFindtype(1);
                                                                                link.setLinkName(node.getIpAddress() + "/" + link.getStartDescr() + "-" + _host.getIpAddress() + "/" + link.getEndDescr());
                                                                                
                                                                                if(link.getStartId() == link.getEndId())continue;
                                                                                LinkDao linkdao = new LinkDao();
                                                                                try {
                                                                                	List linklist = PollingEngine.getInstance().getLinkList();
                                                                                    Hashtable existLinkHash = new Hashtable();
                                                                                    Hashtable existEndLinkHash = new Hashtable();
                                                                                    if(linklist != null && linklist.size()>0){
                                                                                    	LinkRoad lr = null;
                                                                                    	
                                                                                    	for(int p=0;p<linklist.size();p++){
                                                                                    		lr = (LinkRoad)linklist.get(p);
                                                                                    		existLinkHash.put(lr.getStartId()+":"+lr.getStartIndex()+":"+lr.getEndId()+":"+lr.getEndIndex(), lr);
                                                                                    		existEndLinkHash.put(lr.getEndId()+":"+lr.getEndIndex()+":"+lr.getStartId()+":"+lr.getStartIndex(), lr);
                                                                                    	}
                                                                                    }
                                                                                    if(!existLinkHash.containsKey(link.getStartId()+":"+link.getStartIndex()+":"+link.getEndId()+":"+link.getEndIndex())
                                                                                    		&& !existLinkHash.containsKey(link.getEndId()+":"+link.getEndIndex()+":"+link.getStartId()+":"+link.getStartIndex())
                                                                                    		&& !existEndLinkHash.containsKey(link.getStartId()+":"+link.getStartIndex()+":"+link.getEndId()+":"+link.getEndIndex())
                                                                                    		&& !existEndLinkHash.containsKey(link.getEndId()+":"+link.getEndIndex()+":"+link.getStartId()+":"+link.getStartIndex())){
                                                                                    	//
                                                                                    	linkdao.save(link);

                                                                                        XmlOperator xopr = new XmlOperator();
                                                                                        xopr.setFile("network.jsp");
                                                                                        xopr.init4updateXml();
                                                                                        xopr.addLine(link.getLinkName(), String.valueOf(link.getId()), "net" + String.valueOf(link.getStartId()), "net" + String.valueOf(link.getEndId()));
                                                                                        xopr.writeXml();

                                                                                        // ��·��Ϣʵʱ����
                                                                                        LinkRoad lr = new LinkRoad();
                                                                                        lr.setId(link.getId());
                                                                                        lr.setLinkName(link.getLinkName());
                                                                                        lr.setLinkName(link.getLinkName());// yangjun
                                                                                        // add
                                                                                        lr.setMaxSpeed(link.getMaxSpeed());// yangjun
                                                                                        // add
                                                                                        lr.setMaxPer(link.getMaxPer());// yangjun
                                                                                        // add
                                                                                        lr.setStartId(link.getStartId());
                                                                                        lr.setStartIp(link.getStartIp());
                                                                                        lr.setStartIndex(link.getStartIndex());
                                                                                        lr.setStartDescr(link.getStartDescr());
                                                                                        lr.setEndIp(link.getEndIp());
                                                                                        lr.setEndId(link.getEndId());
                                                                                        lr.setEndIndex(link.getEndIndex());
                                                                                        lr.setEndDescr(link.getEndDescr());
                                                                                        lr.setAssistant(link.getAssistant());
                                                                                        lr.setType(link.getType());
                                                                                        lr.setShowinterf(link.getShowinterf());
                                                                                        PollingEngine.getInstance().getLinkList().add(lr);
                                                                                    }
                                                                                	
                                                                                	
                                                                                } catch (Exception e) {
                                                                                    e.printStackTrace();
                                                                                } finally {
                                                                                    linkdao.close();
                                                                                }

                                                                                break;
                                                                    		}
                                                                    	}
                                                                    }
                                                                }
                                                            }
                                                        	
                                                        }
                                                	}
                                                }                                            		
                                        		
                                        	
                                        	}
                                        }
                                    }else{
                                    	//���ݿ��IP�������в����ڸ�IP
                                    	
                                    	if(ipaliasHash.containsKey(ip)){
                                        	//�Ѿ����ڸ��豸
                                        	String nodeIp = "";
                                            if(nodelistHash.containsKey(ip))nodeIp = (String)nodelistHash.get(ip);
                                            if(nodeIp == null || nodeIp.trim().length()==0){
                                            	if(ipaliasHash.containsKey(ip))nodeIp = (String)ipaliasHash.get(ip);
                                            }
                                            Host _host = null;
                                            if(nodeIp != null && nodeIp.trim().length()>0){
                                            	_host = (Host) PollingEngine.getInstance().getNodeByIp(nodeIp);
                                            	if(_host != null){
                                            		//ֻ������·��ϵ
                                            		continue;
                                            	}
                                            }
                                        }
                                    	
                                    	
                                    	TopoHelper helper = new TopoHelper(); // �����������ݿ�͸����ڴ�
                                        int addResult = 0;
                                        addResult = helper.addHost(node.getAssetid(), node.getLocation(), ip, ip, node.getSnmpversion(), node.getCommunity(), node.getWritecommunity(), node.getTransfer(), 2, node.getOstype(), 1, node.getBid(), node.getSendmobiles(), node.getSendemail(), node.getSendphone(), node.getSupperid(), true, node.getSecuritylevel(), node.getSecurityName(), node.getV3_ap(), node.getAuthpassphrase(), node.getV3_privacy(), node.getPrivacyPassphrase(),layer+1);

                                        // ������·
                                        NDPSingleSnmp ndpsnmp = null;
                                        NodeGatherIndicators _alarmIndicatorsNode = new NodeGatherIndicators();
                                        try {
                                            host = helper.getHost();

                                            HostNodeDao nodedao = new HostNodeDao();
                                            try {
                                                nodedao.addNodeByNDP(host, addResult);
                                                // �����豸
                                                XmlOperator opr = new XmlOperator();
                                                opr.setFile("network.jsp");
                                                opr.init4updateXml();
                                                opr.addNode(helper.getHost());
                                                opr.writeXml();
                                            } catch (Exception e) {

                                            } finally {
                                                nodedao.close();
                                            }

                                            _alarmIndicatorsNode.setNodeid(host.getId() + "");
                                            ndpsnmp = (NDPSingleSnmp) Class.forName("com.afunms.polling.snmp.ndp.NDPSingleSnmp").newInstance();
                                            returnHash = ndpsnmp.collect_Data(_alarmIndicatorsNode);

                                            IfEntity endIfEntity = host.getIfEntityByDesc(ndp.getPortName());
                                            IfEntity startIfEntity = null;
                                            if (returnHash != null && returnHash.containsKey("ndp")) {
                                                Vector _ndpVector = (Vector) returnHash.get("ndp");
                                                if (_ndpVector != null && _ndpVector.size() > 0) {
                                                    NDP _ndp = null;
                                                    for (int k = 0; k < _ndpVector.size(); k++) {
                                                        _ndp = (NDP) _ndpVector.get(k);
//                                                        SysLogger.info(host.getIpAddress() + "  ---  " + _ndp.getDeviceId() + "  ---  " + _ndp.getPortName());
                                                        if (nodeMacList.contains(_ndp.getDeviceId())) {
                                                            HostInterfaceDao ifdao = new HostInterfaceDao();
                                                            try {
                                                                startIfEntity = (IfEntity) ifdao.loadInterfacesByNodeIDAndDesc(node.getId(), _ndp.getPortName());
                                                            } catch (Exception e) {
                                                                e.printStackTrace();
                                                            } finally {
                                                                ifdao.close();
                                                            }
                                                            if (startIfEntity != null && endIfEntity != null) {
                                                                Link link = new Link();
                                                                link.setStartId(node.getId());
                                                                link.setStartIndex(startIfEntity.getIndex());
                                                                link.setStartIp(startIfEntity.getIpAddress() == null ? "" : startIfEntity.getIpAddress());
                                                                link.setStartDescr(startIfEntity.getDescr());

                                                                link.setEndId(host.getId());
                                                                link.setEndIndex(endIfEntity.getIndex());
                                                                link.setEndIp(endIfEntity.getIpAddress() == null ? "" : endIfEntity.getIpAddress());
                                                                link.setEndDescr(endIfEntity.getDescr());

                                                                link.setMaxSpeed("200000");
                                                                link.setMaxPer("50");
                                                                link.setLinktype(1);
                                                                link.setType(1);
                                                                link.setFindtype(1);
                                                                link.setLinkName(node.getIpAddress() + "/" + link.getStartDescr() + "-" + host.getIpAddress() + "/" + link.getEndDescr());
                                                                
                                                                if(link.getStartId() == link.getEndId())continue;
                                                                LinkDao linkdao = new LinkDao();
                                                                try {
                                                                	List linklist = PollingEngine.getInstance().getLinkList();
                                                                    Hashtable existLinkHash = new Hashtable();
                                                                    Hashtable existEndLinkHash = new Hashtable();
                                                                    if(linklist != null && linklist.size()>0){
                                                                    	LinkRoad lr = null;
                                                                    	
                                                                    	for(int p=0;p<linklist.size();p++){
                                                                    		lr = (LinkRoad)linklist.get(p);
                                                                    		existLinkHash.put(lr.getStartId()+":"+lr.getStartIndex()+":"+lr.getEndId()+":"+lr.getEndIndex(), lr);
                                                                    		existEndLinkHash.put(lr.getEndId()+":"+lr.getEndIndex()+":"+lr.getStartId()+":"+lr.getStartIndex(), lr);
                                                                    	}
                                                                    }
                                                                    if(!existLinkHash.containsKey(link.getStartId()+":"+link.getStartIndex()+":"+link.getEndId()+":"+link.getEndIndex())
                                                                    		&& !existLinkHash.containsKey(link.getEndId()+":"+link.getEndIndex()+":"+link.getStartId()+":"+link.getStartIndex())
                                                                    		&& !existEndLinkHash.containsKey(link.getStartId()+":"+link.getStartIndex()+":"+link.getEndId()+":"+link.getEndIndex())
                                                                    		&& !existEndLinkHash.containsKey(link.getEndId()+":"+link.getEndIndex()+":"+link.getStartId()+":"+link.getStartIndex())){
                                                                    	linkdao.save(link);

                                                                        XmlOperator xopr = new XmlOperator();
                                                                        xopr.setFile("network.jsp");
                                                                        xopr.init4updateXml();
                                                                        xopr.addLine(link.getLinkName(), String.valueOf(link.getId()), "net" + String.valueOf(link.getStartId()), "net" + String.valueOf(link.getEndId()));
                                                                        xopr.writeXml();

                                                                        // ��·��Ϣʵʱ����
                                                                        LinkRoad lr = new LinkRoad();
                                                                        lr.setId(link.getId());
                                                                        lr.setLinkName(link.getLinkName());
                                                                        lr.setLinkName(link.getLinkName());// yangjun
                                                                        // add
                                                                        lr.setMaxSpeed(link.getMaxSpeed());// yangjun
                                                                        // add
                                                                        lr.setMaxPer(link.getMaxPer());// yangjun
                                                                        // add
                                                                        lr.setStartId(link.getStartId());
                                                                        lr.setStartIp(link.getStartIp());
                                                                        lr.setStartIndex(link.getStartIndex());
                                                                        lr.setStartDescr(link.getStartDescr());
                                                                        lr.setEndIp(link.getEndIp());
                                                                        lr.setEndId(link.getEndId());
                                                                        lr.setEndIndex(link.getEndIndex());
                                                                        lr.setEndDescr(link.getEndDescr());
                                                                        lr.setAssistant(link.getAssistant());
                                                                        lr.setType(link.getType());
                                                                        lr.setShowinterf(link.getShowinterf());
                                                                        PollingEngine.getInstance().getLinkList().add(lr);
                                                                    }
                                                                    
                                                                } catch (Exception e) {
                                                                    e.printStackTrace();
                                                                } finally {
                                                                    linkdao.close();
                                                                }

                                                                break;
                                                            }

                                                        }
                                                    }
                                                }
                                            }

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        } finally {

                                        }
                                        newNDP.add(ndp);
                                    }
                                    
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            } else {
                                // ���Ҳ���NDP��ǰ��IP��ַ,�򲻸���NDP���Ա��´η�����
                                if (ipmacHash != null && ipmacHash.size() > 0) {
                                    // �����̳߳�
                                    int perthread = 50;
                                    if (ipmacHash.size() < 50)
                                        perthread = ipmacHash.size();
                                    ThreadPool dbthreadPool = new ThreadPool(perthread);
                                    // ������Ҫ�ɼ���IP-MACָ��
                                    for (Enumeration enumeration = ipmacHash.keys(); enumeration.hasMoreElements();) {
                                        String mac = (String) enumeration.nextElement();
                                        String ip = (String) ipmacHash.get(mac);
                                        dbthreadPool.runTask(createMACTask(ip, node));
                                    }
                                    // �ر��̳߳ز��ȴ������������
                                    dbthreadPool.join();
                                    dbthreadPool.close();
                                    dbthreadPool = null;

                                    for (Enumeration enumeration = ipmacHash.keys(); enumeration.hasMoreElements();) {
                                        String mac = (String) enumeration.nextElement();
                                        String ip = (String) ipmacHash.get(mac);
//                                        SysLogger.info("ip:" + ip + "===mac:" + mac + "========" + node.getIpAddress());

                                        if (ShareData.getMacNDP().containsKey(ip + ":" + node.getIpAddress())) {
                                            List macList = (List) ShareData.getMacNDP().get(ip + ":" + node.getIpAddress());
                                            if (macList.contains(ndp.getDeviceId())) {
                                                // �����IP��ַ����SNMP���
//                                                SysLogger.info(ip + "     �����IP��ַ����SNMP���4");
                                                //�жϸ�IP�Ƿ��Ѿ�����

                                                if(ipaliasHash.containsKey(ip)){
                                                	//�Ѿ����ڸ��豸
                                                	String nodeIp = "";
                                                    if(nodelistHash.containsKey(ip))nodeIp = (String)nodelistHash.get(ip);
                                                    if(nodeIp == null || nodeIp.trim().length()==0){
                                                    	if(ipaliasHash.containsKey(ip))nodeIp = (String)ipaliasHash.get(ip);
                                                    }
                                                    Host _host = null;
                                                    if(nodeIp != null && nodeIp.trim().length()>0){
                                                    	_host = (Host) PollingEngine.getInstance().getNodeByIp(nodeIp);
                                                    	if(_host != null){
                                                    		//ֻ������·��ϵ
                                                    		continue;
                                                    	}
                                                    }
                                                } 
                                                try {
                                                    // addNode(ip,node);
                                                    TopoHelper helper = new TopoHelper(); // �����������ݿ�͸����ڴ�
                                                    int addResult = 0;
                                                    addResult = helper.addHost(node.getAssetid(), node.getLocation(), ip, ip, node.getSnmpversion(), node.getCommunity(), node.getWritecommunity(), node.getTransfer(), 2, node.getOstype(), 1, node.getBid(), node.getSendmobiles(), node.getSendemail(), node.getSendphone(), node.getSupperid(), true, node.getSecuritylevel(), node.getSecurityName(), node.getV3_ap(), node.getAuthpassphrase(), node.getV3_privacy(), node.getPrivacyPassphrase(),layer+1);

                                                    com.afunms.discovery.Host host = null;
                                                    // ������·
                                                    NDPSingleSnmp ndpsnmp = null;
                                                    NodeGatherIndicators _alarmIndicatorsNode = new NodeGatherIndicators();
                                                    try {
                                                        host = helper.getHost();
                                                        if(host == null)continue;
                                                        HostNodeDao nodedao = new HostNodeDao();
                                                        try {
                                                            nodedao.addNodeByNDP(host, addResult);
                                                            // �����豸
                                                            XmlOperator opr = new XmlOperator();
                                                            opr.setFile("network.jsp");
                                                            opr.init4updateXml();
                                                            opr.addNode(helper.getHost());
                                                            opr.writeXml();
                                                        } catch (Exception e) {

                                                        } finally {
                                                            nodedao.close();
                                                        }

                                                        _alarmIndicatorsNode.setNodeid(host.getId() + "");
                                                        ndpsnmp = (NDPSingleSnmp) Class.forName("com.afunms.polling.snmp.ndp.NDPSingleSnmp").newInstance();
                                                        returnHash = ndpsnmp.collect_Data(_alarmIndicatorsNode);

                                                        IfEntity endIfEntity = host.getIfEntityByDesc(ndp.getPortName());
                                                        IfEntity startIfEntity = null;
                                                        if (returnHash != null && returnHash.containsKey("ndp")) {
                                                            Vector _ndpVector = (Vector) returnHash.get("ndp");
                                                            if (_ndpVector != null && _ndpVector.size() > 0) {
                                                                NDP _ndp = null;
                                                                for (int k = 0; k < _ndpVector.size(); k++) {
                                                                    _ndp = (NDP) _ndpVector.get(k);
//                                                                    SysLogger.info(host.getIpAddress() + "  ---  " + _ndp.getDeviceId() + "  ---  " + _ndp.getPortName());
                                                                    if (nodeMacList.contains(_ndp.getDeviceId())) {
                                                                        HostInterfaceDao ifdao = new HostInterfaceDao();
                                                                        try {
                                                                            startIfEntity = (IfEntity) ifdao.loadInterfacesByNodeIDAndDesc(node.getId(), _ndp.getPortName());
                                                                        } catch (Exception e) {
                                                                            e.printStackTrace();
                                                                        } finally {
                                                                            ifdao.close();
                                                                        }
                                                                        if (startIfEntity == null)
                                                                            SysLogger.info("startIfEntity  is null ####");
                                                                        if (endIfEntity == null)
                                                                            SysLogger.info("endIfEntity  is null ####");

                                                                        if (startIfEntity != null && endIfEntity != null) {
                                                                            Link link = new Link();
                                                                            link.setStartId(node.getId());
                                                                            link.setStartIndex(startIfEntity.getIndex());
                                                                            link.setStartIp(startIfEntity.getIpAddress() == null ? "" : startIfEntity.getIpAddress());
                                                                            link.setStartDescr(startIfEntity.getDescr());

                                                                            link.setEndId(host.getId());
                                                                            link.setEndIndex(endIfEntity.getIndex());
                                                                            link.setEndIp(endIfEntity.getIpAddress() == null ? "" : endIfEntity.getIpAddress());
                                                                            link.setEndDescr(endIfEntity.getDescr());

                                                                            link.setMaxSpeed("200000");
                                                                            link.setMaxPer("50");
                                                                            link.setLinktype(1);
                                                                            link.setType(1);
                                                                            link.setFindtype(1);
                                                                            link.setLinkName(node.getIpAddress() + "/" + link.getStartDescr() + "-" + host.getIpAddress() + "/" + link.getEndDescr());
                                                                            
                                                                            if(link.getStartId() == link.getEndId())continue;
                                                                            LinkDao linkdao = new LinkDao();
                                                                            try {
                                                                            	List linklist = PollingEngine.getInstance().getLinkList();
                                                                                Hashtable existLinkHash = new Hashtable();
                                                                                Hashtable existEndLinkHash = new Hashtable();
                                                                                if(linklist != null && linklist.size()>0){
                                                                                	LinkRoad lr = null;
                                                                                	
                                                                                	for(int p=0;p<linklist.size();p++){
                                                                                		lr = (LinkRoad)linklist.get(p);
                                                                                		existLinkHash.put(lr.getStartId()+":"+lr.getStartIndex()+":"+lr.getEndId()+":"+lr.getEndIndex(), lr);
                                                                                		existEndLinkHash.put(lr.getEndId()+":"+lr.getEndIndex()+":"+lr.getStartId()+":"+lr.getStartIndex(), lr);
                                                                                	}
                                                                                }
                                                                                if(!existLinkHash.containsKey(link.getStartId()+":"+link.getStartIndex()+":"+link.getEndId()+":"+link.getEndIndex())
                                                                                		&& !existLinkHash.containsKey(link.getEndId()+":"+link.getEndIndex()+":"+link.getStartId()+":"+link.getStartIndex())
                                                                                		&& !existEndLinkHash.containsKey(link.getStartId()+":"+link.getStartIndex()+":"+link.getEndId()+":"+link.getEndIndex())
                                                                                		&& !existEndLinkHash.containsKey(link.getEndId()+":"+link.getEndIndex()+":"+link.getStartId()+":"+link.getStartIndex())){
                                                                                	linkdao.save(link);

                                                                                    // ��·��Ϣʵʱ����
                                                                                    LinkRoad lr = new LinkRoad();
                                                                                    lr.setId(link.getId());
                                                                                    lr.setLinkName(link.getLinkName());
                                                                                    lr.setLinkName(link.getLinkName());// yangjun
                                                                                    // add
                                                                                    lr.setMaxSpeed(link.getMaxSpeed());// yangjun
                                                                                    // add
                                                                                    lr.setMaxPer(link.getMaxPer());// yangjun
                                                                                    // add
                                                                                    lr.setStartId(link.getStartId());
                                                                                    lr.setStartIp(link.getStartIp());
                                                                                    lr.setStartIndex(link.getStartIndex());
                                                                                    lr.setStartDescr(link.getStartDescr());
                                                                                    lr.setEndIp(link.getEndIp());
                                                                                    lr.setEndId(link.getEndId());
                                                                                    lr.setEndIndex(link.getEndIndex());
                                                                                    lr.setEndDescr(link.getEndDescr());
                                                                                    lr.setAssistant(link.getAssistant());
                                                                                    lr.setType(link.getType());
                                                                                    lr.setShowinterf(link.getShowinterf());
                                                                                    PollingEngine.getInstance().getLinkList().add(lr);

                                                                                    XmlOperator xopr = new XmlOperator();
                                                                                    xopr.setFile("network.jsp");
                                                                                    xopr.init4updateXml();
                                                                                    xopr.addLine(link.getLinkName(), String.valueOf(link.getId()), "net" + String.valueOf(link.getStartId()), "net" + String.valueOf(link.getEndId()));
                                                                                    xopr.writeXml();
                                                                                }
                                                                                
                                                                            } catch (Exception e) {
                                                                                e.printStackTrace();
                                                                            } finally {
                                                                                linkdao.close();
                                                                            }

                                                                            break;
                                                                        }

                                                                    }
                                                                }
                                                            }
                                                        }

                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    } finally {

                                                    }
                                                    newNDP.add(ndp);
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                                break;
                                            }
                                        }
                                        // dbthreadPool.runTask(createMACTask(ip,node));
                                    }

                                }
                            }
                        }

                    }

                }
            }
        }
        // �Ѳɼ��������sql
        NetHostNDPRttosql ndptosql = new NetHostNDPRttosql();
        ndptosql.CreateResultTosql(newNDP, node);

        ndpVector = null;
        return returnHash;
    }

    /**
     * ����IP-MAC�ɼ�����
     */
    private static Runnable createMACTask(final String ip, final Host node) {
        return new Runnable() {
            public void run() {
                String sysOid = "";
                int deviceType = 0;
                try {
                    sysOid = SnmpUtil.getInstance().getSysOid(ip, node.getCommunity());
                    deviceType = SnmpUtil.getInstance().checkDevice(ip, node.getCommunity(), sysOid);
                    if (deviceType != 0 && deviceType < 4) {
                        // network equipment
                        List ipaliasList = new ArrayList();
                        List macList = new ArrayList();
                        List ifEntityList = SnmpUtil.getInstance().getIfEntityList(ip, node.getCommunity(), deviceType);
                        if (ifEntityList != null && ifEntityList.size() > 0) {
                            IfEntity ifEntity = new IfEntity();
                            for (int i = 0; i < ifEntityList.size(); i++) {
                                ifEntity = (IfEntity) ifEntityList.get(i);
                                if (ifEntity.getIpAddress() != null && ifEntity.getIpAddress().trim().length() > 0) {
                                    if (!ipaliasList.contains(ifEntity.getIpAddress()))
                                        ipaliasList.add(ifEntity.getIpAddress());
                                }
                                if (ifEntity.getPhysAddress() != null && ifEntity.getPhysAddress().trim().length() > 0) {
                                    if (!macList.contains(ifEntity.getPhysAddress()))
                                        macList.add(ifEntity.getPhysAddress());
                                }
                            }
                            ShareData.setMacNDP(ip + ":" + node.getIpAddress(), macList);
                            ShareData.setIpaliasNDP(ip + ":" + node.getIpAddress(), ipaliasList);
                            // gatherHash.put("ipalias", ipaliasList);
                            // gatherHash.put("maclist", macList);
                        }
                        ShareData.setIfEntityNDP(ip + ":" + node.getIpAddress(), ifEntityList);

                    }
                } catch (Exception exc) {
                    exc.printStackTrace();
                }
            }
        };
    }

}
