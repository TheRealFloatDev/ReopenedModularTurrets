package com.ommods.reopenedmodularturrets.client.model;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class TurretBaseModel extends PartModel {
    public TurretBaseModel(ModelPart root) {
        super(root);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition rootDef = mesh.getRoot();
        rootDef.addOrReplaceChild(
                "body",
                CubeListBuilder.create().texOffs(0, 0).addBox(-7.0F, 0.0F, -7.0F, 14.0F, 10.0F, 14.0F),
                PartPose.offset(0.0F, 14.0F, 0.0F));
        rootDef.addOrReplaceChild(
                "cap",
                CubeListBuilder.create().texOffs(0, 24).addBox(-5.0F, -2.0F, -5.0F, 10.0F, 2.0F, 10.0F),
                PartPose.offset(0.0F, 14.0F, 0.0F));
        return LayerDefinition.create(mesh, 64, 64);
    }
}
