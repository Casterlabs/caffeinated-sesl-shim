// Chat events. (PRIVMSG)
Koi.on("RICH_MESSAGE", (event) => {
  let message = "";

  for (const fragment of event.fragments) {
    switch (fragment.type) {
      case "EMOTE":
        message += `<span class="emote" style="background-image: url(${fragment.imageLink});"><img src="${fragment.imageLink}"></span>`;
        break;

      case "TEXT":
        message += fragment.html;
        break;

      default:
        message += escapeHtml(fragment.raw);
        break;
    }
  }

  const streamlabsEvent = {
    priority: 10,
    pic: event.sender.image_link,
    from: event.sender.displayname,
    body: message,
    command: "PRIVMSG",
    tags: {
      "badge-info": "",
      badges: "",
      //   "client-nonce": "208e0792c078f507d8b9c55586846a78",
      color: event.sender.color,
      "display-name": event.sender.displayname,
      emotes: "",
      "first-msg": "0",
      flags: "",
      id: event.id,
      mod: "0",
      "returning-chatter": "0",
      "room-id": event.streamer.channel_id,
      subscriber: "0",
      "tmi-sent-ts": Date.now().toString(),
      turbo: "0",
      "user-id": event.sender.id,
      "user-type": "",
    },
    payload: {
      raw: event, // Ick.
    },
    owner: false,
    subscriber: false,
    userType: "",
    platform: `${event.sender.platform.toLowerCase()}_account`,
    platformAccountId: null,
    messageId: event.id.replaceAll(/=/g, ''),
    access_token: null,
    to: `#${event.streamer.username}`,
  };

  // Do the chatlist_item behavior that Streamlabs implements.
  (() => {
    let chatlistItemTemplate = document.querySelector(
      'script[type="text/template"]#chatlist_item'
    )?.innerText;
    const chatLayoutElement = document.querySelector("#log.sl__chat__layout");

    if (!chatlistItemTemplate || !chatLayoutElement) return;

    // Go through all of the custom fields/settings and replace them.
    for (const [field, value] of Object.entries(allFields)) {
      if (
        typeof value == "string" &&
        value.startsWith("{") &&
        value.endsWith("}")
      ) {
        let correctValue = allFields[value.substring(1, value.length - 1)];
        if (correctValue === undefined) {
          correctValue = value; // It's handled down below.
        }

        chatlistItemTemplate = chatlistItemTemplate.replaceAll(
          new RegExp(`\\{${field}\\}`, "g"),
          correctValue
        );
      } else {
        chatlistItemTemplate = chatlistItemTemplate.replaceAll(
          new RegExp(`\\{${field}\\}`, "g"),
          value
        );
      }
    }

    // Go through all event fields and replace them.
    for (const [field, value] of Object.entries(streamlabsEvent)) {
      chatlistItemTemplate = chatlistItemTemplate.replaceAll(
        new RegExp(`\\{${field}\\}`, "g"),
        value
      );
    }
    for (const [field, value] of Object.entries(streamlabsEvent.tags)) {
      chatlistItemTemplate = chatlistItemTemplate.replaceAll(
        new RegExp(`\\{${field}\\}`, "g"),
        value
      );
    }

    chatlistItemTemplate = chatlistItemTemplate.replaceAll(
      /\{message\}/g,
      message
    );

    const chatlistItemTemplate_el = document.createElement("div");

    chatlistItemTemplate_el.innerHTML = chatlistItemTemplate;

    // Inject the badges.
    const badgesList = chatlistItemTemplate_el.querySelector(".badges");
    if (badgesList) {
      for (const badge of event.sender.badges) {
        badgesList.innerHTML += `<img src="${badge}" class="badge" />`; // TODO additional badge classes.
      }
    }

    chatLayoutElement.appendChild(chatlistItemTemplate_el);
    chatlistItemTemplate_el.outerHTML = chatlistItemTemplate_el.innerHTML;
  })();

  console.debug("Sending SL event:", streamlabsEvent);
  document.dispatchEvent(
    new CustomEvent("onEventReceived", { detail: streamlabsEvent })
  );
  window.dispatchEvent(
    new CustomEvent("onEventReceived", { detail: streamlabsEvent })
  );
});

Koi.on("RICH_MESSAGE", (event) => {
  const streamelementsEvent = {
    listener: "message",
    event: {
      data: {
        time: event.timestamp,
        tags: {
          "badge-info": "",
          badges: "",
          //   "client-nonce": "208e0792c078f507d8b9c55586846a78",
          color: event.sender.color,
          "display-name": event.sender.displayname,
          emotes: "",
          "first-msg": "0",
          flags: "",
          id: event.id,
          mod: "0",
          "returning-chatter": "0",
          "room-id": event.streamer.channel_id,
          subscriber: "0",
          "tmi-sent-ts": Date.now().toString(),
          turbo: "0",
          "user-id": event.sender.id,
          "user-type": "",
        },
        nick: event.sender.username,
        userId: event.sender.id,
        displayName: event.sender.displayname,
        displayColor: event.sender.id,
        badges: [
          // {
          //   type: "broadcaster",
          //   version: "1",
          //   url: "https://static-cdn.jtvnw.net/badges/v1/5527c58c-fb7d-422d-b71b-f309dcb85cc1/3",
          //   description: "Broadcaster",
          // },
        ],
        channel: event.streamer.username,
        text: event.raw,
        isAction: false,
        emotes: [
          // {
          //   type: "twitch",
          //   name: "Kappa",
          //   id: "25",
          //   gif: false,
          //   urls: {
          //     1: "https://static-cdn.jtvnw.net/emoticons/v1/25/1.0",
          //     2: "https://static-cdn.jtvnw.net/emoticons/v1/25/2.0",
          //     4: "https://static-cdn.jtvnw.net/emoticons/v1/25/4.0",
          //   },
          //   start: 5,
          //   end: 9,
          // },
        ],
        msgId: event.id.replaceAll(/=/g, ''),
        raw: event, // Ick.
      },
    },
  };

  console.debug("Sending SE event:", streamelementsEvent);
  document.dispatchEvent(
    new CustomEvent("onEventReceived", { detail: streamelementsEvent })
  );
  window.dispatchEvent(
    new CustomEvent("onEventReceived", { detail: streamelementsEvent })
  );
});
