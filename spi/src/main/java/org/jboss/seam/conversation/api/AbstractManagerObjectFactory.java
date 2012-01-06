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

import java.util.Hashtable;

import javax.enterprise.inject.Typed;
import javax.enterprise.inject.spi.BeanManager;
import javax.naming.CompositeName;
import javax.naming.Context;
import javax.naming.InvalidNameException;
import javax.naming.Name;
import javax.naming.spi.ObjectFactory;

/**
 * Abstract BeanManager object factory.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
@Typed()
public abstract class AbstractManagerObjectFactory implements ObjectFactory {
    private final CompositeName BEAN_MANAGER;

    public AbstractManagerObjectFactory() {
        try {
            BEAN_MANAGER = new CompositeName("BeanManager");
        } catch (InvalidNameException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * Provide current BeanManager instance.
     *
     * @return the current BeanManager instance
     */
    protected abstract BeanManager getBeanManager();

    public Object getObjectInstance(Object obj, Name name, Context nameCtx, Hashtable<?, ?> environment) throws Exception {
        if (name.endsWith(BEAN_MANAGER)) {
            return getBeanManager();
        }
        return null;
    }
}
