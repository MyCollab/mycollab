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
package com.esofthead.mycollab.vaadin.ui;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.core.arguments.SearchCriteria;
import com.esofthead.mycollab.core.db.query.Param;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.GenericSearchPanel.SearchLayout;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import org.vaadin.maddon.layouts.MHorizontalLayout;

/**
 * @param <S>
 * @author MyCollab Ltd.
 * @since 4.0
 */
public abstract class DynamicQueryParamLayout<S extends SearchCriteria> extends SearchLayout<S> {
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
        ComponentContainer body = constructBody();
        ComponentContainer footer = constructFooter();
        this.addComponent(header, "advSearchHeader");
        this.addComponent(body, "advSearchBody");
        this.addComponent(footer, "advSearchFooter");
    }

    @Override
    protected void addHeaderRight(Component c) {
        if (this.header == null)
            return;

        this.header.addComponent(c);
    }

    private HorizontalLayout createButtonControls() {
        MHorizontalLayout buttonControls = new MHorizontalLayout().withMargin(new MarginInfo(false, true, false, true));

        Button searchBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_SEARCH), new Button.ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(final ClickEvent event) {
                DynamicQueryParamLayout.this.callSearchAction();
            }
        });
        searchBtn.setStyleName(UIConstants.THEME_GREEN_LINK);
        searchBtn.setIcon(FontAwesome.SEARCH);

        buttonControls.with(searchBtn).withAlign(searchBtn, Alignment.MIDDLE_CENTER);

        Button clearBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_CLEAR), new Button.ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(final ClickEvent event) {
                clearFields();
            }
        });
        clearBtn.setStyleName(UIConstants.THEME_GRAY_LINK);

        buttonControls.with(clearBtn).withAlign(clearBtn, Alignment.MIDDLE_CENTER);

        Button basicSearchBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_BASIC_SEARCH), new Button.ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(final ClickEvent event) {
                ((DefaultGenericSearchPanel<S>) DynamicQueryParamLayout.this.searchPanel)
                        .moveToBasicSearchLayout();
            }
        });
        basicSearchBtn.setStyleName("link");
        buttonControls.with(basicSearchBtn).withAlign(basicSearchBtn, Alignment.MIDDLE_CENTER);
        return buttonControls;
    }

    protected void clearFields() {

    }

    @Override
    protected S fillUpSearchCriteria() {
        return buildCriterionComp.fillupSearchCriteria();
    }

    protected abstract Class<S> getType();

    public abstract ComponentContainer constructHeader();

    public abstract Param[] getParamFields();

    public ComponentContainer constructBody() {
        buildCriterionComp = new BuildCriterionComponent<S>(getParamFields(), getType(), type) {
            private static final long serialVersionUID = 1L;

            @Override
            protected Component buildPropertySearchComp(String fieldId) {
                return buildSelectionComp(fieldId);
            }
        };
        return buildCriterionComp;
    }

    public ComponentContainer constructFooter() {
        return createButtonControls();
    }

    protected Component buildSelectionComp(String fieldId) {
        return null;
    }
}