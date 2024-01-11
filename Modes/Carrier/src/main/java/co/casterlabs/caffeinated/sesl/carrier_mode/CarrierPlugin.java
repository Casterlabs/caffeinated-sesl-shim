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
package co.casterlabs.caffeinated.sesl.carrier_mode;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.jetbrains.annotations.Nullable;

import co.casterlabs.caffeinated.pluginsdk.Caffeinated;
import co.casterlabs.caffeinated.pluginsdk.CaffeinatedPlugin;
import co.casterlabs.caffeinated.pluginsdk.CaffeinatedPluginImplementation;
import co.casterlabs.caffeinated.pluginsdk.widgets.WidgetDetails;
import co.casterlabs.caffeinated.sesl.SESL;
import co.casterlabs.caffeinated.sesl.carrier_mode.CarrierConfig.ConfigWidget;
import co.casterlabs.commons.functional.tuples.Pair;
import co.casterlabs.commons.io.streams.StreamUtil;
import co.casterlabs.rakurai.json.Rson;
import lombok.NonNull;

@CaffeinatedPluginImplementation
public class CarrierPlugin extends CaffeinatedPlugin {
    public static CarrierConfig config;
    static {
        try {
            String raw = StreamUtil.toString(CarrierPlugin.class.getResourceAsStream("/sesl/carrier.json"), StandardCharsets.UTF_8);
            config = Rson.DEFAULT.fromJson(raw, CarrierConfig.class);
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    @Override
    public void onInit() {
        for (ConfigWidget w : config.widgets) {
            WidgetDetails details = new WidgetDetails()
                .withNamespace(config.id + "." + w.id)
                .withIcon(w.icon)
                .withCategory(w.category)
                .withFriendlyName(w.name)
                .withShowDemo(true, 3 / 4d);

            Caffeinated.getInstance().getPlugins().registerWidgetFactory(
                this,
                details,
                (_unused) -> {
                    return new CarrierWidget(w);
                }
            );
        }
    }

    @Override
    public void onClose() {}

    @Override
    public @NonNull String getName() {
        return config.name;
    }

    @Override
    public @NonNull String getId() {
        return config.id;
    }

    @Override
    public @Nullable Pair<String, String> getResource(String resource) throws IOException {
        return SESL.getResource(resource);
    }

}
