/**
 * Copyright © MyCollab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.vaadin.web.ui;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.common.json.QueryAnalyzer;
import com.mycollab.common.domain.SaveSearchResult;
import com.mycollab.common.domain.criteria.SaveSearchResultCriteria;
import com.mycollab.common.service.SaveSearchResultService;
import com.mycollab.db.arguments.BasicSearchRequest;
import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.db.arguments.SearchCriteria;
import com.mycollab.db.arguments.StringSearchField;
import com.mycollab.db.query.SearchFieldInfo;
import com.mycollab.db.query.SearchQueryInfo;
import com.mycollab.vaadin.EventBusFactory;
import com.mycollab.shell.event.ShellEvent;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.UserUIContext;
import com.vaadin.ui.*;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.hene.popupbutton.PopupButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author MyCollab Ltd
 * @since 5.1.1
 */
public abstract class SavedFilterComboBox extends CustomField<String> {
    private static Logger LOG = LoggerFactory.getLogger(SavedFilterComboBox.class);

    protected TextField componentsText;
    protected String selectedQueryName = "Custom";
    private PopupButton componentPopupSelection;
    private OptionPopupContent popupContent;
    private List<SearchQueryInfo> sharedQueries;
    private List<SearchQueryInfo> savedQueries;

    public SavedFilterComboBox(String type) {
        this(type, null);
    }

    public SavedFilterComboBox(String type, List<SearchQueryInfo> sharedQueries) {
        this.sharedQueries = sharedQueries;

        SaveSearchResultCriteria searchCriteria = new SaveSearchResultCriteria();
        searchCriteria.setType(StringSearchField.and(type));
        searchCriteria.setCreateUser(StringSearchField.and(UserUIContext.getUsername()));
        searchCriteria.setSaccountid(new NumberSearchField(AppUI.getAccountId()));

        SaveSearchResultService saveSearchResultService = AppContextUtil.getSpringBean(SaveSearchResultService.class);
        List<SaveSearchResult> savedSearchResults = (List<SaveSearchResult>)saveSearchResultService.findPageableListByCriteria(new BasicSearchRequest<>(searchCriteria));
        savedQueries = new ArrayList<>();
        for (SaveSearchResult searchResultWithBLOBs : savedSearchResults) {
            try {
                List fieldInfos = QueryAnalyzer.toSearchFieldInfos(searchResultWithBLOBs.getQuerytext(), type);
                savedQueries.add(new SearchQueryInfo(searchResultWithBLOBs.getQueryname(), fieldInfos));
            } catch (Exception e) {
                LOG.error("Invalid query", e);
            }
        }
    }

    public void addSharedSearchQueryInfo(SearchQueryInfo searchQueryInfo) {
        if (sharedQueries == null) {
            sharedQueries = new ArrayList<>();
        }
        sharedQueries.add(searchQueryInfo);
    }

    public void selectQueryInfo(String queryId) {
        if (sharedQueries != null) {
            for (SearchQueryInfo queryInfo : sharedQueries) {
                if (queryId.equals(queryInfo.getQueryId())) {
                    selectedQueryName = queryInfo.getQueryName();
                    updateQueryNameField(selectedQueryName);
                    fireEvent(new QuerySelectEvent(this, queryInfo.getSearchFieldInfos()));
                    EventBusFactory.getInstance().post(new ShellEvent.AddQueryParam(this, queryInfo.getSearchFieldInfos()));
                    componentPopupSelection.setPopupVisible(false);
                }
            }
        }
    }

    @Override
    protected Component initContent() {
        componentsText = new TextField();
        componentsText.setNullRepresentation("");
        componentsText.setReadOnly(true);
        componentsText.addStyleName("noBorderRight");
        componentsText.setWidth("100%");
        componentPopupSelection = new PopupButton();
        componentPopupSelection.addStyleName(WebThemes.MULTI_SELECT_BG);
        componentPopupSelection.setDirection(Alignment.BOTTOM_LEFT);
        componentPopupSelection.addClickListener(clickEvent -> initContentPopup());

        popupContent = new OptionPopupContent();
        componentPopupSelection.setContent(popupContent);

        MHorizontalLayout content = new MHorizontalLayout(componentsText).withAlign(componentsText, Alignment.MIDDLE_LEFT);

        MHorizontalLayout multiSelectComp = new MHorizontalLayout(componentsText, componentPopupSelection)
                .withSpacing(false).expand(componentsText);
        content.with(multiSelectComp);
        return content;
    }

    private void initContentPopup() {
        popupContent.removeOptions();
        popupContent.addSection(UserUIContext.getMessage(GenericI18Enum.OPT_CREATED_BY_USERS));
        for (final SearchQueryInfo queryInfo : savedQueries) {
            Button queryOption = new QueryInfoOption(queryInfo);
            popupContent.addOption(queryOption);
        }

        if (CollectionUtils.isNotEmpty(sharedQueries)) {
            popupContent.addSection(UserUIContext.getMessage(GenericI18Enum.OPT_SHARED_TO_ME));
            for (final SearchQueryInfo queryInfo : sharedQueries) {
                Button queryOption = new QueryInfoOption(queryInfo);
                popupContent.addOption(queryOption);
            }
        }
    }

    private void updateQueryNameField(String value) {
        componentsText.setReadOnly(false);
        componentsText.setValue(value);
        componentsText.setReadOnly(true);
    }

    private class QueryInfoOption extends Button {
        QueryInfoOption(final SearchQueryInfo queryInfo) {
            super("      " + queryInfo.getQueryName());
            addClickListener(clickEvent -> {
                selectedQueryName = queryInfo.getQueryName();
                updateQueryNameField(selectedQueryName);
                SavedFilterComboBox.this.fireEvent(new QuerySelectEvent(SavedFilterComboBox.this, queryInfo.getSearchFieldInfos()));
                componentPopupSelection.setPopupVisible(false);
            });
        }
    }


    @Override
    public Class<? extends String> getType() {
        return String.class;
    }

    public void addQuerySelectListener(QuerySelectListener listener) {
        addListener(QuerySelectEvent.class, listener, QUERY_SELECT);
    }

    private static final Method QUERY_SELECT;

    static {
        try {
            QUERY_SELECT = QuerySelectListener.class.getDeclaredMethod("querySelect", QuerySelectEvent.class);
        } catch (final NoSuchMethodException e) {
            // This should never happen
            throw new java.lang.RuntimeException("Internal error finding methods in AbstractField");
        }
    }

    public static class QuerySelectEvent<S extends SearchCriteria> extends Component.Event {
        private List<SearchFieldInfo<S>> searchFieldInfos;

        QuerySelectEvent(Component source, List<SearchFieldInfo<S>> searchFieldInfos) {
            super(source);
            this.searchFieldInfos = searchFieldInfos;
        }

        public List<SearchFieldInfo<S>> getSearchFieldInfos() {
            return searchFieldInfos;
        }
    }

    public interface QuerySelectListener<S extends SearchCriteria> extends Serializable {
        void querySelect(QuerySelectEvent<S> querySelectEvent);
    }
}
