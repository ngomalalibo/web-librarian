package com.pc.weblibrarian;

import com.pc.weblibrarian.security.SecurityUtils;
import com.vaadin.flow.spring.SpringServlet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.ContextStoppedEvent;
import org.springframework.context.event.EventListener;
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
// @EnableVaadin({"com.pc.weblibrarian.views"})
@SpringBootApplication(exclude = ErrorMvcAutoConfiguration.class)/*(exclude = {org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class})*/
// TODO > Option 2: To disable spring security (Does not disable security)
public class Application extends SpringBootServletInitializer implements WebMvcConfigurer
{
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder)
    {
        return builder.sources(Application.class);
        // return super.configure(builder);
    }
    
    @Override
    public void addInterceptors(InterceptorRegistry registry)
    {
        registry.addInterceptor(new SecurityUtils());
    }
    
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
    
    @EventListener(classes = {ContextStartedEvent.class, ContextStoppedEvent.class/*, ContextClosedEvent.class, ContextRefreshedEvent.class*/})
    public void handleMultipleEvents()
    {
        log.info(" -> Multi-event listener invoked");
    }
    
    /*@Override
    public void onStartup(ServletContext container) throws ServletException
    {
        log.info("---------------Web Application Initialized -> onStartup() called");
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.register(Application.class);
        context.setServletContext(container);
        container.addListener(new ContextLoaderListener(context));
        
        ServletRegistration.Dynamic registration = container.addServlet("dispatcher", new SpringServlet(context, true));
        registration.setLoadOnStartup(1);
        registration.addMapping("/*");
    }*/
    
    /*@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application)
    {
        return application.sources(Application.class);
    }*/
    
    
    
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
