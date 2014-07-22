package com.afunms.vmware.vim25.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.URI;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.dom4j.xpath.DefaultXPath;

/**
 * ���ù�����
 * 
 * @author LXL
 * 
 */
public class Util {

	private static final Log LOGGER = LogFactory.getLog(Util.class);

	// ��_����
	public static final int NONE_INT = -1;

	// ��_����
	public static final int TRUE_INT = 1;

	// ��_����
	public static final int FALSE_INT = 0;

	// �㲥��ַ
	public static final String BROADCAST_ADDR = "255.255.255.255";

	// ���ص�ַǰ׺
	public static final String LOCALHOST_PREFIX = "127.";

	// �ļ����ָ���
	public static final String SEPARATOR = "-";

	// ���ŷָ���
	public static final String SEPARATOR_COMMA = ",";

	// �ֺŷָ���
	public static final String SEPARATOR_SEMICOLON = ";";

	// ��ŷָ���
	public static final String SEPARATOR_PERIOD = ".";

	// �»��߷ָ���
	public static final String SEPARATOR_UNDERLINE = "_";

	// �Ⱥŷָ���
	public static final String SEPARATOR_EQUAL = "=";

	// ������
	public static final String SINGLE_QUOTATION = "'";

	// ������
	public static final String LEFT_PARENTHESIS = "(";

	// ������
	public static final String RIGHT_PARENTHESIS = ")";

	// �ո�
	public static final String SPACE = " ";

	// �ļ���չ��
	public static final String FILENAME_EXTENSION = ".xml";

	// XML�ļ���չ��
	public static final String XMLFILENAME_EXTENSION = FILENAME_EXTENSION;

	// �����ļ���չ��
	public static final String BAKFILENAME_EXTENSION = ".bak";

	// Ĭ�ϳ���ʱ����ʽ
	public static final String DEFAULTDURATION_PATTERN = "dd''��''HH''Сʱ''mm''����''ss''��''";

	// Ĭ��������ʽ
	public static final String DEFAULTDATE_PATTERN = "yyyy-MM-dd";
	private static final SimpleDateFormat DEFAULTDATESDF = new SimpleDateFormat(
			DEFAULTDATE_PATTERN);

	// Ĭ�����ڣ����꣩��ʽ
	public static final String DEFAULTDATENOYEAR_PATTERN = "MM-dd";
	private static final SimpleDateFormat DEFAULTDATENOYEARSDF = new SimpleDateFormat(
			DEFAULTDATENOYEAR_PATTERN);

	// Ĭ��ʱ���ʽ
	public static final String DEFAULTTIME_PATTERN = "HH:mm:ss";
	private static final SimpleDateFormat DEFAULTTIMESDF = new SimpleDateFormat(
			DEFAULTTIME_PATTERN);

	// Ĭ��ʱ����ʽ
	public static final String DEFAULTDATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss.S";
	private static final SimpleDateFormat DEFAULTDATETIMESDF = new SimpleDateFormat(
			DEFAULTDATETIME_PATTERN);

	// ���ݿ�ʱ����ʽ
	public static final String DBDATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
	private static final SimpleDateFormat DBDATETIMESDF = new SimpleDateFormat(
			DBDATETIME_PATTERN);

	// ���ݿ�ʱ����ʽ���·ݿ�ʼ(����)
	public static final String DBDATETIMENOYEAR_PATTERN = "MM-dd HH:mm:ss";
	private static final SimpleDateFormat DBDATETIMENOYEARSDF = new SimpleDateFormat(
			DBDATETIMENOYEAR_PATTERN);

	// �ļ�ʱ����ʽ
	public static final String FILEDATETIME_PATTERN = "yyyyMMddHHmmss";
	private static final SimpleDateFormat FILEDATETIMESDF = new SimpleDateFormat(
			FILEDATETIME_PATTERN);

	// �޷ָ�����ʱ����ʽ
	// public static final String IDDATETIME_PATTERN = "yyyyMMddHHmmssSSS";
	// private static final SimpleDateFormat IDDATETIMESDF = new
	// SimpleDateFormat(
	// IDDATETIME_PATTERN);

	// �������ʱ�ķ�����
	public static final String REFLECT_METHODNAME = "process";

	// IPƥ�����ʽ
	private static final Pattern ipPattern = Pattern
			.compile("^([0-9]{1,3}\\.){3}[0-9]{1,3}$");

	// XPATH����
	private static ConcurrentHashMap<String, DefaultXPath> xPathMap = new ConcurrentHashMap<String, DefaultXPath>();

	// Ĭ�ϰٷֱ����ָ�ʽ
	private static final DecimalFormat DEFAULTPERCENTDF = new DecimalFormat(
			"##.##%");

	// 2λ�����ٷֺ�_�ٷֱ����ָ�ʽ
	private static final DecimalFormat BITS2_NOPERCENT_PERCENTDF = new DecimalFormat(
			"##.##");

	// Ĭ�ϰٷֱ����ָ�ʽ
	private static final DecimalFormat FOURDECIMAL_PERCENTDF = new DecimalFormat(
			"##.####%");

	// 2ΪС��������ת��
	private static final DecimalFormat BITS2_DECIMALTDF = new DecimalFormat(
			"#.##");

	// ���ص�ַ
	private static String localHost = "";

	// �뵽����ı���
	public static final int SECONDS_MILLISECONDS = 1000;

	// ���Ӷ�Ӧ�ĺ�����
	public static final long MINUTE_MILLISECONDS = 60 * SECONDS_MILLISECONDS;

	// Сʱ��Ӧ�ĺ�����
	public static final long HOUR_MILLISECONDS = 60 * MINUTE_MILLISECONDS;

	// 8Сʱ��Ӧ�ĺ�����
	private static final long HOUR_8_MILLISECONDS = 8 * HOUR_MILLISECONDS;

	// 16Сʱ��Ӧ�ĺ�����
	// private static final long HOUR_16_MILLISECONDS = 16 * HOUR_MILLISECONDS;

	// ���ӵ��뱶��
	public static final int MINUTE_SECONDS = 60;

	// Сʱ���뱶��
	public static final int HOUR_SECONDS = 60 * MINUTE_SECONDS;

	// Сʱ���뱶��
	public static final int DAY_SECONDS = 24 * HOUR_SECONDS;

	// ����0
	// private static final int ZERO = 0;

	// ����1
	// private static final int ONE = 1;

	// �׶�Ӧ���ֽ���
	public static final int M = 1024 * 1024;

	// Ĭ�ϵ�����
	public static final int DEFAULT_M = 2;

	// http://www.websina.com/bugzero/kb/java-encoding-charset.html
	// UTF-8����
	public static final String UTF8 = "UTF-8";

	// GB18030����
	public static final String GB18030 = "GB18030";

	// ISO-8859-1����
	public static final String ISO88591 = "ISO-8859-1";

	// Ĭ������
	public static final String DEFAULT_PWD = "123456";

	/**
	 * ��ʽ���ַ���,�������null,��nullת��Ϊ""
	 * 
	 * @param s
	 *            ��Ҫ��ʽ�����ַ���
	 * @return ��ʽ������ַ���
	 */
	public static String normalizeString(String s) {
		return ((s == null) ? "" : s.trim());
	}

	/**
	 * ��ʽ���ַ���,�������null,��nullת��Ϊ"",��trim()�ַ���
	 * 
	 * @param s
	 *            ��Ҫ��ʽ�����ַ���
	 * @return ��ʽ������ַ���
	 */
	public static String normalizeStringNoTrim(String s) {
		return ((s == null) ? "" : s);
	}

