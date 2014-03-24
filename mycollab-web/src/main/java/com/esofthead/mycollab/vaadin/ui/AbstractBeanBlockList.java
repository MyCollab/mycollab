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

public abstract class AbstractBeanBlockList<S extends SearchCriteria, T> extends
VerticalLayout implements HasPagableHandlers {

	private static final long serialVersionUID = -1842929843421392806L;

	private int defaultNumberSearchItems = 10;
	protected final CssLayout itemContainer;
	protected BlockDisplayHandler<T> blockDisplayHandler;
	protected int currentPage = 1;
	protected int totalPage = 1;
	protected int totalCount;
	protected List<T> currentListData;
	protected CssLayout controlBarWrapper;
	protected HorizontalLayout pageManagement;

	private Set<PagableHandler> pagableHandlers;

	protected SearchRequest<S> searchRequest;

	public AbstractBeanBlockList(final BlockDisplayHandler<T> blockDisplayHandler,
			final int defaultNumberSearchItems) {
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
			pagableHandlers = new HashSet<PagableHandler>();
		}
		pagableHandlers.add(handler);
	}

	protected CssLayout createPageControls() {
		this.controlBarWrapper = new CssLayout();
		this.controlBarWrapper.setWidth("100%");

		final HorizontalLayout controlBar = new HorizontalLayout();
		controlBar.setWidth("100%");
		this.controlBarWrapper.addComponent(controlBar);

		this.pageManagement = new HorizontalLayout();

		// defined layout here ---------------------------

		if (this.currentPage > 1) {
			final Button firstLink = new ButtonLink("1", new Button.ClickListener() {
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
		// Here add current ButtonLink
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
		current.addStyleName("buttonPagingcurrent");

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
		this.pageManagement.setSpacing(true);
		controlBar.addComponent(this.pageManagement);
		controlBar.setComponentAlignment(this.pageManagement,
				Alignment.MIDDLE_RIGHT);

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

		if (this.controlBarWrapper != null && this.controlBarWrapper.getParent() == this) {
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
