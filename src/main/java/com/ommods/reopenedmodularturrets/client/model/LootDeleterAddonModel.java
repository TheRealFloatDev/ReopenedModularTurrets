package com.ommods.reopenedmodularturrets.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class LootDeleterAddonModel extends PartModel {
    private final ModelPart body;
    private final ModelPart top;

    public LootDeleterAddonModel(ModelPart root) {
        super(root);
        this.body = root.getChild("body");
        this.top = root.getChild("top");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition rootDef = mesh.getRoot();
        rootDef.addOrReplaceChild(
                "body",
                CubeListBuilder.create().texOffs(0, 0).addBox(-7.0F, 4.0F, -7.0F, 14.0F, 10.0F, 14.0F),
                PartPose.offset(0.0F, 16.0F, 0.0F));
        rootDef.addOrReplaceChild(
                "top",
                CubeListBuilder.create().texOffs(0, 0).addBox(-7.0F, 3.0F, -7.0F, 14.0F, 1.0F, 14.0F),
                PartPose.offset(0.0F, 16.0F, 0.0F));
        return LayerDefinition.create(mesh, 32, 32);
    }

    public void renderBody(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
        body.render(poseStack, buffer, packedLight, packedOverlay, color);
    }

    public void renderTop(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
        top.render(poseStack, buffer, packedLight, packedOverlay, color);
    }
}
