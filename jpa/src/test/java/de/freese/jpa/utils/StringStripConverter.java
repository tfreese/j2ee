// Created: 11.07.23
package de.freese.jpa.utils;

import jakarta.persistence.AttributeConverter;

/**
 * Convert a value into another.<br>
 * Registration:<br>
 *
 * <pre>
 * &#064;Converter(autoApply = true)
 * public class StringStripConverter
 * </pre>
 * or
 * <pre>
 * <code>@Convert(converter = StringStripConverter.class)</code>
 * private String myValue;
 * </pre>
 * or
 * <pre>
 * package.info.java
 * <code>@ConverterRegistration(converter = StringStripConverter.class)</code>
 * </pre>
 *
 * @author Thomas Freese
 */
public class StringStripConverter implements AttributeConverter<String, String> {
    @Override
    public String convertToDatabaseColumn(final String attribute) {
        if (attribute == null) {
            return null;
        }

        return attribute.strip();
    }

    @Override
    public String convertToEntityAttribute(final String dbData) {
        if (dbData == null) {
            return null;
        }

        return dbData.strip();
    }
}
