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
package com.esofthead.mycollab.form.view.builder.type;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class DynaForm {
	private List<DynaSection> sections = new ArrayList<>();

	public int getSectionCount() {
		return sections.size();
	}

	public void addSection(DynaSection section) {
		sections.add(section);
		section.setParentForm(this);
		Collections.sort(sections);
	}

	public void addSections(Collection<DynaSection> sectionCol) {
		for (DynaSection section : sectionCol) {
			sections.add(section);
			section.setParentForm(this);
		}

		Collections.sort(sections);
	}

	public DynaSection getSection(int index) {
		return sections.get(index);
	}
}
