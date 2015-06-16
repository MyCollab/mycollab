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
package com.esofthead.mycollab.vaadin.desktop.ui;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.configuration.SiteConfiguration;
import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.reporting.ReportExportType;
import com.esofthead.mycollab.reporting.RpParameterBuilder;
import com.esofthead.mycollab.reporting.SimpleGridExportItemsStreamResource;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.MassItemActionHandler;
import com.esofthead.mycollab.vaadin.events.ViewItemAction;
import com.esofthead.mycollab.vaadin.ui.ConfirmDialogExt;
import com.esofthead.mycollab.vaadin.ui.table.AbstractPagedBeanTable;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.UI;
import org.vaadin.dialogs.ConfirmDialog;

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
            ConfirmDialogExt.show(UI.getCurrent(),
                    AppContext.getMessage(GenericI18Enum.DIALOG_DELETE_TITLE,
                            SiteConfiguration.getSiteName()),
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
        String exportFileName;
        switch (exportType) {
            case CSV:
                exportFileName = "export.csv";
                break;
            case PDF:
                exportFileName = "export.pdf";
                break;
            case EXCEL:
                exportFileName = "export.xlsx";
                break;
            default:
                throw new MyCollabException("Do not support export type " + exportType);
        }

        AbstractPagedBeanTable pagedBeanTable = ((ListView) presenter
                .getView()).getPagedBeanTable();
        if (presenter.isSelectAll) {
            return new StreamResource(
                    new SimpleGridExportItemsStreamResource.AllItems(
                            getReportTitle(), new RpParameterBuilder(
                            pagedBeanTable.getDisplayColumns()),
                            exportType,
                            presenter.getSearchService(),
                            presenter.searchCriteria,
                            getReportModelClassType()), exportFileName);
        } else {
            return new StreamResource(
                    new SimpleGridExportItemsStreamResource.ListData(
                            getReportTitle(), new RpParameterBuilder(
                            pagedBeanTable.getDisplayColumns()),
                            exportType,
                            presenter.getSelectedItems(),
                            getReportModelClassType()), exportFileName);
        }
    }

    protected abstract void onSelectExtra(String id);

    protected abstract Class<?> getReportModelClassType();

    protected abstract String getReportTitle();
}
