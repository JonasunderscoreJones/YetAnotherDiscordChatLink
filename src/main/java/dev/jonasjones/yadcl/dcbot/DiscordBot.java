package dev.jonasjones.yadcl.dcbot;

import dev.jonasjones.yadcl.config.ModConfigs;
import lombok.Getter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

import static dev.jonasjones.yadcl.YetAnotherDiscordChatLink.LOGGER;

public class DiscordBot extends ListenerAdapter {
    private static String token;
    @Getter
    private static String targetChannelId;
    private static JDA jda;
    @Getter
    private static MinecraftServer minecraftServer;
    @Getter
    private static Boolean isBotRunning = false;

    public DiscordBot(String token, String targetChannelId) {
        this.token = token;
        this.targetChannelId = targetChannelId;
        // Register Minecraft Server events
        registerEvents();
    }

    public static void startBot() {
        if (isBotRunning) {
            return;
        }

        try {

            // Create the bot
            jda = JDABuilder.createDefault(token)
                    .addEventListeners(new MessageListener())
                    .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                    .build();
            // Set the bot status
            if (ModConfigs.BOT_STATUS.equals("Uptime")) {
                new Thread(() -> {
                    while (isBotRunning) {
                        setBotStatus(ModConfigs.BOT_STATUS);
                        try {
                            Thread.sleep(60000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            } else {
                setBotStatus(ModConfigs.BOT_STATUS);
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
        jda.shutdown();
        isBotRunning = false;
    }

    public static void sendToDiscord(String message) {
        if (!isBotRunning) {
            return;
        }
        // Get the text channel by its ID
        try {
            TextChannel channel = jda.getTextChannelById(targetChannelId);
            // Check if the channel exists and send the message
            if (channel != null) {
                channel.sendMessage(message).queue();
            } else {
                //throw new Exception("Discord Channel does not exist!");
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

    public static void setBotStatus(String statusType) {
        if (!isBotRunning) {
            return;
        }
        switch (statusType.toLowerCase()) {
            case "player count" -> jda.getPresence().setActivity(Activity.playing("Players: 100"));
            case "ip" -> jda.getPresence().setActivity(Activity.listening("Server IP: example.com"));
            case "uptime" -> jda.getPresence().setActivity(Activity.watching("Uptime: " + calculateUptime()));
            default -> System.out.println("Invalid status type!");
        }
    }

    private static String calculateUptime() {
        try {
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
        } catch (Exception e) {
            return "-";
        }
    }
}
