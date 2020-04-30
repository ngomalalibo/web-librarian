package com.pc.weblibrarian.configuration;

import com.pc.weblibrarian.Application;
import com.vaadin.flow.spring.VaadinMVCWebAppInitializer;

import java.util.Collection;
import java.util.List;

// import com.vaadin.flow.spring.VaadinMVCWebAppInitializer;

public class CustomWebAppInitializer extends VaadinMVCWebAppInitializer
{
    @Override
    protected Collection<Class<?>> getConfigurationClasses()
    {
        return List.of(Application.class/*, PasswordEncoder.class, SecurityConfig.class*/);
    }
}
