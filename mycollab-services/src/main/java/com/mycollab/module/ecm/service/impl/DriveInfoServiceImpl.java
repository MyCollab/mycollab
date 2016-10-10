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
package com.mycollab.module.ecm.service.impl;

import com.mycollab.core.cache.CacheKey;
import com.mycollab.core.utils.BeanUtility;
import com.mycollab.db.persistence.ICrudGenericDAO;
import com.mycollab.db.persistence.service.DefaultCrudService;
import com.mycollab.lock.DistributionLockUtil;
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
        Lock lock = DistributionLockUtil.getLock("ecm-service" + sAccountId);
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
            DistributionLockUtil.removeLock("ecm-service" + sAccountId);
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
