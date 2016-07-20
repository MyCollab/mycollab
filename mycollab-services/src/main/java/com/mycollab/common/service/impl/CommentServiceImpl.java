/**
 * This file is part of mycollab-services.
 *
 * mycollab-services is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-services is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-services.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.common.service.impl;

import com.mycollab.cache.CleanCacheEvent;
import com.mycollab.common.ActivityStreamConstants;
import com.mycollab.common.ModuleNameConstants;
import com.mycollab.common.MonitorTypeConstants;
import com.mycollab.common.dao.CommentMapper;
import com.mycollab.common.dao.CommentMapperExt;
import com.mycollab.common.domain.ActivityStreamWithBLOBs;
import com.mycollab.common.domain.CommentWithBLOBs;
import com.mycollab.common.domain.RelayEmailNotificationWithBLOBs;
import com.mycollab.common.domain.criteria.CommentSearchCriteria;
import com.mycollab.common.service.ActivityStreamService;
import com.mycollab.common.service.CommentService;
import com.mycollab.common.service.RelayEmailNotificationService;
import com.mycollab.db.persistence.ICrudGenericDAO;
import com.mycollab.db.persistence.ISearchableDAO;
import com.mycollab.db.persistence.service.DefaultService;
import com.mycollab.core.utils.BeanUtility;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.service.MessageService;
import com.google.common.eventbus.AsyncEventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@Service
public class CommentServiceImpl extends DefaultService<Integer, CommentWithBLOBs, CommentSearchCriteria> implements CommentService {
    private static final Logger LOG = LoggerFactory.getLogger(CommentServiceImpl.class);

    @Autowired
    protected CommentMapper commentMapper;

    @Autowired
    protected CommentMapperExt commentMapperExt;

    @Autowired
    private RelayEmailNotificationService relayEmailNotificationService;

    @Autowired
    private ActivityStreamService activityStreamService;

    @Autowired
    private AsyncEventBus asyncEventBus;

    @Override
    public ICrudGenericDAO<Integer, CommentWithBLOBs> getCrudMapper() {
        return commentMapper;
    }

    @Override
    public ISearchableDAO<CommentSearchCriteria> getSearchMapper() {
        return commentMapperExt;
    }

    @Override
    public Integer saveWithSession(CommentWithBLOBs record, String username) {
        Integer saveId = super.saveWithSession(record, username);

        if (ProjectTypeConstants.MESSAGE.equals(record.getType())) {
            asyncEventBus.post(new CleanCacheEvent(record.getSaccountid(), new Class[]{MessageService.class}));
        }

        relayEmailNotificationService.saveWithSession(getRelayEmailNotification(record, username), username);
        activityStreamService.saveWithSession(getActivityStream(record, username), username);
        return saveId;
    }

    private ActivityStreamWithBLOBs getActivityStream(CommentWithBLOBs record, String username) {
        ActivityStreamWithBLOBs activityStream = new ActivityStreamWithBLOBs();
        activityStream.setAction(ActivityStreamConstants.ACTION_COMMENT);
        activityStream.setCreateduser(username);
        activityStream.setSaccountid(record.getSaccountid());
        activityStream.setType(record.getType());
        activityStream.setTypeid(record.getTypeid());
        activityStream.setNamefield(record.getComment());
        activityStream.setExtratypeid(record.getExtratypeid());
        if (record.getType() != null && record.getType().startsWith("Project-")) {
            activityStream.setModule(ModuleNameConstants.PRJ);
        } else if (record.getType() != null && record.getType().startsWith("Crm-")) {
            activityStream.setModule(ModuleNameConstants.CRM);
        } else {
            LOG.error("Can not define module type of bean {}", BeanUtility.printBeanObj(record));
        }
        return activityStream;
    }

    private RelayEmailNotificationWithBLOBs getRelayEmailNotification(CommentWithBLOBs record, String username) {
        RelayEmailNotificationWithBLOBs relayEmailNotification = new RelayEmailNotificationWithBLOBs();
        relayEmailNotification.setSaccountid(record.getSaccountid());
        relayEmailNotification.setAction(MonitorTypeConstants.ADD_COMMENT_ACTION);
        relayEmailNotification.setChangeby(record.getCreateduser());
        relayEmailNotification.setChangecomment(record.getComment());
        relayEmailNotification.setType(record.getType());
        relayEmailNotification.setTypeid(record.getTypeid());
        relayEmailNotification.setExtratypeid(record.getExtratypeid());
        return relayEmailNotification;
    }
}