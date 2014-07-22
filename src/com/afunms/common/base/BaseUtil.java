package com.afunms.common.base;

import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * ����������
 * @author  
 */
public class BaseUtil {
	private static final DecimalFormat num_df = new DecimalFormat("#.##");
	public static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static final SimpleDateFormat YMD = new SimpleDateFormat("yyyy-MM-dd");
	public static final SimpleDateFormat YMD_CN = new SimpleDateFormat("yyyy��MM��dd��");
	public static final SimpleDateFormat HH = new SimpleDateFormat("HH");
    public static SimpleDateFormat Times = new SimpleDateFormat("HH:mm:ss");

    public static SimpleDateFormat Y2DTIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static SimpleDateFormat Y2CH_FORMAT = new SimpleDateFormat("yyyy��MM��dd�� HHʱmm��ss��");

    public static SimpleDateFormat Y2NO_FORMAT = new SimpleDateFormat("yyyyMMddHHmmss");

    public static SimpleDateFormat Y2D_FORMAT = new SimpleDateFormat("yyyyMMdd");

    public static SimpleDateFormat Y2T_FORMAT = new SimpleDateFormat("HHmmss");

    public static SimpleDateFormat YMD_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    public static SimpleDateFormat Y2NO_Mill_FORMAT = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    
    public static final SimpleDateFormat Y2SPECIAL_FORMAT = new SimpleDateFormat("yyyy.MM.dd");
    
