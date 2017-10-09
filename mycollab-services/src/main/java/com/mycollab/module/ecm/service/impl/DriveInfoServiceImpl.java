package com.mycollab.module.ecm.service.impl;

import com.mycollab.core.cache.CacheKey;
import com.mycollab.core.utils.BeanUtility;
import com.mycollab.db.persistence.ICrudGenericDAO;
import com.mycollab.db.persistence.service.DefaultCrudService;
import com.mycollab.concurrent.DistributionLockUtil;
import com.mycollab.module.ecm.dao.DriveInfoMapper;
import com.mycollab.module.ecm.domain.DriveInfo;
import com.mycollab.module.ecm.domain.DriveInfoExample;
import com.mycollab.module.ecm.service.DriveInfoService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

@Service
public class DriveInfoServiceImpl extends DefaultCrudService<Integer, DriveInfo> implements DriveInfoService {
    private static Logger LOG = LoggerFactory.getLogger(DriveInfoServiceImpl.class);

    @Autowired
    private DriveInfoMapper driveInfoMapper;

    @SuppressWarnings("unchecked")
    @Override
    public ICrudGenericDAO<Integer, DriveInfo> getCrudMapper() {
        return driveInfoMapper;
    }

    @Override
    public void saveOrUpdateDriveInfo(@CacheKey DriveInfo driveInfo) {
        Integer sAccountId = driveInfo.getSaccountid();
        DriveInfoExample ex = new DriveInfoExample();
        ex.createCriteria().andSaccountidEqualTo(sAccountId);
        Lock lock = DistributionLockUtil.INSTANCE.getLock("ecm-service" + sAccountId);
        try {
            if (lock.tryLock(15, TimeUnit.SECONDS)) {
                if (driveInfoMapper.countByExample(ex) > 0) {
                    driveInfo.setId(null);
                    driveInfoMapper.updateByExampleSelective(driveInfo, ex);
                } else {
                    driveInfoMapper.insert(driveInfo);
                }
            }
        } catch (Exception e) {
            LOG.error("Error while save drive info " + BeanUtility.printBeanObj(driveInfo), e);
        } finally {
            DistributionLockUtil.INSTANCE.removeLock("ecm-service" + sAccountId);
            lock.unlock();
        }
    }

    @Override
    public DriveInfo getDriveInfo(@CacheKey Integer sAccountId) {
        DriveInfoExample ex = new DriveInfoExample();
        ex.createCriteria().andSaccountidEqualTo(sAccountId);
        List<DriveInfo> driveInfos = driveInfoMapper.selectByExample(ex);
        if (CollectionUtils.isNotEmpty(driveInfos)) {
            return driveInfos.get(0);
        } else {
            DriveInfo driveInfo = new DriveInfo();
            driveInfo.setUsedvolume(0L);
            driveInfo.setSaccountid(sAccountId);
            return driveInfo;
        }
    }

    @Override
    public Long getUsedStorageVolume(@CacheKey Integer sAccountId) {
        DriveInfo driveInfo = getDriveInfo(sAccountId);
        return (driveInfo.getUsedvolume() == null) ? Long.valueOf(0L) : driveInfo.getUsedvolume();
    }
}
