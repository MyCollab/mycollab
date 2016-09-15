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
package com.mycollab.module.crm.ui.components;

import com.mycollab.core.MyCollabException;
import com.mycollab.db.arguments.SearchCriteria;
import com.mycollab.vaadin.web.ui.table.IPagedBeanTable;
import org.apache.commons.beanutils.PropertyUtils;
import org.vaadin.viritin.layouts.MVerticalLayout;
import org.vaadin.viritin.layouts.MWindow;

import java.util.HashSet;
import java.util.Set;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public abstract class RelatedItemSelectionWindow<T, S extends SearchCriteria> extends MWindow {
    private static final long serialVersionUID = 1L;
    private static final String selectedFieldName = "selected";

    protected RelatedListComp2 relatedListComp;
    protected IPagedBeanTable<S, T> tableItem;
    protected Set selectedItems = new HashSet();
    protected MVerticalLayout bodyContent;

    public RelatedItemSelectionWindow(String title, RelatedListComp2 relatedList) {
        super(title);
        bodyContent = new MVerticalLayout();
        this.withContent(bodyContent).withCenter().withModal(true).withResizable(false);
        this.relatedListComp = relatedList;
        initUI();

        tableItem.addTableListener(event -> {
            try {
                Object rowItem = event.getData();
                Boolean selectedVal = (Boolean) PropertyUtils.getProperty(rowItem, selectedFieldName);
                if (selectedVal) {
                    selectedItems.remove(rowItem);
                    PropertyUtils.setProperty(rowItem, selectedFieldName, false);
                } else {
                    selectedItems.add(rowItem);
                    PropertyUtils.setProperty(rowItem, selectedFieldName, true);
                }
            } catch (Exception ex) {
                throw new MyCollabException(ex);
            }
        });
    }

    @Override
    public void close() {
        super.close();
        if (!selectedItems.isEmpty()) {
            relatedListComp.fireSelectedRelatedItems(selectedItems);
        }
    }

    protected abstract void initUI();

    public void setSearchCriteria(S criteria) {
        tableItem.setSearchCriteria(criteria);
    }
}
