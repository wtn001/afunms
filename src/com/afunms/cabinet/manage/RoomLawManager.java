package com.afunms.cabinet.manage;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.afunms.application.util.ReportExport;
import com.afunms.cabinet.dao.EqpRoomDao;
import com.afunms.cabinet.dao.RoomLawDao;
import com.afunms.cabinet.model.EqpRoom;
import com.afunms.cabinet.model.RoomLaw;
import com.afunms.common.base.BaseManager;
import com.afunms.common.base.ManagerInterface;
import com.afunms.common.util.SessionConstant;
import com.afunms.config.model.Huaweitelnetconf;
import com.afunms.initialize.ResourceCenter;
import com.afunms.system.dao.UserDao;
import com.afunms.system.model.User;
import com.ibm.ctg.epi.Session;
import com.jspsmart.upload.SmartUpload;
/**
 *�����ƶȹ���
 * @author wxy
 * @version Dec 22, 2011 10:27:17 AM
 */
public class RoomLawManager extends BaseManager implements ManagerInterface {

	public String execute(String action) {
		if (action.equals("list")) {
			return list();
		}
		if (action.equals("ready_add")) {
			return ready_add();
		}
		if (action.equals("delete")) {
			return delete();
		}
		if (action.equals("save")) {
			return save();
		}
		if (action.equals("ready_edit")) {
			return ready_edit();
		}
		if (action.equals("edit")) {
			return edit();
		}
		if (action.equals("toDetail")) {
			return toDetail();
		}
		if (action.equals("downloadReport")) {
			return downloadReport();
		}
		return null;
	}

