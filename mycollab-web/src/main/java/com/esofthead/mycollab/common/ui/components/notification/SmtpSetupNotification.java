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
import com.esofthead.mycollab.shell.view.SmtpConfigurationWindow;
import com.vaadin.ui.*;
import org.vaadin.maddon.layouts.MHorizontalLayout;

/**
 * @author MyCollab Ltd
 * @since 5.0.4
 */
public class SmtpSetupNotification  extends AbstractNotification {

    public SmtpSetupNotification() {
        super(AbstractNotification.WARNING);
    }

    @Override
    public Component renderContent() {
        MHorizontalLayout layout = new MHorizontalLayout();
        layout.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        Button smtpBtn = new Button("Setup", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                UI.getCurrent().addWindow(new SmtpConfigurationWindow());
            }
        });
        smtpBtn.setStyleName("link");
        layout.with(new Label("You did not set up a SMTP account yet."), smtpBtn);
        return layout;
    }
}
