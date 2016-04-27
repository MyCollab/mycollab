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
package com.esofthead.mycollab.vaadin.web.ui;

import com.esofthead.mycollab.core.arguments.SearchCriteria;
import com.esofthead.mycollab.core.arguments.BasicSearchRequest;
import com.esofthead.mycollab.vaadin.events.HasPagableHandlers;
import com.esofthead.mycollab.vaadin.events.PageableHandler;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @param <S>
 * @param <T>
 * @author MyCollab Ltd.
 * @since 4.0
 */
public abstract class AbstractBeanBlockList<S extends SearchCriteria, T> extends VerticalLayout implements HasPagableHandlers {
    private static final long serialVersionUID = -1842929843421392806L;

    private int defaultNumberSearchItems = 10;
    protected final CssLayout itemContainer;
    protected BlockDisplayHandler<T> blockDisplayHandler;
    protected int currentPage = 1;
    protected int totalPage = 1;
    protected int totalCount;
    protected List<T> currentListData;
    protected CssLayout controlBarWrapper;
    protected MHorizontalLayout pageManagement;

    private Set<PageableHandler> pageableHandlers;

    protected BasicSearchRequest<S> searchRequest;

    public static final String[] COLOR_STYLENAME_LIST = new String[]{"red-block",
            "cyan-block", "blue-block", "lightblue-block", "purple-block",
            "yellow-block", "lime-block", "magenta-block", "silver-block",
            "gray-block", "orange-block", "brown-block", "maroon-block",
            "green-block", "olive-block"};

    public AbstractBeanBlockList(BlockDisplayHandler<T> blockDisplayHandler,
                                 int defaultNumberSearchItems) {
        this(defaultNumberSearchItems);
        this.setBlockDisplayHandler(blockDisplayHandler);
    }

    public AbstractBeanBlockList(final int defaultNumberSearchItems) {
        this.defaultNumberSearchItems = defaultNumberSearchItems;
        this.setSpacing(true);
        itemContainer = new CssLayout();

        Component topControls = generateTopControls();
        if (topControls != null) {
            this.addComponent(topControls);
        }
        this.addComponent(itemContainer);
    }

    @Override
    public void addPageableHandler(final PageableHandler handler) {
        if (pageableHandlers == null) {
            pageableHandlers = new HashSet<>();
        }
        pageableHandlers.add(handler);
    }

    protected CssLayout createPageControls() {
        this.controlBarWrapper = new CssLayout();
        this.controlBarWrapper.setWidth("100%");

        final HorizontalLayout controlBar = new HorizontalLayout();
        controlBar.setWidth("100%");
        this.controlBarWrapper.addComponent(controlBar);

        pageManagement = new MHorizontalLayout();

        // defined layout here ---------------------------

        if (currentPage > 1) {
            final Button firstLink = new Button("1", new Button.ClickListener() {
                private static final long serialVersionUID = 1L;

                @Override
                public void buttonClick(final ClickEvent event) {
                    AbstractBeanBlockList.this.pageChange(1);
                }
            });
            firstLink.addStyleName("buttonPaging");
            pageManagement.addComponent(firstLink);
        }
        if (currentPage >= 5) {
            final Label ss1 = new Label("...");
            ss1.addStyleName("buttonPaging");
            pageManagement.addComponent(ss1);
        }
        if (currentPage > 3) {
            final Button previous2 = new Button("" + (currentPage - 2), new ClickListener() {
                private static final long serialVersionUID = 1L;

                @Override
                public void buttonClick(final ClickEvent event) {
                    pageChange(currentPage - 2);
                }
            });
            previous2.addStyleName("buttonPaging");
            pageManagement.addComponent(previous2);
        }
        if (currentPage > 2) {
            final Button previous1 = new Button("" + (currentPage - 1), new ClickListener() {
                private static final long serialVersionUID = 1L;

                @Override
                public void buttonClick(final ClickEvent event) {
                    AbstractBeanBlockList.this.pageChange(currentPage - 1);
                }
            });
            previous1.addStyleName("buttonPaging");
            pageManagement.addComponent(previous1);
        }
        // Here add current ButtonLinkLegacy
        final Button current = new Button("" + currentPage, new ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(final ClickEvent event) {
                AbstractBeanBlockList.this.pageChange(currentPage);
            }
        });
        current.addStyleName("buttonPaging");
        current.addStyleName("current");

