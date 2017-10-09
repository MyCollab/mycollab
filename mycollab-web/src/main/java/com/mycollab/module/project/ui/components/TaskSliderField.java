package com.mycollab.module.project.ui.components;

import com.vaadin.data.Property;
import com.vaadin.data.Validator;
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
        slider.setImmediate(true);
        slider.setWidth("150px");
        slider.addValueChangeListener(valueChangeEvent -> {
            Double value = (Double) valueChangeEvent.getProperty().getValue();
            if (value != null) {
                double roundValue = Math.ceil(value / 10) * 10;
                slider.setValue(roundValue);
                progressLbl.setValue(roundValue + "%");
                setInternalValue(roundValue);
            } else {
                slider.setValue(0d);
                progressLbl.setValue("0%");
                setInternalValue(0d);
            }
        });
        body = new MHorizontalLayout(slider, progressLbl);
    }

    @Override
    public void setPropertyDataSource(Property newDataSource) {
        Double value = (Double) newDataSource.getValue();
        if (value != null) {
            double roundValue = Math.ceil(value / 10) * 10;
            newDataSource.setValue(roundValue);
            slider.setPropertyDataSource(newDataSource);
            progressLbl.setValue(roundValue + "%");
        }
        super.setPropertyDataSource(newDataSource);
    }

    @Override
    public boolean isModified() {
        return slider.isModified();
    }

    @Override
    public void setBuffered(boolean buffered) {
        slider.setBuffered(buffered);
    }

    @Override
    protected Component initContent() {
        return body;
    }

    @Override
    public void commit() throws SourceException, Validator.InvalidValueException {
        Double value = slider.getValue();
        setInternalValue(value);
        super.commit();
    }

    @Override
    public Class<? extends Double> getType() {
        return Double.class;
    }
}
