package com.volodya.ballisticmovement;

import com.volodya.ballisticmovement.types.FrameInfo;
import com.volodya.ballisticmovement.types.Pair;

import java.util.ArrayList;

public class BallisticModel {
    final ArrayList<FrameInfo> frames = new ArrayList<>();
    private final double horizontalVelocity;

    public BallisticModel(double angle, double height, double velocity) {
        horizontalVelocity = velocity * Math.cos(angle);
        frames.add(new FrameInfo(0, height, velocity * Math.sin(angle)));
    }

    public boolean isXVelocityPositive() {
        return horizontalVelocity > 0;
    }

    public FrameInfo getFirstFrame() {
        return frames.getFirst();
    }
    public FrameInfo getFrame(int id) {
        return frames.get(id);
    }
    public FrameInfo getLastFrame() {
        return frames.getLast();
    }

    public double getX() {
        return getLastFrame().getX();
    }
    public double getY() {
        return getLastFrame().getY();
    }
    public double getVelocity() {
        return getLastFrame().getVelocity();
    }

    public double getInitialHeight() {
        return getFirstFrame().getY();
    }
    public double getHorizontalVelocity() {
        return horizontalVelocity;
    }
    public double getInitialYVelocity() {
        return getFirstFrame().getVelocity();
    }

    public double calculateTheoreticalTime() {
        var initialYVelocity = getInitialYVelocity();
        var initialHeight = getInitialHeight();
        // (Vy0 + (Vy0^2 + 2gh)) / g
        return (
                (initialYVelocity +
                        Math.sqrt(Math.pow(initialYVelocity, 2) +
                        2 * Physics.G * initialHeight)
                )
                / Physics.G
        );
    }
    public double calculateTheoreticalDistance() {
        // Vx * t
        return horizontalVelocity * calculateTheoreticalTime();
    }
    public double calculateTheoreticalHeight() {
        var initialYVelocity = getInitialYVelocity();
        var theoreticalTime = calculateTheoreticalTime();
        // Vy0 * t
        return initialYVelocity * theoreticalTime;
    }

    public double getExperimentalTimeLowerBound() {
        return Math.max(0.0, ((double) (frames.size() - 2)) / BMApplication.FPS);
    }
    public double getExperimentalTimeUpperBound() {
        return ((double) (frames.size() - 1)) / BMApplication.FPS;
    }
    public Pair<Double, Double> getExperimentalTime() {
        return new Pair<>(getExperimentalTimeLowerBound(), getExperimentalTimeUpperBound());
    }

    private double calculateNewYVelocity() {
        return getLastFrame().getVelocity() - Physics.G / BMApplication.FPS;
    }
    private double calculateNewX() {
        return getLastFrame().getX() + horizontalVelocity / BMApplication.FPS;
    }
    private double calculateNewY() {
        return getLastFrame().getY() + calculateNewYVelocity() / BMApplication.FPS;
    }

    public boolean isFlying() {
        return getLastFrame().getVelocity() > 0 || getLastFrame().getY() > 0;
    }
    public void applyVelocity() {
        frames.add(new FrameInfo(calculateNewX(), calculateNewY(), calculateNewYVelocity()));
    }
}
