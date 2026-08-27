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

public class RelativisticTurretModel extends PartModel implements AnimatedTurretModel {
    private final ModelPart base;
    private final ModelPart spike1;
    private final ModelPart spike2;
    private final ModelPart spike3;
    private final ModelPart spike4;
    private final ModelPart base2;
    private final ModelPart crystal;

    public RelativisticTurretModel(ModelPart root) {
        super(root);
        this.base = root.getChild("base");
        this.spike1 = root.getChild("spike1");
        this.spike2 = root.getChild("spike2");
        this.spike3 = root.getChild("spike3");
        this.spike4 = root.getChild("spike4");
        this.base2 = root.getChild("base2");
        this.crystal = root.getChild("crystal");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition rootDef = mesh.getRoot();
        rootDef.addOrReplaceChild(
                "base",
                CubeListBuilder.create().texOffs(0, 37).addBox(-6F, 7F, -6F, 12F, 1F, 12F),
                PartPose.offset(0F, 16F, 0F));
        rootDef.addOrReplaceChild(
                "spike1",
                CubeListBuilder.create().texOffs(24, 0).addBox(-6F, 0F, -6F, 1F, 8F, 1F),
                PartPose.offset(0F, 15F, 0F));
        rootDef.addOrReplaceChild(
                "spike2",
                CubeListBuilder.create().texOffs(24, 0).addBox(-6F, 0F, 5F, 1F, 8F, 1F),
                PartPose.offset(0F, 15F, 0F));
        rootDef.addOrReplaceChild(
                "spike3",
                CubeListBuilder.create().texOffs(24, 0).addBox(5F, 0F, -6F, 1F, 8F, 1F),
                PartPose.offset(0F, 15F, 0F));
        rootDef.addOrReplaceChild(
                "spike4",
                CubeListBuilder.create().texOffs(24, 0).addBox(5F, 0F, 5F, 1F, 8F, 1F),
                PartPose.offset(0F, 15F, 0F));
        rootDef.addOrReplaceChild(
                "base2",
                CubeListBuilder.create().texOffs(0, 0).addBox(-2F, 6F, -2F, 4F, 2F, 4F),
                PartPose.offset(0F, 15F, 0F));
        rootDef.addOrReplaceChild(
                "crystal",
                CubeListBuilder.create().texOffs(0, 25).addBox(-2F, -2F, -2F, 4F, 4F, 4F),
                PartPose.offsetAndRotation(0F, 15F, 0F, 0.7853982F, 0.7853982F, 0.7853982F));
        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override
    public void setupAnim(DirectedTurretModelState state) {
        float fitX = state.baseFitRotationX();
        float fitZ = state.baseFitRotationZ();
        for (ModelPart part : new ModelPart[] { base, spike1, spike2, spike3, spike4, base2, crystal }) {
            part.xRot = fitX;
            part.yRot = fitZ;
        }
    }

    @Override
    public PartModel asPartModel() {
        return this;
    }
}
