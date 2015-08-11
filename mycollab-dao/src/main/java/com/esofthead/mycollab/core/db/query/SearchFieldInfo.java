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
import com.esofthead.mycollab.core.utils.BeanUtility;

import java.io.Serializable;
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

    private Object value;

    private String paramClsName;

    public SearchFieldInfo() {
    }

    public SearchFieldInfo(String prefixOper, Param param, String compareOper, Object value) {
        this.prefixOper = prefixOper;
        this.param = param;
        this.compareOper = compareOper;
        this.value = value;
        this.paramClsName = param.getClass().getName();
    }

    public String getPrefixOper() {
        return prefixOper;
    }

    public void setPrefixOper(String prefixOper) {
        this.prefixOper = prefixOper;
    }

    public Param getParam() {
        return param;
    }

    public void setParam(Param param) {
        this.param = param;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getCompareOper() {
        return compareOper;
    }

    public void setCompareOper(String compareOper) {
        this.compareOper = compareOper;
    }

    public String getParamClsName() {
        return paramClsName;
    }

    public void setParamClsName(String paramClsName) {
        this.paramClsName = paramClsName;
    }

    public static <S extends SearchCriteria> S buildSearchCriteria(Class<S> cls, List<SearchFieldInfo> fieldInfos) {
        try {
            S obj = cls.newInstance();
            for (SearchFieldInfo info : fieldInfos) {
                Param param = info.getParam();
                if (param instanceof StringParam) {
                    StringParam wrapParam = (StringParam) param;
                    SearchField searchField = wrapParam.buildSearchField(info.getPrefixOper(), info.getCompareOper(),
                            (String) info.getValue());
                    obj.addExtraField(searchField);
                } else if (param instanceof NumberParam) {
                    NumberParam wrapParam = (NumberParam) param;
                    SearchField searchField =  wrapParam.buildSearchField(info.getPrefixOper(), info.getCompareOper(),
                            Double.parseDouble((String)info.getValue()));
                    obj.addExtraField(searchField);
                } else if (param instanceof CompositionStringParam) {
                    CompositionStringParam wrapParam = (CompositionStringParam) param;
                    SearchField searchField =  wrapParam.buildSearchField(info.getPrefixOper(), info.getCompareOper(), (String)info.getValue());
                    obj.addExtraField(searchField);
                }
            }
            return obj;
        } catch (Exception e) {
            throw new MyCollabException(e);
        }
    }
}
