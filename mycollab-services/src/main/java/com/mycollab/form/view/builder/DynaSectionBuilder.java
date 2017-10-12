/**
 * Copyright © MyCollab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
