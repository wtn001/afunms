package com.bpm.process.action;
/**
 *  Description:
 * @author ywx
 * 
 *
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.afunms.common.base.JspPage;
import com.afunms.common.util.SessionConstant;
import com.afunms.system.model.User;
import com.bpm.process.service.ProcessService;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

@Controller
@Scope("prototype")
public class TaskClaimListAction extends ActionSupport {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5880298586266544273L;
	@Resource
	private ProcessService processService;
	private String perpagenum="";//ÿҳ��Ҫ��ʾ�ļ�¼��
	private JspPage jsppage=new JspPage();//��ҳ��ѯ
	private String jp="";  //��ǰҳ
	private Map<String,Object> session;
	private List<String> listcontent;
	private List<String> contentList=new ArrayList<String>();
	private Map<String,String> taskUsers = new HashMap<String, String>();
	@Override
	public String execute() throws Exception {
		session=ActionContext.getContext().getSession();
		User user=(User)session.get(SessionConstant.CURRENT_USER);
		if(user==null) return ERROR;
		jsppage.setCurrentPage(jp);//���õ�ǰҳ�����
		jsppage.setPerPage(perpagenum);//����ÿҳ��Ҫ��ȡ�ļ�¼��
		jsppage=processService.findTaskClaimList(user.getUserid(),jsppage.getCurrentPage(),jsppage.getPerPage());
		contentList=processService.findContentList(jsppage.getList());
		taskUsers = processService.findBackUserTaskAssignee(jsppage.getList());
		return SUCCESS;
	}
	public String getPerpagenum() {
		return perpagenum;
	}
	public void setPerpagenum(String perpagenum) {
		this.perpagenum = perpagenum;
	}
	public JspPage getJsppage() {
		return jsppage;
	}
	public void setJsppage(JspPage jsppage) {
		this.jsppage = jsppage;
	}
	public String getJp() {
		return jp;
	}
	public void setJp(String jp) {
		this.jp = jp;
	}
	public List<String> getListcontent() {
		return listcontent;
	}
	public void setListcontent(List<String> listcontent) {
		this.listcontent = listcontent;
	}
	public List<String> getContentList() {
		return contentList;
	}
	public void setContentList(List<String> contentList) {
		this.contentList = contentList;
	}
	public Map<String, String> getTaskUsers() {
		return taskUsers;
	}
	public void setTaskUsers(Map<String, String> taskUsers) {
		this.taskUsers = taskUsers;
	}
	
	
}
