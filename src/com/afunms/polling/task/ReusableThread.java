/*
 * Created on 2005-3-28
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.afunms.polling.task;

import java.util.LinkedList;

public class ReusableThread implements Runnable {
	  //�߳�״̬�����߽ӿ�
	    public interface ThreadStateListener {
	        public abstract void onRunOver(ReusableThread thread);//���û�����ִ����Ϻ���õķ���
	    }
	    
	    public static final byte STATE_READY=0; //�߳���׼���ã��ȴ���ʼ�û�����
	    public static final byte STATE_STARTED=1; //�û�����������
	    public static final byte STATE_DESTROYED=2; //�߳���������
	    
	    byte mState; //��ʾ�������̵߳ĵ�ǰ״̬
	    
	    Thread mThread; //ʵ�ʵ����̶߳���
	    Runnable mProc; //�û����̵�run()����������mProc��
	    ThreadStateListener mListener; //״̬�����ߣ�����Ϊnull
	    
	    /** Creates a new instance of ReusableThread */
	    public ReusableThread(Runnable proc) {
	        mProc = proc;
	        mListener = null;
	        mThread = new Thread(this);
	        mState = STATE_READY;
	    }
	    
	    public byte getState() {return mState;}
	    
	    public void setStateListener(ThreadStateListener listener) {
	        mListener = listener;
	    }
	    
	    /**�����ڴ��ڵȴ�״̬ʱ���ø÷��������û�����*/
	    public synchronized boolean setProcedure(Runnable proc) {
	        if (mState == STATE_READY) {
	            mProc = proc;
	            return true;
	        }
	        else
	            return false;
	    }
	    
	    /**��ʼִ���û�����*/
	    public synchronized boolean start() {
	        if (mState == STATE_READY) {
	            mState = STATE_STARTED;
	            if (!mThread.isAlive()) mThread.start();
	            notify(); //�������û�����ִ�н���������ȴ��е����߳�
	            return true;
	        }
	        else
	            return false;
	    }
	    
	    /**���������̣߳��������̶߳���֮�󽫲����ٴ�����*/
	    public synchronized void destroy() {
	        mState = STATE_DESTROYED;
	        notify();
	        mThread = null;
	    }
	    
	    public void run() {
	        while (true) {
	            synchronized (this) {
	                try {
	                    while (mState != STATE_STARTED) {
	                        if (mState == STATE_DESTROYED) return;
	                        wait();
	                    }
	                } catch(Exception e) {e.printStackTrace();}
	            }
	            
	            if (mProc != null) mProc.run();
	            if (mListener != null) mListener.onRunOver(this); //���û����̽�����ִ�м����ߵ�onRunOver����
	            
	            synchronized (this) {
	                if (mState == STATE_DESTROYED) return;
	                mState = STATE_READY;
	            }
	        }
	    }
	    
	}
    

