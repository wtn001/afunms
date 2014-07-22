package com.afunms.polling.telnet;

import java.io.BufferedInputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.net.telnet.TelnetClient;

import com.afunms.common.base.BaseTelnet;
import com.afunms.common.util.ShareData;
import com.afunms.common.util.SysLogger;
import com.afunms.config.dao.HaweitelnetconfDao;
import com.afunms.config.model.CmdResult;
import com.afunms.config.model.Huaweitelnetconf;
import com.afunms.slaaudit.dao.SlaAuditDao;
import com.afunms.slaaudit.model.SlaAudit;
import com.afunms.system.model.User;

/**
 * @descrition 本来用来登录连接telnet 登录锐捷路由器 实现登录后输入命令返回，命令的信息
 *             主要采集完成后一定要调用disconnect（）方法来关闭telnet连接 实现获取配置文件，实现写配置文件
 * @author wangxiangyong
 * @date Aug 10, 2012 2:51:35 PM
 */
public class RedGiantTelnet extends BaseTelnet {
	private static String[] errors = { "Are you sure to delete all of them?[yes/no]:", "% Unrecognized command", "% Ambiguous command:", "% Incomplete command.",
			"% Invalid input detected at '^' marker.", "#" };

	public RedGiantTelnet() {
		super();
	}

	public RedGiantTelnet(String ip, String user, String password, int port, String suuser, String supassword, String defaule) {
		super(ip, user, password, port, suuser, supassword, defaule);
	}

