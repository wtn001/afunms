/**
 * <p>Description:host,including server and exchange device</p>
 * <p>Company: dhcc.com</p>
 * @author afunms
 * @project afunms
 * @date 2006-08-27
 */

package com.afunms.polling.node;

/**
 * 
 * @author GANYI
 * @since 2012-04-23 18:51:00
 */
public class TFtp extends Application {

	private int id ;
	
	private String name;
	
	private String username;
	
	private String password;
	
	/**
	 * ��ʱ
	 */
	private int timeout;
	
	/**
	 * �Ƿ���� 0Ϊfalse 1Ϊtrue
	 */
	private int monflag;
	
	/**
	 * ip��ַ
	 */
	private String ipaddress;
	
	/**
	 * �ļ���
	 */
	private String filename;
	
	/**
	 * ����ҵ��
	 */
	private String bid;
	
	/**
	 * ���Ž�����
	 */
	private String sendmobiles;
	
	/**
	 * email������
	 */
	private String sendemail;
	
	/**
	 * �绰������
	 */
	private String sendphone;

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
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
	 * @return the timeout
	 */
	public int getTimeout() {
		return timeout;
	}

	/**
	 * @param timeout the timeout to set
	 */
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	/**
	 * @return the monflag
	 */
	public int getMonflag() {
		return monflag;
	}

	/**
	 * @param monflag the monflag to set
	 */
	public void setMonflag(int monflag) {
		this.monflag = monflag;
	}
	
	/**
	 * @return the ipaddress
	 */
	public String getIpaddress() {
		return ipaddress;
	}

	/**
	 * @param ipaddress the ipaddress to set
	 */
	public void setIpaddress(String ipaddress) {
		this.ipaddress = ipaddress;
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

	/**
	 * @return the bid
	 */
	public String getBid() {
		return bid;
	}

	/**
	 * @param bid the bid to set
	 */
	public void setBid(String bid) {
		this.bid = bid;
	}

	/**
	 * @return the sendmobiles
	 */
	public String getSendmobiles() {
		return sendmobiles;
	}

	/**
	 * @param sendmobiles the sendmobiles to set
	 */
	public void setSendmobiles(String sendmobiles) {
		this.sendmobiles = sendmobiles;
	}

	/**
	 * @return the sendemail
	 */
	public String getSendemail() {
		return sendemail;
	}

	/**
	 * @param sendemail the sendemail to set
	 */
	public void setSendemail(String sendemail) {
		this.sendemail = sendemail;
	}

	/**
	 * @return the sendphone
	 */
	public String getSendphone() {
		return sendphone;
	}

	/**
	 * @param sendphone the sendphone to set
	 */
	public void setSendphone(String sendphone) {
		this.sendphone = sendphone;
	}
		
	}