package com.mycollab.vaadin.web.ui.service;

import com.google.common.eventbus.EventBus;
import com.mycollab.cache.service.CacheService;
import com.mycollab.core.AbstractNotification;
import com.mycollab.core.BroadcastMessage;
import com.mycollab.shell.event.ShellEvent;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.ui.MyCollabSession;
import com.mycollab.web.DesktopApplication;

import static com.mycollab.vaadin.ui.MyCollabSession.EVENT_BUS_VAL;

/**
 * @author MyCollab Ltd
 * @since 5.3.5
 */
public abstract class AbstractBroadcastReceiverService implements BroadcastReceiverService {

    protected DesktopApplication myCollabApp;

    public void registerApp(DesktopApplication myCollabApp) {
        this.myCollabApp = myCollabApp;
    }

    @Override
    public void broadcast(BroadcastMessage message) {
        if (message.getWrapObj() instanceof AbstractNotification) {
            EventBus eventBus = (EventBus) myCollabApp.getAttribute(MyCollabSession.EVENT_BUS_VAL);
            eventBus.post(new ShellEvent.NewNotification(this, message.getWrapObj()));

            CacheService cacheService = AppContextUtil.getSpringBean(CacheService.class);
            if (message.getsAccountId() != null) {
                cacheService.putValue(message.getsAccountId() + "", "notification", message.getWrapObj());
            }
        } else {
            onBroadcast(message);
        }
    }

    abstract protected void onBroadcast(BroadcastMessage message);
}
