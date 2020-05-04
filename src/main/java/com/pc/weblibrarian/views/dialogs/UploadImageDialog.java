package com.pc.weblibrarian.views.dialogs;

import com.pc.weblibrarian.customcomponents.CustomNotification;
import com.pc.weblibrarian.customcomponents.CustomUpload;
import com.pc.weblibrarian.customcomponents.SmallButton;
import com.pc.weblibrarian.model.ImageModel;
import com.pc.weblibrarian.utils.ImageUtil;
import com.pc.weblibrarian.views.BasicDialog;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import org.bson.types.Binary;

import java.io.*;

public class UploadImageDialog extends BasicDialog
{
    public UploadImageDialog(String beanId, UploadAction action, boolean noalpha /*return Image to caller*/)
    {
        super(BeanAction.VIEW, DialogSize.SMALL);
        setTitle("Upload Image");
        
        CustomUpload upload = new CustomUpload();
        
        
        SmallButton uploadbtn = new SmallButton("Upload File").theme(ButtonVariant.LUMO_SMALL.getVariantName());
        upload.setUploadButton(uploadbtn);
        
        /*upload.addFinishedListener(e ->
                                   {
                                       fileStream.set(memoryBuffer.get().getInputStream());
                                   });*/
        upload.addSucceededListener(succeededEvent ->
                                    {
                                        try (InputStream fileContent = upload.getMemoryBuffer().getInputStream())
                                        {
                                            String name = succeededEvent.getFileName();
                                            File file = new File(ImageUtil.FILE_PATH + name);
                                            OutputStream outputBuffer = new FileOutputStream(file);
                                            // byte[] data = fileContent.readAllBytes();
                                            byte[] data = new byte[fileContent.available()];
                                            outputBuffer.write(data);
                                            ImageModel imageModel = new ImageModel(name, file.getPath(), data);
                
                                            Binary action1 = action.action(imageModel);
                                            close();
                                        }
                                        catch (Exception e)
                                        {
                                            new Notification("Error saving file: " + e.getMessage(), 2000, Position.MIDDLE).open();
                                        }
                                    });
        
        upload.addFailedListener(lst ->
                                 {
                                     try
                                     {
                                         action.action(ImageUtil.convertPathToImageModel(ImageUtil.IMAGE_FILE));
                                     }
                                     catch (IOException ex)
                                     {
                                         System.out.println("ex.getMessage() = " + ex.getMessage());
                                     }
                                     new Notification("Error uploading file: " + lst.getReason().getMessage(), 2000, Position.MIDDLE).open();
            
                                 });
        upload.addFileRejectedListener(event ->
                                       {
                                           try
                                           {
                                               action.action(ImageUtil.convertPathToImageModel(ImageUtil.IMAGE_FILE));
                                           }
                                           catch (IOException ex)
                                           {
                                               System.out.println("ex.getMessage() = " + ex.getMessage());
                                           }
                                           new Notification("Error uploading file: " + event.getErrorMessage(), 2000, Position.MIDDLE).open();
                                           new CustomNotification(event.getErrorMessage(), "btn-outline-danger", true, 1000, Position.TOP_CENTER).open();
                                       });
        
        setContent(upload);
        
    }
    
    public interface UploadAction
    {
        Binary action(ImageModel bean);
    }
    
}
