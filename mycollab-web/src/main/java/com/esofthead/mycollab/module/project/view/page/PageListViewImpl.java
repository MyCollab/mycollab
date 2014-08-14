package com.esofthead.mycollab.module.project.view.page;

import java.util.List;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.events.PageEvent;
import com.esofthead.mycollab.module.project.i18n.Page18InEnum;
import com.esofthead.mycollab.module.wiki.domain.Folder;
import com.esofthead.mycollab.module.wiki.domain.Page;
import com.esofthead.mycollab.module.wiki.domain.WikiResource;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.AbstractPageView;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.AbstractBeanFieldGroupEditFieldFactory;
import com.esofthead.mycollab.vaadin.ui.AdvancedEditBeanForm;
import com.esofthead.mycollab.vaadin.ui.GenericBeanForm;
import com.esofthead.mycollab.vaadin.ui.GridFormLayoutHelper;
import com.esofthead.mycollab.vaadin.ui.IFormLayoutFactory;
import com.esofthead.mycollab.vaadin.ui.MyCollabResource;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.esofthead.mycollab.vaadin.ui.UiUtils;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Field;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.TextField;
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

		final Button newGroupBtn = new Button(
				AppContext.getMessage(Page18InEnum.BUTTON_NEW_GROUP),
				new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(final ClickEvent event) {
						UI.getCurrent().addWindow(new NewGroupWindow());
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
					public void buttonClick(final ClickEvent event) {
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
		headerLayout.setSpacing(true);
		headerLayout.setMargin(new MarginInfo(true, false, true, false));
	}

	@Override
	public void displayPages(List<WikiResource> resources) {
		this.resources = resources;
		pagesLayout.removeAllComponents();
		if (resources != null) {
			for (WikiResource resource : resources) {
				VerticalLayout resourceBlock = (resource instanceof Page) ? displayPageBlock((Page) resource)
						: displayFolderBlock((Folder) resource);
				pagesLayout.addComponent(resourceBlock);
			}
		}

	}

	private VerticalLayout displayFolderBlock(Folder resource) {
		VerticalLayout block = new VerticalLayout();
		return block;
	}

	private VerticalLayout displayPageBlock(final Page resource) {
		VerticalLayout block = new VerticalLayout();
		HorizontalLayout headerPanel = new HorizontalLayout();
		Button pageLink = new Button(resource.getSubject(),
				new ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(ClickEvent event) {
						EventBusFactory.getInstance().post(
								new PageEvent.GotoRead(PageListViewImpl.this,
										resource));

					}
				});
		headerPanel.addComponent(pageLink);

		block.addComponent(headerPanel);
		return block;
	}

	private class NewGroupWindow extends Window {
		private static final long serialVersionUID = 1L;

		private Folder folder;

		public NewGroupWindow() {
			super(AppContext.getMessage(Page18InEnum.DIALOG_NEW_GROUP_TITLE));
			this.setModal(true);
			this.setWidth("600px");
			this.setResizable(false);
			this.center();
			VerticalLayout content = new VerticalLayout();
			content.setSpacing(true);
			content.setMargin(new MarginInfo(false, false, true, false));

			EditForm editForm = new EditForm();

			folder = new Folder();
			editForm.setBean(folder);
			content.addComponent(editForm);

			this.setContent(content);
		}

		private class EditForm extends AdvancedEditBeanForm<Folder> {

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
									NewGroupWindow.this.close();
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

										NewGroupWindow.this.close();
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
				return null;
			}
		}
	}

}
