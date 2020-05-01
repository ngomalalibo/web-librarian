package com.pc.weblibrarian.dataService;

import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.pc.weblibrarian.dataproviders.GetObjectByID;
import com.pc.weblibrarian.entity.Author;
import com.pc.weblibrarian.entity.Publication;
import org.bson.conversions.Bson;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class AuthorDataService
{
    public AuthorDataService()
    {
    }
    
    public static Long getAuthorsCount()
    {
        return Optional.of(Connection.authors.countDocuments()).get();
    }
    
    public static Author getAuthor(String id)
    {
        Bson filter = Filters.eq("_id", id);
        return Optional.ofNullable(Connection.authors.find(filter).first()).get();
    }
    
    public static Author getAuthorByName(String authorname)
    {
        Bson filter = Filters.eq("aka", authorname);
        return Optional.ofNullable(Connection.authors.find(filter).first()).get();
    }
    
    public static List<Author> getAuthorsInPublication(Publication pBean)
    {
        Bson filter = Filters.in("_id", pBean.getAuthorId());
        Optional<MongoCursor<Author>> authorOptional = Optional.of(Connection.authors.find(filter).iterator());
        MongoCursor<Author> all;
        List<Author> pubAuthors = null;
        all = authorOptional.get();
        
        while (all.hasNext())
        {
            pubAuthors.add(all.next());
        }
        return pubAuthors;
        /*Query<Author> qry = getDS().createQuery(Author.class).filter("_id", authorIds.get(0));
        for (int n = 1; n < authorIds.size(); n++)
        {
            qry.or(qry.criteria("_id").equal(authorIds.get(n)));
        }
        return qry.asList();*/
    }
    
    //    public static Stream<Author> getAuthors()
    public static Stream<Author> getAuthors(int offset, int limit, String sort, boolean asc)
    {
        Bson skip = Aggregates.skip(offset);
        Bson blimit = Aggregates.limit(limit);
        Bson bsort = asc ? Aggregates.sort(Sorts.ascending("firstName")) : Aggregates.sort(Sorts.descending("firstName"));
        
        List<Bson> pipeline = new ArrayList<>(Arrays.asList(skip, blimit, bsort));
        List<Author> allAuthors = new ArrayList<>();
        Optional.of(Connection.authors.aggregate(pipeline)).ifPresent(d -> d.iterator().forEachRemaining(allAuthors::add));
        
        return allAuthors.stream();
    }
    
    //    public static Stream<Author> getAuthors()
    public static List<Author> getAuthors()
    {
        List<Author> allAuthors = new ArrayList<>();
        Optional.of(Connection.authors.find()).ifPresent(s -> s.iterator().forEachRemaining(allAuthors::add));
        
        return allAuthors;
       /* logger.info(String.format("getAuthors (%d %d %s %b)\n", offset, limit, sort, asc));
        FindOptions opt = new FindOptions();
        opt.skip(offset);
        opt.limit(limit);
        return getAuthorsQuery(sort, asc).asList(opt);*/
    }
    
    public List<Author> getAllAuthors()
    {
        List<Author> allAuthors = new ArrayList<>();
        Optional.of(Connection.authors.find()).ifPresent(s -> s.iterator().forEachRemaining(allAuthors::add));
        
        return allAuthors;
       /* logger.info(String.format("getAuthors (%d %d %s %b)\n", offset, limit, sort, asc));
        FindOptions opt = new FindOptions();
        opt.skip(offset);
        opt.limit(limit);
        return getAuthorsQuery(sort, asc).asList(opt);*/
    }
    
    public static void main(String[] args)
    {
        //Populate (Initialize with data) Java Object, POJO, automatically
        //PodamFactory factory = new PodamFactoryImpl();
        
        Connection.startDB();
        Connection.initializeDatabase();
        List<Author> authors = getAuthors();
        authors.forEach(a -> System.out.println("Author Name: " + GetObjectByID.getObjectById(a.getPersonId(), Connection.persons).getFirstName()));
        
        Connection.stopDB();
    }
}
