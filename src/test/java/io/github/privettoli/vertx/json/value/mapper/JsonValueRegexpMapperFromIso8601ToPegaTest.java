package io.github.privettoli.vertx.json.value.mapper;

import io.vertx.core.json.JsonObject;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import java.io.IOException;

import static io.github.privettoli.vertx.json.value.mapper.Fixtures.readResource;

class JsonValueRegexpMapperFromIso8601ToPegaTest {
    public static final JsonValueRegexpMapper FROM_ISO_8601_TO_PEGA_MAPPER = new JsonValueRegexpMapper(
        "^(\\d{4})-(\\d{2})-(\\d{2})T(\\d{2}):(\\d{2}):(\\d{2})\\.(\\d{3})\\d{0,3}Z$",
        "$1$2$3T$4$5$6.$7 GMT",
        "2020-08-13T06:02:50.476Z".length(),
        "2020-08-13T06:02:50.476377Z".length()
    );

    @Test
    void toPegaSmaller() throws IOException, JSONException {
        // Given
        var iso8601Payload = new JsonObject(readResource("iso8601SmallPayload.json"));
        // When
        FROM_ISO_8601_TO_PEGA_MAPPER.map(iso8601Payload);
        // Then
        var expectedPayload = readResource("pegaSmallPayload.json");
        JSONAssert.assertEquals(expectedPayload, iso8601Payload.toString(), true);
    }

    @Test
    void toPegaBigger() throws IOException, JSONException {
        // Given
        var iso8601Payload = new JsonObject(readResource("iso8601BiggerPayload.json"));
        // When
        FROM_ISO_8601_TO_PEGA_MAPPER.map(iso8601Payload);
        // Then
        var expectedPayload = readResource("pegaBiggerPayload.json");
        JSONAssert.assertEquals(expectedPayload, iso8601Payload.toString(), true);
    }
}