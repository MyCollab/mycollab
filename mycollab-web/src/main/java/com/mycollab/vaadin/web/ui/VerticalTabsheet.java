/**
 * Copyright © MyCollab
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.vaadin.web.ui;

import com.mycollab.common.i18n.ShellI18nEnum;
import com.mycollab.core.MyCollabException;
import com.mycollab.vaadin.UserUIContext;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Page;
import com.vaadin.server.Resource;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MCssLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author MyCollab Ltd.
 * @since 3.0
 */
public class VerticalTabsheet extends CustomComponent {
    private static final long serialVersionUID = 1L;

    private static final String MAX_SIZE = "220px";
    private static final String MIN_SIZE = "65px";

    private static final String TABSHEET_STYLE = "vertical-tabsheet";
    private static final String TAB_STYLE = "tab";
    private static final String TAB_SELECTED_STYLE = "tab-selected";
    private static final String GROUP_TAB_SELECTED_STYLE = "group-tab-selected";

    private VerticalLayout navigatorContainer;

    private MCssLayout contentWrapper;

    private Map<String, ButtonTab> compMap = new HashMap<>();

    private ButtonTab selectedComp = null;
    private Button toggleHideButton;
    private Boolean retainVisibility = true;

    public VerticalTabsheet() {

        navigatorContainer = new MVerticalLayout().withStyleName("navigator-wrap").withSpacing(false)
                .withMargin(new MarginInfo(true, false, true, false));

        contentWrapper = new MCssLayout().withStyleName("container-wrap").withFullSize();

        this.setCompositionRoot(new MCssLayout(navigatorContainer, contentWrapper).withFullWidth());
        this.setStyleName(TABSHEET_STYLE);
    }

    private void hideTabsCaption() {
        navigatorContainer.forEach(container -> ((ButtonTab) container).hideCaption());
    }

    private void showTabsCaption() {
        navigatorContainer.forEach(container -> ((ButtonTab) container).showCaption());
    }

    public void addTab(String id, String caption) {
        addTab(null, id, caption, null, null);
    }

    public void addTab(String id, String caption, Resource resource) {
        addTab(null, id, caption, null, resource);
    }

    public void addTab(String id, String caption, String link, Resource resource) {
        addTab(null, id, caption, link, resource);
    }

    public void addTab(String parentId, String id, String caption, String link, Resource resource) {
        if (!hasTab(id)) {
            final ButtonTab tab = new ButtonTab(id, caption, link);

            tab.addClickListener(clickEvent -> {
                if (!clickEvent.isCtrlKey() && !clickEvent.isMetaKey()) {
                    if (selectedComp != tab) {
                        setSelectedTab(tab);
                    }
                    fireTabChangeEvent(new SelectedTabChangeEvent(VerticalTabsheet.this, true));
                } else {
                    Page.getCurrent().open(tab.link, "_blank", false);
                }
            });

            tab.setIcon(resource);
            tab.withStyleName(TAB_STYLE, WebThemes.TEXT_ELLIPSIS).withFullWidth();

            if (parentId == null) {
                navigatorContainer.addComponent(tab);
            } else {
                ButtonTab parentTab = compMap.get(parentId);
                if (parentTab != null) {
                    parentTab.addSubTab(tab);
                    navigatorContainer.addComponent(tab);
                    parentTab.addStyleName("collapsed-tab");
                    tab.addStyleName("child-tab");
                    tab.addStyleName("hide");

                    if (parentTab.getListeners(Button.ClickEvent.class).size() < 2) {
                        parentTab.addClickListener((Button.ClickListener) event -> {
                            parentTab.toggleGroupTabDisplay();
                        });
                    }
                } else {
                    throw new MyCollabException("Not found parent tab with id " + parentId);
                }
            }

            compMap.put(id, tab);
        } else {
            throw new MyCollabException("Existing tab has id " + id);
        }
    }

    private boolean hasTab(String viewId) {
        return compMap.containsKey(viewId);
    }

