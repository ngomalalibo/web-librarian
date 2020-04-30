package com.pc.weblibrarian.entity;

import com.pc.weblibrarian.utils.LoadProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

//@Entity(value = MDB.DB_APPCONFIG, noClassnameStored = true)
@Slf4j
@Data
@EqualsAndHashCode(callSuper = false)
public class AppConfiguration extends PersistingBaseEntity
{
    
    private static final long serialVersionUID = 1L;
    
    // app config
    private String domain;
    private String organizationName;
    private String adminEmail;
    private String dateTimeFormat;
    
    // user configuration
    private boolean requireUserAddress;
    public int minimumPasswordLength;
    public int maximumPasswordAttempts;
    
    // checkout configuration
    
    private boolean copyOrganizationAdminOnAllEmails;
    private boolean allowAdminDeleteCheckOut;
    private boolean notifyAdminAtReminder;
    private boolean notifyUserAtCheckout;
    private boolean emailValidatedBeforeCheckoutAllowed;
    
    public int maxDaysToEditCheckOut;
    public int maxCheckoutItemsPerUser;
    public int maxCheckoutDays;
    private int maxCheckoutExtendedDays;
    private int checkoutReminderDays;
    
    private String contentRating;
    private String currencyTypeAndSymbol;
    private String databaseURI;
    private String databaseURL;
    private String databaseName;
    private int databasePort;
    private String countryCode;
    private String googleUsername;
    private String googlePassword;
    
    
    public static long getSerialversionuid()
    {
        
        return serialVersionUID;
    }
    
    public String toString()
    {
        return "AppConfiguration{" +
                "domain='" + domain + '\'' +
                ", organizationName='" + organizationName + '\'' +
                ", adminEmail='" + adminEmail + '\'' +
                ", dateTimeFormat='" + dateTimeFormat + '\'' +
                ", requireUserAddress=" + requireUserAddress +
                ", minimumPasswordLength=" + minimumPasswordLength +
                ", maximumPasswordAttempts=" + maximumPasswordAttempts +
                ", copyOrganizationAdminOnAllEmails=" + copyOrganizationAdminOnAllEmails +
                ", allowAdminDeleteCheckOut=" + allowAdminDeleteCheckOut +
                ", notifyAdminAtReminder=" + notifyAdminAtReminder +
                ", notifyUserAtCheckout=" + notifyUserAtCheckout +
                ", emailValidatedBeforeCheckoutAllowed=" + emailValidatedBeforeCheckoutAllowed +
                ", maxDaysToEditCheckOut=" + maxDaysToEditCheckOut +
                ", maxCheckoutItemsPerUser=" + maxCheckoutItemsPerUser +
                ", maxCheckoutDays=" + maxCheckoutDays +
                ", maxCheckoutExtendedDays=" + maxCheckoutExtendedDays +
                ", checkoutReminderDays=" + checkoutReminderDays +
                '}';
    }
    
