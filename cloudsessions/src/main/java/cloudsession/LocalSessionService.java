package cloudsession;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Enumeration;
import java.util.Properties;
import util.ObjectSerializer;

/**
 * @author Thomas Freese
 */
public class LocalSessionService implements CloudSession
{
    /**
     *
     */
    // private final File propFile = Paths.get(System.getProperty("java.io.tmpdir"), "cloudsession.properties").toFile();
    private final Path propertiesPath = Paths.get("/tmp", "cloudsession.properties");

    /**
     * Erstellt ein neues {@link LocalSessionService} Object.
     */
    public LocalSessionService()
    {
        super();
    }

    /**
     * @return {@link Properties}
     */
    private Properties getProps()
    {
        Properties props = new Properties();

        try
        {
            if (!Files.exists(this.propertiesPath))
            {
                Files.createDirectories(this.propertiesPath.getParent());
                Files.createFile(this.propertiesPath);
            }

            try (InputStream fis = Files.newInputStream(this.propertiesPath, StandardOpenOption.READ))
            {
                props.load(fis);
            }
        }
        catch (IOException ex)
        {
            throw new RuntimeException(ex);
        }

        return props;
    }

    /**
     * @param sessionID String
     * @param name String
     * @return String
     */
    private String getPropsKey(final String sessionID, final String name)
    {
        return "[" + sessionID + "][" + name + "]";
    }

    /**
     * @see cloudsession.CloudSession#getSessionValue(java.lang.String, java.lang.String)
     */
    @Override
    public Object getSessionValue(final String sessionID, final String name)
    {
        return ObjectSerializer.fromJSON(getProps().getProperty(getPropsKey(sessionID, name)));
    }

    /**
     * @see cloudsession.CloudSession#remove(java.lang.String)
     */
    @Override
    public void remove(final String sessionID)
    {
        Properties props = getProps();
        Enumeration<Object> keys = props.keys();

        while (keys.hasMoreElements())
        {
            String key = (String) keys.nextElement();

            if (key.contains(sessionID))
            {
                props.remove(key);
            }
        }

        storeProps(props);
    }

    /**
     * @param props {@link Properties}
     */
    private void removeOldProps(final Properties props)
    {
        // TODO
        // Enumeration<Object> keys = props.keys();
        // while (keys.hasMoreElements()) {
        //
        // }
    }

    /**
     * @see cloudsession.CloudSession#setSessionValue(java.lang.String, java.lang.String, java.lang.Object)
     */
    @Override
    public void setSessionValue(final String sessionID, final String name, final Object value)
    {
        Properties props = getProps();
        removeOldProps(props);
        props.put(getPropsKey(sessionID, name), ObjectSerializer.toJSON(value));
        storeProps(props);
    }

    /**
     * @param props {@link Properties}
     */
    private void storeProps(final Properties props)
    {
        try
        {
            try (OutputStream fos = Files.newOutputStream(this.propertiesPath, StandardOpenOption.WRITE, StandardOpenOption.CREATE))
            {
                props.store(fos, "comments");
            }
        }
        catch (Exception ex)
        {
            throw new RuntimeException(ex);
        }
    }
}
