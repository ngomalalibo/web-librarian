package com.pc.weblibrarian.customcomponents;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;

public class CustomNotification extends Notification
{
    public CustomNotification(String message, String className, boolean opened, int duration, Position position)
    {
        /*Div message = new Div(new Span(bean.getClass().getSimpleName() + " saved successfully"));
                                      message.setClassName("success");
                                      new CustomNotification(message).open();*/
        super(new Span(message));
        
        Div div = new Div();
        div.addClassName(className);
        
        setDuration(duration);
        setOpened(opened);
        setPosition(position);
        
        
        /*
         * The code below register the style file dynamically. Normally you
         * use @StyleSheet annotation for the component class. This way is
         * chosen just to show the style file source code.
         */
        /*String styles = ".my-style { "
                + "  color: red;"
                + " }";
        StreamRegistration resource = UI.getCurrent().getSession()
                                        .getResourceRegistry()
                                        .registerResource(new StreamResource("frontend\\styles\\css\\publicpage.css", () ->
                                        {
                                            byte[] bytes = styles.getBytes(StandardCharsets.UTF_8);
                                            return new ByteArrayInputStream(bytes);
                                        }));
        UI.getCurrent().getPage().addStyleSheet(
                "base://" + resource.getResourceUri().toString());*/
    }
    
    public CustomNotification()
    {
        super();
    }
}
