/**
 * mycollab-web - Parent pom providing dependency and plugin management for applications
		built with Maven
 * Copyright © ${project.inceptionYear} MyCollab (support@mycollab.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.vaadin.web.ui;

import com.google.common.eventbus.Subscribe;
import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Span;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.common.i18n.ShellI18nEnum;
import com.mycollab.common.ui.components.notification.RequestUploadAvatarNotification;
import com.mycollab.common.ui.components.notification.SmtpSetupNotification;
import com.mycollab.core.AbstractNotification;
import com.mycollab.core.NewUpdateAvailableNotification;
import com.mycollab.vaadin.ApplicationEventListener;
import com.mycollab.vaadin.EventBusFactory;
import com.mycollab.shell.event.ShellEvent;
import com.mycollab.vaadin.AsyncInvoker;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.ui.UIConstants;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import org.infinispan.commons.util.concurrent.ConcurrentHashSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.hene.popupbutton.PopupButton;
import org.vaadin.jouni.restrain.Restrain;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MCssLayout;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import java.util.Set;

/**
 * @author MyCollab Ltd.
 * @since 4.1
 */
public class NotificationComponent extends PopupButton implements PopupButton.PopupVisibilityListener,
        ApplicationEventListener<ShellEvent.NewNotification> {
    private static Logger LOG = LoggerFactory.getLogger(NotificationComponent.class);
    private static final long serialVersionUID = 2908372640829060184L;

    private final Set<AbstractNotification> notificationItems;
    private final VerticalLayout notificationContainer;

    public NotificationComponent() {
        notificationItems = new ConcurrentHashSet<>();
        notificationContainer = new VerticalLayout();
        new Restrain(notificationContainer).setMaxWidth("500px");
        this.setContent(notificationContainer);
        this.setIcon(FontAwesome.BELL);
        this.setStyleName("notification-button");

        addPopupVisibilityListener(this);
        EventBusFactory.getInstance().register(this);

        // Register to receive broadcasts
        JavaScript.getCurrent().addFunction("com.mycollab.scripts.upgrade",
                jsonArray -> {
                    String version = jsonArray.getString(0);
                    String installerFile = jsonArray.getString(1);
                    String manualDownloadLink = jsonArray.getString(2);
                    UI.getCurrent().addWindow(new UpgradeConfirmWindow(version, manualDownloadLink, installerFile));
                });
    }

    @Override
    public void popupVisibilityChange(PopupVisibilityEvent event) {
        notificationContainer.removeAllComponents();

        if (notificationItems.size() > 0) {
            for (AbstractNotification item : notificationItems) {
                Component comp = buildComponentFromNotification(item);
                comp.setStyleName("notification-type");
                comp.addStyleName("notification-type-" + item.getType());
                notificationContainer.addComponent(comp);
            }
        } else {
            Label noItemLbl = new Label(UserUIContext.getMessage(ShellI18nEnum.OPT_NO_NOTIFICATION));
            notificationContainer.addComponent(noItemLbl);
            notificationContainer.setComponentAlignment(noItemLbl, Alignment.MIDDLE_CENTER);
        }
    }

    private void addNotification(AbstractNotification notification) {
        notificationItems.add(notification);
        updateCaption();
        displayTrayNotification(notification);
    }

    public void removeNotification(AbstractNotification notification) {
        notificationItems.remove(notification);
        updateCaption();
    }

    private void updateCaption() {
        if (notificationItems.size() > 0) {
            UI ui = getUI();
            AsyncInvoker.access(ui, new AsyncInvoker.PageCommand() {
                @Override
                public void run() {
                    NotificationComponent.this.setCaption("" + notificationItems.size());
                }
            });
        } else {
            this.setCaption(null);
        }
    }

    @Subscribe
    @Override
    public void handle(ShellEvent.NewNotification event) {
        if (event.getData() instanceof AbstractNotification) {
            addNotification((AbstractNotification) event.getData());
        }
    }

    private void displayTrayNotification(AbstractNotification item) {
        if (item instanceof NewUpdateAvailableNotification) {
            NewUpdateAvailableNotification updateNo = (NewUpdateAvailableNotification) item;
            Notification no;
            if (UserUIContext.isAdmin()) {
                no = new Notification(UserUIContext.getMessage(GenericI18Enum.WINDOW_INFORMATION_TITLE), UserUIContext.getMessage(ShellI18nEnum.OPT_HAVING_NEW_VERSION,
                        ((NewUpdateAvailableNotification) item).getVersion()) + " "
                        + new A("javascript:com.mycollab.scripts.upgrade('" + updateNo.getVersion() + "','" + updateNo.getAutoDownloadLink() + "','" + updateNo.getManualDownloadLink() + "')")
                        .appendText(UserUIContext.getMessage(ShellI18nEnum.ACTION_UPGRADE)),
                        Notification.Type.TRAY_NOTIFICATION);
            } else {
                no = new Notification(UserUIContext.getMessage(GenericI18Enum.WINDOW_INFORMATION_TITLE), UserUIContext.getMessage(ShellI18nEnum.OPT_HAVING_NEW_VERSION,
                        ((NewUpdateAvailableNotification) item).getVersion()), Notification.Type.TRAY_NOTIFICATION);
            }

            no.setHtmlContentAllowed(true);
            no.setDelayMsec(300000);

            UI currentUI = this.getUI();
            AsyncInvoker.access(getUI(), new AsyncInvoker.PageCommand() {
                @Override
                public void run() {
                    no.show(currentUI.getPage());
                }
            });
        }
    }

    private Component buildComponentFromNotification(AbstractNotification item) {
        final MHorizontalLayout wrapper = new MHorizontalLayout();
        wrapper.setData(item);
        wrapper.setDefaultComponentAlignment(Alignment.TOP_LEFT);

        if (item instanceof NewUpdateAvailableNotification) {
            final NewUpdateAvailableNotification notification = (NewUpdateAvailableNotification) item;
            Span spanEl = new Span();
            spanEl.appendText(UserUIContext.getMessage(ShellI18nEnum.OPT_HAVING_NEW_VERSION, notification.getVersion()));
            ELabel lbl = ELabel.html(FontAwesome.INFO_CIRCLE.getHtml() + " " + spanEl.write()).withFullWidth();
            CssLayout lblWrapper = new CssLayout();
            lblWrapper.addComponent(lbl);
            wrapper.addComponent(lblWrapper);
            wrapper.expand(lblWrapper);
            if (UserUIContext.isAdmin()) {
                MButton upgradeBtn = new MButton(UserUIContext.getMessage(ShellI18nEnum.ACTION_UPGRADE), clickEvent -> {
                    UI.getCurrent().addWindow(new UpgradeConfirmWindow(notification.getVersion(), notification.getManualDownloadLink(), notification.getInstallerFile()));
                    NotificationComponent.this.setPopupVisible(false);
                }).withStyleName(UIConstants.BLOCK);
                wrapper.addComponent(upgradeBtn);
            }
        } else if (item instanceof RequestUploadAvatarNotification) {
            wrapper.addComponent(new Label(FontAwesome.EXCLAMATION_TRIANGLE.getHtml() + " " + UserUIContext.getMessage
                    (ShellI18nEnum.OPT_REQUEST_UPLOAD_AVATAR), ContentMode.HTML));
            MButton uploadAvatarBtn = new MButton(UserUIContext.getMessage(ShellI18nEnum.ACTION_UPLOAD_AVATAR), clickEvent -> {
                EventBusFactory.getInstance().post(new ShellEvent.GotoUserAccountModule(this, new String[]{"preview"}));
                NotificationComponent.this.setPopupVisible(false);
            }).withStyleName(UIConstants.BLOCK);
            wrapper.add(uploadAvatarBtn);
        } else if (item instanceof SmtpSetupNotification) {
            MButton smtpBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.ACTION_SETUP), clickEvent -> {
                EventBusFactory.getInstance().post(new ShellEvent.GotoUserAccountModule(this, new String[]{"setup"}));
                NotificationComponent.this.setPopupVisible(false);
            }).withStyleName(UIConstants.BLOCK);
            Label lbl = ELabel.html(FontAwesome.EXCLAMATION_TRIANGLE.getHtml() + " " + UserUIContext.getMessage(ShellI18nEnum.ERROR_NO_SMTP_SETTING));
            MCssLayout lblWrapper = new MCssLayout(lbl);
            wrapper.with(lblWrapper, smtpBtn).expand(lblWrapper);
        } else {
            LOG.error("Do not render notification " + item);
        }
        return wrapper;
    }
}
