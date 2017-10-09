package com.mycollab.module.project.view.bug;

import com.mycollab.module.project.i18n.OptionI18nEnum;
import com.mycollab.module.project.i18n.OptionI18nEnum.BugSeverity;
import com.mycollab.vaadin.web.ui.I18nValueComboBox;
import com.vaadin.data.Property;
import com.vaadin.server.FontAwesome;

import java.util.Arrays;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class BugSeverityComboBox extends I18nValueComboBox {
    private static final long serialVersionUID = 1L;

    public BugSeverityComboBox() {
        super();
        this.setNullSelectionAllowed(false);
        this.setCaption(null);
        this.loadData(Arrays.asList(OptionI18nEnum.bug_severities));

        this.setItemIcon(BugSeverity.Critical.name(), FontAwesome.STAR);
        this.setItemIcon(BugSeverity.Major.name(), FontAwesome.STAR);
        this.setItemIcon(BugSeverity.Minor.name(), FontAwesome.STAR);
        this.setItemIcon(BugSeverity.Trivial.name(), FontAwesome.STAR);

        this.setItemStyleGenerator((source, itemId) -> {
            if (itemId != null) {
                return "bug-severity-" + itemId.toString().toLowerCase();
            } else {
                return null;
            }
        });
    }

    @Override
    public void setPropertyDataSource(Property newDataSource) {
        Object value = newDataSource.getValue();
        if (value == null) {
            newDataSource.setValue(OptionI18nEnum.BugSeverity.Major.name());
        }
        super.setPropertyDataSource(newDataSource);
    }
}
