package com.ommods.reopenedmodularturrets.api.render;

public record LaserColor(int red, int green, int blue) {
    public LaserColor {
        red = clamp(red);
        green = clamp(green);
        blue = clamp(blue);
    }

    private static int clamp(int value) {
        return Math.max(0, Math.min(255, value));
    }
}
