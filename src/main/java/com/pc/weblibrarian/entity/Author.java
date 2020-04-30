package com.pc.weblibrarian.entity;

import com.pc.weblibrarian.dataService.Connection;
import com.pc.weblibrarian.dataproviders.GetObjectByID;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bson.codecs.pojo.annotations.BsonIgnore;

//@Entity(value = MDB.DB_AUTHOR, noClassnameStored = true)
@Data
@EqualsAndHashCode(callSuper = false)
public class Author extends PersistingBaseEntity
{
    private static final long serialVersionUID = 1L;
    
    private String biography;
    private String wikiLink;
    private String personId;
    
    @BsonIgnore
    // @JsonIgnore
    private Person person = new Person();
    
    public Author(String wikiLink, String biography, String personId)
    {
        super.prepersist(this);
        this.wikiLink = wikiLink;
        this.biography = biography;
        this.personId = personId;
        this.person = (GetObjectByID.getObjectById(this.personId, Connection.persons));
    }
    
    public Author()
    {
        super();
    }
}
