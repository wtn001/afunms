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


import java.util.Properties;
import java.util.logging.LogManager;



/**
 * Modified LogFactory: removed all discovery, hardcode a specific implementation
 * If you like a different logging implementation - use either the discovery-based
 * commons-logging, or better - another implementation hardcoded to your favourite
 * logging impl.
 * 
 * Why ? Each application and deployment can choose a logging implementation - 
 * that involves configuration, installing the logger jar and optional plugins, etc.
 * As part of this process - they can as well install the commons-logging implementation
 * that corresponds to their logger of choice. This completely avoids any discovery
 * problem, while still allowing the user to switch. 
 * 
 * Note that this implementation is not just a wrapper arround JDK logging ( like
 * the original commons-logging impl ). It adds 2 features - a simpler configuration
 * ( which is in fact a subset of log4j.properties ) and a formatter that is 
 * less ugly.   
 * 
 * The removal of 'abstract' preserves binary backward compatibility. It is possible
 * to preserve the abstract - and introduce another ( hardcoded ) factory - but I 
 * see no benefit. 
 * 
 * Since this class is not intended to be extended - and provides
 * no plugin for other LogFactory implementation - all protected methods are removed.
 * This can be changed - but again, there is little value in keeping dead code.
 * Just take a quick look at the removed code ( and it's complexity)  
 * 
 * --------------
 * 
 * Original comment:
 * <p>Factory for creating {@link Log} instances, with discovery and
 * configuration features similar to that employed by standard Java APIs
 * such as JAXP.</p>
 * 
 * <p><strong>IMPLEMENTATION NOTE</strong> - This implementation is heavily
 * based on the SAXParserFactory and DocumentBuilderFactory implementations
 * (corresponding to the JAXP pluggability APIs) found in Apache Xerces.</p>
 * 
 *
 * @author ����
 * @version $Revision: 1.0 $ $Date: 2011-03-01 11:39:25 +0100 (Tue, 01 Mar 2011) $
 */
