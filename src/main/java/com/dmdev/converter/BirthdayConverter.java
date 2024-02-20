package com.dmdev.converter;

import com.dmdev.entity.Birthday;
import jakarta.persistence.AttributeConverter;

import java.util.Date;
import java.util.Optional;

public class BirthdayConverter implements AttributeConverter<Birthday, Date> {
    @Override
    public Date convertToDatabaseColumn(Birthday attribute) {
        return Optional.ofNullable(attribute)
                .map(Birthday::birthDate)
                .map(java.sql.Date::valueOf)
                .orElse(null);
    }

    @Override
    public Birthday convertToEntityAttribute(Date dbData) {
        return Optional.ofNullable(dbData)
                .map(D)
                .map(Birthday::new)
                .orElse(null);
    }
}
