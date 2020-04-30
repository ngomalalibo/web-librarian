package com.pc.weblibrarian.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import com.pc.weblibrarian.backend.Persistable;
import com.pc.weblibrarian.dataService.Connection;
import com.pc.weblibrarian.enums.ActivityLogType;
import com.pc.weblibrarian.enums.IDPrefixes;
import com.pc.weblibrarian.utils.CustomNullChecker;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.conversions.Bson;

import javax.persistence.PrePersist;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Base class for all persistable data
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Slf4j
public class PersistingBaseEntity implements Serializable, Persistable, Cloneable, CustomNullChecker
{
    private static final long serialVersionUID = 1L;
    // static Logger log = LoggerFactory.getLogger(PersistingBaseEntity.class);
    
    @BsonProperty("_id")
    @JsonProperty("_id")
    private String uuid;
    private String createdBy;
    private String organization = "Web Librarian";
    private LocalDateTime createdDate;
    private String modifiedBy;
    private LocalDateTime modifiedDate;
    private String archivedBy;
    private LocalDateTime archivedDate;
    
    @BsonIgnore
    @JsonIgnore
    private ActivityLog activityLog;
    
    @BsonIgnore
    @JsonIgnore
    Bson deleteFilter;
    @BsonIgnore
    @JsonIgnore
    MongoCollection collection;
    @BsonIgnore
    @JsonIgnore
    DeleteResult deleteResult;
    // @BsonIgnore
    // @JsonIgnore
    // boolean b = false;
    
    public PersistingBaseEntity()
    {
        super();
        prepersist(this);
    }
    
    @Override
    public Object clone() throws CloneNotSupportedException
    {
        return super.clone();
    }
    
    public <T extends PersistingBaseEntity> PersistingBaseEntity(T t)
    {
        prepersist(t);
    }
    
    /*public String getSortingId()
    {
        return uuid;
    }*/
    
    public <T extends PersistingBaseEntity> String generateFakeId(T t)
    {
        String id = "";
        if (t != null)
        {
            id = IDPrefixes.getIdPrefix(t) + UUID.randomUUID().toString();
            
            
            //System.out.println("uuid: " + t.getUuid());
            return id;
        }
        return id;
    }
    
    @PrePersist
    @Override
    public <T extends PersistingBaseEntity> void prepersist(T t)
    {
        
        //System.out.println("***Pre-persist called***");
        if (!CustomNullChecker.nullObjectChecker(t) && CustomNullChecker.emptyNullStringChecker(t.getUuid()))
        {
            t.setUuid(IDPrefixes.getIdPrefix(t) + UUID.randomUUID().toString());
        }
        if (!CustomNullChecker.nullObjectChecker(t) && t.getCreatedDate() == null)
        {
            //System.out.println("****Stamp Audit date and User for new user****");
            t.setCreatedBy("Ngo"/*getSessionUser()*/);
            t.setCreatedDate(LocalDateTime.now());
        }
        else
        {
            //System.out.println(t.getUuid());
            t.setModifiedBy("***System***");
            t.setModifiedDate(LocalDateTime.now());
        }
            /*if (t.getArchivedBy() != null  && entity deleted status is true)
            {
                t.setArchivedBy("***System***");
                t.setArchivedDate(LocalDateTime.now());
            }*/
        
        
    }
    
