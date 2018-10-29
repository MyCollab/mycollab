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
package com.mycollab.vaadin.web.ui.field.converter;


import com.vaadin.data.Converter;
import com.vaadin.data.Result;
import com.vaadin.data.ValueContext;
import org.joda.time.LocalDate;

import java.util.Date;

/**
 * @author MyCollab Ltd
 * @since 5.1.3
 */
// TODO
public class LocalDateConverter implements Converter<Date, LocalDate> {
//    @Override
//    public LocalDate convertToModel(Date value, Class<? extends LocalDate> targetType, Locale locale) throws ConversionException {
//        return new LocalDate(value.getTime());
//    }
//
//    @Override
//    public Date convertToPresentation(LocalDate value, Class<? extends Date> targetType, Locale locale) throws ConversionException {
//        return value.toDate();
//    }
//
//    @Override
//    public Class<LocalDate> getModelType() {
//        return LocalDate.class;
//    }
//
//    @Override
//    public Class<Date> getPresentationType() {
//        return Date.class;
//    }


    @Override
    public Result<LocalDate> convertToModel(Date date, ValueContext valueContext) {
        return null;
    }

    @Override
    public Date convertToPresentation(LocalDate localDate, ValueContext valueContext) {
        return null;
    }
}
