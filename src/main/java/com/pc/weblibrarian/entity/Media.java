package com.pc.weblibrarian.entity;

import com.pc.weblibrarian.enums.MediaAspectRatio;
import com.pc.weblibrarian.enums.MediaRating;
import com.pc.weblibrarian.enums.MediaType;
import com.pc.weblibrarian.enums.VideoRating;
import com.pc.weblibrarian.model.ImageModel;
import com.pc.weblibrarian.model.MetaData;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bson.codecs.pojo.annotations.BsonIgnore;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = false)
public class Media extends PersistingBaseEntity
{
    private static final long serialVersionUID = 1L;
    
    private LocalDate releaseDate;
       private MetaData metaData = new MetaData();
    // private MetaData metaData;
    
    private MediaRating mediaRating;
    private VideoRating videoRating;
    
    private MediaAspectRatio mediaAspectRatio;
    private String mediaGenres;
    
    private MediaType mediaType;
    
    public LocalDate discontinueDate;
    public String mediaItemLocation;
    private ImageModel image = new ImageModel();
    
    private String label;
    private String ASIN;
    private String copyright;
    
    private String authorId;
    
    @BsonIgnore
    private Author author = new Author();
    
    private String publisherId;
    
    @BsonIgnore
    private Publisher publisher = new Publisher();
    
    public Media(LocalDate releaseDate, MetaData metaData, MediaRating mediaRating, VideoRating videoRating, MediaAspectRatio mediaAspectRatio, String mediaGenres, MediaType mediaType, LocalDate discontinueDate, String mediaItemLocation, String label, String ASIN, String copyright, String publisherId)
    {
        super.prepersist(this);
        this.releaseDate = releaseDate;
        this.metaData = metaData;
        this.mediaRating = mediaRating;
        this.videoRating = videoRating;
        this.mediaAspectRatio = mediaAspectRatio;
        this.mediaGenres = mediaGenres;
        this.mediaType = mediaType;
        this.discontinueDate = discontinueDate;
        this.mediaItemLocation = mediaItemLocation;
        this.label = label;
        this.ASIN = ASIN;
        this.copyright = copyright;
        this.publisherId = publisherId;
    }
    
    public Media()
    {
        super();
    }
}
