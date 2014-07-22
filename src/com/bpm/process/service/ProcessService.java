package com.bpm.process.service;

import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.activiti.engine.impl.bpmn.diagram.ProcessDiagramGenerator;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.ProcessDefinitionImpl;
import org.activiti.engine.impl.pvm.process.TransitionImpl;
import org.activiti.engine.impl.task.TaskDefinition;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.afunms.common.base.JspPage;
import com.afunms.common.util.DBManager;
import com.afunms.common.util.SysLogger;
import com.afunms.config.model.Knowledgebase;
import com.bpm.process.dao.ProcessDao;
import com.bpm.process.model.HistoryProcessInstanceModel;
import com.bpm.process.model.ProcessStatisticalsModel;
import com.bpm.system.utils.ConstanceUtil;
import com.bpm.system.utils.ProcessEnum;
import com.bpm.system.utils.StringUtil;

@Service
@Transactional
public class ProcessService {

	@Resource
	private RepositoryService repositoryService;
	@Resource
	private RuntimeService runtimeService;
	@Resource
	private TaskService taskService;
	@Resource
	private FormService formService;
	@Resource
	private HistoryService historyService;
	@Resource
	private ProcessDao processDao;
	@Resource
	private IdentityService identityService;
	
	private static final Logger logger = Logger.getLogger(ProcessService.class);
	
	
	/**
	 * ����ʵ���б���ʾ�Ѿ������������б�
	 * @return
	 */
	
	public List<ProcessInstance> findPiList()
	{
		List<ProcessInstance> piList = runtimeService.createProcessInstanceQuery().list();
		/*for(ProcessInstance pi:piList)
		{
			System.out.println(pi.getId());
		}*/
		Task task = null;
		return piList;
	}
	
	/**
	 * ��δǩ�յ������б��ȴ�ǩ��
	 * @param userId
	 * @return
	 */
	public JspPage findTaskList(String userId,int curpage, int perpage)
	{
		JspPage jspPage=null;
		int rowcount =taskService.createTaskQuery().taskCandidateUser(userId).list().size();
	    jspPage = new JspPage(perpage, curpage, rowcount);
		// �������ǰ��½�û�������
		List list = taskService.createTaskQuery().taskCandidateUser(userId).orderByTaskCreateTime().desc().listPage((curpage-1)*perpage, perpage);
		jspPage.setList(list);
		return jspPage;
	}
	
