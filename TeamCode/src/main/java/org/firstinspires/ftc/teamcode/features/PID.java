package org.firstinspires.ftc.teamcode.features;

import com.acmerobotics.dashboard.config.Config;

import org.apache.commons.math3.analysis.function.Max;

@Config
public class PID {
    public double p;
    public double i;
    public double d;
    public PID(double P, double I, double D){
        p=P;
        i=I;
        d=D;
    }
    double integral = 0;
    long lastLoopTime = System.nanoTime();
    double lastError = 0;
    int counter = 0;
    double loopTime = 0.0;
    double lastDerivative = 0.0;

    public static double LOW_PASS_FILTER = 0.01;

    public void resetIntegral() {
        integral = 0;
    }
    public double getIntegral() { return integral; }

    public double update(double error, double min, double max){
        if (counter == 0) {
            lastLoopTime = System.nanoTime() - 10000000;
        }

        long currentTime = System.nanoTime();
        loopTime = (currentTime - lastLoopTime)/1000000000.0;
        lastLoopTime = currentTime; // lastLoopTime's start time

        double proportion = p * error;
        integral += error * i * loopTime;

        double rawDerivative = (error - lastError) / loopTime;
        if (counter == 0) {
            lastDerivative = (error - lastError) / loopTime;
        } else {
            lastDerivative = (1.0-LOW_PASS_FILTER) * lastDerivative + LOW_PASS_FILTER * rawDerivative; // 1차 low-pass filter
        }
        double derivative = d * lastDerivative;

        lastError = error;
        counter ++;

        double value = proportion + integral + derivative;
        if (value < min) {
            value = min;
            resetIntegral();
        } else if (value > max) {
            value = max;
            resetIntegral();
        }
        return value;
    }

    public void updatePID(double p, double i, double d) {
        this.p = p;
        this.i = i;
        this.d = d;
    }
}