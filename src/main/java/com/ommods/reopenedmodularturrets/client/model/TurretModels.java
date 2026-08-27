package com.ommods.reopenedmodularturrets.client.model;

import com.ommods.reopenedmodularturrets.client.model.turrets.DisposableItemTurretModel;
import com.ommods.reopenedmodularturrets.client.model.turrets.IncendiaryTurretModel;
import com.ommods.reopenedmodularturrets.client.model.turrets.LaserTurretModel;
import com.ommods.reopenedmodularturrets.client.model.turrets.PotatoCannonTurretModel;
import com.ommods.reopenedmodularturrets.client.model.turrets.RailGunTurretModel;
import com.ommods.reopenedmodularturrets.client.model.turrets.RelativisticTurretModel;
import com.ommods.reopenedmodularturrets.client.model.turrets.RocketTurretModel;
import com.ommods.reopenedmodularturrets.client.model.turrets.TeleporterTurretModel;
import com.ommods.reopenedmodularturrets.turret.TurretKind;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;

import java.util.EnumMap;
import java.util.function.Function;

public final class TurretModels {
    private final EnumMap<TurretKind, AnimatedTurretModel> models = new EnumMap<>(TurretKind.class);

    public TurretModels(Function<ModelLayerLocation, ModelPart> baker) {
        AnimatedTurretModel gun = new GunTurretModel(baker.apply(ModModelLayers.GUN_TURRET));
        AnimatedTurretModel grenade = new GrenadeTurretModel(baker.apply(ModModelLayers.GRENADE_TURRET));

        models.put(TurretKind.GUN, gun);
        models.put(TurretKind.GRENADE, grenade);
        models.put(TurretKind.DISPOSABLE_ITEM, new DisposableItemTurretModel(baker.apply(ModModelLayers.DISPOSABLE_ITEM_TURRET)));
        models.put(TurretKind.INCENDIARY, new IncendiaryTurretModel(baker.apply(ModModelLayers.INCENDIARY_TURRET)));
        models.put(TurretKind.ROCKET, new RocketTurretModel(baker.apply(ModModelLayers.ROCKET_TURRET)));
        models.put(TurretKind.LASER, new LaserTurretModel(baker.apply(ModModelLayers.LASER_TURRET)));
        models.put(TurretKind.POTATO_CANNON, new PotatoCannonTurretModel(baker.apply(ModModelLayers.POTATO_CANNON_TURRET)));
        models.put(TurretKind.RAIL_GUN, new RailGunTurretModel(baker.apply(ModModelLayers.RAIL_GUN_TURRET)));
        models.put(TurretKind.RELATIVISTIC, new RelativisticTurretModel(baker.apply(ModModelLayers.RELATIVISTIC_TURRET)));
        models.put(TurretKind.TELEPORTER, new TeleporterTurretModel(baker.apply(ModModelLayers.TELEPORTER_TURRET)));
        models.put(TurretKind.PLASMA, grenade);
        models.put(TurretKind.ARC, gun);
        models.put(TurretKind.MELEE, gun);
        models.put(TurretKind.CROSSBOW, gun);
    }

    public AnimatedTurretModel get(TurretKind kind) {
        return models.get(kind);
    }
}
