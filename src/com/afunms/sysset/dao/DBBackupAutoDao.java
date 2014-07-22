package com.afunms.sysset.dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.afunms.common.base.BaseDao;
import com.afunms.common.base.BaseVo;
import com.afunms.common.base.DaoInterface;
import com.afunms.common.util.SysLogger;
import com.afunms.initialize.ResourceCenter;
import com.afunms.sysset.model.DBBackup;
import com.afunms.sysset.model.DBBackupAuto;

/**
 * �Զ��������ݿ��ļ�
 * @author HONGLI  Sep 8, 2011
 */
public class DBBackupAutoDao extends BaseDao implements DaoInterface
{
	private String username = "root";   // ����ʱ�õ����ݿ��ʺ�,Ĭ���� root
	private String password = "root";   // ����ʱ�õ�����,Ĭ���� root
	private String database = "afunms"; // ��Ҫ���ݵ����ݿ���
	private String filepath = ResourceCenter.getInstance().getSysPath() + "WEB-INF/dbbackup/"; // ���ݵ�·����ַ
	private String filename = null;			//���ݵ��ļ���
	
	
	public DBBackupAutoDao() {
		super("nms_dbbackup_auto");
	}
	
	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the database
	 */
	public String getDatabase() {
		return database;
	}

	/**
	 * @param database the database to set
	 */
	public void setDatabase(String database) {
		this.database = database;
	}

	/**
	 * @return the filepath
	 */
	public String getFilepath() {
		return filepath;
	}

