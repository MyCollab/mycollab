/**
 * This file is part of mycollab-ui.
 *
 * mycollab-ui is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-ui is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-ui.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.vaadin;

import com.esofthead.mycollab.configuration.SiteConfiguration;
import com.esofthead.mycollab.configuration.StorageFactory;
import com.esofthead.mycollab.core.MyCollabVersion;
import com.esofthead.mycollab.module.user.domain.BillingAccount;
import com.esofthead.mycollab.module.user.service.BillingAccountService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.utils.TooltipHelper;
import com.vaadin.server.BootstrapFragmentResponse;
import com.vaadin.server.BootstrapListener;
import com.vaadin.server.BootstrapPageResponse;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;

import static com.esofthead.mycollab.utils.TooltipHelper.TOOLTIP_ID;

/**
 * @author MyCollab Ltd.
 * @since 3.0
 */
public class MyCollabBootstrapListener implements BootstrapListener {
    private static final long serialVersionUID = 1L;

    @Override
    public void modifyBootstrapFragment(BootstrapFragmentResponse response) {
    }

    @Override
    public void modifyBootstrapPage(BootstrapPageResponse response) {
        String domain = Utils.getSubDomain(response.getRequest());
        BillingAccountService billingService = ApplicationContextUtil.getSpringBean(BillingAccountService.class);

        BillingAccount account = billingService.getAccountByDomain(domain);
        if (account != null) {
            String favIconPath = StorageFactory.getInstance().getFavIconPath(account.getId(), account.getFaviconpath());
            response.getDocument().head().getElementsByAttributeValue("rel", "shortcut icon").attr("href", favIconPath);
            response.getDocument().head().getElementsByAttributeValue("rel", "icon").attr("href", favIconPath);
        }

        response.getDocument().head().append("<meta name=\"robots\" content=\"nofollow\" />");

        response.getDocument().head()
                .append(String.format("<script type=\"text/javascript\" src=\"%sjs/jquery-2.1.4.min.js\"></script>",
                        SiteConfiguration.getCdnUrl()));
        response.getDocument().head()
                .append(String.format("<script type=\"text/javascript\" src=\"%sjs/stickytooltip-1.0.1.js?v=%s\"></script>",
                        SiteConfiguration.getCdnUrl(), MyCollabVersion.getVersion()));

        Element div1 = response.getDocument().body().appendElement("div");
        div1.attr("id", "div1" + TOOLTIP_ID);
        div1.attr("class", "stickytooltip");

        Element div12 = div1.appendElement("div");
        div12.attr("style", "padding:5px");

        Element div13 = div12.appendElement("div");
        div13.attr("id", "div13" + TOOLTIP_ID);
        div13.attr("class", "atip");
        div13.attr("style", "width:550px");

        Element div14 = div13.appendElement("div");
        div14.attr("id", "div14" + TOOLTIP_ID);
    }
}