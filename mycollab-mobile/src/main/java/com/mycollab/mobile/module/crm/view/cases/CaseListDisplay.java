package com.mycollab.mobile.module.crm.view.cases;

import com.hp.gagawa.java.elements.A;
import com.mycollab.mobile.ui.DefaultPagedBeanList;
import com.mycollab.mobile.ui.MobileUIConstants;
import com.mycollab.module.crm.CrmLinkBuilder;
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
            A itemLink = new A(CrmLinkBuilder.generateCasePreviewLinkFull(cases.getId())).appendText(cases.getSubject());
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
