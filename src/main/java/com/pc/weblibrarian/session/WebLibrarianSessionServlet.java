package com.pc.weblibrarian.session;

import com.vaadin.flow.server.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
// @VaadinServletConfiguration(productionMode = false, heartbeatInterval = 300, closeIdleSessions = true)
public class WebLibrarianSessionServlet extends VaadinServlet implements SessionInitListener, SessionDestroyListener
{
    /*@Override
    protected void servletInitialized() throws ServletException
    {
        super.servletInitialized();
        getService().addSessionInitListener(this);
        getService().addSessionDestroyListener(this);
    }*/
    
    @Override
    public void sessionInit(SessionInitEvent event) throws ServiceException
    {
        log.info("custom session initialization.... Do session init stuff here");
        // Do session start stuff here
    }
    
    @Override
    public void sessionDestroy(SessionDestroyEvent event)
    {
        log.info("custom session destruction.... Close/shutdown session resources here");
        // Do session end stuff here
    }
    
    
}
