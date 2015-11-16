/**
 * This file is part of mycollab-mobile.
 *
 * mycollab-mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-mobile.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.mobile.form.view;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.esofthead.mycollab.form.service.MasterFormService;
import com.esofthead.mycollab.form.view.builder.type.AbstractDynaField;
import com.esofthead.mycollab.form.view.builder.type.DynaForm;
import com.esofthead.mycollab.form.view.builder.type.DynaSection;
import com.esofthead.mycollab.mobile.ui.MobileGridFormLayoutHelper;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.IFormLayoutFactory;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Field;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 2.0
 * 
 */
public class DynaFormLayout implements IFormLayoutFactory {
	private static final long serialVersionUID = 1L;

	private static final Logger LOG = LoggerFactory.getLogger(DynaFormLayout.class);

	private DynaForm dynaForm;

	private VerticalLayout layout;

	private final Map<String, AbstractDynaField> fieldMappings = new HashMap<>();
	private Map<DynaSection, MobileGridFormLayoutHelper> sectionMappings;

	public DynaFormLayout(String moduleName, DynaForm defaultForm) {
		MasterFormService formService = ApplicationContextUtil.getSpringBean(MasterFormService.class);
		DynaForm form = formService.findCustomForm(AppContext.getAccountId(), moduleName);

		if (form != null) {
			this.dynaForm = form;
		} else {
			this.dynaForm = defaultForm;
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
					fieldMappings.put("customfield." + dynaField.getFieldName(), dynaField);
				} else {
					fieldMappings.put(dynaField.getFieldName(), dynaField);
				}

			}
		}
	}

	public boolean isVisibleProperty(Object propertyId) {
		return fieldMappings.containsKey(propertyId);
	}

	@Override
	public ComponentContainer getLayout() {
		layout = new VerticalLayout();
		int sectionCount = dynaForm.getSectionCount();
		sectionMappings = new HashMap<>();

		for (int i = 0; i < sectionCount; i++) {
			DynaSection section = dynaForm.getSection(i);
			if (section.isDeletedSection()) {
				continue;
			}
			Label header = new Label(section.getHeader());
			header.setStyleName("h2");
			layout.addComponent(header);

			MobileGridFormLayoutHelper gridLayout;

			if (section.isDeletedSection() || section.getFieldCount() == 0) {
				continue;
			}

			gridLayout = new MobileGridFormLayoutHelper(1, section.getFieldCount(), "100%", "150px", Alignment.TOP_RIGHT);

			gridLayout.getLayout().setWidth("100%");
			gridLayout.getLayout().setMargin(false);
			gridLayout.getLayout().setSpacing(false);
			gridLayout.getLayout().addStyleName("colored-gridlayout");
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
			MobileGridFormLayoutHelper gridLayout = sectionMappings.get(section);
			gridLayout.addComponent(field, dynaField.getDisplayName(), 0, dynaField.getFieldIndex(), Alignment.TOP_RIGHT);

		}
	}
}