    public void getDefaultsFromEnvVariables()
    {
        
        try
        {
            super.prepersist(this);
            this.setDomain(LoadProperties.getProperty("WEBLIBRARIAN_DOMAIN"));
            this.setOrganizationName(LoadProperties.getProperty("WEBLIBRARIAN_ORGANIZATIONNAME"));
            this.setAdminEmail(LoadProperties.getProperty("WEBLIBRARIAN_ADMINEMAIL"));
            this.setDateTimeFormat(LoadProperties.getProperty("WEBLIBRARIAN_DATETIMEFORMAT"));
            this.setRequireUserAddress(Boolean.parseBoolean(LoadProperties.getProperty("WEBLIBRARIAN_REQUIREUSERADDRESS")));
            this.setMinimumPasswordLength(Integer.parseInt(LoadProperties.getProperty("WEBLIBRARIAN_MINIMUMPASSWORDLENGTH")));
            this.setMaximumPasswordAttempts(Integer.parseInt(LoadProperties.getProperty("WEBLIBRARIAN_MAXIMUMPASSWORDATTEMPTS")));
            this.setCopyOrganizationAdminOnAllEmails(Boolean.parseBoolean(LoadProperties.getProperty("WEBLIBRARIAN_COPYORGANIZATIONADMINONALLEMAILS")));
            this.setAllowAdminDeleteCheckOut(Boolean.parseBoolean(LoadProperties.getProperty("WEBLIBRARIAN_ALLOWADMINDELETECHECKOUT")));
            this.setNotifyAdminAtReminder(Boolean.parseBoolean(LoadProperties.getProperty("WEBLIBRARIAN_NOTIFYADMINATREMINDER")));
            this.setNotifyUserAtCheckout(Boolean.parseBoolean(LoadProperties.getProperty("WEBLIBRARIAN_NOTIFYUSERATCHECKOUT")));
            this.setEmailValidatedBeforeCheckoutAllowed(Boolean.parseBoolean(LoadProperties.getProperty("WEBLIBRARIAN_EMAILVALIDATEBEFORECHECKOUTALLOWED")));
            this.setMaxDaysToEditCheckOut(Integer.parseInt(LoadProperties.getProperty("WEBLIBRARIAN_MAXDAYSTOEDITCHECKOUT")));
            this.setMaxCheckoutItemsPerUser(Integer.parseInt(LoadProperties.getProperty("WEBLIBRARIAN_MAXCHECKOUTITEMSPERUSER")));
            this.setMaxCheckoutDays(Integer.parseInt(LoadProperties.getProperty("WEBLIBRARIAN_MAXCHECKOUTDAYS")));
            this.setMaxCheckoutExtendedDays(Integer.parseInt(LoadProperties.getProperty("WEBLIBRARIAN_MAXCHECKOUTEXTENDEDDAYS")));
            this.setCheckoutReminderDays(Integer.parseInt(LoadProperties.getProperty("WEBLIBRARIAN_CHECKOUTREMINDERDAYS")));
            
            this.setContentRating(LoadProperties.getProperty("WEBLIBRARIAN_CONTENTRATING"));
            this.setCurrencyTypeAndSymbol(LoadProperties.getProperty("WEBLIBRARIAN_CURRENCYTYPEANDSYMBOL"));
            this.setDatabaseName(LoadProperties.getProperty("SPRING_DATA_MONGODB_DATABASE"));
            this.setDatabaseURI(LoadProperties.getProperty("SPRING_DATA_MONGODB_URI"));
            this.setDatabaseURL(LoadProperties.getProperty("DATABASE_URL"));
            this.setDatabasePort(Integer.parseInt(LoadProperties.getProperty("SPRING_DATA_MONGODB_PORT")));
            this.setCountryCode(LoadProperties.getProperty("WEBLIBRARIAN_COUNTRYCODE"));
            this.setGoogleUsername(LoadProperties.getProperty("GOOGLE_MAIL_USERNAME"));
            this.setGooglePassword(LoadProperties.getProperty("GOOGLE_MAIL_PASSWORD"));
        }
        catch (IOException io)
        {
            log.info("Property not found in properties file.. Loading properties from Environment variables");
            
            this.setDomain(System.getenv("WEBLIBRARIAN_DOMAIN"));
            this.setOrganizationName(System.getenv("WEBLIBRARIAN_ORGANIZATIONNAME"));
            this.setAdminEmail(System.getenv("WEBLIBRARIAN_ADMINEMAIL"));
            this.setDateTimeFormat(System.getenv("WEBLIBRARIAN_DATETIMEFORMAT"));
            this.setRequireUserAddress(Boolean.parseBoolean(System.getenv("WEBLIBRARIAN_REQUIREUSERADDRESS")));
            this.setMinimumPasswordLength(Integer.parseInt(System.getenv("WEBLIBRARIAN_MINIMUMPASSWORDLENGTH")));
            this.setMaximumPasswordAttempts(Integer.parseInt(System.getenv("WEBLIBRARIAN_MAXIMUMPASSWORDATTEMPTS")));
            this.setCopyOrganizationAdminOnAllEmails(Boolean.parseBoolean(System.getenv("WEBLIBRARIAN_COPYORGANIZATIONADMINONALLEMAILS")));
            this.setAllowAdminDeleteCheckOut(Boolean.parseBoolean(System.getenv("WEBLIBRARIAN_ALLOWADMINDELETECHECKOUT")));
            this.setNotifyAdminAtReminder(Boolean.parseBoolean(System.getenv("WEBLIBRARIAN_NOTIFYADMINATREMINDER")));
            this.setNotifyUserAtCheckout(Boolean.parseBoolean(System.getenv("WEBLIBRARIAN_NOTIFYUSERATCHECKOUT")));
            this.setEmailValidatedBeforeCheckoutAllowed(Boolean.parseBoolean(System.getenv("WEBLIBRARIAN_EMAILVALIDATEBEFORECHECKOUTALLOWED")));
            this.setMaxDaysToEditCheckOut(Integer.parseInt(System.getenv("WEBLIBRARIAN_MAXDAYSTOEDITCHECKOUT")));
            this.setMaxCheckoutItemsPerUser(Integer.parseInt(System.getenv("WEBLIBRARIAN_MAXCHECKOUTITEMSPERUSER")));
            this.setMaxCheckoutDays(Integer.parseInt(System.getenv("WEBLIBRARIAN_MAXCHECKOUTDAYS")));
            this.setMaxCheckoutExtendedDays(Integer.parseInt(System.getenv("WEBLIBRARIAN_MAXCHECKOUTEXTENDEDDAYS")));
            this.setCheckoutReminderDays(Integer.parseInt(System.getenv("WEBLIBRARIAN_CHECKOUTREMINDERDAYS")));
            
            this.setContentRating(System.getenv("WEBLIBRARIAN_CONTENTRATING"));
            this.setCurrencyTypeAndSymbol(System.getenv("WEBLIBRARIAN_CURRENCYTYPEANDSYMBOL"));
            this.setDatabaseName(System.getenv("SPRING_DATA_MONGODB_DATABASE"));
            this.setDatabaseURI(System.getenv("SPRING_DATA_MONGODB_URI"));
            this.setDatabaseURL(System.getenv("DATABASE_URL"));
            this.setDatabasePort(Integer.parseInt(System.getenv("SPRING_DATA_MONGODB_PORT")));
            this.setCountryCode(System.getenv("WEBLIBRARIAN_COUNTRYCODE"));
            this.setGoogleUsername(System.getenv("GOOGLE_MAIL_USERNAME"));
            this.setGooglePassword(System.getenv("GOOGLE_MAIL_PASSWORD"));
            io.getMessage();
        }
    }
    
}
