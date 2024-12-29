package com.bre.namemanager.mixin.client;

import com.bre.namemanager.namemanage.NameManager;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ArmorStandEntity.class)
public abstract class ArmorStandNaming extends EntityNaming {
    @Inject(at = @At("HEAD"), method = "interactAt", cancellable = true)
    public void interactAt(PlayerEntity player, Vec3d hitPos, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        String name = NameManager.nextName();

        if(name == null)
            return;

        NameManager.addName(name, this.getUuid());
        this.setUuid(this.getUuid());
    }
}
