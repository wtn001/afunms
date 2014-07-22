package com.bpm.process.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.activiti.engine.task.Task;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.general.DefaultPieDataset;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.afunms.common.base.JspPage;
import com.afunms.common.util.ChartGraph;
import com.afunms.common.util.DBManager;
import com.afunms.common.util.SysLogger;
import com.afunms.config.model.Knowledgebase;
import com.afunms.topology.util.NodeHelper;
import com.bpm.process.model.ProcessStatisticalsModel;
import com.bpm.process.model.ProcessTaskModel;
import com.bpm.system.utils.ConstanceUtil;
import com.bpm.system.utils.ProcessEnum;
import com.bpm.system.utils.StringUtil;


@Repository
@Transactional
public class ProcessDao {

    private static Logger logger = Logger.getLogger(ProcessDao.class);
    private static final String[] COLORS=new String[] {"#006400","#7171C6","#00B2EE","#8B5A00","#00FF00","#8B7B8B","#1E90FF","#CD3700","#0000CD","#E3E3E3"};
	@Resource
    private SessionFactory sessionFactory;
	
	public String findStartUserIdByProcessInstanceId(String processInstanceId){
		DBManager db=new DBManager();
		ResultSet rSet=null;
		String userId=null;
		String sql=String.format("select START_USER_ID_ from act_hi_procinst where PROC_INST_ID_='%s'", processInstanceId);
		rSet=db.executeQuery(sql);
		try {
			if(rSet.next())
			{
				userId= rSet.getString(1);
			}
			if(rSet!=null) rSet.close();
		} catch (SQLException e) {
			logger.error("ProcessDao.findStartUserIdByProcessInstanceId()---ִ�д���", e);
			e.printStackTrace();
		}
		finally
		{
			db.close();
		}
		return userId;
	}	
	
