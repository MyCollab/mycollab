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
package com.mycollab.vaadin.web.ui;

import com.mycollab.core.MyCollabException;
import com.mycollab.form.service.MasterFormService;
import com.mycollab.form.view.builder.type.AbstractDynaField;
import com.mycollab.form.view.builder.type.DynaForm;
import com.mycollab.form.view.builder.type.DynaSection;
import com.mycollab.form.view.builder.type.DynaSection.LayoutType;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.MyCollabUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.FormContainer;
import com.mycollab.vaadin.ui.IDynaFormLayout;
import com.mycollab.vaadin.web.ui.grid.GridFormLayoutHelper;
import com.vaadin.ui.*;
import org.vaadin.viritin.layouts.MCssLayout;

import java.util.*;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
public class DefaultDynaFormLayout implements IDynaFormLayout {

    private DynaForm dynaForm;

    private Map<String, AbstractDynaField> fieldMappings = new HashMap<>();
    private Map<DynaSection, GridFormLayoutHelper> sectionMappings;
    private Set<String> excludeFields;

    public DefaultDynaFormLayout(String moduleName, DynaForm defaultForm, String... excludeField) {
        if (excludeField.length > 0) {
            this.excludeFields = new HashSet<>(Arrays.asList(excludeField));
        } else {
            this.excludeFields = new HashSet<>();
        }
        MasterFormService formService = AppContextUtil.getSpringBean(MasterFormService.class);
        DynaForm form = formService.findCustomForm(MyCollabUI.getAccountId(), moduleName);
        this.dynaForm = (form != null) ? form : defaultForm;
    }

    public DefaultDynaFormLayout(DynaForm dynaForm, String... excludeField) {
        if (excludeField.length > 0) {
            this.excludeFields = new HashSet<>(Arrays.asList(excludeField));
        } else {
            this.excludeFields = new HashSet<>();
        }
        this.dynaForm = dynaForm;
    }

    @Override
    public AbstractComponent getLayout() {
        FormContainer layout = new FormContainer();
        int sectionCount = dynaForm.getSectionCount();
        sectionMappings = new HashMap<>();

        for (int i = 0; i < sectionCount; i++) {
            DynaSection section = dynaForm.getSection(i);
            if (section.isDeletedSection()) {
                continue;
            }

            if (section.getHeader() != null) {
                Label header = new Label(UserUIContext.getMessage(section.getHeader()));
                MCssLayout formSection = new MCssLayout(header).withStyleName(WebThemes.FORM_SECTION).withFullWidth();
                formSection.addStyleName(WebThemes.HOVER_EFFECT_NOT_BOX);
                layout.addComponent(formSection);
            }

            GridFormLayoutHelper gridLayout;

            if (section.isDeletedSection() || section.getFieldCount() == 0) {
                continue;
            }

            if (section.getLayoutType() == LayoutType.ONE_COLUMN) {
                gridLayout = GridFormLayoutHelper.defaultFormLayoutHelper(2, 1);

                for (int j = 0; j < section.getFieldCount(); j++) {
                    AbstractDynaField dynaField = section.getField(j);
                    if (!excludeFields.contains(dynaField.getFieldName())) {
                        gridLayout.buildCell(UserUIContext.getMessage(dynaField.getDisplayName()),
                                UserUIContext.getMessage(dynaField.getContextHelp()), 0,
                                gridLayout.getRows() - 1, 2, "100%", Alignment.TOP_LEFT);
                        if (j < section.getFieldCount() - 1) {
                            gridLayout.appendRow();
                        }

                        if (dynaField.isCustom()) {
                            fieldMappings.put("customfield." + dynaField.getFieldName(), dynaField);
                        } else {
                            fieldMappings.put(dynaField.getFieldName(), dynaField);
                        }
                    }
                }
            } else if (section.getLayoutType() == LayoutType.TWO_COLUMN) {
                gridLayout = GridFormLayoutHelper.defaultFormLayoutHelper(2, 1);
                int columnIndex = 0;
                for (int j = 0; j < section.getFieldCount(); j++) {
                    AbstractDynaField dynaField = section.getField(j);
                    if (!excludeFields.contains(dynaField.getFieldName())) {
                        if (dynaField.isColSpan()) {
                            if (columnIndex > 0) {
                                gridLayout.appendRow();
                            }
                            gridLayout.buildCell(UserUIContext.getMessage(dynaField.getDisplayName()),
                                    UserUIContext.getMessage(dynaField.getContextHelp()), 0,
                                    gridLayout.getRows() - 1, 2, "100%", Alignment.TOP_LEFT);
                            columnIndex = 0;
                            if (j < section.getFieldCount() - 1) {
                                gridLayout.appendRow();
                            }
                        } else {
                            gridLayout.buildCell(UserUIContext.getMessage(dynaField.getDisplayName()),
                                    UserUIContext.getMessage(dynaField.getContextHelp()), columnIndex, gridLayout.getRows() - 1);
                            columnIndex++;
                            if (columnIndex == 2) {
                                columnIndex = 0;
                                if (j < section.getFieldCount() - 1) {
                                    gridLayout.appendRow();
                                }
                            }
                        }

                        if (dynaField.isCustom()) {
                            fieldMappings.put("customfield." + dynaField.getFieldName(), dynaField);
                        } else {
                            fieldMappings.put(dynaField.getFieldName(), dynaField);
                        }
                    }
                }
            } else {
                throw new MyCollabException("Does not support attachForm layout except 1 or 2 columns");
            }

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
            HorizontalLayout componentWrapper = gridLayout.getComponentWrapper(UserUIContext.getMessage(dynaField.getDisplayName()));
            if (componentWrapper != null) {
                componentWrapper.addComponent(field);
            }
            return field;
        }
        return null;
    }

    @Override
    public Set<String> bindFields() {
        return fieldMappings.keySet();
    }
}