    public static void main(String[] args) {
    	try {
    		System.out.println(BaseUtil.diffMinute(BaseUtil.sdf.parse("2012-11-20 16:1:00"),BaseUtil.sdf.parse("2012-11-20 15:11:00")));
			System.out.println(BaseUtil.sdf.format(new Date()));
			System.out.println(new Date());
			
			SimpleDateFormat asdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			asdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
			System.out.println(asdf.format(new Date()));
			System.out.println(getNowHour());
			
			System.out.println(sdf.format(BaseUtil.sdf.parse("2012-1-20 16:1:00")));
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
    /**
     * ������λС��(���м����뷨:����ӽ����ַ������������ģʽ������������������ֵľ�����ȣ��������ڵ�ż������)
     */
    public static String getNumDf2(Double double1){
    	return num_df.format(double1);
    }
   /**
    * ȡ�õ�ǰʱ���Сʱ������ʱ�䣩
    */
    public static Integer getNowHour(){
    	HH.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
    	return Integer.parseInt(HH.format(new Date()));
    }
	public static String getSystemDateOfString(){
		Calendar calendar = Calendar.getInstance();		
		return sdf.format(calendar.getTime());
	}
	public static Date getSystemDate()
	{
		Calendar calendar = Calendar.getInstance();	
		return calendar.getTime();
	}
	public static Date getDate(String date)
	{
		try {
			return sdf.parse(date);
		} catch (ParseException e) {
			System.out.print("���ڸ�ʽ����ȷ!");
		}
		return getSystemDate();
	}
	/**
	 * ����������������µ�����
	 * @param oldDate
	 * @param intDay
	 * @return
	 */
    public final static Date getDateAdd(Date oldDate, int intDay) {
        Calendar calendar = Calendar.getInstance();// ʵ����calendar����
        calendar.setTime(oldDate);// ����calendar�����ʱ������

        calendar.add(Calendar.DATE, intDay);// ��������������

        return calendar.getTime();// �õ�calendar�����ʱ��

    }
    /**
    * md5 �����ַ���

    * @param String
    * @return String
     * @throws NoSuchAlgorithmException 
     * @throws NoSuchAlgorithmException 
    * @throws NoSuchAlgorithmException 
    */
    public static final String md5(String input){
       byte[] inputByte=input.getBytes();
       MessageDigest md = null;
	   try {
			md = MessageDigest.getInstance("md5");
	   } catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
	   }
       md.update(inputByte);
       byte[] digest=md.digest();
       StringBuffer buf=new StringBuffer();
       for(int i=0;i<digest.length;i++){
        int val=((int)digest[i]) & 0xff;
        if(val < 16){
         buf.append("0");
        }
        buf.append(Integer.toHexString(val));
       }
       return buf.toString().toLowerCase();
    }
    /**
     * ��hql������Ϊsql���
     * @param originalHql
     * @param sessionFactory
     * @return
     * @throws Exception
     */
    /*public static String getCountSql(String originalHql, org.hibernate.SessionFactory sessionFactory) throws Exception { 
        QueryTranslatorImpl queryTranslator = new QueryTranslatorImpl(originalHql, originalHql, 
           Collections.EMPTY_MAP, (org.hibernate.engine.SessionFactoryImplementor)sessionFactory); 

        queryTranslator.compile(Collections.EMPTY_MAP, false); 
        return queryTranslator.getSQLString(); 
    }*/

    /**
     * ����������ʽ�ķ��������ó��õ���ʽ
     * 
     * @author
     * @param strRegex
     *            ͨ��ϰ����д��������ʽ����"^\\d+\\w."�ȣ�ע��Ҫ������б�ܡ�

     * @param flagCase
     *            �Ƿ����ִ�Сд��0 �����֣�1 ���֣� Ĭ�����ִ�Сд�����⣬�˷���Ĭ���˶���ƥ�䡣

     */
    public final static Pattern createPattern(String strRegex, int flagCase) {
        Pattern patternRegex = null;
        if (flagCase == 0) {
            patternRegex = Pattern.compile(strRegex, Pattern.MULTILINE
                    | Pattern.CASE_INSENSITIVE);
        } else {
            patternRegex = Pattern.compile(strRegex, Pattern.MULTILINE);
        }
        return patternRegex;
    }

    /**
     * ʹ��������ʽ���ĳ�ִ��Ƿ�ƥ����ʽ�ķ���
     * 
     * @author 
     * @return �����Ƿ�ƥ��������ʽ��

     * @param strBeChecked
     *            �������ַ�����

     * @param strRegex
     *            �����������ʽ�ַ������磺"\\(\\d{3}\\)\\s\\d{3}-\\d{4}";
     * @param flagCase
     *            �Ƿ����ִ�Сд��0 �����֣�1 ���֣� Ĭ�����ִ�Сд�� flags Match flags, a bit mask
     *            that may include {@link #CASE_INSENSITIVE},
     *            {@link #MULTILINE}, {@link #DOTALL}, {@link #UNICODE_CASE},
     *            and {@link #CANON_EQ}
     */
    public final static boolean checkMatchRegex(String strBeChecked,
            String strRegex, int flagCase) {
        Pattern patternRegex = createPattern(strRegex, flagCase);
        Matcher m = patternRegex.matcher(strBeChecked);
        return m.find();
    }

    /**
     * ʹ��������ʽ�滻ĳ�ִ���ƥ����ʽ�Ĳ��ֵķ���
     * 
     * @author
     * @return �����滻����ִ���

     * @param strBeReplaced
     *            �������ַ�����

     * @param strRegex
     *            �����������ʽ�ַ������磺"\\(\\d{3}\\)\\s\\d{3}-\\d{4}";
     * @param flagCase
     *            �Ƿ����ִ�Сд��0 �����֣�1 ���֣� Ĭ�����ִ�Сд��

     * @param strChange
     *            �滻���ִ���

     */
    public final static String replaceMatchRegex(String strBeChecked,
            String strRegex, int flagCase, String strChange) {
        Pattern patternRegex = createPattern(strRegex, flagCase);
        Matcher m = patternRegex.matcher(strBeChecked);
        return m.replaceAll(strChange);
    }
    public final static String subMatchRegex(String strBeMatch,String strRegex,int falgCase)
    {
    	Pattern pattern = BaseUtil.createPattern(strRegex,falgCase);
		Matcher matcher = pattern.matcher(strBeMatch);
		StringBuffer buffer = new StringBuffer();
		while(matcher.find())
		{
			buffer.append(matcher.group());
		}
		return buffer.toString();
    }
    /**
     * ��ȡ�ַ����е��Ӵ�

     * @param str         ��Ҫ��ȡ���ַ���

     * @param startNum    ��ȡ�ַ�������ʼλ��
     *                    ��startNum = -1ʱ����ʾ�����ȡ���ַ��������cutNumλ

     *                    ��startNum > 0ʱ����ʾ˳���ȡ���ַ���ǰcutNumλ(����ֵ��1��ʼ)
     * @param cutNum      ��ȡ�ַ����ĸ��� 
     * @return
     */
    public final static String subString(String str,int startNum,int cutNum){
    	if(startNum > 0){
    		startNum = startNum - 1;
    		if(startNum <= str.length() && str.length() <= (startNum+cutNum)){
        		return str.substring(startNum, str.length());
        	}
        	else if(str.length() > (startNum+cutNum)){
        		return str.substring(startNum, (startNum+cutNum));
        	}
        	else{
        		return "";
        	}
    	}
    	else{
    		startNum = str.length() - cutNum ;
    		if(startNum > 0){
    			return str.substring(startNum, (startNum+cutNum));
    		}
    		else{
    			return str.substring(0, str.length());
    		}
    	}
    }
    /**
     * �������
     * @param date1 ����
     * @param date2 ����
     * @return ����(date1-date2)����������
     */
     public static int diffDate(Date date, Date date1) {
    	 return (int) ((date.getTime() - date1.getTime()) / (24 * 3600 * 1000));
     }
     /**
      * �������
      * @param date1 ����
      * @param date2 ����
      * @return ����(date1-date2)������Сʱ
      */
      public static int diffHour(Date date, Date date1) {
     	 return (int) ((date.getTime() - date1.getTime()) / (3600 * 1000));
      }
      /**
       * �������
       * @param date1 ����
       * @param date2 ����
       * @return ����(date1-date2)�����ķ���
       */
       public static int diffMinute(Date date, Date date1) {
      	 return (int) ((date.getTime() - date1.getTime()) / (60 * 1000));
       }

     public static String GenerateRandomStr() {
         String randStr = "ABCDEFGHIabcdef0123456789"; // д������ϣ�������е���ĸA-Z,a-z,0-9
         StringBuffer generateRandStr = new StringBuffer();
         Random rand = new Random();
         int randStrLength = 6; // �������ɵ�������ĳ���

         for (int i = 0; i < randStrLength; i++) {
             int randNum = rand.nextInt(25);
             generateRandStr.append(randStr.substring(randNum, randNum + 1));
         }
         System.out.println(generateRandStr); // ��ӡ��Ľ��
         return generateRandStr.toString();
     }
     
     /**
      * ȡ�õ�ǰ�������ӵ��������·ݵĵ�һ��

      * ���磬��ǰ����Ϊ2009-06-25�գ��õ�����һ���º�������7�·ݵ�1��

      * Date date = BaseUtil.getMonthAdd(date,1);
      * @param date ��ǰ����
      * @param month ����
      * @return
      */
     public static Date getMonthAdd(String date, int month)
     {
    	Calendar calendar = Calendar.getInstance();   
 		try {
			calendar.setTime(BaseUtil.YMD.parse(date));
		} catch (ParseException e) {
		}
 		calendar.set(Calendar.DAY_OF_MONTH, 1);
 		calendar.add(Calendar.MONTH, month);
 		
 		return calendar.getTime();
     }
     
     /**
      * ����������ݻ�ȡ�µ�����
      * ���磬��ǰ����Ϊ2009-12-23�գ�����һ���Ϊ2010-12-23��

      * Date date = BaseUtil.getYearAdd(date,1);
      * @param date ��ǰ����
      * @param year ���
      * @return
      */
     public static Date getYearAdd(Date date, int year)
     {
    	 Calendar calendar = Calendar.getInstance();// ʵ����calendar����
         calendar.setTime(date);// ����calendar�����ʱ������

         calendar.add(Calendar.YEAR, year);// ��������������

         return calendar.getTime();// �õ�calendar�����ʱ��

     }
     
     /**
      * ȡ�õ�ǰ�·����ڵڼ����Ⱥͼ��Ȱ����Ŀ�ʼ�·ݺͽ����·�
      * @param month
      * @return
      */
     static public String[] getQuarterOfMonth(String month)
     {
     	String [] ret = new String[2];
     	
     	String firstQuarter = "01,02,03";
     	String secondQuarter = "04,05,06";
     	String thirdQuarter = "07,08,09";
     	String fourthQuarter = "10,11,12";
     	
     	if(firstQuarter.indexOf(month) >= 0)
     	{
     		ret = new String[]{"01","03","��һ����"};
     	}
     	if(secondQuarter.indexOf(month) >= 0)
     	{
     		ret = new String[]{"04","06","�ڶ�����"};
     	}
     	if(thirdQuarter.indexOf(month) >= 0)
     	{
     		ret = new String[]{"07","09","��������"};
     	}
     	if(fourthQuarter.indexOf(month) >= 0)
     	{
     		ret = new String[]{"10","12","���ļ���"};
     	}
     	
     	return ret;
     }
     /**
      * ��ȡָ�����������ڼ�
      * @param date
      * @return 
      */
     static public String getDayOfWeek(Date date)
     {
    	 String[] week = {"������","����һ","���ڶ�","������","������","������","������"};
    	 Calendar calendar = Calendar.getInstance();   
    	 calendar.setTime(date);
    	 return week[calendar.get(Calendar.DAY_OF_WEEK)-1];
     }
     
     /**
      * �õ���������֮������
      * @param startDate
      * @param endDate
      * @return
      * @throws ParseException 
      */
     public static int getDays(String startDate, String endDate) throws ParseException
     {
     	Calendar calendar1 = Calendar.getInstance();
     	calendar1.setTime(YMD_FORMAT.parse(startDate));
     	Calendar calendar2 = Calendar.getInstance();
     	calendar2.setTime(YMD_FORMAT.parse(endDate));
     	
     	int days = calendar1.get(Calendar.DAY_OF_YEAR) - calendar2.get(Calendar.DAY_OF_YEAR) + 1;
     	
     	return days;
     }
     
     /** 
      *��Ӽ��������ڼ������(����ͷβ����)
      * Ҫ�������ʽΪʵ����: 2007-09-25
      * �����ʽ�������󽫷��ؿ��ַ���

      */
     public static String countDaysBetweenTwoData(String stratDataStr, String endDataStr) {
     	String countDays = "";
     	
     	String t1 = stratDataStr.replace('-','/'); 
         String t2 = endDataStr.replace('-','/');
         
         try{ 
             Date dt1= new Date(t1); 
             Date dt2= new Date(t2);  
             long l = dt1.getTime() - dt2.getTime(); 
       
             long countDay = l/60/60/1000/24; 
             countDays = String.valueOf(countDay+1);
         }catch(Exception e){ 
             return ""; 
         }
     	return countDays;
     }
     /**
      * �õ���������֮������(�˸�ʽ��yyyy-MM-dd)������ʱ����
      * @param startDate
      * @param endDate
      * @return
      */
     public static int getDays2(String startDate, String endDate) throws Exception
     {
     	return (int) ((YMD_FORMAT.parse(endDate).getTime() - YMD_FORMAT.parse(startDate).getTime()) / (24 * 3600 * 1000));
     }
     
     /**
      * Md5 �����㷨
      * @param source ����
      * @return ����
      */ 
     public static String getMD5(String source) {
    	byte[] sourceByte = source.getBytes() ; 
 		String s = null;
 		char hexDigits[] = { // �������ֽ�ת���� 16 ���Ʊ�ʾ���ַ�

 		'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
 				'e', 'f' };
 		try {
 			java.security.MessageDigest md = java.security.MessageDigest
 					.getInstance("MD5");
 			md.update(sourceByte);
 			byte tmp[] = md.digest(); // MD5 �ļ�������һ�� 128 λ�ĳ�������
 			// ���ֽڱ�ʾ���� 16 ���ֽ�

 			char str[] = new char[16 * 2]; // ÿ���ֽ��� 16 ���Ʊ�ʾ�Ļ���ʹ�������ַ���
 			// ���Ա�ʾ�� 16 ������Ҫ 32 ���ַ�

 			int k = 0; // ��ʾת������ж�Ӧ���ַ�λ��
 			for (int i = 0; i < 16; i++) { // �ӵ�һ���ֽڿ�ʼ���� MD5 ��ÿһ���ֽ�

 				// ת���� 16 �����ַ���ת��

 				byte byte0 = tmp[i]; // ȡ�� i ���ֽ�

 				str[k++] = hexDigits[byte0 >>> 4 & 0xf]; // ȡ�ֽ��и� 4 λ������ת��, 
 				// >>> Ϊ�߼����ƣ�������λһ������

 				str[k++] = hexDigits[byte0 & 0xf]; // ȡ�ֽ��е� 4 λ������ת��
 			}
 			s = new String(str); // ����Ľ��ת��Ϊ�ַ���


 		} catch (Exception e) {
 			e.printStackTrace();
 		}
 		return s.toUpperCase();
 	}

     /**
      * �õ���������֮����·�

      * @param startDate
      * @param endDate
      * @return
      * @throws ParseException 
      */
     public static int getMonths(String startDate, String endDate) throws ParseException
     {
     	Calendar calendar1 = Calendar.getInstance();
     	calendar1.setTime(YMD_FORMAT.parse(startDate));
     	Calendar calendar2 = Calendar.getInstance();
     	calendar2.setTime(YMD_FORMAT.parse(endDate));
     	
     	int months = calendar1.get(Calendar.MONTH) - calendar2.get(Calendar.MONTH) + 1;
     	return months;
     }
     
     /**
      * �õ���������֮����·ݲ� �ַ�����ʽΪ��yyyy-mm-dd
      * @param startDate ������

      * @param endDate  С����

      * @return
      * @throws ParseException
      */
     public static int getMonths2(String startDate, String endDate) throws ParseException
     {
    	 Calendar calendar1 = Calendar.getInstance();
    	 calendar1.setTime(YMD_FORMAT.parse(startDate));
    	 calendar1.add(Calendar.DATE, 1);
    	 Calendar calendar2 = Calendar.getInstance();
    	 calendar2.setTime(YMD_FORMAT.parse(endDate));
    	 
    	 int years = calendar1.get(Calendar.YEAR) - calendar2.get(Calendar.YEAR);
    	 int months = calendar1.get(Calendar.MONTH) - calendar2.get(Calendar.MONTH);
    	 if(calendar1.get(Calendar.DAY_OF_YEAR) < calendar2.get(Calendar.DAY_OF_YEAR))
    	 {
    		 return years * 12 + months - 1;
    	 }
    	 else
    	 {
    		 return years * 12 + months;
    	 }
    	 
     }

     /**
      * �ж�date1��date2֮ǰ
      * @param date1
      * @param date2
      * @return
      * @throws ParseException
      */
     public static boolean beforDate(String date1, String date2) throws ParseException
     {
     	Calendar calendar1 = Calendar.getInstance();
     	calendar1.setTime(sdf.parse(date1));
     	Calendar calendar2 = Calendar.getInstance();
     	calendar2.setTime(sdf.parse(date2));
     	
     	return calendar1.before(calendar2);
     }
     /**
      * �ж�date1��date2֮��
      * @param date1
      * @param date2
      * @return
      * @throws ParseException
      */
     public static boolean afterDate(String date1, String date2) throws ParseException
     {
     	Calendar calendar1 = Calendar.getInstance();
     	calendar1.setTime(sdf.parse(date1));
     	Calendar calendar2 = Calendar.getInstance();
     	calendar2.setTime(sdf.parse(date2));
     	
     	return calendar1.after(calendar2);
     }
     /**
      * �ж�date1��date2֮��
      * @param date1
      * @param date2
      * @return
      * @throws ParseException
      */
     public static boolean afterDate(Date date1, Date date2) throws ParseException
     {
     	Calendar calendar1 = Calendar.getInstance();
     	calendar1.setTime(date1);
     	Calendar calendar2 = Calendar.getInstance();
     	calendar2.setTime(date2);
     	
     	return calendar1.after(calendar2);
     }
     
     /**
      * ��String[]תΪ��ĳ���ָ����ַ����硰,�����������ַ�����

      * 
      * @param strArrayIn
      *            ������ַ����顣

      * @param strSeparator
      *            �ָ����ַ���

      */
     public static String parseArrayToString(String[] strArrayIn,
             String strSeparator) {
     	if (strArrayIn == null) {
     		return "";
     	}
         StringBuffer sbArray = new StringBuffer();
         for (int i = 0; i < strArrayIn.length; i++) {
             sbArray.append(strArrayIn[i]);
             sbArray.append(strSeparator);
         }
         int iEnd = sbArray.length() - strSeparator.length();
         return sbArray.substring(0, iEnd);
     }

     private static final int DEF_DIV_SCALE = 10;

     /**
      * 
      * �ṩ��ȷ�ļӷ����㡣

      * 
      * @param v1
      *            ������

      * 
      * @param v2
      *            ����
      * 
      * @return ���������ĺ�
      * 
      */

     public static double add(double v1, double v2) {

         BigDecimal b1 = new BigDecimal(Double.toString(v1));

         BigDecimal b2 = new BigDecimal(Double.toString(v2));

         return b1.add(b2).doubleValue();

     }
     
     public static String add(String v1, String v2) {

         BigDecimal b1 = new BigDecimal(v1);

         BigDecimal b2 = new BigDecimal(v2);

         return b1.add(b2).toString();

     }

     /**
      * 
      * �ṩ��ȷ�ļ������㡣

      * 
      * @param v1
      *            ������

      * 
      * @param v2
      *            ����
      * 
      * @return ���������Ĳ�
      * 
      */

     public static double sub(double v1, double v2) {

         BigDecimal b1 = new BigDecimal(Double.toString(v1));

         BigDecimal b2 = new BigDecimal(Double.toString(v2));

         return b1.subtract(b2).doubleValue();

     }
     
     public static String sub(String v1, String v2) {

         BigDecimal b1 = new BigDecimal(v1);

         BigDecimal b2 = new BigDecimal(v2);

         return b1.subtract(b2).toString();

     }

     /**
      * 
      * �ṩ��ȷ�ĳ˷����㡣

      * 
      * @param v1
      *            ������

      * 
      * @param v2
      *            ����
      * 
      * @return ���������Ļ�
      * 
      */

     public static double mul(double v1, double v2) {

         BigDecimal b1 = new BigDecimal(Double.toString(v1));

         BigDecimal b2 = new BigDecimal(Double.toString(v2));

         return b1.multiply(b2).doubleValue();

     }
     
     public static String mul(String v1, String v2) {

         BigDecimal b1 = new BigDecimal(v1);

         BigDecimal b2 = new BigDecimal(v2);

         return b1.multiply(b2).toString();

     }

     /**
      * 
      * �ṩ����ԣ���ȷ�ĳ������㣬�����������������ʱ����ȷ��
      * 
      * С�����Ժ�10λ���Ժ�������������롣

      * 
      * @param v1
      *            ������

      * 
      * @param v2
      *            ����
      * 
      * @return ������������
      * 
      */

     public static double div(double v1, double v2) {

         return div(v1, v2, DEF_DIV_SCALE);

     }

     /**
      * 
      * �ṩ����ԣ���ȷ�ĳ������㡣�����������������ʱ����scale����ָ

      * 
      * �����ȣ��Ժ�������������롣

      * 
      * @param v1
      *            ������

      * 
      * @param v2
      *            ����
      * 
      * @param scale
      *            ��ʾ��ʾ��Ҫ��ȷ��С�����Ժ�λ��

      * 
      * @return ������������
      * 
      */

     public static double div(double v1, double v2, int scale) {

         if (scale < 0) {

             throw new IllegalArgumentException(
                     "The scale must be a positive integer or zero");

         }

         BigDecimal b1 = new BigDecimal(Double.toString(v1));

         BigDecimal b2 = new BigDecimal(Double.toString(v2));

         return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();

     }
     
     public static String div(String v1, String v2, int scale) {

         if (scale < 0) {

             throw new IllegalArgumentException(
                     "The scale must be a positive integer or zero");

         }

         BigDecimal b1 = new BigDecimal(v1);

         BigDecimal b2 = new BigDecimal(v2);

         return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).toString();

     }

