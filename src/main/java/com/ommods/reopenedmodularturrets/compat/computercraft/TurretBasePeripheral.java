package com.ommods.reopenedmodularturrets.compat.computercraft;

import com.ommods.reopenedmodularturrets.blockentity.TurretBaseBlockEntity;
import com.ommods.reopenedmodularturrets.compat.ComputerAccess;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import org.jetbrains.annotations.Nullable;

public class TurretBasePeripheral implements IPeripheral {
    private final TurretBaseBlockEntity base;

    public TurretBasePeripheral(TurretBaseBlockEntity base) {
        this.base = base;
    }

    @Override
    public String getType() {
        return "turret_base";
    }

    @Override
    public boolean equals(@Nullable IPeripheral other) {
        return other instanceof TurretBasePeripheral peripheral && peripheral.base == base;
    }

    private Object[] gate() throws LuaException {
        if (!ComputerAccess.ensureAccessible(base)) {
            throw new LuaException(ComputerAccess.DEACTIVATED);
        }
        return null;
    }

    @LuaFunction
    public final Object[] getOwner() throws LuaException {
        gate();
        return ComputerAccess.getOwner(base);
    }

    @LuaFunction
    public final Object[] isAttacksPlayers() throws LuaException {
        gate();
        return ComputerAccess.isAttacksPlayers(base);
    }

    @LuaFunction
    public final Object[] setAttacksPlayers(boolean value) throws LuaException {
        gate();
        return ComputerAccess.setAttacksPlayers(base, value);
    }

    @LuaFunction
    public final Object[] isAttacksMobs() throws LuaException {
        gate();
        return ComputerAccess.isAttacksMobs(base);
    }

    @LuaFunction
    public final Object[] setAttacksMobs(boolean value) throws LuaException {
        gate();
        return ComputerAccess.setAttacksMobs(base, value);
    }

    @LuaFunction
    public final Object[] isAttacksNeutrals() throws LuaException {
        gate();
        return ComputerAccess.isAttacksNeutrals(base);
    }

    @LuaFunction
    public final Object[] setAttacksNeutrals(boolean value) throws LuaException {
        gate();
        return ComputerAccess.setAttacksNeutrals(base, value);
    }

    @LuaFunction
    public final Object[] getTrustedPlayers() throws LuaException {
        gate();
        return ComputerAccess.getTrustedPlayers(base);
    }

    @LuaFunction
    public final Object[] getTrustedPlayer(String name) throws LuaException {
        gate();
        return ComputerAccess.getTrustedPlayer(base, name);
    }

    @LuaFunction
    public final Object[] addTrustedPlayer(String name, @Nullable Integer accessLevel) throws LuaException {
        gate();
        return ComputerAccess.addTrustedPlayer(base, name, accessLevel);
    }

    @LuaFunction
    public final Object[] removeTrustedPlayer(String name) throws LuaException {
        gate();
        return ComputerAccess.removeTrustedPlayer(base, name);
    }

    @LuaFunction
    public final Object[] changeAccessLevel(String name, int level) throws LuaException {
        gate();
        return ComputerAccess.changeAccessLevel(base, name, level);
    }

    @LuaFunction
    public final Object[] getActive() throws LuaException {
        gate();
        return ComputerAccess.getActive(base);
    }

    @LuaFunction
    public final Object[] getMode() throws LuaException {
        gate();
        return ComputerAccess.getMode(base);
    }

    @LuaFunction
    public final Object[] getRedstone() throws LuaException {
        gate();
        return ComputerAccess.getRedstone(base);
    }

    @LuaFunction
    public final Object[] setMode(String mode) throws LuaException {
        gate();
        return ComputerAccess.setMode(base, mode);
    }

    @LuaFunction
    public final Object[] setAllAutoForceFire(boolean state) throws LuaException {
        gate();
        return ComputerAccess.setAllAutoForceFire(base, state);
    }

    @LuaFunction
    public final Object[] setTurretAutoForceFire(String facing, boolean state) throws LuaException {
        gate();
        return ComputerAccess.setTurretAutoForceFire(base, facing, state);
    }

    @LuaFunction
    public final Object[] forceShootAllTurrets() throws LuaException {
        gate();
        return ComputerAccess.forceShootAllTurrets(base);
    }

    @LuaFunction
    public final Object[] forceShootTurret(String facing) throws LuaException {
        gate();
        return ComputerAccess.forceShootTurret(base, facing);
    }

    @LuaFunction
    public final Object[] setAllYawPitch(double yaw, double pitch) throws LuaException {
        gate();
        return ComputerAccess.setAllYawPitch(base, yaw, pitch);
    }

    @LuaFunction
    public final Object[] setTurretYawPitch(String facing, double yaw, double pitch) throws LuaException {
        gate();
        return ComputerAccess.setTurretYawPitch(base, facing, yaw, pitch);
    }

    @LuaFunction
    public final Object[] getMaxEnergyStorage() throws LuaException {
        gate();
        return ComputerAccess.getMaxEnergyStorage(base);
    }

    @LuaFunction
    public final Object[] getCurrentEnergyStorage() throws LuaException {
        gate();
        return ComputerAccess.getCurrentEnergyStorage(base);
    }
}
