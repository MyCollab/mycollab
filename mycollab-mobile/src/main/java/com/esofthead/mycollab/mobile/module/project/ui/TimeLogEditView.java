/**
 * This file is part of mycollab-mobile.
 *
 * mycollab-mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-mobile.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.mobile.module.project.ui;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.core.arguments.SearchRequest;
import com.esofthead.mycollab.core.arguments.ValuedBean;
import com.esofthead.mycollab.mobile.ui.AbstractMobilePageView;
import com.esofthead.mycollab.mobile.ui.AbstractPagedBeanList.RowDisplayHandler;
import com.esofthead.mycollab.mobile.ui.DefaultPagedBeanList;
import com.esofthead.mycollab.module.project.domain.SimpleItemTimeLogging;
import com.esofthead.mycollab.module.project.domain.criteria.ItemTimeLoggingSearchCriteria;
import com.esofthead.mycollab.module.project.i18n.TimeTrackingI18nEnum;
import com.esofthead.mycollab.module.project.service.ItemTimeLoggingService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.MyCollabResource;
import com.esofthead.mycollab.vaadin.ui.NotificationUtil;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.5.0
 *
 */
public abstract class TimeLogEditView<V extends ValuedBean> extends
		AbstractMobilePageView {
	private static final long serialVersionUID = 1L;

	protected ItemTimeLoggingService itemTimeLoggingService;
	protected V bean;

	private DefaultPagedBeanList<ItemTimeLoggingService, ItemTimeLoggingSearchCriteria, SimpleItemTimeLogging> tableItem;

	private VerticalLayout content;
	private HorizontalLayout headerPanel;

	private Button btnAdd;
	private Label totalSpentTimeLbl;
	private NumbericTextField newTimeInputField;
	private CheckBox isBillableField;
	private DateField forDate;

	private NumbericTextField remainTimeInputField;
	private Label remainTimeLbl;

	protected TimeLogEditView(final V bean) {
		this.bean = bean;
		content = new VerticalLayout();
		this.setContent(content);
		this.setCaption(AppContext
				.getMessage(TimeTrackingI18nEnum.DIALOG_LOG_TIME_ENTRY_TITLE));

		this.itemTimeLoggingService = ApplicationContextUtil
				.getSpringBean(ItemTimeLoggingService.class);

		this.initUI();
		this.loadTimeValue();
	}

	private void initUI() {
		// this.setWidth("900px");
		//
		// headerPanel = new HorizontalLayout();
		// headerPanel.setSpacing(true);
		// headerPanel.setWidth("100%");
		// content.addComponent(headerPanel);
		// constructSpentTimeEntryPanel();
		// constructRemainTimeEntryPanel();

		this.tableItem = new DefaultPagedBeanList<ItemTimeLoggingService, ItemTimeLoggingSearchCriteria, SimpleItemTimeLogging>(
				ApplicationContextUtil
						.getSpringBean(ItemTimeLoggingService.class),
				new TimeLogRowHandler());

		this.tableItem.setWidth("100%");
		content.addComponent(tableItem);
	}

	private void constructSpentTimeEntryPanel() {
		VerticalLayout spentTimePanel = new VerticalLayout();
		spentTimePanel.setSpacing(true);
		headerPanel.addComponent(spentTimePanel);

		final VerticalLayout totalLayout = new VerticalLayout();
		totalLayout.setMargin(true);
		totalLayout.addStyleName("boxTotal");
		totalLayout.setWidth("100%");
		spentTimePanel.addComponent(totalLayout);
		final Label lbTimeInstructTotal = new Label(
				AppContext
						.getMessage(TimeTrackingI18nEnum.OPT_TOTAL_SPENT_HOURS));
		totalLayout.addComponent(lbTimeInstructTotal);
		this.totalSpentTimeLbl = new Label("_");
		this.totalSpentTimeLbl.addStyleName("numberTotal");
		totalLayout.addComponent(this.totalSpentTimeLbl);

		final HorizontalLayout addLayout = new HorizontalLayout();
		addLayout.setSpacing(true);
		addLayout.setSizeUndefined();
		spentTimePanel.addComponent(addLayout);

		this.newTimeInputField = new NumbericTextField();
		this.newTimeInputField.setWidth("80px");
		addLayout.addComponent(this.newTimeInputField);
		addLayout.setComponentAlignment(this.newTimeInputField,
				Alignment.MIDDLE_LEFT);

		this.forDate = new DateField();
		this.forDate.setValue(new GregorianCalendar().getTime());
		addLayout.addComponent(this.forDate);
		addLayout.setComponentAlignment(this.forDate, Alignment.MIDDLE_LEFT);

		this.isBillableField = new CheckBox(
				AppContext.getMessage(TimeTrackingI18nEnum.FORM_IS_BILLABLE),
				true);
		addLayout.addComponent(this.isBillableField);
		addLayout.setComponentAlignment(this.isBillableField,
				Alignment.MIDDLE_LEFT);

		this.btnAdd = new Button(
				AppContext.getMessage(GenericI18Enum.BUTTON_ADD_LABEL),
				new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(final ClickEvent event) {
						double d = 0;
						try {
							d = Double.parseDouble(newTimeInputField.getValue()
									.toString());
						} catch (NumberFormatException e) {
							NotificationUtil
									.showWarningNotification("You must enter a positive number value");
						}
						if (d > 0) {
							TimeLogEditView.this.saveTimeInvest();
							TimeLogEditView.this.loadTimeValue();
							TimeLogEditView.this.newTimeInputField
									.setValue("0.0");
						}
					}

				});

		this.btnAdd.setEnabled(TimeLogEditView.this.isEnableAdd());
		this.btnAdd.setIcon(MyCollabResource
				.newResource("icons/16/addRecord.png"));
		addLayout.addComponent(this.btnAdd);
		addLayout.setComponentAlignment(this.btnAdd, Alignment.MIDDLE_LEFT);
	}

	private void constructRemainTimeEntryPanel() {
		VerticalLayout remainTimePanel = new VerticalLayout();
		remainTimePanel.setSpacing(true);
		this.headerPanel.addComponent(remainTimePanel);

		final VerticalLayout updateLayout = new VerticalLayout();
		updateLayout.setMargin(true);
		updateLayout.addStyleName("boxTotal");
		updateLayout.setWidth("100%");
		remainTimePanel.addComponent(updateLayout);

		final Label lbTimeInstructTotal = new Label(
				AppContext
						.getMessage(TimeTrackingI18nEnum.OPT_REMAINING_WORK_HOURS));
		updateLayout.addComponent(lbTimeInstructTotal);
		this.remainTimeLbl = new Label("_");
		this.remainTimeLbl.addStyleName("numberTotal");
		updateLayout.addComponent(this.remainTimeLbl);

		final HorizontalLayout addLayout = new HorizontalLayout();
		addLayout.setSpacing(true);
		addLayout.setSizeUndefined();
		remainTimePanel.addComponent(addLayout);

		this.remainTimeInputField = new NumbericTextField();
		this.remainTimeInputField.setWidth("80px");
		addLayout.addComponent(this.remainTimeInputField);
		addLayout.setComponentAlignment(this.remainTimeInputField,
				Alignment.MIDDLE_LEFT);

		this.btnAdd = new Button(
				AppContext.getMessage(GenericI18Enum.BUTTON_UPDATE_LABEL),
				new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(final ClickEvent event) {

						try {
							double d = 0;
							try {
								d = Double
										.parseDouble(TimeLogEditView.this.remainTimeInputField
												.getValue().toString());
							} catch (Exception e) {
								NotificationUtil
										.showWarningNotification("You must enter a positive number value");
							}
							if (d >= 0) {
								updateTimeRemain();
								remainTimeLbl
										.setValue(TimeLogEditView.this.remainTimeInputField
												.getValue());
								remainTimeInputField.setValue("0.0");
							}
						} catch (final Exception e) {
							remainTimeInputField.setValue("0.0");
						}
					}

				});

		this.btnAdd.setEnabled(this.isEnableAdd());
		addLayout.addComponent(this.btnAdd);
		addLayout.setComponentAlignment(this.btnAdd, Alignment.MIDDLE_LEFT);
	}

	public void loadTimeValue() {
		final ItemTimeLoggingSearchCriteria searchCriteria = this
				.getItemSearchCriteria();
		this.tableItem.setSearchCriteria(searchCriteria);
		// this.setTotalTimeValue();
		// this.setUpdateTimeValue();
	}

	@SuppressWarnings("unchecked")
	private double getTotalInvest() {
		double total = 0;
		final ItemTimeLoggingSearchCriteria searchCriteria = this
				.getItemSearchCriteria();
		final List<SimpleItemTimeLogging> listTime = itemTimeLoggingService
				.findPagableListByCriteria(new SearchRequest<ItemTimeLoggingSearchCriteria>(
						searchCriteria, 0, Integer.MAX_VALUE));
		for (final SimpleItemTimeLogging simpleItemTimeLogging : listTime) {
			total += simpleItemTimeLogging.getLogvalue();
		}
		return total;
	}

	private void setUpdateTimeValue() {
		if (this.getEstimateRemainTime() > -1) {
			this.remainTimeLbl.setValue(this.getEstimateRemainTime() + "");
		}
	}

	private void setTotalTimeValue() {
		if (this.getTotalInvest() > 0) {
			this.totalSpentTimeLbl.setValue(this.getTotalInvest() + "");
		}
	}

	protected double getInvestValue() {
		return Double.parseDouble(newTimeInputField.getValue().toString());
	}

	protected Boolean isBillableHours() {
		return isBillableField.getValue();
	}

	protected Date forLogDate() {
		return this.forDate.getValue();
	}

	protected abstract void saveTimeInvest();

	protected abstract void updateTimeRemain();

	protected abstract ItemTimeLoggingSearchCriteria getItemSearchCriteria();

	protected abstract double getEstimateRemainTime();

	protected abstract boolean isEnableAdd();

	protected double getUpdateRemainTime() {
		return Double.parseDouble(remainTimeInputField.getValue().toString());
	}

	private class NumbericTextField extends TextField {
		private static final long serialVersionUID = 1L;

		@Override
		protected void setValue(final String newValue,
				final boolean repaintIsNotNeeded) {
			try {
				final String d = Double.parseDouble(newValue) + "";
				super.setValue(d, repaintIsNotNeeded);
			} catch (final Exception e) {
				super.setValue("0.0", repaintIsNotNeeded);
			}
		}
	}

	private class TimeLogRowHandler implements
			RowDisplayHandler<SimpleItemTimeLogging> {

		@Override
		public Component generateRow(SimpleItemTimeLogging obj, int rowIndex) {
			VerticalLayout layout = new VerticalLayout();
			layout.setWidth("100%");
			layout.setStyleName("list-item");
			layout.addStyleName("time-log-item");

			Label valueLbl = new Label(AppContext.formatTime(obj.getLogvalue()));
			valueLbl.setStyleName("log-value");
			layout.addComponent(valueLbl);

			Label logUserLbl = new Label(obj.getLogUserFullName());
			logUserLbl.setStyleName("log-user");
			layout.addComponent(logUserLbl);

			return layout;
		}

	}
}
