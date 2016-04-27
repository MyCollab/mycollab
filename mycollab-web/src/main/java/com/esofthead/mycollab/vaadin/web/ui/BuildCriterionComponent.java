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

import com.esofthead.mycollab.common.domain.SaveSearchResultWithBLOBs;
import com.esofthead.mycollab.common.domain.criteria.SaveSearchResultCriteria;
import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.common.service.SaveSearchResultService;
import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.core.UserInvalidInputException;
import com.esofthead.mycollab.core.arguments.*;
import com.esofthead.mycollab.core.db.query.*;
import com.esofthead.mycollab.core.utils.XStreamJsonDeSerializer;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.DateFieldExt;
import com.esofthead.mycollab.vaadin.ui.I18nValueListSelect;
import com.esofthead.mycollab.vaadin.ui.ValueListSelect;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.lang.reflect.Array;
import java.util.*;

/**
 * @author MyCollab Ltd.
 * @since 4.0
 */
public class BuildCriterionComponent<S extends SearchCriteria> extends MVerticalLayout {
    private static final long serialVersionUID = 1L;

    private static final Logger LOG = LoggerFactory.getLogger(BuildCriterionComponent.class);

    private Param[] paramFields;
    private String searchCategory;
    private Class<S> type;

    private MHorizontalLayout filterBox;
    private MVerticalLayout searchContainer;

