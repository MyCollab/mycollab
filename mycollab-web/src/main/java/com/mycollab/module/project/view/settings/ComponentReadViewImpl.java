package com.mycollab.module.project.view.settings;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.common.i18n.OptionI18nEnum.StatusI18nEnum;
import com.mycollab.configuration.SiteConfiguration;
import com.mycollab.core.arguments.ValuedBean;
import com.mycollab.core.utils.BeanUtility;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectRolePermissionCollections;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.i18n.ComponentI18nEnum;
import com.mycollab.module.project.i18n.ProjectCommonI18nEnum;
import com.mycollab.module.project.ui.ProjectAssetsManager;
import com.mycollab.module.project.ui.components.*;
import com.mycollab.module.tracker.domain.SimpleComponent;
import com.mycollab.module.tracker.service.ComponentService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.event.HasPreviewFormHandlers;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.web.ui.*;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@ViewComponent
public class ComponentReadViewImpl extends AbstractPreviewItemComp<SimpleComponent> implements ComponentReadView {
    private static final long serialVersionUID = 1L;

    private static final Logger LOG = LoggerFactory.getLogger(ComponentReadViewImpl.class);

    private ProjectActivityComponent activityComponent;
    private TagViewComponent tagViewComponent;
    private MButton quickActionStatusBtn;

    private DateInfoComp dateInfoComp;
    private PeopleInfoComp peopleInfoComp;
    private ComponentTimeLogComp componentTimeLogComp;

    public ComponentReadViewImpl() {
        super(UserUIContext.getMessage(ComponentI18nEnum.DETAIL),
                ProjectAssetsManager.getAsset(ProjectTypeConstants.BUG_COMPONENT));
    }

    @Override
    public SimpleComponent getItem() {
        return this.beanItem;
    }

    @Override
    public HasPreviewFormHandlers<SimpleComponent> getPreviewFormHandlers() {
        return this.previewForm;
    }

    @Override
    protected String initFormTitle() {
        return beanItem.getName();
    }

    @Override
    protected void initRelatedComponents() {
        activityComponent = new ProjectActivityComponent(ProjectTypeConstants.BUG_COMPONENT, CurrentProjectVariables.getProjectId());
        dateInfoComp = new DateInfoComp();
        peopleInfoComp = new PeopleInfoComp();
        if (SiteConfiguration.isCommunityEdition()) {
            addToSideBar(dateInfoComp, peopleInfoComp);
        } else {
            componentTimeLogComp = new ComponentTimeLogComp();
            addToSideBar(dateInfoComp, peopleInfoComp, componentTimeLogComp);
        }
    }

