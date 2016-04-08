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
package com.esofthead.mycollab.vaadin.web.ui;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.common.ui.components.notification.ChangeDefaultUsernameNotification;
import com.esofthead.mycollab.common.ui.components.notification.RequestUploadAvatarNotification;
import com.esofthead.mycollab.common.ui.components.notification.SmtpSetupNotification;
import com.esofthead.mycollab.common.ui.components.notification.TimezoneNotification;
import com.esofthead.mycollab.configuration.SiteConfiguration;
import com.esofthead.mycollab.core.AbstractNotification;
import com.esofthead.mycollab.core.NewUpdateAvailableNotification;
import com.esofthead.mycollab.core.NotificationBroadcaster;
import com.esofthead.mycollab.eventmanager.ApplicationEventListener;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.shell.events.ShellEvent;
import com.esofthead.mycollab.shell.view.components.UpgradeConfirmWindow;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.ELabel;
import com.google.common.eventbus.Subscribe;
import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Span;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import elemental.json.JsonArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.hene.popupbutton.PopupButton;
import org.vaadin.jouni.restrain.Restrain;
import org.vaadin.viritin.layouts.MCssLayout;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 4.1
 */
public class NotificationComponent extends PopupButton implements PopupButton.PopupVisibilityListener,
        ApplicationEventListener<ShellEvent.NewNotification>, NotificationBroadcaster.BroadcastListener {
    private static Logger LOG = LoggerFactory.getLogger(NotificationComponent.class);
    private static final long serialVersionUID = 2908372640829060184L;

    private final List<AbstractNotification> notificationItems;
    private final VerticalLayout notificationContainer;

    public NotificationComponent() {
        super();
        notificationItems = new ArrayList<>();
        notificationContainer = new VerticalLayout();
        new Restrain(notificationContainer).setMaxWidth("500px");
        this.setContent(notificationContainer);
        this.setIcon(FontAwesome.BELL);
        this.setStyleName("notification-button");

        addPopupVisibilityListener(this);
        EventBusFactory.getInstance().register(this);

        // Register to receive broadcasts
        NotificationBroadcaster.register(this);
        JavaScript.getCurrent().addFunction("com.mycollab.scripts.upgrade",
                new JavaScriptFunction() {
                    @Override
                    public void call(JsonArray arguments) {
                        String version = arguments.getString(0);
                        String manualDownloadLink = arguments.getString(1);
                        String installerFile = arguments.getString(2);
                        UI.getCurrent().addWindow(new UpgradeConfirmWindow(version, manualDownloadLink, installerFile));
                    }
                });
    }

    @Override
    public void detach() {
        NotificationBroadcaster.unregister(this);
        super.detach();
    }

    @Override
    public void popupVisibilityChange(PopupVisibilityEvent event) {
        notificationContainer.removeAllComponents();

        if (notificationItems.size() > 0) {
            for (int i = 0; i < notificationItems.size(); i++) {
                AbstractNotification item = notificationItems.get(i);
                Component comp = buildComponentFromNotification(item);
                comp.setStyleName("notification-type");
                comp.addStyleName("notification-type-" + item.getType());
                notificationContainer.addComponent(comp);
                if (i < notificationItems.size() - 1) {
                    notificationContainer.addComponent(ELabel.hr());
                }
            }
        } else {
            Label noItemLbl = new Label("There is no notification right now");
            notificationContainer.addComponent(noItemLbl);
            notificationContainer.setComponentAlignment(noItemLbl, Alignment.MIDDLE_CENTER);
        }
    }

    public void addNotification(AbstractNotification notification) {
        notificationItems.add(notification);
        updateCaption();
        displayTrayNotification(notification);
    }

    public void removeNotification(AbstractNotification notification) {
        notificationItems.remove(notification);
        updateCaption();
    }

    protected void updateCaption() {
        if (notificationItems.size() > 0) {
            this.setCaption("" + notificationItems.size());
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

    @Override
    public void broadcastNotification(AbstractNotification notification) {
        addNotification(notification);
    }

    private void displayTrayNotification(AbstractNotification item) {
        if (item instanceof NewUpdateAvailableNotification) {
            NewUpdateAvailableNotification updateNo = (NewUpdateAvailableNotification) item;
            Notification no;
            if (AppContext.isAdmin()) {
                no = new Notification(AppContext.getMessage(GenericI18Enum.WINDOW_INFORMATION_TITLE), "There" +
                        " is the new MyCollab version " + ((NewUpdateAvailableNotification) item).getVersion() + " "
                        + new A("javascript:com.mycollab.scripts.upgrade('" + updateNo.getVersion() + "','" + updateNo.getAutoDownloadLink() + "','" + updateNo.getManualDownloadLink() + "')")
                        .appendText("Upgrade"),
                        Notification.Type.TRAY_NOTIFICATION);
            } else {
                no = new Notification(AppContext.getMessage(GenericI18Enum.WINDOW_INFORMATION_TITLE), "There" +
                        " is the new MyCollab version " + ((NewUpdateAvailableNotification) item).getVersion(), Notification.Type.TRAY_NOTIFICATION);
            }

            no.setHtmlContentAllowed(true);
            no.setDelayMsec(300000);

            UI currentUI = UI.getCurrent();
            if (currentUI != null) {
                if (SiteConfiguration.getPullMethod() == SiteConfiguration.PullMethod.push) {
                    no.show(currentUI.getPage());
                    currentUI.push();
                } else {
                    try {
                        UI.getCurrent().setPollInterval(1000);
                        no.show(currentUI.getPage());
                    } finally {
                        UI.getCurrent().setPollInterval(-1);
                    }
                }
            }
        }
    }

    private Component buildComponentFromNotification(AbstractNotification item) {
        final MHorizontalLayout wrapper = new MHorizontalLayout();
        wrapper.setData(item);
        wrapper.setDefaultComponentAlignment(Alignment.TOP_LEFT);

        if (item instanceof ChangeDefaultUsernameNotification) {
            MCssLayout cssWrapper = new MCssLayout(new Label(FontAwesome.EXCLAMATION_TRIANGLE.getHtml() + " You are " +
                    "using the default email " +
                    "'admin@mycollab.com'. You can not receive the site notifications without using your right email",
                    ContentMode.HTML));

            Button actionBtn = new Button("Change it", new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent event) {
                    EventBusFactory.getInstance().post(new ShellEvent.GotoUserAccountModule(this, new String[]{"preview"}));
                    NotificationComponent.this.setPopupVisible(false);
                }
            });
            actionBtn.setStyleName(UIConstants.BUTTON_BLOCK);
            wrapper.with(cssWrapper, actionBtn).expand(cssWrapper);
        } else if (item instanceof NewUpdateAvailableNotification) {
            final NewUpdateAvailableNotification notification = (NewUpdateAvailableNotification) item;
            Span spanEl = new Span();
            spanEl.appendText("There is the new MyCollab version " + notification.getVersion() + " . For the " +
                    "enhancements and security purpose, the system administrator should upgrade to the latest version");
            Label lbl = new Label(FontAwesome.INFO_CIRCLE.getHtml() + " " + spanEl.write(), ContentMode.HTML);
            lbl.setWidth("100%");
            CssLayout lblWrapper = new CssLayout();
            lblWrapper.addComponent(lbl);
            wrapper.addComponent(lblWrapper);
            wrapper.expand(lblWrapper);
            if (AppContext.isAdmin()) {
                Button upgradeBtn = new Button("Upgrade", new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        UI.getCurrent().addWindow(new UpgradeConfirmWindow(notification.getVersion(), notification.getManualDownloadLink(), notification.getInstallerFile()));
                        NotificationComponent.this.setPopupVisible(false);
                    }
                });
                upgradeBtn.addStyleName(UIConstants.BUTTON_BLOCK);
                wrapper.addComponent(upgradeBtn);
            }
        } else if (item instanceof RequestUploadAvatarNotification) {
            wrapper.addComponent(new Label(FontAwesome.EXCLAMATION_TRIANGLE.getHtml() + " Let people recognize you", ContentMode.HTML));
            Button uploadAvatarBtn = new Button("Upload your avatar", new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent event) {
                    EventBusFactory.getInstance().post(new ShellEvent.GotoUserAccountModule(this, new String[]{"preview"}));
                    NotificationComponent.this.setPopupVisible(false);
                }
            });
            uploadAvatarBtn.setStyleName(UIConstants.BUTTON_BLOCK);
            wrapper.add(uploadAvatarBtn);
        } else if (item instanceof SmtpSetupNotification) {
            Button smtpBtn = new Button("Setup", new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent clickEvent) {
                    EventBusFactory.getInstance().post(new ShellEvent.GotoUserAccountModule(this, new String[]{"setup"}));
                    NotificationComponent.this.setPopupVisible(false);
                }
            });
            smtpBtn.setStyleName(UIConstants.BUTTON_BLOCK);
            Label lbl = new Label(FontAwesome.EXCLAMATION_TRIANGLE.getHtml() + " Your members can not receive any mail " +
                    "notification without a proper SMTP setting", ContentMode.HTML);
            MCssLayout lblWrapper = new MCssLayout(lbl);
            wrapper.with(lblWrapper, smtpBtn).expand(lblWrapper);
        } else if (item instanceof TimezoneNotification) {
            wrapper.addComponent(new Label(FontAwesome.EXCLAMATION_TRIANGLE.getHtml() + " The correct your timezone will help you get " +
                    "the event right", ContentMode.HTML));
            Button actionBtn = new Button("Action", new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent event) {
                    EventBusFactory.getInstance().post(new ShellEvent.GotoUserAccountModule(this, new String[]{"preview"}));
                    NotificationComponent.this.setPopupVisible(false);
                }
            });
            actionBtn.setStyleName(UIConstants.BUTTON_BLOCK);
            wrapper.addComponent(actionBtn);
        } else {
            LOG.error("Do not render notification " + item);
        }
        return wrapper;
    }
}
