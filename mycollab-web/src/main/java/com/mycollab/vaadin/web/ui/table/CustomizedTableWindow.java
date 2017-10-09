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
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import org.vaadin.tepi.listbuilder.ListBuilder;
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
    private ListBuilder listBuilder;
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

        listBuilder = new ListBuilder();
        listBuilder.setImmediate(true);
        listBuilder.setColumns(0);
        listBuilder.setLeftColumnCaption(UserUIContext.getMessage(GenericI18Enum.OPT_AVAILABLE_COLUMNS));
        listBuilder.setRightColumnCaption(UserUIContext.getMessage(GenericI18Enum.OPT_VIEW_COLUMNS));
        listBuilder.setWidth(100, Sizeable.Unit.PERCENTAGE);
        listBuilder.setItemCaptionMode(ItemCaptionMode.EXPLICIT);
        final BeanItemContainer<TableViewField> container = new BeanItemContainer<>(TableViewField.class, this.getAvailableColumns());
        listBuilder.setContainerDataSource(container);
        Iterator<TableViewField> iterator = getAvailableColumns().iterator();
        while (iterator.hasNext()) {
            TableViewField field = iterator.next();
            listBuilder.setItemCaption(field, UserUIContext.getMessage(field.getDescKey()));
        }
        this.setSelectedViewColumns();
        contentLayout.with(listBuilder).withAlign(listBuilder, Alignment.TOP_CENTER);

        MButton restoreLink = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_RESET), clickEvent -> {
            List<TableViewField> defaultSelectedColumns = tableItem.getDefaultSelectedColumns();
            if (defaultSelectedColumns != null) {
                final List<TableViewField> selectedColumns = new ArrayList<>();
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
        }).withStyleName(WebThemes.BUTTON_LINK);
        contentLayout.with(restoreLink).withAlign(restoreLink, Alignment.TOP_RIGHT);

        final MButton saveBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_SAVE), clickEvent -> {
            List<TableViewField> selectedColumns = (List<TableViewField>) listBuilder.getValue();
            table.setDisplayColumns(selectedColumns);
            // Save custom table view def
            CustomViewStore viewDef = new CustomViewStore();
            viewDef.setSaccountid(AppUI.getAccountId());
            viewDef.setCreateduser(UserUIContext.getUsername());
            viewDef.setViewid(viewId);
            viewDef.setViewinfo(FieldDefAnalyzer.toJson(new ArrayList<>(selectedColumns)));
            customViewStoreService.saveOrUpdateViewLayoutDef(viewDef);
            close();
        }).withIcon(FontAwesome.SAVE).withStyleName(WebThemes.BUTTON_ACTION);

        final MButton cancelBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_CANCEL), clickEvent -> close())
                .withStyleName(WebThemes.BUTTON_OPTION);

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
