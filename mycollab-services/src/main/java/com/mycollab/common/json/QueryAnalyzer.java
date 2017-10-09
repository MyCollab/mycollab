package com.mycollab.common.json;

import com.mycollab.core.MyCollabException;
import com.mycollab.db.arguments.SearchCriteria;
import com.mycollab.db.query.CacheParamMapper;
import com.mycollab.db.query.Param;
import com.mycollab.db.query.SearchFieldInfo;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

/**
 * @author MyCollab Ltd
 * @since 5.3.1
 */
public class QueryAnalyzer {
    private static final Logger LOG = LoggerFactory.getLogger(QueryAnalyzer.class);

    public static <S extends SearchCriteria>String toQueryParams(List<SearchFieldInfo<S>> searchFieldInfos) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            SimpleModule module = new SimpleModule();
            module.addSerializer(Param.class, new ParamSerializer());
            mapper.registerModule(module);
            return mapper.writeValueAsString(searchFieldInfos);
        } catch (IOException e) {
            throw new MyCollabException(e);
        }
    }

    public static <S extends SearchCriteria> List<SearchFieldInfo<S>> toSearchFieldInfos(String query, String type) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            SimpleModule module = new SimpleModule();
            module.addDeserializer(Param.class, new ParamDeserializer(type));
            mapper.registerModule(module);
            return mapper.readValue(query, new TypeReference<List<SearchFieldInfo<S>>>() {
            });
        } catch (Exception e) {
            LOG.error("Error", e);
            return null;
        }
    }

    public static class ParamSerializer extends JsonSerializer<Param> {
        @Override
        public void serialize(Param param, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeStringField("id", param.getId());
            jsonGenerator.writeEndObject();
        }
    }

    public static class ParamDeserializer extends JsonDeserializer<Param> {
        private String type;

        ParamDeserializer(String type) {
            this.type = type;
        }

        @Override
        public Param deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
            JsonNode node = jsonParser.getCodec().readTree(jsonParser);
            String id = node.get("id").asText();
            CacheParamMapper.ValueParam valueParam = CacheParamMapper.getValueParam(type, id);
            if (valueParam != null) {
                return valueParam.getParam();
            } else {
                throw new MyCollabException("Invalid query");
            }
        }
    }
}
