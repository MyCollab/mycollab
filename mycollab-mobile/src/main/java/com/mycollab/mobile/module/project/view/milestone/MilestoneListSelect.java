package com.mycollab.mobile.module.project.view.milestone;

import com.mycollab.db.arguments.BasicSearchRequest;
import com.mycollab.db.arguments.SetSearchField;
import com.mycollab.mobile.ui.ValueListSelect;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.domain.SimpleMilestone;
import com.mycollab.module.project.domain.criteria.MilestoneSearchCriteria;
import com.mycollab.module.project.service.MilestoneService;
import com.mycollab.spring.AppContextUtil;
import com.vaadin.data.util.BeanContainer;

import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class MilestoneListSelect extends ValueListSelect {
    private static final long serialVersionUID = 1L;

    public MilestoneListSelect() {
        this.setItemCaptionMode(ItemCaptionMode.PROPERTY);

        MilestoneSearchCriteria criteria = new MilestoneSearchCriteria();
        criteria.setProjectIds(new SetSearchField<>(CurrentProjectVariables.getProjectId()));

        MilestoneService milestoneService = AppContextUtil.getSpringBean(MilestoneService.class);
        List<SimpleMilestone> milestones = (List<SimpleMilestone>) milestoneService.findPageableListByCriteria(new BasicSearchRequest<>(criteria));

        BeanContainer<String, SimpleMilestone> beanItem = new BeanContainer<>(SimpleMilestone.class);
        beanItem.setBeanIdProperty("id");

        for (SimpleMilestone milestone : milestones) {
            beanItem.addBean(milestone);
        }

        this.setContainerDataSource(beanItem);
        this.setItemCaptionPropertyId("name");
    }
}
