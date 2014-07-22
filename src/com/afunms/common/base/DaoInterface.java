package com.afunms.common.base;

import java.util.List;

/**
 * 功能：数据访问层接口，定义了一些数据访问层常用的方法
 * 
 * @author Administrator
 * 
 */
public interface DaoInterface {
	/**
	 * 1.加载所有记录
	 */
	public List loadAll();

	/**
	 * 2.分页列表
	 */
	public List listByPage(int curpage, int perpage);

	/**
	 * 3.带条件的分页列表
	 */
	public List listByPage(int curpage, String where, int perpage);

	/**
	 * 4.删除一批记录
	 */
	public boolean delete(String[] id);

	/**
	 * 5.按ID找一条记录
	 */
	public BaseVo findByID(String id);

	/**
	 * 6.增加一条记录
	 */
	public boolean save(BaseVo vo);

	/**
	 * 7.更新一条记录
	 */
	public boolean update(BaseVo vo);

	/**
	 * 8.得到分页对象
	 */
	public JspPage getPage();

	public List loadAll(String where);
}
