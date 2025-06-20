package org.firstinspires.ftc.teamcode.part.intake;

import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.part.Part;

public class Intake implements Part {
    Telemetry telemetry;
    Commands commandProcessor;

    public void init(HardwareMap hardwareMap, Telemetry telemetry) {
        this.telemetry = telemetry;
        this.commandProcessor = new Commands(this);
    }

    public void update() {
        // Update intake state here
    }

    public void stop() {
        // Stop intake operations here
    }
}
