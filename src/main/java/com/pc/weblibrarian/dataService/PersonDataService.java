package com.pc.weblibrarian.dataService;

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.pc.weblibrarian.dataproviders.GetObjectByID;
import com.pc.weblibrarian.dataproviders.SortProperties;
import com.pc.weblibrarian.entity.Address;
import com.pc.weblibrarian.entity.Author;
import com.pc.weblibrarian.entity.Person;
import com.pc.weblibrarian.utils.CustomNullChecker;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@Service
public class PersonDataService
{
    public static Stream<Person> getPersonsByName(List<SortProperties> sort, String personName)
    {
        List<Person> persons = new ArrayList<>();
        Bson match;
        if (CustomNullChecker.emptyNullStringChecker(personName.trim()))
        {
            //fetch all documents. Empty filter
            match = Aggregates.match(new Document());
        }
        else
        {
            Pattern ptrn = Pattern.compile(personName, Pattern.CASE_INSENSITIVE);
            match = Aggregates.match(Filters.or(Filters.regex("firstName", ptrn), Filters.regex("lastName", ptrn)));
        }
        LinkedList<Bson> pipeline = new LinkedList<>();
        pipeline.add(match);
        sort.forEach(ps -> pipeline.add(Aggregates.sort(ps.isAscending() ? Sorts.ascending(ps.getPropertyName()) : Sorts.descending(ps.getPropertyName()))));
        
        Optional<AggregateIterable<Person>> aggregate = Optional.of(Connection.persons.aggregate(pipeline));
        aggregate.get().iterator().forEachRemaining(persons::add);
        
        return persons.stream();
    }
    
    public static Stream<Person> getPersons()
    {
        Optional<FindIterable<Person>> personOptional = Optional.of(Connection.persons.find());
        List<Person> persons = new ArrayList<>();
        personOptional.get().iterator().forEachRemaining(persons::add);
        
        return persons.stream();
    }
    
    public static Stream<Author> getPersonAndAuthorDetails(List<SortProperties> sort, String personName)
    {
        List<Author> authors = new ArrayList<>();
        Bson match;
        if (CustomNullChecker.emptyNullStringChecker(personName.trim()))
        {
            match = Aggregates.match(new Document());
        }
        else
        {
            Pattern ptrn = Pattern.compile(personName, Pattern.CASE_INSENSITIVE);
            match = Aggregates.match(Filters.or(Filters.regex("firstName", ptrn), Filters.regex("lastName", ptrn)));
        }
        
        LinkedList<Bson> pipeline = new LinkedList<>();
        pipeline.add(match);
        for (SortProperties ps : sort)
        {
            pipeline.add(Aggregates.sort(ps.isAscending() ? Sorts.ascending(ps.getPropertyName()) : Sorts.descending(ps.getPropertyName())));
        }
        List<Address> addresses = new ArrayList<>();
        Optional.of(Connection.persons.aggregate(pipeline))
                .ifPresent(e -> e.iterator().forEachRemaining(person ->
                                                              {
                                                                  AtomicReference<Author> author = new AtomicReference<>(new Author());
                                                                  Bson personIdFilter = Aggregates.match(Filters.eq("personId", person.getUuid()));
                                                                  person.getAddressIds().forEach(addressId -> addresses.add(GetObjectByID.getObjectById(addressId, Connection.addresses)));
                                                                  person.setAddressObjects(addresses);
                                                                  Optional.of(Connection.authors.aggregate(Collections.singletonList(personIdFilter)))
                                                                          .ifPresent(d -> d.iterator().forEachRemaining(a ->
                                                                                                                        {
                
                                                                                                                            author.set(a);
                                                                                                                            author.get().setPerson(person);
                                                                                                                            authors.add(author.get());
                                                                                                                        }));
                                                              }));
        return authors.stream();
    }
    
    public static int getPersonAndAuthorCount(String personName)
    {
        Stream<Author> stream;
        List<Author> authors = new ArrayList<>();
        Bson match;
        if (personName.trim().equals(""))
        {
            //fetch all documents
            match = Aggregates.match(new Document());
        }
        else
        {
            Pattern ptrn = Pattern.compile(personName, Pattern.CASE_INSENSITIVE);
            match = Aggregates.match(Filters.or(Filters.regex("firstName", ptrn), Filters.regex("lastName", ptrn)));
        }
        
        LinkedList<Bson> pipeline = new LinkedList<>();
        pipeline.add(match);
        List<Address> addresses = new ArrayList<>();
        Optional<AggregateIterable<Person>> aggregate = Optional.of(Connection.persons.aggregate(pipeline));
        aggregate.get().iterator().forEachRemaining(person ->
                                                    {
                                                        AtomicReference<Author> acRef = new AtomicReference<>(new Author());
            
                                                        Bson personIdFilter = Aggregates.match(Filters.eq("personId", person.getUuid()));
                                                        person.getAddressIds().forEach(addressId -> addresses.add(GetObjectByID.getObjectById(addressId, Connection.addresses)));
                                                        person.setAddressObjects(addresses);
                                                        Optional<AggregateIterable<Author>> authorAggregate = Optional.of(Connection.authors.aggregate(Collections.singletonList(personIdFilter)));
                                                        authorAggregate.get().iterator().forEachRemaining(a ->
                                                                                                          {
                                                                                                              acRef.set(a);
                                                                                                              acRef.get().setPerson(person);
                                                                                                              authors.add(acRef.get());
                                                                                                          });
                                                    });
        stream = authors.stream();
        return (int) stream.count();
    }
    
    
    public static int getPersonCount(String personName)
    {
        Stream<Person> stream;
        List<Person> persons = new ArrayList<>();
        Bson match;
        if (personName.trim().equals(""))
        {
            //fetch all documents
            match = Aggregates.match(new Document());
        }
        else
        {
            Pattern ptrn = Pattern.compile(personName, Pattern.CASE_INSENSITIVE);
            match = Aggregates.match(Filters.or(Filters.regex("firstName", ptrn), Filters.regex("lastName", ptrn)));
        }
        
        LinkedList<Bson> pipeline = new LinkedList<>();
        pipeline.add(match);
        
        Optional<AggregateIterable<Person>> aggregate = Optional.of(Connection.persons.aggregate(pipeline));
        aggregate.get().iterator().forEachRemaining(persons::add);
        stream = persons.stream();
        return (int) stream.count();
    }
    
    public static void main(String[] args)
    {
        Connection.startDB();
        getPersons().forEach(f ->
                             {
                                 System.out.println("person " + f.getFullName());
                             });
        Connection.stopDB();
    }
}
