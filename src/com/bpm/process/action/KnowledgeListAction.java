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
import com.afunms.config.dao.KnowledgebaseDao;
import com.bpm.process.service.ProcessService;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

@Controller
@Scope("prototype")
public class KnowledgeListAction extends ActionSupport {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5426153601985990225L;
	@Resource
	private ProcessService processService;
	private String perpagenum="";//ÿҳ��Ҫ��ʾ�ļ�¼��
	private JspPage jsppage=new JspPage();//��ҳ��ѯ
	private String jp="";  //��ǰҳ
	private String categorycon;//���
	private String entitycon;//����
	private String subentitycon;//ָ��
	private String wordkey;//�ؼ���
	
	@Override
	public String execute() throws Exception {
		jsppage.setCurrentPage(jp);//���õ�ǰҳ�����
		jsppage.setPerPage(perpagenum);//����ÿҳ��Ҫ��ȡ�ļ�¼��
		KnowledgebaseDao dao=new KnowledgebaseDao();
		String findselect=dao.selectcontent();
		ActionContext.getContext().getSession().put("findselect", findselect);
		jsppage=processService.queryKnowledge(categorycon, entitycon, subentitycon, wordkey, jsppage.getCurrentPage(), jsppage.getPerPage());
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

    
	public String getCategorycon() {
		return categorycon;
	}

	public void setCategorycon(String categorycon) {
		this.categorycon = categorycon;
	}

	public String getEntitycon() {
		return entitycon;
	}

	public void setEntitycon(String entitycon) {
		this.entitycon = entitycon;
	}

	public String getSubentitycon() {
		return subentitycon;
	}

	public void setSubentitycon(String subentitycon) {
		this.subentitycon = subentitycon;
	}

	public String getWordkey() {
		return wordkey;
	}

	public void setWordkey(String wordkey) {
		this.wordkey = wordkey;
	}
	
	

}
