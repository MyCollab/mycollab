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

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.esofthead.mycollab.core.MyCollabException;
import com.vaadin.server.ErrorMessage;
import com.vaadin.server.Resource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 3.0
 */
public class VerticalTabsheet extends CustomComponent {
	private static final long serialVersionUID = 1L;

	private static Logger log = LoggerFactory.getLogger(VerticalTabsheet.class);

	private VerticalLayout tabNavigator;
	private CssLayout tabContainer;
	private VerticalLayout contentWrapper;
	private CssLayout navigatorWrapper;

	private Map<Button, Tab> compMap = new HashMap<Button, Tab>();

	private Button selectedButton = null;
	private Tab selectedComp = null;

	private final String TABSHEET_STYLENAME = "vertical-tabsheet";
	private final String TAB_STYLENAME = "tab";
	private final String TAB_SELECTED_STYLENAME = "tab-selected";

	public VerticalTabsheet() {
		GridLayout contentLayout = new GridLayout(2, 1);
		navigatorWrapper = new CssLayout();
		navigatorWrapper.setStyleName("navigator-wrap");
		navigatorWrapper.setHeight("100%");
		tabNavigator = new VerticalLayout();
		navigatorWrapper.addComponent(tabNavigator);

		contentWrapper = new VerticalLayout();
		contentWrapper.setStyleName("container-wrap");
		contentWrapper.setWidth("100%");

		tabContainer = new CssLayout();
		tabContainer.setWidth("100%");
		contentWrapper.addComponent(tabContainer);

		contentLayout.addComponent(navigatorWrapper, 0, 0);
		contentLayout.addComponent(contentWrapper, 1, 0);
		contentLayout.setColumnExpandRatio(1, 1.0f);
		this.setCompositionRoot(contentLayout);
		this.setStyleName(TABSHEET_STYLENAME);
	}

	public void addTab(Component component, String caption) {
		addTab(component, caption, null);
	}

