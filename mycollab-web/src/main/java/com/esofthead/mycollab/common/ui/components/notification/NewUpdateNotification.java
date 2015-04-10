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
package com.esofthead.mycollab.common.ui.components.notification;

import com.esofthead.mycollab.common.ui.components.AbstractNotification;
import com.esofthead.mycollab.module.user.AccountLinkGenerator;
import com.esofthead.mycollab.vaadin.AppContext;
import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Span;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;

import java.util.Properties;

/**
 * @author MyCollab Ltd.
 * @since 5.0.0
 */
public class NewUpdateNotification extends AbstractNotification {
    private Properties props;

    public NewUpdateNotification(Properties props) {
        super(AbstractNotification.WARNING);

        this.props = props;
    }

    @Override
    public Component renderContent() {
        Span spanEl = new Span();
        spanEl.appendText("There is the new MyCollab version " + props.getProperty("version") + " . For the " +
                "enhancements and security purpose, you should ask the system administrator upgrade to the latest version at ");

        A link = new A(props.getProperty("downloadLink"), "_blank");
        link.appendText("here");
        spanEl.appendChild(link);
        return new Label(FontAwesome.EXCLAMATION.getHtml() + " " + spanEl.write(), ContentMode.HTML);
    }
}
