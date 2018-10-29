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
package com.mycollab.vaadin.web.ui.table;

import com.mycollab.common.GridFieldMeta;
import com.mycollab.db.arguments.SearchCriteria;
import com.mycollab.vaadin.event.ApplicationEvent;
import com.mycollab.vaadin.event.HasPageableHandlers;
import com.mycollab.vaadin.event.HasSelectableItemHandlers;
import com.vaadin.data.ValueProvider;
import com.vaadin.ui.Component;
import com.vaadin.ui.renderers.AbstractRenderer;
import com.vaadin.util.ReflectTools;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.EventListener;
import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
// TODO
public interface IPagedGrid<S extends SearchCriteria, B> extends HasSelectableItemHandlers<B>, HasPageableHandlers, Component {

    int setSearchCriteria(S searchCriteria);

    Collection<B> getCurrentDataList();

    @Deprecated
    void addTableListener(TableClickListener listener);

    <V> void addGeneratedColumn(ValueProvider<B, V> id, AbstractRenderer generatedColumn);

    List<GridFieldMeta> getDisplayColumns();

    B getBeanByIndex(Object itemId);

    @Deprecated
    interface TableClickListener extends EventListener, Serializable {
        Method itemClickMethod = ReflectTools.findMethod(TableClickListener.class, "itemClick", TableClickEvent.class);

        void itemClick(TableClickEvent event);
    }

    @Deprecated
    class TableClickEvent extends ApplicationEvent {
        public static final String TABLE_CLICK_IDENTIFIER = "tableClickEvent";

        private static final long serialVersionUID = 1L;
        private String fieldName;
        private Object data;

        public TableClickEvent(IPagedGrid source, Object data, String fieldName) {
            super(source);
            this.data = data;
            this.fieldName = fieldName;
        }

        public String getFieldName() {
            return fieldName;
        }

        public Object getData() {
            return data;
        }
    }
}
