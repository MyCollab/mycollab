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
package com.esofthead.mycollab.mobile.module.project.view.milestone;

import com.esofthead.mycollab.configuration.StorageFactory;
import com.esofthead.mycollab.core.utils.StringUtils;
import com.esofthead.mycollab.mobile.ui.DefaultPagedBeanList;
import com.esofthead.mycollab.mobile.ui.UIConstants;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectLinkBuilder;
import com.esofthead.mycollab.module.project.ProjectLinkGenerator;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.domain.SimpleMilestone;
import com.esofthead.mycollab.module.project.domain.criteria.MilestoneSearchCriteria;
import com.esofthead.mycollab.module.project.i18n.MilestoneI18nEnum;
import com.esofthead.mycollab.module.project.service.MilestoneService;
import com.esofthead.mycollab.module.project.ui.ProjectAssetsManager;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.ELabel;
import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Div;
import com.hp.gagawa.java.elements.Img;
import com.hp.gagawa.java.elements.Span;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import org.vaadin.viritin.layouts.MCssLayout;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * @author MyCollab Ltd.
 * @since 4.5.2
 */
public class MilestoneListDisplay extends DefaultPagedBeanList<MilestoneService, MilestoneSearchCriteria, SimpleMilestone> {
    private static final long serialVersionUID = 253054104668116456L;

    public MilestoneListDisplay() {
        super(ApplicationContextUtil.getSpringBean(MilestoneService.class), new MilestoneRowDisplayHandler());
    }

    private static class MilestoneRowDisplayHandler implements RowDisplayHandler<SimpleMilestone> {

        @Override
        public Component generateRow(final SimpleMilestone milestone, int rowIndex) {
            MVerticalLayout milestoneInfoLayout = new MVerticalLayout().withSpacing(false).withFullWidth();

            A milestoneLink = new A(ProjectLinkBuilder.generateMilestonePreviewFullLink(CurrentProjectVariables
                    .getProjectId(), milestone.getId())).appendChild(new Span().appendText(milestone.getName()));
            MCssLayout milestoneWrap = new MCssLayout(new ELabel(milestoneLink.write(), ContentMode.HTML));
            milestoneInfoLayout.addComponent(new MHorizontalLayout(new ELabel(ProjectAssetsManager.getAsset
                    (ProjectTypeConstants.MILESTONE).getHtml(), ContentMode.HTML).withWidthUndefined(), milestoneWrap)
                    .expand(milestoneWrap).withFullWidth());

            CssLayout metaLayout = new CssLayout();
            milestoneInfoLayout.addComponent(metaLayout);

            ELabel milestoneDatesInfo = new ELabel().withWidthUndefined();
            milestoneDatesInfo.setValue(AppContext.getMessage(MilestoneI18nEnum.M_LIST_DATE_INFO,
                    AppContext.formatDate(milestone.getStartdate(), " N/A "),
                    AppContext.formatDate(milestone.getEnddate(), " N/A ")));
            milestoneDatesInfo.addStyleName(UIConstants.META_INFO);
            metaLayout.addComponent(milestoneDatesInfo);

            A assigneeLink = new A(ProjectLinkGenerator.generateProjectMemberFullLink(AppContext.getSiteUrl(),
                    CurrentProjectVariables.getProjectId(), milestone.getOwner())).appendText(StringUtils.trim(milestone.getOwnerFullName(), 30, true));
            Div assigneeDiv = new Div().appendChild(new Img("", StorageFactory.getInstance().getAvatarPath(milestone
                    .getOwnerAvatarId(), 16))).appendChild(assigneeLink);

            ELabel assigneeLbl = new ELabel(assigneeDiv.write(), ContentMode.HTML).withStyleName(UIConstants.META_INFO)
                    .withWidthUndefined();
            metaLayout.addComponent(assigneeLbl);

            return milestoneInfoLayout;
        }
    }
}
