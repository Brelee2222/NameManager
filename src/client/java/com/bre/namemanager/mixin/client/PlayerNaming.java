package com.bre.namemanager.mixin.client;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerNaming extends EntityNaming {
    @Inject(at = @At("HEAD"), method = "getName", cancellable = true)
    public void getName(CallbackInfoReturnable<Text> cir) {
        if(this.nickName != null)
            cir.setReturnValue(this.nickName);
    }
}
