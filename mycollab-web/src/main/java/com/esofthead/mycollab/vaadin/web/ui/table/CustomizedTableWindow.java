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
package com.esofthead.mycollab.vaadin.web.ui.table;

import com.esofthead.mycollab.common.TableViewField;
import com.esofthead.mycollab.common.domain.CustomViewStore;
import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.common.json.FieldDefAnalyzer;
import com.esofthead.mycollab.common.service.CustomViewStoreService;
import com.esofthead.mycollab.spring.AppContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.web.ui.UIConstants;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Window;
import org.vaadin.tepi.listbuilder.ListBuilder;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.util.*;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
public abstract class CustomizedTableWindow extends Window {
    private static final long serialVersionUID = 1L;

    private CustomViewStoreService customViewStoreService;
    private ListBuilder listBuilder;
    private AbstractPagedBeanTable<?, ?> tableItem;

    protected String viewId;

    public CustomizedTableWindow(final String viewId, final AbstractPagedBeanTable<?, ?> table) {
        super(AppContext.getMessage(GenericI18Enum.OPT_CUSTOMIZE_VIEW));
        this.viewId = viewId;
        this.setWidth("400px");
        this.setResizable(false);
        this.setModal(true);
        this.center();

        this.tableItem = table;
        customViewStoreService = AppContextUtil.getSpringBean(CustomViewStoreService.class);

        final MVerticalLayout contentLayout = new MVerticalLayout();
        this.setContent(contentLayout);

        listBuilder = new ListBuilder();
        listBuilder.setImmediate(true);
        listBuilder.setColumns(0);
        listBuilder.setLeftColumnCaption(AppContext.getMessage(GenericI18Enum.OPT_AVAILABLE_COLUMNS));
        listBuilder.setRightColumnCaption(AppContext.getMessage(GenericI18Enum.OPT_VIEW_COLUMNS));
        listBuilder.setWidth(100, Sizeable.Unit.PERCENTAGE);
        listBuilder.setItemCaptionMode(ItemCaptionMode.EXPLICIT);
        final BeanItemContainer<TableViewField> container = new BeanItemContainer<>(TableViewField.class, this.getAvailableColumns());
        listBuilder.setContainerDataSource(container);
        Iterator<TableViewField> iterator = getAvailableColumns().iterator();
        while (iterator.hasNext()) {
            TableViewField field = iterator.next();
            listBuilder.setItemCaption(field, AppContext.getMessage(field.getDescKey()));
        }
        this.setSelectedViewColumns();
        contentLayout.with(listBuilder).withAlign(listBuilder, Alignment.TOP_CENTER);

        Button restoreLink = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_RESET), new Button.ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(ClickEvent event) {
                List<TableViewField> defaultSelectedColumns = tableItem.getDefaultSelectedColumns();
                if (defaultSelectedColumns != null) {
                    final List<TableViewField> selectedColumns = new ArrayList<>();
                    final BeanItemContainer<TableViewField> container = (BeanItemContainer<TableViewField>) listBuilder.getContainerDataSource();
                    final Collection<TableViewField> itemIds = container.getItemIds();

                    for (TableViewField column : defaultSelectedColumns) {
                        for (final TableViewField viewField : itemIds) {
                            if (column.getField().equals(viewField.getField())) {
                                selectedColumns.add(viewField);
                            }
                        }
                    }

                    listBuilder.setValue(selectedColumns);
                }

            }
        });
        restoreLink.setStyleName(UIConstants.BUTTON_LINK);
        contentLayout.with(restoreLink).withAlign(restoreLink, Alignment.TOP_RIGHT);

        final Button saveBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_SAVE), new Button.ClickListener() {
            private static final long serialVersionUID = 1L;

            @SuppressWarnings("unchecked")
            @Override
            public void buttonClick(final ClickEvent event) {
                List<TableViewField> selectedColumns = (List<TableViewField>) listBuilder.getValue();
                table.setDisplayColumns(selectedColumns);
                // Save custom table view def
                CustomViewStore viewDef = new CustomViewStore();
                viewDef.setSaccountid(AppContext.getAccountId());
                viewDef.setCreateduser(AppContext.getUsername());
                viewDef.setViewid(viewId);
                viewDef.setViewinfo(FieldDefAnalyzer.toJson(new ArrayList<>(selectedColumns)));
                customViewStoreService.saveOrUpdateViewLayoutDef(viewDef);
                close();
            }
        });
        saveBtn.setStyleName(UIConstants.BUTTON_ACTION);
        saveBtn.setIcon(FontAwesome.SAVE);

        final Button cancelBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_CANCEL), new Button.ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(final ClickEvent event) {
                close();
            }
        });
        cancelBtn.setStyleName(UIConstants.BUTTON_OPTION);

        MHorizontalLayout buttonControls = new MHorizontalLayout(cancelBtn, saveBtn);
        contentLayout.with(buttonControls).withAlign(buttonControls, Alignment.TOP_RIGHT);
    }

    private void setSelectedViewColumns() {
        final Collection<String> viewColumnIds = this.getViewColumns();

        final BeanItemContainer<TableViewField> container = (BeanItemContainer<TableViewField>) listBuilder.getContainerDataSource();
        final Collection<TableViewField> itemIds = container.getItemIds();
        final List<TableViewField> selectedColumns = new ArrayList<>();

        for (String viewColumnId : viewColumnIds) {
            for (final TableViewField viewField : itemIds) {
                if (viewColumnId.equals(viewField.getField())) {
                    selectedColumns.add(viewField);
                }
            }
        }

        listBuilder.setValue(selectedColumns);
    }

    abstract protected Collection<TableViewField> getAvailableColumns();

    protected Collection<String> getViewColumns() {
        Object[] visibleColumns = tableItem.getVisibleColumns();
        String[] copyArr = Arrays.copyOf(visibleColumns, visibleColumns.length, String[].class);
        return Arrays.asList(copyArr);
    }
}
