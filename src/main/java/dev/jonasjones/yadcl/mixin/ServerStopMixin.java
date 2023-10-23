package dev.jonasjones.yadcl.mixin;

import dev.jonasjones.yadcl.config.ModConfigs;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static dev.jonasjones.yadcl.dcbot.DiscordBot.sendToDiscord;

@Mixin(MinecraftServer.class)
public class ServerStopMixin {
    @Inject(at = @At("HEAD"), method = "shutdown")
    private void init(CallbackInfo info) {
        if (ModConfigs.SERVER_START_STOP_MSG) {
            sendToDiscord("Server is shutting down.");
        }
    }
}
