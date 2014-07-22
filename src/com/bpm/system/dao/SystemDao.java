package com.bpm.system.dao;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import javax.annotation.Resource;
import javax.imageio.stream.ImageInputStream;

import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.DeploymentBuilder;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.python.modules.newmodule;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.afunms.common.base.JspPage;
import com.afunms.common.util.DBManager;
import com.afunms.common.util.SysLogger;
import com.afunms.initialize.ResourceCenter;
import com.afunms.system.model.Codedetail;
import com.bpm.system.model.FormModel;
import com.bpm.system.model.Group_Menu;
import com.bpm.system.model.Menu;
import com.bpm.system.model.UserModel;
import com.bpm.system.service.SystemService;
import com.bpm.system.utils.UUIDKey;
import com.ibm.db2.jcc.a.b;
import com.sun.org.apache.bcel.internal.generic.NEW;

/**
 * 
 * Description:ϵͳ����ģ��dao�㡣
 * SystemDao.java Create on 2012-10-18 ����2:50:37 
 * @author hexinlin
 * Copyright (c) 2012 DHCC Company,Inc. All Rights Reserved.
 */
@Repository
@Transactional
public class SystemDao {

	//private String COMPILE="activiti:formKey";//ƥ���ʽ
    private static Logger logger = Logger.getLogger(SystemDao.class);
	@Resource
    private SessionFactory sessionFactory;
	
	@Resource
	private IdentityService identityService;
	@Resource
	private RepositoryService repositoryService;
	
	@Resource
	private SystemService systemService;
	
	 /**
	  * 
	  * Description: ��ȡ�˵���Ϣ
	  * Date:2012-10-18
	  * @author hexinlin
	  * @return List<Menu>
	  */
     public List<Menu> queryAllMenu() {
		List<Menu> list = new ArrayList<Menu>();
		Session session = sessionFactory.getCurrentSession();
		Query query =  session.createQuery("from bpm_menu order by sort,parent_id,menu_seq asc");
	    list = query.list();
	    return list;
     }
     /**
      * 
      * Description:��ȡһ���˵�
      * Date:2012-10-21
      * @author hexinlin
      * @return List<Menu>
      */
     public List<Menu> queryOneLevelMenu() {
    	 List<Menu> list = new ArrayList<Menu>();
    	 Session session = sessionFactory.getCurrentSession();
    	 Query query = session.createQuery("from bpm_menu where menu_id<>'-1' and parent_id='-1' order by menu_seq asc");
    	 list = query.list();
    	 return list;
     }
     
     /**
      * 
      * Description:ɾ��һ���˵������ж����˵��Ĳ���ɾ����
      * Date:2012-10-21
      * @author hexinlin
      * @return String
      */
     public void deleteOneLevelMenuByIds(String checkbox[]) {
    	 Session session = sessionFactory.getCurrentSession();
    	
    	 String temp = "";
    	 for(String str:checkbox) {
    		 temp = temp+str+",";
    	 }
    	 temp = temp.length()>0?temp.substring(0,temp.length()-1):"''";
    	 Query query = session.createQuery("delete from  bpm_menu where menu_id in ("+temp+") and childrennum = ? ");
    	 query.setParameter(0, 0);
		 query.executeUpdate();
     }
     
     /**
      * 
      * Description:�޸�һ���˵���Ϣ��
      * Date:2012-10-21
      * @author hexinlin
      * @return String
      */
     public void modifyMenu(String menu_name,String menu_url,int menu_id) {
    	 Session session = sessionFactory.getCurrentSession();
    	 Query query = session.createQuery("update bpm_menu set menu_name=?,menu_url=? where menu_id=?");
    	 query.setParameter(0, menu_name);
    	 query.setParameter(1, menu_url);
    	 query.setParameter(2, menu_id);
 		 query.executeUpdate();
     }
     
     /**
      * 
      * Description:����һ���˵�
      * Date:2012-10-21
      * @author hexinlin
      * @return void
      */
     public void addOneLevelMenu(Menu menu) {
    	 sessionFactory.getCurrentSession().save(menu);
     }
     
