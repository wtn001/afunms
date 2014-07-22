/**
 * <p>Description:Tomcat Manager</p>
 * <p>Company: dhcc.com</p>
 * @author afunms
 * @project afunms
 * @date 2006-12-07
 */

package com.afunms.application.manage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import com.afunms.application.dao.DHCPConfigDao;
import com.afunms.application.dao.EmailConfigDao;
import com.afunms.application.dao.EmailHistoryDao;
import com.afunms.application.dao.FTPConfigDao;
import com.afunms.application.dao.FtpHistoryDao;
import com.afunms.application.dao.PSTypeDao;
import com.afunms.application.dao.TFTPConfigDao;
import com.afunms.application.dao.TFtpmonitor_historyDao;
import com.afunms.application.dao.WebConfigDao;
import com.afunms.application.dao.WebLoginConfigDao;
import com.afunms.application.model.DHCPConfig;
import com.afunms.application.model.EmailMonitorConfig;
import com.afunms.application.model.FTPConfig;
import com.afunms.application.model.MonitorServiceDTO;
import com.afunms.application.model.PSTypeVo;
import com.afunms.application.model.TFTPConfig;
import com.afunms.application.model.WebConfig;
import com.afunms.application.model.webloginConfig;
import com.afunms.common.base.BaseManager;
import com.afunms.common.base.ManagerInterface;
import com.afunms.common.util.SessionConstant;
import com.afunms.common.util.SystemConstant;
import com.afunms.event.dao.EventListDao;
import com.afunms.inform.util.SystemSnap;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.base.Node;
import com.afunms.polling.loader.DHCPLoader;
import com.afunms.polling.loader.FtpLoader;
import com.afunms.polling.loader.MailLoader;
import com.afunms.polling.loader.SocketServiceLoader;
import com.afunms.polling.loader.TFtpLoader;
import com.afunms.polling.loader.WebLoader;
import com.afunms.polling.loader.WebLoginLoader;
import com.afunms.system.model.User;

public class ServiceManager extends BaseManager implements ManagerInterface {

	public String execute(String action) {
		// TODO Auto-generated method stub
		if ("list".equals(action)) {
			return list();
		} else if ("changeManage".equals(action)) {
			return changeManage();
		}

		return null;
	}

