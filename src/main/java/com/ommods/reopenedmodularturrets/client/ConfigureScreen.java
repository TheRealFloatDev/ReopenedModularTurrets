package com.ommods.reopenedmodularturrets.client;

import com.ommods.reopenedmodularturrets.ModConstants;
import com.ommods.reopenedmodularturrets.core.targeting.TargetFilter;
import com.ommods.reopenedmodularturrets.menu.TurretBaseMenu;
import com.ommods.reopenedmodularturrets.network.ModNetworking;
import com.ommods.reopenedmodularturrets.network.payload.AdjustLightPayload;
import com.ommods.reopenedmodularturrets.network.payload.ToggleTargetFilterPayload;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class ConfigureScreen extends Screen {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(ModConstants.MOD_ID, "textures/gui/configure.png");
    private static final int TEX_W = 256;
    private static final int TEX_H = 256;
    private static final int PANEL_W = 176;
    private static final int PANEL_H = 205;

    private final BlockPos basePos;
    private final Screen parent;
    private final TurretBaseMenu menu;

    private int leftPos;
    private int topPos;
    private Button mobsButton;
    private Button neutralsButton;
    private Button playersButton;
    private LightSlider lightValueSlider;
    private LightSlider lightOpacitySlider;

    public ConfigureScreen(BlockPos basePos, Screen parent, TurretBaseMenu menu) {
        super(Component.translatable("text.reopenedmodularturrets.gui.configure"));
        this.basePos = basePos;
        this.parent = parent;
        this.menu = menu;
    }

    @Override
    protected void init() {
        leftPos = (width - PANEL_W) / 2;
        topPos = (height - PANEL_H) / 2;

        mobsButton = addRenderableWidget(Button.builder(filterLabel(TargetFilter.MOBS), b -> toggleFilter(TargetFilter.MOBS))
                .bounds(leftPos + 10, topPos + 20, 155, 20).build());
        neutralsButton = addRenderableWidget(Button.builder(filterLabel(TargetFilter.NEUTRAL), b -> toggleFilter(TargetFilter.NEUTRAL))
                .bounds(leftPos + 10, topPos + 40, 155, 20).build());
        playersButton = addRenderableWidget(Button.builder(filterLabel(TargetFilter.PLAYERS), b -> toggleFilter(TargetFilter.PLAYERS))
                .bounds(leftPos + 10, topPos + 60, 155, 20).build());

        addRenderableWidget(Button.builder(Component.translatable("gui.reopenedmodularturrets.trusted_players"), b -> openTrustedPlayers())
                .bounds(leftPos + 10, topPos + 95, 155, 20).build());

        int tier = menu.getBase() != null ? menu.getBase().getTier() : 1;
        if (tier > 3) {
            lightValueSlider = addRenderableWidget(new LightSlider(
                    leftPos + 10, topPos + 157, 155, 20,
                    Component.translatable("text.reopenedmodularturrets.gui.light_value"),
                    menu.getData(20),
                    AdjustLightPayload.LightField.VALUE
            ));
            lightOpacitySlider = addRenderableWidget(new LightSlider(
                    leftPos + 10, topPos + 179, 155, 20,
                    Component.translatable("text.reopenedmodularturrets.gui.light_opacity"),
                    menu.getData(21),
                    AdjustLightPayload.LightField.OPACITY
            ));
        }

        addRenderableWidget(Button.builder(Component.translatable("gui.back"), b -> onClose())
                .bounds(leftPos + 185, topPos + 20, 80, 20).build());
    }

    private void openTrustedPlayers() {
        minecraft.setScreen(new TrustedPlayersScreen(basePos, this, menu));
    }

    private void toggleFilter(TargetFilter filter) {
        menu.toggleFilterClient(filter);
        ModNetworking.sendToServer(new ToggleTargetFilterPayload(basePos, filter));
        refreshFilterButtons();
    }

    private void refreshFilterButtons() {
        mobsButton.setMessage(filterLabel(TargetFilter.MOBS));
        neutralsButton.setMessage(filterLabel(TargetFilter.NEUTRAL));
        playersButton.setMessage(filterLabel(TargetFilter.PLAYERS));
    }

    private Component filterLabel(TargetFilter filter) {
        boolean enabled = switch (filter) {
            case MOBS -> menu.getData(2) == 1;
            case PLAYERS -> menu.getData(3) == 1;
            case NEUTRAL -> menu.getData(4) == 1;
        };
        String key = switch (filter) {
            case MOBS -> "text.reopenedmodularturrets.gui.attack_mobs";
            case PLAYERS -> "text.reopenedmodularturrets.gui.attack_players";
            case NEUTRAL -> "text.reopenedmodularturrets.gui.attack_neutral";
        };
        Component state = Component.translatable(enabled ? "text.reopenedmodularturrets.gui.yes" : "text.reopenedmodularturrets.gui.no");
        return Component.translatable(key, state);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(graphics, mouseX, mouseY, partialTick);
        graphics.blit(TEXTURE, leftPos, topPos, 0, 0, PANEL_W, PANEL_H, TEX_W, TEX_H);
        graphics.drawString(font, Component.translatable("text.reopenedmodularturrets.gui.targeting_options"), leftPos + 10, topPos + 9, 0x404040, false);
        int tier = menu.getBase() != null ? menu.getBase().getTier() : 1;
        if (tier > 3) {
            graphics.drawString(font, Component.translatable("text.reopenedmodularturrets.gui.light_values"), leftPos + 10, topPos + 145, 0x404040, false);
            graphics.drawString(font, Component.translatable("text.reopenedmodularturrets.gui.light_help"), leftPos + 10, topPos + 200, 0x606060, false);
        }
        refreshFilterButtons();
        super.render(graphics, mouseX, mouseY, partialTick);
    }

    @Override
    public void onClose() {
        minecraft.setScreen(parent);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    private final class LightSlider extends AbstractSliderButton {
        private final Component prefix;
        private final AdjustLightPayload.LightField field;

        LightSlider(int x, int y, int width, int height, Component prefix, int initial, AdjustLightPayload.LightField field) {
            super(x, y, width, height, Component.empty(), initial / 15.0D);
            this.prefix = prefix;
            this.field = field;
            updateMessage();
        }

        @Override
        protected void updateMessage() {
            setMessage(prefix.copy().append(": " + Math.round(value * 15.0F)));
        }

        @Override
        protected void applyValue() {
            ModNetworking.sendToServer(new AdjustLightPayload(basePos, field, Math.round((float) (value * 15.0D))));
        }
    }
}
