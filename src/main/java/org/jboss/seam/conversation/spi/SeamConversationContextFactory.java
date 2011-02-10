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

import org.jboss.seam.conversation.plugins.candi.CanDIHttpSeamConversationContext;
import org.jboss.seam.conversation.plugins.openwebbeans.OpenWebBeansHttpSeamConversationContext;
import org.jboss.seam.conversation.plugins.weld.WeldBoundSeamConversationContext;
import org.jboss.seam.conversation.plugins.weld.WeldHttpSeamConversationContext;

/**
 * Create SeamConversationContext based on underlying CDI implementation.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class SeamConversationContextFactory
{
   private static String WELD_ENV = "org.jboss.weld.manager.api.WeldManager";
   private static String OWB_ENV = "org.apache.webbeans.container.BeanManagerImpl";
   private static String CANDI_ENV = "com.caucho.config.inject.InjectManager";

   private static SeamConversationContext context;

   /**
    * Get the current Seam converation context instance.
    *
    * @param storeType the store type
    * @return get current conversation context instance
    */
   @SuppressWarnings({"unchecked"})
   public static <T> SeamConversationContext<T> getContext(Class<T> storeType)
   {
      if (context == null)
      {
         synchronized (SeamConversationContextFactory.class)
         {
            if (context == null)
               context = create(storeType);
         }
      }
      return context;
   }

   /**
    * Create new SeamConversationContext instance, based on underlying CDI impl.
    * If null is passed as store type, we use CDI impl's default HTTP based SeamConversationContext.
    *
    * @param storeType the store type
    * @return new Seam conversaton context
    */
   private static <T> SeamConversationContext<T> create(Class<T> storeType)
   {
      boolean isNullOrHttp = HttpServletRequest.class.isAssignableFrom(storeType);

      if (testEnv(WELD_ENV))
      {
         if (isNullOrHttp)
            return (SeamConversationContext<T>) new WeldHttpSeamConversationContext();
         else if ("org.jboss.weld.context.bound.BoundRequest".equals(storeType.getName()))
            return (SeamConversationContext<T>) new WeldBoundSeamConversationContext();
      }

      if (isNullOrHttp == false)
         throw new IllegalArgumentException("Only http Seam context is available for non JBoss Weld impls.");

      if (testEnv(OWB_ENV))
      {
         return (SeamConversationContext<T>) new OpenWebBeansHttpSeamConversationContext();
      }
      else if (testEnv(CANDI_ENV))
      {
         return (SeamConversationContext<T>) new CanDIHttpSeamConversationContext();
      }
      else
         throw new IllegalArgumentException("No matching CDI environment available: " + storeType);
   }

   /**
    * Test if env class exists.
    *
    * @param env the env class
    * @return true if env exists, false otherwise
    */
   private static boolean testEnv(String env)
   {
      try
      {
         ClassLoader cl = SeamConversationContextFactory.class.getClassLoader();
         cl.loadClass(env);
         return true;
      }
      catch (Exception e)
      {
         return false;
      }
   }
}
