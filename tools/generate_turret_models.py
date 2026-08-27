#!/usr/bin/env python3
"""Generate turret model Java classes from OMT 1.12 definitions."""

from __future__ import annotations

from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
OUT = ROOT / "src/main/java/com/ommods/reopenedmodularturrets/client/model/turrets"

HEADER = """package com.ommods.reopenedmodularturrets.client.model.turrets;

import com.ommods.reopenedmodularturrets.client.model.AnimatedTurretModel;
import com.ommods.reopenedmodularturrets.client.model.DirectedTurretModelState;
import com.ommods.reopenedmodularturrets.client.model.PartModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

"""

MODELS: dict[str, dict] = {
    "DisposableItemTurretModel": {
        "layer": "disposable_item_turret",
        "fields": [
            ("base", 0, 37, (-6, 7, -6, 12, 1, 12), (0, 16, 0), None),
            ("pole", 0, 28, (-2, 4, -2, 4, 4, 4), (0, 16, 0), None),
            ("box_under", 0, 19, (-4, 3, -4, 8, 1, 8), (0, 16, 0), None),
            ("box_left", 0, 19, (-4, 4, -4, 8, 1, 8), (0, 16, 0), (0, 0, 1.570796)),
            ("box_right", 0, 19, (-4, -5, -4, 8, 1, 8), (0, 16, 0), (0, 0, 1.570796)),
            ("cross_bar", 0, 0, (-4, -2, 0, 8, 1, 1), (0, 16, 0), None),
            ("cannon", 20, 0, (-2, -3, -12, 4, 4, 14), (0, 16, 0), None),
        ],
        "directed": True,
    },
    "IncendiaryTurretModel": {
        "layer": "incendiary_turret",
        "fields": [
            ("base", 0, 37, (-6, 7, -6, 12, 1, 12), (0, 16, 0), None),
            ("pole", 0, 28, (-2, 0, -2, 4, 4, 4), (0, 19, 0), None),
            ("box_under", 0, 15, (-4, 3, -4, 8, 1, 8), (0, 16, 0), None),
            ("box_left", 0, 15, (-4, 4, -4, 8, 1, 8), (0, 16, 0), (0, 0, 1.570796)),
            ("box_right", 0, 15, (-4, -5, -4, 8, 1, 8), (0, 16, 0), (0, 0, 1.570796)),
            ("cross_bar", 0, 0, (-4, -2, 0, 8, 1, 1), (0, 16, 0), None),
            ("barrel1", 0, 0, (1, -1, -10, 2, 2, 11), (0, 15, 0), None),
            ("barrel2", 0, 0, (-3, -1, -10, 2, 2, 11), (0, 15, 0), None),
            ("tank", 29, 0, (-3, -3, -6, 6, 4, 10), (0, 16, 0), None),
        ],
        "directed": True,
    },
    "RocketTurretModel": {
        "layer": "rocket_turret",
        "fields": [
            ("base", 0, 37, (-6, 7, -6, 12, 1, 12), (0, 16, 0), None),
            ("pole", 0, 28, (-2, 4, -2, 4, 4, 4), (0, 16, 0), None),
            ("box_under", 0, 15, (-4, 3, -4, 8, 1, 8), (0, 16, 0), None),
            ("box_left", 0, 15, (-4, 4, -4, 8, 1, 8), (0, 16, 0), (0, 0, 1.570796)),
            ("box_right", 0, 15, (-4, -5, -4, 8, 1, 8), (0, 16, 0), (0, 0, 1.570796)),
            ("cross_bar", 0, 0, (-4, -2, 0, 8, 1, 1), (0, 16, 0), None),
            ("launcher", 36, 0, (-2, -5, -2, 4, 8, 8), (0, 15, 0), None),
            ("missile1", 0, 6, (-1, -5, -4, 2, 2, 2), (0, 16, 0), None),
            ("missile2", 0, 6, (-1, -1, -4, 2, 2, 2), (0, 16, 0), None),
        ],
        "directed": True,
    },
    "LaserTurretModel": {
        "layer": "laser_turret",
        "fields": [
            ("base", 0, 37, (-6, 7, -6, 12, 1, 12), (0, 16, 0), None),
            ("pole", 0, 28, (-2, 4, -2, 4, 4, 4), (0, 16, 0), None),
            ("box_under", 0, 15, (-4, 3, -4, 8, 1, 8), (0, 16, 0), None),
            ("box_left", 0, 15, (-4, 4, -4, 8, 1, 8), (0, 16, 0), (0, 0, 1.570796)),
            ("box_right", 0, 15, (-4, -5, -4, 8, 1, 8), (0, 16, 0), (0, 0, 1.570796)),
            ("cross_bar", 0, 0, (-4, -2, 0, 8, 1, 1), (0, 16, 0), None),
            ("chamber", 20, 0, (-2, -7, -3, 4, 7, 4), (0, 16, 0.1), None),
            ("bar_under", 37, 0, (-1, -2, -12, 2, 1, 10), (0, 16, 0), None),
            ("bar_middle", 39, 26, (-1, -4, -8, 2, 1, 7), (0, 16, 0), None),
            ("bar_top", 37, 16, (-1, -6, -6, 2, 1, 7), (0, 16, 0), None),
            ("counter_weight", 0, 4, (-2, -6, 1, 4, 4, 4), (0, 17, 0), None),
        ],
        "directed": True,
    },
    "PotatoCannonTurretModel": {
        "layer": "potato_cannon_turret",
        "fields": [
            ("base", 0, 37, (-6, 7, -6, 12, 1, 12), (0, 16, 0), None),
            ("pole", 0, 28, (-2, 4, -2, 4, 4, 4), (0, 16, 0), None),
            ("box_under", 0, 15, (-4, 3, -4, 8, 1, 8), (0, 16, 0), None),
            ("box_left", 0, 15, (-4, 4, -4, 8, 1, 8), (0, 16, 0), (0, 0, 1.570796)),
            ("box_right", 0, 15, (-4, -5, -4, 8, 1, 8), (0, 16, 0), (0, 0, 1.570796)),
            ("cross_bar", 0, 0, (-4, -2, 0, 8, 1, 1), (0, 16, 0), None),
            ("barrel", 36, 0, (-1, -2, -11, 2, 3, 12), (0, 15, 0), None),
            ("chamber", 0, 4, (-2, -3, 1, 4, 4, 4), (0, 15, 0), None),
        ],
        "directed": True,
    },
    "RailGunTurretModel": {
        "layer": "rail_gun_turret",
        "fields": [
            ("base", 0, 0, (-6, 7, -6, 12, 1, 12), (0, 16, 0), None),
            ("barrel_top", 25, 27, (-1, 2, -16, 2, 1, 17), (0, 15, 0), None),
            ("barrel_bot", 25, 27, (-1, -1, -16, 2, 1, 17), (0, 15, 0), None),
            ("barrel_right", 25, 45, (-2, -1, -16, 1, 2, 17), (0, 15, 0), None),
            ("barrel_left", 25, 45, (1, -1, -16, 1, 2, 17), (0, 15, 0), None),
            ("body_bot", 0, 29, (-3, 2, 0, 6, 2, 6), (0, 15, 0), None),
            ("body_top", 0, 37, (-3, -3, 1, 6, 4, 6), (0, 15, 0), None),
            ("binder", 0, 21, (-1, 1, 3, 2, 1, 1), (0, 15, 0), None),
            ("right_guard", 0, 47, (-6.1, -5, -3, 1, 8, 8), (0, 15, 0), None),
            ("left_guard", 0, 47, (5.1, -5, -3, 1, 8, 8), (0, 15, 0), None),
            ("guard_binder", 0, 25, (-6, -0.9, 0, 12, 1, 1), (0, 15, 0), None),
        ],
        "directed": True,
        "railgun": True,
    },
    "RelativisticTurretModel": {
        "layer": "relativistic_turret",
        "fields": [
            ("base", 0, 37, (-6, 7, -6, 12, 1, 12), (0, 16, 0), None),
            ("spike1", 24, 0, (-6, 0, -6, 1, 8, 1), (0, 15, 0), None),
            ("spike2", 24, 0, (-6, 0, 5, 1, 8, 1), (0, 15, 0), None),
            ("spike3", 24, 0, (5, 0, -6, 1, 8, 1), (0, 15, 0), None),
            ("spike4", 24, 0, (5, 0, 5, 1, 8, 1), (0, 15, 0), None),
            ("base2", 0, 0, (-2, 6, -2, 4, 2, 4), (0, 15, 0), None),
            ("crystal", 0, 25, (-2, -2, -2, 4, 4, 4), (0, 15, 0), (0.7853982, 0.7853982, 0.7853982)),
        ],
        "directed": False,
        "base_fit_all": True,
    },
    "TeleporterTurretModel": {
        "layer": "teleporter_turret",
        "fields": [
            ("base", 0, 37, (-6, 7, -6, 12, 1, 12), (0, 16, 0), None),
            ("base_stand", 0, 51, (-6, -1, -6, 12, 1, 12), (0, 13, 0), None),
            ("pillar_large", 0, 0, (-2, 0, -2, 4, 10, 4), (0, 13, 0), None),
            ("spinner1", 0, 14, (-5, 0, -2, 1, 8, 4), (0, 14, 0), None),
            ("spinner2", 0, 26, (-2, 0, 4, 4, 8, 1), (0, 14, 0), None),
            ("spinner3", 0, 26, (-2, 0, -5, 4, 8, 1), (0, 14, 0), None),
            ("spinner4", 0, 14, (4, 0, -2, 1, 8, 4), (0, 14, 0), None),
        ],
        "directed": False,
        "base_fit_all": True,
    },
}