    @Override
    protected void onPreviewItem() {
        activityComponent.loadActivities("" + beanItem.getId());
        dateInfoComp.displayEntryDateTime(beanItem);
        peopleInfoComp.displayEntryPeople(beanItem);

        if (tagViewComponent != null) {
            tagViewComponent.display(ProjectTypeConstants.BUG_COMPONENT, beanItem.getId());
        }

        if (componentTimeLogComp != null) {
            componentTimeLogComp.displayTime(beanItem);
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
            tagViewComponent = new TagViewComponent(CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.COMPONENTS));
            return tagViewComponent;
        }
    }

    @Override
    protected AdvancedPreviewBeanForm<SimpleComponent> initPreviewForm() {
        return new ComponentPreviewForm();
    }

    @Override
    protected HorizontalLayout createButtonControls() {
        ProjectPreviewFormControlsGenerator<SimpleComponent> componentPreviewForm = new ProjectPreviewFormControlsGenerator<>(previewForm);
        HorizontalLayout topPanel = componentPreviewForm.createButtonControls(ProjectRolePermissionCollections.COMPONENTS);
        quickActionStatusBtn = new MButton("", clickEvent -> {
            if (StatusI18nEnum.Closed.name().equals(beanItem.getStatus())) {
                beanItem.setStatus(StatusI18nEnum.Open.name());
                ComponentReadViewImpl.this.removeLayoutStyleName(WebThemes.LINK_COMPLETED);
                quickActionStatusBtn.setCaption(UserUIContext.getMessage(GenericI18Enum.BUTTON_CLOSE));
                quickActionStatusBtn.setIcon(FontAwesome.ARCHIVE);
            } else {
                beanItem.setStatus(StatusI18nEnum.Closed.name());
                ComponentReadViewImpl.this.addLayoutStyleName(WebThemes.LINK_COMPLETED);
                quickActionStatusBtn.setCaption(UserUIContext.getMessage(GenericI18Enum.BUTTON_REOPEN));
                quickActionStatusBtn.setIcon(FontAwesome.CLIPBOARD);
            }

            ComponentService service = AppContextUtil.getSpringBean(ComponentService.class);
            service.updateSelectiveWithSession(beanItem, UserUIContext.getUsername());
        }).withStyleName(WebThemes.BUTTON_ACTION);
        componentPreviewForm.insertToControlBlock(quickActionStatusBtn);
        quickActionStatusBtn.setVisible(CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.COMPONENTS));
        return topPanel;
    }

    @Override
    protected ComponentContainer createBottomPanel() {
        return activityComponent;
    }

    @Override
    protected String getType() {
        return ProjectTypeConstants.BUG_COMPONENT;
    }

    private static class PeopleInfoComp extends MVerticalLayout {
        private static final long serialVersionUID = 1L;

        public void displayEntryPeople(ValuedBean bean) {
            this.removeAllComponents();
            this.withMargin(false);

            Label peopleInfoHeader = new Label(FontAwesome.USER.getHtml() + " " +
                    UserUIContext.getMessage(ProjectCommonI18nEnum.SUB_INFO_PEOPLE), ContentMode.HTML);
            peopleInfoHeader.setStyleName("info-hdr");
            this.addComponent(peopleInfoHeader);

            GridLayout layout = new GridLayout(2, 2);
            layout.setSpacing(true);
            layout.setWidth("100%");
            layout.setMargin(new MarginInfo(false, false, false, true));
            try {
                Label createdLbl = new Label(UserUIContext.getMessage(ProjectCommonI18nEnum.ITEM_CREATED_PEOPLE));
                createdLbl.setSizeUndefined();
                layout.addComponent(createdLbl, 0, 0);

                String createdUserName = (String) PropertyUtils.getProperty(bean, "createduser");
                String createdUserAvatarId = (String) PropertyUtils.getProperty(bean, "createdUserAvatarId");
                String createdUserDisplayName = (String) PropertyUtils.getProperty(bean, "createdUserFullName");

                ProjectMemberLink createdUserLink = new ProjectMemberLink(createdUserName,
                        createdUserAvatarId, createdUserDisplayName);
                layout.addComponent(createdUserLink, 1, 0);
                layout.setColumnExpandRatio(1, 1.0f);

                Label assigneeLbl = new Label(UserUIContext.getMessage(ProjectCommonI18nEnum.ITEM_ASSIGN_PEOPLE));
                assigneeLbl.setSizeUndefined();
                layout.addComponent(assigneeLbl, 0, 1);
                String assignUserName = (String) PropertyUtils.getProperty(bean, "userlead");
                String assignUserAvatarId = (String) PropertyUtils.getProperty(bean, "userLeadAvatarId");
                String assignUserDisplayName = (String) PropertyUtils.getProperty(bean, "userLeadFullName");

                ProjectMemberLink assignUserLink = new ProjectMemberLink(assignUserName,
                        assignUserAvatarId, assignUserDisplayName);
                layout.addComponent(assignUserLink, 1, 1);
            } catch (Exception e) {
                LOG.error("Can not build user link {} ", BeanUtility.printBeanObj(bean));
            }

            this.addComponent(layout);
        }
    }
}
