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

import java.util.Formatter;

import org.jboss.seam.conversation.support.DummyServlet;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.Asset;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.weld.environment.servlet.test.util.BeansXml;

/**
 * @author -- from Weld Servlet --
 * @author Ales Justin
 */
public class Deployments {
    public static final String CONTEXT_PATH = "http://localhost:8080/test/";

    public static final String DEFAULT_WEB_XML_PREFIX = "<web-app>%1s <resource-env-ref><resource-env-ref-name>BeanManager</resource-env-ref-name><resource-env-ref-type>javax.enterprise.inject.spi.BeanManager</resource-env-ref-type></resource-env-ref> <welcome-file-list><welcome-file>index.html</welcome-file></welcome-file-list>";
    public static final String DEFAULT_WEB_XML_SUFFIX = "</web-app>";
    public static final String ARQUILLIAN_WEB_XML_SNIPPET =
            "<servlet><servlet-name>DummyServlet</servlet-name><servlet-class>" + DummyServlet.class.getName() + "</servlet-class></servlet>" +
                    "<servlet><servlet-name>ServletTestRunner</servlet-name><servlet-class>org.jboss.arquillian.protocol.servlet_3.ServletTestRunner</servlet-class></servlet>" +
                    "<servlet-mapping><servlet-name>DummyServlet</servlet-name><url-pattern>/dummy/</url-pattern></servlet-mapping>" +
                    "<servlet-mapping><servlet-name>ServletTestRunner</servlet-name><url-pattern>/ArquillianServletRunner</url-pattern></servlet-mapping>";

    public static final Asset DEFAULT_WEB_XML = new StringAsset(DEFAULT_WEB_XML_PREFIX + ARQUILLIAN_WEB_XML_SNIPPET + DEFAULT_WEB_XML_SUFFIX);

    public static final Asset EMPTY_FACES_CONFIG_XML = new StringAsset("<faces-config version=\"2.0\" xmlns=\"http://java.sun.com/xml/ns/javaee\"></faces-config>");

    public static final Asset FACES_WEB_XML = new StringAsset((DEFAULT_WEB_XML_PREFIX + "<listener><listener-class>com.sun.faces.config.ConfigureListener</listener-class></listener> <context-param><param-name>javax.faces.DEFAULT_SUFFIX</param-name><param-value>.xhtml</param-value></context-param> <servlet><servlet-name>Faces Servlet</servlet-name><servlet-class>javax.faces.webapp.FacesServlet</servlet-class><load-on-startup>1</load-on-startup></servlet> <servlet-mapping><servlet-name>Faces Servlet</servlet-name><url-pattern>*.jsf</url-pattern></servlet-mapping> " + DEFAULT_WEB_XML_SUFFIX));

    public static WebArchive baseDeployment(BeansXml beansXml, Asset webXml) {
        return ShrinkWrap.create(WebArchive.class, "test.war")
                .addAsWebInfResource(beansXml, "beans.xml")
                
                // hack to make Weld tests work on Tomcat
                .addAsWebInfResource(beansXml, "classes/META-INF/beans.xml")
                .setWebXML(webXml);
    }

    public static WebArchive baseDeployment(BeansXml beansXml) {
        return baseDeployment(beansXml, DEFAULT_WEB_XML);
    }

    public static WebArchive baseDeployment() {
        return baseDeployment(new BeansXml(), DEFAULT_WEB_XML);
    }

    public static WebArchive baseDeployment(Asset webXml) {
        return baseDeployment(new BeansXml(), webXml);
    }

    public static String formatDefaultWebXml(String... args) {
        if (args == null || args.length == 0)
            throw new IllegalArgumentException("Null or empty args");

        return new Formatter().format(DEFAULT_WEB_XML_PREFIX, args).toString();
    }

    /**
     * Inserts the extension into the end of the default web.xml (just before closing web-app)
     *
     * @param extension the extension
     * @param args      format args
     * @return appended web xml
     */
    public static String extendDefaultWebXml(String extension, String... args) {
        return formatDefaultWebXml(args) + ARQUILLIAN_WEB_XML_SNIPPET + extension + DEFAULT_WEB_XML_SUFFIX;
    }
}
