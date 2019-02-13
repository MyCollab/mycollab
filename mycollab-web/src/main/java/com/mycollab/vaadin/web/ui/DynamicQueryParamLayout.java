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
package com.mycollab.vaadin.web.ui;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.db.arguments.SearchCriteria;
import com.mycollab.db.query.Param;
import com.mycollab.db.query.SearchFieldInfo;
import com.mycollab.shell.event.ShellEvent;
import com.mycollab.vaadin.EventBusFactory;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.web.CustomLayoutExt;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.HorizontalLayout;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import java.util.List;

/**
 * @param <S>
 * @author MyCollab Ltd.
 * @since 4.0
 */
public abstract class DynamicQueryParamLayout<S extends SearchCriteria> extends SearchLayout<S> {
    private static final long serialVersionUID = 1L;

    protected String type;
    private BuildCriterionComponent<S> buildCriterionComp;
    private MHorizontalLayout headerLayout;

    public DynamicQueryParamLayout(DefaultGenericSearchPanel<S> parent, String type) {
        super(parent);
        CustomLayout layout = CustomLayoutExt.createLayout("advancedSearch");
        this.type = type;
        headerLayout = constructHeader();
        buildCriterionComp = new BuildCriterionComponent<S>(this, getParamFields(), type) {
            private static final long serialVersionUID = 1L;

            @Override
            protected Component buildPropertySearchComp(String fieldId) {
                return buildSelectionComp(fieldId);
            }
        };

        layout.addComponent(headerLayout, "advSearchHeader");
        layout.addComponent(buildCriterionComp, "advSearchBody");
        layout.addComponent(createButtonControls(), "advSearchFooter");
        setCompositionRoot(layout);
    }

    private HorizontalLayout createButtonControls() {
        MButton searchBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_SEARCH), clickEvent -> callSearchAction())
                .withStyleName(WebThemes.BUTTON_ACTION).withIcon(VaadinIcons.SEARCH);

        MButton clearBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_CLEAR), clickEvent -> clearFields())
                .withStyleName(WebThemes.BUTTON_OPTION);

        MButton basicSearchBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_BASIC_SEARCH),
                clickEvent -> searchPanel.moveToBasicSearchLayout())
                .withStyleName(WebThemes.BUTTON_LINK);

        return new MHorizontalLayout(searchBtn, clearBtn, basicSearchBtn).withMargin(new MarginInfo(false, true, false, true));
    }

    private void clearFields() {
        buildCriterionComp.clearAllFields();
    }

    public void displaySearchFieldInfos(List<SearchFieldInfo<S>> searchFieldInfos) {
        buildCriterionComp.fillSearchFieldInfoAndInvokeSearchRequest(searchFieldInfos);
    }

    @Override
    protected S fillUpSearchCriteria() {
        List<SearchFieldInfo<S>> searchFieldInfos = buildCriterionComp.buildSearchFieldInfos();
        EventBusFactory.getInstance().post(new ShellEvent.AddQueryParam(this, searchFieldInfos));
        return SearchFieldInfo.buildSearchCriteria(getType(), searchFieldInfos);
    }

    protected abstract Class<S> getType();

    private MHorizontalLayout constructHeader() {
        return searchPanel.getHeader();
    }

    public abstract Param[] getParamFields();

    protected Component buildSelectionComp(String fieldId) {
        return null;
    }
}