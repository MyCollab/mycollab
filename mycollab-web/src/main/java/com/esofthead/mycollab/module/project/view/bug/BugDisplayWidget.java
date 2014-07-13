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
package com.esofthead.mycollab.module.project.view.bug;

import com.esofthead.mycollab.core.arguments.SearchRequest;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.project.events.BugEvent;
import com.esofthead.mycollab.module.project.view.parameters.BugFilterParameter;
import com.esofthead.mycollab.module.project.view.parameters.BugScreenData;
import com.esofthead.mycollab.module.tracker.domain.SimpleBug;
import com.esofthead.mycollab.module.tracker.domain.criteria.BugSearchCriteria;
import com.esofthead.mycollab.module.tracker.service.BugService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.ui.BeanList;
import com.esofthead.mycollab.vaadin.ui.BeanList.RowDisplayHandler;
import com.esofthead.mycollab.vaadin.ui.Depot;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public abstract class BugDisplayWidget extends Depot {

	private static final long serialVersionUID = 1L;
	public static int MAX_ITEM_DISPLAY = 5;

	protected BugSearchCriteria searchCriteria;
	private BeanList<BugService, BugSearchCriteria, SimpleBug> dataList;
	private Button moreBtn;

	public BugDisplayWidget(
			final String title,
			final Class<? extends RowDisplayHandler<SimpleBug>> rowDisplayHandler) {
		super(title, new VerticalLayout());

		dataList = new BeanList<BugService, BugSearchCriteria, SimpleBug>(
				ApplicationContextUtil.getSpringBean(BugService.class),
				rowDisplayHandler);
		bodyContent.addComponent(dataList);
		bodyContent.setStyleName(UIConstants.BUG_LIST);

	}

	protected abstract BugFilterParameter constructMoreDisplayFilter();

	public void setSearchCriteria(final BugSearchCriteria searchCriteria) {
		this.searchCriteria = searchCriteria;
		final SearchRequest<BugSearchCriteria> searchRequest = new SearchRequest<BugSearchCriteria>(
				searchCriteria, 0, BugDisplayWidget.MAX_ITEM_DISPLAY);
		final int displayItemsCount = dataList.setSearchRequest(searchRequest);
		if (displayItemsCount == BugDisplayWidget.MAX_ITEM_DISPLAY) {
			moreBtn = new Button("More", new Button.ClickListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void buttonClick(final ClickEvent event) {
					EventBusFactory.getInstance().post(
							new BugEvent.GotoList(BugDisplayWidget.this,
									new BugScreenData.Search(
											constructMoreDisplayFilter())));
				}
			});
			moreBtn.setStyleName(UIConstants.THEME_BLANK_LINK);
			final VerticalLayout widgetFooter = new VerticalLayout();
			widgetFooter.addStyleName("widget-footer");
			widgetFooter.setWidth("100%");
			widgetFooter.setMargin(true);
			widgetFooter.addComponent(moreBtn);
			widgetFooter.setComponentAlignment(moreBtn, Alignment.TOP_RIGHT);
			bodyContent.addComponent(widgetFooter);
		}
	}
}
