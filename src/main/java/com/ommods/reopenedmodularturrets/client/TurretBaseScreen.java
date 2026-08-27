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
            ResourceLocation.fromNamespaceAndPath(ModConstants.MOD_ID, "textures/gui/turret_base.png");

    public TurretBaseScreen(TurretBaseMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth = 176;
        this.imageHeight = 184;
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
        int energyWidth = menu.getMaxEnergy() > 0
                ? menu.getEnergyStored() * 52 / menu.getMaxEnergy()
                : 0;
        graphics.fill(x + 8, y + 52, x + 8 + energyWidth, y + 60, 0xFF55AAFF);
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        graphics.drawString(font, title, titleLabelX, titleLabelY, 0x404040, false);
        graphics.drawString(font, playerInventoryTitle, inventoryLabelX, inventoryLabelY + 18, 0x404040, false);
        graphics.drawString(font, "FE: " + menu.getEnergyStored() + "/" + menu.getMaxEnergy(), 8, 6, 0x404040, false);
        graphics.drawString(font, Component.translatable("gui.reopenedmodularturrets.ammo"), 8, 16, 0x404040, false);
        graphics.drawString(font,
                "B:" + menu.getData(5) + " G:" + menu.getData(6) + " C:" + menu.getData(7),
                8, 26, 0x606060, false);
        graphics.drawString(font,
                "S:" + menu.getData(8) + " R:" + menu.getData(9),
                8, 36, 0x606060, false);
        String addons = buildAddonStatus();
        if (!addons.isEmpty()) {
            graphics.drawString(font, addons, 62, 6, 0x006600, false);
        }
    }

    private String buildAddonStatus() {
        StringBuilder builder = new StringBuilder();
        if (menu.getData(10) == 1) {
            builder.append("Solar ");
        }
        if (menu.getData(11) == 1) {
            builder.append("Reactor ");
        }
        if (menu.getData(12) == 1) {
            builder.append("LootDel ");
        }
        if (menu.getData(13) == 1) {
            builder.append("DmgAmp");
        }
        return builder.toString().trim();
    }
}
