package com.visualizer.meepmeepvisualizer;

import com.acmerobotics.roadrunner.Pose2d;

public class Constants {
    public static Pose2d initialPose = new Pose2d(8, -64, Math.toRadians(90));
    public static Pose2d samplePickUpPose = new Pose2d(8, -32, Math.toRadians(90));
    public static Pose2d specimenReadyPose = new Pose2d(32, -60, Math.toRadians(90));
    public static Pose2d specimenPose = new Pose2d(32, -64, Math.toRadians(90));

    public static double sampleDirection1 = Math.toRadians(60);
    public static double sampleDirection2 = Math.toRadians(45);
    public static double sampleDirection3 = Math.toRadians(30);
    public static double observationZoneDirection = Math.toRadians(-45);
    public static Pose2d samplePushPose = new Pose2d(38, -48, sampleDirection1);

    public static double sampleDistance1 = 30;
    public static double sampleDistance2 = 40;
    public static double sampleDistance3 = 50;
    public static double observationZoneDistance = 30;

    public static double standardVelocity = 30;
    public static double specimenVelocity = 10;

    public static double DELAY_FOR_SAMPLE_DETECTION_CHECK = 1.0;

    public static double DELAY_FOR_LINEAR_SLIDE_MOVEMENT = 1.0;
}