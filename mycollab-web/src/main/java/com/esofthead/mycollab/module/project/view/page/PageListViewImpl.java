package com.esofthead.mycollab.module.project.view.page;

import java.util.List;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.core.utils.StringUtils;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectLinkBuilder;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.events.PageEvent;
import com.esofthead.mycollab.module.project.i18n.Page18InEnum;
import com.esofthead.mycollab.module.wiki.domain.Folder;
import com.esofthead.mycollab.module.wiki.domain.Page;
import com.esofthead.mycollab.module.wiki.domain.WikiResource;
import com.esofthead.mycollab.module.wiki.service.WikiService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.AbstractPageView;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.AbstractBeanFieldGroupEditFieldFactory;
import com.esofthead.mycollab.vaadin.ui.AdvancedEditBeanForm;
import com.esofthead.mycollab.vaadin.ui.GenericBeanForm;
import com.esofthead.mycollab.vaadin.ui.GridFormLayoutHelper;
import com.esofthead.mycollab.vaadin.ui.IFormLayoutFactory;
import com.esofthead.mycollab.vaadin.ui.MyCollabResource;
import com.esofthead.mycollab.vaadin.ui.SortButton;
import com.esofthead.mycollab.vaadin.ui.ToggleButtonGroup;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.esofthead.mycollab.vaadin.ui.UiUtils;
import com.vaadin.server.Sizeable;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Field;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.4.0
 * 
 */
@ViewComponent
public class PageListViewImpl extends AbstractPageView implements PageListView {
	private static final long serialVersionUID = 1L;

	private HorizontalLayout headerLayout;

	private VerticalLayout pagesLayout;

	private List<WikiResource> resources;

	public PageListViewImpl() {
		this.setMargin(new MarginInfo(false, true, false, true));

		headerLayout = new HorizontalLayout();
		this.addComponent(headerLayout);
		initHeader();

		pagesLayout = new VerticalLayout();
		pagesLayout.setStyleName("pages-list-layout");
		this.addComponent(pagesLayout);
	}

	private void initHeader() {
		Image titleIcon = new Image(null,
				MyCollabResource
						.newResource("icons/22/project/page_selected.png"));
		Label headerText = new Label(
				AppContext.getMessage(Page18InEnum.VIEW_LIST_TITLE));

		UiUtils.addComponent(headerLayout, titleIcon, Alignment.MIDDLE_LEFT);
		UiUtils.addComponent(headerLayout, headerText, Alignment.MIDDLE_LEFT);
		headerLayout.setExpandRatio(headerText, 1.0f);

		Label sortLbl = new Label(
				AppContext.getMessage(Page18InEnum.OPT_SORT_LABEL));
		sortLbl.setSizeUndefined();
		UiUtils.addComponent(headerLayout, sortLbl, Alignment.MIDDLE_RIGHT);

		ToggleButtonGroup sortGroup = new ToggleButtonGroup();
		UiUtils.addComponent(headerLayout, sortGroup, Alignment.MIDDLE_RIGHT);

		SortButton sortDateBtn = new SortButton(
				AppContext.getMessage(Page18InEnum.OPT_SORT_BY_DATE),
				new Button.ClickListener() {

					private static final long serialVersionUID = -6987503077975316907L;

					@Override
					public void buttonClick(Button.ClickEvent event) {
						// TODO Auto-generated method stub

					}
				});
		sortGroup.addButton(sortDateBtn);

		SortButton sortNameBtn = new SortButton(
				AppContext.getMessage(Page18InEnum.OPT_SORT_BY_NAME),
				new Button.ClickListener() {

					private static final long serialVersionUID = 2847554379518387585L;

					@Override
					public void buttonClick(Button.ClickEvent event) {
						// TODO Auto-generated method stub

					}
				});
		sortGroup.addButton(sortNameBtn);

		SortButton sortKindBtn = new SortButton(
				AppContext.getMessage(Page18InEnum.OPT_SORT_BY_KIND),
				new Button.ClickListener() {

					private static final long serialVersionUID = 2230933690084074590L;

					@Override
					public void buttonClick(Button.ClickEvent event) {
						// TODO Auto-generated method stub

					}
				});
		sortGroup.addButton(sortKindBtn);

		sortGroup.setDefaultButton(sortDateBtn);

		final Button newGroupBtn = new Button(
				AppContext.getMessage(Page18InEnum.BUTTON_NEW_GROUP),
				new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(final Button.ClickEvent event) {
						UI.getCurrent().addWindow(new PageGroupWindow());
					}
				});
		newGroupBtn.setStyleName(UIConstants.THEME_GREEN_LINK);
		newGroupBtn.setIcon(MyCollabResource
				.newResource("icons/16/addRecord.png"));
		newGroupBtn.setEnabled(CurrentProjectVariables
				.canWrite(ProjectRolePermissionCollections.PAGES));
		UiUtils.addComponent(headerLayout, newGroupBtn, Alignment.MIDDLE_RIGHT);

