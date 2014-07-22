
package com.afunms.polling.task;
/*
 * created 7.26 2011
 */
import java.util.Hashtable;

import com.afunms.polling.impl.ProcessVPNData;
import com.afunms.polling.node.Host;
import com.afunms.polling.snmp.cpu.ArrayNetworkCpuSnmp;
import com.afunms.polling.snmp.vpn.ArrayVPNClusterSnmp;
import com.afunms.polling.snmp.vpn.ArrayVPNCountSnmp;
import com.afunms.polling.snmp.vpn.ArrayVPNFlowRateSnmp;
import com.afunms.polling.snmp.vpn.ArrayVPNInforSnmp;
import com.afunms.polling.snmp.vpn.ArrayVPNInterfaceSnmp;
import com.afunms.polling.snmp.vpn.ArrayVPNLogSnmp;
import com.afunms.polling.snmp.vpn.ArrayVPNSSLSnmp;
import com.afunms.polling.snmp.vpn.ArrayVPNSSLSysInforSnmp;
import com.afunms.polling.snmp.vpn.ArrayVPNSessionSnmp;
import com.afunms.polling.snmp.vpn.ArrayVPNSystemSnmp;
import com.afunms.polling.snmp.vpn.ArrayVPNTCPSnmp;
import com.afunms.polling.snmp.vpn.ArrayVPNTcsSnmp;
import com.afunms.polling.snmp.vpn.ArrayVPNVClientAppSnmp;
import com.afunms.polling.snmp.vpn.ArrayVPNVIPSnmp;
import com.afunms.polling.snmp.vpn.ArrayVPNVSSnmp;
import com.afunms.polling.snmp.vpn.ArrayVPNVirtualSiteSnmp;
import com.afunms.polling.snmp.vpn.ArrayVPNWebSnmp;


public class VPNCollectDataTask extends MonitorTask {
		

	public static void main(String [] args){
		VPNCollectDataTask vpnCollectDataTask = new VPNCollectDataTask();
		vpnCollectDataTask.run();
	}

