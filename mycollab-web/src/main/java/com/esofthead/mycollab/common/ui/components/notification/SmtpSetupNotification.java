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
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.shell.events.ShellEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
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
                EventBusFactory.getInstance().post(
                        new ShellEvent.GotoUserAccountModule(this,
                                new String[]{"setup"}));
            }
        });
        smtpBtn.setStyleName("link");
        layout.with(new Label(FontAwesome.EXCLAMATION.getHtml() + " Your members can not receive any mail notification without a proper SMTP setting", ContentMode.HTML), smtpBtn);
        return layout;
    }
}
