package com.pc.weblibrarian;

import com.pc.weblibrarian.security.SecurityUtils;
import com.vaadin.flow.spring.SpringServlet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.context.event.*;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

/**
 * The entry point of the Spring Boot application.
 */
@Slf4j
@PropertySources({@PropertySource("classpath:application_env.properties"),
        @PropertySource("classpath:application.properties")}
)
@SpringBootApplication(exclude = ErrorMvcAutoConfiguration.class)/*(exclude = {org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class})*/
// TODO > Option 2: To disable spring security (Doesn not disable security)
public class Application extends SpringBootServletInitializer implements WebMvcConfigurer, WebApplicationInitializer
{
    
    
    public static void main(String[] args)
    {
        // disableWarning();
        SpringApplication.run(Application.class, args);
    }
    
    public static void disableWarning()
    {
        System.err.close();
        System.setErr(System.out);
    }
    
    @EventListener(classes = {ContextStartedEvent.class, ContextStoppedEvent.class, ContextClosedEvent.class, ContextRefreshedEvent.class})
    public void handleMultipleEvents()
    {
        log.info("Multi-event listener invoked");
    }
    
    @Override
    public void addInterceptors(InterceptorRegistry registry)
    {
        registry.addInterceptor(new SecurityUtils());
    }
    
    /*@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application)
    {
        return application.sources(Application.class);
    }*/
    
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException
    {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.register(Application.class);
        context.setServletContext(servletContext);
        servletContext.addListener(new ContextLoaderListener(context));
        
        ServletRegistration.Dynamic registration = servletContext.addServlet("dispatcher", new SpringServlet(context, true));
        registration.setLoadOnStartup(1);
        registration.addMapping("/*");
    }
    
    /*@Bean
    public ViewResolver internalResourceViewResolver() {
        InternalResourceViewResolver bean = new InternalResourceViewResolver();
        bean.setViewClass(JstlView.class);
        bean.setPrefix("/WEB-INF/view/");
        bean.setSuffix(".jsp");
        return bean;
    }*/
    
    
    
    
    /*@Bean
    public SpringResourceTemplateResolver templateResolver()
    {
        log.info("Application context " + applicationContext);
        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
        templateResolver.setApplicationContext(applicationContext);
        templateResolver.setPrefix("templates/");
        templateResolver.setSuffix(".html");
        
        return templateResolver;
    }*/
    
    /*@Bean
    public SpringTemplateEngine templateEngine()
    {
        
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver());
        templateEngine.setEnableSpringELCompiler(true);
        
        return templateEngine;
    }*/
    
    /*@Bean
    public ViewResolver viewResolver()
    {
        
        ThymeleafViewResolver resolver = new ThymeleafViewResolver();
        ViewResolverRegistry registry = new ViewResolverRegistry(null, applicationContext);
        
        resolver.setTemplateEngine(templateEngine());
        registry.viewResolver(resolver);
        return resolver;
    }*/
}
