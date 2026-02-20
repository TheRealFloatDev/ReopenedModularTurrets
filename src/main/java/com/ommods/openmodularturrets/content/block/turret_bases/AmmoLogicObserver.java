package com.ommods.openmodularturrets.content.block.turret_bases;

import com.ommods.openmodularturrets.content.item.ammo.AmmoDropEvent;

import java.util.ArrayList;
import java.util.List;

public class AmmoLogicObserver {
    private List<TurretBaseBlockEntity> observers;

    public AmmoLogicObserver() {
        observers = new ArrayList<>();
    }

    public void registerObserver(TurretBaseBlockEntity observer) {
        if (!observers.contains(observer) && observer != null) {
            observers.add(observer);
        }
    }

    public void notifyObservers(AmmoDropEvent event){
        for (TurretBaseBlockEntity observer : observers) {
            if (observer != null) {
                observer.handleAmmoDropEvent(event);
            }
        }
    }
}

