package com.mycollab.vaadin.web.ui.table;

import com.mycollab.common.TableViewField;
import com.mycollab.db.arguments.SearchCriteria;
import com.mycollab.vaadin.event.ApplicationEvent;
import com.mycollab.vaadin.event.HasPageableHandlers;
import com.mycollab.vaadin.event.HasSelectableItemHandlers;
import com.vaadin.ui.Component;
import com.vaadin.ui.Table;
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
public interface IPagedBeanTable<S extends SearchCriteria, T> extends HasSelectableItemHandlers<T>, HasPageableHandlers, Component {

    int setSearchCriteria(S searchCriteria);

    Collection<T> getCurrentDataList();

    void addTableListener(TableClickListener listener);

    void addGeneratedColumn(Object id, Table.ColumnGenerator generatedColumn);

    List<TableViewField> getDisplayColumns();

    T getBeanByIndex(Object itemId);

    void refresh();

    interface TableClickListener extends EventListener, Serializable {
        Method itemClickMethod = ReflectTools.findMethod(TableClickListener.class, "itemClick", TableClickEvent.class);

        void itemClick(TableClickEvent event);
    }

    class TableClickEvent extends ApplicationEvent {
        public static final String TABLE_CLICK_IDENTIFIER = "tableClickEvent";

        private static final long serialVersionUID = 1L;
        private String fieldName;
        private Object data;

        public TableClickEvent(IPagedBeanTable source, Object data, String fieldName) {
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
