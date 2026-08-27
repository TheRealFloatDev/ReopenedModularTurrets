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
    private static final int TEX_SIZE = 256;
    private static final int ENERGY_U = 178;
    private static final int ENERGY_V = 18;
    private static final int ENERGY_W = 16;
    private static final int ENERGY_H = 34;

    public TurretBaseScreen(TurretBaseMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth = 176;
        this.imageHeight = 166;
        this.inventoryLabelY = this.imageHeight - 94;
    }

    private ResourceLocation texture() {
        int tier = menu.getBase() != null ? menu.getBase().getTier() : 1;
        String suffix = switch (tier) {
            case 2 -> "two";
            case 3 -> "three";
            case 4 -> "four";
            case 5 -> "five";
            default -> "one";
        };
        return ResourceLocation.fromNamespaceAndPath(ModConstants.MOD_ID, "textures/gui/turret_base_tier_" + suffix + ".png");
    }

    @Override
    protected void init() {
        super.init();
        if (menu.getBase() == null) {
            return;
        }
        int x = leftPos + 116;
        int y = topPos + 18;
        addRenderableWidget(toggleButton(x, y, TargetFilter.MOBS, menu.getData(2) == 1));
        addRenderableWidget(toggleButton(x, y + 22, TargetFilter.PLAYERS, menu.getData(3) == 1));
        addRenderableWidget(toggleButton(x, y + 44, TargetFilter.NEUTRAL, menu.getData(4) == 1));
    }

    private Button toggleButton(int x, int y, TargetFilter filter, boolean active) {
        Component label = Component.translatable("gui.reopenedmodularturrets.filter." + filter.name().toLowerCase());
        return Button.builder(label, button -> sendToggle(filter))
                .bounds(x, y, 56, 20)
                .build();
    }

    private void sendToggle(TargetFilter filter) {
        if (menu.getBase() != null) {
            ModNetworking.sendToServer(new ToggleTargetFilterPayload(menu.getBase().getBlockPos(), filter));
        }
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        int x = leftPos;
        int y = topPos;
        ResourceLocation texture = texture();
        graphics.blit(texture, x, y, 0, 0, imageWidth, imageHeight, TEX_SIZE, TEX_SIZE);
        renderEnergyBar(graphics, x, y, texture);
    }

    private void renderEnergyBar(GuiGraphics graphics, int x, int y, ResourceLocation texture) {
        int max = menu.getMaxEnergy();
        if (max <= 0) {
            return;
        }
        int fill = menu.getEnergyStored() * ENERGY_H / max;
        if (fill <= 0) {
            return;
        }
        graphics.blit(texture,
                x + 152, y + 18 + (ENERGY_H - fill),
                ENERGY_U, ENERGY_V + (ENERGY_H - fill),
                ENERGY_W, fill,
                TEX_SIZE, TEX_SIZE);
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        graphics.drawString(font, title, titleLabelX, titleLabelY, 0x404040, false);
        graphics.drawString(font, playerInventoryTitle, inventoryLabelX, inventoryLabelY, 0x404040, false);
        graphics.drawString(font, Component.translatable("gui.reopenedmodularturrets.energy",
                menu.getEnergyStored(), menu.getMaxEnergy()), 8, 6, 0x303030, false);
        graphics.drawString(font, Component.translatable("gui.reopenedmodularturrets.ammo"), 8, 16, 0x303030, false);
        graphics.drawString(font, "B:" + menu.getData(5) + " G:" + menu.getData(6) + " C:" + menu.getData(7), 8, 26, 0x505050, false);
        graphics.drawString(font, "S:" + menu.getData(8) + " R:" + menu.getData(9), 8, 36, 0x505050, false);
    }
}
