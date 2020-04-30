package com.pc.weblibrarian.views.viewsutil;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.internal.StateNode;

public class RequiredValidationUtil
{
    RequiredValidationUtil()
    {
        // utility class should not be instantiated
    }
    
    public static void attachConnector(Component component)
    {
        execJS(component, "window.Vaadin.Flow.textConnector = {\n"
                + "        disableClientValidation: function (textComponent){\n"
                + "            if ( typeof textComponent.$checkValidity == 'undefined'){\n"
                + "                textComponent.$checkValidity = textComponent.checkValidity;\n"
                + "                textComponent.checkValidity = function() { return true; };\n"
                + "            }\n  "
                + "            if ( typeof textComponent.$validate == 'undefined'){\n"
                + "                textComponent.$validate = textComponent.validate;\n"
                + "                textComponent.validate = function() { return true; };\n"
                + "            }\n  },\n"
                + "        enableClientValidation: function (textComponent){\n"
                + "            if ( textComponent.$checkValidity ){\n"
                + "                textComponent.checkValidity = textComponent.$checkValidity;\n"
                + "                delete textComponent.$checkValidity;\n"
                + "            }\n  "
                + "            if ( textComponent.$validate ){\n"
                + "                textComponent.validate = textComponent.$validate;\n"
                + "                delete textComponent.$validate;\n"
                + "            }\n  }\n }");
    }
    
    public static void updateClientValidation(boolean requiredIndicatorVisible,
                                              Component component)
    {
        if (requiredIndicatorVisible)
        {
            disableClientValidation(component);
        }
        else
        {
            enableClientValidation(component);
        }
    }
    
    static void disableClientValidation(Component component)
    {
        execJS(component,
               "window.Vaadin.Flow.textConnector.disableClientValidation($0);");
    }
    
    static void enableClientValidation(Component component)
    {
        execJS(component,
               "window.Vaadin.Flow.textConnector.enableClientValidation($0);");
    }
    
    private static void execJS(Component component, String js)
    {
        StateNode node = component.getElement().getNode();
        
        node.runWhenAttached(ui -> ui.getInternals().getStateTree()
                                     .beforeClientResponse(node, context -> ui.getPage()
                                                                              .executeJavaScript(js, component.getElement())));
        
    }
}