	public void claimTask(String taskId,String userId)
	{
		taskService.claim(taskId, userId);
	}
	
	
	/**
	 * ��ǩ�յ������б�
	 * @param userId
	 * @return
	 */
	public JspPage findTaskClaimList(String userId,int curpage, int perpage)
	{
		JspPage jspPage=null;
    	int rowcount =taskService.createTaskQuery().taskAssignee(userId).list().size();
    	jspPage = new JspPage(perpage, curpage, rowcount);
		// �������ǰ��½�û�������
		List<Task> list = taskService.createTaskQuery().taskAssignee(userId).orderByTaskCreateTime().desc().listPage((curpage-1)*perpage, perpage);
		jspPage.setList(list);
		return jspPage;
	}
	/**
	 * ��ȡ��ǰtask��ǰһtask��ִ����
	 * @param tasks
	 * @return
	 * @throws Exception
	 */
	public Map<String,String> findBackUserTaskAssignee(List<Task> tasks) throws Exception {
		ActivityImpl activityImpl = null;
		Map<String,String> taskUsers = new HashMap<String, String>();
		for(Task task : tasks) {
			activityImpl = findBackUserTaskAssignee(task.getId());
			if(null!=activityImpl) {
				taskUsers.put(task.getId(),historyService.createHistoricTaskInstanceQuery().taskDefinitionKey(activityImpl.getId()).processInstanceId(task.getProcessInstanceId()).singleResult().getAssignee());
			}
			/*backList = findBackAIList(task.getId());
			for(int i=backList.size()-1;i>=0;i--) {
				ai = backList.get(i);
				if(ai.getProperty("type").equals("userTask")) {
					taskUsers.put(task.getId(),historyService.createHistoricTaskInstanceQuery().taskDefinitionKey(ai.getId()).processInstanceId(task.getProcessInstanceId()).singleResult().getAssignee());
				   break;
				}else {
					continue ;
				}
			}*/
		}
		return taskUsers;
	}
	/**
	 * ͼƬ��
	 * @param taskId
	 * @return
	 * @throws Exception
	 */
	public void getImageStream(String processDefinitionId,String processInstanceId,HttpServletResponse response) throws Exception{
		InputStream imageStream = null;
		response.setContentType( "multipart/form-data" ); 
		ServletOutputStream os=null;
		ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
		
	  	ProcessDefinitionEntity pde =  (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)   
	              .getDeployedProcessDefinition(processDefinitionId); 
		if(null==pi) {
			//ֱ�ӵõ�����ͼƬ������ڵ�δ����
			imageStream=ProcessDiagramGenerator.generatePngDiagram(pde);
		}else {
			
			//����processDefinitionId��taskIdֱ�ӵõ�ͼƬ������ڵ����
		    	String id= runtimeService.createExecutionQuery().processInstanceId(processInstanceId).singleResult().getId();
				imageStream = ProcessDiagramGenerator.generateDiagram(pde, "png", runtimeService.getActiveActivityIds(id));
		}
		
		try {
			os = response.getOutputStream();
			byte[] bt = new byte[1024];
			int len;
			while ((len = imageStream.read(bt)) != -1) 
			{
				os.write(bt, 0, len);
			}
			
		} catch (IOException e) {
			logger.error("ProcessService.getImageStream---ִ�д���", e);
		}
		finally
		{
				try {
					if(imageStream!=null) imageStream.close();
					if(os!=null)os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
    }
	
	
	/** 
     * ��������ID��ȡ���̶��� 
     *  
     * @param taskId 
     *            ����ID 
     * @return 
     * @throws Exception 
     */  
    public ProcessDefinitionEntity findProcessDefinitionEntityByTaskId(String taskId) throws Exception {  
    	TaskEntity taskEntity=findTaskById(taskId);
    	String processDefinitionId=taskEntity.getProcessDefinitionId();
        // ȡ�����̶���  
        ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService).getDeployedProcessDefinition(processDefinitionId);  
        if (processDefinition == null) {  
            throw new Exception("���̶���δ�ҵ�!");  
        }  
        return processDefinition;  
    }  
  
    /** 
     * ��������ID��ȡ��Ӧ������ʵ�� 
     *  
     * @param taskId 
     *            ����ID 
     * @return 
     * @throws Exception 
     */  
    public ProcessInstance findProcessInstanceByTaskId(String taskId)  
            throws Exception {  
        // �ҵ�����ʵ��  
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
        								 .processInstanceId(findTaskById(taskId).getProcessInstanceId())  
                .singleResult();  
        if (processInstance == null) {   
            throw new Exception("����ʵ��δ�ҵ�!");  
        }  
        return processInstance;  
    }  
	/**
	 * ����taskId��ȡ��ʷ����ʵ��
	 * @param taskId
	 * @return
	 */
    public HistoricProcessInstance findHistoriceProcessInstanceByTaskId(String taskId) throws Exception {
    	HistoricProcessInstance instance = historyService.createHistoricProcessInstanceQuery().processInstanceId(historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult().getProcessInstanceId() ).singleResult();
    	return instance;
    }
    
    /** 
     * ����taskId�������ʵ�� 
     *  
     * @param taskId 
     *            ����ID 
     * @return 
     * @throws Exception 
     */  
	public TaskEntity findTaskById(String taskId) throws Exception {  
        TaskEntity task = (TaskEntity) taskService.createTaskQuery().taskId(taskId).singleResult();  
        if (task == null) {  
            throw new Exception("����ʵ��δ�ҵ�!");  
        }  
        return task;  
    }  
    
    /**
     * ����taskId��ñ�
     * @param taskId
     * @return
     */
    public List<FormProperty> findFpList(String taskId)
    {
    	TaskFormData tfd = formService.getTaskFormData(taskId);
    	return  tfd.getFormProperties();
    }
    
    
    /**
     * ����taskExtId����ѯ�ύ������
     * @param taskExtId
     * @return
     */
    public Map findFormMap(String taskExtId)
    {
    	return runtimeService.getVariables(taskExtId);
    }
    
    /** 
     * ���ݵ�ǰ����ID����ѯ���Բ��ص�����ڵ� 
     *  
     * @param taskId 
     *            ��ǰ����ID 
     */  
    public List<ActivityImpl> findBackAIList(String taskId,String processDefinitionId,String executionId) 
    		throws Exception { 
    	
    	//ActivityImpl activityImpl=findActivitiImpl(taskId, null);
    	ActivityImpl activityImpl=findActivityImplByExecutionId(processDefinitionId, executionId);
        List<ActivityImpl> rtnList =  iteratorBackActivity(taskId,activityImpl , new ArrayList<ActivityImpl>(),  
                    new ArrayList<ActivityImpl>());  
        return reverList(rtnList);  
    }
    
    /** 
     * ���ݵ�ǰ����ID����ѯ���Բ��ص�����ڵ� 
     *  
     * @param taskId 
     *            ��ǰ����ID 
     */  
    public List<ActivityImpl> findBackAIList(String taskId) 
    		throws Exception { 
    	
    	ActivityImpl activityImpl=findActivitiImpl(taskId, null);
        List<ActivityImpl> rtnList =  iteratorBackActivity(taskId,activityImpl , new ArrayList<ActivityImpl>(),  
                    new ArrayList<ActivityImpl>());  
        return reverList(rtnList);  
    }
    
    /**
     * ���ݵ�ǰ����ID��������һ����ڵ�
     * @param taskId
     * @return
     */
    public ActivityImpl findBackUserTaskAssignee(String taskId) throws Exception {
    	ActivityImpl activityImpl = findActivitiImpl(taskId, null);
    	return BackActivity(taskId,activityImpl , new ArrayList<ActivityImpl>(),  
                new ArrayList<ActivityImpl>());
    }
    /** 
     * ��������ID�ͽڵ�ID��ȡ��ڵ� <br> 
     *  
     * @param taskId 
     *            ����ID 
     * @param activityId 
     *            ��ڵ�ID <br> 
     *            ���Ϊnull��""����Ĭ�ϲ�ѯ��ǰ��ڵ� <br> 
     *            ���Ϊ"end"�����ѯ�����ڵ� <br> 
     *  
     * @return 
     * @throws Exception 
     */  
    public ActivityImpl findActivitiImpl(String taskId, String activityId)  
            throws Exception {  
        // ȡ�����̶���  
        ProcessDefinitionEntity processDefinition = findProcessDefinitionEntityByTaskId(taskId);  
  
        // ��ȡ��ǰ��ڵ�ID  
        if (StringUtil.isBlank(activityId)) {  
            activityId = findTaskById(taskId).getTaskDefinitionKey();  
        }  
  
        // �������̶��壬��ȡ������ʵ���Ľ����ڵ�  
        if (activityId.toUpperCase().equals("END")) {  
            for (ActivityImpl activityImpl : processDefinition.getActivities()) {  
                List<PvmTransition> pvmTransitionList = activityImpl  
                        .getOutgoingTransitions();  
                if (pvmTransitionList.isEmpty()) {  
                    return activityImpl;  
                }  
            }  
        }  
  
        // ���ݽڵ�ID����ȡ��Ӧ�Ļ�ڵ�  
        ActivityImpl activityImpl = ((ProcessDefinitionImpl) processDefinition)  
                .findActivity(activityId);  
  
        return activityImpl;  
    }
    
    /** 
     * ����ѭ���������ṹ����ѯ��ǰ�ڵ�ɲ��ص�����ڵ� 
     *  
     * @param taskId 
     *            ��ǰ����ID 
     * @param currActivity 
     *            ��ǰ��ڵ� 
     * @param rtnList 
     *            �洢���˽ڵ㼯�� 
     * @param tempList 
     *            ��ʱ�洢�ڵ㼯�ϣ��洢һ�ε��������е�ͬ��userTask�ڵ㣩 
     * @return ���˽ڵ㼯�� 
     */  
    public List<ActivityImpl> iteratorBackActivity(String taskId,  
    		ActivityImpl currActivity, 
    		List<ActivityImpl> rtnList,  
            List<ActivityImpl> tempList) throws Exception {  
        // ��ѯ���̶��壬�����������ṹ  
        ProcessInstance processInstance = findProcessInstanceByTaskId(taskId);  
  
        // ��ǰ�ڵ��������Դ  
        List<PvmTransition> incomingTransitions = currActivity.getIncomingTransitions();  
        
        // ������֧�ڵ㼯�ϣ�userTask�ڵ������ϣ����������˼��ϣ���ѯ������֧��Ӧ��userTask�ڵ�  
        List<ActivityImpl> exclusiveGateways = new ArrayList<ActivityImpl>(); 
        
        // ���нڵ㼯�ϣ�userTask�ڵ������ϣ����������˼��ϣ���ѯ���нڵ��Ӧ��userTask�ڵ�  
        List<ActivityImpl> parallelGateways = new ArrayList<ActivityImpl>();  
        
        // ������ǰ�ڵ���������·��  
        for (PvmTransition pvmTransition : incomingTransitions) 
        {  
            TransitionImpl transitionImpl = (TransitionImpl) pvmTransition;  
            ActivityImpl activityImpl = transitionImpl.getSource();  
            String type = (String) activityImpl.getProperty("type");  
            /** 
             * ���нڵ�����Ҫ��<br> 
             * ����ɶԳ��֣���Ҫ��ֱ����ýڵ�IDΪ:XXX_start(��ʼ)��XXX_end(����) 
             */  
            if (ConstanceUtil.PG.equals(type)) 
            {// ����·��  
                String gatewayId = activityImpl.getId();  
                String gatewayType = gatewayId.substring(gatewayId  
                        .lastIndexOf("_") + 1);  
                if ("START".equals(gatewayType.toUpperCase())) 
                {// ������㣬ֹͣ�ݹ�  
                    return rtnList;  
                } 
                else 
                {// �����յ㣬��ʱ�洢�˽ڵ㣬����ѭ���������������ϣ���ѯ��Ӧ��userTask�ڵ�  
                    parallelGateways.add(activityImpl);  
                }  
            } 
            else if (ConstanceUtil.START.equals(type)) 
            {// ��ʼ�ڵ㣬ֹͣ�ݹ�  
                return rtnList;  
            } 
            else if (ConstanceUtil.UT.equals(type)) 
            {// �û�����  
                tempList.add(activityImpl);  
            } 
            else if (ConstanceUtil.EG.equals(type)) 
            {// ��֧·�ߣ���ʱ�洢�˽ڵ㣬����ѭ���������������ϣ���ѯ��Ӧ��userTask�ڵ�  
                currActivity = transitionImpl.getSource();  
                exclusiveGateways.add(currActivity);  
            }  
        }  
        
        //ֻ������һ���ڵ㣺
        //�ҵ���һ��userTask��Ͳ���Ҫ�ݹ���
        if(tempList.size()==0) 
        {
        /** 
         * ����������֧���ϣ���ѯ��Ӧ��userTask�ڵ� 
         */  
        for (ActivityImpl activityImpl : exclusiveGateways) {  
            iteratorBackActivity(taskId, activityImpl, rtnList, tempList);  
        }  
  
        /** 
         * �������м��ϣ���ѯ��Ӧ��userTask�ڵ� 
         */  
        for (ActivityImpl activityImpl : parallelGateways) {  
            iteratorBackActivity(taskId, activityImpl, rtnList, tempList);  
        }  
        }
  
        /** 
         * ����ͬ��userTask���ϣ�������������Ľڵ� 
         */  
        currActivity = filterNewestActivity(processInstance, tempList);  
        if (currActivity != null) 
        {  
            // ��ѯ��ǰ�ڵ�������Ƿ�Ϊ�����յ㣬����ȡ�������ID  
            String id = findParallelGatewayId(currActivity);  
            if (StringUtil.isBlank(id)) 
            {// �������IDΪ�գ��˽ڵ������ǲ����յ㣬���ϲ����������洢�˽ڵ�  
                rtnList.add(currActivity);  
            } 
            else 
            {// ���ݲ������ID��ѯ��ǰ�ڵ㣬Ȼ�������ѯ���Ӧ��userTask����ڵ�  
                currActivity = findActivitiImpl(taskId, id);  
            }  
  
            // ��ձ��ε�����ʱ����  
            tempList.clear();  
           
            // ִ���´ε�������Ѱǰ����������ڵ� 
          //  iteratorBackActivity(taskId, currActivity, rtnList, tempList);  
        }  
        return rtnList;  
    }  
    
    /** 
     * ����ѭ���������ṹ����ѯ��ǰ�ڵ�ɲ��ص�����ڵ� 
     *  
     * @param taskId 
     *            ��ǰ����ID 
     * @param currActivity 
     *            ��ǰ��ڵ� 
     * @param rtnList 
     *            �洢���˽ڵ㼯�� 
     * @param tempList 
     *            ��ʱ�洢�ڵ㼯�ϣ��洢һ�ε��������е�ͬ��userTask�ڵ㣩 
     * @return ��һ�ڵ�
     */  
    public ActivityImpl BackActivity(String taskId,  
    		ActivityImpl currActivity, 
    		List<ActivityImpl> rtnList,  
            List<ActivityImpl> tempList) throws Exception {  
        // ��ѯ���̶��壬�����������ṹ  
        ProcessInstance processInstance = findProcessInstanceByTaskId(taskId);  
  
        // ��ǰ�ڵ��������Դ  
        List<PvmTransition> incomingTransitions = currActivity.getIncomingTransitions();  
        
        // ������֧�ڵ㼯�ϣ�userTask�ڵ������ϣ����������˼��ϣ���ѯ������֧��Ӧ��userTask�ڵ�  
        List<ActivityImpl> exclusiveGateways = new ArrayList<ActivityImpl>(); 
        
        // ���нڵ㼯�ϣ�userTask�ڵ������ϣ����������˼��ϣ���ѯ���нڵ��Ӧ��userTask�ڵ�  
        List<ActivityImpl> parallelGateways = new ArrayList<ActivityImpl>();  
        
        // ������ǰ�ڵ���������·��  
        for (PvmTransition pvmTransition : incomingTransitions) 
        {  
            TransitionImpl transitionImpl = (TransitionImpl) pvmTransition;  
            ActivityImpl activityImpl = transitionImpl.getSource();  
            String type = (String) activityImpl.getProperty("type");  
            /** 
             * ���нڵ�����Ҫ��<br> 
             * ����ɶԳ��֣���Ҫ��ֱ����ýڵ�IDΪ:XXX_start(��ʼ)��XXX_end(����) 
             */  
            if (ConstanceUtil.PG.equals(type)) 
            {// ����·��  
                String gatewayId = activityImpl.getId();  
                String gatewayType = gatewayId.substring(gatewayId  
                        .lastIndexOf("_") + 1);  
                if ("START".equals(gatewayType.toUpperCase())) 
                {// ������㣬ֹͣ�ݹ�  
                    return null;  
                } 
                else 
                {// �����յ㣬��ʱ�洢�˽ڵ㣬����ѭ���������������ϣ���ѯ��Ӧ��userTask�ڵ�  
                    parallelGateways.add(activityImpl);  
                }  
            } 
            else if (ConstanceUtil.START.equals(type)) 
            {// ��ʼ�ڵ㣬ֹͣ�ݹ�  
                return null;  
            } 
            else if (ConstanceUtil.UT.equals(type)) 
            {// �û�����  
                tempList.add(activityImpl);  
            } 
            else if (ConstanceUtil.EG.equals(type)) 
            {// ��֧·�ߣ���ʱ�洢�˽ڵ㣬����ѭ���������������ϣ���ѯ��Ӧ��userTask�ڵ�  
                currActivity = transitionImpl.getSource();  
                exclusiveGateways.add(currActivity);  
            }  
        }  
        
        /** 
         * ����������֧���ϣ���ѯ��Ӧ��userTask�ڵ� 
         */  
        for (ActivityImpl activityImpl : exclusiveGateways) {  
            iteratorBackActivity(taskId, activityImpl, rtnList, tempList);  
        }  
  
        /** 
         * �������м��ϣ���ѯ��Ӧ��userTask�ڵ� 
         */  
        for (ActivityImpl activityImpl : parallelGateways) {  
            iteratorBackActivity(taskId, activityImpl, rtnList, tempList);  
        }  
  
        /** 
         * ����ͬ��userTask���ϣ�������������Ľڵ� 
         */  
        currActivity = filterNewestActivity(processInstance, tempList);  
        if (currActivity != null) 
        {  
            // ��ѯ��ǰ�ڵ�������Ƿ�Ϊ�����յ㣬����ȡ�������ID  
            String id = findParallelGatewayId(currActivity);  
            if (StringUtil.isBlank(id)) 
            {// �������IDΪ�գ��˽ڵ������ǲ����յ㣬���ϲ����������洢�˽ڵ�  
                if(currActivity.getProperty("type").equals("userTask")) {
                	 rtnList.add(currActivity);  
                	 return currActivity;
                }
            } 
            else 
            {// ���ݲ������ID��ѯ��ǰ�ڵ㣬Ȼ�������ѯ���Ӧ��userTask����ڵ�  
                currActivity = findActivitiImpl(taskId, id);  
            }  
  
            // ��ձ��ε�����ʱ����  
            tempList.clear();  
            // ִ���´ε���  
            iteratorBackActivity(taskId, currActivity, rtnList, tempList);  
        }  
        return null;  
    }  
    
    /** 
     * �����������񼯺ϣ���ѯ���һ�ε���������ڵ� 
     *  
     * @param processInstance 
     *            ����ʵ�� 
     * @param tempList 
     *            �������񼯺� 
     * @return 
     */  
    public ActivityImpl filterNewestActivity(ProcessInstance processInstance,  
            List<ActivityImpl> tempList) {  
        while (tempList.size() > 0) {  
            ActivityImpl activity_1 = tempList.get(0);  
            HistoricActivityInstance activityInstance_1 = findHistoricUserTask(  
                    processInstance, activity_1.getId());  
            if (activityInstance_1 == null) {  
                tempList.remove(activity_1);  
                continue;  
            }  
  
            if (tempList.size() > 1) {  
                ActivityImpl activity_2 = tempList.get(1);  
                HistoricActivityInstance activityInstance_2 = findHistoricUserTask(  
                        processInstance, activity_2.getId());  
                if (activityInstance_2 == null) {  
                    tempList.remove(activity_2);  
                    continue;  
                }  
  
                if (activityInstance_1.getEndTime().before(  
                        activityInstance_2.getEndTime())) {  
                    tempList.remove(activity_1);  
                } else {  
                    tempList.remove(activity_2);  
                }  
            } else {  
                break;  
            }  
        }  
        if (tempList.size() > 0) {  
            return tempList.get(0);  
        }  
        return null;  
    }
    
    
    /** 
     * ��ѯָ������ڵ�����¼�¼ 
     *  
     * @param processInstance 
     *            ����ʵ�� 
     * @param activityId 
     * @return 
     */  
    public HistoricActivityInstance findHistoricUserTask(  
            ProcessInstance processInstance, String activityId) {  
        HistoricActivityInstance rtnVal = null;  
        // ��ѯ��ǰ����ʵ��������������ʷ�ڵ�  
        List<HistoricActivityInstance> historicActivityInstances = historyService  
                .createHistoricActivityInstanceQuery().activityType("userTask")  
                .processInstanceId(processInstance.getId()).activityId(  
                        activityId).finished()  
                .orderByHistoricActivityInstanceEndTime().desc().list();  
        if (historicActivityInstances.size() > 0) {  
            rtnVal = historicActivityInstances.get(0);  
        }  
  
        return rtnVal;  
    }  
    
    /** 
     * ���ݵ�ǰ�ڵ㣬��ѯ��������Ƿ�Ϊ�����յ㣬���Ϊ�����յ㣬��ƴװ��Ӧ�Ĳ������ID 
     *  
     * @param activityImpl 
     *            ��ǰ�ڵ� 
     * @return 
     */  
    public String findParallelGatewayId(ActivityImpl activityImpl) {  
        List<PvmTransition> incomingTransitions = activityImpl  
                .getOutgoingTransitions();  
        for (PvmTransition pvmTransition : incomingTransitions) {  
            TransitionImpl transitionImpl = (TransitionImpl) pvmTransition;  
            activityImpl = transitionImpl.getDestination();  
            String type = (String) activityImpl.getProperty("type");  
            if ("parallelGateway".equals(type)) {// ����·��  
                String gatewayId = activityImpl.getId();  
                String gatewayType = gatewayId.substring(gatewayId  
                        .lastIndexOf("_") + 1);  
                if ("END".equals(gatewayType.toUpperCase())) {  
                    return gatewayId.substring(0, gatewayId.lastIndexOf("_"))  
                            + "_start";  
                }  
            }  
        }  
        return null;  
    }  
  
    /** 
     * ��������list���ϣ����ڲ��ؽڵ㰴˳����ʾ 
     *  
     * @param list 
     * @return 
     */  
    public List<ActivityImpl> reverList(List<ActivityImpl> list) {  
        List<ActivityImpl> rtnList = new ArrayList<ActivityImpl>();  
        // ���ڵ��������ظ����ݣ��ų��ظ�  
        for (int i = list.size(); i > 0; i--) {  
            if (!rtnList.contains(list.get(i - 1)))  
                rtnList.add(list.get(i - 1));  
        }  
        return rtnList;  
    }  
    
    /**
     * �������
     * @param taskId
     * @param map
     */
    public void taskComplete(String taskId,Map map)
    {
    	taskService.complete(taskId, map);
    }
    
  
    /**
     * ����
     * @param taskId
     * @param activityId
     * @throws Exception
     */
    public void backProcess(String taskId, String activityId,String reject) throws Exception
    {
    	ProcessInstance processInstance= findProcessInstanceByTaskId(taskId);
    	String piId=processInstance.getId();
    	Map map=runtimeService.getVariables(piId);
    	map.put("reject", reject);
    	backProcess(taskId, activityId, map);
    	processDao.changeProcessInstanceStatus(ConstanceUtil.PRO_RUN,processInstance.getProcessInstanceId());
    }
    
    
    /** 
     * �������� 
     *  
     * @param taskId 
     *            ��ǰ����ID 
     * @param activityId 
     *            ���ؽڵ�ID 
     * @param variables 
     *            ���̴洢���� 
     * @throws Exception 
     */  
    public void backProcess(String taskId, String activityId,  
            Map<String, Object> variables) throws Exception {  
        if (StringUtil.isBlank(activityId)) {  
            throw new Exception("����Ŀ��ڵ�IDΪ�գ�");  
        }  
  
        // �������в�������ڵ㣬ͬʱ����  
        List<Task> taskList = findTaskListByKey(findProcessInstanceByTaskId(taskId).getId(), findTaskById(taskId).getTaskDefinitionKey());  
        for (Task task : taskList) {  
            commitProcess(task.getId(), variables, activityId);  
        }  
    }
    
    /** 
     * ��������ʵ��ID������keyֵ��ѯ����ͬ�����񼯺� 
     *  
     * @param processInstanceId 
     * @param key 
     * @return 
     */  
    public List<Task> findTaskListByKey(String processInstanceId, String key) {  
        return taskService.createTaskQuery().processInstanceId(  
                processInstanceId).taskDefinitionKey(key).list();  
    }  
    
    /** 
     * @param taskId 
     *            ��ǰ����ID 
     * @param variables 
     *            ���̱��� 
     * @param activityId 
     *            ����ת��ִ������ڵ�ID<br> 
     *            �˲���Ϊ�գ�Ĭ��Ϊ�ύ���� 
     * @throws Exception 
     */  
    public void commitProcess(String taskId, Map<String, Object> variables,  
            String activityId) throws Exception {  
        if (variables == null) {  
            variables = new HashMap<String, Object>();  
        }  
        // ��ת�ڵ�Ϊ�գ�Ĭ���ύ����  
        if (StringUtil.isBlank(activityId)) {  
            taskService.complete(taskId, variables);  
        } else {// ����ת�����  
            turnTransition(taskId, activityId, variables);  
        }  
    }
    
    /** 
     * ����ת����� 
     *  
     * @param taskId 
     *            ��ǰ����ID 
     * @param activityId 
     *            Ŀ��ڵ�����ID 
     * @param variables 
     *            ���̱��� 
     * @throws Exception 
     */  
    public void turnTransition(String taskId, String activityId,  
            Map<String, Object> variables) throws Exception {  
        // ��ǰ�ڵ�  
        ActivityImpl currActivity = findActivitiImpl(taskId, null);  
        // ��յ�ǰ����  
        List<PvmTransition> oriPvmTransitionList = clearTransition(currActivity);  
  
        // ����������  
        TransitionImpl newTransition = currActivity.createOutgoingTransition();  
        // Ŀ��ڵ�  
        ActivityImpl pointActivity = findActivitiImpl(taskId, activityId);  
        // �����������Ŀ��ڵ�  
        newTransition.setDestination(pointActivity);  
        // ִ��ת������  
        taskService.complete(taskId, variables);  
        // ɾ��Ŀ��ڵ�������  
        pointActivity.getIncomingTransitions().remove(newTransition);  
  
        // ��ԭ��ǰ����  
        restoreTransition(currActivity, oriPvmTransitionList);  
    }  

    /** 
     * ���ָ����ڵ����� 
     *  
     * @param activityImpl 
     *            ��ڵ� 
     * @return �ڵ����򼯺� 
     */  
    public List<PvmTransition> clearTransition(ActivityImpl activityImpl) {  
        // �洢��ǰ�ڵ�����������ʱ����  
        List<PvmTransition> oriPvmTransitionList = new ArrayList<PvmTransition>();  
        // ��ȡ��ǰ�ڵ��������򣬴洢����ʱ������Ȼ�����  
        List<PvmTransition> pvmTransitionList = activityImpl  
                .getOutgoingTransitions();  
        for (PvmTransition pvmTransition : pvmTransitionList) {  
            oriPvmTransitionList.add(pvmTransition);  
        }  
        pvmTransitionList.clear();  
  
        return oriPvmTransitionList;  
    }
    
    /** 
     * ��ԭָ����ڵ����� 
     *  
     * @param activityImpl 
     *            ��ڵ� 
     * @param oriPvmTransitionList 
     *            ԭ�нڵ����򼯺� 
     */  
    public static void restoreTransition(ActivityImpl activityImpl,  
            List<PvmTransition> oriPvmTransitionList) {  
        // �����������  
        List<PvmTransition> pvmTransitionList = activityImpl  
                .getOutgoingTransitions();  
        pvmTransitionList.clear();  
        // ��ԭ��ǰ����  
        for (PvmTransition pvmTransition : oriPvmTransitionList) {  
            pvmTransitionList.add(pvmTransition);  
        }  
    }  

    public JspPage findhistoricProcessInstanceList(int curpage, int perpage)
    {
    	JspPage jspPage=null;
    	int rowcount =historyService.createHistoricProcessInstanceQuery().finished().list().size();
    	jspPage = new JspPage(perpage, curpage, rowcount);
    	List list=historyService.createHistoricProcessInstanceQuery().finished().orderByProcessInstanceEndTime().desc().listPage((curpage-1)*perpage, perpage);
    	jspPage.setList(list);
    	return jspPage;
    }
    
    
    /**
     * ����taskId��ȡtask���ⲿ��
     * @param taskId
     * @return
     */
    public String findFormKeyContent(String taskId)
    {
    	// ��������ID��ȡ�ⲿ��
    	String renderedTaskForm=(String)formService.getRenderedTaskForm(taskId);
    	return renderedTaskForm;
    }
    
    /**
     * �������
     * @param taskId
     * @param map
     */
    public void taskFormKeyComplete(String taskId,Map map) throws Exception
    {  /* Task instance = null;
    	try {
			 instance = findTaskById(taskId);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}*/
    	/*taskService.complete(taskId, map);*/
    	
    	formService.submitTaskFormData(taskId, map);
    	/*try {
			List<Task> taks = taskService.createTaskQuery().taskUnnassigned().processInstanceId(instance.getProcessInstanceId()).list();
			for(Task task : taks) {
			List<IdentityLink>	list = taskService.getIdentityLinksForTask(task.getId());
			for(IdentityLink link : list) {
				
				System.out.println(link.getGroupId()+"--"+link.getUserId()+"--"+link.getTaskId());
			}
				
			}
			System.out.println(taks.size());
    	} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
    	HistoricProcessInstance instance = findHistoriceProcessInstanceByTaskId(taskId);
    	if(null==instance.getEndTime()) {
    		processDao.changeProcessInstanceStatus(ConstanceUtil.PRO_RUN, instance.getId());
    	}else {
    		processDao.changeProcessInstanceStatus(ConstanceUtil.PRO_END, instance.getId());
    	}
    }
    
    /**
     * �������
     * @param taskId
     * @param executionId
     * @param map
     */
    public void taskFormKeyComplete(String taskId,String executionId) throws Exception
    {
    	
    	Map map=runtimeService.getVariables(executionId);
    	formService.submitTaskFormData(taskId, map);
    	HistoricProcessInstance instance = findHistoriceProcessInstanceByTaskId(taskId);
    	if(null==instance.getEndTime()) {
    		processDao.changeProcessInstanceStatus(ConstanceUtil.PRO_RUN, instance.getId());
    	}else {
    		processDao.changeProcessInstanceStatus(ConstanceUtil.PRO_END, instance.getId());
    	}
    	
    }
    
    
    /**
     * ������� ����������ʷ�������͵�ǰ������
     * @param taskId
     * @param executionId
     * @param data ��������������
     * @throws Exception
     */
    public void taskFormKeyComplete(String taskId,String executionId,Map<String,Object> data) throws Exception
    {
    	
    	Map map=runtimeService.getVariables(executionId);
    	map.putAll(data);
    	formService.submitTaskFormData(taskId, map);
    	HistoricProcessInstance instance = findHistoriceProcessInstanceByTaskId(taskId);
    	if(null==instance.getEndTime()) {
    		processDao.changeProcessInstanceStatus(ConstanceUtil.PRO_RUN, instance.getId());
    	}else {
    		processDao.changeProcessInstanceStatus(ConstanceUtil.PRO_END, instance.getId());
    	}
    	
    }
    
    /**
     * �������
     * @param taskId
     * @param executionId
     * @param map
     */
    public void taskFormKeyComplete(String taskId,String executionId,String banjie)
    {
    	Map map=runtimeService.getVariables(executionId);
    	map.put("banjie", banjie);
    	formService.submitTaskFormData(taskId, map);
    }
    
    /**
     * ����processDefinitionId��executionId������ActivityImpl
     * @param processDefinitionId
     * @param executionId
     * @return
     * @throws Exception
     */
    public ActivityImpl findActivityImplByExecutionId(String processDefinitionId,String executionId)
    		throws Exception
    {
    	ProcessDefinitionEntity processDefinitionEntity = findProcessDefinitionEntity(processDefinitionId);  
    	ExecutionEntity execution = findExecutionEntity(executionId);
    	// ��ǰʵ����ִ�е��ĸ��ڵ�
    	String activitiId = execution.getActivityId();
    	// ��õ�ǰ��������нڵ�
    	List<ActivityImpl> activitiList = processDefinitionEntity.getActivities();   
    	ActivityImpl actImpl = null;
    	for (ActivityImpl activityImpl : activitiList)
    	{   
    		String id = activityImpl.getId();   
    		if (id.equals(activitiId)) 
    		{   
    		    actImpl = activityImpl;   
    			break;   
    		}   
    	} 
    	return actImpl;
    }
    
    /**
     * ����processDefinitionId����ProcessDefinitionEntity
     * @param processDefinitionId
     * @return
     */
    public ProcessDefinitionEntity findProcessDefinitionEntity(String processDefinitionId)
    {
    	ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)   
                .getDeployedProcessDefinition(processDefinitionId);  
    	return processDefinitionEntity;
    }
    
    /**
     * ����executionId����ExecutionEntity
     * @param executionId
     * @return
     */
    public ExecutionEntity findExecutionEntity(String executionId)
    {
    	ExecutionEntity execution = (ExecutionEntity) runtimeService.createExecutionQuery()
				.executionId(executionId)
				.singleResult();
    	return execution;
    }
    /**
     * 
     * Description:��ѯ����ʵ������ʷ����ڵ�
     * Date:2012-11-27
     * @author hexinlin
     * @return List<HistoricTaskInstance>
     */
    public List<HistoricTaskInstance> findHistoricTaskInstance(String processInstanceId) {
    	List<HistoricTaskInstance> histasklist = new ArrayList<HistoricTaskInstance>();
     	histasklist =  historyService.createHistoricTaskInstanceQuery().processInstanceId(processInstanceId).list();
     	return histasklist;
    }
    
  
    /**
     * 
     * Description:��ȡ�û��Ѱ충��
     * Date:2012-11-27
     * @author hexinlin
     * @return List<HistoricProcessInstance>
     */
    public JspPage findHistoricProcessInstance(String userId,int curpage, int perpage){
    	JspPage jspPage=null;
    	List<HistoricProcessInstance> hisprocesslist = new ArrayList<HistoricProcessInstance>();
    	List<HistoricTaskInstance> histasklist = new ArrayList<HistoricTaskInstance>();
    	histasklist = historyService.createHistoricTaskInstanceQuery().taskAssignee(userId).finished().orderByProcessInstanceId().desc().list();
    	String temp = "";
    	Set<String> sets = new HashSet<String>();
    	for(HistoricTaskInstance task : histasklist) {
    		temp = task.getProcessInstanceId();
    		if(sets.contains(temp)) {
    			continue;
    		}
    		sets.add(temp);
    	}
    	if(sets.size()>0) {
    		
        	int rowcount =historyService.createHistoricProcessInstanceQuery().processInstanceIds(sets).list().size();
        	jspPage = new JspPage(perpage, curpage, rowcount);
        	List list=historyService.createHistoricProcessInstanceQuery().processInstanceIds(sets).orderByProcessInstanceId().desc().listPage((curpage-1)*perpage, perpage);
        	jspPage.setList(list);
    	}
    	return jspPage;
    }
    

    /**
     * ���ݻʵ��ID�����һ�ڵ�
     * @param processInstanceId
     * @return
     */
    	public ActivityImpl findActivityImplByprocessInstanceId(String processInstanceId){
    		String processDefinitionId = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult().getProcessDefinitionId();
    		
    		ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity) ((RepositoryServiceImpl)repositoryService).getDeployedProcessDefinition(processDefinitionId);
    		
    		
    		ExecutionEntity execution = (ExecutionEntity) runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
    		
    		
    		
    		if(execution==null) return null;
    		String activitiId = execution.getActivityId();
    		List<ActivityImpl> activitiList = processDefinitionEntity.getActivities();
    		String id = null;
    		for(ActivityImpl activityImpl:activitiList)
    		{  
    			id = activityImpl.getId(); 
    			if(activitiId.equals(id))
    			{
    				System.out.println(activityImpl.getProperty("name"));
    				if("endEvent".equals(activityImpl.getProperty("type"))) return null;
    				return activityImpl;
    			}
    		}
    		return null;
    	}
    	
    
/**
 * ���ҵ�ǰ������ڵ�
 * @param processInstanceId
 * @return
 */
	public TaskDefinition findCurrentTaskDefinition(String processInstanceId){
		ActivityImpl activityImpl=findActivityImplByprocessInstanceId(processInstanceId);
		return currentTaskDefinition(activityImpl);
	}
	
