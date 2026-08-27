package com.ommods.reopenedmodularturrets.client.model;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class GrenadeTurretModel extends PartModel {
    private final ModelPart base;
    private final ModelPart pole;
    private final ModelPart boxUnder;
    private final ModelPart boxLeft;
    private final ModelPart boxRight;
    private final ModelPart crossBar;
    private final ModelPart barrel;
    private final ModelPart chamber;
    private final ModelPart muzzle;

    public GrenadeTurretModel(ModelPart root) {
        super(root);
        this.base = root.getChild("base");
        this.pole = root.getChild("pole");
        this.boxUnder = root.getChild("box_under");
        this.boxLeft = root.getChild("box_left");
        this.boxRight = root.getChild("box_right");
        this.crossBar = root.getChild("cross_bar");
        this.barrel = root.getChild("barrel");
        this.chamber = root.getChild("chamber");
        this.muzzle = root.getChild("muzzle");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition rootDef = mesh.getRoot();

        rootDef.addOrReplaceChild(
                "base",
                CubeListBuilder.create().texOffs(0, 37).addBox(-6.0F, 7.0F, -6.0F, 12.0F, 1.0F, 12.0F),
                PartPose.offset(0.0F, 16.0F, 0.0F));
        rootDef.addOrReplaceChild(
                "pole",
                CubeListBuilder.create().texOffs(0, 28).addBox(-2.0F, 4.0F, -2.0F, 4.0F, 4.0F, 4.0F),
                PartPose.offset(0.0F, 16.0F, 0.0F));
        rootDef.addOrReplaceChild(
                "box_under",
                CubeListBuilder.create().texOffs(0, 15).addBox(-4.0F, 3.0F, -4.0F, 8.0F, 1.0F, 8.0F),
                PartPose.offset(0.0F, 16.0F, 0.0F));
        rootDef.addOrReplaceChild(
                "box_left",
                CubeListBuilder.create().texOffs(0, 15).addBox(-4.0F, 4.0F, -4.0F, 8.0F, 1.0F, 8.0F),
                PartPose.offsetAndRotation(0.0F, 16.0F, 0.0F, 0.0F, 0.0F, ((float) Math.PI / 2F)));
        rootDef.addOrReplaceChild(
                "box_right",
                CubeListBuilder.create().texOffs(0, 15).addBox(-4.0F, -5.0F, -4.0F, 8.0F, 1.0F, 8.0F),
                PartPose.offsetAndRotation(0.0F, 16.0F, 0.0F, 0.0F, 0.0F, ((float) Math.PI / 2F)));
        rootDef.addOrReplaceChild(
                "cross_bar",
                CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -2.0F, 0.0F, 8.0F, 1.0F, 1.0F),
                PartPose.offset(0.0F, 16.0F, 0.0F));
        rootDef.addOrReplaceChild(
                "barrel",
                CubeListBuilder.create().texOffs(32, 0).addBox(-2.0F, -4.0F, -8.0F, 4.0F, 4.0F, 12.0F),
                PartPose.offset(0.0F, 16.0F, 0.0F));
        rootDef.addOrReplaceChild(
                "chamber",
                CubeListBuilder.create().texOffs(35, 20).addBox(-3.0F, -5.0F, 3.0F, 6.0F, 6.0F, 6.0F),
                PartPose.offset(0.0F, 16.0F, 0.0F));
        rootDef.addOrReplaceChild(
                "muzzle",
                CubeListBuilder.create().texOffs(0, 5).addBox(-1.0F, -6.0F, -7.0F, 2.0F, 2.0F, 4.0F),
                PartPose.offset(0.0F, 16.0F, 0.0F));

        return LayerDefinition.create(mesh, 64, 64);
    }

    public void setupAnim(DirectedTurretModelState state) {
        base.xRot = state.baseFitRotationX();
        base.yRot = state.baseFitRotationZ();
        pole.xRot = state.baseFitRotationX();
        pole.yRot = state.baseFitRotationZ();
        boxUnder.xRot = state.baseFitRotationX();

        float rotationX = state.rotationX();
        float rotationZ = state.rotationZ();
        boxUnder.yRot = rotationZ;
        boxLeft.xRot = rotationZ;
        boxRight.xRot = rotationZ;
        crossBar.xRot = rotationX;
        crossBar.yRot = rotationZ;
        barrel.xRot = rotationX;
        barrel.yRot = rotationZ;
        chamber.xRot = rotationX;
        chamber.yRot = rotationZ;
        muzzle.xRot = rotationX;
        muzzle.yRot = rotationZ;
    }
}
