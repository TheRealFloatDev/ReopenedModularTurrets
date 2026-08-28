package com.ommods.reopenedmodularturrets.compat;

import com.ommods.reopenedmodularturrets.api.ownership.AccessLevel;
import com.ommods.reopenedmodularturrets.blockentity.TurretBaseBlockEntity;
import com.ommods.reopenedmodularturrets.core.MachineMode;
import net.minecraft.core.Direction;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ComputerAccess {
    public static final String DEACTIVATED = "Computer access deactivated!";
    public static final String WRONG_PARAMS = "Wrong parameters!";
    public static final String NOT_FOUND = "Not found!";
    public static final String INVALID_ACCESS = "Invalid Access Level!";

    private ComputerAccess() {}

    public static boolean ensureAccessible(TurretBaseBlockEntity base) {
        return base.isComputerAccessible();
    }

    public static Object[] getOwner(TurretBaseBlockEntity base) {
        return new Object[] {base.getOwnedData().getOwnerName()};
    }

    public static Object[] isAttacksPlayers(TurretBaseBlockEntity base) {
        return new Object[] {base.isAttackPlayers()};
    }

    public static Object[] setAttacksPlayers(TurretBaseBlockEntity base, boolean value) {
        base.setAttackPlayers(value);
        return new Object[] {true};
    }

    public static Object[] isAttacksMobs(TurretBaseBlockEntity base) {
        return new Object[] {base.isAttackMobs()};
    }

    public static Object[] setAttacksMobs(TurretBaseBlockEntity base, boolean value) {
        base.setAttackMobs(value);
        return new Object[] {true};
    }

    public static Object[] isAttacksNeutrals(TurretBaseBlockEntity base) {
        return new Object[] {base.isAttackNeutral()};
    }

    public static Object[] setAttacksNeutrals(TurretBaseBlockEntity base, boolean value) {
        base.setAttackNeutral(value);
        return new Object[] {true};
    }

    public static Object[] getTrustedPlayers(TurretBaseBlockEntity base) {
        return new Object[] {base.getTrustedPlayers().asListMap()};
    }

    public static Object[] getTrustedPlayer(TurretBaseBlockEntity base, String name) {
        return base.getTrustedPlayers().getTrusted(name)
                .map(player -> new Object[] {player.asMap()})
                .orElse(new Object[] {});
    }

    public static Object[] addTrustedPlayer(TurretBaseBlockEntity base, String name, Integer accessLevel) {
        if (name == null || name.isBlank()) {
            return new Object[] {WRONG_PARAMS};
        }
        if (!base.addTrustedPlayer(name)) {
            return new Object[] {"Name not valid!"};
        }
        if (accessLevel != null) {
            if (accessLevel < 0 || accessLevel > 3) {
                return new Object[] {INVALID_ACCESS};
            }
            base.changeTrustedAccessLevel(name, AccessLevel.fromLevel(accessLevel));
        }
        return new Object[] {true};
    }

    public static Object[] removeTrustedPlayer(TurretBaseBlockEntity base, String name) {
        if (name == null || name.isBlank()) {
            return new Object[] {WRONG_PARAMS};
        }
        base.removeTrustedPlayer(name);
        return new Object[] {true};
    }

    public static Object[] changeAccessLevel(TurretBaseBlockEntity base, String name, int level) {
        if (base.getTrustedPlayers().getTrusted(name).isEmpty()) {
            return new Object[] {NOT_FOUND};
        }
        if (level < 0 || level > 3) {
            return new Object[] {INVALID_ACCESS};
        }
        base.changeTrustedAccessLevel(name, AccessLevel.fromLevel(level));
        return new Object[] {true};
    }

    public static Object[] getActive(TurretBaseBlockEntity base) {
        return new Object[] {base.isActive()};
    }

    public static Object[] getMode(TurretBaseBlockEntity base) {
        return new Object[] {base.getMachineMode().getName()};
    }

    public static Object[] getRedstone(TurretBaseBlockEntity base) {
        return new Object[] {base.isRedstonePowered()};
    }

    public static Object[] setMode(TurretBaseBlockEntity base, String modeName) {
        for (MachineMode mode : MachineMode.values()) {
            if (mode.getName().equalsIgnoreCase(modeName)) {
                base.setMachineMode(mode);
                return new Object[] {true};
            }
        }
        return new Object[] {WRONG_PARAMS};
    }

    public static Object[] getType() {
        return new Object[] {"turret_base"};
    }

    public static Object[] setAllAutoForceFire(TurretBaseBlockEntity base, boolean state) {
        base.setAllTurretsForceFire(state);
        return new Object[] {true};
    }

    public static Object[] setTurretAutoForceFire(TurretBaseBlockEntity base, String facingName, boolean state) {
        Direction direction = parseDirection(facingName);
        if (direction == null) {
            return new Object[] {WRONG_PARAMS};
        }
        return new Object[] {base.setTurretForceFire(direction, state)};
    }

    public static Object[] forceShootAllTurrets(TurretBaseBlockEntity base) {
        return new Object[] {base.forceShootAllTurrets()};
    }

    public static Object[] forceShootTurret(TurretBaseBlockEntity base, String facingName) {
        Direction direction = parseDirection(facingName);
        if (direction == null) {
            return new Object[] {WRONG_PARAMS};
        }
        return new Object[] {base.forceShootTurret(direction)};
    }

    public static Object[] setAllYawPitch(TurretBaseBlockEntity base, double yaw, double pitch) {
        base.setAllTurretsYawPitch((float) yaw, (float) pitch);
        return new Object[] {true};
    }

    public static Object[] setTurretYawPitch(TurretBaseBlockEntity base, String facingName, double yaw, double pitch) {
        Direction direction = parseDirection(facingName);
        if (direction == null) {
            return new Object[] {WRONG_PARAMS};
        }
        return new Object[] {base.setTurretYawPitch(direction, (float) yaw, (float) pitch)};
    }

    public static Object[] getMaxEnergyStorage(TurretBaseBlockEntity base) {
        return new Object[] {base.getEnergyStorage().getMaxEnergyStored()};
    }

    public static Object[] getCurrentEnergyStorage(TurretBaseBlockEntity base) {
        return new Object[] {base.getEnergyStorage().getEnergyStored()};
    }

    private static Direction parseDirection(String name) {
        if (name == null) {
            return null;
        }
        return switch (name.toLowerCase()) {
            case "down" -> Direction.DOWN;
            case "up" -> Direction.UP;
            case "north" -> Direction.NORTH;
            case "south" -> Direction.SOUTH;
            case "west" -> Direction.WEST;
            case "east" -> Direction.EAST;
            default -> null;
        };
    }

    public static Map<String, String> methodNames() {
        Map<String, String> names = new HashMap<>();
        names.put("getOwner", "getOwner");
        names.put("isAttacksPlayers", "isAttacksPlayers");
        names.put("setAttacksPlayers", "setAttacksPlayers");
        names.put("isAttacksMobs", "isAttacksMobs");
        names.put("setAttacksMobs", "setAttacksMobs");
        names.put("isAttacksNeutrals", "isAttacksNeutrals");
        names.put("setAttacksNeutrals", "setAttacksNeutrals");
        names.put("getTrustedPlayers", "getTrustedPlayers");
        names.put("getTrustedPlayer", "getTrustedPlayer");
        names.put("addTrustedPlayer", "addTrustedPlayer");
        names.put("removeTrustedPlayer", "removeTrustedPlayer");
        names.put("changeAccessLevel", "changeAccessLevel");
        names.put("getActive", "getActive");
        names.put("getMode", "getMode");
        names.put("getRedstone", "getRedstone");
        names.put("setMode", "setMode");
        names.put("getType", "getType");
        names.put("setAllAutoForceFire", "setAllAutoForceFire");
        names.put("setTurretAutoForceFire", "setTurretAutoForceFire");
        names.put("forceShootAllTurrets", "forceShootAllTurrets");
        names.put("forceShootTurret", "forceShootTurret");
        names.put("setAllYawPitch", "setAllYawPitch");
        names.put("setTurretYawPitch", "setTurretYawPitch");
        names.put("getMaxEnergyStorage", "getMaxEnergyStorage");
        names.put("getCurrentEnergyStorage", "getCurrentEnergyStorage");
        return names;
    }
}
