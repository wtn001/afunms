package com.afunms.detail.service.resinInfo;

import java.util.Hashtable;

import com.afunms.application.dao.ResinDao;

public class ResinInfoService {
	
	
	/**
	 * <p>�����ݿ��л�ȡresin�Ĳɼ���Ϣ</p>
	 * @param nodeid
	 * @return
	 */
	public Hashtable getResinDataHashtable(String nodeid) {
		Hashtable retHashtable = null;
		ResinDao resinDao = new ResinDao();
		try{
			retHashtable = resinDao.getResinDataHashtable(nodeid);
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			resinDao.close();
		}
		return retHashtable;
	}
}
