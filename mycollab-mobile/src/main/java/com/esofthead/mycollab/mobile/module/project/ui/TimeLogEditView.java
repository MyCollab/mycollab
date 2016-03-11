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

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.core.arguments.SearchCriteria;
import com.esofthead.mycollab.core.arguments.SearchRequest;
import com.esofthead.mycollab.core.arguments.ValuedBean;
import com.esofthead.mycollab.mobile.ui.AbstractMobilePageView;
import com.esofthead.mycollab.mobile.ui.AbstractPagedBeanList.RowDisplayHandler;
import com.esofthead.mycollab.mobile.ui.DefaultPagedBeanList;
import com.esofthead.mycollab.mobile.ui.IconConstants;
import com.esofthead.mycollab.module.project.domain.SimpleItemTimeLogging;
import com.esofthead.mycollab.module.project.domain.criteria.ItemTimeLoggingSearchCriteria;
import com.esofthead.mycollab.module.project.i18n.TimeTrackingI18nEnum;
import com.esofthead.mycollab.module.project.service.ItemTimeLoggingService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.NotificationUtil;
import com.esofthead.vaadin.navigationbarquickmenu.NavigationBarQuickMenu;
import com.vaadin.addon.touchkit.ui.DatePicker;
import com.vaadin.addon.touchkit.ui.Switch;
import com.vaadin.addon.touchkit.ui.VerticalComponentGroup;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import org.apache.commons.lang3.time.DateUtils;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 4.5.0
 */
public abstract class TimeLogEditView<V extends ValuedBean> extends AbstractMobilePageView {
    private static final long serialVersionUID = 1L;

    protected ItemTimeLoggingService itemTimeLoggingService;
    protected V bean;

    private DefaultPagedBeanList<ItemTimeLoggingService, ItemTimeLoggingSearchCriteria, SimpleItemTimeLogging> tableItem;

    private VerticalLayout content;
    private HorizontalLayout headerPanel;
    private Label totalSpentTimeLbl;
    private Label remainTimeLbl;

    protected TimeLogEditView(final V bean) {
        this.bean = bean;
        content = new VerticalLayout();
        content.setSpacing(true);
        content.setSizeFull();
        this.setContent(content);
        this.setCaption(AppContext.getMessage(TimeTrackingI18nEnum.DIALOG_LOG_TIME_ENTRY_TITLE));
        this.addStyleName("timelog-edit-view");

        this.itemTimeLoggingService = ApplicationContextUtil.getSpringBean(ItemTimeLoggingService.class);

        this.initUI();
        this.loadTimeValue();
    }

    private void initUI() {
        headerPanel = new HorizontalLayout();
        headerPanel.setWidth("100%");
        headerPanel.setStyleName("summary-info-panel");
        headerPanel.setMargin(new MarginInfo(true, false, true, false));
        headerPanel.setHeightUndefined();
        content.addComponent(headerPanel);
        constructSpentTimeEntryPanel();
        constructRemainTimeEntryPanel();

        this.tableItem = new DefaultPagedBeanList<ItemTimeLoggingService, ItemTimeLoggingSearchCriteria, SimpleItemTimeLogging>(
                ApplicationContextUtil.getSpringBean(ItemTimeLoggingService.class), new TimeLogRowHandler()) {

            private static final long serialVersionUID = -4549910960891655297L;

            @Override
            protected void renderRows() {
                int i = 0;
                Date currentDate = new GregorianCalendar(2100, 1, 1).getTime();
                for (final SimpleItemTimeLogging item : currentListData) {
                    if (!DateUtils.isSameDay(item.getLogforday(), currentDate)) {
                        Label dateLbl = new Label(AppContext.formatDate(item.getLogforday()));
                        dateLbl.setStyleName("log-day");
                        listContainer.addComponent(dateLbl);
                        currentDate = item.getLogforday();
                    }
                    final Component row = getRowDisplayHandler().generateRow(item, i);
                    listContainer.addComponent(row);
                    i++;
                }
            }
        };

        this.tableItem.setWidth("100%");
        content.addComponent(tableItem);
        content.setExpandRatio(tableItem, 1.0f);

        MVerticalLayout controlBtns = new MVerticalLayout().withWidth("100%");
        controlBtns.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        Button addNewEntryBtn = new Button(AppContext.getMessage(TimeTrackingI18nEnum.M_DIALOG_ADD_TIME_LOG_ENTRY), new Button.ClickListener() {
            private static final long serialVersionUID = -2540265040691537699L;

            @Override
            public void buttonClick(Button.ClickEvent event) {
                UI.getCurrent().addWindow(new NewTimeLogEntryWindow());
            }
        });
        addNewEntryBtn.setWidth("100%");
        controlBtns.addComponent(addNewEntryBtn);

        Button updateRemainTimeBtn = new Button(AppContext.getMessage(TimeTrackingI18nEnum.M_DIALOG_UPDATE_REMAIN_HOURS), new Button.ClickListener() {
            private static final long serialVersionUID = 9215577509351959739L;

            @Override
            public void buttonClick(Button.ClickEvent event) {
                UI.getCurrent().addWindow(new UpdateRemainTimeWindow());
            }
        });
        updateRemainTimeBtn.setWidth("100%");
        controlBtns.addComponent(updateRemainTimeBtn);

        NavigationBarQuickMenu editBtn = new NavigationBarQuickMenu();
        editBtn.setButtonCaption(null);
        editBtn.setContent(controlBtns);
        this.setRightComponent(editBtn);
    }

