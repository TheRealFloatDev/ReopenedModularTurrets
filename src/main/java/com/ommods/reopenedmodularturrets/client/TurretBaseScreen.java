package com.ommods.reopenedmodularturrets.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.ommods.reopenedmodularturrets.ModConstants;
import com.ommods.reopenedmodularturrets.core.targeting.TargetFilter;
import com.ommods.reopenedmodularturrets.menu.TurretBaseMenu;
import com.ommods.reopenedmodularturrets.network.ModNetworking;
import com.ommods.reopenedmodularturrets.network.payload.AdjustRangePayload;
import com.ommods.reopenedmodularturrets.network.payload.DropBasePayload;
import com.ommods.reopenedmodularturrets.network.payload.DropTurretsPayload;
import com.ommods.reopenedmodularturrets.network.payload.ToggleActivePayload;
import com.ommods.reopenedmodularturrets.network.payload.ToggleMultiTargetPayload;
import com.ommods.reopenedmodularturrets.network.payload.ToggleTargetFilterPayload;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import java.util.ArrayList;
import java.util.List;

public class TurretBaseScreen extends AbstractContainerScreen<TurretBaseMenu> {
    private static final int TEX_SIZE = 256;
    private static final int BUTTON_SIZE = 18;
    private static final int ICON_SIZE = 12;
    private static final int SIDE_BUTTON_W = 80;
    private static final int SIDE_BUTTON_H = 20;

    private static final ResourceLocation ICON_MOBS =
            ResourceLocation.fromNamespaceAndPath(ModConstants.MOD_ID, "textures/gui/filter/mobs.png");
    private static final ResourceLocation ICON_PLAYERS =
            ResourceLocation.fromNamespaceAndPath(ModConstants.MOD_ID, "textures/gui/filter/players.png");
    private static final ResourceLocation ICON_NEUTRAL =
            ResourceLocation.fromNamespaceAndPath(ModConstants.MOD_ID, "textures/gui/filter/neutral.png");
    private static final ResourceLocation ICON_GEAR =
            ResourceLocation.fromNamespaceAndPath(ModConstants.MOD_ID, "textures/gui/filter/gear.png");

    private static final int ENERGY_X = 153;
    private static final int ENERGY_Y = 17;
    private static final int ENERGY_W = 14;
    private static final int ENERGY_H = 51;
    private static final int[] ENERGY_FILL_U = {196, 215, 234};

    private static final int FILTER_X = ENERGY_X - BUTTON_SIZE - 2;
    private static final int FILTER_Y = ENERGY_Y;
    private static final int FILTER_SPACING = 18;

    private static final int RANGE_PLUS_X = 120;
    private static final int RANGE_PLUS_Y = 15;
    private static final int RANGE_MINUS_Y = 50;
    private static final int SIDE_PANEL_X = 180;

    private TrustedPlayersGearButton trustedGearButton;
    private Button targetButton;

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

        trustedGearButton = new TrustedPlayersGearButton(
                leftPos + FILTER_X - BUTTON_SIZE - 2,
                topPos + FILTER_Y + FILTER_SPACING,
                ICON_GEAR,
                () -> minecraft.setScreen(new TrustedPlayersScreen(menu.getBase().getBlockPos(), this, menu))
        );
        addRenderableWidget(trustedGearButton);

        addRenderableWidget(Button.builder(Component.literal("+"), b -> adjustRange(1))
                .bounds(leftPos + RANGE_PLUS_X, topPos + RANGE_PLUS_Y, 20, 20)
                .tooltip(Tooltip.create(Component.translatable("text.reopenedmodularturrets.plus_range")))
                .build());
        addRenderableWidget(Button.builder(Component.literal("-"), b -> adjustRange(-1))
                .bounds(leftPos + RANGE_PLUS_X, topPos + RANGE_MINUS_Y, 20, 20)
                .tooltip(Tooltip.create(Component.translatable("text.reopenedmodularturrets.minus_range")))
                .build());

        int sideX = leftPos + SIDE_PANEL_X;
        addRenderableWidget(Button.builder(Component.translatable("text.reopenedmodularturrets.gui.mode"), b -> toggleActive())
                .bounds(sideX, topPos, SIDE_BUTTON_W, SIDE_BUTTON_H)
                .tooltip(Tooltip.create(Component.translatable("text.reopenedmodularturrets.toggle_mode")))
                .build());
        addRenderableWidget(Button.builder(Component.translatable("text.reopenedmodularturrets.gui.configure"), b -> openConfigure())
                .bounds(sideX, topPos + 25, SIDE_BUTTON_W, SIDE_BUTTON_H)
                .tooltip(Tooltip.create(Component.translatable("text.reopenedmodularturrets.configure_base")))
                .build());
        targetButton = addRenderableWidget(Button.builder(targetLabel(), b -> toggleMultiTarget())
                .bounds(sideX, topPos + 50, SIDE_BUTTON_W, SIDE_BUTTON_H)
                .tooltip(Tooltip.create(Component.translatable("text.reopenedmodularturrets.multi_targeting")))
                .build());
        addRenderableWidget(Button.builder(Component.translatable("text.reopenedmodularturrets.gui.drop_base"), b -> dropBase())
                .bounds(sideX, topPos + 75, SIDE_BUTTON_W, SIDE_BUTTON_H)
                .build());
        addRenderableWidget(Button.builder(Component.translatable("text.reopenedmodularturrets.gui.drop_turrets"), b -> dropTurrets())
                .bounds(sideX, topPos + 100, SIDE_BUTTON_W, SIDE_BUTTON_H)
                .build());

