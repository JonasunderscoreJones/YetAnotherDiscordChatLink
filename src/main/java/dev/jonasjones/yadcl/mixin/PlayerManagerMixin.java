package dev.jonasjones.yadcl.mixin;

import dev.jonasjones.yadcl.config.ModConfigs;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ConnectedClientData;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//import static dev.jonasjones.yadcl.dcbot.DiscordBot.botStatus;
import static dev.jonasjones.yadcl.dcbot.DiscordBot.sendToDiscord;

@Mixin(PlayerManager.class)
public class PlayerManagerMixin {
    @Inject(at = @At("HEAD"), method = "onPlayerConnect")
    public void onPlayerConnect​(ClientConnection connection, ServerPlayerEntity player, ConnectedClientData clientData, CallbackInfo ci) {
        if (ModConfigs.PLAYER_JOIN_LEAVE_MSG) {
            sendToDiscord(player.getDisplayName().getString() + " joined the game");
            /*if (ModConfigs.BOT_STATUS.equals("PlayerCount")) {
                botStatus(ModConfigs.BOT_STATUS);
            }*/
        }
    }

    @Inject(at = @At("HEAD"), method = "remove")
    public void remove(ServerPlayerEntity player, CallbackInfo ci) {
        if (ModConfigs.PLAYER_JOIN_LEAVE_MSG) {
            sendToDiscord(player.getDisplayName().getString() + " left the game");
            /*if (ModConfigs.BOT_STATUS.equals("PlayerCount")) {
                botStatus(ModConfigs.BOT_STATUS);
            }*/
        }
    }
}