    public <T extends PersistingBaseEntity> MongoCollection getPersistingCollectionFromClass(T t)
    {
        if (!CustomNullChecker.nullObjectChecker(t))
        {
            String simpleName = t.getClass().getSimpleName();
            //System.out.println("IDPrefixes.valueOf(simpleName): " + IDPrefixes.valueOf(simpleName));
            
            Map<IDPrefixes, MongoCollection<?>> map = Connection.mapCollectionsAndIDPrefixes();
            IDPrefixes idPrefix = IDPrefixes.valueOf(simpleName);
            return map.get(idPrefix);
        }
        
        else
        {
            throw new NullPointerException("no collection with provided class name");
        }
    
      /*if(true) return  Arrays.stream(Connection.class.getDeclaredFields())
                             .peek(pk -> pk.setAccessible(Boolean.TRUE)).filter(f -> f.getName().equals(idPrefix.toString())).findFirst().map(e -> {
                  try
                  {
                      Connection obj = (Connection) e.get(this);
                      return obj;
                  }
                  catch(IllegalArgumentException | IllegalAccessException ex){
                  
                  }
        }).orElse(null);*/
        
        //System.out.println("*****");
        /*switch (idPrefix)
        {
            case Author:
                return Connection.authors;
            case Publisher:
                return Connection.publisher;
            case ActivityLog:
                return Connection.activityLog;
            case AppConfiguration:
                return Connection.appConfig;
            case CheckInCheckOut:
                return Connection.checkInOut;
            case LibraryItem:
                return Connection.libraryItem;
            case LibraryUser:
                return Connection.libraryUser;
            case Media:
                return Connection.media;
            case Organization:
                return Connection.organization;
            case Publication:
                return Connection.publication;
            case UserVerification:
                return Connection.userVerification;
            case WaitingList:
                return Connection.waitingList;
            case Address:
                return Connection.addresses;
            case Person:
                return Connection.persons;
            case Quotes:
                return Connection.quotes;
            case Orders:
                return Connection.orders;
            case Article:
                return Connection.articles;
            case Notes:
                return Connection.notes;
            case Comments:
                return Connection.comments;
            default:
                return null;
    }*/
    
    }
    
    /*public <T extends PersistingBaseEntity> String getClassNameFromObject(T t)
    {
        String simpleName = t.getClass().getSimpleName();
        //System.out.println("IDPrefixes.valueOf(simpleName): " + IDPrefixes.valueOf(simpleName));
        
        Map<IDPrefixes, String> map = mapClassesAndIDPrefixes();
        IDPrefixes idPrefix = IDPrefixes.valueOf(simpleName);
        return map.get(idPrefix);
    }
    
    public static HashMap<IDPrefixes, String> mapClassesAndIDPrefixes()
    {
        return new HashMap<IDPrefixes, String>()
        {{
            put(IDPrefixes.Author, Author.class.getSimpleName());
            put(IDPrefixes.Publisher, Publisher.class.getSimpleName());
            put(IDPrefixes.ActivityLog, ActivityLog.class.getSimpleName());
            put(IDPrefixes.AppConfiguration, AppConfiguration.class.getSimpleName());
            put(IDPrefixes.Person, Person.class.getSimpleName());
            put(IDPrefixes.Quotes, Quote.class.getSimpleName());
            put(IDPrefixes.Orders, Order.class.getSimpleName());
            put(IDPrefixes.Article, Article.class.getSimpleName());
            put(IDPrefixes.Notes, Note.class.getSimpleName());
            put(IDPrefixes.Comments, Comment.class.getSimpleName());
            put(IDPrefixes.CheckInCheckOut, CheckInCheckOut.class.getSimpleName());
            put(IDPrefixes.LibraryItem, LibraryItem.class.getSimpleName());
            put(IDPrefixes.LibraryUser, LibraryUser.class.getSimpleName());
            put(IDPrefixes.Media, Media.class.getSimpleName());
            put(IDPrefixes.Organization, Organization.class.getSimpleName());
            put(IDPrefixes.Publication, Publication.class.getSimpleName());
            put(IDPrefixes.UserVerification, UserVerification.class.getSimpleName());
            put(IDPrefixes.WaitingList, WaitingList.class.getSimpleName());
            put(IDPrefixes.Address, Address.class.getSimpleName());
        }};
    }*/
    
    
    /**
     * Performs all saving and logging operations
     */
    public <T extends PersistingBaseEntity> PersistingBaseEntity save(T t)
    {
        MongoCollection collection;
        MongoCollection logCollection;
        final AtomicReference<T> gt = new AtomicReference<>(t);
        
        try
        {
            
            if (!CustomNullChecker.emptyNullStringChecker(t.getUuid()))
            {
                collection = getPersistingCollectionFromClass(t);
                
                // System.out.println("Saving " + t.getClass().getSimpleName());
                collection.insertOne(t);
                log.info("Saved " + t.getClass().getSimpleName());
                
                
                activityLog = new ActivityLog("User", "Saved(" + t.getClass().getSimpleName() + " with " + uuid + ") ",
                                              "Saved(" + t.getClass().getSimpleName() + " with " + uuid + ") ", ActivityLogType.INFO, t.getClass().getSimpleName());
                
                //System.out.println("****Inserting activity Log****");
                logCollection = getPersistingCollectionFromClass(activityLog);
                
                logCollection.insertOne(activityLog);
                
                //System.out.println("****Activity Log Completed****");
                Bson query = Filters.eq("_id", t.getUuid());
                Optional.ofNullable(collection.find(query)).ifPresentOrElse(d -> gt.set((T) d.iterator().tryNext()), () -> gt.set((T) activityLog));
            }
            else
            {
                throw new RuntimeException("cannot save entity with blank or null id");
            }
        }
        catch (Exception me)
        {
            System.out.println("Exception.getMessage() = " + me.getMessage() + " Cause: " + me.getCause());
            activityLog = new ActivityLog("User", "Not Saved(" + t.getClass().getSimpleName() + " with " + uuid + ") ",
                                          "Error Message" + me.getMessage(), ActivityLogType.ERROR, t.getClass().getSimpleName());
            me.printStackTrace();
        }
        
        return gt.get();
    }
    
