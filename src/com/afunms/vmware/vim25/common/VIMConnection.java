package com.afunms.vmware.vim25.common;

import java.util.HashMap;
import java.util.UUID;

import org.apache.log4j.Logger;

import com.vmware.apputils.version.ExtendedAppUtil;

/**
 * VMWare���ӹ���
 * 
 * @author LXL
 * 
 */
public class VIMConnection {

	// LOGGER
	private final static Logger LOGGER = Logger.getLogger(VIMConnection.class);

	// ���ӵĻ���
	private static HashMap<String, ExtendedAppUtil> ecbMap = new HashMap<String, ExtendedAppUtil>();

	/**
	 * ��ȡ��������
	 * 
	 * @param url
	 * @param username
	 * @param password
	 * @return
	 */
	public static String[] getVMArgs(String url, String username,
			String password) {
		String[] vmargs = { "--url", url, "--username", username, "--password",
				password, "--ignorecert", "ignorecert", };
		return vmargs;
	}

	/**
	 * ��ȡExtendedAppUtil�洢��Key
	 * 
	 * @param url
	 * @return
	 */
	protected static String getVIMKey(String url, String username) {
		return url + username;
	}

	/**
	 * ��ȡExtendedAppUtil,�����������Ƿ����ӷ�����
	 * 
	 * @param name
	 * @param url
	 * @param username
	 * @param password
	 * @param isConnect
	 * @return
	 */
	private static ExtendedAppUtil getECB(String url, String username,
			String password, boolean isConnect) {

		ExtendedAppUtil ecb = null;

		try {
			LOGGER.info("getECB, url='" + url + "', username='" + username
					+ "', password='" + password + "', isConnect='" + isConnect
					+ "'");

			if (ecbMap.containsKey(getVIMKey(url, username))) {
				ecb = ecbMap.get(getVIMKey(url, username));
			} else {
				ecb = ExtendedAppUtil.initialize(UUID.randomUUID().toString(),
						getVMArgs(url, username, password));
				if (ecb != null) {
					ecbMap.put(getVIMKey(url, username), ecb);
				}
			}

			// ecb��������
			if (isConnect) {
				if (!ecb.getConnection().isConnected()) {
					ecb.connect();
				}
			}
		} catch (Exception e) {
			// �ͷ�����
			releaseECB(url, username);

			LOGGER.error("getECB error, ", e);
		}

		return ecb;
	}

	/**
	 * ��ȡExtendedAppUtil,�����ӷ�����
	 * 
	 * @param url
	 * @param username
	 * @param password
	 * @return
	 */
	public static ExtendedAppUtil getECB(String url, String username,
			String password) {
		return getECB(url, username, password, true);
	}

	/**
	 * ��ȡExtendedAppUtil,�����ӷ�����
	 * 
	 * @param url
	 * @param username
	 * @param password
	 * @return
	 */
	public static ExtendedAppUtil getECBNotConnected(String url,
			String username, String password) {
		return getECB(url, username, password, false);
	}

	/**
	 * �ͷ�ExtendedAppUtil����
	 * 
	 * @param ecb
	 */
	public static void releaseECB(String url, String username) {
		try {
			if (ecbMap.containsKey(getVIMKey(url, username))) {
				ExtendedAppUtil ecb = ecbMap.remove(getVIMKey(url, username));
				if (ecb != null) {
					ecb.disConnect();
				}
				ecb = null;
			}
		} catch (Exception e) {
			LOGGER.error("releaseECB error,", e);
		}
	}
}
