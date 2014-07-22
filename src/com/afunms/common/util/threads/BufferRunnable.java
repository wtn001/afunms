package com.afunms.common.util.threads;

import java.util.Vector;

import com.afunms.common.util.logging.Log;
import com.afunms.common.util.logging.LogFactory;



/**
 * ���ڴ����̳߳ػ���������Ҫִ�еĲ�����������Ҫ����Ĳ����ȼ��뵽
 * �������У�Ȼ���ɸ��߳�ÿ�ν��������Ĳ������뵽�̳߳������С�
 * 
 */
public class BufferRunnable implements Runnable {
	
	private static Log log = LogFactory.getLog(BufferRunnable.class);
	
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
	 * ����
	 */
	protected String name;
	
	/**
	 * <code>ThreadPoolRunnable</code> ������
	 */
	protected Vector<ThreadPoolRunnable> threadPoolRunnableBuffer = null;
	
	/**
	 * ��ǰִ�е� <code>ThreadPoolRunnable</code> 
	 */
	protected ThreadPoolRunnable threadPoolRunnable = null;

	/**
	 * ���췽��
	 * @param threadPool
	 * 			��Ҫ��ӻ����̵߳��̳߳�
	 */
	public BufferRunnable(ThreadPool threadPool) {
		this.setThreadPool(threadPool);
		this.start();
	}
	
	public void start() {
		this.isTerminate = false;
		this.thread = new Thread(this);
		this.thread.setDaemon(threadPool.isDaemon());
		setName(this.threadPool.getName() + "-Buffer");
		setThreadPoolRunnableBuffer(threadPool.threadPoolBuffer);
		if(log.isDebugEnabled()) {
			log.debug("�����̳߳ػ������߳�");
		}
		this.thread.start();
	}

	public void run() {
		while (true) {
			try {
				// �����������û���κ���Ҫִ�е� ThreadPoolRunnable 
				synchronized (this) {
					while (threadPoolRunnableBuffer == null || threadPoolRunnableBuffer.size() == 0) {
						this.wait();
						if(isTerminate){
							threadPoolRunnableBuffer = null;
						}
					}
					if(isTerminate){
						break;
					}
					threadPoolRunnable = threadPoolRunnableBuffer.remove(0);
				}
				if(log.isDebugEnabled()) {
					log.debug("�ӻ������л�ȡһ����Ҫִ�е� ThreadPoolRunnable");
				}
				// ִ��
				this.threadPool.runIt(threadPoolRunnable);
			} catch (InterruptedException e) {
				log.error("�̳߳ػ����߳�:" + getName() +  " �����쳣������ Unexpected exception", e);
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
	 * ִ�л����߳�
	 */
	public synchronized void runIt() {
		this.isTerminate = false;
		this.notify();
	}

	/**
	 * �жϻ����߳�
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

	/**
	 * @return the threadPoolRunnableBuffer
	 */
	public Vector<ThreadPoolRunnable> getThreadPoolRunnableBuffer() {
		return threadPoolRunnableBuffer;
	}

	/**
	 * @param threadPoolRunnableBuffer the threadPoolRunnableBuffer to set
	 */
	public void setThreadPoolRunnableBuffer(
			Vector<ThreadPoolRunnable> threadPoolRunnableBuffer) {
		this.threadPoolRunnableBuffer = threadPoolRunnableBuffer;
	}

	

}
