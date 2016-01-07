/**
 * This file is part of mycollab-web.
 *
 * mycollab-web is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-web is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-web.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.module.project.ui.format;

import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectLinkBuilder;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.domain.SimpleMilestone;
import com.esofthead.mycollab.module.project.service.MilestoneService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.HistoryFieldFormat;
import com.hp.gagawa.java.elements.Span;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author MyCollab Ltd.
 * @since 4.0
 */
public final class MilestoneHistoryFieldFormat implements HistoryFieldFormat {
    private static final Logger LOG = LoggerFactory.getLogger(MilestoneHistoryFieldFormat.class);

    @Override
    public String toString(String value) {
        if (StringUtils.isBlank(value)) {
            return new Span().write();
        }

        try {
            int milestoneId = Integer.parseInt(value);
            MilestoneService milestoneService = ApplicationContextUtil.getSpringBean(MilestoneService.class);
            SimpleMilestone milestone = milestoneService.findById(milestoneId, AppContext.getAccountId());

            if (milestone != null) {
                return ProjectLinkBuilder.generateProjectItemHtmlLinkAndTooltip(CurrentProjectVariables.getShortName(),
                        milestone.getProjectid(), milestone.getName(), ProjectTypeConstants.MILESTONE, milestone.getId() + "");
            }
        } catch (Exception e) {
            LOG.error("Error", e);
        }

        return value;
    }

}
