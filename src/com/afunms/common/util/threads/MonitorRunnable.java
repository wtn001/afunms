package com.afunms.common.util.threads;

import com.afunms.common.util.logging.Log;
import com.afunms.common.util.logging.LogFactory;

/**
 * ���ڼ�ص��̣߳�����ִ�������̳߳صĶ���
 */
public class MonitorRunnable implements Runnable {
	
	private static Log log = LogFactory.getLog(MonitorRunnable.class);
	
	/**
	 * �������߳���
	 */
	protected ThreadPool threadPool;
	
	/**
	 * �����߳�
	 */
	protected Thread thread;
	
	/**
	 * �Ƿ��ж�
	 */
	protected boolean isTerminate; 
	
	/**
	 * ���ִ�еļ��
	 */
	protected long interval;
	
	/**
	 * ����
	 */
	protected String name;

	/**
	 * ���췽��
	 * @param threadPool
	 * 			��Ҫ��Ӽ���̵߳��̳߳�
	 */
	public MonitorRunnable(ThreadPool threadPool) {
		this.setThreadPool(threadPool);
		if(log.isDebugEnabled()) {
			log.debug("�����̳߳ؼ���߳�");
		}
		
		this.start();
	}
	
	public void start() {
		this.isTerminate = false;
		this.thread = new Thread(this);
		this.thread.setDaemon(threadPool.isDaemon());
		setName(this.threadPool.getName() + "-Monitor");
		setInterval(threadPool.getWorkWaitTimeout());
		this.thread.start();
	}

	public void run() {
		while (true) {
			try {
				// �ȴ�ʱ���� ����˯��
				synchronized (this) {
					this.wait(interval);
				}
				
				if(isTerminate){
					break;
				}
				
				// ���ͻ��տ����߳�
				this.threadPool.checkSpareControllers();
			} catch (InterruptedException e) {
				log.error("�̳߳ؼ���߳�:" + getName() +  " �����쳣������ Unexpected exception", e);
			}
		}
	}
	
	/**
	 * ֹͣ����̣߳��� <code>terminate()</code> Ч��һ��
	 * @see #terminate()
	 */
	public void stop() {
		this.terminate();
	}

	/**
	 * �жϼ���߳�
	 */
	public synchronized void terminate() {
		this.isTerminate = true;
		this.notify();
	}

	/**
	 * @return the threadPool
	 */
	public ThreadPool getThreadPool() {
		return threadPool;
	}

	/**
	 * @param threadPool the threadPool to set
	 */
	public void setThreadPool(ThreadPool threadPool) {
		this.threadPool = threadPool;
	}

	/**
	 * @return the interval
	 */
	public long getInterval() {
		return interval;
	}

	/**
	 * @param interval the interval to set
	 */
	public void setInterval(long interval) {
		this.interval = interval;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		name = thread.getName();
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
		this.thread.setName(name);
	}
	
	

}
