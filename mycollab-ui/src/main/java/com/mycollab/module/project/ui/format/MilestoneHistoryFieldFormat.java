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
package com.mycollab.module.project.ui.format;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectLinkBuilder;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.domain.SimpleMilestone;
import com.mycollab.module.project.service.MilestoneService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.ui.formatter.HistoryFieldFormat;
import com.mycollab.vaadin.UserUIContext;
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
        return toString(value, true, UserUIContext.getMessage(GenericI18Enum.FORM_EMPTY));
    }

    @Override
    public String toString(String value, Boolean displayAsHtml, String msgIfBlank) {
        if (StringUtils.isBlank(value)) {
            return msgIfBlank;
        }

        try {
            Integer milestoneId = Integer.parseInt(value);
            MilestoneService milestoneService = AppContextUtil.getSpringBean(MilestoneService.class);
            SimpleMilestone milestone = milestoneService.findById(milestoneId, AppUI.getAccountId());

            if (milestone != null) {
                if (displayAsHtml) {
                    return ProjectLinkBuilder.generateProjectItemHtmlLinkAndTooltip(CurrentProjectVariables.getShortName(),
                            milestone.getProjectid(), milestone.getName(), ProjectTypeConstants.MILESTONE, milestone.getId() + "");
                } else {
                    return milestone.getName();
                }
            }
        } catch (Exception e) {
            LOG.error("Error", e);
        }

        return value;
    }
}
