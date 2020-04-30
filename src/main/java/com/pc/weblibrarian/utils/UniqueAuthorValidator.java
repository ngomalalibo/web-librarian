package com.pc.weblibrarian.utils;


import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.pc.weblibrarian.dataService.Connection;
import com.pc.weblibrarian.entity.Author;
import com.vaadin.flow.data.binder.ErrorLevel;
import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.binder.Validator;
import com.vaadin.flow.data.binder.ValueContext;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class UniqueAuthorValidator implements Validator<String>
{
    @Override
    public ValidationResult apply(String value, ValueContext valueContext)
    {
        Objects.requireNonNull(value);
        Bson bson = Aggregates.match(Filters.eq("personId", value));
        
        List<Author> user = new ArrayList<>();
        Connection.authors.aggregate(Collections.singletonList(bson)).iterator().forEachRemaining(user::add);
        
        //user.forEach(r -> System.out.println("User: " + r));
//        Author user = Connection.authors.aggregate(Collections.singletonList(bson)).first();
        return (user.size() == 0) ? ValidationResult.ok() : ValidationResult.create("This person is already an author", ErrorLevel.ERROR)/*ValidationResult.error("This person is already an author")*/;
    }
}
