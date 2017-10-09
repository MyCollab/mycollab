package com.mycollab.module.user.view.component;

import com.mycollab.common.i18n.SecurityI18nEnum;
import com.mycollab.security.AccessPermissionFlag;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.web.ui.KeyCaptionComboBox;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class AccessPermissionComboBox extends KeyCaptionComboBox {
    private static final long serialVersionUID = 1L;

    public AccessPermissionComboBox() {
        super(false);

        this.addItem(AccessPermissionFlag.NO_ACCESS, UserUIContext.getMessage(SecurityI18nEnum.NO_ACCESS));
        this.addItem(AccessPermissionFlag.READ_ONLY, UserUIContext.getMessage(SecurityI18nEnum.READONLY));
        this.addItem(AccessPermissionFlag.READ_WRITE, UserUIContext.getMessage(SecurityI18nEnum.READ_WRITE));
        this.addItem(AccessPermissionFlag.ACCESS, UserUIContext.getMessage(SecurityI18nEnum.ACCESS));
        this.setValue(AccessPermissionFlag.READ_ONLY);
    }
}
