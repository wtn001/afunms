//
// This file is part of the OpenNMS(R) Application.
//
// OpenNMS(R) is Copyright (C) 2005 The OpenNMS Group, Inc.  All rights reserved.
// OpenNMS(R) is a derivative work, containing both original code, included code and modified
// code that was published under the GNU General Public License. Copyrights for modified 
// and included code are below.
//
// OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
//
// Modifications:
//
// 2007 Jun 23: Use Java 5 generics to eliminate warnings. - dj@opennms.org
//
// Original code base Copyright (C) 1999-2001 Oculan Corp.  All rights reserved.
//
// This program is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 2 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
//
// For more information contact:
// OpenNMS Licensing       <license@opennms.org>
//     http://www.opennms.org/
//     http://www.opennms.com/
//
package org.opennms.netmgt.snmp;

import java.net.InetAddress;
import java.util.Map;
import java.util.TreeMap;


public abstract class AbstractSnmpStore {

    private Map<String, SnmpValue> m_responseMap = new TreeMap<String, SnmpValue>();
    public static final String IFINDEX = "ifIndex";
    public abstract void storeResult(SnmpObjId base, SnmpInstId inst, SnmpValue val);

    public AbstractSnmpStore() {
    }

    public Integer getInt32(String key) {
        SnmpValue val = getValue(key);
        return (val == null ? null : new Integer(val.toInt()));
    }

    public Long getUInt32(String key) {
        SnmpValue val = getValue(key);
        return (val == null ? null : new Long(val.toLong()));
    }

    public String getDisplayString(String key) {
        SnmpValue val = getValue(key);
        return (val == null ? null : val.toDisplayString());
    }

    public String getHexString(String key) {
        SnmpValue val = getValue(key);
        return (val == null ? null : val.toHexString());
    }

    public InetAddress getIPAddress(String key) {
        SnmpValue val = getValue(key);
        return (val == null ? null : val.toInetAddress());
    }

    public String getObjectID(String key) {
        return (getValue(key) == null ? null : getValue(key).toString());
    }

    public SnmpValue getValue(String key) {
        return m_responseMap.get(key);
    }

    protected void putValue(String key, SnmpValue value) {
        m_responseMap.put(key, value);
    }

    public Integer getIfIndex() {
        return getInt32(IFINDEX);
    }

    protected void putIfIndex(int ifIndex) {
        putValue(IFINDEX, SnmpUtils.getValueFactory().getInt32(ifIndex));
    }
    
    public int size() {
        return m_responseMap.size();
    }
    
    public boolean isEmpty() {
        return m_responseMap.isEmpty();
    }


}
