package com.mycollab.mobile.module.project.view.bug;

import com.mycollab.mobile.ui.I18NValueListSelect;
import com.mycollab.module.project.i18n.OptionI18nEnum;
import com.mycollab.module.project.i18n.OptionI18nEnum.BugSeverity;
import com.vaadin.server.FontAwesome;

import java.util.Arrays;

/**
 * @author MyCollab Ltd.
 * @since 4.5.2
 */
public class BugSeverityListSelect extends I18NValueListSelect {
    private static final long serialVersionUID = 1L;

    public BugSeverityListSelect() {
        super();
        this.setNullSelectionAllowed(false);
        this.setCaption(null);
        this.loadData(Arrays.asList(OptionI18nEnum.bug_severities));

        this.setItemIcon(BugSeverity.Critical.name(), FontAwesome.STAR);
        this.setItemIcon(BugSeverity.Major.name(), FontAwesome.STAR);
        this.setItemIcon(BugSeverity.Minor.name(), FontAwesome.STAR);
        this.setItemIcon(BugSeverity.Trivial.name(), FontAwesome.STAR);
    }
}
