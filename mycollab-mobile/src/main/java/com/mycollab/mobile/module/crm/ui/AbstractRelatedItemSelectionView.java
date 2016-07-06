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
package com.mycollab.mobile.module.crm.ui;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.db.arguments.SearchCriteria;
import com.mycollab.mobile.ui.AbstractMobilePageView;
import com.mycollab.mobile.ui.AbstractPagedBeanList;
import com.mycollab.mobile.ui.AbstractRelatedListView;
import com.mycollab.vaadin.AppContext;
import com.vaadin.ui.Button;

import java.util.HashSet;
import java.util.Set;

/**
 * @author MyCollab Inc.
 * @since 4.3.1
 */
public abstract class AbstractRelatedItemSelectionView<T, S extends SearchCriteria> extends AbstractMobilePageView {
    private static final long serialVersionUID = -8672605824883862622L;

    protected static final String SELECTED_STYLENAME = "selected";

    protected final AbstractRelatedListView<T, S> relatedListView;
    protected Set<T> selections = new HashSet<>();
    protected AbstractPagedBeanList<S, T> itemList;

    public AbstractRelatedItemSelectionView(String title, final AbstractRelatedListView<T, S> relatedListView) {
        this.setCaption(title);
        this.relatedListView = relatedListView;
        initUI();
        this.setContent(itemList);
        Button doneBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_SAVE), clickEvent -> {
            if (!selections.isEmpty()) {
                relatedListView.fireSelectedRelatedItems(selections);
            }
        });
        this.setRightComponent(doneBtn);
    }

    protected abstract void initUI();

    public void setSearchCriteria(S criteria) {
        itemList.search(criteria);
    }

    protected class SelectableButton extends Button {
        private static final long serialVersionUID = 8233451662822791473L;
        private boolean selected = false;

        public SelectableButton(String caption) {
            super(caption);
            addClickListener(new Button.ClickListener() {
                private static final long serialVersionUID = 6187441057387703570L;

                @Override
                public void buttonClick(Button.ClickEvent event) {
                    selected = !selected;
                    if (selected) {
                        addStyleName(SELECTED_STYLENAME);
                    } else {
                        removeStyleName(SELECTED_STYLENAME);
                    }
                }
            });
        }

        public boolean isSelected() {
            return selected;
        }

        public void select() {
            this.selected = true;
            this.addStyleName(SELECTED_STYLENAME);
        }
    }
}
