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

import com.mycollab.common.i18n.SecurityI18nEnum;
import com.mycollab.core.MyCollabException;
import com.mycollab.security.AccessPermissionFlag;
import com.mycollab.security.BooleanPermissionFlag;
import com.mycollab.vaadin.UserUIContext;
import com.vaadin.data.Converter;
import com.vaadin.data.Result;
import com.vaadin.data.ValueContext;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.ItemCaptionGenerator;

import java.util.Arrays;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
public class KeyCaptionComboBox extends ComboBox<KeyCaptionComboBox.Entry> implements Converter<KeyCaptionComboBox.Entry, Integer> {
    private static final long serialVersionUID = 1L;

    private Entry[] entries;

    public KeyCaptionComboBox(boolean nullSelectionAllowed, Entry... entries) {
        this.setEmptySelectionAllowed(nullSelectionAllowed);
        this.entries = entries;
        this.setItems(entries);
        this.setItemCaptionGenerator((ItemCaptionGenerator<Entry>) entry -> UserUIContext.getMessage(entry.caption));
        this.setWidth(WebThemes.FORM_CONTROL_WIDTH);
    }

    @Override
    public Result<Integer> convertToModel(Entry entry, ValueContext valueContext) {
        return Result.ok(entry.key);
    }

    @Override
    public Entry convertToPresentation(Integer key, ValueContext valueContext) {
        return Arrays.stream(entries).filter(entry -> entry.key == key).findFirst().get();
    }

    public enum Entry {
        TRUE(BooleanPermissionFlag.TRUE, SecurityI18nEnum.YES),
        FALSE(BooleanPermissionFlag.FALSE, SecurityI18nEnum.NO),
        NO_ACCESS(AccessPermissionFlag.NO_ACCESS, SecurityI18nEnum.NO_ACCESS),
        READ_ONLY(AccessPermissionFlag.READ_ONLY, SecurityI18nEnum.READONLY),
        READ_WRITE(AccessPermissionFlag.READ_WRITE, SecurityI18nEnum.READ_WRITE),
        ACCESS(AccessPermissionFlag.ACCESS, SecurityI18nEnum.ACCESS);

        private Integer key;

        private Enum<?> caption;

        Entry(Integer key, Enum<?> caption) {
            this.key = key;
            this.caption = caption;
        }

        public Integer getKey() {
            return key;
        }

        public static Entry of(Integer key) {
            switch (key) {
                case BooleanPermissionFlag.TRUE:
                    return TRUE;
                case BooleanPermissionFlag.FALSE:
                    return FALSE;
                case AccessPermissionFlag.NO_ACCESS:
                    return NO_ACCESS;
                case AccessPermissionFlag.READ_ONLY:
                    return READ_ONLY;
                case AccessPermissionFlag.READ_WRITE:
                    return READ_WRITE;
                case AccessPermissionFlag.ACCESS:
                    return ACCESS;
                default:
                    throw new MyCollabException("Not support entry value " + key);
            }
        }
    }
}
