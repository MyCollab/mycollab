/**
 * Copyright © MyCollab
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.vaadin.web.ui;

import com.vaadin.data.Converter;
import com.vaadin.data.Result;
import com.vaadin.data.ValueContext;
import com.vaadin.ui.ComboBox;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
// TODO
public class KeyCaptionComboBox extends ComboBox implements Converter<String, String> {
    private static final long serialVersionUID = 1L;

    private Entry[] entries;

    public KeyCaptionComboBox(boolean nullSelectionAllowed, Entry... entries) {
        this.setEmptySelectionAllowed(nullSelectionAllowed);
        this.entries = entries;
        this.setItems(entries);
//        this.setItemCaptionGenerator((ItemCaptionGenerator<Entry>) entry -> UserUIContext.getMessage(entry.caption));
    }

    @Override
    public Result<String> convertToModel(String s, ValueContext valueContext) {
        return Result.ok("A");
    }

    @Override
    public String convertToPresentation(String s, ValueContext valueContext) {
        return "V";
    }

    public static class Entry {
        private Object key;

        private Enum<?> caption;

        public Entry(Object key, Enum<?> caption) {
            this.key = key;
            this.caption = caption;
        }
    }
}
