package com.mycollab.common.ui.components.notification;

import com.mycollab.core.AbstractNotification;

/**
 * @author MyCollab Ltd.
 * @since 4.5.2
 */
public class RequestUploadAvatarNotification extends AbstractNotification {

    public RequestUploadAvatarNotification() {
        super(SCOPE_USER, WARNING);
    }
}
