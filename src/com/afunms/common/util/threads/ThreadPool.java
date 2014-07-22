/*
 *  ���̳߳�Ϊ�ɼ����ṩ�ɼ��̵߳��̳߳أ�����������뷨���ɼ��Ĳɼ��̹߳���һ���̳߳ء�
 *  ���ɼ���������ʱ���ɼ��̼߳��뵽�̳߳��С�
 *  ������ӻ��޸ģ����ڴ�ע����
 *  
 */

package com.afunms.common.util.threads;

import com.afunms.common.util.logging.Log;
import com.afunms.common.util.logging.LogFactory;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Vector;
import java.util.regex.Pattern;




/**
 * 
 * ģ�� Tomcat ���̳߳�
 * 
 * @author <a href=nielin@dhcc.com.cn>����</a> 
 * @version 1.0
 */
public class ThreadPool {
	
	private static Log log = LogFactory.getLog(ThreadPool.class);
	
	private static boolean logfull = true;
	
	/**
	 * 	Ĭ�ϵ�����߳���  Ĭ��ֵ
	 */
	public static final int MAX_THREADS = 200;
	
	/**
	 * 	Ĭ�ϵ���С�߳���  Ĭ��ֵ
	 */
	public static final int MAX_THREADS_MIN = 10;
	
	/**
	 * 	Ĭ�ϵ����Ŀ����߳���  Ĭ��ֵ
	 */
	public static final int MAX_SPARE_THREADS = 50;
	
	/**
	 * Ĭ�ϵ���С�Ŀ����߳���  Ĭ��ֵ
	 */
	public static final int MIN_SPARE_THREADS = 5;
	
	/**
	 * 	Ĭ�ϵ�������ȴ�ʱ��(�����տ����̼߳��ʱ��)  Ĭ��ֵ
	 */
	public static final long WORK_WAIT_TIMEOUT = 60 * 1000;
	
	/**
	 * 	Ĭ�ϵ��̳߳ص��߳����ȼ�  Ĭ��ֵ
	 */
	public static final int THREAD_PRIORITIY = Thread.NORM_PRIORITY;
	
	/**
	 * 	Ĭ�ϵ��̳߳ص��߳����ȼ�  Ĭ��ֵ
	 */
	public static final String THREAD_NAME = "TP";


	/**
	 * 	�̳߳ص�����߳���
	 */
	protected int maxThreads;

	/**
	 * 	�̳߳���С�����߳���
	 */
	protected int minSpareThreads;

	/**
	 * 	�̳߳��������߳���
	 */
	protected int maxSpareThreads;

	/**
	 * ��ǰ�̳߳ص��е��߳���
	 */
	protected int currentThreadCount;

	/**
	 * 	��ǰ�������߳���
	 */
	protected int currentThreadsBusy;

	/**
	 * 	�Ƿ���ͣ�̳߳�(����ͣ�̳߳ص��е������߳���)
	 */
	protected boolean stopThePool;

	/* Flag to control if the main thread is 'daemon' */
	protected boolean isDaemon = true;

	/**
	 * 	�̳߳�����
	 */
	protected String name;
	
	/**
	 * �߳�ֵ������
	 */
	protected Properties properties = null;
	
	/**
	 * ����ִ���߳�����
	 */
	protected ControlRunnable[] pool = null;
	
	/**
	 * ���ڼ�غ�������߳�
	 */
	protected MonitorRunnable monitorRunnable = null;
	
	/**
	 * ���ڽ��������� <code>ThreadPoolRunnable</code>
	 * ���뵽�̳߳�������
	 */
	protected BufferRunnable bufferRunnable = null;
	
	/**
	 * <code>ThreadPoolRunnable</code> ������
	 */
	protected Vector<ThreadPoolRunnable> threadPoolBuffer = new Vector<ThreadPoolRunnable>();
	
	
	/**
	 * <p>���ڴ�Ŵ򿪵��̡߳�</p>
	 * <p>���ǣ�<code>Thread</code> ��ֵ�ǣ�<code>ControlRunnable</code></p>
	 */
	protected Hashtable<Thread, ControlRunnable> threads = new Hashtable<Thread, ControlRunnable>();
	
