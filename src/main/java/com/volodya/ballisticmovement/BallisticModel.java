package com.volodya.ballisticmovement;

import java.util.ArrayList;

public class BallisticModel {
    final ArrayList<FrameInfo> frames = new ArrayList<>();
    final double horizontalVelocity;
    double angle;

    BallisticModel(double angle, double height, double velocity) {
        this.angle = angle;
        horizontalVelocity = velocity * Math.cos(angle);
        frames.add(new FrameInfo(0, height, velocity * Math.sin(angle)));
    }
    private double calculateX() {
        return getLastFrame().getX() + horizontalVelocity / BMApplication.APPROX_FPS;
    }
    private double calculateY(double verticalVelocity) {
        return getLastFrame().getY() + verticalVelocity / BMApplication.APPROX_FPS;
    }
    private double calculateVelocity() {
        return getLastFrame().getVelocity() - Physics.G / BMApplication.APPROX_FPS;
    }
    boolean isFlying() {
        return getLastFrame().getVelocity() > 0 || getLastFrame().getY() > 0;
    }
    FrameInfo getLastFrame() {
        return frames.getLast();
    }
    public void applyVelocity() {
        double verticalVelocity = calculateVelocity();
        double x = calculateX();
        double y = calculateY(verticalVelocity);
        frames.add(new FrameInfo(x, y, verticalVelocity));
    }
}
