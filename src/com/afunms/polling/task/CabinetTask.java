/*
 * Created on 2010-06-24
 *
 */
package com.afunms.polling.task;

import com.afunms.cabinet.util.CabinetXML;
import com.afunms.common.util.SysLogger;





/**
 *3D�������ݸ���task
 */
public class CabinetTask extends MonitorTask {
	
	public void run() {
		try{
			//��������3D��������xml�ļ�
			CabinetXML cxml = new CabinetXML();
		   	cxml.CreateCabinetXML();
		}catch(Exception e){
			SysLogger.error(e.getMessage());
			e.printStackTrace();
			//���������⣬����Ҫ���µ���������ʱ����ķ���
		}finally{
			SysLogger.info("********CabinetTask Thread Count : "+Thread.activeCount());
		}
	}
}
