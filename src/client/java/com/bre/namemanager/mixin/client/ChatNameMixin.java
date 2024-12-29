package com.bre.namemanager.mixin.client;

import com.bre.namemanager.namemanage.NameManager;
import net.minecraft.client.gui.screen.ChatScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatScreen.class)
public class ChatNameMixin {
    @Inject(at = @At("HEAD"), method = "sendMessage", cancellable = true)
    public void sendMessage(String chatText, boolean addToHistory, CallbackInfo ci) {
        if(chatText.startsWith("name entity ")) {

            NameManager.queueName(chatText.replaceFirst("name entity ", ""));
            ci.cancel();
        }
    }
}
