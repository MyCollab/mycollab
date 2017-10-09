package com.mycollab.db.query;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

import java.io.IOException;
import java.util.*;

/**
 * @author MyCollab Ltd
 * @since 5.2.1
 */
@JsonSerialize(using = VariableInjector.Serializer.class)
@JsonDeserialize(using = VariableInjector.Deserializer.class)
public interface VariableInjector<T> {
    Object eval();

    Class<T> getType();

    boolean isArray();

    boolean isCollection();

    VariableInjector LAST_WEEK = new VariableInjector() {
        @Override
        public Object eval() {
            LocalDate date = new LocalDate();
            date = date.minusWeeks(-1);
            LocalDate minDate = date.dayOfWeek().withMinimumValue();
            LocalDate maxDate = date.dayOfWeek().withMaximumValue();
            return new Date[]{minDate.toDate(), maxDate.toDate()};
        }

        @Override
        public Class getType() {
            return Date[].class;
        }

        @Override
        public boolean isArray() {
            return true;
        }

        @Override
        public boolean isCollection() {
            return false;
        }
    };

    VariableInjector THIS_WEEK = new VariableInjector() {
        @Override
        public Object eval() {
            LocalDate date = new LocalDate();
            LocalDate minDate = date.dayOfWeek().withMinimumValue();
            LocalDate maxDate = date.dayOfWeek().withMaximumValue();
            return new Date[]{minDate.toDate(), maxDate.toDate()};
        }

        @Override
        public Class getType() {
            return Date[].class;
        }

        @Override
        public boolean isArray() {
            return true;
        }

        @Override
        public boolean isCollection() {
            return false;
        }
    };

    class Serializer extends JsonSerializer<VariableInjector> {
        @Override
        public void serialize(VariableInjector variableInjector, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            jsonGenerator.writeStartObject();
            Object value = variableInjector.eval();
            jsonGenerator.writeObjectField("value", value);
            if (variableInjector.isArray()) {
                jsonGenerator.writeStringField("array", "true");
            } else if (variableInjector.isCollection()) {
                jsonGenerator.writeStringField("collection", "true");
            }

            if (variableInjector.getType() != null && Date.class.isAssignableFrom(variableInjector.getType())) {
                jsonGenerator.writeStringField("type", "date");
            }
            jsonGenerator.writeEndObject();
        }
    }

    class Deserializer extends JsonDeserializer<VariableInjector> {
        @Override
        public VariableInjector deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
            JsonNode node = jsonParser.getCodec().readTree(jsonParser);
            JsonNode valueNode = node.get("value");
            if (valueNode.isArray()) {
                ArrayNode arrNode = (ArrayNode) valueNode;
                JsonNode typeNode = node.get("type");
                String type = typeNode == null ? "" : typeNode.asText();
                Collection values = new ArrayList(arrNode.size());
                for (int i = 0; i < arrNode.size(); i++) {
                    values.add(convertType(type, arrNode.get(i).asText()));
                }
                JsonNode arrayType = node.get("array");
                if (arrayType != null && "true".equals(arrayType.asText())) {
                    return ConstantValueInjector.valueOf(values.toArray());
                }
                return ConstantValueInjector.valueOf(values);
            } else {
                JsonNode typeNode = node.get("type");
                String type = typeNode == null ? "" : typeNode.asText();
                return ConstantValueInjector.valueOf(convertType(type, valueNode.asText()));
            }
        }

        private Object convertType(String type, String value) {
            if ("date".equals(type)) {
                long timeInMillis = Long.parseLong(value);
                return new LocalDateTime(timeInMillis).toDate();
            } else {
                return value;
            }
        }
    }
}
