package com.pc.weblibrarian.utils;

import java.util.*;
import java.util.stream.Collectors;

public class GetLanguages
{
    public static Collection<String> getLanguages()
    {
        Set<String> set = new LinkedHashSet<>();
        set.add(Locale.getDefault().getDisplayLanguage());
        set.addAll(Arrays.stream(Locale.getISOLanguages()).map(l -> new Locale(l).getDisplayLanguage()).sorted(Comparator.naturalOrder())
                         .collect(Collectors.toCollection(LinkedHashSet::new)));
        
        
        return set;
    }
    
    public static void main(String[] args)
    {
        GetLanguages.getLanguages().forEach(System.out::println);
        // System.out.println("Language "+new Locale("en").getDisplayLanguage());
        // GetLanguages.getLanguages().stream().filter(d -> d.equals(Locale.getDefault().getDisplayLanguage())).forEach(System.out::print);
    }
}
