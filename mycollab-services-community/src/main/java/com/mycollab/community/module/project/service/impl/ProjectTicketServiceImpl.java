package com.mycollab.community.module.project.service.impl;

import com.mycollab.core.MyCollabException;
import com.mycollab.module.project.domain.ProjectTicket;
import com.mycollab.module.project.service.impl.AbstractProjectTicketServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author MyCollab Ltd
 * @since 5.4.2
 */
@Service
public class ProjectTicketServiceImpl extends AbstractProjectTicketServiceImpl {
    @Override
    public void updateAssignmentValue(ProjectTicket assignment, String username) {
        throw new MyCollabException("Not support this operation in the community edition");
    }

    @Override
    public void closeSubAssignmentOfMilestone(Integer milestoneId) {
        throw new MyCollabException("Not support this operation in the community edition");
    }
}
