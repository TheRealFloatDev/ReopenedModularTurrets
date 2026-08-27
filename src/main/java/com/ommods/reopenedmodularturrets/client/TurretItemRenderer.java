package com.ommods.reopenedmodularturrets.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.ommods.reopenedmodularturrets.client.model.DirectedTurretModelState;
import com.ommods.reopenedmodularturrets.client.model.GrenadeTurretModel;
import com.ommods.reopenedmodularturrets.client.model.GunTurretModel;
import com.ommods.reopenedmodularturrets.client.model.ModEntityTextures;
import com.ommods.reopenedmodularturrets.client.model.ModModelLayers;
import com.ommods.reopenedmodularturrets.client.model.TurretRenderHelper;
import com.ommods.reopenedmodularturrets.registry.ModBlocks;
import com.ommods.reopenedmodularturrets.turret.TurretKind;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class TurretItemRenderer extends BlockEntityWithoutLevelRenderer {
    private final GunTurretModel gunModel;
    private final GrenadeTurretModel grenadeModel;

    public TurretItemRenderer(BlockEntityRenderDispatcher dispatcher, EntityModelSet modelSet) {
        super(dispatcher, modelSet);
        this.gunModel = new GunTurretModel(modelSet.bakeLayer(ModModelLayers.GUN_TURRET));
        this.grenadeModel = new GrenadeTurretModel(modelSet.bakeLayer(ModModelLayers.GRENADE_TURRET));
    }

    @Override
    public void renderByItem(
            ItemStack stack,
            ItemDisplayContext displayContext,
            PoseStack poseStack,
            MultiBufferSource buffer,
            int packedLight,
            int packedOverlay
    ) {
        TurretKind kind = kindFromItem(stack.getItem());
        if (kind == null) {
            return;
        }
        DirectedTurretModelState modelState = DirectedTurretModelState.IDLE;
        RenderType renderType = RenderType.entityCutoutNoCull(ModEntityTextures.forTurret(kind));
        VertexConsumer consumer = buffer.getBuffer(renderType);
        poseStack.pushPose();
        TurretRenderHelper.prepareTurretItemPose(poseStack, displayContext);
        if (kind.usesGrenadeModel()) {
            grenadeModel.setupAnim(modelState);
            grenadeModel.renderToBuffer(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        } else {
            gunModel.setupAnim(modelState);
            gunModel.renderToBuffer(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        }
        poseStack.popPose();
    }

    private static TurretKind kindFromItem(Item item) {
        if (!(item instanceof BlockItem blockItem)) {
            return null;
        }
        if (blockItem.getBlock() == ModBlocks.GUN_TURRET.get()) return TurretKind.GUN;
        if (blockItem.getBlock() == ModBlocks.GRENADE_TURRET.get()) return TurretKind.GRENADE;
        if (blockItem.getBlock() == ModBlocks.DISPOSABLE_ITEM_TURRET.get()) return TurretKind.DISPOSABLE_ITEM;
        if (blockItem.getBlock() == ModBlocks.POTATO_CANNON_TURRET.get()) return TurretKind.POTATO_CANNON;
        if (blockItem.getBlock() == ModBlocks.INCENDIARY_TURRET.get()) return TurretKind.INCENDIARY;
        if (blockItem.getBlock() == ModBlocks.ROCKET_TURRET.get()) return TurretKind.ROCKET;
        if (blockItem.getBlock() == ModBlocks.RELATIVISTIC_TURRET.get()) return TurretKind.RELATIVISTIC;
        if (blockItem.getBlock() == ModBlocks.TELEPORTER_TURRET.get()) return TurretKind.TELEPORTER;
        if (blockItem.getBlock() == ModBlocks.LASER_TURRET.get()) return TurretKind.LASER;
        if (blockItem.getBlock() == ModBlocks.RAIL_GUN_TURRET.get()) return TurretKind.RAIL_GUN;
        if (blockItem.getBlock() == ModBlocks.PLASMA_TURRET.get()) return TurretKind.PLASMA;
        if (blockItem.getBlock() == ModBlocks.ARC_TURRET.get()) return TurretKind.ARC;
        if (blockItem.getBlock() == ModBlocks.MELEE_TURRET.get()) return TurretKind.MELEE;
        if (blockItem.getBlock() == ModBlocks.CROSSBOW_TURRET.get()) return TurretKind.CROSSBOW;
        return null;
    }

    public static TurretItemRenderer create() {
        Minecraft mc = Minecraft.getInstance();
        return new TurretItemRenderer(mc.getBlockEntityRenderDispatcher(), mc.getEntityModels());
    }
}
