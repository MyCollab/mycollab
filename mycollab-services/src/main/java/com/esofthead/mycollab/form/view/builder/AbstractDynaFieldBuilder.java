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
package com.esofthead.mycollab.form.view.builder;

import com.esofthead.mycollab.form.view.builder.type.AbstractDynaField;

/**
 * @param <F>
 * @author MyCollab Ltd.
 * @since 1.0.0
 */
public abstract class AbstractDynaFieldBuilder<F extends AbstractDynaField> {
    protected F field;

    public AbstractDynaFieldBuilder<F> fieldIndex(int index) {
        field.setFieldIndex(index);
        return this;
    }

    public AbstractDynaFieldBuilder<F> fieldName(String fieldName) {
        field.setFieldName(fieldName);
        return this;
    }

    @SuppressWarnings("rawtypes")
    public AbstractDynaFieldBuilder<F> fieldName(Enum fieldName) {
        field.setFieldName(fieldName.name());
        return this;
    }

    public AbstractDynaFieldBuilder<F> displayName(String displayName) {
        field.setDisplayName(displayName);
        return this;
    }

    public AbstractDynaFieldBuilder<F> mandatory(boolean isMandatory) {
        field.setMandatory(isMandatory);
        return this;
    }

    public AbstractDynaFieldBuilder<F> required(boolean isRequired) {
        field.setRequired(isRequired);
        return this;
    }

    public AbstractDynaFieldBuilder<F> customField(boolean isCustom) {
        field.setCustom(isCustom);
        return this;
    }

    public AbstractDynaFieldBuilder<F> colSpan(boolean isColSpan) {
        field.setColSpan(isColSpan);
        return this;
    }

    public AbstractDynaField build() {
        return field;
    }
}
