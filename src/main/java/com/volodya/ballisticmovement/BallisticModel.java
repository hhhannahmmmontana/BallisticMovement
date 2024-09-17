package com.volodya.ballisticmovement;

import com.volodya.ballisticmovement.types.FrameInfo;
import com.volodya.ballisticmovement.types.Pair;

import java.util.ArrayList;

public final class BallisticModel {
    private final ArrayList<FrameInfo> frames = new ArrayList<>();
    private double horizontalVelocity;
    private int fps;

    public BallisticModel(double angle, double height, double velocity, int fps) {
        updateInitData(angle, height, velocity, fps);
    }

    public void updateInitData(double angle, double height, double velocity, int fps) {
        frames.clear();
        angle = Math.toRadians(angle);
        horizontalVelocity = velocity * Math.cos(angle);
        this.frames.add(new FrameInfo(0, height, velocity * Math.sin(angle)));
        this.fps = fps;
    }

    ArrayList<FrameInfo> getFrames() {
        return frames;
    }

    int getFps() {
        return fps;
    }
    private void setFps(int fps) {
        this.fps = fps;
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
    public FrameInfo getPrevFrame() {
        return frames.get(frames.size() - 2);
    }
    public int getFrameId() {
        return frames.size() - 1;
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
        var initialHeight = getInitialHeight();
        // h + Vy0^2 / 2g
        return initialHeight + Math.pow(initialYVelocity, 2) / (2 * Physics.G);
    }
    public int calculateMaxFramesAmount() {
        return (int) Math.ceil(calculateTheoreticalTime() * fps);
    }

    public double getExperimentalTimeLowerBound() {
        return Math.max(0.0, ((double) (frames.size() - 2)) / fps);
    }
    public double getExperimentalTimeUpperBound() {
        return ((double) (frames.size() - 1)) / fps;
    }
    public Pair<Double, Double> getExperimentalTime() {
        return new Pair<>(getExperimentalTimeLowerBound(), getExperimentalTimeUpperBound());
    }

    private double calculateNewYVelocity() {
        return getLastFrame().getVelocity() - Physics.G / fps;
    }
    private double calculateNewX() {
        return getLastFrame().getX() + horizontalVelocity / fps;
    }
    private double calculateNewY() {
        return getLastFrame().getY() + calculateNewYVelocity() / fps;
    }

    public boolean isFlying() {
        return getLastFrame().getVelocity() > 0 || getLastFrame().getY() > 0;
    }
    public boolean isFalling() {
        return getPrevFrame().getY() > getLastFrame().getY();
    }
    boolean peak() {
        return getLastFrame().getVelocity() >= 0 && calculateNewYVelocity() < 0;
    }

    public void applyVelocity() {
        frames.add(new FrameInfo(calculateNewX(), calculateNewY(), calculateNewYVelocity()));
    }
}
