/**
 * MIT LICENSE
 *
 * Copyright (c) 2024 Alex Bowles @ Casterlabs
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package co.casterlabs.caffeinated.sesl;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.jetbrains.annotations.Nullable;

import co.casterlabs.caffeinated.pluginsdk.widgets.settings.WidgetSettingsItem;
import co.casterlabs.caffeinated.pluginsdk.widgets.settings.WidgetSettingsLayout;
import co.casterlabs.caffeinated.pluginsdk.widgets.settings.WidgetSettingsSection;
import co.casterlabs.caffeinated.pluginsdk.widgets.settings.items.WidgetSettingsCheckboxBuilder;
import co.casterlabs.caffeinated.pluginsdk.widgets.settings.items.WidgetSettingsColorBuilder;
import co.casterlabs.caffeinated.pluginsdk.widgets.settings.items.WidgetSettingsDropdownBuilder;
import co.casterlabs.caffeinated.pluginsdk.widgets.settings.items.WidgetSettingsFileBuilder;
import co.casterlabs.caffeinated.pluginsdk.widgets.settings.items.WidgetSettingsFontBuilder;
import co.casterlabs.caffeinated.pluginsdk.widgets.settings.items.WidgetSettingsNumberBuilder;
import co.casterlabs.caffeinated.pluginsdk.widgets.settings.items.WidgetSettingsRangeBuilder;
import co.casterlabs.caffeinated.pluginsdk.widgets.settings.items.WidgetSettingsTextBuilder;
import co.casterlabs.caffeinated.util.MimeTypes;
import co.casterlabs.commons.functional.tuples.Pair;
import co.casterlabs.rakurai.io.IOUtil;
import co.casterlabs.rakurai.json.element.JsonElement;
import co.casterlabs.rakurai.json.element.JsonNull;
import co.casterlabs.rakurai.json.element.JsonObject;
import lombok.NonNull;
import xyz.e3ndr.fastloggingframework.logging.FastLogger;

public class SESL {
    public static final FastLogger LOGGER = new FastLogger();

    @SuppressWarnings("deprecation")
    public static WidgetSettingsLayout generateLayout(SESLWidget widget) {
        try {
            Map<String, WidgetSettingsSection> sections = new HashMap<>();

            WidgetSettingsSection defaultSection = widget.getDefaultFields();
            if (defaultSection != null) {
                sections.put(defaultSection.getId(), defaultSection);
            }

            // Convert the custom fields.
            JsonObject defaultsData = widget.getDataDefaults();
            for (Map.Entry<String, JsonElement> entry : widget.getFields().entrySet()) {
                String fieldId = entry.getKey();
                JsonObject fieldData = entry.getValue().getAsObject();

                String type = fieldData.getString("type").toLowerCase();
                String fieldName = fieldData.containsKey("label") ? fieldData.getString("label") : "SESL_INVALID_LABEL";
                JsonElement defaultValue = fieldData.containsKey("value") ? fieldData.get("value") : defaultsData.containsKey(fieldId) ? defaultsData.get(fieldId) : JsonNull.INSTANCE;

                String group = fieldData.containsKey("group") ? fieldData.getString("group") : "settings";

                WidgetSettingsItem input;
                switch (type) {
                    case "colorpicker": {
                        input = new WidgetSettingsColorBuilder()
                            .withId(fieldId)
                            .withName(fieldName)
                            .withDefaultValue(defaultValue.isJsonString() ? defaultValue.getAsString() : "#ea4c4c")
                            .build();
                        break;
                    }

                    case "text":
                    case "textfield": {
                        input = new WidgetSettingsTextBuilder()
                            .withId(fieldId)
                            .withName(fieldName)
                            .withDefaultValue(defaultValue.isJsonString() ? defaultValue.getAsString() : "")
                            .build();
                        break;
                    }

                    case "number": {
                        Number steps = fieldData.containsKey("step") ? fieldData.getNumber("step") : 1;
                        Number min = fieldData.containsKey("min") ? fieldData.getNumber("min") : Integer.MIN_VALUE;
                        Number max = fieldData.containsKey("max") ? fieldData.getNumber("max") : Integer.MAX_VALUE;

                        input = new WidgetSettingsNumberBuilder()
                            .withId(fieldId)
                            .withName(fieldName)
                            .withStep(steps)
                            .withMin(min)
                            .withMax(max)
                            .withDefaultValue(defaultValue.isJsonNumber() ? defaultValue.getAsNumber() : 0)
                            .build();
                        break;
                    }

                    case "slider": {
                        Number steps = fieldData.containsKey("step") ? fieldData.getNumber("step") : 1;
                        Number min = fieldData.containsKey("min") ? fieldData.getNumber("min") : Integer.MIN_VALUE;
                        Number max = fieldData.containsKey("max") ? fieldData.getNumber("max") : Integer.MAX_VALUE;

                        input = new WidgetSettingsRangeBuilder()
                            .withId(fieldId)
                            .withName(fieldName)
                            .withStep(steps)
                            .withMin(min)
                            .withMax(max)
                            .withDefaultValue(defaultValue.isJsonNumber() ? defaultValue.getAsNumber() : 0)
                            .build();
                        break;
                    }

                    case "checkbox": {
                        input = new WidgetSettingsCheckboxBuilder()
                            .withId(fieldId)
                            .withName(fieldName)
                            .withDefaultValue(defaultValue.isJsonBoolean() ? defaultValue.getAsBoolean() : false)
                            .build();
                        break;
                    }

                    case "googlefont":
                    case "fontpicker": {
                        input = new WidgetSettingsFontBuilder()
                            .withId(fieldId)
                            .withName(fieldName)
                            .withDefaultValue(defaultValue.isJsonString() ? defaultValue.getAsString() : "Poppins")
                            .build();
                        break;
                    }

                    case "image-input": {
                        input = new WidgetSettingsFileBuilder()
                            .withId(fieldId)
                            .withName(fieldName)
                            .withAllowedTypes("image")
                            .build();

                        if (!widget.settings().getJson().containsKey(group + "." + fieldId) && defaultValue.isJsonString()) {
                            widget.settings().set(group + "." + fieldId, defaultValue.getAsString());
                        }
                        break;
                    }

                    case "sound-input": {
                        input = new WidgetSettingsFileBuilder()
                            .withId(fieldId)
                            .withName(fieldName)
                            .withAllowedTypes("audio")
                            .build();

                        if (!widget.settings().getJson().containsKey(group + "." + fieldId) && defaultValue.isJsonString()) {
                            widget.settings().set(group + "." + fieldId, defaultValue.getAsString());
                        }
                        break;
                    }

                    case "dropdown": {
                        String dv = //
                            defaultValue.isJsonString() ? defaultValue.getAsString() : "";

                        Map<String, String> optionsMap = new HashMap<>();
                        for (Map.Entry<String, JsonElement> opt : fieldData.getObject("options")) {
                            String key = opt.getKey();
                            String value = opt.getValue().getAsString();

                            if (dv.equals(value)) {
                                dv = key; // Strange...
                            }

                            optionsMap.put(key, value);
                        }

                        input = new WidgetSettingsDropdownBuilder()
                            .withId(fieldId)
                            .withName(fieldName)
                            .withDefaultValue(dv)
                            .withOptions(optionsMap)
                            .build();
                        break;
                    }

                    case "hidden":
                        continue;

                    default: {
                        LOGGER.warn("Unrecognized type: %s", fieldData);
                        input = new WidgetSettingsTextBuilder()
                            .withId(fieldId)
                            .withName("Unsupported: " + fieldName)
                            .withDefaultValue(defaultValue.isJsonNull() ? "" : defaultValue.toString())
                            .withPlaceholder("Unknown type: " + type)
                            .build();
                        break;
                    }
                }

                WidgetSettingsSection section = sections.get(group);

                if (section == null) {
                    section = new WidgetSettingsSection(group, group);
                    sections.put(group, section);
                }

                section.addItem(input);
            }

            WidgetSettingsLayout layout = new WidgetSettingsLayout();
            for (WidgetSettingsSection section : sections.values()) {
                layout.addSection(section);
            }
            return layout;
        } catch (Throwable t) {
            LOGGER.fatal("An error occurred whilst building layout:\n%s", t);
            return new WidgetSettingsLayout(); // Empty.
        }
    }

    public static @Nullable Pair<String, String> getResource(@NonNull String resource) throws IOException {
        resource = "sesl" + resource;

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
