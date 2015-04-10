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

import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import org.apache.commons.lang3.StringUtils;
import org.vaadin.maddon.layouts.MHorizontalLayout;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

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

	private Map<String, GridCellWrapper> fieldCaptionMappings = new HashMap<>();

	public GridFormLayoutHelper() {
		this(0, 0);
	}

	public GridFormLayoutHelper(final int columns, final int rows) {
		this(columns, rows, UIConstants.DEFAULT_CONTROL_WIDTH,
				UIConstants.DEFAULT_CAPTION_FORM_WIDTH, Alignment.TOP_LEFT);
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
		this.layout.setMargin(false);
		this.layout.setSpacing(false);
		this.layout.setRowExpandRatio(0, 0);
	}

    public static GridFormLayoutHelper defaultFormLayoutHelper(int columns, int rows) {
        GridFormLayoutHelper helper = new GridFormLayoutHelper(columns, rows, "100%", "167px", Alignment.TOP_LEFT);
        helper.getLayout().setWidth("100%");
        helper.getLayout().addStyleName("colored-gridlayout");
        return helper;
    }

	public void appendRow() {
		this.layout.setRows(layout.getRows() + 1);
	}

	public int getRows() {
		return this.layout.getRows();
	}

	public void addComponent(final Component field, final String caption,
			final int columns, final int rows, final int colspan,
			final String width) {
		this.addComponent(field, caption, columns, rows, colspan, width,
				this.captionAlignment);
	}

	public void addComponent(final Component field, final String caption,
			final int columns, final int rows) {
		this.addComponent(field, caption, columns, rows, 1,
				this.fieldControlWidth, captionAlignment);
	}

	public void addComponent(final Component field, final String caption,
			final int columns, final int rows, final Alignment alignment) {
		this.addComponent(field, caption, columns, rows, 1,
				this.fieldControlWidth, alignment);
	}

	public void addComponent(final Component field, final String caption,
			final int columns, final int rows, final int colspan,
			final String width, final Alignment alignment) {
		GridCellWrapper cell = buildCell(caption, columns, rows, colspan,
				width, alignment);
		cell.addComponent(field);
	}

	public GridCellWrapper buildCell(final String caption, final int columns,
			final int rows) {
		return buildCell(caption, columns, rows, 1, fieldControlWidth,
				captionAlignment);
	}

	public GridCellWrapper buildCell(final String caption, final int columns,
			final int rows, final int colspan, final String width,
			final Alignment alignment) {
		if (StringUtils.isNotBlank(caption)) {
			final Label l = new Label(caption + ":");
			final MHorizontalLayout captionWrapper = new MHorizontalLayout().withSpacing(false).withMargin(true)
					.withWidth(this.defaultCaptionWidth).withHeight("100%").withStyleName("gridform-caption").with(l)
					.withAlign(l, alignment);
			if (columns == 0) {
				captionWrapper.addStyleName("first-col");
			}
			if (rows == 0) {
				captionWrapper.addStyleName("first-row");
			}
			this.layout.addComponent(captionWrapper, 2 * columns, rows);
		}
		final GridCellWrapper fieldWrapper = new GridCellWrapper();

		if (rows == 0) {
			fieldWrapper.addStyleName("first-row");
			fieldWrapper.setWidth(width);
		}
		this.layout.addComponent(fieldWrapper, 2 * columns + 1, rows,
				2 * (columns + colspan - 1) + 1, rows);
		this.layout.setColumnExpandRatio(2 * columns + 1, 1.0f);

		if (StringUtils.isNotBlank(caption)) {
			fieldCaptionMappings.put(caption, fieldWrapper);
		}
		return fieldWrapper;
	}

	/**
	 * 
	 * @param caption
	 * @return null if it can not find the component wrapper associates with
	 *         <code>caption</code>
	 */
	public GridCellWrapper getComponentWrapper(String caption) {
		return fieldCaptionMappings.get(caption);
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

		if (field instanceof MultiSelectComp) {
			field.setWidth("200px");
		} else {
			field.setWidth(fieldControlWidth);
		}

		this.layout.addComponent(field, 2 * columns + 1, rows);
		this.layout.setColumnExpandRatio(2 * columns + 1, 1.0f);
		return field;
	}

	public GridLayout getLayout() {
		return this.layout;
	}

	public static class GridCellWrapper extends HorizontalLayout {
		private static final long serialVersionUID = 1L;

		public GridCellWrapper() {
			this.setStyleName("gridform-field");
			this.setMargin(true);
			this.setWidth("100%");
		}

		public void addComponent(Component component) {
			if (!(component instanceof Button))
				component.setCaption(null);

			if (component instanceof MultiSelectComp) {
				component.setWidth("200px");
			} else {
				component.setWidth("100%");
			}
			super.addComponent(component);
		}
	}

}
