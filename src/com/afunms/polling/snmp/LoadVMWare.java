package com.afunms.polling.snmp;

/*
 * @author hukelei@dhcc.com.cn
 *
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.afunms.alarm.dao.AlarmIndicatorsNodeDao;
import com.afunms.alarm.model.AlarmIndicatorsNode;
import com.afunms.alarm.util.AlarmConstant;
import com.afunms.alarm.util.AlarmIndicatorsUtil;
import com.afunms.common.util.CheckEventUtil;
import com.afunms.common.util.DBManager;
import com.afunms.common.util.EncryptUtil;
import com.afunms.common.util.ShareData;
import com.afunms.common.util.SysLogger;
import com.afunms.common.util.SysUtil;
import com.afunms.common.util.SystemConstant;
import com.afunms.config.model.Diskconfig;
import com.afunms.config.model.Nodeconfig;
import com.afunms.event.dao.SmscontentDao;
import com.afunms.event.model.Smscontent;
import com.afunms.indicators.model.NodeDTO;
import com.afunms.indicators.model.NodeGatherIndicators;
import com.afunms.indicators.util.NodeUtil;
import com.afunms.initialize.ResourceCenter;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.node.Host;
import com.afunms.polling.om.CPUcollectdata;
import com.afunms.polling.om.Diskcollectdata;
import com.afunms.polling.om.Interfacecollectdata;
import com.afunms.polling.om.Processcollectdata;
import com.afunms.polling.om.Systemcollectdata;
import com.afunms.polling.om.Usercollectdata;
import com.afunms.polling.om.VMWareConnectConfig;
import com.afunms.polling.om.VMWareVid;
import com.afunms.system.util.TimeGratherConfigUtil;
import com.afunms.topology.dao.VMWareConnectConfigDao;
import com.afunms.topology.dao.VMWareVidDao;
import com.afunms.vmware.vim25.cache.VIMCache;
import com.afunms.vmware.vim25.common.VIMMgr;
import com.afunms.vmware.vim25.constants.VIMConstants;
import com.afunms.vmware.vim25.constants.VMConstants;
import com.afunms.vmware.vim25.mgr.HostMgr;
import com.afunms.vmware.vim25.mgr.VMMgr;
import com.afunms.vmware.vim25.vo.VMWareDao;
import com.afunms.vmware.vim25.vo.VMWareVO;
import com.gatherResulttosql.HostDatatempAllutilhdxRtosql;
import com.gatherResulttosql.HostDatatempCollecttimeRtosql;
import com.gatherResulttosql.HostDatatempDiskRttosql;
import com.gatherResulttosql.HostDatatempNodeconfRtosql;
import com.gatherResulttosql.HostDatatempProcessRtTosql;
import com.gatherResulttosql.HostDatatempUserRtosql;
import com.gatherResulttosql.HostDatatempinterfaceRtosql;
import com.gatherResulttosql.HostDatatemputilhdxRtosql;
import com.gatherResulttosql.HostdiskResultosql;
import com.gatherResulttosql.NetHostDatatempCpuRTosql;
import com.gatherResulttosql.NetHostDatatempSystemRttosql;
import com.gatherdb.GathersqlListManager;
import com.vmware.vim25.PerfCounterInfo;
import com.vmware.vim25.PerfEntityMetricBase;
import com.vmware.vim25.PerfEntityMetricCSV;
import com.vmware.vim25.PerfMetricSeriesCSV;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class LoadVMWare {
	/**
	 * @param hostname
	 */
	private String ipaddress;
	 private Hashtable sendeddata = ShareData.getProcsendeddata();

	 java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public LoadVMWare(String ipaddress) {
		this.ipaddress = ipaddress;
	}
	
	
	public LoadVMWare() {
		
	}
	//NodeGatherIndicators alarmIndicatorsNode
	public Hashtable collect_Data(NodeGatherIndicators alarmIndicatorsNode)
    {
		Hashtable returnHash = new Hashtable();
		Host host = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(alarmIndicatorsNode.getNodeid()));//Integer.parseInt(alarmIndicatorsNode.getNodeid())
		SysLogger.info("######## "+host.getIpAddress()+" ��ʼ�ɼ�VMWare����##########");
		if(host == null)return returnHash;
		//�ж��Ƿ��ڲɼ�ʱ�����
    	if(ShareData.getTimegatherhash() != null){
    		if(ShareData.getTimegatherhash().containsKey(host.getId()+":equipment")){
    			TimeGratherConfigUtil timeconfig = new TimeGratherConfigUtil();
    			int _result = 0;
    			_result = timeconfig.isBetween((List)ShareData.getTimegatherhash().get(host.getId()+":equipment"));
    			if(_result ==1 ){
    				//SysLogger.info("########ʱ�����: ��ʼ�ɼ� "+node.getIpAddress()+" PING������Ϣ##########");
    			}else if(_result == 2){
    				//SysLogger.info("########ȫ��: ��ʼ�ɼ� "+node.getIpAddress()+" PING������Ϣ##########");
    			}else {
    				SysLogger.info("######## "+host.getIpAddress()+" ���ڲɼ�VMWareʱ�����,�˳�##########");
    				//���֮ǰ�ڴ��в����ĸ澯��Ϣ
//    			    try{
//    			    	//���֮ǰ�ڴ��в������ڴ�澯��Ϣ
//						CheckEventUtil checkutil = new CheckEventUtil();
//						List delList = new ArrayList();
//						delList.add(host.getId()+":host:iowait");
//						delList.add(host.getId()+":host:diskperc");
//						delList.add(host.getId()+":host:cpu");
//						delList.add(host.getId()+":host:physicalmemory");
//						delList.add(host.getId()+":host:swapmemory");						
//						checkutil.deleteEvents(delList);
//    			    }catch(Exception e){
//    			    	e.printStackTrace();
//    			    }
    				return returnHash;
    			}
    			
    		}
    	}
		
		//yangjun
		
		ipaddress=host.getIpAddress();
		
		Hashtable ipAllData = (Hashtable)ShareData.getSharedata().get(ipaddress);
		if(ipAllData == null)ipAllData = new Hashtable();
		//
		
		
		//DBManager dbm = new DBManager();
		
		//���ڴ��л�ȡ����ǰVMWare��vid�б�
		ArrayList<String> vidlist = new ArrayList();
		
		//VMConstants
		double overallcpuusage = 0;		// �����ĵ�����CPU,��λΪMHZ,SUMMARY_RESOURCE_OVERALLCPUUSAGE
		double memorysizemb = 0;		// �ڴ�,��λΪMB,SUMMARY_COMMON_MEMORYSIZEMB
		double memoryoverhead = 0;		// �ڴ濪��,��λΪMB,SUMMARY_COMMON_MEMORYOVERHEAD
		double hostmemoryusage = 0;		// �����ĵ������ڴ�,��λΪMB,SUMMARY_RESOURCE_HOSTMEMORYUSAGE
		String guesthost = "";				// ����,SUMMARY_COMMON_HOST
		String guestfullname = "";		// �ͻ�������ϵͳ,SUMMARY_COMMON_GUESTFULLNAME
		String powerstate = "";			// ��Դ״̬,SUMMARY_COMMON_POWERSTATE
		int numcpu = 0;					// cpu����,SUMMARY_COMMON_CPU
		double guestmemoryusage = 0;	// ��ͻ����ڴ�,��λΪMB,SUMMARY_RESOURCE_GUESTMEMORYUSAGE
		double storagecommitted = 0;	// ��ʹ�õĴ洢,��λΪGB,SUMMARY_RESOURCE_STORAGE_COMMITTED
		double storageunshared = 0;		// δ����Ĵ洢,��λΪGB,SUMMARY_RESOURCE_STORAGE_UNSHARED
		double storageall = 0;			// �ñ��Ĵ洢,��λΪGB,SUMMARY_RESOURCE_STORAGE_ALL
		String vmwaretools = "";		// ����״̬,SUMMARY_COMMON_VMWARETOOLS
		String hostname = "";			// DNS����,SUMMARY_COMMON_HOSTNAME
		//String ipaddress = "";			// IP��ַ,SUMMARY_COMMON_IPADDRESS
		String version = "";			// ������汾,SUMMARY_COMMON_VERSION
		//VIMConstants
		//�洢��
		double dscapacity = 0;			// ����,��λΪGB,SUMMARY_RESOURCE_DS_CAPACITY
		String dstime = "";				// �ϴθ���ʱ��,��ʽ��2012-10-17 09:42:32,SUMMARY_RESOURCE_DS_TIME,
		double dsfreespace = 0;			// ���û��ռ�,��λΪGB,SUMMARY_RESOURCE_DS_FREESPACE
		String dsname = "";				// ��������,SUMMARY_RESOURCE_DS_NAME
		String dsalarmactions = "";		// ��������,true,SUMMARY_RESOURCE_DS_ALARMACTIONS
		String dsstatus = "";			// ����״̬,SUMMARY_RESOURCE_DS_STATUS
		//����
		String netname = "";			// ������,SUMMARY_RESOURCE_NET_NAME
		String netstatus = "";			// ����״̬,SUMMARY_RESOURCE_NET_STATUS
		String netalarmactions = "";	// ��������,true,SUMMARY_RESOURCE_NET_ALARMACTIONS
		
		String vid = ""; 
		String sql = ""; 
		
		String url = "https://"+host.getIpAddress()+"/sdk"; // URL
		String username = "";// �˻�
		String password = "";// ����
