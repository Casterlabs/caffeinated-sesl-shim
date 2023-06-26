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

public class SESLExamples {

    public static final String customCSS = "@import url(https://fonts.googleapis.com/css?family=Roboto:700);\r\n"
        + "\r\n"
        + "* {\r\n"
        + "    box-sizing: border-box;\r\n"
        + "}\r\n"
        + "\r\n"
        + "html, body {\r\n"
        + "    height: 100%;\r\n"
        + "    overflow: hidden;\r\n"
        + "}\r\n"
        + "\r\n"
        + "body {\r\n"
        + "    text-shadow: 0 0 1px #000, 0 0 2px #000;\r\n"
        + "    background: {background_color};\r\n"
        + "    font-family: 'Roboto';\r\n"
        + "    font-weight: 700;\r\n"
        + "    font-size: {font_size};\r\n"
        + "    line-height: 1.5em;\r\n"
        + "    color: {text_color};\r\n"
        + "}\r\n"
        + "\r\n"
        + "#log>div {\r\n"
        + "    animation: fadeInRight .3s ease forwards, fadeOut 0.5s ease {message_hide_delay} forwards;\r\n"
        + "    -webkit-animation: fadeInRight .3s ease forwards, fadeOut 0.5s ease {message_hide_delay} forwards;\r\n"
        + "}\r\n"
        + "\r\n"
        + ".colon {\r\n"
        + "    display: none;\r\n"
        + "}\r\n"
        + "\r\n"
        + "#log {\r\n"
        + "    display: table;\r\n"
        + "    position: absolute;\r\n"
        + "    bottom: 0;\r\n"
        + "    left: 0;\r\n"
        + "    padding: 0 10px 10px;\r\n"
        + "    width: 100%;\r\n"
        + "    table-layout: fixed;\r\n"
        + "}\r\n"
        + "\r\n"
        + "#log>div {\r\n"
        + "    display: table-row;\r\n"
        + "}\r\n"
        + "\r\n"
        + "#log>div.deleted {\r\n"
        + "    visibility: hidden;\r\n"
        + "}\r\n"
        + "\r\n"
        + "#log .emote {\r\n"
        + "    background-repeat: no-repeat;\r\n"
        + "    background-position: center;\r\n"
        + "    background-size: contain;\r\n"
        + "    padding: 0.4em 0.2em;\r\n"
        + "    position: relative;\r\n"
        + "}\r\n"
        + "\r\n"
        + "#log .emote img {\r\n"
        + "    display: inline-block;\r\n"
        + "    height: 1em;\r\n"
        + "    opacity: 0;\r\n"
        + "}\r\n"
        + "\r\n"
        + "#log .message,#log .meta {\r\n"
        + "    vertical-align: top;\r\n"
        + "    display: table-cell;\r\n"
        + "    padding-bottom: 0.1em;\r\n"
        + "}\r\n"
        + "\r\n"
        + "#log .meta {\r\n"
        + "    width: 35%;\r\n"
        + "    text-align: right;\r\n"
        + "    padding-right: 0.5em;\r\n"
        + "    white-space: nowrap;\r\n"
        + "    text-overflow: ellipsis;\r\n"
        + "    overflow: hidden;\r\n"
        + "}\r\n"
        + "\r\n"
        + "#log .message {\r\n"
        + "    word-wrap: break-word;\r\n"
        + "    width: 65%;\r\n"
        + "}\r\n"
        + "\r\n"
        + ".badge {\r\n"
        + "    display: inline-block;\r\n"
        + "    margin-right: 0.2em;\r\n"
        + "    position: relative;\r\n"
        + "    height: 1em;\r\n"
        + "    vertical-align: middle;\r\n"
        + "    top: -0.1em;\r\n"
        + "}\r\n"
        + "\r\n"
        + ".name {\r\n"
        + "    margin-left: 0.2em;\r\n"
        + "}";

    public static final String customJS = "// Please use event listeners to run functions.\r\n"
        + "document.addEventListener('onLoad', function(obj) {\r\n"
        + "    // obj will be empty for chat widget\r\n"
        + "    // this will fire only once when the widget loads\r\n"
        + "});\r\n"
        + "\r\n"
        + "document.addEventListener('onEventReceived', function(obj) {\r\n"
        + "    // obj will contain information about the event\r\n"
        + "    \r\n"
        + "});";

    public static final String customHTML = "<!-- item will be appened to this layout -->\r\n"
        + "<div id=\"log\" class=\"sl__chat__layout\">\r\n"
        + "</div>\r\n"
        + "\r\n"
        + "<!-- chat item -->\r\n"
        + "<script type=\"text/template\" id=\"chatlist_item\">\r\n"
        + "  <div data-from=\"{from}\" data-id=\"{messageId}\">\r\n"
        + "    <span class=\"meta\" style=\"color: {color}\">\r\n"
        + "      <span class=\"badges\">\r\n"
        + "      </span>\r\n"
        + "      <span class=\"name\">{from}</span>\r\n"
        + "    </span>\r\n"
        + "\r\n"
        + "    <span class=\"message\">\r\n"
        + "      {message}\r\n"
        + "    </span>\r\n"
        + "  </div>\r\n"
        + "</script>";

    public static final String fields = "{\r\n"
        + "    \"customField1\": {\r\n"
        + "        \"label\": \"Color Picker Example\",\r\n"
        + "        \"type\": \"colorpicker\",\r\n"
        + "        \"value\": \"#000EF0\"\r\n"
        + "    },\r\n"
        + "    \"customField2\": {\r\n"
        + "        \"label\": \"Slider Example\",\r\n"
        + "        \"type\": \"slider\",\r\n"
        + "        \"name\": \"\",\r\n"
        + "        \"value\": \"3\",\r\n"
        + "        \"max\": 200,\r\n"
        + "        \"min\": 100,\r\n"
        + "        \"steps\": 4\r\n"
        + "    },\r\n"
        + "    \"customField3\": {\r\n"
        + "        \"label\": \"Textfield Example\",\r\n"
        + "        \"type\": \"textfield\",\r\n"
        + "        \"value\": \"Hi There\"\r\n"
        + "    },\r\n"
        + "    \"customField4\": {\r\n"
        + "        \"label\": \"Font Picker Example\",\r\n"
        + "        \"type\": \"fontpicker\",\r\n"
        + "        \"value\": \"Open Sans\"\r\n"
        + "    },\r\n"
        + "    \"customField5\": {\r\n"
        + "        \"label\": \"Dropdown Example\",\r\n"
        + "        \"type\": \"dropdown\",\r\n"
        + "        \"options\": {\r\n"
        + "            \"optionA\": \"Option A\",\r\n"
        + "            \"optionB\": \"Option B\",\r\n"
        + "            \"optionC\": \"Option C\"\r\n"
        + "        },\r\n"
        + "        \"value\": \"optionB\"\r\n"
        + "    },\r\n"
        + "    \"customField6\": {\r\n"
        + "        \"label\": \"Image Input Example\",\r\n"
        + "        \"type\": \"image-input\",\r\n"
        + "        \"value\": null\r\n"
        + "    },\r\n"
        + "    \"customField7\": {\r\n"
        + "        \"label\": \"Sound Input Example\",\r\n"
        + "        \"type\": \"sound-input\",\r\n"
        + "        \"value\": null\r\n"
        + "    }\r\n"
        + "}";

}
