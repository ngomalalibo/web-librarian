package com.pc.weblibrarian.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ResponseStatus;

// @ResponseStatus(HttpStatus.FORBIDDEN) //map exception to HTTP status so it can be caught with a view
public class CustomAccessDeniedException extends AccessDeniedException
{
    public CustomAccessDeniedException(String msg)
    {
        super(msg);
    }
    
    public CustomAccessDeniedException(String msg, Throwable t)
    {
        super(msg, t);
    }
}