    public void removeTab(String viewId) {
        ButtonTab tabImpl = compMap.get(viewId);
        if (tabImpl != null) {
            navigatorContainer.removeComponent(tabImpl);
            compMap.remove(viewId);
        }
    }

    private ButtonTab getButtonById(String viewId) {
        for (int i = 0; i < navigatorContainer.getComponentCount(); i++) {
            ButtonTab button = (ButtonTab) navigatorContainer.getComponent(i);
            if (viewId.equals(button.getTabId())) {
                return button;
            }
        }

        return null;
    }

    public void setNavigatorVisibility(boolean visibility) {
        if (!visibility) {
            navigatorContainer.setWidth(MIN_SIZE);
            this.hideTabsCaption();

            navigatorContainer.setComponentAlignment(toggleHideButton, Alignment.MIDDLE_CENTER);
            toggleHideButton.setIcon(VaadinIcons.ANGLE_DOUBLE_RIGHT);
            toggleHideButton.setStyleName(WebThemes.BUTTON_ICON_ONLY + " expand-button");
            toggleHideButton.addStyleName("toggle-button");
            toggleHideButton.setDescription(UserUIContext.getMessage(ShellI18nEnum.ACTION_EXPAND_MENU));
            toggleHideButton.setWidth("65px");
            toggleHideButton.setCaption("");
        } else {
            navigatorContainer.setWidth(MAX_SIZE);
            this.showTabsCaption();

            toggleHideButton.setStyleName(WebThemes.BUTTON_ICON_ONLY + " closed-button");
            toggleHideButton.addStyleName("toggle-button");
            toggleHideButton.setIcon(VaadinIcons.CLOSE_SMALL);
            toggleHideButton.setWidth(MAX_SIZE);
            toggleHideButton.setDescription(UserUIContext.getMessage(ShellI18nEnum.ACTION_COLLAPSE_MENU));
            navigatorContainer.setComponentAlignment(toggleHideButton, Alignment.MIDDLE_CENTER);
        }
    }

    public void addToggleNavigatorControl() {
        if (getButtonById("button") == null) {
            toggleHideButton = new ButtonTab("button", "", "");
            toggleHideButton.setStyleName(WebThemes.BUTTON_ICON_ONLY + " closed-button");
            toggleHideButton.addStyleName("toggle-button");
            toggleHideButton.addClickListener(clickEvent -> {
                retainVisibility = !retainVisibility;
                setNavigatorVisibility(retainVisibility);
            });
            navigatorContainer.addComponent(toggleHideButton, 0);
            navigatorContainer.setComponentAlignment(toggleHideButton, Alignment.TOP_RIGHT);
        }

        setNavigatorVisibility(retainVisibility);
    }

    private void fireTabChangeEvent(SelectedTabChangeEvent event) {
        this.fireEvent(event);
    }

    private static final Method SELECTED_TAB_CHANGE_METHOD;

    static {
        try {
            SELECTED_TAB_CHANGE_METHOD = SelectedTabChangeListener.class
                    .getDeclaredMethod("selectedTabChange", SelectedTabChangeEvent.class);
        } catch (NoSuchMethodException e) {
            throw new MyCollabException("Internal error finding methods in TabSheet");
        }
    }

    public void addSelectedTabChangeListener(TabSheet.SelectedTabChangeListener listener) {
        this.addListener(SelectedTabChangeEvent.class, listener, SELECTED_TAB_CHANGE_METHOD);
    }

    public Component selectTab(String viewId) {
        return selectTab(viewId, null);
    }

