package com.afunms.sysset.manage;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.afunms.common.base.BaseManager;
import com.afunms.common.base.ErrorMessage;
import com.afunms.common.base.ManagerInterface;
import com.afunms.common.util.SessionConstant;
import com.afunms.sysset.dao.DBBackupAutoDao;
import com.afunms.sysset.dao.DBBackupDao;
import com.afunms.sysset.model.DBBackup;
import com.afunms.sysset.model.DBBackupAuto;

public class DBBackupManager extends BaseManager implements ManagerInterface
{
	
	public String execute(String action) {
		// ���ݿ� �� �б�
		if ("list".equals(action)) 
		{	
			request.setAttribute("result", "false");
			return getTableList();			
		}
		// �����ļ��б�
		if ("dbbackuplist".equals(action)) 
		{	
			request.setAttribute("result", "false");
			return getDBBackupList();
		}
		// ���ñ���
		if("backup".equals(action))  
		{
			return backup();
		}
		// ���õ��뱸��
		if("load".equals(action))
		{
			return load();
		}
		// ɾ������
		if("delete".equals(action)){
			//return deleteDBBackupFile();
		}
		//�Զ������б�
		if("autobackuplist".equals(action)){
			return autobackuplist();
		}
		if("addbackup".equals(action)){
			return addbackup();
		}
		if("autobackup".equals(action)){
			return autobackup();
		}
		if("downloadDBFile".equals(action)){
			return downloadDBFile();
		}
		if("deleteDBFile".equals(action)){
			return deleteDBFile();
		}
		setErrorCode(ErrorMessage.ACTION_NO_FOUND);
		return null;
		
	}
	
	/**
	 * ɾ��
	 * @return
	 */
	private String deleteDBFile(){
    	boolean result = false;
		String[] id = getParaArrayValue("checkbox");
		List<String> list = new ArrayList<String>();
		DBBackupAutoDao dao = new DBBackupAutoDao();
		try{
			for(int i = 0 ; i< id.length;i++ ){
				DBBackupAuto dbBackup = (DBBackupAuto)dao.findByID(id[i]);
				list.add(dbBackup.getFilename());
			}
			result = dao.delete(id);
			result = true;
		}catch(Exception e){
			result = false;
			e.printStackTrace();
		}finally{
			dao.close();
		}
		for(int i = 0 ; i< list.size();i++ ){
			new File(dao.getFilepath() + list.get(i)).delete();
		}
		if(result){
			request.setAttribute("result", "true");
			request.setAttribute("msg", "���ݿⱸ���ļ�ɾ���ɹ���");
			return autobackuplist();
		}else{
			setErrorCode(-1);
	    	request.setAttribute(SessionConstant.ERROR_INFO, "���ݿⱸ���ļ�ɾ��ʧ�ܣ�");
	    	return null;
		}
	}
	
	/**
	 * ��ӱ���
	 * @return
	 */
	private String addbackup(){
		return "/sysset/dbbackup/addbackup.jsp";
	}
	
	/**
	 * ���ر��ݵ����ݿ��ļ�
	 * @return
	 */
	private String downloadDBFile(){
		String id = request.getParameter("id");
		DBBackupAuto dbBackupAuto = null;
		DBBackupAutoDao dao = new DBBackupAutoDao();
		try {
			dbBackupAuto = (DBBackupAuto)dao.findByID(id);
		} catch (RuntimeException e) {
			e.printStackTrace();
		} finally{
			dao.close();
		}
		String filename = dao.getFilepath()+File.separator+dbBackupAuto.getFilename();
		request.setAttribute("filename", filename);
		return "/sysset/dbbackup/download.jsp";
	}
	
	/**
	 * ִ�б������ݿ⹦��  
	 * @return
	 */
	private String autobackup(){
		//
		String time = getParaValue("time");
		String filename = getParaValue("filename");
		String description = getParaValue("description");
		//����Ƿ�����ͬ���ֵ��ļ�
		DBBackupAutoDao dao = new DBBackupAutoDao();
		List<DBBackupAuto> dbBackupAutoList = null;
		try {
			dbBackupAutoList = dao.findByCondition(" where filename='"+filename.trim()+".sql'");
		} catch (RuntimeException e) {
			e.printStackTrace();
		} finally{
			dao.close();
		}
		if(dbBackupAutoList.size() > 0){
			//���ļ��Ѵ���
			setErrorCode(ErrorMessage.FILENAME_EXIST);
			return null;
		}
		
		//ִ�б��ݵ��߼�
		//ȡ���������Ƽ���
		dao = new DBBackupAutoDao();
		List<String> list = null;
		try {
			list = dao.findByCriteria("show tables");
		} catch (RuntimeException e) {
			e.printStackTrace();
		} finally{
			dao.close();
		}
		backupTables(filename,time, list);
		System.out.println(filename+" "+description);
		return autobackuplist();
	}
	