	/**
	 * ��ʽ��Document
	 * 
	 * @param doc
	 *            ��Ҫ��ʽ����Document
	 * 
	 * @return �ı�
	 */
	public static String normalizeDocument(Document doc) {
		return ((doc == null) ? "" : doc.asXML());
	}

	/**
	 * ��ʽ������,�������null,��nullת��Ϊ""
	 * 
	 * @param o
	 *            ��Ҫ��ʽ���Ķ���
	 * @return ��ʽ����Ķ���
	 */
	public static Object normalizeObject(Object o) {
		return ((o == null) ? "" : o);
	}

	/**
	 * ����Map�����Ƿ�Ϊnull����isEmpty
	 * 
	 * @param map
	 *            MAP
	 * 
	 * @return Map�Ƿ�Ϊ��
	 */
	@SuppressWarnings("unchecked")
	public static boolean isMapEmpty(Map map) {
		return (map != null) && (!map.isEmpty());
	}

	/**
	 * Stringת��ΪInt�����ת��ʧ���򷵻�Ĭ��ֵ,���isFixΪ��,����ת�����<=0,�򷵻�ֵΪdefaultValue
	 * 
	 * @param value
	 *            ��Ҫת�����ַ���
	 * @param defaultValue
	 *            ת��ʧ��ʱ��Ĭ��ֵ
	 * @return ��ֵ
	 */
	public static int string2Int(String value, int defaultValue, boolean isFix) {
		int result;
		try {
			result = Integer.parseInt(value);
			if ((isFix) && (result <= 0)) {
				result = defaultValue;
			}
		} catch (NumberFormatException nfe) {
			result = defaultValue;
		}
		return result;
	}

	/**
	 * Stringת��ΪInt�����ת��ʧ���򷵻�Ĭ��ֵ,���isFixΪ��,����ת�����<=0,�򷵻�ֵΪdefaultValue
	 * 
	 * @param value
	 *            ��Ҫת�����ַ���
	 * @param defaultValue
	 *            ת��ʧ��ʱ��Ĭ��ֵ
	 * @return ��ֵ
	 */
	public static int string2Int(String value, int defaultValue) {
		return string2Int(value, defaultValue, false);
	}

	/**
	 * Stringת��ΪLong�����ת��ʧ���򷵻�Ĭ��ֵ,���isFixΪ��,����ת�����<=0,�򷵻�ֵΪdefaultValue
	 * 
	 * @param value
	 *            ��Ҫת�����ַ���
	 * @param defaultValue
	 *            ת��ʧ��ʱ��Ĭ��ֵ
	 * @return ��ֵ
	 */
	public static long string2Long(String value, long defaultValue,
			boolean isFix) {
		long result;
		try {
			result = Long.parseLong(value);
			if ((isFix) && (result <= 0)) {
				result = defaultValue;
			}
		} catch (NumberFormatException nfe) {
			result = defaultValue;
		}
		return result;
	}

	/**
	 * Stringת��ΪLong�����ת��ʧ���򷵻�Ĭ��ֵ,���isFixΪ��,����ת�����<=0,�򷵻�ֵΪdefaultValue
	 * 
	 * @param value
	 *            ��Ҫת�����ַ���
	 * @param defaultValue
	 *            ת��ʧ��ʱ��Ĭ��ֵ
	 * @return ��ֵ
	 */
	public static long string2Long(String value, long defaultValue) {
		return string2Long(value, defaultValue, false);
	}

	/**
	 * Stringת��ΪFloat�����ת��ʧ���򷵻�Ĭ��ֵ
	 * 
	 * @param value
	 *            ��Ҫת�����ַ���
	 * @param defaultValue
	 *            ת��ʧ��ʱ��Ĭ��ֵ
	 * @return ��ֵ
	 */
	public static float string2Float(String value, float defaultValue) {
		float result;
		try {
			result = Float.parseFloat(value);
			if (result <= 0) {
				result = defaultValue;
			}
		} catch (Exception e) {
			result = defaultValue;
		}
		return result;
	}

	/**
	 * Documentת��Ϊ�ı�
	 * 
	 * @param doc
	 *            Document
	 * 
	 * @return �ı�
	 */
	public static String document2String(Document doc) {
		return document2String(doc, DEFAULT_M);
	}

	/**
	 * Documentת��Ϊ�ı�
	 * 
	 * @param doc
	 *            Document
	 * @param mNumber
	 *            ������mNumber��
	 * 
	 * @return �ı�
	 */
	public static String document2String(Document doc, int mNumber) {
		if (doc != null) {
			String docStr = doc.asXML();
			return object2String(docStr, mNumber);
		} else {
			return "";
		}
	}

	/**
	 * Objectת��Ϊ�ı�
	 * 
	 * @param obj
	 *            ����
	 * 
	 * @return �ı�
	 */
	public static String object2String(Object obj) {
		return object2String(obj, DEFAULT_M);
	}

	/**
	 * Objectת��Ϊ�ı�
	 * 
	 * @param obj
	 *            ����
	 * @param mNumber
	 *            ������mNumber��
	 * 
	 * @return �ı�
	 */
	public static String object2String(Object obj, int mNumber) {
		if (obj != null) {
			String objStr = obj.toString();
			if (objStr.length() > mNumber * M) {
				return objStr.substring(0, mNumber * M) + "...����������...";
			} else
				return objStr;
		} else {
			return "";
		}
	}

	/**
	 * �������ĳ���ʱ��,��2Сʱ3��15��,1��5Сʱ37���
	 * 
	 * @param time
	 *            ʱ��(����)
	 * @return ����ʱ��
	 */
//	public static String getDurationTime(long time) {
//		synchronized (DEFAULTDURATION_PATTERN) {
//			return DurationFormatUtils.formatDuration(time,
//					DEFAULTDURATION_PATTERN);
//		}
//	}

	/**
	 * ���ص�ǰʱ��
	 * 
	 * @return ��ǰʱ��
	 */
	public static Date getCurrentDate() {
		return new Date();
	}

	/**
	 * ��Ĭ��������ʽ���ص�ǰ����
	 * 
	 * @return Ĭ����ʽ�ĵ�ǰ����
	 */
	public static String getCurrentDate2() {
		synchronized (DEFAULTDATESDF) {
			return DEFAULTDATESDF.format(System.currentTimeMillis());
		}
	}

	/**
	 * ��Ĭ����ʽ���ص�ǰʱ��
	 * 
	 * @return Ĭ����ʽ�ĵ�ǰʱ��
	 */
	public static String getCurrentDateTime() {
		synchronized (DEFAULTDATETIMESDF) {
			return DEFAULTDATETIMESDF.format(System.currentTimeMillis());
		}
	}

	/**
	 * ��ָ����ʽ���ص�ǰʱ��
	 * 
	 * @param pattern
	 *            ��ʽ
	 * @return ָ����ʽ�ĵ�ǰʱ��
	 */
	public static String getCurrentDateTime(String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		synchronized (sdf) {
			return sdf.format(System.currentTimeMillis());
		}
	}

	/**
	 * ��Ĭ����ʽ����ָ��ʱ��
	 * 
	 * @param dateTime
	 *            ָ��ʱ��
	 * @return Ĭ����ʽ��ָ��ʱ��
	 */
	public static String getDateTime(long dateTime) {
		synchronized (DEFAULTDATETIMESDF) {
			return DEFAULTDATETIMESDF.format(dateTime);
		}
	}

