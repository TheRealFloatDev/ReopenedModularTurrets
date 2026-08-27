package com.ommods.reopenedmodularturrets.client.model;

public record DirectedTurretModelState(float rotationX, float rotationZ) {
    public static final DirectedTurretModelState IDLE = new DirectedTurretModelState(0.0F, 0.0F);
}
