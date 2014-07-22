package com.afunms.application.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.afunms.common.util.DBManager;
import com.afunms.config.model.CompCheckResultModel;
import com.afunms.config.model.DetailCompRule;
public class CompareRuleHelper {

	public static void main(String[] args) {
		CompareRuleHelper bc = new CompareRuleHelper();
		File f = new File("D:/10.10.117.176_20110718-10-42cfg.cfg");
		boolean b = false;
		
		String[] compareWords = {
				"service timestamps log datetime"
				};
//		String[] compareWords = {" ip address 200.10.11.255 255.255.255.255","service timestamps debug uptime","no service password-encryption","hostname 2621"};
//		String[] compareWords = {" ip address 200.10.11.255 255.255.255.255a","service timestamps debug uptime","no service password-encryptiona","hostname 2621"};
		int[] flag = {-1};
		//b = bc.contentSimpleWords(f, compareWords, flag);
boolean flags=bc.isExistFile(f, "", "interface Ethernet1/0/2", " level fff", 1);
		System.out.println(flags+"sss");
		
//		List<String> lines = new ArrayList<String>();
//		lines.add("(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])");
//		lines.add("username\\s.+\\sprivilege 15 password 0 liwei");
//		b = bc.contentSimpleLines(f, lines, 4);
//		System.out.println(b);
	}

	/**
	 * @param args
	 * filename�Ǵ�����ļ�����
	 * compareWords����Ҫ�Ƚϵ��ַ� �ж�����ļ����Ƿ������ǰ���ַ�
	 */
	public boolean contentSimpleWords(File filename, String compareWords) {
		if (null == filename || null == compareWords || "".equals(compareWords)) {
			return false;
		}

		FileReader fr = null;
		BufferedReader br = null;
		boolean flag = false;
		try {
			fr = new FileReader(filename);
			br = new BufferedReader(fr);
			String lineStr = "";
			Pattern p = Pattern.compile(compareWords);
			while (null != (lineStr = br.readLine())) {
				Matcher m = p.matcher(lineStr);
				if (m.find()) {
					flag = true;
					break;
				}
			}
			return flag;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			close(br, fr);
		}
		return false;
	}
	/**
	 * 
	 * @param filename
	 * @param compareWords
	 * @param begin
	 * @param end
	 * @return
	 */

	public boolean customFirstWords(File filename, String compareWords,String begin,String end) {
		if (null == filename || null == compareWords || "".equals(compareWords)) {
			return false;
		}
		
		FileReader fr = null;
		BufferedReader br = null;
		boolean flag = false;
		try {
			fr = new FileReader(filename);
			br = new BufferedReader(fr);
			String lineStr = "";
			Pattern p = Pattern.compile(begin);
			while (null != (lineStr = br.readLine())) {
				Matcher m = p.matcher(lineStr);
				if (m.find()) {
					break;
				}	
			}	
			p = Pattern.compile(compareWords);
			while (null != (lineStr = br.readLine())) {
				Matcher m = p.matcher(lineStr);
				if (m.find()) {
					flag = true;
					break;
				}
			}
			p = Pattern.compile(end);
			boolean endFlag=false;
			while (null != (lineStr = br.readLine())) {
				Matcher m = p.matcher(lineStr);
				if (m.find()) {
					endFlag = true;
					break;
				}
			}
		boolean realFlag=flag&endFlag;	
			return realFlag;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			close(br, fr);
		}
		return false;
	}
	
