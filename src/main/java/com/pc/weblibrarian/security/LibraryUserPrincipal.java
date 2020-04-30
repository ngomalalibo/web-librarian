package com.pc.weblibrarian.security;

import com.pc.weblibrarian.entity.LibraryUser;
import com.pc.weblibrarian.enums.PersonRoleType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

public class LibraryUserPrincipal implements UserDetails
{
    
    private LibraryUser user;
    
    public LibraryUserPrincipal(LibraryUser user)
    {
        super();
        this.user = user;
    }
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities()
    {
        Set<SimpleGrantedAuthority> grantedAuthorities = new HashSet<>();
        
        PersonRoleType[] availableRoles = PersonRoleType.getAvailableRoles(this.user.getPersonRoleTypes());
        List.of(availableRoles).forEach(d -> grantedAuthorities.add(new SimpleGrantedAuthority(PersonRoleType.getDisplayText(d))));
        
        return grantedAuthorities;
        // return SetUtils.singletonSet(new SimpleGrantedAuthority(this.user.getPersonRoleTypes().displayText()));
        // this.user.getPersonRoleTypes().forEach(d -> grantedAuthorities.add(new SimpleGrantedAuthority(d.displayText())));
    }
    
    @Override
    public String getPassword()
    {
        // return this.user.getPassword();
        return this.user.getHashedPassword();
    }
    
    @Override
    public String getUsername()
    {
        return this.user.getUserEmail();
    }
    
    @Override
    public boolean isAccountNonExpired()
    {
        return !this.user.getAccountLocked();
    }
    
    @Override
    public boolean isAccountNonLocked()
    {
        return !this.user.getAccountLocked();
    }
    
    @Override
    public boolean isCredentialsNonExpired()
    {
        return !this.user.getAccountLocked();
    }
    
    @Override
    public boolean isEnabled()
    {
        return !this.user.getAccountLocked();
    }
}
