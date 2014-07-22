package com.bpm.system.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.impl.persistence.entity.GroupEntity;

/**
 * 
 * Description:
 * ConstanceUtil.java Create on 2012-10-17 ����2:30:33 
 * @author hexinlin
 * Copyright (c) 2012 DHCC Company,Inc. All Rights Reserved.
 */
public class ConstanceUtil {

	public static final String AUTHKEY = "authkey"; //��½��֤
	public static final String USER = "user"; //�洢�û���session�ı�ʾ
	public static final String LOGINACTION = "controller/login.action";//���ڵ�½��action����Ҫ����Ȩ����֤
	
	
	
	public static final String PG="parallelGateway";//ƽ��ͨ��
	public static final String EG="exclusiveGateway";//��֧ͨ��
	public static final String START="startEvent";//��ʼ�ڵ�
	public static final String UT="userTask";//�û�����ڵ�
	public static final Map<String,String> map = new HashMap<String, String>(); 
	public static final String BEFORE_END = "���̷�����";//���ڽ�������
	public static final String PRO_START="1"; //��������
	public static final String PRO_RUN="2"; //��������
	public static final String PRO_HALF="3"; //���̰��
	public static final String PRO_END="4"; //���̽���
	
	public static final String PRO_START_MESSAGE="����"; //��������
	public static final String PRO_RUN_MESSAGE="������"; //��������
	public static final String PRO_HALF_MESSAGE="���"; //���̰��
	public static final String PRO_END_MESSAGE="����"; //���̽���
	
	public static final String FORM_WRITE="0";//����д
	public static final String FORM_READ="1";//������
	public static final String FORM_READ_WRITE="2";//������д
	
	public static final String Process_BANJIE="1";//Ҫ���
	public static final String NO_Process_BANJIE="0";//����Ҫ���
	
	public static List<GroupEntity> bpmgroup  = new ArrayList<GroupEntity>();


	public static List<GroupEntity> getBpmgroup() {
		return bpmgroup;
	}


	public static void setBpmgroup(List<GroupEntity> bpmgroup) {
		ConstanceUtil.bpmgroup = bpmgroup;
	}
	
	static{
		map.put("taskFormType", "0");
	}

}

