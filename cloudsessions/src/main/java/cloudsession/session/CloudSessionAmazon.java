package cloudsession.session;

import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
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
public class CloudSessionAmazon implements CloudSession {
    private static final String SESSIONS_DOMAIN = "sessions";

    private final AmazonSimpleDB amazonClient;

    public CloudSessionAmazon() {
        super();

        // final AWSCredentials credentials;
        //
        // try (InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("AwsCredentials.properties")) {
        //     credentials = new PropertiesCredentials(inputStream);
        // }
        // catch (IOException ex) {
        //     throw new AmazonServiceException(ex.getMessage());
        // }
        //
        // amazonClient = AmazonSimpleDBClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials)).build();
        amazonClient = AmazonSimpleDBClientBuilder.standard().withCredentials(new ClasspathPropertiesFileCredentialsProvider()).build();
    }

    @Override
    public String getSessionValue(final String sessionID, final String name) {
        final GetAttributesResult gar = amazonClient.getAttributes(new GetAttributesRequest().withDomainName(SESSIONS_DOMAIN).withItemName(sessionID));

        for (Attribute attribute : gar.getAttributes()) {
            if (attribute.getName().equals(name)) {
                return attribute.getValue();
            }
        }

        return null;
    }

    @Override
    public void remove(final String sessionID) {
        final DeletableItem deletableItem = new DeletableItem().withName(sessionID);

        amazonClient.batchDeleteAttributes(new BatchDeleteAttributesRequest().withDomainName(SESSIONS_DOMAIN).withItems(deletableItem));
    }

    @Override
    public void setSessionValue(final String sessionID, final String name, final String value) {
        final ReplaceableAttribute replaceableAttribute = new ReplaceableAttribute().withName(name).withValue(value).withReplace(Boolean.TRUE);
        final ReplaceableItem replaceableItem = new ReplaceableItem().withName(sessionID).withAttributes(replaceableAttribute);

        amazonClient.batchPutAttributes(new BatchPutAttributesRequest().withDomainName(SESSIONS_DOMAIN).withItems(replaceableItem));
    }
}
