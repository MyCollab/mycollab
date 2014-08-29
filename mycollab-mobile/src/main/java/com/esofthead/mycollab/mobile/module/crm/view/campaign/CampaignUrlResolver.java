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
package com.esofthead.mycollab.mobile.module.crm.view.campaign;

import com.esofthead.mycollab.common.UrlEncodeDecoder;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.mobile.module.crm.CrmUrlResolver;
import com.esofthead.mycollab.mobile.module.crm.events.CampaignEvent;
import com.esofthead.mycollab.module.crm.domain.Account;

/**
 * @author MyCollab Ltd.
 *
 * @since 4.4.0
 *
 */
public class CampaignUrlResolver extends CrmUrlResolver {
	public CampaignUrlResolver() {
		this.addSubResolver("list", new CampaignListUrlResolver());
		this.addSubResolver("add", new CampaignAddUrlResolver());
		this.addSubResolver("edit", new CampaignEditUrlResolver());
		this.addSubResolver("preview", new CampaignPreviewUrlResolver());
	}

	public static class CampaignListUrlResolver extends CrmUrlResolver {
		@Override
		protected void handlePage(String... params) {
			EventBusFactory.getInstance().post(
					new CampaignEvent.GotoList(this, null));
		}
	}

	public static class CampaignAddUrlResolver extends CrmUrlResolver {
		@Override
		protected void handlePage(String... params) {
			EventBusFactory.getInstance().post(
					new CampaignEvent.GotoAdd(this, new Account()));
		}
	}

	public static class CampaignEditUrlResolver extends CrmUrlResolver {
		@Override
		protected void handlePage(String... params) {
			String decodeUrl = UrlEncodeDecoder.decode(params[0]);
			int accountId = Integer.parseInt(decodeUrl);
			EventBusFactory.getInstance().post(
					new CampaignEvent.GotoEdit(this, accountId));
		}
	}

	public static class CampaignPreviewUrlResolver extends CrmUrlResolver {
		@Override
		protected void handlePage(String... params) {
			String decodeUrl = UrlEncodeDecoder.decode(params[0]);
			int accountId = Integer.parseInt(decodeUrl);
			EventBusFactory.getInstance().post(
					new CampaignEvent.GotoRead(this, accountId));
		}
	}
}
