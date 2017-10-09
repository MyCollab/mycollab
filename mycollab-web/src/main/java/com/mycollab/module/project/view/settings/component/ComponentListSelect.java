package com.mycollab.module.project.view.settings.component;

import com.mycollab.common.i18n.OptionI18nEnum.StatusI18nEnum;
import com.mycollab.db.arguments.BasicSearchRequest;
import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.db.arguments.StringSearchField;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.tracker.domain.Component;
import com.mycollab.module.tracker.domain.criteria.ComponentSearchCriteria;
import com.mycollab.module.tracker.service.ComponentService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.web.ui.IntegerKeyListSelect;

import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 4.5.0
 */
public class ComponentListSelect extends IntegerKeyListSelect {
    private static final long serialVersionUID = 1L;

    public ComponentListSelect() {
        this.setItemCaptionMode(ItemCaptionMode.EXPLICIT);
        this.setMultiSelect(true);

        ComponentSearchCriteria searchCriteria = new ComponentSearchCriteria();
        searchCriteria.setStatus(StringSearchField.and(StatusI18nEnum.Open.name()));
        searchCriteria.setProjectId(NumberSearchField.equal(CurrentProjectVariables.getProjectId()));

        ComponentService componentService = AppContextUtil.getSpringBean(ComponentService.class);
        List<Component> components = (List<Component>) componentService.findPageableListByCriteria(new BasicSearchRequest<>(searchCriteria));
        for (Component component : components) {
            this.addItem(component.getId());
            this.setItemCaption(component.getId(), component.getName());
        }

        this.setRows(4);
    }
}
