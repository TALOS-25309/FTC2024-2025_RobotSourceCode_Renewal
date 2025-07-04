package org.firstinspires.ftc.teamcode.part;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public interface Part {
    void init(HardwareMap hardwareMap);
    void update();
    void stop();
}
