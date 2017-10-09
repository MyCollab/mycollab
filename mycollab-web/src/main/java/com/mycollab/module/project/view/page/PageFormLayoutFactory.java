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
