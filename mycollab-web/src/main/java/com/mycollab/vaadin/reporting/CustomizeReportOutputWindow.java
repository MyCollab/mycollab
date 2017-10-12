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
package com.mycollab.vaadin.reporting;

import com.mycollab.common.TableViewField;
import com.mycollab.common.domain.CustomViewStore;
import com.mycollab.common.domain.NullCustomViewStore;
import com.mycollab.common.i18n.FileI18nEnum;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.common.json.FieldDefAnalyzer;
import com.mycollab.common.service.CustomViewStoreService;
import com.mycollab.core.arguments.ValuedBean;
import com.mycollab.db.arguments.SearchCriteria;
import com.mycollab.db.persistence.service.ISearchableService;
import com.mycollab.db.query.VariableInjector;
import com.mycollab.reporting.ReportExportType;
import com.mycollab.reporting.RpFieldsBuilder;
import com.mycollab.reporting.SimpleReportTemplateExecutor;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.resources.LazyStreamSource;
import com.mycollab.vaadin.resources.OnDemandFileDownloader;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.web.ui.MailFormWindow;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Sizeable;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.*;
import org.vaadin.tepi.listbuilder.ListBuilder;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;
import org.vaadin.viritin.layouts.MWindow;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author MyCollab Ltd
 * @since 5.3.4
 */
public abstract class CustomizeReportOutputWindow<S extends SearchCriteria, B extends ValuedBean> extends MWindow {

    private VariableInjector<S> variableInjector;
    private ListBuilder listBuilder;
    private String viewId;
    private OptionGroup optionGroup;
    private Table sampleTableDisplay;

