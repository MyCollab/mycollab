/**
 * Copyright © MyCollab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.vaadin.web.ui;

import com.mycollab.core.utils.StringUtils;
import com.mycollab.db.arguments.SearchRequest;
import com.mycollab.vaadin.event.HasPageableHandlers;
import com.mycollab.vaadin.event.PageableHandler;
import com.mycollab.vaadin.ui.IBeanList;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
public abstract class AbstractBeanPagedList<T> extends VerticalLayout implements HasPageableHandlers, IBeanList<T> {
    private static final long serialVersionUID = 1L;

    protected int defaultNumberSearchItems = 10;
    private Set<PageableHandler> pageableHandlers;
    private String[] listControlStyle = {"listControl"};

    protected CssLayout listContainer;
    private IBeanList.RowDisplayHandler<T> rowDisplayHandler;
    protected int currentPage = 1;
    protected int totalPage = 1;
    protected int totalCount;
    private List<T> currentListData;
    protected MHorizontalLayout controlBarWrapper;
    private MHorizontalLayout pageManagement;
    private QueryHandler<T> queryHandler;
    protected SearchRequest searchRequest;

    public AbstractBeanPagedList(IBeanList.RowDisplayHandler<T> rowDisplayHandler, int defaultNumberSearchItems) {
        this.defaultNumberSearchItems = defaultNumberSearchItems;
        this.rowDisplayHandler = rowDisplayHandler;
        listContainer = new CssLayout();
        listContainer.setWidth("100%");
        this.addComponent(listContainer);
        this.setExpandRatio(listContainer, 1.0f);
        this.addStyleName(WebThemes.SCROLLABLE_CONTAINER);
        queryHandler = buildQueryHandler();
    }

    @Override
    public void addPageableHandler(PageableHandler handler) {
        if (pageableHandlers == null) {
            pageableHandlers = new HashSet<>();
        }
        pageableHandlers.add(handler);
    }

    public void setControlStyle(String... style) {
        this.listControlStyle = style;
    }

    protected MHorizontalLayout createPageControls() {
        controlBarWrapper = new MHorizontalLayout().withFullWidth().withMargin(new MarginInfo(false, true, false, true))
                .withStyleName(listControlStyle);

        pageManagement = new MHorizontalLayout();

        // defined layout here ---------------------------

        if (currentPage > 1) {
            MButton firstLink = new MButton("1", clickEvent -> pageChange(1)).withStyleName("buttonPaging");
            pageManagement.addComponent(firstLink);
        }
        if (currentPage >= 5) {
            final Label ss1 = new Label("...");
            ss1.addStyleName("buttonPaging");
            pageManagement.addComponent(ss1);
        }
        if (currentPage > 3) {
            MButton previous2 = new MButton("" + (currentPage - 2), clickEvent -> pageChange(currentPage - 2))
                    .withStyleName("buttonPaging");
            pageManagement.addComponent(previous2);
        }
        if (currentPage > 2) {
            MButton previous1 = new MButton("" + (currentPage - 1), clickEvent -> pageChange(currentPage - 1))
                    .withStyleName("buttonPaging");
            pageManagement.addComponent(previous1);
        }
        // Here add current ButtonLinkLegacy
        MButton current = new MButton("" + currentPage, clickEvent -> pageChange(currentPage)).withStyleName("buttonPaging", "current");

        pageManagement.addComponent(current);
        final int range = this.totalPage - currentPage;
        if (range >= 1) {
            MButton next1 = new MButton("" + (currentPage + 1), clickEvent -> pageChange(currentPage + 1)).withStyleName("buttonPaging");
            pageManagement.addComponent(next1);
        }
        if (range >= 2) {
            MButton next2 = new MButton("" + (currentPage + 2), clickEvent -> pageChange(currentPage + 2)).withStyleName("buttonPaging");
            pageManagement.addComponent(next2);
        }
        if (range >= 4) {
            Label ss2 = new Label("...");
            ss2.addStyleName("buttonPaging");
            pageManagement.addComponent(ss2);
        }
        if (range >= 3) {
            MButton last = new MButton("" + this.totalPage, clickEvent -> pageChange(totalPage)).withStyleName("buttonPaging");
            pageManagement.addComponent(last);
        }

        controlBarWrapper.with(pageManagement).withAlign(pageManagement, Alignment.MIDDLE_RIGHT);
        return controlBarWrapper;
    }

    public void setCurrentDataList(List<T> list) {
        this.currentListData = list;
        listContainer.removeAllComponents();

        if (currentListData.size() != 0) {
            int i = 0;
            for (T item : currentListData) {
                Component row = rowDisplayHandler.generateRow(this, item, i);
                listContainer.addComponent(row);
                i++;
            }
        } else {
            String msg = stringWhenEmptyList();
            if (msg != null) {
                listContainer.addComponent(msgWhenEmptyList());
            }
        }
    }

    abstract protected QueryHandler<T> buildQueryHandler();

    protected String stringWhenEmptyList() {
        return null;
    }

    private Component msgWhenEmptyList() {
        String value = stringWhenEmptyList();
        if (StringUtils.isNotBlank(value)) {
            return new MHorizontalLayout().withStyleName("panel-body").withMargin(true).withFullWidth().with(new Label(value));
        } else {
            return null;
        }
    }

    public Component insertRowAt(T item, int index) {
        Component row = rowDisplayHandler.generateRow(this, item, index);
        listContainer.addComponent(row, index);
        return row;
    }

    public T getItemAt(int index) {
        if (currentListData != null) {
            try {
                return currentListData.get(index);
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    public Component getRowAt(int index) {
        try {
            return listContainer.getComponent(index);
        } catch (Exception e) {
            return null;
        }
    }

    public Integer getRowCount() {
        return currentListData.size();
    }

    public int getTotalCount() {
        return totalCount;
    }

    protected void doSearch() {
        if (searchRequest == null) {
            searchRequest = new SearchRequest(0, defaultNumberSearchItems);
        }
        totalCount = queryHandler.queryTotalCount();
        totalPage = (totalCount - 1) / searchRequest.getNumberOfItems() + 1;
        if (searchRequest.getCurrentPage() > totalPage) {
            searchRequest.setCurrentPage(totalPage);
        }

        if (totalPage > 1) {
            if (controlBarWrapper != null) {
                this.removeComponent(controlBarWrapper);
            }
            this.addComponent(this.createPageControls());
        } else {
            if (getComponentCount() == 2) {
                removeComponent(getComponent(1));
            }
        }

        currentListData = queryHandler.queryCurrentData();
        listContainer.removeAllComponents();

        if (currentListData.size() != 0) {
            int i = 0;
            for (T item : currentListData) {
                Component row = rowDisplayHandler.generateRow(this, item, i);
                listContainer.addComponent(row);
                i++;
            }
        } else {
            Component component = msgWhenEmptyList();
            if (component != null) {
                listContainer.addComponent(component);
            }
        }
    }

    protected void pageChange(int currentPage) {
        if (searchRequest != null) {
            this.currentPage = currentPage;
            searchRequest.setCurrentPage(currentPage);
            doSearch();

            if (pageableHandlers != null) {
                for (PageableHandler handler : pageableHandlers) {
                    handler.move(currentPage);
                }
            }
        }
    }

    public void removeRow(Component row) {
        listContainer.removeComponent(row);
    }

    public void setSelectedRow(Component row) {
        for (int i = 0; i < listContainer.getComponentCount(); i++) {
            listContainer.getComponent(i).removeStyleName("selected");
        }
        row.addStyleName("selected");
    }

    public interface QueryHandler<T> {
        int queryTotalCount();

        List<T> queryCurrentData();
    }
}
