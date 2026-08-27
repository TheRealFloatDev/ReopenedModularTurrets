package com.ommods.reopenedmodularturrets.entity;

import com.ommods.reopenedmodularturrets.registry.ModEntityTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class LaserBeamEntity extends Entity {
    public static final int BEAM_DURATION_TICKS = 1;

    private static final EntityDataAccessor<Float> END_X = SynchedEntityData.defineId(LaserBeamEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> END_Y = SynchedEntityData.defineId(LaserBeamEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> END_Z = SynchedEntityData.defineId(LaserBeamEntity.class, EntityDataSerializers.FLOAT);

    private int life;

    public LaserBeamEntity(EntityType<? extends LaserBeamEntity> type, Level level) {
        super(type, level);
        this.noPhysics = true;
    }

    public LaserBeamEntity(Level level, Vec3 start, Vec3 end) {
        this(ModEntityTypes.LASER_BEAM.get(), level);
        setPos(start.x, start.y, start.z);
        setEnd(end);
    }

    public Vec3 getEnd() {
        return new Vec3(entityData.get(END_X), entityData.get(END_Y), entityData.get(END_Z));
    }

    public void setEnd(Vec3 end) {
        entityData.set(END_X, (float) end.x);
        entityData.set(END_Y, (float) end.y);
        entityData.set(END_Z, (float) end.z);
    }

    public float getBeamProgress(float partialTick) {
        return Math.min(1.0F, (life + partialTick) / (float) BEAM_DURATION_TICKS);
    }

    public Vec3 getBeamTip(float partialTick) {
        Vec3 start = position();
        Vec3 end = getEnd();
        return start.lerp(end, getBeamProgress(partialTick));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(END_X, 0.0F);
        builder.define(END_Y, 0.0F);
        builder.define(END_Z, 0.0F);
    }

    @Override
    public void tick() {
        super.tick();
        if (++life > BEAM_DURATION_TICKS) {
            discard();
        }
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        entityData.set(END_X, tag.getFloat("EndX"));
        entityData.set(END_Y, tag.getFloat("EndY"));
        entityData.set(END_Z, tag.getFloat("EndZ"));
        life = tag.getInt("Life");
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putFloat("EndX", entityData.get(END_X));
        tag.putFloat("EndY", entityData.get(END_Y));
        tag.putFloat("EndZ", entityData.get(END_Z));
        tag.putInt("Life", life);
    }
}
