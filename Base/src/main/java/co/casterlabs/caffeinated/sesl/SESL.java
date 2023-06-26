/*
 * Copyright 2023 Casterlabs
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package co.casterlabs.caffeinated.sesl;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.jetbrains.annotations.Nullable;

import co.casterlabs.caffeinated.pluginsdk.widgets.settings.WidgetSettingsItem;
import co.casterlabs.caffeinated.pluginsdk.widgets.settings.WidgetSettingsLayout;
import co.casterlabs.caffeinated.pluginsdk.widgets.settings.WidgetSettingsSection;
import co.casterlabs.caffeinated.util.MimeTypes;
import co.casterlabs.commons.functional.tuples.Pair;
import co.casterlabs.rakurai.io.IOUtil;
import co.casterlabs.rakurai.json.element.JsonElement;
import co.casterlabs.rakurai.json.element.JsonObject;
import lombok.NonNull;
import xyz.e3ndr.fastloggingframework.logging.FastLogger;

public class SESL {
    public static final FastLogger LOGGER = new FastLogger();

    /**
     * The unique ID for Carrier mode. Always null for shim mode.
     */
    public static String seslId = null;

    public static boolean isShimMode() {
        return seslId == null;
    }

    public static WidgetSettingsLayout generateLayout(SESLWidget widget) {
        WidgetSettingsLayout layout = new WidgetSettingsLayout();

        WidgetSettingsSection fieldsSection = new WidgetSettingsSection("settings", "Settings");

        // Default Streamlabs fields.
        switch (widget.getShimType()) {
            case "chatbox": {
                // We intentionally leave out background_color.
                fieldsSection
                    .addItem(WidgetSettingsItem.asColor("text_color", "Text Color", "#ffffff"))
                    .addItem(WidgetSettingsItem.asNumber("font_size", "Font Size (px)", 18, 1, 0, 80));
                // TODO the rest...
                break;
            }
        }

        // Convert the custom fields.
        for (Map.Entry<String, JsonElement> entry : widget.getFields().entrySet()) {
            String fieldId = entry.getKey();
            JsonObject fieldData = entry.getValue().getAsObject();

            String se_fieldType = fieldData.getString("type");
            String fieldName = fieldData.getString("label");

            WidgetSettingsItem input;
            switch (se_fieldType) {
                case "colorpicker": {
                    String defaultValue = fieldData.containsKey("value") ? fieldData.getString("value") : "#ea4c4c";
                    input = WidgetSettingsItem.asColor(fieldId, fieldName, defaultValue);
                    break;
                }

                case "textfield": {
                    String defaultValue = fieldData.containsKey("value") ? fieldData.getString("value") : "";
                    input = WidgetSettingsItem.asText(fieldId, fieldName, defaultValue, "");
                    break;
                }

                case "slider": {
                    double defaultValue = fieldData.containsKey("value") ? fieldData.getNumber("value").doubleValue() : 0;
                    input = WidgetSettingsItem.asRange(fieldId, fieldName, defaultValue, fieldData.getNumber("steps"), fieldData.getNumber("min"), fieldData.getNumber("max"));
                    break;
                }

                case "fontpicker": {
                    String defaultValue = fieldData.containsKey("value") ? fieldData.getString("value") : "Poppins";
                    input = WidgetSettingsItem.asFont(fieldId, fieldName, defaultValue);
                    break;
                }

                case "image-input": {
                    input = WidgetSettingsItem.asFile(fieldId, fieldName, "image");
                    break;
                }

                case "sound-input": {
                    input = WidgetSettingsItem.asFile(fieldId, fieldName, "audio");
                    break;
                }

                // TODO SDK Updates...
//                case "dropdown": {
//                    input = WidgetSettingsItem.asDropdown(fieldId, fieldName, fieldName, null)
//                    break;
//                }

                default: {
                    String defaultValue = fieldData.containsKey("value") ? fieldData.getString("value") : "";
                    input = WidgetSettingsItem.asText(fieldId, fieldName, defaultValue, "Unknown type: " + se_fieldType);
                    break;
                }
            }

            fieldsSection.addItem(input);
        }
        layout.addSection(fieldsSection);

        return layout;
    }

    public static @Nullable Pair<String, String> getResource(@NonNull String resource) throws IOException {
        if (isShimMode()) {
            resource = "sesl" + resource;
        } else {
            resource = seslId + resource;
        }

        String mimeType = "application/octet-stream";

        String[] split = resource.split("\\.");
        if (split.length > 1) {
            mimeType = MimeTypes.getMimeForType(split[split.length - 1]);
        }

        LOGGER.debug("Loading resource: %s", resource);

        try (InputStream in = SESL.class.getClassLoader().getResourceAsStream(resource)) {
            return new Pair<>(
                IOUtil.readInputStreamString(in, StandardCharsets.UTF_8),
                mimeType
            );
        } catch (Exception e) {
            LOGGER.debug("An error occurred whilst loading resource %s:\n%s", resource, e);
            return new Pair<>("", "text/plain");
        }
    }

    public static JsonElement generateCustomData(SESLWidget widget) {
        return new JsonObject()
            .put("shimType", widget.getShimType())
            .put("customCSS", widget.getCustomCSS())
            .put("customJS", widget.getCustomJS())
            .put("customHTML", widget.getCustomHTML());
    }

}
