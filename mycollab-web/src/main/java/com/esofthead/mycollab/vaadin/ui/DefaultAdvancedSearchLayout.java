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

import java.util.List;

import org.vaadin.dialogs.ConfirmDialog;

import com.esofthead.mycollab.common.domain.SaveSearchResultWithBLOBs;
import com.esofthead.mycollab.common.domain.criteria.SaveSearchResultCriteria;
import com.esofthead.mycollab.common.localization.GenericI18Enum;
import com.esofthead.mycollab.common.service.SaveSearchResultService;
import com.esofthead.mycollab.configuration.SiteConfiguration;
import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchCriteria;
import com.esofthead.mycollab.core.arguments.SearchRequest;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.core.utils.JsonDeSerializer;
import com.esofthead.mycollab.core.utils.LocalizationHelper;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.MassItemActionHandler;
import com.esofthead.mycollab.vaadin.ui.GenericSearchPanel.SearchLayout;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.server.StreamResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 * @param <S>
 */
@SuppressWarnings("serial")
public abstract class DefaultAdvancedSearchLayout<S extends SearchCriteria>
		extends SearchLayout<S> {

	private SaveSearchResultService saveSearchResultService;

	private TextField saveSearchValue;
	private SavedSearchResultComboBox saveResultComboBox;
	private Label filterLabel = new Label("Filter");
	protected String type;

	private PopupButtonControl tableActionControls;
	private HorizontalLayout saveSearchControls;
	private Button addnewBtn;

	public DefaultAdvancedSearchLayout(DefaultGenericSearchPanel<S> parent,
			String type) {
		super(parent, "advancedSearch");
		setStyleName("advancedSearchLayout");

		saveSearchResultService = ApplicationContextUtil
				.getSpringBean(SaveSearchResultService.class);
		this.type = type;
		initLayout();
	}

	protected void initLayout() {
		ComponentContainer header = constructHeader();
		ComponentContainer body = constructBody();
		ComponentContainer footer = constructFooter();
		this.addComponent(header, "advSearchHeader");
		this.addComponent(body, "advSearchBody");
		this.addComponent(footer, "advSearchFooter");
	}

	public abstract ComponentContainer constructHeader();

	public abstract ComponentContainer constructBody();

	protected abstract void loadSaveSearchToField(S value);

	protected abstract void clearFields();

	private HorizontalLayout createButtonControls() {
		HorizontalLayout buttonControls = new HorizontalLayout();
		buttonControls.setSpacing(true);
		final Button searchBtn = new Button(
				LocalizationHelper.getMessage(GenericI18Enum.BUTTON_SEARCH),
				new Button.ClickListener() {
					@Override
					public void buttonClick(final ClickEvent event) {
						DefaultAdvancedSearchLayout.this.callSearchAction();
					}
				});
		UiUtils.addComponent(buttonControls, searchBtn, Alignment.MIDDLE_CENTER);
		searchBtn.setStyleName(UIConstants.THEME_BLUE_LINK);

		final Button clearBtn = new Button(
				LocalizationHelper.getMessage(GenericI18Enum.BUTTON_CLEAR),
				new Button.ClickListener() {
					@Override
					public void buttonClick(final ClickEvent event) {
						clearFields();
					}
				});
		clearBtn.setStyleName(UIConstants.THEME_BLUE_LINK);
		UiUtils.addComponent(buttonControls, clearBtn, Alignment.MIDDLE_CENTER);
		final Button basicSearchBtn = new Button(
				LocalizationHelper
						.getMessage(GenericI18Enum.BUTTON_BASIC_SEARCH),
				new Button.ClickListener() {
					@Override
					public void buttonClick(final ClickEvent event) {
						((DefaultGenericSearchPanel<S>) DefaultAdvancedSearchLayout.this.searchPanel)
								.moveToBasicSearchLayout();
					}
				});
		basicSearchBtn.setStyleName("link");
		UiUtils.addComponent(buttonControls, basicSearchBtn,
				Alignment.MIDDLE_CENTER);
		return buttonControls;
	}

	private HorizontalLayout createSaveSearchControls() {
		final HorizontalLayout saveSearchControls = new HorizontalLayout();
		// ----- Defined reUsed Layout -----------------------
		saveSearchValue = new TextField();
		saveResultComboBox = new SavedSearchResultComboBox();

		addnewBtn = new Button("New");
		final Button saveBtn = new Button("Save");
		final Button updateBtn = new Button("Update");
		final Button cancelBtn = new Button("Cancel");

		addnewBtn.setStyleName(UIConstants.THEME_BLUE_LINK);
		saveBtn.setStyleName(UIConstants.THEME_BLUE_LINK);
		updateBtn.setStyleName(UIConstants.THEME_BLUE_LINK);
		cancelBtn.setStyleName(UIConstants.THEME_BLUE_LINK);

		// tableActionControll for Update group controls
		tableActionControls = new PopupButtonControl("updateSearch", updateBtn);

		tableActionControls.addOptionItem("delete",
				LocalizationHelper.getMessage(GenericI18Enum.BUTTON_DELETE));
		tableActionControls.addOptionItem("new", "New");
		tableActionControls.setVisible(true);

		// ---- Default Layout Generation ----------
		UiUtils.addComponent(saveSearchControls, filterLabel,
				Alignment.MIDDLE_RIGHT);
		UiUtils.addComponent(saveSearchControls, saveResultComboBox,
				Alignment.MIDDLE_RIGHT);
		UiUtils.addComponent(saveSearchControls, addnewBtn,
				Alignment.MIDDLE_RIGHT);

		// -----Defined Listener ---------------------
		addnewBtn.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				saveSearchControls.removeComponent(addnewBtn);
				UiUtils.addComponent(saveSearchControls, saveBtn,
						Alignment.MIDDLE_RIGHT);
				UiUtils.addComponent(saveSearchControls, cancelBtn,
						Alignment.MIDDLE_RIGHT);
				saveSearchControls.replaceComponent(saveResultComboBox,
						saveSearchValue);
			}
		});
		saveBtn.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				S searchCriteria = fillupSearchCriteria();
				if (searchCriteria != null
						&& saveSearchValue.getValue() != null) {
					SaveSearchResultWithBLOBs searchResult = new SaveSearchResultWithBLOBs();
					searchResult.setSaveuser(AppContext.getUsername());
					searchResult.setSaccountid(AppContext.getAccountId());
					searchResult.setQuerytext(JsonDeSerializer
							.toJson(searchCriteria));
					searchResult.setType(type);
					searchResult.setQueryname((String) saveSearchValue
							.getValue());

					saveSearchResultService.saveWithSession(searchResult,
							AppContext.getUsername());
					NotificationUtil
							.showNotification("Query is saved successfully.");
					saveSearchValue.setValue("");

					BeanContainer<String, SaveSearchResultWithBLOBs> beanData = saveResultComboBox
							.getBeanIteam();
					beanData.addBean(searchResult);
					saveResultComboBox.setContainerDataSource(beanData);
					saveResultComboBox.setItemCaptionPropertyId("queryname");

					saveResultComboBox.setValue(searchResult.getId());
				}

				saveSearchControls.replaceComponent(saveSearchValue,
						saveResultComboBox);
				saveSearchControls.removeComponent(cancelBtn);
				saveSearchControls.removeComponent(saveBtn);
				saveSearchControls.addComponent(tableActionControls);
			}
		});
		cancelBtn.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				saveSearchControls.removeComponent(cancelBtn);
				saveSearchControls.replaceComponent(saveBtn, addnewBtn);
				saveSearchControls.replaceComponent(saveSearchValue,
						saveResultComboBox);
			}
		});
		updateBtn.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				Integer itemId = (Integer) saveResultComboBox.getValue();
				if (itemId != null) {
					BeanContainer<String, SaveSearchResultWithBLOBs> beanData = saveResultComboBox
							.getBeanIteam();

					String captionStr = saveResultComboBox
							.getItemCaption(itemId);
					S searchCriteria = fillupSearchCriteria();

					SaveSearchResultWithBLOBs searchResult = new SaveSearchResultWithBLOBs();
					searchResult.setSaveuser(AppContext.getUsername());
					searchResult.setSaccountid(AppContext.getAccountId());
					searchResult.setQuerytext(JsonDeSerializer
							.toJson(searchCriteria));
					searchResult.setType(type);
					searchResult.setId(itemId);
					searchResult.setQueryname(captionStr);

					saveSearchResultService.updateWithSession(searchResult,
							AppContext.getUsername());
					NotificationUtil.showNotification("Updated successfully.");

					beanData.removeItem(itemId);
					beanData.addBean(searchResult);
					saveResultComboBox.setContainerDataSource(beanData);
					saveResultComboBox.setItemCaptionPropertyId("queryname");

					saveResultComboBox.setValue(searchResult.getId());
				}
			}
		});
		tableActionControls
				.addMassItemActionHandler(new MassItemActionHandler() {
					@Override
					public void onSelect(String id) {
						if ("delete".equals(id)) {
							ConfirmDialogExt.show(
									UI.getCurrent(),
									LocalizationHelper.getMessage(
											GenericI18Enum.DELETE_DIALOG_TITLE,
											SiteConfiguration.getSiteName()),
									"Do you want to delete record?",
									LocalizationHelper
											.getMessage(GenericI18Enum.BUTTON_YES_LABEL),
									LocalizationHelper
											.getMessage(GenericI18Enum.BUTTON_NO_LABEL),
									new ConfirmDialog.Listener() {
										private static final long serialVersionUID = 1L;

										@Override
										public void onClose(ConfirmDialog dialog) {
											if (dialog.isConfirmed()) {
												Integer itemDelete = (Integer) saveResultComboBox
														.getValue();
												saveSearchResultService
														.removeWithSession(
																itemDelete,
																AppContext
																		.getUsername(),
																AppContext
																		.getAccountId());
												NotificationUtil
														.showNotification("Delete saved query successfully.");

												BeanContainer<String, SaveSearchResultWithBLOBs> beanData = saveResultComboBox
														.getBeanIteam();
												beanData.removeItem(itemDelete);
												saveResultComboBox
														.setContainerDataSource(beanData);
												saveResultComboBox
														.setItemCaptionPropertyId("queryname");

												clearFields();
												saveResultComboBox
														.setValue(null);
											}
										}
									}); // end confirm Dialog
						} else if ("new".equals(id)) {
							saveSearchControls.replaceComponent(
									tableActionControls, saveBtn);
							saveSearchControls.replaceComponent(
									saveResultComboBox, saveSearchValue);
							cancelBtn.addClickListener(new ClickListener() {
								@Override
								public void buttonClick(ClickEvent event) {
									saveSearchControls
											.replaceComponent(saveSearchValue,
													saveResultComboBox);
									saveSearchControls.replaceComponent(
											saveBtn, tableActionControls);
									saveSearchControls
											.removeComponent(cancelBtn);
									saveSearchControls
											.removeComponent(addnewBtn);
								}
							});
							saveSearchControls.addComponent(cancelBtn);
						}
					}

					@Override
					public StreamResource buildStreamResource(String id) {
						throw new MyCollabException(
								"Does not support download feature");
					}
				});

		return saveSearchControls;
	}

	public ComponentContainer constructFooter() {
		// ------Define VerticalLayout for footerLayout ------------------
		VerticalLayout footerLayout = new VerticalLayout();
		footerLayout.setSpacing(true);
		footerLayout.setMargin(new MarginInfo(false, true, false, false));
		// ------Define & contruct TopfooterLayout ------------------------
		HorizontalLayout topfooterLayout = new HorizontalLayout();
		topfooterLayout.setWidth("100%");

		Label spaceLbl = new Label("&nbsp;", ContentMode.HTML);
		topfooterLayout.addComponent(spaceLbl);
		topfooterLayout.setExpandRatio(spaceLbl, 1.0f);

		HorizontalLayout buttonControls = createButtonControls();
		UiUtils.addComponent(topfooterLayout, buttonControls,
				Alignment.MIDDLE_CENTER);
		buttonControls.setMargin(new MarginInfo(false, true, false, false));
		topfooterLayout.setExpandRatio(buttonControls, 1.0f);

		saveSearchControls = createSaveSearchControls();
		saveSearchControls.setSpacing(true);
		saveSearchControls.setMargin(new MarginInfo(false, true, false, false));
		UiUtils.addComponent(topfooterLayout, saveSearchControls,
				Alignment.MIDDLE_RIGHT);
		topfooterLayout.setExpandRatio(saveSearchControls, 1.0f);

		footerLayout.addComponent(topfooterLayout);
		return footerLayout;
	}

	private class SavedSearchResultComboBox extends ComboBox {
		BeanContainer<String, SaveSearchResultWithBLOBs> beanItem;

		public SavedSearchResultComboBox() {
			this.setImmediate(true);
			this.setItemCaptionMode(ItemCaptionMode.PROPERTY);

			contructComboBox();

			this.addValueChangeListener(new ValueChangeListener() {
				@Override
				public void valueChange(
						com.vaadin.data.Property.ValueChangeEvent event) {
					Object itemId = SavedSearchResultComboBox.this.getValue();
					if (saveResultComboBox != null)
						itemId = saveResultComboBox.getValue();
					if (itemId != null) {
						SaveSearchResultWithBLOBs data = beanItem.getItem(
								itemId).getBean();

						saveSearchValue.setValue("");
						String queryText = data.getQuerytext();
						Class<?> type2 = DefaultAdvancedSearchLayout.this
								.getType();
						S value = (S) JsonDeSerializer.fromJson(queryText,
								type2);
						loadSaveSearchToField(value);
						saveSearchControls.replaceComponent(addnewBtn,
								tableActionControls);
					} else {
						saveSearchControls.removeComponent(tableActionControls);
						saveSearchControls.addComponent(addnewBtn);
					}
				}
			});
			this.setImmediate(true);
		}

		public void contructComboBox() {
			SaveSearchResultCriteria searchCriteria = new SaveSearchResultCriteria();
			searchCriteria.setType(new StringSearchField(type));
			searchCriteria.setCreateUser(new StringSearchField(AppContext
					.getUsername()));
			searchCriteria.setSaccountid(new NumberSearchField(AppContext
					.getAccountId()));

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

		public BeanContainer<String, SaveSearchResultWithBLOBs> getBeanIteam() {
			return beanItem;
		}
	}

	protected abstract Class<S> getType();
}
