package com.pc.weblibrarian.tests;

import com.pc.weblibrarian.dataService.Connection;
import com.pc.weblibrarian.dataService.GenericDataService;
import com.pc.weblibrarian.dataproviders.GetObjectByID;
import com.pc.weblibrarian.dataproviders.SortProperties;
import com.pc.weblibrarian.entity.ActivityLog;
import com.pc.weblibrarian.entity.Author;
import com.pc.weblibrarian.entity.Person;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

class GenericDataServiceTest
{
    @BeforeEach
    public void setup()
    {
        Connection.startDB();
    }
    
    @AfterEach
    public void tearDown()
    {
        Connection.stopDB();
    }
    
    @Test
    void getEntityByKeyCount()
    {
        int count = 1_7;
        
        GenericDataService gds = new GenericDataService(new Author());
        int entityCount = gds.getEntityByKeyCount("_id", "");
        
        Assertions.assertEquals(entityCount, count);
    }
    
    
    @Test
    void getRecordsByEntityKeyTest()
    {
        String id = "PRNfcca6680-6510-4677-9ab3-293b5d6a5c9f";
        GenericDataService gds = new GenericDataService(new Person());
        List<SortProperties> sortOrder = new ArrayList<>();
        List<Person> id1 = gds.getRecordsByEntityKey("_id", id, sortOrder);
        
        // Assertions.assertEquals(id, id1.findFirst().get().getUuid());
        
    }
    
    @Test
    void getRecordByEntityId()
    {
        String key = "lastName";
        String value = "McKenzie";
        GenericDataService gds = new GenericDataService(new Person());
        Person recordByEntityId = (Person) gds.getRecordByEntityProperty(key, value);
        
        Assertions.assertEquals(value, recordByEntityId.getLastName());
    }
    
    @Test
    void getRecordAndEmbeddedObjectListTest()
    {
        String id = "ATH-2ca19612-147d-4b51-a767-a521e2c44a1a";
        String personId = "PRNfcca6680-6510-4677-9ab3-293b5d6a5c9f";
        
        Author a = GetObjectByID.getObjectById(id, Connection.authors);
        GenericDataService gds = new GenericDataService(new Person());
        
        List<Person> p = gds.getRecordsByEntityKey("_id", personId, Collections.singletonList(new SortProperties("", true)));
        
        Map<Author, List<Person>> recordAndEmbeddedObjects = gds.getRecordAndEmbeddedObjectList("Author", "Person", "_id", id, "personId", personId);
        recordAndEmbeddedObjects.forEach((f, g) ->
                                         {
                                             Assertions.assertEquals(f.getUuid(), a.getUuid());
                                             Assertions.assertEquals(g.get(0).getUuid(), a.getPersonId());
            
                                             Assertions.assertEquals(g.get(0).getUuid(), p.get(0).getUuid());
                                             Assertions.assertEquals(g.get(0).getLastName(), p.get(0).getLastName());
                                         });
    }
    
    @Test
    void getEntitiesSortedTest()
    {
        String firstName1 = "Aaron";
        String firstName2 = "Aliza";
        String firstName3 = "Cira";
        
        String sort = "firstName";
        boolean asc = true;
        GenericDataService gds = new GenericDataService(new Person());
        Stream entitiesSorted = gds.getEntitiesSorted(sort, asc).limit(3);
        AtomicInteger count = new AtomicInteger(0);
        
        entitiesSorted.forEach((d) ->
                               {
                                   if (count.get() == 0)
                                   {
                                       Assertions.assertEquals(firstName1, ((Person) d).getFirstName());
                                   }
                                   if (count.get() == 1)
                                   {
                                       Assertions.assertEquals(firstName2, ((Person) d).getFirstName());
                                   }
                                   if (count.get() == 2)
                                   {
                                       Assertions.assertEquals(firstName3, ((Person) d).getFirstName());
                                   }
                                   count.incrementAndGet();
                               });
    }
    
    @Test
    void getAllOfEntityTest()
    {
        long expected = 206;
        GenericDataService gds = new GenericDataService(new ActivityLog());
        long returned = gds.getAllOfEntity().count();
        Assertions.assertEquals(expected, returned);
    }
    
    @Test
    void getEntityByTwoFilterSearchTest()
    {
        
        /**db.getCollection("persons").find({$or: [{"firstName": {$regex: /kenz/i}}, {"lastName": {$regex: /kenz/i}}]})*/
        GenericDataService gds = new GenericDataService(new Person());
        List<Person> entityByTwoFilterSearch = gds.getEntityByTwoFilterSearch(Collections.singletonList(new SortProperties("firstName", true)), "firstName", "lastName", "kenz");
        
        entityByTwoFilterSearch.stream().findFirst().ifPresent(h ->
                                                               {
                                                                   Assertions.assertEquals("Van", h.getFirstName());
                                                                   Assertions.assertEquals("McKenzie", h.getLastName());
                                                               });
        /** > also works entityByTwoFilterSearch.forEach(h ->
         {
         Assertions.assertEquals("Van", h.getFirstName());
         Assertions.assertEquals("McKenzie", h.getLastName());
         });*/
    }
    
    @Test
    void getEntityJoinTest()
    {
        String personId = "PRNfcca6680-6510-4677-9ab3-293b5d6a5c9f";
        String authId = "ATH-2ca19612-147d-4b51-a767-a521e2c44a1a";
        
        String wiki = "www.xjoanna-hermiston.biz";
        String lastName = "McKenzie";
        
        GenericDataService gds = new GenericDataService(new Author());
        Map<Author, List<Person>> entityJoin = gds.getEntityJoin("kenz", "_id", personId, "Person", "firstName", "lastName");
        
        
        // AtomicReference<AuthorCommand> ac = new AtomicReference<>(new AuthorCommand());
        entityJoin.forEach((e, f) ->
                           {
                               e.setPerson(entityJoin.get(e).get(0));
                               Assertions.assertEquals(wiki, e.getWikiLink());
                               Assertions.assertEquals(lastName, e.getPerson().getLastName());
                               Assertions.assertEquals(lastName, f.stream().findFirst().get().getLastName());
                           });
        
    }
}