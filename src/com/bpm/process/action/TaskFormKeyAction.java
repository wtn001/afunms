package com.bpm.process.action;
/**
 *  Description:
 * ����taskId���鿴���̵�form��
 * @author ywx
 *
 */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.afunms.config.dao.KnowledgebaseDao;
import com.bpm.process.service.ProcessService;
import com.bpm.system.utils.ConstanceUtil;
import com.bpm.system.utils.StringUtil;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

@Controller
@Scope("prototype")
public class TaskFormKeyAction extends ActionSupport {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4003288897427750845L;
	@Resource
	private ProcessService processService;
	private String taskId;
	private String taskExtId;
	private String taskName;
	private String formKey;
	private List<ActivityImpl> backAIList;
	private String jspname;
	private ActivityImpl afterActivityImpl;//�������̷����˽ڵ�
	private String startTime;
	private Map<String,Object> map;//���̱���
	private String flag;//Ϊfirst��ʾͨ���澯��Ϣ���������̵ĵ�һ��������	private String afterActivityId;//������󻷽�ID��
	private String afterActivityId;//������󻷽�ID��
	//private String isbanjie;//�Ƿ���Ҫ��ᣬ0����Ҫ��1��Ҫ
	private String isbanjiebutton;//�Ƿ���Ҫ��ᰴť��0����Ҫ��1��Ҫ
	@Override
	public String execute() throws Exception {
		formKey=processService.findFormKeyContent(taskId);
		//backAIList=processService.findBackAIList(taskId);
		if(ConstanceUtil.BEFORE_END.equals(taskName)) {
			backAIList = new ArrayList<ActivityImpl>();
			afterActivityImpl=null;
		}else {
			backAIList=processService.findBackAIList(taskId);
			afterActivityImpl=processService.findAfterActivityImpl(taskId);
		}
		startTime = processService.getProcessInstanceStartTime(taskId);
	    map = processService.getProcessVariables(taskId);
	    KnowledgebaseDao dao=new KnowledgebaseDao();
		String findselect=dao.selectcontent3();
		ActionContext.getContext().getSession().put("findselect", findselect);
		//afterActivityId=(String) map.get("afterActivityId");
		//������̱������޴�ֵ���ٲ�ѯ���ִ�л���
		afterActivityId=(String) map.get("afterActivityId");
		//isbanjie=(String)map.get("isbanjie");
		isbanjiebutton=(String)map.get("isbanjiebutton");
		if(StringUtil.isBlank(afterActivityId))
		{
			afterActivityImpl=processService.findAfterActivityImpl(taskId);
		}
		createFile();
		return SUCCESS;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}


	public String getTaskExtId() {
		return taskExtId;
	}

	public void setTaskExtId(String taskExtId) {
		this.taskExtId = taskExtId;
	}

	public String getFormKey() {
		return formKey;
	}

	public void setFormKey(String formKey) {
		this.formKey = formKey;
	}

	public List<ActivityImpl> getBackAIList() {
		return backAIList;
	}

	public void setBackAIList(List<ActivityImpl> backAIList) {
		this.backAIList = backAIList;
	}

	public String getJspname() {
		return jspname;
	}

	public void setJspname(String jspname) {
		this.jspname = jspname;
	}
    
	private void createFile() throws Exception {
		String basepath = ServletActionContext.getServletContext().getRealPath("/template");
		File dir = new File(basepath);
		File files[] = dir.listFiles();
		if(files.length > 200) {
			for (int i = 0; i < files.length; i++) { 
	            if("task_form_key.jsp".equals(files[i].getName())) {
	            	continue ;
	            }else {
	            	files[i].delete();
	            }
	        } 
		}
		jspname = Calendar.getInstance().getTime().getTime()+"temp"+".jsp";
		File file = new File(basepath+"/"+jspname);
		if(!file.exists()) {
			file.createNewFile();
		}
		String path = basepath+"/task_form_key.jsp";
		FileInputStream fis = new FileInputStream(path);
        InputStreamReader ir = new InputStreamReader(fis,"UTF-8");
        BufferedReader br = new BufferedReader(ir);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file),"UTF-8"));
        String s;
        while((s=br.readLine())!=null)
        {
        	 bw.write(s);
        	 bw.write("\r\n");
        	 bw.flush();
        	if("<!--embed-form-->".equals(s.trim())) {
        		if(StringUtil.isNotBlank(formKey)) {
        			bw.write(formKey);
            		bw.write("\r\n");
            		bw.flush();
        		}
        	}
         	
        }
       
        bw.close();
        br.close();
	}

	public ActivityImpl getAfterActivityImpl() {
		return afterActivityImpl;
	}

	public void setAfterActivityImpl(ActivityImpl afterActivityImpl) {
		this.afterActivityImpl = afterActivityImpl;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public Map<String, Object> getMap() {
		return map;
	}

	public void setMap(Map<String, Object> map) {
		this.map = map;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getAfterActivityId() {
		return afterActivityId;
	}

	public void setAfterActivityId(String afterActivityId) {
		this.afterActivityId = afterActivityId;
	}

	/*public String getIsbanjie() {
		return isbanjie;
	}

	public void setIsbanjie(String isbanjie) {
		this.isbanjie = isbanjie;
	}*/

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getIsbanjiebutton() {
		return isbanjiebutton;
	}

	public void setIsbanjiebutton(String isbanjiebutton) {
		this.isbanjiebutton = isbanjiebutton;
	}
	
	
}

