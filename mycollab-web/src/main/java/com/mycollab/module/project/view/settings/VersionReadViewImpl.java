package com.mycollab.module.project.view.settings;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.common.i18n.OptionI18nEnum.StatusI18nEnum;
import com.mycollab.configuration.SiteConfiguration;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectRolePermissionCollections;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.i18n.VersionI18nEnum;
import com.mycollab.module.project.ui.ProjectAssetsManager;
import com.mycollab.module.project.ui.components.ProjectPreviewFormControlsGenerator;
import com.mycollab.vaadin.web.ui.*;
import com.mycollab.module.project.ui.components.DateInfoComp;
import com.mycollab.module.project.ui.components.ProjectActivityComponent;
import com.mycollab.module.project.ui.components.TagViewComponent;
import com.mycollab.module.tracker.domain.Version;
import com.mycollab.module.tracker.service.VersionService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.event.HasPreviewFormHandlers;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.HorizontalLayout;
import org.vaadin.viritin.button.MButton;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@ViewComponent
public class VersionReadViewImpl extends AbstractPreviewItemComp<Version> implements VersionReadView {
    private static final long serialVersionUID = 1L;

    private MButton quickActionStatusBtn;
    private TagViewComponent tagViewComponent;
    private ProjectActivityComponent activityComponent;
    private DateInfoComp dateInfoComp;
    private VersionTimeLogComp versionTimeLogComp;

    public VersionReadViewImpl() {
        super(UserUIContext.getMessage(VersionI18nEnum.DETAIL),
                ProjectAssetsManager.getAsset(ProjectTypeConstants.BUG_VERSION));
    }

    @Override
    public HasPreviewFormHandlers<Version> getPreviewFormHandlers() {
        return this.previewForm;
    }

    @Override
    protected void initRelatedComponents() {
        activityComponent = new ProjectActivityComponent(ProjectTypeConstants.BUG_VERSION,
                CurrentProjectVariables.getProjectId());

        dateInfoComp = new DateInfoComp();
        if (SiteConfiguration.isCommunityEdition()) {
            addToSideBar(dateInfoComp);
        } else {
            versionTimeLogComp = new VersionTimeLogComp();
            addToSideBar(dateInfoComp, versionTimeLogComp);
        }
    }

    @Override
    protected void onPreviewItem() {
        activityComponent.loadActivities("" + beanItem.getId());
        dateInfoComp.displayEntryDateTime(beanItem);

        if (tagViewComponent != null) {
            tagViewComponent.display(ProjectTypeConstants.BUG_VERSION, beanItem.getId());
        }

        if (versionTimeLogComp != null) {
            versionTimeLogComp.displayTime(beanItem);
        }

        if (StatusI18nEnum.Open.name().equals(beanItem.getStatus())) {
            removeLayoutStyleName(WebThemes.LINK_COMPLETED);
            quickActionStatusBtn.setCaption(UserUIContext.getMessage(GenericI18Enum.BUTTON_CLOSE));
            quickActionStatusBtn.setIcon(FontAwesome.ARCHIVE);
        } else {
            addLayoutStyleName(WebThemes.LINK_COMPLETED);
            quickActionStatusBtn.setCaption(UserUIContext.getMessage(GenericI18Enum.BUTTON_REOPEN));
            quickActionStatusBtn.setIcon(FontAwesome.CLIPBOARD);
        }
    }

    @Override
    protected ComponentContainer createExtraControls() {
        if (SiteConfiguration.isCommunityEdition()) {
            return null;
        } else {
            tagViewComponent = new TagViewComponent(CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.VERSIONS));
            return tagViewComponent;
        }
    }

    @Override
    protected String initFormTitle() {
        return beanItem.getName();
    }

    @Override
    protected AdvancedPreviewBeanForm<Version> initPreviewForm() {
        return new VersionPreviewForm();
    }

    @Override
    protected HorizontalLayout createButtonControls() {
        ProjectPreviewFormControlsGenerator<Version> versionPreviewForm = new ProjectPreviewFormControlsGenerator<>(previewForm);
        HorizontalLayout topPanel = versionPreviewForm.createButtonControls(ProjectRolePermissionCollections.VERSIONS);

        quickActionStatusBtn = new MButton("", clickEvent -> {
            if (StatusI18nEnum.Closed.name().equals(beanItem.getStatus())) {
                beanItem.setStatus(StatusI18nEnum.Open.name());
                VersionReadViewImpl.this.removeLayoutStyleName(WebThemes.LINK_COMPLETED);
                quickActionStatusBtn.setCaption(UserUIContext.getMessage(GenericI18Enum.BUTTON_CLOSE));
                quickActionStatusBtn.setIcon(FontAwesome.ARCHIVE);
            } else {
                beanItem.setStatus(StatusI18nEnum.Closed.name());
                VersionReadViewImpl.this.addLayoutStyleName(WebThemes.LINK_COMPLETED);
                quickActionStatusBtn.setCaption(UserUIContext.getMessage(GenericI18Enum.BUTTON_REOPEN));
                quickActionStatusBtn.setIcon(FontAwesome.CLIPBOARD);
            }

            VersionService service = AppContextUtil.getSpringBean(VersionService.class);
            service.updateSelectiveWithSession(beanItem, UserUIContext.getUsername());
        }).withStyleName(WebThemes.BUTTON_ACTION)
                .withVisible(CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.VERSIONS));
        versionPreviewForm.insertToControlBlock(quickActionStatusBtn);
        return topPanel;
    }

    @Override
    protected ComponentContainer createBottomPanel() {
        return activityComponent;
    }

    @Override
    public Version getItem() {
        return beanItem;
    }

    @Override
    protected String getType() {
        return ProjectTypeConstants.BUG_VERSION;
    }

}