//		System.out.println(ShareData.getVmwareConfig()+"------------------------"+ShareData.getVmwareConfig().containsKey(host.getId()+""));
		if(ShareData.getVmwareConfig() != null && ShareData.getVmwareConfig().containsKey(host.getId()+"")){
			VMWareConnectConfig vmwareConfig = (VMWareConnectConfig)ShareData.getVmwareConfig().get(host.getId()+"");
			username = vmwareConfig.getUsername(); 
			password = vmwareConfig.getPwd(); 
		}
		if(username.equals("") && password.equals("")){
			VMWareConnectConfigDao condao = new VMWareConnectConfigDao();
			VMWareConnectConfig vo = (VMWareConnectConfig) condao.getbynodeid(Long.parseLong(host.getId()+"")).get(0);
		    username = vo.getUsername();
		    password = vo.getPwd();
		}
		String enpassword = "";
		try{
			if(!password.equals(""))
			{
				enpassword = EncryptUtil.decode(password);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		

//		sql = "select vm.VID from oss_virtualmachine vm ";
//		res = dbm.executeQuery(sql);
//		try {
//			while (res.next()) {
//				vidlist.add(res.getString(1));
//			}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				res.close();
//			} catch (Exception e) {
//			}
//			dbm.close();
//		}
		
//		WMWareUtil vmwareutil = new WMWareUtil();
//		HashMap vmids = new HashMap();
//		try{
//			SysLogger.info(host.getIpAddress()+"   username:"+username+"    password:"+password);
//			vmids = (HashMap)vmwareutil.SyncVM(host.getIpAddress(), username, password);
//			if(vmids != null && vmids.size()>0){
//				Iterator iterator = vmids.keySet().iterator();
//				while (iterator.hasNext()) {
//					String vmid = (String) iterator.next();
//					String guestname = (String)vmids.get(vmid);
//					SysLogger.info(vmid+" ##############======="+guestname);
//				}
//			}
//			SysLogger.info("================�����ɼ�VMWARE"+host.getIpAddress());
//			
//		}catch(Exception e){
//			e.printStackTrace();
//		}

		VMWareVidDao vmwareviddao = new VMWareVidDao();
		try{
			List vidvolist = vmwareviddao.getbynodeid(Long.parseLong(host.getId()+""));
			if(vidvolist != null && vidvolist.size()>0){
				for(int k=0;k<vidvolist.size();k++){
					VMWareVid vmvid = (VMWareVid)vidvolist.get(k);
					vidlist.add(vmvid.getVid());
				}
			}
		}catch(Exception e){
			
		}finally{
			vmwareviddao.close();
		}
//		for (int i = 0; i < vidlist.size(); i++) {
			
//			vid = vidlist.get(i);
			SysLogger.info("begin collect vid:"+vid+"  xinxi =============host.getId()"+host.getId());
			// ���ժҪ��Ϣ��
			SysLogger.info(url+"====username:"+username+"===enpassword:"+enpassword+"===vid:"+vid);
//			HashMap<String, Object> summaryresultMap = (HashMap<String, Object>)VIMMgr.syncVIMObjs(url, username, enpassword);
//			
//			if(!(ShareData.getSharedata().containsKey(host.getIpAddress())))
//				{
//					if(ipAllData == null)ipAllData = new Hashtable();
//					if(summaryresultMap != null && summaryresultMap.size()>0)ipAllData.put("summaryresultMap",summaryresultMap);
//				    ShareData.getSharedata().put(host.getIpAddress(), ipAllData);
//				}else
//				 {
//					if(summaryresultMap != null && summaryresultMap.size()>0)((Hashtable)ShareData.getSharedata().get(host.getIpAddress())).put("summaryresultMap",summaryresultMap);
//				 }
//			Map<String, Object> summaryresultMap1 = null;
//			if(ipAllData != null){
//		    	try {
//		    		summaryresultMap1 = (Map<String, Object>)ipAllData.get("summaryresultMap");
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				ArrayList<HashMap<String, Object>> dslist = (ArrayList<HashMap<String, Object>>) summaryresultMap.get("Datastore");
//		    	ArrayList<HashMap<String, Object>> wulist = (ArrayList<HashMap<String, Object>>) summaryresultMap.get("HostSystem");
//		    	ArrayList<HashMap<String, Object>> rplist = (ArrayList<HashMap<String, Object>>) summaryresultMap.get("ResourcePool");
//		    	ArrayList<HashMap<String, Object>> vmlist = (ArrayList<HashMap<String, Object>>) summaryresultMap.get("VirtualMachine");
//		    	ArrayList<HashMap<String, Object>> dclist = (ArrayList<HashMap<String, Object>>) summaryresultMap.get("Datacenter");
//		    	ArrayList<HashMap<String, Object>> crlist = (ArrayList<HashMap<String, Object>>) summaryresultMap.get("ComputeResource");//Ⱥ��
//		    	System.out.println("------------"+wulist);
//		    	System.out.println("------------"+vmlist);
//		    	System.out.println("------------"+dslist);
//		    	System.out.println("------------"+rplist);
//		    	System.out.println("------------"+crlist);
//		    	System.out.println("------------"+dclist);
//		    	//�����������ӻ�����Ϣ
//		    	VMWareVidDao vm_vid = new VMWareVidDao();
//		    	List ds_vid = new ArrayList();//���Ϸŵ��ǣ�֮ǰflag=1�ȱ��ɼ���vid
//		    	List wu_vid = new ArrayList();
//		    	List vmware_vid = new ArrayList();
//		    	List rp_vid = new ArrayList();
//		    	List cr_vid = new ArrayList();
//		    	List all_vid = new ArrayList();//��Ÿýڵ����е�vid���������ж��Ƿ��ǵ�һ�βɼ�
//		    	String nodeid = host.getId()+"";
//		    	String ip = ipaddress.replace(".", "_");
//		    	all_vid = vm_vid.getbynodeid(Long.parseLong(host.getId()+""));
//		    	if(all_vid.size()==0){
//		    		this.savePhysical(wulist, nodeid);
//		    		this.saveVmware(vmlist, nodeid);
//		    		this.saveDatastore(dslist, nodeid);
//		    		this.saveResourcepool(rplist, nodeid);
//		    		this.saveDatacenter(dclist, nodeid);
//		    		this.saveYun(crlist, nodeid);
//		    	}else if(all_vid.size()>0){
//		    		List flag_vid = new ArrayList();
//		    		VMWareVidDao viddao = new VMWareVidDao();
//		    		flag_vid = viddao.queryVid(host.getId()+"");//���flag=0��  ���ݵ�vid
//		    		List sql_list = new ArrayList();
//		    		sql_list.add("delete from nms_vmwarevid where nodeid = "+nodeid);
//		    		sql_list.add("truncate table vm_basephysical"+ip);
//		    		sql_list.add("truncate table vm_basevmware"+ip);
//		    		sql_list.add("truncate table vm_basedatastore"+ip);
//		    		sql_list.add("truncate table vm_basedatacenter"+ip);
//		    		sql_list.add("truncate table vm_baseresource"+ip);
//		    		sql_list.add("truncate table vm_baseyun"+ip);
//		    		VMWareDao vmdao = new VMWareDao();
//		    		vmdao.save(sql_list);//�������������ݣ�Ϊ���²�������⡣
//		    		this.savePhysical(wulist, nodeid);
//		    		this.saveVmware(vmlist, nodeid);
//		    		this.saveDatastore(dslist, nodeid);
//		    		this.saveResourcepool(rplist, nodeid);
//		    		this.saveDatacenter(dclist, nodeid);
//		    		this.saveYun(crlist, nodeid);
//		    		viddao.updateVidFlag(flag_vid, nodeid);
//		    	}
		    	
		    	
		    	
		    	
		    	
		    	
		    	
		    	
		    	
		    	
		    	
		    	
		    	
		    	
		    	HostMgr host_list = new HostMgr();
		    	VIMMgr vm = new VIMMgr();
//		    	String vid1 = "";
//				String vtype = VIMConstants.RESOURCE_HO;
				String perfIntervalKey = VIMConstants.PERFINTERVAL_DAY;
				String perfCounterKeys[] = { "cpu.usage.average",
				"cpu.ready.summation", "mem.vmmemctl.average",
				"mem.swapoutRate.average", "mem.swapinRate.average",
				"cpu.usagemhz.average", "mem.usage.average",
				"disk.usage.average", "net.usage.average" };
				Calendar endTime = Calendar.getInstance();
				Hashtable vmData =null;
				
				int size=0;
		        HashMap<String, Object> map_vm = new HashMap<String, Object>();
		        HashMap<String, Object> map_per = new HashMap<String, Object>();
		        HashMap<String, Object> wuliji = new HashMap<String, Object>();
		        HashMap<String, Object> vmware = new HashMap<String, Object>();
		        List list_per = new ArrayList();
		        List list_vm = new ArrayList();
		        HashMap resultMap1 = new HashMap();
				HashMap resultMap2 = new HashMap();
		        VMWareVidDao vmdao = new VMWareVidDao();
		        List wu_list = vmdao.queryVidFlag("physical", host.getId()+"", "");
		        vmdao.close();
		    	if(wu_list != null && wu_list.size()>0){
		        	for(int i=0;i<wu_list.size();i++){
		        		 try
		        		   {
		        			 resultMap1 = (HashMap<String, Object>) VIMMgr.getPerformances(url,username, enpassword, VIMConstants.RESOURCE_HO,wu_list.get(i).toString(), perfIntervalKey, perfCounterKeys, endTime);
		        		   }catch(Exception e){
		        			   e.printStackTrace();
		        		   }
						   List valuelist = new ArrayList();
						   if ("success".equals(resultMap1.get(VIMConstants.INFO_STATE))) {
								PerfEntityMetricBase[] perfEntityMetricBases1 = (PerfEntityMetricBase[]) resultMap1.get(VIMConstants.INFO_RESULT);
								// �����е�����ָ�����Map,��perfCounterInfo.getKeyΪMap��key
								HashMap<Integer, PerfCounterInfo> perfCounterInfoMap = new HashMap<Integer, PerfCounterInfo>();
								for (int h = 0; h < perfCounterKeys.length; h++) {
									PerfCounterInfo perfCounterInfo1 = VIMCache.getInstance(url, username, enpassword).getPerfCounterInfo(perfCounterKeys[h]);
									perfCounterInfoMap.put(perfCounterInfo1.getKey(),perfCounterInfo1);
								}
								if (null != perfEntityMetricBases1) {
									for (PerfEntityMetricBase perfEntityMetricBase : perfEntityMetricBases1) {
										PerfEntityMetricCSV csvMetric = (PerfEntityMetricCSV) perfEntityMetricBase;
										PerfMetricSeriesCSV[] perfMetricSeriesCSVs = csvMetric.getValue();
										if (null != perfMetricSeriesCSVs) {
											for (PerfMetricSeriesCSV perfMetricSeriesCSV : perfMetricSeriesCSVs) {
												int counterId = perfMetricSeriesCSV.getId().getCounterId();
												PerfCounterInfo perfCounterInfo = perfCounterInfoMap.get(counterId);
												String value = perfMetricSeriesCSV.getValue();
												String kpi = counterId + "";
												String[] str = value.split(",");
												// �ж�value�����Ƿ����288���������ȡ288����Ȼȡvaluelist�ĳ���
												if (str.length > 288) {
													valuelist.add(str[277]);
												} else {
													valuelist.add(str[str.length-1]);
												}
											}
									}
								 }
								}
								
								map_per.put(wu_list.get(i).toString(), perfEntityMetricBases1);
				        		//����������������
		                        this.CreateResultTosqlHost(map_per, host.getIpAddress(),wu_list.get(i).toString());	
						   }
						
						
						
					    vmdao = new VMWareVidDao();	        		
//		        	    System.out.println(wulist.get(i).get("vid").toString()+"<------���������--------->"+vm.getPerformances(url, username, password, vtype, wulist.get(i).get("vid").toString(), perfIntervalKey, perfCounterKeys, endTime).get("info.result"));
                        List vm_list = vmdao.queryVidFlag("vmware", host.getId()+"", wu_list.get(i).toString());
                        vmdao.close();
                        if(vm_list != null && vm_list.size()>0){
		        	       for(int j=0;j<vm_list.size();j++){
//		        	    	 if(wulist.get(i).get("vid").toString().equals(vmlist.get(j).get("hoid").toString())){
//		        	    	 PerfEntityMetricBase[] perfEntityMetricBases = (PerfEntityMetricBase[]) vm.getPerformances(url, username, enpassword, VIMConstants.RESOURCE_VM, vm_list.get(j).toString(), perfIntervalKey, perfCounterKeys, endTime).get("info.result");
                                 
																 

		        	   resultMap2 = (HashMap<String, Object>) VIMMgr.getPerformances(url,username, enpassword, VIMConstants.RESOURCE_VM,vm_list.get(j).toString(), perfIntervalKey, perfCounterKeys, endTime);
					   List valuelist1 = new ArrayList();
					   if ("success".equals(resultMap2.get(VIMConstants.INFO_STATE))) {
							PerfEntityMetricBase[] perfEntityMetricBases = (PerfEntityMetricBase[]) resultMap2.get(VIMConstants.INFO_RESULT);
							
							      vmware.put(vm_list.get(j).toString(),perfEntityMetricBases);
		        	    		//����������������
		                         this.CreateResultTosqlGuesthost(vmware, host.getIpAddress(),wu_list.get(i).toString(),vm_list.get(j).toString());	
		        	    		 list_vm.add(perfEntityMetricBases);
				                }

		        	      }
		        	    }
                        
                        vmware.put("vm", vmware);
		        	    vmware.put(wu_list.get(i).toString(), list_vm);
		        	    
		        	}
		        	wuliji.put("wuliji_per", map_per);
		        	if(!(ShareData.getVmdata().containsKey(host.getIpAddress())))
					{
						if(vmData == null)vmData = new Hashtable();
						if(wuliji != null && wuliji.size()>0)vmData.put("wuliji",wuliji);vmData.put("vmware", vmware);
					    ShareData.getVmdata().put(host.getIpAddress(), vmData);
					}else
					 {
						if(wuliji != null && wuliji.size()>0)((Hashtable)ShareData.getVmdata().get(host.getIpAddress())).put("wuliji",wuliji);
						((Hashtable)ShareData.getVmdata().get(host.getIpAddress())).put("vmware",vmware);
					 }
		        	//ȡ��ʱ��ͨ���������vid����ֵȡ
		        	//�澯
		        }
		    	 //���д��̸澯���
			    SysLogger.info("### ��ʼ���м��VMWare�Ƿ�澯### ... ###");
			    try{
					List list = this.VMgetAlarmInicatorsThresholdForNode(String.valueOf(alarmIndicatorsNode.getNodeid()), AlarmConstant.TYPE_VIRTUAL, "vmware","physical");
					
					
					for(int i = 0 ; i < list.size() ; i ++){
						AlarmIndicatorsNode alarmIndicatorsnode = (AlarmIndicatorsNode)list.get(i);
						String event_vid = "";
						String name = "";
						if(alarmIndicatorsnode.getEnabled().equals("1")){
							event_vid = alarmIndicatorsnode.getSubentity();
							VMWareVidDao queryname = new VMWareVidDao();
							name = queryname.queryName(host.getId()+"", event_vid);
							queryname.close();
						}else{
							continue;
						}
//						SysLogger.info("alarmIndicatorsnode name ======"+alarmIndicatorsnode.getName());
						if(alarmIndicatorsnode.getName().equalsIgnoreCase("cpu")){
						    this.checkDisk(host,event_vid,alarmIndicatorsnode,"physical","cpu","cpu",name);
						}
						if(alarmIndicatorsnode.getName().equalsIgnoreCase("cpuuse")){
						    this.checkDisk(host,event_vid,alarmIndicatorsnode,"physical","cpuuse","cpuuse",name);
						}
						if(alarmIndicatorsnode.getName().equalsIgnoreCase("mem")){
						    this.checkDisk(host,event_vid,alarmIndicatorsnode,"physical","mem","mem",name);
						}
						if(alarmIndicatorsnode.getName().equalsIgnoreCase("memin")){
						    this.checkDisk(host,event_vid,alarmIndicatorsnode,"physical","memin","memin",name);
						}
						if(alarmIndicatorsnode.getName().equalsIgnoreCase("memout")){
						    this.checkDisk(host,event_vid,alarmIndicatorsnode,"physical","memout","memout",name);
						}
						if(alarmIndicatorsnode.getName().equalsIgnoreCase("meminc")){
						    this.checkDisk(host,event_vid,alarmIndicatorsnode,"physical","meminc","meminc",name);
						}
					}
			    }catch(Exception e){
			    	e.printStackTrace();
			    }
			    
			    //������澯
			    try{
					List list = this.VMgetAlarmInicatorsThresholdForNode(String.valueOf(alarmIndicatorsNode.getNodeid()), AlarmConstant.TYPE_VIRTUAL, "vmware","vmware");
//					VMWareVidDao vmdao1 = new VMWareVidDao();
//					List vm_list = vmdao1.queryVMVid( host.getId(), "vmware");
//					System.out.println("---------��ѯ�澯ָ���--------"+list.size());
					
					for(int i = 0 ; i < list.size() ; i ++){
						AlarmIndicatorsNode alarmIndicatorsnode = (AlarmIndicatorsNode)list.get(i);
						String event_vid = "";
						String name="";
						if(alarmIndicatorsnode.getEnabled().equals("1")){
							event_vid = alarmIndicatorsnode.getSubentity();
							VMWareVidDao queryname = new VMWareVidDao();
							name = queryname.queryName(host.getId()+"", event_vid);
							queryname.close();
						}else{
							continue;
						}
//						SysLogger.info("alarmIndicatorsnode name ======"+alarmIndicatorsnode.getName());
						if(alarmIndicatorsnode.getName().equalsIgnoreCase("cpu")){
						    this.checkDisk(host,event_vid,alarmIndicatorsnode,"vmware","cpu","cpu",name);
						}
						if(alarmIndicatorsnode.getName().equalsIgnoreCase("cpuuse")){
						    this.checkDisk(host,event_vid,alarmIndicatorsnode,"vmware","cpuuse","cpuuse",name);
						}
						if(alarmIndicatorsnode.getName().equalsIgnoreCase("mem")){
						    this.checkDisk(host,event_vid,alarmIndicatorsnode,"vmware","mem","mem",name);
						}
						if(alarmIndicatorsnode.getName().equalsIgnoreCase("memin")){
						    this.checkDisk(host,event_vid,alarmIndicatorsnode,"vmware","memin","memin",name);
						}
						if(alarmIndicatorsnode.getName().equalsIgnoreCase("memout")){
						    this.checkDisk(host,event_vid,alarmIndicatorsnode,"vmware","memout","memout",name);
						}
						if(alarmIndicatorsnode.getName().equalsIgnoreCase("meminc")){
						    this.checkDisk(host,event_vid,alarmIndicatorsnode,"vmware","meminc","meminc",name);
						}
						if(alarmIndicatorsnode.getName().equalsIgnoreCase("net")){
						    this.checkDisk(host,event_vid,alarmIndicatorsnode,"vmware","net","net",name);
						}
					}
			    }catch(Exception e){
			    	e.printStackTrace();
			    }
			    
			    
			    
		    	
			  // ----------------------------�洢
		    	try{
//		       System.out.println("//----------------------------�洢");
		       HashMap ds_map = new HashMap();
		       String cunchu_perfCounterKeys[] = { "disk.used.latest",
				"disk.provisioned.latest","disk.capacity.latest" };
		       HashMap resultMap = new HashMap();
		       VMWareVidDao ds_vm = new VMWareVidDao();
		       List ds_list = ds_vm.queryVidFlag("datastore", host.getId()+"", "");
		       ds_vm.close();
			   if(ds_list != null && ds_list.size()>0){
				   for(int i =0;i<ds_list.size();i++){
//					   PerfEntityMetricBase[] perfEntityMetricBases = (PerfEntityMetricBase[]) vm.getPerformances(url, username, password, VIMConstants.RESOURCE_DS, dslist.get(i).get("vid").toString(), perfIntervalKey, cunchu_perfCounterKeys, endTime).get("info.result");
					   List valuelist = new ArrayList();
					   resultMap = (HashMap<String, Object>) VIMMgr.getPerformances(url,username, enpassword, VIMConstants.RESOURCE_DC_DATASTORE,ds_list.get(i).toString(), perfIntervalKey, cunchu_perfCounterKeys, endTime);
					   System.out.println("success-----------"+("success".equals(resultMap.get(VIMConstants.INFO_STATE))));
					if ("success".equals(resultMap.get(VIMConstants.INFO_STATE))) {
						PerfEntityMetricBase[] perfEntityMetricBases = (PerfEntityMetricBase[]) resultMap.get(VIMConstants.INFO_RESULT);
						// �����е�����ָ�����Map,��perfCounterInfo.getKeyΪMap��key
						HashMap<Integer, PerfCounterInfo> perfCounterInfoMap = new HashMap<Integer, PerfCounterInfo>();
						for (int j = 0; j < cunchu_perfCounterKeys.length; j++) {
							PerfCounterInfo perfCounterInfo = VIMCache.getInstance(url, username, enpassword).getPerfCounterInfo(cunchu_perfCounterKeys[j]);
							perfCounterInfoMap.put(perfCounterInfo.getKey(),perfCounterInfo);
						}
//                        List valuelist = new ArrayList();
						if (null != perfEntityMetricBases) {
							for (PerfEntityMetricBase perfEntityMetricBase : perfEntityMetricBases) {
								PerfEntityMetricCSV csvMetric = (PerfEntityMetricCSV) perfEntityMetricBase;
								PerfMetricSeriesCSV[] perfMetricSeriesCSVs = csvMetric.getValue();
//								ds_map.put(dslist.get(i).get("vid").toString(), perfMetricSeriesCSVs);
								if (null != perfMetricSeriesCSVs) {
									for (PerfMetricSeriesCSV perfMetricSeriesCSV : perfMetricSeriesCSVs) {
										int counterId = perfMetricSeriesCSV.getId().getCounterId();
										PerfCounterInfo perfCounterInfo = perfCounterInfoMap.get(counterId);
										String value = perfMetricSeriesCSV.getValue();
//										System.out.println("=================perfCounterInfo="+ perfCounterInfo.getKey());
//										System.out.println("=================value="+ value);
										String kpi = counterId + "";
										String[] str = value.split(",");

										// �ж�value�����Ƿ����288���������ȡ288����Ȼȡvaluelist�ĳ���
										if (str.length > 288) {
											valuelist.add(str[277]);
										} else {
											valuelist.add(str[str.length-1]);
										}
//										System.out.println("-----�洢��Ϣ-------");
//									    System.out.print("id:"+kpi+"--");
//									    System.out.print(str[size]);
					   
									}
							}
						}
					   
					   
				   }
//				   vmData.put("ds", ds_map);
			   }
					System.out.println("valuelist-----------------"+valuelist.size());
					this.CreateResultTosqlDS(valuelist,  host.getIpAddress(), ds_list.get(i).toString());
					ds_map.put(ds_list.get(i).toString(), valuelist);
				   }
			   if(!(ShareData.getVmdata().containsKey(host.getIpAddress())))
				{
					if(vmData == null)vmData = new Hashtable();
					if(ds_list != null && ds_list.size()>0)vmData.put("ds", ds_map);
				    ShareData.getVmdata().put(host.getIpAddress(), vmData);
				}else
				 {
					if(ds_list != null && ds_list.size()>0)((Hashtable)ShareData.getVmdata().get(host.getIpAddress())).put("ds",ds_map);
				 }
			   }
		    	}catch(Exception e){
		    		e.printStackTrace();
		    	}
			 //----------------------------Ⱥ��
		    try{
//		    	 System.out.println("//----------------------------Ⱥ��");
		    	 String quji_perfCounterKeys[] = { "cpu.usagemhz.average","mem.consumed.average" ,"cpu.totalmhz.average","mem.totalmb.average" };
			   HashMap cr_map = new HashMap();
			   HashMap resultMap = new HashMap();
			   VMWareVidDao cr_vm = new VMWareVidDao();
		       List cr_list = cr_vm.queryVidFlag("yun", host.getId()+"", "");
		       cr_vm.close();
			   if(cr_list != null && cr_list.size()>0){
				   for(int i =0;i<cr_list.size();i++){
//					   PerfEntityMetricBase[] perfEntityMetricBases = (PerfEntityMetricBase[]) vm.getPerformances(url, username, password, VIMConstants.RESOURCE_CCR, crlist.get(i).get("vid").toString(), perfIntervalKey, quji_perfCounterKeys, endTime).get("info.result");
//					   cr_map.put(crlist.get(i).get("vid").toString(), perfEntityMetricBases);
//					   SysLogger.info("----------�ɼ���Ⱥ-----------"+perfEntityMetricBases);
					   List valuelist = new ArrayList();
					   resultMap = (HashMap<String, Object>) VIMMgr.getPerformances(url,username, enpassword, VIMConstants.RESOURCE_CCR,cr_list.get(i).toString(), perfIntervalKey, quji_perfCounterKeys, endTime);
						if ("success".equals(resultMap.get(VIMConstants.INFO_STATE))) {
							PerfEntityMetricBase[] perfEntityMetricBases = (PerfEntityMetricBase[]) resultMap.get(VIMConstants.INFO_RESULT);
							// �����е�����ָ�����Map,��perfCounterInfo.getKeyΪMap��key
							HashMap<Integer, PerfCounterInfo> perfCounterInfoMap = new HashMap<Integer, PerfCounterInfo>();
							for (int j = 0; j < quji_perfCounterKeys.length; j++) {
								PerfCounterInfo perfCounterInfo = VIMCache.getInstance(url, username, enpassword).getPerfCounterInfo(quji_perfCounterKeys[j]);
								perfCounterInfoMap.put(perfCounterInfo.getKey(),perfCounterInfo);
							}
//	                        List valuelist = new ArrayList();
							if (null != perfEntityMetricBases) {
								for (PerfEntityMetricBase perfEntityMetricBase : perfEntityMetricBases) {
									PerfEntityMetricCSV csvMetric = (PerfEntityMetricCSV) perfEntityMetricBase;
									PerfMetricSeriesCSV[] perfMetricSeriesCSVs = csvMetric.getValue();
//									cr_map.put(crlist.get(i).get("vid").toString(), perfMetricSeriesCSVs);
									if (null != perfMetricSeriesCSVs) {
										for (PerfMetricSeriesCSV perfMetricSeriesCSV : perfMetricSeriesCSVs) {
											int counterId = perfMetricSeriesCSV.getId().getCounterId();
											PerfCounterInfo perfCounterInfo = perfCounterInfoMap.get(counterId);
											String value = perfMetricSeriesCSV.getValue();
//											System.out.println("=================perfCounterInfo="+ perfCounterInfo.getKey());
//											System.out.println("=================value="+ value);
											String kpi = counterId + "";
											String[] str = value.split(",");

											// �ж�value�����Ƿ����288���������ȡ288����Ȼȡvaluelist�ĳ���
											if (str.length > 288) {
												valuelist.add(str[277]);
											} else {
												valuelist.add(str[str.length-1]);
											}
//											System.out.println("-----Ⱥ����Ϣ-------");
//										    System.out.print("id:"+kpi+"--");
//										    System.out.print(str[size]);
						   
										}
								}
							}
						   
						   
					   }
				   }
						this.CreateResultTosqlCR(valuelist,  host.getIpAddress(), cr_list.get(i).toString());
						cr_map.put(cr_list.get(i).toString(), valuelist);
				   }
//				   vmData.put("cr", cr_map);
				   SysLogger.info("----------�ɼ���Ⱥ-----------"+cr_map);;

			   }
			   
			   if(!(ShareData.getVmdata().containsKey(host.getIpAddress())))
				{
					if(vmData == null)vmData = new Hashtable();
					if(cr_list != null && cr_list.size()>0)vmData.put("cr", cr_map);
				    ShareData.getVmdata().put(host.getIpAddress(), vmData);
				}else
				 {
					if(cr_list != null && cr_list.size()>0)((Hashtable)ShareData.getVmdata().get(host.getIpAddress())).put("cr",cr_map);
				 }
			    
			   
			 //����Դ�澯
			    try{
					List list = this.VMgetAlarmInicatorsThresholdForNode(String.valueOf(alarmIndicatorsNode.getNodeid()), AlarmConstant.TYPE_VIRTUAL, "vmware","yun");
//					System.out.println("---------��ѯ�澯ָ���--------"+list.size());
					
					for(int i = 0 ; i < list.size() ; i ++){
						AlarmIndicatorsNode alarmIndicatorsnode = (AlarmIndicatorsNode)list.get(i);
//						SysLogger.info("alarmIndicatorsnode name ======"+alarmIndicatorsnode.getName());
						String event_vid = "";
						String name="";
						if(alarmIndicatorsnode.getEnabled().equals("1")){
							event_vid = alarmIndicatorsnode.getSubentity();
							VMWareVidDao queryname = new VMWareVidDao();
							name = queryname.queryName(host.getId()+"", event_vid);
							queryname.close();
						}else{
							continue;
						}
						if(alarmIndicatorsnode.getName().equalsIgnoreCase("cpuuse")){
						    this.checkDisk(host,event_vid,alarmIndicatorsnode,"yun","cpu","cpuuse",name);
						}
						if(alarmIndicatorsnode.getName().equalsIgnoreCase("memtotal")){
						    this.checkDisk(host,event_vid,alarmIndicatorsnode,"yun","mem","memtotal",name);
						}
					}
			    }catch(Exception e){
			    	e.printStackTrace();
			    }
			   
			   
		    }catch(Exception e){
		    	e.printStackTrace();
		    }
			 //----------------------------��Դ��
		    try{
//		       System.out.println("//----------------------------��Դ��");
		       String ziyuanchi_perfCounterKeys[] = { "cpu.usagemhz.average" };
			   HashMap rp_map = new HashMap();
			   HashMap resultMap = new HashMap();
			   VMWareVidDao rp_vm = new VMWareVidDao();
		       List rp_list = rp_vm.queryVidFlag("resourcepool", host.getId()+"", "");
		       rp_vm.close();
			   if(rp_list != null && rp_list.size()>0){
				   for(int i =0;i<rp_list.size();i++){
//					   PerfEntityMetricBase[] perfEntityMetricBases = (PerfEntityMetricBase[]) vm.getPerformances(url, username, password, VIMConstants.RESOURCE_RP, rplist.get(i).get("vid").toString(), perfIntervalKey, ziyuanchi_perfCounterKeys, endTime).get("info.result");
//					   rp_map.put(rplist.get(i).get("vid").toString(), perfEntityMetricBases);
					   resultMap = (HashMap<String, Object>) VIMMgr.getPerformances(url,username, enpassword, VIMConstants.RESOURCE_RP,rp_list.get(i).toString(), perfIntervalKey, ziyuanchi_perfCounterKeys, endTime);
					   List valuelist = new ArrayList();
					   if ("success".equals(resultMap.get(VIMConstants.INFO_STATE))) {
							PerfEntityMetricBase[] perfEntityMetricBases = (PerfEntityMetricBase[]) resultMap.get(VIMConstants.INFO_RESULT);
							// �����е�����ָ�����Map,��perfCounterInfo.getKeyΪMap��key
							HashMap<Integer, PerfCounterInfo> perfCounterInfoMap = new HashMap<Integer, PerfCounterInfo>();
							for (int j = 0; j < ziyuanchi_perfCounterKeys.length; j++) {
								PerfCounterInfo perfCounterInfo = VIMCache.getInstance(url, username, enpassword).getPerfCounterInfo(ziyuanchi_perfCounterKeys[j]);
								perfCounterInfoMap.put(perfCounterInfo.getKey(),perfCounterInfo);
							}
//	                        List valuelist = new ArrayList();
							if (null != perfEntityMetricBases) {
								for (PerfEntityMetricBase perfEntityMetricBase : perfEntityMetricBases) {
									PerfEntityMetricCSV csvMetric = (PerfEntityMetricCSV) perfEntityMetricBase;
									PerfMetricSeriesCSV[] perfMetricSeriesCSVs = csvMetric.getValue();
//									rp_map.put(rplist.get(i).get("vid").toString(), perfMetricSeriesCSVs);
									if (null != perfMetricSeriesCSVs) {
										for (PerfMetricSeriesCSV perfMetricSeriesCSV : perfMetricSeriesCSVs) {
											int counterId = perfMetricSeriesCSV.getId().getCounterId();
											PerfCounterInfo perfCounterInfo = perfCounterInfoMap.get(counterId);
											String value = perfMetricSeriesCSV.getValue();
//											System.out.println("=================perfCounterInfo="+ perfCounterInfo.getKey());
//											System.out.println("=================value="+ value);
											String kpi = counterId + "";
											String[] str = value.split(",");

											// �ж�value�����Ƿ����288���������ȡ288����Ȼȡvaluelist�ĳ���
											if (str.length > 288) {
												valuelist.add(str[277]);
											} else {
												valuelist.add(str[str.length-1]);
											}
//											System.out.println("-----��Դ����Ϣ-------");
//										    System.out.print("id:"+kpi+"--");
//										    System.out.print(str[size]);
										}
								}
							}
						   
						   
					   }
				   }
					   
					    this.CreateResultTosqlRP(valuelist, host.getIpAddress(), rp_list.get(i).toString());
						rp_map.put(rp_list.get(i).toString(), valuelist);
				   
				   
				   }
//				   vmData.put("rp", rp_map);
			   }
			   if(!(ShareData.getVmdata().containsKey(host.getIpAddress())))
				{
					if(vmData == null)vmData = new Hashtable();
					if(rp_list != null && rp_list.size()>0)vmData.put("rp", rp_map);
				    ShareData.getVmdata().put(host.getIpAddress(), vmData);
				}else
				 {
					if(rp_list != null && rp_list.size()>0)((Hashtable)ShareData.getVmdata().get(host.getIpAddress())).put("rp",rp_map);
				 }
			   
			   
			 //��Դ�ظ澯
			    try{
					List list = this.VMgetAlarmInicatorsThresholdForNode(String.valueOf(alarmIndicatorsNode.getNodeid()), AlarmConstant.TYPE_VIRTUAL, "vmware","resourcepool");
					
					for(int i = 0 ; i < list.size() ; i ++){
						AlarmIndicatorsNode alarmIndicatorsnode = (AlarmIndicatorsNode)list.get(i);
						String event_vid = "";
						String name="";
						if(alarmIndicatorsnode.getEnabled().equals("1")){
							event_vid = alarmIndicatorsnode.getSubentity();
							VMWareVidDao queryname = new VMWareVidDao();
							name = queryname.queryName(host.getId()+"", event_vid);
							queryname.close();
						}else{
							continue;
						}
						if(alarmIndicatorsnode.getName().equalsIgnoreCase("cpuuse")){
						    this.checkDisk(host,event_vid,alarmIndicatorsnode,"resourcepool","cpu","cpuuse",name);
						}
					}
			    }catch(Exception e){
			    	e.printStackTrace();
			    }
			   
			   
			   
			}catch(Exception e){
		    	e.printStackTrace();
		    }
			
			VMWareVidDao dao = new VMWareVidDao();
			VMWareVid vvo = null;
			Hashtable vid_map = new Hashtable();
			List queryall = dao.queryall();
			dao.close();
			if(queryall != null && queryall.size()>0){
			   for(int i=0;i<queryall.size();i++){
				    vvo = (VMWareVid) queryall.get(i);
				    vid_map.put(vvo.getVid(), vvo.getGuestname());
			   }
			}
			ShareData.getVmdata().put("getname", vid_map);
		return returnHash;
    }	
	
    public String getMaxNum(String ipAddress){
    	String maxStr = null;
		File logFolder = new File(ResourceCenter.getInstance().getSysPath() + "linuxserver/");
   		String[] fileList = logFolder.list();
   		
   		for(int i=0;i<fileList.length;i++) //��һ�����µ��ļ�
   		{
   			if(!fileList[i].startsWith(ipAddress)) continue;
   			
   			return ipAddress;
   		}
   		return maxStr;
    }
	
    public void deleteFile(String ipAddress){

			try
			{
				File delFile = new File(ResourceCenter.getInstance().getSysPath() + "linuxserver/" + ipAddress + ".log");
			//System.out.println("###��ʼɾ���ļ���"+delFile);
			//delFile.delete();
			System.out.println("###�ɹ�ɾ���ļ���"+delFile);
			}
			catch(Exception e)		
			{}
    }
    public void copyFile(String ipAddress,String max){
	try   { 
		String currenttime = SysUtil.getCurrentTime();
		currenttime = currenttime.replaceAll("-", "");
		currenttime = currenttime.replaceAll(" ", "");
		currenttime = currenttime.replaceAll(":", "");
		String ipdir = ipAddress.replaceAll("\\.", "-");
		String filename = ResourceCenter.getInstance().getSysPath() + "/linuxserver_bak/"+ipdir;
		File file=new File(filename);
		if(!file.exists())file.mkdir();
        String cmd   =   "cmd   /c   copy   "+ResourceCenter.getInstance().getSysPath() + "linuxserver\\" + ipAddress + ".log"+" "+ResourceCenter.getInstance().getSysPath() + "linuxserver_bak\\" +ipdir+"\\"+ ipAddress+"-" +currenttime+ ".log";             
        //SysLogger.info(cmd);
        Process   child   =   Runtime.getRuntime().exec(cmd);   
      }catch (IOException e){    
        e.printStackTrace();
    }   
	
    }

	 
	 public void createFileNotExistSMS(String ipaddress){
		 	//��������		 	
		 	//���ڴ����õ�ǰ���IP��PING��ֵ
				Calendar date=Calendar.getInstance();
				try{
					Host host = (Host)PollingEngine.getInstance().getNodeByIP(ipaddress);
					if(host == null)return;
					if (!sendeddata.containsKey(ipaddress+":file:"+host.getId())){
						//�����ڣ��������ţ�������ӵ������б���
						Smscontent smscontent = new Smscontent();
						String time = sdf.format(date.getTime());
						smscontent.setLevel("3");
						smscontent.setObjid(host.getId()+"");
						smscontent.setMessage(host.getAlias()+" ("+host.getIpAddress()+")"+"����־�ļ��޷���ȷ�ϴ������ܷ�����");
						smscontent.setRecordtime(time);
						smscontent.setSubtype("host");
						smscontent.setSubentity("ftp");
						smscontent.setIp(host.getIpAddress());//���Ͷ���
						SmscontentDao smsmanager=new SmscontentDao();
						smsmanager.sendURLSmscontent(smscontent);	
						sendeddata.put(ipaddress+":file:"+host.getId(),date);
					}else{
						//���ڣ�����ѷ��Ͷ����б����ж��Ƿ��Ѿ����͵���Ķ���
						Calendar formerdate =(Calendar)sendeddata.get(ipaddress+":file:"+host.getId());		 				
						SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
						Date last = null;
						Date current = null;
						Calendar sendcalen = formerdate;
						Date cc = sendcalen.getTime();
						String tempsenddate = formatter.format(cc);
		 			
						Calendar currentcalen = date;
						cc = currentcalen.getTime();
						last = formatter.parse(tempsenddate);
						String currentsenddate = formatter.format(cc);
						current = formatter.parse(currentsenddate);
		 			
						long subvalue = current.getTime()-last.getTime();			 			
						if (subvalue/(1000*60*60*24)>=1){
							//����һ�죬���ٷ���Ϣ
							Smscontent smscontent = new Smscontent();
							String time = sdf.format(date.getTime());
							smscontent.setLevel("3");
							smscontent.setObjid(host.getId()+"");
							smscontent.setMessage(host.getAlias()+" ("+host.getIpAddress()+")"+"����־�ļ��޷���ȷ�ϴ������ܷ�����");
							smscontent.setRecordtime(time);
							smscontent.setSubtype("host");
							smscontent.setSubentity("ftp");
							smscontent.setIp(host.getIpAddress());//���Ͷ���
							SmscontentDao smsmanager=new SmscontentDao();
							smsmanager.sendURLSmscontent(smscontent);
							//�޸��Ѿ����͵Ķ��ż�¼	
							sendeddata.put(ipaddress+":file:"+host.getId(),date);
						}	
					}	 			 			 			 			 	
				}catch(Exception e){
					e.printStackTrace();
				}
		 	}	 
	 
	 //������������
	 public void CreateResultTosqlHost(HashMap<String,Object> host,String ip,String vid)
		{
		 if(host == null || host.size() == 0)return;
		        	int size=0;
		        	List l = new ArrayList();
			        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		        	String allipstr = SysUtil.doip(ip);
			        DecimalFormat df = new DecimalFormat("0.0");
					Date cc = new Date();
					String time = sdf.format(cc);
					
					String tablename = "vm_host"+allipstr;
					
					PerfEntityMetricBase[] perfEntityMetricBases = (PerfEntityMetricBase[]) host.get(vid);
					for(int i=0;i<8;i++){
						 String[] str = ((PerfMetricSeriesCSV) ((PerfEntityMetricCSV) perfEntityMetricBases[0]).getValue()[i]).getValue().toString().split(",");
		      			 if(str != null && str.length>0){
		      			   if(str.length < 288){
		      				  size = str.length-1;
		      			    }else{
		      				  size = 287;
		      			    }
		    				  l.add(str[size]);
		      			 }
					}
		      	    String cpu= df.format(Long.parseLong((String)l.get(0))/100);
		      	    String cpuuse = l.get(5)+"";
		      	    String meminc = l.get(2)+"";
		      	    String memin = l.get(4)+"";
		      	    String memout= l.get(3)+"";
		      	    String mem =  df.format(Long.parseLong((String)l.get(6))/100);
		      	    String disk =  l.get(7)+"";
		      	    	StringBuffer sBuffer = new StringBuffer();
					    sBuffer.append("insert into ");
					    sBuffer.append(tablename);
				    	sBuffer.append(" (hostid,cpu,cpuuse,meminc,memin,memout,mem,disk,collecttime) ");
					    sBuffer.append("values('");
				    	sBuffer.append(vid);
			    		sBuffer.append("','");
		     			sBuffer.append(cpu);
			    		sBuffer.append("','");
			    		sBuffer.append(cpuuse);
			    		sBuffer.append("','");
			    		sBuffer.append(meminc);
			    		sBuffer.append("','");
			    		sBuffer.append(memin);
			    		sBuffer.append("','");
			    		sBuffer.append(memout);
			    		sBuffer.append("','");
			    		sBuffer.append(mem);
			    		sBuffer.append("','");
			    		sBuffer.append(disk);
					if("mysql".equalsIgnoreCase(SystemConstant.DBType)){
						sBuffer.append("','");
						sBuffer.append(time);
						sBuffer.append("')");
					}else if("oracle".equalsIgnoreCase(SystemConstant.DBType)){
						sBuffer.append("',");
						sBuffer.append("to_date('"+time+"','YYYY-MM-DD HH24:MI:SS')");
						sBuffer.append(")");
					}
					
					//System.out.println(sql);
				    	GathersqlListManager.Addsql(sBuffer.toString());
				    	sBuffer = null;	
//		      	    }
		   }
	 
	 
	 //������������
	 public void CreateResultTosqlGuesthost(HashMap<String,Object> host,String ip,String hoid,String vid)
		{
		 if(host == null || host.size() == 0)return;
			int size=0;
			List l = new ArrayList();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String allipstr = SysUtil.doip(ip);
			DecimalFormat df = new DecimalFormat("0.0");
					Date cc = new Date();
					String time = sdf.format(cc);
					
					String tablename = "vm_guesthost"+allipstr;
					
					 PerfEntityMetricBase[] perfEntityMetricBases = (PerfEntityMetricBase[]) host.get(vid);
					for(int i=0;i<9;i++){
						try{
					
						 String[] str = ((PerfMetricSeriesCSV) ((PerfEntityMetricCSV) perfEntityMetricBases[0]).getValue()[i]).getValue().toString().split(",");
		      			 if(str != null && str.length>0){
		      			   if(str.length < 288){
		      				  size = str.length-1;
		      			    }else{
		      				  size = 287;
		      			    }
		    				  l.add(str[size]);
		      			 }
						}catch(NullPointerException e){
							l.add("0");
//							e.printStackTrace();
						}
					}
//		      			 List category = new ArrayList();
//		      			 List thevalue = new ArrayList();
//		      			category.add("cpu");
//		      			category.add("cpuuse");
//		      			category.add("meminc");
//		      			category.add("memio");
//		      			category.add("mem");
//		      			category.add("disk");
//		      			category.add("net");
		      	    String cpu= df.format(Long.parseLong((String)l.get(0))/100);
		      	    String cpuuse = l.get(5)+"";
		      	    String meminc = l.get(2)+"";
		      	    String memin = l.get(4)+"";
		      	    String memout = l.get(3)+"";
		      	    String mem =  df.format(Long.parseLong((String)l.get(6))/100);
		      	    String disk =  l.get(7)+"";
		      	    String net= l.get(8)+"";
//		      	    thevalue.add(cpu);thevalue.add(cpuuse);thevalue.add(meminc);thevalue.add(memio);thevalue.add(mem);thevalue.add(disk);thevalue.add(net);
//		      	    for(int j=0;j<category.size();j++){
		      	    StringBuffer sBuffer = new StringBuffer();
					sBuffer.append("insert into ");
					sBuffer.append(tablename);
					sBuffer.append(" (vid,hostid,cpu,cpuuse,meminc,memin,memout,mem,disk,net,collecttime) ");
					sBuffer.append("values('");
					sBuffer.append(vid);
					sBuffer.append("','");
					sBuffer.append(hoid);
					sBuffer.append("','");
					sBuffer.append(cpu);
					sBuffer.append("','");
					sBuffer.append(cpuuse);
					sBuffer.append("','");
					sBuffer.append(meminc);
					sBuffer.append("','");
					sBuffer.append(memin);
					sBuffer.append("','");
					sBuffer.append(memout);
					sBuffer.append("','");
					sBuffer.append(mem);
					sBuffer.append("','");
					sBuffer.append(disk);
					sBuffer.append("','");
					sBuffer.append(net);
					if("mysql".equalsIgnoreCase(SystemConstant.DBType)){
						sBuffer.append("','");
						sBuffer.append(time);
						sBuffer.append("')");
					}else if("oracle".equalsIgnoreCase(SystemConstant.DBType)){
						sBuffer.append("',");
						sBuffer.append("to_date('"+time+"','YYYY-MM-DD HH24:MI:SS')");
						sBuffer.append(")");
					}
					//System.out.println(sql);
					GathersqlListManager.Addsql(sBuffer.toString());
					sBuffer = null;	
//		      	    }
		   }
	 
	 //��Ⱥ���
	 public void CreateResultTosqlCR(List cr_list,String ip,String vid)
		{
		 if(cr_list == null || cr_list.size() == 0)return;
			int size=0;
			List l = new ArrayList();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String allipstr = SysUtil.doip(ip);
			DecimalFormat df = new DecimalFormat("0.0");
					Date cc = new Date();
					String time = sdf.format(cc);
					
					String tablename = "vm_cluster"+allipstr;
					if(cr_list.size() == 4){
		      	    String cpu= cr_list.get(0)+"";
		      	    String cputotal = cr_list.get(1)+"";
		      	    String  memtotal= cr_list.get(2)+"";
		      	    String  mem= cr_list.get(3)+"";
//		      	    thevalue.add(cpu);thevalue.add(cpuuse);thevalue.add(meminc);thevalue.add(memio);thevalue.add(mem);thevalue.add(disk);thevalue.add(net);
//		      	    for(int j=0;j<category.size();j++){
		      	    StringBuffer sBuffer = new StringBuffer();
					sBuffer.append("insert into ");
					sBuffer.append(tablename);
					sBuffer.append(" (vid,cpu,cputotal,mem,memtotal,collecttime) ");
					sBuffer.append("values('");
					sBuffer.append(vid);
					sBuffer.append("','");
					sBuffer.append(cpu);
					sBuffer.append("','");
					sBuffer.append(cputotal);
					sBuffer.append("','");
					sBuffer.append(mem);
					sBuffer.append("','");
					sBuffer.append(memtotal);
					if("mysql".equalsIgnoreCase(SystemConstant.DBType)){
						sBuffer.append("','");
						sBuffer.append(time);
						sBuffer.append("')");
					}else if("oracle".equalsIgnoreCase(SystemConstant.DBType)){
						sBuffer.append("',");
						sBuffer.append("to_date('"+time+"','YYYY-MM-DD HH24:MI:SS')");
						sBuffer.append(")");
					}
					GathersqlListManager.Addsql(sBuffer.toString());
					sBuffer = null;	
		      	    }
		   }
	 
	//�洢���
	 public void CreateResultTosqlDS(List ds_list,String ip,String vid)
		{
			if(ds_list == null || ds_list.size() == 0)return;
			int size=0;
			List l = new ArrayList();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String allipstr = SysUtil.doip(ip);
			DecimalFormat df = new DecimalFormat("0.0");
					Date cc = new Date();
					String time = sdf.format(cc);
					
					String tablename = "vm_datastore"+allipstr;
					
		      	    String used=df.format(Double.parseDouble((String)ds_list.get(0))/(1024*1024))+"";
		      	    String capacity =df.format(Double.parseDouble((String)ds_list.get(2))/(1024*1024))+"";
		      	    String assigned =df.format(Double.parseDouble((String)ds_list.get(1))/(1024*1024))+"";
		      	    String use= df.format(Long.parseLong((String)ds_list.get(0))*100/Long.parseLong((String)ds_list.get(2)));
//		      	    thevalue.add(cpu);thevalue.add(cpuuse);thevalue.add(meminc);thevalue.add(memio);thevalue.add(mem);thevalue.add(disk);thevalue.add(net);
//		      	    for(int j=0;j<category.size();j++){
		      	    StringBuffer sBuffer = new StringBuffer();
					sBuffer.append("insert into ");
					sBuffer.append(tablename);
					sBuffer.append(" (vid,used,assigned ,capacity,useuse,collecttime) ");
					sBuffer.append("values('");
					sBuffer.append(vid);
					sBuffer.append("','");
					sBuffer.append(used);
					sBuffer.append("','");
					sBuffer.append(assigned);
					sBuffer.append("','");
					sBuffer.append(capacity);
					sBuffer.append("','");
					sBuffer.append(use);
					if("mysql".equalsIgnoreCase(SystemConstant.DBType)){
						sBuffer.append("','");
						sBuffer.append(time);
						sBuffer.append("')");
					}else if("oracle".equalsIgnoreCase(SystemConstant.DBType)){
						sBuffer.append("',");
						sBuffer.append("to_date('"+time+"','YYYY-MM-DD HH24:MI:SS')");
						sBuffer.append(")");
					}
					GathersqlListManager.Addsql(sBuffer.toString());
					sBuffer = null;	
//		      	    }
		   }
	 
	 
	//��Դ�����
	 public void CreateResultTosqlRP(List rp_list,String ip,String vid)
		{
		 if(rp_list == null || rp_list.size() == 0)return;
			int size=0;
			List l = new ArrayList();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String allipstr = SysUtil.doip(ip);
			DecimalFormat df = new DecimalFormat("0.0");
					Date cc = new Date();
					String time = sdf.format(cc);
					String tablename = "vm_resourcepool"+allipstr;
					if(rp_list.size() == 1){
		      	    String cpu=rp_list.get(0)+"";
//		      	    thevalue.add(cpu);thevalue.add(cpuuse);thevalue.add(meminc);thevalue.add(memio);thevalue.add(mem);thevalue.add(disk);thevalue.add(net);
//		      	    for(int j=0;j<category.size();j++){
		      	    StringBuffer sBuffer = new StringBuffer();
					sBuffer.append("insert into ");
					sBuffer.append(tablename);
					sBuffer.append(" (vid,cpu,collecttime) ");
					sBuffer.append("values('");
					sBuffer.append(vid);
					sBuffer.append("','");
					sBuffer.append(cpu);
					if("mysql".equalsIgnoreCase(SystemConstant.DBType)){
						sBuffer.append("','");
						sBuffer.append(time);
						sBuffer.append("')");
					}else if("oracle".equalsIgnoreCase(SystemConstant.DBType)){
						sBuffer.append("',");
						sBuffer.append("to_date('"+time+"','YYYY-MM-DD HH24:MI:SS')");
						sBuffer.append(")");
					}
					GathersqlListManager.Addsql(sBuffer.toString());
					sBuffer = null;	
//		      	    }
					}
		   }
	 
	 public void savePhysical(List<HashMap<String,Object>> wulist,String nodeid){
		 if(wulist.size()>0 && wulist != null){
 			VMWareVidDao viddao = new VMWareVidDao();
 			VMWareDao vmdao = new VMWareDao();
 			List physical = new ArrayList();
 			List physical_vid = new ArrayList();
 			for(int i=0;i<wulist.size();i++){
 				VMWareVO vmware = new VMWareVO();
 				VMWareVid vmvid = new VMWareVid();
 				String host_vid = wulist.get(i).get("vid").toString();
 				vmware.setVid(wulist.get(i).get("vid").toString());
 				vmware.setName(wulist.get(i).get("name").toString());
 				vmware.setModel(wulist.get(i).get("model").toString());
 				vmware.setCpunum(wulist.get(i).get("numcpu").toString());
 				vmware.setNetnum(wulist.get(i).get("numnics").toString());
 				vmware.setMemory(wulist.get(i).get("memorysizemb").toString());
 				vmware.setGhz(wulist.get(i).get("cpumhz").toString());
 				vmware.setHostpower(wulist.get(i).get("powerstate").toString());
 				physical.add(vmware);
 				
 				vmvid.setVid(host_vid);
 				vmvid.setCategory("physical");
 				vmvid.setNodeid(Long.parseLong(nodeid));
 				vmvid.setGuestname(wulist.get(i).get("name").toString());
 				physical_vid.add(vmvid);
 			}
 			vmdao.savePhysical(physical, ipaddress);
 			viddao.save(physical_vid);
 		}
	 }
	 
 public void saveVmware(List<HashMap<String,Object>> vmlist,String nodeid){
	 if(vmlist.size()>0 && vmlist != null){
			VMWareVidDao viddao = new VMWareVidDao();
			VMWareDao vmdao = new VMWareDao();
			List vm = new ArrayList();
			List vm_id = new ArrayList();
			for(int i=0;i<vmlist.size();i++){
				VMWareVO vmware = new VMWareVO();
				VMWareVid vmvid = new VMWareVid();
				String id = vmlist.get(i).get("vid").toString();
				vmware.setVid(vmlist.get(i).get("vid").toString());
				vmware.setName(vmlist.get(i).get("name").toString());
				vmware.setFullname(vmlist.get(i).get("guestfullname").toString());
				vmware.setHoid(vmlist.get(i).get("hoid").toString());
				vmware.setMemoryuse(vmlist.get(i).get("memorysizemb").toString());
				vmware.setCpu(vmlist.get(i).get("numcpu").toString()+"��*"+vmlist.get(i).get("numcore").toString()+"��");
				vmware.setVmpower(vmlist.get(i).get("powerstate").toString());
				vm.add(vmware);
				//
				vmvid.setVid(id);
				vmvid.setHoid(vmlist.get(i).get("hoid").toString());
				vmvid.setCategory("vmware");
				vmvid.setGuestname(vmlist.get(i).get("name").toString());
				vmvid.setNodeid(Long.parseLong(nodeid));
				vm_id.add(vmvid);
			}
			vmdao.saveVmware(vm, ipaddress);
			viddao.save(vm_id);
		}
	 }
 public void saveYun(List<HashMap<String,Object>> crlist,String nodeid){
	 if(crlist.size()>0 && crlist != null){
			VMWareVidDao viddao = new VMWareVidDao();
			VMWareDao vmdao = new VMWareDao();
			List vm = new ArrayList();
			List vm_id = new ArrayList();
			for(int i=0;i<crlist.size();i++){
				VMWareVO vmware = new VMWareVO();
				VMWareVid vmvid = new VMWareVid();
				String id = crlist.get(i).get("vid").toString();
				vmware.setVid(crlist.get(i).get("vid").toString());
				vmware.setName(crlist.get(i).get("name").toString());
				vmware.setDisk(crlist.get(i).get("totaldssizemb").toString());
				vmware.setCpuuse(crlist.get(i).get("totalcpu").toString());
				vmware.setHostnum(crlist.get(i).get("numhosts").toString());
				vmware.setMem(crlist.get(i).get("totalmemory").toString());
				vmware.setCpunum(crlist.get(i).get("numcpucores").toString());
				vm.add(vmware);
				//
				vmvid.setVid(id);
				vmvid.setCategory("yun");
				vmvid.setGuestname(crlist.get(i).get("name").toString());
				vmvid.setNodeid(Long.parseLong(nodeid));
				vm_id.add(vmvid);
			}
			vmdao.saveYun(vm, ipaddress);
			viddao.save(vm_id);
		}
 }
 public void saveDatastore(List<HashMap<String,Object>> dslist,String nodeid){

		if(dslist != null && dslist.size()>0  ){
			VMWareVidDao viddao = new VMWareVidDao();
			VMWareDao vmdao = new VMWareDao();
			List vm = new ArrayList();
			List vm_id = new ArrayList();
			for(int i=0;i<dslist.size();i++){
				VMWareVO vmware = new VMWareVO();
				VMWareVid vmvid = new VMWareVid();
				String id = dslist.get(i).get("vid").toString();
				vmware.setVid(dslist.get(i).get("vid").toString());
				vmware.setName(dslist.get(i).get("name").toString());
				vmware.setStore(dslist.get(i).get("capacity").toString());
				vmware.setUnusedstore(dslist.get(i).get("freespace").toString());
				vm.add(vmware);
				//
				vmvid.setVid(id);
				vmvid.setCategory("datastore");
				vmvid.setGuestname(dslist.get(i).get("name").toString());
				vmvid.setNodeid(Long.parseLong(nodeid));
				vm_id.add(vmvid);
			}
			vmdao.saveDatastore(vm, ipaddress);
			viddao.save(vm_id);
		}
 }
 public void saveDatacenter(List<HashMap<String,Object>> dclist,String nodeid){
	 if(  dclist != null && dclist.size()>0){
			VMWareVidDao viddao = new VMWareVidDao();
			VMWareDao vmdao = new VMWareDao();
			List vm = new ArrayList();
			List vm_id = new ArrayList();
			for(int i=0;i<dclist.size();i++){
				VMWareVO vmware = new VMWareVO();
				VMWareVid vmvid = new VMWareVid();
				String id = dclist.get(i).get("vid").toString();
				vmware.setVid(dclist.get(i).get("vid").toString());
				vmware.setName(dclist.get(i).get("name").toString());
				vmware.setDcid(dclist.get(i).get("vid").toString());
				vm.add(vmware);
				//
				vmvid.setVid(id);
				vmvid.setCategory("datacenter");
				vmvid.setGuestname(dclist.get(i).get("name").toString());
				vmvid.setNodeid(Long.parseLong(nodeid));
				vm_id.add(vmvid);
			}
			vmdao.saveDatacenter(vm, ipaddress);
			viddao.save(vm_id);
		}
 }
public void saveResourcepool(List<HashMap<String,Object>> rplist,String nodeid){
	if( rplist != null  && rplist.size()>0){
		VMWareVidDao viddao = new VMWareVidDao();
		VMWareDao vmdao = new VMWareDao();
		List vm = new ArrayList();
		List vm_id = new ArrayList();
		for(int i=0;i<rplist.size();i++){
			VMWareVO vmware = new VMWareVO();
			VMWareVid vmvid = new VMWareVid();
			String id = rplist.get(i).get("vid").toString();
			vmware.setVid(rplist.get(i).get("vid").toString());
			vmware.setName(rplist.get(i).get("name").toString());
			vmware.setDcid(rplist.get(i).get("dcid").toString());
			vmware.setCrid(rplist.get(i).get("crid").toString());
			vm.add(vmware);
			//
			vmvid.setVid(id);
			vmvid.setCategory("resourcepool");
			vmvid.setGuestname(rplist.get(i).get("name").toString());
			vmvid.setNodeid(Long.parseLong(nodeid));
			vm_id.add(vmvid);
		}
		vmdao.saveResourcepool(vm, ipaddress);
		viddao.save(vm_id);
	}
 }



public void checkDisk(Host node,String vid,AlarmIndicatorsNode nm,String category,String flag,String name,String guestname){
	SysLogger.info("### ��ʼ���������--"+category+"--ָ���Ƿ�澯... ###");
	
//	System.out.println(nm.getName()+"-------------------------------"+nm.getEnabled());
	
	CheckEventUtil ce = new CheckEventUtil();
	if ("0".equals(nm.getEnabled())) {
		//�澯ָ��δ��� �����κ����� ����
		return;
	}
//	if (vid == null || vid.size() == 0) {
//		//δ�ɼ������� �����κ����� ����
//		return;
//	}
//		for(int i=0;i<map.size();i++){
			AlarmIndicatorsNodeDao alarm = new AlarmIndicatorsNodeDao();
			AlarmIndicatorsNode vo = new AlarmIndicatorsNode();
			List list = (List) alarm.VMgetByNodeIdAndTypeAndSubType(node.getId()+"", "virtual", "vmware", category, vid,name);
			if(list.size()>0){
			vo = (AlarmIndicatorsNode)list.get(0);
//			if(vo.getEnabled().equals("0"))break;
			int limevalue0 = Integer.parseInt(vo.getLimenvalue0());
			int limevalue1 = Integer.parseInt(vo.getLimenvalue1());
			int limevalue2 =Integer.parseInt(vo.getLimenvalue2());
			System.out.println(nm.getName() + "============һ����ֵ=============" + limevalue0 + "==������ֵ==" + limevalue1 + "===������ֵ===" + limevalue2);
			nm.setLimenvalue0(limevalue0 + "");
			nm.setLimenvalue1(limevalue1 + "");
			nm.setLimenvalue2(limevalue2 + "");
			NodeUtil nodeUtil = new NodeUtil();
			NodeDTO nodeDTO = nodeUtil.conversionToNodeDTO(node);
			
			VMWareDao vmdao = new VMWareDao();
			String tablename ="";
			if(category.equalsIgnoreCase("physical")){
				tablename = "vm_host";
			}else if(category.equalsIgnoreCase("vmware")){
				tablename = "vm_guesthost";
			}else if(category.equalsIgnoreCase("resourcepool")){
				tablename = "vm_resourcepool";
			}else if(category.equalsIgnoreCase("yun")){
				tablename = "vm_cluster";
			}
			HashMap value = vmdao.queryLast(tablename, node.getIpAddress(), vid);
			SysLogger.info("###########"+category+"�澯���###############################");
			if(value.size()!=0){
			  SysLogger.info("###########"+category+":"+value.get(flag).toString()+"�澯���###############################");
			  ce.checkEvent(nodeDTO, nm, value.get(flag).toString(), guestname);
			}
//			}
		}
}


public void checkDisk(Host node,List map,AlarmIndicatorsNode nm,String category,String flag,String name){
	SysLogger.info("### ��ʼ���������--"+category+"--ָ���Ƿ�澯... ###");
	CheckEventUtil ce = new CheckEventUtil();
	if ("0".equals(nm.getEnabled())) {
		//�澯ָ��δ��� �����κ����� ����
		return;
	}
	if (map == null || map.size() == 0) {
		//δ�ɼ������� �����κ����� ����
		return;
	}
		for(int i=0;i<map.size();i++){
			AlarmIndicatorsNodeDao alarm = new AlarmIndicatorsNodeDao();
			AlarmIndicatorsNode vo = new AlarmIndicatorsNode();
			List list = (List) alarm.VMgetByNodeIdAndTypeAndSubType(node.getId()+"", "virtual", "vmware", category, map.get(i).toString(),name);
			if(list.size()>0){
			vo = (AlarmIndicatorsNode)list.get(0);
			int limevalue0 = Integer.parseInt(vo.getLimenvalue0());
			int limevalue1 = Integer.parseInt(vo.getLimenvalue1());
			int limevalue2 =Integer.parseInt(vo.getLimenvalue2());
			System.out.println(nm.getName() + "============һ����ֵ=============" + limevalue0 + "==������ֵ==" + limevalue1 + "===������ֵ===" + limevalue2);
			nm.setLimenvalue0(limevalue0 + "");
			nm.setLimenvalue1(limevalue1 + "");
			nm.setLimenvalue2(limevalue2 + "");
			NodeUtil nodeUtil = new NodeUtil();
			NodeDTO nodeDTO = nodeUtil.conversionToNodeDTO(node);
			
			VMWareDao vmdao = new VMWareDao();
			String tablename ="";
			if(category.equalsIgnoreCase("physical")){
				tablename = "vm_host";
			}else if(category.equalsIgnoreCase("vmware")){
				tablename = "vm_guesthost";
			}else if(category.equalsIgnoreCase("resourcepool")){
				tablename = "vm_resourcepool";
			}else if(category.equalsIgnoreCase("yun")){
				tablename = "vm_cluster";
			}
			HashMap value = vmdao.queryLast(tablename, node.getIpAddress(), map.get(i)+"");
			SysLogger.info("###########"+category+"�澯���###############################");
			if(value.size()!=0){
			  SysLogger.info("###########"+category+":"+value.get(flag).toString()+"�澯���###############################");
			  ce.checkEvent(nodeDTO, nm, value.get(flag).toString(), "");
			}
			}
		}
}

public List VMgetAlarmInicatorsThresholdForNode(String nodeId , String type , String subtype,String category){ 
	String key= nodeId +":"+ type+":"+ subtype; 
//	SysLogger.info("##########################������澯:"+key);
	List resultList=new ArrayList();//����Key��ѯ�õ��Ľ��������Ҫ������ 
	try{
		Hashtable hs=ResourceCenter.getInstance().getAlarmHashtable();
		
//		System.out.println("--------hs---------"+hs);
		
		if(hs == null) hs = new Hashtable();
		if(category != null && category.trim().length()>0){				
			if(hs.containsKey(key))
			{
				resultList=(ArrayList)hs.get(key);
			}
		}else{
			if(hs.size()>0){
				Enumeration newProEnu = hs.keys();
				while(newProEnu.hasMoreElements())
				{
					String alarmName = (String)newProEnu.nextElement();
					if(alarmName.startsWith(nodeId +":"+ type+":")){
						resultList=(ArrayList)hs.get(alarmName);
					}
				}
			}
		}

	}catch(Exception e){
		e.printStackTrace();
	}
	List list = new ArrayList();
	for(int i=0;i<resultList.size();i++){
		AlarmIndicatorsNode vo = new AlarmIndicatorsNode();
		vo = (AlarmIndicatorsNode) resultList.get(i);
		if(vo.getCategory().equalsIgnoreCase(category)){
			list.add(vo);
		}
	}
	return list;  
} 
	 







}