	private TaskDefinition currentTaskDefinition(ActivityImpl activityImpl){
			if("userTask".equals(activityImpl.getProperty("type")))
			{
				TaskDefinition taskDefinition = ((UserTaskActivityBehavior)activityImpl.getActivityBehavior()).getTaskDefinition();
				return taskDefinition;
			}
			else
			{
			return null;
			}
	}
	
    /**
     * ��ȡ������������ʵ���������¼�
     * @param taskId
     * @return
     */
    public String getProcessInstanceStartTime(String taskId) {
    	SimpleDateFormat sdf=new  SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	return sdf.format(historyService.createHistoricProcessInstanceQuery().processInstanceId(taskService.createTaskQuery().taskId(taskId).singleResult().getProcessInstanceId()).singleResult().getStartTime());
    }

	/**
	 * ��ѯ���̿�ʼ��
	 * @param processInstanceId
	 * @return
	 */
	public String findStartUserIdByProcessInstanceId(String processInstanceId)
	{
		return processDao.findStartUserIdByProcessInstanceId(processInstanceId);
	}
	
	 /** 
     * ���ݵ�ǰ����ID����ѯ�������̷����˽ڵ�
     *  
     * @param taskId 
     *            ��ǰ����ID 
     */  
    public ActivityImpl findAfterActivityImpl(String taskId) 
    		throws Exception { 
    	
    	ActivityImpl activityImpl=findActivitiImpl(taskId, null);
        ActivityImpl afterActivityImpl =  findAfterActivity(taskId,activityImpl , new ArrayList<ActivityImpl>());  
        return afterActivityImpl;  
    }
    
