package com.pc.weblibrarian.dataService;

import com.github.javafaker.Faker;
import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;
import com.pc.weblibrarian.entity.*;
import com.pc.weblibrarian.enums.*;
import com.pc.weblibrarian.model.Country;
import com.pc.weblibrarian.model.State;
import com.pc.weblibrarian.utils.DateToLocalDateConverter;
import com.pc.weblibrarian.utils.ImageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class DataInitialization<T extends PersistingBaseEntity> //implements CommandLineRunner
{
    static Logger logger = LoggerFactory.getLogger(PersistingBaseEntity.class);
    
    //private PersistingBaseEntity pbe;
    private FakeValuesService fakeValuesService;
    
    private static AppConfiguration appConfiguration;
    
    private String isbn_10;
    private String isbn_13;
    
    private Author author;
//    private Publication pub;
//    private Media media;
//    private LibraryItem<T> libraryItem;
//    private LibraryUser libraryUser;
//    private Organization organization;
//    private WaitingList waitingList;
//
//    private Article article;
//    private Comment comment;
//    private Note note;
//    private Order order;
//    private Quote quote;
    
    private Random random;
    
    private Integer rating1to5 = 0;
    private Integer random1to6 = 0;
    private Integer random5to30 = 0;
    private Integer random15to30 = 0;
    private Integer random25to50 = 0;
    private Integer random30to5000 = 0;
    private Double price = 0D;
    
    private String description;
    
    private List<Author> authors;
    List<String> addresses = new LinkedList<>();
    private List<String> authorIds;
//    private List<LibraryItem<T>> libraryItems;
//    private List<Publication> publications;
//    private List<Media> medias;
//    private List<LibraryUser> libraryUsers;
//    private List<Organization> organizations;
//    private List<WaitingList> waitingLists;
    
    private Faker faker;
    private DateTimeFormatter dtf;
    private LocalDate disContinueDate;
    private LocalDate DOB;
    private LocalDate publishedDate;
    
    private PricingType pt;
    private AddressType addressType;
    private LibraryItemType libraryItemType;
    private PersonGenderType genderType;
    private PersonTitleType titleType;
    private CurrencyTypeAndSymbol currencyTypeAndSymbol;
    
    private Person person = new Person();
    //    private Publisher publisher;
    private Address address = new Address();
//    private ShippingInformation shippingInformation;
    
    private StringBuilder sb;
    
    public String allConstants;
    
    public String fakerConstants()
    {
//        pbe = new PersistingBaseEntity();
        sb = new StringBuilder();
        random = new Random();
        fakeValuesService = new FakeValuesService(
                new Locale("en-GB"), new RandomService());
//        String email = fakeValuesService.bothify("????##@gmail.com");
//        Matcher emailMatcher = Pattern.compile("\\w{4}\\d{2}@gmail.com").matcher(email);
        
        /*Our code creates an isbn string. Our pattern checks the generated string against the regex.*/
        isbn_10 = fakeValuesService.regexify("^(?:ISBN(?:-10)?:? )?(?=[0-9X]{10}$|(?=(?:[0-9]+[- ]){3})[- 0-9X]{13}$)[0-9]{1,5}[- ]?[0-9]+[- ]?[0-9]+[- ]?[0-9X]$");
//        Matcher isbn_10Matcher = Pattern.compile("/\\d{9}(?:\\d|X)/").matcher(isbn_10);
        
        //String isbn_13 = fakeValuesService.regexify("^(?:ISBN(?:-13)?:? )?(?=[0-9]{13}$|(?=(?:[0-9]+[- ]){4})[- 0-9]{17}$)97[89][- ]?[0-9]{1,5}[- ]?[0-9]+[- ]?[0-9]+[- ]?[0-9]$");
        
        faker = new Faker(Locale.getDefault());
        // faker = new Faker(new Locale("en-GB"));
        
        dtf = DateTimeFormatter.ISO_LOCAL_DATE;
        
        
        rating1to5 = faker.number()
                          .numberBetween(1, 5);
        random1to6 = faker.number()
                          .numberBetween(1, 6);
        random5to30 = faker.number()
                           .numberBetween(5, 30);
        random15to30 = faker.number()
                            .numberBetween(15, 30);
        random25to50 = faker.number()
                            .numberBetween(25, 50);
        random30to5000 = faker.number()
                              .numberBetween(30, 5000);
        price = faker.number()
                     .randomDouble(2, 500, 50000);
        
        disContinueDate = DateToLocalDateConverter.dateToLocalDateConverter(faker.date()
                                                                                 .between(new Date(2019, 01, 01), new Date(2050, 01, 01)));
//        sb.append("disContinueDate: " + disContinueDate + "\n");
        DOB = DateToLocalDateConverter.dateToLocalDateConverter(faker.date()
                                                                     .between(new Date(1900, 01, 01), new Date(2010, 01, 01)));
//        sb.append("DOB: " + DOB + "\n");
        publishedDate = DateToLocalDateConverter.dateToLocalDateConverter(faker.date()
                                                                               .between(new Date(0000, 01, 01), new Date(2019, 01, 01)));
//        sb.append("publishedDate: " + publishedDate + "\n");
        //System.out.println("LibraryItemType.values()[1]" + LibraryItemType.values()[1]);
        
        
        
        /*publisher = new Publisher(
                faker.book()
                     .publisher(),
                faker.internet()
                     .url(),
                faker.chuckNorris()
                     .fact(),
                faker.internet()
                     .url(),
                faker.internet()
                     .url(),
                pbe.generateFakeId(new Publisher()));*/

//        sb.append("publisher: " + publisher + "\n");
        /*shippingInformation = new ShippingInformation(faker.number()
                                                           .randomDouble(2, 1, 1000), WeightUnit.values()[random.nextInt(WeightUnit.values().length)], faker.number()
                                                                                                                                                            .numberBetween(1, 50) + " x " + faker.number()
                                                                                                                                                                                                 .numberBetween(1, 50));*/

//        sb.append("shippingInformation: " + shippingInformation + "\n");


//        sb.append("price: " + price + "\n");
        description = faker.lorem()
                           .paragraph(20);
//        sb.append("description: " + description + "\n");
        
        return sb.toString();
    }
    
    
    public DataInitialization()
    {
//        String dbname1 = con.DBNAME;
//        String dbname = Connection.DBNAME;
        //pbe = new PersistingBaseEntity();
        
        authors = new ArrayList<>();
        authorIds = new ArrayList<>();
//        libraryItems = new ArrayList<>();
//        publications = new ArrayList<>();
//        medias = new ArrayList<>();
//        libraryUsers = new ArrayList<>();
//        organizations = new ArrayList<>();
//        waitingLists = new ArrayList<>();
        
        int libraryItemTypeSize = LibraryItemType.values().length;
        int pricingTypeSize = PricingType.values().length;
        int addressTypeSize = AddressType.values().length;
        int personGenderTypeSize = PersonGenderType.values().length;
        int personTitleTypeSize = PersonTitleType.values().length;
        int currencyTypeSize = CurrencyTypeAndSymbol.values().length;
        
        /*System.out.println("libraryItemTypeSize = " + libraryItemTypeSize);
        System.out.println("pricingTypeSize = " + pricingTypeSize);
        System.out.println("addressTypeSize = " + addressTypeSize);
        System.out.println("personGenderTypeSize = " + personGenderTypeSize);
        System.out.println("personTitleTypeSize = " + personTitleTypeSize);
        System.out.println("currencyTypeSize = " + currencyTypeSize);*/
        
        libraryItemType = LibraryItemType.values()[new Random(libraryItemTypeSize).nextInt(libraryItemTypeSize)];
        pt = PricingType.values()[new Random(pricingTypeSize).nextInt(pricingTypeSize)];
        addressType = AddressType.values()[new Random(addressTypeSize).nextInt(addressTypeSize)];
        genderType = PersonGenderType.values()[new Random(personGenderTypeSize).nextInt(personGenderTypeSize)];
        titleType = PersonTitleType.values()[new Random(personTitleTypeSize).nextInt(personTitleTypeSize)];
        currencyTypeAndSymbol = CurrencyTypeAndSymbol.values()[new Random(currencyTypeSize).nextInt(currencyTypeSize)];
        
        /*
        libraryItemType = LibraryItemType.values()[random.nextInt(LibraryItemType.values().length)];
        libraryItemType = LibraryItemType.values()[new Random(LibraryItemType.values().length)];
        pt = PricingType.values()[new Random(PricingType.values().length).nextInt()];
        addressType = AddressType.values()[new Random(AddressType.values().length).nextInt()];
        genderType = PersonGenderType.values()[new Random(PersonGenderType.values().length).nextInt()];
        titleType = PersonTitleType.values()[new Random(PersonTitleType.values().length).nextInt()];
        currencyTypeAndSymbol = CurrencyTypeAndSymbol.values()[new Random(CurrencyTypeAndSymbol.values().length).nextInt()];*/
        
        System.out.println("******Initialization Output*******\n\n" + fakerConstants());
        
    }
    
    
    public String initData()
    {
        //new DataInitialization();
        
        int i = 0;
        
        
        /** TODO> Increase to 20 after test
         *
         * */
        //reloadDefaults();
        while (i < 1)
        {
//            initalizeAddress();
//            initializePerson();
//            initAuthors();
//            initLibraryItems();
//            initLibraryUser();
//            initOrganization();
//            initWaitingList();
//            initArticles();
//            initNotes();
//            initQuotes();
//            initOrders();
            
            i++;
        }
        
        
        /*User user1 = new User("ngomalalibo@gmail.com", "user1", "user1Pass", "admin");
        
        users.add(user1);
        this.userRepository.save(user1);
        
        User user2 = new User("ngomalalibo@gmail.com", "user2", "user2Pass", "customer");
        
        users.add(user2);
        this.userRepository.save(user2);
        
        users = Arrays.asList(user1, user2);*/
        
        return "Completed";
    }
    
    public Person initializePerson()
    {
        try
        {
            person = new Person(
                    titleType,
                    genderType,
                    faker.name().firstName(),
                    faker.name().lastName(),
                    faker.funnyName().name(),
                    faker.internet().emailAddress(),
                    faker.phoneNumber().cellPhone(),
                    faker.internet().url(),
                    ImageUtil.convertPathToImageModel(ImageUtil.IMAGE_FILE)
                    ,
                    DOB,
                    addresses);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        //person.save(person);
        sb.append("person: " + person + "\n");
        return person;
    }
    
    public List<String> initalizeAddress()
    {
        int f = 0;
        while (f <= random1to6)
        {
            address = new Address(faker.address()
                                       .city(), new State(faker.address()
                                                               .state(), "us"/*GetCountriesAndStates.getCountries().get(random15to30 % 20).getCode()*/), faker.address()
                                                                                                                                                              .zipCode(), new Country(faker.address().country(), "us"), faker.bool()
                                                                                                                                                                                                                             .bool(), faker.address()
                                                                                                                                                                                                                                           .streetAddress(), addressType);
            System.out.println("address: " + address);
            address.save(address);
            
            addresses.add(address.getUuid());
            
            f++;
            sb.append("address " + f + ": " + address + "\n");
        }
        return addresses;
    }
    
    public Author initAuthors()
    {
        int d = 0;
        //change to random1to6 after test
        while (d < 2)
        {
            
            author = new Author(faker.internet().url(), faker.internet().url(), person.getUuid());
//            author.setUuid(author.generateFakeId(author));
            
            authors.add(author);
            d++;
        }
        System.out.println("***Initializing Authors***");
        
        for (Author author : authors)
        {
            author.save(author);
        }
        
        System.out.println("***Authors Initialized***");
        
        return author;
    }
    
    /*private void initOrders()
    {
        System.out.println("***Initializing Orders***");
        order = new Order();
        double totalAmount = 0;
        order.setUserId(person.getUuid());
//         already added via OrderItem.libraryItemType
//         order.setLibraryItems(libraryItems.stream().map((item) -> item.getUuid()).collect(Collectors.toList()));
        
        List<OrderItem> orderItemList = new ArrayList<>();
        
        while (counter <= random1to6)
        {
            OrderItem orderItem = new OrderItem();
            orderItem.setLibraryItem(libraryItem.getUuid());
            orderItem.setQuantity(random1to6);
            orderItem.setOrderSerialNumber(counter);
            orderItem.setPrice(price);
            orderItemList.add(orderItem);
            
            totalAmount += price;
            
            counter++;
        }
        order.setOrderItem(orderItemList);
        order.setPricingType(pt);
        order.setRemark(faker.chuckNorris()
                             .fact());
        order.setDiscount(0);
        order.setTotalAmount(totalAmount);
        
        order.save(order);
        
        System.out.println("***Orders Initialized***");
    }*/
    
    /*private void initQuotes()
    {
        System.out.println("***Initializing Quotes***");
        quote = new Quote(author.getUuid(), faker.hitchhikersGuideToTheGalaxy()
                                                 .quote(), commentList);
        quote.save(quote);
        
        System.out.println("***Quotes Initialized***");
    }*/
    
    /*private void initNotes()
    {
        System.out.println("***Initializing Notes***");
        note = new Note(faker.shakespeare()
                             .asYouLikeItQuote());
        note.save(note);
        
        System.out.println("***Notes Initialized***");
    }*/
    
    private int counter = 0;
    private List<Comment> commentList;
    
    /*private void initArticles()
    {
        System.out.println("***Initializing Articles***");
        article = new Article();
        article.setAuthorId(pbe.generateFakeId(article));
        
        commentList = new ArrayList<>();
        while (counter <= random1to6)
        {
            comment = new Comment();
            comment.setCommenterId(pbe.generateFakeId(new Person()));
            comment.setComment(faker.chuckNorris()
                                    .fact());
            commentList.add(comment);
            
            counter++;
        }
        
        article.setComments(commentList);
        article.setRating(rating1to5);
        article.save(article);
        
        System.out.println("***Aticles Initialized***");
    }*/
    
    /*private void initWaitingList()
    {
        System.out.println("***Initializing Initializing the Waiting List***");
        waitingList = new WaitingList();
        waitingList.setEntityId(faker.company()
                                     .buzzword());
        waitingList.setFulfilledDate(LocalDate.now());
        waitingList.setNoticeSentDate(LocalDate.now());
        waitingList.setUserId(faker.name()
                                   .username());
        waitingList.setUuid(pbe.generateFakeId(new WaitingList()));
        
        waitingList.save(waitingList);
        
        waitingLists.add(waitingList);
        
        System.out.println("***Waiting List Initialized***");
    }*/
    
    /*private void initOrganization()
    {
        System.out.println("***Initializing Organizations***");
        organization = new Organization();
        
        organization.setTheme(faker.company()
                                   .logo());
        organization.setOrganizationWebsite(faker.company()
                                                 .url());
        organization.setOrganizationName(faker.company()
                                              .name());
        organization.setOrganizationBaseCurrency(currencyTypeAndSymbol);
        organization.setOrganization(faker.funnyName()
                                          .toString()
                                          .toUpperCase());
        organization.setOrganizationWebsite(faker.company()
                                                 .url());
        organization.setContactPerson(person.getFullName(""));
        organization.setUuid(pbe.generateFakeId(new Organization()));
        
        organization.save(organization);
        
        organizations.add(organization);
        
        System.out.println("***Organization Initialized***");
    }*/
    
    /*private void initLibraryUser()
    {
        System.out.println("***Initializing Library Users***");
        libraryUser = new LibraryUser();
        libraryUser.setPersonId(pbe.generateFakeId(new Person()));
        libraryUser.setAccessToConsumable(faker.bool()
                                               .bool());
        libraryUser.setAccessToRentable(faker.bool()
                                             .bool());
        libraryUser.setAccountBanned(faker.bool()
                                          .bool());
        libraryUser.setAccountLocked(faker.bool()
                                          .bool());
        
        libraryUser.setGender(genderType);
        libraryUser.setPassword(faker.internet()
                                     .password());
        libraryUser.setLastLoginDateTime(faker.date()
                                              .past(2, TimeUnit.DAYS)
                                              .toInstant()
                                              .atZone(ZoneId.systemDefault())
                                              .toLocalDateTime());
        libraryUser.setLoginAttempts(faker.number()
                                          .numberBetween(1, 4));
        libraryUser.setMaximumCheckoutItems(faker.number()
                                                 .numberBetween(2, 5));
        libraryUser.setPersonRoleType(PersonRoleType.values()[random.nextInt(PersonRoleType.values().length)]);
        
        libraryUser.setTitle(titleType);
        libraryUser.setUserId(faker.name()
                                   .username());
        libraryUser.setUuid(pbe.generateFakeId(new LibraryUser()));
        
        libraryUser.save(libraryUser);
        
        libraryUsers.add(libraryUser);
        
        System.out.println("***Library User Initialized***");
    }*/
    
    /*private void initLibraryItems()
    {
        System.out.println("***Initializing Library Items***");
        Map<String, PersistingBaseEntity> libraryItemMap = new HashMap<>();
        //PersistingBaseEntity d = libraryItemType.getLibraryItemClass(LibraryItemType.values()[random.nextInt(LibraryItemType.values().length)]);
        String lit = LibraryItemType.values()[random.nextInt(LibraryItemType.values().length)].getLibraryItemType();
        libraryItemMap.put(lit, LibraryItemType.getLibraryItemClass(LibraryItemType.values()[random.nextInt(LibraryItemType.values().length)]));
        libraryItem = new LibraryItem<>();
        libraryItem.setLibraryItemId(pbe.generateFakeId(new LibraryItem()));
        libraryItem.setMaximumCheckoutCopies(random5to30);
        libraryItem.setDiscontinueDate(disContinueDate);
        libraryItem.setLibraryItemType(libraryItemMap);
        libraryItem.setDiscontinueDate(disContinueDate);
        libraryItem.setShippingInformation(shippingInformation);
        libraryItem.setLibraryItemLocation(new LibraryItemLocation(random5to30.toString(), random15to30.toString(), random25to50.toString()));
        
        libraryItem.setLibraryItemType(libraryItemMap);
        */
    
    /**
     * Note not needed in library Item
     * * List<Note> notes = new ArrayList<>();
     * <p>
     * notes.add(new Note(description));
     *//*
        
        //Connection.notes.insertMany(notes);
        
        List<Pricing> pricings = new ArrayList<>();
        pricings.add(new Pricing(Double.valueOf(faker.commerce()
                                                     .price(20, 40)), faker.currency()
                                                                           .name(), pt));
        
        libraryItem.setPricingInformation(pricings);

//            LibraryItem is either a Publication or Media
//            if (libraryItemType.getLibraryItemType().containsKey(LibraryItemType.PUBLICATION))
        if (libraryItem.getLibraryItemType().containsKey(LibraryItemType.PUBLICATION))
        {
            pub = new Publication();
            
            Connection.authors.find().limit(2).iterator().forEachRemaining(a -> authorIds.add(a.getPersonId()));
            
            pub.setAuthorIds(authorIds);
            pub.setBindingType(BindingType.values()[random.nextInt(BindingType.values().length)]);
            pub.setDescription(description);
            pub.setDiscontinueDate(disContinueDate);
            pub.setEdition(String.valueOf(faker.number()
                                               .numberBetween(1, 10)));
            pub.setFirstPublishedDate(publishedDate);
            pub.setGenre(faker.book()
                              .genre()); // Get genre Later
            pub.setImages(faker.internet()
                               .image()
                               .getBytes());
            pub.setISBN_10(isbn_10);
            pub.setLanguage(faker.address()
                                 .country()); //get language later
            pub.setNumberOfPages(random30to5000);
            pub.setPublicationType(PublicationType.values()[random.nextInt(PublicationType.values().length)]);
            pub.setUuid(faker.internet()
                             .uuid());
            pub.setPublisherId(publisher.getUuid());
            pub.setReleaseCycle(ReleaseCycle.values()[random.nextInt(ReleaseCycle.values().length)]);
            pub.setReleaseYear(String.valueOf(publishedDate.getYear()));
            pub.setReviewText(faker.chuckNorris()
                                   .fact());
            pub.setTitle(faker.book()
                              .title());
            
            libraryItemMap.put(lit, pub);
            
            libraryItem.setLibraryItemType(libraryItemMap);
        }
        else
        {
            Connection.authors.find().limit(1).iterator().forEachRemaining(a -> authorIds.add(a.getPersonId()));
            media = new Media();
            media.setUuid(pbe.generateFakeId(new Media()));
            media.setASIN(faker.lorem()
                               .words(4)
                               .toString());
            media.setMediaRating(MediaRating.values()[random.nextInt(MediaRating.values().length)]);
            media.setAuthorIds(authorIds);
            media.setCopyright(faker.commerce()
                                    .productName());
            media.setLabel(faker.company()
                                .catchPhrase()); //company() is a useful faker
            media.setMediaType(MediaType.values()[random.nextInt(MediaType.values().length)]);
            media.setMetaData(new MetaData(faker.zelda()
                                                .character(), faker.number()
                                                                   .digit(), faker.book()
                                                                                  .genre(),
                                           faker.gameOfThrones()
                                                .character(), faker.book()
                                                                   .author(), faker.ancient()
                                                                                   .hero(), faker.gameOfThrones()
                                                                                                 .character(), MediaAspectRatio.values()[random.nextInt(MediaAspectRatio.values().length)], faker.number()
                                                                                                                                                                                                 .numberBetween(5, 120), (float) faker.number()
                                                                                                                                                                                                                                      .randomDouble(2, 300, 5000)));
            media.setPublisherId(publisher.getUuid());
            media.setReleaseDate(publishedDate);
            media.setVideoRating(VideoRating.values()[random.nextInt(VideoRating.values().length)]);
            
            libraryItemMap.put(lit, media);
            
            libraryItem.setLibraryItemType(libraryItemMap);
            
        }
        
        libraryItem.setQuantityUpdateDate(LocalDateTime.now());
        
        libraryItem.setShippingInformation(shippingInformation);
        libraryItem.setQuantityAvailable(faker.number()
                                              .numberBetween(2, 500));
        libraryItem.setUuid(pbe.generateFakeId(new LibraryItem()));
        libraryItem.save(libraryItem);
        
        libraryItems.add(libraryItem);
        
        System.out.println("***Library Item Initialized***");
    }*/
    public static AppConfiguration getDefaults()
    {
        return loadDefaults();
    }
    
    public static AppConfiguration loadDefaults()
    {
        appConfiguration = new AppConfiguration();
        // appConfiguration.save(appConfiguration);
        // logger.info("---------------------------- Application Defaults Loaded");
        
        return appConfiguration;
        
        // Bson query = Filters.eq(appConfiguration.getUuid());
        // return Connection.appConfig.find(query).iterator().tryNext();
    }
    
    /*@Override
    public void run(String... args) throws Exception
    {
        System.out.println("***Command Line Runner Initiated***");
    }*/
}
