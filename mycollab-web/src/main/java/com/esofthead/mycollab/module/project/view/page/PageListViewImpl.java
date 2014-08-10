package com.esofthead.mycollab.module.project.view.page;

import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.events.PageEvent;
import com.esofthead.mycollab.module.project.i18n.Page18InEnum;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.AbstractPageView;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.MyCollabResource;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.esofthead.mycollab.vaadin.ui.UiUtils;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

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

		final Button createBtn = new Button(
				AppContext.getMessage(Page18InEnum.BUTTON_NEW_PAGE),
				new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(final ClickEvent event) {
						EventBusFactory.getInstance().post(
								new PageEvent.GotoAdd(this, null));
					}
				});
		createBtn.setStyleName(UIConstants.THEME_GREEN_LINK);
		createBtn.setIcon(MyCollabResource
				.newResource("icons/16/addRecord.png"));
		createBtn.setEnabled(CurrentProjectVariables
				.canWrite(ProjectRolePermissionCollections.PAGES));
		
		headerText.setStyleName(UIConstants.HEADER_TEXT);

		UiUtils.addComponent(headerLayout, titleIcon, Alignment.MIDDLE_LEFT);
		UiUtils.addComponent(headerLayout, headerText, Alignment.MIDDLE_LEFT);
		UiUtils.addComponent(headerLayout, createBtn, Alignment.MIDDLE_RIGHT);
		headerLayout.setExpandRatio(headerText, 1.0f);

		headerLayout.setStyleName(UIConstants.HEADER_VIEW);
		headerLayout.setWidth("100%");
		headerLayout.setSpacing(true);
		headerLayout.setMargin(new MarginInfo(true, false, true, false));
	}

	@Override
	public void displayPages() {
		// TODO Auto-generated method stub

	}

}
