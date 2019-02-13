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
package com.mycollab.vaadin.ui.field;

import com.hp.gagawa.java.elements.A;
import com.mycollab.core.utils.StringUtils;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;

/**
 * @author MyCollab Ltd.
 * @since 4.5.3
 */
public class UrlLinkViewField extends CustomField<String> {
    private static final long serialVersionUID = 1L;

    private String url;
    private String caption;

    public UrlLinkViewField(String url) {
        this(url, url);
    }

    public UrlLinkViewField(String url, String caption) {
        this.url = url;
        this.caption = caption;
    }

    @Override
    protected Component initContent() {
        if (StringUtils.isBlank(url) || StringUtils.isBlank(caption)) {
            return ELabel.html("&nbsp;");
        } else {
            final A link = new A(url).appendText(caption).setTarget("_blank");
            return ELabel.html(link.write()).withStyleName(WebThemes.TEXT_ELLIPSIS);
        }
    }

    @Override
    protected void doSetValue(String s) {

    }

    @Override
    public String getValue() {
        return null;
    }
}
