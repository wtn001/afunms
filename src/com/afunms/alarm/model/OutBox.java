package com.afunms.alarm.model;

import com.afunms.common.base.BaseVo;

public class OutBox extends BaseVo{
	
	/**
	 * ������
	 */
	private String Sender;
	
	/**
	 * �绰
	 */
	private String ReceiverMobileNo;
	
	/**
	 * ��Ϣ
	 */
	private String Msg;
	
	/**
	 * ����ʱ��
	 */
	private String SendTime;

	public String getSender() {
		return Sender;
	}

	public void setSender(String sender) {
		Sender = sender;
	}

	public String getReceiverMobileNo() {
		return ReceiverMobileNo;
	}

	public void setReceiverMobileNo(String receiverMobileNo) {
		ReceiverMobileNo = receiverMobileNo;
	}

	public String getMsg() {
		return Msg;
	}

	public void setMsg(String msg) {
		Msg = msg;
	}

	public String getSendTime() {
		return SendTime;
	}

	public void setSendTime(String sendTime) {
		SendTime = sendTime;
	}

	

}
