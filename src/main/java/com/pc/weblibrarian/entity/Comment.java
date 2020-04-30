package com.pc.weblibrarian.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class Comment extends PersistingBaseEntity
{
    private static final long serialVersionUID = 1L;
    
    private String comment;
    private String commenterId;
    
    public Comment(String comment, String commenterId)
    {
        super.prepersist(this);
        this.comment = comment;
        this.commenterId = commenterId;
    }
    
    public Comment()
    {
        super();
    }
}