	public void addTab(Component component, String caption, Resource resource) {
		final Button button = new Button(caption);
		button.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				if (selectedButton != button) {
					clearTabSelection(true);
					selectedButton = button;
					selectedButton.addStyleName(TAB_SELECTED_STYLENAME);
					selectedComp = compMap.get(selectedButton);
				}
				fireTabChangeEvent(new SelectedTabChangeEvent(
						VerticalTabsheet.this));
			}
		});
		if (resource != null){
			button.setIcon(resource);
		}
		else
		{
			setDefaulButtonIcon(button, false);
		}
		button.setStyleName(TAB_STYLENAME);
		button.setWidth("100%");

		//button.setIcon(resource);
		tabNavigator.addComponent(button);

		tabContainer.removeAllComponents();
		tabContainer.addComponent(component);

		TabImpl tabImpl = new TabImpl(caption, component);
		compMap.put(button, tabImpl);
	}



	private void fireTabChangeEvent(SelectedTabChangeEvent event) {
		this.fireEvent(event);
	}

	private static final Method SELECTED_TAB_CHANGE_METHOD;
	static {
		try {
			SELECTED_TAB_CHANGE_METHOD = SelectedTabChangeListener.class
					.getDeclaredMethod("selectedTabChange",
							new Class[] { SelectedTabChangeEvent.class });
		} catch (final java.lang.NoSuchMethodException e) {
			// This should never happen
			throw new java.lang.RuntimeException(
					"Internal error finding methods in TabSheet");
		}
	}

	public void addSelectedTabChangeListener(
			final TabSheet.SelectedTabChangeListener listener) {

		this.addListener(SelectedTabChangeEvent.class, listener,
				SELECTED_TAB_CHANGE_METHOD);
	}

	public Component selectTab(String viewName)
	{
		Collection<Button> tabs = compMap.keySet();
		for (Button btn : tabs) {
			Tab tab = compMap.get(btn);
			if (tab.getCaption().equals(viewName)) {
				selectedButton = btn;
				clearTabSelection(true);
				selectedButton.addStyleName(TAB_SELECTED_STYLENAME);
				setDefaulButtonIcon(selectedButton, true);
				selectedComp = tab;
				tabContainer.removeAllComponents();
				tabContainer.addComponent(tab.getComponent());
				return tab.getComponent();
			}
		}
		return null;
	}

	public Tab getSelectedTab() {
		return selectedComp;
	}

	public static class TabImpl implements Tab {
		private static final long serialVersionUID = 1L;

		private String caption;
		private Component component;

		public TabImpl(String caption, Component component) {
			this.caption = caption;
			this.component = component;
		}

		@Override
		public boolean isVisible() {
			throw new MyCollabException("Do not support");
		}

		@Override
		public void setVisible(boolean visible) {
			throw new MyCollabException("Do not support");
		}

		@Override
		public boolean isClosable() {
			throw new MyCollabException("Do not support");
		}

		@Override
		public void setClosable(boolean closable) {
			throw new MyCollabException("Do not support");
		}

		@Override
		public boolean isEnabled() {
			throw new MyCollabException("Do not support");
		}

		@Override
		public void setEnabled(boolean enabled) {
			throw new MyCollabException("Do not support");
		}

		@Override
		public void setCaption(String caption) {
			this.caption = caption;

		}

		@Override
		public String getCaption() {
			return caption;
		}

		@Override
		public Resource getIcon() {
			throw new MyCollabException("Do not support");
		}

		@Override
		public void setIcon(Resource icon) {
			throw new MyCollabException("Do not support");

		}

		@Override
		public String getDescription() {
			throw new MyCollabException("Do not support");
		}

		@Override
		public void setDescription(String description) {
			throw new MyCollabException("Do not support");
		}

		@Override
		public void setComponentError(ErrorMessage componentError) {
			throw new MyCollabException("Do not support");

		}

		@Override
		public ErrorMessage getComponentError() {
			throw new MyCollabException("Do not support");
		}

		@Override
		public Component getComponent() {
			return component;
		}

		@Override
		public void setStyleName(String styleName) {
			component.setStyleName(styleName);

		}

		@Override
		public String getStyleName() {
			return component.getStyleName();
		}

	}

	@Override
	public void setWidth(float width, Unit unit) {
		super.setWidth(width, unit);

		if (getCompositionRoot() != null)
			getCompositionRoot().setWidth(width, unit);
	}

	public void setNavigatorWidth(String width) {
		tabNavigator.setWidth(width);
		Iterator<Component> i = tabNavigator.iterator();
		while (i.hasNext()) {
			Component childComponent = i.next();
			childComponent.setWidth(width);
		}
	}

	public void setNavigatorStyleName(String styleName) {
		tabNavigator.setStyleName(styleName);
	}

	public void addNavigatorStyleName(String styleName) {
		tabNavigator.addStyleName(styleName);
	}

	public void setContainerWidth(String width) {
		tabContainer.setWidth(width);
	}

	public void setContainerStyleName(String styleName) {
		tabContainer.setStyleName(styleName);
	}

	public void addContainerStyleName(String styleName) {
		tabContainer.addStyleName(styleName);
	}

	private void clearTabSelection() {
		boolean setDefaultIcon = false;
		clearTabSelection(setDefaultIcon);
	}
	
	private void clearTabSelection(boolean setDefaultIcon) {
		Collection<Button> tabs = compMap.keySet();
		if (setDefaultIcon == true)
		{
			for (Button btn : tabs) {
				if(btn.getStyleName().contains(TAB_SELECTED_STYLENAME)){
					btn.removeStyleName(TAB_SELECTED_STYLENAME);       
					setDefaulButtonIcon(btn, false);
				}
			}
		}
		else 
		{
			for (Button btn : tabs) {
				if(btn.getStyleName().contains(TAB_SELECTED_STYLENAME)){
					btn.removeStyleName(TAB_SELECTED_STYLENAME);     
					/*setDefaulButtonIcon(btn, false);*/
				}
			}
		}
			
	}

	public void setDefaulButtonIcon(Button btn, Boolean selected){
		String caption = btn.getCaption();
		String suffix;
		if (selected == true)
			suffix = "_selected";
		else
			suffix = "";

		switch (caption){
		case "Dashboard":
			btn.setIcon(MyCollabResource
					.newResource("icons/22/project/dashboard" + suffix + ".png"));
			break;

		case "Messages":
			btn.setIcon(MyCollabResource
					.newResource("icons/22/project/message" + suffix + ".png"));
			break;
		case "Phases":
			btn.setIcon(MyCollabResource
					.newResource("icons/22/project/phase" + suffix + ".png"));
			break;

		case "Tasks":
			btn.setIcon(MyCollabResource
					.newResource("icons/22/project/task" + suffix + ".png"));
			break;

		case "Bugs":
			btn.setIcon(MyCollabResource
					.newResource("icons/22/project/bug" + suffix + ".png"));
			break;

		case "Files":
			btn.setIcon(MyCollabResource
					.newResource("icons/22/project/file" + suffix + ".png"));
			break;
		case "Risks":
			btn.setIcon(MyCollabResource
					.newResource("icons/22/project/risk" + suffix + ".png"));
			break;
		case "Problems":
			btn.setIcon(MyCollabResource
					.newResource("icons/22/project/problem" + suffix + ".png"));
			break;
		case "Time":
			btn.setIcon(MyCollabResource
					.newResource("icons/22/project/time" + suffix + ".png"));
			break;
		case "StandUp":
			btn.setIcon(MyCollabResource
					.newResource("icons/22/project/standup" + suffix + ".png"));
			break;
		case "Users & Settings":
			btn.setIcon(MyCollabResource
					.newResource("icons/22/project/user" + suffix + ".png"));
			break;
		default:
			log.debug("cannot find resource for" + caption);
		}
	}

	public VerticalLayout getContentWrapper() {
		return this.contentWrapper;
	}

	public CssLayout getNavigatorWrapper() {
		return this.navigatorWrapper;
	}
}
