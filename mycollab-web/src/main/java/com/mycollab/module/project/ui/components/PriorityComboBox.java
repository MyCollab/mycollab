package com.mycollab.module.project.ui.components;

import com.mycollab.module.project.i18n.OptionI18nEnum.Priority;
import com.mycollab.vaadin.web.ui.I18nValueComboBox;
import com.vaadin.data.Property;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.ComboBox;

import java.util.Arrays;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class PriorityComboBox extends I18nValueComboBox {
    private static final long serialVersionUID = 1L;

    public PriorityComboBox() {
        this.setNullSelectionAllowed(false);
        this.setWidth("150px");

        this.loadData(Arrays.asList(Priority.Urgent, Priority.High, Priority.Medium, Priority.Low, Priority.None));

        this.setItemIcon(Priority.Urgent.name(), FontAwesome.ARROW_UP);
        this.setItemIcon(Priority.High.name(), FontAwesome.ARROW_UP);
        this.setItemIcon(Priority.Medium.name(), FontAwesome.ARROW_UP);
        this.setItemIcon(Priority.Low.name(), FontAwesome.ARROW_DOWN);
        this.setItemIcon(Priority.None.name(), FontAwesome.ARROW_DOWN);

        this.setItemStyleGenerator((source, itemId) -> {
            if (itemId != null) {
                return String.format("task-%s", itemId.toString().toLowerCase());
            }
            return null;
        });
    }

    @Override
    public void setPropertyDataSource(Property newDataSource) {
        Object value = newDataSource.getValue();
        if (value == null) {
            newDataSource.setValue(Priority.Medium.name());
        }
        super.setPropertyDataSource(newDataSource);
    }
}