	/**
	 * �̳߳ؼ�������
	 */
	protected Vector<ThreadPoolListener> listeners = new Vector<ThreadPoolListener>();

	/**
	 * 
	 */
	protected int sequence = 1;

	/**
	 *	�߳����ȼ�
	 */
	protected int threadPriority;
	
	/**
	 * �����ȴ��ĳ�ʱʱ�䣬���������̻߳��յĵȴ�ʱ��
	 */
	protected long workWaitTimeout;
	
	/**
	 * ʹ��Ĭ�ϵĲ���������һ���̳߳�
	 */
	public ThreadPool() {
		initialize();
	}
	
	/**
	 * ʹ��Ĭ�ϵĲ���������һ���̳߳�
	 */
	public ThreadPool(Properties properties) {
		setProperties(properties);
		initialize();
	}
	
	/**
	 * <p>����һ���̳߳�ʵ����</p>
	 * <p>����� <code>jmx</code> ��δʹ�ã��÷�����ͬ�ڷ���
	 * һ��Ĭ�����Ե��̳߳ء�</p>
	 * @param jmx
	 *            ��δʹ��(UNUSED)
	 * @return �̳߳�<code>ThreadPool</code> ��ʵ��. If JMX support is requested, you need to
	 *         call register() in order to set a name.
	 */
	public static ThreadPool createThreadPool(boolean jmx) {
		return new ThreadPool();
	}
	
	protected void initialize(){
		if(properties == null){
			if(log.isDebugEnabled()){
				log.debug("�̳߳����Լ�Ϊ�գ�ʹ��Ĭ�����ó�ʼ��");
			}
			properties = new Properties();
		}
		currentThreadCount = 0;
		currentThreadsBusy = 0;
		stopThePool = false;
		
		name = THREAD_NAME;
		maxThreads = MAX_THREADS;
		maxSpareThreads = MAX_SPARE_THREADS;
		minSpareThreads = MIN_SPARE_THREADS;
		threadPriority = THREAD_PRIORITIY;
		workWaitTimeout = WORK_WAIT_TIMEOUT;
		
		String _name = properties.getProperty(ThreadPoolProperitesConstant.MAX_THREADS);
		if(_name != null && _name.trim().length() > 0) {
			name = _name.trim();
		} else {
			if(log.isDebugEnabled()){
				log.debug("�̳߳�����δ���û��������ó���ʹ��Ĭ�����ã�" + name);
			}
		}
		
		Pattern pattern = Pattern.compile("[0-9]+");
		
		String _maxThreads = properties.getProperty(ThreadPoolProperitesConstant.MAX_THREADS);
		if(_maxThreads != null && pattern.matcher(_maxThreads).matches()) {
			if(Integer.valueOf(_maxThreads) >= MAX_THREADS_MIN) {
				maxThreads = Integer.valueOf(_maxThreads);
			} else {
				if(log.isDebugEnabled()){
					log.debug("�̳߳�����߳�������С��:" + maxThreads + "��ʹ��Ĭ�����ã�" + maxThreads);
				}
			}
		} else {
			if(log.isDebugEnabled()){
				log.debug("�̳߳�����߳���û�����û��������ó���ʹ��Ĭ�����ã�" + maxThreads);
			}
		}
		
		String _maxSpareThreads = properties.getProperty(ThreadPoolProperitesConstant.MAX_SPARE_THREADS);
		if(_maxSpareThreads != null && pattern.matcher(_maxSpareThreads).matches()) {
			maxSpareThreads = Integer.valueOf(_maxSpareThreads);
		} else {
			if(log.isDebugEnabled()){
				log.debug("�̳߳���������û�����û��������ó���ʹ��Ĭ�����ã�" + maxSpareThreads);
			}
		}
		
		String _minSpareThreads = properties.getProperty(ThreadPoolProperitesConstant.MIN_SPARE_THREADS);
		if(_minSpareThreads != null && pattern.matcher(_minSpareThreads).matches()) {
			minSpareThreads = Integer.valueOf(_minSpareThreads);
		} else {
			if(log.isDebugEnabled()){
				log.debug("�̳߳���С������û�����û��������ó���ʹ��Ĭ�����ã�" + minSpareThreads);
			}
		}
		
		String _threadPriority = properties.getProperty(ThreadPoolProperitesConstant.THREAD_PRIORITIY);
		if(_threadPriority != null && pattern.matcher(_threadPriority).matches()) {
			if(Integer.valueOf(_threadPriority) >= Thread.MIN_PRIORITY && Integer.valueOf(_threadPriority) <= Thread.MAX_PRIORITY) {
				threadPriority = Integer.valueOf(_threadPriority);
			} else {
				if(log.isDebugEnabled()){
					log.debug("�̳߳����ȼ��趨����1~10��Χ�ڣ�ʹ��Ĭ�����ã�" + threadPriority);
				}
			}
		}
		
		String _workWaitTimeout = properties.getProperty(ThreadPoolProperitesConstant.WORK_WAIT_TIMEOUT);
		if(_workWaitTimeout != null && pattern.matcher(_workWaitTimeout).matches()) {
			workWaitTimeout = Long.valueOf(_workWaitTimeout);
		} else {
			if(log.isDebugEnabled()){
				log.debug("�̳߳����̵߳Ĺ����ȴ�ʱ�䣨���������̱߳����յ�ʱ������û�����û��������ó���ʹ��Ĭ�����ã���λ���룩��" + workWaitTimeout);
			}
		}
	}
	
