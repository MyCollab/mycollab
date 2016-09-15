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
package com.mycollab.mobile.module.crm.view.contact;

import com.mycollab.mobile.module.crm.ui.AbstractRelatedItemSelectionView;
import com.mycollab.mobile.module.crm.view.opportunity.OpportunityListDisplay;
import com.mycollab.mobile.ui.AbstractPagedBeanList;
import com.mycollab.mobile.ui.AbstractRelatedListView;
import com.mycollab.module.crm.domain.SimpleOpportunity;
import com.mycollab.module.crm.domain.criteria.OpportunitySearchCriteria;
import com.mycollab.module.crm.i18n.OpportunityI18nEnum;
import com.mycollab.vaadin.UserUIContext;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;

/**
 * @author MyCollab Inc.
 * @since 4.3.1
 */
public class ContactOpportunitySelectionView extends AbstractRelatedItemSelectionView<SimpleOpportunity, OpportunitySearchCriteria> {
    private static final long serialVersionUID = -238551162632570679L;

    public ContactOpportunitySelectionView(AbstractRelatedListView<SimpleOpportunity, OpportunitySearchCriteria> relatedListView) {
        super(UserUIContext.getMessage(OpportunityI18nEnum.M_TITLE_SELECT_OPPORTUNITIES), relatedListView);
    }

    @Override
    protected void initUI() {
        this.itemList = new OpportunityListDisplay();
        this.itemList.setRowDisplayHandler(new AbstractPagedBeanList.RowDisplayHandler<SimpleOpportunity>() {

            @Override
            public Component generateRow(final SimpleOpportunity obj,
                                         int rowIndex) {
                final SelectableButton b = new SelectableButton(obj.getOpportunityname());
                if (selections.contains(obj)) b.select();
                b.addClickListener(new Button.ClickListener() {

                    private static final long serialVersionUID = 2458940518722524446L;

                    @Override
                    public void buttonClick(ClickEvent event) {
                        if (b.isSelected())
                            selections.add(obj);
                        else
                            selections.remove(obj);
                    }

                });
                return b;
            }
        });
    }

}
