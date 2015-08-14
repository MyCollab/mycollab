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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.esofthead.mycollab.core.arguments.SearchCriteria;
import com.esofthead.mycollab.core.arguments.SearchRequest;
import com.esofthead.mycollab.vaadin.events.HasPagableHandlers;
import com.esofthead.mycollab.vaadin.events.PagableHandler;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import org.vaadin.viritin.layouts.MHorizontalLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.0
 * 
 * @param <S>
 * @param <T>
 */
public abstract class AbstractBeanBlockList<S extends SearchCriteria, T>
		extends VerticalLayout implements HasPagableHandlers {
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

	private Set<PagableHandler> pagableHandlers;

	protected SearchRequest<S> searchRequest;

    public static final String[] COLOR_STYLENAME_LIST = new String[] { "red-block",
			"cyan-block", "blue-block", "lightblue-block", "purple-block",
			"yellow-block", "lime-block", "magenta-block", "silver-block",
			"gray-block", "orange-block", "brown-block", "maroon-block",
			"green-block", "olive-block" };

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
	public void addPagableHandler(final PagableHandler handler) {
		if (pagableHandlers == null) {
			pagableHandlers = new HashSet<>();
		}
		pagableHandlers.add(handler);
	}

	protected CssLayout createPageControls() {
		this.controlBarWrapper = new CssLayout();
		this.controlBarWrapper.setWidth("100%");

		final HorizontalLayout controlBar = new HorizontalLayout();
		controlBar.setWidth("100%");
		this.controlBarWrapper.addComponent(controlBar);

		this.pageManagement = new MHorizontalLayout();

		// defined layout here ---------------------------

		if (this.currentPage > 1) {
			final Button firstLink = new ButtonLink("1",
					new Button.ClickListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void buttonClick(final ClickEvent event) {
							AbstractBeanBlockList.this.pageChange(1);
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
			final Button previous2 = new ButtonLink(
					"" + (this.currentPage - 2), new ClickListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void buttonClick(final ClickEvent event) {
							AbstractBeanBlockList.this
									.pageChange(AbstractBeanBlockList.this.currentPage - 2);
						}
					}, false);
			previous2.addStyleName("buttonPaging");
			this.pageManagement.addComponent(previous2);
		}
		if (this.currentPage > 2) {
			final Button previous1 = new ButtonLink(
					"" + (this.currentPage - 1), new ClickListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void buttonClick(final ClickEvent event) {
							AbstractBeanBlockList.this
									.pageChange(AbstractBeanBlockList.this.currentPage - 1);
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
						AbstractBeanBlockList.this
								.pageChange(AbstractBeanBlockList.this.currentPage);
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
							AbstractBeanBlockList.this
									.pageChange(AbstractBeanBlockList.this.currentPage + 1);
						}
					}, false);
			next1.addStyleName("buttonPaging");
			this.pageManagement.addComponent(next1);
		}
		if (range >= 2) {
			final Button next2 = new ButtonLink("" + (this.currentPage + 2),
					new ClickListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void buttonClick(final ClickEvent event) {
							AbstractBeanBlockList.this
									.pageChange(AbstractBeanBlockList.this.currentPage + 2);
						}
					}, false);
			next2.addStyleName("buttonPaging");
			this.pageManagement.addComponent(next2);
		}
		if (range >= 4) {
			final Label ss2 = new Label("...");
			ss2.addStyleName("buttonPaging");
			this.pageManagement.addComponent(ss2);
		}
		if (range >= 3) {
			final Button last = new ButtonLink("" + this.totalPage,
					new ClickListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void buttonClick(final ClickEvent event) {
							AbstractBeanBlockList.this
									.pageChange(AbstractBeanBlockList.this.totalPage);
						}
					}, false);
			last.addStyleName("buttonPaging");
			this.pageManagement.addComponent(last);
		}

		this.pageManagement.setWidth(null);
		controlBar.addComponent(this.pageManagement);
		controlBar.setComponentAlignment(this.pageManagement, Alignment.MIDDLE_RIGHT);

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

			if (pagableHandlers != null) {
				for (final PagableHandler handler : pagableHandlers) {
					handler.move(currentPage);
				}
			}
		}
	}

	protected void setCurrentPage(final int currentPage) {
		this.currentPage = currentPage;
	}

	public void setSearchCriteria(final S searchCriteria) {
		searchRequest = new SearchRequest<S>(searchCriteria, currentPage,
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

	public static interface BlockDisplayHandler<T> {

		Component generateBlock(T obj, int blockIndex);

	}

}
