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

import org.jetbrains.annotations.Nullable;

import co.casterlabs.caffeinated.pluginsdk.widgets.Widget;
import co.casterlabs.caffeinated.pluginsdk.widgets.WidgetInstance;
import co.casterlabs.caffeinated.pluginsdk.widgets.WidgetInstanceMode;
import co.casterlabs.caffeinated.pluginsdk.widgets.settings.WidgetSettingsSection;
import co.casterlabs.caffeinated.pluginsdk.widgets.settings.items.WidgetSettingsColorBuilder;
import co.casterlabs.caffeinated.pluginsdk.widgets.settings.items.WidgetSettingsNumberBuilder;
import co.casterlabs.rakurai.json.element.JsonObject;
import lombok.NonNull;

public abstract class SESLWidget extends Widget {

    public abstract @NonNull String getShimType();

    public abstract @NonNull JsonObject getFields();

    public abstract @NonNull JsonObject getDataDefaults();

    public abstract @NonNull String getCustomCSS();

    public abstract @NonNull String getCustomJS();

    public abstract @NonNull String getCustomHTML();

    @SuppressWarnings("deprecation")
    public @Nullable WidgetSettingsSection getDefaultFields() {
        // Default Streamlabs fields.
        switch (this.getShimType()) {
            case "chatbox":
                // We intentionally leave out background_color.
                return new WidgetSettingsSection("settings", "Settings")
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
            default:
                return null;
        }
    }

    @Override
    protected void onSettingsUpdate() {
        this.setSettingsLayout(SESL.generateLayout(this));
    }

    @Override
    public void onNewInstance(@NonNull WidgetInstance instance) {
        try {
            instance.emit("custom-data", SESL.generateCustomData(this));
        } catch (IOException ignored) {}
    }

    @Override
    public @NonNull String getWidgetBasePath(WidgetInstanceMode mode) {
        return "/shim.html";
    }

}
