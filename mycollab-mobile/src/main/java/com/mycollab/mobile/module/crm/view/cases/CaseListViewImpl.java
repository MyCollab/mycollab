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
package com.mycollab.mobile.module.crm.view.cases;

import com.mycollab.eventmanager.EventBusFactory;
import com.mycollab.mobile.module.crm.events.CaseEvent;
import com.mycollab.mobile.module.crm.ui.AbstractListViewComp;
import com.mycollab.mobile.ui.AbstractPagedBeanList;
import com.mycollab.module.crm.domain.SimpleCase;
import com.mycollab.module.crm.domain.criteria.CaseSearchCriteria;
import com.mycollab.module.crm.i18n.CaseI18nEnum;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.vaadin.ui.Component;
import org.vaadin.viritin.button.MButton;

/**
 * @author MyCollab Ltd.
 * @since 4.0
 */
@ViewComponent
public class CaseListViewImpl extends AbstractListViewComp<CaseSearchCriteria, SimpleCase> implements CaseListView {
    private static final long serialVersionUID = -2790165346072368795L;

    public CaseListViewImpl() {
        super();
        setCaption(UserUIContext.getMessage(CaseI18nEnum.NEW));
    }

    @Override
    protected AbstractPagedBeanList<CaseSearchCriteria, SimpleCase> createBeanTable() {
        return new CaseListDisplay();
    }

    @Override
    protected Component createRightComponent() {
        return new MButton("", clickEvent -> EventBusFactory.getInstance().post(new CaseEvent.GotoAdd(this, null)))
                .withStyleName("add-btn");
    }

}
