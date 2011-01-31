/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.jboss.seam.conversation.spi;

import javax.servlet.http.HttpServletRequest;

/**
 * Manage Seam Conversation context.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public interface SeamConversationContext
{
   /**
    * Associate request with conversation context.
    *
    * @param request the current request
    * @return the flowing current SeamConversationContext instance
    */
   SeamConversationContext associate(HttpServletRequest request);

   /**
    * Activate conversation with given id.
    *
    * @param conversationId the conversation id to activate
    * @return the flowing current SeamConversationContext instance
    */
   SeamConversationContext activate(String conversationId);

   /**
    * Invalidate conversation context.
    *
    * @return the flowing current SeamConversationContext instance
    */
   SeamConversationContext invalidate();

   /**
    * Deactivate conversation context(s).
    *
    * @return the flowing current SeamConversationContext instance
    */
   SeamConversationContext deactivate();

   /**
    * Dissociate request with conversation context.
    *
    * @param request the current request
    * @return the flowing current SeamConversationContext instance
    */
   SeamConversationContext dissociate(HttpServletRequest request);
}
