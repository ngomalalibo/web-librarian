package com.pc.weblibrarian.utils;

import java.io.File;
import java.net.URL;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

public class GetEntityNamesFromPackage
{
    private static final char PKG_SEPARATOR = '.';
    private static final char DIR_SEPARATOR = '/';
    private static final String CLASS_FILE_SUFFIX = ".class";
    private static final String BAD_PACKAGE_ERROR = "Unable to get resources from path '%s'. Are you sure the package '%s' exists?";
    
    public static Set<String> retrieveEntityNamesFromPackage(String scannedPackage)
    {
        //System.out.println("*** Retrieving entities from package ***");
        String scannedPath = scannedPackage.replace(PKG_SEPARATOR, DIR_SEPARATOR);
        URL scannedUrl = Thread.currentThread().getContextClassLoader().getResource(scannedPath);
        if (scannedUrl == null)
        {
            throw new IllegalArgumentException(String.format(BAD_PACKAGE_ERROR, scannedPath, scannedPackage));
        }
        File scannedDir = new File(scannedUrl.getFile());
        Set<String> classes = new HashSet<String>();
        for (File file : Objects.requireNonNull(scannedDir.listFiles()))
        {
            String name = file.getName();
            if (name.endsWith(CLASS_FILE_SUFFIX))
            {
                int endIndex = name.length() - CLASS_FILE_SUFFIX.length();
                name = name.substring(0, endIndex);
                classes.add(name);
            }
            classes.add(name);
            //classes.addAll(findInDirectory(file, scannedPackage));
        }
        Stream<String> sorted = classes.stream().sorted();
        sorted.forEach(d ->
                       {
                           //System.out.println(d);
                           classes.add(d);
                       });
        return classes;
    }
    
    
    public static void main(String[] args)
    {
        Set<String> entities = GetEntityNamesFromPackage.retrieveEntityNamesFromPackage("com.pc.weblibrarian.entity");
    }
}
