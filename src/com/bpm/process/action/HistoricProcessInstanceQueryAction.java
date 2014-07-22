package com.bpm.process.action;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.afunms.common.base.JspPage;
import com.bpm.process.service.ProcessService;
import com.opensymphony.xwork2.ActionSupport;
/**
 * �����ۺϲ�ѯ
 * @author HXL
 * 2012-12-13
 */

@Controller
@Scope("prototype")
public class HistoricProcessInstanceQueryAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6658956154977827081L;
	private String status;//״̬
	private String startuser;//������
	private String starttime1;//��ʼʱ��1
	private String starttime2;//��ʼʱ��2
	private String perpagenum="";//ÿҳ��Ҫ��ʾ�ļ�¼��
	private JspPage jsppage=new JspPage();//��ҳ��ѯ
	private String jp="";  //��ǰҳ
	@Resource
	private ProcessService processService;
	
	
	@Override
	public String execute() throws Exception {
		jsppage.setCurrentPage(jp);//���õ�ǰҳ�����
		jsppage.setPerPage(perpagenum);//����ÿҳ��Ҫ��ȡ�ļ�¼��
		jsppage = processService.queryHistoricProcessInstance(status, startuser, starttime1, starttime2,jsppage.getCurrentPage(),jsppage.getPerPage());
		return SUCCESS;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public String getStartuser() {
		return startuser;
	}


	public void setStartuser(String startuser) {
		this.startuser = startuser;
	}


	public String getStarttime1() {
		return starttime1;
	}


	public void setStarttime1(String starttime1) {
		this.starttime1 = starttime1;
	}


	public String getStarttime2() {
		return starttime2;
	}


	public void setStarttime2(String starttime2) {
		this.starttime2 = starttime2;
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