    private void constructSpentTimeEntryPanel() {
        final VerticalLayout totalLayout = new VerticalLayout();
        totalLayout.setMargin(new MarginInfo(false, true, false, true));
        totalLayout.setStyleName("summary-block");
        totalLayout.addStyleName("total-time");
        totalLayout.setWidth("100%");
        final Label lbTimeInstructTotal = new Label(AppContext.getMessage(TimeTrackingI18nEnum.OPT_TOTAL_SPENT_HOURS));
        lbTimeInstructTotal.setStyleName("block-label");
        totalLayout.addComponent(lbTimeInstructTotal);
        this.totalSpentTimeLbl = new Label("_");
        this.totalSpentTimeLbl.setStyleName("block-value");
        totalLayout.addComponent(this.totalSpentTimeLbl);

        headerPanel.addComponent(totalLayout);
    }

    private void constructRemainTimeEntryPanel() {
        final VerticalLayout updateLayout = new VerticalLayout();
        updateLayout.setMargin(new MarginInfo(false, true, false, true));
        updateLayout.setStyleName("summary-block");
        updateLayout.addStyleName("remain-time");
        updateLayout.setWidth("100%");

        final Label lbTimeInstructTotal = new Label(AppContext.getMessage(TimeTrackingI18nEnum.OPT_REMAINING_WORK_HOURS));
        lbTimeInstructTotal.setStyleName("block-label");
        updateLayout.addComponent(lbTimeInstructTotal);
        this.remainTimeLbl = new Label("_");
        this.remainTimeLbl.setStyleName("block-value");
        updateLayout.addComponent(this.remainTimeLbl);

        this.headerPanel.addComponent(updateLayout);
    }

    public void loadTimeValue() {
        final ItemTimeLoggingSearchCriteria searchCriteria = this.getItemSearchCriteria();
        searchCriteria.addOrderField(new SearchCriteria.OrderField("logForDay", SearchCriteria.DESC));
        this.tableItem.search(searchCriteria);
        this.setTotalTimeValue();
        this.setUpdateTimeValue();
    }

