package com.mycollab.vaadin.web.ui.service;

import com.mycollab.core.BroadcastListener;
import com.mycollab.web.DesktopApplication;

/**
 * @author MyCollab Ltd
 * @since 5.3.5
 */
public interface BroadcastReceiverService extends BroadcastListener {
    void registerApp(DesktopApplication myCollabApp);
}
