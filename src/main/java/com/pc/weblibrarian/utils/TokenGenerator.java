package com.pc.weblibrarian.utils;

import com.pc.weblibrarian.entity.LibraryUser;
import elemental.json.Json;
import elemental.json.JsonObject;
import org.bson.Document;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

public class TokenGenerator
{
    private static final String ALGORITHM = "AES";
    private static byte[] key = new byte[]{'8', '0', 'e', 'h', 'm', 'v', 'd', 'a', 'o', 'i', 'u', 'y', 'r', 't', 'q',
            'w'};
    
    public static Document dec(String r)
    {
        return Document.parse(decrypt(new String(Base64.getUrlDecoder().decode(r))));
    }
    
    public static String enc(Document r)
    {
        return Base64.getUrlEncoder().encodeToString(encrypt(r.toJson()).getBytes());
    }
    
    public static String decString(String r)
    {
        return decrypt(new String(Base64.getUrlDecoder().decode(r)));
    }
    
    public static String encString(String r)
    {
        System.out.println("encString" + r);
        return Base64.getUrlEncoder().encodeToString(encrypt(r).getBytes());
    }
    
    public static String encrypt(String valueToEnc)
    {
        try
        {
            Cipher c = Cipher.getInstance(ALGORITHM);
            c.init(Cipher.ENCRYPT_MODE, generateKey());
            return Base64.getEncoder().encodeToString(c.doFinal(valueToEnc.getBytes()));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
    
    private static Key generateKey() throws Exception
    {
        return new SecretKeySpec(key, ALGORITHM);
    }
    
    public static String decrypt(String encryptedValue)
    {
        try
        {
            Cipher c = Cipher.getInstance(ALGORITHM);
            c.init(Cipher.DECRYPT_MODE, generateKey());
            return new String(c.doFinal(Base64.getDecoder().decode(encryptedValue)));
        }
        catch (Exception e)
        {
            return null;
        }
    }
    
    
    public static String createToken(LibraryUser user)
    {
        JsonObject json = Json.createObject();
        json.put("time", LocalDate.now().format(DateTimeFormatter.ISO_DATE));
        json.put("userEmail", user.getUserEmail());
        json.put("organization", String.valueOf(user.getOrganization()));
        return TokenGenerator.encString(json.toJson());
    }
    
    public static void main(String[] args)
    {
        String in = "hello";
        String out = enc(new Document("x", in));
        Document tt = dec(out);
        System.out.println(">>" + in + " -> " + out + "-> " + tt);
    }
}
