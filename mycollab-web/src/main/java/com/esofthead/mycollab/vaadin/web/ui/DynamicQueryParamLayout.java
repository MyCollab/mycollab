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
package com.esofthead.mycollab.vaadin.web.ui;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.core.arguments.SearchCriteria;
import com.esofthead.mycollab.core.db.query.Param;
import com.esofthead.mycollab.core.db.query.SearchFieldInfo;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.shell.events.ShellEvent;
import com.esofthead.mycollab.vaadin.AppContext;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
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
        MHorizontalLayout buttonControls = new MHorizontalLayout().withMargin(new MarginInfo(false, true, false, true));

        Button searchBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_SEARCH), new Button.ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(final ClickEvent event) {
                callSearchAction();
            }
        });
        searchBtn.setStyleName(UIConstants.BUTTON_ACTION);
        searchBtn.setIcon(FontAwesome.SEARCH);

        buttonControls.with(searchBtn).withAlign(searchBtn, Alignment.MIDDLE_CENTER);

        Button clearBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_CLEAR), new Button.ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(final ClickEvent event) {
                clearFields();
            }
        });
        clearBtn.setStyleName(UIConstants.BUTTON_OPTION);

        buttonControls.with(clearBtn).withAlign(clearBtn, Alignment.MIDDLE_CENTER);

        Button basicSearchBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_BASIC_SEARCH), new Button.ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(final ClickEvent event) {
                ((DefaultGenericSearchPanel<S>) searchPanel).moveToBasicSearchLayout();
            }
        });
        basicSearchBtn.setStyleName(UIConstants.BUTTON_LINK);
        buttonControls.with(basicSearchBtn).withAlign(basicSearchBtn, Alignment.MIDDLE_CENTER);
        return buttonControls;
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