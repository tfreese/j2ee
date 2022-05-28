package util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Thomas Freese
 */
public class ObjectSerializer
{

    // /**
    // *
    // */
    // private static final XStream XSTREAM = new XStream(new XppDriver());
    /**
     *
     */
    private static final ObjectMapper JSON_MAPPER = new ObjectMapper();

    /**
     *
     */
    public static final Logger LOGGER = LoggerFactory.getLogger(ObjectSerializer.class);

    static
    {
        // Name des Root-Objektes mit anzeigen.
        JSON_MAPPER.configure(SerializationFeature.WRAP_ROOT_VALUE, true);
        JSON_MAPPER.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, true);

        // Globales PrettyPrinting; oder einzeln über jsonMapper.writerWithDefaultPrettyPrinter() nutzbar.
        JSON_MAPPER.enable(SerializationFeature.INDENT_OUTPUT);

        JSON_MAPPER.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
    }

    /**
     * @param json String
     *
     * @return Object
     */
    public static Object fromJson(final String json)
    {
        if ((json == null) || json.trim().isEmpty())
        {
            return null;
        }

        try
        {
            Object o = JSON_MAPPER.readValue(json, Object.class);

            return o;
        }
        catch (Exception ex)
        {
            if (ex instanceof RuntimeException rex)
            {
                throw rex;
            }

            throw new RuntimeException(ex);
        }
    }

    // /**
    // * @param xml String
    // *
    // * @return Object
    // */
    // public static Object fromXml(final String xml)
    // {
    // if (xml == null || xml.trim().isEmpty())
    // {
    // return null;
    // }
    //
    // Object o = XSTREAM.fromXML(xml);
    //
    // return o;
    // }
    // /**
    // * @param o Object
    // *
    // * @return String
    // */
    // public static String toXml(final Object o)
    // {
    // if (o == null)
    // {
    // return null;
    // }
    //
    // return XSTREAM.toXML(o);
    // }

    /**
     * @param o Object
     *
     * @return String
     */
    public static String toJson(final Object o)
    {
        if (o == null)
        {
            return null;
        }

        try
        {
            String json = JSON_MAPPER.writeValueAsString(o);

            return json;
        }
        catch (JsonProcessingException ex)
        {
            throw new RuntimeException(ex);
        }
    }
}
