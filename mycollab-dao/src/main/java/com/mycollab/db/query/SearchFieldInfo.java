/**
 * Copyright © MyCollab
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.db.query;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mycollab.common.i18n.QueryI18nEnum.CollectionI18nEnum;
import com.mycollab.core.MyCollabException;
import com.mycollab.core.utils.BeanUtility;
import com.mycollab.core.utils.StringUtils;
import com.mycollab.db.arguments.SearchCriteria;
import com.mycollab.db.arguments.SearchField;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import static com.mycollab.common.i18n.QueryI18nEnum.CollectionI18nEnum.IN;
import static com.mycollab.common.i18n.QueryI18nEnum.CollectionI18nEnum.valueOf;

/**
 * @author MyCollab Ltd.
 * @since 4.0
 */
public class SearchFieldInfo<S extends SearchCriteria> implements Serializable {
    private static final long serialVersionUID = 1L;

    private String prefixOper;

    private Param param;

    private String compareOper;

    private VariableInjector variableInjector;

    @JsonCreator
    public SearchFieldInfo(@JsonProperty("prefixOper") String prefixOper, @JsonProperty("param") Param param,
                           @JsonProperty("compareOper") String compareOper,
                           @JsonProperty("variableInjector") VariableInjector value) {
        this.prefixOper = prefixOper;
        this.param = param;
        this.compareOper = compareOper;
        this.variableInjector = value;
    }

    public static SearchFieldInfo inCollection(PropertyListParam param, VariableInjector value) {
        return new SearchFieldInfo(SearchField.AND, param, CollectionI18nEnum.IN.name(), value);
    }

    public static SearchFieldInfo inCollection(I18nStringListParam param, VariableInjector value) {
        return new SearchFieldInfo(SearchField.AND, param, CollectionI18nEnum.IN.name(), value);
    }

    public static SearchFieldInfo notInCollection(I18nStringListParam param, VariableInjector value) {
        return new SearchFieldInfo(SearchField.AND, param, CollectionI18nEnum.NOT_IN.name(), value);
    }

    public static SearchFieldInfo inCollection(StringListParam param, VariableInjector value) {
        return new SearchFieldInfo(SearchField.AND, param, IN.name(), value);
    }

    public static SearchFieldInfo inDateRange(DateParam param, VariableInjector value) {
        return new SearchFieldInfo(SearchField.AND, param, DateParam.BETWEEN, value);
    }

    public String getPrefixOper() {
        return prefixOper;
    }

    public SearchFieldInfo setPrefixOper(String prefixOper) {
        this.prefixOper = prefixOper;
        return this;
    }

    public Param getParam() {
        return param;
    }

    public SearchFieldInfo setParam(Param param) {
        this.param = param;
        return this;
    }

    public String getCompareOper() {
        return compareOper;
    }

    public SearchFieldInfo setCompareOper(String compareOper) {
        this.compareOper = compareOper;
        return this;
    }

    public VariableInjector getVariableInjector() {
        return variableInjector;
    }

    public void setVariableInjector(VariableInjector variableInjector) {
        this.variableInjector = variableInjector;
    }

    public Object eval() {
        return variableInjector.eval();
    }

