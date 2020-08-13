package io.github.privettoli.vertx.json.value.mapper;

import io.vertx.core.json.JsonObject;
import org.openjdk.jmh.annotations.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static io.github.privettoli.vertx.json.value.mapper.Fixtures.readResource;

@SuppressWarnings({"java:S1118", "FieldMayBeFinal"})
public class FromIso8601ToPegaBenchmark {
    @State(Scope.Benchmark)
    public static class PerBenchmarkState {
        JsonValueRegexpMapper converter = JsonValueRegexpMapperFromIso8601ToPegaTest.FROM_ISO_8601_TO_PEGA_MAPPER;
    }

    @State(Scope.Thread)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public static class SmallJsonPayload {
        JsonObject iso8601Json;
        JsonObject samplePayload;

        @Setup(Level.Trial)
        public void init() throws IOException {
            samplePayload = new JsonObject(readResource("iso8601SmallPayload.json"));
        }

        @Setup(Level.Invocation)
        public void doSetup() {
            iso8601Json = samplePayload.copy();
        }

        @Benchmark
        public JsonObject smallJsonPayload(PerBenchmarkState benchmarkState) {
            benchmarkState.converter.map(iso8601Json);
            return iso8601Json;
        }
    }

    @State(Scope.Thread)
    public static class BiggerPayload {
        private JsonObject iso8601;
        private JsonObject samplePayload;

        @Setup(Level.Trial)
        public void init() throws IOException {
            samplePayload = new JsonObject(readResource("iso8601BiggerPayload.json"));
        }

        @Setup(Level.Iteration)
        public void doSetup() {
            iso8601 = samplePayload.copy();
        }

        @Benchmark
        public JsonObject biggerPayload(PerBenchmarkState benchmarkState) {
            benchmarkState.converter.map(iso8601);
            return iso8601;
        }
    }
}
