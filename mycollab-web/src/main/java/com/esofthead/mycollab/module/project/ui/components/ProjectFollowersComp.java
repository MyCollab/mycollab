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
package com.esofthead.mycollab.module.project.ui.components;

import com.esofthead.mycollab.common.TableViewField;
import com.esofthead.mycollab.common.domain.MonitorItem;
import com.esofthead.mycollab.common.domain.SimpleMonitorItem;
import com.esofthead.mycollab.common.domain.criteria.MonitorSearchCriteria;
import com.esofthead.mycollab.common.i18n.FollowerI18nEnum;
import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.common.service.MonitorItemService;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.core.arguments.ValuedBean;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.domain.ProjectMember;
import com.esofthead.mycollab.module.project.domain.SimpleProjectMember;
import com.esofthead.mycollab.module.project.view.settings.component.ProjectMemberMultiSelectComp;
import com.esofthead.mycollab.module.project.view.settings.component.ProjectUserLink;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.ELabel;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.esofthead.mycollab.vaadin.ui.table.DefaultPagedBeanTable;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Table.ColumnGenerator;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.maddon.layouts.MHorizontalLayout;
import org.vaadin.maddon.layouts.MVerticalLayout;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 4.3.3
 */
public class ProjectFollowersComp<V extends ValuedBean> extends MVerticalLayout {
    private static final long serialVersionUID = 1L;

    private static final Logger LOG = LoggerFactory.getLogger(ProjectFollowersComp.class);

    protected MonitorItemService monitorItemService;

    private boolean currentUserFollow;
    private String type;
    private V bean;
    private String permissionItem;

    private Button followersBtn;

    public ProjectFollowersComp(String type, String permissionItem) {
        super();
        monitorItemService = ApplicationContextUtil.getSpringBean(MonitorItemService.class);
        this.type = type;
        this.permissionItem = permissionItem;
    }

