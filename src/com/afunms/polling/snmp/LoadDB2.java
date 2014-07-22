package com.afunms.polling.snmp;

/*
 * @author yangjun@dhcc.com.cn
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
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.afunms.alarm.model.AlarmIndicatorsNode;
import com.afunms.alarm.send.SendAlarmUtil;
import com.afunms.alarm.send.SendMailAlarm;
import com.afunms.alarm.util.AlarmConstant;
import com.afunms.alarm.util.AlarmIndicatorsUtil;
import com.afunms.application.dao.DBDao;
import com.afunms.application.model.DBVo;
import com.afunms.common.util.Arith;
import com.afunms.common.util.CheckEventUtil;
import com.afunms.common.util.ShareData;
import com.afunms.indicators.model.NodeGatherIndicators;
import com.afunms.initialize.ResourceCenter;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.node.DBNode;
import com.afunms.polling.om.CPUcollectdata;
import com.afunms.polling.om.Processcollectdata;
import com.afunms.polling.om.Systemcollectdata;
import com.afunms.polling.om.Usercollectdata;
  
/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */     
public class LoadDB2 {        
	/**
	 * @param     
	 */   
	private String ipaddress;
	DecimalFormat df=new DecimalFormat("#.##");
	public LoadDB2(String ipaddress) {
		this.ipaddress = ipaddress;   
	}
	public Hashtable collect_Data()   
    {       
		
		
		Hashtable allDb2Data = (Hashtable)ShareData.getAlldb2data();
		//yangjun
		Hashtable ipAllData = (Hashtable)allDb2Data.get(this.ipaddress);
		
		if(ipAllData == null)ipAllData = new Hashtable();
		StringBuffer fileContent = new StringBuffer();   
		
		String Lock_Timeouts = "";//���ݿ⡢Ӧ������ʱ����
		String Lock_waits = "";//���ݿ⡢Ӧ�����ȴ�����
		String Lock_escalations = "";//������������
		String e_Lock_escalations ="";//��������������
		String Deadlocks_detected = "";//��������
		String Lock_waits_Times = "";//�ȴ�����ƽ��ʱ��
		String Locks_held_currently = "";//����ʹ�õ�������
		String Sort_overflows ="";//��������Ĵ���
		String Sort_heap ="";//�����ջ�Ĵ�С
		String Shared_Sort = "";
		String avg_sort_time = "";//ƽ������ʱ��
		String Sort_time = "0";
		String Total_sorts = "0";
		String Internal_rollbacks = "";//�ع���������
		String Internal_commits = "";//�ύ��������
		String rollbacks_deadlock = "";//��Ϊ�ڲ������Ļع�����
		String Secondary_logs = "";//���ݿ������־�ռ�
		String secondary_log_space = "";//������־ʹ�ÿռ�
		String Number_read_log = "";//��־��ȡ����
		String Number_write_log = "";//��־д�����
		String Internal_percent = "";//���ݿ���SQL�ع��ٷֱ�
		String Select_executed = "";
		String Update_Insert_Delete_executed ="";
		String all_sql_nums = "";//���ݿ�����ִ�е�����SQL��Ŀ
		String Failed_operations = "";
		String Failed_operations_per = "";
		String Buffer_pool_index_writes = "";
		String write_elapsed_time = "";
		String Direct_writes = "";
		String Direct_writes_time = "";
		String reads_elapsed_time = "";
		String Direct_reads = "";
		String Direct_reads_time = "";
		String Buffer_pool_index_physical = "";//��ռ���ͬ��������������
		String Buffer_pool_index_logical = "";
		String Asynchronous_pool_index_writes = "";//��ռ����첽������д����
		String Asynchronous_pool_index_reads = "";//��ռ����첽������������
		String Buffer_pool_write_time = "";
		String Buffer_pool_logical_write = "";
		String avg_buffer_pool_write_time = "";//��ռ�ƽ����buffer poolдʱ��
		String Buffer_pool_read_time = "";
		String Buffer_pool_logical_read = "";
		String avg_buffer_pool_reads_time = "";//��ռ�ƽ����buffer pool��ʱ��
		String Catalog_cache_overflows = "";//catalog cache�������
		String Buffer_pool_data_writes = "";//buffer poolд����
		String Buffer_pool_physical_read = "";//
		String Application_section_inserts = "";//Ӧ�ù�������������
		String Application_section_lookups = "";//Ӧ�ù�������������
		String Package_cache_inserts = "";
		String Package_cache_lookups = "";
		String package_cache = "";//package cache������
		String Catalog_cache_inserts = "";
		String Catalog_cache_lookups = "";
		String catlog_cache = "";//catlog cache������
		String buffer_pool_index = "";//buffer pool����������
		String buffer_pool = "";//buffer pool������
		
    	try {
			String filename = ResourceCenter.getInstance().getSysPath() + "/linuxserver/"+this.ipaddress+"_db2.log";	
			
			File file=new File(filename);
			if(!file.exists()){
				//�ļ�������,������澯
				try{
//					createFileNotExistSMS(ipaddress);
				}catch(Exception e){
					e.printStackTrace();
				}
				return null;
			}
			
			file = null;
			FileInputStream fis = new FileInputStream(filename);
			InputStreamReader isr=new InputStreamReader(fis);
			BufferedReader br=new BufferedReader(isr);
			String strLine = null;
    		//�����ļ�����
    		while((strLine=br.readLine())!=null)
    		{
    			fileContent.append(strLine + "\n");
    		}
    		isr.close();
    		fis.close();
    		br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String dbmanager_Content = "";
    	Pattern tmpPt = null;
    	Matcher mr = null;
    	Calendar date = Calendar.getInstance();
		tmpPt = Pattern.compile("(cmdbegin:dbmanager)(.*)(cmdbegin:db)",Pattern.DOTALL);
		mr = tmpPt.matcher(fileContent.toString());
		if(mr.find())
		{
			dbmanager_Content = mr.group(2);
		}
		String[] dbmanager_LineArr = null;
		String[] dbmanager_tmpData = null;
		if (dbmanager_Content != null && dbmanager_Content.trim().length()>0 ){
			dbmanager_LineArr = dbmanager_Content.split("\n");
		}
		
		String dbContent = "";
		tmpPt = Pattern.compile("(cmdbegin:db)(.*)(cmdbegin:tablespaces)",Pattern.DOTALL);
		mr = tmpPt.matcher(fileContent.toString());
		if(mr.find())
		{
			dbContent = mr.group(2);
		}
		String[] db_LineArr = null;
		String[] db_tmpData = null;
		if (dbContent != null && dbContent.trim().length()>0){
			db_LineArr = dbContent.split("\n");
			if(db_LineArr!=null&&db_LineArr.length>0){
				for(int i=0;i<db_LineArr.length;i++){
					if(db_LineArr[i].indexOf("=")!=-1){
						db_tmpData = db_LineArr[i].split("=");
                        //���ݿ⡢Ӧ������ʱ����
						if(db_tmpData!=null&&db_tmpData.length>1&&db_tmpData[0].trim().equalsIgnoreCase("Lock Timeouts")){
							Lock_Timeouts = db_tmpData[1].trim();	
						}
						//���ݿ⡢Ӧ�����ȴ�����
						if(db_tmpData!=null&&db_tmpData.length>1&&db_tmpData[0].trim().equalsIgnoreCase("Lock waits")){
							Lock_waits = db_tmpData[1].trim();	
						}
                        //������������
						if(db_tmpData!=null&&db_tmpData.length>1&&db_tmpData[0].trim().equalsIgnoreCase("Lock escalations")){
							Lock_escalations = db_tmpData[1].trim();	
						}
                        //��������������
						if(db_tmpData!=null&&db_tmpData.length>1&&db_tmpData[0].trim().equalsIgnoreCase("Exclusive lock escalations")){
							e_Lock_escalations = db_tmpData[1].trim();	
						}
						//��������
						if(db_tmpData!=null&&db_tmpData.length>1&&db_tmpData[0].trim().equalsIgnoreCase("Deadlocks detected")){
							Deadlocks_detected = db_tmpData[1].trim();	
						}
						//�ȴ�����ƽ��ʱ��
						if(db_tmpData!=null&&db_tmpData.length>1&&db_tmpData[0].trim().equalsIgnoreCase("Time database waited on locks (ms)")){
							Lock_waits_Times = db_tmpData[1].trim().equalsIgnoreCase("Not Collected")?db_tmpData[1].trim():(Double.parseDouble(db_tmpData[1].trim()+"")/(Double.parseDouble(Lock_waits)==0?1:Double.parseDouble(Lock_waits)))+"";	
						}
						//����ʹ�õ�������
						if(db_tmpData!=null&&db_tmpData.length>1&&db_tmpData[0].trim().equalsIgnoreCase("Locks held currently")){
							Locks_held_currently = db_tmpData[1].trim();	
						}
						//��������Ĵ���
						if(db_tmpData!=null&&db_tmpData.length>1&&db_tmpData[0].trim().equalsIgnoreCase("Sort overflows")){
							Sort_overflows = db_tmpData[1].trim();	
						}
						//�����ջ�Ĵ�С
						if(db_tmpData!=null&&db_tmpData.length>1&&db_tmpData[0].trim().equalsIgnoreCase("Total Private Sort heap allocated")){
							Sort_heap = db_tmpData[1].trim();	
						}
						if(db_tmpData!=null&&db_tmpData.length>1&&db_tmpData[0].trim().equalsIgnoreCase("Total sort time (ms)")){
							Sort_time = db_tmpData[1].trim();	
						}
						if(db_tmpData!=null&&db_tmpData.length>1&&db_tmpData[0].trim().equalsIgnoreCase("Total sorts")){
							Total_sorts = db_tmpData[1].trim();	
						}
						//�ع���������
						if(db_tmpData!=null&&db_tmpData.length>1&&db_tmpData[0].trim().equalsIgnoreCase("Internal rollbacks")){
							Internal_rollbacks = db_tmpData[1].trim();	
						}
						//�ύ��������
						if(db_tmpData!=null&&db_tmpData.length>1&&db_tmpData[0].trim().equalsIgnoreCase("Internal commits")){
							Internal_commits = db_tmpData[1].trim();	
						}
						//��Ϊ�ڲ������Ļع�����,�������SQLʧ�ܵ�����
						if(db_tmpData!=null&&db_tmpData.length>1&&db_tmpData[0].trim().equalsIgnoreCase("Internal rollbacks due to deadlock")){
							rollbacks_deadlock = db_tmpData[1].trim();	
						}
						//���ݿ������־�ռ�
						if(db_tmpData!=null&&db_tmpData.length>1&&db_tmpData[0].trim().equalsIgnoreCase("Secondary logs allocated currently")){
							Secondary_logs = db_tmpData[1].trim();	
						}
						//������־ʹ�ÿռ�
						if(db_tmpData!=null&&db_tmpData.length>1&&db_tmpData[0].trim().equalsIgnoreCase("Maximum secondary log space used (Bytes)")){
							secondary_log_space = db_tmpData[1].trim();	
						}
						//��־��ȡ����
						if(db_tmpData!=null&&db_tmpData.length>1&&db_tmpData[0].trim().equalsIgnoreCase("Number read log IOs")){
							Number_read_log = db_tmpData[1].trim();	
						}
						//��־д�����
						if(db_tmpData!=null&&db_tmpData.length>1&&db_tmpData[0].trim().equalsIgnoreCase("Number write log IOs")){
							Number_write_log = db_tmpData[1].trim();	
						}
						//
						if(db_tmpData!=null&&db_tmpData.length>1&&db_tmpData[0].trim().equalsIgnoreCase("Select SQL statements executed")){
							Select_executed = db_tmpData[1].trim();	
						}
						//
						if(db_tmpData!=null&&db_tmpData.length>1&&db_tmpData[0].trim().equalsIgnoreCase("Update/Insert/Delete statements executed")){
							Update_Insert_Delete_executed = db_tmpData[1].trim();	
						}
						//ʧ�ܵ�����
						if(db_tmpData!=null&&db_tmpData.length>1&&db_tmpData[0].trim().equalsIgnoreCase("Failed statement operations")){
							Failed_operations = db_tmpData[1].trim();	
						}
						//��ռ���ͬ��������д����
						if(db_tmpData!=null&&db_tmpData.length>1&&db_tmpData[0].trim().equalsIgnoreCase("Buffer pool index writes")){
							Buffer_pool_index_writes = db_tmpData[1].trim();	
						}
						//
						if(db_tmpData!=null&&db_tmpData.length>1&&db_tmpData[0].trim().equalsIgnoreCase("Direct write elapsed time (ms)")){
							write_elapsed_time = db_tmpData[1].trim();	
						}
						//
						if(db_tmpData!=null&&db_tmpData.length>1&&db_tmpData[0].trim().equalsIgnoreCase("Direct writes")){
							Direct_writes = db_tmpData[1].trim();	
						}
						//
						if(db_tmpData!=null&&db_tmpData.length>1&&db_tmpData[0].trim().equalsIgnoreCase("Direct reads elapsed time (ms)")){
							reads_elapsed_time = db_tmpData[1].trim();	
						}
						//
						if(db_tmpData!=null&&db_tmpData.length>1&&db_tmpData[0].trim().equalsIgnoreCase("Direct reads")){
							Direct_reads = db_tmpData[1].trim();	
						}
						//��ռ���ͬ��������������
						if(db_tmpData!=null&&db_tmpData.length>1&&db_tmpData[0].trim().equalsIgnoreCase("Buffer pool index physical reads")){
							Buffer_pool_index_physical = db_tmpData[1].trim();	
						}
						//��ռ����첽������д����
						if(db_tmpData!=null&&db_tmpData.length>1&&db_tmpData[0].trim().equalsIgnoreCase("Asynchronous pool index page writes")){
							Asynchronous_pool_index_writes = db_tmpData[1].trim();	
						}
						//��ռ����첽������������
						if(db_tmpData!=null&&db_tmpData.length>1&&db_tmpData[0].trim().equalsIgnoreCase("Asynchronous pool index page reads")){
							Asynchronous_pool_index_reads = db_tmpData[1].trim();	
						}
						//buffer pool��дʱ��
						if(db_tmpData!=null&&db_tmpData.length>1&&db_tmpData[0].trim().equalsIgnoreCase("Total buffer pool write time (ms)")){
							Buffer_pool_write_time = db_tmpData[1].trim();	
						}
						//
						if(db_tmpData!=null&&db_tmpData.length>1&&db_tmpData[0].trim().equalsIgnoreCase("Buffer pool data logical write")){
							Buffer_pool_logical_write = db_tmpData[1].trim();	
						}
						//buffer pool�Ķ�ʱ�� 
						if(db_tmpData!=null&&db_tmpData.length>1&&db_tmpData[0].trim().equalsIgnoreCase("Total buffer pool read time (ms)")){
							Buffer_pool_read_time = db_tmpData[1].trim();	
						}
						//buffer pool������
						if(db_tmpData!=null&&db_tmpData.length>1&&db_tmpData[0].trim().equalsIgnoreCase("Buffer pool data logical reads")){
							Buffer_pool_logical_read = db_tmpData[1].trim();	
						}
						//catalog cache�������
						if(db_tmpData!=null&&db_tmpData.length>1&&db_tmpData[0].trim().equalsIgnoreCase("Catalog cache overflows")){
							Catalog_cache_overflows = db_tmpData[1].trim();	
						}
						//buffer poolд����
						if(db_tmpData!=null&&db_tmpData.length>1&&db_tmpData[0].trim().equalsIgnoreCase("Buffer pool data writes")){
							Buffer_pool_data_writes = db_tmpData[1].trim();	
						}
						//buffer pool������
						if(db_tmpData!=null&&db_tmpData.length>1&&db_tmpData[0].trim().equalsIgnoreCase("Buffer pool data physical reads")){
							Buffer_pool_physical_read = db_tmpData[1].trim();	
						}
						//Ӧ�ù�������������
						if(db_tmpData!=null&&db_tmpData.length>1&&db_tmpData[0].trim().equalsIgnoreCase("Application section inserts")){
							Application_section_inserts = db_tmpData[1].trim();	
						}
						//Ӧ�ù�������������
						if(db_tmpData!=null&&db_tmpData.length>1&&db_tmpData[0].trim().equalsIgnoreCase("Application section lookups")){
							Application_section_lookups = db_tmpData[1].trim();	
						}
						//
						if(db_tmpData!=null&&db_tmpData.length>1&&db_tmpData[0].trim().equalsIgnoreCase("Package cache inserts")){
							Package_cache_inserts = db_tmpData[1].trim();	
						}
						//
						if(db_tmpData!=null&&db_tmpData.length>1&&db_tmpData[0].trim().equalsIgnoreCase("Package cache lookups")){
							Package_cache_lookups = db_tmpData[1].trim();	
						}
						//
						if(db_tmpData!=null&&db_tmpData.length>1&&db_tmpData[0].trim().equalsIgnoreCase("Catalog cache inserts")){
							Catalog_cache_inserts = db_tmpData[1].trim();	
						}
						//
						if(db_tmpData!=null&&db_tmpData.length>1&&db_tmpData[0].trim().equalsIgnoreCase("Catalog cache lookups")){
							Catalog_cache_lookups = db_tmpData[1].trim();	
						}
						//
						if(db_tmpData!=null&&db_tmpData.length>1&&db_tmpData[0].trim().equalsIgnoreCase("Buffer pool index logical reads")){
							Buffer_pool_index_logical = db_tmpData[1].trim();	
						}
						//
						if(db_tmpData!=null&&db_tmpData.length>1&&db_tmpData[0].trim().equalsIgnoreCase("Total Shared Sort heap allocated")){
							Shared_Sort = db_tmpData[1].trim();	
						}
					}
				}
//				ƽ������ʱ��
				if(!Sort_time.trim().equalsIgnoreCase("Not Collected")&&!Total_sorts.trim().equalsIgnoreCase("Not Collected")&&Double.parseDouble(Total_sorts)!=0){
					avg_sort_time = df.format(Double.parseDouble(Sort_time)/Double.parseDouble(Total_sorts))+"ms";
				}
				//���ݿ���SQL�ع��ٷֱ�
				Internal_percent = df.format(Double.parseDouble(Internal_rollbacks)*100/(Double.parseDouble(Internal_commits)+Double.parseDouble(Internal_rollbacks)))+"%";
			    //���ݿ�����ִ�е�����SQL��Ŀ
				all_sql_nums = (Integer.parseInt(Select_executed)+Integer.parseInt(Update_Insert_Delete_executed))+"";
				//SQLִ��ʧ�ܰٷֱ�
				Failed_operations_per = df.format(Double.parseDouble(Failed_operations)*100/Double.parseDouble(all_sql_nums))+"%";
				//ƽ����ռ�ֱ��дʱ��
				if(!write_elapsed_time.trim().equalsIgnoreCase("Not Collected")&&!Direct_writes.trim().equalsIgnoreCase("Not Collected")&&Double.parseDouble(Direct_writes)!=0){
					Direct_writes_time =  df.format(Double.parseDouble(write_elapsed_time)/Double.parseDouble(Direct_writes))+"ms";
				}
                //ƽ����ռ�ֱ�Ӷ�ʱ��
				if(!reads_elapsed_time.trim().equalsIgnoreCase("Not Collected")&&!Direct_reads.trim().equalsIgnoreCase("Not Collected")){
					Direct_reads_time =  df.format(Double.parseDouble(reads_elapsed_time)/Double.parseDouble(Direct_reads))+"ms";
				}
				//��ռ�ƽ����buffer poolдʱ��
				if(!Buffer_pool_write_time.trim().equalsIgnoreCase("Not Collected")&&!Buffer_pool_logical_write.trim().equalsIgnoreCase("Not Collected")){
					avg_buffer_pool_write_time =  df.format(Double.parseDouble(Buffer_pool_write_time)/Double.parseDouble(Buffer_pool_logical_write))+"ms";
				}
				//��ռ�ƽ����buffer pool��ʱ
				if(!Buffer_pool_read_time.trim().equalsIgnoreCase("Not Collected")&&!Buffer_pool_logical_read.trim().equalsIgnoreCase("Not Collected")){
					avg_buffer_pool_reads_time =  df.format(Double.parseDouble(Buffer_pool_read_time)/Double.parseDouble(Buffer_pool_logical_read))+"ms";
				}
				//package cache������
				if(!Package_cache_inserts.trim().equalsIgnoreCase("Not Collected")&&!Package_cache_lookups.trim().equalsIgnoreCase("Not Collected")){
					package_cache =  df.format((1-(Double.parseDouble(Package_cache_inserts) / Double.parseDouble(Package_cache_lookups))) * 100)+"%";
				}
				//catlog cache������
				if(!Catalog_cache_inserts.trim().equalsIgnoreCase("Not Collected")&&!Catalog_cache_lookups.trim().equalsIgnoreCase("Not Collected")){
					catlog_cache =  df.format((1-(Double.parseDouble(Catalog_cache_inserts) / Double.parseDouble(Catalog_cache_lookups))) * 100)+"%";
				}
				//buffer pool����������
				if(!Buffer_pool_index_physical.trim().equalsIgnoreCase("Not Collected")&&!Buffer_pool_index_logical.trim().equalsIgnoreCase("Not Collected")){
					buffer_pool_index =  df.format((1-(Double.parseDouble(Buffer_pool_index_physical) / Double.parseDouble(Buffer_pool_index_logical))) * 100)+"%";
				}
				//buffer pool������
				if(!Buffer_pool_physical_read.trim().equalsIgnoreCase("Not Collected")&&!Buffer_pool_logical_read.trim().equalsIgnoreCase("Not Collected")){
					buffer_pool =  df.format((1-(Double.parseDouble(Buffer_pool_physical_read) / Double.parseDouble(Buffer_pool_logical_read))) * 100)+"%";
				}
			}
		}
		
		String tablespaces_Content = "";
		tmpPt = Pattern.compile("(cmdbegin:tablespaces)(.*)(cmdbegin:locks)",Pattern.DOTALL);
		mr = tmpPt.matcher(fileContent.toString());
		if(mr.find())
		{
			tablespaces_Content = mr.group(2);
		} 
		String[] tablespaces_LineArr = null;
		String[] tablespaces_tmpData = null;
		try{
			if(tablespaces_Content!=null&&tablespaces_Content.trim().length()>0){
				tablespaces_LineArr = tablespaces_Content.split("\n");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		String locks_Content = "";
		tmpPt = Pattern.compile("(cmdbegin:locks)(.*)(cmdbegin:bufferpools)",Pattern.DOTALL);
		mr = tmpPt.matcher(fileContent.toString());
		if(mr.find())
		{
			try{
				locks_Content = mr.group(2);
			}catch(Exception e){
				e.printStackTrace();
			}
			
		} 
		String[] locks_LineArr = null;
		String[] locks_tmpData = null;
		try{
			if(locks_Content!=null&&locks_Content.trim().length()>0){
				locks_LineArr = locks_Content.split("\n");
			}
			if(locks_LineArr!=null&&locks_LineArr.length>1)
			{    			
				locks_tmpData = locks_LineArr[2].trim().split("\\s++");          			
				if(locks_tmpData != null){
					
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		String bufferpools_Content = "";
		tmpPt = Pattern.compile("(cmdbegin:bufferpools)(.*)(cmdbegin:end)",Pattern.DOTALL);
		mr = tmpPt.matcher(fileContent.toString());
		if(mr.find())
		{
			bufferpools_Content = mr.group(2);	
		} 
		String[] bufferpools_LineArr = null;
		String[] bufferpools_tmpData = null;
		try {
			if(bufferpools_Content!=null&&bufferpools_Content.trim().length()>0){
				bufferpools_LineArr = bufferpools_Content.split("\n");
			}
			if(bufferpools_LineArr!=null&&bufferpools_LineArr.length>0){
				for(int i=0; i<bufferpools_LineArr.length;i++){
				}
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
//		ShareData.getSharedata().put(host.getIpAddress(), returnHash);
		ipAllData.put("Lock_Timeouts", Lock_Timeouts);
		ipAllData.put("Lock_waits", Lock_waits);
		ipAllData.put("Lock_escalations", Lock_escalations);
		ipAllData.put("e_Lock_escalations", e_Lock_escalations);
		ipAllData.put("Deadlocks_detected", Deadlocks_detected);
		ipAllData.put("Lock_waits_Times", Lock_waits_Times);
		ipAllData.put("Locks_held_currently", Locks_held_currently);
		ipAllData.put("Sort_overflows", Sort_overflows);
		ipAllData.put("Sort_heap", Sort_heap);
		ipAllData.put("avg_sort_time", avg_sort_time);
		ipAllData.put("Internal_rollbacks", Internal_rollbacks);
		ipAllData.put("Internal_commits", Internal_commits);
		ipAllData.put("rollbacks_deadlock", rollbacks_deadlock);
		ipAllData.put("Secondary_logs", Secondary_logs);
		ipAllData.put("secondary_log_space", secondary_log_space);
		ipAllData.put("Number_read_log", Number_read_log);
		ipAllData.put("Number_write_log", Number_write_log);
		ipAllData.put("Internal_percent", Internal_percent);
		ipAllData.put("all_sql_nums", all_sql_nums);
		ipAllData.put("Buffer_pool_index_physical", Buffer_pool_index_physical);
		ipAllData.put("Asynchronous_pool_index_writes", Asynchronous_pool_index_writes);
		ipAllData.put("Asynchronous_pool_index_reads", Asynchronous_pool_index_reads);
		ipAllData.put("avg_buffer_pool_write_time", avg_buffer_pool_write_time);
		ipAllData.put("avg_buffer_pool_reads_time", avg_buffer_pool_reads_time);
		ipAllData.put("Catalog_cache_overflows", Catalog_cache_overflows);
		ipAllData.put("Buffer_pool_data_writes", Buffer_pool_data_writes);
		ipAllData.put("Application_section_inserts", Application_section_inserts);
		ipAllData.put("Application_section_lookups", Application_section_lookups);
		ipAllData.put("package_cache", package_cache);
		ipAllData.put("catlog_cache", catlog_cache);
		ipAllData.put("buffer_pool_index", buffer_pool_index);
		ipAllData.put("buffer_pool", buffer_pool);
		ipAllData.put("Select_executed", Select_executed);
		ipAllData.put("Failed_operations", Failed_operations);
		ipAllData.put("Failed_operations_per", Failed_operations_per);
		ipAllData.put("Buffer_pool_index_writes", Buffer_pool_index_writes);
		ipAllData.put("write_elapsed_time", write_elapsed_time);
		ipAllData.put("Direct_writes_time", Direct_writes_time);
		ipAllData.put("Direct_reads_time", Direct_reads_time);
		ipAllData.put("Buffer_pool_index_logical", Buffer_pool_index_logical);
		ipAllData.put("Buffer_pool_write_time", Buffer_pool_write_time);
		ipAllData.put("Buffer_pool_logical_write", Buffer_pool_logical_write);
		ipAllData.put("Buffer_pool_read_time", Buffer_pool_read_time);
		ipAllData.put("Buffer_pool_logical_read", Buffer_pool_logical_read);
		ipAllData.put("Buffer_pool_physical_read", Buffer_pool_physical_read);
		ipAllData.put("Package_cache_inserts", Package_cache_inserts);
		ipAllData.put("Shared_Sort", Shared_Sort);
		allDb2Data.put(this.ipaddress, ipAllData);
		return ipAllData;
    }	
	
}






