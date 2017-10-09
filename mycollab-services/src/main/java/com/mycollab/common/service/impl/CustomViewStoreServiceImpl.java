package com.mycollab.common.service.impl;

import com.mycollab.common.dao.CustomViewStoreMapper;
import com.mycollab.common.domain.CustomViewStore;
import com.mycollab.common.domain.CustomViewStoreExample;
import com.mycollab.common.domain.NullCustomViewStore;
import com.mycollab.common.service.CustomViewStoreService;
import com.mycollab.db.persistence.ICrudGenericDAO;
import com.mycollab.db.persistence.service.DefaultCrudService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.GregorianCalendar;
import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
@Service
public class CustomViewStoreServiceImpl extends DefaultCrudService<Integer, CustomViewStore> implements CustomViewStoreService {
    @Autowired
    private CustomViewStoreMapper customViewStoreMapper;

    @Override
    public ICrudGenericDAO<Integer, CustomViewStore> getCrudMapper() {
        return customViewStoreMapper;
    }

    @Override
    public CustomViewStore getViewLayoutDef(Integer accountId, String username, String viewId) {
        CustomViewStoreExample ex = new CustomViewStoreExample();
        ex.createCriteria().andCreateduserEqualTo(username).andViewidEqualTo(viewId).andSaccountidEqualTo(accountId);
        List<CustomViewStore> views = customViewStoreMapper.selectByExampleWithBLOBs(ex);
        if (CollectionUtils.isNotEmpty(views)) {
            return views.get(0);
        }
        return new NullCustomViewStore();
    }

    @Override
    public void saveOrUpdateViewLayoutDef(CustomViewStore viewStore) {
        CustomViewStore viewLayoutDef = getViewLayoutDef(viewStore.getSaccountid(), viewStore.getCreateduser(),
                viewStore.getViewid());
        viewStore.setCreatedtime(new GregorianCalendar().getTime());
        if (!(viewLayoutDef instanceof NullCustomViewStore)) {
            viewStore.setId(viewLayoutDef.getId());
            updateWithSession(viewStore, viewStore.getCreateduser());
        } else {
            saveWithSession(viewStore, viewStore.getCreateduser());
        }
    }
}
