package com.mycollab.vaadin.web.ui;

import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Div;
import com.hp.gagawa.java.elements.Img;
import com.hp.gagawa.java.elements.Text;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;

/**
 * @author MyCollab Ltd.
 * @since 4.0
 */
public class LabelLink extends Label {
    private static final long serialVersionUID = 1L;

    private Div div;
    private A link;

    public LabelLink(String title, String href) {
        super("", ContentMode.HTML);
        createContent(title, href);
        this.setValue(div.write());
    }

    private void createContent(String title, String href) {
        div = new Div();
        link = new A();
        if (href != null) {
            link.setHref(href);
        }

        if (title != null) {
            setTitle(title);
        }
        div.appendChild(link);
    }

    public void setTitle(String title) {
        link.removeChildren();
        link.appendChild(new Text(title));
    }

    public void setIconLink(Object source) {
        if (source instanceof FontAwesome) {
            Text txt = new Text(((FontAwesome) source).getHtml() + " ");
            div.appendChild(0, txt);
            this.setValue(div.write());
        } else {
            Img img = new Img("", source.toString());
            div.appendChild(0, img);
            this.setValue(div.write());
        }
    }

    public void setLink(String href) {
        div.getChild(1).setAttribute("href", href);
        this.setValue(div.write());
    }
}