     /**
      * 
      * Description:�洢�û����ڲ˵��Ķ�Ӧ��ϵ
      * Date:2012-10-21
      * @author hexinlin
      * @return void
      */
     public void saveGroupMenu(String groupId,int checkbox[]) {
    	 Session session = sessionFactory.getCurrentSession();
    	 Query query = session.createQuery("delete from group_menu where groupId=?");
    	 query.setParameter(0, groupId);
    	 query.executeUpdate();
    	 Group_Menu gm = new Group_Menu();
    	 for(int menu_id: checkbox) {
    		 gm.setPriKey(UUIDKey.getKey());
    		 gm.setGroupId(groupId);
    		 gm.setMenu_id(menu_id);
    		 session.save(gm);
    	 }
     }
     
     /**
      * 
      * Description:��ȡһ���˵���������
      * Date:2012-10-22
      * @author hexinlin
      * @return int
      */
     public int getMaxSort(){
    	 Session session = sessionFactory.getCurrentSession();
    	 Query query = session.createQuery("select max(sort) from bpm_menu where parent_id=-1");
    	 int sort = (Integer)query.list().get(0);
    	 return sort;
     }
     
     /**
      * 
      * Description:��ȡ�����˵�
      * Date:2012-10-23
      * @author hexinlin
      * @return List<Menu>
      */
     public List<Menu> querysecmenu(int parent_id) {
    	 List<Menu> list = new ArrayList<Menu>();
    	 Session session = sessionFactory.getCurrentSession();
    	 Query query = session.createQuery("from bpm_menu where parent_id=?");
    	 query.setParameter(0, parent_id);
    	 list = query.list();
    	 return list;
     }
     /**
      * 
      * Description:���ݸ�ID��ȡ��ǰ�˵��µ����ID��
      * Date:2012-10-23
      * @author hexinlin
      * @return int
      */
     public int maxMenu_id(int parent_id) {
    	 Session session = sessionFactory.getCurrentSession();
    	 Query query = session.createQuery("select max(menu_id) from bpm_menu where sort=?");
    	 query.setParameter(0, parent_id);
    	 int id = (Integer)query.list().get(0);
    	 return id;
     }
     /**
      * 
      * Description:����µĶ����˵�
      * Date:2012-10-23
      * @author hexinlin
      * @return void
      */
     public void addNewSecMenu(Menu menu) {
    	 Session session = sessionFactory.getCurrentSession();
    	 session.save(menu);
    	 Query query = session.createQuery("update bpm_menu set childrennum=childrennum+1 where menu_id=?");
    	 query.setParameter(0, menu.getParent().getMenu_id());
    	 query.executeUpdate();
     }
     /**
      * 
      * Description:����ʵ��
      * Date:2012-10-23
      * @author hexinlin
      * @return void
      */
     public void updateObj(Object o) {
    	 Session session = sessionFactory.getCurrentSession();
    	 session.update(o);
     }
     /**
      * 
      * Description:ɾ���Ӳ˵�
      * Date:2012-10-23
      * @author hexinlin
      * @return void
      */
     public void delSecMenu(int menu_id,int parent_id) {
    	 Session session = sessionFactory.getCurrentSession();
    	 Query query = session.createQuery("delete from bpm_menu where menu_id=?");
    	 query.setParameter(0, menu_id);
    	 query.executeUpdate();
    	 query = session.createQuery("update bpm_menu set childrennum=childrennum-1 where menu_id=?");
    	 query.setParameter(0, parent_id);
    	 query.executeUpdate();
     }
     /**
      * 
      * Description:ɾ���û��飬���û���������齫���ܱ�ɾ��
      * Date:2012-10-23
      * @author hexinlin
      * @return void
      */
     public void delGroup(String checkbox[]) {
    	 Session session = sessionFactory.getCurrentSession();
    	 Query query = session.createSQLQuery("delete from act_id_group  where ID_ in(:name) and ID_ not in (select GROUP_ID_ from act_id_membership)");
    	 query.setParameterList("name", checkbox);
    	 query.executeUpdate();
     }
     
