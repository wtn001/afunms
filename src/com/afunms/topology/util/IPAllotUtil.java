package com.afunms.topology.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.afunms.config.dao.IpAliasDao;
import com.afunms.config.model.IpAlias;

public class IPAllotUtil {
	// keyΪipǰ��λ��valueΪ��IP�ε�����ip
	public Map<String, List<String>> sort() {
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		List<IpAlias> ipList = null;
		IpAliasDao dao = new IpAliasDao();
		try {
			ipList = dao.loadAll();
		} catch (Exception e) {

		} finally {
			dao.close();
		}
		for (int i = 0; i < ipList.size(); i++) {
			IpAlias ipalias = (IpAlias) ipList.get(i);
			String str = ipalias.getAliasip();
			int k = str.lastIndexOf("."); // �õ����һ�� ��.�� ������
			String aa = str.substring(0, k);
			if (map.containsKey(aa)) {
				List<String> bb = map.get(aa);
				bb.add(str);
			} else {
				List<String> c = new ArrayList<String>();
				c.add(str);
				map.put(aa, c);
			}
		}
		return map;
	}

	/*
	 * ip��������
	 * 
	 * ����ip����Ķ������ô˷�������
	 */
	public String[] ipsort(String[] ip) {
		// ��ip�����Ϊ�ȳ�����
		for (int i = 0; i < ip.length; i++) {
			String[] temp = ip[i].split("\\.");
			ip[i] = "";
			for (int j = 0; j < temp.length; j++) {
				if (Integer.parseInt(temp[j]) / 10 == 0) { // ����ip��Ϊ0-9ʱ��ǰ�油��00��
					temp[j] = "00" + temp[j];
				} else if (Integer.parseInt(temp[j]) / 100 == 0) { // ����ip��Ϊ10-99ʱ��ǰ�油��0��
					temp[j] = "0" + temp[j];
				}
				ip[i] += temp[j] + "."; // ������װ����
			}
			ip[i] = ip[i].substring(0, ip[i].length() - 1); // ȥ�����һ������ġ�.��
		}

		Arrays.sort(ip);//��������

		// ��ԭip
		for (int i = 0; i < ip.length; i++) {
			String[] temp = ip[i].split("\\.");
			ip[i] = "";
			for (int j = 0; j < temp.length; j++) {
				if (temp[j].startsWith("00")) { // ȥ��ǰ���"00"
					temp[j] = temp[j].substring(2);
				} else if (temp[j].startsWith("0")) { // ȥ��ǰ���"0"
					temp[j] = temp[j].substring(1);
				}
				ip[i] += temp[j] + ".";
			}
			ip[i] = ip[i].substring(0, ip[i].length() - 1);
		}
		return ip;
	}

}
