package io.github.privettoli.vertx.json.value.mapper;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

import static java.util.Objects.requireNonNull;
import static java.util.regex.Pattern.compile;

/**
 * Replaces string values in {@link JsonObject}
 * using {@link JsonValueRegexpMapper#matcher} (created from regexp provided in constructor)
 * and {@link JsonValueRegexpMapper#replacement}
 * exactly once.<br>
 * Optionally, {@link JsonValueRegexpMapper#minimumLength} and {@link JsonValueRegexpMapper#maximumLength}
 * can filter out values that shouldn't even be checked with regexp.
 * This class is not thread-safe.
 */
public class JsonValueRegexpMapper {
    private final Matcher matcher;
    private final String replacement;
    private final int minimumLength;
    private final int maximumLength;

    public JsonValueRegexpMapper(String regexp, String replacement) {
        this(regexp, replacement, 1, Integer.MAX_VALUE);
    }

    public JsonValueRegexpMapper(String regexp, String replacement, int length) {
        this(regexp, replacement, length, length);
    }

    public JsonValueRegexpMapper(String regexp, String replacement, int minimumLength, int maximumLength) {
        this.matcher = compile(requireNonNull(regexp, "Regexp was null")).matcher("Not thread safe");
        this.replacement = requireNonNull(replacement, "Replacement was null");
        checkLengthRange(minimumLength, maximumLength);
        this.minimumLength = minimumLength;
        this.maximumLength = maximumLength;
    }

    public void map(JsonObject jsonObject) {
        mapFromMap(jsonObject.getMap());
    }

    @SuppressWarnings("unchecked")
    public void map(JsonArray jsonArray) {
        mapFromList(jsonArray.getList());
    }

    private void mapFromMap(Map<String, Object> map) {
        map.forEach((key, value) -> {
            if (value instanceof String) {
                final String mappedValue = map((String) value);
                if (mappedValue != value) {
                    map.put(key, mappedValue);
                }
            } else {
                mapFromObject(value);
            }
        });
    }

    @SuppressWarnings("unchecked")
    public void mapFromObject(Object value) {
        if (value instanceof Map) {
            mapFromMap((Map<String, Object>) value);
        } else if (value instanceof JsonObject) {
            mapFromMap(((JsonObject) value).getMap());
        } else if (value instanceof JsonArray) {
            mapFromList((List<Object>) ((JsonArray) value).getList());
        } else if (value instanceof List) {
            mapFromList((List<Object>) value);
        }
    }

    private void mapFromList(List<Object> list) {
        for (int i = 0, listSize = list.size(); i < listSize; i++) {
            Object value = list.get(i);
            if (value instanceof String) {
                final String mappedValue = map((String) value);
                if (mappedValue != value) {
                    list.set(i, mappedValue);
                }
            } else {
                mapFromObject(value);
            }
        }
    }

    private String map(String value) {
        int length = value.length();
        if (length < minimumLength || length > maximumLength) {
            return value;
        }
        return matcher.reset(value).replaceFirst(replacement);
    }

    private void checkLengthRange(int minimumLength, int maximumLength) {
        if (minimumLength <= 0 || maximumLength < minimumLength) {
            String range = maximumLength == minimumLength
                ? String.valueOf(maximumLength)
                : "from " + minimumLength + " to " + maximumLength;
            throw new IllegalArgumentException(
                "Provided length " + range + " is not supported"
            );
        }
    }
}