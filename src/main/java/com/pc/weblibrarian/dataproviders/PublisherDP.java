package com.pc.weblibrarian.dataproviders;

import com.pc.weblibrarian.dataService.GenericDataService;
import com.pc.weblibrarian.entity.Publisher;
import com.vaadin.flow.data.provider.AbstractBackEndDataProvider;
import com.vaadin.flow.data.provider.Query;

import java.util.List;
import java.util.stream.Stream;

public class PublisherDP extends AbstractBackEndDataProvider<Publisher, String>
{
    GenericDataService gdsPublisher = new GenericDataService(new Publisher());
    
    public PublisherDP()
    {
        super();
    }
    
    @Override
    protected Stream<Publisher> fetchFromBackEnd(Query<Publisher, String> query)
    {
        List<SortProperties> sortProperties = SortProperties.getSortProperties(query.getSortOrders());
        
        String filter = query.getFilter().orElse("");
        return fetch(sortProperties, filter);
    }
    
    @Override
    protected int sizeInBackEnd(Query<Publisher, String> query)
    {
        String filter = query.getFilter().orElse("");
        List<SortProperties> sortProperties = SortProperties.getSortProperties(query.getSortOrders());
        
        return (int) fetch(sortProperties, filter).count();
    }
    
    public Stream<Publisher> fetch(List<SortProperties> sortOrders, String filter)
    {
        List<Publisher> publishers = gdsPublisher.getRecordsByEntityKey("publisherName", filter, sortOrders);
        return fetchDetails(publishers).stream();
        
    }
    
    public List<Publisher> fetchDetails(List<Publisher> publishers)
    {
        //no detail to fetch
        /*publishers.forEach(d ->
                           {
                               d.setContactPerson(GetObjectByID.getObjectById(d.getContactPersonId(), Connection.persons));
                           });*/
        
        return publishers;
    }
}
