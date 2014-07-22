package com.afunms.emc.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.afunms.emc.model.Array;
import com.afunms.emc.model.Environment;
import com.afunms.emc.model.MemModel;

public class EnvironmentParser {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
//		String fileName = System.getProperty("user.dir")+File.separator+"log\\environment.log";
		String fileName = "C:/Users/Administrator/Desktop/emc´æ´¢/environment_-_1.log";
		FileReader fr = new FileReader(fileName);
		BufferedReader br = new BufferedReader(fr);
		String line = "";
		StringBuffer sb = new StringBuffer();
		Environment env = null;
		while (null != (line = br.readLine())) {
			sb.append(line).append("\r\n");
		}
		
		if (sb.length()>0) {
			env = parse(sb.toString());
		}
		System.out.println(sb.toString());
		System.out.println("====================");
		Array ar = env.getArray();
			System.out.println(ar.getStatus());
			System.out.println(ar.getAveragewatts());
			System.out.println(ar.getPresentWatts());
			System.out.println("-------array---------");
			
		List<MemModel> l2 = (List<MemModel>)env.getBakPowerList();
		MemModel mem = null;
		System.out.println(l2.size());
		for(int i=0;i<l2.size();i++){
			mem = (MemModel) l2.get(i);
			System.out.println(mem.getName());
			System.out.println(mem.getPowerStatus());
			System.out.println(mem.getPresentWatts());
			System.out.println(mem.getAverageWatts());
			System.out.println("-------getBakPowerList---------");
		}
		List<MemModel> l3 = (List<MemModel>)env.getMemList();
		MemModel mem1 = null;
		for(int i=0;i<l3.size();i++){
			mem1 = (MemModel) l3.get(i);
			System.out.println(mem1.getName());
			System.out.println(mem1.getPowerStatus());
			System.out.println(mem1.getPresentDegree());
			System.out.println(mem1.getAverageDegree());
			System.out.println(mem1.getAirStatus());
			System.out.println(mem1.getPresentWatts());
			System.out.println(mem1.getAverageWatts());
			System.out.println("-------getMemList---------");
		}
	}
	
	public static Environment parse(String str){
		Environment environment = new Environment();
		environment.setArray(arrayParse(str));
		environment.setMemList(memParse(str));
		environment.setBakPowerList(bakPowerParse(str));
		return environment;
	}
	
	private static Array arrayParse(String str){
		Array array = new Array(); 
		Pattern p = Pattern.compile("Array\\r\\n\\s*\\r\\n" +
				"Input Power\\r\\n" +
				"Status:\\s*(\\w+)\\r\\n" +
				"Present.watts.:\\s*(\\w+)\\r\\n" +
				"Rolling Average.watts.:\\s*(\\w+)\\r\\n");
		Matcher m = p.matcher(str);
		if (m.find()) {
			array.setStatus(m.group(1).trim());
			array.setPresentWatts(m.group(2).trim());
			array.setAveragewatts(m.group(3).trim());
		}
		return array;
	}
	
	private static List<MemModel> memParse(String str){
		List<MemModel> memModelList = new ArrayList<MemModel>();
		Pattern p = Pattern.compile("(\\w+ Bus \\d+ Enclosure \\d+)\\s*\\r\\n\\s*\\r\\n" +
				"Input Power\\r\\n" +
				"Status:\\s*(\\w+)\\r\\n" +
				"Present.watts.:\\s*(\\w+)\\r\\n"+
				"Rolling Average.watts.:\\s*(\\w+)\\r\\n\\w*\\r\\n"+
				"Air Inlet Temperature\\r\\n"+
				"Status:\\s*(\\w+)\\r\\n"+
				"Present.degree C.:\\s*(\\w+)\\r\\n"+
				"Rolling Average.degree C.:\\s*(\\w+)\\r\\n");
		Matcher m = p.matcher(str);
		while (m.find()) {
			MemModel memModel = new MemModel(); 
			memModel.setName(m.group(1).trim());
			memModel.setPowerStatus(m.group(2).trim());
			memModel.setPresentWatts(m.group(3).trim());
			memModel.setAverageWatts(m.group(4).trim());
			memModel.setAirStatus(m.group(5).trim());
			memModel.setPresentDegree(m.group(6).trim());
			memModel.setAverageDegree(m.group(7).trim());
			memModelList.add(memModel);
		}
		return memModelList;
	}	
	
	private static List<MemModel> bakPowerParse(String str){
		List<MemModel> memModelList = new ArrayList<MemModel>();
		Pattern p = Pattern.compile("(Bus \\d+ Enclosure \\d+ SPS \\w+)\\s*\\r\\n\\s*\\r\\n" +
				"Input Power\\r\\n" +
				"Status:\\s*(\\w+)\\r\\n" +
				"Present.watts.:\\s*(\\w+)\\r\\n"+
				"Rolling Average.watts.:\\s*(\\w+)\\r\\n\\w*\\r\\n");
		Matcher m = p.matcher(str);
		while (m.find()) {
			MemModel memModel = new MemModel(); 
			memModel.setName(m.group(1).trim());
			memModel.setPowerStatus(m.group(2).trim());
			memModel.setPresentWatts(m.group(3).trim());
			memModel.setAverageWatts(m.group(4).trim());
			memModelList.add(memModel);
		}
		return memModelList;
	}	
}
