package com.pc.weblibrarian.dataproviders;


import com.pc.weblibrarian.dataService.PersonDataService;
import com.pc.weblibrarian.entity.PersistingBaseEntity;
import com.vaadin.flow.data.provider.AbstractBackEndDataProvider;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.data.provider.QuerySortOrder;
import com.vaadin.flow.data.provider.SortDirection;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;


public class PersonListBoxDP extends AbstractBackEndDataProvider<String, String>
{
    //Choose your JSON API to use depending on what you want to do with the data
    //Use Binding and DOM APIs to map to objects and Streaming to pick out elements from json documents
    public PersonListBoxDP()
    {
    }
    
    @Override
    // the grid is the Query provider while the dataprovider is the data provider
    protected Stream<String> fetchFromBackEnd(Query<String, String> query)
    {
        int limit = query.getLimit();
        int offset = query.getOffset();
    
        List<SortProperties> sortProperties =  SortProperties.getSortProperties(query.getSortOrders());
        
        String filter = query.getFilter().orElse("");
        return fetch(limit, offset, sortProperties, filter);
        
    }
    
    @Override
    protected int sizeInBackEnd(Query<String, String> query)
    {
        //Objects.requireNonNull(q.getFilter().orElse(null)) // Intellij recommendation for first use. Nice method
        String filter = Objects.requireNonNull(query.getFilter().orElse(""));
        return PersonDataService.getPersonCount(filter);
    }
    
    public Stream<String> fetch(int offset, int limit, List<SortProperties> sortOrders, String filter)
    {
        return PersonDataService.getPersonsByName(sortOrders, filter).map(PersistingBaseEntity::getUuid);
    }
    
    /*public int totalSize()
    {
        return (int) sizeInBackEnd(new Query<>());
    }*/
    
}