	public String list() {
		List<User> userList = new ArrayList<User>();
		Hashtable<Integer, User> userHash = new Hashtable<Integer, User>();
		List<EqpRoom> eqpRoomlist = new ArrayList<EqpRoom>();
		Hashtable<Integer, EqpRoom> eqpRoomHash = new Hashtable<Integer, EqpRoom>();
		UserDao userDao = null;
		EqpRoomDao eqpRoomDao = null;
		try {
			userDao = new UserDao();
			userList = userDao.loadAll();
			if (userList != null && userList.size() > 0) {
				for (int i = 0; i < userList.size(); i++) {
					User user = (User) userList.get(i);
					userHash.put(user.getId(), user);
				}
			}
			eqpRoomDao = new EqpRoomDao();
			eqpRoomlist = eqpRoomDao.loadAll();
			if (eqpRoomlist != null && eqpRoomlist.size() > 0) {
				for (int i = 0; i < eqpRoomlist.size(); i++) {
					EqpRoom eqpRoom = (EqpRoom) eqpRoomlist.get(i);
					eqpRoomHash.put(eqpRoom.getId(), eqpRoom);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			userDao.close();
			eqpRoomDao.close();
		}
		int cabinetid = getParaIntValue("cabinetid");// ����ʱ��
		String startdate = getParaValue("startdate");// ��ʼ����
		String todate = getParaValue("todate");// 
		StringBuffer where = new StringBuffer();

		where.append(" where 1=1");
		
		if(cabinetid!=-1){
			where.append(" and cabinetid=" + cabinetid);
		}
		
		if (startdate != null && todate != null && !"".equals(startdate)
				&& !"".equals(todate) && !"null".equals(startdate)
				&& !"null".equals(todate)) {
			where.append(" and dotime>'" + startdate
					+ " 00:00:00' and dotime<'" + todate + " 23:59:59'");
		} else {
			String currentDateString = "";//��ʼҳ�濪ʼ����Ϊ�� 
			String perWeekDateString = "";//��ʼҳ���������Ϊ�� 
			todate = currentDateString;
			startdate = perWeekDateString;
		}
		request.setAttribute("cabinetid", cabinetid );
		request.setAttribute("startdate", startdate);
		request.setAttribute("todate", todate);
		request.setAttribute("userHash", userHash);
		request.setAttribute("eqpRoomHash", eqpRoomHash);
		setTarget("/cabinet/roomlaw/list.jsp");
		RoomLawDao dao = new RoomLawDao();

		return list(dao, where + " order by dotime desc");
	}

	public String ready_add() {
		EqpRoomDao dao = new EqpRoomDao();
		List roomList = new ArrayList();
		try {
			roomList = dao.loadAll();
		} catch (Exception e) {

		} finally {
			dao.close();
		}
		request.setAttribute("roomList", roomList);
		return "/cabinet/roomlaw/add.jsp";
	}

	public String delete() {
		String[] ids = getParaArrayValue("checkbox");
		String realPath = ResourceCenter.getInstance().getSysPath();
		if (ids != null && ids.length > 0) {
			StringBuffer id = new StringBuffer();
			for (int i = 0; i < ids.length; i++) {
				if (i != ids.length - 1) {
					id.append(ids[i]).append(",");
				} else {
					id.append(ids[i]);
				}
			}
			RoomLawDao dao = new RoomLawDao();
			List<String> list = null;
			boolean flag = false;
			try {
				list = dao.findByIds("where id in(" + id + ")");
				flag = dao.delete(ids);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				dao.close();
			}
			if (flag && list != null) {
				for (int i = 0; i < list.size(); i++) {

					String path = realPath + "cabinet\\roomlaw\\pdf\\" + list.get(i);
					File file = null;
					try {
						file = new File(path);
						if (file.exists()) {
							file.delete();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

		}
		return list();
	}

	public String save() {
		boolean flag = true;
		String name = getParaValue("name");
		int cabinetid = getParaIntValue("cabinetid");
		String description = getParaValue("description");
		String filePath = getParaValue("filePath");

		User user = (User) session.getAttribute(SessionConstant.CURRENT_USER);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = sdf.format(new Date());
		// ///////////////////////
		// FileItem fileIntem = null;
		// String serverFilePath = null;
		// DiskFileItemFactory factory = new DiskFileItemFactory();
		// factory.setSizeThreshold(1024 * 10);
		// String fileName = null;
		// fileName = ResourceCenter.getInstance().getSysPath();
		// String prefix = fileName.replace("\\", "\\\\");
		// factory.setRepository(new File(prefix + "ftpupload"));// ���÷������˱���·��
		// ServletFileUpload upload = new ServletFileUpload(factory);
		// upload.setSizeMax(1000000);
		// String path = "";
		// try {
		// List fileItems = upload.parseRequest(this.request);
		// Iterator iter = fileItems.iterator(); // ���δ���ÿ���ؼ�
		// while (iter.hasNext()) {
		// FileItem item = (FileItem) iter.next();// �����������ļ�������б���Ϣ
		// if (item.getName() != null) {
		// fileIntem = item;
		// fileName = fileIntem.getName();
		// String[] items = fileName.split("\\\\");
		// if (items != null && items.length >= 1) {
		// fileName = items[items.length - 1];
		// }
		//
		// }
		// }
		// SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMddHHmmss");
		// String time2 = sdf2.format(new Date());
		// serverFilePath = prefix + "ftpupload\\\\roomlaw-" + time2+".pdf";
		// path = "ftpupload/roomlaw-" + time2+".pdf";
		// fileIntem.write(new File(serverFilePath));
		// } catch (Exception e) {
		// e.printStackTrace();
		// setErrorCode(0);// δ֪����
		// flag = false;
		// }
		// /////////////////////////////////////////////////////////////////////////
		if (flag) {
			RoomLaw roomLaw = new RoomLaw();
			roomLaw.setName(name);
			roomLaw.setFilename(filePath);
			roomLaw.setDescription(description);
			roomLaw.setDotime(time);
			roomLaw.setUserid(user.getId());
			roomLaw.setCabinetid(cabinetid);
			RoomLawDao dao = null;
			try {
				dao = new RoomLawDao();
				flag = dao.save(roomLaw);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				dao.close();
			}
		}
		return list();
	}

	public String ready_edit() {
		int id = getParaIntValue("id");
		RoomLawDao roomLawDao = null;
		RoomLaw roomLaw = null;
		EqpRoomDao dao = new EqpRoomDao();
		List roomList = new ArrayList();
		try {
			roomList = dao.loadAll();
			roomLawDao = new RoomLawDao();
			roomLaw = (RoomLaw) roomLawDao.findByID(id + "");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dao.close();
			roomLawDao.close();
		}

		request.setAttribute("roomList", roomList);
		request.setAttribute("roomLaw", roomLaw);
		return "/cabinet/roomlaw/edit.jsp";
	}

	public String edit() {
		int id = getParaIntValue("id");
		String name = getParaValue("name");
		int cabinetid = getParaIntValue("cabinetid");
		String description = getParaValue("description");
		String filename = getParaValue("filePath");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = sdf.format(new Date());
		User user = (User) session.getAttribute(SessionConstant.CURRENT_USER);
		RoomLawDao roomLawDao = null;
		RoomLaw roomLaw = new RoomLaw();
		roomLaw.setId(id);
		roomLaw.setName(name);
		roomLaw.setDotime(time);
		roomLaw.setUserid(user.getId());
		roomLaw.setDescription(description);
		roomLaw.setFilename(filename);
		roomLaw.setCabinetid(cabinetid);
		try {
			roomLawDao = new RoomLawDao();
			roomLawDao.update(roomLaw);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			roomLawDao.close();
		}

		return list();
	}

	public String toDetail() {
		
		String filePath = ResourceCenter.getInstance().getSysPath() + "\\cabinet\\roomlaw\\pdf\\";
		String fileName =getParaValue("filename");
		filePath = filePath + fileName;
		request.setAttribute("filename", filePath);
		return "/cabinet/roomlaw/download.jsp";
	}
	private String downloadReport() {
		String type=getParaValue("type");
		String exportType=getParaValue("exportType");
		int cabinetid=getParaIntValue("cabinetid");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String time=sdf.format(new Date());
		//String name = getParaValue("fileName");
		String filePath = ResourceCenter.getInstance().getSysPath() + "/cabinet/roomlaw/pdf/roomReport("+time+")." +exportType;
		String startTime=getParaValue("startdate");
		String toTime=getParaValue("todate");
		ReportExport export=new ReportExport();
		export.exportRoomReport( type, filePath,cabinetid, startTime, toTime,exportType);
		request.setAttribute("filename", filePath);
		return "/capreport/net/download.jsp";
	}
}
