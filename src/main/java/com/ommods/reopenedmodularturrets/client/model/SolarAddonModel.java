package com.ommods.reopenedmodularturrets.client.model;

import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.util.Unit;

public class SolarAddonModel extends Model<Unit> {

  public SolarAddonModel(ModelPart root) {
    super(root, RenderTypes::entitySolid);
  }

  public static LayerDefinition createBodyLayer() {
    MeshDefinition mesh = new MeshDefinition();
    PartDefinition rootDef = mesh.getRoot();

    rootDef.addOrReplaceChild(
        "panel",
        CubeListBuilder.create().texOffs(26, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 1.0F, 8.0F),
        PartPose.offset(0.0F, 16.0F, 0.0F));
    rootDef.addOrReplaceChild(
        "leg_front",
        CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -8.0F, -1.0F, 2.0F, 6.0F, 1.0F),
        PartPose.offsetAndRotation(0.0F, 16.0F, 0.0F, 0.2321598F, 0.0F, 0.0F));
    rootDef.addOrReplaceChild(
        "leg_back",
        CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -8.0F, 0.0F, 2.0F, 6.0F, 1.0F),
        PartPose.offsetAndRotation(0.0F, 16.0F, 0.0F, -0.2565324F, 0.0F, 0.0F));

    return LayerDefinition.create(mesh, 64, 64);
  }

  @Override
  public void setupAnim(Unit state) {
    super.setupAnim(state);
  }
}
