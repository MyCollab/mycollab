package com.mycollab.community.module.ecm.service.impl;

import com.mycollab.db.persistence.ICrudGenericDAO;
import com.mycollab.db.persistence.service.DefaultCrudService;
import com.mycollab.module.ecm.dao.ExternalDriveMapper;
import com.mycollab.module.ecm.domain.ExternalDrive;
import com.mycollab.module.ecm.service.ExternalDriveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author MyCollab Ltd
 * @since 1.0.0
 */
@Service
public class ExternalDriveServiceImpl extends DefaultCrudService<Integer, ExternalDrive> implements ExternalDriveService {

    @Autowired
    private ExternalDriveMapper externalDriveMapper;

    @SuppressWarnings("unchecked")
    @Override
    public ICrudGenericDAO<Integer, ExternalDrive> getCrudMapper() {
        return externalDriveMapper;
    }

    @Override
    public List<ExternalDrive> getExternalDrivesOfUser(String username) {
        return new ArrayList<>();
    }
}
