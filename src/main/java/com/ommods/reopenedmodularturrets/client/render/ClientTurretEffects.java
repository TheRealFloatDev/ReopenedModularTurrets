package com.ommods.reopenedmodularturrets.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.phys.Vec3;

public final class ClientTurretEffects {
    private ClientTurretEffects() {}

    public static void spawnLightning(Vec3 from, Vec3 to) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level == null) {
            return;
        }
        int steps = 12;
        for (int i = 0; i <= steps; i++) {
            double t = i / (double) steps;
            double x = from.x + (to.x - from.x) * t + (minecraft.level.random.nextDouble() - 0.5) * 0.15;
            double y = from.y + (to.y - from.y) * t + (minecraft.level.random.nextDouble() - 0.5) * 0.15;
            double z = from.z + (to.z - from.z) * t + (minecraft.level.random.nextDouble() - 0.5) * 0.15;
            minecraft.level.addParticle(ParticleTypes.ELECTRIC_SPARK, x, y, z, 0, 0, 0);
        }
    }

    public static void spawnRay(Vec3 from, Vec3 to, int red, int green, int blue) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level == null) {
            return;
        }
        int steps = 16;
        float r = red / 255.0F;
        float g = green / 255.0F;
        float b = blue / 255.0F;
        for (int i = 0; i <= steps; i++) {
            double t = i / (double) steps;
            double x = from.x + (to.x - from.x) * t;
            double y = from.y + (to.y - from.y) * t;
            double z = from.z + (to.z - from.z) * t;
            minecraft.level.addParticle(
                    new net.minecraft.core.particles.DustParticleOptions(new org.joml.Vector3f(r, g, b), 0.8F),
                    x, y, z, 0, 0, 0
            );
        }
    }
}