	/**
	 * @param filename       ���ݿ��ļ���
	 * @param time           ���ݵ�ʱ��
	 * @param tablenameList  ������
	 */
	private String backupTables(String filename,String time, List<String> tablenameList){
		String[] tables = new String[tablenameList.size()];
		for(int i=0; i<tablenameList.size(); i++){
			tables[i] = tablenameList.get(i);
		}
		HashMap<String, String[]> tableMap = getConfigTables(tablenameList);
		//ȡ����Ҫ���ݱ����ݵ����ñ�
		String[] configTables = tableMap.get("configTables");
		String[] tempTables = tableMap.get("tempTables");
		boolean result = false;
		//�������ñ�ı����ݺͱ�ṹ
		DBBackupAutoDao dao = new DBBackupAutoDao();
		try {
			result = dao.backup(configTables, 2, filename);
		} catch (RuntimeException e) {
			e.printStackTrace();
		} finally{
			dao.close();
		}
		//������ʱ��ı�ṹ
		dao = new DBBackupAutoDao();
		try {
			result = dao.backup(tempTables, 0, filename);
		} catch (RuntimeException e) {
			e.printStackTrace();
		} finally{
			dao.close();
		}
		if(result){
			DBBackupAuto dbBackupAuto = new DBBackupAuto();
			dbBackupAuto.setFilename(filename+".sql");
			dbBackupAuto.setTime(time);
			try{
				dao = new DBBackupAutoDao();
				result = dao.save(dbBackupAuto);
			}catch(Exception e){
				new File(dao.getFilepath() + filename+".sql").delete();
			}finally{
				dao.close();
			}
		}
		if(result){
			request.setAttribute("result", "true");
			request.setAttribute("msg", "���ݿⱸ�ݳɹ���");
			return autobackuplist();
		} else {
			setErrorCode(-1);
	    	request.setAttribute(SessionConstant.ERROR_INFO, "���ݿⱸ�ݷ�������������־�ļ���");
	    	return null;
		}
	}
	
	/**
	 * �������еı����˳���Ҫ���ݵ����ñ�
	 * @param tablenameList
	 * @return
	 */
	private HashMap<String, String[]> getConfigTables(List<String> tablenameList){
		List<String> configTablesList = new ArrayList<String>();
		Pattern pattern = Pattern.compile("\\d+_\\d");//���������»��������ı� Ϊ��ʷ���������������
		Pattern dataTempPattern = Pattern.compile("nms_(.*)_data_temp");//����nms_cpu_data_temp����ʱ���������������
		Vector<String> nmsTableVector = new Vector<String>();//һ���ֲ���Ҫ�������ݵı�������ɵļ���
		nmsTableVector.add("nms_alarminfo");
		nmsTableVector.add("nms_checkevent");
		nmsTableVector.add("nms_errptlog");
		nmsTableVector.add("node_indicator_alarm");
		nmsTableVector.add("storageping");
		nmsTableVector.add("system_eventlist");
		List<String> nmsTempTables = new ArrayList<String>();
		
		for(int i=0; i<tablenameList.size(); i++){
			//�Ƿ������ñ�ı�־λ
			boolean isConfigTableFlag = true;
			String tableName = tablenameList.get(i);
			Matcher matcher = pattern.matcher(tableName);
			if(matcher.find()){
				isConfigTableFlag = false;
			}
			Matcher dataTempMatcher = dataTempPattern.matcher(tableName);
			if(dataTempMatcher.find()){
				isConfigTableFlag = false;
			}
			if(tableName.startsWith("nms_apache_(.*)")){
				isConfigTableFlag = false;
			}
			if(tableName.matches("nms_as400_(.*)")){
				isConfigTableFlag = false;
			}
			if(tableName.matches("nms_db2(.*)")){
				isConfigTableFlag = false;
			}
			if(tableName.matches("nms_domino(.*)_realtime")){
				isConfigTableFlag = false;
			}
			if(tableName.matches("nms_informix(.*)")){
				isConfigTableFlag = false;
			}
			if(tableName.matches("nms_ora(.*)")){
				isConfigTableFlag = false;
			}
			if(tableName.matches("nms_sqlserver(.*)")){
				isConfigTableFlag = false;
			}
			if(tableName.matches("nms_sybase(.*)")){
				isConfigTableFlag = false;
			}
			if(tableName.matches("nms_weblogic_(.*)")){
				isConfigTableFlag = false;
			}
			if(nmsTableVector.indexOf(tableName) != -1){
				isConfigTableFlag = false;
			}
			if(isConfigTableFlag){
				configTablesList.add(tableName);
			}else{
				nmsTempTables.add(tableName);
			}
		}
		String[] configTables = new String[configTablesList.size()];
		for(int i=0; i<configTablesList.size(); i++){
			configTables[i] = configTablesList.get(i);
		}
		String[] tempTables = new String[nmsTempTables.size()];
		for(int i=0; i<nmsTempTables.size(); i++){
			tempTables[i] = nmsTempTables.get(i);
		}
		HashMap<String, String[]> tableMap = new HashMap<String, String[]>();
		tableMap.put("tempTables", tempTables);
		tableMap.put("configTables", configTables);
		return tableMap;
	}
	
