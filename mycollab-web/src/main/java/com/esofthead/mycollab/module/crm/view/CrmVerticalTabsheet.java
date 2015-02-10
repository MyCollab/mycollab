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
package com.esofthead.mycollab.module.crm.view;

import com.esofthead.mycollab.module.crm.ui.CrmAssetsManager;
import com.esofthead.mycollab.vaadin.ui.VerticalTabsheet;
import com.vaadin.server.Resource;
import com.vaadin.ui.Component;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.0
 * 
 */
public class CrmVerticalTabsheet extends VerticalTabsheet {
	private static final long serialVersionUID = 1L;

	public CrmVerticalTabsheet(boolean isLeft) {
		super(isLeft);
	}

	@Override
	protected void setDefaulButtonIcon(Component btn, Boolean selected) {
        ButtonTabImpl btnTabImpl = (ButtonTabImpl) btn;
        String tabId = btnTabImpl.getTabId();

        Resource resource = CrmAssetsManager.getAsset(tabId);
        btn.setIcon(resource);

	}
}