    /** 
     * ����ѭ���������ṹ����ѯ�������̷����˽ڵ�
     *  
     * @param taskId 
     *            ��ǰ����ID 
     * @param currActivity 
     *            ��ǰ��ڵ� 
     * @param tempList 
     *            ��ʱ�洢�ڵ㼯�ϣ��洢����userTask�ڵ㣩 
     * @return  
     */  
    public ActivityImpl findAfterActivity(String taskId,  
    		ActivityImpl currActivity, 
            List<ActivityImpl> tempList) throws Exception {  
        // ��ѯ���̶��壬�����������ṹ  
        ProcessInstance processInstance = findProcessInstanceByTaskId(taskId);  
  
        // ��ǰ�ڵ����������  
        List<PvmTransition> outcomingTransitions = currActivity.getOutgoingTransitions(); 
        
        // ������֧�ڵ㼯�ϣ�userTask�ڵ������ϣ����������˼��ϣ���ѯ������֧��Ӧ��userTask�ڵ�  
        List<ActivityImpl> exclusiveGateways = new ArrayList<ActivityImpl>(); 
        
        // ���нڵ㼯�ϣ�userTask�ڵ������ϣ����������˼��ϣ���ѯ���нڵ��Ӧ��userTask�ڵ�  
        List<ActivityImpl> parallelGateways = new ArrayList<ActivityImpl>();  
        
        // ������ǰ�ڵ���������·��  
        for (PvmTransition pvmTransition : outcomingTransitions) {  
            TransitionImpl transitionImpl = (TransitionImpl) pvmTransition;  
            ActivityImpl activityImpl = transitionImpl.getDestination();  
            String type = (String) activityImpl.getProperty("type");  
            /** 
             * ���нڵ�����Ҫ��<br> 
             * ����ɶԳ��֣���Ҫ��ֱ����ýڵ�IDΪ:XXX_start(��ʼ)��XXX_end(����) 
             */  
            if (ConstanceUtil.PG.equals(type)) {// ����·��  
                String gatewayId = activityImpl.getId();  
                String gatewayType = gatewayId.substring(gatewayId  
                        .lastIndexOf("_") + 1);  
                if ("START".equals(gatewayType.toUpperCase())) {// ������㣬ֹͣ�ݹ�  
                    return null;  
                } else {// �����յ㣬��ʱ�洢�˽ڵ㣬����ѭ���������������ϣ���ѯ��Ӧ��userTask�ڵ�  
                    parallelGateways.add(activityImpl);
                }  
            } else if (ConstanceUtil.START.equals(type)) {// ��ʼ�ڵ㣬ֹͣ�ݹ�  
                return null;  
            } else if (ConstanceUtil.UT.equals(type)) {// �û�����  
                tempList.add(activityImpl); 
                findAfterActivity(taskId, activityImpl, tempList);  
                
            } else if (ConstanceUtil.EG.equals(type)) {// ��֧·�ߣ���ʱ�洢�˽ڵ㣬����ѭ���������������ϣ���ѯ��Ӧ��userTask�ڵ�  
                currActivity = transitionImpl.getDestination();  
                exclusiveGateways.add(currActivity); 
            }  
        }  
        
        /** 
         * ����������֧���ϣ���ѯ��Ӧ��userTask�ڵ� 
         */  
        for (ActivityImpl activityImpl : exclusiveGateways) 
        {  
        	findAfterActivity(taskId, activityImpl, tempList);  
        } 
       
        /** 
         * �������м��ϣ���ѯ��Ӧ��userTask�ڵ� 
         */  
        for (ActivityImpl activityImpl : parallelGateways) {  
        	findAfterActivity(taskId, activityImpl, tempList);  
        } 
        
        for(ActivityImpl ai:tempList)
        {
        	String name=(String)ai.getProperty("name");
        	if(name.equals(ConstanceUtil.BEFORE_END)) return ai;
        }
        
        return null;  
    }  
    
