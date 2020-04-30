package com.pc.weblibrarian.dataproviders;

import com.pc.weblibrarian.dataService.Connection;
import com.pc.weblibrarian.dataService.GenericDataService;
import com.pc.weblibrarian.entity.Article;
import com.pc.weblibrarian.entity.Author;
import com.pc.weblibrarian.utils.CustomNullChecker;
import com.vaadin.flow.data.provider.AbstractBackEndDataProvider;
import com.vaadin.flow.data.provider.Query;

import java.util.*;
import java.util.stream.Stream;

public class ArticleDP extends AbstractBackEndDataProvider<Article, String>
{
    GenericDataService gds;
    
    public ArticleDP()
    {
        super();
        gds = new GenericDataService(new Article());
        
    }
    
    @Override
    protected Stream<Article> fetchFromBackEnd(Query<Article, String> query)
    {
        int limit = query.getLimit();
        int offset = query.getOffset();
    
        List<SortProperties> sortProperties =  SortProperties.getSortProperties(query.getSortOrders());
        
        String filter = query.getFilter().orElse("");
        return fetch(sortProperties, filter);
    }
    
    @Override
    protected int sizeInBackEnd(Query<Article, String> query)
    {
        String filter = Objects.requireNonNull(query.getFilter().orElse(""));
    
        List<SortProperties> sortProperties =  SortProperties.getSortProperties(query.getSortOrders());
        long count = fetch(sortProperties, filter).count();
        return (int) count;
    }
    
    public Stream<Article> fetch(List<SortProperties> sortOrder, String filter)
    {
        List<Article> articles = gds.getEntityByTwoFilterSearch(sortOrder, "title", "article", filter);
        return fetchDetails(articles);
    }
    
    public Stream<Article> fetchDetails(List<Article> articles)
    {
        
        List<Article> articleWithDetails = new ArrayList<>();
        
        if (!articles.isEmpty())
        {
            articles.forEach(f ->
                             {
                                 if (!CustomNullChecker.emptyNullStringChecker(f.getAuthorID()))
                                 {
                                     f.setAuthor(GetObjectByID.getObjectById(f.getAuthorID(), Connection.authors));
                                     if (!CustomNullChecker.emptyNullStringChecker(f.getAuthor().getPersonId()))
                                     {
                                         f.getAuthor().setPerson(GetObjectByID.getObjectById(f.getAuthor().getPersonId(), Connection.persons));
                                     }
                    
                                     articleWithDetails.add(f);
                                 }
                             });
        }
        return articleWithDetails.stream();
    }
    
    public static void main(String[] args)
    {
        Connection.startDB();
        ArticleDP cc = new ArticleDP();
        
        Author a = GetObjectByID.getObjectById("ATH-86adde15-8955-41d8-80ea-c38f070c3b87", Connection.authors);
        
        List<Article> entityByTwoFilterSearch = cc.gds.getEntityByTwoFilterSearch(Collections.singletonList(new SortProperties("title", false)), "title", "article", "t");
        Stream<Article> articleStream = cc.fetchDetails(entityByTwoFilterSearch);
        articleStream.forEach(s ->
                              {
                                  System.out.println("Full name from db " + a.getPerson().getFullName());
                                  System.out.println("Full name from stream " + s.getAuthor().getPerson().getFullName());
            
                                  System.out.println(a.getPerson().getFullName().equals(s.getAuthor().getPerson().getFullName()));
                              });
        
        
        // cc.gds.getRecordsByEntityKey("_id", "").collect(Collectors.toList()).forEach(d -> System.out.println("d.getTitle() = " + ((Article) d).getTitle()));
        // System.out.println("Size: "+cc.gds.getRecordsByEntityKey("_id", "").collect(Collectors.toList()).size());
        
        
        Connection.stopDB();
    }
}
