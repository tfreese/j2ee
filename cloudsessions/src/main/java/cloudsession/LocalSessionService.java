package cloudsession;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.Properties;
import util.XmlSerializer;

/**
 * @author Thomas Freese
 */
public class LocalSessionService implements ICloudSession
{
    /**
     *
     */
    // private final File propFile = Paths.get(System.getProperty("java.io.tmpdir"), "cloudsession.properties").toFile();
    private final File propFile = Paths.get("/tmp", "cloudsession.properties").toFile();

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
            if (!this.propFile.exists())
            {
                this.propFile.createNewFile();
            }

            try (FileInputStream fis = new FileInputStream(this.propFile))
            {
                props.load(fis);
            }

            return props;
        }
        catch (IOException ex)
        {
            throw new RuntimeException(ex);
        }
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
     * @see cloudsession.ICloudSession#getSessionValue(java.lang.String, java.lang.String)
     */
    @Override
    public Object getSessionValue(final String sessionID, final String name)
    {
        return XmlSerializer.fromXML(getProps().getProperty(getPropsKey(sessionID, name)));
    }

    /**
     * @see cloudsession.ICloudSession#remove(java.lang.String)
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
     * @see cloudsession.ICloudSession#setSessionValue(java.lang.String, java.lang.String, java.lang.Object)
     */
    @Override
    public void setSessionValue(final String sessionID, final String name, final Object value)
    {
        Properties props = getProps();
        removeOldProps(props);
        props.put(getPropsKey(sessionID, name), XmlSerializer.toXML(value));
        storeProps(props);
    }

    /**
     * @param props {@link Properties}
     */
    private void storeProps(final Properties props)
    {
        try
        {
            try (FileOutputStream fos = new FileOutputStream(this.propFile))
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
