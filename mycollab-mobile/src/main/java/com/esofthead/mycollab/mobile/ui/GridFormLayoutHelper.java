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
package com.esofthead.mycollab.mobile.ui;

import java.io.Serializable;

import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class GridFormLayoutHelper implements Serializable {

	private static final long serialVersionUID = 1L;
	private final GridLayout layout;

	private final String fieldControlWidth;
	private final String defaultCaptionWidth;
	private final Alignment captionAlignment;

	private static final String DEFAULT_CAPTION_FORM_WIDTH = "120px";
	private static final String DEFAULT_CONTROL_WIDTH = "250px";

	public GridFormLayoutHelper(final int columns, final int rows) {
		this(columns, rows, DEFAULT_CAPTION_FORM_WIDTH);
	}

	public GridFormLayoutHelper(final int columns, final int rows,
			final String defaultCaptionWidth) {
		this(columns, rows, DEFAULT_CONTROL_WIDTH, defaultCaptionWidth,
				Alignment.TOP_RIGHT);
	}

	public GridFormLayoutHelper(final int columns, final int rows,
			final String fieldControlWidth, final String defaultCaptionWidth) {
		this(columns, rows, fieldControlWidth, defaultCaptionWidth,
				Alignment.TOP_RIGHT);
	}

	public GridFormLayoutHelper(final int columns, final int rows,
			final String fieldControlWidth, final String defaultCaptionWidth,
			final Alignment captionAlignment) {
		this.fieldControlWidth = fieldControlWidth;
		this.defaultCaptionWidth = defaultCaptionWidth;
		this.captionAlignment = captionAlignment;

		this.layout = new GridLayout(2 * columns, rows);
		this.layout.setMargin(new MarginInfo(true, false, false, false));
		this.layout.setSpacing(false);

		this.layout.setRowExpandRatio(0, 0);
	}

	public Component addComponent(final boolean condition,
			final Field<?> field, final String caption, final int columns,
			final int rows, final Alignment alignment) {
		if (condition) {
			return this.addComponent(field, caption, columns, rows,
					this.fieldControlWidth, alignment);
		} else {
			return null;
		}
	}

	public Component addComponent(final Boolean condition,
			final Component field, final String caption, final int columns,
			final int rows, final int colspan) {
		if (condition) {
			return this.addComponent(field, caption, columns, rows, colspan);
		} else {
			return null;
		}
	}

	public Component addComponent(final Component field, final String caption,
			final int columns, final int rows) {
		return this.addComponent(field, caption, columns, rows,
				this.fieldControlWidth);
	}

	public Component addComponent(final Component field, final String caption,
			final int columns, final int rows, final Alignment alignment) {
		return this.addComponent(field, caption, columns, rows,
				this.fieldControlWidth, alignment);
	}

	public Component addComponent(final Component field, final String caption,
			final int columns, final int rows, final int colspan) {
		return this.addComponent(field, caption, columns, rows, colspan,
				this.fieldControlWidth, this.captionAlignment);
	}

	public Component addComponent(final Component field, final String caption,
			final int columns, final int rows, final int colspan,
			final int rowspan) {
		return this.addComponent(field, caption, columns, rows, colspan,
				rowspan, this.captionAlignment);
	}

	public Component addComponent(final Component field, final String caption,
			final int columns, final int rows, final int colspan,
			final int rowspan, final Alignment alignment) {
		if (caption != null) {
			final Label l = new Label(caption);
			l.setSizeUndefined();
			this.layout.addComponent(l, 2 * columns, rows);
			this.layout.setComponentAlignment(l, alignment);

			this.layout.addComponent(field, 2 * columns + 1, rows, 2 * (columns
					+ colspan - 1) + 1, rows + rowspan);
			this.layout.setColumnExpandRatio(2 * columns + 1, 1.0f);
			if (!(field instanceof Button))
				field.setCaption(null);
			return field;
		}
		this.layout.addComponent(field, 2 * columns, rows, 2 * (columns
				+ colspan - 1) + 1, rows + rowspan);
		this.layout.setColumnExpandRatio(2 * columns + 1, 1.0f);
		if (!(field instanceof Button))
			field.setCaption(null);

		field.setWidth("100%");

		return field;
	}

	public Component addComponent(final Component field, final String caption,
			final int columns, final int rows, final int colspan,
			final String width) {
		return this.addComponent(field, caption, columns, rows, colspan, width,
				this.captionAlignment);
	}

	public Component addComponent(final Component field, final String caption,
			final int columns, final int rows, final int colspan,
			final String width, final Alignment alignment) {
		if (caption != null) {
			final Label l = new Label(caption);
			final HorizontalLayout captionWrapper = new HorizontalLayout();
			captionWrapper.addComponent(l);
			captionWrapper.setComponentAlignment(l, alignment);
			captionWrapper.setStyleName("gridform-caption");
			captionWrapper.setMargin(true);
			captionWrapper.setWidth(this.defaultCaptionWidth);
			if (columns == 0) {
				captionWrapper.addStyleName("first-col");
			}
			if (rows == 0) {
				captionWrapper.addStyleName("first-row");
			}
			if ((rows + 1) % 2 == 0)
				captionWrapper.addStyleName("even-row");

			this.layout.addComponent(captionWrapper, 2 * columns, rows);
			captionWrapper.setHeight("100%");
		}
		final HorizontalLayout fieldWrapper = new HorizontalLayout();
		fieldWrapper.setStyleName("gridform-field");
		fieldWrapper.setMargin(true);
		fieldWrapper.addComponent(field);

		if (!(field instanceof Button))
			field.setCaption(null);

		field.setWidth(width);

		fieldWrapper.setWidth("100%");
		if (rows == 0) {
			fieldWrapper.addStyleName("first-row");
		}
		if ((rows + 1) % 2 == 0) {
			fieldWrapper.addStyleName("even-row");
		}
		this.layout.addComponent(fieldWrapper, 2 * columns + 1, rows,
				2 * (columns + colspan - 1) + 1, rows);
		this.layout.setColumnExpandRatio(2 * columns + 1, 1.0f);
		return field;
	}

	public Component addComponent(final Component field, final String caption,
			final int columns, final int rows, final int colspan,
			final String width, final String height, final Alignment alignment) {
		final Label l = new Label(caption);
		l.setSizeUndefined();
		this.layout.addComponent(l, 2 * columns, rows);
		this.layout.setComponentAlignment(l, alignment);

		this.layout.addComponent(field, 2 * columns + 1, rows, 2 * (columns
				+ colspan - 1) + 1, rows);
		this.layout.setColumnExpandRatio(2 * columns + 1, 1.0f);
		if (!(field instanceof Button))
			field.setCaption(null);
		field.setWidth(width);
		return field;
	}

	public Component addComponent(final Component field, final String caption,
			final int columns, final int rows, final String width) {
		return this.addComponent(field, caption, columns, rows, width,
				this.captionAlignment);
	}

	public Component addComponent(final Component field, final String caption,
			final int columns, final int rows, final String width,
			final Alignment alignment) {
		if (caption != null) {
			final Label l = new Label(caption);
			// l.setHeight("100%");
			final HorizontalLayout captionWrapper = new HorizontalLayout();
			captionWrapper.addComponent(l);
			captionWrapper.setComponentAlignment(l, alignment);
			captionWrapper.setWidth(this.defaultCaptionWidth);
			captionWrapper.setHeight("100%");
			captionWrapper.setStyleName("gridform-caption");
			captionWrapper.setMargin(true);
			if (columns == 0) {
				captionWrapper.addStyleName("first-col");
			}
			if (rows == 0) {
				captionWrapper.addStyleName("first-row");
			}
			this.layout.addComponent(captionWrapper, 2 * columns, rows);
		}
		final HorizontalLayout fieldWrapper = new HorizontalLayout();
		fieldWrapper.setStyleName("gridform-field");
		if (!(field instanceof Button))
			field.setCaption(null);
		fieldWrapper.addComponent(field);

		field.setWidth(width);

		fieldWrapper.setWidth("100%");
		fieldWrapper.setMargin(true);
		if (rows == 0) {
			fieldWrapper.addStyleName("first-row");
		}
		this.layout.addComponent(fieldWrapper, 2 * columns + 1, rows);
		this.layout.setColumnExpandRatio(2 * columns + 1, 1.0f);
		return field;
	}

	public Component addComponentNoWrapper(final Component field,
			final String caption, final int columns, final int rows) {
		if (caption != null) {
			final Label l = new Label(caption);
			l.setWidth(this.defaultCaptionWidth);
			this.layout.addComponent(l, 2 * columns, rows);
			this.layout.setComponentAlignment(l, this.captionAlignment);
		}
		if (!(field instanceof Button))
			field.setCaption(null);

		field.setWidth(fieldControlWidth);

		this.layout.addComponent(field, 2 * columns + 1, rows);
		this.layout.setColumnExpandRatio(2 * columns + 1, 1.0f);
		return field;
	}

	public Component getComponent(final int column, final int row) {
		return this.layout.getComponent(2 * column + 1, row);
	}

	public GridLayout getLayout() {
		return this.layout;
	}

	public Component addComponent(Component fieldValue, Component fieldCaption,
			String defaultCaptionWidth, String fieldValueWidth, int columns,
			int rows, Alignment alignment) {
		final HorizontalLayout captionWrapper = new HorizontalLayout();
		captionWrapper.addComponent(fieldCaption);
		captionWrapper.setComponentAlignment(fieldCaption, alignment);
		captionWrapper.setWidth(defaultCaptionWidth);
		captionWrapper.setHeight("100%");
		captionWrapper.setMargin(true);
		captionWrapper.setStyleName("gridform-caption");
		if (columns == 0) {
			captionWrapper.addStyleName("first-col");
		}
		if (rows == 0) {
			captionWrapper.addStyleName("first-row");
		}
		this.layout.addComponent(captionWrapper, 2 * columns, rows);
		final HorizontalLayout fieldWrapper = new HorizontalLayout();
		fieldWrapper.setStyleName("gridform-field");
		if (!(fieldValue instanceof Button))
			fieldValue.setCaption(null);
		fieldWrapper.addComponent(fieldValue);

		fieldValue.setWidth(fieldValueWidth);

		fieldWrapper.setWidth("100%");
		fieldWrapper.setMargin(true);
		if (rows == 0) {
			fieldWrapper.addStyleName("first-row");
		}
		this.layout.addComponent(fieldWrapper, 2 * columns + 1, rows);
		this.layout.setColumnExpandRatio(2 * columns + 1, 1.0f);
		return fieldValue;
	}

	public Component addComponentSupportFieldCaption(
			final Component fieldValue, final Component fieldCaption,
			final String defaultCaptionWidth, final String fileValueWidth,
			final int columns, final int rows, final Alignment alignment) {
		return this.addComponent(fieldValue, fieldCaption, defaultCaptionWidth,
				fileValueWidth, columns, rows, alignment);
	}

}
