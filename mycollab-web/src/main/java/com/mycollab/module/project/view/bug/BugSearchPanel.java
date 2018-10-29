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
package com.mycollab.module.project.view.bug;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.db.arguments.SearchField;
import com.mycollab.db.query.ConstantValueInjector;
import com.mycollab.db.query.Param;
import com.mycollab.db.query.SearchFieldInfo;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.event.BugEvent;
import com.mycollab.module.project.ui.ProjectAssetsManager;
import com.mycollab.module.project.view.milestone.MilestoneListSelect;
import com.mycollab.module.project.view.settings.component.ComponentListSelect;
import com.mycollab.module.project.view.settings.component.ProjectMemberListSelect;
import com.mycollab.module.project.view.settings.component.VersionListSelect;
import com.mycollab.module.tracker.domain.criteria.BugSearchCriteria;
import com.mycollab.shell.event.ShellEvent;
import com.mycollab.vaadin.EventBusFactory;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.web.ui.*;
import com.vaadin.event.ShortcutAction;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.*;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.mycollab.common.i18n.QueryI18nEnum.CollectionI18nEnum;
import static com.mycollab.common.i18n.QueryI18nEnum.StringI18nEnum;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class BugSearchPanel extends DefaultGenericSearchPanel<BugSearchCriteria> {
    private static final long serialVersionUID = 1L;

    private BugSearchCriteria searchCriteria;
    private BugSavedFilterComboBox savedFilterComboBox;

    private static Param[] paramFields = new Param[]{
            BugSearchCriteria.p_textDesc, BugSearchCriteria.p_priority,
            BugSearchCriteria.p_severity, BugSearchCriteria.p_status,
            BugSearchCriteria.p_milestones, BugSearchCriteria.p_affectedVersions,
            BugSearchCriteria.p_fixedVersions, BugSearchCriteria.p_components,
            BugSearchCriteria.p_assignee, BugSearchCriteria.p_resolveddate,
            BugSearchCriteria.p_duedate, BugSearchCriteria.p_createdtime,
            BugSearchCriteria.p_bugkey, BugSearchCriteria.p_lastupdatedtime};

    public BugSearchPanel(boolean canSwitchToAdvanceLayout) {
        super(canSwitchToAdvanceLayout);
    }

    @Override
    protected ComponentContainer buildSearchTitle() {
        if (canSwitchToAdvanceLayout) {
            savedFilterComboBox = new BugSavedFilterComboBox();
            savedFilterComboBox.addQuerySelectListener(new SavedFilterComboBox.QuerySelectListener() {
                @Override
                public void querySelect(SavedFilterComboBox.QuerySelectEvent querySelectEvent) {
                    List<SearchFieldInfo<BugSearchCriteria>> fieldInfos = querySelectEvent.getSearchFieldInfos();
                    BugSearchCriteria criteria = SearchFieldInfo.buildSearchCriteria(BugSearchCriteria.class, fieldInfos);
                    criteria.setProjectId(new NumberSearchField(CurrentProjectVariables.getProjectId()));
                    EventBusFactory.getInstance().post(new BugEvent.SearchRequest(BugSearchPanel.this, criteria));
                    EventBusFactory.getInstance().post(new ShellEvent.AddQueryParam(this, fieldInfos));
                }
            });
            ELabel taskIcon = ELabel.h2(ProjectAssetsManager.getAsset(ProjectTypeConstants.BUG).getHtml()).withUndefinedWidth();
            return new MHorizontalLayout(taskIcon, savedFilterComboBox).expand(savedFilterComboBox).alignAll(Alignment.MIDDLE_LEFT);
        } else {
            return null;
        }
    }

    public void displaySearchFieldInfos(List<SearchFieldInfo<BugSearchCriteria>> searchFieldInfos) {
        if (canSwitchToAdvanceLayout) {
            BugAdvancedSearchLayout advancedSearchLayout = (BugAdvancedSearchLayout) moveToAdvancedSearchLayout();
            advancedSearchLayout.displaySearchFieldInfos(searchFieldInfos);
        }
    }

    @Override
    public void setTotalCountNumber(Integer countNumber) {
        savedFilterComboBox.setTotalCountNumber(countNumber);
    }

    public void selectQueryInfo(String queryId) {
        savedFilterComboBox.selectQueryInfo(queryId);
    }

    @Override
    protected SearchLayout<BugSearchCriteria> createBasicSearchLayout() {
        return new BugBasicSearchLayout();
    }

    @Override
    protected SearchLayout<BugSearchCriteria> createAdvancedSearchLayout() {
        return new BugAdvancedSearchLayout();
    }

    private class BugBasicSearchLayout extends BasicSearchLayout<BugSearchCriteria> {
        private static final long serialVersionUID = 1L;
        private TextField nameField;
        private CheckBox myItemCheckbox;

        BugBasicSearchLayout() {
            super(BugSearchPanel.this);
        }

        @Override
        public ComponentContainer constructBody() {
            MHorizontalLayout basicSearchBody = new MHorizontalLayout().withMargin(true);
            Label nameLbl = new Label(UserUIContext.getMessage(GenericI18Enum.FORM_NAME) + ":");
            basicSearchBody.with(nameLbl).withAlign(nameLbl, Alignment.MIDDLE_LEFT);

            nameField = new MTextField().withPlaceholder(UserUIContext.getMessage(GenericI18Enum.ACTION_QUERY_BY_TEXT))
                    .withWidth(WebUIConstants.DEFAULT_CONTROL_WIDTH);
            basicSearchBody.with(nameField).withAlign(nameLbl, Alignment.MIDDLE_CENTER);

            myItemCheckbox = new CheckBox(UserUIContext.getMessage(GenericI18Enum.OPT_MY_ITEMS));
            basicSearchBody.with(myItemCheckbox).withAlign(myItemCheckbox, Alignment.MIDDLE_CENTER);

            MButton searchBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_SEARCH), clickEvent -> callSearchAction())
                    .withIcon(VaadinIcons.SEARCH).withStyleName(WebThemes.BUTTON_ACTION)
                    .withClickShortcut(ShortcutAction.KeyCode.ENTER);
            basicSearchBody.with(searchBtn).withAlign(searchBtn, Alignment.MIDDLE_LEFT);

            MButton cancelBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_CLEAR), clickEvent -> nameField.setValue(""))
                    .withStyleName(WebThemes.BUTTON_OPTION);
            basicSearchBody.addComponent(cancelBtn);

            if (canSwitchToAdvanceLayout) {
                MButton advancedSearchBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_ADVANCED_SEARCH),
                        clickEvent -> moveToAdvancedSearchLayout()).withStyleName(WebThemes.BUTTON_LINK);
                basicSearchBody.with(advancedSearchBtn).withAlign(advancedSearchBtn, Alignment.MIDDLE_CENTER);
            }

            return basicSearchBody;
        }

        @Override
        protected BugSearchCriteria fillUpSearchCriteria() {
            List<SearchFieldInfo<BugSearchCriteria>> searchFieldInfos = new ArrayList<>();
            searchFieldInfos.add(new SearchFieldInfo(SearchField.AND, BugSearchCriteria.p_textDesc, StringI18nEnum.CONTAINS.name(),
                    ConstantValueInjector.valueOf(nameField.getValue().trim())));
            if (myItemCheckbox.getValue()) {
                searchFieldInfos.add(new SearchFieldInfo(SearchField.AND, BugSearchCriteria.p_assignee, CollectionI18nEnum.IN.name(),
                        ConstantValueInjector.valueOf(Collections.singletonList(UserUIContext.getUsername()))));
            }
            EventBusFactory.getInstance().post(new ShellEvent.AddQueryParam(this, searchFieldInfos));
            searchCriteria = SearchFieldInfo.buildSearchCriteria(BugSearchCriteria.class,
                    searchFieldInfos);
            searchCriteria.setProjectId(new NumberSearchField(CurrentProjectVariables.getProjectId()));
            return searchCriteria;
        }
    }

    private class BugAdvancedSearchLayout extends DynamicQueryParamLayout<BugSearchCriteria> {
        private static final long serialVersionUID = 1L;

        BugAdvancedSearchLayout() {
            super(BugSearchPanel.this, ProjectTypeConstants.BUG);
        }

        @Override
        protected Class<BugSearchCriteria> getType() {
            return BugSearchCriteria.class;
        }

        @Override
        public Param[] getParamFields() {
            return paramFields;
        }

        @Override
        protected Component buildSelectionComp(String fieldId) {
            if ("assignuser".equals(fieldId)) {
                return new ProjectMemberListSelect(false, Arrays.asList(CurrentProjectVariables.getProjectId()));
            } else if ("affected_versions".equals(fieldId)) {
                return new VersionListSelect();
            } else if ("fixed_versions".equals(fieldId)) {
                return new VersionListSelect();
            } else if ("components".equals(fieldId)) {
                return new ComponentListSelect();
            } else if ("milestones".equals(fieldId)) {
                return new MilestoneListSelect();
            }
            return null;
        }

        @Override
        protected BugSearchCriteria fillUpSearchCriteria() {
            searchCriteria = super.fillUpSearchCriteria();
            searchCriteria.setProjectId(new NumberSearchField(CurrentProjectVariables.getProjectId()));
            return searchCriteria;
        }
    }
}