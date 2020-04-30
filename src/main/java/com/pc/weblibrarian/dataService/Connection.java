package com.pc.weblibrarian.dataService;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.*;
import com.pc.weblibrarian.codecs.ActivityLogTypeCodec;
import com.pc.weblibrarian.codecs.AddressTypeCodec;
import com.pc.weblibrarian.entity.*;
import com.pc.weblibrarian.enums.IDPrefixes;
import com.pc.weblibrarian.model.*;
import org.bson.Document;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.glassfish.jersey.internal.guava.Iterators;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.HashMap;
import java.util.HashSet;

@Repository
public class Connection<T extends PersistingBaseEntity> implements ServletContextListener
{
    
    private static Logger logger = LoggerFactory.getLogger(Connection.class);
    
    public static final String DBNAME = "weblibrarian";
    public static final String DB_ORGANIZATION = "organization";
    public static final String DB_PUBLICATION = "publications";
    public static final String DB_MEDIA = "media";
    public static final String DB_LIBRARYITEM = "libraryitems";
    public static final String DB_APPCONFIG = "appconfig";
    
    public static final String DB_PERSON = "persons";
    public static final String DB_ADDRESS = "address";
    public static final String DB_AUTHOR = "authors";
    public static final String DB_PUBLISHER = "publishers";
    public static final String DB_FILES = "files";
    public static final String DB_IMAGES = "images";
    public static final String DB_USER = "users";
    public static final String DB_USERVERIFICATION = "userverifications";
    public static final String DB_ACTIVITYLOG = "activitylogs";
    public static final String DB_LIBRARYITEMINVNTRY = "mediaiteminventory";
    public static final String DB_CHECKINCHECKOUT = "checkincheckout";
    public static final String DB_WAITLIST = "waitlist";
    public static final String DB_QUOTES = "quotes";
    public static final String DB_ARTICLES = "articles";
    public static final String DB_NOTES = "notes";
    public static final String DB_COMMENTS = "comments";
    public static final String DB_ORDERS = "orders";
    public static final String DB_LIBRARYITEMLOCATION = "libraryItemLocation";
    public static final String DB_LOGIN = "login";
    public static final String DB_METADATA = "metaData";
    public static final String DB_ORDERITEM = "orderItem";
    public static final String DB_PRICING = "pricing";
    public static final String DB_SESSION = "session";
    public static final String DB_SHIPPINGINFORMATION = "shippingInformation";
    public static final String DOCUMENT = "mongoDocument";
    
    // @Value("${spring.data.mongodb.uri}")
    // public static String DBSTR = System.getenv("SPRING_DATA_MONGODB_URI_LOCAL");
    public static String DBSTR = System.getenv("SPRING_DATA_MONGODB_URI");
//    static final String DBSTR = "mongodb://localhost:27017?retryWrites=true&maxPoolSize=50&connectTimeoutMS=2000&w=1&wtimeoutMS=2500";
//    static final String DBSTR = "mongodb://localhost:27017";
    
    public static final int LIMIT = 20;
    public static final int SKIP = 0;
    public static final int SORT = -1;
    
    public static MongoClient mongo = null;
    public static MongoDatabase db;
    
    public static MongoCollection<Person> persons; //DB_PERSON
    public static MongoCollection<Address> addresses; //DB_ADDRESS
    public static MongoCollection<Author> authors; // DB_AUTHOR
    public static MongoCollection<CheckInCheckOut> checkInOut;
    public static MongoCollection<LibraryItem> libraryItem; //DB_LIBRARYITEMINVNTRY
    public static MongoCollection<LibraryUser> libraryUser;
    public static MongoCollection<Media> media; // DB_MEDIA
    public static MongoCollection<Organization> organization; //DB_ORGANIZATION
    public static MongoCollection<Publication> publication; //DB_PUBLICATION
    public static MongoCollection<Publisher> publisher; // DB_PUBLISHER
    public static MongoCollection<UserVerification> userVerification; //DB_USER
    public static MongoCollection<WaitingList> waitingList; //DB_WAITLIST
    public static MongoCollection<ActivityLog> activityLog; //DB_ACTIVITYLOG
    public static MongoCollection<AppConfiguration> appConfig; //DB_APPCONFIG
    public static MongoCollection<OrderTransaction> orders; //DB_ORDERS
    public static MongoCollection<Article> articles; //DB_ARTICLES
    public static MongoCollection<Note> notes; //DB_NOTES
    public static MongoCollection<Comment> comments; // DB_COMMENTS
    public static MongoCollection<CheckInCheckOut> checkInCheckOut;//DB_CHECKINCHECKOUT
    public static MongoCollection<Quotation> quotes;//DB_QUOTES
    public static MongoCollection<LibraryItemLocation> libItemLocation;//DB_QUOTES
    public static MongoCollection<Login> login;//DB_QUOTES
    public static MongoCollection<MetaData> metaData;//DB_QUOTES
    public static MongoCollection<OrderItem> orderItem;//DB_QUOTES
    public static MongoCollection<Pricing> pricing;//DB_QUOTES
    public static MongoCollection<Session> session;//DB_QUOTES
    public static MongoCollection<ShippingInformation> shippingInformation;//DB_QUOTES
    public static MongoCollection<Document> document;//DB_QUOTES
    
