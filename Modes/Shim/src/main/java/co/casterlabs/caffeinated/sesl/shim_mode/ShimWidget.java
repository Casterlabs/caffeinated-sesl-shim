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
package co.casterlabs.caffeinated.sesl.shim_mode;

import co.casterlabs.caffeinated.pluginsdk.widgets.WidgetDetails;
import co.casterlabs.caffeinated.pluginsdk.widgets.WidgetDetails.WidgetDetailsCategory;
import co.casterlabs.caffeinated.pluginsdk.widgets.settings.WidgetSettingsSection;
import co.casterlabs.caffeinated.pluginsdk.widgets.settings.items.WidgetSettingsCodeBuilder;
import co.casterlabs.caffeinated.pluginsdk.widgets.settings.items.WidgetSettingsDropdownBuilder;
import co.casterlabs.caffeinated.sesl.SESL;
import co.casterlabs.caffeinated.sesl.SESLExamples;
import co.casterlabs.caffeinated.sesl.SESLWidget;
import co.casterlabs.rakurai.json.Rson;
import co.casterlabs.rakurai.json.element.JsonObject;
import lombok.NonNull;
import lombok.SneakyThrows;

public class ShimWidget extends SESLWidget {

    public static final WidgetDetails DETAILS = new WidgetDetails()
        .withNamespace("co.casterlabs.caffeinated.sesl.shim_mode.widget")
        .withIcon("code-bracket")
        .withCategory(WidgetDetailsCategory.OTHER)
        .withFriendlyName("SESL")
        .withShowDemo(true, 3 / 4d);

    @Override
    public @NonNull String getShimType() {
        return this.settings().getString("sesl.shim_type", "");
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onSettingsUpdate() {
        this.broadcastToAll("new-settings"); // Tell all of the Widgets to reload.

        this.setSettingsLayout(
            SESL.generateLayout(this)
                .addSection(
                    new WidgetSettingsSection("sesl", "SESL")
                        .addItem(
                            new WidgetSettingsDropdownBuilder()
                                .withId("shim_type")
                                .withName("Type")
                                .withDefaultValue("chatbox")
                                .withOption("chatbox", "Chat Box")
                                .build()
                        )
                        .addItem(
                            new WidgetSettingsCodeBuilder()
                                .withId("fields")
                                .withName("Fields / Settings")
                                .withDefaultValue("{}")
                                .withLanguage("json")
                                .build()
                        )
                        .addItem(
                            new WidgetSettingsCodeBuilder()
                                .withId("data")
                                .withName("Data / Defaults")
                                .withDefaultValue("{}")
                                .withLanguage("json")
                                .build()
                        )
                        .addItem(
                            new WidgetSettingsCodeBuilder()
                                .withId("custom_css")
                                .withName("Custom CSS")
                                .withDefaultValue(SESLExamples.customCSS)
                                .withLanguage("css")
                                .build()
                        )
                        .addItem(
                            new WidgetSettingsCodeBuilder()
                                .withId("custom_js")
                                .withName("Custom JS")
                                .withDefaultValue(SESLExamples.customJS)
                                .withLanguage("javascript")
                                .build()
                        )
                        .addItem(
                            new WidgetSettingsCodeBuilder()
                                .withId("custom_html")
                                .withName("Custom HTML")
                                .withDefaultValue(SESLExamples.customHTML)
                                .withLanguage("html")
                                .build()
                        )
                )
        );
    }

    @Override
    @SneakyThrows
    public @NonNull JsonObject getFields() {
        return Rson.DEFAULT.fromJson(this.settings().getString("sesl.fields", "{}"), JsonObject.class);
    }

    @Override
    @SneakyThrows
    public @NonNull JsonObject getDataDefaults() {
        return Rson.DEFAULT.fromJson(this.settings().getString("sesl.data", "{}"), JsonObject.class);
    }

    @Override
    public @NonNull String getCustomCSS() {
        return this.settings().getString("sesl.custom_css", "");
    }

    @Override
    public @NonNull String getCustomJS() {
        return this.settings().getString("sesl.custom_js", "");
    }

    @Override
    public @NonNull String getCustomHTML() {
        return this.settings().getString("sesl.custom_html", "");
    }

}
