package com.bpm.system.action;
/**
 *  Description:
 * ���̶����б����Ѿ�����ɹ��������б�
 * @author ywx
 *
 */
import java.util.List;

import javax.annotation.Resource;

import org.activiti.engine.repository.ProcessDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.afunms.common.base.JspPage;
import com.bpm.system.service.SystemService;
import com.opensymphony.xwork2.ActionSupport;

@Controller
@Scope("prototype")
public class ProcessDefineListAction extends ActionSupport {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3369119189966248651L;
	@Resource
	private SystemService systemService;
	private String perpagenum="";//ÿҳ��Ҫ��ʾ�ļ�¼��
	private JspPage jsppage=new JspPage();//��ҳ��ѯ
	private String jp="";  //��ǰҳ
	private String result;
	
	@Override
	public String execute() throws Exception {
		jsppage.setCurrentPage(jp);//���õ�ǰҳ�����
		jsppage.setPerPage(perpagenum);//����ÿҳ��Ҫ��ȡ�ļ�¼��
		jsppage=systemService.findPdList(jsppage.getCurrentPage(),jsppage.getPerPage());
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

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
	
	
}
