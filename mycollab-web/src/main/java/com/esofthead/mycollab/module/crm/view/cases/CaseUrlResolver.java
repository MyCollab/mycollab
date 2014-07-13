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
package com.esofthead.mycollab.module.crm.view.cases;

import com.esofthead.mycollab.common.UrlEncodeDecoder;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.crm.domain.Account;
import com.esofthead.mycollab.module.crm.events.CaseEvent;
import com.esofthead.mycollab.module.crm.view.CrmUrlResolver;

public class CaseUrlResolver extends CrmUrlResolver {
	public CaseUrlResolver() {
		this.addSubResolver("list", new CaseListUrlResolver());
		this.addSubResolver("add", new CaseAddUrlResolver());
		this.addSubResolver("edit", new CaseEditUrlResolver());
		this.addSubResolver("preview", new CasePreviewUrlResolver());
	}

	public static class CaseListUrlResolver extends CrmUrlResolver {
		@Override
		protected void handlePage(String... params) {
			EventBusFactory.getInstance()
					.post(new CaseEvent.GotoList(this, null));
		}
	}

	public static class CaseAddUrlResolver extends CrmUrlResolver {
		@Override
		protected void handlePage(String... params) {
			EventBusFactory.getInstance().post(
					new CaseEvent.GotoAdd(this, new Account()));
		}
	}

	public static class CaseEditUrlResolver extends CrmUrlResolver {
		@Override
		protected void handlePage(String... params) {
			String decodeUrl = UrlEncodeDecoder.decode(params[0]);
			int caseId = Integer.parseInt(decodeUrl);
			EventBusFactory.getInstance().post(
					new CaseEvent.GotoEdit(this, caseId));
		}
	}

	public static class CasePreviewUrlResolver extends CrmUrlResolver {
		@Override
		protected void handlePage(String... params) {
			String decodeUrl = UrlEncodeDecoder.decode(params[0]);
			int caseId = Integer.parseInt(decodeUrl);
			EventBusFactory.getInstance().post(
					new CaseEvent.GotoRead(this, caseId));
		}
	}
}
