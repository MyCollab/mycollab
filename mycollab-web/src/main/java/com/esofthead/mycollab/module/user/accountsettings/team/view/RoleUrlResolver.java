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

import com.esofthead.mycollab.common.UrlEncodeDecoder;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.user.accountsettings.view.AccountUrlResolver;
import com.esofthead.mycollab.module.user.domain.SimpleRole;
import com.esofthead.mycollab.module.user.events.RoleEvent;
import com.esofthead.mycollab.module.user.service.RoleService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class RoleUrlResolver extends AccountUrlResolver {
	public RoleUrlResolver() {
		this.addSubResolver("list", new ListUrlResolver());
		this.addSubResolver("add", new AddUrlResolver());
		this.addSubResolver("edit", new EditUrlResolver());
		this.addSubResolver("preview", new PreviewUrlResolver());
	}

	private class ListUrlResolver extends AccountUrlResolver {
		protected void handlePage(String... params) {
			EventBusFactory.getInstance().post(
					new RoleEvent.GotoList(ListUrlResolver.this, null));
		}
	}

	private class AddUrlResolver extends AccountUrlResolver {
		protected void handlePage(String... params) {
			EventBusFactory.getInstance().post(
					new RoleEvent.GotoAdd(AddUrlResolver.this, null));
		}
	}

	private class EditUrlResolver extends AccountUrlResolver {
		protected void handlePage(String... params) {
			int roleId = Integer.parseInt(UrlEncodeDecoder.decode(params[0]));
			RoleService roleService = ApplicationContextUtil
					.getSpringBean(RoleService.class);
			SimpleRole role = roleService.findById(roleId,
					AppContext.getAccountId());
			EventBusFactory.getInstance().post(
					new RoleEvent.GotoEdit(EditUrlResolver.this, role));
		}
	}

	private class PreviewUrlResolver extends AccountUrlResolver {
		protected void handlePage(String... params) {
			int roleId = Integer.parseInt(UrlEncodeDecoder.decode(params[0]));
			EventBusFactory.getInstance().post(
					new RoleEvent.GotoRead(PreviewUrlResolver.this, roleId));
		}
	}
}
