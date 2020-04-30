package com.pc.weblibrarian.dataproviders;

import com.pc.weblibrarian.dataService.Connection;
import com.pc.weblibrarian.dataService.GenericDataService;
import com.pc.weblibrarian.entity.LibraryItem;
import com.pc.weblibrarian.enums.LibraryItemType;
import com.vaadin.flow.data.provider.AbstractBackEndDataProvider;
import com.vaadin.flow.data.provider.Query;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PublicationDP extends AbstractBackEndDataProvider<LibraryItem, String>
{
    GenericDataService gds = new GenericDataService(new LibraryItem());
    
    public PublicationDP()
    {
        super();
    }
    
    public static LibraryItem getLibraryItemEmbeddedObjects(LibraryItem d)
    {
        if (d.getLibraryItemType().equals(LibraryItemType.PUBLICATION))
        {
            d.setPublication(GetObjectByID.getObjectById(d.getLibraryItemTypeId(), Connection.publication));
            if (d.getPublication() != null)
            {
                
                if (d.getPublication().getPublisherId() != null)
                {
                    d.getPublication().setPublisher(GetObjectByID.getObjectById(d.getPublication().getPublisherId(), Connection.publisher));
                }
                if (d.getPublication().getAuthorId() != null)
                {
                    d.getPublication().setAuthor(GetObjectByID.getObjectById(d.getPublication().getAuthorId(), Connection.authors));
                }
                if (d.getPublication().getAuthor() != null)
                {
                    if (d.getPublication().getAuthor().getPersonId() != null)
                    {
                        d.getPublication().getAuthor().setPerson(GetObjectByID.getObjectById(d.getPublication().getAuthor().getPersonId(), Connection.persons));
                    }
                    if (d.getPublication().getAuthor().getPerson() != null)
                    {
                        d.getPublication().getAuthor().getPerson().setAddressObjects(d.getPublication().getAuthor().getPerson().getAddressIds().stream().map(e -> GetObjectByID.getObjectById(e, Connection.addresses)).collect(Collectors.toList()));
                        //System.out.println("author in getLibraryItemEmbeddedObjects " + d.getPublication().getAuthor().getPerson().getFullName());
                    }
                }
            }
        }
        else if (d.getLibraryItemType().equals(LibraryItemType.MEDIA))
        {
            //System.out.println("item Id " + d.getLibraryItemTypeId());
            d.setMedia(GetObjectByID.getObjectById(d.getLibraryItemTypeId(), Connection.media));
            if (d.getMedia() != null)
            {
                // System.out.println("d.getMedia()");
                if (d.getMedia().getPublisherId() != null)
                {
                    // System.out.println("d.getMedia().getPublisherId() " + d.getMedia().getPublisherId());
                    d.getMedia().setPublisher(GetObjectByID.getObjectById(d.getMedia().getPublisherId(), Connection.publisher));
                }
                if (d.getMedia().getAuthorId() != null)
                {
                    // System.out.println("d.getMedia().getAuthorId() " + d.getMedia().getAuthorId());
                    d.getMedia().setAuthor(GetObjectByID.getObjectById(d.getMedia().getAuthorId(), Connection.authors));
                    // System.out.println("Author  ID " + d.getMedia().getAuthor().getUuid());
                }
                if (d.getMedia().getAuthor() != null)
                {
                    //System.out.println("d.getMedia().getAuthor() ");
                    if (d.getMedia().getAuthorId() != null)
                    {
                        // System.out.println("d.getMedia().getAuthorId() " + d.getMedia().getAuthorId());
                        
                        d.getMedia().getAuthor().setPerson(GetObjectByID.getObjectById(d.getMedia().getAuthor().getPersonId(), Connection.persons));
                        // System.out.println("d.getMedia().getAuthor().getPerson() " + d.getMedia().getAuthor().getPerson());
                    }
                    if (d.getMedia().getAuthor().getPerson() != null)
                    {
                        d.getMedia().getAuthor().getPerson().setAddressObjects(d.getMedia().getAuthor().getPerson().getAddressIds().stream().map(e -> GetObjectByID.getObjectById(e, Connection.addresses)).collect(Collectors.toList()));
                        // System.out.println("author in getLibraryItemEmbeddedObjects " + d.getMedia().getAuthor().getPerson().getFullName());
                    }
                }
            }
        }
        
        return d;
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
        Predicate<LibraryItem> pred = d -> d.getLibraryItemType() == LibraryItemType.PUBLICATION;
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