    public static CodecRegistry getCodecRegistry()
    {
        final CodecRegistry defaultCodecRegistry = MongoClientSettings.getDefaultCodecRegistry();
        final CodecProvider pojoCodecProvider = PojoCodecProvider.builder()
                                                                 .register("com.pc.weblibrarian.entity", "com.pc.weblibrarian.model", "com.pc.weblibrarian.enums", "com.pc.weblibrarian.temporary.command").automatic(true).build();
        final CodecRegistry cvePojoCodecRegistry = CodecRegistries.fromProviders(pojoCodecProvider);
        final CodecRegistry customEnumCodecs = CodecRegistries.fromCodecs(new AddressTypeCodec(), new ActivityLogTypeCodec());
        return CodecRegistries.fromRegistries(defaultCodecRegistry, customEnumCodecs, cvePojoCodecRegistry);
    }
    
    
    public static MongoDatabase startDB()
    {
        logger.warn("---------------------------- Starting Database");
        ConnectionString connectionString = new ConnectionString(DBSTR);
        
        
        MongoClientSettings settings = MongoClientSettings.builder()
                                                          .applyConnectionString(connectionString)
                                                          .retryWrites(true)
                                                          .codecRegistry(getCodecRegistry())
                                                          .build();
        CodecRegistry pojoCodecRegistry = getCodecRegistry();
        
        if (db == null)
        {
            //mongo = MongoClients.create(DBSTR);
            mongo = MongoClients.create(settings);
            db = mongo.getDatabase(DBNAME);
            //db.withCodecRegistry(settings.getCodecRegistry());
        }
        
        
        
        /*CodecRegistry pojoCodecRegistry =
                fromRegistries(
                        MongoClientSettings.getDefaultCodecRegistry(),
                        fromProviders(PojoCodecProvider.builder()
                                                       .automatic(true)
                                                       .build()));*/
        
        persons = db.getCollection(DB_PERSON, Person.class).withCodecRegistry(pojoCodecRegistry);
        addresses = db.getCollection(DB_ADDRESS, Address.class).withCodecRegistry(pojoCodecRegistry);
        authors = db.getCollection(DB_AUTHOR, Author.class).withCodecRegistry(pojoCodecRegistry);
        checkInOut = db.getCollection(DB_CHECKINCHECKOUT, CheckInCheckOut.class).withCodecRegistry(pojoCodecRegistry);
        libraryItem = db.getCollection(DB_LIBRARYITEM, LibraryItem.class).withCodecRegistry(pojoCodecRegistry);
        libraryUser = db.getCollection(DB_USER, LibraryUser.class).withCodecRegistry(pojoCodecRegistry);
        media = db.getCollection(DB_MEDIA, Media.class).withCodecRegistry(pojoCodecRegistry);
        organization = db.getCollection(DB_ORGANIZATION, Organization.class).withCodecRegistry(pojoCodecRegistry);
        publication = db.getCollection(DB_PUBLICATION, Publication.class).withCodecRegistry(pojoCodecRegistry);
        publisher = db.getCollection(DB_PUBLISHER, Publisher.class).withCodecRegistry(pojoCodecRegistry);
        userVerification = db.getCollection(DB_USERVERIFICATION, UserVerification.class).withCodecRegistry(pojoCodecRegistry);
        waitingList = db.getCollection(DB_WAITLIST, WaitingList.class).withCodecRegistry(pojoCodecRegistry);
        activityLog = db.getCollection(DB_ACTIVITYLOG, ActivityLog.class).withCodecRegistry(pojoCodecRegistry);
        appConfig = db.getCollection(DB_APPCONFIG, AppConfiguration.class).withCodecRegistry(pojoCodecRegistry);
        comments = db.getCollection(DB_COMMENTS, Comment.class).withCodecRegistry(pojoCodecRegistry);
        articles = db.getCollection(DB_ARTICLES, Article.class).withCodecRegistry(pojoCodecRegistry);
        notes = db.getCollection(DB_NOTES, Note.class).withCodecRegistry(pojoCodecRegistry);
        orders = db.getCollection(DB_ORDERS, OrderTransaction.class).withCodecRegistry(pojoCodecRegistry);
        checkInCheckOut = db.getCollection(DB_CHECKINCHECKOUT, CheckInCheckOut.class).withCodecRegistry(pojoCodecRegistry);
        quotes = db.getCollection(DB_QUOTES, Quotation.class).withCodecRegistry(pojoCodecRegistry);
        libItemLocation = db.getCollection(DB_LIBRARYITEMLOCATION, LibraryItemLocation.class).withCodecRegistry(pojoCodecRegistry);
        login = db.getCollection(DB_LOGIN, Login.class).withCodecRegistry(pojoCodecRegistry);
        metaData = db.getCollection(DB_METADATA, MetaData.class).withCodecRegistry(pojoCodecRegistry);
        orderItem = db.getCollection(DB_ORDERITEM, OrderItem.class).withCodecRegistry(pojoCodecRegistry);
        pricing = db.getCollection(DB_PRICING, Pricing.class).withCodecRegistry(pojoCodecRegistry);
        session = db.getCollection(DB_SESSION, Session.class).withCodecRegistry(pojoCodecRegistry);
        shippingInformation = db.getCollection(DB_SHIPPINGINFORMATION, ShippingInformation.class).withCodecRegistry(pojoCodecRegistry);
        document = db.getCollection(DOCUMENT, Document.class).withCodecRegistry(pojoCodecRegistry);
        
        return db;
    }
    
