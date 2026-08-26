package com.ommods.reopenedmodularturrets.client;

import com.ommods.reopenedmodularturrets.core.targeting.TargetFilter;
import com.ommods.reopenedmodularturrets.menu.TurretBaseMenu;
import com.ommods.reopenedmodularturrets.network.ModNetworking;
import com.ommods.reopenedmodularturrets.network.payload.ToggleTargetFilterPayload;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

public class TurretBaseScreen extends AbstractContainerScreen<TurretBaseMenu> {
    private static final Identifier TEXTURE = Identifier.withDefaultNamespace("textures/gui/container/generic_54.png");

    public TurretBaseScreen(TurretBaseMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, 176, 166);
    }

    @Override
    protected void init() {
        super.init();
        int x = leftPos + 116;
        int y = topPos + 18;
        addRenderableWidget(Button.builder(Component.literal("Mobs"), button -> sendToggle(TargetFilter.MOBS))
                .bounds(x, y, 50, 20).build());
        addRenderableWidget(Button.builder(Component.literal("Players"), button -> sendToggle(TargetFilter.PLAYERS))
                .bounds(x, y + 24, 50, 20).build());
        addRenderableWidget(Button.builder(Component.literal("Neutral"), button -> sendToggle(TargetFilter.NEUTRAL))
                .bounds(x, y + 48, 50, 20).build());
    }

    private void sendToggle(TargetFilter filter) {
        if (menu.getBase() != null) {
            ModNetworking.sendToServer(new ToggleTargetFilterPayload(menu.getBase().getBlockPos(), filter));
        }
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractBackground(graphics, mouseX, mouseY, partialTick);
        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, leftPos, topPos, 0.0F, 0.0F, imageWidth, imageHeight, 256, 256);
    }

    @Override
    protected void extractLabels(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        super.extractLabels(graphics, mouseX, mouseY);
        graphics.text(font, "FE: " + menu.getEnergyStored() + "/" + menu.getMaxEnergy(), leftPos + 8, topPos + 6, 0x404040, false);
    }
}
