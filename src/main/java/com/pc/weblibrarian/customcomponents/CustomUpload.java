package com.pc.weblibrarian.customcomponents;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;

public class CustomUpload extends Upload
{
    MemoryBuffer memoryBuffer;
    
    public MemoryBuffer getMemoryBuffer()
    {
        return memoryBuffer;
    }
    
    public CustomUpload()
    {
        super();
        memoryBuffer = new MemoryBuffer();
        setReceiver(memoryBuffer);
        setDropLabel(new Span("Upload image."));
        setAutoUpload(true);
        setDropAllowed(true);
        setMaxFileSize(1024 * 1024 * 4);
        setMaxFiles(1);
        setAcceptedFileTypes("image/*");
    }
}
