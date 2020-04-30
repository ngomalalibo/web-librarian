package com.pc.weblibrarian.model;

import lombok.Data;

@Data
public class ImageModel
{
    private String fileName;
    private String filePath;
    private byte[] imageByteArray = new byte[0];
    
    public ImageModel(String fileName, String filePath, byte[] imageByteArray)
    {
        this.fileName = fileName;
        this.filePath = filePath;
        this.imageByteArray = imageByteArray;
    }
    
    public ImageModel()
    {
        super();
    }
}
