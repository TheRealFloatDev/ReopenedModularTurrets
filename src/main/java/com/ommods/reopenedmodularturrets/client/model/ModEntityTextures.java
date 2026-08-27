package com.ommods.reopenedmodularturrets.client.model;

import com.ommods.reopenedmodularturrets.ModConstants;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.resources.Identifier;
import com.ommods.reopenedmodularturrets.turret.TurretKind;

public final class ModEntityTextures {
    public static final SpriteId SOLAR_ADDON = sprite("addon_solar");

    private ModEntityTextures() {}

    public static SpriteId forTurret(TurretKind kind) {
        return sprite(kind.textureName());
    }

    public static SpriteId sprite(String name) {
        return Sheets.BLOCK_ENTITIES_MAPPER.apply(Identifier.fromNamespaceAndPath(ModConstants.MOD_ID, name));
    }
}
