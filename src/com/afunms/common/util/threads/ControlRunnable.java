package com.afunms.common.util.threads;

import com.afunms.common.util.logging.Log;
import com.afunms.common.util.logging.LogFactory;


/**
 * ���̳߳ؿ��ƺ�����ִ�и��ֲ���(�� {@link ThreadPoolRunnable} )
 * ��һ���̶߳��� 
 * 
 * @author ����
 * @version 1.0 $Date: 2011-03-07 16:34:30 +0100 (Mon, Mar 7, 2011) $
 */
public class ControlRunnable implements Runnable {
	
	private Log log = LogFactory.getLog(ControlRunnable.class);
	
	/**
	 * �������߳���
	 */
	protected ThreadPool threadPool;
	
	/**
	 * �Ƿ��ж�
	 */
	protected boolean shouldTerminate; 
	
	/**
	 * ���ڼ���ִ���߳�
	 */
	protected boolean shouldRun;
	
	/**
	 * ���߳�ִ�еķ�����
	 */
	
	/**
	 * 1. Runnable ��ʽ
	 */
	protected Runnable toRunRunnable;
	
	/**
	 * 2. ThreadPoolRunnable ����ʽ
	 */
	protected ThreadPoolRunnable toRun;
	
	/**
	 * ����ִ�е��߳�
	 */
	protected Thread thread;
	
	/**
	 * ���̵߳�����
	 */
	protected ThreadPoolRunnableAttributes runnableAttributes;


	/**
	 * ����һ�����߳�
	 */
	protected ControlRunnable(ThreadPool threadPool) {
		this.threadPool = threadPool;
		toRun = null;
		shouldTerminate = false;
		shouldRun = false;
		thread = new Thread(this);
		thread.setDaemon(this.threadPool.isDaemon());
		//thread.setName(this.threadPool.getName() + "-Processor" + this.threadPool.incSequence());
		thread.setPriority(threadPool.getThreadPriority());
		this.threadPool.addThread(thread, this);
		thread.start();
	}

	public void run() {

		boolean _shouldRun = false;
		boolean _shouldTerminate = false;
		ThreadPoolRunnable _toRun = null;
		try {
			while (true) {
				try {
					/* Wait for work. */
					synchronized (this) {
						while (!shouldRun && !shouldTerminate) {
							this.wait();
						}
						_shouldRun = shouldRun;
						_shouldTerminate = shouldTerminate;
						_toRun = toRun;
					}

					if (_shouldTerminate) {
						if (log.isDebugEnabled())
							log.debug("Terminate");
						break;
					}

					/* ����Ƿ����ִ��һ���߳� */
					try {
						if (_toRun != null) {
							runnableAttributes = _toRun.getThreadPoolRunnableAttributes();
//							if (log.isDebugEnabled())
//								log.debug("Getting thread data");
						}
						if (_shouldRun) {
							if (_toRun != null) {
								_toRun.runIt(runnableAttributes);
							} else if (toRunRunnable != null) {
								toRunRunnable.run();
							} else {
								if (log.isDebugEnabled())
									log.debug("No toRun ???");
							}
						}
					} catch (Throwable t) {
//						log.error(sm.getString(
//								"threadpool.thread_error", t, toRun
//										.toString()));
						/*
						 * 
						 * ������ runnable �׳��쳣 (����������һ�����߳�)����˵��
						 * ����߳��Ѿ�������
						 * 
						 * Ҳ����ζ��������Ҫ���̳߳����ͷ�����߳�
						 * 
						 */
						_shouldTerminate = true;
						_shouldRun = false;
						threadPool.notifyThreadEnd(this);
					} finally {
						if (_shouldRun) {
							shouldRun = false;
							/*
							 * ֪ͨ�̳߳أ����߳����ڴ��ڿ���״̬
							 */
							threadPool.returnController(this);
						}
					}

					/*
					 * ����Ƿ���ֹ
					 */
					if (_shouldTerminate) {
						break;
					}
				} catch (InterruptedException ie) { 
					/*
					 * Ϊ�˵ȴ�һֱ�ܹ�������ȥ�����ǲ�������жϣ�
					 * ����������ᷢ��
					 */
					log.error("Unexpected exception", ie);
				}
			}
		} finally {
			threadPool.removeThread(Thread.currentThread());
		}
	}
	
	/**
	 * Run a task
	 * 
	 * @param toRun
	 */
	public synchronized void runIt(Runnable toRun) {
		this.toRunRunnable = toRun;
		// Do not re-init, the whole idea is to run init only once per
		// thread - the pool is supposed to run a single task, that is
		// initialized once.
		// noThData = true;
		//shouldRun = true;
		this.notify();
	}
	
	/**
	 * Run a task
	 * 
	 * @param toRun
	 */
	public synchronized void runIt(ThreadPoolRunnable toRun) {
		 this.toRun = toRun;
		// Do not re-init, the whole idea is to run init only once per
		// thread - the pool is supposed to run a single task, that is
		// initialized once.
		// noThData = true;
		shouldRun = true;
		this.notify();
	}
	
	public synchronized void terminate() {
		shouldTerminate = true;
		this.notify();
	}
}
