package com.mycollab.form.view.builder;

import com.mycollab.form.view.builder.type.AbstractDynaField;

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

    public AbstractDynaFieldBuilder<F> fieldName(Enum fieldName) {
        field.setFieldName(fieldName.name());
        return this;
    }

    public AbstractDynaFieldBuilder<F> displayName(Enum displayName) {
        field.setDisplayName(displayName);
        return this;
    }

    public AbstractDynaFieldBuilder<F> contextHelp(Enum contextHelp) {
        field.setContextHelp(contextHelp);
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
