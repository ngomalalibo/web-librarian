package com.pc.weblibrarian.utils;

import com.pc.weblibrarian.dataService.AuthorDataService;
import com.pc.weblibrarian.entity.Author;
import com.pc.weblibrarian.entity.PersistingBaseEntity;
import org.bson.Document;

import java.time.LocalDateTime;

public class ModifyEntityContent
{
    public static <T extends PersistingBaseEntity> Document modifyEntity(T t)
    {
//        UpdateResult ur = collection.updateOne(query, Updates.combine(Updates.set("$set", entity), Updates.set("modifiedDate", LocalDateTime.now())), options);
        Document doc = new Document();
        if (t instanceof Author)
        {
            Author author = (Author) t;
            doc.put("wikiLink", author.getWikiLink());
            doc.put("biography", author.getBiography());
            doc.put("modifiedDate", LocalDateTime.now());
            
            Author db = AuthorDataService.getAuthor(t.getUuid());
            
            System.out.println(db.getWikiLink().equals(((Author) t).getWikiLink()) ? "The same" : "Different");
        }
        return doc;
    }
}
