package com.mycollab.module.project.view;

import com.mycollab.common.i18n.OptionI18nEnum;
import com.mycollab.vaadin.web.ui.I18nValueComboBox;

/**
 * @author MyCollab Ltd
 * @since 5.3.5
 */
public class ProjectStatusComboBox  extends I18nValueComboBox {
    private static final long serialVersionUID = 1L;

    public ProjectStatusComboBox() {
        super(false, OptionI18nEnum.StatusI18nEnum.Open, OptionI18nEnum.StatusI18nEnum.Closed);
    }
}
