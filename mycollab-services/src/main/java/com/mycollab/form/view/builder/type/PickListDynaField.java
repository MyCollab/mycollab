package com.mycollab.form.view.builder.type;

import java.util.ArrayList;
import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class PickListDynaField<T> extends AbstractDynaField {
    private List<T> options = new ArrayList<>();

    public void addOption(T option) {
        options.add(option);
    }
}
