package org.firstinspires.ftc.teamcode.part.deposit;

public enum DepositState {
    REST, // Ready to deposit
    TRANSFER, // Transferring the sample/specimen from intake part
    LOAD_SAMPLE, // Loading the sample to the deposit part
    LOAD_SPECIMEN, // Loading the specimen to the deposit part
    READY_FOR_DEPOSIT_BASKET, // Ready to deposit the sample to the basket
    READY_FOR_DEPOSIT_SPECIMEN, // Ready to deposit the specimen
    READY_FOR_PICKUP, // Ready to pick up sample
    READY_FOR_DISCARD, // Ready to discard the sample or specimen
    ASCENDING, // Ascending to the climbing position
}
