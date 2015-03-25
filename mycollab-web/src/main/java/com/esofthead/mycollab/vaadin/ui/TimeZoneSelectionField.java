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
package com.esofthead.mycollab.vaadin.ui;

import com.esofthead.mycollab.core.utils.TimezoneMapper;
import com.esofthead.mycollab.core.utils.TimezoneMapper.TimezoneExt;
import com.vaadin.data.Property;
import com.vaadin.data.Validator;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import org.vaadin.maddon.layouts.MHorizontalLayout;
import org.vaadin.maddon.layouts.MVerticalLayout;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
@SuppressWarnings("serial")
public class TimeZoneSelectionField extends CustomField<String> {
    private boolean isVerticalDisplay = true;
	private ValueComboBox comboArea;
	private ValueComboBox comboTimezone;
	private List<String> lstLimeZoneArea = new ArrayList<>();

	public TimeZoneSelectionField(boolean isVerticalDisplay) {
        this.isVerticalDisplay = isVerticalDisplay;
		comboArea = new ValueComboBox(false, TimezoneMapper.AREAS);
		comboArea.addValueChangeListener(new Property.ValueChangeListener() {

			@Override
			public void valueChange(
					com.vaadin.data.Property.ValueChangeEvent event) {
				lstLimeZoneArea.clear();
				setCboTimeZone(event.getProperty().getValue().toString().trim());
			}
		});

		for (TimezoneExt timezone : TimezoneMapper.timeMap.values()) {
			if (timezone.getArea().equals(comboArea.getValue())) {
				lstLimeZoneArea.add(timezone.getDisplayName());
			}
		}

		String[] arrayTimezone = lstLimeZoneArea
				.toArray(new String[lstLimeZoneArea.size()]);

		comboTimezone = new ValueComboBox(false, arrayTimezone);
	}

	@Override
	protected Component initContent() {
        if (isVerticalDisplay) {
            MVerticalLayout layout = new MVerticalLayout().withMargin(false);
            layout.with(comboArea, comboTimezone);
            return layout;
        } else {
            MHorizontalLayout layout = new MHorizontalLayout();
            layout.with(comboArea, comboTimezone);
            return layout;
        }
	}

	private void setCboTimeZone(String area) {
		for (TimezoneExt timezone : TimezoneMapper.timeMap.values()) {
			if (timezone.getArea().trim().equals(area)) {
				lstLimeZoneArea.add(timezone.getDisplayName());
			}
		}

		comboTimezone.removeAllItems();
		String[] arrayTimezone = lstLimeZoneArea
				.toArray(new String[lstLimeZoneArea.size()]);
		comboTimezone.loadData(arrayTimezone);
	}

	public void setTimeZone(TimezoneExt timeZone) {
		if (timeZone != null && !timeZone.getArea().equals("")) {
			comboArea.select(timeZone.getArea());
			setCboTimeZone(timeZone.getArea());
			comboTimezone.select(timeZone.getDisplayName());
		}
	}

	public TimezoneExt getTimeZone() {
		for (TimezoneExt timezone : TimezoneMapper.timeMap.values()) {
			if (timezone.getDisplayName().trim()
					.equals(comboTimezone.getValue())) {
				return timezone;
			}
		}
		return null;
	}

    @Override
    public void setPropertyDataSource(Property newDataSource) {
        String value = (String) newDataSource.getValue();
        if (value != null) {

        }
        super.setPropertyDataSource(newDataSource);
    }

    @Override
    public void commit() throws SourceException, Validator.InvalidValueException {

        super.commit();
    }

	@Override
	public Class<String> getType() {
		return String.class;
	}
}
