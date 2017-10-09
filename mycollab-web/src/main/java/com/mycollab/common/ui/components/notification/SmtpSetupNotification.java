package com.mycollab.common.ui.components.notification;

import com.mycollab.core.AbstractNotification;

/**
 * @author MyCollab Ltd
 * @since 5.0.4
 */
public class SmtpSetupNotification extends AbstractNotification {

    public SmtpSetupNotification() {
        super(SCOPE_GLOBAL, WARNING);
    }
}
