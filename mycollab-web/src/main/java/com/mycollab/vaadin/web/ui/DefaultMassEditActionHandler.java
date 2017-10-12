/**
 * Copyright © MyCollab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.vaadin.web.ui;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.reporting.ReportExportType;
import com.mycollab.reporting.ReportTemplateExecutor;
import com.mycollab.reporting.RpFieldsBuilder;
import com.mycollab.reporting.SimpleReportTemplateExecutor;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.event.MassItemActionHandler;
import com.mycollab.vaadin.event.ViewItemAction;
import com.mycollab.vaadin.reporting.ReportStreamSource;
import com.mycollab.vaadin.web.ui.table.IPagedBeanTable;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.UI;

import java.util.HashMap;
import java.util.Map;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
public abstract class DefaultMassEditActionHandler implements MassItemActionHandler {
    private ListSelectionPresenter presenter;

    public DefaultMassEditActionHandler(ListSelectionPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onSelect(String id) {
        if (ViewItemAction.DELETE_ACTION.equals(id)) {
            ConfirmDialogExt.show(UI.getCurrent(), UserUIContext.getMessage(GenericI18Enum.DIALOG_DELETE_TITLE, AppUI.getSiteName()),
                    UserUIContext.getMessage(GenericI18Enum.DIALOG_DELETE_MULTIPLE_ITEMS_MESSAGE),
                    UserUIContext.getMessage(GenericI18Enum.BUTTON_YES),
                    UserUIContext.getMessage(GenericI18Enum.BUTTON_NO),
                    confirmDialog -> {
                        if (confirmDialog.isConfirmed()) {
                            presenter.deleteSelectedItems();
                        }
                    });
        } else {
            onSelectExtra(id);
        }

    }

    @Override
    public StreamResource buildStreamResource(ReportExportType exportType) {
        IPagedBeanTable pagedBeanTable = ((IListView) presenter.getView()).getPagedBeanTable();
        final Map<String, Object> additionalParameters = new HashMap<>();
        additionalParameters.put("siteUrl", AppUI.getSiteUrl());
        additionalParameters.put(SimpleReportTemplateExecutor.CRITERIA, presenter.searchCriteria);
        ReportTemplateExecutor reportTemplateExecutor;
        if (presenter.isSelectAll) {
            reportTemplateExecutor = new SimpleReportTemplateExecutor.AllItems(UserUIContext.getUserTimeZone(),
                    UserUIContext.getUserLocale(), getReportTitle(),
                    new RpFieldsBuilder(pagedBeanTable.getDisplayColumns()), exportType, getReportModelClassType(),
                    presenter.getSearchService());
        } else {
            reportTemplateExecutor = new SimpleReportTemplateExecutor.ListData(UserUIContext.getUserTimeZone(),
                    UserUIContext.getUserLocale(), getReportTitle(),
                    new RpFieldsBuilder(pagedBeanTable.getDisplayColumns()), exportType, presenter.getSelectedItems(),
                    getReportModelClassType());
        }
        return new StreamResource(new ReportStreamSource(reportTemplateExecutor) {
            @Override
            protected void initReportParameters(Map<String, Object> parameters) {
                parameters.putAll(additionalParameters);
            }
        }, exportType.getDefaultFileName());
    }

    protected abstract void onSelectExtra(String id);

    protected abstract Class<?> getReportModelClassType();

    protected abstract String getReportTitle();
}
