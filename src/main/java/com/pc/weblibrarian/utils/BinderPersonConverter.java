package com.pc.weblibrarian.utils;

import com.pc.weblibrarian.dataService.Connection;
import com.pc.weblibrarian.dataproviders.GetObjectByID;
import com.pc.weblibrarian.entity.Author;
import com.pc.weblibrarian.entity.Person;
import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;

public class BinderPersonConverter implements Converter<Person, String>
{
    @Override
    public Result<String> convertToModel(Person person, ValueContext valueContext)
    {
        
        String id = "";
        try
        {
            if (person == null)
            {
                person = Connection.persons.find().first();
            }
            id = person.getUuid();
        }
        catch (NullPointerException npe)
        {
            System.out.println("Cause: " + npe.getCause());
            System.out.println("Message: " + npe.getMessage());
            npe.printStackTrace();
            
        }
        return Result.ok(id);
    }
    
    @Override
    public Person convertToPresentation(String s, ValueContext valueContext)
    {
        Person p = new Person();
        if (!CustomNullChecker.emptyNullStringChecker(s))
        {
            p = GetObjectByID.getObjectById(s, Connection.persons);
        }
        else
        {
            Author first = Connection.authors.find().first();
            if (!CustomNullChecker.nullObjectChecker(first))
            {
                if (!CustomNullChecker.emptyNullStringChecker(first.getPersonId()))
                {
                    p = GetObjectByID.getObjectById(first.getPersonId(), Connection.persons);
                }
            }
        }
        return p;
        
    }
    
}
