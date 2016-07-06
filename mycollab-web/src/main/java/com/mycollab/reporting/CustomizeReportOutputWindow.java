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
package com.mycollab.reporting;

import com.mycollab.common.TableViewField;
import com.mycollab.common.domain.CustomViewStore;
import com.mycollab.common.domain.NullCustomViewStore;
import com.mycollab.common.i18n.FileI18nEnum;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.common.json.FieldDefAnalyzer;
import com.mycollab.common.service.CustomViewStoreService;
import com.mycollab.db.arguments.SearchCriteria;
import com.mycollab.core.arguments.ValuedBean;
import com.mycollab.db.query.VariableInjector;
import com.mycollab.db.persistence.service.ISearchableService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppContext;
import com.mycollab.vaadin.resources.LazyStreamSource;
import com.mycollab.vaadin.resources.OnDemandFileDownloader;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.web.ui.UIConstants;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.Sizeable;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.*;
import org.vaadin.tepi.listbuilder.ListBuilder;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * @author MyCollab Ltd
 * @since 5.3.4
 */
public abstract class CustomizeReportOutputWindow<S extends SearchCriteria, B extends ValuedBean> extends Window {
    private VariableInjector<S> variableInjector;
    private ListBuilder listBuilder;
    private String viewId;
    private Table sampleTableDisplay;
    private ReportExportType exportType;

