package io.github.privettoli.vertx.json.value.mapper;

import io.vertx.core.json.JsonObject;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import java.io.IOException;

import static io.github.privettoli.vertx.json.value.mapper.Fixtures.readResource;

class JsonValueRegexpMapperFromPegaToIso8601Test {
    public static final JsonValueRegexpMapper FROM_PEGA_TO_ISO_8601_MAPPER = new JsonValueRegexpMapper(
        "^(\\d{4})(\\d{2})(\\d{2})T(\\d{2})(\\d{2})(\\d{2})\\.(\\d{3})\\sGMT$",
        "$1-$2-$3T$4:$5:$6.$7Z",
        "19890918T053347.789 GMT".length()
    );

    @Test
    void toIso8601Smaller() throws IOException, JSONException {
        // Given
        var pegaPayload = new JsonObject(readResource("pegaSmallPayload.json"));
        // When
        FROM_PEGA_TO_ISO_8601_MAPPER.map(pegaPayload);
        // Then
        var expectedPayload = readResource("iso8601SmallPayload.json");
        JSONAssert.assertEquals(expectedPayload, pegaPayload.toString(), true);
    }

    @Test
    void toIso8601Bigger() throws IOException, JSONException {
        // Given
        var pegaPayload = new JsonObject(readResource("pegaBiggerPayload.json"));
        // When
        FROM_PEGA_TO_ISO_8601_MAPPER.map(pegaPayload);
        // Then
        var expectedPayload = readResource("iso8601BiggerPayload.json");
        JSONAssert.assertEquals(expectedPayload, pegaPayload.toString(), true);
    }
}