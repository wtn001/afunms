package com.afunms.emc.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.afunms.emc.model.Lun;

public class LunParser {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
//		String fileName = System.getProperty("user.dir")+File.separator+"log\\getlun.log";
		String fileName = "C:/Users/Administrator/Desktop/emc´æ´¢/lun.log";
		FileReader fr = new FileReader(fileName);
		BufferedReader br = new BufferedReader(fr);
		String line = "";
		List<Lun> list = new ArrayList<Lun>();
		Lun lun = null;
		StringBuffer sb = new StringBuffer();
		while (null != (line = br.readLine())) {
			sb.append(line).append("\r\n");
		}
		if (sb.length()>0) {
			list = parse(sb.toString());
		}
//		System.out.println(sb.toString());
		System.out.println(list.size());
		for(int i=0;i<list.size();i++){
			lun = list.get(i);
			System.out.println(lun.getId());
			System.out.println(lun.getName());
			System.out.println(lun.getRAIDType());
			System.out.println(lun.getRAIDGroupID());
			System.out.println(lun.getDisksList());
			System.out.println(lun.getCurrentOwner());
			System.out.println(lun.getDefaultOwner());
			System.out.println(lun.getWritecache());
			System.out.println(lun.getReadcache());
			System.out.println(lun.getPrctRebuilt());
			System.out.println(lun.getPrctBound());
			System.out.println(lun.getLUNCapacity());
			System.out.println(lun.getState());
			System.out.println(lun.getTotalHardErrors());
			System.out.println(lun.getTotalSoftErrors());
			System.out.println(lun.getTotalQueueLength());
			System.out.println("------------------");
		}
		
	}

	public static List<Lun> parse(String str){
		List<Lun> list = new ArrayList<Lun>();
		
		String regex = "";
		System.out.println(str.contains("Histogram overflows"));
		if(!str.contains("Histogram overflows")){
		     regex = "(Total Hard Errors:\\s*.*\\r\\n)"
					+ "(Total Soft Errors:\\s*.*\\r\\n)"
					+ "(Total Queue Length:\\s*.*\\r\\n)"
					+ "(Name\\s*.*\\r\\n)"
					+ "Minimum latency reads N/A(\\s*\\r\\n)+"
					+ "(RAID Type:\\s*.*\\r\\n)"
					+ "(RAIDGroup ID:\\s*.*\\r\\n)"
					+ "(State:\\s*.*\\r\\n)"
					+ "Stripe Crossing:\\s*.*\\r\\n"
					+ "Element Size:\\s*.*\\r\\n"
					+ "(Current owner:\\s*.*\\r\\n)"
					+ "Offset:\\s*.*\\r\\n"
					+ "Auto-trespass:\\s*.*\\r\\n"
					+ "Auto-assign:\\s*.*\\r\\n"
					+ "(Write cache:\\s*.*\\r\\n)"
					+ "(Read cache:\\s*.*\\r\\n)"
					+ "Idle Threshold:\\s*.*\\r\\n"
					+ "Idle Delay Time:\\s*.*\\r\\n"
					+ "Write Aside Size:\\s*.*\\r\\n"
					+ "(Default Owner:\\s*.*\\r\\n)"
					+ "Rebuild Priority:\\s*.*\\r\\n"
					+ "Verify Priority:\\s*.*\\r\\n"
					+ "Prct Reads Forced Flushed:\\s*.*\\r\\n"
					+ "Prct Writes Forced Flushed:\\s*.*\\r\\n"
					+ "(Prct Rebuilt:\\s*.*\\r\\n)"
					+ "(Prct Bound:\\s*.*\\r\\n)"
					+ "(LUN Capacity.Megabytes.:\\s*.*\\r\\n)"
					+ "LUN Capacity.Blocks.:\\s*.*\\r\\n"
					+ "(UID:\\s*.*\\r\\n(Bus \\d+ Enclosure \\d+  Disk \\d+.*\\r\\n)+)";
		}else{
//			 regex = "(Total Hard Errors:\\s*.*\\r\\n)"
//					+ "(Total Soft Errors:\\s*.*\\r\\n)"
//					+ "(Total Queue Length:\\s*.*\\r\\n)"
//					+ "(Name\\s*.*\\r\\n)"
//					+ "Minimum latency reads N/A(\\s*\\r\\n)+"
//					+ "Read Histogram:(\\s*.*\\r\\n)+"
//					+ "(Read Histogram overflows:\\s*.*\\r\\n)"
//					+ "Write Histogram:(\\s*.*\\r\\n)+"
//					+ "(Write Histogram overflows:\\s*.*\\r\\n)"
//					+ "(Blocks written:\\s*.*\\r\\n)"
//					+ "(Read cache hits:\\s*.*\\r\\n)"
//					+ "(Read cache misses:\\s*.*\\r\\n)"
//					+ "(Prefetched blocks:\\s*.*\\r\\n)"
//					+ "(Unused prefetched blocks:\\s*.*\\r\\n)"
//					+ "(Write cache hits:\\s*.*\\r\\n)"
//					+ "(Forced flushes:\\s*.*\\r\\n)"
//					+ "(Read Hit Ratio:\\s*.*\\r\\n)"
//					+ "(Write Hit Ratio:\\s*.*\\r\\n)"
//					+ "(RAID Type:\\s*.*\\r\\n)"
//					+ "(RAIDGroup ID:\\s*.*\\r\\n)"
//					+ "(State:\\s*.*\\r\\n)"
//					+ "Stripe Crossing:\\s*.*\\r\\n"
//					+ "Element Size:\\s*.*\\r\\n"
//					+ "(Current owner:\\s*.*\\r\\n)"
//					+ "Offset:\\s*.*\\r\\n"
//					+ "Auto-trespass:\\s*.*\\r\\n"
//					+ "Auto-assign:\\s*.*\\r\\n"
//					+ "(Write cache:\\s*.*\\r\\n)"
//					+ "(Read cache:\\s*.*\\r\\n)"
//					+ "Idle Threshold:\\s*.*\\r\\n"
//					+ "Idle Delay Time:\\s*.*\\r\\n"
//					+ "Write Aside Size:\\s*.*\\r\\n"
//					+ "(Default Owner:\\s*.*\\r\\n)"
//					+ "Rebuild Priority:\\s*.*\\r\\n"
//					+ "Verify Priority:\\s*.*\\r\\n"
//					+ "Prct Reads Forced Flushed:\\s*.*\\r\\n"
//					+ "Prct Writes Forced Flushed:\\s*.*\\r\\n"
//					+ "(Prct Rebuilt:\\s*.*\\r\\n)"
//					+ "(Prct Bound:\\s*.*\\r\\n)"
//					+ "(LUN Capacity.Megabytes.:\\s*.*\\r\\n)"
//					+ "LUN Capacity.Blocks.:\\s*.*\\r\\n"
//					+ "(UID:\\s*.*\\r\\n(Bus \\d+ Enclosure \\d+  Disk \\d+.*\\r\\n)+)";
			
			
			regex = "(Total Hard Errors:\\s*.*\\r\\n)"
				+ "(Total Soft Errors:\\s*.*\\r\\n)"
				+ "(Total Queue Length:\\s*.*\\r\\n)"
				+ "(Name\\s*.*\\r\\n)"
				+ "Minimum latency reads N/A(\\r\\n)+"
				+ "(Read Histogram\\[\\d+\\] \\d+\\r\\n)+"
				+ "Read Histogram overflows \\d+(\\r\\n)+"
				+ "(Write Histogram\\[\\d+\\] \\d+\\r\\n)+"
				+ "Write Histogram overflows \\d+(\\r\\n)+"
				+ "Read Requests:\\s*.*\\r\\n"
				+ "Write Requests:\\s*.*\\r\\n"
				+ "Blocks read:\\s*.*\\r\\n"
				+ "Blocks written:\\s*.*\\r\\n"
				+ "Read cache hits:\\s*.*\\r\\n"
				+ "Read cache misses:\\s*.*\\r\\n"
				+ "Prefetched blocks:\\s*.*\\r\\n"
				+ "Unused prefetched blocks:\\s*.*\\r\\n"
				+ "Write cache hits:\\s*.*\\r\\n"
				+ "Forced flushes:\\s*.*\\r\\n"
				+ "Read Hit Ratio:\\s*.*\\r\\n"
				+ "Write Hit Ratio:\\s*.*\\r\\n"
				+ "(RAID Type:\\s*.*\\r\\n)"
				+ "(RAIDGroup ID:\\s*.*\\r\\n)"
				+ "(State:\\s*.*\\r\\n)"
				+ "Stripe Crossing:\\s*.*\\r\\n"
				+ "Element Size:\\s*.*\\r\\n"
				+ "(Current owner:\\s*.*\\r\\n)"
				+ "Offset:\\s*.*\\r\\n"
				+ "Auto-trespass:\\s*.*\\r\\n"
				+ "Auto-assign:\\s*.*\\r\\n"
				+ "(Write cache:\\s*.*\\r\\n)"
				+ "(Read cache:\\s*.*\\r\\n)"
				+ "Idle Threshold:\\s*.*\\r\\n"
				+ "Idle Delay Time:\\s*.*\\r\\n"
				+ "Write Aside Size:\\s*.*\\r\\n"
				+ "(Default Owner:\\s*.*\\r\\n)"
				+ "Rebuild Priority:\\s*.*\\r\\n"
				+ "Verify Priority:\\s*.*\\r\\n"
				+ "Prct Reads Forced Flushed:\\s*.*\\r\\n"
				+ "Prct Writes Forced Flushed:\\s*.*\\r\\n"
				+ "(Prct Rebuilt:\\s*.*\\r\\n)"
				+ "(Prct Bound:\\s*.*\\r\\n)"
				+ "(LUN Capacity.Megabytes.:\\s*.*\\r\\n)"
				+ "LUN Capacity.Blocks.:\\s*.*\\r\\n"
				+ "(UID:\\s*.*\\r\\n(Bus \\d+ Enclosure \\d+  Disk \\d+.*\\r\\n)+)";
			
		}

		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(str);
//		System.out.println(p);
//		System.out.println(m);
		int i = 0;
		if(!str.contains("Histogram overflows")){
		while (m.find()) {
			i++;
			Lun lun = new Lun();
			lun.setTotalHardErrors(Integer.parseInt(m.group(1).substring(m.group(1).indexOf(":")+1).trim()));
			lun.setTotalSoftErrors(Integer.parseInt(m.group(2).substring(m.group(2).indexOf(":")+1).trim()));
			lun.setTotalQueueLength(Integer.parseInt(m.group(3).substring(m.group(3).indexOf(":")+1).trim()));
			lun.setName(m.group(4).substring(4).trim());
			lun.setRAIDType(m.group(6).substring(m.group(6).indexOf(":")+1).trim());
			lun.setRAIDGroupID(m.group(7).substring(m.group(7).indexOf(":")+1).trim());
			lun.setState(m.group(8).substring(m.group(8).indexOf(":")+1).trim());
			lun.setCurrentOwner(m.group(9).substring(m.group(9).indexOf(":")+1).trim());
			lun.setWritecache(m.group(10).substring(m.group(10).indexOf(":")+1).trim());
			lun.setReadcache(m.group(11).substring(m.group(11).indexOf(":")+1).trim());
			lun.setDefaultOwner(m.group(12).substring(m.group(12).indexOf(":")+1).trim());
			lun.setPrctRebuilt(m.group(13).substring(m.group(13).indexOf(":")+1).trim());
			lun.setPrctBound(m.group(14).substring(m.group(14).indexOf(":")+1).trim());
			lun.setLUNCapacity(m.group(15).substring(m.group(15).indexOf(":")+1).trim());
			lun.setDisksList(subParseToList(m.group(16).trim()));
			list.add(lun);
		}
		}else{
			while (m.find()) {
				i++;
				Lun lun = new Lun();
				
//				lun.setTotalHardErrors(Integer.parseInt(m.group(1).substring(m.group(1).indexOf(":")+1).trim()));
//				System.out.println("---->"+Integer.parseInt(m.group(1).substring(m.group(1).indexOf(":")+1).trim()));
//				lun.setTotalSoftErrors(Integer.parseInt(m.group(2).substring(m.group(2).indexOf(":")+1).trim()));
//				System.out.println("---->"+Integer.parseInt(m.group(2).substring(m.group(2).indexOf(":")+1).trim()));
//				lun.setTotalQueueLength(Integer.parseInt(m.group(3).substring(m.group(3).indexOf(":")+1).trim()));
//				System.out.println("---->"+Integer.parseInt(m.group(3).substring(m.group(3).indexOf(":")+1).trim()));
//				lun.setName(m.group(4).substring(4).trim());
//				System.out.println("---->"+m.group(4).substring(4).trim());
//				lun.setRAIDType(m.group(18).substring(m.group(18).indexOf(":")+1).trim());
//				System.out.println("---->"+m.group(18).substring(m.group(18).indexOf(":")+1).trim());
//				lun.setRAIDGroupID(m.group(19).substring(m.group(19).indexOf(":")+1).trim());
//				System.out.println("---->"+m.group(19).substring(m.group(19).indexOf(":")+1).trim());
//				lun.setState(m.group(20).substring(m.group(20).indexOf(":")+1).trim());
//				System.out.println("---->"+m.group(20).substring(m.group(20).indexOf(":")+1).trim());
//				lun.setCurrentOwner(m.group(21).substring(m.group(21).indexOf(":")+1).trim());
//				System.out.println("---->"+m.group(21).substring(m.group(21).indexOf(":")+1).trim());
//				lun.setWritecache(m.group(22).substring(m.group(22).indexOf(":")+1).trim());
//				System.out.println("---->"+m.group(22).substring(m.group(22).indexOf(":")+1).trim());
//				lun.setReadcache(m.group(23).substring(m.group(23).indexOf(":")+1).trim());
//				System.out.println("---->"+m.group(23).substring(m.group(23).indexOf(":")+1).trim());
//				lun.setDefaultOwner(m.group(24).substring(m.group(25).indexOf(":")+1).trim());
//				System.out.println("---->"+m.group(24).substring(m.group(25).indexOf(":")+1).trim());
//				lun.setPrctRebuilt(m.group(25).substring(m.group(25).indexOf(":")+1).trim());
//				System.out.println("---->"+m.group(25).substring(m.group(25).indexOf(":")+1).trim());
//				lun.setPrctBound(m.group(26).substring(m.group(26).indexOf(":")+1).trim());
//				System.out.println("---->"+m.group(26).substring(m.group(26).indexOf(":")+1).trim());
//				lun.setLUNCapacity(m.group(27).substring(m.group(27).indexOf(":")+1).trim());
//				System.out.println("---->"+m.group(27).substring(m.group(27).indexOf(":")+1).trim());
//				lun.setDisksList(subParseToList(m.group(28).trim()));
//				System.out.println("---->"+subParseToList(m.group(28).trim()));
				
				lun.setTotalHardErrors(Integer.parseInt(m.group(1).substring(m.group(1).indexOf(":")+1).trim()));
				lun.setTotalSoftErrors(Integer.parseInt(m.group(2).substring(m.group(2).indexOf(":")+1).trim()));
				lun.setTotalQueueLength(Integer.parseInt(m.group(3).substring(m.group(3).indexOf(":")+1).trim()));
				lun.setName(m.group(4).substring(4).trim());
				lun.setRAIDType(m.group(10).substring(m.group(10).indexOf(":")+1).trim());
				lun.setRAIDGroupID(m.group(11).substring(m.group(11).indexOf(":")+1).trim());
				lun.setState(m.group(12).substring(m.group(12).indexOf(":")+1).trim());
				lun.setCurrentOwner(m.group(13).substring(m.group(13).indexOf(":")+1).trim());
				lun.setWritecache(m.group(14).substring(m.group(14).indexOf(":")+1).trim());
				lun.setReadcache(m.group(15).substring(m.group(15).indexOf(":")+1).trim());
				lun.setDefaultOwner(m.group(16).substring(m.group(16).indexOf(":")+1).trim());
				lun.setPrctRebuilt(m.group(17).substring(m.group(17).indexOf(":")+1).trim());
				lun.setPrctBound(m.group(18).substring(m.group(18).indexOf(":")+1).trim());
				lun.setLUNCapacity(m.group(19).substring(m.group(19).indexOf(":")+1).trim());
				lun.setDisksList(subParseToList(m.group(20).trim()));
				
				list.add(lun);
			}
		}
		return list;
	}
	
	
	
	
	
	
	
	
	
	
	private static List<String> subParseToList(String str){
		List<String> diskList = new ArrayList<String>();
		Pattern p = Pattern.compile("Bus \\d+ Enclosure \\d+  Disk \\d+");
		Matcher m = p.matcher(str);
		Set<String> diskSet = new HashSet<String>();
		while(m.find()){
			diskSet.add(m.group().trim());
		}
		if (diskSet.size()>0) {
			Iterator<String> iter = diskSet.iterator();
			while (iter.hasNext()) {
				diskList.add(iter.next());
			}
		}
		return diskList;
	}
}
