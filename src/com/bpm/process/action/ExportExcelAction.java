package com.bpm.process.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.annotation.Resource;

import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.bpm.process.service.ExportExcelService;
import com.bpm.process.model.SearchModel;
import com.opensymphony.xwork2.ActionSupport;

@Controller
@Scope("prototype")
public class ExportExcelAction extends ActionSupport {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2695155607653339982L;
	@Resource
	private ExportExcelService exportExcelService;
	private SearchModel model;
	private InputStream inputStream;
	private String fileName;
	private String contenttype;//导出内容类型
	
	public String downloadExcelFile() throws Exception {
		fileName=exportExcelService.export(contenttype,model);
		String path = ServletActionContext.getServletContext().getRealPath("/downloadfile")+"/"+fileName;
		File file = new File(path);
		inputStream = new FileInputStream(file);
		return SUCCESS;
	}

	public SearchModel getModel() {
		return model;
	}

	public void setModel(SearchModel model) {
		this.model = model;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getContenttype() {
		return contenttype;
	}

	public void setContenttype(String contenttype) {
		this.contenttype = contenttype;
	}
	
}
