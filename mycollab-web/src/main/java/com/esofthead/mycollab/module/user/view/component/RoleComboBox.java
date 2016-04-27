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

package com.esofthead.mycollab.module.user.view.component;

import com.esofthead.mycollab.core.arguments.BasicSearchRequest;
import com.esofthead.mycollab.module.user.domain.SimpleRole;
import com.esofthead.mycollab.module.user.domain.criteria.RoleSearchCriteria;
import com.esofthead.mycollab.module.user.service.RoleService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.ui.ComboBox;

import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class RoleComboBox extends ComboBox {
    private static final long serialVersionUID = 1L;

    public RoleComboBox() {
        super();
        this.setNullSelectionAllowed(false);
        this.setItemCaptionMode(ItemCaptionMode.PROPERTY);

        RoleSearchCriteria criteria = new RoleSearchCriteria();

        RoleService roleService = ApplicationContextUtil.getSpringBean(RoleService.class);
        List<SimpleRole> roleList = roleService.findPagableListByCriteria(new BasicSearchRequest<>(criteria, 0, Integer.MAX_VALUE));

        BeanContainer<String, SimpleRole> beanItem = new BeanContainer<>(SimpleRole.class);
        beanItem.setBeanIdProperty("id");

        SimpleRole ownerRole = new SimpleRole();
        ownerRole.setId(-1);
        ownerRole.setRolename("Account Owner");
        beanItem.addBean(ownerRole);

        for (SimpleRole role : roleList) {
            beanItem.addBean(role);
        }

        this.setContainerDataSource(beanItem);
        this.setItemCaptionPropertyId("rolename");
        if (roleList.size() > 0) {
            SimpleRole role = roleList.get(0);
            this.setValue(role.getId());
        } else {
            this.setValue(-1);
        }
    }

}
