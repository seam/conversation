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

import javax.enterprise.context.Conversation;

import java.util.Map;

import org.apache.webbeans.context.ConversationContext;
import org.apache.webbeans.conversation.ConversationImpl;
import org.apache.webbeans.conversation.ConversationManager;

/**
 * OpenWebBeans based Seam conversation manager.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
class OpenWebBeansSeamConversationManager
{
   static void doActivate(String conversationId, String sessionId)
   {
      ConversationManager manager = ConversationManager.getInstance();
      if (manager.isConversationExistWithGivenId(conversationId) == false)
      {
         Conversation conversation = new ConversationImpl(sessionId);
         manager.addConversationContext(conversation, null);
      }
   }

   static void doInvalidate()
   {
      ConversationManager manager = ConversationManager.getInstance();
      manager.destroyWithRespectToTimout();
   }

   static void doDeactivate(String sessionId)
   {
      ConversationManager manager = ConversationManager.getInstance();
      Map<Conversation, ConversationContext> map = manager.getConversationMapWithSessionId(sessionId);
      for (Map.Entry<Conversation, ConversationContext> entry : map.entrySet())
      {
         Conversation conversation = entry.getKey();
         if (conversation.isTransient())
            entry.getValue().destroy();
         manager.removeConversation(conversation);
      }
   }
}