    public SearchField buildSearchField(S searchCriteria) {
        if (param instanceof StringParam) {
            StringParam wrapParam = (StringParam) param;
            return wrapParam.buildSearchField(prefixOper, compareOper, (String) this.eval());
        } else if (param instanceof StringListParam) {
            StringListParam listParam = (StringListParam) param;
            if (this.getCompareOper().equals(IN.name())) {
                return listParam.buildStringParamInList(prefixOper, (Collection<String>) this.eval());
            } else {
                return listParam.buildStringParamNotInList(prefixOper, (Collection<String>) this.eval());
            }
        } else if (param instanceof I18nStringListParam) {
            I18nStringListParam wrapParam = (I18nStringListParam) param;
            CollectionI18nEnum compareValue = valueOf(compareOper);
            switch (compareValue) {
                case IN:
                    return wrapParam.buildStringParamInList(prefixOper, (Collection<String>) this.eval());
                case NOT_IN:
                    return wrapParam.buildStringParamNotInList(prefixOper, (Collection<String>) this.eval());
                default:
                    throw new MyCollabException("Not support yet");
            }
        } else if (param instanceof BooleanParam) {
            BooleanParam wrapParam = (BooleanParam) param;
            return wrapParam.buildSearchField(prefixOper, compareOper, (String) this.eval());
        } else if (param instanceof NumberParam) {
            NumberParam wrapParam = (NumberParam) param;
            String value = (String) this.eval();
            if (StringUtils.isNotBlank(value)) {
                try {
                    return wrapParam.buildSearchField(prefixOper, compareOper, Double.valueOf(value));
                } catch (Exception e) {
                    return null;
                }
            }
            return null;
        } else if (param instanceof PropertyListParam) {
            PropertyListParam wrapParam = (PropertyListParam) param;
            CollectionI18nEnum compareValue = CollectionI18nEnum.valueOf(compareOper);
            switch (compareValue) {
                case IN:
                    return wrapParam.buildPropertyParamInList(prefixOper, (Collection<?>) this.eval());
                case NOT_IN:
                    return wrapParam.buildPropertyParamNotInList(prefixOper, (Collection<?>) this.eval());
                default:
                    throw new MyCollabException("Not support yet");
            }
        } else if (param instanceof CustomSqlParam) {
            CustomSqlParam wrapParam = (CustomSqlParam) param;
            CollectionI18nEnum compareValue = CollectionI18nEnum.valueOf(compareOper);
            switch (compareValue) {
                case IN:
                    return wrapParam.buildPropertyParamInList(prefixOper, (Collection<String>) this.eval());
                case NOT_IN:
                    return wrapParam.buildPropertyParamNotInList(prefixOper, (Collection<String>) this.eval());
                default:
                    throw new MyCollabException("Not support yet");
            }
        } else if (param instanceof SearchCriteriaBridgeParam) {
            SearchCriteriaBridgeParam wrapParam = (SearchCriteriaBridgeParam) param;
            CollectionI18nEnum compareValue = CollectionI18nEnum.valueOf(compareOper);
            switch (compareValue) {
                case IN:
                    wrapParam.injectCriteriaInList(searchCriteria, prefixOper, (Collection<?>) this.eval());
                    return null;
                case NOT_IN:
                    wrapParam.injectCriteriaNotInList(searchCriteria, prefixOper, (Collection<?>) this.eval());
                    return null;
                default:
                    throw new MyCollabException("Not support yet");
            }
        } else if (param instanceof PropertyParam) {
            PropertyParam wrapParam = (PropertyParam) param;
            return wrapParam.buildSearchField(prefixOper, compareOper, eval());
        } else if (param instanceof CompositionStringParam) {
            CompositionStringParam wrapParam = (CompositionStringParam) param;
            return wrapParam.buildSearchField(prefixOper, compareOper, (String) eval());
        } else if (param instanceof ConcatStringParam) {
            ConcatStringParam wrapParam = (ConcatStringParam) param;
            return wrapParam.buildSearchField(prefixOper, compareOper, (String) eval());
        } else if (param instanceof DateParam) {
            DateParam wrapParam = (DateParam) param;
            Object value = this.eval();
            if (value.getClass().isArray()) {
                Date val1 = (Date) Array.get(value, 0);
                Date val2 = (Date) Array.get(value, 1);
                return wrapParam.buildSearchField(prefixOper, compareOper, val1, val2);
            } else {
                return wrapParam.buildSearchField(prefixOper, compareOper, (Date) value);
            }
        } else {
            throw new MyCollabException("Not support yet");
        }
    }

    public static <S extends SearchCriteria> S buildSearchCriteria(Class<S> cls, List<SearchFieldInfo<S>> fieldInfos) {
        try {
            S searchCriteria = cls.newInstance();
            for (SearchFieldInfo info : fieldInfos) {
                SearchField searchField = info.buildSearchField(searchCriteria);
                if (searchField != null) {
                    searchCriteria.addExtraField(searchField);
                }
            }
            return searchCriteria;
        } catch (Exception e) {
            throw new MyCollabException(e);
        }
    }

    public static <S extends SearchCriteria> S buildSearchCriteria(S searchCriteria, List<SearchFieldInfo<S>> fieldInfos) {
        try {
            S tmpSearchCriteria = BeanUtility.deepClone(searchCriteria);
            for (SearchFieldInfo info : fieldInfos) {
                SearchField searchField = info.buildSearchField(tmpSearchCriteria);
                if (searchField != null) {
                    tmpSearchCriteria.addExtraField(searchField);
                }
            }
            return tmpSearchCriteria;
        } catch (Exception e) {
            throw new MyCollabException(e);
        }
    }
}
