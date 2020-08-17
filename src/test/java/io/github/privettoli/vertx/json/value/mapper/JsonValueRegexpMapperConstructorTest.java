package io.github.privettoli.vertx.json.value.mapper;

import org.junit.jupiter.api.Test;

import java.util.regex.PatternSyntaxException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JsonValueRegexpMapperConstructorTest {
    @Test
    void constructorShouldThrowExceptionWhenLengthIsNegative() {
        assertThatThrownBy(() -> new JsonValueRegexpMapper("", "", -1))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Provided length -1 is not supported");
    }

    @Test
    void constructorShouldThrowExceptionWhenMaximumLengthIsSmallerMinimumLength() {
        assertThatThrownBy(() -> new JsonValueRegexpMapper("", "", 10, 5))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Provided length from 10 to 5 is not supported");
    }

    @Test
    void constructorShouldThrowExceptionWhenPatternCompilationFails() {
        final String invalidRegexp = "[";
        assertThatThrownBy(() -> new JsonValueRegexpMapper(invalidRegexp, ""))
            .isInstanceOf(PatternSyntaxException.class)
            .hasMessage("Unclosed character class near index 0\n[\n^");
    }

    @Test
    void constructorShouldThrowExceptionWhenRegexpIsNull() {
        assertThatThrownBy(() -> new JsonValueRegexpMapper(null, ""))
            .isInstanceOf(NullPointerException.class)
            .hasMessage("Regexp was null");
    }

    @Test
    void constructorShouldThrowExceptionWhenReplacementIsNull() {
        assertThatThrownBy(() -> new JsonValueRegexpMapper("Hello, World!", null))
            .isInstanceOf(NullPointerException.class)
            .hasMessage("Replacement was null");
    }
}