    /**
     * ������̣�ֱ���ύ�����̷�����
     * @param taskId
     * @param activityId
     * @throws Exception
     *//*
    public void afterProcess(String taskId, String activityId) throws Exception
    {
    	ProcessInstance processInstance= findProcessInstanceByTaskId(taskId);
    	String piId=processInstance.getId();
    	Map map=runtimeService.getVariables(piId);
    	afterProcess(taskId, activityId, map);
    	processDao.changeProcessInstanceStatus(ConstanceUtil.PRO_HALF,processInstance.getProcessInstanceId());//�趨״̬Ϊ���
    	
    }
    */
    /**
     * ���
     * @param taskId
     * @param activityId
     * @param variables
     * @param flag
     * @throws Exception
     */
    public void afterProcess(String taskId, String activityId, Map<String, Object> variables,String flag) throws Exception
    {
    	ProcessInstance processInstance= findProcessInstanceByTaskId(taskId);
    	Map map = new HashMap();
    	if(ConstanceUtil.FORM_READ_WRITE.equals(flag)) { //��ǰ���� ����д
    		map = runtimeService.getVariables(processInstance.getId());
    		map.putAll(variables);
    	}else if(ConstanceUtil.FORM_WRITE.equals(flag)) {//��ǰ���� ��д
    		map = variables;
    	}else if(ConstanceUtil.FORM_READ.equals(flag)) {//��ǰ���� ����
    		map = runtimeService.getVariables(processInstance.getId());
    	}
    	afterProcess(taskId, activityId, map);
    	processDao.changeProcessInstanceStatus(ConstanceUtil.PRO_HALF,processInstance.getProcessInstanceId());//�趨״̬Ϊ���
    }
    
