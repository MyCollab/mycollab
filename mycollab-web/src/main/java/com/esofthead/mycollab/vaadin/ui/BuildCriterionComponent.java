package com.esofthead.mycollab.vaadin.ui;

import java.util.Collection;
import java.util.Iterator;

import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.core.arguments.SearchCriteria;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.core.db.query.CompositionStringParam;
import com.esofthead.mycollab.core.db.query.DateParam;
import com.esofthead.mycollab.core.db.query.DateTimeParam;
import com.esofthead.mycollab.core.db.query.NumberParam;
import com.esofthead.mycollab.core.db.query.Param;
import com.esofthead.mycollab.core.db.query.PropertyListParam;
import com.esofthead.mycollab.core.db.query.PropertyParam;
import com.esofthead.mycollab.core.db.query.StringListParam;
import com.esofthead.mycollab.core.db.query.StringParam;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
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
	private Class<S> type;

	private VerticalLayout searchContainer;

	public BuildCriterionComponent(Param[] paramFields, Class<S> type) {
		this.setSpacing(true);
		this.paramFields = paramFields;
		this.type = type;

		this.searchContainer = new VerticalLayout();
		this.searchContainer.setSpacing(true);

		HorizontalLayout controlsBtn = new HorizontalLayout();
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

		Button saveSearchCriteriaBtn = new Button("Save Search");
		controlsBtn.addComponent(saveSearchCriteriaBtn);

		this.addComponent(searchContainer);
		this.addComponent(controlsBtn);
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
						} else if (field instanceof DateTimeParam) {
							compareSelectionBox.loadData(DateTimeParam.OPTIONS);
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

			if (field instanceof StringParam) {
				valueBox.addComponent(new TextField());
			} else if (field instanceof NumberParam) {
				valueBox.addComponent(new TextField());
			} else if (field instanceof DateParam) {
				if (DateParam.BETWEEN.equals(compareItem)) {
					DateField field1 = new DateField();
					DateField field2 = new DateField();
					valueBox.addComponent(field1);
					valueBox.addComponent(field2);
				} else {
					valueBox.addComponent(new DateField());
				}
			} else if (field instanceof DateTimeParam) {

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
					return null;
				} else if (param instanceof CompositionStringParam) {
					if (valueBox.getComponentCount() != 1) {
						return null;
					}
					TextField field = (TextField) valueBox.getComponent(0);
					String value = field.getValue();
					CompositionStringParam wrapParam = (CompositionStringParam) param;
					return wrapParam.buildSearchField(prefixOperation,
							compareOper, value);
				} else {
					throw new MyCollabException("Not support yet");
				}
			}
			return null;
		}
	}
}
