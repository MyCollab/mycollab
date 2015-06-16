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
package com.esofthead.mycollab.common.service.ibatis;

import com.esofthead.mycollab.common.dao.CustomViewStoreMapper;
import com.esofthead.mycollab.common.domain.CustomViewStore;
import com.esofthead.mycollab.common.domain.CustomViewStoreExample;
import com.esofthead.mycollab.common.domain.NullCustomViewStore;
import com.esofthead.mycollab.common.service.CustomViewStoreService;
import com.esofthead.mycollab.core.persistence.ICrudGenericDAO;
import com.esofthead.mycollab.core.persistence.service.DefaultCrudService;
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
public class CustomViewStoreServiceImpl extends
        DefaultCrudService<Integer, CustomViewStore> implements
        CustomViewStoreService {
    @Autowired
    private CustomViewStoreMapper customViewStoreMapper;

    @Override
    public ICrudGenericDAO<Integer, CustomViewStore> getCrudMapper() {
        return customViewStoreMapper;
    }

    @Override
    public CustomViewStore getViewLayoutDef(Integer accountId, String username,
                                            String viewId) {
        CustomViewStoreExample ex = new CustomViewStoreExample();
        ex.createCriteria().andCreateduserEqualTo(username)
                .andViewidEqualTo(viewId).andSaccountidEqualTo(accountId);
        List<CustomViewStore> views = customViewStoreMapper
                .selectByExampleWithBLOBs(ex);
        if (CollectionUtils.isNotEmpty(views)) {
            return views.get(0);
        }
        return new NullCustomViewStore();
    }

    @Override
    public void saveOrUpdateViewLayoutDef(CustomViewStore viewStore) {
        CustomViewStore viewLayoutDef = getViewLayoutDef(
                viewStore.getSaccountid(), viewStore.getCreateduser(),
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
