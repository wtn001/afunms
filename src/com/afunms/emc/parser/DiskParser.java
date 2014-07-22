package com.afunms.emc.parser;

import java.beans.PropertyDescriptor;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.afunms.emc.model.Disk;

public class DiskParser {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
//		String fileName = System.getProperty("user.dir")+File.separator+"log\\getdisk.log";
		String fileName = "C:/Users/Administrator/Desktop/emc´æ´¢/getdisk.log";
		FileReader fr = new FileReader(fileName);
		BufferedReader br = new BufferedReader(fr);
		String line = "";
		StringBuffer sb = new StringBuffer();
		List<Disk> list = new ArrayList<Disk>();
		Disk disk=null;
		while (null != (line = br.readLine())) {
			sb.append(line).append("\r\n");
		}
		if (sb.length()>0) {
			 list = (List<Disk>)parse(sb.toString());
		}
		int num =0;
		int num1= 0;
		System.out.println(sb.toString());
		for(int i=0;i<list.size();i++){
			 disk = list.get(i);
			 HashMap type_map = new HashMap();
			 HashMap built_map = new HashMap();
			 HashMap bound_map = new HashMap();
			 List key = new ArrayList();
			 List value = new ArrayList();
			 List built_value = new ArrayList();
			 List bound_value = new ArrayList();
//			 System.out.println(disk.getNumberofLuns());
//			 if(disk.getNumberofLuns() != null ){
//			 if(Integer.parseInt(disk.getNumberofLuns())>1){
//				 String[] type = disk.getType().split(" ");
//				  String[] number = disk.getLun().split(" ");
//				  String[] build = disk.getPrctRebuilt().split(" ");
//				  String[] bound = disk.getPrctBound().split(" ");
//				  for(int j=0;j<type.length;j++)
//				  {
//					  if(j%2==0){
//						  num=j;
//						  String[] flag1 = type[num].split(":");
//						  key.add(flag1[0]);
//						  System.out.print(flag1[0]+"--------");
//					  }else if(j%2 == 1){
//						  num1=j;
//						  value.add(type[num1]);
//						  built_value.add(type[num1]);
//						  bound_value.add(type[num1]);
//						  System.out.print(type[num1]+"--------");
//						  System.out.print(build[num1]+"--------");
//						  System.out.print(bound[num1]+"--------");
//						  System.out.println("--------");
//					  }
//				  }
//			  
//			 }
//			 }
		}
		
		
	}
	
	public static List<Disk> parse(String str) {
		Pattern pattern = Pattern.compile("Bus \\d+ Enclosure \\d+  Disk \\d+");
		Matcher matcher = pattern.matcher(str);
		List<Integer> indexs = new ArrayList<Integer>();
		List<Disk> disksList = new ArrayList<Disk>();
		
		while(matcher.find()){
			int start = matcher.start();
			indexs.add(start);
			Disk disk = new Disk();
			disk.setName(matcher.group().trim());
			disksList.add(disk);
		}

		for (int i = 0; i < indexs.size(); i++) {
			Disk disk = disksList.get(i);
			String subStr = "";
			if (i< indexs.size()-1) {
				subStr = str.substring(indexs.get(i), indexs.get(i+1));
			}else{
				subStr = str.substring(indexs.get(i));
			}

			toParse("Bus.*\\r\\n",subStr,disk,"did");
			toParse("Product Id:.*\\r\\n",subStr,disk,"rid");
			toParse("Product Revision:.*\\r\\n",subStr,disk,"revision");
			toParse("Lun:.*\\r\\n",subStr,disk,"lun");
			toParse("Type:.*\\r\\n",subStr,disk,"type");
			toParse("State:.*\\r\\n",subStr,disk,"state");
			toParse("Hot Spare:.*\\r\\n",subStr,disk,"hotSpare");
			toParse("Prct Rebuilt:.*\\r\\n",subStr,disk,"prctRebuilt");
			toParse("Prct Bound:.*\\r\\n",subStr,disk,"prctBound");
			toParse("Serial Number:.*\\r\\n",subStr,disk,"serialNumber");
			toParse("Capacity:.*\\r\\n",subStr,disk,"capacity");
			toParse("Hard Read Errors:.*\\r\\n",subStr,disk,"hardReadErrors");
			toParse("Hard Write Errors:.*\\r\\n",subStr,disk,"hardWriteErrors");
			toParse("Soft Read Errors:.*\\r\\n",subStr,disk,"softReadErrors");
			toParse("Soft Write Errors:.*\\r\\n",subStr,disk,"softWriteErrors");
			toParse("Number of Reads:.*\\r\\n",subStr,disk,"numberofReads");
			toParse("Number of Writes:.*\\r\\n",subStr,disk,"numberofWrites");
			toParse("Number of Luns:.*\\r\\n",subStr,disk,"numberofLuns");
			toParse("Raid Group ID:.*\\r\\n",subStr,disk,"raidGroupID");
			toParse("Kbytes Read:.*\\r\\n",subStr,disk,"kbytesRead");
			toParse("Kbytes Written:.*\\r\\n",subStr,disk,"kbytesWritten");
			toParse("Drive Type:.*\\r\\n",subStr,disk,"driveType");
			toParse("Idle Ticks:.*\\r\\n",subStr,disk,"idleTicks");
			toParse("Busy Ticks:.*\\r\\n",subStr,disk,"busyTicks");
			toParse("Current Speed:.*\\r\\n",subStr,disk,"currentSpeed");
			toParse("Maximum Speed:.*\\r\\n",subStr,disk,"maximumSpeed");
		}
		return disksList;
	}
	
	private static void toParse(String patternStr,String subStr,Disk disk,Method[] methods,String field) throws Exception{
		Pattern pattern = Pattern.compile(patternStr);
		Matcher matcher = pattern.matcher(subStr);
		if (matcher.find()) {
			String value = matcher.group().substring(patternStr.length()).trim();
			for (Method method : methods) {
				if (method.getName().equals("set"+field)) {
					method.invoke(disk, value);
				}
			}
		}
	}
	
	private static void toParse(String patternStr,String subStr,Disk disk,String field) {
		try{
			PropertyDescriptor pd = new PropertyDescriptor(field,Disk.class);
			Method setMethod = pd.getWriteMethod();
			Pattern pattern = Pattern.compile(patternStr);
			Matcher matcher = pattern.matcher(subStr);
			if (matcher.find()) {
				String value = matcher.group();
				value = value.substring(value.indexOf(":")+1).trim();
				setMethod.invoke(disk, value);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