        pageManagement.addComponent(current);
        final int range = totalPage - currentPage;
        if (range >= 1) {
            final Button next1 = new Button("" + (currentPage + 1), new ClickListener() {
                private static final long serialVersionUID = 1L;

                @Override
                public void buttonClick(final ClickEvent event) {
                    AbstractBeanBlockList.this.pageChange(currentPage + 1);
                }
            });
            next1.addStyleName("buttonPaging");
            pageManagement.addComponent(next1);
        }
        if (range >= 2) {
            final Button next2 = new Button("" + (currentPage + 2), new ClickListener() {
                private static final long serialVersionUID = 1L;

                @Override
                public void buttonClick(final ClickEvent event) {
                    AbstractBeanBlockList.this.pageChange(currentPage + 2);
                }
            });
            next2.addStyleName("buttonPaging");
            pageManagement.addComponent(next2);
        }
        if (range >= 4) {
            final Label ss2 = new Label("...");
            ss2.addStyleName("buttonPaging");
            pageManagement.addComponent(ss2);
        }
        if (range >= 3) {
            final Button last = new Button("" + totalPage, new ClickListener() {
                private static final long serialVersionUID = 1L;

                @Override
                public void buttonClick(final ClickEvent event) {
                    pageChange(totalPage);
                }
            });
            last.addStyleName("buttonPaging");
            pageManagement.addComponent(last);
        }

        pageManagement.setWidth(null);
        controlBar.addComponent(pageManagement);
        controlBar.setComponentAlignment(pageManagement, Alignment.MIDDLE_RIGHT);

        return this.controlBarWrapper;
    }

    abstract protected int queryTotalCount();

    abstract protected List<T> queryCurrentData();

    protected void doSearch() {
        totalCount = queryTotalCount();
        totalPage = (totalCount - 1) / searchRequest.getNumberOfItems() + 1;
        if (searchRequest.getCurrentPage() > totalPage) {
            searchRequest.setCurrentPage(totalPage);
        }

        if (this.controlBarWrapper != null
                && this.controlBarWrapper.getParent() == this) {
            this.removeComponent(this.controlBarWrapper);
        }

        if (totalPage > 1) {
            this.addComponent(this.createPageControls());
        }

        currentListData = queryCurrentData();

        itemContainer.removeAllComponents();

        int i = 0;
        for (final T item : currentListData) {
            final Component row = blockDisplayHandler.generateBlock(item, i);
            itemContainer.addComponent(row);
            i++;
        }
    }

    protected void pageChange(final int currentPage) {
        if (searchRequest != null) {
            this.currentPage = currentPage;
            searchRequest.setCurrentPage(currentPage);
            doSearch();

            if (pageableHandlers != null) {
                for (final PageableHandler handler : pageableHandlers) {
                    handler.move(currentPage);
                }
            }
        }
    }

    protected void setCurrentPage(final int currentPage) {
        this.currentPage = currentPage;
    }

    public void setSearchCriteria(final S searchCriteria) {
        searchRequest = new BasicSearchRequest<S>(searchCriteria, currentPage,
                defaultNumberSearchItems);
        doSearch();
    }

    protected void setTotalPage(final int totalPage) {
        this.totalPage = totalPage;
    }

    public BlockDisplayHandler<T> getRowDisplayHandler() {
        return blockDisplayHandler;
    }

    public void setBlockDisplayHandler(BlockDisplayHandler<T> handler) {
        this.blockDisplayHandler = handler;
    }

    abstract protected Component generateTopControls();

    public interface BlockDisplayHandler<T> {

        Component generateBlock(T obj, int blockIndex);

    }

}
