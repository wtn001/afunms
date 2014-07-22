package com.afunms.event.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class QuestionService {
	private Map<String, List<String>> datas = new HashMap<String, List<String>>();
	
	public QuestionService(){
		String[] c1 = {"0A01��Щ�����ĵ�¼ʧ�ܴ�����ࣿ","0A02�����з����˶��ٵ�¼ʧ�ܵ��¼���","0A03��Щ�û��˺ű������¼��Ƚ϶ࣿ"};
		String[] c2 = {"0B01��Щ�û��ɹ��޸������ǵ����룿","0B02��Щ�û��޸�����ʧ�ܣ�","0B03��Щ�˺ű�ɾ��/���ã�","0B04��Щ�û��޸Ļ�������˰�ȫ�����־��"};
		String[] c3 = {"0C01��Щ���������˴������¼���","0C02��Щ���������˾����¼���","0C03��Щ���������˴�����Σ���¼���"};//,"0C04��Щ���������˶��ٸ�Σ���¼���"
		String[] c4 = {"0D01���һ�ܵĸ澯�¼�����Щ��", "0D02���һСʱ�����˶��ٴ����¼���"};
		String[] c5 = {"0E01��Щ�������ɵĸ澯��ࣿ"};
		String[] c6 = {"0F01����������վ�϶����û��޸���ϵͳʱ�䣿","0F02������������ж��ٸ�Σ���¼���","0F03������������ж��ٴζ�����ļ��б����ʣ�"};

		datas.put("ȫ������", null);
		datas.put("��¼/�˳�", Arrays.asList(c1));
		datas.put("�û��˻�", Arrays.asList(c2));
		datas.put("�¼�", Arrays.asList(c3));
		datas.put("����ʱ��", Arrays.asList(c4));
		datas.put("�澯", Arrays.asList(c5));
//		datas.put("ϵͳ", Arrays.asList(c6));
	}
	
	public List<String> loadQuestionDetail(String question) {
		return datas.get(question)==null?new ArrayList() : datas.get(question);
	}
	
	public List<String> loadQuestionDetailAll() {
		List list = new ArrayList();
		if (datas.size() > 0) {
			Set set = datas.keySet();
			Iterator iter = set.iterator();
			String key = "";
			while(iter.hasNext()){
				key = (String)iter.next();
				if (!"ȫ������".equals(key)) {
					list.addAll((List)datas.get(key));
				}
			}
		}
		return list;
	}

	public List<String> loadQuestionTitle() {
		return new ArrayList<String>(datas.keySet());
	}
}
