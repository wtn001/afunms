/**
 * <p>Description:manager interface,define a method(execute) of all dao classes</p>
 * <p>Company: dhcc.com</p>
 * @author afunms
 * @project afunms
 * @date 2006-08-07
 */

package com.afunms.common.base;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 功能：控制层接口，定义了一个所有具体子类必须实现的执行方法
 * 
 * @author Administrator
 * 
 */
public interface ManagerInterface {
	public void setRequest(HttpServletRequest req);

	public void setRequest(HttpServletRequest req, HttpServletResponse res);

	public int getErrorCode();

	/**
	 * 关键方法,每个具体子类必须实现
	 */
	public String execute(String action);
}