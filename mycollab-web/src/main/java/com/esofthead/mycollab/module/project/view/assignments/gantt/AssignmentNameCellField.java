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
package com.esofthead.mycollab.module.project.view.assignments.gantt;

import com.esofthead.mycollab.module.project.ui.ProjectAssetsManager;
import com.vaadin.data.Property;
import com.vaadin.data.Validator;
import com.vaadin.event.FieldEvents;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import org.vaadin.viritin.layouts.MHorizontalLayout;

/**
 * @author MyCollab Ltd
 * @since 5.2.7
 */
public class AssignmentNameCellField extends CustomField<String> implements FieldEvents.BlurNotifier {
    private TextField field = new TextField();
    private String type;

    public AssignmentNameCellField(String type) {
        this.type = type;
    }

    @Override
    public void addBlurListener(FieldEvents.BlurListener blurListener) {
        field.addBlurListener(blurListener);
    }

    @Override
    public void addListener(FieldEvents.BlurListener blurListener) {

    }

    @Override
    public void removeBlurListener(FieldEvents.BlurListener blurListener) {
        field.removeBlurListener(blurListener);
    }

    @Override
    public void removeListener(FieldEvents.BlurListener blurListener) {

    }

    @Override
    public void commit() throws SourceException, Validator.InvalidValueException {
        String value = field.getValue();
        this.setInternalValue(value);
        super.commit();
    }

    @Override
    public void setPropertyDataSource(Property newDataSource) {
        if (newDataSource != null) {
            String value = (String) newDataSource.getValue();
            if (value != null) {
                if (field.isReadOnly()) {
                    field.setReadOnly(false);
                    field.setValue(value);
                    field.setReadOnly(true);
                } else {
                    field.setValue(value);
                }
            }
        }

        super.setPropertyDataSource(newDataSource);
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        field.setReadOnly(readOnly);
    }

    public void setDescription(String description) {
        field.setDescription(description);
    }

    @Override
    public String getValue() {
        return field.getValue();
    }

    @Override
    protected Component initContent() {
        MHorizontalLayout layout = new MHorizontalLayout().withWidth("100%");
        layout.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        field.setImmediate(true);
        field.setBuffered(true);
        field.setWidth("100%");
        Label icon = new Label(ProjectAssetsManager.getAsset(type).getHtml(), ContentMode.HTML);
        layout.with(new CssLayout(icon), field).expand(field);
        return layout;
    }

    @Override
    public Class<? extends String> getType() {
        return String.class;
    }
}
