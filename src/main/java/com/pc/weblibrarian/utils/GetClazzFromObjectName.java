package com.pc.weblibrarian.utils;

import com.pc.weblibrarian.dataService.Connection;

public class GetClazzFromObjectName
{
    public static Class getClazzFromObject(String className)
    {
        return Connection.mapObjectAndClazzNames().get(className);
    }
}
