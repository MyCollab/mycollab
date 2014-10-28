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
package com.esofthead.mycollab.vaadin.ui;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.esofthead.mycollab.common.domain.SaveSearchResultWithBLOBs;
import com.esofthead.mycollab.common.domain.criteria.SaveSearchResultCriteria;
import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.common.service.SaveSearchResultService;
import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.core.UserInvalidInputException;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchCriteria;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.core.arguments.SearchRequest;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.core.db.query.CompositionStringParam;
import com.esofthead.mycollab.core.db.query.ConcatStringParam;
import com.esofthead.mycollab.core.db.query.CustomSqlParam;
import com.esofthead.mycollab.core.db.query.DateParam;
import com.esofthead.mycollab.core.db.query.I18nStringListParam;
import com.esofthead.mycollab.core.db.query.NumberParam;
import com.esofthead.mycollab.core.db.query.Param;
import com.esofthead.mycollab.core.db.query.PropertyListParam;
import com.esofthead.mycollab.core.db.query.PropertyParam;
import com.esofthead.mycollab.core.db.query.SearchFieldInfo;
import com.esofthead.mycollab.core.db.query.StringListParam;
import com.esofthead.mycollab.core.db.query.StringParam;
import com.esofthead.mycollab.core.utils.XStreamJsonDeSerializer;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Field;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.0
 * 
 */
