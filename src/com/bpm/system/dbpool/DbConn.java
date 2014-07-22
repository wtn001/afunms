package com.bpm.system.dbpool;

import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

public class DbConn {
	private static final Logger logger = Logger.getLogger(DbConn.class);
	private Connection conn = null;
	private Statement stmt = null;
	private ResultSet rs = null;
	private DbconnManager dbm = new DbconnManager();

	public DbConn() {
		init();
	}
	
	public Connection getConnection()
	{
		if(conn==null)
		{
			conn=dbm.getConnection();
		}
		return conn;
	}

	/**
	 * 初始化连接池
	 */
	private void init() {
		try {
			conn = dbm.getConnection();
			stmt = conn.createStatement();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 查询
	 * 
	 * @param sql
	 * @return
	 */
	public ResultSet executeQuery(String sql) {
		try {
			if (conn == null) {
				this.init();
			}
			rs = stmt.executeQuery(sql);
		} catch (SQLException se) {
			se.printStackTrace();
			logger.error("executeQuery() 查询数据出错:" + sql);
		}

		return rs;
	}

	/**
	 * 更新数据
	 * 
	 * @param sql
	 * @return
	 */
	public int executeUpdate(String sql) {
		int iresult = -1;
		iresult = executeUpdate(sql, false);
		return iresult;
	}

	/**
	 * 更新数据
	 * 
	 * @param sql
	 * @param bCommit
	 * @return
	 */
	private int executeUpdate(String sql, boolean bCommit) {
		int iresult = -1;
		try {
			if (conn == null) {
				this.init();
			}
			logger.info("执行sql语句如下" + sql);
			iresult = stmt.executeUpdate(sql);
			if (bCommit) {
				conn.commit();
			}
			return iresult;
		} catch (SQLException se) {
			se.printStackTrace();
			logger.error("executeUpdate():更新数据出错:" + sql);
			try {
				conn.rollback();
				return iresult;
			} catch (Exception sqle) {
				return iresult;
			}

		}

	}

	/**
	 * 提交
	 */
	private void commit() {
		try {
			conn.commit();
		} catch (SQLException se) {
			logger.error("commit() 出错:", se);
		}
	}

	/**
	 * 回滚
	 */
	private void rollback() {
		try {
			conn.rollback();
		} catch (SQLException se) {
			logger.error("rollback()出错:", se);
		}
	}

	/**
	 * 关闭
	 */
	public void close() {
		try {
			if (rs != null)
				rs.close();
			if (stmt != null)
				stmt.close();
			if (conn != null && !conn.isClosed()) {
				conn.close();
			}
		} catch (SQLException se) {
			se.printStackTrace();
			logger.error("close()出错", se);
		}
	}
	
	/**
	 * 加入批处理
	 * @param sql
	 */
	public void addBatch(String sql) {
		try {
			if (null == conn) {
				this.init();
			}
			stmt.addBatch(sql);
		} catch (SQLException se) {
			logger.error("addBatch() 出现错误!" + sql, se);
		}
	}
	
	/**
	 * 执行批处理
	 * @return
	 */
	@SuppressWarnings("finally")
	public int[] executeBatch() {
		int[] intlist = null;
		try {
			if (null == conn) {
				this.init();
				conn.setAutoCommit(false);
			}
			conn.setAutoCommit(false);
			intlist = stmt.executeBatch();
			conn.commit();
			conn.setAutoCommit(true);
			return intlist;
		} catch (BatchUpdateException bse) {
			logger.error("至少有一条SQL语句错误", bse);
			conn.rollback();
			return intlist;
		} catch (SQLException se) {
			logger.error("executeBatch():", se);
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

	
	
	/*
	 * public static void test() throws Exception { // 获取Content对象 Context ctx
	 * =new InitialContext(); DataSource ds =
	 * (DataSource)ctx.lookup("java:/comp/env/jdbc/webdb"); Connection conn =
	 * ds.getConnection(); Statement stmt = null; String sql=null; ResultSet
	 * rSet = null; stmt=conn.createStatement();
	 * 
	 * sql="select * from bpm_menu"; rSet=stmt.executeQuery(sql);
	 * while(rSet.next()) { System.out.println(rSet.getString(1)); } }
	 * 
	 * public static void main(String[] args) throws Exception { test(); }
	 */

}
