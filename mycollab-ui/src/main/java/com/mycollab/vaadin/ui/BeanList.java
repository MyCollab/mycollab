/**
 * This file is part of mycollab-ui.
 *
 * mycollab-ui is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-ui is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-ui.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.vaadin.ui;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.db.arguments.BasicSearchRequest;
import com.mycollab.db.arguments.SearchCriteria;
import com.mycollab.db.persistence.service.ISearchableService;
import com.mycollab.vaadin.UserUIContext;
import com.vaadin.ui.*;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.util.List;

/**
 * Generic list item
 *
 * @param <SearchService> search service generic interface
 * @param <S>             search criteria
 * @param <T>             bean item
 * @author MyCollab Ltd.
 * @since 2.0
 */
public class BeanList<SearchService extends ISearchableService<S>, S extends SearchCriteria, T> extends CustomComponent
        implements IBeanList<T> {
    private static final long serialVersionUID = 1L;
    private static final Logger LOG = LoggerFactory.getLogger(BeanList.class);

    private SearchService searchService;
    private IBeanList.RowDisplayHandler<T> rowDisplayHandler;
    private Layout contentLayout;
    private boolean isDisplayEmptyListText = true;

    public BeanList(SearchService searchService, IBeanList.RowDisplayHandler<T> rowDisplayHandler) {
        this.searchService = searchService;
        this.rowDisplayHandler = rowDisplayHandler;

        this.contentLayout = new CssLayout();
        this.contentLayout.setWidth("100%");
        this.setCompositionRoot(this.contentLayout);
        this.setStyleName("bean-list");
    }

    public Layout getContentLayout() {
        return this.contentLayout;
    }

    public void setDisplayEmptyListText(boolean isDisplayEmptyListText) {
        this.isDisplayEmptyListText = isDisplayEmptyListText;
    }

    public void removeRow(Component row) {
        contentLayout.removeComponent(row);
    }

    public int setSearchCriteria(S searchCriteria) {
        BasicSearchRequest<S> searchRequest = new BasicSearchRequest<>(searchCriteria, 0, Integer.MAX_VALUE);
        return setSearchRequest(searchRequest);
    }

    public int setSearchRequest(BasicSearchRequest<S> searchRequest) {
        List<T> currentListData = searchService.findPageableListByCriteria(searchRequest);
        loadItems(currentListData);
        return currentListData.size();
    }

    public int getTotalCount(S searchCriteria) {
        return searchService.getTotalCount(searchCriteria);
    }

    public void loadItems(List<T> currentListData) {
        contentLayout.removeAllComponents();

        try {
            if (CollectionUtils.isEmpty(currentListData) && isDisplayEmptyListText) {
                Label noItemLbl = new Label(UserUIContext.getMessage(GenericI18Enum.EXT_NO_ITEM));
                MVerticalLayout widgetFooter = new MVerticalLayout().withFullWidth();
                widgetFooter.addStyleName("widget-footer");
                widgetFooter.with(noItemLbl).withAlign(noItemLbl, Alignment.MIDDLE_CENTER);
                contentLayout.addComponent(widgetFooter);
            } else {
                int i = 0;
                for (T item : currentListData) {
                    Component row = rowDisplayHandler.generateRow(this, item, i);
                    if (row != null) {
                        row.setWidth("100%");
                        contentLayout.addComponent(row);
                    }
                    i++;
                }
            }

        } catch (Exception e) {
            LOG.error("Error while generate column display", e);
        }
    }
}
