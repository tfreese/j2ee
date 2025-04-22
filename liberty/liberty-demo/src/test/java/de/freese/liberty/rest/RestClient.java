// Created: 15 März 2025
package de.freese.liberty.rest;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import jakarta.ws.rs.core.HttpHeaders;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.freese.liberty.kryo.KryoContextResolver;
import de.freese.liberty.kryo.KryoReaderWriter;

/**
 * @author Thomas Freese
 */
// @SuppressWarnings("all")
@SuppressWarnings({"unchecked", "rawtypes"})
public final class RestClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(RestClient.class);

    public static void main(final String[] args) throws IOException, InterruptedException {
        final URI uri = URI.create("http://localhost:9080/liberty-demo/my-app/service/kryo");
        final KryoContextResolver kryoProvider = new KryoContextResolver();
        final Kryo kryo = kryoProvider.getKryo();

        byte[] bytes = null;

        try (Output output = new Output(16_384)) {
            kryo.writeClassAndObject(output, List.of(1L, 2L, 3L));

            // Needs Registration in Kryo.
            // kryo.writeObjectOrNull(output, List.of(1L, 2L, 3L), List.class);
            bytes = output.toBytes();
        }

        try (HttpClient httpClient = HttpClient.newBuilder().build()) {
            final HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(uri)
                    .header(HttpHeaders.CONTENT_TYPE, KryoReaderWriter.KRYO_MEDIA_TYPE)
                    .header(HttpHeaders.ACCEPT, KryoReaderWriter.KRYO_MEDIA_TYPE)
                    .POST(HttpRequest.BodyPublishers.ofByteArray(bytes))
                    .build();

            final HttpResponse<InputStream> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofInputStream());
            LOGGER.info("statusCode: {}", httpResponse.statusCode());

            if (httpResponse.statusCode() != HttpURLConnection.HTTP_OK) {
                return;
            }

            // final ByteArrayOutputStream baos = new ByteArrayOutputStream();
            // httpResponse.body().transferTo(baos);
            // bytes = baos.toByteArray();
            //
            // LOGGER.info("content length: {}", bytes.length);

            try (InputStream inputStream = httpResponse.body();
                 Input input = new Input(inputStream)) {
                final List<String> stringValues = (List) kryo.readClassAndObject(input);

                // Needs Registration in Kryo.
                // final List<String> stringValues = kryo.readObjectOrNull(input, List.class);
                LOGGER.info("stringValues: {}", stringValues);
            }
        }
    }

    private RestClient() {
        super();
    }
}
