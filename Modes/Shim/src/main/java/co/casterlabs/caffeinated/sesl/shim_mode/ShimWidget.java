/*
 * Copyright 2023 Casterlabs
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package co.casterlabs.caffeinated.sesl.shim_mode;

import co.casterlabs.caffeinated.pluginsdk.widgets.WidgetDetails;
import co.casterlabs.caffeinated.pluginsdk.widgets.WidgetDetails.WidgetDetailsCategory;
import co.casterlabs.caffeinated.sesl.SESLWidget;
import co.casterlabs.rakurai.json.Rson;
import co.casterlabs.rakurai.json.element.JsonObject;
import lombok.NonNull;
import lombok.SneakyThrows;

public class ShimWidget extends SESLWidget {

    public static final WidgetDetails DETAILS = new WidgetDetails()
        .withNamespace("co.castelrabs.caffeinated.sesl.shim_mode.widget")
        .withIcon("puzzle-piece")
        .withCategory(WidgetDetailsCategory.OTHER)
        .withFriendlyName("SESL")
        .withShowDemo(true, 3 / 4d);

    @Override
    public @NonNull String getShimType() {
        return "chatbox"; // TODO This'll work for now.
//        return this.settings().getString("sesl.shim_type", "");
    }

    @Override
    @SneakyThrows
    public @NonNull JsonObject getFields() {
        return Rson.DEFAULT.fromJson(this.settings().getString("sesl.custom_fields", "{}"), JsonObject.class);
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