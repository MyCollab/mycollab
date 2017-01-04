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
package com.mycollab.module.project.view.page;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.module.project.i18n.PageI18nEnum;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.AbstractFormLayoutFactory;
import com.mycollab.vaadin.web.ui.grid.GridFormLayoutHelper;
import com.vaadin.ui.*;

/**
 * @author MyCollab Ltd.
 * @since 4.4.0
 */
public class PageFormLayoutFactory extends AbstractFormLayoutFactory {
    private static final long serialVersionUID = 1L;

    private GridFormLayoutHelper informationLayout;

    @Override
    public AbstractComponent getLayout() {
        final VerticalLayout layout = new VerticalLayout();

        informationLayout = GridFormLayoutHelper.defaultFormLayoutHelper(2, 3);
        layout.addComponent(informationLayout.getLayout());
        layout.setComponentAlignment(informationLayout.getLayout(), Alignment.BOTTOM_CENTER);
        return layout;
    }

    @Override
    protected Component onAttachField(Object propertyId, Field<?> field) {
        if (propertyId.equals("subject")) {
            return informationLayout.addComponent(field, UserUIContext.getMessage(PageI18nEnum.FORM_SUBJECT), 0, 0, 2, "100%");
        } else if (propertyId.equals("content")) {
            return informationLayout.addComponent(field, UserUIContext.getMessage(GenericI18Enum.FORM_DESCRIPTION), 0, 1, 2, "100%");
        } else if (propertyId.equals("status")) {
            return informationLayout.addComponent(field, UserUIContext.getMessage(PageI18nEnum.FORM_VISIBILITY), 0, 2);
        }
        return null;
    }
}