    public BuildCriterionComponent(Param[] paramFields, Class<S> type, String searchCategory) {
        this.paramFields = paramFields;
        this.type = type;
        this.searchCategory = searchCategory;

        MHorizontalLayout headerBox = new MHorizontalLayout().withMargin(new MarginInfo(true, false, true, true));
        headerBox.setDefaultComponentAlignment(Alignment.TOP_LEFT);
        addComponent(headerBox);

        Label filterLbl = new Label(AppContext.getMessage(GenericI18Enum.OPT_SAVED_FILTER));
        headerBox.with(filterLbl).withAlign(filterLbl, Alignment.TOP_LEFT);

        filterBox = new MHorizontalLayout();
        headerBox.with(filterBox).withAlign(filterBox, Alignment.TOP_LEFT);

        buildFilterBox(null);

        searchContainer = new MVerticalLayout().withMargin(false);
        searchContainer.setDefaultComponentAlignment(Alignment.TOP_LEFT);

        MHorizontalLayout controlsBtn = new MHorizontalLayout().withMargin(true);

        Button addCriteriaBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_ADD_CRITERIA), new Button.ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(ClickEvent event) {
                CriteriaSelectionLayout newCriteriaBar = new CriteriaSelectionLayout(searchContainer.getComponentCount() + 1);
                searchContainer.addComponent(newCriteriaBar);
            }
        });
        addCriteriaBtn.setStyleName(UIConstants.BUTTON_ACTION);
        addCriteriaBtn.setIcon(FontAwesome.PLUS);

        controlsBtn.with(addCriteriaBtn);

        this.with(searchContainer, controlsBtn);
    }

    private void buildFilterBox(String queryname) {
        filterBox.removeAllComponents();

        SavedSearchResultComboBox filterComboBox = new SavedSearchResultComboBox();
        filterBox.addComponent(filterComboBox);

        Button saveSearchBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_NEW_FILTER), new Button.ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(ClickEvent event) {
                buildSaveFilterBox();
            }
        });
        saveSearchBtn.addStyleName(UIConstants.BUTTON_ACTION);
        saveSearchBtn.setIcon(FontAwesome.PLUS);
        filterBox.addComponent(saveSearchBtn);
    }

    private void buildSaveFilterBox() {
        filterBox.removeAllComponents();

        final TextField queryTextField = new TextField();
        filterBox.addComponent(queryTextField);

        Button saveBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_SAVE), new Button.ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(ClickEvent event) {
                String queryText = queryTextField.getValue();
                saveSearchCriteria(queryText);
            }
        });
        saveBtn.setStyleName(UIConstants.BUTTON_ACTION);
        saveBtn.setIcon(FontAwesome.SAVE);
        filterBox.addComponent(saveBtn);

        Button cancelBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_CANCEL), new Button.ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(ClickEvent event) {
                buildFilterBox(null);
            }
        });
        cancelBtn.addStyleName(UIConstants.BUTTON_OPTION);
        filterBox.addComponent(cancelBtn);
    }

    private void saveSearchCriteria(String queryText) {
        List<SearchFieldInfo> fieldInfos = buildSearchFieldInfos();

        if (CollectionUtils.isEmpty(fieldInfos)) {
            throw new UserInvalidInputException("You must select at least one search criteria");
        }

        SaveSearchResultService saveSearchResultService = ApplicationContextUtil.getSpringBean(SaveSearchResultService.class);
        SaveSearchResultWithBLOBs searchResult = new SaveSearchResultWithBLOBs();
        searchResult.setSaveuser(AppContext.getUsername());
        searchResult.setSaccountid(AppContext.getAccountId());
        searchResult.setQuerytext(XStreamJsonDeSerializer.toJson(fieldInfos));
        searchResult.setType(searchCategory);
        searchResult.setQueryname(queryText);
        saveSearchResultService.saveWithSession(searchResult, AppContext.getUsername());
        buildFilterBox(queryText);
    }

    private List<SearchFieldInfo> buildSearchFieldInfos() {
        Iterator<Component> iterator = searchContainer.iterator();
        List<SearchFieldInfo> fieldInfos = new ArrayList<>();
        while (iterator.hasNext()) {
            CriteriaSelectionLayout bar = (CriteriaSelectionLayout) iterator.next();
            SearchFieldInfo searchFieldInfo = bar.buildSearchFieldInfo();
            if (searchFieldInfo != null) {
                fieldInfos.add(searchFieldInfo);
            }
        }
        return fieldInfos;
    }

    protected Component buildPropertySearchComp(String fieldId) {
        return null;
    }

    public S fillupSearchCriteria() {
        try {
            S searchCriteria = type.newInstance();
            Iterator<Component> iterator = searchContainer.iterator();
            while (iterator.hasNext()) {
                CriteriaSelectionLayout bar = (CriteriaSelectionLayout) iterator.next();
                SearchField searchField = bar.buildSearchField();
                if (searchField != null) {
                    searchCriteria.addExtraField(searchField);
                }
            }
            return searchCriteria;
        } catch (Exception e) {
            throw new MyCollabException(e);
        }
    }

    private void fillSearchFieldInfoAndInvokeSearchRequest(List<SearchFieldInfo> searchFieldInfos) {
        searchContainer.removeAllComponents();

        try {
            for (int i = 0; i < searchFieldInfos.size(); i++) {
                SearchFieldInfo searchFieldInfo = searchFieldInfos.get(i);
                CriteriaSelectionLayout newCriteriaBar = new CriteriaSelectionLayout(searchContainer.getComponentCount() + 1);

                newCriteriaBar.fillSearchFieldInfo(searchFieldInfo);
                searchContainer.addComponent(newCriteriaBar);
            }
        } catch (Exception e) {
            LOG.error("Error while build criterion", e);
        }
    }

    private class CriteriaSelectionLayout extends GridLayout {
        private static final long serialVersionUID = 1L;

        private int index;
        private Label indexLbl;
        private ValueComboBox operatorSelectionBox;
        private ComboBox fieldSelectionBox;
        private ValueComboBox compareSelectionBox;
        private MVerticalLayout valueBox;
        private Button deleteBtn;

        public CriteriaSelectionLayout(int index) {
            super(6, 1);
            this.index = index;
            this.setSpacing(true);
            this.setMargin(new MarginInfo(false, true, false, true));
            this.setDefaultComponentAlignment(Alignment.TOP_LEFT);

            indexLbl = new Label(index + "");
            indexLbl.setWidth("70px");
            this.addComponent(indexLbl, 0, 0);

            if (index == 1) {
                Label placeHolder = new Label("&nbsp;", ContentMode.HTML);
                placeHolder.setWidth("90px");
                this.addComponent(placeHolder, 1, 0);
            } else {
                operatorSelectionBox = new ValueComboBox(false, SearchField.AND, SearchField.OR);
                operatorSelectionBox.setWidth("90px");
                this.addComponent(operatorSelectionBox, 1, 0);
            }
            buildFieldSelectionBox();

            valueBox = new MVerticalLayout().withMargin(false).withWidth("300px");
            deleteBtn = new Button("", new Button.ClickListener() {
                private static final long serialVersionUID = 1L;

                @SuppressWarnings("unchecked")
                @Override
                public void buttonClick(ClickEvent event) {
                    int compIndex = searchContainer.getComponentIndex(CriteriaSelectionLayout.this);
                    searchContainer.removeComponent(CriteriaSelectionLayout.this);
                    for (int i = compIndex; i < searchContainer.getComponentCount(); i++) {
                        CriteriaSelectionLayout searchCriteriaLayout = (CriteriaSelectionLayout) searchContainer.getComponent(i);
                        searchCriteriaLayout.updateIndex();
                    }
                }
            });
            deleteBtn.addStyleName(UIConstants.BUTTON_ICON_ONLY);
            deleteBtn.setIcon(FontAwesome.TRASH_O);

            this.addComponent(fieldSelectionBox, 2, 0);
            this.addComponent(compareSelectionBox, 3, 0);
            this.addComponent(valueBox, 4, 0);
            this.addComponent(deleteBtn, 5, 0);
        }

        private void updateIndex() {
            index--;
            indexLbl.setValue(index + "");
            if (index == 1) {
                operatorSelectionBox.setVisible(false);
            }
        }

        private void fillSearchFieldInfo(SearchFieldInfo searchFieldInfo) {
            String width = "300px";
            if (operatorSelectionBox != null) {
                operatorSelectionBox.setValue(searchFieldInfo.getPrefixOper());
            }

            Param param = searchFieldInfo.getParam();
            Collection<?> itemIds = fieldSelectionBox.getItemIds();
            for (Object item : itemIds) {
                if (param.equals(item)) {
                    fieldSelectionBox.setValue(item);
                    break;
                }
            }

            compareSelectionBox.setValue(searchFieldInfo.getCompareOper());
            valueBox.removeAllComponents();

            if (param instanceof StringParam || param instanceof ConcatStringParam) {
                TextField valueField = new TextField();
                valueField.setValue((String) searchFieldInfo.eval());
                valueField.setWidth(width);
                valueBox.addComponent(valueField);
            } else if (param instanceof NumberParam) {
                TextField valueField = new TextField();
                valueField.setValue(String.valueOf(searchFieldInfo.eval()));
                valueField.setWidth(width);
                valueBox.addComponent(valueField);
            } else if (param instanceof DateParam) {
                String compareItem = (String) compareSelectionBox.getValue();
                if (DateParam.BETWEEN.equals(compareItem) || DateParam.NOT_BETWEEN.equals(compareItem)) {
                    DateFieldExt field1 = new DateFieldExt();
                    field1.setValue((Date) Array.get(searchFieldInfo.eval(), 0));
                    field1.setWidth(width);
                    DateFieldExt field2 = new DateFieldExt();
                    field2.setValue((Date) Array.get(searchFieldInfo.eval(), 1));
                    field2.setWidth(width);
                    valueBox.with(field1, field2);
                } else {
                    DateFieldExt field = new DateFieldExt();
                    field.setValue((Date) searchFieldInfo.eval());
                    field.setWidth(width);
                    valueBox.addComponent(field);
                }
            } else if (param instanceof PropertyParam || param instanceof PropertyListParam || param instanceof CustomSqlParam) {
                Component comp = buildPropertySearchComp(param.getId());
                if (comp != null) {
                    if (comp instanceof CustomField<?> && (((CustomField) comp).getType() == Integer.class)) {
                        ((Field) comp).setValue(Integer.parseInt(searchFieldInfo.eval() + ""));
                    } else {
                        ((Field) comp).setValue(searchFieldInfo.eval());
                    }
                    comp.setWidth(width);
                    valueBox.addComponent(comp);
                }
            } else if (param instanceof StringListParam) {
                ValueListSelect listSelect = new ValueListSelect();
                listSelect.setCaption(null);
                listSelect.loadData(((StringListParam) param).getValues().toArray(new String[0]));
                listSelect.setValue(searchFieldInfo.eval());
                listSelect.setWidth(width);
                valueBox.addComponent(listSelect);

            } else if (param instanceof I18nStringListParam) {
                I18nValueListSelect listSelect = new I18nValueListSelect();
                listSelect.setCaption(null);
                listSelect.loadData(((I18nStringListParam) param).getLstValues());
                listSelect.setValue(searchFieldInfo.eval());
                listSelect.setWidth(width);
                valueBox.addComponent(listSelect);

            } else if (param instanceof CompositionStringParam) {
                TextField tempTextField = new TextField();
                tempTextField.setValue(String.valueOf(searchFieldInfo.eval()));
                tempTextField.setWidth(width);
                valueBox.addComponent(tempTextField);
            }
        }

        private void buildFieldSelectionBox() {
            fieldSelectionBox = new ComboBox();
            fieldSelectionBox.setImmediate(true);
            fieldSelectionBox.setWidth("200px");
            fieldSelectionBox.setItemCaptionMode(ItemCaptionMode.EXPLICIT);
            for (Param field : paramFields) {
                fieldSelectionBox.addItem(field);
                fieldSelectionBox.setItemCaption(field, AppContext.getMessage(field.getDisplayName()));
            }

            fieldSelectionBox.addValueChangeListener(new ValueChangeListener() {
                private static final long serialVersionUID = 1L;

                @Override
                public void valueChange(ValueChangeEvent event) {
                    compareSelectionBox.removeAllItems();

                    Param field = (Param) fieldSelectionBox.getValue();
                    if (field != null) {
                        if (field instanceof StringParam) {
                            compareSelectionBox.loadData(StringParam.OPTIONS);
                        } else if (field instanceof NumberParam) {
                            compareSelectionBox.loadData(NumberParam.OPTIONS);
                        } else if (field instanceof DateParam) {
                            compareSelectionBox.loadData(DateParam.OPTIONS);
                        } else if (field instanceof PropertyParam) {
                            compareSelectionBox.loadData(PropertyParam.OPTIONS);
                        } else if (field instanceof PropertyListParam || field instanceof CustomSqlParam) {
                            compareSelectionBox.loadData(PropertyListParam.OPTIONS);
                        } else if (field instanceof StringListParam) {
                            compareSelectionBox.loadData(StringListParam.OPTIONS);
                        } else if (field instanceof I18nStringListParam) {
                            compareSelectionBox.loadData(I18nStringListParam.OPTIONS);
                        } else if (field instanceof CompositionStringParam) {
                            compareSelectionBox.loadData(StringParam.OPTIONS);
                        } else if (field instanceof ConcatStringParam) {
                            compareSelectionBox.loadData(ConcatStringParam.OPTIONS);
                        }

                        displayAssociateInputField((Param) fieldSelectionBox.getValue());
                    }
                }
            });

            compareSelectionBox = new ValueComboBox(false, "");
            compareSelectionBox.setWidth("150px");
            compareSelectionBox.setImmediate(true);
            compareSelectionBox.addValueChangeListener(new ValueChangeListener() {
                private static final long serialVersionUID = 1L;

                @Override
                public void valueChange(ValueChangeEvent event) {
                    displayAssociateInputField((Param) fieldSelectionBox.getValue());
                }
            });
        }

        private void displayAssociateInputField(Param field) {
            String width = "300px";
            String compareItem = (String) compareSelectionBox.getValue();
            valueBox.removeAllComponents();

            if (field instanceof StringParam || field instanceof ConcatStringParam) {
                TextField tempTextField = new TextField();
                tempTextField.setWidth(width);
                valueBox.addComponent(tempTextField);
            } else if (field instanceof NumberParam) {
                TextField tempTextField = new TextField();
                tempTextField.setWidth(width);
                valueBox.addComponent(tempTextField);
            } else if (field instanceof DateParam) {
                if (DateParam.BETWEEN.equals(compareItem) || DateParam.NOT_BETWEEN.equals(compareItem)) {
                    DateField field1 = new DateField();
                    DateField field2 = new DateField();
                    field1.setWidth(width);
                    field2.setWidth(width);
                    valueBox.addComponent(field1);
                    valueBox.addComponent(field2);
                } else {
                    DateField tempDateField = new DateField();
                    tempDateField.setWidth(width);
                    valueBox.addComponent(tempDateField);
                }
            } else if (field instanceof PropertyParam || field instanceof PropertyListParam || field instanceof CustomSqlParam) {
                Component comp = buildPropertySearchComp(field.getId());
                if (comp != null) {
                    comp.setWidth(width);
                    valueBox.addComponent(comp);
                }
            } else if (field instanceof StringListParam) {
                ValueListSelect listSelect = new ValueListSelect();
                listSelect.setCaption(null);
                listSelect.loadData(((StringListParam) field).getValues().toArray(new String[0]));
                listSelect.setWidth(width);
                valueBox.addComponent(listSelect);
            } else if (field instanceof I18nStringListParam) {
                I18nValueListSelect listSelect = new I18nValueListSelect();
                listSelect.setCaption(null);
                listSelect.loadData(((I18nStringListParam) field).getLstValues());
                listSelect.setWidth(width);
                valueBox.addComponent(listSelect);
            } else if (field instanceof CompositionStringParam) {
                TextField tempTextField = new TextField();
                tempTextField.setWidth(width);
                valueBox.addComponent(tempTextField);
            }
        }

        private SearchFieldInfo buildSearchFieldInfo() {
            String prefixOper = (operatorSelectionBox != null) ? (String) operatorSelectionBox.getValue() : "AND";
            Param param = (Param) fieldSelectionBox.getValue();
            String compareOper = (String) compareSelectionBox.getValue();
            Object value;
            int componentCount = valueBox.getComponentCount();
            if (componentCount == 1) {
                Field<?> component = (Field<?>) valueBox.getComponent(0);
                value = component.getValue();
            } else if (componentCount > 1) {
                value = new Object[componentCount];
                for (int i = 0; i < componentCount; i++) {
                    Array.set(value, i, ((Field<?>) valueBox.getComponent(i)).getValue());
                }
            } else {
                return null;
            }

            return new SearchFieldInfo(prefixOper, param, compareOper, value);
        }

        private SearchField buildSearchField() {
            Param param = (Param) fieldSelectionBox.getValue();
            String prefixOperation = (operatorSelectionBox != null) ? (String) operatorSelectionBox.getValue() : "AND";
            if (param != null) {
                String compareOper = (String) compareSelectionBox.getValue();

                if (param instanceof StringParam) {
                    if (valueBox.getComponentCount() != 1) {
                        return null;
                    }
                    TextField field = (TextField) valueBox.getComponent(0);
                    String value = field.getValue();
                    StringParam wrapParam = (StringParam) param;
                    return wrapParam.buildSearchField(prefixOperation, compareOper, value);
                } else if (param instanceof StringListParam) {
                    if (valueBox.getComponentCount() != 1) {
                        return null;
                    }
                    ValueListSelect field = (ValueListSelect) valueBox.getComponent(0);
                    Collection<?> value = (Collection<?>) field.getValue();
                    if (CollectionUtils.isEmpty(value)) {
                        return null;
                    }

                    StringListParam wrapParam = (StringListParam) param;

                    switch (compareOper) {
                        case StringListParam.IN:
                            return wrapParam.buildStringParamInList(prefixOperation, value);
                        case StringListParam.NOT_IN:
                            return wrapParam.buildStringParamNotInList(prefixOperation, value);
                        default:
                            throw new MyCollabException("Not support yet");
                    }
                } else if (param instanceof I18nStringListParam) {
                    if (valueBox.getComponentCount() != 1) {
                        return null;
                    }
                    I18nValueListSelect field = (I18nValueListSelect) valueBox.getComponent(0);
                    Collection<?> value = (Collection<?>) field.getValue();
                    if (CollectionUtils.isEmpty(value)) {
                        return null;
                    }

                    I18nStringListParam wrapParam = (I18nStringListParam) param;

                    switch (compareOper) {
                        case StringListParam.IN:
                            return wrapParam.buildStringParamInList(prefixOperation, value);
                        case StringListParam.NOT_IN:
                            return wrapParam.buildStringParamNotInList(prefixOperation, value);
                        default:
                            throw new MyCollabException("Not support yet");
                    }
                } else if (param instanceof NumberParam) {
                    if (valueBox.getComponentCount() != 1) {
                        return null;
                    }
                    TextField field = (TextField) valueBox.getComponent(0);
                    Number value = 0;
                    try {
                        value = Double.parseDouble(field.getValue());
                    } catch (Exception e) {

                    }

                    NumberParam wrapParam = (NumberParam) param;
                    return wrapParam.buildSearchField(prefixOperation, compareOper, value);
                } else if (param instanceof PropertyListParam) {
                    if (valueBox.getComponentCount() != 1) {
                        return null;
                    }

                    ListSelect field = (ListSelect) valueBox.getComponent(0);
                    Collection<?> value = (Collection<?>) field.getValue();
                    if (CollectionUtils.isEmpty(value)) {
                        return null;
                    }
                    PropertyListParam wrapParam = (PropertyListParam) param;
                    switch (compareOper) {
                        case PropertyListParam.BELONG_TO:
                            return wrapParam.buildPropertyParamInList(prefixOperation, value);
                        case PropertyListParam.NOT_BELONG_TO:
                            return wrapParam.buildPropertyParamNotInList(prefixOperation, value);
                        default:
                            throw new MyCollabException("Not support yet");
                    }
                } else if (param instanceof CustomSqlParam) {
                    if (valueBox.getComponentCount() != 1) {
                        return null;
                    }

                    ListSelect field = (ListSelect) valueBox.getComponent(0);
                    Collection<?> value = (Collection<?>) field.getValue();
                    if (CollectionUtils.isEmpty(value)) {
                        return null;
                    }
                    CustomSqlParam wrapParam = (CustomSqlParam) param;
                    switch (compareOper) {
                        case PropertyListParam.BELONG_TO:
                            return wrapParam.buildPropertyParamInList(prefixOperation, value);
                        case PropertyListParam.NOT_BELONG_TO:
                            return wrapParam.buildPropertyParamNotInList(prefixOperation, value);
                        default:
                            throw new MyCollabException("Not support yet");
                    }
                } else if (param instanceof PropertyParam) {
                    if (valueBox.getComponentCount() != 1) {
                        return null;
                    }
                    Field field = (Field) valueBox.getComponent(0);
                    Object value = field.getValue();
                    PropertyParam wrapParam = (PropertyParam) param;
                    return wrapParam.buildSearchField(prefixOperation, compareOper, value);
                } else if (param instanceof CompositionStringParam) {
                    if (valueBox.getComponentCount() != 1) {
                        return null;
                    }
                    TextField field = (TextField) valueBox.getComponent(0);
                    String value = field.getValue();
                    CompositionStringParam wrapParam = (CompositionStringParam) param;
                    return wrapParam.buildSearchField(prefixOperation, compareOper, value);
                } else if (param instanceof ConcatStringParam) {
                    if (valueBox.getComponentCount() != 1) {
                        return null;
                    }
                    TextField field = (TextField) valueBox.getComponent(0);
                    String value = field.getValue();
                    ConcatStringParam wrapParam = (ConcatStringParam) param;
                    return wrapParam.buildSearchField(prefixOperation, compareOper, value);
                } else if (param instanceof DateParam) {
                    DateParam wrapParam = (DateParam) param;
                    if (DateParam.BETWEEN.equals(compareOper) || DateParam.NOT_BETWEEN.equals(compareOper)) {
                        if (valueBox.getComponentCount() != 2) {
                            return null;
                        }
                        Date dateValue1 = ((DateField) valueBox.getComponent(0)).getValue();
                        Date dateValue2 = ((DateField) valueBox.getComponent(1)).getValue();
                        return wrapParam.buildSearchField(prefixOperation, compareOper, dateValue1, dateValue2);
                    } else {
                        if (valueBox.getComponentCount() != 1) {
                            return null;
                        }
                        Date dateValue = ((DateField) valueBox.getComponent(0)).getValue();
                        return wrapParam.buildSearchField(prefixOperation, compareOper, dateValue);
                    }
                } else {
                    throw new MyCollabException("Not support yet");
                }
            }
            return null;
        }
    }

    private class SavedSearchResultComboBox extends ComboBox {
        private static final long serialVersionUID = 1L;
        private BeanContainer<String, SaveSearchResultWithBLOBs> beanItem;

        public SavedSearchResultComboBox() {
            this.setImmediate(true);
            this.setItemCaptionMode(ItemCaptionMode.PROPERTY);

            contructComboBox();

            this.addValueChangeListener(new ValueChangeListener() {
                private static final long serialVersionUID = 1L;

                @SuppressWarnings("unchecked")
                @Override
                public void valueChange(com.vaadin.data.Property.ValueChangeEvent event) {
                    Object itemId = SavedSearchResultComboBox.this.getValue();
                    if (itemId != null) {
                        final SaveSearchResultWithBLOBs data = beanItem.getItem(itemId).getBean();

                        String queryText = data.getQuerytext();
                        List<SearchFieldInfo> fieldInfos = (List<SearchFieldInfo>) XStreamJsonDeSerializer.fromJson(queryText);
                        // @HACK: === the library serialize with extra list
                        // wrapper
                        if (CollectionUtils.isEmpty(fieldInfos)) {
                            throw new UserInvalidInputException("There is no field in search criterion");
                        }
                        fieldInfos = (List<SearchFieldInfo>) fieldInfos.get(0);
                        fillSearchFieldInfoAndInvokeSearchRequest(fieldInfos);

                        if (filterBox.getComponentCount() <= 3) {
                            Button updateBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_UPDATE_LABEL));
                            updateBtn.setStyleName(UIConstants.BUTTON_ACTION);
                            updateBtn.setIcon(FontAwesome.REFRESH);
                            updateBtn.addClickListener(new Button.ClickListener() {
                                private static final long serialVersionUID = 1L;

                                @Override
                                public void buttonClick(ClickEvent event) {
                                    List<SearchFieldInfo> fieldInfos = buildSearchFieldInfos();
                                    SaveSearchResultService saveSearchResultService = ApplicationContextUtil
                                            .getSpringBean(SaveSearchResultService.class);
                                    data.setSaveuser(AppContext.getUsername());
                                    data.setSaccountid(AppContext.getAccountId());
                                    data.setQuerytext(XStreamJsonDeSerializer.toJson(fieldInfos));
                                    saveSearchResultService.updateWithSession(data, AppContext.getUsername());

                                }
                            });

                            Button deleteBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_DELETE), new Button.ClickListener() {
                                private static final long serialVersionUID = 1L;

                                @Override
                                public void buttonClick(ClickEvent event) {
                                    SaveSearchResultService saveSearchResultService = ApplicationContextUtil
                                            .getSpringBean(SaveSearchResultService.class);
                                    saveSearchResultService.removeWithSession(data,
                                            AppContext.getUsername(), AppContext.getAccountId());
                                    searchContainer.removeAllComponents();
                                    if (filterBox.getComponentCount() > 2) {
                                        filterBox.removeComponent(filterBox.getComponent(1));
                                    }
                                    contructComboBox();
                                }
                            });
                            deleteBtn.setStyleName(UIConstants.BUTTON_DANGER);
                            deleteBtn.setIcon(FontAwesome.TRASH_O);

                            filterBox.addComponent(deleteBtn, 1);
                            filterBox.addComponent(updateBtn, 1);

                        }

                    } else {
                        searchContainer.removeAllComponents();
                        if (filterBox.getComponentCount() > 3) {
                            filterBox.removeComponent(filterBox.getComponent(1));
                        }
                    }
                }
            });
            this.setImmediate(true);
        }

        private void contructComboBox() {
            SaveSearchResultCriteria searchCriteria = new SaveSearchResultCriteria();
            searchCriteria.setType(StringSearchField.and(searchCategory));
            searchCriteria.setCreateUser(StringSearchField.and(AppContext.getUsername()));
            searchCriteria.setSaccountid(new NumberSearchField(AppContext.getAccountId()));

            SaveSearchResultService saveSearchResultService = ApplicationContextUtil.getSpringBean(SaveSearchResultService.class);
            List<SaveSearchResultWithBLOBs> result = saveSearchResultService.findPagableListByCriteria(new BasicSearchRequest<>(
                    searchCriteria, 0, Integer.MAX_VALUE));
            beanItem = new BeanContainer<>(SaveSearchResultWithBLOBs.class);
            beanItem.setBeanIdProperty("id");

            for (SaveSearchResultWithBLOBs searchResult : result) {
                beanItem.addBean(searchResult);
            }
            this.setContainerDataSource(beanItem);
            this.setItemCaptionPropertyId("queryname");
        }
    }
}