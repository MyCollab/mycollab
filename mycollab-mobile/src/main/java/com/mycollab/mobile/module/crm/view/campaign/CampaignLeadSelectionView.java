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
/**
 *
 */
package com.mycollab.mobile.module.crm.view.campaign;

import com.mycollab.mobile.module.crm.ui.AbstractRelatedItemSelectionView;
import com.mycollab.mobile.module.crm.view.lead.LeadListDisplay;
import com.mycollab.mobile.ui.AbstractRelatedListView;
import com.mycollab.module.crm.domain.SimpleLead;
import com.mycollab.module.crm.domain.criteria.LeadSearchCriteria;
import com.mycollab.module.crm.i18n.LeadI18nEnum;
import com.mycollab.vaadin.UserUIContext;

/**
 * @author MyCollab Inc.
 * @since 4.3.1
 */
public class CampaignLeadSelectionView extends AbstractRelatedItemSelectionView<SimpleLead, LeadSearchCriteria> {
    private static final long serialVersionUID = -7266079544811933378L;

    public CampaignLeadSelectionView(AbstractRelatedListView<SimpleLead, LeadSearchCriteria> relatedListView) {
        super(UserUIContext.getMessage(LeadI18nEnum.M_TITLE_SELECT_LEADS), relatedListView);
    }

    @Override
    protected void initUI() {
        this.itemList = new LeadListDisplay();
        this.itemList.setRowDisplayHandler((lead, rowIndex) -> {
            final SelectableButton b = new SelectableButton(lead.getLeadName());
            if (selections.contains(lead)) b.select();
            b.addClickListener(clickEvent -> {
                if (b.isSelected())
                    selections.add(lead);
                else
                    selections.remove(lead);
            });
            return b;
        });
    }

}
