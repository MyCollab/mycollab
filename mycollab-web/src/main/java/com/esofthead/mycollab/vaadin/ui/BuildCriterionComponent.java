package com.esofthead.mycollab.vaadin.ui;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.esofthead.mycollab.common.domain.SaveSearchResultWithBLOBs;
import com.esofthead.mycollab.common.domain.criteria.SaveSearchResultCriteria;
import com.esofthead.mycollab.common.service.SaveSearchResultService;
import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchCriteria;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.core.arguments.SearchRequest;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.core.db.query.CompositionStringParam;
import com.esofthead.mycollab.core.db.query.ConcatStringParam;
import com.esofthead.mycollab.core.db.query.DateParam;
import com.esofthead.mycollab.core.db.query.JsonDeSerializerHelper;
import com.esofthead.mycollab.core.db.query.NumberParam;
import com.esofthead.mycollab.core.db.query.Param;
import com.esofthead.mycollab.core.db.query.PropertyListParam;
import com.esofthead.mycollab.core.db.query.PropertyParam;
import com.esofthead.mycollab.core.db.query.SearchFieldInfo;
import com.esofthead.mycollab.core.db.query.StringListParam;
import com.esofthead.mycollab.core.db.query.StringParam;
import com.esofthead.mycollab.core.utils.JsonDeSerializer;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.vaadin.popupbutton.PopupButtonExt;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
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
		headerBox.addComponent(new Label("Saved Filter: "));
		filterBox = new HorizontalLayout();
		filterBox.setSpacing(true);
		headerBox.addComponent(filterBox);
		this.addComponent(filterBox);
		buildFilterBox(null);

		this.searchContainer = new VerticalLayout();
		this.searchContainer.setSpacing(true);

		controlsBtn = new HorizontalLayout();
		controlsBtn.setSpacing(true);

		Button addCriteriaBtn = new Button("Add Criteria",
				new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(ClickEvent event) {
						CriteriaSelectionLayout newCriteriaBar = new CriteriaSelectionLayout(
								searchContainer.getComponentCount() + 1);
						searchContainer.addComponent(newCriteriaBar);
					}
				});
		controlsBtn.addComponent(addCriteriaBtn);

		this.addComponent(searchContainer);
		this.addComponent(controlsBtn);
	}

	private void buildFilterBox(String queryname) {
		filterBox.removeAllComponents();

		filterComboBox = new SavedSearchResultComboBox();
		filterBox.addComponent(filterComboBox);

		Button saveSearchBtn = new Button("New Filter",
				new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(ClickEvent event) {
						buildSaveFilterBox();
					}
				});
		filterBox.addComponent(saveSearchBtn);

		if (queryname != null) {

		}
	}

	private void buildSaveFilterBox() {
		filterBox.removeAllComponents();
		final TextField queryTextField = new TextField();
		filterBox.addComponent(queryTextField);

		Button saveBtn = new Button("Save", new Button.ClickListener() {
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

		Button cancelBtn = new Button("Cancel", new Button.ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				buildFilterBox(null);
			}
		});
		filterBox.addComponent(cancelBtn);
	}

	private void saveSearchCriteria(String queryText) {
		List<SearchFieldInfo> fieldInfos = buildSearchFieldInfos();

		SaveSearchResultService saveSearchResultService = ApplicationContextUtil
				.getSpringBean(SaveSearchResultService.class);
		SaveSearchResultWithBLOBs searchResult = new SaveSearchResultWithBLOBs();
		searchResult.setSaveuser(AppContext.getUsername());
		searchResult.setSaccountid(AppContext.getAccountId());
		searchResult.setQuerytext(JsonDeSerializer.toJson(fieldInfos));
		searchResult.setType(searchCategory);
		searchResult.setQueryname(queryText);
		saveSearchResultService.saveWithSession(searchResult,
				AppContext.getUsername());
		buildFilterBox(queryText);
	}

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

			indexLbl = new Label(index + "");
			this.addComponent(indexLbl, 0, 0);
			operatorSelectionBox = new ValueComboBox(false, SearchField.AND,
					SearchField.OR);
			operatorSelectionBox.setWidth("60px");
			this.addComponent(operatorSelectionBox, 1, 0);

			if (index == 1) {
				operatorSelectionBox.setVisible(false);
			}

			buildFieldSelectionBox();

			valueBox = new VerticalLayout();
			deleteBtn = new Button("Delete", new Button.ClickListener() {
				private static final long serialVersionUID = 1L;

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
			operatorSelectionBox.setValue(searchFieldInfo.getPrefixOper());

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
				valueBox.addComponent(valueField);
			} else if (param instanceof NumberParam) {
				TextField valueField = new TextField();
				valueField.setValue(String.valueOf(searchFieldInfo.getValue()));
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
					valueBox.addComponent(field1);
					valueBox.addComponent(field2);
				} else {
					DateField field = new DateField();
					field.setValue((Date) searchFieldInfo.getValue());
					valueBox.addComponent(field);
				}
			} else if (param instanceof PropertyParam
					|| param instanceof PropertyListParam) {
				Component comp = buildPropertySearchComp(param.getId());
				if (comp != null) {
					if (comp instanceof CustomField<?>
							&& (((CustomField) comp).getType() == Integer.class)) {
						((Field) comp).setValue(Integer
								.parseInt(searchFieldInfo.getValue() + ""));
					} else {
						((Field) comp).setValue(searchFieldInfo.getValue());
					}

					valueBox.addComponent(comp);
				}
			} else if (param instanceof StringListParam) {
				ValueListSelect listSelect = new ValueListSelect();
				listSelect.setCaption(null);
				listSelect.loadData(((StringListParam) param).getLstValues()
						.toArray(new String[0]));
				listSelect.setValue(searchFieldInfo.getValue());
				valueBox.addComponent(listSelect);
			} else if (param instanceof CompositionStringParam) {
				valueBox.addComponent(new TextField());
			}
		}

		private void buildFieldSelectionBox() {
			fieldSelectionBox = new ComboBox();
			fieldSelectionBox.setImmediate(true);
			fieldSelectionBox.setItemCaptionMode(ItemCaptionMode.EXPLICIT);
			for (Param field : paramFields) {
				fieldSelectionBox.addItem(field);
				fieldSelectionBox.setItemCaption(field, field.getDisplayName());
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
						} else if (field instanceof PropertyListParam) {
							compareSelectionBox
									.loadData(PropertyListParam.OPTIONS);
						} else if (field instanceof StringListParam) {
							compareSelectionBox
									.loadData(StringListParam.OPTIONS);
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
			String compareItem = (String) compareSelectionBox.getValue();
			valueBox.removeAllComponents();

			if (field instanceof StringParam
					|| field instanceof ConcatStringParam) {
				valueBox.addComponent(new TextField());
			} else if (field instanceof NumberParam) {
				valueBox.addComponent(new TextField());
			} else if (field instanceof DateParam) {
				if (DateParam.BETWEEN.equals(compareItem)
						|| DateParam.NOT_BETWEEN.equals(compareItem)) {
					DateField field1 = new DateField();
					DateField field2 = new DateField();
					valueBox.addComponent(field1);
					valueBox.addComponent(field2);
				} else {
					valueBox.addComponent(new DateField());
				}
			} else if (field instanceof PropertyParam
					|| field instanceof PropertyListParam) {
				Component comp = buildPropertySearchComp(field.getId());
				if (comp != null) {
					valueBox.addComponent(comp);
				}
			} else if (field instanceof StringListParam) {
				ValueListSelect listSelect = new ValueListSelect();
				listSelect.setCaption(null);
				listSelect.loadData(((StringListParam) field).getLstValues()
						.toArray(new String[0]));
				valueBox.addComponent(listSelect);
			} else if (field instanceof CompositionStringParam) {
				valueBox.addComponent(new TextField());
			}
		}

		private SearchFieldInfo buildSearchFieldInfo() {
			String prefixOper = (String) operatorSelectionBox.getValue();
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

		private SearchField buildSearchField() {
			Param param = (Param) fieldSelectionBox.getValue();
			String prefixOperation = (String) operatorSelectionBox.getValue();
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
					if (value.size() == 0) {
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
					if (value.size() == 0) {
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

				@Override
				public void valueChange(
						com.vaadin.data.Property.ValueChangeEvent event) {
					Object itemId = SavedSearchResultComboBox.this.getValue();
					if (itemId != null) {
						final SaveSearchResultWithBLOBs data = beanItem
								.getItem(itemId).getBean();

						String queryText = data.getQuerytext();
						List<SearchFieldInfo> fieldInfos = JsonDeSerializerHelper
								.fromJson(queryText);
						fillSearchFieldInfo(fieldInfos);

						if (filterBox.getComponentCount() <= 2) {
							Button updateBtn = new Button("Update");
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
											data.setQuerytext(JsonDeSerializer
													.toJson(fieldInfos));
											saveSearchResultService
													.updateWithSession(
															data,
															AppContext
																	.getUsername());

										}
									});
							SplitButton optionBtn = new SplitButton(updateBtn);

							final VerticalLayout optionContent = new VerticalLayout();
							Button deleteBtn = new Button("Delete",
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
							optionContent.addComponent(deleteBtn);
							optionBtn.setContent(optionContent);

							filterBox.addComponent(optionBtn, 1);
						}

					} else {
						searchContainer.removeAllComponents();
						if (filterBox.getComponentCount() > 2) {
							filterBox.removeComponent(filterBox.getComponent(1));
						}
					}
				}
			});
			this.setImmediate(true);
		}

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
