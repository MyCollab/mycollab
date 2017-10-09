package com.mycollab.module.project.view.user;

import com.mycollab.db.arguments.SetSearchField;
import com.mycollab.db.arguments.StringSearchField;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.domain.ProjectGenericItem;
import com.mycollab.module.project.domain.criteria.ProjectGenericItemSearchCriteria;
import com.mycollab.module.project.i18n.ProjectI18nEnum;
import com.mycollab.module.project.service.ProjectGenericItemService;
import com.mycollab.module.project.ui.components.GenericItemRowDisplayHandler;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.mvp.AbstractVerticalPageView;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.web.ui.DefaultBeanPagedList;
import com.vaadin.server.FontAwesome;

/**
 * @author MyCollab Ltd.
 * @since 5.0.3
 */
@ViewComponent
public class ProjectSearchItemsViewImpl extends AbstractVerticalPageView implements ProjectSearchItemsView {

    public ProjectSearchItemsViewImpl() {
        this.withMargin(true);
    }

    @Override
    public void displayResults(String value) {
        this.removeAllComponents();

        ELabel headerLbl = ELabel.h2("");

        DefaultBeanPagedList<ProjectGenericItemService, ProjectGenericItemSearchCriteria, ProjectGenericItem>
                searchItemsTable = new DefaultBeanPagedList<>(AppContextUtil.getSpringBean(ProjectGenericItemService.class),
                new GenericItemRowDisplayHandler());
        searchItemsTable.setControlStyle("borderlessControl");

        this.with(headerLbl, searchItemsTable);
        ProjectGenericItemSearchCriteria criteria = new ProjectGenericItemSearchCriteria();
        criteria.setPrjKeys(new SetSearchField<>(CurrentProjectVariables.getProjectId()));
        criteria.setTxtValue(StringSearchField.and(value));
        int foundNum = searchItemsTable.setSearchCriteria(criteria);
        headerLbl.setValue(String.format(FontAwesome.SEARCH.getHtml() + " " + UserUIContext.getMessage(ProjectI18nEnum.OPT_SEARCH_TERM)
                , value, foundNum));
    }
}
