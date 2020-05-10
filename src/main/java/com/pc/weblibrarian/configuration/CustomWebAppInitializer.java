package com.pc.weblibrarian.configuration;

import com.pc.weblibrarian.security.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Slf4j
@PropertySources({
        @PropertySource("classpath:application.properties"),
        @PropertySource("classpath:application_env.properties")
}
)
public class CustomWebAppInitializer/* extends VaadinMVCWebAppInitializer */
{
    // @Override
    // protected Collection<Class<?>> getConfigurationClasses()
    // {
    //     // return List.of(Application.class/*, PasswordEncoder.class, SecurityConfig.class*/);
    //     return Arrays.asList(Application.class/*, PasswordEncoder.class, SecurityConfig.class*/);
    // }
    
    
    
    
}
