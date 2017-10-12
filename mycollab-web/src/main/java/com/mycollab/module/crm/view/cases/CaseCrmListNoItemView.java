/**
 * Copyright © MyCollab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.crm.view.cases;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.vaadin.EventBusFactory;
import com.mycollab.module.crm.CrmTypeConstants;
import com.mycollab.module.crm.event.CaseEvent;
import com.mycollab.module.crm.i18n.CaseI18nEnum;
import com.mycollab.module.crm.ui.CrmAssetsManager;
import com.mycollab.module.crm.ui.components.CrmListNoItemView;
import com.mycollab.security.RolePermissionCollections;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;

/**
 * @author MyCollab Ltd.
 * @since 4.0
 */
@ViewComponent
public class CaseCrmListNoItemView extends CrmListNoItemView {
    private static final long serialVersionUID = 1L;

    @Override
    protected FontAwesome titleIcon() {
        return CrmAssetsManager.getAsset(CrmTypeConstants.CASE);
    }

    @Override
    protected String titleMessage() {
        return UserUIContext.getMessage(GenericI18Enum.VIEW_NO_ITEM_TITLE);
    }

    @Override
    protected String hintMessage() {
        return UserUIContext.getMessage(GenericI18Enum.VIEW_NO_ITEM_HINT);
    }

    @Override
    protected String actionMessage() {
        return UserUIContext.getMessage(CaseI18nEnum.NEW);
    }

    @Override
    protected Button.ClickListener actionListener() {
        return new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent clickEvent) {
                EventBusFactory.getInstance().post(new CaseEvent.GotoAdd(this, null));
            }
        };
    }

    @Override
    protected boolean hasPermission() {
        return UserUIContext.canWrite(RolePermissionCollections.CRM_CASE);
    }
}
