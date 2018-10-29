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
package com.mycollab.vaadin.web.ui;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.event.HasSelectableItemHandlers;
import com.mycollab.vaadin.event.HasSelectionOptionHandlers;
import com.mycollab.vaadin.event.SelectionOptionHandler;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Resource;
import com.vaadin.ui.Button;

import java.util.HashSet;
import java.util.Set;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
public class SelectionOptionButton extends SplitButton implements HasSelectionOptionHandlers {
    private static final long serialVersionUID = 1L;
    private boolean isSelectAll = false;
    private boolean isSelected = false;

    private Set<SelectionOptionHandler> handlers;

    private final Button selectAllBtn;
    private final Button selectThisPageBtn;

    public SelectionOptionButton(final HasSelectableItemHandlers selectableItemHandlers) {
        super();
        addStyleName(WebThemes.BUTTON_ACTION);
        addStyleName(WebThemes.BUTTON_SMALL_PADDING);
        setIcon(VaadinIcons.SQUARE_SHADOW);

        addClickListener(clickEvent -> toggleChangeOption());

        final OptionPopupContent selectContent = new OptionPopupContent();

        selectAllBtn = new Button("", clickEvent -> {
            isSelectAll = true;
            setIcon(VaadinIcons.CHECK_SQUARE_O);
            fireSelectAll();
            setPopupVisible(false);
        });
        selectContent.addOption(selectAllBtn);

        selectThisPageBtn = new Button("", clickEvent -> {
            isSelectAll = false;
            setIcon(VaadinIcons.CHECK_SQUARE_O);
            fireSelectCurrentPage();
            setPopupVisible(false);
        });
        selectContent.addOption(selectThisPageBtn);

        addPopupVisibilityListener(event -> {
            if (event.isPopupVisible()) {
                selectAllBtn.setCaption(UserUIContext.getMessage(GenericI18Enum.ACTION_SELECT_ALL_VALUE, selectableItemHandlers.totalItemsCount()));
                selectThisPageBtn.setCaption(UserUIContext.getMessage(GenericI18Enum.ACTION_SELECT_PAGE_VALUE, selectableItemHandlers.currentViewCount()));
            }
        });

        Button deSelectBtn = new Button(UserUIContext.getMessage(GenericI18Enum.ACTION_DESELECT_ALL), clickEvent -> {
            isSelectAll = false;
            setIcon(VaadinIcons.SQUARE_SHADOW);
            fireDeselect();
            setPopupVisible(false);
        });
        selectContent.addOption(deSelectBtn);
        setContent(selectContent);
    }

    @Override
    public void addSelectionOptionHandler(final SelectionOptionHandler handler) {
        if (handlers == null) {
            handlers = new HashSet<>();
        }
        handlers.add(handler);
    }

    private void fireDeselect() {
        if (handlers != null) {
            handlers.forEach(handler -> handler.onDeSelect());
        }
    }

    private void fireSelectAll() {
        if (handlers != null) {
            handlers.forEach(handler -> handler.onSelectAll());
        }
    }

    private void fireSelectCurrentPage() {
        if (handlers != null) {
            handlers.forEach(SelectionOptionHandler::onSelectCurrentPage);
        }
    }

    public void setSelectedCheckbox(final boolean selected) {
        isSelected = selected;
        Resource icon = (selected) ? VaadinIcons.CHECK_SQUARE_O : VaadinIcons.SQUARE_SHADOW;
        this.setIcon(icon);
    }

    private void toggleChangeOption() {
        if (isSelectAll) {
            return;
        }

        isSelected = !isSelected;
        final Resource icon = (isSelected) ? VaadinIcons.CHECK_SQUARE_O : VaadinIcons.SQUARE_SHADOW;
        this.setIcon(icon);

        if (isSelected) {
            fireSelectCurrentPage();
        } else {
            fireDeselect();
        }
    }
}
