package io.github.privettoli.vertx.json.value.mapper;

import io.vertx.core.json.JsonObject;
import org.openjdk.jmh.annotations.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static io.github.privettoli.vertx.json.value.mapper.Fixtures.readResource;

@SuppressWarnings({"java:S1118", "FieldMayBeFinal"})
public class FromPegaToIso8601Benchmark {
    @State(Scope.Benchmark)
    public static class PerBenchmarkState {
        JsonValueRegexpMapper converter = JsonValueRegexpMapperFromPegaToIso8601Test.FROM_PEGA_TO_ISO_8601_MAPPER;
    }

    @State(Scope.Thread)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public static class SmallJsonPayload {
        JsonObject pegaJson;
        JsonObject samplePayload;

        @Setup(Level.Trial)
        public void init() throws IOException {
            samplePayload = new JsonObject(readResource("pegaSmallPayload.json"));
        }

        @Setup(Level.Invocation)
        public void doSetup() {
            pegaJson = samplePayload.copy();
        }

        @Benchmark
        public JsonObject smallJsonPayload(PerBenchmarkState benchmarkState) {
            benchmarkState.converter.map(pegaJson);
            return pegaJson;
        }
    }

    @State(Scope.Thread)
    public static class BiggerPayload {
        private JsonObject pegaJson;
        private JsonObject samplePayload;

        @Setup(Level.Trial)
        public void init() throws IOException {
            samplePayload = new JsonObject(readResource("pegaBiggerPayload.json"));
        }

        @Setup(Level.Iteration)
        public void doSetup() {
            pegaJson = samplePayload.copy();
        }

        @Benchmark
        public JsonObject biggerPayload(PerBenchmarkState benchmarkState) {
            benchmarkState.converter.map(pegaJson);
            return pegaJson;
        }
    }
}
