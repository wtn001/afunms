package com.afunms.polling.snmp.interfaces;

import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import com.afunms.alarm.model.AlarmIndicatorsNode;
import com.afunms.alarm.model.AlarmPort;
import com.afunms.alarm.util.AlarmIndicatorsUtil;
import com.afunms.common.util.CheckEventUtil;
import com.afunms.common.util.ShareData;
import com.afunms.common.util.SnmpUtils;
import com.afunms.common.util.SysLogger;
import com.afunms.config.model.Portconfig;
import com.afunms.indicators.model.NodeGatherIndicators;
import com.afunms.initialize.PortConfigCenter;
import com.afunms.monitor.executor.base.SnmpMonitor;
import com.afunms.monitor.item.base.MonitoredItem;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.base.LinkRoad;
import com.afunms.polling.base.Node;
import com.afunms.polling.node.Host;
import com.afunms.polling.om.AllUtilHdx;
import com.afunms.polling.om.DiscardsPerc;
import com.afunms.polling.om.ErrorsPerc;
import com.afunms.polling.om.InPkts;
import com.afunms.polling.om.Interfacecollectdata;
import com.afunms.polling.om.OutPkts;
import com.afunms.polling.om.Packs;
import com.afunms.polling.om.UtilHdx;
import com.afunms.polling.om.UtilHdxPerc;
import com.afunms.polling.snmp.SnmpMibConstants;
import com.afunms.topology.model.HostNode;
import com.gatherResulttosql.NetInterfaceDataTemptosql;
import com.gatherResulttosql.NetinterfaceResultTosql;

public class InterfaceSnmp extends SnmpMonitor {

	private static Hashtable ifEntity_ifStatus = null;
	static {
		ifEntity_ifStatus = new Hashtable();
		ifEntity_ifStatus.put("1", "up");
		ifEntity_ifStatus.put("2", "down");
		ifEntity_ifStatus.put("3", "testing");
		ifEntity_ifStatus.put("5", "unknow");
		ifEntity_ifStatus.put("7", "unknow");
	};

	java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public InterfaceSnmp() {
	}

	public void collectData(Node node, MonitoredItem item) {

	}

	public void collectData(HostNode node) {

	}

