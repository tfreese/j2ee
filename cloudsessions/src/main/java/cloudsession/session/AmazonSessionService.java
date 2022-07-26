package cloudsession.session;

import java.io.IOException;
import java.io.InputStream;

import cloudsession.util.ObjectSerializer;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.simpledb.AmazonSimpleDB;
import com.amazonaws.services.simpledb.AmazonSimpleDBClientBuilder;
import com.amazonaws.services.simpledb.model.Attribute;
import com.amazonaws.services.simpledb.model.BatchDeleteAttributesRequest;
import com.amazonaws.services.simpledb.model.BatchPutAttributesRequest;
import com.amazonaws.services.simpledb.model.DeletableItem;
import com.amazonaws.services.simpledb.model.GetAttributesRequest;
import com.amazonaws.services.simpledb.model.GetAttributesResult;
import com.amazonaws.services.simpledb.model.ReplaceableAttribute;
import com.amazonaws.services.simpledb.model.ReplaceableItem;

/**
 * @author Thomas Freese
 */
public class AmazonSessionService implements CloudSession
{
    /**
     *
     */
    private static final String SESSIONS_DOMAIN = "Sessions";
    /**
     *
     */
    private final AmazonSimpleDB amazonClient;

    /**
     * Erstellt ein neues {@link AmazonSessionService} Object.
     */
    public AmazonSessionService()
    {
        super();

        AWSCredentials credentials = null;

        try (InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("AwsCredentials.properties"))
        {
            credentials = new PropertiesCredentials(inputStream);
        }
        catch (IOException ex)
        {
            throw new AmazonServiceException(ex.getMessage());
        }

        // this.amazonClient = new AmazonSimpleDBClient(credentials);
        this.amazonClient = AmazonSimpleDBClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials)).build();
    }

    /**
     * @see CloudSession#getSessionValue(java.lang.String, java.lang.String)
     */
    @Override
    public Object getSessionValue(final String sessionID, final String name)
    {
        GetAttributesResult gar = this.amazonClient.getAttributes(new GetAttributesRequest().withDomainName(SESSIONS_DOMAIN).withItemName(sessionID));

        for (Attribute a : gar.getAttributes())
        {
            if (a.getName().equals(name))
            {
                return ObjectSerializer.fromJson(a.getValue());
            }
        }

        return null;
    }

    /**
     * @see CloudSession#remove(java.lang.String)
     */
    @Override
    public void remove(final String sessionID)
    {
        this.amazonClient
                .batchDeleteAttributes(new BatchDeleteAttributesRequest().withDomainName(SESSIONS_DOMAIN).withItems(new DeletableItem().withName(sessionID)));
    }

    /**
     * @see CloudSession#setSessionValue(java.lang.String, java.lang.String, java.lang.Object)
     */
    @Override
    public void setSessionValue(final String sessionID, final String name, final Object value)
    {
        ReplaceableAttribute replAttr = new ReplaceableAttribute().withName(name).withValue(ObjectSerializer.toJson(value)).withReplace(Boolean.TRUE);

        this.amazonClient.batchPutAttributes(
                new BatchPutAttributesRequest().withDomainName(SESSIONS_DOMAIN).withItems(new ReplaceableItem().withName(sessionID).withAttributes(replAttr)));
    }
}