	/**
	 * ��Ĭ����ʽ����ָ��ʱ��
	 * 
	 * @param date
	 *            ָ��ʱ��
	 * @return Ĭ����ʽ��ָ��ʱ��
	 */
	public static String getDateTime(Date date) {
		return getDateTime(date.getTime());
	}

	/**
	 * ��ָ����ʽ����ָ��ʱ��
	 * 
	 * @param dateTime
	 *            ָ��ʱ��
	 * @param pattern
	 *            ָ����ʽ
	 * @return ָ����ʽ��ָ��ʱ��
	 */
	protected static String getDateTime(long dateTime, String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		synchronized (sdf) {
			return sdf.format(dateTime);
		}
	}

	/**
	 * �����ݿ���ʽ(yyyy-MM-dd HH:mm:ss)���ص�ǰʱ��
	 * 
	 * @return ���ݿ���ʽ(yyyy-MM-dd HH:mm:ss)�ĵ�ǰʱ��
	 */
	public static String getCurrentDBDateTime() {
		return getDBDateTime(System.currentTimeMillis());
	}

	/**
	 * �����ݿ���ʽ(yyyy-MM-dd HH:mm:ss)����ָ��ʱ��
	 * 
	 * @param dateTime
	 *            ָ��ʱ��
	 * @return ���ݿ���ʽ(yyyy-MM-dd HH:mm:ss)��ָ��ʱ��
	 */
	public static String getDBDateTime(long dateTime) {
		synchronized (DBDATETIMESDF) {
			return DBDATETIMESDF.format(dateTime);
		}
	}

	/**
	 * �����ݿ���ʽ(yyyy-MM-dd HH:mm:ss)����ָ��ʱ�� �����ֵΪС�ڵ���0������մ�
	 * 
	 * @param dateTime
	 *            ָ��ʱ��
	 * @return ���ݿ���ʽ(yyyy-MM-dd HH:mm:ss)��ָ��ʱ��
	 */
	public static String getDBDataTimeMaybeEmpty(long dateTime) {
		return (dateTime <= 0) ? "" : getDBDateTime(dateTime);
	}

	/**
	 * �����ݿ���ʽ(MM-dd HH:mm:ss)����ָ��ʱ��
	 * 
	 * @param dateTime
	 *            ָ��ʱ��
	 * @return ���ݿ���ʽ(MM-dd HH:mm:ss)��ָ��ʱ��
	 */
	public static String getDBDateTimeNoYear(long dateTime) {
		synchronized (DBDATETIMENOYEARSDF) {
			return DBDATETIMENOYEARSDF.format(dateTime);
		}
	}

	/**
	 * �����ݿ���ʽ(MM-dd)����ָ��ʱ��
	 * 
	 * @param dateTime
	 *            ָ��ʱ��
	 * @return ���ݿ���ʽ(MM-dd)��ָ��ʱ��
	 */
	public static String getDateNoYear(long dateTime) {
		synchronized (DEFAULTDATENOYEARSDF) {
			return DEFAULTDATENOYEARSDF.format(dateTime);
		}
	}

	/**
	 * �����ݿ���ʽ(HH:mm:ss)����ָ��ʱ��
	 * 
	 * @param dateTime
	 *            ָ��ʱ��
	 * @return ���ݿ���ʽ(HH:mm:ss)��ָ��ʱ��
	 */
	public static String getTimeMaybeEmpty(long dateTime) {
		synchronized (DEFAULTTIMESDF) {
			return (dateTime <= 0) ? "" : DEFAULTTIMESDF.format(dateTime);
		}
	}

	/**
	 * �����ݿ���ʽ(MM-dd HH:mm:ss)����ָ��ʱ�� �����ֵΪС�ڵ���0������մ�
	 * 
	 * @param dateTime
	 *            ָ��ʱ��
	 * @return ���ݿ���ʽ(MM-dd HH:mm:ss)��ָ��ʱ��
	 */
	public static String getDBDataTimeNoYearMaybeEmpty(long dateTime) {
		return (dateTime <= 0) ? "" : getDBDateTimeNoYear(dateTime);
	}

	/**
	 * �����ݿ���ʽ(yyyy-MM-dd HH:mm:ss)����ָ��ʱ��
	 * 
	 * @param dateTime
	 *            ָ��ʱ��
	 * @return ���ݿ���ʽ(yyyy-MM-dd HH:mm:ss)��ָ��ʱ��
	 */
	public static long getDBDateTime(String dbDateTime) {
		long result = 0;
		try {
			if (!dbDateTime.equals("")) {
				synchronized (DBDATETIMESDF) {
					result = DBDATETIMESDF.parse(dbDateTime).getTime();
				}
			}
		} catch (Exception e) {
			LOGGER.error("�������ݿ���ʽʱ��='" + dbDateTime + "'ʱ���ִ���", e);
		}
		return result;
	}

	/**
	 * ���ļ�����ʽ(yyyymmddHHMMSS)���ص�ǰʱ��
	 * 
	 * @return �ļ�����ʽ(yyyymmddHHMMSS)��ָ��ʱ��
	 */
	public static String getCurrentFileDateTime() {
		return getFileDateTime(System.currentTimeMillis());
	}

	/**
	 * ���ļ�����ʽ(yyyymmddHHMMSS)����ָ��ʱ��
	 * 
	 * @param dateTime
	 *            ָ��ʱ��
	 * @return �ļ�����ʽ(yyyymmddHHMMSS)��ָ��ʱ��
	 */
	public static String getFileDateTime(long dateTime) {
		synchronized (FILEDATETIMESDF) {
			return FILEDATETIMESDF.format(dateTime);
		}
	}

	/**
	 * �����뵱ǰʱ����������intervalʱ�� ����:ʱ����Ϊ10��,��ǰʱ��Ϊ5��1��,�򷵻�5��0��; ��ǰʱ��Ϊ5��8��,�򷵻�5��10��
	 * 
	 * @param interval
	 *            ʱ����,��λΪ��
	 * @return
	 */
	public static Date getIntervalDate(long interval) {
		Date date = null;
		long current = System.currentTimeMillis();
		long previous, next;

		previous = getPreviousInterval(current, interval);
		next = getNextInterval(current, interval);

		if ((current - previous) > (next - current)) {
			// ������ʱ�����
			date = new Date(next);
		} else {
			date = new Date(previous);
		}
		// if (LOGGER.isDebugEnabled()) {
		// LOGGER.debug("��ǰʱ��=" + getDBDateTime(current) + ",current="
		// + current);
		// LOGGER.debug("ǰ�μ��ʱ��=" + getDBDateTime(previous) + ",previous="
		// + previous);
		// LOGGER.debug("��μ��ʱ��=" + getDBDateTime(next) + ",next=" + next);
		// LOGGER.debug("����ʱ��=" + getDBDateTime(date.getTime()));
		// }

		return date;
	}

