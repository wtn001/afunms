package com.bpm.process.action;

/**
 * ��ȡ�Ѱ칤��
 */
import java.util.Map;

import javax.annotation.Resource;

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
public class ProcessUserFinishedAction extends ActionSupport {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8870471888420540776L;
	@Resource
	private ProcessService processService;
	private Map<String,Object> session;
	private String perpagenum="";//ÿҳ��Ҫ��ʾ�ļ�¼��
	private JspPage jsppage=new JspPage();//��ҳ��ѯ
	private String jp="";  //��ǰҳ
	
	@Override
	public String execute() throws Exception {
		session=ActionContext.getContext().getSession();
		User user=(User)session.get(SessionConstant.CURRENT_USER);
		if(user==null) return ERROR;
		jsppage.setCurrentPage(jp);//���õ�ǰҳ�����
		jsppage.setPerPage(perpagenum);//����ÿҳ��Ҫ��ȡ�ļ�¼��
		jsppage= processService.findHistoricProcessInstance(user.getUserid(),jsppage.getCurrentPage(),jsppage.getPerPage());
		return SUCCESS;
	}
	public Map<String, Object> getSession() {
		return session;
	}
	public void setSession(Map<String, Object> session) {
		this.session = session;
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

}
