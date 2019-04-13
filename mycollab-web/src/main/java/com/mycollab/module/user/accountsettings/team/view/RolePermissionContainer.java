package com.mycollab.module.user.accountsettings.team.view;

import com.mycollab.common.i18n.SecurityI18nEnum;
import com.mycollab.form.view.LayoutType;
import com.mycollab.module.project.i18n.RolePermissionI18nEnum;
import com.mycollab.module.user.accountsettings.localization.RoleI18nEnum;
import com.mycollab.module.user.domain.SimpleRole;
import com.mycollab.security.PermissionDefItem;
import com.mycollab.security.PermissionFlag;
import com.mycollab.security.PermissionMap;
import com.mycollab.security.RolePermissionCollections;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.ui.FormContainer;
import com.mycollab.vaadin.web.ui.grid.GridFormLayoutHelper;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.util.List;

public class RolePermissionContainer extends VerticalLayout {
    private MVerticalLayout permissionLayout;

    RolePermissionContainer() {
        this.setMargin(new MarginInfo(true, false, false, false));
        this.addComponent(ELabel.h2(UserUIContext.getMessage(RolePermissionI18nEnum.LIST)));
        permissionLayout = new MVerticalLayout().withMargin(false);
        this.addComponent(permissionLayout);
    }

    public void displayRolePermission(SimpleRole role) {
        permissionLayout.removeAllComponents();
        PermissionMap permissionMap = (role != null) ? role.getPermissionMap() : PermissionMap.ADMIN_ROLE_MAP;

        if (permissionMap != null) {
            permissionLayout.addComponent(constructPermissionSectionView(UserUIContext.getMessage(RoleI18nEnum.SECTION_PROJECT_MANAGEMENT_TITLE),
                    permissionMap, RolePermissionCollections.PROJECT_PERMISSION_ARR));

            permissionLayout.addComponent(constructPermissionSectionView(UserUIContext.getMessage(RoleI18nEnum.SECTION_ACCOUNT_MANAGEMENT_TITLE),
                    permissionMap, RolePermissionCollections.ACCOUNT_PERMISSION_ARR));
        }
    }

    private ComponentContainer constructPermissionSectionView(String depotTitle, PermissionMap permissionMap,
                                                              List<PermissionDefItem> defItems) {
        GridFormLayoutHelper formHelper = GridFormLayoutHelper.defaultFormLayoutHelper(LayoutType.TWO_COLUMN);
        FormContainer permissionsPanel = new FormContainer();

        for (int i = 0; i < defItems.size(); i++) {
            PermissionDefItem permissionDefItem = defItems.get(i);
            Integer flag = permissionMap.getPermissionFlag(permissionDefItem.getKey());
            SecurityI18nEnum permissionVal = PermissionFlag.toVal(flag);
            formHelper.addComponent(new Label(UserUIContext.getMessage(permissionVal)), UserUIContext.getMessage(permissionDefItem.getCaption()),
                    UserUIContext.getMessage(permissionVal.desc()), i % 2, i / 2);
        }
        permissionsPanel.addSection(depotTitle, formHelper.getLayout());
        return permissionsPanel;
    }
}
