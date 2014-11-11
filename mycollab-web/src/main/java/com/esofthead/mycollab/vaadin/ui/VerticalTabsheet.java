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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.esofthead.mycollab.core.MyCollabException;
import com.vaadin.server.ErrorMessage;
import com.vaadin.server.Page;
import com.vaadin.server.Resource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
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

	private Map<String, Tab> compMap = new HashMap<String, Tab>();

	private Component selectedButton = null;
	private Tab selectedComp = null;

	private final String TABSHEET_STYLENAME = "vertical-tabsheet";
	private final String TAB_STYLENAME = "tab";
	private final String TAB_SELECTED_STYLENAME = "tab-selected";

	public VerticalTabsheet() {
		this(true);
	}

	public VerticalTabsheet(boolean isLeft) {
		CssLayout contentLayout = new CssLayout();
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
		// contentLayout.setExpandRatio(contentWrapper, 1.0f);

		this.setCompositionRoot(contentLayout);
		this.setStyleName(TABSHEET_STYLENAME);
	}

	public void addTab(Component component, String id, String caption) {
		addTab(component, id, 0, caption, null, null);
	}

	public void addTab(Component component, String id, int level,
			String caption, String link) {
		addTab(component, id, level, caption, link, null);
	}

	public void addTab(Component component, String id, String caption,
			Resource resource) {
		addTab(component, id, 0, caption, null, resource);
	}

	public void addTab(Component component, String id, int level,
			String caption, String link, Resource resource) {
		if (!hasTab(id)) {
			final ButtonTabImpl button = new ButtonTabImpl(id, level, caption,
					link);

			button.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void buttonClick(ClickEvent event) {
					if (!event.isCtrlKey() && !event.isMetaKey()) {
						if (selectedButton != button) {
							clearTabSelection(true);
							selectedButton = button;
							selectedButton.addStyleName(TAB_SELECTED_STYLENAME);
							selectedComp = compMap.get(button.getTabId());
						}
						fireTabChangeEvent(new SelectedTabChangeEvent(
								VerticalTabsheet.this));
					} else {
						Page.getCurrent().open(button.link, "_blank", false);
					}

				}
			});

			if (resource == null) {
				setDefaulButtonIcon(button, false);
			} else {
				button.setIcon(resource);
			}
			button.setStyleName(TAB_STYLENAME);
			button.setWidth("100%");

			if (button.getLevel() > 0) {
				int insertIndex = 0;
				for (int i = 0; i < tabNavigator.getComponentCount(); i++) {
					ButtonTabImpl buttonTmp = (ButtonTabImpl) tabNavigator
							.getComponent(i);
					if (buttonTmp.getLevel() > level) {
						break;
					} else {
						insertIndex++;
					}
				}
				tabNavigator.addComponent(button, insertIndex);
			} else {
				tabNavigator.addComponent(button);
			}

			TabImpl tabImpl = new TabImpl(id, caption, component);
			compMap.put(id, tabImpl);
		}

	}

	public boolean hasTab(String viewId) {
		return compMap.containsKey(viewId);
	}

	public void removeTab(String viewId) {
		Tab tabImpl = compMap.get(viewId);
		if (tabImpl != null) {
			ButtonTabImpl button = getButtonById(viewId);
			if (button != null) {
				tabNavigator.removeComponent(button);
				compMap.remove(viewId);
			}
		}
	}

	private ButtonTabImpl getButtonById(String viewId) {
		for (int i = 0; i < tabNavigator.getComponentCount(); i++) {
			ButtonTabImpl button = (ButtonTabImpl) tabNavigator.getComponent(i);
			if (viewId.equals(button.getTabId())) {
				return button;
			}
		}

		return null;
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
		Tab tab = compMap.get(viewId);
		Button btn = getButtonById(viewId);
		if (btn != null) {
			selectedButton = btn;
			clearTabSelection(true);
			selectedButton.addStyleName(TAB_SELECTED_STYLENAME);
			setDefaulButtonIcon(selectedButton, true);
			selectedComp = tab;
			tabContainer.removeAllComponents();
			tabContainer.addComponent(tab.getComponent());
			return tab.getComponent();
		} else {
			return null;
		}
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
		Iterator<Component> iterator = tabNavigator.iterator();
		if (setDefaultIcon == true) {
			while (iterator.hasNext()) {
				Component btn = iterator.next();
				if (btn.getStyleName().contains(TAB_SELECTED_STYLENAME)) {
					btn.removeStyleName(TAB_SELECTED_STYLENAME);
					setDefaulButtonIcon(btn, false);
				}
			}
		} else {
			while (iterator.hasNext()) {
				Component btn = iterator.next();
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

	protected void setDefaulButtonIcon(Component btn, Boolean selected) {

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
		private int level;
		String link;

		public ButtonTabImpl(String id, int level, String caption, String link) {
			super(caption);
			this.tabId = id;
			this.link = link;
			this.level = level;
		}

		public String getTabId() {
			return tabId;
		}

		public int getLevel() {
			return level;
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
