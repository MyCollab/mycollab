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
package com.esofthead.mycollab.module.project.view.settings.component;

import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchRequest;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectMemberStatusConstants;
import com.esofthead.mycollab.module.project.domain.SimpleProjectMember;
import com.esofthead.mycollab.module.project.domain.criteria.ProjectMemberSearchCriteria;
import com.esofthead.mycollab.module.project.events.ProjectMemberEvent;
import com.esofthead.mycollab.module.project.service.ProjectMemberService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.ui.MultiSelectComp;
import com.esofthead.mycollab.vaadin.ui.UserAvatarControlFactory;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class ProjectMemberMultiSelectComp extends MultiSelectComp<SimpleProjectMember> {
    private static final long serialVersionUID = 1L;

    private static final Logger LOG = LoggerFactory.getLogger(ProjectMemberMultiSelectComp.class);

    public ProjectMemberMultiSelectComp() {
        super("memberFullName", false);
    }

    @Override
    protected List<SimpleProjectMember> createData() {
        ProjectMemberSearchCriteria criteria = new ProjectMemberSearchCriteria();
        criteria.setProjectId(new NumberSearchField(CurrentProjectVariables.getProjectId()));
        criteria.setStatus(new StringSearchField(ProjectMemberStatusConstants.ACTIVE));

        ProjectMemberService projectMemberService = ApplicationContextUtil.getSpringBean(ProjectMemberService.class);
        List<SimpleProjectMember> items = projectMemberService.findPagableListByCriteria(new SearchRequest<>(
                criteria, 0, Integer.MAX_VALUE));
        return items;
    }

    @Override
    protected void requestAddNewComp() {
        EventBusFactory.getInstance().post(new ProjectMemberEvent.GotoInviteMembers(this, null));
    }

    @Override
    public Class<? extends SimpleProjectMember> getType() {
        return SimpleProjectMember.class;
    }


    @Override
    protected ItemSelectionComp<SimpleProjectMember> buildItem(final SimpleProjectMember item) {
        ItemSelectionComp<SimpleProjectMember> buildItem = super.buildItem(item);
        String userAvatarId = "";

        try {
            userAvatarId = (String) PropertyUtils.getProperty(item, "memberAvatarId");
        } catch (Exception e) {
            LOG.error("Error while getting project member avatar", e);
        }

        buildItem.setIcon(UserAvatarControlFactory.createAvatarResource(userAvatarId, 16));
        return buildItem;
    }

}