    public void afterProcess(String taskId, String activityId,  
            Map<String, Object> variables) throws Exception {  
        if (StringUtil.isBlank(activityId)) {  
            throw new Exception("Ŀ��ڵ�IDΪ�գ�");  
        }  
  
        List<Task> taskList = findTaskListByKey(findProcessInstanceByTaskId(taskId).getId(), findTaskById(taskId).getTaskDefinitionKey());  
        for (Task task : taskList) {  
            commitAfterProcess(task.getId(), variables, activityId);  
        }  
    }
    
    public void commitAfterProcess(String taskId, Map<String, Object> variables,  
            String activityId) throws Exception {  
        if (variables == null) {  
            variables = new HashMap<String, Object>();  
        }  
        // ��ת�ڵ�Ϊ�գ�Ĭ���ύ����  
        if (StringUtil.isBlank(activityId)) {  
            taskService.complete(taskId, variables);  
        } else {// ����ת�����  
        	afterTransition(taskId, activityId, variables);  
        }  
    }
    
    public void afterTransition(String taskId, String activityId,  
            Map<String, Object> variables) throws Exception {  
        // ��ǰ�ڵ�  
        ActivityImpl currActivity = findActivitiImpl(taskId, null);  
        // ��յ�ǰ����  
        List<PvmTransition> oriPvmTransitionList = clearTransition(currActivity);  
  
        // ����������  
        TransitionImpl newTransition = currActivity.createOutgoingTransition();  
        // Ŀ��ڵ�  
        ActivityImpl pointActivity = findActivitiImpl(taskId, activityId);  
        // �����������Ŀ��ڵ�  
        newTransition.setDestination(pointActivity);  
  
        // ִ��ת������  
        taskService.complete(taskId, variables);  
        // ɾ��Ŀ��ڵ�������  
        pointActivity.getIncomingTransitions().remove(newTransition);  
  
        // ��ԭ��ǰ����  
        restoreTransition(currActivity, oriPvmTransitionList);  
    }  
    
