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
package com.mycollab.module.project.ui.components;

import com.mycollab.common.domain.MonitorItem;
import com.mycollab.common.domain.criteria.MonitorSearchCriteria;
import com.mycollab.common.i18n.FollowerI18nEnum;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.common.service.MonitorItemService;
import com.mycollab.core.arguments.*;
import com.mycollab.core.utils.StringUtils;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectMemberStatusConstants;
import com.mycollab.module.project.domain.SimpleProjectMember;
import com.mycollab.module.project.domain.criteria.ProjectMemberSearchCriteria;
import com.mycollab.module.project.service.ProjectMemberService;
import com.mycollab.module.user.CommonTooltipGenerator;
import com.mycollab.module.user.domain.SimpleUser;
import com.mycollab.db.arguments.*;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppContext;
import com.mycollab.vaadin.AsyncInvoker;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.ui.NotificationUtil;
import com.mycollab.vaadin.ui.UserAvatarControlFactory;
import com.mycollab.vaadin.web.ui.UIConstants;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.jouni.restrain.Restrain;
import org.vaadin.viritin.layouts.MCssLayout;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 4.3.3
 */
public class ProjectFollowersComp<V extends ValuedBean> extends MVerticalLayout {
    private static final long serialVersionUID = 1L;
    private static final Logger LOG = LoggerFactory.getLogger(ProjectFollowersComp.class);

    private MonitorItemService monitorItemService;
    private List<SimpleUser> followers;

    private String type;
    private Integer typeId;
    private V bean;
    private String permissionItem;
    private CssLayout watcherLayout;

    public ProjectFollowersComp(String type, String permissionItem) {
        super();
        this.withMargin(false);
        monitorItemService = AppContextUtil.getSpringBean(MonitorItemService.class);
        this.type = type;
        this.permissionItem = permissionItem;
        this.setWidth("100%");
    }

    public void displayFollowers(final V bean) {
        this.bean = bean;
        try {
            typeId = (Integer) PropertyUtils.getProperty(bean, "id");
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            LOG.error("Error", e);
            return;
        }
        this.removeAllComponents();

        MHorizontalLayout header = new MHorizontalLayout().withStyleName("info-hdr");
        header.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        Label followerHeader = new Label(FontAwesome.EYE.getHtml() + " " +
                AppContext.getMessage(FollowerI18nEnum.OPT_SUB_INFO_WATCHERS), ContentMode.HTML);
        header.addComponent(followerHeader);

        if (hasEditPermission()) {
            final PopupView addPopupView = new PopupView(AppContext.getMessage(GenericI18Enum.ACTION_MODIFY), new MVerticalLayout());
            addPopupView.addPopupVisibilityListener(popupVisibilityEvent -> {
                PopupView.Content content = addPopupView.getContent();
                if (popupVisibilityEvent.isPopupVisible()) {
                    MVerticalLayout popupComponent = (MVerticalLayout) content.getPopupComponent();
                    popupComponent.removeAllComponents();
                    popupComponent.with(new ELabel(AppContext.getMessage(FollowerI18nEnum.OPT_SUB_INFO_WATCHERS))
                            .withStyleName(ValoTheme.LABEL_H3), new ModifyWatcherPopup());
                } else {
                    MVerticalLayout popupComponent = (MVerticalLayout) content.getPopupComponent();
                    ModifyWatcherPopup popup = (ModifyWatcherPopup) popupComponent.getComponent(1);
                    List<MonitorItem> unsavedItems = popup.getUnsavedItems();
                    monitorItemService.saveMonitorItems(unsavedItems);
                    loadWatchers();
                }
            });
            header.addComponent(addPopupView);
        }
        header.addComponent(ELabel.fontIcon(FontAwesome.QUESTION_CIRCLE).withStyleName(UIConstants.INLINE_HELP)
                .withDescription(AppContext.getMessage(FollowerI18nEnum.FOLLOWER_EXPLAIN_HELP)));

        this.addComponent(header);

        watcherLayout = new MCssLayout().withFullWidth().withStyleName(UIConstants.FLEX_DISPLAY);
        this.addComponent(watcherLayout);
        loadWatchers();
    }

    private void loadWatchers() {
        AsyncInvoker.access(new AsyncInvoker.PageCommand() {
            @Override
            public void run() {
                watcherLayout.removeAllComponents();
                followers = monitorItemService.getWatchers(type, typeId);
                for (SimpleUser follower : followers) {
                    watcherLayout.addComponent(new FollowerComp(follower));
                }
            }
        });
    }

    private boolean hasEditPermission() {
        return CurrentProjectVariables.canWrite(permissionItem);
    }

