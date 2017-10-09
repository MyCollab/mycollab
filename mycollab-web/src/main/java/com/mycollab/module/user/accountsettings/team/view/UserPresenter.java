package com.mycollab.module.user.accountsettings.team.view;

import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.db.arguments.SetSearchField;
import com.mycollab.module.billing.RegisterStatusConstants;
import com.mycollab.module.user.accountsettings.localization.UserI18nEnum;
import com.mycollab.module.user.accountsettings.view.parameters.UserScreenData;
import com.mycollab.module.user.domain.criteria.UserSearchCriteria;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.mvp.PresenterResolver;
import com.mycollab.vaadin.mvp.ScreenData;
import com.mycollab.vaadin.web.ui.AbstractPresenter;
import com.vaadin.ui.HasComponents;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
public class UserPresenter extends AbstractPresenter<UserContainer> {
    private static final long serialVersionUID = 1L;

    public UserPresenter() {
        super(UserContainer.class);
    }

    @Override
    protected void onGo(HasComponents container, ScreenData<?> data) {
        UserPermissionManagementView groupContainer = (UserPermissionManagementView) container;
        groupContainer.gotoSubView(UserUIContext.getMessage(UserI18nEnum.LIST));

        if (data == null) {
            UserListPresenter listPresenter = PresenterResolver.getPresenter(UserListPresenter.class);
            UserSearchCriteria criteria = new UserSearchCriteria();
            criteria.setSaccountid(new NumberSearchField(AppUI.getAccountId()));
            criteria.setRegisterStatuses(new SetSearchField(RegisterStatusConstants.ACTIVE, RegisterStatusConstants.NOT_LOG_IN_YET));
            listPresenter.go(view, new ScreenData.Search<>(criteria));
        } else if (data instanceof UserScreenData.Read) {
            UserReadPresenter presenter = PresenterResolver.getPresenter(UserReadPresenter.class);
            presenter.go(view, data);
        } else if (data instanceof UserScreenData.Search) {
            UserListPresenter presenter = PresenterResolver.getPresenter(UserListPresenter.class);
            presenter.go(view, data);
        } else if (data instanceof UserScreenData.Add || data instanceof UserScreenData.Edit) {
            UserAddPresenter presenter = PresenterResolver.getPresenter(UserAddPresenter.class);
            presenter.go(view, data);
        }
    }
}