	public Hashtable collect_Data(NodeGatherIndicators nodeGatherIndicators) {
		Hashtable returnHash = new Hashtable();
		Vector interfaceVector = new Vector();
		Vector utilhdxVector = new Vector();// ���������
		Vector allUtilhdxVector = new Vector();// �ۺ�����
		Vector packsVector = new Vector();// �ۺϰ�
		Vector inPacksVector = new Vector();// ��ڰ�
		Vector outPacksVector = new Vector();// ���ڰ�
		Vector discardsPercVector = new Vector();// ������
		Vector errorsPercVector = new Vector();// ������
		Vector allErrorsPercVector = new Vector();// �ܴ�����
		Vector allDiscardsPercVector = new Vector();// �ܶ�����
		Vector allUtilhdxPercVector = new Vector();// �ۺϴ���������
		Vector utilhdxPercVector = new Vector();// ����ڴ���������
		Host host = (Host) PollingEngine.getInstance().getNodeByID(Integer.parseInt(nodeGatherIndicators.getNodeid()));
		if (host == null)
			return returnHash;

		Hashtable portHs = PortConfigCenter.getInstance().getPortHastable();
		List portList = (List) portHs.get(host.getIpAddress());// ��Ҫ��ӵ�����ͳ�ƵĶ˿ڼ���

		List portconfiglist = new ArrayList();
		Portconfig portconfig = null;
		Hashtable allportconfighash = ShareData.getPortConfigHash();
		if (allportconfighash != null && allportconfighash.size() > 0) {
			if (allportconfighash.containsKey(host.getIpAddress())) {
				portconfiglist = (List) allportconfighash.get(host.getIpAddress());
			}
		}
		Hashtable portconfigHash = new Hashtable();
		if (portconfiglist != null && portconfiglist.size() > 0) {
			for (int i = 0; i < portconfiglist.size(); i++) {
				portconfig = (Portconfig) portconfiglist.get(i);
				portconfigHash.put(portconfig.getPortindex() + "", portconfig);
			}
		}
		portconfig = null;

		List alarmportconfiglist = new ArrayList();
		AlarmPort alarmPortConfig = null;
		Hashtable allalarmportconfighash = ShareData.getAlarmportConfigHash();
		if (allalarmportconfighash != null && allalarmportconfighash.size() > 0) {
			if (allalarmportconfighash.containsKey(host.getIpAddress())) {
				alarmportconfiglist = (List) allalarmportconfighash.get(host.getIpAddress());
			}
		}
		Hashtable alarmportconfigHash = new Hashtable();
		if (alarmportconfiglist != null && alarmportconfiglist.size() > 0) {
			for (int i = 0; i < alarmportconfiglist.size(); i++) {
				alarmPortConfig = (AlarmPort) alarmportconfiglist.get(i);
				alarmportconfigHash.put(alarmPortConfig.getPortindex() + "", alarmPortConfig);
			}
		}
		alarmPortConfig = null;

		try {
			Interfacecollectdata interfacedata = null;
			UtilHdx utilhdx = new UtilHdx();
			InPkts inPacks = new InPkts();
			OutPkts outPacks = new OutPkts();
			UtilHdxPerc utilhdxPerc = new UtilHdxPerc();
			AllUtilHdx allUtilhdx = new AllUtilHdx();
			Calendar date = Calendar.getInstance();

			try {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				com.afunms.polling.base.Node snmpnode = (com.afunms.polling.base.Node) PollingEngine.getInstance().getNodeByIP(host.getIpAddress());
				Date cc = date.getTime();
				String time = sdf.format(cc);
				snmpnode.setLastTime(time);
			} catch (Exception e) {

			}
			try {
				long lastTimeInMillis = 0;
				BigInteger longInterval = new BigInteger("0");

				Hashtable lastOctetsHash = ShareData.getOctetsdata(host.getIpAddress());
				if (lastOctetsHash == null) {
					lastOctetsHash = new Hashtable();
				} else {
					// �����һ�εĲɼ�ʱ�䲻Ϊ��,��˵���Ѿ��ɼ���һ��������
					Calendar calendar = (Calendar) lastOctetsHash.get("collecttime");
					if (calendar != null) {
						lastTimeInMillis = calendar.getTimeInMillis();
						longInterval = new BigInteger((date.getTimeInMillis() - lastTimeInMillis) / 1000 + "");// ʱ����,������������˿�����
					}
				}

				Hashtable interfaceSpeedHash = new Hashtable();
				Hashtable octetsHash = new Hashtable();

				// ���붪����=��ifInDiscards��(��ifInUcastPkts+��ifInNUcastPkts)*100��
				// ���������=��ifOutDiscards��(��ifOutUcastPkts+��ifOutNUcastPkts )*100��
				// ��������=��ifInErrors��(��ifInUcastPkts+��ifInNUcastPkts)*100��
				// ��������=��ifOuterrors��(��ifOutUcastPkts ����ifOutNUcastPkts)*100��

				String[] lowOids = new String[] { 
						"1.3.6.1.2.1.2.2.1.1", // ifIndex
						"1.3.6.1.2.1.2.2.1.2", // ifDescr
						"1.3.6.1.2.1.2.2.1.2", // ifName
						"1.3.6.1.2.1.2.2.1.3", // ifType
						"1.3.6.1.2.1.2.2.1.4", // ifMtu
						"1.3.6.1.2.1.2.2.1.5", // ifSpeed
						"1.3.6.1.2.1.2.2.1.6", // ifPhysAddress
						"1.3.6.1.2.1.2.2.1.7", // ifAdminStatus
						"1.3.6.1.2.1.2.2.1.8", // ifOperStatus
						"1.3.6.1.2.1.2.2.1.9", // ifLastChange
						"1.3.6.1.2.1.2.2.1.10", // ifInOctets
						"1.3.6.1.2.1.2.2.1.16", // ifOutOctets
						"1.3.6.1.2.1.2.2.1.11", // ifInUcastPkts
						"1.3.6.1.2.1.2.2.1.17", // ifOutUcastPkts
						"1.3.6.1.2.1.2.2.1.12", // ifInNUcastPkts
						"1.3.6.1.2.1.2.2.1.18", // ifOutNUcastPkts
						"1.3.6.1.2.1.2.2.1.13", // ifInDiscards
						"1.3.6.1.2.1.2.2.1.19", // ifOutDiscards
						"1.3.6.1.2.1.2.2.1.14", // ifInErrors
						"1.3.6.1.2.1.2.2.1.20", // ifOutErrors
						"1.3.6.1.2.1.31.1.1.1.2", // ifInMulticastPkts
						"1.3.6.1.2.1.31.1.1.1.4", // ifOutMulticastPkts
						"1.3.6.1.2.1.31.1.1.1.3", // ifInBroadcastPkts
						"1.3.6.1.2.1.31.1.1.1.5", // ifOutBroadcastPkts

				};

				String[] highOids = new String[] { 
						"1.3.6.1.2.1.2.2.1.1", // ifIndex
						"1.3.6.1.2.1.2.2.1.2", // ifDescr
						"1.3.6.1.2.1.2.2.1.2", // ifName
						"1.3.6.1.2.1.2.2.1.3", // ifType
						"1.3.6.1.2.1.2.2.1.4", // ifMtu
						"1.3.6.1.2.1.31.1.1.1.15", // ifHighSpeed
						"1.3.6.1.2.1.2.2.1.6", // ifPhysAddress
						"1.3.6.1.2.1.2.2.1.7", // ifAdminStatus
						"1.3.6.1.2.1.2.2.1.8", // ifOperStatus
						"1.3.6.1.2.1.2.2.1.9", // ifLastChange
						"1.3.6.1.2.1.31.1.1.1.6", // ifHCInOctets
						"1.3.6.1.2.1.31.1.1.1.10", // ifHCOutOctets
						"1.3.6.1.2.1.31.1.1.1.7", // ifHCInUcastPkts
						"1.3.6.1.2.1.31.1.1.1.11", // ifHCOutUcastPkts
						"1.3.6.1.2.1.2.2.1.12", // ifInNUcastPkts
						"1.3.6.1.2.1.2.2.1.18", // ifOutNUcastPkts
						"1.3.6.1.2.1.2.2.1.13", // ifInDiscards
						"1.3.6.1.2.1.2.2.1.19", // ifOutDiscards
						"1.3.6.1.2.1.2.2.1.14", // ifInErrors
						"1.3.6.1.2.1.2.2.1.20", // ifOutErrors
						"1.3.6.1.2.1.31.1.1.1.8", // ifHCInMulticastPkts
						"1.3.6.1.2.1.31.1.1.1.12", // ifHCOutMulticastPkts
						"1.3.6.1.2.1.31.1.1.1.9", // ifHCInBroadcastPkts
						"1.3.6.1.2.1.31.1.1.1.13", // ifHCOutBroadcastPkts

				};

				final String[] desc = SnmpMibConstants.NetWorkMibInterfaceDesc;
				final String[] unit = SnmpMibConstants.NetWorkMibInterfaceUnit;
				final String[] chname = SnmpMibConstants.NetWorkMibInterfaceChname;
				final int[] scale = SnmpMibConstants.NetWorkMibInterfaceScale;

				String[][] valueArray = null;
				try {
					if (host.getSnmpversion() == 0) {
						SysLogger.info("Interface " + host.getIpAddress() + " SnmpV1");
						valueArray = SnmpUtils.getTableData(host.getIpAddress(), host.getCommunity(), lowOids, host.getSnmpversion(), 3, 1000 * 30);
					} else if (host.getSnmpversion() == 1) {
						SysLogger.info("Interface " + host.getIpAddress() + " SnmpV2");
						valueArray = SnmpUtils.getTableData(host.getIpAddress(), host.getCommunity(), highOids, host.getSnmpversion(), 3, 1000 * 30);
					} else if (host.getSnmpversion() == 3) {
						SysLogger.info("Interface " + host.getIpAddress() + " SnmpV3");
						valueArray = SnmpUtils.getTableData(host.getIpAddress(), host.getCommunity(), highOids, host.getSnmpversion(), host.getSecuritylevel(), host.getSecurityName(), host.getV3_ap(), host.getAuthpassphrase(), host.getV3_privacy(), host.getPrivacyPassphrase(), 3, 1000 * 30);
					}

				} catch (Exception e) {
					e.printStackTrace();
				}

				long allSpeed = 0;
				BigInteger allOutOctetsSpeed = new BigInteger("0");// ת���ۺ�����(�����˿�ת���ٶ�֮��)
				BigInteger allInOctetsSpeed = new BigInteger("0");// �����ۺ�����(�����˿ڽ����ٶ�֮��)
				BigInteger allOctetsSpeed = new BigInteger("0");// �ۺ�����(ȫ���˿ڽ��պ�ת�����ٶ�֮��)

				Vector tempV = new Vector();
				Hashtable tempHash = new Hashtable();

				if (valueArray != null) {
//					System.out.println("-----------valueArray != null-------------" + valueArray.length);
//					for (int i = 0; i < valueArray.length; i++) {
//						String[] strs = valueArray[i];
//						for (int j = 0; j < strs.length; j++) {
//							System.out.println("-----------valueArray[" + i +"][" + j + "] = " + strs[j]);
//						}
//					}
					for (int i = 0; i < valueArray.length; i++) {
						if (valueArray[i][0] == null)
							continue;
						String sIndex = valueArray[i][0].toString();
						tempV.add(sIndex);
						tempHash.put(i, sIndex);
						for (int j = 0; j < 24; j++) {
							String interfaceValue = valueArray[i][j];
							interfacedata = new Interfacecollectdata();
							interfacedata.setIpaddress(host.getIpAddress());
							interfacedata.setCollecttime(date);
							interfacedata.setCategory("Interface");
							interfacedata.setEntity(desc[j]);
							interfacedata.setSubentity(sIndex);
							interfacedata.setRestype("static");
							interfacedata.setUnit(unit[j]);
							if ((j == 5) && interfaceValue != null) {// ����
								long longValue = 0L;
								if (host.getCategory() == 4 || host.getCategory() == 8) {// ��Ϊ������ʱ��,ȡ����������Ϊԭʼ��С
									longValue = Long.parseLong(interfaceValue);
								} else {
									if (host.getSnmpversion() == 0) {
										longValue = Long.parseLong(interfaceValue);
									} else {
										longValue = Long.parseLong(interfaceValue) * 1000000;
									}
								}
								interfaceValue = Long.toString(longValue);
								interfaceSpeedHash.put(sIndex, Long.toString(longValue));
								allSpeed = allSpeed + longValue;
							}

							if ((j == 3) && interfaceValue != null) {// �˿�����
								if (Interface_IfType.get(interfaceValue) != null) {
									interfacedata.setThevalue(Interface_IfType.get(interfaceValue).toString());
								} else {
									interfacedata.setThevalue("0.0");
								}
							} else if ((j == 8) && interfaceValue != null) {// �˿�״̬
								if (ifEntity_ifStatus.get(interfaceValue) != null) {
									interfacedata.setThevalue(ifEntity_ifStatus.get(interfaceValue).toString());
								} else {
									interfacedata.setThevalue("unknow");
								}
							} else {
								if (scale[j] == 0) {
									interfacedata.setThevalue(interfaceValue);
								} else {
									if (interfaceValue != null) {
										interfacedata.setThevalue(Long.toString(Long.parseLong(interfaceValue) / scale[j]));
									} else {
										interfacedata.setThevalue("0");
									}
								}
							}
							interfacedata.setChname(chname[j]);
							if ("ifPhysAddress".equals(interfacedata.getEntity())) {
								if ((interfacedata.getThevalue() == null) || (interfacedata.getThevalue().length() > 0 && !interfacedata.getThevalue().contains(":"))) {// mac��ַ�ַ������ܳ���
									interfacedata.setThevalue("--");
								}
							}
							interfaceVector.addElement(interfacedata);
						}
					}
				} 
				
				if(interfaceVector.size() == 0) {
					//û�вɼ�������
					interfacedata = new Interfacecollectdata();
					interfacedata.setIpaddress(host.getIpAddress());
					interfacedata.setCollecttime(date);
					interfacedata.setCategory("Interface");
					interfacedata.setEntity("");
					interfacedata.setSubentity("unknown");
					interfacedata.setRestype("static");
					interfacedata.setUnit("");
					interfacedata.setThevalue("");
					interfaceVector.addElement(interfacedata);
				} 

				// ����˿�����
				DecimalFormat df = new DecimalFormat("#.##");
				String octetsKey = "";
				String lastOctetsStringValue = "";
				BigInteger lastOctetsBigIntValue = new BigInteger("0");// ��һ��������¼(���ñ���)
				BigInteger currentOctetsBigIntValue = new BigInteger("0");// ����������¼(���ñ���)
				BigInteger octetsBetween = new BigInteger("0");// ������ֵ(���ñ���)
				BigInteger flowSpeed = new BigInteger("0");// �˿�����
				double interfaceBandwidthUsedPercent = 0.0;// �˿ڴ���������

				Hashtable lastInUcastPktsHash = new Hashtable();
				Hashtable lastInNucastPktsHash = new Hashtable();

				Hashtable lastOutUcastPktsHash = new Hashtable();
				Hashtable lastOutNucastPktsHash = new Hashtable();
				for (int i = 0; i < interfaceVector.size(); i++) {
					Interfacecollectdata interfaceCollectData = (Interfacecollectdata) interfaceVector.get(i);
					if (interfaceCollectData.getSubentity().equals("unknown")) {
						continue;
					}
					String ifIndex = interfaceCollectData.getSubentity();

					if (interfaceCollectData.getEntity().equals("ifInOctets")) {
						currentOctetsBigIntValue = new BigInteger(interfaceCollectData.getThevalue());
						octetsKey = "ifInOctets:" + ifIndex;
						if (lastOctetsHash.get(octetsKey) != null) {
							lastOctetsStringValue = lastOctetsHash.get(octetsKey).toString();
							if (lastOctetsStringValue != null && !lastOctetsStringValue.equals("")) {
								lastOctetsBigIntValue = new BigInteger(lastOctetsStringValue);
							}
						}

						// ��ʱ����ת����
						if (currentOctetsBigIntValue.compareTo(lastOctetsBigIntValue) < 0) {
							if (host.getSnmpversion() == 0) {
								// 32λ����
								currentOctetsBigIntValue = currentOctetsBigIntValue.add(new BigInteger(4294967296L + ""));
							} else if (host.getSnmpversion() == 1 || host.getSnmpversion() == 2) {
								// 64λ����
								currentOctetsBigIntValue = currentOctetsBigIntValue.add(new BigInteger("18446744073709551615"));
							}
						}

						// �����ǵ�һ�μ��˿�,��ô�����ֵû������
						if (longInterval.compareTo(new BigInteger("0")) != 0) {
							// ������-ǰ����
							octetsBetween = currentOctetsBigIntValue.subtract(lastOctetsBigIntValue);
							flowSpeed = octetsBetween.divide(longInterval);// ����ʱ���õ�����(B/s)
						}

						// ʱ�������ڰ����С
						Packs packs = new Packs();
						packs.setIpaddress(host.getIpAddress());
						packs.setCollecttime(date);
						packs.setCategory("Interface");
						packs.setEntity("InCastPkts");
						packs.setSubentity(interfaceCollectData.getSubentity());
						packs.setRestype("dynamic");
						packs.setUnit(interfaceCollectData.getUnit());// byte(�ֽ�)
						packs.setChname("��ڰ���С");
						if (!lastOctetsStringValue.equals("0")) {
							packs.setThevalue(octetsBetween + "");// ��ֵ
						} else {
							packs.setThevalue("0");
						}
						packsVector.add(packs);

						// �����ڴ�
						if (ShareData.getPacksdata(host.getIpAddress() + ":" + ifIndex) != null) {
							ShareData.getPacksdata(host.getIpAddress() + ":" + ifIndex).put("AllInCastPkts" + ":" + ifIndex, octetsBetween + "");
						} else {
							Hashtable inOctetsBetweenHash = new Hashtable();
							inOctetsBetweenHash.put("AllInCastPkts" + ":" + ifIndex, octetsBetween + "");
							ShareData.setPacksdata(host.getIpAddress() + ":" + ifIndex, inOctetsBetweenHash);
						}

						// ƽ����������=��ifInOctets/T
						utilhdx = new UtilHdx();
						utilhdx.setIpaddress(host.getIpAddress());
						utilhdx.setCollecttime(date);
						utilhdx.setCategory("Interface");
						utilhdx.setEntity("InBandwidthUtilHdx");
						utilhdx.setSubentity(ifIndex);
						utilhdx.setRestype("dynamic");
						utilhdx.setUnit("kb/s");// ͳһ�������λ
						utilhdx.setChname(ifIndex + "�˿��������");
						utilhdx.setThevalue(df.format(flowSpeed.longValue() * 8 / 1000));// ����
						utilhdxVector.addElement(utilhdx);

						if (portList != null && portList.contains("*" + ifIndex + ":" + 1)) {
							allInOctetsSpeed = allInOctetsSpeed.add(flowSpeed);// (B/s)
							allOctetsSpeed = allOctetsSpeed.add(flowSpeed);
						}

						// ����������=����ifInOctets*8��/��ifSpeed*T��*100%
						utilhdxPerc = new UtilHdxPerc();
						utilhdxPerc.setIpaddress(host.getIpAddress());
						utilhdxPerc.setCollecttime(date);
						utilhdxPerc.setCategory("Interface");
						utilhdxPerc.setEntity("InBandwidthUtilHdxPerc");
						utilhdxPerc.setSubentity(ifIndex);
						utilhdxPerc.setRestype("dynamic");
						utilhdxPerc.setUnit("%");
						utilhdxPerc.setChname(ifIndex + "�˿���ڴ���������");
						double ifSpeed = 0.0;
						String ifSpeedString = "0";
						if (interfaceSpeedHash.get(ifIndex) != null) {
							ifSpeedString = interfaceSpeedHash.get(ifIndex).toString();
						}
						if (!"0".equalsIgnoreCase(ifSpeedString)) {
							ifSpeed = Double.parseDouble(ifSpeedString);
							interfaceBandwidthUsedPercent = flowSpeed.longValue() * 8 / ifSpeed * 100;
						}else{
							interfaceBandwidthUsedPercent=0;
						}
						utilhdxPerc.setThevalue(Double.toString(interfaceBandwidthUsedPercent));// ��ڴ���������
						utilhdxPercVector.addElement(utilhdxPerc);

						// ������ȡ�õ��ۼ�������������ڴ�
						octetsHash.put(octetsKey, interfaceCollectData.getThevalue());

					}

					if (interfaceCollectData.getEntity().equalsIgnoreCase("ifInDiscards") || interfaceCollectData.getEntity().equalsIgnoreCase("ifInErrors")) {
						long inUcastPktsBetween = 0;
						long inNucastPktsBetween = 0;

						for (int j = 0; j < interfaceVector.size(); j++) {
							Interfacecollectdata tempInterfaceData_ = (Interfacecollectdata) interfaceVector.get(i);
							// �ж���ڵ���
							// �ֱ�ȡ�ñ��κ��ϴε����İ���
							if (tempInterfaceData_.getEntity().equalsIgnoreCase("ifInUcastPkts")) {
								String ifIndex_ = tempInterfaceData_.getSubentity();
								if (!ifIndex_.equals(ifIndex)) {
									continue;
								}
								lastInUcastPktsHash = ShareData.getUcastpktsdata(host.getIpAddress() + ":" + ifIndex_);

								String lastInUcastPktsValueString = "";
								long lastInUcastPktsValue = 0;
								long currentInUcastPktsValue = 0;

								if (lastInUcastPktsHash != null) {
									if (lastInUcastPktsHash.get("AllInUcastPkts" + ":" + ifIndex_) != null) {
										lastInUcastPktsValueString = lastInUcastPktsHash.get("AllInUcastPkts" + ":" + ifIndex_).toString();
										if (lastInUcastPktsValueString != null && !lastInUcastPktsValueString.equals("")) {
											lastInUcastPktsValue = Long.parseLong(lastInUcastPktsValueString);
										}
									}
								}
								if (tempInterfaceData_.getThevalue() != null && !tempInterfaceData_.getThevalue().equals("")) {
									currentInUcastPktsValue = Long.parseLong(tempInterfaceData_.getThevalue());
								}

								if (longInterval.compareTo(new BigInteger("0")) != 0) {
									inUcastPktsBetween = currentInUcastPktsValue - lastInUcastPktsValue;
								}
								// �����ڴ�
								if (ShareData.getUcastpktsdata(host.getIpAddress() + ":" + ifIndex_) != null) {
									ShareData.getUcastpktsdata(host.getIpAddress() + ":" + ifIndex_).put("AllInUcastPkts" + ":" + ifIndex_, currentInUcastPktsValue + "");
								} else {
									Hashtable allInUcastPktsHash = new Hashtable();
									allInUcastPktsHash.put("AllInUcastPkts" + ":" + ifIndex_, currentInUcastPktsValue + "");
									ShareData.setUcastpktsdata(host.getIpAddress() + ":" + ifIndex_, allInUcastPktsHash);
								}
							}
							// �ж���ڷǵ���
							// �ֱ�ȡ�ñ��κ��ϴηǵ����İ���
							if (tempInterfaceData_.getEntity().equalsIgnoreCase("ifInNucastPkts")) {
								String ifIndex_ = tempInterfaceData_.getSubentity();
								if (!ifIndex_.equals(ifIndex)) {
									continue;
								}

								lastInNucastPktsHash = ShareData.getNucastpktsdata(host.getIpAddress() + ":" + ifIndex_);
								String lastInNucastPktsValueString = "";
								long lastInNucastPktsValue = 0;
								long currentInNucastPktsValue = 0;

								if (lastInNucastPktsHash != null) {
									if (lastInNucastPktsHash.get("AllInNucastPkts" + ":" + ifIndex_) != null) {
										lastInNucastPktsValueString = lastInNucastPktsHash.get("AllInNucastPkts" + ":" + ifIndex_).toString();
										if (lastInNucastPktsValueString != null && !lastInNucastPktsValueString.equals("")) {
											lastInNucastPktsValue = Long.parseLong(lastInNucastPktsValueString);
										}
									}
								}
								if (tempInterfaceData_.getThevalue() != null && !tempInterfaceData_.getThevalue().equals("")) {
									currentInNucastPktsValue = Long.parseLong(tempInterfaceData_.getThevalue());
								}
								if (longInterval.compareTo(new BigInteger("0")) != 0) {
									inUcastPktsBetween = currentInNucastPktsValue - lastInNucastPktsValue;
								}
								// �����ڴ�
								if (ShareData.getNucastpktsdata(host.getIpAddress() + ":" + ifIndex_) != null) {
									ShareData.getNucastpktsdata(host.getIpAddress() + ":" + ifIndex_).put("AllInNucastPkts" + ":" + ifIndex_, currentInNucastPktsValue + "");
								} else {
									Hashtable allInNucastPktsHash = new Hashtable();
									allInNucastPktsHash.put("AllInNucastPkts" + ":" + ifIndex_, currentInNucastPktsValue + "");
									ShareData.setUcastpktsdata(host.getIpAddress() + ":" + ifIndex_, allInNucastPktsHash);
								}

							}
							tempInterfaceData_ = null;
						}

						if (interfaceCollectData.getEntity().equalsIgnoreCase("ifInDiscards")) {

							Hashtable lastDiscardsHash = ShareData.getDiscardsdata(host.getIpAddress() + ":" + ifIndex);
							String lastInDiscardsValueString = "";
							long lastInDiscardsValue = 0;
							long currentInDiscardsValue = 0;

							if (lastDiscardsHash != null) {
								if (lastDiscardsHash.get("AllInDiscards" + ":" + ifIndex) != null) {
									lastInDiscardsValueString = lastDiscardsHash.get("AllInDiscards" + ":" + ifIndex).toString();
									if (lastInDiscardsValueString != null && !lastInDiscardsValueString.equals("")) {
										lastInDiscardsValue = Long.parseLong(lastInDiscardsValueString);
									}
								}
							}
							if (interfaceCollectData.getThevalue() != null && !interfaceCollectData.getThevalue().equals("")) {
								currentInDiscardsValue = Long.parseLong(interfaceCollectData.getThevalue());
							}

							DiscardsPerc discardsPerc = new DiscardsPerc();
							discardsPerc.setIpaddress(host.getIpAddress());
							discardsPerc.setCollecttime(date);
							discardsPerc.setCategory("Interface");
							discardsPerc.setEntity("InDiscardsPerc");
							discardsPerc.setSubentity(ifIndex);
							discardsPerc.setRestype("dynamic");
							discardsPerc.setUnit("%");
							discardsPerc.setChname("��ڶ�����");
							double indiscardsPerc = 0.0;
							if ((inUcastPktsBetween + inNucastPktsBetween) == 0) {
								indiscardsPerc = 0;
							} else {
								if ((inUcastPktsBetween + inNucastPktsBetween) > 0) {
									indiscardsPerc = (currentInDiscardsValue - lastInDiscardsValue) / (inUcastPktsBetween + inNucastPktsBetween);
								} else {
									indiscardsPerc = 0;
								}
							}
							discardsPerc.setThevalue(Double.toString(indiscardsPerc));
							discardsPercVector.add(discardsPerc);

							// �����ڴ�
							if (ShareData.getDiscardsdata(host.getIpAddress() + ":" + ifIndex) != null) {
								ShareData.getDiscardsdata(host.getIpAddress() + ":" + ifIndex).put("AllInDiscards" + ":" + ifIndex, currentInDiscardsValue + "");
							} else {
								Hashtable allInDiscardsHash = new Hashtable();
								allInDiscardsHash.put("AllInDiscards" + ":" + ifIndex, currentInDiscardsValue + "");
								ShareData.setDiscardsdata(host.getIpAddress() + ":" + ifIndex, allInDiscardsHash);
							}
						} else if (interfaceCollectData.getEntity().equalsIgnoreCase("ifInErrors")) {

							Hashtable lastErrorsHash = ShareData.getErrorsdata(host.getIpAddress() + ":" + ifIndex);
							String lastInErrorsValueString = "";
							long lastInErrorsValue = 0;
							long currentInErrorsValue = 0;

							if (lastErrorsHash != null) {
								if (lastErrorsHash.get("AllInErrors" + ":" + ifIndex) != null) {
									lastInErrorsValueString = lastErrorsHash.get("AllInErrors" + ":" + ifIndex).toString();
									if (lastInErrorsValueString != null && !lastInErrorsValueString.equals("")) {
										lastInErrorsValue = Long.parseLong(lastInErrorsValueString);
									}
								}
							}
							if (interfaceCollectData.getThevalue() != null && !interfaceCollectData.getThevalue().equals("")) {
								currentInErrorsValue = Long.parseLong(interfaceCollectData.getThevalue());
							}

							ErrorsPerc errorsPerc = new ErrorsPerc();
							errorsPerc.setIpaddress(host.getIpAddress());
							errorsPerc.setCollecttime(date);
							errorsPerc.setCategory("Interface");
							errorsPerc.setEntity("InErrorsPerc");
							errorsPerc.setSubentity(ifIndex);
							errorsPerc.setRestype("dynamic");
							errorsPerc.setUnit("%");
							errorsPerc.setChname("��ڴ����");
							double inerrorsPerc = 0.0;
							if ((inUcastPktsBetween + inNucastPktsBetween) == 0) {
								inerrorsPerc = 0;
							} else {
								if ((inUcastPktsBetween + inNucastPktsBetween) > 0) {
									inerrorsPerc = (currentInErrorsValue - lastInErrorsValue) / (inUcastPktsBetween + inNucastPktsBetween);
								} else {
									inerrorsPerc = 0;
								}
							}
							errorsPerc.setThevalue(Double.toString(inerrorsPerc));
							errorsPercVector.add(errorsPerc);

							// �����ڴ�
							if (ShareData.getErrorsdata(host.getIpAddress() + ":" + ifIndex) != null) {
								ShareData.getErrorsdata(host.getIpAddress() + ":" + ifIndex).put("AllInErrors" + ":" + ifIndex, currentInErrorsValue + "");
							} else {
								Hashtable allInErrorsHash = new Hashtable();
								allInErrorsHash.put("AllInErrors" + ":" + ifIndex, currentInErrorsValue + "");
								ShareData.setErrorsdata(host.getIpAddress() + ":" + ifIndex, allInErrorsHash);
							}

						}
					}

					if (interfaceCollectData.getEntity().equals("ifOutOctets")) {
						currentOctetsBigIntValue = new BigInteger(interfaceCollectData.getThevalue());
						octetsKey = "ifOutOctets:" + ifIndex;
						if (lastOctetsHash.get(octetsKey) != null) {
							lastOctetsStringValue = lastOctetsHash.get(octetsKey).toString();
						}
						if (lastOctetsStringValue != null && !lastOctetsStringValue.equals("")) {
							lastOctetsBigIntValue = new BigInteger(lastOctetsStringValue);
						}
						if (currentOctetsBigIntValue.compareTo(lastOctetsBigIntValue) < 0) {
							if (host.getSnmpversion() == 0) {
								// 32λ����
								currentOctetsBigIntValue = currentOctetsBigIntValue.add(new BigInteger(4294967296L + ""));
							} else if (host.getSnmpversion() == 1 || host.getSnmpversion() == 2) {
								// 64λ����
								currentOctetsBigIntValue = currentOctetsBigIntValue.add(new BigInteger("18446744073709551615"));
							}
						}

						if (longInterval.compareTo(new BigInteger("0")) != 0) {
							// ������-ǰ����
							octetsBetween = currentOctetsBigIntValue.subtract(lastOctetsBigIntValue);
							flowSpeed = octetsBetween.divide(longInterval);// ����ʱ���õ�����
						}

						// ʱ�������ڰ����С
						Packs packs = new Packs();
						packs.setIpaddress(host.getIpAddress());
						packs.setCollecttime(date);
						packs.setCategory("Interface");
						packs.setEntity("OutCastPkts");
						packs.setSubentity(interfaceCollectData.getSubentity());
						packs.setRestype("dynamic");
						packs.setUnit(interfaceCollectData.getUnit());// byte
						packs.setChname("���ڰ���С");
						if (!lastOctetsStringValue.equals("0")) {
							packs.setThevalue(octetsBetween + "");// ��ֵ
						} else {
							packs.setThevalue("0");
						}
						packsVector.add(packs);

						// �����ڴ�
						if (ShareData.getPacksdata(host.getIpAddress() + ":" + ifIndex) != null) {
							ShareData.getPacksdata(host.getIpAddress() + ":" + ifIndex).put("AllOutCastPkts" + ":" + ifIndex, octetsBetween + "");
						} else {
							Hashtable allOutOctetsBetweenHash = new Hashtable();
							allOutOctetsBetweenHash.put("AllOutCastPkts" + ":" + ifIndex, octetsBetween + "");
							ShareData.setPacksdata(host.getIpAddress() + ":" + ifIndex, allOutOctetsBetweenHash);
						}

						// ƽ���������=��ifOutOctets/T
						utilhdx = new UtilHdx();
						utilhdx.setIpaddress(host.getIpAddress());
						utilhdx.setCollecttime(date);
						utilhdx.setCategory("Interface");
						utilhdx.setEntity("OutBandwidthUtilHdx");
						utilhdx.setSubentity(ifIndex);
						utilhdx.setRestype("dynamic");
						utilhdx.setUnit("kb/s");
						utilhdx.setChname(ifIndex + "�˿ڳ�������");
						utilhdx.setThevalue(df.format(flowSpeed.longValue() * 8 / 1000));// ����
						utilhdxVector.addElement(utilhdx);

						if (portList != null && portList.contains("*" + ifIndex + ":" + 1)) {
							allOutOctetsSpeed = allOutOctetsSpeed.add(flowSpeed);
							allOctetsSpeed = allOctetsSpeed.add(flowSpeed);
						}

						// ���������=����ifOutOctets*8��/��ifSpeed*T��*100%
						utilhdxPerc = new UtilHdxPerc();
						utilhdxPerc.setIpaddress(host.getIpAddress());
						utilhdxPerc.setCollecttime(date);
						utilhdxPerc.setCategory("Interface");
						utilhdxPerc.setEntity("OutBandwidthUtilHdxPerc");
						utilhdxPerc.setSubentity(ifIndex);
						utilhdxPerc.setRestype("dynamic");
						utilhdxPerc.setUnit("%");
						utilhdxPerc.setChname(ifIndex + "�˿ڳ��ڴ���������");
						double ifSpeed = 0.0;
						String ifSpeedString = "0";
						if (interfaceSpeedHash.get(ifIndex) != null) {
							ifSpeedString = interfaceSpeedHash.get(ifIndex).toString();
						}
						if (!"0".equalsIgnoreCase(ifSpeedString)) {
							ifSpeed = Double.parseDouble(ifSpeedString);
							interfaceBandwidthUsedPercent = flowSpeed.intValue() * 8 / ifSpeed * 100;
						}else{
							interfaceBandwidthUsedPercent=0;
						}
						utilhdxPerc.setThevalue(Double.toString(interfaceBandwidthUsedPercent));// ����������
						utilhdxPercVector.addElement(utilhdxPerc);

						// ������ȡ�õ��ۼƳ������������ڴ�
						octetsHash.put(octetsKey, interfaceCollectData.getThevalue());
					}

					// ������ڶ����ʺͳ��ڴ����
					if (interfaceCollectData.getEntity().equalsIgnoreCase("ifOutDiscards") || interfaceCollectData.getEntity().equalsIgnoreCase("ifOutErrors")) {

						long outUcastPktsBetween = 0;
						long outNucastPktsBetween = 0;

						for (int j = 0; j < interfaceVector.size(); j++) {
							Interfacecollectdata tempInterfaceData_ = (Interfacecollectdata) interfaceVector.get(i);
							// �жϳ��ڵ���
							// �ֱ�ȡ�ñ��κ��ϴε����İ���
							if (tempInterfaceData_.getEntity().equalsIgnoreCase("ifOutUcastPkts")) {
								String ifIndex_ = tempInterfaceData_.getSubentity();
								if (!ifIndex_.equals(ifIndex)) {
									continue;
								}
								lastOutUcastPktsHash = ShareData.getUcastpktsdata(host.getIpAddress() + ":" + ifIndex_);

								String lastOutUcastPktsValueString = "";
								long lastOutUcastPktsValue = 0;
								long currentOutUcastPktsValue = 0;

								if (lastOutUcastPktsHash != null) {
									if (lastOutUcastPktsHash.get("AllOutUcastPkts" + ":" + ifIndex_) != null) {
										lastOutUcastPktsValueString = lastOutUcastPktsHash.get("AllOutUcastPkts" + ":" + ifIndex_).toString();
										if (lastOutUcastPktsValueString != null && !lastOutUcastPktsValueString.equals("")) {
											lastOutUcastPktsValue = Long.parseLong(lastOutUcastPktsValueString);
										}
									}
								}
								if (tempInterfaceData_.getThevalue() != null && !tempInterfaceData_.getThevalue().equals("")) {
									currentOutUcastPktsValue = Long.parseLong(tempInterfaceData_.getThevalue());
								}
								if (longInterval.compareTo(new BigInteger("0")) != 0) {
									outUcastPktsBetween = currentOutUcastPktsValue - lastOutUcastPktsValue;
								}
								// �����ڴ�
								if (ShareData.getUcastpktsdata(host.getIpAddress() + ":" + ifIndex_) != null) {
									ShareData.getUcastpktsdata(host.getIpAddress() + ":" + ifIndex_).put("AllOutUcastPkts" + ":" + ifIndex_, currentOutUcastPktsValue + "");
								} else {
									Hashtable allOutUcastPktsHash = new Hashtable();
									allOutUcastPktsHash.put("AllOutUcastPkts" + ":" + ifIndex_, currentOutUcastPktsValue + "");
									ShareData.setUcastpktsdata(host.getIpAddress() + ":" + ifIndex_, allOutUcastPktsHash);
								}
							}
							// �ж���ڷǵ���
							// �ֱ�ȡ�ñ��κ��ϴηǵ����İ���
							if (tempInterfaceData_.getEntity().equalsIgnoreCase("ifOutNucastPkts")) {
								String ifIndex_ = tempInterfaceData_.getSubentity();
								if (!ifIndex_.equals(ifIndex)) {
									continue;
								}

								lastOutNucastPktsHash = ShareData.getNucastpktsdata(host.getIpAddress() + ":" + ifIndex_);
								String lastOutNucastPktsValueString = "";
								long lastOutNucastPktsValue = 0;
								long currentOutNucastPktsValue = 0;

								if (lastOutNucastPktsHash != null) {
									if (lastOutNucastPktsHash.get("AllOutNucastPkts" + ":" + ifIndex_) != null) {
										lastOutNucastPktsValueString = lastOutNucastPktsHash.get("AllOutNucastPkts" + ":" + ifIndex_).toString();
										if (lastOutNucastPktsValueString != null && !lastOutNucastPktsValueString.equals("")) {
											lastOutNucastPktsValue = Long.parseLong(lastOutNucastPktsValueString);
										}
									}
								}
								if (tempInterfaceData_.getThevalue() != null && !tempInterfaceData_.getThevalue().equals("")) {
									currentOutNucastPktsValue = Long.parseLong(tempInterfaceData_.getThevalue());
								}

								if (longInterval.compareTo(new BigInteger("0")) != 0) {
									outUcastPktsBetween = currentOutNucastPktsValue - lastOutNucastPktsValue;
								}
								// �����ڴ�
								if (ShareData.getNucastpktsdata(host.getIpAddress() + ":" + ifIndex_) != null) {
									ShareData.getNucastpktsdata(host.getIpAddress() + ":" + ifIndex_).put("AllOutNucastPkts" + ":" + ifIndex_, currentOutNucastPktsValue + "");
								} else {
									Hashtable allOutNucastPktsHash = new Hashtable();
									allOutNucastPktsHash.put("AllOutNucastPkts" + ":" + ifIndex_, currentOutNucastPktsValue + "");
									ShareData.setUcastpktsdata(host.getIpAddress() + ":" + ifIndex_, allOutNucastPktsHash);
								}

							}
							tempInterfaceData_ = null;
						}

						if (interfaceCollectData.getEntity().equalsIgnoreCase("ifOutDiscards")) {

							Hashtable lastDiscardsHash = ShareData.getDiscardsdata(host.getIpAddress() + ":" + ifIndex);
							String lastOutDiscardsValueString = "";
							long lastOutDiscardsValue = 0;
							long currentOutDiscardsValue = 0;

							if (lastDiscardsHash != null) {
								if (lastDiscardsHash.get("AllOutDiscards" + ":" + ifIndex) != null) {
									lastOutDiscardsValueString = lastDiscardsHash.get("AllOutDiscards" + ":" + ifIndex).toString();
									if (lastOutDiscardsValueString != null && !lastOutDiscardsValueString.equals("")) {
										lastOutDiscardsValue = Long.parseLong(lastOutDiscardsValueString);
									}
								}
							}
							if (interfaceCollectData.getThevalue() != null && !interfaceCollectData.getThevalue().equals("")) {
								currentOutDiscardsValue = Long.parseLong(interfaceCollectData.getThevalue());
							}

							DiscardsPerc discardsPerc = new DiscardsPerc();
							discardsPerc.setIpaddress(host.getIpAddress());
							discardsPerc.setCollecttime(date);
							discardsPerc.setCategory("Interface");
							discardsPerc.setEntity("OutDiscardsPerc");
							discardsPerc.setSubentity(ifIndex);
							discardsPerc.setRestype("dynamic");
							discardsPerc.setUnit("%");
							discardsPerc.setChname("���ڶ�����");
							double indiscardserc = 0.0;
							if ((outUcastPktsBetween + outNucastPktsBetween) == 0) {
								indiscardserc = 0;
							} else {
								if ((outUcastPktsBetween + outNucastPktsBetween) > 0) {
									indiscardserc = (currentOutDiscardsValue - lastOutDiscardsValue) / (outUcastPktsBetween + outNucastPktsBetween);
								} else {
									indiscardserc = 0;
								}
							}
							discardsPerc.setThevalue(Double.toString(indiscardserc));
							discardsPercVector.add(discardsPerc);

							// �����ڴ�
							if (ShareData.getDiscardsdata(host.getIpAddress() + ":" + ifIndex) != null) {
								ShareData.getDiscardsdata(host.getIpAddress() + ":" + ifIndex).put("AllOutDiscards" + ":" + ifIndex, currentOutDiscardsValue + "");
							} else {
								Hashtable allOutDiscardsHash = new Hashtable();
								allOutDiscardsHash.put("AllOutDiscards" + ":" + ifIndex, currentOutDiscardsValue + "");
								ShareData.setDiscardsdata(host.getIpAddress() + ":" + ifIndex, allOutDiscardsHash);
							}
						} else if (interfaceCollectData.getEntity().equalsIgnoreCase("ifOutErrors")) {

							Hashtable lastErrorsHash = ShareData.getErrorsdata(host.getIpAddress() + ":" + ifIndex);
							String lastOutErrorsValueString = "";
							long lastOutErrorsValue = 0;
							long currentOutErrorsValue = 0;

							if (lastErrorsHash != null) {
								if (lastErrorsHash.get("AllOutErrors" + ":" + ifIndex) != null) {
									lastOutErrorsValueString = lastErrorsHash.get("AllOutErrors" + ":" + ifIndex).toString();
									if (lastOutErrorsValueString != null && !lastOutErrorsValueString.equals("")) {
										lastOutErrorsValue = Long.parseLong(lastOutErrorsValueString);
									}
								}
							}
							if (interfaceCollectData.getThevalue() != null && !interfaceCollectData.getThevalue().equals("")) {
								currentOutErrorsValue = Long.parseLong(interfaceCollectData.getThevalue());
							}

							ErrorsPerc errorsPerc = new ErrorsPerc();
							errorsPerc.setIpaddress(host.getIpAddress());
							errorsPerc.setCollecttime(date);
							errorsPerc.setCategory("Interface");
							errorsPerc.setEntity("OutErrorsPerc");
							errorsPerc.setSubentity(ifIndex);
							errorsPerc.setRestype("dynamic");
							errorsPerc.setUnit("%");
							errorsPerc.setChname("���ڴ����");
							double inerrorsPerc = 0.0;
							if ((outUcastPktsBetween + outNucastPktsBetween) == 0) {
								inerrorsPerc = 0;
							} else {
								if ((outUcastPktsBetween + outNucastPktsBetween) > 0) {
									inerrorsPerc = (currentOutErrorsValue - lastOutErrorsValue) / (outUcastPktsBetween + outNucastPktsBetween);
								} else {
									inerrorsPerc = 0;
								}
							}
							errorsPerc.setThevalue(Double.toString(inerrorsPerc));
							errorsPercVector.add(errorsPerc);

							// �����ڴ�
							if (ShareData.getErrorsdata(host.getIpAddress() + ":" + ifIndex) != null) {
								ShareData.getErrorsdata(host.getIpAddress() + ":" + ifIndex).put("AllOutErrors" + ":" + ifIndex, currentOutErrorsValue + "");
							} else {
								Hashtable allOutErrorsHash = new Hashtable();
								allOutErrorsHash.put("AllOutErrors" + ":" + ifIndex, currentOutErrorsValue + "");
								ShareData.setErrorsdata(host.getIpAddress() + ":" + ifIndex, allOutErrorsHash);
							}
						}
					}

					// ��ڶಥ����
					if (interfaceCollectData.getEntity().equalsIgnoreCase("ifInMulticastPkts")) {
						octetsKey = interfaceCollectData.getEntity() + ":" + interfaceCollectData.getSubentity();// Ψһ��ʶ

						BigInteger lastInMulticastOctetsValue = new BigInteger("0");// �����ϴβɼ���InMulticastOctets
						BigInteger currentInMulticastOctetsValue = new BigInteger("0");// ���汾�βɼ���InMulticastOctets
						BigInteger InMulticastOctetsBetween = new BigInteger("0");// ������ڶಥ���ݴ�С��ֵ

						String lastInMulticastOctetsValueString = "";
						if (lastOctetsHash.get(octetsKey) != null) {
							lastInMulticastOctetsValueString = lastOctetsHash.get(octetsKey).toString();// ȡ���ϴλ�õ�InMulticastOctets
							if (lastInMulticastOctetsValueString != null && !lastInMulticastOctetsValueString.equals("")) {
								lastInMulticastOctetsValue = new BigInteger(lastInMulticastOctetsValueString);
							}
						}

						if (interfaceCollectData.getThevalue() != null && !interfaceCollectData.getThevalue().equals("")) {
							currentInMulticastOctetsValue = new BigInteger(interfaceCollectData.getThevalue());
						}

						if (currentInMulticastOctetsValue.compareTo(lastInMulticastOctetsValue) < 0) {
							if (host.getSnmpversion() == 0) {
								currentInMulticastOctetsValue = currentInMulticastOctetsValue.add(new BigInteger(4294967296L + ""));// 32λ����
							} else if (host.getSnmpversion() == 1 || host.getSnmpversion() == 2) {
								currentInMulticastOctetsValue = currentInMulticastOctetsValue.add(new BigInteger("18446744073709551615"));// 64λ����
							}

						}
						if (longInterval.compareTo(new BigInteger("0")) != 0) {
							InMulticastOctetsBetween = currentInMulticastOctetsValue.subtract(lastInMulticastOctetsValue);
						}
						inPacks = new InPkts();
						inPacks.setIpaddress(host.getIpAddress());
						inPacks.setCollecttime(date);
						inPacks.setCategory("Interface");
						inPacks.setEntity("ifInMulticastPkts");
						inPacks.setSubentity(ifIndex);
						inPacks.setRestype("dynamic");
						inPacks.setUnit(interfaceCollectData.getUnit());
						inPacks.setChname("��ڶಥ");
						if (!lastInMulticastOctetsValueString.equals("0")) {
							inPacks.setThevalue(InMulticastOctetsBetween + "");// ��ֵ
						} else {
							inPacks.setThevalue("0");
						}
						inPacksVector.addElement(inPacks);
						// ������ȡ�õ��ۼƳ������������ڴ�
						octetsHash.put(octetsKey, interfaceCollectData.getThevalue());
					}
					// ��ڹ㲥����
					if (interfaceCollectData.getEntity().equalsIgnoreCase("ifInBroadcastPkts")) {
						octetsKey = interfaceCollectData.getEntity() + ":" + interfaceCollectData.getSubentity();// Ψһ��ʶ

						BigInteger lastInBroadcastOctetsValue = new BigInteger("0");// �����ϴβɼ���InMulticastOctets
						BigInteger currentInBroadcastOctetsValue = new BigInteger("0");// ���汾�βɼ���InMulticastOctets
						BigInteger InBroadcastOctetsBetween = new BigInteger("0");// ������ڶಥ���ݴ�С��ֵ

						String lastInBroadcastOctetsValueString = "";
						if (lastOctetsHash.get(octetsKey) != null) {
							lastInBroadcastOctetsValueString = lastOctetsHash.get(octetsKey).toString();// ȡ���ϴλ�õ�InMulticastOctets
							if (lastInBroadcastOctetsValueString != null && !lastInBroadcastOctetsValueString.equals("")) {
								lastInBroadcastOctetsValue = new BigInteger(lastInBroadcastOctetsValueString);
							}
						}

						if (interfaceCollectData.getThevalue() != null && !interfaceCollectData.getThevalue().equals("")) {
							currentInBroadcastOctetsValue = new BigInteger(interfaceCollectData.getThevalue());
						}

						if (currentInBroadcastOctetsValue.compareTo(lastInBroadcastOctetsValue) < 0) {
							if (host.getSnmpversion() == 0) {
								currentInBroadcastOctetsValue = currentInBroadcastOctetsValue.add(new BigInteger(4294967296L + ""));// 32λ����
							} else if (host.getSnmpversion() == 1 || host.getSnmpversion() == 2) {
								currentInBroadcastOctetsValue = currentInBroadcastOctetsValue.add(new BigInteger("18446744073709551615"));// 64λ����
							}

						}
						if (longInterval.compareTo(new BigInteger("0")) != 0) {
							InBroadcastOctetsBetween = currentInBroadcastOctetsValue.subtract(lastInBroadcastOctetsValue);
						}
						inPacks = new InPkts();
						inPacks.setIpaddress(host.getIpAddress());
						inPacks.setCollecttime(date);
						inPacks.setCategory("Interface");
						inPacks.setEntity("ifInBroadcastPkts");
						inPacks.setSubentity(ifIndex);
						inPacks.setRestype("dynamic");
						inPacks.setUnit(interfaceCollectData.getUnit());
						inPacks.setChname("��ڹ㲥");
						if (!lastInBroadcastOctetsValueString.equals("0")) {
							inPacks.setThevalue(InBroadcastOctetsBetween + "");// ��ֵ
						} else {
							inPacks.setThevalue("0");
						}
						inPacksVector.addElement(inPacks);
						// ������ȡ�õ��ۼƳ������������ڴ�
						octetsHash.put(octetsKey, interfaceCollectData.getThevalue());
					}
					// ���ڶಥ����
					if (interfaceCollectData.getEntity().equalsIgnoreCase("ifOutMulticastPkts")) {
						octetsKey = interfaceCollectData.getEntity() + ":" + interfaceCollectData.getSubentity();// Ψһ��ʶ

						BigInteger lastOutMulticastOctetsValue = new BigInteger("0");// �����ϴβɼ���InMulticastOctets
						BigInteger currentOutMulticastOctetsValue = new BigInteger("0");// ���汾�βɼ���InMulticastOctets
						BigInteger OutMulticastOctetsBetween = new BigInteger("0");// ���γ��ڶಥ���ݴ�С��ֵ

						String lastOutMulticastOctetsValueString = "";
						if (lastOctetsHash.get(octetsKey) != null) {
							lastOutMulticastOctetsValueString = lastOctetsHash.get(octetsKey).toString();// ȡ���ϴλ�õ�InMulticastOctets
							if (lastOutMulticastOctetsValueString != null && !lastOutMulticastOctetsValueString.equals("")) {
								lastOutMulticastOctetsValue = new BigInteger(lastOutMulticastOctetsValueString);
							}
						}

						if (interfaceCollectData.getThevalue() != null && !interfaceCollectData.getThevalue().equals("")) {
							currentOutMulticastOctetsValue = new BigInteger(interfaceCollectData.getThevalue());
						}

						if (currentOutMulticastOctetsValue.compareTo(lastOutMulticastOctetsValue) < 0) {
							if (host.getSnmpversion() == 0) {
								currentOutMulticastOctetsValue = currentOutMulticastOctetsValue.add(new BigInteger(4294967296L + ""));// 32λ����
							} else if (host.getSnmpversion() == 1 || host.getSnmpversion() == 2) {
								currentOutMulticastOctetsValue = currentOutMulticastOctetsValue.add(new BigInteger("18446744073709551615"));// 64λ����
							}

						}
						if (longInterval.compareTo(new BigInteger("0")) != 0) {
							OutMulticastOctetsBetween = currentOutMulticastOctetsValue.subtract(lastOutMulticastOctetsValue);
						}
						outPacks = new OutPkts();
						outPacks.setIpaddress(host.getIpAddress());
						outPacks.setCollecttime(date);
						outPacks.setCategory("Interface");
						outPacks.setEntity("ifOutMulticastPkts");
						outPacks.setSubentity(ifIndex);
						outPacks.setRestype("dynamic");
						outPacks.setUnit(interfaceCollectData.getUnit());
						outPacks.setChname("���ڶಥ");
						if (!lastOutMulticastOctetsValueString.equals("0")) {
							outPacks.setThevalue(OutMulticastOctetsBetween + "");// ��ֵ
						} else {
							outPacks.setThevalue("0");
						}
						outPacksVector.addElement(outPacks);
						// ������ȡ�õ��ۼƳ������������ڴ�
						octetsHash.put(octetsKey, interfaceCollectData.getThevalue());
					}
					// ���ڹ㲥����
					if (interfaceCollectData.getEntity().equalsIgnoreCase("ifOutBroadcastPkts")) {
						octetsKey = interfaceCollectData.getEntity() + ":" + interfaceCollectData.getSubentity();// Ψһ��ʶ

						BigInteger lastOutBroadcastOctetsValue = new BigInteger("0");// �����ϴβɼ���InMulticastOctets
						BigInteger currentOutBroadcastOctetsValue = new BigInteger("0");// ���汾�βɼ���InMulticastOctets
						BigInteger OutBroadcastOctetsBetween = new BigInteger("0");// ������ڶಥ���ݴ�С��ֵ

						String lastOutBroadcastOctetsValueString = "";
						if (lastOctetsHash.get(octetsKey) != null) {
							lastOutBroadcastOctetsValueString = lastOctetsHash.get(octetsKey).toString();// ȡ���ϴλ�õ�InMulticastOctets
							if (lastOutBroadcastOctetsValueString != null && !lastOutBroadcastOctetsValueString.equals("")) {
								lastOutBroadcastOctetsValue = new BigInteger(lastOutBroadcastOctetsValueString);
							}
						}

						if (interfaceCollectData.getThevalue() != null && !interfaceCollectData.getThevalue().equals("")) {
							currentOutBroadcastOctetsValue = new BigInteger(interfaceCollectData.getThevalue());
						}

						if (currentOutBroadcastOctetsValue.compareTo(lastOutBroadcastOctetsValue) < 0) {
							if (host.getSnmpversion() == 0) {
								currentOutBroadcastOctetsValue = currentOutBroadcastOctetsValue.add(new BigInteger(4294967296L + ""));// 32λ����
							} else if (host.getSnmpversion() == 1 || host.getSnmpversion() == 2) {
								currentOutBroadcastOctetsValue = currentOutBroadcastOctetsValue.add(new BigInteger("18446744073709551615"));// 64λ����
							}

						}
						if (longInterval.compareTo(new BigInteger("0")) != 0) {
							OutBroadcastOctetsBetween = currentOutBroadcastOctetsValue.subtract(lastOutBroadcastOctetsValue);
						}
						outPacks = new OutPkts();
						outPacks.setIpaddress(host.getIpAddress());
						outPacks.setCollecttime(date);
						outPacks.setCategory("Interface");
						outPacks.setEntity("ifOutBroadcastPkts");
						outPacks.setSubentity(ifIndex);
						outPacks.setRestype("dynamic");
						outPacks.setUnit(interfaceCollectData.getUnit());
						outPacks.setChname("���ڹ㲥");
						if (!lastOutBroadcastOctetsValueString.equals("0")) {
							outPacks.setThevalue(OutBroadcastOctetsBetween + "");// ��ֵ
						} else {
							outPacks.setThevalue("0");
						}
						outPacksVector.addElement(outPacks);
						// ������ȡ�õ��ۼƳ������������ڴ�
						octetsHash.put(octetsKey, interfaceCollectData.getThevalue());
					}
					interfaceCollectData = null;
					ifIndex = null;

				}//���ݴ������

				AllUtilHdx allInutilhdx = new AllUtilHdx();
				allInutilhdx = new AllUtilHdx();
				allInutilhdx.setIpaddress(host.getIpAddress());
				allInutilhdx.setCollecttime(date);
				allInutilhdx.setCategory("Interface");
				allInutilhdx.setEntity("AllInBandwidthUtilHdx");
				allInutilhdx.setSubentity("AllInBandwidthUtilHdx");
				allInutilhdx.setRestype("dynamic");
				allInutilhdx.setUnit("kb/s");
				allInutilhdx.setChname("�������");
				allInutilhdx.setThevalue(Long.toString(allInOctetsSpeed.longValue() * 8 / 1000));
				allUtilhdxVector.addElement(allInutilhdx);

				AllUtilHdx alloututilhdx = new AllUtilHdx();
				alloututilhdx = new AllUtilHdx();
				alloututilhdx.setIpaddress(host.getIpAddress());
				alloututilhdx.setCollecttime(date);
				alloututilhdx.setCategory("Interface");
				alloututilhdx.setEntity("AllOutBandwidthUtilHdx");
				alloututilhdx.setSubentity("AllOutBandwidthUtilHdx");
				alloututilhdx.setRestype("dynamic");
				alloututilhdx.setUnit("kb/s");
				alloututilhdx.setChname("��������");
				alloututilhdx.setThevalue(Long.toString(allOutOctetsSpeed.longValue() * 8 / 1000));
				allUtilhdxVector.addElement(alloututilhdx);

				allUtilhdx = new AllUtilHdx();
				allUtilhdx.setIpaddress(host.getIpAddress());
				allUtilhdx.setCollecttime(date);
				allUtilhdx.setCategory("Interface");
				allUtilhdx.setEntity("AllBandwidthUtilHdx");
				allUtilhdx.setSubentity("AllBandwidthUtilHdx");
				allUtilhdx.setRestype("dynamic");
				allUtilhdx.setUnit("kb/s");
				allUtilhdx.setChname("�ۺ�����");
				allUtilhdx.setThevalue(Long.toString(allOctetsSpeed.longValue() * 8 / 1000));
				allUtilhdxVector.addElement(allUtilhdx);

				interfaceSpeedHash = null;
				octetsHash.put("collecttime", date);
				ShareData.setOctetsdata(host.getIpAddress(), octetsHash);

			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (!(ShareData.getSharedata().containsKey(host.getIpAddress()))) {
			Hashtable ipAllData = new Hashtable();
			if (ipAllData == null)
				ipAllData = new Hashtable();
			if (interfaceVector != null && interfaceVector.size() > 0 && !((Interfacecollectdata)interfaceVector.get(0)).getSubentity().equals("unknown"))
				ipAllData.put("interface", interfaceVector);
			if (allUtilhdxPercVector != null && allUtilhdxPercVector.size() > 0)
				ipAllData.put("allutilhdxperc", allUtilhdxPercVector);
			if (allUtilhdxVector != null && allUtilhdxVector.size() > 0)
				ipAllData.put("allutilhdx", allUtilhdxVector);
			if (utilhdxPercVector != null && utilhdxPercVector.size() > 0)
				ipAllData.put("utilhdxperc", utilhdxPercVector);
			if (utilhdxVector != null && utilhdxVector.size() > 0)
				ipAllData.put("utilhdx", utilhdxVector);
			if (discardsPercVector != null && discardsPercVector.size() > 0)
				ipAllData.put("discardsperc", discardsPercVector);
			if (errorsPercVector != null && errorsPercVector.size() > 0)
				ipAllData.put("errorsperc", errorsPercVector);
			if (allErrorsPercVector != null && allErrorsPercVector.size() > 0)
				ipAllData.put("allerrorsperc", allErrorsPercVector);
			if (allDiscardsPercVector != null && allDiscardsPercVector.size() > 0)
				ipAllData.put("alldiscardsperc", allDiscardsPercVector);
			if (packsVector != null && packsVector.size() > 0)
				ipAllData.put("packs", packsVector);
			if (inPacksVector != null && inPacksVector.size() > 0)
				ipAllData.put("inpacks", inPacksVector);
			if (outPacksVector != null && outPacksVector.size() > 0)
				ipAllData.put("outpacks", outPacksVector);
			ShareData.getSharedata().put(host.getIpAddress(), ipAllData);
		} else {
			if (interfaceVector != null && interfaceVector.size() > 0)
				((Hashtable) ShareData.getSharedata().get(host.getIpAddress())).put("interface", interfaceVector);
			if (allUtilhdxPercVector != null && allUtilhdxPercVector.size() > 0)
				((Hashtable) ShareData.getSharedata().get(host.getIpAddress())).put("allutilhdxperc", allUtilhdxPercVector);
			if (allUtilhdxVector != null && allUtilhdxVector.size() > 0)
				((Hashtable) ShareData.getSharedata().get(host.getIpAddress())).put("allutilhdx", allUtilhdxVector);
			if (utilhdxPercVector != null && utilhdxPercVector.size() > 0)
				((Hashtable) ShareData.getSharedata().get(host.getIpAddress())).put("utilhdxperc", utilhdxPercVector);
			if (utilhdxVector != null && utilhdxVector.size() > 0)
				((Hashtable) ShareData.getSharedata().get(host.getIpAddress())).put("utilhdx", utilhdxVector);
			if (discardsPercVector != null && discardsPercVector.size() > 0)
				((Hashtable) ShareData.getSharedata().get(host.getIpAddress())).put("discardsperc", discardsPercVector);
			if (errorsPercVector != null && errorsPercVector.size() > 0)
				((Hashtable) ShareData.getSharedata().get(host.getIpAddress())).put("errorsperc", errorsPercVector);
			if (allErrorsPercVector != null && allErrorsPercVector.size() > 0)
				((Hashtable) ShareData.getSharedata().get(host.getIpAddress())).put("allerrorsperc", allErrorsPercVector);
			if (allDiscardsPercVector != null && allDiscardsPercVector.size() > 0)
				((Hashtable) ShareData.getSharedata().get(host.getIpAddress())).put("alldiscardsperc", allDiscardsPercVector);
			if (packsVector != null && packsVector.size() > 0)
				((Hashtable) ShareData.getSharedata().get(host.getIpAddress())).put("packs", packsVector);
			if (inPacksVector != null && inPacksVector.size() > 0)
				((Hashtable) ShareData.getSharedata().get(host.getIpAddress())).put("inpacks", inPacksVector);
			if (outPacksVector != null && outPacksVector.size() > 0)
				((Hashtable) ShareData.getSharedata().get(host.getIpAddress())).put("outpacks", outPacksVector);
		}

		returnHash.put("interface", interfaceVector);
		returnHash.put("allutilhdxperc", allUtilhdxPercVector);
		returnHash.put("allutilhdx", allUtilhdxVector);
		returnHash.put("utilhdxperc", utilhdxPercVector);
		returnHash.put("utilhdx", utilhdxVector);
		returnHash.put("discardsperc", discardsPercVector);
		returnHash.put("errorsperc", errorsPercVector);
		returnHash.put("allerrorsperc", allErrorsPercVector);
		returnHash.put("alldiscardsperc", allDiscardsPercVector);
		returnHash.put("packs", packsVector);
		returnHash.put("inpacks", inPacksVector);
		returnHash.put("outpacks", outPacksVector);

		try {
			CheckEventUtil checkutil = new CheckEventUtil();
			AlarmIndicatorsNode alarmIndicatorsNode_port = new AlarmIndicatorsNode();
			AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
			List list = alarmIndicatorsUtil.getAlarmInicatorsThresholdForNode(String.valueOf(host.getId()), nodeGatherIndicators.getType(), nodeGatherIndicators.getSubtype());
			if (list == null) {
				list = new ArrayList();
			}
			for (int i = 0; i < list.size(); i++) {
				AlarmIndicatorsNode alarmIndicatorsNode = (AlarmIndicatorsNode) list.get(i);
				if ("0".equals(alarmIndicatorsNode.getEnabled())) {
					continue;
				}
				if (alarmIndicatorsNode.getName().equalsIgnoreCase("AllInBandwidthUtilHdx") || alarmIndicatorsNode.getName().equalsIgnoreCase("AllOutBandwidthUtilHdx") || alarmIndicatorsNode.getName().equalsIgnoreCase("AllBandwidthUtilHdx")) {
					alarmIndicatorsNode_port.setAlarm_level(alarmIndicatorsNode.getAlarm_level());
					alarmIndicatorsNode_port.setAlarm_times(alarmIndicatorsNode.getAlarm_times());
					alarmIndicatorsNode_port.setCategory(alarmIndicatorsNode.getCategory());
					alarmIndicatorsNode_port.setCompare_type(alarmIndicatorsNode.getCompare_type());
					alarmIndicatorsNode_port.setDatatype(alarmIndicatorsNode.getDatatype());
					alarmIndicatorsNode_port.setInterval_unit(alarmIndicatorsNode.getInterval_unit());
					alarmIndicatorsNode_port.setThreshlod_unit(alarmIndicatorsNode.getThreshlod_unit());
					alarmIndicatorsNode_port.setMoid(alarmIndicatorsNode.getMoid());
					alarmIndicatorsNode_port.setNodeid(alarmIndicatorsNode.getNodeid());
					alarmIndicatorsNode_port.setPoll_interval(alarmIndicatorsNode.getPoll_interval());
					alarmIndicatorsNode_port.setSubentity(alarmIndicatorsNode.getSubentity());
					alarmIndicatorsNode_port.setSubtype(alarmIndicatorsNode.getSubtype());
					alarmIndicatorsNode_port.setType(alarmIndicatorsNode.getType());
					alarmIndicatorsNode_port.setUnit(alarmIndicatorsNode.getUnit());
					// ���ܳ��������ֵ���и澯���
					try {
						if (returnHash != null) {
							Vector allutilVector = (Vector) returnHash.get("allutilhdx");
							if (allutilVector != null && allutilVector.size() > 0) {
								for (int j = 0; j < allutilVector.size(); j++) {
									AllUtilHdx allutilhdx = (AllUtilHdx) allutilVector.get(j);
									if (allutilhdx.getEntity().equalsIgnoreCase(alarmIndicatorsNode.getName())) {
										checkutil.checkEvent(host, alarmIndicatorsNode, allutilhdx.getThevalue());
									}
								}
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else if ("interface".equalsIgnoreCase(alarmIndicatorsNode.getName())) {
					try {
						if (returnHash != null) {
							if (interfaceVector != null && interfaceVector.size() > 0) {

								for (int j = 0; j < interfaceVector.size(); j++) {
									Interfacecollectdata interfacedata = (Interfacecollectdata) interfaceVector.get(j);
									if ("ifOperStatus".equalsIgnoreCase(interfacedata.getEntity())) {
										if (portconfigHash.containsKey(interfacedata.getSubentity())) {
											portconfig = (Portconfig) portconfigHash.get(interfacedata.getSubentity());
											if (portconfig != null) {
												alarmIndicatorsNode.setEnabled(portconfig.getSms() + "");
												alarmIndicatorsNode.setLimenvalue0("up");
												alarmIndicatorsNode.setSms0("1");
												alarmIndicatorsNode.setLimenvalue1("up");
												alarmIndicatorsNode.setSms0("0");
												alarmIndicatorsNode.setLimenvalue2("up");
												alarmIndicatorsNode.setSms0("0");
												alarmIndicatorsNode.setCompare(2);
												refreshLinkState(host, portconfig.getPortindex()+"", interfacedata.getThevalue());
												checkutil.checkEvent(host, alarmIndicatorsNode, interfacedata.getThevalue(), portconfig.getName().trim());
												
											}
										}
									}
								}
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else if ("InBandwidthUtilHdx".equalsIgnoreCase(alarmIndicatorsNode.getName()) || "OutBandwidthUtilHdx".equalsIgnoreCase(alarmIndicatorsNode.getName())) {
					// �����豸�˿ڵ� ��� �� ���� ����
					try {
						if (returnHash != null) {
							if (utilhdxVector != null && utilhdxVector.size() > 0) {
								for (int j = 0; j < utilhdxVector.size(); j++) {
									UtilHdx utilhdx = (UtilHdx) utilhdxVector.get(j);
									if (alarmportconfigHash.containsKey(utilhdx.getSubentity())) {
										// �����ڶ˿���� �� ���� ���ٸ澯����
										alarmPortConfig = (AlarmPort) alarmportconfigHash.get(utilhdx.getSubentity());
										try {
											if (alarmPortConfig != null) {
												if ("InBandwidthUtilHdx".equalsIgnoreCase(alarmIndicatorsNode.getName())) {
													alarmIndicatorsNode.setEnabled(alarmPortConfig.getEnabled());
													alarmIndicatorsNode.setLimenvalue0(alarmPortConfig.getLevelinvalue1() + "");
													alarmIndicatorsNode.setLimenvalue1(alarmPortConfig.getLevelinvalue2() + "");
													alarmIndicatorsNode.setLimenvalue2(alarmPortConfig.getLevelinvalue3() + "");
													alarmIndicatorsNode.setSms0(alarmPortConfig.getSmsin1() + "");
													alarmIndicatorsNode.setSms1(alarmPortConfig.getSmsin2() + "");
													alarmIndicatorsNode.setSms2(alarmPortConfig.getSmsin3() + "");
													alarmIndicatorsNode.setTime0(alarmPortConfig.getLevelintimes1() + "");
													alarmIndicatorsNode.setTime1(alarmPortConfig.getLevelintimes2() + "");
													alarmIndicatorsNode.setTime2(alarmPortConfig.getLevelintimes3() + "");
													alarmIndicatorsNode.setWay0(alarmPortConfig.getWayin1() + "");
													alarmIndicatorsNode.setWay1(alarmPortConfig.getWayin2() + "");
													alarmIndicatorsNode.setWay2(alarmPortConfig.getWayin3() + "");
													alarmIndicatorsNode.setCompare(alarmPortConfig.getCompare());
												}
												if ("OutBandwidthUtilHdx".equalsIgnoreCase(alarmIndicatorsNode.getName())) {
													alarmIndicatorsNode.setEnabled(alarmPortConfig.getEnabled());
													alarmIndicatorsNode.setLimenvalue0(alarmPortConfig.getLeveloutvalue1() + "");
													alarmIndicatorsNode.setLimenvalue1(alarmPortConfig.getLeveloutvalue2() + "");
													alarmIndicatorsNode.setLimenvalue2(alarmPortConfig.getLeveloutvalue3() + "");
													alarmIndicatorsNode.setSms0(alarmPortConfig.getSmsout1() + "");
													alarmIndicatorsNode.setSms1(alarmPortConfig.getSmsout2() + "");
													alarmIndicatorsNode.setSms2(alarmPortConfig.getSmsout3() + "");
													alarmIndicatorsNode.setTime0(alarmPortConfig.getLevelouttimes1() + "");
													alarmIndicatorsNode.setTime1(alarmPortConfig.getLevelouttimes2() + "");
													alarmIndicatorsNode.setTime2(alarmPortConfig.getLevelouttimes3() + "");
													alarmIndicatorsNode.setWay0(alarmPortConfig.getWayout1() + "");
													alarmIndicatorsNode.setWay1(alarmPortConfig.getWayout2() + "");
													alarmIndicatorsNode.setWay2(alarmPortConfig.getWayout3() + "");
													alarmIndicatorsNode.setCompare(alarmPortConfig.getCompare());
												}
												checkutil.checkEvent(host, alarmIndicatorsNode, utilhdx.getThevalue(), alarmPortConfig.getName().trim());
											}
										} catch (Exception e) {
										}
									}
								}
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
				/**
				 * @author hipo
				 * ���ã��˿����ٸ澯
				 */
				try {
					if (returnHash != null) {
						if (utilhdxVector != null && utilhdxVector.size() > 0) {
							
							for (int j = 0; j < utilhdxVector.size(); j++) {
								UtilHdx utilhdx = (UtilHdx) utilhdxVector.get(j);
								if (alarmportconfigHash.containsKey(utilhdx.getSubentity())) {
									// �����ڶ˿���� �� ���� ���ٸ澯����
									alarmPortConfig = (AlarmPort) alarmportconfigHash.get(utilhdx.getSubentity());
									
									try {
										if (alarmPortConfig != null) {
											if (alarmPortConfig.getEnabled() == null || !alarmPortConfig.getEnabled().trim().equals("1")) {
												continue;
											}
											if ("InBandwidthUtilHdx".equalsIgnoreCase(utilhdx.getEntity())) {
												
												alarmIndicatorsNode_port.setEnabled(alarmPortConfig.getEnabled());
												alarmIndicatorsNode_port.setName("InBandwidthUtilHdx");
												alarmIndicatorsNode_port.setDescr("�˿��������");
												alarmIndicatorsNode_port.setAlarm_info("�˿�������ٳ�����ֵ");
												alarmIndicatorsNode_port.setLimenvalue0(alarmPortConfig.getLevelinvalue1() + "");
												alarmIndicatorsNode_port.setLimenvalue1(alarmPortConfig.getLevelinvalue2() + "");
												alarmIndicatorsNode_port.setLimenvalue2(alarmPortConfig.getLevelinvalue3() + "");
												alarmIndicatorsNode_port.setSms0(alarmPortConfig.getSmsin1() + "");
												alarmIndicatorsNode_port.setSms1(alarmPortConfig.getSmsin2() + "");
												alarmIndicatorsNode_port.setSms2(alarmPortConfig.getSmsin3() + "");
												alarmIndicatorsNode_port.setTime0(alarmPortConfig.getLevelintimes1() + "");
												alarmIndicatorsNode_port.setTime1(alarmPortConfig.getLevelintimes2() + "");
												alarmIndicatorsNode_port.setTime2(alarmPortConfig.getLevelintimes3() + "");
												alarmIndicatorsNode_port.setWay0(alarmPortConfig.getWayin1() + "");
												alarmIndicatorsNode_port.setWay1(alarmPortConfig.getWayin2() + "");
												alarmIndicatorsNode_port.setWay2(alarmPortConfig.getWayin3() + "");
												alarmIndicatorsNode_port.setCompare(alarmPortConfig.getCompare());
											}
											if ("OutBandwidthUtilHdx".equalsIgnoreCase(utilhdx.getEntity())) {
												alarmIndicatorsNode_port.setEnabled(alarmPortConfig.getEnabled());
												alarmIndicatorsNode_port.setName("OutBandwidthUtilHdx");
												alarmIndicatorsNode_port.setDescr("�˿ڳ�������");
												alarmIndicatorsNode_port.setAlarm_info("�˿ڳ������ٳ�����ֵ");
												alarmIndicatorsNode_port.setLimenvalue0(alarmPortConfig.getLeveloutvalue1() + "");
												alarmIndicatorsNode_port.setLimenvalue1(alarmPortConfig.getLeveloutvalue2() + "");
												alarmIndicatorsNode_port.setLimenvalue2(alarmPortConfig.getLeveloutvalue3() + "");
												alarmIndicatorsNode_port.setSms0(alarmPortConfig.getSmsout1() + "");
												alarmIndicatorsNode_port.setSms1(alarmPortConfig.getSmsout2() + "");
												alarmIndicatorsNode_port.setSms2(alarmPortConfig.getSmsout3() + "");
												alarmIndicatorsNode_port.setTime0(alarmPortConfig.getLevelouttimes1() + "");
												alarmIndicatorsNode_port.setTime1(alarmPortConfig.getLevelouttimes2() + "");
												alarmIndicatorsNode_port.setTime2(alarmPortConfig.getLevelouttimes3() + "");
												alarmIndicatorsNode_port.setWay0(alarmPortConfig.getWayout1() + "");
												alarmIndicatorsNode_port.setWay1(alarmPortConfig.getWayout2() + "");
												alarmIndicatorsNode_port.setWay2(alarmPortConfig.getWayout3() + "");
												alarmIndicatorsNode_port.setCompare(alarmPortConfig.getCompare());
											}
											
											checkutil.checkEvent(host, alarmIndicatorsNode_port, utilhdx.getThevalue(), alarmPortConfig.getName().trim());
										}
									} catch (Exception e) {
									}
								}
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		outPacksVector = null;
		inPacksVector = null;
		packsVector = null;
		allDiscardsPercVector = null;
		allErrorsPercVector = null;
		errorsPercVector = null;
		discardsPercVector = null;
		utilhdxVector = null;
		utilhdxPercVector = null;
		allUtilhdxVector = null;
		allUtilhdxPercVector = null;
		interfaceVector = null;

		NetinterfaceResultTosql tosql = new NetinterfaceResultTosql();
		tosql.CreateResultTosql(returnHash, host.getIpAddress());
		String runmodel = PollingEngine.getCollectwebflag();// �ɼ������ģʽ
		if (!"0".equals(runmodel)) {
			NetInterfaceDataTemptosql datatemp = new NetInterfaceDataTemptosql();
			datatemp.CreateResultTosql(returnHash, host);
		}

		return returnHash;
	}
	private void refreshLinkState(Host host,String index,String value){
		List linkList = PollingEngine.getInstance().getLinkList();
//		System.out.println("linkList.size()==============="+linkList.size());
		if(linkList != null && linkList.size()>0){
//			System.out.println("host==============="+host.getId()+"---------"+index);
			for(int j = 0; j < linkList.size(); j++){
				LinkRoad lr = (LinkRoad) linkList.get(j);
				Host host1 = (Host) PollingEngine.getInstance().getNodeByID(lr.getStartId());
				Host host2 = (Host) PollingEngine.getInstance().getNodeByID(lr.getEndId());
				if(host.getId()==host1.getId()&&index.equalsIgnoreCase(lr.getStartIndex())){
					if("down".equalsIgnoreCase(value)){
						lr.setAlarm(true);
						lr.setLevels(2);
						lr.setStarOper("down");
//						System.out.println("lr.isAlarm==============="+lr.isAlarm());
					} else {
						lr.setAlarm(false);
						lr.setLevels(0);
						lr.setStarOper("up");
//						System.out.println("lr.isAlarm==============="+lr.isAlarm());
					}
				}
				if(host.getId()==host2.getId()&&index.equalsIgnoreCase(lr.getEndIndex())){
					if("down".equalsIgnoreCase(value)){
						lr.setAlarm(true);
						lr.setLevels(2);
						lr.setEndOper("down");
//						System.out.println("lr.isAlarm==============="+lr.isAlarm());
					} else {
						lr.setAlarm(false);
						lr.setLevels(0);
						lr.setEndOper("up");
//						System.out.println("lr.isAlarm==============="+lr.isAlarm());
					}
				}
			}
		}
	}
	public int getInterval(float d, String t) {
		int interval = 0;
		if (t.equals("d"))
			interval = (int) d * 24 * 60 * 60; // ����
		else if (t.equals("h"))
			interval = (int) d * 60 * 60; // Сʱ
		else if (t.equals("m"))
			interval = (int) d * 60; // ����
		else if (t.equals("s"))
			interval = (int) d; // ��
		return interval;
	}
}
