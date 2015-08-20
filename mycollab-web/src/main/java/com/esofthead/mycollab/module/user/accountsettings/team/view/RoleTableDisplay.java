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

package com.esofthead.mycollab.module.user.accountsettings.team.view;

import com.esofthead.mycollab.common.GenericLinkUtils;
import com.esofthead.mycollab.common.TableViewField;
import com.esofthead.mycollab.module.user.AccountLinkGenerator;
import com.esofthead.mycollab.module.user.domain.SimpleRole;
import com.esofthead.mycollab.module.user.domain.criteria.RoleSearchCriteria;
import com.esofthead.mycollab.module.user.service.RoleService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.ui.CheckBoxDecor;
import com.esofthead.mycollab.vaadin.ui.LabelLink;
import com.esofthead.mycollab.vaadin.ui.table.DefaultPagedBeanTable;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.ui.Table;

import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class RoleTableDisplay extends DefaultPagedBeanTable<RoleService, RoleSearchCriteria, SimpleRole> {
    private static final long serialVersionUID = 1L;

    public RoleTableDisplay(TableViewField requiredColumn, List<TableViewField> displayColumns) {
        super(ApplicationContextUtil.getSpringBean(RoleService.class),
                SimpleRole.class, requiredColumn, displayColumns);

        this.addGeneratedColumn("selected", new Table.ColumnGenerator() {
            private static final long serialVersionUID = 1L;

            @Override
            public Object generateCell(Table source, Object itemId, Object columnId) {
                final SimpleRole role = getBeanByIndex(itemId);
                CheckBoxDecor cb = new CheckBoxDecor("", role.isSelected());
                cb.setImmediate(true);
                cb.addValueChangeListener(new Property.ValueChangeListener() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void valueChange(ValueChangeEvent event) {
                        RoleTableDisplay.this.fireSelectItemEvent(role);
                    }
                });
                role.setExtraData(cb);
                return cb;
            }
        });

        this.addGeneratedColumn("rolename", new Table.ColumnGenerator() {
            private static final long serialVersionUID = 1L;

            @Override
            public com.vaadin.ui.Component generateCell(Table source,
                                                        final Object itemId, Object columnId) {
                SimpleRole role = getBeanByIndex(itemId);
                return new LabelLink(role.getRolename(), GenericLinkUtils.URL_PREFIX_PARAM
                                + AccountLinkGenerator.generateRoleLink(role.getId()));
            }
        });
    }
}