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
package com.mycollab.mobile.module.project.ui;

import com.esofthead.vaadin.navigationbarquickmenu.NavigationBarQuickMenu;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.core.arguments.ValuedBean;
import com.mycollab.db.arguments.BasicSearchRequest;
import com.mycollab.db.arguments.SearchCriteria;
import com.mycollab.mobile.ui.AbstractMobilePageView;
import com.mycollab.mobile.ui.AbstractPagedBeanList.RowDisplayHandler;
import com.mycollab.mobile.ui.DefaultPagedBeanList;
import com.mycollab.mobile.ui.IconConstants;
import com.mycollab.module.project.domain.SimpleItemTimeLogging;
import com.mycollab.module.project.domain.criteria.ItemTimeLoggingSearchCriteria;
import com.mycollab.module.project.i18n.TimeTrackingI18nEnum;
import com.mycollab.module.project.service.ItemTimeLoggingService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppContext;
import com.mycollab.vaadin.ui.NotificationUtil;
import com.vaadin.addon.touchkit.ui.DatePicker;
import com.vaadin.addon.touchkit.ui.Switch;
import com.vaadin.addon.touchkit.ui.VerticalComponentGroup;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import org.apache.commons.lang3.time.DateUtils;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MVerticalLayout;
import org.vaadin.viritin.layouts.MWindow;

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

        this.itemTimeLoggingService = AppContextUtil.getSpringBean(ItemTimeLoggingService.class);

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
                AppContextUtil.getSpringBean(ItemTimeLoggingService.class), new TimeLogRowHandler()) {

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

        MVerticalLayout controlBtns = new MVerticalLayout().withFullWidth();
        controlBtns.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        MButton addNewEntryBtn = new MButton(AppContext.getMessage(TimeTrackingI18nEnum.M_DIALOG_ADD_TIME_LOG_ENTRY),
                clickEvent -> UI.getCurrent().addWindow(new NewTimeLogEntryWindow())).withFullWidth();
        controlBtns.addComponent(addNewEntryBtn);

        MButton updateRemainTimeBtn = new MButton(AppContext.getMessage(TimeTrackingI18nEnum.M_DIALOG_UPDATE_REMAIN_HOURS),
                clickEvent -> UI.getCurrent().addWindow(new UpdateRemainTimeWindow())).withFullWidth();
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

    private void loadTimeValue() {
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
        final List<SimpleItemTimeLogging> listTime = itemTimeLoggingService.findPageableListByCriteria(new BasicSearchRequest<>(
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

    private class NumericTextField extends TextField {
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

    private class NewTimeLogEntryWindow extends MWindow {
        private static final long serialVersionUID = 1285267216691339362L;

        private NumericTextField newTimeInputField;
        private Switch isBillableField;
        private DatePicker forDate;
        private MButton createBtn;

        NewTimeLogEntryWindow() {
            super(AppContext.getMessage(TimeTrackingI18nEnum.M_DIALOG_ADD_TIME_LOG_ENTRY));
            withModal(true).withResizable(false).withClosable(false).withDraggable(false)
                    .withStyleName("time-log-window", "new-time-entry-window").withWidth("95%").withCenter();
            constructUI();
        }

        private void constructUI() {
            final VerticalLayout addLayout = new VerticalLayout();
            addLayout.setWidth("100%");
            this.setContent(addLayout);

            VerticalComponentGroup inputWrapper = new VerticalComponentGroup();
            inputWrapper.setWidth("100%");
            inputWrapper.setStyleName("input-wrapper");

            this.newTimeInputField = new NumericTextField();
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

            createBtn = new MButton(AppContext.getMessage(GenericI18Enum.BUTTON_CREATE), clickEvent -> {
                double d = 0;
                try {
                    d = Double.parseDouble(newTimeInputField.getValue());
                } catch (NumberFormatException e) {
                    close();
                    NotificationUtil.showWarningNotification("You must enter a positive number value");
                }
                if (d > 0) {
                    saveTimeInvest(Double.parseDouble(newTimeInputField.getValue()),
                            isBillableField.getValue(), forDate.getValue());
                    loadTimeValue();
                    close();
                }
            }).withStyleName("add-btn");

            Button cancelBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_CANCEL), clickEvent -> close());

            buttonLayout.addComponent(cancelBtn);
            buttonLayout.addComponent(this.createBtn);

            addLayout.addComponent(buttonLayout);
        }
    }

    private class UpdateRemainTimeWindow extends MWindow {
        private static final long serialVersionUID = -8992497645142044633L;

        private NumericTextField remainTimeInputField;
        private MButton createBtn;

        UpdateRemainTimeWindow() {
            super(AppContext.getMessage(TimeTrackingI18nEnum.M_DIALOG_UPDATE_REMAIN_HOURS));
            withModal(true).withResizable(false).withClosable(false).withDraggable(false)
                    .withStyleName("time-log-window", "update-remain-time-window").withWidth("95%").withCenter();
            constructUI();
        }

        private void constructUI() {
            final VerticalLayout addLayout = new VerticalLayout();
            addLayout.setWidth("100%");
            this.setContent(addLayout);

            CssLayout inputWrapper = new CssLayout();
            inputWrapper.setStyleName("input-wrapper");
            inputWrapper.setWidth("100%");

            this.remainTimeInputField = new NumericTextField();
            this.remainTimeInputField.setWidth("100%");
            inputWrapper.addComponent(this.remainTimeInputField);
            addLayout.addComponent(inputWrapper);

            HorizontalLayout buttonLayout = new HorizontalLayout();
            buttonLayout.setStyleName("button-layout");
            buttonLayout.setWidth("100%");

            this.createBtn = new MButton(AppContext.getMessage(GenericI18Enum.BUTTON_UPDATE_LABEL), clickEvent -> {
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
                    close();
                }
            }).withStyleName("add-btn");

            Button cancelBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_CANCEL), clickEvent -> close());

            buttonLayout.addComponent(cancelBtn);
            buttonLayout.addComponent(this.createBtn);

            addLayout.addComponent(buttonLayout);
        }
    }
}
