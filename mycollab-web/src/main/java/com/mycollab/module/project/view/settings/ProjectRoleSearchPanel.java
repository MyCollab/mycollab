package com.mycollab.module.project.view.settings;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.db.arguments.StringSearchField;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectRolePermissionCollections;
import com.mycollab.module.project.domain.criteria.ProjectRoleSearchCriteria;
import com.mycollab.module.project.event.ProjectRoleEvent;
import com.mycollab.module.project.i18n.ProjectRoleI18nEnum;
import com.mycollab.vaadin.EventBusFactory;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.HeaderWithFontAwesome;
import com.mycollab.vaadin.web.ui.*;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.layouts.MHorizontalLayout;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class ProjectRoleSearchPanel extends DefaultGenericSearchPanel<ProjectRoleSearchCriteria> {
    private static final long serialVersionUID = 1L;

    @Override
    protected SearchLayout<ProjectRoleSearchCriteria> createBasicSearchLayout() {
        return new ProjectRoleBasicSearchLayout();
    }

    @Override
    protected SearchLayout<ProjectRoleSearchCriteria> createAdvancedSearchLayout() {
        return null;
    }

    @Override
    protected HeaderWithFontAwesome buildSearchTitle() {
        return HeaderWithFontAwesome.h2(FontAwesome.GROUP, UserUIContext.getMessage(ProjectRoleI18nEnum.LIST));
    }

    @Override
    protected Component buildExtraControls() {
        if (CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.ROLES)) {
            return new MButton(UserUIContext.getMessage(ProjectRoleI18nEnum.NEW),
                    clickEvent -> EventBusFactory.getInstance().post(new ProjectRoleEvent.GotoAdd(this, null)))
                    .withIcon(FontAwesome.PLUS).withStyleName(WebThemes.BUTTON_ACTION);
        } else return null;
    }

    private class ProjectRoleBasicSearchLayout extends BasicSearchLayout<ProjectRoleSearchCriteria> {
        private static final long serialVersionUID = 1L;
        private TextField nameField;

        private ProjectRoleBasicSearchLayout() {
            super(ProjectRoleSearchPanel.this);
        }

        @Override
        public ComponentContainer constructBody() {
            MHorizontalLayout basicSearchBody = new MHorizontalLayout().withMargin(true);
            basicSearchBody.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
            basicSearchBody.addComponent(new Label(UserUIContext.getMessage(GenericI18Enum.FORM_NAME) + ":"));
            nameField = new MTextField().withInputPrompt(UserUIContext.getMessage(GenericI18Enum.ACTION_QUERY_BY_TEXT))
                    .withWidth(WebUIConstants.DEFAULT_CONTROL_WIDTH);
            basicSearchBody.addComponent(nameField);

            MButton searchBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_SEARCH), clickEvent -> callSearchAction())
                    .withIcon(FontAwesome.SEARCH).withStyleName(WebThemes.BUTTON_ACTION)
                    .withClickShortcut(ShortcutAction.KeyCode.ENTER);
            basicSearchBody.addComponent(searchBtn);

            MButton clearBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_CLEAR), clickEvent -> nameField.setValue(""))
                    .withStyleName(WebThemes.BUTTON_OPTION);
            basicSearchBody.addComponent(clearBtn);
            return basicSearchBody;
        }

        @Override
        protected ProjectRoleSearchCriteria fillUpSearchCriteria() {
            ProjectRoleSearchCriteria searchCriteria = new ProjectRoleSearchCriteria();
            searchCriteria.setProjectId(new NumberSearchField(CurrentProjectVariables.getProjectId()));
            searchCriteria.setRolename(StringSearchField.and(nameField.getValue()));
            return searchCriteria;
        }
    }
}