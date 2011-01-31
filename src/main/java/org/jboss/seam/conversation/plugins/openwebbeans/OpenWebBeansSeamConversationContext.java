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

package org.jboss.seam.conversation.plugins.openwebbeans;

import javax.servlet.http.HttpServletRequest;

import org.jboss.seam.conversation.plugins.AbstractSeamConversationContext;

import org.apache.webbeans.conversation.ConversationManager;

/**
 * OpenWebBeans based Seam conversation context.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class OpenWebBeansSeamConversationContext extends AbstractSeamConversationContext
{
   protected void doAssociate(HttpServletRequest request)
   {
      ConversationManager manager = ConversationManager.getInstance();
   }

   protected void doActivate(String conversationId)
   {
      //To change body of implemented methods use File | Settings | File Templates.
   }

   protected void doInvalidate()
   {
      //To change body of implemented methods use File | Settings | File Templates.
   }

   protected void doDeactivate()
   {
      //To change body of implemented methods use File | Settings | File Templates.
   }

   protected void doDissociate(HttpServletRequest request)
   {
      //To change body of implemented methods use File | Settings | File Templates.
   }
}
