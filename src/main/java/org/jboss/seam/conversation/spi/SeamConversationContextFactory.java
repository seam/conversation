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

/**
 * Create SeamConversationContext based on underlying CDI implementation.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class SeamConversationContextFactory
{
   private static SeamConversationContext context;

   /**
    * Get the current Seam converation context instance.
    *
    * @return get current conversation context instance
    */
   public static SeamConversationContext getContext()
   {
      if (context == null)
      {
         synchronized (SeamConversationContextFactory.class)
         {
            if (context == null)
               context = create();
         }
      }
      return context;
   }

   /**
    * Create new SeamConversationContext instance, based on underlying CDI impl.
    *
    * @return new Seam conversaton context
    */
   private static SeamConversationContext create()
   {
      return null;
   }
}
