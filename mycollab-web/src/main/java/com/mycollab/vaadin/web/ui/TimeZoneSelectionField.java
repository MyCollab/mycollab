/**
 * This file is part of mycollab-web.
 *
 * mycollab-web is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-web is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-web.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.vaadin.web.ui;

import com.mycollab.core.utils.TimezoneVal;
import com.vaadin.data.Property;
import com.vaadin.data.Validator;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.util.Collection;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class TimeZoneSelectionField extends CustomField<String> {
    private boolean isVerticalDisplay = true;
    private ValueComboBox areaSelection;
    private ComboBox timezoneSelection;

    public TimeZoneSelectionField(boolean isVerticalDisplay) {
        this.isVerticalDisplay = isVerticalDisplay;
        areaSelection = new ValueComboBox(false, TimezoneVal.getAreas());
        areaSelection.addValueChangeListener(new Property.ValueChangeListener() {

            @Override
            public void valueChange(com.vaadin.data.Property.ValueChangeEvent event) {
                setCboTimeZone((String) areaSelection.getValue());
            }
        });
        timezoneSelection = new ComboBox();
        timezoneSelection.setItemCaptionMode(AbstractSelect.ItemCaptionMode.EXPLICIT_DEFAULTS_ID);
        String area = (String) areaSelection.getItemIds().iterator().next();
        areaSelection.setValue(area);
        setCboTimeZone(area);
    }

    @Override
    protected Component initContent() {
        if (isVerticalDisplay) {
            MVerticalLayout layout = new MVerticalLayout().withMargin(false);
            layout.with(areaSelection, timezoneSelection);
            return layout;
        } else {
            MHorizontalLayout layout = new MHorizontalLayout();
            layout.with(areaSelection, timezoneSelection);
            return layout;
        }
    }

    private void setCboTimeZone(String area) {
        timezoneSelection.removeAllItems();
        Collection<TimezoneVal> timeZones = TimezoneVal.getTimezoneInAreas(area);
        for (TimezoneVal timezoneVal : timeZones) {
            timezoneSelection.addItem(timezoneVal.getId());
            timezoneSelection.setItemCaption(timezoneVal.getId(), timezoneVal.getDisplayName());
        }

        if (timeZones.size() > 0) {
            timezoneSelection.setValue(timeZones.iterator().next());
        }
    }

    @Override
    public void setPropertyDataSource(Property newDataSource) {
        String value = (String) newDataSource.getValue();
        if (value != null) {
            TimezoneVal timezoneVal = new TimezoneVal(value);
            areaSelection.setValue(timezoneVal.getArea());
            timezoneSelection.setValue(value);
        }
        super.setPropertyDataSource(newDataSource);
    }

    @Override
    public String getValue() {
        return (timezoneSelection != null) ? (String) timezoneSelection.getValue() : null;
    }

    @Override
    public void setValue(String newFieldValue) throws ReadOnlyException, Converter.ConversionException {
        TimezoneVal timezoneVal = new TimezoneVal(newFieldValue);
        areaSelection.setValue(timezoneVal.getArea());
        timezoneSelection.setValue(newFieldValue);
        super.setValue(newFieldValue);
    }

    @Override
    public void commit() throws SourceException, Validator.InvalidValueException {
        String timeZoneId = (String) timezoneSelection.getValue();
        setInternalValue(timeZoneId);
        super.commit();
    }

    @Override
    public Class<String> getType() {
        return String.class;
    }
}
