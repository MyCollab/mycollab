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
package com.mycollab.vaadin.web.ui;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.db.arguments.SearchCriteria;
import com.mycollab.db.query.Param;
import com.mycollab.db.query.SearchFieldInfo;
import com.mycollab.eventmanager.EventBusFactory;
import com.mycollab.shell.events.ShellEvent;
import com.mycollab.vaadin.AppContext;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import java.util.List;

/**
 * @param <S>
 * @author MyCollab Ltd.
 * @since 4.0
 */
public abstract class DynamicQueryParamLayout<S extends SearchCriteria> extends GenericSearchPanel.SearchLayout<S> {
    private static final long serialVersionUID = 1L;

    protected String type;
    protected BuildCriterionComponent<S> buildCriterionComp;
    protected ComponentContainer header;

    public DynamicQueryParamLayout(DefaultGenericSearchPanel<S> parent, String type) {
        super(parent, "advancedSearch");
        setStyleName("advancedSearchLayout");
        this.type = type;
        initLayout();
    }

    protected void initLayout() {
        header = constructHeader();
        buildCriterionComp = new BuildCriterionComponent<S>(this, getParamFields(), getType(), type) {
            private static final long serialVersionUID = 1L;

            @Override
            protected Component buildPropertySearchComp(String fieldId) {
                return buildSelectionComp(fieldId);
            }
        };

        this.addComponent(header, "advSearchHeader");
        this.addComponent(buildCriterionComp, "advSearchBody");
        this.addComponent(createButtonControls(), "advSearchFooter");
    }

    @Override
    protected void addHeaderRight(Component c) {
        if (header == null)
            return;

        header.addComponent(c);
    }

    private HorizontalLayout createButtonControls() {
        MButton searchBtn = new MButton(AppContext.getMessage(GenericI18Enum.BUTTON_SEARCH), clickEvent -> callSearchAction())
                .withStyleName(UIConstants.BUTTON_ACTION).withIcon(FontAwesome.SEARCH);

        MButton clearBtn = new MButton(AppContext.getMessage(GenericI18Enum.BUTTON_CLEAR), clickEvent -> clearFields())
                .withStyleName(UIConstants.BUTTON_OPTION);

        MButton basicSearchBtn = new MButton(AppContext.getMessage(GenericI18Enum.BUTTON_BASIC_SEARCH),
                clickEvent -> ((DefaultGenericSearchPanel<S>) searchPanel).moveToBasicSearchLayout())
                .withStyleName(UIConstants.BUTTON_LINK);

        return new MHorizontalLayout(searchBtn, clearBtn, basicSearchBtn).withMargin(new MarginInfo(false, true, false, true));
    }

    protected void clearFields() {
        buildCriterionComp.clearAllFields();
    }

    public void displaySearchFieldInfos(List<SearchFieldInfo> searchFieldInfos) {
        buildCriterionComp.fillSearchFieldInfoAndInvokeSearchRequest(searchFieldInfos);
    }

    @Override
    protected S fillUpSearchCriteria() {
        List<SearchFieldInfo> searchFieldInfos = buildCriterionComp.buildSearchFieldInfos();
        EventBusFactory.getInstance().post(new ShellEvent.AddQueryParam(this, searchFieldInfos));
        return SearchFieldInfo.buildSearchCriteria(getType(), searchFieldInfos);
    }

    protected abstract Class<S> getType();

    public abstract ComponentContainer constructHeader();

    public abstract Param[] getParamFields();

    protected Component buildSelectionComp(String fieldId) {
        return null;
    }
}