package com.fix_it.app.common.converter;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fix_it.app.model.view.ItemServicoView;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.List;

@Converter(autoApply = false)
public class JsonListConverter implements AttributeConverter<List<ItemServicoView>, String> {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<ItemServicoView> attribute) {
        try {
            return mapper.writeValueAsString(attribute);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao converter lista para JSON", e);
        }
    }

    @Override
    public List<ItemServicoView> convertToEntityAttribute(String dbData) {
        try {
            return mapper.readValue(dbData, new TypeReference<>() {
            });
        } catch (Exception e) {
            throw new RuntimeException("Erro ao converter JSON para lista", e);
        }
    }
}