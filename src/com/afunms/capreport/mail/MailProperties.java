/**
 * @author sunqichang/������
 * Created on 2011-5-12 ����02:05:32
 */
package com.afunms.capreport.mail;

import java.io.FileInputStream;
import java.util.Properties;

/**
 * mail���Զ�ȡ
 * 
 * @author sunqichang/������
 * 
 */
public final class MailProperties {
	private static String sender; // �����˵�ַ

	private static String smtpHost; // �ʼ����ͷ�������smtp��

	private static String user; // ��¼�û���

	private static String password; // ��¼����

	private static String authentication; // �Ƿ���Ҫ�����֤

	private static String port; // smtp�˿�

	private static Properties mailproperties;

	/**
	 * �ʼ�������Ϣ��ȡ
	 * 
	 * @param mail
	 */
	public static void MailConfig(String mailPath) {
		try {
			mailproperties = new Properties();
			Properties properties = new Properties();
			FileInputStream stream = new FileInputStream(mailPath);
			if (stream != null) {
				properties.load(stream);
				smtpHost = properties.getProperty("mail.smtp.host");
				sender = properties.getProperty("mail.from");
				authentication = properties.getProperty("mail.smtp.auth");
				user = properties.getProperty("mail.smtp.user");
				password = properties.getProperty("mail.smtp.password");
				port = properties.getProperty("mail.smtp.port");
				mailproperties.put("mail.smtp.host", smtpHost); // ����smtp����
				mailproperties.put("mail.from", sender); // ���÷�������
				mailproperties.put("mail.smtp.auth", authentication); // ����smtp�����֤
				mailproperties.put("mail.smtp.user", user); // �����û�
				mailproperties.put("mail.smtp.password", password); // ���ÿ���
				mailproperties.put("mail.smtp.port", port); // ����smtp�˿�
			} else {
				System.out.println(mailPath + "is null!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * ��ȡ�ʼ�������Ϣ
	 * 
	 * @param mailPath
	 *            �ʼ������ļ�·��
	 * @return
	 */
	public static Properties getMailproperties(String mailPath) {
		if (mailproperties == null) {
			MailConfig(mailPath);
		}
		return mailproperties;
	}

	public static String getAuthentication() {
		return authentication;
	}

	public static void setAuthentication(String authentication) {
		MailProperties.authentication = authentication;
	}

	public static String getPassword() {
		return password;
	}

	public static void setPassword(String password) {
		MailProperties.password = password;
	}

	public static String getPort() {
		return port;
	}

	public static void setPort(String port) {
		MailProperties.port = port;
	}

	public static String getSender() {
		return sender;
	}

	public static void setSender(String sender) {
		MailProperties.sender = sender;
	}

	public static String getSmtpHost() {
		return smtpHost;
	}

	public static void setSmtpHost(String smtpHost) {
		MailProperties.smtpHost = smtpHost;
	}

	public static String getUser() {
		return user;
	}

	public static void setUser(String user) {
		MailProperties.user = user;
	}

	/**
	 * ����properties�ļ���mail�����ļ�ģ����
	 * @param properties
	 * @return
	 */
	public static synchronized void loadMailProperies(Properties properties) {
		MailProperties mailProperties = new MailProperties();
		smtpHost = properties.getProperty("mail.smtp.host");
		sender = properties.getProperty("mail.from");
		authentication = properties.getProperty("mail.smtp.auth");
		user = properties.getProperty("mail.smtp.user");
		password = properties.getProperty("mail.smtp.password");
		port = properties.getProperty("mail.smtp.port");
		
		mailProperties.setAuthentication(authentication);
		mailProperties.setPassword(password);
		mailProperties.setPort(port);
		mailProperties.setSender(sender);
		mailProperties.setSmtpHost(smtpHost);
		mailProperties.setUser(user);
	}
}
