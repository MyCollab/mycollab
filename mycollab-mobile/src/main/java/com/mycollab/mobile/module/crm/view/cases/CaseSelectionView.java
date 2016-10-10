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

import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.mobile.ui.AbstractSelectionView;
import com.mycollab.module.crm.domain.SimpleCase;
import com.mycollab.module.crm.domain.criteria.CaseSearchCriteria;
import com.mycollab.module.crm.i18n.CaseI18nEnum;
import com.mycollab.vaadin.MyCollabUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.IBeanList;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;

/**
 * @author MyCollab Ltd.
 * @since 4.1
 */
public class CaseSelectionView extends AbstractSelectionView<SimpleCase> {
    private static final long serialVersionUID = 2092608350938161913L;

    private CaseListDisplay itemList;

    private CaseRowDisplayHandler rowHandler = new CaseRowDisplayHandler();

    public CaseSelectionView() {
        super();
        createUI();
        this.setCaption(UserUIContext.getMessage(CaseI18nEnum.M_VIEW_CASE_NAME_LOOKUP));
    }

    private void createUI() {
        itemList = new CaseListDisplay();
        itemList.setWidth("100%");
        itemList.setRowDisplayHandler(rowHandler);
        this.setContent(itemList);
    }

    @Override
    public void load() {
        CaseSearchCriteria searchCriteria = new CaseSearchCriteria();
        searchCriteria.setSaccountid(new NumberSearchField(MyCollabUI.getAccountId()));
        itemList.search(searchCriteria);
        SimpleCase clearCase = new SimpleCase();
        itemList.addComponentAtTop(rowHandler.generateRow(itemList, clearCase, 0));
    }

    private class CaseRowDisplayHandler implements IBeanList.RowDisplayHandler<SimpleCase> {

        @Override
        public Component generateRow(IBeanList<SimpleCase> host, final SimpleCase cases, int rowIndex) {
            return new Button(cases.getSubject(), clickEvent -> {
                selectionField.fireValueChange(cases);
                CaseSelectionView.this.getNavigationManager().navigateBack();
            });
        }
    }
}
