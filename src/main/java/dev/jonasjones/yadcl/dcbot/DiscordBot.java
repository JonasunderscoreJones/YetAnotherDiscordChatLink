package dev.jonasjones.yadcl.dcbot;

import lombok.Setter;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.Text;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.channel.TextChannel;

import static dev.jonasjones.yadcl.YetAnotherDiscordChatLink.LOGGER;

public class DiscordBot {
    @Setter
    private static String token;
    @Setter
    private static String targetChannelId;
    private static DiscordApi api;
    private static MinecraftServer minecraftServer;
    private static Boolean isBotRunning = false;

    public static void startBot() {
        if (isBotRunning) {
            return;
        }

        try {
            registerServerTickEvent();
            api = new DiscordApiBuilder().setToken(token).setAllIntents().login().join();
            api.addMessageCreateListener(event -> {
                // Check if the message is from the specific channel by comparing channel IDs
                if (String.valueOf(event.getChannel().getId()).equals(targetChannelId)) {
                    //check if message author is the bot
                    if (event.getMessageAuthor().isBotUser()) {
                        return;
                    }
                    String discordMessage = "[" + event.getMessageAuthor().getDisplayName() + "] " + event.getMessageContent();
                    // Broadcast the message to Minecraft chat
                    // You can implement a method to broadcast messages to Minecraft players
                    minecraftServer.getPlayerManager().broadcast(Text.of(discordMessage), false);
                }
            });
        } catch (Exception e) {
            LOGGER.error("Failed to start Discord bot. Check the provided discord token in the config file.");
        }
        isBotRunning = true;
    }

    public static void stopBot() {
        if (!isBotRunning) {
            return;
        }
        api.disconnect();
        isBotRunning = false;
    }

    public static void sendToDiscord(String message) {
        if (!isBotRunning) {
            return;
        }
        // Get the text channel by its ID
        TextChannel channel = api.getTextChannelById(targetChannelId).orElse(null);

        // Check if the channel exists and send the message
        if (channel != null) {
            channel.sendMessage(message);
        } else {
            // Handle the case where the channel does not exist
            LOGGER.error("Discord Channel does not exist!");
        }
    }

    private static void registerServerTickEvent() {
        ServerTickEvents.START_SERVER_TICK.register(server -> {
            // This code is executed on every server tick
            minecraftServer = server;
        });
    }
}
