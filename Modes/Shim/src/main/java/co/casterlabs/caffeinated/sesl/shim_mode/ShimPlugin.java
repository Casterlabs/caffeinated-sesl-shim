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

import java.io.IOException;

import org.jetbrains.annotations.Nullable;

import co.casterlabs.caffeinated.pluginsdk.CaffeinatedPlugin;
import co.casterlabs.caffeinated.pluginsdk.CaffeinatedPluginImplementation;
import co.casterlabs.caffeinated.sesl.SESL;
import co.casterlabs.commons.functional.tuples.Pair;
import lombok.NonNull;

@CaffeinatedPluginImplementation
public class ShimPlugin extends CaffeinatedPlugin {

    @Override
    public void onInit() {
        if (!SESL.isShimMode()) {
            this.getLogger().warn("SESL is not in shim mode yet the Shim plugin was loaded. I hope you know what you are doing...");
        }

        this.getPlugins().registerWidget(this, ShimWidget.DETAILS, ShimWidget.class);
    }

    @Override
    public void onClose() {}

    @Override
    public @NonNull String getName() {
        return "SESL Shim";
    }

    @Override
    public @NonNull String getId() {
        return "co.casterlabs.caffeinated.sesl.shim_mode.plugin";
    }

    @Override
    public @Nullable Pair<String, String> getResource(String resource) throws IOException {
        return SESL.getResource(resource);
    }

}
