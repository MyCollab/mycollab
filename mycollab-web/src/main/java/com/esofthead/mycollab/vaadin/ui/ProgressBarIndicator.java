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

import com.esofthead.mycollab.web.CustomLayoutExt;
import com.vaadin.ui.Label;
import com.vaadin.ui.ProgressBar;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
public class ProgressBarIndicator extends CustomLayoutExt {
	private static final long serialVersionUID = 1L;
	private final ProgressBar progressIndicator;
	private final Label progressStatusLabel;

	public ProgressBarIndicator(final int total, final int remaining) {
		this(total, remaining, true);
	}

	public ProgressBarIndicator(final int total, final int remaining,
			final Boolean displayPercentage) {
		super("progressBar");

		float value = (total != 0) ? ((float)(total - remaining) / total) : 1;

		this.progressIndicator = new ProgressBar(new Float(value));
		this.progressIndicator.setWidth("100%");
		if (displayPercentage) {
			if (total > 0) {
				this.progressStatusLabel = new Label(String.format("%.0f",
						value * 100) + "%");
			} else {
				this.progressStatusLabel = new Label("100%");
			}
		} else {
			this.progressStatusLabel = new Label(String.valueOf(total
					- remaining)
					+ "/" + String.valueOf(total));
		}
		this.progressStatusLabel.setWidth("100%");
		this.progressStatusLabel.setHeight("100%");
		this.addComponent(this.progressIndicator, "progressbar-container");
		this.addComponent(this.progressStatusLabel, "progressbar-status");
	}
}
