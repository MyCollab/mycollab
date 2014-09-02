/**
 * This file is part of mycollab-mobile.
 *
 * mycollab-mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-mobile.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.mobile.module.crm.view.opportunity;

import com.esofthead.mycollab.common.UrlEncodeDecoder;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.mobile.module.crm.CrmModuleScreenData;
import com.esofthead.mycollab.mobile.module.crm.CrmUrlResolver;
import com.esofthead.mycollab.mobile.module.crm.events.CrmEvent;
import com.esofthead.mycollab.mobile.module.crm.events.OpportunityEvent;
import com.esofthead.mycollab.module.crm.domain.Account;
import com.esofthead.mycollab.module.crm.i18n.CrmCommonI18nEnum;
import com.esofthead.mycollab.vaadin.AppContext;

/**
 * @author MyCollab Ltd.
 *
 * @since 4.4.0
 *
 */
public class OpportunityUrlResolver extends CrmUrlResolver {
	public OpportunityUrlResolver() {
		this.addSubResolver("list", new OpportunityListUrlResolver());
		this.addSubResolver("add", new OpportunityAddUrlResolver());
		this.addSubResolver("edit", new OpportunityEditUrlResolver());
		this.addSubResolver("preview", new OpportunityPreviewUrlResolver());
	}

	public static class OpportunityListUrlResolver extends CrmUrlResolver {
		@Override
		protected void handlePage(String... params) {
			EventBusFactory
					.getInstance()
					.post(new CrmEvent.GotoContainer(
							this,
							new CrmModuleScreenData.GotoModule(
									AppContext
											.getMessage(CrmCommonI18nEnum.TOOLBAR_OPPORTUNTIES_HEADER))));
		}
	}

	public static class OpportunityAddUrlResolver extends CrmUrlResolver {
		@Override
		protected void handlePage(String... params) {
			EventBusFactory.getInstance().post(
					new OpportunityEvent.GotoAdd(this, new Account()));
		}
	}

	public static class OpportunityEditUrlResolver extends CrmUrlResolver {
		@Override
		protected void handlePage(String... params) {
			String decodeUrl = UrlEncodeDecoder.decode(params[0]);
			int opportunintyId = Integer.parseInt(decodeUrl);
			EventBusFactory.getInstance().post(
					new OpportunityEvent.GotoEdit(this, opportunintyId));
		}
	}

	public static class OpportunityPreviewUrlResolver extends CrmUrlResolver {
		@Override
		protected void handlePage(String... params) {
			String decodeUrl = UrlEncodeDecoder.decode(params[0]);
			int opportunintyId = Integer.parseInt(decodeUrl);
			EventBusFactory.getInstance().post(
					new OpportunityEvent.GotoRead(this, opportunintyId));
		}
	}
}