	/**
	 * <p>�����̳߳�</p>
	 * <p>�̳߳�</p>
	 * @return
	 */
	public synchronized boolean start() {
		stopThePool = false;
		currentThreadCount = 0;
		currentThreadsBusy = 0;

		adjustLimits();

		pool = new ControlRunnable[maxThreads];

		openThreads(minSpareThreads);
		if (maxSpareThreads < maxThreads) {
			monitorRunnable = new MonitorRunnable(this);
			bufferRunnable = new BufferRunnable(this);
		}
		
		return false;
	}
	
	/**
	 * ����δ�������߳�
	 * 
	 * @param toOpen
	 *            ��Ҫ�򿪵��߳���
	 */
	protected void openThreads(int toOpen) {

		if (toOpen > maxThreads) {
			toOpen = maxThreads;
		}

		for (int i = currentThreadCount; i < toOpen; i++) {
			pool[i - currentThreadsBusy] = new ControlRunnable(this);
		}

		currentThreadCount = toOpen;
	}
	
	/**
	 * �ɼ���߳������ͻ��տ����߳�
	 */
	protected synchronized void checkSpareControllers() {

		if (stopThePool) {
			return;
		}

		if ((currentThreadCount - currentThreadsBusy) > maxSpareThreads) {
			int toFree = currentThreadCount - currentThreadsBusy
					- maxSpareThreads;

			for (int i = 0; i < toFree; i++) {
				ControlRunnable controlRunnable = pool[currentThreadCount
						- currentThreadsBusy - 1];
				controlRunnable.terminate();
				pool[currentThreadCount - currentThreadsBusy - 1] = null;
				currentThreadCount--;
			}

		}

	}
	
