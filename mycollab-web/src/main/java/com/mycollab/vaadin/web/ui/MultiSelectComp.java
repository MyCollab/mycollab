package com.mycollab.vaadin.web.ui;

import com.hp.gagawa.java.elements.Li;
import com.hp.gagawa.java.elements.Ul;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.core.utils.StringUtils;
import com.mycollab.vaadin.UserUIContext;
import com.vaadin.ui.*;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.hene.popupbutton.PopupButton;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public abstract class MultiSelectComp<T> extends CustomField<T> {
    private static final long serialVersionUID = 1L;

    private static final Logger LOG = LoggerFactory.getLogger(MultiSelectComp.class);

    private boolean canAddNew;
    protected TextField componentsText;
    protected PopupButton componentPopupSelection;
    private VerticalLayout popupContent;

    private String propertyDisplayField;
    private String widthVal;

    protected List<T> selectedItems = new ArrayList<>();
    protected List<T> items = new ArrayList<>();

    public MultiSelectComp(final String displayName, boolean canAddNew) {
        this.canAddNew = canAddNew;
        propertyDisplayField = displayName;

        componentsText = new MTextField().withNullRepresentation("").withReadOnly(true)
                .withStyleName("noBorderRight").withFullWidth();

        componentPopupSelection = new PopupButton();
        componentPopupSelection.addClickListener(clickEvent -> initContentPopup());

        popupContent = new MVerticalLayout();
        componentPopupSelection.setContent(popupContent);
    }

    abstract protected List<T> createData();

    @Override
    protected Component initContent() {
        MHorizontalLayout content = new MHorizontalLayout().withSpacing(true).withWidth(widthVal).with(componentsText)
                .withAlign(componentsText, Alignment.MIDDLE_LEFT);

        componentPopupSelection.addStyleName(WebThemes.MULTI_SELECT_BG);
        componentPopupSelection.setDirection(Alignment.TOP_LEFT);

        MHorizontalLayout multiSelectComp = new MHorizontalLayout().withSpacing(false).with(componentsText, componentPopupSelection)
                .expand(componentsText);
        content.with(multiSelectComp);

        if (canAddNew) {
            MButton newBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_ADD), clickEvent -> requestAddNewComp())
                    .withStyleName(WebThemes.BUTTON_LINK);
            content.with(newBtn);
        }
        content.expand(multiSelectComp);
        return content;
    }

    abstract protected void requestAddNewComp();

    @Override
    public void setWidth(String width) {
        super.setWidth(width);
        widthVal = width;
    }

    private void initContentPopup() {
        popupContent.removeAllComponents();
        items = createData();
        for (final T item : items) {
            final ItemSelectionComp<T> chkItem = buildItem(item);

            if (selectedItems != null) {
                for (T selectedItem : selectedItems) {
                    if (compareVal(item, selectedItem)) {
                        chkItem.setInternalVal(true);
                    }
                }
            }

            popupContent.addComponent(chkItem);
        }

        popupContent.setWidth(widthVal);
    }

    protected ItemSelectionComp<T> buildItem(final T item) {
        String itemName = "";
        if (!"".equals(propertyDisplayField)) {
            try {
                itemName = (String) PropertyUtils.getProperty(item, propertyDisplayField);
            } catch (final Exception e) {
                LOG.error("Error", e);
            }
        } else {
            itemName = item.toString();
        }

        final ItemSelectionComp<T> chkItem = new ItemSelectionComp<>(item, itemName);
        chkItem.setImmediate(true);

        chkItem.addValueChangeListener(valueChangeEvent -> {
            final Boolean value = chkItem.getValue();

            if (value && !selectedItems.contains(item)) {
                selectedItems.add(item);
            } else {
                selectedItems.remove(item);
            }

            displaySelectedItems();
        });
        return chkItem;
    }

    private void displaySelectedItems() {
        componentsText.setReadOnly(false);
        componentsText.setValue(selectedItems.size() + " selected");
        componentsText.setReadOnly(true);
        Ul ul = new Ul();
        try {
            for (T item : selectedItems) {
                String objDisplayName = (String) PropertyUtils.getProperty(item, propertyDisplayField);
                ul.appendChild(new Li().appendText(objDisplayName));
            }
        } catch (Exception e) {
            LOG.error("Error when build tooltip", e);
        }
        componentsText.setDescription(ul.write());
    }

    public void setSelectedItems(List<T> selectedValues) {
        this.selectedItems = selectedValues;
        displaySelectedItems();
    }

    private boolean compareVal(T value1, T value2) {
        if (value1 == null && value2 == null) {
            return true;
        } else if (value1 == null || value2 == null) {
            return false;
        } else {
            try {
                Integer field1 = (Integer) PropertyUtils.getProperty(value1, "id");
                Integer field2 = (Integer) PropertyUtils.getProperty(value2, "id");
                return field1.equals(field2);
            } catch (final Exception e) {
                LOG.error("Error when compare value", e);
                return false;
            }
        }
    }

    public List<T> getSelectedItems() {
        return this.selectedItems;
    }

    public static class ItemSelectionComp<T> extends CheckBox {
        private static final long serialVersionUID = 1L;

        private T item;

        public ItemSelectionComp(T item, String caption) {
            super();
            this.item = item;
            this.setCaption(StringUtils.trim(caption, 25, true));
        }

        void setInternalVal(Boolean val) {
            this.setInternalValue(val);
        }
    }
}
