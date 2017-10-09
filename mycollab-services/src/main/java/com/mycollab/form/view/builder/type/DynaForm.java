package com.mycollab.form.view.builder.type;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class DynaForm {
    private List<DynaSection> sections = new ArrayList<>();

    public int getSectionCount() {
        return sections.size();
    }

    public DynaForm sections(DynaSection... sectionArr) {
        for (DynaSection section : sectionArr) {
            sections.add(section);
            section.setParentForm(this);
        }

        Collections.sort(sections);
        return this;
    }

    public DynaSection getSection(int index) {
        return sections.get(index);
    }
}
