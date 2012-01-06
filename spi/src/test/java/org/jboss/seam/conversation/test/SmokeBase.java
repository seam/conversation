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

import junit.framework.Assert;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.seam.conversation.api.AbstractHttpSeamConversationContext;
import org.jboss.seam.conversation.spi.SeamConversationContext;
import org.jboss.seam.conversation.support.RealTestFilter;
import org.jboss.seam.conversation.support.SetupHttpSCCFilter;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.Asset;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;

/**
 * Simple smoke test.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
@RunAsClient
public class SmokeBase {
    static String FILTER =
            "<filter>" +
                    "<filter-name>conversation</filter-name>" +
                    "<filter-class>" + SetupHttpSCCFilter.class.getName() + "</filter-class>" +
                    "</filter>" +
                    "<filter>" +
                    "<filter-name>test-filter</filter-name>" +
                    "<filter-class>" + RealTestFilter.class.getName() + "</filter-class>" +
                    "</filter>" +
                    "<filter-mapping>" +
                    "<filter-name>conversation</filter-name>" +
                    "<url-pattern>/*</url-pattern>" +
                    "</filter-mapping>" +
                    "<filter-mapping>" +
                    "<filter-name>test-filter</filter-name>" +
                    "<url-pattern>/*</url-pattern>" +
                    "</filter-mapping>";

    protected static Asset getWebXml(String... args) {
        return new StringAsset(Deployments.extendDefaultWebXml(FILTER, args));
    }

    protected static WebArchive deployment(WebArchive archive, String... args) {
        archive.addPackage(SetupHttpSCCFilter.class.getPackage());

        archive.addAsLibrary(ShrinkWrap.create(JavaArchive.class, "seam-conversation-spi.jar")
                .addPackage(SeamConversationContext.class.getPackage())
                .addPackage(AbstractHttpSeamConversationContext.class.getPackage())
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
        );

        WebArchive webArchive = TomcatDeployments.tomcatfy(archive, args);
        webArchive.add(new StringAsset("<html/>"), "index.html");
        System.err.println(webArchive.toString(true));
        return webArchive;
    }

    @Test
    public void testFactory() throws Exception {
        Thread.sleep(1000L);

        SimpleHttpConnectionManager connManager = new SimpleHttpConnectionManager(true);
        HttpClient client = new HttpClient(connManager);

        HttpMethod method = new GetMethod(Deployments.CONTEXT_PATH + "dummy/");
        int response = client.executeMethod(method);
        Assert.assertEquals(200, response);
        Assert.assertEquals("OK", method.getResponseBodyAsString());

        method = new GetMethod(Deployments.CONTEXT_PATH + "dummy/?cid=1234");
        response = client.executeMethod(method);
        Assert.assertEquals(200, response);
        Assert.assertEquals("OK", method.getResponseBodyAsString());
    }
}
