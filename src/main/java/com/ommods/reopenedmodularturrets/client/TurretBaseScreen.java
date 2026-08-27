package com.ommods.reopenedmodularturrets.client;

import com.ommods.reopenedmodularturrets.ModConstants;
import com.ommods.reopenedmodularturrets.core.targeting.TargetFilter;
import com.ommods.reopenedmodularturrets.menu.TurretBaseMenu;
import com.ommods.reopenedmodularturrets.network.ModNetworking;
import com.ommods.reopenedmodularturrets.network.payload.ToggleTargetFilterPayload;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class TurretBaseScreen extends AbstractContainerScreen<TurretBaseMenu> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.withDefaultNamespace("textures/gui/container/generic_54.png");

    public TurretBaseScreen(TurretBaseMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth = 176;
        this.imageHeight = 166;
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
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        graphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight, 256, 256);
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        graphics.drawString(font, title, titleLabelX, titleLabelY, 0x404040, false);
        graphics.drawString(font, playerInventoryTitle, inventoryLabelX, inventoryLabelY, 0x404040, false);
        graphics.drawString(font, "FE: " + menu.getEnergyStored() + "/" + menu.getMaxEnergy(), 8, 6, 0x404040, false);
    }
}
