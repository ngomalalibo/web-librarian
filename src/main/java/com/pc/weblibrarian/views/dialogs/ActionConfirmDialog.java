package com.pc.weblibrarian.views.dialogs;

import com.pc.weblibrarian.customcomponents.SmallButton;
import com.pc.weblibrarian.views.BasicDialog;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;

public class ActionConfirmDialog extends BasicDialog
{
    
    public ActionConfirmDialog(OnAction onAction, String header, String body, BeanAction action, DialogSize dialogSize)
    {
        super(action, dialogSize);
        setTitle(header + "?");
        
        Span bodys = new Span(body);
        bodys.getStyle().set("wordWrap", "break-word");
        
        Div form = new Div(bodys);
        form.getStyle().set("paddingTop", "1em");
        setContent(form);
        otherstuff(onAction, action == BeanAction.DELETE);
    }
    
    public ActionConfirmDialog(OnAction onAction, String header, Component body, BeanAction action, DialogSize dialogSize)
    {
        super(action, dialogSize);
        setTitle(header);
        Div form = new Div(body);
        form.getStyle().set("paddingTop", "1em").set("paddingBottom", "1em");
        setContent(form);
        otherstuff(onAction, action == BeanAction.DELETE);
    }
    
    public ActionConfirmDialog(OnAction onAction, Component header, Component body, BeanAction action, DialogSize dialogSize)
    {
        super(action, dialogSize);
        setTitle(header);
        Div form = new Div(body);
        form.getStyle().set("paddingTop", "1em").set("paddingBottom", "1em");
        setContent(form);
        otherstuff(onAction, action == BeanAction.DELETE);
    }
    
    private void otherstuff(OnAction onaction, boolean deleteaction)
    {
        SmallButton submit = new SmallButton("Confirm").theme(deleteaction ? "error primary" : "primary");
        submit.addClickListener(e ->
                                {
                                    onaction.action();
                                    close();
                                });
        addTerminalComponent(submit);
//		addFormComponent(submit);
    }
    
    public interface OnAction
    {
        void action();
    }
    
}