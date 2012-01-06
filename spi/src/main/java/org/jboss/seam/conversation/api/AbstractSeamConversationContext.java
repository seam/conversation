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

package org.jboss.seam.conversation.api;


import javax.enterprise.inject.Typed;

import org.jboss.seam.conversation.spi.SeamConversationContext;

/**
 * Abstract Seam conversation context.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
@Typed()
public abstract class AbstractSeamConversationContext<T> implements SeamConversationContext<T> {
    protected abstract void doAssociate(T storage);

    public SeamConversationContext<T> associate(T storage) {
        doAssociate(storage);
        return this;
    }

    protected abstract void doDissociate(T storage);

    public SeamConversationContext<T> dissociate(T storage) {
        doDissociate(storage);
        return this;
    }

    protected abstract void doActivate(String conversationId);

    public SeamConversationContext<T> activate(String conversationId) {
        doActivate(conversationId);
        return this;
    }

    protected abstract void doInvalidate();

    public SeamConversationContext<T> invalidate() {
        doInvalidate();
        return this;
    }

    protected abstract void doDeactivate();

    public SeamConversationContext<T> deactivate() {
        doDeactivate();
        return this;
    }

    protected static boolean isEmpty(String string) {
        int len;
        if (string == null || (len = string.length()) == 0) {
            return true;
        }

        for (int i = 0; i < len; i++) {
            if ((Character.isWhitespace(string.charAt(i)) == false)) {
                return false;
            }
        }
        return true;
    }
}
