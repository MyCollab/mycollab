package com.mycollab.form.view.builder;

import com.mycollab.form.view.builder.type.PickListDynaField;

/**
 * @param <T>
 * @author MyCollab Ltd.
 * @since 1.0.0
 */
public class PickListDynaFieldBuilder<T> extends AbstractDynaFieldBuilder<PickListDynaField<T>> {

    public PickListDynaFieldBuilder() {
        field = new PickListDynaField<T>();
    }

    public void options(T... options) {
        for (T option : options) {
            field.addOption(option);
        }
    }
}
