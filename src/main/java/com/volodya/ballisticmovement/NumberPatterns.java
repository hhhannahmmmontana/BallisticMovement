package com.volodya.ballisticmovement;

import javafx.scene.control.TextFormatter;
import javafx.util.StringConverter;

import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

public final class NumberPatterns {
    private final static Pattern INPUT_UNSIGNED_DOUBLE_PATTERN = Pattern.compile("(0|([1-9][0-9]*))?([.,][0-9]*E?[1-9]?[0-9]*?)?");
    public final static Pattern UNSIGNED_DOUBLE_PATTERN = Pattern.compile("(0|([1-9][0-9]*))(\\.[0-9]+(E[1-9][0-9]*)?)?");

    private final static Pattern INPUT_SIGNED_DOUBLE_PATTERN = Pattern.compile("-?(0|([1-9][0-9]*))?([.,][0-9]*E?[1-9]?[0-9]*?)?");
    public final static Pattern SIGNED_DOUBLE_PATTERN = Pattern.compile("-?(0|([1-9][0-9]*))(\\.[0-9]+(E[1-9][0-9]*)?)?");

    private final static Pattern INPUT_UNSIGNED_INT_PATTERN = Pattern.compile("[1-9]*[0-9]*");
    public final static Pattern UNSIGNED_INT_PATTERN = Pattern.compile("[1-9][0-9]*");

    private static UnaryOperator<TextFormatter.Change> makeFilter(Pattern pattern) {
        final UnaryOperator<TextFormatter.Change> filter = c -> {
            String text = c.getControlNewText();
            if (pattern.matcher(text).matches()) {
                return c;
            } else {
                return null;
            }
        };
        return filter;
    }

    private final static StringConverter<Double> UNSIGNED_DOUBLE_CONVERTER = new StringConverter<Double>() {
        @Override
        public String toString(Double d) {
            return d.toString();
        }

        @Override
        public Double fromString(String s) {
            if (s.contains(",")) {
                s = s.substring(0, s.lastIndexOf(",")) + '.' + s.substring(s.lastIndexOf(',') + 1);
            }
            if (UNSIGNED_DOUBLE_PATTERN.matcher(s).matches()) {
                return Double.valueOf(s);
            } else {
                return 0.0;
            }
        }
    };

    private final static StringConverter<Double> SIGNED_DOUBLE_CONVERTER = new StringConverter<Double>() {
        @Override
        public String toString(Double d) {
            return d.toString();
        }

        @Override
        public Double fromString(String s) {
            if (s.contains(",")) {
                s = s.substring(0, s.lastIndexOf(",")) + '.' + s.substring(s.lastIndexOf(',') + 1);
            }
            if (SIGNED_DOUBLE_PATTERN.matcher(s).matches()) {
                return Double.valueOf(s);
            } else {
                return 0.0;
            }
        }
    };

    private final static StringConverter<Integer> UNSIGNED_INT_CONVERTER = new StringConverter<Integer>() {
        @Override
        public String toString(Integer i) {
            return i.toString();
        }

        @Override
        public Integer fromString(String s) {
            if (SIGNED_DOUBLE_PATTERN.matcher(s).matches()) {
                return Integer.valueOf(s);
            } else {
                return 0;
            }
        }
    };

    public static TextFormatter<Double> makeUnsignedDoubleFormatter(Double defaultValue) {
        return new TextFormatter<>(
            UNSIGNED_DOUBLE_CONVERTER,
            defaultValue,
            makeFilter(INPUT_UNSIGNED_DOUBLE_PATTERN)
        );
    }

    public static TextFormatter<Double> makeSignedDoubleFormatter(Double defaultValue) {
        return new TextFormatter<>(
                SIGNED_DOUBLE_CONVERTER,
                defaultValue,
                makeFilter(INPUT_SIGNED_DOUBLE_PATTERN)
        );
    }

    public static TextFormatter<Integer> makeUnsignedIntFormatter(Integer defaultValue) {
        return new TextFormatter<>(
                UNSIGNED_INT_CONVERTER,
                defaultValue,
                makeFilter(INPUT_UNSIGNED_INT_PATTERN)
        );
    }
}
