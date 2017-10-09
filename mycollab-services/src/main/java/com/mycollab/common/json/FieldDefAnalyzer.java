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
