/**
 * Created: 11.05.2013
 */

package de.freese.j2ee.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import de.freese.j2ee.model.Kunde;

/**
 * @author Thomas Freese
 */
public class RESTClient
{
    /**
     * @param oid long
     * @throws Exception Falls was schief geht.
     */
    static void delete(final long oid) throws Exception
    {
        System.err.println("RESTClient.delete()");

        URL url = new URL("http://localhost:8080/de.freese.j2ee/rest/kunde/" + oid);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("DELETE");

        // if (connection.getResponseCode() != HttpURLConnection.HTTP_OK)
        // {
        // throw new RuntimeException("Operation failed: " + connection.getResponseCode());
        // }

        System.out.println("Content-Type = " + connection.getContentType());
        System.out.println("Location: " + connection.getHeaderField("Location"));

        connection.disconnect();
    }

    /**
     * @param name String
     * @param vorname String
     * @throws Exception Falls was schief geht.
     */
    static void insert(final String name, final String vorname) throws Exception
    {
        System.err.println("RESTClient.insert()");

        URL url = new URL("http://localhost:8080/de.freese.j2ee/rest/kunde?name=" + name + "&vorname=" + vorname);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("PUT");
        // connection.setRequestProperty("Accept", "application/json");
        // connection.setRequestProperty("Accept", "text/xml");

        // if (connection.getResponseCode() != HttpURLConnection.HTTP_OK)
        // {
        // throw new RuntimeException("Operation failed: " + connection.getResponseCode());
        // }

        System.out.println("Content-Type = " + connection.getContentType());
        System.out.println("Location: " + connection.getHeaderField("Location"));

        connection.disconnect();
    }

    /**
     * @param args String[]
     * @throws Exception Falls was schief geht.
     */
    public static void main(final String[] args) throws Exception
    {
        // selectOne(1);
        // update();
        selectAll();
        insert("Freese", "Thomas");
        delete(0);
        selectAll();
    }

    /**
     * @throws Exception Falls was schief geht.
     */
    static void selectAll() throws Exception
    {
        System.err.println("RESTClient.selectAll()");

        URL url = new URL("http://localhost:8080/de.freese.j2ee/rest/kunde");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json");
        // connection.setRequestProperty("Accept", "text/xml");

        if (connection.getResponseCode() != HttpURLConnection.HTTP_OK)
        {
            throw new RuntimeException("Operation failed: " + connection.getResponseCode());
        }

        System.out.println("Content-Type = " + connection.getContentType());
        System.out.println("Location: " + connection.getHeaderField("Location"));

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line = reader.readLine();

        while (line != null)
        {
            System.out.println(line);
            line = reader.readLine();
        }

        connection.disconnect();
    }

    /**
     * @param oid long
     * @throws Exception Falls was schief geht.
     */
    static void selectOne(final long oid) throws Exception
    {
        System.err.println("RESTClient.selectOne()");

        URL url = new URL("http://localhost:8080/de.freese.j2ee/rest/kunde/" + oid);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        // connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("Accept", "text/xml");

        if (connection.getResponseCode() != HttpURLConnection.HTTP_OK)
        {
            throw new RuntimeException("Operation failed: " + connection.getResponseCode());
        }

        System.out.println("Content-Type = " + connection.getContentType());
        System.out.println("Location: " + connection.getHeaderField("Location"));

        JAXBContext context = JAXBContext.newInstance(Kunde.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        Kunde kunde = (Kunde) unmarshaller.unmarshal(connection.getInputStream());
        System.out.println(kunde);

        // BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        //
        // String line = reader.readLine();
        //
        // while(line != null)
        // {
        // System.out.println(line);
        // line=reader.readLine();
        // }

        connection.disconnect();
    }

    /**
     * @throws Exception Falls was schief geht.
     */
    static void update() throws Exception
    {
        System.err.println("RESTClient.update()");

        URL url = new URL("http://localhost:8080/de.freese.j2ee/rest/kunde");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setInstanceFollowRedirects(false);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "text/xml");

        try (OutputStream out = connection.getOutputStream())
        {
            out.write("<kunde id=\"1\"><name>CCC</name><vorname>DDD</vorname></kunde>".getBytes());
            out.flush();
        }

        if (connection.getResponseCode() != HttpURLConnection.HTTP_OK)
        {
            throw new RuntimeException("Failed to update");
        }

        System.out.println("Content-Type = " + connection.getContentType());
        System.out.println("Location: " + connection.getHeaderField("Location"));

        connection.disconnect();
    }
}
