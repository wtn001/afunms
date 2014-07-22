/*
 * This file is part of the OpenNMS(R) Application.
 *
 * OpenNMS(R) is Copyright (C) 2007 The OpenNMS Group, Inc.  All rights reserved.
 * OpenNMS(R) is a derivative work, containing both original code, included code and modified
 * code that was published under the GNU General Public License. Copyrights for modified
 * and included code are below.
 *
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
 *
 * Modifications:
 * 
 * Created: August 17, 2007
 *
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * For more information contact:
 *      OpenNMS Licensing       <license@opennms.org>
 *      http://www.opennms.org/
 *      http://www.opennms.com/
 */
package org.opennms.core.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 
 * @author <a href="mailto:ranger@opennms.org">Benjamin Reed</a>
 */
public class CollectionMath {

	/**
	 * Get the number of null entries in a {@link List}
	 * @param list the {@link List}
	 * @return the number of null entries
	 */
	@SuppressWarnings("unchecked")
	public static long countNull(List list) {
		long count = 0;
		for (Object entry : list) {
			if (entry == null) {
				count++;
			}
		}
		return count;
	}

	/**
	 * Get the number of non-null entries in a {@link List}
	 * @param list the list
	 * @return the number of non-null entries
	 */
	@SuppressWarnings("unchecked")
	public static long countNotNull(List list) {
		return (list.size() - countNull(list));
	}

	/**
	 * Get the percentage of null entries in a {@link List} of {@link Number} values
	 * @param list the {@link List} of {@link Number} values
	 * @return the percentage of null values as a {@link Number} value
	 */
	public static Number percentNull(List<Number> list) {
		return percentNull(convertNumberToBigDecimal(list));
	}

	/**
	 * Get the percentage of null entries in a {@link List} of {@link BigDecimal} values
	 * @param list the {@link List} of {@link BigDecimal} values
	 * @return the percentage of null values as a {@link BigDecimal} value
	 */
	public static BigDecimal percentNull(List<BigDecimal> list) {
		if (list.size() > 0) {
			return new BigDecimal(countNull(list)).divide(new BigDecimal(list.size()), 16, BigDecimal.ROUND_HALF_EVEN).multiply(new BigDecimal(100));
		} else {
			return null;
		}
	}
	
	/**
	 * Get the percentage of not-null entries in a {@link List} of {@link Number} values
	 * @param list the {@link List} of {@link Number} values
	 * @return the percentage of not-null values as a {@link Number} value
	 */
	public static Number percentNotNull(List<Number> list) {
		return percentNotNull(convertNumberToBigDecimal(list));
	}
	
	/**
	 * Get the percentage of not-null entries in a {@link List} of {@link BigDecimal} values
	 * @param list the {@link List} of {@link BigDecimal} values
	 * @return the percentage of not-null values as a {@link BigDecimal} value
	 */
	public static BigDecimal percentNotNull(List<BigDecimal> list) {
		if (list.size() > 0) {
			return new BigDecimal(countNotNull(list)).divide(new BigDecimal(list.size()), 16, BigDecimal.ROUND_HALF_EVEN).multiply(new BigDecimal(100));
		} else {
			return null;
		}
	}

	/**
	 * Get the average of the contents of a {@link List} of {@link Number} values, excluding null entries
	 * @param list the {@link List} of {@link Number} values
	 * @return the average of the not-null values as a {@link Number} value
	 */
	public static Number average(List<Number> list) {
		return average(convertNumberToBigDecimal(list));
	}
	
	/**
	 * Get the average of the contents of a {@link List} of {@link BigDecimal} values, excluding null entries
	 * @param list the {@link List} of {@link BigDecimal} values
	 * @return the average of the not-null values as a {@link BigDecimal} value
	 */
	public static BigDecimal average(List<BigDecimal> list) {
		BigDecimal total = new BigDecimal(0);
		List<BigDecimal> notNullEntries = getNotNullEntries(list);
		if (notNullEntries.size() == 0) {
			return null;
		}
		
		for (BigDecimal entry : notNullEntries) {
			total = total.add(entry);
		}
		
		return total.divide(new BigDecimal(notNullEntries.size()), 16, BigDecimal.ROUND_HALF_EVEN);
	}
	
	/**
	 * Get the median of the contents of a {@link List} of {@link Number} values, excluding null entries
	 * @param list the {@link List} of {@link Number} values
	 * @return the median of the not-null values as a {@link Number} value
	 */
	public static Number median(List<Number> list) {
		return median(convertNumberToBigDecimal(list));
	}
	
	/**
	 * Get the median of the contents of a {@link List} of {@link BigDecimal} values, excluding null entries
	 * @param list the {@link List} of {@link BigDecimal} values
	 * @return the median of the not-null values as a {@link BigDecimal} value
	 */
	public static BigDecimal median(List<BigDecimal> list) {
		List<BigDecimal> notNullEntries = getNotNullEntries(list);
		Collections.sort(notNullEntries);
		
		if (notNullEntries.size() == 0) {
		    return null;
		}
		
		if (notNullEntries.size() % 2 == 0) {
			// even number of entries, take the mean of the 2 center ones
			BigDecimal value1, value2;
		    value1 = notNullEntries.get(notNullEntries.size() / 2);
		    value2 = notNullEntries.get((notNullEntries.size() / 2) - 1);
			return value1.add(value2).divide(new BigDecimal(2), 16, BigDecimal.ROUND_HALF_EVEN);
		} else {
			return notNullEntries.get(notNullEntries.size() / 2);
		}
	}
	
	/**
	 * Utility method, converts a {@link Number} {@link List} into a {@link BigDecimal} {@link List}
	 * @param list a {@link List} of {@link Number} values
	 * @return a {@link List} of {@link BigDecimal} values
	 */
	private static List<BigDecimal> convertNumberToBigDecimal(List<Number> c) {
		List<BigDecimal> bd = new ArrayList<BigDecimal>();
		for (Number entry : c) {
		    if (entry != null) {
		        bd.add(new BigDecimal(entry.doubleValue()));
		    }
		}
		return bd;
	}
	
	/**
	 * Utility method, gets not-null-entries from a {@link List}
	 * @param list the {@link List} to search
	 * @return an {@link ArrayList} of not-null values (if any)
	 */
	private static List<BigDecimal> getNotNullEntries(List<BigDecimal> list) {
		List<BigDecimal> s = new ArrayList<BigDecimal>();
		for (BigDecimal entry : list) {
			if (entry != null) {
				s.add(entry);
			}
		}
		return s;
	}
	
}
