package com.afunms.polling.telnet;

import java.io.InputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import org.apache.commons.net.telnet.TelnetClient;

import com.afunms.common.base.BaseVo;
import com.afunms.common.util.SysLogger;
import com.afunms.config.model.CmdResult;
import com.afunms.initialize.ResourceCenter;

/**
 * 
 * ����������¼����telnet ��¼��Ϊ·���� ʵ�ֵ�¼����������أ��������Ϣ ��Ҫ�ɼ���ɺ�һ��Ҫ����
 * disconnect�����������ر�telnet���� ʵ�ֻ�ȡ�����ļ���ʵ��д�����ļ�
 * 
 * @author konglq
 * 
 */

public class Huawei3comvpn extends BaseVo {

	private final static int debug = 0;
	private TelnetClient telnet = new TelnetClient();

	private InputStream in; // 

	private PrintStream out;

	private String DEFAULT_FLAG = ">";
	private String DEFAULT_PROMPT = "#";

	private final String USER_PROMPT = ">";// һ���û���ʶ��
	private final String SU_PROMPT = "#";// �����û���ʶ��
	private final String SYS_PROMPT = "]";// ȫ����ͼ�û���ʶ��

	private String Loginuser = "Username:";// �û�
	private String Loginpassword = "Password:";// �û�������

	private String Loginsuuser = "Username:";// su�û�
	private String Loginpsuassword = "Password:";// su�û�������

	private int nummber = 30000;// ����һ����ȡ�ַ�������

	/**
	 * telnet �˿�
	 */
	private int port = 23;

	/**
	 * �û���
	 */
	private String user = "";

	/**
	 * ����
	 */
	private String password = "";

	/**
	 * IP ��ַ
	 */
	private String ip = "";

	/**
	 * ȱʡ�˿�
	 */
	private int DEFAULT_TELNET_PORT = 23;

	private String suuser = "";

	private String supassword = "";

	/**
	 * 
	 * Ĭ�Ϲ���
	 */
	public Huawei3comvpn() {
	}

	/**
	 * 
	 * ����
	 * 
	 * @param ip
	 *            ��ַ
	 * @param user
	 *            Ĭ���û�
	 * @param password
	 *            Ĭ���û�����
	 * @param suuser
	 *            su �û�
	 * @param supassword
	 *            �û�����
	 * @param defaule
	 *            Ĭ�ϵ�ϵͳ��ʶ���� $ ���� #
	 */
	public Huawei3comvpn(String ip, String user, String password, int port, String suuser, String supassword, String defaule) {
		this.ip = ip;
		this.port = this.getDEFAULT_TELNET_PORT();
		this.user = user; // Ĭ���û�
		this.password = password; // Ĭ���û�����
		this.supassword = supassword;// su�û�
		this.suuser = suuser;// su�û�������
		this.DEFAULT_PROMPT = defaule;// ϵͳĬ�ϱ�ʶ����
		this.DEFAULT_TELNET_PORT = port;// �˿ں�
	}

	/**
	 * 
	 * ����·����
	 * 
	 * @return boolean ���ӳɹ�����true�����򷵻�false
	 */
	private boolean connect() {

		boolean isConnect = true;

		try {
			telnet.setConnectTimeout(5000);// ���ô�δ���ӵ�����״̬�ĳ�ʱʱ��
			telnet.connect(ip, port);
			in = telnet.getInputStream(); // ������
			out = new PrintStream(telnet.getOutputStream()); // �����

		} catch (Exception e) {
			isConnect = false;
			e.printStackTrace();
			return isConnect;
		}
		return isConnect;
	}

