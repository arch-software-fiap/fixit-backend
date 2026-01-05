package com.fix_it.app.common.databind;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import lombok.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

public class Databind {
    @Generated
    private static final Logger log = LoggerFactory.getLogger(Databind.class);

    public static class IdDeserializer<T> extends JsonDeserializer<T> implements ContextualDeserializer {
        private Class<?> clazz;

        public IdDeserializer() {
        }

        public IdDeserializer(Class<?> tipo) {
            this.clazz = tipo;
        }

        public JsonDeserializer<?> createContextual(DeserializationContext deserializationContext, BeanProperty beanProperty) {
            JavaType tipo = deserializationContext.getContextualType() != null ? deserializationContext.getContextualType() : beanProperty.getMember().getType();
            return new IdDeserializer<>(tipo.getRawClass());
        }

        @Override
        public T deserialize(JsonParser jp, DeserializationContext deserializationContext) throws IOException {
            JsonNode node = jp.getCodec().readTree(jp); // Removido cast explícito

            try {
                if (node.isTextual()) {
                    return this.setIdAnyType(UUID.fromString(node.asText()));
                }

                if (node.isObject()) {
                    JsonNode idNode = node.get("id");
                    if (idNode != null && idNode.isTextual()) {
                        return this.setIdAnyType(UUID.fromString(idNode.asText()));
                    }
                }
            } catch (Exception e) {
                Databind.log.info("Erro durante a desserialização de UUID. Verifique se o relacionamento é OneToMany ou ManyToMany e se o uso do \"contentUsing\" na anotação JsonDeserialize é apropriado.", e);
            }

            return null;
        }

        protected T setIdAnyType(UUID id) throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
            T c = (T) this.clazz.newInstance();
            this.clazz.getMethod("setId", UUID.class).invoke(c, id);
            return c;
        }
    }

    public static class IdSerializer<T> extends JsonSerializer<T> {
        @Override
        public void serialize(T entity, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            try {
                UUID id = (UUID) entity.getClass().getMethod("getId").invoke(entity);
                if (id != null) {
                    jsonGenerator.writeString(id.toString());
                } else {
                    jsonGenerator.writeNull();
                }
            } catch (Exception e) {
                Databind.log.info("Erro durante a serialização de UUID. Verifique se o relacionamento é OneToMany ou ManyToMany e se o uso do \"contentUsing\" na anotação JsonSerialize é apropriado.", e);
            }
        }
    }
}
