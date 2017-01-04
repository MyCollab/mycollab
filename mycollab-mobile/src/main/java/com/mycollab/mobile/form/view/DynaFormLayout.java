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
package com.mycollab.mobile.form.view;

import com.mycollab.form.service.MasterFormService;
import com.mycollab.form.view.builder.type.AbstractDynaField;
import com.mycollab.form.view.builder.type.DynaForm;
import com.mycollab.form.view.builder.type.DynaSection;
import com.mycollab.mobile.ui.FormSectionBuilder;
import com.mycollab.mobile.ui.grid.GridFormLayoutHelper;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.MyCollabUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.IFormLayoutFactory;
import com.vaadin.ui.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
public class DynaFormLayout implements IFormLayoutFactory {
    private static final Logger LOG = LoggerFactory.getLogger(DynaFormLayout.class);

    private DynaForm dynaForm;
    private final Map<String, AbstractDynaField> fieldMappings = new HashMap<>();
    private Map<DynaSection, GridFormLayoutHelper> sectionMappings;

    private Set<String> excludeFields;

    public DynaFormLayout(String moduleName, DynaForm defaultForm, String... excludeField) {
        if (excludeField.length > 0) {
            this.excludeFields = new HashSet<>(Arrays.asList(excludeField));
        } else {
            this.excludeFields = new HashSet<>();
        }
        MasterFormService formService = AppContextUtil.getSpringBean(MasterFormService.class);
        DynaForm form = formService.findCustomForm(MyCollabUI.getAccountId(), moduleName);

        this.dynaForm = (form != null) ? form : defaultForm;

        int sectionCount = dynaForm.getSectionCount();
        for (int i = 0; i < sectionCount; i++) {
            DynaSection section = dynaForm.getSection(i);
            if (section.isDeletedSection()) {
                continue;
            }
            int fieldCount = section.getFieldCount();
            for (int j = 0; j < fieldCount; j++) {
                AbstractDynaField dynaField = section.getField(j);
                if (!excludeFields.contains(dynaField.getFieldName())) {
                    if (dynaField.isCustom()) {
                        fieldMappings.put("customfield." + dynaField.getFieldName(), dynaField);
                    } else {
                        fieldMappings.put(dynaField.getFieldName(), dynaField);
                    }
                }
            }
        }
    }

    public boolean isVisibleProperty(Object propertyId) {
        return fieldMappings.containsKey(propertyId);
    }

    @Override
    public AbstractComponent getLayout() {
        VerticalLayout layout = new VerticalLayout();
        int sectionCount = dynaForm.getSectionCount();
        sectionMappings = new HashMap<>();

        for (int i = 0; i < sectionCount; i++) {
            DynaSection section = dynaForm.getSection(i);
            if (section.isDeletedSection()) {
                continue;
            }

            if (section.getHeader() != null) {
                layout.addComponent(FormSectionBuilder.build(UserUIContext.getMessage(section.getHeader())));
            }

            GridFormLayoutHelper gridLayout;

            if (section.isDeletedSection() || section.getFieldCount() == 0) {
                continue;
            }

            gridLayout = GridFormLayoutHelper.defaultFormLayoutHelper(1, section.getFieldCount());
            layout.addComponent(gridLayout.getLayout());

            sectionMappings.put(section, gridLayout);
        }
        return layout;
    }

    @Override
    public Component attachField(Object propertyId, Field<?> field) {
        AbstractDynaField dynaField = fieldMappings.get(propertyId);
        if (dynaField != null) {
            DynaSection section = dynaField.getOwnSection();
            GridFormLayoutHelper gridLayout = sectionMappings.get(section);
            return gridLayout.addComponent(field, UserUIContext.getMessage(dynaField.getDisplayName()), 0, dynaField.getFieldIndex());
        }
        return null;
    }

    @Override
    public Set<String> bindFields() {
        return fieldMappings.keySet();
    }
}