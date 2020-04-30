package com.pc.weblibrarian.utils;

import com.mongodb.client.MongoCollection;
import com.pc.weblibrarian.dataService.Connection;

public class GetCollectionFromEntityName
{
    public static MongoCollection getCollectionFromEntityName(String entityName)
    {
        
        return Connection.mapCollectionsAndEntities().get(entityName);
    }
}
