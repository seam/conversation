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

package org.jboss.seam.conversation.support;

import java.net.URL;
import java.util.LinkedList;

import javax.servlet.ServletContext;

import org.apache.AnnotationProcessor;
import org.apache.catalina.Lifecycle;
import org.apache.catalina.LifecycleEvent;
import org.apache.catalina.core.StandardContext;
import org.apache.webbeans.servlet.WebBeansConfigurationListener;
import org.apache.webbeans.web.tomcat.ContextLifecycleListener;
import org.apache.webbeans.web.tomcat.TomcatAnnotProcessor;
import org.apache.webbeans.web.tomcat.TomcatSecurityListener;

/**
 * Hack event type.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class HackContextLifecycleListener extends ContextLifecycleListener {
    private AnnotationProcessor processor;

    public void lifecycleEvent(LifecycleEvent event) {
        try {
            if (event.getSource() instanceof StandardContext) {
                StandardContext context = (StandardContext) event.getSource();

                if (event.getType().equals(Lifecycle.AFTER_START_EVENT)) {
                    ServletContext scontext = context.getServletContext();
                    URL url = scontext.getResource("/WEB-INF/beans.xml");
                    if (url != null) {
                        //Registering ELResolver with JSP container
                        System.setProperty("org.apache.webbeans.application.jsp", "false");

                        String[] oldListeners = context.findApplicationListeners();
                        LinkedList<String> listeners = new LinkedList<String>();
                        listeners.addFirst(WebBeansConfigurationListener.class.getName());
                        for (String listener : oldListeners) {
                            listeners.add(listener);
                            context.removeApplicationListener(listener);
                        }

                        for (String listener : listeners) {
                            context.addApplicationListener(listener);
                        }

                        context.addApplicationListener(TomcatSecurityListener.class.getName());

                        processor = context.getAnnotationProcessor();
                        AnnotationProcessor custom = new TomcatAnnotProcessor(context.getLoader().getClassLoader(), processor);
                        context.setAnnotationProcessor(custom);

                        context.getServletContext().setAttribute(AnnotationProcessor.class.getName(), custom);
                    }
                } else if (event.getType().equals(Lifecycle.AFTER_STOP_EVENT)) {
                    context.setAnnotationProcessor(processor);
                }
            } else {
                super.lifecycleEvent(event);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
