package de.freese.jpa.converter;

import java.awt.Color;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * @author Thomas Freese
 * @since 11.07.23
 */
@Converter
public class ColorConverter implements AttributeConverter<Color, String> {

    @Override
    public String convertToDatabaseColumn(final Color attribute) {
        if (attribute == null) {
            return null;
        }

        return "#" + Integer.toHexString(attribute.getRGB()).substring(0, 6);
    }

    @Override
    public Color convertToEntityAttribute(final String dbData) {
        if (dbData == null) {
            return null;
        }

        return Color.decode(dbData);
    }
}
