package com.afunms.event.model;

import com.afunms.common.base.BaseVo;
/**
 * @author Administrator
 *
 */
public class NetSyslogImpEvent extends BaseVo {
	private int loginSuccess;//��¼�ɹ����û�
	private int logoutSuccess;//�ǳ��ɹ����û�
	private int loginFailure;//��¼ʧ�ܵ��û�
	private int clearLog;//��������־
	private int strategyModified;//��Ʋ��Ա��
	private int accoutModified;//�û��ʺű��
	private int accoutLocked;//�������û��ʺ�
	private int sceCli;//SceCli�����
	public int getLoginSuccess() {
		return loginSuccess;
	}
	public void setLoginSuccess(int loginSuccess) {
		this.loginSuccess = loginSuccess;
	}
	public int getLogoutSuccess() {
		return logoutSuccess;
	}
	public void setLogoutSuccess(int logoutSuccess) {
		this.logoutSuccess = logoutSuccess;
	}
	public int getLoginFailure() {
		return loginFailure;
	}
	public void setLoginFailure(int loginFail) {
		this.loginFailure = loginFail;
	}
	public int getClearLog() {
		return clearLog;
	}
	public void setClearLog(int cleareLog) {
		this.clearLog = cleareLog;
	}
	public int getStrategyModified() {
		return strategyModified;
	}
	public void setStrategyModified(int strategyModified) {
		this.strategyModified = strategyModified;
	}
	public int getAccoutModified() {
		return accoutModified;
	}
	public void setAccoutModified(int accoutModified) {
		this.accoutModified = accoutModified;
	}
	public int getAccoutLocked() {
		return accoutLocked;
	}
	public void setAccoutLocked(int accoutLocked) {
		this.accoutLocked = accoutLocked;
	}
	public int getSceCli() {
		return sceCli;
	}
	public void setSceCli(int sceCli) {
		this.sceCli = sceCli;
	}
}