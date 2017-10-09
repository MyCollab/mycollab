package com.mycollab.form.view.builder;

import com.mycollab.form.view.builder.type.DynaSection;
import com.mycollab.form.view.builder.type.DynaSection.LayoutType;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class DynaSectionBuilder {

    private DynaSection section;

    public DynaSectionBuilder() {
        section = new DynaSection();
    }

    public DynaSectionBuilder header(Enum header) {
        section.setHeader(header);
        return this;
    }

    public DynaSectionBuilder layoutType(LayoutType layoutType) {
        section.setLayoutType(layoutType);
        return this;
    }

    public DynaSectionBuilder orderIndex(int orderIndex) {
        section.setOrderIndex(orderIndex);
        return this;
    }

    public DynaSectionBuilder deleteSection(boolean isDeleteSection) {
        section.setDeletedSection(isDeleteSection);
        return this;
    }

    public DynaSectionBuilder fields(AbstractDynaFieldBuilder<?>... fields) {
        for (AbstractDynaFieldBuilder<?> field : fields) {
            section.fields(field.build());
        }
        return this;
    }

    public DynaSection build() {
        return section;
    }
}