	/**
	 * @param filepath the filepath to set
	 */
	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}

	/**
	 * @return the filename
	 */
	public String getFilename() {
		return filename;
	}

	/**
	 * @param filename the filename to set
	 */
	public void setFilename(String filename) {
		this.filename = filename;
	}

	// ��ѯ���ݿ������еı���
	public List<String> findByCriteria(String sql) 
	{
		List<String> list = new ArrayList<String>();
		try 
		{
			rs = conn.executeQuery(sql);
			if (rs == null)
				return null;
			while (rs.next()) 
			{
				list.add(rs.getString(1));
			}
		} 
		catch (Exception e)
		{
			list = null;
			SysLogger.error("DBBackupDao.findByCriteria()", e);
		} 
		finally 
		{
			conn.close();
		}
		return list;
	}
	/**
	 * ���ݿⱸ��
	 * @param tables    ��Ҫ���ݵı�����
	 * @param radio     ���ݵ�����
	 * @param filename  �ļ���
	 * @return
	 */ 
	public boolean backup(String[] tables, int radio, String filename) {
		getUserAndPass();
		String option = null; // �������ݿ�����Ĳ���
		boolean result = false;
		if (radio == 0){ // ֻ���ݱ�ṹ
			option = " --opt -d";
		} else if (radio == 1){ // ֻ���ݱ�����
			option = " --opt -t";
		} else{ // ���ݱ�ṹ������
			option = " --opt";
		}
		// ע��mysqldump�ǵ���mysql���ݿ��һ���������δ��ϵͳ�����������Ļ���Ҫ������дmysqldump������·��
		int count = tables.length / 10;
		FileOutputStream fout = null;
		OutputStreamWriter writer = null;
		try {
			File file = new File(filepath); // �ж�·���Ƿ���ڣ����������򴴽�
			if (!file.exists()) {
				file.mkdir();
			}
			fout = new FileOutputStream(filepath + filename + ".sql", true);
			writer = new OutputStreamWriter(fout, "utf8");
			for (int j = 0; j <= count; j++) {
				// �����̫�� ������� mysqldump ���������� ���� mysqldump ������ȷִ�� ��ÿʮ����ִ��һ��
				StringBuffer sb = new StringBuffer();
				sb.append("mysqldump ");
				sb.append(" --add-drop-table ");
				sb.append(database);
				boolean flag = false;//�Ƿ������Ϊ��  
				for (int i = j * 10; i < tables.length && i < j * 10 + 10; i++) {
					sb.append(" ");
					if(tables[i] == null || tables[i].equals("")){
						continue;
					}
					flag = true;
					sb.append(tables[i]);
				}
				if(!flag){//���˵����������У�һ����ֵΪ�յ��ַ�������Ϊ�յ����β�����bug 
					continue;
				}
				sb.append(" -u ").append(username).append(" -p").append(password).append(option);
//				System.out.println(sb.toString());
				Process child = Runtime.getRuntime().exec(sb.toString());
				InputStream in = null;
				InputStreamReader xx = null;
				BufferedReader br = null;
				try {
					String inStr;
					String outStr;
					in = child.getInputStream();// ����̨�������Ϣ��Ϊ������
					xx = new InputStreamReader(in, "utf8");// �������������Ϊutf8�����������utf8����������ж����������
					// ��Ͽ���̨�����Ϣ�ַ���
					br = new BufferedReader(xx);
					while ((inStr = br.readLine()) != null) {
						StringBuffer sbsql = new StringBuffer("");
						sbsql.append(inStr + "\r\n");
						outStr = sbsql.toString();// ���ݳ�����������һ��������
						writer.write(outStr);
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally{
					if(writer != null){
						writer.flush();
					}
					if(in != null){
						in.close();
					}
					if(xx != null){
						xx.close();
					}
					if(br != null){
						br.close();
					}
					if(child != null){
						child.destroy();
					}
				}
			}
			result = true;
		} catch (IOException e) {
			SysLogger.error("DBBackupDao.backup()", e);
			result = false;
		} finally {
			conn.close();
			if(writer != null){
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(fout != null){
				try {
					fout.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}
	
	// ���ݿ��ļ�����
	public boolean load(String filename) { 
		boolean result = false;
        try {  
        	if(filename == null){
        		throw new IOException("�ļ�·��Ϊ��");
        	}
        	File file = new File(filename);
        	if(!file.exists()){
        		throw new IOException("�ļ�������");
        			
        	} 
            Runtime rt = Runtime.getRuntime();  
            System.out.println(filename);
            // ���� mysql �� cmd:  
            Process child = rt.exec("cmd /c mysql -u root -proot afunms " );  
           
            OutputStream out = child.getOutputStream();//����̨��������Ϣ��Ϊ�����  
            String inStr;  
            
            String outStr;  
            BufferedReader br = new BufferedReader(new InputStreamReader(  
                    new FileInputStream(file), "utf8"));
            OutputStreamWriter writer = new OutputStreamWriter(out, "utf8");  
            while ((inStr = br.readLine()) != null) {  
            	StringBuffer sb = new StringBuffer();  
                sb.append(inStr + "\r\n");  
                outStr = sb.toString(); 
                writer.write(outStr); 
                 
            }  
            // ע����������û��巽ʽд���ļ��Ļ����ᵼ���������룬��flush()��������Ա���  
            writer.flush(); 
            // �����ǹر����������  
            writer.close();  
            out.close();  
            br.close();
            
            System.out.println("/* Load OK! */");  
            result = true;
        } catch (Exception e) { 
        	result = false;
            e.printStackTrace();  
        }finally{
			conn.close();
		} 
        return result;
    } 
	
	//��ȡ�����ļ�afunms.xml�е��û���������
	private void getUserAndPass()
	{
		try 
		{
			File file = new File( ResourceCenter.getInstance().getSysPath() + "WEB-INF/classes/afunms.xml");
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(file);
			NodeList nl = doc.getElementsByTagName("parameter");
			for (int i = 0; i < nl.getLength(); i++) 
			{
				if ("username".equals(doc.getElementsByTagName("name").item(i).getFirstChild().getNodeValue()))
				{
					username = doc.getElementsByTagName("value").item(i).getFirstChild().getNodeValue();
					continue;
				}
				if ("password".equals(doc.getElementsByTagName("name").item(i).getFirstChild().getNodeValue())) 
				{
					password = doc.getElementsByTagName("value").item(i).getFirstChild().getNodeValue();
				}
			}
		} 
		catch (Exception e) 
		{
			SysLogger.error("DBBackupDao.getUserAndPass()", e);
		}
	}

	@Override
	public BaseVo loadFromRS(ResultSet rs) {
		// TODO Auto-generated method stub
		DBBackupAuto dbBackupAuto = new DBBackupAuto();		
		try {
			dbBackupAuto.setId(rs.getInt("id"));
			dbBackupAuto.setFilename(rs.getString("filename"));
			dbBackupAuto.setTime(rs.getString("time"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return dbBackupAuto;
	}

	public boolean save(BaseVo vo) {
		// TODO Auto-generated method stub
		DBBackupAuto DBBackupAuto = (DBBackupAuto)vo;
		
        StringBuffer sb = new StringBuffer();
        sb.append("insert into nms_DBBackup_auto(filename,time)values(");
        sb.append("'");
        sb.append(DBBackupAuto.getFilename());
        sb.append("','");
        sb.append(DBBackupAuto.getTime());   
        sb.append("')");	        
        return saveOrUpdate(sb.toString());
	}

	public boolean update(BaseVo vo) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public boolean delete(String id){
		boolean result = false;
		try {
			conn.executeUpdate("delete from nms_DBBackup_auto where id=" + id);
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		} finally{
			conn.close();
		}
		return result;
	}
}
