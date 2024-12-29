package com.bre.namemanager.mixin.client;

import com.bre.namemanager.namemanage.NameManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

@Mixin(Entity.class)
public abstract class EntityNaming {

    @Shadow public abstract UUID getUuid();

    @Shadow @Nullable public abstract MinecraftServer getServer();

    @Shadow public abstract World getWorld();

    @Shadow public abstract int getId();

    @Shadow public abstract void setUuid(UUID uuid);

    @Unique
    public Text nickName;


    @Unique
    public void setName(String name) {
        this.nickName = (name == null ? null : Text.of(name));
    }

    @Inject(at = @At("TAIL"), method = "setUuid")
    public void setUuid(UUID uuid, CallbackInfo ci) {
        this.setName(NameManager.getName(uuid));
    }

    @Inject(at = @At("HEAD"), method="isCustomNameVisible", cancellable = true)
    public void isCustomNameVisible(CallbackInfoReturnable<Boolean> cir) {
        if(this.nickName != null)
            cir.setReturnValue(true);
    }

    @Inject(at = @At("HEAD"), method = "getName", cancellable = true)
    public void getName(CallbackInfoReturnable<Text> cir) {
        if(this.nickName != null)
            cir.setReturnValue(this.nickName);
    }

    @Inject(at = @At("HEAD"), method = "interactAt", cancellable = true)
    public void interact(PlayerEntity player, Vec3d hitPos, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        String name = NameManager.nextName();

        if(name == null)
            return;

        NameManager.addName(name, this.getUuid());
        this.setName(name);
        cir.setReturnValue(ActionResult.SUCCESS);
    }
}
