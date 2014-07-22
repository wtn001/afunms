package com.afunms.topology.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

import com.afunms.common.util.EncryptUtil;
import com.afunms.common.util.SysLogger;
import com.afunms.vmware.vim25.common.VIMMgr;
import com.afunms.vmware.vim25.constants.VIMConstants;

public class WMWareUtil {
	public Hashtable SyncVM(String ipaddress,String username,String password) {
		Hashtable<String, Object> VIMmap = new Hashtable<String,Object>();
		String enpassword = "";
		try{
			enpassword = EncryptUtil.decode(password);
		}catch(Exception e){
			e.printStackTrace();
		}
		VIMMgr vimmgr = new VIMMgr();
		VIMmap = (Hashtable)vimmgr.syncVIMObjs("https://"+ipaddress+"/sdk", username,enpassword );//��ȡvmware���ص���������
		
		ArrayList<Hashtable<String,Object>> VMlist = new ArrayList<Hashtable<String,Object>>();
		VMlist = (ArrayList<Hashtable<String,Object>>)VIMmap.get(VIMConstants.SYNC_VM);//����������
		
	
		Hashtable<String, Object> VMmap = new Hashtable<String, Object>();
		if(VMlist.size()==0){
			VIMmap = null;
			SysLogger.info("û�з���"+ipaddress+"�����");
		}else{
			for(int i=0;i<VMlist.size();i++){
				String vid = (String)VMlist.get(i).get(VIMConstants.SYNC_COMMON_VID);
				String name = (String)VMlist.get(i).get(VIMConstants.SYNC_COMMON_NAME);
				VMmap.put(vid,name);
			}
		}
		
		return VMmap;
			
	}
}
