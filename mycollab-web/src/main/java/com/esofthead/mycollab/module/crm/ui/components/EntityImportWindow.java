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
package com.esofthead.mycollab.module.crm.ui.components;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.easyuploads.SingleFileUploadField;

import au.com.bytecode.opencsv.CSVReader;

import com.esofthead.mycollab.common.i18n.FileI18nEnum;
import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.core.persistence.service.ICrudService;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.iexporter.CSVImportEntityProcess;
import com.esofthead.mycollab.iexporter.CSVObjectEntityConverter.FieldMapperDef;
import com.esofthead.mycollab.iexporter.CSVObjectEntityConverter.ImportFieldDef;
import com.esofthead.mycollab.module.crm.domain.Contact;
import com.esofthead.mycollab.module.crm.domain.criteria.ContactSearchCriteria;
import com.esofthead.mycollab.module.crm.events.ContactEvent;
import com.esofthead.mycollab.module.crm.service.ContactService;
import com.esofthead.mycollab.module.crm.view.contact.ContactListView;
import com.esofthead.mycollab.module.crm.view.contact.iexport.ContactVCardObjectEntityConverter;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.ConfirmDialogExt;
import com.esofthead.mycollab.vaadin.ui.GridFormLayoutHelper;
import com.esofthead.mycollab.vaadin.ui.NotificationUtil;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.esofthead.mycollab.vaadin.ui.UiUtils;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import ezvcard.Ezvcard;
import ezvcard.VCard;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public abstract class EntityImportWindow<E> extends Window {
	private static final long serialVersionUID = 1L;

	public static final String[] fileType = { "CSV", "VCard" };

	private FileConfigurationLayout fileConfigurationLayout;
	private MappingCrmConfigurationLayout mappingCrmFieldLayout;
	private boolean isSupportCSV = true;
	private boolean isSupportVCard = false;
	private ICrudService services;
	private Class<E> cls;

	public EntityImportWindow(boolean isSupportVCard, String title,
			ICrudService service, Class<E> cls) {
		super(title);
		center();
		this.setWidth("1000px");
		this.setResizable(false);
		this.setStyleName("entity-import-window");
		this.setModal(true);
		this.isSupportVCard = isSupportVCard;
		this.services = service;
		this.cls = cls;

		fileConfigurationLayout = new FileConfigurationLayout();
		this.setContent(fileConfigurationLayout);
	}

	abstract protected List<FieldMapperDef> constructCSVFieldMapper();

	abstract protected void reloadWhenBackToListView();

	private class FileConfigurationLayout extends VerticalLayout {
		private static final long serialVersionUID = 1L;
		private InputStream contentStream;
		private CheckBox hasHeaderCheckBox;
		private SingleFileUploadField uploadField;
		private VerticalLayout uploadFieldVerticalLayout;

		private ComboBox fileformatComboBox;

		public FileConfigurationLayout() {
			final VerticalLayout layout = new VerticalLayout();
			layout.setWidth("100%");
			layout.setMargin(true);
			layout.setSpacing(true);

			final HorizontalLayout informationLayout = new HorizontalLayout();
			informationLayout.setWidth("100%");
			informationLayout.setSpacing(true);

			CssLayout fileUploadLayout = fileUploadLayout();
			CssLayout fileInfomationLayout = fileConfigurationLayout();

			informationLayout.addComponent(fileUploadLayout);
			informationLayout.addComponent(fileInfomationLayout);
			layout.addComponent(informationLayout);

			HorizontalLayout controlGroupBtn = new HorizontalLayout();
			controlGroupBtn.setSpacing(true);
			Button nextBtn = new Button("Next");

			nextBtn.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void buttonClick(ClickEvent event) {
					try {
						contentStream = uploadField.getContentAsStream();
					} catch (Exception e) {
						NotificationUtil.showWarningNotification(AppContext
								.getMessage(FileI18nEnum.NOT_ATTACH_FILE_WARNING));
					}
					if (contentStream != null) {
						String filename = uploadField.getFileName();
						String fileuploadType = filename.substring(
								filename.indexOf(".") + 1, filename.length());
						if (fileuploadType.equals("vcf") && isSupportVCard) {
							ConfirmDialogExt.show(
									UI.getCurrent(),
									"Message information",
									"You choose a vcf file. This step will import to database. Do you want to do it?",
									AppContext
											.getMessage(FileI18nEnum.IMPORT_FILE),
									AppContext
											.getMessage(GenericI18Enum.BUTTON_CANCEL_LABEL),
									new ConfirmDialog.Listener() {
										private static final long serialVersionUID = 1L;

										@Override
										public void onClose(ConfirmDialog dialog) {
											if (dialog.isConfirmed()) {
												try {
													ContactService contactService = ApplicationContextUtil
															.getSpringBean(ContactService.class);
													List<VCard> lstVcard = Ezvcard
															.parse(contentStream)
															.all();
													for (VCard vcard : lstVcard) {
														ContactVCardObjectEntityConverter converter = new ContactVCardObjectEntityConverter();
														Contact add = converter
																.convert(
																		Contact.class,
																		vcard);
														add.setCreatedtime(new Date());
														add.setSaccountid(AppContext
																.getAccountId());
														contactService
																.saveWithSession(
																		add,
																		AppContext
																				.getUsername());
													}

													NotificationUtil
															.showNotification(AppContext
																	.getMessage(FileI18nEnum.IMPORT_FILE_SUCCESS));
													EntityImportWindow.this
															.close();
													ContactSearchCriteria contactSearchCriteria = new ContactSearchCriteria();
													contactSearchCriteria
															.setSaccountid(new NumberSearchField(
																	AppContext
																			.getAccountId()));
													contactSearchCriteria
															.setContactName(new StringSearchField(
																	""));
													EventBusFactory.getInstance()
															.post(
																	new ContactEvent.GotoList(
																			ContactListView.class,
																			new ContactSearchCriteria()));
												} catch (IOException e) {
													throw new MyCollabException(
															e);
												}
											}
										}
									});
						} else if (fileuploadType.equals("csv") && isSupportCSV) {
							File uploadFile = uploadField.getContentAsFile();
							if (uploadFile != null) {
								mappingCrmFieldLayout = new MappingCrmConfigurationLayout(
										hasHeaderCheckBox.getValue(),
										uploadFile);
								// EntityImportWindow.this
								// .removeComponent(fileConfigurationLayout);
								//
								// EntityImportWindow.this.setWidth("800px");
								//
								// EntityImportWindow.this.center();
								//
								// EntityImportWindow.this
								// .addComponent(mappingCrmFieldLayout);
							}

						} else {
							int uploadFieldIndex = uploadFieldVerticalLayout
									.getComponentIndex(uploadField);
							uploadFieldVerticalLayout
									.removeComponent(uploadField);

							uploadField = new SingleFileUploadField();
							uploadField.addListener(new ValueChangeListener() {
								private static final long serialVersionUID = 1L;

								@Override
								public void valueChange(ValueChangeEvent event) {
									String filename = uploadField.getFileName();
									String fileuploadType = filename.substring(
											filename.indexOf(".") + 1,
											filename.length());
									if (fileuploadType.equals("vcf")) {
										fileformatComboBox.setValue("VCard");
										fileformatComboBox.setEnabled(false);
									} else if (fileuploadType.equals("csv")) {
										fileformatComboBox.setValue("CSV");
										fileformatComboBox.setEnabled(false);
									}
								}
							});

							uploadFieldVerticalLayout.addComponent(uploadField,
									uploadFieldIndex);

							NotificationUtil.showWarningNotification(AppContext
									.getMessage(FileI18nEnum.CHOOSE_SUPPORT_FILE_TYPES_WARNING));

						}
					}
				}
			});
			nextBtn.addStyleName(UIConstants.THEME_GREEN_LINK);
			UiUtils.addComponent(controlGroupBtn, nextBtn,
					Alignment.MIDDLE_CENTER);

			Button cancelBtn = new Button(
					AppContext.getMessage(GenericI18Enum.BUTTON_CANCEL_LABEL));
			cancelBtn.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void buttonClick(ClickEvent event) {
					EntityImportWindow.this.close();
				}
			});
			cancelBtn.addStyleName(UIConstants.THEME_BLANK_LINK);
			UiUtils.addComponent(controlGroupBtn, cancelBtn,
					Alignment.MIDDLE_CENTER);

			UiUtils.addComponent(layout, controlGroupBtn,
					Alignment.MIDDLE_CENTER);
			this.addComponent(layout);
		}

		@SuppressWarnings("unchecked")
		private CssLayout fileConfigurationLayout() {
			final CssLayout bodyLayoutWapper = new CssLayout();
			bodyLayoutWapper.addStyleName(UIConstants.BORDER_BOX_2);
			bodyLayoutWapper.setWidth("100%");

			final HorizontalLayout bodyLayout = new HorizontalLayout();
			bodyLayout.setSpacing(true);
			bodyLayout.setMargin(true);

			Label title = new Label("Step 2:");
			title.addStyleName("h3");
			UiUtils.addComponent(bodyLayout, title, Alignment.TOP_LEFT);

			VerticalLayout informationLayout = new VerticalLayout();
			informationLayout.setSpacing(true);
			informationLayout.setWidth("100%");

			GridFormLayoutHelper gridLayout = new GridFormLayoutHelper(1, 4,
					"100%", "200px", Alignment.TOP_LEFT);

			gridLayout.getLayout().setSpacing(true);
			gridLayout.getLayout().setMargin(false);
			gridLayout.getLayout().setWidth("100%");

			informationLayout.addComponent(new Label("Specify Format"));

			@SuppressWarnings("rawtypes")
			BeanItemContainer<String> fileformatType = new BeanItemContainer(
					String.class, Arrays.asList(fileType));

			fileformatComboBox = new ComboBox();
			fileformatComboBox.setContainerDataSource(fileformatType);
			fileformatComboBox.setNullSelectionAllowed(false);
			fileformatComboBox
					.setItemCaptionMode(ItemCaptionMode.EXPLICIT_DEFAULTS_ID);
			fileformatComboBox.setValue("VCard");
			if (isSupportCSV && isSupportVCard)
				fileformatComboBox.setEnabled(true);
			else {
				fileformatComboBox.setEnabled(false);
				if (isSupportCSV)
					fileformatComboBox.setValue("CSV");
				else
					fileformatComboBox.setValue("VCard");
			}

			gridLayout.addComponent(fileformatComboBox, "File Type", 0, 0);

			ComboBox encodingCombobox = new ComboBox();
			encodingCombobox
					.setItemCaptionMode(ItemCaptionMode.EXPLICIT_DEFAULTS_ID);
			encodingCombobox.addItem("UTF-8");
			encodingCombobox.setValue("UTF-8");
			encodingCombobox.setEnabled(false);
			gridLayout.addComponent(encodingCombobox, "Character Encoding", 0,
					1);

			ComboBox delimiterComboBox = new ComboBox();
			delimiterComboBox
					.setItemCaptionMode(ItemCaptionMode.EXPLICIT_DEFAULTS_ID);
			delimiterComboBox.addItem(",(comma)");
			delimiterComboBox.addItem("#,(sharp)");

			delimiterComboBox.setValue(",(comma)");
			delimiterComboBox.setEnabled(false);
			gridLayout.addComponent(delimiterComboBox, "Delimiter", 0, 2);

			HorizontalLayout checkboxHorizontalLayout = new HorizontalLayout();
			hasHeaderCheckBox = new CheckBox();
			checkboxHorizontalLayout.addComponent(hasHeaderCheckBox);

			Label checkboxMessageLabel = new Label(
					"(Has header at first-line?)");
			checkboxHorizontalLayout.addComponent(checkboxMessageLabel);

			gridLayout.addComponent(checkboxHorizontalLayout, "Has header", 0,
					3);
			informationLayout.addComponent(gridLayout.getLayout());

			bodyLayout.addComponent(informationLayout);
			bodyLayout.setExpandRatio(informationLayout, 1.0f);

			bodyLayoutWapper.addComponent(bodyLayout);
			return bodyLayoutWapper;
		}

		private CssLayout fileUploadLayout() {
			final CssLayout bodyLayoutWapper = new CssLayout();
			bodyLayoutWapper.setWidth("100%");
			bodyLayoutWapper.setHeight("100%");
			bodyLayoutWapper.addStyleName(UIConstants.BORDER_BOX_2);

			final HorizontalLayout bodyLayout = new HorizontalLayout();
			bodyLayout.setSpacing(true);
			bodyLayout.setMargin(true);
			bodyLayout.setHeight("100%");

			Label title = new Label("Step 1:");
			title.addStyleName("h3");

			UiUtils.addComponent(bodyLayout, title, Alignment.TOP_LEFT);

			uploadFieldVerticalLayout = new VerticalLayout();
			uploadFieldVerticalLayout.setSpacing(true);
			uploadFieldVerticalLayout.setMargin(false);
			uploadFieldVerticalLayout.setWidth("100%");

			uploadFieldVerticalLayout.addComponent(new Label("Select File"));

			uploadField = new SingleFileUploadField();
			uploadField.addStyleName(UIConstants.THEME_BLUE_LINK);
			uploadField.addListener(new ValueChangeListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void valueChange(ValueChangeEvent event) {
					String filename = uploadField.getFileName();
					String fileuploadType = filename.substring(
							filename.indexOf(".") + 1, filename.length());
					if (fileuploadType.equals("vcf")) {
						fileformatComboBox.setValue("VCard");
						fileformatComboBox.setEnabled(false);
					} else if (fileuploadType.equals("csv")) {
						fileformatComboBox.setValue("CSV");
						fileformatComboBox.setEnabled(false);
					}
				}
			});
			uploadFieldVerticalLayout.addComponent(uploadField);

			String fileTypeSupportString = (isSupportVCard) ? "Supported Fileds Type : VCF, CSV"
					: "Supported Files Type : CSV";
			uploadFieldVerticalLayout.addComponent(new Label(
					fileTypeSupportString));

			bodyLayout.addComponent(uploadFieldVerticalLayout);
			bodyLayout.setExpandRatio(uploadFieldVerticalLayout, 1.0f);

			bodyLayoutWapper.addComponent(bodyLayout);

			return bodyLayoutWapper;
		}
	}

	private class MappingCrmConfigurationLayout extends CssLayout {
		private static final long serialVersionUID = 1L;
		private VerticalLayout columnMappingCrmLayout;
		private GridFormLayoutHelper gridCrmMapping;
		private File uploadFile;
		private final List<FieldMapperDef> contactCrmFields = constructCSVFieldMapper();
		private VerticalLayout messageImportVerticalLayout;

		public MappingCrmConfigurationLayout(final boolean checkboxChecked,
				final File uploadFile) {
			this.uploadFile = uploadFile;
			this.setWidth("100%");
			this.addStyleName(UIConstants.BORDER_BOX_2);

			final HorizontalLayout bodyLayout = new HorizontalLayout();
			bodyLayout.setMargin(new MarginInfo(false, false, false, true));
			bodyLayout.setSpacing(true);

			final HorizontalLayout titleHorizontal = new HorizontalLayout();
			Label title = new Label("Step 3:");
			title.addStyleName("h2");
			titleHorizontal.addComponent(title);
			bodyLayout.addComponent(titleHorizontal);

			columnMappingCrmLayout = new VerticalLayout();
			columnMappingCrmLayout.setWidth("100%");
			columnMappingCrmLayout.setMargin(true);
			Label infoLabel = new Label("Map the columns to Module fields");
			infoLabel.addStyleName("h3");
			columnMappingCrmLayout.addComponent(infoLabel);
			try {
				gridCrmMapping = new GridFormLayoutHelper(2, (new CSVReader(
						new FileReader(uploadFile))).readNext().length + 2,
						"100%", "200px");
			} catch (Exception e) {
				throw new MyCollabException(e);
			}
			gridCrmMapping.getLayout().setMargin(true);
			gridCrmMapping.getLayout().setSpacing(true);

			Label header = new Label("Header");
			header.addStyleName("h3");

			Label crmLabel = new Label("CRM Fields");
			crmLabel.addStyleName("h3");
			Label colIndex = new Label("Colum Index");
			colIndex.addStyleName("h3");
			// IF has header
			if (checkboxChecked)
				gridCrmMapping.addComponentSupportFieldCaption(header,
						new Label(), "0px", "200px", 0, 0,
						Alignment.MIDDLE_CENTER);
			else {
				Label firstRowDataLabel = new Label("First Row Data");
				firstRowDataLabel.addStyleName("h3");
				gridCrmMapping.addComponentSupportFieldCaption(
						firstRowDataLabel, new Label(), "0px", "200px", 0, 0,
						Alignment.MIDDLE_CENTER);
			}
			gridCrmMapping.addComponentSupportFieldCaption(crmLabel, colIndex,
					"200px", "200px", 1, 0, Alignment.MIDDLE_CENTER);
			columnMappingCrmLayout.addComponent(gridCrmMapping.getLayout());

			HorizontalLayout controlGroupBtn = new HorizontalLayout();
			controlGroupBtn
					.setMargin(new MarginInfo(false, false, false, false));
			UiUtils.addComponent(columnMappingCrmLayout, controlGroupBtn,
					Alignment.MIDDLE_CENTER);

			Button saveBtn = new Button(
					AppContext.getMessage(GenericI18Enum.BUTTON_SAVE_LABEL),
					new ClickListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void buttonClick(ClickEvent event) {
							try {
								if (messageImportVerticalLayout != null) {
									columnMappingCrmLayout
											.removeComponent(messageImportVerticalLayout);
								}
								List<ImportFieldDef> listImportFieldDef = new ArrayList<ImportFieldDef>();
								for (int i = 0; i < gridCrmMapping.getLayout()
										.getRows(); i++) {
									Component componentOnGrid = gridCrmMapping
											.getComponent(1, i + 1);
									if (componentOnGrid instanceof HorizontalLayout) {
										Iterator<Component> lstComponentOnGrid = ((HorizontalLayout) componentOnGrid)
												.iterator();
										Component compent = lstComponentOnGrid
												.next();
										if (compent instanceof CSVBeanFieldComboBox) {
											ImportFieldDef importFieldDef = new ImportFieldDef(
													i,
													(FieldMapperDef) ((CSVBeanFieldComboBox) compent)
															.getValue());
											listImportFieldDef
													.add(importFieldDef);
										}
									}
								}

								CSVImportEntityProcess importProcess = new CSVImportEntityProcess();
								try {
									importProcess.doImport(uploadFile,
											checkboxChecked, services, cls,
											listImportFieldDef);
								} catch (IllegalArgumentException e) {
									StringBuffer msg = new StringBuffer(e
											.getMessage());
									if (msg.indexOf("numRowSuccess") > 0) {
										int numRowSuccess = Integer.parseInt(msg.substring(
												msg.indexOf("numRowSuccess:")
														+ "numRowSuccess:"
																.length(),
												msg.indexOf("numRowError:")));
										int numRowError = Integer.parseInt(msg.substring(
												msg.indexOf("numRowError:")
														+ "numRowError:"
																.length(),
												msg.indexOf("Detail:")));
										messageImportVerticalLayout = new VerticalLayout();
										messageImportVerticalLayout
												.setSpacing(true);

										Label msgLabel = new Label(
												"Import successfull "
														+ numRowSuccess
														+ " rows, " + "fail "
														+ numRowError
														+ " rows.");
										messageImportVerticalLayout
												.addComponent(msgLabel);

										final String[] errorDetail = msg
												.substring(
														msg.indexOf("Detail:")
																+ "Detail:"
																		.length())
												.split("//");
										int numErrMsgShowLimit = 3;
										for (String errMsg : errorDetail) {
											messageImportVerticalLayout
													.addComponent(new Label(
															errMsg));
											if (numErrMsgShowLimit == 0)
												break;
											numErrMsgShowLimit--;
										}
										columnMappingCrmLayout
												.addComponent(messageImportVerticalLayout);
									}
								}
							} catch (Exception e) {
								throw new MyCollabException(e);
							}
						}
					});

			saveBtn.addStyleName(UIConstants.THEME_GREEN_LINK);
			controlGroupBtn.addComponent(saveBtn);

			Button previousBtn = new Button("Previous", new ClickListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void buttonClick(ClickEvent event) {
					// EntityImportWindow.this.removeAllComponents();
					// EntityImportWindow.this.setWidth("950px");
					// EntityImportWindow.this
					// .addComponent(fileConfigurationLayout);
					EntityImportWindow.this.center();
				}
			});
			previousBtn.addStyleName(UIConstants.THEME_GREEN_LINK);
			controlGroupBtn.addComponent(previousBtn);

			Button btnClose = new Button("Close", new ClickListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void buttonClick(ClickEvent event) {
					EntityImportWindow.this.close();
					reloadWhenBackToListView();
				}
			});
			btnClose.addStyleName(UIConstants.THEME_GREEN_LINK);
			controlGroupBtn.addComponent(btnClose);
			bodyLayout.addComponent(columnMappingCrmLayout);
			this.addComponent(bodyLayout);

			fillDataToGridLayout();
		}

		@SuppressWarnings({ "resource" })
		private void fillDataToGridLayout() {
			CSVReader csvReader;
			try {
				csvReader = new CSVReader(new FileReader(uploadFile));
				String[] stringHeader = csvReader.readNext();
				for (int i = 0; i < stringHeader.length; i++) {

					final CSVBeanFieldComboBox crmFieldComboBox = new CSVBeanFieldComboBox(
							contactCrmFields);

					String headerStr = stringHeader[i];
					gridCrmMapping.addComponentSupportFieldCaption(new Label(
							headerStr), new Label(), "0px", "200px", 0, i + 1,
							Alignment.MIDDLE_CENTER);
					Label fieldCaptionColumnIndex = new Label("Column "
							+ (i + 1));
					gridCrmMapping.addComponentSupportFieldCaption(
							crmFieldComboBox, fieldCaptionColumnIndex, "200px",
							"200px", 1, i + 1, Alignment.MIDDLE_CENTER);
				}
			} catch (IOException e) {
				throw new MyCollabException(e);
			}
		}
	}
}
