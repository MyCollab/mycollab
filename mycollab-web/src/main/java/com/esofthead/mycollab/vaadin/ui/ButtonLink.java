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
import com.vaadin.server.Page;
import org.apache.commons.lang3.StringUtils;
import org.vaadin.maddon.button.MButton;

/**
 * @author MyCollab Ltd.
 * @since 5.0.5
 */
public class ButtonLink extends MButton {
    public ButtonLink(final String caption, final String href) {
        this.addStyleName("link");
        this.setCaptionAsHtml(true);
        A captionLink = new A(href).appendText(caption);
        this.withCaption(captionLink.write());
    }

    public ButtonLink withEnabled(boolean enabled) {
        this.setEnabled(enabled);
        return this;
    }
}