def field_name(name: str) -> str:
    return name.replace("-", "_")


def pose(offset, rotation):
    ox, oy, oz = offset
    if rotation is None:
        return f"PartPose.offset({ox}F, {oy}F, {oz}F)"
    rx, ry, rz = rotation
    return f"PartPose.offsetAndRotation({ox}F, {oy}F, {oz}F, {rx}F, {ry}F, {rz}F)"


def generate_class(name: str, spec: dict) -> str:
    fields = spec["fields"]
    field_decls = "\n".join(
        f"    private final ModelPart {field_name(f[0])};" for f in fields
    )
    ctor_assign = "\n".join(
        f"        this.{field_name(f[0])} = root.getChild(\"{f[0]}\");" for f in fields
    )
    mesh_lines = []
    for f in fields:
        part_name, u, v, box, offset, rotation = f
        x, y, z, w, h, d = box
        mesh_lines.append(
            f"        rootDef.addOrReplaceChild(\n"
            f"                \"{part_name}\",\n"
            f"                CubeListBuilder.create().texOffs({u}, {v}).addBox({x}F, {y}F, {z}F, {w}F, {h}F, {d}F),\n"
            f"                {pose(offset, rotation)});"
        )

    if spec.get("railgun"):
        setup = """        base.xRot = state.baseFitRotationX();
        base.yRot = state.baseFitRotationZ();
        float rotationX = state.rotationX();
        float rotationZ = state.rotationZ();
        for (ModelPart part : new ModelPart[] {
                barrelTop, barrelBot, barrelRight, barrelLeft, bodyBot, bodyTop, binder, rightGuard, leftGuard, guardBinder
        }) {
            part.xRot = rotationX;
            part.yRot = rotationZ;
        }"""
    elif spec.get("base_fit_all"):
        parts = ", ".join(field_name(f[0]) for f in fields)
        setup = f"""        float fitX = state.baseFitRotationX();
        float fitZ = state.baseFitRotationZ();
        for (ModelPart part : new ModelPart[] {{{parts}}}) {{
            part.xRot = fitX;
            part.yRot = fitZ;
        }}"""
    else:
        aim_parts = [field_name(f[0]) for f in fields if f[0] not in {"base", "pole", "box_under", "box_left", "box_right", "cross_bar"}]
        aim_array = ", ".join(aim_parts)
        setup = f"""        base.xRot = state.baseFitRotationX();
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
        for (ModelPart part : new ModelPart[] {{{aim_array}}}) {{
            part.xRot = rotationX;
            part.yRot = rotationZ;
        }}"""
        # fix field names for standard parts
        setup = setup.replace("boxUnder", "boxUnder").replace("boxLeft", "boxLeft").replace("boxRight", "boxRight").replace("crossBar", "crossBar")
        # Need camelCase for java fields
        setup = setup.replace("box_under", "boxUnder").replace("box_left", "boxLeft").replace("box_right", "boxRight").replace("cross_bar", "crossBar")

    # Fix ctor and fields to use camelCase
    def to_java(name: str) -> str:
        parts = name.split("_")
        return parts[0] + "".join(p.capitalize() for p in parts[1:])

    field_decls = "\n".join(
        f"    private final ModelPart {to_java(f[0])};" for f in fields
    )
    ctor_assign = "\n".join(
        f"        this.{to_java(f[0])} = root.getChild(\"{f[0]}\");" for f in fields
    )

    if spec.get("railgun"):
        setup = """        base.xRot = state.baseFitRotationX();
        base.yRot = state.baseFitRotationZ();
        float rotationX = state.rotationX();
        float rotationZ = state.rotationZ();
        barrelTop.xRot = rotationX;
        barrelTop.yRot = rotationZ;
        barrelBot.xRot = rotationX;
        barrelBot.yRot = rotationZ;
        barrelRight.xRot = rotationX;
        barrelRight.yRot = rotationZ;
        barrelLeft.xRot = rotationX;
        barrelLeft.yRot = rotationZ;
        bodyBot.xRot = rotationX;
        bodyBot.yRot = rotationZ;
        bodyTop.xRot = rotationX;
        bodyTop.yRot = rotationZ;
        binder.xRot = rotationX;
        binder.yRot = rotationZ;
        rightGuard.xRot = rotationX;
        rightGuard.yRot = rotationZ;
        leftGuard.xRot = rotationX;
        leftGuard.yRot = rotationZ;
        guardBinder.xRot = rotationX;
        guardBinder.yRot = rotationZ;"""
    elif spec.get("base_fit_all"):
        java_fields = [to_java(f[0]) for f in fields]
        setup = f"""        float fitX = state.baseFitRotationX();
        float fitZ = state.baseFitRotationZ();
        for (ModelPart part : new ModelPart[] {{ {", ".join(java_fields)} }}) {{
            part.xRot = fitX;
            part.yRot = fitZ;
        }}"""
    else:
        aim_parts = [to_java(f[0]) for f in fields if f[0] not in {"base", "pole", "box_under", "box_left", "box_right", "cross_bar"}]
        setup = f"""        base.xRot = state.baseFitRotationX();
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
        for (ModelPart part : new ModelPart[] {{ {", ".join(aim_parts)} }}) {{
            part.xRot = rotationX;
            part.yRot = rotationZ;
        }}"""

    return HEADER + f"""public class {name} extends PartModel implements AnimatedTurretModel {{
{field_decls}

    public {name}(ModelPart root) {{
        super(root);
{ctor_assign}
    }}

    public static LayerDefinition createBodyLayer() {{
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition rootDef = mesh.getRoot();
{chr(10).join(mesh_lines)}
        return LayerDefinition.create(mesh, 64, 64);
    }}

    @Override
    public void setupAnim(DirectedTurretModelState state) {{
{setup}
    }}

    @Override
    public PartModel asPartModel() {{
        return this;
    }}
}}
"""


def main() -> None:
    OUT.mkdir(parents=True, exist_ok=True)
    for name, spec in MODELS.items():
        path = OUT / f"{name}.java"
        path.write_text(generate_class(name, spec))
        print(f"Wrote {path.name}")


if __name__ == "__main__":
    main()
