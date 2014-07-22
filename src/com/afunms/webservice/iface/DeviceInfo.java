package com.afunms.webservice.iface;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import org.python.modules.time;

import com.afunms.webservice.model.Attribute;
import com.afunms.webservice.model.AttributeValue;
import com.afunms.webservice.model.CiType;
import com.afunms.webservice.model.DeviceBaseInfo;
import com.afunms.webservice.model.DeviceHardwareInfo;
import com.afunms.webservice.model.DeviceInterfaceGroup;
import com.afunms.webservice.model.DeviceInterfaceInfo;

/**
 * @description webservice�������ķ���
 * @author wangxiangyong
 * @date Feb 4, 2012 9:29:07 AM
 */
@WebService
public interface DeviceInfo {
	/**
	 * ��ȡ�����豸�Ļ�����Ϣ��ͨ�ã�
	 * 
	 * @return
	 */
	@WebMethod
	public List<DeviceBaseInfo> getAllDeviceBaseInfo();

	/**
	 * ����IP��ȡ�豸�Ļ�����Ϣ��ͨ�ã�
	 * 
	 * @return
	 */
	@WebMethod
	public DeviceBaseInfo getDeviceBaseInfoByIp(@WebParam(name = "ip",targetNamespace= "http://iface.webservice.afunms.com/") String ip);
	/**
	 * ��ȡ�����豸�ӿڵĻ�����Ϣ��ͨ�ã�
	 * 
	 * @return
	 */
	@WebMethod
	public List<DeviceInterfaceInfo> getAllDeviceInterfaceInfo();
	/**
	 * ����IP��ȡ�豸�ӿڵĻ�����Ϣ��ͨ�ã�
	 * 
	 * @return
	 */
	@WebMethod
	public DeviceInterfaceGroup getDeviceInterfaceInfoByIp(@WebParam(name = "ip",targetNamespace= "http://iface.webservice.afunms.com/") String ip);
	/**
	 * ����IP��ȡ�豸Ӳ���Ļ�����Ϣ��ͨ�ã�
	 * 
	 * @return
	 */
	@WebMethod
	public DeviceHardwareInfo getDeviceHardwareByIp(@WebParam(name = "ip",targetNamespace= "http://iface.webservice.afunms.com/") String ip);
	/**
	 * �����������ȡID���ϣ���׼��
	 * 
	 * @return
	 */
	@WebMethod
	public  String[] getMoIds(@WebParam(name = "ciType",targetNamespace= "http://iface.webservice.afunms.com/")String ciType);
	/**
	 *��ط��ֵĶ��󣨱�׼��
	 * 
	 * @return
	 */
//	@WebMethod
//	public  void sendChangedMo(@WebParam(name = "moids")String moids[],@WebParam(name = "updatetime")String updatetime);
	/**
	 *��ȡ��ض��������ָ��ֵ����׼��
	 * 
	 * @return
	 */
	@WebMethod
	public   AttributeValue[] getMoAttributeValue(@WebParam(name = "attributes",targetNamespace= "http://iface.webservice.afunms.com/")Attribute[] attributes);
	/**
	 *��ȡ��ض����ָ��ֵ����չ��
	 * 
	 * @return
	 */
	@WebMethod
	public   CiType[] getMoAttributeName();

}
