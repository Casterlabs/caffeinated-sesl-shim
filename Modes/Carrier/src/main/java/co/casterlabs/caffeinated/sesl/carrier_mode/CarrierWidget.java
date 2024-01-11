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

import java.nio.charset.StandardCharsets;

import org.jetbrains.annotations.Nullable;

import co.casterlabs.caffeinated.pluginsdk.widgets.settings.WidgetSettingsSection;
import co.casterlabs.caffeinated.sesl.SESL;
import co.casterlabs.caffeinated.sesl.SESLWidget;
import co.casterlabs.caffeinated.sesl.carrier_mode.CarrierConfig.ConfigWidget;
import co.casterlabs.commons.io.streams.StreamUtil;
import co.casterlabs.rakurai.json.Rson;
import co.casterlabs.rakurai.json.element.JsonObject;
import lombok.Getter;
import lombok.NonNull;
import lombok.SneakyThrows;

public class CarrierWidget extends SESLWidget {
    private ConfigWidget config;

    private @Getter JsonObject fields;
    private @Getter JsonObject dataDefaults;
    private @Getter String customCSS;
    private @Getter String customJS;
    private @Getter String customHTML;

    @SneakyThrows
    public CarrierWidget(ConfigWidget config) {
        this.config = config;

        this.fields = Rson.DEFAULT.fromJson(StreamUtil.toString(CarrierPlugin.class.getResourceAsStream("/sesl/" + config.id + "/fields.json"), StandardCharsets.UTF_8), JsonObject.class);
        this.dataDefaults = Rson.DEFAULT.fromJson(StreamUtil.toString(CarrierPlugin.class.getResourceAsStream("/sesl/" + config.id + "/data.json"), StandardCharsets.UTF_8), JsonObject.class);

        this.customCSS = StreamUtil.toString(CarrierPlugin.class.getResourceAsStream("/sesl/" + config.id + "/custom.css"), StandardCharsets.UTF_8);
        this.customJS = StreamUtil.toString(CarrierPlugin.class.getResourceAsStream("/sesl/" + config.id + "/custom.js"), StandardCharsets.UTF_8);
        this.customHTML = StreamUtil.toString(CarrierPlugin.class.getResourceAsStream("/sesl/" + config.id + "/custom.html"), StandardCharsets.UTF_8);
    }

    @Override
    public @Nullable WidgetSettingsSection getDefaultFields() {
        if (this.config.addDefaultFields) {
            return super.getDefaultFields();
        } else {
            return null;
        }
    }

    @Override
    public @NonNull String getShimType() {
        return this.config.type;
    }

    @Override
    protected void onSettingsUpdate() {
        this.broadcastToAll("new-settings"); // Tell all of the Widgets to reload.
        this.setSettingsLayout(SESL.generateLayout(this));
    }

}
