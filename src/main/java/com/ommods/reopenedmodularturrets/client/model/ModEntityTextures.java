package com.ommods.reopenedmodularturrets.client.model;

import com.ommods.reopenedmodularturrets.ModConstants;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.resources.Identifier;

public final class ModEntityTextures {
    public static final SpriteId GUN_TURRET = Sheets.BLOCK_ENTITIES_MAPPER.apply(
            Identifier.fromNamespaceAndPath(ModConstants.MOD_ID, "gun_turret"));
    public static final SpriteId GRENADE_TURRET = Sheets.BLOCK_ENTITIES_MAPPER.apply(
            Identifier.fromNamespaceAndPath(ModConstants.MOD_ID, "grenade_turret"));
    public static final SpriteId SOLAR_ADDON = Sheets.BLOCK_ENTITIES_MAPPER.apply(
            Identifier.fromNamespaceAndPath(ModConstants.MOD_ID, "addon_solar"));

    private ModEntityTextures() {}
}
