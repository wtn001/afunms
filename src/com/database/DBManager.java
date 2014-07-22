package com.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Queue;

import org.apache.log4j.Logger;
import java.util.Hashtable;

/**
 * 
 * @author konglq
 * @�������� 2010-8-24 ����01:05:06
 * @���� ʵ���������ݿ�Ĺ��ܣ����ṩ���ַ���������List��HashMap
 */
public class DBManager {
	private static final Logger logger = Logger.getLogger(DBManager.class);
	private Connection conn = null;// �������Ӷ���
	private Statement stmt = null;
	private ResultSet rs = null;// �����

	private DBConnectionManager mg = new DBConnectionManager();// ���ݿ�������

	/**
	 * ����Ĭ��ϵͳ���ݿ��ǿ����������ӵģ����Բ��׳�����
	 */
	public DBManager() {
		logger
				.info("����connection11111111111111111111111111111111111111111111111");
		try {
			init();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Can not connect system DB!", e);
		}
	}

	/**
	 * 
	 * @author konglq
	 * @����ʱ�� 2010-8-24 ����01:03:11
	 * @������  init void
	 * @�������� ��ʼ�����ӳ����Ӻ�Statement
	 */
	private void init() {

		try {
			conn = mg.getConnection();
			stmt = conn.createStatement();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * @author konglq
	 * @����ʱ�� 2010-8-24 ����01:02:56
	 * @������  executeQuery
	 * @param sql
	 * @return ResultSet
	 * @�������� ִ�в�ѯ������ResultSet
	 */
	public ResultSet executeQuery(String sql) {
		try {
			if (conn == null) {
				conn = mg.getConnection();
				stmt = conn.createStatement();
			}
			logger.info("ִ��sql�������" + sql);
			rs = stmt.executeQuery(sql);
		} catch (SQLException se) {
			se.printStackTrace();
			logger.error("Error in DBManager.executeQuery(),SQL=\n" + sql);
		}

		return rs;
	}

	/**
	 * 
	 * @author konglq
	 * @����ʱ�� 2010-8-24 ����01:02:42
	 * @������  executeUpdate
	 * @param sql
	 * @return int
	 * @�������� ���º�Ĭ���ύ
	 */
	public int executeUpdate(String sql) {
		int iresult = -1;
		iresult = executeUpdate(sql, true);
		return iresult;
	}

	/**
	 * 
	 * @author konglq
	 * @����ʱ�� 2010-8-24 ����01:02:31
	 * @������  executeUpdate
	 * @param sql
	 * @param bCommit
	 * @return int
	 * @�������� ���£�����bCommit�������Ƿ�ֱ���ύ
	 */
	private int executeUpdate(String sql, boolean bCommit) {
		int iresult = -1;
		try {
			if (conn == null) {
				conn = mg.getConnection();
				stmt = conn.createStatement();
			}
			logger.info("ִ��sql�������" + sql);
			iresult = stmt.executeUpdate(sql);
			if (bCommit) {
				conn.commit();
			}
			return iresult;
		} catch (SQLException se) {
			se.printStackTrace();
			logger.error("DBManager.executeUpdate():�������ݳ���:\n" + sql, se);
			try {
				conn.rollback();
				return iresult;
			} catch (Exception sqle) {
				return iresult;
			}

		}

	}

	/**
	 * 
	 * @author konglq
	 * @����ʱ�� 2010-8-24 ����01:02:19
	 * @������  commit void
	 * @�������� �ύ
	 */
	private void commit() {
		try {
			conn.commit();
		} catch (SQLException se) {
			logger.error("DBManager.commit()���ִ���:", se);
		}
	}

	/**
	 * 
	 * @author konglq
	 * @����ʱ�� 2010-8-24 ����01:02:10
	 * @������  rollback void
	 * @�������� �ع�
	 */
	private void rollback() {
		try {
			conn.rollback();
		} catch (SQLException se) {
			logger.error("DBManager.rollback()���ִ���:", se);
		}
	}

	/**
	 * 
	 * @author konglq
	 * @����ʱ�� 2010-8-24 ����01:01:58
	 * @������  addBatch
	 * @param sql void
	 * @�������� ����������
	 */
	public void addBatch(String sql) {
		try {

			if (null == conn) {
				conn = mg.getConnection();
				stmt = conn.createStatement();
			}
			//logger.info("������������sql�������==" + sql);
			stmt.addBatch(sql);
		} catch (SQLException se) {
			logger.error("DBManager.addBatch() ���ִ���!" + sql, se);
		}
	}

	/**
	 * 
	 * @author konglq
	 * @����ʱ�� 2010-8-24 ����01:01:44
	 * @������  executeBatch
	 * @return int[]
	 * @�������� ִ��������
	 */
	public int[] executeBatch() {
		int[] intlist = null;
		try {
			if (null == conn) {
				conn = mg.getConnection();
				stmt = conn.createStatement();
				conn.setAutoCommit(false);
			}
			conn.setAutoCommit(false);
			intlist = stmt.executeBatch();
			conn.commit();
			conn.setAutoCommit(true);
			return intlist;
		} catch (BatchUpdateException bse) {
			logger.error("������һ��SQL������", bse);
			return intlist;
		} catch (SQLException se) {
			logger.error("Error in DBManager.executeBatch()!", se);
			return intlist;
		} finally {
			try {
				stmt.clearBatch();
				conn.setAutoCommit(true);
				return intlist;
			} catch (SQLException xe) {
				return intlist;
			}
		}
	}

	/**
	 * 
	 * @author konglq
	 * @����ʱ�� 2010-8-24 ����01:01:27
	 * @������  close void
	 * @�������� �ر�����
	 */
	public void close() {
		try {
			if (rs != null)
				rs.close();
			if (stmt != null)
				stmt.close();
			if (conn != null && !conn.isClosed()) {
				logger
						.info("�ر�connection2222222222222222222222222222222222222222222222");
				conn.close();
			}
		} catch (SQLException se) {
			se.printStackTrace();
			logger.error("Error in DBManager.close()!", se);
		}
	}

	/**
	 * 
	 * @author konglq
	 * @����ʱ�� 2010-8-24 ����01:01:11
	 * @������  getNextID
	 * @return int
	 * @�������� ��ȡΨһid
	 */
	public synchronized int getNextID() {
		String sql = "SELECT SYS_BACK_SQ.NEXTVAL FROM DUAL";

		int max = -1;
		try {

			if (null == conn) {
				conn = mg.getConnection();
				stmt = conn.createStatement();
			}
			logger.info("ִ��sql���£�" + sql);
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				max = rs.getInt(1);
			}
		} catch (Exception se) {
			logger.error("ִ��sql�������⣬sql���£�" + sql + se);
			return max;
		}

		return max;
	}

	/**
	 * 
	 * @author konglq
	 * @����ʱ�� 2010-8-24 ����12:39:28
	 * @������ executeQueryOne
	 * @param sql
	 * @return
	 * @throws SQLException
	 *             Hashtable
	 * @�������� ִ�е�����ѯ��sql�����뱣ֻ֤����һ����¼�� ���ڸ���ʱ��ȡ������¼��Ϣ
	 */
	public HashMap executeQueryHashMap(String sql) throws SQLException {
		HashMap hm = new HashMap();
		try {
			logger.info("ִ��sql���£�" + sql);
			rs = executeQuery(sql);
			// ȡ������,
			ResultSetMetaData rsmd = rs.getMetaData();
			int numCols = rsmd.getColumnCount();
			while (rs.next()) {
				for (int i = 1; i <= numCols; i++) {
					String key = rsmd.getColumnName(i);
					String value = rs.getString(i);
					if (value == null) {
						value = "";
					}
					hm.put(key, value);
				}
			}
		} catch (SQLException e) {
			logger.error("ִ��sql�������⣺" + sql + e);
			return null;

		}
		return hm;
	}

	/**
	 * 
	 * @author konglq
	 * @����ʱ�� 2010-8-24 ����12:58:26
	 * @������  executeQueryListHashMap
	 * @param sql
	 * @param indexkey ����һ����¼��Ψһkey
	 * @return
	 * @throws SQLException List
	 * @�������� ִ�����ݿ��ѯ��䷵���������͵�����,������ÿһ��ֵ�ǹ�ϣ�� 
	 * ÿ����ϣ�����һ����¼
	 */
	public Hashtable executeQuerykeyoneListHashMap(String sql, String indexkey)
			throws SQLException {
		ResultSetMetaData rsmd = null;
		Hashtable list = new Hashtable();
		int columnCount = 0;
		try {
			logger.info("ִ��sql���£�" + sql);
			rs = executeQuery(sql);
			if (rs == null) {
				return null;
			}
			rsmd = rs.getMetaData();
			if (rsmd == null) {
				return null;
			}
			columnCount = rsmd.getColumnCount(); // �õ��ֶ�����
			if (columnCount == 0) {
				return null;
			}

			// �����ĵ�һ��ֵ����������
			String[] keys = new String[columnCount];
			for (int i = 1; i <= columnCount; i++) {
				keys[i - 1] = rsmd.getColumnName(i); // ����ֶ���
			}
			// vct.add(keys);
			while (rs.next()) {
				Hashtable hm = new Hashtable();
				hm.clear();
				String key = null;
				for (int i = 1; i <= columnCount; i++) {
					String result = rs.getString(i);
					if ((result == null) || (result.length() == 0)) {
						result = "";
					}

					if (keys[i - 1].equals(indexkey)) {
						key = result;
					}

					hm.put(keys[i - 1], result); // ��ÿ����¼���浽һ����ϣ���У�keyΪ�ֶ�����resultΪֵ
				}
				list.put(key, hm); // �����ݼ���ÿһ�в�������
			}
		} catch (SQLException e) {
			logger.error("ִ��sql�������⣺" + sql + e);
			return null;
		}
		return list; // ����SQL���ԵĲ�ѯ�������
	}

	/**
	 * 
	 * ͨ�� onekye��twokey��ϳ�һ��Ψһ��key���������ݿ�Ĳ�ѯ���
	 * �ŵ�hashtable ��;onekey-towkey Ϊ����
	 * @author konglq
	 * @����ʱ�� 2010-8-24 ����12:58:26
	 * @������  executeQueryListHashMap
	 * @param sql
	 * @param onekey ����һ����¼��Ψһkey
	 * @param twokey �ڶ���key
	 * @return
	 * @throws SQLException List
	 * @�������� ִ�����ݿ��ѯ��䷵���������͵�����,������ÿһ��ֵ�ǹ�ϣ�� 
	 * ÿ����ϣ�����һ����¼
	 */
	public Hashtable executeQuerykeytwoListHashMap(String sql, String onekey,
			String twokey) throws SQLException {
		ResultSetMetaData rsmd = null;
		Hashtable list = new Hashtable();
		int columnCount = 0;
		try {
			logger.info("ִ��sql���£�" + sql);
			rs = executeQuery(sql);
			if (rs == null) {
				return null;
			}
			rsmd = rs.getMetaData();
			if (rsmd == null) {
				return null;
			}
			columnCount = rsmd.getColumnCount(); // �õ��ֶ�����
			if (columnCount == 0) {
				return null;
			}

			// �����ĵ�һ��ֵ����������
			String[] keys = new String[columnCount];
			for (int i = 1; i <= columnCount; i++) {
				keys[i - 1] = rsmd.getColumnName(i); // ����ֶ���
			}
			// vct.add(keys);
			while (rs.next()) {
				Hashtable hm = new Hashtable();
				hm.clear();
				String key1 = null;
				String key2=null;
				for (int i = 1; i <= columnCount; i++) {
					String result = rs.getString(i);
					if ((result == null) || (result.length() == 0)) {
						result = "";
					}

					if (keys[i - 1].equals(onekey)) {
						key1 = result;
					}
					
					if (keys[i - 1].equals(twokey)) {
						key2 = result;
					}

					hm.put(keys[i - 1], result); // ��ÿ����¼���浽һ����ϣ���У�keyΪ�ֶ�����resultΪֵ
				}
				list.put(key1+"-"+key2, hm); // �����ݼ���ÿһ�в�������
			}
		} catch (SQLException e) {
			logger.error("ִ��sql�������⣺" + sql + e);
			return null;
		}
		return list; // ����SQL���ԵĲ�ѯ�������
	}

	/**
	 * 
	 * @author konglq
	 * @����ʱ�� 2010-8-24 ����12:58:26
	 * @������  executeQueryListHashMap
	 * @param sql
	 * @return
	 * @throws SQLException List
	 * @�������� ִ�����ݿ��ѯ��䷵���������͵�����,������ÿһ��ֵ�ǹ�ϣ�� 
	 * ÿ����ϣ�����һ����¼
	 */
	public List executeQueryListHashMap(String sql) throws SQLException {
		ResultSetMetaData rsmd = null;
		List list = new ArrayList();
		int columnCount = 0;
		try {
			logger.info("ִ��sql���£�" + sql);
			rs = executeQuery(sql);
			if (rs == null) {
				return null;
			}
			rsmd = rs.getMetaData();
			if (rsmd == null) {
				return null;
			}
			columnCount = rsmd.getColumnCount(); // �õ��ֶ�����
			if (columnCount == 0) {
				return null;
			}

			// �����ĵ�һ��ֵ����������
			String[] keys = new String[columnCount];
			for (int i = 1; i <= columnCount; i++) {
				keys[i - 1] = rsmd.getColumnName(i); // ����ֶ���
			}
			// vct.add(keys);
			while (rs.next()) {
				HashMap hm = new HashMap();
				hm.clear();
				for (int i = 1; i <= columnCount; i++) {
					String result = rs.getString(i);
					if ((result == null) || (result.length() == 0)) {
						result = "";
					}
					hm.put(keys[i - 1], result); // ��ÿ����¼���浽һ����ϣ���У�keyΪ�ֶ�����resultΪֵ
				}
				list.add(hm); // �����ݼ���ÿһ�в�������
			}
		} catch (SQLException e) {
			logger.error("ִ��sql�������⣺" + sql + e);
			return null;
		}
		return list; // ����SQL���ԵĲ�ѯ�������
	}

	/**
	 * 
	 * @author konglq
	 * @date 2010-8-26 ����12:43:40
	 * @param list
	 *            list��ÿ���ڵ���һ��sql���
	 * @return boolean
	 * @Description: TODO����ִ�и�����sql���(insert delete update)
	 */
	public boolean excuteBatchSql(List<String> list) {
		if (list == null) {
			return false;
		}
		try {
			if (!list.isEmpty()) {
				for (int i = 0; i < list.size(); i++) {
					addBatch(list.get(i));
				}
				int[] iResult = null;
				iResult = executeBatch();
				if (iResult != null) {
					String str = iResult.toString();
					if (str.indexOf(-1) >= 0) {
						return false;
					}
				} else {
					return false;
				}

			}
		} catch (Exception e) {
			logger.error("����ִ��sql��������\n");
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	
	
	
	
	/**
	 * 
	 * @author konglq
	 * @date 2010-8-26 ����12:43:40
	 * @param list
	 *            list��ÿ���ڵ���һ��sql���
	 * @return boolean
	 * @Description: TODO����ִ�и�����sql���(insert delete update)
	 */
	public boolean excuteBatchSql(Queue<String> list) {
		if (list == null) {
			return false;
		}
		
		try{
			
				if (null == conn) {
					conn = mg.getConnection();	
					stmt = conn.createStatement();
				}
				
				conn.setAutoCommit(false);
				//logger.info("������������sql�������==" + sql);
				
			if(list.size()>0)
			{
				       String sql="";
						while((sql=list.poll())!=null)
						{
							//logger.info(sql);
							stmt.addBatch(sql);
						}
							
						stmt.executeBatch();
						conn.commit();
						conn.close();
						stmt.close();
						
						
						//logger.info("=======����������========");
			}		
	
				} catch (Exception e) {
					logger.error("����ִ��sql��������");
					e.printStackTrace();
				}
				
				
				
		return true;
	}

	/**
	 * oracle�л�ȡ���еķ���
	 * @author gaoguangfei
	 * @date 2010-9-2 ����02:29:05
	 * @return int
	 * @Description: TODO(�����ô���userid����)
	 */
	public synchronized int getNextUserID() {
		String sql = "SELECT SYSUSER_SQ.NEXTVAL FROM DUAL";

		int max = -1;
		try {

			if (null == conn) {
				conn = mg.getConnection();
				stmt = conn.createStatement();
			}
			//SysLogger.info();
			logger.info("ִ��sql���£�" + sql);
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				max = rs.getInt(1);
			}
		} catch (Exception se) {
			//SysLogger.error();
			logger.info("ִ��sql�������⣬sql���£�" + sql + se);
			return max;
		}

		return max;
	}

	/**
	 * 
	 * ���Է���
	 * 
	 * @param arg
	 */
	public static void main(String arg[]) {

		DBManager db = new DBManager();
		try {
			System.out.println(db.executeQuerykeytwoListHashMap(
					"select * from nms_alarm_indicators_node", "nodeid","name").toString());

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
