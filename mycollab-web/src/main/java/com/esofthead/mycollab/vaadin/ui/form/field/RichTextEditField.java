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
package com.esofthead.mycollab.vaadin.ui.form.field;

import com.esofthead.mycollab.core.utils.StringUtils;
import com.vaadin.data.Property;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.event.FieldEvents;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import org.vaadin.openesignforms.ckeditor.CKEditorConfig;
import org.vaadin.openesignforms.ckeditor.CKEditorTextField;

/**
 * @author MyCollab Ltd.
 * @since 4.5.3
 */
public class RichTextEditField extends CustomField<String> implements FieldEvents.BlurNotifier {
    private static final long serialVersionUID = 1L;

    private CKEditorTextField textArea;

    public RichTextEditField() {
        CKEditorConfig config = new CKEditorConfig();

        config.addWriterRules("p", "{indent : false, breakBeforeOpen : true, breakAfterOpen : false, breakBeforeClose : false, breakAfterClose : true}");
        config.setDisableNativeSpellChecker(true);
        config.setResizeDir(CKEditorConfig.RESIZE_DIR.HORIZONTAL);
        config.disableSpellChecker();
        config.disableResizeEditor();
        config.disableElementsPath();
        config.setToolbarCanCollapse(true);
        config.setWidth("100%");
        textArea = new CKEditorTextField(config);
    }

    @Override
    protected Component initContent() {
        textArea.setWidth("100%");
        return textArea;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void setPropertyDataSource(Property newDataSource) {
        String value = (String) newDataSource.getValue();
        if (value != null) {
            textArea.setValue(value);
        } else {
            textArea.setValue("");
        }
        super.setPropertyDataSource(newDataSource);
    }

    @Override
    public String getValue() {
        return textArea.getValue();
    }

    @Override
    public void commit() throws SourceException, InvalidValueException {
        String value = textArea.getValue();
        value = StringUtils.formatRichText(value);
        this.setInternalValue(value);
        super.commit();
    }

    @Override
    public Class<? extends String> getType() {
        return String.class;
    }

    @Override
    public void addBlurListener(FieldEvents.BlurListener listener) {
        textArea.addBlurListener(listener);
    }

    @Override
    public void addListener(FieldEvents.BlurListener listener) {
        textArea.addListener(listener);
    }

    @Override
    public void removeBlurListener(FieldEvents.BlurListener listener) {
        textArea.removeBlurListener(listener);
    }

    @Override
    public void removeListener(FieldEvents.BlurListener listener) {
        textArea.removeListener(listener);
    }
}
