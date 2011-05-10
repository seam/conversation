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

package org.jboss.seam.conversation.plugins.candi;

import javax.enterprise.context.ConversationScoped;
import javax.servlet.http.HttpServletRequest;

import com.caucho.config.inject.InjectManager;
import com.caucho.server.webbeans.ConversationContext;
import org.jboss.seam.conversation.api.AbstractHttpSeamConversationContext;

/**
 * CanDI Http based Seam conversation context.
 * <p/>
 * Note: depends on JSF
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class CanDIHttpSeamConversationContext extends AbstractHttpSeamConversationContext {
    private ConversationContext context;
    private static ThreadLocal<HttpServletRequest> requests = new ThreadLocal<HttpServletRequest>();

    /**
     * Get context.
     *
     * @return the context.
     */
    protected ConversationContext getContext() {
        if (context == null) {
            synchronized (this) {
                if (context == null) {
                    InjectManager manager = InjectManager.create();
                    context = (ConversationContext) manager.getContext(ConversationScoped.class);
                }
            }
        }
        return context;
    }

    protected void doAssociate(HttpServletRequest request) {
        requests.set(request);
        HackFacesContext.setCurrent(request);
        // real associate work is done in ConversationContext::createJsfScope
    }

    protected void doActivate(String conversationId) {
        HttpServletRequest request = requests.get();
        if (request == null)
            throw new IllegalArgumentException("Forgot to associate request with conversation context?");

        HackFacesContext.doActivate(conversationId);
    }

    protected void doInvalidate() {
        // TODO -- any way to invalidate conversations?
    }

    protected void doDeactivate() {
        HttpServletRequest request = requests.get();
        if (request == null)
            throw new IllegalArgumentException("Forgot to associate request with conversation context?");

        HackFacesContext.doDeactivate();
    }

    protected void doDissociate(HttpServletRequest request) {
        try {
            getContext().destroy();
        } finally {
            requests.remove();
            HackFacesContext.getCurrentInstance().release();
        }
    }
}
