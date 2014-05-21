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

import com.esofthead.mycollab.core.MyCollabException;
import com.vaadin.server.ErrorMessage;
import com.vaadin.server.Resource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
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
		this(true);
	}

	public VerticalTabsheet(boolean isLeft) {
		HorizontalLayout contentLayout = new HorizontalLayout();
		navigatorWrapper = new CssLayout();
		navigatorWrapper.setStyleName("navigator-wrap");
		tabNavigator = new VerticalLayout();
		navigatorWrapper.addComponent(tabNavigator);

		contentWrapper = new VerticalLayout();
		contentWrapper.setStyleName("container-wrap");
		contentWrapper.setWidth("100%");

		tabContainer = new CssLayout();
		tabContainer.setWidth("100%");
		contentWrapper.addComponent(tabContainer);

		if (isLeft) {
			contentLayout.addComponent(navigatorWrapper);
			contentLayout.addComponent(contentWrapper);

		} else {
			contentLayout.addComponent(contentWrapper);
			contentLayout.addComponent(navigatorWrapper);
		}
		contentLayout.setExpandRatio(contentWrapper, 1.0f);

		this.setCompositionRoot(contentLayout);
		this.setStyleName(TABSHEET_STYLENAME);
	}

	public void addTab(Component component, String id, String caption) {
		addTab(component, id, caption, null);
	}

	public void addTab(Component component, String id, String caption,
			Resource resource) {
		final ButtonTabImpl button = new ButtonTabImpl(id, caption);
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

		if (resource == null) {
			setDefaulButtonIcon(button, false);
		} else {
			button.setIcon(resource);
		}
		button.setStyleName(TAB_STYLENAME);
		button.setWidth("100%");

		tabNavigator.addComponent(button);

		tabContainer.removeAllComponents();
		tabContainer.addComponent(component);

		TabImpl tabImpl = new TabImpl(id, caption, component);
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

	public Component selectTab(String viewId) {
		Collection<Button> tabs = compMap.keySet();
		for (Button btn : tabs) {
			TabImpl tab = (TabImpl) compMap.get(btn);
			if (tab.getTabId().equals(viewId)) {
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

	private void clearTabSelection(boolean setDefaultIcon) {
		Collection<Button> tabs = compMap.keySet();
		if (setDefaultIcon == true) {
			for (Button btn : tabs) {
				if (btn.getStyleName().contains(TAB_SELECTED_STYLENAME)) {
					btn.removeStyleName(TAB_SELECTED_STYLENAME);
					setDefaulButtonIcon(btn, false);
				}
			}
		} else {
			for (Button btn : tabs) {
				if (btn.getStyleName().contains(TAB_SELECTED_STYLENAME)) {
					btn.removeStyleName(TAB_SELECTED_STYLENAME);
				}
			}
		}

	}

	public VerticalLayout getContentWrapper() {
		return this.contentWrapper;
	}

	public CssLayout getNavigatorWrapper() {
		return this.navigatorWrapper;
	}

	protected void setDefaulButtonIcon(Button btn, Boolean selected) {

	}

	public void replaceContainer(ComponentContainer newContainer) {
		replaceContainer(newContainer, null);
	}

	public void replaceContainer(ComponentContainer newContainer,
			ComponentContainer newPosition) {
		ComponentContainer containerParent = (ComponentContainer) tabContainer
				.getParent();
		if (containerParent != null) {
			containerParent.removeComponent(tabContainer);
		}
		if (newPosition == null)
			newPosition = newContainer;
		newPosition.addComponent(tabContainer);
		contentWrapper.addComponent(newContainer);
	}

	public static class ButtonTabImpl extends Button {
		private static final long serialVersionUID = 1L;
		private String tabId;

		public ButtonTabImpl(String id, String caption) {
			super(caption);
			this.tabId = id;
		}

		public String getTabId() {
			return tabId;
		}

		public void setTabId(String tabId) {
			this.tabId = tabId;
		}
	}

	public static class TabImpl implements Tab {
		private static final long serialVersionUID = 1L;

		private String tabId;
		private String caption;
		private Component component;

		public TabImpl(String id, String caption, Component component) {
			this.tabId = id;
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

		public String getTabId() {
			return tabId;
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

		@Override
		public void setDefaultFocusComponent(Focusable component) {
			throw new MyCollabException("Do not support");
			
		}

		@Override
		public Focusable getDefaultFocusComponent() {
			throw new MyCollabException("Do not support");
		}

		@Override
		public void setIcon(Resource icon, String iconAltText) {
			throw new MyCollabException("Do not support");
		}

		@Override
		public String getIconAlternateText() {
			throw new MyCollabException("Do not support");
		}

		@Override
		public void setIconAlternateText(String iconAltText) {
			throw new MyCollabException("Do not support");
		}

		@Override
		public void setId(String id) {
			throw new MyCollabException("Do not support");
			
		}

		@Override
		public String getId() {
			throw new MyCollabException("Do not support");
		}

	}
}
