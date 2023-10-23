package dev.jonasjones.yadcl.dcbot;

import dev.jonasjones.yadcl.config.ModConfigs;
import lombok.Setter;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.Text;

import java.util.concurrent.TimeUnit;

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
            registerEvents();
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
            // Set the bot status
            if (ModConfigs.BOT_STATUS.equals("Uptime")) {
                new Thread(() -> {
                    while (isBotRunning) {
                        botStatus(ModConfigs.BOT_STATUS);
                        try {
                            Thread.sleep(60000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            } else {
                botStatus(ModConfigs.BOT_STATUS);
            }
        } catch (Exception e) {
            LOGGER.error("Failed to start Discord bot. Check the provided discord token in the config file.");
            return;
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
        try {
            TextChannel channel = api.getTextChannelById(targetChannelId).orElse(null);
            // Check if the channel exists and send the message
            if (channel != null) {
                channel.sendMessage(message);
            } else {
                throw new Exception("Discord Channel does not exist!");
            }
        } catch (Exception e) {
            LOGGER.error("Discord Channel does not exist!");
        }
    }

    private static void registerEvents() {
        ServerTickEvents.START_SERVER_TICK.register(server -> {
            // This code is executed on every server tick
            minecraftServer = server;
        });

        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            sendToDiscord("Server is started!");
        });

        ServerLifecycleEvents.SERVER_STOPPED.register(server -> {
            sendToDiscord("Server is stopped!");
            stopBot();
        });
    }

    public static void botStatus(String status) {
        if (!isBotRunning) {
            return;
        }
        switch (status) {
            case "None" -> api.updateActivity("");
            case "PlayerCount" ->
                    api.updateActivity("Player Count: " + minecraftServer.getCurrentPlayerCount() + "/" + minecraftServer.getMaxPlayerCount());
            case "IP" -> api.updateActivity("IP: " + minecraftServer.getServerIp());
            case "Uptime" -> api.updateActivity("Uptime: " + calculateUptime());
            default -> api.updateActivity(null);
        }
    }

    private static String calculateUptime() {
        long secs = System.currentTimeMillis() - minecraftServer.getTicks() / 20;
        long days = TimeUnit.SECONDS.toDays(secs);
        secs -= TimeUnit.DAYS.toMillis(days);
        long hours = TimeUnit.SECONDS.toHours(secs);
        secs -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.SECONDS.toMinutes(secs);

        StringBuilder duration = new StringBuilder();
        if (days > 0) {
            duration.append(days).append(" Days, ");
        }
        if (hours > 0 || days > 0) {
            if (hours == 1) {
                duration.append(hours).append(" Hour, ");
            } else {
                duration.append(hours).append(" Hours, ");
            }
        }
        if (minutes > 0 || hours > 0 || days > 0) {
            if (minutes == 1) {
                duration.append(minutes).append(" Minute, ");
            } else {
                duration.append(minutes).append(" Minutes, ");
            }
        }

        return duration.toString();
    }
}
