package com.pc.weblibrarian.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pc.weblibrarian.enums.BindingType;
import com.pc.weblibrarian.enums.PublicationType;
import com.pc.weblibrarian.enums.ReleaseCycle;
import com.pc.weblibrarian.model.ImageModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bson.codecs.pojo.annotations.BsonIgnore;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = false)
public class Publication extends PersistingBaseEntity
{
    
    private static final long serialVersionUID = 1L;
    
    private String edition;
    private String genre;
    private String ISBN_13;
    
    private String title;
    private String pubLanguage;
    private String description;
    private String reviewText;
    private String releaseYear;
    private ImageModel image = new ImageModel();
    
    private LocalDate firstPublishedDate;
    private int numberOfPages;
    
    private PublicationType publicationType;
    private ReleaseCycle releaseCycle;
    private BindingType bindingType;
    
    private String authorId;
    
    @BsonIgnore
    @JsonIgnore
    private Author author = new Author();
    
    private String publisherId;
    
    @BsonIgnore
    @JsonIgnore
    private Publisher publisher = new Publisher();
    
    private LocalDate discontinueDate;
    
    public Publication(String edition, String genre, String ISBN_13, String title, String pubLanguage, String description, String reviewText, String releaseYear, ImageModel image, LocalDate firstPublishedDate, int numberOfPages, PublicationType publicationType, ReleaseCycle releaseCycle, BindingType bindingType, String authorId, String publisherId, LocalDate discontinueDate)
    {
        super.prepersist(this);
        this.edition = edition;
        this.genre = genre;
        this.ISBN_13 = ISBN_13;
        this.title = title;
        this.pubLanguage = pubLanguage;
        this.description = description;
        this.reviewText = reviewText;
        this.releaseYear = releaseYear;
        this.image = image;
        this.firstPublishedDate = firstPublishedDate;
        this.numberOfPages = numberOfPages;
        this.publicationType = publicationType;
        this.releaseCycle = releaseCycle;
        this.bindingType = bindingType;
        this.authorId = authorId;
        this.publisherId = publisherId;
        this.discontinueDate = discontinueDate;
    }
    
    public Publication()
    {
        super();
    }
}
