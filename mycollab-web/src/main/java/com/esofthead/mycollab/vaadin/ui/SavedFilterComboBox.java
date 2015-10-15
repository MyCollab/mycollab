/**
 * This file is part of mycollab-web.
 *
 * mycollab-web is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-web is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-web.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.vaadin.ui;

import com.esofthead.mycollab.common.domain.SaveSearchResultWithBLOBs;
import com.esofthead.mycollab.common.domain.criteria.SaveSearchResultCriteria;
import com.esofthead.mycollab.common.service.SaveSearchResultService;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchRequest;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.core.db.query.SearchFieldInfo;
import com.esofthead.mycollab.core.db.query.SearchQueryInfo;
import com.esofthead.mycollab.core.utils.XStreamJsonDeSerializer;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
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
public class SavedFilterComboBox extends CustomField<String> {
    private static Logger LOG = LoggerFactory.getLogger(SavedFilterComboBox.class);

    private TextField componentsText;
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
        searchCriteria.setType(new StringSearchField(type));
        searchCriteria.setCreateUser(new StringSearchField(AppContext.getUsername()));
        searchCriteria.setSaccountid(new NumberSearchField(AppContext.getAccountId()));

        SaveSearchResultService saveSearchResultService = ApplicationContextUtil.getSpringBean(SaveSearchResultService.class);
        List<SaveSearchResultWithBLOBs> savedSearchResults = saveSearchResultService.findPagableListByCriteria(new
                SearchRequest<>(searchCriteria, 0, Integer.MAX_VALUE));
        savedQueries = new ArrayList<>();
        for (SaveSearchResultWithBLOBs searchResultWithBLOBs : savedSearchResults) {
            try {
                List fieldInfos = (List) XStreamJsonDeSerializer.fromJson(searchResultWithBLOBs.getQuerytext());
                // @HACK: === the library serialize with extra list
                // wrapper
                if (CollectionUtils.isEmpty(fieldInfos)) {
                    LOG.error("Can not parse query " + searchResultWithBLOBs.getQuerytext() + " of type " + type);
                    continue;
                }
                List<SearchFieldInfo> searchFieldInfos = (List<SearchFieldInfo>) fieldInfos.get(0);
                savedQueries.add(new SearchQueryInfo(searchResultWithBLOBs.getQueryname(), searchFieldInfos));
            } catch (Exception e) {
                continue;
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
        componentPopupSelection.addClickListener(new Button.ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(final Button.ClickEvent event) {
                SavedFilterComboBox.this.initContentPopup();
            }
        });

        popupContent = new OptionPopupContent();
        componentPopupSelection.setContent(popupContent);

        MHorizontalLayout content = new MHorizontalLayout().withSpacing(true).with(componentsText)
                .withAlign(componentsText, Alignment.MIDDLE_LEFT);

        componentPopupSelection.addStyleName(UIConstants.MULTI_SELECT_BG);
        componentPopupSelection.setWidth("25px");
        componentPopupSelection.setDirection(Alignment.TOP_LEFT);

        MHorizontalLayout multiSelectComp = new MHorizontalLayout().withSpacing(false).with(componentsText, componentPopupSelection)
                .expand(componentsText);
        content.with(multiSelectComp);
        return content;
    }

    private void initContentPopup() {
        popupContent.removeOptions();
        if (CollectionUtils.isNotEmpty(sharedQueries)) {
            popupContent.addSection("Shared to me");
            for (final SearchQueryInfo queryInfo : sharedQueries) {
                Button queryOption = new QueryInfoOption(queryInfo);
                popupContent.addOption(queryOption);
            }
        }

        popupContent.addSection("Created by users");
        for (final SearchQueryInfo queryInfo : savedQueries) {
            Button queryOption = new QueryInfoOption(queryInfo);
            popupContent.addOption(queryOption);
        }
    }

    private class QueryInfoOption extends Button {
        QueryInfoOption(final SearchQueryInfo queryInfo) {
            super("      " + queryInfo.getQueryName(), new ClickListener() {
                @Override
                public void buttonClick(ClickEvent event) {
                    SavedFilterComboBox.this.fireEvent(new QuerySelectEvent(SavedFilterComboBox.this, queryInfo
                            .getSearchFieldInfos()));
                    componentPopupSelection.setPopupVisible(false);
                }
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
            QUERY_SELECT = QuerySelectListener.class.getDeclaredMethod("querySelect", new Class[]{QuerySelectEvent.class});
        } catch (final java.lang.NoSuchMethodException e) {
            // This should never happen
            throw new java.lang.RuntimeException("Internal error finding methods in AbstractField");
        }
    }

    public static class QuerySelectEvent extends Component.Event {
        private List<SearchFieldInfo> searchFieldInfos;

        public QuerySelectEvent(Component source, List<SearchFieldInfo> searchFieldInfos) {
            super(source);
            this.searchFieldInfos = searchFieldInfos;
        }

        public List<SearchFieldInfo> getSearchFieldInfos() {
            return searchFieldInfos;
        }
    }

    public interface QuerySelectListener extends Serializable {
        void querySelect(QuerySelectEvent querySelectEvent);
    }
}
