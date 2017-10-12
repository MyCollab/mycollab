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
package com.mycollab.common.json;

import com.mycollab.common.TableViewField;
import com.mycollab.core.MyCollabException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author MyCollab Ltd
 * @since 5.3.1
 */
public class FieldDefAnalyzer {
    private static Logger LOG = LoggerFactory.getLogger(FieldDefAnalyzer.class);

    public static List<TableViewField> toTableFields(String jsonValue) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(jsonValue, new TypeReference<List<TableViewField>>() {
            });
        } catch (Exception e) {
            LOG.error("Error", e);
            return new ArrayList<>();
        }
    }

    public static String toJson(List<TableViewField> fields) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(fields);
        } catch (IOException e) {
            throw new MyCollabException(e);
        }
    }
}
