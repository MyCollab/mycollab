package com.mycollab.module.crm.ui.components;

import com.mycollab.common.domain.MonitorItem;
import com.mycollab.common.domain.criteria.MonitorSearchCriteria;
import com.mycollab.common.i18n.FollowerI18nEnum;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.common.service.MonitorItemService;
import com.mycollab.core.arguments.ValuedBean;
import com.mycollab.core.utils.StringUtils;
import com.mycollab.db.arguments.BasicSearchRequest;
import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.db.arguments.SetSearchField;
import com.mycollab.db.arguments.StringSearchField;
import com.mycollab.module.user.CommonTooltipGenerator;
import com.mycollab.module.user.domain.SimpleUser;
import com.mycollab.module.user.domain.criteria.UserSearchCriteria;
import com.mycollab.module.user.service.UserService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.ui.NotificationUtil;
import com.mycollab.vaadin.ui.UIConstants;
import com.mycollab.vaadin.ui.UserAvatarControlFactory;
import com.mycollab.vaadin.web.ui.WebThemes;
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

import static com.mycollab.vaadin.AsyncInvoker.*;

/**
 * @author MyCollab Ltd.
 * @since 4.3.3
 */
public class CrmFollowersComp<V extends ValuedBean> extends MVerticalLayout {
    private static final long serialVersionUID = 1L;
    private static final Logger LOG = LoggerFactory.getLogger(CrmFollowersComp.class);

    private MonitorItemService monitorItemService;
    private String type;
    private Integer typeId;
    private String permissionItem;
    private List<SimpleUser> followers;
    private V bean;

    private CssLayout watcherLayout;

    public CrmFollowersComp(String type, String permissionItem) {
        super();
        withMargin(false);
        monitorItemService = AppContextUtil.getSpringBean(MonitorItemService.class);
        this.type = type;
        this.permissionItem = permissionItem;
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
                UserUIContext.getMessage(FollowerI18nEnum.OPT_SUB_INFO_WATCHERS), ContentMode.HTML);
        header.addComponent(followerHeader);

        if (hasEditPermission()) {
            final PopupView addPopupView = new PopupView(UserUIContext.getMessage(GenericI18Enum.ACTION_MODIFY), new MVerticalLayout());
            addPopupView.addPopupVisibilityListener(popupVisibilityEvent -> {
                PopupView.Content content = addPopupView.getContent();
                if (popupVisibilityEvent.isPopupVisible()) {
                    MVerticalLayout popupComponent = (MVerticalLayout) content.getPopupComponent();
                    popupComponent.removeAllComponents();
                    popupComponent.with(new ELabel(UserUIContext.getMessage(FollowerI18nEnum.OPT_SUB_INFO_WATCHERS))
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
        header.addComponent(ELabel.fontIcon(FontAwesome.QUESTION_CIRCLE).withStyleName(WebThemes.INLINE_HELP)
                .withDescription(UserUIContext.getMessage(FollowerI18nEnum.FOLLOWER_EXPLAIN_HELP)));

        this.addComponent(header);
        watcherLayout = new MCssLayout().withFullWidth().withStyleName(WebThemes.FLEX_DISPLAY);
        this.addComponent(watcherLayout);
        loadWatchers();
    }

    private void loadWatchers() {
        access(getUI(), new PageCommand() {
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

    private class FollowerComp extends CssLayout {
        FollowerComp(final SimpleUser user) {
            final Image userAvatarBtn = UserAvatarControlFactory.createUserAvatarEmbeddedComponent(user.getAvatarid(), 32);
            userAvatarBtn.addStyleName(UIConstants.CIRCLE_BOX);
            userAvatarBtn.setDescription(CommonTooltipGenerator.generateTooltipUser(UserUIContext.getUserLocale(), user,
                    AppUI.getSiteUrl(), UserUIContext.getUserTimeZone()));
            addComponent(userAvatarBtn);
            this.addStyleName("removeable-btn");
            this.setWidthUndefined();
            this.addLayoutClickListener(layoutClickEvent -> {
                if (layoutClickEvent.getClickedComponent() == userAvatarBtn) {
                } else if (!hasEditPermission()) {
                    NotificationUtil.showMessagePermissionAlert();
                } else {
                    unfollowItem(user.getUsername());
                    ((ComponentContainer) FollowerComp.this.getParent()).removeComponent(FollowerComp.this);
                }
            });
        }
    }

    private boolean hasEditPermission() {
        return UserUIContext.canWrite(permissionItem);
    }

    private void unfollowItem(String username) {
        try {
            MonitorSearchCriteria criteria = new MonitorSearchCriteria();
            criteria.setTypeId(new NumberSearchField((Integer) PropertyUtils.getProperty(bean, "id")));
            criteria.setType(StringSearchField.and(type));
            criteria.setUser(StringSearchField.and(username));
            monitorItemService.removeByCriteria(criteria, AppUI.getAccountId());
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

    private class ModifyWatcherPopup extends MVerticalLayout {
        private List<SimpleUser> users;
        private List<SimpleUser> unsavedUsers = new ArrayList<>();

        ModifyWatcherPopup() {
            new Restrain(this).setMaxHeight("600px");
            this.addStyleName(WebThemes.SCROLLABLE_CONTAINER);
            this.setWidth("100%");
            UserSearchCriteria criteria = new UserSearchCriteria();
            criteria.setStatuses(new SetSearchField<>("Active"));

            UserService userService = AppContextUtil.getSpringBean(UserService.class);
            users = (List<SimpleUser>) userService.findPageableListByCriteria(new BasicSearchRequest<>(criteria));
            users.stream().map(FollowerRow::new).forEach(this::addComponent);
        }

        List<MonitorItem> getUnsavedItems() {
            List<MonitorItem> items = new ArrayList<>(unsavedUsers.size());
            for (SimpleUser member : unsavedUsers) {
                MonitorItem item = new MonitorItem();
                item.setMonitorDate(new GregorianCalendar().getTime());
                item.setSaccountid(AppUI.getAccountId());
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

            private FollowerRow(final SimpleUser member) {
                isSelectedBox = new CheckBox();
                Image avatarResource = UserAvatarControlFactory.createUserAvatarEmbeddedComponent(member.getAvatarid(), 16);
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
                            unsavedUsers.add(member);
                        }
                    } else {
                        if (isWatching) {
                            unfollowItem(member.getUsername());
                            isWatching = false;
                        } else {
                            unsavedUsers.remove(member);
                        }
                    }
                });
            }
        }
    }
}
