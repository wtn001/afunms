package com.afunms.common.base;

import java.io.InputStream;
import java.io.PrintStream;

import org.apache.commons.net.telnet.TelnetClient;

import com.afunms.common.util.SysLogger;

/**
 * ����������¼����telnet ��¼·������������ ʵ�ֵ�¼����������أ��������Ϣ ��Ҫ�ɼ���ɺ�һ��Ҫ����
 * disconnect�����������ر�telnet���� ʵ�ֻ�ȡ�����ļ���ʵ��д�����ļ���
 * 
 * @author wxy
 * @version Dec 10, 2011 7:35:28 PM
 */
public class BaseTelnet {
	private TelnetClient telnet = new TelnetClient();

	protected InputStream in; // 
	protected PrintStream out;
	protected String prompt = ">";
	protected final String USER_PROMPT = ">";// һ���û���ʶ��
	protected  String SU_PROMPT = "#";// �����û���ʶ��
	protected  String SYS_PROMPT = "]";// �����û���ʶ��
	protected final String USERSING = "Username:";// �û�����ʶ
	protected final String PWDSING = "Password:";// �����ʶ
	protected String Loginuser = "Username:";// �û�
	protected String Loginpassword = "Password:";// �û�������
	private int nummber = 20000; // ����һ����ȡ�ַ�������
	protected int port = 23; // telnet �˿�
	protected String user = ""; // �û���
	protected String password = ""; // ����
	protected String suuser = ""; // ��������Ա
	protected String supassword = "";// ��������Ա���롢
	protected String ip = ""; // IP ��ַ
	protected int DEFAULT_TELNET_PORT = 23;// ȱʡ�˿�

	/**
	 * Ĭ�Ϲ��췽��
	 */
	public BaseTelnet() {

	}

	/**
	 * ���������췽��
	 */
	public BaseTelnet(String ip, String user, String password, int port, String suuser, String supassword, String defaule) {
		this.ip = ip;
		this.port = this.getDEFAULT_TELNET_PORT();
		this.user = user; // Ĭ���û�
		this.password = password; // Ĭ���û�����
		this.supassword = supassword;// su�û�
		this.suuser = suuser;// su�û�������
		this.prompt = defaule;// ϵͳĬ�ϱ�ʶ����
		this.DEFAULT_TELNET_PORT = port;// �˿ں�
	}