    public CustomizeReportOutputWindow(String viewId, String reportTitle, Class<B> beanCls,
                                       ISearchableService<S> searchableService, VariableInjector<S> variableInjector) {
        super(UserUIContext.getMessage(GenericI18Enum.ACTION_EXPORT));
        MVerticalLayout contentLayout = new MVerticalLayout();
        this.withModal(true).withResizable(false).withWidth("1000px").withCenter().withContent(contentLayout);
        this.viewId = viewId;
        this.variableInjector = variableInjector;

        optionGroup = new OptionGroup();
        optionGroup.addStyleName("sortDirection");
        optionGroup.addItems(UserUIContext.getMessage(FileI18nEnum.CSV), UserUIContext.getMessage(FileI18nEnum.PDF),
                UserUIContext.getMessage(FileI18nEnum.EXCEL));
        optionGroup.setValue(UserUIContext.getMessage(FileI18nEnum.CSV));
        contentLayout.with(new MHorizontalLayout(ELabel.h3(UserUIContext.getMessage(GenericI18Enum.ACTION_EXPORT)),
                optionGroup).alignAll(Alignment.MIDDLE_LEFT));

        contentLayout.with(ELabel.h3(UserUIContext.getMessage(GenericI18Enum.ACTION_SELECT_COLUMNS)));
        listBuilder = new ListBuilder();
        listBuilder.setImmediate(true);
        listBuilder.setColumns(0);
        listBuilder.setLeftColumnCaption(UserUIContext.getMessage(GenericI18Enum.OPT_AVAILABLE_COLUMNS));
        listBuilder.setRightColumnCaption(UserUIContext.getMessage(GenericI18Enum.OPT_VIEW_COLUMNS));
        listBuilder.setWidth(100, Sizeable.Unit.PERCENTAGE);
        listBuilder.setItemCaptionMode(AbstractSelect.ItemCaptionMode.EXPLICIT);
        final BeanItemContainer<TableViewField> container = new BeanItemContainer<>(TableViewField.class,
                this.getAvailableColumns());
        listBuilder.setContainerDataSource(container);
        getAvailableColumns().forEach(field -> listBuilder.setItemCaption(field, UserUIContext.getMessage(field.getDescKey())));

        final Collection<TableViewField> viewColumnIds = this.getViewColumns();
        listBuilder.setValue(viewColumnIds);
        contentLayout.with(listBuilder).withAlign(listBuilder, Alignment.TOP_CENTER);

        contentLayout.with(ELabel.h3(UserUIContext.getMessage(GenericI18Enum.ACTION_PREVIEW)));
        sampleTableDisplay = new Table();
        for (TableViewField field : getAvailableColumns()) {
            sampleTableDisplay.addContainerProperty(field.getField(), String.class, "",
                    UserUIContext.getMessage(field.getDescKey()), null, Table.Align.LEFT);
            sampleTableDisplay.setColumnWidth(field.getField(), field.getDefaultWidth());
        }
        sampleTableDisplay.setWidth("100%");
        sampleTableDisplay.addItem(buildSampleData(), 1);
        sampleTableDisplay.setPageLength(1);
        contentLayout.with(sampleTableDisplay);
        filterColumns();

        listBuilder.addValueChangeListener(valueChangeEvent -> filterColumns());

        MButton resetBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_RESET), clickEvent -> {
            listBuilder.setValue(getDefaultColumns());
            filterColumns();
        }).withStyleName(WebThemes.BUTTON_LINK);

        MButton cancelBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_CANCEL), clickEvent -> close())
                .withStyleName(WebThemes.BUTTON_OPTION);

        final MButton exportBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.ACTION_EXPORT))
                .withStyleName(WebThemes.BUTTON_ACTION).withIcon(FontAwesome.DOWNLOAD);
        OnDemandFileDownloader fileDownloader = new OnDemandFileDownloader(new LazyStreamSource() {

            @Override
            protected StreamResource.StreamSource buildStreamSource() {
                return (StreamResource.StreamSource) () -> {
                    Collection<TableViewField> columns = (Collection<TableViewField>) listBuilder.getValue();
                    // Save custom table view def
                    CustomViewStoreService customViewStoreService = AppContextUtil.getSpringBean(CustomViewStoreService.class);
                    CustomViewStore viewDef = new CustomViewStore();
                    viewDef.setSaccountid(AppUI.getAccountId());
                    viewDef.setCreateduser(UserUIContext.getUsername());
                    viewDef.setViewid(viewId);
                    viewDef.setViewinfo(FieldDefAnalyzer.toJson(new ArrayList<>(columns)));
                    customViewStoreService.saveOrUpdateViewLayoutDef(viewDef);

                    SimpleReportTemplateExecutor reportTemplateExecutor = new SimpleReportTemplateExecutor.AllItems<>(
                            UserUIContext.getUserTimeZone(), UserUIContext.getUserLocale(), reportTitle,
                            new RpFieldsBuilder(columns), getExportType(), beanCls, searchableService);
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
                return getExportType().getDefaultFileName();
            }
        });
        fileDownloader.extend(exportBtn);

        final MButton exportMailBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.ACTION_EXPORT_MAIL))
                .withStyleName(WebThemes.BUTTON_ACTION).withIcon(FontAwesome.MAIL_REPLY_ALL);
        exportMailBtn.addClickListener(clickEvent -> {
            Collection<TableViewField> columns = (Collection<TableViewField>) listBuilder.getValue();
            SimpleReportTemplateExecutor reportTemplateExecutor = new SimpleReportTemplateExecutor.AllItems<>(
                    UserUIContext.getUserTimeZone(), UserUIContext.getUserLocale(), reportTitle,
                    new RpFieldsBuilder(columns), getExportType(), beanCls, searchableService);
            Map<String, Object> parameters = new ConcurrentHashMap<>();
            parameters.put("siteUrl", AppUI.getSiteUrl());
            parameters.put("user", UserUIContext.getUser());
            parameters.put(SimpleReportTemplateExecutor.CRITERIA, variableInjector.eval());
            reportTemplateExecutor.setParameters(parameters);
            MailFormWindow mailFormWindow = new MailFormWindow(reportTemplateExecutor);
            UI.getCurrent().addWindow(mailFormWindow);
            CustomizeReportOutputWindow.this.close();
        });

        MHorizontalLayout buttonControls = new MHorizontalLayout(resetBtn, cancelBtn, exportBtn, exportMailBtn);
        contentLayout.with(buttonControls).withAlign(buttonControls, Alignment.TOP_RIGHT);
    }

    private ReportExportType getExportType() {
        String exportTypeVal = (String) optionGroup.getValue();
        if (UserUIContext.getMessage(FileI18nEnum.CSV).equals(exportTypeVal)) {
            return ReportExportType.CSV;
        } else if (UserUIContext.getMessage(FileI18nEnum.EXCEL).equals(exportTypeVal)) {
            return ReportExportType.EXCEL;
        } else {
            return ReportExportType.PDF;
        }
    }

    private void filterColumns() {
        Collection<TableViewField> columns = (Collection<TableViewField>) listBuilder.getValue();
        Collection<String> visibleColumns = new ArrayList<>();
        for (TableViewField column : columns) {
            visibleColumns.add(column.getField());
        }
        sampleTableDisplay.setVisibleColumns(visibleColumns.toArray(new String[visibleColumns.size()]));
    }

    private Collection<TableViewField> getViewColumns() {
        CustomViewStoreService customViewStoreService = AppContextUtil.getSpringBean(CustomViewStoreService.class);
        CustomViewStore viewLayoutDef = customViewStoreService.getViewLayoutDef(AppUI.getAccountId(),
                UserUIContext.getUsername(), viewId);
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
