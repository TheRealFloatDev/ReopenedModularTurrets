package com.ommods.reopenedmodularturrets.client.model;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class LeverBlockModel extends PartModel {
    private final ModelPart arm;

    public LeverBlockModel(ModelPart root) {
        super(root);
        this.arm = root.getChild("arm");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition rootDef = mesh.getRoot();
        rootDef.addOrReplaceChild(
                "base",
                CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, 0.0F, -4.0F, 8.0F, 2.0F, 8.0F),
                PartPose.offset(0.0F, 16.0F, 0.0F));
        rootDef.addOrReplaceChild(
                "arm",
                CubeListBuilder.create().texOffs(0, 10).addBox(-1.0F, -8.0F, -1.0F, 2.0F, 8.0F, 2.0F),
                PartPose.offsetAndRotation(0.0F, 16.0F, 0.0F, -0.6F, 0.0F, 0.0F));
        return LayerDefinition.create(mesh, 32, 32);
    }
}
