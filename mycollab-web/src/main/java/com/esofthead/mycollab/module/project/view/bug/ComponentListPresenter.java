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

package com.esofthead.mycollab.module.project.view.bug;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.esofthead.mycollab.core.persistence.service.ISearchableService;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.i18n.ComponentI18nEnum;
import com.esofthead.mycollab.module.project.view.ProjectBreadcrumb;
import com.esofthead.mycollab.module.project.view.ProjectGenericListPresenter;
import com.esofthead.mycollab.module.tracker.domain.SimpleComponent;
import com.esofthead.mycollab.module.tracker.domain.criteria.ComponentSearchCriteria;
import com.esofthead.mycollab.module.tracker.service.ComponentService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.desktop.ui.DefaultMassEditActionHandler;
import com.esofthead.mycollab.vaadin.events.MassItemActionHandler;
import com.esofthead.mycollab.vaadin.mvp.LoadPolicy;
import com.esofthead.mycollab.vaadin.mvp.ScreenData;
import com.esofthead.mycollab.vaadin.mvp.ViewManager;
import com.esofthead.mycollab.vaadin.mvp.ViewScope;
import com.esofthead.mycollab.vaadin.ui.MailFormWindow;
import com.esofthead.mycollab.vaadin.ui.NotificationUtil;
import com.vaadin.ui.UI;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 */
@LoadPolicy(scope = ViewScope.PROTOTYPE)
public class ComponentListPresenter
		extends
		ProjectGenericListPresenter<ComponentListView, ComponentSearchCriteria, SimpleComponent> {

	private static final long serialVersionUID = 1L;
	private final ComponentService componentService;

	public ComponentListPresenter() {
		super(ComponentListView.class, ComponentListNoItemView.class);

		componentService = ApplicationContextUtil
				.getSpringBean(ComponentService.class);
	}

	@Override
	protected void postInitView() {
		super.postInitView();

		view.getPopupActionHandlers().setMassActionHandler(
				new DefaultMassEditActionHandler(this) {

					@Override
					protected void onSelectExtra(String id) {
						if (MassItemActionHandler.MAIL_ACTION.equals(id)) {
							UI.getCurrent().addWindow(new MailFormWindow());
						}

					}

					@Override
					protected String getReportTitle() {
						return AppContext
								.getMessage(ComponentI18nEnum.VIEW_LIST_TITLE);
					}

					@Override
					protected Class<?> getReportModelClassType() {
						return SimpleComponent.class;
					}
				});
	}

	@Override
	protected void onGo(com.vaadin.ui.ComponentContainer container,
			ScreenData<?> data) {
		if (CurrentProjectVariables
				.canRead(ProjectRolePermissionCollections.COMPONENTS)) {
			ComponentContainer trackerContainer = (ComponentContainer) container;
			trackerContainer.removeAllComponents();
			trackerContainer.addComponent(view.getWidget());

			searchCriteria = (ComponentSearchCriteria) data.getParams();

			int totalCount = componentService.getTotalCount(searchCriteria);

			if (totalCount > 0) {
				displayListView(container, data);
				doSearch(searchCriteria);
			} else {
				displayNoExistItems(container, data);
			}

			ProjectBreadcrumb breadcrumb = ViewManager
					.getCacheComponent(ProjectBreadcrumb.class);
			breadcrumb.gotoComponentList();
		} else {
			NotificationUtil.showMessagePermissionAlert();
		}
	}

	@Override
	protected void deleteSelectedItems() {
		if (!isSelectAll) {
			Collection<SimpleComponent> currentDataList = view
					.getPagedBeanTable().getCurrentDataList();
			List<Integer> keyList = new ArrayList<Integer>();
			for (SimpleComponent item : currentDataList) {
				if (item.isSelected()) {
					keyList.add(item.getId());
				}
			}

			if (keyList.size() > 0) {
				componentService.massRemoveWithSession(keyList,
						AppContext.getUsername(), AppContext.getAccountId());
			}
		} else {
			componentService.removeByCriteria(searchCriteria,
					AppContext.getAccountId());
		}

		int totalCount = componentService.getTotalCount(searchCriteria);

		if (totalCount > 0) {
			displayListView((ComponentContainer) view.getParent(), null);
			doSearch(searchCriteria);
		} else {
			displayNoExistItems((ComponentContainer) view.getParent(), null);
		}

	}

	@Override
	public ISearchableService<ComponentSearchCriteria> getSearchService() {
		return componentService;
	}
}
