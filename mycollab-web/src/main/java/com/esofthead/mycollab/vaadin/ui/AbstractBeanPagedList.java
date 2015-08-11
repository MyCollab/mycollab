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
package com.esofthead.mycollab.vaadin.ui;

import com.esofthead.mycollab.core.arguments.SearchCriteria;
import com.esofthead.mycollab.core.arguments.SearchRequest;
import com.esofthead.mycollab.vaadin.events.HasPagableHandlers;
import com.esofthead.mycollab.vaadin.events.PagableHandler;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import org.vaadin.maddon.layouts.MHorizontalLayout;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
public abstract class AbstractBeanPagedList<S extends SearchCriteria, T> extends VerticalLayout implements HasPagableHandlers {
    private static final long serialVersionUID = 1L;

    private int defaultNumberSearchItems = 10;
    private Set<PagableHandler> pagableHandlers;
    private String listControlStyle = "listControl";

    protected CssLayout listContainer;
    protected RowDisplayHandler<T> rowDisplayHandler;
    protected int currentPage = 1;
    protected int totalPage = 1;
    protected int totalCount;
    protected List<T> currentListData;
    protected CssLayout controlBarWrapper;
    protected MHorizontalLayout pageManagement;
    protected SearchRequest<S> searchRequest;

    public AbstractBeanPagedList(RowDisplayHandler<T> rowDisplayHandler,
                                 int defaultNumberSearchItems) {
        this.defaultNumberSearchItems = defaultNumberSearchItems;
        this.rowDisplayHandler = rowDisplayHandler;
        listContainer = new CssLayout();
        listContainer.setWidth("100%");
        this.addComponent(listContainer);
    }

    @Override
    public void addPagableHandler(PagableHandler handler) {
        if (pagableHandlers == null) {
            pagableHandlers = new HashSet<>();
        }
        pagableHandlers.add(handler);
    }

    public void setControlStyle(String style) {
        this.listControlStyle = style;
    }

    protected CssLayout createPageControls() {
        this.controlBarWrapper = new CssLayout();
        this.controlBarWrapper.setStyleName(listControlStyle);
        this.controlBarWrapper.setWidth("100%");

        HorizontalLayout controlBar = new HorizontalLayout();
        controlBar.setWidth("100%");
        this.controlBarWrapper.addComponent(controlBar);

        this.pageManagement = new MHorizontalLayout();
        this.pageManagement.setWidth(null);

        // defined layout here ---------------------------

        if (this.currentPage > 1) {
            Button firstLink = new ButtonLink("1", new Button.ClickListener() {
                private static final long serialVersionUID = 1L;

                @Override
                public void buttonClick(final ClickEvent event) {
                    pageChange(1);
                }
            }, false);
            firstLink.addStyleName("buttonPaging");
            this.pageManagement.addComponent(firstLink);
        }
        if (this.currentPage >= 5) {
            final Label ss1 = new Label("...");
            ss1.addStyleName("buttonPaging");
            this.pageManagement.addComponent(ss1);
        }
        if (this.currentPage > 3) {
            Button previous2 = new ButtonLink("" + (this.currentPage - 2), new ClickListener() {
                private static final long serialVersionUID = 1L;

                @Override
                public void buttonClick(final ClickEvent event) {
                    pageChange(currentPage - 2);
                }
            }, false);
            previous2.addStyleName("buttonPaging");
            this.pageManagement.addComponent(previous2);
        }
        if (this.currentPage > 2) {
            final Button previous1 = new ButtonLink("" + (this.currentPage - 1), new ClickListener() {
                private static final long serialVersionUID = 1L;

                @Override
                public void buttonClick(final ClickEvent event) {
                    pageChange(currentPage - 1);
                }
            }, false);
            previous1.addStyleName("buttonPaging");
            this.pageManagement.addComponent(previous1);
        }
        // Here add current ButtonLinkLegacy
        final Button current = new ButtonLink("" + this.currentPage,
                new ClickListener() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void buttonClick(final ClickEvent event) {
                        pageChange(currentPage);
                    }
                }, false);
        current.addStyleName("buttonPaging");
        current.addStyleName("current");