    public CustomizeReportOutputWindow(final String viewId, final String reportTitle, final Class<B> beanCls,
                                       final ISearchableService<S> searchableService, final VariableInjector<S> variableInjector) {
        super("Export");
        this.setModal(true);
        this.setWidth("1000px");
        this.setResizable(false);
        this.center();
        this.viewId = viewId;
        this.variableInjector = variableInjector;

        MVerticalLayout contentLayout = new MVerticalLayout();
        setContent(contentLayout);

        final OptionGroup optionGroup = new OptionGroup();
        optionGroup.addStyleName("sortDirection");
        optionGroup.addItems(AppContext.getMessage(FileI18nEnum.CSV), AppContext.getMessage(FileI18nEnum.PDF),
                AppContext.getMessage(FileI18nEnum.EXCEL));
        optionGroup.setValue(AppContext.getMessage(FileI18nEnum.CSV));
        contentLayout.with(new MHorizontalLayout(ELabel.h3(AppContext.getMessage(GenericI18Enum.ACTION_EXPORT)), optionGroup)
                .alignAll(Alignment.MIDDLE_LEFT));

        contentLayout.with(ELabel.h3(AppContext.getMessage(GenericI18Enum.ACTION_SELECT_COLUMNS)));
        listBuilder = new ListBuilder();
        listBuilder.setImmediate(true);
        listBuilder.setColumns(0);
        listBuilder.setLeftColumnCaption(AppContext.getMessage(GenericI18Enum.OPT_AVAILABLE_COLUMNS));
        listBuilder.setRightColumnCaption(AppContext.getMessage(GenericI18Enum.OPT_VIEW_COLUMNS));
        listBuilder.setWidth(100, Sizeable.Unit.PERCENTAGE);
        listBuilder.setItemCaptionMode(AbstractSelect.ItemCaptionMode.EXPLICIT);
        final BeanItemContainer<TableViewField> container = new BeanItemContainer<>(TableViewField.class, this.getAvailableColumns());
        listBuilder.setContainerDataSource(container);
        Iterator<TableViewField> iterator = getAvailableColumns().iterator();
        while (iterator.hasNext()) {
            TableViewField field = iterator.next();
            listBuilder.setItemCaption(field, AppContext.getMessage(field.getDescKey()));
        }
        final Collection<TableViewField> viewColumnIds = this.getViewColumns();
        listBuilder.setValue(viewColumnIds);
        contentLayout.with(listBuilder).withAlign(listBuilder, Alignment.TOP_CENTER);

        contentLayout.with(ELabel.h3(AppContext.getMessage(GenericI18Enum.ACTION_PREVIEW)));
        sampleTableDisplay = new Table();
        for (TableViewField field : getAvailableColumns()) {
            sampleTableDisplay.addContainerProperty(field.getField(), String.class, "", AppContext.getMessage(field.getDescKey()), null, Table.Align.LEFT);
            sampleTableDisplay.setColumnWidth(field.getField(), field.getDefaultWidth());
        }
        sampleTableDisplay.setWidth("100%");
        sampleTableDisplay.addItem(buildSampleData(), 1);
        sampleTableDisplay.setPageLength(1);
        contentLayout.with(sampleTableDisplay);
        filterColumns();

        listBuilder.addValueChangeListener(valueChangeEvent -> filterColumns());

        MButton resetBtn = new MButton(AppContext.getMessage(GenericI18Enum.BUTTON_RESET), clickEvent -> {
            listBuilder.setValue(getDefaultColumns());
            filterColumns();
        }).withStyleName(UIConstants.BUTTON_LINK);

        MButton cancelBtn = new MButton(AppContext.getMessage(GenericI18Enum.BUTTON_CANCEL), clickEvent -> close())
                .withStyleName(UIConstants.BUTTON_OPTION);


        final Button exportBtn = new Button(AppContext.getMessage(GenericI18Enum.ACTION_EXPORT));
        exportBtn.addStyleName(UIConstants.BUTTON_ACTION);
        OnDemandFileDownloader pdfFileDownloader = new OnDemandFileDownloader(new LazyStreamSource() {
            @Override
            protected StreamResource.StreamSource buildStreamSource() {
                return (StreamResource.StreamSource) () -> {
                    Collection<TableViewField> columns = (Collection<TableViewField>) listBuilder.getValue();
                    // Save custom table view def
                    CustomViewStoreService customViewStoreService = AppContextUtil.getSpringBean(CustomViewStoreService.class);
                    CustomViewStore viewDef = new CustomViewStore();
                    viewDef.setSaccountid(AppContext.getAccountId());
                    viewDef.setCreateduser(AppContext.getUsername());
                    viewDef.setViewid(viewId);
                    viewDef.setViewinfo(FieldDefAnalyzer.toJson(new ArrayList<>(columns)));
                    customViewStoreService.saveOrUpdateViewLayoutDef(viewDef);

                    SimpleReportTemplateExecutor reportTemplateExecutor = new SimpleReportTemplateExecutor.AllItems<>(reportTitle,
                            new RpFieldsBuilder(columns), exportType, beanCls, searchableService);
                    ReportStreamSource streamSource = new ReportStreamSource(reportTemplateExecutor) {
                        @Override
                        protected void initReportParameters(Map<String, Object> parameters) {
                            parameters.put(SimpleReportTemplateExecutor.CRITERIA, variableInjector.eval());
                        }
                    };
                    return streamSource.getStream();
                };
            }

            @Override
            public String getFilename() {
                String exportTypeVal = (String) optionGroup.getValue();
                if (AppContext.getMessage(FileI18nEnum.CSV).equals(exportTypeVal)) {
                    exportType = ReportExportType.CSV;
                } else if (AppContext.getMessage(FileI18nEnum.EXCEL).equals(exportTypeVal)) {
                    exportType = ReportExportType.EXCEL;
                } else {
                    exportType = ReportExportType.PDF;
                }
                return exportType.getDefaultFileName();
            }
        });
        pdfFileDownloader.extend(exportBtn);

        MHorizontalLayout buttonControls = new MHorizontalLayout(resetBtn, cancelBtn, exportBtn);
        contentLayout.with(buttonControls).withAlign(buttonControls, Alignment.TOP_RIGHT);
    }

    private void filterColumns() {
        Collection<TableViewField> columns = (Collection<TableViewField>) listBuilder.getValue();
        Collection<String> visibleColumns = new ArrayList<>();
        for (TableViewField column : columns) {
            visibleColumns.add(column.getField());
        }
        sampleTableDisplay.setVisibleColumns(visibleColumns.toArray(new String[visibleColumns.size()]));
    }

    protected Collection<TableViewField> getViewColumns() {
        CustomViewStoreService customViewStoreService = AppContextUtil.getSpringBean(CustomViewStoreService.class);
        CustomViewStore viewLayoutDef = customViewStoreService.getViewLayoutDef(AppContext.getAccountId(),
                AppContext.getUsername(), viewId);
        if (!(viewLayoutDef instanceof NullCustomViewStore)) {
            try {
                return FieldDefAnalyzer.toTableFields(viewLayoutDef.getViewinfo());
            } catch (Exception e) {
                return getDefaultColumns();
            }
        } else {
            return getDefaultColumns();
        }
    }

    abstract protected Collection<TableViewField> getDefaultColumns();

    abstract protected Collection<TableViewField> getAvailableColumns();

    abstract protected Object[] buildSampleData();
}
