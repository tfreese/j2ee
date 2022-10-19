// Created: 11.05.2013
package de.freese.j2ee.client;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;

import de.freese.j2ee.model.Kunde;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Thomas Freese
 */
public class RestClient
{
    private static final Logger LOGGER = LoggerFactory.getLogger(RestClient.class);

    public static void main(final String[] args) throws Exception
    {
        // selectOne(1);
        // update();
        selectAll();
        insert("Freese", "Thomas");
        delete(0);
        selectAll();
    }

    static void delete(final long oid) throws Exception
    {
        System.err.println("RestClient.delete()");

        URL url = new URL("http://localhost:8080/de.freese.j2ee/rest/kunde/" + oid);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("DELETE");

        // if (connection.getResponseCode() != HttpURLConnection.HTTP_OK)
        // {
        // throw new RuntimeException("Operation failed: " + connection.getResponseCode());
        // }

        LOGGER.info("Content-Type = {}", connection.getContentType());
        LOGGER.info("Location: {}", connection.getHeaderField("Location"));

        connection.disconnect();
    }

    static void insert(final String name, final String vorname) throws Exception
    {
        LOGGER.info("RestClient.insert()");

        URL url = new URL("http://localhost:8080/de.freese.j2ee/rest/kunde?name=" + name + "&vorname=" + vorname);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("PUT");
        // connection.setRequestProperty("Accept", "application/json");
        // connection.setRequestProperty("Accept", "text/xml");

        // if (connection.getResponseCode() != HttpURLConnection.HTTP_OK)
        // {
        // throw new RuntimeException("Operation failed: " + connection.getResponseCode());
        // }

        LOGGER.info("Content-Type = {}", connection.getContentType());
        LOGGER.info("Location: {}", connection.getHeaderField("Location"));

        connection.disconnect();
    }

    static void selectAll() throws Exception
    {
        LOGGER.info("RestClient.selectAll()");

        URL url = new URL("http://localhost:8080/de.freese.j2ee/rest/kunde");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json");
        // connection.setRequestProperty("Accept", "text/xml");

        if (connection.getResponseCode() != HttpURLConnection.HTTP_OK)
        {
            throw new RuntimeException("Operation failed: " + connection.getResponseCode());
        }

        LOGGER.info("Content-Type = {}", connection.getContentType());
        LOGGER.info("Location: {}", connection.getHeaderField("Location"));

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
        String line = reader.readLine();

        while (line != null)
        {
            LOGGER.info(line);
            line = reader.readLine();
        }

        connection.disconnect();
    }

    static void selectOne(final long oid) throws Exception
    {
        LOGGER.info("RestClient.selectOne()");

        URL url = new URL("http://localhost:8080/de.freese.j2ee/rest/kunde/" + oid);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        // connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("Accept", "text/xml");

        if (connection.getResponseCode() != HttpURLConnection.HTTP_OK)
        {
            throw new RuntimeException("Operation failed: " + connection.getResponseCode());
        }

        LOGGER.info("Content-Type = {}", connection.getContentType());
        LOGGER.info("Location: {}", connection.getHeaderField("Location"));

        JAXBContext context = JAXBContext.newInstance(Kunde.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();

        try (InputStream inputStream = connection.getInputStream())
        {
            Kunde kunde = (Kunde) unmarshaller.unmarshal(inputStream);
            LOGGER.info("{}", kunde);
        }

        try (InputStream inputStream = connection.getInputStream();
             InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(inputStreamReader))
        {

            String line = reader.readLine();

            while (line != null)
            {
                LOGGER.info(line);
                line = reader.readLine();
            }
        }

        connection.disconnect();
    }

    static void update() throws Exception
    {
        LOGGER.info("RestClient.update()");

        URL url = new URL("http://localhost:8080/de.freese.j2ee/rest/kunde");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setInstanceFollowRedirects(false);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "text/xml");

        try (OutputStream out = connection.getOutputStream())
        {
            out.write("<kunde id=\"1\"><name>CCC</name><vorname>DDD</vorname></kunde>".getBytes(StandardCharsets.UTF_8));
            out.flush();
        }

        if (connection.getResponseCode() != HttpURLConnection.HTTP_OK)
        {
            throw new RuntimeException("Failed to update");
        }

        LOGGER.info("Content-Type = {}", connection.getContentType());
        LOGGER.info("Location: {}", connection.getHeaderField("Location"));

        connection.disconnect();
    }
}