	/**
	 * ����������⣬����������޸������ṩ��Ĭ�ϵ�����
	 */
	protected void adjustLimits() {
		if (maxThreads <= 0) {
			maxThreads = MAX_THREADS;
		} else if (maxThreads < MAX_THREADS_MIN) {
			maxThreads = MAX_THREADS_MIN;
			log.warn("�̳߳�����߳�������С��:" + maxThreads + "����ǰ���ã�" + maxThreads);
		}

		if (maxSpareThreads >= maxThreads) {
			maxSpareThreads = maxThreads;
			log.warn("�̳߳��������߳������ܴ�������߳�������ǰ���ã�" + maxSpareThreads);
		}

		if (maxSpareThreads <= 0) {
			if (1 == maxThreads) {
				maxSpareThreads = 1;
			} else {
				maxSpareThreads = maxThreads / 2;
			}
			log.warn("�̳߳��������߳�������С��0����ǰ���ã�" + maxSpareThreads);
		}

		if (minSpareThreads > maxSpareThreads) {
			minSpareThreads = maxSpareThreads;
			log.warn("�̳߳���С�����߳������ܴ����������߳�������ǰ���ã�" + minSpareThreads);
		}

		if (minSpareThreads <= 0) {
			if (1 == maxSpareThreads) {
				minSpareThreads = 1;
			} else {
				minSpareThreads = maxSpareThreads / 2;
			}
			log.warn("�̳߳���С�����߳�������С��0����ǰ���ã�" + maxSpareThreads);
		}
	}

	
	/**
	 * ֹͣ�̳߳�
	 */
	public synchronized void shutdown() {
		if (!stopThePool) {
			stopThePool = true;
			if (monitorRunnable != null) {
				// ֹͣ����߳�
				monitorRunnable.terminate();
				monitorRunnable = null;
			}
			for (int i = 0; i < currentThreadCount - currentThreadsBusy; i++) {
				try {
					pool[i].terminate();
				} catch (Throwable t) {
					// �����κ���...�������У�ֹͣ�̳߳أ�������ֹͣ����������
					log.error("Ignored exception while shutting down thread pool",
									t);
				}
			}
			currentThreadsBusy = currentThreadCount = 0;
			pool = null;
			notifyAll();
		}
	}
	
	/**
	 * ���̳߳صĻ����������һ����Ҫ���е� <code>ThreadPoolRunnable</code> ,
	 * һ�����뵽�������У�����̳߳��Ѿ������������뵽�̳߳����Զ����С�
	 * ����̳߳ص������̶߳���æ�����һֱ�ȴ�һ�������߳������С�
	 * @param r
	 * 			��Ҫ���е� ThreadPoolRunnable
	 */
	public void add(ThreadPoolRunnable r) {
		if (null == r) {
			throw new NullPointerException();
		}
		
		threadPoolBuffer.add(r);
		
		bufferRunnable.runIt();
	}
	
	/**
	 * ���и����� ThreadPoolRunnable
	 * @param r
	 * 			������ ThreadPoolRunnable
	 */
	public void runIt(ThreadPoolRunnable r) {
		if (null == r) {
			throw new NullPointerException();
		}

		ControlRunnable c = findControlRunnable();
		c.runIt(r);
	}
	
	
	private ControlRunnable findControlRunnable() {
		ControlRunnable c = null;

		if (stopThePool) {
			throw new IllegalStateException();
		}

		// ���̳߳��л�ȡһ�����߳�
		synchronized (this) {

			while (currentThreadsBusy == currentThreadCount) {
				// �����̶߳���æ
				if (currentThreadCount < maxThreads) {
					// ������е��̶߳��Ǵ򿪵ģ����һ���µĿ��߳��̣߳����߳���λ��������
					int toOpen = currentThreadCount + minSpareThreads;
					openThreads(toOpen);
				} else {
					logFull(log, currentThreadCount, maxThreads);
					// �ȴ��̵߳������
					try {
						this.wait();
					}
					// ������������ڲ��� Throwable; ��Ϊ�����ܻ��� wait() �׳������쳣;
					// ���������ֻ�����ڲ���һ���쳣; ��Ϊû�����κεط�ʹ���߳��жϣ�����
					// ����쳣��Զ���ᷢ����
					catch (InterruptedException e) {
						log.error("�ȴ������̳߳����쳣������Unexpected exception", e);
					}
					if (log.isDebugEnabled()) {
						log.debug("Finished waiting: CTC=" + currentThreadCount
								+ ", CTB=" + currentThreadsBusy);
					}
					// ���ֹͣ�̳߳أ����˳�
					if (stopThePool) {
						break;
					}
				}
			}
			// �̳߳��Ѿ�ֹͣ���׳��쳣
			if (0 == currentThreadCount || stopThePool) {
				throw new IllegalStateException();
			}

			// ������������˵����һ�������̣߳�����ȥʹ�á�
			int pos = currentThreadCount - currentThreadsBusy - 1;
			c = pool[pos];
			pool[pos] = null;
			currentThreadsBusy++;

		}
		return c;
	}
	
