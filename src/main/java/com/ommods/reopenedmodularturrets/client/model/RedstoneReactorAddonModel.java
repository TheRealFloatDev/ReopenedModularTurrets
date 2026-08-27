package com.ommods.reopenedmodularturrets.client.model;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class RedstoneReactorAddonModel extends PartModel {
    private final ModelPart railLower;
    private final ModelPart railUpper;
    private final ModelPart core;
    private final ModelPart coilLeft;
    private final ModelPart coilRight;

    public RedstoneReactorAddonModel(ModelPart root) {
        super(root);
        this.railLower = root.getChild("rail_lower");
        this.railUpper = root.getChild("rail_upper");
        this.core = root.getChild("core");
        this.coilLeft = root.getChild("coil_left");
        this.coilRight = root.getChild("coil_right");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition rootDef = mesh.getRoot();
        rootDef.addOrReplaceChild(
                "rail_lower",
                CubeListBuilder.create().texOffs(5, 15).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 1.0F, 11.0F),
                PartPose.offset(0.0F, 15.0F, 0.0F));
        rootDef.addOrReplaceChild(
                "rail_upper",
                CubeListBuilder.create().texOffs(5, 15).addBox(-1.0F, -3.0F, 0.0F, 2.0F, 1.0F, 11.0F),
                PartPose.offset(0.0F, 15.0F, 0.0F));
        rootDef.addOrReplaceChild(
                "core",
                CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -4.0F, 9.0F, 6.0F, 6.0F, 6.0F),
                PartPose.offset(0.0F, 15.0F, 0.0F));
        rootDef.addOrReplaceChild(
                "coil_left",
                CubeListBuilder.create().texOffs(29, 0).addBox(-1.0F, -6.0F, 11.0F, 2.0F, 3.0F, 1.0F),
                PartPose.offset(0.0F, 15.0F, 0.0F));
        rootDef.addOrReplaceChild(
                "coil_right",
                CubeListBuilder.create().texOffs(29, 0).addBox(-1.0F, -6.0F, 13.0F, 2.0F, 3.0F, 1.0F),
                PartPose.offset(0.0F, 15.0F, 0.0F));
        return LayerDefinition.create(mesh, 64, 64);
    }

    public void setRotationForTarget(float rotationX, float rotationZ) {
        railLower.xRot = rotationX;
        railLower.yRot = rotationZ;
        railUpper.xRot = rotationX;
        railUpper.yRot = rotationZ;
        core.xRot = rotationX;
        core.yRot = rotationZ;
        coilLeft.xRot = rotationX;
        coilLeft.yRot = rotationZ;
        coilRight.xRot = rotationX;
        coilRight.yRot = rotationZ;
    }
}
