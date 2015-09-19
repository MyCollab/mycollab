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

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.common.ui.components.notification.*;
import com.esofthead.mycollab.core.AbstractNotification;
import com.esofthead.mycollab.core.NewUpdateAvailableNotification;
import com.esofthead.mycollab.core.NotificationBroadcaster;
import com.esofthead.mycollab.eventmanager.ApplicationEventListener;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.user.domain.SimpleUser;
import com.esofthead.mycollab.module.user.service.UserService;
import com.esofthead.mycollab.shell.events.ShellEvent;
import com.esofthead.mycollab.shell.view.components.AdRequestWindow;
import com.esofthead.mycollab.shell.view.components.UpgradeConfirmWindow;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.google.common.eventbus.Subscribe;
import com.hp.gagawa.java.elements.Span;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.hene.popupbutton.PopupButton;
import org.vaadin.jouni.restrain.Restrain;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 4.1
 */
public class NotificationButton extends PopupButton implements PopupButton.PopupVisibilityListener,
        ApplicationEventListener<ShellEvent.NewNotification>, NotificationBroadcaster.BroadcastListener {
    private static Logger LOG = LoggerFactory.getLogger(NotificationButton.class);
    private static final long serialVersionUID = 2908372640829060184L;

    private final List<AbstractNotification> notificationItems;
    private final VerticalLayout notificationContainer;

    public NotificationButton() {
        super();
        notificationItems = new ArrayList<>();
        notificationContainer = new VerticalLayout();
        new Restrain(notificationContainer).setMaxWidth("500px");
        notificationContainer.setWidth("500px");
        this.setContent(notificationContainer);
        this.setIcon(FontAwesome.BELL);
        this.setStyleName("notification-button");

        addPopupVisibilityListener(this);
        EventBusFactory.getInstance().register(this);

        // Register to receive broadcasts
        NotificationBroadcaster.register(this);
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
                    notificationContainer.addComponent(new Hr());
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
            Notification no = new Notification(AppContext.getMessage(GenericI18Enum.WINDOW_INFORMATION_TITLE), "There" +
                    " is the new MyCollab version " + ((NewUpdateAvailableNotification) item).getVersion(),
                    Notification.Type.TRAY_NOTIFICATION);
            no.setHtmlContentAllowed(true);
            no.setDelayMsec(3000);

            UI currentUI = getUI();
            if (currentUI != null) {
                no.show(currentUI.getPage());
                currentUI.push();
            }
        }
    }

    private Component buildComponentFromNotification(AbstractNotification item) {
        final MHorizontalLayout wrapper = new MHorizontalLayout();
        wrapper.setData(item);
        wrapper.setDefaultComponentAlignment(Alignment.TOP_LEFT);

        if (item instanceof ChangeDefaultUsernameNotification) {
            wrapper.addComponent(new Label(FontAwesome.EXCLAMATION_TRIANGLE.getHtml() + " You are using the default username " +
                    "'admin@mycollab.com'. You can not receive the site notifications without using your right email",
                    ContentMode.HTML));
            Button actionBtn = new Button("Change it", new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent event) {
                    EventBusFactory.getInstance().post(new ShellEvent.GotoUserAccountModule(this, new String[]{"preview"}));
                    NotificationButton.this.setPopupVisible(false);
                }
            });
            actionBtn.setStyleName(UIConstants.THEME_LINK);
            actionBtn.addStyleName("block");
            wrapper.addComponent(actionBtn);
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
                        NotificationButton.this.setPopupVisible(false);
                    }
                });
                upgradeBtn.setStyleName(UIConstants.THEME_LINK);
                upgradeBtn.addStyleName("block");
                wrapper.addComponent(upgradeBtn);
            }
        } else if (item instanceof RequestUploadAvatarNotification) {
            wrapper.addComponent(new Label(FontAwesome.EXCLAMATION_TRIANGLE.getHtml() + " Let people recognize you", ContentMode.HTML));
            Button uploadAvatarBtn = new Button("Upload your avatar", new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent event) {
                    EventBusFactory.getInstance().post(new ShellEvent.GotoUserAccountModule(this, new String[]{"preview"}));
                    NotificationButton.this.setPopupVisible(false);
                }
            });
            uploadAvatarBtn.setStyleName(UIConstants.THEME_LINK);
            uploadAvatarBtn.addStyleName("block");
            wrapper.add(uploadAvatarBtn);
        } else if (item instanceof SmtpSetupNotification) {
            Button smtpBtn = new Button("Setup", new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent clickEvent) {
                    EventBusFactory.getInstance().post(
                            new ShellEvent.GotoUserAccountModule(this, new String[]{"setup"}));
                    NotificationButton.this.setPopupVisible(false);
                }
            });
            smtpBtn.setStyleName(UIConstants.THEME_LINK);
            smtpBtn.addStyleName("block");
            Label lbl = new Label(FontAwesome.EXCLAMATION_TRIANGLE.getHtml() + " Your members can not receive any mail " +
                    "notification without a proper SMTP setting", ContentMode.HTML);
            CssLayout lblWrapper = new CssLayout();
            lblWrapper.addComponent(lbl);
            wrapper.with(lblWrapper, smtpBtn).expand(lblWrapper);
        } else if (item instanceof TimezoneNotification) {
            wrapper.addComponent(new Label(FontAwesome.EXCLAMATION_TRIANGLE.getHtml() + " The correct your timezone will help you get " +
                    "the event right", ContentMode.HTML));
            Button actionBtn = new Button("Action", new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent event) {
                    EventBusFactory.getInstance().post(new ShellEvent.GotoUserAccountModule(this, new String[]{"preview"}));
                    NotificationButton.this.setPopupVisible(false);
                }
            });
            actionBtn.setStyleName(UIConstants.THEME_LINK);
            actionBtn.addStyleName("block");
            wrapper.addComponent(actionBtn);
        } else if (item instanceof RequestPreviewNotification) {
            wrapper.addComponent(new Label(FontAwesome.THUMBS_O_UP.getHtml() + " Help us to spread the world",
                    ContentMode.HTML));
            Button dismissBtn = new Button("Dismiss", new ClickListener() {
                @Override
                public void buttonClick(ClickEvent event) {
                    SimpleUser user = AppContext.getUser();
                    user.setRequestad(false);
                    UserService userService = ApplicationContextUtil.getSpringBean(UserService.class);
                    userService.updateSelectiveWithSession(user, AppContext.getUsername());
                    notificationContainer.removeComponent(wrapper);
                    NotificationButton.this.setPopupVisible(false);
                }
            });
            dismissBtn.setStyleName(UIConstants.THEME_LINK);
            dismissBtn.addStyleName("block");
            wrapper.addComponent(dismissBtn);
            Button spreadBtn = new Button("I will", new ClickListener() {
                @Override
                public void buttonClick(ClickEvent event) {
                    UI.getCurrent().addWindow(new AdRequestWindow(AppContext.getUser()));
                    NotificationButton.this.setPopupVisible(false);
                }
            });
            spreadBtn.setStyleName(UIConstants.THEME_LINK);
            spreadBtn.addStyleName("block");
            wrapper.addComponent(spreadBtn);
        } else {
            LOG.error("Do not render notification " + item);
        }
        return wrapper;
    }
}
