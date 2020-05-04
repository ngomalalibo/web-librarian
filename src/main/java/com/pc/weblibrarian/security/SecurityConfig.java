package com.pc.weblibrarian.security;

import com.pc.weblibrarian.controllers.UserController;
import com.pc.weblibrarian.dataproviders.LibraryUsersDP;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter
{
    private static final String LOGIN_PROCESSING_URL = "/login";
    private static final String LOGIN_FAILURE_URL = "/login?error";
    private static final String LOGIN_URL = "/login";
    private static final String LOGOUT_SUCCESS_URL = "/login";
    
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception
    { //
        return super.authenticationManagerBean();
    }
    
    @Bean
    public CustomRequestCache requestCache()
    { //
        return new CustomRequestCache();
    }
    
    /*@Bean
    public DaoAuthenticationProvider authenticationProvider()
    {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService());
        provider.setPasswordEncoder(PasswordEncoder.getPasswordEncoder());
        // provider.setPasswordEncoder(PasswordEncoder.createDelegatingPasswordEncoder());
        // provider.setPasswordEncoder(NoOpPasswordEncoder.getInstance()); //for testing
        provider.setAuthoritiesMapper(authoritiesMapper());
        return provider;
    }*/
    
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception
    {
        auth.authenticationProvider(new UserAuthenticationProvider());
        // auth.authenticationProvider(authenticationProvider());
    }
    
    @Bean
    @Override
    protected UserDetailsService userDetailsService()
    {
        return new LibraryUsersDP();
        
    }
    
    @Bean
    public static GrantedAuthoritiesMapper authoritiesMapper()
    {
        SimpleAuthorityMapper authMapper = new SimpleAuthorityMapper();
        authMapper.setConvertToUpperCase(true);
        authMapper.setDefaultAuthority("ADMIN");
        return authMapper;
    }
    
    @Override
    public void configure(WebSecurity web)
    {
        // web.ignoring().antMatchers("/**");
        // web.ignoring().antMatchers("/resources/**");
        
        web.ignoring().antMatchers(
                // "/VAADIN/**",
                "/favicon.ico",
                "/robots.txt",
                "/manifest.webmanifest",
                "/sw.js",
                "/offline.html",
                "/icons/**",
                "/images/**",
                "/styles/**",
                "/frontend/**",
                "/register/**",
                "/emailvalidation/**",
                "/h2-console/**",
                "/frontend-es5/**", "frontend-es6/**");
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception
    {
        
        http.csrf().disable()
            .requestCache().requestCache(new CustomRequestCache())
            .and().authorizeRequests()
            .antMatchers("/login", "/register", "/passwordrecovery", "passwordreset", "/").permitAll()
            .requestMatchers(SecurityUtils::isFrameworkInternalRequest).permitAll()
            .antMatchers("/VAADIN/**").permitAll()
            .anyRequest().authenticated()
            .and()
            .exceptionHandling()/*.accessDeniedPage("/forbidden")*/.accessDeniedHandler(new RestAccessDeniedHandler()).authenticationEntryPoint(new RestAuthenticationEntryPoint())
            // .and()
            // .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .formLogin()
            .loginPage(LOGIN_URL).permitAll()
            .loginProcessingUrl(LOGIN_PROCESSING_URL)
            .failureUrl(LOGIN_FAILURE_URL)
            .and()
            .logout()
            .invalidateHttpSession(true)
            .deleteCookies("JSESSIONID")
            .clearAuthentication(true)
            .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
            .logoutSuccessHandler( new UserController() ) //added
            .logoutSuccessUrl(LOGOUT_SUCCESS_URL)
            .and().rememberMe().key("pssssst").alwaysRemember(true);
    
        /*http.requiresChannel()
            .requestMatchers(r -> r.getHeader("X-Forwarded-Proto") != null)
            .requiresSecure();*/
    }
    
}