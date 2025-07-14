package org.firstinspires.ftc.teamcode.part.intake;

public enum IntakeState {
    REST, // Do not load anything and in the resting state
    READY_FOR_TRANSFER,
    TRANSFER, // Transferring the sample/specimen to the deposit part
    AUTO_DETECTING, // Auto detecting sample
    READY_FOR_PICKUP, // Ready to pick up sample
    PICKED_UP, // Sample is picked up
    DROP_SAMPLE, // Drop sample
}
