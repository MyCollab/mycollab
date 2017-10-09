package com.mycollab.module.user.view.component;

import com.mycollab.db.arguments.BasicSearchRequest;
import com.mycollab.module.user.accountsettings.localization.RoleI18nEnum;
import com.mycollab.module.user.domain.SimpleRole;
import com.mycollab.module.user.domain.criteria.RoleSearchCriteria;
import com.mycollab.module.user.service.RoleService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.UserUIContext;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.ui.ComboBox;

import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class RoleComboBox extends ComboBox {
    private static final long serialVersionUID = 1L;

    public RoleComboBox() {
        this.setNullSelectionAllowed(false);
        this.setItemCaptionMode(ItemCaptionMode.PROPERTY);

        RoleSearchCriteria criteria = new RoleSearchCriteria();

        RoleService roleService = AppContextUtil.getSpringBean(RoleService.class);
        List<SimpleRole> roles = (List<SimpleRole>) roleService.findPageableListByCriteria(new BasicSearchRequest<>(criteria));

        BeanContainer<String, SimpleRole> beanItem = new BeanContainer<>(SimpleRole.class);
        beanItem.setBeanIdProperty("id");
        this.setContainerDataSource(beanItem);
        this.setItemCaptionPropertyId("rolename");

        SimpleRole ownerRole = new SimpleRole();
        ownerRole.setId(-1);
        ownerRole.setRolename(UserUIContext.getMessage(RoleI18nEnum.OPT_ACCOUNT_OWNER));
        beanItem.addBean(ownerRole);

        for (SimpleRole role : roles) {
            beanItem.addBean(role);
            if (Boolean.TRUE.equals(role.getIsdefault())) {
                this.setValue(role.getId());
            }
        }
    }
}
