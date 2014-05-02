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

import com.esofthead.mycollab.vaadin.ui.utils.LabelStringGenerator;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
@SuppressWarnings("serial")
public class LabelHTMLDisplayWidget extends HorizontalLayout {
	private final LabelStringGenerator menuLinkGenerator = new BugDescriptionLinkLabelStringGenerator();
	private final Label lbDes;
	private boolean hasShowLess;
	private final String description;

	private static String pathIconPlus = String.format(
			"<img class='plus-btn' src=\"%s\">",
			MyCollabResource.newResourceLink("icons/16/plus.png"));
	private String pathIconMinus = String.format(
			"<img class='plus-btn' src=\"%s\">",
			MyCollabResource.newResourceLink("icons/16/minus.png"));
	private static int NUM_CUT = 100;

	public LabelHTMLDisplayWidget(String content) {
		description = content;

		String contentLabel = menuLinkGenerator.handleText(content);
		lbDes = new Label(description, ContentMode.HTML);
		if (contentLabel != null && contentLabel.length() > NUM_CUT) {
			hasShowLess = true;

			contentLabel += " " + pathIconPlus;
			lbDes.setValue(contentLabel);
			lbDes.addStyleName(UIConstants.LABEL_CLICKABLE);
		}

		lbDes.setDescription(description);
		this.addComponent(lbDes);
		this.addLayoutClickListener(new LayoutClickListener() {

			@Override
			public void layoutClick(LayoutClickEvent event) {
				if (event.getClickedComponent() instanceof Label) {
					if (description != null && description.length() > NUM_CUT) {
						if (hasShowLess) {
							lbDes.setValue(description + " " + pathIconMinus);
						} else {
							lbDes.setValue(menuLinkGenerator
									.handleText(description)
									+ " "
									+ pathIconPlus);
						}
						lbDes.setContentMode(ContentMode.HTML);
						hasShowLess = !hasShowLess;
					}
				}
			}
		});
	}

	private static class BugDescriptionLinkLabelStringGenerator implements
			LabelStringGenerator {

		@Override
		public String handleText(String value) {
			if (value != null && value.length() > NUM_CUT) {
				return value.substring(0, NUM_CUT) + "...";
			}
			return value;
		}

	}

}