		final Button newPageBtn = new Button(
				AppContext.getMessage(Page18InEnum.BUTTON_NEW_PAGE),
				new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(final Button.ClickEvent event) {
						EventBusFactory.getInstance().post(
								new PageEvent.GotoAdd(this, null));
					}
				});
		newPageBtn.setStyleName(UIConstants.THEME_GREEN_LINK);
		newPageBtn.setIcon(MyCollabResource
				.newResource("icons/16/addRecord.png"));
		newPageBtn.setEnabled(CurrentProjectVariables
				.canWrite(ProjectRolePermissionCollections.PAGES));

		headerText.setStyleName(UIConstants.HEADER_TEXT);

		UiUtils.addComponent(headerLayout, newPageBtn, Alignment.MIDDLE_RIGHT);

		headerLayout.setStyleName(UIConstants.HEADER_VIEW);
		headerLayout.setWidth("100%");
		headerLayout.setHeight(Sizeable.SIZE_UNDEFINED, Sizeable.Unit.PIXELS);
		headerLayout.setSpacing(true);
		headerLayout.setMargin(new MarginInfo(true, false, true, false));
	}

	@Override
	public void displayPages(List<WikiResource> resources) {
		this.resources = resources;
		pagesLayout.removeAllComponents();
		if (resources != null) {
			for (WikiResource resource : resources) {
				Layout resourceBlock = resource instanceof Page ? displayPageBlock((Page) resource)
						: displayFolderBlock((Folder) resource);
				pagesLayout.addComponent(resourceBlock);
			}
		}

	}

	private Layout displayFolderBlock(final Folder resource) {
		HorizontalLayout container = new HorizontalLayout();
		container.setWidth("100%");
		container.setSpacing(true);
		container.setStyleName("page-item-block");
		Image iconResource = new Image(null,
				MyCollabResource.newResource("icons/48/project/folder.png"));
		container.addComponent(iconResource);
		container.setComponentAlignment(iconResource, Alignment.TOP_LEFT);

		VerticalLayout block = new VerticalLayout();
		block.setWidth("600px");
		HorizontalLayout headerPanel = new HorizontalLayout();
		Button folderLink = new Button(resource.getName(),
				new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(Button.ClickEvent event) {
						EventBusFactory.getInstance().post(
								new PageEvent.GotoList(PageListViewImpl.this,
										resource.getPath()));

					}
				});
		folderLink.addStyleName("link");
		folderLink.addStyleName("h3");
		headerPanel.addComponent(folderLink);
		block.addComponent(headerPanel);
		block.addComponent(new Label(StringUtils.trimHtmlTags(resource
				.getDescription())));

		HorizontalLayout controlBtns = new HorizontalLayout();
		controlBtns.setSpacing(true);
		controlBtns.setStyleName("control-btns");
		Button editBtn = new Button(
				AppContext.getMessage(GenericI18Enum.BUTTON_EDIT_LABEL),
				new Button.ClickListener() {

					private static final long serialVersionUID = -5387015552598157076L;

					@Override
					public void buttonClick(Button.ClickEvent event) {
						UI.getCurrent()
								.addWindow(new PageGroupWindow(resource));
					}
				});
		editBtn.setIcon(MyCollabResource
				.newResource("icons/12/project/edit.png"));
		editBtn.setStyleName("link");
		controlBtns.addComponent(editBtn);

		Button deleteBtn = new Button(
				AppContext.getMessage(GenericI18Enum.BUTTON_DELETE_LABEL),
				new Button.ClickListener() {

					private static final long serialVersionUID = -5387015552598157076L;

					@Override
					public void buttonClick(Button.ClickEvent event) {
						// TODO Delete this Folder

					}
				});
		deleteBtn.setIcon(MyCollabResource
				.newResource("icons/12/project/delete.png"));
		deleteBtn.setStyleName("link");
		controlBtns.addComponent(deleteBtn);

		block.addComponent(controlBtns);

		HorizontalLayout footer = new HorizontalLayout();
		block.addComponent(footer);

		container.addComponent(block);
		container.setExpandRatio(block, 1);
		return container;
	}

	private Layout displayPageBlock(final Page resource) {
		HorizontalLayout container = new HorizontalLayout();
		container.setWidth("100%");
		container.setSpacing(true);
		container.setStyleName("page-item-block");

		Image iconResource = new Image(null,
				MyCollabResource.newResource("icons/48/project/document.png"));
		container.addComponent(iconResource);
		container.setComponentAlignment(iconResource, Alignment.TOP_LEFT);

		VerticalLayout block = new VerticalLayout();
		block.setWidth("600px");
		HorizontalLayout headerPanel = new HorizontalLayout();
		Button pageLink = new Button(resource.getSubject(),
				new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(Button.ClickEvent event) {
						EventBusFactory.getInstance().post(
								new PageEvent.GotoRead(PageListViewImpl.this,
										resource));

					}
				});
		pageLink.addStyleName("link");
		pageLink.addStyleName("h3");
		headerPanel.addComponent(pageLink);

		block.addComponent(headerPanel);

		block.addComponent(new Label(StringUtils.trimHtmlTags(resource
				.getContent())));

		Label lastUpdateInfo = new Label(AppContext.getMessage(
				Page18InEnum.LABEL_LAST_UPDATE,
				ProjectLinkBuilder.generateProjectMemberHtmlLink(
						resource.getLastUpdatedUser(),
						CurrentProjectVariables.getProjectId()),
				AppContext.formatDate(resource.getLastUpdatedTime()),
				ContentMode.HTML));
		lastUpdateInfo.addStyleName("last-update-info");
		block.addComponent(lastUpdateInfo);

		HorizontalLayout controlBtns = new HorizontalLayout();
		controlBtns.setSpacing(true);
		controlBtns.setStyleName("control-btns");
		Button editBtn = new Button(
				AppContext.getMessage(GenericI18Enum.BUTTON_EDIT_LABEL),
				new Button.ClickListener() {

					private static final long serialVersionUID = -5387015552598157076L;

					@Override
					public void buttonClick(Button.ClickEvent event) {
						EventBusFactory.getInstance().post(
								new PageEvent.GotoEdit(PageListViewImpl.this,
										resource));
					}
				});
		editBtn.setIcon(MyCollabResource
				.newResource("icons/12/project/edit.png"));
		editBtn.setStyleName("link");
		controlBtns.addComponent(editBtn);

		Button deleteBtn = new Button(
				AppContext.getMessage(GenericI18Enum.BUTTON_DELETE_LABEL),
				new Button.ClickListener() {

					private static final long serialVersionUID = 2575434171770462361L;

					@Override
					public void buttonClick(Button.ClickEvent event) {
						// TODO Auto-generated method stub

					}
				});
		deleteBtn.setIcon(MyCollabResource
				.newResource("icons/12/project/delete.png"));
		deleteBtn.setStyleName("link");
		controlBtns.addComponent(deleteBtn);

		block.addComponent(controlBtns);

		container.addComponent(block);
		container.setExpandRatio(block, 1);
		return container;
	}

	private class PageGroupWindow extends Window {
		private static final long serialVersionUID = 1L;

		private Folder folder;

		private boolean editMode = false;

		public PageGroupWindow(Folder editFolder) {
			super();
			this.setModal(true);
			this.setWidth("700px");
			this.setResizable(false);
			this.center();
			VerticalLayout content = new VerticalLayout();
			content.setSpacing(true);
			content.setMargin(new MarginInfo(false, false, true, false));

			EditForm editForm = new EditForm();

			if (editFolder == null) {
				folder = new Folder();
				this.setCaption(AppContext
						.getMessage(Page18InEnum.DIALOG_NEW_GROUP_TITLE));
				editMode = false;
			} else {
				folder = editFolder;
				this.setCaption(AppContext
						.getMessage(Page18InEnum.DIALOG_EDIT_GROUP_TITLE));
				editMode = true;
			}
			String pagePath = CurrentProjectVariables.getCurrentPagePath();
			folder.setPath(pagePath + "/" + StringUtils.generateSoftUniqueId());

			editForm.setBean(folder);
			content.addComponent(editForm);

			this.setContent(content);
		}

		public PageGroupWindow() {
			this(null);
		}

		private class EditForm extends AdvancedEditBeanForm<Folder> {

			private static final long serialVersionUID = -1898444508905690238L;

			@Override
			public void setBean(final Folder item) {
				this.setFormLayoutFactory(new FormLayoutFactory());
				this.setBeanFormFieldFactory(new EditFormFieldFactory(
						EditForm.this));
				super.setBean(item);
			}

			class FormLayoutFactory implements IFormLayoutFactory {

				private static final long serialVersionUID = 1L;
				private GridFormLayoutHelper informationLayout;

				@Override
				public Layout getLayout() {
					final VerticalLayout layout = new VerticalLayout();
					this.informationLayout = new GridFormLayoutHelper(2, 2,
							"100%", "167px", Alignment.TOP_LEFT);
					this.informationLayout.getLayout().setWidth("100%");
					this.informationLayout.getLayout().setMargin(false);
					this.informationLayout.getLayout().addStyleName(
							"colored-gridlayout");

					layout.addComponent(this.informationLayout.getLayout());

					final HorizontalLayout controlsBtn = new HorizontalLayout();
					controlsBtn.setSpacing(true);
					controlsBtn.setMargin(new MarginInfo(true, true, true,
							false));
					layout.addComponent(controlsBtn);

					final Button cancelBtn = new Button(
							AppContext
									.getMessage(GenericI18Enum.BUTTON_CANCEL_LABEL),
							new Button.ClickListener() {
								private static final long serialVersionUID = 1L;

								@Override
								public void buttonClick(
										final Button.ClickEvent event) {
									PageGroupWindow.this.close();
								}
							});
					cancelBtn.setStyleName(UIConstants.THEME_GRAY_LINK);
					controlsBtn.addComponent(cancelBtn);
					controlsBtn.setComponentAlignment(cancelBtn,
							Alignment.MIDDLE_LEFT);

					final Button saveBtn = new Button(
							AppContext
									.getMessage(GenericI18Enum.BUTTON_SAVE_LABEL),
							new Button.ClickListener() {
								private static final long serialVersionUID = 1L;

								@Override
								public void buttonClick(
										final Button.ClickEvent event) {
									if (EditForm.this.validateForm()) {

										// FIXME: if editMode is true, update
										// folder instead of create new one

										WikiService wikiService = ApplicationContextUtil
												.getSpringBean(WikiService.class);
										wikiService.createFolder(folder,
												AppContext.getUsername());
										resources.add(folder);
										PageGroupWindow.this.close();
										displayPages(resources);
									}
								}
							});
					saveBtn.setStyleName(UIConstants.THEME_GREEN_LINK);
					controlsBtn.addComponent(saveBtn);
					controlsBtn.setComponentAlignment(saveBtn,
							Alignment.MIDDLE_RIGHT);

					layout.setComponentAlignment(controlsBtn,
							Alignment.MIDDLE_RIGHT);

					return layout;
				}

				@Override
				public void attachField(Object propertyId, Field<?> field) {
					if (propertyId.equals("name")) {
						this.informationLayout.addComponent(field,
								AppContext.getMessage(Page18InEnum.FORM_GROUP),
								0, 0);
					} else if (propertyId.equals("description")) {
						this.informationLayout.addComponent(field, AppContext
								.getMessage(GenericI18Enum.FORM_DESCRIPTION),
								0, 1, 2, "100%", Alignment.MIDDLE_LEFT);
					}

				}
			}
		}

		private class EditFormFieldFactory extends
				AbstractBeanFieldGroupEditFieldFactory<Folder> {
			private static final long serialVersionUID = 1L;

			public EditFormFieldFactory(GenericBeanForm<Folder> form) {
				super(form);
			}

			@Override
			protected Field<?> onCreateField(final Object propertyId) {
				if (propertyId.equals("description")) {
					RichTextArea descrArea = new RichTextArea();
					descrArea.setNullRepresentation("");
					return descrArea;
				}

				return null;
			}
		}
	}

}