     /**
      * 
      * �ṩ��ȷ��С��λ�������봦��

      * 
      * @param v
      *            ��Ҫ�������������
      * 
      * @param scale
      *            С���������λ
      * 
      * @return ���������Ľ��
      * 
      */

     public static double round(double v, int scale) {

         if (scale < 0) {

             throw new IllegalArgumentException(
                     "The scale must be a positive integer or zero");

         }

         BigDecimal b = new BigDecimal(Double.toString(v));

         BigDecimal one = new BigDecimal("1");

         return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();

     }
     /**
      * �����ָ�ʽ������ַ���

      * @param formatStr ��ʽ��

      * @param num
      * @return
      */
     public final static String formatString(String formatStr, int num) {
         DecimalFormat df = new DecimalFormat(formatStr);
         return df.format(num);
     }
     /**
      * ��double��ʽ������ַ���
      * @param formatStr ��ʽ�� ��1.234 ��ʽ��Ϊ"#0.00" 
      * @param num
      * @return
      */
     public final static String formatString(String formatStr, double decimal) {
    	 NumberFormat format = new DecimalFormat(formatStr);
         return format.format(decimal);
     }
     /**
      * ʵ��decode�����Ĺ���

      * @param str(����,ֵ1,����ֵ1,ֵ2,����ֵ2,...ֵn,����ֵn,ȱʡֵ)
      * @return
      */
     public final static String decode(String str){
    	 String[] strGroup = str.split(",");
    	 String strTerm = strGroup[0];
    	 if(strGroup.length % 2 != 0){
    		 return "str��ʽ����ȷ������֤";
    	 }
    	 for(int i=1;i<=strGroup.length-3;i+=2){
    		 if(strTerm.equals(strGroup[i])){
    			 return strGroup[i+1];
    		 }
    	 }
    	 return strGroup[strGroup.length-1];  //����ȱʡֵ

     }
    /**
     * @author HANS
     * @param currentDate ָ������
     * @return ����ָ�������·ݵ����һ�������
     */
    @SuppressWarnings("deprecation")
	public final static Date lastDayOfMonth(Date currentDate) {
    	Calendar calendar = Calendar.getInstance();
    	calendar.setTime(currentDate);
    	final int lastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    	Date lastDate = calendar.getTime();
    	lastDate.setDate(lastDay);
    	return lastDate;
    }
    /**
     * @author HANS
     * @param currentDate ָ������
     * @return ����ָ��������ݵ����һ�������
     */
    public final static Date lastDayOfYear(Date currentDate) {
    	Calendar calendar = Calendar.getInstance();
    	calendar.setTime(currentDate);
    	calendar.set(Calendar.MONTH, 11);
    	calendar.set(Calendar.DAY_OF_MONTH, 31);
    	return calendar.getTime();
    }
    
