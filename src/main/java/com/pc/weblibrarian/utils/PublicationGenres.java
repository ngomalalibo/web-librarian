package com.pc.weblibrarian.utils;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PublicationGenres
{
    public static List<String> getPublicationGenres()
    {
        return Stream.of("Science fiction", "Satire", "Dating", "Drama",
                                                   "Action and Adventure", "Romance", "Mystery", "Horror", "Self help", "Health", "Guide", "Travel",
                                                   "Children's", "Christian Living", "Christmas", "Religion, Spirituality & New Age", "Science", "History",
                                                   "Math", "Anthology", "Poetry", "Encyclopedias", "Dictionaries", "Comics", "Art", "Cookbooks", "Diaries",
                                                   "Journals", "Prayer", "Series", "Trilogy", "Biographies", "Autobiographies", "Fantasy", "Spiritual Growth",
                                                   "Wisdom", "Singles", "Relationship", "Motivational", "Missions", "Men", "Marriage", "Leadership",
                                                   "Holy Spirit", "Finance", "Faith", "Evangelism").sorted(Comparator.naturalOrder()).collect(Collectors.toList());
    }
}
