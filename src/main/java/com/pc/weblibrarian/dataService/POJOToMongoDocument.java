package com.pc.weblibrarian.dataService;

import com.google.gson.Gson;
import com.mongodb.client.MongoCollection;
import com.pc.weblibrarian.entity.Address;
import org.bson.Document;

public class POJOToMongoDocument
{
    
    public static void main(String[] args)
    {
        // create client and connect to db
        //Connection.startDB();
        
        // populate pojo
        Address myPOJO = new Address();
        
        
        // convert pojo to json using Gson and parse using Document.parse()
        Gson gson = new Gson();
        Document document = Document.parse(gson.toJson(myPOJO));
        System.out.println("Converted address document " + document);
        //Connection.stopDB();
    }
    
}