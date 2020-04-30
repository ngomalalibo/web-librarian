package com.pc.weblibrarian.utils;

import com.pc.weblibrarian.dataService.Connection;
import com.pc.weblibrarian.entity.PersistingBaseEntity;

public class GetObjectFromEntityName<B extends PersistingBaseEntity>
{
    public static <B> B getObjectFromEntityName(String entityName)
    {
        return (B) Connection.mapObjectAndEntityNames().get(entityName);
    }
}
