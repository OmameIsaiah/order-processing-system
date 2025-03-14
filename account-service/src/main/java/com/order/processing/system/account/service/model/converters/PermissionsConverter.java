package com.order.processing.system.account.service.model.converters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.order.processing.system.account.service.enums.Permissions;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.io.IOException;
import java.util.Set;

@Converter(autoApply = true)
public class PermissionsConverter implements AttributeConverter<Set<Permissions>, String> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Set<Permissions> permissions) {
        try {
            return objectMapper.writeValueAsString(permissions);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert permissions set to JSON", e);
        }
    }

    @Override
    public Set<Permissions> convertToEntityAttribute(String json) {
        try {
            return objectMapper.readValue(json, objectMapper.getTypeFactory().constructCollectionType(Set.class, Permissions.class));
        } catch (IOException e) {
            throw new RuntimeException("Failed to convert JSON to permissions set", e);
        }
    }
}
