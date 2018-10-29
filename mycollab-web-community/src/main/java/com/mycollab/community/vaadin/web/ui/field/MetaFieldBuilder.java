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
package com.mycollab.community.vaadin.web.ui.field;

import com.mycollab.core.utils.StringUtils;
import com.mycollab.vaadin.ui.ELabel;
import com.vaadin.server.FontIcon;
import com.vaadin.ui.AbstractComponent;

/**
 * @author MyCollab Ltd
 * @since 5.1.4
 */
public class MetaFieldBuilder {
    private String captionHtml;
    private String description = "Edit";

    public MetaFieldBuilder withCaptionAndIcon(FontIcon icon, String caption) {
        captionHtml = icon.getHtml() + " " + StringUtils.trim(caption, 20, true);
        return this;
    }

    public MetaFieldBuilder withCaption(String caption) {
        this.captionHtml = caption;
        return this;
    }

    public MetaFieldBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public AbstractComponent build() {
        return ELabel.html(captionHtml).withDescription(description).withStyleName("block-popupedit").withUndefinedWidth();
    }
}
