package net.digitaltsunami.wheredigo.ui;

import com.vaadin.ui.renderers.TextRenderer;
import elemental.json.JsonValue;

import java.util.Arrays;
import java.util.stream.Collectors;

public class TagRenderer extends TextRenderer {
    @Override
    public JsonValue encode(Object value) {
        if (value == null) {
            return super.encode(null);
        }
        String[] tags = (String[])value;
        return super.encode(Arrays.stream(tags)
                .collect(Collectors.joining(", ")));
    }

}
