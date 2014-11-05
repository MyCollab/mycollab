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
package com.esofthead.mycollab.module.project.ui.components;

import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import com.esofthead.mycollab.common.TableViewField;
import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.core.arguments.SearchRequest;
import com.esofthead.mycollab.core.arguments.ValuedBean;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.domain.SimpleItemTimeLogging;
import com.esofthead.mycollab.module.project.domain.criteria.ItemTimeLoggingSearchCriteria;
import com.esofthead.mycollab.module.project.i18n.TimeTrackingI18nEnum;
import com.esofthead.mycollab.module.project.service.ItemTimeLoggingService;
import com.esofthead.mycollab.module.project.view.settings.component.ProjectUserLink;
import com.esofthead.mycollab.module.project.view.time.TimeTableFieldDef;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.DateFieldExt;
import com.esofthead.mycollab.vaadin.ui.MyCollabResource;
import com.esofthead.mycollab.vaadin.ui.NotificationUtil;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.esofthead.mycollab.vaadin.ui.WebResourceIds;
import com.esofthead.mycollab.vaadin.ui.table.DefaultPagedBeanTable;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.3.3
 *
 */
public abstract class TimeLogEditWindow<V extends ValuedBean> extends Window {
	private static final long serialVersionUID = 1L;

	protected ItemTimeLoggingService itemTimeLoggingService;
	protected V bean;

	private DefaultPagedBeanTable<ItemTimeLoggingService, ItemTimeLoggingSearchCriteria, SimpleItemTimeLogging> tableItem;

	private VerticalLayout content;
	private HorizontalLayout headerPanel;

	private Button btnAdd;
	private Label totalSpentTimeLbl;
	private NumbericTextField newTimeInputField;
	private CheckBox isBillableField;
	private DateFieldExt forDateField;

	private NumbericTextField remainTimeInputField;
	private Label remainTimeLbl;

	protected TimeLogEditWindow(final V bean) {
		this.bean = bean;
		this.center();
		content = new VerticalLayout();
		this.setResizable(false);
		this.setModal(true);
		this.setContent(content);
		content.setMargin(true);
		content.setSpacing(true);

		this.itemTimeLoggingService = ApplicationContextUtil
				.getSpringBean(ItemTimeLoggingService.class);

		this.initUI();
		this.loadTimeValue();
	}

