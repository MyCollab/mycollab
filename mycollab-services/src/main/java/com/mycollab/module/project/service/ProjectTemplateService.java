package com.mycollab.module.project.service;

import com.mycollab.db.persistence.service.IService;

/**
 * @author MyCollab Ltd
 * @since 5.2.6
 */
public interface ProjectTemplateService extends IService {
    Integer cloneProject(Integer projectId, String newPrjName, String newPrjKey, Integer sAccountId, String username);
}
