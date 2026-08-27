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

public class LaserTurretModel extends PartModel implements AnimatedTurretModel {
    private final ModelPart base;
    private final ModelPart pole;
    private final ModelPart boxUnder;
    private final ModelPart boxLeft;
    private final ModelPart boxRight;
    private final ModelPart crossBar;
    private final ModelPart chamber;
    private final ModelPart barUnder;
    private final ModelPart barMiddle;
    private final ModelPart barTop;
    private final ModelPart counterWeight;

    public LaserTurretModel(ModelPart root) {
        super(root);
        this.base = root.getChild("base");
        this.pole = root.getChild("pole");
        this.boxUnder = root.getChild("box_under");
        this.boxLeft = root.getChild("box_left");
        this.boxRight = root.getChild("box_right");
        this.crossBar = root.getChild("cross_bar");
        this.chamber = root.getChild("chamber");
        this.barUnder = root.getChild("bar_under");
        this.barMiddle = root.getChild("bar_middle");
        this.barTop = root.getChild("bar_top");
        this.counterWeight = root.getChild("counter_weight");
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
                CubeListBuilder.create().texOffs(0, 28).addBox(-2F, 4F, -2F, 4F, 4F, 4F),
                PartPose.offset(0F, 16F, 0F));
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
                "chamber",
                CubeListBuilder.create().texOffs(20, 0).addBox(-2F, -7F, -3F, 4F, 7F, 4F),
                PartPose.offset(0F, 16F, 0.1F));
        rootDef.addOrReplaceChild(
                "bar_under",
                CubeListBuilder.create().texOffs(37, 0).addBox(-1F, -2F, -12F, 2F, 1F, 10F),
                PartPose.offset(0F, 16F, 0F));
        rootDef.addOrReplaceChild(
                "bar_middle",
                CubeListBuilder.create().texOffs(39, 26).addBox(-1F, -4F, -8F, 2F, 1F, 7F),
                PartPose.offset(0F, 16F, 0F));
        rootDef.addOrReplaceChild(
                "bar_top",
                CubeListBuilder.create().texOffs(37, 16).addBox(-1F, -6F, -6F, 2F, 1F, 7F),
                PartPose.offset(0F, 16F, 0F));
        rootDef.addOrReplaceChild(
                "counter_weight",
                CubeListBuilder.create().texOffs(0, 4).addBox(-2F, -6F, 1F, 4F, 4F, 4F),
                PartPose.offset(0F, 17F, 0F));
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
        for (ModelPart part : new ModelPart[] { chamber, barUnder, barMiddle, barTop, counterWeight }) {
            part.xRot = rotationX;
            part.yRot = rotationZ;
        }
    }

    @Override
    public PartModel asPartModel() {
        return this;
    }
}
