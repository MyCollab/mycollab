package com.mycollab.form.view.builder.type;

import com.mycollab.form.view.builder.DynaSectionBuilder;

/**
 * @author MyCollab Ltd
 * @since 5.3.2
 */
public class DynaBuilder {

    public static DynaForm form(DynaSection... sections) {
        DynaForm form = new DynaForm();
        form.sections(sections);
        return form;
    }

    public static DynaSection section(int orderIndex, Enum header, DynaSection.LayoutType layoutType,
                                            AbstractDynaField... fields) {
        DynaSection section = new DynaSectionBuilder().layoutType(layoutType).orderIndex(orderIndex).header(header).build();
        section.fields(fields);
        return section;
    }

    public static DynaSection section(DynaSection.LayoutType layoutType, AbstractDynaField... fields) {
        DynaSection section = new DynaSectionBuilder().layoutType(layoutType).orderIndex(0).header(null).build();
        section.fields(fields);
        return section;
    }

    public static AbstractDynaField field() {
        return null;
    }
}
