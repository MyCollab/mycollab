package com.mycollab.module.user.view.component;

import com.mycollab.common.i18n.SecurityI18nEnum;
import com.mycollab.security.BooleanPermissionFlag;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.web.ui.KeyCaptionComboBox;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class YesNoPermissionComboBox extends KeyCaptionComboBox {
    private static final long serialVersionUID = 1L;

    public YesNoPermissionComboBox() {
        super(false);
        this.addItem(BooleanPermissionFlag.TRUE, UserUIContext.getMessage(SecurityI18nEnum.YES));
        this.addItem(BooleanPermissionFlag.FALSE, UserUIContext.getMessage(SecurityI18nEnum.NO));
        this.setValue(BooleanPermissionFlag.FALSE);
    }
}