	private void initUI() {
		this.setWidth("900px");

		headerPanel = new HorizontalLayout();
		headerPanel.setSpacing(true);
		headerPanel.setWidth("100%");
		content.addComponent(headerPanel);
		constructSpentTimeEntryPanel();
		constructRemainTimeEntryPanel();

		this.tableItem = new DefaultPagedBeanTable<ItemTimeLoggingService, ItemTimeLoggingSearchCriteria, SimpleItemTimeLogging>(
				ApplicationContextUtil
						.getSpringBean(ItemTimeLoggingService.class),
				SimpleItemTimeLogging.class, Arrays.asList(
						TimeTableFieldDef.logUser,
						TimeTableFieldDef.logForDate,
						TimeTableFieldDef.logValue, TimeTableFieldDef.billable,
						new TableViewField(null, "id",
								UIConstants.TABLE_CONTROL_WIDTH)));

		this.tableItem.addGeneratedColumn("logUserFullName",
				new Table.ColumnGenerator() {
					private static final long serialVersionUID = 1L;

					@Override
					public com.vaadin.ui.Component generateCell(
							final Table source, final Object itemId,
							final Object columnId) {
						final SimpleItemTimeLogging timeLoggingItem = TimeLogEditWindow.this.tableItem
								.getBeanByIndex(itemId);

						return new ProjectUserLink(
								timeLoggingItem.getLoguser(), timeLoggingItem
										.getLogUserAvatarId(), timeLoggingItem
										.getLogUserFullName());

					}
				});

		this.tableItem.addGeneratedColumn("logforday", new ColumnGenerator() {
			private static final long serialVersionUID = 1L;

			@Override
			public com.vaadin.ui.Component generateCell(final Table source,
					final Object itemId, final Object columnId) {
				final SimpleItemTimeLogging monitorItem = TimeLogEditWindow.this.tableItem
						.getBeanByIndex(itemId);
				final Label l = new Label();
				l.setValue(AppContext.formatDate(monitorItem.getLogforday()));
				return l;
			}
		});

		this.tableItem.addGeneratedColumn("logvalue", new ColumnGenerator() {
			private static final long serialVersionUID = 1L;

			@Override
			public com.vaadin.ui.Component generateCell(final Table source,
					final Object itemId, final Object columnId) {
				final SimpleItemTimeLogging itemTimeLogging = TimeLogEditWindow.this.tableItem
						.getBeanByIndex(itemId);
				final Label l = new Label();
				l.setValue(itemTimeLogging.getLogvalue() + "");
				return l;
			}
		});

		this.tableItem.addGeneratedColumn("isbillable", new ColumnGenerator() {
			private static final long serialVersionUID = 1L;

			@Override
			public Object generateCell(Table source, Object itemId,
					Object columnId) {
				final SimpleItemTimeLogging monitorItem = tableItem
						.getBeanByIndex(itemId);
				Button icon = new Button();
				if (monitorItem.getIsbillable().booleanValue()) {
					icon.setIcon(MyCollabResource
							.newResource("icons/16/yes.png"));
				} else {
					icon.setIcon(MyCollabResource
							.newResource("icons/16/no.png"));
				}
				icon.setStyleName("link");
				return icon;
			}
		});

		this.tableItem.addGeneratedColumn("id", new ColumnGenerator() {
			private static final long serialVersionUID = 1L;

			@Override
			public com.vaadin.ui.Component generateCell(final Table source,
					final Object itemId, final Object columnId) {
				final SimpleItemTimeLogging itemTimeLogging = TimeLogEditWindow.this.tableItem
						.getBeanByIndex(itemId);
				final Button deleteBtn = new Button(null,
						new Button.ClickListener() {
							private static final long serialVersionUID = 1L;

							@Override
							public void buttonClick(final ClickEvent event) {
								TimeLogEditWindow.this.itemTimeLoggingService
										.removeWithSession(
												itemTimeLogging.getId(),
												AppContext.getUsername(),
												AppContext.getAccountId());
								TimeLogEditWindow.this.loadTimeValue();
							}
						});
				deleteBtn.setStyleName("link");
				deleteBtn.setIcon(MyCollabResource
						.newResource("icons/16/delete.png"));
				itemTimeLogging.setExtraData(deleteBtn);

				deleteBtn.setEnabled(CurrentProjectVariables.isAdmin()
						|| AppContext.getUsername().equals(
								itemTimeLogging.getLoguser()));
				return deleteBtn;
			}
		});

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

		this.forDateField = new DateFieldExt();
		this.forDateField.setValue(new GregorianCalendar().getTime());
		addLayout.addComponent(this.forDateField);
		addLayout.setComponentAlignment(this.forDateField,
				Alignment.MIDDLE_LEFT);

		this.isBillableField = new CheckBox(
				AppContext.getMessage(TimeTrackingI18nEnum.FORM_IS_BILLABLE),
				true);
		addLayout.addComponent(this.isBillableField);
		addLayout.setComponentAlignment(this.isBillableField,
				Alignment.MIDDLE_LEFT);

		btnAdd = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_ADD),
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
							saveTimeInvest();
							loadTimeValue();
							newTimeInputField.setValue("0.0");
						}
					}

				});

		btnAdd.setEnabled(TimeLogEditWindow.this.isEnableAdd());
		btnAdd.setStyleName(UIConstants.THEME_GREEN_LINK);
		btnAdd.setIcon(MyCollabResource
				.newResource(WebResourceIds._16_addRecord));
		addLayout.addComponent(btnAdd);
		addLayout.setComponentAlignment(btnAdd, Alignment.MIDDLE_LEFT);
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

		btnAdd = new Button(
				AppContext.getMessage(GenericI18Enum.BUTTON_UPDATE_LABEL),
				new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(final ClickEvent event) {

						try {
							double d = 0;
							try {
								d = Double.parseDouble(remainTimeInputField
										.getValue().toString());
							} catch (Exception e) {
								NotificationUtil
										.showWarningNotification("You must enter a positive number value");
							}
							if (d >= 0) {
								updateTimeRemain();
								remainTimeLbl.setValue(remainTimeInputField
										.getValue());
								remainTimeInputField.setValue("0.0");
							}
						} catch (final Exception e) {
							remainTimeInputField.setValue("0.0");
						}
					}

				});

		btnAdd.setEnabled(TimeLogEditWindow.this.isEnableAdd());
		btnAdd.setStyleName(UIConstants.THEME_GREEN_LINK);
		addLayout.addComponent(btnAdd);
		addLayout.setComponentAlignment(btnAdd, Alignment.MIDDLE_LEFT);
	}

	public void loadTimeValue() {
		final ItemTimeLoggingSearchCriteria searchCriteria = getItemSearchCriteria();
		tableItem.setSearchCriteria(searchCriteria);
		setTotalTimeValue();
		setUpdateTimeValue();
	}

	@SuppressWarnings("unchecked")
	private double getTotalInvest() {
		double total = 0;
		final ItemTimeLoggingSearchCriteria searchCriteria = getItemSearchCriteria();
		final List<SimpleItemTimeLogging> listTime = itemTimeLoggingService
				.findPagableListByCriteria(new SearchRequest<ItemTimeLoggingSearchCriteria>(
						searchCriteria, 0, Integer.MAX_VALUE));
		for (final SimpleItemTimeLogging simpleItemTimeLogging : listTime) {
			total += simpleItemTimeLogging.getLogvalue();
		}
		return total;
	}

	private void setUpdateTimeValue() {
		if (getEstimateRemainTime() > -1) {
			this.remainTimeLbl.setValue(getEstimateRemainTime() + "");
		}
	}

	private void setTotalTimeValue() {
		if (getTotalInvest() > 0) {
			totalSpentTimeLbl.setValue(getTotalInvest() + "");
		}
	}

	protected double getInvestValue() {
		return Double.parseDouble(newTimeInputField.getValue().toString());
	}

	protected Boolean isBillableHours() {
		return isBillableField.getValue();
	}

	protected Date forLogDate() {
		Date date = this.forDateField.getValue();
		return (date != null) ? date : new GregorianCalendar().getTime();
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
}
