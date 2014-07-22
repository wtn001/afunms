/*
 * Created on 2005-4-5
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.afunms.polling.task;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.afunms.common.util.SysLogger;

/**
 * @author GANYI
 * @since 2012-04-23 11:00:00
 */
public class TFtpUtil {
	private String File = "";
	private String id;
	private String ip = "";
	private int port = 69;
	// Զ���ļ�Ŀ¼
	private String filepath = "";
	private String username = "";
	private String password = "";
	private String serverpath = "";

	/**
	 * 
	 */

	public TFtpUtil() {
		// TODO Auto-generated constructor stub
	}

	public TFtpUtil(String ip, int port, String username, String password, String filepath, String serverpath, String currentfile) {
		super();
		this.ip = ip;
		this.port = port;
		this.username = username;
		this.password = password;
		this.filepath = filepath;
		this.File = currentfile;
		this.serverpath = serverpath;
	}

	public static void main(String[] args) {
		try {
			TFtpUtil tftputil = new TFtpUtil();
			tftputil.uploadFile("10.10.152.71", "d:/1.log");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * tftp ���ط���
	 * 
	 * @author GANYI
	 * @since 2012-04-23 11:11:00
	 * @param host
	 *            tftp��������ip
	 * @param serverFileName
	 *            tftp������Ҫ�����ļ����ļ���
	 * @param filePath
	 *            ��Ҫ�������ļ��ŵ��ط�
	 * @return �Ƿ����سɹ�
	 */
	public boolean tftpOne(String host, String serverFileName, String filePath) {
		boolean flag = true;
		InputStream is = null;
		BufferedReader br = null;
		try {
			Runtime rt = Runtime.getRuntime();
			SysLogger.info("tftp " + host + " get " + serverFileName + " " + filePath);
			Process p = rt.exec("tftp " + host + " get " + serverFileName + " " + filePath);
			is = p.getInputStream();
			br = new BufferedReader(new InputStreamReader(is));
			String line = "";
			String s = "";
			while ((s = br.readLine()) != null) {
				line += s;
			}
			if (line.startsWith("����ɹ�") || line.startsWith("Transfer successful")) {
				SysLogger.info("##############���سɹ�\n" + line);
				flag = true;
			} else {
				SysLogger.info("##############" + line);
				flag = false;
			}
		} catch (Exception e) {
			return false;
		} finally {
			try {
				if (br != null)
					br.close();
				if (is != null)
					is.close();
			} catch (Exception e) {

			}
		}

		return flag;

	}

	/**
	 * tftp�ϴ�����
	 * 
	 * @author GANYI
	 * @since 2012-04-23 11:27:00
	 * @param host
	 *            tftp��������ip
	 * @param path
	 *            ���ϴ��ļ���·��
	 * @return �Ƿ��ϴ��ɹ�
	 */
	public boolean uploadFile(String host, String path) {
		boolean flag = true;
		InputStream is = null;
		BufferedReader br = null;
		try {
			Runtime rt = Runtime.getRuntime();
			Process p = rt.exec("tftp " + host + " put " + path);
			is = p.getInputStream();
			br = new BufferedReader(new InputStreamReader(is));
			String line = "";
			String s = "";
			while ((s = br.readLine()) != null) {
				line += s;
			}
			if (line.startsWith("����ɹ�") || line.startsWith("Transfer successful")) {
				SysLogger.info("##############�ϴ��ɹ�\n" + line);
				flag = true;
			} else {
				SysLogger.info("##############" + line);
				flag = false;
			}
		} catch (Exception e) {
			return false;
		} finally {
			try {
				if (br != null)
					br.close();
				if (is != null)
					is.close();
			} catch (Exception e) {

			}
		}

		return flag;
	}

	/**
	 * @return
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param string
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return
	 */
	public String getServerpath() {
		return serverpath;
	}

	/**
	 * @param string
	 */
	public void setServerpath(String serverpath) {
		this.serverpath = serverpath;
	}

	/**
	 * @return
	 */
	public String getFile() {
		return File;
	}

	/**
	 * @return
	 */
	public String getFilepath() {
		return filepath;
	}

	/**
	 * @param string
	 */
	public void setFile(String string) {
		File = string;
	}

	/**
	 * @param string
	 */
	public void setFilepath(String string) {
		filepath = string;
	}

}
