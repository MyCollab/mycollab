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
    private ValueComboBox areaSelection;
    private ComboBox timezoneSelection;

    public TimeZoneSelectionField(boolean isVerticalDisplay) {
        this.isVerticalDisplay = isVerticalDisplay;
        areaSelection = new ValueComboBox(false, TimezoneVal.getAreas());
        areaSelection.addValueChangeListener((ValueChangeListener) valueChangeEvent -> setCboTimeZone((String) areaSelection.getValue()));
        timezoneSelection = new ComboBox();
        String area = (String) areaSelection.getSelectedItem().get();
//        areaSelection.setValue(area);
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
            layout.with(areaSelection, timezoneSelection).expand(timezoneSelection);
            return layout;
        }
    }

    private void setCboTimeZone(String area) {
        Collection<TimezoneVal> timeZones = TimezoneVal.getTimezoneInAreas(area);
        timezoneSelection.setItems(timeZones);
        timezoneSelection.setItemCaptionGenerator((ItemCaptionGenerator<TimezoneVal>) timezone -> timezone.getDisplayName());
    }

    @Override
    public String getValue() {
        return (timezoneSelection != null) ? (String) timezoneSelection.getValue() : null;
    }

    @Override
    protected void doSetValue(String s) {

    }
}
