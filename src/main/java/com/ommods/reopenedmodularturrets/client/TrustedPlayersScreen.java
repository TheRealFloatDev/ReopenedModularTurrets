package com.ommods.reopenedmodularturrets.client;

import com.ommods.reopenedmodularturrets.blockentity.TurretBaseBlockEntity;
import com.ommods.reopenedmodularturrets.menu.TurretBaseMenu;
import com.ommods.reopenedmodularturrets.network.ModNetworking;
import com.ommods.reopenedmodularturrets.network.payload.TrustedPlayerPayload;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrustedPlayersScreen extends Screen {
    private static final Map<BlockPos, List<String>> CLIENT_TRUSTED_CACHE = new HashMap<>();

    private final BlockPos basePos;
    private final Screen parent;
    private final TurretBaseMenu menu;
    private final List<String> displayNames = new ArrayList<>();
    private EditBox nameField;

    public TrustedPlayersScreen(BlockPos basePos, Screen parent, TurretBaseMenu menu) {
        this(basePos, parent, menu, null);
    }

    public TrustedPlayersScreen(BlockPos basePos, Screen parent, TurretBaseMenu menu, List<String> initialNames) {
        super(Component.translatable("gui.reopenedmodularturrets.trusted_players"));
        this.basePos = basePos;
        this.parent = parent;
        this.menu = menu;
        if (initialNames != null) {
            displayNames.addAll(initialNames);
        }
    }

    @Override
    protected void init() {
        if (displayNames.isEmpty()) {
            reloadFromSource();
        }
        int centerX = width / 2;
        int listTop = 36;

        nameField = new EditBox(font, centerX - 80, height - 52, 120, 18, Component.translatable("gui.reopenedmodularturrets.add_trusted_player"));
        nameField.setMaxLength(16);
        nameField.setResponder(value -> {});
        addRenderableWidget(nameField);
        setInitialFocus(nameField);

        addRenderableWidget(Button.builder(Component.literal("+"), button -> addPlayer())
                .bounds(centerX + 44, height - 52, 20, 18).build());
        addRenderableWidget(Button.builder(Component.literal("-"), button -> removeSelected())
                .bounds(centerX + 68, height - 52, 20, 18).build());
        addRenderableWidget(Button.builder(Component.translatable("gui.back"), button -> onClose())
                .bounds(centerX - 40, height - 28, 80, 20).build());

        int y = listTop;
        for (String name : displayNames) {
            String entry = name;
            addRenderableWidget(Button.builder(Component.literal(entry), button -> removePlayer(entry))
                    .bounds(centerX - 100, y, 200, 18).build());
            y += 20;
            if (y > height - 72) {
                break;
            }
        }
    }

    private void reloadFromSource() {
        displayNames.clear();
        List<String> cached = CLIENT_TRUSTED_CACHE.get(basePos);
        if (cached != null && !cached.isEmpty()) {
            displayNames.addAll(cached);
        }
        if (minecraft != null && minecraft.level != null) {
            if (minecraft.level.getBlockEntity(basePos) instanceof TurretBaseBlockEntity base) {
                List<String> fromBase = base.getTrustedPlayers().getNames();
                if (!fromBase.isEmpty()) {
                    displayNames.clear();
                    displayNames.addAll(fromBase);
                    updateCache();
                }
            }
        }
    }

    private void updateCache() {
        CLIENT_TRUSTED_CACHE.put(basePos, new ArrayList<>(displayNames));
    }

    private void addPlayer() {
        String name = nameField.getValue().trim();
        if (name.isEmpty()) {
            return;
        }
        boolean alreadyListed = displayNames.stream().anyMatch(existing -> existing.equalsIgnoreCase(name));
        if (alreadyListed) {
            return;
        }
        ModNetworking.sendToServer(new TrustedPlayerPayload(TrustedPlayerPayload.Action.ADD, basePos, name));
        displayNames.add(name);
        updateCache();
        nameField.setValue("");
        rebuild();
    }

    private void removeSelected() {
        String name = nameField.getValue().trim();
        if (!name.isEmpty()) {
            removePlayer(name);
        }
    }

    private void removePlayer(String name) {
        ModNetworking.sendToServer(new TrustedPlayerPayload(TrustedPlayerPayload.Action.REMOVE, basePos, name));
        displayNames.removeIf(existing -> existing.equalsIgnoreCase(name));
        updateCache();
        rebuild();
    }

    private void rebuild() {
        if (minecraft != null) {
            minecraft.setScreen(new TrustedPlayersScreen(basePos, parent, menu, new ArrayList<>(displayNames)));
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.render(graphics, mouseX, mouseY, partialTick);
        graphics.drawCenteredString(font, title, width / 2, 12, 0xFFFFFF);
        graphics.drawString(font, Component.translatable("gui.reopenedmodularturrets.add_trusted_player"), width / 2 - 80, height - 64, 0xA0A0A0, false);
        if (displayNames.isEmpty()) {
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
