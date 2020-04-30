package com.pc.weblibrarian.dataproviders;

import com.google.common.base.Strings;
import com.pc.weblibrarian.dataService.Connection;
import com.pc.weblibrarian.dataService.GenericDataService;
import com.pc.weblibrarian.entity.LibraryUser;
import com.pc.weblibrarian.security.LibraryUserPrincipal;
import com.pc.weblibrarian.security.PasswordEncoder;
import com.vaadin.flow.data.provider.AbstractBackEndDataProvider;
import com.vaadin.flow.data.provider.Query;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LibraryUsersDP extends AbstractBackEndDataProvider<LibraryUser, String> implements UserDetailsService
{
    private final GenericDataService gds = new GenericDataService(new LibraryUser());
    
    private BCryptPasswordEncoder bcryptPassEncoder = PasswordEncoder.getPasswordEncoder();
    
    public LibraryUsersDP()
    {
        super();
    }
    
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException
    {
        LibraryUser user = (LibraryUser) gds.getRecordByEntityProperty("userEmail", s);
        
        if (user == null)
        {
            throw new UsernameNotFoundException("user not found: " + s);
        }
        
        return new LibraryUserPrincipal(user);
    }
    
    @Override
    protected Stream<LibraryUser> fetchFromBackEnd(Query<LibraryUser, String> query)
    {
        List<SortProperties> sortProperties = SortProperties.getSortProperties(query.getSortOrders());
        
        String filter = query.getFilter().orElse("");
        return fetch(sortProperties, filter).stream();
    }
    
    @Override
    protected int sizeInBackEnd(Query<LibraryUser, String> query)
    {
        List<SortProperties> sortProperties = SortProperties.getSortProperties(query.getSortOrders());
        
        String filter = query.getFilter().orElse("");
        return fetch(sortProperties, filter).size();
    }
    
    public List<LibraryUser> fetch(List<SortProperties> sortOrders, String filter)
    {
        List<LibraryUser> libraryUsers = gds.getEntityByTwoFilterSearch(sortOrders, "userEmail", "firstName", filter);
        return fetchDetails(libraryUsers);
    }
    
    public List<LibraryUser> fetchDetails(List<LibraryUser> libraryUsers)
    {
        return libraryUsers.stream().map(d ->
                                         {
                                             if (!Strings.isNullOrEmpty(d.getPersonId()))
                                             {
                                                 d.setPerson(GetObjectByID.getObjectById(d.getPersonId(), Connection.persons));
                                             }
                                             return d;
                                         }).collect(Collectors.toList());
    }
}
