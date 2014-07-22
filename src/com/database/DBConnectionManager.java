package com.database;

import java.sql.*;

import com.database.config.*;
import com.mchange.v2.c3p0.*;


/**
 * 
 * 
 * ���ݿ⽨������ʵ����
 * 
 * @author itims
 *
 */

public class DBConnectionManager {
	
	private static ComboPooledDataSource cpds = null;

	private static void init() {
		
		if(cpds==null)
		{
		// �������ݿ����ӳ�
		String DRIVER_NAME = SystemConfig.getConfigInfomation("SystemConfigResources","DRIVER_NAME"); // ������
		String DATABASE_URL = SystemConfig.getConfigInfomation("SystemConfigResources","DATABASE_URL"); // ���ݿ�����url
		String DATABASE_USER = SystemConfig
				.getConfigInfomation("SystemConfigResources","DATABASE_USER"); // ���ݿ��û���
		String DATABASE_PASSWORD = SystemConfig
				.getConfigInfomation("SystemConfigResources","DATABASE_PASSWORD"); // ���ݿ�����
		
		int Min_PoolSize = 5;
		int Max_PoolSize = 50;
		int Acquire_Increment = 5;
		int Initial_PoolSize = 10;
		int Idle_Test_Period = 3000;// ÿ��3000s���������Ƿ��������ʹ��
		int MAX_IdleTime=60;
		int Timeout=10000;//
		int CheckoutTimeout=3000;
		int NumHelperThreads=3;
		
		
		String Validate = SystemConfig.getConfigInfomation("SystemConfigResources","Validate");// ÿ��������֤�����Ƿ����
		if (Validate.equals("")) {
			Validate = "false";
		}
		// ��С������
		try {
			Min_PoolSize = Integer.parseInt(SystemConfig
					.getConfigInfomation("SystemConfigResources","Min_PoolSize"));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		// ��������
		try {
			Acquire_Increment = Integer.parseInt(SystemConfig
					.getConfigInfomation("SystemConfigResources","Acquire_Increment"));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		// ���������
		try {
			Max_PoolSize = Integer.parseInt(SystemConfig
					.getConfigInfomation("SystemConfigResources","Max_PoolSize"));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		// ��ʼ��������
		try {
			Initial_PoolSize = Integer.parseInt(SystemConfig
					.getConfigInfomation("SystemConfigResources","Initial_PoolSize"));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		// ÿ��3000s���������Ƿ��������ʹ��
		try {
			Idle_Test_Period = Integer.parseInt(SystemConfig
					.getConfigInfomation("SystemConfigResources","Idle_Test_Period"));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		
		// 
		try {
			MAX_IdleTime = Integer.parseInt(SystemConfig
					.getConfigInfomation("SystemConfigResources","MAX_IdleTime"));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		try {
			CheckoutTimeout = Integer.parseInt(SystemConfig
					.getConfigInfomation("SystemConfigResources","CheckoutTimeout"));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		try {
			NumHelperThreads = Integer.parseInt(SystemConfig
					.getConfigInfomation("SystemConfigResources","NumHelperThreads"));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		

		try {
			cpds = new ComboPooledDataSource();
			cpds.setDriverClass(DRIVER_NAME); // ������
			cpds.setJdbcUrl(DATABASE_URL); // ���ݿ�url
			cpds.setUser(DATABASE_USER); // �û���
			cpds.setPassword(DATABASE_PASSWORD); // ����
			cpds.setInitialPoolSize(Initial_PoolSize); // ��ʼ�����ӳش�С
			cpds.setMinPoolSize(Min_PoolSize); // ����������
			cpds.setMaxPoolSize(Max_PoolSize); // ���������
			cpds.setAcquireIncrement(Acquire_Increment); // ������������
			cpds.setIdleConnectionTestPeriod(Idle_Test_Period); //��������Ч��ʱ����
			cpds.setTestConnectionOnCheckout(Boolean.getBoolean(Validate)); // ÿ��������֤�����Ƿ����
			cpds.setMaxIdleTime(MAX_IdleTime);
			cpds.setCheckoutTimeout(CheckoutTimeout);
			cpds.setAutoCommitOnClose(true);
			cpds.setNumHelperThreads(NumHelperThreads);
			
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		}
		
	}

	

	/**
	 * ��ȡ�������ӳ�
	 * @return
	 */
    public static  Connection getConnection() {    
        Connection con = null;    
        try {  
        	if (cpds == null) {
				init();
			}
        	
            con = cpds.getConnection();    
        } catch (SQLException e1) {    
            e1.printStackTrace();    
        }    
        return con;    
    }  
	
	
	
	
	/**
	 * 
	 * �ر����ݿ����ӣ��ر�����Դ��
	 * 
	 */
	private static void release() {
		try {
			if (cpds != null) {
				cpds.close();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public static void main(String arg[]) throws SQLException
	{
		
		DBConnectionManager db=new DBConnectionManager();
        db.getConnection();
        String SQL="select * from system_user";
        Connection con = null;    
        con= db.getConnection();
        //PreparedStatement pstmt = null;    
        //pstmt = con.prepareStatement(sql);    
        Statement stmt=con.createStatement();
        ResultSet rs=stmt.executeQuery(SQL);//���ݿ��ѯ
        boolean flag=false;
        while(rs.next()) {    
            // ����м�¼����ִ�д˶δ���    
            // �û��ǺϷ��ģ����Ե�½    
            flag = true;    
            
           }    
        db.release();


		
	}
	
	
	
}