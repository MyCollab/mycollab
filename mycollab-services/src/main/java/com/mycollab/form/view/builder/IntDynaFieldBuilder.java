package com.mycollab.form.view.builder;

import com.mycollab.form.view.builder.type.IntDynaField;

/**
 * @author MyCollab Ltd.
 * @since 1.0.0
 */
public class IntDynaFieldBuilder extends AbstractDynaFieldBuilder<IntDynaField> {
    public IntDynaFieldBuilder() {
        field = new IntDynaField();
    }

    public IntDynaFieldBuilder maxValue(int maxValue) {
        field.setMaxValue(maxValue);
        return this;
    }

}
