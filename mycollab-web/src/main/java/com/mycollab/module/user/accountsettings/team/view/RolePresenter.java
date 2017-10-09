package com.mycollab.module.user.accountsettings.team.view;

import com.mycollab.module.user.accountsettings.localization.RoleI18nEnum;
import com.mycollab.module.user.accountsettings.view.parameters.RoleScreenData;
import com.mycollab.module.user.domain.criteria.RoleSearchCriteria;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.mvp.ScreenData;
import com.mycollab.vaadin.web.ui.AbstractPresenter;
import com.vaadin.ui.HasComponents;

import static com.mycollab.vaadin.mvp.PresenterResolver.getPresenter;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class RolePresenter extends AbstractPresenter<RoleContainer> {
    private static final long serialVersionUID = 1L;

    public RolePresenter() {
        super(RoleContainer.class);
    }

    @Override
    protected void onGo(HasComponents container, ScreenData<?> data) {
        UserPermissionManagementView groupContainer = (UserPermissionManagementView) container;
        groupContainer.gotoSubView(UserUIContext.getMessage(RoleI18nEnum.LIST));

        if (data == null) {
            RoleListPresenter listPresenter = getPresenter(RoleListPresenter.class);
            RoleSearchCriteria criteria = new RoleSearchCriteria();
            listPresenter.go(view, new ScreenData.Search<>(criteria));
        } else if (data instanceof RoleScreenData.Add || data instanceof RoleScreenData.Edit) {
            RoleAddPresenter presenter = getPresenter(RoleAddPresenter.class);
            presenter.go(view, data);
        } else if (data instanceof RoleScreenData.Read) {
            RoleReadPresenter presenter = getPresenter(RoleReadPresenter.class);
            presenter.go(view, data);
        } else if (data instanceof RoleScreenData.Search) {
            RoleListPresenter presenter = getPresenter(RoleListPresenter.class);
            presenter.go(view, data);
        }
    }
}
