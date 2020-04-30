package com.pc.weblibrarian.dataproviders;

import com.pc.weblibrarian.dataService.Connection;
import com.pc.weblibrarian.dataService.GenericDataService;
import com.pc.weblibrarian.entity.Address;
import com.pc.weblibrarian.entity.Person;
import com.pc.weblibrarian.utils.CustomNullChecker;
import com.vaadin.flow.data.provider.AbstractBackEndDataProvider;
import com.vaadin.flow.data.provider.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PersonDP extends AbstractBackEndDataProvider<Person, String>
{
    //Choose your JSON API to use depending on what you want to do with the data
    //Use Binding and DOM APIs to map to objects and Streaming to pick out elements from json documents
    
    GenericDataService gds;
    
    public PersonDP()
    {
        super();
        gds = new GenericDataService(new Person());
    }
    
    @Override
    // the grid is the Query provider while the dataprovider is the data provider
    protected Stream<Person> fetchFromBackEnd(Query<Person, String> query)
    {
        List<SortProperties> sortProperties = SortProperties.getSortProperties(query.getSortOrders());
        
        String filter = query.getFilter().orElse("");
        return fetch(sortProperties, filter);
        
        
    }
    
    @Override
    protected int sizeInBackEnd(Query<Person, String> query)
    {
        String filter = query.getFilter().orElse("");
        
        List<SortProperties> sortProperties = SortProperties.getSortProperties(query.getSortOrders());
        // return (int) PersonDataService.getPersonCount(filter);
        return gds.getEntityByTwoFilterSearch(sortProperties, "firstName", "lastName", filter).size();
    }
    
    List<Address> addresses = new ArrayList<>();
    
    public Stream<Person> fetch(List<SortProperties> sortOrders, String filter)
    {
        List<Person> entityByTwoFilterSearch = gds.getEntityByTwoFilterSearch(sortOrders, "firstName", "lastName", filter);
        List<Person> pWithDetail = new ArrayList<>();
        
        entityByTwoFilterSearch.forEach(d ->
                                        {
                                            if (d.getAddressIds().size() > 0)
                                            {
                                                addresses = d.getAddressIds().stream().filter(w -> !CustomNullChecker.emptyNullStringChecker(w)).map(s -> GetObjectByID.getObjectById(s, Connection.addresses)).collect(Collectors.toList());
                                                d.setAddressObjects(addresses);
                                                pWithDetail.add(d);
                                            }
                                        });
        return pWithDetail.stream();
    }
}