    private static DataInitialization dataInit;
    
    
    public static HashMap<String, MongoCollection<?>> mapCollectionsAndEntities()
    {
        return new HashMap<String, MongoCollection<?>>()
        {{
            put("ActivityLog", Connection.activityLog);
            put("Address", Connection.addresses);
            put("Author", Connection.authors);
            put("AppConfiguration", Connection.appConfig);
            put("Article", Connection.articles);
            put("Publisher", Connection.publisher);
            put("CheckInCheckOut", Connection.checkInOut);
            put("Comments", Connection.comments);
            put("LibraryItem", Connection.libraryItem);
            put("LibraryUser", Connection.libraryUser);
            put("Media", Connection.media);
            put("Note", Connection.notes);
            put("Order", Connection.orders);
            put("Organization", Connection.organization);
            put("Person", Connection.persons);
            put("Publication", Connection.publication);
            put("Quotation", Connection.quotes);
            put("UserVerification", Connection.userVerification);
            put("WaitingList", Connection.waitingList);
            put("OrderItem", Connection.orderItem);
        }};
    }
    
    public static HashMap<String, ? extends PersistingBaseEntity> mapObjectAndEntityNames()
    {
        return new HashMap<String, PersistingBaseEntity>()
        {{
            put("ActivityLog", new ActivityLog());
            put("Address", new Address());
            put("Author", new Author());
            put("AppConfiguration", new AppConfiguration());
            put("Article", new Article());
            put("Publisher", new Publisher());
            put("CheckInCheckOut", new CheckInCheckOut());
            put("Comment", new Comment());
            put("LibraryItem", new LibraryItem());
            put("LibraryUser", new LibraryUser());
            put("Media", new Media());
            put("Note", new Note());
            put("Order", new OrderTransaction());
            put("Organization", new Organization());
            put("Person", new Person());
            put("Publication", new Publication());
            put("Quotation", new Quotation());
            put("UserVerification", new UserVerification());
            put("WaitingList", new WaitingList());
            put("OrderItem", new OrderItem());
        }};
    }
    