public /* abstract */ class LogFactory {

    // ----------------------------------------------------- Manifest Constants

    /**
     * ���������ڱ�ʶ LogFactory ʵ���������
     */
    public static final String FACTORY_PROPERTY =
        "org.apache.commons.logging.LogFactory";

    /**
     * Ĭ�ϵ� LogFactory ���ʵ���࣬���û���ҵ�ָ�����࣬��ʹ�ø��ࡣ
     */
    public static final String FACTORY_DEFAULT =
        "org.apache.commons.logging.impl.LogFactoryImpl";

    /**
     * �����ļ�����
     */
    public static final String FACTORY_PROPERTIES =
        "commons-logging.properties";
    
    /**
     * <p>Setting this system property value allows the <code>Hashtable</code> used to store
     * classloaders to be substituted by an alternative implementation.
     * </p>
     * <p>
     * <strong>Note:</strong> <code>LogFactory</code> will print:
     * <code><pre>
     * [ERROR] LogFactory: Load of custom hashtable failed</em>
     * </code></pre>
     * to system error and then continue using a standard Hashtable.
     * </p>
     * <p>
     * <strong>Usage:</strong> Set this property when Java is invoked
     * and <code>LogFactory</code> will attempt to load a new instance 
     * of the given implementation class.
     * For example, running the following ant scriplet:
     * <code><pre>
     *  &lt;java classname="${test.runner}" fork="yes" failonerror="${test.failonerror}"&gt;
     *     ...
     *     &lt;sysproperty 
     *        key="org.apache.commons.logging.LogFactory.HashtableImpl"
     *        value="org.apache.commons.logging.AltHashtable"/&gt;
     *  &lt;/java&gt;
     * </pre></code>
     * will mean that <code>LogFactory</code> will load an instance of
     * <code>org.apache.commons.logging.AltHashtable</code>.
     * </p>
     * <p>
     * A typical use case is to allow a custom
     * Hashtable implementation using weak references to be substituted.
     * This will allow classloaders to be garbage collected without
     * the need to release them (on 1.3+ JVMs only, of course ;)
     * </p>
     */
    public static final String HASHTABLE_IMPLEMENTATION_PROPERTY =
        "org.apache.commons.logging.LogFactory.HashtableImpl";
    
    private static LogFactory singleton = new LogFactory();

    Properties logConfig;
    
    // ----------------------------------------------------------- Constructors


    /**
     * ˽�еĹ��췽�����ڵ���ģʽ (Singleton)
     */
    private LogFactory() {
        logConfig = new Properties();
    }
    
    // hook for syserr logger - class level
    void setLogConfig( Properties p ) {
        this.logConfig=p;
    }
    // --------------------------------------------------------- Public Methods

    // ʹ�ò�ͬ�ļ�¼����ֻ��Ҫ�ı��������������Ϳ����ˡ�
    
    /**
     * <p>ʹ�õ�ǰ��־�������������ã�������(������Ҫ)������һ����־ <code>Log</code>
     * ��ʵ����</p>
     *
     * <p><strong>ע��</strong> - ������ʹ�õ���־���� <code>LogFactory</code>
     * ��ʵ��, �㷵�ص���־ <code>Log</code> ʵ��������Ҳ���ܲ����ڵ�ǰӦ�ó�����, 
     * ���ҿ��ܻ�Ҳ���ܲ���������ٴε�����ʹ��ͬһ����ͬ���� <code>name</code> ������</p>
     *
     * @param name ��Ҫ������־ <code>Log</code> ʵ�����߼�����(�����ζ�ţ�
     * �����Ʊ��ײ���־��ʵ����ʶ��)��������Ϊ��־ <code>Log</code> ����ȫ�޶�����
     *
     * @exception LogConfigurationException ���û�к��ʵ���־ <code>Log</code>
     * ʵ���������׳����쳣
     */
    public Log getInstance(String name) throws LogConfigurationException {
        return DirectJDKLog.getInstance(name);
    }


    /**
     * �ͷ���Դ
     * �ͷŵ�ǰ��������������ص���־ {@link Log} ���������κ��ڲ������á�
     * ����ڱ�ʵ�ֵ�Ӧ�ó��������ֱ�������� <code>ClassLoader</code> 
     * ���¼��صĻ����У��Ƿǳ����õģ������� <code>servlet</code> �����С�
     */
    public void release() {
        DirectJDKLog.release();
    }

    /**
     * �����ƶ������� <code>name</code> ���������õ����ԣ����û������������
     * �򷵻� <code>null</code> .
     *
     * @param name Name of the attribute to return
     */
    public Object getAttribute(String name) {
        return logConfig.get(name);
    }


    /**
     * ����һ�����飬������������е�ǰ�������õ��������ơ� 
     * ���û�����ԣ��򷵻�һ������Ϊ������顣
     */
    public String[] getAttributeNames() {
        String result[] = new String[logConfig.size()];
        return logConfig.keySet().toArray(result);
    }

    /**
     * ɾ���ƶ����� <code>name</code> ���������ԡ�
     * ���û�и����ԣ��򲻷������еĲ�����
     *
     * @param name ��Ҫɾ�����Ե�����
     */
    public void removeAttribute(String name) {
        logConfig.remove(name);
     }   


    /**
     * 
     * ����ָ�����Ƶ��������ԡ�  ���ø÷�����ֵ���Ϊ�� <code>null</code>��
     * ���൱�ڵ��� <code>removeAttribute(name)</code> ������
     *  
     * @param name ��Ҫ�������Ե�����
     * @param value ��Ҫ�������Ե�ֵ�����ֵΪ�� <code>null</code>
     * ��ɾ��������Ե��κ����á�
     */
    public void setAttribute(String name, Object value) {
        logConfig.put(name, value);
    }


    /**
     * һ�������ͬ��������ͨ��ָ������ <code>Class</code> �������������ƣ���ͨ��
     * ���� <code>getInstance(String)</code> �������õ���־ <code>Log</code>
     * ʵ����
     *
     * @param clazz ����һ�����ʵĻ�ȡ��־ <code>Log</code> ���Ƶ���
     * <code>Class</code>
     *
     * @exception LogConfigurationException ������ܷ���һ�����ʵ�
     * ��־ <code>Log</code> ��ʵ��ʱ�׳�
     */
    public Log getInstance(Class clazz) throws LogConfigurationException {
        return getInstance( clazz.getName());
    }


    


    // ------------------------------------------------------- ��̬����



    // --------------------------------------------------------- ��̬����


    /**
     * <p>���첢����һ����־���� <code>LogFactory</code> ��ʵ����ʹ�ò��ҳ���
     * ȷ������ʵ��������������ء�</p>
     * <ul>
     * <li>ϵͳ�����ļ��е� <code>org.apache.commons.logging.LogFactory</code> </li>
     * <li>JDK 1.3 ��֧��</li>
     * <li>������������ļ� <code>commons-logging.properties</code> ��·������ʹ��
     *	   �������ļ����������ļ��ǰ��ձ�׼�� <code>java.util.Properties</code> �ĸ�
     *     ʽ������ϵͳ�����ж����˼������Ӧʵ�������ȫ�޶�����
     * <li>���û����ʹ��Ĭ�ϵ�ʵ����
     *     (<code>org.apache.commons.logging.impl.LogFactoryImpl</code>).</li>
     * </ul>
     *
     * <p><em>ע��</em> - �����־���� <code>LogFactory</code> ��ʵ����������ö���
     * �����ļ��ķ������������������ļ��ж������������Ӧ����־���� <code>LogFactory</code> 
     * ʵ����Ӧ�������ó����õ����ԡ�</p>
     *
     * @exception LogConfigurationException ���ʵ���಻���û����ǲ���ʵ�����׳�
     */
    public static LogFactory getFactory() throws LogConfigurationException {
        return singleton;
    }


    /**
     * һ������ķ�����־ <code>Log</code> ��ͬ���������÷��������������
     * ���������Ӧ�õġ�
     *
     * @param clazz ���ڻ�ȡ��־���Ƶ��� <code>Class</code>
     *
     * @exception LogConfigurationException ������ܷ���һ�����ʵ���־ 
     * <code>Log</code> ��ʵ����
     */
    public static Log getLog(Class clazz) throws LogConfigurationException {
        return (getFactory().getInstance(clazz));
    }


    /**
     * һ������ķ�����־ <code>Log</code> ��ͬ���������÷��������������
     * ���������Ӧ�õġ�
     *
     * @param name ��Ҫ������־ <code>Log</code> ʵ�����߼�����(�����ζ�ţ�
     * �����Ʊ��ײ���־��ʵ����ʶ��)��������Ϊ��־ <code>Log</code> ����ȫ�޶�����
     *
     * @exception LogConfigurationException ������ܷ���һ�����ʵ���־ 
     * <code>Log</code> ��ʵ����
     */
    public static Log getLog(String name) throws LogConfigurationException {
        return (getFactory().getInstance(name));
    }


    /**
     * �ͷŵ�ǰ����ָ����ļ�������������־���� {@link LogFactory} ���������κ��ڲ��ӿڣ�
     * ������ÿ��ʵ���� <code>release()</code> ������
     *
     * @param classLoader ��Ҫ�ͷŵ���־���� <code>LogFactory</code> �� {@link ClassLoader}
     */
    public static void release(
            @SuppressWarnings("unused") ClassLoader classLoader) {
        // JULI's log manager looks at the current classLoader
        LogManager.getLogManager().reset();
    }


    /**
     * �ͷŵ�ǰ������־���� {@link LogFactory} ʵ�����������κ��ڲ��ӿڣ�Ȼ�����ÿ��ʵ��
     * �� <code>release()</code> ����������ڱ�ʵ�ֵ�Ӧ�ó�������
     * �ֱ�������� <code>ClassLoader</code> ���¼��صĻ����У��Ƿǳ����õģ�
     * ������ <code>servlet</code> �����С�
     */
    public static void releaseAll() {
        singleton.release();
    }

    /**
     * ����һ���ַ��������ַ�����ָ�������Ψһ��ʾ��
     * 
     * <p>���ص��ַ����ĸ�ʽΪ��"classname@hashcode" ������ <code>Object.toString()</code>
     * �������ص�ֵһ���������ڹ�����ָ����������������д <code>toString</code> ����</p>
     * 
     * @param o ָ���Ķ��󣬿���Ϊ�� <code>null<code>.
     * @return string һ����ʽΪ <code>classname@hashcode</code> ���ַ���, 
     * ���ָ���Ķ���Ϊ�գ��򷵻ؿ� <code>null<code>.
     */
    public static String objectId(Object o) {
        if (o == null) {
            return "null";
        } else {
            return o.getClass().getName() + "@" + System.identityHashCode(o);
        }
    }
}
