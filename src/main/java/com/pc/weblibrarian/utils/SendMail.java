package com.pc.weblibrarian.utils;

import com.pc.weblibrarian.dataService.DataInitialization;
import com.pc.weblibrarian.entity.AppConfiguration;
import com.pc.weblibrarian.templates.ActionableEmail;
import org.springframework.beans.factory.annotation.Value;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

public class SendMail
{
    private static String template;
    
    @Value("${google.mail.username}")
    static String username;
    @Value("${google.mail.password}")
    static String password;
    
    public static String host = "smtp.gmail.com";
    public static String port = "465";
    
    static AppConfiguration config = DataInitialization.loadDefaults();
    
    public SendMail()
    {
        File file = new File(Objects.requireNonNull(getClass().getClassLoader().getResource("templates/actionableemail.html")).getFile());
        
        try
        {
            template = new String(Files.readAllBytes(file.toPath()));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    
    public ActionableEmail getMailInstance()
    {
        ActionableEmail mailObject = new ActionableEmail();
        mailObject.setSubject("Web Librarian - Confirm Email Address");
        mailObject.setToAddresses("ngomalalibo@yahoo.com");
        mailObject.setFromAddresses(username != null ? username : "weblibrarianapp@gmail.com");
        mailObject.setLine1(
                "You have been added as a user on Weblibrarian. Please confirm your email address by clicking the link below.");
        mailObject.setLine2(
                "We may need to send you critical information about our service and it is important that we have an accurate email address.");
        mailObject.setLine3("Welcome to Web Librarian");
        mailObject.setButtonText("Confirm email address");
        mailObject.setButtonLink("http://www.javainuse.com");
        mailObject.setMessage(getTemplate(mailObject));
        
        return mailObject;
    }
    
    
    public static boolean sendMailSSL(ActionableEmail actionableEmail)
    {
        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");
        
        Session session = Session.getInstance(properties, new javax.mail.Authenticator()
        {
            
            protected PasswordAuthentication getPasswordAuthentication()
            {
                return new PasswordAuthentication(username != null ? username : "weblibrarianapp@gmail.com", password != null ? password : "Web1234567890");
            }
            
        });
        
        // Used to debug SMTP issues
        session.setDebug(true);
        
        try
        {
            MimeMessage message = new MimeMessage(session);
            
            message.setFrom(new InternetAddress(username != null ? username : "weblibrarianapp@gmail.com"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(String.join(",", List.of(username != null ? username : "weblibrarianapp@gmail.com", actionableEmail.getToAddresses()))));
            message.setSubject(actionableEmail.getSubject());
            message.setSentDate(new Date());
            message.setContent(actionableEmail.getMessage(), "text/html");
            
            Transport.send(message);
            
            System.out.println("Login Successful....");
        }
        catch (MessagingException mex)
        {
            mex.printStackTrace();
        }
        return true;
    }
    
    
    public String getTemplate(ActionableEmail actionableEmail)
    {
        
        return template//
                       .replaceAll("##LINE1##", CustomNullChecker.stringSafe(actionableEmail.getLine1()))//
                       .replaceAll("##LINE2##", CustomNullChecker.stringSafe(actionableEmail.getLine2()))//
                       .replaceAll("##LINE3##", CustomNullChecker.stringSafe(actionableEmail.getLine3()))//
                       .replaceAll("##companyname##", CustomNullChecker.stringSafe(config.getOrganizationName()))//
                       .replaceAll("##messagetitle##", CustomNullChecker.stringSafe(actionableEmail.getSubject()))//
                       .replaceAll("##personname##", CustomNullChecker.stringSafe(actionableEmail.getPersonName()))//
                       .replaceAll("##BUTTONLINK##", CustomNullChecker.stringSafe(actionableEmail.getButtonLink()))//
                       .replaceAll("##BUTTONTEXT##", CustomNullChecker.stringSafe(actionableEmail.getButtonText()))//
                       .replaceAll("##emailaddress##", CustomNullChecker.stringSafe(actionableEmail.getToAddresses()));
    }
    
    public static void main(String[] args)
    {
        SendMail sendMail = new SendMail();
        boolean b = sendMailSSL(sendMail.getMailInstance());
        System.out.println("Response " + b);
    }
}
