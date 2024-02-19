package com.alert.alert.entities.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.stream.Stream;

@Converter(autoApply = true)
public class ActionConverter implements AttributeConverter<Action, String> {
    @Override
    public String convertToDatabaseColumn(Action action) {
        if(action == null) {
            return null;
        }
        return action.name();
    }

    @Override
    public Action convertToEntityAttribute(String name) {
        if(name == null) {
            return null;
        }

        return Stream.of(Action.values())
                .filter(c -> c.name().equals(name))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
}
}
