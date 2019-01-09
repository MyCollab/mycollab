/**
 * Copyright © MyCollab
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.vaadin.web.ui.table;

import com.mycollab.common.TableViewField;
import com.mycollab.common.domain.CustomViewStore;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.common.json.FieldDefAnalyzer;
import com.mycollab.common.service.CustomViewStoreService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.ItemCaptionGenerator;
import org.tepi.listbuilder.ListBuilder;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;
import org.vaadin.viritin.layouts.MWindow;

import java.util.*;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
public abstract class CustomizedTableWindow extends MWindow {
    private static final long serialVersionUID = 1L;

    private CustomViewStoreService customViewStoreService;
    private ListBuilder<TableViewField> listBuilder;
    private AbstractPagedBeanTable<?, ?> tableItem;

    protected String viewId;

    public CustomizedTableWindow(final String viewId, final AbstractPagedBeanTable<?, ?> table) {
        super(UserUIContext.getMessage(GenericI18Enum.OPT_CUSTOMIZE_VIEW));
        this.viewId = viewId;
        this.withWidth("400px").withModal(true).withResizable(false).withCenter();

        this.tableItem = table;
        customViewStoreService = AppContextUtil.getSpringBean(CustomViewStoreService.class);

        final MVerticalLayout contentLayout = new MVerticalLayout();
        this.setContent(contentLayout);

        ListDataProvider<TableViewField> dataProvider = new ListDataProvider<>(this.getAvailableColumns());
        listBuilder = new ListBuilder<>("", dataProvider);
        listBuilder.setLeftColumnCaption(UserUIContext.getMessage(GenericI18Enum.OPT_AVAILABLE_COLUMNS));
        listBuilder.setRightColumnCaption(UserUIContext.getMessage(GenericI18Enum.OPT_VIEW_COLUMNS));
        listBuilder.setWidth(100, Sizeable.Unit.PERCENTAGE);

        listBuilder.setItemCaptionGenerator((ItemCaptionGenerator<TableViewField>) item -> UserUIContext.getMessage(item.getDescKey()));
        this.setSelectedViewColumns();
        contentLayout.with(listBuilder).withAlign(listBuilder, Alignment.TOP_CENTER);

        MButton restoreLink = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_RESET), clickEvent -> {
            Set<TableViewField> defaultSelectedColumns = new HashSet<>(tableItem.getDefaultSelectedColumns());
            if (defaultSelectedColumns != null) {
                final Set<TableViewField> selectedColumns = new HashSet<>();
                final Collection<TableViewField> itemIds = ((ListDataProvider<TableViewField>) listBuilder.getDataProvider()).getItems();

                for (TableViewField column : defaultSelectedColumns) {
                    for (TableViewField viewField : itemIds) {
                        if (column.getField().equals(viewField.getField())) {
                            selectedColumns.add(viewField);
                        }
                    }
                }

                listBuilder.setValue(selectedColumns);
            }
        }).withStyleName(WebThemes.BUTTON_LINK);
        contentLayout.with(restoreLink).withAlign(restoreLink, Alignment.TOP_RIGHT);

        final MButton saveBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_SAVE), clickEvent -> {
            Set<TableViewField> selectedColumns = listBuilder.getValue();
            table.setDisplayColumns(new ArrayList<>(selectedColumns));
            // Save custom table view def
            CustomViewStore viewDef = new CustomViewStore();
            viewDef.setSaccountid(AppUI.getAccountId());
            viewDef.setCreateduser(UserUIContext.getUsername());
            viewDef.setViewid(viewId);
            viewDef.setViewinfo(FieldDefAnalyzer.toJson(new ArrayList<>(selectedColumns)));
            customViewStoreService.saveOrUpdateViewLayoutDef(viewDef);
            close();
        }).withIcon(VaadinIcons.CLIPBOARD).withStyleName(WebThemes.BUTTON_ACTION);

        final MButton cancelBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_CANCEL), clickEvent -> close())
                .withStyleName(WebThemes.BUTTON_OPTION);

        MHorizontalLayout buttonControls = new MHorizontalLayout(cancelBtn, saveBtn);
        contentLayout.with(buttonControls).withAlign(buttonControls, Alignment.TOP_RIGHT);
    }

    private void setSelectedViewColumns() {
        final Collection<String> viewColumnIds = this.getViewColumns();

        final ListDataProvider<TableViewField> dataProvider = (ListDataProvider<TableViewField>) listBuilder.getDataProvider();
        final Collection<TableViewField> itemIds = dataProvider.getItems();
        final Set<TableViewField> selectedColumns = new HashSet<>();

        for (String viewColumnId : viewColumnIds) {
            for (TableViewField viewField : itemIds) {
                if (viewColumnId.equals(viewField.getField())) {
                    selectedColumns.add(viewField);
                }
            }
        }

        listBuilder.setValue(selectedColumns);
    }

    abstract protected Collection<TableViewField> getAvailableColumns();

    private Collection<String> getViewColumns() {
        Object[] visibleColumns = tableItem.getVisibleColumns();
        String[] copyArr = Arrays.copyOf(visibleColumns, visibleColumns.length, String[].class);
        return Arrays.asList(copyArr);
    }
}
