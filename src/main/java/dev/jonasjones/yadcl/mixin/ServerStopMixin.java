package dev.jonasjones.yadcl.mixin;

import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static dev.jonasjones.yadcl.dcbot.DiscordBot.sendToDiscord;
import static dev.jonasjones.yadcl.dcbot.DiscordBot.stopBot;

@Mixin(MinecraftServer.class)
public class ServerStopMixin {
    @Inject(at = @At("HEAD"), method = "shutdown")
    private void init(CallbackInfo info) {
        sendToDiscord("Server is shutting down.");
        //wait 2 seconds for the message to send
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stopBot();
    }
}
