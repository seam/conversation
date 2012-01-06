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

import java.io.IOException;

import javax.enterprise.context.Conversation;
import javax.enterprise.inject.Any;
import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.junit.Assert;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class RealTestFilter implements Filter {
    private String cId = "1234";

    @Inject @Any
    private MiddleBean bean;

    public void init(FilterConfig config) throws ServletException {
        String t = config.getInitParameter("cid");
        if (t != null)
            cId = t;
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String cid = request.getParameter("cid");
        System.err.println("Testing ..." + cid + ", sessionId = " + ((HttpServletRequest) request).getSession().getId());

        Assert.assertNotNull(bean);
        Conversation conversation = bean.getConversation();
        Assert.assertNotNull(conversation);

        if (cid == null) {
            conversation.begin(cId);
        } else {
            Assert.assertEquals(cId, conversation.getId());
        }

        try {
            chain.doFilter(request, response);
        } finally {
            if (cid != null)
                conversation.end();
        }
    }

    public void destroy() {
    }
}
