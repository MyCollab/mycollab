package com.mycollab.module.project.view.settings.component;

import com.mycollab.common.i18n.OptionI18nEnum;
import com.mycollab.db.arguments.BasicSearchRequest;
import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.db.arguments.StringSearchField;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.tracker.domain.Component;
import com.mycollab.module.tracker.domain.criteria.ComponentSearchCriteria;
import com.mycollab.module.tracker.service.ComponentService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.web.ui.MultiSelectComp;
import com.vaadin.data.Property;
import com.vaadin.ui.UI;

import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class ComponentMultiSelectField extends MultiSelectComp {
    private static final long serialVersionUID = 1L;

    public ComponentMultiSelectField() {
        super("name", true);
    }

    @Override
    protected List<Component> createData() {
        ComponentSearchCriteria searchCriteria = new ComponentSearchCriteria();
        searchCriteria.setStatus(StringSearchField.and(OptionI18nEnum.StatusI18nEnum.Open.name()));
        searchCriteria.setProjectId(new NumberSearchField(CurrentProjectVariables.getProjectId()));

        ComponentService componentService = AppContextUtil.getSpringBean(ComponentService.class);
        return (List<Component>) componentService.findPageableListByCriteria(new BasicSearchRequest<>(searchCriteria));
    }

    @Override
    protected void requestAddNewComp() {
        UI.getCurrent().addWindow(new ComponentAddWindow());
    }

    @Override
    public void setPropertyDataSource(Property newDataSource) {
        List<Component> components = (List<Component>) newDataSource.getValue();
        if (components != null) {
            this.setSelectedItems(components);
        }
        super.setPropertyDataSource(newDataSource);
    }

    @Override
    public Class<?> getType() {
        return Object.class;
    }
}
