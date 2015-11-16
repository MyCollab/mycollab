/**
 * This file is part of mycollab-dao.
 *
 * mycollab-dao is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-dao is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-dao.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.core.db.query;

import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.core.arguments.SearchCriteria;
import com.esofthead.mycollab.core.arguments.SearchField;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 4.0
 */
public class SearchFieldInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String prefixOper;
    private Param param;
    private String compareOper;
    private VariableInjecter variableInjecter;

    public SearchFieldInfo() {
        this(SearchField.AND, null, null, null);
    }

    public SearchFieldInfo(Param param, String compareOper, Object value) {
        this(SearchField.AND, param, compareOper, value);
    }

    public SearchFieldInfo(String prefixOper, Param param, String compareOper, Object value) {
        this.prefixOper = prefixOper;
        this.param = param;
        this.compareOper = compareOper;
        if (value instanceof VariableInjecter) {
            this.variableInjecter = (VariableInjecter) value;
        } else {
            variableInjecter = new ConstantValueInjecter(value);
        }
    }

    public static SearchFieldInfo inCollection(PropertyListParam param, Object value) {
        return new SearchFieldInfo(SearchField.AND, param, PropertyListParam.BELONG_TO, value);
    }

    public static SearchFieldInfo inDateRange(DateParam param, Object value) {
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

    public VariableInjecter getVariableInjecter() {
        return variableInjecter;
    }

    public void setVariableInjecter(VariableInjecter variableInjecter) {
        this.variableInjecter = variableInjecter;
    }

    public Object eval() {
        return variableInjecter.eval();
    }

    public static <S extends SearchCriteria> S buildSearchCriteria(Class<S> cls, List<SearchFieldInfo> fieldInfos) {
        try {
            S obj = cls.newInstance();
            for (SearchFieldInfo info : fieldInfos) {
                Param param = info.getParam();
                SearchField searchField;
                if (param instanceof StringParam) {
                    StringParam wrapParam = (StringParam) param;
                    searchField = wrapParam.buildSearchField(info.getPrefixOper(), info.getCompareOper(),
                            (String) info.eval());
                    obj.addExtraField(searchField);
                } else if (param instanceof NumberParam) {
                    NumberParam wrapParam = (NumberParam) param;
                    searchField = wrapParam.buildSearchField(info.getPrefixOper(), info.getCompareOper(),
                            Double.parseDouble((String) info.eval()));
                    obj.addExtraField(searchField);
                } else if (param instanceof CompositionStringParam) {
                    CompositionStringParam wrapParam = (CompositionStringParam) param;
                    searchField = wrapParam.buildSearchField(info.getPrefixOper(), info.getCompareOper(),
                            (String) info.eval());
                    obj.addExtraField(searchField);
                } else if (param instanceof StringListParam) {
                    StringListParam listParam = (StringListParam) param;
                    if (info.getCompareOper().equals(StringListParam.IN)) {
                        searchField = listParam.buildStringParamInList(info.getPrefixOper(), (Collection<String>) info.eval());
                    } else {
                        searchField = listParam.buildStringParamNotInList(info.getPrefixOper(), (Collection<String>) info.eval());
                    }
                    obj.addExtraField(searchField);
                } else if (param instanceof DateParam) {
                    DateParam dateParam = (DateParam) param;
                    Object value = info.eval();
                    if (value.getClass().isArray()) {
                        Date val1 = (Date) Array.get(value, 0);
                        Date val2 = (Date) Array.get(value, 1);
                        searchField = dateParam.buildSearchField(info.getPrefixOper(), info.getCompareOper(), val1, val2);
                    } else {
                        searchField = dateParam.buildSearchField(info.getPrefixOper(), info.getCompareOper(), (Date) value);
                    }
                    obj.addExtraField(searchField);
                } else if (param instanceof PropertyListParam) {
                    PropertyListParam listParam = (PropertyListParam) param;
                    switch (info.getCompareOper()) {
                        case PropertyListParam.BELONG_TO:
                            searchField = listParam.buildPropertyParamInList(info.getPrefixOper(), (Collection) info.eval());
                            break;
                        case PropertyListParam.NOT_BELONG_TO:
                            searchField = listParam.buildPropertyParamNotInList(info.getPrefixOper(), (Collection) info.eval());
                            break;
                        default:
                            throw new MyCollabException("Not support yet");
                    }
                    obj.addExtraField(searchField);
                } else {
                    throw new IllegalArgumentException("Not support field: " + param);
                }
            }
            return obj;
        } catch (Exception e) {
            throw new MyCollabException(e);
        }
    }
}
