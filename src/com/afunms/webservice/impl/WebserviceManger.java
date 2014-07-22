package com.afunms.webservice.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.cxf.frontend.ClientProxyFactoryBean;

import com.afunms.application.dao.DBDao;
import com.afunms.application.model.DBVo;
import com.afunms.common.base.BaseManager;
import com.afunms.common.base.ManagerInterface;
import com.afunms.indicators.model.NodeDTO;
import com.afunms.indicators.util.NodeUtil;
import com.afunms.topology.dao.HostNodeDao;
import com.afunms.topology.model.HostNode;
import com.afunms.webservice.dao.CiTypeDao;
import com.afunms.webservice.dao.RelationDao;
import com.afunms.webservice.model.Attribute;
import com.afunms.webservice.model.CiType;
import com.afunms.webservice.model.MoAndCiRelation;

public class WebserviceManger extends BaseManager implements ManagerInterface {

	public String execute(String action) {

		if (action.equals("synchronousData")) {
			return synchronousData();
		}
		if (action.equals("list")) {
			return list();
		}
		return null;
	}

	public String synchronousData() {
//		HostNodeDao dao = new HostNodeDao();
//		List<HostNode> list = null;
//		try {
//			list = dao.loadall();
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			dao.close();
//		}
//		String typeCode = "";
//		RelationDao relationDao = new RelationDao();
//		relationDao.delteAllData();// 清除数据
//		MoAndCiRelation relation=null;
//		if (list != null && list.size() > 0) {
//			
//			for (int i = 0; i < list.size(); i++) {
//				HostNode node = list.get(i);
//				if (node != null) {
//					// typeCode = new StringBuffer();
//					// NodeUtil nodeUtil = new NodeUtil();
//					// NodeDTO nodedto = nodeUtil.creatNodeDTOByNode(node);
//					if (node.getCategory() == 1 || node.getCategory() == 2 || node.getCategory() == 3) {
//						typeCode = "0103";
//						// if (node.getCategory() == 1) {
//						// typeCode.append("010301");// 路由器
//						// } else if (node.getCategory() == 2) {
//						// typeCode.append("010302");// 路由交换机
//						// } else if (node.getCategory() == 3) {
//						// typeCode.append("010303");// 交换机
//					} else if (node.getCategory() == 4) {
//						typeCode = "0101";// 服务器
//						if(node.getOstype()==5){
//							relation = new MoAndCiRelation();
//							relation.setCiType("020501");
//							relation.setMoId(node.getId());
//
//							try {
//								relationDao.addBatch(relation);
//							} catch (Exception e) {
//								e.printStackTrace();
//							}
//						}
//					}
// 
//					// if (nodedto != null) {
//					// if (nodedto.getSubtype().equals("windows")) {
//					// typeCode.append("01");
//					// } else if (nodedto.getSubtype().equals("aix")) {
//					// typeCode.append("02");
//					// } else if (nodedto.getSubtype().equals("hpunix")) {
//					// typeCode.append("03");
//					// } else if (nodedto.getSubtype().equals("solaris")) {
//					// typeCode.append("04");
//					// } else if (nodedto.getSubtype().equals("linux")) {
//					// typeCode.append("05");
//					// } else if (nodedto.getSubtype().equals("as400")) {
//					// typeCode.append("06");
//					// } else if (nodedto.getSubtype().equals("scounix")) {
//					// typeCode.append("07");
//					// } else if (nodedto.getSubtype().equals("scoopenserver"))
//					// {
//					// typeCode.append("08");
//					// }
//
//					relation = new MoAndCiRelation();
//					relation.setCiType(typeCode);
//					relation.setMoId(node.getId());
//
//					try {
//						relationDao.addBatch(relation);
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//					// }
//
//				}
//			}
//			
//		}
//		List<DBVo> dbList=null;
//		DBDao dbdao=new DBDao();
//		try {
//			dbList=dbdao.getDbByMonFlag(1);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}finally{
//			dbdao.close();
//		}
//		
//		if (dbList!=null&&dbList.size()>0) {
//			for (int i = 0; i < dbList.size(); i++) {
//				DBVo vo=dbList.get(i);
//				if (vo!=null) {
//					if (vo.getDbtype()==1) {//oracle
//						typeCode="020201";
//					}else if (vo.getDbtype()==2) {
//						typeCode="020202";
//					}else if (vo.getDbtype()==4) {
//						typeCode="020203";
//					}else if (vo.getDbtype()==5) {
//						typeCode="020204";
//					}else if (vo.getDbtype()==6) {
//						typeCode="020205";
//					}else if (vo.getDbtype()==7) {
//						typeCode="020206";
//					}
//					relation = new MoAndCiRelation();
//					relation.setCiType(typeCode);
//					relation.setMoId(vo.getId());
//					try {
//						relationDao.addBatch(relation);
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				}
//			}
//		}
//		relationDao.executeBatch();
		// return null;
//		DeviceInfoImplHelper helper=new DeviceInfoImplHelper();
//		helper.synchronousData();
		DeviceInfoImpl impl = new DeviceInfoImpl();
		Attribute attribute = new Attribute();
		attribute.setMoId("2");
		attribute.setAttributeName("statupTime");
		Attribute attribute0 = new Attribute();
		attribute0.setMoId("2");
		attribute0.setAttributeName("softwareName");
		Attribute attribute1 = new Attribute();
		attribute1.setMoId("2");
		attribute1.setAttributeName("diskName");
		Attribute attribute2 = new Attribute();
		attribute2.setMoId("6");
		attribute2.setAttributeName("dbName");
		
		Attribute[] attributes = { attribute0,attribute,attribute1,attribute2};
		impl.getMoAttributeValue(attributes);
		String[] moidStrings=impl.getMoIds("020203");
		 for (int i = 0; i < moidStrings.length; i++) {
		 System.out.println(moidStrings[i] + "---------");
		 }
//		CiTypeDao dao2=new CiTypeDao();
//		CiType[] types=null;
//		try {
//			types=dao2.getAllCiType();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}finally{
//			dao2.close();
//		}
//		for (int i = 0; i < types.length; i++) {
//			CiType type=types[i];
//			System.out.println(type.getAttributeName());
//		}
		return "/webservice/webservice.jsp";
	}

	public String sendChangedMoIds() {
		ClientProxyFactoryBean factory = new ClientProxyFactoryBean();
		// factory.getServiceFactory().setDataBinding(new JAXBDataBinding());
		// factory.setServiceClass(Helloworld.class);
		// factory.setAddress("http://10.10.152.60:8083/Test/helloworld");
		// Helloworld service = (Helloworld) factory.create();
		RelationDao dao = new RelationDao();
		List list = dao.loadAll();
		String[] moIds = null;
		if (list != null && list.size() > 0) {
			moIds = new String[list.size()];
			for (int i = 0; i < list.size(); i++) {
				MoAndCiRelation model = (MoAndCiRelation) list.get(i);
				moIds[i] = model.getMoId() + "";
			}
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = sdf.format(new Date());

		return "/webservice/webservice.jsp";
	}

	public String list() {
		return "/webservice/webservice.jsp";
	}
}
