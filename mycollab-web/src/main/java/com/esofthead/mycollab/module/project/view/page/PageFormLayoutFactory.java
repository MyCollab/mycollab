package com.esofthead.mycollab.module.project.view.page;

import com.esofthead.mycollab.module.project.i18n.Page18InEnum;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.GridFormLayoutHelper;
import com.esofthead.mycollab.vaadin.ui.IFormLayoutFactory;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Field;
import com.vaadin.ui.Layout;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.4.0
 *
 */
public class PageFormLayoutFactory implements IFormLayoutFactory {
	private static final long serialVersionUID = 1L;

	private GridFormLayoutHelper informationLayout;

	@Override
	public Layout getLayout() {
		final VerticalLayout layout = new VerticalLayout();

		this.informationLayout = new GridFormLayoutHelper(2, 3, "100%",
				"167px", Alignment.TOP_LEFT);
		this.informationLayout.getLayout().setWidth("100%");
		this.informationLayout.getLayout().setMargin(false);
		this.informationLayout.getLayout().addStyleName("colored-gridlayout");
		layout.addComponent(this.informationLayout.getLayout());
		layout.setComponentAlignment(this.informationLayout.getLayout(),
				Alignment.BOTTOM_CENTER);
		return layout;
	}

	@Override
	public void attachField(Object propertyId, Field<?> field) {
		if (propertyId.equals("subject")) {
			this.informationLayout.addComponent(field,
					AppContext.getMessage(Page18InEnum.FORM_SUBJECT), 0, 0, 2,
					"100%");
		} else if (propertyId.equals("content")) {
			this.informationLayout.addComponent(field,
					AppContext.getMessage(Page18InEnum.FORM_DESCRIPTION), 0, 1,
					2, "100%");
		} else if (propertyId.equals("status")) {
			this.informationLayout.addComponent(field,
					AppContext.getMessage(Page18InEnum.FORM_VISIBILITY), 0, 2);
		}
	}
}
