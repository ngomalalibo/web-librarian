package com.pc.weblibrarian.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Properties;
import java.util.Random;

public class LoadProperties
{
    private static Properties properties;
    private static Logger logger = LoggerFactory.getLogger(LoadProperties.class);
    
    public static String getProperty(String propertyKey) throws IOException
    {
        try
        {
            if (properties == null)
            {
                LoadProperties.init();
            }
        }
        catch (IOException io)
        {
        
        }
        
        
        return properties.getProperty(propertyKey);
    }
    
    public static void init() throws IOException
    {
        properties = new Properties();
        logger.info("---- loading properties from application.properties file");
        properties.load(ClassLoader.getSystemResourceAsStream("application.properties"));
        logger.info("---- properties loaded successfully");
    }
    
    public String randomText()
    {
        return randomText(10);
    }
    
    public String randomText(int size)
    {
        byte[] array = new byte[size];
        new Random().nextBytes(array);
        return new String(array, Charset.forName("UTF-8"));
    }
}