	/**
	 * �Ƚ��ļ��Ƿ��������
	 * @param filename
	 * @param begin
	 * @param end
	 * @param extra
	 * @param isContain
	 * @return
	 */
	public boolean isExistFile(File filename, String begin,String end,String extra,int isContain) {
		if (null == begin||begin.equals("null"))
			begin="";
		if (null == end||end.equals("null")) {
			end="";
		}
		if (null == extra||extra.equals("null")) {
			extra="";
		}
		
		FileReader fr = null;
		BufferedReader br = null;
		boolean flag = false;
		try {
			fr = new FileReader(filename);
			br = new BufferedReader(fr);
			String lineStr = "";
			Pattern p = Pattern.compile(begin);
			while (null != (lineStr = br.readLine())) {
				Matcher m = p.matcher(lineStr);
				if (m.find()) {
					//flag = true;
					if(isContain==-1){
					 p = Pattern.compile(end);
					 while (null != (lineStr = br.readLine())) {
						 m = p.matcher(lineStr);
						 if (m.find()) {
							flag=true;
							 break;
						}
						
					 }
					}else if (isContain==0) {
						p = Pattern.compile(extra);

						// p = Pattern.compile(end);
						 while (null != (lineStr = br.readLine())) {
							 m = p.matcher(lineStr);
							 if (m.find()) {
								flag=false;
								break;
								
							}
							 if(flag){
							 p = Pattern.compile(end);
							 while (null != (lineStr = br.readLine())) {
								 m = p.matcher(lineStr);
								 if (m.find()) {
									flag=true;
									 break;
								}
								
							 }
							 }
						 }
						
					}else if (isContain==1) {

						p = Pattern.compile(extra);

						// p = Pattern.compile(end);
						 while (null != (lineStr = br.readLine())) {
							 m = p.matcher(lineStr);
							 if (m.find()) {
								flag=true;
								 p = Pattern.compile(end);
								 while (null != (lineStr = br.readLine())) {
									 m = p.matcher(lineStr);
									 if (m.find()) {
										flag=true;
										 break;
									}
									
								 }
								 break;
							}
							
						 }
						
					
					}
					break;
				}
				
			}
			return flag;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			close(br, fr);
		}
		return false;
	}
	
