package org.firstinspires.ftc.teamcode.part.intake;

public enum IntakeState {
    REST, // Do not load anything and in the resting state
    READY_FOR_TRANSFER,
    TRANSFER_SAMPLE, // Transferring the sample to the deposit part
    AUTO_DETECTING, // Auto detecting sample
    READY_TO_PICKUP, // Ready to pick up sample
    PICKED_UP, // Sample is picked up
}
