/**
 * This file is part of mycollab-web.
 *
 * mycollab-web is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-web is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-web.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.module.project.ui.components;

import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.form.view.builder.type.AbstractDynaField;
import com.esofthead.mycollab.form.view.builder.type.DynaForm;
import com.esofthead.mycollab.form.view.builder.type.DynaSection;
import com.esofthead.mycollab.form.view.builder.type.DynaSection.LayoutType;
import com.esofthead.mycollab.vaadin.ui.GridFormLayoutHelper;
import com.esofthead.mycollab.vaadin.ui.IFormLayoutFactory;
import com.vaadin.ui.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.5.4
 *
 */
public class DynaFormLayout implements IFormLayoutFactory {
	private static final long serialVersionUID = 1L;

	private static final Logger LOG = LoggerFactory
			.getLogger(DynaFormLayout.class);

	private DynaForm dynaForm;

	private Map<String, AbstractDynaField> fieldMappings = new HashMap<>();
	private Map<DynaSection, GridFormLayoutHelper> sectionMappings;

	private Set<String> excludeFields;

	public DynaFormLayout(String moduleName, DynaForm defaultForm) {
		this(moduleName, defaultForm, new String[0]);
	}

	public DynaFormLayout(String moduleName, DynaForm defaultForm,
			String... excludeField) {
		// MasterFormService formService = ApplicationContextUtil
		// .getSpringBean(MasterFormService.class);
		// DynaForm form = formService.findCustomForm(AppContext.getAccountId(),
		// moduleName);
		//
		// this.dynaForm = (form != null) ? form : defaultForm;
		this.dynaForm = defaultForm;
		if (excludeField.length > 0) {
			this.excludeFields = new HashSet<>(Arrays.asList(excludeField));
		} else {
			this.excludeFields = new HashSet<>();
		}

		LOG.debug("Fill fields of originSection to map field");

		int sectionCount = dynaForm.getSectionCount();
		for (int i = 0; i < sectionCount; i++) {
			DynaSection section = dynaForm.getSection(i);
			if (section.isDeletedSection()) {
				continue;
			}
			int fieldCount = section.getFieldCount();
			for (int j = 0; j < fieldCount; j++) {
				AbstractDynaField dynaField = section.getField(j);
				if (dynaField.isCustom()) {
					fieldMappings.put(
							"customfield." + dynaField.getFieldName(),
							dynaField);
				} else {
					fieldMappings.put(dynaField.getFieldName(), dynaField);
				}

			}
		}
	}

	@Override
	public ComponentContainer getLayout() {
		VerticalLayout layout = new VerticalLayout();
		int sectionCount = dynaForm.getSectionCount();
		sectionMappings = new HashMap<>();

		for (int i = 0; i < sectionCount; i++) {
			DynaSection section = dynaForm.getSection(i);
			if (section.isDeletedSection()) {
				continue;
			}

			GridFormLayoutHelper gridLayout;

			if (section.isDeletedSection() || section.getFieldCount() == 0) {
				continue;
			}

			if (section.getLayoutType() == LayoutType.ONE_COLUMN) {
				gridLayout =  GridFormLayoutHelper.defaultFormLayoutHelper(2, 1);

				for (int j = 0; j < section.getFieldCount(); j++) {
					AbstractDynaField dynaField = section.getField(j);
					if (!excludeFields.contains(dynaField.getFieldName())) {
						gridLayout.buildCell(dynaField.getDisplayName(), 0,
								gridLayout.getRows() - 1, 2, "100%",
								Alignment.TOP_LEFT);
						if (j < section.getFieldCount() - 1) {
							gridLayout.appendRow();
						}
					}
				}
			} else if (section.getLayoutType() == LayoutType.TWO_COLUMN) {
				gridLayout =  GridFormLayoutHelper.defaultFormLayoutHelper(2, 1);
				int columnIndex = 0;
				for (int j = 0; j < section.getFieldCount(); j++) {
					AbstractDynaField dynaField = section.getField(j);
					if (!excludeFields.contains(dynaField.getFieldName())) {
						if (dynaField.isColSpan()) {
							if (columnIndex > 0) {
								gridLayout.appendRow();
							}
							LOG.debug("Build cell {}",
									new Object[] { dynaField.getDisplayName() });
							gridLayout.buildCell(dynaField.getDisplayName(), 0,
									gridLayout.getRows() - 1, 2, "100%",
									Alignment.TOP_LEFT);
							columnIndex = 0;
							if (j < section.getFieldCount() - 1) {
								gridLayout.appendRow();
							}
						} else {
							LOG.debug("Build cell {}",
									new Object[] { dynaField.getDisplayName() });
							gridLayout.buildCell(dynaField.getDisplayName(),
									columnIndex, gridLayout.getRows() - 1);
							columnIndex++;
							if (columnIndex == 2) {
								columnIndex = 0;
								if (j < section.getFieldCount() - 1) {
									gridLayout.appendRow();
								}
							}
						}
					}
				}
			} else {
				throw new MyCollabException(
						"Does not support attachForm layout except 1 or 2 columns");
			}

			layout.addComponent(gridLayout.getLayout());

			sectionMappings.put(section, gridLayout);
		}
		return layout;
	}

	@Override
	public void attachField(Object propertyId, Field<?> field) {
		AbstractDynaField dynaField = fieldMappings.get(propertyId);
		if (dynaField != null) {
			DynaSection section = dynaField.getOwnSection();
			GridFormLayoutHelper gridLayout = sectionMappings.get(section);
			HorizontalLayout componentWrapper = gridLayout
					.getComponentWrapper(dynaField.getDisplayName());
			if (componentWrapper != null) {
				componentWrapper.addComponent(field);
			}
		}
	}
}