        updateTrustedGearVisibility();
    }

    private Component targetLabel() {
        String modeKey = menu.isMultiTargeting()
                ? "text.reopenedmodularturrets.gui.multi"
                : "text.reopenedmodularturrets.gui.single";
        return Component.translatable("text.reopenedmodularturrets.gui.target")
                .append(": ")
                .append(Component.translatable(modeKey));
    }

    private void adjustRange(int delta) {
        if (menu.getBase() != null) {
            ModNetworking.sendToServer(new AdjustRangePayload(menu.getBase().getBlockPos(), delta));
        }
    }

    private void toggleActive() {
        if (menu.getBase() != null) {
            ModNetworking.sendToServer(new ToggleActivePayload(menu.getBase().getBlockPos()));
        }
    }

    private void toggleMultiTarget() {
        if (menu.getBase() != null) {
            ModNetworking.sendToServer(new ToggleMultiTargetPayload(menu.getBase().getBlockPos()));
        }
    }

    private void openConfigure() {
        if (menu.getBase() != null) {
            minecraft.setScreen(new ConfigureScreen(menu.getBase().getBlockPos(), this, menu));
        }
    }

    private void dropTurrets() {
        if (menu.getBase() != null) {
            ModNetworking.sendToServer(new DropTurretsPayload(menu.getBase().getBlockPos()));
        }
    }

    private void dropBase() {
        if (menu.getBase() != null) {
            ModNetworking.sendToServer(new DropBasePayload(menu.getBase().getBlockPos()));
        }
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        updateTrustedGearVisibility();
        if (targetButton != null) {
            targetButton.setMessage(targetLabel());
        }
        if (menu.getBase() != null) {
            menu.refreshFilterData();
        }
    }

    private void updateTrustedGearVisibility() {
        if (trustedGearButton != null) {
            trustedGearButton.visible = menu.getData(3) == 1;
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
        graphics.drawString(font, Component.translatable("text.reopenedmodularturrets.gui.range"), 116, 6, 0x404040, false);
        int range = menu.getTargetRange();
        int maxRange = menu.getMaxAllowedRange();
        int rangeColor = range >= maxRange ? 0xFFAA22 : 0x009900;
        String rangeText = String.valueOf(range);
        graphics.drawString(font, rangeText, rangeText.length() == 1 ? 127 : 124, 39, rangeColor, false);
        if (tier >= 2) {
            graphics.drawString(font, Component.translatable("gui.reopenedmodularturrets.addons"), 71, 6, 0x404040, false);
            graphics.drawString(font, Component.translatable("gui.reopenedmodularturrets.upgrades"), 71, 39, 0x404040, false);
        }
        graphics.drawString(font, playerInventoryTitle, inventoryLabelX, inventoryLabelY, 0x404040, false);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.render(graphics, mouseX, mouseY, partialTick);
        if (isHoveringEnergyBar(mouseX, mouseY)) {
            renderEnergyTooltip(graphics, mouseX, mouseY);
        }
        if (isHoveringInfoPanel(mouseX, mouseY)) {
            renderInfoTooltip(graphics, mouseX, mouseY);
        }
    }

    private void renderInfoTooltip(GuiGraphics graphics, int mouseX, int mouseY) {
        List<Component> lines = new ArrayList<>();
        if (menu.getBase() != null) {
            String owner = menu.getBase().getOwnedData().getOwnerName();
            lines.add(Component.translatable("text.reopenedmodularturrets.gui.owner", owner.isEmpty() ? "?" : owner));
            lines.add(Component.translatable("text.reopenedmodularturrets.gui.active", yesNo(menu.isActive())));
            lines.add(Component.empty());
            lines.add(Component.translatable("text.reopenedmodularturrets.gui.kills", menu.getData(18)));
            lines.add(Component.translatable("text.reopenedmodularturrets.gui.player_kills", menu.getData(19)));
            lines.add(Component.empty());
            lines.add(Component.translatable("text.reopenedmodularturrets.gui.attack_mobs", yesNo(menu.getData(2) == 1)));
            lines.add(Component.translatable("text.reopenedmodularturrets.gui.attack_neutral", yesNo(menu.getData(4) == 1)));
            lines.add(Component.translatable("text.reopenedmodularturrets.gui.attack_players", yesNo(menu.getData(3) == 1)));
        }
        graphics.renderComponentTooltip(font, lines, mouseX, mouseY);
    }

    private static Component yesNo(boolean value) {
        return Component.translatable(value ? "text.reopenedmodularturrets.gui.yes" : "text.reopenedmodularturrets.gui.no");
    }

    private boolean isHoveringInfoPanel(int mouseX, int mouseY) {
        return mouseX >= leftPos - 128 && mouseX < leftPos
                && mouseY >= topPos + 17 && mouseY < topPos + 120;
    }

    private void renderEnergyTooltip(GuiGraphics graphics, int mouseX, int mouseY) {
        int max = Math.max(1, menu.getMaxEnergy());
        int percent = menu.getEnergyStored() * 100 / max;
        graphics.renderComponentTooltip(font, List.of(Component.translatable(
                "gui.reopenedmodularturrets.energy",
                menu.getEnergyStored(),
                max,
                percent
        )), mouseX, mouseY);
    }

    private boolean isHoveringEnergyBar(int mouseX, int mouseY) {
        return mouseX >= leftPos + ENERGY_X
                && mouseX < leftPos + ENERGY_X + ENERGY_W
                && mouseY >= topPos + ENERGY_Y
                && mouseY < topPos + ENERGY_Y + ENERGY_H;
    }

    private boolean isFilterEnabled(TargetFilter filter) {
        return switch (filter) {
            case MOBS -> menu.getData(2) == 1;
            case PLAYERS -> menu.getData(3) == 1;
            case NEUTRAL -> menu.getData(4) == 1;
        };
    }

    private static void blitIcon(GuiGraphics graphics, ResourceLocation icon, int x, int y) {
        graphics.blit(icon, x + (BUTTON_SIZE - ICON_SIZE) / 2, y + (BUTTON_SIZE - ICON_SIZE) / 2, 0, 0, ICON_SIZE, ICON_SIZE, ICON_SIZE, ICON_SIZE);
    }

    private final class TargetFilterIconButton extends AbstractButton {
        private final TargetFilter filter;
        private final ResourceLocation icon;

        TargetFilterIconButton(int x, int y, TargetFilter filter, ResourceLocation icon) {
            super(x, y, BUTTON_SIZE, BUTTON_SIZE, Component.empty());
            this.filter = filter;
            this.icon = icon;
            this.setTooltip(Tooltip.create(Component.translatable(
                    "gui.reopenedmodularturrets.filter." + filter.name().toLowerCase())));
        }

        @Override
        protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
            boolean enabled = isFilterEnabled(filter);
            boolean highlighted = enabled || isHoveredOrFocused();
            graphics.setColor(1.0F, 1.0F, 1.0F, this.alpha);
            RenderSystem.enableBlend();
            RenderSystem.enableDepthTest();
            graphics.blitSprite(SPRITES.get(true, highlighted), getX(), getY(), getWidth(), getHeight());
            graphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
            blitIcon(graphics, icon, getX(), getY());
        }

        @Override
        public void onPress() {
            menu.toggleFilterClient(filter);
            updateTrustedGearVisibility();
            if (menu.getBase() != null) {
                ModNetworking.sendToServer(new ToggleTargetFilterPayload(menu.getBase().getBlockPos(), filter));
            }
        }

        @Override
        public void updateWidgetNarration(NarrationElementOutput output) {
            defaultButtonNarrationText(output);
        }
    }

    private final class TrustedPlayersGearButton extends AbstractButton {
        private final ResourceLocation icon;
        private final Runnable onOpen;

        TrustedPlayersGearButton(int x, int y, ResourceLocation icon, Runnable onOpen) {
            super(x, y, BUTTON_SIZE, BUTTON_SIZE, Component.empty());
            this.icon = icon;
            this.onOpen = onOpen;
            this.setTooltip(Tooltip.create(Component.translatable("gui.reopenedmodularturrets.trusted_players")));
        }

        @Override
        protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
            graphics.setColor(1.0F, 1.0F, 1.0F, this.alpha);
            RenderSystem.enableBlend();
            RenderSystem.enableDepthTest();
            graphics.blitSprite(SPRITES.get(this.active, isHoveredOrFocused()), getX(), getY(), getWidth(), getHeight());
            graphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
            blitIcon(graphics, icon, getX(), getY());
        }

        @Override
        public void onPress() {
            onOpen.run();
        }

        @Override
        public void updateWidgetNarration(NarrationElementOutput output) {
            defaultButtonNarrationText(output);
        }
    }
}
