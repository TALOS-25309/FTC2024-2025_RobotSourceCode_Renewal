package org.firstinspires.ftc.teamcode.opmode.auto;

public class GlobalConstants {
    public static final double TILE_MM_SIZE = 599; // Width of the field in mm
    public static final double TILE_SIZE = 24; // Tile size of road runner unit
    public static final double ROBOT_X_OFFSET = (TILE_SIZE * 160.0) / TILE_MM_SIZE;
    public static final double ROBOT_Y_OFFSET = (TILE_SIZE * 172.5) / TILE_MM_SIZE;

    public static final double SUBMERSIBLE_Y_POSITION = 1245 * TILE_SIZE / TILE_MM_SIZE - (TILE_SIZE * 3);
    public static final double UNIT_CM = 10 * TILE_SIZE / TILE_MM_SIZE;
}
