package com.mycollab.mobile.module.project.view.bug;

import com.mycollab.mobile.ui.I18NValueListSelect;
import com.mycollab.module.project.i18n.OptionI18nEnum.BugResolution;

/**
 * @author MyCollab Ltd.
 * @since 4.5.2
 */
public class BugResolutionListSelect extends I18NValueListSelect {
    private static final long serialVersionUID = 1L;

    private BugResolutionListSelect(boolean nullIsAllowable, Enum<?>... values) {
        super(nullIsAllowable, values);
    }

    public static BugResolutionListSelect getInstanceForWontFixWindow() {
        return new BugResolutionListSelect(false, BugResolution.CannotReproduce, BugResolution.Duplicate, BugResolution.Invalid);
    }

    public static BugResolutionListSelect getInstanceForResolvedBugWindow() {
        return new BugResolutionListSelect(false, BugResolution.Fixed);
    }
}
