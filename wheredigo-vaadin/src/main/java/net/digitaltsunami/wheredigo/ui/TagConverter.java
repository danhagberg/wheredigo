package net.digitaltsunami.wheredigo.ui;

import com.vaadin.data.Converter;
import com.vaadin.data.Result;
import com.vaadin.data.ValueContext;

import java.util.Arrays;
import java.util.stream.Collectors;

public class TagConverter implements Converter<String, String[]> {
    @Override
    public Result<String[]> convertToModel(String value, ValueContext context) {
        if (value == null) {
            return Result.ok(null);
        }
        else {
            return Result.ok(
                    Arrays.stream(value.split(","))
                    .map(tag -> tag.trim())
                    .toArray(String[]::new));
        }
    }

    @Override
    public String convertToPresentation(String[] value, ValueContext context) {
        if (value == null) {
            return null;
        }
        else {
            return Arrays.stream(value).collect(Collectors.joining(", "));
        }
    }
}
