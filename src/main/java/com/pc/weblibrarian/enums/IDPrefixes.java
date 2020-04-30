package com.pc.weblibrarian.enums;

public enum IDPrefixes
{
    Author, Publisher, ActivityLog, AppConfiguration, Person, Quotes, Orders, Article, Note, Comment,
    PersistingBaseEntity, CheckInCheckOut, LibraryItem, LibraryUser, Media, Organization, Publication, UserVerification, WaitingList, File, Image, Address, Country, State, OrderItem;
    
    public static String getDisplayText(IDPrefixes idPrefix)
    {
        switch (idPrefix)
        {
            case Author:
                return "ATH-";
            case Publisher:
                return "PUB";
            case ActivityLog:
                return "LOG";
            case AppConfiguration:
                return "AC";
            case PersistingBaseEntity:
                return "";
            case CheckInCheckOut:
                return "CICO";
            case LibraryItem:
                return "ITM-";
            case LibraryUser:
                return "USR";
            case Media:
                return "MDA";
            case Organization:
                return "ORG";
            case Publication:
                return "PBC";
            case UserVerification:
                return "USR-V";
            case WaitingList:
                return "WTL";
            case File:
                return "FILE";
            case Image:
                return "IMAGE";
            case Address:
                return "ADR";
            case Person:
                return "PRN";
            case Quotes:
                return "QUT";
            case Orders:
                return "ORD";
            case Article:
                return "ART";
            case Note:
                return "NOTE";
            case Comment:
                return "CMNT";
            case Country:
                return "CTRY";
            case State:
                return "ST";
            case OrderItem:
                return "ORDITEM";
            default:
                return "";
        }
    }
    
    public static <T> String getIdPrefix(T t)
    {
        
        String simpleName = t.getClass().getSimpleName();
        //System.out.println("Simple Class Name: " + t.getClass().getSimpleName());
        
        IDPrefixes idPrefixes = IDPrefixes.valueOf(simpleName);
        switch (idPrefixes)
        {
            case Author:
                return IDPrefixes.getDisplayText(IDPrefixes.Author);
            case Publisher:
                return IDPrefixes.getDisplayText(IDPrefixes.Publisher);
            case ActivityLog:
                return IDPrefixes.getDisplayText(IDPrefixes.ActivityLog);
            case AppConfiguration:
                return IDPrefixes.getDisplayText(IDPrefixes.AppConfiguration);
            case PersistingBaseEntity:
                return IDPrefixes.getDisplayText(IDPrefixes.PersistingBaseEntity);
            case CheckInCheckOut:
                return IDPrefixes.getDisplayText(IDPrefixes.CheckInCheckOut);
            case LibraryItem:
                return IDPrefixes.getDisplayText(IDPrefixes.LibraryItem);
            case LibraryUser:
                return IDPrefixes.getDisplayText(IDPrefixes.LibraryUser);
            case Media:
                return IDPrefixes.getDisplayText(IDPrefixes.Media);
            case Organization:
                return IDPrefixes.getDisplayText(IDPrefixes.Organization);
            case Publication:
                return IDPrefixes.getDisplayText(IDPrefixes.Publication);
            case UserVerification:
                return IDPrefixes.getDisplayText(IDPrefixes.UserVerification);
            case WaitingList:
                return IDPrefixes.getDisplayText(IDPrefixes.WaitingList);
            case Address:
                return IDPrefixes.getDisplayText(IDPrefixes.Address);
            case Person:
                return IDPrefixes.getDisplayText(IDPrefixes.Person);
            case Orders:
                return IDPrefixes.getDisplayText(IDPrefixes.Orders);
            case OrderItem:
                return IDPrefixes.getDisplayText(IDPrefixes.OrderItem);
            case Article:
                return IDPrefixes.getDisplayText(IDPrefixes.Article);
            case Note:
                return IDPrefixes.getDisplayText(IDPrefixes.Note);
            case Comment:
                return IDPrefixes.getDisplayText(IDPrefixes.Comment);
            case File:
                return IDPrefixes.getDisplayText(IDPrefixes.File);
            case Image:
                return IDPrefixes.getDisplayText(IDPrefixes.Image);
            case Quotes:
                return IDPrefixes.getDisplayText(IDPrefixes.Quotes);
            case Country:
                return IDPrefixes.getDisplayText(IDPrefixes.Country);
            case State:
                return IDPrefixes.getDisplayText(IDPrefixes.State);
            default:
                return "";
        }
    }
}
