/**
 * Copyright © MyCollab
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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

    public DynaForm(DynaSection... sections) {
        sections(sections);
    }

    public DynaForm sections(DynaSection... sections) {
        for (DynaSection section : sections) {
            this.sections.add(section);
            section.setParentForm(this);
        }

        Collections.sort(this.sections);
        return this;
    }

    public DynaSection getSection(int index) {
        return sections.get(index);
    }
}