	/**
	 * �ı�����ʵ��״̬��statusֵΪ1��ʾ������2��ʾ�����У�3��ʾ��ء�4��ʾ������
	 */
	public void changeProcessInstanceStatus(String status,String process_instance_id) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createSQLQuery("update act_hi_procinst set STATUS = ? where PROC_INST_ID_ = ?");
		query.setParameter(0, status);
		query.setParameter(1, process_instance_id);
		query.executeUpdate();
	}
	
	
	/**
	 * �洢�������
	 * @param nodeId
	 * @param orderSolution
	 */
	public void stroeOrderSolution(String nodeId,String orderSolution,String alarmType,String userId){
		DBManager dbm = new DBManager();
		String sql = String.format("select category,so.ostypename from topo_host_node thn ,system_ostype so where thn.ostype=so.ostypeid and thn.id = %d ", nodeId);
	    String type = "";
	    String ostype = "";
		ResultSet rs = null;
	    rs = dbm.executeQuery(sql);
	    try {
			if(rs.next()) {
				type = NodeHelper.getNodeCategory(rs.getInt("category"));
				ostype = rs.getString("ostypename");
			}
			if(StringUtil.isNotBlank(type)&&StringUtil.isNotBlank(ostype)) {
				//�洢���������
				sql = String.format("insert into system_knowledgebase (id, category, entity, subentity, titles, contents, bak, attachfiles, userid, ktime)values(default, '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', default)", type,ostype,alarmType,alarmType,orderSolution,"","",userId);
			    dbm.executeUpdate(sql);
			}
		} catch (Exception e1) {
			SysLogger.error("stroeOrderSolution--ʧ��",e1);
		}
	    finally
	    {
	    	if(null!=rs) {
		    	try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		    }
	    	dbm.close();
	    }
	
	}
	/**
	 * �洢�������
	 * @param orderSolution
	 * @param alarmType
	 * @param orderType
	 * @param userId
	 */
	public void storeOrderSolution(String ordernodetype,String orderSolution,String alarmType,String orderType,String userId) {
		if(StringUtil.isNotBlank(alarmType)&&StringUtil.isNotBlank(orderType)&&StringUtil.isNotBlank(ordernodetype)) {
			DBManager dbm = new DBManager();
			String sql = String.format("insert into system_knowledgebase (id, category, entity, subentity, titles, contents, bak, attachfiles, userid, ktime)values(default, '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', default)", orderType,StringUtil.isBlank(ordernodetype)?"":ordernodetype.toLowerCase(),alarmType,alarmType,orderSolution,"","",userId);
			dbm.executeUpdate(sql); 
			dbm.close();
		}
	}
	/**
	 * ͨ���ڵ�ID��ȡ����ϵͳ����
	 * @param nodeId
	 * @return
	 */
	public String getOstypeById(String nodeId) {
		String sql = String.format("select so.ostypename from topo_host_node thn,system_ostype so where thn.ostype = so.ostypeid and thn.id = %s ", nodeId);
		String ostypename = "";
		ResultSet rs = null;
		DBManager dbm = new DBManager();
		try {
			rs = dbm.executeQuery(sql);
			if(rs.next()) {
				ostypename = rs.getString("ostypename");
			}
		} catch (Exception e) {
			SysLogger.error("getOstypeById--ʧ��", e);
		} finally {
			try {
				if(rs!=null)rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			dbm.close();
		}
		return ostypename;
	}
	/**
	 * ��ҳ��ȡ֪ʶ����Ϣ
	 * @param querySql
	 * @param countSql
	 * @param curpage
	 * @param perpage
	 * @return
	 */
	public JspPage queryKnowledge(String querySql,String countSql,int curpage, int perpage){
		DBManager dbm = new DBManager();
		List<Knowledgebase> list = new ArrayList<Knowledgebase>();
		JspPage jspPage = null;
		int rowcount = 0;
		ResultSet rs = null;
		try {
			rs = dbm.executeQuery(countSql);
			if(rs.next()) {
				rowcount = rs.getInt(1);
			}
			jspPage = new JspPage(perpage, curpage, rowcount);
			rs = dbm.executeQuery(querySql);
			ResultSetHandler<List<Knowledgebase>> rsh = new BeanListHandler<Knowledgebase>(Knowledgebase.class);
			list = rsh.handle(rs);
			jspPage.setList(list);
		} catch (SQLException e) {
			SysLogger.error("queryKnowledge--ִ��ʧ��", e);
		} finally {
			if(null!=rs) {
				try {
					rs.close();
				} catch (SQLException e) {
				}
			}
			dbm.close();
		}
		return jspPage;
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
		 JspPage jspPage=null;
		 List list = new ArrayList();
		 String[] sqls=getStatisticalsSql(type, person,null,null);
		 String querySql=sqls[0];
		 String countSql=sqls[1];
		 DBManager db=new DBManager();
		 ResultSet rs=null; 
		 int rowcount=0;
		 try {
			rowcount =db.executeQueryCount(countSql);
		 	jspPage = new JspPage(perpage, curpage, rowcount);
			rs = db.executeQueryFromAll(querySql, jspPage.getMinNum(), perpage);
		 	ResultSetHandler<List<ProcessStatisticalsModel>> rsh = new BeanListHandler<ProcessStatisticalsModel>(ProcessStatisticalsModel.class);
			list = rsh.handle(rs);
			jspPage.setList(list);
		} catch (SQLException e) {
			logger.error("processStatisticals--ִ�г���", e);
		}
		finally
		{
				try {
					if(rs!=null)	rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			db.close();
		}
		 return jspPage;
	    }
	 
	 /**
	  * ͳ��
	  * @param type���뷽ʽ (�����ʵ����ʽ)��0����1ʵ��
	  * @param person//���ź���Ա��ʽ��0��Ա��1��λ
	  * @param startdate��ʼ����
	  * @param todate��������
	  * @return
	  */
	 	public String[] getStatisticalsSql(String type,String person,String startdate, String todate)
	 	{
	 		String[] sqls=new String[2];
	 		if(StringUtil.isBlank(person)) person="0";
	 		if(StringUtil.isBlank(type)) type="0";
	 		
	 		if(person.equals("0") && type.equals("0"))
	 		{
	 			sqls[0]="select count(*) as total,assignee_ as assignee from act_hi_taskinst where 1=1 ";
	 			if(StringUtil.isNotBlank(getDateSql(startdate, todate))) sqls[0]=sqls[0]+getDateSql(startdate, todate);
	 			sqls[0]=sqls[0]+ "group by assignee_  ";
	 		}
	 		else if (person.equals("0") && type.equals("1")) 
	 		{
	 			sqls[0]="select count(*) as total,assignee_ as assignee " +
		 				"from (" +
		 				"select distinct act_hi_procinst.PROC_INST_ID_ ,act_hi_taskinst.assignee_  " +
		 				"from act_hi_procinst, act_hi_taskinst  " +
		 				"where act_hi_taskinst.PROC_INST_ID_=act_hi_procinst.PROC_INST_ID_" ;
	 			if(StringUtil.isNotBlank(getDateSql(startdate, todate))) sqls[0]=sqls[0]+getDateSql(startdate, todate);
	 			sqls[0]=sqls[0]+
		 				") " +
		 				"as temp_table  " +
		 				"group by assignee_";
				
			}
	 		else if (person.equals("1") && type.equals("0")) 
	 		{
	 			sqls[0]="select count(*) as total,act_id_group.id_ as groupid,act_id_group.name_ as groupname " +
	 					"from act_hi_taskinst,act_id_membership,act_id_group " +
	 					"where act_hi_taskinst.ASSIGNEE_ =act_id_membership.user_id_ and act_id_membership.group_id_=act_id_group.id_ " ;
	 			if(StringUtil.isNotBlank(getDateSql(startdate, todate))) sqls[0]=sqls[0]+getDateSql(startdate, todate);
	 			sqls[0]=sqls[0]+"group by act_id_membership.group_id_";
			}
	 		else
	 		{
	 			sqls[0]="select count(*) as total,group_id_ as groupid,name_ as groupname " +
	 					"from (" +
	 					"select distinct act_hi_procinst.PROC_INST_ID_ ,act_id_membership.group_id_,act_id_group.name_ " +
	 					"from act_hi_procinst, act_hi_taskinst,act_id_membership,act_id_group  " +
	 					"where act_hi_taskinst.PROC_INST_ID_=act_hi_procinst.PROC_INST_ID_  and act_hi_taskinst.assignee_=act_id_membership.user_id_ " +
	 					"and act_id_membership.group_id_=act_id_group.id_" ;
	 			if(StringUtil.isNotBlank(getDateSql(startdate, todate))) sqls[0]=sqls[0]+getDateSql(startdate, todate);
	 			sqls[0]=sqls[0]+	")  " +
	 					"as temp_table  " +
	 					"group by group_id_";
	 		}
	 		sqls[1]="select count(*) from ("+sqls[0]+") as temp_table11";
	 		return sqls;
	 	}
	 	
	 	/**
	 	 * ͳ�����ӿ�ʼʱ�䣬����ʱ��
	 	 * @param startdate
	 	 * @param todate
	 	 * @return
	 	 */
	 	public String getDateSql(String startdate,String todate)
	 	{
	 		String dateSql=null;
	 		if(StringUtil.isNotBlank(startdate))
	 		{
	 			startdate = startdate+ " 00:00:00";
	 			dateSql=String.format(" and act_hi_taskinst.START_TIME_>='%s' ", startdate);
	 		}
	 		if(StringUtil.isNotBlank(todate))
	 		{
	 			dateSql=dateSql+String.format(" and act_hi_taskinst.END_TIME_<='%s' ", todate);
	 			todate=todate+" 23:59:59";
	 		}
	 		return dateSql;
	 	}
	 	
	 	/**
		  * ͳ��
		  * @param type���뷽ʽ (�����ʵ����ʽ)��0����1ʵ��
		  * @param person//���ź���Ա��ʽ��0��Ա��1��λ
		  * @param startdate��ʼ����
		  * @param todate��������
		  * @return
		  */
	 	 public  List<ProcessStatisticalsModel> queryProcessStatisticals(String type,String person,String startdate,String todate){
	 		 List<ProcessStatisticalsModel> list = new ArrayList();
	 		 String[] sqls=getStatisticalsSql(type, person,startdate,todate);
	 		 String querySql=sqls[0];
	 		 DBManager db=new DBManager();
	 		 ResultSet rs=null; 
	 		 try {
	 			rs = db.executeQuery(querySql);
	 		 	ResultSetHandler<List<ProcessStatisticalsModel>> rsh = new BeanListHandler<ProcessStatisticalsModel>(ProcessStatisticalsModel.class);
	 			list = rsh.handle(rs);
	 		} catch (SQLException e) {
	 			logger.error("processStatisticals--ִ�г���", e);
	 		}
	 		finally
	 		{
	 				try {
	 					if(rs!=null)	rs.close();
	 				} catch (SQLException e) {
	 					e.printStackTrace();
	 				}
	 			db.close();
	 		}
	 		 return list;
	 	    }
	  
	 	public JspPage queryProcessStatisticalsDetail(String exectname,String type,String person,String startdate,String todate,int curpage, int perpage){
			DBManager dbm = new DBManager();
			List list = new ArrayList();
			JspPage jspPage = null;
			String[] sql=getStatisticalsDetailSql(exectname,type, person,startdate,todate);
			int rowcount = 0;
			ResultSet rs = null;
			try {
				rowcount =dbm.executeQueryCount(sql[1]);
			 	jspPage = new JspPage(perpage, curpage, rowcount);
				rs = dbm.executeQueryFromAll(sql[0], jspPage.getMinNum(), perpage);
				ResultSetHandler<List<ProcessTaskModel>> rsh = new BeanListHandler<ProcessTaskModel>(ProcessTaskModel.class);
				list = rsh.handle(rs);
				jspPage.setList(list);
			} catch (SQLException e) {
				SysLogger.error("queryProcessStatisticalsDetail--ִ��ʧ��", e);
			} finally {
				if(null!=rs) {
					try {
						rs.close();
					} catch (SQLException e) {
					}
				}
				dbm.close();
			}
			return jspPage;
		}
	 	
	 	public String[] getStatisticalsDetailSql(String exectname,String type,String person,String startdate,String todate)
	 	{
	 		String[] sqls=new String[2];
	 		if(StringUtil.isBlank(person)) person="0";
	 		if(StringUtil.isBlank(type)) type="0";
	 		if(person.equals("0") && type.equals("0"))
	 		{
	 			sqls[0]="select act_hi_taskinst.NAME_ as name,act_hi_taskinst.START_TIME_ as starttime," +
	 					"act_hi_taskinst.END_TIME_ as endtime,act_hi_taskinst.ASSIGNEE_ as personname " +
	 					"from act_hi_taskinst where 1=1 ";
	 			if(StringUtil.isNotBlank(exectname)) sqls[0]=sqls[0]+" and act_hi_taskinst.ASSIGNEE_='"+exectname+"'";
	 		}
	 		else if (person.equals("0") && type.equals("1")) 
	 		{
	 			sqls[0]="select distinct act_hi_procinst.PROC_INST_ID_ as name," +
	 					"act_hi_procinst.START_TIME_ as starttime ,act_hi_procinst.END_TIME_ as endtime," +
	 					"act_hi_taskinst.assignee_ as personname " +
		 				"from act_hi_procinst, act_hi_taskinst  " +
		 				"where act_hi_taskinst.PROC_INST_ID_=act_hi_procinst.PROC_INST_ID_" ; 
	 			if(StringUtil.isNotBlank(exectname)) sqls[0]=sqls[0]+" and act_hi_taskinst.ASSIGNEE_='"+exectname+"'";
			}
	 		else if (person.equals("1") && type.equals("0")) 
	 		{
	 			sqls[0]="select act_hi_taskinst.NAME_ as name,act_id_group.id_ as groupid,act_id_group.name_ as personname, " +
	 					"act_hi_taskinst.START_TIME_ as starttime ,act_hi_taskinst.END_TIME_ as endtime " +
	 					"from act_hi_taskinst,act_id_membership,act_id_group " +
	 					"where act_hi_taskinst.ASSIGNEE_ =act_id_membership.user_id_ and act_id_membership.group_id_=act_id_group.id_ " ;
	 			if(StringUtil.isNotBlank(exectname)) sqls[0]=sqls[0]+" and act_id_group.id_='"+exectname+"'";
			}
	 		else
	 		{
	 			sqls[0]="select distinct act_hi_procinst.PROC_INST_ID_ as name ,act_hi_taskinst.assignee_,act_id_membership.group_id_,act_id_group.name_ as personname, " +
	 					"act_hi_procinst.START_TIME_ as starttime ,act_hi_procinst.END_TIME_ as endtime " +
	 					"from act_hi_procinst, act_hi_taskinst,act_id_membership,act_id_group  " +
	 					"where act_hi_taskinst.PROC_INST_ID_=act_hi_procinst.PROC_INST_ID_  and act_hi_taskinst.assignee_=act_id_membership.user_id_ " +
	 					"and act_id_membership.group_id_=act_id_group.id_" ;
	 			if(StringUtil.isNotBlank(exectname)) sqls[0]=sqls[0]+" and act_id_group.id_='"+exectname+"'";
	 		}
	 		if(StringUtil.isNotBlank(getDateSql(startdate, todate))) sqls[0]=sqls[0]+getDateSql(startdate, todate);
	 		sqls[1]="select count(*) from ("+sqls[0]+") as temp_table11";
	 		return sqls;
	 	}
	 	

	 	/**
	 	 * ���ø澯�б����ɹ�����ʶ��0δ���ɣ�1����
	 	 * @param enentid���澯ID
	 	 */
	 	public void setEventOrderFlag(String eventid)
	 	{
	 		DBManager dbm=new DBManager();
	 		String sql=String.format("update system_eventlist set isorder=1 where id=%d", Integer.parseInt(eventid));
	 		dbm.executeUpdate(sql);
	 		dbm.close();
	 	}
	 	
	 	public String drawBar(List<ProcessStatisticalsModel> list,String person)
	 	{
	 		String imgname="process_statisticals_bar";
	 		ChartGraph cg = new ChartGraph();
	 		int size=list.size();
	 		String c[]= new String[size];
	 		double[][] d= new double[1][size];
	 		String[] titles={"����ͳ��"};
	 		for(int i=0;i<size;i++)
	 		{
	 			ProcessStatisticalsModel model=list.get(i);
	 			if(StringUtil.isBlank(person) || person.equals("0"))
	 			{
	 				c[i]=model.getAssignee();
	 			}
	 			else
	 			{
	 				c[i]=model.getGroupid();
	 			}
	 			d[0][i]=Double.parseDouble(model.getTotal());
	 		}
	 		CategoryDataset dataset = DatasetUtilities.createCategoryDataset(titles,c,d);
	 		cg.zhu("",imgname,dataset,480,250);
	 		return imgname;
	 	}
	 	
	 	public String drawPie(List<ProcessStatisticalsModel> list,String person)
	 	{
	 		String imgname="process_statisticals_pie";
	 		ChartGraph cg = new ChartGraph();
	 		DefaultPieDataset dfp = new DefaultPieDataset();  
	 		if(StringUtil.isBlank(person) || person.equals("0"))
	 		{
	 			for(ProcessStatisticalsModel model:list)
	 			{
	 				dfp.setValue(model.getAssignee(), Double.parseDouble(model.getTotal()));
	 			}
	 		}
	 		else
	 		{
	 			for(ProcessStatisticalsModel model:list)
	 			{
	 				dfp.setValue(model.getGroupid(), Double.parseDouble(model.getTotal()));
	 			}
	 		}
	 		cg.pie("", dfp, imgname, 480, 250);
	 		return imgname;
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
	 	 
	 	 /**
	      * �������
	      * @param querySql
	      * @return
	      */
	     public List<Knowledgebase> queryKnowledge(String querySql){
	 		DBManager dbm = new DBManager();
	 		List<Knowledgebase> list = new ArrayList<Knowledgebase>();
	 		ResultSet rs = null;
	 		try {
	 			rs = dbm.executeQuery(querySql);
	 			ResultSetHandler<List<Knowledgebase>> rsh = new BeanListHandler<Knowledgebase>(Knowledgebase.class);
	 			list = rsh.handle(rs);
	 		} catch (SQLException e) {
	 			SysLogger.error("queryKnowledge--ִ��ʧ��", e);
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
	     
	     public List<String> getListcontent()
	 	 {
	    	// DBManager dbm = new DBManager();
	    	 List<String> list = new ArrayList<String>();
	    	 return list;
	 	 }
	     
	     /**
	      * ͳ��pieͼ
	      * @param list
	      * @return
	      */
	     public String getPieXml(List<ProcessStatisticalsModel> list,String person)
	     {
	    	StringBuffer sb=new StringBuffer();
	    	ProcessStatisticalsModel model=null;
	    	int k=0;
	    	if(StringUtil.isBlank(person) || person.equals("0")) 
	    	{
	    		for(int i=0;i<list.size();i++)
		 		{
		 			model=list.get(i);
		 			if(StringUtil.isBlank(model.getAssignee())) sb.append("δǩ��");
		 			else sb.append(model.getAssignee());
		 			sb.append(";").append(model.getTotal()).append(";false;").append(COLORS[k]).append("\\n");
		 			k++;
		 			if(k>=list.size()) k=0;
		 		}
	    	}
	    	else {
	    		for(int i=0;i<list.size();i++)
		 		{
		 			model=list.get(i);
		 			sb.append(model.getGroupid()).append(";").append(model.getTotal()).append(";false;").append(COLORS[k]).append("\\n");
		 			k++;
		 			if(k>=list.size()) k=0;
		 		}
			}
	 		return sb.toString();
	     }
	     
	     public String getBarXml(List<ProcessStatisticalsModel> list,String person)
	     {
	    	StringBuffer sb=new StringBuffer();
	    	sb.append("<?xml version='1.0' encoding='gb2312'?>");
	    	sb.append("<chart><series>");
	 		ProcessStatisticalsModel model=null;
	 		if(StringUtil.isBlank(person) || person.equals("0")) 
	 		{
	 			for(int i=0;i<list.size();i++){
	 				model=list.get(i);
	 				sb.append("<value xid='").append(i).append("'>");
	 				if(StringUtil.isBlank(model.getAssignee())) sb.append("δǩ��");
		 			else sb.append(model.getAssignee());
	 				sb.append("</value>");
		 		}
	 		}
	 		else {
	 			for(int i=0;i<list.size();i++){
	 				model=list.get(i);
	 				sb.append("<value xid='").append(i).append("'>").append(model.getGroupid()).append("</value>");
		 		}
			}
	 		sb.append("</series><graphs>");
	 		sb.append("<graph gid='0'>");
	 		int k=0;
	 		for(int i=0;i<list.size();i++){
	 			model=list.get(i);
	 			sb.append("<value xid='").append(i).append("' color='").append(COLORS[k]).append("'>").append(model.getTotal()).append("</value>");
	 			k++;
	 			if(k>=list.size()) k=0;
	 		}
	 		sb.append("</graph>");
	 		sb.append("</graphs></chart>");
	 		return sb.toString();
	     }
	     
	     /**
	      * ��������
	      * @param list
	      * @return
	      */
	     public List<String> findContentList(List list)
	     {
	    	 List<String> contentList=new ArrayList<String>();
	    	 DBManager db=new DBManager();
			 ResultSet rs=null; 
			 String sql=null;
			 Task task=null;
			 String content=null;
	    	 try {
	    		 for(int i=0;i<list.size();i++)
	    		 {
	    	    	task=(Task)list.get(i);
	    	    	sql=String.format("select distinct text_ from act_ru_variable " +
	    	    			"where PROC_INST_ID_='%s' and (name_='content' || name_='ordercontent')", task.getProcessInstanceId());
	    	    	rs=db.executeQuery(sql);
					if(rs.next())
					{
						content=rs.getString(1);
						rs.close();
					}
					if(StringUtil.isNotBlank(content)) contentList.add(content);
					else contentList.add("");
					content="";
	    	    }
				} catch (SQLException e) {
					e.printStackTrace();
				}
				 finally
			 		{
			 				try {
			 					if(rs!=null)	rs.close();
			 				} catch (SQLException e) {
			 					e.printStackTrace();
			 				}
			 			db.close();
			 		}
	 		 return contentList;
	 	 }
	     
	     /**
	   	 * ͳ������
	   	 * @param type
	   	 * @return
	   	 */
	   	 public  List<ProcessStatisticalsModel> queryProInsStatDetail(String type)
	   	 {
			 List<ProcessStatisticalsModel> list = new ArrayList<ProcessStatisticalsModel>();
			 String sql=getStatisticalsSql(type);
			 DBManager db=new DBManager();
			 ResultSet rs=null; 
			 try {
				rs=db.executeQuery(sql);
			 	ResultSetHandler<List<ProcessStatisticalsModel>> rsh = new BeanListHandler<ProcessStatisticalsModel>(ProcessStatisticalsModel.class);
				list = rsh.handle(rs);
			} catch (SQLException e) {
				logger.error("queryProInsStatDetail--ִ�г���", e);
			}
			finally
			{
					try {
						if(rs!=null)	rs.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				db.close();
			}
	   		 return list;
	   	 }
	   	 
	   	 /**
	   	  * ͳ��sql���
	   	  * @param type
	   	  * @return
	   	  */
	   	public String getStatisticalsSql(String type)
	   	{
	   		String sql=null;
	   		if(type.equals("1"))
	   		{
	   			sql="select count(*) as total,assignee_ as assignee from act_ru_task  where assignee_ is NOT NULL group by assignee_";
	   		}
	   		else if(type.equals("2"))
	   		{
	   			sql="select count(*) as total,a.group_id_ as assignee  " +
	   					"from act_ru_identitylink a,act_ru_task b " +
	   					"where a.task_id_=b.id_ and b.assignee_ is NULL group by a.group_id_";
	   		}
	   		else if(type.equals("3"))
	   		{
	   			String[] endTime=getprocessTime();
	   			sql=String.format("select count(*) as total,start_user_id_ as assignee from act_hi_procinst " +
					"where status='%s' and end_time_>='%s' and end_time_<='%s' group by start_user_id_ ",
					ConstanceUtil.PRO_END,endTime[0],endTime[1]);//�����������������
	   		}
	   		else 
	   		{
	   			String[] endTime=getprocessTime();
	   			sql=String.format("select count(*) as total,start_user_id_ as assignee from (select distinct a.proc_inst_id_,b.start_user_id_ " +
						"from act_hi_taskinst as a, act_hi_procinst as b where a.proc_inst_id_=b.proc_inst_id_  " +
						"and  b.status!='%s' and a.end_time_>='%s' and a.end_time_<='%s'  ) as temp  group by start_user_id_ ",
						ConstanceUtil.PRO_END,endTime[0],endTime[1]);//�����Ѱ죬δ�����������
			}
	   		return sql;
	   	}
	   	 
	   	/**
	      * ͳ��pieͼ
	      * @param list
	      * @return
	      */
	     public String getProInsPieXml(List<ProcessStatisticalsModel> list)
	     {
	    	StringBuffer sb=new StringBuffer();
	    	ProcessStatisticalsModel model=null;
	    	int k=COLORS.length-1;
	    	for(int i=0;i<list.size();i++)
		 		{
		 			model=list.get(i);
		 			sb.append(model.getAssignee()).append(";").append(model.getTotal()).append(";false;").append(COLORS[k]).append("\\n");
		 			k--;
		 			if(k<=0) k=COLORS.length-1;
		 		}
	 		return sb.toString();
	     }
	     
	     public String getProInsBarXml(List<ProcessStatisticalsModel> list)
	     {
	    	StringBuffer sb=new StringBuffer();
	    	sb.append("<?xml version='1.0' encoding='gb2312'?>");
	    	sb.append("<chart><series>");
	 		ProcessStatisticalsModel model=null;
	 		for(int i=0;i<list.size();i++)
	 		{
	 			model=list.get(i);
	 			sb.append("<value xid='").append(i).append("'>").append(model.getAssignee()).append("</value>");
		 	}
	 		sb.append("</series><graphs>");
	 		sb.append("<graph gid='0'>");
	 		int k=0;
	 		for(int i=0;i<list.size();i++)
	 		{
	 			model=list.get(i);
	 			sb.append("<value xid='").append(i).append("' color='").append(COLORS[k]).append("'>").append(model.getTotal()).append("</value>");
	 			k++;
	 			if(k>=list.size()) k=0;
	 		}
	 		sb.append("</graph>");
	 		sb.append("</graphs></chart>");
	 		return sb.toString();
	     }
	     

		 
		 public String[] getprocessTime()
		 {
			String[] endTime=new String[2];
			Calendar cal = Calendar.getInstance();
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY); // ��ȡ����һ������
			endTime[0]=df.format(cal.getTime())+" 00:00:00";
			cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
			cal.add(Calendar.WEEK_OF_YEAR, 1);//��������
			endTime[1]=df.format(cal.getTime())+" 23:59:59";
		    return endTime;
		 }
		 
		 /**
		  * ����ͳ��
		  * @return
		  */
		 public HashMap<ProcessEnum, String> queryProIns()
	   	 {
			 HashMap<ProcessEnum, String> map=new HashMap<ProcessEnum, String>();
			 DBManager db=new DBManager();
			 ResultSet rs=null; 
			 try {
				String sql="select count(*) as total from act_ru_task  where assignee_ is NOT NULL";//��ǰ��ǩ�գ�����������
				rs=db.executeQuery(sql);
				if(rs.next())
				 {
					 map.put(ProcessEnum.CLAIMTASK, rs.getString(1));
					 rs.close();
				 }
				sql="select count(*) as total  " +
					"from act_ru_identitylink a,act_ru_task b " +
					"where a.task_id_=b.id_ and b.assignee_ is NULL";//��ǰ��ǩ������
				rs=db.executeQuery(sql);
				if(rs.next())
				 {
					 map.put(ProcessEnum.UNCLAIMTASK, rs.getString(1));
					 rs.close();
				 }
				String[] endTime=getprocessTime();
		   		sql=String.format("select count(*) as total from act_hi_procinst " +
						"where status='%s' and end_time_>='%s' and end_time_<='%s' ",
						ConstanceUtil.PRO_END,endTime[0],endTime[1]);//�����������������
				rs=db.executeQuery(sql);
				if(rs.next())
				 {
					 map.put(ProcessEnum.FINISHEDPRO, rs.getString(1));
					 rs.close();
				 }
				sql=String.format("select count(*) from (select distinct a.proc_inst_id_ " +
						"from act_hi_taskinst as a, act_hi_procinst as b where a.proc_inst_id_=b.proc_inst_id_  " +
						"and  b.status!='%s' and a.end_time_>='%s' and a.end_time_<='%s'  ) as temp ",
						ConstanceUtil.PRO_END,endTime[0],endTime[1]);//�����Ѱ죬δ�����������
				rs=db.executeQuery(sql);
				if(rs.next())
				 {
					 map.put(ProcessEnum.UNFINISHEDPRO, rs.getString(1));
					 rs.close();
				 }
			} catch (SQLException e) {
				logger.error("queryProIns--ִ�г���", e);
			}
			finally{
				db.close();
			}
	   		 return map;
	   	 }
	     
		 public String pieXmlProIns(HashMap<ProcessEnum, String> map)
		 {
			 StringBuffer sb=new StringBuffer();
			 int k=COLORS.length-1;
		     for(ProcessEnum key:map.keySet())
		     {
		    	 sb.append(key.decp).append(";").append(map.get(key)).append(";false;").append(COLORS[k]).append("\\n");
		    	 k--;
		 		 if(k<=0) k=COLORS.length-1;
		     }
		 	return sb.toString();
		 }
		 public String barXmllProIns(HashMap<ProcessEnum, String> map)
		 {
			 StringBuffer sb=new StringBuffer();
		     sb.append("<?xml version='1.0' encoding='gb2312'?>");
		     sb.append("<chart><series>");
		 	 int i=0;
		 	 for(ProcessEnum key:map.keySet())
			    {
		 			sb.append("<value xid='").append(i).append("'>");
		 			sb.append(key.decp);
		 			sb.append("</value>");
		 			i++;
			     }
		 		sb.append("</series><graphs>");
		 		sb.append("<graph gid='0'>");
		 		i=0;
		 		int k=0;
		 		for(ProcessEnum key:map.keySet())
		 		{
		 			sb.append("<value xid='").append(i).append("' color='").append(COLORS[k]).append("'>").append(map.get(key)).append("</value>");
		 			i++;
		 			k++;
		 			if(k>=map.size()) k=0;
		 		}
		 		sb.append("</graph>");
		 		sb.append("</graphs></chart>");
		 		return sb.toString();
		 }
}
