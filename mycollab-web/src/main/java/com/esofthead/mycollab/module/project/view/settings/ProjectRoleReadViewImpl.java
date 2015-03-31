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
import com.esofthead.mycollab.module.project.i18n.ProjectRoleI18nEnum;
import com.esofthead.mycollab.module.project.i18n.RolePermissionI18nEnum;
import com.esofthead.mycollab.security.PermissionMap;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.HasPreviewFormHandlers;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.*;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import org.vaadin.maddon.layouts.MHorizontalLayout;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@ViewComponent
public class ProjectRoleReadViewImpl extends VerticalLayout implements
        ProjectRoleReadView {

    private static final long serialVersionUID = 1L;

    private SimpleProjectRole beanItem;
    private AdvancedPreviewBeanForm<SimpleProjectRole> previewForm;
    private ReadViewLayout previewLayout;
    private Label headerText;
    private MHorizontalLayout header;

    private GridFormLayoutHelper projectFormHelper;

    public ProjectRoleReadViewImpl() {
        this.headerText = new Label();
        headerText.setCaption(AppContext.getMessage(ProjectRoleI18nEnum.FORM_READ_TITLE));
        headerText.setIcon(FontAwesome.USERS);
        headerText.addStyleName("headerName");

        this.headerText.setSizeUndefined();
        this.addComponent(constructHeader());

        previewForm = initPreviewForm();
        ComponentContainer actionControls = createButtonControls();
        if (actionControls != null) {
            actionControls.addStyleName("control-buttons");
        }

        addHeaderRightContent(actionControls);

        CssLayout contentWrapper = new CssLayout();
        contentWrapper.setStyleName("content-wrapper");

        previewLayout = new DefaultReadViewLayout("");

        contentWrapper.addComponent(previewLayout);

        previewLayout.addBody(previewForm);

        this.addComponent(contentWrapper);
    }

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

    protected ComponentContainer createButtonControls() {
        return (new ProjectPreviewFormControlsGenerator<>(
                previewForm))
                .createButtonControls(ProjectRolePermissionCollections.ROLES);
    }

    protected ComponentContainer createBottomPanel() {
        VerticalLayout permissionsPanel = new VerticalLayout();
        final Label organizationHeader = new Label(
                AppContext.getMessage(ProjectRoleI18nEnum.SECTION_PERMISSIONS));
        organizationHeader.setStyleName("h2");
        permissionsPanel.addComponent(organizationHeader);

        projectFormHelper = GridFormLayoutHelper.defaultFormLayoutHelper(2,
                ProjectRolePermissionCollections.PROJECT_PERMISSIONS.length);

        permissionsPanel.addComponent(projectFormHelper.getLayout());

        return permissionsPanel;
    }

    protected void onPreviewItem() {
        projectFormHelper.getLayout().removeAllComponents();

        final PermissionMap permissionMap = beanItem.getPermissionMap();
        for (int i = 0; i < ProjectRolePermissionCollections.PROJECT_PERMISSIONS.length; i++) {
            final String permissionPath = ProjectRolePermissionCollections.PROJECT_PERMISSIONS[i];
            projectFormHelper.addComponent(
                    new Label(AppContext.getPermissionCaptionValue(
                            permissionMap,
                            RolePermissionI18nEnum.valueOf(permissionPath)
                                    .name())), AppContext
                            .getMessage(RolePermissionI18nEnum
                                    .valueOf(permissionPath)), 0, i);
        }

    }

    protected String initFormTitle() {
        return beanItem.getRolename();
    }

    protected IFormLayoutFactory initFormLayoutFactory() {
        return new ProjectRoleFormLayoutFactory();
    }

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
        header = new MHorizontalLayout().withStyleName("hdr-view")
                .withWidth("100%").withMargin(true);

        headerText.setStyleName("header-text");

        header.with(headerText).alignAll(Alignment.MIDDLE_LEFT)
                .expand(headerText);

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

    @Override
    public ComponentContainer getWidget() {
        return this;
    }

    @Override
    public void addViewListener(ViewListener listener) {

    }

}
