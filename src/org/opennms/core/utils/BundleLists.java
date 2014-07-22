//
// This file is part of the OpenNMS(R) Application.
//
// OpenNMS(R) is Copyright (C) 2002-2003 The OpenNMS Group, Inc.  All rights reserved.
// OpenNMS(R) is a derivative work, containing both original code, included code and modified
// code that was published under the GNU General Public License. Copyrights for modified 
// and included code are below.
//
// OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
// 
// Copyright (C) 1999-2001 Oculan Corp.  All rights reserved.
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
//      OpenNMS Licensing       <license@opennms.org>
//      http://www.opennms.org/
//      http://www.opennms.com/
//

package org.opennms.core.utils;

import java.util.StringTokenizer;

/**
 * Contains utility functions for handling with <em>bundle lists</em>, that
 * is, a comma-delimited list of strings that are contained as one line in a
 * Java properties file. This class contains methods to parse and create these
 * bundle lists.
 * 
 * @author <A HREF="mailto:larry@opennms.org">Lawrence Karnowski </A>
 * @author <A HREF="http://www.opennms.org/">OpenNMS </A>
 */
public class BundleLists extends Object {

    /**
     * Parses a string into an array of substrings, using a comma as a delimiter
     * and trimming whitespace.
     * 
     * @param list
     *            The list formatted as a <code>delimeter</code> -delimited
     *            string.
     * @return The list formatted as an array of strings.
     */
    public static String[] parseBundleList(String list) {
        return parseBundleList(list, ",");
    }

    /**
     * Parses a string into an array of substrings, using the specified
     * delimeter and trimming whitespace.
     * 
     * @param list
     *            The list formatted as a <code>delimeter</code> -delimited
     *            string.
     * @param delimiter
     *            The delimeter.
     * @return The list formatted as an array of strings.
     */
    public static String[] parseBundleList(String list, String delimiter) {
        if (list == null || delimiter == null) {
            throw new IllegalArgumentException("Cannot take null parameters.");
        }

        String[] strings = new String[0];

        StringTokenizer tokenizer = new StringTokenizer(list, delimiter, false);

        int stringCount = tokenizer.countTokens();
        strings = new String[stringCount];

        for (int i = 0; i < stringCount; i++) {
            strings[i] = tokenizer.nextToken().trim();
        }

        return (strings);
    }

    /**
     * Parses a Object array and puts them into a array of substrings, using a
     * comma as a delimiter
     * 
     * @param objArray
     *            The object array to be formatted as a comma-delimited string.
     * @return The comma-delimited string.
     */
    public static String createBundleList(Object[] objArray) {
        return createBundleList(objArray, ", ");
    }

    /**
     * Parses a Object array and puts them into a array of substrings, using a
     * comma as a delimiter
     * 
     * @param objArray
     *            The object array to be formatted as a comma-delimited string.
     * @return The comma-delimited string.
     */
    public static String createBundleList(Object[] objArray, String delimiter) {
        if (objArray == null || delimiter == null) {
            throw new IllegalArgumentException("Cannot take null parameters.");
        }

        StringBuffer strings = new StringBuffer();

        for (int i = 0; i < objArray.length; i++) {
            strings.append(objArray[i].toString());

            if (i < objArray.length - 1) {
                strings.append(delimiter);
            }
        }

        return (strings.toString());
    }
}
