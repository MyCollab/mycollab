package com.mycollab.module.user.accountsettings.team.view;

import com.mycollab.db.persistence.service.ISearchableService;
import com.mycollab.module.user.accountsettings.localization.RoleI18nEnum;
import com.mycollab.module.user.accountsettings.view.AccountSettingBreadcrumb;
import com.mycollab.module.user.domain.Role;
import com.mycollab.module.user.domain.SimpleRole;
import com.mycollab.module.user.domain.criteria.RoleSearchCriteria;
import com.mycollab.module.user.service.RoleService;
import com.mycollab.security.AccessPermissionFlag;
import com.mycollab.security.RolePermissionCollections;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.event.ViewItemAction;
import com.mycollab.vaadin.mvp.ScreenData;
import com.mycollab.vaadin.mvp.ViewManager;
import com.mycollab.vaadin.mvp.ViewPermission;
import com.mycollab.vaadin.ui.NotificationUtil;
import com.mycollab.vaadin.web.ui.DefaultMassEditActionHandler;
import com.mycollab.vaadin.web.ui.ListSelectionPresenter;
import com.mycollab.vaadin.web.ui.MailFormWindow;
import com.vaadin.ui.HasComponents;
import com.vaadin.ui.UI;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@ViewPermission(permissionId = RolePermissionCollections.ACCOUNT_ROLE, impliedPermissionVal = AccessPermissionFlag.READ_ONLY)
public class RoleListPresenter extends ListSelectionPresenter<RoleListView, RoleSearchCriteria, SimpleRole> {
    private static final long serialVersionUID = 1L;

    private RoleService roleService;

    public RoleListPresenter() {
        super(RoleListView.class);
        roleService = AppContextUtil.getSpringBean(RoleService.class);
    }

    @Override
    protected void postInitView() {
        super.postInitView();

        view.getPopupActionHandlers().setMassActionHandler(new DefaultMassEditActionHandler(this) {
            @Override
            protected void onSelectExtra(String id) {
                if (ViewItemAction.MAIL_ACTION.equals(id)) {
                    UI.getCurrent().addWindow(new MailFormWindow());
                }
            }

            @Override
            protected String getReportTitle() {
                return UserUIContext.getMessage(RoleI18nEnum.LIST);
            }

            @Override
            protected Class<?> getReportModelClassType() {
                return SimpleRole.class;
            }
        });
    }

    @Override
    protected void deleteSelectedItems() {
        if (!isSelectAll) {
            Collection<SimpleRole> currentDataList = view.getPagedBeanTable().getCurrentDataList();
            List<Role> keyList = new ArrayList<>();
            for (SimpleRole item : currentDataList) {
                if (item.isSelected()) {
                    if (Boolean.TRUE.equals(item.getIssystemrole())) {
                        NotificationUtil.showErrorNotification(UserUIContext.getMessage(RoleI18nEnum.ERROR_CAN_NOT_DELETE_SYSTEM_ROLE,
                                item.getRolename()));
                    } else {
                        keyList.add(item);
                    }
                }
            }

            if (keyList.size() > 0) {
                roleService.massRemoveWithSession(keyList, UserUIContext.getUsername(), AppUI.getAccountId());
                doSearch(searchCriteria);
            }
        } else {
            roleService.removeByCriteria(searchCriteria, AppUI.getAccountId());
            doSearch(searchCriteria);
        }
    }

    @Override
    protected void onGo(HasComponents container, ScreenData<?> data) {
        if (UserUIContext.canRead(RolePermissionCollections.ACCOUNT_ROLE)) {
            RoleContainer roleContainer = (RoleContainer) container;
            roleContainer.removeAllComponents();
            roleContainer.addComponent(view);
            searchCriteria = (RoleSearchCriteria) data.getParams();
            doSearch(searchCriteria);

            AccountSettingBreadcrumb breadcrumb = ViewManager.getCacheComponent(AccountSettingBreadcrumb.class);
            breadcrumb.gotoRoleList();
        } else {
            NotificationUtil.showMessagePermissionAlert();
        }
    }

    @Override
    public ISearchableService<RoleSearchCriteria> getSearchService() {
        return AppContextUtil.getSpringBean(RoleService.class);
    }
}