	/**
	 * ����Сʱ���ĳ�ʼ����ʱʱ��,<br>
	 * ���񴫽�����interval����3600��(һСʱ��������),<br>
	 * �ӵ���0�㿪ʼ����,�ó���һ��interal�������������ڵ�ǰֵ��Сʱ�����ǵ�һ��ִ�е�ʱ��,<br>
	 * ���統ǰʱ����3:23,interval��4Сʱ,��ô4:00����ִ��ʱ��,<br>
	 * ��ǰʱ����15:30,interval��5Сʱ,��ô20:00����ִ��ʱ��
	 * 
	 * @param current
	 *            ��ǰʱ��
	 * @param interval
	 *            ʱ����
	 * 
	 * @return ��ʼ����ʱʱ��
	 */
	// public static long getInitialDelayHours(long current, long interval) {
	// // ʱ����Сʱ��
	// long intervalHour = (interval / HOUR_SECONDS);
	// // ��ǰʱ��
	// Calendar currCalendar = Calendar.getInstance();
	// currCalendar.setTime(new Date(current));
	// // ����ʱ��
	// Calendar runCalendar = (Calendar) currCalendar.clone();
	// // ���÷���Ϊ0
	// runCalendar.set(Calendar.MINUTE, ZERO);
	// runCalendar.set(Calendar.SECOND, ZERO);
	// runCalendar.set(Calendar.MILLISECOND, ZERO);
	//
	// for (;;) {
	// // Сʱ����1
	// runCalendar.add(Calendar.HOUR, ONE);
	// // ��Сʱ����ʱ����Сʱ����������,����ʱ���Ѿ��ڶ���
	// if ((runCalendar.get(Calendar.HOUR_OF_DAY) % intervalHour == ZERO)
	// || (runCalendar.get(Calendar.DAY_OF_YEAR) != currCalendar
	// .get(Calendar.DAY_OF_YEAR))) {
	// break;
	// }
	// }
	//
	// return (runCalendar.getTimeInMillis() - currCalendar.getTimeInMillis())
	// / SECONDS_MILLISECONDS;
	// }
	/**
	 * �����뼶�ĳ�ʼ����ʱʱ��
	 * 
	 * @param current
	 *            ��ǰʱ��
	 * @param interval
	 *            ʱ����
	 * 
	 * @return �뼶�ĳ�ʼ����ʱʱ��
	 */
	// public static long getInitialDelaySeconds(long current, long interval) {
	// return getInitialDelayMilliseconds(current, interval)
	// / SECONDS_MILLISECONDS;
	// }
	/**
	 * ���غ��뼶�ĳ�ʼ����ʱʱ��
	 * 
	 * @param current
	 *            ��ǰʱ��
	 * @param interval
	 *            ʱ����
	 * 
	 * @return ���뼶�ĳ�ʼ����ʱʱ��
	 */
	public static long getInitialDelayMilliseconds(long current, long interval) {
		return (long) (getNextInterval(current, interval) - current);
	}

	/**
	 * ������һ��ִ�е�ʱ��
	 * 
	 * @param current
	 *            ��ǰʱ��
	 * @param interval
	 *            ʱ����
	 * 
	 * @return ��һ��ִ�е�ʱ��
	 */
	public static long getPreviousInterval(long current, long interval) {
		// ϵͳ����1970-01-01 08:00:00����Ϊʱ������,����ͨ���������������
		// ʱ������Լ1Сʱ,��Ҫ�������,��Ϊϵͳ��ʼʱ��Ϊ1970-01-01
		// 08:00:00,����Сʱ����������������,Ŀǰ����5Сʱ����ļ��㲻׼

		if (interval > HOUR_SECONDS) {
			return getPreviousInterval0(current + HOUR_8_MILLISECONDS, interval)
					- HOUR_8_MILLISECONDS;
		} else {
			return getPreviousInterval0(current, interval);
		}
	}

	/**
	 * ������һ��ִ�е�ʱ��
	 * 
	 * @param current
	 *            ��ǰʱ��
	 * @param interval
	 *            ʱ����
	 * 
	 * @return ��һ��ִ�е�ʱ��
	 */
	private static long getPreviousInterval0(long current, long interval) {
		return ((getCurrentIntervalQuotient(current, interval)) * interval * SECONDS_MILLISECONDS);
	}

	/**
	 * ������һ��ִ�е�ʱ��
	 * 
	 * @param current
	 *            ��ǰʱ��
	 * @param interval
	 *            ʱ����
	 * 
	 * @return ��һ��ִ�е�ʱ��
	 */
	public static long getNextInterval(long current, long interval) {
		// ʱ������Լ1Сʱ,��Ҫ�������,��Ϊϵͳ��ʼʱ��Ϊ1970-01-01
		// 08:00:00,����Сʱ����������������,Ŀǰ����5Сʱ����ļ��㲻׼
		if (interval > HOUR_SECONDS) {
			return getNextInterval0(current + HOUR_8_MILLISECONDS, interval)
					- HOUR_8_MILLISECONDS;
		} else {
			return getNextInterval0(current, interval);
		}
	}

	/**
	 * ������һ��ִ�е�ʱ��
	 * 
	 * @param current
	 *            ��ǰʱ��
	 * @param interval
	 *            ʱ����
	 * 
	 * @return ��һ��ִ�е�ʱ��
	 */
	private static long getNextInterval0(long current, long interval) {
		return ((getCurrentIntervalQuotient(current, interval) + 1L) * interval * SECONDS_MILLISECONDS);
	}

	/**
	 * ���ص�ǰʱ����interval����
	 * 
	 * @param current
	 *            ��ǰʱ��
	 * @param interval
	 *            ʱ����
	 * 
	 * @return ��ǰʱ����interval����
	 */
	private static long getCurrentIntervalQuotient(long current, long interval) {
		return (current / interval / SECONDS_MILLISECONDS);
	}

	/**
	 * ת�����ݿ�ʱ��Ϊ�ļ�ʱ��
	 * 
	 * @param dbDateTime
	 * 
	 * @return �ļ���ʽ��ʱ��
	 */
	public static String convertDBDateTime2FileDateTime(String dbDateTime) {
		String result = dbDateTime;
		try {
			synchronized (FILEDATETIMESDF) {
				result = FILEDATETIMESDF
						.format(DBDATETIMESDF.parse(dbDateTime));
			}
		} catch (Exception e) {
			// ��ȡ��ǰʱ����ļ�ʱ��
			result = getFileDateTime(System.currentTimeMillis());
			LOGGER.error("ת�����ݿ���ʽʱ��='" + dbDateTime + "'Ϊ�ļ���ʽʱ��ʱ���ִ���", e);
		}
		return result;
	}

	/**
	 * ���ص�ǰʱ���Ƿ��Ѿ������Ƚ�ʱ��+ʱ����
	 * 
	 * @param compareTime
	 *            �Ƚ�ʱ��
	 * @param interval
	 *            ʱ����
	 * 
	 * @return �Ƿ�ʱ
	 */
	public static boolean isTimeout(long compareTime, long interval) {
		long current = System.currentTimeMillis();
		return current > (compareTime + interval);
	}

	/**
	 * �����ļ�ȫ·��
	 * 
	 * @param filePath
	 *            �ļ�·��
	 * @param fileName
	 *            �ļ�����
	 * @return �ļ���ȫ·��
	 */
	public static String getFileAllName(String filePath, String fileName) {
		return filePath + File.separator + convertFileName(fileName);
	}

	/**
	 * ��String����Ϊָ���ļ�·���µ��ļ�
	 * 
	 * @param filePath
	 *            �ļ�·��
	 * @param fileName
	 *            �ļ�����
	 * @param str
	 *            ������ı�
	 * @return �ļ���
	 */
	public static String stringSaveFile(String filePath, String fileName,
			String str) {
		// �ļ���
		String result = "";

		// �ļ������
		FileOutputStream fos = null;
		// �ļ�ȫ��
		String name = getFileAllName(filePath, fileName);
		try {
			File path = new File(filePath);
			if (!path.exists()) {
				if (!path.mkdirs()) {
					// ����Ŀ¼ʧ��
				}
			}
			fos = new FileOutputStream(name);
			fos.write(str.getBytes(UTF8));
			fos.flush();
			result = new File(name).getCanonicalPath();
		} catch (FileNotFoundException fnfe) {
			LOGGER.error("�ı�����Ϊ�ļ�='" + name + "'ʱ���ִ���", fnfe);
		} catch (IOException ie) {
			LOGGER.error("�ı�����Ϊ�ļ�='" + name + "'ʱ���ִ���", ie);
		} finally {
			try {
				if (fos != null) {
					fos.close();
				}
			} catch (IOException ie) {
				LOGGER.error("�ı�����Ϊ�ļ�='" + name + "'�ر��ļ������ʱ���ִ���", ie);
			}
		}
		return result;
	}