public class BuildCriterionComponent<S extends SearchCriteria> extends
		VerticalLayout {
	private static final long serialVersionUID = 1L;

	private Param[] paramFields;
	private String searchCategory;
	private Class<S> type;

	private HorizontalLayout filterBox;
	private SavedSearchResultComboBox filterComboBox;
	private VerticalLayout searchContainer;
	private HorizontalLayout controlsBtn;

	public BuildCriterionComponent(Param[] paramFields, Class<S> type,
			String searchCategory) {
		this.setSpacing(true);
		this.paramFields = paramFields;
		this.type = type;
		this.searchCategory = searchCategory;

		HorizontalLayout headerBox = new HorizontalLayout();
		headerBox.setSpacing(true);
		headerBox.setMargin(new MarginInfo(true, false, true, true));
		headerBox.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);

		UiUtils.addComponent(headerBox, new Label("&nbsp; Saved Filter: ",
				ContentMode.HTML), Alignment.MIDDLE_LEFT);

		filterBox = new HorizontalLayout();
		filterBox.setSpacing(true);
		UiUtils.addComponent(headerBox, filterBox, Alignment.MIDDLE_LEFT);
		this.addComponent(headerBox);
		buildFilterBox(null);

		this.searchContainer = new VerticalLayout();
		this.searchContainer.setSpacing(true);
		this.searchContainer
				.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);

		controlsBtn = new HorizontalLayout();
		controlsBtn.setSpacing(true);
		controlsBtn.setMargin(true);

		Button addCriteriaBtn = new Button(
				AppContext.getMessage(GenericI18Enum.BUTTON_ADD_CRITERIA),
				new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(ClickEvent event) {
						CriteriaSelectionLayout newCriteriaBar = new CriteriaSelectionLayout(
								searchContainer.getComponentCount() + 1);
						searchContainer.addComponent(newCriteriaBar);
					}
				});
		addCriteriaBtn.setStyleName(UIConstants.THEME_BROWN_LINK);
		addCriteriaBtn.setIcon(MyCollabResource
				.newResource(WebResourceIds._16_add));
		UiUtils.addComponent(this, addCriteriaBtn, Alignment.MIDDLE_CENTER);

		controlsBtn.addComponent(addCriteriaBtn);

		this.addComponent(searchContainer);
		this.addComponent(controlsBtn);
	}

	private void buildFilterBox(String queryname) {
		filterBox.removeAllComponents();

		filterComboBox = new SavedSearchResultComboBox();
		filterComboBox.setWidth("125px");
		filterBox.addComponent(filterComboBox);

		Button saveSearchBtn = new Button(
				AppContext.getMessage(GenericI18Enum.BUTTON_NEW_FILTER),
				new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(ClickEvent event) {
						buildSaveFilterBox();
					}
				});
		saveSearchBtn.addStyleName(UIConstants.THEME_GREEN_LINK);
		saveSearchBtn.setIcon(MyCollabResource
				.newResource(WebResourceIds._16_addRecord));
		filterBox.addComponent(saveSearchBtn);

		if (queryname != null) {

		}
	}

	private void buildSaveFilterBox() {
		filterBox.removeAllComponents();

		final TextField queryTextField = new TextField();
		queryTextField.setWidth("125px");
		filterBox.addComponent(queryTextField);

		Button saveBtn = new Button(
				AppContext.getMessage(GenericI18Enum.BUTTON_SAVE),
				new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(ClickEvent event) {
						String queryText = queryTextField.getValue();
						saveSearchCriteria(queryText);
					}
				});
		saveBtn.setStyleName(UIConstants.THEME_GREEN_LINK);
		saveBtn.setIcon(MyCollabResource.newResource("icons/16/save.png"));
		filterBox.addComponent(saveBtn);

		Button cancelBtn = new Button(
				AppContext.getMessage(GenericI18Enum.BUTTON_CANCEL),
				new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(ClickEvent event) {
						buildFilterBox(null);
					}
				});
		cancelBtn.addStyleName(UIConstants.THEME_GRAY_LINK);
		filterBox.addComponent(cancelBtn);
	}

	private void saveSearchCriteria(String queryText) {
		List<SearchFieldInfo> fieldInfos = buildSearchFieldInfos();

		if (CollectionUtils.isEmpty(fieldInfos)) {
			throw new UserInvalidInputException(
					"You must select at least one search criteria");
		}

		SaveSearchResultService saveSearchResultService = ApplicationContextUtil
				.getSpringBean(SaveSearchResultService.class);
		SaveSearchResultWithBLOBs searchResult = new SaveSearchResultWithBLOBs();
		searchResult.setSaveuser(AppContext.getUsername());
		searchResult.setSaccountid(AppContext.getAccountId());
		searchResult.setQuerytext(XStreamJsonDeSerializer.toJson(fieldInfos));
		searchResult.setType(searchCategory);
		searchResult.setQueryname(queryText);
		saveSearchResultService.saveWithSession(searchResult,
				AppContext.getUsername());
		buildFilterBox(queryText);
	}

	@SuppressWarnings("unchecked")
	private List<SearchFieldInfo> buildSearchFieldInfos() {
		Iterator<Component> iterator = searchContainer.iterator();
		List<SearchFieldInfo> fieldInfos = new ArrayList<SearchFieldInfo>();
		while (iterator.hasNext()) {
			CriteriaSelectionLayout bar = (CriteriaSelectionLayout) iterator
					.next();
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

	@SuppressWarnings("unchecked")
	public S fillupSearchCriteria() {
		try {
			S searchCriteria = type.newInstance();
			Iterator<Component> iterator = searchContainer.iterator();
			while (iterator.hasNext()) {
				CriteriaSelectionLayout bar = (CriteriaSelectionLayout) iterator
						.next();
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

	private void fillSearchFieldInfo(List<SearchFieldInfo> searchFieldInfos) {
		searchContainer.removeAllComponents();

		for (int i = 0; i < searchFieldInfos.size(); i++) {
			SearchFieldInfo searchFieldInfo = searchFieldInfos.get(i);
			CriteriaSelectionLayout newCriteriaBar = new CriteriaSelectionLayout(
					searchContainer.getComponentCount() + 1);

			newCriteriaBar.fillSearchFieldInfo(searchFieldInfo);
			searchContainer.addComponent(newCriteriaBar);
		}
	}

	private class CriteriaSelectionLayout extends GridLayout {
		private static final long serialVersionUID = 1L;

		private int index;
		private Label indexLbl;
		private ValueComboBox operatorSelectionBox;
		private ComboBox fieldSelectionBox;
		private ValueComboBox compareSelectionBox;
		private VerticalLayout valueBox;
		private Button deleteBtn;

		public CriteriaSelectionLayout(int index) {
			super(6, 1);
			this.index = index;
			this.setSpacing(true);
			this.setMargin(new MarginInfo(false, true, false, true));
			this.setDefaultComponentAlignment(Alignment.TOP_LEFT);

			indexLbl = new Label(index + "");
			indexLbl.addStyleName("index_lbl");
			this.addComponent(indexLbl, 0, 0);
			this.setComponentAlignment(indexLbl, Alignment.TOP_LEFT);

			if (index == 1) {
				Label placeHolder = new Label("&nbsp;", ContentMode.HTML);
				placeHolder.setWidth("60px");
				this.addComponent(placeHolder, 1, 0);
			} else {
				operatorSelectionBox = new ValueComboBox(false,
						SearchField.AND, SearchField.OR);
				operatorSelectionBox.setWidth("60px");
				this.addComponent(operatorSelectionBox, 1, 0);
			}

			buildFieldSelectionBox();

			valueBox = new VerticalLayout();
			valueBox.setSpacing(true);
			valueBox.setWidth("200px");
			deleteBtn = new Button("", new Button.ClickListener() {
				private static final long serialVersionUID = 1L;

				@SuppressWarnings("unchecked")
				@Override
				public void buttonClick(ClickEvent event) {
					int compIndex = searchContainer
							.getComponentIndex(CriteriaSelectionLayout.this);
					searchContainer
							.removeComponent(CriteriaSelectionLayout.this);
					for (int i = compIndex; i < searchContainer
							.getComponentCount(); i++) {
						CriteriaSelectionLayout searchCriteriaLayout = (CriteriaSelectionLayout) searchContainer
								.getComponent(i);
						searchCriteriaLayout.updateIndex();
					}
				}
			});
			deleteBtn.addStyleName(UIConstants.THEME_TRANSPARENT_LINK);
			deleteBtn.setIcon(MyCollabResource
					.newResource("icons/16/crm/basket.png"));

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

		@SuppressWarnings({ "rawtypes", "unchecked" })
		private void fillSearchFieldInfo(SearchFieldInfo searchFieldInfo) {
			String width = "200px";
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

			if (param instanceof StringParam
					|| param instanceof ConcatStringParam) {
				TextField valueField = new TextField();
				valueField.setValue((String) searchFieldInfo.getValue());
				valueField.setWidth(width);
				valueBox.addComponent(valueField);

			} else if (param instanceof NumberParam) {
				TextField valueField = new TextField();
				valueField.setValue(String.valueOf(searchFieldInfo.getValue()));
				valueField.setWidth(width);
				valueBox.addComponent(valueField);
			} else if (param instanceof DateParam) {
				String compareItem = (String) compareSelectionBox.getValue();
				if (DateParam.BETWEEN.equals(compareItem)
						|| DateParam.NOT_BETWEEN.equals(compareItem)) {
					DateField field1 = new DateField();
					field1.setValue((Date) Array.get(
							searchFieldInfo.getValue(), 0));
					DateField field2 = new DateField();
					field2.setValue((Date) Array.get(
							searchFieldInfo.getValue(), 1));
					field1.setWidth(width);
					field2.setWidth(width);
					valueBox.addComponent(field1);
					valueBox.addComponent(field2);
				} else {
					DateField field = new DateField();
					field.setValue((Date) searchFieldInfo.getValue());
					field.setWidth(width);
					valueBox.addComponent(field);
				}
			} else if (param instanceof PropertyParam
					|| param instanceof PropertyListParam
					|| param instanceof CustomSqlParam) {
				Component comp = buildPropertySearchComp(param.getId());
				if (comp != null) {
					if (comp instanceof CustomField<?>
							&& (((CustomField) comp).getType() == Integer.class)) {
						((Field) comp).setValue(Integer
								.parseInt(searchFieldInfo.getValue() + ""));
					} else {
						((Field) comp).setValue(searchFieldInfo.getValue());
					}
					comp.setWidth(width);
					valueBox.addComponent(comp);
				}
			} else if (param instanceof StringListParam) {
				ValueListSelect listSelect = new ValueListSelect();
				listSelect.setCaption(null);
				listSelect.loadData(((StringListParam) param).getLstValues()
						.toArray(new String[0]));
				listSelect.setValue(searchFieldInfo.getValue());
				listSelect.setWidth(width);
				valueBox.addComponent(listSelect);

			} else if (param instanceof I18nStringListParam) {
				I18nValueListSelect listSelect = new I18nValueListSelect();
				listSelect.setCaption(null);
				listSelect.loadData(((I18nStringListParam) param)
						.getLstValues());
				listSelect.setValue(searchFieldInfo.getValue());
				listSelect.setWidth(width);
				valueBox.addComponent(listSelect);

			} else if (param instanceof CompositionStringParam) {
				TextField tempTextField = new TextField();
				tempTextField.setValue(String.valueOf(searchFieldInfo
						.getValue()));
				tempTextField.setWidth(width);
				valueBox.addComponent(tempTextField);
			}
		}

		private void buildFieldSelectionBox() {
			fieldSelectionBox = new ComboBox();
			fieldSelectionBox.setImmediate(true);
			fieldSelectionBox.setWidth("125px");
			fieldSelectionBox.setItemCaptionMode(ItemCaptionMode.EXPLICIT);
			for (Param field : paramFields) {
				fieldSelectionBox.addItem(field);
				fieldSelectionBox.setItemCaption(field,
						AppContext.getMessage(field.getDisplayName()));
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
						} else if (field instanceof PropertyListParam
								|| field instanceof CustomSqlParam) {
							compareSelectionBox
									.loadData(PropertyListParam.OPTIONS);
						} else if (field instanceof StringListParam) {
							compareSelectionBox
									.loadData(StringListParam.OPTIONS);
						} else if (field instanceof I18nStringListParam) {
							compareSelectionBox
									.loadData(I18nStringListParam.OPTIONS);
						} else if (field instanceof CompositionStringParam) {
							compareSelectionBox.loadData(StringParam.OPTIONS);
						} else if (field instanceof ConcatStringParam) {
							compareSelectionBox
									.loadData(ConcatStringParam.OPTIONS);
						}

						displayAssociateInputField((Param) fieldSelectionBox
								.getValue());
					}
				}
			});

			compareSelectionBox = new ValueComboBox(false, "");
			compareSelectionBox.setWidth("120px");
			compareSelectionBox.setImmediate(true);
			compareSelectionBox
					.addValueChangeListener(new ValueChangeListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void valueChange(ValueChangeEvent event) {
							displayAssociateInputField((Param) fieldSelectionBox
									.getValue());
						}
					});
		}

		private void displayAssociateInputField(Param field) {
			String width = "200px";
			String compareItem = (String) compareSelectionBox.getValue();
			valueBox.removeAllComponents();

			if (field instanceof StringParam
					|| field instanceof ConcatStringParam) {
				TextField tempTextField = new TextField();
				tempTextField.setWidth(width);
				valueBox.addComponent(tempTextField);
			} else if (field instanceof NumberParam) {
				TextField tempTextField = new TextField();
				tempTextField.setWidth(width);
				valueBox.addComponent(tempTextField);
			} else if (field instanceof DateParam) {
				if (DateParam.BETWEEN.equals(compareItem)
						|| DateParam.NOT_BETWEEN.equals(compareItem)) {
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
			} else if (field instanceof PropertyParam
					|| field instanceof PropertyListParam
					|| field instanceof CustomSqlParam) {
				Component comp = buildPropertySearchComp(field.getId());
				if (comp != null) {
					comp.setWidth(width);
					valueBox.addComponent(comp);
				}
			} else if (field instanceof StringListParam) {
				ValueListSelect listSelect = new ValueListSelect();
				listSelect.setCaption(null);
				listSelect.loadData(((StringListParam) field).getLstValues()
						.toArray(new String[0]));
				listSelect.setWidth(width);
				valueBox.addComponent(listSelect);
			} else if (field instanceof I18nStringListParam) {
				I18nValueListSelect listSelect = new I18nValueListSelect();
				listSelect.setCaption(null);
				listSelect.loadData(((I18nStringListParam) field)
						.getLstValues());
				listSelect.setWidth(width);
				valueBox.addComponent(listSelect);
			} else if (field instanceof CompositionStringParam) {
				TextField tempTextField = new TextField();
				tempTextField.setWidth(width);
				valueBox.addComponent(tempTextField);
			}
		}

		private SearchFieldInfo buildSearchFieldInfo() {
			String prefixOper = (operatorSelectionBox != null) ? (String) operatorSelectionBox
					.getValue() : "AND";
			Param param = (Param) fieldSelectionBox.getValue();
			String compareOper = (String) compareSelectionBox.getValue();
			Object value = null;
			int componentCount = valueBox.getComponentCount();
			if (componentCount == 1) {
				Field<?> component = (Field<?>) valueBox.getComponent(0);
				value = component.getValue();
			} else if (componentCount > 1) {
				value = new Object[componentCount];
				for (int i = 0; i < componentCount; i++) {
					Array.set(value, i,
							((Field<?>) valueBox.getComponent(i)).getValue());
				}
			} else {
				return null;
			}

			return new SearchFieldInfo(prefixOper, param, compareOper, value);
		}

		@SuppressWarnings("rawtypes")
		private SearchField buildSearchField() {
			Param param = (Param) fieldSelectionBox.getValue();
			String prefixOperation = (operatorSelectionBox != null) ? (String) operatorSelectionBox
					.getValue() : "AND";
			if (param != null) {
				String compareOper = (String) compareSelectionBox.getValue();

				if (param instanceof StringParam) {
					if (valueBox.getComponentCount() != 1) {
						return null;
					}
					TextField field = (TextField) valueBox.getComponent(0);
					String value = field.getValue();
					StringParam wrapParam = (StringParam) param;
					return wrapParam.buildSearchField(prefixOperation,
							compareOper, value);
				} else if (param instanceof StringListParam) {
					if (valueBox.getComponentCount() != 1) {
						return null;
					}
					ValueListSelect field = (ValueListSelect) valueBox
							.getComponent(0);
					Collection<?> value = (Collection<?>) field.getValue();
					if (CollectionUtils.isEmpty(value)) {
						return null;
					}

					StringListParam wrapParam = (StringListParam) param;

					switch (compareOper) {
					case StringListParam.IN:
						return wrapParam.buildStringParamInList(
								prefixOperation, value);
					case StringListParam.NOT_IN:
						return wrapParam.buildStringParamNotInList(
								prefixOperation, value);
					default:
						throw new MyCollabException("Not support yet");
					}
				} else if (param instanceof I18nStringListParam) {
					if (valueBox.getComponentCount() != 1) {
						return null;
					}
					I18nValueListSelect field = (I18nValueListSelect) valueBox
							.getComponent(0);
					Collection<?> value = (Collection<?>) field.getValue();
					if (CollectionUtils.isEmpty(value)) {
						return null;
					}

					I18nStringListParam wrapParam = (I18nStringListParam) param;

					switch (compareOper) {
					case StringListParam.IN:
						return wrapParam.buildStringParamInList(
								prefixOperation, value);
					case StringListParam.NOT_IN:
						return wrapParam.buildStringParamNotInList(
								prefixOperation, value);
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
					return wrapParam.buildSearchField(prefixOperation,
							compareOper, value);
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
						return wrapParam.buildPropertyParamInList(
								prefixOperation, value);
					case PropertyListParam.NOT_BELONG_TO:
						return wrapParam.buildPropertyParamNotInList(
								prefixOperation, value);
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
						return wrapParam.buildPropertyParamInList(
								prefixOperation, value);
					case PropertyListParam.NOT_BELONG_TO:
						return wrapParam.buildPropertyParamNotInList(
								prefixOperation, value);
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
					return wrapParam.buildSearchField(prefixOperation,
							compareOper, value);
				} else if (param instanceof CompositionStringParam) {
					if (valueBox.getComponentCount() != 1) {
						return null;
					}
					TextField field = (TextField) valueBox.getComponent(0);
					String value = field.getValue();
					CompositionStringParam wrapParam = (CompositionStringParam) param;
					return wrapParam.buildSearchField(prefixOperation,
							compareOper, value);
				} else if (param instanceof ConcatStringParam) {
					if (valueBox.getComponentCount() != 1) {
						return null;
					}
					TextField field = (TextField) valueBox.getComponent(0);
					String value = field.getValue();
					ConcatStringParam wrapParam = (ConcatStringParam) param;
					return wrapParam.buildSearchField(prefixOperation,
							compareOper, value);
				} else if (param instanceof DateParam) {
					DateParam wrapParam = (DateParam) param;

					if (DateParam.BETWEEN.equals(compareOper)
							|| DateParam.NOT_BETWEEN.equals(compareOper)) {
						if (valueBox.getComponentCount() != 2) {
							return null;
						}
						Date dateValue1 = ((DateField) valueBox.getComponent(0))
								.getValue();
						Date dateValue2 = ((DateField) valueBox.getComponent(1))
								.getValue();
						return wrapParam.buildSearchField(prefixOperation,
								compareOper, dateValue1, dateValue2);
					} else {
						if (valueBox.getComponentCount() != 1) {
							return null;
						}
						Date dateValue = ((DateField) valueBox.getComponent(0))
								.getValue();
						return wrapParam.buildSearchField(prefixOperation,
								compareOper, dateValue);
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
				public void valueChange(
						com.vaadin.data.Property.ValueChangeEvent event) {
					Object itemId = SavedSearchResultComboBox.this.getValue();
					if (itemId != null) {
						final SaveSearchResultWithBLOBs data = beanItem
								.getItem(itemId).getBean();

						String queryText = data.getQuerytext();
						List<SearchFieldInfo> fieldInfos = (List<SearchFieldInfo>) XStreamJsonDeSerializer
								.fromJson(queryText);
						// @HACK: === the library serialize with extra list
						// wrapper
						if (CollectionUtils.isEmpty(fieldInfos)) {
							throw new UserInvalidInputException(
									"There is no field in search criterion");
						}
						fieldInfos = (List<SearchFieldInfo>) fieldInfos.get(0);
						fillSearchFieldInfo(fieldInfos);

						if (filterBox.getComponentCount() <= 3) {
							Button updateBtn = new Button("Update");
							updateBtn.setStyleName(UIConstants.THEME_BLUE_LINK);
							updateBtn.setIcon(MyCollabResource
									.newResource("icons/16/crm/refresh.png"));
							updateBtn
									.addClickListener(new Button.ClickListener() {
										private static final long serialVersionUID = 1L;

										@Override
										public void buttonClick(ClickEvent event) {

											List<SearchFieldInfo> fieldInfos = buildSearchFieldInfos();
											SaveSearchResultService saveSearchResultService = ApplicationContextUtil
													.getSpringBean(SaveSearchResultService.class);
											data.setSaveuser(AppContext
													.getUsername());
											data.setSaccountid(AppContext
													.getAccountId());
											data.setQuerytext(XStreamJsonDeSerializer
													.toJson(fieldInfos));
											saveSearchResultService
													.updateWithSession(
															data,
															AppContext
																	.getUsername());

										}
									});

							Button deleteBtn = new Button(AppContext
									.getMessage(GenericI18Enum.BUTTON_DELETE),
									new Button.ClickListener() {
										private static final long serialVersionUID = 1L;

										@Override
										public void buttonClick(ClickEvent event) {
											SaveSearchResultService saveSearchResultService = ApplicationContextUtil
													.getSpringBean(SaveSearchResultService.class);
											saveSearchResultService.removeWithSession(
													data.getId(),
													AppContext.getUsername(),
													AppContext.getAccountId());
											searchContainer
													.removeAllComponents();
											if (filterBox.getComponentCount() > 2) {
												filterBox
														.removeComponent(filterBox
																.getComponent(1));
											}
											contructComboBox();
										}
									});
							deleteBtn.setStyleName(UIConstants.THEME_RED_LINK);
							deleteBtn.setIcon(MyCollabResource
									.newResource("icons/16/delete2.png"));

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

		@SuppressWarnings("unchecked")
		private void contructComboBox() {
			SaveSearchResultCriteria searchCriteria = new SaveSearchResultCriteria();
			searchCriteria.setType(new StringSearchField(searchCategory));
			searchCriteria.setCreateUser(new StringSearchField(AppContext
					.getUsername()));
			searchCriteria.setSaccountid(new NumberSearchField(AppContext
					.getAccountId()));

			SaveSearchResultService saveSearchResultService = ApplicationContextUtil
					.getSpringBean(SaveSearchResultService.class);
			List<SaveSearchResultWithBLOBs> result = saveSearchResultService
					.findPagableListByCriteria(new SearchRequest<SaveSearchResultCriteria>(
							searchCriteria, 0, Integer.MAX_VALUE));
			beanItem = new BeanContainer<String, SaveSearchResultWithBLOBs>(
					SaveSearchResultWithBLOBs.class);
			beanItem.setBeanIdProperty("id");

			for (SaveSearchResultWithBLOBs searchResult : result) {
				beanItem.addBean(searchResult);
			}
			this.setContainerDataSource(beanItem);
			this.setItemCaptionPropertyId("queryname");
		}
	}
}