    /**
     * ����������Ϣ��������
     * @param processId
     * @return
     */
    public String startProcessAuto(String processId,Map<String,Object> map){
    	//�����������̵��û�
    	Map data = map;
    	String message = "";//�洢������Ϣ��
    	boolean flag = true;//Ϊtrueʱ��ʾ��Ҫǩ�գ�Ϊfalse��ʱ��
    	String userId = (String)data.get("userId");
		identityService.setAuthenticatedUserId(userId);
		data.put("owner",userId);
		data.remove("userId");
    	ProcessInstance instance= runtimeService.startProcessInstanceById(processId, data);//��������
    	if(instance.isEnded()) {
    		processDao.changeProcessInstanceStatus(ConstanceUtil.PRO_END, instance.getProcessInstanceId());
    	}else {
    		processDao.changeProcessInstanceStatus(ConstanceUtil.PRO_START, instance.getProcessInstanceId());
    	}
    	Task task = taskService.createTaskQuery().processInstanceId(instance.getProcessInstanceId()).taskCandidateUser(userId).singleResult();//��ȡ��Ҫǩ�յ�����
    	if(null==task) {
    		task = taskService.createTaskQuery().processInstanceBusinessKey(instance.getProcessInstanceId()).taskAssignee(userId).singleResult();//��ȡ��Ҫ���������
    		if(null==task) {
    			message = "noauth";
    			return message;
    		}else {
    			flag = false;
    		}
    	}
    	if(flag) {
    		taskService.claim(task.getId(), userId);
    	}
    	return task.getId();
    }
    
    /**
     * ��ȡ���̶����б�
     * @return
     */
    public List<ProcessDefinition> findPdList() {
    	List<ProcessDefinition> pdList = repositoryService.createProcessDefinitionQuery().list();
    	return pdList;
    }
    
    /**
     * ��ȡ���̱���
     * @param taskId
     * @return
     */
    public Map<String,Object> getProcessVariables(String taskId)  {
    	ProcessInstance processInstance = null;
    	try {
			processInstance = findProcessInstanceByTaskId(taskId);
		} catch (Exception e) {
			SysLogger.error("getProcessVariables--ʧ��", e);
		}
    	String piId=processInstance.getId();
    	Map map = new HashMap();
    	map=runtimeService.getVariables(piId);
    	return map;
    }
    
    /**
     * ����������ȡ����ʵ��
     * @param status
     * @param startuser
     * @param starttime1
     * @param starttime2
     * @return
     */
    public JspPage queryHistoricProcessInstance(String status,String startuser,String starttime1,String starttime2,int curpage, int perpage){
    	
    	String sql = "select hp.status,hp.START_TIME_ as startTime, hp.START_USER_ID_ as startUser,hp.PROC_DEF_ID_ as processDefinitionId,hp.PROC_INST_ID_ as processInstanceId from act_hi_procinst hp where 1=1 ";
    	String countsql = "select count(*) from act_hi_procinst hp where 1=1 ";
    	if(StringUtil.isNotBlank(status)) {
    		sql = sql + "and hp.status='"+status+"' ";
    		countsql = countsql + "and hp.status='"+status+"' ";
    	}
    	if(StringUtil.isNotBlank(startuser)) {
    		sql = sql + "and hp.START_USER_ID_ like '%"+startuser+"%' ";
    		countsql = countsql + "and hp.START_USER_ID_ like '%"+startuser+"%' ";
    	}
    	if(StringUtil.isNotBlank(starttime1)) {
    		sql = sql + "and hp.START_TIME_ >= '"+starttime1+"' ";
    		countsql = countsql + "and hp.START_TIME_ >= '"+starttime1+"' ";
    	}
    	if(StringUtil.isNotBlank(starttime2)) {
    		sql = sql + "and hp.START_TIME_ <= '"+starttime2+"' ";
    		countsql = countsql + "and hp.START_TIME_ <= '"+starttime2+"' ";
    	}
    	sql=sql+"order by startTime desc ";
    	sql = sql + "limit "+ (curpage-1) + ","+perpage;
    	List<HistoryProcessInstanceModel> list = new ArrayList<HistoryProcessInstanceModel>();
    	DBManager dbm = new DBManager();
    	JspPage jspPage=null;
    	ResultSet rs = null;
    	int rowcount = 0;
    	try {
    	rs = dbm.executeQuery(countsql);
    	if(rs.next()) {
    		rowcount = rs.getInt(1);
    	}
    	    jspPage = new JspPage(perpage, curpage, rowcount);
			rs = dbm.executeQuery(sql);
			ResultSetHandler<List<HistoryProcessInstanceModel>> rsh = new BeanListHandler<HistoryProcessInstanceModel>(HistoryProcessInstanceModel.class);
			list = rsh.handle(rs);
			jspPage.setList(list);
			
		} catch (Exception e) {
			SysLogger.error("queryHistoricProcessInstance--ִ��ʧ��", e);
		}	finally {
			dbm.close();
		}
    	return jspPage;
    }
    
