package dev.jonasjones.yadcl.config;


import com.mojang.datafixers.util.Pair;
import dev.jonasjones.yadcl.YetAnotherDiscordChatLink;

public class ModConfigs {
    public static SimpleConfig CONFIG;
    private static ModConfigProvider configs;

    public static String TOKEN;
    public static String CHANNEL_ID;
    public static Boolean PLAYER_JOIN_LEAVE_MSG;
    //public static Boolean PLAYER_DEATH_MSG;
    //public static Boolean PLAYER_ADVANCEMENT_MSG;
    public static Boolean PLAYER_CHAT_MSG;
    //public static Boolean PLAYER_COMMAND_MSG;
    public static Boolean SERVER_START_STOP_MSG;

    //public static String BOT_STATUS;

    public static void registerConfigs() {
        configs = new ModConfigProvider();
        createConfigs();

        CONFIG = SimpleConfig.of(YetAnotherDiscordChatLink.MOD_ID + "config").provider(configs).request();

        assignConfigs();
    }

    private static void createConfigs() {
        configs.addKeyValuePair(new Pair<>("bot.token", "[Insert bot token here]"), "The Bot token from the discord developer dashboard");
        configs.addKeyValuePair(new Pair<>("bot.channel", "1165690308185563216"), "The channel id of the channel the bot should listen and send to");
        configs.addKeyValuePair(new Pair<>("bot.player_join_leave_msgs", true), "Should the bot send a message when a player joins or leaves the server");
        //configs.addKeyValuePair(new Pair<>("bot.player_death_msgs", true), "Should the bot send a message when a player dies");
        //configs.addKeyValuePair(new Pair<>("bot.player_advancement_msgs", true), "Should the bot send a message when a player advances an advancement");
        configs.addKeyValuePair(new Pair<>("bot.player_chat_msgs", true), "Should the bot send a message when a player sends a chat message");
        //configs.addKeyValuePair(new Pair<>("bot.player_command_msgs", true), "Should the bot send a message when a player sends a command");
        configs.addKeyValuePair(new Pair<>("bot.server_start_stop_msgs", true), "Should the bot send a message when the server starts or stops");
        //configs.addKeyValuePair(new Pair<>("bot.status", "PlayerCount"), "What status should the bot display [None, PlayerCount, IP, Uptime]");
    }

    private static void assignConfigs() {
        TOKEN = CONFIG.getOrDefault("bot.token", "default value");
        CHANNEL_ID = CONFIG.getOrDefault("bot.channel", "1165690308185563216");
        PLAYER_JOIN_LEAVE_MSG = CONFIG.getOrDefault("bot.player_join_leave_msgs", true);
        //PLAYER_DEATH_MSG = CONFIG.getOrDefault("bot.player_death_msgs", true);
        //PLAYER_ADVANCEMENT_MSG = CONFIG.getOrDefault("bot.player_advancement_msgs", true);
        PLAYER_CHAT_MSG = CONFIG.getOrDefault("bot.player_chat_msgs", true);
        //PLAYER_COMMAND_MSG = CONFIG.getOrDefault("bot.player_command_msgs", true);
        SERVER_START_STOP_MSG = CONFIG.getOrDefault("bot.server_start_stop_msgs", true);
        //BOT_STATUS = CONFIG.getOrDefault("bot.status", "PlayerCount");

        YetAnotherDiscordChatLink.LOGGER.info("All " + configs.getConfigsList().size() + " Configs have been set properly");
    }
}