     /** 
      * �����Զ�������
      * @param checkbox
      */
     public void designProcessDeploy(String[] checkbox,String url,String url2)
     {
    	DBManager db=new DBManager();
     	String sql=getCheckBoxSql(checkbox);
     	ResultSet rSet=db.executeQuery(sql);
     	delpoyProcess(rSet);
     //	delpoyProcess(rSet, url,url2);
		try {
				if(rSet!=null)rSet.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		finally
		{
			db.close();
		}
     }
     
     /**
      * ��֯chebox��sql���
      * @param checkbox
      * @return
      */
     public String getCheckBoxSql(String[] checkbox)
     {
     	String sql="select name,bpmxml from act_ge_bytearray_temp where modelid in(";
     	for(String value:checkbox)
     	{
     		sql+="'"+value+"',";
     	}
     	sql=sql.substring(0, sql.length()-1);
     	sql+=")";
     	return sql;
     }
     
     /**
      * ƥ��formkey����������
      * @param rSet
      */
     public void delpoyProcess(ResultSet rSet)
     {
    	 ArrayList<String> formList=new ArrayList<String>();//��¼����formkey
    	 DBManager db=new DBManager();
    	 InputStream is=null;
    	 ResultSet rSet2=null;
    	 InputStream is2=null;
    	 try {
			while(rSet.next())
			 {
				String fileName=rSet.getString(1);
				is=rSet.getBinaryStream(2);
				formList=getFormList(is);
				is.reset();
				
				//���bpmxml�ļ�
				DeploymentBuilder deploymentBuilder=repositoryService.createDeployment();
				deploymentBuilder.addInputStream(fileName+".bpmn20.xml", is);
				
				//���form�ļ�
				String formSql=getFormSql(formList);
				if(null==formSql) {
					deploymentBuilder.deploy();
				}else {
					rSet2=db.executeQuery(formSql);
					while (rSet2.next()) {
						String name=rSet2.getString(1);
						is2=rSet2.getBinaryStream(2);
						deploymentBuilder.addInputStream(name, is2);
					}
					deploymentBuilder.deploy();
				}
				
				
				
			 }
		} catch (Exception e) {
			logger.error("SystemDao.delpoyProcess()����", e);
			e.printStackTrace();
		}
    	 finally{
				try {
					 if(rSet!=null)rSet.close();
					 if(rSet2!=null) rSet2.close();
					 if(is!=null) is.close();
					 if(is2!=null) is2.close();
				} catch (Exception e) {
					logger.error("rSet.close()����", e);
					e.printStackTrace();
				}
				db.close();
    	 }
     }
     
     /**
      * ����bpmxml�ļ�������formKey
      * @param is
      * @return
      */
     public ArrayList<String> getFormList(InputStream is)
     {
    	// PatternString ps=new PatternString();//ƥ�䴦��
    	 ArrayList<String> formList=new ArrayList<String>();
    	 
    	 InputStreamReader isr=null;
		 BufferedReader br=null;
			try {
				isr=new InputStreamReader(is, "UTF-8");
				br=new BufferedReader(isr);
				String line=null;
				while((line=br.readLine())!=null)
				{
					if(line.contains("activiti:formKey"))
					{
						String[] data=line.split("\"");
						String str=null;
						if(data.length==0) continue;
						for(int i=0;i<data.length;i++)
						{
							str=data[i];
							if(str.contains("activiti:formKey") && i<data.length-1)
							{
								formList.add(data[i+1]);
							}
						}
					}
					if(line.contains("bpmndi:BPMNDiagram")) break;
				}
			} catch (Exception e) {
				
				e.printStackTrace();
			}
			finally
			{
					try {
						if(br!=null) br.close();
						if(isr!=null) isr.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				
			}
			return formList;
     }
     
     
     
     /**
      * ��jdom����xml�ļ�����������
      * @param rSet
      * @return
      */
     public void delpoyProcess(ResultSet rSet,String url,String url2)
     {
    	 DBManager db=new DBManager();
		 ResultSet rSet2=null;
      	try {
  			while(rSet.next())
  			{
  				ArrayList<String> formList=new ArrayList<String>();
  				String fileName=rSet.getString(1);
  				InputStream is=rSet.getBinaryStream(2);
  				
  				//jdom����xml�ļ������xml���е�formkey
  				SAXBuilder sbld = new SAXBuilder();
  				Document cfgxml;
  				try {
 					cfgxml=sbld.build(is);
 					
 					Element rootElement = cfgxml.getRootElement();
 					//Element pElement=rootElement.getChild("process");
 					//Element bElement=rootElement.getChild("BPMNDiagram");
 					List<Element> list1=rootElement.getChildren();
 					for(Element element:list1)
 					{
 						if(element.getName().equals("process"))
 						{
 							List<Element> list2=element.getChildren();
 							{
 								for(Element element2:list2)
 								{
 									if(element2.getName().equals("userTask"))
 									{
 										/*String formKey=element2.getAttributeValue("formKey");
 										System.out.println(formKey);
 										System.out.println("^^^^^^^^^^^^");
 										*/
 										List<Attribute> list3=element2.getAttributes();
 										for(Attribute attribute:list3)
 										{
 											/*System.out.println(attribute.getName());
 											System.out.println(attribute.getValue());
 											System.out.println("^^^^^^^^^^^^");*/
 											if(attribute.getName().equals("formKey"))
 											{
 												formList.add(attribute.getValue());
 											}
 										}
 									}
 								}
 							}
 						}
 					//	System.out.println(element.getName());
 					}
 					
 					
 					//ֱ���ϴ�
 					//���bpmn20.xml
 					is.reset();
 					DeploymentBuilder deploymentBuilder=repositoryService.createDeployment();
 					deploymentBuilder.addInputStream(fileName+".bpmn20.xml", is);
 					is.close();
 					
 					//���form
 					String formSql=getFormSql(formList);
 					if(formSql==null) continue;
 					rSet2=db.executeQuery(formSql);
 					while (rSet2.next()) {
 						String name=rSet2.getString(1);
 						InputStream is2=rSet2.getBinaryStream(2);
 						deploymentBuilder.addInputStream(name, is2);
 						is2.close();
					}
 					deploymentBuilder.deploy();
 					
 					
 					//ѹ�����ϴ�
 					//xml�ļ�
 					/*OutputStream os = new BufferedOutputStream(new FileOutputStream(url2+fileName+".bpm20.xml"));
 					byte b[] = new byte[1024];
						while (is.read(b) > 0) {
							os.write(b);
						}
					   is.close();
					   os.close();
 					
					//form��
 					DBManager dbConn=new DBManager();
 					String formSql=getFormSql(formList);
 					ResultSet rSet2=dbConn.executeQuery(formSql);
 					while(rSet2.next())
 					{
 						String name=rSet2.getString(1);
 						InputStream is2=rSet2.getBinaryStream(2);
 						OutputStream os2 = new BufferedOutputStream(new FileOutputStream(url2+name));
 						while (is2.read(b) > 0) {
 							os2.write(b);
 						}
 					   is2.close();
 					   os2.close();
 					}
 			      	
 					CompressFile bean = new CompressFile();
 					System.out.println(url2);
 					System.out.println(url+"temp.zip");
 					bean.zip(url2, url+"temp.zip");
 					
 					File file=new File(url+"temp.zip");
 					systemService.deployProcess(file, "zip", "temp.zip", null);
 					*/
 					
 				} catch (JDOMException e) {
 					logger.error("����bpmxl����", e);
 					e.printStackTrace();
 				} catch (IOException e) {
 					logger.error("����bpmxl����", e);
 					e.printStackTrace();
 				}
  				
  				
  			}
  		} catch (SQLException e) {
  			e.printStackTrace();
  		}
      	finally
      	{
				try {
					if(rSet!=null) rSet.close();
					if(rSet2!=null) rSet2.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				db.close();
      	}
      	
     }
     
    /**
     * ��ѯxml������form�ļ�
     * @param formList
     * @return
     */
     public String getFormSql(ArrayList<String> formList)
     {
    	 if(formList==null || formList.size()==0) return null;
    	 String sql="select name,content from bpm_form where name in(";
      	for(String value:formList)
      	{
      		sql+="'"+value+"',";
      	}
      	sql=sql.substring(0, sql.length()-1);
      	sql+=")";
      	return sql;
     }
     
     /**
      * �ϴ���
      * @param file
      * @param fileName
      * @return
      */
     public String deployForm(File file,String fileName)
     {
     	String data[]=fileName.split("\\.");
     	if(data.length<1) return "error";
     	if(!(data[data.length-1].equals("form") || data[data.length-1].equals("zip"))) return "error";
     	if(data[data.length-1].equals("form"))
     	{
     		return handleForm(file, fileName, data);
     	}
     	else 
     	{
			return handleZip(file, fileName);
		}
     
     }

     /**
      * �ϴ�����������form
      * @param file
      * @param fileName
      * @param data
      * @return
      */
     public String handleForm(File file,String fileName,String data[])
     {
    	DBManager db=new DBManager();
      	Connection conn=null;
      	PreparedStatement pstmt=null;
      	InputStream is=null;
      	try {
      		conn=db.getConn();
      		conn.setAutoCommit(false);
      		pstmt=conn.prepareStatement("insert into bpm_form(id,name,content,createtime) values(?,?,?,?)");
      		is=new FileInputStream(file);
      		pstmt.setString(1, fileName);
          	pstmt.setString(2, fileName);
          	pstmt.setBinaryStream(3, is,is.available());
          	pstmt.setTimestamp(4, new Timestamp(new Date().getTime())); 
          	pstmt.executeUpdate();
      		conn.commit();
      		conn.setAutoCommit(true);
  		} catch (Exception e) {
  			logger.error("�ϴ���ʧ��", e);
  			e.printStackTrace();
  			return "error";
  		}
      	finally
      	{
 				try {
 					if(is!=null) is.close();
 					if(pstmt!=null)	pstmt.close();
 					if (conn != null && !conn.isClosed()) conn.close();
 				} catch (Exception e) {
 					
 					e.printStackTrace();
 				}
 				db.close();
      	}
      	
      	return "success";
     }
     
     /**
      * ���ϴ���zipѹ���ļ�
      * @param filePath
      * @param fileName
      * @return
      */
     public String handleZip(File file,String fileName)
     {
    	DBManager db=new DBManager();
      	Connection conn=null;
      	PreparedStatement pstmt=null;
      	InputStream is=null;
      	ZipFile zipFile=null;
      	//File file=null;
      	try {
      		conn=db.getConn();
      		pstmt=conn.prepareStatement("insert into bpm_form(id,name,content,createtime) values(?,?,?,?)");
      		conn.setAutoCommit(false);
      		pstmt.clearBatch();
      		
      		zipFile= new ZipFile(file.getPath());
 			File file2=null;
 			for (Enumeration entries = zipFile.getEntries(); entries.hasMoreElements();) 
			{
				ZipEntry entry = (ZipEntry) entries.nextElement();
				
				file2 = new File(entry.getName());
				if (entry.isDirectory()) 
				{
					file2.mkdirs();
				} 
				else 
				{
					/*File parent = file2.getParentFile();
					if (parent != null && !parent.exists())
					{
						parent.mkdirs();
					}*/
					is = zipFile.getInputStream(entry);
					int length=is.available();
					pstmt.setString(1, entry.getName());
     				pstmt.setString(2, entry.getName());
     				pstmt.setBinaryStream(3, is,is.available());
     				pstmt.setTimestamp(4, new Timestamp(new Date().getTime())); 
     				pstmt.addBatch();
				}
			}
      		
      		/*zipFile= new ZipFile(filePath);
      		for (Enumeration entries = zipFile.getEntries(); entries.hasMoreElements();) 
			{
				ZipEntry entry = (ZipEntry) entries.nextElement();
				file = new File(entry.getName());
				if (entry.isDirectory()) 
				{
					file.mkdirs();
				} 
				else 
				{
					File parent = file.getParentFile();
					if (parent != null && !parent.exists())
					{
						parent.mkdirs();
					}
					is = zipFile.getInputStream(entry);
					int length=is.available();
					pstmt.setString(1, entry.getName());
     				pstmt.setString(2, entry.getName());
     				pstmt.setBinaryStream(3, is,length);
     				pstmt.setTimestamp(4, new Timestamp(new Date().getTime())); 
     				pstmt.addBatch();
				//	is.close();
				}
			}*/
      		
 			pstmt.executeBatch();
      		conn.commit();
      		conn.setAutoCommit(true);
  		} catch (Exception e) {
  			logger.error("�ϴ���ʧ��", e);
  			e.printStackTrace();
  			return "error";
  		}
      	finally
      	{
 				try {
 					if(is!=null) is.close();
 					if(zipFile!=null) zipFile.close();
 					if(pstmt!=null)	pstmt.close();
 					if (conn != null && !conn.isClosed()) conn.close();
 				} catch (Exception e) {
 					
 					e.printStackTrace();
 				}
 				db.close();
      	}
      	
      	return "success";
     }
     
     
     /**
      * form�б�
      * @return
      */
     public JspPage findFormList(int curpage, int perpage) {
    	 List list = new ArrayList();
		 JspPage jspPage=null;
	     String sql=String.format("select id,name from bpm_form");
	     String countSql="select count(*) from bpm_form";
	     DBManager db=new DBManager();
	     ResultSet rs=null;
	     try {
	    	int rowcount=db.executeQueryCount(countSql);
		    jspPage = new JspPage(perpage, curpage, rowcount);
			rs = db.executeQueryFromAll(sql, jspPage.getMinNum(), perpage);
			ResultSetHandler<List<FormModel>> rsh = new BeanListHandler<FormModel>(FormModel.class);
			list = rsh.handle(rs);
			jspPage.setList(list);
		} catch (Exception e) {
			logger.error("SystemDao.findFormList()-------ִ�г���", e);
		} finally {
			if(null!=rs) {
				try {
					rs.close();
				} catch (SQLException e) {
					
				}
			}
			db.close();
		}
	     return jspPage;
	}

     /**
      * ɾ����
      * @param checkbox
      */
     public void deleteForm(String checkbox[]) {
    	 Session session = sessionFactory.getCurrentSession();
    	 Query query = session.createSQLQuery("delete from bpm_form  " +
    	 		                              "where id in(:tempid)");
    	 query.setParameterList("tempid", checkbox);
    	 query.executeUpdate();
     }
     
     /**
      * ��ѯ�����û�
      * @return
      */
     public List<UserModel> queryAllUser() {
    	DBManager dbm = new DBManager();
 		List<UserModel> list = new ArrayList<UserModel>();
 		ResultSet rs = null;
 		String sql="select id_ as id from act_id_user";
 		try {
 			rs = dbm.executeQuery(sql);
 			ResultSetHandler<List<UserModel>> rsh = new BeanListHandler<UserModel>(UserModel.class);
 			list = rsh.handle(rs);
 		} catch (SQLException e) {
 			SysLogger.error("queryAllUser--ִ��ʧ��", e);
 		} finally {
 			dbm.close();
 			if(null!=rs) {
 				try {
 					rs.close();
 				} catch (SQLException e) {
 				}
 			}
 			
 		}
	    return list;
     }
     
     public String isStart(String processDefinitionId,String userId)
     {
    	 String result="success";
    	 DBManager dbm = new DBManager();
    	 ResultSet rs = null;
    	 List<String> instList=new ArrayList<String>();
    	 StringBuffer sb=new StringBuffer();
    	 String sql=String.format("select count(*),PROC_INST_ID_ from " +
    	 		"(select PROC_INST_ID_,ASSIGNEE_ from act_hi_actinst where PROC_DEF_ID_='%s') " +
    	 		"as t1 group by PROC_INST_ID_", processDefinitionId);
    	 rs=dbm.executeQuery(sql);
    	 int count=0;
    	 String procId=null;
    	 try {
			while(rs.next())
			 {
				 count=rs.getInt(1);
				 procId=rs.getString(2);
				 if(count==2) instList.add(procId);
			 }
			if(instList.size()==0) return result;
			int size=instList.size();
			for(int i=0;i<size;i++)
			{
				sb.append("'");
				sb.append(instList.get(i));
				sb.append("'");
				if(i!=size-1) sb.append(",");
			}
			sql=String.format("select START_USER_ID_ from act_hi_procinst where START_USER_ID_='%s' and  PROC_INST_ID_ in (%s) ",userId,sb.toString());
			rs=dbm.executeQuery(sql);
			if(rs.next())
			{
				result="error";
			}
    	 } catch (SQLException e) {
			e.printStackTrace();
		}
    	 finally
    	 {
    		 if(rs!=null)
 				try {
 					rs.close();
 				} catch (SQLException e1) {
 					e1.printStackTrace();
 				}
     		 dbm.close();
    	 }
    	 return result;
     }
     
     public String deployForm(File file,String fileName,File filepic,String filepicName )
     {
    	DBManager db=new DBManager();
       	Connection conn=null;
       	PreparedStatement pstmt=null;
       	InputStream is=null;
       //	FileInputStream is2=null;
    	//InputStream is3 = null;
       	try {
       		conn=db.getConn();
       		conn.setAutoCommit(false);
       		pstmt=conn.prepareStatement("insert into bpm_form(id,name,content,createtime) values(?,?,?,?)");
       		is=new FileInputStream(file);
       		
       		pstmt.setString(1, fileName);
           	pstmt.setString(2, fileName);
        	pstmt.setBinaryStream(3, is,is.available());
           	pstmt.setTimestamp(4, new Timestamp(new Date().getTime())); 
          // is2 =	new BufferedInputStream(new FileInputStream(filepic));  
           //	is2 = new FileInputStream(filepic);
        	//pstmt.setBinaryStream(5, is2,(int) filepic.length());
        	
           	pstmt.executeUpdate();
       		conn.commit();
       		conn.setAutoCommit(true);
       		
       		//ͼƬ�޷�����mysql�У���ʱ�����ڱ���
       		String path=ResourceCenter.getInstance().getSysPath()+"images\\bpm\\"+filepicName;
       	    InputStream is2 = new BufferedInputStream(new FileInputStream(filepic));
       	    OutputStream os = new BufferedOutputStream(new FileOutputStream(path));
  	        byte b[] = new byte[1024];
  		    while (is2.read(b) > 0) 
  		    {
  		    	os.write(b);
  			 }
  		  if(is2!=null) is2.close();
  		  if(os!=null) os.close();
       	
       	} catch (Exception e) {
   			logger.error("�ϴ�����ͼƬʧ��", e);
   			e.printStackTrace();
   			return "error";
   		}
       	finally
       	{
  				try {
  					if(is!=null) is.close();
  					if(pstmt!=null)	pstmt.close();
  					if (conn != null && !conn.isClosed()) conn.close();
  				} catch (Exception e) {
  					
  					e.printStackTrace();
  				}
  				db.close();
       	}
    	 return "success";
     }
     
     /**
      * ��ȡ���������ֵ�
      * @param typeId
      * @return
      */
     public List<Codedetail> loadCodedetail(String typeId) {
    	 StringBuilder sb = new StringBuilder();
    	 List<Codedetail> list = new ArrayList<Codedetail>();
   	     sb.append("select ID,NAME,CODE from nms_codedetail where 1=1")
   	       .append(" and typeid='"+typeId+"' order by seq asc ");
   	     DBManager dbm = new DBManager();
 	     ResultSet rs = null;
 	     rs = dbm.executeQuery(sb.toString());
 	     Codedetail vo = null;
 	     try {
			while(rs.next()) {
				 vo = new Codedetail();
				 vo.setName(rs.getString("NAME"));
				 vo.setCode(rs.getString("CODE")+"|"+rs.getString("ID"));
				 list.add(vo);
			 }
		} catch (SQLException e) {
			SysLogger.error("SystemDao.loadCodedetail--ִ��ʧ��", e);
		} finally {
			dbm.close();
			if(null!=rs) {
				try {
					rs.close();
				} catch (SQLException e) {
				}
			}
		}
 	     return list;
     }
    
     /**
      * �޸�ģ������
      * @param keytext
      * @param modelid
      * @return
      */
     public String modifyModelType(String keytext,String modelid) {
    	 DBManager dbm = new DBManager();
    	 String flag = "success";
    	 String sql = "select modelid from bpm_modeltype where modelid='"+modelid+"'"; 
    	 String sql1 = "";
    	 ResultSet rs = null;
    	 try {
			rs = dbm.executeQuery(sql);
			 if(rs.next()) {
				sql1 = String.format("update bpm_modeltype set TYPEID='%s' where MODELID='%s'", keytext,modelid);
			 }else {
				sql1 = String.format("insert into bpm_modeltype(ID,MODELID,TYPEID) values ('%s','%s','%s')", UUIDKey.getKey(),modelid,keytext);
			 }
			 dbm.executeUpdate(sql1);
		} catch (SQLException e) {
			SysLogger.error("SystemDao.modifyModelType--ִ��ʧ��", e);
			flag = "error";
		} finally {
			if(null!=rs) {
				try {
					rs.close();
					dbm.close();
				} catch (SQLException e) {
				}
			}
		}
    	 return flag;
     }
}
