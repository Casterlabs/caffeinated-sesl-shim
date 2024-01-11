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
import co.casterlabs.rakurai.json.element.JsonObject;
import lombok.NonNull;
import xyz.e3ndr.fastloggingframework.logging.FastLogger;

public class SESL {
    public static final FastLogger LOGGER = new FastLogger();

    @SuppressWarnings("deprecation")
    public static WidgetSettingsLayout generateLayout(SESLWidget widget) {
        try {
            WidgetSettingsLayout layout = new WidgetSettingsLayout();

            Map<String, WidgetSettingsSection> sections = new HashMap<>();
            sections.put("settings", new WidgetSettingsSection("settings", "Settings"));

            // Default Streamlabs fields.
            switch (widget.getShimType()) {
                case "chatbox": {
                    // We intentionally leave out background_color.
                    sections.get("settings")
                        .addItem(
                            new WidgetSettingsColorBuilder()
                                .withId("text_color")
                                .withName("Text Color")
                                .withDefaultValue("#ffffff")
                                .build()
                        )
                        .addItem(
                            new WidgetSettingsNumberBuilder()
                                .withId("font_size")
                                .withName("Font Size (px)")
                                .withStep(1)
                                .withMin(0)
                                .withMax(80)
                                .withDefaultValue(18)
                                .build()
                        );
                    // TODO the rest...
                    break;
                }
            }

            // Convert the custom fields.
            JsonObject defaultsData = widget.getDataDefaults();
            for (Map.Entry<String, JsonElement> entry : widget.getFields().entrySet()) {
                String fieldId = entry.getKey();
                JsonObject fieldData = entry.getValue().getAsObject();

                String se_fieldType = fieldData.getString("type");
                String fieldName = fieldData.getString("label");

                String group = fieldData.containsKey("group") ? fieldData.getString("group") : "settings";

                WidgetSettingsItem input;
                switch (se_fieldType) {
                    case "colorpicker": {
                        String defaultValue = //
                            defaultsData.containsKey(fieldId) ? defaultsData.getString(fieldId) : //
                                fieldData.containsKey("value") ? fieldData.getString("value") : //
                                    "#ea4c4c";
                        input = new WidgetSettingsColorBuilder()
                            .withId(fieldId)
                            .withName(fieldName)
                            .withDefaultValue(defaultValue)
                            .build();
                        break;
                    }

                    case "textfield": {
                        String defaultValue = //
                            defaultsData.containsKey(fieldId) ? defaultsData.getString(fieldId) : //
                                fieldData.containsKey("value") ? fieldData.getString("value") : //
                                    "";
                        input = new WidgetSettingsTextBuilder()
                            .withId(fieldId)
                            .withName(fieldName)
                            .withDefaultValue(defaultValue)
                            .build();
                        break;
                    }

                    case "slider": {
                        Number defaultValue = //
                            defaultsData.containsKey(fieldId) ? defaultsData.getNumber(fieldId) : //
                                fieldData.containsKey("value") ? fieldData.getNumber("value") : //
                                    0;
                        Number steps = fieldData.containsKey("step") ? fieldData.getNumber("step") : 1;
                        Number min = fieldData.containsKey("min") ? fieldData.getNumber("min") : Integer.MIN_VALUE;
                        Number max = fieldData.containsKey("max") ? fieldData.getNumber("max") : Integer.MAX_VALUE;

                        input = new WidgetSettingsRangeBuilder()
                            .withId(fieldId)
                            .withName(fieldName)
                            .withStep(steps)
                            .withMin(min)
                            .withMax(max)
                            .withDefaultValue(defaultValue)
                            .build();
                        break;
                    }

                    case "fontpicker": {
                        String defaultValue = //
                            defaultsData.containsKey(fieldId) ? defaultsData.getString(fieldId) : //
                                fieldData.containsKey("value") ? fieldData.getString("value") : //
                                    "Poppins";
                        input = new WidgetSettingsFontBuilder()
                            .withId(fieldId)
                            .withName(fieldName)
                            .withDefaultValue(defaultValue)
                            .build();
                        break;
                    }

                    case "image-input": {
                        input = new WidgetSettingsFileBuilder()
                            .withId(fieldId)
                            .withName(fieldName)
                            .withAllowedTypes("image")
                            .build();

                        String defaultValue = //
                            defaultsData.containsKey(fieldId) ? defaultsData.getString(fieldId) : //
                                fieldData.containsKey("value") ? fieldData.getString("value") : //
                                    null;
                        if (!widget.settings().getJson().containsKey(group + "." + fieldId) && defaultValue != null) {
                            widget.settings().set(group + "." + fieldId, defaultValue);
                        }
                        break;
                    }

                    case "sound-input": {
                        input = new WidgetSettingsFileBuilder()
                            .withId(fieldId)
                            .withName(fieldName)
                            .withAllowedTypes("audio")
                            .build();

                        String defaultValue = //
                            defaultsData.containsKey(fieldId) ? defaultsData.getString(fieldId) : //
                                fieldData.containsKey("value") ? fieldData.getString("value") : //
                                    null;
                        if (!widget.settings().getJson().containsKey(group + "." + fieldId) && defaultValue != null) {
                            widget.settings().set(group + "." + fieldId, defaultValue);
                        }
                        break;
                    }

                    case "dropdown": {
                        String defaultValue = //
                            defaultsData.containsKey(fieldId) ? defaultsData.getString(fieldId) : //
                                fieldData.containsKey("value") ? fieldData.getString("value") : //
                                    "";

                        Map<String, String> optionsMap = new HashMap<>();
                        for (Map.Entry<String, JsonElement> opt : fieldData.getObject("options")) {
                            String key = opt.getKey();
                            String value = opt.getValue().getAsString();

                            if (defaultValue.equals(value)) {
                                defaultValue = key; // Strange...
                            }

                            optionsMap.put(key, value);
                        }

                        input = new WidgetSettingsDropdownBuilder()
                            .withId(fieldId)
                            .withName(fieldName)
                            .withDefaultValue(defaultValue)
                            .withOptions(optionsMap)
                            .build();
                        break;
                    }

                    default: {
                        String defaultValue = fieldData.containsKey("value") ? fieldData.getString("value") : defaultsData.containsKey(fieldId) ? defaultsData.getString(fieldId) : "";

                        input = new WidgetSettingsTextBuilder()
                            .withId(fieldId)
                            .withName(fieldName)
                            .withDefaultValue(defaultValue)
                            .withPlaceholder("Unknown type: " + se_fieldType)
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