	/**
	 * 
	 * ֪ͨ�̳߳أ�ָ�����߳��Ѿ���ɡ�
	 * 
	 * �� ControlRunnable.run() ִ�����׳��쳣ʱ�����á�
	 * 
	 */
	protected synchronized void notifyThreadEnd(ControlRunnable c) {
		currentThreadsBusy--;
		currentThreadCount--;
		notify();
	}
	
	/**
	 * ���̷߳��ص��߳��С�
	 * ��������̷߳���ɿ����̣߳������̳߳���������á�
	 */
	protected synchronized void returnController(ControlRunnable c) {

		if (0 == currentThreadCount || stopThePool) {
			c.terminate();
			return;
		}

		// atomic
		currentThreadsBusy--;

		pool[currentThreadCount - currentThreadsBusy - 1] = c;
		notify();
	}
	
	/**
	 * ���߳������һ���߳�
	 * @param thread ��Ҫ��ӵ��߳�
	 * @param cr     �߳������Ŀ����߳�
	 */
	public void addThread(Thread thread, ControlRunnable cr) {
//		threads.put(t, cr);
		for (int i = 0; i < listeners.size(); i++) {
			ThreadPoolListener threadPoolListener = (ThreadPoolListener) listeners
						.elementAt(i);
			threadPoolListener.threadEnd(this, thread);
		}
	}
	
	/**
	 * �Ƴ�ָ�����߳�
	 * @param thread
	 * 			��Ҫ�Ƴ����߳�
	 */
	public void removeThread(Thread thread) {
//		threads.remove(t);
		for (int i = 0; i < listeners.size(); i++) {
			ThreadPoolListener threadPoolListener = (ThreadPoolListener) listeners
					.elementAt(i);
			threadPoolListener.threadEnd(this, thread);
		}
	}
	
	private static void logFull(Log loghelper, int currentThreadCount,
			int maxThreads) {
		if (logfull) {
//			log.error(sm.getString("threadpool.busy", new Integer(
//					currentThreadCount), new Integer(maxThreads)));
//			logfull = false;
		} else if (log.isDebugEnabled()) {
			log.debug("All threads are busy " + currentThreadCount + " "
					+ maxThreads);
		}
	}


	// --------------------------------------------------------- ��ȡ���������Է���
	

	/**
	 * �����̳߳ص����Լ�
	 * @return the properties �̳߳ص����Լ�
	 */
	public Properties getProperties() {
		return properties;
	}

	/**
	 * �����̳߳ص����Լ�
	 * @param properties �̳߳ص����Լ�
	 */
	public void setProperties(Properties properties) {
		this.properties = properties;
	}


	
	
	/**
	 * ��ȡ�̳߳����ơ�
	 * @return �����̳߳ص�����
	 */
	public String getName() {
		return name;
	}

	/**
	 * �����̳߳����ơ�
	 * @param name
	 * 			�̳߳�����
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * ��ȡ�̳߳����߳�����Ĭ�ϵ����ȼ���
	 * @return �̳߳ظ��߳�Ĭ�ϵ����ȼ�
	 */
	public int getThreadPriority() {
		return threadPriority;
	}

	/**
	 * �����̳߳ظ��߳�����Ĭ�ϵ����ȼ���
	 * @param threadPriority
	 * 			�̳߳ظ��߳�Ĭ�����õ����ȼ�
	 */
	public void setThreadPriority(int threadPriority) {
		if (log.isDebugEnabled())
			log.debug(getClass().getName() + ": setPriority(" + threadPriority
					+ "): here.");

		if (threadPriority < Thread.MIN_PRIORITY) {
			throw new IllegalArgumentException("new priority < MIN_PRIORITY");
		} else if (threadPriority > Thread.MAX_PRIORITY) {
			throw new IllegalArgumentException("new priority > MAX_PRIORITY");
		}

		this.threadPriority = threadPriority;

		Enumeration<Thread> currentThreads = getThreads();
		Thread t = null;
		while (currentThreads.hasMoreElements()) {
			// ���õ�ǰ�����̵߳����ȼ�
			t = (Thread) currentThreads.nextElement();
			t.setPriority(threadPriority);
		}
	}
	
	
	/**
	 * ��������߳�����
	 * @param maxThreads
	 * 			����߳���
	 */
	public void setMaxThreads(int maxThreads) {
		this.maxThreads = maxThreads;
	}

