package com.bre.namemanager.mixin.client.namemanage;

import com.bre.namemanager.screens.EntityNamesScreen;
import net.minecraft.client.gui.screen.*;
import net.minecraft.client.gui.screen.option.*;
import net.minecraft.client.gui.screen.pack.PackScreen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.*;
import net.minecraft.client.option.GameOptions;
import net.minecraft.resource.ResourcePackManager;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Supplier;

import static net.minecraft.client.gui.screen.option.AccessibilityOptionsScreen.TITLE_TEXT;


@Mixin(OptionsScreen.class)
public abstract class MenuMixin extends Screen {
    @Shadow @Final private ThreePartsLayoutWidget layout;

    protected MenuMixin(Text title) {
        super(title);
    }

    @Shadow protected abstract ButtonWidget createButton(Text message, Supplier<Screen> screenSupplier);

    @Shadow @Final private GameOptions settings;

    @Shadow protected abstract Widget createTopRightButton();

    @Shadow @Final private static Text SKIN_CUSTOMIZATION_TEXT;

    @Shadow @Final private static Text SOUNDS_TEXT;

    @Shadow @Final private static Text VIDEO_TEXT;

    @Shadow @Final private static Text CONTROL_TEXT;

    @Shadow @Final private static Text LANGUAGE_TEXT;

    @Shadow @Final private static Text CHAT_TEXT;

    @Shadow @Final private static Text RESOURCE_PACK_TEXT;

    @Shadow protected abstract void refreshResourcePacks(ResourcePackManager resourcePackManager);

    @Shadow @Final private static Text ACCESSIBILITY_TEXT;

    @Shadow @Final private static Text TELEMETRY_TEXT;

    @Shadow @Final private static Tooltip TELEMETRY_DISABLED_TOOLTIP;

    @Shadow @Final private static Text CREDITS_AND_ATTRIBUTION_TEXT;

    @Inject(at = @At(value = "HEAD"), method = "init", cancellable = true)
    public void init(CallbackInfo ci) {
        ci.cancel();
        DirectionalLayoutWidget directionalLayoutWidget = (DirectionalLayoutWidget)this.layout.addHeader(DirectionalLayoutWidget.vertical().spacing(8));
        directionalLayoutWidget.add(new TextWidget(TITLE_TEXT, this.textRenderer), Positioner::alignHorizontalCenter);
        DirectionalLayoutWidget directionalLayoutWidget2 = ((DirectionalLayoutWidget)directionalLayoutWidget.add(DirectionalLayoutWidget.horizontal())).spacing(8);
        directionalLayoutWidget2.add(this.settings.getFov().createWidget(this.client.options));
        directionalLayoutWidget2.add(this.createTopRightButton());
        GridWidget gridWidget = new GridWidget();
        gridWidget.getMainPositioner().marginX(4).marginBottom(4).alignHorizontalCenter();
        GridWidget.Adder adder = gridWidget.createAdder(2);
        adder.add(this.createButton(SKIN_CUSTOMIZATION_TEXT, () -> {
            return new SkinOptionsScreen(this, this.settings);
        }));
        adder.add(this.createButton(SOUNDS_TEXT, () -> {
            return new SoundOptionsScreen(this, this.settings);
        }));
        adder.add(this.createButton(VIDEO_TEXT, () -> {
            return new VideoOptionsScreen(this, this.client, this.settings);
        }));
        adder.add(this.createButton(CONTROL_TEXT, () -> {
            return new ControlsOptionsScreen(this, this.settings);
        }));
        adder.add(this.createButton(LANGUAGE_TEXT, () -> {
            return new LanguageOptionsScreen(this, this.settings, this.client.getLanguageManager());
        }));
        adder.add(this.createButton(CHAT_TEXT, () -> {
            return new ChatOptionsScreen(this, this.settings);
        }));
        adder.add(this.createButton(RESOURCE_PACK_TEXT, () -> {
            return new PackScreen(this.client.getResourcePackManager(), this::refreshResourcePacks, this.client.getResourcePackDir(), Text.translatable("resourcePack.title"));
        }));
        adder.add(this.createButton(ACCESSIBILITY_TEXT, () -> {
            return new AccessibilityOptionsScreen(this, this.settings);
        }));
        ButtonWidget buttonWidget = (ButtonWidget)adder.add(this.createButton(TELEMETRY_TEXT, () -> {
            return new TelemetryInfoScreen(this, this.settings);
        }));
        if (!this.client.isTelemetryEnabledByApi()) {
            buttonWidget.active = false;
            buttonWidget.setTooltip(TELEMETRY_DISABLED_TOOLTIP);
        }

        adder.add(this.createButton(CREDITS_AND_ATTRIBUTION_TEXT, () -> {
            return new CreditsAndAttributionScreen(this);
        }));

        adder.add(this.createButton(Text.of("Entity Names"), () -> {
            return new EntityNamesScreen();
        }));

        this.layout.addBody(gridWidget);
        this.layout.addFooter(ButtonWidget.builder(ScreenTexts.DONE, (button) -> {
            this.close();
        }).width(200).build());
        this.layout.forEachChild((child) -> {
            ClickableWidget var10000 = (ClickableWidget)this.addDrawableChild(child);
        });
        this.refreshWidgetPositions();
    }
}