	/**
	 * ��String����Ϊָ���ļ�·���µ��ļ�
	 * 
	 * @param fileAllName
	 *            �ļ�ȫ��
	 * @param str
	 *            ������ı�
	 * 
	 * @return �ļ���
	 */
	public static boolean stringSaveFile(String fileAllName, String str) {
		return stringSaveFile(new File(fileAllName), str);
	}

	/**
	 * ��String����Ϊָ���ļ�·���µ��ļ�
	 * 
	 * @param file
	 *            �ļ�
	 * @param str
	 *            ������ı�
	 * 
	 * @deprecated �˷�������,����ļ�Ŀ¼�����ڲ����Զ�����
	 * 
	 * @return �ļ���
	 */
	public static boolean stringSaveFile0(File file, String str) {
		// �����ʶ
		boolean result = false;

		// �ļ������
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			fos.write(str.getBytes(UTF8));
			fos.flush();
			result = true;
		} catch (FileNotFoundException fnfe) {
			LOGGER.error("�ı�����Ϊ�ļ�='" + normalizeObject(file) + "'ʱ���ִ���", fnfe);
		} catch (IOException ie) {
			LOGGER.error("�ı�����Ϊ�ļ�='" + normalizeObject(file) + "'ʱ���ִ���", ie);
		} finally {
			try {
				if (fos != null) {
					fos.close();
				}
			} catch (IOException ie) {
				LOGGER.error("�ı�����Ϊ�ļ�='" + normalizeObject(file)
						+ "'�ر��ļ������ʱ���ִ���", ie);
			}
		}
		return result;
	}

	/**
	 * ��String����Ϊָ���ļ�·���µ��ļ�
	 * 
	 * @param file
	 *            �ļ�
	 * @param str
	 *            ������ı�
	 * 
	 * @return �ļ���
	 */
	public static boolean stringSaveFile(File file, String str) {
		// �����ʶ
		boolean result = false;
		try {
			FileUtils.writeStringToFile(file, str, UTF8);
			result = true;
		} catch (IOException ie) {
			LOGGER.error("�ı�����Ϊ�ļ�='" + normalizeObject(file) + "'ʱ���ִ���", ie);
		}
		return result;
	}

	/**
	 * ��Object��ʽ���󱣴�Ϊָ���ļ�·���µ��ļ�
	 * 
	 * @param filePath
	 *            �ļ�·��
	 * @param fileName
	 *            �ļ�����
	 * @param obj
	 *            ����
	 * 
	 * @return �ļ���
	 * 
	 * @throws Exception
	 */
	public static String xmlSaveFile(String filePath, String fileName,
			Object obj) {
		// �ļ���
		String result = "";

		FileWriter fileWriter = null;
		XMLWriter xmlWriter = null;
		String name = getFileAllName(filePath, fileName);
		try {
			fileWriter = new FileWriter(name);
			// ����ʽ
			OutputFormat outputFormat = OutputFormat.createPrettyPrint();
			outputFormat.setEncoding(UTF8);
			xmlWriter = new XMLWriter(fileWriter, outputFormat);
			if (obj instanceof String) {
				Document doc = DocumentHelper.parseText((String) obj);
				xmlWriter.write(doc);
			} else {
				xmlWriter.write(obj);
			}
			result = new File(name).getCanonicalPath();
		} catch (DocumentException de) {
			LOGGER.error("XML����Ϊ�ļ�='" + name + "'ʱ���ִ���", de);
		} catch (IOException ie) {
			LOGGER.error("XML����Ϊ�ļ�='" + name + "'ʱ���ִ���", ie);
		} finally {
			try {
				if (xmlWriter != null) {
					xmlWriter.close();
				}
			} catch (IOException ie) {
				LOGGER.error("XML����Ϊ�ļ�='" + name + "'�ر�XMLWriterʱ���ִ���", ie);
			}
			try {
				if (fileWriter != null) {
					fileWriter.close();
				}
			} catch (IOException ie) {
				LOGGER.error("XML����Ϊ�ļ�='" + name + "'�ر�FileWriterʱ���ִ���", ie);
			}
		}
		return result;
	}

	/**
	 * ת���ļ���,�ļ����а����Ƿ��ַ�,<br>
	 * ����MOID�а���:
	 * 
	 * @param fileName
	 *            �ļ���
	 * @return ת������ļ���
	 */
	public static String convertFileName(String fileName) {
		// Windows�µ��ļ������ܰ���\ / : * ? " < > |
		return fileName.replaceAll(":", "��");
	}

	/**
	 * ��ȡInetSocketAddress����Ϣ
	 * 
	 * @param inetSocketAddress
	 *            ��ַ
	 * 
	 * @return ��Ϣ
	 */
	public static String monitorInetSocketAddress(
			InetSocketAddress inetSocketAddress) {
		StringBuffer strBuff = new StringBuffer();
		strBuff.append("HostName=" + inetSocketAddress.getHostName() + ",");
		strBuff.append("Port=" + inetSocketAddress.getPort() + ",");
		strBuff.append("Address='"
				+ inetSocketAddress.getAddress().getCanonicalHostName() + ","
				+ inetSocketAddress.getAddress().getHostAddress() + ","
				+ inetSocketAddress.getAddress().getHostName() + "'");
		return strBuff.toString();
	}

	/**
	 * ���ر���IP��ַ
	 * 
	 * @return ����IP��ַ
	 */
	public static String getLocalHost() {
		if (localHost.equals("")) {
			StringBuffer hostBuffer = new StringBuffer();
			try {
				Enumeration<NetworkInterface> networkInterfaces = NetworkInterface
						.getNetworkInterfaces();

				while (networkInterfaces.hasMoreElements()) {
					NetworkInterface networkInterface = networkInterfaces
							.nextElement();
					Enumeration<InetAddress> inetAddresses = networkInterface
							.getInetAddresses();
					while (inetAddresses.hasMoreElements()) {
						InetAddress inetAddress = inetAddresses.nextElement();
						if (!inetAddress.getHostAddress().startsWith(
								LOCALHOST_PREFIX)) {
							hostBuffer.append(inetAddress.getHostAddress()
									+ SEPARATOR);
						}
					}
				}
			} catch (java.net.SocketException se) {
				LOGGER.error("��ȡ���ص�ַʱ���ִ���", se);
			}
			if (hostBuffer.length() > 0) {
				hostBuffer.deleteCharAt(hostBuffer.length() - 1);
			}
			localHost = hostBuffer.toString();
		}
		return localHost;
	}

	/**
	 * hostName�Ƿ���IP��ַ
	 * 
	 * @param hostName
	 *            ������
	 * 
	 * @return �Ƿ�IP��ַ
	 */
	public static boolean isIPAddress(String hostName) {
		return ipPattern.matcher(hostName).matches();
	}

	/**
	 * ����IP��ַ
	 * 
	 * @param hostName
	 *            ������
	 * @return IP��ַ
	 */
	public static String getIPAddress(String hostName) {
		if (!normalizeString(hostName).equals("")) {
			if (isIPAddress(hostName)) {
				return hostName;
			} else {
				String ipAddress = "";
				try {
					InetAddress inetAddress = InetAddress.getByName(hostName);
					ipAddress = inetAddress.getHostAddress();
				} catch (UnknownHostException uhe) {
					LOGGER.error("��ȡ������='" + hostName + "'��IP��ַʱ���ִ���", uhe);
				}
				return ipAddress;
			}
		} else {
			return "";
		}
	}

	/**
	 * ʹ��XPATH�ڶ����в�ѯ���ڵ�
	 * 
	 * @param xPathStr
	 *            XPATH��
	 * @param obj
	 *            dom4j����
	 * 
	 * @return dom4j�ڵ�
	 */
	public static Node selectSingleNode(String xPathStr, Object obj) {
		if ((!normalizeString(xPathStr).equals("")) && (obj != null)) {
			DefaultXPath defaultXPath = (DefaultXPath) xPathMap.get(xPathStr);
			if (defaultXPath == null) {
				defaultXPath = new DefaultXPath(xPathStr);
				xPathMap.put(xPathStr, defaultXPath);
			}
			Node node = defaultXPath.selectSingleNode(obj);
			if (LOGGER.isDebugEnabled()) {
				StringBuffer debugMsg = new StringBuffer();
				debugMsg.append("ʹ��XPATH='" + xPathStr + "'�ڶ���='" + obj
						+ "'�в�ѯ���");
				if (node == null) {
					debugMsg.append("Ϊ��");
				} else {
					debugMsg.append("='" + node.asXML() + "'");
				}
				LOGGER.debug(debugMsg);
			}
			return node;
		} else {
			return null;
		}
	}

	/**
	 * ʹ��XPATH�ڶ����в�ѯ�ڵ��б�
	 * 
	 * @param xPathStr
	 *            XPATH��
	 * @param obj
	 *            dom4j����
	 * 
	 * @return dom4j�ڵ��б�
	 */
	@SuppressWarnings("unchecked")
	public static List selectNodes(String xPathStr, Object obj) {
		if ((!normalizeString(xPathStr).equals("")) && (obj != null)) {
			DefaultXPath defaultXPath = (DefaultXPath) xPathMap.get(xPathStr);
			if (defaultXPath == null) {
				defaultXPath = new DefaultXPath(xPathStr);
				xPathMap.put(xPathStr, defaultXPath);
			}
			List nodeList = defaultXPath.selectNodes(obj);
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("ʹ��XPATH='" + xPathStr + "'�ڶ���='" + obj
						+ "'�в�ѯ�����'" + nodeList.size() + "'��");
			}
			return nodeList;
		} else {
			return new ArrayList();
		}
	}

	/**
	 * ���ذٷֱ��ַ���
	 * 
	 * @param divisor
	 *            ����
	 * @param dividend
	 *            ������
	 * @return �ٷֱ��ַ���
	 */
	public static String getPercent(long divisor, long dividend) {
		if (dividend == 0) {
			return "";
		} else {
			return FOURDECIMAL_PERCENTDF
					.format((Double.valueOf(divisor) / dividend));
		}
	}

	/**
	 * ����2λС���ٷֱ��ַ���
	 * 
	 * @param divisor
	 *            ����
	 * @param dividend
	 *            ������
	 * 
	 * @return �ٷֱ��ַ���
	 */
	public static String getPercent2Bits(long divisor, long dividend) {
		if (dividend == 0) {
			return 0 + "";
		} else {
			return BITS2_NOPERCENT_PERCENTDF
					.format((Double.valueOf(divisor) / dividend));
		}
	}

	/**
	 * ת��2λС���ٷֱ�
	 * 
	 * @param percentStr
	 *            �ٷֱ��ַ���
	 * 
	 * @return �ٷֱ�
	 */
	public static double getPercent2Bits(String percentStr) {
		double percent = 0;
		try {
			Number percentNum = BITS2_NOPERCENT_PERCENTDF.parse(percentStr);
			percent = percentNum.doubleValue();
		} catch (Exception e) {
			LOGGER.error("getPercent2Bits error,percentStr='" + percentStr
					+ "'", e);
		}
		return percent;
	}

	/**
	 * ���ذٷֱ��ַ���
	 * 
	 * @param percent
	 *            �ٷֱ�
	 * 
	 * @return �ٷֱ��ַ���
	 */
	public static String getPercent(double percent) {
		return DEFAULTPERCENTDF.format(percent);
	}

	/**
	 * ��ʽ�����������,����С�����2λ
	 * 
	 * @param number
	 *            ������
	 * 
	 * @return ����С�����2λ���ַ���
	 */
	public static String floatFormat2Bits(float number) {
		return BITS2_DECIMALTDF.format(number);
	}

	/**
	 * ���������ļ���
	 * 
	 * @param resource
	 *            �����ļ���
	 * 
	 * @return �����ļ���
	 */
	public static InputStream getResourceAsStream(String resource) {
		String stripped = resource.startsWith("/") ? resource.substring(1)
				: resource;

		InputStream is = null;
		URL url = null;

		ClassLoader classLoader = Thread.currentThread()
				.getContextClassLoader();
		if (classLoader != null) {
			is = classLoader.getResourceAsStream(stripped);
			url = classLoader.getResource("");
		}

		if (is == null) {
			LOGGER
					.error("Thread.currentThread().getContextClassLoader().getResource(),·��='"
							+ getURLPath(url) + "'�޷��ҵ���Դ='" + stripped + "'");
			is = ClassLoader.getSystemResourceAsStream(stripped);
			url = ClassLoader.getSystemResource("");
		} else {
			LOGGER
					.info("Thread.currentThread().getContextClassLoader().getResource(),·��='"
							+ getURLPath(url) + "'�ҵ���Դ='" + stripped + "'");
			return is;
		}

		if (is == null) {
			LOGGER.error("ClassLoader.getSystemResource(),·��='"
					+ getURLPath(url) + "'�޷��ҵ���Դ='" + stripped + "'");
			is = Util.class.getResourceAsStream(resource);
			url = Util.class.getResource("");
		} else {
			LOGGER.info("ClassLoader.getSystemResource(),·��='"
					+ getURLPath(url) + "'�ҵ���Դ='" + stripped + "'");
			return is;
		}

		if (is == null) {
			LOGGER.error("Util.class.getResource(),·��='" + getURLPath(url)
					+ "'�޷��ҵ���Դ='" + resource + "'");
			is = Util.class.getClassLoader().getResourceAsStream(stripped);
			url = Util.class.getClassLoader().getResource("");
		} else {
			LOGGER.info("Util.class.getResource(),·��='" + getURLPath(url)
					+ "'�ҵ���Դ='" + resource + "'");
			return is;
		}

		if (is == null) {
			LOGGER.error("Util.class.getClassLoader().getResource(),·��='"
					+ getURLPath(url) + "'�޷��ҵ���Դ='" + stripped + "'");
			LOGGER.error("������Դ·�����޷��ҵ���Դ='" + stripped + "'");
		} else {
			LOGGER.info("Util.class.getClassLoader().getResource(),·��='"
					+ getURLPath(url) + "'�ҵ���Դ='" + stripped + "'");
			return is;
		}
		return is;
	}

	/**
	 * ���������ļ�
	 * 
	 * @param resource
	 *            �����ļ���
	 * 
	 * @return �����ļ�
	 */
	public static URL getResource(String resource) {
		String stripped = resource.startsWith("/") ? resource.substring(1)
				: resource;

		URL url = null;
		URL pathUrl = null;
		String path = "";

		ClassLoader classLoader = Thread.currentThread()
				.getContextClassLoader();
		if (classLoader != null) {
			url = classLoader.getResource(resource);
			pathUrl = classLoader.getResource("");
			if (pathUrl != null) {
				path = pathUrl.getPath();
			}
		}

		if (url == null) {
			LOGGER
					.error("Thread.currentThread().getContextClassLoader().getResource(),�޷��ҵ���Դ='"
							+ stripped + "',path='" + path + "'");
			url = ClassLoader.getSystemResource(resource);
			pathUrl = ClassLoader.getSystemResource("");
			if (pathUrl != null) {
				path = pathUrl.getPath();
			}
		} else {
			LOGGER
					.info("Thread.currentThread().getContextClassLoader().getResource(),�ҵ���Դ='"
							+ getURLPath(url) + "'");
			return url;
		}

		if (url == null) {
			LOGGER.error("ClassLoader.getSystemResource(),�޷��ҵ���Դ='" + stripped
					+ "',path='" + path + "'");
			url = Util.class.getResource(resource);
			pathUrl = Util.class.getResource("");
			if (pathUrl != null) {
				path = pathUrl.getPath();
			}
		} else {
			LOGGER.info("ClassLoader.getSystemResource(),�ҵ���Դ='"
					+ getURLPath(url) + "'");
			return url;
		}

		if (url == null) {
			LOGGER.error("Util.class.getResource(),�޷��ҵ���Դ='" + resource
					+ "',path='" + path + "'");
			url = Util.class.getClassLoader().getResource(resource);
			pathUrl = Util.class.getClassLoader().getResource("");
			if (pathUrl != null) {
				path = pathUrl.getPath();
			}
		} else {
			LOGGER.info("Util.class.getResource(),�ҵ���Դ='" + getURLPath(url)
					+ "'");
			return url;
		}

		if (url == null) {
			LOGGER.error("Util.class.getClassLoader().getResource(),�޷��ҵ���Դ='"
					+ stripped + "',path='" + path + "'");
			LOGGER.error("������Դ·�����޷��ҵ���Դ='" + stripped + "'");
		} else {
			LOGGER.info("Util.class.getClassLoader().getResource(),�ҵ���Դ='"
					+ getURLPath(url) + "'");
			return url;
		}
		return url;
	}

	/**
	 * ���������ļ�,����ļ�������,�򴴽�һ���ļ�����
	 * 
	 * @param resource
	 *            �����ļ���
	 * 
	 * @return �����ļ�
	 */
	public static File getResourceFile(String resource) {
		File file = null;
		try {
			URL fileURL = getResource(resource);
			if (fileURL != null) {
				URI fileURI = fileURL.toURI();
				file = new File(fileURI);
			}
		} catch (Exception e) {
			LOGGER.error("�޷�����Դ='" + resource + "'ת��ΪFile����", e);
		} finally {
			if (file == null) {
				file = new File(resource);
			}
		}
		return file;
	}

	/*
	 * ����URL·��
	 */
	private static String getURLPath(URL url) {
		if (url != null) {
			return url.getFile();
		} else {
			return "";
		}
	}

	/**
	 * ��ISO-8859-1ת��ΪUTF-8
	 * 
	 * @param str
	 *            ת��ǰ��ISO�����ַ���
	 * 
	 * @return UTF-8�ַ���
	 */
	public static String isoToUTF8(String str) {
		try {
			return new String(str.getBytes(ISO88591), UTF8);
		} catch (UnsupportedEncodingException uee) {
			return str;
		} catch (Exception e) {
			return str;
		}
	}

	/**
	 * ɾ���ļ�
	 * 
	 * @param filePath
	 *            �ļ�·��
	 * @param fileName
	 *            �ļ�����
	 * 
	 * @return �����ʶ
	 */
	public static boolean deleteFile(String filePath, String fileName) {
		boolean result = false;
		File file = new File(getFileAllName(filePath, fileName));
		if ((file != null) && (file.exists()) && (file.isFile())) {
			result = file.delete();
		}
		return result;
	}

	/**
	 * ɾ���ļ�
	 * 
	 * @param file
	 *            �ļ�
	 * 
	 * @return �����ʶ
	 */
	public static boolean deleteFile(File file) {
		if (file != null) {
			return file.delete();
		} else {
			return false;
		}
	}

	/**
	 * ���Ŀ¼�е��ļ�
	 * 
	 * @param file
	 *            �ļ�·��
	 * 
	 * @return �����ʶ
	 */
	public static boolean cleanDirectory(File file) {
		// �����ʶ
		boolean result = false;
		if (file != null) {
			try {
				FileUtils.cleanDirectory(file);
				result = true;
			} catch (IOException ie) {
				LOGGER.error("���Ŀ¼='" + normalizeObject(file) + "'�е��ļ�ʱ���ִ���",
						ie);
			}
		}
		return result;
	}

	/**
	 * �ֽ���ת��Ϊ��ʾ�ַ���
	 * 
	 * @param size
	 *            �ֽ���
	 * @return ��ʾ�ַ���
	 */
	public static String byteCountToDisplaySize(long size) {
		return FileUtils.byteCountToDisplaySize(size);
	}

	/**
	 * ��UTF-8��ʽ��ȡ�ļ�����
	 * 
	 * @param filePath
	 *            �ļ�·��
	 * @param fileName
	 *            �ļ�����
	 * 
	 * @return �ļ�����
	 */
	public static String readFileToString(String filePath, String fileName) {
		// �ļ�����
		String text = "";
		// �ļ�ȫ��
		String name = null;
		try {
			name = getFileAllName(filePath, fileName);
			File file = new File(name);
			text = FileUtils.readFileToString(file, UTF8);
		} catch (IOException ie) {
			LOGGER.error("��ȡ�ļ�='" + normalizeString(name) + "'ʱ���ִ���", ie);
		}
		return text;
	}

	/**
	 * ��UTF-8��ʽд���ļ�����
	 * 
	 * @param filePath
	 *            �ļ�·��
	 * @param fileName
	 *            �ļ�����
	 * @param data
	 *            �ļ�����
	 * 
	 * @return �����ʶ
	 */
	public static boolean writeStringToFile(String filePath, String fileName,
			String data) {
		// �����ʶ
		boolean result = false;
		// �ļ�ȫ��
		String name = null;
		try {
			name = getFileAllName(filePath, fileName);
			File file = new File(name);
			FileUtils.writeStringToFile(file, data, UTF8);
			result = true;
		} catch (IOException ie) {
			LOGGER.error("д���ļ�='" + normalizeString(name) + "',����='" + data
					+ "'ʱ���ִ���", ie);
		}
		return result;
	}

	/**
	 * ����MD5��
	 * 
	 * @param str
	 *            Դ��
	 * @return MD5��
	 */
	public static String getMD5(String str) {
		String md5;
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.reset();
			messageDigest.update(str.getBytes());
			byte digest[] = messageDigest.digest();

			StringBuffer hexString = new StringBuffer();
			for (int i = 0; i < digest.length; i++) {
				hexString.append(Integer.toHexString(0xFF & digest[i]));
			}
			md5 = hexString.toString();
		} catch (NoSuchAlgorithmException e) {
			md5 = "MD5_" + str.hashCode();
		}
		return md5;
	}

	/**
	 * �����Ƿ��Ǵ��ڵ��ļ�
	 * 
	 * @param file
	 *            �ļ�
	 * 
	 * @return �Ƿ��Ǵ��ڵ��ļ�
	 */
	public static boolean isExistFile(File file) {
		try {
			return (file != null) && file.exists() && file.isFile();
		} catch (Exception e) {
			LOGGER.error("isExistFile error, file='" + normalizeObject(file)
					+ "', " + e);
			return false;
		}
	}

	/**
	 * �����Ƿ��Ǵ��ڲ�����Ч(�ֽ�������0)���ļ�
	 * 
	 * @param file
	 *            �ļ�
	 * 
	 * @return �Ƿ��Ǵ��ڲ�����Ч���ļ�
	 */
	public static boolean isExistValidFile(File file) {
		try {
			return (file != null) && file.exists() && file.isFile()
					&& file.length() > 0;
		} catch (Exception e) {
			LOGGER.error("isExistValidFile error, file='"
					+ normalizeObject(file) + "', " + e);
			return false;
		}
	}

	/**
	 * ����splitCount���ָ��б�
	 * 
	 * @param allList
	 *            ȫ�б�
	 * @param splitCount
	 *            �ָ�ĸ���
	 * @return �ָ�ȫ�б���б�
	 */
	@SuppressWarnings("unchecked")
	public static List<List> splitList(List allList, int splitCount) {
		List<List> splitList = new ArrayList<List>();
		while (allList.size() > 0) {
			if (allList.size() > splitCount) {
				splitList.add(new ArrayList(allList.subList(0, splitCount)));
				for (int i = 0; i < splitCount; i++) {
					allList.remove(0);
				}
			} else {
				splitList.add(new ArrayList(allList));
				allList.clear();
			}
		}
		return splitList;
	}

	/**
	 * ԭʼ�������ӿո�,ʹ�ִ����ȴﵽԤ�ڳ���
	 * 
	 * @param strBuffer
	 *            �ַ�������
	 * @param src
	 *            ԭʼ��
	 * @param length
	 *            Ԥ�ڳ���
	 * @param isLeftAdd
	 *            �Ƿ��������(�Ҷ���)
	 * @param lastAttaString
	 *            ���渽���ַ���
	 */
	public static void stringAddSpace(StringBuffer strBuffer, String src,
			int length, boolean isLeftAdd, String lastAttaString) {
		if (isLeftAdd) {
			for (int i = 0; i < length - src.length(); i++) {
				strBuffer.append(SPACE);
			}
			strBuffer.append(src);
		} else {
			strBuffer.append(src);
			for (int i = 0; i < length - src.length(); i++) {
				strBuffer.append(SPACE);
			}
		}
		strBuffer.append(lastAttaString);
	}

	/**
	 * ѭ������length��ԭʼ��
	 * 
	 * @param strBuffer
	 *            �ַ�������
	 * @param src
	 *            ԭʼ��
	 * @param length
	 *            ����
	 * @param lastAttaString
	 *            ���渽���ַ���
	 */
	public static void stringCircleAddString(StringBuffer strBuffer,
			String src, int length, String lastAttaString) {
		for (int i = 0; i < length; i++) {
			strBuffer.append(src);
		}
		strBuffer.append(lastAttaString);
	}

	/*
	 * �ж��ֻ������Ƿ�Ϸ�
	 */
	public static boolean isMobilePhone(String source) {
		String mask = "^13[0-9]{1}[0-9]{8}$";
		if (check(mask, source)) {
			return true;
		}

		mask = "^15[9]{1}[0-9]{8}$";
		if (check(mask, source)) {
			return true;
		}

		mask = "^18[9]{1}[0-9]{8}$";
		if (check(mask, source)) {
			return true;
		}

		mask = "^1[3,5]{1}[0-9]{1}[0-9]{8}$";
		return check(mask, source);

	}

	public static boolean check(String mask, String source) {
		if (source == null)
			return false;

		if ("".equals(source))
			return false;

		if (mask == null)
			return true;

		if ("".equals(mask))
			return true;

		Pattern p = Pattern.compile(mask);
		Matcher m = p.matcher(source);
		return m.find();

	}

	/**
	 * �Ƿ�ΪIPv4
	 * 
	 * @param source
	 * @return
	 */
	public static boolean isIPv4(String source) {
		String mask = "^(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])$";
		return check(mask, source);
	}

	/**
	 * �Ƿ�ΪIPv6
	 * 
	 * @param source
	 * @return
	 */
	public static boolean isIPv6(String source) {
		String mask = "^([\\da-fA-F]{1,4}:){6}((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$|^::([\\da-fA-F]{1,4}:){0,4}((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$|^([\\da-fA-F]{1,4}:):([\\da-fA-F]{1,4}:){0,3}((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$|^([\\da-fA-F]{1,4}:){2}:([\\da-fA-F]{1,4}:){0,2}((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$|^([\\da-fA-F]{1,4}:){3}:([\\da-fA-F]{1,4}:){0,1}((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$|^([\\da-fA-F]{1,4}:){4}:((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$|^([\\da-fA-F]{1,4}:){7}[\\da-fA-F]{1,4}$|^:((:[\\da-fA-F]{1,4}){1,6}|:)$|^[\\da-fA-F]{1,4}:((:[\\da-fA-F]{1,4}){1,5}|:)$|^([\\da-fA-F]{1,4}:){2}((:[\\da-fA-F]{1,4}){1,4}|:)$|^([\\da-fA-F]{1,4}:){3}((:[\\da-fA-F]{1,4}){1,3}|:)$|^([\\da-fA-F]{1,4}:){4}((:[\\da-fA-F]{1,4}){1,2}|:)$|^([\\da-fA-F]{1,4}:){5}:([\\da-fA-F]{1,4})?$|^([\\da-fA-F]{1,4}:){6}:$";
		return check(mask, source);
	}

	/**
	 * ����UUID�����������
	 * 
	 * @return
	 */
	public static String getRandomPwd() {
		String pwd = "";
		try {
			String uuid = UUID.randomUUID().toString();
			pwd = uuid.substring(0, uuid.indexOf("-"));
		} catch (Exception e) {
			LOGGER.error("getRandomPwd error, ", e);
			pwd = DEFAULT_PWD;
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.info("getRandomPwd pwd='" + pwd + "'");
			}
		}
		return pwd;
	}

	/**
	 * �Ƿ���һ�����ڵ��ļ�,�����ļ���С��ͬ
	 * 
	 * @param file
	 * @return
	 */
	public static boolean isExistFile(File file, long size) {
		try {
			return file != null && file.exists() && file.isFile() && size > 0
					&& file.length() == size;
		} catch (Exception e) {
			LOGGER.error("isExistFile error, file='" + normalizeObject(file)
					+ "', size='" + size + "', " + e);
			return false;
		}
	}

	/**
	 * �ر�������
	 * 
	 * @param is
	 */
	public static void closeInputStream(InputStream is) {
		try {
			if (is != null) {
				is.close();
			}
		} catch (Exception e) {
			LOGGER.error("closeInputStream error, is='"
					+ Util.normalizeObject(is) + "'", e);
		}
	}

	/**
	 * �ر������
	 * 
	 * @param is
	 */
	public static void closeOutputStream(OutputStream os) {
		try {
			if (os != null) {
				os.close();
			}
		} catch (Exception e) {
			LOGGER.error("closeOutputStream error, os='"
					+ Util.normalizeObject(os) + "'", e);
		}
	}
}
