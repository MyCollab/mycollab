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
package com.mycollab.module.project.ui.components;

import com.mycollab.vaadin.web.ui.WebThemes;
import com.vaadin.shared.ui.slider.SliderOrientation;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.Label;
import com.vaadin.ui.Slider;
import org.vaadin.viritin.layouts.MHorizontalLayout;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class TaskSliderField extends CustomField<Double> {
    private static final long serialVersionUID = 1L;

    private Slider slider;
    private MHorizontalLayout body;
    private Label progressLbl;

    public TaskSliderField() {
        progressLbl = new Label();
        progressLbl.setWidthUndefined();
        slider = new Slider();
        slider.setOrientation(SliderOrientation.HORIZONTAL);
        slider.setWidth(WebThemes.FORM_CONTROL_WIDTH);
        slider.addValueChangeListener(valueChangeEvent -> {
            displayValue(valueChangeEvent.getValue());
        });
        body = new MHorizontalLayout(slider, progressLbl);
    }

    @Override
    protected Component initContent() {
        return body;
    }


    @Override
    protected void doSetValue(Double value) {
        displayValue(value);
    }

    @Override
    public Double getValue() {
        return slider.getValue();
    }

    private void displayValue(Double value) {
        if (value != null) {
            double roundValue = Math.ceil(value / 10) * 10;
            slider.setValue(roundValue);
            progressLbl.setValue(roundValue + "%");
        } else {
            slider.setValue(0d);
            progressLbl.setValue("0%");
        }
    }
}
