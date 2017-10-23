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
package com.mycollab.vaadin.web.ui;

import com.mycollab.common.domain.SaveSearchResult;
import com.mycollab.common.domain.criteria.SaveSearchResultCriteria;
import com.mycollab.common.i18n.ErrorI18nEnum;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.common.json.QueryAnalyzer;
import com.mycollab.common.service.SaveSearchResultService;
import com.mycollab.core.UserInvalidInputException;
import com.mycollab.db.arguments.*;
import com.mycollab.db.query.*;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.*;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.*;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.lang.reflect.Array;
import java.util.*;

/**
 * @author MyCollab Ltd.
 * @since 4.0
 */
public class BuildCriterionComponent<S extends SearchCriteria> extends MVerticalLayout implements CriteriaBuilderComponent<S> {
    private static final long serialVersionUID = 1L;
    private static final Logger LOG = LoggerFactory.getLogger(BuildCriterionComponent.class);

    private SearchLayout<S> hostSearchLayout;
    private Param[] paramFields;
    private String searchCategory;

    private MHorizontalLayout filterBox;
    private MVerticalLayout searchContainer;

    public BuildCriterionComponent(SearchLayout<S> searchLayout, Param[] paramFields, String searchCategory) {
        this.hostSearchLayout = searchLayout;
        this.paramFields = paramFields;
        this.searchCategory = searchCategory;

        MHorizontalLayout headerBox = new MHorizontalLayout().withMargin(new MarginInfo(true, false, true, true));
        headerBox.setDefaultComponentAlignment(Alignment.TOP_LEFT);
        addComponent(headerBox);

        Label filterLbl = new Label(UserUIContext.getMessage(GenericI18Enum.OPT_SAVED_FILTER));
        headerBox.with(filterLbl).withAlign(filterLbl, Alignment.TOP_LEFT);

        filterBox = new MHorizontalLayout();
        headerBox.with(filterBox).withAlign(filterBox, Alignment.TOP_LEFT);

        buildFilterBox(null);

        searchContainer = new MVerticalLayout().withMargin(false);
        searchContainer.setDefaultComponentAlignment(Alignment.TOP_LEFT);

        MButton addCriteriaBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_ADD_CRITERIA), clickEvent -> {
            CriteriaSelectionLayout newCriteriaBar = new CriteriaSelectionLayout(searchContainer.getComponentCount() + 1);
            searchContainer.addComponent(newCriteriaBar);
        }).withIcon(FontAwesome.PLUS).withStyleName(WebThemes.BUTTON_ACTION);

        this.with(searchContainer, new MHorizontalLayout(addCriteriaBtn).withMargin(true));
    }

    private void buildFilterBox(String queryName) {
        filterBox.removeAllComponents();

        SavedSearchResultComboBox filterComboBox = new SavedSearchResultComboBox();
        filterBox.addComponent(filterComboBox);

        MButton saveSearchBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_NEW_FILTER), clickEvent -> buildSaveFilterBox())
                .withStyleName(WebThemes.BUTTON_ACTION).withIcon(FontAwesome.PLUS);
        filterBox.addComponent(saveSearchBtn);
    }

    private void buildSaveFilterBox() {
        filterBox.removeAllComponents();

        final TextField queryTextField = new TextField();
        filterBox.addComponent(queryTextField);

        MButton saveBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_SAVE), clickEvent -> {
            String queryText = queryTextField.getValue();
            saveSearchCriteria(queryText);
        }).withIcon(FontAwesome.SAVE).withStyleName(WebThemes.BUTTON_ACTION);
        filterBox.addComponent(saveBtn);

        MButton cancelBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_CANCEL), clickEvent -> buildFilterBox(null))
                .withStyleName(WebThemes.BUTTON_OPTION);
        filterBox.addComponent(cancelBtn);
    }

    private void saveSearchCriteria(String queryText) {
        List<SearchFieldInfo<S>> fieldInfos = buildSearchFieldInfos();

        if (CollectionUtils.isEmpty(fieldInfos)) {
            throw new UserInvalidInputException(UserUIContext.getMessage(ErrorI18nEnum.SELECT_AT_LEAST_ONE_CRITERIA));
        }

        SaveSearchResultService saveSearchResultService = AppContextUtil.getSpringBean(SaveSearchResultService.class);
        SaveSearchResult searchResult = new SaveSearchResult();
        searchResult.setSaveuser(UserUIContext.getUsername());
        searchResult.setSaccountid(AppUI.getAccountId());
        searchResult.setQuerytext(QueryAnalyzer.toQueryParams(fieldInfos));
        searchResult.setType(searchCategory);
        searchResult.setQueryname(queryText);
        saveSearchResultService.saveWithSession(searchResult, UserUIContext.getUsername());
        buildFilterBox(queryText);
    }

    @Override
    public List<SearchFieldInfo<S>> buildSearchFieldInfos() {
        Iterator<Component> iterator = searchContainer.iterator();
        List<SearchFieldInfo<S>> fieldInfos = new ArrayList<>();
        while (iterator.hasNext()) {
            CriteriaSelectionLayout criteriaSelectionLayout = (CriteriaSelectionLayout) iterator.next();
            SearchFieldInfo searchFieldInfo = criteriaSelectionLayout.buildSearchFieldInfo();
            if (searchFieldInfo != null) {
                fieldInfos.add(searchFieldInfo);
            }
        }
        return fieldInfos;
    }

    void clearAllFields() {
        searchContainer.removeAllComponents();
    }

    protected Component buildPropertySearchComp(String fieldId) {
        return null;
    }

    void fillSearchFieldInfoAndInvokeSearchRequest(List<SearchFieldInfo<S>> searchFieldInfos) {
        searchContainer.removeAllComponents();

        try {
            searchFieldInfos.forEach(fieldInfo -> {
                CriteriaSelectionLayout criteriaSelectionLayout = new CriteriaSelectionLayout(searchContainer.getComponentCount() + 1);
                criteriaSelectionLayout.fillSearchFieldInfo(fieldInfo);
                searchContainer.addComponent(criteriaSelectionLayout);
            });
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
        private I18nValueComboBox compareSelectionBox;
        private MVerticalLayout valueBox;
        private Button deleteBtn;

        CriteriaSelectionLayout(int index) {
            super(6, 1);
            this.index = index;
            this.setSpacing(true);
            this.setMargin(new MarginInfo(false, true, false, true));
            this.setDefaultComponentAlignment(Alignment.TOP_LEFT);

            indexLbl = new Label(index + "");
            indexLbl.setWidth("70px");
            this.addComponent(indexLbl, 0, 0);

            if (index == 1) {
                this.addComponent(ELabel.html("&nbsp;").withWidth("90px"), 1, 0);
            } else {
                operatorSelectionBox = new ValueComboBox(false, SearchField.AND, SearchField.OR);
                operatorSelectionBox.setWidth("90px");
                this.addComponent(operatorSelectionBox, 1, 0);
            }
            buildFieldSelectionBox();

            valueBox = new MVerticalLayout().withMargin(false).withWidth("300px");
            deleteBtn = new MButton("", event -> {
                int compIndex = searchContainer.getComponentIndex(CriteriaSelectionLayout.this);
                searchContainer.removeComponent(CriteriaSelectionLayout.this);
                for (int i = compIndex; i < searchContainer.getComponentCount(); i++) {
                    CriteriaSelectionLayout searchCriteriaLayout = (CriteriaSelectionLayout) searchContainer.getComponent(i);
                    searchCriteriaLayout.updateIndex();
                }
            }).withIcon(FontAwesome.TRASH_O).withStyleName(WebThemes.BUTTON_ICON_ONLY);

            this.addComponent(fieldSelectionBox, 2, 0);
            this.addComponent(compareSelectionBox, 3, 0);
            this.addComponent(valueBox, 4, 0);
            this.addComponent(deleteBtn, 5, 0);
        }

        private void updateIndex() {
            index--;
            indexLbl.setValue(index + "");
            if (index == 1) {
                removeComponent(operatorSelectionBox);
                this.addComponent(ELabel.html("&nbsp;").withWidth("90px"), 1, 0);
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
            } else if (param instanceof BooleanParam) {
                I18nValueComboBox valueField = new I18nValueComboBox(false, GenericI18Enum.ACTION_YES, GenericI18Enum.ACTION_NO);
                valueField.setValue(String.valueOf(searchFieldInfo.eval()));
                valueField.setWidth(width);
                valueBox.addComponent(valueField);
            } else if (param instanceof DateParam) {
                String compareItem = (String) compareSelectionBox.getValue();
                if (DateParam.BETWEEN.equals(compareItem) || DateParam.NOT_BETWEEN.equals(compareItem)) {
                    PopupDateFieldExt field1 = new PopupDateFieldExt();
                    field1.setValue((Date) Array.get(searchFieldInfo.eval(), 0));
                    field1.setWidth(width);
                    PopupDateFieldExt field2 = new PopupDateFieldExt();
                    field2.setValue((Date) Array.get(searchFieldInfo.eval(), 1));
                    field2.setWidth(width);
                    valueBox.with(field1, field2);
                } else {
                    PopupDateFieldExt field = new PopupDateFieldExt();
                    field.setValue((Date) searchFieldInfo.eval());
                    field.setWidth(width);
                    valueBox.addComponent(field);
                }
            } else if (param instanceof PropertyParam || param instanceof PropertyListParam || param instanceof CustomSqlParam
                    || param instanceof SearchCriteriaBridgeParam) {
                Component comp = buildPropertySearchComp(param.getId());
                if (comp != null) {
                    comp.setWidth(width);
                    valueBox.addComponent(comp);
                    if (comp instanceof CustomField<?> && (((CustomField) comp).getType() == Integer.class)) {
                        ((Field) comp).setValue(Integer.parseInt(searchFieldInfo.eval() + ""));
                    } else {
                        ((Field) comp).setValue(searchFieldInfo.eval());
                    }
                }
            } else if (param instanceof StringListParam) {
                ValueListSelect listSelect = new ValueListSelect();
                listSelect.setCaption(null);
                listSelect.loadData(((StringListParam) param).getValues().toArray(new String[0]));
                listSelect.setValue(searchFieldInfo.eval());
                listSelect.setWidth(width);
                valueBox.addComponent(listSelect);

            } else if (param instanceof I18nStringListParam) {
                List<? extends Enum<?>> values = ((I18nStringListParam) param).getValues();
                if (CollectionUtils.isNotEmpty(values)) {
                    I18nValueListSelect listSelect = new I18nValueListSelect();
                    listSelect.setCaption(null);
                    listSelect.loadData(((I18nStringListParam) param).getValues());
                    listSelect.setValue(searchFieldInfo.eval());
                    listSelect.setWidth(width);
                    valueBox.addComponent(listSelect);
                }
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
                CacheParamMapper.ValueParam valueParam = CacheParamMapper.getValueParam(searchCategory, field.getId());
                fieldSelectionBox.setItemCaption(field, UserUIContext.getMessage(valueParam.getDisplayName()));
            }

            fieldSelectionBox.addValueChangeListener(valueChangeEvent -> {
                compareSelectionBox.removeAllItems();

                Param field = (Param) fieldSelectionBox.getValue();
                if (field != null) {
                    if (field instanceof StringParam) {
                        compareSelectionBox.loadData(Arrays.asList(StringParam.OPTIONS));
                    } else if (field instanceof NumberParam) {
                        compareSelectionBox.loadData(Arrays.asList(NumberParam.OPTIONS));
                    } else if (field instanceof BooleanParam) {
                        compareSelectionBox.loadData(BooleanParam.OPTIONS);
                    } else if (field instanceof DateParam) {
                        compareSelectionBox.loadData(DateParam.OPTIONS);
                    } else if (field instanceof PropertyParam) {
                        compareSelectionBox.loadData(Arrays.asList(PropertyParam.OPTIONS));
                    } else if (field instanceof PropertyListParam || field instanceof CustomSqlParam || field instanceof SearchCriteriaBridgeParam) {
                        compareSelectionBox.loadData(Arrays.asList(PropertyListParam.OPTIONS));
                    } else if (field instanceof StringListParam) {
                        compareSelectionBox.loadData(Arrays.asList(StringListParam.OPTIONS));
                    } else if (field instanceof I18nStringListParam) {
                        compareSelectionBox.loadData(Arrays.asList(I18nStringListParam.OPTIONS));
                    } else if (field instanceof CompositionStringParam) {
                        compareSelectionBox.loadData(Arrays.asList(StringParam.OPTIONS));
                    } else if (field instanceof ConcatStringParam) {
                        compareSelectionBox.loadData(Arrays.asList(ConcatStringParam.OPTIONS));
                    }
                }
            });

            compareSelectionBox = new I18nValueComboBox(false);
            compareSelectionBox.setWidth("150px");
            compareSelectionBox.setImmediate(true);
            compareSelectionBox.addValueChangeListener(valueChangeEvent -> displayAssociateInputField((Param) fieldSelectionBox.getValue()));
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
            } else if (field instanceof BooleanParam) {
                I18nValueComboBox yesNoBox = new I18nValueComboBox(false, GenericI18Enum.ACTION_YES, GenericI18Enum.ACTION_NO);
                yesNoBox.setWidth(width);
                valueBox.addComponent(yesNoBox);
            } else if (field instanceof DateParam) {
                if (DateParam.BETWEEN.equals(compareItem) || DateParam.NOT_BETWEEN.equals(compareItem)) {
                    PopupDateFieldExt field1 = new PopupDateFieldExt().withWidth(width);
                    PopupDateFieldExt field2 = new PopupDateFieldExt().withWidth(width);
                    valueBox.with(field1, field2);
                } else {
                    PopupDateFieldExt tempDateField = new PopupDateFieldExt().withWidth(width);
                    valueBox.addComponent(tempDateField);
                }
            } else if (field instanceof PropertyParam || field instanceof PropertyListParam || field instanceof CustomSqlParam
                    || field instanceof SearchCriteriaBridgeParam) {
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
                listSelect.loadData(((I18nStringListParam) field).getValues());
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

            if (value != null) {
                if (value.getClass().isArray()) {
                    if (Array.getLength(value) == 0) {
                        return null;
                    }
                } else if (Collection.class.isAssignableFrom(value.getClass())) {
                    if (((Collection) value).size() == 0) {
                        return null;
                    }
                }
                return new SearchFieldInfo(prefixOper, param, compareOper, ConstantValueInjector.valueOf(value));
            } else {
                return null;
            }
        }
    }

    private class SavedSearchResultComboBox extends ComboBox {
        private static final long serialVersionUID = 1L;
        private BeanContainer<String, SaveSearchResult> beanItem;

        SavedSearchResultComboBox() {
            this.setImmediate(true);
            this.setItemCaptionMode(ItemCaptionMode.PROPERTY);
            buildQuerySelectComponent();

            this.addValueChangeListener(valueChangeEvent -> {
                Object itemId = SavedSearchResultComboBox.this.getValue();
                if (itemId != null) {
                    final SaveSearchResult data = beanItem.getItem(itemId).getBean();

                    String queryText = data.getQuerytext();
                    try {
                        List<SearchFieldInfo<S>> fieldInfos = QueryAnalyzer.toSearchFieldInfos(queryText, searchCategory);
                        fillSearchFieldInfoAndInvokeSearchRequest(fieldInfos);
                        hostSearchLayout.callSearchAction();
                    } catch (Exception e) {
                        LOG.error("Error of invalid query", e);
                        NotificationUtil.showErrorNotification(UserUIContext.getMessage(ErrorI18nEnum.QUERY_SEARCH_IS_INVALID));
                    }

                    if (filterBox.getComponentCount() <= 3) {
                        MButton updateBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_UPDATE_LABEL), clickEvent -> {
                            List<SearchFieldInfo<S>> fieldInfos = buildSearchFieldInfos();
                            SaveSearchResultService saveSearchResultService = AppContextUtil.getSpringBean(SaveSearchResultService.class);
                            data.setSaveuser(UserUIContext.getUsername());
                            data.setSaccountid(AppUI.getAccountId());
                            data.setQuerytext(QueryAnalyzer.toQueryParams(fieldInfos));
                            saveSearchResultService.updateWithSession(data, UserUIContext.getUsername());
                        }).withIcon(FontAwesome.REFRESH).withStyleName(WebThemes.BUTTON_ACTION);

                        MButton deleteBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_DELETE), clickEvent -> {
                            SaveSearchResultService saveSearchResultService = AppContextUtil.getSpringBean(SaveSearchResultService.class);
                            saveSearchResultService.removeWithSession(data, UserUIContext.getUsername(), AppUI.getAccountId());
                            searchContainer.removeAllComponents();
                            if (filterBox.getComponentCount() > 2) {
                                filterBox.removeComponent(filterBox.getComponent(1));
                            }
                            buildQuerySelectComponent();
                        }).withIcon(FontAwesome.TRASH_O).withStyleName(WebThemes.BUTTON_DANGER);

                        filterBox.addComponent(deleteBtn, 1);
                        filterBox.addComponent(updateBtn, 1);
                    }

                } else {
                    searchContainer.removeAllComponents();
                    if (filterBox.getComponentCount() > 3) {
                        filterBox.removeComponent(filterBox.getComponent(1));
                        filterBox.removeComponent(filterBox.getComponent(1));
                    }
                }
            });
        }

        private void buildQuerySelectComponent() {
            SaveSearchResultCriteria searchCriteria = new SaveSearchResultCriteria();
            searchCriteria.setType(StringSearchField.and(searchCategory));
            searchCriteria.setCreateUser(StringSearchField.and(UserUIContext.getUsername()));
            searchCriteria.setSaccountid(new NumberSearchField(AppUI.getAccountId()));

            SaveSearchResultService saveSearchResultService = AppContextUtil.getSpringBean(SaveSearchResultService.class);
            List<SaveSearchResult> result = (List<SaveSearchResult>) saveSearchResultService.findPageableListByCriteria(new BasicSearchRequest<>(searchCriteria));
            beanItem = new BeanContainer<>(SaveSearchResult.class);
            beanItem.setBeanIdProperty("id");

            for (SaveSearchResult searchResult : result) {
                beanItem.addBean(searchResult);
            }
            this.setContainerDataSource(beanItem);
            this.setItemCaptionPropertyId("queryname");
        }
    }
}