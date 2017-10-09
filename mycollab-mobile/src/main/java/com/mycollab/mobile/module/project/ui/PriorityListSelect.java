package com.mycollab.mobile.module.project.ui;

import com.mycollab.mobile.ui.I18NValueListSelect;
import com.mycollab.module.project.i18n.OptionI18nEnum.Priority;

import java.util.Arrays;

/**
 * @author MyCollab Ltd.
 * @since 4.5.0
 */
public class PriorityListSelect extends I18NValueListSelect {
    private static final long serialVersionUID = 5484692572022056722L;

    public PriorityListSelect() {
        this.setNullSelectionAllowed(false);

        this.loadData(Arrays.asList(Priority.Urgent, Priority.High, Priority.Medium, Priority.Low,
                Priority.None));
        this.setValue(this.getItemIds().iterator().next());
    }
}
