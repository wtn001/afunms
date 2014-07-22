package com.afunms.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.afunms.polling.base.Node;
import com.afunms.polling.node.Host;
import com.afunms.polling.PollingEngine;
import com.afunms.topology.dao.DiscoverStatDao;
import com.afunms.topology.dao.HostNodeDao;
import com.afunms.topology.dao.LinkDao;
import com.afunms.topology.model.HostNode;
import com.afunms.topology.model.Link;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class ExcelImportUtil {
	public List<Link> parseExcel(String path) {

		InputStream is;
		List<Link> nodeList = null;
		try {
			File file = new File(path);
			if (file.exists()) {
				is = new FileInputStream(path);

				Workbook rwb = null;

				// ����һ��������
				try {
					rwb = Workbook.getWorkbook(is);
					// ��ù������ĸ���
					rwb.getNumberOfSheets();
				} catch (BiffException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

				// ��Excel�ĵ��У���һ�Ź������ȱʡ������0
				Sheet st = rwb.getSheet(0);
				// ͨ�õĻ�ȡcellֵ�ķ�ʽ,getCell(int column, int row) �к���
				int rows = st.getRows();
				int cols = st.getColumns();
				nodeList = new ArrayList<Link>();
				Link link = null;
				for (int i = 2; i < rows; i++) {
					Cell c1 = st.getCell(1, i);// ��ʼID
					Cell c2 = st.getCell(2, i);// ��ʼIP
					Cell c3 = st.getCell(3, i);// ��ʼ�˿�����
					Cell c4 = st.getCell(4, i);// ��ʼ�˿�����
					Cell c5 = st.getCell(5, i);// ��ʼ�˿�
					Cell c6 = st.getCell(6, i);// ��ʼMAC
					Cell c7 = st.getCell(7, i);// �յ��豸ID
					Cell c8 = st.getCell(8, i);// �յ��豸IP
					Cell c9 = st.getCell(9, i);// �յ�˿�����
					Cell c10 = st.getCell(10, i);// �յ��豸����
					Cell c11 = st.getCell(11, i);// �յ�˿�
					Cell c12 = st.getCell(12, i);// �յ�MAC
					Cell c13 = st.getCell(13, i);// ��·����
					Cell c14 = st.getCell(14, i);// �Ƿ�����·
					Cell c15 = st.getCell(15, i);// ����
					Cell c16 = st.getCell(16, i);// ��������
					Cell c17 = st.getCell(17, i);// ��·����
					Cell c18 = st.getCell(18, i);// ������ֵ
					Cell c19 = st.getCell(19, i);// ����������
					Cell c20 = st.getCell(20, i);// �˿��Ƿ���ʾ
					link = new Link();
					int startId=Integer.parseInt(c1.getContents());
					String startIp=c2.getContents();
                    String startDescr=c3.getContents();
                    String startIndex=c4.getContents();
                    String startPort=c5.getContents();
                    String startMac=c6.getContents();
                    int endId=Integer.parseInt(c7.getContents());
                    String endIp=c8.getContents();
                    link.setEndDescr(c9.getContents());
                    String endIndex=c10.getContents();
                    String endPort=c11.getContents();
                    String endMac=c12.getContents();
                    link.setStartMac(startMac);
                    link.setEndMac(endMac);
                    link.setStartDescr(startDescr);
					link.setStartIp(startIp);
					link.setStartIndex(startIndex);
					link.setStartPort(startPort);
					link.setEndIp(endIp);
					link.setEndIndex(endIndex);
					link.setEndPort(endPort);
					
//					
//					Host startNode=(Host)PollingEngine.getInstance().getNodeByID(startId);
//					Host endNode=(Host)PollingEngine.getInstance().getNodeByID(endId);
//					if(startNode!=null){
//						link.setStartMac(startNode.getMac());
//					}
//					if(endNode!=null){
//						link.setStartMac(endNode.getMac());
//					}
					link.setStartId(startId);
					
					link.setEndId(endId);
					//String linkName=startIp+"_"+startIndex+"/"+endIp+"_"+endIndex;
					
					link.setLinkName(c13.getContents());
					link.setAssistant(Integer.parseInt(c14.getContents()));
					link.setType(Integer.parseInt(c15.getContents()));
					link.setFindtype(Integer.parseInt(c16.getContents()));
					link.setLinktype(Integer.parseInt(c17.getContents()));
					link.setMaxSpeed(c18.getContents());
					link.setMaxPer(c19.getContents());
					
					int interf=Integer.parseInt(c20.getContents());
					link.setShowinterf(interf);
					nodeList.add(link);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return nodeList;

	}

	public String  loadAllData(String path) {
		String flag="1";
		List<Link> linkList = this.parseExcel(path);
		DBManager db = new DBManager();
		StringBuffer sql = null;
		if (linkList != null && linkList.size() > 0) {
			//LinkDao dao = new LinkDao();
			//int nextId = dao.getNextId();
			int id=1;
			sql = new StringBuffer();
			sql.append("delete from topo_network_link");
			db.addBatch(sql.toString());
			try {
				for (int i = 0; i < linkList.size(); i++) {
					Link vo = linkList.get(i);
					if (vo != null) {
						   sql = new StringBuffer(200);
						   sql.append("insert into topo_network_link(id,link_name,start_id,start_index,start_ip,start_descr,start_port,start_mac,");
						   sql.append("end_id,end_index,end_ip,end_descr,end_port,end_mac,assistant,type,findtype,linktype,max_speed,max_per)values(");
						   sql.append(id);
						   sql.append(",'");
						   sql.append(vo.getLinkName());
						   sql.append("',");
						   sql.append(vo.getStartId());
						   sql.append(",'");
						   sql.append(vo.getStartIndex());
						   sql.append("','");
						   sql.append(vo.getStartIp());
						   sql.append("','");
						   sql.append(vo.getStartDescr());
						   sql.append("','");
						   sql.append(vo.getStartPort());
						   sql.append("','");
						   sql.append(vo.getStartMac());
						   sql.append("',");
						   sql.append(vo.getEndId());
						   sql.append(",'");
						   sql.append(vo.getEndIndex());
						   sql.append("','");
						   sql.append(vo.getEndIp());
						   sql.append("','");
						   sql.append(vo.getEndDescr());
						   sql.append("','");
						   sql.append(vo.getEndPort());
						   sql.append("','");
						   sql.append(vo.getEndMac());
						   sql.append("',");	   
						   sql.append(vo.getAssistant());
						   sql.append(",");
						   sql.append(vo.getType());
						   sql.append(",");
						   sql.append(vo.getFindtype());
						   sql.append(",");
						   sql.append(vo.getLinktype());
						   sql.append(",'");
						   sql.append(vo.getMaxSpeed());
						   sql.append("','");
						   sql.append(vo.getMaxPer());
						   sql.append("')");
						   db.addBatch(sql.toString());
						 //  System.out.println("--"+sql.toString());
						id++;
					}
				}
				db.executeBatch();
			} catch (Exception e) {
				flag="0";
				e.printStackTrace();
			} finally {
				
				db.close();
			}
		}
		return flag;

	}
	public void loadData(String path) {

		List<Link> linkList = this.parseExcel(path);
		LinkDao dao = new LinkDao();
		if (linkList != null && linkList.size() > 0) {
			
			try {
				for (int i = 0; i < linkList.size(); i++) {
					Link link = linkList.get(i);
					if (link != null) {
						dao.saveLink(link);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				dao.close();
			}
		}

	}
}
