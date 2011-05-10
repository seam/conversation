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

import java.util.Iterator;

import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseStream;
import javax.faces.context.ResponseWriter;
import javax.faces.render.RenderKit;
import javax.servlet.http.HttpServletRequest;

/**
 * Hack around faces context limitations.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
class HackFacesContext extends FacesContext {
    private HttpServletRequest request;
    private HackExternalContext externalContext;

    HackFacesContext(HttpServletRequest request) {
        this.request = request;
    }

    static void setCurrent(HttpServletRequest request) {
        setCurrentInstance(new HackFacesContext(request));
    }

    static void doActivate(String conversationId) {
        HackFacesContext hfc = (HackFacesContext) getCurrentInstance();
        HackExternalContext hec = hfc.getExternalContext();
        hec.doActivate(conversationId);
    }

    static void doDeactivate() {
        HackFacesContext hfc = (HackFacesContext) getCurrentInstance();
        HackExternalContext hec = hfc.getExternalContext();
        hec.doDeactivate();
    }

    public HackExternalContext getExternalContext() {
        if (externalContext == null)
            externalContext = new HackExternalContext(request);

        return externalContext;
    }

    public UIViewRoot getViewRoot() {
        return new UIViewRoot();
    }

    public Application getApplication() {
        return null;
    }

    public Iterator<String> getClientIdsWithMessages() {
        return null;
    }

    public FacesMessage.Severity getMaximumSeverity() {
        return null;
    }

    public Iterator<FacesMessage> getMessages() {
        return null;
    }

    public Iterator<FacesMessage> getMessages(String s) {
        return null;
    }

    public RenderKit getRenderKit() {
        return null;
    }

    public boolean getRenderResponse() {
        return false;
    }

    public boolean getResponseComplete() {
        return false;
    }

    public ResponseStream getResponseStream() {
        return null;
    }

    public void setResponseStream(ResponseStream responseStream) {
    }

    public ResponseWriter getResponseWriter() {
        return null;
    }

    public void setResponseWriter(ResponseWriter responseWriter) {
    }

    public void setViewRoot(UIViewRoot uiViewRoot) {
    }

    public void addMessage(String s, FacesMessage facesMessage) {
    }

    public void release() {
        setCurrentInstance(null);
    }

    public void renderResponse() {
    }

    public void responseComplete() {
    }
}
