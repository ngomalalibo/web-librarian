package com.pc.weblibrarian.security;

import com.pc.weblibrarian.dataService.GenericDataService;
import com.pc.weblibrarian.dataproviders.LibraryUsersDP;
import com.pc.weblibrarian.entity.LibraryUser;
import com.pc.weblibrarian.enums.PersonRoleType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
public class UserAuthenticationProvider extends DaoAuthenticationProvider
{
    
    public UserAuthenticationProvider()
    {
        super();
    }
    
    private static GenericDataService gds = new GenericDataService(new LibraryUser());
    
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException
    {
        log.info("authenticating......");
        
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        
        // log.info("authenticate -> " + username);
        // log.info("credentials -> " + password);
        
        if (!(password instanceof String))
        {
            throw new BadCredentialsException("Invalid username or password.");
        }
        
        LibraryUser usere = (LibraryUser) gds.getRecordByEntityProperty("userEmail", username);
        
        if (usere == null || usere.getLoginAttempts() > 5)
        {
            System.out.println("Account not found or Account is locked -> " + username);
            // account not found or account is locked
            throw new BadCredentialsException("Account not found or Account is locked.");
        }
        
        if (PasswordEncoder.getPasswordEncoder().matches(password, usere.getHashedPassword()))
        {
            Set<SimpleGrantedAuthority> grantedAuthorities = new HashSet<>();
            
            PersonRoleType[] availableRoles = PersonRoleType.getAvailableRoles(usere.getPersonRoleTypes());
            // List.of(availableRoles).forEach(d -> grantedAuthorities.add(new SimpleGrantedAuthority(PersonRoleType.getDisplayText(d))));
           Arrays.asList(availableRoles).forEach(d -> grantedAuthorities.add(new SimpleGrantedAuthority(PersonRoleType.getDisplayText(d))));
            log.info("authorities -> " + grantedAuthorities.toString());
            usere.setLoginAttempts(1);
            usere.setLastLoginDateTime(LocalDateTime.now());
            usere.replaceEntity(usere, usere);
            
            return new UsernamePasswordAuthenticationToken(username, password, grantedAuthorities);
        }
        else
        {
            // failed authentication
            usere.setLoginAttempts(usere.getLoginAttempts() + 1);
            if (usere.getLoginAttempts() > 5)
            {
                usere.setAccountLocked(true);
            }
            usere.replaceEntity(usere, usere);
            throw new BadCredentialsException("Invalid username or password.");
        }
    }
    
    @Override
    public boolean supports(Class<?> authentication)
    {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
    
    @Override
    public void setAuthoritiesMapper(GrantedAuthoritiesMapper authoritiesMapper)
    {
        super.setAuthoritiesMapper(SecurityConfig.authoritiesMapper());
    }
    
    
    @Override
    public void setPasswordEncoder(org.springframework.security.crypto.password.PasswordEncoder passwordEncoder)
    {
        super.setPasswordEncoder(PasswordEncoder.getPasswordEncoder());
    }
    
    @Override
    public void setUserDetailsService(UserDetailsService userDetailsService)
    {
        super.setUserDetailsService(new LibraryUsersDP());
    }
}