    /**
     * �洢���������
     * @param nodeId
     * @param orderSolution
     * @param alarmType
     * @param userId
     */
    public void stroeOrderSolution(String ordernodetype,String orderSolution,String alarmType,String orderType,String userId) {
    	
    		processDao.storeOrderSolution(ordernodetype,orderSolution, alarmType, orderType, userId);
    	
    }
    /**
     * ���ݽڵ�ID��ȡ����ϵͳ����
     * @param id
     * @return
     */
    public String getNodeOstypeById(String id) {
    	String ostype = "";
    	if(!"0".equals(id)&&StringUtil.isNotBlank(id)) {
    		ostype = processDao.getOstypeById(id);
    	}
    	return ostype;
    }
    /**
     * ƴ��sql ���ڲ�ѯ֪ʶ��
     * @param category
     * @param entity
     * @param subentity
     * @param wordkey
     * @param curpage
     * @param perpage
     * @return
     */
    public JspPage queryKnowledge(String category,String entity,String subentity,String wordkey,int curpage,int perpage){
    	String querySql = "select * from system_knowledgebase sk where 1=1 ";
    	String countSql = "select count(*) from system_knowledgebase sk where 1=1 ";
    	if(!"ȫ��".equals(category)&&StringUtil.isNotBlank((category))) {
    		querySql = querySql + "and sk.category = '"+category+"' ";
    		countSql = countSql + "and sk.category = '"+category+"' ";
    	}
    	if(!"ȫ��".equals(entity)&&StringUtil.isNotBlank((entity))) {
    		querySql = querySql + "and sk.entity = '"+entity+"' ";
    		countSql = countSql + "and sk.entity = '"+entity+"' ";
    	}
    	if(!"ȫ��".equals(subentity)&&StringUtil.isNotBlank((subentity))) {
    		querySql = querySql + "and sk.subentity = '"+subentity+"' ";
    		countSql = countSql + "and sk.subentity = '"+subentity+"' ";
    	}
    	if(StringUtil.isNotBlank(wordkey)&&StringUtil.isNotBlank((wordkey))) {
    		querySql = querySql + "and sk.contents like '%"+wordkey+"%' or sk.attachfiles like '%"+wordkey+"%' or sk.titles like '%"+wordkey+"%' ";
    		countSql = countSql + "and sk.contents like '%"+wordkey+"%' or sk.attachfiles like '%"+wordkey+"%' or sk.titles like '%"+wordkey+"%' ";
    	}
    	return processDao.queryKnowledge(querySql, countSql, curpage, perpage);
    }
    
    
/**
 * ͳ��
 * @param curpage
 * @param perpage
 * @param type//����ʵ����ʽ
 * @param person//��Ա��ʽ
 * @return
 */
 public JspPage processStatisticals(int curpage, int perpage,String type,String person){
	return processDao.processStatisticals(curpage, perpage, type, person);
    }
 
 	
 	/**
 	 * ͳ�������������
 	 * @param type
 	 * @param person
 	 * @return
 	 */
 	 public  List<ProcessStatisticalsModel> processStatisticals(String type,String person,String startdate,String todate){
 		 return processDao.queryProcessStatisticals(type, person, startdate, todate);
 	    }
 	 
 	public JspPage queryProcessStatisticalsDetail(String exectname,String type,String person,String startdate,String todate,int curpage, int perpage){
 		return processDao.queryProcessStatisticalsDetail(exectname,type, person,startdate,todate, curpage, perpage);
 	}
 	
 	/**
 	 * ���ø澯�б����ɹ�����ʶ��0δ���ɣ�1����
 	 * @param enentid���澯ID
 	 */
 	public void setEventOrderFlag(String eventid)
 	{
 		processDao.setEventOrderFlag(eventid);
 	}
 	
 	public String drawBar(List<ProcessStatisticalsModel> list,String person)
 	{
 		return processDao.drawBar(list, person);
 	}
 	public String drawPie(List<ProcessStatisticalsModel> list,String person)
 	{
 		return processDao.drawPie(list, person);
 	}
 	
 	/**
	 * ��������
	 * @param deploymentId
	 * @param userId
	 */
	public String isStart(String processDefinitionId,String userId)
	{
		return processDao.isStart(processDefinitionId, userId);
	}
	
	/**
 	  * �������
 	  * @param category
 	  * @param entity
 	  * @param subentity
 	  * @param wordkey
 	  * @return
 	  */
 	 public List<Knowledgebase> queryKnowledge(String category,String entity,String subentity,String wordkey){
    	String querySql = "select * from system_knowledgebase sk where 1=1 ";
    	if(!"ȫ��".equals(category)&&StringUtil.isNotBlank((category))) {
    		querySql = querySql + "and sk.category = '"+category+"' ";
    	}
    	if(!"ȫ��".equals(entity)&&StringUtil.isNotBlank((entity))) {
    		querySql = querySql + "and sk.entity = '"+entity+"' ";
    	}
    	if(!"ȫ��".equals(subentity)&&StringUtil.isNotBlank((subentity))) {
    		querySql = querySql + "and sk.subentity = '"+subentity+"' ";
    	}
    	if(StringUtil.isNotBlank(wordkey)&&StringUtil.isNotBlank((wordkey))) {
    		querySql = querySql + "and sk.contents like '%"+wordkey+"%' or sk.attachfiles like '%"+wordkey+"%' or sk.titles like '%"+wordkey+"%' ";
    	}
    	return processDao.queryKnowledge(querySql);
    }

 	 public List<String> getListcontent()
 	 {
 		 return processDao.getListcontent();
 	 }
 	 
 	 /**
 	  * ͳ��pieͼ
 	  * @param list
 	  * @return
 	  */
 	 public String getPieXml(List<ProcessStatisticalsModel> list,String person)
 	 {
 		 return processDao.getPieXml(list,person);
 	 }
 	 
 	 /**
 	  * ��״ͼ
 	  * @param list
 	  * @return
 	  */
 	 public String getBarXml(List<ProcessStatisticalsModel> list,String person)
 	 {
 		 return processDao.getBarXml(list,person);
 	 }
 	 
 	/**
 	  * task����
 	  * @param list
 	  * @return
 	  */
 	 public List<String> findContentList(List list)
 	 {
 		 return processDao.findContentList(list);
 	 }
 	 
 	 
 	/**
  	 * ͳ�Ƶ�ǰ����������
  	 * @param type
  	 * @return
  	 */
  	 public  List<ProcessStatisticalsModel> queryProInsStatDetail(String type)
  	 {
  		 return processDao.queryProInsStatDetail(type);
  	 }
  	 
 	 /**
 	  * ͳ��pieͼ
 	  * @param list
 	  * @return
 	  */
 	 public String getProInsPieXml(List<ProcessStatisticalsModel> list)
 	 {
 		 return processDao.getProInsPieXml(list);
 	 }
 	 
 	 /**
 	  * ��״ͼ
 	  * @param list
 	  * @return
 	  */
 	 public String getProInsBarXml(List<ProcessStatisticalsModel> list)
 	 {
 		 return processDao.getProInsBarXml(list);
 	 }
 	 
   	 /**
   	  * ����ͳ��
   	  * @return
   	  */
   	 public HashMap<ProcessEnum, String> queryProIns()
   	 {
   		 return processDao.queryProIns();
   	 }
   	 
   	 /**
   	  * ͳ�Ʊ�ͼ
   	  * @param map
   	  * @return
   	  */
   	 public String pieXmlProIns(HashMap<ProcessEnum, String> map)
	 {
		 return processDao.pieXmlProIns(map);
	 }
   	 
   	 /**
   	  * ͳ����״ͼ
   	  * @param map
   	  * @return
   	  */
	 public String barXmllProIns(HashMap<ProcessEnum, String> map)
	 {
		 return processDao.barXmllProIns(map);
	 }
   	 
}
