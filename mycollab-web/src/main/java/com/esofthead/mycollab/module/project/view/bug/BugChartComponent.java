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

import org.vaadin.hene.popupbutton.PopupButton;

import com.esofthead.mycollab.core.utils.LocalizationHelper;
import com.esofthead.mycollab.module.project.localization.BugI18nEnum;
import com.esofthead.mycollab.module.tracker.domain.criteria.BugSearchCriteria;
import com.esofthead.mycollab.vaadin.mvp.ViewManager;
import com.esofthead.mycollab.vaadin.ui.Depot;
import com.rits.cloning.Cloner;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class BugChartComponent extends Depot {
	private static final long serialVersionUID = 1L;
	private final String[] reportDashboard = { "BugsByPriority",
			"BugsByStatus", "BugByResolution" };
	private int currentReportIndex = 0;

	private final BugSearchCriteria baseSearchCriteria;

	public BugChartComponent(final BugSearchCriteria baseSearchCriteria,
			final int headerWidth, final int titleWidth) {
		super(LocalizationHelper.getMessage(BugI18nEnum.CHARTS_WIDGET_TITLE),
				null, new CssLayout(), headerWidth + "px", titleWidth + "px");
		this.setSizeFull();
		this.baseSearchCriteria = baseSearchCriteria;
		this.setContentBorder(true);
		this.bodyContent.setSizeFull();
		initUI();
	}

	public void displayReport() {
		final String reportName = reportDashboard[currentReportIndex];

		final CssLayout bodyContent = (CssLayout) this.bodyContent;
		bodyContent.removeAllComponents();

		if ("BugsByPriority".equals(reportName)) {
			IPrioritySummaryChartWidget prioritySummaryChartWidget = ViewManager
					.getView(IPrioritySummaryChartWidget.class);
			bodyContent.addComponent(prioritySummaryChartWidget);

			final BugSearchCriteria prioritySearchCriteria = new Cloner()
					.deepClone(baseSearchCriteria);
			prioritySummaryChartWidget
					.setSearchCriteria(prioritySearchCriteria);
		} else if ("BugsByStatus".equals(reportName)) {
			IStatusSummaryChartWidget statusSummaryChartWidget = ViewManager
					.getView(IStatusSummaryChartWidget.class);
			bodyContent.addComponent(statusSummaryChartWidget);

			final BugSearchCriteria statusSearchCriteria = new Cloner()
					.deepClone(baseSearchCriteria);
			statusSummaryChartWidget.setSearchCriteria(statusSearchCriteria);
		} else if ("BugByResolution".equals(reportName)) {
			IBugResolutionSummaryChartWidget resolutionSummaryWdiget = ViewManager
					.getView(IBugResolutionSummaryChartWidget.class);
			bodyContent.addComponent(resolutionSummaryWdiget);

			final BugSearchCriteria statusSearchCriteria = new Cloner()
					.deepClone(baseSearchCriteria);
			resolutionSummaryWdiget.setSearchCriteria(statusSearchCriteria);
		}
	}

	private void initUI() {

		final PopupButton bugChartPopup = new PopupButton("Bugs By Priority");
		bugChartPopup.addStyleName("link");

		final VerticalLayout filterBtnLayout = new VerticalLayout();
		filterBtnLayout.setMargin(true);
		filterBtnLayout.setSpacing(true);
		filterBtnLayout.setWidth("200px");

		final Button btnBugByPriority = new Button("Bugs By Priority",
				new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(final ClickEvent event) {
						bugChartPopup.setPopupVisible(false);
						bugChartPopup.setCaption("Bugs By Priority");
						currentReportIndex = 0;
						displayReport();
					}
				});
		btnBugByPriority.setStyleName("link");
		filterBtnLayout.addComponent(btnBugByPriority);

		final Button btnBugByStatus = new Button("Bugs By Status",
				new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(final ClickEvent event) {
						bugChartPopup.setPopupVisible(false);
						bugChartPopup.setCaption("Bugs By Status");
						currentReportIndex = 1;
						displayReport();
					}
				});
		btnBugByStatus.setStyleName("link");
		filterBtnLayout.addComponent(btnBugByStatus);

		final Button btnBugByResolution = new Button("Bugs By Resolution",
				new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(final ClickEvent event) {
						bugChartPopup.setPopupVisible(false);
						bugChartPopup.setCaption("Bugs By Resolution");
						currentReportIndex = 2;
						displayReport();
					}
				});
		btnBugByResolution.setStyleName("link");
		filterBtnLayout.addComponent(btnBugByResolution);

		displayReport();

		bugChartPopup.setContent(filterBtnLayout);
		this.addHeaderElement(bugChartPopup);
		this.setHeaderColor(true);
	}
}
