package com.pc.weblibrarian.utils;

import com.pc.weblibrarian.dataService.Connection;
import com.pc.weblibrarian.dataproviders.GetObjectByID;
import com.pc.weblibrarian.entity.Author;
import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;

public class BinderAuthorConverter implements Converter<Author, String>
{
    
    @Override
    public Result<String> convertToModel(Author author, ValueContext valueContext)
    {
        String id = "";
        if (author != null && author.getUuid() != null)
        {
            id = author.getUuid();
        }
        else
        {
            try
            {
                Author first = Connection.authors.find().first();
                if (first != null && first.getUuid() != null)
                {
                    id = first.getUuid();
                }
            }
            catch (NullPointerException npe)
            {
                System.out.println("Cause: " + npe.getCause());
                System.out.println("Message: " + npe.getMessage());
                npe.printStackTrace();
            }
        }
        return Result.ok(id);
    }
    
    @Override
    public Author convertToPresentation(String modelId, ValueContext valueContext)
    {
        Author author = new Author();
        if (!CustomNullChecker.emptyNullStringChecker(modelId))
        {
            author = GetObjectByID.getObjectById(modelId, Connection.authors);
            
            if (author != null && author.getPersonId() != null)
            {
                author.setPerson(GetObjectByID.getObjectById(author.getPersonId(), Connection.persons));
                // System.out.println("author in convertToPresentation " + author.getPerson().getFullName());
            }
            // System.out.println("person " + author.getPerson());
        }
        else
        {
            author = Connection.authors.find().first();
            if (author != null && author.getPerson() != null)
            {
                // System.out.println("author in convertToPresentation " + author.getPerson().getFullName());
                author.setPerson(GetObjectByID.getObjectById(author.getPersonId(), Connection.persons));
            }
            else
            {
                throw new NullPointerException("Author not found");
            }
        }
        return author;
    }
    
    
}