    /**
     * ��һ���ַ���ͳһ��ʽ��

     * @author MJW
     * @param time
     * @return
     * @throws ParseException
     */
    public final static Date formatCSTDateString(String time) throws ParseException{
		Date dt = null;		
		SimpleDateFormat sdfCST = new SimpleDateFormat("EEE MMM dd hh:mm:ss zzz yyyy", Locale.ENGLISH);
		SimpleDateFormat sdfYYYY = new SimpleDateFormat("yyyy-MM-dd");
		if (time.toUpperCase().indexOf("CST") !=-1){
			Date date = sdfCST.parse(time);				 
			String ctime = sdfYYYY.format(date); 		
			dt = sdfYYYY.parse(ctime);
		}else {
			dt = sdfYYYY.parse(time);
		}
		return dt; 
	}
    /**
     * ���ڼ���
     * @param date
     * @param month
     * @return
     */
    public static Date getMonthAddInt(String date, int month)
    {
    	Calendar calendar = Calendar.getInstance();   
		try {
			calendar.setTime(BaseUtil.YMD.parse(date));
		} catch (ParseException e) {
		}
		calendar.add(Calendar.MONTH, month);
		
		return calendar.getTime();
    }
    
    /**
     * ���ݳ������ڼ�������
     * @param birthDay
     * @return
     * @throws Exception
     */
    public static int getAge(Date birthDay) throws Exception {
        Calendar cal = Calendar.getInstance();

        if (cal.before(birthDay)) {
            throw new IllegalArgumentException(
                "The birthDay is before Now.It's unbelievable!");
        }

        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH);
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
        cal.setTime(birthDay);

        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH);
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

        int age = yearNow - yearBirth;

        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                //monthNow==monthBirth
                if (dayOfMonthNow < dayOfMonthBirth) {
                    age--;
                } else {
                    //do nothing
                }
            } else {
                //monthNow>monthBirth
                age--;
            }
        } else {
            //monthNow<monthBirth
            //donothing
        }

        return age;
    }
    /**
	* �ж��Ƿ�Ϊ��
	* @param str
	* @return
	*/
	public static boolean isEmpty(String str) {
		if (str == null || "".equals(str.trim())) {
			return true;
		} else {
			return false;
		}
	}
	/**
	 * ������ڸ�ʽ
	 * @param param
	 * @return
	 */
	public static boolean checkDateYYYYMMDD(String param) {
		boolean bool = true;
		if (param == null || "".equals(param.trim())) {
			return false;
		}
		try {
			YMD_FORMAT.parse(param);
		} catch (ParseException e) {
			bool = false;
		}
		return bool;
	}
}
