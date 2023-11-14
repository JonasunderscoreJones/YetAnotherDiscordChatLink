package dev.jonasjones.yadcl.dcbot;

import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.minecraft.text.Text;

import static dev.jonasjones.yadcl.YetAnotherDiscordChatLink.discordBot;

public class MessageListener extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (!DiscordBot.getIsBotRunning()) return; // Ignore messages if the bot is not running
        if (event.getAuthor().isBot()) return; // Ignore messages from other bots

        // Check if the message was sent in the trigger channel
        if (event.getChannel().getId().equals(DiscordBot.getTargetChannelId())) {
            // Send the message to the server
            DiscordBot.getMinecraftServer().getPlayerManager().broadcast(Text.of("[" +event.getAuthor().getName() + "]: " + event.getMessage().getContentRaw()), false);
        }
    }
}