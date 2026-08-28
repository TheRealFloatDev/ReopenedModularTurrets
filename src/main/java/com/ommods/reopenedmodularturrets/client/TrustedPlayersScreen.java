package com.ommods.reopenedmodularturrets.client;

import com.ommods.reopenedmodularturrets.api.ownership.AccessLevel;
import com.ommods.reopenedmodularturrets.api.ownership.TrustedPlayer;
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
    private static final Map<BlockPos, List<TrustedPlayer>> CLIENT_TRUSTED_CACHE = new HashMap<>();

    private final BlockPos basePos;
    private final Screen parent;
    private final TurretBaseMenu menu;
    private final List<TrustedPlayer> displayPlayers = new ArrayList<>();
    private EditBox nameField;

    public TrustedPlayersScreen(BlockPos basePos, Screen parent, TurretBaseMenu menu) {
        this(basePos, parent, menu, null);
    }

    public TrustedPlayersScreen(BlockPos basePos, Screen parent, TurretBaseMenu menu, List<TrustedPlayer> initialPlayers) {
        super(Component.translatable("gui.reopenedmodularturrets.trusted_players"));
        this.basePos = basePos;
        this.parent = parent;
        this.menu = menu;
        if (initialPlayers != null) {
            displayPlayers.addAll(initialPlayers);
        }
    }

    @Override
    protected void init() {
        if (displayPlayers.isEmpty()) {
            reloadFromSource();
        }
        int centerX = width / 2;
        int listTop = 36;

        nameField = new EditBox(font, centerX - 80, height - 52, 120, 18, Component.translatable("gui.reopenedmodularturrets.add_trusted_player"));
        nameField.setMaxLength(16);
        addRenderableWidget(nameField);
        setInitialFocus(nameField);

        addRenderableWidget(Button.builder(Component.literal("+"), button -> addPlayer())
                .bounds(centerX + 44, height - 52, 20, 18).build());
        addRenderableWidget(Button.builder(Component.literal("-"), button -> removeSelected())
                .bounds(centerX + 68, height - 52, 20, 18).build());
        addRenderableWidget(Button.builder(Component.translatable("gui.back"), button -> onClose())
                .bounds(centerX - 40, height - 28, 80, 20).build());

        int y = listTop;
        for (TrustedPlayer player : displayPlayers) {
            TrustedPlayer entry = player;
            String label = entry.getName() + " [" + entry.getAccessLevel().getLevel() + "]";
            addRenderableWidget(Button.builder(Component.literal(label), button -> cycleAccess(entry))
                    .bounds(centerX - 100, y, 160, 18).build());
            addRenderableWidget(Button.builder(Component.literal("x"), button -> removePlayer(entry.getName()))
                    .bounds(centerX + 64, y, 20, 18).build());
            y += 20;
            if (y > height - 72) {
                break;
            }
        }
    }

    private void reloadFromSource() {
        displayPlayers.clear();
        List<TrustedPlayer> cached = CLIENT_TRUSTED_CACHE.get(basePos);
        if (cached != null && !cached.isEmpty()) {
            displayPlayers.addAll(cached);
        }
        if (minecraft != null && minecraft.level != null) {
            if (minecraft.level.getBlockEntity(basePos) instanceof TurretBaseBlockEntity base) {
                List<TrustedPlayer> fromBase = base.getTrustedPlayers().getPlayers();
                if (!fromBase.isEmpty()) {
                    displayPlayers.clear();
                    displayPlayers.addAll(fromBase);
                    updateCache();
                }
            }
        }
    }

    private void updateCache() {
        List<TrustedPlayer> copy = new ArrayList<>();
        for (TrustedPlayer player : displayPlayers) {
            copy.add(new TrustedPlayer(player.getName(), player.getAccessLevel()));
        }
        CLIENT_TRUSTED_CACHE.put(basePos, copy);
    }

    private void addPlayer() {
        String name = nameField.getValue().trim();
        if (name.isEmpty()) {
            return;
        }
        boolean alreadyListed = displayPlayers.stream().anyMatch(existing -> existing.getName().equalsIgnoreCase(name));
        if (alreadyListed) {
            return;
        }
        ModNetworking.sendToServer(new TrustedPlayerPayload(TrustedPlayerPayload.Action.ADD, basePos, name));
        displayPlayers.add(new TrustedPlayer(name, AccessLevel.NONE));
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
        displayPlayers.removeIf(existing -> existing.getName().equalsIgnoreCase(name));
        updateCache();
        rebuild();
    }

    private void cycleAccess(TrustedPlayer player) {
        AccessLevel next = player.getAccessLevel().next();
        ModNetworking.sendToServer(new TrustedPlayerPayload(
                TrustedPlayerPayload.Action.CHANGE_ACCESS,
                basePos,
                player.getName(),
                next.getLevel()
        ));
        player.setAccessLevel(next);
        updateCache();
        rebuild();
    }

    private void rebuild() {
        if (minecraft != null) {
            List<TrustedPlayer> copy = new ArrayList<>();
            for (TrustedPlayer player : displayPlayers) {
                copy.add(new TrustedPlayer(player.getName(), player.getAccessLevel()));
            }
            minecraft.setScreen(new TrustedPlayersScreen(basePos, parent, menu, copy));
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.render(graphics, mouseX, mouseY, partialTick);
        graphics.drawCenteredString(font, title, width / 2, 12, 0xFFFFFF);
        graphics.drawString(font, Component.translatable("gui.reopenedmodularturrets.add_trusted_player"), width / 2 - 80, height - 64, 0xA0A0A0, false);
        graphics.drawCenteredString(font, Component.translatable("gui.reopenedmodularturrets.trusted_access_hint"), width / 2, 24, 0x808080);
        if (displayPlayers.isEmpty()) {
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
