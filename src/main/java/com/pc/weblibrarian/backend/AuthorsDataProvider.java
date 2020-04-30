package com.pc.weblibrarian.backend;

import com.pc.weblibrarian.dataService.AuthorDataService;
import com.pc.weblibrarian.entity.Author;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.DataProviderListener;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.shared.Registration;

import java.util.List;
import java.util.stream.Stream;

public class AuthorsDataProvider implements DataProvider<Author, String>
{
    
    private static DataProvider<Author, Void> dataProvider;
    
    public AuthorsDataProvider()
    {
    
    }
    
    public List<Author> fetch()
    {
        return AuthorDataService.getAuthors();
    }
    
    @Override
    public boolean isInMemory()
    {
        return false;
    }
    
    @Override
    public int size(Query<Author, String> query)
    {
        return (int) AuthorDataService.getAuthors().size();
    }
    
    @Override
    public Stream<Author> fetch(Query<Author, String> query)
    {
        
        return AuthorDataService.getAuthors().stream();
    }
    
    @Override
    public void refreshItem(Author author)
    {
    
    }
    
    @Override
    public void refreshAll()
    {
    
    }
    
    @Override
    public Registration addDataProviderListener(DataProviderListener<Author> dataProviderListener)
    {
        return null;
    }
    
    /*@Override
    protected Stream<Author> fetchFromBackEnd(Query<Author, String> query)
    {
        
        return fetch();
    }
    
    @Override
    protected int sizeInBackEnd(Query<Author, String> q)
    {
        List<Bson> pipeline = new ArrayList<>();
        pipeline.add(Aggregates.count());
        List<Author> count = new ArrayList<>();
        Iterable<Author> iterator = Connection.authors.aggregate(pipeline);
        
        iterator.iterator().forEachRemaining(count::add);
        
        
        return count.size();
    }
    
    @Override
    public void setSortOrders(List<QuerySortOrder> sortOrders)
    {
        super.setSortOrders(sortOrders);
    }*/
}
