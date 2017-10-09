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

    private RelatedListComp2 relatedListComp;
    private Set selectedItems = new HashSet();

    protected MVerticalLayout bodyContent;
    protected IPagedBeanTable<S, T> tableItem;

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
