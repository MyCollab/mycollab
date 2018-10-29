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
package com.mycollab.vaadin.ui;

import com.google.common.base.MoreObjects;
import com.hp.gagawa.java.elements.A;
import com.mycollab.core.utils.StringUtils;
import com.mycollab.vaadin.UserUIContext;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.ValoTheme;

import java.util.Date;

/**
 * @author MyCollab Ltd.
 * @since 5.0.3
 */
public class ELabel extends Label {

    public ELabel() {
        super();
    }

    public ELabel(String content) {
        super(content);
    }

    public ELabel(ContentMode mode) {
        this("", mode);
    }

    public ELabel(String content, ContentMode mode) {
        super(content, mode);
    }

    public ELabel withDescription(String description) {
        this.setDescription(description);
        return this;
    }

    public ELabel withUndefinedWidth() {
        return withWidth("-1px");
    }

    public ELabel withUndefinedHeight() {
        return withHeight("-1px");
    }

    public ELabel withWidth(String width) {
        this.setWidth(width);
        return this;
    }

    public ELabel withFullWidth() {
        this.setWidth("100%");
        return this;
    }

    public ELabel withHeight(String width) {
        this.setHeight(width);
        return this;
    }

    public ELabel withStyleName(String... styleNames) {
        for (String styleName : styleNames) {
            this.addStyleName(styleName);
        }
        return this;
    }

    public ELabel prettyDate(Date date) {
        this.setValue(UserUIContext.formatPrettyTime(date));
        this.setDescription(UserUIContext.formatDate(date));
        return this;
    }

    public ELabel prettyDateTime(Date date) {
        this.setValue(UserUIContext.formatPrettyTime(date));
        this.setDescription(UserUIContext.formatDateTime(date));
        return this;
    }

    public static ELabel html(String value) {
        return new ELabel(value, ContentMode.HTML);
    }

    public static ELabel html(String value, String link) {
        return new ELabel(new A(link).appendText(value).write(), ContentMode.HTML);
    }

    public static ELabel email(String email) {
        return new ELabel(new A("mailto:" + email).appendText(MoreObjects.firstNonNull(email, "")).write(), ContentMode.HTML);
    }

    public static ELabel richText(String value) {
        return ELabel.html(StringUtils.formatRichText(value)).withStyleName(UIConstants.LABEL_WORD_WRAP).withFullWidth();
    }

    public static ELabel h1(String value) {
        return ELabel.html(value).withStyleName(ValoTheme.LABEL_H1, ValoTheme.LABEL_NO_MARGIN);
    }

    public static ELabel h2(String value) {
        return ELabel.html(value).withStyleName(ValoTheme.LABEL_H2, ValoTheme.LABEL_NO_MARGIN);
    }

    public static ELabel h3(String value) {
        return ELabel.html(value).withStyleName(ValoTheme.LABEL_H3, ValoTheme.LABEL_NO_MARGIN);
    }

    public static ELabel fontIcon(VaadinIcons icon) {
        return ELabel.html(icon.getHtml()).withUndefinedWidth();
    }

    public static ELabel EMPTY_SPACE() {
        return ELabel.html("&nbsp;").withUndefinedWidth();
    }

    public static ELabel hr() {
        return ELabel.html("<hr/>").withStyleName("hr");
    }

    public static ELabel i18n(String value, Class<? extends Enum> enumType) {
        if (StringUtils.isBlank(value)) {
            return new ELabel("");
        } else {
            try {
                Enum anEnum = Enum.valueOf(enumType, value);
                return new ELabel(UserUIContext.getMessage(anEnum));
            } catch (Exception e) {
                return new ELabel(value);
            }
        }
    }
}
