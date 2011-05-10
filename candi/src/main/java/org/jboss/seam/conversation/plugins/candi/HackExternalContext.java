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

package org.jboss.seam.conversation.plugins.candi;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.Principal;
import java.util.AbstractMap;
import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.faces.context.ExternalContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Hack around faces context limitations.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
class HackExternalContext extends ExternalContext {
    private Map<String, String> requestMap = new ConcurrentHashMap<String, String>();
    private HttpSession session;

    @SuppressWarnings({"unchecked"})
    HackExternalContext(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            String[] value = entry.getValue();
            if (value != null && value.length > 0)
                this.requestMap.put(entry.getKey(), value[0]);
        }
        this.session = request.getSession();
    }

    void doActivate(String conversationId) {
        if (conversationId != null) {
            requestMap.put("cid", conversationId);
            requestMap.put("caucho.cid", conversationId);
        }
    }

    void doDeactivate() {
        requestMap.remove("cid");
        requestMap.remove("caucho.cid");
    }

    public Map<String, String> getRequestParameterMap() {
        return requestMap;
    }

    public Map<String, Object> getSessionMap() {
        return new AbstractMap<String, Object>() {
            public Set<Entry<String, Object>> entrySet() {
                return Collections.emptySet();
            }

            public Object get(Object key) {
                return session.getAttribute(key.toString());
            }

            public Object put(String key, Object value) {
                Object previous = get(key);
                session.setAttribute(key, value);
                return previous;
            }
        };
    }

    public void dispatch(String s) throws IOException {
    }

    public String encodeActionURL(String s) {
        return null;
    }

    public String encodeNamespace(String s) {
        return null;
    }

    public String encodeResourceURL(String s) {
        return null;
    }

    public Map<String, Object> getApplicationMap() {
        return null;
    }

    public String getAuthType() {
        return null;
    }

    public Object getContext() {
        return null;
    }

    public String getInitParameter(String s) {
        return null;
    }

    public Map getInitParameterMap() {
        return null;
    }

    public String getRemoteUser() {
        return null;
    }

    public Object getRequest() {
        return null;
    }

    public String getRequestContextPath() {
        return null;
    }

    public Map<String, Object> getRequestCookieMap() {
        return null;
    }

    public Map<String, String> getRequestHeaderMap() {
        return null;
    }

    public Map<String, String[]> getRequestHeaderValuesMap() {
        return null;
    }

    public Locale getRequestLocale() {
        return null;
    }

    public Iterator<Locale> getRequestLocales() {
        return null;
    }

    public Map<String, Object> getRequestMap() {
        return null;
    }

    public Iterator<String> getRequestParameterNames() {
        return null;
    }

    public Map<String, String[]> getRequestParameterValuesMap() {
        return null;
    }

    public String getRequestPathInfo() {
        return null;
    }

    public String getRequestServletPath() {
        return null;
    }

    public URL getResource(String s) throws MalformedURLException {
        return null;
    }

    public InputStream getResourceAsStream(String s) {
        return null;
    }

    public Set<String> getResourcePaths(String s) {
        return null;
    }

    public Object getResponse() {
        return null;
    }

    public Object getSession(boolean b) {
        return null;
    }

    public Principal getUserPrincipal() {
        return null;
    }

    public boolean isUserInRole(String s) {
        return false;
    }

    public void log(String s) {

    }

    public void log(String s, Throwable throwable) {

    }

    public void redirect(String s) throws IOException {

    }
}
