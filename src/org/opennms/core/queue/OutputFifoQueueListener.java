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
//
// Tab Size = 8
//
//

package org.opennms.core.queue;

/**
 * <p>
 * This interface is implemented by objects that need to be notified when a new
 * element is removed from a queue. The notification method is invoked aftet the
 * element is removed to the queue, the exact semantics of which are defined by
 * the queue.
 * </p>
 * 
 * <p>
 * This is most often used to notify a object that a full queue has new space
 * for more elements to be added. This allows the object to perform other
 * potentially useful work while waiting on new queue elements.
 * </p>
 * 
 * @author <a href="mailto:weave@oculan.com">Brian Weaver </a>
 * @author <a href="http://www.opennms.org">OpenNMS </a>
 * 
 */
public interface OutputFifoQueueListener {
    /**
     * This method is invoked by a {@link NotifiableOutputFifoQueue queue}when
     * an element has been removed from the queue.
     * 
     * @param queue
     *            The queue where an element was removed.
     */
    public void onQueueOutput(NotifiableOutputFifoQueue queue);
}
