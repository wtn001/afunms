package com.afunms.webservice.impl;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.jws.WebParam;
import javax.jws.WebService;

//import javax.jws.WebService;
import com.afunms.topology.dao.HostNodeDao;
import com.afunms.topology.model.HostNode;
import com.afunms.webservice.dao.CiTypeDao;
import com.afunms.webservice.dao.RelationDao;
import com.afunms.webservice.iface.DeviceInfo;
import com.afunms.webservice.model.Attribute;
import com.afunms.webservice.model.AttributeValue;
import com.afunms.webservice.model.CiType;
import com.afunms.webservice.model.DeviceBaseInfo;
import com.afunms.webservice.model.DeviceHardwareInfo;
import com.afunms.webservice.model.DeviceInterfaceGroup;
import com.afunms.webservice.model.DeviceInterfaceInfo;
import com.afunms.webservice.model.MoAndCiRelation;

/**
 * @description �ӿڵľ���ʵ����
 * @author wangxiangyong
 * @date Feb 4, 2012 9:28:06 AM
 */
@WebService(endpointInterface = "com.afunms.webservice.iface.DeviceInfo")
public class DeviceInfoImpl implements DeviceInfo {
	/**
	 * ��ȡ�����豸�Ļ�����Ϣ(ͨ�÷���)
	 * 
	 * @return
	 */
	public List<DeviceBaseInfo> getAllDeviceBaseInfo() {
		List<DeviceBaseInfo> deviceInfoList = new ArrayList<DeviceBaseInfo>();
		HostNodeDao dao = new HostNodeDao();
		List<HostNode> list = null;
		try {
			list = dao.loadall();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dao.close();
		}

		DeviceBaseInfo deviceInfo = null;
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				HostNode node = list.get(i);
				deviceInfo = new DeviceBaseInfo();
				deviceInfo.setId(node.getId() + "");
				deviceInfo.setIpAddress(node.getIpAddress());
				deviceInfo.setSysName(node.getSysName());
				deviceInfo.setAlias(node.getAlias());
				deviceInfo.setNetMask(node.getNetMask());
				deviceInfo.setSysDescr(node.getSysDescr());
				deviceInfo.setSysOid(node.getSysOid());
				deviceInfo.setCategory(node.getCategory() + "");
				deviceInfo.setOsType(node.getOstype() + "");
				deviceInfo.setStatus(node.getStatus() + "");
				deviceInfoList.add(deviceInfo);
			}
		}
		return deviceInfoList;
	}

	/**
	 * ����IP��ȡ�豸�Ļ�����Ϣ(ͨ�÷���)
	 * 
	 * @return
	 */
	public DeviceBaseInfo getDeviceBaseInfoByIp(String ip) {
		HostNodeDao dao = new HostNodeDao();
		DeviceBaseInfo deviceInfo = null;
		try {
			HostNode node = (HostNode) dao.findByIpaddress(ip);

			if (node != null) {
				deviceInfo = new DeviceBaseInfo();
				deviceInfo.setId(node.getId() + "");
				deviceInfo.setIpAddress(node.getIpAddress());
				deviceInfo.setSysName(node.getSysName());
				deviceInfo.setAlias(new String(node.getAlias()));
				deviceInfo.setNetMask(node.getNetMask());
				deviceInfo.setSysDescr(node.getSysDescr());
				deviceInfo.setSysOid(node.getSysOid());
				deviceInfo.setCategory(node.getCategory() + "");
				deviceInfo.setOsType(node.getOstype() + "");
				deviceInfo.setStatus(node.getStatus() + "");
			}
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			dao.close();
		}
		return deviceInfo;
	}

	/**
	 * ��ȡ�����豸�ӿڵĻ�����Ϣ(ͨ�÷���)
	 * 
	 * @return
	 */
	public List<DeviceInterfaceInfo> getAllDeviceInterfaceInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * ����IP��ȡ�豸�ӿڵĻ�����Ϣ(ͨ�÷���)
	 * 
	 * @return
	 */
	public DeviceInterfaceGroup getDeviceInterfaceInfoByIp(String ip) {
		DeviceInfoImplHelper helper = new DeviceInfoImplHelper();
		DeviceInterfaceGroup group = helper.getDeviceInterface(ip);
		return group;
	}

	/**
	 * ����IP��ȡ�豸Ӳ���Ļ�����Ϣ(ͨ�÷���)
	 * 
	 * @return
	 */
	public DeviceHardwareInfo getDeviceHardwareByIp(String ip) {
		DeviceInfoImplHelper helper = new DeviceInfoImplHelper();
		DeviceHardwareInfo hardwareInfo = helper.getDeviceHardwareInfo(ip);
		return hardwareInfo;
	}

	/**
	 * ��ȡ������ID�б�������׼������
	 */
	

	/**
	 * ��ȡ��ض����ָ��ֵ����׼������
	 * 
	 */
	public AttributeValue[] getMoAttributeValue(Attribute[] attributes) {
		Vector<String> vector = new Vector<String>();
		Vector<String> deviceVec=new Vector<String>();
		AttributeValue[] values = null;
		Hashtable<Integer,String> moIdTable=new Hashtable<Integer, String>();
		if (attributes != null && attributes.length > 0) {
			List<MoAndCiRelation> list=null;
			RelationDao relationDao=new RelationDao();
			try {
				list=relationDao.loadAll();
				if (list!=null&&list.size()>0) {
					for (int i = 0; i < list.size(); i++) {
						MoAndCiRelation relation=list.get(i);
						if (relation!=null) {
							moIdTable.put(relation.getMoId(), relation.getCiType());
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				relationDao.close();
			}
			
			values = new AttributeValue[attributes.length];
			for (int i = 0; i < attributes.length; i++) {
				Attribute attribute = attributes[i];
				if (attribute == null)
					continue;
				if (!vector.contains(attribute.getMoId())) {
					vector.add(attribute.getMoId());
				}
				String ciType=moIdTable.get(attribute.getMoId());

			}
			DeviceInfoImplHelper helper = new DeviceInfoImplHelper();
			Hashtable<String, String> hashTable = new Hashtable<String, String>();
			helper.getDeviceInterface(vector, hashTable);// ��ѯ�ӿ���Ϣ�������뼯�϶�����
			helper.getAllDeviceBaseInfo(hashTable);// ��ȡ�豸�Ļ�����Ϣ�������뼯�϶�����
			helper.getHardwareInfo(vector, hashTable);// ��ȡӲ����Ϣ�������뼯�϶�����
			helper.getOsInfo(vector, hashTable); // ��ȡ����ϵͳ��Ϣ�������뼯�϶�����
			helper.getSoftwareInfo(vector, hashTable);// ��ȡ�����Ϣ�������뼯�϶�����
			helper.getAllDbInfo(hashTable);// ��ȡ�������ݿ����Ϣ�������뼯�϶�����
			helper.getAllTomcatInfo(hashTable);//��ȡtomcat�ڵ���Ϣ,�����뼯�϶�����
			helper.getAllMQInfo(hashTable);//��ȡMQ�ڵ���Ϣ,�����뼯�϶�����
			AttributeValue value = null;
			for (int i = 0; i < attributes.length; i++) {
				Attribute attribute = attributes[i];
				value = new AttributeValue();
				String key = attribute.getMoId() + "_" + attribute.getAttributeName();

				if (hashTable.containsKey(key)) {
					value.setAttributeName(attribute.getAttributeName());
					value.setMoid(attribute.getMoId() + "");
					value.setValue(hashTable.get(key));
					System.out.println(hashTable.get(key) + "----" + key);

				}
				values[i] = value;

			}

		}

		return values;
	}

	/**
	 * ����һ����չ��webservice�ӿڷ������������Զ��������롢����������Ϣ
	 * 
	 */
	public CiType[] getMoAttributeName() {
		List<CiType> list = null;
		CiTypeDao dao = new CiTypeDao();

		try {
			list = dao.loadAll();
		} catch (Exception e) {
			e.printStackTrace();
		}

		CiType[] types = null;
		if (list != null && list.size() > 0) {
			types = new CiType[list.size()];
			for (int i = 0; i < list.size(); i++) {
				types[i] = list.get(i);
			}
		}
		return types;
	}

	public String[] getMoIds(String ciType) {
		DeviceInfoImplHelper helper = new DeviceInfoImplHelper();
		helper.synchronousData();// ͬ��
		RelationDao dao = new RelationDao();
		if (ciType == null) {
			ciType = "";
		}
		System.out.println("==="+ciType);
		String[] moIds = null;
		try {
			moIds = dao.getMoIdsByCiType(ciType);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dao.close();
		}

		return moIds;
	}

}
