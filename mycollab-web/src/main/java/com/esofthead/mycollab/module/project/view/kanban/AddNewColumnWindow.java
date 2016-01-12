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
package com.esofthead.mycollab.module.project.view.kanban;

import com.esofthead.mycollab.common.domain.OptionVal;
import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.common.service.OptionValService;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.view.IKanbanView;
import com.esofthead.mycollab.security.RolePermissionCollections;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.web.ui.UIConstants;
import com.esofthead.mycollab.vaadin.web.ui.grid.GridFormLayoutHelper;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import com.vaadin.ui.Button;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.awt.*;
import java.util.GregorianCalendar;

/**
 * @author MyCollab Ltd
 * @since 5.1.1
 */
public class AddNewColumnWindow extends Window {
    private static final Color DEFAULT_COLOR = Color.decode("#fdde86");

    public AddNewColumnWindow(final IKanbanView kanbanView, final String type) {
        super("Add column");
        this.setWidth("800px");
        this.setModal(true);
        this.setResizable(false);
        this.center();
        MVerticalLayout layout = new MVerticalLayout().withMargin(new MarginInfo(false, false, true, false));
        GridFormLayoutHelper gridFormLayoutHelper = GridFormLayoutHelper.defaultFormLayoutHelper(1, 4);
        this.setContent(layout);

        final TextField stageField = new TextField();
        final CheckBox defaultProject = new CheckBox();
        defaultProject.setEnabled(AppContext.canBeYes(RolePermissionCollections.GLOBAL_PROJECT_SETTINGS));
        final ColorPicker colorPicker = new ColorPicker("", new com.vaadin.shared.ui.colorpicker.Color(DEFAULT_COLOR.getRed(),
                DEFAULT_COLOR.getGreen(), DEFAULT_COLOR.getBlue()));
        final TextArea description = new TextArea();

        gridFormLayoutHelper.addComponent(stageField, "Stage name", 0, 0);
        gridFormLayoutHelper.addComponent(defaultProject, "Default for new projects", 0, 1);
        gridFormLayoutHelper.addComponent(colorPicker, "Color", 0, 2);
        gridFormLayoutHelper.addComponent(description, "Description", 0, 3);

        Button saveBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_SAVE), new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                OptionVal optionVal = new OptionVal();
                optionVal.setCreatedtime(new GregorianCalendar().getTime());
                optionVal.setCreateduser(AppContext.getUsername());
                optionVal.setDescription(description.getValue());
                com.vaadin.shared.ui.colorpicker.Color color = colorPicker.getColor();
                String cssColor = color.getCSS();
                if (cssColor.startsWith("#")) {
                    cssColor = cssColor.substring(1);
                }
                optionVal.setColor(cssColor);
                if (defaultProject.getValue()) {
                    optionVal.setIsdefault(true);
                } else {
                    optionVal.setIsdefault(false);
                    optionVal.setExtraid(CurrentProjectVariables.getProjectId());
                }
                optionVal.setSaccountid(AppContext.getAccountId());
                optionVal.setType(type);
                optionVal.setTypeval(stageField.getValue());
                OptionValService optionService = ApplicationContextUtil.getSpringBean(OptionValService.class);
                int optionValId = optionService.saveWithSession(optionVal, AppContext.getUsername());

                if (optionVal.getIsdefault()) {
                    optionVal.setId(null);
                    optionVal.setIsdefault(false);
                    optionVal.setRefoption(optionValId);
                    optionVal.setExtraid(CurrentProjectVariables.getProjectId());
                    optionService.saveWithSession(optionVal, AppContext.getUsername());
                }
                kanbanView.addColumn(optionVal);
                AddNewColumnWindow.this.close();
            }
        });
        saveBtn.setIcon(FontAwesome.SAVE);
        saveBtn.setStyleName(UIConstants.BUTTON_ACTION);

        Button cancelBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_CANCEL), new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                AddNewColumnWindow.this.close();
            }
        });
        cancelBtn.setStyleName(UIConstants.THEME_GRAY_LINK);

        MHorizontalLayout controls = new MHorizontalLayout().with(saveBtn, cancelBtn).withMargin(
                new MarginInfo(false, true, false, false));
        layout.with(gridFormLayoutHelper.getLayout(), controls).withAlign(controls, Alignment.BOTTOM_RIGHT);
    }
}
