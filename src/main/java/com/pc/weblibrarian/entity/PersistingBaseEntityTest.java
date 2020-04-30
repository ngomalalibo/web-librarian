package com.pc.weblibrarian.entity;


import com.pc.weblibrarian.dataService.AuthorDataService;
import com.pc.weblibrarian.dataService.Connection;
import org.junit.Ignore;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PersistingBaseEntityTest
{
    
    @BeforeEach
    void setup()
    {
        Connection.startDB();
    }
    
    @AfterEach
    void tearDown()
    {
        Connection.stopDB();
    }
    
    @Test
    @Ignore
    void generateFakeId()
    {
    }
    
    @org.junit.jupiter.api.Test
    void save()
    {
    }
    
    @org.junit.jupiter.api.Test
    void replaceEntityTest()
    {
        Author newEntity = new Author("wiki", "bio", "PRN1edd4fff-5cb0-466a-9daa-7b313ff88962");
        Author existing = AuthorDataService.getAuthor("ATH-dc087baf-324e-4f2c-912c-0719d9fd172f");
        //newEntity.setUuid(existing.getUuid());
        System.out.println("existing = " + existing);
        System.out.println("newEntity = " + newEntity);
        
        PersistingBaseEntity updated = existing.replaceEntity(existing, newEntity);
        System.out.println("updated = " + updated);
        boolean ff = updated instanceof Author;
        Assertions.assertTrue(ff);
        
        if (ff)
        {
            Author updatedA = (Author) updated;
            Assertions.assertEquals(updatedA.getWikiLink(), newEntity.getWikiLink());
            Assertions.assertEquals(updatedA.getBiography(), newEntity.getBiography());
            Assertions.assertEquals(updatedA.getUuid(), newEntity.getUuid());
        }
        
        
    }
    
    /*@org.junit.jupiter.api.Test
    void updateEntityTest()
    {
        // Author newEntity = new Author("wikilink", "biography", "PRN6ccd3beb-1b6a-4545-9048-8176ff7a77ca");
        Author newEntity = new Author("wiki", "bio", "PRN1edd4fff-5cb0-466a-9daa-7b313ff88962");
        // Author newEntity = new Author("Weeb", "Beeb", "PRN6ccd3beb-1b6a-4545-9048-8176ff7a77ca");
        Author existing = AuthorDataService.getAuthor("ATH-dc087baf-324e-4f2c-912c-0719d9fd172f");
        System.out.println("existing with current data: = " + existing);
        
        boolean b = existing.updateEntity(existing.getUuid(), newEntity);
        Assertions.assertTrue(b);
        
        if (b)
        {
            Author updatedA = AuthorDataService.getAuthor("ATH-dc087baf-324e-4f2c-912c-0719d9fd172f");
            System.out.println("updated = " + updatedA);
            Assertions.assertEquals(updatedA.getWikiLink(), newEntity.getWikiLink());
            Assertions.assertEquals(updatedA.getBiography(), newEntity.getBiography());
            Assertions.assertEquals(updatedA.getPersonId(), newEntity.getPersonId());
            Assertions.assertNotNull(updatedA.getModifiedDate());
        }
    }*/
    
    @org.junit.jupiter.api.Test
    void delete()
    {
    }
}