	/**
	 * lines���ǰ�����һ�л��߶��е���䡣 
	 * ��flag=0,���ж��ļ����Ƿ������Щlines����κ�һ�С�
	 * ��flag=1,���ж��ļ����Ƿ������Щlines��������С�������Щ�ж�����һ����ֵġ�
	 * ��flag=2�����ж��ļ��Ƿ���������У����ǲ���һ�����
	 * @param filename
	 * @param lines
	 * @param flag
	 * @return
	 */
	public boolean contentSimpleWords(File filename, String[] compareWords, int[] flag){
		if (null == filename || null == compareWords || null == flag) {
			return false;
		}
		if(compareWords.length != flag.length)return false;
		
		try {
			boolean bFlag = contentSimpleWords(filename,compareWords[0]);
			for (int i = 1; i < flag.length; i++) {
				boolean cFlag = contentSimpleWords(filename, compareWords[i]);
				if (flag[i] == 0) {
					bFlag = bFlag || cFlag;
				}else if (flag[i] == 1) {
					bFlag = bFlag && cFlag;
				}
				if(bFlag && i+1 < flag.length && flag[i+1] == 0){//bFlagΪtrueʱ����һ��flagΪ�����㣬��ֱ������
						i++;
				}
			}
			return bFlag;
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return false;
	}
	/**
	 * 
	 * @param filename
	 * @param compareWords
	 * @param flag
	 * @param flag1 �Ƿ����
	 * @param dbManager
	 * @param model
	 * @return
	 */
	public boolean contentSimpleWords(File filename, String[] compareWords, int[] flag, boolean[] flag1,DBManager dbManager,CompCheckResultModel model){
		if (null == filename || null == compareWords || null == flag || null == flag1) {
			return false;
		}
		if(compareWords.length != flag.length || flag.length != flag1.length)return false;
		StringBuffer sql=null;
		int isViolation=0;
		int isContain=0;
		try {
			boolean bFlag = contentSimpleWords(filename,compareWords[0]);
			if(!flag1[0])bFlag = !bFlag; 
			if (bFlag) {
			isViolation=1;	//�Ϲ�
			}else {
			isViolation=0;  //Υ��
			}
			if (flag1[0]) {
				isContain=1;
			}else {
				isContain=0;
			}
			sql=new StringBuffer();
			sql.append("insert into nms_comp_check_rule(STRATEGY_ID,GROUP_ID,RULE_ID,IP,ISVIOLATION,RELATION,ISCONTAIN,CONTENT) values(");
			sql.append(model.getStrategyId());
			sql.append(",");
			sql.append(model.getGroupId());
			sql.append(",");
			sql.append(model.getRuleId());
			sql.append(",'");
			sql.append(model.getIp());
			sql.append("',");
			sql.append(isViolation);
			sql.append(",");
			sql.append("-1");
			sql.append(",");
			sql.append(isContain);
			sql.append(",'");
			sql.append(compareWords[0]);
			sql.append("')");
			dbManager.addBatch(sql.toString());
			for (int i = 1; i < flag.length; i++) {
				boolean cFlag = contentSimpleWords(filename, compareWords[i]);
				if (!flag1[i]) {
					cFlag = !cFlag;
				}
				if (flag[i] == 0) {
					bFlag = bFlag || cFlag;
				}else if (flag[i] == 1) {
					bFlag = bFlag && cFlag;
				}
//				if(bFlag && i+1 < flag.length && flag[i+1] == 0){//bFlagΪtrueʱ����һ��flagΪ�����㣬��ֱ������
//						i++;
//				}
				if (cFlag) {
					isViolation=1;	//�Ϲ�
					}else {
					isViolation=0;  //Υ��
					}
					if (flag1[i]) {
						isContain=1;
					}else {
						isContain=0;
					}
				sql=new StringBuffer();
				sql.append("insert into nms_comp_check_rule(STRATEGY_ID,GROUP_ID,RULE_ID,IP,ISVIOLATION,RELATION,ISCONTAIN,CONTENT) values(");
				sql.append(model.getStrategyId());
				sql.append(",");
				sql.append(model.getGroupId());
				sql.append(",");
				sql.append(model.getRuleId());
				sql.append(",'");
				sql.append(model.getIp());
				sql.append("',");
				sql.append(isViolation);
				sql.append(",");
				sql.append(flag[i]);
				sql.append(",");
				sql.append(isContain);
				sql.append(",'");
				sql.append(compareWords[i]);
				sql.append("')");
				dbManager.addBatch(sql.toString());
			}
			return bFlag;
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return false;		
	}
	public boolean contentCustomWords(File filename, String[] compareWords, int[] flag, boolean[] flag1,DetailCompRule detailCompRule,DBManager dbManager,CompCheckResultModel model){
		if (null == filename || null == compareWords || null == flag || null == flag1) {
			return false;
		}
		if(compareWords.length != flag.length || flag.length != flag1.length)return false;
		StringBuffer sql=null;
		int isViolation=0;
		int isContain=0;
		String begin=detailCompRule.getBeginBlock();
		String end=detailCompRule.getEndBlock();
		String extra=detailCompRule.getExtraBlock();
		int IsExtraContain=detailCompRule.getIsExtraContain();
		boolean isExist=this.isExistFile(filename,begin ,end, extra,IsExtraContain);
    if (isExist) {
	 isViolation=1;
      }else {
		isViolation=0;
	}
		String block="";
		String relation="";
		for (int j = 0; j < 3; j++) {
			if (j==0) {
				block=begin;
				relation="-2";
			}else if (j==1) {
				block=end;
				relation="-3";
			}else if(j==2){
				block=extra;
				relation="-4";
				isContain=detailCompRule.getIsExtraContain();
			}
			sql=new StringBuffer();
			sql.append("insert into nms_comp_check_rule(STRATEGY_ID,GROUP_ID,RULE_ID,IP,ISVIOLATION,RELATION,ISCONTAIN,CONTENT) values(");
			sql.append(model.getStrategyId());
			sql.append(",");
			sql.append(model.getGroupId());
			sql.append(",");
			sql.append(model.getRuleId());
			sql.append(",'");
			sql.append(model.getIp());
			sql.append("',");
			sql.append(isViolation);//Υ������
			sql.append(",");
			sql.append(relation);//���ӿ�
			
			sql.append(",");
			sql.append(isContain);
			sql.append(",'");
			sql.append(block);
			sql.append("')");
			dbManager.addBatch(sql.toString());
		}
	
		if (isExist) {
			try {
				
				boolean bFlag = customFirstWords(filename,compareWords[0],begin,end);
				if(!flag1[0])bFlag = !bFlag; 
				if (bFlag) {
					isViolation=1;	//�Ϲ�
				}else {
					isViolation=0;  //Υ��
				}
				if (flag1[0]) {
					isContain=1;
				}else {
					isContain=0;
				}
				sql=new StringBuffer();
				sql.append("insert into nms_comp_check_rule(STRATEGY_ID,GROUP_ID,RULE_ID,IP,ISVIOLATION,RELATION,ISCONTAIN,CONTENT) values(");
				sql.append(model.getStrategyId());
				sql.append(",");
				sql.append(model.getGroupId());
				sql.append(",");
				sql.append(model.getRuleId());
				sql.append(",'");
				sql.append(model.getIp());
				sql.append("',");
				sql.append(isViolation);
				sql.append(",");
				sql.append("-1");
				sql.append(",");
				sql.append(isContain);
				sql.append(",'");
				sql.append(compareWords[0]);
				sql.append("')");
				dbManager.addBatch(sql.toString());
				for (int i = 1; i < flag.length; i++) {
					boolean cFlag = customFirstWords(filename, compareWords[i],begin,end);
					if (!flag1[i]) {
						cFlag = !cFlag;
					}
					if (flag[i] == 0) {
						bFlag = bFlag || cFlag;
					}else if (flag[i] == 1) {
						bFlag = bFlag && cFlag;
					}
//					if(bFlag && i+1 < flag.length && flag[i+1] == 0){//bFlagΪtrueʱ����һ��flagΪ�����㣬��ֱ������
//							i++;
//					}
					if (cFlag) {
						isViolation=1;	//�Ϲ�
					}else {
						isViolation=0;  //Υ��
					}
					if (flag1[i]) {
						isContain=1;
					}else {
						isContain=0;
					}
					sql=new StringBuffer();
					sql.append("insert into nms_comp_check_rule(STRATEGY_ID,GROUP_ID,RULE_ID,IP,ISVIOLATION,RELATION,ISCONTAIN,CONTENT) values(");
					sql.append(model.getStrategyId());
					sql.append(",");
					sql.append(model.getGroupId());
					sql.append(",");
					sql.append(model.getRuleId());
					sql.append(",'");
					sql.append(model.getIp());
					sql.append("',");
					sql.append(isViolation);
					sql.append(",");
					sql.append(flag[i]);
					sql.append(",");
					sql.append(isContain);
					sql.append(",'");
					sql.append(compareWords[i]);
					sql.append("')");
					dbManager.addBatch(sql.toString());
				}
				return bFlag;
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}

		return false;		
	}
	/**
	 * ��һ����contentSimpleLines(File filename, List<String> lines, int flag)
	 * �޸ĵĲ�����flag�Ĳ�����0.���������� 1.�������κ��� 2.Ӧ�ð������� 3.����������
	 * @param filename
	 * @param lines
	 * @param flag
	 * @return
	 */
	public boolean contentSimpleLines(File filename, List<String> lines, int flag,DBManager dbManager,CompCheckResultModel model) {
		if (null == filename || null == lines || (flag > 3 && flag < 0)) {
			return false;
		}

		FileReader fr = null;
		BufferedReader br = null;
		boolean bFlag = false;
		boolean realFlag=true;
		String lineStr = "";
         int isViolation=0;
         StringBuffer sql=null;
		try {
			fr = new FileReader(filename);
			br = new BufferedReader(fr);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		
		if(flag == 0){//0.����������
			try {
				for (int i = 0; i < lines.size(); i++) {
					br.mark(10000);
					while (null != (lineStr = br.readLine())) {
						if (lineStr.length() == 0)
							continue;
						
						bFlag = lineStr.matches(lines.get(i));
						if (bFlag) {
							br.reset();
							break;
						}
					}
					if(!bFlag){
						realFlag= false;
						br.reset();
						isViolation=0;//���Ϲ�
					}else {
						isViolation=1;//�Ϲ�
					}
					sql=new StringBuffer();
					sql.append("insert into nms_comp_check_rule(STRATEGY_ID,GROUP_ID,RULE_ID,IP,ISVIOLATION,RELATION,ISCONTAIN,CONTENT) values(");
					sql.append(model.getStrategyId());
					sql.append(",");
					sql.append(model.getGroupId());
					sql.append(",");
					sql.append(model.getRuleId());
					sql.append(",'");
					sql.append(model.getIp());
					sql.append("',");
					sql.append(isViolation);
					sql.append(",");
					sql.append("-1");
					sql.append(",");
					sql.append(flag);
					sql.append(",'");
					sql.append(lines.get(i));
					sql.append("')");
					dbManager.addBatch(sql.toString());
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				close(br, fr);
			}
			return realFlag;
		}else if(flag == 1){//1.�������κ���
			try {
				for (int i = 0; i < lines.size(); i++) {
					br.mark(10000);
				while (null != (lineStr = br.readLine())) {
					if (lineStr.length() == 0)
						continue;

					
						bFlag = lineStr.matches((String) lines.get(i));//true:�ҵ�����������
						if (bFlag) {
							realFlag= false;
							isViolation=0;
							br.reset();
							break;
						}else {
							isViolation=1;//�Ϲ�
						}
						
					}
				if(!bFlag){
					br.reset();
					isViolation=1;
				}else {
					isViolation=0;
				}
				sql=new StringBuffer();
				sql.append("insert into nms_comp_check_rule(STRATEGY_ID,GROUP_ID,RULE_ID,IP,ISVIOLATION,RELATION,ISCONTAIN,CONTENT) values(");
				sql.append(model.getStrategyId());
				sql.append(",");
				sql.append(model.getGroupId());
				sql.append(",");
				sql.append(model.getRuleId());
				sql.append(",'");
				sql.append(model.getIp());
				sql.append("',");
				sql.append(isViolation);
				sql.append(",");
				sql.append("-1");
				sql.append(",");
				sql.append(flag);
				sql.append(",'");
				sql.append(lines.get(i));
				sql.append("')");
				dbManager.addBatch(sql.toString());
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				close(br, fr);
			}
			return realFlag;			
		}else if(flag == 2){//2.Ӧ�ð�������
			StringBuffer sb1 = new StringBuffer();
			
			if (lines.size() >= 1) {
				Iterator<String> iter = lines.iterator();
				while (iter.hasNext()) {
					sb1.append(iter.next()).append("\r\n");
				}
			}
			if (sb1.length() == 0)
				return false;

			try {
				StringBuffer sb2 = new StringBuffer();
				while (null != (lineStr = br.readLine())) {
					sb2.append(lineStr).append("\r\n");
				}
				Pattern p = Pattern.compile(sb1.toString());

				Matcher m = p.matcher(sb2);
				if (m.find()) {
					bFlag = true;
					isViolation=1;//�Ϲ�
				}else {
					isViolation=0;//���Ϲ�
				}
				if (lines.size() >= 1) {
					Iterator<String> iter = lines.iterator();
					
					while (iter.hasNext()) {
						sql=new StringBuffer();
						sql.append("insert into nms_comp_check_rule(STRATEGY_ID,GROUP_ID,RULE_ID,IP,ISVIOLATION,RELATION,ISCONTAIN,CONTENT) values(");
						sql.append(model.getStrategyId());
						sql.append(",");
						sql.append(model.getGroupId());
						sql.append(",");
						sql.append(model.getRuleId());
						sql.append(",'");
						sql.append(model.getIp());
						sql.append("',");
						sql.append(isViolation);
						sql.append(",");
						sql.append("-1");
						sql.append(",");
						sql.append(flag);
						sql.append(",'");
						sql.append(iter.next());
						sql.append("')");
						dbManager.addBatch(sql.toString());
					}
				}
				return bFlag;
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				close(br, fr);
			}
			
			return bFlag;
		}else{// 3.����������
			StringBuffer sb1 = new StringBuffer();
			if (lines.size() >= 1) {
				Iterator<String> iter = lines.iterator();
				while (iter.hasNext()) {
					sb1.append(iter.next()).append("\r\n");
				}
			}
			if (sb1.length() == 0)
				return false;

			try {
				StringBuffer sb2 = new StringBuffer();
				while (null != (lineStr = br.readLine())) {
					sb2.append(lineStr).append("\r\n");
				}
				Pattern p = Pattern.compile(sb1.toString());

				Matcher m = p.matcher(sb2);
				if (!m.find()) {
					bFlag = true;
					isViolation=1;
				}else {
					isViolation=0;
				}
				if (lines.size() >= 1) {
					Iterator<String> iter = lines.iterator();
					
					while (iter.hasNext()) {
						sql=new StringBuffer();
						sql.append("insert into nms_comp_check_rule(STRATEGY_ID,GROUP_ID,RULE_ID,IP,ISVIOLATION,RELATION,ISCONTAIN,CONTENT) values(");
						sql.append(model.getStrategyId());
						sql.append(",");
						sql.append(model.getGroupId());
						sql.append(",");
						sql.append(model.getRuleId());
						sql.append(",'");
						sql.append(model.getIp());
						sql.append("',");
						sql.append(isViolation);
						sql.append(",");
						sql.append("-1");
						sql.append(",");
						sql.append(flag);
						sql.append(",'");
						sql.append(iter.next());
						sql.append("')");
						dbManager.addBatch(sql.toString());
					}
				}
				return bFlag;
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				close(br, fr);
			}
			return bFlag;
		}
	}
	
	/**
	 * �ر���Դ
	 * @param br
	 * @param fr
	 */
	void close(BufferedReader br, FileReader fr) {
		if (br != null) {
			try {
				br.close();
				br = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (fr != null) {
			try {
				fr.close();
				fr = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}}
