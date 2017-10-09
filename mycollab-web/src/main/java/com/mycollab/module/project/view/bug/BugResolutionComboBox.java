package com.mycollab.module.project.view.bug;

import com.mycollab.vaadin.web.ui.I18nValueComboBox;

import static com.mycollab.module.project.i18n.OptionI18nEnum.BugResolution.*;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class BugResolutionComboBox extends I18nValueComboBox {
    private static final long serialVersionUID = 1L;

    private BugResolutionComboBox(boolean nullIsAllowable, Enum<?>... values) {
        super(nullIsAllowable, values);
    }

    public static BugResolutionComboBox getInstanceForResolvedBugWindow() {
        return new BugResolutionComboBox(false, Fixed, CannotReproduce, Duplicate, Invalid, InComplete);
    }
}