    private void unFollowItem(String username) {
        try {
            MonitorSearchCriteria criteria = new MonitorSearchCriteria();
            criteria.setTypeId(new NumberSearchField((Integer) PropertyUtils.getProperty(bean, "id")));
            criteria.setType(StringSearchField.and(type));
            criteria.setUser(StringSearchField.and(username));
            monitorItemService.removeByCriteria(criteria, AppContext.getAccountId());
            for (SimpleUser user : followers) {
                if (username.equals(user.getUsername())) {
                    followers.remove(user);
                    break;
                }
            }
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            LOG.error("Error", e);
        }
    }

    private class FollowerComp extends CssLayout {
        FollowerComp(final SimpleUser user) {
            final Image userAvatarBtn = UserAvatarControlFactory.createUserAvatarEmbeddedComponent(user.getAvatarid(), 32);
            userAvatarBtn.addStyleName(UIConstants.CIRCLE_BOX);
            userAvatarBtn.setDescription(CommonTooltipGenerator.generateTooltipUser(AppContext.getUserLocale(), user,
                    AppContext.getSiteUrl(), AppContext.getUserTimeZone()));
            addComponent(userAvatarBtn);
            this.addStyleName("removeable-btn");
            this.setWidthUndefined();
            this.addLayoutClickListener(layoutClickEvent -> {
                if (layoutClickEvent.getClickedComponent() == userAvatarBtn) {
                } else if (!hasEditPermission()) {
                    NotificationUtil.showMessagePermissionAlert();
                } else {
                    unFollowItem(user.getUsername());
                    ((ComponentContainer) FollowerComp.this.getParent()).removeComponent(FollowerComp.this);
                }
            });
        }
    }

    private class ModifyWatcherPopup extends MVerticalLayout {
        private List<SimpleProjectMember> projectMembers;
        private List<SimpleProjectMember> unsavedMembers = new ArrayList<>();

        ModifyWatcherPopup() {
            new Restrain(this).setMaxHeight("600px");
            this.addStyleName(UIConstants.SCROLLABLE_CONTAINER);
            ProjectMemberSearchCriteria criteria = new ProjectMemberSearchCriteria();
            criteria.setProjectId(new NumberSearchField(CurrentProjectVariables.getProjectId()));
            criteria.setStatuses(new SetSearchField<>(ProjectMemberStatusConstants.ACTIVE));
            criteria.addOrderField(new SearchCriteria.OrderField("memberFullName", SearchCriteria.ASC));

            ProjectMemberService projectMemberService = AppContextUtil.getSpringBean(ProjectMemberService.class);
            projectMembers = projectMemberService.findPageableListByCriteria(new BasicSearchRequest<>(
                    criteria, 0, Integer.MAX_VALUE));
            for (SimpleProjectMember member : projectMembers) {
                this.addComponent(new FollowerRow(member));
            }
            this.setWidth("100%");
        }

        List<MonitorItem> getUnsavedItems() {
            List<MonitorItem> items = new ArrayList<>(unsavedMembers.size());
            for (SimpleProjectMember member : unsavedMembers) {
                MonitorItem item = new MonitorItem();
                item.setExtratypeid(CurrentProjectVariables.getProjectId());
                item.setMonitorDate(new GregorianCalendar().getTime());
                item.setSaccountid(AppContext.getAccountId());
                item.setType(type);
                item.setTypeid(typeId);
                item.setUser(member.getUsername());
                items.add(item);
            }
            return items;
        }

        private class FollowerRow extends MHorizontalLayout {
            private CheckBox isSelectedBox;
            private boolean isWatching = false;

            private FollowerRow(final SimpleProjectMember member) {
                isSelectedBox = new CheckBox();
                Image avatarResource = UserAvatarControlFactory.createUserAvatarEmbeddedComponent(member.getMemberAvatarId(), 16);
                Label icon = new Label(StringUtils.trim(member.getDisplayName(), 20, true));
                icon.setDescription(member.getDisplayName());
                this.with(isSelectedBox, avatarResource, icon);
                for (SimpleUser follower : followers) {
                    if (follower.getUsername().equals(member.getUsername())) {
                        isSelectedBox.setValue(true);
                        isWatching = true;
                    }
                }
                isSelectedBox.addValueChangeListener(valueChangeEvent -> {
                    if (isSelectedBox.getValue()) {
                        if (!isWatching) {
                            unsavedMembers.add(member);
                        }
                    } else {
                        if (isWatching) {
                            unFollowItem(member.getUsername());
                            isWatching = false;
                        } else {
                            unsavedMembers.remove(member);
                        }
                    }
                });
            }
        }
    }
}
