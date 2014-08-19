package com.esofthead.mycollab.module.project.view.page;

import com.esofthead.mycollab.common.CommentType;
import com.esofthead.mycollab.common.i18n.WikiI18nEnum;
import com.esofthead.mycollab.configuration.SiteConfiguration;
import com.esofthead.mycollab.html.DivLessFormatter;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectLinkBuilder;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.domain.SimpleProjectMember;
import com.esofthead.mycollab.module.project.i18n.Page18InEnum;
import com.esofthead.mycollab.module.project.i18n.ProjectCommonI18nEnum;
import com.esofthead.mycollab.module.project.service.ProjectMemberService;
import com.esofthead.mycollab.module.project.ui.components.AbstractPreviewItemComp2;
import com.esofthead.mycollab.module.project.ui.components.CommentDisplay;
import com.esofthead.mycollab.module.wiki.domain.Page;
import com.esofthead.mycollab.schedule.email.project.ProjectPageRelayEmailNotificationAction;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.HasPreviewFormHandlers;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.mvp.ViewScope;
import com.esofthead.mycollab.vaadin.ui.AbstractBeanFieldGroupViewFieldFactory;
import com.esofthead.mycollab.vaadin.ui.AdvancedPreviewBeanForm;
import com.esofthead.mycollab.vaadin.ui.DefaultFormViewFieldFactory.FormViewField;
import com.esofthead.mycollab.vaadin.ui.DefaultFormViewFieldFactory.I18nFormViewField;
import com.esofthead.mycollab.vaadin.ui.GenericBeanForm;
import com.esofthead.mycollab.vaadin.ui.IFormLayoutFactory;
import com.esofthead.mycollab.vaadin.ui.MyCollabResource;
import com.esofthead.mycollab.vaadin.ui.ProjectPreviewFormControlsGenerator;
import com.esofthead.mycollab.vaadin.ui.TabsheetLazyLoadComp;
import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Img;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Field;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.4.0
 *
 */
@ViewComponent(scope=ViewScope.PROTOTYPE)
public class PageReadViewImpl extends AbstractPreviewItemComp2<Page> implements
		PageReadView {
	private static final long serialVersionUID = 1L;

	private CommentDisplay commentListComp;

	private PageInfoComp pageInfoComp;

	public PageReadViewImpl() {
		super(AppContext.getMessage(Page18InEnum.VIEW_READ_TITLE),
				MyCollabResource
						.newResource("icons/22/project/page_selected.png"));
	}

	@Override
	public Page getItem() {
		return beanItem;
	}

	@Override
	protected void initRelatedComponents() {
		commentListComp = new CommentDisplay(CommentType.PRJ_PAGE,
				CurrentProjectVariables.getProjectId(), true, true,
				ProjectPageRelayEmailNotificationAction.class);
		commentListComp.setWidth("100%");
		commentListComp.setMargin(true);

		pageInfoComp = new PageInfoComp();
		addToSideBar(pageInfoComp);

	}

	@Override
	protected void onPreviewItem() {
		commentListComp.loadComments(beanItem.getId());
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
		return new PageReadFormLayout();
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

		tabContainer.addTab(this.commentListComp, AppContext
				.getMessage(ProjectCommonI18nEnum.TAB_COMMENT),
				MyCollabResource
						.newResource("icons/16/project/gray/comment.png"));

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

	private class PageReadFormLayout implements IFormLayoutFactory {
		private static final long serialVersionUID = 1L;

		private VerticalLayout layout;

		@Override
		public Layout getLayout() {
			layout = new VerticalLayout();
			layout.setStyleName("page-read-layout");
			layout.setMargin(true);
			layout.setSpacing(true);
			layout.setWidth("100%");
			return layout;
		}

		@Override
		public void attachField(Object propertyId, Field<?> field) {
			if (propertyId.equals("content")) {
				layout.addComponent(field);
			}
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

			String createdUser = beanItem.getCreatedUser();

			Label createdLbl = new Label(AppContext.getMessage(
					Page18InEnum.OPT_CREATED_USER, getMemberLink(createdUser)),
					ContentMode.HTML);
			createdLbl.setSizeUndefined();
			layout.addComponent(createdLbl);

			Label visibilityLbl = new Label(String.format(
					"%s: %s",
					AppContext.getMessage(Page18InEnum.FORM_VISIBILITY),
					AppContext.getMessage(WikiI18nEnum.class,
							beanItem.getStatus())));
			layout.addComponent(visibilityLbl);

			this.addComponent(layout);
		}

		private String getMemberLink(String createdUser) {
			if (createdUser != null && !createdUser.equals("")) {
				ProjectMemberService projectMemberService = ApplicationContextUtil
						.getSpringBean(ProjectMemberService.class);
				SimpleProjectMember member = projectMemberService
						.findMemberByUsername(createdUser,
								CurrentProjectVariables.getProjectId(),
								AppContext.getAccountId());
				if (member != null) {
					DivLessFormatter div = new DivLessFormatter();
					Img userAvatar = new Img("",
							SiteConfiguration.getAvatarLink(
									member.getMemberAvatarId(), 16));
					A userLink = new A();
					userLink.setHref(ProjectLinkBuilder
							.generateProjectMemberFullLink(
									CurrentProjectVariables.getProjectId(),
									createdUser));
					userLink.appendText(member.getMemberFullName());
					div.appendChild(userAvatar, userLink);
					return div.write();
				}
			}

			return createdUser;
		}
	}
}
