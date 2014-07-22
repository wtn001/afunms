package com.bpm.process.action;
/**
 *  Description:
 * �������
 * 
 *
 */

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.afunms.config.model.Knowledgebase;
import com.bpm.process.service.ProcessService;
import com.opensymphony.xwork2.ActionSupport;

@Controller
@Scope("prototype")
public class OrderSolutionListAction extends ActionSupport {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7502596866413701226L;
	@Resource
	private ProcessService processService;
	private String category;//���
	private String entity;//����
	private String subentitycon;//ָ��
	private String wordkey;//�ؼ���
	private List<Knowledgebase> list;
	
	@Override
	public String execute() throws Exception {
		list=processService.queryKnowledge(category, entity, subentitycon, wordkey);
		return SUCCESS;
	}

	public List<Knowledgebase> getList() {
		return list;
	}

	public void setList(List<Knowledgebase> list) {
		this.list = list;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getEntity() {
		return entity;
	}

	public void setEntity(String entity) {
		this.entity = entity;
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
