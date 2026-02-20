package com.ommods.openmodularturrets.content.block.turret_bases;

import com.ommods.omlib.util.OMTarget;
import com.ommods.openmodularturrets.content.item.ammo.AmmoStorageLogic;
import com.ommods.openmodularturrets.content.block.turrets.Turret;

import java.util.ArrayList;
import java.util.List;

public class TurretBaseLogic {
    private final AmmoStorageLogic ammoStorageLogic;
    private List<Turret> turrets = new ArrayList<>();
    private int tier;
    private int range = 0;

    public TurretBaseLogic(int tier) {
        this.tier = tier;
        this.ammoStorageLogic = new AmmoStorageLogic(Math.toIntExact(Math.round(Math.pow(2D,tier +2D))) * 128);
    }

    private List<OMTarget> getTargets(TurretBaseBlockEntity turretBaseBlockEntity) {
        return null;
    }

    public List<Turret> getTurrets() {
        return turrets;
    }

    public void setTurrets(List<Turret> turrets) {
        this.turrets = turrets;
    }

    public void addTurret(Turret turret) {
        turrets.add(turret);
        if (turret.getRange() > range) {
            range = turret.getRange();
        }
    })

    public void removeTurret(Turret turret) {
        try {
            turrets.remove(turret);
        } catch (Exception e) {

        }
    }

    public AmmoStorageLogic getAmmoStorageLogic() {
        return ammoStorageLogic;
    }

    public int getTier() {
        return tier;
    }

    public void setTier(int tier) {
        this.tier = tier;
    }
}
