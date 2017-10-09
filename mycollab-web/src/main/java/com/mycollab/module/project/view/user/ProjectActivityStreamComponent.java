package com.mycollab.module.project.view.user;

import com.mycollab.common.ModuleNameConstants;
import com.mycollab.common.domain.criteria.ActivityStreamSearchCriteria;
import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.db.arguments.SetSearchField;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.vaadin.AppUI;
import fi.jasoft.dragdroplayouts.DDCssLayout;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class ProjectActivityStreamComponent extends DDCssLayout {
    private static final long serialVersionUID = 1L;

    public void showProjectFeeds() {
        this.removeAllComponents();
        ProjectActivityStreamPagedList activityStreamList = new ProjectActivityStreamPagedList();
        this.addComponent(activityStreamList);
        ActivityStreamSearchCriteria searchCriteria = new ActivityStreamSearchCriteria();
        searchCriteria.setModuleSet(new SetSearchField<>(ModuleNameConstants.PRJ));
        searchCriteria.setSaccountid(new NumberSearchField(AppUI.getAccountId()));
        searchCriteria.setExtraTypeIds(new SetSearchField<>(CurrentProjectVariables.getProjectId()));
        activityStreamList.setSearchCriteria(searchCriteria);
    }
}
