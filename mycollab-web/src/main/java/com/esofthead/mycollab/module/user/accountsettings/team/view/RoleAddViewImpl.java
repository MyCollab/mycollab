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

package com.esofthead.mycollab.module.user.accountsettings.team.view;

import java.util.HashMap;
import java.util.Map;

import com.esofthead.mycollab.module.user.accountsettings.localization.RoleI18nEnum;
import com.esofthead.mycollab.module.user.domain.Role;
import com.esofthead.mycollab.module.user.domain.SimpleRole;
import com.esofthead.mycollab.module.user.view.component.PermissionComboBoxFactory;
import com.esofthead.mycollab.security.PermissionDefItem;
import com.esofthead.mycollab.security.PermissionMap;
import com.esofthead.mycollab.security.RolePermissionCollections;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.HasEditFormHandlers;
import com.esofthead.mycollab.vaadin.mvp.AbstractPageView;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.AbstractBeanFieldGroupEditFieldFactory;
import com.esofthead.mycollab.vaadin.ui.AddViewLayout;
import com.esofthead.mycollab.vaadin.ui.AdvancedEditBeanForm;
import com.esofthead.mycollab.vaadin.ui.Depot;
import com.esofthead.mycollab.vaadin.ui.EditFormControlsGenerator;
import com.esofthead.mycollab.vaadin.ui.GenericBeanForm;
import com.esofthead.mycollab.vaadin.ui.GridFormLayoutHelper;
import com.esofthead.mycollab.vaadin.ui.KeyCaptionComboBox;
import com.esofthead.mycollab.vaadin.ui.MyCollabResource;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Field;
import com.vaadin.ui.HorizontalLayout;
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
public class RoleAddViewImpl extends AbstractPageView implements RoleAddView {

	private static final long serialVersionUID = 1L;

	private final EditForm editForm;
	private Role role;

	public RoleAddViewImpl() {
		super();

		this.setMargin(new MarginInfo(false, true, false, true));
		this.editForm = new EditForm();
		this.addComponent(this.editForm);
		this.addStyleName("accountsettings-role");
	}

	@Override
	public void editItem(final Role item) {
		this.role = item;
		this.editForm.setBean(this.role);
	}

	@Override
	public PermissionMap getPermissionMap() {
		return this.editForm.getPermissionMap();
	}

	public class EditForm extends AdvancedEditBeanForm<Role> {

		private static final long serialVersionUID = 1L;
		private final Map<String, KeyCaptionComboBox> permissionControlsMap = new HashMap<String, KeyCaptionComboBox>();

		@Override
		public void setBean(final Role item) {
			this.setFormLayoutFactory(new EditForm.FormLayoutFactory());
			this.setBeanFormFieldFactory(new EditFormFieldFactory(EditForm.this));
			super.setBean(item);
		}

		private class FormLayoutFactory extends RoleFormLayoutFactory {

			private static final long serialVersionUID = 1L;

			public FormLayoutFactory() {
				super("");
			}

			@Override
			public ComponentContainer getLayout() {
				final AddViewLayout formAddLayout = new AddViewLayout(
						initFormHeader(),
						MyCollabResource
								.newResource("icons/24/project/user.png"));

				final ComponentContainer topLayout = createButtonControls();
				if (topLayout != null) {
					formAddLayout.addHeaderRight(topLayout);
				}

				formAddLayout.setTitle(initFormTitle());

				userInformationLayout = new RoleInformationLayout();

				formAddLayout.addBody(userInformationLayout.getLayout());

				final ComponentContainer bottomPanel = createBottomPanel();
				if (bottomPanel != null) {
					formAddLayout.addBottomControls(bottomPanel);
				}

				return formAddLayout;
			}

			protected String initFormHeader() {
				return role.getId() == null ? AppContext
						.getMessage(RoleI18nEnum.VIEW_NEW_TITLE) : AppContext
						.getMessage(RoleI18nEnum.VIEW_EDIT_TITLE);
			}

			protected String initFormTitle() {
				return role.getId() == null ? null : role.getRolename();
			}

			private Layout createButtonControls() {
				final HorizontalLayout controlPanel = new HorizontalLayout();
				final Layout controlButtons = new EditFormControlsGenerator<Role>(
						RoleAddViewImpl.EditForm.this).createButtonControls();
				controlButtons.setSizeUndefined();
				controlPanel.addComponent(controlButtons);
				controlPanel.setComponentAlignment(controlButtons,
						Alignment.MIDDLE_CENTER);
				controlPanel.addStyleName("control-buttons");
				return controlPanel;
			}

