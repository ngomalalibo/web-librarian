package com.pc.weblibrarian.configuration;


import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ApplicationServiceInitListener implements VaadinServiceInitListener
{
    
    @Override
    public void serviceInit(ServiceInitEvent event)
    {
        event.addBootstrapListener(response ->
                                   {
                                       log.info("VaadinServiceInitListener callback -> BoostrapListener to change the bootstrap page");
                                       // BoostrapListener to change the bootstrap page
                                   });
        
        event.addDependencyFilter((dependencies, filterContext) ->
                                  {
                                      log.info("VaadinServiceInitListener callback -> DependencyFilter to add/remove/change dependencies sent to the client");
                                      // DependencyFilter to add/remove/change dependencies sent to
                                      // the client
                                      return dependencies;
                                  });
        
        event.addRequestHandler((session, request, response) ->
                                {
                                    log.info("VaadinServiceInitListener callback -> RequestHandler to change how responses are handled");
                                    // RequestHandler to change how responses are handled
                                    return false;
                                });
    }
    
}


