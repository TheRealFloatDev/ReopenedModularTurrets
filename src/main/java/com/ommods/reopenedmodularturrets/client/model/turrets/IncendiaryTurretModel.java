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

public class IncendiaryTurretModel extends PartModel implements AnimatedTurretModel {
    private final ModelPart base;
    private final ModelPart pole;
    private final ModelPart boxUnder;
    private final ModelPart boxLeft;
    private final ModelPart boxRight;
    private final ModelPart crossBar;
    private final ModelPart barrel1;
    private final ModelPart barrel2;
    private final ModelPart tank;

    public IncendiaryTurretModel(ModelPart root) {
        super(root);
        this.base = root.getChild("base");
        this.pole = root.getChild("pole");
        this.boxUnder = root.getChild("box_under");
        this.boxLeft = root.getChild("box_left");
        this.boxRight = root.getChild("box_right");
        this.crossBar = root.getChild("cross_bar");
        this.barrel1 = root.getChild("barrel1");
        this.barrel2 = root.getChild("barrel2");
        this.tank = root.getChild("tank");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition rootDef = mesh.getRoot();
        rootDef.addOrReplaceChild(
                "base",
                CubeListBuilder.create().texOffs(0, 37).addBox(-6F, 7F, -6F, 12F, 1F, 12F),
                PartPose.offset(0F, 16F, 0F));
        rootDef.addOrReplaceChild(
                "pole",
                CubeListBuilder.create().texOffs(0, 28).addBox(-2F, 0F, -2F, 4F, 4F, 4F),
                PartPose.offset(0F, 19F, 0F));
        rootDef.addOrReplaceChild(
                "box_under",
                CubeListBuilder.create().texOffs(0, 15).addBox(-4F, 3F, -4F, 8F, 1F, 8F),
                PartPose.offset(0F, 16F, 0F));
        rootDef.addOrReplaceChild(
                "box_left",
                CubeListBuilder.create().texOffs(0, 15).addBox(-4F, 4F, -4F, 8F, 1F, 8F),
                PartPose.offsetAndRotation(0F, 16F, 0F, 0F, 0F, 1.570796F));
        rootDef.addOrReplaceChild(
                "box_right",
                CubeListBuilder.create().texOffs(0, 15).addBox(-4F, -5F, -4F, 8F, 1F, 8F),
                PartPose.offsetAndRotation(0F, 16F, 0F, 0F, 0F, 1.570796F));
        rootDef.addOrReplaceChild(
                "cross_bar",
                CubeListBuilder.create().texOffs(0, 0).addBox(-4F, -2F, 0F, 8F, 1F, 1F),
                PartPose.offset(0F, 16F, 0F));
        rootDef.addOrReplaceChild(
                "barrel1",
                CubeListBuilder.create().texOffs(0, 0).addBox(1F, -1F, -10F, 2F, 2F, 11F),
                PartPose.offset(0F, 15F, 0F));
        rootDef.addOrReplaceChild(
                "barrel2",
                CubeListBuilder.create().texOffs(0, 0).addBox(-3F, -1F, -10F, 2F, 2F, 11F),
                PartPose.offset(0F, 15F, 0F));
        rootDef.addOrReplaceChild(
                "tank",
                CubeListBuilder.create().texOffs(29, 0).addBox(-3F, -3F, -6F, 6F, 4F, 10F),
                PartPose.offset(0F, 16F, 0F));
        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override
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
        for (ModelPart part : new ModelPart[] { barrel1, barrel2, tank }) {
            part.xRot = rotationX;
            part.yRot = rotationZ;
        }
    }

    @Override
    public PartModel asPartModel() {
        return this;
    }
}
