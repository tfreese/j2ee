// Created: 10 Jan. 2025
package de.freese.j2ee.client;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

/**
 * @author Thomas Freese
 */
public final class RsClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(RsClient.class);

    public static void main(final String[] args) {
        // Redirect Java-Util-Logger to Slf4J.
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();

        // UriBuilder.fromUri().buildFromMap()
        final URI uri = URI.create("https://repo1.maven.org/maven2/dev/failsafe/failsafe/3.3.2/failsafe-3.3.2.pom");

        try (Client client = createClient()) {
            final WebTarget webTarget = client.target(uri);

            try (Response response = webTarget.request(MediaType.TEXT_XML).get()) {
                final String body = response.readEntity(String.class);
                LOGGER.info(body);
            }

            try (Response response = webTarget.request(MediaType.APPLICATION_OCTET_STREAM).get();
                 InputStream inputStream = response.readEntity(InputStream.class)) {
                final byte[] bytes = inputStream.readAllBytes();
                LOGGER.info(new String(bytes, StandardCharsets.UTF_8));
            }
        }
        catch (IOException ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
    }

    private static Client createClient() {
        // final ClientConfig clientConfig = new ClientConfig()
        //         .connectorProvider(new JavaNetHttpConnectorProvider());

        return ClientBuilder.newBuilder()
                .connectTimeout(90, TimeUnit.SECONDS)
                .readTimeout(90, TimeUnit.SECONDS)
                .property(ClientProperties.CONNECTOR_PROVIDER, "org.glassfish.jersey.jnh.connector.JavaNetHttpConnectorProvider")
                // .withConfig(clientConfig)
                .build();
    }

    private RsClient() {
        super();
    }
}
