package com.bpm.system.dbpool;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class DbconnManager {
	private static Context ctx;
	private static DataSource ds;
	private Connection conn = null;

	public DbconnManager() {
	}

	/**
	 * 初始化连接池
	 */
	public void init() {
		try {
			ctx = new InitialContext();
			ds = (DataSource) ctx.lookup("java:/comp/env/jdbc/webdb");
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取连接
	 * @return
	 */
	public Connection getConnection() {
		try {
			if (ds == null) {
				init();
			}
			conn = ds.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}

	/**
	 * 关闭连接 
	 */
	public void close() {
		try {
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		conn = null;
	}

}
