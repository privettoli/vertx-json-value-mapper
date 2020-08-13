package io.github.privettoli.vertx.json.value.mapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static java.util.Objects.requireNonNull;

public class Fixtures {
    public static String readResource(String filename) throws IOException {
        return new String(
            requireNonNull(Fixtures.class.getClassLoader().getResourceAsStream(filename)).readAllBytes(),
            StandardCharsets.UTF_8
        );
    }
}