	/**
	 * ��������߳�����
	 * @return ����߳���
	 */
	public int getMaxThreads() {
		return maxThreads;
	}

	/**
	 * ������С�����߳���
	 * @param minSpareThreads
	 * 			��С�����߳���
	 */
	public void setMinSpareThreads(int minSpareThreads) {
		this.minSpareThreads = minSpareThreads;
	}

	/**
	 * ������С�����߳�����
	 * @return ��С�����߳���
	 */
	public int getMinSpareThreads() {
		return minSpareThreads;
	}

	/**
	 * �����������߳�����
	 * @param maxSpareThreads
	 * 			�������߳���
	 */
	public void setMaxSpareThreads(int maxSpareThreads) {
		this.maxSpareThreads = maxSpareThreads;
	}

	/**
	 * �����������߳�����
	 * @return �������߳���
	 */
	public int getMaxSpareThreads() {
		return maxSpareThreads;
	}
	
	/**
	 * �����̳߳��л���������Ҫִ�е� <code>ThreadPoolRunnable</code> ���б�
	 * @return the threadPoolBuffer
	 * 			����������Ҫִ�е� <code>ThreadPoolRunnable</code> ���б�
	 */
	public Vector<ThreadPoolRunnable> getThreadPoolBuffer() {
		return threadPoolBuffer;
	}

	/**
	 * �����̳߳ػ���������Ҫִ�е� <code>ThreadPoolRunnable</code> ���б�
	 * @param threadPoolBuffer the threadPoolBuffer to set
	 * 		��Ҫִ�е� <code>ThreadPoolRunnable</code> ���б�
	 */
	public void setThreadPoolBuffer(Vector<ThreadPoolRunnable> threadPoolBuffer) {
		this.threadPoolBuffer = threadPoolBuffer;
	}
	
	/**
	 * ���ع����ȴ��ĳ�ʱʱ�䣬���������̻߳��յĵȴ�ʱ�䡣��λ������
	 * @return the workWaitTimeout
	 * 			�����ȴ��ĳ�ʱʱ�䣬���������̻߳��յĵȴ�ʱ�䡣��λ������
	 */
	public long getWorkWaitTimeout() {
		return workWaitTimeout;
	}

	/**
	 * ���ù����ȴ��ĳ�ʱʱ�䣬���������̻߳��յĵȴ�ʱ�䡣��λ������
	 * @param workWaitTimeout the workWaitTimeout to set
	 * 			��Ҫ���õĹ����ȴ��ĳ�ʱʱ�䣬���������̻߳��յĵȴ�ʱ�䡣��λ������
	 */
	public void setWorkWaitTimeout(long workWaitTimeout) {
		this.workWaitTimeout = workWaitTimeout;
	}
	
	
	// --------------------------------------------------------- ��ȡֻ�����Եķ���
	
	/**
	 * 
	 */
	public Enumeration<Thread> getThreads() {
		return threads.keys();
	}

	/**
	 * ��ȡ��ǰ�߳�����
	 * @return ��ǰ�߳���
	 */
	public int getCurrentThreadCount() {
		return currentThreadCount;
	}

	/**
	 * ��ȡ��ǰ��æ(����)���߳���
	 * @return ��ǰ��æ(����)���߳���
	 */
	public int getCurrentThreadsBusy() {
		return currentThreadsBusy;
	}

	/**
	 * ��ȡ�Ƿ�Ϊ�ػ��̡߳�
	 * @return �Ƿ�Ϊ�ػ��߳�
	 */
	public boolean isDaemon() {
		return isDaemon;
	}

	public static int getDebug() {
		return 0;
	}
	
}
