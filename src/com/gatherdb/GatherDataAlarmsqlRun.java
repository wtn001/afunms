package com.gatherdb;

import java.util.TimerTask;
import java.util.Vector;

public class GatherDataAlarmsqlRun extends TimerTask {

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		Vector alldata=null;
		//NetHostDatatempResultTosql datatemp=new NetHostDatatempResultTosql();
		//datatemp.ResulttoSql();
		
		if(!GathersqlListManager.idbstatus_alarm)
		{
			
	    //System.out.println("=====��ʼ��ʱ�������======"+GathersqlListManager.idbdatatempstatus);		
		GathersqlListManager.Addsql_alarm("DHCC-DB");
		}
			
		
	}

}