			@Override
			protected Layout createBottomPanel() {
				final VerticalLayout permissionsPanel = new VerticalLayout();
				final Label organizationHeader = new Label(
						AppContext
								.getMessage(RoleI18nEnum.FORM_PERMISSION_HEADER));
				organizationHeader.setStyleName(UIConstants.H2_STYLE2);
				permissionsPanel.addComponent(organizationHeader);

				PermissionMap perMap;
				if (RoleAddViewImpl.this.role instanceof SimpleRole) {
					perMap = ((SimpleRole) RoleAddViewImpl.this.role)
							.getPermissionMap();
				} else {
					perMap = new PermissionMap();
				}

				final GridFormLayoutHelper crmFormHelper = new GridFormLayoutHelper(
						2,
						RolePermissionCollections.CRM_PERMISSIONS_ARR.length,
						"100%", "167px", Alignment.TOP_LEFT);
				crmFormHelper.getLayout().setMargin(false);
				crmFormHelper.getLayout().setWidth("100%");
				crmFormHelper.getLayout().addStyleName(
						UIConstants.COLORED_GRIDLAYOUT);

				for (int i = 0; i < RolePermissionCollections.CRM_PERMISSIONS_ARR.length; i++) {
					PermissionDefItem permissionDefItem = RolePermissionCollections.CRM_PERMISSIONS_ARR[i];
					KeyCaptionComboBox permissionBox = PermissionComboBoxFactory
							.createPermissionSelection(permissionDefItem
									.getPermissionCls());

					final Integer flag = perMap
							.getPermissionFlag(permissionDefItem.getKey());
					permissionBox.setValue(flag);
					EditForm.this.permissionControlsMap.put(
							permissionDefItem.getKey(), permissionBox);
					crmFormHelper.addComponent(permissionBox,
							permissionDefItem.getCaption(), 0, i);
				}

				permissionsPanel
						.addComponent(constructGridLayout(
								AppContext
										.getMessage(RoleI18nEnum.SECTION_PROJECT_MANAGEMENT_TITLE),
								perMap,
								RolePermissionCollections.PROJECT_PERMISSION_ARR));
				permissionsPanel.addComponent(constructGridLayout(
						AppContext.getMessage(RoleI18nEnum.SECTION_CRM_TITLE),
						perMap, RolePermissionCollections.CRM_PERMISSIONS_ARR));
				permissionsPanel.addComponent(constructGridLayout(AppContext
						.getMessage(RoleI18nEnum.SECTION_DOCUMENT_TITLE),
						perMap,
						RolePermissionCollections.DOCUMENT_PERMISSION_ARR));
				permissionsPanel
						.addComponent(constructGridLayout(
								AppContext
										.getMessage(RoleI18nEnum.SECTION_ACCOUNT_MANAGEMENT_TITLE),
								perMap,
								RolePermissionCollections.ACCOUNT_PERMISSION_ARR));

				return permissionsPanel;
			}
		}

		private Depot constructGridLayout(String depotTitle,
				PermissionMap perMap, PermissionDefItem[] defItems) {
			final GridFormLayoutHelper formHelper = new GridFormLayoutHelper(2,
					defItems.length, "100%", "167px", Alignment.TOP_LEFT);
			formHelper.getLayout().setMargin(false);
			formHelper.getLayout().setWidth("100%");
			formHelper.getLayout().addStyleName(UIConstants.COLORED_GRIDLAYOUT);
			final Depot component = new Depot(depotTitle,
					formHelper.getLayout());

			for (int i = 0; i < defItems.length; i++) {
				final PermissionDefItem permissionDefItem = defItems[i];
				KeyCaptionComboBox permissionBox = PermissionComboBoxFactory
						.createPermissionSelection(permissionDefItem
								.getPermissionCls());
				final Integer flag = perMap.getPermissionFlag(permissionDefItem
						.getKey());
				permissionBox.setValue(flag);
				EditForm.this.permissionControlsMap.put(
						permissionDefItem.getKey(), permissionBox);
				formHelper.addComponent(permissionBox,
						permissionDefItem.getCaption(), 0, i);

			}

			return component;
		}

		protected PermissionMap getPermissionMap() {
			final PermissionMap permissionMap = new PermissionMap();

			for (final String permissionItem : this.permissionControlsMap
					.keySet()) {
				final KeyCaptionComboBox permissionBox = this.permissionControlsMap
						.get(permissionItem);
				final Integer perValue = (Integer) permissionBox.getValue();
				permissionMap.addPath(permissionItem, perValue);
			}
			return permissionMap;
		}

		private class EditFormFieldFactory extends
				AbstractBeanFieldGroupEditFieldFactory<Role> {
			private static final long serialVersionUID = 1L;

			public EditFormFieldFactory(GenericBeanForm<Role> form) {
				super(form);
			}

			@Override
			protected Field<?> onCreateField(final Object propertyId) {
				if (propertyId.equals("description")) {
					final TextArea textArea = new TextArea();
					textArea.setNullRepresentation("");
					return textArea;
				} else if (propertyId.equals("rolename")) {
					final TextField tf = new TextField();
					tf.setNullRepresentation("");
					tf.setRequired(true);
					tf.setRequiredError("Please enter a role name");
					return tf;

				}
				return null;
			}
		}
	}

	@Override
	public HasEditFormHandlers<Role> getEditFormHandlers() {
		return this.editForm;
	}
}
