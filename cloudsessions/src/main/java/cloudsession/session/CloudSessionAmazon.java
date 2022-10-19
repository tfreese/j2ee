package cloudsession.session;

import java.io.IOException;
import java.io.InputStream;

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
public class CloudSessionAmazon implements CloudSession
{
    private static final String SESSIONS_DOMAIN = "Sessions";

    private final AmazonSimpleDB amazonClient;

    public CloudSessionAmazon()
    {
        super();

        final AWSCredentials credentials;

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
    public String getSessionValue(final String sessionID, final String name)
    {
        GetAttributesResult gar = this.amazonClient.getAttributes(new GetAttributesRequest().withDomainName(SESSIONS_DOMAIN).withItemName(sessionID));

        for (Attribute attribute : gar.getAttributes())
        {
            if (attribute.getName().equals(name))
            {
                return attribute.getValue();
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
        DeletableItem deletableItem = new DeletableItem().withName(sessionID);

        this.amazonClient.batchDeleteAttributes(new BatchDeleteAttributesRequest().withDomainName(SESSIONS_DOMAIN).withItems(deletableItem));
    }

    /**
     * @see CloudSession#setSessionValue(java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public void setSessionValue(final String sessionID, final String name, final String value)
    {
        ReplaceableAttribute replaceableAttribute = new ReplaceableAttribute().withName(name).withValue(value).withReplace(Boolean.TRUE);
        ReplaceableItem replaceableItem = new ReplaceableItem().withName(sessionID).withAttributes(replaceableAttribute);

        this.amazonClient.batchPutAttributes(new BatchPutAttributesRequest().withDomainName(SESSIONS_DOMAIN).withItems(replaceableItem));
    }
}