    public <T extends PersistingBaseEntity> T replaceEntity(T oldEntity, T newEntity)
    {
        MongoCollection collection;
        MongoCollection logCollection;
        T gt = oldEntity;
        
        try
        {
            if (!CustomNullChecker.emptyNullStringChecker(newEntity.getUuid()))
            {
                collection = getPersistingCollectionFromClass(oldEntity);
                Bson query = Filters.eq("_id", oldEntity.getUuid());
                newEntity.setUuid(oldEntity.getUuid());
                collection.replaceOne(query, newEntity);
                
                gt = newEntity;
                
                activityLog = new ActivityLog("User", "Updated(" + newEntity.getClass().getSimpleName() + " with " + uuid + ") ",
                                              "Updated author:" + newEntity + " with " + uuid + ") ", ActivityLogType.INFO, newEntity.getClass().getSimpleName());
                
                logCollection = getPersistingCollectionFromClass(activityLog);
                
                logCollection.insertOne(activityLog);
                log.info("Updated " + oldEntity.getClass().getSimpleName());
            }
            else
            {
                throw new NullPointerException("attempting to replace a non existing entity");
            }
        }
        catch (Exception me)
        {
            gt = (T) activityLog;
            System.out.println("Exception.getMessage() = " + me.getMessage() + " Cause: " + me.getCause());
            activityLog = new ActivityLog("User", "Not Saved(" + newEntity.getClass().getSimpleName() + " with " + uuid + ") ",
                                          "Error Message" + me.getMessage(), ActivityLogType.ERROR, newEntity.getClass().getSimpleName());
            logCollection = getPersistingCollectionFromClass(activityLog);
            logCollection.insertOne(activityLog);
        }
        
        return gt;
    }
    
    /*public <T extends PersistingBaseEntity> boolean updateEntity(String exitingEntityId, T newEntity)
    {
        boolean result = false;
        MongoCollection collection;
        MongoCollection logCollection;
        T gt = newEntity;
        
        UpdateOptions options = new UpdateOptions();
        options.upsert(true);
        options.bypassDocumentValidation(true);
        
        try
        {
            if (newEntity != null && !CustomNullChecker.emptyNullStringChecker(exitingEntityId))
            {
                newEntity.setUuid(exitingEntityId);
                Bson query = Filters.eq("_id", exitingEntityId);
                collection = getPersistingCollectionFromClass(newEntity);
                
                Document document = POJOToDocumentConverter.pojoToDocumentConverter(newEntity);
                UpdateResult ur = collection.updateOne(query, document, options);
                //System.out.println("document = " + document);
                //System.out.println("entity = " + entity);
                
                //UpdateResult updateResult = collection.updateOne(query, Updates.combine(Updates.set("name", "Fresh Breads and Tulips"), Updates.currentDate("lastModified")), new UpdateOptions().upsert(true).bypassDocumentValidation(true));
                
                if (ur.getMatchedCount() == 1)
                {
                    result = true;
                }
                else
                {
                    result = false;
                    throw new Exception("Update not successful");
                }
                
                activityLog = new ActivityLog("User", "Updated(" + newEntity.getClass().getSimpleName() + " with " + this.uuid + ") ",
                                              "Updated author:" + newEntity + " with " + this.uuid + ") ", ActivityLogType.INFO, newEntity.getClass().getSimpleName());
                
                logCollection = getPersistingCollectionFromClass(activityLog);
                
                logCollection.insertOne(activityLog);
            }
            else
            {
                throw new NullPointerException("No record to update");
            }
        }
        catch (Exception me)
        {
            System.out.println("Exception.getMessage() = " + me.getMessage() + "\n Cause: " + me.getCause());
            me.printStackTrace();
            activityLog = new ActivityLog("User", "Not Saved(" + newEntity.getClass().getSimpleName() + " with " + uuid + ") ",
                                          "Error Message" + me.getMessage(), ActivityLogType.ERROR, newEntity.getClass().getSimpleName());
            logCollection = getPersistingCollectionFromClass(activityLog);
            logCollection.insertOne(activityLog);
        }
        
        return result;
    }*/
    