    public static HashMap<String, Class<?>> mapObjectAndClazzNames()
    {
        return new HashMap<>()
        {{
            put("ActivityLog", ActivityLog.class);
            put("Address", Address.class);
            put("Author", Author.class);
            put("AppConfiguration", AppConfiguration.class);
            put("Article", Article.class);
            put("Publisher", Publisher.class);
            put("CheckInCheckOut", CheckInCheckOut.class);
            put("Comment", Comment.class);
            put("LibraryItem", LibraryItem.class);
            put("LibraryUser", LibraryUser.class);
            put("Media", Media.class);
            put("Note", Note.class);
            put("Order", OrderTransaction.class);
            put("Organization", Organization.class);
            put("Person", Person.class);
            put("Publication", Publication.class);
            put("Quotation", Quotation.class);
            put("UserVerification", UserVerification.class);
            put("WaitingList", WaitingList.class);
            put("OrderItem", OrderItem.class);
        }};
    }
    
    
    public static HashMap<IDPrefixes, MongoCollection<?>> mapCollectionsAndIDPrefixes()
    {
        return new HashMap<IDPrefixes, MongoCollection<?>>()
        {{
            put(IDPrefixes.Author, Connection.authors);
            put(IDPrefixes.Publisher, Connection.publisher);
            put(IDPrefixes.ActivityLog, Connection.activityLog);
            put(IDPrefixes.AppConfiguration, Connection.appConfig);
            put(IDPrefixes.Person, Connection.persons);
            put(IDPrefixes.Quotes, Connection.quotes);
            put(IDPrefixes.Orders, Connection.orders);
            put(IDPrefixes.OrderItem, Connection.orderItem);
            put(IDPrefixes.Article, Connection.articles);
            put(IDPrefixes.Note, Connection.notes);
            put(IDPrefixes.Comment, Connection.comments);
            put(IDPrefixes.CheckInCheckOut, Connection.checkInOut);
            put(IDPrefixes.LibraryItem, Connection.libraryItem);
            put(IDPrefixes.LibraryUser, Connection.libraryUser);
            put(IDPrefixes.Media, Connection.media);
            put(IDPrefixes.Organization, Connection.organization);
            put(IDPrefixes.Publication, Connection.publication);
            put(IDPrefixes.UserVerification, Connection.userVerification);
            put(IDPrefixes.WaitingList, Connection.waitingList);
            put(IDPrefixes.Address, Connection.addresses);
        }};
    }
    
    public static String initializeDatabase()
    {
        dataInit = new DataInitialization();
        //System.out.println("Triggered onApplicationEvent loading application Data");
        //System.out.println("Loaded Application Bootstrap Data");
        return dataInit.initData();
    }
    
    public static void stopDB()
    {
        logger.warn("---------------------------- Stopping Database");
        if (mongo != null)
        {
            mongo.close();
        }
        mongo = null;
        db = null;
    }
    
    @Override
    public void contextInitialized(ServletContextEvent s)
    {
        
        startDB();
        
        /**TODO> uncomment 4 lines below to createCollections, initialize database, load defaults and get database statistics
         * createAllCollections();
         * initializeDatabase();
         * DataInitialization.reloadDefaults();
         * getDBStats();
         * */
    }
    
    @Override
    public void contextDestroyed(ServletContextEvent s)
    {
        stopDB();
    }
    
    public static MongoDatabase getDBConnections()
    {
        if (db == null || mongo == null)
        {
            mongo = MongoClients.create(DBSTR);
            db = mongo.getDatabase(DBNAME);
        }
        return db;
    }
    
    public static MongoDatabase getDS()
    {
        return db;
    }
    
    
    public Document getDBStats()
    {
        MongoDatabase ds = getDBConnections();
        Document stats = ds.runCommand(new Document("dbstats", 1024));
        System.out.println("DBStats: " + stats.toJson());
        
        return stats;
    }
    
    public static int createAllCollections()
    {
        logger.warn("---------------------------- Creating Collections");
        MongoDatabase db = getDBConnections();
//        MongoIterable<String> colls = dataService.listCollectionNames();
        MongoIterable<String> colls = db.listCollectionNames();
        
        HashSet<String> cols = new HashSet<>();
        for (String j : colls)
        {
            cols.add(j);
        }
        createCollection(cols, DB_AUTHOR);
        createCollection(cols, DB_PERSON);
        createCollection(cols, DB_ADDRESS);
        createCollection(cols, DB_ACTIVITYLOG);
        createCollection(cols, DB_ARTICLES);
        createCollection(cols, DB_COMMENTS);
        /*createCollection(cols, DB_PUBLISHER);
        createCollection(cols, DB_FILES);
        createCollection(cols, DB_IMAGES);
        createCollection(cols, DB_USER);
        createCollection(cols, DB_ORGANIZATION);
        createCollection(cols, DB_PUBLICATION);
        createCollection(cols, DB_CHECKINCHECKOUT);
        createCollection(cols, DB_LIBRARYITEMINVNTRY);
        createCollection(cols, DB_MEDIA);
        createCollection(cols, DB_APPCONFIG);
        createCollection(cols, DB_USERVERIFICATION);
        createCollection(cols, DB_WAITLIST);
        createCollection(cols, DB_QUOTES);
        createCollection(cols, DB_NOTES);
        createCollection(cols, DB_ORDERS);*/
        
       /* if (!cols.contains(DB_ADDRESS))
        {
            db.createCollection(DB_ADDRESS);
        }*/
        logger.warn("---------------------------- Collections Created");
        return Iterators.size(db.listCollections().iterator());
    }
    
    public static void createCollection(HashSet<String> hash, String collection)
    {
        if (!hash.contains(collection))
        {
            db.createCollection(collection);
        }
    }
    
}
