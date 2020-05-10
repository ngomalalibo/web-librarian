package com.pc.weblibrarian.controllers;

import com.pc.weblibrarian.dataService.AuthorDataService;
import com.pc.weblibrarian.security.SecuredByRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

// import com.vaadin.flow.spring.annotation.SpringComponent;

// @ResponseStatus(HttpStatus.FORBIDDEN) // Important here. This handleNotFound method is called each time the HttpStatus.NOT_FOUND status occurs
// @PreAuthorize(value = "hasAnyRole('ADMIN')")
@SecuredByRole(value = {"ADMIN"})
@Controller
public class AuthorController
{
    @Autowired
    private AuthorDataService authorRepo;
    
    public AuthorController(AuthorDataService authorRepo)
    {
        this.authorRepo = authorRepo;
    }
    
    // @Secured(value = {"ADMIN", "USER", "SUPER_USER"})
    // @ExceptionHandler(AccessDeniedException.class)// This method is called each time the CustomFileNotFoundException is thrown. 2 different views are called to handle same exception. error.html and ApplicationRuntimeExceptionPage
    @RequestMapping("springauthor")
    public String getAuthors(Model model)
    {
        // ChuckNorrisQuotes ch = new ChuckNorrisQuotes();
        // model.addAttribute("joke", ch.getRandomQuote());
        
        model.addAttribute("authors", AuthorDataService.getAuthors());
        return "springauthor";
    }
    
    
}
