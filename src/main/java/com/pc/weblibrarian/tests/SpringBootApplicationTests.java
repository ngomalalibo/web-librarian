package com.pc.weblibrarian.tests;

import com.pc.weblibrarian.controllers.AuthorController;
import com.pc.weblibrarian.dataService.AuthorDataService;
import com.pc.weblibrarian.dataService.GenericDataService;
import com.pc.weblibrarian.entity.Author;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class SpringBootApplicationTests
{
    
    GenericDataService gds;
    @Mock
    Author a = new Author();
    
    AuthorController controller = new AuthorController(new AuthorDataService());
    
    @BeforeEach
    public void setup()
    {
        MockitoAnnotations.initMocks(this);
        gds = new GenericDataService(a);
        // Connection.startDB();
        
    }
    
    @AfterEach
    void tearDown()
    {
        // Connection.stopDB();
    }
    
    @Test
    public void testMock()
    {
        /*a.setWikiLink("wiki");
        a.setBiography("Bio");
        List<Author> aa = List.of(a);
        
        Mockito.when(gds.getAllOfEntity().collect(Collectors.toList())).thenReturn(aa);
        
        List<Author> authors = ads.getAuthors();
        
        Assertions.assertEquals(authors.get(0).getWikiLink(), "wiki");
        */
    }
    
}
