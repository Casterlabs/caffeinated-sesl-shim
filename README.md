# caffeinated-sesl-shim

Caffeinated /sEE-sEl/ Shim

## Preramble

Streamlabs and StreamElements allow you to inject custom html into your chatbox, goals, and other widgets. Both platforms share a common format, allowing artists like NerdOrDie to easily create their own custom widgets. The goal of this project is to bridge the difference between the Caffeinated SDK and what Streamlabs/StreamElements expect, allowing you to directly run these widgets _mostly_ unmodified in Casterlabs.

## Usage

There are two modes of usage, [Carrier](#Carrier%20Mode) and [Shim](#Shim%20Mode)

### Shim Mode

This is for non-technical users of Casterlabs who wish to bring the custom code that they paid for over to Casterlabs.

1. Download the "Sesl-Shim.jar" from the [Latest Release](https://github.com/Casterlabs/caffeinated-sesl-shim/releases/latest).
2. Place the downloaded Jar into your plugins folder, you can get there by opening Caffeinated, navigating to Settings > Plugins, and clicking the "Open plugins folder" button at the bottom.

- The plugin will be automatically loaded every time you open Caffeinated.
  - If you added it to the app while it's running, you can click the "Load" button next to the file in Settings > Plugins to load it without needing to close Caffeinated.

3. Go to the Widgets page, hover over the +, and click Other > SESL.
4. Under the SESL Tab:  
   a. Select the type of widget you have (Goal, Chatbox, etc).  
   b. Paste in your CSS, Data, Fields, HTML, and JS texts.  
   Once you paste in the Fields you will see new a new "Settings" tab, in there you can select the code-specific settings (font size, colors, etc).
5. Click the copy link button and add to OBS like normal. Et Voilà!

#### The custom code keeps giving errors.

This may be due to the code expecting the platform to always be Twitch or YouTube. In that case, you can enable "Forced compatibility mode" under the SESL tab. This will cause the code to always think that the event came from Twitch instead of another platform like Kick or TikTok.

### Carrier Mode

When in Carrier mode, you package your code and give your client the resulting Jar. This will make your widgets appear as if they were native to Caffeinated. It is recommended that you try your best to update your Plugin as SESL evolves, as there may be new incompatibilities in the SDK that require an updated SESL Jar.

// TODO Carrier instructions, give Nathan a high-five.

## Development

Development of SESL is the same as any other Caffeinated plugin. All you need is Maven, Java 11, and a good IDE (VSCode is fine).

To build, just run `mvn install` in the root directory. The resulting jars will be in the `target` folder in `Modes/Carrier-Mode` and `Mode/Shim-Mode`.
