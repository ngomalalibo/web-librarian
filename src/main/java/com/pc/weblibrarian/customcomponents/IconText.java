package com.pc.weblibrarian.customcomponents;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;

public class IconText extends Div
{
    public IconText(String icon, String text)
    {
        Span span = new Span(text);
        span.setClassName("hideonsmall");
        add(new SVGIcon(icon), span);
        addClassName("icontext");
    }
}
