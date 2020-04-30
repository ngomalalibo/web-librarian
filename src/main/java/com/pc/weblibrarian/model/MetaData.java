package com.pc.weblibrarian.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
public class MetaData implements Serializable
{
    private static final long serialVersionUID = 1L;
    
    private String season;
    private String episode;
    private String director;
    private String specialFeatures;
    private String castAndCrew;
    
    private String mediaDurationMinutes;
    private double userRating; // 4/5 stars
    
    public MetaData(String season, String episode, String director, String specialFeatures, String castAndCrew, String mediaDurationMinutes, double userRating)
    {
        this.season = season;
        this.episode = episode;
        this.director = director;
        this.specialFeatures = specialFeatures;
        this.castAndCrew = castAndCrew;
        this.mediaDurationMinutes = mediaDurationMinutes;
        this.userRating = userRating;
    }
    
    public MetaData()
    {
    
    }
}