    @Override
    public <T extends PersistingBaseEntity> boolean delete(String collectionName, T t)
    {
        boolean b = false;
        
        if (!CustomNullChecker.nullObjectChecker(t) && !CustomNullChecker.emptyNullStringChecker(t.getUuid()))
        {
            log.info("delete(" + t.getUuid() + ")");
            Bson deleteFilter = Filters.eq("_id", t.getUuid());
            MongoCollection collection = getPersistingCollectionFromClass(t);
            DeleteResult deleteResult = collection.deleteOne(deleteFilter);
            
            long deletedCount = deleteResult.getDeletedCount();
            if (deletedCount == 1)
            {
                b = true;
                activityLog = new ActivityLog("Session User", "Deleted(" + t.getClass()
                                                                            .getSimpleName() + " with " + uuid + ") ", "Deleted(" + t.getClass()
                                                                                                                                     .getSimpleName() + " with " + uuid + ") ", ActivityLogType.INFO, t.getClass().getSimpleName());
                Connection.activityLog.insertOne(activityLog);
            }
            else
            {
                activityLog = new ActivityLog("Session User", "Delete not successful", t.getClass()
                                                                                        .getSimpleName() + " with " + t.getUuid() + ") ", ActivityLogType.ERROR, t.getClass().getSimpleName());
                Connection.activityLog.insertOne(activityLog);
            }
        }
        else
        {
            throw new NullPointerException("attempting to delete a non-existent entity");
        }
        
        return b;
    }
    
    
    @Override
    public <T extends PersistingBaseEntity> boolean deleteMany(List<T> t, String collectionName)
    {
        final boolean[] b = {false};
        if (t.size() > 0)
        {
            t.forEach(d ->
                      {
                          if (!CustomNullChecker.emptyNullStringChecker(d.getUuid()))
                          {
                              log.info("delete(" + d.getUuid() + ")");
                              deleteFilter = Filters.eq("_id", d.getUuid());
                              collection = getPersistingCollectionFromClass(d);
                              deleteResult = collection.deleteOne(deleteFilter);
                              long deletedCount = deleteResult.getDeletedCount();
                              if (deletedCount == 1)
                              {
                                  b[0] = true;
                                  activityLog = new ActivityLog("Session User", "Deleted(" + t.getClass()
                                                                                              .getSimpleName() + " with " + uuid + ") ", "Deleted(" + t.getClass()
                                                                                                                                                       .getSimpleName() + " with " + uuid + ") ", ActivityLogType.INFO, t.getClass().getSimpleName());
                                  Connection.activityLog.insertOne(activityLog);
                              }
                              else
                              {
                                  activityLog = new ActivityLog("Session User", "Delete not successful", t.getClass()
                                                                                                          .getSimpleName() + " with " + d.getUuid() + ") ", ActivityLogType.ERROR, t.getClass().getSimpleName());
                                  Connection.activityLog.insertOne(activityLog);
                              }
                          }
                          else
                          {
                              throw new NullPointerException("attempting to delete a non existent entity");
                          }
                      });
        }
        return b[0];
    }
    
    /*public <T extends PersistingBaseEntity> void ensureIdAndLogDetails(T t)
    {
        if (uuid == null)
            setUuid(getIdPrefix(t) + UUID.randomUUID().toString());

        if (createdDate == null)
        {
            setCreatedBy(getSessionUser());
            setCreatedDate(LocalDateTime.now());
        }
    }*/
}