    public Component selectTab(String viewId, Component viewDisplay) {
        ButtonTab tab = compMap.get(viewId);
        if (tab != null) {
            setSelectedTab(tab);

            // Hack for tab view has both header - content or content only
            if (contentWrapper.getComponentCount() > 0 && "tab-content-header".equals(contentWrapper.getComponent(0).getId())) {
                if (contentWrapper.getComponentCount() > 1) {
                    int count = contentWrapper.getComponentCount();
                    for (int i = count - 1; i > 0; i--) {
                        contentWrapper.removeComponent(contentWrapper.getComponent(i));
                    }
                }
            } else {
                contentWrapper.removeAllComponents();
            }

            if (viewDisplay != null) {
                contentWrapper.addComponent(viewDisplay);
            }

            return viewDisplay;
        } else {
            return null;
        }
    }

    private void setSelectedTab(ButtonTab tab) {
        if (tab != selectedComp) {
            clearTabSelection();
            selectedComp = tab;
            selectedComp.addStyleName(TAB_SELECTED_STYLE);

            ButtonTab parentGroupTab = null;
            if (tab.hasChildTabs()) {
                parentGroupTab = tab;
            }
            if (tab.getParentTab() != null) {
                parentGroupTab = tab.getParentTab();
            }

            if (parentGroupTab != null) {
                parentGroupTab.addStyleName(GROUP_TAB_SELECTED_STYLE);
                parentGroupTab.children.forEach(child -> child.addStyleName(GROUP_TAB_SELECTED_STYLE));

                if (parentGroupTab.collapsed) {
                    parentGroupTab.toggleGroupTabDisplay();
                }
            }
        }
    }

    public ButtonTab getSelectedTab() {
        return selectedComp;
    }

    @Override
    public void setWidth(float width, Unit unit) {
        super.setWidth(width, unit);

        if (getCompositionRoot() != null)
            getCompositionRoot().setWidth(width, unit);
    }

    public void setNavigatorWidth(String width) {
        navigatorContainer.setWidth(width);
        navigatorContainer.forEach(comp -> comp.setWidth(width));
    }

    private void clearTabSelection() {
        navigatorContainer.forEach(component -> {
            if (component.getStyleName().contains(TAB_SELECTED_STYLE)) {
                component.removeStyleName(TAB_SELECTED_STYLE);
            }

            if (component.getStyleName().contains(GROUP_TAB_SELECTED_STYLE)) {
                component.removeStyleName(GROUP_TAB_SELECTED_STYLE);
            }
        });
    }

    public MCssLayout getContentWrapper() {
        return this.contentWrapper;
    }

    public static class ButtonTab extends MButton {
        private static final long serialVersionUID = 1L;

        private String tabId;
        String link;
        private String caption;

        private ButtonTab parentTab;
        private List<ButtonTab> children;
        private boolean collapsed = true;

        ButtonTab(String id, String caption, String link) {
            super(caption);
            this.tabId = id;
            this.link = link;
            this.caption = caption;
        }

        void hideCaption() {
            this.setCaption("");
            this.setDescription(String.format("<div class=\"v-label-h3 no-margin\">%s</div>", caption), ContentMode.HTML);
        }

        void showCaption() {
            this.setCaption(caption);
            this.setDescription("");
        }

        public String getTabId() {
            return tabId;
        }

        public void setParentTab(ButtonTab parentTab) {
            this.parentTab = parentTab;
        }

        public ButtonTab getParentTab() {
            return parentTab;
        }

        public void addSubTab(ButtonTab child) {
            if (children == null) {
                children = new ArrayList<>();
            }
            children.add(child);
            child.setParentTab(this);
        }

        public boolean hasChildTabs() {
            return (children != null) && children.size() > 0;
        }

        void toggleGroupTabDisplay() {
            if (children != null) {
                this.collapsed = !this.collapsed;
                String newStyleName = this.collapsed ? "collapsed-tab" : "un-collapsed-tab";
                String oldStyleName = this.collapsed ? "un-collapsed-tab" : "collapsed-tab";
                this.removeStyleName(oldStyleName);
                this.addStyleName(newStyleName);

                if (this.collapsed) {
                    this.children.forEach(childTab -> childTab.addStyleName("hide"));
                } else {
                    this.children.forEach(childTab -> childTab.removeStyleName("hide"));
                }
            }
        }
    }
}
