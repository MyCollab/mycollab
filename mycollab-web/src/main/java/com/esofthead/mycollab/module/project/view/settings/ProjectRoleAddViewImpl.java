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

import java.util.HashMap;
import java.util.Map;

import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.domain.ProjectRole;
import com.esofthead.mycollab.module.project.domain.SimpleProjectRole;
import com.esofthead.mycollab.module.project.ui.components.AbstractEditItemComp;
import com.esofthead.mycollab.module.user.view.component.AccessPermissionComboBox;
import com.esofthead.mycollab.security.PermissionMap;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.AbstractBeanFieldGroupEditFieldFactory;
import com.esofthead.mycollab.vaadin.ui.AdvancedEditBeanForm;
import com.esofthead.mycollab.vaadin.ui.EditFormControlsGenerator;
import com.esofthead.mycollab.vaadin.ui.GridFormLayoutHelper;
import com.esofthead.mycollab.vaadin.ui.IFormLayoutFactory;
import com.esofthead.mycollab.vaadin.ui.MyCollabResource;
import com.vaadin.server.Resource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Field;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 */
@ViewComponent
public class ProjectRoleAddViewImpl extends AbstractEditItemComp<ProjectRole>
		implements ProjectRoleAddView {

	private static final long serialVersionUID = 1L;
	private final Map<String, AccessPermissionComboBox> permissionControlsMap = new HashMap<String, AccessPermissionComboBox>();

	@Override
	protected String initFormHeader() {
		return beanItem.getId() == null ? "Create Role" : "Role Edit";
	}

	@Override
	protected String initFormTitle() {
		return beanItem.getId() == null ? null : beanItem.getRolename();
	}

	@Override
	protected Resource initFormIconResource() {
		return MyCollabResource.newResource("icons/24/project/user.png");
	}

	@Override
	protected ComponentContainer createButtonControls() {
		final Layout controlButtons = new EditFormControlsGenerator<ProjectRole>(
				editForm).createButtonControls();
		return controlButtons;
	}

	@Override
	protected AdvancedEditBeanForm<ProjectRole> initPreviewForm() {
		return new AdvancedEditBeanForm<ProjectRole>();
	}

	@Override
	protected IFormLayoutFactory initFormLayoutFactory() {
		return new ProjectRoleFormLayoutFactory();
	}

	@Override
	protected AbstractBeanFieldGroupEditFieldFactory<ProjectRole> initBeanFormFieldFactory() {
		return new AbstractBeanFieldGroupEditFieldFactory<ProjectRole>(editForm) {
			private static final long serialVersionUID = 1L;

			@Override
			protected Field<?> onCreateField(Object propertyId) {
				if (propertyId.equals("description")) {
					final TextArea textArea = new TextArea();
					textArea.setNullRepresentation("");
					return textArea;
				} else if (propertyId.equals("isadmin")) {

				} else if (propertyId.equals("rolename")) {
					final TextField tf = new TextField();
					if (isValidateForm) {
						tf.setNullRepresentation("");
						tf.setRequired(true);
						tf.setRequiredError("Please enter a projectRole name");
					}
					return tf;
				}
				return null;
			}
		};
	}

	@Override
	protected ComponentContainer createBottomPanel() {
		final VerticalLayout permissionsPanel = new VerticalLayout();
		final Label organizationHeader = new Label("Permissions");
		organizationHeader.setStyleName("h2");
		permissionsPanel.addComponent(organizationHeader);

		PermissionMap perMap;
		if (beanItem instanceof SimpleProjectRole) {
			perMap = ((SimpleProjectRole) beanItem).getPermissionMap();
		} else {
			perMap = new PermissionMap();
		}

		final GridFormLayoutHelper permissionFormHelper = new GridFormLayoutHelper(
				2, ProjectRolePermissionCollections.PROJECT_PERMISSIONS.length,
				"100%", "167px", Alignment.TOP_LEFT);

		for (int i = 0; i < ProjectRolePermissionCollections.PROJECT_PERMISSIONS.length; i++) {
			final String permissionPath = ProjectRolePermissionCollections.PROJECT_PERMISSIONS[i];
			final AccessPermissionComboBox permissionBox = new AccessPermissionComboBox();

			final Integer flag = perMap.getPermissionFlag(permissionPath);
			permissionBox.setValue(flag);
			permissionControlsMap.put(permissionPath, permissionBox);
			permissionFormHelper.addComponent(permissionBox, permissionPath, 0,
					i);
		}

		permissionFormHelper.getLayout().setWidth("100%");
		permissionFormHelper.getLayout().setMargin(false);
		permissionFormHelper.getLayout().addStyleName("colored-gridlayout");
		permissionsPanel.addComponent(permissionFormHelper.getLayout());

		return permissionsPanel;
	}

	@Override
	public PermissionMap getPermissionMap() {
		final PermissionMap permissionMap = new PermissionMap();

		for (final String permissionItem : this.permissionControlsMap.keySet()) {
			final AccessPermissionComboBox permissionBox = this.permissionControlsMap
					.get(permissionItem);
			final Integer perValue = (Integer) permissionBox.getValue();
			permissionMap.addPath(permissionItem, perValue);
		}
		return permissionMap;
	}

}
