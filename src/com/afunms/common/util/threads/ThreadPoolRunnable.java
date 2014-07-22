package com.afunms.common.util.threads;



/** 
 * ����뽫������뵽�̳߳������У������ʵ�ָýӿڡ�
 * 
 * @author ����
 * @version 1.0
 */
public interface ThreadPoolRunnable {
    // XXX use notes or a hashtable-like
    // Important: ThreadData in JDK1.2 is implemented as a Hashtable( Thread -> object ),
    // expensive.
    
	/**
	 * @return ThreadPoolRunnableAttributes
	 *			�̵߳�����
	 */
	public ThreadPoolRunnableAttributes getThreadPoolRunnableAttributes();

    /**
     * �̳߳ػ����һ���߳���ִ�����������
     * ���ִ����ɺ���Ὣ�̹߳黹���̳߳ء�
     */
    public void runIt(ThreadPoolRunnableAttributes threadPoolRunnableAttributes);

}
