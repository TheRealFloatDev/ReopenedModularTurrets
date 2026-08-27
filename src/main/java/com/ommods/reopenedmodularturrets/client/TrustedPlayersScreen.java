package com.ommods.reopenedmodularturrets.client;

import com.ommods.reopenedmodularturrets.blockentity.TurretBaseBlockEntity;
import com.ommods.reopenedmodularturrets.network.ModNetworking;
import com.ommods.reopenedmodularturrets.network.payload.TrustedPlayerPayload;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public class TrustedPlayersScreen extends Screen {
    private final BlockPos basePos;
    private final Screen parent;
    private EditBox nameField;
    private List<String> trustedNames = List.of();

    public TrustedPlayersScreen(BlockPos basePos, Screen parent) {
        super(Component.translatable("gui.reopenedmodularturrets.trusted_players"));
        this.basePos = basePos;
        this.parent = parent;
    }

    @Override
    protected void init() {
        reloadTrustedNames();
        int centerX = width / 2;
        int listTop = 36;

        nameField = new EditBox(font, centerX - 80, height - 52, 120, 18, Component.translatable("gui.reopenedmodularturrets.add_trusted_player"));
        nameField.setMaxLength(16);
        addRenderableWidget(nameField);

        addRenderableWidget(Button.builder(Component.literal("+"), button -> addPlayer())
                .bounds(centerX + 44, height - 52, 20, 18).build());
        addRenderableWidget(Button.builder(Component.literal("-"), button -> removeSelected())
                .bounds(centerX + 68, height - 52, 20, 18).build());
        addRenderableWidget(Button.builder(Component.translatable("gui.back"), button -> onClose())
                .bounds(centerX - 40, height - 28, 80, 20).build());

        int y = listTop;
        for (String name : trustedNames) {
            int rowY = y;
            addRenderableWidget(Button.builder(Component.literal(name), button -> removePlayer(name))
                    .bounds(centerX - 100, rowY, 200, 18).build());
            y += 20;
            if (y > height - 72) {
                break;
            }
        }
    }

    private void reloadTrustedNames() {
        if (minecraft != null && minecraft.level != null) {
            if (minecraft.level.getBlockEntity(basePos) instanceof TurretBaseBlockEntity base) {
                trustedNames = new ArrayList<>(base.getTrustedPlayers().getNames());
                return;
            }
        }
        trustedNames = List.of();
    }

    private void addPlayer() {
        String name = nameField.getValue().trim();
        if (!name.isEmpty()) {
            ModNetworking.sendToServer(new TrustedPlayerPayload(TrustedPlayerPayload.Action.ADD, basePos, name));
            nameField.setValue("");
            reopen();
        }
    }

    private void removeSelected() {
        String name = nameField.getValue().trim();
        if (!name.isEmpty()) {
            removePlayer(name);
        }
    }

    private void removePlayer(String name) {
        ModNetworking.sendToServer(new TrustedPlayerPayload(TrustedPlayerPayload.Action.REMOVE, basePos, name));
        reopen();
    }

    private void reopen() {
        if (minecraft != null) {
            minecraft.setScreen(new TrustedPlayersScreen(basePos, parent));
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.render(graphics, mouseX, mouseY, partialTick);
        graphics.drawCenteredString(font, title, width / 2, 12, 0xFFFFFF);
        graphics.drawString(font, Component.translatable("gui.reopenedmodularturrets.add_trusted_player"), width / 2 - 80, height - 64, 0xA0A0A0, false);
        if (trustedNames.isEmpty()) {
            graphics.drawCenteredString(font, Component.translatable("gui.reopenedmodularturrets.trusted_none"), width / 2, 48, 0x808080);
        }
    }

    @Override
    public void onClose() {
        if (minecraft != null) {
            minecraft.setScreen(parent);
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
