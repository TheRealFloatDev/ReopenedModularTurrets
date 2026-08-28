package com.ommods.reopenedmodularturrets.compat.jade;

import com.ommods.reopenedmodularturrets.ModConstants;
import com.ommods.reopenedmodularturrets.blockentity.TurretBaseBlockEntity;
import com.ommods.reopenedmodularturrets.blockentity.TurretHeadBlockEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public final class JadeCompat {
    private JadeCompat() {}

    public static final class TurretBaseProvider implements IBlockComponentProvider {
        public static final TurretBaseProvider INSTANCE = new TurretBaseProvider();
        public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(ModConstants.MOD_ID, "turret_base");

        @Override
        public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
            if (!(accessor.getBlockEntity() instanceof TurretBaseBlockEntity base)) {
                return;
            }
            tooltip.add(Component.literal("Tier " + base.getTier()));
            tooltip.add(Component.literal("Mode: " + base.getMachineMode().getName()));
            tooltip.add(Component.literal(base.isActive() ? "Active" : "Inactive"));
            int max = base.getEnergyStorage().getMaxEnergyStored();
            int stored = base.getEnergyStorage().getEnergyStored();
            tooltip.add(Component.literal("Energy: " + stored + "/" + max + " FE"));
            if (!base.getOwnedData().getOwnerName().isEmpty()) {
                tooltip.add(Component.literal("Owner: " + base.getOwnedData().getOwnerName()));
            }
        }

        @Override
        public ResourceLocation getUid() {
            return ID;
        }
    }

    public static final class TurretHeadProvider implements IBlockComponentProvider {
        public static final TurretHeadProvider INSTANCE = new TurretHeadProvider();
        public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(ModConstants.MOD_ID, "turret_head");

        @Override
        public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
            if (!(accessor.getBlockEntity() instanceof TurretHeadBlockEntity head)) {
                return;
            }
            tooltip.add(Component.literal("Turret: " + head.getKind().name()));
        }

        @Override
        public ResourceLocation getUid() {
            return ID;
        }
    }
}
