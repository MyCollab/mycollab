/**
 * mycollab-mobile - Parent pom providing dependency and plugin management for applications
		built with Maven
 * Copyright © ${project.inceptionYear} MyCollab (support@mycollab.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.mobile.module.project.ui;

import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Div;
import com.hp.gagawa.java.elements.Img;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.core.arguments.ValuedBean;
import com.mycollab.db.arguments.BasicSearchRequest;
import com.mycollab.db.arguments.SearchCriteria;
import com.mycollab.html.DivLessFormatter;
import com.mycollab.mobile.ui.AbstractMobilePageView;
import com.mycollab.mobile.ui.DefaultPagedBeanList;
import com.mycollab.module.file.service.AbstractStorageService;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectLinkBuilder;
import com.mycollab.module.project.domain.SimpleItemTimeLogging;
import com.mycollab.module.project.domain.criteria.ItemTimeLoggingSearchCriteria;
import com.mycollab.module.project.i18n.TimeTrackingI18nEnum;
import com.mycollab.module.project.service.ItemTimeLoggingService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.touchkit.NavigationBarQuickMenu;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.ui.IBeanList;
import com.mycollab.vaadin.ui.NotificationUtil;
import com.mycollab.vaadin.ui.UIConstants;
import com.vaadin.addon.touchkit.ui.DatePicker;
import com.vaadin.addon.touchkit.ui.NumberField;
import com.vaadin.addon.touchkit.ui.Switch;
import com.vaadin.addon.touchkit.ui.VerticalComponentGroup;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import org.apache.commons.lang3.time.DateUtils;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MCssLayout;
import org.vaadin.viritin.layouts.MHorizontalLayout;
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

    private MVerticalLayout content;
    private HorizontalLayout headerPanel;
    private Label totalSpentTimeLbl;
    private Label remainTimeLbl;

    protected TimeLogEditView(final V bean) {
        this.bean = bean;
        content = new MVerticalLayout().withMargin(false);
        this.setContent(content);
        this.setCaption(UserUIContext.getMessage(TimeTrackingI18nEnum.DIALOG_LOG_TIME_ENTRY_TITLE));

        this.itemTimeLoggingService = AppContextUtil.getSpringBean(ItemTimeLoggingService.class);

        this.initUI();
        this.loadTimeValue();
    }

    private void initUI() {
        headerPanel = new MHorizontalLayout().withFullWidth().withMargin(new MarginInfo(true, false, true, false));
        content.addComponent(headerPanel);

        constructSpentTimeEntryPanel();
        constructRemainTimeEntryPanel();

        tableItem = new DefaultPagedBeanList<ItemTimeLoggingService, ItemTimeLoggingSearchCriteria, SimpleItemTimeLogging>(
                AppContextUtil.getSpringBean(ItemTimeLoggingService.class), new TimeLogRowHandler()) {
            private static final long serialVersionUID = -4549910960891655297L;

            @Override
            protected void renderRows() {
                int i = 0;
                Date currentDate = new GregorianCalendar(2100, 1, 1).getTime();
                for (final SimpleItemTimeLogging item : currentListData) {
                    if (!DateUtils.isSameDay(item.getLogforday(), currentDate)) {
                        listContainer.addComponent(new ELabel(UserUIContext.formatDate(item.getLogforday()))
                                .withStyleName(UIConstants.FIELD_NOTE).withWidthUndefined());
                        currentDate = item.getLogforday();
                    }
                    final Component row = getRowDisplayHandler().generateRow(tableItem, item, i);
                    listContainer.addComponent(row);
                    i++;
                }
            }
        };

        content.with(ELabel.hr(), tableItem).expand(tableItem);

        MVerticalLayout controlBtns = new MVerticalLayout().withFullWidth();
        controlBtns.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        MButton addNewEntryBtn = new MButton(UserUIContext.getMessage(TimeTrackingI18nEnum.M_DIALOG_ADD_TIME_LOG_ENTRY),
                clickEvent -> UI.getCurrent().addWindow(new NewTimeLogEntryWindow())).withFullWidth();
        controlBtns.addComponent(addNewEntryBtn);

        MButton updateRemainTimeBtn = new MButton(UserUIContext.getMessage(TimeTrackingI18nEnum.M_DIALOG_UPDATE_REMAIN_HOURS),
                clickEvent -> UI.getCurrent().addWindow(new UpdateRemainTimeWindow())).withFullWidth();
        controlBtns.addComponent(updateRemainTimeBtn);

        NavigationBarQuickMenu editBtn = new NavigationBarQuickMenu();
        editBtn.setContent(controlBtns);
        this.setRightComponent(editBtn);
    }

    private void constructSpentTimeEntryPanel() {
        final VerticalLayout totalLayout = new VerticalLayout();
        totalLayout.setMargin(new MarginInfo(false, true, false, true));
        totalLayout.setWidth("100%");
        final Label lbTimeInstructTotal = new Label(UserUIContext.getMessage(TimeTrackingI18nEnum.OPT_TOTAL_SPENT_HOURS));
        totalLayout.addComponent(lbTimeInstructTotal);
        this.totalSpentTimeLbl = new ELabel("_").withStyleName("h2");
        totalLayout.addComponent(this.totalSpentTimeLbl);

        headerPanel.addComponent(totalLayout);
    }

    private void constructRemainTimeEntryPanel() {
        final VerticalLayout updateLayout = new VerticalLayout();
        updateLayout.setMargin(new MarginInfo(false, true, false, true));
        updateLayout.setWidth("100%");

        final Label lbTimeInstructTotal = new Label(UserUIContext.getMessage(TimeTrackingI18nEnum.OPT_REMAINING_WORK_HOURS));
        updateLayout.addComponent(lbTimeInstructTotal);
        remainTimeLbl = new ELabel("_").withStyleName("h2");
        updateLayout.addComponent(this.remainTimeLbl);

        headerPanel.addComponent(updateLayout);
    }

    private void loadTimeValue() {
        final ItemTimeLoggingSearchCriteria searchCriteria = this.getItemSearchCriteria();
        searchCriteria.addOrderField(new SearchCriteria.OrderField("logForDay", SearchCriteria.DESC));
        this.tableItem.search(searchCriteria);
        this.setTotalTimeValue();
        this.setUpdateTimeValue();
    }

    private double getTotalInvest() {
        double total = 0;
        final ItemTimeLoggingSearchCriteria searchCriteria = this.getItemSearchCriteria();
        final List<SimpleItemTimeLogging> listTime = (List<SimpleItemTimeLogging>)itemTimeLoggingService.findPageableListByCriteria(new BasicSearchRequest<>(searchCriteria));
        for (final SimpleItemTimeLogging simpleItemTimeLogging : listTime) {
            total += simpleItemTimeLogging.getLogvalue();
        }
        return total;
    }

    private void setUpdateTimeValue() {
        if (this.getEstimateRemainTime() > -1) {
            remainTimeLbl.setValue(this.getEstimateRemainTime() + "");
        }
    }

    private void setTotalTimeValue() {
        if (this.getTotalInvest() > 0) {
            totalSpentTimeLbl.setValue(this.getTotalInvest() + "");
        }
    }

    protected abstract void saveTimeInvest(double spentHours, boolean isBillable, Date forDate);

    protected abstract void updateTimeRemain(double newValue);

    protected abstract ItemTimeLoggingSearchCriteria getItemSearchCriteria();

    protected abstract double getEstimateRemainTime();

    protected abstract boolean isEnableAdd();

    private class TimeLogRowHandler implements IBeanList.RowDisplayHandler<SimpleItemTimeLogging> {

        @Override
        public Component generateRow(IBeanList<SimpleItemTimeLogging> host, SimpleItemTimeLogging itemLogging, int rowIndex) {
            Img avatar = new Img("", AppContextUtil.getSpringBean(AbstractStorageService.class)
                    .getAvatarPath(itemLogging.getLogUserAvatarId(), 16))
                    .setCSSClass(UIConstants.CIRCLE_BOX);
            Div memberLink = new DivLessFormatter().appendChild(avatar, DivLessFormatter.EMPTY_SPACE,
                    new A(ProjectLinkBuilder.generateProjectMemberFullLink(CurrentProjectVariables.getProjectId(),
                            itemLogging.getLoguser())).appendText(itemLogging.getLogUserFullName()));
            MCssLayout memberLbl = new MCssLayout(ELabel.html(memberLink.write()).withStyleName(UIConstants
                    .TEXT_ELLIPSIS).withFullWidth());
            FontAwesome icon = (Boolean.TRUE.equals(itemLogging.getIsbillable())) ? FontAwesome.MONEY : FontAwesome.GIFT;
            Label timeValueLbl = ELabel.html(icon.getHtml() + " " + UserUIContext.formatTime(itemLogging.getLogvalue()));
            return new MHorizontalLayout(timeValueLbl, memberLbl).withStyleName("row");
        }

    }

    private class NewTimeLogEntryWindow extends MWindow {
        private static final long serialVersionUID = 1285267216691339362L;

        private NumberField newTimeInputField;
        private Switch isBillableField;
        private DatePicker forDate;

        NewTimeLogEntryWindow() {
            super(UserUIContext.getMessage(TimeTrackingI18nEnum.M_DIALOG_ADD_TIME_LOG_ENTRY));
            withModal(true).withResizable(false).withClosable(false).withDraggable(false).withWidth("95%").withCenter();
            constructUI();
        }

        private void constructUI() {
            final VerticalLayout addLayout = new VerticalLayout();
            addLayout.setWidth("100%");
            this.setContent(addLayout);

            VerticalComponentGroup inputWrapper = new VerticalComponentGroup();
            inputWrapper.setWidth("100%");

            this.newTimeInputField = new NumberField();
            this.newTimeInputField.setCaption(UserUIContext.getMessage(TimeTrackingI18nEnum.M_FORM_SPENT_HOURS));
            this.newTimeInputField.setWidth("100%");
            inputWrapper.addComponent(this.newTimeInputField);

            this.forDate = new DatePicker();
            this.forDate.setValue(new GregorianCalendar().getTime());
            this.forDate.setCaption(UserUIContext.getMessage(TimeTrackingI18nEnum.LOG_FOR_DATE));
            inputWrapper.addComponent(this.forDate);

            this.isBillableField = new Switch(UserUIContext.getMessage(TimeTrackingI18nEnum.M_FORM_IS_BILLABLE), true);
            inputWrapper.addComponent(this.isBillableField);
            addLayout.addComponent(inputWrapper);

            MButton createBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_CREATE), clickEvent -> {
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

            Button cancelBtn = new Button(UserUIContext.getMessage(GenericI18Enum.BUTTON_CANCEL), clickEvent -> close());

            MHorizontalLayout buttonLayout = new MHorizontalLayout(cancelBtn, createBtn).withSpacing(false).withFullWidth();
            addLayout.addComponent(buttonLayout);
        }
    }

    private class UpdateRemainTimeWindow extends MWindow {
        private static final long serialVersionUID = -8992497645142044633L;

        private NumberField remainTimeInputField;

        UpdateRemainTimeWindow() {
            super(UserUIContext.getMessage(TimeTrackingI18nEnum.M_DIALOG_UPDATE_REMAIN_HOURS));
            withModal(true).withResizable(false).withClosable(false).withDraggable(false).withWidth("95%").withCenter();
            constructUI();
        }

        private void constructUI() {
            final VerticalLayout addLayout = new VerticalLayout();
            addLayout.setWidth("100%");
            this.setContent(addLayout);

            CssLayout inputWrapper = new CssLayout();
            inputWrapper.setWidth("100%");

            this.remainTimeInputField = new NumberField();
            this.remainTimeInputField.setWidth("100%");
            inputWrapper.addComponent(this.remainTimeInputField);
            addLayout.addComponent(inputWrapper);

            MButton createBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_UPDATE_LABEL), clickEvent -> {
                try {
                    double d = 0;
                    try {
                        d = Double.parseDouble(remainTimeInputField.getValue());
                    } catch (Exception e) {
                        UpdateRemainTimeWindow.this.close();
                        NotificationUtil.showWarningNotification("You must enter a positive number value");
                    }
                    if (d >= 0) {
                        updateTimeRemain(d);
                        remainTimeLbl.setValue(remainTimeInputField.getValue());
                        remainTimeInputField.setValue("0.0");
                    }
                } catch (final Exception e) {
                    remainTimeInputField.setValue("0.0");
                } finally {
                    close();
                }
            }).withStyleName("add-btn");

            Button cancelBtn = new Button(UserUIContext.getMessage(GenericI18Enum.BUTTON_CANCEL), clickEvent -> close());

            MHorizontalLayout buttonLayout = new MHorizontalLayout(cancelBtn, createBtn).withSpacing(false)
                    .withStyleName("border-top").withFullWidth();

            addLayout.addComponent(buttonLayout);
        }
    }
}
