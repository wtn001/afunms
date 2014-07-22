package com.bpm.process.action;
/**
 *  Description:
 * �ѽ����������б�
 * @author ywx
 *
 */
import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.afunms.common.base.JspPage;
import com.bpm.process.service.ProcessService;
import com.opensymphony.xwork2.ActionSupport;

@Controller
@Scope("prototype")
public class HistoricListAction extends ActionSupport {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3580781028603344307L;
	@Resource
	private ProcessService processService;
	private String perpagenum="";//ÿҳ��Ҫ��ʾ�ļ�¼��
	private JspPage jsppage=new JspPage();//��ҳ��ѯ
	private String jp="";  //��ǰҳ
	
	@Override
	public String execute() throws Exception {
		jsppage.setCurrentPage(jp);//���õ�ǰҳ�����
		jsppage.setPerPage(perpagenum);//����ÿҳ��Ҫ��ȡ�ļ�¼��
		jsppage=processService.findhistoricProcessInstanceList(jsppage.getCurrentPage(),jsppage.getPerPage());
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

}
