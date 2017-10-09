package com.mycollab.mobile.ui;

import com.mycollab.common.i18n.OptionI18nEnum.StatusI18nEnum;
import com.mycollab.vaadin.UserUIContext;
import com.vaadin.data.Property;

import java.util.Arrays;
import java.util.List;

/**
 * @author MyCollab Ltd
 * @since 5.2.5
 */
public class I18NValueListSelect extends ValueListSelect {
    private static final long serialVersionUID = 1L;

    public I18NValueListSelect() {
        super();
    }

    public I18NValueListSelect(boolean nullIsAllowable, Enum<?>... keys) {
        setNullSelectionAllowed(nullIsAllowable);
        loadData(Arrays.asList(keys));
    }

    public final void loadData(List<? extends Enum<?>> values) {
        this.setItemCaptionMode(ItemCaptionMode.EXPLICIT_DEFAULTS_ID);

        for (Enum<?> entry : values) {
            this.addItem(entry.name());
            this.setItemCaption(entry.name(), UserUIContext.getMessage(entry));
        }

        if (!this.isNullSelectionAllowed() && getItemIds().size() > 0) {
            this.select(this.getItemIds().iterator().next());
        }
    }

    @Override
    public void setPropertyDataSource(Property newDataSource) {
        Object value = newDataSource.getValue();
        if (value == null) {
            newDataSource.setValue(StatusI18nEnum.Open.name());
        }
        super.setPropertyDataSource(newDataSource);
    }
}
