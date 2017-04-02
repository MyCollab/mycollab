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

import com.mycollab.core.arguments.ValuedBean;
import com.mycollab.db.arguments.SearchCriteria;
import com.mycollab.db.persistence.service.ISearchableService;
import com.mycollab.vaadin.events.PageableHandler;
import com.mycollab.vaadin.events.SelectionOptionHandler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @param <V>
 * @param <S>
 * @param <B>
 * @author MyCollab Ltd.
 * @since 1.0
 */
public abstract class ListSelectionPresenter<V extends IListView<S, B>, S extends SearchCriteria, B extends ValuedBean>
        extends AbstractPresenter<V> {
    private static final long serialVersionUID = 1L;

    protected boolean isSelectAll = false;
    protected S searchCriteria;

    public ListSelectionPresenter(Class<V> viewClass) {
        super(viewClass);
    }

    @Override
    protected void viewAttached() {
        if (view.getSearchHandlers() != null) {
            view.getSearchHandlers().addSearchHandler(this::doSearch);
        }

        if (view.getPagedBeanTable() != null) {
            view.getPagedBeanTable().addPageableHandler(new PageableHandler() {
                private static final long serialVersionUID = 1L;

                @Override
                public void move(int newPageNumber) {
                    pageChange();
                }

                private void pageChange() {
                    if (isSelectAll) {
                        selectAllItemsInCurrentPage();
                    }
                    checkWhetherEnableTableActionControl();
                }
            });
        }

        if (view.getOptionSelectionHandlers() != null) {
            view.getOptionSelectionHandlers().addSelectionOptionHandler(new SelectionOptionHandler() {
                private static final long serialVersionUID = 1L;

                @Override
                public void onSelectCurrentPage() {
                    isSelectAll = false;
                    selectAllItemsInCurrentPage();
                    checkWhetherEnableTableActionControl();
                }

                @Override
                public void onDeSelect() {
                    Collection<B> currentDataList = view.getPagedBeanTable().getCurrentDataList();
                    isSelectAll = false;
                    currentDataList.forEach(item -> {
                        item.setSelected(false);
                        CheckBoxDecor checkBox = (CheckBoxDecor) item.getExtraData();
                        checkBox.setValueWithoutNotifyListeners(false);
                    });
                    checkWhetherEnableTableActionControl();
                }

                @Override
                public void onSelectAll() {
                    isSelectAll = true;
                    selectAllItemsInCurrentPage();
                    checkWhetherEnableTableActionControl();
                }
            });
        }

        if (view.getSelectableItemHandlers() != null) {
            view.getSelectableItemHandlers().addSelectableItemHandler(item -> {
                isSelectAll = false;
                item.setSelected(!item.isSelected());
                checkWhetherEnableTableActionControl();
            });
        }
    }

    private void selectAllItemsInCurrentPage() {
        Collection<B> currentDataList = view.getPagedBeanTable().getCurrentDataList();
        currentDataList.forEach(item -> {
            item.setSelected(true);
            CheckBoxDecor checkBox = (CheckBoxDecor) item.getExtraData();
            checkBox.setValueWithoutNotifyListeners(true);
        });
    }

    public void doSearch(S searchCriteria) {
        this.searchCriteria = searchCriteria;
        int totalCountItems = view.getPagedBeanTable().setSearchCriteria(searchCriteria);
        checkWhetherEnableTableActionControl();
        view.getSearchHandlers().setTotalCountNumber(totalCountItems);
    }

    protected void checkWhetherEnableTableActionControl() {
        Collection<B> currentDataList = view.getPagedBeanTable().getCurrentDataList();
        int countItems = 0;
        for (B item : currentDataList) {
            if (item.isSelected()) {
                countItems++;
            }
        }
        if (countItems > 0) {
            view.enableActionControls(countItems);
        } else {
            view.disableActionControls();
        }
    }

    List<B> getSelectedItems() {
        List<B> items = new ArrayList<>();
        Collection<B> currentDataList = view.getPagedBeanTable().getCurrentDataList();
        items.addAll(currentDataList.stream().filter(ValuedBean::isSelected).collect(Collectors.toList()));
        return items;
    }

    abstract public ISearchableService<S> getSearchService();

    abstract protected void deleteSelectedItems();
}
