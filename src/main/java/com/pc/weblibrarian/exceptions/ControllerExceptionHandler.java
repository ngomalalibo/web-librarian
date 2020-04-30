package com.pc.weblibrarian.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
// @ControllerAdvice(annotations = {PreAuthorize.class})
@ControllerAdvice
public class ControllerExceptionHandler
{
    @ResponseStatus(HttpStatus.FORBIDDEN) // Important here. This handleNotFound method is called each time the HttpStatus.NOT_FOUND status occurs
    @ExceptionHandler(AccessDeniedException.class)// This method is called each time the CustomFileNotFoundException is thrown. 2 different views are called to handle same exception. error.html and ApplicationRuntimeExceptionPage
    public ModelAndView handleException(AccessDeniedException exception)
    {
        log.error("Spring Handling custom not found exception");
        log.error("Spring Message -> " + exception.getMessage());
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("error");
        // modelAndView.setViewName("forbidden");
        modelAndView.addObject("exception", exception);
        modelAndView.addObject("code", "Error Code: " + HttpStatus.FORBIDDEN.value());
        return modelAndView;
    }
    
    
}
