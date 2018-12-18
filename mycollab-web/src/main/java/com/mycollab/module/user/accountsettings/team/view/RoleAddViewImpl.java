/**
 * Copyright © MyCollab
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.user.accountsettings.team.view;

import com.mycollab.common.i18n.SecurityI18nEnum;
import com.mycollab.module.user.accountsettings.localization.RoleI18nEnum;
import com.mycollab.module.user.domain.Role;
import com.mycollab.module.user.domain.SimpleRole;
import com.mycollab.module.user.view.component.PermissionComboBoxFactory;
import com.mycollab.module.user.view.component.YesNoPermissionComboBox;
import com.mycollab.security.PermissionDefItem;
import com.mycollab.security.PermissionMap;
import com.mycollab.security.RolePermissionCollections;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.event.HasEditFormHandlers;
import com.mycollab.vaadin.mvp.AbstractVerticalPageView;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.ui.AbstractBeanFieldGroupEditFieldFactory;
import com.mycollab.vaadin.ui.AdvancedEditBeanForm;
import com.mycollab.vaadin.ui.FormContainer;
import com.mycollab.vaadin.ui.GenericBeanForm;
import com.mycollab.vaadin.web.ui.AddViewLayout;
import com.mycollab.vaadin.web.ui.KeyCaptionComboBox;
import com.mycollab.vaadin.web.ui.grid.GridFormLayoutHelper;
import com.vaadin.data.HasValue;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.mycollab.vaadin.web.ui.utils.FormControlsGenerator.generateEditFormControls;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@ViewComponent
public class RoleAddViewImpl extends AbstractVerticalPageView implements RoleAddView {
    private static final long serialVersionUID = 1L;

    private EditForm editForm;
    private Role role;

    public RoleAddViewImpl() {
        this.setMargin(new MarginInfo(false, true, true, true));
        this.editForm = new EditForm();
        this.addComponent(this.editForm);
    }

    @Override
    public void editItem(Role item) {
        this.role = item;
        this.editForm.setBean(this.role);
    }

    @Override
    public PermissionMap getPermissionMap() {
        return editForm.getPermissionMap();
    }

    public class EditForm extends AdvancedEditBeanForm<Role> {
        private static final long serialVersionUID = 1L;
        private final Map<String, KeyCaptionComboBox> permissionControlsMap = new HashMap<>();

        @Override
        public void setBean(Role item) {
            this.setFormLayoutFactory(new EditForm.FormLayoutFactory());
            this.setBeanFormFieldFactory(new EditFormFieldFactory(EditForm.this));
            super.setBean(item);
        }

        private class FormLayoutFactory extends RoleFormLayoutFactory {

            FormLayoutFactory() {
                super("");
            }

            @Override
            public AbstractComponent getLayout() {
                AddViewLayout formAddLayout = new AddViewLayout(initFormHeader(), VaadinIcons.USERS);

                ComponentContainer topLayout = createButtonControls();
                if (topLayout != null) {
                    formAddLayout.addHeaderRight(topLayout);
                }
                formAddLayout.setTitle(initFormTitle());

                wrappedLayoutFactory = new RoleInformationLayout();
                formAddLayout.addBody(wrappedLayoutFactory.getLayout());

                ComponentContainer bottomPanel = createBottomPanel();
                if (bottomPanel != null) {
                    formAddLayout.addBottom(bottomPanel);
                }

                return formAddLayout;
            }

            protected String initFormHeader() {
                return role.getId() == null ? UserUIContext.getMessage(RoleI18nEnum.NEW) : UserUIContext.getMessage(RoleI18nEnum.DETAIL);
            }

            protected String initFormTitle() {
                return role.getId() == null ? null : role.getRolename();
            }

            private ComponentContainer createButtonControls() {
                return generateEditFormControls(EditForm.this);
            }

            @Override
            protected Layout createBottomPanel() {
                final VerticalLayout permissionsPanel = new VerticalLayout();

                PermissionMap perMap;
                if (role instanceof SimpleRole) {
                    perMap = ((SimpleRole) role).getPermissionMap();
                } else {
                    perMap = new PermissionMap();
                }

                permissionsPanel.addComponent(constructGridLayout(UserUIContext.getMessage(RoleI18nEnum.SECTION_PROJECT_MANAGEMENT_TITLE),
                        perMap, RolePermissionCollections.PROJECT_PERMISSION_ARR));

                permissionsPanel.addComponent(constructGridLayout(UserUIContext.getMessage(RoleI18nEnum.SECTION_ACCOUNT_MANAGEMENT_TITLE),
                        perMap, RolePermissionCollections.ACCOUNT_PERMISSION_ARR));

                return permissionsPanel;
            }
        }

        private ComponentContainer constructGridLayout(String depotTitle, PermissionMap perMap, List<PermissionDefItem> defItems) {
            GridFormLayoutHelper formHelper = GridFormLayoutHelper.defaultFormLayoutHelper(2, defItems.size() / 2 + 1);
            FormContainer permissionsPanel = new FormContainer();
            permissionsPanel.addSection(depotTitle, formHelper.getLayout());

            for (int i = 0; i < defItems.size(); i++) {
                PermissionDefItem permissionDefItem = defItems.get(i);
                KeyCaptionComboBox permissionBox = PermissionComboBoxFactory.createPermissionSelection(permissionDefItem.getPermissionCls());
                Integer flag = perMap.getPermissionFlag(permissionDefItem.getKey());
                permissionBox.setValue(flag);
                Enum captionHelp;
                if (permissionBox instanceof YesNoPermissionComboBox) {
                    captionHelp = SecurityI18nEnum.BOOLEAN_PERMISSION_HELP;
                } else {
                    captionHelp = SecurityI18nEnum.ACCESS_PERMISSION_HELP;
                }
                permissionControlsMap.put(permissionDefItem.getKey(), permissionBox);
                formHelper.addComponent(permissionBox, UserUIContext.getMessage(permissionDefItem.getCaption()),
                        UserUIContext.getMessage(captionHelp), i % 2, i / 2);
            }

            return permissionsPanel;
        }

        protected PermissionMap getPermissionMap() {
            PermissionMap permissionMap = new PermissionMap();

            for (Map.Entry<String, KeyCaptionComboBox> entry : permissionControlsMap.entrySet()) {
                KeyCaptionComboBox permissionBox = entry.getValue();
                Integer perValue = (Integer) permissionBox.getValue();
                permissionMap.addPath(entry.getKey(), perValue);
            }
            return permissionMap;
        }

        // TODO
        private class EditFormFieldFactory extends AbstractBeanFieldGroupEditFieldFactory<Role> {
            private static final long serialVersionUID = 1L;

            public EditFormFieldFactory(GenericBeanForm<Role> form) {
                super(form);
            }

            @Override
            protected HasValue<?> onCreateField(final Object propertyId) {
                if (propertyId.equals("description")) {
                    return new RichTextArea();
                } else if (propertyId.equals("rolename")) {
//                    return new MTextField().withRequired(true)
//                            .withRequiredError(UserUIContext.getMessage(ErrorI18nEnum.FIELD_MUST_NOT_NULL,
//                                    UserUIContext.getMessage(GenericI18Enum.FORM_NAME)));

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
