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

import co.casterlabs.caffeinated.pluginsdk.widgets.Widget;
import co.casterlabs.caffeinated.pluginsdk.widgets.WidgetInstance;
import co.casterlabs.rakurai.json.Rson;
import co.casterlabs.rakurai.json.element.JsonObject;
import lombok.NonNull;
import lombok.SneakyThrows;

public abstract class SESLWidget extends Widget {

    /**
     * Override as needed.
     */
    public @NonNull String getShimType() {
        return "chatbox"; // TODO This'll work for now.
//        return this.settings().getString("sesl.shim_type", "");
    }

    /**
     * Override as needed.
     */
    @SneakyThrows
    public @NonNull JsonObject getFields() {
        return Rson.DEFAULT.fromJson(this.settings().getString("sesl.custom_fields", "{}"), JsonObject.class);
    }

    /**
     * Override as needed.
     */
    public @NonNull String getCustomCSS() {
        return this.settings().getString("sesl.custom_css", "");
    }

    /**
     * Override as needed.
     */
    public @NonNull String getCustomJS() {
        return this.settings().getString("sesl.custom_js", "");
    }

    /**
     * Override as needed.
     */
    public @NonNull String getCustomHTML() {
        return this.settings().getString("sesl.custom_html", "");
    }

    @Override
    protected void onSettingsUpdate() {
        this.setSettingsLayout(SESL.generateLayout(this));
    }

    @Override
    public void onNewInstance(@NonNull WidgetInstance instance) {
        instance.on("want-custom-data", () -> {
            try {
                instance.emit("custom-data", SESL.generateCustomData(this));
            } catch (IOException ignored) {}
        });
    }

}
