// Created: 11.07.23
package de.freese.jpa.utils;

import java.awt.Color;

import jakarta.persistence.AttributeConverter;

/**
 * @author Thomas Freese
 */
public class ColorConverter implements AttributeConverter<Color, String> {

    @Override
    public String convertToDatabaseColumn(Color attribute) {
        if (attribute == null) {
            return null;
        }

        return "#" + Integer.toHexString(attribute.getRGB()).substring(0, 6);
    }

    @Override
    public Color convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }

        return Color.decode(dbData);
    }
}
