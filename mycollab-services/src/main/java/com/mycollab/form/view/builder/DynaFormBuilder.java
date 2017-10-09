package com.mycollab.form.view.builder;

import com.mycollab.form.view.builder.type.DynaForm;

/**
 * @author MyCollab Ltd.
 * @since 1.0.0
 */
public class DynaFormBuilder {

    private DynaForm form;

    public DynaFormBuilder() {
        form = new DynaForm();
    }

    public DynaFormBuilder sections(DynaSectionBuilder... sections) {
        for (DynaSectionBuilder section : sections) {
            form.sections(section.build());
        }
        return this;
    }

    public DynaForm build() {
        return form;
    }
}
