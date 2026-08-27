package com.ommods.reopenedmodularturrets.client.model.turrets;

import com.ommods.reopenedmodularturrets.client.model.AnimatedTurretModel;
import com.ommods.reopenedmodularturrets.client.model.DirectedTurretModelState;
import com.ommods.reopenedmodularturrets.client.model.PartModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class RailGunTurretModel extends PartModel implements AnimatedTurretModel {
    private final ModelPart base;
    private final ModelPart barrelTop;
    private final ModelPart barrelBot;
    private final ModelPart barrelRight;
    private final ModelPart barrelLeft;
    private final ModelPart bodyBot;
    private final ModelPart bodyTop;
    private final ModelPart binder;
    private final ModelPart rightGuard;
    private final ModelPart leftGuard;
    private final ModelPart guardBinder;

    public RailGunTurretModel(ModelPart root) {
        super(root);
        this.base = root.getChild("base");
        this.barrelTop = root.getChild("barrel_top");
        this.barrelBot = root.getChild("barrel_bot");
        this.barrelRight = root.getChild("barrel_right");
        this.barrelLeft = root.getChild("barrel_left");
        this.bodyBot = root.getChild("body_bot");
        this.bodyTop = root.getChild("body_top");
        this.binder = root.getChild("binder");
        this.rightGuard = root.getChild("right_guard");
        this.leftGuard = root.getChild("left_guard");
        this.guardBinder = root.getChild("guard_binder");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition rootDef = mesh.getRoot();
        rootDef.addOrReplaceChild(
                "base",
                CubeListBuilder.create().texOffs(0, 0).addBox(-6F, 7F, -6F, 12F, 1F, 12F),
                PartPose.offset(0F, 16F, 0F));
        rootDef.addOrReplaceChild(
                "barrel_top",
                CubeListBuilder.create().texOffs(25, 27).addBox(-1F, 2F, -16F, 2F, 1F, 17F),
                PartPose.offset(0F, 15F, 0F));
        rootDef.addOrReplaceChild(
                "barrel_bot",
                CubeListBuilder.create().texOffs(25, 27).addBox(-1F, -1F, -16F, 2F, 1F, 17F),
                PartPose.offset(0F, 15F, 0F));
        rootDef.addOrReplaceChild(
                "barrel_right",
                CubeListBuilder.create().texOffs(25, 45).addBox(-2F, -1F, -16F, 1F, 2F, 17F),
                PartPose.offset(0F, 15F, 0F));
        rootDef.addOrReplaceChild(
                "barrel_left",
                CubeListBuilder.create().texOffs(25, 45).addBox(1F, -1F, -16F, 1F, 2F, 17F),
                PartPose.offset(0F, 15F, 0F));
        rootDef.addOrReplaceChild(
                "body_bot",
                CubeListBuilder.create().texOffs(0, 29).addBox(-3F, 2F, 0F, 6F, 2F, 6F),
                PartPose.offset(0F, 15F, 0F));
        rootDef.addOrReplaceChild(
                "body_top",
                CubeListBuilder.create().texOffs(0, 37).addBox(-3F, -3F, 1F, 6F, 4F, 6F),
                PartPose.offset(0F, 15F, 0F));
        rootDef.addOrReplaceChild(
                "binder",
                CubeListBuilder.create().texOffs(0, 21).addBox(-1F, 1F, 3F, 2F, 1F, 1F),
                PartPose.offset(0F, 15F, 0F));
        rootDef.addOrReplaceChild(
                "right_guard",
                CubeListBuilder.create().texOffs(0, 47).addBox(-6.1F, -5F, -3F, 1F, 8F, 8F),
                PartPose.offset(0F, 15F, 0F));
        rootDef.addOrReplaceChild(
                "left_guard",
                CubeListBuilder.create().texOffs(0, 47).addBox(5.1F, -5F, -3F, 1F, 8F, 8F),
                PartPose.offset(0F, 15F, 0F));
        rootDef.addOrReplaceChild(
                "guard_binder",
                CubeListBuilder.create().texOffs(0, 25).addBox(-6F, -0.9F, 0F, 12F, 1F, 1F),
                PartPose.offset(0F, 15F, 0F));
        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override
    public void setupAnim(DirectedTurretModelState state) {
        base.xRot = state.baseFitRotationX();
        base.yRot = state.baseFitRotationZ();
        float rotationX = state.rotationX();
        float rotationZ = state.rotationZ();
        barrelTop.xRot = rotationX;
        barrelTop.yRot = rotationZ;
        barrelBot.xRot = rotationX;
        barrelBot.yRot = rotationZ;
        barrelRight.xRot = rotationX;
        barrelRight.yRot = rotationZ;
        barrelLeft.xRot = rotationX;
        barrelLeft.yRot = rotationZ;
        bodyBot.xRot = rotationX;
        bodyBot.yRot = rotationZ;
        bodyTop.xRot = rotationX;
        bodyTop.yRot = rotationZ;
        binder.xRot = rotationX;
        binder.yRot = rotationZ;
        rightGuard.xRot = rotationX;
        rightGuard.yRot = rotationZ;
        leftGuard.xRot = rotationX;
        leftGuard.yRot = rotationZ;
        guardBinder.xRot = rotationX;
        guardBinder.yRot = rotationZ;
    }

    @Override
    public PartModel asPartModel() {
        return this;
    }
}
