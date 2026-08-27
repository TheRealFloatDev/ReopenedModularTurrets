package com.ommods.reopenedmodularturrets.client.model;

public interface AnimatedTurretModel {
    void setupAnim(DirectedTurretModelState state);

    PartModel asPartModel();
}
