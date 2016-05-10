/**
 * This file is part of mycollab-services.
 *
 * mycollab-services is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-services is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-services.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.common;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
 * @author MyCollab Ltd
 * @since 5.3.1
 */
public class LocalizationEnumXStreamConverter implements Converter {
    @Override
    public void marshal(Object o, HierarchicalStreamWriter writer, MarshallingContext marshallingContext) {
        Enum enumVal = (Enum) o;
        String value = enumVal.name();
        writer.setValue(value);
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext unmarshallingContext) {
        try {
            String value = reader.getValue();
            Class enumCls = unmarshallingContext.getRequiredType();
            return Enum.valueOf(enumCls, value);
        } catch (Exception e) {
            return GenericI18Enum.OPT_UNDEFINED;
        }
    }

    @Override
    public boolean canConvert(Class aClass) {
        return aClass.isEnum() || Enum.class.isAssignableFrom(aClass);
    }
}
