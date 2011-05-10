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

import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;

/**
 * @author -- from Weld Servlet --
 * @author Ales Justin
 */
public class TomcatDeployments {
    public static final String CONTEXT_XML =
            "<Context>" +
                    "%1s" +
                    "<Manager pathname=\"\" /> <Resource name=\"BeanManager\" auth=\"Container\" type=\"javax.inject.manager.BeanManager\" factory=\"%2s\"/>" +
                    "</Context>";

    /**
     * Add Tomcat specific metadata.
     *
     * @param archive the current archive
     * @param args    the args
     * @return modified archive
     */
    public static WebArchive tomcatfy(WebArchive archive, String... args) {
        if (args == null || args.length == 0)
            throw new IllegalArgumentException("Null or empty args");

        String contextXml = new Formatter().format(CONTEXT_XML, args).toString();
        return archive.add(new StringAsset(contextXml), "META-INF/context.xml");
    }
}
