package com.volodya.ballisticmovement;

public final class FrameInfo {
    private final double verticalVelocity;
    private final double x;
    private final double y;

    FrameInfo(double x, double y, double velocity) {
        this.x = x;
        this.y = y;
        this.verticalVelocity = velocity;
    }
    double getX() {
        return x;
    }
    double getY() {
        return y;
    }
    double getVelocity() {
        return verticalVelocity;
    }
}
