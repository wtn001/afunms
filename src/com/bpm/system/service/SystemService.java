package com.bpm.system.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.impl.persistence.entity.GroupEntity;
import org.activiti.engine.impl.persistence.entity.UserEntity;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.xwork.ArrayUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bpm.process.dao.ProcessDao;
import com.afunms.common.base.JspPage;
import com.afunms.common.util.DBManager;
import com.afunms.system.model.Codedetail;
import com.bpm.system.dao.SystemDao;
import com.bpm.system.model.FormModel;
import com.bpm.system.model.Menu;
import com.bpm.system.model.UserModel;
import com.bpm.system.utils.ConstanceUtil;
import com.bpm.system.utils.StringUtil;

@Service
@Transactional
public class SystemService {

	@Resource
	private IdentityService identityService;
	@Resource
	private RepositoryService repositoryService;
	@Resource
	private RuntimeService runtimeService;
	private static final Logger logger = Logger.getLogger(SystemService.class);
	@Resource
	private SystemDao systemDao;
	
	@Resource
	private ProcessDao processDao;
	/**
	 * 
	 * Description: ��֤�û���½��������Session���б����½��Ϣ
	 * Date:2012-10-17
	 * @author hexinlin
	 * @return String
	 */
	public String UserLogin(String username,String password,Map<String,Object> session) {
		session.clear();
		if(StringUtil.exitBlank(username,password)) {
			return "error";
		}
		boolean checkPassword = identityService.checkPassword(username,password);
		if(checkPassword){
			User user = identityService.createUserQuery().userId(username).singleResult();
			session.put(ConstanceUtil.USER, user);
			
			List<Group> groupList = identityService.createGroupQuery().groupMember(username).list();
			session.put("groups", groupList);

			String[] groupNames = new String[groupList.size()];
			for (int i = 0; i < groupNames.length; i++) {
				groupNames[i] = groupList.get(i).getName();
			}
			session.put("groupNames", ArrayUtils.toString(groupNames));
			
			session.put(ConstanceUtil.AUTHKEY, username);
			return "success";
		} else {
			return "error";
		}
	} 
	
	/**
	 * ��������
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	public String deployProcess(File file,String fileContentType,String fileName,String url)
	{
		
		if(file==null || StringUtil.isBlank(fileName)) return "error";
		String result="success";
		try {
			InputStream fileInputStream = new FileInputStream(file);
			String extension = FilenameUtils.getExtension(fileName);
			if (extension.equals("zip") || extension.equals("bar")) 
			{
				ZipInputStream zip = new ZipInputStream(fileInputStream);
				repositoryService.createDeployment()
				.addZipInputStream(zip)
				.deploy();
			} 
			else if (extension.equals("png"))
			{
				repositoryService.createDeployment()
				.addInputStream(fileName, fileInputStream)
				.deploy();
			} 
			else if (fileName.indexOf("bpmn20.xml") != -1) 
			{
				//InputStream is=new FileInputStream(url+"start.form");
				repositoryService.createDeployment()
				.addInputStream(fileName, fileInputStream)
				//.addInputStream("start.form", is)
				.deploy();
			}  
			else if (extension.equals("bpmn")) 
			{
				String baseName = FilenameUtils.getBaseName(fileName);
				repositoryService.createDeployment()
				.addInputStream(baseName + ".bpmn20.xml", fileInputStream)
				.deploy();
			} 
			else 
			{
				result="error";
			}
		} catch (Exception e) {
			result="error";
			logger.error(e);
			e.printStackTrace();
			
		}
		return result;
	}
	/**
	 * 
	 * Description:��ȡ���в˵���Ϣ
	 * Date:2012-10-18
	 * @author hexinlin
	 * @return List<Menu>
	 */
	public List<Menu> queryAllMenu() {
		
		return systemDao.queryAllMenu();
	}
	/**
	 * ���̶����б�
	 * @return
	 */
	public JspPage findPdList(int curpage, int perpage)
	{
		JspPage jspPage=null;
    	int rowcount =repositoryService.createProcessDefinitionQuery().list().size();
    	jspPage = new JspPage(perpage, curpage, rowcount);
		List list = repositoryService.createProcessDefinitionQuery().listPage((curpage-1)*perpage, perpage);
		jspPage.setList(list);
		return jspPage;
	}
	
