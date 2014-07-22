package com.afunms.common.base;

import java.util.List;

/**
 * ���ܣ����ݷ��ʲ�ӿڣ�������һЩ���ݷ��ʲ㳣�õķ���
 * 
 * @author Administrator
 * 
 */
public interface DaoInterface {
	/**
	 * 1.�������м�¼
	 */
	public List loadAll();

	/**
	 * 2.��ҳ�б�
	 */
	public List listByPage(int curpage, int perpage);

	/**
	 * 3.�������ķ�ҳ�б�
	 */
	public List listByPage(int curpage, String where, int perpage);

	/**
	 * 4.ɾ��һ����¼
	 */
	public boolean delete(String[] id);

	/**
	 * 5.��ID��һ����¼
	 */
	public BaseVo findByID(String id);

	/**
	 * 6.����һ����¼
	 */
	public boolean save(BaseVo vo);

	/**
	 * 7.����һ����¼
	 */
	public boolean update(BaseVo vo);

	/**
	 * 8.�õ���ҳ����
	 */
	public JspPage getPage();

	public List loadAll(String where);
}
