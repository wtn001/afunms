package com.afunms.application.model;
/**
 * @author HONGLI E-mail: ty564457881@163.com
 * @version ����ʱ�䣺Aug 21, 2011 6:20:35 PM
 * ��˵�� �״�������ʶ
 */
public class Test {
	private static Test test = new Test();
	
	/**
	 *�״�������ʶ 
	 */
	private boolean isFirstStart = true;


	public boolean isFirstStart() {
		return isFirstStart;
	}


	public void setFirstStart(boolean isFirstStart) {
		this.isFirstStart = isFirstStart;
	}


	public synchronized static Test getInstance(){
		return test;
	}
}
