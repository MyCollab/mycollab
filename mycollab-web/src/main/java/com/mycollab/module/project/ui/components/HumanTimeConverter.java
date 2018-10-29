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

import com.vaadin.data.Converter;
import com.vaadin.data.Result;
import com.vaadin.data.ValueContext;

/**
 * @author MyCollab Ltd
 * @since 5.1.4
 */
// TODO
public class HumanTimeConverter implements Converter<String, Long> {
//    @Override
//    public Long convertToModel(String value, Class<? extends Long> targetType, Locale locale) throws
//            ConversionException {
//        if (StringUtils.isBlank(value)) {
//            return null;
//        }
//        HumanTime humanTime = HumanTime.eval(value);
//        Long duration = new Long(humanTime.getDelta());
//        if (duration.intValue() == 0) {
//            throw new UserInvalidInputException("Invalid value. The format of duration must be [number] y " +
//                    "[number] d [number] h [number] m [number] s");
//        }
//        return duration;
//    }
//
//    @Override
//    public String convertToPresentation(Long value, Class<? extends String> targetType, Locale locale) throws
//            ConversionException {
//        if (value != null) {
//            HumanTime humanTime = new HumanTime(value.longValue());
//            return humanTime.getExactly();
//        } else {
//            return "";
//        }
//    }
//
//    @Override
//    public Class<Long> getModelType() {
//        return Long.class;
//    }
//
//    @Override
//    public Class<String> getPresentationType() {
//        return String.class;
//    }


    @Override
    public Result<Long> convertToModel(String s, ValueContext valueContext) {
        return null;
    }

    @Override
    public String convertToPresentation(Long aLong, ValueContext valueContext) {
        return null;
    }
}
