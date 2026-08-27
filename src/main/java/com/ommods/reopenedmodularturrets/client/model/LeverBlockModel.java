package com.ommods.reopenedmodularturrets.client.model;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class LeverBlockModel extends PartModel {
    private final ModelPart crank;
    private final ModelPart handle;

    public LeverBlockModel(ModelPart root) {
        super(root);
        this.crank = root.getChild("crank");
        this.handle = root.getChild("handle");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition rootDef = mesh.getRoot();
        rootDef.addOrReplaceChild(
                "crank",
                CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -1.0F, -9.0F, 2.0F, 2.0F, 8.0F),
                PartPose.offset(0.0F, 16.0F, 0.0F));
        rootDef.addOrReplaceChild(
                "handle",
                CubeListBuilder.create().texOffs(22, 0).addBox(-1.0F, 0.0F, -3.0F, 2.0F, 6.0F, 2.0F),
                PartPose.offset(0.0F, 16.0F, 0.0F));
        return LayerDefinition.create(mesh, 32, 32);
    }
}
