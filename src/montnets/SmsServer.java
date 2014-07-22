package montnets;


import java.net.MalformedURLException;
import java.rmi.RemoteException;

import javax.xml.namespace.QName;
import javax.xml.rpc.ParameterMode;
import javax.xml.rpc.ServiceException;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.XMLType;

import com.afunms.alarm.dao.OutBoxDao;
import com.afunms.alarm.model.OutBox;
import com.afunms.common.util.SysLogger;

  public class SmsServer {
	  
	  
	public int sendSMS(String num, String msg) {
		mondem Mytest = new mondem(); // ����һ�� mondem ���� �������������֧��64���˿ڷ���
		int rc=-1;
		rc = Mytest.SetThreadMode(1); // �����߳�ģʽ
		if (rc == 0) {
			System.out.println("�����߳�ģʽ�ɹ�");
		} else {
			System.out.println("�����߳�ģʽʧ��");
		}
		// ȫ�����óɵ���è��ʽ
		Mytest.SetModemType(0, 0);
		Mytest.SetModemType(1, 0);
		Mytest.SetModemType(2, 0);
		Mytest.SetModemType(3, 0);
		Mytest.SetModemType(4, 0);
		Mytest.SetModemType(5, 0);
		Mytest.SetModemType(6, 0);
		Mytest.SetModemType(7, 0);

		if ((rc = (Mytest.InitModem(-1))) == 0)// ��ʼ������è
		{
			System.out.println("��ʼ���ɹ�");
			try {
				rc = Mytest.SendMsg(-1, num, msg); // ����һ����Ϣ
			} catch (Exception ex) {
				ex.printStackTrace();
				rc = -1;
			}
			if (rc >= 0) {
				System.out.println("�ύ�ɹ�, rc=" + rc);
			} else {
				System.out.println("�ύ����, rc=" + rc);
			}
		} else {
			System.out.println("��ʼ������!" + rc);
		}
		return rc;
	}
	
	public int sendSMS_chuangtian(String num, String msg,String recordtime) {
		//SysLogger.info("######## ��ʼ���Ͷ��� ###############");
		OutBoxDao outboxdao = new OutBoxDao();
		OutBox outbox = new OutBox();
		outbox.setMsg(msg);
		outbox.setReceiverMobileNo(num);
		outbox.setSender("����ϵͳ");
		outbox.setSendTime(recordtime);
		int rc=-1;
		try{
			outboxdao.save(outbox);
			rc = 0;
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			outboxdao.close();
		}
		return rc;
	}
	
	
	/**
	 * ���������ֳ���������---�������ص��ö˿�
	 * ����webserice ���Ͷ��Ÿ澯
	 * @param mask ���ű���� 5701
	 * @param strPhone  �ֻ���
	 * @param strContent ��Ϣ����
	 */
	public String SendOneMessageMsgID(String mask,String strPhone,String strContent){  
		String result="-1011";//ʧ��
		  try {
		   Service service = new Service();
		   Call call = (Call)service.createCall();
		   call.setTargetEndpointAddress(new java.net.URL("http://10.10.7.239:8009/service.asmx"));
		   
		   
		   call.setOperationName(new QName("http://tempuri.org/","SendOneMessageMsgID"));
		   
		   //��Ӳ���
		   call.addParameter(new QName("http://tempuri.org/","mask"),XMLType.XSD_STRING, ParameterMode.IN);
		   call.addParameter(new QName("http://tempuri.org/","strPhone"),XMLType.XSD_STRING, ParameterMode.IN);
		   call.addParameter(new QName("http://tempuri.org/","strContent"),XMLType.XSD_STRING, ParameterMode.IN);
		   
		   
		   
		   
		   call.setUseSOAPAction(true);
		   call.setSOAPActionURI("http://tempuri.org/SendOneMessageMsgID");
		   call.setReturnType(XMLType.XSD_STRING);

		   result = (String)call.invoke(new Object[]{mask,strPhone,strContent}); 
		   
		   return result;
		   
		  } catch (ServiceException e) {
		   // TODO Auto-generated catch block
		   e.printStackTrace();
		  } catch (RemoteException e) {
		   // TODO Auto-generated catch block
		   e.printStackTrace();
		  } catch (MalformedURLException e) {
		   // TODO Auto-generated catch block
		   e.printStackTrace();
		  }
		  
		  return result;
		 }
	
	

	public static void main(String args[]) {
		SmsServer test = new SmsServer();
		//test.sendSMS("15210016034", "������Ϣ����ϲ����!");

	}
}
