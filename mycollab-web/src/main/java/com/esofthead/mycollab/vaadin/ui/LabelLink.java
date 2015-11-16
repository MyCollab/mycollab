/**
 * This file is part of mycollab-web.
 *
 * mycollab-web is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-web is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-web.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.vaadin.ui;

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
        this.setStyleName(UIConstants.BUTTON_LINK);

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
