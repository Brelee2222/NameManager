package com.bre.namemanager.screens;

import com.bre.namemanager.namemanage.NameManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.*;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;

import java.util.Enumeration;
import java.util.UUID;

public class EntityNamesScreen extends Screen {
    public final ThreePartsLayoutWidget layout = new ThreePartsLayoutWidget(this);

    public Widget body;

    public EntityNamesScreen() {
        super(Text.of("Entity Names"));
    }

    protected void init() {
        this.layout.addHeader(this.title, this.textRenderer);
        GridWidget gridWidget = new GridWidget();
        GridWidget.Adder adder = gridWidget.createAdder(1);

        DirectionalLayoutWidget namesListWidget = new DirectionalLayoutWidget(200, 50, DirectionalLayoutWidget.DisplayAxis.VERTICAL);

        Enumeration<UUID> uuids = NameManager.getUUIDs();
        while(uuids.hasMoreElements()) {
            UUID uuid = uuids.nextElement();

            namesListWidget.add(ButtonWidget.builder(Text.of(NameManager.getName(uuid) + " - " + Long.toHexString(uuid.getMostSignificantBits())), (button) -> {
                button.visible = false;
                NameManager.removeName(uuid);
                MinecraftClient.getInstance().world.getEntities().forEach(entity -> {
                    if(entity.getUuid().equals(uuid))
                        entity.setUuid(uuid);
                });
            }).build());
        }

        adder.add(namesListWidget);

        TextFieldWidget textField = new TextFieldWidget(this.textRenderer, 308, 20, Text.of("entity name"));
        adder.add(textField);
        adder.add(ButtonWidget.builder(Text.of("Add name"), (button) -> {
            NameManager.queueName(textField.getText());
            this.close();
        }).width(200).build());

        this.body = this.layout.addBody(gridWidget);

        this.layout.addFooter(ButtonWidget.builder(ScreenTexts.DONE, (button) -> {
            this.close();
        }).width(200).build());
        this.layout.forEachChild((child) -> {
            ClickableWidget var10000 = (ClickableWidget)this.addDrawableChild(child);
        });
        this.layout.refreshPositions();
    }
}
