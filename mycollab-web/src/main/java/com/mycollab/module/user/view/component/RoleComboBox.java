/**
 * Copyright © MyCollab
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.user.view.component;

import com.mycollab.db.arguments.BasicSearchRequest;
import com.mycollab.module.user.accountsettings.localization.RoleI18nEnum;
import com.mycollab.module.user.domain.SimpleRole;
import com.mycollab.module.user.domain.criteria.RoleSearchCriteria;
import com.mycollab.module.user.service.RoleService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.UserUIContext;
import com.vaadin.data.Converter;
import com.vaadin.data.Result;
import com.vaadin.data.ValueContext;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.ItemCaptionGenerator;

import java.util.List;
import java.util.Optional;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class RoleComboBox extends ComboBox implements Converter<SimpleRole, Integer> {
    private static final long serialVersionUID = 1L;

    private List<SimpleRole> roles;

    public RoleComboBox() {
        RoleSearchCriteria criteria = new RoleSearchCriteria();

        RoleService roleService = AppContextUtil.getSpringBean(RoleService.class);
        roles = (List<SimpleRole>) roleService.findPageableListByCriteria(new BasicSearchRequest<>(criteria));

        SimpleRole ownerRole = new SimpleRole();
        ownerRole.setId(-1);
        ownerRole.setRolename(UserUIContext.getMessage(RoleI18nEnum.OPT_ACCOUNT_OWNER));
        roles.add(ownerRole);

        setItems(roles);
        setItemCaptionGenerator((ItemCaptionGenerator<SimpleRole>) role -> role.getRolename());

        roles.forEach(role -> {
            if (Boolean.TRUE.equals(role.getIsdefault())) {
                this.setValue(role);
                return;
            }
        });
    }

    @Override
    public Result<Integer> convertToModel(SimpleRole simpleRole, ValueContext valueContext) {
        return Result.ok(simpleRole.getId());
    }

    @Override
    public SimpleRole convertToPresentation(Integer id, ValueContext valueContext) {
        Optional<SimpleRole> result = roles.stream().filter(role -> role.getId() == id).findFirst();
        return result.get();
    }
}
