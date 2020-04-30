package com.pc.weblibrarian.dataproviders;

import com.pc.weblibrarian.dataService.GenericDataService;
import com.pc.weblibrarian.entity.LibraryItem;
import com.pc.weblibrarian.enums.LibraryItemType;
import com.vaadin.flow.data.provider.AbstractBackEndDataProvider;
import com.vaadin.flow.data.provider.Query;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class MediaDP extends AbstractBackEndDataProvider<LibraryItem, String>
{
    GenericDataService gds = new GenericDataService(new LibraryItem());
    
    public MediaDP()
    {
        super();
    }
    
    @Override
    protected Stream<LibraryItem> fetchFromBackEnd(Query<LibraryItem, String> query)
    {
        List<SortProperties> sortProperties = SortProperties.getSortProperties(query.getSortOrders());
        
        String filter = query.getFilter().orElse("");
        return fetch(sortProperties, filter).stream();
    }
    
    
    @Override
    protected int sizeInBackEnd(Query<LibraryItem, String> query)
    {
        List<SortProperties> sortProperties = SortProperties.getSortProperties(query.getSortOrders());
        
        String filter = query.getFilter().orElse("");
        return fetch(sortProperties, filter).size();
    }
    
    public List<LibraryItem> fetch(List<SortProperties> sortOrders, String filter)
    {
        List<LibraryItem> libraryItem = gds.getRecordsByEntityKey("libraryItemName", filter, sortOrders);
        Predicate<LibraryItem> pred = d -> d.getLibraryItemType()==LibraryItemType.MEDIA;
        return libraryItem.stream().filter(pred).map(PublicationDP::getLibraryItemEmbeddedObjects).collect(Collectors.toList());
    }
    
    /*public static void main(String[] args)
    {
        Connection.startDB();
        GenericDataService gds = new GenericDataService(new LibraryItem());
        List<LibraryItem> collect = new ArrayList<>();
        
        List<LibraryItem> libraryItem = gds.getRecordsByEntityKey("libraryItemName", "", List.of(new SortProperties("libraryItemName", true)));
        libraryItem.forEach(d ->
                            {
                                if (d.getLibraryItemType().equals(LibraryItemType.PUBLICATION))
                                {
                                    d.setPublication(GetObjectByID.getObjectById(d.getLibraryItemTypeId(), Connection.publication));
                                    d.getPublication().setPublisher(GetObjectByID.getObjectById(d.getPublication().getPublisherId(), Connection.publisher));
                                }
                                else if (d.getLibraryItemType().equals(LibraryItemType.MEDIA))
                                {
                                    d.setMedia(GetObjectByID.getObjectById(d.getLibraryItemTypeId(), Connection.media));
                                    d.getMedia().setPublisher(GetObjectByID.getObjectById(d.getMedia().getPublisherId(), Connection.publisher));
                                }
                                collect.add(d);
                            });
        System.out.println("publication info = " + collect.get(0).getPublication());
    }*/
}
