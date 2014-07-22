/*
 * @(#)PerformanceInfoService.java     v1.01, Jan 11, 2013
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.node.service;

import java.util.List;

import com.afunms.node.dao.PerformaceInfoTableDao;
import com.afunms.node.model.PerformanceInfo;

/**
 * ClassName:   PerformanceInfoService.java
 * <p>
 *
 * @author      ����
 * @version     v1.01
 * @since       v1.01
 * @Date        Jan 11, 2013 5:42:45 PM
 */
public class PerformanceInfoService {

    /**
     * getPerformance:
     * <p>��ȡ������Ϣ
     *
     * @param table
     * @param startTime
     * @param endTime
     * @return
     *
     * @since   v1.01
     */
    public List<PerformanceInfo> getPerformance(String table, String startTime, String endTime) {
        List<PerformanceInfo> list = null;
        PerformaceInfoTableDao dao = new PerformaceInfoTableDao(table);
        try {
            list = dao.findByCollectTime(startTime, endTime);
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            dao.close();
        }
        return list;
    }

    /**
     * save:
     * <p>����������Ϣ
     *
     * @param   table
     *          - ������Ϣ��
     * @param   info
     *          - ������Ϣ
     * @return  {@link Boolean}
     *          - �������ɹ����򷵻� <code>true</code> , ���򷵻� <code>false</code>
     *
     * @since   v1.01
     */
    public boolean save(String table, PerformanceInfo info) {
        boolean result = false;
        PerformaceInfoTableDao dao = new PerformaceInfoTableDao(table);
        try {
            result = dao.save(info);
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            dao.close();
        }
        return result;
    }
}

