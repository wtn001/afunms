package com.gathertask;
/**
 * �ó���������ʵ�ְ�ʱ��ִ�и��ֳ��ص�򲦲��Э��
 */
import java.lang.reflect.Method;
import java.util.TimerTask;
import org.apache.log4j.Logger;

import com.afunms.indicators.model.NodeGatherIndicators;
import com.gatherdb.nmsmemorydate;
import java.util.Hashtable;




/***
 * 
 * 
 * @author konglq
 *
 */
public class BaskTask extends TimerTask{
	
	 Logger logger = Logger.getLogger(BaskTask.class);
	 //private Hashtable ht=new Hashtable(30);
	 private String runclasspath="";
	 private String runtime="";
	 private String runtype="";//  
	 private String taskid="";
	 private String taskname="";
	 private String nodeid="";
	 private NodeGatherIndicators gather=new NodeGatherIndicators();
	 
	 /**
	  * 
	  * �������߳�
	  * �ɼ��������÷���
	  */
	 public void run() {
		 
		 
		 //logger.info("--����--nodeid"+nodeid+"--"+taskname+"--");
		 Object runner;
		 Method mt;
		 try {
			 
			 runner=Class.forName(runclasspath.trim()).newInstance();
			 mt=runner.getClass().getMethod("collect_Data",NodeGatherIndicators.class);
			// Hashtable result=(Hashtable)mt.invoke(runner, gather);
			 mt.invoke(runner, gather);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		 
		//logger.info("--����--nodeid"+nodeid+"--"+taskname+"--"+"--���н���");
		 	 
	 }
	 
	 
 
	 
	public String getRunclasspath() {
		return runclasspath;
	}


	public void setRunclasspath(String runclasspath) {
		this.runclasspath = runclasspath;
	}


	public String getRuntime() {
		return runtime;
	}


	public String getruntype() {
		return runtype;
	}


	public void setruntype(String runtype) {
		this.runtype = runtype;
	}


	public String getTaskid() {
		return taskid;
	}



	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}



	public String getTaskname() {
		return taskname;
	}



	public void setTaskname(String taskname) {
		this.taskname = taskname;
	}



	public String getNodeid() {
		return nodeid;
	}



	public void setNodeid(String nodeid) {
		this.nodeid = nodeid;
	}


	public NodeGatherIndicators getGather() {
		return gather;
	}



	public void setGather(NodeGatherIndicators gather) {
		this.gather = gather;
	}

	 
	 
	 




}
