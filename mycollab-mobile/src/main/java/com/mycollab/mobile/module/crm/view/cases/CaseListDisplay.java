/**
 * Copyright © MyCollab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.mobile.module.crm.view.cases;

import com.hp.gagawa.java.elements.A;
import com.mycollab.mobile.ui.DefaultPagedBeanList;
import com.mycollab.mobile.ui.MobileUIConstants;
import com.mycollab.module.crm.CrmLinkGenerator;
import com.mycollab.module.crm.CrmTypeConstants;
import com.mycollab.module.crm.domain.SimpleCase;
import com.mycollab.module.crm.domain.criteria.CaseSearchCriteria;
import com.mycollab.module.crm.service.CaseService;
import com.mycollab.module.crm.ui.CrmAssetsManager;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.ui.IBeanList;
import com.vaadin.ui.Component;
import org.vaadin.viritin.layouts.MCssLayout;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import static com.mycollab.module.crm.i18n.OptionI18nEnum.CaseStatus.Closed;
import static com.mycollab.module.crm.i18n.OptionI18nEnum.CaseStatus.Rejected;

/**
 * @author MyCollab Ltd.
 * @since 4.0
 */
public class CaseListDisplay extends DefaultPagedBeanList<CaseService, CaseSearchCriteria, SimpleCase> {
    private static final long serialVersionUID = -5865353122197825948L;

    public CaseListDisplay() {
        super(AppContextUtil.getSpringBean(CaseService.class), new CaseRowDisplayHandler());
    }

    private static class CaseRowDisplayHandler implements IBeanList.RowDisplayHandler<SimpleCase> {

        @Override
        public Component generateRow(IBeanList<SimpleCase> host, final SimpleCase cases, int rowIndex) {
            MVerticalLayout rowLayout = new MVerticalLayout().withMargin(false).withSpacing(false).withFullWidth();
            A itemLink = new A(CrmLinkGenerator.generateCasePreviewLink(cases.getId())).appendText(cases.getSubject());
            if (Closed.name().equals(cases.getStatus()) || Rejected.name().equals(cases.getStatus())) {
                itemLink.setCSSClass(MobileUIConstants.LINK_COMPLETED);
            }
            MCssLayout itemWrap = new MCssLayout(ELabel.html(itemLink.write()));
            rowLayout.addComponent(new MHorizontalLayout(ELabel.fontIcon(CrmAssetsManager.getAsset
                    (CrmTypeConstants.CASE)), itemWrap).expand(itemWrap).withFullWidth());
            return rowLayout;
        }
    }
}
