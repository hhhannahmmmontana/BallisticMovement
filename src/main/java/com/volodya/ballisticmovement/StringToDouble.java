package com.volodya.ballisticmovement;

import javafx.scene.control.TextFormatter;
import javafx.util.StringConverter;

import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

public final class StringToDouble {
    private final static Pattern INPUT_DOUBLE_PATTERN = Pattern.compile("(0|([1-9][0-9]*))?([.,][0-9]*E?[1-9]?[0-9]*?)?");
    final static Pattern DOUBLE_PATTERN = Pattern.compile("(0|([1-9][0-9]*))(\\.[0-9]+(E[1-9][0-9]*)?)?");

    private final static UnaryOperator<TextFormatter.Change> DOUBLE_FILTER = c -> {
        String text = c.getControlNewText();
        if (INPUT_DOUBLE_PATTERN.matcher(text).matches()) {
            return c;
        } else {
            return null;
        }
    };

    private final static StringConverter<Double> DOUBLE_CONVERTER = new StringConverter<Double>() {
        @Override
        public String toString(Double d) {
            return d.toString();
        }

        @Override
        public Double fromString(String s) {
            if (s.contains(",")) {
                s = s.substring(0, s.lastIndexOf(",")) + '.' + s.substring(s.lastIndexOf(',') + 1);
            }
            if (DOUBLE_PATTERN.matcher(s).matches()) {
                return Double.valueOf(s);
            } else {
                return 0.0;
            }
        }
    };

    public final static TextFormatter<Double> DOUBLE_FORMATTER = new TextFormatter<>(DOUBLE_CONVERTER, 0.0, DOUBLE_FILTER);
}
