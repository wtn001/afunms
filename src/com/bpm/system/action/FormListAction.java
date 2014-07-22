package com.bpm.system.action;

/**
 * 
 */

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.afunms.common.base.JspPage;
import com.bpm.system.model.FormModel;
import com.bpm.system.service.SystemService;

@Controller
@Scope("prototype")
public class FormListAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3349484350058498445L;

	private String perpagenum="";//ÿҳ��Ҫ��ʾ�ļ�¼��
	private JspPage jsppage=new JspPage();//��ҳ��ѯ
	private String jp="";  //��ǰҳ
	@Resource
	private SystemService systemService;
	@Override
	public String execute() throws Exception {
		  jsppage.setCurrentPage(jp);//���õ�ǰҳ�����
		  jsppage.setPerPage(perpagenum);//����ÿҳ��Ҫ��ȡ�ļ�¼��
          jsppage = systemService.findFormList(jsppage.getCurrentPage(),jsppage.getPerPage());
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
