package com.mycollab.module.user.view.component;

import com.mycollab.core.MyCollabException;
import com.mycollab.security.AccessPermissionFlag;
import com.mycollab.security.BooleanPermissionFlag;
import com.mycollab.security.PermissionFlag;
import com.mycollab.vaadin.web.ui.KeyCaptionComboBox;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class PermissionComboBoxFactory {
    public static KeyCaptionComboBox createPermissionSelection(Class<? extends PermissionFlag> flag) {
        if (AccessPermissionFlag.class.isAssignableFrom(flag)) {
            return new AccessPermissionComboBox();
        } else if (BooleanPermissionFlag.class.isAssignableFrom(flag)) {
            return new YesNoPermissionComboBox();
        } else {
            throw new MyCollabException("Do not support permission flag " + flag);
        }
    }
}
