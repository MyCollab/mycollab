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
package com.esofthead.mycollab.module.project.view.bug;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.core.arguments.SetSearchField;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum.BugStatus;
import com.esofthead.mycollab.module.tracker.domain.criteria.BugSearchCriteria;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.web.ui.GenericSearchPanel;
import com.esofthead.mycollab.vaadin.web.ui.ShortcutExtension;
import com.esofthead.mycollab.vaadin.web.ui.UIConstants;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@SuppressWarnings("serial")
public class BugSimpleSearchPanel extends GenericSearchPanel<BugSearchCriteria> {
    private BugSearchCriteria searchCriteria;
    private TextField textValueField;
    private GridLayout layoutSearchPane;
    private CheckBox chkIsOpenBug;

    public BugSimpleSearchPanel() {
        createBasicSearchLayout();
    }

    private void createBasicSearchLayout() {
        layoutSearchPane = new GridLayout(5, 3);
        layoutSearchPane.setSpacing(true);

        addTextFieldSearch();

        chkIsOpenBug = new CheckBox("Only Open Bugs");
        layoutSearchPane.addComponent(chkIsOpenBug, 2, 0);
        layoutSearchPane.setComponentAlignment(chkIsOpenBug,
                Alignment.MIDDLE_CENTER);

        Button searchBtn = new Button(
                AppContext.getMessage(GenericI18Enum.BUTTON_SEARCH));
        searchBtn.setStyleName(UIConstants.BUTTON_ACTION);
        searchBtn.setIcon(FontAwesome.SEARCH);
        searchBtn.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                doSearch();
            }
        });
        layoutSearchPane.addComponent(searchBtn, 3, 0);
        layoutSearchPane.setComponentAlignment(searchBtn,
                Alignment.MIDDLE_CENTER);

        Button clearBtn = new Button(
                AppContext.getMessage(GenericI18Enum.BUTTON_CLEAR));
        clearBtn.setStyleName(UIConstants.THEME_GRAY_LINK);
        clearBtn.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                textValueField.setValue("");
            }
        });

        layoutSearchPane.addComponent(clearBtn, 4, 0);
        layoutSearchPane.setComponentAlignment(clearBtn,
                Alignment.MIDDLE_CENTER);

        this.setCompositionRoot(layoutSearchPane);
    }

    private void doSearch() {
        searchCriteria = new BugSearchCriteria();
        searchCriteria.setProjectId(new NumberSearchField(
                SearchField.AND, CurrentProjectVariables.getProject().getId()));
        searchCriteria.setSummary(new StringSearchField(textValueField.getValue().trim()));

        if (chkIsOpenBug.getValue()) {
            searchCriteria.setStatuses(new SetSearchField<>(BugStatus.InProgress.name(), BugStatus.Open.name(), BugStatus.ReOpened.name()));
        }

        notifySearchHandler(searchCriteria);
    }

    private void addTextFieldSearch() {
        textValueField = ShortcutExtension.installShortcutAction(new TextField(),
                new ShortcutListener("BugSearchRequest", ShortcutAction.KeyCode.ENTER, null) {
                    @Override
                    public void handleAction(Object o, Object o1) {
                        doSearch();
                    }
                });
        textValueField.setWidth("300px");
        layoutSearchPane.addComponent(textValueField, 0, 0);
        layoutSearchPane.setComponentAlignment(textValueField,
                Alignment.MIDDLE_CENTER);
    }

    @Override
    public void setTotalCountNumber(int totalCountNumber) {

    }
}
