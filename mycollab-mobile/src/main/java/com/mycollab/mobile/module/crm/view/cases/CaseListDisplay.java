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
import com.mycollab.mobile.ui.DefaultPagedBeanList;
import com.mycollab.mobile.ui.MobileUIConstants;
import com.mycollab.module.crm.domain.SimpleCase;
import com.mycollab.module.crm.domain.criteria.CaseSearchCriteria;
import com.mycollab.module.crm.service.CaseService;
import com.mycollab.spring.AppContextUtil;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;

/**
 * @author MyCollab Ltd.
 * @since 4.0
 */
public class CaseListDisplay extends DefaultPagedBeanList<CaseService, CaseSearchCriteria, SimpleCase> {
    private static final long serialVersionUID = -5865353122197825948L;

    public CaseListDisplay() {
        super(AppContextUtil.getSpringBean(CaseService.class), new CaseRowDisplayHandler());
    }

    static public class CaseRowDisplayHandler implements RowDisplayHandler<SimpleCase> {

        @Override
        public Component generateRow(final SimpleCase cases, int rowIndex) {
            Button b = new Button(cases.getSubject(), new Button.ClickListener() {
                private static final long serialVersionUID = 1L;

                @Override
                public void buttonClick(ClickEvent event) {
                    EventBusFactory.getInstance().post(new CaseEvent.GotoRead(this, cases.getId()));
                }
            });

            if ("Closed".equals(cases.getStatus()) || "Rejected".equals(cases.getStatus())) {
                b.addStyleName(MobileUIConstants.LINK_COMPLETED);
            }
            b.setWidth("100%");
            return b;
        }
    }
}
