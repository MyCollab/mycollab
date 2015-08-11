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
package com.esofthead.mycollab.vaadin.ui;

import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import org.apache.commons.lang3.StringUtils;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@SuppressWarnings("serial")
public class LabelHTMLDisplayWidget extends HorizontalLayout {
    private static int NUM_CUT = 100;
    private Label lbDes;
    private boolean hasShowLess;
    private String description;


    public LabelHTMLDisplayWidget(String content) {
        description = content;
        if (StringUtils.isBlank(description)) {
            addComponent(new Label("<<No Description>>"));
            return;
        }
        String contentLabel = trimText(content);
        lbDes = new Label(description, ContentMode.HTML);
        if (contentLabel != null && contentLabel.length() > NUM_CUT) {
            hasShowLess = true;

            contentLabel += " " + FontAwesome.PLUS_SQUARE.getHtml();
            lbDes.setValue(contentLabel);
            lbDes.addStyleName(UIConstants.LABEL_CLICKABLE);
        }

        lbDes.setDescription(description);
        addComponent(lbDes);
        addLayoutClickListener(new LayoutClickListener() {

            @Override
            public void layoutClick(LayoutClickEvent event) {
                if (event.getClickedComponent() instanceof Label) {
                    if (description != null && description.length() > NUM_CUT) {
                        if (hasShowLess) {
                            lbDes.setValue(description + " " + FontAwesome.MINUS_SQUARE.getHtml());
                        } else {
                            lbDes.setValue(trimText(description) + " " + FontAwesome.PLUS_SQUARE.getHtml());
                        }
                        lbDes.setContentMode(ContentMode.HTML);
                        hasShowLess = !hasShowLess;
                    }
                }
            }
        });
    }

    private static String trimText(String value) {
        if (value != null && value.length() > NUM_CUT) {
            return value.substring(0, NUM_CUT) + "...";
        }
        return value;
    }
}