	public void executeRedGiantCommands(List<CmdResult> list, String ip, User operator, String[] commands) {
		boolean isSuccess = siglelogin();
		CmdResult cmdResult = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String admin = "";
		String tag = "";
		if (isSuccess) {
			try {

				String result = "";
				HaweitelnetconfDao dao = new HaweitelnetconfDao();
				Huaweitelnetconf vo = null;
				try {
					vo = (Huaweitelnetconf) dao.loadByIp(ip);
					write("configure terminal");// 进入全局模式
					result = this.readUntil(errors);
					if (result.endsWith(prompt)) {
						String successSign = prompt;
						for (int i = 0; i < commands.length; i++) {
							cmdResult = new CmdResult();
							cmdResult.setIp(ip);
							cmdResult.setCommand(commands[i]);
							cmdResult.setTime(sdf.format(date));
							if (successSign.endsWith(prompt)) {
								write(commands[i]);
								successSign = this.readUntil(errors);
								if (successSign.endsWith(prompt)) {
									cmdResult.setResult("执行成功!");
								} else if (successSign.endsWith(errors[0])) {
									write("Y");
									successSign = this.readUntil(errors);
									cmdResult.setResult("执行成功!");
								} else {
									cmdResult.setResult("执行失败!");

								}
							} else {
								cmdResult.setResult("执行失败!");
								isSuccess = false;
							}

							list.add(cmdResult);
						}
					} else {
						cmdResult = new CmdResult();
						cmdResult.setIp(ip);
						cmdResult.setCommand("-------");
						cmdResult.setResult("操作失败!");
						cmdResult.setTime(sdf.format(date));
						isSuccess = false;
						list.add(cmdResult);
					}

				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					dao.close();
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			cmdResult = new CmdResult();
			cmdResult.setIp(ip);
			cmdResult.setCommand("-------");
			cmdResult.setResult("登录失败!");
			cmdResult.setTime(sdf.format(date));
			list.add(cmdResult);
		}
		Huaweitelnetconf vo = null;
		HaweitelnetconfDao dao = null;
		try {
			dao = new HaweitelnetconfDao();
			vo = (Huaweitelnetconf) dao.loadByIp(ip);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dao.close();
		}
		// 操作审计
		SlaAudit slaaudit = new SlaAudit();
		slaaudit.setUserid(operator.getId());
		slaaudit.setSlatype("vpn");
		slaaudit.setTelnetconfigid(vo.getId());
		slaaudit.setOperation("add");
		slaaudit.setDotime(sdf.format(date));
		if (isSuccess) {
			slaaudit.setDostatus(1);
		} else {
			slaaudit.setDostatus(0);
		}
		StringBuffer content = new StringBuffer();
		for (int i = 0; i < commands.length; i++) {
			String temp = commands[i];
			content.append(temp).append("\r\n");
		}
		slaaudit.setCmdcontent(content.toString());

		SlaAuditDao slaauditdao = new SlaAuditDao();
		try {
			slaauditdao.save(slaaudit);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			slaauditdao.close();
		}
		disconnect();
	}

	public String getFileCfg(String[] commands) {
		String error = "#";
		StringBuffer result = new StringBuffer();
		if (commands != null && commands.length > 0) {

			for (int i = 0; i < commands.length; i++) {
				String each = (String) commands[i];
				write(each);
				String temp = this.readUntil(error);
				result.append(temp);
			}
		}
		return result.toString();
	}

	/*
	 * 重写提供多个字符串，作为流结束的判断 命令执行后，可能返回成功，也可能返回失败，能快速识别命令是否执行结束，加快程序执行效率
	 */
	public String readUntil(String[] patterns) {

		StringBuffer sb = new StringBuffer();
		try {
			if (patterns != null && patterns.length > 0) {
				char[] lastChars = new char[patterns.length];
				for (int i = 0; i < patterns.length; i++) {
					lastChars[i] = patterns[i].charAt(patterns[i].length() - 1);
				}

				char ch = (char) in.read();
				int n = 0;
				boolean flag = true;
				while (flag) {

					if (ch == 0 || ch == 13 || ch == 10 || (ch >= 32)) {
						sb.append(ch);
					}
					for (int i = 0; i < lastChars.length; i++) {
						if (ch == lastChars[i]) {
							if (sb.toString().endsWith(patterns[i])) {
								return sb.toString();
							}
						}

					}
					if (sb.toString().endsWith("--More--")) {
						out.write(32);
						out.flush();
					}
					ch = (char) in.read();

					n++;
					if (n > 100000) {// 如果读取的字符个数大于1万个字符就认没有正确
						flag = false;
						return "time out";
					}

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "0";
		}
		return sb.toString() + "";

	}

	public String readUntil(String pattern) {
		int count = 0;
		StringBuffer sb = new StringBuffer(20000);
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
						flag = false;
						return sb.toString();
					}
				}

				if (sb.toString().endsWith("--More--")) {
					// if(++count>15)return sb.toString();
					sb.delete(sb.toString().length() - 9, sb.toString().length());
					out.write(32);
					out.flush();
				}
				ch = (char) in.read();
				n++;
				if (n > 100000) {// 如果读取的字符个数大于10万个字符就认为没有正确
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
	
	/**
	 * 
	 * @description 获取配置文件信息
	 * @author wangxiangyong 
	 * @date Feb 25, 2013 9:15:28 AM
	 * @return String[]  
	 * @param bkptype
	 * @return
	 */
	public String[] getCfg(String bkptype) {
		
		
		String result1 = "";
		// setPrompt("Password:");
		String result2 = "";
		String[] result = new String[2];
		try {
			
		
		if (bkptype.equals("run")) {
			result1 = sendCommand("show run");
		} else if (bkptype.equals("startup")) {
			result1 = sendCommand("show startup");
		} else {
			result1 = sendCommand("show run");
			disconnect();
			if (super.tologin()) {
			//	Thread.sleep(500);
				result2 = sendCommand("show startup");
			}
			
		
		}
		// 对结果进行格式化
		if (null != result1 && !result1.equals("")) {
			String[] st = result1.split("\r\n");
			StringBuffer buff = new StringBuffer();
			for (int i = 1; i < st.length - 1; i++) {

				if (!st[i].toLowerCase().contains("--more--")) {
					buff.append(st[i]).append("\r\n");
				}
			}
			result1 = buff.toString();

		}
		if (null != result2 && !result2.equals("")) {
			String[] st = result2.split("\r\n");
			StringBuffer buff = new StringBuffer();
			for (int i = 1; i < st.length - 1; i++) {

				if (!st[i].toLowerCase().contains("--more--")) {
					buff.append(st[i]).append("\r\n");
				}
			}
			result2 = buff.toString();

		}
		result[0] = result1;
		result[1] = result2;
		} catch (Exception e) {
			SysLogger.error("RedGiantTelnet.getCfg(String bkptype) error",e);
		}finally{
			disconnect();
		}
		
		return result;
	}
	/**
	 * 
	 * @description 执行无返回结果的命令集合
	 * @author wangxiangyong
	 * @date Apr 22, 2013 1:42:16 PM
	 * @return void  
	 * @param commands
	 */
	public void sendCommands(String[] commands) {
		try {
			for (int i = 0; i < commands.length; i++) {
				sendCommand(commands[i]);
			}

			// return readUntil(prompt);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	public String  sendSuPwd(String command) {
		write(command);
		return readSuUntil(prompt);
	}
	
	public String readSuUntil(String pattern) 
	{
		try {
			char lastChar = pattern.charAt(pattern.length() - 1);
			StringBuffer sb = new StringBuffer();
			char ch = (char) in.read();

			while (true) 
			{
				
				sb.append(ch);
				if (sb.toString().indexOf("Password:")>-1) {
					disconnect();
					return "user or password error";
				
				}
				if(sb.toString().toLowerCase().endsWith(" --more-- "))
				{
					out.write(32);
					out.flush();
				}
				
				if (ch == lastChar) {
					if (sb.toString().endsWith(pattern)) 
					{
						
						return sb.toString();
					}
				}
				ch = (char) in.read();
				
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String getFileCfgRed(String enPasswd,String[] content)
	{
		String result = "";
		
		setPrompt("Password:");
		sendCommand("en");
		setPrompt("#");
		String enPassword=sendSuPwd(enPasswd);
//		System.out.println("-------getFileCfg---------"+enPassword);
		if(enPassword.equals("user or password error"))
			return enPassword;
		//result = sendCommand(content);
		System.out.println(":"+enPassword+":");
		// 对结果进行格式化
//		if (null != result && !result.equals("user or password error")) {
//			String[] st = result.split("\r\n");
//			StringBuffer buff = new StringBuffer();
//			for (int i = 1; i < st.length-1; i++) { 
//				
//				if(!st[i].contains("--More--"))
//				{
//					buff.append(st[i]).append("\r\n");
//				}
//			}
//			result = buff.toString();
//			
//		}
		disconnect();
		return result;
	}

	}