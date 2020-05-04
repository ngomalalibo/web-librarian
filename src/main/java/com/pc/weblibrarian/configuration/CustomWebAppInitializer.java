package com.pc.weblibrarian.configuration;

import com.pc.weblibrarian.Application;
import com.vaadin.flow.spring.VaadinMVCWebAppInitializer;

import java.util.Arrays;
import java.util.Collection;

// import com.vaadin.flow.spring.VaadinMVCWebAppInitializer;

public class CustomWebAppInitializer extends VaadinMVCWebAppInitializer
{
    @Override
    protected Collection<Class<?>> getConfigurationClasses()
    {
        // return List.of(Application.class/*, PasswordEncoder.class, SecurityConfig.class*/);
        return Arrays.asList(Application.class/*, PasswordEncoder.class, SecurityConfig.class*/);
    }
}
