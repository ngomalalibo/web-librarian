package com.pc.weblibrarian.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bson.codecs.pojo.annotations.BsonIgnore;

//@Entity(value = MDB.DB_PUBLISHER, noClassnameStored = true)
@Data
@EqualsAndHashCode(callSuper = false)
public class Publisher extends PersistingBaseEntity
{
    
    private static final long serialVersionUID = 1L;
    
    private String publisherName;
    private String publisherWebsite;
    private String copyright;
    private String wikiLink;
    
    private String addressText;
    
    /*@BsonIgnore
    private Address address = new Address();*/
    
    private String contactPerson;
    
    
    
    public Publisher(String publisherName, String publisherWebsite, String copyright, String wikiLink, String addressText, String contactPerson)
    {
        super.prepersist(this);
        this.publisherName = publisherName;
        this.publisherWebsite = publisherWebsite;
        this.copyright = copyright;
        this.wikiLink = wikiLink;
        this.addressText = addressText;
        this.contactPerson = contactPerson;
    }
    
    public Publisher()
    {
        super();
    }
}