    public void displayFollowers(final V bean) {
        this.bean = bean;
        this.removeAllComponents();
        this.withMargin(new MarginInfo(false, false, false, true));

        MHorizontalLayout header = new MHorizontalLayout();
        Label followerHeader = new Label(FontAwesome.EYE.getHtml() + " " +
                AppContext.getMessage(FollowerI18nEnum.OPT_SUB_INFO_WATCHERS), ContentMode.HTML);
        followerHeader.setStyleName("info-hdr");
        header.addComponent(followerHeader);

        if (hasEditPermission()) {
            Button editBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_EDIT),
                    new Button.ClickListener() {
                        private static final long serialVersionUID = 1L;

                        @Override
                        public void buttonClick(ClickEvent event) {
                            showEditWatchersWindow(bean);

                        }
                    });
            editBtn.setStyleName("link");
            editBtn.addStyleName("info-hdr");
            header.addComponent(editBtn);
        }

        this.addComponent(header);
        Label sep = new Label("/");
        sep.setStyleName("info-hdr");
        header.addComponent(sep);

        currentUserFollow = isUserWatching(bean);

        final Button toogleWatching = new Button("");
        toogleWatching.setStyleName("link");
        toogleWatching.addStyleName("info-hdr");
        toogleWatching.addClickListener(new ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(ClickEvent event) {
                if (currentUserFollow) {
                    unfollowItem(AppContext.getUsername(), bean);
                    currentUserFollow = false;
                    toogleWatching.setCaption(AppContext
                            .getMessage(FollowerI18nEnum.BUTTON_FOLLOW));
                } else {
                    followItem(AppContext.getUsername(), bean);
                    toogleWatching.setCaption(AppContext
                            .getMessage(FollowerI18nEnum.BUTTON_UNFOLLOW));
                    currentUserFollow = true;
                }

                updateTotalFollowers(bean);
            }
        });
        header.addComponent(toogleWatching);

        if (currentUserFollow) {
            toogleWatching.setCaption(AppContext
                    .getMessage(FollowerI18nEnum.BUTTON_UNFOLLOW));
        } else {
            toogleWatching.setCaption(AppContext
                    .getMessage(FollowerI18nEnum.BUTTON_FOLLOW));
        }

        MVerticalLayout layout = new MVerticalLayout().withWidth("100%").withMargin(new MarginInfo
                (false, false, false, true));
        this.addComponent(layout);

        int totalFollowers = getTotalFollowers(bean);
        followersBtn = new Button(AppContext.getMessage(FollowerI18nEnum.OPT_NUM_FOLLOWERS, totalFollowers),
                new ClickListener() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void buttonClick(ClickEvent event) {
                        if (hasReadPermission()) {
                            showEditWatchersWindow(bean);
                        }
                    }
                });
        followersBtn.setStyleName("link");
        layout.addComponent(followersBtn);
    }

    private void updateTotalFollowers(V bean) {
        int totalFollowers = getTotalFollowers(bean);
        followersBtn.setCaption(AppContext.getMessage(
                FollowerI18nEnum.OPT_NUM_FOLLOWERS, totalFollowers));
    }

    private boolean hasReadPermission() {
        return CurrentProjectVariables.canRead(permissionItem);
    }

    private boolean hasEditPermission() {
        return CurrentProjectVariables.canWrite(permissionItem);
    }

    private void showEditWatchersWindow(V bean) {
        UI.getCurrent().addWindow(new CompFollowersEditWindow(hasEditPermission()));
    }

    private boolean isUserWatching(V bean) {
        try {
            return monitorItemService.isUserWatchingItem(
                    AppContext.getUsername(), type,
                    (int) PropertyUtils.getProperty(bean, "id"));
        } catch (IllegalAccessException | InvocationTargetException
                | NoSuchMethodException e) {
            return false;
        }
    }

    private int getTotalFollowers(V bean) {
        try {
            MonitorSearchCriteria criteria = new MonitorSearchCriteria();
            criteria.setTypeId(new NumberSearchField((Number) PropertyUtils
                    .getProperty(bean, "id")));
            criteria.setType(new StringSearchField(type));
            return monitorItemService.getTotalCount(criteria);
        } catch (IllegalAccessException | InvocationTargetException
                | NoSuchMethodException e) {
            LOG.error("Error", e);
            return 0;
        }
    }

    private void followItem(String username, V bean) {
        try {
            MonitorItem monitorItem = new MonitorItem();
            monitorItem.setMonitorDate(new GregorianCalendar().getTime());
            monitorItem.setType(type);
            monitorItem.setTypeid((Integer) PropertyUtils.getProperty(bean, "id"));
            monitorItem.setUser(username);
            monitorItem.setSaccountid(AppContext.getAccountId());
            monitorItemService.saveWithSession(monitorItem,
                    AppContext.getUsername());
        } catch (IllegalAccessException | InvocationTargetException
                | NoSuchMethodException e) {
            LOG.error("Error", e);
        }
    }

    private void unfollowItem(String username, V bean) {
        try {
            MonitorSearchCriteria criteria = new MonitorSearchCriteria();
            criteria.setTypeId(new NumberSearchField((Integer) PropertyUtils
                    .getProperty(bean, "id")));
            criteria.setType(new StringSearchField(type));
            criteria.setUser(new StringSearchField(username));
            monitorItemService.removeByCriteria(criteria,
                    AppContext.getAccountId());
        } catch (IllegalAccessException | InvocationTargetException
                | NoSuchMethodException e) {
            LOG.error("Error", e);
        }
    }

    class CompFollowersEditWindow extends Window {
        private static final long serialVersionUID = 1L;

        private DefaultPagedBeanTable<MonitorItemService, MonitorSearchCriteria, SimpleMonitorItem> tableItem;

        public CompFollowersEditWindow(boolean isEdit) {
            this.setModal(true);
            this.setResizable(false);
            this.setCaption(AppContext.getMessage(FollowerI18nEnum.DIALOG_WATCHERS_TITLE));
            this.setWidth("600px");

            MVerticalLayout content = new MVerticalLayout();
            this.setContent(content);

            if (isEdit) {
                MHorizontalLayout headerPanel = new MHorizontalLayout();
                content.addComponent(headerPanel);

                final ProjectMemberMultiSelectComp memberSelection = new ProjectMemberMultiSelectComp();
                headerPanel.addComponent(memberSelection);
                Button btnSave = new Button(AppContext.getMessage(FollowerI18nEnum.BUTTON_FOLLOW),
                        new Button.ClickListener() {
                            private static final long serialVersionUID = 1L;

                            @Override
                            public void buttonClick(ClickEvent event) {
                                List<SimpleProjectMember> members = memberSelection.getSelectedItems();

                                for (ProjectMember member : members) {
                                    followItem(member.getUsername(), bean);
                                }

                                memberSelection.resetComp();
                                loadMonitorItems();
                            }
                        });

                btnSave.setStyleName(UIConstants.THEME_GREEN_LINK);
                btnSave.setIcon(FontAwesome.PLUS);

                headerPanel.addComponent(btnSave);

                this.addCloseListener(new CloseListener() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void windowClose(CloseEvent e) {
                        displayFollowers(bean);
                    }
                });
            }

            tableItem = new DefaultPagedBeanTable<>(ApplicationContextUtil
                            .getSpringBean(MonitorItemService.class),
                    SimpleMonitorItem.class,
                    Arrays.asList(
                            new TableViewField(FollowerI18nEnum.OPT_FOLLOWER_NAME,
                                    "user", UIConstants.TABLE_EX_LABEL_WIDTH),
                            new TableViewField(
                                    FollowerI18nEnum.OPT_FOLLOWER_CREATE_DATE,
                                    "monitorDate", UIConstants.TABLE_DATE_WIDTH),
                            new TableViewField(null, "id",
                                    UIConstants.TABLE_CONTROL_WIDTH)));

            tableItem.addGeneratedColumn("user", new Table.ColumnGenerator() {
                private static final long serialVersionUID = 1L;

                @Override
                public com.vaadin.ui.Component generateCell(Table source,
                                                            final Object itemId, Object columnId) {
                    SimpleMonitorItem monitorItem = tableItem
                            .getBeanByIndex(itemId);

                    return new ProjectUserLink(monitorItem.getUser(),
                            monitorItem.getUserAvatarId(), monitorItem
                            .getUserFullname());

                }
            });

            tableItem.addGeneratedColumn("monitorDate", new ColumnGenerator() {
                private static final long serialVersionUID = 1L;

                @Override
                public com.vaadin.ui.Component generateCell(Table source,
                                                            Object itemId, Object columnId) {
                    MonitorItem monitorItem = tableItem
                            .getBeanByIndex(itemId);
                    return new ELabel().prettyDateTime(monitorItem.getMonitorDate());
                }
            });

            if (isEdit) {
                tableItem.addGeneratedColumn("id", new ColumnGenerator() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public Object generateCell(Table source, Object itemId,
                                               Object columnId) {
                        final MonitorItem monitorItem = tableItem
                                .getBeanByIndex(itemId);

                        Button deleteBtn = new Button(null,
                                new Button.ClickListener() {
                                    private static final long serialVersionUID = 1L;

                                    @Override
                                    public void buttonClick(ClickEvent event) {
                                        MonitorItemService monitorItemService = ApplicationContextUtil
                                                .getSpringBean(MonitorItemService.class);
                                        monitorItemService.removeWithSession(
                                                monitorItem.getId(),
                                                AppContext.getUsername(),
                                                AppContext.getAccountId());
                                        CompFollowersEditWindow.this
                                                .loadMonitorItems();
                                    }
                                });
                        deleteBtn.setIcon(FontAwesome.TRASH_O);
                        deleteBtn.addStyleName(UIConstants.BUTTON_ICON_ONLY);
                        return deleteBtn;
                    }
                });
            } else {
                tableItem.addGeneratedColumn("id", new ColumnGenerator() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public Object generateCell(Table source, Object itemId,
                                               Object columnId) {
                        return new Label("");
                    }

                });
            }
            tableItem.setWidth("100%");
            content.addComponent(tableItem);
            loadMonitorItems();
        }

        private void loadMonitorItems() {
            try {
                MonitorSearchCriteria searchCriteria = new MonitorSearchCriteria();
                searchCriteria.setTypeId(new NumberSearchField(
                        (Integer) PropertyUtils.getProperty(bean, "id")));
                searchCriteria.setType(new StringSearchField(type));
                tableItem.setSearchCriteria(searchCriteria);
            } catch (IllegalAccessException | InvocationTargetException
                    | NoSuchMethodException e) {
                LOG.error("Error", e);
            }
        }
    }
}
