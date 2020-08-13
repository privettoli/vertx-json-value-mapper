package io.github.privettoli.vertx.json.value.mapper;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;

import static java.util.regex.Pattern.compile;

/**
 * Not thread-safe.
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
        this.matcher = compile(regexp).matcher("Not thread safe");
        this.replacement = Objects.requireNonNull(replacement);
        this.maximumLength = Objects.checkIndex(maximumLength, Integer.MAX_VALUE);
        this.minimumLength = Objects.checkFromToIndex(minimumLength, maximumLength, Integer.MAX_VALUE);
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
}