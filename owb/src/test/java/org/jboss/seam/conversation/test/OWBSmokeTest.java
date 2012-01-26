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

package org.jboss.seam.conversation.test;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.seam.conversation.plugins.openwebbeans.ManagerObjectFactory;
import org.jboss.seam.conversation.plugins.openwebbeans.OpenWebBeansHttpSeamConversationContext;
import org.jboss.seam.conversation.spi.SeamConversationContext;
import org.jboss.seam.conversation.support.HackContextLifecycleListener;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.runner.RunWith;

/**
 * Simple smoke test.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
@RunWith(Arquillian.class)
public class OWBSmokeTest extends SmokeBase {
    @Deployment
    public static WebArchive deployment() {
        return deployment(
                Deployments.baseDeployment(getWebXml("")),
                "<Listener className=\"" + HackContextLifecycleListener.class.getName() + "\"/>", ManagerObjectFactory.class.getName()
                
        )
        
        .addAsLibrary(ShrinkWrap.create(JavaArchive.class, "seam-conversation-owb.jar")
            .addPackage(OpenWebBeansHttpSeamConversationContext.class.getPackage())
            .addAsServiceProvider(SeamConversationContext.class, OpenWebBeansHttpSeamConversationContext.class)
        );
    }
}
