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

package com.esofthead.mycollab.module.project.ui.components;

import com.vaadin.data.Property;
import com.vaadin.shared.ui.slider.SliderOrientation;
import com.vaadin.ui.Slider;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class TaskCompleteStatusSelection extends Slider {
	private static final long serialVersionUID = 1L;

	public TaskCompleteStatusSelection() {
        this.setOrientation(SliderOrientation.HORIZONTAL);
        this.setImmediate(true);
        this.setWidth("200px");
        this.addValueChangeListener(new ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                Double value = TaskCompleteStatusSelection.this.getValue();
                if (value != null) {
                    double roundValue = Math.ceil(value/10) * 10;
                    TaskCompleteStatusSelection.this.setValue(roundValue);
                } else {
                    TaskCompleteStatusSelection.this.setValue(0d);
                }
            }
        });
	}
}