        this.pageManagement.addComponent(current);
        final int range = this.totalPage - this.currentPage;
        if (range >= 1) {
            final Button next1 = new ButtonLink("" + (this.currentPage + 1),
                    new ClickListener() {
                        private static final long serialVersionUID = 1L;

                        @Override
                        public void buttonClick(final ClickEvent event) {
                            pageChange(currentPage + 1);
                        }
                    }, false);
            next1.addStyleName("buttonPaging");
            this.pageManagement.addComponent(next1);
        }
        if (range >= 2) {
            Button next2 = new ButtonLink("" + (this.currentPage + 2), new ClickListener() {
                private static final long serialVersionUID = 1L;

                @Override
                public void buttonClick(final ClickEvent event) {
                    pageChange(currentPage + 2);
                }
            }, false);
            next2.addStyleName("buttonPaging");
            this.pageManagement.addComponent(next2);
        }
        if (range >= 4) {
            Label ss2 = new Label("...");
            ss2.addStyleName("buttonPaging");
            this.pageManagement.addComponent(ss2);
        }
        if (range >= 3) {
            Button last = new ButtonLink("" + this.totalPage, new ClickListener() {
                private static final long serialVersionUID = 1L;

                @Override
                public void buttonClick(final ClickEvent event) {
                    pageChange(totalPage);
                }
            }, false);
            last.addStyleName("buttonPaging");
            this.pageManagement.addComponent(last);
        }

        controlBar.addComponent(this.pageManagement);
        controlBar.setComponentAlignment(this.pageManagement, Alignment.MIDDLE_RIGHT);

        return this.controlBarWrapper;
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
            listContainer.addComponent(msgWhenEmptyList());
        }
    }

    protected String stringWhenEmptyList() {
        return null;
    }

    private Component msgWhenEmptyList() {
        HorizontalLayout comp = new HorizontalLayout();
        comp.setMargin(true);
        comp.addComponent(new Label(stringWhenEmptyList()));
        return comp;
    }

    abstract protected int queryTotalCount();

    abstract protected List<T> queryCurrentData();

    public int getTotalCount() {
        return totalCount;
    }

    protected void doSearch() {
        totalCount = queryTotalCount();
        totalPage = (totalCount - 1) / searchRequest.getNumberOfItems() + 1;
        if (searchRequest.getCurrentPage() > totalPage) {
            searchRequest.setCurrentPage(totalPage);
        }

        if (totalPage > 1) {
            if (this.controlBarWrapper != null) {
                this.removeComponent(this.controlBarWrapper);
            }
            this.addComponent(this.createPageControls());
        } else {
            if (getComponentCount() == 2) {
                removeComponent(getComponent(1));
            }
        }

        currentListData = queryCurrentData();
        listContainer.removeAllComponents();

        if (currentListData.size() != 0) {
            int i = 0;
            for (T item : currentListData) {
                Component row = rowDisplayHandler.generateRow(this, item, i);
                listContainer.addComponent(row);
                i++;
            }
        } else {
            listContainer.addComponent(msgWhenEmptyList());
        }
    }

    protected void pageChange(int currentPage) {
        if (searchRequest != null) {
            this.currentPage = currentPage;
            searchRequest.setCurrentPage(currentPage);
            doSearch();

            if (pagableHandlers != null) {
                for (PagableHandler handler : pagableHandlers) {
                    handler.move(currentPage);
                }
            }
        }
    }

    public int setSearchCriteria(final S searchCriteria) {
        listContainer.removeAllComponents();
        searchRequest = new SearchRequest<>(searchCriteria, currentPage, defaultNumberSearchItems);
        doSearch();
        return totalCount;
    }

    public interface RowDisplayHandler<T> {
        Component generateRow(AbstractBeanPagedList host, T item, int rowIndex);
    }
}
