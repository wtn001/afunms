/**
 * <p>Description:DBManager,used to connect database</p>
 * <p>Company: dhcc.com</p>
 * @author afunms
 * @project afunms
 * @date 2006-08-06
 */

package com.afunms.common.util;

import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Hashtable;
import java.util.List;
import java.util.Queue;

import com.afunms.initialize.ResourceCenter;
import com.afunms.util.DataGate;

/**
 * ���ܣ����ݿ����ӹ����ࣺ�����������ݿ�
 * 
 * @author Administrator
 * 
 */
public class DBManager {
	protected Connection conn;
	protected Statement stmt;
	protected PreparedStatement pstmt;
	protected ResultSet rs;
	protected String preparesql;

	/**
	 * ����Ĭ��ϵͳ���ݿ��ǿ����������ӵģ����Բ��׳�����
	 */

	public Connection getConn() {
		return conn;
	}

	public DBManager() {
		try {
			init(ResourceCenter.getInstance().getJndi());
		} catch (Exception e) {
			e.printStackTrace();
			SysLogger.error("Can not connect system DB!", e);
		}
	}

	public DBManager(String jndi) throws Exception {
		init(jndi);
	}

	/**
	 * ��ʼ��
	 */
	public void init(String jndi) {
		// Context initCtx = null;

		// DataSource ds = null;
		try {
			conn = DataGate.getCon();

			// initCtx = new InitialContext();
			// if("tomcat".equals(ResourceCenter.getInstance().getAppServer()))
			// ds = (DataSource)initCtx.lookup("java:comp/env/" + jndi);
			// //tomcat
			// else
			// ds = (DataSource)initCtx.lookup(jndi); //weblogic
			// conn = ds.getConnection();

			conn.setAutoCommit(false);
			stmt = conn.createStatement();
			// pstmt = conn.prepareStatement(sql);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// try{
			// initCtx.close();
			// }catch(Exception ex){
			// ex.printStackTrace();
			// }
		}

	}

	/**
	 * ��ѯ
	 */
	public ResultSet executeQuery(String sql) {
		try {
			rs = stmt.executeQuery(sql);
		} catch (SQLException se) {
			se.printStackTrace();
			SysLogger.error("Error in DBManager.executeQuery(),SQL=\n" + sql);
		}
		return rs;
	}

	/**
	 * ����,���ύ
	 */
	public void executeUpdate(String sql) {
		// SysLogger.info("executeUpdate(sql)==="+sql);
		executeUpdate(sql, true);
	}

