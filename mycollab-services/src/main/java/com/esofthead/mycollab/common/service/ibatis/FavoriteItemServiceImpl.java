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

import com.esofthead.mycollab.common.dao.FavoriteItemMapper;
import com.esofthead.mycollab.common.domain.FavoriteItem;
import com.esofthead.mycollab.common.domain.FavoriteItemExample;
import com.esofthead.mycollab.common.service.FavoriteItemService;
import com.esofthead.mycollab.core.persistence.ICrudGenericDAO;
import com.esofthead.mycollab.core.persistence.service.DefaultCrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @author MyCollab Ltd.
 * @since 5.0.1
 */
@Service
public class FavoriteItemServiceImpl extends DefaultCrudService<Integer, FavoriteItem> implements FavoriteItemService {
    @Autowired
    private FavoriteItemMapper favoriteItemMapper;

    @Override
    public ICrudGenericDAO<Integer, FavoriteItem> getCrudMapper() {
        return favoriteItemMapper;
    }

    @Override
    public void saveOrDelete(FavoriteItem item) {
        FavoriteItemExample ex = new FavoriteItemExample();
        ex.createCriteria().andTypeEqualTo(item.getType()).andTypeidEqualTo(item.getTypeid()).
                andCreateduserEqualTo(item.getCreateduser());
        int count = favoriteItemMapper.countByExample(ex);
        if (count > 0) {
            favoriteItemMapper.deleteByExample(ex);
        } else {
            Date now = new GregorianCalendar().getTime();
            item.setLastupdatedtime(now);
            item.setCreatedtime(now);
            favoriteItemMapper.insert(item);
        }
    }

    @Override
    public boolean isUserFavorite(String username, String type, String typeId) {
        FavoriteItemExample ex = new FavoriteItemExample();
        ex.createCriteria().andTypeEqualTo(type).andTypeidEqualTo(typeId).andCreateduserEqualTo(username);
        return (favoriteItemMapper.countByExample(ex) > 0);
    }
}
