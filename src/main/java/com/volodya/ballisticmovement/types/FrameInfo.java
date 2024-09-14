package com.volodya.ballisticmovement.types;

public final class FrameInfo {
    private final double verticalVelocity;
    private final double x;
    private final double y;

    public FrameInfo(double x, double y, double velocity) {
        this.x = x;
        this.y = y;
        this.verticalVelocity = velocity;
    }
    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }
    public double getVelocity() {
        return verticalVelocity;
    }
}
