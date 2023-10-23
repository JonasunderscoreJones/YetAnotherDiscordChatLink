# Yet Another Discord Chat Link

<a href="https://github.com/J-onasJones/YetAnotherDiscordChatLink/blob/master/LICENSE"><img src="https://img.shields.io/github/license/J-onasJones/YetAnotherDiscordChatLink?style=flat&color=900c3f" alt="License"></a>
<a href="https://discord.gg/V2EsuUVmWh"><img src="https://img.shields.io/discord/702180921234817135?color=5865f2&label=Discord&style=flat" alt="Discord"></a>
<a href="https://www.curseforge.com/minecraft/mc-mods/yet-another-discord-chat-link/"><img src="https://cf.way2muchnoise.eu/full_649823.svg" alt="CF"></a>
<a href="https://modrinth.com/mod/yet-another-discord-chat-link"><img src="https://img.shields.io/modrinth/dt/yet-another-discord-chat-link?logo=modrinth&label=&style=flat&color=242629&labelColor=00AF5C&logoColor=white" alt="Modrinth"></a>
<a href="https://modrinth.com/mod/yet-another-discord-chat-link"><img src="https://img.shields.io/modrinth/game-versions/yet-another-discord-chat-link?logo=modrinth&color=242629&labelColor=00AF5C&logoColor=white"></a>

<a align="center"><img src="https://cdn.jonasjones.dev/mod-badges/support-fabric.png" width="250px"><img src="https://cdn.jonasjones.dev/mod-badges/support-quilt.png" width="250px"><img src="https://cdn.jonasjones.dev/mod-badges/fabric-api.png" width="250px"><img src="https://cdn.jonasjones.dev/mod-badges/no-support-forge.png" width="250px"><img src="https://cdn.jonasjones.dev/mod-badges/available-modrinth.png" width="250px"></a>


A simple mod to connect the in-game chat with a Discord channel.

## Why another one?
- This one focuses on simplicity. It is as light as it gets with only the basic functionality and very little required setup!

**Setup:**
1. Go to https://discord.com/developers and create a new application
2. Copy the ID and invite the bot with the following url (dont forget to change the ID or else the link won't work:
```url
https://discord.com/oauth2/authorize?permissions=2953964624&scope=bot%20applications.commands&client_id=APPLICATION_ID
```

3. Enable developer mode on discord to be able to right-click a channel and copy its channel ID
4. Download the mod into the mods folder and let it generate the config file. The server will crash on first launch since the Bot Token is invalid.
5. Paste the copied channel ID of the channel that you would like the chat to be linked to into the config file
6. On the discord developer dashboard, in your application, go to the bot tab on the sidebar and reset and copy the bot token and paste it into the config file.
7. Restart the server and you should be good to go. Check that the bot has read/write permission in that channel.

*Alternative suggested Setup Guide (not by me):*
https://erdbeerbaerlp.de/projects/discord-integration/quick-setup