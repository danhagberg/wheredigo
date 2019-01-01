package net.digitaltsunami.wheredigo.ui;

import com.vaadin.ui.renderers.TextRenderer;
import elemental.json.JsonValue;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Converts tags from presentation to model.
 * Format of presentation is comma delimited string.  Model is String array.
 */
public class TagRenderer extends TextRenderer {
    @Override
    public JsonValue encode(Object value) {
        if (value == null) {
            return super.encode(null);
        }
        String[] tags = (String[]) value;
        return super.encode(Arrays.stream(tags)
                .collect(Collectors.joining(", ")));
    }
}