	/***************************************************************************
	 * ��ȡ��Ϣ
	 * 
	 * @param pattern
	 *            ��������
	 * @return ���������Ӧ���ַ������ַ����������н�������û�ж����򷵻�null
	 */
	private String readUntil(String pattern) {
		StringBuffer sb = new StringBuffer();
		try {

			// System.out.println("======��ȡ");
			char lastChar = pattern.charAt(pattern.length() - 1);
			// System.out.println("======��ȡ---"+lastChar);
			char ch = (char) in.read();
			int n = 0;
			// System.out.println("======ch---"+ch);
			boolean flag = true;
			while (flag) {
				if (ch == 0 || ch == 13 || ch == 10 || (ch >= 32)) {
					sb.append(ch);
				}
				if (ch == lastChar) {
					if (sb.toString().endsWith(pattern)) {
						return sb.toString();
					}
				}

				ch = (char) in.read();

				n++;
				if (n > this.nummber) {// �����ȡ���ַ���������2����ַ�����û����ȷ
					sb.delete(0, sb.length());
					sb.append("user or password error");
					flag = false;
					break;
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return sb.toString();
	}

	/*
	 * �ṩ�����ַ�������Ϊ���������ж� ����ִ�к󣬿��ܷ��سɹ���Ҳ���ܷ���ʧ�ܡ��ṩ�����ַ������ܿ���ʶ�������Ƿ�ִ�н������ӿ����ִ��Ч��
	 */
	private String readUntil1(String pattern1, String pattern2) {
		StringBuffer sb = new StringBuffer();
		try {

			char ch = (char) in.read();
			int n = 0;
			boolean flag = true;

			while (flag) {

				if (ch == 0 || ch == 13 || ch == 10 || (ch >= 32)) {
					sb.append(ch);
				}
				if (sb.toString().endsWith("in unit1 login")) // �����pattern1��β��˵������ִ�гɹ�
				{
					return "in unit1 login";
				} else if (sb.toString().endsWith(pattern1)) // �涨�������pattern1��β��˵������ִ�гɹ�
				{
					return pattern1;
				} else if (sb.toString().endsWith(pattern2)) // �����pattern1��β��˵������ִ�гɹ�
				{
					return pattern2;
				} else if (sb.toString().indexOf("% Login failed!") > 0) {
					return "user or password error";
				}
				ch = (char) in.read();
				n++;
				if (n > this.nummber) {// �����ȡ���ַ���������2����ַ�����û����ȷ
					sb.delete(0, sb.length());
					sb.append("user or password error");
					flag = false;
					break;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			return "user or password error";
		}
		return sb.toString();
	}

	/**
	 * 
	 * @description �ṩ��������������Ϊ���������ж�
	 *              ����ִ�к󣬿��ܷ��سɹ���Ҳ���ܷ���ʧ�ܡ��ṩ�����ַ������ܿ���ʶ�������Ƿ�ִ�н������ӿ����ִ��Ч��
	 * @author wangxiangyong
	 * @date Feb 23, 2013 11:04:49 AM
	 * @return String
	 * @param pattern1
	 * @param pattern2
	 * @param pattern3
	 * @return
	 */
	private String readUntil(String pattern1, String pattern2, String pattern3) {
		StringBuffer sb = new StringBuffer();
		try {

			char ch = (char) in.read();
			int n = 0;
			boolean flag = true;

			while (flag) {

				if (ch == 0 || ch == 13 || ch == 10 || (ch >= 32)) {
					sb.append(ch);
				}

				if (sb.toString().endsWith(pattern1)) // �涨�������pattern1��β��˵������ִ�гɹ�
				{
					return pattern1;
				} else if (sb.toString().endsWith(pattern2)) // �����pattern2��β��˵������ִ�гɹ�
				{
					return pattern2;
				} else if (sb.toString().endsWith(pattern3)) // �����pattern3��β��˵������ִ�гɹ�
				{
					return pattern3;
				}
				ch = (char) in.read();
				n++;
				if (n > this.nummber) {// �����ȡ���ַ���������2����ַ�����û����ȷ
					sb.delete(0, sb.length());
					sb.append("user or password error");
					flag = false;
					break;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			return "user or password error";
		}

		return sb.toString();
	}

	/**
	 * 
	 * @description �ṩ�����ַ����飬��Ϊ���������ж�
	 *              ����ִ�к󣬿��ܷ��سɹ���Ҳ���ܷ���ʧ�ܡ��ṩ�����ַ������ܿ���ʶ�������Ƿ�ִ�н������ӿ����ִ��Ч��
	 * @author wangxiangyong
	 * @date Feb 23, 2013 10:49:15 AM
	 * @return String
	 * @param patterns
	 * @return
	 */
	private String readUntil(String[] patterns) {
		StringBuffer sb = new StringBuffer();
		try {

			char ch = (char) in.read();
			int n = 0;
			boolean flag = true;

			while (flag) {

				if (ch == 0 || ch == 13 || ch == 10 || (ch >= 32)) {
					sb.append(ch);
				}
				for (int i = 0; i < patterns.length; i++) {
					if (sb.toString().endsWith(patterns[i])) // �涨�������pattern1��β��˵������ִ�гɹ�
					{
						return patterns[i];
					}
				}

				ch = (char) in.read();
				n++;
				if (n > this.nummber) {// �����ȡ���ַ���������2����ַ�����û����ȷ
					sb.delete(0, sb.length());
					sb.append("user or password error");
					flag = false;
					break;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			return "user or password error";
		}
		return sb.toString();
	}

	private boolean isLoginSuccess(String pattern) {
		StringBuffer sb = new StringBuffer();
		try {

			// System.out.println("======��ȡ");
			char lastChar = pattern.charAt(pattern.length() - 1);

			char ch = (char) in.read();
			int n = 0;
			while (true) {
				// System.out.print(ch);// ---��Ҫע�͵�

				if (ch == 0 || ch == 13 || ch == 10 || (ch >= 32)) {
					sb.append(ch);
				}

				if (ch == lastChar) {
					if (sb.toString().endsWith(pattern)) {
						// System.out.println(sb.toString());
						return true;
					}
				}
				if (sb.toString().contains("Login failed!")) {
					// System.out.println(sb.toString());
					return false;
				}

				ch = (char) in.read();
				n++;
				if (n > this.nummber) {// �����ȡ���ַ���������2����ַ�����û����ȷ
					// sb.delete(0, sb.length());
					// sb.append("login error");
					break;
				}
				// System.out.println(n);

			}

		} catch (Exception e) {
			return false;
		}
		return false;
	}

	/***************************************************************************
	 * 
	 * ��ȡ����ؽ��
	 * 
	 * @param pattern
	 * @return
	 */
	private String readcommand(String pattern) {
		try {
			char lastChar = pattern.charAt(pattern.length() - 1);
			byte[] tempchars = new byte[30];

			int charread = 0;

			// �������ַ����ַ������У�charreadΪһ�ζ�ȡ�ַ���
			byte[] b1 = new byte[1];
			int n = 0;
			int nn = 0;
			StringBuffer ret = new StringBuffer();
			String current;

			while (n >= 0) {
				// System.out.println("--��ȡ������Ϣ");
				n = in.read(b1);
				if (n > 0) {
					current = new String(b1, 0, n, "iso-8859-1");
					String tempCurrent = "";
					char[] a = current.toCharArray();
					for (int i = 0; i < a.length; i++) {
						char flag = a[i];
						if (flag == 0 || flag == 13 || flag == 10 || (flag >= 32)) {
							ret.append(a[i]);
						}

					}

					ret.append(tempCurrent);

					if (ret.toString().endsWith("---- More ----")) {
						out.write(32);
						out.flush();
					}

					if (ret.toString().endsWith(pattern)) {

						return ret.toString();
					}
					if (ret.toString().endsWith(pattern)) {

						break;
					}

				} // if
				// nn++;
				// if(nn>this.nummber)
				// {
				// return null;
				// }
			}

			return ret.toString();

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * 
	 * @description TODO
	 * @author wangxiangyong
	 * @date Feb 23, 2013 1:40:30 PM
	 * @return String
	 * @param pattern
	 * @return
	 */
	private String readcommand(String pattern1, String pattern2) {
		try {

			// �������ַ����ַ������У�charreadΪһ�ζ�ȡ�ַ���
			byte[] b1 = new byte[254];
			int n = 0;
			StringBuffer ret = new StringBuffer();
			String current;

			while (n >= 0) {
				n = in.read(b1);
				if (n > 0) {

					current = new String(b1, 0, n, "iso-8859-1");
					String tempCurrent = "";
					char[] a = current.toCharArray();

					for (int i = 0; i < a.length; i++) {
						char flag = a[i];
						if (flag == 0 || flag == 13 || flag == 10 || (flag >= 32)) {
							ret.append(a[i]);
						}

					}

					ret.append(tempCurrent);

					if (ret.toString().toLowerCase().indexOf("---- more ----") > 0||ret.toString().toLowerCase().indexOf("--more--") > 0) {
						out.write(32);
						out.flush();
					}

					if (ret.toString().indexOf(pattern1) > 0) {

						return ret.toString();
					}
					if (ret.toString().indexOf(pattern2) > 0) {
						return ret.toString();
					}

				}
			}

			return ret.toString();

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	/***************************************************************************
	 * 
	 * ��ȡ����ؽ��
	 * 
	 * @param pattern
	 * @return
	 */
	private void readcommand(String pattern1, String pattern2, StringBuffer ret, String each) {
		try {

			// �������ַ����ַ������У�charreadΪһ�ζ�ȡ�ַ���
			byte[] b1 = new byte[1];
			int n = 0;
			int nn = 0;

			String current;

			while (n >= 0) {
				// System.out.println("--��ȡ������Ϣ");
				n = in.read(b1);
				if (n > 0) {
					current = new String(b1, 0, n, "iso-8859-1");
					String tempCurrent = "";
					char[] a = current.toCharArray();
					for (int i = 0; i < a.length; i++) {
						char flag = a[i];
						if (flag == 0 || flag == 13 || flag == 10 || (flag >= 32)) {
							ret.append(a[i]);
						}

					}

					ret.append(tempCurrent);

					if (ret.toString().endsWith("---- More ----")||ret.toString().endsWith("--More--")) {
						out.write(32);
						out.flush();
					}

					if (ret.toString().indexOf(pattern1) > 0) {

						break;
					}
					if (ret.toString().indexOf(pattern2) > 0) {
						break;
					}

				} // if

			}

			// System.out.println(ret.toString());

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * ����ָ��
	 * 
	 * @param value
	 */
	private void write(String value) {
		try {
			out.println(value);

			out.flush();
			// System.out.println(value);// ---��Ҫע�͵�
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * ����������ؽ��
	 * 
	 * @param command
	 *            ������
	 * @return ����Ľ��
	 */
	private String sendCommand(String command) {
		String content = null;
		try {
			write("\r\n");
			// content = readcommand(DEFAULT_PROMPT);
			// System.out.println(content);
			write(command);
			content = readcommand(DEFAULT_PROMPT);
			return content;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private void disconnect() {
		try {
			if (null != in) {
				in.close();
			}

			if (null != out) {
				out.close();
			}
			if (null != telnet && telnet.isConnected()) {
				telnet.disconnect();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * ͳһ��¼��������¼��Ϊ2���� һ������ͨ�û���¼������su�û���¼
	 * 
	 * @return true ���� false true��ʾ�ɹ���false��ʾʧ��
	 */
	private boolean Loginnet() {
		boolean connetflg = false;
		boolean loginflg = false;

		if (null != this.user && this.user.trim().length() > 0 && null != this.password && this.password.trim().length() > 0) {
			connetflg = this.connect();// telnet����
			// ���Ӳ��ɹ�����null
			if (connetflg) {// �ɹ�

				// String aa = readUntil(this.Loginsuuser); // �ȶ�ȡ
				// System.out.println("��ȡ��¼�ַ��Ƿ�ɹ�"+aa);
				// write(user);
				String aa = readUntil(this.Loginpassword);
				// System.out.println("====2"+aa);
				write(password);
				aa = readUntil1(DEFAULT_PROMPT, this.Loginpassword);//
				if (aa.equals("user or password error"))
					loginflg = false;
				else if (aa.equals(DEFAULT_PROMPT))
					loginflg = true;
				else if (aa.equals("Password:"))
					loginflg = false;
				// System.out.println(aa);

				// System.out.println("��һ�˵�¼�ɹ�");
			}

		} else if (loginflg == false) {// �û����벻��ȷ����
			// System.out.println("=======1==");
			this.disconnect(); // �ر�����
			return false;
		}
		// System.out.println("");
		// �����¼�ɹ������ж�su���û�����Ŀ�����
		if (loginflg && null != suuser && suuser.trim().length() > 0 && null != supassword && supassword.trim().length() > 0) {

			// System.out.println("=====��ʼsu �û���¼");
			// readUntil(this.Loginsuuser); //�ȶ�ȡ
			// System.out.println("��ȡ��¼�ַ��Ƿ�ɹ�"+aa);
			write(this.suuser);
			String suresult = readUntil(this.Loginpsuassword);
			write(this.supassword);
			suresult = readUntil(DEFAULT_PROMPT);// �������¼�ɹ�

			loginflg = true;

		} else if (loginflg == false) {
			this.disconnect(); // �ر�����
			return false;
		}

		return loginflg;

	}

	/**
	 * 
	 * ���h3c/haiwei�豸�����豸������
	 * 
	 * @return �ַ�����ʽ������
	 */
	public String backupConfFile(String bkpType) {

		String result = "";
		// result = this.Getcommantvalue("disp cu");// ��ȡ����Ľ��
		if (bkpType.equals("run"))
			result = this.getCommantValue("0", "disp cu");// ��ȡ����Ľ��
		// 0:h3c/haiwei�豸�����ļ�����
		else
			result = this.getCommantValue("0", "disp saved-configuration");// ��ȡ����Ľ��
		// 0:h3c/haiwei�豸�����ļ�����
		// �Խ�����и�ʽ��
		if (null != result && !result.equals("user or password error")) {
			String[] st = result.split("\r\n");
			StringBuffer buff = new StringBuffer();
			for (int i = 1; i < st.length - 1; i++) {

				if (!st[i].startsWith("  ---- More ----")) {
					buff.append(st[i]).append("\r\n");
				}
			}
			result = buff.toString();

		}

		return result;
	}

	/**
	 * 
	 * ����ɨ�������豸������
	 * 
	 * @return �ַ�����ʽ������
	 */
	public String BackupConfFile(String[] content) {

		String result = "";
		// result = this.Getcommantvalue("disp cu");// ��ȡ����Ľ��

		result = this.Getcommantvalue1(content);// ��ȡ����Ľ��

		// �Խ�����и�ʽ��
		// if (null != result && !result.equals("user or password error")) {
		// String[] st = result.split("\r\n");
		// StringBuffer buff = new StringBuffer();
		// for (int i = 1; i < st.length-1; i++) {
		//				
		// if(!st[i].startsWith(" ---- More ----"))
		// {
		// buff.append(st[i]).append("\r\n");
		// }
		// }
		// result = buff.toString();
		//			
		// }

		return result;
	}

	/**
	 * ��ָ���ļ������ļ��ϴ���ָ����IP�豸�ϣ�����ʹ�������ļ����豸����Ч
	 * 
	 * @param remoteFile
	 *            �ϴ�����ļ�������/cfg1.cfg
	 * @param localFile
	 *            �����ļ�������C:\\Documents and Settings\\GZM\\cfg1.cfg
	 * @param ftpIp
	 * @param ftpPort
	 * @param ftpUser
	 * @param ftpPassword
	 * @return
	 */
	public boolean setupNewConfFile(String remoteFile, String localFile, String ftpIp, int ftpPort, String ftpUser, String ftpPassword) {
		boolean flg = Loginnet();

		String aa = null;
		if (flg) {
			write("system-view");
			System.out.println(readUntil("]"));

			write("ftp server enable");
			System.out.println(readUntil("]"));
			write("quit");
			System.out.println(readUntil(">"));

			FTPComply ft = new FTPComply();
			// flg = ft.uploadFile("/cfg1.cfg", "C:\\Documents and
			// Settings\\GZM\\cfg1.cfg","10.10.152.254",21,"admin","admin",12000);
			flg = ft.uploadFile(remoteFile, localFile, ftpIp, ftpPort, ftpUser, ftpPassword, 12000);
		}
		if (flg) {
			flg = false;
			write("startup saved-configuration " + remoteFile.substring(1));
			aa = readUntil("[Y/N]:");
			System.out.println(aa);
			if (!aa.equals("user or password error")) {
				write("y");
				aa = readUntil(">");
				System.out.println(aa);
				if (!aa.equals("user or password error")) {
					write("reboot");
					aa = readUntil("[Y/N]?");
					System.out.println(aa);
					if (!aa.equals("user or password error")) {
						write("n");
						aa = readUntil("[Y/N]?");
						System.out.println(aa);
						if (!aa.equals("user or password error")) {
							flg = true;
							write("y");
							try {
								Thread.sleep(1000);
							} catch (Exception e) {
							}
							System.out.println("wake up");
						}
					}
				}
			}
		}
		this.disconnect();
		return flg;
	}

	public boolean Writeconffile(String conf) {
		boolean flg = Loginnet();

		if (flg) {
			// ���뵽����ģʽ
			write("system-view");
			write(conf);// д�����ļ�
			if (this.suuser != null && this.supassword != null && this.supassword.length() > 0) {

				write("quit");
			}
			write("quit");
			write("save");
			readUntil("N");
			write("y");
			readUntil(">");// �ȴ��������

		}

		return flg;
	}

	/**
	 * 
	 * ��ȡ������ȡ�����е���Ϣ
	 * 
	 * @param command
	 *            //������
	 * @return �����������ʾ���ݣ�������Ӳ��ϻ��쳣����null
	 */
	public String Getcommantvalue(String command) {
		String result = "";// ������
		// ���ж��û��������Ƿ�Ϊnull���ǿյ��ַ�
		boolean connetflg = false;// ���ӱ�ʶ����
		boolean loginflg = false;
		// String Liginstr="";
		if (null != this.user && this.user.trim().length() > 0 && null != this.password && this.password.trim().length() > 0) {

			connetflg = this.connect();// telnet����
			// ���Ӳ��ɹ�����null
			if (connetflg) {// �ɹ�

				// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
				// /////////////////���ò�ͬ���˻����ͣ����в�ͬ���ַ���ʾ������ط���Ӱ�쵽����ִ���ٶ�
				// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
				// String aa = readUntil(this.Loginsuuser); // �ȶ�ȡ
				String aa = readUntil("Password:");
				// System.out.println("��ȡ��¼�ַ��Ƿ�ɹ�"+aa);
				write(user);
				aa = readUntil(this.Loginpassword);
				// System.out.println("====2"+aa);
				write(password);
				aa = readUntil(DEFAULT_PROMPT);//
				if (aa.equals("user or password error"))
					loginflg = false;
				else {
					loginflg = true;
					// System.out.println("��һ�˵�¼�ɹ�");
				}
			}
		} else if (loginflg == false) {// �û����벻��ȷ����
			// System.out.println("=======1==");
			this.disconnect(); // �ر�����
			return "user or password error";
		}
		// System.out.println("");
		// �����¼�ɹ������ж�su���û�����Ŀ�����
		if (loginflg && null != suuser && suuser.trim().length() > 0 && null != supassword && supassword.trim().length() > 0) {
			// System.out.println("=====��ʼsu �û���¼");
			// readUntil(this.Loginsuuser); //�ȶ�ȡ
			// System.out.println("��ȡ��¼�ַ��Ƿ�ɹ�"+aa);
			write(this.suuser);
			readUntil(this.Loginpsuassword);
			write(this.supassword);
			readUntil(DEFAULT_PROMPT);// �������¼�ɹ�

			loginflg = true;

		} else if (loginflg == false) {
			this.disconnect(); // �ر�����
			return "user or password error";
		}

		// �����¼����������
		if (loginflg) {
			result = this.sendCommand(command);
			// System.out.println("�ɼ����=======" + result);
		}

		this.disconnect(); // �ر�����

		return result;
	}

	/**
	 * 
	 * @description ��ȡ����ؽ�����˷�����Ҫ��Զ�ȡ�����ļ���
	 * @author wangxiangyong
	 * @date Feb 23, 2013 10:09:06 AM
	 * @return String
	 * @param type
	 *            0:h3c/huawei �豸�����ļ� 1��ͨ�õ�
	 * @param command
	 * @return
	 */

	public String getCommantValue(String type, String command) {
		String result = "";// ������

		boolean connetflg = false;// ���ӱ�ʶ����
		boolean loginflg = false;
		try {

			connetflg = this.connect();// ��telnet�û���������Ϊ��ʱ��ֱ�Ӵ�������
			if (null != this.password && this.password.trim().length() > 0) {
				// ���Ӳ��ɹ�����null
				if (connetflg) {// �ɹ�
					// /////////////////���ò�ͬ���˻����ͣ����в�ͬ���ַ���ʾ������ط���Ӱ�쵽����ִ���ٶ�/////////////
					String[] patterns = { USER_PROMPT, SYS_PROMPT, this.Loginuser, Loginpassword, "Password:" };
					String[] pass_patterns = { "in unit1 login", USER_PROMPT, SYS_PROMPT, Loginpassword, "% Login failed!" };
					String aa = readUntil(patterns); // �ȶ�ȡ
					if (aa.equalsIgnoreCase("Password:")) {
						write(password);
						aa = readUntil(pass_patterns);
					}
					if (null != this.user && this.user.trim().length() > 0) {
						if (aa.equalsIgnoreCase("Username:")) {
							write(user);
							aa = readUntil("Password:");
							write(password);
							aa = readUntil(pass_patterns);
						}
					}

					if (aa.equals("% Login failed!") || aa.equals("user or password error")) {
						loginflg = false;
					} else if (aa.equals(USER_PROMPT) || aa.equals(SYS_PROMPT)) {
						loginflg = true;
					} else if (aa.equals("Password:")) {
						loginflg = false;
					} else if (aa.equals("in unit1 login")) {
						write("\r\n");
						loginflg = true;
					}

				}
			} else if (loginflg == false) {// �û����벻��ȷ����
				// this.disconnect(); // �ر�����
				return "user or password error";
			}
			// �����¼�ɹ������ж�su���û�����Ŀ�����
			if (loginflg && null != suuser && suuser.trim().length() > 0 && null != supassword && supassword.trim().length() > 0) {
				write(suuser);
				String pass = readUntil(USER_PROMPT, SYS_PROMPT, "Password:");

				if (pass.equalsIgnoreCase("Password:"))
					write(this.supassword);

				readUntil1(USER_PROMPT, SYS_PROMPT);// �������¼�ɹ�
				loginflg = true;

			} else if (loginflg == false) {
				// this.disconnect(); // �ر�����
				return "user or password error";
			}

			// �����¼����������
			if (loginflg) {
				write(command);
				String pattern1 = "";
				String pattern2 = "";
				if (type != null && type.equals("0")) {
					pattern1 = "return\r\n";
					pattern2 = USER_PROMPT;
				} else {
					pattern1 = USER_PROMPT;
					pattern2 = SYS_PROMPT;
				}
				result = readcommand(pattern1, pattern2);
				if (result != null){
					
					result = result.substring(result.indexOf(command));
				}
			}
		} catch (Exception e) {
			SysLogger.error("telnet ����ִ�г�������com.afunms.polling.telnet.Huawei3comvpn ������getCommantValue(String command)", e);
		} finally {
			this.disconnect(); // �ر�����
		}

		return result;
	}

	/**
	 * 
	 * @description �޷��ؽ��������
	 * @author wangxiangyong
	 * @date Apr 22, 2013 1:33:44 PM
	 * @return void
	 * @param type 1:�ڳ�������Ա��ִ��
	 * @param command
	 */
	public void getCommantValue(String type, String[] command) {

		boolean connetflg = false;// ���ӱ�ʶ����
		boolean loginflg = false;
		try {

			connetflg = this.connect();// ��telnet�û���������Ϊ��ʱ��ֱ�Ӵ�������
			if (null != this.password && this.password.trim().length() > 0) {
				// ���Ӳ��ɹ�����null
				if (connetflg) {// �ɹ�
					// /////////////////���ò�ͬ���˻����ͣ����в�ͬ���ַ���ʾ������ط���Ӱ�쵽����ִ���ٶ�/////////////
					String[] patterns = { USER_PROMPT, SYS_PROMPT, this.Loginuser, Loginpassword, "Password:" };
					String[] pass_patterns = { "in unit1 login", USER_PROMPT, SYS_PROMPT, Loginpassword, "% Login failed!" };
					String aa = readUntil(patterns); // �ȶ�ȡ
					if (aa.equalsIgnoreCase("Password:")) {
						write(password);
						aa = readUntil(pass_patterns);
					}
					if (null != this.user && this.user.trim().length() > 0) {
						if (aa.equalsIgnoreCase("Username:")) {
							write(user);
							aa = readUntil("Password:");
							write(password);
							aa = readUntil(pass_patterns);
						}
					}

					if (aa.equals("% Login failed!") || aa.equals("user or password error")) {
						loginflg = false;
					} else if (aa.equals(USER_PROMPT) || aa.equals(SYS_PROMPT)) {
						loginflg = true;
					} else if (aa.equals("Password:")) {
						loginflg = false;
					} else if (aa.equals("in unit1 login")) {
						write("\r\n");
						loginflg = true;
					}

				}
			}
			// �����¼�ɹ������ж�su���û�����Ŀ�����
			if (type!=null&&type.equals("1")&&loginflg && null != suuser && suuser.trim().length() > 0 && null != supassword && supassword.trim().length() > 0) {
				write(suuser);
				String pass = readUntil(USER_PROMPT, SYS_PROMPT, "Password:");

				if (pass.equalsIgnoreCase("Password:"))
					write(this.supassword);

				readUntil1(USER_PROMPT, SYS_PROMPT);// �������¼�ɹ�
				loginflg = true;

			}

			// �����¼����������
			if (loginflg) {
				StringBuffer result = new StringBuffer();
				if (command != null && command.length > 0) {
					for (int i = 0; i < command.length; i++) {
						String each = (String) command[i];
						write(each);

						readcommand(USER_PROMPT, SYS_PROMPT, result, each);
					}
				}
				String pattern1 = USER_PROMPT;
				String pattern2 = SYS_PROMPT;

				readcommand(pattern1, pattern2);

			}
		} catch (Exception e) {
			SysLogger.error("telnet ����ִ�г�������com.afunms.polling.telnet.Huawei3comvpn ������getCommantValue(String command)", e);
		} finally {
			this.disconnect(); // �ر�����
		}

	}

	/**
	 * �����
	 * 
	 * @param command
	 * @return
	 */
	public String Getcommantvalue1(String[] command) {
		StringBuffer result = null;// ������
		StringBuffer realResult = new StringBuffer();// ������
		String tempRes = "";
		// ���ж��û��������Ƿ�Ϊnull���ǿյ��ַ�
		boolean connetflg = false;// ���ӱ�ʶ����
		boolean loginflg = false;
		// String Liginstr="";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String time = sdf.format(new Date());
		if (null != this.user && this.user.trim().length() > 0 && null != this.password && this.password.trim().length() > 0) {

			connetflg = this.connect();// telnet����
			// ���Ӳ��ɹ�����null
			if (connetflg) {// �ɹ�

				// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
				// /////////////////���ò�ͬ���˻����ͣ����в�ͬ���ַ���ʾ������ط���Ӱ�쵽����ִ���ٶ�
				// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
				String aa = readUntil(this.Loginsuuser); // �ȶ�ȡ
				write(user);

				aa = readUntil("Password:");
				write(password);
				aa = readUntil1(DEFAULT_PROMPT, "Password:");
				if (aa.equals("user or password error"))
					loginflg = false;
				else if (aa.equals(DEFAULT_PROMPT))
					loginflg = true;
				else if (aa.equals("Password:"))
					loginflg = false;
			}
		} else if (loginflg == false) {// �û����벻��ȷ����
			// System.out.println("=======1==");
			this.disconnect(); // �ر�����
			return "user or password error";
		}
		// System.out.println("");
		// �����¼�ɹ������ж�su���û�����Ŀ�����
		if (loginflg && null != suuser && suuser.trim().length() > 0 && null != supassword && supassword.trim().length() > 0) {

			// System.out.println("=====��ʼsu �û���¼");
			// readUntil(this.Loginsuuser); //�ȶ�ȡ
			// System.out.println("��ȡ��¼�ַ��Ƿ�ɹ�"+aa);
			readUntil1(">", "unit1 login");
			write(this.suuser);
			String suresult = readUntil1(this.Loginpsuassword, DEFAULT_PROMPT);
			if (suresult.equalsIgnoreCase(this.Loginpsuassword)) {
				write(this.supassword);
				suresult = readUntil(DEFAULT_PROMPT);// �������¼�ɹ�
			} else if (suresult.equalsIgnoreCase(DEFAULT_PROMPT)) {
				// suresult=readUntil(DEFAULT_PROMPT);// �Ѿ�Ϊ��������Ա
				loginflg = true;
			}

		} else if (loginflg == false) {
			this.disconnect(); // �ر�����
			return "user or password error";
		}

		// �����¼����������
		if (loginflg) {
			// String aa = this.readUntil(">");
			// out.println("\r\n");
			// out.flush();
			// String aa = readUntil1(">","unit1 login");
			if (command != null && command.length > 0) {
				for (int i = 0; i < command.length; i++) {
					result = new StringBuffer();
					String each = (String) command[i];
					write(each);

					readcommand(USER_PROMPT, SYS_PROMPT, result, each);
					String resultTemp = result.toString();
					if (null != resultTemp && !resultTemp.equals("user or password error")) {
						String[] st = resultTemp.split("\r\n");
						StringBuffer buff = new StringBuffer();
						buff.append("\r\n-----------------Date(" + time + ")-----------------\r\n");
						buff.append("-----------------begin(" + each + ")-----------------\r\n");
						for (int j = 1; j < st.length - 1; j++) {

							if (!st[j].startsWith("  ---- More ----")) {
								buff.append(st[j]).append("\r\n");
							}
						}
						buff.append("-----------------end(" + each + ")-----------------\r\n");
						realResult.append(buff.toString());

					}
				}
			}
			// result = this.sendCommand(command);
			// System.out.println("�ɼ����=======" + result);
		}

		this.disconnect(); // �ر�����
		tempRes = realResult.toString() + "";
		return tempRes;
	}

	/**
	 * 
	 * @param command
	 * @param list
	 * @param ip
	 * @return
	 */
	public void getCommantValue(String[] command, List<CmdResult> list, String ip) {
		StringBuffer result = null;// ������
		// ���ж��û��������Ƿ�Ϊnull���ǿյ��ַ�
		boolean connetflg = false;// ���ӱ�ʶ����
		boolean loginflg = false;
		// String Liginstr="";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String time = sdf.format(new Date());
		if ( null != this.password && this.password.trim().length() > 0) {

			connetflg = this.connect();// telnet����
			// ���Ӳ��ɹ�����null
			if (connetflg) {
				// ���Ӳ��ɹ�����null
				if (connetflg) {// �ɹ�
					// /////////////////���ò�ͬ���˻����ͣ����в�ͬ���ַ���ʾ������ط���Ӱ�쵽����ִ���ٶ�/////////////
					String[] patterns = { USER_PROMPT, SYS_PROMPT, this.Loginuser, Loginpassword, "Password:" };
					String[] pass_patterns = { "in unit1 login", USER_PROMPT, SYS_PROMPT, Loginpassword, "% Login failed!" };
					String aa = readUntil(patterns); // �ȶ�ȡ
					if (aa.equalsIgnoreCase("Password:")) {
						write(password);
						aa = readUntil(pass_patterns);
					}
					if (null != this.user && this.user.trim().length() > 0) {
						if (aa.equalsIgnoreCase("Username:")||aa.equalsIgnoreCase("login:")) {
							write(user);
							aa = readUntil("Password:");
							write(password);
							aa = readUntil(pass_patterns);
						}
					}

					if (aa.equals("% Login failed!") || aa.equals("user or password error")) {
						loginflg = false;
					} else if (aa.equals(USER_PROMPT) || aa.equals(SYS_PROMPT)) {
						loginflg = true;
					} else if (aa.equals("Password:")) {
						loginflg = false;
					} else if (aa.equals("in unit1 login")) {
						write("\r\n");
						loginflg = true;
					}

				}
			}
		} else if (loginflg == false) {// �û����벻��ȷ����
			this.disconnect(); // �ر�����
			CmdResult cmdResult = new CmdResult();
			cmdResult.setIp(ip);
			cmdResult.setCommand("---------");
			cmdResult.setResult("��¼ʧ��!");
			list.add(cmdResult);
		}
		// �����¼�ɹ������ж�su���û�����Ŀ�����
		if (loginflg && null != suuser && suuser.trim().length() > 0 && null != supassword && supassword.trim().length() > 0) {
			String[] patterns = { USER_PROMPT, SYS_PROMPT };
			write(this.suuser);
			readUntil(this.Loginpsuassword);
			write(this.supassword);

			readUntil(patterns);// �������¼�ɹ�

			loginflg = true;

		} else if (loginflg == false) {
			this.disconnect(); // �ر�����
			CmdResult cmdResult = new CmdResult();
			cmdResult.setIp(ip);
			cmdResult.setCommand("---------");
			cmdResult.setResult("��¼ʧ��!");
			list.add(cmdResult);
		}
		// �����¼����������

		if (loginflg) {
//			String aa = readUntil1(">", "unit1 login");
			if (command != null && command.length > 0) {

				for (int i = 0; i < command.length; i++) {
					boolean isSucess = true;
					result = new StringBuffer();
					CmdResult cmdResult = new CmdResult();
					String each = (String) command[i];
					write(each);
					String[] patterns = { USER_PROMPT, SYS_PROMPT,SU_PROMPT};

					readUntil(patterns);// �������¼�ɹ�
//					readcommand(USER_PROMPT, SYS_PROMPT,SU_PROMPT);
//					String resultTemp = result.toString();
					isSucess = true;
//					if (null != resultTemp && !resultTemp.equals("user or password error")) {
//						String[] st = resultTemp.split("\r\n");
//						for (int j = 0; j < st.length; j++) {
//
//							if (!st[j].startsWith("---- More ----")) {
//								if (st[j].indexOf("% Unrecognized command found at '^' position.") > -1 || st[j].indexOf("% Ambiguous command found at '^' position.") > -1 || st[j].indexOf("% Incomplete command found at '^' position.") > -1 || st[j].indexOf("Error:") > -1) {
//									cmdResult.setIp(ip);
//									cmdResult.setCommand(each);
//									cmdResult.setResult("ִ��ʧ��!");
//									isSucess = false;
//									break;
//								}
//							}
//						}
//
//					}
					if (isSucess) {

						cmdResult.setIp(ip);
						cmdResult.setCommand(each);
						cmdResult.setResult("ִ�����!");
					}
					list.add(cmdResult);
				}
			}
		}

		this.disconnect(); // �ر�����

	}

	/**
	 * 
	 * @description TODO
	 * @author wangxiangyong
	 * @date Feb 25, 2013 12:05:42 PM
	 * @return boolean
	 * @return
	 */
	public boolean login() {
		// ���ж��û��������Ƿ�Ϊnull���ǿյ��ַ�
		boolean connetflg = false;// ���ӱ�ʶ����
		boolean loginflg = false;
		connetflg = this.connect();// ��telnet�û���������Ϊ��ʱ��ֱ�Ӵ�������
		if (null != this.password && this.password.trim().length() > 0) {
			// ���Ӳ��ɹ�����null
			if (connetflg) {// �ɹ�
				// /////////////////���ò�ͬ���˻����ͣ����в�ͬ���ַ���ʾ������ط���Ӱ�쵽����ִ���ٶ�/////////////
				String[] patterns = { USER_PROMPT, SYS_PROMPT, this.Loginuser, Loginpassword, "Password:" };
				String[] pass_patterns = { "in unit1 login", USER_PROMPT, SYS_PROMPT, Loginpassword, "% Login failed!" };
				String aa = readUntil(patterns); // �ȶ�ȡ
				if (aa.equalsIgnoreCase("Password:")) {
					write(password);
					aa = readUntil(pass_patterns);
				}
				if (null != this.user && this.user.trim().length() > 0) {
					if (aa.equalsIgnoreCase("Username:")||aa.equalsIgnoreCase("login:")) {
						write(user);
						aa = readUntil("Password:");
						write(password);
						aa = readUntil(pass_patterns);
					}
				}

				if (aa.equals("% Login failed!") || aa.equals("user or password error")) {
					loginflg = false;
				} else if (aa.equals(USER_PROMPT) || aa.equals(SYS_PROMPT)) {
					loginflg = true;
				} else if (aa.equals("Password:")) {
					loginflg = false;
				} else if (aa.equals("in unit1 login")) {
					write("\r\n");
					loginflg = true;
				}

			}
		}
		// �����¼�ɹ������ж�su���û�����Ŀ�����
		if (loginflg && null != suuser && suuser.trim().length() > 0 && null != supassword && supassword.trim().length() > 0) {
			String[] patterns = { USER_PROMPT, SYS_PROMPT, SU_PROMPT, Loginpassword };
			
			write(suuser);
			String pass = readUntil(patterns);


			if (pass.equalsIgnoreCase("Password:"))
				write(this.supassword);
             System.out.println(pass+"==="+suuser+"-----"+supassword);
			String sign = readUntil(patterns);// �������¼�ɹ�
			if (sign.equals(SU_PROMPT) || sign.equals(SYS_PROMPT)) {
				loginflg = true;
			} 
		}
		return loginflg;
	}

	/**
	 * 
	 * @description �ֹ����������
	 * @author wangxiangyong
	 * @date Feb 25, 2013 3:16:44 PM
	 * @return String
	 * @param command
	 * @return
	 */
	public String getCommantValue(String[] command) {
		StringBuffer result = null;// ������
		StringBuffer realResult = new StringBuffer();// ������
		// ���ж��û��������Ƿ�Ϊnull���ǿյ��ַ�
		boolean loginflg = this.login();
		// �����¼����������
		if (loginflg) {
			if (command != null && command.length > 0) {
				for (int i = 0; i < command.length; i++) {
					result = new StringBuffer();
					String each = (String) command[i];
					write(each);
					String pattern1 = USER_PROMPT;
					String pattern2 = SYS_PROMPT;
					if (each.startsWith("dis")) {
						pattern1 = "return\r\n";
						pattern2 = "return\r\n";
					}
//					readcommand(pattern1, pattern2, result, each);
					String resultTemp = readcommand(pattern1, pattern2);

					if (null != resultTemp && !resultTemp.equals("user or password error")) {
						resultTemp = resultTemp.substring(resultTemp.indexOf(each));
						String[] st = resultTemp.split("\r\n");
						StringBuffer buff = new StringBuffer();
						for (int j = 0; j < st.length; j++) {
							if (j == st.length - 1) {
								buff.append(st[j].trim());
							} else if (!st[j].startsWith("  ---- More ----")) {
								buff.append(st[j]).append("\r\n");
							}
						}
						realResult.append(buff.toString());

					}
				}
			}
		}

		this.disconnect(); // �ر�����

		return realResult.toString();
	}

	/**
	 * �ֹ����������,���ؽ������
	 * 
	 * @param command
	 *            �����
	 * @return
	 */
	public String[] getCommantValues(String[] command) {
		String[] results = new String[command.length]; // �����
		StringBuffer result = null;// ������
		StringBuffer realResult = new StringBuffer();// ������
		// ���ж��û��������Ƿ�Ϊnull���ǿյ��ַ�
		boolean connetflg = false;// ���ӱ�ʶ����
		boolean loginflg = false;
		if (null != this.user && this.user.trim().length() > 0 && null != this.password && this.password.trim().length() > 0) {

			connetflg = this.connect();// telnet����
			// ���Ӳ��ɹ�����null
			if (connetflg) {// �ɹ�

				// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
				// /////////////////���ò�ͬ���˻����ͣ����в�ͬ���ַ���ʾ������ط���Ӱ�쵽����ִ���ٶ�
				// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
				String aa = readUntil(this.Loginsuuser); // �ȶ�ȡ
				write(user);
				aa = readUntil("Password:");
				write(password);
				aa = readUntil1(USER_PROMPT, "Password:");
				if (aa.equals("user or password error"))
					loginflg = false;
				else if (aa.equals(USER_PROMPT))
					loginflg = true;
				else if (aa.equals("Password:"))
					loginflg = false;
			}
		} else if (loginflg == false) {
			// �û����벻��ȷ����
			this.disconnect(); // �ر�����
			results = new String[] { "user or password error" };
			return results;
		}
		// �����¼�ɹ������ж�su���û�����Ŀ�����
		if (loginflg && null != suuser && suuser.trim().length() > 0 && null != supassword && supassword.trim().length() > 0) {
			write(this.suuser);
			readUntil(this.Loginpsuassword);
			write(this.supassword);

			readUntil(USER_PROMPT);// �������¼�ɹ�

			loginflg = true;

		} else if (loginflg == false) {
			this.disconnect(); // �ر�����
			results = new String[] { "user or password error" };
			return results;
		}

		// �����¼����������
		if (loginflg) {
			// String aa = readUntil1(">","unit1 login");
			if (command != null && command.length > 0) {
				for (int i = 0; i < command.length; i++) {
					realResult = new StringBuffer();
					result = new StringBuffer();
					String each = (String) command[i];
					// SysLogger.info("------------------------each:"+each);
					write(each);

					readcommand(USER_PROMPT, SU_PROMPT, result, each);
					String resultTemp = result.toString();
					if (null != resultTemp && !resultTemp.equals("user or password error")) {
						String[] st = resultTemp.split("\r\n");
						StringBuffer buff = new StringBuffer();
						for (int j = 0; j < st.length; j++) {
							if (j == st.length - 1) {
								buff.append(st[j].trim());
							} else if (!st[j].startsWith("  ---- More ----")) {
								buff.append(st[j]).append("\r\n");
							}
						}
						realResult.append(buff.toString());

					}
					// ��ӵ����������
					// SysLogger.info(realResult.toString());
					results[i] = realResult.toString();

				}
			}
		}

		this.disconnect(); // �ر�����

		return results;
	}

	// ���������Ƿ���ȷ
	public String isPasswordCorrect() {
		String result = "";// ������
		// ���ж��û��������Ƿ�Ϊnull���ǿյ��ַ�
		boolean connetflg = false;// ���ӱ�ʶ����
		boolean loginflg = false;
		// String Liginstr="";
		if (null != this.user && this.user.trim().length() > 0 && null != this.password && this.password.trim().length() > 0) {
			connetflg = this.connect();// telnet����
			// ���Ӳ��ɹ�����null
			if (connetflg) {// �ɹ�

				// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
				// /////////////////���ò�ͬ���˻����ͣ����в�ͬ���ַ���ʾ������ط���Ӱ�쵽����ִ���ٶ�
				// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
				// String aa = readUntil(this.Loginsuuser); // �ȶ�ȡ
				String aa = readUntil("Password:");
				write(password);
				aa = readUntil1(USER_PROMPT, "Password:");
				if (aa.equals("user or password error"))
					loginflg = false;
				else if (aa.equals(USER_PROMPT))
					loginflg = true;
				else if (aa.equals("Password:"))
					loginflg = false;
			}
		} else if (loginflg == false) {// �û����벻��ȷ����
			// System.out.println("=======1==");
			this.disconnect(); // �ر�����
			return "user or password error";
		}
		// System.out.println("");
		// �����¼�ɹ������ж�su���û�����Ŀ�����
		if (loginflg && null != suuser && suuser.trim().length() > 0 && null != supassword && supassword.trim().length() > 0) {
			// System.out.println("=====��ʼsu �û���¼");
			// readUntil(this.Loginsuuser); //�ȶ�ȡ
			// System.out.println("��ȡ��¼�ַ��Ƿ�ɹ�"+aa);
			write(this.suuser);
			readUntil(this.Loginpsuassword);
			write(this.supassword);
			readUntil1(USER_PROMPT, "");// �������¼�ɹ�//bug
			loginflg = true;
		} else if (loginflg == false) {
			this.disconnect(); // �ر�����
			return "su user or password error";
		}
		this.disconnect(); // �ر�����
		return result;
	}

	/**
	 * 
	 * �����޸���ͨ�û���su �û�������
	 * 
	 * @param modifyuser
	 *            �޸ĵ��û�
	 * @param threeA
	 *            �Ƿ���3a��֤
	 * @param newpassword
	 *            ��Ҫ�޸ĵ�����
	 * @param encrypt
	 *            ���ܷ�ʽ 0������ܣ�1��������
	 * @param newpassword
	 *            ������
	 * @return
	 */
	public boolean modifypassowd(String modifyuser, String newpassword, int encrypt, String threeA, String ostype) {
		// ���ж��û��������Ƿ�Ϊnull���ǿյ��ַ�
		boolean connetflg = false;// ���ӱ�ʶ����
		boolean loginflg = false;// �Ƿ��¼�ɹ�
		boolean threeaflg = false;// �Ƿ���3a����֤��ʽ
		// System.out.println("---modifyuser-----"+modifyuser+"--------------newpassword-------"+newpassword+"----------encrypt------------"+encrypt+"----threeA---"+threeA+"-----ostype----"+ostype);
		if (null != this.password && this.password.trim().length() > 0) {
			connetflg = this.connect();// telnet����
			// ���Ӳ��ɹ�����null
			if (connetflg) // �ɹ�
			{
				String aa = readUntil1(this.Loginsuuser, this.Loginpassword); // �ȶ�ȡ
				
				if (null != this.user && this.user.trim().length() > 0 &&aa.equalsIgnoreCase("Username:")) {
					write(user);
					aa = readUntil(this.Loginpassword);
					write(password);
				} else if (aa.equalsIgnoreCase("Password:")) {
					write(password);
				}
				aa = readUntil1(USER_PROMPT, "failed!");//
				if (aa.equals("user or password error"))
					loginflg = false;
				else if (aa.equals(USER_PROMPT))
					loginflg = true;
				else if (aa.equals("Password:"))
					loginflg = false;

			}

		} else if (loginflg == false) // �û����벻��ȷ����
		{
			this.disconnect(); // �ر�����
			return false;
		}
		// System.out.println("");
		// �����¼�ɹ������ж�su���û�����Ŀ�����
		if (loginflg && null != suuser && suuser.trim().length() > 0 && null != supassword && supassword.trim().length() > 0) {
			//readUntil(this.Loginsuuser); // �ȶ�ȡ

			write(this.suuser);
			readUntil(this.Loginpsuassword);
			write(this.supassword);
			readUntil1(USER_PROMPT, SU_PROMPT);// �������¼�ɹ�//bug
			loginflg = true;
		} else if (loginflg == false) {
			this.disconnect(); // �ر�����
			return false;
		}
		// �����¼����������
		String SYS_PROMPT="]";
		if (loginflg) {
			String aa = "";
//			write("disp version");
//			aa = readUntil1(USER_PROMPT,SU_PROMPT);
			write("system-view");
			aa = readUntil(SYS_PROMPT);
			if (null != threeA && threeA.trim().length() > 0) {
				write(threeA);
				threeaflg = true;
			}

			// encrypt ���ܷ�ʽ 0������ܣ�1��������
			if (encrypt == 0) {
				// write("local-user " + modifyuser + " password cipher " +
				// newpassword);
				write("local-user " + modifyuser);
				aa = readUntil1(USER_PROMPT,SYS_PROMPT);
				write("password cipher " + newpassword);
				aa=readUntil1(USER_PROMPT,SYS_PROMPT);
				if (aa.contains("Unrecognized command found")) {
					this.disconnect(); // �ر�����
					return false;
				}
				// System.out.println(aa);
			}

			if (encrypt == 1) {
				write("local-user " + modifyuser);
				aa=readUntil1(USER_PROMPT,SYS_PROMPT);
				write("password simple " + newpassword);
				aa=readUntil1(USER_PROMPT,SYS_PROMPT);
				if (aa.contains("Unrecognized command found")) {
					this.disconnect(); // �ر�����
					return false;
				}
				// System.out.println(aa);
			}

			if (threeaflg) {// �����3a��֤����Ҫ��һ���˳�
				write("quit");
			}
			write("quit");
			write("save");
			aa=readUntil1(USER_PROMPT,"N");
			// System.out.println(aa);
			write("y");
//			aa = readUntil1(USER_PROMPT,"key):");
//			write("\r\n");
//			aa = readUntil1(USER_PROMPT,"fully.");
			this.disconnect(); // �ر�����
			return true;
		}

		this.disconnect(); // �ر�����

		return true;

	}

	private String getVersion(String content) {
		String method = "";
		Hashtable hash = (Hashtable) ResourceCenter.getInstance().getCfgHash().get("h3c");
		// System.out.println("----------------1517------hash---------"+hash);
		Enumeration enu = hash.keys();
		while (enu.hasMoreElements()) {
			String s = (String) enu.nextElement();
			// System.out.println("----------------1521---------------"+s);
			// System.out.println("----------------content.contains(s)---------------"+content.contains(s));
			if (content.contains(s)) {
				method = (String) hash.get(s);
				// System.out.println("-------------1527--------------------------"+method);
				break;
			}
		}
		return method;
	}

	/**
	 * 
	 * @author GZM
	 */
	public boolean Modifypassowd(String modifyuser, String newpassword) {
		String result = "";// ������
		boolean isSuccess = false;
		String aa = null;
		isSuccess = this.connect();// telnet����

		// ���Ӳ��ɹ�����null
		if (isSuccess) // �ɹ�
		{
			isSuccess = false;
			aa = readUntil("Username:"); // �ȶ�ȡ
			if (!isContainInvalidateWords(aa)) {
				write(user);
				aa = readUntil("Password:");
				if (!isContainInvalidateWords(aa)) {
					write(password);
					boolean b = isLoginSuccess(">");
					if (b) {
						write("system-view");
						aa = readUntil("]");
						if (!isContainInvalidateWords(aa)) {
							write("local-user " + modifyuser);
							aa = readUntil("]");
							if (!isContainInvalidateWords(aa)) {
								write("password simple " + newpassword);
								aa = readUntil("]");
								if (!isContainInvalidateWords(aa)) {
									write("service-type telnet level 3");
									aa = readUntil("]");
									if (!isContainInvalidateWords(aa)) {
										write("quit");
										aa = readUntil("]");
										if (!isContainInvalidateWords(aa)) {
											write("user-interface vty 0 4");
											aa = readUntil("]");
											if (!isContainInvalidateWords(aa)) {
												write("authentication-mode scheme");
												aa = readUntil("]");
												if (!isContainInvalidateWords(aa)) {
													write("quit");
													aa = readUntil("]");
													if (!isContainInvalidateWords(aa)) {
														write("quit");
														isSuccess = true;
														/*
														 * aa = readUntil(">");
														 * if(!isContainInvalidateWords(aa)) {
														 * write("save"); aa =
														 * readUntil(">");
														 * if(!isContainInvalidateWords(aa)) {
														 * out.write(89);//y
														 * out.write(13);//�س���
														 * aa = readUntil(":");
														 * out.write(13);//�س���
														 * //aa =
														 * readUntil(">");
														 * isSuccess = true; } }
														 */
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		this.disconnect(); // �ر�����
		return isSuccess;
	}

	private boolean isContainInvalidateWords(String content) {
		boolean isContained = false;
		if (content.contains("failed") || content.contains("Unknown")) {
			isContained = true;
		}
		return isContained;
	}

	public int getDEFAULT_TELNET_PORT() {
		return DEFAULT_TELNET_PORT;
	}

	public void setDEFAULT_TELNET_PORT(int default_telnet_port) {
		DEFAULT_TELNET_PORT = default_telnet_port;
	}

	public String getSuuser() {
		return suuser;
	}

	public void setSuuser(String suuser) {
		this.suuser = suuser;
	}

	public String getSupassword() {
		return supassword;
	}

	public void setSupassword(String supassword) {
		this.supassword = supassword;
	}

	public TelnetClient getTelnet() {
		return telnet;
	}

	public void setTelnet(TelnetClient telnet) {
		this.telnet = telnet;
	}

	public String getDEFAULT_PROMPT() {
		return DEFAULT_PROMPT;
	}

	public void setDEFAULT_PROMPT(String default_prompt) {
		DEFAULT_PROMPT = default_prompt;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public static void main(String[] args) {
		Huawei3comvpn tvpn = new Huawei3comvpn();
		tvpn.setSuuser("");// su
		tvpn.setSupassword("");// su����
		tvpn.setUser("1");// �û�
		tvpn.setPassword("1");// ����
		tvpn.setIp("10.10.151.176");// ip��ַ
		tvpn.setDEFAULT_PROMPT(">");// ������Ƿ���
		tvpn.setPort(23);
		if (tvpn.Modifypassowd("1", "1")) {
			System.out.println("success");
		}
	}

	public String t(String pattern) {
		try {
			char lastChar = pattern.charAt(pattern.length() - 1);
			StringBuffer sb = new StringBuffer();
			char ch = (char) in.read();
			while (true) {
				System.out.print(ch + "@" + (int) ch);
				sb.append(ch);
				if (ch == lastChar) {
					if (sb.toString().endsWith(pattern)) {
						return sb.toString();
					}
				}
				if (sb.toString().endsWith("--More--")) {
					write((char) 32 + "");
				}
				if (ch == 8) {
					sb = sb.deleteCharAt(sb.length() - 1);
					sb = sb.deleteCharAt(sb.length() - 1);
				}
				ch = (char) in.read();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