	public String list() {

		String category = getParaValue("category");

		String flag = getParaValue("flag");

		request.setAttribute("flag", flag);

		List monitorServiceDTOList = new ArrayList();
		MonitorServiceDTO monitorServiceDTO = null;

		if ("mail".equals(category)) {
			try {
				// mail
				List maiList = getMailList();
				for (int i = 0; i < maiList.size(); i++) {
					EmailMonitorConfig emailMonitorConfig = (EmailMonitorConfig) maiList.get(i);

					monitorServiceDTO = getMonitorServiceDTOByMail(emailMonitorConfig);

					monitorServiceDTOList.add(monitorServiceDTO);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if ("ftp".equals(category)) {
			try {
				// FTP
				List ftpList = getFtpList();
				for (int i = 0; i < ftpList.size(); i++) {
					FTPConfig ftpConfig = (FTPConfig) ftpList.get(i);

					monitorServiceDTO = getMonitorServiceDTOByFtp(ftpConfig);

					monitorServiceDTOList.add(monitorServiceDTO);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if ("tftp".equals(category)) {
			try {
				// TFTP
				List tftpList = getTftpList();
				for (int i = 0; i < tftpList.size(); i++) {
					TFTPConfig tftpConfig = (TFTPConfig) tftpList.get(i);

					monitorServiceDTO = getMonitorServiceDTOByTftp(tftpConfig);

					monitorServiceDTOList.add(monitorServiceDTO);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if ("dhcp".equals(category)) {
			try {
				// DHCP
				List dhcpList = getDHCPList();
				for (int i = 0; i < dhcpList.size(); i++) {
					DHCPConfig dhcpConfig = (DHCPConfig) dhcpList.get(i);

					monitorServiceDTO = getMonitorServiceDTOByDHCP(dhcpConfig);

					monitorServiceDTOList.add(monitorServiceDTO);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if ("web".equals(category)) {
			try {
				// WEB
				List webList = getWebList();
				for (int i = 0; i < webList.size(); i++) {
					WebConfig webConfig = (WebConfig) webList.get(i);

					monitorServiceDTO = getMonitorServiceDTOByWeb(webConfig);

					monitorServiceDTOList.add(monitorServiceDTO);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if ("weblogin".equals(category)) {
			try {
				// WEBLogin
				List webList = getWebLoginList();
				for (int i = 0; i < webList.size(); i++) {
					webloginConfig webConfig = (webloginConfig) webList.get(i);

					monitorServiceDTO = getMonitorServiceDTOByWebLogin(webConfig);

					monitorServiceDTOList.add(monitorServiceDTO);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		else if ("portservice".equals(category)) {
			try {
				// portService
				List psTypeVoList = getPSTypeVoList();
				for (int i = 0; i < psTypeVoList.size(); i++) {
					PSTypeVo pSTypeVo = (PSTypeVo) psTypeVoList.get(i);

					monitorServiceDTO = getMonitorServiceDTOByPSTypeVo(pSTypeVo);

					monitorServiceDTOList.add(monitorServiceDTO);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			monitorServiceDTOList = getList();

			category = "services";
		}

		request.setAttribute("category", category);

		request.setAttribute("list", monitorServiceDTOList);
		return "/application/service/list.jsp";
	}

	public List getList() {

		List monitorServiceDTOList = new ArrayList();
		MonitorServiceDTO monitorServiceDTO = null;

		try {
			// mail
			List maiList = getMailList();
			for (int i = 0; i < maiList.size(); i++) {
				EmailMonitorConfig emailMonitorConfig = (EmailMonitorConfig) maiList.get(i);

				monitorServiceDTO = getMonitorServiceDTOByMail(emailMonitorConfig);

				monitorServiceDTOList.add(monitorServiceDTO);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			// FTP
			List ftpList = getFtpList();
			for (int i = 0; i < ftpList.size(); i++) {
				FTPConfig ftpConfig = (FTPConfig) ftpList.get(i);

				monitorServiceDTO = getMonitorServiceDTOByFtp(ftpConfig);

				monitorServiceDTOList.add(monitorServiceDTO);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			// TFTP
			List ftpList = getTftpList();
			for (int i = 0; i < ftpList.size(); i++) {
				TFTPConfig tftpConfig = (TFTPConfig) ftpList.get(i);

				monitorServiceDTO = getMonitorServiceDTOByTftp(tftpConfig);

				monitorServiceDTOList.add(monitorServiceDTO);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			// DHCP
			List dhcpList = getDHCPList();
			for (int i = 0; i < dhcpList.size(); i++) {
				DHCPConfig dhcpConfig = (DHCPConfig) dhcpList.get(i);

				monitorServiceDTO = getMonitorServiceDTOByDHCP(dhcpConfig);

				monitorServiceDTOList.add(monitorServiceDTO);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			// WEB
			List webList = getWebList();
			for (int i = 0; i < webList.size(); i++) {
				WebConfig webConfig = (WebConfig) webList.get(i);

				monitorServiceDTO = getMonitorServiceDTOByWeb(webConfig);

				monitorServiceDTOList.add(monitorServiceDTO);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			// WEBLOGIN
			List webList = getWebLoginList();
			for (int i = 0; i < webList.size(); i++) {
				webloginConfig webConfig = (webloginConfig) webList.get(i);

				monitorServiceDTO = getMonitorServiceDTOByWebLogin(webConfig);

				monitorServiceDTOList.add(monitorServiceDTO);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			// portService
			List psTypeVoList = getPSTypeVoList();
			for (int i = 0; i < psTypeVoList.size(); i++) {
				PSTypeVo pSTypeVo = (PSTypeVo) psTypeVoList.get(i);

				monitorServiceDTO = getMonitorServiceDTOByPSTypeVo(pSTypeVo);

				monitorServiceDTOList.add(monitorServiceDTO);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return monitorServiceDTOList;
	}

	/**
	 * ��ȡ Mail �б�
	 * 
	 * @return
	 */
	public List getMailList() {
		// mail

		List maillist = null;

		EmailConfigDao emailConfigDao = new EmailConfigDao();
		try {
			maillist = emailConfigDao.findByCondition(getMailSql());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			emailConfigDao.close();
		}

		if (maillist == null) {
			maillist = new ArrayList();
		}

		return maillist;
	}

	/**
	 * ��ȡ Mail Sql ���
	 * 
	 * @return
	 */
	public String getMailSql() {

		String sql = " where 1=1";

		sql = sql + getMonFlagSql("monflag");

		sql = sql + getBidSql("bid");

		return sql;
	}

	/**
	 * ���� mailConfig ����װ �м��
	 * 
	 * @param mailConfig
	 * @return
	 */
	public MonitorServiceDTO getMonitorServiceDTOByMail(EmailMonitorConfig emailMonitorConfig) {

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String date = simpleDateFormat.format(new Date());

		String starttime = date + " 00:00:00";
		String totime = date + " 23:59:59";

		int id = emailMonitorConfig.getId();
		String alias = emailMonitorConfig.getName();
		String ipAddress = emailMonitorConfig.getIpaddress();
		String category = "mail";

		Node mailNode = PollingEngine.getInstance().getMailByID(id);

		int status = 0;
		try {
			// status = mailNode.getStatus();
			status = SystemSnap.getNodeStatus(mailNode);
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Hashtable eventListSummary = new Hashtable();

		String generalAlarm = "0"; // ��ͨ�澯�� Ĭ��Ϊ 0
		String urgentAlarm = "0"; // ���ظ澯�� Ĭ��Ϊ 0
		String seriousAlarm = "0"; // �����澯�� Ĭ��Ϊ 0

		EmailHistoryDao emailmonitor_historyDao = new EmailHistoryDao();
		try {
			String str = "";
			if ("mysql".equalsIgnoreCase(SystemConstant.DBType)) {
				str = "  where email_id='" + id + "'" + " and is_canconnected='0' and mon_time>='" + starttime + "' and mon_time<='" + totime + "'";
			} else if ("oracle".equalsIgnoreCase(SystemConstant.DBType)) {
				str = "  where email_id='" + id + "'" + " and is_canconnected='0' and mon_time>=" + "to_date('" + starttime + "','YYYY-MM-DD HH24:MI:SS')" + " and mon_time<=" + "to_date('" + totime + "','YYYY-MM-DD HH24:MI:SS')" + "";
			}
			seriousAlarm = emailmonitor_historyDao.getCountByWhere(str);
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			emailmonitor_historyDao.close();
		}
		eventListSummary.put("generalAlarm", generalAlarm);
		eventListSummary.put("urgentAlarm", urgentAlarm);
		eventListSummary.put("seriousAlarm", seriousAlarm);

		String monflag = "��";

		if (emailMonitorConfig.getMonflag() == 1) {
			monflag = "��";
		}

		MonitorServiceDTO monitorServiceDTO = new MonitorServiceDTO();
		monitorServiceDTO.setId(id);
		monitorServiceDTO.setStatus(String.valueOf(status));
		monitorServiceDTO.setAlias(alias);
		monitorServiceDTO.setIpAddress(ipAddress);
		monitorServiceDTO.setPingValue("");
		monitorServiceDTO.setCategory(category);
		monitorServiceDTO.setEventListSummary(eventListSummary);
		monitorServiceDTO.setMonflag(monflag);
		return monitorServiceDTO;
	}

	/**
	 * ��ȡ FTP �б�
	 * 
	 * @return
	 */
	public List getFtpList() {
		// FTP

		List ftplist = null;

		FTPConfigDao ftpConfigDao = new FTPConfigDao();
		try {
			ftplist = ftpConfigDao.findByCondition(getFtpSql());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ftpConfigDao.close();
		}

		if (ftplist == null) {
			ftplist = new ArrayList();
		}

		return ftplist;
	}

	/**
	 * ��ȡ TFTP �б�
	 * 
	 * @return
	 */
	public List getTftpList() {
		// TFTP

		List tftplist = null;

		TFTPConfigDao tftpConfigDao = new TFTPConfigDao();
		try {
			tftplist = tftpConfigDao.findByCondition(getTftpSql());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			tftpConfigDao.close();
		}

		if (tftplist == null) {
			tftplist = new ArrayList();
		}

		return tftplist;
	}

	/**
	 * ��ȡ DHCP �б�
	 * 
	 * @return
	 */
	public List getDHCPList() {
		// TFTP

		List dhcplist = null;

		DHCPConfigDao dhcpConfigDao = new DHCPConfigDao();
		try {
			dhcplist = dhcpConfigDao.findByCondition(getDHCPSql());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dhcpConfigDao.close();
		}

		if (dhcplist == null) {
			dhcplist = new ArrayList();
		}

		return dhcplist;
	}

	/**
	 * ��ȡ Ftp Sql ���
	 * 
	 * @return
	 */
	public String getFtpSql() {

		String sql = " where 1=1";

		sql = sql + getMonFlagSql("monflag");

		sql = sql + getBidSql("bid");

		return sql;
	}

	/**
	 * ��ȡ Ftp Sql ���
	 * 
	 * @return
	 */
	public String getTftpSql() {

		String sql = " where 1=1";

		sql = sql + getMonFlagSql("monflag");

		sql = sql + getBidSql("bid");

		return sql;
	}

	/**
	 * ��ȡ DHCP Sql ���
	 * 
	 * @return
	 */
	public String getDHCPSql() {

		String sql = " where 1=1";

		sql = sql + getMonFlagSql("monflag");

		sql = sql + getBidSql("netid");

		return sql;
	}

	/**
	 * ���� ftpConfig ����װ �м��
	 * 
	 * @param ftpConfig
	 * @return
	 */
	public MonitorServiceDTO getMonitorServiceDTOByFtp(FTPConfig ftpConfig) {

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String date = simpleDateFormat.format(new Date());

		String starttime = date + " 00:00:00";
		String totime = date + " 23:59:59";

		int id = ftpConfig.getId();
		String alias = ftpConfig.getName();
		String ipAddress = ftpConfig.getIpaddress();
		String category = "ftp";

		Node ftpNode = PollingEngine.getInstance().getFtpByID(id);

		int status = 0;

		try {
			// status = ftpNode.getStatus();
			status = SystemSnap.getNodeStatus(ftpNode);
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Hashtable eventListSummary = new Hashtable();

		String generalAlarm = "0"; // ��ͨ�澯�� Ĭ��Ϊ 0
		String urgentAlarm = "0"; // ���ظ澯�� Ĭ��Ϊ 0
		String seriousAlarm = "0"; // �����澯�� Ĭ��Ϊ 0

		FtpHistoryDao ftpmonitor_historyDao = new FtpHistoryDao();
		try {
			String str = "";
			if ("mysql".equalsIgnoreCase(SystemConstant.DBType)) {
				str = " where ftp_id='" + id + "'" + " and is_canconnected='0' and mon_time>='" + starttime + "' and mon_time<='" + totime + "'";
			} else if ("oracle".equalsIgnoreCase(SystemConstant.DBType)) {
				str = " where ftp_id='" + id + "'" + " and is_canconnected='0' and mon_time>=" + "to_date('" + starttime + "','YYYY-MM-DD HH24:MI:SS')" + " and mon_time<=" + "to_date('" + totime + "','YYYY-MM-DD HH24:MI:SS')" + "";
			}
			seriousAlarm = ftpmonitor_historyDao.getCountByWhere(str);
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			ftpmonitor_historyDao.close();
		}
		eventListSummary.put("generalAlarm", generalAlarm);
		eventListSummary.put("urgentAlarm", urgentAlarm);
		eventListSummary.put("seriousAlarm", seriousAlarm);

		String monflag = "��";
		if (ftpConfig.getMonflag() == 1) {
			monflag = "��";
		}

		MonitorServiceDTO monitorServiceDTO = new MonitorServiceDTO();
		monitorServiceDTO.setId(id);
		monitorServiceDTO.setStatus(String.valueOf(status));
		monitorServiceDTO.setAlias(alias);
		monitorServiceDTO.setIpAddress(ipAddress);
		monitorServiceDTO.setPingValue("");
		monitorServiceDTO.setCategory(category);
		monitorServiceDTO.setEventListSummary(eventListSummary);
		monitorServiceDTO.setMonflag(monflag);
		return monitorServiceDTO;
	}

	/**
	 * ���� tftpConfig ����װ �м��
	 * 
	 * @param tftpConfig
	 * @return
	 */
	public MonitorServiceDTO getMonitorServiceDTOByTftp(TFTPConfig tftpConfig) {

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String date = simpleDateFormat.format(new Date());

		String starttime = date + " 00:00:00";
		String totime = date + " 23:59:59";

		int id = tftpConfig.getId();
		String alias = tftpConfig.getName();
		String ipAddress = tftpConfig.getIpaddress();
		String category = "tftp";

		Node tftpNode = PollingEngine.getInstance().getTftpByID(id);

		int status = 0;
		try {
			status = tftpNode.getStatus();
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Hashtable eventListSummary = new Hashtable();

		String generalAlarm = "0"; // ��ͨ�澯�� Ĭ��Ϊ 0
		String urgentAlarm = "0"; // ���ظ澯�� Ĭ��Ϊ 0
		String seriousAlarm = "0"; // �����澯�� Ĭ��Ϊ 0

		TFtpmonitor_historyDao tftpmonitor_historyDao = new TFtpmonitor_historyDao();
		try {
			String str = "";
			if ("mysql".equalsIgnoreCase(SystemConstant.DBType)) {
				str = " where tftp_id='" + id + "'" + " and is_canconnected='0' and mon_time>='" + starttime + "' and mon_time<='" + totime + "'";
			} else if ("oracle".equalsIgnoreCase(SystemConstant.DBType)) {
				str = " where tftp_id='" + id + "'" + " and is_canconnected='0' and mon_time>=" + "to_date('" + starttime + "','YYYY-MM-DD HH24:MI:SS')" + " and mon_time<=" + "to_date('" + totime + "','YYYY-MM-DD HH24:MI:SS')" + "";
			}
			seriousAlarm = tftpmonitor_historyDao.getCountByWhere(str);
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			tftpmonitor_historyDao.close();
		}
		eventListSummary.put("generalAlarm", generalAlarm);
		eventListSummary.put("urgentAlarm", urgentAlarm);
		eventListSummary.put("seriousAlarm", seriousAlarm);

		String monflag = "��";
		if (tftpConfig.getMonflag() == 1) {
			monflag = "��";
		}

		MonitorServiceDTO monitorServiceDTO = new MonitorServiceDTO();
		monitorServiceDTO.setId(id);
		monitorServiceDTO.setStatus(String.valueOf(status));
		monitorServiceDTO.setAlias(alias);
		monitorServiceDTO.setIpAddress(ipAddress);
		monitorServiceDTO.setPingValue("");
		monitorServiceDTO.setCategory(category);
		monitorServiceDTO.setEventListSummary(eventListSummary);
		monitorServiceDTO.setMonflag(monflag);
		return monitorServiceDTO;
	}

	/**
	 * ���� DHCPConfig ����װ �м��
	 * 
	 * @param dhcpConfig
	 * @return
	 */
	public MonitorServiceDTO getMonitorServiceDTOByDHCP(DHCPConfig dhcpConfig) {

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String date = simpleDateFormat.format(new Date());

		String starttime = date + " 00:00:00";
		String totime = date + " 23:59:59";

		int id = dhcpConfig.getId();
		String alias = dhcpConfig.getAlias();
		String ipAddress = dhcpConfig.getIpAddress();
		String category = "dhcp";

		Node dhcpNode = PollingEngine.getInstance().getDHCPByID(id);

		int status = 0;
		try {
			status = dhcpNode.getStatus();
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Hashtable eventListSummary = new Hashtable();

		String generalAlarm = "0"; // ��ͨ�澯�� Ĭ��Ϊ 0
		String urgentAlarm = "0"; // ���ظ澯�� Ĭ��Ϊ 0
		String seriousAlarm = "0"; // �����澯�� Ĭ��Ϊ 0

		EventListDao eventListDao = new EventListDao();
		try {
			String g = "";
			String u = "";
			String s = "";
			if ("mysql".equalsIgnoreCase(SystemConstant.DBType)) {
				g = " where nodeid='" + id + "'" + " and level1='1' and subtype='dhcp' and recordtime>='" + starttime + "' and recordtime<='" + totime + "'";
				u = " where nodeid='" + id + "'" + " and level1='2' and subtype='dhcp' and recordtime>='" + starttime + "' and recordtime<='" + totime + "'";
				s = " where nodeid='" + id + "'" + " and level1='3' and subtype='dhcp' and recordtime>='" + starttime + "' and recordtime<='" + totime + "'";
			} else if ("oracle".equalsIgnoreCase(SystemConstant.DBType)) {
				g = " where nodeid='" + id + "'" + " and level1='1' and subtype='dhcp' and recordtime>=" + "to_date('" + starttime + "','YYYY-MM-DD HH24:MI:SS')" + " and recordtime<=" + "to_date('" + totime + "','YYYY-MM-DD HH24:MI:SS')" + "";
				u = " where nodeid='" + id + "'" + " and level1='2' and subtype='dhcp' and recordtime>=" + "to_date('" + starttime + "','YYYY-MM-DD HH24:MI:SS')" + " and recordtime<=" + "to_date('" + totime + "','YYYY-MM-DD HH24:MI:SS')" + "";
				s = " where nodeid='" + id + "'" + " and level1='3' and subtype='dhcp' and recordtime>=" + "to_date('" + starttime + "','YYYY-MM-DD HH24:MI:SS')" + " and recordtime<=" + "to_date('" + totime + "','YYYY-MM-DD HH24:MI:SS')" + "";
			}
			generalAlarm = eventListDao.getCountByWhere(g);
			urgentAlarm = eventListDao.getCountByWhere(u);
			seriousAlarm = eventListDao.getCountByWhere(s);
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			eventListDao.close();
		}
		eventListSummary.put("generalAlarm", generalAlarm);
		eventListSummary.put("urgentAlarm", urgentAlarm);
		eventListSummary.put("seriousAlarm", seriousAlarm);

		String monflag = "��";
		if (dhcpConfig.getMon_flag() == 1) {
			monflag = "��";
		}

		MonitorServiceDTO monitorServiceDTO = new MonitorServiceDTO();
		monitorServiceDTO.setId(id);
		monitorServiceDTO.setStatus(String.valueOf(status));
		monitorServiceDTO.setAlias(alias);
		monitorServiceDTO.setIpAddress(ipAddress);
		monitorServiceDTO.setPingValue("");
		monitorServiceDTO.setCategory(category);
		monitorServiceDTO.setEventListSummary(eventListSummary);
		monitorServiceDTO.setMonflag(monflag);
		return monitorServiceDTO;
	}

	/**
	 * ��ȡ WEB �б�
	 * 
	 * @return
	 */
	public List getWebList() {
		// WEB

		List weblist = null;

		WebConfigDao webConfigDao = new WebConfigDao();
		try {
			weblist = webConfigDao.findByCondition(getWebSql());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			webConfigDao.close();
		}

		if (weblist == null) {
			weblist = new ArrayList();
		}

		return weblist;
	}

	/**
	 * ��ȡ Web Sql ���
	 * 
	 * @return
	 */
	public String getWebSql() {

		String sql = " where 1=1";

		sql = sql + getMonFlagSql("flag");

		sql = sql + getBidSql("netid");

		return sql;
	}

	/**
	 * ��ȡ WEBLogin �б�
	 * 
	 * @return
	 */
	public List getWebLoginList() {
		// WEBLogin

		List weblist = null;

		WebLoginConfigDao webConfigDao = new WebLoginConfigDao();
		try {
			weblist = webConfigDao.findByCondition(getWebLoginSql());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			webConfigDao.close();
		}

		if (weblist == null) {
			weblist = new ArrayList();
		}

		return weblist;
	}

	/**
	 * ��ȡ Web Sql ���
	 * 
	 * @return
	 */
	public String getWebLoginSql() {

		String sql = " where 1=1";

		sql = sql + getMonFlagSql("flag");

		sql = sql + getBidSql("bid");

		return sql;
	}

	/**
	 * ���� webConfig ����װ �м��
	 * 
	 * @param webConfig
	 * @return
	 */
	public MonitorServiceDTO getMonitorServiceDTOByWeb(WebConfig webConfig) {

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String date = simpleDateFormat.format(new Date());

		String starttime = date + " 00:00:00";
		String totime = date + " 23:59:59";

		int id = webConfig.getId();
		String alias = webConfig.getAlias();
		String ipAddress = webConfig.getIpAddress();
		String category = "web";

		Node webNode = PollingEngine.getInstance().getWebByID(id);

		int status = 0;
		try {
			status = SystemSnap.getNodeStatus(webNode);
		} catch (RuntimeException e) {
			e.printStackTrace();
		}

		Hashtable eventListSummary = new Hashtable();

		String generalAlarm = "0"; // ��ͨ�澯�� Ĭ��Ϊ 0
		String urgentAlarm = "0"; // ���ظ澯�� Ĭ��Ϊ 0
		String seriousAlarm = "0"; // �����澯�� Ĭ��Ϊ 0

		EventListDao eventListDao = new EventListDao();
		try {
			String g = "";
			String u = "";
			String s = "";
			if ("mysql".equalsIgnoreCase(SystemConstant.DBType)) {
				g = " where nodeid='" + id + "'" + " and level1='1' and recordtime>='" + starttime + "' and recordtime<='" + totime + "'";
				u = " where nodeid='" + id + "'" + " and level1='2' and recordtime>='" + starttime + "' and recordtime<='" + totime + "'";
				s = " where nodeid='" + id + "'" + " and level1='3' and recordtime>='" + starttime + "' and recordtime<='" + totime + "'";
			} else if ("oracle".equalsIgnoreCase(SystemConstant.DBType)) {
				g = " where nodeid='" + id + "'" + " and level1='1' and recordtime>=" + "to_date('" + starttime + "','YYYY-MM-DD HH24:MI:SS')" + " and recordtime<=" + "to_date('" + totime + "','YYYY-MM-DD HH24:MI:SS')" + "";
				u = " where nodeid='" + id + "'" + " and level1='2' and recordtime>=" + "to_date('" + starttime + "','YYYY-MM-DD HH24:MI:SS')" + " and recordtime<=" + "to_date('" + totime + "','YYYY-MM-DD HH24:MI:SS')" + "";
				s = " where nodeid='" + id + "'" + " and level1='3' and recordtime>=" + "to_date('" + starttime + "','YYYY-MM-DD HH24:MI:SS')" + " and recordtime<=" + "to_date('" + totime + "','YYYY-MM-DD HH24:MI:SS')" + "";
			}
			generalAlarm = eventListDao.getCountByWhere(g);
			urgentAlarm = eventListDao.getCountByWhere(u);
			seriousAlarm = eventListDao.getCountByWhere(s);
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			eventListDao.close();
		}
		eventListSummary.put("generalAlarm", generalAlarm);
		eventListSummary.put("urgentAlarm", urgentAlarm);
		eventListSummary.put("seriousAlarm", seriousAlarm);

		String monflag = "��";
		if (webConfig.getFlag() == 1) {
			monflag = "��";
		}

		MonitorServiceDTO monitorServiceDTO = new MonitorServiceDTO();
		monitorServiceDTO.setId(id);
		monitorServiceDTO.setStatus(String.valueOf(status));
		monitorServiceDTO.setAlias(alias);
		monitorServiceDTO.setIpAddress(ipAddress);
		monitorServiceDTO.setPingValue("");
		monitorServiceDTO.setCategory(category);
		monitorServiceDTO.setEventListSummary(eventListSummary);
		monitorServiceDTO.setMonflag(monflag);
		return monitorServiceDTO;
	}

	/**
	 * ���� webConfig ����װ �м��
	 * 
	 * @param webConfig
	 * @return
	 */
	public MonitorServiceDTO getMonitorServiceDTOByWebLogin(webloginConfig webConfig) {

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String date = simpleDateFormat.format(new Date());

		String starttime = date + " 00:00:00";
		String totime = date + " 23:59:59";

		int id = webConfig.getId();
		String alias = webConfig.getAlias();
		String ipAddress = "";
		String category = "weblogin";

		Node webNode = PollingEngine.getInstance().getWebLoginByID(id);

		int status = 0;
		try {
			// status = webNode.getStatus();
			status = SystemSnap.getNodeStatus(webNode);
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Hashtable eventListSummary = new Hashtable();

		String generalAlarm = "0"; // ��ͨ�澯�� Ĭ��Ϊ 0
		String urgentAlarm = "0"; // ���ظ澯�� Ĭ��Ϊ 0
		String seriousAlarm = "0"; // �����澯�� Ĭ��Ϊ 0

		EventListDao eventListDao = new EventListDao();
		try {
			String g = "";
			String u = "";
			String s = "";
			if ("mysql".equalsIgnoreCase(SystemConstant.DBType)) {
				g = " where nodeid='" + id + "'" + " and level1='1' and recordtime>='" + starttime + "' and recordtime<='" + totime + "'";
				u = " where nodeid='" + id + "'" + " and level1='2' and recordtime>='" + starttime + "' and recordtime<='" + totime + "'";
				s = " where nodeid='" + id + "'" + " and level1='3' and recordtime>='" + starttime + "' and recordtime<='" + totime + "'";
			} else if ("oracle".equalsIgnoreCase(SystemConstant.DBType)) {
				g = " where nodeid='" + id + "'" + " and level1='1' and recordtime>=" + "to_date('" + starttime + "','YYYY-MM-DD HH24:MI:SS')" + " and recordtime<=" + "to_date('" + totime + "','YYYY-MM-DD HH24:MI:SS')" + "";
				u = " where nodeid='" + id + "'" + " and level1='2' and recordtime>=" + "to_date('" + starttime + "','YYYY-MM-DD HH24:MI:SS')" + " and recordtime<=" + "to_date('" + totime + "','YYYY-MM-DD HH24:MI:SS')" + "";
				s = " where nodeid='" + id + "'" + " and level1='3' and recordtime>=" + "to_date('" + starttime + "','YYYY-MM-DD HH24:MI:SS')" + " and recordtime<=" + "to_date('" + totime + "','YYYY-MM-DD HH24:MI:SS')" + "";
			}
			generalAlarm = eventListDao.getCountByWhere(g);
			urgentAlarm = eventListDao.getCountByWhere(u);
			seriousAlarm = eventListDao.getCountByWhere(s);
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			eventListDao.close();
		}
		eventListSummary.put("generalAlarm", generalAlarm);
		eventListSummary.put("urgentAlarm", urgentAlarm);
		eventListSummary.put("seriousAlarm", seriousAlarm);

		String monflag = "��";
		try {
			if (Integer.parseInt(webConfig.getFlag()) == 1) {
				monflag = "��";
			}
		} catch (Exception e) {

		}

		MonitorServiceDTO monitorServiceDTO = new MonitorServiceDTO();
		monitorServiceDTO.setId(id);
		monitorServiceDTO.setStatus(String.valueOf(status));
		monitorServiceDTO.setAlias(alias);
		monitorServiceDTO.setIpAddress(ipAddress);
		monitorServiceDTO.setPingValue("");
		monitorServiceDTO.setCategory(category);
		monitorServiceDTO.setEventListSummary(eventListSummary);
		monitorServiceDTO.setMonflag(monflag);
		return monitorServiceDTO;
	}

	/**
	 * ��ȡ PSTypeVo �б�
	 * 
	 * @return
	 */
	public List getPSTypeVoList() {
		// WEB

		List psTypeVoList = null;

		PSTypeDao psTypeDao = new PSTypeDao();
		try {
			psTypeVoList = psTypeDao.findByCondition(getPSTypeVoSql());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			psTypeDao.close();
		}

		if (psTypeVoList == null) {
			psTypeVoList = new ArrayList();
		}

		return psTypeVoList;
	}

	/**
	 * ��ȡ PSTypeVo Sql ���
	 * 
	 * @return
	 */
	public String getPSTypeVoSql() {

		String sql = " where 1=1";

		sql = sql + getMonFlagSql("monflag");

		sql = sql + getBidSql("bid");

		return sql;
	}

	/**
	 * ���� psTypeVo ����װ �м��
	 * 
	 * @param psTypeVo
	 * @return
	 */
	public MonitorServiceDTO getMonitorServiceDTOByPSTypeVo(PSTypeVo psTypeVo) {

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String date = simpleDateFormat.format(new Date());

		String starttime = date + " 00:00:00";
		String totime = date + " 23:59:59";

		int id = psTypeVo.getId();
		String alias = psTypeVo.getPortdesc() + "-" + psTypeVo.getPort();
		String ipAddress = psTypeVo.getIpaddress();
		String category = "portService";

		Node psTypeVoNode = PollingEngine.getInstance().getSocketByID(id);

		int status = 0;
		try {
			status = SystemSnap.getNodeStatus(psTypeVoNode);
		} catch (RuntimeException e) {
			e.printStackTrace();
		}

		Hashtable eventListSummary = new Hashtable();

		String generalAlarm = "0"; // ��ͨ�澯�� Ĭ��Ϊ 0
		String urgentAlarm = "0"; // ���ظ澯�� Ĭ��Ϊ 0
		String seriousAlarm = "0"; // �����澯�� Ĭ��Ϊ 0

		EventListDao eventListDao = new EventListDao();
		try {
			String g = "";
			String u = "";
			String s = "";
			if ("mysql".equalsIgnoreCase(SystemConstant.DBType)) {
				g = " where nodeid='" + id + "'" + " and level1='1' and recordtime>='" + starttime + "' and recordtime<='" + totime + "'";
				u = " where nodeid='" + id + "'" + " and level1='2' and recordtime>='" + starttime + "' and recordtime<='" + totime + "'";
				s = " where nodeid='" + id + "'" + " and level1='3' and recordtime>='" + starttime + "' and recordtime<='" + totime + "'";
			} else if ("oracle".equalsIgnoreCase(SystemConstant.DBType)) {
				g = " where nodeid='" + id + "'" + " and level1='1' and recordtime>=" + "to_date('" + starttime + "','YYYY-MM-DD HH24:MI:SS')" + " and recordtime<=" + "to_date('" + totime + "','YYYY-MM-DD HH24:MI:SS')" + "";
				u = " where nodeid='" + id + "'" + " and level1='2' and recordtime>=" + "to_date('" + starttime + "','YYYY-MM-DD HH24:MI:SS')" + " and recordtime<=" + "to_date('" + totime + "','YYYY-MM-DD HH24:MI:SS')" + "";
				s = " where nodeid='" + id + "'" + " and level1='3' and recordtime>=" + "to_date('" + starttime + "','YYYY-MM-DD HH24:MI:SS')" + " and recordtime<=" + "to_date('" + totime + "','YYYY-MM-DD HH24:MI:SS')" + "";
			}
			generalAlarm = eventListDao.getCountByWhere(g);
			urgentAlarm = eventListDao.getCountByWhere(u);
			seriousAlarm = eventListDao.getCountByWhere(s);
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			eventListDao.close();
		}
		eventListSummary.put("generalAlarm", generalAlarm);
		eventListSummary.put("urgentAlarm", urgentAlarm);
		eventListSummary.put("seriousAlarm", seriousAlarm);

		String monflag = "��";
		if (psTypeVo.getMonflag() == 1) {
			monflag = "��";
		}

		MonitorServiceDTO monitorServiceDTO = new MonitorServiceDTO();
		monitorServiceDTO.setId(id);
		monitorServiceDTO.setStatus(String.valueOf(status));
		monitorServiceDTO.setAlias(alias);
		monitorServiceDTO.setIpAddress(ipAddress);
		monitorServiceDTO.setPingValue("");
		monitorServiceDTO.setCategory(category);
		monitorServiceDTO.setEventListSummary(eventListSummary);
		monitorServiceDTO.setMonflag(monflag);
		return monitorServiceDTO;
	}

	public String getMonFlagSql(String fieldName) {
		String mon_flag = getParaValue("mon_flag");

		String sql = "";
		if (mon_flag != null && "1".equals(mon_flag)) {
			sql = " and " + fieldName + "='1'";
		}

		request.setAttribute("mon_flag", mon_flag);

		return sql;
	}

	/**
	 * ���ҵ��Ȩ�޵� SQL ���
	 * 
	 * @author nielin
	 * @date 2010-08-09
	 * @return
	 */
	public String getBidSql(String fieldName) {
		User current_user = (User) session.getAttribute(SessionConstant.CURRENT_USER);

		String sql = "";
		StringBuffer s = new StringBuffer();
		if (current_user.getRole() != 0) {
			int _flag = 0;
			if (current_user.getBusinessids() != null) {
				if (current_user.getBusinessids() != "-1") {
					String[] bids = current_user.getBusinessids().split(",");
					if (bids.length > 0) {
						for (int i = 0; i < bids.length; i++) {
							if (bids[i].trim().length() > 0) {
								if (_flag == 0) {
									s.append(" and ( " + fieldName + " like '%," + bids[i].trim() + ",%' ");
									_flag = 1;
								} else {
									// flag = 1;
									s.append(" or " + fieldName + " like '%," + bids[i].trim() + ",%' ");
								}
							}
						}
						s.append(") ");
					}

				}
			}
		}

		sql = s.toString();

		String treeBid = request.getParameter("treeBid");
		if (treeBid != null && treeBid.trim().length() > 0) {
			treeBid = treeBid.trim();
			treeBid = "," + treeBid + ",";
			String[] treeBids = treeBid.split(",");
			if (treeBids != null) {
				for (int i = 0; i < treeBids.length; i++) {
					if (treeBids[i].trim().length() > 0) {
						sql = sql + " and " + fieldName + " like '%," + treeBids[i].trim() + ",%'";
					}
				}
			}
		}
		// SysLogger.info("select * from topo_host_node where managed=1 "+sql);
		return sql;
	}

	public String changeManage() {

		String category = getParaValue("type");

		try {
			if ("ftp".equals(category)) {
				changeFtpManage();
			} else if ("mail".equals(category)) {
				changeMailManage();
			} else if ("tftp".equals(category)) {
				changeTftpManage();
			} else if ("web".equals(category)) {
				changeWebManage();
			} else if ("portService".equals(category)) {
				changePortServiceManage();
			} else if ("weblogin".equals(category)) {
				changeWebLoginManage();
			} else if ("dhcp".equals(category)) {
				changeDhcpManage();
			}
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return list();
	}

	/**
	 * �޸� FTPConfig �ļ�����Ϣ֮�󷵻�FTPConfig�б�ҳ��
	 * 
	 * @return
	 */
	private void changeFtpManage() {
		boolean result = false;
		FTPConfig ftpConfig = new FTPConfig();
		FTPConfigDao ftpConfigDao = null;
		try {
			String id = getParaValue("id");
			int monflag = getParaIntValue("value");
			ftpConfigDao = new FTPConfigDao();
			ftpConfig = (FTPConfig) ftpConfigDao.findByID(id);
			ftpConfig.setMonflag(monflag);
			result = ftpConfigDao.update(ftpConfig);
			// Ftp ftp = (Ftp)
			// PollingEngine.getInstance().getFtpByID(Integer.parseInt(id));
			// ftp.setMonflag(monflag);
			FtpLoader loader = new FtpLoader();
			loader.loading();
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		} finally {
			ftpConfigDao.close();
		}
		FtpLoader loader = new FtpLoader();
		loader.loading();
	}

	/**
	 * �޸� TFTPConfig �ļ�����Ϣ֮�󷵻�TFTPConfig�б�ҳ��
	 * 
	 * @return
	 */
	private void changeTftpManage() {
		boolean result = false;
		TFTPConfig tftpConfig = new TFTPConfig();
		TFTPConfigDao tftpConfigDao = null;
		try {
			String id = getParaValue("id");
			int monflag = getParaIntValue("value");
			tftpConfigDao = new TFTPConfigDao();
			tftpConfig = (TFTPConfig) tftpConfigDao.findByID(id);
			tftpConfig.setMonflag(monflag);
			result = tftpConfigDao.update(tftpConfig);
			// TFtp tftp = (TFtp)
			// PollingEngine.getInstance().getTftpByID(Integer.parseInt(id));
			// tftp.setMonflag(monflag);
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		} finally {
			tftpConfigDao.close();
		}
		TFtpLoader loader = new TFtpLoader();
		loader.loading();
	}

	private void changeMailManage() {
		boolean result = false;
		EmailMonitorConfig emailMonitorConfig = new EmailMonitorConfig();
		EmailConfigDao emailConfigDao = null;
		try {
			String id = getParaValue("id");
			int monflag = getParaIntValue("value");
			emailConfigDao = new EmailConfigDao();
			emailMonitorConfig = (EmailMonitorConfig) emailConfigDao.findByID(id);
			emailMonitorConfig.setMonflag(monflag);
			result = emailConfigDao.update(emailMonitorConfig);
			// Mail mail =
			// (Mail)PollingEngine.getInstance().getMailByID(Integer.parseInt(id));
			// mail.setFlag(monflag);

		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		} finally {
			emailConfigDao.close();
		}
		MailLoader loader = new MailLoader();
		loader.loading();
	}

	private void changeWebManage() {
		WebConfig vo = new WebConfig();
		WebConfigDao configdao = null;
		try {
			int monflag = getParaIntValue("value");
			configdao = new WebConfigDao();
			vo = (WebConfig) configdao.findByID(getParaValue("id"));
			vo.setFlag(monflag);
			configdao.update(vo);
			// Web web =
			// (Web)PollingEngine.getInstance().getWebByID(vo.getId());
			// web.setFlag(monflag);
			WebLoginLoader loader = new WebLoginLoader();
			loader.loading();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			configdao.close();
		}
		WebLoader loader = new WebLoader();
		loader.loading();
	}

	private void changeWebLoginManage() {
		webloginConfig vo = new webloginConfig();
		WebLoginConfigDao configdao = null;
		try {
			int monflag = getParaIntValue("value");
			configdao = new WebLoginConfigDao();
			vo = (webloginConfig) configdao.findByID(getParaValue("id"));
			vo.setFlag(monflag + "");
			configdao.update(vo);

			// WebLogin web = (WebLogin)
			// PollingEngine.getInstance().getWebLoginByID(vo.getId());
			// web.setFlag(monflag);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			configdao.close();
		}
		WebLoginLoader loader = new WebLoginLoader();
		loader.loading();
	}

	private void changeDhcpManage() {
		DHCPConfig vo = new DHCPConfig();
		DHCPConfigDao configdao = null;
		try {
			int monflag = getParaIntValue("value");
			configdao = new DHCPConfigDao();
			vo = (DHCPConfig) configdao.findByID(getParaValue("id"));
			vo.setMon_flag(monflag);
			configdao.update(vo);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			configdao.close();
		}
		// wxy upadate ���ص��ڴ�
		DHCPLoader loader = new DHCPLoader();
		loader.loading();
	}

	private void changePortServiceManage() {
		boolean result = false;
		PSTypeVo pstyVo = new PSTypeVo();
		PSTypeDao pstypedao = null;
		try {
			String id = getParaValue("id");
			int monflag = getParaIntValue("value");
			pstypedao = new PSTypeDao();
			pstyVo = (PSTypeVo) pstypedao.findByID(id);
			pstyVo.setMonflag(monflag);
			result = pstypedao.update(pstyVo);

		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		} finally {
			pstypedao.close();
		}
		SocketServiceLoader loader = new SocketServiceLoader();
		loader.loading();
	}

}