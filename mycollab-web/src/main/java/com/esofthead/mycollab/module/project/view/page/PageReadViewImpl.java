package com.esofthead.mycollab.module.project.view.page;

import com.esofthead.mycollab.common.CommentType;
import com.esofthead.mycollab.common.i18n.WikiI18nEnum;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.i18n.ProjectCommonI18nEnum;
import com.esofthead.mycollab.module.project.i18n.RiskI18nEnum;
import com.esofthead.mycollab.module.project.ui.components.AbstractPreviewItemComp2;
import com.esofthead.mycollab.module.project.ui.components.CommentDisplay;
import com.esofthead.mycollab.module.wiki.domain.Page;
import com.esofthead.mycollab.schedule.email.project.ProjectPageRelayEmailNotificationAction;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.HasPreviewFormHandlers;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.AbstractBeanFieldGroupViewFieldFactory;
import com.esofthead.mycollab.vaadin.ui.AdvancedPreviewBeanForm;
import com.esofthead.mycollab.vaadin.ui.DefaultFormViewFieldFactory.FormViewField;
import com.esofthead.mycollab.vaadin.ui.DefaultFormViewFieldFactory.I18nFormViewField;
import com.esofthead.mycollab.vaadin.ui.GenericBeanForm;
import com.esofthead.mycollab.vaadin.ui.IFormLayoutFactory;
import com.esofthead.mycollab.vaadin.ui.MyCollabResource;
import com.esofthead.mycollab.vaadin.ui.ProjectPreviewFormControlsGenerator;
import com.esofthead.mycollab.vaadin.ui.TabsheetLazyLoadComp;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Field;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.4.0
 *
 */
@ViewComponent
public class PageReadViewImpl extends AbstractPreviewItemComp2<Page> implements
		PageReadView {
	private static final long serialVersionUID = 1L;

	private CommentDisplay commentDisplay;

	private PageInfoComp pageInfoComp;

	public PageReadViewImpl() {
		super(AppContext.getMessage(RiskI18nEnum.FORM_READ_TITLE),
				MyCollabResource
						.newResource("icons/22/project/page_selected.png"));
	}

	@Override
	public Page getItem() {
		return beanItem;
	}

	@Override
	protected void initRelatedComponents() {
		commentDisplay = new CommentDisplay(CommentType.PRJ_PAGE,
				CurrentProjectVariables.getProjectId(), true, true,
				ProjectPageRelayEmailNotificationAction.class);
		commentDisplay.setWidth("100%");
		commentDisplay.setMargin(true);

		pageInfoComp = new PageInfoComp();
		addToSideBar(pageInfoComp);

	}

	@Override
	protected void onPreviewItem() {
		pageInfoComp.displayEntryInfo();
	}

	@Override
	protected String initFormTitle() {
		return beanItem.getSubject();
	}

	@Override
	protected AdvancedPreviewBeanForm<Page> initPreviewForm() {
		return new AdvancedPreviewBeanForm<Page>();
	}

	@Override
	protected IFormLayoutFactory initFormLayoutFactory() {
		return new PageFormLayoutFactory();
	}

	@Override
	public HasPreviewFormHandlers<Page> getPreviewFormHandlers() {
		return previewForm;
	}

	@Override
	protected AbstractBeanFieldGroupViewFieldFactory<Page> initBeanFormFieldFactory() {
		return new PageReadFormFieldFactory(previewForm);
	}

	@Override
	protected ComponentContainer createButtonControls() {
		return new ProjectPreviewFormControlsGenerator<Page>(previewForm)
				.createButtonControls(
						ProjectPreviewFormControlsGenerator.ADD_BTN_PRESENTED
								| ProjectPreviewFormControlsGenerator.EDIT_BTN_PRESENTED
								| ProjectPreviewFormControlsGenerator.DELETE_BTN_PRESENTED,
						ProjectRolePermissionCollections.PAGES);
	}

	@Override
	protected ComponentContainer createBottomPanel() {
		final TabsheetLazyLoadComp tabContainer = new TabsheetLazyLoadComp();
		tabContainer.setWidth("100%");

		return tabContainer;
	}

	private static class PageReadFormFieldFactory extends
			AbstractBeanFieldGroupViewFieldFactory<Page> {
		private static final long serialVersionUID = 1L;

		public PageReadFormFieldFactory(GenericBeanForm<Page> form) {
			super(form);
		}

		@Override
		protected Field<?> onCreateField(Object propertyId) {

			if (propertyId.equals("status")) {
				return new I18nFormViewField(attachForm.getBean().getStatus(),
						WikiI18nEnum.class);
			} else if (propertyId.equals("content")) {
				return new FormViewField(attachForm.getBean().getContent(),
						ContentMode.HTML);
			}
			return null;
		}
	}

	private class PageInfoComp extends VerticalLayout {
		private static final long serialVersionUID = 1L;

		public void displayEntryInfo() {
			this.removeAllComponents();
			this.setSpacing(true);
			this.setMargin(new MarginInfo(false, false, false, true));

			Label pageInfoHeader = new Label("Page Information");
			pageInfoHeader.setStyleName("info-hdr");
			this.addComponent(pageInfoHeader);

			VerticalLayout layout = new VerticalLayout();
			layout.setWidth("100%");
			layout.setSpacing(true);
			layout.setMargin(new MarginInfo(false, false, false, true));
			String createdDate = AppContext.formatDate(beanItem
					.getCreatedTime().getTime());
			layout.addComponent(new Label(AppContext.getMessage(
					ProjectCommonI18nEnum.ITEM_CREATED_DATE, createdDate)));

			Label createdLbl = new Label(
					AppContext
							.getMessage(ProjectCommonI18nEnum.ITEM_CREATED_PEOPLE));
			createdLbl.setSizeUndefined();
			layout.addComponent(createdLbl);

			this.addComponent(layout);
		}
	}
}
