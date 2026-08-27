package com.ommods.reopenedmodularturrets.client.model;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class RedstoneReactorAddonModel extends PartModel {
    public RedstoneReactorAddonModel(ModelPart root) {
        super(root);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition rootDef = mesh.getRoot();
        rootDef.addOrReplaceChild(
                "core",
                CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, 2.0F, -4.0F, 8.0F, 6.0F, 8.0F),
                PartPose.offset(0.0F, 16.0F, 0.0F));
        rootDef.addOrReplaceChild(
                "cap",
                CubeListBuilder.create().texOffs(0, 14).addBox(-5.0F, 1.0F, -5.0F, 10.0F, 1.0F, 10.0F),
                PartPose.offset(0.0F, 16.0F, 0.0F));
        return LayerDefinition.create(mesh, 32, 32);
    }
}
