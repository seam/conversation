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

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.Annotated;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.servlet.http.HttpServletRequest;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.security.AccessController;
import java.security.PrivilegedExceptionAction;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ServiceLoader;

/**
 * Create SeamConversationContext based on underlying CDI implementation.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class SeamConversationContextFactory
{
   private static Map<String, SeamConversationContext> contexts;

   /**
    * Produce matching Seam conversation context.
    *
    * @param ip current injection point
    * @return new Seam conversation context instance
    */
   @SuppressWarnings({"unchecked"})
   @Produces
   public static SeamConversationContext produce(InjectionPoint ip)
   {
      Annotated annotated = ip.getAnnotated();
      Class<?> storeType = null;
      if (annotated != null)
      {
         Type baseType = annotated.getBaseType();
         if (baseType instanceof ParameterizedType)
         {
            ParameterizedType pt = (ParameterizedType) baseType;
            storeType = (Class<?>) pt.getActualTypeArguments()[0];
         }
      }
      return getContext(storeType);
   }

   /**
    * Get the current Seam conversation context instance.
    * If null is passed as store type,
    * we try to use CDI impl's default HTTP based SeamConversationContext.
    *
    * @param storeType the store type
    * @return get current conversation context instance
    */
   @SuppressWarnings({"unchecked"})
   public static synchronized <T> SeamConversationContext<T> getContext(Class<T> storeType)
   {
      if (contexts == null)
         contexts = new HashMap<String, SeamConversationContext>();

      if (storeType == null)
         storeType = (Class<T>) HttpServletRequest.class;

      String type = storeType.getName();
      SeamConversationContext scc = contexts.get(type);
      if (scc == null)
      {
         scc = create(storeType);
         contexts.put(type, scc);
      }
      return scc;
   }

   /**
    * Create new SeamConversationContext instance, based on underlying CDI impl.
    *
    * @param storeType the store type
    * @return new Seam conversation context
    */
   @SuppressWarnings({"unchecked"})
   private static <T> SeamConversationContext<T> create(Class<T> storeType)
   {
      ServiceLoader<SeamConversationContext> loader = ServiceLoader.load(SeamConversationContext.class);
      Iterator<SeamConversationContext> iter = loader.iterator();
      while(iter.hasNext())
      {
         SeamConversationContext scc = iter.next();
         if (match(scc, storeType))
            return scc;
      }
      throw new IllegalArgumentException("No matching CDI environment available: " + storeType);
   }

   /**
    * Match current Seam conversation context instance with store type.
    *
    * @param scc current Seam conversation context
    * @param storeType the store type
    * @return true if scc can be used together with store type, false otherwise
    */
   private static boolean match(SeamConversationContext scc, final Class<?> storeType)
   {
      // TODO -- any better way of doing this?
      try
      {
         Class<?> current = scc.getClass();
         while (current != Object.class)
         {
            final Class<?> clazz = current;
            Method doAssociate = AccessController.doPrivileged(new PrivilegedExceptionAction<Method>()
            {
               public Method run() throws Exception
               {
                  try
                  {
                     // TODo -- impl detail
                     return clazz.getDeclaredMethod("doAssociate", storeType);
                  }
                  catch (NoSuchMethodException nsme)
                  {
                     return null;
                  }
               }
            });
            if (doAssociate != null)
               return true;

            current = current.getSuperclass();
         }
      }
      catch (Throwable ignored)
      {
      }
      return false;
   }
}
