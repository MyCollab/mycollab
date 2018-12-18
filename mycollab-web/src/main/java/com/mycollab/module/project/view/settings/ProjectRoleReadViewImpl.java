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
package com.mycollab.module.project.view.settings;

import com.mycollab.common.i18n.SecurityI18nEnum;
import com.mycollab.module.project.ProjectRolePermissionCollections;
import com.mycollab.module.project.domain.SimpleProjectRole;
import com.mycollab.module.project.i18n.ProjectRoleI18nEnum;
import com.mycollab.module.project.i18n.RolePermissionI18nEnum;
import com.mycollab.module.project.ui.components.ProjectPreviewFormControlsGenerator;
import com.mycollab.security.PermissionFlag;
import com.mycollab.security.PermissionMap;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.event.HasPreviewFormHandlers;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.ui.AbstractBeanFieldGroupViewFieldFactory;
import com.mycollab.vaadin.ui.FormContainer;
import com.mycollab.vaadin.ui.HeaderWithIcon;
import com.mycollab.vaadin.ui.IFormLayoutFactory;
import com.mycollab.vaadin.web.ui.AdvancedPreviewBeanForm;
import com.mycollab.vaadin.web.ui.DefaultReadViewLayout;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.mycollab.vaadin.web.ui.grid.GridFormLayoutHelper;
import com.vaadin.data.HasValue;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@ViewComponent
public class ProjectRoleReadViewImpl extends MVerticalLayout implements ProjectRoleReadView {
    private static final long serialVersionUID = 1L;

    private SimpleProjectRole beanItem;
    private AdvancedPreviewBeanForm<SimpleProjectRole> previewForm;
    private DefaultReadViewLayout previewLayout;
    private HeaderWithIcon headerText;
    private MHorizontalLayout header;

    private GridFormLayoutHelper projectFormHelper;

    public ProjectRoleReadViewImpl() {
        withMargin(new MarginInfo(true, false, true, false));
        headerText = HeaderWithIcon.h2(VaadinIcons.USERS, UserUIContext.getMessage(ProjectRoleI18nEnum.DETAIL));
        headerText.setSizeUndefined();
        this.addComponent(constructHeader());

        previewForm = initPreviewForm();
        ComponentContainer actionControls = createButtonControls();

        addHeaderRightContent(actionControls);

        CssLayout contentWrapper = new CssLayout();
        contentWrapper.setWidth("100%");
        contentWrapper.setStyleName(WebThemes.CONTENT_WRAPPER);

        previewLayout = new DefaultReadViewLayout("");
        contentWrapper.addComponent(previewLayout);
        previewLayout.addBody(previewForm);
        this.addComponent(contentWrapper);
    }

    protected AdvancedPreviewBeanForm<SimpleProjectRole> initPreviewForm() {
        return new AdvancedPreviewBeanForm<>();
    }

    protected ComponentContainer createButtonControls() {
        return (new ProjectPreviewFormControlsGenerator<>(previewForm)).createButtonControls(ProjectRolePermissionCollections.ROLES);
    }

    protected ComponentContainer createBottomPanel() {
        FormContainer permissionsPanel = new FormContainer();

        projectFormHelper = GridFormLayoutHelper.defaultFormLayoutHelper(2, (ProjectRolePermissionCollections.PROJECT_PERMISSIONS.length + 1) / 2, "180px");
        permissionsPanel.addSection(UserUIContext.getMessage(ProjectRoleI18nEnum.SECTION_PERMISSIONS), projectFormHelper.getLayout());

        return permissionsPanel;
    }

    protected void onPreviewItem() {
        projectFormHelper.getLayout().removeAllComponents();

        PermissionMap permissionMap = beanItem.getPermissionMap();
        for (int i = 0; i < ProjectRolePermissionCollections.PROJECT_PERMISSIONS.length; i++) {
            String permissionPath = ProjectRolePermissionCollections.PROJECT_PERMISSIONS[i];
            Enum permissionKey = RolePermissionI18nEnum.valueOf(permissionPath);
            Integer perVal = permissionMap.get(permissionKey.name());
            SecurityI18nEnum permissionVal = PermissionFlag.toVal(perVal);
            projectFormHelper.addComponent(new Label(UserUIContext.getMessage(permissionVal)),
                    UserUIContext.getMessage(permissionKey), UserUIContext.getMessage(permissionVal.desc()), i % 2, i / 2);
        }

    }

    protected String initFormTitle() {
        return beanItem.getRolename();
    }

    protected IFormLayoutFactory initFormLayoutFactory() {
        return new ProjectRoleFormLayoutFactory();
    }

    protected AbstractBeanFieldGroupViewFieldFactory<SimpleProjectRole> initBeanFormFieldFactory() {
        return new AbstractBeanFieldGroupViewFieldFactory<SimpleProjectRole>(previewForm) {
            private static final long serialVersionUID = 1L;

            @Override
            protected HasValue<?> onCreateField(Object propertyId) {
                return null;
            }
        };
    }

    @Override
    public SimpleProjectRole getItem() {
        return beanItem;
    }

    @Override
    public HasPreviewFormHandlers<SimpleProjectRole> getPreviewFormHandlers() {
        return previewForm;
    }

    private void initLayout() {
        ComponentContainer bottomPanel = createBottomPanel();
        if (bottomPanel != null) {
            previewLayout.addBottomControls(bottomPanel);
        }
    }

    private ComponentContainer constructHeader() {
        header = new MHorizontalLayout().withStyleName("hdr-view").withFullWidth().withMargin(true);
        header.with(headerText).alignAll(Alignment.MIDDLE_LEFT).expand(headerText);
        return header;
    }

    public void addHeaderRightContent(Component c) {
        header.addComponent(c);
    }

    public void previewItem(final SimpleProjectRole item) {
        beanItem = item;
        initLayout();
        previewLayout.setTitle(initFormTitle());

        previewForm.setFormLayoutFactory(initFormLayoutFactory());
        previewForm.setBeanFormFieldFactory(initBeanFormFieldFactory());
        previewForm.setBean(item);

        onPreviewItem();
    }

    public SimpleProjectRole getBeanItem() {
        return beanItem;
    }

    public AdvancedPreviewBeanForm<SimpleProjectRole> getPreviewForm() {
        return previewForm;
    }

}
