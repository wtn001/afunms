/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */ 


package com.afunms.common.util.logging;

/**
 * <p>һ���򵥵���־��¼ API �ĳ���ӿ�.  Ϊ���ܹ�����־���� {@ LogFactory }
 * ���ɹ�ʵ������־, ��������ʵ�ָýӿڣ����ұ����д�һ���ַ��� {@ String} 
 * ��Ϊ�����Ĺ��췽�������ַ������ڸ������־��ʵ��������</p>
 *
 * <p> ����Ϊ <code>Log</code> �������ȼ�:
 * <ol>
 * <li>׷�� trace (the least serious)</li>
 * <li>���� debug</li>
 * <li>��Ϣ info</li>
 * <li>���� warn</li>
 * <li>���� error</li>
 * <li>���� fatal (the most serious)</li>
 * </ol>
 * ��Щ��־�ȼ��ĸ����Ƿ��Ӧ����Ҫ�����ڵײ���־ϵͳ��ʵ�֡�
 * ��ȻԤ������������ģ����ǻ�����Ҫʵ������֤.</p>
 *
 * <p>���������Ǽ�¼�ȽϹ�ע��.
 * ͨ������ʵ�������, һ����������ܱ���һЩ����Ĳ���(�����¼������Ϣ�Ļ�).</p>
 *
 * <p> ����,
 * <code><pre>
 *    if (log.isDebugEnabled()) {
 *        ... do something expensive ...
 *        log.debug(theResult);
 *    }
 * </pre></code>
 * </p>
 *
 * <p>�ײ����־ϵͳһ�㶼�����ⲿ����־ API ����������, Ȼ��ͨ��ĳ�ֻ�����֧�ָ�ϵͳ</p>
 *
 * @author <a href="mailto:nielin@dhcc.com.cn">����</a>
 * @version $Revision: 1.0 $ $Date: 2011-03-01 15:48:25 +0100 (Tue, 01 Mar 2011) $
 */
public interface Log {


    // ----------------------------------------------------- Logging Properties


    /**
     * <p> ��ǰ������־�Ƿ�����</p>
     *
     * <p> ����־�ļ��𳬹����Լ��𣬵��ø÷������Լ���һЩ����Ҫ�Ĳ����� </p>
     */
    public boolean isDebugEnabled();


    /**
     * <p> ��ǰ������־�Ƿ�����</p>
     *
     * <p> ����־�ļ��𳬹����󼶱𣬵��ø÷������Լ���һЩ����Ҫ�Ĳ����� </p>
     */
    public boolean isErrorEnabled();


    /**
     * <p> ��ǰ������־�Ƿ�����</p>
     *
     * <p> ����־�ļ��𳬹��������𣬵��ø÷������Լ���һЩ����Ҫ�Ĳ����� </p>
     */
    public boolean isFatalEnabled();


    /**
     * <p> ��ǰ��Ϣ��־�Ƿ�����</p>
     *
     * <p> ����־�ļ��𳬹���Ϣ���𣬵��ø÷������Լ���һЩ����Ҫ�Ĳ����� </p>
     */
    public boolean isInfoEnabled();


    /**
     * <p> ��ǰ׷����־�Ƿ�����</p>
     *
     * <p> ����־�ļ��𳬹�׷�ټ��𣬵��ø÷������Լ���һЩ����Ҫ�Ĳ����� </p>
     */
    public boolean isTraceEnabled();


    /**
     * <p> ��ǰ������־�Ƿ�����</p>
     *
     * <p> ����־�ļ��𳬹����漶�𣬵��ø÷������Լ���һЩ����Ҫ�Ĳ����� </p>
     */
    public boolean isWarnEnabled();


    // -------------------------------------------------------- Logging Methods


    /**
     * <p> ����־��׷�ٵȼ�����¼��Ϣ </p>
     *
     * @param message log this message
     */
    public void trace(Object message);


    /**
     * <p> ����־��׷�ٵȼ�����¼���� </p>
     *
     * @param message log this message
     * @param t log this cause
     */
    public void trace(Object message, Throwable t);


    /**
     * <p> ����־�ĵ��Եȼ�����¼��Ϣ </p>
     *
     * @param message log this message
     */
    public void debug(Object message);


    /**
     * <p> ����־�ĵ��Եȼ�����¼���� </p>
     *
     * @param message log this message
     * @param t log this cause
     */
    public void debug(Object message, Throwable t);


    /**
     * <p> ����־����Ϣ�ȼ�����¼��Ϣ </p>
     *
     * @param message log this message
     */
    public void info(Object message);


    /**
     * <p> ����־����Ϣ�ȼ�����¼���� </p>
     *
     * @param message log this message
     * @param t log this cause
     */
    public void info(Object message, Throwable t);


    /**
     * <p> ����־�ĸ澯�ȼ�����¼��Ϣ </p>
     *
     * @param message log this message
     */
    public void warn(Object message);


    /**
     * <p> ����־�ĸ澯�ȼ�����¼���� </p>
     *
     * @param message log this message
     * @param t log this cause
     */
    public void warn(Object message, Throwable t);


    /**
     * <p> ����־�Ĵ���ȼ�����¼��Ϣ </p>
     *
     * @param message log this message
     */
    public void error(Object message);


    /**
     * <p> ����־�Ĵ���ȼ�����¼���� </p>
     *
     * @param message log this message
     * @param t log this cause
     */
    public void error(Object message, Throwable t);


    /**
     * <p> ����־�������ȼ�����¼��Ϣ </p>
     *
     * @param message log this message
     */
    public void fatal(Object message);


    /**
     * <p> ����־�������ȼ�����¼���� </p>
     *
     * @param message log this message
     * @param t log this cause
     */
    public void fatal(Object message, Throwable t);


}
