package com.mycollab.vaadin.web.ui;

import com.mycollab.db.arguments.SearchCriteria;
import com.mycollab.db.query.SearchFieldInfo;

import java.util.List;

/**
 * @author MyCollab Ltd
 * @since 5.4.4
 */
public interface CriteriaBuilderComponent<S extends SearchCriteria> {
    List<SearchFieldInfo<S>> buildSearchFieldInfos();
}