	/**
	 * ����
	 */
	public void executeUpdate(String sql, boolean bCommit) {

		// SysLogger.info("executeUpdate(sql,boolean)==="+sql);
		try {
			// if(stmt == null){
			// try
			// {
			// init(ResourceCenter.getInstance().getJndi());
			// }
			// catch(Exception e)
			// {
			// e.printStackTrace();
			// SysLogger.error("Can not connect system DB!",e);
			// }
			// }
			try {
				stmt = conn.createStatement();
				stmt.executeUpdate(sql);
			} catch (Exception e) {
				e.printStackTrace();
				// SysLogger.error("Can not connect system DB!",e);
			}
			if (bCommit)
				conn.commit();
		} catch (SQLException se) {
			se.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException sqle) {
			}
			SysLogger.error("DBManager.executeUpdate():�������ݳ���:\n" + sql, se);
		}
	}

	/**
	 * �ύ
	 */
	public void commit() {
		try {
			conn.commit();
		} catch (SQLException se) {
			SysLogger.error("DBManager.commit():", se);
		}
	}

	/**
	 * �ع�
	 */
	public void rollback() {
		try {
			conn.rollback();
		} catch (SQLException se) {
			SysLogger.error("DBManager.rollback():", se);
		}
	}

	/**
	 * ����������
	 */
	public void addBatch(String sql) {
		try {
			stmt.addBatch(sql);
		} catch (SQLException se) {
			SysLogger.error("Error in DBManager.addBatch()!" + sql, se);
		}
	}

	/**
	 * ִ��������
	 */
	public void executeBatch() {
		try {
			stmt.executeBatch();
			conn.commit();
		} catch (BatchUpdateException bse) {
			bse.printStackTrace();
			SysLogger.error("-------������һ��SQL������-------", bse);
		} catch (SQLException se) {
			se.printStackTrace();
			SysLogger.error("Error in DBManager.executeBatch()!", se);
		} catch (Exception se) {
			se.printStackTrace();
			SysLogger.error("Error in DBManager.executeBatch()!", se);
		} finally {
			try {
				stmt.clearBatch();
			} catch (SQLException xe) {
				xe.printStackTrace();
			}
		}
	}

	/**
	 * ����������SQL
	 */
	public void setPrepareSql(String sql) {
		preparesql = sql;
		try {
			pstmt = conn.prepareStatement(preparesql);
		} catch (SQLException xe) {
			xe.printStackTrace();
		}
	}

	/**
	 * ����������
	 */
	public void addPrepareBatch(List list) {
		try {
			pstmt.setString(1, (String) list.get(0));
			pstmt.setString(2, (String) list.get(1));
			pstmt.setString(3, (String) list.get(2));
			pstmt.setString(4, (String) list.get(3));
			pstmt.setString(5, (String) list.get(4));
			pstmt.setString(6, (String) list.get(5));
			pstmt.setString(7, (String) list.get(6));
			pstmt.setString(8, (String) list.get(7));
			pstmt.setString(9, (String) list.get(8));
			pstmt.setString(10, (String) list.get(9));
			if ("mysql".equalsIgnoreCase(SystemConstant.DBType)) {
				pstmt.setString(11, (String) list.get(10));
			} else if ("oracle".equalsIgnoreCase(SystemConstant.DBType)) {
				pstmt.setTimestamp(11, Timestamp.valueOf((String) list.get(10)));
			}

			pstmt.setString(12, (String) list.get(11));
			pstmt.setString(13, (String) list.get(12));
			pstmt.addBatch();
			// SysLogger.info((String)list.get(4)+"====="+(String)list.get(5)+"==="+(String)list.get(6));
		} catch (SQLException se) {
			SysLogger.error("Error in DBManager.addBatch()!", se);
		}
	}

	/**
	 * ����������
	 */
	public void addPrepareProcBatch(List list) {
		try {
			pstmt.setString(1, (String) list.get(0));
			pstmt.setString(2, (String) list.get(1));
			pstmt.setString(3, (String) list.get(2));
			pstmt.setString(4, (String) list.get(3));
			pstmt.setString(5, (String) list.get(4));
			pstmt.setString(6, (String) list.get(5));
			pstmt.setString(7, (String) list.get(6));
			pstmt.setString(8, (String) list.get(7));
			pstmt.setString(9, (String) list.get(8));
			pstmt.setString(10, (String) list.get(9));

			if ("mysql".equalsIgnoreCase(SystemConstant.DBType)) {
				pstmt.setString(11, (String) list.get(10));
			} else if ("oracle".equalsIgnoreCase(SystemConstant.DBType)) {
				pstmt.setTimestamp(11, Timestamp.valueOf((String) list.get(10)));
			}
			pstmt.addBatch();
		} catch (SQLException se) {
			SysLogger.error("Error in DBManager.addBatch()!", se);
		}
	}

	/**
	 * ����������
	 */
	public void addPrepareProcLongBatch(List list) {
		try {
			pstmt.setString(1, (String) list.get(0));
			pstmt.setString(2, (String) list.get(1));
			pstmt.setString(3, (String) list.get(2));
			pstmt.setString(4, (String) list.get(3));
			pstmt.setString(5, (String) list.get(4));
			pstmt.setString(6, (String) list.get(5));
			pstmt.setString(7, (String) list.get(6));
			pstmt.setString(8, (String) list.get(7));
			pstmt.setLong(9, (Long) list.get(8));
			pstmt.setString(10, (String) list.get(9));
			if ("mysql".equalsIgnoreCase(SystemConstant.DBType)) {
				pstmt.setString(11, (String) list.get(10));
			} else if ("oracle".equalsIgnoreCase(SystemConstant.DBType)) {
				pstmt.setTimestamp(11, Timestamp.valueOf((String) list.get(10)));
			}

			pstmt.addBatch();
		} catch (SQLException se) {
			SysLogger.error("Error in DBManager.addBatch()!", se);
		}
	}

	/**
	 * ����������
	 */
	public void addPrepareSoftwareBatch(List list) {
		try {
			pstmt.setString(1, (String) list.get(0));
			pstmt.setString(2, (String) list.get(1));
			pstmt.setString(3, (String) list.get(2));
			pstmt.setString(4, (String) list.get(3));
			pstmt.setString(5, (String) list.get(4));
			pstmt.setString(6, (String) list.get(5));
			pstmt.setString(7, (String) list.get(6));
			pstmt.setString(8, (String) list.get(7));

			if ("mysql".equalsIgnoreCase(SystemConstant.DBType)) {
				pstmt.setString(9, (String) list.get(8));
			} else if ("oracle".equalsIgnoreCase(SystemConstant.DBType)) {
				pstmt.setTimestamp(9, Timestamp.valueOf((String) list.get(8)));
			}
			pstmt.addBatch();
		} catch (SQLException se) {
			SysLogger.error("Error in DBManager.addBatch()!", se);
		}
	}

	/**
	 * ����������
	 */
	public void addPrepareServiceBatch(List list) {
		try {
			pstmt.setString(1, (String) list.get(0));
			pstmt.setString(2, (String) list.get(1));
			pstmt.setString(3, (String) list.get(2));
			pstmt.setString(4, (String) list.get(3));
			pstmt.setString(5, (String) list.get(4));
			pstmt.setString(6, (String) list.get(5));
			pstmt.setString(7, (String) list.get(6));
			pstmt.setString(8, (String) list.get(7));
			pstmt.setString(9, (String) list.get(8));

			if ("mysql".equalsIgnoreCase(SystemConstant.DBType)) {
				pstmt.setString(10, (String) list.get(9));
			} else if ("oracle".equalsIgnoreCase(SystemConstant.DBType)) {
				pstmt.setTimestamp(10, Timestamp.valueOf((String) list.get(9)));
			}
			pstmt.addBatch();
		} catch (SQLException se) {
			SysLogger.error("Error in DBManager.addBatch()!", se);
		}
	}

	/**
	 * ����������
	 */
	public void addPrepareErrptBatch(List list) {
		try {
			pstmt.setString(1, (String) list.get(0));
			pstmt.setString(2, (String) list.get(1));

			if ("mysql".equalsIgnoreCase(SystemConstant.DBType)) {
				pstmt.setString(3, (String) list.get(2));
			} else if ("oracle".equalsIgnoreCase(SystemConstant.DBType)) {
				pstmt.setTimestamp(3, Timestamp.valueOf((String) list.get(2)));
			}
			pstmt.setInt(4, (Integer) list.get(3));
			pstmt.setString(5, (String) list.get(4));
			pstmt.setString(6, (String) list.get(5));
			pstmt.setString(7, (String) list.get(6));
			pstmt.setString(8, (String) list.get(7));
			pstmt.setString(9, (String) list.get(8));
			pstmt.setString(10, (String) list.get(9));
			pstmt.setString(11, (String) list.get(10));
			pstmt.setString(12, (String) list.get(11));
			pstmt.setString(13, (String) list.get(12));
			pstmt.setString(14, (String) list.get(13));
			pstmt.setString(15, (String) list.get(14));
			pstmt.addBatch();
		} catch (SQLException se) {
			SysLogger.error("Error in DBManager.addBatch()!", se);
		}
	}

	/**
	 * ִ��������
	 */
	public void executePreparedBatch() {
		try {
			pstmt.executeBatch();
			conn.commit();
		} catch (BatchUpdateException bse) {
			bse.printStackTrace();
			SysLogger.error("-------������һ��SQL������-------", bse);
		} catch (SQLException se) {
			se.printStackTrace();
			SysLogger.error("Error in DBManager.executeBatch()!", se);
		} catch (Exception se) {
			se.printStackTrace();
			SysLogger.error("Error in DBManager.executeBatch()!", se);
		} finally {
			try {
				pstmt.clearBatch();
			} catch (SQLException xe) {
			}
		}
	}

	/**
	 * �ر�����
	 */
	public void close() {

		try {
			conn.commit();
			// stmt.close();
			DataGate.freeCon(conn);
			// DataGate.freeCon(conn);
			// conn.close();
		} catch (Exception ex) {

		}
		try {
			if (rs != null)
				rs.close();
			if (stmt != null)
				stmt.close();
		} catch (SQLException se) {
			se.printStackTrace();
			SysLogger.error("Error in DBManager.close()!", se);
		} finally {
			// rs = null;
			// stmt = null;
			// conn = null;
		}
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
				iResult = executeBatchs();
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
			// logger.error("����ִ��sql��������\n");
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
		try {
			if (null == conn) {
				conn = DataGate.getCon();
				conn.setAutoCommit(false);
				stmt = conn.createStatement();
				// conn = mg.getConnection();
				// stmt = conn.createStatement();
			}
			conn.setAutoCommit(false);
			// logger.info("������������sql�������==" + sql);
			if (list.size() > 0) {
				String sql = "";
				while ((sql = list.poll()) != null) {
					// SysLogger.info(sql);
					stmt.addBatch(sql);
				}
				stmt.executeBatch();
				conn.commit();
				DataGate.freeCon(conn);// ���ͷ����ӣ���ر��α�
				stmt.close();
				// conn.close();
				// logger.info("=======����������========");
			}
		} catch (Exception e) {
			// logger.error("����ִ��sql��������");
			e.printStackTrace();
		} finally {
			try {
				conn.commit();
				stmt.close();
				// conn.close();
			} catch (Exception e) {

			}
		}
		return true;
	}

	/**
	 * 
	 * @author konglq
	 * @����ʱ�� 2010-8-24 ����01:01:44
	 * @������ executeBatch
	 * @return int[]
	 * @�������� ִ��������
	 */
	public int[] executeBatchs() {
		int[] intlist = null;
		try {
			if (null == conn) {
				conn = DataGate.getCon();
				conn.setAutoCommit(false);
				stmt = conn.createStatement();
			}
			conn.setAutoCommit(false);
			intlist = stmt.executeBatch();
			conn.commit();
			conn.setAutoCommit(true);
			return intlist;
		} catch (BatchUpdateException bse) {
			// logger.error("������һ��SQL������", bse);
			return intlist;
		} catch (SQLException se) {
			// logger.error("Error in DBManager.executeBatch()!", se);
			return intlist;
		} finally {
			try {
				stmt.clearBatch();
				stmt.close();
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
	 * @����ʱ�� 2010-8-24 ����12:58:26
	 * @������ executeQueryListHashMap
	 * @param sql
	 * @param indexkey
	 *            ����һ����¼��Ψһkey
	 * @return
	 * @throws SQLException
	 *             List
	 * @�������� ִ�����ݿ��ѯ��䷵���������͵�����,������ÿһ��ֵ�ǹ�ϣ�� ÿ����ϣ�����һ����¼
	 */
	public Hashtable executeQuerykeyoneListHashMap(String sql, String indexkey)
			throws SQLException {
		ResultSetMetaData rsmd = null;
		Hashtable list = new Hashtable();
		int columnCount = 0;
		try {
			// logger.info("ִ��sql���£�" + sql);
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
			// logger.error("ִ��sql�������⣺" + sql + e);
			return null;
		}
		return list; // ����SQL���ԵĲ�ѯ�������
	}

	/**
	 * ����sql ͳ�Ʋ�ѯ���ݼ�¼�������� sql �б�����count(*) ���û����ֱ�ӷ���0
	 * 
	 * @������ executeQuery
	 * @param sql
	 * @return ResultSet
	 * @�������� ִ�в�ѯ������ResultSet
	 */
	public int executeQueryCount(String sql) {
		int i = 0;

		if (sql.indexOf("count(*)") > 0) {// ���sql ��û��count(*) ����ͳ��
			try {
				rs = stmt.executeQuery(sql);
				if (rs.next())
					i = rs.getInt(1);

			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
		return i;
	}

	/**
	 * һ��ֻ�����ݿ��в�ѯ���maxCount����¼
	 * 
	 * @param sql
	 *            �����sql���
	 * @param startNo
	 *            ����һ����¼��ʼ
	 * @param maxCount
	 *            �ܹ�ȡ��������¼
	 */
	public ResultSet executeQueryFromAll(String sql, int startNo, int maxCount) {
		ResultSet rs = null;
		try {
			PreparedStatement pstat = conn.prepareStatement(sql,
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			// ����ѯ���ڼ�����¼
			pstat.setMaxRows(startNo + maxCount - 1);
			rs = pstat.executeQuery();
			// ���α��ƶ�����һ����¼
			rs.first();
			// �α��ƶ���Ҫ����ĵ�һ����¼
			rs.relative(startNo - 2);
			return rs;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}
}