	public static void main(String[] args){
//		String tableName = "allutilhdxday20_10_1_2";
//		Pattern pattern = Pattern.compile("\\d+_\\d");
//		 Matcher matcher = pattern.matcher(tableName);
//		 System.out.println(matcher.find());
//		 while(matcher.find()){
//			 System.out.println(matcher.group(0));
//		 }
		
		String tableName = "nms_db2lock";
		System.out.println(tableName.matches("nms_db2(.*)"));
	}
	
	
	/**
	 * ��ת���Զ������б�
	 * @return
	 */
	private String autobackuplist(){
		DBBackupAutoDao dBBackupAutoDao = new DBBackupAutoDao();
		List backList = null;
		try{
			backList = dBBackupAutoDao.loadAll();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			dBBackupAutoDao.close();
		}
		request.setAttribute("backList", backList);
		return "/sysset/dbbackup/autobackuplist.jsp";
	}
	
	//��ȡ���ݿ������еı���
	private String getTableList()
	{
		DBBackupDao dao = new DBBackupDao();
		List<String> list = dao.findByCriteria("show tables");
		request.setAttribute("tablesname", list);
		return "/sysset/dbbackup/list.jsp";
	}
	
	/** 
	 * ��ȡ ���б���б�
	 * @return
	 */
	private String getDBBackupList(){
		DBBackupDao dao = new DBBackupDao();
		setTarget("/sysset/dbbackup/dbbackuplist.jsp");
		return list(dao);
	}
	
	/**
	 * ɾ�������ļ�
	 * @return
	 */
	private String deleteDBBackupFile(){
		boolean result = false;
		String[] id = request.getParameterValues("id");
		List<String> list = new ArrayList<String>();
		DBBackupDao dao = new DBBackupDao();
		try{
			for(int i = 0 ; i< id.length;i++ ){
				DBBackup dbBackup = (DBBackup)dao.findByID(id[i]);
				list.add(dbBackup.getFilename());
			}
			result = dao.delete(id);
			result = true;
		}catch(Exception e){
			result = false;
			e.printStackTrace();
		}finally{
			dao.close();
		}
		for(int i = 0 ; i< list.size();i++ ){
			new File(dao.getFilepath() + list.get(i)).delete();
		}
		if(result){
			request.setAttribute("result", "true");
			request.setAttribute("msg", "���ݿⱸ���ļ�ɾ���ɹ���");
			return getDBBackupList();
		}else{
			setErrorCode(-1);
	    	request.setAttribute(SessionConstant.ERROR_INFO, "���ݿⱸ���ļ�ɾ��ʧ�ܣ�");
	    	return null;
		}
	}
	
	/**
	 * ����  ������ݳɹ��� �������ݿ�����Ӽ�¼
	 * �����Ӽ�¼ʧ�� ��ɾ�������ļ�
	 * @return
	 */
	private String backup(){
		String[] tables = getParaArrayValue("checkbox");
		int radio = getParaIntValue("radio");
		DBBackupDao dao = new DBBackupDao();
		boolean result = dao.backup(tables,radio);
		if(result)
		{
			DBBackup dbBackup = new DBBackup();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
			String time = sdf.format(new Date());
			String filename = dao.getFilename();
			dbBackup.setFilename(filename+".sql");
			dbBackup.setTime(time);
			try{
				dao = new DBBackupDao();
				result = dao.save(dbBackup);
			}catch(Exception e){
				new File(dao.getFilepath() + filename+".sql").delete();
			}finally{
				dao.close();
			}
		}
		if(result)
		{
			request.setAttribute("result", "true");
			request.setAttribute("msg", "���ݿⱸ�ݳɹ���");
			return getDBBackupList();
		}
		else
		{
			setErrorCode(-1);
	    	request.setAttribute(SessionConstant.ERROR_INFO, "���ݿⱸ�ݷ�������������־�ļ���");
	    	return null;
		}
		
	}
	/**
	 * ���뱸���ļ�
	 * @return
	 */
	private String load(){
		DBBackupDao dao = new DBBackupDao();
		String filename = request.getParameter("filename");
		boolean result = dao.load(dao.getFilepath()+filename);
		if(result)
		{
			request.setAttribute("msg", "���ݿ⵼��ɹ���");
			request.setAttribute("result", "true");
			return getDBBackupList();
		}
		else
		{
			setErrorCode(-1);
	    	request.setAttribute(SessionConstant.ERROR_INFO, "���ݿ⵼�뷢������������־�ļ���");
	    	return null;
		}
	}
	
	
	
	

}
