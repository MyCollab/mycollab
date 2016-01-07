/**
 * This file is part of mycollab-mobile.
 *
 * mycollab-mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-mobile.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.mobile.module.project.view.bug;

import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.mobile.module.project.events.BugEvent;
import com.esofthead.mycollab.mobile.module.project.ui.AbstractListViewComp;
import com.esofthead.mycollab.mobile.ui.AbstractPagedBeanList;
import com.esofthead.mycollab.module.project.i18n.BugI18nEnum;
import com.esofthead.mycollab.module.tracker.domain.SimpleBug;
import com.esofthead.mycollab.module.tracker.domain.criteria.BugSearchCriteria;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.vaadin.navigationbarquickmenu.NavigationBarQuickMenu;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * @author MyCollab Ltd.
 * @since 4.5.2
 */
@ViewComponent
public class BugListViewImpl extends AbstractListViewComp<BugSearchCriteria, SimpleBug> implements BugListView {
    private static final long serialVersionUID = -7877935907665712184L;

    public BugListViewImpl() {
        this.setCaption(AppContext.getMessage(BugI18nEnum.VIEW_LIST_TITLE));
    }

    @Override
    protected AbstractPagedBeanList<BugSearchCriteria, SimpleBug> createBeanList() {
        return new BugListDisplay();
    }

    @Override
    protected Component buildRightComponent() {
        NavigationBarQuickMenu menu = new NavigationBarQuickMenu();
        menu.setButtonCaption("...");
        MVerticalLayout content = new MVerticalLayout();
        content.with(new Button(AppContext.getMessage(BugI18nEnum.BUTTON_NEW_BUG), new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                EventBusFactory.getInstance().post(new BugEvent.GotoAdd(this, null));
            }
        }));
        menu.setContent(content);
        return menu;
    }
}
