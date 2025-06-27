package org.firstinspires.ftc.teamcode.vision;

import java.util.List;

public class Sample {
    public enum SampleColor {YELLOW, BLUE, RED}
    public enum State {UNKNOWN, DETECTED, FAILED}

    public SampleColor color;

    double x, y;
    double angle;
    State state;
    List<List<Double>> corners;
    double x_angle, y_angle;

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

    int getColorIndex() {
        switch (color) {
            case YELLOW:
                return 0;
            case BLUE:
                return 1;
            case RED:
                return 2;
            default:
                return -1;
        }
    }
}
