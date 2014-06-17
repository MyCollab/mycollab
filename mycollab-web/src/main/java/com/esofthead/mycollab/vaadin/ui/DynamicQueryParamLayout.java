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
import com.esofthead.mycollab.core.arguments.SearchCriteria;
import com.esofthead.mycollab.core.db.query.Param;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.GenericSearchPanel.SearchLayout;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.HorizontalLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.0
 * 
 * @param <S>
 */
public abstract class DynamicQueryParamLayout<S extends SearchCriteria> extends
		SearchLayout<S> {
	private static final long serialVersionUID = 1L;

	protected String type;
	protected BuildCriterionComponent<S> buildCriterionComp;
	protected ComponentContainer header;

	public DynamicQueryParamLayout(DefaultGenericSearchPanel<S> parent,
			String type) {
		super(parent, "advancedSearch");
		setStyleName("advancedSearchLayout");

		this.type = type;
		initLayout();
	}

	protected void initLayout() {
		header = constructHeader();
		ComponentContainer body = constructBody();
		ComponentContainer footer = constructFooter();
		this.addComponent(header, "advSearchHeader");
		this.addComponent(body, "advSearchBody");
		this.addComponent(footer, "advSearchFooter");
	}

	@Override
	protected void addHeaderRight(Component c) {
		if (this.header == null)
			return;

		this.header.addComponent(c);
	}

	private HorizontalLayout createButtonControls() {
		HorizontalLayout buttonControls = new HorizontalLayout();
		buttonControls.setSpacing(true);
		buttonControls.setMargin(true);
		final Button searchBtn = new Button(
				AppContext.getMessage(GenericI18Enum.BUTTON_SEARCH),
				new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(final ClickEvent event) {
						DynamicQueryParamLayout.this.callSearchAction();
					}
				});
		UiUtils.addComponent(buttonControls, searchBtn, Alignment.MIDDLE_CENTER);
		searchBtn.setStyleName(UIConstants.THEME_GREEN_LINK);
		searchBtn.setIcon(MyCollabResource.newResource("icons/16/search.png"));

		final Button clearBtn = new Button(
				AppContext.getMessage(GenericI18Enum.BUTTON_CLEAR_LABEL),
				new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(final ClickEvent event) {
						clearFields();
					}
				});
		clearBtn.setStyleName(UIConstants.THEME_GRAY_LINK);
		UiUtils.addComponent(buttonControls, clearBtn, Alignment.MIDDLE_CENTER);

		final Button basicSearchBtn = new Button(
				AppContext.getMessage(GenericI18Enum.BUTTON_BASIC_SEARCH),
				new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(final ClickEvent event) {
						((DefaultGenericSearchPanel<S>) DynamicQueryParamLayout.this.searchPanel)
								.moveToBasicSearchLayout();
					}
				});
		basicSearchBtn.setStyleName("link");
		UiUtils.addComponent(buttonControls, basicSearchBtn,
				Alignment.MIDDLE_CENTER);
		return buttonControls;
	}

	protected void clearFields() {

	}

	@Override
	protected S fillupSearchCriteria() {
		return buildCriterionComp.fillupSearchCriteria();
	}

	protected abstract Class<S> getType();

	public abstract ComponentContainer constructHeader();

	public abstract Param[] getParamFields();

	public ComponentContainer constructBody() {
		buildCriterionComp = new BuildCriterionComponent<S>(getParamFields(),
				getType(), type) {
			@Override
			protected Component buildPropertySearchComp(String fieldId) {
				return buildSelectionComp(fieldId);
			}
		};
		return buildCriterionComp;
	}

	public ComponentContainer constructFooter() {
		HorizontalLayout buttonControls = createButtonControls();
		buttonControls.setMargin(new MarginInfo(false, true, false, true));
		return buttonControls;
	}

	protected Component buildSelectionComp(String fieldId) {
		return null;
	}
}
