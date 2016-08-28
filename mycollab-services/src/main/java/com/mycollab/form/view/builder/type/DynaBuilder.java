/**
 * This file is part of mycollab-services.
 *
 * mycollab-services is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-services is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-services.  If not, see <http://www.gnu.org/licenses/>.
 */
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
