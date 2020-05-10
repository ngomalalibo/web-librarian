package com.pc.weblibrarian.utils;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.pc.weblibrarian.dataService.Connection;
import com.pc.weblibrarian.entity.Note;
import com.pc.weblibrarian.entity.PersistingBaseEntity;
import org.bson.Document;

public class POJOToDocumentConverter
{
    public static <T extends PersistingBaseEntity> Document pojoToDocumentConverter(T t)
    {
        // convert pojo to json using Gson and parse using Document.parse()
    
        Gson gson = new Gson();
        Document parse = Document.parse(gson.toJson(t));
        System.out.println("parse = " + parse);
        return parse;
    }
    
    public static void main(String[] args)
    {
        Note note = new Note("Simple note");
        Document doc = POJOToDocumentConverter.pojoToDocumentConverter(note);
        System.out.println("doc = " + doc);
    }
}
