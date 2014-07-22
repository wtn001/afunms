package com.afunms.common.util.threads;


/** 
 * ����뽫������뵽�̳߳������У������ʵ�ָýӿڡ�
 * 
 * @author ����
 * @version 1.0
 */
public abstract class ThreadPoolRunnableAbstract implements ThreadPoolRunnable{
    // XXX use notes or a hashtable-like
    // Important: ThreadData in JDK1.2 is implemented as a Hashtable( Thread -> object ),
    // expensive.
	
	@SuppressWarnings("unused")
	protected ThreadPoolRunnableAttributes threadPoolRunnableAttributes;
    
	/**
	 * @return ThreadPoolRunnableAttributes
	 *			�̵߳�����
	 */
	public abstract ThreadPoolRunnableAttributes getThreadPoolRunnableAttributes();
	

	/**
     * �����߳��������
	 * @param threadPoolRunnableAttributes the threadPoolRunnableAttributes to set
	 */
	public void setThreadPoolRunnableAttributes(
			ThreadPoolRunnableAttributes threadPoolRunnableAttributes) {
		this.threadPoolRunnableAttributes = threadPoolRunnableAttributes;
	}



	/**
     * �̳߳ػ����һ���߳���ִ�����������
     * ���ִ����ɺ���Ὣ�̹߳黹���̳߳ء�
     */
    public abstract void runIt(ThreadPoolRunnableAttributes threadPoolRunnableAttributes);

}
