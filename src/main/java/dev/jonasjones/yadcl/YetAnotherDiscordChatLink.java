package dev.jonasjones.yadcl;

import dev.jonasjones.yadcl.config.ModConfigs;
import dev.jonasjones.yadcl.dcbot.DiscordBot;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static dev.jonasjones.yadcl.dcbot.DiscordBot.sendToDiscord;

public class YetAnotherDiscordChatLink implements ModInitializer {
    // This logger is used to write text to the console and the log file.
    // It is considered best practice to use your mod id as the logger's name.
    // That way, it's clear which mod wrote info, warnings, and errors.
    public static final String MOD_ID = "yadcl";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        // Register the config
        ModConfigs.registerConfigs();

        // Set the token and channel id
        DiscordBot.setToken(ModConfigs.TOKEN);
        DiscordBot.setTargetChannelId(ModConfigs.CHANNEL_ID);

        // Start the bot
        DiscordBot.startBot();
        // send starting message
        sendToDiscord("Server is starting up.");
    }
}