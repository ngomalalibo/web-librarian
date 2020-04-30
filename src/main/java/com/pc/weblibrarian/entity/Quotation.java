package com.pc.weblibrarian.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class Quotation extends PersistingBaseEntity
{
    
    private static final long serialVersionUID = 1L;
    
    private String authorId;
    private String quote;
    private List<Comment> comments;
    
    private int averageRating;
    
    public Quotation(String authorId, String quote, List<Comment> comments)
    {
        super.prepersist(this);
        this.authorId = authorId;
        this.quote = quote;
        this.comments = comments;
    }
    
    public Quotation()
    {
        super();
    }
}
