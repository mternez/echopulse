package fr.mternez.echopulse.server.data.converter;

import fr.mternez.echopulse.core.common.domain.model.Permission;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Set;
import java.util.stream.Collectors;

@Converter(autoApply = false)
public class PermissionSetConverter implements AttributeConverter<Set<Permission>, String> {

    private static final String SPLIT_CHAR = ",";

    @Override
    public String convertToDatabaseColumn(Set<Permission> attribute) {
        if (attribute == null || attribute.isEmpty()) {
            return "";
        }
        return attribute.stream()
                .map(Enum::name)
                .collect(Collectors.joining(SPLIT_CHAR));
    }

    @Override
    public Set<Permission> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isBlank()) {
            return EnumSet.noneOf(Permission.class);
        }
        return Arrays.stream(dbData.split(SPLIT_CHAR))
                .map(Permission::valueOf)
                .collect(Collectors.toCollection(() ->
                        EnumSet.noneOf(Permission.class)));
    }
}