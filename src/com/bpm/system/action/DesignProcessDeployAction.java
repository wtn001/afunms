package com.bpm.system.action;
/**
 *  Description:
 * 部署在线设计的流程
 * @author ywx
 *
 */
import javax.annotation.Resource;

import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.bpm.system.service.SystemService;
import com.bpm.system.utils.StringUtil;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

@Controller
@Scope("prototype")
public class DesignProcessDeployAction extends ActionSupport {
	/**
	 * 
	 */
	private static final long serialVersionUID = 51323663287683247L;
	@Resource
	private SystemService systemService;
	private String[] checkbox;
	
	@Override
	public String execute() throws Exception {
		/*String url=ServletActionContext.getServletContext().getRealPath("form")+"/";
		String url2=ServletActionContext.getServletContext().getRealPath("tempform")+"/";*/
		systemService.designProcessDeploy(checkbox,null,null);
		return SUCCESS;
	}

	public String[] getCheckbox() {
		return checkbox;
	}

	public void setCheckbox(String[] checkbox) {
		this.checkbox = checkbox;
	}
	


}
