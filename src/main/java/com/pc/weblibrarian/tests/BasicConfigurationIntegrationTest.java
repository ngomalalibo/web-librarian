package com.pc.weblibrarian.tests;

import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BasicConfigurationIntegrationTest
{
    
    org.springframework.boot.test.web.client.TestRestTemplate restTemplate;
    URL base;
    @LocalServerPort
    int port;
    
    @Before
    public void setUp() throws MalformedURLException
    {
        restTemplate = new TestRestTemplate("user", "password");
        base = new URL("http://localhost:" + port);
    }
    
    @Test
    public void whenLoggedUserRequestsHomePage_ThenSuccess()
            throws IllegalStateException, IOException
    {
        ResponseEntity<String> response
                = restTemplate.getForEntity(base.toString(), String.class);
        
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertTrue(response
                                      .getBody()
                                      .contains("Baeldung"));
    }
    
    @Test
    public void whenUserWithWrongCredentials_thenUnauthorizedPage()
            throws Exception
    {
        
        restTemplate = new TestRestTemplate("user", "wrongpassword");
        ResponseEntity<String> response
                = restTemplate.getForEntity(base.toString(), String.class);
        
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        Assertions.assertTrue(response
                                      .getBody()
                                      .contains("Unauthorized"));
    }
}