	/**
	 * ����·����
	 * 
	 * @return boolean ���ӳɹ�����true�����򷵻�false
	 */
	public boolean connect() {

		boolean isConnect = true;

		try {
			telnet.setDefaultTimeout(5000);// ���ô�δ���ӵ�����״̬�ĳ�ʱʱ��
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

	public boolean userLogin() {
		boolean isLogin = false;
		try {
			if (null != this.password && this.password.trim().length() > 0) {
				// ���Ӳ��ɹ�����null
				// /////////////////���ò�ͬ���˻����ͣ����в�ͬ���ַ���ʾ������ط���Ӱ�쵽����ִ���ٶ�/////////////
				String[] patterns = { USER_PROMPT, SU_PROMPT, Loginuser, Loginpassword, "password:", "login:" };
				String aa = readUntil(patterns); // �ȶ�ȡ
				if (aa.endsWith("Password:") || aa.endsWith("password:")) {
					write(password);
					aa = readUntil(patterns);
				}
				if (null != this.user && this.user.trim().length() > 0) {
					if (aa.endsWith("Username:") || aa.endsWith("login:")) {
						write(user);
						aa = readUntil("Password:", "password:");
						write(password);
						aa = readUntil(patterns);
					}
				}
				if (aa.indexOf("% Login failed!") > 0 || aa.indexOf("user or password error") > 0) {
					isLogin = false;
				} else if (aa.equals(USER_PROMPT) || aa.equals(SU_PROMPT)) {
					isLogin = true;
				} else if (aa.endsWith(":")) {
					isLogin = false;
				} else if (aa.equals("in unit1 login")) {
					write("\r\n");
					isLogin = true;
				}
			}

		} catch (Exception e) {
			isLogin = false;
			this.disconnect();
			e.printStackTrace();
		}
		return isLogin;
	}

	/**
	 * ��֤��������Ա��¼
	 * 
	 * @return
	 */
	public boolean suLogin() {

		boolean result = false;
		String[] patterns = { USER_PROMPT, SU_PROMPT, "Password:", "password:" };
		if (null != suuser && suuser.trim().length() > 0 && null != supassword && supassword.trim().length() > 0) {
			write(suuser);
			String pass = readUntil(patterns);

			if (pass.equalsIgnoreCase("Password:") || pass.equalsIgnoreCase("password:"))
				write(this.supassword);

			String aa = readUntil(patterns);// �������¼�ɹ�
			if (aa.equals(SU_PROMPT)) {
				result = true;
			}
		}
		return result;

	}

	/**
	 * ��֤��ͨ�û��ĵ�¼
	 * 
	 * @return
	 */
	public boolean siglelogin() {
		boolean flag = false;
		flag = this.connect();
		if (flag) {
			flag = this.userLogin();
		}
		return flag;
	}

	/**
	 * ��֤�����û��ĵ�¼
	 * 
	 * @return
	 */
	public boolean tologin() {
		boolean flag = false;
		flag = this.connect();
		if (flag) {
			flag = this.userLogin();
			if (flag) {
				flag = this.suLogin();
			}
		}
		return flag;
	}

	/**
	 * ��֤�����û��ĵ�¼
	 * 
	 * @return
	 */
	public String login() {
		String msg = "";
		boolean flag = false;
		// flag = this.connect();
		// if (flag) {// true:���ӳɹ�
		// flag = this.userLogin();
		// if (flag) {
		// flag = this.suLogin();
		// if (flag) {
		// msg = "�û�/����Ա��¼�ɹ�";
		// } else {
		// msg = "�û���¼�ɹ�";
		// }
		// } else {
		// msg = "�û���¼ʧ��";
		// }
		// } else {
		// msg = "����ʧ��";
		// }

		boolean isLogin = false;
		try {

			boolean connetflg = this.connect();
			String[] patterns = { USER_PROMPT, SU_PROMPT, Loginuser, Loginpassword, "Password:", "password:", "login:" };
			if (connetflg) {// �ɹ�
				if (null != this.password && this.password.trim().length() > 0) {
					// ���Ӳ��ɹ�����null
					// /////////////////���ò�ͬ���˻����ͣ����в�ͬ���ַ���ʾ������ط���Ӱ�쵽����ִ���ٶ�/////////////

					String aa = readUntil(patterns); // �ȶ�ȡ
					if (aa.endsWith("Password:") || aa.endsWith("password:")) {
						write(password);
						aa = readUntil(patterns);
					}
					if (null != this.user && this.user.trim().length() > 0) {
						if (aa.endsWith("Username:") || aa.endsWith("login:")) {
							write(user);
							aa = readUntil("Password:", "password:");
							write(password);
							aa = readUntil(patterns);
						}
					}
					if (aa.indexOf("% Login failed!") > 0 || aa.indexOf("user or password error") > 0) {
						isLogin = false;
						msg = "�û���¼ʧ��";
					} else if (aa.endsWith(USER_PROMPT) || aa.endsWith(SU_PROMPT)) {
						isLogin = true;
						msg = "�û���¼�ɹ�";
					} else if (aa.endsWith(":")) {
						isLogin = false;
						msg = "�û���¼ʧ��";
					} else if (aa.endsWith("in unit1 login")) {
						write("\r\n");
						isLogin = true;
						msg = "�û���¼�ɹ�";
					}
				}
				// �����¼�ɹ������ж�su���û�����Ŀ�����
				String aa = "";
				if (isLogin && null != suuser && suuser.trim().length() > 0 && null != supassword && supassword.trim().length() > 0) {
					write(suuser);
					String pass = readUntil(patterns);

					if (pass.equalsIgnoreCase("Password:") || pass.equalsIgnoreCase("password:"))
						write(this.supassword);

					aa = readUntil(patterns);// �������¼�ɹ�
					if (aa.equals(SU_PROMPT)) {
						msg = "�û�/����Ա��¼�ɹ�";
					} else if (aa.equals(USER_PROMPT)) {
						msg = "�����û���¼ʧ��";
					}
				}

			} else {
				msg = "����ʧ��";
			}

		} catch (Exception e) {
			isLogin = false;
			this.disconnect();
			e.printStackTrace();
		}

		return msg;

	}

	/**
	 * 
	 * @description �޸��豸����
	 * @author wangxiangyong
	 * @date Apr 24, 2013 1:14:59 PM
	 * @return boolean
	 * @param devType
	 *            �豸����
	 * @param newUser
	 * @param newPasswd
	 *            ������
	 * @return
	 */
	public boolean modifyDevPasswd(String devType, String newUser, String newPasswd, String encrypt) {
		boolean isSuccess = false;
		try {
			String cmd = "";
			if (devType.equals("zte")) {

				String temp = sendCommand("conf t");
				cmd = "set login-password " + newPasswd;
				sendCommand(cmd);
				temp = sendCommand(cmd);
				if (!isContainInvalidateWords(temp)) {
					isSuccess = true;
				}
			} else if (devType.equals("redgiant")) {
				String temp = sendCommand("conf t");
				cmd = "username " + newUser + " password " + newPasswd;
				temp = sendCommand(cmd);
				if (!isContainInvalidateWords(temp)) {
					isSuccess = true;
				}
			} else if (devType.equals("huawei")) {
				String temp = sendHCommand("system-view");
				cmd = "user-interface console 0";
				temp = sendHCommand(cmd);
				if (!isContainInvalidateWords(temp)) {
					temp = sendHCommand("user-interface vty 0 4");
					if (encrypt != null && encrypt == "0") {
						encrypt = "cipher";
					} else {
						encrypt = "simple";
					}
					;
					cmd = "set authentication password " + encrypt + " " + newPasswd;
					temp = sendHCommand(cmd);

					if (!isContainInvalidateWords(temp)) {
						isSuccess = true;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}
		return isSuccess;
	}

	private boolean isContainInvalidateWords(String content) {
		boolean isContained = false;
		if (content.contains("invalid") || content.contains("Unknown") || content.contains("% Ambiguous command")) {
			isContained = true;
		}
		return isContained;
	}

	/**
	 * ��������
	 * 
	 * @param command
	 * @return
	 */
	public String sendCommand(String command) {
		try {
			write(command);
			return readcommand(USER_PROMPT, SU_PROMPT);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * ��������
	 * 
	 * @param command
	 * @return
	 */
	public String sendHCommand(String command) {
		try {
			write(command);
			return readcommand(USER_PROMPT, SYS_PROMPT);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/*
	 * ��ȡ��Ϣ @param pattern �������� @return ���������Ӧ���ַ������ַ����������н�������û�ж����򷵻�null
	 */
	public String readUntil(String pattern) {

		StringBuffer sb = new StringBuffer();
		try {

			char lastChar = pattern.charAt(pattern.length() - 1);

			char ch = (char) in.read();
			int n = 0;

			boolean flag = true;
			while (flag) {

				if (ch == 0 || ch == 13 || ch == 10 || (ch >= 32)) {
					sb.append(ch);
				}
				if (ch == lastChar) {
					if (sb.toString().endsWith(pattern)) {
						return pattern;
					}
				}
				ch = (char) in.read();
				if (sb.toString().endsWith(" --More-- ")) {
					out.write(32);
					out.flush();
				}
				n++;
				if (n > this.nummber) {// �����ȡ���ַ���������2����ַ�����û����ȷ
					flag = false;
					return "time out";
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			return "0";
		}
		return sb.toString() + "";
	}

	/*
	 * �ṩ�����ַ�������Ϊ���������ж� ����ִ�к󣬿��ܷ��سɹ���Ҳ���ܷ���ʧ�ܡ��ṩ�����ַ������ܿ���ʶ�������Ƿ�ִ�н������ӿ����ִ��Ч��
	 */
	public String readUntil(String pattern1, String pattern2) {
		StringBuffer sb = new StringBuffer();
		try {
			char ch = (char) in.read();
			boolean flag = true;
			int n = 0;
			while (flag) {

				if (ch == 0 || ch == 13 || ch == 10 || (ch >= 32)) {
					sb.append(ch);
				}
				if (sb.toString().endsWith(pattern1)) // �涨�������pattern1��β��˵������ִ�гɹ�
				{
					return pattern1;
				}
				// }
				if (sb.toString().endsWith(pattern2)) // �����pattern1��β��˵������ִ�гɹ�
				{
					return pattern2;
				}
				// }
				if (sb.toString().endsWith("in unit1 login")) // �����pattern1��β��˵������ִ�гɹ�
				{
					return "in unit1 login";
				}
				if (sb.toString().endsWith(" --More-- ")) {
					out.write(32);
					out.flush();
				}
				ch = (char) in.read();

				n++;
				if (n > this.nummber) {// �����ȡ���ַ���������2����ַ�����û����ȷ
					flag = false;
					return "time out";
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
		return sb.toString();
	}

	/*
	 * �ṩ�����ַ�������Ϊ���������ж� ����ִ�к󣬿��ܷ��سɹ���Ҳ���ܷ���ʧ�ܡ��ṩ�����ַ������ܿ���ʶ�������Ƿ�ִ�н������ӿ����ִ��Ч��
	 */
	public String readUntil(String pattern1, String pattern2, String pattern3) {
		StringBuffer sb = new StringBuffer();
		try {
			char ch = (char) in.read();
			boolean flag = true;
			int n = 0;
			while (flag) {

				if (ch == 0 || ch == 13 || ch == 10 || (ch >= 32)) {
					sb.append(ch);
				}

				if (sb.toString().endsWith(pattern1)) // �涨�������pattern1��β��˵������ִ�гɹ�
				{
					return pattern1;
				} else if (sb.toString().endsWith(pattern2)) // �����pattern3��β��˵������ִ�гɹ�
				{
					return pattern2;
				} else if (sb.toString().endsWith(pattern3)) // �����pattern3��β��˵������ִ�гɹ�
				{
					return pattern3;
				}

				if (sb.toString().endsWith("in unit1 login")) // �����pattern1��β��˵������ִ�гɹ�
				{
					return "in unit1 login";
				}
				if (sb.toString().endsWith(" --More-- ")) {
					out.write(32);
					out.flush();
				}
				ch = (char) in.read();

				n++;
				if (n > this.nummber) {// �����ȡ���ַ���������2����ַ�����û����ȷ
					flag = false;
					return "time out";
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
		return sb.toString();
	}

	/*
	 * �ṩ����ַ�������Ϊ���������ж� ����ִ�к󣬿��ܷ��سɹ���Ҳ���ܷ���ʧ�ܡ��ṩ�����ַ������ܿ���ʶ�������Ƿ�ִ�н������ӿ����ִ��Ч��
	 */
	public String readUntil(String pattern, String[] patterns) {

		StringBuffer sb = new StringBuffer();
		int length = 0;
		try {

			char lastChar = pattern.charAt(pattern.length() - 1);

			char ch = (char) in.read();
			int n = 0;

			boolean flag = true;
			while (flag) {

				if (ch == 0 || ch == 13 || ch == 10 || (ch >= 32)) {
					sb.append(ch);
				}
				for (int i = 0; i < patterns.length; i++) {
					length = sb.toString().trim().length();
					if (length >= patterns[i].length() && sb.toString().indexOf((patterns[i])) > -1) {
						flag = false;
						return patterns[i];
					}
				}
				if (ch == lastChar) {
					if (sb.toString().endsWith(pattern)) {
						return pattern;
					}
				}

				ch = (char) in.read();
				if (sb.toString().endsWith(" --More-- ")) {
					out.write(32);
					out.flush();
				}
				n++;
				if (n > this.nummber) {// �����ȡ���ַ���������2����ַ�����û����ȷ
					flag = false;
					return "time out";
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			return "0";
		}
		return sb.toString() + "";

	}

	/*
	 * �ṩ����ַ�������Ϊ���������ж� ����ִ�к󣬿��ܷ��سɹ���Ҳ���ܷ���ʧ�ܣ��ܿ���ʶ�������Ƿ�ִ�н������ӿ����ִ��Ч��
	 */
	public String readUntil(String[] patterns) {
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

	/**
	 * 
	 * @description ���������ȡ���ص���Ϣ
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
					if (ret.toString().indexOf(" --More-- ") > 0 || ret.toString().toLowerCase().indexOf("---- more ----") > 0) {
						out.write(32);
						out.flush();
					}
					if (ret.toString().endsWith(pattern1)) {

						return ret.toString();
					}
					if (ret.toString().endsWith(pattern2)) {
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

	/**
	 * 
	 * @description ��������ϵĽ��
	 * @author wangxiangyong
	 * @date Feb 25, 2013 11:11:50 AM
	 * @return String
	 * @param command
	 * @return
	 */
	public String getCommantValue(String[] command) {

		// setPrompt("Password:");
		// sendCommand("en");
		// setPrompt("#");
		// sendCommand(enPasswd);
		String result = "";
		StringBuffer sb = new StringBuffer();
		try {
			for (int i = 0; i < command.length; i++) {
				write(command[i]);
				result = readcommand(USER_PROMPT, SU_PROMPT);
				if (null != result && !result.equals("user or password error")) {
					String[] st = result.split("\r\n");
					StringBuffer buff = new StringBuffer();
					for (int j = 0; j < st.length; j++) {
						if (j == st.length - 1) {
							buff.append(st[j]);
						} else if (!st[j].toString().toLowerCase().contains("--more--") || !st[j].toString().toLowerCase().contains("---- more ----")) {
							buff.append(st[j]).append("\r\n");
						}
					}

					result = buff.toString();

				}
				sb.append(result);
			}

		} catch (Exception e) {
			SysLogger.error("BaseTelnet.getCommantValue( String[] command) error", e);
			e.printStackTrace();
		} finally {
			disconnect();
		}

		return result;
	}

	/**
	 * ����ָ��
	 * 
	 * @param value
	 */
	public void write(String value) {
		try {
			out.println(value);
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * �ر�����
	 */
	public void disconnect() {
		try {
			if (in != null) {
				in.close();
			}
			if (out != null) {
				out.close();
			}
			if (telnet != null && telnet.isConnected()) {
				telnet.disconnect();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getPrompt() {
		return prompt;
	}

	public void setPrompt(String prompt) {
		this.prompt = prompt;
	}

	public int getDEFAULT_TELNET_PORT() {
		return DEFAULT_TELNET_PORT;
	}

	public void setDEFAULT_TELNET_PORT(int default_telnet_port) {
		DEFAULT_TELNET_PORT = default_telnet_port;
	}
}
