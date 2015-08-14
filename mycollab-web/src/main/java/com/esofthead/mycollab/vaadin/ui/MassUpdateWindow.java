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

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.MassUpdateCommand;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import org.vaadin.viritin.layouts.MHorizontalLayout;

/**
 * Mass update
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 * @param <B>
 */
public abstract class MassUpdateWindow<B> extends Window {
	private static final long serialVersionUID = 1L;

	protected B beanItem;
	protected AdvancedEditBeanForm<B> updateForm;
	protected MassUpdateLayout contentLayout;

	protected MassUpdateCommand<B> massUpdateCommand;

	protected Button updateBtn, closeBtn;

	public MassUpdateWindow(String title, Resource iconResource,
			B initialValue, MassUpdateCommand<B> massUpdatePresenter) {
		super(title);
		this.setWidth("1000px");
		this.setResizable(false);
		this.massUpdateCommand = massUpdatePresenter;
		this.beanItem = initialValue;
		this.setIcon(iconResource);

		this.contentLayout = new MassUpdateLayout();
		this.updateForm = new AdvancedEditBeanForm<>();

		this.contentLayout.addBody(this.updateForm);

		this.setContent(this.contentLayout);

		updateForm.setFormLayoutFactory(buildFormLayoutFactory());
		updateForm.setBeanFormFieldFactory(buildBeanFormFieldFactory());
		updateForm.setBean(beanItem);

		center();
	}

	abstract protected IFormLayoutFactory buildFormLayoutFactory();

	abstract protected AbstractBeanFieldGroupEditFieldFactory<B> buildBeanFormFieldFactory();

	protected ComponentContainer buildButtonControls() {
		MHorizontalLayout controlsLayout = new MHorizontalLayout().withMargin(true).withStyleName("addNewControl")
                .withWidth("100%");

		updateBtn = new Button(
				AppContext.getMessage(GenericI18Enum.BUTTON_UPDATE_LABEL),
				new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(ClickEvent event) {
						updateForm.commit();
						massUpdateCommand.massUpdate(beanItem);
						MassUpdateWindow.this.close();
					}
				});
		updateBtn.setStyleName(UIConstants.THEME_GREEN_LINK);
		updateBtn.setIcon(FontAwesome.SAVE);

		closeBtn = new Button(
				AppContext.getMessage(GenericI18Enum.BUTTON_CLOSE),
				new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(ClickEvent event) {
						MassUpdateWindow.this.close();
					}
				});
		closeBtn.setStyleName(UIConstants.THEME_GRAY_LINK);

        Label spacing = new Label();
        controlsLayout.with(spacing, updateBtn, closeBtn).alignAll(Alignment.MIDDLE_RIGHT).expand(spacing);
		return controlsLayout;
	}

}