    @SuppressWarnings("unchecked")
    private double getTotalInvest() {
        double total = 0;
        final ItemTimeLoggingSearchCriteria searchCriteria = this.getItemSearchCriteria();
        final List<SimpleItemTimeLogging> listTime = itemTimeLoggingService.findPagableListByCriteria(new SearchRequest<>(
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

    protected abstract void saveTimeInvest(double spentHours, boolean isBillable, Date forDate);

    protected abstract void updateTimeRemain(double newValue);

    protected abstract ItemTimeLoggingSearchCriteria getItemSearchCriteria();

    protected abstract double getEstimateRemainTime();

    protected abstract boolean isEnableAdd();

    private class NumbericTextField extends TextField {
        private static final long serialVersionUID = 1L;

        @Override
        protected void setValue(final String newValue, final boolean repaintIsNotNeeded) {
            try {
                final String d = Double.parseDouble(newValue) + "";
                super.setValue(d, repaintIsNotNeeded);
            } catch (final Exception e) {
                super.setValue("0.0", repaintIsNotNeeded);
            }
        }
    }

    private class TimeLogRowHandler implements RowDisplayHandler<SimpleItemTimeLogging> {

        @Override
        public Component generateRow(SimpleItemTimeLogging obj, int rowIndex) {
            HorizontalLayout layout = new HorizontalLayout();
            layout.setWidth("100%");
            layout.addStyleName("time-log-item");

            VerticalLayout leftCol = new VerticalLayout();
            leftCol.setWidth("100%");

            Label valueLbl = new Label(AppContext.formatTime(obj.getLogvalue()));
            valueLbl.setStyleName("log-value");
            leftCol.addComponent(valueLbl);

            Label logUserLbl = new Label(obj.getLogUserFullName());
            logUserLbl.setStyleName("log-user");
            logUserLbl.setWidthUndefined();
            leftCol.addComponent(logUserLbl);

            layout.addComponent(leftCol);
            layout.setExpandRatio(leftCol, 1.0f);

            if (obj.getIsbillable()) {
                Label billableLbl = new Label(
                        "<span aria-hidden=\"true\" data-icon=\""
                                + IconConstants.CIRCLE_CHECK
                                + "\"></span><div class=\"screen-reader-text\">"
                                + AppContext
                                .getMessage(TimeTrackingI18nEnum.FORM_IS_BILLABLE)
                                + "</div>");
                billableLbl.setContentMode(ContentMode.HTML);
                billableLbl.setWidthUndefined();
                billableLbl.setStyleName("is-billable");
                layout.addComponent(billableLbl);
                layout.setComponentAlignment(billableLbl, Alignment.MIDDLE_LEFT);
            }

            return layout;
        }

    }

    private class NewTimeLogEntryWindow extends Window {
        private static final long serialVersionUID = 1285267216691339362L;

        private NumbericTextField newTimeInputField;
        private Switch isBillableField;
        private DatePicker forDate;
        private Button btnAdd;

        public NewTimeLogEntryWindow() {
            super();
            constructUI();
        }

        public void constructUI() {
            this.setStyleName("time-log-window");
            this.addStyleName("new-time-entry-window");
            this.setWidth("95%");
            this.center();
            this.setResizable(false);
            this.setClosable(false);
            this.setModal(true);
            this.setDraggable(false);
            this.setCaption(AppContext.getMessage(TimeTrackingI18nEnum.M_DIALOG_ADD_TIME_LOG_ENTRY));

            final VerticalLayout addLayout = new VerticalLayout();
            addLayout.setWidth("100%");
            this.setContent(addLayout);

            VerticalComponentGroup inputWrapper = new VerticalComponentGroup();
            inputWrapper.setWidth("100%");
            inputWrapper.setStyleName("input-wrapper");

            this.newTimeInputField = new NumbericTextField();
            this.newTimeInputField.setCaption(AppContext.getMessage(TimeTrackingI18nEnum.M_FORM_SPENT_HOURS));

            this.newTimeInputField.setWidth("100%");
            inputWrapper.addComponent(this.newTimeInputField);

            this.forDate = new DatePicker();
            this.forDate.setValue(new GregorianCalendar().getTime());
            this.forDate.setCaption(AppContext.getMessage(TimeTrackingI18nEnum.M_FORM_FOR_DAY));
            inputWrapper.addComponent(this.forDate);

            this.isBillableField = new Switch(AppContext.getMessage(TimeTrackingI18nEnum.M_FORM_IS_BILLABLE), true);
            inputWrapper.addComponent(this.isBillableField);
            addLayout.addComponent(inputWrapper);

            HorizontalLayout buttonLayout = new HorizontalLayout();
            buttonLayout.setStyleName("button-layout");
            buttonLayout.setWidth("100%");

            this.btnAdd = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_CREATE), new Button.ClickListener() {
                private static final long serialVersionUID = 1L;

                @Override
                public void buttonClick(final Button.ClickEvent event) {
                    double d = 0;
                    try {
                        d = Double.parseDouble(newTimeInputField.getValue().toString());
                    } catch (NumberFormatException e) {
                        NewTimeLogEntryWindow.this.close();
                        NotificationUtil.showWarningNotification("You must enter a positive number value");
                    }
                    if (d > 0) {
                        TimeLogEditView.this.saveTimeInvest(Double.parseDouble(newTimeInputField.getValue()),
                                isBillableField.getValue(), forDate.getValue());
                        TimeLogEditView.this.loadTimeValue();
                        NewTimeLogEntryWindow.this.close();
                    }
                }

            });

            this.btnAdd.setStyleName("add-btn");

            Button cancelBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_CANCEL), new Button.ClickListener() {
                private static final long serialVersionUID = -5815617874202220414L;

                @Override
                public void buttonClick(Button.ClickEvent event) {
                    NewTimeLogEntryWindow.this.close();
                }
            });

            buttonLayout.addComponent(cancelBtn);
            buttonLayout.addComponent(this.btnAdd);

            addLayout.addComponent(buttonLayout);
        }
    }

    private class UpdateRemainTimeWindow extends Window {
        private static final long serialVersionUID = -8992497645142044633L;

        private NumbericTextField remainTimeInputField;
        private Button btnAdd;

        public UpdateRemainTimeWindow() {
            super();

            constructUI();
        }

        private void constructUI() {
            this.setStyleName("time-log-window");
            this.addStyleName("update-remain-time-window");
            this.setWidth("95%");
            this.center();
            this.setResizable(false);
            this.setClosable(false);
            this.setModal(true);
            this.setDraggable(false);
            this.setCaption(AppContext.getMessage(TimeTrackingI18nEnum.M_DIALOG_UPDATE_REMAIN_HOURS));

            final VerticalLayout addLayout = new VerticalLayout();
            addLayout.setWidth("100%");
            this.setContent(addLayout);

            CssLayout inputWrapper = new CssLayout();
            inputWrapper.setStyleName("input-wrapper");
            inputWrapper.setWidth("100%");

            this.remainTimeInputField = new NumbericTextField();
            this.remainTimeInputField.setWidth("100%");
            inputWrapper.addComponent(this.remainTimeInputField);
            addLayout.addComponent(inputWrapper);

            HorizontalLayout buttonLayout = new HorizontalLayout();
            buttonLayout.setStyleName("button-layout");
            buttonLayout.setWidth("100%");

            this.btnAdd = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_UPDATE_LABEL), new Button.ClickListener() {
                private static final long serialVersionUID = 1L;

                @Override
                public void buttonClick(final Button.ClickEvent event) {
                    try {
                        double d = 0;
                        try {
                            d = Double.parseDouble(UpdateRemainTimeWindow.this.remainTimeInputField.getValue());
                        } catch (Exception e) {
                            UpdateRemainTimeWindow.this.close();
                            NotificationUtil.showWarningNotification("You must enter a positive number value");
                        }
                        if (d >= 0) {
                            updateTimeRemain(d);
                            remainTimeLbl.setValue(UpdateRemainTimeWindow.this.remainTimeInputField.getValue());
                            remainTimeInputField.setValue("0.0");
                        }
                    } catch (final Exception e) {
                        remainTimeInputField.setValue("0.0");
                    } finally {
                        UpdateRemainTimeWindow.this.close();
                    }
                }

            });
            this.btnAdd.setStyleName("add-btn");

            Button cancelBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_CANCEL), new Button.ClickListener() {
                private static final long serialVersionUID = -4046714950287134902L;

                @Override
                public void buttonClick(Button.ClickEvent event) {
                    UpdateRemainTimeWindow.this.close();
                }
            });

            buttonLayout.addComponent(cancelBtn);
            buttonLayout.addComponent(this.btnAdd);

            addLayout.addComponent(buttonLayout);
        }
    }
}