	/**
	 * ���������̵�XML�ļ���ͼƬ
	 * @param deploymentId
	 * @param resourceName
	 * @return
	 */
	public void findSourcebyPdId(String deploymentId,String resourceName,HttpServletResponse response )
	{
		/*try {
			System.out.println(new String(resourceName.getBytes("ISO-8859-1"),"UTF-8")+"-----------");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}*/
		InputStream is = null;
		ServletOutputStream os=null;
		try {
			List<String> names=repositoryService.getDeploymentResourceNames(deploymentId);
			String resName=null;
			for(String name:names)
			{
				if((name.endsWith("xml") && resourceName.endsWith("xml")) || (name.endsWith("png") && resourceName.endsWith("png")))
				{
					resName=name;
					break;
				}
			}
			is = repositoryService.getResourceAsStream(deploymentId, resName);
			os = response.getOutputStream();
			byte[] bt = new byte[1024];
			int len;
			while ((len = is.read(bt)) != -1) 
			{
				os.write(bt, 0, len);
			}
			
		} catch (IOException e) {
			logger.error("SystemService.findSourcebyPdId---ִ�д���", e);
		}
		finally
		{
				try {
					if(is!=null) is.close();
					if(os!=null)os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}
	
	/**
	 * ɾ������������
	 * @param deploymentId
	 */
	@SuppressWarnings("deprecation")
	public void deletePdById(String deploymentId)
	{
		repositoryService.deleteDeploymentCascade(deploymentId);
		//repositoryService.deleteDeployment(deploymentId);
	}
	
	/**
	 * ��������
	 * @param deploymentId
	 * @param userId
	 */
	public String startPdById(String processDefinitionId,String userId)
	{
		/*String result=systemDao.isStart(processDefinitionId, userId);
		if(result.equals("error")) return result;*/
		/*String authenticatedUserId = Authentication.getAuthenticatedUserId();
		System.out.println(authenticatedUserId);*/
		String result="success";
		//�����������̵��û�
		identityService.setAuthenticatedUserId(userId);
		Map map = new HashMap();
		map.put("owner",userId);
		//map.put("isbanjie","0" );
		map.put("isbanjiebutton", ConstanceUtil.NO_Process_BANJIE);//0�ǲ���Ҫ��ᰴť
		//runtimeService.startProcessInstanceById(deploymentId, map);
		
		ProcessInstance instance = runtimeService.startProcessInstanceById(processDefinitionId,map);
    	if(instance.isEnded()) {
    		processDao.changeProcessInstanceStatus(ConstanceUtil.PRO_END, instance.getProcessInstanceId());
    	}else {
    		processDao.changeProcessInstanceStatus(ConstanceUtil.PRO_START, instance.getProcessInstanceId());
    	}
    	return result;
	}
	
	/**
	 * 
	 * Description:��ȡһ���˵�
	 * Date:2012-10-21
	 * @author hexinlin
	 * @return List<Menu>
	 */
	public List<Menu> queryOneLevelMenu() {
		return systemDao.queryOneLevelMenu();
	}
	
	/**
     * 
     * Description:ɾ��һ���˵������ж����˵��Ĳ���ɾ����
     * Date:2012-10-21
     * @author hexinlin
     * @return String
     */
    public String deleteOneLevelMenuByIds(String checkbox[]) {
    	try {
			systemDao.deleteOneLevelMenuByIds(checkbox);
		} catch (Exception e) {
			logger.error("SystemDao.deleteOneLevelMenuByIds(String checkbox[])--ִ�г���", e);
			return "error";
		}
    	return "success";
    }
	
    /**
     * 
     * Description:�޸�һ���˵���Ϣ��
     * Date:2012-10-21
     * @author hexinlin
     * @return String
     */
    public String modifyOneLevelMenu(String menu_name,String menu_url,int menu_id) {
    	try {
			systemDao.modifyMenu(menu_name,menu_url, menu_id);
		} catch (Exception e) {
			logger.error("SystemDao.modifyOneLevelMenu(String menu_name,int menu_seq,String menu_url,int menu_id)--ִ�г���", e);
			return "error";
		}
    	return "success";
    }
    
    
    /**
     * 
     * Description:����һ���˵�
     * Date:2012-10-21
     * @author hexinlin
     * @return void
     */
    public String addOneLevelMenu(Menu menu) {
    	try {
    		if(StringUtil.isBlank(menu.getMenu_url())){
    			menu.setMenu_url("javascript:void(null)");
    		}
    		int sort = systemDao.getMaxSort();
    		menu.setMenu_id(sort+1);
    		menu.setSort(sort+1);
    		menu.setMenu_seq(sort+1);
    		Menu parent = new Menu();
    		parent.setMenu_id(-1);
    		menu.setParent(parent);
			systemDao.addOneLevelMenu(menu);
		} catch (Exception e) {
			logger.error("SystemDao.addOneLevelMenu(Menu menu)--ִ�г���", e);
			return "error";
		}
    	return "success";
    }
    /**
     * 
     * Description:��ȡ�û�����Ϣ
     * Date:2012-10-21
     * @author hexinlin
     * @return List<Group>
     */
    public List<GroupEntity> queryAllGroup() {
    	List list = new ArrayList();
    	list=identityService.createGroupQuery().list();
        return list;
    }
    /**
     * ��ȡ���������ֵ�
     * @param typeId
     * @return
     */
    public List<Codedetail> loadCodedetail(String typeId) {
    	return new SystemDao().loadCodedetail(typeId);
    }
    /**
     * 
     * Description:��ȡ�û���Ϣ
     * Date:2012-10-24
     * @author hexinlin
     * @return List<UserEntity>
     */
    public List<UserEntity> queryUser() {
    	List list = new ArrayList();
    	list = identityService.createUserQuery().list();	
    	
    	return list;
    }
    
    
    /**
     * 
     * Description:���ӻ��߸����û���
     * Date:2012-10-21
     * @author hexinlin
     * @return String
     */
    public String addOrUpdateGroup(Group group) {
    	try {
    		Group updateGroup = identityService.createGroupQuery().groupId(group.getId()).singleResult();
    		if(null==updateGroup) {
    			identityService.saveGroup(group);
    		}else {
    			updateGroup.setName(group.getName().trim());
    			identityService.saveGroup(updateGroup);
    		}
    		
		} catch (Exception e) {
			logger.error("SystemDao.addOrUpdateGroup(Group group)--ִ�г���", e);
			return "error";
		}
    	return "success";
    }
    
    /**
     * 
     * Description:ɾ��ָ���û���
     * Date:2012-10-21
     * @author hexinlin
     * @return String
     */
    public String deleteGroups(String checkbox[]) {
    	try {
			systemDao.delGroup(checkbox);
		} catch (Exception e) {
			logger.error("SystemDao.deleteGroups(String checkbox[])  --ִ�г���", e);
			return "error";
		}
    	return "success";
    }
	
    /**
     * 
     * Description:�������߸����û������ҽ������û���֮��Ĺ�����
     * Date:2012-10-21
     * @author hexinlin
     * @return String
     */
    public String saveOrUpdateUser(User user,String []addgroups,String[]modifygroups,String flag){
    	try {
    		
    		if("add".equals(flag)){//�����û�
    			identityService.saveUser(user);
    			for(String groupId:addgroups) {
    				identityService.createMembership(user.getId(), groupId);
    			}
    			
    		}else {//�����û�
    			User updateUser = identityService.createUserQuery().userId(user.getId()).singleResult();
    			updateUser.setFirstName(user.getFirstName());
    			updateUser.setLastName(user.getLastName());
    			updateUser.setEmail(user.getEmail());
                if(!StringUtil.isBlank(user.getPassword())) {
                   updateUser.setPassword(user.getPassword());
                }
    			identityService.saveUser(updateUser);
    			List<Group> list = identityService.createGroupQuery().groupMember(user.getId()).list();
                for(Group g :list) {
                	identityService.deleteMembership(user.getId(), g.getId());
                }
                for(String groupId:modifygroups) {
                	identityService.createMembership(user.getId(), groupId);
                }
    			
    		}
			
		} catch (Exception e) {
			logger.error("SystemService.saveUser(User user,String groupId) --ִ�г���", e);
			return "error";
		}
    	return "success";
    }
  
    /**
     * 
     * Description:ɾ���û���Ϣ������ɾ���û�����֮��Ĺ���
     * Date:2012-10-21
     * @author hexinlin
     * @return String
     */
    public String deleteUsers(String checkbox[]) {
    	// ɾ���û�ʱ �漰�����д����˵���ش���
    	return "success";
    }
    /**
     * 
     * Description:�������ȡ�û���Ϣ
     * Date:2012-10-21
     * @author hexinlin
     * @return List<User>
     */
    public List<User> queryUsersByGroup(String groupId) {
    	List<User> list = new ArrayList<User>();
    	list = identityService.createUserQuery().memberOfGroup(groupId).list();
    	return list;
    }
    
    
    /**
     * 
     * Description:�洢�û����ڲ˵��Ķ�Ӧ��ϵ
     * Date:2012-10-21
     * @author hexinlin
     * @return void
     */
    public String saveGroupMenu(String groupId,int checkbox[]) {
    	try {
			systemDao.saveGroupMenu(groupId, checkbox);
		} catch (Exception e) {
			logger.error("SystemDao.saveGroupMenu(String groupId,int checkbox[]) --ִ�г���", e);
			return "error";
		}
    	return "success";
    }
    
    /**
     * 
     * Description:���ݸ��˵���ѯ�����˵�
     * Date:2012-10-23
     * @author hexinlin
     * @return List
     */
    public List querysecmenu(int parent_id) {
    	List list = new ArrayList<Menu>();
    	if(0==parent_id) {
    		return list;
    	}
    	list = systemDao.querysecmenu(parent_id);
    	return list;
    }
    
    /**
     * 
     * Description:�༭�˵�
     * Date:2012-10-23
     * @author hexinlin
     * @return String
     */
    public String editSecMenu(int edit_menu_id,String edit_menu_name,String edit_menu_url,int edit_parent_id,int del_menu_id) {
    	Menu menu = new Menu();
    	Menu parent = new Menu();
    	parent.setMenu_id(edit_parent_id);
    	int menu_id = 0;
    	try {
    		if(del_menu_id!=0) {
    			systemDao.delSecMenu(del_menu_id,edit_parent_id);
    		}
    	   else if(edit_menu_id==0) { //����������µĲ˵�
				menu_id = systemDao.maxMenu_id(edit_parent_id);
				menu_id = menu_id>10?menu_id+1:menu_id+11;
				menu.setMenu_id(menu_id);
				menu.setMenu_name(edit_menu_name);
				menu.setMenu_url(StringUtil.isBlank(edit_menu_url)?"javascript:void(null)":edit_menu_url);
				menu.setSort(edit_parent_id);
				menu.setMenu_seq(new Integer(String.valueOf(menu_id).substring(1)));
			    menu.setParent(parent);
			    menu.setChildrennum(0);
			    systemDao.addNewSecMenu(menu);
			}
			else{//�����в˵����б༭
				systemDao.modifyMenu(edit_menu_name,edit_menu_url, edit_menu_id);
			}
		}catch (Exception e) {
			logger.error("SystemDao.editSecMenu --ִ�г���", e);
			return "error";
		}
    	return "success";
    }
    
    /**
     * 
     * Description:�����û�ID��ȡ��Ϣ
     * Date:2012-10-24
     * @author hexinlin
     * @return UserEntity
     */
    public UserEntity getUserById(String userId) {
    	UserEntity user = null;
    	user = (UserEntity)identityService.createUserQuery().userId(userId).singleResult();
    	return user;
    }
    /**
     * 
     * Description:����UserId��ȡGroup��Ϣ
     * Date:2012-10-24
     * @author hexinlin
     * @return Group
     */
    public List<GroupEntity> getGroupsByUser(String userId) {
    	List list = new ArrayList();
        list = identityService.createGroupQuery().groupMember(userId).list();
    	return list;
    }
    
    /**
     * �ϴ���
     * @param file
     * @param fileName
     * @return
     */

    public String deployForm(File file,String fileName)
    {
    	if(StringUtil.isBlank(fileName)) return "error";
    	return systemDao.deployForm(file, fileName);
    }
    
    /** 
     * �����Զ�������
     * @param checkbox
     */
    public void designProcessDeploy(String[] checkbox,String url,String url2)
    {
    	if(checkbox==null) return ;
    	systemDao.designProcessDeploy(checkbox,url,url2);
    }
    
    /**
     * form�б�
     * @return
     */
    public JspPage findFormList(int curpage, int perpage) {
		 return systemDao.findFormList(curpage,perpage);
	}
    
    /**
     * 
     * Description:����groupId���Ҷ�Ӧ��group
     * Date:2012-11-14
     * @author hexinlin
     * @return Group
     */
    public Group getGroupById(String groupId) {
    	
    	return identityService.createGroupQuery().groupId(groupId).singleResult();
    }
    
    public String deleteForm(String checkbox[]) {
    	try {
			systemDao.deleteForm(checkbox);
		} catch (Exception e) {
			logger.error("SystemDao.deleteForm(String checkbox[])  --ִ�г���", e);
			return "error";
		}
    	return "success";
    }
   
    public List<UserModel> queryAllUser()
    {
    	return systemDao.queryAllUser();
    }
    
    /**
     * �ϴ����Ͷ�ӦͼƬ
     * @param file,form�ļ�
     * @param fileName��form�ļ���
     * @param filepic��ͼƬ
     * @param filepicName��ͼƬ��
     * @return
     */

    public String deployForm(File file,String fileName,File filepic,String filepicName )
    {
    	if(StringUtil.isBlank(fileName)) return "error";
    	return systemDao.deployForm(file, fileName, filepic, filepicName);
    }
    /**
     * �޸�ģ�͵�����
     * @param modelid
     * @param codetype
     * @param codedetail
     * @return
     */
    public String modifyCodeType(String keytext,String modelid) {
    	return systemDao.modifyModelType(keytext, modelid);
    }
}
