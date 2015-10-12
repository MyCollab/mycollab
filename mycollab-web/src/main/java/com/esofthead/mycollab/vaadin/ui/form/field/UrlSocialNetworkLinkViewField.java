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
package com.esofthead.mycollab.vaadin.ui.form.field;

import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.vaadin.server.ExternalResource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import org.apache.commons.lang3.StringUtils;

/**
 * @author MyCollab Ltd.
 * @since 4.5.3
 */
public class UrlSocialNetworkLinkViewField extends CustomField<String> {
    private static final long serialVersionUID = 1L;

    private String caption;
    private String linkAccount;

    public UrlSocialNetworkLinkViewField(String caption, String linkAccount) {
        this.caption = caption;
        this.linkAccount = linkAccount;
    }

    @Override
    public Class<String> getType() {
        return String.class;
    }

    @Override
    protected Component initContent() {
        if (StringUtils.isBlank(caption)) {
            Label lbl = new Label("&nbsp;");
            lbl.setContentMode(ContentMode.HTML);
            lbl.setWidth("100%");
            return lbl;
        } else {
            linkAccount = (linkAccount == null) ? "" : linkAccount;
            final Link link = new Link();
            link.setResource(new ExternalResource(linkAccount));
            link.setCaption(caption);
            link.setTargetName("_blank");
            link.setWidth(UIConstants.DEFAULT_CONTROL_WIDTH);
            return link;
        }
    }
}
