/**
 * Copyright © MyCollab
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.vaadin.web.ui;

import com.mycollab.core.utils.TimezoneVal;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.ItemCaptionGenerator;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.util.Collection;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class TimeZoneSelectionField extends CustomField<String> {
    private boolean isVerticalDisplay;
    private StringValueComboBox areaSelection;
    private ComboBox<TimezoneVal> timezoneSelection;

    public TimeZoneSelectionField(boolean isVerticalDisplay) {
        this.isVerticalDisplay = isVerticalDisplay;
        areaSelection = new StringValueComboBox(false, TimezoneVal.getAreas());
        areaSelection.addValueChangeListener((ValueChangeListener) event -> setCboTimeZone(areaSelection.getValue()));
        timezoneSelection = new ComboBox<>();
        timezoneSelection.setWidth(WebThemes.FORM_CONTROL_WIDTH);
        String area = areaSelection.getSelectedItem().orElse(null);
        areaSelection.setValue(area);
        setCboTimeZone(area);
    }

    @Override
    protected Component initContent() {
        return (isVerticalDisplay) ? new MVerticalLayout(areaSelection, timezoneSelection).withMargin(false) :
                new MHorizontalLayout(areaSelection, timezoneSelection);
    }

    private void setCboTimeZone(String area) {
        Collection<TimezoneVal> timeZones = TimezoneVal.getTimezoneInAreas(area);
        timezoneSelection.setItems(timeZones);
        timezoneSelection.setItemCaptionGenerator((ItemCaptionGenerator<TimezoneVal>) timezone -> timezone.getDisplayName());
    }

    @Override
    public String getValue() {
        TimezoneVal value = timezoneSelection.getValue();
        return (value != null) ? value.getId() : null;
    }

    @Override
    protected void doSetValue(String zoneId) {
        TimezoneVal tzVal = new TimezoneVal(zoneId);

        areaSelection.setValue(tzVal.getArea());
        timezoneSelection.setValue(tzVal);
    }
}
