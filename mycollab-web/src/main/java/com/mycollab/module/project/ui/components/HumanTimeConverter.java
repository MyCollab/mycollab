package com.mycollab.module.project.ui.components;

import com.mycollab.core.UserInvalidInputException;
import com.mycollab.core.utils.HumanTime;
import com.mycollab.core.utils.StringUtils;
import com.vaadin.data.util.converter.Converter;

import java.util.Locale;

/**
 * @author MyCollab Ltd
 * @since 5.1.4
 */
public class HumanTimeConverter implements Converter<String, Long> {
    @Override
    public Long convertToModel(String value, Class<? extends Long> targetType, Locale locale) throws
            ConversionException {
        if (StringUtils.isBlank(value)) {
            return null;
        }
        HumanTime humanTime = HumanTime.eval(value);
        Long duration = new Long(humanTime.getDelta());
        if (duration.intValue() == 0) {
            throw new UserInvalidInputException("Invalid value. The format of duration must be [number] y " +
                    "[number] d [number] h [number] m [number] s");
        }
        return duration;
    }

    @Override
    public String convertToPresentation(Long value, Class<? extends String> targetType, Locale locale) throws
            ConversionException {
        if (value != null) {
            HumanTime humanTime = new HumanTime(value.longValue());
            return humanTime.getExactly();
        } else {
            return "";
        }
    }

    @Override
    public Class<Long> getModelType() {
        return Long.class;
    }

    @Override
    public Class<String> getPresentationType() {
        return String.class;
    }
}
