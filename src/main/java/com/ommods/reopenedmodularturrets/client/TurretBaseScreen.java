package com.ommods.reopenedmodularturrets.client;

import com.ommods.reopenedmodularturrets.ModConstants;
import com.ommods.reopenedmodularturrets.core.targeting.TargetFilter;
import com.ommods.reopenedmodularturrets.menu.TurretBaseMenu;
import com.ommods.reopenedmodularturrets.network.ModNetworking;
import com.ommods.reopenedmodularturrets.network.payload.ToggleTargetFilterPayload;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class TurretBaseScreen extends AbstractContainerScreen<TurretBaseMenu> {
    private static final int TEX_SIZE = 256;

    private static final ResourceLocation ICON_MOBS =
            ResourceLocation.fromNamespaceAndPath(ModConstants.MOD_ID, "textures/gui/filter/mobs.png");
    private static final ResourceLocation ICON_PLAYERS =
            ResourceLocation.fromNamespaceAndPath(ModConstants.MOD_ID, "textures/gui/filter/players.png");
    private static final ResourceLocation ICON_NEUTRAL =
            ResourceLocation.fromNamespaceAndPath(ModConstants.MOD_ID, "textures/gui/filter/neutral.png");

    private static final int FILTER_X = 98;
    private static final int FILTER_Y = 18;
    private static final int FILTER_SIZE = 16;
    private static final int FILTER_SPACING = 18;

    private static final int ENERGY_X = 153;
    private static final int ENERGY_Y = 17;
    private static final int ENERGY_W = 14;
    private static final int ENERGY_H = 51;
    private static final int[] ENERGY_FILL_U = {196, 215, 234};

    public TurretBaseScreen(TurretBaseMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth = 176;
        this.imageHeight = 166;
        this.titleLabelY = -1000;
        this.inventoryLabelY = 73;
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
        int x = leftPos + FILTER_X;
        int y = topPos + FILTER_Y;
        addRenderableWidget(new TargetFilterIconButton(x, y, TargetFilter.MOBS, ICON_MOBS));
        addRenderableWidget(new TargetFilterIconButton(x, y + FILTER_SPACING, TargetFilter.PLAYERS, ICON_PLAYERS));
        addRenderableWidget(new TargetFilterIconButton(x, y + FILTER_SPACING * 2, TargetFilter.NEUTRAL, ICON_NEUTRAL));
        addRenderableWidget(Button.builder(
                Component.translatable("gui.reopenedmodularturrets.trusted_players"),
                button -> minecraft.setScreen(new TrustedPlayersScreen(menu.getBase().getBlockPos(), this))
        ).bounds(leftPos + 8, topPos + 56, 88, 16).build());
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
        graphics.blit(texture, x + ENERGY_X, y + ENERGY_Y, 178, 17, ENERGY_W, ENERGY_H, TEX_SIZE, TEX_SIZE);

        int max = Math.max(1, menu.getMaxEnergy());
        int fill = menu.getEnergyStored() * ENERGY_H / max;
        if (fill <= 0) {
            return;
        }
        int frame = minecraft != null && minecraft.level != null
                ? (int) (minecraft.level.getGameTime() / 8L % ENERGY_FILL_U.length)
                : 0;
        graphics.blit(texture,
                x + ENERGY_X,
                y + ENERGY_Y + ENERGY_H - fill,
                ENERGY_FILL_U[frame],
                68 - fill,
                ENERGY_W,
                fill,
                TEX_SIZE,
                TEX_SIZE);
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        int tier = menu.getBase() != null ? menu.getBase().getTier() : 1;
        graphics.drawString(font, Component.translatable("gui.reopenedmodularturrets.ammo"), 8, 6, 0x404040, false);
        if (tier >= 2) {
            graphics.drawString(font, Component.translatable("gui.reopenedmodularturrets.addons"), 71, 6, 0x404040, false);
            graphics.drawString(font, Component.translatable("gui.reopenedmodularturrets.upgrades"), 71, 39, 0x404040, false);
        }
        graphics.drawString(font, playerInventoryTitle, inventoryLabelX, inventoryLabelY, 0x404040, false);
    }

    @Override
    protected void renderTooltip(GuiGraphics graphics, int mouseX, int mouseY) {
        super.renderTooltip(graphics, mouseX, mouseY);
        if (isHoveringEnergyBar(mouseX, mouseY)) {
            graphics.renderTooltip(font, Component.translatable(
                    "gui.reopenedmodularturrets.energy",
                    menu.getEnergyStored(),
                    menu.getMaxEnergy()
            ), mouseX, mouseY);
        }
    }

    private boolean isHoveringEnergyBar(int mouseX, int mouseY) {
        return mouseX >= leftPos + ENERGY_X
                && mouseX < leftPos + ENERGY_X + ENERGY_W
                && mouseY >= topPos + ENERGY_Y
                && mouseY < topPos + ENERGY_Y + ENERGY_H;
    }

    private final class TargetFilterIconButton extends AbstractWidget {
        private final TargetFilter filter;
        private final ResourceLocation icon;

        TargetFilterIconButton(int x, int y, TargetFilter filter, ResourceLocation icon) {
            super(x, y, FILTER_SIZE, FILTER_SIZE, Component.empty());
            this.filter = filter;
            this.icon = icon;
            this.setTooltip(Tooltip.create(Component.translatable(
                    "gui.reopenedmodularturrets.filter." + filter.name().toLowerCase())));
        }

        private boolean isFilterEnabled() {
            return switch (filter) {
                case MOBS -> menu.getData(2) == 1;
                case PLAYERS -> menu.getData(3) == 1;
                case NEUTRAL -> menu.getData(4) == 1;
            };
        }

        @Override
        protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
            if (!isFilterEnabled()) {
                graphics.fill(getX(), getY(), getX() + width, getY() + height, 0xAA000000);
            }
            if (isFilterEnabled()) {
                graphics.fill(getX() - 1, getY() - 1, getX() + width + 1, getY() + height + 1, 0xAA00AA00);
            } else if (isHovered()) {
                graphics.fill(getX() - 1, getY() - 1, getX() + width + 1, getY() + height + 1, 0x66FFFFFF);
            }
            graphics.blit(icon, getX(), getY(), 0, 0, width, height, 16, 16);
        }

        @Override
        public void onClick(double mouseX, double mouseY) {
            if (menu.getBase() != null) {
                ModNetworking.sendToServer(new ToggleTargetFilterPayload(menu.getBase().getBlockPos(), filter));
            }
        }

        @Override
        protected void updateWidgetNarration(NarrationElementOutput output) {
            defaultButtonNarrationText(output);
        }
    }
}
