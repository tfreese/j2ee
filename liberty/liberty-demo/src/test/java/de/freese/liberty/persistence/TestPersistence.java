// Created: 23 Apr. 2025
package de.freese.liberty.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.URI;
import java.net.URL;
import java.time.LocalDateTime;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * @author Thomas Freese
 */
class TestPersistence {
    private static EntityManagerFactory entityManagerFactory;

    @AfterAll
    static void afterAll() {
        entityManagerFactory.close();
    }

    @BeforeAll
    static void beforeAll() {
        entityManagerFactory = Persistence.createEntityManagerFactory("my-pu-test");
    }

    @Test
    void testTimestamp() {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            final LocalDateTime localDateTime = (LocalDateTime) entityManager.createNativeQuery("select CURRENT_TIMESTAMP", LocalDateTime.class).getSingleResult();
            final LocalDateTime localDateTimeNow = LocalDateTime.now();

            assertEquals(localDateTimeNow.getYear(), localDateTime.getYear());
            assertEquals(localDateTimeNow.getMonth(), localDateTime.getMonth());
            assertEquals(localDateTimeNow.getDayOfMonth(), localDateTime.getDayOfMonth());
            assertEquals(localDateTimeNow.getHour(), localDateTime.getHour());
            assertEquals(localDateTimeNow.getMinute(), localDateTime.getMinute());
            assertEquals(localDateTimeNow.getSecond(), localDateTime.getSecond());
        }
    }

    @Test
    @Disabled("disabled")
    void testValidateSchema() throws Exception {
        final URI uri = URI.create("https://jakarta.ee/xml/ns/persistence/persistence_3_0.xsd");
        final Source schemaFile = new StreamSource(uri.toURL().openStream());

        final URL url = ClassLoader.getSystemResource("META-INF/persistence.xml");
        final Source xmlFile = new StreamSource(url.openStream());

        // Validate Schema.
        final SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        // schemaFactory.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
        schemaFactory.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, "");

        final Schema schema = schemaFactory.newSchema(schemaFile);

        // Validation either by Validator or Unmarshaller, otherwise the Stream is closed when parsing.
        final Validator validator = schema.newValidator();
        validator.validate(xmlFile);

        // final JAXBContext jaxbContext = JAXBContext.newInstance(MyClass.class.getPackageName());
    }
}
