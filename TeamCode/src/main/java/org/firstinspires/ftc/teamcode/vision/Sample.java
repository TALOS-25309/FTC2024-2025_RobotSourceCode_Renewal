package org.firstinspires.ftc.teamcode.vision;

import org.firstinspires.ftc.teamcode.features.Vision;

public class Sample {
    public enum SampleColor {YELLOW, BLUE, RED}
    public enum State {UNKNOWN, DETECTED, FAILED}

    public SampleColor color;

    double x, y;
    double angle;
    State state;

    public Sample() {
        this.color = SampleColor.YELLOW; // Default color
        this.x = 0; // Default x coordinate
        this.y = 0; // Default y coordinate
        this.angle = 0.0; // Default angle
        this.state = State.UNKNOWN; // Initial state
    }

    public State state() {
        return state;
    }

    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }
    public double getAngle() {
        return angle;
    }

    String getLabel() {
        switch (color) {
            case YELLOW:
                return "yellow";
            case BLUE:
                return "blue";
            case RED:
                return "red";
            default:
                return "unknown";
        }
    }
}
