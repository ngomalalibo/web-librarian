package com.pc.weblibrarian.dataproviders;

import com.pc.weblibrarian.dataService.Connection;
import com.pc.weblibrarian.dataService.GenericDataService;
import com.pc.weblibrarian.entity.Address;
import com.pc.weblibrarian.entity.Author;
import com.pc.weblibrarian.entity.Person;
import com.pc.weblibrarian.utils.CustomNullChecker;
import com.vaadin.flow.data.provider.AbstractBackEndDataProvider;
import com.vaadin.flow.data.provider.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class AuthorDP extends AbstractBackEndDataProvider<Author, String>
{
    //Initiate a GenerisDataService object and use the methods here to get data for the Data provider
    
    //Choose your JSON API to use depending on what you want to do with the data
    //Use Binding and DOM APIs to map to objects and Streaming to pick out elements from json documents
    
    GenericDataService gdsPerson = new GenericDataService(new Person());
    GenericDataService gdsAuthor = new GenericDataService(new Author());
    
    public AuthorDP()
    {
        super();
    }
    
    // the grid is the Query provider while the dataprovider is the data provider
    @Override
    protected Stream<Author> fetchFromBackEnd(Query<Author, String> query)
    {
        List<SortProperties> sortProperties = SortProperties.getSortProperties(query.getSortOrders());
        
        String filter = query.getFilter().orElse("");
        return fetch(sortProperties, filter);
    }
    
    @Override
    protected int sizeInBackEnd(Query<Author, String> query)
    {
        String filter = query.getFilter().orElse("");
        List<SortProperties> sortProperties = SortProperties.getSortProperties(query.getSortOrders());
    
        return  (int)fetch(sortProperties, filter).count();
        
        /*List<Person> persons = gdsPerson.getEntityByTwoFilterSearch(sortProperties, "firstName", "lastName", filter);
        return fetchDetails(persons).size();*/
        // return PersonDataService.getPersonAndAuthorCount(filter);
    }
    
    
    public Stream<Author> fetch(List<SortProperties> sortOrders, String filter)
    {
        List<Person> persons = gdsPerson.getEntityByTwoFilterSearch(sortOrders, "firstName", "lastName", filter);
        return fetchDetails(persons).stream();
        
        // return PersonDataService.getPersonAndAuthorDetails(sortOrders, filter);
    }
    
    public List<Author> fetchDetails(List<Person> persons)
    {
        List<Author> authors = new ArrayList<>();
        persons.forEach(d ->
                        {
                            List<Address> addresses = new ArrayList<>();
                            Author author;
                            d.getAddressIds().forEach(addressId -> addresses.add(GetObjectByID.getObjectById(addressId, Connection.addresses)));
                            d.setAddressObjects(addresses);
                            author = (Author) gdsAuthor.getRecordByEntityProperty("personId", d.getUuid());
                            if (!CustomNullChecker.nullObjectChecker(author))
                            {
                                author.setPerson(d);
                                authors.add(author);
                            }
                        });
        
        return authors;
    }
    
}
