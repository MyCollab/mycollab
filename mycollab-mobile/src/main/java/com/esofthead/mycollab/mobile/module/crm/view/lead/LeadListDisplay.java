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
package com.esofthead.mycollab.mobile.module.crm.view.lead;

import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.mobile.module.crm.events.LeadEvent;
import com.esofthead.mycollab.mobile.ui.DefaultPagedBeanList;
import com.esofthead.mycollab.module.crm.domain.SimpleLead;
import com.esofthead.mycollab.module.crm.domain.criteria.LeadSearchCriteria;
import com.esofthead.mycollab.module.crm.service.LeadService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;

/**
 * @author MyCollab Ltd.
 * @since 4.0
 */
public class LeadListDisplay extends DefaultPagedBeanList<LeadService, LeadSearchCriteria, SimpleLead> {
    private static final long serialVersionUID = -2350731660593521985L;

    public LeadListDisplay() {
        super(ApplicationContextUtil.getSpringBean(LeadService.class), new LeadRowDisplayHandler());
    }

    static public class LeadRowDisplayHandler implements RowDisplayHandler<SimpleLead> {

        @Override
        public Component generateRow(final SimpleLead lead, int rowIndex) {
            final Button b = new Button(lead.getLeadName(), new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent clickEvent) {
                    EventBusFactory.getInstance().post(new LeadEvent.GotoRead(this, lead.getId()));
                }
            });
            b.setWidth("100%");
            return b;
        }

    }
}
