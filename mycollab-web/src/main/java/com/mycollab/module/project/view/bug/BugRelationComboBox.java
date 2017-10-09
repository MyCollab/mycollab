package com.mycollab.module.project.view.bug;

import com.mycollab.module.project.i18n.OptionI18nEnum;
import com.mycollab.vaadin.web.ui.I18nValueComboBox;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class BugRelationComboBox extends I18nValueComboBox {
    private static final long serialVersionUID = 1L;

    public BugRelationComboBox() {
        super(false, OptionI18nEnum.BugRelation.Block, OptionI18nEnum.BugRelation.Duplicated, OptionI18nEnum.BugRelation.Related);
    }

}
