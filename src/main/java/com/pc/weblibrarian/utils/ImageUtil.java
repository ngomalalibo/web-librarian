package com.pc.weblibrarian.utils;

import com.pc.weblibrarian.model.ImageModel;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class ImageUtil
{
    public static String IMAGE_FILE = "frontend\\styles\\images\\dummy2.png";
    public static String FILE_PATH = "frontend\\styles\\images\\";
    
    public static ImageModel convertPathToImageModel(String path) throws IOException
    {
        File file = new File(path);
        BufferedImage bImage = ImageIO.read(file);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(bImage, "png", bos);
        
        return new ImageModel(file.getName(), FILE_PATH, bos.toByteArray());
    }
    
    public static Binary convertImageModelToBinary(ImageModel model) throws IOException
    {
        return new Binary(BsonBinarySubType.BINARY, model.getImageByteArray());
    }
    
    public static void main(String[] args)
    {
        try
        {
            ImageModel mm = ImageUtil.convertPathToImageModel(IMAGE_FILE);
            Binary ff = new Binary(BsonBinarySubType.BINARY, mm.getImageByteArray());
            System.out.println("ff.length() = " + ff.length());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        
    }
}
