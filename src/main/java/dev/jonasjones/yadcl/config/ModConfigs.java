package dev.jonasjones.yadcl.config;


import com.mojang.datafixers.util.Pair;
import dev.jonasjones.yadcl.YetAnotherDiscordChatLink;

public class ModConfigs {
    public static SimpleConfig CONFIG;
    private static ModConfigProvider configs;

    public static String TOKEN;
    public static String CHANNEL_ID;

    public static void registerConfigs() {
        configs = new ModConfigProvider();
        createConfigs();

        CONFIG = SimpleConfig.of(YetAnotherDiscordChatLink.MOD_ID + "config").provider(configs).request();

        assignConfigs();
    }

    private static void createConfigs() {
        configs.addKeyValuePair(new Pair<>("bot.token", "[Insert bot token here]"), "The Bot token from the discord developer dashboard");
        configs.addKeyValuePair(new Pair<>("bot.channel", "1165690308185563216"), "The channel id of the channel the bot should listen and send to");
    }

    private static void assignConfigs() {
        TOKEN = CONFIG.getOrDefault("bot.token", "default value");
        CHANNEL_ID = CONFIG.getOrDefault("bot.channel", "1165690308185563216");


        YetAnotherDiscordChatLink.LOGGER.info("All " + configs.getConfigsList().size() + " Configs have been set properly");
    }
}
