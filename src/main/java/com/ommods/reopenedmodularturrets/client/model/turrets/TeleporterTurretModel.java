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

public class TeleporterTurretModel extends PartModel implements AnimatedTurretModel {
    private final ModelPart base;
    private final ModelPart baseStand;
    private final ModelPart pillarLarge;
    private final ModelPart spinner1;
    private final ModelPart spinner2;
    private final ModelPart spinner3;
    private final ModelPart spinner4;

    public TeleporterTurretModel(ModelPart root) {
        super(root);
        this.base = root.getChild("base");
        this.baseStand = root.getChild("base_stand");
        this.pillarLarge = root.getChild("pillar_large");
        this.spinner1 = root.getChild("spinner1");
        this.spinner2 = root.getChild("spinner2");
        this.spinner3 = root.getChild("spinner3");
        this.spinner4 = root.getChild("spinner4");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition rootDef = mesh.getRoot();
        rootDef.addOrReplaceChild(
                "base",
                CubeListBuilder.create().texOffs(0, 37).addBox(-6F, 7F, -6F, 12F, 1F, 12F),
                PartPose.offset(0F, 16F, 0F));
        rootDef.addOrReplaceChild(
                "base_stand",
                CubeListBuilder.create().texOffs(0, 51).addBox(-6F, -1F, -6F, 12F, 1F, 12F),
                PartPose.offset(0F, 13F, 0F));
        rootDef.addOrReplaceChild(
                "pillar_large",
                CubeListBuilder.create().texOffs(0, 0).addBox(-2F, 0F, -2F, 4F, 10F, 4F),
                PartPose.offset(0F, 13F, 0F));
        rootDef.addOrReplaceChild(
                "spinner1",
                CubeListBuilder.create().texOffs(0, 14).addBox(-5F, 0F, -2F, 1F, 8F, 4F),
                PartPose.offset(0F, 14F, 0F));
        rootDef.addOrReplaceChild(
                "spinner2",
                CubeListBuilder.create().texOffs(0, 26).addBox(-2F, 0F, 4F, 4F, 8F, 1F),
                PartPose.offset(0F, 14F, 0F));
        rootDef.addOrReplaceChild(
                "spinner3",
                CubeListBuilder.create().texOffs(0, 26).addBox(-2F, 0F, -5F, 4F, 8F, 1F),
                PartPose.offset(0F, 14F, 0F));
        rootDef.addOrReplaceChild(
                "spinner4",
                CubeListBuilder.create().texOffs(0, 14).addBox(4F, 0F, -2F, 1F, 8F, 4F),
                PartPose.offset(0F, 14F, 0F));
        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override
    public void setupAnim(DirectedTurretModelState state) {
        float fitX = state.baseFitRotationX();
        float fitZ = state.baseFitRotationZ();
        for (ModelPart part : new ModelPart[] { base, baseStand, pillarLarge, spinner1, spinner2, spinner3, spinner4 }) {
            part.xRot = fitX;
            part.yRot = fitZ;
        }
    }

    @Override
    public PartModel asPartModel() {
        return this;
    }
}
