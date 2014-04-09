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
package com.esofthead.mycollab.module.project.view.settings;

import com.esofthead.mycollab.common.ModuleNameConstants;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.domain.SimpleProjectRole;
import com.esofthead.mycollab.module.project.ui.components.AbstractPreviewItemComp;
import com.esofthead.mycollab.security.AccessPermissionFlag;
import com.esofthead.mycollab.security.PermissionMap;
import com.esofthead.mycollab.vaadin.ui.AbstractBeanFieldGroupViewFieldFactory;
import com.esofthead.mycollab.vaadin.ui.AdvancedPreviewBeanForm;
import com.esofthead.mycollab.vaadin.ui.GridFormLayoutHelper;
import com.esofthead.mycollab.vaadin.ui.IFormLayoutFactory;
import com.esofthead.mycollab.vaadin.ui.MyCollabResource;
import com.esofthead.mycollab.vaadin.ui.ProjectPreviewFormControlsGenerator;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Field;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 3.0
 * 
 */
class ProjectRoleReadComp extends AbstractPreviewItemComp<SimpleProjectRole> {
	private static final long serialVersionUID = 1L;

	private VerticalLayout permissionsPanel;
	private GridFormLayoutHelper projectFormHelper;

	public ProjectRoleReadComp() {
		super("Role Detail",MyCollabResource.newResource("icons/22/user/group.png"));
	}

	@Override
	protected AdvancedPreviewBeanForm<SimpleProjectRole> initPreviewForm() {
		return new AdvancedPreviewBeanForm<SimpleProjectRole>() {
			private static final long serialVersionUID = 1L;

			@Override
			public void showHistory() {
				final ProjectRoleHistoryLogWindow historyLog = new ProjectRoleHistoryLogWindow(
						ModuleNameConstants.PRJ,
						ProjectTypeConstants.PROJECT_ROLE);
				historyLog.loadHistory(previewForm.getBean().getId());
				UI.getCurrent().addWindow(historyLog);
			}
		};
	}

	@Override
	protected ComponentContainer createButtonControls() {
		return (new ProjectPreviewFormControlsGenerator<SimpleProjectRole>(
				previewForm))
				.createButtonControls(ProjectRolePermissionCollections.ROLES);
	}

	@Override
	protected ComponentContainer createBottomPanel() {
		permissionsPanel = new VerticalLayout();
		final Label organizationHeader = new Label("Permissions");
		organizationHeader.setStyleName("h2");
		permissionsPanel.addComponent(organizationHeader);

		projectFormHelper = new GridFormLayoutHelper(2,
				ProjectRolePermissionCollections.PROJECT_PERMISSIONS.length,
				"100%", "167px", Alignment.MIDDLE_LEFT);
		projectFormHelper.getLayout().setWidth("100%");
		projectFormHelper.getLayout().setMargin(false);
		projectFormHelper.getLayout().addStyleName("colored-gridlayout");

		permissionsPanel.addComponent(projectFormHelper.getLayout());

		return permissionsPanel;
	}

	@Override
	protected void initRelatedComponents() {

	}

	@Override
	protected void onPreviewItem() {
		projectFormHelper.getLayout().removeAllComponents();

		final PermissionMap permissionMap = beanItem.getPermissionMap();
		for (int i = 0; i < ProjectRolePermissionCollections.PROJECT_PERMISSIONS.length; i++) {
			final String permissionPath = ProjectRolePermissionCollections.PROJECT_PERMISSIONS[i];
			projectFormHelper.addComponent(
					new Label(this.getValueFromPerPath(permissionMap,
							permissionPath)), permissionPath, 0, i);
		}

	}

	@Override
	protected String initFormTitle() {
		return beanItem.getRolename();
	}

	@Override
	protected IFormLayoutFactory initFormLayoutFactory() {
		return new ProjectRoleFormLayoutFactory();
	}

	@Override
	protected AbstractBeanFieldGroupViewFieldFactory<SimpleProjectRole> initBeanFormFieldFactory() {
		return new AbstractBeanFieldGroupViewFieldFactory<SimpleProjectRole>(
				previewForm) {
			private static final long serialVersionUID = 1L;

			@Override
			protected Field<?> onCreateField(Object propertyId) {
				return null;
			}
		};
	}

	private String getValueFromPerPath(final PermissionMap permissionMap,
			final String permissionItem) {
		final Integer perVal = permissionMap.get(permissionItem);
		if (perVal == null) {
			return "No Access";
		} else {
			return AccessPermissionFlag.toString(perVal);
		}
	}
}
