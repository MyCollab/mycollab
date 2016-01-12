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
package com.esofthead.mycollab.vaadin.web.ui;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.reporting.*;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.MassItemActionHandler;
import com.esofthead.mycollab.vaadin.events.ViewItemAction;
import com.esofthead.mycollab.vaadin.web.ui.table.AbstractPagedBeanTable;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.UI;
import org.vaadin.dialogs.ConfirmDialog;

import java.util.HashMap;
import java.util.Map;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
@SuppressWarnings("rawtypes")
public abstract class DefaultMassEditActionHandler implements MassItemActionHandler {
    private ListSelectionPresenter presenter;

    public DefaultMassEditActionHandler(ListSelectionPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onSelect(String id) {
        if (ViewItemAction.DELETE_ACTION().equals(id)) {
            ConfirmDialogExt.show(UI.getCurrent(), AppContext.getMessage(GenericI18Enum.DIALOG_DELETE_TITLE,
                            AppContext.getSiteName()),
                    AppContext.getMessage(GenericI18Enum.DIALOG_DELETE_MULTIPLE_ITEMS_MESSAGE),
                    AppContext.getMessage(GenericI18Enum.BUTTON_YES),
                    AppContext.getMessage(GenericI18Enum.BUTTON_NO),
                    new ConfirmDialog.Listener() {
                        private static final long serialVersionUID = 1L;

                        @Override
                        public void onClose(ConfirmDialog dialog) {
                            if (dialog.isConfirmed()) {
                                presenter.deleteSelectedItems();
                            }
                        }
                    });
        } else {
            onSelectExtra(id);
        }

    }

    @SuppressWarnings("unchecked")
    @Override
    public StreamResource buildStreamResource(ReportExportType exportType) {
        AbstractPagedBeanTable pagedBeanTable = ((ListView) presenter.getView()).getPagedBeanTable();
        final Map<String, Object> parameters = new HashMap<>();
        parameters.put("siteUrl", AppContext.getSiteUrl());
        parameters.put(SimpleReportTemplateExecutor.CRITERIA, presenter.searchCriteria);
        ReportTemplateExecutor reportTemplateExecutor;
        if (presenter.isSelectAll) {
            reportTemplateExecutor = new SimpleReportTemplateExecutor.AllItems(getReportTitle(),
                    new RpFieldsBuilder(pagedBeanTable.getDisplayColumns()), exportType, getReportModelClassType(),
                    presenter.getSearchService());
        } else {
            reportTemplateExecutor = new SimpleReportTemplateExecutor.ListData(getReportTitle(),
                    new RpFieldsBuilder(pagedBeanTable.getDisplayColumns()), exportType, presenter.getSelectedItems(),
                    getReportModelClassType());
        }
        return new StreamResource(new ReportStreamSource(reportTemplateExecutor) {
            @Override
            protected Map<String, Object> initReportParameters() {
                return parameters;
            }
        }, exportType.getDefaultFileName());
    }

    protected abstract void onSelectExtra(String id);

    protected abstract Class<?> getReportModelClassType();

    protected abstract String getReportTitle();
}
