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

import javax.enterprise.context.BusyConversationException;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.NonexistentConversationException;

import org.apache.webbeans.config.WebBeansContext;
import org.apache.webbeans.context.ContextFactory;
import org.apache.webbeans.context.ConversationContext;
import org.apache.webbeans.conversation.ConversationImpl;
import org.apache.webbeans.conversation.ConversationManager;

/**
 * OpenWebBeans based Seam conversation manager.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
class OpenWebBeansSeamConversationManager {
    static void doActivate(String cid) {
        ConversationManager conversationManager = WebBeansContext.currentInstance().getConversationManager();
        Conversation conversation = conversationManager.getConversationBeanReference();
        ContextFactory contextFactory = WebBeansContext.currentInstance().getContextFactory();

        if (conversation.isTransient()) {
            contextFactory.initConversationContext(null);
            //Not restore, throw exception
            if (cid != null && "".equals(cid) == false) {
                throw new NonexistentConversationException("Propagated conversation with cid=" + cid + " is not restored. It creates a new transient conversation.");
            }
        } else {
            //Conversation must be used by one thread at a time
            ConversationImpl owbConversation = (ConversationImpl) conversation;
            if (owbConversation.getInUsed().compareAndSet(false, true) == false) {
                contextFactory.initConversationContext(null);
                //Throw Busy exception
                throw new BusyConversationException("Propagated conversation with cid=" + cid + " is used by other request. It creates a new transient conversation");
            } else {
                ConversationContext conversationContext = conversationManager.getConversationContext(conversation);
                contextFactory.initConversationContext(conversationContext);
            }
        }
    }

    static void doInvalidate() {
        ConversationManager conversationManager = WebBeansContext.currentInstance().getConversationManager();
        conversationManager.destroyWithRespectToTimout();
    }

    static void doDeactivate() {
        ConversationManager conversationManager = WebBeansContext.currentInstance().getConversationManager();
        Conversation conversation = conversationManager.getConversationBeanReference();
        ContextFactory contextFactory = WebBeansContext.currentInstance().getContextFactory();

        if (conversation.isTransient()) {
            contextFactory.destroyConversationContext();
        } else {
            //Conversation must be used by one thread at a time
            ConversationImpl owbConversation = (ConversationImpl) conversation;
            owbConversation.updateTimeOut();
            //Other threads can now access propagated conversation.
            owbConversation.setInUsed(false);
        }
    }
}
