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
package com.mycollab.mobile.module.crm.view.lead;

import com.mycollab.eventmanager.EventBusFactory;
import com.mycollab.mobile.module.crm.events.LeadEvent;
import com.mycollab.mobile.ui.DefaultPagedBeanList;
import com.mycollab.module.crm.domain.SimpleLead;
import com.mycollab.module.crm.domain.criteria.LeadSearchCriteria;
import com.mycollab.module.crm.service.LeadService;
import com.mycollab.spring.AppContextUtil;
import com.vaadin.ui.Component;
import org.vaadin.viritin.button.MButton;

/**
 * @author MyCollab Ltd.
 * @since 4.0
 */
public class LeadListDisplay extends DefaultPagedBeanList<LeadService, LeadSearchCriteria, SimpleLead> {
    private static final long serialVersionUID = -2350731660593521985L;

    public LeadListDisplay() {
        super(AppContextUtil.getSpringBean(LeadService.class), new LeadRowDisplayHandler());
    }

    private static class LeadRowDisplayHandler implements RowDisplayHandler<SimpleLead> {

        @Override
        public Component generateRow(final SimpleLead lead, int rowIndex) {
            return new MButton(lead.getLeadName(), clickEvent -> EventBusFactory.getInstance().post(new LeadEvent.GotoRead(this,
                    lead.getId()))).withFullWidth();
        }
    }
}