	public void run() { 
		try{
			/*
			 * ���ýڵ���Ϣ
			 */
			Host node = new Host();
			node.setIpAddress("10.204.3.253");
			node.setCommunity("oavpn-1");
			node.setSnmpversion(1);
//			SysLogger.info("-------------------------------��ʼ�ɼ�VPN��Ϣ" +
//					"-------------------------------");
			ProcessVPNData processVPNData = new ProcessVPNData();
					/*
					 * ��Cluster�������ݲɼ�
					 */
//					ArrayVPNClusterSnmp arrayVPNClusterSnmp = new ArrayVPNClusterSnmp();
//					Hashtable returnHash = arrayVPNClusterSnmp.collect_Data(node);
//					processVPNData.saveVPNClusterData(returnHash);
//					/*
//					 * ��SSL�������ݲɼ�
//					 */
//					ArrayVPNSSLSnmp arrayVPNSSLSnmp = new ArrayVPNSSLSnmp();
//					Hashtable returnHashSSL = arrayVPNSSLSnmp.collect_Data(node);
//					processVPNData.saveVPNSSLData(returnHashSSL);
//					/*
//					 * ��session�������ݲɼ�
//					 */
//					ArrayVPNSessionSnmp arrayVPNSessionSnmp = new ArrayVPNSessionSnmp();
//					Hashtable returnHashSession = arrayVPNSessionSnmp.collect_Data(node);
//					processVPNData.saveVPNSessionData(returnHashSession);
//					/*
//					 * ��TCP�������ݲɼ� 
//					 */
//					ArrayVPNTCPSnmp arrayVPNTCPSnmp = new ArrayVPNTCPSnmp();
//					Hashtable returnHashTCP = arrayVPNTCPSnmp.collect_Data(node);
//					processVPNData.saveVPNTCPData(returnHashTCP);
//					/*
//					 * ��Interface�������ݲɼ�
//					 */
//					ArrayVPNInterfaceSnmp arrayVPNInterfaceSnmp = new ArrayVPNInterfaceSnmp();
//					Hashtable returnHashInterface = arrayVPNInterfaceSnmp.collect_Data(node);
//					processVPNData.saveVPNInterfaceData(returnHashInterface);
//					/*
//					 * ��FlowRate�������ݲɼ�
//					 */
//					ArrayVPNFlowRateSnmp arrayVPNFlowRateSnmp = new ArrayVPNFlowRateSnmp();
//					Hashtable returnHashFlowRate = arrayVPNFlowRateSnmp.collect_Data(node);
//					processVPNData.saveVPNFlowRateData(returnHashFlowRate);
//					/*
//					 * ��VirtualSite�������ݲɼ�
//					 */
//					ArrayVPNVirtualSiteSnmp arrayVPNVirtualSiteSnmp = new ArrayVPNVirtualSiteSnmp();
//					Hashtable returnHashVirtualSite = arrayVPNVirtualSiteSnmp.collect_Data(node);
//					processVPNData.saveVPNVirtualSiteData(returnHashVirtualSite);
//					/*
//					 * ��VPNInf�������ݲɼ�
//					 */
//					ArrayVPNInforSnmp arrayVPNInforSnmp = new ArrayVPNInforSnmp();
//					Hashtable returnHashVPNInf = arrayVPNInforSnmp.collect_Data(node);
//					processVPNData.saveVPNInforData(returnHashVPNInf);
//					/*
//					 * ��VPNweb�������ݲɼ�
//					 */
//					ArrayVPNWebSnmp arrayVPNWebSnmp = new ArrayVPNWebSnmp();
//					Hashtable returnHashVPNWeb= arrayVPNWebSnmp.collect_Data(node);
//					processVPNData.saveVPNWebData(returnHashVPNWeb);
//					/*
//					 * ��VPNVS�������ݲɼ�
//					 */
//					ArrayVPNVSSnmp arrayVPNVSSnmp = new ArrayVPNVSSnmp();
//					Hashtable returnHashVPNVS= arrayVPNVSSnmp.collect_Data(node);
//					processVPNData.saveVPNVSData(returnHashVPNVS);
//					/*
//					 * ��VPNVS�������ݲɼ�
//					 */
//					ArrayVPNVClientAppSnmp arrayVPNVClientAppSnmp = new ArrayVPNVClientAppSnmp();
//					Hashtable returnHashVPNVC= arrayVPNVClientAppSnmp.collect_Data(node);
//					processVPNData.saveVPNVCData(returnHashVPNVC);
//					/*
//					 * ��VPNVIP�������ݲɼ�
//					 */
//					ArrayVPNVIPSnmp arrayVPNVIPSnmp = new ArrayVPNVIPSnmp();
//					Hashtable returnHashVPNVIP= arrayVPNVIPSnmp.collect_Data(node);
//					processVPNData.saveVPNVIPData(returnHashVPNVIP);
//					/*
//					 * ��VPNLog�������ݲɼ�
//					 */ 
//					ArrayVPNLogSnmp arrayVPNLogSnmp = new ArrayVPNLogSnmp();
//					Hashtable returnHashVPNLog = arrayVPNLogSnmp.collect_Data(node);
//					processVPNData.saveVPNLogData(returnHashVPNLog);
//					/*
//					 * ��count�������ݲɼ�
//					 */
//					ArrayVPNCountSnmp arrayVPNCountSnmp = new ArrayVPNCountSnmp();
//					Hashtable returnHashVPNCount = arrayVPNCountSnmp.collect_Data(node);
//					processVPNData.saveVPNCountData(returnHashVPNCount);
//					/*
//					 * ��TCS�������ݲɼ�
//					 */
//					ArrayVPNTcsSnmp arrayVPNTcsSnmp = new ArrayVPNTcsSnmp();
//					Hashtable returnHashVPNTcs = arrayVPNTcsSnmp.collect_Data(node);
//					processVPNData.saveVPNTCSData(returnHashVPNTcs);
//					/*
//					 * ��System�������ݲɼ�
//					 */
//					ArrayVPNSystemSnmp arrayVPNSystemSnmp = new ArrayVPNSystemSnmp();
//					Hashtable returnHashVPNSystem = arrayVPNSystemSnmp.collect_Data(node);
//					processVPNData.saveVPNSystemData(returnHashVPNSystem);
//					/*
//					 * ��SSLSysInfor�������ݲɼ�
//					 */
//					ArrayVPNSSLSysInforSnmp arrayVPNSSLSysInforSnmp = new ArrayVPNSSLSysInforSnmp();
//					Hashtable returnHashVPNSSLSysInfor = arrayVPNSSLSysInforSnmp.collect_Data(node);
//					processVPNData.saveVPNSSLSysInforData(returnHashVPNSSLSysInfor);
//					/*
//					 * ��cpu�������ݲɼ�
//					 */
//					ArrayNetworkCpuSnmp arrayNetworkCpuSnmp = new ArrayNetworkCpuSnmp();
//					Hashtable returnHashVPNCpu = arrayNetworkCpuSnmp.collect_Data(node);
//					processVPNData.saveVPNCpuData(returnHashVPNCpu);

					
			
			 
		}catch(Exception e){					 	
			e.printStackTrace();
		}
			finally{
//			SysLogger.info("-------------------------------VPN �ɼ������Ѿ���� : " +
//					"-------------------------------"
//					+Thread.activeCount()
//					);
		}
				
	}
	
	

	
}
