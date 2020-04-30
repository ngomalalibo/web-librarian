package com.pc.weblibrarian.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pc.weblibrarian.dataService.Connection;
import com.pc.weblibrarian.dataproviders.GetObjectByID;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bson.codecs.pojo.annotations.BsonIgnore;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class Article extends PersistingBaseEntity
{
    private static final long serialVersionUID = 1L;
    
    private String title;
    private String article;
    private String authorID;
    // private List<Comment> comments = new ArrayList<>();
    private double rating;
    
    @BsonIgnore
    @JsonIgnore
    private Author author = new Author();
    
    
    public Article(String title, String article, String authorID, List<Comment> comments, int rating)
    {
        super.prepersist(this);
        this.title = title;
        this.article = article;
        this.authorID = authorID;
        this.author = GetObjectByID.getObjectById(this.authorID, Connection.authors);
        // this.comments = comments;
        this.rating = rating;
    }
    
    public Article()
    {
        super();
    }
}
